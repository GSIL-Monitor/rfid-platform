package com.casesoft.dmc.extend.api.dto;

import com.casesoft.dmc.core.vo.MessageBox;

/**
 * Created by pc on 2016-12-27.
 */
public class RespMessage extends MessageBox {
    private String subCode;//错误详细编码
    private String subMessage;//错误详细
    private String version; //接口版本

    public RespMessage() {
     }
    public RespMessage(boolean success,RespStatus respStatus) {
        this.setSuccess(success);
        this.setRespStatus(respStatus);
    }
    public RespMessage(boolean success,RespStatus respStatus,String subErrorMessage) {
        this.setSuccess(success);
        this.setRespStatus(respStatus);
        this.setSubMessage(subErrorMessage);
    }
    public RespMessage(boolean success,RespStatus respStatus,String msg,Object result) {
        this.setSuccess(success);
        this.setMsg(msg);
        this.setResult(result);
        this.setRespStatus(respStatus);
    }
    public RespMessage(boolean success,RespStatus respStatus,Object result) {
        this.setSuccess(success);
        this.setRespStatus(respStatus);
        this.setResult(result);
    }
    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubMessage() {
        return subMessage;
    }

    public void setSubMessage(String subMessage) {
        this.subMessage = subMessage;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    public void setRespStatus(RespStatus respStatus){
        this.setStatusCode(respStatus.getCode());
        this.setMsg(respStatus.getStatus());
       // this.setSubMessage(respStatus.getStatus());
    }
}
