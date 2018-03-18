<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../search/searchBaseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>
<style>
    .customClass{
        color: red;
    }
    .errorData{
        color:red;
        text-decoration: line-through;
    }
</style>
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
                                        <button class="btn  btn-primary" onclick="signBill();">

                                            <i class="ace-icon fa fa-file-excel-o"></i>
                                            <span class="bigger-110">标记错误记录</span>

                                        </button>

                                        <button class="btn  btn-primary" onclick="unSignBill();">

                                            <i class="ace-icon fa fa-file-excel-o"></i>
                                            <span class="bigger-110">取消标记</span>

                                        </button>

                                        <button class="btn  btn-primary" onclick="collapseGroup('#searchGrid');">
                                            <i class="ace-icon fa fa-chevron-up"></i>
                                            <span class="bigger-110">折叠分组</span>
                                        </button>

                                        <button class="btn  btn-primary" onclick="expandGroup('#searchGrid');">
                                            <i class="ace-icon fa fa-chevron-down"></i>
                                            <span class="bigger-110">展开分组</span>
                                        </button>

                                        <button class="btn  btn-primary" onclick="exportExcel();">

                                            <i class="ace-icon fa fa-file-excel-o"></i>
                                            <span class="bigger-110">导出</span>

                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-right">

                                        <button class="btn btn-info" onclick="showAdvSearchPanel();">
                                            <i class="ace-icon fa fa-binoculars"></i>
                                            <span class="bigger-110">高级查询</span>
                                        </button>

                                    </div>
                                </div>
                            </div>
                            <div class="widget-main" id="searchPanel" style="display:none;">
                                <form class="form-horizontal" role="form" id="searchForm">
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               for="filter_contains_billNo">办单单号</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="filter_contains_billNo"
                                                   name="filter_contains_billNo" type="text"
                                                   placeholder="模糊查询"/>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               for="filter_in_taskOperator">操作员</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="chosen-select form-control" id="filter_in_taskOperator"
                                                    name="filter_contains_taskOperator" data-placeholder="员工列表">
                                            </select>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               for="filter_eq_sign">是否错误</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="chosen-select form-control" id="filter_eq_sign" name="filter_eq_sign">
                                                <option value="">-请选择-</option>
                                                <option value="Y">是</option>
                                                <option value="N">否</option>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               for="filter_in_token">任务类型</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="chosen-select form-control" id="filter_in_token"
                                                    name="filter_in_token" data-placeholder="任务列表">
                                            </select>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               for="filter_in_groupCode">操作员组</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="chosen-select form-control" id="filter_in_groupCode"
                                                    name="filter_in_groupCode" data-placeholder="员工组列表">
                                            </select>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               for="filter_contains_customerId">客户</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="filter_contains_customerId"
                                                   name="filter_contains_customerId" type="text"
                                                   placeholder="模糊查询"/>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               for="filter_in_category">产品</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="chosen-select form-control" id="filter_in_category"
                                                    name="filter_in_category" data-placeholder="产品列表">
                                            </select>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               >任务时间</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <div class="input-group">
                                                <input id="filter_gte_scanTime" class="form-control date-picker startDate"
                                                       name="filter_gte_taskDate"
                                                       readonly
                                                       data-date-format="yyyy-mm-dd"/>
                                                <span class="input-group-addon">
															<i class="fa fa-exchange"></i>
                                                      </span>
                                                <input id="filter_lte_scanTime" class="form-control date-picker endDate"
                                                       name="filter_lte_taskDate"
                                                       readonly
                                                       data-date-format="yyyy-mm-dd"/>
                                            </div>
                                        </div>

                                    </div>
                                    <div class="form-group">
                                        <div class="col-sm-offset-5 col-sm-10">
                                            <button type="button" class="btn btn-sm btn-primary" onclick="search();">
                                                <i class="ace-icon fa fa-search"></i>
                                                <span class="bigger-110">查询</span>
                                            </button>
                                            <button type="button" class="btn btn-sm btn-warning" onclick="resetData();">
                                                <i class="ace-icon fa fa-undo"></i>
                                                <span class="bigger-110">清空</span></button>
                                        </div>
                                    </div>

                                </form>

                            </div>
                        </div>
                        <div id="searchGrid" style="height:800px"></div>
                    </div>

                </div>

            </div>
        </div>
    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>

</div>
<jsp:include page="../search/search_js.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/factory/dateFormatUtil.js"></script>
<script type="text/javascript" src="<%=basePath%>/views/factory/factoryBillDtlSearchController.js"></script>
</body>
</html>