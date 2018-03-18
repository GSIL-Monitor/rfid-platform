<%@ page pageEncoding="UTF-8" import="java.util.*" language="java" %>
<div class="modal fade" id="area_edit_dialog" tabindex="-1" role="doalog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                分区信息
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="area_editForm">
                    <div class="form-group" id="AcodeGroup">
                        <label class="col-sm-2 text-right" for="form_Acode">分区编号</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="code" id="form_Acode" type="text" readonly/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 text-right" for="form_Aname">分区名称</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="name" id="form_Aname" type="text"/>
                        </div>
                    </div>
                    <div class="form-group" hidden>
                        <label class="col-sm-2 text-right" for="form_AareaId">分区标识</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="areaId" id="form_AareaId" type="text" value="A"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 text-right" for="form_AownerId">所属展厅</label>
                        <div class="col-sm-7">
                            <select class="chosen-select form-control" name="ownerId" id="form_AownerId">
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 text-right" for="form_Astatus">状态</label>
                        <div class="col-sm-7">
                            <select class="chosen-select form-control" name="status" id="form_Astatus">
                                <option value="0">停用</option>
                                <option value="1">启用</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group" id="asDefaultAreaGroup">
                        <label class="col-sm-2 text-right" for="form_asDefaultArea">分区默认</label>
                        <div class="col-sm-7">
                            <select class="chosen-select form-control" name="asDefault" id="form_asDefaultArea">
                                <option value="0">否</option>
                                <option value="1">是</option>
                            </select>
                        </div>
                        <label class="col-sm-3 text-left" style="font-size: x-small">*每个样衣间只有一个默认分区</label>
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

            <button type="button" href="#" class="btn btn-primary" onclick="saveArea()">保存</button>

        </div>
    </div>
</div>
<script>
    $(function(){
        $("#area_edit_dialog").on('show.bs.modal', function () {
            AinitEditFormValid();
            $("#area_editForm").resetForm();
        });
        $("#area_edit_dialog").on('hide.bs.modal',function(){
            $("#area_editForm").data('bootstrapValidator').destroy();
            $('#area_editForm').data('bootstrapValidator', null);
            AinitEditFormValid();
        });
        $("#form_Astatus").change(function(){
           if($(this).val()==1){
               $("#asDefaultAreaGroup").slideDown("fast");
           } else {
               $("#asDefaultAreaGroup").slideUp("fast");
           }
        });
    });

    function AcloseEditDialog(){
        $("#area_edit_dialog").modal("hide");
    }

    function AinitEditFormValid(){
        $('#area_editForm').bootstrapValidator({
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
                        $('#area_editForm').bootstrapValidator('disableSubmitButtons', false);
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
