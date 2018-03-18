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
                    <input type="hidden" id="form_creatorTime" name='createTime' hidden="true">
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_code">编号</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_code" name="code"
                                   onkeyup="this.value=this.value.toUpperCase()"
                                   type="text" readonly
                                   placeholder=""/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_name">名称</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_name" name="name"
                                   type="text" placeholder=""/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_groupId">分类</label>

                        <div class="col-xs-14 col-sm-7">
                            <select class="chosen-select form-control" id="form_groupId"
                                    name="groupId" data-placeholder="">
                                <option value=""></option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_ownerId">所属方</label>

                        <div class="col-xs-14 col-sm-7">
                            <div class="input-group">
                                <input class="form-control" id="form_ownerId"
                                       type="text" name="ownerId" readonly/>
                                <span class="input-group-btn">
                                    <button class="btn btn-sm btn-default" type="button"
                                            onclick="openUnitDialog('#form_ownerId','#form_unitName',null,'head')">
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
                        <label class="col-sm-2 control-label no-padding-right" for="form_remark">备注</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_remark" name="remark"
                                   type="textarea" placeholder=""/>
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
    $(function () {
        $("#edit-dialog").on('show.bs.modal', function () {

            initEditFormValid();
        });
        $("#edit-dialog").on('hide.bs.modal', function () {


            $("#editForm").data('bootstrapValidator').destroy();
            $('#editForm').data('bootstrapValidator', null);
            initEditFormValid();
        });
    });
    function closeEditDialog() {
        $("#edit-dialog").modal('hide');
    }

    function initEditFormValid() {
        $('#editForm').bootstrapValidator({
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
                        $('#editForm').bootstrapValidator('disableSubmitButtons', false);
                    }
                }, 'json');
            },
            fields: {
                code: {
                    validators: {
                        stringLength: {
                            min: 4,
                            max: 20,
                            message: '用户编号长度必须在4到20之间'
                        },
                        threshold: 5, //有5字符以上才发送ajax请求，（input中输入一个字符，插件会向服务器发送一次，设置限制，6字符以上才开始）
                        remote: {//ajax验证。server result:{"valid",true or false} 向服务发送当前input name值，获得一个json数据。例表示正确：{"valid",true}
                            url: basePath + "/sys/agent/checkUniqueCode.do?pageType=" + pageType,//验证地址
                            message: '用户已存在',//提示消息
                            delay: 2000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                            type: 'POST',//请求方式
                            data: function (validator) {
                                return {
                                    password: $('[name="passwordNameAttributeInYourForm"]').val(),
                                    whatever: $('[name="whateverNameAttributeInYourForm"]').val()
                                };
                            }

                        },
                        regexp: {
                            regexp: /^[a-zA-Z0-9_]+$/,
                            message: '编号由数字字母下划线和.组成'
                        }
                    }
                },
                name: {
                    validators: {
                        notEmpty: {
                            message: '名称不能为空'
                        }
                    }
                },
                ownerId: {
                    validators: {
                        notEmpty: {
                            message: '所属方不为空'
                        }
                    }
                },
                groupId: {
                    validators: {}
                },
                tel: {
                    validators: {
                        notEmpty: {
                            message: '联系电话不能为空'
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

</script>
