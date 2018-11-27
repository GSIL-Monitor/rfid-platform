<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<div id="show-allUniqueCode-list" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                唯一码明细
            </div>
        </div>
        <div class="modal-content">
            <div class="hr hr4"></div>
            <table id="allUniqueCodeListGrid"></table>
        </div>
        <div class="modal-footer">
            <div id="dialog_buttonGroup">
                <button id="close"  class='btn btn-primary' onclick="close()">清空</button>
            </div>
        </div>
    </div>
</div>

<script>
    $(function () {
        initAllUniqueCodeList();
    });

    function close() {
        $("#show-allUniqueCode-list").modal("hide");
    }

    function initAllUniqueCodeList() {
        $("#allUniqueCodeListGrid").jqGrid({
            height: 200,
            datatype:"json",
            mtype: "POST",
            colModel: [
                {name: 'code', label: '唯一码', width: 150},
                {name: 'sku', label: 'SKU', width: 150},
                {name: 'warehouseId', label: '仓库', hidden:true},
                {name: 'floor',label:'仓库名',width:150},
                {name: 'inStock', label: '库存状态', hidden: true},
                {
                    name: '', label: '库存状态', width: 110,
                    formatter: function (cellValue, options, rowObject) {
                        switch (rowObject.inStock) {
                            case 1:
                                return "在库";
                            case 0:
                                return "不在库";
                            default:
                                return "";
                        }
                    }
                }
            ],
            rownumbers: true,
            viewrecords: true,
            autowidth: true,
            altRows: true,
            multiselect: false,
            shrinkToFit: true,
            sortname: 'code',
            sortorder: "desc"
        });
    }
    function loadOutPutCodeDetail(rowId) {
        var row = $('#billInformationOutgrid').getRowData(rowId);
        var allCode=row.uniqueCodes.split(",");
        $.each(allCode,function (index,value) {
            var rowData={};
            rowData.code=value;
            rowData.sku=row.sku;
            rowData.warehouseId=row.warehouseId;
            rowData.floor=row.floor;
            rowData.inStock=row.inStock;
            $("#allUniqueCodeListGrid").addRowData($("#allUniqueCodeListGrid").getDataIDs().length,rowData);
        });
    }

    function loadPutCodeDetail(rowId) {
        var row = $('#notThisOneOutgrid').getRowData(rowId);
        var allCode=row.uniqueCodes.split(",");
        $.each(allCode,function (index,value) {
            var rowData={};
            rowData.code=value;
            rowData.sku=row.sku;
            rowData.warehouseId=row.warehouseId;
            rowData.floor=row.floor;
            rowData.inStock=row.inStock;
            $("#allUniqueCodeListGrid").addRowData($("#allUniqueCodeListGrid").getDataIDs().length,rowData);
        });
    }

    function loadInPutCodeDetail(rowId) {
        var row = $('#billInformationIngrid').getRowData(rowId);
        var allCode=row.uniqueCodes.split(",");
        $.each(allCode,function (index,value) {
            var rowData={};
            rowData.code=value;
            rowData.sku=row.sku;
            rowData.warehouseId=row.warehouseId;
            rowData.floor=row.floor;
            rowData.inStock=row.inStock;
            $("#allUniqueCodeListGrid").addRowData($("#allUniqueCodeListGrid").getDataIDs().length,rowData);
        });
    }

    function loadPutCodeNoDetail(rowId) {
        var row = $('#notThisOneIngrid').getRowData(rowId);
        var allCode=row.uniqueCodes.split(",");
        $.each(allCode,function (index,value) {
            var rowData={};
            rowData.code=value;
            rowData.sku=row.sku;
            rowData.warehouseId=row.warehouseId;
            rowData.floor=row.floor;
            rowData.inStock=row.inStock;
            $("#allUniqueCodeListGrid").addRowData($("#allUniqueCodeListGrid").getDataIDs().length,rowData);
        });
    }
</script>