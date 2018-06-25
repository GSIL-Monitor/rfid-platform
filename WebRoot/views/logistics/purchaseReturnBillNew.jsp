<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/6/25
  Time: 上午 10:36
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
        var pageType="${pageType}";
        var OwnerId="${OwnerId}";
        var userId="${userId}";
        var defaultWarehId="${defaultWarehId}";
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
                                                   for="search_destUnitId">供应商</label>
                                            <div class="col-md-10">
                                                <div class="input-group">
                                                    <input class="form-control" id="search_destUnitId"
                                                           type="text"
                                                           name="filter_EQS_destUnitId" readonly/>
                                                    <span class="input-group-btn">
                                                            <button class="btn btn-sm btn-default"
                                                                    id="search_guest_button"
                                                                    type="button"
                                                                    onclick="openSearchVendorDialog('search')">
                                                                <i class="ace-icon fa fa-list"></i>
                                                            </button>
											            </span>
                                                    <input class="form-control" id="search_destUnitName"
                                                           type="text"
                                                           name="search_destUnitName" readonly/>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-md-2 control-label"
                                                   for="search_origId">出库仓库</label>
                                            <div class="col-md-4">
                                                <select class="form-control selectpicker show-tick"
                                                        id="search_origId"
                                                        name="filter_LIKES_origId"
                                                        style="width: 100%;" data-live-search="true">
                                                </select>
                                            </div>
                                            <label class="col-md-2 control-label"
                                                   for="select_outStatus">出库状态</label>
                                            <div class="col-md-4">
                                                <select class="form-control selectpicker show-tick"
                                                        id="select_outStatus"
                                                        name="filter_INI_outStatus" data-live-search="true">
                                                    <option value="">--请选择--</option>
                                                    <option value="0,3">订单状态</option>
                                                    <option value="2">已出库</option>
                                                    <option value="3">出库中</option>
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
                                                       for="edit_destUnitId">供应商</label>
                                                <div class="col-md-5">
                                                    <div class="input-group">
                                                        <input class="form-control" id="edit_destUnitId"
                                                               type="text"
                                                               name="destUnitId"
                                                               value="${purchaseReturnBill.destUnitId}" readonly/>
                                                        <span class="input-group-btn">
                                                                    <button class="btn btn-sm btn-default"
                                                                            id="edit_guest_button"
                                                                            type="button"
                                                                            onclick="openSearchVendorDialog('edit')">
                                                                        <i class="ace-icon fa fa-list"></i>
                                                                    </button>
                                                                </span>
                                                        <input class="form-control" id="edit_destUnitName"
                                                               type="text"
                                                               name="destUnitName"
                                                               value="${purchaseReturnBill.destUnitName}" readonly/>
                                                    </div>
                                                </div>
                                                <label class="col-md-1 control-label"
                                                       for="edit_billType">退货类型</label>
                                                <div class="col-md-5">
                                                    <input class="form-control" id="edit_billType" name="billType"
                                                           type="text" readOnly
                                                           value="${purchaseReturnBill.billNo}"/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-1 control-label"
                                                       for="edit_billNo">单据编号</label>
                                                <div class="col-md-5">
                                                    <input class="form-control" id="edit_billNo" name="billNo"
                                                           type="text" readOnly
                                                           value="${purchaseReturnBill.billNo}"/>
                                                </div>
                                                <label class="col-md-1 control-label"
                                                       for="edit_billDate">单据日期</label>
                                                <div class="col-md-5">
                                                    <input class="form-control date-picker" id="edit_billDate"
                                                           name="billDate"
                                                           type="text" value="${purchaseReturnBill.billDate}"/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-1 control-label"
                                                       for="edit_origId">出库仓库</label>
                                                <div class="col-md-3">
                                                    <select class="form-control selectpicker show-tick"
                                                            id="edit_origId"
                                                            name="origId"
                                                            style="width: 100%;" data-live-search="true" value="${purchaseReturnBill.origId}">
                                                    </select>
                                                </div>
                                                <label class="col-md-1 control-label"
                                                       for="edit_actPrice">应退货金额</label>
                                                <div class="col-md-3">
                                                    <input class="form-control date-picker" id="edit_actPrice"
                                                           name="actPrice"
                                                           type="text" value="${purchaseReturnBill.actPrice}"/>
                                                </div>
                                                <label class="col-md-1 control-label"
                                                       for="edit_payPrice">实退货金额</label>
                                                <div class="col-md-3">
                                                    <input class="form-control" id="edit_payPrice" name="payPrice"
                                                           type="text" value="${purchaseReturnBill.payPrice}"/>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="col-md-1 control-label"
                                                       for="edit_remark">备注</label>

                                                <div class="col-md-11 col-sm-11">
                                                <textarea maxlength="400" class="form-control" id="edit_remark"
                                                          name="remark">${purchaseReturnBill.remark}</textarea>
                                                </div>
                                            </div>
                                            <input id="edit_status" name="status"
                                                   value="${purchaseReturnBill.status}"
                                                   type="hidden">
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
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="../sys/print_two.jsp"></jsp:include>
<jsp:include page="purchaseReturnBillPrint.jsp"></jsp:include>
<jsp:include page="../base/search_vendor_dialog.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/logistics/purchaseReturnBillNewController.js"></script>
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/dateFormatUtil.js"></script>
<script src="<%=basePath%>/Olive/plugin/print/LodopFuncs.js"></script>

</body>
</html>
