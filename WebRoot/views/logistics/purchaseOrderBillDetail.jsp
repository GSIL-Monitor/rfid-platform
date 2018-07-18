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
        var pageType = "${pageType}";
        var billNo = "${purchaseOrderBill.billNo}";
        var purchaseOrder_destId = "${purchaseOrderBill.destId}";
        var purchaseOrder_orderWarehouseId = "${purchaseOrderBill.orderWarehouseId}";
        var saleOrder_buyahandId = "${purchaseOrderBill.buyahandId}";
        var curOwnerId = "${ownerId}";
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
                            <form id="editForm" class="form-horizontal" role="form">
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="search_billNo">单据编号</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_billNo" name="billNo"
                                               type="text" readonly value="${purchaseOrderBill.billNo}"/>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_origUnitId"><span
                                            class="text-danger">* </span>供应商</label>
                                    <%--<div class="col-xs-2">--%>
                                    <%--<input class="form-control" id="search_vendor" name="origUnitId"--%>
                                    <%--style="width: 100%;"--%>
                                    <%--value="${purchaseOrderBill.origUnitId}"/>--%>
                                    <%--</div>--%>
                                    <div class="col-xs-2">
                                        <div class="input-group">
                                            <input class="form-control" id="search_origUnitId" type="text"
                                                   name="origUnitId" value="${purchaseOrderBill.origUnitId}" readonly/>
                                            <span class="input-group-btn">
                                                <button class="btn btn-sm btn-default" id="search_vendor_button"
                                                        type="button" onclick="openSearchVendorDialog()">
                                                    <i class="ace-icon fa fa-list"></i>
                                                </button>
											    </span>
                                            <input class="form-control" id="search_origUnitName" type="text"
                                                   name="origUnitName" value="${purchaseOrderBill.origUnitName}"
                                                   readonly/>
                                        </div>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_buyahandId">买手</label>
                                    <div class="col-xs-2">
                                        <select class="form-control selectpicker show-tick" id="search_buyahandId"
                                                name="buyahandId"
                                                style="width: 100%;" data-live-search="true">
                                        </select>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_destId"><span class="text-danger">* </span>入库仓库</label>
                                    <div class="col-xs-2">
                                        <%--<input class="form-control" id="search_destId" name="destId"--%>
                                        <%--style="width: 100%;"--%>
                                        <%--value="${purchaseOrderBill.destId}"/>--%>
                                        <select class="form-control selectpicker show-tick" id="search_destId" name="destId"
                                                style="width: 100%;" value="${purchaseOrderBill.destId}" data-live-search="true">
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="search_billDate"
                                    ><span class="text-danger">* </span>单据日期</label>
                                    <div class="col-xs-2">
                                        <input class="form-control date-picker" id="search_billDate" name="billDate"
                                               type="text" value="${purchaseOrderBill.billDate}"/>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_payPrice"><span
                                            class="text-danger">* </span>实付金额</label>
                                    <div class="col-xs-2 ">
                                        <div class="input-group">
                                            <span class="input-group-addon"><i class="fa fa-jpy"></i></span>
                                            <input class="form-control" id="search_payPrice" name="payPrice"
                                                   type="number" value="${purchaseOrderBill.payPrice}"/>
                                        </div>

                                    </div>
                                    <label class="col-xs-1 control-label" for="search_payType">支付方式</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_payType" name="payType"
                                               type="text" value="${purchaseOrderBill.payType}"/>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_orderWarehouseId"></span>订货仓库</label>
                                    <div class="col-xs-2">
                                        <select class="form-control selectpicker show-tick" id="search_orderWarehouseId" name="orderWarehouseId"
                                                style="width: 100%;" value="${purchaseOrderBill.orderWarehouseId}" data-live-search="true">>
                                        </select>
                                    </div>

                                </div>
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="search_discount">整单折扣</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_discount" name="discount"
                                               onblur="search_discount_onblur()">
                                        </input>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_srcBillNo">补货单编号</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_srcBillNo" name="srcBillNo"
                                               type="text" readonly value="${purchaseOrderBill.srcBillNo}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-1 control-label"
                                           for="form_remark">备注</label>

                                    <div class="col-xs-9 col-sm-9">
                                            <textarea maxlength="400" class="form-control" id="form_remark"
                                                      name="remark" >${purchaseOrderBill.remark}</textarea>
                                    </div>
                                </div>
                                <div>
                                    <input id="search_status" name="status" value="${purchaseOrderBill.status}" type="hidden">
                                    </input>
                                    <input id="search_ownerId" name="ownerId" value="${purchaseOrderBill.ownerId}" type="hidden">
                                    </input>
                                    <input id="search_id" name="id" value="${purchaseOrderBill.id}" type="hidden">
                                    </input>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="hr hr4"></div>
    <input  id="findOwnerId"
           type="text" value="${OwnerId}" style="display: none"/>
    <input  id="defaultWarehId"
            type="text" value="${defaultWarehId}" style="display: none"/>
    <div class="widget-box transparent">
        <div class="widget-header ">
            <h4 class="widget-title lighter">单据明细</h4>
            <div class="widget-toolbar no-border">
                <ul class="nav nav-tabs" id="myTab">
                    <li class="active">
                        <a data-toggle="tab" href="#detail">SKU明细</a>
                    </li>
                </ul>
            </div>
        </div>
        <div class="widget-body">
            <div class="widget-main padding-12 no-padding-left no-padding-right">
                <div class="tab-content padding-4">
                    <div id="addDetail" class="tab-pane in active" style="height:80%;">
                        <table id="addDetailgrid"></table>
                        <div id="addDetailgrid-pager"></div>
                    </div>
                    <div id="editDetail" class="tab-pane in active" style="height:80%;">
                        <table id="editDetailgrid"></table>
                        <div id="editDetailgrid-pager"></div>
                    </div>
                </div>
                <div id="buttonGroup" class="col-sm-offset-5 col-sm-10 ">
                </div>

            </div>
        </div>
    </div>

</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="add_detail_dialog.jsp"></jsp:include>
<jsp:include page="add_epc_dialog.jsp"></jsp:include>
<jsp:include page="purchaseOrderBillPrint.jsp"></jsp:include>
<jsp:include page="../sys/print_two.jsp"></jsp:include>
<jsp:include page="uniqueCode_detail_list.jsp"></jsp:include>
<jsp:include page="../base/waitingPage.jsp"></jsp:include>
<jsp:include page="../base/search_vendor_dialog.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/logistics/purchaseOrderBillDetailController.js"></script>
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/dateFormatUtil.js"></script>
<script src="<%=basePath%>/Olive/plugin/print/LodopFuncs.js"></script>
<script type="text/javascript">

</script>
</body>
</html>