package com.casesoft.dmc.core.controller;

import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.model.sys.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Deprecated
public class AuthFilter implements Filter {

	private static final String LOGIN_URI = "LOGON_URI";
	private static final String HOME_URI = "HOME_URI";
	private String login_page;
	private String home_page;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.login_page = filterConfig.getInitParameter(LOGIN_URI);
		this.home_page = filterConfig.getInitParameter(HOME_URI);

		if (null == this.login_page || null == this.home_page) {
			throw new ServletException("登录页面查询不到");
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		resp.setContentType("text/html");
		resp.setCharacterEncoding("utf-8");

		String request_uri = req.getRequestURI();
		String contextPath = req.getContextPath();

		String uri = request_uri.substring(contextPath.length());
	    if (uri.equals(this.login_page)
				|| uri.contains("admin")
				|| uri.contains("login")
                || uri.contains("index.jsp")
                || uri.contains("WS")
                || uri.contains("api")) {
			chain.doFilter(req, resp);
		} else if (uri.endsWith(".png") || uri.endsWith(".PNG")
				|| uri.endsWith(".jpg") || uri.endsWith(".JPG")
				|| uri.endsWith(".gif") || uri.endsWith(".GIF")
				|| uri.endsWith(".bmp") || uri.endsWith(".BMP")
				|| uri.endsWith(".css") || uri.endsWith(".js")) {// 不过滤css和js文件
			chain.doFilter(req, resp);
		} else {
			HttpSession session = req.getSession();
			User user = (User) session
					.getAttribute(Constant.Session.User_Session);
			if (null == user) {
				request.getRequestDispatcher(this.login_page).forward(request,
						response);
			} else {
				chain.doFilter(req, resp);
			}
		}

	}


	public void destroy() {

	}

}
