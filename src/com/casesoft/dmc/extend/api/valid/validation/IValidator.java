package com.casesoft.dmc.extend.api.valid.validation;

import com.casesoft.dmc.extend.api.dto.RequestEntity;
import com.casesoft.dmc.extend.api.dto.RespMessage;

/**
 * Created by john on 2016/12/29.
 */
public interface IValidator {
    /**
     * 验证授权
     *
     * @param deviceId
     * @param userCode
     * @param appCode
     * @param key
     * @return
     */
    boolean validAuth(String deviceId, String userCode, String appCode, String key);

    /**
     * 验证设备号
     *
     * @param deviceId
     * @return
     */
    boolean validDevice(String deviceId);

    /**
     * 验证用户
     *
     * @param userCode
     * @return
     */
    boolean validUser(String userCode);

    /**
     * 验证appCode
     *
     * @param appCode
     * @return
     */
    boolean validAppCode(String appCode);

    /**
     * 验证Key
     *
     * @param key
     * @return
     */
    boolean validKey(String key);

    /**
     * 验证请求实体类
     *
     * @param requestEntity
     * @return
     */
    RespMessage validRequestEntity(RequestEntity requestEntity);

    void setRequestEntity(RequestEntity requestEntity);
}
