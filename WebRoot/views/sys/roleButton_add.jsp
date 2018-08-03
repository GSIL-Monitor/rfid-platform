<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2018/4/12
  Time: 17:42
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div id="edit_roleButton_dialog" class="modal fade" tabindex="-1" role="dialog">
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
                <form class="form-horizontal" role="form" id="editRoleButtonForm">
                    <input  id="savecode" name="code" type="hidden"/>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><span class="text-danger"></span>按钮名称</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="privilegeName" name="privilegeName"
                                   type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><span class="text-danger"></span>按钮Id</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="privilegeId" name="privilegeId"
                                   type="text"/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn" onclick="closeEditButtonDialog()">关闭</a>
            <button type="button"  class="btn btn-primary" onclick="saveButton()">保存</button>
        </div>
    </div>
</div>
<script>
    function addButton() {
        $("#editRoleButtonForm").resetForm();
        rowId = $("#resourceGrid").jqGrid("getGridParam", "selrow");
        if (rowId) {
            var row = $("#resourceGrid").jqGrid('getRowData', rowId);
            $("#edit_roleButton_dialog").modal("show");
        } else {
            bootbox.alert("请选择父菜单！");
        }
        //选择父菜单，赋值code
        var so=rowId;
        if (so==""){
            so="01";
        }
        $("#savecode").val(so);
        initEditButtonFormValid();
    }

    function closeEditButtonDialog() {
        closeButtonDialog();
    }

    function closeButtonDialog() {
        $("#edit_roleButton_dialog").modal('hide');
        $("#editRoleButtonForm").resetForm();
        $("#editRoleButtonForm").data('bootstrapValidator').destroy();
        $('#editRoleButtonForm').data('bootstrapValidator', null);

    }



    function saveButton() {

        checkButtonId(checkBack);

    }


    function checkBack(isok) {
        var isok=isok;
        if(!isok){
            return;
        }
        $('#editRoleButtonForm').data('bootstrapValidator').validate();
        if(!$('#editRoleButtonForm').data('bootstrapValidator').isValid()){
            return ;
        }
        /*if($("#privilegeName").val()==""||$("#privilegeName").val()==undefined){
            $.gritter.add({
                text: "按钮名称不能为空",
                class_name: 'gritter-success  gritter-light'
            });
            return
        }
        if($("#privilegeId").val()==""||$("#privilegeId").val()==undefined){
            $.gritter.add({
                text: "按钮ID不能为空",
                class_name: 'gritter-success  gritter-light'
            });
            return
        }*/

       cs.showProgressBar();
        $.ajax({
            dataType:"json",
            url: basePath + "/sys/role/saveResourceButton.do",
            data:{
                roleStr:JSON.stringify(array2obj($("#editRoleButtonForm").serializeArray()))
            },
            type:"POST",
            success:function(result) {
                cs.closeProgressBar();
                if(result.success == true || result.success == 'true') {
                    $.gritter.add({
                        text: result.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                } else {
                    cs.showAlertMsgBox(result.msg);
                }
                closeButtonDialog();
            }
        });

    }
    function checkButtonId(checkBack) {
        cs.showProgressBar();
        $.ajax({
            dataType:"json",
            async: true,
            url: basePath + "/sys/role/checkPrivilegeId.do",
            data:{
                code:$("#code").val(),
                privilegeId:$("#privilegeId").val()
            },
            type:"POST",
            success:function(result) {
                cs.closeProgressBar();
                if(result.success == true || result.success == 'true') {
                    $.gritter.add({
                        text: result.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                    checkBack(true)
                } else {
                    cs.showAlertMsgBox(result.msg);
                    checkBack(false)
                }
            }
        });
    }
    function initEditButtonFormValid() {
        $('#editRoleButtonForm').bootstrapValidator({
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
                        $('#editRoleButtonForm').bootstrapValidator('disableSubmitButtons', false);
                    }
                }, 'json');
            },
            fields: {
                ownerId: {
                    validators: {
                        notEmpty: {
                            message: '父菜单不能为空'
                        }
                    }
                },
                privilegeName: {
                    validators: {
                        notEmpty: {
                            message: '按钮名称不能为空'
                        }
                    }
                },
                privilegeId: {
                    validators: {
                        notEmpty: {
                            message: '按钮Id不能为空'
                        }
                    }
                }
            }
        });
    }
</script>