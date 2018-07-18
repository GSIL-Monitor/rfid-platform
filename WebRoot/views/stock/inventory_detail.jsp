<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>
    <style>
        #searchbtn:hover{
            transform: rotate(360deg);
            transition:all 0.5s;
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

                            <button class="btn btn-xs btn-light bigger" onclick="history.back(-1);">
                                <i class="ace-icon fa fa-arrow-left"></i>
                                返回
                            </button>

                        </div>
                    </div>
                    <div class="widget-body">
                        <div class="widget-main padding-12">
                            <form class="form-horizontal" role="form">
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="search_deviceId">设备号</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_deviceId" name="filter_LIKES_deviceId"
                                               type="text" disabled value="${business.deviceId}" />
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_storageId">仓库</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_storageId" name="filter_LIKES_storageId"
                                               type="text" disabled value="[${business.origId}]${business.origName}" />
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_totEpc">总数量</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_totEpc" name="filter_LIKES_totEpc"
                                               type="text" disabled value="${business.totEpc}" />
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_totStyle">总款数</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_totStyle" name="filter_LIKES_totStyle"
                                               type="text" disabled value="${business.totStyle}" />
                                    </div>

                                </div>
                                <div class="form-group">

                                    <label class="col-xs-1 control-label" for="search_totSku">SKU数</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_totSku" name="filter_LIKES_totSku"
                                               type="text" disabled value="${business.totSku}" />
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_beginTime">开始时间</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_beginTime" name="filter_LIKES_beginTime"
                                               type="text" disabled value='<fmt:formatDate value="${business.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' />
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_endTime">结束时间</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_endTime" name="filter_LIKES_endTime"
                                               type="text" disabled value='<fmt:formatDate value="${business.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' />
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_id">任务号</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_id" name="filter_LIKES_id"
                                               type="text" disabled value="${business.id}" />
                                    </div>
                                </div>

                            </form>
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
                        <li>
                            <a data-toggle="tab" href="#record">装箱明细</a>
                        </li>
                    </ul>
                </div>
                <div class="widget-toolbar no-border" id="searchbtn" title="条件过滤" onclick="showSearchForm()">
                    <span class="bigger-110"><i class="ace-icon fa fa-search"></i></span>
                </div>
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
                        <div class="col-xs-8 sm-4 col-md-2">
                            <button type="button" class="btn btn-sm btn-primary"
                                    onclick="changeinstoke()">
                                <i class="ace-icon fa fa-search"></i> <span
                                    class="bigger-110">设置</span>
                            </button>
                        </div>
                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 col-sm-offset-3 control-label text-right"
                               for="filter_EQS_taskId" style="display: none">taskId</label>
                        <div class="col-xs-8 sm-4 col-md-2" >
                            <input class="form-control" name="filter_EQS_taskId" id="filter_EQS_taskId"
                                  value="${business.id}" style="display: none"/>
                        </div>
                        <div class="col-xs-4 col-sm-4 col-md-1 col-lg-1 btnPosition">
                            <button type="button" class="btn btn-sm btn-primary"
                                    onclick="_search()">
                                <i class="ace-icon fa fa-search"></i> <span
                                    class="bigger-110">查询</span>
                            </button>

                        </div>
                    </div>
                </form>
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
                        <div id="record" class="tab-pane">
                            <table id="recordgrid"></table>
                        </div>
                        <div id="recordgrid-pager"></div>
                    </div>
                </div>
            </div>
        </div>

    </div>

</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<script type="text/javascript">
    $(function () {
        initCodedetailGrid();
        initGrid();
        initRecordGrid();
        //initCodedetailGrid();
    });

    function showSearchForm() {
        $("#searchForm").slideToggle("fast");
    }
    function changeinstoke() {
        var codes="(";
        for(var i=0;i<savecaode.length;i++){
            if(i==0){
                codes+=savecaode[i];
            }else{
                codes+=","+savecaode[i]
            }
        }
        codes+=")";
        $.ajax({
            dataType: "json",
            async: false,
            url: basePath + "/stock/inventory/updateEpcStockInStock.do",
            data: {"codes":codes},
            type: "POST",
            success: function (msg) {
                if (msg.success) {
                    $.gritter.add({
                        text: msg.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                } else {
                    bootbox.alert(msg.msg);
                }
            }
        });

    }
    var savecaode=[];
    function initCodedetailGrid() {
        var serializeArray = $("#searchForm").serializeArray();
        var params = array2obj(serializeArray);
        $("#codedetaillgrid").jqGrid({
            height:500,
            url: basePath + "/stock/inventory/codedetailPage.do",
            postData: params,
            datatype: "json",
            colModel: [
                {name: 'id', label: 'id', hidden: true, width: 40},
                {name: 'code', label: '唯一码', editable: true, width: 40},
                {name: 'sku', label: 'SKU', editable: true, width: 40},
                {name: 'styleId', label: '款号', editable: true, width: 60},
                {name: 'colorId', label: '色码', editable: true, width: 40},
                {name: 'sizeId', label: '尺码', editable: true, width: 40},
                {name: 'styleName', label: '款名', editable: true, width: 40},
                {name: 'colorName', label: '颜色', editable: true, width: 40},
                {name: 'sizeName', label: '尺码', editable: true, width: 40},
            ],

            viewrecords: true,
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum: 20,
            rowList: [20, 50, 100],
            pager: "#codedetailgrid-pager",
            multiselect: false,
            shrinkToFit: true,
            sortname: 'id',
            sortorder: "asc",
            multiselect: true,
             onSelectRow: function (rowid,status) {
                 debugger;
                 var row = $("#codedetaillgrid").getRowData(rowid);
                 var status=status;
                 if(status){
                     savecaode.push(row.code)
                 }else{
                     for(var i=0;i<savecaode.length;i++){
                         if(savecaode[i]==row.code){
                             savecaode.splice(i,1);
                         }
                     }
                 }
             },
             onSelectAll:function (aRowids,status) {
                 debugger;
                 var rowid=aRowids;
                 var status=status;
                 if(status){
                     for(var i=0;i<aRowids.length;i++){
                         var row = $("#codedetaillgrid").getRowData(rowid);
                         savecaode.push(row.code)
                     }
                 }else{
                     for(var i=0;i<aRowids.length;i++){
                         var row = $("#codedetaillgrid").getRowData(rowid);
                         for(var a=0;a<savecaode.length;a++){
                             if(savecaode[a]==row.code){
                                 savecaode.splice(a,1);
                             }
                         }
                     }
                 }

             },
        });
        debugger;
        var parent_column = $("#main-container");
        $("#codedetaillgrid").jqGrid( 'setGridWidth', parent_column.width()-5);
        $("#codedetaillgrid").jqGrid("setFrozenColumns");
        //$("#detailgrid").children().find("#cb_detailgrid").css("text-align", "center");


    }

    function initGrid() {
       // var id = "${business.id}";
        var serializeArray = $("#searchForm").serializeArray();
        var params = array2obj(serializeArray);
        $("#detailgrid").jqGrid({
            height:500,
            url: basePath + "/stock/inventory/detailPage.do",
            postData: params,
            datatype: "json",
            colModel: [
                {name: 'id', label: 'id', hidden: true, width: 40},
                {name: 'sku', label: 'SKU', editable: true, width: 40},
                {name: 'styleId', label: '款号', editable: true, width: 60},
                {name: 'colorId', label: '色码', editable: true, width: 40},
                {name: 'sizeId', label: '尺码', editable: true, width: 40},
                {name: 'qty', label: '数量', editable: true, width: 40},
                {name: 'styleName', label: '款名', editable: true, width: 40},
                {name: 'colorName', label: '颜色', editable: true, width: 40},
                {name: 'sizeName', label: '尺码', editable: true, width: 40},
            ],
            viewrecords: true,
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum: 20,
            rowList: [20, 50, 100],
            pager: "#detailgrid-pager",
            multiselect: false,
            shrinkToFit: true,
            sortname: 'id',
            sortorder: "asc"
           /* multiselect: true,
            onSelectRow: function (rowid,status) {
                debugger;
               var rowid=rowid;
               var status=status;
            },
            onSelectAll:function (aRowids,status) {
                debugger;
                var rowid=aRowids;
                var status=status;
            },*/
        });
        debugger;
        var parent_column = $("#main-container");
        $("#detailgrid").jqGrid( 'setGridWidth', parent_column.width()-5);
        //$("#detailgrid").children().find("#cb_detailgrid").css("text-align", "center");

    }
    function initRecordGrid() {
        var serializeArray = $("#searchForm").serializeArray();
        var params = array2obj(serializeArray);

        $("#recordgrid").jqGrid({
            height: 500,
            url: basePath + "/stock/inventory/codedetailPage.do",
            postData: params,
            datatype: "json",
            colModel: [
                {name: 'id', label: 'id', hidden: true, width: 40},
                {name:'sku',label:'SKU',editable:true,width:40},
                {name: 'cartonId', label: '箱码', editable: true, width: 40},
                {name: 'styleId', label: '款号', editable: true, width: 40},
                {name: 'colorId', label: '色码', editable: true, width: 40},
                {name: 'sizeId', label: '尺码', editable: true, width: 40},
                {name: 'sku', label: '吊牌码', editable: true, width: 40},
                {name: 'styleName', label: '款名', editable: true, width: 40},
                {name: 'colorName', label: '颜色', editable: true, width: 40},
                {name: 'sizeName', label: '尺码', editable: true, width: 40},
            ],

            viewrecords: true,
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum: 20,
            rowList: [20, 50, 100],
            pager: "#recordgrid-pager",
            multiselect: false,
            shrinkToFit: true,
            sortname: 'id',
            sortorder: "asc"
           /* multiselect: true,
            onSelectRow: function (rowid,status) {
                debugger;
                var rowid=rowid;
                var status=status;
            },
            onSelectAll:function (aRowids,status) {
                debugger;
                var rowid=aRowids;
                var status=status;
            },*/
        });
        debugger;
        var parent_column = $("#main-container");
        $("#recordgrid").jqGrid( 'setGridWidth', parent_column.width()-5);
        //$("#recordgrid").children().find("#cb_detailgrid").css("text-align", "center");
    }
    function _search() {
        var serializeArray = $("#searchForm").serializeArray();
        var params = array2obj(serializeArray);
        $("#recordgrid").jqGrid('setGridParam', {
            page: 1,
            url: basePath + "/stock/inventory/codedetailPage.do",
            postData: params
        });
        $("#recordgrid").trigger("reloadGrid");
        $("#detailgrid").jqGrid('setGridParam', {
            page: 1,
            url:  basePath + "/stock/inventory/detailPage.do",
            postData: params
        });
        $("#detailgrid").trigger("reloadGrid");
        $("#codedetaillgrid").jqGrid('setGridParam', {
            page: 1,
            url:  basePath + "/stock/inventory/codedetailPage.do",
            postData: params
        });
        $("#codedetaillgrid").trigger("reloadGrid");
    }
</script>
</body>
</html>