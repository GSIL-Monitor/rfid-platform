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

<div id="edit_roleDiv_dialog" class="modal fade" tabindex="-1" role="dialog">
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
                <form class="form-horizontal" role="form" id="editRoleDivForm">
                    <input  id="divCode" name="code" type="hidden"/>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><span class="text-danger"></span>表单字段名称</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="divName" name="privilegeName"
                                   type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><span class="text-danger"></span>表单字段divId</label>
                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="divId" name="privilegeId"
                                   type="text"/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn" onclick="closeEditDivDialog()">关闭</a>
            <button type="button"  class="btn btn-primary" onclick="saveDiv()">保存</button>
        </div>
    </div>
</div>
<script>
    function addDiv() {
        $("#editRoleDivForm").resetForm();
        rowId = $("#resourceGrid").jqGrid("getGridParam", "selrow");
        if (rowId) {
            var row = $("#resourceGrid").jqGrid('getRowData', rowId);
            $("#edit_roleDiv_dialog").modal("show");
        } else {
            bootbox.alert("请选择父菜单！");
        }
        var so=rowId;
        if (so==""){
            so="01";
        }
        $("#divCode").val(so);
    }

    function closeEditDivDialog() {
        closeDivDialog();
    }

    function closeDivDialog() {
        $("#edit_roleDiv_dialog").modal('hide');
        $("#editRoleDivForm").resetForm();
    }



    function saveDiv() {

        checkDivId(checkBackDiv);

    }
    function checkBackDiv(isok) {
        var isok=isok;
        if(!isok){
            return;
        }
        if($("#divName").val()==""||$("#divName").val()==undefined){
            $.gritter.add({
                text: "表单名称不能为空",
                class_name: 'gritter-success  gritter-light'
            });
            return
        }
        cs.showProgressBar();
        $.ajax({
            dataType:"json",
            url: basePath + "/sys/role/saveResourceDiv.do",
            data:{
                roleStr:JSON.stringify(array2obj($("#editRoleDivForm").serializeArray()))
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
    }
    function checkDivId(checkBackDiv) {
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
                    checkBackDiv(true)
                } else {
                    cs.showAlertMsgBox(result.msg);
                    checkBackDiv(false)
                }
            }
        });
    }
</script>