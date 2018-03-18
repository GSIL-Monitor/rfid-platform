<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/12/6
  Time: 10:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="inventoryMerge-origList" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                原始盘点单据
            </div>
        </div>
        <div class="modal-content">
            <div class="hr hr4"></div>
            <table id="origListGrid"></table>
        </div>
    </div>
</div>

<script>
        $(function () {
            initOrigList();
        });

    function initOrigList() {
        $("#origListGrid").jqGrid({
            height: 400,
            url: basePath + "/stock/InventoryMerge/findOrigBill.do?billNo=" + billNo,
            datatype: "json",
            mtype: "POST",
            colModel: [
                {name: 'id', label: '单据编号', width: 200},
                {name: 'origId', label: '盘点单位', width: 150, hidden: true},
                {
                    name: 'origName', label: '盘点单位', width: 300,
                    formatter: function (cellValue, option, rowObject) {
                        return "[" + rowObject.origId + "]" + cellValue;
                    }
                },
                {name: 'totQty', label: '预计单品数', width: 150},
                {name: 'actQty', label: '实际单品数', width: 150},
                {name: 'billDate', label: '单据日期', width: 150}
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
        var parent_column = $("#origListGrid").closest('.modal-dialog');
        $("#origListGrid").jqGrid('setGridWidth', parent_column.width());
    }
</script>
