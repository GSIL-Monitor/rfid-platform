<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/9/25
  Time: 18:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="inStockCodes_window" class="modal fade" tabindex="-1" role="dialog">
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
            <table id="inStockCodeListGrid"></table>
        </div>
    </div>
</div>

<script>

    function initInstockCodeList(sku, warehouseId) {
        $("#inStockCodeListGrid").jqGrid({
            height: 400,
            url: basePath + "/stock/warehStock/findInStockCodesBySku.do?sku=" + sku + "&warehouseId=" + warehouseId,
            datatype: "json",
            mtype: "POST",
            colModel: [
                {name: 'code', label: '唯一码', width: 150},
                {name: 'sku', label: 'SKU', width: 150},
                {name: 'warehouseId', label: '仓库', hidden: true},
                {name: 'floor', label: '仓库名', width: 150},
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
            sortorder: "desc",
            ondblClickRow: function (rowid) {
                transferToInitTagPage(rowid);
                closeGuestDialog();
            }
        });
        var parent_column = $("#inStockCodeListGrid").closest('.modal-dialog');
        $("#inStockCodeListGrid").jqGrid('setGridWidth', parent_column.width() - 2);
    }

    function inStockCodeListReload(sku, warehouseId) {
        $("#inStockCodeListGrid").clearGridData();
        $("#inStockCodeListGrid").jqGrid('setGridParam', {
            url: basePath + "/stock/warehStock/findInStockCodesBySku.do?sku=" + sku + "&warehouseId=" + warehouseId
        }).trigger("reloadGrid");
    }

    function closeGuestDialog() {
        $("#inStockCodes_window").modal('hide');
    }

    function transferToInitTagPage(code) {
        debugger;
        var billNo;
        $.ajax({
            dataType: "json",
            async: false,
            url: basePath + "/tag/birth/scanCode.do",
            data: {uniqueCode: code},
            type: "POST",
            success: function (result) {
                if (result.success) {
                    billNo = result.result;
                } else {
                    $.gritter.add({
                        text: result.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                }
            }
        });
        if (billNo && billNo !== null) {
            location.href = basePath + "/tag/birth/detail.do?billNo=" + billNo;
        }
    }
</script>