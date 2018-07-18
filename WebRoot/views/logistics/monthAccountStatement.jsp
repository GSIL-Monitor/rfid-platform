<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/7/10
  Time: 10:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<html>
<!DOCTYPE html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>
</head>

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
                    <div class="col-xs-12">
                        <!-- PAGE CONTENT BEGINS -->
                        <div class="widget-body">

                            <div class="widget-toolbox padding-8 clearfix">
                                <div class="btn-toolbar" role="toolbar">
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-info" onclick="refresh()">
                                            <i class="ace-icon fa fa-refresh"></i>
                                            <span class="bigger-110">刷新</span>
                                        </button>
                                    </div>
                                    <%--<div class="btn-group btn-group-sm pull-left">--%>
                                        <%--<button type="button" class="btn btn-primary" onclick="add()">--%>
                                            <%--<i class="ace-icon fa fa-plus"></i>--%>
                                            <%--<span class="bigger-110">新增</span>--%>
                                        <%--</button>--%>
                                    <%--</div>--%>
                                    <div class="btn-group btn-group-sm pull-right">
                                        <button type="button" class="btn btn-info" onclick="showAdvSearchPanel();">
                                            <i class="ace-icon fa fa-binoculars"></i>
                                            <span class="bigger-110">高级查询</span>
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="hr hr4"></div>

                            <div class="widget-main" id="searchPanel" style="display: none">
                                <form class="form-horizontal" role="form" id="searchForm">

                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               for="search_billId">单号</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="search_billId" name="" filter_LIKES_billNo
                                                   type="text" onkeyup="this.value=this.value.toUpperCase()"
                                                   placeholder="模糊查询"/>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               for="search_createTime">创建日期 </label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <div class="input-group">
                                                <input class="form-control date-picker" id="search_createTime"
                                                       type="text" name="filter_GED_billDate"
                                                       data-date-format="yyyy-mm-dd"/>

                                                <span class="input-group-addon">
                                                    <i class="fa fa-exchange"></i>
                                                </span>

                                                <input class="form-control date-picker" type="text"
                                                       class="input-sm form-control" name="filter_LED_billDate"
                                                       data-date-format="yyyy-mm-dd"/>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <div class="col-sm-offset-5 col-sm-10">
                                            <button type="button" class="btn btn-sm btn-primary" onclick="_search()">
                                                <i class="ace-icon fa fa-search"></i>
                                                <span class="bigger-110">查询</span>
                                            </button>
                                            <button type="reset" class="btn btn-sm btn-warning">
                                                <i class="ace-icon fa fa-undo"></i>
                                                <span class="bigger-110">清空</span></button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <table id="grid"></table>
                        <div id="grid-pager"></div>
                        <!-- PAGE CONTENT ENDS -->
                    </div>
                    <!-- /.col -->
                </div>
                <!-- /.row -->
                <!--/#page-content-->
            </div>
        </div>
    </div>


    <!--/.fluid-container#main-container-->
</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<link href="<%=basePath%>/kendoUI/styles/kendo.common-material.min.css" rel="stylesheet">
<link href="<%=basePath%>/kendoUI/styles/kendo.rtl.min.css" rel="stylesheet">
<link href="<%=basePath%>/kendoUI/styles/kendo.material.min.css" rel="stylesheet">
<script src="<%=basePath%>/kendoUI/js/kendo.all.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/kendo.timezones.min.js"></script>

<script src="<%=basePath%>/kendoUI/js/cultures/kendo.culture.zh-CN.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/messages/kendo.messages.zh-CN.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/views/logistics/monthAccountStatementController.js"></script>

</body>
</html>