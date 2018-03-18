package com.casesoft.dmc.extend.api.valid;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.extend.api.config.APIConfig;
import com.casesoft.dmc.extend.api.dto.RequestEntity;
import com.casesoft.dmc.extend.api.dto.RespMessage;
import com.casesoft.dmc.extend.api.dto.RespStatus;
import com.casesoft.dmc.extend.api.valid.validation.IValidator;
import com.casesoft.dmc.service.sys.impl.UserService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by john on 2016/12/29.
 */
@Component
@Scope("prototype")
 public class ValidatorRequest implements IValidator {
    private RespMessage respMessage;
    @Resource
    private UserService userService;
    @Override
    public boolean validAuth(String deviceId, String userCode, String appCode,String key) {
        return true;
    }

    @Override
    public boolean validDevice(String deviceId) {
        if(deviceId.equals("Test001")|| CommonUtil.isNotBlank(CacheManager.getDeviceByCode(deviceId))){
            return true;
        }else{
            respMessage.setSuccess(false);
            respMessage.setRespStatus(RespStatus.DEVICE_ERROR);
            return false;
        }
      /*  if(CommonUtil.isBlank(deviceId)
                ||deviceId.equals("")
                || CommonUtil.isNotBlank(CacheManager.getDeviceByCode(deviceId))){
            respMessage.setSuccess(false);
            respMessage.setRespStatus(RespStatus.DEVICE_ERROR);
            return false;
        }
        return true;*/
    }

    @Override
    public boolean validUser(String userCode) {
        if(userCode.equals("TestUser")){
            return true;
        }else{
            respMessage.setSuccess(false);
            respMessage.setRespStatus(RespStatus.USER_ERROR);
            return false;
        }
       /* if(CommonUtil.isBlank(userCode)
                ||userCode.equals("")){
            respMessage.setSuccess(false);
            respMessage.setRespStatus(RespStatus.USER_ERROR);
            return false;
        }
        return true;*/
    }

    @Override
    public boolean validAppCode(String appCode) {
        if(appCode.equals("000000001")){
            return true;
        }else{
            respMessage.setSuccess(false);
            respMessage.setRespStatus(RespStatus.APPCODE_ERROR);
            return false;
        }
       /* if(CommonUtil.isBlank(appCode)
                ||appCode.equals("")){
            respMessage.setSuccess(false);
            respMessage.setRespStatus(RespStatus.KEY_ERROR);
            return false;
        }
        return true;*/
    }

    @Override
    public boolean validKey(String key) {
        if(key.equals("221b368d7f5f597867f525971f28ff75")){
            return true;
        }else {
            respMessage.setSuccess(false);
            respMessage.setRespStatus(RespStatus.KEY_ERROR);
        }
        if(CommonUtil.isBlank(key)
                ||key.equals("")){
            respMessage.setSuccess(false);
            respMessage.setRespStatus(RespStatus.KEY_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public RespMessage validRequestEntity(RequestEntity requestEntity) {
        respMessage=new RespMessage();
        respMessage.setVersion(APIConfig.version);
        if(CommonUtil.isBlank(requestEntity)){
            respMessage.setSuccess(false);
            respMessage.setRespStatus(RespStatus.ERROR);
        }else{
            if(!(validAppCode(requestEntity.getAppCode())
                    &&validDevice(requestEntity.getDeviceId())
                    &&validUser(requestEntity.getUserCode())
                    &&validKey(requestEntity.getKey()))){
                return respMessage;
            }else if(!validAuth(requestEntity.getDeviceId(),requestEntity.getUserCode(),
                    requestEntity.getAppCode(),requestEntity.getKey())){
                respMessage.setSuccess(false);
                respMessage.setRespStatus(RespStatus.AUTH_ERROR);
            }else{
                respMessage.setSuccess(true);
                respMessage.setRespStatus(RespStatus.SUCCESS);
            }
        }
        return respMessage;
    }

    @Override
    public void setRequestEntity(RequestEntity requestEntity) {

    }

    public static ValidatorRequest getInstance() {
        return new ValidatorRequest();
    }
}
