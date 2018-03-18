var searchUrl = basePath + "/factory/workTime/page.do";
$(function () {
    initGrid();

});
function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}
function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: searchUrl,
        mtype: "POST",
        datatype: "json",
        colModel: [
            {name: 'code', label: '工厂编号', editable: true, width: 140},
            {name: 'codeName', label: '工厂名称', editable: true, width: 140},
            {name: 'tokenName', label: '流程', editable: true, width: 100},
            {name: 'token', label: '流程', hidden: true, width: 100},
            {name: 'morningStartTime', label: '早上上班时间', editable: true, width: 180},
            {name: 'morningEndTime', label: '早上下班时间', editable: true, width: 180},
            {name: 'afternoonStartTime', label: '下午上班时间', editable: true, width: 180},
            {name: 'afternoonEndTime', label: '下午下班时间', editable: true, width: 180}
        ],
        viewrecords: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        sortname: 'code',
        sortorder: "asc",
        shrinkToFit: false,
        autoScroll: false,
        autowidth: true
    });
}
function _search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        url: searchUrl,
        page: 1,
        postData: params
    });
    $("#grid").trigger("reloadGrid");
}

function _clearSearch() {
    $("#searchForm").resetForm();
}
function add() {
    $('option', $("#form_code")).each(function(element) {
        $("#form_code").multiselect('deselect', $(this).val());
    });
    $('option', $("#form_token")).each(function(element) {
        $("#form_token").multiselect('deselect', $(this).val());
    });
    $("#editForm").resetForm();

    $("#edit-dialog").modal('show');


}

function edit() {
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#grid").jqGrid('getRowData', rowId);
        $("#edit-dialog2").modal('show');
        $("#form_tokenName").val(row.tokenName);
        $.ajax({
            type: "POST",
            url: basePath + "/factory/workTime/findWorkTime.do?token=" + row.token+"&code="+row.code,
            dataType: "json",
            success: function (msg) {
                var workTime = msg.result;
                $("#form_token2").val(workTime.token);
                $("#form_code2").val(workTime.code);
                $("#form_morningStartTime2").val(workTime.morningStartTime);
                $("#form_morningEndTime2").val(workTime.morningEndTime);
                $("#form_afternoonStartTime2").val(workTime.afternoonStartTime);
                $("#form_afternoonEndTime2").val(workTime.afternoonEndTime);
                $("#dayTotalTime2").val(workTime.dayTotalTime);
                $("#morningTotalTime2").val(workTime.morningTotalTime);


            }
        });


    } else {
        bootbox.alert("请选择一项");
    }
}

function refresh() {
    location.reload(true);
}

function save(){
    var token=$("#form_token").val();
    var code=$("#form_code").val();
    if (token==null||code==null){
        bootbox.alert("工厂和过程不能为空");
        return;
    }
    var morningStartTime=$("#form_morningStartTime").val();
    var morningEndTime=$("#form_morningEndTime").val();
    var afternoonStartTime=$("#form_afternoonStartTime").val();
    var afternoonEndTime=$("#form_afternoonEndTime").val();
    var morningTime=diffM(morningStartTime,morningEndTime);
    var afternoonTime=diffM(afternoonStartTime,afternoonEndTime);
    if(morningTime>=0&&afternoonTime>=0){
        $("#morningTotalTime").val(morningTime);
        $("#dayTotalTime").val(morningTime+afternoonTime);
        $.post(basePath + "/factory/workTime/save.do",
            $("#editForm").serialize(),
            function (result) {
                if (result.success == true || result.success == 'true') {
                    $("#edit-dialog").modal('hide');
                    $("#grid").trigger("reloadGrid");
                } else {
                    bootbox.alert("保存失败");
                }
            }, 'json');
    }else{
        bootbox.alert("请输入正确的时间");
    }
}
function save2(){
    var token=$("#form_token2").val();
    var code=$("#form_code2").val();
    if (token==null||code==null){
        bootbox.alert("工厂和过程不能为空");
        return;
    }
    var morningStartTime=$("#form_morningStartTime2").val();
    var morningEndTime=$("#form_morningEndTime2").val();
    var afternoonStartTime=$("#form_afternoonStartTime2").val();
    var afternoonEndTime=$("#form_afternoonEndTime2").val();
    var morningTime=diffM(morningStartTime,morningEndTime);
    var afternoonTime=diffM(afternoonStartTime,afternoonEndTime);
    if(morningTime>=0&&afternoonTime>=0){
        $("#morningTotalTime2").val(morningTime);
        $("#dayTotalTime2").val(morningTime+afternoonTime);
        $.post(basePath + "/factory/workTime/save.do",
            $("#editForm2").serialize(),
            function (result) {
                if (result.success == true || result.success == 'true') {
                    $("#edit-dialog2").modal('hide');
                    $("#grid").trigger("reloadGrid");
                } else {
                    bootbox.alert("保存失败");
                }
            }, 'json');
    }else{
        bootbox.alert("请输入正确的时间");
    }

}
function diffM(first,second){
    var f= first.split(":");
    var s=second.split(":");
    return (Number(s[0])*60+Number(s[1]))-(Number(f[0])*60+Number(f[1]));

}

function remove(){
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if (rowId) {
        Confirm({
            msg: '是否删除',
            onOk: function(){
                var row = $("#grid").jqGrid('getRowData', rowId);
                $.ajax({
                    dataType:"json",
                    url: basePath + "/factory/workTime/delete.do?code=" + row.code+"&token="+row.token,
                    type: "POST",
                    success: function (data, textStatus) {
                        $("#grid").trigger("reloadGrid");
                    }
                })
            },
            onCancel: function(){

            }
        });

    } else {
        bootbox.alert("请选择一项");
    }
}