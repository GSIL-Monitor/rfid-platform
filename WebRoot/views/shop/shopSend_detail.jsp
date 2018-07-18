<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
        var shopId = "${shopId}";
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
                         <button class="btn btn-xs bigger btn-yellow dropdown-toggle"
                                            onclick="history.back(-1);">
                             <i class="ace-icon fa fa-arrow-left"></i> 返回
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
                                               type="text" disabled value='${business.deviceId}' />
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_token">出库方式</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_token" name="filter_LIKES_token"
                                               type="text" disabled value='<c:choose><c:when test="${business.token==15}">销售出库</c:when><c:when test="${business.token==18}">调拨出库</c:when><c:when test="${business.token==27}">退仓出库</c:when></c:choose>'/>
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_totEpc">总数量</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_totEpc" name="filter_LIKES_totEpc"
                                               type="text" disabled value='${business.totEpc}' />
                                    </div>


                                </div>
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="search_totStyle">款数</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_totStyle" name="filter_LIKES_totStyle"
                                               type="text" disabled value='${business.totStyle}' />
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_totSku">SKU数</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_totSku" name="filter_LIKES_totSku"
                                               type="text" disabled value='${business.totSku}' />
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_origUnitId">收货方</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_origUnitId" name="filter_LIKES_origUnitId"
                                               type="text" disabled value='[${business.origUnitId}]${business.origUnitName}' />
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_beginTime">开始时间</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_beginTime" name="filter_LIKES_beginTime"
                                               type="text" disabled value='<fmt:formatDate value="${business.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>'/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="search_endTime">结束时间</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_endTime" name="filter_LIKES_endTime"
                                               type="text" disabled value='<fmt:formatDate value="${business.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' />
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_billNo">ERP单号</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_billNo" name="filter_LIKES_billNo"
                                               type="text" disabled value='${business.billNo}' />
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_id">任务号</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_id" name="filter_LIKES_id"
                                               type="text" disabled value='${business.id}' />
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
                        <li class="active">
                            <a data-toggle="tab" href="#detail">SKU明细</a>
                        </li>
                        <li>
                            <a data-toggle="tab" href="#record">装箱明细</a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="widget-body">
                <div class="widget-main padding-12 no-padding-left no-padding-right">
                    <div class="tab-content padding-4">
                        <div id="detail" class="tab-pane in active">
                            <table id="detailgrid"></table>
                        </div>
                        <div id="record" class="tab-pane">
                            <table id="recordgrid"></table>
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
        initRecordGrid();
    });
    function initGrid() {
        var id = "${business.id}";
        $("#detailgrid").jqGrid({
            height: 500,
            url: basePath + "/shop/shopReceive/detailPage.do?id=" + id,
            datatype: "json",
            colModel: [
                {name: 'id', label: 'id', hidden: true, width: 40},
                {name: 'styleId', label: '款号', editable: true, width: 60},
                {name: 'colorId', label: '色码', editable: true, width: 40},
                {name: 'sizeId', label: '尺码', editable: true, width: 40},
                {name: 'qty', label: '数量', editable: true, width: 40},
                {name: 'sku', label: 'SKU', editable: true, width: 40},
                {name: 'styleName', label: '款名', editable: true, width: 40},
                {name: 'colorName', label: '颜色', editable: true, width: 40},
                {name: 'sizeName', label: '尺码', editable: true, width: 40},
            ],

            viewrecords: true,
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum:-1,
            multiselect: false,
            shrinkToFit: true,
            sortname: 'id',
            sortorder: "asc"
        });
        var parent_column = $("#main-container");
        $("#detailgrid").jqGrid( 'setGridWidth', parent_column.width()-5);

    }
    function initRecordGrid() {

        var id = "${business.id}";
        $("#recordgrid").jqGrid({
            height: 500,
            url: basePath + "/shop/shopReceive/recordPage.do?id=" + id,
            datatype: "json",
            colModel: [
                {name: 'id', label: 'id', hidden: true, width: 40},
                {name: 'cartonId', label: '箱码', editable: true, width: 40},

                {name: 'styleId', label: '款号', editable: true, width: 40},
                {name: 'colorId', label: '色码', editable: true, width: 40},
                {name: 'sizeId', label: '尺码', editable: true, width: 40},
                {name: 'code', label: '吊牌码', editable: true, width: 40},
                {name: 'styleName', label: '款名', editable: true, width: 40},
                {name: 'colorName', label: '颜色', editable: true, width: 40},
                {name: 'sizeName', label: '尺码', editable: true, width: 40},
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

        var parent_column = $("#main-container");
        $("#recordgrid").jqGrid( 'setGridWidth', parent_column.width()-5);
    }
</script>
</body>
</html>