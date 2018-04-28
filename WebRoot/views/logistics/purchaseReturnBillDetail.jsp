<%@ page pageEncoding="UTF-8" import="java.util.*" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script>
        var basePath = '<%=basePath%>';
        var billNo = "${purchaseReturnBill.billNo}";
        var pageType = '${pageType}';
        var returnStatus = "${purchaseReturnBill.status}";
        var purchaseReturnOrder_origId = "${purchaseReturnBill.origId}";
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
                                    <label class="col-xs-1 control-label" for="search_billNo">单号</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" value="${purchaseReturnBill.billNo}" name="billNo"
                                               id="search_billNo" readonly/>

                                    </div>
                                    <label class="col-xs-1 control-label" for="search_origId">出库仓库</label>
                                    <div class="col-xs-2">
                                        <select class="form-control" id="search_origId" name="origId"
                                                style="width: 100%;" value="${purchaseReturnBill.origId}">
                                        </select>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_destUnitId">供应商</label>
                                    <div class="col-xs-2">
                                        <div class="input-group">
                                            <input class="form-control" id="search_destUnitId" type="text"
                                                   name="destUnitId" value="${purchaseReturnBill.destUnitId}" readonly/>
                                            <span class="input-group-btn">
                                                <button class="btn btn-sm btn-default" id="search_vendor_button"
                                                        type="button" onclick="openSearchVendorDialog()">
                                                    <i class="ace-icon fa fa-list"></i>
                                                </button>
											    </span>
                                            <input class="form-control" id="search_destUnitName" type="text"
                                                   name="origUnitName" value="${purchaseReturnBill.destUnitName}"
                                                   readonly/>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">

                                    <label class="col-xs-1 control-label" for="search_actPrice">应退货金额</label>
                                    <div class="col-xs-2">
                                        <div class="input-group">
                                            <span class="input-group-addon"><i class="fa fa-jpy"></i></span>
                                            <input class="form-control" id="search_actPrice" name="actPrice"
                                                   value="${purchaseReturnBill.actPrice}" readonly/>
                                        </div>
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_billType">退货类型</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_billType" name="billType"
                                               value="${purchaseReturnBill.billType}"/>
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_billDate">单据日期</label>
                                    <div class="col-xs-2">
                                        <input class="form-control date-picker" name="billDate"
                                               value='<fmt:formatDate value="${purchaseReturnBill.billDate}" pattern="yyyy-MM-dd" />'
                                               id="search_billDate"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="search_payPrice" >实退货金额</label>
                                    <div class="col-xs-2">
                                        <div class="input-group">
                                            <span class="input-group-addon"><i class="fa fa-jpy"></i></span>
                                            <input class="form-control" id="search_payPrice" name="payPrice"
                                                   value="${purchaseReturnBill.payPrice}"/>
                                        </div>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-xs-4 col-sm-4 col-md-1 com-lg-1 control-label text-right"
                                           for="search_remark">备注</label>
                                    <div class="col-xs-8 col-sm-8 col-md-11 col-lg-11">
                                        <textarea class="form-control limited" name="remark"
                                                  id="search_remark">${purchaseReturnBill.remark}</textarea>
                                    </div>
                                </div>
                                <div>
                                    <input id="search_status" name="status" value="${purchaseReturnBill.status}" type="hidden">
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
                </div>
                <div id="buttonGroup" class="col-sm-offset-5 col-sm-10 ">
                </div>

            </div>
        </div>
    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>
</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="add_detail_dialog.jsp"></jsp:include>
<jsp:include page="add_uniqCode_dialog.jsp"></jsp:include>
<jsp:include page="../sys/print_two.jsp"></jsp:include>
<jsp:include page="purchaseReturnBillPrint.jsp"></jsp:include>
<jsp:include page="../base/waitingPage.jsp"></jsp:include>
<jsp:include page="../base/search_vendor_dialog.jsp"></jsp:include>

<script src="<%=basePath%>/views/logistics/purchaseReturnBillDtlController.js"></script>
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/dateFormatUtil.js"></script>
<script src="<%=basePath%>/Olive/plugin/print/LodopFuncs.js"></script>

</body>
</html>
