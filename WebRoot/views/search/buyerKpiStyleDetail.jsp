<%--
  Created by IntelliJ IDEA.
  User: MMI
  Date: 2018/9/20
  Time: 17:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page pageEncoding="UTF-8" %>
<%String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="searchBaseView.jsp"/>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
        var buyerId = "${buyerId}";
        var startDate = "${startDate}";
        var endDate = "${endDate}";
    </script>
</head>

<body class="no-skin">
<div class="main-container" id="main-container" style="overflow-x:hidden;">
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
                        <div class="widget-body ">
                            <div class="widget-toolbox padding-8 clearfix">
                                <div class="btn-toolbar" role="toolbar">
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-info" onclick="refresh();">
                                            <i class="ace-icon fa fa-refresh"></i>
                                            <span class="bigger-110">刷新</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-info" onclick="newChooseExportFunction();">
                                            <i class="ace-icon fa fa-file-excel-o"></i>
                                            <span class="bigger-110">导出</span>
                                        </button>
                                    </div>
                                    <%--<div class="btn-group btn-group-sm pull-left">--%>
                                        <%--<button class="btn btn-info" onclick="collapseGroup('#searchGrid');">--%>
                                            <%--<i class="ace-icon fa fa-chevron-up"></i>--%>
                                            <%--<span class="bigger-110">折叠分组</span>--%>
                                        <%--</button>--%>
                                        <%--<button class="btn  btn-info" onclick="expandGroup('#searchGrid');">--%>
                                            <%--<i class="ace-icon fa fa-chevron-down"></i>--%>
                                            <%--<span class="bigger-110">展开分组</span>--%>
                                        <%--</button>--%>
                                    <%--</div>--%>
                                    <%--<div class="btn-group btn-group-sm pull-left">--%>
                                        <%--<button class="btn btn-info" onclick="today();">--%>
                                            <%--<i class="ace-icon fa fa-chevron-right"></i>--%>
                                            <%--<span class="bigger-110">当天</span>--%>
                                        <%--</button>--%>
                                        <%--<button class="btn  btn-info" onclick="thisMonth();">--%>
                                            <%--<i class="ace-icon fa fa-chevron-right"></i>--%>
                                            <%--<span class="bigger-110">本月</span>--%>
                                        <%--</button>--%>
                                        <%--<button class="btn  btn-info" onclick="lastMonth();">--%>
                                            <%--<i class="ace-icon fa fa-chevron-right"></i>--%>
                                            <%--<span class="bigger-110">上月</span>--%>
                                        <%--</button>--%>
                                    <%--</div>--%>
                                    <div class="btn-group btn-group-sm pull-right">
                                        <button class="btn btn-info" onclick="showAdvSearchPanel();">
                                            <i class="ace-icon fa fa-binoculars"></i>
                                            <span class="bigger-110">高级查询</span>
                                        </button>

                                        <div class="btn-group btn-group-sm pull-right" style="margin-left: 5px">
                                            <button type="button" class="btn btn-info btn-yellow"
                                                    onclick="javascript:history.back(-1)">
                                                <i class="ace-icon fa fa-arrow-left"></i>
                                                <span class="bigger-110">返回</span>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="widget-main" id="searchPanel" style="display:none;">
                                <form class="form-horizontal" role="form" id="searchForm">
                                    <div class="form-group">
                                        <div>
                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                                   for="filter_gte_billDate">日期</label>
                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                <div class="input-group">
                                                    <input id="filter_gte_billDate" class="form-control date-picker"
                                                           name="filter_gte_billDate" data-date-format="yyyy-mm-dd"
                                                           autocomplete="off" value="${startDate}"/>
                                                    <span class="input-group-addon">
                                                                <i class="fa fa-exchange"></i>
                                                          </span>
                                                    <input id="filter_lte_billDate" class="form-control date-picker"
                                                           name="filter_lte_billDate" data-date-format="yyyy-mm-dd"
                                                           autocomplete="off" value="${endDate}"/>
                                                </div>
                                            </div>
                                        </div>
                                        <%--<div>--%>
                                            <%--<label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"--%>
                                                   <%--for="filter_contains_buyerId">买手</label>--%>
                                            <%--<div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">--%>
                                                <%--<input class="form-control" id="filter_contains_buyerId"--%>
                                                       <%--name="filter_contains_buyerId" type="text"--%>
                                                       <%--value="${buyerId}"/>--%>
                                            <%--</div>--%>
                                        <%--</div>--%>
                                        <div>
                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                                   for="filter_contains_buyerId">买手</label>
                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                <select class="form-control selectpicker show-tick" id="filter_contains_buyerId"
                                                        name="filter_contains_buyerId"
                                                        style="width: 100%;" data-live-search="true">
                                                </select>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <div class="col-sm-offset-5 col-sm-10">
                                            <button type="button" class="btn btn-sm btn-primary" onclick="search();">
                                                <i class="ace-icon fa fa-search"></i>
                                                <span class="bigger-110">查询</span>
                                            </button>
                                            <button type="reset" class="btn btn-sm btn-warning" onclick="_reset();">
                                                <i class="ace-icon fa fa-undo"></i>
                                                <span class="bigger-110">清空</span></button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <div id="searchGrid" style="height:800px"></div>
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

<jsp:include page="search_js.jsp"/>
<script type="text/javascript" src="<%=basePath%>/views/search/buyerKpiStyleDetailController.js"></script>
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/dateFormatUtil.js"></script>
</body>
</html>
