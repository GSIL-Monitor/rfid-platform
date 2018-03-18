<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="multi_shop_dialog" class="modal fade bs-example-modal-lg"  tabindex="-1">
    <div class="modal-dialog" style="width: 1100px;height: 550px">
        <div class="modal-content">
            <div class="modal-header no-padding">
                <div class="table-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        <span class="white">&times;</span>
                    </button>
                    选择门店信息(多选)
                </div>
            </div>
            <div class="modal-body no-padding">
                <div class="modal-header">
                    <form class="form-horizontal" role="form" id="searchShopForm">
                        <div class="form-group">
                            <label class="col-xs-2 control-label text-right" for="filter_LIKES_code">编号</label>

                            <div class="col-xs-4">
                                <input id="filter_LIKES_code" class="form-control" name="filter_LIKES_code"
                                       type="text"
                                       placeholder="模糊查询" onkeyup="this.value=this.value.toUpperCase()"/>
                            </div>
                            <label class="col-xs-2 control-label text-right" for="filter_LIKES_Name">名称</label>

                            <div class="col-xs-4">
                                <input id="filter_LIKES_Name" class="form-control" name="filter_LIKES_Name"
                                       type="text" placeholder="模糊查询"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-5 col-sm-10">
                                <button type="button" class="btn btn-sm btn-primary" onclick="search_Multi_Shop();">
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
                        <%--
                                                <div class="col-md-2">.col-md-2</div>
                        --%>
                        <div class="col-md-6">
                            <table id="multi_shop_Grid"></table>
                            <div id="multi_shop_Page"></div>
                        </div>
                        <div class="col-md-1">
                            <div class="btn-group" role="group"  style="height: 300px">
                                <div>
                                    <button type="button" class="btn  btn-app btn-xs" onclick="selectMulti()">
                                        <i class="fa fa-2x fa-angle-double-right" aria-hidden="true"></i>
                                    </button>
                                </div>
                                <div>
                                    <button type="button" class="btn btn-app btn-xs" onclick="selsctSingle();">
                                        <i class="fa fa-2x fa-angle-right" aria-hidden="true"></i>
                                        <span></span>
                                    </button>
                                </div>
                            </div>
                            <div class="btn-group" role="group"  style="height: 300px">
                                <div>
                                    <button type="button" class="btn btn-app btn-xs" onclick="clearAllMulti();">
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
                            <table id="select_shop_Grid"></table>
                        </div>

                    </div>
                </div>
            </div>

            <div class="modal-footer no-margin-top">
                <a href="#" class="btn" onclick="close_multi_styleDilog()">关闭</a>
                <a href="#" class="btn btn-primary" onclick="selectMultiStyle()">确定</a>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
<script>
    function open_multi_shopDialog(viewTextId, callbackFun) {
        $("#multi_shop_dialog").data("viewTextId", viewTextId);
        $("#multi_shop_dialog").data("callbackFun", callbackFun);
        $('#multi_shop_dialog').modal({
            backdrop: 'static',
            keyboard: false,
            show:false
        });
        $("#multi_shop_dialog").on('show.bs.modal', function () {
            $("#multi_shop_Grid").jqGrid({
                height: '500px',
                multiselect: true,
                url: basePath + "/sys/shop/page.do?filter_EQI_type=4",
                datatype: "json",
                mtype:'POST',
                colNames: [ '编号', '名称', '所在城市'],
                colModel: [
                    {name: 'code',width:100},
                    {name: 'name',width:200},
                    {name: 'cityId',width:100}
                ],
                fitColumns: true,
                viewrecords: true,
                autowidth: true,
                rownumbers: true,
                altRows: false,
                rowNum: 15,
                rowList: [15, 10],
                pager: "#multi_shop_Page",
                shrinkToFit: false,
                autoScroll: true,
                sortname: 'code',
                sortorder: "desc"
            });
            $("#select_shop_Grid").jqGrid({
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
    
    function selsctSingle(){
 	var sids = $("#multi_shop_Grid").jqGrid("getGridParam", "selarrrow");
 	var wholePage = $("#select_shop_Grid").jqGrid("getRowData");
    	if(sids.length>0){
    		var rtf=$("#multi_shop_Grid").jqGrid('getRowData',sids[0]);
    		if(contain(rtf,wholePage)){
                $("#multi_shop_Grid").jqGrid("setSelection",sids[0]);
                return
            }
    			$("#select_shop_Grid").jqGrid("addRowData", rtf.code, rtf, "first");
    			$("#multi_shop_Grid").jqGrid("setSelection",sids[0]);
    		}
    }
    
    function selectMulti(){
        var sids = $("#multi_shop_Grid").jqGrid("getGridParam", "selarrrow");
        var wholePage = $("#select_shop_Grid").jqGrid("getRowData");
        //获得当前最大行号（数据编号）
        if(sids){
            for(var i=sids.length-1;i>=0;i--){
                var ret = $("#multi_shop_Grid").jqGrid("getRowData",sids[i]);   //获得第一行的数据
                if($("#select_shop_Grid").jqGrid('getRowData',sids[i]).code==undefined){
                    if(contain(ret,wholePage)){
                        continue;
                    }
                    //获得新添加行的行号（数据编号）
                    $("#select_shop_Grid").jqGrid("addRowData", ret.code, ret, "first");
                    $('#select_shop_Grid').jqGrid('editRow', ret.code, false);

                }
            }
        }
    }

    function contain(element,list) {
        if(list.length>0){
            for(var i=0;i<list.length;i++){
                if(list[i].styleId==element){
                    return true;
                }
            }
        }
        return false;
    }

    function clearAllMulti(){
        $("#select_shop_Grid").jqGrid('clearGridData');
    }

    function clearMulti(){
        var sids = $("#select_shop_Grid").jqGrid("getGridParam", "selarrrow");
         //获得当前最大行号（数据编号）
        if(sids){
            var len = sids.length;

            for(var i=0;i<len;i++){
                $("#select_shop_Grid").jqGrid("delRowData",sids[0]);   //获得第一行的数据
            }
        }
    }
    
/*     function selectAll(){
        var rowNum=$("#multi_shop_Grid").jqGrid("getGridParam","rowNum");
        for(var i=0;i < rowNum;i++){
        	$("#multi_shop_Grid").jqGrid("setSelection",i);
        }
        selectMulti();
        for(var i=1;i <= rowNum;i++){
        	$("#multi_shop_Grid").jqGrid("setSelection",i);
        }
        $("#select_shop_Grid").trigger("reloadGrid");
    } */
    
    function search_Multi_Shop() {
        var serializeArray = $("#searchShopForm").serializeArray();
        var params = array2obj(serializeArray);
        $("#multi_shop_Grid").jqGrid('setGridParam', {
            url: basePath + "/sys/shop/page.do?filter_EQI_type=4",
            postData: params
        });
        $("#multi_shop_Grid").trigger("reloadGrid");
    }

    function close_multi_styleDilog() {
        var callbackFun = $("#multi_shop_dialog").data("callbackFun");
        if (callbackFun != null) {
            var fn = eval(callbackFun);
            fn.call(this);
        }
        $("#multi_shop_dialog").modal('hide');
    }
    function selectMultiStyle() {
        var ids = $("#select_shop_Grid").jqGrid("getDataIDs");
        var viewTextId = $("#multi_shop_dialog").data("viewTextId");
        $(viewTextId).val(ids.toString());
        close_multi_styleDilog();
    }
</script>
