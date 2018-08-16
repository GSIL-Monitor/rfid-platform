<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/9/25
  Time: 18:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="show-findTransferOrder-list" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                调拨单
            </div>
        </div>
        <div class="modal-content">
            <div class="hr hr4"></div>
            <table id="findTransferOrderGrid"></table>
        </div>
    </div>
</div>

<script>
    //    $(function () {
    //        initUniqueCodeList();
    //    });

    function initUniqueTransferOrderList() {
        debugger;
        console.log($("#edit_billNo").val());
        $("#findTransferOrderGrid").jqGrid({
            height: 400,
            url: basePath + "/logistics/transferOrder/findTransferBillNo.do?billno=" + $("#edit_billNo").val(),
            datatype: "json",
            mtype: "POST",
            colModel: [
                {name: 'id', label: '单号', width: 150},
                {name: 'billDate', label: '时间', width: 150},
                {name: 'origUnitName', label: '发货方',  width: 150},
                {name: 'destUnitName',label:'收货方',width:150},
                {name: 'totQty', label: '数量',  width: 150},

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

            }
        });
        var parent_column = $("#findTransferOrderGrid").closest('.modal-dialog');
        $("#findTransferOrderGrid").jqGrid('setGridWidth', parent_column.width());
    }

    function retrunTransferOrderListReload() {
        debugger;
        $("#findTransferOrderGrid").clearGridData();
        $("#findTransferOrderGrid").jqGrid('setGridParam', {
            url: basePath + "/logistics/Consignment/findSaleOrderReturnBillNo.do?billno=" + $("#edit_billNo").val()
        }).trigger("reloadGrid");
    }
</script>