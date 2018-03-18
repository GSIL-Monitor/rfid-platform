package com.casesoft.dmc.extend.msg.websocket;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.extend.msg.websocket.entity.*;
import org.apache.log4j.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;


import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.core.Constant;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class WsMsgWebSocketHandler implements WebSocketHandler {
    private static final Logger logger = Logger
            .getLogger(WsMsgWebSocketHandler.class);
    
    public static final Map<String, WebSocketSession> userSocketSessionMap;

    public static final Map<String, WebSocketSession> deviceSessionMap;
    public static final Map<String, OnlineUserVo> onlineUserMap;

    static {
        userSocketSessionMap = new HashMap<>();
        deviceSessionMap = new HashMap<>();
        onlineUserMap = new HashMap<>();
    }


    private WsMessageService wsMessageService;


    private SessionManager sessionManager;

    // 连接建立后处理
    @Override
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
        logger.debug("链接成功......");
        System.out.println("链接成功......");
        Object temp = session.getAttributes()
                .get(Constant.Session.User_Session);
        final PushUser user = (PushUser) temp; // 用户信息
        user.setWebSocketSession(session);
        user.setId(session.getId());
        sessionManager.put(user);

        OnlineUserVo onlineUserVo = new OnlineUserVo();
        onlineUserVo.setIpAddr(user.getIpAddr());
        onlineUserVo.setCode(user.getUserCode());
        onlineUserVo.setOnlineTime(CommonUtil.getDateString(user.getOnlineTime(),"yyyy-MM-dd HH:mm:ss"));
        onlineUserVo.setSessionId(user.getId());
        onlineUserVo.setType(user.getType());
        onlineUserMap.put(user.getUserCode(),onlineUserVo);


        if(user.getType().equals(PushUser.Device)) {
            String deviceId = user.getUserCode();
            if(! deviceSessionMap.containsKey(deviceId)) {
                deviceSessionMap.put(deviceId,session);
            }
            WsMessage wsMessage = new WsMessage();
            wsMessage.setType(MessageType.Notice_Online);
            wsMessage.setFromCode(user.getUserCode());
            wsMessage.setFromName(user.getIpAddr());
            wsMessage.setContent("设备"+user.getUserCode()+"上线");
            broadcast(new TextMessage(JSON.toJSONString(wsMessage)));
        } else {
            String userCode = user.getUserCode();
            if(! userSocketSessionMap.containsKey(userCode)) {
                userSocketSessionMap.put(userCode,session);
            }
        }

//
//        if (temp != null) {
//            try {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        List<WsMessage> wsMessages=wsMessageService.findAcceptUnSuccessMsg(user.getUserCode());
//                        sessionManager.sendMessageToUser(wsMessages);
//                        //sessionManager.sendMessageToUser(sessionManager.getCountMessage(user.getUserCode()));
//                    }
//                }).start();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//            }
//        }

    }

    // 接收消息处理消息，并发送出去
    @Override
    public void handleMessage(final WebSocketSession webSocketSession,
                              final WebSocketMessage<?> webSocketMessage) throws Exception {
        System.out.println("handleMessage......");
       
            try {
                String msg = webSocketMessage.getPayload().toString();
                WsMessage wsMessage = JSON.parseObject(msg,
                        WsMessage.class);
                switch(wsMessage.getType()) {
                    case MessageType.Update_Product:
                        broadcastDeviceInfo(new TextMessage(JSON.toJSONString(wsMessage)));
                        break;
                    case MessageType.Notice_Msg:
                        if(CommonUtil.isBlank(wsMessage.getToCode())
                                || PushUser.All_User_Code.equals(wsMessage.getToCode())) {
                            broadcast(new TextMessage(JSON.toJSONString(wsMessage)));
                            broadcastDeviceInfo(new TextMessage(JSON.toJSONString(wsMessage)));
                        } else {
                            OnlineUserVo onlineUserVo = onlineUserMap.get(wsMessage.getToCode());
                            if(onlineUserVo.getType().equals(PushUser.Device)) {
                                sendMsgToDevice(wsMessage.getToCode(),new TextMessage(JSON.toJSONString(wsMessage)));
                            } else {
                                sendMsgToUser(wsMessage.getToCode(),new TextMessage(JSON.toJSONString(wsMessage)));
                            }
                        }
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
      

    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession,
                                     Throwable throwable) throws Exception {
        if (webSocketSession.isOpen()) {
            webSocketSession.close();
        }
        logger.debug("链接出错，关闭链接......");
        PushUser user = (PushUser) webSocketSession.getAttributes().get(
                Constant.Session.User_Session);
        if(user.getType().equals(PushUser.Device)) {
            deviceSessionMap.remove(user.getUserCode());
        } else {
            sessionManager.remove(user);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession,
                                      CloseStatus closeStatus) throws Exception {
        logger.debug("链接关闭......" + closeStatus.toString());
        PushUser user = (PushUser) webSocketSession.getAttributes().get(
                Constant.Session.User_Session);
        if(user.getType().equals(PushUser.Device)) {
            deviceSessionMap.remove(user.getUserCode());
        } else {
            sessionManager.remove(user);
        }

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    
    /**
     * 给所有在线用户发送消息
     *
     * @param message
     * @throws IOException
     */
    public void broadcast(final TextMessage message) throws IOException {
        Iterator<Entry<String, WebSocketSession>> it = userSocketSessionMap
                .entrySet().iterator();

        // 多线程群发
        while (it.hasNext()) {

            final Entry<String, WebSocketSession> entry = it.next();
            sendMsgToSession(entry,message);


        }
    }

    private void sendMsgToSession(final Entry<String, WebSocketSession> entry, final TextMessage message) {
          sendMsgToSession(entry.getValue(),message);
    }

    public void sendMsgToSession(final WebSocketSession socketSession, final TextMessage message) {
        if (socketSession.isOpen()) {
            try {
                socketSession.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 广播所有设备信息
     * @param message
     * @throws IOException
     */
    public void broadcastDeviceInfo(final TextMessage message) throws IOException {
        Iterator<Entry<String, WebSocketSession>> it = deviceSessionMap
                .entrySet().iterator();

        // 多线程群发
        while (it.hasNext()) {

            final Entry<String, WebSocketSession> entry = it.next();

            sendMsgToSession(entry,message);

        }
    }

    public void sendMsgToUser(String userCode, final TextMessage message) {
        sendMsgToSession(userSocketSessionMap.get(userCode),message);
    }
    public void sendMsgToDevice(String deviceId, final TextMessage message) {
        sendMsgToSession(deviceSessionMap.get(deviceId),message);
    }

    public WsMessageService getWsMessageService() {
        return wsMessageService;
    }

    public void setWsMessageService(WsMessageService wsMessageService) {
        this.wsMessageService = wsMessageService;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
}
