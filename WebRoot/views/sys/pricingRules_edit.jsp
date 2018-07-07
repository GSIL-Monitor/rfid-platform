<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>

<div id="edit_pricingRules_dialog" class="modal fade" tabindex="-1" role="dialog">
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
                <form class="form-horizontal" role="form" id="editPricingRulesForm">

                    <input id="id" name="id" type="hidden">

                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right">定价规则名</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="name" name="name"
                                   type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="rule1_name"><span class="text-danger"></span>吊牌价与采购价的关系</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="rule1_name" name="rule1"
                                   type="text"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="rule2_name"><span class="text-danger"></span>吊牌价与门店价的关系</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="rule2_name" name="rule2"
                                   type="text"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="rule3_name"><span class="text-danger"></span>吊牌价与代理价的关系</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="rule3_name" name="rule3"
                                   type="text"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_series">系列</label>
                        <div class="col-xs-10 col-sm-5">
                            <select class="chosen-select form-control" id="form_series" name="series">
                                <option value="">请选择系列</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_class3">大类</label>
                        <div class="col-xs-10 col-sm-5">
                            <select class="chosen-select form-control" id="form_class3" name="class3">
                                <option value="">请选择大类</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <input id="form_state" name="state" hidden="hidden"/>
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
        $('#editPricingRulesForm').data('bootstrapValidator').validate();
        if(!$('#editPricingRulesForm').data('bootstrapValidator').isValid()){
            return ;
        }
        $("#form_series").removeAttr("disabled");
        cs.showProgressBar();
        var  formData = $("#editPricingRulesForm").serialize();
        $.post( basePath + "/sys/pricingRules/save.do?pageType="+pageType,
            $("#editPricingRulesForm").serialize(),
            function(result) {
                cs.closeProgressBar();
                if(result.success == true || result.success == 'true') {
                    $("#edit_pricingRules_dialog").modal('hide');
                    refresh();
                } else {
                    $.gritter.add({
                        text: result.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                }
            }, 'json');
    }
    function closeEditDialog() {
        $("#edit_pricingRules_dialog").modal('hide');
    }
    $(function() {
        $("#edit_pricingRules_dialog").on('show.bs.modal', function () {
            initEditFormValid();

        });
        $("#edit_pricingRules_dialog").on('hide.bs.modal', function () {
            $("#editPricingRulesForm").data('bootstrapValidator').destroy();
            $('#editPricingRulesForm').data('bootstrapValidator', null);
        });
    });

    function initEditFormValid() {
        $('#editPricingRulesForm').bootstrapValidator({
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
                        $('#editPricingRulesForm').bootstrapValidator('disableSubmitButtons', false);
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
                rule1: {
                    validators: {
                        notEmpty: {
                            message: '吊牌价与采购价的关系不能为空'
                        },
                        regexp: {
                            regexp: /^[0-9]{1}\d*(\.\d{1,2})?$/,
                            message: '请输入只有两位小数的数字'
                        }
                    }
                },
                rule2: {
                    validators: {
                        notEmpty: {
                            message: '吊牌价与门店价的关系不能为空'
                        },
                        regexp: {
                            regexp: /^[0-9]{1}\d*(\.\d{1,2})?$/,
                            message: '请输入只有两位小数的数字'
                        }
                    }
                },
                rule3: {
                    validators: {
                        notEmpty: {
                            message: '吊牌价与代理价的关系不能为空'
                        },
                        regexp: {
                            regexp: /^[0-9]{1}\d*(\.\d{1,2})?$/,
                            message: '请输入只有两位小数的数字'
                        }
                    }
                },
                series: {
                    validators: {
                        notEmpty: {
                            message: '系列不能为空请选择'
                        }
                    }
                },
                class3:{
                    validators: {
                        notEmpty: {
                            message: '大类不能为空请选择'
                        }
                    }
                }
            }
        });
    }
</script>
