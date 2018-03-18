<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="multi_warehouse_dialog" class="modal fade bs-example-modal-lg"  tabindex="-1">
    <div class="modal-dialog" style="width: 1100px;height: 550px">
        <div class="modal-content">
            <div class="modal-header no-padding">
                <div class="table-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        <span class="white">&times;</span>
                    </button>
                    选择仓库信息(多选)
                </div>
            </div>
            <div class="modal-body no-padding">
                <div class="modal-header">
                    <form class="form-horizontal" role="form" id="searchWarehouseForm">
                        <div class="form-group">
                            <label class="col-xs-2 control-label text-right" for="filter_LIKES_code">编号</label>

                            <div class="col-xs-4">
                                <input id="filter_LIKES_code" class="form-control" name="filter_LIKES_code"
                                       type="text"
                                       placeholder="模糊查询" onkeyup="this.value=this.value.toUpperCase()"/>
                            </div>
                            <label class="col-xs-2 control-label text-right" for="filter_LIKES_name">名称</label>

                            <div class="col-xs-4">
                                <input id="filter_LIKES_name" class="form-control" name="filter_LIKES_name"
                                       type="text" placeholder="模糊查询"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-xs-10 col-xs-offset-5">
                                <button type="button" class="btn btn-sm btn-primary" onclick="search_Multi_Warehouse();">
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
                <div class="container">
                    <div class="row">

                        <div class="col-md-6">
                            <table id="multi_warehouse_Grid"></table>
                            <div id="multi_warehouse_Page"></div>
                        </div>
                        <div class="col-md-1">
                            <div class="btn-group" role="group"  style="height: 300px">
                                <div>
                                    <button type="button" class="btn  btn-app btn-xs" onclick="selectMulti()">
                                        <i class="fa fa-2x fa-angle-double-right" aria-hidden="true"></i>
                                    </button>
                                </div>
                                <div>
                                    <button type="button" class="btn btn-app btn-xs" onclick="selectSingle()">
                                        <i class="fa fa-2x fa-angle-right" aria-hidden="true"></i>
                                        <span></span>
                                    </button>
                                </div>
                            </div>
                            <div class="btn-group" role="group"  style="height: 300px">
                                <div>
                                    <button type="button" class="btn btn-app btn-xs" onclick="clearAllMulti()">
                                        <i class="fa fa-2x fa-angle-double-left" aria-hidden="true"></i>
                                    </button>
                                </div>
                                <div>
                                    <button type="button" class="btn btn-app btn-xs" onclick="clearMulti();">
                                        <i class="fa fa-2x fa-angle-left" aria-hidden="true"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-5">
                            <table id="select_warehouse_Grid"></table>
                        </div>

                    </div>
                </div>
            </div>

            <div class="modal-footer no-margin-top">
                <a href="#" class="btn" onclick="close_multi_warehouseDilog()">关闭</a>
                <a href="#" class="btn btn-primary" onclick="selectMultiWarehouse()">确定</a>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
<script>
    function open_multi_warehouseDialog(viewTextId, callbackFun) {
        $("#multi_warehouse_dialog").data("viewTextId", viewTextId);
        $("#multi_warehouse_dialog").data("callbackFun", callbackFun);
        $('#multi_warehouse_dialog').modal({
            backdrop: 'static',
            keyboard: false,
            show:false
        });
        $("#multi_warehouse_dialog").on('show.bs.modal', function () {
            $("#multi_warehouse_Grid").jqGrid({
                height: '500px',
                mtype:"POST",
                multiselect: true,
                url: basePath + "/sys/warehouse/page.do?filter_EQI_type=9",
                datatype: "json",
                colNames: [ '编号', '名称'],
                colModel: [
                    {name: 'code',width:100},
                    {name: 'name',width:200}
                ],
                fitColumns: true,
                viewrecords: true,
                autowidth: true,
                rownumbers: true,
                altRows: false,
                rowNum: 15,
                rowList: [15, 10],
                pager: "#multi_warehouse_Page",
                shrinkToFit: false,
                autoScroll: true,
                sortname: 'code',
                sortorder: "desc"
            });
            $("#select_warehouse_Grid").jqGrid({
                height: '550px',
                multiselect: true,
                datatype: "json",
                colNames: [ '编号', '名称'],
                colModel: [
                    {name: 'code',width:100},
                    {name: 'name',width:100}

                ],
                viewrecords: true,
                autowidth: true,
                rownumbers: true,
                altRows: true,
                autoScroll: true,
                rowNum:0,
                shrinkToFit: true
            });
        }).modal("show");

    }
     function selectSingle(){
    	var sids = $("#multi_warehouse_Grid").jqGrid("getGridParam", "selarrrow");
    	
    	if(sids.length>0){
    		var rtf=$("#multi_warehouse_Grid").jqGrid('getRowData',sids[0]);
    			$("#select_warehouse_Grid").jqGrid("addRowData", rtf.code, rtf, "first");
  				$("#multi_warehouse_Grid").jqGrid("setSelection",sids[0]);
    		}
    }
    function selectMulti(){
        var sids = $("#multi_warehouse_Grid").jqGrid("getGridParam", "selarrrow");
        /*jqGrid获取一整页数据 
    	var rtf = $("#multi_warehouse_Grid").jqGrid("getRowData");
    	$("#select_warehouse_Grid").jqGrid("addRowData",rtf.code,rtf,"last"); */
    	
        //获得当前最大行号（数据编号）
        if(sids){
            for(var i=sids.length-1;i>=0;i--){
                var ret = $("#multi_warehouse_Grid").jqGrid("getRowData",sids[i]);   //获得第一行的数据
                if($("#select_warehouse_Grid").jqGrid('getRowData',sids[i]).code==undefined){

                    //获得新添加行的行号（数据编号）
                    $("#select_warehouse_Grid").jqGrid("addRowData", ret.code, ret, "first");
                    $('#select_warehouse_Grid').jqGrid('editRow', ret.code, false);

                }
            }
        }
    }
    function clearAllMulti(){
        $("#select_warehouse_Grid").jqGrid('clearGridData');
    }

    function clearMulti(){
        var sids = $("#select_warehouse_Grid").jqGrid("getGridParam", "selarrrow");
         //获得当前最大行号（数据编号）
        if(sids){
            var len = sids.length;

            for(var i=0;i<len;i++){
                $("#select_warehouse_Grid").jqGrid("delRowData",sids[0]);   //获得第一行的数据
            }
        }
       
    }


    
    function search_Multi_Warehouse() {
        var serializeArray = $("#searchWarehouseForm").serializeArray();
        var params = array2obj(serializeArray);
        $("#multi_warehouse_Grid").jqGrid('setGridParam', {
            url: basePath + "/sys/warehouse/list.do?filter_EQI_type=9",
            postData: params
        });
        $("#multi_warehouse_Grid").trigger("reloadGrid");
    }

    function close_multi_warehouseDilog() {
        var callbackFun = $("#multi_warehouse_dialog").data("callbackFun");
        if (callbackFun != null) {
            var fn = eval(callbackFun);
            fn.call(this);
        }
        $("#multi_warehouse_dialog").modal('hide');
    }
    function selectMultiWarehouse() {
        var ids = $("#select_warehouse_Grid").jqGrid("getDataIDs");
        var viewTextId = $("#multi_warehouse_dialog").data("viewTextId");
        $(viewTextId).val(ids.toString());
        close_multi_warehouseDilog();
    }
</script>
