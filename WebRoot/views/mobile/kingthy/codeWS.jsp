<%@ page import="com.casesoft.dmc.extend.api.web.kingthy.callback.Good" %><%--
  Created by IntelliJ IDEA.
  User: john
  Date: 2017/1/10
  Time: 8:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
    <title>无锡凯施智联提供</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <link rel="stylesheet" href="<%=basePath%>/Olive/assets/css/bootstrap.css" />
    <script src="<%=basePath%>/Olive/assets/js/jquery.js"></script>
    <script src="<%=basePath%>/Olive/assets/js/bootstrap.js"></script>
    <style>

    </style>
</head>
<body>
<div class="container-fluid">
    <div class="table table-responsive">
        <table  border="0" cellspacing="0" cellpadding="0" class="table">
            <tr class=" label-primary">
                <th valign="right" scope="col" width="40%" ><span style="color:white">标题</span></th>
                <th valign="right"scope="col"><span style="color:white">内容</span></th>
            </tr>
            <tr class="active">
                <td>订单号</td>
                <td>${good.orderId}</td>
            </tr>
            <tr class="success">
                <td>收件人</td>
                <td>${good.cusName}</td>
            </tr>
            <tr class="active">
                <td>物流名称</td>
                <td>${good.logistical}</td>
            </tr>
            <tr class="success">
                <td>运送单号</td>
                <td>${good.trackNo}</td>
            </tr>
            <tr class="active">
                <td>成品条码</td>
                <td>${good.barcode2D}</td>
            </tr>
        </table>
    </div>

</div>
<div id="footer" class="container">
    <nav class="navbar navbar-default navbar-fixed-bottom">
        <div class="navbar-inner navbar-content-center">
            <p class="text-muted credit" style="padding: 20px">
            <div style="text-align:center">
						<span class="bigger-120"  >
							<span class="blue bolder">&copy; CaseSoft RFID</span>
							 @2011-2016 &nbsp;&nbsp;|&nbsp;&nbsp;<a href="http://www.casesoft.com.cn" target="_blank" style="text-decoration: none;">关于 CaseSoft</a>
						</span>

                &nbsp; &nbsp; 建议使用
                <span class="action-buttons">
							<a href="https://www.google.com/intl/zh-CN/chrome/browser/" target="_blank">
                                <i class="ace-icon fa fa-chrome light-blue bigger-110"></i>
                            </a>

							<a href="http://firefox.com.cn/download/" target="_blank">
                                <i class="ace-icon fa fa-firefox text-primary bigger-110"></i>
                            </a>

							<a href="http://windows.microsoft.com/zh-CN/internet-explorer/products/ie/home" target="_blank">
                                <i class="ace-icon fa fa-internet-explorer orange bigger-110"></i>
                            </a>
						</span>
            </div>
            </p>
        </div>
    </nav>
</body>
</html>
