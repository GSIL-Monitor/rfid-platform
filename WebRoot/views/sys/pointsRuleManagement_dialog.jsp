<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/11/3
  Time: 15:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="ruleManagement_dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                规则设置
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="ruleManagementForm">
                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_unitId">门店</label>
                        <div class="col-xs-10 col-sm-7">
                            <select class="form-control" id="form_unitId"
                                    name="unitId">
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_startDate">起止时间</label>
                        <div class="col-xs-10 col-sm-7">
                            <div class="input-group">
                                <input class="form-control date-picker" id="form_startDate" type="text"
                                       name="startDate" data-date-format="yyyy-mm-dd"/>
                                <span class="input-group-addon"><i class="fa fa-exchange"></i></span>
                                <input class="form-control date-picker" id="form_endDate" type="text"
                                       name="endDate" data-date-format="yyyy-mm-dd"/>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_unitPoints">每100元对应积分</label>
                        <div class="col-xs-10 col-sm-7">
                            <input class="form-control" id="form_unitPoints" name="unitPoints" type="number"
                                   placeholder="客户每消费100元对应的积分"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_status">是否启用</label>
                        <div class='col-xs-10 col-sm-7'>
                            <select class="form-control" id="form_status" name="status">
                                <option value="1" selected>是</option>
                                <option value="0">否</option>
                            </select>
                            <span class='col-sm-9'  style='font-size: 12px'>
                                (同一时段只能启用一个规则)
                            </span>
                        </div>
                    </div>

                    <div class="form-group" id="div_defaultRule" hidden>
                        <label class="col-sm-3 control-label no-padding-right" for="form_defaultRule">是否默认规则</label>
                        <div class='col-xs-10 col-sm-7'>
                            <select class="form-control" id="form_defaultRule" name="defaultRule" onchange="changeDefault()">
                                <option value=true>是</option>
                                <option value=false selected>否</option>
                            </select>
                            <span class='col-sm-8' style='font-size: 12px'>
                                (默认规则无需填写门店和起止时间)
                            </span>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_remark">备注</label>
                        <div class="col-xs-10 col-sm-7">
                            <input class="form-control" id="form_remark" name="remark" type="text">
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="modal-footer">

            <a href="#" class="btn" onclick="closeRuleDialog()">关闭</a>

            <button type="button" class="btn btn-primary" onclick="saveRule()">保存</button>

        </div>
    </div>
</div>
<script>

    $(function () {
        initUnit();
        initRuleFormValid();
    });

    function initUnit() {
        var searchShopUrl;
        if (ownerId === 1 || ownerId === "1") {
            searchShopUrl = basePath + "/sys/shop/search.do?filter_EQI_type=4";
        } else {
            searchShopUrl = basePath + "/sys/shop/search.do?filter_EQI_type=4&filter_EQS_id=" + ownerId;
        }
        $.ajax({
            url: searchShopUrl,
            cache: false,
            async: false,
            type: "POST",
            success: function (data) {
                $("#form_unitId").empty();
                var json = data;
                $("#form_unitId").append("<option value=''>-- 请选择门店--</option>");
                for (var i = 0; i < json.length; i++) {
                    $("#form_unitId").append("<option value='" + json[i].id + "'>" + "[" + json[i].id + "]" + json[i].name + "</option>");
                }
//                $('#form_unitId').selectpicker('refresh');
            }
        });
    }

    function saveRule() {

        $('#ruleManagementForm').data('bootstrapValidator').validate();
        if (!$('#ruleManagementForm').data('bootstrapValidator').isValid()) {
            return;
        }
        var progressDialog = bootbox.dialog({
            message: '<p><i class="fa fa-spin fa-spinner"></i> 数据上传中...</p>'
        });

        if ($("#form_status").val() === 1 || $("#form_status").val()  === "1") {
            $.ajax({
                url: basePath + "/sys/pointsRule/rulesCheck.do",
                cache: false,
                async: false,
                type: "POST",
                data: {
                    pointsRuleStr: JSON.stringify(array2obj($("#ruleManagementForm").serializeArray())),
                },
                success: function (data) {
                    progressDialog.modal('hide');
                    if (data.success) {
                        $.gritter.add({
                            text: data.msg,
                            class_name: 'gritter-success  gritter-light'
                        });
                        saveAjax();
                    } else {
                        bootbox.confirm({
                            title: "规则调整确认",
                            buttons: {confirm: {label: '确定'}, cancel: {label: '取消'}},
                            message: data.msg + "，是否将其关闭？",
                            callback: function (result) {
                                if (result) {
                                    turnOffConflictRule(data.result);
                                }
                            }
                        });
                    }
                }
            })
        }else {
            saveAjax();
        }
        progressDialog.modal('hide');
    }

    function turnOffConflictRule(result) {
        $.ajax({
            url: basePath + "/sys/pointsRule/turnOffConflictRule.do",
            cache: false,
            async: false,
            type: "POST",
            data: {
                ruleIdStr: result
            },
            success: function (data) {
                if (data.success) {
                    $.gritter.add({
                        text: data.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                    saveAjax();
                } else {
                    $.gritter.add({
                        text: data.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                }
            }
        })
    }

    function saveAjax() {
        $.ajax({
            url: basePath + "/sys/pointsRule/saveRule.do",
            cache: false,
            async: false,
            type: "POST",
            data: {
                pointsRuleStr: JSON.stringify(array2obj($("#ruleManagementForm").serializeArray())),
                userId: userId
            },
            success: function (data) {
                if (data.success) {
                    $.gritter.add({
                        text: data.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                } else {
                    $.gritter.add({
                        text: data.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                }
                $("#ruleManagement_dialog").modal('hide');
                $("#grid").trigger("reloadGrid");
            }
        })
    }
    function closeRuleDialog() {
        $("#ruleManagement_dialog").modal('hide');
    }

    function initRuleFormValid() {
        $('#ruleManagementForm').bootstrapValidator({
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
                        $('#ruleManagementForm').bootstrapValidator('disableSubmitButtons', false);
                    }
                }, 'json');
            },
            fields: {
                unitId: {
                    validators: {
                        notEmpty: {
                            message: '门店不能为空'
                        }
                    }
                },
                startDate: {
                    validators: {
                        notEmpty: {
                            message: '开始时间不能为空'
                        }
                    }
                },
                endDate: {
                    validators: {
                        notEmpty: {
                            message: '结束时间不能为空'
                        }
                    }
                },
                unitPoints: {
                    validators: {
                        notEmpty: {
                            message: '每消费100对应的积分不能为空'
                        }
                    }
                }
            }
        });
    }

    function changeDefault() {
        if ($("#form_defaultRule").val() === true || $("#form_defaultRule").val() === 'true') {
            $("#form_unitId").attr('disabled', true);
            $("#form_startDate").attr('disabled', true);
            $("#form_endDate").attr('disabled', true);
        } else {
            $("#form_unitId").removeAttr('disabled');
            $("#form_startDate").removeAttr('disabled');
            $("#form_endDate").removeAttr('disabled');
        }
    }

</script>
