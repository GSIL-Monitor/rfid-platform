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

<div id="edit_rolePower_dialog" class="modal fade" tabindex="-1" role="dialog">
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
                <form class="form-horizontal" role="form" id="editRolePowerForm">
                            <input  id="code" name="code" type="hidden"/>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><span class="text-danger"></span>菜单英文名称</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="ename" name="ename"
                                   type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><span class="text-danger"></span>图标名称</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="iconCls" name="iconCls"
                                   type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><span class="text-danger"></span>菜单名称</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="name" name="name"
                                   type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><span class="text-danger"></span>图片名称</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="image" name="image"
                                   type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right">父菜单</label>
                        <div class="col-xs-10 col-sm-5">
                            <select class="chosen-select form-control" id="ownerId" name="ownerId">
                                <option value="01">父菜单</option>
                            </select>
                        </div>
                    </div>
                    <input  id="seqNo" name="seqNo" type="hidden"/>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><span class="text-danger"></span>页面路径</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="url" name="url"
                                   type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><span class="text-danger"></span>小程序编号</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="clientCode" name="clientCode"
                                   type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><span class="text-danger"></span>小程序名</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="clientName" name="clientName"
                                   type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><span class="text-danger"></span>小程序路径</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="wxUrl" name="wxUrl"
                                   type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <input id="lccked" name="lccked" value="0" hidden="hidden"/>
                    </div>
                    <div class="form-group">
                        <input id="status" name="status" value="1" hidden="hidden"/>
                    </div>
                </form>
            </div>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn" onclick="closeEditDialog()">关闭</a>
            <button type="button"  class="btn btn-primary" onclick="pSave()">保存</button>
        </div>
    </div>
</div>
<script>
    function addPower() {
        $("#editRolePowerForm").resetForm();
        pageType="addPower";
        rowId = $("#resourceGrid").jqGrid("getGridParam", "selrow");
        if (rowId) {
            var row = $("#resourceGrid").jqGrid('getRowData', rowId);
            $("#edit_rolePower_dialog").modal("show");
        } else {
            bootbox.alert("请选择父菜单！");
        }
        var so=row.ownerId;
        if (so==""){
            so="01";
        }
        $("#ownerId ").val(so);
        $("#ownerId").attr("disabled","disabled");
    }

    function closeEditDialog() {
        closeDialog();
    }

    function closeDialog() {
        $("#edit_rolePower_dialog").modal('hide');
        $("#editRolePowerForm").resetForm();
    }

    function editPower() {
        $("#ownerId").removeAttr("disabled");
        pageType="editPower";
        rowId = $("#resourceGrid").jqGrid("getGridParam", "selrow");
        if (rowId) {
            var row = $("#resourceGrid").jqGrid('getRowData', rowId);
            $("#edit_rolePower_dialog").modal("show");
            $("#editRolePowerForm").loadData(row);
        } else {
            bootbox.alert("请选择一项进行修改！");
        }
        var so=row.ownerId;
        if (so==""){
            so="01";
        }
        $("#ownerId ").val(so);
    }

    function pSave() {
        $("#ownerId").removeAttr("disabled");
        $("#editRolePowerForm").data('bootstrapValidator').validate();
        if(!$('#editRolePowerForm').data('bootstrapValidator').isValid()){
            return ;
        }
        cs.showProgressBar();
        $.ajax({
            dataType:"json",
            url: basePath + "/sys/role/pSave.do",
            data:{
                roleStr:JSON.stringify(array2obj($("#editRolePowerForm").serializeArray())),
                pageType:pageType
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
            }
        });
        closeDialog();
    }

    function initEditFormValid() {
        $('#editRolePowerForm').bootstrapValidator({
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
                        $('#editRolePowerForm').bootstrapValidator('disableSubmitButtons', false);
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
    }
</script>