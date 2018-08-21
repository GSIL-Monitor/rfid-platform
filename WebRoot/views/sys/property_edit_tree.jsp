<%--
  Created by IntelliJ IDEA.
  User: MMI
  Date: 2018/8/20
  Time: 20:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<div id="edit-dialog-tree" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                属性明细
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="editFormTree">
                    <input id="form_tree_parentId" name="parentId" hidden>
                    <input id="form_tree_id" name="id" hidden>
                    <input id="form_tree_code" name="code" hidden>
                    <input id="form_tree_type" name="type" hidden>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_tree_name">名称</label>
                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_tree_name" name="name"
                                   type="text" placeholder=""/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_tree_ynuse">是否启用</label>
                        <div class="col-xs-14 col-sm-7">
                            <select class="form-control" type="text" id="form_tree_ynuse" name='ynuse'>
                                <option value="Y" selected>是</option>
                                <option value="N">否</option>
                            </select>
                        </div>
                    </div>
                </form>

            </div>
        </div>
        <div class="modal-footer">

            <a href="#" class="btn" onclick="closeEditDialogde()">关闭</a>

            <button type="button" href="#" class="btn btn-primary" onclick="savePropertyInTree()">保存</button>

        </div>
    </div>
</div>
<script>
    $(function () {
        $("#edit-dialog-tree").on('show.bs.modal', function () {   //在调用show方法后触发

            initTreeEditFormValidtype();          //执行一些动作
        });
        $("#edit-dialog-tree").on('show.bs.modal', function () {
            $("#editFormTree").data('bootstrapValidator').destroy();
            $('#editFormTree').data('bootstrapValidator', null);
            initTreeEditFormValidtype();
        });
    });

    function initTreeEditFormValidtype() {
        $('#editFormTree').bootstrapValidator({      //数据验证
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
                        $('#editFormTree').bootstrapValidator('disableSubmitButtons', false);
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
            }
        });
    }

    function closeEditDialogde() {
        $("#edit-dialog-tree").modal('hide');
    }
</script>
