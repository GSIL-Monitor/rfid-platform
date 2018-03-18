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
                    <input type="hidden" name="id"/>

                    <input type="hidden" id="form_creatorId" name='creatorId' hidden="true">
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_name">名称</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_name" name="name" type="text"
                                   placeholder=""/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_province">省</label>

                        <div class="col-xs-14 col-sm-7">
                            <select class="form-control selectpicker show-tick" data-live-search="true" id="form_province" name="province" onchange="provinceChange()">

                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_city">市</label>

                        <div class="col-xs-14 col-sm-7">
                            <select class="form-control selectpicker show-tick" data-live-search="true" id="form_city" name="city" onchange="cityChange()">
                                <option >--请先选择省--</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_area">县</label>

                        <div class="col-xs-14 col-sm-7">
                            <select class="form-control selectpicker show-tick" data-live-search="true" id="form_area" name="area" onchange="areaChange()">
                                <option >--请先选择市--</option>
                            </select>
                        </div>

                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_ownerid">所属方</label>

                        <div class="col-xs-14 col-sm-7">
                            <select class="form-control selectpicker show-tick" data-live-search="true" id="form_ownerid" name="ownerid">
                                <option >--请选择--</option>
                            </select>
                        </div>

                    </div>

                    <!-- #section:elements.form -->


                </form>

            </div>
        </div>
        <div class="modal-footer">

            <a href="#" class="btn" onclick="closeEditDialog()">关闭</a>

            <button type="button"  class="btn btn-primary" onclick="saveGuest()">保存</button>

        </div>
    </div>
</div>
<script>
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

    function initEditFormValid() {
        $('#editForm').bootstrapValidator({
            message: '输入值无效',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
           /* submitHandler: function(validator, form, submitButton) {
                $.post(form.attr('action'), form.serialize(), function(result) {
                    if (result.success == true || result.success == 'true') {
                    } else {
                        $('#editForm').bootstrapValidator('disableSubmitButtons', false);
                    }
                }, 'json');
            },*/
            fields: {
                name: {
                    validators: {
                        notEmpty: {
                            message: '名称不能为空'
                        }
                    }
                },
                province: {
                    validators: {
                        notEmpty: {
                            message: '省不能为空'
                        }
                    }
                },
                city: {
                    validators: {
                        notEmpty: {
                            message: '市不能为空'
                        }
                    }
                },
                area: {
                    validators: {
                        notEmpty: {
                            message: '县不能为空'
                        }
                    }
                }

            }
        });
    }

</script>
