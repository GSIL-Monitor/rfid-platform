<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>

<div id="edit-dialog2" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                工厂工作时间
            </div>
        </div>
        <div class="modal-content">
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="editForm2">


                            <input class="form-control" id="dayTotalTime2" name="dayTotalTime" type="hidden"/>
                            <input class="form-control" id="morningTotalTime2" name="morningTotalTime" type="hidden"/>
                            <input class="form-control" id="form_token2" name="token" type="hidden" />

                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_code2">工厂编号</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_code2" name="code" type="text" readonly/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_tokenName">流程</label>

                        <div class="col-xs-14 col-sm-7">
                            <input class="form-control" id="form_tokenName"  type="text" readonly/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_morningStartTime2">早上上班时间</label>

                        <div class="col-xs-14 col-sm-7">
                            <div class="input-group bootstrap-timepicker">
                                <input id="form_morningStartTime2" type="text" name="morningStartTime" class="form-control work-timepicker2" readonly/>
                                <span class="input-group-addon"><i class="fa fa-clock-o bigger-110"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_morningEndTime2">早上下班时间</label>
                        <div class="col-xs-14 col-sm-7">
                            <div class="input-group bootstrap-timepicker">
                                <input id="form_morningEndTime2" type="text" name="morningEndTime" class="form-control work-timepicker2" readonly/>
                                <span class="input-group-addon"><i class="fa fa-clock-o bigger-110"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_afternoonStartTime2">下午上班时间</label>

                        <div class="col-xs-14 col-sm-7">
                            <div class="input-group bootstrap-timepicker">
                            <input id="form_afternoonStartTime2" type="text" name="afternoonStartTime" class="form-control work-timepicker2" readonly/>
                            <span class="input-group-addon"><i class="fa fa-clock-o bigger-110"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_afternoonEndTime2">下午下班时间</label>

                        <div class="col-xs-14 col-sm-7">
                            <div class="input-group bootstrap-timepicker">
                            <input id="form_afternoonEndTime2" type="text" name="afternoonEndTime" class="form-control work-timepicker2" readonly/>
                            <span class="input-group-addon"><i class="fa fa-clock-o bigger-110"></i></span>
                            </div>
                        </div>
                    </div>
                </form>

            </div>
        </div>
        <div class="modal-footer">

            <a href="#" class="btn" onclick="closeEditDialog2()">关闭</a>

            <button type="button" href="#" class="btn btn-primary" onclick="save2()">保存</button>

        </div>
    </div>
</div>
<script>

    $(function () {
        $("#edit-dialog2").on('show.bs.modal', function () {


        });


        $(".work-timepicker2").timepicker({
            appendWidgetTo:"#edit-dialog2",
            minuteStep: 1,
            showSeconds: false,
            showMeridian: false,
            disableFocus: false,
            icons: {
                up: 'fa fa-chevron-up',
                down: 'fa fa-chevron-down'
            }
        }).next().on(ace.click_event, function () {
            $(this).prev().focus();
        });

    });
    function closeEditDialog2() {
        $("#edit-dialog2").modal('hide');
    }





</script>
