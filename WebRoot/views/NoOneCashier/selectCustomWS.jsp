<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/8/16
  Time: 下午 4:23
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
    <style>

    </style>
</head>
<body style="height: 100%;width: 100%">
<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
    <div class="center">
        <div class="panel panel-default left-panel">
            <div class="panel-body">
                <div class="widget-body">
                    <input  class=" col-lg-10 col-md-10 col-sm-10" type="text" style="width: 100%;text-align:center" placeholder="请输入编号，用户名和手机号">
                    <input  class=" col-lg-10 col-md-10 col-sm-10" type="text" style="width: 100%;display: none">
                </div>
            </div>
        </div>
    </div>

    <div class="center" style="height: 70%">
        <div class="panel panel-default left-panel">
            <div class="panel-body">
                <div class="widget-body">
                   <%--<div>
                       asdasd
                   </div>
                    <hr width="100%">
                    <div>
                        asdasd
                    </div>
                    <hr width="100%">--%>
                       <div style="overflow-y:scroll;width:100%;height: 100%;white-space:nowrap;">
                           <div>
                               asdasd
                           </div>
                           <hr width="100%">
                           <div>
                               asdasd
                           </div>
                           <hr width="100%">
                           <div>
                               asdasd
                           </div>
                           <hr width="100%">
                           <div>
                               asdasd
                           </div>
                           <hr width="100%">
                           <div>
                               asdasd
                           </div>
                           <hr width="100%">
                           <div>
                               asdasd
                           </div>
                           <hr width="100%">
                           <div>
                               asdasd
                           </div>
                           <hr width="100%">
                           <div>
                               asdasd
                           </div>
                           <hr width="100%">
                           <div>
                               asdasd
                           </div>
                           <hr width="100%">
                           <div>
                               asdasd
                           </div>
                           <hr width="100%">
                       </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    function a() {
        alert(123);
    }
</script>
</body>
</html>
