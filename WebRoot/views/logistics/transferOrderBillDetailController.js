/**
 * Created by yushen on 2017/7/4.
 */
var editDtailRowId = null;
var allCodes;//用于拼接所有添加过的唯一码，防止重复添加
var taskType; //用于判断出入库类型 1入库 0 出库
var wareHouse;
var inOntWareHouseValid; //用于判断在编辑BillDtl时出入库操作是否需要校验，使用哪种校验
var skuQty = {};//保存每个SKU对应的出入库数量
var allCodeStrInDtl; //每次表格loadComplete，取出所有明细中的唯一码
$(function () {
    initGrid();
    initForm();
    keydown();
    initButtonGroup();
    initEditFormValid();
    if (billNo != "") {
        sessionStorage.setItem("billNotransfer", billNo);
    }
});

function initForm() {
    initSelectOrigForm();
    initSelectDestForm();

    if (pageType === "add") {
        $("#search_billDate").val(getToDay("yyyy-MM-dd"));
    } else if (pageType === "edit") {
        $("#search_origId").val(transferOrder_origId);
        $("#search_destId").val(transferOrder_destId);

        if (transferOrder_status != "0") {
            $("#search_origId").attr('disabled', true);
            $("#search_destId").attr('disabled', true);
            $("#search_billDate").attr('readOnly', true);
        }
    } else if (pageType === "copyAdd") {
        $("#search_billNo").val("");
        $("#search_origId").val(transferOrder_origId);
        $("#search_destId").val(transferOrder_destId);
        $("#search_billDate").val(getToDay("yyyy-MM-dd"));
    }
    $(".selectpicker").selectpicker('refresh');
    $('.selectpicker').selectpicker('render');
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

function initButtonGroup() {
    editDtailRowId = null;

    if (pageType === "add") {
        $("#buttonGroup").html("" +
            "<button id='TODtl_save' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='save()'>" +
            "    <i class='ace-icon fa fa-save'></i>" +
            "    <span class='bigger-110'>保存</span>" +
            "</button>" +
            "<button id='TODtl_addUniqCode' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='addUniqCode()'>" +
            "    <i class='ace-icon fa fa-barcode'></i>" +
            "    <span class='bigger-110'>扫码</span>" +
            "</button>" +
            "<button id='TODtl_wareHouseOut' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='wareHouseOut()'>" +
            "    <i class='ace-icon fa fa-sign-out'></i>" +
            "    <span class='bigger-110'>出库</span>" +
            "</button>"
        );
    }
    if (pageType === "edit") {
        $("#buttonGroup").html("" +
            "<button id='TODtl_save' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='save()'>" +
            "    <i class='ace-icon fa fa-save'></i>" +
            "    <span class='bigger-110'>保存</span>" +
            "</button>" +
            "<button id='TODtl_addUniqCode' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='addUniqCode()'>" +
            "    <i class='ace-icon fa fa-barcode'></i>" +
            "    <span class='bigger-110'>扫码</span>" +
            "</button>" +
            "<button id='TODtl_wareHouseOut' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='wareHouseOut()'>" +
            "    <i class='ace-icon fa fa-sign-out'></i>" +
            "    <span class='bigger-110'>出库</span>" +
            "</button>" +
            "<button id='TODtl_wareHouseIn' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='wareHouseIn()'>" +
            "    <i class='ace-icon fa fa-sign-in'></i>" +
            "    <span class='bigger-110'>入库</span>" +
            "</button>" +
            "<button id='TRDtl_doPrintA4' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='doPrintA4()'>" +
            "    <i class='ace-icon fa fa-print'></i>" +
            "    <span class='bigger-110'>A4打印</span>" +
            "</button>"
        );
        if (transferOrder_status !== "0") {
            $("#search_orig_button").attr({"disabled": "disabled"});
            $("#search_dest_button").attr({"disabled": "disabled"});
            $("#TODtl_addUniqCode").attr({"disabled": "disabled"});
            $("#TODtl_save").attr({"disabled": "disabled"});
        }
        if (roleId != "0") {
            $("#TRDtl_doPrintA4").hide();
            $("#print_div").hide();
        }
    }
    $("#addDetail").show();
}

function initGrid() {

    $("#addDetailgrid").jqGrid({
        height: 'auto',
        url: basePath + "/logistics/transferOrder/findBillDtl.do?billNo=" + billNo,
        datatype: "json",
        colModel: [
            {name: 'id', label: 'id', hidden: true, width: 40},
            {name: 'billId', label: 'billId', hidden: true},
            {name: 'billNo', label: 'billNo', hidden: true},
            {name: 'status', hidden: true},
            {name: 'outStatus', hidden: true},
            {name: 'inStatus', hidden: true},
            {
                name: "operation", label: "操作", width: 30, editable: false, align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=saveItem('" + options.rowId + "')><i class='ace-icon ace-icon fa fa-save' title='保存'></i></a>"
                        + "<a style='margin-left: 20px' href='javascript:void(0);' onclick=deleteItem('" + options.rowId + "')><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";
                }
            },
            {
                name: 'statusImg', label: '状态', width: 20, align: 'center', hidden: true,
                formatter: function (cellValue, options, rowObject) {
                    return '<i class="fa fa-tasks blue" title="订单状态"></i>';
                }
            },
            {
                name: 'inStatusImg', label: '入库状态', width: 30, align: 'center', sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.status == 0) {
                        return '<i class="fa fa-tasks blue" title="订单状态"></i>';
                    } else if (rowObject.status == 1) {
                        return '<i class="fa fa-sign-in blue" title="已入库"></i>';
                    } else if (rowObject.status == 2) {
                        return '<i class="fa fa-truck blue blue" title="入库中"></i>';
                    } else {
                        return '';
                    }
                }
            },
            {
                name: 'outStatusImg', label: '出库状态', width: 30, align: 'center', sortable: false,
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
            {name: 'styleId', label: '款号', width: 40},
            {name: 'styleName', label: '款名', width: 40},
            {name: 'colorId', label: '色码', width: 40},
            {name: 'colorName', label: '颜色', width: 30},
            {name: 'sizeId', label: '尺码', width: 30},
            {name: 'sizeName', label: '尺码', width: 40},
            {
                name: 'qty', label: '数量', editable: true, width: 40,
                editrules: {
                    number: true,
                    minValue: 1
                },
                editoptions: {
                    dataInit: function (e) {
                        $(e).spinner();
                    }
                }
            },
            {name: 'outQty', label: '已出库数量', width: 40},
            {name: 'inQty', label: '已入库数量', width: 40},
            {name: 'sku', label: 'SKU', width: 40},
            {name: 'price', label: '调拨成本', width: 40, hidden: true},
            {name: 'totPrice', label: '总成本', width: 40, hidden: true},
            {name: 'uniqueCodes', label: '唯一码', hidden: true},
            {
                name: '', label: '唯一码明细', width: 40, align: "center",
                formatter: function (cellValue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=showCodesDetail('" + rowObject.uniqueCodes + "')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
                }
            }
        ],
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: -1,
        pager: '#addDetailgrid-pager',
        multiselect: false,
        shrinkToFit: true,
        sortname: 'id',
        sortorder: "asc",
        footerrow: true,
        // onSelectRow: function (rowid, status) {
        //     if (pageType != "edit") {
        //         if (editDtailRowId != null) {
        //             saveItem(editDtailRowId);
        //         }
        //         editDtailRowId = rowid;
        //         $('#addDetailgrid').editRow(rowid);
        //     }
        // },
        gridComplete: function () {
            setFooterData();
        },
        loadComplete: function () {
            initAllCodesList();
        }
    });
    if (pageType == "edit") {
        $("#addDetailgrid").setGridParam().hideCol("operation");
    } else {
        $("#addDetailgrid").setGridParam().showCol("operation");
        // $("#addDetailgrid").jqGrid('navGrid', "#addDetailgrid-pager",
        //     {
        //         edit: false,
        //         add: true,
        //         addicon: "ace-icon fa fa-plus",
        //         addfunc: function () {
        //             addDetail();
        //         },
        //         del: false,
        //         search: false,
        //         refresh: false,
        //         view: false
        //     }
        // );
    }
    $("#addDetailgrid-pager_center").html("");
}
function setFooterData() {
    var sum_qty = $("#addDetailgrid").getCol('qty', false, 'sum');
    var sum_outQty = $("#addDetailgrid").getCol('outQty', false, 'sum');
    var sum_inQty = $("#addDetailgrid").getCol('inQty', false, 'sum');
    var sum_totPrice = $("#addDetailgrid").getCol('totPrice', false, 'sum');
    $("#addDetailgrid").footerData('set', {
        styleId: "合计",
        qty: sum_qty,
        outQty: sum_outQty,
        inQty: sum_inQty,
        totPrice: sum_totPrice,
    });
}
function deleteItem(rowId) {
    $("#addDetailgrid").jqGrid("delRowData", rowId);
    setFooterData();
}
function saveItem(rowId) {
    $('#addDetailgrid').saveRow(rowId);
    var value = $('#addDetailgrid').getRowData(rowId);
    value.totPrice = value.qty * value.price;
    value.totActPrice = value.qty * value.actPrice;
    $("#addDetailgrid").setRowData(rowId, value);
    setFooterData();
}


function save() {
   cs.showProgressBar();
    $("#search_origId").removeAttr('disabled');
    $("#search_destId").removeAttr('disabled');

    if ($("#search_origId").val() == $("#search_destId").val()) {
        bootbox.alert("请选择不同的仓库进行调拨");
         cs.closeProgressBar();
        return;
    }

    $("#editForm").data('bootstrapValidator').destroy();
    $('#editForm').data('bootstrapValidator', null);
    initEditFormValid();
    $('#editForm').data('bootstrapValidator').validate();
    if (!$('#editForm').data('bootstrapValidator').isValid()) {
         cs.closeProgressBar();
        return;
    }

    if (editDtailRowId !== null) {
        $("#addDetailgrid").saveRow(editDtailRowId);
        editDtailRowId = null;
    }
    if ($("#addDetailgrid").getDataIDs().length === 0) {
        bootbox.alert("请添加入库商品");
         cs.closeProgressBar();
        return;
    }
    var dtlArray = [];
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        dtlArray.push(rowData);
    });
    $.ajax({
        dataType: "json",
        // async:false,
        url: basePath + "/logistics/transferOrder/save.do",
        data: {
            transferOrderBillStr: JSON.stringify(array2obj($("#editForm").serializeArray())),
            strDtlList: JSON.stringify(dtlArray),
            userId: userId
        },
        type: "POST",
        success: function (msg) {
             cs.closeProgressBar();
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                if ($("#search_billNo").val() === "" || $("#search_billNo").val() === null || $("#search_billNo").val() === undefined) {
                    $("#search_billNo").val(msg.result);
                }

                $("#addDetailgrid").jqGrid('setGridParam', {
                    page: 1,
                    url: basePath + "/logistics/transferOrder/findBillDtl.do?billNo=" + $("#search_billNo").val(),
                });
                $("#addDetailgrid").trigger("reloadGrid");
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}

function initAllCodesList() {
    allCodeStrInDtl = "";
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        allCodeStrInDtl = allCodeStrInDtl + "," + rowData.uniqueCodes;
    });
    if (allCodeStrInDtl != "") {
        if (allCodeStrInDtl.substr(0, 1) === ",") {
            allCodeStrInDtl = allCodeStrInDtl.substr(1);
        }
    }
}

function addUniqCode() {
    inOntWareHouseValid = 'addPage_scanUniqueCode';
    var origId = $("#search_origId").val();
    taskType = 0;
    wareHouse = origId;
    if (origId && origId !== null) {
        $("#dialog_buttonGroup").html("" +
            "<button type='button' id='so_savecode_button' class='btn btn-primary' onclick='addProductsOnCode()'>保存</button>"
        );
        $("#add-uniqCode-dialog").modal('show').on('hidden.bs.modal', function () {
            $("#uniqueCodeGrid").clearGridData();
        });
        initUniqeCodeGridColumn("showStockPrice");
        $("#codeQty").text(0);
    } else {
        bootbox.alert("发货仓库不能为空！");
    }
    allCodes = "";
}

function addProductsOnCode() {
    if (!$('#so_savecode_button').prop('disabled')) {
        $("#so_savecode_button").attr({"disabled": "disabled"});
        var productListInfo = [];
        $.each($("#uniqueCodeGrid").getDataIDs(), function (index, value) {
            var productInfo = $("#uniqueCodeGrid").getRowData(value);
            productInfo.qty = 1;
            productInfo.outQty = 0;
            productInfo.inQty = 0;
            productInfo.status = 0;
            productInfo.inStatus = 0;
            productInfo.outStatus = 0;
            productInfo.uniqueCodes = productInfo.code;
            productInfo.price = parseFloat(productInfo.stockPrice);
            productInfo.totPrice = parseFloat(productInfo.stockPrice);
            productListInfo.push(productInfo);
        });
        if (productListInfo.length === 0) {
            bootbox.alert("请添加唯一码");
            return;
        }
        var isAdd = true;
        $.each(productListInfo, function (index, value) {
            isAdd = true;
            $.each($("#addDetailgrid").getDataIDs(), function (dtlIndex, dtlValue) {
                var dtlRow = $("#addDetailgrid").getRowData(dtlValue);
                if (value.sku === dtlRow.sku) {
                    if (dtlRow.uniqueCodes.indexOf(value.code) !== -1) {
                        isAdd = false;
                        $.gritter.add({
                            text: value.code + "不能重复添加",
                            class_name: 'gritter-success  gritter-light'
                        });
                        return true;
                    }
                    dtlRow.qty = parseInt(dtlRow.qty) + 1;
                    dtlRow.totPrice = parseFloat(dtlRow.totPrice) + parseFloat(dtlRow.price);
                    dtlRow.price = dtlRow.totPrice / dtlRow.qty;
                    dtlRow.uniqueCodes = dtlRow.uniqueCodes + "," + value.code;
                    if (dtlRow.id) {
                        $("#addDetailgrid").setRowData(dtlRow.id, dtlRow);
                    } else {
                        $("#addDetailgrid").setRowData(dtlIndex, dtlRow);
                    }
                    isAdd = false;
                }
            });
            if (isAdd) {
                $("#addDetailgrid").addRowData($("#addDetailgrid").getDataIDs().length, value);
            }
        });
        $("#so_savecode_button").removeAttr("disabled");
        $("#add-uniqCode-dialog").modal('hide');
        setFooterData();
    }
}

function wareHouseOut() {
    $("#TODtl_wareHouseOut").attr({"disabled": "disabled"});
    var sum_qty = parseInt($("#addDetailgrid").footerData('get').qty);
    var sum_outQty = parseInt($("#addDetailgrid").footerData('get').outQty);

    if (sum_qty === sum_outQty) {
        $.gritter.add({
            text: '已全部出库',
            class_name: 'gritter-success  gritter-light'
        });
        $("#TODtl_wareHouseOut").removeAttr("disabled");
        return;
    }


   cs.showProgressBar();
    var billNo = $("#search_billNo").val();
    if (billNo && billNo !== null) {

        var epcArray = [];
        $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
            var rowData = $("#addDetailgrid").getRowData(value);
            var codes = rowData.uniqueCodes.split(",");
            if (codes && codes !== null && codes !== "") {
                $.each((codes), function (index, value) {
                    // if (uniqueCodes_inHouse.indexOf(value) !== -1) {
                    var epc = {};
                    epc.code = value;
                    epc.styleId = rowData.styleId;
                    epc.sizeId = rowData.sizeId;
                    epc.colorId = rowData.colorId;
                    epc.qty = 1;
                    epc.sku = rowData.sku;
                    epcArray.push(epc);
                    // }
                })
            }
        });
        if (epcArray.length === 0) {
            $.gritter.add({
                text: "请扫码添加出库商品",
                class_name: 'gritter-success  gritter-light'
            });
             cs.closeProgressBar();
            $("#TODtl_wareHouseOut").removeAttr("disabled");
            return;
        }

        var dtlArray = [];
        $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
            var rowData = $("#addDetailgrid").getRowData(value);
            dtlArray.push(rowData);
        });

        $.ajax({
            dataType: "json",
            // async:false,
            url: basePath + "/logistics/transferOrder/convertOut.do",
            data: {
                billNo: billNo,
                strEpcList: JSON.stringify(epcArray),
                strDtlList: JSON.stringify(dtlArray),
                userId: userId
            },
            type: "POST",
            success: function (msg) {
                 cs.closeProgressBar();
                $("#TODtl_wareHouseOut").removeAttr("disabled");
                if (msg.success) {
                    $.gritter.add({
                        text: msg.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                    $("#modal-addEpc-table").modal('hide');
                    $("#addDetailgrid").jqGrid('setGridParam', {
                        page: 1,
                        url: basePath + "/logistics/transferOrder/findBillDtl.do?billNo=" + billNo,
                    });
                    $("#addDetailgrid").trigger("reloadGrid");
                    setFooterData();
                    bootbox.alert({
                        buttons: {ok: {label: '确定'}},
                        message: "已出库" + epcArray.length + "件商品",
                        callback: function () {
                            quitback();
                            window.location.href = basePath + "/logistics/transferOrder/index.do";
                        },
                    });
                } else {
                    bootbox.alert(msg.msg);
                }
            }
        });
    } else {
        bootbox.alert("请先保存当前单据");
    }

}
function quitback() {
    $.ajax({
        url: basePath + "/logistics/transferOrder/quit.do?billNo=" + billNo,
        cache: false,
        async: true,
        type: "POST",
        success: function (data, textStatus) {

            if (textStatus == "success") {
                $.gritter.add({
                    text: billNo + "可以编辑",
                    class_name: 'gritter-success  gritter-light'
                });

            }

        }
    });
}


function wareHouseIn() {
    skuQty = {};
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        skuQty[rowData.sku] = rowData.inQty;
    });

    inOntWareHouseValid = 'wareHouseIn_valid';
    taskType = 1;
    var destId = $("#search_destId").val();
    wareHouse = destId;
    if (destId && destId != null) {
        $("#dialog_buttonGroup").html("" +
            "<button type='button' id='WareHouseIn_dialog_buttonGroup' class='btn btn-primary' onclick='confirmWareHouseIn()'>确认入库</button>"
        );
        $("#add-uniqCode-dialog").modal('show').on('hidden.bs.modal', function () {
            $("#uniqueCodeGrid").clearGridData();
        });
        initUniqeCodeGridColumn("showStockPrice");
        $("#codeQty").text(0);
    } else {
        bootbox.alert("入库仓库不能为空！");
    }
    allCodes = "";
}

function confirmWareHouseIn() {
    $("#WareHouseIn_dialog_buttonGroup").attr({"disabled": "disabled"});
   cs.showProgressBar();
    var billNo = $("#search_billNo").val();

    var epcArray = [];
    $.each($("#uniqueCodeGrid").getDataIDs(), function (index, value) {
        var rowData = $("#uniqueCodeGrid").getRowData(value);
        epcArray.push(rowData);
    });
    if (epcArray.length == 0) {
        bootbox.alert("请添加唯一码!");
         cs.closeProgressBar();
        $("#WareHouseIn_dialog_buttonGroup").removeAttr("disabled");
        return;
    }

    $.ajax({
        dataType: "json",
        // async:false,
        url: basePath + "/logistics/transferOrder/convertIn.do",
        data: {
            billNo: billNo,
            strEpcList: JSON.stringify(epcArray),
            userId: userId
        },
        type: "POST",
        success: function (msg) {
             cs.closeProgressBar();
            $("#WareHouseIn_dialog_buttonGroup").removeAttr("disabled");
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#modal-addEpc-table").modal('hide');
                $("#addDetailgrid").trigger("reloadGrid");
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });

    $("#add-uniqCode-dialog").modal('hide');
}

function initEditFormValid() {
    $('#editForm').bootstrapValidator({
        message: '输入值无效',
        excluded: [':disabled'],
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
            billNo: {
                validators: {}
            },
            origUnitId: {
                validators: {
                    notEmpty: {
                        message: '请选择发货组织'
                    }
                }
            },
            origId: {
                validators: {
                    notEmpty: {
                        message: '请选择发货仓库'
                    }
                }
            },
            destUnitId: {
                validators: {
                    notEmpty: {
                        message: '请选择收货组织'
                    }
                }
            },
            destId: {
                validators: {
                    notEmpty: {
                        message: '请选择收货仓库'
                    }
                }
            },
            billDate: {
                validators: {
                    notEmpty: {
                        message: '请选择单据日期'
                    }
                }
            }
        }
    });
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
function showCodesDetail(uniqueCodes) {

    $("#show-uniqueCode-list").modal('show');
    initUniqueCodeList(uniqueCodes);
    codeListReload(uniqueCodes);
}
function doPrintA4() {
    var billno = $("#search_billNo").val();
    $.ajax({
        dataType: "json",
        url: basePath + "/logistics/transferOrder/printA4Info.do",
        data: {"billNo": billno},
        type: "POST",
        success: function (msg) {
            if (msg.success) {

                var print = msg.result.print;
                var bill = msg.result.bill;
                var billDtl = msg.result.dtl;
                var LODOP = getLodop();
                eval(print.printCont);
                LODOP.SET_PRINT_STYLEA("remark", 'Content', bill.remark);
                LODOP.SET_PRINT_STYLEA("storehouseName", 'Content', bill.origUnitName + "-" + bill.origName);
                var recordmessage = "";
                var totQty = 0;
                $.each(billDtl, function (index, value) {
                    recordmessage += "<tr style='border-top:1px ;padding-top:5px;'>" +
                        "<td align='left' style='border-top:1px ;padding-top:5px;width: 20%;font-size:17px;'>" + value.styleId + "</td>" +
                        "<td align='left' style='border-top:1px ;padding-top:5px;width: 20%;font-size:17px;'>" + value.styleName + "</td>";
                    if (value.supplierName == undefined) {
                        recordmessage += "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>" + "" + "</td>";
                    } else {
                        recordmessage += "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>" + value.supplierName + "</td>";
                    }
                    var qty = 0;
                    switch ($("#form_printSelect").val()) {
                        case "0":
                            qty = value.inQty
                            break;
                        case "1":
                            qty = value.outQty
                            break;
                        case "2":
                            qty = value.qty
                            break;
                    }
                    totQty += qty;
                    recordmessage += "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>" + qty + "</td>" +
                        "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>" + value.price.toFixed(2) + "</td>" +
                        "</tr>";
                });

                recordmessage += "<tr style='border-top:1px ;padding-top:5px;'>" +
                    "<td align='left' style='border-top:1px ;padding-top:5px;width: 20%;font-size:17px;'>&nbsp;</td>" +
                    "<td align='left' style='border-top:1px ;padding-top:5px;width: 20%;font-size:17px;'>&nbsp;</td>" +
                    "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>&nbsp;</td>" +
                    "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>" + totQty + "</td>" +
                    "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>&nbsp;</td>" +
                    "</tr>";
                $("#loadtabA4").html(recordmessage);
                //alert($("#edit-dialogA4").html());
                console.log($("#edit-dialogA4").html());
                LODOP.SET_PRINT_STYLEA("baseHtml", 'Content', $("#edit-dialogA4").html());
                //LODOP.PREVIEW();
                LODOP.PRINT();
                $("#edit-dialog-print").hide();


            } else {
                bootbox.alert(msg.msg);
            }
        }
    });


}
