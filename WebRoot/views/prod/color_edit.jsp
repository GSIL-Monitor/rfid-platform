<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<div id="edit_color_dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                颜色编辑
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="editColorForm">
                    <input id="id" name="id" hidden/>
                    <div class="form-group">
                        <div id="colorId_div" hidden="hidden">
                            <label class="col-sm-2 control-label no-padding-right" for="form_colorId">颜色编码</label>
                            <div class="col-xs-10 col-sm-5">
                                <input class="form-control" id="form_colorId" name="colorId"
                                       type="text"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_colorName"><span class="text-danger">* </span>颜色</label>

                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="form_colorName" name="colorName"
                                   type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_hex"><span class="text-danger">* </span>色码</label>

                        <div class="col-xs-10 col-sm-5">
                            <div class="control-group">
                                <div class="bootstrap-colorpicker">
                                    <!-- #section:plugins/misc.colorpicker -->
                                    <input id="form_hex" type="text" class="input-small" name="hex" readonly/>

                                </div>

                            </div>
                        </div>


                    </div>
                </form>

            </div>
        </div>
        <div class="modal-footer">

            <a href="#" class="btn" onclick="closeColorDialog()">关闭</a>

            <button type="button"  class="btn btn-primary" onclick="saveColor()">保存</button>

        </div>
    </div>
</div>
<script>

    function saveColor() {
        $('#editColorForm').data('bootstrapValidator').validate();
        if(!$('#editColorForm').data('bootstrapValidator').isValid()){
            return ;
        }
        $("#form_colorId").removeAttr("disabled");
        var progressDialog = bootbox.dialog({
            message: '<p><i class="fa fa-spin fa-spinner"></i> 数据上传中...</p>'
        });
        $("#form_colorId").val($("#form_colorName").val());
        $.post(basePath+"/prod/color/save.do",
                $("#editColorForm").serialize(),
                function(result) {
                    if(result.success == true || result.success == 'true') {
                        progressDialog.modal('hide');
                        $("#edit_color_dialog").modal('hide');
                        $("#grid").trigger("reloadGrid");
                    }
                }, 'json');
    }
    function closeColorDialog(){
        $("#edit_color_dialog").modal('hide');
    }
    $(function() {
        $('#form_hex').colorpicker();
        $("#edit_color_dialog").on('show.bs.modal', function () {
            initeditColorFormValid();
            $("#form_colorId").attr("disabled", true);
        });
    });
    $('#edit_color_dialog').on('hidden.bs.modal', function() {
        $("#editColorForm").data('bootstrapValidator').destroy();
        $('#editColorForm').data('bootstrapValidator', null);
        initeditColorFormValid();
    });
    function initeditColorFormValid() {
        $('#editColorForm').bootstrapValidator({
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
                        $('#editColorForm').bootstrapValidator('disableSubmitButtons', false);
                    }
                }, 'json');
            },
            fields: {
                colorId: {
                    validators: {
                        notEmpty: {
                            message: '编码不能为空'
                        }
                    }
                },
                colorName: {
                    validators: {
                        notEmpty: {
                            message: '颜色不能为空'
                        }
                    }
                }
            }
        });
    }

</script>
