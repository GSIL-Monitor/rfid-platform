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
        var billNo = "${saleOrderBill.billNo}";
        var saleOrder_customerTypeId = "${saleOrderBill.customerTypeId}";
        var saleOrder_origId = "${saleOrderBill.origId}";
        var saleOrder_destId = "${saleOrderBill.destId}";
        var curOwnerId = "${ownerId}";
        var ownersId="${ownersId}";
        var userId = "${userId}";
        var defaultWarehId = "${defaultWarehId}";
        var defaultSaleStaffId="${defaultSaleStaffId}";
        var defalutCustomerId="${defalutCustomerId}";
        var defalutCustomerName="${defalutCustomerName}";
        var defalutCustomerdiscount="${defalutCustomerdiscount}";
        var defalutCustomercustomerType="${defalutCustomercustomerType}";
        var defalutCustomerowingValue="${defalutCustomerowingValue}";

        var saleOrder_busnissId = "${saleOrderBill.busnissId}";
        var slaeOrder_status = "${saleOrderBill.status}";
        var roleid = "${roleid}";
        var Codes = "${Codes}";
        var groupid="${groupid}";

    </script>
</head>
<body class="no-skin">


<div class="main-container">
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
                            <form id="editForm" class="form-horizontal" role="form"
                                  onkeydown="if(event.keyCode==13)return false;">
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="search_billNo">单据编号</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_billNo" name="billNo"
                                               type="text" readOnly value="${saleOrderBill.billNo}"/>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_billDate">单据日期</label>
                                    <div class="col-xs-2">
                                        <input class="form-control date-picker" id="search_billDate" name="billDate"
                                               type="text" value="${saleOrderBill.billDate}"/>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_payType">支付方式</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_payType" name="payType"
                                               type="text" value="${saleOrderBill.payType}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" hidden for="search_customerType">客户类型</label>
                                    <div class="col-xs-2" hidden>
                                        <select class="form-control" hidden id="search_customerType" name="customerTypeId"
                                                style="width: 100%;" value="${saleOrderBill.customerTypeId}">
                                        </select>
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_destUnitId">客户</label>
                                    <div class="col-xs-2">
                                        <div class="input-group">
                                            <input class="form-control" id="search_destUnitId" type="text"
                                                   name="destUnitId" value="${saleOrderBill.destUnitId}" readonly/>
                                            <span class="input-group-btn">
												<button class="btn btn-sm btn-default" id="search_guest_button"
                                                        type="button"
                                                        onclick="openSearchGuestDialog()">
													<i class="ace-icon fa fa-list"></i>
												</button>
											</span>
                                            <input class="form-control" id="search_destUnitName" type="text"
                                                   name="destUnitName" value="${saleOrderBill.destUnitName}" readonly/>
                                        </div>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_actPrice">应收金额</label>
                                    <div class="col-xs-2">
                                        <div class="input-group">
                                            <span class="input-group-addon"><i class="fa fa-jpy"></i></span>
                                            <input class="form-control" id="search_actPrice" name="actPrice"
                                                   type="number" step="0.01" readonly
                                                   value="${saleOrderBill.actPrice}"/>
                                        </div>
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_payPrice">实收金额</label>
                                    <div class="col-xs-2">
                                        <div class="input-group">
                                            <span class="input-group-addon"><i class="fa fa-jpy"></i></span>
                                            <input class="form-control" id="search_payPrice" name="payPrice"
                                                   type="number" step="0.01"
                                                   value="${saleOrderBill.payPrice}"/>
                                        </div>
                                    </div>

                                </div>
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="pre_Balance">售前余额</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="pre_Balance" name="preBalance"
                                               value="${saleOrderBill.preBalance}" readonly>
                                        </input>
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_destId">入库仓库</label>
                                    <div class="col-xs-2">
                                        <select class="form-control selectpicker show-tick" id="search_destId" name="destId"
                                                style="width: 100%;" value="${saleOrderBill.destId}" data-live-search="true">
                                        </select>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_origId">出库仓库</label>
                                    <div class="col-xs-2">
                                        <select class="form-control selectpicker show-tick" id="search_origId" name="origId"
                                                style="width: 100%;" value="${saleOrderBill.origId}" data-live-search="true">
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="after_Balance">售后余额</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="after_Balance" name="afterBalance"
                                               value="${saleOrderBill.afterBalance}" readonly>
                                        </input>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_discount">整单折扣</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_discount" name="discount"
                                               value="${saleOrderBill.discount}" onblur="search_discount_onblur()">
                                        </input>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_busnissId">销售员</label>
                                    <div class="col-xs-2">
                                        <select class="form-control selectpicker show-tick" id="search_busnissId"
                                                name="busnissId"
                                                style="width: 100%;" data-live-search="true">
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-1 control-label"
                                           for="form_remark">备注</label>

                                    <div class="col-xs-9 col-sm-9">
                                            <textarea maxlength="400" class="form-control" id="form_remark"
                                                      name="remark">${saleOrderBill.remark}</textarea>
                                    </div>
                                </div>
                                <div>
                                    <input id="search_status" name="status" value="${saleOrderBill.status}"
                                           type="hidden">
                                    </input>
                                    <input id="search_ownerId" name="ownerId" value="${saleOrderBill.ownerId}"
                                           type="hidden">
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
                </div>
                <div id="buttonGroup" style="margin-left: 10%" class="col-sm-offset-4 col-sm-10 ">
                </div>
               <%-- <div id="buttonGroupfindWxshop" style="margin-top: 20px" class="col-sm-offset-4 col-sm-10 ">
                </div>--%>

            </div>
        </div>
    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>
</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="add_detail_dialog.jsp"></jsp:include>
<jsp:include page="saleOrderBillPrint.jsp"></jsp:include>
<jsp:include page="../sys/print_two.jsp"></jsp:include>
<jsp:include page="../sys/print_A4.jsp"></jsp:include>
<jsp:include page="../sys/print_A4_1.jsp"></jsp:include>
<jsp:include page="add_uniqCode_dialog.jsp"></jsp:include>
<jsp:include page="uniqueCode_detail_list.jsp"></jsp:include>
<jsp:include page="exchageGood_dialog.jsp"></jsp:include>
<jsp:include page="findRetrunNo.jsp"></jsp:include>
<jsp:include page="findWxShop.jsp"></jsp:include>
<jsp:include page="sendStreamNO.jsp"></jsp:include>
<jsp:include page="../base/waitingPage.jsp"></jsp:include>
<jsp:include page="../base/search_guest_dialog.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/logistics/saleOrderBillDetailController.js"></script>
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/dateFormatUtil.js"></script>
<script src="<%=basePath%>/Olive/plugin/print/LodopFuncs.js"></script>
<script type="text/javascript">

</script>
</body>
</html>