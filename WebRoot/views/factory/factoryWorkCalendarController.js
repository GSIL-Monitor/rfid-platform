/** 当天信息初始化 **/
var flag = true;
var oldDate = undefined;
$(function () {

    var dayDate = new Date();
    var d = $.fullCalendar.formatDate(dayDate, "dddd");
    var m = $.fullCalendar.formatDate(dayDate, "yyyy年MM月dd日");
    var lunarDate = lunar(dayDate);
    $(".alm_date").html(m + "&nbsp;" + d);
    $(".today_date").html(dayDate.getDate())
    $("#alm_cnD").html("农历" + lunarDate.lMonth + "月" + lunarDate.lDate);
    $("#alm_cnY").html(lunarDate.gzYear + "年&nbsp;" + lunarDate.gzMonth + "月&nbsp;" + lunarDate.gzDate + "日");
    $("#alm_cnA").html("【" + lunarDate.animal + "年】");
    var fes = lunarDate.festival();
    if (fes.length > 0) {
        $(".alm_lunar_date").html($.trim(lunarDate.festival()[0].desc));
        $(".alm_lunar_date").show();
    } else {
        $(".alm_lunar_date").hide();
    }
    initCalendar();
    $("#fcs_date_year").change(function(){
        flag=true;
    });
    $("#fcs_date_month").change(function(){
        flag=true;
    });

});

function initCalendar() {
    var date = new Date();
    var d = date.getDate();
    var m = date.getMonth();
    var y = date.getFullYear();
    $("#calendar").fullCalendar(
        {
            header: {
                left: 'prevYear,nextYear',
                left: 'title',
                right: 'prev,next today'
            },
            buttonText: {
                prev: "<span class='fc-text-arrow'>&lsaquo;上个月</span>",
                next: "<span class='fc-text-arrow'>下个月&rsaquo;</span>",
                prevYear: "<span class='fc-text-arrow'>&laquo;上一年</span>",
                nextYear: "<span class='fc-text-arrow'>下一年&raquo;</span>"
            },
            dayClick: function (dayDate, allDay, jsEvent, view) {
                if ($(this).children().children().find('.redFont').text() != undefined && $(this).children().children().find('.redFont').text() != "") {
                    endEdit(oldDate,dayDate);
                    if (flag && $(this).hasClass('fc-other-month') == false) {
                        $(this).attr("id", "curretDiv");
                        var sltA = $(this).children().children().find('.redFont').text() == '全天班' ? 'selected' : '';
                        var sltB = $(this).children().children().find('.redFont').text() == '休息日' ? 'selected' : '';
                        var sltC = $(this).children().children().find('.redFont').text() == '上午班' ? 'selected' : '';
                        $(this).children().children().find('.redFont').remove();
                        var cdate = $.fullCalendar.formatDate(dayDate, "yyyyMMdd");
                        oldDate = cdate;
                        var cont = "<select id='selectTest' onchange='changeWork(" + cdate + ")'><option value='' " + ">-请选择-</option><option value='休息日' " + sltB + ">休息日</option><option value='全天班'" + sltA + ">全天班</option><option value='上午班'" + sltC + ">上午班</option></select>"
                        $(this).children().find('.fc-day-content').append(cont);
                        flag = false;
                        $(this).css("background-color", "#D6D6FF");
                    }
                } else {
                    endEdit(oldDate,dayDate);
                    console.info($(this).hasClass('fc-other-month'));
                    if (flag && $(this).hasClass('fc-other-month') == false) {
                        $(this).attr("id", "curretDiv");
                        var cdate = $.fullCalendar.formatDate(dayDate, "yyyyMMdd");
                        oldDate = cdate;
                        var cont = "<select id='selectTest' onchange='changeWork(" + cdate + ")'><option value=''>-请选择-</option><option value='休息日'>休息日</option><option value='全天班'>全天班</option><option value='上午班'>上午班</option></select>"
                        $(this).children().find('.fc-day-content').append(cont);
                        flag = false;
                        $(this).css("background-color", "#D6D6FF");
                    }
                }
                var d = $.fullCalendar.formatDate(dayDate, "dddd");
                var m = $.fullCalendar.formatDate(dayDate, "yyyy年MM月dd日");
                var lunarDate = lunar(dayDate);
                $(".alm_date").html(m + "&nbsp;" + d);
                $(".today_date").html(dayDate.getDate())
                $("#alm_cnD").html("农历" + lunarDate.lMonth + "月" + lunarDate.lDate);
                $("#alm_cnY").html(lunarDate.gzYear + "年&nbsp;" + lunarDate.gzMonth + "月&nbsp;" + lunarDate.gzDate + "日");
                $("#alm_cnA").html("【" + lunarDate.animal + "年】");
                var fes = lunarDate.festival();
                if (fes.length > 0) {
                    $(".alm_lunar_date").html($.trim(lunarDate.festival()[0].desc));
                    $(".alm_lunar_date").show();
                } else {
                    $(".alm_lunar_date").hide();
                }
                // 当天则显示“当天”标识
                var now = new Date();
                if (now.getDate() == dayDate.getDate() && now.getMonth() == dayDate.getMonth() && now.getFullYear() == dayDate.getFullYear()) {
                    $(".today_icon").show();
                } else {
                    $(".today_icon").hide();
                }
            },
            loading: function (bool) {
                if (bool)
                    $("#msgTopTipWrapper").show();
                else
                    $("#msgTopTipWrapper").fadeOut();
            }
        });
    getCurMothCalendar(y, m);
    $("#fc-dateSelect").delegate("select", "change", function () {
        flag=true;
        var fcsYear = $("#fcs_date_year").val();
        var fcsMonth = $("#fcs_date_month").val();
        $("#calendar").fullCalendar('gotoDate', fcsYear, fcsMonth);
        getCurMothCalendar(fcsYear, fcsMonth);
    });
    $(".fc-button").click(function () {
        flag=true;
        var fcsYear = $("#fcs_date_year").val();
        var fcsMonth = $("#fcs_date_month").val();
        $("#calendar").fullCalendar('gotoDate', fcsYear, fcsMonth);
        getCurMothCalendar(fcsYear, fcsMonth);
    });
}
function getCurMothCalendar(curYear, curMonth) {
    var month = parseInt(curMonth) + 1;
    if (month < 10) {
        month = "0" + month;
    }
    $.ajax({
        type: "post",
        async: false,
        url: basePath + '/factory/workCalendar/list.do',
        dataType: "json",
        data: {
            filter_LIKES_day: curYear + "-" + month
        },
        success: function (msg) {
            if (msg.success) {
                $.each(msg.result, function (index, value) {
                    $(".fc-day").each(function () {
                        //console.info($(this).attr("data-date"));
                        if (value.day == $(this).attr("data-date")) {

                            var state;
                            if (value.status == 0) {
                                state = '<font class="redFont" style="font-weight: bold;background: #FFAB3D;color: #fff;padding: 5px;font-size: 16px;border-radius: 5px;">休息日</font>';
                            } else if (value.status == 1) {
                                state = '<font class="redFont" style="font-weight: bold;background: #37ABEC;color: #fff;padding: 5px;font-size: 16px;border-radius: 5px;">全天班</font>';
                            } else {
                                state = '<font class="redFont" style="font-weight: bold;background: #37ABEC;color: #fff;padding: 5px;font-size: 16px;border-radius: 5px;">上午班</font>';
                            }
                            $(this).children().find('.fc-day-content').html(state);
                        }
                    });
                });
            }
        },
        error: function (response) {

        }
    });
}

function saveWorkCalendar(cDate, state) {

    $.ajax({
        type: "post",
        async: false,
        url: basePath + "/factory/workCalendar/save.do",
        dataType: "json",
        data: {status: state, day: cDate},
        success: function (data) {
            if (data.success) {
                // AJAX发送后成功执行.
                if (state == 0) {
                    state = '<font class="redFont" style="font-weight: bold;background: #FFAB3D;color: #fff;padding: 5px;font-size: 16px;border-radius: 5px;">休息日</font>';
                } else if (state == 1) {
                    state = '<font class="redFont" style="font-weight: bold;background: #37ABEC;color: #fff;padding: 5px;font-size: 16px;border-radius: 5px;">全天班</font>';
                } else {
                    state = '<font class="redFont" style="font-weight: bold;background: #37ABEC;color: #fff;padding: 5px;font-size: 16px;border-radius: 5px;">上午班</font>';
                }
                $("#curretDiv").children().find('.fc-day-content').append(state);
                $("#curretDiv").css("background-color", "#ACD8D9");
                $("#curretDiv").attr("id", "");
                $("#selectTest").remove();
            } else {
                bootbox.alert(data.msg);
            }
            flag = true;
        }
    });
}

function endEdit(oldDate,dayDate){
    var deferred = $.Deferred();
    var cdate = $.fullCalendar.formatDate(dayDate, "yyyyMMdd");
    if(oldDate != undefined && oldDate != cdate){
        $("#curretDiv").children().find('.fc-day-content').append("");
        $("#curretDiv").css("background-color", "#ffffff");
        $("#curretDiv").attr("id", "");
        $("#selectTest").remove();
        flag = true;
        var fcsYear = $("#fcs_date_year").val();
        var fcsMonth = $("#fcs_date_month").val();
        $("#calendar").fullCalendar('gotoDate', fcsYear, fcsMonth);
        getCurMothCalendar(fcsYear, fcsMonth);
        deferred.reject();
        return;
    }
}
function changeWork(cDate) {
    var deferred = $.Deferred();
    var state = $("#selectTest option:selected").val();
    if(state==""){
        $("#curretDiv").children().find('.fc-day-content').append("");
        $("#curretDiv").css("background-color", "#ffffff");
        $("#curretDiv").attr("id", "");
        $("#selectTest").remove();
        flag = true;
        var fcsYear = $("#fcs_date_year").val();
        var fcsMonth = $("#fcs_date_month").val();
        $("#calendar").fullCalendar('gotoDate', fcsYear, fcsMonth);
        getCurMothCalendar(fcsYear, fcsMonth);
        deferred.reject();
        return;
    }
    switch (state) {
        case '休息日':
            state = 0;
            break;
        case '全天班':
            state = 1;
            break;
        case '上午班':
            state = 2;
            break;
    }
    cDate = cDate.toString().substr(0, 4) + "-" + cDate.toString().substr(4, 2) + "-" + cDate.toString().substr(6, 2);
    saveWorkCalendar(cDate, state);
    if (state == 0) {
        state = '<font class="redFont" style="font-weight: bold;background: #FFAB3D;color: #fff;padding: 5px;font-size: 16px;border-radius: 5px;">休息日</font>';
    } else if (state == 1) {
        state = '<font class="redFont" style="font-weight: bold;background: #37ABEC;color: #fff;padding: 5px;font-size: 16px;border-radius: 5px;">全天班</font>';
    } else if (state == 2) {
        state = '<font class="redFont" style="font-weight: bold;background: #37ABEC;color: #fff;padding: 5px;font-size: 16px;border-radius: 5px;">上午班</font>';
    }
    $("#curretDiv").children().find('.fc-day-content').append(state);
    $("#curretDiv").css("background-color", "#ACD8D9");
    $("#curretDiv").attr("id", "");
    $("#selectTest").remove();
    flag = true;

    var fcsYear = $("#fcs_date_year").val();
    var fcsMonth = $("#fcs_date_month").val();
    $("#calendar").fullCalendar('gotoDate', fcsYear, fcsMonth);
    getCurMothCalendar(fcsYear, fcsMonth);


}