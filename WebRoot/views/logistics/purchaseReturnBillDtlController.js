var allCodes; //用于拼接所有添加过的唯一码，防止重复添加
var taskType; //用于判断出入库类型 1入库 0 出库
var wareHouse;
var inOntWareHouseValid; //用于判断在编辑BillDtl时出入库操作是否需要校验，使用哪种校验
var skuQty = {};//保存每个SKU对应的出入库数量
var allCodeStrInDtl = "";
$(function () {
    iniGrid();
    iniForm();
    keydown();
    initButtonGroup();
    initEditFormValid();
    if(billNo!=""){
        sessionStorage.setItem("billNoPurchaseReturn",billNo);
    }
});
var editDtailRowId = null;
var loadComplete = true, loadStatus = true;
var isPassed = true;

function iniForm() {
    initSelectOrigForm();

    if (pageType === "add") {
        $("#search_billDate").val(getToDay("yyyy-MM-dd"));
    } else if (pageType === "edit") {
        $("#search_origId").val(purchaseReturnOrder_origId);
        if(returnStatus !== "0"){
            $("#search_origId").attr('disabled', true);
            $("#search_billDate").attr('readOnly', true);
        }
    } else if (pageType === "copyAdd") {
        $("#search_billNo").val("");
        $("#search_origId").val(purchaseReturnOrder_origId);
        $("#search_billDate").val(getToDay("yyyy-MM-dd"));
    }
}

function initSelectOrigForm() {
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + $("#findOwnerId").val(),
        cache: false,
        async:false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_origId").empty();
            $("#search_origId").append("<option value='' style='background-color: #eeeeee'>--请选择出库仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_origId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                $("#search_origId").trigger('chosen:updated');
            }
        }
    });
    $("#search_origId").val($("#defaultWarehId").val());
}

function initButtonGroup() {
    $("#addDetailgrid").clearGridData();
    if (pageType === "add") {
        $("#buttonGroup").html("" +
            "<button id='SODtl_save' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='save()'>" +
            "    <i class='ace-icon fa fa-save'></i>" +
            "    <span class='bigger-110'>保存</span>" +
            "</button>" +
            "<button id='SODtl_addUniqCode' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='addUniqCode()'>" +
            "    <i class='ace-icon fa fa-barcode'></i>" +
            "    <span class='bigger-110'>扫码</span>" +
            "</button>" +
            "<button id='SODtl_wareHouseOut' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='wareHouseOut()'>" +
            "    <i class='ace-icon fa fa-sign-out'></i>" +
            "    <span class='bigger-110'>出库</span>" +
            "</button>"
        );
    }
    if (pageType === "edit") {
        $("#buttonGroup").html("" +
            "<button id='SODtl_save' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='save()'>" +
            "    <i class='ace-icon fa fa-save'></i>" +
            "    <span class='bigger-110'>保存</span>" +
            "</button>" +
            "<button id='SODtl_addUniqCode' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='addUniqCode()'>" +
            "    <i class='ace-icon fa fa-barcode'></i>" +
            "    <span class='bigger-110'>扫码</span>" +
            "</button>" +
            "<button id='SODtl_wareHouseOut' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='edit_wareHouseOut()'>" +
            "    <i class='ace-icon fa fa-sign-out'></i>" +
            "    <span class='bigger-110'>出库</span>" +
            "</button>"+
            "<button  type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='doPrint(billNo)'>" +
            "    <i class='ace-icon fa fa-reply'></i>" +
            "    <span class='bigger-110'>打印</span>" +
            "</button>"
        );
        if(returnStatus !== "0"){
            $("#search_vendor_button").attr({"disabled": "disabled"});
            $("#SODtl_save").attr({"disabled": "disabled"});
            $("#SODtl_addUniqCode").attr({"disabled": "disabled"});
        }
    }
}

function iniGrid() {
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
            {name: 'styleId', label: '款号', width: 40},
            {name: 'styleName', label: '款名', width: 40},
            {name: 'colorId', label: '色码', width: 40},
            {name: 'colorName', label: '颜色', width: 40},
            {name: 'sizeId', label: '尺码', width: 40},
            {name: 'sizeName', label: '尺名', width: 40},
            {
                name: 'qty', label: '数量', editable: true, width: 40,
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
                name: 'actPrice', label: '实际价格', editable: true, width: 40,
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
            setFooterData();
        },
        loadComplete: function () {
            initAllCodesList();
        }

    });
    if (pageType == "edit" && returnStatus !== "0") {
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

function setFooterData() {
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
    $("#search_actPrice").val(sum_totActPrice);
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
                $("#search_billNo").val(msg.result);
                $("#add_addDetailgrid").hide();
                $("#savebutton").hide();
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}

function saveItem(rowId) {
    $('#addDetailgrid').saveRow(rowId);
    var value = $('#addDetailgrid').getRowData(rowId);
    value.totPrice = -Math.floor(value.qty * value.price);
    value.totActPrice = -Math.floor(value.qty * value.actPrice);
    $("#addDetailgrid").setRowData(rowId, value);
    setFooterData();
}

function deleteItem(rowId) {
    $("#addDetailgrid").jqGrid("delRowData", rowId);
    setFooterData();
}

function isInteger(obj) {
    var num = Math.floor(obj);
    return num == obj;
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
            },
        }
    });
}

function addUniqCode() {
    inOntWareHouseValid = 'addPage_scanUniqueCode';
    taskType = 0;
    var origId = $("#search_origId").val();
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
    setFooterData();
}

function wareHouseOut() {
    var billNo = $("#search_billNo").val();
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
            async: true,
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

                    var all_outQty = sum_outQty + epcArray.length;
                    var diff_qty = sum_qty - all_outQty;
                    if (pageType === "edit") {
                        $("#SODtl_addUniqCode").attr({"disabled": "disabled"});
                        $("#SODtl_save").attr({"disabled": "disabled"});
                        $("#search_origId").attr('disabled', true);
                        $("#search_billDate").attr('readOnly', true);
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
                        }
                        bootbox.alert({
                            buttons: {ok: {label: '确定'}},
                            message: alertMessage,
                            callback: function () {
                                quitback();

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
        url: basePath +"/logistics/purchaseReturn/quit.do?billNo=" +billNo,
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {

            if(textStatus=="success"){
                $.gritter.add({
                    text: billNo+"可以编辑",
                    class_name: 'gritter-success  gritter-light'
                });
                window.location.href = basePath + "/logistics/purchaseReturn/index.do";
            }

        }
    });
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

        var origId = $("#search_origId").val();
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
        allCodes = "";
    }
}

function confirmWareHouseOut() {
    cs.showProgressBar();
    var billNo = $("#search_billNo").val();
    var epcArray = [];
    $.each($("#uniqueCodeGrid").getDataIDs(), function (index, value) {
        var rowData = $("#uniqueCodeGrid").getRowData(value);
        epcArray.push(rowData);
    });
    if (epcArray.length === 0) {
        cs.showProgressBar();
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

var dialogOpenPage;
function openSearchVendorDialog() {
    dialogOpenPage = "purchaseReturn";
    $("#modal_vendor_search_table").modal('show').on('shown.bs.modal', function () {
        initVendorSelect_Grid();
    });
    $("#searchVendorDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='confirm_selected_VendorId_purchaseReturn()'>确认</button>"
    );
}
function doPrint() {
    debugger;
    /*$("#editForm").resetForm();*/
    $("#edit-dialog-print").modal('show');
    $("#form_code").removeAttr("readOnly");
    var billNo=$("#search_billNo").val();
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
    debugger;
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