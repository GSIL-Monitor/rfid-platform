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

    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/chosen.css" />
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/datepicker.css" />
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/bootstrap-timepicker.css"/>
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/bootstrap-multiselect.css" />
    <link rel="stylesheet" href="<%=basePath%>/Olive/assets/css/bootstrap-select.css"/>
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/jquery-ui.css" />
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/jquery.gritter.css" />

    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/ui.jqgrid.css" />
    <link rel="stylesheet" href="<%=basePath%>Olive/plugin/bootstrap-validator/css/bootstrapValidator.min.css" />

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
    <style>
        html { overflow-x:hidden; }
        .ui-jqgrid tr.jqgrow td {font-size:15px;}
    </style>

    <script>

    </script>
</head>
