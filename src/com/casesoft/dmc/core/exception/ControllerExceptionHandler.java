package com.casesoft.dmc.core.exception;

import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.dto.RespMessage;
import com.casesoft.dmc.extend.api.dto.RespStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by WingLi on 2015-12-10.
 */
public class ControllerExceptionHandler implements HandlerExceptionResolver {
    @Override
    @ResponseBody
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        String errorMsg = e.getMessage();
        if (e instanceof ClassCastException) {
            errorMsg = "类型转换异常类";
        }
        if (e instanceof ArithmeticException) {
            errorMsg = "算术异常类";
        }
        if (e instanceof NullPointerException) {
            errorMsg = "空指针异常类";
        }
        if (e instanceof NegativeArraySizeException) {
            errorMsg = "数组负下标异常";
        }
        if (e instanceof ArrayIndexOutOfBoundsException) {
            errorMsg = "数组下标越界异常";
        }
        if (e instanceof SecurityException) {
            errorMsg = "违背安全原则异常";
        }
        if (e instanceof Exception) {
            errorMsg = "文件已结束异常";
        }
        if (e instanceof ClassNotFoundException) {
            errorMsg = "文件未找到异常";
        }
        if (e instanceof NumberFormatException) {
            errorMsg = "字符串转换为数字异常";
        }
        if (e instanceof Exception) {
            errorMsg = "操作数据库异常";
        }
        if (e instanceof ClassCastException) {
            errorMsg = "输入输出异常";
        }
        if (e instanceof NoSuchMethodException) {
            errorMsg = "方法未找到异常";
        }

        if (e instanceof ArithmeticException) {
            errorMsg = "算术条件异常";
        }
        if (e instanceof ArrayIndexOutOfBoundsException) {
            errorMsg = "数组索引越界异常";
        }
        if (e instanceof ArrayStoreException) {
            errorMsg = "数组存储异常";
        }
        if (e instanceof ClassCastException) {
            errorMsg = "类造型异常";
        }
        if (e instanceof ClassNotFoundException) {
            errorMsg = "找不到类异常";
        }
        if (e instanceof CloneNotSupportedException) {
            errorMsg = "不支持克隆异常";
        }
        if (e instanceof EnumConstantNotPresentException) {
            errorMsg = "枚举常量不存在异常";
        }
        if (e instanceof Exception) {
            errorMsg = "根异常";
        }
        if (e instanceof IllegalAccessException) {
            errorMsg = "违法的访问异常";
        }
        if (e instanceof IllegalMonitorStateException) {
            errorMsg = "违法的监控状态异常";
        }
        if (e instanceof IllegalStateException) {
            errorMsg = "违法的状态异常";
        }
        if (e instanceof IllegalThreadStateException) {
            errorMsg = "违法的线程状态异常";
        }
        if (e instanceof IndexOutOfBoundsException) {
            errorMsg = "索引越界异常";
        }
        if (e instanceof InstantiationException) {
            errorMsg = "实例化异常";
        }
        if (e instanceof InterruptedException) {
            errorMsg = "被中止异常";
        }
        if (e instanceof NegativeArraySizeException) {
            errorMsg = "数组大小为负值异常";
        }
        if (e instanceof NoSuchFieldException) {
            errorMsg = "属性不存在异常";
        }
        if (e instanceof NoSuchMethodException) {
            errorMsg = "方法不存在异常";
        }
        if (e instanceof NullPointerException) {
            errorMsg = "空指针异常";
        }
        if (e instanceof NumberFormatException) {
            errorMsg = "数字格式异常";
        }
        if (e instanceof RuntimeException) {
            errorMsg = "运行时异常";
        }
        if (e instanceof SecurityException) {
            errorMsg = "安全异常";
        }
        if (e instanceof StringIndexOutOfBoundsException) {
            errorMsg = "字符串索引越界异常";
        }
        if (e instanceof TypeNotPresentException) {
            errorMsg = "类型不存在异常";
        }
        if (e instanceof UnsupportedOperationException) {
            errorMsg = "不支持的方法异常";
        }
        ModelAndView mav = new ModelAndView("jsonView");
        return mav.addObject(new RespMessage(false, RespStatus.ERROR,errorMsg));
    }
}
