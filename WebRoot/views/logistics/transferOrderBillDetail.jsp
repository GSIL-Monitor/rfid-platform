<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/7/4
  Time: 10:53
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
        var pageType = "${pageType}";
        var billNo = "${transferOrderBill.billNo}";
        var ownersId="${ownersId}";
        var transferOrder_origId = "${transferOrderBill.origId}";
        var transferOrder_destId = "${transferOrderBill.destId}";
        var curOwnerId = "${ownerId}";
        var userId = "${userId}";
        var roleId="${roleId}";
        var transferOrder_status = "${transferOrderBill.status}";
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
                                               type="text" readonly value="${transferOrderBill.billNo}"/>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_origUnitId"><span
                                            class="text-danger">* </span>发货方</label>
                                    <div class="col-xs-2">
                                        <div class="input-group">
                                            <input class="form-control" id="search_origUnitId" type="text"
                                                   name="origUnitId" readonly value="${transferOrderBill.origUnitId}"/>
                                            <span class="input-group-btn">
                                                    <button class="btn btn-sm btn-default" id="search_orig_button"
                                                            type="button" onclick="openSearchOrigDialog();">
                                                        <i class="ace-icon fa fa-list"></i>
                                                    </button>
                                                </span>
                                            <input class="form-control" id="search_origUnitName" type="text"
                                                   name="origUnitName" readonly
                                                   value="${transferOrderBill.origUnitName}"/>
                                        </div>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_origId"><span class="text-danger">* </span>出库仓库</label>
                                    <div class="col-xs-2">
                                        <select class="form-control" id="search_origId" name="origId"
                                                style="width: 100%;">
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="search_billDate"><span
                                            class="text-danger">* </span>单据日期</label>
                                    <div class="col-xs-2">
                                        <input class="form-control date-picker" id="search_billDate" name="billDate"
                                               type="text" value="${transferOrderBill.billDate}"/>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_destUnitId"><span
                                            class="text-danger">* </span>收货方</label>
                                    <div class="col-xs-2">
                                        <div class="input-group">
                                            <input class="form-control" id="search_destUnitId" type="text"
                                                   name="destUnitId" readonly value="${transferOrderBill.destUnitId}"/>
                                            <span class="input-group-btn">
                                                    <button class="btn btn-sm btn-default" id="search_dest_button"
                                                            type="button" onclick="openSearchDestDialog();">
                                                        <i class="ace-icon fa fa-list"></i>
                                                    </button>
                                                </span>
                                            <input class="form-control" id="search_destUnitName" type="text"
                                                   name="destUnitName" readonly
                                                   value="${transferOrderBill.destUnitName}"/>
                                        </div>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_destId"><span class="text-danger">* </span>入库仓库</label>
                                    <div class="col-xs-2">
                                        <select class="form-control" id="search_destId" name="destId"
                                                style="width: 100%;">
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-1 control-label"
                                           for="form_remark">备注</label>

                                    <div class="col-xs-9 col-sm-9">
                                            <textarea maxlength="400" class="form-control" id="form_remark"
                                                      name="remark"></textarea>
                                    </div>
                                </div>
                                <div>
                                    <input id="search_status" name="status" value="${transferOrderBill.status}"
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
                    <%--<div id="editDetail" class="tab-pane in active" style="height:80%;">--%>
                    <%--<table id="editDetailgrid"></table>--%>
                    <%--</div>--%>
                </div>
                <div id="buttonGroup" class="col-sm-offset-5 col-sm-10 ">
                </div>

            </div>
        </div>
    </div>

</div>
<div id="edit-dialogA4" style="text-align: center;font-size:12px;display: none">

    <table style="text-align:center;font-size:10px;" border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
        <thead >
        <tr >
            <th align="left"  nowrap="nowrap" style="border:0px;font-size:17px;width:20%;">款号</th>
            <th align="left" nowrap="nowrap" style="border:0px;font-size:17px;width: 10%;">款名</th>
            <th align="left" nowrap="nowrap" style="border:0px;font-size:17px;width: 10%;">系列</th>
            <th align="left" nowrap="nowrap" style="border:0px;font-size:17px;width: 7%;">数量</th>
            <th align="left" nowrap="nowrap" style="border:0px;font-size:17px;width: 10%;">吊牌价</th>

        </tr>
        </thead>
        <tbody id="loadtabA4">
        <tr style="border-top:1px ;padding-top:5px;">
            <td align="left" style="border-top:1px ;padding-top:5px;width: 20%;">&nbsp;</td>
            <td align="left" style="border-top:1px ;padding-top:5px;width: 20%;">&nbsp;</td>
            <td align="left" style="border-top:1px ;padding-top:5px;width: 10%;">&nbsp;</td>
            <td align="left" style="border-top:1px ;padding-top:5px;width: 10%;">&nbsp;</td>
            <td align="left" style="border-top:1px ;padding-top:5px;width: 10%;">0</td>
        </tr>
        </tbody>
    </table>



</div>
<jsp:include page="../layout/footer.jsp"></jsp:include>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="add_detail_dialog.jsp"></jsp:include>
<jsp:include page="add_epc_dialog.jsp"></jsp:include>
<jsp:include page="uniqueCode_detail_list.jsp"></jsp:include>
<jsp:include page="add_uniqCode_dialog.jsp"></jsp:include>
<jsp:include page="../base/waitingPage.jsp"></jsp:include>
<jsp:include page="../base/search_guest_dialog.jsp"></jsp:include>
<link href="<%=basePath%>/kendoUI/styles/kendo.common-material.min.css" rel="stylesheet">
<link href="<%=basePath%>/kendoUI/styles/kendo.rtl.min.css" rel="stylesheet">
<link href="<%=basePath%>/kendoUI/styles/kendo.material.min.css" rel="stylesheet">
<script src="<%=basePath%>/kendoUI/js/kendo.all.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/kendo.timezones.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/cultures/kendo.culture.zh-CN.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/messages/kendo.messages.zh-CN.min.js"></script>
<script src="<%=basePath%>/Olive/plugin/print/LodopFuncs.js"></script>
<script type="text/javascript" src="<%=basePath%>/views/logistics/transferOrderBillDetailController.js"></script>
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/dateFormatUtil.js"></script>
<script type="text/javascript">

</script>
</body>
</html>
