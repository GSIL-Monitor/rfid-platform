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
        var ownersId = "${ownersId}";
        var userId = "${userId}";
        var billNo = "${billNo}";
        var saleOrder_customerTypeId = "${saleOrderBill.customerTypeId}";
        var saleOrder_origId = "${saleOrderBill.origId}";
        var saleOrder_destId = "${saleOrderBill.destId}";
        var defaultWarehId = "${defaultWarehId}";
        var defaultSaleStaffId = "${defaultSaleStaffId}";
        var defalutCustomerId = "${defalutCustomerId}";
        var defalutCustomerName = "${defalutCustomerName}";
        var defalutCustomerdiscount = "${defalutCustomerdiscount}";
        var defalutCustomercustomerType = "${defalutCustomercustomerType}";
        var defalutCustomerowingValue = "${defalutCustomerowingValue}";

        var saleOrder_busnissId = "${saleOrderBill.busnissId}";
        var slaeOrder_status = "${saleOrderBill.status}";
        var roleid = "${roleid}";
        var Codes = "${Codes}";
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
            <!-- /.page-header -->
            <div class="row">
                <div class="col-md-12">
                    <!-- PAGE CONTENT BEGINS -->
                    <div class="center">
                        <div class="col-md-4 order-panel-left">
                            <!-- 左则面板 -->
                            <div class="panel panel-default  left-panel">
                                <div class="panel-body">
                                    <div class="widget-body">
                                        <%-- <div class="widget-toolbox padding-8 clearfix">
                                             <div class="btn-toolbar" role="toolbar">
                                                 <div class="btn-group btn-group-sm pull-left">
                                                     <button class="btn btn-info" onclick="refresh()">
                                                         <i class="ace-icon fa fa-refresh"></i>
                                                         <span class="bigger-110">刷新</span>
                                                     </button>
                                                 </div>
                                                 <div class="btn-group btn-group-sm pull-left">
                                                     <button type="button" class="btn btn-primary" onclick="add()">
                                                         <i class="ace-icon fa fa-plus"></i>
                                                         <span class="bigger-110">新增</span>
                                                     </button>
                                                 </div>
                                                 <div class="btn-group btn-group-sm pull-left">
                                                     <button type="button" class="btn btn-primary" onclick="addUniqCode()">
                                                         <i class="ace-icon fa fa-plus"></i>
                                                         <span class="bigger-110">扫描</span>
                                                     </button>
                                                 </div>
                                                 <div class="btn-group btn-group-sm pull-right">
                                                     <button type="button" class="btn btn-info" onclick="showAdvSearchPanel();">
                                                         <i class="ace-icon fa fa-binoculars"></i>
                                                         <span class="bigger-110">高级查询</span>
                                                     </button>

                                                 </div>
                                             </div>
                                         </div>--%>

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
                                                       for="search_destUnitId">客户</label>
                                                <div class="col-md-10">
                                                    <div class="input-group">
                                                        <input class="form-control" id="search_destUnitId"
                                                               type="text"
                                                               name="filter_EQS_destUnitId" readonly/>
                                                        <span class="input-group-btn">
                                                            <button class="btn btn-sm btn-default"
                                                                    id="search_guest_button"
                                                                    type="button"
                                                                    onclick="openSearchGuestDialog('search')">
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
                                                <label class="col-md-2 control-label"
                                                       for="search_busnissId">销售员</label>
                                                <div class="col-md-4">
                                                    <select class="form-control selectpicker show-tick"
                                                            id="search_busnissId"
                                                            name="filter_EQS_busnissId"
                                                            style="width: 100%;" data-live-search="true">
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
                                                            onclick="_reset()">
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
                            <!-- 左则面板 end -->
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
                                                           for="edit_destUnitId">客户</label>
                                                    <div class="col-md-5">
                                                        <div class="input-group">
                                                            <input class="form-control" id="edit_destUnitId"
                                                                   type="text"
                                                                   name="destUnitId"
                                                                   value="${saleOrderBill.destUnitId}" readonly/>
                                                            <span class="input-group-btn">
												                <button class="btn btn-sm btn-default"
                                                                        id="edit_guest_button"
                                                                        type="button"
                                                                        onclick="openSearchGuestDialog('edit')">
                                                                    <i class="ace-icon fa fa-list"></i>
                                                                </button>
											                </span>
                                                            <input class="form-control" id="edit_destUnitName"
                                                                   type="text"
                                                                   name="destUnitName"
                                                                   value="${saleOrderBill.destUnitName}" readonly/>
                                                        </div>
                                                    </div>
                                                    <label class="col-md-3 control-label"
                                                           for="edit_customerType">客户类型</label>
                                                    <div class="col-md-3">
                                                        <select class="form-control selectpicker"
                                                                id="edit_customerType"
                                                                name="customerTypeId"
                                                                style="width: 100%;"
                                                                value="${saleOrderBill.customerTypeId}" disabled>
                                                        </select>
                                                    </div>


                                                </div>
                                                <div class="form-group">
                                                    <label class="col-md-1 control-label"
                                                           for="edit_billNo">单据编号</label>
                                                    <div class="col-md-3">
                                                        <input class="form-control" id="edit_billNo" name="billNo"
                                                               type="text" readOnly
                                                               value="${saleOrderBill.billNo}"/>
                                                    </div>
                                                    <label class="col-md-1 control-label"
                                                           for="edit_billDate">单据日期</label>
                                                    <div class="col-md-3">
                                                        <input class="form-control date-picker" id="edit_billDate"
                                                               name="billDate"
                                                               type="text" value="${saleOrderBill.billDate}"/>
                                                    </div>
                                                    <label class="col-md-1 control-label"
                                                           for="edit_payType">支付方式</label>
                                                    <div class="col-md-3">
                                                        <input class="form-control" id="edit_payType" name="payType"
                                                               type="text" value="${saleOrderBill.payType}"/>
                                                    </div>
                                                </div>

                                                <div class="form-group">
                                                    <label class="col-md-1 control-label"
                                                           for="edit_destId">入库仓库</label>
                                                    <div class="col-md-3">
                                                        <select class="form-control selectpicker show-tick"
                                                                id="edit_destId" name="destId"
                                                                style="width: 100%;" value="${saleOrderBill.destId}"
                                                                data-live-search="true">
                                                        </select>
                                                    </div>
                                                    <label class="col-md-1 control-label"
                                                           for="search_origId">出库仓库</label>
                                                    <div class="col-md-3">
                                                        <select class="form-control selectpicker show-tick"
                                                                id="edit_origId" name="origId"
                                                                style="width: 100%;" value="${saleOrderBill.origId}"
                                                                data-live-search="true">
                                                        </select>
                                                    </div>
                                                    <label class="col-md-1 control-label"
                                                           for="edit_busnissId">销售员</label>
                                                    <div class="col-md-3">
                                                        <select class="form-control selectpicker show-tick"
                                                                id="edit_busnissId"
                                                                name="busnissId"
                                                                style="width: 100%;" data-live-search="true">
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-md-1 control-label"
                                                           for="edit_pre_Balance">售前余额</label>
                                                    <div class="col-md-3">
                                                        <input class="form-control" id="edit_pre_Balance"
                                                               name="preBalance"
                                                               value="${saleOrderBill.preBalance}" readonly>
                                                        </input>
                                                    </div>

                                                    <label class="col-md-1 control-label"
                                                           for="edit_after_Balance">售后余额</label>
                                                    <div class="col-md-3">
                                                        <input class="form-control" id="edit_after_Balance"
                                                               name="afterBalance"
                                                               value="${saleOrderBill.afterBalance}" readonly>
                                                        </input>
                                                    </div>
                                                    <label class="col-md-1 control-label"
                                                           for="edit_discount">整单折扣</label>
                                                    <div class="col-md-3">
                                                        <input class="form-control" id="edit_discount"
                                                               name="discount"
                                                               value="${saleOrderBill.discount}"
                                                               onblur="search_discount_onblur()">
                                                        </input>
                                                    </div>
                                                </div>
                                                <div class="form-group">

                                                    <label class="col-md-1 control-label"
                                                           for="edit_actPrice">应收金额</label>
                                                    <div class="col-md-3">
                                                        <div class="input-group">
                                                                <span class="input-group-addon"><i
                                                                        class="fa fa-jpy"></i></span>
                                                            <input class="form-control" id="edit_actPrice"
                                                                   name="actPrice"
                                                                   type="number" step="0.01" readonly
                                                                   value="${saleOrderBill.actPrice}"/>
                                                        </div>
                                                    </div>

                                                    <label class="col-md-1 control-label"
                                                           for="edit_payPrice">实收金额</label>
                                                    <div class="col-md-3">
                                                        <div class="input-group">
                                                                <span class="input-group-addon"><i
                                                                        class="fa fa-jpy"></i></span>
                                                            <input class="form-control" id="edit_payPrice"
                                                                   name="payPrice"
                                                                   type="number" step="0.01"
                                                                   value="${saleOrderBill.payPrice}"/>
                                                        </div>
                                                    </div>

                                                </div>
                                                <div class="form-group">
                                                    <label class="col-md-1 control-label"
                                                           for="edit_remark">备注</label>

                                                    <div class="col-md-11 col-sm-11">
                                            <textarea maxlength="400" class="form-control" id="edit_remark"
                                                      name="remark">${saleOrderBill.remark}</textarea>
                                                    </div>
                                                </div>
                                                <div>
                                                    <input id="edit_status" name="status"
                                                           value="${saleOrderBill.status}"
                                                           type="hidden">
                                                    </input>
                                                    <input id="_ownerId" name="ownerId"
                                                           value="${saleOrderBill.ownerId}"
                                                           type="hidden">
                                                    </input>
                                                </div>
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
                <!-- /.col -->

            </div>
            <!-- /.row -->
            <!--/#page-content-->


        </div>

    </div>

</div>
<!--/.fluid-container#main-container-->
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="add_detail_dialog.jsp"></jsp:include>
<jsp:include page="saleOrderBillPrint.jsp"></jsp:include>
<jsp:include page="../sys/print_two.jsp"></jsp:include>
<jsp:include page="add_saleOrder_code_dialog.jsp"></jsp:include>
<jsp:include page="uniqueCode_detail_list.jsp"></jsp:include>
<jsp:include page="add_uniqCode_dialog.jsp"></jsp:include>
<jsp:include page="exchageGood_dialog.jsp"></jsp:include>
<jsp:include page="findRetrunNo.jsp"></jsp:include>
<jsp:include page="findWxShop.jsp"></jsp:include>
<jsp:include page="sendStreamNO.jsp"></jsp:include>
<jsp:include page="../base/search_guest_dialog.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/logistics/saleOrderBillNewControllor.js"></script>
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/dateFormatUtil.js"></script>

<div id="dialog"></div>
<div id="progressDialog"></div>
<span id="notification"></span>
</body>
</html>