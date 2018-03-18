<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<style>
    .help-block {
        color: red;
    }
</style>
<div id="edit-dialog" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">
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
                <form class="form-horizontal" role="form" id="bindEditForm">
                    <div class="row">

                        <label class="col-sm-2 control-label no-padding-right"
                               for="form_code">商品条码</label>

                        <div class="col-xs-12 col-sm-9">
                            <input class="form-control" id="form_code" name="form_code"
                                   onkeyup="this.value=this.value.toUpperCase()"
                                   type="text" placeholder="按回车键结束"/>
                        </div>
                    </div>
                    <div class="row">
                        <label class="col-sm-2 control-label no-padding-right"></label>
                        <div class="col-xs-12 col-sm-9" id="code_validate" style="color:red;font-size: small"></div>
                    </div>
                    <div class="row">
                        <label class="col-sm-2 control-label no-padding-right"
                               for="form_styleId">款号</label>

                        <div class="col-xs-12 col-sm-4">
                            <input class="form-control" id="form_styleId" name="styleId"
                                   type="text" disabled/>
                        </div>
                        <label class="col-sm-1 control-label no-padding-right"
                               for="form_styleName">款名</label>

                        <div class="col-xs-12 col-sm-4">
                            <input class="form-control" id="form_styleName" name="styleName"
                                   type="text" disabled/>
                        </div>

                    </div>
                    <div class="row">
                        <label class="col-sm-2 control-label no-padding-right"
                               for="form_colorId">色码</label>

                        <div class="col-xs-12 col-sm-4">
                            <input class="form-control" id="form_colorId" name="colorId"
                                   type="text" disabled/>
                        </div>
                        <label class="col-sm-1 control-label no-padding-right"
                               for="form_colorName">颜色</label>

                        <div class="col-xs-12 col-sm-4">
                            <input class="form-control" id="form_colorName" name="colorName"
                                   type="text" disabled/>
                        </div>
                    </div>
                    <div class="row">
                        <label class="col-sm-2 control-label no-padding-right"
                               for="form_sizeId">尺号</label>

                        <div class="col-xs-12 col-sm-4">
                            <input class="form-control" id="form_sizeId" name="sizeId"
                                   type="text" disabled/>
                        </div>
                        <label class="col-sm-1 control-label no-padding-right"
                               for="form_sizeName">尺码</label>

                        <div class="col-xs-12 col-sm-4">
                            <input class="form-control" id="form_sizeName" name="sizeName"
                                   type="text" disabled/>
                        </div>
                    </div>
                    <div class="row">
                        <label class="col-sm-2 control-label no-padding-right"
                               for="form_epc">EPC</label>

                        <div class="col-xs-12 col-sm-9">
                            <input class="form-control" id="form_epc" name="form_epc" required  type="text"
                                   onkeyup="this.value=this.value.toUpperCase()"

                                   placeholder="按回车键结束"/>
                        </div>
                    </div>
                    <div class="row">
                        <label class="col-sm-2 control-label no-padding-right"></label>
                        <div class="col-xs-12 col-sm-9" id="epc_validate" style="color:red;font-size: small"></div>
                    </div>
                </form>
                    <div class="row">
                        <label class="col-sm-2 control-label no-padding-right">多次绑定</label>

                        <div class="btn-group" style="padding-top: 6px;padding-left: 13px;">
                            <input name="flag" type="radio" value="1"/>是
                            <input name="flag" type="radio" value="0" checked/>否
                        </div>
                        <button type="button" class="btn btn-sm btn-primary " onclick="resetForm()" style="margin-left: 290px">
                            <i class="ace-icon fa fa-undo"></i>
                            <span class="bigger-110">清空</span>
                        </button>
                    </div>

            </div>
            <div class="hr hr4"></div>

            <table id="bindgrid"></table>

        </div>
    </div>
</div>

<script>

    function resetForm(){
        $("#epc_validate").html(null);
        $("#code_validate").html(null);
        $('#bindEditForm').clearForm();
        $('#bindEditForm').resetForm();
        $("#bindgrid").jqGrid("clearGridData");
    }
    $(function () {
        $("#edit-dialog").on('show.bs.modal', function () {

           resetForm();
        //   initBindEditFormValid();
        });
    });
    function initBindEditFormValid(){
        $('#bindEditForm').bootstrapValidator({
            message: '输入值无效',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                form_epc: {
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
                        }
                    }
                }
            }
        });
    }
</script>



