<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<div id="edit-dialog-detailed" class="modal fade" tabindex="-1" role="dialog">
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
                <form class="form-horizontal" role="form" id="editFormdetailed">
                    <div class="form-group" style="display: none">
                        <label class="col-sm-2 control-label no-padding-right" for="form_ids">编号</label>
                        <div class="col-xs-14 col-sm-7">
                             <input type="text" id="form_ids" name='id'>
                            <input type="text"  id="form_type" name='type' hidden>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_code">编号</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_code" name="code"
                                   onkeyup="this.value=this.value.toUpperCase()"
                                   type="text"
                                   placeholder=""/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_name">代码名称</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_name" name="name"
                                   type="text" placeholder=""/>
                        </div>
                    </div>
                </form>

            </div>
        </div>
        <div class="modal-footer">

            <a href="#" class="btn" onclick="closeEditDialogde()">关闭</a>

            <button type="button" href="#" class="btn btn-primary" onclick="saveproperty()">保存</button>

        </div>
    </div>
</div>
<script>
    $(function () {
        $("#edit-dialog-detailed").on('show.bs.modal', function () {   //在调用show方法后触发

            initEditFormValidtype();          //执行一些动作
        });
        $("#edit-dialog-detailed").on('show.bs.modal', function () {
            $("#editFormdetailed").data('bootstrapValidator').destroy();
            $('#editFormdetailed').data('bootstrapValidator', null);
            initEditFormValidtype();
        });
    });

    function initEditFormValidtype() {
        $('#editFormdetailed').bootstrapValidator({      //数据验证
            message: '输入值无效',
            feedbackIcons: {       //只是一个样式表，显示验证成功或者失败时的小图标
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            submitHandler: function (validator, form, submitButton) {
                $.post(form.attr('action'), form.serialize(), function (result) {
                    if (result.success == true || result.success == 'true') {
                    } else {
                        // Enable the submit buttons
                        $('#editFormdetailed').bootstrapValidator('disableSubmitButtons', false);
                    }
                }, 'json');
            },
            fields: {
                code: {
                    validators: {
                        threshold: 5, //有5字符以上才发送ajax请求，（input中输入一个字符，插件会向服务器发送一次，设置限制，6字符以上才开始）
                        remote: {//ajax验证。server result:{"valid",true or false} 向服务发送当前input name值，获得一个json数据。例表示正确：{"valid",true}
                            url: basePath + "/sys/property/checkCodetype.do",//验证地址
                            message: '编号已存在',//提示消息
                            delay: 2000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                            type: 'POST',//请求方式
                            data: function (validator) {
                                console.log( $("#form_type").val());
                                return {
                                    type: $("#form_type").val(),
                                    pageType:pagetype
                                }
                            }
                        },
                        regexp: {
                            regexp: /^[a-zA-Z0-9_.\-]+$/,
                            message: '编号由数字字母下划线和.组成'
                        },
                        stringLength: {
                            min: 0,
                            max: 2,
                            message: '用户名长度必须在0到3之间'
                        },
                    }
                },
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

    function closeEditDialogde() {
        $("#edit-dialog-detailed").modal('hide');
    }
</script>
