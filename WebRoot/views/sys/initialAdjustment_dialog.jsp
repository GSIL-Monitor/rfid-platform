<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/10/20
  Time: 14:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<div id="initialAdjustment_dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                期初调整
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="initialAdjustmentForm">
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_unitId">客户编号</label>

                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="form_unitId" name="unitId" type="text" readonly/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_month">月份</label>

                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control datetimepicker" id="form_month" name="month" type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_preVal">期初金额</label>

                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="form_preVal" name="preVal"
                                   readOnly type="text"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_afterVal">调整后金额</label>

                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="form_afterVal" name="afterVal" type="number"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right" for="form_remark">备注</label>

                        <div class="col-xs-10 col-sm-5">
                            <input class="form-control" id="form_remark" name="remark"
                                   type="text">
                        </div>
                    </div>
                </form>

            </div>
        </div>
        <div class="modal-footer">

            <a href="#" class="btn" onclick="closeInitialAdjustmentDialog()">关闭</a>

            <button type="button" class="btn btn-primary" onclick="saveInitialAdjustment()">保存</button>

        </div>
    </div>
</div>
<script>

    $(function () {
        $('#form_month').datepicker({
            language: "zh-CN",
            todayHighlight: true,
            format: 'yyyy-mm',
            autoclose: true,
            startView: 'months',
            maxViewMode:'months',
            minViewMode:'months'
        }).on('changeDate', function(e){
            var date = new Date(e.date.getTime());
            if(date.getMonth()== 0){
                dateStr = date.getFullYear()-1+"-"+12;
            }else if(0< date.getMonth()&&date.getMonth()< 10){
                dateStr = date.getFullYear()+"-0"+date.getMonth();
            }else{
                dateStr = date.getFullYear()+"-"+date.getMonth();
            }
            console.log(dateStr);
            masId = $("#form_unitId").val()+'-'+dateStr;
            $.ajax({
                dataType: "json",
                url: basePath + "/logistics/monthAccountStatement/getMas.do",
                data: {
                    masId: masId
                },
                type: "POST",
                success: function (result) {
                    if (result.success) {
                        $("#form_preVal").val(result.result.totVal);
                    } else {
                        $("#form_preVal").val(0);
                    }
                }
            });
        });
    });

    function saveInitialAdjustment() {

//        $('#initialAdjustmentForm').data('bootstrapValidator').validate();
//        if (!$('#initialAdjustmentForm').data('bootstrapValidator').isValid()) {
//            return;
//        }

        if ($("#form_afterVal").val() && $("#form_afterVal").val() !== null) {
            var progressDialog = bootbox.dialog({
                message: '<p><i class="fa fa-spin fa-spinner"></i> 数据上传中...</p>'
            });
            $.ajax({
                dataType: "json",
                url: basePath + "/logistics/initialAdjustment/saveInitialAdjustment.do",
                data: {
                    initialAdjustmentStr: JSON.stringify(array2obj($("#initialAdjustmentForm").serializeArray())),
                    userId: userId,
                    masId: masId
                },
                type: "POST",
                success: function (result) {
                    if (result.success) {
                        $.gritter.add({
                            text: result.msg,
                            class_name: 'gritter-success  gritter-light'
                        });
                    } else {
                        bootbox.alert(result.msg);
                    }
                    progressDialog.modal('hide');
                    closeInitialAdjustmentDialog();
                }
            });
        }else {
            $.gritter.add({
                text: "请填写调整后金额",
                class_name: 'gritter-success  gritter-light'
            });
        }
    }
    function closeInitialAdjustmentDialog() {
        $("#initialAdjustment_dialog").modal('hide');
    }
//
//    $(function () {
//        $("#initialAdjustment_dialog").on('show.bs.modal', function () {
//            initialAdjustmentFormValid();
//        });
//    });

//    function initialAdjustmentFormValid() {
//        $('#initialAdjustment_dialog').bootstrapValidator({
//            message: '输入值无效',
//            feedbackIcons: {
//                valid: 'glyphicon glyphicon-ok',
//                invalid: 'glyphicon glyphicon-remove',
//                validating: 'glyphicon glyphicon-refresh'
//            },
//            submitHandler: function (validator, form, submitButton) {
//                $.post(form.attr('action'), form.serialize(), function (result) {
//                    if (result.success == true || result.success == 'true') {
//
//                    } else {
//                        $('#initialAdjustment_dialog').bootstrapValidator('disableSubmitButtons', false);
//                    }
//                }, 'json');
//            },
//            fields: {
//                afterVal: {
//                    validators: {
//                        notEmpty: {
//                            message: '调整后金额不能为空'
//                        }
//                    }
//                }
//            }
//        });
//    }
</script>
