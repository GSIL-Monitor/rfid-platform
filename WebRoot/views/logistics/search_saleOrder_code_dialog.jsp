<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/7/17
  Time: 10:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<div id="saerch-saleOrder-dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                查询订单
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">


                <div class="row">
                    <form class="form-horizontal" role="search" id="saerchbill-editForm"  onkeydown="if(event.keyCode==13)return false;">
                        <label class="col-sm-2 control-label no-padding-right"
                               for="searchbill_uniqueCodes">唯一码</label>
                        <div class="col-xs-12 col-sm-9">
                            <input class="form-control" id="searchbill_uniqueCodes" name="add_uniqueCode" type="text"
                                   placeholder="按回车键结束"/>
                        </div>

                    </form>
                </div>
            </div>
            <div class="hr hr4"></div>
            <table id="saerchbillGrid"></table>
        </div>
        <div class="modal-footer">
            <div id="searchbill_dialog_buttonGroup">
            </div>
        </div>
    </div>
</div>

<script>
    $(function () {
        initSearchBillGrid();
        searchBillkeydown();
    });

    function initSearchBillGrid() {
        $("#saerchbillGrid").jqGrid({
            height: 400,
            datatype: "json",
            colModel: [
                /*{name: 'code', label: '唯一码', width: 60},*/
                {name: 'billId', label: '单号',width: 20},
                {name: 'styleId', label: '款号', width: 15},
                {name: 'colorId', label: '色码', width: 10},
                {name: 'sizeId', label: '尺码', width: 10},
                {name: 'sku', label: 'SKU', width: 20},
                /*{name: 'styleName', label: '款式', width: 40, hidden: true},
                {name: 'colorName', label: '颜色', width: 40, hidden: true},
                {name: 'sizeName', label: '尺寸', width: 40, hidden: true},*/
                {name: 'billDate', label: '时间', width: 15},
                {name: 'busnissName', label: '销售员', width: 15}
               /* {name: 'stockPrice', label: '库存金额', width: 40, hidden: true} //库存金额*/
            ],
            rownumbers: true,
            viewrecords: true,
            autowidth: true,
            altRows: true,
            multiselect: false,
            shrinkToFit: true,
            sortname: 'updateTime',
            sortorder: "desc",
            ondblClickRow: function (rowid) {
                var rowData = $("#saerchbillGrid").jqGrid('getRowData', rowid);
                searchOrderBillInfo(rowData.billId);
            }
        });
        var parent_column = $("#saerchbillGrid").closest('.modal-dialog');
        $("#saerchbillGrid").jqGrid('setGridWidth', parent_column.width() - 5);
    }

    function searchBillkeydown() {

        //监听回车键
        $("#searchbill_uniqueCodes").keydown(function (event) {
            if (event.keyCode == 13) {


                var code = document.getElementById("searchbill_uniqueCodes").value;
                var progressDialog = bootbox.dialog({
                    message: '<p><i class="fa fa-spin fa-spinner"></i>checking...</p>'
                });


                var ajax_url=basePath + "/stock/warehStock/checksaleEpcStock.do";
                var ajax_data={code: code};

                $.ajax({
                    url: ajax_url,
                    data: ajax_data,
                    datatype: "json",
                    type: "POST",
                    success: function (data) {
                        if (data.success) {
                           /* if (skuValid(data.result)) {*/
                                $("#saerchbillGrid").addRowData($('#saerchbillGrid').getDataIDs().length, data.result);
                               /* allCodes = allCodes + "," + code;*/
                           /* }*/
                        } else {
                            $.gritter.add({
                                text: data.msg,
                                class_name: 'gritter-success  gritter-light'
                            });
                        }
                        $('#saerchbill-editForm').clearForm();
                        progressDialog.modal('hide');
                    }
                });
            }
        })
    }


</script>
