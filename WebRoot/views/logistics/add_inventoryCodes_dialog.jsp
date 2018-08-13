<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/7/17
  Time: 10:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="add-inventory-dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                添加唯一码
            </div>
        </div>
        <div class="modal-content no-padding" style="width:100%;height:600px;">
            <div class="modal-body">


                <div class="row">
                    <form class="form-horizontal" role="search" id="inventoryCode-editForm" onkeydown="if(event.keyCode==13)return false;">
                        <label class="col-sm-2 control-label no-padding-right"
                               for="add_uniqueCodes">唯一码</label>
                        <div class="col-xs-12 col-sm-9">
                            <input class="form-control" id="add_uniqueCodes" name="add_uniqueCode"type="text"
                                   placeholder="按回车键结束" style="ime-mode:active"/>
                        </div>

                    </form>
                </div>
            </div>
            <div class="hr hr4"></div>
            <table id="inventoryCodeGrid"></table>
        </div>
        <div class="modal-footer">
            <label class="control-label col-sm-3" for="codeQtys" style="text-align: center">已添加数量：</label>
            <div class="col-sm-1">
                <input class="form-control" type="text" id="codeQtys"
                       style="float: left;background-color: transparent;border: none" readonly value="0">
            </div>
            <div id="dialog_buttonGroups"></div>
        </div>
    </div>
</div>

<script>
    $(function () {
        initUniqueCodeGrids();
        keydowns();

    });

    function initUniqueCodeGrids() {
        $("#inventoryCodeGrid").jqGrid({
            height: 500,
            datatype: "json",
            colModel: [
                {name: 'code', label: '唯一码', width: 60},
                {name: 'updateTime', label: '修改时间', hidden: true, width: 40},
                {name: 'styleId', label: '款号', width: 40},
                {name: 'colorId', label: '色码', width: 40},
                {name: 'sizeId', label: '尺码', width: 40},
                {name: 'sku', label: 'SKU', width: 40},
                {name: 'styleName', label: '款式', width: 40, hidden: true},
                {name: 'colorName', label: '颜色', width: 40, hidden: true},
                {name: 'sizeName', label: '尺寸', width: 40, hidden: true},
                {
                    name: 'inStock', label: '是否在库', width: 30, align: 'center', sortable: false,
                    formatter: function (cellValue, options, rowObject) {
                        if (rowObject.inStock == 0) {
                            return '不在库';
                        } else if (rowObject.inStock == 1) {
                            return '在库';
                        } else {
                            return '';
                        }
                    }
                },
                {name: 'storage', label: '所在仓库', width: 40}

            ],
            rownumbers: true,
            viewrecords: true,
            autowidth: true,
            altRows: true,
            multiselect: false,
            shrinkToFit: true,
            sortname: 'updateTime',
            sortorder: "desc"
            /* ondblClickRow: function (rowid) {

             var rowData = $("#inventoryCodeGrid").jqGrid('getRowData', rowid);
             alert(rowData.code);
             }*/
        });
        var parent_column = $("#inventoryCodeGrid").closest('.modal-dialog');
        $("#inventoryCodeGrid").jqGrid('setGridWidth', parent_column.width() - 5);

    }

    $(function () {
        $("#add-inventory-dialog").on('shown.bs.modal', function () {
            initCodeEditFormValid();
            $('#inventoryCode-editForm').clearForm();
            $('#inventoryCode-editForm').resetForm();
            $("#inventoryCodeGrid").jqGrid("clearGridData");

            $("#add_uniqueCodes").focus();

        });
    });
    function initCodeEditFormValid() {
        $('#inventoryCode-editForm').bootstrapValidator({
            message: '输入值无效',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                add_uniqueCode: {
                    validators: {
                        notEmpty: {
                            message: '唯一码不能为空'
                        }
                    }
                }
            }
        });

    }

    var allCodes="";
    function keydowns() {
        //监听回车键
        $("#add_uniqueCodes").keydown(function (event) {

            if (event.keyCode == 13) {
                /*$('#inventoryCode-editForm').data('bootstrapValidator').validate();
                 if (!$('#inventoryCode-editForm').data('bootstrapValidator').isValid()) {
                 return;
                 }*/
                var code = document.getElementById("add_uniqueCodes").value;

                //每次添加，拼接code，用于判断当前添加是否重复
                if (allCodes.indexOf(code) != -1) {
                    $('#inventoryCode-editForm').clearForm();
                    $.gritter.add({
                        text: "不能重复添加",
                        class_name: 'gritter-success  gritter-light'
                    });
                    return;
                }

                var progressDialog = bootbox.dialog({
                    message: '<p><i class="fa fa-spin fa-spinner"></i>checking...</p>'
                });
                /* //计算还剩的数量
                 var sum=0;
                 $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
                 var Detailgrid=$("#addDetailgrid").getRowData(value);
                 var inQty=Detailgrid.inQty;
                 var outQty=Detailgrid.outQty;
                 var outMonyQty=Detailgrid.outMonyQty;
                 sum+=parseInt(inQty)-parseInt(outQty)-parseInt(outMonyQty);
                 });*/

                var ajax_url;
                var ajax_data;
                /* if (taskType === -1) {
                 ajax_url = basePath + "/stock/warehStock/inCheckEpcStock.do";
                 ajax_data = {warehId: wareHouse, code: code}
                 } else {
                 ajax_url = basePath + "/stock/warehStock/checkEpcStock.do";
                 ajax_data = {warehId: wareHouse, code: code, type: taskType};
                 }*/
                ajax_url = basePath + "/logistics/inventoryBillController/checkinventoryEpcStock.do";
                ajax_data = { code: code,ownerId: ownerId};
                $.ajax({
                    async: false,
                    url: ajax_url,
                    data: ajax_data,
                    datatype: "json",
                    type: "POST",
                    success: function (data) {
                        if (data.success) {
                            /*if (skuValid(data.result)) {*/
                            $("#inventoryCodeGrid").addRowData($('#inventoryCodeGrid').getDataIDs().length, data.result, 'first');
                            allCodes = allCodes + "," + code;
                            var scanCodeQty = $('#inventoryCodeGrid').getDataIDs().length;
                            $("#codeQtys").val(scanCodeQty);
                            /*}*/
                        } else {
                            $.gritter.add({
                                text: data.msg,
                                class_name: 'gritter-success  gritter-light'
                            });
                        }
                        $('#inventoryCode-editForm').clearForm();
                        progressDialog.modal('hide');
                    }
                });
            }
        })
    }

    /*   //billDtl页面编辑进入时，销售出库和入库按钮的校验，判断当前code的SKU是否可以出入库
     function skuValid(result) {
     if (inOntWareHouseValid === 'wareHouseOut_valid') {//出库，edit_wareHouseOut()中对参数赋值
     var returnValue = true;
     var epcSkuInDtl;
     $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
     epcSkuInDtl = false;
     var rowData = $("#addDetailgrid").getRowData(value);
     var currentSku = rowData.sku;
     if (currentSku === result.sku) {//判断该code的SKU是否在billDtl中
     epcSkuInDtl = true;
     var returnQty = rowData.returnQty;
     if (rowData.returnQty === null || rowData.returnQty === "" || rowData.returnQty === undefined) {
     returnQty = 0;
     }
     if (rowData.qty < parseInt(skuQty[currentSku]) + parseInt(returnQty) + 1) {//如果billDtl中的SKU已全部出库
     $.gritter.add({
     text: "SKU: " + result.sku + " 出库数量超出单据数量，不能添加",
     class_name: 'gritter-success  gritter-light'
     });
     returnValue = false;
     } else {
     skuQty[currentSku] = parseInt(skuQty[currentSku]) + 1;
     }
     return false;
     }
     });
     if (epcSkuInDtl === false) {//billDtl中没有该code的SKU
     $.gritter.add({
     text: "SKU: " + result.sku + " 不在当前单据中",
     class_name: 'gritter-success  gritter-light'
     });
     returnValue = false;
     return false;
     }
     return returnValue;
     } else if (inOntWareHouseValid === 'wareHouseIn_valid') {//入库， wareHouseIn()中对参数赋值
     if (allCodeStrInDtl.indexOf(result.code) === -1) {
     $.gritter.add({
     text: "唯一码: " + result.code + " 不在当前单据中",
     class_name: 'gritter-success  gritter-light'
     });
     return false;
     } else {
     return true;
     }
     } else if (inOntWareHouseValid === 'addPage_scanUniqueCode') {
     if (allCodeStrInDtl.indexOf(result.code) !== -1) {
     $.gritter.add({
     text: "唯一码: " + result.code + " 已在当前单据中，请不要重复添加",
     class_name: 'gritter-success  gritter-light'
     });
     return false;
     } else {
     return true;
     }
     } else {
     return true;
     }
     }*/
</script>
