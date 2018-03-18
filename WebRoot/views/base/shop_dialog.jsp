<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="modal-shop-table" class="modal fade" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header no-padding">
                <div class="table-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        <span class="white">&times;</span>
                    </button>
                    选择门店
                </div>
            </div>

            <div class="modal-body no-padding">
                <div class="widget-body">

                    <div class="widget-main" id="search_shop_Panel">
                        <form class="form-horizontal" role="form" id="search_shop_Form">
                            <div class="form-group">
                                <label class="col-xs-2 control-label text-right" for="search_shop_code">编号</label>

                                <div class="col-xs-4">
                                    <input class="form-control" id="search_shop_code" name="filter_LIKES_code"
                                           type="text" onkeyup="this.value=this.value.toUpperCase()"
                                           placeholder="模糊查询"/>
                                </div>
                                <label class="col-xs-2 control-label text-right" for="search_shop_name">名称</label>

                                <div class="col-xs-4">
                                    <input class="form-control" id="search_shop_name" name="filter_LIKES_name"
                                           type="text" placeholder="模糊查询"/>
                                </div>


                            </div>
                            <div class="form-group">
                                <div class="col-xs-10 col-xs-offset-5">
                                    <button type="button" class="btn btn-sm btn-primary" onclick="_searchShop()">
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
                <table id="shopSelect_Grid" ></table>
                <div id="shopSelect_Page"></div>
            </div>

            <div class="modal-footer no-margin-top">
                <a href="#" class="btn" onclick="closeShopSelectDialog()">关闭</a>
                <a href="#" class="btn btn-primary" onclick="selectedShop()">确定</a>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
<script>
function initSearchShopType() {
}
    function _searchShop(searchShopUrl) {
        var serializeArray = $("#search_shop_Form").serializeArray();
        var params = array2obj(serializeArray);
        $("#shopSelect_Grid").jqGrid('setGridParam', {
        	url:searchShopUrl,
            page : 1,
            postData : params
        }).trigger("reloadGrid");
    }
    function openShopSelectDialog(viewId,viewTextId,ownerId) {
        initSearchShopType();
      //  if(cs.isBlank(ownerId)){
      //      bootbox.alert("请选择所属方");
       // }else{
            $("#modal-shop-table").data("viewId",viewId);
            $("#modal-shop-table").data("viewTextId",viewTextId);
            var Url = basePath+"/sys/shop/page.do?filter_EQI_type="+constant.unitType.Shop+"&filter_EQS_ownerId=";
            var searchShopUrl;
            if(ownerId!=''){
            	searchShopUrl =Url + ownerId;
            } else {
            	searchShopUrl=Url;
            }
            $("#modal-shop-table").on('show.bs.modal', function () {
                $shopSelect_Grid = $("#shopSelect_Grid").jqGrid({
                    height: 300,
                    url:searchShopUrl,
                    mtype:"POST",
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
                    rowNum: 20,
                    rowList: [20, 50],
                    pager: "#shopSelect_Page",
                    multiselect: false,
                    shrinkToFit: true,
                    sortname : 'createTime',
                    sortorder : "desc",
                    ondblClickRow:function(rowid) {
                        var row = $("#shopSelect_Grid").jqGrid("getRowData",rowid);
                        var viewId = $("#modal-shop-table").data("viewId");
                        var viewTextId = $("#modal-shop-table").data("viewTextId");
                        $(viewId).val(row.code);
                        $(viewTextId).val(row.name);
                        $("#modal-shop-table").modal('hide').on('hidden.bs.modal',function () {
                            $("#modal-shop-table").removeData("viewId");
                            $("#modal-shop-table").removeData("viewTextId");
                        });
                    }
                });
                var parent_column = $("#shopSelect_Grid").closest('.modal-dialog');
                $("#shopSelect_Grid").jqGrid( 'setGridWidth', parent_column.width()-2);
                $("#modal-shop-table").trigger("reload");
            }).modal("show");
            _searchShop(searchShopUrl);
      //  }
    }
    function closeShopSelectDialog() {
        $("#modal-shop-table").modal('hide');
    }
    function selectedShop() {
        var rowId = $("#shopSelect_Grid").jqGrid("getGridParam", "selrow");
        var row = $("#shopSelect_Grid").jqGrid('getRowData',rowId);
        var viewId = $("#modal-shop-table").data("viewId");
        var viewTextId = $("#modal-shop-table").data("viewTextId");
        $(viewId).val(row.code);
        $(viewTextId).val(row.name);
        $("#modal-shop-table").modal('hide');
    }
</script>
