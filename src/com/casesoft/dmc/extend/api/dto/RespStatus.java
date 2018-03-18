package com.casesoft.dmc.extend.api.dto;

import java.lang.reflect.Method;

/**
 * Created by pc on 2016-12-27.
 *
 * 接口返回定义
 */
public enum RespStatus {
    SUCCESS("success","10000"),//请求成功
    ERROR("error","10001"),//请求失败
    SERVICE_NO("serviceError","10002"),//接口不存在
    DEVICE_ERROR("deviceIdError","10003"),//设备号非法
    APPCODE_ERROR("appCodeError","10004"),//应用编号非法
    KEY_ERROR("keyError","10005"),//应用KEY非法
    AUTH_ERROR("authError","10006"),//授权失败
    DATA_ERROR("dataError","10007"),//数据验证错
    USER_ERROR("userError","10008"),//用户非法
    METHOD_ERROR("methodError","10009"),//方法不存在
    SYN_ERROR("synError","10010");//第三方接口上传失败

    private String status;//状态
    private String code;//编码
    RespStatus(String status,String code) {
        this.status=status;
        this.code=code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
