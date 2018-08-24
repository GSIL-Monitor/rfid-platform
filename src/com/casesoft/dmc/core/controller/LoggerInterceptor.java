package com.casesoft.dmc.core.controller;

import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.model.log.ServerLogMessage;
import com.casesoft.dmc.service.log.ServerLogMessageService;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by WingLi on 2017/1/11.
 */
public class LoggerInterceptor implements HandlerInterceptor,AfterReturningAdvice {
    private NamedThreadLocal<Long>  startTimeThreadLocal
        = new NamedThreadLocal<Long>("Request-StartTime");

    @Autowired
    private ServerLogMessageService serverLogMessageService;


    @Override
    public void afterReturning(Object o, Method method, Object[] objects, Object o1) throws Throwable {

    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        long beginTime = System.currentTimeMillis();//1、开始时间
        startTimeThreadLocal.set(beginTime);//线程绑定变量（该数据只有当前请求的线程可见）
        return true;//继续流程
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object handler, Exception e) throws Exception {
        long endTime = System.currentTimeMillis();//2、结束时间
        long beginTime = startTimeThreadLocal.get();//得到线程绑定的局部变量（开始时间）
        long consumeTime = endTime - beginTime;//3、消耗的时间

        String uri = httpServletRequest.getRequestURI();
        ServerLogMessage logMessage = new ServerLogMessage();
        logMessage.setConsumeTime(consumeTime);
        logMessage.setCreateTime(new Date());
        logMessage.setMethod(uri);
        if (consumeTime > 500) {//此处认为处理时间超过500毫秒的请求为慢请求
            //TODO 记录到日志文件
            System.out.println(
                    String.format("%s consume %d millis", uri, consumeTime));
            logMessage.setType(Constant.LogType.LONG_TIME);
        } else if (uri.contains("api")) {
            logMessage.setType(Constant.LogType.API);
        } else if(uri.toLowerCase().contains("synchronize")) {
            logMessage.setType(Constant.LogType.SYN);
        } else if(uri.toLowerCase().contains("add") ||
                uri.toLowerCase().contains("save") || uri.toLowerCase().contains("edit") ||
                uri.toLowerCase().contains("insert") || uri.toLowerCase().contains("del") ||
                uri.toLowerCase().contains("upload") ) {
            logMessage.setType(Constant.LogType.SYSTEM);
        }
        this.serverLogMessageService.save(logMessage);
    }

}
