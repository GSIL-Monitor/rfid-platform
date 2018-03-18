package com.casesoft.dmc.extend.msg.websocket.entity;

public class MessageType {
    public static final int MSG_PTOP_TYPE = 1;//点对点
    public static final int MSG_PUBLISH_TYPE = 2;//发布订阅
    public static final int MSG_KEY_TYPE = 3;//游客信息

    //region 定义消息类型
    public static final String Update_Product = "UD_PROD";//更新产品通知
    public static final String Notice_Msg = "NT_MSG";//通知消息
    public static final String Notice_Online = "NT_OLN";//通知上线
    //endregion

}
