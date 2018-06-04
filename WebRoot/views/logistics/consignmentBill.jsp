<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017-06-29
  Time: 上午 10:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page pageEncoding="UTF-8" language="java" import="java.util.*" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script>
        var basePath = '<%=basePath%>';
        var curOwnerId = "${ownerId}";
        var ownersId="${ownersId}";
        var billNo = "${billNo}";
        var userId = "${userId}";
    </script>

</head>
<body class="no-skin">
<div class="main-container" id="main-container">
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
                                        <button type="button" class="btn btn-primary" onclick="addSaleReturnBill()">
                                            <i class="ace-icon fa fa-plus"></i>
                                            <span class="bigger-110">新增</span>
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
                            <div class="hr hr4"></div>
                            <div class="widget-main" id="searchPanel" style="display:none">
                                <form class="form-horizontal" role="form" id="searchForm">
                                    <div class="form-group">
                                        <label class="col-xs-1 control-label text-right"
                                               for="search_billId">单号</label>
                                        <div class="col-xs-2">
                                            <input class="form-control" id="search_billId"
                                                   name="filter_LIKES_billNo"
                                                   type="text" onkeyup="this.value=this.value.toUpperCase()"
                                                   placeholder="模糊查询"/>
                                        </div>

                                        <label class="col-xs-1 control-label" for="search_billDate">创建日期</label>
                                        <div class="col-xs-2">
                                            <div class="input-group">
                                                <input class="form-control date-picker" id="search_billDate"
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
                                        <label class="col-xs-1 control-label" for="search_origUnitId">客户</label>
                                        <div class="col-xs-2">
                                            <div class="input-group">
                                                <input class="form-control" id="search_origUnitId" type="text"
                                                       name="filter_EQS_origUnitId" readonly/>
                                                <span class="input-group-btn">
                                                    <button class="btn btn-sm btn-default" id="search_guest_button"
                                                            type="button" onclick="openSearchGuestDialog()">
                                                        <i class="ace-icon fa fa-list"></i>
                                                    </button>
											    </span>
                                                <input class="form-control" id="search_origUnitName" type="text"
                                                       name="origUnitName"  readonly/>
                                            </div>
                                        </div>
                                    </div>
                                    <%--<div class="form-group">
                                        &lt;%&ndash;<label class="col-xs-1 control-label" for="search_origId">出库仓库</label>
                                        <div class="col-xs-2">
                                            <select class="form-control" id="search_origId" name="filter_LIKES_origId" style="width: 100%;">
                                            </select>
                                        </div>&ndash;%&gt;
                                        <label class="col-xs-1 control-label" for="select_outStatus">出库状态</label>
                                        <div class="col-xs-2">
                                            <select class="form-control" id="select_outStatus"
                                                    name="filter_EQI_outStatus">
                                                <option>--请选择--</option>
                                                <option value="0">订单状态</option>
                                                <option value="2">已出库</option>
                                                <option value="3">出库中</option>
                                            </select>
                                        </div>
                                    </div>--%>
                                    <div class="form-group">
                                        <label class="col-xs-1 control-label" for="search_destId">入库仓库</label>
                                        <div class="col-xs-2">
                                            <select class="form-control selectpicker show-tick" id="search_destId" name="filter_LIKES_destId"
                                                    style="width: 100%;" data-live-search="true">
                                            </select>
                                        </div>
                                        <label class="col-xs-1 control-label" for="select_inStatus">入库状态</label>
                                        <div class="col-xs-2">
                                            <select class="form-control selectpicker show-tick" id="select_inStatus"
                                                    name="filter_EQI_inStatus"  data-live-search="true">
                                                <option value="">--请选择--</option>
                                                <option value="0">订单状态</option>
                                                <option value="1">已入库</option>
                                                <option value="4">入库中</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-sm-offset-5 col-sm-10">
                                            <button type="button" class="btn btn-sm btn-primary" onclick="_search()">
                                                <i class="ace-icon fa fa-search"></i>
                                                <span class="bigger-110">查询</span>
                                            </button>
                                            <button type="reset" class="btn btn-sm btn-warning", onclick="_reset()">
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
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="../base/search_guest_dialog.jsp"></jsp:include>
<link href="<%=basePath%>/kendoUI/styles/kendo.common-material.min.css" rel="stylesheet">
<link href="<%=basePath%>/kendoUI/styles/kendo.rtl.min.css" rel="stylesheet">
<link href="<%=basePath%>/kendoUI/styles/kendo.material.min.css" rel="stylesheet">
<script src="<%=basePath%>/kendoUI/js/kendo.all.min.js"></script>
<script src="<%=basePath%>/views/logistics/consignmentBillController.js"></script>
<script src="<%=basePath%>/Olive/plugin/print/LodopFuncs.js"></script>

</body>
</html>
