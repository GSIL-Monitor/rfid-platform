package com.casesoft.dmc.core.controller.security;

import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.model.sys.User;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Shiro框架中自定义权限过滤器
 * 
 * @author WinstonLi
 * 
 */
public class PermissionAuthorizationFilter extends AuthorizationFilter {

  @Override
  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
      Object mappedValue) throws Exception {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;
    resp.setContentType("text/html");
    resp.setCharacterEncoding("utf-8");

    String request_uri = req.getRequestURI();
    String contextPath = req.getContextPath();

    String uri = request_uri.substring(contextPath.length());

    // if(uri.equals(this.login_page) ||
    if (uri.contains("admin")
        || uri.toLowerCase().contains("login")
        || uri.contains("WS")
        || uri.contains("api")) {
      return true;
    } else if (uri.toLowerCase().endsWith(".png") || uri.toLowerCase().endsWith(".jpg")
        || uri.toLowerCase().endsWith(".gif") 
        || uri.endsWith(".css") || uri.endsWith(".js")) {// 不过滤css和js文件
      return true;
    } else {
      HttpSession session = req.getSession();
      User user = (User) session.getAttribute(Constant.Session.User_Session);
      if (null == user) {
        return false;
      } else {
        return true;
      }
    }
  }

}
