$(function () {
    initGrid();
    initDialog();
    initNotification();
});
function down(id) {
    openProgress();
    var rowData = $("#grid").jqGrid('getRowData', id);
    var jobUrl = basePath+ "/"+rowData.api;
    if (rowData.enable) {
        $.ajax({
            type: "POST",
            url: jobUrl,
            dataType: "json",
            success: function (data) {
                closeProgress();
                if (data.success) {
                    $("#notification").data('kendoNotification').showText(data.msg, 'success');
                } else {
                    $("#notification").data('kendoNotification').showText(data.msg, 'error');
                    openWarmDialog(data.msg);
                }
            },
            error:function (e) {
                closeProgress();
                $("#notification").data('kendoNotification').showText('下载失败！', 'error');
                openWarmDialog("下载失败");

            }
        });
    } else {
        $("#notification").data('kendoNotification').showText('已被禁止！不能进行操作！', 'error');
        openWarmDialog("已被禁止！不能进行操作");

    }
}

var allSuccess=true;
function downAll() {
    openProgress();
    var rows = $("#grid").jqGrid('getRowData');
/*    for (var j = 0;j<rows.size; ++j) {
        url = UrlList[j];
        $.ajax({
            async: true,
            cache: false,
            timeout: 5000,
            type: "GET",
            url: url,
            complete:ajaxComplete.bind(null, url)
        });
    }*/

    $.each(rows, function (idx, obj) {
        if (obj.enable) {
            var jobUrl = basePath + "/"+ obj.api;
            $.ajax({
                type: "POST",
                url: jobUrl,
                dataType: "json",
                success: function (data) {
                    if(!data.success){
                        allSuccess=false;
                    }
                    console.log(data.msg);
                },
                complete:completeAjax(idx,rows)
            });
        }
    });
}
function completeAjax(i,rows){
    if(i==rows.length-1){
        closeProgress();
        openWarmDialog("同步失败！");
        $("#notification").data('kendoNotification').showText('完成！', 'success');
        allSuccess=true;
    }
}
function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: basePath + "/syn/basicConfiguration/list.do?filter_EQI_type=0",
        datatype: "json",
        colModel: [
            
            {
                name: "", label: "操作", width: 150,
                formatter: function (cellvalue, options, rowObject) {
                    return "<div class='btn-group btn-group-sm pull-center'>" +
                        "<button type='button' class='btn btn-xs btn-success' onclick=\"down('" + rowObject.id + "')\" data-toggle='tooltip'" +
                        "  data-placement='bottom' title='下载'>"
                        + "<span class='glyphicon glyphicon-download-alt' aria-hidden='true'></span> 下载"
                        + " </button></div>";
                }
            },
            {name: 'configState', label: 'configState', width: 40, hidden: true},
            {name: 'type', label: 'type', width: 40, hidden: true},
            {name: 'id', label: '任务编号', width: 200},
            {name: 'name', label: '名称', width: 200},
            {name: 'enable', label: '是否有效', width: 29, hidden: true},

            {
                name: 'enableShow', label: '是否有效', width: 180,sortable:false,
                formatter: function (cellvalue, options, rowObject) {
                    return "<div class='btn-group btn-group-sm pull-left'> <span class='col-sm-10'>" +
                        "<input id='configState' disabled " +
                        "type='checkbox' " + (rowObject.enable == true ? "checked" : "") +
                        " class='ace ace-switch ace-switch-5'/>" +
                        "<span class='lbl middle'></span></span></div>";
                }
            },
            {name: 'api', label: 'API', width: 400},
            {name: 'updateDate', label: '更新日期', width: 200},
            {name: 'remark', label: '备注', width: 200}
        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: false,
        autoScroll:false,
        jsonReader: {
            id: "id",//设置返回参数中，表格ID的名字为blackId
            repeatitems: false
        },
        loadComplete: function (data) {
            //var records = data.total;
            //var total = Math.ceil(records / data.pageSize);
            //data.records = records;
            //data.total = total;
            //jQuery("#grid")[0].addJSONData(eval(data));
            //
            var table = this;
            setTimeout(function () {

                updatePagerIcons(table);
            }, 0);
        }

    });
    $(window).triggerHandler('resize.jqGrid');
}


function initDialog() {
    $("#dialog").kendoDialog({
        width: "400px",
        height: "250px",
        title: "警告",
        closable: false,
        modal: true,
        content: "<center><h3><div class='alert alert-danger' role='alert'>删除后将无法恢复！是否继续？</div></h3></center>",
        buttonLayout: "normal",
        actions: [
            {
                text: '<span class="glyphicon glyphicon-ok">确定</span>',
                primary: true
            },
            {text: '<span class="glyphicon glyphicon-remove">取消</span>'}
        ]
    }).data("kendoDialog").close();

    $("#progressDialog").kendoDialog({
        width: "400px",
        height: "250px",
        title: "提示",
        closable: false,
        animation: true,
        modal: true,
        content: '<center><h3>正在处理中...</h3></center>' +
        '<div class="progress"><div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="45" aria-valuemin="0" aria-valuemax="100" style="width: 45%">' +
        '<span class="sr-only">100%</span></div></div>',
        buttonLayout: "normal"
    }).data("kendoDialog").close();
}
function openWarmDialog(content) {
    $("#dialog").data('kendoDialog').content("<center><h3>"+content+"</h3></center>").open();
}
function closeWarmDialog() {
    $("#dialog").data('kendoDialog').content("<center><h3>"+content+"</h3></center>").close();
}
function openProgress() {
    $("#progressDialog").data('kendoDialog').open();
}
function closeProgress() {
    $("#progressDialog").data('kendoDialog').close();
}

function initNotification() {
    $("#notification").kendoNotification({
        position: {
            top: 50
        },
        stacking: "left"
    }).data("kendoNotification").hide();
}