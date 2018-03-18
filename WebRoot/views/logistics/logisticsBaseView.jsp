<%@ page language="java"
         import="java.util.*,com.casesoft.dmc.model.sys.User"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    User user = (User) session.getAttribute("userSession");
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>CaseSoft RFID大数据平台</title>

    <meta name="description" content="RFID &amp; 大数据" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/bootstrap.css" />
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/font-awesome.css" />

    <!-- page specific plugin styles -->


    <!-- text fonts -->
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/ace-fonts.css" />

    <!-- ace styles -->
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/ace.css" class="ace-main-stylesheet" id="main-ace-style" />

    <!--[if lte IE 9]>
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/ace-part2.css" class="ace-main-stylesheet" />
    <![endif]-->
    <!--[if lte IE 9]>
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/ace-ie.css" />

    <![endif]-->

    <!-- inline styles related to this page -->

    <!-- ace settings handler -->
    <script src="<%=basePath%>Olive/assets/js/ace-extra.js"></script>

    <!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

    <!--[if lte IE 8]>
    <script src="<%=basePath%>Olive/assets/js/html5shiv.js"></script>
    <script src="<%=basePath%>Olive/assets/js/respond.js"></script>
    <![endif]-->
    <link href="<%=basePath%>Olive/plugin/fileInput/css/fileinput.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%=basePath%>css/main.css" />
    <link href="<%=basePath%>jslib2/themes/insdep/easyui.css" rel="stylesheet" type="text/css">
    <link href="<%=basePath%>jslib2/themes/insdep//easyui_animation.css" rel="stylesheet" type="text/css">
    <!--
        easyui_animation.css
        Insdep对easyui的额外增加的动画效果样式，根据需求引入或不引入，此样式不会对easyui产生影响
    -->

    <link href="<%=basePath%>jslib2/themes/insdep/easyui_plus.css" rel="stylesheet" type="text/css">
    <!--
        easyui_plus.css
        Insdep对easyui的额外增强样式,内涵所有 insdep_xxx.css 的所有组件样式
        根据需求可单独引入insdep_xxx.css或不引入，此样式不会对easyui产生影响
    -->

    <link href="<%=basePath%>jslib2/themes/insdep/insdep_theme_default.css" rel="stylesheet" type="text/css">
    <!--
        insdep_theme_default.css
        Insdep官方默认主题样式,更新需要自行引入，此样式不会对easyui产生影响
    -->

    <link href="<%=basePath%>jslib2/themes/insdep/icon.css" rel="stylesheet" type="text/css">
    <!--
        icon.css
        美化过的easyui官方icons样式，根据需要自行引入
    -->

    <script type="text/javascript" src="<%=basePath%>jslib2/jquery.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>jslib2/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>jslib2/themes/insdep/jquery.insdep-extend.min.js"></script>

    <script>

    </script>
</head>
