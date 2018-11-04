<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<div id="edit-dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                编辑
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="editForm">
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_name">成分名称</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_name" name="name"
                                   type="text" placeholder=""/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_parentId">父类成分名称</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_parentId" name="parentId" readonly
                                   type="text" placeholder=""/>
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
    $(function () {
        $("#edit-dialog").on('show.bs.modal', function () {   //在调用show方法后触发
            initEditFormValid();          //执行一些动作
        });
        $("#edit-dialog").on('show.bs.modal', function () {
            $("#editForm").data('bootstrapValidator').destroy();
            $('#editForm').data('bootstrapValidator', null);
           initEditFormValid();
        });
    });

    function initEditFormValid() {
        $('#editForm').bootstrapValidator({      //数据验证
            message: '输入值无效',
            feedbackIcons: {       //只是一个样式表，显示验证成功或者失败时的小图标
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
                name: {
                    validators: {
                        notEmpty: {
                            message: '成分名称不能为空'
                        }
                    }
                }

            }
        });
    }

    function closeEditDialog() {
        $("#edit-dialog").modal('hide');
    }

</script>
