<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/10/30
  Time: 11:18
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
        var defaultWarehId ="${defaultWarehId}"
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
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-info" onclick="billMerge()">
                                            <i class="ace-icon fa fa-gg"></i>
                                            <span class="bigger-110">单据合并</span>
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
                            <div class="hr hr-2 hr-dotted"></div>

                            <div class="widget-main" id="searchPanel" style="display:none">
                                <form class="form-horizontal" role="form" id="searchForm">
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               for="search_id">单据编号</label>
                                        <div class="col-xs-8 col-sm-8 col-md-2 col-lg-2">
                                            <input class="form-control" id="search_id" name="filter_LIKES_id"
                                                   type="text" placeholder="模糊查询"/>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               for="search_warehouseId">仓库</label>
                                        <div class="col-xs-8 col-sm-8 col-md-2 col-lg-2">
                                            <select class="form-control selectpicker show-tick" id="search_warehouseId"
                                                    name="filter_EQS_origId" data-live-search="true">
                                            </select>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               for="search_billDate">单据日期</label>
                                        <div class="col-xs-8 col-sm-8 col-md-2 col-lg-2">
                                            <div class="input-group">
                                                <input class="form-control date-picker" id="search_billDate" type="text"
                                                       name="filter_GTD_billDate" data-date-format="yyyy-mm-dd"/>
                                                <span class="input-group-addon">
                                                    <i class="fa fa-exchange"></i>
                                                </span>
                                                <input class="form-control date-picker" type="text"
                                                       name="filter_LED_billDate" data-date-format="yyyy-mm-dd"/>
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
                                                    onclick="_reset()">
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
            type: "POST",
            success: function (data, textStatus) {
                $("#search_warehouseId").empty();
                $("#search_warehouseId").append("<option value=''>--请选择仓库--</option>");
                var json = data;
                for (var i = 0; i < json.length; i++) {
                    $("#search_warehouseId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                }
            }
        });
        $('.selectpicker').selectpicker('refresh');
    }

    var url;
    function initUrl() {
        if (curOwnerId === "1" || curOwnerId === 1) {
            url = basePath + "/stock/erpBill/page.do?filter_INI_type=9"
        } else {
            url = basePath + "/stock/erpBill/page.do?filter_INI_type=9&filter_EQS_destId=" + defaultWarehId;
        }
    }

    function initGrid() {
        $("#grid").jqGrid({
            height: "auto",
            url: url,
            datatype: "json",
            colModel: [
                {name: 'id', label: '单据编号', width: 200},
                {name: 'taskId', label: '任务编号', width: 300},
                {
                    name: "", label: "查看明细", width: 100, align: "center",
                    formatter: function (cellValue, options, rowObject) {
                        var id = rowObject.id;
                        return "<a href='" + basePath + "/stock/erpBill/detail.do?id=" + id + "'><i class='ace-icon fa fa-list'></i></a>";
                    }
                },
                {name: 'origId', label: '盘点单位', width: 150, hidden: true},
                {
                    name: 'origName', label: '盘点单位', width: 300,
                    formatter: function (cellValue, option, rowObject) {
                        return "[" + rowObject.origId + "]" + cellValue;
                    }
                },
                {name: 'totQty', label: '预计单品数', width: 150},
                {name: 'skuQty', label: '预计SKU数', width: 150, hidden: true},
                {name: 'actQty', label: '实际单品数', width: 150},
                {name: 'actSkuQty', label: '实际SKU数', width: 150, hidden: true},
                {name: 'billDate', label: '单据日期', width: 150},
                {name: 'remark', label: '备注', width: 300}
            ],
            viewrecords: true,
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum: 20,
            rowList: [20, 50, 100],
            pager: "#grid-pager",
            multiselect: true,
            shrinkToFit: false,
            sortname: 'billDate',
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

    function billMerge() {
        var ids = $('#grid').jqGrid('getGridParam', 'selarrrow');
        console.log(ids);
        var warehouse;
        var couldMerge = true;
        var billArray = [];
        $.each(ids, function (index, value) {
            var rowData = $('#grid').getRowData(value);
            if (warehouse === undefined) {
                warehouse = rowData.origId;
                billArray.push(rowData);
            } else {
                if (warehouse !== rowData.origId) {
                    bootbox.alert('所有合并单据的仓库必须相同');
                    couldMerge = false;
                    return false;
                }
                billArray.push(rowData);
            }
        });
        if (couldMerge) {
            if (billArray.length < 2) {
                bootbox.alert('您必须合并至少两个及以上的单据');
                return;
            }
            console.log(billArray);
            $.ajax({
                dataType: 'json',
                type: 'POST',
                url: basePath + '/stock/InventoryMerge/mergeBill.do',
                data: {
                    billListStr: JSON.stringify(billArray)
                },
                success: function (data) {
                    bootbox.alert(data.msg);
                }
            })

        }
    }

</script>
</body>
</html>
