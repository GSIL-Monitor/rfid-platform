<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<div id="edit-dialog" class="modal fade" tabindex="-1" role="dialog">
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
                <form class="form-horizontal" role="form" id="editForm">
                    <input type="hidden" id="form_id"name="id" />

                    <input type="hidden" id="form_templateid" name='templateid' hidden="true">
                    <%--<input type="hidden" id="form_creatorTime" name='createTime' hidden="true">--%>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_title">模板标题</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_title" name="title" type="text"
                                   placeholder="" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_content">模板内容</label>

                        <div class="col-xs-14 col-sm-7">
                            <%--<input class="form-control" id="form_content" name="content"

                                   type="text" placeholder=""/>--%>
                           <textarea class="form-control" id="form_content" name="content"

                                      type="text" placeholder=""></textarea>
                        </div>
                    </div>

                    <%--<div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_password">密码</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_password" name="password"
                                   type="password" placeholder=""/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_ownerId">所属方</label>

                        <div class="col-xs-14 col-sm-7">
                            <div class="input-group">
                                <input class="form-control" id="form_ownerId"
                                       type="text" name="ownerId" readonly/>
                                <span class="input-group-btn">
                                    <button class="btn btn-sm btn-default" type="button" onclick="openUnitDialog('#form_ownerId','#form_unitName',null,'withShop')">
                                        <i class="ace-icon fa fa-list"></i>
                                    </button>
								</span>
                                <input class="form-control" id="form_unitName"
                                       type="text" name="unitName" readonly/>
                            </div>
                        </div>

                    </div>
                    <!-- #section:elements.form -->
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_roleId">角色</label>

                        <div class="col-xs-14 col-sm-7">
                            <select class="chosen-select form-control" id="form_roleId" name="roleId">

                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_isAdmin">管理员</label>

                        <div class="col-xs-14 col-sm-7">
                            <select class="chosen-select form-control" id="form_isAdmin" name="isAdmin">
                                <option value="0">否</option>
                                <option value="1">是</option>
                            </select>
                        </div>

                    </div>--%>

                </form>

            </div>
        </div>
        <div class="modal-footer">

            <a href="#" class="btn" onclick="closeEditDialog()">关闭</a>

            <button id="saveButton" type="button"  class="btn btn-primary" onclick="save()">保存</button>

        </div>
    </div>
</div>
<script>
   /* $(function() {
        $("#edit-dialog").on('show.bs.modal', function () {

            initEditFormValid();
        });
        $("#edit-dialog").on('hide.bs.modal', function () {
            $("#editForm").data('bootstrapValidator').destroy();
            $('#editForm').data('bootstrapValidator', null);
            initEditFormValid();
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
    }*/
   $(function() {

   });
   function save(){
       /*$('#editForm').data('bootstrapValidator').validate();
        if(!$('#editForm').data('bootstrapValidator').isValid()){
        return ;
        }*/
       debugger;
       var progressDialog = bootbox.dialog({
           message: '<p><i class="fa fa-spin fa-spinner"></i> 数据上传中...</p>'
       });
       $.post(basePath+"/sys/SMSModule/save.do",
           $("#editForm").serialize(),
           function(result) {
               debugger;
               if(result.success == true || result.success == 'true') {
                   progressDialog.modal('hide');
                   $("#edit-dialog").modal('hide');
                   $("#grid").trigger("reloadGrid");
                   $.gritter.add({
                       text: result.msg,
                       class_name: 'gritter-success  gritter-light'
                   });
               }else{
                   bootbox.alert(result.msg);
               }
           }, 'json');
   }
</script>
