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
        var userId = "${user.id}";
        var billNo = "${billNo}";
        var defaultWarehId = "${defaultWarehId}";
        var roleid = "${roleid}";
        var Codes = "${Codes}";
        var groupid="${groupid}";
        var pageType = "${pageType}";
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
                                                       for="search_origId">仓库</label>
                                                <div class="col-md-4">
                                                    <select class="form-control selectpicker show-tick"
                                                            id="search_origId"
                                                            name="filter_EQS_origId"
                                                            style="width: 100%;" data-live-search="true">
                                                    </select>
                                                </div>
                                                <label class="col-md-2 control-label" for="SdestId"></span>库位</label>
                                                <div class="col-md-4" style="position:relative;z-index: 100;">
                                                    <input class="form-control" id="SdestId" name="filter_LIKES_NallocationId" readonly
                                                           style="width: 100%;background-color: #abbac3 !important;color: #ffffff;border-width: 3px;">
                                                    </input>
                                                    <div class="widget-body" id="Stree" style="display:none;height:600px;width: 120%; overflow-y:auto;text-align: left;position:absolute;z-index: 9999;!important;">
                                                        <div class="col-sm-12" style="width: 98%;margin-top: 3%;">
                                                            <input class="form-control" id="Ssearch_organizationName" type="text"
                                                                   placeholder="模糊查询,回车结束"/>
                                                        </div>
                                                        <div class="col-sm-12" style="text-align: center;margin-top:2%;">
                                                            <button type="button" class="btn btn-sm btn-primary" onclick="SchooseCage()" style="width: 35%;border: 0;margin-right: 8%;">
                                                                <span class="bigger-110">确定</span>
                                                            </button>
                                                            <button type="button" class="btn btn-sm btn-warning" onclick="SunChoose()" style="width: 35%;border: 0;">
                                                                <span class="bigger-110">取消</span>
                                                            </button>
                                                        </div>
                                                        <div class="widget-main no-padding">
                                                            <div id="Sjstree"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <div class="col-md-offset-3 col-md-6">
                                                    <button type="button" class="btn btn-sm btn-primary"
                                                            onclick="_search()">
                                                        <i class="ace-icon fa fa-search"></i>
                                                        <span class="bigger-110">查询</span>
                                                    </button>
                                                    <button type="button" class="btn btn-sm btn-warning" ,
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
                                                    <div id="billNo_div">
                                                        <label class="col-md-1 control-label"
                                                               for="edit_billNo">单据编号</label>
                                                        <div class="col-md-3">
                                                            <input class="form-control" id="edit_billNo" name="billNo"
                                                                   type="text" readOnly
                                                            />
                                                        </div>
                                                    </div>
                                                    <div id="billDate_div">
                                                        <label class="col-md-1 control-label"
                                                               for="edit_billDate">单据日期</label>
                                                        <div class="col-md-3">
                                                            <input class="form-control" id="edit_billDate"
                                                                   name="billDate"
                                                                   type="text" readonly/>
                                                        </div>
                                                    </div>
                                                    <div id="userName_div">
                                                        <label class="col-md-1 control-label"
                                                               for="edit_userName">操作用户</label>
                                                        <div class="col-md-3">
                                                            <input class="form-control" id="edit_userName" name="userName" readonly
                                                                   type="text" value="${user.id}"/>
                                                        </div>
                                                    </div>

                                                </div>

                                                <div class="form-group">
                                                    <div id="origId_div">
                                                        <label class="col-md-1 control-label"
                                                               for="edit_origId">仓库</label>
                                                        <div class="col-md-3">
                                                            <select class="form-control selectpicker show-tick"
                                                                    id="edit_origId" name="origId"
                                                                    style="width: 100%;" value="${defaultWarehId}"
                                                                    data-live-search="true">
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div id="newRmId_div">
                                                        <label class="col-md-1 control-label" for="destId"></span>库位</label>
                                                        <div class="col-md-3" style="position:relative;z-index: 100;">
                                                            <input class="form-control" id="destId" name="newRmId" readonly
                                                                   style="width: 100%;background-color: #abbac3 !important;color: #ffffff;border-width: 3px;">
                                                            </input>
                                                            <div class="widget-body" id="tree" style="display:none;height:600px;width: 93%; overflow-y:auto;text-align: left;position:absolute;z-index: 9999;!important;">
                                                                <div class="col-sm-12" style="width: 98%;margin-top: 3%;">
                                                                    <input class="form-control" id="search_organizationName" type="text"
                                                                           placeholder="模糊查询,回车结束"/>
                                                                </div>
                                                                <div class="col-sm-12" style="text-align: center;margin-top:2%;">
                                                                    <button type="button" class="btn btn-sm btn-primary" onclick="chooseCage()" style="width: 35%;border: 0;margin-right: 8%;">
                                                                        <span class="bigger-110">确定</span>
                                                                    </button>
                                                                    <button type="button" class="btn btn-sm btn-warning" onclick="unChoose()" style="width: 35%;border: 0;">
                                                                        <span class="bigger-110">取消</span>
                                                                    </button>
                                                                </div>
                                                                <div class="widget-main no-padding">
                                                                    <div id="jstree"></div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>

                                                </div>
                                                <div class="form-group">
                                                    <div id="remark_div">
                                                        <label class="col-md-1 control-label"
                                                               for="edit_remark">备注</label>

                                                        <div class="col-md-11 col-sm-11">
                                                        <textarea maxlength="400" class="form-control" id="edit_remark"
                                                                  name="remark">
                                                        </textarea>
                                                        </div>
                                                    </div>

                                                </div>
                                                <div>
                                                    <input id="edit_status" name="status"
                                                           type="hidden">
                                                    </input>

                                                    <input id="edit_ownerId" name="ownerId"
                                                           value="${ownerId}"
                                                           type="hidden">
                                                    </input>
                                                    <input id="edit_Id" name="id"
                                                           type="hidden">
                                                    </input>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                    <div class="widget-box transparent">
                                        <div class="widget-header ">
                                            <div class="widget-toolbar no-border">
                                                <ul class="nav nav-tabs" id="myTab">
                                                    <li class="active">
                                                        <a data-toggle="tab" href="#addDetail">SKU明细</a>
                                                    </li>
                                                    <li>
                                                        <a data-toggle="tab" href="#codeDetail">唯一码明细</a>
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>
                                        <div class="widget-body">
                                            <div class="widget-main padding-12 no-padding-left no-padding-right">
                                                <div class="tab-content padding-4">
                                                    <div id="addDetail" class="tab-pane  active" style="height:80%;">
                                                        <table id="addDetailgrid"></table>
                                                        <div id="addDetailgrid-pager"></div>
                                                    </div>
                                                    <div id="codeDetail" class="tab-pane" style="height:80%;">
                                                        <table id="codeDetailgrid"></table>
                                                        <div id="codeDetailgrid-pager"></div>
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
<jsp:include page="add_uniqCodeRm_dialog.jsp"></jsp:include>
<jsp:include page="search_saleOrder_code_dialog.jsp"></jsp:include>
<jsp:include page="uniqueCode_detail_list.jsp"></jsp:include>
<jsp:include page="exchageGood_dialog.jsp"></jsp:include>
<jsp:include page="findRetrunNo.jsp"></jsp:include>
<jsp:include page="findWxShop.jsp"></jsp:include>
<jsp:include page="sendStreamNO.jsp"></jsp:include>
<jsp:include page="../base/search_guest_dialog.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/logistics/repositoryAdjust.js"></script>
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/dateFormatUtil.js"></script>
<script src="<%=basePath%>/Olive/plugin/print/LodopFuncs.js"></script>

<div id="dialog"></div>
<div id="progressDialog"></div>
<span id="notification"></span>
</body>
</html>