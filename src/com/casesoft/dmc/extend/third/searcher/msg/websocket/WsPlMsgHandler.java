package com.casesoft.dmc.extend.third.searcher.msg.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.extend.msg.websocket.SessionManager;
import com.casesoft.dmc.extend.msg.websocket.entity.PushUser;
import com.casesoft.dmc.extend.msg.websocket.entity.WsMessage;
import com.casesoft.dmc.extend.msg.websocket.entity.WsMessageService;
import com.casesoft.dmc.extend.third.searcher.model.SearchMain;
import com.casesoft.dmc.extend.third.searcher.service.SearchMainService;
import org.apache.log4j.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pc on 2016/3/28.
 */
//@Component
public class WsPlMsgHandler implements WebSocketHandler {
    private static final Logger logger = Logger
            .getLogger(WsPlMsgHandler.class);

    public static class MsgType{
        public static int SEARCHER=1;//找货人
        public static int ORDER=2;//请求人
    }
    // @Autowired
    private WsMessageService wsMessageService;

    private SearchMainService searchMainService;

    //  @Autowired
    private SessionManager sessionManager;

    // 初次链接成功执行
    @Override
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
        logger.debug("Pl链接成功......");
        Object temp = session.getAttributes()
                .get(Constant.Session.User_Session);
        final PushUser user = (PushUser) temp; // 用户信息
        user.setWebSocketSession(session);
        user.setId(session.getId());
        sessionManager.put(user);
        if (temp != null) {
          List<PropertyFilter> hfilters = new ArrayList<PropertyFilter>();
            PropertyFilter hfilter = new PropertyFilter("EQI_status", ""
                    + SearchMain.Status.MAIN_STATUS_SEND);
            hfilters.add(hfilter);
            hfilter = new PropertyFilter("EQS_toCode", user.getUserCode());
            hfilters.add(hfilter);
            hfilter = new PropertyFilter("GED_sendDate", CommonUtil.getDateString(new Date(),"yyyy-MM-dd"));
            hfilters.add(hfilter);
            List<SearchMain> searchMains = this.searchMainService.find(hfilters);
            WsMessage wsMessage = new WsMessage();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderQty", searchMains.size());
            wsMessage.setContent(String.valueOf(searchMains.size()));
            wsMessage.setFromCode("system");
            wsMessage.setMsgType(1);
            wsMessage.setToCode(user.getUserCode());
            sessionManager.sendPlMessageToUser(wsMessage);

            List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
            PropertyFilter filter = new PropertyFilter("GEI_status", ""
                    + SearchMain.Status.MAIN_STATUS_END);
            filters.add(filter);
            filter = new PropertyFilter("EQS_fromCode",user.getUserCode());
            filters.add(filter);
            filter = new PropertyFilter("GED_sendDate", CommonUtil.getDateString(new Date(),"yyyy-MM-dd"));
            filters.add(filter);
            List<SearchMain> osearchMains = this.searchMainService.find(filters);
            WsMessage owsMessage = new WsMessage();
            owsMessage.setContent(String.valueOf(osearchMains.size()));
            owsMessage.setFromCode("system");
            owsMessage.setMsgType(2);
            owsMessage.setToCode(user.getUserCode());
            sessionManager.sendPlMessageToUser(owsMessage);
        }

    }

    // 接受消息处理消息
    @Override
    public void handleMessage(final WebSocketSession webSocketSession,
                              final WebSocketMessage<?> webSocketMessage) throws Exception {

        String msg = webSocketMessage.getPayload().toString();
        WsMessage wsMessage = JSON.parseObject(msg,
                WsMessage.class);
        if(wsMessage.getMsgType()==MsgType.ORDER){
            List<PropertyFilter> hfilters = new ArrayList<PropertyFilter>();
            PropertyFilter hfilter = new PropertyFilter("EQI_status", ""
                    + SearchMain.Status.MAIN_STATUS_SEND);
            hfilters.add(hfilter);
            hfilter = new PropertyFilter("EQS_toCode", wsMessage.getToCode());
            hfilters.add(hfilter);
            hfilter = new PropertyFilter("GED_sendDate", CommonUtil.getDateString(new Date(),"yyyy-MM-dd"));
            hfilters.add(hfilter);
            List<SearchMain> searchMains = this.searchMainService.find(hfilters);
            WsMessage wsMessageTransfer = new WsMessage();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderQty", searchMains.size());
            wsMessageTransfer.setContent(String.valueOf(searchMains.size()));
            wsMessageTransfer.setFromCode(wsMessage.getFromCode());
            wsMessageTransfer.setMsgType(MsgType.SEARCHER);
            wsMessageTransfer.setToCode(wsMessage.getToCode());
            sessionManager.sendPlMessageToUser(wsMessageTransfer);

        }else{
            List<PropertyFilter> hfilters = new ArrayList<PropertyFilter>();
            PropertyFilter hfilter = new PropertyFilter("GEI_status", ""
                    + SearchMain.Status.MAIN_STATUS_END);
            hfilters.add(hfilter);
            hfilter = new PropertyFilter("EQS_fromCode", wsMessage.getToCode());
            hfilters.add(hfilter);
            hfilter = new PropertyFilter("GED_sendDate", CommonUtil.getDateString(new Date(),"yyyy-MM-dd"));
            hfilters.add(hfilter);
            List<SearchMain> searchMains = this.searchMainService.find(hfilters);
            WsMessage wsMessageTransfer = new WsMessage();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderQty", searchMains.size());
            wsMessageTransfer.setContent(String.valueOf(searchMains.size()));
            wsMessageTransfer.setFromCode(wsMessage.getFromCode());
            wsMessageTransfer.setMsgType(MsgType.ORDER);
            wsMessageTransfer.setToCode(wsMessage.getToCode());
            sessionManager.sendPlMessageToUser(wsMessageTransfer);
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
        sessionManager.remove(user);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession,
                                      CloseStatus closeStatus) throws Exception {
        logger.debug("链接关闭......" + closeStatus.toString());
        PushUser user = (PushUser) webSocketSession.getAttributes().get(
                Constant.Session.User_Session);
        sessionManager.remove(user);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
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

    public SearchMainService getSearchMainService() {
        return searchMainService;
    }

    public void setSearchMainService(SearchMainService searchMainService) {
        this.searchMainService = searchMainService;
    }
}
