package com.casesoft.dmc.core.controller;

import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import com.casesoft.dmc.core.exception.ExceptionCode;
import com.casesoft.dmc.model.sys.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.WebMockUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by WingLi on 2015-09-07.
 */
public abstract class BaseController {
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected String jsonString = "";

    public BaseController() {
    }

    /** 基于@ExceptionHandler异常处理 */
    @ExceptionHandler
    @ResponseBody
    public MessageBox handleException(HttpServletRequest request, Exception ex) {

        return exceptionToMessageBox(ex);
    }


    public abstract String index();
    
    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
        binder.registerCustomEditor(Date.class, dateEditor);
    }

    @ModelAttribute
    public void setReqAndResp(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession(true);
    }


    public HttpServletRequest getRequest() {
        if(null == this.request) {
            this.request = WebMockUtil.getHttpServletRequest();
        }

        return this.request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getJsonString() {
        return this.jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    public HttpSession getSession() {
        return this.session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public User getCurrentUser() {
    	User user = (User)this.session.getAttribute("userSession");
        return user;
    }

    public HttpServletResponse getResponse() {
        if(null == this.response) {
            this.response = new MockHttpServletResponse();
        }
        return this.response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public MessageBox returnSuccessInfo(String msg)  {
        return new MessageBox(true, msg);
    }

    public MessageBox returnSuccessInfo(String msg, Object result) {
        return new MessageBox(true, msg, result);
    }

    public MessageBox returnFailInfo(String msg)  {
        return new MessageBox(false, msg);
    }

    public MessageBox returnFailInfo(String msg, Object result)  {
        return new MessageBox(false, msg, result);
    }

    public void returnSuccess(String msg) throws Exception {
        this.returnSuccess(new MessageBox(true, msg));
    }

    public void returnSuccess(MessageBox msgBox) throws Exception {
        this.outJsonString(JSON.toJSONString(msgBox));
    }

    public void returnSuccess(String msg, Object result) throws Exception {
        this.outJsonString(JSON.toJSONString(new MessageBox(true, msg, result)));
    }

    public void returnSuccess(String msg, Object result, String dateFormat) throws Exception {
        this.outJsonString(JSON.toJSONStringWithDateFormat(new MessageBox(true, msg, result), dateFormat));
    }

    public void returnFailur(String failMesg) throws Exception {
        this.returnSuccess(new MessageBox(false, failMesg));
    }

    public void returnFailur(String failMesg, Object result) {
        this.outJsonString(JSON.toJSONString(new MessageBox(false, failMesg, result)));
    }

    public void outJsonString(String str) {
        this.getResponse().setContentType("text/javascript;charset=UTF-8");
        this.outString(str);
    }

    public void outString(String str) {
        this.setJsonString(str);
        PrintWriter out = null;

        try {
            out = this.getResponse().getWriter();
            out.write(str);
        } catch (IOException var7) {
            var7.printStackTrace();
        } finally {
            out.close();
        }

    }

    public void outXmlString(String xmlStr) {
        this.getResponse().setContentType("application/xml;charset=UTF-8");
        this.outString(xmlStr);
    }

    public void logAllRequestParams() {
        HttpServletRequest request = this.getRequest();
        Enumeration paramNames = request.getParameterNames();

        while(paramNames.hasMoreElements()) {
            String paramName = (String)paramNames.nextElement();
            System.out.println(paramName + ": " + request.getParameter(paramName));
            this.logger.info(CommonUtil.getDateString(new Date(), "YYYY-MM-dd HH:mm:ss") + " " + paramName + ": " + request.getParameter(paramName));
        }

    }

    public String getReqParam(String name) {
        return this.getRequest().getParameter(name);
    }


    private MessageBox exceptionToMessageBox(Exception e) {
        String errorResultMsg = e.getMessage();
        String errorCode = ExceptionCode.ElseException;
        String errorMsg = "";
        if (e instanceof NullPointerException) {
            errorMsg = "空指针异常类";
            errorCode = ExceptionCode.NullPointerException;
        } else 
        if (e instanceof ClassCastException) {
            errorMsg = "类型转换异常类";
        } else
        if (e instanceof ArithmeticException) {
            errorMsg = "算术异常类";
        } else

        if (e instanceof NegativeArraySizeException) {
            errorMsg = "数组负下标异常";
        } else
        if (e instanceof ArrayIndexOutOfBoundsException) {
            errorMsg = "数组下标越界异常";
        } else
        if (e instanceof SecurityException) {
            errorMsg = "违背安全原则异常";
        } else
        if (e instanceof Exception) {
            errorMsg = "文件已结束异常";
        } else
        if (e instanceof ClassNotFoundException) {
            errorMsg = "文件未找到异常";
        } else
        if (e instanceof NumberFormatException) {
            errorMsg = "字符串转换为数字异常";
        } else
        if (e instanceof Exception) {
            errorMsg = "操作数据库异常";
        } else
        if (e instanceof ClassCastException) {
            errorMsg = "输入输出异常";
        } else
        if (e instanceof NoSuchMethodException) {
            errorMsg = "方法未找到异常";
        } else
        if (e instanceof ArithmeticException) {
            errorMsg = "算术条件异常";
        } else
        if (e instanceof ArrayIndexOutOfBoundsException) {
            errorMsg = "数组索引越界异常";
        } else
        if (e instanceof ArrayStoreException) {
            errorMsg = "数组存储异常";
        } else
        if (e instanceof ClassCastException) {
            errorMsg = "类造型异常";
        } else
        if (e instanceof ClassNotFoundException) {
            errorMsg = "找不到类异常";
        } else
        if (e instanceof CloneNotSupportedException) {
            errorMsg = "不支持克隆异常";
        } else
        if (e instanceof EnumConstantNotPresentException) {
            errorMsg = "枚举常量不存在异常";
        }  else
        if (e instanceof IllegalAccessException) {
            errorMsg = "违法的访问异常";
        } else
        if (e instanceof IllegalMonitorStateException) {
            errorMsg = "违法的监控状态异常";
        } else
        if (e instanceof IllegalStateException) {
            errorMsg = "违法的状态异常";
        } else
        if (e instanceof IllegalThreadStateException) {
            errorMsg = "违法的线程状态异常";
        } else
        if (e instanceof IndexOutOfBoundsException) {
            errorMsg = "索引越界异常";
        } else
        if (e instanceof InstantiationException) {
            errorMsg = "实例化异常";
        } else
        if (e instanceof InterruptedException) {
            errorMsg = "被中止异常";
        } else
        if (e instanceof NegativeArraySizeException) {
            errorMsg = "数组大小为负值异常";
        } else
        if (e instanceof NoSuchFieldException) {
            errorMsg = "属性不存在异常";
        } else
        if (e instanceof NoSuchMethodException) {
            errorMsg = "方法不存在异常";
        } else
        if (e instanceof NullPointerException) {
            errorMsg = "空指针异常";
        } else
        if (e instanceof NumberFormatException) {
            errorMsg = "数字格式异常";
        } else
        if (e instanceof RuntimeException) {
            errorMsg = "运行时异常";
        } else
        if (e instanceof SecurityException) {
            errorMsg = "安全异常";
        } else
        if (e instanceof StringIndexOutOfBoundsException) {
            errorMsg = "字符串索引越界异常";
        } else
        if (e instanceof TypeNotPresentException) {
            errorMsg = "类型不存在异常";
        } else
        if (e instanceof UnsupportedOperationException) {
            errorMsg = "不支持的方法异常";
        } else {
           
                errorMsg = "根异常";
        }
        e.printStackTrace();
        MessageBox msgBox = new MessageBox(false,errorMsg,errorResultMsg);
        msgBox.setStatusCode(errorCode);
        return msgBox;
    }



    protected void outFile(String fileName, File outFile, String contentType) throws IOException {
        HttpServletResponse response = this.getResponse();
        response.setContentType(contentType);
        response.setHeader("Content-Disposition", "attachment;filename="
                + URLEncoder.encode(fileName, "utf-8"));

        byte[] data = getBytes(outFile);

        response.setContentLength(data.length);
        response.getOutputStream().write(data);
        response.flushBuffer();
    }
    public static byte[] getBytes(File file){
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}
