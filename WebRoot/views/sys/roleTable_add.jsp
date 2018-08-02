<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/7/5
  Time: 上午 10:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div id="edit_roleTable_dialog" class="modal fade" tabindex="-1" role="dialog">
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
                <form class="form-horizontal" role="form" id="editRoleTableForm">
                    <input  id="savetablecode" name="code" type="hidden"/>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><span class="text-danger"></span>名称</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="tableName" name="privilegeName"
                                   type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><span class="text-danger"></span>表列name</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="tabelId" name="privilegeId"
                                   type="text"/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn" onclick="closeEditTableDialog()">关闭</a>
            <button type="button"  class="btn btn-primary" onclick="tSave()">保存</button>
        </div>
    </div>
</div>
<script>
    function addTable() {
        $("#editRoleTableForm").resetForm();
        rowId = $("#resourceGrid").jqGrid("getGridParam", "selrow");
        if (rowId) {
            var row = $("#resourceGrid").jqGrid('getRowData', rowId);
            $("#edit_roleTable_dialog").modal("show");
        } else {
            bootbox.alert("请选择父菜单！");
        }
        var so=rowId;
        if (so==""){
            so="01";
        }
        $("#savetablecode").val(so);

    }

    function closeEditTableDialog() {
        closeTableDialog();
    }

    function closeTableDialog() {
        $("#edit_roleTable_dialog").modal('hide');
        $("#editRoleTableForm").resetForm();
    }



    function tSave() {

        checkTableId(checkTableBack);

    }

    /*function initEditFormValid() {
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
                }
            }
        });
    }*/
    function checkTableBack(isok) {
        var isok=isok;
        if(!isok){
            return;
        }
        if($("#tableName").val()==""||$("#tableName").val()==undefined){
            $.gritter.add({
                text: "名称不能为空",
                class_name: 'gritter-success  gritter-light'
            });
            return
        }

        cs.showProgressBar();
        $.ajax({
            dataType:"json",
            url: basePath + "/sys/role/saveResourceTable.do",
            data:{
                roleStr:JSON.stringify(array2obj($("#editRoleTableForm").serializeArray()))
            },
            type:"POST",
            success:function(result) {
                cs.closeProgressBar();
                if(result.success == true || result.success == 'true') {
                    $.gritter.add({
                        text: result.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                    $("#edit_roleTable_dialog").modal("hide");
                } else {
                    cs.showAlertMsgBox(result.msg);
                }
            }
        });

    }
    function checkTableId(checkTableBack) {
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
                    checkTableBack(true)
                } else {
                    cs.showAlertMsgBox(result.msg);
                    checkTableBack(false)
                }
            }
        });
    }
</script>
