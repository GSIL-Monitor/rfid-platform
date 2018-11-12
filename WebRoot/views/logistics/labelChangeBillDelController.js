var taskType; //用于判断出入库类型 1入库 0 出库
var wareHouse;
var allCodes; //用于拼接所有添加过的唯一码，防止重复添加
var inOntWareHouseValid; //用于判断在编辑BillDtl时出入库操作是否需要校验，使用哪种校验。
var allCodeStrInDtl = "";  //入库时，所有明细中的唯一码
var class9 = "";
var isCheckWareHouse = false;//是否检测出库仓库
$(function () {
    initForm();
    initGrid();
    keydown();
});
function initForm() {
    //initSelectDestForm();
    initSelectOrigForm();
    initSelectclass9();
    initButtonGroup();
    if (pageType == "add") {
        if (type == "CS") {
            $("#search_discount").attr('disabled', true);
            $("#search_prefix").attr('disabled', true);
            $("#search_suffix").attr('disabled', true);

        }
        if (type == "PC") {
            $("#search_nowclass9").attr('disabled', true);
            $("#search_beforeclass9").attr('disabled', true);
            $("#search_prefix").attr('disabled', true);
            $("#search_suffix").attr('disabled', true);
        }
        if (type == "TG") {
            $("#search_nowclass9").attr('disabled', true);
            $("#search_beforeclass9").attr('disabled', true);
            $("#search_prefix").attr('disabled', true);
            $("#search_suffix").attr('disabled', true);
        }
        if (type == "ID") {
            $("#search_nowclass9").attr('disabled', true);
            $("#search_beforeclass9").attr('disabled', true);
            $("#search_discount").attr('disabled', true);
        }

        $("#select_changeType").val(type);
        $("#select_changeType").attr('disabled', true);
        $("#search_origId").val(defaultWarehId);
        $("#search_origId").attr('disabled', true);
    }

    if (pageType == "edit") {
        $("#search_nowclass9").val(nowclass9);
        $("#search_beforeclass9").val(beforeclass9);
        $("#search_origId").val(origId);
        $("#select_changeType").val(changeType);
        $("#search_nowclass9").attr('disabled', true);
        $("#search_beforeclass9").attr('disabled', true);
        $("#search_origId").attr('disabled', true);
        $("#select_changeType").attr('disabled', true);
        $("#search_billNo").attr('disabled', true);
        $("#search_billDate").attr('disabled', true);
        $("#search_discount").attr('disabled', true);

    }

}
function initSelectclass9() {
    $.ajax({
        url: basePath + "/sys/property/findclass9name.do?filter_EQS_type=C9",
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_beforeclass9").empty();
            $("#search_nowclass9").empty();
            $("#search_beforeclass9").append("<option value='' style='background-color: #eeeeee'>--请选择原系列--</option>");
            $("#search_nowclass9").append("<option value='' style='background-color: #eeeeee'>--请选择原系列--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_beforeclass9").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                $("#search_nowclass9").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                $("#search_beforeclass9").trigger('chosen:updated');
                $("#search_nowclass9").trigger('chosen:updated');
            }
        }
    });


}


function initSelectOrigForm() {


    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9",
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_origId").empty();
            $("#search_origId").append("<option value='' style='background-color: #eeeeee'>--请选择仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_origId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                $("#search_origId").trigger('chosen:updated');
            }
        }
    });


}

function initGrid() {
    $("#addDetailgrid").jqGrid({
        height: "auto",
        url: basePath + "/logistics/labelChangeBill/findBillDtl.do?billNo=" + billNo,
        datatype: "json",
        sortorder: 'desc',
        colModel: [
            {name: 'id', label: 'id', hidden: true},
            {name: 'billId', label: 'billId', hidden: true},
            {name: 'billNo', label: 'billNo', hidden: true},
            {name: 'styleId', label: '原款号', sortable: true, width: 40},
            {name: 'styleNew', label: '新款号', sortable: true, width: 40},
            {
                name: "", label: "操作", width: 30, editable: false, sortable: false, align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    if (pageType != "edit") {
                        return "<a href='javascript:void(0);' onclick=saveItem('" + options.rowId + "')><i class='ace-icon ace-icon fa fa-save' title='保存'></i></a>"
                            + "<a style='margin-left: 20px' href='javascript:void(0);' onclick=deleteItem('" + options.rowId + "')><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";
                    } else {
                        return ""
                    }


                }
            },
            {name: 'colorId', label: '色码', sortable: true, width: 40},
            {name: 'sizeId', label: '尺寸', sortable: true, width: 40},
            {name: 'qty', label: '数量', width: 40},
            {name: 'outQty', label: '已出库数量', width: 40},
            {name: 'inQty', label: '已入库数量', width: 40},
            {name: 'sku', label: 'sku', width: 40},
            {
                name: 'discount', label: "折扣", width: 40, editable: true
            },
            {
                name: 'price', label: '销售价格', editable: false, width: 40,
                editrules: {
                    number: true
                },
                formatter: function (cellValue, options, rowObject) {
                    var price = parseFloat(cellValue).toFixed(2);
                    return price;
                }
            },
            {name: 'preCast', label: '采购价', hidden: true, width: 40},
            {
                name: 'actPrice', label: '实际价格', editable: true, width: 40,
                editrules: {
                    number: true
                },
                formatter: function (cellValue, options, rowObject) {
                    var actPrice = parseFloat(cellValue).toFixed(2);
                    return actPrice;
                }
            },
            {
                name: 'totActPrice', label: '实际金额', width: 40,
                formatter: function (cellValue, options, rowObject) {
                    var totActPrice = parseFloat(cellValue).toFixed(2);
                    return totActPrice;
                }
            },
            {
                name: '', label: '唯一码明细', width: 40, align: "center",
                formatter: function (cellValue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=showCodesDetail('" + rowObject.sku + "')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
                }
            },
            {name: 'uniqueCodes', label: '唯一码', hidden: true}

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
        cellEdit: true,
        cellsubmit: 'clientArray',
        afterSaveCell: function (rowid, cellname, value, iRow, iCol) {

            if (cellname === "discount") {

                var var_price = Math.round(value * $('#addDetailgrid').getCell(rowid, "price")) / 100;
                var var_actPrice = Math.round(value * $('#addDetailgrid').getCell(rowid, "actPrice")) / 100;
                var var_totActPrice = Math.round(value * $('#addDetailgrid').getCell(rowid, "totActPrice")) / 100;
                $('#addDetailgrid').setCell(rowid, "price", var_price);
                $('#addDetailgrid').setCell(rowid, "actPrice", var_actPrice);
                $('#addDetailgrid').setCell(rowid, "totActPrice", var_totActPrice);
            }
            setFooterData();
        },
        gridComplete: function () {
            setFooterData();
        },
        loadComplete: function () {
            initAllCodesList();

        }
    });
}
function initButtonGroup() {
    console.log(pageType);
    if (pageType === "add") {
        $("#buttonGroup").html("" +
            "<button id='Dtl_save' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='save()'>" +
            "    <i class='ace-icon fa fa-save'></i>" +
            "    <span class='bigger-110'>保存</span>" +
            "</button>" +
            "<button id='Dtl_addUniqCode' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='addUniqCode()'>" +
            "    <i class='ace-icon fa fa-barcode'></i>" +
            "    <span class='bigger-110'>扫码</span>" +
            "</button>" +
            "<button id='Dtl_wareHouseOut' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='wareHouseOutIn()'>" +
            "    <i class='ace-icon fa fa-sign-out'></i>" +
            "    <span class='bigger-110'>出库入库</span>" +
            "</button>" +
            "<button id='Dtl_findBirthno' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='findbirth()'>" +
            "    <i class='ace-icon fa fa-search'></i>" +
            "    <span class='bigger-110'>查找标签初始化</span>" +
            "</button>"
        );
    }
    if (pageType === "edit") {
        $("#buttonGroup").html("" +
            "<button id='Dtl_save' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='save()'>" +
            "    <i class='ace-icon fa fa-save'></i>" +
            "    <span class='bigger-110'>保存</span>" +
            "</button>" +
            "<button id='Dtl_wareHouseOut' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='wareHouseOutIn()'>" +
            "    <i class='ace-icon fa fa-sign-out'></i>" +
            "    <span class='bigger-110'>出库入库</span>" +
            "</button>" +
            "<button id='Dtl_findBirthno' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='findbirth()'>" +
            "    <i class='ace-icon fa fa-search'></i>" +
            "    <span class='bigger-110'>查找标签初始化</span>" +
            "</button>"
        );
    }

}
function addUniqCode() {
    inOntWareHouseValid = 'addPage_scanUniqueCode';
    var origId = $("#search_origId").val();
    //var destId=$("#search_destId").val();
    var beforeclass9 = $("#search_beforeclass9").val();
    var nowclass9 = $("#search_nowclass9").val();
    var changeType = $("#select_changeType").val();
    var discount = $("#search_discount").val();

    wareHouse = origId;
    class9 = $("#search_beforeclass9").val().split("-")[1];
    if (origId == "" || origId == null) {
        bootbox.alert("仓库不能为空！");
        return
    }
    /*if (destId ==""|| destId == null) {
     bootbox.alert("入库仓库不能为空！")
     return
     }*/
    if (type == "CS") {
        if (beforeclass9 == "" || beforeclass9 == null) {
            bootbox.alert("原系列不能为空！");
            return
        }
        if (nowclass9 == "" || nowclass9 == null) {
            bootbox.alert("现系列不能为空！");
            return
        }
        if (beforeclass9 == nowclass9) {
            bootbox.alert("原系列和现系列不能相同！");
            return
        }
        if (changeType == "" || changeType == null) {
            bootbox.alert("转变类型不能为空！");
            return
        }
        taskType = 3;
    } else {
        taskType = 0;
    }

    $("#dialog_buttonGroup").html("" +
        "<button  type='button' id = 'so_savecode_button'  class='btn btn-primary' onclick='addProductsOnCode()'>保存</button>"
    );
    $("#add-uniqCode-dialog").modal('show').on('hidden.bs.modal', function () {
        $("#uniqueCodeGrid").clearGridData();
    });
    initUniqeCodeGridColumn("CT-LS");
    allCodes = "";

}
function addProductsOnCode() {
    var productListInfo = [];
    if (!$('#so_savecode_button').prop('disabled')) {
        $("#so_savecode_button").attr({"disabled": "disabled"});

        $.each($("#uniqueCodeGrid").getDataIDs(), function (index, value) {
            var productInfo = $("#uniqueCodeGrid").getRowData(value);
            productInfo.qty = 1;

            productInfo.price = productInfo.price;
            productInfo.preCast = productInfo.preCast;
            productInfo.outQty = 0;
            productInfo.inQty = 0;
            productInfo.uniqueCodes = productInfo.code;
            if ($("#search_discount").val() && $("#search_discount").val() !== null) {
                productInfo.discount = $("#search_discount").val();
            } else {
                productInfo.discount = 100;
            }
            productInfo.actPrice = Math.round(productInfo.price * productInfo.discount) / 100;
            productInfo.totPrice = productInfo.price;
            productInfo.totActPrice = productInfo.actPrice;
            productListInfo.push(productInfo);
        });
        if (productListInfo.length == 0) {
            bootbox.alert("请添加唯一码");
            return;
        }
        var isAdd = true;
        var alltotActPrice = 0;
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
                    dtlRow.actPrice = (dtlRow.price * dtlRow.discount) / 100;
                    dtlRow.totActPrice = dtlRow.qty * dtlRow.actPrice;
                    alltotActPrice += dtlRow.qty * dtlRow.actPrice;
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
function saveItem(rowId) {
    if (addDetailgridiRow != null && addDetailgridiCol != null) {
        $("#addDetailgrid").saveCell(addDetailgridiRow, addDetailgridiCol);
        addDetailgridiRow = null;
        addDetailgridiCol = null;
    }
    var value = $('#addDetailgrid').getRowData(rowId);
    value.totPrice = value.qty * value.price;
    value.totActPrice = value.qty * value.actPrice;
    $("#addDetailgrid").setRowData(rowId, value);
    setFooterData();
}

function deleteItem(rowId) {
    var value = $('#addDetailgrid').getRowData(rowId);
    $("#addDetailgrid").jqGrid("delRowData", rowId);
    setFooterData();


}
function setFooterData() {
    var sum_qty = $("#addDetailgrid").getCol('qty', false, 'sum');
    var sum_outQty = $("#addDetailgrid").getCol('outQty', false, 'sum');
    var sum_inQty = $("#addDetailgrid").getCol('inQty', false, 'sum');
    var sum_totPrice = $("#addDetailgrid").getCol('totPrice', false, 'sum');
    var sum_totActPrice = Math.round($("#addDetailgrid").getCol('totActPrice', false, 'sum'));
    $("#search_actPrice").val(sum_totActPrice);
    $("#addDetailgrid").footerData('set', {
        styleId: "合计",
        qty: sum_qty,
        outQty: sum_outQty,
        inQty: sum_inQty,
        totPrice: sum_totPrice,
        totActPrice: sum_totActPrice
    });
}

function save() {

    $("#search_nowclass9").attr('disabled', false);
    $("#search_beforeclass9").attr('disabled', false);
    $("#search_origId").attr('disabled', false);
    $("#select_changeType").attr('disabled', false);
    $("#search_billNo").attr('disabled', false);
    $("#search_billDate").attr('disabled', false);
    $("#search_discount").attr('disabled', false);
    if ($("#addDetailgrid").getDataIDs().length == 0) {
        $.gritter.add({
            text: "请扫描需要该标签的商品",
            class_name: 'gritter-success  gritter-light'
        });
        addDisabled();
        return;
    }
    var discount = $("#search_discount").val();
    if (type == "PC") {
        if (discount == "" || discount == null) {
            bootbox.alert("折扣不能为空！");
            addDisabled();

            return
        }
        if (discount > 100 || discount <= 0) {
            bootbox.alert("折扣请添写1到100的数字！");
            addDisabled();

            return
        }

    }
    var suffix = $("#search_suffix").val();
    var prefix = $("#search_prefix").val();
    var isTrue = false;
    if (type == "ID") {
        if (suffix != "") {
            isTrue = true;
        }
        if (prefix != ""){
            isTrue =true;
        }
        if(!isTrue){
            bootbox.alert("请填写新款号前缀或者后缀！");
            return
        }
            }
    cs.showProgressBar();
    var dtlArray = [];
    var isCanSave = true;
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        console.log(rowData);
        var styleId = rowData.styleId;
        var suffixVariate = styleId.substring(styleId.length - 4, styleId.length - 2);
        if (suffixVariate == "PD") {
            isCanSave = false;
            return
        }
        dtlArray.push(rowData);
    });
    if (isCanSave) {
        $.ajax({
            dataType: "json",
            async: false,
            url: basePath + "/logistics/labelChangeBill/save.do",
            data: {
                bill: JSON.stringify(array2obj($("#editForm").serializeArray())),
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
                    $("#search_billNo").val(msg.result);
                    $("#search_id").val(msg.result);
                    location.href = basePath + "/logistics/labelChangeBill/edit.do?billNo=" + msg.result;


                } else {
                    bootbox.alert(msg.msg);
                }
            }
        });
    } else {
        bootbox.alert("已有打折处理的商品！");
        addDisabled();
        cs.closeProgressBar();

    }

}

function wareHouseOutIn() {
    cs.showProgressBar();
    $("#Dtl_wareHouseOut").attr({"disabled": "disabled"});
    var billNo = $("#search_billNo").val();
    if (billNo && billNo != null) {
        if (outStockCheck()) {
            cs.closeProgressBar();
            $("#Dtl_wareHouseOut").removeAttr("disabled");
            return;
        }
        var allUniqueCodes = "";
        $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
            var rowData = $("#addDetailgrid").getRowData(value);
            allUniqueCodes = allUniqueCodes + "," + rowData.uniqueCodes;
        });
        if (allUniqueCodes.substr(0, 1) == ",") {
            allUniqueCodes = allUniqueCodes.substr(1);
        }
        var uniqueCodes_inHouse;
        taskType = 0;
        wareHouse = $("#search_origId").val();
        $.ajax({
            async: false,
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
                uniqueCodes_inHouse = data.result;
            }
        });
        var epcArray = [];
        $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
            var rowData = $("#addDetailgrid").getRowData(value);
            var curOutQty;
            rowData.outQty == "" ? curOutQty = 0 : curOutQty = rowData.outQty;
            var codes = rowData.uniqueCodes.split(",");
            if (codes && codes != null && codes != "") {
                $.each((codes), function (index, value) {
                    if (rowData.qty > curOutQty) {
                        if (uniqueCodes_inHouse.indexOf(value) != -1) {
                            var epc = {};
                            epc.code = value;
                            epc.styleId = rowData.styleId;
                            epc.sizeId = rowData.sizeId;
                            epc.colorId = rowData.colorId;
                            epc.qty = 1;
                            epc.sku = rowData.sku;
                            epcArray.push(epc);
                            curOutQty++;
                        }
                    }
                })
            }
        });
        if (epcArray.length == 0) {
            cs.closeProgressBar();
            $.gritter.add({
                text: "唯一码已全部出库",
                class_name: 'gritter-success  gritter-light'
            });
            return;
        }
        var dtlArray = [];
        $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
            var rowData = $("#addDetailgrid").getRowData(value);
            dtlArray.push(rowData);
        });
        $.ajax({
            async: false,
            dataType: "json",
            url: basePath + "/logistics/labelChangeBill/wareHouseOutIn.do",
            data: {
                billNo: billNo,
                strEpcList: JSON.stringify(epcArray),
                strDtlList: JSON.stringify(dtlArray),
                userId: userId
            },
            type: "POST",
            success: function (msg) {
                cs.closeProgressBar();
                $("#Dtl_wareHouseOut").removeAttr("disabled");
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
                        url: basePath + "/logistics/labelChangeBill/findBillDtl.do?billNo=" + billNo
                    });
                    $("#addDetailgrid").trigger("reloadGrid");

                    var all_outQty = sum_outQty + epcArray.length;
                    var diff_qty = sum_qty - all_outQty;
                    if (pageType === "edit") {

                        //出库成功后，禁止保存扫码和表单上的控件
                        $("#search_busnissId").attr('disabled', true);
                        $("#search_origId").attr('disabled', true);
                        $("#search_destId").attr('disabled', true);
                        $("#search_billDate").attr('readOnly', true);
                        $("#search_guest_button").attr({"disabled": "disabled"});
                        $("#SODtl_addUniqCode").attr({"disabled": "disabled"});
                        $("#SODtl_save").attr({"disabled": "disabled"});
                        if (sum_qty > all_outQty) {
                            $.gritter.add({
                                text: "已出库入库数量为：" + all_outQty + "；剩余数量为：" + diff_qty + "，其余商品请扫码出库",
                                class_name: 'gritter-success  gritter-light'
                            });
                            edit_wareHouseOut();
                        } else if (sum_qty === all_outQty) {
                            $.gritter.add({
                                text: "共" + all_outQty + "件商品，已全部出库入库",
                                class_name: 'gritter-success  gritter-light'
                            });
                        }
                    } else if (pageType === "add") {
                        var alertMessage;
                        if (sum_qty > all_outQty) {
                            alertMessage = "已出库入库数量为：" + all_outQty + "；剩余数量为：" + diff_qty + "，其余商品请扫码出库"
                        } else if (sum_qty == all_outQty) {
                            alertMessage = "共" + all_outQty + "件商品，已全部出库入库";
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
    } else {
        cs.closeProgressBar();
        bootbox.alert("请先保存当前单据");
    }


}
function outStockCheck() {
    var sum_qty = parseInt($("#addDetailgrid").footerData('get').qty);
    var sum_outQty = parseInt($("#addDetailgrid").footerData('get').outQty);
    if (sum_qty <= sum_outQty) {
        $.gritter.add({
            text: '已全部出库',
            class_name: 'gritter-success  gritter-light'
        });
        return true;
    }
}
function search_discount_onblur() {
    setDiscount();
}
//将整单折扣设置到明细中
function setDiscount() {
    /* if (addDetailgridiRow != null && addDetailgridiCol != null) {
     $("#addDetailgrid").saveCell(addDetailgridiRow, addDetailgridiCol);
     addDetailgridiRow = null;
     addDetailgridiCol = null;
     }*/
    var discount = $("#search_discount").val();
    if (discount && discount != null && discount != "") {
        $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
            $('#addDetailgrid').setCell(value, "discount", discount);
            var var_price = Math.round(discount * $('#addDetailgrid').getCell(value, "price")) / 100;
            var var_actPrice = Math.round(discount * $('#addDetailgrid').getCell(value, "actPrice")) / 100;
            var var_totActPrice = Math.round(discount * $('#addDetailgrid').getCell(value, "totActPrice")) / 100;
            $('#addDetailgrid').setCell(value, "price", var_price);
            $('#addDetailgrid').setCell(value, "actPrice", var_actPrice);
            $('#addDetailgrid').setCell(value, "totActPrice", var_totActPrice);
        });
    }
    setFooterData();
}
function findbirth() {
    $("#show-findBirthNo-list").modal('show');
    initBirthNoList($("#search_billNo").val());

}

function showCodesDetail(sku) {

    var billNo = $("#search_billNo").val();
    var uniqueCodes = "";
    $.ajax({
        async: false,
        dataType: "json",
        url: basePath + "/stock/warehStock/findCodesStr.do",
        data: {
            sku: sku,
            billNo: billNo
        },
        type: "POST",
        success: function (result) {
            if (result.success) {
                uniqueCodes = result.result;
            } else {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
            }
        }
    });

    $("#show-uniqueCode-list").modal('show');
    initUniqueCodeList(uniqueCodes);
    codeListReload(uniqueCodes);
}

function addDisabled() {
    if (type == "CS") {
        $("#search_discount").attr('disabled', true);

    }
    if (type == "PC") {
        $("#search_nowclass9").attr('disabled', true);
        $("#search_beforeclass9").attr('disabled', true);
    }
    if (type == "SC") {
        $("#search_discount").attr('disabled', true);
        $("#search_nowclass9").attr('disabled', true);
        $("#search_beforeclass9").attr('disabled', true);
    }

    $("#select_changeType").val(type);
    $("#select_changeType").attr('disabled', true);
    $("#search_origId").val(defaultWarehId);
    $("#search_origId").attr('disabled', true);
}

