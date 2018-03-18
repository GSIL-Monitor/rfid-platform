<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/9/8
  Time: 11:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
        var curOwnerId = "${ownerId}";
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
                                    <div class="btn-group btn-group-sm pull-left" onclick="refresh()">
                                        <button class="btn btn-info">
                                            <i class="ace-icon fa fa-refresh"></i> <span class="bigger-110">刷新</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-right">
                                        <button type="button" class="btn btn-info" onclick="showAdvSearchPanel()">
                                            <i class="ace-icon fa fa-binoculars"></i>
                                            <span class="bigger-110">高级查询</span>
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="hr hr-2 hr-dotted"></div>
                            <div class="widget-main" id="searchPanel" hidden>
                                <form class="form-horizontal" role="form" id="searchForm">
                                    <div class="form-group">
                                        <label class="col-xs-1 control-label" for="search_styleId">货号</label>

                                        <div class="col-xs-2">
                                            <input class="form-control" id="search_styleId"
                                                   name="filter_LIKES_styleId" type="text" placeholder="模糊查询"/>
                                        </div>
                                        <label class="col-xs-1 control-label text-right"
                                               for="search_businessId">销售员</label>

                                        <div class="col-xs-2">
                                            <input class="form-control" id="search_businessId"
                                                   name="filter_LIKES_businessId" type="text" placeholder="模糊查询"/>
                                        </div>
                                        <label class="col-xs-1 control-label text-right"
                                               for="search_recordStartTime">穿着日期 </label>

                                        <div class="col-xs-2">
                                            <div class="input-group">
                                                <input class="form-control date-picker"
                                                       id="search_recordStartTime" type="text"
                                                       name="filter_GED_recordStartTime"
                                                       data-date-format="yyyy-mm-dd"/>
                                                <span class="input-group-addon">
                                                    <i class="fa fa-exchange"></i>
                                                </span>
                                                <input class="form-control date-picker"
                                                       name="filter_LED_recordStartTime" type="text"
                                                       data-date-format="yyyy-mm-dd"/>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-11 btnPosition">
                                            <button type="button" class="btn btn-sm btn-primary" onclick="_search()">
                                                <i class="ace-icon fa fa-search"></i>
                                                <span class="bigger-110">查询</span>
                                            </button>
                                            <button type="reset" class="btn btn-sm btn-warning" onclick="_clearSearch()">
                                                <i class="ace-icon fa fa-undo"></i>
                                                <span class="bigger-110">清空</span>
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    <!-- PAGE CONTENT ENDS -->
                </div>
                <!-- /.col -->
            </div>
            <!-- /.row -->
            <!--/#page-content-->
        </div>
    </div>

    <div class="hr hr4"></div>

    <div class="widget-box transparent">
        <div class="widget-header ">
            <div class="no-border">
                <div class="btn-group btn-group-sm pull-left">
                    <button class="btn btn-primary" onclick="dressing()">
                        <i class="ace-icon fa fa-plus"></i> <span class="bigger-110">套版</span>
                    </button>
                    <button class="btn btn-primary" onclick="returnBack()">
                        <i class="ace-icon fa fa-edit"></i> <span class="bigger-110">归还</span>
                    </button>
                </div>
                <div class="form-group form-group-sm pull-left">
                    <label class="col-xs-2 control-label text-right"
                           for="form_dressCode">扫码</label>
                    <div class="col-xs-4">
                        <input class="form-control" id="form_dressCode"
                               name="dressCode" type="text"/>
                    </div>
                    <label class="col-xs-2 control-label text-right"
                           for="form_businessId">销售员</label>
                    <div class="col-xs-4">
                        <select class="form-control selectpicker show-tick" id="form_businessId"
                                name="businessId"
                                style="width: 100%;" data-live-search="true">
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <div class="widget-body">
            <div class="widget-main padding-12 no-padding-left no-padding-right">
                <div class="tab-content padding-4">
                    <div id="addDetail" class="tab-pane in active" style="height:80%;">
                        <table id="grid"></table>
                        <div id="grid-pager"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>
</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<script src="<%=basePath%>/views/shop/dressRecordController.js"></script>
</body>
</html>
