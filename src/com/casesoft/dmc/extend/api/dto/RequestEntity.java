package com.casesoft.dmc.extend.api.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by john on 2016/12/29.
 * 外部接口接受路径
 */
public class RequestEntity<T> {
    @NotNull(message = "设备号为空")
    private String deviceId;//授权设备号
    @NotNull(message = "应用授权码为空")
    private String appCode;//应用授权码
    @NotNull(message = "key为空")
    private String key;//应用Key
    @NotNull(message = "方法不存在")
    private String method;//调用接口名称
    @NotNull(message = "品牌商编码为空")
    private String brandCode;//品牌商编码
    @Valid
    @NotNull(message = "请求数据为空")
    private T requestData;//请求数据
    @NotNull(message = "用户为空")
    private String userCode;//用户
    @NotNull(message = "接口版本为空")
    private String version;//接口版本

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getRequestData() {
        return requestData;
    }

    public void setRequestData(T requestData) {
        this.requestData = requestData;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
