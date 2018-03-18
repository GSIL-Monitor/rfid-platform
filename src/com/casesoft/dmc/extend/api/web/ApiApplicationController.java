package com.casesoft.dmc.extend.api.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.casesoft.dmc.extend.api.dto.RequestEntity;
import com.casesoft.dmc.extend.api.dto.RespMessage;
import com.casesoft.dmc.extend.api.dto.RespStatus;

/**
 * Created by john on 2017/1/3.
 * 外部接口统一入口
 */
@Controller
public class ApiApplicationController extends ApiBaseController{
    @Autowired
    private  ApplicationContext applicationContext;
    private HttpMessageConverter httpMessageConverter;
    @Override
    public String index() {
        return null;
    }
    @RequestMapping(value = "/api",method = RequestMethod.POST,produces = "application/json;charset:UTF-8")
    @ResponseBody
    public RespMessage  api(@Valid RequestEntity<String> entity ,BindingResult result) throws ServletException, IOException {
        applicationContext.getApplicationName();
        return this.returnApiFailInfo(RespStatus.ERROR);
    }

}
