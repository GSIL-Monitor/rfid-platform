var addDetailgridiRow;//存储iRow
var addDetailgridiCol;//存储iCol
var allCodes; //用于拼接所有添加过的唯一码，防止重复添加
var taskType; //用于判断出入库类型 1入库 0 出库
var wareHouse;
var inOntWareHouseValid; //用于判断在编辑BillDtl时出入库操作是否需要校验，使用哪种校验。
var skuQty = {};//保存每个SKU对应的出入库数量。
var issaleretrun = true;
var editDtailiRow = null;
var editDtailiCol = null;
var billNo;
$(function () {
    initGrid();
    initForm();
    keydown();
    input_keydown();
    initButtonGroup();
    initEditFormValid();
    if (billNo != "") {
        sessionStorage.setItem("billNoConsignment", billNo);
    }
    if (pageType === "add") {
        $("#search_origUnitId").val(defalutCustomerId);
        $("#search_origUnitName").val(defalutCustomerName);
        $("#search_discount").val(defalutCustomerdiscount);
        if (defaultSaleStaffId != "" && defaultSaleStaffId != undefined) {
            addUniqCode();
        }

    }
    $(".selectpicker").selectpicker('refresh');
});
// var editDtailRowId = null;
// var editDtailColumn = null;
var loadComplete = true, loadStatus = true;
var isPassed = true;

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
        if (returnStatus != "0" && userId != "admin") {
            $("#search_origId").attr('disabled', true);
            $("#search_destId").attr('disabled', true);
            $("#search_billDate").attr('readOnly', true);
            $("#search_busnissId").attr('disabled', true);
        }
        if (userId == "admin") {
            $("#search_guest_button").removeAttr("disabled");
            $("#search_origId").attr('disabled', true);
            $("#search_destId").attr('disabled', true);
        }
    } else if (pageType === "copyAdd") {
        $("#search_billNo").val("");
        $("#search_origId").val(saleOrderReturn_origId);
        $("#search_busnissId").val(saleOrderReturn_busnissId);
        $("#search_destId").val(saleOrderReturn_destId);
        $("#search_billDate").val(getToDay("yyyy-MM-dd"));
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
                if (defaultSaleStaffId != "" && defaultSaleStaffId != undefined) {
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

function initButtonGroup() {
    $("#addDetailgrid").clearGridData();
    var html =
        "<button id='CMDtl_save' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='save()'>" +
        "    <i class='ace-icon fa fa-search'></i>" +
        "    <span class='bigger-110'>保存</span>" +
        "</button>";
    if (pageType === "add") {
        html +=
            "<button id='CMDtl_addUniqCode' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='addUniqCode()'>" +
            "    <i class='ace-icon fa fa-undo'></i>" +
            "    <span class='bigger-110'>扫码</span>" +
            "</button>" +
            "<button id='CMDtl_wareHouseIn' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='confirmWareHouseIn()'>" +
            "    <i class='ace-icon fa fa-search'></i>" +
            "    <span class='bigger-110'>入库</span>" +
            "</button>";
        $("#search_guest_button").removeAttr("disabled");
    }
    if (pageType === "edit") {
        html +=
            "<button id='CMDtl_wareHouseSale' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='saleRetrunNo()'>" +
            "    <i class='ace-icon fa fa-search'></i>" +
            "    <span class='bigger-110'>扫描退货</span>" +
            "</button>" +
            "<button id='CMDtl_wareHouseokSale' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='saleRetrunNook()'>" +
            "    <i class='ace-icon fa fa-search'></i>" +
            "    <span class='bigger-110'>退货</span>" +
            "</button>" +
            "<button id='CMDtl_findRetrunno' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='findRetrunno()'>" +
            "    <i class='ace-icon fa fa-search'></i>" +
            "    <span class='bigger-110'>查找退单</span>" +
            "</button>";
        if ($("#search_status").val() != "2" && $("#search_status").val() != "3") {
            html += "<button id='CMDtl_wareHouseIn' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='confirmWareHouseIn()'>" +
                "    <i class='ace-icon fa fa-search'></i>" +
                "    <span class='bigger-110'>入库</span>" +
                "</button>" +
                "<button id='CMDtl_addUniqCode' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='addUniqCode()'>" +
                "    <i class='ace-icon fa fa-undo'></i>" +
                "    <span class='bigger-110'>扫码</span>" +
                "</button>";
        }
        if (userId != "admin") {
            $("#search_guest_button").attr({"disabled": "disabled"});
        }
    }
    $("#buttonGroup").html("" + html);
}
var beforsale = 0;
var readysale = 0;
var isfrist = true;
function initGrid() {
    $("#addDetailgrid").jqGrid({
        height: "auto",
        datatype: "json",
        url: basePath + "/logistics/Consignment/returnDetails.do?billNo=" + billNo,
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
                    if (rowObject.inStatus != 4 || rowObject.inStatus != 1) {
                        return "<a href='javascript:void(0);' onclick=saveItem('" + options.rowId + ")'><i class='ace-icon ace-icon fa fa-save' title='保存'></i></a>"
                            + "<a href='javascript:void(0);' style='margin-left: 20px' onclick=deleteRow('" + options.rowId + "')><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";
                    } else {
                        return "";
                    }
                    /*var html = '<a href="#" title="保存该行" onclick="saveItem(' + options.rowId + ')"><i class="ace-icon fa fa-save"></i></a>';
                     html += '&nbsp;&nbsp;&nbsp;<a href="#"  title="删除一行" onclick="deleteRow(' + options.rowId + ')"><i class="ace-icon fa fa-trash"></i></a>';*/
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
                        return '<i class="fa fa-sign-out blue" title="入库中"></i>';
                    } else {
                        return '';
                    }
                }
            },
            /*{
             name: 'outStatusImg', label: '出库状态', width: 30, align: 'center',
             formatter: function (cellValue, options, rowObject) {
             if (rowObject.outStatus == 0) {
             return '<i class="fa fa-tasks blue" title="订单状态"></i>';
             } else if (rowObject.outStatus == 2) {
             return '<i class="fa fa-sign-in blue" title="已出库"></i>';
             } else if (rowObject.outStatus == 3) {
             return '<i class="fa fa-sign-out blue" title="出库中"></i>';
             } else {
             return '';
             }
             }
             },*/
            {name: 'rType', label: '退货类型', hidden: true},
            {
                name: '',
                label: '退货类型',
                width: 40,
                hidden: true,
                editable: true,
                formatter: function (cellValue, options, rowObject) {
                    switch (cellValue) {
                        case "0":
                            return "缺损退货";
                        default:
                            return "报损退货";
                    }
                },
                editoptions: {
                    dataInit: function (e) {
                        $(e).kendoComboBox({
                            dataTextField: "name",
                            dataValueField: "id",
                            height: 200,
                            suggest: true,
                            change: function (e) {
                                if (this._initial != this._prev) {
                                    $('#addDetailgrid').saveRow(editDtailRowId);
                                    var value = $('#addDetailgrid').getRowData(editDtailRowId);
                                    value.inStockType = this.value();
                                    $("#addDetailgrid").setRowData(editDtailRowId, value);
                                }

                            },
                            dataSource: {
                                type: "jsonp",
                                transport: {
                                    read: basePath + "/sys/property/searchByType.do?type=C6"
                                }
                            }
                        });
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
                name: 'qty', label: '数量', editable: true, width: 40,
                editrules: {
                    number: true,
                    minValue: 1
                }
            },
            {name: 'sale', label: '已销售数量', width: 40},
            {name: 'outMonyQty', label: '退款数量', editable: true, width: 40},
            {name: 'outQty', label: '退货数量', width: 40},
            {name: 'inQty', label: '已入库数量', width: 40},
            {name: 'sku', label: 'sku', width: 50},
            {
                name: 'price', label: '寄售价格', width: 40,
                formatter: function (cellValue, options, rowObject) {
                    var price = parseFloat(cellValue).toFixed(2);
                    return price;
                }
            },
            {
                name: 'totPrice', label: '寄售金额', width: 40,
                formatter: function (cellValue, options, rowObject) {
                    var totPrice = parseFloat(cellValue).toFixed(2);
                    return totPrice;
                }
            },
            {name: 'discount', label: '折扣', width: 40, editable: true},
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
            },
            {name: 'beforeoutQty', hidden: true},
            {name: 'savehaveuniqueCodes', label: '唯一码', hidden: true},
            {name: 'savenohanveuniqueCodes', label: '唯一码', hidden: true},
            {name: 'readysale', label: '准备销售', hidden: true},
            {name:'stylePriceMap',label:'价格表',hidden:true}
        ],
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: -1,
        multiselect: false,
        shrinkToFit: true,
        pager: '#addDetailgrid-pager',
        sortname: 'id',
        sortorder: "desc",
        footerrow: true,
        cellEdit: true,
        cellsubmit: 'clientArray',
        beforeEditCell: function (rowid, celname, value, iRow, iCol) {
            if (isfrist) {
                beforsale = $('#addDetailgrid').getCell(rowid, "outMonyQty");
                readysale = $('#addDetailgrid').getCell(rowid, "readysale");
            }
            isfrist = false;

        },
        afterEditCell: function (rowid, celname, value, iRow, iCol) {

            editDtailiRow = iRow;
            editDtailiCol = iCol;
            issaleretrun = false;
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
            } else if (cellname === "qty") {
                $('#addDetailgrid').setCell(rowid, "totPrice", -Math.abs(Math.round($('#addDetailgrid').getCell(rowid, "price") * value * 100) / 100));
                $('#addDetailgrid').setCell(rowid, "totActPrice", -Math.abs(Math.round($('#addDetailgrid').getCell(rowid, "actPrice") * value * 100) / 100));
            } else if (cellname === "outMonyQty") {
                debugger;
                var isok = true;
                var outQty = $('#addDetailgrid').getCell(rowid, "outQty");
                /* var sale=$('#addDetailgrid').getCell(rowid, "sale");*/
                var inQty = $('#addDetailgrid').getCell(rowid, "inQty");
                if ((parseInt(outQty) + parseInt(value)) > parseInt(inQty)) {
                    isok = false;
                    $.gritter.add({
                        text: "已超过入库数量",
                        class_name: 'gritter-success  gritter-light'
                    });
                    $('#addDetailgrid').setCell(rowid, "outMonyQty", beforsale);
                    $('#addDetailgrid').setCell(rowid, "readysale", readysale);
                }
                if (parseInt(value) < beforsale) {
                    isok = false;
                    $.gritter.add({
                        text: "少于退款数量",
                        class_name: 'gritter-success  gritter-light'
                    });
                    $('#addDetailgrid').setCell(rowid, "outMonyQty", beforsale);
                    $('#addDetailgrid').setCell(rowid, "readysale", readysale);
                }
                //var sale=$('#addDetailgrid').getCell(rowid, "sale");
                if (isok) {
                    /*  alert(beforsale);*/
                    /* var readysale=$('#addDetailgrid').getCell(rowid, "readysale");*/
                    var sum = parseInt(readysale) + (value - beforsale);
                    $('#addDetailgrid').setCell(rowid, "readysale", sum);
                }


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

    $("#addDetailgrid-pager_center").html("");
}
function setFooterData() {
    var sum_qty = $("#addDetailgrid").getCol('qty', false, 'sum');
    var sum_sale = $("#addDetailgrid").getCol('sale', false, 'sum');
    var sum_outMonyQty = $("#addDetailgrid").getCol('outMonyQty', false, 'sum');
    var sum_outQty = $("#addDetailgrid").getCol('outQty', false, 'sum');
    var sum_inQty = $("#addDetailgrid").getCol('inQty', false, 'sum');
    var sum_totPrice = $("#addDetailgrid").getCol('totPrice', false, 'sum');
    var sum_totActPrice = Math.round($("#addDetailgrid").getCol('totActPrice', false, 'sum'));
    $("#addDetailgrid").footerData('set', {
        styleId: "合计",
        qty: sum_qty,
        sale: sum_sale,
        outMonyQty: sum_outMonyQty,
        outQty: sum_outQty,
        inQty: sum_inQty,
        totPrice: -Math.abs(sum_totPrice),
        totActPrice: -Math.abs(sum_totActPrice)
    });
    $("#search_actPrice").val(-Math.abs(sum_totActPrice));
}
function addDetail() {
    var ct = $("#search_customerType").val();
    if (ct && ct != null) {
        $("#modal-addDetail-table").modal('show').on('hidden.bs.modal', function () {
            $("#StyleSearchForm").resetForm();
            $("#stylegrid").clearGridData();
            $("#color_size_grid").clearGridData();
        });
    } else {
        bootbox.alert("客户类型不能为空！");
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
    cs.showProgressBar();
    $("#addDetailgrid").saveCell(editDtailiRow, editDtailiCol);
    $("#search_customerType").removeAttr('disabled');
    $("#search_origId").removeAttr('disabled');
    $("#search_destId").removeAttr('disabled');
    $("#search_busnissId").removeAttr('disabled');

    $("#editForm").data('bootstrapValidator').destroy();
    $('#editForm').data('bootstrapValidator', null);
    initEditFormValid();
    $('#editForm').data('bootstrapValidator').validate();
    if (!$('#editForm').data('bootstrapValidator').isValid()) {
        cs.closeProgressBar();
        return;
    }
    if ($("#addDetailgrid").getDataIDs().length == 0) {
        bootbox.alert("请添加退货商品");
        cs.closeProgressBar();
        return;
    }
    if ($("#search_origId").val() === $("#search_destId").val()) {
        bootbox.alert("相同店不能寄售");
        cs.closeProgressBar();
        return;
    }
    var consignmentBill = JSON.stringify(array2obj($("#editForm").serializeArray()));

    if (addDetailgridiRow != null && addDetailgridiCol != null) {
        $("#addDetailgrid").saveCell(addDetailgridiRow, addDetailgridiCol);
        addDetailgridiRow = null;
        addDetailgridiCol = null;
    }

    var dtlArray = [];
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        dtlArray.push(rowData);
    });
    $.ajax({
        dataType: "json",
        async:true,
        url: basePath + "/logistics/Consignment/save.do",
        data: {
            'bill': consignmentBill,
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
                billNo = msg.result;
                issaleretrun = true;
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });

}

function saveItem(rowId) {
    $('#addDetailgrid').saveRow(rowId);
    var value = $('#addDetailgrid').getRowData(rowId);
    value.totPrice = value.qty * value.price;
    value.totActPrice = -Math.abs(value.qty * value.actPrice);
    $("#addDetailgrid").setRowData(rowId, value);
    setFooterData();

}
function saveother(totActPrice) {
    cs.showProgressBar();
    $("#addDetailgrid").saveCell(editDtailiRow, editDtailiCol);
    $("#search_customerType").removeAttr('disabled');
    $("#search_origId").removeAttr('disabled');
    $("#search_destId").removeAttr('disabled');
    $("#search_busnissId").removeAttr('disabled');

    $("#editForm").data('bootstrapValidator').destroy();
    $('#editForm').data('bootstrapValidator', null);
    initEditFormValid();
    $('#editForm').data('bootstrapValidator').validate();
    if (!$('#editForm').data('bootstrapValidator').isValid()) {
        cs.closeProgressBar();
        return;
    }
    /* if ($("#addDetailgrid").getDataIDs().length == 0) {
     bootbox.alert("请添加退货商品");
     return;
     }*/
    if ($("#search_origId").val() === $("#search_destId").val()) {
        bootbox.alert("相同店不能寄售");
        cs.closeProgressBar();
        return;
    }
    //实收金额的计算
    var payPrice = $("#search_payPrice").val();
    if (parseFloat(payPrice) < 0) {
        var summun = parseFloat(payPrice) - parseFloat(totActPrice);
        if (summun < 0) {
            $("#search_payPrice").val(summun.toFixed(2));
        }
    }
    var consignmentBill = JSON.stringify(array2obj($("#editForm").serializeArray()));

    if (addDetailgridiRow != null && addDetailgridiCol != null) {
        $("#addDetailgrid").saveCell(addDetailgridiRow, addDetailgridiCol);
        addDetailgridiRow = null;
        addDetailgridiCol = null;
    }

    var dtlArray = [];
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        dtlArray.push(rowData);
    });
    $.ajax({
        dataType: "json",
        async:true,
        url: basePath + "/logistics/Consignment/save.do",
        data: {
            'bill': consignmentBill,
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
                billNo = msg.result;
                issaleretrun = true;
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}

function deleteRow(rowId) {
    var value = $('#addDetailgrid').getRowData(rowId);
    $("#addDetailgrid").jqGrid("delRowData", rowId);
    setFooterData();
    var totActPrice = value.totActPrice;
    debugger;
    saveother(totActPrice);
}

function isInteger(obj) {
    var num = Math.floor(obj);
    return num == obj;
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
    debugger;
    inOntWareHouseValid = 'addPage_scanUniqueCode';
    taskType = -1; //出库
    var destId = $("#search_destId").val();
    wareHouse = destId;
    billNo = $("#search_billNo").val();
    var ct = $("#search_customerType").val();
    if (ct && ct != null) {
        $("#dialog_buttonGroup").html("" +
            "<button type='button' id='so_savecode_button' class='btn btn-primary' onclick='addProductsOnCode()'>保存</button>"
        );
        $("#add-uniqCode-dialog").modal('show').on('hidden.bs.modal', function () {
            $("#uniqueCodeGrid").clearGridData();
        });
        initUniqeCodeGridColumn(ct);
        $("#codeQty").text(0);
    } else {
        bootbox.alert("客户类型不能为空！");
    }
    allCodes = "";
}

function addProductsOnCode() {
    debugger;
    if (!$('#so_savecode_button').prop('disabled')) {
        $("#so_savecode_button").attr({"disabled": "disabled"});
        var productListInfo = [];
        var alltotActPrice = 0;
        var ct = $("#search_customerType").val();
        $.each($("#uniqueCodeGrid").getDataIDs(), function (index, value) {
            var productInfo = $("#uniqueCodeGrid").getRowData(value);
            if(productInfo.code!=""&&productInfo.code!=undefined){
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
                productInfo.actPrice = Math.round(productInfo.price * productInfo.discount) / 100;
                productInfo.uniqueCodes = productInfo.code;
                productInfo.totPrice = -Math.abs(productInfo.price);
                productInfo.totActPrice = -Math.abs(productInfo.actPrice);
                alltotActPrice += -Math.abs(productInfo.actPrice);
                productInfo.sale = 0;
                productListInfo.push(productInfo);
            }
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
                    dtlRow.totActPrice = -Math.abs(dtlRow.qty * dtlRow.actPrice);
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



function edit_wareHouseOut() {
    skuQty = {};
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        skuQty[rowData.sku] = rowData.outQty;
    });

    inOntWareHouseValid = 'wareHouseOut_valid';

    var origId = $("#search_origId").val();
    taskType = 0;
    wareHouse = origId;
    var ct = $("#search_customerType").val();
    if (ct && ct != null) {
        $("#dialog_buttonGroup").html("" +
            "<button type='button'  class='btn btn-primary' onclick='confirmWareHouseOut()'>确认出库</button>"
        );
        $("#add-uniqCode-dialog").modal('show').on('hidden.bs.modal', function () {
            $("#uniqueCodeGrid").clearGridData();
        });
        initUniqeCodeGridColumn(ct);
        $("#codeQty").text(0);
    } else {
        bootbox.alert("客户类型不能为空！");
    }
    allCodes = "";
}

function confirmWareHouseOut() {
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
        return;
    }
    $.ajax({
        dataType: "json",
        url: basePath + "/logistics/Consignment/convertOut.do",
        data: {
            billNo: billNo,
            strEpcList: JSON.stringify(epcArray),
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



function confirmWareHouseIn() {
    cs.showProgressBar();
    $("#CMDtl_wareHouseIn").attr({"disabled": "disabled"});

    var billNo = $("#search_billNo").val();

    if (billNo && billNo != null) {
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
        if (epcArray.length == 0) {
            bootbox.alert("请添加单据明细！");
            cs.closeProgressBar();
            $("#CMDtl_wareHouseIn").removeAttr("disabled");
            return;
        }
    } else {
        bootbox.alert("没有单据单号！");
        cs.closeProgressBar();
        return;
    }

    $.ajax({
        dataType: "json",
        async:true,
        url: basePath + "/logistics/Consignment/convertIn.do",
        data: {
            billNo: billNo,
            strEpcList: JSON.stringify(epcArray),
            userId: userId
        },
        type: "POST",
        success: function (msg) {
            cs.closeProgressBar();
            $("#CMDtl_wareHouseIn").removeAttr("disabled");
            if (msg.success) {
                var inqty = 0;
                $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
                    var rowData = $("#addDetailgrid").getRowData(value);
                    inqty += parseInt(rowData.qty);
                });
                $("#modal-addEpc-table").modal('hide');
                $("#addDetailgrid").trigger("reloadGrid");
                bootbox.alert({
                    buttons: {ok: {label: '确定',}},
                    message: "已入库" + inqty + "件",
                    callback: function () {
                        quitback();

                    },
                });

            } else {
                bootbox.alert(msg.msg);
            }
        }
    });

    $("#add-uniqCode-dialog").modal('hide');
}
function quitback() {
    $.ajax({
        url: basePath + "/logistics/Consignment/quit.do?billNo=" + billNo,
        cache: false,
        async: true,
        type: "POST",
        success: function (data, textStatus) {

            if (textStatus == "success") {
                $.gritter.add({
                    text: billNo + "可以编辑",
                    class_name: 'gritter-success  gritter-light'
                });
                window.location.href = basePath + '/logistics/Consignment/index.do';
            }

        }
    });
}
var dialogOpenPage;
var prefixId;
function openSearchGuestDialog() {
    dialogOpenPage = "transferOrderconsignmentBill";
    prefixId="edit";
    $("#modal_guest_search_table").modal('show').on('shown.bs.modal', function () {
        initGuestSelect_Grid();
    });
    $("#searchGuestDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='confirm_selected_GuestId_Consignment()'>确认</button>"
    );

}
function updateBillDetailData(){
    debugger
    var ct = $("#search_customerType").val();
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var dtlRow = $("#addDetailgrid").getRowData(value);
        var stylePriceMap = JSON.parse(dtlRow.stylePriceMap);
        if (ct == "CT-AT") {//省代价格
            dtlRow.price = stylePriceMap['puPrice'];
        } else if (ct == "CT-ST") {//门店价格
            dtlRow.price = stylePriceMap['wsPrice'];
        } else if (ct == "CT-LS") {//吊牌价格
            dtlRow.price = stylePriceMap['price'];
        }
        dtlRow.totPrice = -Math.round(dtlRow.qty * dtlRow.price);
        dtlRow.totActPrice = -Math.round((dtlRow.qty * dtlRow.actPrice).toFixed(2));
        if(dtlRow.id){
            $("#addDetailgrid").setRowData(dtlRow.id, dtlRow);
        }else{
            $("#addDetailgrid").setRowData(value, dtlRow);
        }
    });

}
var allCodeStrInDtl = "";  //入库时，所有在单的唯一码
function initAllCodesList() {
    allCodeStrInDtl = "";
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        allCodeStrInDtl = allCodeStrInDtl + "," + rowData.uniqueCodes;
    });
    if (allCodeStrInDtl.substr(0, 1) == ",") {
        allCodeStrInDtl = allCodeStrInDtl.substr(1);
    }
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
    debugger;
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

function saleRetrunNo() {
    allCodes = "";
    $("#codeQtys").val(0);
    $("#dialog_buttonGroups").html("" +
        "<button type='button'  class='btn btn-primary' onclick='addProductsOnCodes()'>保存</button>"
    );
    $("#add-inventory-dialog").modal('show').on('hidden.bs.modal', function () {
        $("#inventoryCodeGrid").clearGridData();
    });

}
function addProductsOnCodes() {
    var codes = "";
    $.each($("#inventoryCodeGrid").getDataIDs(), function (index, value) {
        /* var productInfo = $("#inventoryCodeGrid").getRowData(value);
         if(index==0){
         codes+=productInfo.code;
         }else{
         codes+=","+productInfo.code;
         }*/
        debugger;
        var isok = 0;
        var inventory = $("#inventoryCodeGrid").getRowData(value);
        $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
            debugger;

            var Detailgrid = $("#addDetailgrid").getRowData(value);
            if (inventory.sku == Detailgrid.sku) {
                //改变数量
                var inQty = Detailgrid.inQty;//获取入库数量
                var outQty = Detailgrid.outQty;//获取退货数量
                if (parseInt(inQty) < (parseInt(outQty) + 1)) {
                    $.gritter.add({
                        text: Detailgrid.sku + "退货数量已经超出入库数量",
                        class_name: 'gritter-success  gritter-light'
                    });

                } else {
                    var outQtys = parseInt(outQty) + 1;
                    $('#addDetailgrid').setCell(value, "outQty", outQtys);
                    //修改数据库寄存退货的数量
                    //updateconsignmentnum(Detailgrid.id,outQtys);
                    //比较codes
                    var uniqueCodes = Detailgrid.uniqueCodes;
                    var savehaveuniqueCodes = Detailgrid.savehaveuniqueCodes;
                    var savenohanveuniqueCodes = Detailgrid.savenohanveuniqueCodes;
                    if (uniqueCodes.indexOf(inventory.code) != -1) {
                        if (savehaveuniqueCodes == "") {
                            savehaveuniqueCodes += inventory.code;
                            $('#addDetailgrid').setCell(value, "savehaveuniqueCodes", savehaveuniqueCodes);
                        } else {
                            savehaveuniqueCodes += "," + inventory.code;
                            $('#addDetailgrid').setCell(value, "savehaveuniqueCodes", savehaveuniqueCodes);
                        }
                    } else {
                        if (savenohanveuniqueCodes == "") {
                            savenohanveuniqueCodes += inventory.code;
                            $('#addDetailgrid').setCell(value, "savenohanveuniqueCodes", savenohanveuniqueCodes);
                        } else {
                            savenohanveuniqueCodes += "," + inventory.code;
                            $('#addDetailgrid').setCell(value, "savenohanveuniqueCodes", savenohanveuniqueCodes);
                        }
                    }
                }
                issaleretrun = false;
            } else {
                /*  debugger;
                 $.gritter.add({
                 text: "此退货详情没有对应的sku",
                 class_name: 'gritter-success  gritter-light'
                 });*/
                isok++;
            }
            if (isok == $("#addDetailgrid").getDataIDs().length) {
                $.gritter.add({
                    text: "此退货详情没有对应的sku",
                    class_name: 'gritter-success  gritter-light'
                });
            } else {
                $("#add-inventory-dialog").modal('hide');
            }


        });
    });
}

function updateconsignmentnum(id, outQtys) {

    $.ajax({
        dataType: "json",
        // async:false,
        url: basePath + "/logistics/Consignment/updateConsignmentNum.do",
        data: {id: id, outQtys: outQtys},
        type: "POST",
        success: function (msg) {
            hideWaitingPage();
            if (msg.success) {

            } else {
                bootbox.alert(msg.msg);
            }
        }
    });

}

function saleRetrunNook() {
    cs.showProgressBar();
    var isok = true;
    var billNo = $("#search_billNo").val();
    var epcArray = [];
    if (billNo == "") {
        $.gritter.add({
            text: "请先保存!",
            class_name: 'gritter-success  gritter-light'
        });
        cs.closeProgressBar();
    } else {
        $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
            var rowData = $("#addDetailgrid").getRowData(value);
            if (parseInt(rowData.inQty) == 0) {
                $.gritter.add({
                    text: "请先入库",
                    class_name: 'gritter-success  gritter-light'
                });
                cs.closeProgressBar();
                isok = false;

            } else {
                //  {name: 'savehaveuniqueCodes', label: '唯一码', hidden: false},
                //{name: 'savenohanveuniqueCodes', label: '唯一码', hidden: false}
                //先处理savehaveuniqueCodes唯一码
                var codeshave = rowData.savehaveuniqueCodes.split(",");
                if (codeshave && codeshave != null && codeshave != "") {
                    $.each((codeshave), function (index, value) {
                        var epc = {};
                        epc.code = value;
                        epc.styleId = rowData.styleId;
                        epc.sizeId = rowData.sizeId;
                        epc.colorId = rowData.colorId;
                        epc.qty = 1;
                        epc.sku = rowData.sku;
                        epcArray.push(epc)
                    });
                }
                //处理savenohanveuniqueCodes唯一码
                var codesnohanve = rowData.savenohanveuniqueCodes.split(",");
                if (codesnohanve && codesnohanve != null && codesnohanve != "") {
                    $.each((codesnohanve), function (index, value) {
                        var epc = {};
                        epc.code = value;
                        epc.styleId = rowData.styleId;
                        epc.sizeId = rowData.sizeId;
                        epc.colorId = rowData.colorId;
                        epc.qty = 1;
                        epc.sku = rowData.sku;
                        epcArray.push(epc)
                    });
                }
            }

        });
        $("#search_customerType").removeAttr('disabled');
        $("#search_origId").removeAttr('disabled');
        $("#search_destId").removeAttr('disabled');
        $("#search_busnissId").removeAttr('disabled');
        $("#addDetailgrid").saveCell	(editDtailiRow,editDtailiCol);
        var consignmentBill = JSON.stringify(array2obj($("#editForm").serializeArray()));
        if (addDetailgridiRow != null && addDetailgridiCol != null) {
            $("#addDetailgrid").saveCell(addDetailgridiRow, addDetailgridiCol);
            addDetailgridiRow = null;
            addDetailgridiCol = null;
        }
        console.log(consignmentBill);
        var dtlArray = [];
        $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
            var rowData = $("#addDetailgrid").getRowData(value);
            dtlArray.push(rowData);
        });
        console.log(dtlArray);
        if (isok) {
            $("#CMDtl_wareHouseokSale").hide();
            $.ajax({
                dataType: "json",
                async: true,
                url: basePath + "/logistics/Consignment/saleRetrunNo.do",
                data: {
                    //billNo: billNo,
                    bill: consignmentBill,
                    strDtlList: JSON.stringify(dtlArray),
                    strEpcList: JSON.stringify(epcArray),
                    userId: userId
                },
                type: "POST",
                success: function (msg) {
                    cs.closeProgressBar();
                    $("#CMDtl_wareHouseokSale").show();
                    if (msg.success) {
                        /* $.gritter.add({
                         text: msg.msg,
                         class_name: 'gritter-success  gritter-light'
                         });*/
                        $("#modal-addEpc-table").modal('hide');
                        $("#addDetailgrid").trigger("reloadGrid");
                        bootbox.alert({
                            buttons: {ok: {label: '确定',}},
                            message: msg.msg,
                            callback: function () {
                                quitback();

                            },
                        });

                    } else {
                        bootbox.alert(msg.msg);
                    }
                }
            });
        }
    }

}
function showCodesDetail(uniqueCodes) {

    $("#show-uniqueCode-list").modal('show');
    initUniqueCodeList(uniqueCodes);
    codeListReload(uniqueCodes);
}

function findRetrunno() {
    $("#show-findRetrunNo-list").modal('show');
    initUniqueretrunList();
    retrunListReload();

}