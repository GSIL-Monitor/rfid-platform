<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/7/12
  Time: 15:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<div id="gathering_dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                收付款
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="gatheringForm">
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_guestId">
                            <span class="text-danger">* </span>客户编号</label>

                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="form_guestId" name="customsId" type="text" readonly/>
                        </div>
                    </div>
                    <%--<div class="form-group">--%>
                        <%--<label class="col-sm-2 control-label no-padding-right" for="search_guestName">--%>
                            <%--<span class="text-danger">* </span>客户名</label>--%>

                        <%--<div class="col-xs-10 col-sm-5">--%>
                            <%--<input class="form-control" id="search_guestName" name="customsName" type="text"/>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right">
                            <span class="text-danger">* </span>客户欠款</label>

                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="form_owingValue" name="owingValue"
                                   readOnly type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_billDate">
                            <span class="text-danger">* </span>交易时间</label>

                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="form_billDate" name="billDate"
                                   type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_billType">
                            <span class="text-danger">* </span>交易类型</label>

                        <div class="col-xs-10 col-sm-5">
                            <select class="form-control" id="form_billType" name="billType">
                                <option value="">--请选择--</option>
                                <option selected="selected" value=0>收款</option>
                                <option value=1>储值</option>
                                <option value=2>付款</option>
                            </select>
                        </div>

                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_payType">
                            <span class="text-danger">* </span>支付方式</label>

                        <div class="col-xs-10 col-sm-5">
                            <select class="form-control" id="form_payType" name="payType">
                                <option value="">--请选择--</option>
                                <option value="xianjinzhifu">现金支付</option>
                                <option value="zhifubaozhifu">支付宝支付</option>
                                <option value="wechatpay">微信支付</option>
                                <option value="cardpay">刷卡支付</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_payPrice">
                            <span class="text-danger">* </span>交易金额</label>

                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="form_payPrice" name="payPrice"
                                   type="number" step="0.01"/>
                        </div>
                    </div>
                    <div class="form-group hidden" id="donation">
                        <label class="col-sm-2 control-label no-padding-right" for="form_donationPrice">
                            <span class="text-danger">* </span>赠送金额</label>

                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="form_donationPrice" name="donationPrice"
                                   type="number" step="0.01"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_remark">备注</label>

                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="form_remark" name="remark"
                                   type="text">
                        </div>
                    </div>
                </form>

            </div>
        </div>
        <div class="modal-footer">

            <a href="#" class="btn" onclick="closeGatheringDialog()">关闭</a>

            <button type="button" class="btn btn-primary" onclick="saveGathering()">保存</button>

        </div>
    </div>
</div>
<script>

    function saveGathering() {

        $('#gatheringForm').data('bootstrapValidator').validate();
        if (!$('#gatheringForm').data('bootstrapValidator').isValid()) {
            return;
        }
        var progressDialog = bootbox.dialog({
            message: '<p><i class="fa fa-spin fa-spinner"></i> 数据上传中...</p>'
        });
        $.post(basePath + "/logistics/paymentGatheringBill/gatheringSave.do",
            $("#gatheringForm").serialize(),
            function (result) {
                if (result.success == true || result.success == 'true') {
                    progressDialog.modal('hide');
                    $("#gathering_dialog").modal('hide');
                    $("#grid").trigger("reloadGrid");
                }
            }, 'json');
    }
    function closeGatheringDialog() {
        $("#gathering_dialog").modal('hide');
    }

    $(function () {
        $("#gathering_dialog").on('show.bs.modal', function () {
            initGatheringFormValid();
            $("#form_payType").val(defaultPayType);
        });

        $("#form_billType").bind('change',function () {
           if($("#form_billType").val() == '1'){
               $("#donation").removeClass("hidden");
           }
           else {
               $("#donation").addClass("hidden");
           }
        });
    });

    function initGatheringFormValid() {
        $('#gatheringForm').bootstrapValidator({
            message: '输入值无效',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            submitHandler: function (validator, form, submitButton) {
                $.post(form.attr('action'), form.serialize(), function (result) {
                    if (result.success == true || result.success == 'true') {

                    } else {
                        $('#gatheringForm').bootstrapValidator('disableSubmitButtons', false);
                    }
                }, 'json');
            },
            excluded:[":disabled",":hidden"],
            fields: {
                payPrice: {
                    validators: {
                        notEmpty: {
                            message: '金额不能为空'
                        }
                    }
                },
                billType: {
                    validators: {
                        notEmpty: {
                            message: '交易类型不能为空'
                        }
                    }
                }
            }
        });
    }

</script>

