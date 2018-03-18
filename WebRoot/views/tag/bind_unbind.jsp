<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div id="unbind-dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                绑定
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">


                <div class="row">
                    <form class="form-horizontal" role="search" id="editForm">
                    <label class="col-sm-2 control-label no-padding-right"
                           for="unbind_epc">EPC</label>
                    <div class="col-xs-12 col-sm-9">
                        <input class="form-control" id="unbind_epc" name="unbind_epc" type="text"
                               placeholder="按回车键结束" />
                    </div>

                </form>
                </div>
            </div>
            <div class="hr hr4"></div>
            <table id="unbindgrid"></table>


        </div>
    </div>
</div>

<script>
    $(function() {
        $("#unbind-dialog").on('show.bs.modal', function () {
            initEditFormValid();
            $('#editForm').clearForm();
            $('#editForm').resetForm();
            $("#unbindgrid").jqGrid("clearGridData");
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
            fields: {
                unbind_epc: {
                    validators: {
                        notEmpty: {
                            message: 'epc不能为空'
                        },
                        regexp:{
                            regexp:"[1-9]+|[a-z]+|[A-Z]+",
                            message:"只能为字母或数字"
                        },
                        callback:{
                            message:'请输入12、24、32位字符',
                            callback:function(value){
                                return 12 == value.length || 24 == value.length ||  32 == value.length;
                            }
                        },
                        threshold: 5, //有5字符以上才发送ajax请求，（input中输入一个字符，插件会向服务器发送一次，设置限制，6字符以上才开始）
                        remote: {//ajax验证。server result:{"valid",true or false} 向服务发送当前input name值，获得一个json数据。例表示正确：{"valid",true}
                            url: basePath + "/tag/bind/checkEpc.do",//验证地址
                            message: 'epc不存在',//提示消息
                            delay: 2000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                            type: 'POST',//请求方式
                            data: function (validator) {
                                return {
                                };
                            }

                        }
                    }
                }
            }
        });
    }
    </script>

