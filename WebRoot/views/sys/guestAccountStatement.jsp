<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/7/12
  Time: 13:25
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
        <%--var guestId = "${guest.id}";--%>
        var userId = "${userId}";
        var masId = "${masId}";
        var defaultPayType = "${payType}";
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
        <div class="row">
            <div class="col-xs-12">
                <div class="widget-box widget-color-blue  light-border">
                    <div class="widget-header">
                        <h5 class="widget-title">基本信息</h5>
                        <div class="widget-toolbar no-border">

                            <a class="btn btn-xs bigger btn-yellow dropdown-toggle" href="<%=basePath%>/${mainUrl}">
                                <i class="ace-icon fa fa-arrow-left"></i> 返回
                            </a>
                        </div>
                    </div>
                    <div class="widget-body">
                        <div class="widget-main padding-12">
                            <form id="searchForm" class="form-horizontal" role="form">
                                <div class="form-group">
                                    <%--<label class="col-xs-1 control-label" for="search_guestId">客户编号</label>--%>
                                    <div class="col-xs-2" hidden>
                                        <input class="form-control" id="search_guestId" name="filter_EQS_unitId"
                                               type="text" readonly value="${guest.id}"/>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_guestName">客户名称</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_guestName" name="guestName"
                                               style="width: 100%;" readonly value="${guest.name}"/>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_guestUnitType">客户类型</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_guestUnitType" name="guestUnitType"
                                               style="width: 100%;" readonly value="${guest.unitTypeName}"/>
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
                                    <label class="col-xs-1 control-label" for="search_guestTel">电话</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_guestTel" name="guestTel"
                                               type="text" readonly value="${guest.tel}"/>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_guestOwingValue">当前欠款</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_guestOwingValue" name="guestOwingValue"
                                               style="width: 100%;" readonly value="${guest.owingValue}"/>
                                    </div>
                                    <label class="col-xs-1 control-label text-right"
                                           for="search_createTime">查询期间 </label>
                                    <div class="col-xs-2">
                                        <div class="input-group">
                                            <input class="form-control date-picker startDate"
                                                   id="search_createTime"
                                                   type="text" name="filter_GED_billDate"
                                                   data-date-format="yyyy-mm-dd"/>

                                            <span class="input-group-addon">
                                                    <i class="fa fa-exchange"></i>
                                            </span>

                                            <input class="form-control date-picker endDate"
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
                </div>
            </div>
        </div>
    </div>

    <div class="hr hr4"></div>


    <div class="widget-box transparent">
        <div class="widget-header ">
            <h4 class="widget-title lighter">明细</h4>
            <div class="widget-toolbar no-border">
                <ul class="nav nav-tabs" id="myTab">
                    <li class="active">
                        <a data-toggle="tab" href="#detail">对账明细</a>
                    </li>
                </ul>
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
                <div id="buttonGroup" class="col-sm-offset-5 col-sm-10 ">
                    <button type="button" class="btn btn-sm btn-primary" onclick="gathering()">
                        <i class="ace-icon fa fa-jpy"></i>
                        <span class="bigger-110">收付款</span>
                    </button>

                    <button type="button" class="btn btn-sm btn-primary"  style="margin-left: 20px" onclick="initValueModify()">
                        <i class="ace-icon fa fa-crop"></i>
                        <span class="bigger-110">期初调整</span>
                    </button>
                </div>

            </div>
        </div>
    </div>

</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="../base/waitingPage.jsp"></jsp:include>
<jsp:include page="gathering_dialog.jsp"></jsp:include>
<jsp:include page="initialAdjustment_dialog.jsp"></jsp:include>

<script type="text/javascript" src="<%=basePath%>/views/factory/dateFormatUtil.js"></script>
<script type="text/javascript" src="<%=basePath%>/views/sys/guestAccountStatementController.js"></script>

<script type="text/javascript">

</script>
</body>
</html>
