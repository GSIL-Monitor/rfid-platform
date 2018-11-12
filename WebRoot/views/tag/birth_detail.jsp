<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
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
                            <button class="btn btn-xs bigger btn-yellow dropdown-toggle" onclick="history.back(-1);">
                                <i class="ace-icon fa fa-arrow-left"></i>
                                返回
                            </button>
                        </div>
                    </div>

                    <div class="widget-body">
                        <div class="widget-main padding-12">

                            <form class="form-horizontal" role="form">
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="search_billNo">批次号</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_billNo" name="filter_LIKES_billNo"
                                               type="text" disabled value='${init.billNo}'/>
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_billDate">导入时间</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_billDate" name="filter_LIKES_billDate"
                                               type="text" disabled
                                               value='<fmt:formatDate value="${init.billDate}" pattern="yyyy-MM-dd HH:mm:ss"/>'/>
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_fileName">导入文件名</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_fileName" name="filter_LIKES_fileName"
                                               type="text" disabled value='${init.fileName}'/>
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_totSku">SKU数</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_totSku" name="filter_LIKES_totSku"
                                               type="text" disabled value='${init.totSku}'/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="search_totEpc">总数量</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_totEpc" name="filter_LIKES_totEpc"
                                               type="text" disabled value='${init.totEpc}'/>
                                    </div>
                                   <%-- <label class="col-xs-1 control-label" for="search_unit2Id">收货方</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_unit2Id" name="filter_LIKES_unit2Id"
                                               type="text" disabled value='${init.destId}'/>
                                    </div>--%>
                                    <label class="col-xs-1 control-label" for="search_totPrintQty">已打印数量</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_totPrintQty"
                                               name="filter_LIKES_totPrintQty"
                                               type="text" disabled value='${init.totPrintQty}'/>
                                    </div>

                                </div>
                                <div class="form-group">
                                    <div class="col-xs-offset-5 col-xs-12">
                                        <button type="button" class="btn-sm btn-primary" onclick="printRfidDtlTag()">
                                            <i class="ace-icon fa fa-print"></i>
                                            <span class="bigger-110">打印Rfid标签</span>
                                        </button>
                                        <button type="button" class="btn-sm btn-primary" onclick="printLabelDtlTag()">
                                            <i class="ace-icon fa fa-print"></i>
                                            <span class="bigger-110">打印洗水唛</span></button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>

<div class="hr hr4"></div>
<div class="widget-box transparent">
    <div class="widget-header">
        <h4 class="widget-title lighter">单据明细</h4>
        <div class="widget-toolbar no-border">
            <ul class="nav nav-tabs" id="myTab">
                <li id ="dtlli" class="active">
                    <a data-toggle="tab" href="#intdtl"> 标签明细</a>
                </li>
                <li id ="epcli">
                    <a data-toggle="tab" href="#epcs"> 唯一码明细</a>
                </li>
            </ul>
        </div>
        <div class="widget-toolbar no-border" id="searchbtn" title="条件过滤" onclick="showSearchForm()">
            <span class="bigger-110"><i class="ace-icon fa fa-search"></i></span>
        </div>

        <div id="searchPannel" class="widget-main padding-12">
            <form class="form-horizontal" role="form" hidden id="searchForm">
                <div class="form-group">

                    <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 col-sm-offset-3 control-label text-right"
                           for="filter_LIKES_sku">SKU</label>
                    <div class="col-xs-8 sm-4 col-md-2">
                        <input class="form-control" name="filter_LIKES_sku" id="filter_LIKES_sku"
                               placeholder="模糊查询"/>
                    </div>
                    <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4 ">
                        <button type="button" class="btn-sm btn-primary" onclick="_search()">
                            <i class="ace-icon fa fa-search"></i>
                            <span class="bigger-110">查询</span>
                        </button>
                        <button type="reset" class="btn-sm btn-yellow" onclick="clearData()">
                            <i class="ace-icon fa fa-search"></i>
                            <span class="bigger-110">清空</span>
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="widget-body">
        <div class="widget-main padding-12 no-padding-left no-padding-right">
            <div class="tab-content padding-4">
                <div id="intdtl" class="tab-pane in active">
                    <table id="detailgrid"></table>
                </div>
                <div id="epcs" class="tab-pane">
                    <table id="epcgrid"></table>
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
        initEpcGrid();
    });
    function initGrid() {
        var billNo = "${init.billNo}";
        $("#detailgrid").jqGrid({
            height: 500,
            url: basePath + "/tag/birth/detailPage.do?billNo=" + billNo,
            datatype: "json",
            colModel: [
                {name: 'id', label: 'id', hidden: true, width: 40},
//                {name:"unicode",label:'唯一码',width:40},
                {name: 'styleId', label: '款号', editable: true, width: 60},
                {name: 'colorId', label: '色码', editable: true, width: 40},
                {name: 'sizeId', label: '尺码', editable: true, width: 40},
                {name: 'qty', label: '数量', editable: true, width: 40},
                {name: 'printQty', label: '已打印数量', editable: true, width: 40},
                {
                    name: 'status', label: '状态', editable: true, width: 40, align: "center",
                    formatter: function (cellValue, option, rowObject) {
                        var html = '';
                        if (cellValue == 1) {
                            html += '<i class="fa fa-check green" title="已确认"></i>';
                        } else if (cellValue == 0) {
                            html += '<i class="fa fa-times red" title="未确认"></i>';
                        } else if (cellValue == 2) {
                            html += '<i class="fa fa-print blue" title="已打印"></i>';
                        } else if (cellValue == -1) {
                            html += '<i class="fa fa-print red" title="打印中"></i>';
                        }
                        return html;
                    }
                },
                {name: 'styleName', label: '款名', editable: true, width: 40},
                {name: 'sku', label: 'SKU', editable: true, width: 40},
                {name: 'colorName', label: '颜色', editable: true, width: 40},
                {name: 'sizeName', label: '尺码', editable: true, width: 40}
            ],

            viewrecords: true,
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum: -1,
            multiselect: true,
            shrinkToFit: true,
            sortname: 'styleId',
            sortorder: "asc",
            onSelectRow: function (rowid) {
                var selectdata = $("#detailgrid").getRowData(rowid);
                console.log(selectdata);
            }
        });
        var parent_column = $("#main-container");
        $("#detailgrid").jqGrid('setGridWidth', parent_column.width() - 5);
    }
    function showSearchForm() {
        $("#searchForm").slideToggle("fast");
    }

    function initEpcGrid() {
        var billNo = "${init.billNo}";
        $("#epcgrid").jqGrid({
            height: 500,
            url: basePath + "/tag/birth/detailEpcPage.do?billNo=" + billNo,
            datatype: "json",
            mtype: "POST",
            colModel: [
                {name: 'id', label: 'id', hidden: true, width: 240},
                {name: 'code', label: '唯一码', eidtable: true, width: 160},
                {
                    name: 'status', label: '状态', editable: true, width: 40, align: "center",
                    formatter: function (cellValue, option, rowObject) {
                        var html = '';
                        if (cellValue == 1) {
                            html += '<i class="fa fa-sign-in blue" title="已入库"></i>';
                        } else if (cellValue == 0) {
                            html += '<i class="fa fa-caret-square-o-down blue" title="未入库"></i>';
                        }
                        return html;
                    }
                },
                {name: 'styleId', label: '款号', editable: true, width: 160},
                {name: 'styleName', label: '款名', editable: true, width: 240},
                {name: 'colorId', label: '色号', editable: true, width: 200},
                {name: 'colorName', label: '颜色', editable: true, width: 200},
                {name: 'sizeId', label: '尺寸', editable: true, width: 200},
                {name: 'sizeName', label: '尺码', editable: true, width: 240},
                {name: 'sku', label: 'SKU', hidden: true},
            ],
            viewrecords: true,
            autowidth: false,
            rownumbers: true,
            altRows: true,
            rowNum: -1,
            multiselect: true,
            shrinkToFit: true,
            sortname: 'styleId',
            sortorder: "asc",
        });
        var parent_column = $("#main-container");
        $("#epcgrid").jqGrid('setGridWidth', parent_column.width() - 5);
    }
    function printRfidDtlTag(){
        //获取选中li标签 Id
        var selectedTab = $("li.active").attr('id');
        var selectData = getSelectData(selectedTab);
        if(selectedTab == "dtlli"){
            downloadDtlPrintInfo($("#search_billNo").val(),'rfid',JSON.stringify(selectData),'');
        }else{
            downloadDtlPrintInfo($("#search_billNo").val(),'rfid','',JSON.stringify(selectData));
        }

    }
    function printLabelDtlTag(){
        var selectedTab = $("li.active").attr('id');
        var selectData = getSelectData(selectedTab);
        if(selectedTab == "dtlli"){
            downloadDtlPrintInfo($("#search_billNo").val(),'label',JSON.stringify(selectData),'');
        }else{
            downloadDtlPrintInfo($("#search_billNo").val(),'label','',JSON.stringify(selectData));
        }

    }
    function _search(){
        var billNo = "${init.billNo}";
        var sku = $("#filter_LIKES_sku").val();
        $("#detailgrid").jqGrid('setGridParam', {
            url: basePath + "/tag/birth/detailPage.do",
            postData: {
                billNo:billNo,
                sku:sku
            }
        }).trigger("reloadGrid");
        $("#epcgrid").jqGrid('setGridParam', {
            url: basePath + "/tag/birth/detailEpcPage.do",
            postData: {
                billNo:billNo,
                sku:sku
            }
        }).trigger("reloadGrid");

    }

    function clearData() {
        $("#filter_LIKES_sku").val("");
        _search();
    }
    //获取选中表单数据
    function getSelectData(selectedTab){
        var dtlListStr=[];
        var epcListStr=[];
        if(selectedTab == "dtlli"){
            $.each($('#detailgrid').jqGrid('getGridParam','selarrrow'),function(index,value){
                var rowData = $("#detailgrid").getRowData(value);
                dtlListStr.push(rowData.sku);
                console.log(dtlListStr);
            });
            return dtlListStr;

        }else if(selectedTab="epcli"){
            $.each($('#epcgrid').jqGrid('getGridParam','selarrrow'),function(index,value){
                var rowData = $("#epcgrid").getRowData(value);
                epcListStr.push(rowData.code);
                console.log(epcListStr);
            });
            return epcListStr;
        }
    }
    function searchDtl(){

    }
    //下载文件
    function downloadDtlPrintInfo(billNo,outPutFile,dtlListStr,epcListStr){
        //post格式下载
        var url = basePath + "/tag/birth/printByBillNo.do";
        var form = $("<form></form>").attr("action", url).attr("method", "post");
        form.append($("<input></input>").attr("type", "hidden").attr("name", "billNo").attr("value", billNo));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "dtlListStr").attr("value", dtlListStr));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "epcListStr").attr("value", epcListStr));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "outFileName").attr("value", outPutFile));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "isAll").attr("value", false));
        form.appendTo('body').submit().remove();
    }
</script>
</body>
</html>