package com.casesoft.dmc.extend.msg.websocket.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.web.socket.WebSocketSession;

import java.util.Date;

/**
 * 访客
 * */
public class PushUser implements java.io.Serializable {

    /*
    * TYPE_MSG 系统信息
    * TYPE_TASK task信息
    * TYPE_UN_ACCEPT_COUNT 请求未读信息
    * **/
    @JSONField(serialize = false)
    public static final int TYPE_ALL =0;
    @JSONField(serialize = false)
    public static final int TYPE_MSG =1;
    @JSONField(serialize = false)
    public static final int TYPE_TASK =2;
    @JSONField(serialize = false)
    public static final int TYPE_UN_ACCEPT_COUNT =3;
    @JSONField(serialize = false)
    public static final String Device = "D";
    @JSONField(serialize = false)
    public static final String Web = "W";
    @JSONField(serialize = false)
    public static final String SYS = "SYS";
    public static final String All_User_Code = "ALL";

	private String  id;
    private String userCode;
    private Date onlineTime;
    private String ipAddr;
    @JSONField(serialize = false)
    private WebSocketSession webSocketSession;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    public void setWebSocketSession(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Date onlineTime) {
        this.onlineTime = onlineTime;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }
}
