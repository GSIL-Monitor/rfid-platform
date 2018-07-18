<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
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
                            <a   class="btn btn-xs bigger btn-yellow"  href="<%=basePath%>/${mainUrl}">
                                <i class="ace-icon fa fa-arrow-left"></i> 返回
                            </a>
                        </div>
                    </div>
                    <div class="widget-body">
                        <div class="widget-main padding-12">
                            <form class="form-horizontal" role="form">
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="search_billNo">单号</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_billNo" name="filter_LIKES_billNo"
                                               type="text" disabled value='${bill.billNo}' />
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_token">类型</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_token" name="filter_LIKES_token"
                                               type="text" disabled value='${bill.billType}' />
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_totEpc">单据数量</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_totEpc" name="filter_LIKES_totEpc"
                                               type="text" disabled value='${bill.totQty}' />
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_totCarton">实际数量</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_totCarton" name="filter_LIKES_totCarton"
                                               type="text" disabled value='${bill.actQty}' />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="search_srcBillNo">原始单号</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_srcBillNo" name="filter_LIKES_srcBillNo"
                                               type="text" disabled value='${bill.srcBillNo}' />
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_unitName">发货方</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_unitName" name="filter_LIKES_origId"
                                               type="text" disabled value='${bill.origUnitName}' />
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_destUnitName">收货方</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_destUnitName" name="filter_LIKES_destId"
                                               type="text" disabled value='${bill.destUnitName}' />
                                    </div>
                                     <label class="col-xs-1 control-label" for="search_billDate">单据日期</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_billDate" name="filter_LIKES_billDate"
                                               type="text" disabled value='${bill.billDate}' />
                                    </div>
                                </div>

                                <div class="form-group">

                                    <label class="col-xs-1 control-label" for="search_id">任务号</label>

                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_id" name="filter_LIKES_taskId"
                                               type="text" disabled value='${bill.taskId}' />
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
                <h4 class="widget-title lighter">单据明细</h4>
                <div class="widget-toolbar no-border">
                    <ul class="nav nav-tabs" id="myTab">
                        <li class="active">
                            <a data-toggle="tab" href="#detail">SKU明细</a>
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
    });
    function initGrid() {
        var id = "${bill.id}";
        $("#detailgrid").jqGrid({
            height: 500,
            url: basePath + "/syn/bill/findDetail.do?billId=" + id,
            datatype: "json",
            colModel: [
                {name: 'id', label: 'id', hidden: true, width: 40},
                {name: 'styleId', label: '款号', editable: true, width: 60},
                {name: 'colorId', label: '色码', editable: true, width: 40},
                {name: 'sizeId', label: '尺码', editable: true, width: 40},
                {name: 'qty', label: '单据数量', editable: true, width: 40},
                {name: 'actQty', label: '实际数量', editable: true, width: 40},
                {name: 'sku', label: 'SKU', editable: true, width: 40},
                {name: 'styleName', label: '款名', editable: true, width: 40},
                {name: 'colorName', label: '颜色', editable: true, width: 40},
                {name: 'sizeName', label: '尺码', editable: true, width: 40},
            ],
            viewrecords: true,
            autowidth: true,
            rownumbers: true,
            altRows: true,
            multiselect: false,
            rowNum:-1,
            shrinkToFit: true,
            sortname: 'id',
            sortorder: "asc"
        });
        var parent_column = $("#main-container");
        $("#detailgrid").jqGrid( 'setGridWidth', parent_column.width()-5);

    }
</script>
</body>
</html>