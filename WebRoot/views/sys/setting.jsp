
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>

</head>
<style>
    input[type=checkbox].ace.ace-switch.ace-switch-5 + .lbl::before {
        content: "是\a0\a0\a0\a0\a0\a0\a0\a0\a0\a0\a0否";!important;
    }
</style>
<body class="no-skin">
<div class="main-container" id="main-container" style="">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>
    <div class="main-content">
        <div class="main-content-inner">
            <!-- /.page-header -->

            <div id="page-content">

                <div class="row">
                    <div class="col-xs-6 col-sm-6">
                        <div class="widget-box widget-color-blue  light-border">

                            <div class="widget-header">

                                <h5 class="widget-title">基本设置</h5>
                                <div class="widget-toolbar padding-5 no-border">
                                    <a href="#" data-action="collapse" >
                                        <i class="ace-icon fa fa-chevron-up"></i>
                                    </a>
                                </div>
                                 <div class="widget-toolbar no-border">
                                 <button class="btn btn-xs btn-light bigger" onclick="refreshCache();">
                                                                <i class="ace-icon fa fa-refresh"></i>
                                                                刷新缓存
                                                            </button>
                                                        </div>
                            </div>
                            <div class="widget-body">
                                <div class="widget-main no-padding">
                                    <br />
                                    <form class="form-horizontal" role="form" id="editForm">
                                        <c:forEach items="${settingList}" var="setting">
                                        <div class="form-group">
                                            <label class="col-xs-3 control-label" for="${setting.id}">${setting.name}</label>

                                            <div class="col-xs-4">
                                                <input class="form-control" id="${setting.id}" name="${setting.id}"
                                                       type="text" value="${setting.value}" readonly
                                                       />
                                            </div>
                                        </div>
                                        </c:forEach>

                                    </form>

                                </div>
                            </div>
                        </div>
                    </div>
                    <%--<div class="col-xs-12 col-sm-6">
                        <div class="widget-box widget-color-blue  light-border">
                            <div class="widget-header">
                                <h5 class="widget-title">TOKEN设置查询</h5>
                                <div class="widget-toolbar padding-5 no-border">
                                    <a href="#" data-action="collapse" >
                                        <i class="ace-icon fa fa-chevron-up"></i>
                                    </a>
                                </div>
                            </div>
                            <div class="widget-body">
                                <table id="token_grid"></table>
                                <div id="token_grid-pager"></div>
                            </div>
                        </div>
                    </div>--%>
                    <div class="col-xs-6 col-sm-6">
                        <div class="widget-box widget-color-blue  light-border">

                            <div class="widget-header">

                                <h5 class="widget-title">系统设置</h5>
                                <div class="widget-toolbar padding-5 no-border">
                                    <a href="#" data-action="collapse" >
                                        <i class="ace-icon fa fa-chevron-up"></i>
                                    </a>
                                </div>

                            </div>
                            <div class="widget-body">
                                <div class="widget-main no-padding">
                                    <br />
                                    <form class="form-horizontal" role="form" id="editForm1">
                                        <c:forEach items="${operateList}" var="operate">
                                            <div class="form-group">
                                                <label class="col-xs-3 control-label" for="${operate.id}">${operate.name}</label>

                                                <c:if test="${operate.id=='wxshop_Path'}">
                                                    <div class="col-xs-4">
                                                        <input class="form-control" id="${operate.id}" name="${operate.id}"
                                                               type="text" value="${operate.value}" readonly
                                                        />
                                                    </div>
                                                </c:if>
                                                <c:if test="${operate.id!='wxshop_Path'}">
                                                    <div class='btn-group btn-group-sm pull-left' style="line-height: 32px">
                                                        <span class='col-sm-10'>
                                                            <input style="width: 100%" type="checkbox" ${(operate.value=='true'?"checked":"")} onclick="setOperate('${operate.id}','${operate.value}')" class="ace ace-switch ace-switch-5">
                                                            <span class='lbl middle'></span>
                                                        </span>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:forEach>

                                    </form>

                                </div>
                            </div>
                        </div>
                    </div>

                </div>

            </div>
        </div>
    </div>

    <!--/.fluid-container#main-container-->
</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/sys/settingController.js"></script>

</body>
</html>