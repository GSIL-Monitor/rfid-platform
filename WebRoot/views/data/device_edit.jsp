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
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_code">设备编号</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_code" name="code" onkeyup="this.value=this.value.toUpperCase()"
                                   type="text" placeholder=""/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_ownerId">所属方</label>

                        <div class="col-xs-14 col-sm-7">
                            <div class="input-group">
                                <input class="form-control" id="form_ownerId"
                                       type="text" name="ownerId" readonly />
                                <span class="input-group-btn">
                                    <button class="btn btn-sm btn-default" type="button" onclick="openUnitDialog('#form_ownerId','#form_ownerName',null,'withShop')">
                                        <i class="ace-icon fa fa-list"></i>
                                    </button>
								</span>
                                <input class="form-control" id="form_ownerName"
                                       type="text" name="ownerName" readonly/>
                            </div>
                        </div>

                    </div>
                    <!-- #section:elements.form -->
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_storageId">所属仓店</label>

                        <div class="col-xs-14 col-sm-7">
                            <!-- <select class="chosen-select form-control" id="form_storageId" name="storageId" > -->
                            <div class="input-group">
                                <input class="form-control" id="form_storageId"
                                       type="text" name="storageId" readonly />
                                <span class="input-group-btn">
                                    <button class="btn btn-sm btn-default" type="button" onclick="initSelect()">
                                        <i class="ace-icon fa fa-list"></i>
                                    </button>
								</span>
                                <input class="form-control" id="form_storageName"
                                       type="text" name="ownerName" readonly/>
                            </div>

                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_appCode">用户ID</label>
                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_appCode" name="appCode"
                                   type="text" placeholder=""/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_mchId">商户电子ID</label>
                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_mchId" name="mchId"
                                   type="text" placeholder=""/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_key">电子签名</label>
                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_key" name="key"
                                   type="text" placeholder=""/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_callbackUrl">回传地址</label>
                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_callbackUrl" name="callbackUrl"
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

            <button type="button"  class="btn btn-primary" onclick="save()">保存</button>

        </div>
    </div>
</div>
<script>

/*该初始化方法置于选择所属方的回调方法中，选择后才能进行初始化  */
    function initSelect(){
        var ownerId=$("#form_ownerId").val();
        if(ownerId=="")
            bootbox.alert("请先选择所属方");
        else
            _openUnitDialog("#form_storageId","#form_storageName",ownerId);
    }

    $(function() {
        $("#edit-dialog").on('show.bs.modal', function () {

            initEditFormValid();
        });
    });
    $('#edit-dialog').on('hidden.bs.modal', function() {
        $("#editForm").data('bootstrapValidator').destroy();
        $('#editForm').data('bootstrapValidator', null);
        initEditFormValid();
    });
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
                        $('#editForm').bootstrapValidator('disableSubmitButtons', false);
                    }
                }, 'json');
            },
            fields: {
                code: {
                    validators: {
                        notEmpty: {
                            message: '设备编号不能为空'
                        },
                        stringLength: {
                            min: 8,
                            max: 8,
                            message: '编号长度必须为8位'
                        },
                        regexp: {
                            regexp: /^[A-Za-z0-9]+$/,
                            message: '只能是字母或数字'
                        }
                    }
                },
                ownerId: {
                    validators: {
                        notEmpty: {
                            message: '请选择所属方'
                        }
                    }
                },
                roleId: {
                    validators: {
                        notEmpty: {
                            message: '请选择所属仓库'
                        }
                    }
                },
                appCode:{
                    validators:{
                        regexp:{
                            regexp: /^[A-Za-z0-9]+$/,
                            message: '只能是字母或数字'
                        }
                    }
                },
                mchId:{
                    validators:{
                        regexp:{
                            regexp: /^[A-Za-z0-9]+$/,
                            message: '只能是字母或数字'
                        }
                    }
                },
                key:{
                    validators:{
                        regexp:{
                            regexp: /^[A-Za-z0-9]+$/,
                            message: '只能是字母或数字'
                        }
                    }
                }
            }
        });
    }

</script>
