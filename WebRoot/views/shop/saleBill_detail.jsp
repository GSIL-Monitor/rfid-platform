<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>
<body class="no-skin">
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>
    <div class="main-content">
        <div class="main-content-inner">
        <div class="page-header">
            <h1>
                <a href="#" onclick="toIndex()">销售单查询</a>
                <small>
                    <i class="ace-icon fa fa-angle-double-right"></i>
                        查看明细
                </small>
            </h1>
        </div><!-- /.page-header -->
        <div class="page-content">

        <div class="row">

            <div class="col-xs-8 col-sm-4">

                <div class="widget-box widget-color-blue  light-border">
                    <div class="widget-header">
                        <h5 class="widget-title">基本信息</h5>
                    </div>
                    <div class="widget-body">
                        <div class="widget-main padding-12">
                            <form class="form-horizontal" role="form">
                                <div class="form-group">
                                    <label class="col-xs-3 control-label" for="search_billNo">销售单号</label>

                                    <div class="col-xs-6">
                                        <input class="form-control" id="search_billNo" name="billNo"
                                               type="text" readonly value="${saleBill.billNo}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-3 control-label" for="search_shopId">门店</label>

                                    <div class="col-xs-6">
                                        <input class="form-control" id="search_shopId" name="shopId"
                                               type="text" readonly value="[${saleBill.shopId}]${saleBill.shopName}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-3 control-label" for="search_clientId">收银员</label>

                                    <div class="col-xs-6">
                                        <input class="form-control" id="search_clientId" name="clientId"
                                               type="text" readonly value="[${saleBill.clientId}]${saleBill.clientName}"/>
                                    </div>

                                </div>
                                <div class="form-group">
                                    <label class="col-xs-3 control-label" for="search_client2Id">会员</label>

                                    <div class="col-xs-6">
                                        <input class="form-control" id="search_client2Id" name="client2Id"
                                               type="text" readonly value="[${saleBill.client2Id}]${saleBill.client2Name}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-3 control-label" for="search_totOrderQty">总数量</label>

                                    <div class="col-xs-6">
                                        <input class="form-control" id="search_totOrderQty" name="totOrderQty"
                                               type="text" readonly value="${saleBill.totOrderQty}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-3 control-label" for="search_totOrderValue">总额度</label>

                                    <div class="col-xs-6">
                                        <input class="form-control" id="search_totOrderValue" name="totOrderValue"
                                               type="text" readonly value="${saleBill.totOrderValue}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-3 control-label" for="search_totActValue">实际额度</label>

                                    <div class="col-xs-6">
                                        <input class="form-control" id="search_totActValue" name="totActValue"
                                               type="text" readonly value="${saleBill.totActValue}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-3 control-label" for="search_payWay">支付方式</label>

                                    <div class="col-xs-6">
                                        <input class="form-control" id="search_payWay" name="payWay"
                                               type="text" readonly value="[${saleBill.payWay}]${saleBill.payWayName}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-3 control-label" for="search_payForCash">总付款</label>

                                    <div class="col-xs-6">
                                        <input class="form-control" id="search_payForCash" name="payForCash"
                                               type="text" readonly value="${saleBill.payForCash}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-3 control-label" for="search_backForCash">找零</label>

                                    <div class="col-xs-6">
                                        <input class="form-control" id="search_backForCash" name="backForCash"
                                               type="text" readonly value="${saleBill.backForCash}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-3 control-label" for="search_balance">差额</label>

                                    <div class="col-xs-6">
                                        <input class="form-control" id="search_balance" name="balance"
                                               type="text" readonly value="${saleBill.balance}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-3 control-label" for="form_toZero">抹零</label>

                                    <div class="col-xs-6">
                                        <input class="form-control" id="form_toZero" name="toZero"
                                               type="text" readonly value="${saleBill.toZero}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-3 control-label" for="search_remark">备注</label>

                                    <div class="col-xs-6">
                                        <input class="form-control" id="search_remark" name="remark"
                                               type="text" readonly value="${saleBill.remark}"/>
                                    </div>
                                </div>

                            </form>
                        </div>
                    </div>
                </div>

            </div>


            <div class="col-xs-16 col-sm-8">
                <div class="widget-box widget-color-blue light-border">
                    <div class="widget-header ">
                        <h4 class="widget-title lighter">销售明细</h4>

                        <div class="widget-toolbar no-border">
                        </div>
                    </div>
                    <div class="widget-body">
                        <div class="widget-main">
                            <div id="detail" class="tab-pane in active">
                                <table id="detailgrid"></table>
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
    <script type="text/javascript">
        $(function () {
            initGrid();
        });
        function toIndex() {
            location.href = basePath+"/shop/saleBill/index.do";
        }
        function initGrid() {
            var billId = "${saleBill.id}";
            $("#detailgrid").jqGrid({
                height: 500,
                url: basePath + "/shop/saleBill/listDtl.do?billId=" + billId,
                datatype: "json",
                colModel: [
                    {name: 'id', label: 'id', hidden: true, width: 40},
                    {name: 'barcode', label: 'SKU', editable: true, width: 80},
                    {name: 'styleId', label: '款号', editable: true, width: 60},
                    {name: 'colorId', label: '色码', editable: true, width: 40},
                    {name: 'sizeId', label: '尺码', editable: true, width: 40},
                    {name: 'qty', label: '数量', editable: true, width: 40},
                    {name: 'price', label: '吊牌价', editable: true, width: 40},
                    {name: 'acePrice', label: '实际单价', editable: true, width: 40},
                    {name: 'qty', label: '数量', editable: true, width: 40},
                    {name: 'actValue', label: '实际总额', editable: true, width: 40},
                    {name: 'uniqueCode', label: '吊牌码', editable: true, width: 40},
                    {name: 'styleName', label: '款名', editable: true, width: 40},
                    {name: 'colorName', label: '颜色', editable: true, width: 40},
                    {name: 'sizeName', label: '尺码', editable: true, width: 40}
                ],

                viewrecords: true,
                autowidth: true,
                rownumbers: true,
                altRows: true,
                multiselect: false,
                shrinkToFit: true,
                sortname: 'id',
                sortorder: "asc"
            });
            var parent_column = $("#detailgrid").closest('.widget-main');
            $("#detailgrid").jqGrid('setGridWidth', parent_column.width() - 2);
            $(window).on('resize.jqGrid', function () {
                var parent_column = $("#detailgrid").closest('.widget-main');
                $("#detailgrid").jqGrid('setGridWidth', parent_column.width() - 2);
            });
        }

    </script>
</body>
</html>