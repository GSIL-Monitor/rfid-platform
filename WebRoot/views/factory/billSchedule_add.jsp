<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="modal-scheduleAdd-table" class="modal fade" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header no-padding">
                <div class="table-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        <span class="white">&times;</span>
                    </button>
                    添加排期
                </div>
            </div>

            <div class="modal-body no-padding">
                <div class="widget-body">

                    <div class="widget-main" id="search_scheduleAdd_Panel">
                        <form class="form-horizontal" role="form" id="scheduleAddForm">
                            <div class="form-group">
                                <label class="col-xs-3 control-label text-right" for="formAdd_scheduleToken">流程</label>

                                <div class="col-xs-8">
                                    <select class="chosen-select form-control" id="formAdd_scheduleToken" name="token">
                                    </select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-xs-3 control-label text-right" for="formAdd_schedule">预计完成时间</label>

                                <div class="col-xs-8">
                                    <input id="formAdd_schedule" class="form-control date-picker endDate" name="schedule"
                                           data-date-format="yyyy-mm-dd"/>

                                </div>
                            </div>
                        </form>

                    </div>
                </div>
            </div>

            <div class="modal-footer no-margin-top">
                <a href="#" class="btn" onclick="closeScheduleAddDialog()">关闭</a>
                <a href="#" class="btn btn-primary" onclick="ScheduleAdd()">确定</a>
            </div>
        </div>
    </div>
</div>
<script>

    function initEditFormValid() {
        $('#scheduleAddForm').bootstrapValidator({
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
                token: {
                    validators: {
                        notEmpty: {
                            message: '流程不能为空'
                        }
                    }
                },
                schedule: {
                    validators: {
                        notEmpty: {
                            message: '预计时间不能为空'
                        }
                    }
                }
            }
        });
    }
    function ScheduleAdd() {
        $('#scheduleAddForm').data('bootstrapValidator').validate();
        if(!$('#scheduleAddForm').data('bootstrapValidator').isValid()){
            return ;
        }
        var Object ={};
       Object.token=$("#formAdd_scheduleToken").val();
       Object.schedule=$("#formAdd_schedule").val();

        $.ajax({
            dataType: "json",
            url: basePath + "/factory/billSchedule/saveSchedule.do",
            data:{factoryBillStr:JSON.stringify(scheduleRow),billScheduleStr:JSON.stringify(Object)},
            type: "POST",
            success: function (data) {
                $("#"+scheduleRow.billNo).trigger("reloadGrid");
                closeScheduleAddDialog();
            }
        });

    }
    function closeScheduleAddDialog() {
        $("#modal-scheduleAdd-table").modal('hide');
    }
</script>