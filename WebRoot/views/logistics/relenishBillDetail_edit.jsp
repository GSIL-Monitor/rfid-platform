<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<div id="edit-dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                审核(反)信息
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="editForm">

                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_remarks">原因</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_remarks" name="remark" type="text"
                                   placeholder=""/>
                        </div>
                    </div>


                </form>

            </div>
        </div>
        <div class="modal-footer">

            <a href="#" class="btn" onclick="closeEditDialog()">关闭</a>

            <button id="checkSave" type="button"  class="btn btn-primary" onclick="check()">保存</button>
            <button id="noCheckSave" type="button"  class="btn btn-primary" onclick="noCheck()">保存</button>

        </div>
    </div>
</div>
<script>
    $(function() {
        $("#edit-dialog").on('show.bs.modal', function () {

            //initEditFormValid();
        });
        $("#edit-dialog").on('hide.bs.modal', function () {
           /* $("#editForm").data('bootstrapValidator').destroy();
            $('#editForm').data('bootstrapValidator', null);*/
            //initEditFormValid();
        });
    });

    function initEditFormValid() {
        $('#editForm').bootstrapValidator({
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
                        $('#editForm').bootstrapValidator('disableSubmitButtons', false);
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
                code: {
                    validators: {
                        notEmpty: {
                            message: '登录名不能为空'
                        }
                    }
                },
                password: {
                    validators: {
                        notEmpty: {
                            message: '密码不能为空'
                        }
                    }
                },
                ownerId: {
                    validators: {
                        notEmpty: {
                            message: '所属方不能为空'
                        }
                    }
                },
                roleId: {
                    validators: {
                        notEmpty: {
                            message: '请选择角色'
                        }
                    }
                } ,
                isAdmin: {
                    validators: {
                        notEmpty: {
                            message: '请选择管理员权限'
                        }
                    }
                }
            }
        });
    }

</script>
