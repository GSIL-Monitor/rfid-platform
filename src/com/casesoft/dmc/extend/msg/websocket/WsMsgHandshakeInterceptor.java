package com.casesoft.dmc.extend.msg.websocket;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.extend.msg.websocket.entity.PushUser;
import com.casesoft.dmc.model.sys.User;
import org.springframework.web.socket.WebSocketSession;

//@Component
public class WsMsgHandshakeInterceptor implements HandshakeInterceptor {

    private static final Logger logger = Logger.getLogger(WsMsgHandshakeInterceptor.class);

    /**
     * beforeHandshake，在调用handler前处理方法。常用在注册用户信息，绑定WebSocketSession，在handler里根据用户信息获取WebSocketSession发送消息。
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @param webSocketHandler
     * @param map
     * @return
     * @throws Exception 
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        logger.info(serverHttpRequest.getRemoteAddress() + "开始连接！");
        if (serverHttpRequest instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) serverHttpRequest;
            String deviceId = servletRequest.getServletRequest().getParameter("deviceId");
            if(CommonUtil.isNotBlank(deviceId)) {
                PushUser ps = new PushUser();
                ps.setType(PushUser.Device);
                ps.setOnlineTime(new Date());
                ps.setUserCode(deviceId);
                ps.setIpAddr(serverHttpRequest.getRemoteAddress().getAddress().getHostAddress());
                map.put(Constant.Session.User_Session, ps);
            } else {
                HttpSession session = servletRequest.getServletRequest().getSession(true);
                if (session != null) {
                    //使用usercode区分WebSocketHandler，以便定向发送消息
                    Object obj = session.getAttribute(Constant.Session.User_Session);
                    if (CommonUtil.isNotBlank(obj)) {
                        User user = (User) session.getAttribute(Constant.Session.User_Session);
                        PushUser ps = new PushUser();
                        ps.setType(PushUser.Web);
                        ps.setOnlineTime(new Date());
                        ps.setUserCode(user.getCode());
                        ps.setIpAddr(serverHttpRequest.getRemoteAddress().getAddress().getHostAddress());
                        map.put(Constant.Session.User_Session, ps);
                    } else {
                        PushUser ps = new PushUser();
                        ps.setOnlineTime(new Date());
                        ps.setType(PushUser.Web);
                        ps.setIpAddr(serverHttpRequest.getRemoteAddress().getAddress().getHostAddress());
                        map.put(Constant.Session.User_Session, ps);
                    }
                } else {
                    PushUser ps = new PushUser();
                    ps.setOnlineTime(new Date());
                    ps.setType(PushUser.Web);
                    ps.setUserCode(PushUser.SYS);
                    ps.setIpAddr(serverHttpRequest.getRemoteAddress().getAddress().getHostAddress());
                    map.put(Constant.Session.User_Session, ps);
                }
            }
        }
        return true;
    }

    /**
     * 连接关闭后处理
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @param webSocketHandler
     * @param e 
     */
    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
        System.err.println("有人访问了：" + serverHttpRequest.getRemoteAddress());
        logger.info(serverHttpRequest.getRemoteAddress() + "连接成功！");
    }
    

    
}
