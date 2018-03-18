<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>

<div id="edit-dialog" class="modal fade" tabindex="-1" role="dialog">
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
                <form class="form-horizontal" role="form" id="editForm">


                            <input class="form-control" id="dayTotalTime" name="dayTotalTime" type="hidden"/>
                            <input class="form-control" id="morningTotalTime" name="morningTotalTime" type="hidden"/>

                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_code">工厂</label>

                        <div class="col-xs-14 col-sm-7">
                            <select multiple="" class="multiselect" data-placeholder="-请选择-" id="form_code" name="code"
                                    required="true">

                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_token">流程</label>

                        <div class="col-xs-14 col-sm-7">
                            <select multiple="" class="multiselect" data-placeholder="-请选择-" id="form_token"
                                    name="token" required="true">

                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_morningStartTime">早上上班时间</label>

                        <div class="col-xs-14 col-sm-7">
                            <div class="input-group bootstrap-timepicker">
                                <input id="form_morningStartTime" type="text" name="morningStartTime" class="form-control work-timepicker" readonly/>
                                <span class="input-group-addon"><i class="fa fa-clock-o bigger-110"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_morningEndTime">早上下班时间</label>
                        <div class="col-xs-14 col-sm-7">
                            <div class="input-group bootstrap-timepicker">
                                <input id="form_morningEndTime" type="text" name="morningEndTime" class="form-control work-timepicker" readonly/>
                                <span class="input-group-addon"><i class="fa fa-clock-o bigger-110"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_afternoonStartTime">下午上班时间</label>

                        <div class="col-xs-14 col-sm-7">
                            <div class="input-group bootstrap-timepicker">
                            <input id="form_afternoonStartTime" type="text" name="afternoonStartTime" class="form-control work-timepicker" readonly/>
                            <span class="input-group-addon"><i class="fa fa-clock-o bigger-110"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right" for="form_afternoonEndTime">下午下班时间</label>

                        <div class="col-xs-14 col-sm-7">
                            <div class="input-group bootstrap-timepicker">
                            <input id="form_afternoonEndTime" type="text" name="afternoonEndTime" class="form-control work-timepicker" readonly/>
                            <span class="input-group-addon"><i class="fa fa-clock-o bigger-110"></i></span>
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


        });
        initSelect();

        $(".work-timepicker").timepicker({
            appendWidgetTo:"#edit-dialog",
            minuteStep: 1,
            showSeconds: false,
            showMeridian: false,
            disableFocus: true,

            icons: {
                up: 'fa fa-chevron-up',
                down: 'fa fa-chevron-down'
            }
        }).next().on(ace.click_event, function () {
            $(this).prev().focus();
        });

    });
    function closeEditDialog() {
        $("#edit-dialog").modal('hide');
    }

    function initSelect() {

        $.ajax({
            dataType: "json",
            url: basePath + "/factory/workTime/initSelect.do",
            cache: false,
            async: false,
            type: "POST",
            success: function (data) {
                var tokenjson = data.result.tokenList;
                for (var i = 0; i < tokenjson.length; i++) {
                    $("#form_token").append("<option value='" + tokenjson[i].token + "'>" + tokenjson[i].name + "</option>");
                    $("#form_token").trigger('chosen:updated');
                }
                var factoryjson = data.result.factoryList;
                for (var i = 0; i < factoryjson.length; i++) {
                    $("#form_code").append("<option value='" + factoryjson[i][1] + "'>" + "["+factoryjson[i][1]+"]"+factoryjson[i][0] + "</option>");
                    $("#form_code").trigger('chosen:updated');
                }
                $('.multiselect').multiselect({
                    enableFiltering: true,
                    enableHTML: true,
                    buttonWidth: '100%',
                    buttonClass: 'btn btn-white btn-primary',
                    templates: {
                        button: '<button type="button" class="multiselect dropdown-toggle" data-toggle="dropdown"><span class="multiselect-selected-text"></span> &nbsp;<b class="fa fa-caret-down"></b></button>',
                        ul: '<ul class="multiselect-container dropdown-menu"></ul>',
                        filter: '<li class="multiselect-item filter"><div class="input-group"><span class="input-group-addon"><i class="fa fa-search"></i></span><input class="form-control multiselect-search" type="text"></div></li>',
                        filterClearBtn: '<span class="input-group-btn"><button class="btn btn-default btn-white btn-grey multiselect-clear-filter" type="button"><i class="fa fa-times-circle red2"></i></button></span>',
                        li: '<li><a tabindex="0"><label></label></a></li>',
                        divider: '<li class="multiselect-item divider"></li>',
                        liGroup: '<li class="multiselect-item multiselect-group"><label></label></li>'
                    }
                });
            }
        });

    }



</script>
