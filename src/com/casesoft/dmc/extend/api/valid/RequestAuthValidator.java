package com.casesoft.dmc.extend.api.valid;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.extend.api.dto.RequestEntity;
import com.casesoft.dmc.extend.api.dto.RespMessage;
import com.casesoft.dmc.extend.api.dto.RespStatus;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Created by pc on 2016-12-27.
 * 接口验证器
 */
@Aspect
@Component
@Scope("prototype")
public class RequestAuthValidator {
    private static final Logger logger = LoggerFactory.getLogger("RequestLimitLogger");
    private RespMessage respMessage;
    private HttpServletResponse response;
    private HttpServletRequest request;
    @Autowired
    private ValidatorRequest validatorRequest;

    @Around(value = "within(@org.springframework.stereotype.Controller *) && @annotation(com.casesoft.dmc.extend.api.valid.validation.ApiAuth)")
    private void
    validAuth(ProceedingJoinPoint joinPoint) {
        try {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            RequestEntity requestEntity = null;// JSON.parseObject(requestBodyStr, RequestEntity.class);
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0 && args[0].getClass() == RequestEntity.class) {
                requestEntity = (RequestEntity) args[0];
            }
            RespMessage respMessage = validatorRequest.validRequestEntity(requestEntity);
            if (respMessage.getSuccess()) {
                respMessage = (RespMessage) joinPoint.proceed();
            }
            this.outJsonString(response, respMessage);
        } catch (IOException e) {
            e.printStackTrace();
            respMessage = new RespMessage();
            respMessage.setRespStatus(RespStatus.DATA_ERROR);
            respMessage.setSuccess(false);
            this.outJsonString(response, respMessage);
        } catch (Exception e) {
            respMessage = new RespMessage();
            respMessage.setRespStatus(RespStatus.ERROR);
            respMessage.setSuccess(false);
            e.printStackTrace();
            this.outJsonString(response, respMessage);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            respMessage = new RespMessage();
            respMessage.setRespStatus(RespStatus.ERROR);
            respMessage.setSuccess(false);
            this.outJsonString(response, respMessage);
        }
    }

    public void outJsonString(HttpServletResponse response, RespMessage respMessage) {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(JSON.toJSONString(respMessage));
            logger.info("写入Resonse字符串：" + JSON.toJSONString(respMessage));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }
}
