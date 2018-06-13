<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/9/25
  Time: 18:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="show-findWxShop-list" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                商城信息
            </div>
        </div>
        <div class="modal-content">
            <div class="hr hr4"></div>
            <table id="findWxShopListGrid"></table>
        </div>
    </div>
</div>

<script>
    //    $(function () {
    //        initUniqueCodeList();
    //    });

    function initfindWxShopList() {
        debugger;
        $("#findWxShopListGrid").jqGrid({
            height: 400,
            url: basePath + "/product/SendInventory/findPaymentMessage.do?billno=" + $("#edit_billNo").val(),
            datatype: "json",
            mtype: "POST",
            colModel: [
                {name: 'billNo', label: '单号', width: 150},
                {name: 'customName', label: '客户名称', width: 150},
                {name: 'phone', label: '电话',  width: 150},
                {name: 'address',label:'地址',width:150}


            ],
            rownumbers: true,
            viewrecords: true,
            autowidth: true,
            altRows: true,
            multiselect: false,
            shrinkToFit: true,
            sortname: 'code',
            sortorder: "desc",

        });
        var parent_column = $("#findWxShopListGrid").closest('.modal-dialog');
        $("#findWxShopListGrid").jqGrid('setGridWidth', parent_column.width());
    }


</script>