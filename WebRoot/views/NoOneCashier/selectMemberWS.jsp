<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/8/15
  Time: 下午 1:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<html>
<!DOCTYPE html>
<head>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>
    <script src="<%=basePath%>/Olive/assets/js/jquery.js"></script>
    <script src="<%=basePath%>/Olive/assets/js/bootstrap.js"></script>
    <script src="<%=basePath%>/Olive/assets/js/bootbox.js"></script>
    <link rel="stylesheet" href="<%=basePath%>/Olive/assets/css/ace.css"/>
    <link rel="stylesheet" href="<%=basePath%>/Olive/assets/css/bootstrap-multiselect.css">
    <link rel="stylesheet" href="<%=basePath%>/Olive/assets/css/datepicker.css">
    <link rel="stylesheet" href="<%=basePath%>/Olive/assets/css/bootstrap.css">
    <link rel="stylesheet" href="<%=basePath%>/Olive/assets/css/jquery-ui.custom.css">
</head>
<body>
  <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12" style="">
     <div class="center">
         <div class="panel panel-default left-panel">
             <div class="panel-body">
                 <div class="widget-body">
                     <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                       <button class="col-xs-10 col-sm-10 col-md-10 col-lg-10" style="padding: 50px;height: 200px">
                           asdasd
                       </button>
                     </div>
                     <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
                         <button class="col-xs-10 col-sm-10 col-md-10 col-lg-10" style="height: 200px">
                             asdasd
                         </button>
                     </div>
                 </div>
             </div>
         </div>
     </div>
  </div>
</body>
</html>
