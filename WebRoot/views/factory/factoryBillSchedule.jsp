<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <%--<jsp:include page="../search/searchBaseView.jsp"></jsp:include>--%>
        <link href="<%=basePath%>/kendoUI/styles/kendo.common-material.min.css" rel="stylesheet">
        <link href="<%=basePath%>/kendoUI/styles/kendo.rtl.min.css" rel="stylesheet">
        <link href="<%=basePath%>/kendoUI/styles/kendo.material.min.css" rel="stylesheet">
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>
    <style>
        .ui-th-div{
            text-align: center;
        }
    </style>

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
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button id="add_schedule_button" type="button" class="btn btn-primary" onclick="showAddDialog()">
                                            <i class="ace-icon fa fa-file-excel-o"></i>
                                            <span class="bigger-110">新增排期</span>
                                        </button>
                                        <button type="button" class="btn btn-primary" onclick="exportExcel()">
                                            <i class="ace-icon fa fa-file-excel-o"></i>
                                            <span class="bigger-110">导出</span>
                                        </button>

                                    </div>

                                    <div class="btn-group btn-group-sm pull-right">

                                        <button type="button" class="btn btn-info" onclick="showAdvSearchPanel();">
                                            <i class="ace-icon fa fa-binoculars"></i>
                                            <span class="bigger-110">高级查询</span>
                                        </button>

                                    </div>
                                </div>
                            </div>
                            <div class="hr hr-2 hr-dotted"></div>

                            <div class="widget-main" id="searchPanel" style="display:none">
                                <form class="form-horizontal" role="form" id="searchForm" method='post'>
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 text-right control-label"
                                               for="search_billNo">办单单号</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="search_billNo" name="filter_LIKES_billNo"
                                                   type="text" placeholder="模糊查询"/>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               for="search_progress">办单进度 </label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="chosen-select form-control" id="search_progress" name="filter_LIKES_progress">
                                            </select>
                                        </div>


                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 text-right control-label"
                                               for="search_category">产品</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="form-control" id="search_category" name="filter_INS_category">
                                            </select>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 text-right control-label"
                                               for="search_customerId">客户</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class=" form-control" id="search_customerId" type="text"
                                                    name="filter_LIKES_customerId" placeholder="模糊查询"/>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 text-right control-label"
                                               for="search_sex">男/女装</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="search_sex" name="filter_LIKES_sex"
                                                   type="text" placeholder="模糊查询"/>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 text-right control-label"
                                               for="search_type">办类</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="search_type" type="text" name="filter_LIKES_type" placeholder="模糊查询"/>

                                        </div>
                                    </div>


                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 text-right control-label"
                                               for="search_shirtType">衫型</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="search_shirtType"
                                                   name="filter_LIKES_shirtType"
                                                   type="text" placeholder="模糊查询"/>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 text-right control-label"
                                               for="search_isSchedule">是否排期</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="form-control chosen-select" id="search_isSchedule"
                                                    name="filter_EQS_isSchedule" onchange= "changeSchedule()">
                                                <option value="N">未排期</option>
                                                <option value="Y">已排期</option>
                                                <option value="">全部</option>

                                            </select>
                                        </div>


                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 text-right control-label"
                                               for="search_factory">工厂</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="search_factory" name="filter_LIKES_factory"
                                                   type="text" placeholder="模糊查询"/>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 text-right control-label"
                                        >发单时间</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <div class="input-group">
                                                <input id="filter_GED_billDate" class="form-control date-picker startDate"
                                                       name="filter_GED_billDate"
                                                       readonly
                                                       data-date-format="yyyy-mm-dd"/>
                                                <span class="input-group-addon">
															<i class="fa fa-exchange"></i>
                                                      </span>
                                                <input id="filter_LED_billDate" class="form-control date-picker endDate"
                                                       name="filter_LED_billDate"
                                                       readonly
                                                       data-date-format="yyyy-mm-dd"/>
                                            </div>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 text-right control-label"
                                        >排期时间</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <div class="input-group">
                                                <input id="filter_GED_scheduleDate" class="form-control date-picker startDate"
                                                       name="filter_GED_scheduleDate"
                                                       readonly
                                                       data-date-format="yyyy-mm-dd"/>
                                                <span class="input-group-addon">
															<i class="fa fa-exchange"></i>
                                                      </span>
                                                <input id="filter_LED_scheduleDate" class="form-control date-picker endDate"
                                                       name="filter_LED_scheduleDate"
                                                       readonly
                                                       data-date-format="yyyy-mm-dd"/>
                                            </div>
                                        </div>


                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 text-right control-label"
                                                >打印时间</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <div class="input-group">
                                                <input id="filter_GED_printDate" class="form-control date-picker startDate"
                                                       name="filter_GED_printDate"
                                                       readonly
                                                       data-date-format="yyyy-mm-dd"/>
                                                <span class="input-group-addon">
															<i class="fa fa-exchange"></i>
                                                      </span>
                                                <input id="filter_LED_printDate" class="form-control date-picker endDate"
                                                       name="filter_LED_printDate"
                                                       readonly
                                                       data-date-format="yyyy-mm-dd"/>
                                            </div>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 text-right control-label"
                                               for="search_isSchedule">过滤打印时间</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="form-control chosen-select" id="search_isPrint"
                                                    name="search_isPrint" onchange= "changePrint()">
                                                <option value="N">否</option>
                                                <option value="Y">是</option>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-11 btnPosition">
                                            <button type="button" class="btn btn-sm btn-primary" onclick="_search()">
                                                <i class="ace-icon fa fa-search"></i>
                                                <span class="bigger-110">查询</span>
                                            </button>
                                            <button type="button" class="btn btn-sm btn-warning"
                                                    onclick="_clearSearch()">
                                                <i class="ace-icon fa fa-undo"></i>
                                                <span class="bigger-110">清空</span></button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <table id="grid"></table>
                        <div id="grid-pager"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>

</div>
<script src="<%=basePath%>/kendoUI/js/jszip.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/jquery.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/kendo.all.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/kendo.timezones.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/cultures/kendo.culture.zh-CN.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/messages/kendo.messages.zh-CN.min.js"></script>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>

<jsp:include page="billSchedule_dialog.jsp"></jsp:include>
<jsp:include page="billSchedule_add.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/factory/dateFormatUtil.js"></script>
<script type="text/javascript" src="<%=basePath%>/views/factory/factoryBillScheduleController.js"></script>
</body>
</html>