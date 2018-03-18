<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/9/21
  Time: 15:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<html>
<head>
    <link rel="stylesheet" href="<%=basePath%>/Olive/assets/css/fakeloader.css">
    <style>
        .transparent_class {
            filter: alpha(opacity=25);
            -moz-opacity: 0.25;
            -khtml-opacity: 0.25;
            opacity: 0.25;
            background-color: #277de7;
            position: fixed;
            top: 0px;
            left: 0px;
            width: 100%;
            height: 100%;
        }
    </style>
</head>
<body>
<div id="fakeLoader" class="transparent_class" style="display: none">
    <div class="fl spinner2">
        <div class="spinner-container container1">
            <div class="circle1"></div>
            <div class="circle2"></div>
            <div class="circle3"></div>
            <div class="circle4"></div>
        </div>
        <div class="spinner-container container2">
            <div class="circle1"></div>
            <div class="circle2"></div>
            <div class="circle3"></div>
            <div class="circle4"></div>
        </div>
        <div class="spinner-container container3">
            <div class="circle1"></div>
            <div class="circle2"></div>
            <div class="circle3"></div>
            <div class="circle4"></div>
        </div>
    </div>
</div>

<script>
    $(function () {
        $(window).load(function () {
            centerLoader();
            $(window).resize(function () {
                centerLoader();
            });
        });
    });

    function centerLoader() {

        var winW = $(window).width();
        var winH = $(window).height();

        var spinnerW = $('.fl').outerWidth();
        var spinnerH = $('.fl').outerHeight();

        $('.fl').css({
            'position': 'absolute',
            'left': (winW / 2) - (spinnerW / 2),
            'top': (winH / 2) - (spinnerH / 2)
        });

    }

    function showWaitingPage() {
        $("#fakeLoader").show();

    }

    function hideWaitingPage() {
        $("#fakeLoader").hide();

    }

</script>
</body>
</html>
