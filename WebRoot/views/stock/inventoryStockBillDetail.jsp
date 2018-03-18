<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/10/30
  Time: 11:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>
    <style>
        #searchBtn:hover {
            transform: rotate(360deg);
            transition: all 0.5s;
        }
    </style>
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
                            <a class="btn btn-xs bigger btn-yellow dropdown-toggle" href=javascript:history.go(-1)>
                                <i class="ace-icon fa fa-arrow-left"></i> 返回
                            </a>
                        </div>
                    </div>
                    <div class="widget-body">
                        <div class="widget-main padding-12">
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="search_id">单据编号</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_id" name="filter_LIKES_id"
                                               type="text" disabled value="${erpBill.id}"/>
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_taskId">任务编号</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_taskId" name="filter_LIKES_taskId"
                                               type="text" disabled value="${erpBill.taskId}"/>
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_origId">盘点单位</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_origId" name="filter_LIKES_origId"
                                               type="text" disabled value="[${erpBill.origId}]${erpBill.origName}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="search_totQty">库存数</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_totQty" name="filter_LIKES_totQty"
                                               type="text" disabled value="${erpBill.totQty}"/>
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_scanQty">扫描数量</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_scanQty" name="filter_LIKES_scanQty"
                                               type="text" disabled value="${erpBill.scanQty}"/>
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_billDate">盘点时间</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_billDate" name="filter_LIKES_billDate"
                                               type="text" disabled
                                               value='<fmt:formatDate value="${erpBill.billDate}" pattern="yyyy-MM-dd"/>'/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="hr hr4"></div>


        <div class="widget-box transparent">
            <div class="widget-header ">
                <h4 class="widget-title lighter">任务明细</h4>
                <div class="widget-toolbar no-border">
                    <ul class="nav nav-tabs" id="myTab">
                        <li>
                            <a data-toggle="tab" href="#codedetail">CODE明细</a>
                        </li>
                        <li class="active">
                            <a data-toggle="tab" href="#detail">SKU明细</a>
                        </li>
                    </ul>
                </div>
                <div class="widget-toolbar no-border" id="searchBtn" title="条件过滤" onclick="showSearchForm()">
                    <span class="bigger-110"><i class="ace-icon fa fa-search"></i></span>
                </div>
                <div class="widget-toolbar no-border" title="导出" onclick="exportExcel()">
                    <span class="bigger-110"><i class="ace-icon fa fa-file-excel-o"></i></span>
                </div>

                <div class="widget-toolbar no-border" style="margin-right: 20px">
                    <label for="displayType">
                        <input type="checkbox" id="displayType" title="仅显示差异" style="vertical-align: middle"
                               onclick="showDifference()">
                        <span style="vertical-align: middle"> 仅显示差异</span>
                    </label>
                </div>
            </div>
            <div id="searchPannel" class="widget-main padding-12">
                <div class="form-horizontal" hidden id="searchForm">
                    <div class="form-group">
                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 col-sm-offset-3 control-label text-right"
                               for="matchSku">SKU</label>
                        <div class="col-xs-8 sm-4 col-md-2">
                            <input class="form-control" id="matchSku"
                                   placeholder="模糊查询"/>
                        </div>
                        <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4 ">
                            <button type="button" class="btn btn-sm btn-primary" onclick="_search()">
                                <i class="ace-icon fa fa-search"></i>
                                <span class="bigger-110">查询</span>
                            </button>
                            <button type="button" class="btn btn-sm btn-yellow" onclick="clearData()">
                                <i class="ace-icon fa fa-search"></i>
                                <span class="bigger-110">清空</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="widget-body">
                <div class="widget-main padding-12 no-padding-left no-padding-right">
                    <div class="tab-content padding-4">
                        <div id="detail" class="tab-pane in active">
                            <table id="detailgrid"></table>
                        </div>
                        <div id="detailgrid-pager"></div>

                        <div id="codedetail" class="tab-pane">
                            <table id="codedetaillgrid"></table>
                        </div>
                        <div id="codedetailgrid-pager"></div>
                    </div>
                </div>
            </div>
        </div>
        <form id="form1" action="" method=post name=form1 style='display:none'>
            <input id="id" type=hidden  name='id' value=''>
            <input id="isChecked" type=hidden  name='isChecked' value=''>
            <input id="sku" type=hidden  name='sku' value=''>
        </form>

    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>
</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<script type="text/javascript">
    $(function () {
        initCodedetailGrid();
        initGrid();
    });

    var billNo = "${erpBill.id}";
    var params = {};

    function showSearchForm() {
        $("#searchForm").slideToggle("fast");
    }

    function initCodedetailGrid() {
        $("#codedetaillgrid").jqGrid({
            height: 450,
            url: basePath + "/stock/erpBill/findInventoryRecord.do",
            postData: {
                billNo: $("#search_id").val()
            },
            datatype: "json",
            colModel: [
                {name: 'id', label: 'id', hidden: true, width: 40},
                {name: 'styleId', label: '款号', width: 40},
                {name: 'styleName', label: '款名', width: 40},
                {name: 'colorId', label: '色码', width: 40},
                {name: 'sizeId', label: '尺码', width: 40},
                {name: 'sku', label: 'SKU', width: 40},
                {name: 'code', label: '唯一码', width: 40},
                {name: 'isScanned', label: '', hidden: true},
                {
                    name: 'stockState', label: '盘点情况', width: 40,
                    formatter: function (cellValue, option, rowObject) {
                        switch (rowObject.isScanned) {
                            case 1:
                                return "在库";
                            case 0:
                                return "不在库";
                            default:
                                return "不在库";
                        }
                    }
                },
                {name: 'recordDate', label: '盘点日期', width: 40}
            ],
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum: -1,
            pager: "#codedetailgrid-pager",
            multiselect: true,
            shrinkToFit: true,
            sortname: 'stockState',
            sortorder: "desc",
            footerrow: true,
            loadComplete: function () {
                setFooterData();
            }
        });
        var parent_column = $("#main-container");
        $("#codedetaillgrid").jqGrid('setGridWidth', parent_column.width() - 5);

        $("#codedetailgrid-pager_center").html("");
    }

    function initGrid() {
        $("#detailgrid").jqGrid({
            height: 450,
            url: basePath + "/stock/erpBill/findErpBillDtl.do",
            postData: {
                billNo: $("#search_id").val()
            },
            datatype: "json",
            colModel: [
                {name: 'id', label: 'id', hidden: true, width: 40},
                {name: 'styleId', label: '款号', width: 40},
                {name: 'styleName', label: '款名', width: 40},
                {name: 'colorId', label: '色码', width: 40},
                {name: 'sizeId', label: '尺码', width: 40},
                {name: 'sku', label: 'SKU', width: 40},
                {name: 'qty', label: '库存数量', width: 40},
                {name: 'actQty', label: '实际数量', width: 40, sortable: true},
                {
                    name: '', label: '差异数量', width: 40,
                    formatter: function (cellValue, option, rowObject) {
                        return parseInt(rowObject.qty) - parseInt(rowObject.actQty);
                    }
                },
                {name: 'price', label: '吊牌价', width: 40, sortable: true}
            ],
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum: -1,
            pager: "#detailgrid-pager",
            multiselect: false,
            shrinkToFit: true,
            sortname: 'actQty',
            sortorder: "desc",
            footerrow: true,
            ondblClickRow: function (rowId) {
                var row = $("#detailgrid").jqGrid("getRowData", rowId);
                $("#myTab a[href='#codedetail']").tab('show');
                var isChecked = false;
                if ($("#displayType").is(':checked')) {
                    isChecked = true;
                }
                $("#codedetaillgrid").jqGrid('setGridParam', {
                    url: basePath + "/stock/erpBill/findInventoryRecord.do",
                    postData: {
                        billNo: $("#search_id").val(),
                        isChecked: isChecked,
                        sku: row.sku
                    }
                });
                $("#codedetaillgrid").trigger("reloadGrid");
            },
            loadComplete: function () {
                setFooterData();
            }
        });
        var parent_column = $("#main-container");
        $("#detailgrid").jqGrid('setGridWidth', parent_column.width() - 5);
        $("#detailgrid-pager_center").html("");
    }

    function setFooterData() {
        var sum_sku = $("#detailgrid").getCol('sku', false, 'count');
        var sum_qty = $("#detailgrid").getCol('qty', false, 'sum');
        var sum_actQty = $("#detailgrid").getCol('actQty', false, 'sum');
        $("#detailgrid").footerData('set', {
            styleId: "合计",
            sku: sum_sku,
            qty: sum_qty,
            actQty: sum_actQty
        });
    }

    function _search() {
        initAjaxParams();
        $("#detailgrid").jqGrid('setGridParam', {
            url: basePath + "/stock/erpBill/findErpBillDtl.do",
            postData: params
        });
        $("#codedetaillgrid").jqGrid('setGridParam', {
            url: basePath + "/stock/erpBill/findInventoryRecord.do",
            postData: params
        });
        $("#detailgrid").trigger("reloadGrid");
        $("#codedetaillgrid").trigger("reloadGrid");
    }

    function showDifference() {
        _search();
    }

    function clearData() {
        $("#matchSku").val("");
        _search();
    }
    
    function exportExcel() {
        initAjaxParams();
      /*  $.ajax({
         dataType: "json",
         url: basePath + "/stock/erpBill/exportExcel.do",
         data: params,
         type: "POST",
         success: function (data) {
         $.gritter.add({
         text: data.msg,
         class_name: 'gritter-success  gritter-light'
         });
         }
         })*/
      $("#form1").attr("action",basePath + "/stock/erpBill/exportExcel.do");
      $("#id").val(params.id);
      $("#isChecked").val(params.isChecked);
      $("#sku").val(params.sku);
      $("#form1").submit();
    }

    function initAjaxParams() {
        if ($("#displayType").is(':checked')) {
            params = {
                id: billNo,
                isChecked: true,
                sku: $("#matchSku").val()
            }
        }else {
            params = {
                id: billNo,
                isChecked: false,
                sku: $("#matchSku").val()
            }
        }
    }
</script>
</body>
</html>