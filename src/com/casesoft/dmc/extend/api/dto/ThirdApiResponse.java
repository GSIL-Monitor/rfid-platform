package com.casesoft.dmc.extend.api.dto;

/**
 * Created by john on 2017-05-31.
 * @author john
 * 第三方接口返回值
 */
public class ThirdApiResponse {
    private String message;//说明信息
    private String body;//返回值
    private String resCode="-1";//返回编号 -1为默认错误
    private Object convertObject =null;//转换成对象

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public Object getConvertObject() {
        return convertObject;
    }

    public void setConvertObject(Object convertObject) {
        this.convertObject = convertObject;
    }
}
