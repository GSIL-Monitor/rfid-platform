<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
                            <button class="btn btn-xs bigger btn-yellow dropdown-toggle" onclick="javascript:history.back(-1);">
                                <i class="ace-icon fa fa-arrow-left"></i>
                                返回
                            </button>
                        </div>
                    </div>

                    <div class="widget-body">
                        <div class="widget-main padding-12">

                            <form class="form-horizontal" role="form" >
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="search_billNo">批次号</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_billNo" name="filter_LIKES_billNo"
                                               type="text"disabled value='${init.billNo}' />
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_billDate">导入时间</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_billDate" name="filter_LIKES_billDate"
                                               type="text"disabled value='<fmt:formatDate value="${init.billDate}" pattern="yyyy-MM-dd HH:mm:ss"/>' />
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_fileName">导入文件名</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_fileName" name="filter_LIKES_fileName"
                                               type="text" disabled value='${init.fileName}' />
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_totSku">SKU数</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_totSku" name="filter_LIKES_totSku"
                                               type="text" disabled value='${init.totSku}' />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="search_totEpc">总数量</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_totEpc" name="filter_LIKES_totEpc"
                                               type="text" disabled value='${init.totEpc}' />
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_unit2Id">收货方</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_unit2Id" name="filter_LIKES_unit2Id"
                                               type="text" disabled value='${init.destId}' />
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_detectTotQty">已检测数量</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_detectTotQty" name="filter_LIKES_detectTotQty"
                                               type="text" disabled value='${init.detectTotQty}' />
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_deliverNo">快递单号</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_deliverNo" name="filter_LIKES_deliverNo"
                                               type="text" disabled value='${init.deliverNo}' />
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
        <h4 class="widget-title lighter">任务明细</h4>
        <div class="widget-toolbar no-border">
            <ul class="nav nav-tabs" id="myTab">
                <li class="active">
                    <a data-toggle="tab" href="#intdtl"> 标签明细</a>
                </li>
                <li>
                    <a data-toggle="tab" href="#epcs"> 唯一码明细</a>
                </li>
            </ul>
        </div>
    </div>
    <div class="widget-body">
        <div class="widget-main padding-12 no-padding-left no-padding-right">
            <div class="tab-content padding-4">
                <div id="intdtl"class="tab-pane in active">
                    <table id="detailgrid"></table>
                </div>
                <div id="epcs" class="tab-pane">
                    <table id="epcgrid"></table>
                </div>
            </div>
        </div>
    </div>
</div>
<%--<div class="row">--%>
    <%--<div class="col-sm-12">--%>
        <%--<table id="detailgrid"></table>--%>
    <%--</div>--%>
    <%--<!--  <div class="col-sm-4">--%>
          <%--<table id="epcgrid"></table>--%>
      <%--</div>--%>
      <%---->--%>
<%--</div>--%>
</div>
<jsp:include page="../layout/footer.jsp"></jsp:include>
</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<script type="text/javascript">
    $(function () {
        initGrid();
        initEpcGrid();
    });
    function initGrid(){
        var billNo="${init.billNo}";
        $("#detailgrid").jqGrid({
            height: 500,
            url: basePath + "/tag/birth/detailPage.do?billNo="+billNo,
            datatype: "json",
            colModel: [
                {name: 'id', label: 'id', hidden: true, width: 40},
//                {name:"unicode",label:'唯一码',width:40},
                {name: 'styleId', label: '款号', editable: true, width: 60},
                {name: 'colorId', label: '色码', editable: true, width: 40},
                {name: 'sizeId', label: '尺码', editable: true, width: 40},
                {name: 'qty', label: '数量', editable: true, width: 40},
                //{name: '', label: '吊牌码', editable: true, width: 40},
                {name: 'styleName', label: '款名', editable: true, width: 40},
                {name: 'colorName', label: '颜色', editable: true, width: 40},
                {name: 'sizeName', label: '尺码', editable: true, width: 40},
                {name: 'detectQty', label: '检测数量', editable: true, width: 40},
                {name: 'receiveQty', label: '接收数量', editable: true, width: 40},
            ],

            viewrecords: true,
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum:-1,
            multiselect: false,
            shrinkToFit: true,
            sortname: 'styleId',
            sortorder: "asc",
            onSelectRow:function(rowid){
                var selectdata=$("#detailgrid").getRowData(rowid);
                console.log(selectdata);
            }
        });
        var parent_column = $("#main-container");
        $("#detailgrid").jqGrid( 'setGridWidth', parent_column.width()-5);
    }
    function initEpcGrid(){
        var billNo="${init.billNo}";
        $("#epcgrid").jqGrid({
            height: 500,
            url:basePath+"/tag/birth/detailEpcPage.do?billNo="+billNo,
            datatype: "json",
            mtype:"POST",
            colModel: [
                {name: 'id', label: 'id', hidden: true, width: 240},
                {name:'code',label:'唯一码',eidtable:true,width:160},
                {name:'styleId', label: '款号', editable: true, width: 160},
                {name:'styleName',label:'款名',editable:true,width:240},
                {name:'colorId',label:'色号',editable:true,width:200},
                {name:'colorName',label:'颜色',editable:true,width:200},
                {name:'sizeId',label:'尺寸',editable:true,width:200},
                {name:'sizeName',label:'尺码',editable:true,width:240}
            ],
            viewrecords: true,
            autowidth: false,
            rownumbers: true,
            altRows: true,
            rowNum:-1,
            multiselect: false,
            shrinkToFit: true,
            sortname: 'styleId',
            sortorder: "asc",
        });
        var parent_column = $("#main-container");
        $("#epcgrid").jqGrid( 'setGridWidth', parent_column.width()-5);
    }
</script>
</body>
</html>