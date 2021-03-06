var searchUrl = basePath + "/logistics/transferOrder/page.do?filter_GTI_status=-1";
var billNo;
var taskType; //用于判断出入库类型 1入库 0 出库
var wareHouse;
var isCheckWareHouse=true;//是否检测出库仓库
$(function () {
    load().then(function (data) {
        /*初始化左侧grig*/
        initGrid();
    });
});

function load() {
    var promise = new Promise(function(resolve, reject){
        /*初始化右侧grig*/
        initAddGrid();
        initButtonGroup(0);
        setEditFormVal();
        initForm();
        /*初始化右侧表单验证*/
        initEditFormValid();
        /*回车监事件*/
        keydown();
        resolve("success");
    });
    return promise;
}

function initForm() {
    initSelectOrigForm();
    initSelectDestForm();
    initSelectOrigEditForm();
    initSelectDestEditForm();
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
function initSelectOrigEditForm() {
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + $("#edit_origUnitId").val(),
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#edit_origId").empty();
            $("#edit_origId").append("<option value=''>--请选择出库仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#edit_origId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
            }
        }
    });
}
function initSelectDestEditForm() {
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + $("#edit_destUnitId").val(),
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#edit_destId").empty();
            $("#edit_destId").append("<option value=''>--请选择入库仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#edit_destId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
            }

        }
    });
}
function initSelectDestForm() {
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + $("#edit_destUnitId").val(),
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
    var url="";
    if (cargoTrack=="cargoTracking"){
        url= basePath + "/logistics/transferOrder/list.do?billNo="+cTbillNo;
    }else {
        url = basePath + "/logistics/transferOrder/page.do?filter_GTI_status=-1";
    }
    $("#grid").jqGrid({
        height: "auto",
        url:url,
        datatype: "json",
        mtype: 'POST',
        colModel: [

            {name: 'billNo', label: '单据编号', sortable: true, width: 35},
            {name: 'status', hidden: true},
            {name: 'outStatus', label: '出库状态', hidden: true},
            {name: 'inStatus', label: '入库状态', hidden: true},
            {name: "ownerId", hidden: true},
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
            {name: 'billDate', label: '单据日期', sortable: true, width:18},
            {name: 'origUnitId', label: '发货方ID', hidden: true},
            {name: 'origUnitName', label: '发货方', width: 30,hidden: true},
            {name: 'origId', label: '发货仓库ID', hidden: true},
            {name: 'origName', label: '出库仓库', width: 20},
            {name: 'destUnitId', label: '出库方ID', hidden: true},
            {name: 'destUnitName', label: '入库方', width: 20},
            {name: 'destId', label: '入库仓库ID', hidden: true},
            {name: 'destName', label: '收货仓库', width: 30,hidden: true},
            {name: 'totQty', label: '单据数量', width: 15},
            {name: 'totOutQty', label: '已出库数量', width: 15},
            {name: 'totOutVal', label: '总出库金额', width: 30, hidden:true},
            {name: 'totInQty', label: '已入库数量', width: 15},
            {name: 'totInVal', label: '总入库金额', width: 30, hidden:true},
            {name: 'remark', label: '备注', width: 50,hidden: true},
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
        sortorder: 'desc',
        autoScroll: false,
        footerrow: true,
        gridComplete: function () {
            setFooterData();
        },
        onSelectRow: function (rowid, status) {
            initDetailData(rowid)
        },
        loadComplete:function () {
            if (cargoTrack=="cargoTracking"){
                initDetailData(cTbillNo);
                $("#search_billId").val(cTbillNo);
            }
        }
    });
}
function initDetailData(rowid) {
    var rowData = $("#grid").getRowData(rowid);
    //add by yushen  选择其他调拨单时，下拉框重新加载所有仓库，否则其他单据有可能显示不出来仓库
    $("#edit_origUnitId").val('');
    $("#edit_destUnitId").val('');
    initSelectOrigEditForm();
    initSelectDestEditForm();

    loadDetailData(rowData);
}

//右边明细表加载数据
function loadDetailData(rowData) {
    $("#editForm").setFromData(rowData);
    transferOrder_status=rowData.status;

    //modify by yushen  在选择不同单据时，根据单据状态禁用表单内容，录入单不允许修改入库仓库，但是如果改变了入库方，则可以修改入库仓库
    if (transferOrder_status != "0") {
        $("#edit_billDate").attr('readOnly', true);
        $("#edit_dest_button").attr('disabled', true);
    }else {
        $("#edit_billDate").attr('readOnly', false);
        $("#edit_dest_button").attr('disabled', false);
    }
    $("#edit_destId").attr('disabled', true);
    $("#edit_origId").attr('disabled', true);
    $("#edit_orig_button").attr('disabled', true);
    $(".selectpicker").selectpicker('refresh');
    $('#addDetailgrid').jqGrid("clearGridData");
    $('#addDetailgrid').jqGrid('GridUnload');
    pageType="edit";
    initeditGrid(rowData.billNo);
    initButtonGroup(transferOrder_status);
    $("#addDetailgrid").trigger("reloadGrid");
}
function initAddGrid() {
    $("#addDetailgrid").jqGrid({
        height: 'auto',
        datatype: "local",
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
            {name: 'colorId', label: '色码', width: 40, hidden: true},
            {name: 'colorName', label: '颜色', width: 30},
            {name: 'sizeId', label: '尺码', width: 30, hidden: true},
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
            },
            {
                name: '', label: '异常唯一码明细', width: 75, align: "center",
                formatter: function (cellValue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=showCodesDetail('" + rowObject.noOutPutCode + "')><i class='ace-icon ace-icon fa fa-list' title='显示异常唯一码明细'></i></a>";
                }
            },
            {name: 'noOutPutCode', label: '异常唯一码', hidden: true}
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
        gridComplete: function () {
            setAddFooterData();
        },
        loadComplete: function () {
            initAllCodesList();
        }
    });
    if (pageType == "edit") {
        $("#addDetailgrid").setGridParam().hideCol("operation");
    } else {
        $("#addDetailgrid").setGridParam().showCol("operation");

    }
    $("#addDetailgrid-pager_center").html("");
}
function initeditGrid(billNo) {
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
            {name: 'colorId', label: '色码', width: 40, hidden: true},
            {name: 'colorName', label: '颜色', width: 30},
            {name: 'sizeId', label: '尺码', width: 30, hidden: true},
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
            },
            {
                name: '', label: '异常唯一码明细', width: 75, align: "center",
                formatter: function (cellValue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=showCodesDetail('" + rowObject.noOutPutCode + "')><i class='ace-icon ace-icon fa fa-list' title='显示异常唯一码明细'></i></a>";
                }
            },
            {name: 'noOutPutCode', label: '异常唯一码', hidden: true}
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
        gridComplete: function () {
            setAddFooterData();
        },
        loadComplete: function () {
            initAllCodesList();
        }
    });
    if (pageType == "edit") {
        $("#addDetailgrid").setGridParam().hideCol("operation");
    } else {
        $("#addDetailgrid").setGridParam().showCol("operation");

    }
    $("#addDetailgrid-pager_center").html("");
}
function setAddFooterData() {
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
function _resetForm(){
    $("#searchForm").clearForm();
    $("#search_destId").val();
    $("#search_origId").val();
    $("#filter_INI_outStatus").val();
    $("#filter_INI_inStatus").val();
    $(".selectpicker").selectpicker('refresh');
}
var dialogOpenPage;
var prefixId;
function openSearchOrigDialog(preId) {
    dialogOpenPage = "transferOrderOrig";
    prefixId =preId;
    origIdVal = $("#edit_origId").val();
    $("#modal_guest_search_table").modal('show').on('shown.bs.modal', function () {
        initGuestSelect_Grid();
    });
    $("#searchGuestDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='confirm_selected_OrigUnit()'>确认</button>"
    );
}
function openSearchDestDialog(preId) {
    dialogOpenPage = "transferOrderUnit";
    prefixId =preId;
    destIdVal = $("#edit_destId").val();
    $("#modal_guest_search_table").modal('show').on('shown.bs.modal', function () {
        initGuestSelect_Grid();
    });
    $("#searchGuestDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='confirm_selected_DestUnit()'>确认</button>"
    );
}

//add by yushen 未保存前如果发货仓库发生改变清空调拨明细
var origIdVal;
function origIdChange(){
    var preOrigId = origIdVal;
    var afterOrigId = $("#edit_origId").val();

    if(preOrigId !== afterOrigId){
        $("#addDetailgrid").jqGrid("clearGridData");
    }
}
//如果入库方发生改变，取消入库仓库的选择禁用
var destIdVal;
function destIdChange() {
    var preDestId = destIdVal;
    var afterDestId = $("#edit_destId").val();

    if(preDestId !== afterDestId){
        $("#edit_destId").removeAttr('disabled');
        $(".selectpicker").selectpicker('refresh');
    }
}
/*
 *查询code明细
 */
function showCodesDetail(uniqueCodes) {
    debugger;

    $("#show-uniqueCode-list").modal('show');
    initUniqueCodeList(uniqueCodes);
    codeListReload(uniqueCodes);
}
function deleteItem(rowId) {
    $("#addDetailgrid").jqGrid("delRowData", rowId);
    setAddFooterData();
}
function saveItem(rowId) {
    $('#addDetailgrid').saveRow(rowId);
    var value = $('#addDetailgrid').getRowData(rowId);
    value.totPrice = value.qty * value.price;
    value.totActPrice = value.qty * value.actPrice;
    $("#addDetailgrid").setRowData(rowId, value);
    setAddFooterData();
}
/**
 * 加载按钮
 */
function initButtonGroup(billStatus) {
    editDtailRowId = null;
    $("#buttonGroup").html("" +
        "<button id='TRDtl_add' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='addNew()'>" +
        "    <i class='ace-icon fa fa-plus'></i>" +
        "    <span class='bigger-110'>新增</span>" +
        "</button>" +
        "<button id='TRDtl_cancel' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='cancel()'>" +
        "    <i class='ace-icon fa fa-undo'></i>" +
        "    <span class='bigger-110'>撤销</span>" +
        "</button>" +
        "<button id='TRDtl_save' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='save()'>" +
        "    <i class='ace-icon fa fa-save'></i>" +
        "    <span class='bigger-110'>保存</span>" +
        "</button>" +
        "<button id='TRDtl_addUniqCode' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='addUniqCode()'>" +
        "    <i class='ace-icon fa fa-barcode'></i>" +
        "    <span class='bigger-110'>扫码</span>" +
        "</button>" +
        "<button id='cm_scan_codes' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='addAllUniqCode()'>" +
        "    <i class='ace-icon fa fa-undo'></i>" +
        "    <span class='bigger-110'>批量扫码</span>" +
        "</button>" +
        "<button id='TRDtl_wareHouseOut' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='wareHouseOut()'>" +
        "    <i class='ace-icon fa fa-sign-out'></i>" +
        "    <span class='bigger-110'>出库</span>" +
        "</button>" +
        "<button id='cm_scancodes_in' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='batchWareHouseOut()'>" +
        "    <i class='ace-icon fa fa-undo'></i>" +
        "    <span class='bigger-110'>批量出库</span>" +
        "</button>" +
        "<button id='TRDtl_wareHouseIn' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='wareHouseIn()'>" +
        "    <i class='ace-icon fa fa-sign-in'></i>" +
        "    <span class='bigger-110'>入库</span>" +
        "</button>" +
        "<button id='cm_scancodes_in' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='batchWareHouseIn()'>" +
        "    <i class='ace-icon fa fa-undo'></i>" +
        "    <span class='bigger-110'>批量入库</span>" +
        "</button>" +
        "<button id='TRDtl_floorallocation' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='floorallocation()'>" +
        "    <i class='ace-icon fa fa-sign-in'></i>" +
        "    <span class='bigger-110'>库位详情</span>" +
        "</button>" +
        "<button id='TRDtl_wareHouseInAll' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='wareHouseInAll()'>" +
        "    <i class='ace-icon fa fa-sign-in'></i>" +
        "    <span class='bigger-110'>全部入库</span>" +
        "</button>" +
        "<button id='TRDtl_doPrint' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='doPrint()'>" +
        "    <i class='ace-icon fa fa-print'></i>" +
        "    <span class='bigger-110'>打印</span>" +
        "</button>"+
        "<button id='TRDtl_doPrintA4Size' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='doPrintA4Size()'>" +
        "    <i class='ace-icon fa fa-print'></i>" +
        "    <span class='bigger-110'>A4打印(无尺码信息)</span>" +
        "</button>"+
        "<button id='TRDtl_doPrintA4' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='doPrintA4()'>" +
        "    <i class='ace-icon fa fa-print'></i>" +
        "    <span class='bigger-110'>A4打印</span>" +
        "</button>"+
        "<button id='TRDtl_doPrintSanLian' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='doPrintSanLian()'>" +
        "    <i class='ace-icon fa fa-print'></i>" +
        "    <span class='bigger-110'>打印(三联)</span>" +
        "</button>"
    );
    loadingButtonDivTable(billStatus);

    $("#addDetail").show();

}
/**
 * billStatus 单据状态新增为0
 * 动态配置按钮,div,表格列字段
 * */
function loadingButtonDivTable(billStatus) {
    var privilegeMap = ButtonAndDivPower(resourcePrivilege);
    //初始化表格权限
    $.each(privilegeMap['table'],function(index,value){
        if(value.isShow!==0) {
            $('#addDetailgrid').setGridParam().hideCol(value.privilegeId);
        }
    });
    var disableButtonIds = "";
    switch (billStatus){
        case "-1" :
            disableButtonIds = ["TRDtl_cancel","TRDtl_addUniqCode","TRDtl_wareHouseOut","TRDtl_wareHouseIn"];
            break;
        case "0" :
            disableButtonIds = ["TRDtl_wareHouseIn"];
            break;
        case "1":
            disableButtonIds = ["TRDtl_cancel","TRDtl_save,TRDtl_addUniqCode"];
            break;
        case "2" :
            disableButtonIds = ["TRDtl_cancel","TRDtl_save","TRDtl_addUniqCode","TRDtl_wareHouseOut","TRDtl_wareHouseIn","TRDtl_floorallocation","TRDtl_wareHouseInAll"];
            break;
        case "3":
            disableButtonIds = ["TRDtl_cancel","TRDtl_save","TRDtl_addUniqCode"];
            break;
        default:
            disableButtonIds = ["TRDtl_wareHouseIn"];
    }
    //根据单据状态disable按钮
    $.each(privilegeMap['button'],function(index,value){
        if($.inArray(value.privilegeId,disableButtonIds)!= -1){
            $("#"+value.privilegeId).attr({"disabled": "disabled"});
        }else{
            $("#"+value.privilegeId).removeAttr("disabled");
        }
    });

}

/**
 * 新增单据调用
 *
 * */
function addNew(isScan){
    $('#addDetailgrid').jqGrid("clearGridData");
    $('#addDetailgrid').jqGrid('GridUnload');
    initAddGrid();
    $("#editForm").clearForm();
    $("#edit_status").val("");
    setEditFormVal();
    $("#addDetailgrid").trigger("reloadGrid");
    $(".selectpicker").selectpicker('refresh');
    $("#form_printSelect").val(0);
    pageType="add";
    initButtonGroup(0);
    $("#edit_origId").attr('disabled', false);
    $("#edit_orig_button").attr('disabled', false);

}
function setEditFormVal(){
    $("#edit_billDate").val(getToDay("yyyy-MM-dd"));
    $("#edit_origId").removeAttr("disabled");
    $("#edit_destId").removeAttr("disabled");
    $("#edit_dest_button").removeAttr("disabled");
    $("#edit_orig_button").removeAttr("disabled");
}

function save() {
    cs.showProgressBar();
    $("#edit_origId").removeAttr('disabled');
    $("#edit_destId").removeAttr('disabled');

    if ($("#edit_origId").val() == $("#edit_destId").val()) {
        bootbox.alert("请选择不同的仓库进行调拨");
        cs.closeProgressBar();
        return;
    }

    if(userId !== 'admin'){
        if($("#edit_origUnitId").val() !== curOwnerId && $("#edit_destUnitId").val() !== curOwnerId){
            bootbox.alert("发货方或出货方必须有一个是本店");
            cs.closeProgressBar();
            return;
        }
    }


    $("#editForm").data('bootstrapValidator').destroy();
    $('#editForm').data('bootstrapValidator', null);
    initEditFormValid();
    $('#editForm').data('bootstrapValidator').validate();
    if (!$('#editForm').data('bootstrapValidator').isValid()) {
        cs.closeProgressBar();
        return;
    }
    console.log($("#edit_status").val());
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
    $("#edit_billDate").val(updateTime($("#edit_billDate").val()));
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
                if ($("#edit_billNo").val() === "" || $("#edit_billNo").val() === null || $("#edit_billNo").val() === undefined) {
                    $("#edit_billNo").val(msg.result.billNo);
                }

                $("#addDetailgrid").jqGrid('setGridParam', {
                    page: 1,
                    url: basePath + "/logistics/transferOrder/findBillDtl.do?billNo=" + $("#edit_billNo").val(),
                });
                $("#addDetailgrid").trigger("reloadGrid");
                $("#grid").trigger("reloadGrid");
                addNew();
                _search();
                loadDetailData(msg.result);
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
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
/**
 * 扫描
 */
function addUniqCode() {
    inOntWareHouseValid = 'addPage_scanUniqueCode';
    var origId = $("#edit_origId").val();
    taskType = 0;
    wareHouse = origId;
    billNo = $("#edit_billNo").val();
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
        setAddFooterData();
    }
}
/**
 * 出库
 */
/*function wareHouseOut() {
    $("#TRDtl_wareHouseOut").attr({"disabled": "disabled"});
    var sum_qty = parseInt($("#addDetailgrid").footerData('get').qty);
    var sum_outQty = parseInt($("#addDetailgrid").footerData('get').outQty);

    if (sum_qty === sum_outQty) {
        $.gritter.add({
            text: '已全部出库',
            class_name: 'gritter-success  gritter-light'
        });
        $("#addDetailgrid").trigger("reloadGrid");
        $("#TRDtl_wareHouseOut").removeAttr("disabled");
        return;
    }


    cs.showProgressBar();
    var billNo = $("#edit_billNo").val();
    if (billNo && billNo !== null) {

        var epcArray = [];
        $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
            var rowData = $("#addDetailgrid").getRowData(value);
            if (rowData.uniqueCodes && rowData.uniqueCodes !== null && rowData.uniqueCodes !== "") {
                var codes = rowData.uniqueCodes.split(",");
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
            $("#TRDtl_wareHouseOut").removeAttr("disabled");
            edit_wareHouseOut();
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
                $("#TRDtl_wareHouseOut").removeAttr("disabled");
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
                    $("#grid").trigger("reloadGrid");
                    initButtonGroup(transferOrder_status);
                    setFooterData();
                    bootbox.alert("已出库" + epcArray.length + "件商品");
                } else {
                    bootbox.alert(msg.msg);
                }
            }
        });
    } else {
        cs.closeProgressBar();
        bootbox.alert("请先保存当前单据");
    }

}*/
function wareHouseOut() {
    $("#TRDtl_wareHouseOut").attr({"disabled": "disabled"});
    var sum_qty = parseInt($("#addDetailgrid").footerData('get').qty);
    var sum_outQty = parseInt($("#addDetailgrid").footerData('get').outQty);
    if (sum_qty === sum_outQty) {
        $.gritter.add({
            text: '已全部出库',
            class_name: 'gritter-success  gritter-light'
        });
        $("#addDetailgrid").trigger("reloadGrid");
        $("#TRDtl_wareHouseOut").removeAttr("disabled");
        return;
    }
    cs.showProgressBar();
    var billNo = $("#edit_billNo").val();
    if (billNo && billNo !== null) {
        var allUniqueCodes = "";
        $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
            var rowData = $("#addDetailgrid").getRowData(value);
            allUniqueCodes = allUniqueCodes + "," + rowData.uniqueCodes;
        });
        $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
            var rowData = $("#addDetailgrid").getRowData(value);
            allUniqueCodes = allUniqueCodes + "," + rowData.noOutPutCode;
        });
        if (allUniqueCodes.substr(0, 1) == ",") {
            allUniqueCodes = allUniqueCodes.substr(1);
        }
        taskType = 0;
        wareHouse = $("#edit_origId").val();
        var uniqueCodes_inHouse;
        $.ajax({
            async: true,
            dataType: "json",
            url: basePath + "/stock/warehStock/checkCodes.do",
            data: {
                warehId: wareHouse,
                codes: allUniqueCodes,
                type: taskType,
                billNo: billNo
            },
            type: "POST",
            success: function (data) {
                debugger;
                uniqueCodes_inHouse = data.result;
                var epcArray = [];
                $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
                        var rowData = $("#addDetailgrid").getRowData(value);
                        var codes = rowData.uniqueCodes.split(",");
                        var noOutcodes ="";
                        if(rowData.noOutPutCode!=undefined&& rowData.noOutPutCode != null && rowData.noOutPutCode != "") {
                            noOutcodes = rowData.noOutPutCode.split(",");
                        }
                        if (codes && codes != null && codes != "") {
                            $.each(codes, function (index, value) {
                                if (uniqueCodes_inHouse.indexOf(value) != -1) {
                                    var epc = {};
                                    epc.code = value;
                                    epc.styleId = rowData.styleId;
                                    epc.sizeId = rowData.sizeId;
                                    epc.colorId = rowData.colorId;
                                    epc.qty = 1;
                                    epc.sku = rowData.sku;
                                    epcArray.push(epc)
                                }
                            });
                        }
                        if (noOutcodes && noOutcodes != null && noOutcodes != "") {
                            $.each(noOutcodes, function (index, value) {
                                if (uniqueCodes_inHouse.indexOf(value) != -1) {
                                    debugger;
                                    var epc = {};
                                    epc.code = value;
                                    epc.styleId = rowData.styleId;
                                    epc.sizeId = rowData.sizeId;
                                    epc.colorId = rowData.colorId;
                                    epc.qty = 1;
                                    epc.sku = rowData.sku;
                                    epcArray.push(epc)
                                }
                            });
                        }

                });

                if (epcArray.length === 0) {
                    $.gritter.add({
                        text: "没有可以直接出库的商品",
                        class_name: 'gritter-success  gritter-light'
                    });
                    cs.closeProgressBar();
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
                        $("#TRDtl_wareHouseOut").removeAttr("disabled");
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
                            $("#grid").trigger("reloadGrid");
                            initButtonGroup(transferOrder_status);
                            setFooterData();
                            bootbox.alert("已出库" + epcArray.length + "件商品");
                        } else {
                            bootbox.alert(msg.msg);
                        }
                    }
                });
            }
        });
    }else {
        cs.closeProgressBar();
        bootbox.alert("请先保存当前单据");
    }
}
function wareHouseIn() {
    skuQty = {};
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        skuQty[rowData.sku] = rowData.inQty;
    });

    inOntWareHouseValid = 'wareHouseIn_valid';
    taskType = 1;
    var destId = $("#edit_destId").val();
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
    var billNo = $("#edit_billNo").val();

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
                $("#grid").trigger("reloadGrid");
                initButtonGroup(transferOrder_status);
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });

    $("#add-uniqCode-dialog").modal('hide');
}
/*function doPrintA4() {
 var billno = $("#edit_billNo").val();
 $.ajax({
 dataType: "json",
 url: basePath + "/logistics/transferOrder/printA4Info.do",
 data: {
 "billNo": billno,
 "ruleReceipt":"A4N0Size",
 "type":"TR"

 },
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
 LODOP.PREVIEW();
 //LODOP.PRINT();
 $("#edit-dialog-print").hide();


 } else {
 bootbox.alert(msg.msg);
 }
 }
 });


 }*/
function doPrintA4() {
    var billno = $("#edit_billNo").val();
    $.ajax({
        dataType: "json",
        url: basePath + "/logistics/transferOrder/printA4Info.do",
        data: {
            "billNo": billno,
            "ruleReceipt":"A4N0Size",
            "type":"TR"

        },
        type: "POST",
        success: function (msg) {
            if (msg.success) {
                var print = msg.result.print;
                var bill = msg.result.bill;
                var billDtl = msg.result.dtl;
                var LODOP = getLodop();



                //LODOP.PRINT();
                $("#edit-dialog-print").hide();
                $("#edit-dialogA4").html(print.printTableTh);
                var printCode=print.printCode;
                var printCodeArray=printCode.split(",");
                for(var i=0;i<printCodeArray.length;i++){
                    debugger;
                    var plp = printCodeArray[i];
                    var message = "";
                    if(plp=="remark"){
                        if(bill[plp]!=undefined&&bill[plp]!=""){
                            message ="备注:"+ bill[plp];
                        }else{
                            message = "备注:";
                        }

                    }else if(plp=="destName"){
                        if(bill[plp]!=undefined&&bill[plp]!=""){
                            message ="仓库:"+ bill[plp];
                        }else{
                            message = "仓库:";
                        }
                    }


                    $("#edit-dialogA4").find("#"+plp).text(message);
                }
                var tbodyCont="";
                for(var a=0;a<billDtl.length;a++){
                    var del=billDtl[a];
                    var printTableCode=print.printTableCode.split(",");
                    tbodyCont+=" <tr style='border-top:1px ;padding-top:5px;'>";
                    for(var b=0;b<printTableCode.length;b++){

                        if(printTableCode[b]=="qty"){
                            var qty = 0;
                            switch ($("#form_printSelect").val()) {
                                case "0":
                                    qty = del.inQty;
                                    break;
                                case "1":
                                    qty = del.outQty;
                                    break;
                                case "2":
                                    qty = del.qty;
                                    break;
                            }
                            tbodyCont += "<td align='middle' colspan='3' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" +qty + "</td>"
                        }else {
                            tbodyCont += "<td align='middle' colspan='3' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        }

                    }
                    tbodyCont+="</tr>"
                }
                $("#loadtabNoSize").html(tbodyCont);
                console.log($("#edit-dialogA4").html());
                //LODOP.SET_PRINT_STYLEA("baseHtml", 'Content', $("#edit-dialogSanLian").html());
                LODOP.ADD_PRINT_TABLE(100,1,printParameter.receiptWidthA4,printParameter.receiptheightSanLian,$("#edit-dialogA4").html());
                //LODOP.PREVIEW();
                LODOP.PRINT();
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}
function doPrintA4Size() {
    var billno = $("#edit_billNo").val();
    $.ajax({
        dataType: "json",
        url: basePath + "/logistics/transferOrder/printA4SizeInfo.do",
        data: {
            "billNo": billno,
            "ruleReceipt":"A4",
            "type":"TR"

        },
        type: "POST",
        success: function (msg) {
            if (msg.success) {

                var print = msg.result.print;
                var cont = msg.result.cont;
                var contDel = msg.result.contDel;
                console.log(print);
                console.log(cont);
                console.log(contDel);
                var LODOP = getLodop();
                //eval(print.printCont);
                $("#edit-dialogA4").html(print.printTableTh);
                var printCode=print.printCode;
                var printCodeArray=printCode.split(",");
                for(var i=0;i<printCodeArray.length;i++){
                    debugger;
                    var plp = printCodeArray[i];
                    var message = cont[plp];
                    $("#edit-dialogA4").find("#"+plp).text(message);
                }
                var tbodyCont="";
                for(var a=0;a<contDel.length;a++){
                    var del=contDel[a];
                    var printTableCode=print.printTableCode.split(",");
                    tbodyCont+=" <tr style='border-top:1px ;padding-top:5px;'>";
                    for(var b=0;b<printTableCode.length;b++){
                        if(printTableCode[b]=="styleId"||printTableCode[b]=="styleName"||printTableCode[b]=="colorId") {
                            tbodyCont += "<td align='middle' colspan='3' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        }else if(printParameter.sizeArry.indexOf(printTableCode[b])!=-1){
                            tbodyCont += "<td align='middle' colspan='1' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        }else if(printTableCode[b]=="other"){
                            tbodyCont += "<td align='middle' colspan='1' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        }else{
                            tbodyCont += "<td align='middle' colspan='2' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        }

                    }
                    tbodyCont+="</tr>"
                }
                $("#loadtabA4").html(tbodyCont);
                console.log($("#edit-dialogA4").html());
                //LODOP.SET_PRINT_STYLEA("baseHtml", 'Content', $("#edit-dialogSanLian").html());
                LODOP.ADD_PRINT_TABLE(100,1,printParameter.receiptWidthA4,printParameter.receiptheightSanLian,$("#edit-dialogA4").html());
                //LODOP.PREVIEW();
                LODOP.PRINT();


            } else {
                bootbox.alert(msg.msg);
            }

        }
    });
}
function cancel() {

    var billId= $("#edit_billNo").val();
    var status = $("#edit_status").val();
    if (status != "0") {
        bootbox.alert("不是录入状态，无法撤销");
        return;
    }
    if(billId == "" || billId == undefined){
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
                cancelAjax(billId);
                addNew()
            } else {
            }
        }
    });
}
function cancelAjax(billId) {
    $.ajax({
        dataType: "json",
        url: basePath + "/logistics/transferOrder/cancel.do",
        data: {billNo: billId},
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
function doPrintSanLian() {
    $("#edit-dialog-print").modal('show');
    $("#form_code").removeAttr("readOnly");
    var billNo = $("#search_billNo").val();
    $("#billno").val(billNo);
    $("#edit-dialog-print").show();
    $.ajax({
        dataType: "json",
        url: basePath + "/sys/printset/findPrintSetListByOwnerId.do",
        type: "POST",
        data: {
            type:"TR",
            ruleReceipt:"SanLian"
        },
        success: function (msg) {
            if (msg.success) {
                var addcont = "";
                for (var i = 0; i < msg.result.length; i++) {
                    addcont += "<div class='form-group' onclick=setSanLian('" + msg.result[i].id + "') title='" + msg.result[i].name + "'>" +
                        "<button class='btn btn-info'>" +
                        "<i class='cae-icon fa fa-refresh'></i>" +
                        "<span class='bigger-10'>套打" + msg.result[i].name + "</span>" +
                        "</button>" +
                        "</div>"
                }
                $("#addbutton").html(addcont);

            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}
function setSanLian(id) {
    $.ajax({
        dataType: "json",
        url: basePath + "/sys/printset/printMessageSanLian.do",
        data: {"id": id, "billno": $("#edit_billNo").val()},
        type: "POST",
        success: function (msg) {
            if (msg.success) {

                var print = msg.result.print;
                var cont = msg.result.cont;
                var contDel = msg.result.contDel;
                console.log(print);
                console.log(cont);
                console.log(contDel);
                var LODOP = getLodop();
                //eval(print.printCont);
                $("#edit-dialogSanLian").html(print.printTableTh);
                var printCode=print.printCode;
                var printCodeArray=printCode.split(",");
                for(var i=0;i<printCodeArray.length;i++){
                    debugger;
                    var plp = printCodeArray[i];
                    var message = cont[plp];
                    $("#edit-dialogSanLian").find("#"+plp).text(message);
                }
                var tbodyCont="";
                for(var a=0;a<contDel.length;a++){
                    var del=contDel[a];
                    var printTableCode=print.printTableCode.split(",");
                    tbodyCont+=" <tr style='border-top:1px ;padding-top:5px;'>";
                    for(var b=0;b<printTableCode.length;b++){
                        if(printTableCode[b]=="styleId"||printTableCode[b]=="styleName"||printTableCode[b]=="colorId") {
                            tbodyCont += "<td align='middle' colspan='3' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        }else if(printParameter.sizeArrySanLian.indexOf(printTableCode[b])!=-1){
                            tbodyCont += "<td align='middle' colspan='1' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        }else{
                            tbodyCont += "<td align='middle' colspan='2' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        }

                    }
                    tbodyCont+="</tr>"
                }
                $("#loadtabSanLian").html(tbodyCont);
                console.log($("#edit-dialogSanLian").html());
                //LODOP.SET_PRINT_STYLEA("baseHtml", 'Content', $("#edit-dialogSanLian").html());
                LODOP.ADD_PRINT_TABLE(100,1,printParameter.receiptWidthSanLian,printParameter.receiptheightSanLian,$("#edit-dialogSanLian").html());
                //LODOP.PREVIEW();
                LODOP.PRINT();
                $("#edit-dialog-print").hide();



            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}
/**
 * 热敏打印
 * */
function doPrint() {
    /*$("#editForm").resetForm();*/
    $("#edit-dialog-print").modal('show');
    $("#form_code").removeAttr("readOnly");
    var billNo = $("#search_billNo").val();
    $("#billno").val(billNo);
    $("#edit-dialog-print").show();
    $.ajax({
        dataType: "json",
        url: basePath + "/sys/printset/findPrintSetListByOwnerId.do",
        type: "POST",
        data: {
            type:"TR",
        },
        success: function (msg) {
            if (msg.success) {
                var addcont = "";
                for (var i = 0; i < msg.result.length; i++) {
                    addcont += "<div class='form-group' onclick=set('" + msg.result[i].id + "') title='" + msg.result[i].name + "'>" +
                        "<button class='btn btn-info'>" +
                        "<i class='cae-icon fa fa-refresh'></i>" +
                        "<span class='bigger-10'>套打" + msg.result[i].name + "</span>" +
                        "</button>" +
                        "</div>"
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
        url: basePath + "/sys/printset/printMessage.do",
        data: {"id": id, "billno": $("#edit_billNo").val()},
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
                for (var a = 0; a < contDel.length; a++) {
                    var conts = contDel[a];
                    recordmessage += "<tr style='border-top:1px dashed black;padding-top:5px;'>" +
                        "<td align='left' style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + conts.sku + "</td>" +
                        "<td align='left'style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + conts.qty + "</td>" +
                        "<td style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + conts.price.toFixed(1) + "</td>" +
                        "</tr>";

                    sum = sum + parseInt(conts.qty);
                    //allprice = allprice + parseFloat(conts.actPrice*conts.qty.toFixed(2));
                    //alldiscount = alldiscount + parseFloat((conts.actPrice * conts.qty).toFixed(2));
                }
                //alldiscount = alldiscount.toFixed(0);
                recordmessage += " <tr style='border-top:1px dashed black;padding-top:5px;'>" +
                    "<td align='left' style='border-top:1px dashed black;padding-top:5px;'>合计:</td>" +
                    "<td align='left'style='border-top:1px dashed black;padding-top:5px;'>" + sum + "</td>" +
                    "<td style='border-top:1px dashed black;padding-top:5px;'>&nbsp;</td>" +
                    " </tr>";

                $("#loadtabTR").html(recordmessage);
                LODOP.SET_PRINT_STYLEA("baseHtml", 'Content', $("#edit-dialog-TR").html());
                //LODOP.PREVIEW();
                LODOP.PRINT();
                $("#edit-dialog-print").hide();


            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}
function floorallocation() {
    $("#show-floorallocation-list").modal('show');
    initFloorallocationList();
    FloorallocationListReload();
}
function wareHouseInAll() {
    var billNo  = $("#edit_billNo").val();
    $("#modal-addTransfer-table").modal('show').on('hidden.bs.modal', function () {
        $("#epcTransfergrid").clearGridData();
    });
    $("#epcTransfergrid").jqGrid('setGridParam', {
        page: 1,
        url: basePath + '/stock/warehStock/findNotInTransfer.do',
        postData: {billNo: billNo}
    }).trigger("reloadGrid");
}

function saveTransferEpc() {
    cs.showProgressBar();
    var ids = $("#epcTransfergrid").jqGrid("getGridParam", "selarrrow");
    if (ids.length == 0) {
        cs.closeProgressBar();
        bootbox.alert("请选择要入库的唯一码信息");
    } else {
        var epcArray = [];
        $.each(ids, function (index, value) {
            var rowData = $("#epcTransfergrid").getRowData(value);
            epcArray.push(rowData);
        });
        $.ajax({
            dataType: "json",
            // async:false,
            url: basePath + "/logistics/transferOrder/convertIn.do",
            data: {
                billNo: $("#edit_billNo").val(),
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
                    $("#grid").trigger("reloadGrid");
                    initButtonGroup(transferOrder_status);
                } else {
                    bootbox.alert(msg.msg);
                }
            }
        });
        $("#modal-addTransfer-table").modal('hide');
    }
}
/**
 * 扫码出库
 * */
function edit_wareHouseOut() {
    inOntWareHouseValid = 'addPage_scanUniqueCode';
    var origId = $("#edit_origId").val();
    taskType = 0;
    wareHouse = origId;
    billNo = $("#edit_billNo").val();
    if (origId && origId !== null) {
        $("#dialog_buttonGroup").html("" +
            "<button type='button' id='so_savecode_button' class='btn btn-primary' onclick='confirmWareHouseOut()'>确认出库</button>"
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

/*
 * 扫码出库确认
 * */
function confirmWareHouseOut() {
    cs.showProgressBar();
    //$("#so_comfirmout_button").attr({"disabled": "disabled"});
    var billNo = $("#edit_billNo").val();
    var epcArray = [];
    $.each($("#uniqueCodeGrid").getDataIDs(), function (index, value) {
        var rowData = $("#uniqueCodeGrid").getRowData(value);
        epcArray.push(rowData);
    });
    if (epcArray.length == 0) {
        bootbox.alert("请添加唯一码!");
        $("#so_comfirmout_button").removeAttr("disabled");
        cs.closeProgressBar();
        return;
    }
    $.each(epcArray, function (index, value) {
        $.each($("#addDetailgrid").getDataIDs(), function (dtlIndex, dtlValue) {
            var dtlRow = $("#addDetailgrid").getRowData(dtlValue);
            if (value.sku === dtlRow.sku) {
                if (dtlRow.uniqueCodes.indexOf(value.code) != -1) {
                    $.gritter.add({
                        text: value.code + "不能重复添加",
                        class_name: 'gritter-success  gritter-light'
                    });
                    return true;
                }
                dtlRow.uniqueCodes = dtlRow.uniqueCodes + "," + value.code;
                if (dtlRow.id) {
                    $("#addDetailgrid").setRowData(dtlRow.id, dtlRow);
                } else {
                    $("#addDetailgrid").setRowData(dtlIndex, dtlRow);
                }
            }
        });
    });
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
            $("#TRDtl_wareHouseOut").removeAttr("disabled");
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
                $("#grid").trigger("reloadGrid");
                initButtonGroup(transferOrder_status);
                setFooterData();
                bootbox.alert("已出库" + epcArray.length + "件商品");
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
    /*$.ajax({
        dataType: "json",
        url: basePath + "/logistics/saleOrderBill/convertOut.do",
        data: {
            billNo: billNo,
            strEpcList: JSON.stringify(epcArray),
            strDtlList: JSON.stringify(dtlArray),
            userId: userId
        },
        type: "POST",
        success: function (msg) {
            $("#so_comfirmout_button").removeAttr("disabled");
            cs.closeProgressBar();
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#modal-addEpc-table").modal('hide');
                $("#addDetailgrid").trigger("reloadGrid");
                _search();
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });*/
    $("#add-uniqCode-dialog").modal('hide');
}


function addAllUniqCode() {
    var origId = $("#edit_origId").val();
    taskType = 0;
    wareHouse = origId;
    if (wareHouse && wareHouse !== null) {
    }else {
        bootbox.alert("请选择出库仓库");
        return;
    }
    billNo = $("#edit_billNo").val();
    $("#modal-batch-table").modal('show').on('hidden.bs.modal', function () {
        $("#batchDetailgrid").clearGridData();
        skuInfo=[];
        oldSkuInfo = [];
    });
    initWebSocket();
}


//添加批量扫码页面到单据明细中
function saveEPC(){
    var uniqueCodeList=[];//商品唯一码list(含商品信息)
    var rightCodesArry=[];//正确唯一码（只有唯一码)
    var errorCodesArry=[];//异常唯一码(只有唯一码信息)
    //遍历批量扫到的唯一码将sku明细转换为唯一码明细
    $.each($("#batchDetailgrid").getDataIDs(),function (index,value) {
        var rowData=$("#batchDetailgrid").jqGrid('getRowData',value);
        var rightCodes;//正常唯一码
        var errorCodes;//异常唯一码
        if(rowData.uniqueCodes!=""&&rowData.uniqueCodes!=undefined){
            rightCodes=rowData.uniqueCodes.split(",");
            rightCodesArry = rightCodesArry.concat(rightCodes);
        }
        if(rowData.noOutPutCode!=""&&rowData.noOutPutCode!=undefined){
            errorCodes=rowData.noOutPutCode.split(",");
            errorCodesArry = errorCodesArry.concat(errorCodes);
        }
        if (rightCodes!=undefined){
            delete rowData.noOutPutCode;
            $.each(rightCodes,function (cIndex,cValue) {
                var newRowData=JSON.parse(JSON.stringify(rowData));
                newRowData.uniqueCodes=cValue;
                newRowData.qty = 1;
                //判断实际价格是不是小于门店批发价格
                if($('#discount_div').is(':hidden')) {
                    newRowData.actPrice = Math.round(newRowData.price * newRowData.discount) / 100;
                    newRowData.abnormalStatus=0;
                }else {
                    if(Math.round(newRowData.price * newRowData.discount) / 100<newRowData.wsPrice&&isUserAbnormal){
                        newRowData.actPrice = newRowData.wsPrice;
                        newRowData.discount = parseFloat(newRowData.wsPrice/newRowData.price).toFixed(2)*100;
                        newRowData.abnormalStatus=1;
                    }else{
                        newRowData.actPrice = Math.round(newRowData.price * newRowData.discount) / 100;

                        newRowData.abnormalStatus=0;
                    }
                }
                newRowData.outQty = 0;
                newRowData.inQty = 0;
                newRowData.status = 0;
                newRowData.inStatus = 0;
                newRowData.outStatus = 0;
                newRowData.totPrice = newRowData.price;
                newRowData.totActPrice = newRowData.actPrice;
                var stylePriceMap={};
                stylePriceMap['price']=newRowData.price;
                stylePriceMap['wsPrice']=newRowData.wsPrice;
                stylePriceMap['puPrice']=newRowData.puPrice;
                newRowData.stylePriceMap=JSON.stringify(stylePriceMap);
                uniqueCodeList.push(newRowData);
            });
        }
    });
    //适用于寄存调拨只能添加正常唯一码情况
    if(rightCodesArry.length === 0){
        bootbox.alert("没有正常的唯一码,请重新扫码");
    }else{
        var isAdd = true;
        var alltotActPrice = 0;
        //将唯一码明细添加入明细表格
        $.each(uniqueCodeList,function (index,value) {
            isAdd=true;
            $.each($("#addDetailgrid").getDataIDs(),function (dtlIndex,dtlValue) {
                var dtlRow = $("#addDetailgrid").getRowData(dtlValue);
                if (value.sku === dtlRow.sku) {
                    if (value.uniqueCodes!=""&&value.uniqueCodes!=undefined&&dtlRow.uniqueCodes.indexOf(value.uniqueCodes)=== -1) {
                        dtlRow.qty = parseInt(dtlRow.qty) + 1;
                        dtlRow.totPrice = dtlRow.qty * dtlRow.price;
                        dtlRow.totActPrice = dtlRow.qty * dtlRow.actPrice;
                        alltotActPrice += dtlRow.qty * dtlRow.actPrice;
                        dtlRow.uniqueCodes = dtlRow.uniqueCodes + "," + value.uniqueCodes;
                    }
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
        setFooterData();
        bootbox.alert("添加正常数据"+rightCodesArry.length+"条,异常数据"+errorCodesArry.length+"条无法添加");
    }
    $("#modal-batch-table").modal('hide');

}

/**
 * 批量扫码出库
 */
function batchWareHouseOut() {
    billNo = $("#edit_billNo").val();
    wareHouse=$("#edit_origId").val();
    taskType = 0;
    $("#modal-batch-show-table").modal('show').on('hidden.bs.modal', function () {
        $("#billInformationOutgrid").clearGridData();
        $("#notThisOneOutgrid").clearGridData();
        skuInfo=[];
        oldSkuInfo=[];
    });
    lodeBillInformationOutgrid();
    fullOutWebSocket();
    $("#outCodeQty").text(0);
}


/**
 * 批量扫码入库
 */
function batchWareHouseIn() {
    taskType = 1;
    var destId = $("#edit_destId").val();
    wareHouse = destId;
    billNo = $("#edit_billNo").val();
    var ct = $("#edit_customerType").val();
    if (destId && destId != null) {
        $("#modal-batch-show-In-table").modal('show').on('hidden.bs.modal', function () {
            $("#billInformationIngrid").clearGridData();
            $("#notThisOneIngrid").clearGridData();
            skuInfo=[];
            oldSkuInfo=[];
        });
        lodeBillInformationIngrid();
        fullWebInSocket();
        initUniqeCodeGridColumn(ct);
        $("#inCodeQty").text(0);
    } else {
        bootbox.alert("入库仓库不能为空！");
    }
}