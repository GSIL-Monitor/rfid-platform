<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/12/4
  Time: 13:58
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
        var curOwnerId = "${ownerId}";
    </script>
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
                                    <%--<div class="btn-group btn-group-sm pull-right">--%>
                                    <%--<button type="button" class="btn btn-info btn-yellow" onclick="javascript:history.back(-1)">--%>
                                    <%--<i class="ace-icon fa fa-arrow-left"></i>--%>
                                    <%--<span class="bigger-110">返回</span>--%>
                                    <%--</button>--%>
                                    <%--</div>--%>
                                    <div class="btn-group btn-group-sm pull-right">
                                        <button type="button" class="btn btn-info" onclick="showAdvSearchPanel();">
                                            <i class="ace-icon fa fa-binoculars"></i>
                                            <span class="bigger-110">高级查询</span>
                                        </button>

                                    </div>
                                </div>
                            </div>
                            <div class="hr hr-2 hr-dotted"></div>
                            <div class="widget-main" id="searchPanel" style="display:none">
                                <form class="form-horizontal" role="form" id="searchForm">
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               for="search_warehouseId">仓库</label>
                                        <div class="col-xs-8 col-sm-8 col-md-2 col-lg-2">
                                            <select class="form-control" id="search_warehouseId"
                                                    name="filter_EQS_warehouseId">
                                            </select>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               for="search_mergeDate">单据合并日期</label>
                                        <div class="col-xs-8 col-sm-8 col-md-2 col-lg-2">
                                            <div class="input-group">
                                                <input class="form-control date-picker" id="search_mergeDate"
                                                       type="text"
                                                       name="filter_GTD_mergeDate" data-date-format="yyyy-mm-dd"/>
                                                <span class="input-group-addon">
                                                    <i class="fa fa-exchange"></i>
                                                </span>
                                                <input class="form-control date-picker" type="text"
                                                       name="filter_LED_mergeDate" data-date-format="yyyy-mm-dd"/>
                                            </div>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               for="search_inventoryDate">盘点日期</label>
                                        <div class="col-xs-8 col-sm-8 col-md-2 col-lg-2">
                                            <div class="input-group">
                                                <input class="form-control date-picker" id="search_inventoryDate"
                                                       type="text"
                                                       name="filter_GTD_inventoryDate" data-date-format="yyyy-mm-dd"/>
                                                <span class="input-group-addon">
                                                    <i class="fa fa-exchange"></i>
                                                </span>
                                                <input class="form-control date-picker" type="text"
                                                       name="filter_LED_inventoryDate" data-date-format="yyyy-mm-dd"/>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-11 btnPosition">
                                            <button type="button" class="btn btn-sm btn-primary" onclick="_search()">
                                                <i class="ace-icon fa fa-search"></i>
                                                <span class="bigger-110">查询</span>
                                            </button>
                                            <button type="reset" class="btn btn-sm btn-warning"
                                                    onclick="_clearSearch()">
                                                <i class="ace-icon fa fa-undo"></i>
                                                <span class="bigger-110">清空</span>
                                            </button>
                                        </div>
                                    </div>

                                </form>

                            </div>

                        </div>

                        <table id="grid" style="background:#ffffff"></table>

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
    <jsp:include page="../layout/footer.jsp"></jsp:include>
    <!--/.fluid-container#main-container-->
</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<script type="text/javascript">

    $(function () {
        initUrl();
        initGrid();
        initWarehouseSelect();
    });
    function refresh() {
        location.reload(true);
    }

    function initWarehouseSelect() {
        $.ajax({
            url: basePath + '/unit/list.do?filter_EQI_type=9',
            cache: false,
            async: false,
            type: 'POST',
            suceess: function (data) {
                $("#search_warehouseId").empty();
                $("#search_warehouseId").append("<option value='' style='background-color: #eeeeee'>--请选择仓库--</option>");
                var json = data;
                for (var i = 0; i < json.length; i++) {
                    $("#search_warehouseId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                    $("#search_warehouseId").trigger('chosen:updated');
                }
            }
        })
    }
    var url = "";
    function initUrl() {
        if (curOwnerId === "1" || curOwnerId === 1) {
            url = basePath + "/stock/InventoryMerge/page.do?";
        } else {
            url = basePath + "/stock/InventoryMerge/page.do?filter_EQS_ownerId=" + curOwnerId;
        }
    }

    function initGrid() {
        $("#grid").jqGrid({
            height: "auto",
            url: url,
            datatype: "json",
            colModel: [
                {name: 'id', label: '单据编号', width: 200},
                {
                    name: "", label: "查看明细", width: 100, align: "center",
                    formatter: function (cellValue, options, rowObject) {
                        var id = rowObject.id;
                        return "<a href='" + basePath + "/stock/InventoryMerge/viewMergeBillDtl.do?id=" + id + "'><i class='ace-icon fa fa-list'></i></a>";
                    }
                },
                {name: 'warehouseId', label: '盘点仓库', width: 150, hidden: true},
                {
                    name: 'warehouseName', label: '盘点仓库', width: 300,
                    formatter: function (cellValue, option, rowObject) {
                        return "[" + rowObject.warehouseId + "]" + cellValue;
                    }
                },
                {name: 'mergeBillQty', label: '合并单据数', width: 150},
                {name: 'totMergeQty', label: '预计库存', width: 150},
                {name: 'totScannedQty', label: '实际库存', width: 150},
                {name: 'mergeDate', label: '合并日期', width: 150},
                {name: 'inventoryDate', label: '盘点日期', width: 150}
            ],
            viewrecords: true,
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum: 20,
            rowList: [20, 50, 100],
            pager: "#grid-pager",
            multiselect: false,
            shrinkToFit: false,
            sortname: 'mergeDate',
            sortorder: "desc",
            autoScroll: false
        });
    }

    function _search() {
        var serializeArray = $("#searchForm").serializeArray();
        var params = array2obj(serializeArray);
        $("#grid").jqGrid('setGridParam', {
            url: url,
            page: 1,
            postData: params
        });
        $("#grid").trigger("reloadGrid");
    }

    function showAdvSearchPanel() {
        $("#searchPanel").slideToggle("fast");
    }

</script>
</body>
</html>
