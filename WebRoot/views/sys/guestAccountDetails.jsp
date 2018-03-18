<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/9/29
  Time: 14:45
  To change this template use File | Settings | File Templates.
--%>
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
        var mainUrl = "${mainUrl}";
        var userId = "${userId}";
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
                                    <div class="btn-group btn-group-sm pull-right">
                                        <button type="button" class="btn btn-info btn-yellow" onclick="backward()">
                                            <i class="ace-icon fa fa-arrow-left"></i>
                                            <span class="bigger-110">返回</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-right">
                                        <button type="button" class="btn btn-info" onclick="showAdvSearchPanel();">
                                            <i class="ace-icon fa fa-binoculars"></i>
                                            <span class="bigger-110">高级查询</span>
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="hr hr4"></div>

                            <div class="widget-main" id="searchPanel" style="display: none">
                                <form class="form-horizontal" role="form" id="searchForm">
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="search_billNo">单号</label>
                                        <div class="col-xs-8 col-sm-8 col-md-2 col-lg-2">
                                            <input class="form-control" id="search_billNo" name="filter_LIKES_billNo"
                                                   type="text" placeholder="模糊查询"/>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1  control-label"
                                               for="search_billDate">单据日期</label>
                                        <div class="col-xs-8 col-sm-8 col-md-2 col-lg-2">
                                            <div class="input-group">
                                                <input class="form-control date-picker" id="search_billDate"
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
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="select_billType">单据类型状态</label>
                                        <div class="col-xs-8 col-sm-8 col-md-2 col-lg-2">
                                            <select class="form-control" id="select_billType"
                                                    name="filter_EQS_billType">
                                                <option value="">--请选择--</option>
                                                <option value="0">收款</option>
                                                <option value="1">储值</option>
                                                <option value="2">付款</option>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-xs-1 control-label" for="search_customsId">客户</label>
                                        <div class="col-xs-2">
                                            <div class="input-group">
                                                <input class="form-control" id="search_customsId" type="text"
                                                       name="filter_EQS_customsId" readonly/>
                                                <span class="input-group-btn">
                                                    <button class="btn btn-sm btn-default" id="search_guest_button"
                                                            type="button"
                                                            onclick="openSelectGuestDialog('#search_customsId','#search_customsName','guestAccountPage')">
                                                        <i class="ace-icon fa fa-list"></i>
                                                    </button>
											    </span>
                                                <input class="form-control" id="search_customsName" type="text"
                                                       name="customsName" readonly/>
                                            </div>
                                        </div>

                                        <label class="col-xs-1 control-label" for="search_vendorId">供应商</label>
                                        <div class="col-xs-2">
                                            <div class="input-group">
                                                <input class="form-control" id="search_vendorId" type="text"
                                                       name="filter_EQS_vendorId" readonly/>
                                                <span class="input-group-btn">
                                                    <button class="btn btn-sm btn-default" id="search_vendor_button"
                                                            type="button"
                                                            onclick="openSelectVendorDialog('#search_vendorId','#search_vendorName','guestAccountPage')">
                                                        <i class="ace-icon fa fa-list"></i>
                                                    </button>
											    </span>
                                                <input class="form-control" id="search_vendorName" type="text"
                                                       name="vendorName" readonly/>
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
                                                <span class="bigger-110">清空</span></button>
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

    <jsp:include page="../layout/footer_js.jsp"></jsp:include>
    <!--/.fluid-container#main-container-->
</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="../base/simp_select_guest_dialog.jsp"></jsp:include>
<jsp:include page="../base/simp_select_vendor_dialog.jsp"></jsp:include>
<script>
    $(function () {
        initGrid();
    });

    function initGrid() {
        $("#grid").jqGrid({
            height: "auto",
            mtype: "POST",
            datatype: "json",
            url: basePath + "/sys/guestAccount/findDetails.do?userId=" + userId,
            colModel: [
                {name: 'id', label: '', sortable: true, hidden: true},
                {name: 'billNo', label: '单据编号', sortable: true, width: 200},
                {name: 'billType', label: '单据类型', sortable: true, hidden: true},
                {
                    name: '', label: '单据类型', sortable: true, width: 100,
                    formatter: function (cellValue, options, rowObject) {
                        switch (rowObject.billType) {
                            case "0":
                                return "收款";
                            case "1":
                                return "储值";
                            case "2":
                                return "付款";
                            default:
                                return "";
                        }
                    }
                },
                {name: 'billDate', label: '单据日期', sortable: true, width: 200},
                {name: 'customsName', label: '客户名', sortable: true, width: 150},
                {name: 'customsId', label: '客户ID', sortable: true, width: 150},
                {name: 'vendorName', label: '供应商名', sortable: true, width: 150},
                {name: 'vendorId', label: '供应商ID', sortable: true, width: 150},
                {name: 'payPrice', label: '交易金额', sortable: true, width: 150},
                {name: 'oprId', label: '操作帐号', sortable: true, width: 100},
                {name: 'ownerId', label: '所属方', sortable: true, width: 150},
                {name: 'remark', label: '备注', sortable: true, width: 200}
            ],
            viewrecords: true,
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum: 50,
            rowList: [20, 50, 100],
            pager: "#grid-pager",
            multiselect: false,
            shrinkToFit: true,
            sortname: 'billNo',
            sortorder: 'desc',
            autoScroll: false,
            footerrow: true,
            gridComplete: function () {
                setFooterData();
            }
        });

    }

    function setFooterData() {
        var sum_payPrice = $("#grid").getCol('payPrice', false, 'sum');
        $("#grid").footerData('set', {
            billNo: "合计",
            payPrice: sum_payPrice
        });
    }

    function refresh() {
        location.reload(true);
    }

    function _search() {

        var serializeArray = $("#searchForm").serializeArray();
        var params = array2obj(serializeArray);
        $("#grid").jqGrid('setGridParam', {
            page: 1,
            url: basePath + "/sys/guestAccount/findDetails.do?userId=" + userId,
            postData: params
        });
        $("#grid").trigger("reloadGrid");
        setFooterData();
    }

    function showAdvSearchPanel() {
        $("#searchPanel").slideToggle("fast");
    }

    function backward() {
        location.href = basePath + mainUrl;
    }

</script>
</body>
</html>