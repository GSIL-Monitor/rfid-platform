var searchUrl = basePath + "/factory/billSearch/page.do";
var scheduleRow;
$(function () {
    initGrid();
    initDate();
    initSelect();
    changePrint();
    changeSchedule();
    _search();

});
function initDate() {
    var startDate = getMonthFirstDay("yyyy-MM-dd");
    var endDate = getToDay("yyyy-MM-dd");
    $('.startDate').datepicker('setDate', startDate);
    $('.endDate').datepicker('setDate', endDate);
}
function initSelect() {

    $.ajax({
        dataType: "json",
        url: basePath + "/factory/token/findToken.do",
        cache: false,
        async: false,
        type: "POST",
        success: function (data) {
            var json = data.result;
            $("#search_progress").append("<option value=''>-请选择-</option>");
            for (var i = 0; i < json.length; i++) {
                $("#search_progress").append("<option value='" + json[i].token + "'>" + json[i].name + "</option>");
                $("#search_progress").trigger('chosen:updated');
            }
        }
    });

    $.ajax({
        dataType: "json",
        url: basePath + "/factory/token/findCategory.do",
        cache: false,
        async: false,
        type: "POST",
        success: function (data) {
            var json = data.result;
            $("#search_category").append("<option value=''>-请选择-</option>");
            for (var i = 0; i < json.length; i++) {
                $("#search_category").append("<option value='" + json[i].code + "'>" + json[i].name + "</option>");
                $("#search_category").trigger('chosen:updated');
            }
        }
    });
}
function changeSchedule() {
    var isSchedule = $("#search_isSchedule").val();
    if (isSchedule == "N") {
        $("#add_schedule_button").attr("disabled", false);
        $("#filter_GED_scheduleDate").attr("disabled", true);
        $("#filter_LED_scheduleDate").attr("disabled", true);
    } else {
        if (isSchedule == "Y") {
            $("#filter_GED_scheduleDate").attr("disabled", false);
            $("#filter_LED_scheduleDate").attr("disabled", false);
        } else {
            $("#filter_GED_scheduleDate").attr("disabled", true);
            $("#filter_LED_scheduleDate").attr("disabled", true);
        }
        $("#add_schedule_button").attr("disabled", "disabled");
    }
    _search();
}
function changePrint() {
    var isPrint = $("#search_isPrint").val();
    if (isPrint == "N") {
        $("#filter_GED_printDate").attr("disabled", "disabled");
        $("#filter_LED_printDate").attr("disabled", true);
    } else {
        $("#filter_GED_printDate").attr("disabled", false);
        $("#filter_LED_printDate").attr("disabled", false);
    }
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
    initDate();
}
function refresh() {
    _clearSearch();
}
function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}
function initGrid() {
      $("#grid").jqGrid({
        height: "auto",
        mtype: "POST",
        datatype: "json",
        colModel: [

            {name: 'customerId', label: '客户', editable: true, width: 180},
            {name: 'billNo', label: '办单单号', editable: true, width: 180},
            {name: 'type', label: '办类', editable: true, width: 200},
            {name: 'billQty', label: '办单件数', editable: true, width: 180},
            {name: 'printDate', label: '打印日期', editable: true, width: 180},
            {name: 'endDate', label: '办期', editable: true, width: 180},
            {name: 'groupId', label: '组别', editable: true, width: 180},
            {name: 'billOperator', label: '开单人', editable: true, width: 180},
            {name: 'season', label: '季度', editable: true, width: 180},
            {name: 'sex', label: '男/女装', editable: true, width: 180},
            {name: 'factory', label: '工厂', editable: true, width: 180},
            {name: 'billDate', label: '发单日期', editable: true, width: 180},
            {name: 'shirtType', label: '衫型', editable: true, width: 180},
            {name: 'progressName', label: '办单进度', editable: true, width: 180},
            {name: 'washType', label: '洗水类型', editable: true, width: 180},
            {name: 'outDate', label: '出办日期', editable: true, width: 180},
            {name: 'schedulePeriod', label: '预排周期', editable: true, width: 180,
                formatter:function (cellValue, options, rowObject) {
                    if(cellValue){
                        return cellValue+"天";
                    }else{
                        return "";
                    }
                }},
            {name: 'totDay', label: '实际周期', editable: true, width: 180,
                formatter:function (cellValue, options, rowObject) {
                    if(cellValue){
                        return cellValue+"天";
                    }else{
                        return "";
                    }
                }
            },
            {name: 'uploadNo', label: '', hidden: true, width: 180},
            {name: 'status', label: '状态', editable: true, width: 180,align:"center",
             formatter:function (cellValue, options, rowObject) {
                 if(rowObject.totDay&&rowObject.schedulePeriod){
                     var diff = rowObject.totDay - rowObject.schedulePeriod;
                     if(diff <= 0){
                         return '<span title="状态>1 红色,状态=1 黄色,状态<=0 绿色" style="background-color:green" >'+diff+'天</span>';
                     }else if(diff == 1){
                         return '<span title="状态>1 红色,状态=1 黄色,状态<=0 绿色" style="background-color:yellow" >'+diff+'天</span>';
                     }else if(diff > 1){
                         return '<span title="状态>1 红色,状态=1 黄色,状态<=0 绿色" style="background-color:red" >'+diff+'天</span>';
                     }
                 }else{
                     return "";
                 }
             }
            },
            {name: 'progress',hidden:true},
            {name: "category", width: 60, hidden: true},
            {name: "factory", width: 60, hidden: true},
            {name: "season", width: 60, hidden: true}

        ],
        viewrecords: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        sortname: 'billDate',
        sortorder: "desc",
        shrinkToFit: false,
        autoScroll: false,
        multiselect: true,
        autowidth: true,
        subGrid: true,
        subGridOptions:{
            plusicon : "ace-icon fa fa-plus ",
            minusicon : "ace-icon fa fa-minus",
            openicon: "ui-icon-carat-1-sw",
            expandOnLoad: false,
            selectOnExpand : false,
            reloadOnExpand : true
        },
        subGridRowExpanded: function (subgrid_id, row_id) {
            bindSubGrid(subgrid_id, row_id);

        }

    });
}

function bindSubGrid(subgrid_id, row_id) {

    var parentRow = $("#grid").jqGrid("getRowData", row_id);
    var billNo = parentRow.billNo;
    var subGridId = billNo;
    var pager_id = "p_"+billNo;

    $("#" + subgrid_id).html("<table id='" + billNo + "' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");

    $("#" + billNo).jqGrid(
        {
            url: basePath + "/factory/billSchedule/list.do?filter_EQS_billNo=" + billNo, //子表格数据对应的url
            datatype: "json",
            colModel: [
                {name: 'tokenName', label: '流程', editable: false},
                {name: 'tokenIndex', label: '', hidden: true},
                {name: 'billNo', label: '', hidden: true},
                {name: 'type', label: '', hidden: true},
                {name: 'token', label: '', hidden: true},
                {
                    name: ' ', label: '过程', editable: false, width: 180,
                    formatter: function (cellValue, options, rowObject) {
                        switch (rowObject.type) {
                            case "I":
                                return "开始";
                                break;
                            case "O":
                                return "结束";
                                break;
                            case "P":
                                return "暂停";
                                break;
                            case "R":
                                return "恢复";
                                break;
                        }

                    }
                },
                {name: 'schedule', label: '预计完成时间', editable: false, width: 180},
                {name: 'taskTime', label: '实际时间', editable: false, width: 180},
                {name: 'status', label: '状态', editable: false, width: 180,align:"center",
                    formatter:function (cellValue, options, rowObject) {
                        if(rowObject.taskTime){
                            var diff = DateDiff("d",rowObject.taskTime,rowObject.schedule);
                            if(diff <= 0){
                                return '<span title="状态>1 红色,状态=1 黄色,状态<=0 绿色" style="background-color:green" ><=0天</span>';
                            }else if(diff == 1){
                                return '<span title="状态>1 红色,状态=1 黄色,状态<=0 绿色" style="background-color:yellow" ><=1天</span>';
                            }else if(diff > 1){
                                return '<span title="状态>1 红色,状态=1 黄色,状态<=0 绿色" style="background-color:red" >>1天</span>';
                            }

                        }else{
                            return "";
                        }
                    }
                },
                {name: 'remark', label: '备注', editable: true, width: 180,edittype:"textarea"
                },
                {
                    name: '', label: '操作', editable: false, width: 180,
                    formatter: function (cellValue, options, rowObject) {

                        var html;
                        html = "<a style='margin-left: 20px' href='javascript:void(0);' onclick=deleteSchedule(" + options.rowId + "," + subGridId + ")><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";
                        html += "<a style='margin-left: 20px' href='javascript:void(0);' onclick=editSchedule(" + options.rowId + "," + subGridId + ")><i class='ace-icon fa fa-edit' title='编辑'></i></a>";
                        html += "<a style='margin-left: 20px' href='javascript:void(0);' onclick=cancleEdit(" + options.rowId + "," + subGridId + ")><i class='ace-icon fa fa-undo' title='取消'></i></a>";
                        html += "<a style='margin-left: 20px' href='javascript:void(0);' onclick=saveSchedule(" + options.rowId + "," + subGridId + "," + row_id + ")><i class='ace-icon fa fa-save' title='保存'></i></a>";
                        return html;

                    }
                }
            ],
            viewrecords: true,
            autowidth: false,
            rownumbers: true,
            altRows: true,
            multiselect: false,
            rowNum: -1,
            pager: pager_id,
            shrinkToFit: true,
            sortable:true,
            sortname: 'tokenIndex',
            sortorder: "asc"
        });
        $("#" + billNo).jqGrid('navGrid',"#"+pager_id,
            {
                edit: false,
                add: true,
                addicon:"ace-icon fa fa-plus",
                addfunc:function(){
                    showAdd(row_id,billNo);
                },
                del: false,
                search: false,
                refresh: false,
                view: false
            });
        $("#p_"+billNo+"_center").html("");

}

function deleteSchedule(rowId, subGridId) {
    var row = $("#"+subGridId.id).jqGrid('getRowData', rowId);
    if (row.taskTime == "" || row.taskTime == undefined) {
        $.ajax({
            dataType: "json",
            url: basePath + "/factory/billSchedule/deleteSchedule.do",
            data:{type:row.type,token:row.token,billNo:row.billNo,schedule:row.schedule},
            type: "POST",
            success: function (data) {
                $("#"+subGridId.id).trigger("reloadGrid");
            }
        })
    } else {
        bootbox.alert("无法删除");
    }
}

function editSchedule(rowId, subGridId) {
    $("#" + subGridId.id).editRow("" + rowId);
}
function cancleEdit(rowId, subGridId) {
    $("#" + subGridId.id).restoreRow("" + rowId);
}

function saveSchedule(rowId, subGridId,parentRowId) {
    $("#" + subGridId.id).saveRow("" + rowId);
    var temp=parentRowId.id+"";
    var parentRow = $("#grid").jqGrid("getRowData", temp);
   var row = $("#"+subGridId.id).jqGrid('getRowData', rowId);
    console.log(parentRow);
    console.log(row);
    $.ajax({
        dataType: "json",
        url: basePath + "/factory/billSchedule/saveSchedule.do",
        data:{factoryBillStr:JSON.stringify(parentRow),billScheduleStr:JSON.stringify(row)},
        type: "POST",
        success: function (data) {
            $("#"+subGridId.id).trigger("reloadGrid");
        }
    })

}

function exportExcel() {
    var datafrom = $("#searchForm").serialize();
    window.location.href = basePath + "/factory/billSearch/exportScheduleExcel.do?" + datafrom;

}
function showAdd(row_id,subGridId) {
    initEditFormValid();
    scheduleRow = $("#grid").jqGrid("getRowData", row_id);
    console.log(scheduleRow);

    var gridData = $("#" + subGridId).jqGrid("getRowData");
    $.ajax({
        dataType: "json",
        url: basePath + "/factory/token/findToken.do",
        cache: false,
        async: false,
        type: "POST",
        success: function (data) {
            var json = data.result;
            $("#formAdd_scheduleToken").html("");
            $("#formAdd_scheduleToken").append("<option value=''>-请选择-</option>");
            for (var i = 0; i < json.length; i++) {
                if (!scheduleIsExists(json[i].name,gridData)) {
                    $("#formAdd_scheduleToken").append("<option value='" + json[i].token + "'>" + json[i].name + "</option>");
                    $("#formAdd_scheduleToken").trigger('chosen:updated');
                }
            }
        }
    });
    $("#modal-scheduleAdd-table").modal('show').on('hidden.bs.modal', function() {
        $("#scheduleAddForm").data('bootstrapValidator').destroy();
        $('#scheduleAddForm').data('bootstrapValidator', null);
        initEditFormValid();
    });

}
function scheduleIsExists(name,gridData) {
    for (var i=0;i<gridData.length;i++){
        if (gridData[i].tokenName==name){
            return true;
        }
    }
    return false;
}
function showAddDialog() {
    var sids = $("#grid").jqGrid("getGridParam", "selarrrow");
    if (sids.length==0){
        bootbox.alert("请选择");
    }else{
        $("#billScheduleForm").resetForm();
        $("#billSchedule_Grid").jqGrid('clearGridData');
        $("#modal-billSchedule-table").modal('show');
    }

}
