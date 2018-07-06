var searchUrl = basePath + "/logistics/saleOrder/page.do?filter_GTI_status=-1&userId="+userId;
$(function () {
    initGrid();
    initForm();
    if(billNo){
        bootbox.alert("单据"+billNo+"正在编辑中");
    }else{
        sessionStorage.removeItem("billNosale");
    }

});
function initForm() {
    initSelectOrigForm();
    initSelectDestForm();
    initSelectBusinessIdForm();
    $(".selectpicker").selectpicker('refresh');
}
function initSelectBusinessIdForm() {
    var url;
    if (curOwnerId == "1") {
        url = basePath + "/sys/user/list.do?filter_EQI_type=4";
    } else {
        url = basePath + "/sys/user/list.do?filter_EQI_type=4&filter_EQS_ownerId=" + curOwnerId;
    }
    $.ajax({
        url: url,
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_busnissId").empty();
            $("#search_busnissId").append("<option value='' >--请选择销售员--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_busnissId").append("<option value='" + json[i].id + "'>" + json[i].name + "</option>");
                // $("#search_busnissId").selectpicker('refresh');
            }
            $("#search_busnissId").selectpicker('refresh');
        }
    });
}
//发货仓库
function initSelectOrigForm() {
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + curOwnerId,
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
            $("#search_origId").selectpicker('refresh');
        }
    });
}
//收货仓库，客户仓库
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
            $("#search_destId").selectpicker('refresh');
        }
    });
}

function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: basePath + "/logistics/saleOrder/page.do?filter_GTI_status=-1&userId=" + userId,
        datatype: "json",
        mtype: 'POST',
        colModel: [
            {name: 'billNo', label: '单据编号', sortable: true, width: 45},
            {
                name: "", label: "操作", width: 55, editable: false, align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    var billNo = rowObject.billNo;
                    var html;
                    html = "<a href='" + basePath + "/logistics/saleOrder/copyAdd.do?billNo=" + billNo + "'><i class='ace-icon fa fa-files-o' title='复制新增'></i></a>";
                    html += "<a style='margin-left: 20px' href='" + basePath + "/logistics/saleOrder/edit.do?billNo=" + billNo + "'><i class='ace-icon fa fa-edit' title='编辑'></i></a>";
                    html += "<a style='margin-left: 20px' href='#' onclick=check('" + billNo + "')><i class='ace-icon fa fa-check-square-o' title='审核'></i></a>";
                    html += "<a style='margin-left: 20px' href='#' onclick=cancel('" + billNo + "')><i class='ace-icon fa fa-undo' title='撤销'></i></a>";
                    html += "<a style='margin-left: 20px' href='#' onclick=quit('" + billNo + "')><i class='ace-icon fa fa-check-circle-o' title='修改'></i></a>";


                    /*html += "<a style='margin-left: 20px' href='#' onclick=doPrint('" + billNo + "')><i class='ace-icon fa fa-print' title='打印'></i></a>";*/
                    return html;
                }
            },
            {name: 'status', hidden: true},
            {name: 'destUnitId', label: '客户ID', hidden: true},
            {name: 'destUnitName', label: '客户', width: 40},
            {name: 'outStatus', label: '出库状态', hidden: true,sortable: false},
            {name: 'inStatus', label: '入库状态', hidden: true,sortable: false},
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
                            break;
                        case 6:
                            html = "<i class='fa fa-bars blue' title='商城订单暂存'></i>";
                            break;
                        case 7:
                            html = "<i class='fa fa-caret-square-o-down blue' title='商城订单录入'></i>";
                            break;
                        default:
                            break;
                    }
                    return html;
                }
            },
            {
                name: 'outStatusImg', label: '出库状态', width: 25, align: 'center',sortable: false,
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
                name: 'inStatusImg', label: '入库状态', width: 25, align: 'center',sortable: false,
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
            {name: 'billDate', label: '单据日期', sortable: true, width: 35},
            {name: 'customerTypeId', label: '客户类型', hidden: true},
            {
                label: '客户类型', sortable: false, width: 30,
                formatter: function (cellValue, options, rowObjec) {
                    if (rowObjec.customerTypeId == "CT-AT") {
                        return "省代客户";
                    } else if (rowObjec.customerTypeId == "CT-ST") {
                        return "门店客户";
                    } else if (rowObjec.customerTypeId == "CT-LS") {
                        return "零售客户";
                    }
                }
            },
            {name: 'origUnitId', label: '发货方ID', hidden: true},
            {name: 'origUnitName', label: '发货方', width: 30},
            {name: 'origId', label: '发货仓库ID', hidden: true},
            {name: 'origName', label: '发货仓库', width: 30},

            {name: 'destId', label: '收货仓库ID', hidden: true},
            {name: 'destName', label: '收货仓库', width: 30},
            {name: 'busnissId', label: '业务员Id', hidden: true},
            {name: 'busnissName', label: '业务员', width: 30},
            {name: 'totQty', label: '单据数量', width: 30},
            {name: 'totOutQty', label: '已出库数量', width: 30},
            // {name: 'totOutVal', label: '总出库金额', width: 30},
            {name: 'totInQty', label: '已入库数量', width: 30},
            // {name: 'totInVal', label: '总入库金额', width: 30},
            {name: 'actPrice', label: '应付付金额', width: 30,
                formatter: function (cellValue, options, rowObject) {
                    if(cellValue){
                        var actPrice=cellValue.toFixed(2);
                        return actPrice;
                    }else{
                        return cellValue;
                    }

                }
            },
            {name: 'payPrice', label: '实付金额', width: 30,
                formatter: function (cellValue, options, rowObject) {
                    if(cellValue){
                        var payPrice=cellValue.toFixed(2);
                        return payPrice;
                    }else{
                        return cellValue;
                    }

                }
            },
            {name: 'remark', label: '备注', width: 50}
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
function add() {
    location.href = basePath + "/logistics/saleOrder/add.do";
}
function refresh() {
    location.reload(true);
}
function quit(billNo) {
    $.ajax({
        url: basePath +"/logistics/saleOrder/quit.do?billNo=" +billNo,
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
        url: basePath + "/logistics/saleOrder/check.do",
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
        url: basePath + "/logistics/saleOrder/check.do",
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
        url: basePath + "/logistics/saleOrder/cancel.do",
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
        url: basePath + "/logistics/saleOrder/cancel.do",
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
var prefixId;
function openSearchGuestDialog() {
    dialogOpenPage = "saleOrder";
    prefixId="seach"
    $("#modal_guest_search_table").modal('show').on('shown.bs.modal', function () {
        initGuestSelect_Grid();
    });
    $("#searchGuestDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='confirm_selected_GuestId_sale()'>确认</button>"
    );
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
                        "<td align='left' style='border-top:1px dashed black;padding-top:5px;font-size:15px;'>" + conts.sku + "</td>" +
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
function setFooterData() {

    var sum_totQty = $("#grid").getCol('totQty', false, 'sum');
    var sum_totOutQty = $("#grid").getCol('totOutQty', false, 'sum');
    var sum_totOutVal = $("#grid").getCol('totOutVal', false, 'sum');
    var sum_totInQty = $("#grid").getCol('totInQty',false,'sum');
    var sum_totInVal = $("#grid").getCol('totInVal', false, 'sum');
    var sum_totActPrice = $("#grid").getCol('actPrice', false, 'sum');
    var sum_payPrice = $("#grid").getCol('payPrice', false, 'sum');
    /*$("#search_actPrice").val(sum_totActPrice);*/
    $("#grid").footerData('set', {
        billNo: "合计",
        totQty: sum_totQty,
        totOutQty: sum_totOutQty,
        totOutVal: sum_totOutVal,
        totInQty: sum_totInQty,
        totInVal: sum_totInVal,
        actPrice:sum_totActPrice,
        payPrice: sum_payPrice
    });
}
function addUniqCode() {
 /*   inOntWareHouseValid = 'addPage_scanUniqueCode';
    var origId = $("#search_origId").val();*/
   /* taskType = 0;*/
    /*wareHouse = origId;*/
   /* var ct = $("#search_customerType").val();
    if (ct && ct != null) {
        if (origId && origId != null) {*/
            $("#dialog_buttonGroup").html("" +
                "<button type='button'  class='btn btn-primary' onclick='addProductsOnCode()'>保存</button>"
            );
            $("#add-uniqCode-dialog").modal('show').on('hidden.bs.modal', function () {
                $("#uniqueCodeGrid").clearGridData();
            });
           /* initUniqeCodeGridColumn(ct);*/
     /*   } else {
            bootbox.alert("出库仓库不能为空！")
        }
    } else {
        bootbox.alert("请选择客户！");
    }
    allCodes = "";*/
}

function addProductsOnCode() {
    var rowId = $("#uniqueCodeGrid").jqGrid("getGridParam", "selrow");
    var rowData = $("#uniqueCodeGrid").jqGrid('getRowData', rowId);
    window.location.href= basePath + "/logistics/saleOrder/edit.do?billNo=" + rowData. billId;

}
