<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>

<div id="edit_vipCard_dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                详细信息
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="editVipCardForm">
                    <input id="id" name="id" type="hidden">
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right">会员名:</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="name" name="name"
                                   type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><span class="text-danger"></span>会员等级:</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="rank" name="rank"
                                   type="number"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><span class="text-danger"></span>折扣:</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="discount" name="discount"
                                   type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><span class="text-danger"></span>是否包邮:</label>
                        <div class="col-xs-10 col-sm-5">
                            <select class="chosen-select form-control" id="freeShipping" name="freeShipping">
                                <option value="1">包邮</option>
                                <option value="0">不包邮</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right">升级类型:</label>
                        <div class="col-xs-10 col-sm-5">
                            <select class="chosen-select form-control" id="upgradeType" name="upgradeType">
                                <option value="1">自动升级</option>
                                <option value="0">手动升级</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right">升级规则&成交数量:</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="upgradeDealNo" name="upgradeDealNo"
                                   type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right">升级规则&总消费金额:</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="upgradeConsumeNo" name="upgradeConsumeNo"
                                   type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right">升级规则&总累计积分:</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="upgradePoints" name="upgradePoints"
                                   type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right">备注:</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="remark" name="remark"
                                   type="text"/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn" onclick="closeEditDialog()">关闭</a>
            <button type="button"  class="btn btn-primary" onclick="save()">保存</button>
        </div>
    </div>
</div>
<script>
    function save() {
        $('#editVipCardForm').data('bootstrapValidator').validate();
        if(!$('#editVipCardForm').data('bootstrapValidator').isValid()){
            return ;
        }
        cs.showProgressBar();
        var  formData = $("#editVipCardForm").serialize();
        $.post( basePath + "/shop/vipCard/save.do?pageType="+pageType,
            $("#editVipCardForm").serialize(),
            function(result) {
                cs.closeProgressBar();
                if(result.success == true || result.success == 'true') {
                    $.gritter.add({
                        text: result.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                    _search();
                } else {
                    cs.showAlertMsgBox(result.msg);
                }
            }, 'json');
        $("#edit_vipCard_dialog").modal('hide');
    }
    function closeEditDialog() {
        $("#edit_vipCard_dialog").modal('hide');
    }
    $(function() {
        $("#edit_vipCard_dialog").on('show.bs.modal', function () {
            initEditFormValid();
        });
        $("#edit_vipCard_dialog").on('hide.bs.modal', function () {
            $("#editVipCardForm").data('bootstrapValidator').destroy();
            $('#editVipCardForm').data('bootstrapValidator', null);
        });
    });

    function initEditFormValid() {
        $('#editVipCardForm').bootstrapValidator({
            message: '输入值无效',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            submitHandler: function(validator, form, submitButton) {
                $.post(form.attr('action'), form.serialize(), function(result) {
                    if (result.success == true || result.success == 'true') {
                    } else {
                        $('#editVipCardForm').bootstrapValidator('disableSubmitButtons', false);
                    }
                }, 'json');
            },
            fields: {
                name: {
                    validators: {
                        notEmpty: {
                            message: '名称不能为空'
                        }
                    }
                },
                rank: {
                    validators: {
                        notEmpty: {
                            message: '等级不能为空'
                        },
                        regexp: {
                            regexp: /^([1-9]|10)$/,
                            message: '请输入1-9的数字'
                        }
                    }
                },
                discount: {
                    validators: {
                        notEmpty: {
                            message: '折扣不能为空'
                        },
                        regexp: {
                            regexp: /^[0-9]{1}\d*(\.\d{1,2})?$/,
                            message: '请输入只有两位小数的数字'
                        }
                    }
                }
            }
        });
    }
</script>
