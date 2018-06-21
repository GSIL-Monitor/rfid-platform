<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/6/20
  Time: 上午 9:38
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
        var ownersId="${ownersId}";
        var billNo = "${billNo}";
        var userId = "${userId}";
        var pageType="${pageType}";
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
            <div class="row">
                <div class="col-md-12">
                    <div class="center">
                        <div class="col-md-4 order-panel-left">
                            <div class="panel panel-default  left-panel">
                                <div class="panel-body">
                                    <div class="widget-body">
                                        <form class="form-horizontal" role="form" id="searchForm">
                                            <div class="form-group">
                                                <label class="col-md-2 control-label" for="search_billId">单号</label>
                                                <div class="col-md-10">
                                                    <input class="form-control" id="search_billId"
                                                           name="filter_LIKES_billNo"
                                                           type="text" onkeyup="this.value=this.value.toUpperCase()"
                                                           placeholder="模糊查询"/>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="col-md-2 control-label"
                                                       for="search_createTime">创建日期</label>
                                                <div class="col-md-10">
                                                    <div class="input-group">
                                                        <input class="form-control date-picker"
                                                               id="search_createTime"
                                                               type="text" name="filter_GED_billDate"
                                                               data-date-format="yyyy-mm-dd"/>
                                                        <span class="input-group-addon">
                                                    <i class="fa fa-exchange"></i>
                                                </span>
                                                        <input class="form-control date-picker" type="text"
                                                               class="input-sm form-control"
                                                               name="filter_LED_billDate"
                                                               data-date-format="yyyy-mm-dd"/>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-2 control-label"
                                                       for="search_origUnitId">客户</label>
                                                <div class="col-md-10">
                                                    <div class="input-group">
                                                        <input class="form-control" id="search_origUnitId"
                                                               type="text"
                                                               name="filter_EQS_origUnitId" readonly/>
                                                        <span class="input-group-btn">
                                                            <button class="btn btn-sm btn-default"
                                                                    id="search_guest_button"
                                                                    type="button"
                                                                    onclick="openSearchGuestDialog('search')">
                                                                <i class="ace-icon fa fa-list"></i>
                                                            </button>
											            </span>
                                                        <input class="form-control" id="search_origUnitName"
                                                               type="text"
                                                               name="search_origUnitName" readonly/>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-2 control-label"
                                                       for="search_destId">入库仓库</label>
                                                <div class="col-md-4">
                                                    <select class="form-control selectpicker show-tick"
                                                            id="search_destId"
                                                            name="filter_LIKES_destId"
                                                            style="width: 100%;" data-live-search="true">
                                                    </select>
                                                </div>
                                                <label class="col-md-2 control-label"
                                                       for="select_inStatus">入库状态</label>
                                                <div class="col-md-4">
                                                    <select class="form-control selectpicker show-tick"
                                                            id="select_inStatus"
                                                            name="filter_INI_inStatus" data-live-search="true">
                                                        <option value="">--请选择--</option>
                                                        <option value="0">订单状态</option>
                                                        <option value="1">已入库</option>
                                                        <option value="4">入库中</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <div class="col-md-offset-3 col-md-6">
                                                    <button type="button" class="btn btn-sm btn-primary"
                                                            onclick="_search()">
                                                        <i class="ace-icon fa fa-search"></i>
                                                        <span class="bigger-110">查询</span>
                                                    </button>
                                                    <button type="reset" class="btn btn-sm btn-warning" ,
                                                            onclick="_resetForm()">
                                                        <i class="ace-icon fa fa-undo"></i>
                                                        <span class="bigger-110">清空</span></button>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="col-md-12">
                                        <table id="grid"></table>
                                        <div id="grid-pager"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-8 order-panel-right">
                            <div class="panel panel-default">
                                <div class="panel-heading" style="text-align: right">
                                    <div id="buttonGroup">
                                    </div>
                                </div>
                                <div class="panel-body">
                                    <div class="widget-body">
                                        <div class="widget-main padding-12">
                                            <form id="editForm" class="form-horizontal" role="form"
                                                  onkeydown="if(event.keyCode==13)return false;">
                                                <div class="form-group">
                                                    <label class="col-md-1 control-label"
                                                           for="edit_origUnitId">客户</label>
                                                    <div class="col-md-5">
                                                        <div class="input-group">
                                                            <input class="form-control" id="edit_origUnitId"
                                                                   type="text"
                                                                   name="destUnitId"
                                                                   value="${consignmentBill.origUnitId}" readonly/>
                                                            <span class="input-group-btn">
												                <button class="btn btn-sm btn-default"
                                                                        id="edit_guest_button"
                                                                        type="button"
                                                                        onclick="openSearchGuestDialog('edit')">
                                                                    <i class="ace-icon fa fa-list"></i>
                                                                </button>
											                </span>
                                                            <input class="form-control" id="edit_origUnitName"
                                                                   type="text"
                                                                   name="origUnitName"
                                                                   value="${consignmentBill.origUnitName}" readonly/>
                                                        </div>
                                                    </div>
                                                    <label class="col-md-3 control-label"
                                                           for="edit_customerType">客户类型</label>
                                                    <div class="col-md-3">
                                                        <select class="form-control selectpicker"
                                                                id="edit_customerType"
                                                                name="customerType"
                                                                style="width: 100%;"
                                                                value="${consignmentBill.customerType}" disabled>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-md-1 control-label"
                                                           for="edit_billNo">单据编号</label>
                                                    <div class="col-md-3">
                                                        <input class="form-control" id="edit_billNo" name="billNo"
                                                               type="text" readOnly
                                                               value="${consignmentBill.billNo}"/>
                                                    </div>
                                                    <label class="col-md-1 control-label"
                                                           for="edit_billDate">单据日期</label>
                                                    <div class="col-md-3">
                                                        <input class="form-control date-picker" id="edit_billDate"
                                                               name="billDate"
                                                               type="text" value="${consignmentBill.billDate}"/>
                                                    </div>
                                                    <label class="col-md-1 control-label"
                                                           for="edit_payType">支付方式</label>
                                                    <div class="col-md-3">
                                                        <input class="form-control" id="edit_payType" name="payType"
                                                               type="text" value="${consignmentBill.payType}"/>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-md-1 control-label"
                                                           for="edit_destId">入库仓库</label>
                                                    <div class="col-md-3">
                                                        <select class="form-control selectpicker show-tick"
                                                                id="edit_destId" name="destId"
                                                                style="width: 100%;" value="${consignmentBill.destId}"
                                                                data-live-search="true">
                                                        </select>
                                                    </div>
                                                    <label class="col-md-1 control-label"
                                                           for="edit_actPrice">应付金额</label>
                                                    <div class="col-md-3">
                                                        <div class="input-group">
                                                            <span class="input-group-addon"><i class="fa fa-jpy"></i></span>
                                                            <input class="form-control" id="edit_actPrice" name="actPrice"
                                                                   type="number" step="0.01" readonly
                                                                   value='${consignmentBill.actPrice}'/>
                                                        </div>
                                                    </div>
                                                    <label class="col-md-1 control-label"
                                                           for="edit_payPrice">应付金额</label>
                                                    <div class="col-md-3">
                                                        <div class="input-group">
                                                            <span class="input-group-addon"><i class="fa fa-jpy"></i></span>
                                                            <input class="form-control" id="edit_payPrice" name="payPrice"
                                                                   type="number" step="0.01"
                                                                   value='${consignmentBill.payPrice}'/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-md-1 control-label"
                                                           for="edit_discount">整单折扣</label>
                                                    <div class="col-md-3">
                                                        <input class="form-control" id="edit_discount" name="discount"
                                                                   value="${consignmentBill.discount}" onblur="search_discount_onblur()">
                                                        </input>

                                                    </div>
                                                    <label class="col-md-1 control-label"
                                                           for="edit_busnissId">销售员</label>
                                                    <div class="col-md-3">
                                                        <select class="form-control selectpicker show-tick" id="edit_busnissId" name="busnissId"
                                                                style="width: 100%;" data-live-search="true">
                                                        </select>

                                                    </div>
                                                </div>
                                                <input class="form-control" id="edit_status" name="status"
                                                       value="${consignmentBill.status}"style="display: none">
                                                </input>
                                            </form>
                                        </div>
                                    </div>
                                    <div class="widget-body">
                                        <div class="widget-main padding-12 no-padding-left no-padding-right">
                                            <div class="tab-content padding-4">
                                                <div id="addDetail" class="tab-pane in active">
                                                    <table id="addDetailgrid"></table>
                                                    <div id="addDetailgrid-pager"></div>
                                                </div>
                                            </div>


                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="../base/search_guest_dialog.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/logistics/consignmentBillNewController.js"></script>
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/dateFormatUtil.js"></script>
<div id="dialog"></div>
<div id="progressDialog"></div>
<span id="notification"></span>
</body>
</html>
