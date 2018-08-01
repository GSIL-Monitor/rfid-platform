<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<div id="edit_size_sort_dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                尺寸组编辑
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="editSizeSortForm">
                    <input id="form_id" name="id" type="hidden"/>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_sortName"><span class="text-danger">* </span>尺寸组名</label>

                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="form_sortName" name="sortName"
                                   type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_remark">备注</label>

                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="form_remark" name="remark"
                                   type="text">
                        </div>
                    </div>
                </form>

            </div>
        </div>
        <div class="modal-footer">

            <a href="#" class="btn" onclick="closeSizeSortDialog()">关闭</a>

            <button type="button" class="btn btn-primary" onclick="saveSizeSort()">保存</button>

        </div>
    </div>
</div>
<script>

    function saveSizeSort() {

        $('#editSizeSortForm').data('bootstrapValidator').validate();
        if (!$('#editSizeSortForm').data('bootstrapValidator').isValid()) {
            return;
        }
        var progressDialog = bootbox.dialog({
            message: '<p><i class="fa fa-spin fa-spinner"></i> 数据上传中...</p>'
        });
        debugger;
        $.post(basePath + "/prod/size/sizeSortSave.do",
            $("#editSizeSortForm").serialize(),
            function (result) {
                if (result.success == true || result.success == 'true') {
                    progressDialog.modal('hide');
                    $("#edit_size_sort_dialog").modal('hide');
                    location.reload(true);
                }
            }, 'json');
    }
    function closeSizeSortDialog() {
        $("#edit_size_sort_dialog").modal('hide');
    }

    $(function () {
        $("#edit_size_sort_dialog").on('show.bs.modal', function () {
            initeditSizeSortFormValid();
        });
    });

    function initeditSizeSortFormValid() {
        $('#editSizeSortForm').bootstrapValidator({
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
                        $('#editSizeSortForm').bootstrapValidator('disableSubmitButtons', false);
                    }
                }, 'json');
            },
            fields: {
                sortName: {
                    validators: {
                        notEmpty: {
                            message: '尺寸组名不能为空'
                        }
                    }
                }
            }
        });
    }

</script>
