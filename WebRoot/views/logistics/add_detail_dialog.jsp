<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="modal-addDetail-table" class="modal fade" role="dialog" tabindex="-1">
    <div class="modal-dialog" style="width:70%;">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                添加商品信息
            </div>
        </div>
        <div class="modal-content no-padding" style="width:100%;height:650px;">
            <div class="modal-body ">

                <div class="col-sm-6" id="search_style_Panel">
                    <div class="col-xs-12 col-sm-12">
                        <div class="widget-box light-border">
                            <div class="widget-header">
                                <h5 class="widget-title">商品款式</h5>
                            </div>
                            <br />
                            <form class="form-horizontal" role="form" id="StyleSearchForm" onkeydown="if(event.keyCode==13)return false;">
                                <%--<form class="form-horizontal" role="search" id="StyleSearchForm">--%>
                                <div class="form-group">
                                    <label class="col-xs-2 control-label text-right" for="filter_LIKES_styleId">款号</label>
                                    <div class="col-xs-9">
                                        <input class="form-control" id="filter_LIKES_styleId" name="filter_LIKES_styleId"
                                               type="text"
                                               placeholder="模糊查询"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-11 btnPosition">
                                        <button type="button" class="btn btn-sm btn-primary" onclick="searcheditStyle()">
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
                        <table id="stylegrid" ></table>
                        <div id="stylegrid-pager"></div>
                    </div>

                </div>
                <div class=" col-sm-6" id="search_color_size_Panel">
                    <div class="col-xs-12 col-sm-12">
                        <div class="widget-box  light-border">
                            <div class="widget-header">
                                <h5 class="widget-title">颜色尺码</h5>
                            </div>

                        </div>
                        <table id="color_size_grid"></table>
                    </div>

                </div>

                <%-- </div>--%>
            </div>
        </div>
        <div class="modal-footer">

            <button type="button"  class="btn btn-primary" onclick="addProductInfo(false)">新增</button>
            <button type="button"  class="btn btn-primary" onclick="addProductInfo(true)">新增并关闭</button>

        </div>
    </div>
</div>
<script>
    var searchStyleUrl = basePath +  "/prod/style/page.do";
    var editcolosizeRow = null;
    var parent_dialog = $("#modal-addDetail-table");
    $(function () {
        $("#modal-addDetail-table").on('show.bs.modal', function () {
            initStyleGrid();
            initColorSizeGrid();
            searcheditStyle();
            initStyleGridGroup()
        });

    });

    function initStyleGridGroup() {
        var name = [];
        var colModel=$("#stylegrid").jqGrid('getGridParam','colModel');
        $.each(colModel,function (index,value) {
           if (index>0){
               name.push(value.name);
           }
        });
        $.ajax({
            dataType: "json",
            async: false,
            url: basePath + "/prod/style/initStyleGridGroup.do",
            type: "POST",
            success: function (msg) {
                if (msg.success) {
                    var result=msg.result;
                    for(var i=0;i<result.length;i++){
                        if(result[i].isShow===0){
                            for (var j = 0;j<name.length;j++){
                                if (name[j]==result[i].privilegeId){
                                    $ ("#stylegrid").setGridParam().showCol(result[i].privilegeId).trigger("reloadGrid");
                                }
                            }
                        }else {
                            for (var j = 0;j<name.length;j++) {
                                if (name[j] == result[i].privilegeId) {
                                    $("#stylegrid").setGridParam().hideCol(result[i].privilegeId);
                                }
                            }
                        }
                    }
                } else {
                    bootbox.alert(msg.msg);
                }
            }
        });
    }
    function initStyleGridColumn(storeType){
        if(storeType == "CT-AT"){
            $ ("#stylegrid").setGridParam().hideCol("preCast");
            $ ("#stylegrid").setGridParam().hideCol("wsPrice");
            $ ("#stylegrid").setGridParam().showCol("puPrice").trigger("reloadGrid");
        }else if(storeType == "CT-ST"){
            $ ("#stylegrid").setGridParam().hideCol("preCast");
            $ ("#stylegrid").setGridParam().hideCol("puPrice");
            $ ("#stylegrid").setGridParam().showCol("wsPrice").trigger("reloadGrid");

        }else if(storeType == "CT-LS"){
            $ ("#stylegrid").setGridParam().hideCol("preCast");
            $ ("#stylegrid").setGridParam().hideCol("puPrice");
            $ ("#stylegrid").setGridParam().hideCol("wsPrice").trigger("reloadGrid");
        }
    }
    function initStyleGrid() {
        $("#stylegrid").jqGrid({
            height:  "350",
            datatype: "local",
            colModel: [
                {name: 'id', label:'编号',editable:true,width: 100,hidden:true},
                {name: 'styleId', label: '款号',editable:true, width: 100,sortable: true},
                {name: 'styleName', label: '款名', editable:true,width: 200},
                {name: 'preCast', label: '采购价', editable:true,width: 80},
                {name: 'puPrice', label: '销售价', editable:true,width: 80,hidden:true},
                {name: 'wsPrice', label: '销售价', editable:true,width: 80,hidden:true},
                {name: 'price', label: '吊牌价', editable:true,width: 80},
                {name: 'class6', label: '入库类型',hidden:true},
                {name:'bargainPrice',label:'特价',editable:true,width:80,hidden:true}
            ],
            viewrecords: true,
            rownumbers: true,
            altRows: true,
            autowidth:true,
            shrinkToFit: true,
            rowNum: 20,
            rowList: [10,20, 50, 100],
            pager: "#stylegrid-pager",
            multiselect: false,
            sortname : 'id',
            sortorder : "desc",
            onSelectRow : function(rowid,status) {
                var row = $("#stylegrid").getRowData(rowid);
                $("#color_size_grid").jqGrid("setGridParam",{
                    datatype: "json",
                    url: basePath+"/prod/product/listOrderByColorAndSize.do?styleId="+row.styleId,
                }).trigger('reloadGrid');
            }

        });
        $("#stylegrid").jqGrid( 'setGridWidth', parent_dialog.width()*0.6/2);

    }
    function searcheditStyle(){
        var serializeArray = $("#StyleSearchForm").serializeArray();
        var params = array2obj(serializeArray);

        $("#stylegrid").jqGrid("setGridParam",{
            url: searchStyleUrl,
            page : 1,
            datatype: "json",
            postData : params
        }).trigger('reloadGrid');
    }

    function initColorSizeGrid() {
        $("#color_size_grid").jqGrid({
            height:  "500",
            datatype:"local",
            colModel: [
                {name: 'code', label:'code',width: 100,hidden:true},
                {name: 'styleId', label: '款号', width: 100,sortable: true,hidden:true},
                {name: 'styleName', label: '款名',width: 100,hidden:true},
                {name: 'preCast', label: '采购价',width: 80,hidden:true},
                {name: 'puPrice', label: '采购价', width: 80,hidden:true},
                {name: 'wsPrice', label: '采购价', width: 80,hidden:true},
                {name:'bargainPrice',label:'特价',hidden:true,width:80},
                {name: 'price', label: '吊牌价',width: 80,hidden:true},
                {name: 'colorId', label: '色码',width: 80,sortable: true},
                {name: 'colorName', label: '颜色', width: 100,sortable: true},
                {name: 'sizeId', label: '尺码', width: 80,sortable: true},
                {name: 'sizeName', label: '尺寸', width: 100,sortable: true},
                {name: 'qty', label: '数量',editable:true, width: 100,sortable: true,

                    editoptions: {
                        dataInit: function (e) {
                            $(e).spinner();
                        }
                    }
                },

            ],
            viewrecords: true,
            rownumbers: true,
            altRows: true,
            autowidth:true,
            shrinkToFit: true,
            rowNum: -1,
            multiselect: false,
            sortname : 'sku',
            sortorder : "desc",onSelectRow: function (rowid, status) {
                if(editcolosizeRow!=null){
                    $('#color_size_grid').saveRow(editcolosizeRow);
                }
                editcolosizeRow = rowid;
                $('#color_size_grid').editRow(rowid);
            }

        });
        $("#color_size_grid").jqGrid( 'setGridWidth', parent_dialog.width()*0.6/2);

    }

    /**
     * add by yushen 订单中通过加号选择商品时，可扫描唯一码查出对应的款式。
     */
    var $inputOfStyleId = $("#filter_LIKES_styleId");
    function addProduct_keydown() {
        //监听回车
        $inputOfStyleId.keydown(function (event) {
            if (event.keyCode == 13) {
                console.log("添加商品对话框输入内容："+ $inputOfStyleId.val());
                if(!$inputOfStyleId.val()){
                    return;
                }
                if(uniqueCodeValid($inputOfStyleId.val())){//如果输入为唯一码
                    $.ajax({
                        async: false,
                        url: basePath + "/stock/warehStock/getStyleIdByCode.do?code=" + $inputOfStyleId.val(),
                        datatype: "json",
                        type: "GET",
                        success: function (data) {//查询成功将款号放入输入框，查询失败将输入框清空
                            if(data.success){
                                $inputOfStyleId.val(data.result);
                            }else {
                                $inputOfStyleId.val("");
                                $.gritter.add({
                                    text: data.msg,
                                    class_name: 'gritter-success  gritter-light'
                                });
                            }
                        }
                    })
                }
                searchStyle();
                $("#color_size_grid").clearGridData();
            }
        })
    }

    /**
     * add by yushen 校验是否唯一码
     */
    function uniqueCodeValid(code) {
        //匹配13位纯数字唯一码
        var reg1 = /^[0-9]{13}$/;
        //匹配24位十六进制数
        var reg2 = /^[0-9a-fA-F]{24}$/i;

        if(code.match(reg1) || code.match(reg2)){
            return true;
        }else {
            return false;
        }
    }
</script>