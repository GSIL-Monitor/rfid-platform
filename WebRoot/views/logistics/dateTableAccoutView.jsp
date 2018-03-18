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
        var userId = "${userId}";
    </script>
    <style type="text/css">
        .data-container {
            /*display: inline-block;*/
            border: 1px solid #d7d7d7;
            /*border-right: 0px;*/
            width: 20%;
            margin-bottom: 10px;
            font-size: 12px;
            text-align: center;
            padding-top: 3px;
            padding-bottom: 3px;
        }
        .title {
            color: #53606b;
            font-size: 14px;
            line-height: 31px;
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
                                        <button class="btn btn-info" onclick="checkout()">
                                            <i class="ace-icon fa fa-refresh"></i>
                                            <span class="bigger-110">结账</span>
                                        </button>
                                    </div>
                                    <%--<div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-primary" onclick="showDetailPage()">
                                            <i class="ace-icon fa fa-list"></i>
                                            <span class="bigger-110">一览表</span>
                                        </button>
                                    </div>--%>
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
                                <form id="searchForm" class="form-horizontal" role="form">
                                    <div class="form-group">
                                        <%--<label class="col-xs-1 control-label" for="search_guestId">客户编号</label>--%>
                                        <div class="col-xs-2" hidden>
                                            <input class="form-control" id="search_guestId" name="filter_EQS_unitId"
                                                   type="text"  v/>
                                        </div>
                                            <label class="col-xs-1 control-label" for="search_destUnitId">客户</label>
                                            <div class="col-xs-2">
                                                <div class="input-group">
                                                    <input class="form-control" id="search_destUnitId" type="text"
                                                           name="filter_EQS_destUnitId" readonly/>
                                                    <span class="input-group-btn">
                                                    <button class="btn btn-sm btn-default" id="search_guest_button"
                                                            type="button" onclick="openSearchGuestDialog()">
                                                        <i class="ace-icon fa fa-list"></i>
                                                    </button>
											    </span>
                                                    <input class="form-control" id="search_destUnitName" type="text"
                                                           name="destUnitName" readonly/>
                                                </div>
                                            </div>

                                        <label class="col-xs-1 control-label" for="search_billType">单据类型</label>
                                        <div class="col-xs-2">
                                            <select class="form-control" id="search_billType" name="filter_EQS_billType"
                                                    style="width: 100%;">
                                                <option value="">--请选择--</option>
                                                <%--<option value="采购订单">采购订单</option>--%>
                                                <option value="销售订单">销售订单</option>
                                                <%--<option value="采购退货申请单">采购退货申请单</option>--%>
                                                <option value="销售退货申请单">销售退货申请单</option>
                                                <option value="收款">收款</option>
                                                <option value="储值">储值</option>
                                                <option value="付款">付款</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                       <%-- <label class="col-xs-1 control-label" for="search_guestTel">电话</label>
                                        <div class="col-xs-2">
                                            <input class="form-control" id="search_guestTel" name="guestTel"
                                                   type="text"/>
                                        </div>
                                        <label class="col-xs-1 control-label" for="search_guestOwingValue">当前欠款</label>
                                        <div class="col-xs-2">
                                            <input class="form-control" id="search_guestOwingValue" name="guestOwingValue"
                                                   style="width: 100%;" />
                                        </div>--%>
                                        <label class="col-xs-1 control-label text-right"
                                               for="search_createTime" style="display: none">查询期间 </label>
                                        <div class="col-xs-2" style="display: none">
                                            <div class="input-group">
                                                <input class="form-control date-picker startDate"
                                                       id="search_createTime"
                                                       type="text" name="filter_GED_billDate"
                                                       data-date-format="yyyy-mm-dd"/>

                                                <span class="input-group-addon">
                                                    <i class="fa fa-exchange"></i>
                                            </span>

                                                <input class="form-control date-picker endDate"
                                                       id="endDate"
                                                       type="text" name="filter_LED_billDate"
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
                                            <%--<button type="reset" class="btn btn-sm btn-warning">--%>
                                            <%--<i class="ace-icon fa fa-undo"></i>--%>
                                            <%--<span class="bigger-110">清空</span></button>--%>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>

                       <div id="isAdmin">
                            <div class="data-container col-lg-2" style="width: 25%">
                                <span class="title">总销：</span>
                                <span class="title" id ="saleNum">查询中...</span>
                            </div>
                            <div class="data-container col-lg-2" style="width: 25%">
                                <span class="title">总退：</span>
                                <span class="title" id ="saleRetrunNum">查询中...</span>
                            </div>
                            <div class="data-container col-lg-2" style="width: 25%">
                                <span class="title">扣会员：</span>
                                <span class="title" id="memberNum">查询中...</span>
                            </div>
                            <div class="data-container col-lg-2" style="width: 25%">
                                <span class="title">储值：</span>
                                <span class="title" id="storedvalueNum">查询中...</span>
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

    <jsp:include page="../layout/footer_js.jsp"></jsp:include>
    <!--/.fluid-container#main-container-->
</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="../base/search_guest_dialog.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/factory/dateFormatUtil.js"></script>
<script type="text/javascript" src="<%=basePath%>/views/logistics/dateTableAccoutViewController.js"></script>
</body>
</html>