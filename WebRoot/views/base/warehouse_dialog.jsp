<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="modal-warehouse-table" class="modal fade" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header no-padding">
                <div class="table-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        <span class="white">&times;</span>
                    </button>
                    选择所属仓库
                </div>
            </div>

            <div class="modal-body no-padding">
                <div class="widget-body">

                    <div class="widget-main" id="search_unit_Panel">
                        <form class="form-horizontal" role="form" id="search_unit_Form">
                            <div class="form-group">
                                <label class="col-xs-2 control-label text-right" for="search_unit_code">编号</label>

                                <div class="col-xs-4">
                                    <input class="form-control" id="search_unit_code" name="filter_LIKES_code"
                                           type="text"
                                           placeholder="模糊查询"/>
                                </div>
                                <label class="col-xs-2 control-label text-right" for="search_unit_name">名称</label>

                                <div class="col-xs-4">
                                    <input class="form-control" id="search_unit_name" name="filter_LIKES_name"
                                           type="text" placeholder="模糊查询"/>
                                </div>


                            </div>
                            <div class="form-group">

                                <div class="col-xs-4">
                                    <button type="button" class="btn btn-sm btn-primary" onclick="_searchWarehouse()">
                                        <i class="ace-icon fa fa-search"></i>
                                        <span class="bigger-110">查询</span>
                                    </button>
                                    <button type="reset" class="btn btn-sm btn-warning">
                                    	<i class="ace-icon fa fa-undo"></i>
                                    <span class="bigger-110">清空</span></button>
                                </div>

                            </div>

                        </form>

                    </div>
                </div>
                <table id="warehouseSelect_Grid" ></table>
                <div id="warehouseSelect_Page"></div>
            </div>

            <div class="modal-footer no-margin-top">
                <a href="#" class="btn" onclick="closeWarehouseSelectDialog()">关闭</a>
                <a href="#" class="btn btn-primary" onclick="selectedWarehouse()">确定</a>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
<script>


    function _searchWarehouse() {
        $("#warehouseSelect_Grid").jqGrid('setGridParam', {
            url:basePath+"/sys/warehouse/page.do",
            page : 1,
            postData : $("#search_unit_Form").serialize()
        });
        $("#warehouseSelect_Grid").trigger("reloadGrid");
    }
    function openWarehouseDialog(viewId,viewTextId) {
        var ownerId=document.getElementById("form_ownerId").value;
        if(ownerId==""){
            bootbox.alert("请选择所属仓库");
        }else{
            $("#modal-warehouse-table").data("viewId",viewId); 
            $("#modal-warehouse-table").on('show.bs.modal', function () {
                var jqUrl = basePath+"/sys/warehouse/page.do?filter_EQS_ownerId="+ownerId;
                $("#warehouseSelect_Grid").jqGrid({
                    height: 300,
                    url:jqUrl,
                    datatype: "json",
                    colModel: [
                        {name: 'id', label: 'id',hidden:true, width: 40},
                        {name: 'code', label: '编号',editable:true, width: 20},
                        {name: 'name', label: '名称', editable:true,width: 40},
                        {name: 'groupId', label: '分类', editable:true,width: 20},
                        {name: 'createTime', label: '创建时间', width: 30}
                    ],
                    viewrecords: true,
                    autowidth: true,
                    rownumbers: true,
                    altRows: true,
                    rowNum: 50,
                    rowList: [20, 50],
                    pager: "#warehouseSelect_Page",
                    multiselect: false,
                    shrinkToFit: true,
                    sortname : 'createTime',
                    sortorder : "desc",
                    ondblClickRow:function(rowid) {
                        var row = $("#warehouseSelect_Grid").jqGrid("getRowData",rowid);
                        $(viewId).val(row.code);
                        $(viewTextId).val(row.name);
                        $("#modal-warehouse-table").modal('hide')
                    }
                });
                var parent_column = $("#warehouseSelect_Grid").closest('.modal-dialog');
                $("#warehouseSelect_Grid").jqGrid( 'setGridWidth', parent_column.width()-2);

            }).modal("show");
            var params = $({});
            params.attr("filter_EQS_ownerId", ownerId);
            $("#warehouseSelect_Grid").jqGrid("setGridParam",{
                url: jqUrl,
                page : 1
            }).trigger('reloadGrid');
        }

    }
    function closeWarehouseSelectDialog() {
        $("#modal-warehouse-table").modal('hide');
    }
    function selectedWarehouse() {
        var rowId = $("#warehouseSelect_Grid").jqGrid("getGridParam", "selrow");
        var row = $("#warehouseSelect_Grid").jqGrid('getRowData',rowId);
        var viewId = $("#modal-warehouse-table").data("viewId");
        $(viewId).val(row.code);
        $("#modal-warehouse-table").modal('hide');
    }
</script>
