package com.casesoft.dmc.extend.msg.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 
 * 实现WebSocketConfigurer接口，重写registerWebSocketHandlers方法，这是一个核心实现方法，
 * 配置websocket入口，允许访问的域、注册Handler、SockJs支持和拦截器。
 */
//@Configuration
//@EnableWebSocket
public class WsMsgWebSocketConfigurer implements WebSocketConfigurer {

	//@Autowired
	private WsMsgHandshakeInterceptor webSocketInterceptor;
    //@Autowired
    private WsMsgWebSocketHandler systemWebSocketHandler;
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
    	//注册websocket服务器 registry.addHandler注册和路由的功能，当客户端发起websocket连接，把/path交给对应的handler处理，而不实现具体的业务逻辑，可以理解为收集和任务分发中心。
    	webSocketHandlerRegistry.addHandler(systemWebSocketHandler,"/msgServer.do").addInterceptors(webSocketInterceptor);
    	//注册websocket服务器(基于soketJS)
        webSocketHandlerRegistry.addHandler(systemWebSocketHandler,"/sockJS/msgServer.do").addInterceptors(webSocketInterceptor).withSockJS();

    }
}
