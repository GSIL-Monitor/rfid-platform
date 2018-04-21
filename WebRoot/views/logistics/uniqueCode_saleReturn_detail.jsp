<%--
  Created by IntelliJ IDEA.
  User: Anna
  Date: 18/4/20
  Time: 14:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="show-uniqueCode-saleReturn-list" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" style="width:900px;align-content: center;">
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
            <table id="uniqueCodeSaleReturnListGrid"></table>
        </div>
    </div>
</div>

<script>

    //    $(function () {
    //        initUniqueCodeList();
    //    });

    function initUniqueCodeSaleReturnList(uniqueCodes) {
        debugger;
        $("#uniqueCodeSaleReturnListGrid").jqGrid({
            height: 400,
            // url: basePath + "/stock/warehStock/findCodeList.do?uniqueCodes=" + uniqueCodes,
            url: basePath + "/stock/warehStock/findCodeSaleReturnList.do?uniqueCodes=" + uniqueCodes,
            datatype: "json",
            mtype: "POST",
            colModel: [
                {name: 'code', label: '唯一码', width: 150},
                {name: 'sku', label: 'SKU', width: 120},
                {name: 'warehouseId', label: '仓库', hidden: true},
                {name: 'floor', label: '仓库名', width: 100},
                {name: 'inStock', label: '库存状态', hidden: true},
                {
                    name: '', label: '库存状态', width: 80,
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
                },
                /* Anna */
                {name: 'originBillNo', label: '原始单号', width: 160},
                {name: 'lastSaleTime', label: '最后销售时间', width: 160},
                {name: 'saleCycle', label: '销售周期', width: 70, cellattr: addCellAttr} //销售周期（开单当天时间－销售单时间）
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
        var parent_column = $("#uniqueCodeGrid").closest('.modal-dialog');
        // $("#uniqueCodeGrid").jqGrid('setGridWidth', parent_column.width());
    }

    function addCellAttr(rowId, val, rawObject, cm, rdata) {
        if (rawObject.saleCycle >= 20) {
            return "style='color:red'";
        }
    }

    function codeSaleReturnListReload(uniqueCodes) {
        debugger;
        $("#uniqueCodeSaleReturnListGrid").clearGridData();
        $("#uniqueCodeSaleReturnListGrid").jqGrid('setGridParam', {
            // url: basePath + "/stock/warehStock/findCodeList.do?uniqueCodes=" + uniqueCodes
            url: basePath + "/stock/warehStock/findCodeSaleReturnList.do?uniqueCodes=" + uniqueCodes
        }).trigger("reloadGrid");
    }
</script>
