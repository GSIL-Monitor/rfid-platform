<%@ page pageEncoding="UTF-8" import="java.util.*" language="java" %>
<div class="modal fade" id="edit_dialog" tabindex="-1" role="doalog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                员工信息
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="editForm">
                    <div class="form-group" id="codeGroup">
                        <label class="col-sm-2 text-right" for="form_code">编号</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="code" id="form_code" type="text" readonly/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 text-right" for="form_name">姓名</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="name" id="form_name" type="text"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 text-right" for="form_ownerId">所属部门</label>
                        <div class="col-sm-7">
                            <select class="chosen-select form-control" name="ownerId" id="form_ownerId">
                                <option value="">-请选择-</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 text-right" for="form_tel">电话</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="tel" id="form_tel" type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 text-right" for="form_email">邮箱</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="email" id="form_email" type="text"/>
                        </div>
                    </div>
                    <div class="form-group" id="createTimeGroup">
                        <label class="col-sm-2 text-right" for="form_createTime">创建时间</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="createTime" id="form_createTime" type="text" readonly/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 text-right" for="form_remark">备注</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="remark" id="form_remark" type="text"/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="modal-footer">

            <a href="#" class="btn" onclick="closeEditDialog()">关闭</a>

            <button type="button" href="#" class="btn btn-primary" onclick="save()">保存</button>

        </div>
    </div>
</div>
<script>
    $(function(){
        $("#edit_dialog").on('show.bs.modal', function () {
            initEditFormValid();
            $("#editForm").resetForm();
        });
        $("#edit_dialog").on('hide.bs.modal',function(){
            $("#editForm").data('bootstrapValidator').destroy();
            $('#editForm').data('bootstrapValidator', null);
            initEditFormValid();
        });
    });

    function closeEditDialog(){
        $("#edit_dialog").modal("hide");
    }

    function initEditFormValid(){
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
                        // Enable the submit buttons
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
                ownerId:{
                    validators: {
                        notEmpty: {
                            message: '所属部门不能为空'
                        }
                    }
                },
                tel: {
                    validators: {
                        stringLength: {
                            min: 11,
                            max: 11,
                            message: '请输入11位手机号码'
                        },
                        regexp: {
                            regexp: /^1[3|5|8]{1}[0-9]{9}$/,
                            message: '请输入正确的手机号码'
                        }
                    }
                } ,
                email:{
                    validators:{
                        emailAddress: {
                            message: '邮箱地址格式有误'
                        }
                    }
                }
            }
        });
    }
</script>
