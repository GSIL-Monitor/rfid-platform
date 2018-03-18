package com.casesoft.dmc.extend.third.searcher.msg;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.extend.msg.websocket.entity.WsMessage;

/**
 * Created by john on 2017-04-17.
 */
public class Test {
    public static void main(String []args){
        WsMessage wsMessage=new WsMessage();
        wsMessage.setContent("我");
        wsMessage.setFromCode("kingthy");
        wsMessage.setMsgType(1);
        wsMessage.setToCode("admin");
        System.out.println(JSON.toJSONString(wsMessage));
    }
}
