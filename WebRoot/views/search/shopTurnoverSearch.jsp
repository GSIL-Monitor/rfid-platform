<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<html>
<!DOCTYPE html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
        var userId = "${userId}";
        var ownerId = "${ownerId}";
        var defaultPayType = "${payType}";
    </script>
    <style type="text/css">
        .ui-jqgrid-bdiv{
            overflow-x: hidden!important;
        }
    </style>
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
            <div id="page-content">
                <div class="row">
                    <div class="col-xs-12">
                        <!-- PAGE CONTENT BEGINS -->
                        <div class="widget-body">

                            <div class="widget-toolbox padding-8 clearfix">
                                <div class="btn-toolbar" role="toolbar">
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-info" onclick="refresh()">
                                            <i class="ace-icon fa fa-refresh"></i>
                                            <span class="bigger-110">刷新</span>
                                        </button>
                                    </div>
                                    <%--<div class="btn-group btn-group-sm pull-right">
                                        <button type="button" class="btn btn-info" onclick="showAdvSearchPanel();">
                                            <i class="ace-icon fa fa-binoculars"></i>
                                            <span class="bigger-110">高级查询</span>
                                        </button>
                                    </div>--%>
                                </div>
                            </div>
                            <div class="hr hr4"></div>

                            <div class="widget-main" id="searchPanel">
                                <form class="form-horizontal" role="form" id="searchForm">

                                    <div class="form-group">

                                        <input type="hidden" name="id" id="id">
                                        <input type="hidden" name="nd" id="nd" value="1542250486976">
                                        <label class="col-md-1 control-label"
                                               for="search_createTime">创建日期</label>
                                        <div class="col-md-3">
                                            <div class="input-group">
                                                <input class="form-control date-picker"
                                                       id="search_createTime"
                                                       type="text" name="GED_billDate"
                                                       data-date-format="yyyy-mm-dd"/>
                                                <span class="input-group-addon">
                                                    <i class="fa fa-exchange"></i>
                                                </span>
                                                <input class="form-control date-picker" type="text"
                                                       class="input-sm form-control"
                                                       name="LED_billDate"
                                                       data-date-format="yyyy-mm-dd"/>
                                            </div>
                                        </div>


                                        <label class="col-md-1 control-label"
                                               for="edit_shopId">店铺</label>
                                        <div class="col-md-3">
                                            <select class="form-control selectpicker show-tick"
                                                    id="edit_shopId" name="shopId"
                                                    style="width: 100%;"
                                                    data-live-search="true">
                                            </select>
                                        </div>

                                        <div id="payType_div">
                                            <label class="col-md-1 control-label"
                                                   for="edit_payType">支付方式</label>
                                            <div class="col-md-3">
                                                <select class="form-control selectpicker" id="edit_payType" name="payType">
                                                    <option value="">--选择支付方式--</option>
                                                    <option value="xianjinzhifu">现金支付</option>
                                                    <option value="zhifubaozhifu">支付宝支付</option>
                                                    <option value="wechatpay">微信支付</option>
                                                    <option value="cardpay">刷卡支付</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <div class="col-sm-offset-5 col-sm-10">
                                            <button type="button" class="btn btn-sm btn-primary" onclick="_search()">
                                                <i class="ace-icon fa fa-search"></i>
                                                <span class="bigger-110">查询</span>
                                            </button>
                                            <button type="reset" class="btn btn-sm btn-warning">
                                                <i class="ace-icon fa fa-undo"></i>
                                                <span class="bigger-110">清空</span>
                                            </button>
                                            <button id='SODtl_doPrint' type='button' style='margin: 8px' class='btn btn-sm btn-primary' onclick="doPrint()">
                                                <i class='ace-icon fa fa-print'></i>
                                                <span class='bigger-110'>打印</span>
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>


                        <table id="grid"></table>
                        <div id="grid-pager"></div>
                        <!-- PAGE CONTENT ENDS -->
                    </div>
                    <!-- /.col -->
                </div>
                <!-- /.row -->
                <!--/#page-content-->
            </div>
        </div>
    </div>


    <!--/.fluid-container#main-container-->
</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="../logistics/saleOrderBillPrint.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/dateFormatUtil.js"></script>
<script type="text/javascript" src="<%=basePath%>/views/search/shopTurnoverSearchController.js"></script>
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/print/LodopFuncs.js"></script>
</body>
</html>