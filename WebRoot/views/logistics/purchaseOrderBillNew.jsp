<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2018/6/19
  Time: 9:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
        var defaultWarehId = "${defaultWarehId}";
    </script>
</head>
<body>
    <div class="main-container" id="main-container" style="">
        <script type="text/javascript">
            try {
                ace.settings.check('main-container','fixed')
            }catch(e) {
            }
        </script>
        <div class="main-content">
            <div class="main-content-inner">
                <!-- /.page-header -->
                <div class="row">
                    <!-- PAGE CONTENT BEGINS -->
                    <div class="col-md-12">
                        <div class="center">
                            <div class="col-md-4 order-panel-left">
                                <!-- 左则面板 -->
                                <div class="panel panel-default  left-panel">
                                    <div class="panel-body">
                                        <div class="widget-body">
                                            <form class="form-horizontal" role="form" id="searchForm">
                                                <div class="form-group">
                                                    <label class="col-md-2 control-label" for="search_billId">单号</label>
                                                    <div class="col-md-10">
                                                        <input class="form-control" id="search_billId" name="filter_LIKES_billNo"
                                                               type="text" onkeyup="this.value=this.value.toUpperCase()"
                                                               placeholder="模糊查询"/>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-md-2 control-label" for="search_returnBillNo">退货单号</label>
                                                    <div class="col-md-10">
                                                        <input class="form-control" id="search_returnBillNo" name="filter_LIKES_returnBillNo"
                                                               type="text" onkeyup="this.value=this.value.toUpperCase()"
                                                               placeholder="模糊查询"/>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-md-2 control-label" for="search_createTime">创建日期</label>
                                                    <div class="col-md-10">
                                                        <div class="input-group">
                                                            <input class="form-control date-picker" id="search_createTime"
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
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-md-2 control-label" for="search_origUnitId">供应商</label>
                                                    <div class="col-md-10">
                                                        <div class="input-group">
                                                            <input class="form-control" id="search_origUnitId" type="text"
                                                                   name="filter_EQS_origUnitId" readonly/>
                                                            <span class="input-group-btn">
                                                    <button class="btn btn-sm btn-default" id="search_vendor_button"
                                                            type="button" onclick="openSearchVendorDialog('search')">
                                                        <i class="ace-icon fa fa-list"></i>
                                                    </button>
											    </span>
                                                            <input class="form-control" id="search_origUnitName" type="text"
                                                                   name="search_origUnitName" readonly/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-md-2 control-label" for="search_destId">入库仓库</label>
                                                    <div class="col-md-4">
                                                        <select class="form-control selectpicker show-tick" id="search_destId" name="filter_LIKES_destId"
                                                                style="width: 100%;" data-live-search="true">
                                                        </select>
                                                    </div>
                                                    <label class="col-md-2 control-label" for="select_inStatus">入库状态</label>
                                                    <div class="col-md-4">
                                                        <select class="form-control selectpicker show-tick" id="select_inStatus"
                                                                name="filter_EQI_inStatus" data-live-search="true">
                                                            <option value="">--请选择--</option>
                                                            <option value="0">订单状态</option>
                                                            <option value="1">已入库</option>
                                                            <option value="4">入库中</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <div class="col-sm-offset-3 col-sm-6">
                                                        <button type="button" class="btn btn-sm btn-primary" onclick="_search()">
                                                            <i class="ace-icon fa fa-search"></i>
                                                            <span class="bigger-110">查询</span>
                                                        </button>
                                                        <button type="reset" class="btn btn-sm btn-warning" onclick="_resetForm()">
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
                                    <div class="panel=body">
                                        <div class="widget-body">
                                            <div class="widget-main padding-12">
                                                <form id="editForm" class="form-horizontal" role="form">
                                                    <div class="form-group">
                                                        <label class="col-md-1 control-label" for="search_origUnitId"><span
                                                                class="text-danger">* </span>供应商</label>
                                                        <div class="col-md-5">
                                                            <div class="input-group">
                                                                <input class="form-control" id="edit_origUnitId" type="text"
                                                                       name="origUnitId"  readonly/>
                                                                <span class="input-group-btn">
                                                                    <button class="btn btn-sm btn-default" id="edit_vendor_button"
                                                                            type="button" onclick="openSearchVendorDialog('edit')">
                                                                        <i class="ace-icon fa fa-list"></i>
                                                                    </button>
											                    </span>
                                                                <input class="form-control" id="edit_origUnitName" type="text"
                                                                       name="origUnitName"
                                                                       readonly/>
                                                            </div>
                                                        </div>
                                                        <label class="col-md-3 control-label" for="edit_billNo">单据编号</label>
                                                        <div class="col-md-3">
                                                            <input class="form-control" id="edit_billNo" name="billNo"
                                                                   type="text" readonly />
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-md-1 control-label" for="search_buyahandId">买手</label>
                                                        <div class="col-md-3">
                                                            <select class="form-control selectpicker show-tick" id="search_buyahandId"
                                                                    name="buyahandId"
                                                                    style="width: 100%;" data-live-search="true">
                                                            </select>
                                                        </div>
                                                        <label class="col-md-1 control-label" for="edit_destId"><span class="text-danger">* </span>入库仓库</label>
                                                        <div class="col-md-3">
                                                            <select class="form-control selectpicker show-tick" id="edit_destId" name="destId"
                                                                    style="width: 100%;"  data-live-search="true">
                                                            </select>
                                                        </div>
                                                        <label class="col-md-1 control-label" for="search_billDate"
                                                        ><span class="text-danger">* </span>单据日期</label>
                                                        <div class="col-md-3">
                                                            <input class="form-control date-picker" id="search_billDate" name="billDate"
                                                                   type="text" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-md-1 control-label" for="search_payPrice"><span
                                                                class="text-danger">* </span>实付金额</label>
                                                        <div class="col-md-3">
                                                            <div class="input-group">
                                                                <span class="input-group-addon"><i class="fa fa-jpy"></i></span>
                                                                <input class="form-control" id="search_payPrice" name="payPrice"
                                                                       type="number"/>
                                                            </div>
                                                        </div>
                                                        <label class="col-md-1 control-label" for="search_payType">支付方式</label>
                                                        <div class="col-md-3">
                                                            <input class="form-control" id="search_payType" name="payType"
                                                                   type="text"/>
                                                        </div>
                                                        <label class="col-md-1 control-label" for="search_orderWarehouseId"></span>订货仓库</label>
                                                        <div class="col-md-3">
                                                            <select class="form-control selectpicker show-tick" id="search_orderWarehouseId" name="orderWarehouseId"
                                                                    style="width: 100%;"  data-live-search="true">>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-md-1 control-label" for="search_discount">整单折扣</label>
                                                        <div class="col-md-3">
                                                            <input class="form-control" id="search_discount" name="discount"
                                                                   onblur="search_discount_onblur()">
                                                            </input>
                                                        </div>
                                                        <label class="col-md-1 control-label" for="search_srcBillNo">补货单编号</label>
                                                        <div class="col-md-3">
                                                            <input class="form-control" id="search_srcBillNo" name="srcBillNo"
                                                                   type="text" readonly/>
                                                        </div>
                                                        <label class="col-md-1 control-label" for="search_retBillNo">退货单编号</label>
                                                        <div class="col-md-3">
                                                            <input class="form-control" id="search_retBillNo" name="returnBillNo"
                                                                   type="text" readonly/>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="col-xs-1 control-label"
                                                               for="form_remark">备注</label>
                                                        <div class="col-md-11 col-sm-11">
                                                        <textarea maxlength="400" class="form-control" id="form_remark"
                                                            name="remark" >
                                                        </textarea>
                                                        </div>
                                                    </div>
                                                    <input id="search_status" name="status" type="hidden">
                                                    </input>
                                                    <input id="search_ownerId" name="ownerId"  type="hidden">
                                                    </input>
                                                    <input id="search_id" name="id" type="hidden">
                                                    </input>
                                                </form>
                                                <form id="ReturnEditForm" >
                                                    <input id="return_origId" name="origId" type="hidden"/>
                                                    <input id="return_destUnitId" type="hidden" name="destUnitId"/>
                                                    <input id="return_destUnitName" type="hidden" name="destUnitName"/>
                                                    <input id="return_actPrice" name="actPrice" type="hidden"/>
                                                    <input id="return_billDate" name="billDate" type="hidden"/>
                                                    <input id="return_payPrice" name="payPrice" type="hidden"/>
                                                    <input  name="remark" id="search_remark" type="hidden"/>
                                                </form>
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
    <jsp:include page="add_detail_dialog.jsp"></jsp:include>
    <jsp:include page="add_epc_dialog.jsp"></jsp:include>
    <jsp:include page="purchaseOrderBillPrint.jsp"></jsp:include>
    <jsp:include page="../sys/print_two.jsp"></jsp:include>
    <jsp:include page="uniqueCode_detail_list.jsp"></jsp:include>
    <jsp:include page="../base/waitingPage.jsp"></jsp:include>
    <jsp:include page="../base/search_vendor_dialog.jsp"></jsp:include>
    <script type="text/javascript" src="<%=basePath%>/views/logistics/purchaseOrderBillControllerNew.js"></script>
    <script type="text/javascript" src="<%=basePath%>/Olive/plugin/dateFormatUtil.js"></script>
    <script src="<%=basePath%>/Olive/plugin/print/LodopFuncs.js"></script>
    <div id="dialog"></div>
    <div id="progressDialog"></div>
    <span id="notification"></span>
</body>
</html>
