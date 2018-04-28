<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/9/25
  Time: 18:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="show-uniqueCode-list" class="modal fade" tabindex="-1" role="dialog">
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
            <table id="uniqueCodeListGrid"></table>
        </div>
    </div>
</div>

<script>
    //    $(function () {
    //        initUniqueCodeList();
    //    });

    function initUniqueCodeList(uniqueCodes) {
        debugger;
        $("#uniqueCodeListGrid").jqGrid({
            height: 400,
            url: basePath + "/stock/warehStock/findCodeList.do?uniqueCodes=" + uniqueCodes,
            datatype: "json",
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
        var parent_column = $("#uniqueCodeGrid").closest('.modal-dialog');
        $("#uniqueCodeGrid").jqGrid('setGridWidth', parent_column.width());
    }

    function codeListReload(uniqueCodes,billNo) {
        debugger;
        $("#uniqueCodeListGrid").clearGridData();
        $("#uniqueCodeListGrid").jqGrid('setGridParam', {
            url: basePath + "/stock/warehStock/findCodeList.do?uniqueCodes=" + uniqueCodes+"&billNo="+billNo
        }).trigger("reloadGrid");
    }
</script>