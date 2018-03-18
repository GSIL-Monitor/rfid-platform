package com.casesoft.dmc.extend.api.web;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.extend.api.dto.RespMessage;
import com.casesoft.dmc.extend.api.dto.RespStatus;

/**
 * Created by john on 2016/12/30.
 */
public abstract class ApiBaseController extends BaseController {
    public RespMessage returnApiSuccessInfo(Object result) {
        return new RespMessage(true, RespStatus.SUCCESS, result);
    }
    public RespMessage returnApiSuccessInfo() {
        return new RespMessage(true, RespStatus.SUCCESS);
    }
    public RespMessage returnApiFailInfo(String subErrorMessage)  {
        return new RespMessage(false, RespStatus.ERROR,subErrorMessage);
    }
    public RespMessage returnApiFailInfo(RespStatus respStatus)  {
        return new RespMessage(false, respStatus);
    }
    public RespMessage returnApiInfo(boolean success,RespStatus respStatus,String subErrorMessage) {
        return new RespMessage(success, respStatus,subErrorMessage);
    }
    public RespMessage returnApiSuccessInfo(String message, Object result) {
        return new RespMessage(true,RespStatus.SUCCESS,message,result);
    }
}
