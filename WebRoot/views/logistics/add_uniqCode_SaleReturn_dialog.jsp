<%--
  Created by IntelliJ IDEA.
  User: Anna
  Date: 18/4/22
  Time: 13:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>



<div id="add-uniqCode-dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" style="width:950px;align-content: center;">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                添加唯一码
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">


                <div class="row">
                    <form class="form-horizontal" role="search" id="uniqCode-editForm">
                        <label class="col-sm-2 control-label no-padding-right"
                               for="add_uniqueCode">唯一码</label>
                        <div class="col-xs-12 col-sm-9">
                            <input class="form-control" id="add_uniqueCode" name="add_uniqueCode" type="text"
                                   placeholder="按回车键结束" style="ime-mode:active"/>
                        </div>

                    </form>
                </div>
            </div>
            <div class="hr hr4"></div>
            <table id="uniqueCodeGrid"></table>
        </div>
        <div class="modal-footer">
            <%--<label class="control-label col-sm-3" for="codeQty" style="text-align: center">已添加数量：</label>--%>
            <%--<div class="col-sm-1">--%>
            <%--<input class="form-control" type="text" id="codeQty"--%>
            <%--style="float: left;background-color: transparent;border: none" readonly value="0">--%>
            <%--</div>--%>

            <div class="col-lg-4">
                <span>已添加数量：</span>
                <span id="codeQty">0</span>
            </div>

            <div id="dialog_buttonGroup"></div>
        </div>
    </div>
</div>



<script>



    $(function () {
        initUniqueCodeGrid();
    });

    function initUniqueCodeGrid() {
        $("#uniqueCodeGrid").jqGrid({
            height: 400,
            datatype: "local",
            colModel: [
                {name: 'code', label: '唯一码', width: 100},
                {name: 'updateTime', label: '修改时间', hidden: true, width: 40},
                {name: 'styleId', label: '款号', width: 40, hidden: true},
                {name: 'colorId', label: '色码', width: 40, hidden: true},
                {name: 'sizeId', label: '尺码', width: 40, hidden: true},
                {name: 'sku', label: 'SKU', width: 120},
                {name: 'styleName', label: '款式', width: 40, hidden: true},
                {name: 'colorName', label: '颜色', width: 40, hidden: true},
                {name: 'sizeName', label: '尺寸', width: 40, hidden: true},
                {name: 'price', label: '销售价格', width: 100},                  //吊牌价格
                {name: 'preCast', label: '采购价', width: 40, hidden: true},  //事前成本价(采购价)
                {name: 'wsPrice', label: '销售价格', width: 40, hidden: true},  //门店批发价格
                {name: 'puPrice', label: '销售价格', width: 40, hidden: true},  //代理商批发价格
                {name: 'stockPrice', label: '库存金额', width: 40, hidden: true}, //库存金额
                /* add by Anna */
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
            sortname: 'lastSaleTime',
            sortorder: "desc"

        });
        var parent_column = $("#uniqueCodeGrid").closest('.modal-dialog');
        $("#uniqueCodeGrid").jqGrid('setGridWidth', parent_column.width() - 5);
    }

    function addCellAttr(rowId, val, rawObject, cm, rdata) {
        if (rawObject.saleCycle >= 20) {
            return "style='color:red'";
        }
    }



    function initUniqeCodeGridColumn(storeType) {
        if (storeType == "CT-AT") {//代理商
            $("#uniqueCodeGrid").setGridParam().hideCol("preCast");
            $("#uniqueCodeGrid").setGridParam().hideCol("wsPrice");
            $("#uniqueCodeGrid").setGridParam().hideCol("price");
            $("#uniqueCodeGrid").setGridParam().hideCol("stockPrice");
            $("#uniqueCodeGrid").setGridParam().showCol("puPrice").trigger("reloadGrid");
        } else if (storeType == "CT-ST") {//门店
            $("#uniqueCodeGrid").setGridParam().hideCol("preCast");
            $("#uniqueCodeGrid").setGridParam().hideCol("puPrice");
            $("#uniqueCodeGrid").setGridParam().hideCol("price");
            $("#uniqueCodeGrid").setGridParam().hideCol("stockPrice");
            $("#uniqueCodeGrid").setGridParam().showCol("wsPrice").trigger("reloadGrid");
        } else if (storeType == "CT-LS") {//零售客户
            $("#uniqueCodeGrid").setGridParam().hideCol("preCast");
            $("#uniqueCodeGrid").setGridParam().hideCol("puPrice");
            $("#uniqueCodeGrid").setGridParam().hideCol("wsPrice");
            $("#uniqueCodeGrid").setGridParam().hideCol("stockPrice");
            $("#uniqueCodeGrid").setGridParam().showCol("price").trigger("reloadGrid");
        } else if (storeType == "showPrecast") {//采购价
            $("#uniqueCodeGrid").setGridParam().hideCol("price");
            $("#uniqueCodeGrid").setGridParam().hideCol("puPrice");
            $("#uniqueCodeGrid").setGridParam().hideCol("wsPrice");
            $("#uniqueCodeGrid").setGridParam().hideCol("stockPrice");
            $("#uniqueCodeGrid").setGridParam().showCol("preCast").trigger("reloadGrid");
        } else if (storeType == "showStockPrice") {
            $("#uniqueCodeGrid").setGridParam().hideCol("price");
            $("#uniqueCodeGrid").setGridParam().hideCol("puPrice");
            $("#uniqueCodeGrid").setGridParam().hideCol("wsPrice");
            $("#uniqueCodeGrid").setGridParam().hideCol("preCast");
            $("#uniqueCodeGrid").setGridParam().hideCol("stockPrice").trigger("reloadGrid");
        }
    }

    $(function () {
        $("#add-uniqCode-dialog").on('shown.bs.modal', function () {
            initCodeEditFormValid();
            $('#uniqCode-editForm').clearForm();
            $('#uniqCode-editForm').resetForm();
            $("#addUniqcodeGrid").jqGrid("clearGridData");
            $("#add_uniqueCode").focus();
        });
    });

    function initCodeEditFormValid() {
        $('#uniqCode-editForm').bootstrapValidator({
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

    function keydown() {
        //监听回车键
        $("#add_uniqueCode").keydown(function (event) {
            if (event.keyCode == 13) {
                $('#uniqCode-editForm').data('bootstrapValidator').validate();
                if (!$('#uniqCode-editForm').data('bootstrapValidator').isValid()) {
                    return;
                }
                var code = document.getElementById("add_uniqueCode").value;

                //每次添加，拼接code，用于判断当前添加是否重复
                if (allCodes.indexOf(code) != -1) {
                    $('#uniqCode-editForm').clearForm();
                    $.gritter.add({
                        text: "不能重复添加",
                        class_name: 'gritter-success  gritter-light'
                    });
                    return;
                }

                var progressDialog = bootbox.dialog({
                    message: '<p><i class="fa fa-spin fa-spinner"></i>checking...</p>'
                });


                var ajax_url;
                var ajax_data;
                if (taskType === -1) {
                    ajax_url = basePath + "/stock/warehStock/inCheckEpcStockAndFindDate.do";
                    ajax_data = {warehId: wareHouse, code: code, billNo: billNo};
                } else {
                    ajax_url = basePath + "/stock/warehStock/checkEpcStockAndFindDate.do";
                    ajax_data = {warehId: wareHouse, code: code, type: taskType, billNo: billNo};
                }
                $.ajax({
                    async: false,
                    url: ajax_url,
                    data: ajax_data,
                    datatype: "json",
                    type: "POST",
                    success: function (data) {
                        if (data.success) {
                            if (skuValid(data.result)) {
                                $("#uniqueCodeGrid").addRowData($('#uniqueCodeGrid').getDataIDs().length, data.result, 'first');
                                allCodes = allCodes + "," + code;
                                var scanCodeQty = $('#uniqueCodeGrid').getDataIDs().length;
                                $("#codeQty").text(scanCodeQty);
                            }
                        } else {
                            $.gritter.add({
                                text: data.msg,
                                class_name: 'gritter-success  gritter-light'
                            });
                        }
                        $('#uniqCode-editForm').clearForm();
                        progressDialog.modal('hide');
                    }
                });
            }
        })
    }

    //billDtl页面编辑进入时，销售出库和入库按钮的校验，判断当前code的SKU是否可以出入库
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
    }
</script>
