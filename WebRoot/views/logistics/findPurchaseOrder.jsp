<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/9/25
  Time: 18:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="show-findPurchase-list" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                采购单
            </div>
        </div>
        <div class="modal-content">
            <div class="hr hr4"></div>
            <table id="findPurchaseListGrid"></table>
        </div>
    </div>
</div>

<script>
    //    $(function () {
    //        initUniqueCodeList();
    //    });

    function initUniquePurchaseList() {
        debugger;
        $("#findPurchaseListGrid").jqGrid({
            height: 400,
            url: basePath + "/logistics/relenishBill/findpurchaseOrderBillonReplenishBill.do?billno=" + $("#edit_billNo").val(),
            datatype: "json",
            mtype: "POST",
            colModel: [
                {name: 'id', label: '单号', width: 150},
                {name: 'billDate', label: '时间', width: 150},
                {name: 'destName', label: '收货仓库',  width: 150},
                {name: 'totQty',label:'单据数量',width:150},
                {name: 'totInQty', label: '已入库数量',  width: 150},

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
                var row = $("#findPurchaseListGrid").jqGrid("getRowData", rowid);
                /* $(viewId).val(row.code);
                 $(viewTextId).val(row.name);
                 closeUnitSelectDialog();*/
                window.location.href=basePath+"/logistics/relenishBill/findPurchase.do?billNo="+row.id+"&url=/logistics/relenishBill/index.do";
            }
        });
        var parent_column = $("#findPurchaseListGrid").closest('.modal-dialog');
        $("#findPurchaseListGrid").jqGrid('setGridWidth', parent_column.width());
    }


</script>