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
        var pageType = "${pageType}";
        var userId = "${userId}";
        var ownersId = "${ownersId}";
        var resourcePrivilege =${resourcePrivilege};
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
                                                       for="search_origUnitId">发货方</label>
                                                <div class="col-md-10">
                                                    <div class="input-group">
                                                        <input class="form-control" id="search_origUnitId"
                                                               type="text"
                                                               name="filter_EQS_origUnitId" readonly/>
                                                        <span class="input-group-btn">
                                                            <button class="btn btn-sm btn-default"
                                                                    id="search_origUnitId_button"
                                                                    type="button"
                                                                    onclick="openSearchOrigDialog('search');">
                                                                <i class="ace-icon fa fa-list"></i>
                                                            </button>
											            </span>
                                                        <input class="form-control" id="search_origUnitName"
                                                               type="text"
                                                               name="filter_EQS_origUnitName" readonly/>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-2 control-label"
                                                       for="search_destUnitId">收货方</label>
                                                <div class="col-md-10">
                                                    <div class="input-group">
                                                        <input class="form-control" id="search_destUnitId"
                                                               type="text"
                                                               name="filter_EQS_origUnitId" readonly/>
                                                        <span class="input-group-btn">
                                                            <button class="btn btn-sm btn-default"
                                                                    id="search_destUnitId_button"
                                                                    type="button"
                                                                    onclick="openSearchDestDialog('search');">
                                                                <i class="ace-icon fa fa-list"></i>
                                                            </button>
											            </span>
                                                        <input class="form-control" id="search_destUnitName"
                                                               type="text"
                                                               name="filter_EQS_destUnitName" readonly/>
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
                                                    <div id="origUnitId_div">
                                                        <label class="col-md-1 control-label"
                                                               for="edit_origUnitId">发货方</label>
                                                        <div class="col-md-5">
                                                            <div class="input-group">
                                                                <input class="form-control" id="edit_origUnitId"
                                                                       type="text"
                                                                       name="origUnitId"
                                                                       value="${transferOrderBill.origUnitId}"
                                                                       readonly/>
                                                            <span class="input-group-btn">
												                <button class="btn btn-sm btn-default"
                                                                        id="edit_orig_button"
                                                                        type="button"
                                                                        onclick="openSearchOrigDialog('edit');">
                                                                    <i class="ace-icon fa fa-list"></i>
                                                                </button>
											                </span>
                                                                <input class="form-control" id="edit_origUnitName"
                                                                       type="text"
                                                                       name="origUnitName"
                                                                       value="${transferOrderBill.origUnitName}"
                                                                       readonly/>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div id="destUnitId_div">
                                                        <label class="col-md-1 control-label"
                                                               for="edit_destUnitId">收货方</label>
                                                        <div class="col-md-5">
                                                            <div class="input-group">
                                                                <input class="form-control" id="edit_destUnitId"
                                                                       type="text"
                                                                       name="destUnitId"
                                                                       value="${transferOrderBill.destUnitId}"
                                                                       readonly/>
                                                            <span class="input-group-btn">
												                <button class="btn btn-sm btn-default"
                                                                        id="edit_dest_button"
                                                                        type="button"
                                                                        onclick="openSearchDestDialog('edit');">
                                                                    <i class="ace-icon fa fa-list"></i>
                                                                </button>
											                </span>
                                                                <input class="form-control" id="edit_destUnitName"
                                                                       type="text"
                                                                       name="destUnitName"
                                                                       value="${transferOrderBill.destUnitName}"
                                                                       readonly/>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <div id="origId_div">
                                                        <label class="col-md-1 control-label"
                                                               for="edit_origId">出库仓库</label>
                                                        <div class="col-md-5">
                                                            <select class="form-control selectpicker show-tick"
                                                                    id="edit_origId"
                                                                    name="origId"
                                                                    style="width: 100%;" data-live-search="true"
                                                                    value="${transferOrderBill.origId}">
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div id="destId_div">
                                                        <label class="col-md-1 control-label"
                                                               for="edit_destId">入库仓库</label>
                                                        <div class="col-md-5">
                                                            <select class="form-control selectpicker show-tick"
                                                                    id="edit_destId"
                                                                    name="destId"
                                                                    style="width: 100%;" data-live-search="true"
                                                                    value="${transferOrderBill.destId}">
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <div id="billNo_div">
                                                        <label class="col-md-1 control-label"
                                                               for="edit_billNo">单据编号</label>
                                                        <div class="col-md-3">
                                                            <input class="form-control" id="edit_billNo" name="billNo"
                                                                   type="text" readOnly
                                                                   value="${transferOrderBill.billNo}"/>
                                                        </div>
                                                    </div>
                                                    <div id="billDate_div">
                                                        <label class="col-md-1 control-label"
                                                               for="edit_billDate">单据日期</label>
                                                        <div class="col-md-3">
                                                            <input class="form-control date-picker" id="edit_billDate"
                                                                   name="billDate"
                                                                   type="text" value="${transferOrderBill.billDate}"/>
                                                        </div>
                                                    </div>
                                                    <div id="printSelect_div">
                                                        <label class="col-md-1 control-label"
                                                               for="form_printSelect">打印选择</label>
                                                        <div class="col-md-3">
                                                            <select class="form-control" id="form_printSelect"
                                                                    style="width: 100%;">
                                                                <option value="0">入库数量</option>
                                                                <option value="1">出库数量</option>
                                                                <option value="2">单据数量</option>

                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <div id="remark_div">
                                                        <label class="col-md-1 control-label"
                                                               for="edit_remark">备注</label>

                                                        <div class="col-md-11 col-sm-11">
                                                            <textarea maxlength="400" class="form-control" id="edit_remark"
                                                            name="remark">${transferOrderBill.remark}</textarea>
                                                        </div>
                                                    </div>
                                                </div>
                                                <input id="edit_status" name="status" type="hidden">
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
                <!-- /.col -->

            </div>
            <!-- /.row -->
            <!--/#page-content-->


        </div>

    </div>
    <div id="edit-dialogA4" style="text-align: center;font-size:12px;display: none">

        <table style="text-align:center;font-size:10px;" border="0" cellspacing="0" cellpadding="0" width="100%"
               align="center">
            <thead>
            <tr>
                <th align="left" nowrap="nowrap" style="border:0px;font-size:17px;width:20%;">款号</th>
                <th align="left" nowrap="nowrap" style="border:0px;font-size:17px;width: 10%;">款名</th>
                <th align="left" nowrap="nowrap" style="border:0px;font-size:17px;width: 10%;">厂家/品牌</th>
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

</div>
<!--/.fluid-container#main-container-->
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="add_epc_dialog.jsp"></jsp:include>
<jsp:include page="uniqueCode_detail_list.jsp"></jsp:include>
<jsp:include page="add_uniqCode_dialog.jsp"></jsp:include>
<jsp:include page="saleOrderBillPrint.jsp"></jsp:include>
<jsp:include page="../sys/print_Test.jsp"></jsp:include>
<jsp:include page="../base/waitingPage.jsp"></jsp:include>
<jsp:include page="../base/search_guest_dialog.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/logistics/transferOrderController.js"></script>
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/dateFormatUtil.js"></script>
<script src="<%=basePath%>/Olive/plugin/print/LodopFuncs.js"></script>
<div id="dialog"></div>
<div id="progressDialog"></div>
<span id="notification"></span>
</body>
</html>