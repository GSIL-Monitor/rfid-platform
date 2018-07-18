<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/12/4
  Time: 13:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
    <html>
    <head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
        var allrowDate = [];
    </script>
    <style>
        #searchBtn:hover {
            transform: rotate(360deg);
            transition: all 0.5s;
        }
    </style>
</head>
<body>
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
                                        <input class="form-control" id="search_id"
                                               type="text" disabled value="${mergeBill.id}"/>
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_mergeBillQty">合并单据数</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_mergeBillQty"
                                               type="text" disabled value="${mergeBill.mergeBillQty}"/>
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_warehouseId">盘点单位</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_warehouseId"
                                               type="text" disabled value="[${mergeBill.warehouseId}]${mergeBill.warehouseName}"/>
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
                <h4 class="widget-title lighter">明细</h4>
                <div class="widget-toolbar no-border">
                    <ul class="nav nav-tabs" id="myTab">
                        <li>
                            <a data-toggle="tab" href="#codedetail">盘点合并明细</a>
                        </li>
                    </ul>
                </div>
                <div class="widget-toolbar no-border" id="searchBtn" title="条件过滤" onclick="showSearchForm()">
                    <span class="bigger-110"><i class="ace-icon fa fa-search"></i></span>
                </div>
                <div class="widget-toolbar no-border" title="查看原始盘点单据" onclick="showInventoryRecordList()">
                    <span class="bigger-110"><i class="ace-icon fa fa-list"></i></span>
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
                <div class="widget-toolbar no-border">
                    <button type="button " onclick="decruit()">调出</button>
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

</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="inventoryMerge_origList.jsp"></jsp:include>
<jsp:include page="inventoryMergeBillDtl_edit.jsp"></jsp:include>
<script type="text/javascript">
    $(function () {
        initGrid();
    });

    var billNo = "${mergeBill.id}";
    var params = {};
    var userId = "${userId}";

    function showSearchForm() {
        $("#searchForm").slideToggle("fast");
    }

    function showInventoryRecordList() {
        $("#inventoryMerge-origList").modal('show');
    }

    function initGrid() {
        $("#detailgrid").jqGrid({
            height: 450,
            url: basePath + "/stock/InventoryMerge/findMergeBillDtl.do?id=" + billNo,
            postData: {
                billNo: $("#search_id").val()
            },
            datatype: "json",
            colModel: [
                {name: 'id', label: 'id', hidden: true, width: 40},
                {name: 'sku', label: 'SKU', width: 40},
                {name: 'code', label: '唯一码', width: 40},
                {name: 'styleId', label: '款号', width: 40},
                {name: 'styleName', label: '款名', width: 40},
                {name: 'colorId', label: '色码', width: 40},
                {name: 'sizeId', label: '尺码', width: 40},
                {name: 'countTimes', label: '统计次数', width: 40, sortable: true},
                {name: 'scannedQty', label: '被扫描次数', width: 40, sortable: true},
                {name: 'inStock', label: '盘点数量', width: 40, sortable: true},
                {name: 'price', label: '吊牌价', width: 40, sortable: true,
                    formatter: function (cellValue, options, rowObject) {
                        var price=rowObject.price.toFixed(2);
                        return price;
                    }
                },
                {name: 'state', label: '标记', width: 40,formatter:function(cellValue, options, rowObject){
                        if (rowObject.inStock==1 && cellValue=="N"){
                            return "正常";
                        }
                        if (rowObject.inStock==0 && cellValue=="N"){
                            return "未处理";
                        }
                        if (rowObject.inStock==0 && cellValue=="Y"){
                            return "已处理";
                        }
                        if (rowObject.inStock==0 && cellValue=="X"){
                            return "无需处理";
                        }
                    }
                }
            ],
            onSelectRow:function (id) {
                var jsonObj = $("#detailgrid").jqGrid("getRowData",id);
                if (jsonObj.state=="正常"||jsonObj.state=="已处理"||jsonObj.state=="无需处理"){
                    $("#detailgrid").jqGrid("setSelection",id,false);
                }
            },
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum: -1,
            recordpos : 'left',
            pager: "#detailgrid-pager",
            multiselect: true,
            shrinkToFit: true,
            sortname: 'actQty',
            sortorder: "desc",
            footerrow: true,
            loadComplete: function () {
                setFooterData();
            }
        });
        var parent_column = $("#main-container");
        $("#detailgrid").jqGrid('setGridWidth', parent_column.width() - 5);
        $("#detailgrid-pager_center").html("");

    }
    
    function decruit() {
        if ($("#displayType").is(':checked')) {
            var rowIdd = [];
            /*获取所有行id*/
            rowIdd = $("#detailgrid").jqGrid('getGridParam','selarrrow');
            console.log(rowIdd);
            if(rowIdd.length!=0){
                for (var j= 0 ;j<rowIdd.length; j++){
                    /*获取该行id对应的值*/
                    var rowDate = $('#detailgrid').jqGrid('getRowData',rowIdd[j]);
                    allrowDate.push(rowDate);
                }
                $("#edit_inventoryMergeBillDtl_dialog").modal('show');
                /*将数据传递给dialog*/
                $("#edit_inventoryMergeBillDtl_dialog").loadData(allrowDate);
                var rowIdg = [];
                rowIdg = $("#grid").jqGrid('getGridParam','selarrrow');
                for (var i = 0; i<allrowDate.length;i++) {
                    $("#grid").addRowData(i + 1,allrowDate[i]);
                }
                allrowDate = [];
            }else {
                bootbox.alert("请选择后调出！");
            }
        }else {
            bootbox.alert("请勾选仅显示差异后再选择调出！");
        }
    }

    function setFooterData() {
        var count_code = $("#detailgrid").getCol('code', false, 'count');
        var sum_inStock = $("#detailgrid").getCol('inStock', false, 'sum');
        $("#detailgrid").footerData('set', {
            sku: "合计",
            code: count_code,
            inStock: sum_inStock
        });
    }

    function _search() {
        initAjaxParams();
        $("#detailgrid").jqGrid('setGridParam', {
            url: basePath + "/stock/InventoryMerge/findMergeBillDtl.do",
            postData: params
        });
        $("#detailgrid").trigger("reloadGrid");
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
        /*$.ajax({
            dataType: "json",
            url: basePath + "/stock/InventoryMerge/exportExcel.do",
            data: params,
            type: "POST",
            success: function (data) {
                $.gritter.add({
                    text: data.msg,
                    class_name: 'gritter-success  gritter-light'
                });
            }
        })*/
        $("#form1").attr("action", basePath + "/stock/InventoryMerge/exportExcel.do");
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
