<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<div id="edit-dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" style="height: 100%">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                编辑
            </div>
        </div>
        <div class="modal-content" style="overflow-y: scroll;height: 70%;overflow-x: hidden;">

            <div class="modal-body">
                <form class="form-horizontal" role="form" id="editForm">
                    <input type="hidden" name="id"/>

                    <input type="hidden" id="form_creatorId" name='creatorId' hidden="true">
                    <input type="hidden" id="form_creatorTime" name='createTime' hidden="true">
                    <div class="form-group" id="codeGroup">
                        <label class="col-sm-2 control-label no-padding-right" for="form_code">编号</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_code" name="code"type="text" onkeyup="this.value=this.value.toUpperCase()"
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
                                            onclick="openUnitDialog('#form_ownerId','#form_unitName', null, 'Shop')">
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
                    <div class="form-group">
                        <div id="areasId_div" hidden="hidden">
                            <label class="col-sm-2 control-label no-padding-right" for="form_areasId">区域</label>
                            <div class="col-xs-14 col-sm-7">
                                <select class="form-control selectpicker show-tick" data-live-search="true" id="form_areasId" name="areasId">
                                    <option value="">请选择</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div id="ownerids_div" hidden="hidden">
                            <label class="col-sm-2 control-label no-padding-right" for="form_ownerids">加盟商(供应商)</label>
                            <div class="col-xs-14 col-sm-7">
                                <select class="form-control selectpicker show-tick" data-live-search="true" id="form_ownerids" name="ownerids">
                                    <option value="">请选择</option>
                                </select>
                            </div>
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
        $("#edit-dialog").on('hide.bs.modal',function(){
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
                code:{
                    validators:{
                        regexp: {
                            regexp: /^[a-zA-Z0-9_]+$/,
                            message: '编号由数字字母下划线和.组成'
                        },
                        stringLength: {
                            min: 4,
                            max: 20,
                            message: '用户名长度必须在4到20之间'
                        },
                        threshold :  5 , //有5字符以上才发送ajax请求，（input中输入一个字符，插件会向服务器发送一次，设置限制，6字符以上才开始）
                        remote: {//ajax验证。server result:{"valid",true or false} 向服务发送当前input name值，获得一个json数据。例表示正确：{"valid",true}
                            url: basePath+"/sys/shop/checkCode.do?pageType="+shopStatus,//验证地址
                            message: '编号已存在',//提示消息
                            delay : 2000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                            type: 'POST',//请求方式
                            data: function(validator) {
                                return {
                                    //  password: $('[name="passwordNameAttributeInYourForm"]').val(),
                                    //  whatever: $('[name="whateverNameAttributeInYourForm"]').val()
                                };
                            }
                        }
                    }
                },
                name: {
                    validators: {
                        notEmpty: '名称不能为空'
                    }
                },
                groupId: {
                    validators: {
                        notEmpty: {
                            message: '分类不能为空'
                        }
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
function closeEditDialog(){
        $("#edit-dialog").modal("hide");
}
</script>
