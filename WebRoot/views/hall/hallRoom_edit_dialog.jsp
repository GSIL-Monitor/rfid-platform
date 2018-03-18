<%@ page pageEncoding="UTF-8" import="java.util.*" language="java" %>
<div class="modal fade" id="edit_dialog" tabindex="-1" role="doalog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                样衣间信息
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="editForm">
                    <div class="form-group" id="codeGroup">
                        <label class="col-sm-2 text-right" for="form_code">编号</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="code" id="form_code" type="text" readonly/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 text-right" for="form_name">名称</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="name" id="form_name" type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_ownerId">所属方</label>

                        <div class="col-xs-14 col-sm-7">
                            <div class="input-group">
                                <input class="form-control" id="form_ownerId"
                                       type="text" name="ownerId" readonly />
                                <span class="input-group-btn">
                                    <button class="btn btn-sm btn-default" type="button" onclick="openUnitDialog('#form_ownerId','#form_unitNames',null,'head')">
                                        <i class="ace-icon fa fa-list"></i>
                                    </button>
								</span>
                                <input class="form-control" id="form_unitNames"
                                       type="text" name="unitName" readonly/>
                            </div>
                        </div>

                    </div>
                    <%--<div class="form-group">--%>
                        <%--&lt;%&ndash;重用废弃字段industry为状态字段&ndash;%&gt;--%>
                        <%--<label class="col-sm-2 text-right" for="form_industry">状态</label>--%>
                        <%--<div class="col-sm-7">--%>
                            <%--<select class="chosen-select form-control" name="industry" id="form_industry">--%>
                                <%--<option value="0">停用</option>--%>
                                <%--<option value="1">启用</option>--%>
                            <%--</select>--%>
                        <%--</div>--%>
                    <%--</div>--%>

                    <%--<div class="form-group">--%>
                        <%--<label class="col-sm-2 text-right" for="form_deviceId">绑定设备</label>--%>
                        <%--<div class="col-sm-7">--%>
                            <%--<input class="form-control" name="deviceId" id="form_deviceId" type="text" onkeyup="this.value=this.value.toUpperCase()"/>--%>
                        <%--</div>--%>
                        <%--<label class="col-sm-3 text-left" style="font-size: x-small">*多编号以逗号“,”隔开</label>--%>
                    <%--</div>--%>
                    <div class="form-group">
                        <label class="col-sm-2 text-right" for="form_tel">联系电话</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="tel" id="form_tel" type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 text-right" for="form_linkman">联系人</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="linkman" id="form_linkman" type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 text-right" for="form_email">邮箱</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="email" id="form_email" type="text"/>
                        </div>
                    </div>
                    <div class="form-group" id="createTimeGroup">
                        <label class="col-sm-2 text-right" for="form_createTime">创建时间</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="createTime" id="form_createTime" type="text" readonly/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 text-right" for="form_remark">备注</label>
                        <div class="col-sm-7">
                            <input class="form-control" name="remark" id="form_remark" type="text"/>
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
    $(function(){
        $("#edit_dialog").on('show.bs.modal', function () {
            initEditFormValid();
            $("#editForm").resetForm();
        });
        $("#edit_dialog").on('hide.bs.modal',function(){
            $("#editForm").data('bootstrapValidator').destroy();
            $('#editForm').data('bootstrapValidator', null);
            initEditFormValid();
        });
    });

    function closeEditDialog(){
        $("#edit_dialog").modal("hide");
    }

    function initEditFormValid(){
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
                name: {
                    validators: {
                        notEmpty: {
                            message: '名称不能为空'
                        }
                    }
                },
//
//                deviceId:{
//                  validators:{
//                      notEmpty:{
//                          message:"绑定设备不能为空"
//                      },
//                      stringLength:{
//                          max:20,
//                          min:8,
//                          message:"设备编号长度在8到20号之间"
//                      },
//                      regexp:{
//                          regexp:/^[0-9a-zA-Z,]+$/,
//                          message:"设备编号不包含特殊字符"
//                        }
//                  }
//                },

                tel: {
                    validators: {
                        notEmpty:{
                            message:'联系电话不能为空'
                        },
                        stringLength: {
                            min: 11,
                            max: 11,
                            message: '请输入11位手机号码'
                        },
                        regexp: {
                            regexp: /^1[3|5|8]{1}[0-9]{9}$/,
                            message: '请输入正确的手机号码'
                        }
                    }
                } ,
                linkman: {
                    validators: {
                        notEmpty: {
                            message: '联系人不能为空'
                        }
                    }
                },
                email:{
                    validators:{
                        emailAddress: {
                            message: '邮箱地址格式有误'
                        },
                    }
                }
            }
        });
    }
</script>
