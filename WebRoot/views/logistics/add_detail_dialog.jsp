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
                                            <button type="button" class="btn btn-sm btn-primary" onclick="searchStyle()">
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


            <button type="button"  class="btn btn-primary" onclick="addProductInfo()">保存</button>

        </div>
    </div>
</div>
<script>
    var searchStyleUrl = basePath +  "/prod/style/page.do";
    var editcolosizeRow = null;
    var parent_dialog = $("#modal-addDetail-table");
    $(function () {
        initStyleGrid();
        initColorSizeGrid();

    });
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
            url:searchStyleUrl,
            datatype: "json",
            colModel: [
                {name: 'id', label:'编号',editable:true,width: 100,hidden:true},
                {name: 'styleId', label: '款号',editable:true, width: 100,sortable: true},
                {name: 'styleName', label: '款名', editable:true,width: 200},
                {name: 'preCast', label: '采购价', editable:true,width: 80},
                {name: 'puPrice', label: '销售价', editable:true,width: 80,hidden:true},
                {name: 'wsPrice', label: '销售价', editable:true,width: 80,hidden:true},
                {name: 'price', label: '吊牌价', editable:true,width: 80},
                {name: 'class6', label: '入库类型',hidden:true}

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
                    url: basePath+"/prod/product/listOrderByColorAndSize.do?styleId="+row.styleId,
                }).trigger('reloadGrid');
            }

        });
        $("#stylegrid").jqGrid( 'setGridWidth', parent_dialog.width()*0.6/2);


    }
    function searchStyle(){
        var serializeArray = $("#StyleSearchForm").serializeArray();
        var params = array2obj(serializeArray);

        $("#stylegrid").jqGrid("setGridParam",{
            url: searchStyleUrl,
            page : 1,
            postData : params
        }).trigger('reloadGrid');
    }

    function initColorSizeGrid() {
        $("#color_size_grid").jqGrid({
            height:  "500",
            datatype: "json",
            colModel: [
                {name: 'code', label:'code',width: 100,hidden:true},
                {name: 'styleId', label: '款号', width: 100,sortable: true,hidden:true},
                {name: 'styleName', label: '款名',width: 100,hidden:true},
                {name: 'preCast', label: '采购价',width: 80,hidden:true},
                {name: 'puPrice', label: '采购价', width: 80,hidden:true},
                {name: 'wsPrice', label: '采购价', width: 80,hidden:true},
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
</script>