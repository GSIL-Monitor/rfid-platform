<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<div id="edit-dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                编辑
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="editForm">
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_vendorId">供应商名称</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_vendorId" name="vendorId"
                                   type="text" readonly/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_owingValue">欠款</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_owingValue" name="owingValue"
                                   type="text" placeholder=""/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_payPrice">本次付款</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_payPrice" name="payPrice"
                                   type="text" placeholder=""/>
                        </div>
                    </div>
                    <!-- #section:elements.form -->
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="from_remark">备注</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="from_remark" name="remark"
                                   type="text"
                                   placeholder=""/>
                        </div>
                    </div>

                </form>

            </div>
        </div>
        <div class="modal-footer">

            <a href="#" class="btn" onclick="closeEditDialog()">关闭</a>

            <button type="button" href="#" class="btn btn-primary" onclick="save()">保存</button>

        </div>
    </div>
</div>
<script>
    //输入的校验
    $(function() {
        $("#edit-dialog").on('show.bs.modal', function () {

            initEditFormValid();
        });
        $("#edit-dialog").on('hide.bs.modal', function () {
            $("#editForm").data('bootstrapValidator').destroy();
            $('#editForm').data('bootstrapValidator', null);
            initEditFormValid();
        });
    });

    function callback(){}

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


                        // Enable the submit buttons
                        $('#editForm').bootstrapValidator('disableSubmitButtons', false);
                    }
                }, 'json');
            },
            fields: {
                payPrice: {
                    validators: {
                        regexp: {
                            regexp: /^(([1-9]\d{0,9})|0)(\.\d{1,2})?$/,
                            message: '请输入正确的付款金额'
                        }
                    }
                } ,
            }
        });
    }
    function closeEditDialog(){
        $("#edit-dialog").modal('hide');
    }
</script>
