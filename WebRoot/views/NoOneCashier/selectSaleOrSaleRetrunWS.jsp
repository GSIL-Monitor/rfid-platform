<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/8/16
  Time: 下午 2:40
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
        .btn1 {
            width:50%;
            height:90%;
            background:url('<%=basePath%>/images/pad/sale.png')  no-repeat left top;
            background-size:contain;
            border:none;
            color:#0a0a0a;
        }
        .btn2 {
            width:50%;
            height:90%;
            background:url('<%=basePath%>/images/pad/retreat.png')  no-repeat left top;
            background-size:contain;
            border:none;
            color:#0a0a0a;
        }
        .center-vertical {
            position:relative;
            top:50%;
            transform:translateY(-50%)
        }
        .row3{
            height: 80%;
        }
        .bottom-button{
            color:white;
            background-color:#307a3c;
            height:8%;
            position: fixed;
            right: 10px;
            bottom: 10px;
            border-color:#307a3c
        }
    </style>
</head>
<body style="height: 100% ; width: 100%">
<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
    <div class="center">
        <div class="panel panel-default left-panel">
            <div class="panel-body">
                <div class="widget-body">
                    <div class=" row3 col-lg-6 col-md-6 col-sm-6" style="text-align:center">
                        <button class="btn1  center-vertical" id="sale" type="button" ></button>
                    </div>
                    <div class=" row3 col-lg-6 col-md-6 col-sm-6" style="text-align:center">
                        <button class="btn2  center-vertical" id="saleRetrun" type="button" ></button>
                    </div>
                </div>
            </div>
        </div>
        <button class="col-xs-2 col-sm-2 col-md-2 col-lg-2 bottom-button" onclick="onBack()">上一页</button>
    </div>

</div>
<script>
    $(function () {
        //会员客户和普通客户的正方形
        var vipWidth=$("#sale").width();
        var vipHeight=$("#sale").height();
        if(vipWidth<=vipHeight) {
            $("#sale").css("height", vipWidth);
            $("#sale").css("width", vipWidth);
        }else{
            $("#sale").css("height", vipHeight);
            $("#sale").css("width", vipHeight);
        }
        var vipWidth=$("#saleRetrun").width();
        var vipHeight=$("#saleRetrun").height();
        if(vipWidth<=vipHeight) {
            $("#saleRetrun").css("height", vipWidth);
            $("#saleRetrun").css("width", vipWidth);
        }else{
            $("#saleRetrun").css("height", vipHeight);
            $("#saleRetrun").css("width", vipHeight);
        }

    });
    function onBack() {
        window.location.href=basePath+'/views/NoOneCashier/selectMemberWS.jsp';
    }
</script>
</body>
</html>
