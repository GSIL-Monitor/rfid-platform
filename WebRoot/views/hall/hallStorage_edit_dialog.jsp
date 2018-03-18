<%@ page pageEncoding="UTF-8" import="java.util.*" language="java" %>
<div class="modal fade" id="storage_edit_dialog" tabindex="-1" role="doalog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                库位信息
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="storage_editForm">
                    <div class="form-group" id="ScodeGroup">
                        <label class="col-sm-2 text-right" for="form_Scode">库位编号</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="code" id="form_Scode" type="text" readonly/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 text-right" for="form_Sname">库位名称</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="name" id="form_Sname" type="text"/>
                        </div>
                    </div>
                    <div class="form-group" hidden>
                        <label class="col-sm-2 text-right" for="form_SstorageId">分区标识</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="areaId" id="form_SstorageId" type="text" value="E"/>
                        </div>
                    </div>

                    <div class="form-group" hidden>
                        <label class="col-sm-2 text-right" for="form_SownerId">所属分区</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="ownerId" id="form_SownerId">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 text-right" for="form_Sstatus">状态</label>
                        <div class="col-sm-7">
                            <select class="chosen-select form-control" name="status" id="form_Sstatus">
                                <option value="0">停用</option>
                                <option value="1">启用</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group" id="asDefaultGroup">
                        <label class="col-sm-2 text-right" for="form_asDefault">入库默认</label>
                        <div class="col-sm-7">
                            <select class="chosen-select form-control" name="asDefault" id="form_asDefault">
                                <option value="0">否</option>
                                <option value="1">是</option>
                            </select>
                        </div>
                        <label class="col-sm-3 text-left" style="font-size: x-small">*每个分区只有一个默认库位</label>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 text-right" for="form_Aremark">备注</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="remark" id="form_Aremark" type="text"/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="modal-footer">

            <a href="#" class="btn" onclick="AcloseEditDialog()">关闭</a>

            <button type="button" href="#" class="btn btn-primary" onclick="saveStorage()">保存</button>

        </div>
    </div>
</div>
<script>
    $(function () {
        $("#storage_edit_dialog").on('show.bs.modal', function () {
            SinitEditFormValid();
            $("#storage_editForm").resetForm();
        });
        $("#storage_edit_dialog").on('hide.bs.modal', function () {
            $("#storage_editForm").data('bootstrapValidator').destroy();
            $('#storage_editForm').data('bootstrapValidator', null);
            SinitEditFormValid();
        });
        $("#form_Sstatus").change(function () {
            if ($(this).val() == 1) {
                $("#asDefaultGroup").slideDown("fast");
            } else {
                $("#asDefaultGroup").slideUp("fast");
            }
        });
        $("#form_asDefault").change(function () {
            var rowAreaId = $("#Agrid").jqGrid("getGridParam", 'selrow');
            console.log($(this).val());
            if ($(this).val() == 1) {
                if (rowAreaId) {
                    var rowArea = $("#Agrid").jqGrid("getRowData", rowAreaId);
                    if (rowArea.asDefault != 1) {
                        bootbox.alert("该库位所在分区不是默认分区，请在默认分区中选取默认库位");
//                        $(this).find("option[value='0']").attr("selected",true);
                        $(this).val(0);
                        return;
                    }
                }
            }
        });
    });

    function AcloseEditDialog() {
        $("#storage_edit_dialog").modal("hide");
    }

    function SinitEditFormValid() {
        $('#storage_editForm').bootstrapValidator({
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
                        $('#storage_editForm').bootstrapValidator('disableSubmitButtons', false);
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
</script>
