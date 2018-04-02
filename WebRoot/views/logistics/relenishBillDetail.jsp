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
        var billNo = "${replenishBill.billNo}";

        var curOwnerId = "${ownerId}";
        var ownersId="${ownersId}";
        var userId = "${userId}";
        var defaultWarehId = "${defaultWarehId}";
        var saleOrder_busnissId = "${replenishBill.busnissId}";
        var saleOrder_buyahandId = "${replenishBill.buyahandId}";
        var slaeOrder_status = "${replenishBill.status}";
        var roleid = "${roleid}";
        var replenishType="${replenishBill.replenishType}";

    </script>
</head>
<body class="no-skin">

<div class="modal fade" id="loadingModal">
    <div style="width: 200px;height:20px; z-index: 20000; position: absolute; text-align: center; left: 50%; top: 50%;margin-left:-100px;margin-top:-10px">
        <div class="progress progress-striped active" style="margin-bottom: 0;">
            <div class="progress-bar" style="width: 100%;"></div>
        </div>
        <h5>正在加载...</h5>
    </div>
</div>

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
                                <i class="ace-icon fa fa-arrow-left"></i> 历史单据
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
                                               type="text" readOnly value="${replenishBill.billNo}"/>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_billDate">单据日期</label>
                                    <div class="col-xs-2">
                                        <input class="form-control date-picker" id="search_billDate" name="billDate"
                                               type="text" value="${replenishBill.billDate}"/>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_busnissId">销售员</label>
                                    <div class="col-xs-2">
                                        <select class="form-control selectpicker show-tick" id="search_busnissId"
                                                name="busnissId"
                                                style="width: 100%;" data-live-search="true">
                                        </select>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_buyahandId">买手</label>
                                    <div class="col-xs-2">
                                        <select class="form-control selectpicker show-tick" id="search_buyahandId"
                                                name="buyahandId"
                                                style="width: 100%;" data-live-search="true" onchange="changebuy()">
                                        </select>
                                    </div>

                                </div>
                                <div class="form-group">

                                    <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                           for="edit_replenishType">类型</label>
                                    <div class="col-xs-8 col-sm-8 col-md-2 col-lg-2 control-group">
                                        <div class="radio" id="edit_replenishType">
                                            <label><input name="replenishType" type="radio" value="1"/>购货</label>
                                            <label><input name="replenishType" type="radio" value="0"/>退货</label>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">

                                    <label class="col-xs-1 control-label"
                                           for="form_remark">备注</label>

                                    <div class="col-xs-9 col-sm-9">
                                            <textarea maxlength="400" class="form-control" id="form_remark"
                                                      name="remark">${replenishBill.remark}</textarea>
                                    </div>
                                </div>
                                <div>
                                    <input id="search_srcBillNo" name="srcBillNo" value="${replenishBill.srcBillNo}"
                                           type="hidden">
                                    </input>
                                    <input id="search_status" name="status" value="${replenishBill.status}"
                                           type="hidden">
                                    </input>
                                    <input id="search_ownerId" name="ownerId" value="${replenishBill.ownerId}"
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
            <div class="widget-main padding-12 no-padding-left no-padding-right" >
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
            <div id ="approvedCheck" class="approved" style="display: none">
                <img class="approvedImage" id="checkImage">
            </div>
        </div>
    </div>
 <%--   <img style="width: 200px;height: 200px" src="/csr/images/check/check.png">--%>
    <jsp:include page="../layout/footer.jsp"></jsp:include>
</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="add_detail_dialog.jsp"></jsp:include>


<jsp:include page="relenishBillDetail_edit.jsp"></jsp:include>


<jsp:include page="../base/waitingPage.jsp"></jsp:include>

<link href="<%=basePath%>/kendoUI/styles/kendo.common-material.min.css" rel="stylesheet">
<link href="<%=basePath%>/kendoUI/styles/kendo.rtl.min.css" rel="stylesheet">
<link href="<%=basePath%>/kendoUI/styles/kendo.material.min.css" rel="stylesheet">
<script src="<%=basePath%>/kendoUI/js/kendo.all.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/kendo.timezones.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/cultures/kendo.culture.zh-CN.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/messages/kendo.messages.zh-CN.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/views/logistics/relenishBillDetailController.js"></script>
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/dateFormatUtil.js"></script>

<script type="text/javascript">

</script>
</body>
</html>