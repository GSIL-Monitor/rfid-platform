var searchUrl = basePath + "/logistics/purchase/page.do";
$(function () {
    initGrid();
    initForm();
    initProgressDialog();
    initNotification();
    if(billNo){
        bootbox.alert("单据"+billNo+"正在编辑中");
    }else{
        sessionStorage.removeItem("billNopurchase");
    }
});

function initForm() {
    initSelectDestForm();
}

function initSelectDestForm() {
   // "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId="+$("#search_unitId").val()
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9",
        cache: false,
        async: true,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_destId").empty();
            $("#search_destId").append("<option value='' style='background-color: #eeeeee'>--请选择入库仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_destId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                $("#search_destId").trigger('chosen:updated');
            }
        }
    });
}

function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: basePath + "/logistics/purchase/page.do?filter_GTI_status=-1",
        datatype: "json",
        sortorder: 'desc',
        colModel: [

            {name: 'billNo', label: '单据编号', sortable: true, width: 40},
            {
                name: "", label: "操作", width: 60, editable: false, align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    var billNo = rowObject.billNo;
                    var html;
                    html = "<a href='" + basePath + "/logistics/purchase/copyAdd.do?billNo=" + billNo + "'><i class='ace-icon fa fa-files-o' title='复制新增'></i></a>";
                    html += "<a style='margin-left: 20px' href='" + basePath + "/logistics/purchase/edit.do?billNo=" + billNo + "'><i class='ace-icon fa fa-edit' title='编辑'></i></a>";
                    html += "<a style='margin-left: 20px' href='#' onclick=check('" + billNo + "')><i class='ace-icon fa fa-check-square-o' title='审核'></i></a>";
                    html += "<a style='margin-left: 20px' href='#' onclick=cancel('" + billNo + "')><i class='ace-icon fa fa-undo' title='撤销'></i></a>";
                  /*  html += "<a style='margin-left: 20px' href='#' onclick=doPrint('" + billNo + "')><i class='ace-icon fa fa-print' title='打印'></i></a>";*/

                    html += "<a style='margin-left: 20px' href='#' onclick=quit('" + rowObject.billNo + "')><i class='ace-icon fa fa-check-circle-o' title='修改'></i></a>";

                    return html;

                }
            },
            {name: 'status', hidden: true},
            {name: 'inStatus', label: '入库状态', hidden: true},
            {
                name: 'statusImg', label: '状态', width: 20, align: 'center',
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
                            html = "<i class='fa fa-tasks blue' title='结束'></i>";
                            break;
                        default:
                            break;

                    }
                    return html;
                }
            },
            {
                name: 'inStatusImg', label: '入库状态', width: 20, align: 'center',
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.inStatus == 0) {
                        return '<i class="fa fa-tasks blue" title="订单状态"></i>';
                    } else if (rowObject.inStatus == 1) {
                        return '<i class="fa fa-sign-in blue" title="已入库"></i>';
                    } else if (rowObject.inStatus == 4) {
                        return '<i class="fa fa-sign-out blue" title="入库中"></i>';
                    } else {
                        return '';
                    }
                }
            },
            {name: 'billDate', label: '单据日期', sortable: true, width: 40},
            {name: 'origUnitId', label: '供应商ID', hidden: true},
            {name: 'origUnitName', label: '供应商', width: 40},
            {name: 'origId', label: '出库仓库ID', hidden: true},
            {name: 'origName', label: '出库仓库', hidden: true, width: 40},
            {name: 'destUnitId', label: '收货方ID', hidden: true},
            {name: 'destUnitName', label: '收货方', hidden: true, width: 40},
            {name: 'destId', label: '收货仓库ID', hidden: true},
            {name: 'destName', label: '收货仓库', width: 40},
            {name: 'totQty', label: '单据数量', sortable: false, width: 40},
            {name: 'totInQty', label: '已入库数量', width: 30},
            {name: 'totInVal', label: '总入库金额', width: 30,
                formatter: function (cellValue, options, rowObject) {
                    var totInVal=parseFloat(cellValue).toFixed(2);
                    return totInVal;
                }},
            {name: 'remark', label: '备注', sortable: false, width: 40},
            {name: 'id', hidden: true}
        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: true,
        sortname: 'billNo',
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
    var sum_totInQty = $("#grid").getCol('totInQty',false,'sum');
    var sum_totInVal = $("#grid").getCol('totInVal', false, 'sum');
    $("#grid").footerData('set', {
        billNo: "合计",
        totQty: sum_totQty,
        totInQty: sum_totInQty,
        totInVal: sum_totInVal,
    });
}

function add() {
    location.href = basePath + "/logistics/purchase/add.do";
}
function refresh() {
    location.reload(true);
}
function quit(billNo) {
    $.ajax({
        url:basePath + "/logistics/purchase/quit.do?billNo=" +billNo,
        cache: false,
        async: false,
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
/*function check(billNo) {
    var row = $("#grid").getRowData(billNo);
    if (row.status != 0) {
        bootbox.alert("不是录入状态，无法审核");
        return;
    }
    $.ajax({
        dataType: "json",
        url: basePath + "/logistics/purchase/check.do",
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
}*/
function check(billNo) {
    var row = $("#grid").getRowData(billNo);
    if (row.status != 0) {
        bootbox.alert("不是录入状态，无法审核");
        return;
    }
    bootbox.confirm({
        /*title: "余额确认",*/
        buttons: {confirm: {label: '确定'}, cancel: {label: '取消'}},
        message: "审核确定",
        callback: function (result) {
            /* $("#SODtl_save").removeAttr("disabled");*/
            if (result) {
                checkAjax(billNo);
            } else {
            }
        }
    });
}
function checkAjax(billNo) {
    $.ajax({
        dataType: "json",
        url: basePath + "/logistics/purchase/check.do",
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
/*function cancel(billNo) {
    var row = $("#grid").getRowData(billNo);
    if (row.status != 0) {
        bootbox.alert("不是录入状态，无法撤销");
        return;
    }
    $.ajax({
        dataType: "json",
        url: basePath + "/logistics/purchase/cancel.do",
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
}*/
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
        url: basePath + "/logistics/purchase/cancel.do",
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
   debugger;
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
function initProgressDialog() {
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

function doPrint(billNo) {
    $("#editForm").resetForm();
    $("#edit-dialog-print").modal('show');
    $("#form_code").removeAttr("readOnly");
    $("#billno").val(billNo);
    $("#edit-dialog-print").show();
    $.ajax({
        dataType: "json",
        url: basePath + "/sys/print/findAll.do",
        type: "POST",
        success: function (msg) {
            if (msg.success) {
                debugger;
                var addcont = "";
                for (var i = 0; i < msg.result.length; i++) {
                    if (billNo.indexOf(msg.result[i].type) >= 0) {
                        addcont += "<div class='form-group' onclick=set('" + msg.result[i].id + "') title='" + msg.result[i].name + "'>" +
                            "<button class='btn btn-info'>" +
                            "<i class='cae-icon fa fa-refresh'></i>" +
                            "<span class='bigger-10'>套打" + msg.result[i].name + "</span>" +
                            "</button>" +
                            "</div>"
                    }
                }
                $("#addbutton").html(addcont);

            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}
function set(id) {
    $.ajax({
        dataType: "json",
        url: basePath + "/sys/print/printMessage.do",
        data: {"id": id, "billno": $("#billno").val()},
        type: "POST",
        success: function (msg) {
            if (msg.success) {
                debugger;
                var print = msg.result.print;
                var cont = msg.result.cont;
                var contDel = msg.result.contDel;
                var LODOP = getLodop();
                //var LODOP=getLodop(document.getElementById('LODOP2'),document.getElementById('LODOP_EM2'));
                eval(print.printCont);
                var printCode = print.printCode;
                var printCodes = printCode.split(",");
                for (var i = 0; i < printCodes.length; i++) {
                    var plp = printCodes[i];
                    var message = cont[plp];
                    if (message != "" && message != null && message != undefined) {
                        LODOP.SET_PRINT_STYLEA(printCodes[i], 'Content', message);
                    } else {
                        LODOP.SET_PRINT_STYLEA(printCodes[i], 'Content', "");
                    }

                }

                var recordmessage = "";
                var sum = 0;
                var allprice = 0;
                for (var a = 0; a < contDel.length; a++) {
                    var conts = contDel[a];
                    recordmessage += "<tr style='border-top:1px dashed black;padding-top:5px;'>" +
                        "<td align='left' style='border-top:1px dashed black;padding-top:5px;'>" + conts.sku + "</td>" +
                        "<td align='right'style='border-top:1px dashed black;padding-top:5px;'>" + conts.qty + "</td>" +
                        "<td style='border-top:1px dashed black;padding-top:5px;'>" + conts.price.toFixed(2) + "</td>" +
                        "<td style='border-top:1px dashed black;padding-top:5px;'>" + conts.actPrice.toFixed(2) + "</td>" +
                        "<td align='right' style='border-top:1px dashed black;padding-top:5px;'>" + conts.totPrice.toFixed(2) + "</td>" +
                        "</tr>";

                    sum = sum + parseInt(conts.qty);
                    allprice = allprice + parseFloat(conts.totPrice.toFixed(2));
                }
                recordmessage += " <tr style='border-top:1px dashed black;padding-top:5px;'>" +
                    "<td align='left' style='border-top:1px dashed black;padding-top:5px;'>合计:</td>" +
                    "<td align='right'style='border-top:1px dashed black;padding-top:5px;'>" + sum + "</td>" +
                    "<td style='border-top:1px dashed black;padding-top:5px;'>&nbsp;</td>" +
                    " <td style='border-top:1px dashed black;padding-top:5px;'>&nbsp;</td>" +
                    "<td align='right' style='border-top:1px dashed black;padding-top:5px;'>" + allprice + "</td>" +
                    " </tr>";

                $("#loadtab").html(recordmessage);
                LODOP.SET_PRINT_STYLEA("baseHtml", 'Content', $("#edit-dialog2").html());
                //LODOP.PREVIEW();
                LODOP.PRINT();
                $("#edit-dialog-print").hide();


            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}
var dialogOpenPage;
function openSearchVendorDialog() {
    dialogOpenPage = "purchaseOrder";
    $("#modal_vendor_search_table").modal('show').on('shown.bs.modal', function () {
        initVendorSelect_Grid();
    });
    $("#searchVendorDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='confirm_selected_VendorId_purchaseOrder()'>确认</button>"
    );
}