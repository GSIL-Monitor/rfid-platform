<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/9/25
  Time: 18:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="show-floorallocation-list" class="modal fade" tabindex="-1" role="dialog">
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
            <table id="FloorallocationListGrid"></table>
        </div>
    </div>
</div>

<script>
    $(function () {
        initFloorallocationList();
    });

    function initFloorallocationList() {
        $("#FloorallocationListGrid").jqGrid({
            height: 400,
            datatype:"local",
            mtype: "POST",
            colModel: [
                {name: 'sku', label: 'SKU', width: 150},
                {name: 'Floorallocation', label: '库位'},
                {name: 'sum',label:'数量',width:150},
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
        var parent_column = $("#FloorallocationListGrid").closest('.modal-dialog');
        $("#FloorallocationListGrid").jqGrid('setGridWidth', parent_column.width());
    }

    function FloorallocationListReload() {
        /*   debugger;
         $("#uniqueCodeListGrid").clearGridData();*/
        $("#FloorallocationListGrid").jqGrid('setGridParam', {
            url: basePath + "/logistics/transferOrder/findFloorallocationAndSku.do?billNo=" +$("#edit_billNo").val(),
            datatype:"json"
        }).trigger("reloadGrid");
    }
</script>