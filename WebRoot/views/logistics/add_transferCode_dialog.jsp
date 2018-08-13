<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="modal-addTransfer-table" class="modal fade" role="dialog" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                添加唯一码信息
            </div>
        </div>
        <div class="modal-content no-padding" >
            <table id="epcTransfergrid" ></table>
        </div>
        <div class="modal-footer">
            <button type="button"  class="btn btn-primary" onclick="saveTransferEpc()">保存</button>
        </div>
    </div>
</div>
<script>

    $(function () {
        initEpcGrid();
    });

    function initEpcGrid() {
        $("#epcTransfergrid").jqGrid({
            height:  "350",
            datatype: "json",
            colModel: [
                {name: 'id', label: 'id', hidden: true, width: 240},
                {name: 'sku', label: 'sku', hidden: true, width: 240},
                {name:'code',label:'唯一码',eidtable:true,width:160},
                {name:'styleId', label: '款号', editable: true, width: 160},
                {name:'colorId',label:'色号',editable:true,width:200},
                {name:'sizeId',label:'尺寸',editable:true,width:200},

            ],
            viewrecords: true,
            autowidth: false,
            rownumbers: true,
            altRows: true,
            rowNum:-1,
            multiselect: true,
            shrinkToFit: true,
            sortname: 'code',
            sortorder: "asc",


        });
        var parent_column = $("#epcTransfergrid").closest('.modal-dialog');
        console.log(parent_column.width());
        $("#epcTransfergrid").jqGrid('setGridWidth', parent_column.width() - 5);


    }

</script>