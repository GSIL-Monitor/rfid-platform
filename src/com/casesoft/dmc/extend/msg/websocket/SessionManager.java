package com.casesoft.dmc.extend.msg.websocket;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.extend.msg.PushUtil;
import com.casesoft.dmc.extend.msg.websocket.entity.PushUser;
import com.casesoft.dmc.extend.msg.websocket.entity.WsMessage;
import com.casesoft.dmc.extend.msg.websocket.entity.WsMessageService;
import com.casesoft.dmc.model.sys.User;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

/**
 * 
 */
//@Component
//@Scope("singleton")
public class SessionManager {
    private WsMessageService wsMessageService;

    // 有id 的用户 k = 用户id
    private HashMap<String, PushUser> userMap = new HashMap<>();

    public void put(PushUser user) {
        if (!userMap.containsKey(user.getId())) {
            userMap.put(user.getId(), user);
        }
    }

    /**
     * sss
     *
     * @param pushUser
     */
    public void remove(PushUser pushUser) {
        userMap.remove(pushUser.getId());
    }

    /**
     * 给所有在线用户发送消息,但是不包括自己
     *
     * @param wsMessage 是谁发的？
     */
    public void sendMessageToUsers(WsMessage wsMessage) {
        for (Map.Entry<String, PushUser> entry : this.userMap.entrySet()) {
            WebSocketSession session = entry.getValue().getWebSocketSession();
            if (!entry.getValue().getUserCode().equals(wsMessage.getToCode())) {
                if (session.isOpen()) {
                    try {
                        wsMessage.setSendDate(new Date());
                        session.sendMessage(new TextMessage(JSON
                                .toJSONStringWithDateFormat(wsMessage,
                                        "yyyy-MM-dd HH:mm:ss")));
                        wsMessage.setSuccess(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                        wsMessage.setSuccess(false);
                    }
                }
            }
        }
    }
    


    /**
     * 发送多条信息
     *
     * @param wsMessages
     */
    public void sendMessageToUser(List<WsMessage> wsMessages) {
        if(CommonUtil.isNotBlank(wsMessages)){
            for (WsMessage wsMessage : wsMessages) {
                sendMessageToUser(wsMessage);
            }
            this.wsMessageService.save(wsMessages);
        }
    }

    /**
     * 系统通知
     *
     * @param wsMessage
     **/
    public void sendMessageAllSysUser(WsMessage wsMessage)
            throws IOException, ClassNotFoundException {
        List<User> list = new ArrayList<>(CacheManager.getUserMap().values());
        List<WsMessage> wsMessages = new ArrayList<>();
        for (User u : list) {
            if (!u.getCode().equals(wsMessage.getFromCode())) {
                WsMessage phmsg;
                phmsg = (WsMessage) PushUtil.deepClone(wsMessage);
                phmsg.setToCode(u.getCode());
                wsMessages.add(phmsg);
            }
        }
        sendMessageToUser(wsMessages);
    }

    /**
     * 发送信息到店仓
     *
     * @param unitCode
     * @param wsMessage
     */
    public void sendMessageUnit(String unitCode, WsMessage wsMessage) throws IOException, ClassNotFoundException {
        System.out.println(unitCode);
        List<User> list = new ArrayList<>(CacheManager.getUserMap().values());
        List<WsMessage> wsMessages = new ArrayList<>();
        for (User u : list) {
            if (!u.getCode().equals(wsMessage.getFromCode()) && unitCode.equals(u.getOwnerId())) {
                WsMessage phmsg;
                phmsg = (WsMessage) PushUtil.deepClone(wsMessage);
                phmsg.setToCode(u.getCode());
                wsMessages.add(phmsg);
            }
        }
        sendMessageToUser(wsMessages);
        for (WsMessage m : wsMessages) {
            WsMessage msgCount = getCountMessage(m.getToCode());
            sendMessageToUser(msgCount);
        }

    }

    /**
     * 给某个用户发送消息
     *
     * @param wsMessage
     */
    public void sendMessageToUser(WsMessage wsMessage) {
        if(CommonUtil.isBlank(wsMessage)) {
            wsMessage.setId(new GuidCreator().toString());
        }
        for (Map.Entry<String, PushUser> entry : this.userMap.entrySet()) {
            PushUser user = entry.getValue();
            if (user.getUserCode().equals(wsMessage.getToCode())) {
                WebSocketSession session = entry.getValue().getWebSocketSession();
                if (session != null) {
                    if (session.isOpen()) {
                        try {
                            wsMessage.setSendDate(new Date());
                            System.out.println("session.sendMessage......");
                            wsMessage.setFromCode("SYS");
                            wsMessage.setRemark(wsMessage.getContent());
                            wsMessage.setContent(JSON
                                    .toJSONStringWithDateFormat(wsMessage,
                                            "yyyy-MM-dd HH:mm:ss"));
                            session.sendMessage(new TextMessage(wsMessage.getContent()));

                            wsMessage.setSuccess(true);
                        } catch (IOException e) {
                            e.printStackTrace();
                            wsMessage.setSuccess(false);
                        } finally {
                            this.wsMessageService.save(wsMessage);
                        }
                    }
                } else {
                     this.wsMessageService.save(wsMessage);
                }
            }
        }
    }
    public void sendPlMessageToUser(WsMessage wsMessage) {
        for (Map.Entry<String, PushUser> entry : this.userMap.entrySet()) {
            PushUser user = entry.getValue();
            if (user.getUserCode().equals(wsMessage.getToCode())) {
                WebSocketSession session = entry.getValue().getWebSocketSession();
                if (session != null) {
                    if (session.isOpen()) {
                        try {

                            session.sendMessage(new TextMessage(JSON.toJSONString(wsMessage)));
                            wsMessage.setSuccess(true);
                        } catch (IOException e) {
                            e.printStackTrace();
                            wsMessage.setSuccess(false);
                        } finally {
                    //        this.wsMessageService.save(wsMessage);
                        }
                    }
                } else {
              //      this.wsMessageService.save(wsMessage);
                }
            }
        }
    }
    public WsMessage getCountMessage(String toCode) {
        long count = wsMessageService.findCountAcceptUnSuccessMsg(toCode);
        WsMessage wsMessage = new WsMessage();
        wsMessage.setFromCode("system");
        wsMessage.setContent(String.valueOf(count));
        wsMessage.setToCode(toCode);
        wsMessage.setSendDate(new Date());
        return wsMessage;
    }

    public WsMessageService getWsMessageService() {
        return wsMessageService;
    }

    public void setWsMessageService(WsMessageService wsMessageService) {
        this.wsMessageService = wsMessageService;
    }
}
