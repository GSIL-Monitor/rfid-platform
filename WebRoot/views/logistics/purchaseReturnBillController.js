var purchaseReturn_status;
var billNo;
var editDtailRowId = null;
var loadComplete = true, loadStatus = true;
var isPassed = true;
var allCodeStrInDtl = "";
var isCheckWareHouse=true;
var taskType;
var wareHouse;
$(function () {
    /*初始化左侧grig*/
    initSearchGrid();
    /*初始化右侧grig*/
    initAddGrid();
    /*初始化from表单*/
    initSearchAndEditForm();
    setEditFormVal();
    initEditFormValid();
    /*回车事件*/
    keydown();
});
function initSearchGrid() {
    $("#grid").jqGrid({
        height: 'auto',
        datatype: 'json',
        url: basePath + "/logistics/purchaseReturn/page.do",
        mtype: 'POST',
        colModel: [
            {name: 'billNo', label: '单号', editable: true, width: 40},
            {name: 'status', hidden: true},
            {name: 'outStatus', hidden: true},
            {
                name: "", label: '状态', editable: true, width: 20, align: 'center',sortable: false,
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
            {name: 'billType', label: '退货类型', hidden: true},
            {name: 'billDate', label: '单据日期', width: 40},
            {name: "ownerId", hidden: true},
            {name: 'origUnitId', label: '发货方ID', hidden: true},
            {name: 'origUnitName', label: '发货方', width: 40,hidden: true},
            {name: 'origId', label: '出库仓库ID', hidden: true,hidden: true},
            {name: 'origName', label: '出库仓库', width: 40,hidden: true},
            {name: 'destUnitId', label: '供应商ID', hidden: true},
            {name: 'destUnitName', label: '供应商', width: 40},
            {name: 'destId', label: '收货仓库ID', hidden: true},
            {name: 'destName', label: '收货仓库', editable: true, width: 40,hidden: true},
            {name: 'totOutQty', label: '已出库数量', width: 40,hidden: true},
            {name: "payPrice", label: '退货金额', width: 40,hidden: true},
            {name: 'totQty', label: '单据数量', editable: true, width: 40,align:"center"},
            {name: 'remark', label: '备注', editable: true, width: 40,hidden: true},
            {name:'returnBillNo',hidden:true}
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
            initDetailData(rowid);
        }

    });
    $("#grid").jqGrid("setFrozenColumns");
}
function initDetailData(rowid) {
    var rowData = $("#grid").getRowData(rowid);
    $("#editForm").setFromData(rowData);
    purchaseReturn_status=rowData.status;
    if(purchaseReturn_status !== "0"){
        $("#edit_guest_button").attr({"disabled": "disabled"});
    }
    $(".selectpicker").selectpicker('refresh');
    $('#addDetailgrid').jqGrid("clearGridData");
    $('#addDetailgrid').jqGrid('GridUnload');
    initeditGrid(rowData.billNo);
    initButtonGroup(purchaseReturn_status);

}
function setFooterData() {
    var sum_totQty = $("#grid").getCol('totQty', false, 'sum');
    var sum_totOutQty = $("#grid").getCol('totOutQty', false, 'sum');
    var sum_payPrice = $("#grid").getCol('payPrice', false, 'sum');
    $("#grid").footerData('set', {
        billNo: "合计",
        totQty: sum_totQty,
        totOutQty: sum_totOutQty,
        payPrice: sum_payPrice
    });
}
function initeditGrid(billId) {
    billNo=billId;
    $("#addDetailgrid").jqGrid({
        height: 'auto',
        datatype: "json",
        url: basePath + '/logistics/purchaseReturn/findDtls.do?billNo=' + billNo,
        colModel: [
            {name: 'id', label: 'id', hidden: true},
            {name: 'billNo', label: "billNo", hidden: true},
            {name: 'status', hidden: true},
            {name: 'outStatus', hidden: true},
            {
                name: 'operation', label: '操作', width: 30, align: 'center',sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    var html = '<a href="#" title="保存该行" onclick="saveItem(' + options.rowId + ')" style="text-decoration:none"><i class="ace-icon fa fa-save"></i></a>&nbsp;&nbsp;&nbsp;';
                    html += '<a href="#" title="删除该行" onclick="deleteItem(' + options.rowId + ')"><i class="ace-icon fa fa-trash"></i></a>';
                    return html;
                }
            },
            {
                label: '状态', width: 20, align: 'center', hidden: true,sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    return '<i class="fa fa-tasks blue" title="订单状态"></i>';
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
            {name: 'sku', label: "sku", width: 40},
            {name: 'styleId', label: '款号', width: 20,hidden: true},
            {name: 'colorId', label: '色码', width: 20,hidden: true},
            {name: 'sizeId', label: '尺码', width: 20,hidden: true},
            {name: 'styleName', label: '款名', width: 40},
            {name: 'colorName', label: '颜色', width: 20,hidden: true},
            {name: 'sizeName', label: '尺码', width: 20,hidden: true},
            {
                name: 'qty', label: '数量', width: 20,
                editrules: {
                    custom: true,
                    custom_func: validNum
                }
            },
            {name: 'outQty', label: '已出库数量', width: 20},
            {
                name: 'price', label: '采购价格', width: 40,
                editrules: {
                    custom: true,
                    custom_func: validNum
                }
            },
            {name: 'totPrice', label: '退货金额', width: 40},
            {
                name: 'actPrice', label: '实际价格',  width: 40,
                editrules: {
                    custom: true,
                    custom_func: validNum
                }
            },
            {name: 'totActPrice', label: '退货实际金额', width: 40},
            {name: 'uniqueCodes', label: 'code', hidden: true},
            {
                name: '', label: '唯一码明细', width: 40, align: "center",
                formatter: function (cellValue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=showCodesDetail('" + rowObject.uniqueCodes + "')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
                }
            },
        ],
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: -1,
        multiselect: false,
        shrinkToFit: true,
        pager: '#addDetailgrid-pager',
        sortname: 'id',
        sortorder: "asc",
        footerrow: true,
        onSelectRow: function (rowid, status) {
            if (editDtailRowId != null) {
                saveItem(editDtailRowId);
            }
            editDtailRowId = rowid;
            $('#addDetailgrid').editRow(rowid);
        },
        gridComplete: function () {
            setAddFooterData();
        },
        loadComplete: function () {
            initAllCodesList();
        }

    });
    if (pageType == "edit" && purchaseReturn_status !== "0") {
        $("#addDetailgrid").setGridParam().hideCol("operation");
    } else {
        $("#addDetailgrid").setGridParam().showCol("operation");
        $("#addDetailgrid").jqGrid('navGrid', "#addDetailgrid-pager",
            {
                edit: false,
                add: true,
                addicon: "ace-icon fa fa-plus",
                addfunc: function () {
                    addDetail();
                },
                del: false,
                search: false,
                refresh: false,
                view: false
            });
    }
    $("#addDetailgrid-pager_center").html("");
}
function saveItem(rowId) {
    $('#addDetailgrid').saveRow(rowId);
    var value = $('#addDetailgrid').getRowData(rowId);
    value.totPrice = -Math.floor(value.qty * value.price);
    value.totActPrice = -Math.floor(value.qty * value.actPrice);
    $("#addDetailgrid").setRowData(rowId, value);
    setAddFooterData();
}
function deleteItem(rowId) {
    $("#addDetailgrid").jqGrid("delRowData", rowId);
    setAddFooterData();
}
function setAddFooterData() {
    var sum_qty = $("#addDetailgrid").getCol('qty', false, 'sum');
    var sum_outQty = $("#addDetailgrid").getCol('outQty', false, 'sum');
    var sum_totPrice = $("#addDetailgrid").getCol('totPrice', false, 'sum');
    var sum_totActPrice = Math.round($("#addDetailgrid").getCol('totActPrice', false, 'sum'));
    $("#addDetailgrid").footerData('set', {
        styleId: "合计",
        qty: sum_qty,
        outQty: sum_outQty,
        totPrice: sum_totPrice,
        totActPrice: sum_totActPrice
    });
    $("#edit_actPrice").val(sum_totActPrice);
}
function initSearchAndEditForm() {
    initSelectOrigForm();
    initSelectOrigEditForm();
    initButtonGroup(0);
    $(".selectpicker").selectpicker('refresh');
}
function setEditFormVal(){
    $("#edit_billDate").val(getToDay("yyyy-MM-dd"));
    $("#edit_actPrice").val(0);
}
function initSelectOrigForm() {
    //"/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + $("#search_unitId").val()
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9",
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_origId").empty();
            $("#search_origId").append("<option value=''>--请选择入库仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_origId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
            }
        }
    });
}
function initSelectOrigEditForm() {
    //"/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + $("#search_unitId").val()
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9",
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#edit_origId").empty();
            $("#edit_origId").append("<option value=''>--请选择入库仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#edit_origId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
            }
            $("#edit_origId").val(defaultWarehId);
        }
    });
}
var dialogOpenPage;
var prefixId;
/*
 *@param preId id前缀 search/edit 区分回调框id
 **/
function openSearchVendorDialog(preId) {
    dialogOpenPage = "purchaseReturn";
    prefixId=preId;
    $("#modal_vendor_search_table").modal('show').on('shown.bs.modal', function () {
        initVendorSelect_Grid();
    });
    $("#searchVendorDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='confirm_selected_VendorId_purchaseReturn()'>确认</button>"
    );
}

/**
 * 查询左侧表格内容
 * */
function _search() {

    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        page: 1,
        postData: params
    });
    $("#grid").trigger("reloadGrid");
}
function validNum(value, name) {
    var id = "#" + editDtailRowId + "_qty";
    console.log(editDtailRowId + "," + name);

    try {
        var v = Number(value);
        if (isNaN(v) || value == "")
            throw "非数字！";
        if ("数量" != name || "实际数量" != name)
            return [true, ''];
        if (v >= 1 && isInteger(v)) {
            $(id).val(Math.floor(v));
            isPassed = true
        } else {
            isPassed = false;
            $('#addDetailgrid').editRow(editDtailRowId);
            bootbox.alert(name + ":请输入一个大于0的整数");
        }
        return [true, ''];
    } catch (e) {
        isPassed = false;
        if ("数量" != name || "实际数量" != name) {
            bootbox.alert(name + ":请输入数字");
            $('#addDetailgrid').editRow(editDtailRowId);
            return [true, '']
        }

        console.log(e);
        $('#addDetailgrid').editRow(editDtailRowId);
        bootbox.alert(name + ":请输入一个大于0的整数");
        return [true, ''];
    }
}
function initAllCodesList() {
    allCodeStrInDtl = "";
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        allCodeStrInDtl = allCodeStrInDtl + "," + rowData.uniqueCodes;
    });
    if (allCodeStrInDtl !== "") {
        if (allCodeStrInDtl.substr(0, 1) === ",") {
            allCodeStrInDtl = allCodeStrInDtl.substr(1);
        }
    }
}
/*根据权限初始化按钮*/
function initButtonGroup(billStatus) {
    $("#buttonGroup").html("" +
        "<button id='PIDtl_add' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='addNew()'>" +
        "    <i class='ace-icon fa fa-plus'></i>" +
        "    <span class='bigger-110'>新增</span>" +
        "</button>" +
        "<button id='PIDtl_cancel' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='cancel()'>" +
        "    <i class='ace-icon fa fa-undo'></i>" +
        "    <span class='bigger-110'>撤销</span>" +
        "</button>" +
        "<button id='PIDtl_save' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='save()'>" +
        "    <i class='ace-icon fa fa-save'></i>" +
        "    <span class='bigger-110'>保存</span>" +
        "</button>" +
        "<button id='PRDtl_batchUniqCode' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='batchUniqCode()'>" +
        "    <i class='ace-icon fa fa-sign-out'></i>" +
        "    <span class='bigger-110'>批量扫码</span>" +
        "</button>"+
        "<button id='PIDtl_addUniqCode' type='button'  style='margin: 8px' class='btn btn-xs btn-primary' onclick='addUniqCode()'>" +
        "    <i class='ace-icon fa fa-barcode'></i>" +
        "    <span class='bigger-110'>扫码</span>" +
        "</button>" +
        "<button id='PIDtl_wareHouseOut' type='button'  style='margin: 8px' class='btn btn-xs btn-primary' onclick='edit_wareHouseOut()'>" +
        "    <i class='ace-icon fa fa-sign-out'></i>" +
        "    <span class='bigger-110'>出库</span>" +
        "</button>"+
        "<button id='PRDtl_batchWareHouseOut' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='batchWareHouseOut()'>" +
        "    <i class='ace-icon fa fa-sign-out'></i>" +
        "    <span class='bigger-110'>批量出库</span>" +
        "</button>"+
        "<button id='PRDtl_doPrint' type='button'  style='margin: 8px' class='btn btn-xs btn-primary' onclick='doPrint(billNo)'>" +
        "    <i class='ace-icon fa fa-reply'></i>" +
        "    <span class='bigger-110'>打印</span>" +
        "</button>"+
        "<button id='PRDtl_doPrintSanLian' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='doPrintSanLian()'>" +
        "    <i class='ace-icon fa fa-print'></i>" +
        "    <span class='bigger-110'>三联打印</span>" +
        "</button>"+
        "<button id='PRDtl_doPrintA4' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='doPrintA4()'>" +
        "    <i class='ace-icon fa fa-print'></i>" +
        "    <span class='bigger-110'>A4打印</span>" +
        "</button>"
    );
    loadingButtonDivTable(billStatus);
}
function initAddGrid() {
    $("#addDetailgrid").jqGrid({
        height: 'auto',
        datatype: "local",
        colModel: [
            {name: 'id', label: 'id', hidden: true},
            {name: 'billNo', label: "billNo", hidden: true},
            {name: 'status', hidden: true},
            {name: 'outStatus', hidden: true},
            {
                name: 'operation', label: '操作', width: 30, align: 'center',sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    var html = '<a href="#" title="保存该行" onclick="saveItem(' + options.rowId + ')" style="text-decoration:none"><i class="ace-icon fa fa-save"></i></a>&nbsp;&nbsp;&nbsp;';
                    html += '<a href="#" title="删除该行" onclick="deleteItem(' + options.rowId + ')"><i class="ace-icon fa fa-trash"></i></a>';
                    return html;
                }
            },
            {
                label: '状态', width: 20, align: 'center', hidden: true,sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    return '<i class="fa fa-tasks blue" title="订单状态"></i>';
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
            {name: 'sku', label: "sku", width: 40},
            {name: 'styleId', label: '款号', width: 20,hidden: true},
            {name: 'colorId', label: '色码', width: 20,hidden: true},
            {name: 'sizeId', label: '尺码', width: 20,hidden: true},
            {name: 'styleName', label: '款名', width: 20},
            {name: 'colorName', label: '颜色', width: 20,hidden: true},
            {name: 'sizeName', label: '尺码', width: 20,hidden: true},
            {
                name: 'qty', label: '数量', width: 40,
                editrules: {
                    custom: true,
                    custom_func: validNum
                }
            },
            {name: 'outQty', label: '已出库数量', width: 40},
            {
                name: 'price', label: '采购价格', width: 40,
                editrules: {
                    custom: true,
                    custom_func: validNum
                }
            },
            {name: 'totPrice', label: '退货金额', width: 40},
            {
                name: 'actPrice', label: '实际价格', width: 40,
                editrules: {
                    custom: true,
                    custom_func: validNum
                }
            },
            {name: 'totActPrice', label: '退货实际金额', width: 40},
            {name: 'uniqueCodes', label: 'code', hidden: true}

        ],
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: -1,
        multiselect: false,
        shrinkToFit: true,
        pager: '#addDetailgrid-pager',
        sortname: 'id',
        sortorder: "asc",
        footerrow: true,
        onSelectRow: function (rowid, status) {
            if (editDtailRowId != null) {
                saveItem(editDtailRowId);
            }
            editDtailRowId = rowid;
            $('#addDetailgrid').editRow(rowid);
        },
        gridComplete: function () {
            setAddFooterData();
        },
        loadComplete: function () {
            initAllCodesList();
        }

    });
    if (pageType == "edit" && purchaseReturn_status !== "0") {
        $("#addDetailgrid").setGridParam().hideCol("operation");
    } else {
        $("#addDetailgrid").setGridParam().showCol("operation");
        $("#addDetailgrid").jqGrid('navGrid', "#addDetailgrid-pager",
            {
                edit: false,
                add: true,
                addicon: "ace-icon fa fa-plus",
                addfunc: function () {
                    addDetail();
                },
                del: false,
                search: false,
                refresh: false,
                view: false
            });
    }
    $("#addDetailgrid-pager_center").html("");
}
function save() {
    cs.showProgressBar();
    $("#editForm").data('bootstrapValidator').destroy();
    $('#editForm').data('bootstrapValidator', null);
    initEditFormValid();
    $('#editForm').data('bootstrapValidator').validate();
    if (!$('#editForm').data('bootstrapValidator').isValid()) {
        cs.closeProgressBar();
        return;
    }
    if (!isPassed) {
        cs.closeProgressBar();
        bootbox.alert("请确保表格中数据准确");
    }
    if ($("#addDetailgrid").getDataIDs().length == 0) {
        cs.closeProgressBar();
        bootbox.alert("请添加退货商品");
        return;
    }
    $("#edit_billDate").val(updateTime($("#edit_billDate").val()));
    var purchaseReturnBill = JSON.stringify(array2obj($("#editForm").serializeArray()));
    console.log(purchaseReturnBill);
    $("#addDetailgrid").saveRow(editDtailRowId);
    var dtlArray = [];
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        dtlArray.push(rowData);
    });

    $.ajax({
        dataType: "json",
        async:true,
        url: basePath + "/logistics/purchaseReturn/save.do",
        data: {
            'strBill': purchaseReturnBill,
            'strDtlList': JSON.stringify(dtlArray),
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
                $("#edit_billNo").val(msg.result);
                $("#add_addDetailgrid").hide();
                $("#savebutton").hide();
                console.info("111");
                $("#grid").trigger("reloadGrid");
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
            destUnitId: {
                validators: {
                    notEmpty: {
                        message: "供应商不能为空",
                    }
                }
            },
            billDate: {
                validators: {
                    notEmpty: {
                        message: '请选择单据日期'
                    }
                }
            },
            payPrice: {
                validators: {
                    notEmpty: {
                        message: '金额不能为空'
                    }
                }
            }
        }
    });
}
function addUniqCode() {
    inOntWareHouseValid = 'addPage_scanUniqueCode';
    taskType = 0;
    var origId = $("#edit_origId").val();
    wareHouse = origId;
    if (origId && origId != null) {
        $("#dialog_buttonGroup").html("" +
            "<button type='button'  class='btn btn-primary' onclick='addProductsOnCode()'>保存</button>"
        );
        $("#add-uniqCode-dialog").modal('show').on('hidden.bs.modal', function () {
            $("#uniqueCodeGrid").clearGridData();
        });
        initUniqeCodeGridColumn('showPrecast');
        $("#codeQty").text(0);
    } else {
        bootbox.alert("发货仓库不能为空！")
    }
    allCodes = "";
}
function edit_wareHouseOut() {
    var sum_qty = parseInt($("#addDetailgrid").footerData('get').qty);
    var sum_outQty = parseInt($("#addDetailgrid").footerData('get').outQty);
    if (sum_qty === sum_outQty) {
        $.gritter.add({
            text: '已全部出库',
            class_name: 'gritter-success  gritter-light'
        });
    }else if (sum_qty > sum_outQty) {
        skuQty = {};
        $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
            var rowData = $("#addDetailgrid").getRowData(value);
            skuQty[rowData.sku] = rowData.outQty;
        });
        inOntWareHouseValid = 'wareHouseOut_valid';

        var origId = $("#edit_origId").val();
        taskType = 0;
        wareHouse = origId;

        var inOutState = wareHouseOut();

        if(inOutState !== "done"){
            if (origId && origId !== null) {
                $("#dialog_buttonGroup").html("" +
                    "<button type='button'  class='btn btn-primary' onclick='confirmWareHouseOut()'>确认出库</button>"
                );
                $("#add-uniqCode-dialog").modal('show').on('hidden.bs.modal', function () {
                    $("#uniqueCodeGrid").clearGridData();
                });
                initUniqeCodeGridColumn('showPrecast');
                $("#codeQty").text(0);
            } else {
                bootbox.alert("发货仓库不能为空！");
            }
        }
        else {
            addNew();
        }
        allCodes = "";
    }
}
function addProductsOnCode() {
    var productListInfo = [];
    $.each($("#uniqueCodeGrid").getDataIDs(), function (index, value) {
        var productInfo = $("#uniqueCodeGrid").getRowData(value);
        productInfo.qty = 1;
        productInfo.price = productInfo.preCast;
        productInfo.outQty = 0;
        productInfo.status = 0;
        productInfo.outStatus = 0;
        productInfo.uniqueCodes = productInfo.code;
        productInfo.totPrice = -Math.abs(productInfo.price);
        productInfo.actPrice = productInfo.price;
        productInfo.totActPrice = -Math.abs(productInfo.actPrice);
        productListInfo.push(productInfo);
    });
    if (productListInfo.length == 0) {
        bootbox.alert("请添加唯一码");
        return;
    }
    var isAdd = true;
    $.each(productListInfo, function (index, value) {
        isAdd = true;
        $.each($("#addDetailgrid").getDataIDs(), function (dtlIndex, dtlValue) {
            var dtlRow = $("#addDetailgrid").getRowData(dtlValue);
            if (value.sku === dtlRow.sku) {
                if (dtlRow.uniqueCodes.indexOf(value.code) != -1) {
                    isAdd = false;
                    $.gritter.add({
                        text: value.code + "不能重复添加",
                        class_name: 'gritter-success  gritter-light'
                    });
                    return true;
                }
                dtlRow.qty = parseInt(dtlRow.qty) + 1;
                dtlRow.totPrice = -Math.abs(dtlRow.qty * dtlRow.price);
                dtlRow.actPrice = dtlRow.price;
                dtlRow.totActPrice = -Math.abs(dtlRow.qty * dtlRow.actPrice);
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
    $("#add-uniqCode-dialog").modal('hide');
    setAddFooterData();
}
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
            type:"PR"
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
                var allprice = 0;
                var alldiscount=0;
                for (var a = 0; a < contDel.length; a++) {
                    var conts = contDel[a];
                    recordmessage += "<tr style='border-top:1px dashed black;padding-top:5px;'>" +
                        "<td align='left' style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + conts.sku + "</td>" +
                        "<td align='left'style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + conts.qty + "</td>" +
                        "<td style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + conts.price.toFixed(1) + "</td>" +
                        "<td style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + conts.actPrice.toFixed(1) + "</td>" +
                        "<td align='right' style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + (conts.actPrice*conts.qty).toFixed(2) + "</td>" +
                        "</tr>";

                    sum = sum + parseInt(conts.qty);
                    //allprice = allprice + parseFloat(conts.actPrice*conts.qty.toFixed(2));
                    alldiscount = alldiscount+parseFloat((conts.actPrice*conts.qty).toFixed(2));
                }
                alldiscount=alldiscount.toFixed(0);
                recordmessage += " <tr style='border-top:1px dashed black;padding-top:5px;'>" +
                    "<td align='left' style='border-top:1px dashed black;padding-top:5px;'>合计:</td>" +
                    "<td align='left'style='border-top:1px dashed black;padding-top:5px;'>" + sum + "</td>" +
                    "<td style='border-top:1px dashed black;padding-top:5px;'>&nbsp;</td>" +
                    " <td style='border-top:1px dashed black;padding-top:5px;'>&nbsp;</td>" +
                    "<td align='right' style='border-top:1px dashed black;padding-top:5px;'>" + alldiscount + "</td>" +
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
function wareHouseOut() {
    var billNo = $("#edit_billNo").val();
    if (billNo && billNo !== null) {

        var epcArray = [];
        $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
            var rowData = $("#addDetailgrid").getRowData(value);
            var codes = rowData.uniqueCodes.split(",");
            if (codes && codes != null && codes != "") {
                $.each((codes), function (index, value) {
                    var epc = {};
                    epc.code = value;
                    epc.styleId = rowData.styleId;
                    epc.sizeId = rowData.sizeId;
                    epc.colorId = rowData.colorId;
                    epc.qty = 1;
                    epc.sku = rowData.sku;
                    epcArray.push(epc)
                })
            }
        });
        if (epcArray.length === 0) {
            $.gritter.add({
                text: "请扫码出库",
                class_name: 'gritter-success  gritter-light'
            });
            return;
        }
        var dtlArray = [];
        $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
            var rowData = $("#addDetailgrid").getRowData(value);
            dtlArray.push(rowData);
        });

        var returnValue = "";
        cs.showProgressBar();
        $.ajax({
            async: false,//必须同步
            dataType: "json",
            url: basePath + "/logistics/purchaseReturn/convertOut.do",
            data: {
                billNo: billNo,
                strEpcList: JSON.stringify(epcArray),
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
                    $("#modal-addEpc-table").modal('hide');

                    var sum_qty = parseInt($("#addDetailgrid").footerData('get').qty);        //reload前总数量
                    var sum_outQty = parseInt($("#addDetailgrid").footerData('get').outQty);  //reload前总出库数量
                    $("#addDetailgrid").jqGrid('setGridParam', {
                        page: 1,
                        url: basePath + '/logistics/purchaseReturn/findDtls.do?billNo=' + billNo,
                    });
                    $("#addDetailgrid").trigger("reloadGrid");
                    $("#grid").trigger("reloadGrid");
                    var all_outQty = sum_outQty + epcArray.length;
                    var diff_qty = sum_qty - all_outQty;
                    if (pageType === "edit") {
                        $("#PIDtl_addUniqCode").attr({"disabled": "disabled"});
                        $("#PIDtl_save").attr({"disabled": "disabled"});
                        $("#edit_origId").attr('disabled', true);
                        $("#edit_billDate").attr('readOnly', true);
                        if (sum_qty > all_outQty) {
                            $.gritter.add({
                                text: "已出库数量为：" + all_outQty + "；剩余数量为：" + diff_qty + "，其余商品请扫码出库",
                                class_name: 'gritter-success  gritter-light'
                            });
                        }else if(sum_qty === all_outQty){
                            $.gritter.add({
                                text: "共" + all_outQty + "件商品，已全部出库",
                                class_name: 'gritter-success  gritter-light'
                            });
                            returnValue = "done";
                        }
                    } else if (pageType === "add") {
                        var alertMessage;
                        if (sum_qty > all_outQty) {
                            alertMessage = "已出库数量为：" + all_outQty + "；剩余数量为：" + diff_qty + "，其余商品请扫码出库"
                        } else if (sum_qty === all_outQty) {
                            alertMessage = "共" + all_outQty + "件商品，已全部出库";
                            returnValue = "done";
                        }
                        bootbox.alert({
                            buttons: {ok: {label: '确定'}},
                            message: alertMessage,
                            callback: function () {

                            }
                        });
                    }
                } else {
                    bootbox.alert(msg.msg);
                }
            }
        });
        return returnValue;
    } else {
        bootbox.alert("请先保存当前单据");
    }
}
function quitback() {
    $.ajax({
        url: basePath + "/logistics/purchaseReturn/quit.do?billNo=" + $("#edit_billNo").val(),
        cache: false,
        async: true,
        type: "POST",
        success: function (data, textStatus) {
            if (textStatus == "success") {
                $.gritter.add({
                    text: $("#edit_billNo").val() + "可以修改",
                    class_name: 'gritter-success  gritter-light'
                });
            }
        }
    });
}
/*查看唯一吗明细*/
function showCodesDetail(uniqueCodes) {

    $("#show-uniqueCode-list").modal('show');
    codeListReload(uniqueCodes,billNo);
}
/**
 * 新增单据调用
 * isScan 是否调用扫码框
 * */
function addNew(){
    billNo = null;
    $("#edit_status").val("0");
    $('#addDetailgrid').jqGrid("clearGridData");
    $('#addDetailgrid').jqGrid('GridUnload');
    initAddGrid();
    $("#editForm").clearForm();
    setEditFormVal();
    initSelectOrigEditForm();
    $("#addDetailgrid").trigger("reloadGrid");
    $(".selectpicker").selectpicker('refresh');
    initButtonGroup(0);
    $("#edit_guest_button").removeAttr("disabled");

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
            /* $("#PIDtl_save").removeAttr("disabled");*/
            if (result) {
                cancelAjax(billId);
            } else {
            }
        }
    });
}
function cancelAjax(billId) {
    $.ajax({
        dataType: "json",
        url: basePath + "/logistics/purchaseReturn/cancel.do",
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
            type:"PR",
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
 * A4打印
 * */
function doPrintA4() {

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
            type:"SR",
            ruleReceipt:"A4"
        },
        success: function (msg) {
            if (msg.success) {
                var addcont = "";
                for (var i = 0; i < msg.result.length; i++) {
                    addcont += "<div class='form-group' onclick=setA4('" + msg.result[i].id + "') title='" + msg.result[i].name + "'>" +
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
function setA4(id) {
    $.ajax({
        dataType: "json",
        url: basePath + "/sys/printset/printMessageA4.do",
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
                        }else if(printParameter.sizeArry.indexOf(printTableCode[b])!=-1){
                            tbodyCont += "<td align='middle' colspan='1' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        } else if(printTableCode[b]=="other"){
                            tbodyCont += "<td align='middle' colspan='1' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        }else{
                            tbodyCont += "<td align='middle' colspan='2' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        }

                    }
                    tbodyCont+="</tr>"
                }
                $("#loadtabA4").html(tbodyCont);
                console.log($("#edit-dialogSanLian").html());
                //LODOP.SET_PRINT_STYLEA("baseHtml", 'Content', $("#edit-dialogSanLian").html());
                LODOP.ADD_PRINT_TABLE(100,1,printParameter.receiptWidthA4,printParameter.receiptheightSanLian,$("#edit-dialogSanLian").html());
                //LODOP.PREVIEW();
                LODOP.PRINT();
                $("#edit-dialog-print").hide();



            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}
function addDetail() {
    $("#modal-addDetail-table").modal('show').on('hidden.bs.modal', function () {
        $("#StyleSearchForm").resetForm();
        $("#stylegrid").clearGridData();
        $("#color_size_grid").clearGridData();
    });
}
/**
 * status 是否关闭对话框
 * */
function addProductInfo(status) {
    if (editDtailRowId != null) {
        $('#addDetailgrid').saveRow(editcolosizeRow);
    }
    var addProductInfo = [];
    if (editcolosizeRow != null) {
        $('#color_size_grid').saveRow(editcolosizeRow,false,'clientArray');
    }
    var styleRow = $("#stylegrid").getRowData($("#stylegrid").jqGrid("getGridParam", "selrow"));

    $.each($("#color_size_grid").getDataIDs(), function (index, value) {
        var productInfo = $("#color_size_grid").getRowData(value);
        if (productInfo.qty > 0) {
            productInfo.price = styleRow.preCast;
            productInfo.outQty = 0;
            productInfo.status = 0;
            productInfo.outStatus = 0;
            productInfo.actPrice = productInfo.price;
            productInfo.totPrice = -Math.abs(productInfo.qty * productInfo.price);
            productInfo.totActPrice = -Math.abs(productInfo.qty * productInfo.actPrice);
            productInfo.sku = productInfo.code;
            productInfo.inStockType = styleRow.class6;
            addProductInfo.push(productInfo);
        }
    });
    var isAdd = true;
    $.each(addProductInfo, function (index, value) {
        isAdd = true;
        $.each($("#addDetailgrid").getDataIDs(), function (dtlndex, dtlValue) {
            var dtlRow = $("#addDetailgrid").getRowData(dtlValue);
            if (value.code === dtlRow.sku) {
                dtlRow.qty = parseInt(dtlRow.qty) + parseInt(value.qty);
                dtlRow.totPrice = dtlRow.qty * dtlRow.price;
                $("#addDetailgrid").setRowData(dtlndex, dtlRow);
                isAdd = false;
            }
        });
        if (isAdd) {
            $("#addDetailgrid").addRowData($("#addDetailgrid").getDataIDs().length, value);
        }
    });
    if(status){
        $("#modal-addDetail-table").modal('hide');
    }
    setFooterData();
}
function confirmWareHouseOut() {
    cs.showProgressBar();
    var billNo = $("#edit_billNo").val();
    var epcArray = [];
    $.each($("#uniqueCodeGrid").getDataIDs(), function (index, value) {
        var rowData = $("#uniqueCodeGrid").getRowData(value);
        epcArray.push(rowData);
    });
    if (epcArray.length === 0) {
        cs.closeProgressBar();
        bootbox.alert("请添加唯一码!");
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
        url: basePath + "/logistics/purchaseReturn/convertOut.do",
        data: {
            billNo: billNo,
            strEpcList: JSON.stringify(epcArray),
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
                $("#modal-addEpc-table").modal('hide');
                $("#addDetailgrid").trigger("reloadGrid");
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
    $("#add-uniqCode-dialog").modal('hide');
}
/**
 * 动态配置按钮,div,表格列字段
 * */
function loadingButtonDivTable(billStatus) {
    var privilegeMap = ButtonAndDivPower(resourcePrivilege);
    $.each(privilegeMap['table'],function(index,value){
        if(value.isShow!=0) {
            $('#addDetailgrid').setGridParam().hideCol(value.privilegeId);
        }
    });
    var disableButtonIds = "";
    switch (billStatus){
        case "-1" :
            disableButtonIds = ["PIDtl_cancel","PIDtl_save","PIDtl_addUniqCode","PIDtl_wareHouseOut","PRDtl_batchUniqCode","PRDtl_batchWareHouseOut"];
            break;
        case "0" :
            disableButtonIds = [];
            break;
        case "1":
            disableButtonIds = [];
            break;
        case "2" :
            disableButtonIds = ["PIDtl_cancel","PIDtl_save","PIDtl_addUniqCode","PIDtl_wareHouseOut","PRDtl_batchUniqCode","PRDtl_batchWareHouseOut"];
            break;
        case "3":
            disableButtonIds = ["PIDtl_cancel","PIDtl_save","PIDtl_addUniqCode","PRDtl_batchUniqCode"];
            break;
        default:
            disableButtonIds = [];
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
//批量出库
function batchWareHouseOut() {
    var sum_qty = parseInt($("#addDetailgrid").footerData('get').qty);
    var sum_outQty = parseInt($("#addDetailgrid").footerData('get').outQty);
    if (sum_qty === sum_outQty) {
        $.gritter.add({
            text: '已全部出库',
            class_name: 'gritter-success  gritter-light'
        });
    }else if (sum_qty > sum_outQty) {
        billNo = $("#edit_billNo").val();
        inOntWareHouseValid = 'wareHouseOut_valid';
        var origId = $("#edit_origId").val();
        taskType = 0;
        wareHouse = origId;
        $("#modal-batch-show-table").modal('show').on('hidden.bs.modal', function () {
            $("#billInformationOutgrid").clearGridData();
            $("#notThisOneOutgrid").clearGridData();
            skuInfo=[];
            $("#outCodeQty").text(0);
        });
        lodeBillInformationOutgrid();
        fullOutWebSocket();
    }
}
//批量扫码
function batchUniqCode() {
    var origId = $("#edit_origId").val();
    taskType = 0;
    wareHouse = origId;
    billNo = $("#edit_billNo").val();
    if (origId && origId != null) {
        $("#modal-batch-table").modal('show').on('hidden.bs.modal', function () {
            $("#batchDetailgrid").clearGridData();
            skuInfo=[];
        });
        initWebSocket();
        $("#scanCodeQty").text(0);
    } else {
        bootbox.alert("出库仓库不能为空！")
    }
}

//批量扫描保存方法
function saveEPC() {
    var IDs=$("#batchDetailgrid").getDataIDs();
    var productListInfo=[];
    $.each(IDs,function (index,value) {
        var rowData=$("#batchDetailgrid").jqGrid('getRowData',value);
        var uRowData=$("#batchDetailgrid").jqGrid('getRowData',value);
        //正常唯一码
        if(rowData.uniqueCodes!=""&&rowData.uniqueCodes!=undefined){
            var codes=rowData.uniqueCodes.split(",");
        }
        //异常唯一码
        if(uRowData.noOutPutCode!=""&&uRowData.noOutPutCode!=undefined){
            var noCoses=rowData.noOutPutCode.split(",");
        }
        if (codes!=undefined){
            delete rowData.noOutPutCode;
            $.each(codes,function (cIndex,cValue) {
                var newRowData=JSON.parse(JSON.stringify(rowData));
                newRowData.uniqueCodes=cValue;
                newRowData.qty = 1;
                //判断实际价格是不是小于门店批发价格
                if($('#sale_discount_div').is(':hidden')) {
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
                productListInfo.push(newRowData);
            });
        }
        if (noCoses!=undefined){
            delete uRowData.uniqueCodes;
            $.each(noCoses,function (nIndex,nValue) {
                var newURowData=JSON.parse(JSON.stringify(uRowData));
                newURowData.noOutPutCode=nValue;
                newURowData.qty = 1;
                //判断实际价格是不是小于门店批发价格
                if($('#sale_discount_div').is(':hidden')) {
                    newURowData.actPrice = Math.round(newURowData.price * newURowData.discount) / 100;
                    newURowData.abnormalStatus=0;
                }else {
                    if(Math.round(newURowData.price * newURowData.discount) / 100<newURowData.wsPrice&&isUserAbnormal){
                        newURowData.actPrice = newURowData.wsPrice;
                        newURowData.discount = parseFloat(newURowData.wsPrice/newURowData.price).toFixed(2)*100;
                        newURowData.abnormalStatus=1;
                    }else{
                        newURowData.actPrice = Math.round(newURowData.price * newURowData.discount) / 100;
                        newURowData.abnormalStatus=0;
                    }
                }
                newURowData.outQty = 0;
                newURowData.inQty = 0;
                newURowData.status = 0;
                newURowData.inStatus = 0;
                newURowData.outStatus = 0;
                newURowData.totPrice = newURowData.price;
                newURowData.totActPrice = newURowData.actPrice;
                var stylePriceMap={};
                stylePriceMap['price']=newURowData.price;
                stylePriceMap['wsPrice']=newURowData.wsPrice;
                stylePriceMap['puPrice']=newURowData.puPrice;
                newURowData.stylePriceMap=JSON.stringify(stylePriceMap);
                productListInfo.push(newURowData);
            });
        }
    });
    var isAdd = true;
    var alltotActPrice = 0;
    $.each(productListInfo,function (index,value) {
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
                if (value.noOutPutCode!=""&&value.noOutPutCode!=undefined&&dtlRow.noOutPutCode.indexOf(value.noOutPutCode) === -1) {
                    dtlRow.qty = parseInt(dtlRow.qty) + 1;
                    dtlRow.totPrice = dtlRow.qty * dtlRow.price;
                    dtlRow.totActPrice = dtlRow.qty * dtlRow.actPrice;
                    alltotActPrice += dtlRow.qty * dtlRow.actPrice;
                    dtlRow.noOutPutCode = dtlRow.noOutPutCode + "," + value.noOutPutCode;
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
    $("#modal-batch-table").modal('hide');
    setFooterData();
}
