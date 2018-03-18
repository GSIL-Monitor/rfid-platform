<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>
    <style>
        #searchbtn:hover {
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
                            <button class="btn btn-xs bigger btn-yellow dropdown-toggle"
                                    onclick="javascript:history.back(-1);">
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
                                        <input class="form-control" id="search_deviceId" name="form_deviceId"
                                               type="text" disabled value="${business.deviceId}"/>
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_token">出库方式</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_token" name="form_token"
                                               type="text" disabled
                                               value='<c:choose><c:when test="${business.token==26}">退货出库</c:when><c:when test="${business.token==10}">普通出库</c:when><c:when test="${business.token==24}">调拨出库</c:when></c:choose>'/>
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_totEpc">总数量</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_totEpc" name="form_totEpc"
                                               type="text" disabled value="${business.totEpc}"/>
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_totCarton">箱数</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_totCarton" name="form_totCarton"
                                               type="text" disabled value="${business.totCarton}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="search_origName">出库仓库</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_origName" name="form_origName"
                                               type="text" disabled value="[${business.origId}]${business.origName}"/>
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_destName">收货方</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_destName" name="form_destName"
                                               type="text" disabled
                                               value="[${business.destUnitId}]${business.destUnitName}"/>

                                    </div>
                                    <label class="col-xs-1 control-label" for="search_totSku">收货仓库</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_totSku" name="form_totSku"
                                               type="text" disabled value="[${business.destId}]${business.destName}"/>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_beginTime">开始时间</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_beginTime" name="form_beginTime"
                                               type="text" disabled
                                               value='<fmt:formatDate value="${business.beginTime}" pattern="yyyy-MM-dd HH:mm:ss" />'/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="search_endTime">结束时间</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_endTime" name="form_endTime"
                                               type="text" disabled
                                               value='<fmt:formatDate value="${business.endTime}" pattern="yyyy-MM-dd HH:mm:ss" />'/>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_billNo">ERP单号</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_billNo" name="form_billNo"
                                               type="text" disabled value="${business.billNo}"/>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_id">任务号</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_id" name="form_id"
                                               type="text" disabled value="${business.id}"/>
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
                        <div id="record" class="tab-pane">
                            <table id="recordgrid"></table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>
</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<script type="text/javascript">
    $(function () {
        initGrid();
        initRecordGrid();
    });

    function showSearchForm() {
        $("#searchForm").slideToggle("fast");
    }

    function initGrid() {
        var id = "${business.id}";
        $("#detailgrid").jqGrid({
            height: 500,
            url: basePath + "/task/outbound/detailPage.do?id=" + id,
            datatype: "json",
            mtype:"POST",
            colModel: [
                {name: 'id', label: 'id', hidden: true, width: 40},
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
            rowNum: -1,
            multiselect: false,
            shrinkToFit: true,
            sortname: 'id',
            sortorder: "asc"
        });
        var parent_column = $("#main-container");
        $("#detailgrid").jqGrid('setGridWidth', parent_column.width() - 5);

    }
    function initRecordGrid() {

        var id = "${business.id}";
        $("#recordgrid").jqGrid({
            height: 500,
            url: basePath + "/task/outbound/recordPage.do?id=" + id,
            datatype: "json",
            mtype:"POST",
            colModel: [
                {name: 'id', label: 'id', hidden: true, width: 40},
                {name: 'sku', label: 'SKU', editable: true, width: 40},
                {name: 'code', label: '吊牌码', editable: true, width: 40},
                {name: 'cartonId', label: '箱码', editable: true, width: 40},
                {name: 'styleId', label: '款号', editable: true, width: 40},
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
            rowNum: -1,
            multiselect: false,
            shrinkToFit: true,
            sortname: 'id',
            sortorder: "asc"
        });

        var parent_column = $("#main-container");
        $("#recordgrid").jqGrid('setGridWidth', parent_column.width() - 5);
    }
</script>
</body>
</html>