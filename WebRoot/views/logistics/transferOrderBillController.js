/**
 * Created by yushen on 2017/7/4.
 */
var searchUrl = basePath + "/logistics/transferOrder/page.do";
$(function () {
    initGrid();
    initForm();
    if(billNo){
        bootbox.alert("单据"+billNo+"正在编辑中");
    }else{
        sessionStorage.removeItem("billNotransfer");
    }
});

function initForm() {
    initSelectOrigForm();
    initSelectDestForm();
    $(".selectpicker").selectpicker('refresh');
}
function initSelectOrigForm() {
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + $("#search_origUnitId").val(),
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_origId").empty();
            $("#search_origId").append("<option value=''>--请选择出库仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_origId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
            }


        }
    });
}
function initSelectDestForm() {
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + $("#search_destUnitId").val(),
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_destId").empty();
            $("#search_destId").append("<option value=''>--请选择入库仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_destId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
            }

        }
    });
}

function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: basePath + "/logistics/transferOrder/page.do?filter_GTI_status=-1",
        datatype: "json",
        mtype: 'POST',
        colModel: [

            {name: 'billNo', label: '单据编号', sortable: true, width: 40},
            {
                name: "", label: "操作", width: 50, editable: false, align: "center",sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    var billNo = rowObject.billNo;
                    var html;
                    html = "<a href='" + basePath + "/logistics/transferOrder/copyAdd.do?billNo=" + billNo + "'><i class='ace-icon fa fa-files-o' title='复制新增'></i></a>";
                    html += "<a style='margin-left: 20px' href='" + basePath + "/logistics/transferOrder/edit.do?billNo=" + billNo + "'><i class='ace-icon fa fa-edit' title='编辑'></i></a>";
                    html += "<a style='margin-left: 20px' href='#' onclick=check('" + billNo + "')><i class='ace-icon fa fa-check-square-o' title='审核'></i></a>";
                    html += "<a style='margin-left: 20px' href='#' onclick=cancel('" + billNo + "')><i class='ace-icon fa fa-undo' title='撤销'></i></a>";
                    html += "<a style='margin-left: 20px' href='#' onclick=quit('" + rowObject.billNo + "')><i class='ace-icon fa fa-check-circle-o' title='修改'></i></a>";


                    return html;
                }
            },
            {name: 'status', hidden: true},
            {name: 'outStatus', label: '出库状态', hidden: true},
            {name: 'inStatus', label: '入库状态', hidden: true},
            {
                name: 'statusImg', label: '状态', width: 15, align: 'center',sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    var html = "";
                    switch (rowObject.status) {
                        case -1 :
                            html = "<i class='fa fa-undo blue' title='撤销'></i>";
                            break;
                        case 0 :
                            html = "<i class='fa fa-caret-square-o-down blue' title='录入'></i>";
                            break;
                        case 1:
                            html = "<i class='fa fa-check-square-o blue' title='审核'></i>";
                            break;
                        case 2 :
                            html = "<i class='fa fa-archive blue' title='结束'></i>";
                            break;
                        case 3:
                            html = "<i class='fa fa-wrench blue' title='操作中'></i>";
                            break;
                        case 4:
                            html = "<i class='fa fa-paper-plane blue' title='申请撤销'></i>";
                        default:
                            break;

                    }
                    return html;
                }
            },
            {
                name: 'outStatusImg', label: '出库状态', width: 30, align: 'center',sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.outStatus == 0) {
                        return '<i class="fa fa-tasks blue" title="订单状态"></i>';
                    } else if (rowObject.outStatus == 2) {
                        return '<i class="fa fa-sign-out blue" title="已出库"></i>';
                    } else if (rowObject.outStatus == 3) {
                        return '<i class="fa fa-truck blue" title="出库中"></i>';
                    } else {
                        return '';
                    }
                }
            },
            {
                name: 'inStatusImg', label: '入库状态', width: 30, align: 'center',sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.inStatus == 0) {
                        return '<i class="fa fa-tasks blue" title="订单状态"></i>';
                    } else if (rowObject.inStatus == 1) {
                        return '<i class="fa fa-sign-in blue" title="已入库"></i>';
                    } else if (rowObject.inStatus == 4) {
                        return '<i class="fa fa-truck blue" title="入库中"></i>';
                    } else {
                        return '';
                    }
                }
            },
            {name: 'billDate', label: '单据日期', sortable: true, width: 30},
            {name: 'origUnitId', label: '发货方ID', hidden: true},
            {name: 'origUnitName', label: '发货方', width: 30},
            {name: 'origId', label: '发货仓库ID', hidden: true},
            {name: 'origName', label: '发货仓库', width: 30},
            {name: 'destUnitId', label: '收货方ID', hidden: true},
            {name: 'destUnitName', label: '收货方', width: 30},
            {name: 'destId', label: '收货仓库ID', hidden: true},
            {name: 'destName', label: '收货仓库', width: 30},
            {name: 'totQty', label: '单据数量', width: 30},
            {name: 'totOutQty', label: '已出库数量', width: 30},
            {name: 'totOutVal', label: '总出库金额', width: 30, hidden:true},
            {name: 'totInQty', label: '已入库数量', width: 30},
            {name: 'totInVal', label: '总入库金额', width: 30, hidden:true},
            {name: 'remark', label: '备注', width: 50},
            {name: 'id', hidden: true}
        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 50,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: true,
        sortname: 'billNo',
        sortorder: 'desc',
        autoScroll: false,
        footerrow: true,
        gridComplete: function () {
            setFooterData();
        },
        onSelectRow: function (rowid, status) {
        }
    });
}

function setFooterData() {
    var sum_totQty = $("#grid").getCol('totQty', false, 'sum');
    var sum_totOutQty = $("#grid").getCol('totOutQty', false, 'sum');
    var sum_totOutVal = $("#grid").getCol('totOutVal', false, 'sum');
    var sum_totInQty = $("#grid").getCol('totInQty',false,'sum');
    var sum_totInVal = $("#grid").getCol('totInVal', false, 'sum');
    $("#grid").footerData('set', {
        billNo: "合计",
        totQty: sum_totQty,
        totOutQty: sum_totOutQty,
        totOutVal: sum_totOutVal,
        totInQty: sum_totInQty,
        totInVal: sum_totInVal,
    });
}

function add() {
    location.href = basePath + "/logistics/transferOrder/add.do";
}
function refresh() {
    location.reload(true);
}
function quit(billNo) {
    $.ajax({
        url: basePath +"/logistics/transferOrder/quit.do?billNo=" +billNo,
        cache: false,
        async: true,
        type: "POST",
        success: function (data, textStatus) {

            if(textStatus=="success"){
                $.gritter.add({
                    text: billNo+"可以编辑",
                    class_name: 'gritter-success  gritter-light'
                });

            }

        }
    });
}
function check(billNo) {
    var row = $("#grid").getRowData(billNo);
    if (row.status != 0) {
        bootbox.alert("不是录入状态，无法审核");
        return;
    }
    $.ajax({
        dataType: "json",
        url: basePath + "/logistics/transferOrder/check.do",
        data: {billNo: billNo},
        type: "POST",
        success: function (msg) {
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#grid").trigger("reloadGrid");
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}


function cancel(billNo) {

    var row = $("#grid").getRowData(billNo);
    if (row.status != 0) {
        bootbox.alert("不是录入状态，无法撤销");
        return;
    }
    bootbox.confirm({
        /*title: "余额确认",*/
        buttons: {confirm: {label: '确定'}, cancel: {label: '取消'}},
        message: "撤销确定",
        callback: function (result) {
            /* $("#SODtl_save").removeAttr("disabled");*/
            if (result) {
                cancelAjax(billNo);
            } else {
            }
        }
    });
}
function cancelAjax(billNo) {
    $.ajax({
        dataType: "json",
        url: basePath + "/logistics/transferOrder/cancel.do",
        data: {billNo: billNo},
        type: "POST",
        success: function (msg) {
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#grid").trigger("reloadGrid");
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}

function _search() {

    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        page: 1,
        url: searchUrl,
        postData: params
    });
    $("#grid").trigger("reloadGrid");
}


function showAdvSearchPanel() {

    $("#searchPanel").slideToggle("fast");
}




var dialogOpenPage;
function openSearchOrigDialog() {
    dialogOpenPage = "transferOrderOrig";
    $("#modal_guest_search_table").modal('show').on('shown.bs.modal', function () {
        initGuestSelect_Grid();
    });
    $("#searchGuestDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='confirm_selected_OrigUnit()'>确认</button>"
    );
}
function openSearchDestDialog() {
    dialogOpenPage = "transferOrderUnit";
    $("#modal_guest_search_table").modal('show').on('shown.bs.modal', function () {
        initGuestSelect_Grid();
    });
    $("#searchGuestDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='confirm_selected_DestUnit()'>确认</button>"
    );
}
