<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/10/24
  Time: 13:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<div id="exchangeGoods-dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                换货
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">

                <div class="row">
                    <form class="form-horizontal" id="exchangeGoods-Form">
                        <div class="form-group">
                            <label class="col-xs-2 col-sm-2 col-md-2 col-lg-2 control-label no-padding-right"
                                   for="orig_code">本单商品</label>

                            <div class="col-xs-5 col-sm-5 col-md-5 col-lg-5">
                                <input class="form-control" id="orig_code" name="orig_code" type="text"
                                       placeholder="原唯一码，按回车键结束"/>
                            </div>
                            <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
                                <input class="form-control" id="orig_sku" name="orig_sku" type="text"
                                       placeholder="SKU" readonly/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-2 col-sm-2 col-md-2 col-lg-2 control-label no-padding-right"
                                   for="exchange_code">替换商品</label>

                            <div class="col-xs-5 col-sm-5 col-md-5 col-lg-5">
                                <input class="form-control" id="exchange_code" name="exchange_code" type="text"
                                       placeholder="换货唯一码，按回车键结束"/>
                            </div>
                            <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4">
                                <input class="form-control" id="exchange_sku" name="exchange_sku" type="text"
                                       placeholder="SKU" readonly/>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="hr hr4"></div>
        </div>
        <div class="modal-footer">
            <div id="dialog_buttonGroup">
                <button id='closeExchangeDialog' type='button' style='margin-left: 20px' class='btn btn-sm btn-grey'
                        onclick='close_exchangeDialog()'>
                    <span class='bigger-110'>取消</span>
                </button>
                <button id='clearExchangeForm' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary'
                        onclick='clear_exchangeForm()'>
                    <span class='bigger-110'>清空替换信息</span>
                </button>
                <button id='confirmExchange' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary'
                        onclick='exchangeConfirm()'>
                    <span class='bigger-110'>确认换货</span>
                </button>
            </div>
        </div>
    </div>
</div>

<script>
    $(function () {
        initUniqueCodeGrid();
        exchange_keyDown();
    });


    $(function () {
        $("#exchangeGoods-dialog").on('show.bs.modal', function () {
            $('#exchangeGoods-Form').clearForm();
            $('#exchangeGoods-Form').resetForm();
        });
    });


    function exchange_keyDown() {
        $("#orig_code").keydown(function (event) {
            if (event.keyCode === 13) {
                var code = $("#orig_code").val();
                if(code === null || code === undefined || code === ""){
                    return;
                }
                $("#exchange_code").val("");
                $("#exchange_sku").val("");
                $.ajax({
                    async: false,
                    url: basePath + "/stock/warehStock/searchCodeForExchange.do",
                    data: {
                        code: code,
                        warehouseId: wareHouse
                    },
                    datatype: "json",
                    type: "POST",
                    success: function (data) {
                        if (data.success) {
                            if(allCodeStrInDtl.indexOf(data.result.code) !== -1){
                                if(data.result.inStock === 0){
                                    $("#orig_code").val(data.result.code);
                                    $("#orig_sku").val(data.result.sku);
                                }else {
                                    $.gritter.add({
                                        text: "该商品尚未出售",
                                        class_name: 'gritter-success  gritter-light'
                                    });
                                    $("#orig_code").val("");
                                    $("#orig_sku").val("");
                                }
                            }else {
                                $.gritter.add({
                                    text: "唯一码不在当前单据中",
                                    class_name: 'gritter-success  gritter-light'
                                });
                                $("#orig_code").val("");
                                $("#orig_sku").val("");
                            }
                        } else {
                            $.gritter.add({
                                text: data.msg,
                                class_name: 'gritter-success  gritter-light'
                            });
                        }
                    }
                });
            }
        });

        $("#exchange_code").keydown(function (event) {
            if (event.keyCode === 13) {
                var code = $("#exchange_code").val();
                if(code === null || code === undefined || code === ""){
                    return;
                }
                $.ajax({
                    async: false,
                    url: basePath + "/stock/warehStock/checkEpcStock.do",
                    data: {
                        warehId: wareHouse,
                        code: code,
                        type: 0,
                        billNo: billNo
                    },
                    datatype: "json",
                    type: "POST",
                    success: function (data) {
                        debugger;
                        if (data.success) {
                            $("#exchange_code").val(data.result.code);
                            $("#exchange_sku").val(data.result.sku);
                        } else {
                            $.gritter.add({
                                text: data.msg,
                                class_name: 'gritter-success  gritter-light'
                            });
                        }
                    }
                });
            }
        })
    }

    function exchangeConfirm() {
        var origCode = $("#orig_code").val();
        var exchangeCode = $("#exchange_code").val();
        var origSku = $("#orig_sku").val();
        var exchangeSku = $("#exchange_sku").val();
        var billNo = $("#edit_billNo").val();

        if(origSku === exchangeSku &&
            exchangeSku && exchangeSku !== null){
            $.ajax({
                async: false,
                url: basePath + "/logistics/saleOrderBill/confirmExchange.do",
                data: {
                    origCode: origCode,
                    exchangeCode: exchangeCode,
                    origSku: origSku,
                    exchangeSku: exchangeSku,
                    billNo: billNo
                },
                datatype: "json",
                type: "POST",
                success: function (data) {
                    if (data.success) {
                        $.gritter.add({
                            text: data.msg,
                            class_name: 'gritter-success  gritter-light'
                        });
                        close_exchangeDialog();
                    } else {
                        $.gritter.add({
                            text: data.msg,
                            class_name: 'gritter-success  gritter-light'
                        });
                    }
                }
            });
        }else {
            $.gritter.add({
                text: "替换商品的SKU必须和原款一致",
                class_name: 'gritter-success  gritter-light'
            });
        }
    }

    function close_exchangeDialog() {
        $("#exchangeGoods-dialog").modal('hide');
    }

    function clear_exchangeForm() {
        $("#exchange_code").val("");
        $("#exchange_sku").val("");
    }

</script>