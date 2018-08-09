<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<div id="edit-dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                新增
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="addForm">
                    <input type="hidden" name="id"/>
                    <input type="hidden" id="form_creatorId" name='creatorId' hidden="true">
                    <input type="hidden" id="form_creatorTime" name='createTime' hidden="true">
                    <input type="hidden" id="form_code" name="code" type="text" hidden="true">

                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_name">名称</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_name" name="name"
                                   type="text" placeholder=""/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_ownerId">所属方</label>

                        <div class="col-xs-14 col-sm-7">
                            <div class="input-group">
                                <input class="form-control" id="form_ownerId"
                                       type="text" name="ownerId" readonly/>
                                <span class="input-group-btn">
                                    <button class="btn btn-sm btn-default" disabled="disabled">
                                        <i class="ace-icon fa fa-list"></i>
                                    </button>
								</span>
                                <input class="form-control" id="form_unitName"
                                       type="text" name="unitName" readonly/>
                            </div>
                        </div>

                    </div>
                    <!-- #section:elements.form -->
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="from_tel">联系电话</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="from_tel" name="tel"
                                   type="text"
                                   placeholder=""/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_linkman">联系人</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_linkman" name="linkman"
                                   type="text" placeholder=""/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_email">邮箱</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_email" name="email"
                                   type="text" placeholder=""/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_provinceId">所在省份</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_provinceId" name="provinceId"
                                   type="text" placeholder=""/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_cityId">所在城市</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_cityId" name="cityId"
                                   type="text" placeholder=""/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_address">街道地址</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_address" name="address"
                                   type="text" placeholder=""/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_remark">备注</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_remark" name="remark"
                                   type="text" placeholder=""/>
                        </div>
                    </div>
                </form>

            </div>
        </div>
        <div class="modal-footer">

            <a href="#" class="btn" onclick="closeEditDialog()">关闭</a>

            <button type="button" href="#" class="btn btn-primary" onclick="saveOrganization()">保存</button>

        </div>
    </div>
</div>
<script>
    $(function () {
        $("#edit-dialog").on('show.bs.modal', function () {

            initAddFormValid();
        });
        $("#edit-dialog").on('hide.bs.modal', function () {
            $("#addForm").data('bootstrapValidator').destroy();
            $('#addForm').data('bootstrapValidator', null);
            initAddFormValid();
        });
    });

    function initAddFormValid() {
        $('#addForm').bootstrapValidator({
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
                        $('#addForm').bootstrapValidator('disableSubmitButtons', false);
                    }
                }, 'json');
            },
            fields: {
                name: {
                    validators: {
                        notEmpty: '名称不能为空'
                    }
                },
                tel: {
                    validators: {
                        stringLength: {
                            min: 11,
                            max: 11,
                            message: '请输入11位手机号码'
                        },
                        regexp: {
                            regexp: /^1[3|5|8]{1}[0-9]{9}$/,
                            message: '请输入正确的手机号码'
                        },
                        notEmpty: {
                            message: '联系电话人不能为空'
                        }
                    }
                },
                linkman: {
                    validators: {
                        notEmpty: {
                            message: '联系人不能为空'
                        }
                    }
                }
            }
        });
    }
    function closeEditDialog() {
        $("#edit-dialog").modal("hide");
    }
</script>
