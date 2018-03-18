<%@ page pageEncoding="UTF-8" import="java.util.*" language="java" %>
<div class="modal fade" id="edit_User_dialog" tabindex="-1" role="doalog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                设置用户
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="setUserForm">
                    <div class="form-group">
                        <label class="col-sm-2 text-right" for="form_uCode">账号</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="code" id="form_uCode" type="text" onfocus="blur()"/>
                        </div>

                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 text-right" for="form_asuName">用户名</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="name" id="form_asuName" type="text" onfocus="blur()"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 text-right" for="form_uPassword">用户密码</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="password" type="password" id="form_uPassword"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 text-right" for="form_uOwnerName">样衣间</label>
                        <div class="col-sm-7">
                            <select class="form-control chosen-select" name="ownerId" id="form_uOwnerName">
                                <option value="">-请选择-</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group" hidden>
                        <label class="col-sm-2 text-right" for="form_uEmail">邮箱</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="email" id="form_uEmail" type="text"/>
                        </div>

                    </div>
                </form>

            </div>
        </div>
        <div class="modal-footer">

            <a href="#" class="btn" onclick="closeUserEditDialog()">关闭</a>

            <button type="button" href="#" class="btn btn-primary" onclick="saveAsUser()">保存</button>

        </div>
    </div>
</div>

<script>
    $(function(){
        $("#edit_User_dialog").on('show.bs.modal', function () {
            initUEditFormValid();
            $("#setUserForm").resetForm();
        });
        $("#edit_User_dialog").on('hide.bs.modal',function(){
            $("#setUserForm").data('bootstrapValidator').destroy();
            $('#setUserForm').data('bootstrapValidator', null);
            initUEditFormValid();
        });
    });




    function saveAsUser(){
        $('#setUserForm').data('bootstrapValidator').validate();
        if(!$('#setUserForm').data('bootstrapValidator').isValid()){
            return ;
        }
        var progressDialog = bootbox.dialog({
            message: '<p><i class="fa fa-spin fa-spinner"></i> 数据上传中...</p>'
        });
        $.post(basePath+"/hall/employee/setAsUser.do",
            $("#setUserForm").serialize(),
            function (result) {
                if(result.success == true || result.success == 'true') {
                    progressDialog.modal('hide');
                    $.gritter.add({
                        text: result.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                    $("#edit_User_dialog").modal('hide');
                    $("#grid").trigger("reloadGrid");
                }
            },"json"
        );
    }

    function closeUserEditDialog(){
        $("#edit_User_dialog").modal("hide");
    }

    function initUEditFormValid() {
        $('#setUserForm').bootstrapValidator({
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
                            // Enable the submit buttons
                            $('#editForm').bootstrapValidator('disableSubmitButtons', false);
                        }
                    }, 'json');
                },
                fields: {
                    password:{
                        validators:{
                            notEmpty:{
                                message:"不可设置空密码"
                            },
                            stringLength:{
                                min:6,
                                max:16,
                                message:"请输入介于6至16位密码"
                            },
                            regexp:{
                                regexp:/^[0-9a-zA-Z_]+$/,
                                message:"密码只包括数字、字母以及下划线"
                            }
                        },
                    },
                    ownerId:{
                        validators:{
                            notEmpty:{
                                message:"请选择样衣间"
                            }
                        }
                    }

                }
            });
    }
</script>