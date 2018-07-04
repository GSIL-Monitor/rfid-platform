<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/7/3
  Time: 上午 10:09
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
        var userId = "${userId}";
        var billNo = "${billNo}";
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
                                                       for="search_styleid">款号</label>
                                                <div class="col-md-4">
                                                    <div class="input-group">
                                                        <input class="form-control" id="search_styleid"
                                                               type="text" name="filter_EQS_styleid" readonly/>
                                                        <span class="input-group-btn">
                                                                 <button class="btn btn-sm btn-default" type="button" onclick="openstyleDialog('#search_styleid','#filter_eq_stylename')">
                                                                     <i class="ace-icon fa fa-list"></i>
                                                                 </button>
                                                              </span>
                                                        <input class="form-control" id="filter_eq_stylename"
                                                               type="text" name="" readonly  placeholder="款名"/>
                                                    </div>
                                                </div>
                                                <label class="col-md-2 control-label"
                                                       for="search_class1">厂家</label>
                                                <div class="col-md-4">
                                                    <div class="input-group">
                                                        <input class="form-control" id="search_class1"
                                                               type="text" name="filter_EQS_class1" readonly/>
                                                        <span class="input-group-btn">
                                                                 <button class="btn btn-sm btn-default" type="button" onclick="initClass1Select_Grid('#search_class1','#filter_eq_class1Name')">
                                                                     <i class="ace-icon fa fa-list"></i>
                                                                 </button>
                                                              </span>
                                                        <input class="form-control" id="filter_eq_class1Name"
                                                               type="text" name="" readonly  placeholder="厂家"/>
                                                    </div>
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
                                                           for="edit_billNo">单据编号</label>
                                                    <div class="col-md-3">
                                                        <input class="form-control" id="edit_billNo" name="billNo"
                                                               type="text" readOnly
                                                               value="${replenishBill.billNo}"/>
                                                    </div>
                                                    <label class="col-md-1 control-label"
                                                           for="edit_billDate">单据日期</label>
                                                    <div class="col-md-3">
                                                        <input class="form-control date-picker" id="edit_billDate"
                                                               name="billDate"
                                                               type="text" value="${replenishBill.billDate}"/>
                                                    </div>
                                                    <label class="col-md-1 control-label"
                                                           for="edit_replenishType">类型</label>
                                                    <div class="col-md-3">
                                                        <div class="radio" id="edit_replenishType">
                                                            <label><input name="replenishType" type="radio" value="1"/>购货</label>
                                                            <label><input name="replenishType" type="radio" value="0"/>退货</label>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-md-1 control-label"
                                                           for="edit_buyahandId">买手</label>
                                                    <div class="col-md-3">
                                                        <select class="form-control selectpicker show-tick" id="edit_buyahandId"
                                                                name="buyahandId"
                                                                style="width: 100%;" data-live-search="true" onchange="changebuy()">
                                                        </select>
                                                    </div>
                                                    <label class="col-md-1 control-label"
                                                           for="edit_busnissId">销售员</label>
                                                    <div class="col-md-3">
                                                        <select class="form-control selectpicker show-tick" id="edit_busnissId"
                                                                name="busnissId"
                                                                style="width: 100%;" data-live-search="true">
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-md-1 control-label"
                                                           for="edit_remark">备注</label>

                                                    <div class="col-md-11 col-sm-11">
                                            <textarea maxlength="400" class="form-control" id="edit_remark"
                                                      name="remark">${replenishBill.remark}</textarea>
                                                    </div>
                                                </div>
                                                <input id="edit_srcBillNo" name="srcBillNo" value="${replenishBill.srcBillNo}"
                                                       type="hidden">
                                                </input>
                                                <input id="edit_status" name="status" value="${replenishBill.status}"
                                                       type="hidden">
                                                </input>
                                                <input id="edit_ownerId" name="ownerId" value="${replenishBill.ownerId}"
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
</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="add_detail_dialog.jsp"></jsp:include>
<jsp:include page="relenishBillDetail_edit.jsp"></jsp:include>
<jsp:include page="findPurchaseOrder.jsp"></jsp:include>
<jsp:include page="../base/waitingPage.jsp"></jsp:include>
<jsp:include page="saleOrderBillPrint.jsp"></jsp:include>
<jsp:include page="../sys/print_two.jsp"></jsp:include>
<jsp:include page="../base/style_dialog.jsp"></jsp:include>
<jsp:include page="search_saleOrder_code_dialog.jsp"></jsp:include>
<jsp:include page="../base/search_class1_dialog.jsp"></jsp:include>
<jsp:include page="../base/search_guest_dialog.jsp"></jsp:include>



<script type="text/javascript" src="<%=basePath%>/views/logistics/relenishBillNewController.js"></script>
<div id="dialog"></div>
<div id="progressDialog"></div>
<span id="notification"></span>
</body>
</html>
