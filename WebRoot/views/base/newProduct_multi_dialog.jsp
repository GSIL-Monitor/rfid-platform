<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="multi_newProduct_dialog" class="modal fade bs-example-modal-lg"  tabindex="-1">
    <div class="modal-dialog" style="width: 1100px;height: 550px">
        <div class="modal-content">
            <div class="modal-header no-padding">
                <div class="table-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        <span class="white">&times;</span>
                    </button>
                    选择新品信息(多选)
                </div>
            </div>
            <div class="modal-body no-padding">
                <div class="modal-header">
                    <form class="form-horizontal" role="form" id="searchStyleForm">
                        <div class="form-group">
                            <label class="col-xs-2 control-label text-right" for="filter_LIKES_styleId">款号</label>

                            <div class="col-xs-4">
                                <input id="filter_LIKES_styleId" class="form-control" name="filter_LIKES_styleId"
                                       type="text"
                                       placeholder="模糊查询" onkeyup="this.value=this.value.toUpperCase()"/>
                            </div>
                            <label class="col-xs-2 control-label text-right" for="filter_LIKES_styleName">名称</label>

                            <div class="col-xs-4">
                                <input id="filter_LIKES_styleName" class="form-control" name="filter_LIKES_name"
                                       type="text" placeholder="模糊查询"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-5 col-sm-10">
                                <button type="button" class="btn btn-sm btn-primary" onclick="search_Multi_NewProduct()">
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
                            <table id="multi_newProduct_Grid"></table>
                            <div id="multi_newProduct_Page"></div>
                        </div>
                        <div class="col-md-1">
                            <div class="btn-group" role="group"  style="height: 300px">
                                <div>
                                    <button type="button" class="btn  btn-app btn-xs" onclick="selectMulti()">
                                        <i class="fa fa-2x fa-angle-double-right" aria-hidden="true"></i>
                                    </button>
                                </div>
                                <div>
                                    <button type="button" class="btn btn-app btn-xs" onclick="selectSingle();">
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
                            <table id="select_newProduct_Grid"></table>
                        </div>

                    </div>
                </div>
            </div>

            <div class="modal-footer no-margin-top">
                <a href="#" class="btn" onclick="close_multi_newProductDilog()">关闭</a>
                <a href="#" class="btn btn-primary" onclick="selectMultiNewProduct()">确定</a>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
<script>
    function open_multi_newProductDialog(viewTextId, callbackFun) {
        $("#multi_newProduct_dialog").data("viewTextId", viewTextId);
        $("#multi_newProduct_dialog").data("callbackFun", callbackFun);
        $('#multi_newProduct_dialog').modal({
            backdrop: 'static',
            keyboard: false,
            show:false
        });
        $("#multi_newProduct_dialog").on('show.bs.modal', function () {
            $("#multi_newProduct_Grid").jqGrid({
                height: '500px',
                multiselect: true,
                url: basePath + "/smart/newProduct/page.do",
                datatype: "json",
                mtype:'POST',
                colNames: [ '款号', '款名', '品牌'],
                colModel: [
                    {name: 'styleId',width:100},
                    {name: 'name',width:200},
                    {name: 'brandCode',width:100}
                ],
                fitColumns: true,
                viewrecords: true,
                autowidth: true,
                rownumbers: true,
                altRows: false,
                rowNum: 15,
                rowList: [15, 10],
                pager: "#multi_newProduct_Page",
                shrinkToFit: false,
                autoScroll: true,
                sortname: 'styleId',
                sortorder: "desc"
            });
            $("#select_newProduct_Grid").jqGrid({
                height: '550px',
                multiselect: true,
                datatype: "json",
                colNames: [ '款号', '款名', '品牌'],
                colModel: [
                    {name: 'styleId',width:100},
                    {name: 'name',width:100},
                    {name: 'brandCode',width:100}

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
    	var sids = $("#multi_newProduct_Grid").jqGrid("getGridParam", "selarrrow");
        var wholePage = $("#select_newProduct_Grid").jqGrid("getRowData");
    	if(sids.length>0){
    		var rtf=$("#multi_newProduct_Grid").jqGrid('getRowData',sids[0]);
    		if(contain(rtf.styleId,wholePage)){
                $("#multi_newProduct_Grid").jqGrid("setSelection",sids[0]);
    		    return
            }
    			$("#select_newProduct_Grid").jqGrid("addRowData", rtf.styleId, rtf, "first");
                $("#multi_newProduct_Grid").jqGrid("setSelection",sids[0]);
    		}
    }
//用于判断是否重复添加
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

    function selectMulti(){
        var sids = $("#multi_newProduct_Grid").jqGrid("getGridParam", "selarrrow");
        var wholePage = $("#select_newProduct_Grid").jqGrid("getRowData");
        //获得当前最大行号（数据编号）
//        var wholeMap={};
//        if(wholePage.length>=0){
//            for(var i=0;i<wholePage.length;i++){
//                var key=wholePage[i].styleId;
//                wholeMap[key]="Y";
//            }
//
//        }
        console.log(wholePage);
        if(sids){
            for(var i=sids.length-1;i>=0;i--){
                var ret = $("#multi_newProduct_Grid").jqGrid("getRowData",sids[i]);   //获得第一行的数据

                if(contain(ret.styleId,wholePage)){
                    continue
                }
                if($("#select_newProduct_Grid").jqGrid('getRowData',sids[i]).styleId==undefined){

                    //获得新添加行的行号（数据编号）
                    $("#select_newProduct_Grid").jqGrid("addRowData", ret.styleId, ret, "first");
                    $('#select_newProduct_Grid').jqGrid('editRow', ret.styleId, false);

                }
            }
        }
    }
    function clearAllMulti(){
        $("#select_newProduct_Grid").jqGrid('clearGridData');
    }

    function clearMulti(){
        var sids = $("#select_newProduct_Grid").jqGrid("getGridParam", "selarrrow");
         //获得当前最大行号（数据编号）
        if(sids){
            var len = sids.length;

            for(var i=0;i<len;i++){
                $("#select_newProduct_Grid").jqGrid("delRowData",sids[0]);   //获得第一行的数据
            }
        }
    }
    function search_Multi_NewProduct() {
        var serializeArray = $("#searchStyleForm").serializeArray();
        var params = array2obj(serializeArray);
        $("#multi_newProduct_Grid").jqGrid('setGridParam', {
            url: basePath + "/smart/newProduct/list.do",
            postData: params
        });
        $("#multi_newProduct_Grid").trigger("reloadGrid");
    }

    function close_multi_newProductDilog() {
        var callbackFun = $("#multi_newProduct_dialog").data("callbackFun");
        if (callbackFun != null) {
            var fn = eval(callbackFun);
            fn.call(this);
        }
        $("#multi_newProduct_dialog").modal('hide');
    }
    function selectMultiNewProduct() {
        var ids = $("#select_newProduct_Grid").jqGrid("getDataIDs");
        var viewTextId = $("#multi_newProduct_dialog").data("viewTextId");
        $(viewTextId).val(ids.toString());
        close_multi_newProductDilog();
    }
</script>
