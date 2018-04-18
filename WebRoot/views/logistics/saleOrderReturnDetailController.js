var addDetailgridiRow;//存储iRow
var addDetailgridiCol;//存储iCol
var allCodes; //用于拼接所有添加过的唯一码，防止重复添加
var taskType; //用于判断出入库类型 1入库 0 出库
var wareHouse;
var inOntWareHouseValid; //用于判断在编辑BillDtl时出入库操作是否需要校验，使用哪种校验。
var skuQty = {};//保存每个SKU对应的出入库数量。
var allCodeStrInDtl = "";  //入库时，所有明细中的唯一码
var billNo;
$(function () {
    initGrid();
    initForm();
    keydown();
    input_keydown();
    initButtonGroup();
    initEditFormValid();
    if (billNo != "") {
        sessionStorage.setItem("billNosaleReturn", billNo);
    }
    if (pageType === "add") {
        $("#search_origUnitId").val(defalutCustomerId);
        $("#search_origUnitName").val(defalutCustomerName);


        $("#search_discount").val(defalutCustomerdiscount);
        if (defalutCustomerId != "" && defalutCustomerId != undefined) {
            addUniqCode();
        }


    }
});

function initForm() {
    initCustomerTypeForm();
    initSelectDestForm();
    initSelectOrigForm();
    initSelectBusinessIdForm();
    $("#search_customerType").attr('disabled', true);

    if (pageType === "add") {
        $("#search_billDate").val(getToDay("yyyy-MM-dd"));
        $("#search_payPrice").val(0);
        $("#search_destId").val(defaultWarehId);
    } else if (pageType === "edit") {
        $("#search_customerType").val(saleOrderReturn_customerType);
        $("#search_origId").val(saleOrderReturn_origId);
        $("#search_destId").val(saleOrderReturn_destId);
        $("#search_busnissId").val(saleOrderReturn_busnissId);
        if (saleOrderReturn_status !== "0" && userId !== "admin") {
            $("#search_origId").attr('disabled', true);
            $("#search_destId").attr('disabled', true);
            $("#search_billDate").attr('readOnly', true);
            $("#search_busnissId").attr('disabled', true);
        }
    } else if (pageType === "copyAdd") {
        $("#search_billNo").val("");
        $("#search_origId").val(saleOrderReturn_origId);
        $("#search_destId").val(saleOrderReturn_destId);
        $("#search_busnissId").val(saleOrderReturn_busnissId);
        $("#search_billDate").val(getToDay("yyyy-MM-dd"));
    }
    $("#search_customerType").attr('disabled', true);
}

function initCustomerTypeForm() {
    $.ajax({
        url: basePath + "/sys/property/searchByType.do?type=CT",
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_customerType").empty();
            $("#search_customerType").append("<option value='' style='background-color: #eeeeee'>--请选择客户类型--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_customerType").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                $("#search_customerType").trigger('chosen:updated');
            }
            if (pageType === "add") {
                if (defalutCustomerId != "" && defalutCustomerId != undefined) {
                    $("#search_customerType").val("CT-LS");
                }

            }
        }
    });
}

function initSelectOrigForm() {
    if (userId == "admin") {
        $.ajax({
            url: basePath + "/unit/list.do?filter_EQI_type=9",
            cache: false,
            async: false,
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
    } else {
        $.ajax({
            url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + $("#search_origUnitId").val(),
            cache: false,
            async: false,
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
    }

}

function initSelectDestForm() {
    if (userId == "admin") {
        $.ajax({
            url: basePath + "/unit/list.do?filter_EQI_type=9",
            cache: false,
            async: false,
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
    } else {
        $.ajax({
            url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + curOwnerId,
            cache: false,
            async: false,
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
                // $("#search_busnissId").trigger('chosen:updated');
            }
            if (defaultSaleStaffId != "" && defaultSaleStaffId != undefined) {
                $("#search_busnissId").val(defaultSaleStaffId);
            }
        }
    });
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
            "<button id='SODtl_wareHouseOut' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='wareHouseInOut(" + "\"out\"" + ")'>" +
            "    <i class='ace-icon fa fa-sign-out'></i>" +
            "    <span class='bigger-110'>出库</span>" +
            "</button>" +
            "<button id='SODtl_wareHouseIn' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='wareHouseInOut(" + "\"in\"" + ")'>" +
            "    <i class='ace-icon fa fa-sign-in'></i>" +
            "    <span class='bigger-110'>入库</span>" +
            "</button>" +
            "<button id='SODtl_doPrint' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='doPrint(billNo)'>" +
            "    <i class='ace-icon fa fa-print'></i>" +
            "    <span class='bigger-110'>打印</span>" +
            "</button>"
        );
        $("#search_guest_button").removeAttr("disabled");
    }
    //if (pageType === "edit" && $("#returnCode").val() === "") {
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
            "<button id='SODtl_wareHouseOut' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='wareHouseInOut(" + "\"out\"" + ")'>" +
            "    <i class='ace-icon fa fa-sign-out'></i>" +
            "    <span class='bigger-110'>出库</span>" +
            "</button>" +
            "<button id='SODtl_wareHouseIn_noOutHouse' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='wareHouseInOut(" + "\"in\"" + ")'>" +
            "    <i class='ace-icon fa fa-sign-in'></i>" +
            "    <span class='bigger-110'>入库</span>" +
            "</button>" +
            "<button id='SODtl_wareHouseIn' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='edit_wareHouseIn()'>" +
            "    <i class='ace-icon fa fa-sign-in'></i>" +
            "    <span class='bigger-110'>入库</span>" +
            "</button>" +
            "<button id='SODtl_doPrint' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='doPrint(billNo)'>" +
            "    <i class='ace-icon fa fa-print'></i>" +
            "    <span class='bigger-110'>打印</span>" +
            "</button>"
        );
        if (saleOrderReturn_status !== "0" && userId !== "admin") {
            $("#search_guest_button").attr({"disabled": "disabled"});
            $("#SODtl_save").attr({"disabled": "disabled"});
            $("#SODtl_addUniqCode").attr({"disabled": "disabled"});
        }
        if ($("#search_origId").val() && $("#search_origId").val() !== null && $("#search_origId").val() !== "") {
            $("#SODtl_wareHouseOut").removeAttr("disabled");
            $("#SODtl_wareHouseIn").show();
            $("#SODtl_wareHouseIn_noOutHouse").hide();
        } else {
            $("#SODtl_wareHouseOut").attr({"disabled": "disabled"});
            $("#SODtl_wareHouseIn").hide();
            $("#SODtl_wareHouseIn_noOutHouse").show();
        }
        if (srcBillNo != "") {
            $("#SODtl_addUniqCode").hide();
            $("#SODtl_wareHouseIn_noOutHouse").hide();
            $("#SODtl_wareHouseOut").hide();
            $("#SODtl_wareHouseIn").hide();
        }
    }
}

function initGrid() {
    $("#addDetailgrid").jqGrid({
        height: "auto",
        datatype: "json",
        url: basePath + "/logistics/saleOrderReturn/returnDetails.do?billNo=" + billNo,
        colModel: [
            {name: 'id', label: 'id', hidden: true},
            {name: 'billId', label: 'billId', hidden: true},
            {name: 'billNo', label: 'billNo', hidden: true},
            {name: 'status', hidden: true},
            {name: 'inStatus', hidden: true},
            {name: 'outStatus', hidden: true},
            {
                name: "operation", label: '操作', width: 30, align: 'center', sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=saveItem('" + options.rowId + ")'><i class='ace-icon ace-icon fa fa-save' title='保存'></i></a>"
                        + "<a href='javascript:void(0);' style='margin-left: 20px'  onclick=deleteRow('" + options.rowId + "')><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";
                }
            },
            {
                label: '状态', width: 20, hidden: true, sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    return '<i class="ace-icon fa fa-tasks blue"></i>'
                }
            },
            {
                name: 'inStatusImg', label: '入库状态', width: 30, align: 'center', sortable: false,
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
            {name: 'styleName', label: '款式', width: 40},
            {name: 'colorId', label: '色号', width: 40},
            {name: 'colorName', label: '颜色', width: 30},
            {name: 'sizeId', label: '尺码', width: 30},
            {name: 'sizeName', label: '尺寸', width: 40},
            {
                name: 'qty', label: '数量', width: 40, editable: true,
                editrules: {
                    number: true,
                    minValue: 1
                }
            },
            {name: 'outQty', label: '已出库数量', width: 40},
            {name: 'inQty', label: '已入库数量', width: 40},
            {name: 'sku', label: 'sku', width: 50},
            {
                name: 'price', label: '销售价格', width: 40,
                formatter: function (cellValue, options, rowObject) {
                    var price = parseFloat(cellValue).toFixed(2);
                    return price;
                }
            },
            {
                name: 'totPrice', label: '销售金额', width: 40,
                formatter: function (cellValue, options, rowObject) {
                    var totPrice = parseFloat(cellValue).toFixed(2);
                    return totPrice;
                }
            },
            {name: 'discount', label: '折扣', width: 40, editable: true,},
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
        multiselect: false,
        shrinkToFit: true,
        pager: '#addDetailgrid-pager',
        sortname: 'id',
        sortorder: "asc",
        footerrow: true,
        cellEdit: true,
        cellsubmit: 'clientArray',
        // onSelectRow: function (rowId, status) {/logistics/monthAccountStatement
        //     if (pageType != "edit") {
        //         if (editDtailRowId != null) {
        //             saveItem(editDtailRowId);
        //         }
        //         editDtailRowId = rowId;
        //         $('#addDetailgrid').editRow(rowId);
        //     }
        // },

        afterEditCell: function (rowid, celname, value, iRow, iCol) {
            addDetailgridiRow = iRow;
            addDetailgridiCol = iCol;
        },
        afterSaveCell: function (rowid, cellname, value, iRow, iCol) {
            if (cellname === "discount") {
                var var_actPrice = Math.round(value * $('#addDetailgrid').getCell(rowid, "price")) / 100;
                var var_totActPrice = -Math.abs(Math.round(var_actPrice * $('#addDetailgrid').getCell(rowid, "qty") * 100) / 100);
                $('#addDetailgrid').setCell(rowid, "actPrice", var_actPrice);
                $('#addDetailgrid').setCell(rowid, "totActPrice", var_totActPrice);
            } else if (cellname === "actPrice") {
                var var_discount = Math.round(value / $('#addDetailgrid').getCell(rowid, "price") * 100);
                var var_totActPrice = -Math.abs(Math.round(value * $('#addDetailgrid').getCell(rowid, "qty") * 100) / 100);
                $('#addDetailgrid').setCell(rowid, "discount", var_discount);
                $('#addDetailgrid').setCell(rowid, "totActPrice", var_totActPrice);
            }
            else if (cellname === "qty") {
                $('#addDetailgrid').setCell(rowid, "totPrice", -Math.abs(Math.round($('#addDetailgrid').getCell(rowid, "price") * value * 100) / 100));
                $('#addDetailgrid').setCell(rowid, "totActPrice", -Math.abs(Math.round($('#addDetailgrid').getCell(rowid, "actPrice") * value * 100) / 100));
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

    if (pageType === "edit" && saleOrderReturn_status !== "0" || srcBillNo !== "") {
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
    var sum_inQty = $("#addDetailgrid").getCol('inQty', false, 'sum');
    var sum_totPrice = $("#addDetailgrid").getCol('totPrice', false, 'sum');
    var sum_totActPrice = Math.round($("#addDetailgrid").getCol('totActPrice', false, 'sum'));
    $("#search_actPrice").val(sum_totActPrice);
    $("#addDetailgrid").footerData('set', {
        styleId: "合计",
        qty: sum_qty,
        outQty: sum_outQty,
        inQty: sum_inQty,
        totPrice: -Math.abs(sum_totPrice),
        totActPrice: -Math.abs(sum_totActPrice)
    });
}
function addDetail() {
    var ct = $("#search_customerType").val();
    if (ct && ct != null) {
        $("#modal-addDetail-table").modal('show').on('hidden.bs.modal', function () {
            $("#StyleSearchForm").resetForm();
            $("#stylegrid").clearGridData();
            $("#color_size_grid").clearGridData();
        });
        initStyleGridColumn(ct);
    } else {
        bootbox.alert("请选择客户！");
    }
}

function addProductInfo() {
    if (addDetailgridiRow != null && addDetailgridiCol != null) {
        $("#addDetailgrid").saveCell(addDetailgridiRow, addDetailgridiCol);
        addDetailgridiRow = null;
        addDetailgridiCol = null;
    }
    var addProductInfo = [];
    if (editcolosizeRow != null) {
        $('#color_size_grid').saveRow(editcolosizeRow, false, 'clientArray');
    }
    var ct = $("#search_customerType").val();
    var styleRow = $("#stylegrid").getRowData($("#stylegrid").jqGrid("getGridParam", "selrow"));
    $.each($("#color_size_grid").getDataIDs(), function (index, value) {
        var productInfo = $("#color_size_grid").getRowData(value);
        if (productInfo.qty > 0) {

            if (ct == "CT-AT") {//省代价格
                productInfo.price = styleRow.puPrice;
            } else if (ct == "CT-ST") {//门店价格
                productInfo.price = styleRow.wsPrice;
            } else if (ct == "CT-LS") {//吊牌价格
                productInfo.price = styleRow.price;
            }

            productInfo.outQty = 0;
            productInfo.inQty = 0;
            productInfo.status = 0;
            productInfo.inStatus = 0;
            productInfo.outStatus = 0;
            if ($("#search_discount").val() && $("#search_discount").val() !== null) {
                productInfo.discount = $("#search_discount").val();
            } else {
                productInfo.discount = 100;
            }
            productInfo.actPrice = Math.round(productInfo.price * productInfo.discount) / 100;
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
                dtlRow.totPrice = -Math.abs(dtlRow.qty * dtlRow.price);
                dtlRow.totActPrice = -Math.abs(dtlRow.qty * dtlRow.actPrice);
                if (dtlRow.id) {
                    $("#addDetailgrid").setRowData(dtlRow.id, dtlRow);
                } else {
                    $("#addDetailgrid").setRowData(dtlndex, dtlRow);
                }
                isAdd = false;

            }
        });
        if (isAdd) {
            $("#addDetailgrid").addRowData($("#addDetailgrid").getDataIDs().length, value);
        }
    });
    $("#modal-addDetail-table").modal('hide');
    setFooterData();
}

function save() {
    $("#search_customerType").removeAttr('disabled');
    $("#search_origId").removeAttr('disabled');
    $("#search_destId").removeAttr('disabled');
    $("#search_busnissId").removeAttr('disabled');

    if ($("#search_origId").val() == $("#search_destId").val()) {
        bootbox.alert("不能在相同的单位之间做销售退货");
        return;
    }

    $("#editForm").data('bootstrapValidator').destroy();
    $('#editForm').data('bootstrapValidator', null);
    initEditFormValid();
    $('#editForm').data('bootstrapValidator').validate();
    if (!$('#editForm').data('bootstrapValidator').isValid()) {
        return;
    }

    if ($("#addDetailgrid").getDataIDs().length == 0) {
        bootbox.alert("请添加退货商品");
        return;
    }

    if (addDetailgridiRow != null && addDetailgridiCol != null) {
        $("#addDetailgrid").saveCell(addDetailgridiRow, addDetailgridiCol);
        addDetailgridiRow = null;
        addDetailgridiCol = null;
    }

    var purchaseReturnBill = JSON.stringify(array2obj($("#editForm").serializeArray()));
    var dtlArray = [];
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        dtlArray.push(rowData);
    });
    showWaitingPage();
    $.ajax({
        dataType: "json",
        async: false,
        url: basePath + "/logistics/saleOrderReturn/save.do",
        data: {
            'bill': purchaseReturnBill,
            'strDtlList': JSON.stringify(dtlArray),
            userId: userId
        },
        type: "POST",
        success: function (msg) {
            hideWaitingPage();
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#search_billNo").val(msg.result);
                billNo = msg.result;

                $("#addDetailgrid").jqGrid('setGridParam', {
                    page: 1,
                    url: basePath + "/logistics/saleOrderReturn/returnDetails.do?billNo=" + msg.result,
                });
                $("#addDetailgrid").trigger("reloadGrid");
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });

}

function saveItem(rowId) {
    $('#addDetailgrid').saveRow(rowId);
    var value = $('#addDetailgrid').getRowData(rowId);
    value.totPrice = -Math.abs(value.qty * value.price);
    value.totActPrice = -Math.abs(value.qty * value.actPrice);
    $("#addDetailgrid").setRowData(rowId, value);
    setFooterData();

}

function deleteRow(rowId) {
    var value = $('#addDetailgrid').getRowData(rowId);
    $("#addDetailgrid").jqGrid("delRowData", rowId);
    setFooterData();
    var totActPrice = value.totActPrice;

    saveother(totActPrice);
}
function saveother(totActPrice) {

    $("#search_customerType").removeAttr('disabled');
    $("#search_origId").removeAttr('disabled');
    $("#search_destId").removeAttr('disabled');
    $("#search_busnissId").removeAttr('disabled');

    if ($("#search_origId").val() == $("#search_destId").val()) {
        bootbox.alert("不能在相同的单位之间做销售退货");
        return;
    }

    $("#editForm").data('bootstrapValidator').destroy();
    $('#editForm').data('bootstrapValidator', null);
    initEditFormValid();
    $('#editForm').data('bootstrapValidator').validate();
    if (!$('#editForm').data('bootstrapValidator').isValid()) {
        return;
    }

    /*if ($("#addDetailgrid").getDataIDs().length == 0) {
     bootbox.alert("请添加退货商品");
     return;
     }*/

    if (addDetailgridiRow != null && addDetailgridiCol != null) {
        $("#addDetailgrid").saveCell(addDetailgridiRow, addDetailgridiCol);
        addDetailgridiRow = null;
        addDetailgridiCol = null;
    }
//实收金额的计算
    var payPrice = $("#search_payPrice").val();
    if (parseFloat(payPrice) < 0) {
        var summun = parseFloat(payPrice) - parseFloat(totActPrice);
        if (summun < 0) {
            $("#search_payPrice").val(summun.toFixed(2));
        }
    }
    var purchaseReturnBill = JSON.stringify(array2obj($("#editForm").serializeArray()));
    var dtlArray = [];
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        dtlArray.push(rowData);
    });
    showWaitingPage();
    $.ajax({
        dataType: "json",
        // async: false,
        url: basePath + "/logistics/saleOrderReturn/save.do",
        data: {
            'bill': purchaseReturnBill,
            'strDtlList': JSON.stringify(dtlArray),
            userId: userId
        },
        type: "POST",
        success: function (msg) {
            hideWaitingPage();
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#search_billNo").val(msg.result);
                billNo = msg.result;

                $("#addDetailgrid").jqGrid('setGridParam', {
                    page: 1,
                    url: basePath + "/logistics/saleOrderReturn/returnDetails.do?billNo=" + msg.result,
                });
                $("#addDetailgrid").trigger("reloadGrid");
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
                        message: "供应商不能为空"
                    }
                }
            },
            destId: {
                validators: {
                    notEmpty: {
                        message: "收货仓库不能为空"
                    }
                }
            },
            customerId: {
                validators: {
                    notEmpty: {
                        message: '客户不能为空'
                    }
                }
            },
            billType: {
                validators: {
                    notEmpty: {
                        message: "请填写退货类型"
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
            discount: {
                validators: {
                    numeric: {
                        message: '折扣只能只能为0-100之间的数字'
                    },
                    callback: {
                        message: '折扣只能只能为0-100之间的数字',
                        callback: function (value, validator) {
                            if (parseInt(value) < 0 || parseInt(value) > 100) {
                                return false;
                            }
                            return true;
                        }
                    }
                }
            },
            busnissId: {
                validators: {
                    callback: {
                        message: '请选择销售员',
                        callback: function (value, validator) {
                            if (value === "") {
                                return false;
                            }
                            return true;
                        }
                    }
                }
            }
        }
    });

}
//扫码
function addUniqCode() {
    var ct = $("#search_customerType").val();
    if (ct && ct != null) {

        inOntWareHouseValid = 'addPage_scanUniqueCode';
        billNo = $("#search_billNo").val();
        if ($("#search_origId").val() && $("#search_origId").val() !== null) {
            taskType = 0; //出库
            wareHouse = $("#search_origId").val();
        } else if (($("#search_destId").val() && $("#search_destId").val() !== null)) {
            taskType = -1; //没有出库仓库时，直接入库。入库类型等于 -1 表明校验时不需要该参数
            wareHouse = $("#search_destId").val();
        } else {
            $.gritter.add({
                text: "请选择入库仓库",
                class_name: 'gritter-success  gritter-light'
            });
            return
        }

        $("#dialog_buttonGroup").html("" +
            "<button type='button' id='so_savecode_button' class='btn btn-primary' onclick='addProductsOnCode()'>保存</button>"
        );
        $("#add-uniqCode-dialog").modal('show').on('hidden.bs.modal', function () {
            $("#uniqueCodeGrid").clearGridData();
        });
        initUniqeCodeGridColumn(ct);
        $("#codeQty").text(0);
    } else {
        bootbox.alert("请选择客户！");
    }
    allCodes = "";
}

function addProductsOnCode() {
    debugger;
    if (!$('#so_savecode_button').prop('disabled')) {
        $("#so_savecode_button").attr({"disabled": "disabled"});
        var productListInfo = [];
        var ct = $("#search_customerType").val();
        $.each($("#uniqueCodeGrid").getDataIDs(), function (index, value) {
            var productInfo = $("#uniqueCodeGrid").getRowData(value);
            productInfo.qty = 1;
            if (ct == "CT-AT") {//省代价格
                productInfo.price = productInfo.puPrice;
            } else if (ct == "CT-ST") {//门店价格
                productInfo.price = productInfo.wsPrice;
            } else if (ct == "CT-LS") {//吊牌价格
                productInfo.price = productInfo.price;
            }
            productInfo.outQty = 0;
            productInfo.inQty = 0;
            productInfo.status = 0;
            productInfo.inStatus = 0;
            productInfo.outStatus = 0;
            if ($("#search_discount").val() && $("#search_discount").val() !== null) {
                productInfo.discount = $("#search_discount").val();
            } else {
                productInfo.discount = 100;
            }
            productInfo.uniqueCodes = productInfo.code;
            productInfo.totPrice = -Math.abs(productInfo.price);
            productInfo.actPrice = Math.round(productInfo.price * productInfo.discount) / 100;
            productInfo.totActPrice = -Math.abs(productInfo.actPrice);
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
                    dtlRow.totPrice = -Math.abs(dtlRow.qty * dtlRow.price);
                    dtlRow.totActPrice = -Math.abs(dtlRow.qty * dtlRow.actPrice);
                    alltotActPrice += -Math.abs(dtlRow.qty * dtlRow.actPrice);
                    dtlRow.uniqueCodes = dtlRow.uniqueCodes + "," + value.code;
                    if (dtlRow.id) {
                        $("#addDetailgrid").setRowData(dtlRow.id, dtlRow);
                    } else {
                        $("#addDetailgrid").setRowData(dtlIndex, dtlRow);
                    }
                    isAdd = false;
                }
                console.log(dtlRow.uniqueCodes);
            });
            if (isAdd) {
                $("#addDetailgrid").addRowData($("#addDetailgrid").getDataIDs().length, value);
            }
        });
        $("#so_savecode_button").removeAttr("disabled");
        $("#add-uniqCode-dialog").modal('hide');
        setFooterData();
        saveother(0 - alltotActPrice);
    }
}

// @param: type     出入库类型，"in"入库；"out"出库
function wareHouseInOut(type) {
    showWaitingPage();
    var billNo = $("#search_billNo").val();
    if (type === "in") {
        $("#SODtl_wareHouseIn_noOutHouse").attr({"disabled": "disabled"});
    } else if (type === "out") {
        $("#SODtl_wareHouseOut").attr({"disabled": "disabled"});
    }

    if (billNo && billNo != null) {
        if (inOutStockCheck(type)) {
            hideWaitingPage();
            return;
        }
        var url_ajax;
        var inOutString;
        if (type === "out") {
            url_ajax = basePath + "/logistics/saleOrderReturn/convertOut.do";
            inOutString = "出";
            taskType = 0;
            wareHouse = $("#search_origId").val();
        } else {
            url_ajax = basePath + "/logistics/saleOrderReturn/convertIn.do";
            inOutString = "入";
            taskType = -1;
            wareHouse = $("#search_destId").val();
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
            var codes = rowData.uniqueCodes.split(",");
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
        });
        if (epcArray.length === 0) {
            $.gritter.add({
                text: "没有可以直接" + inOutString + "库的商品",
                class_name: 'gritter-success  gritter-light'
            });
            if (pageType === "edit") {
                if (type === "out") {
                    edit_wareHouseOut();
                } else {
                    edit_wareHouseIn_noOutHouse();
                }
            }
            hideWaitingPage();
            if (type === "in") {
                $("#SODtl_wareHouseIn_noOutHouse").removeAttr("disabled");
            } else if (type === "out") {
                $("#SODtl_wareHouseOut").removeAttr("disabled");
            }
            return;
        }

        var dtlArray = [];
        $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
            var rowData = $("#addDetailgrid").getRowData(value);
            dtlArray.push(rowData);
        });

        $.ajax({
            // async: false,
            dataType: "json",
            url: url_ajax,
            data: {
                billNo: billNo,
                strEpcList: JSON.stringify(epcArray),
                strDtlList: JSON.stringify(dtlArray),
                userId: userId
            },
            type: "POST",
            success: function (msg) {
                hideWaitingPage();
                if (type === "in") {
                    $("#SODtl_wareHouseIn_noOutHouse").removeAttr("disabled");
                } else if (type === "out") {
                    $("#SODtl_wareHouseOut").removeAttr("disabled");
                }
                if (msg.success) {
                    $.gritter.add({
                        text: msg.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                    $("#modal-addEpc-table").modal('hide');

                    var sum_qty = parseInt($("#addDetailgrid").footerData('get').qty);        //reload前总数量
                    $("#addDetailgrid").jqGrid('setGridParam', {
                        page: 1,
                        url: basePath + "/logistics/saleOrderReturn/returnDetails.do?billNo=" + billNo
                    });
                    $("#addDetailgrid").trigger("reloadGrid");

                    var diff_qty = sum_qty - epcArray.length;
                    if (pageType === "edit") {
                        // $("#addDetailgrid").setColProp('qty',{editable:{value:"True:False"}});
                        // $("#addDetailgrid-pager_left").hide();
                        $("#search_guest_button").attr({"disabled": "disabled"});
                        $("#SODtl_save").attr({"disabled": "disabled"});
                        $("#SODtl_addUniqCode").attr({"disabled": "disabled"});
                        $("#search_origId").attr('disabled', true);
                        $("#search_destId").attr('disabled', true);
                        $("#search_billDate").attr('readOnly', true);
                        $("#search_busnissId").attr('disabled', true);
                        if (sum_qty > epcArray.length) {
                            $.gritter.add({
                                text: "已" + inOutString + "库数量为：" + epcArray.length + "；剩余数量为：" + diff_qty + "，其余商品请扫码" + inOutString + "库",
                                class_name: 'gritter-success  gritter-light'
                            });
                            if (type === "out") {
                                edit_wareHouseOut();
                            } else {
                                edit_wareHouseIn_noOutHouse();
                            }
                        } else if (sum_qty === epcArray.length) {
                            $.gritter.add({
                                text: "共" + epcArray.length + "件商品，已全部" + inOutString + "库",
                                class_name: 'gritter-success  gritter-light'
                            });
                        }
                    } else if (pageType === "add") {
                        var alertMessage;
                        if (sum_qty > epcArray.length) {
                            alertMessage = "已" + inOutString + "库数量为：" + epcArray.length + "；剩余数量为：" + diff_qty + "，其余商品请扫码" + inOutString + "库"
                        } else if (sum_qty === epcArray.length) {
                            alertMessage = "共" + epcArray.length + "件商品，已全部" + inOutString + "库";
                        }
                        bootbox.alert({
                            buttons: {ok: {label: '确定'}},
                            message: alertMessage,
                            callback: function () {
                                quitback();

                            },
                        });
                    }
                } else {
                    bootbox.alert(msg.msg);
                }
            }
        });
    } else {
        hideWaitingPage();
        if (type === "in") {
            $("#SODtl_wareHouseIn_noOutHouse").removeAttr("disabled");
        } else if (type === "out") {
            $("#SODtl_wareHouseOut").removeAttr("disabled");
        }
        bootbox.alert("请先保存当前单据");
    }
}
function quitback() {
    $.ajax({
        url: basePath + "/logistics/saleOrderReturn/quit.do?billNo=" + billNo,
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {

            if (textStatus == "success") {
                $.gritter.add({
                    text: billNo + "可以编辑",
                    class_name: 'gritter-success  gritter-light'
                });
                window.location.href = basePath + "/logistics/saleOrderReturn/index.do";
            }

        }
    });
}

function edit_wareHouseOut() {
    skuQty = {};
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        skuQty[rowData.sku] = rowData.outQty;
    });
    inOntWareHouseValid = 'wareHouseOut_valid';
    var ct = $("#search_customerType").val();

    $("#dialog_buttonGroup").html("" +
        "<button type='button' id='WareHouseOut_dialog_buttonGroup' class='btn btn-primary' onclick='confirmWareHouseOut()'>确认出库</button>"
    );
    $("#add-uniqCode-dialog").modal('show').on('hidden.bs.modal', function () {
        $("#uniqueCodeGrid").clearGridData();
    });
    initUniqeCodeGridColumn(ct);
    $("#codeQty").text(0);
    allCodes = "";
}

function confirmWareHouseOut() {
    showWaitingPage();
    $("#WareHouseOut_dialog_buttonGroup").attr({"disabled": "disabled"});
    var billNo = $("#search_billNo").val();
    var epcArray = [];
    $.each($("#uniqueCodeGrid").getDataIDs(), function (index, value) {
        var rowData = $("#uniqueCodeGrid").getRowData(value);
        epcArray.push(rowData);
    });
    if (epcArray.length == 0) {
        bootbox.alert("请添加唯一码!");
        hideWaitingPage();
        $("#WareHouseIn_dialog_buttonGroup").removeAttr("disabled");
        return;
    }

    $.each(epcArray, function (index, value) {
        $.each($("#addDetailgrid").getDataIDs(), function (dtlIndex, dtlValue) {
            var dtlRow = $("#addDetailgrid").getRowData(dtlValue);
            if (value.sku === dtlRow.sku) {
                if (dtlRow.uniqueCodes.indexOf(value.code) !== -1) {
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
        // async: false,
        url: basePath + "/logistics/saleOrderReturn/convertOut.do",
        data: {
            billNo: billNo,
            strEpcList: JSON.stringify(epcArray),
            strDtlList: JSON.stringify(dtlArray),
            userId: userId
        },
        type: "POST",
        success: function (msg) {
            hideWaitingPage();
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

function edit_wareHouseIn_noOutHouse() {
    skuQty = {};
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        skuQty[rowData.sku] = rowData.outQty;
    });
    inOntWareHouseValid = 'wareHouseOut_valid';
    var ct = $("#search_customerType").val();

    $("#dialog_buttonGroup").html("" +
        "<button type='button' id='WareHouseIn_dialog_buttonGroup'class='btn btn-primary' onclick='confirmWareHouseIn()'>确认入库</button>"
    );
    $("#add-uniqCode-dialog").modal('show').on('hidden.bs.modal', function () {
        $("#uniqueCodeGrid").clearGridData();
    });
    initUniqeCodeGridColumn(ct);
    $("#codeQty").text(0);
    allCodes = "";

}

// @param: type     出入库类型，"in"入库；"out"出库
function inOutStockCheck(type) {
    var sum_qty = parseInt($("#addDetailgrid").footerData('get').qty);
    var sum_outQty = parseInt($("#addDetailgrid").footerData('get').outQty);
    var sum_inQty = parseInt($("#addDetailgrid").footerData('get').inQty);
    if (type === "in") {
        if (sum_qty <= sum_inQty) {
            $.gritter.add({
                text: '已全部入库',
                class_name: 'gritter-success  gritter-light'
            });
            return true;
        }
    } else {
        if (sum_qty <= sum_outQty) {
            $.gritter.add({
                text: '已全部出库',
                class_name: 'gritter-success  gritter-light'
            });
            return true;
        }
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

function edit_wareHouseIn() {
    skuQty = {};
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        skuQty[rowData.sku] = rowData.inQty;
    });

    inOntWareHouseValid = 'wareHouseIn_valid';
    taskType = 1;
    var destId = $("#search_destId").val();
    wareHouse = destId;
    var ct = $("#search_customerType").val();
    if (destId && destId !== null) {
        $("#dialog_buttonGroup").html("" +
            "<button type='button' id='WareHouseIn_dialog_buttonGroup' class='btn btn-primary' onclick='confirmWareHouseIn()'>确认入库</button>"
        );
        $("#add-uniqCode-dialog").modal('show').on('hidden.bs.modal', function () {
            $("#uniqueCodeGrid").clearGridData();
        });
        initUniqeCodeGridColumn(ct);
        $("#codeQty").text(0);
    } else {
        bootbox.alert("客户仓库不能为空！");
    }
    allCodes = "";
}

function confirmWareHouseIn() {
    showWaitingPage();
    var billNo = $("#search_billNo").val();

    $("#WareHouseIn_dialog_buttonGroup").attr({"disabled": "disabled"});


    var epcArray = [];
    $.each($("#uniqueCodeGrid").getDataIDs(), function (index, value) {
        var rowData = $("#uniqueCodeGrid").getRowData(value);
        epcArray.push(rowData);
    });
    if (epcArray.length === 0) {
        bootbox.alert("请添加唯一码!");
        hideWaitingPage();
        $("#WareHouseIn_dialog_buttonGroup").removeAttr("disabled");
        return;
    }
    var dtlArray = [];
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        dtlArray.push(rowData);
    });

    $.ajax({
        dataType: "json",
        // async: false,
        url: basePath + "/logistics/saleOrderReturn/convertIn.do",
        data: {
            billNo: billNo,
            strEpcList: JSON.stringify(epcArray),
            strDtlList: JSON.stringify(dtlArray),
            userId: userId
        },
        type: "POST",
        success: function (msg) {
            hideWaitingPage();
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
var dialogOpenPage;
function openSearchGuestDialog() {
    dialogOpenPage = "saleOrderReturn";
    $("#modal_guest_search_table").modal('show').on('shown.bs.modal', function () {
        initGuestSelect_Grid();
    });
    $("#searchGuestDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='confirm_selected_GuestId_saleReturn()'>确认</button>"
    );
}

function input_keydown() {
    $("#search_discount").keydown(function (event) {
        if (event.keyCode == 13) {
            setDiscount();
        }
    })
}
function search_discount_onblur() {
    setDiscount();
}
//将整单折扣设置到明细中
function setDiscount() {

    if (addDetailgridiRow != null && addDetailgridiCol != null) {
        $("#addDetailgrid").saveCell(addDetailgridiRow, addDetailgridiCol);
        addDetailgridiRow = null;
        addDetailgridiCol = null;
    }
    var discount = $("#search_discount").val();
    if (discount && discount != null && discount != "") {
        $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
            $('#addDetailgrid').setCell(value, "discount", discount);
            var var_actPrice = Math.round(discount * $('#addDetailgrid').getCell(value, "price")) / 100;
            var var_totActPrice = -Math.abs(Math.round(var_actPrice * $('#addDetailgrid').getCell(value, "qty") * 100) / 100);
            $('#addDetailgrid').setCell(value, "actPrice", var_actPrice);
            $('#addDetailgrid').setCell(value, "totActPrice", var_totActPrice);
        });
    }
    setFooterData();
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
        url: basePath + "/sys/print/findAll.do",
        type: "POST",
        success: function (msg) {
            /* /!* if (msg.success) {

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

             } else {*!/
             if (msg.success) {
             /!*
             var addcont = "";
             var ishave=false;
             for (var i = 0; i < msg.result.length; i++) {
             if(msg.result[i].saveownerid==curOwnerId){
             /!* addcont += "<div class='form-group' onclick=set('" + msg.result[i].id + "') title='" + msg.result[i].name + "'>" +
             "<button class='btn btn-info'>" +
             "<i class='cae-icon fa fa-refresh'></i>" +
             "<span class='bigger-10'>套打" + msg.result[i].name + "</span>" +
             "</button>" +
             "</div>"*!/
             ishave=true;
             break;
             }
             }

             if(ishave==true){
             for (var i = 0; i < msg.result.length; i++) {
             if(msg.result[i].saveownerid==curOwnerId&&billNo.indexOf(msg.result[i].type) >= 0){
             addcont += "<div class='form-group' onclick=set('" + msg.result[i].id + "') title='" + msg.result[i].name + "'>" +
             "<button class='btn btn-info'>" +
             "<i class='cae-icon fa fa-refresh'></i>" +
             "<span class='bigger-10'>套打" + msg.result[i].name + "</span>" +
             "</button>" +
             "</div>"

             }
             }
             }
             if(ishave==false){
             for (var i = 0; i < msg.result.length; i++) {
             if(msg.result[i].saveownerid==undefined&&billNo.indexOf(msg.result[i].type) >= 0){
             addcont += "<div class='form-group' onclick=set('" + msg.result[i].id + "') title='" + msg.result[i].name + "'>" +
             "<button class='btn btn-info'>" +
             "<i class='cae-icon fa fa-refresh'></i>" +
             "<span class='bigger-10'>套打" + msg.result[i].name + "</span>" +
             "</button>" +
             "</div>"

             }
             }
             }*!/


             $("#addbutton").html(addcont);

             } else {
             bootbox.alert(msg.msg);
             }*/
            if (msg.success) {
                var addcont = "";
                //var ishave = false;
                for (var i = 0; i < msg.result.length; i++) {

                    if (billNo.indexOf(msg.result[i].type) >= 0) {
                        if (roleid == "JMSJS" && msg.result[i].isFranchisee == "is") {
                            addcont += "<div class='form-group' onclick=set('" + msg.result[i].id + "') title='" + msg.result[i].name + "'>" +
                                "<button class='btn btn-info'>" +
                                "<i class='cae-icon fa fa-refresh'></i>" +
                                "<span class='bigger-10'>套打" + msg.result[i].name + "</span>" +
                                "</button>" +
                                "</div>"
                        }
                        if (roleid != "JMSJS" && msg.result[i].isFranchisee != "is") {
                            addcont += "<div class='form-group' onclick=set('" + msg.result[i].id + "') title='" + msg.result[i].name + "'>" +
                                "<button class='btn btn-info'>" +
                                "<i class='cae-icon fa fa-refresh'></i>" +
                                "<span class='bigger-10'>套打" + msg.result[i].name + "</span>" +
                                "</button>" +
                                "</div>"
                        }

                    }
                }

                /* for (var i = 0; i < msg.result.length; i++) {
                 if (msg.result[i].saveownerid == curOwnerId) {
                 /!* addcont += "<div class='form-group' onclick=set('" + msg.result[i].id + "') title='" + msg.result[i].name + "'>" +
                 "<button class='btn btn-info'>" +
                 "<i class='cae-icon fa fa-refresh'></i>" +
                 "<span class='bigger-10'>套打" + msg.result[i].name + "</span>" +
                 "</button>" +
                 "</div>"*!/
                 ishave = true;
                 break;
                 }
                 }
                 if (ishave == true) {
                 for (var i = 0; i < msg.result.length; i++) {
                 if (msg.result[i].saveownerid == curOwnerId && billNo.indexOf(msg.result[i].type) >= 0) {
                 addcont += "<div class='form-group' onclick=set('" + msg.result[i].id + "') title='" + msg.result[i].name + "'>" +
                 "<button class='btn btn-info'>" +
                 "<i class='cae-icon fa fa-refresh'></i>" +
                 "<span class='bigger-10'>套打" + msg.result[i].name + "</span>" +
                 "</button>" +
                 "</div>"

                 }
                 }
                 }
                 if (ishave == false) {
                 for (var i = 0; i < msg.result.length; i++) {
                 if (msg.result[i].saveownerid == undefined && billNo.indexOf(msg.result[i].type) >= 0) {
                 addcont += "<div class='form-group' onclick=set('" + msg.result[i].id + "') title='" + msg.result[i].name + "'>" +
                 "<button class='btn btn-info'>" +
                 "<i class='cae-icon fa fa-refresh'></i>" +
                 "<span class='bigger-10'>套打" + msg.result[i].name + "</span>" +
                 "</button>" +
                 "</div>"

                 }
                 }
                 }*/

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
                var alldiscount = 0;
                for (var a = 0; a < contDel.length; a++) {
                    var conts = contDel[a];
                    recordmessage += "<tr style='border-top:1px dashed black;padding-top:5px;'>" +
                        "<td align='left' style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + conts.sku + "</td>" +
                        "<td align='left'style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + conts.qty + "</td>" +
                        "<td style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + conts.price.toFixed(1) + "</td>" +
                        "<td style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + conts.actPrice.toFixed(1) + "</td>" +
                        "<td align='right' style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + (conts.actPrice * conts.qty).toFixed(2) + "</td>" +
                        "</tr>";

                    sum = sum + parseInt(conts.qty);
                    //allprice = allprice + parseFloat(conts.actPrice*conts.qty.toFixed(2));
                    alldiscount = alldiscount + parseFloat((conts.actPrice * conts.qty).toFixed(2));
                }
                alldiscount = alldiscount.toFixed(0);
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
function showCodesDetail(uniqueCodes) {

    $("#show-uniqueCode-list").modal('show');
    initUniqueCodeList(uniqueCodes);
    codeListReload(uniqueCodes);
}