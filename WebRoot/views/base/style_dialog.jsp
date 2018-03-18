<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="modal-tableStyle" class="modal fade"  tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header no-padding">
                <div class="table-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        <span class="white">&times;</span>
                    </button>
                    选择款信息
                </div>
            </div>

            <div class="modal-body no-padding">
                <div class="widget-body">

                    <div class="widget-main" id="searchPanel">
                        <form class="form-horizontal" role="form" id="searchStyleForm">
                            <div class="form-group">
                                <label class="col-xs-2 control-label text-right" for="filter_LIKES_styleId">款号</label>

                                <div class="col-xs-4">
                                    <input  id="filter_LIKES_styleId"  class="form-control" name="filter_LIKES_styleId"
                                           type="text"
                                           placeholder="模糊查询"/>
                                </div>
                                <label class="col-xs-2 control-label text-right" for="filter_LIKES_styleName">名称</label>

                                <div class="col-xs-4">
                                    <input  id="filter_LIKES_styleName" class="form-control"  name="filter_LIKES_styleName"
                                           type="text" placeholder="模糊查询"/>
                                </div>
                            </div>
                             <div class="form-group">
                                        <div class="col-sm-offset-5 col-sm-10">
                                            <button type="button" class="btn btn-sm btn-primary" onclick="searchStyle();">
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
                <table id="styleSelect_Grid" ></table>
                <div id="styleSelect_Page"></div>
            </div>

            <div class="modal-footer no-margin-top">
                <a href="#" class="btn" onclick="closestyleSelectDialog()">关闭</a>
                <a href="#" class="btn btn-primary" onclick="selectedstyle()">确定</a>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
<script>
    function openstyleDialog(viewId,viewTextId,callbackFun) {
        $("#modal-tableStyle").data("viewId",viewId);
        $("#modal-tableStyle").data("viewTextId",viewTextId);
        $("#modal-tableStyle").data("callbackFun",callbackFun);
        $("#modal-tableStyle").on('show.bs.modal', function () {
            $("#styleSelect_Grid").jqGrid({
                height: 300,
                url: basePath+"/prod/style/page.do",
                mtype:"POST",
                datatype: "json",
                colModel: [
                    {name: 'id', label: 'id',hidden:true, width: 40},
                    {name: 'styleId', label: '款号',editable:true, width: 40},
                    {name: 'styleName', label: '名称', editable:true,width: 40},
                    {name: 'price', label: '吊牌价', editable:true,width: 40}
                ],
                viewrecords: true,
                autowidth: true,
                rownumbers: true,
                altRows: true,
                rowNum: 50,
                rowList: [20, 50],
                pager: "#styleSelect_Page",
                multiselect: false,
                shrinkToFit: true,
                sortname : 'styleId',
                sortorder : "desc",
                ondblClickRow:function(rowid) {
                    var row = $("#styleSelect_Grid").jqGrid("getRowData",rowid);
                    $(viewId).val(row.styleId);
                    $(viewTextId).val(row.styleName);
                   closestyleSelectDialog();
                }

            });
            var parent_column = $("#styleSelect_Grid").closest('.modal-dialog');
            $("#styleSelect_Page_left").css("width","2px");
            $("#styleSelect_Grid").jqGrid( 'setGridWidth', parent_column.width()-2);

        }).modal("show");

    }
    function searchStyle(){
    	 var serializeArray = $("#searchStyleForm").serializeArray();
    	 var params = array2obj(serializeArray);
    	 $("#styleSelect_Grid").jqGrid('setGridParam', {
    		    url:basePath+"/prod/style/page.do",
    	        page : 1,
    	        postData : params
    	 });
    	 $("#styleSelect_Grid").trigger("reloadGrid");
    }
    function resetStyle(){
    	
    }
    function closestyleSelectDialog() {
        var callbackFun =  $("#modal-tableStyle").data("callbackFun");
        if(callbackFun!=null){
            var fn = eval(callbackFun);
            fn.call(this);
        }
        $("#modal-tableStyle").modal('hide');
    }
    function selectedstyle() {
        var rowId = $("#styleSelect_Grid").jqGrid("getGridParam", "selrow");
        var row = $("#styleSelect_Grid").jqGrid('getRowData',rowId);
        var viewId = $("#modal-tableStyle").data("viewId");
        var viewTextId = $("#modal-tableStyle").data("viewTextId");
        $(viewId).val(row.styleId);
        $(viewTextId).val(row.styleName);
       closestyleSelectDialog();
    }
</script>
