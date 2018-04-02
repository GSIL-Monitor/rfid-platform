var editDtailRowId = null;
$(function () {
    initGrid();
    initForm();
    initButtonGroup();
    initEditFormValid();
    initSelectbuyahandIdForm();
    if(billNo!=""){
        sessionStorage.setItem("billNopurchase",billNo);
    }
});

function initForm() {
    initSelectDestForm();
    if (pageType === "add") {
        $("#search_billDate").val(getToDay("yyyy-MM-dd"));
    } else if (pageType === "edit") {
        $("#search_destId").val(purchaseOrder_destId);
        if(userId!=="admin"){
            $("#search_destId").attr('disabled', true);
        }
        $("#search_billDate").attr('readOnly', true);
    } else if (pageType === "copyAdd") {
        $("#search_billNo").val("");
        $("#search_destId").val(purchaseOrder_destId);
    }
}

function initSelectDestForm() {
    if(userId==="admin"){
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
    }else{
        $.ajax({
            url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + $("#findOwnerId").val(),
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

    $("#search_destId").val($("#defaultWarehId").val());
}

function initButtonGroup() {
    editDtailRowId = null;
    if (pageType === "edit") {
        $("#editDetailgrid").clearGridData();
        $("#buttonGroup").html("" +
            "<button style='margin-left: 20px'type='button' class='btn btn-sm btn-primary' onclick='convertToTagBirth()'" +
            "    <i class='ace-icon fa fa-search'></i>" +
            "    <span class='bigger-110'>标签初始化</span>" +
            "</button>" +
            "<button style='margin-left: 20px' type='button' class='btn btn-sm btn-primary' onclick='openAddEpcDialog()'" +
            "    <i class='ace-icon fa fa-search'></i>" +
            "    <span class='bigger-110'>采购入库单</span>" +
            "</button>" +
            "<button  type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='doPrint(billNo)'>" +
            "    <i class='ace-icon fa fa-reply'></i>" +
            "    <span class='bigger-110'>打印</span>" +
            "</button>");
        $("#editDetail").show();
        $("#addDetail").hide();
        $("#search_vendor_button").attr({"disabled": "disabled"});
        if(userId==="admin"){
            $("#buttonGroup").html("" +
                "<button type='button' class='btn btn-sm btn-primary' onclick='save()'" +
                "    <i class='ace-icon fa fa-search'></i>" +
                "    <span class='bigger-110'>保存</span>" +
                "</button>"+
                "<button style='margin-left: 20px'type='button' class='btn btn-sm btn-primary' onclick='convertToTagBirth()'" +
                "    <i class='ace-icon fa fa-search'></i>" +
                "    <span class='bigger-110'>标签初始化</span>" +
                "</button>" +
                "<button style='margin-left: 20px' type='button' class='btn btn-sm btn-primary' onclick='openAddEpcDialog()'" +
                "    <i class='ace-icon fa fa-search'></i>" +
                "    <span class='bigger-110'>采购入库单</span>" +
                "</button>" +
                "<button  type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='doPrint(billNo)'>" +
                "    <i class='ace-icon fa fa-reply'></i>" +
                "    <span class='bigger-110'>打印</span>" +
                "</button>");
        }
    } else {
        $("#addDetailgrid").clearGridData();
        $("#search_billNo").val("");
        $("#search_payPrice").val("");
        $("#buttonGroup").html("" +
            "<button type='button' class='btn btn-sm btn-primary' onclick='save()'" +
            "    <i class='ace-icon fa fa-search'></i>" +
            "    <span class='bigger-110'>保存</span>" +
            "</button>");
        $("#addDetail").show();
        $("#editDetail").hide();
    }
}
var editDtailRowId = null;  //当前编辑行号
var editDtailColumn = null; //当前编辑列名
var addDetailgridiRow;//存储iRow
var addDetailgridiCol;//存储iCol

function initGrid() {
    $("#addDetailgrid").jqGrid({
        height: 'auto',
        url: basePath + "/logistics/purchase/findBillDtl.do?billNo=" + billNo,
        datatype: "json",
        colModel: [
            {name: 'id', label: 'id', hidden: true, width: 40},
            {name: 'sku', label: 'sku', hidden: true, width: 40},
            {
                name: 'status', hidden: true,
                formatter: function (cellValue, options, rowObject) {
                    return 0;
                }
            },
            {name: 'inStatus', label: '入库状态', hidden: true},
            {
                name: 'printStatus', hidden: true, align: 'center',
                formatter: function (cellValue, options, rowObject) {
                    return 0;
                }
            },
            {
                name: "operation", label: "操作", width: 40, editable: false, align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    return "<a style='margin-left: 20px' a  href='javascript:void(0);' onclick=saveItem('" + options.rowId + "')><i class='ace-icon ace-icon fa fa-save' title='保存'></i></a>"
                        + "<a style='margin-left: 20px' a  href='javascript:void(0);' onclick=deleteItem('" + options.rowId + "')><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";
                }
            },
            {
                name: 'statusImg', label: '状态', width: 20, align: 'center',
                formatter: function (cellValue, options, rowObject) {
                    return '<i class="fa fa-tasks blue" title="订单状态"></i>';
                }
            },
            {
                name: 'inStatusImg', label: '入库状态', width: 30, align: 'center',
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
            {
                name: 'printStatusImg', label: '打印状态', width: 40,
                formatter: function (cellValue, options, rowObject) {
                    return '<i class="fa fa-times blue" title="未打印"></i>';
                }
            },
            {name: "inStockType", hidden: true},
            {
                name: 'inStockTypeName', label: '入库类型', width: 40, editable: true,
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

                }, formatter: function (cellValue, options, rowObject) {
                switch (rowObject.inStockType) {
                    case 'XK':
                        return "新款";
                    case 'BH':
                        return "补货";
                    case 'PH':
                        return "供应商配货";
                    case 'JS':
                        return "寄售";
                    default :
                        return "";
                }
            }
            },
            {name: 'styleId', label: '款号', width: 40},
            {name: 'colorId', label: '色码', width: 40},
            {name: 'sizeId', label: '尺码', width: 40},
            {name: 'styleName', label: '款名', width: 40},
            {name: 'colorName', label: '颜色', width: 40},
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
            {name: 'actPrintQty', label: '已打印数量', width: 40},
            {name: 'inQty', label: '已入库数量', width: 40},
            {name: 'sku', label: 'SKU', width: 40},

            {
                name: 'price', label: '采购价格', editable: true, width: 40,
                editrules: {
                    number: true
                }
            },
            {
                name: 'discount', label: "折扣", width: 40, editable: true,
                editrules: {
                    number: true,
                    minValue: 0,
                    maxValue: 100
                }
            },
            {name: 'totPrice', label: '采购金额', width: 40},
            {name: 'actPrice', label: '实际价格', editable: true, width: 40},
            {name: 'totActPrice', label: '实际金额', width: 40},
            {
                name: '', label: '唯一码明细', width: 40, align: "center",
                formatter: function (cellValue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=showCodesDetail('" + rowObject.sku + "')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
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
        cellEdit: true,
        cellsubmit: 'clientArray',
        /*onSelectRow: function (rowid, status) {
         if (pageType != "edit") {
         if (editDtailRowId != null) {
         saveItem(editDtailRowId);
         }
         editDtailRowId = rowid;
         $('#addDetailgrid').editRow(rowid);
         }
         },*/
        afterEditCell: function (rowid, celname, value, iRow, iCol) {
            editDtailRowIds = rowid;
            editDtailColumn = celname;
            /* $('#addDetailgrid').saveCell(iRow,iCol);*/
            addDetailgridiRow = iRow;
            addDetailgridiCol = iCol;

        },
        afterSaveCell: function (rowid, cellname, value, iRow, iCol) {
            if (cellname === "discount") {
                var var_actPrice = Math.round(value * $('#addDetailgrid').getCell(rowid, "price") / 100);
                var var_totActPrice = var_actPrice * $('#addDetailgrid').getCell(rowid, "qty");
                $('#addDetailgrid').setCell(rowid, "actPrice", var_actPrice);
                $('#addDetailgrid').setCell(rowid, "totActPrice", var_totActPrice);
            } else if (cellname === "actPrice") {
                var var_discount = Math.round(value / $('#addDetailgrid').getCell(rowid, "price") * 100);
                var var_totActPrice = value * $('#addDetailgrid').getCell(rowid, "qty");
                $('#addDetailgrid').setCell(rowid, "discount", var_discount);
                $('#addDetailgrid').setCell(rowid, "totActPrice", var_totActPrice);
            }
            setFooterData();
            for (var i = 0; i < addDetailgridiRow.length; i++) {
                if (addDetailgridiRow[i] == iRow && addDetailgridiCol[i] == iCol) {
                    addDetailgridiRow.splice(i, 1);
                    addDetailgridiCol.splice(i, 1);
                }
            }
        },
        gridComplete: function () {
            setFooterData();
        }
    });
    var parent_column = $("#main-container");
    if (pageType == "edit") {
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
            }
        );
    }

    $("#addDetailgrid-pager_center").html("");
    $("#addDetailgrid").jqGrid('setGridWidth', parent_column.width() - 10);

    $("#editDetailgrid").jqGrid({
        height: 'auto',
        datatype: "json",
        url: basePath + "/logistics/purchase/findBillDtl.do?billNo=" + billNo,
        colModel: [
            {name: 'id', label: 'id', hidden: true, width: 40},
            {name: 'sku', label: 'sku', hidden: true, width: 40},
            {name: 'status', hidden: true},
            {name: 'inStatus', hidden: true},
            {name: 'printStatus', hidden: true},

            {
                name: 'statusImg', label: '状态', width: 20, align: 'center',
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.status == 0) {
                        return '<i class="fa fa-tasks blue" title="订单状态"></i>';
                    } else if (rowObject.status == 1) {
                        return '<i class="fa fa-sign-in blue" title="入库状态"></i>';
                    } else if (rowObject.status == 2) {
                        return '<i class="fa fa-sign-out blue" title="出库状态"></i>';
                    } else {
                        return '';
                    }
                }
            },
            {
                name: 'inStatusImg', label: '入库状态', width: 30, align: 'center',
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
            {
                name: 'printStatusImg', label: '打印状态', width: 40, align: 'center',
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.printStatus == 0) {
                        return '<i class="fa fa-times blue" title="未打印"></i>';
                    } else if (rowObject.printStatus == 1) {
                        return '<i class="fa fa-tags blue" title="部分打印"></i>';
                    } else if (rowObject.printStatus == 2) {
                        return '<i class="fa fa-print blue" title="已打印"></i>';
                    } else {
                        return '';
                    }
                }
            },
            {
                name: 'inStockTypeName', label: '入库类型', width: 40,
                formatter: function (cellValue, options, rowObject) {
                    switch (rowObject.inStockType) {
                        case 'XK':
                            return "新款";
                        case 'BH':
                            return "补货";
                        case 'PH':
                            return "供应商配货";
                        case 'JS':
                            return "寄售";
                        default :
                            return "";
                    }
                }
            },

            {name: 'styleId', label: '款号', width: 40},
            {name: 'colorId', label: '色码', width: 40},
            {name: 'sizeId', label: '尺码', width: 40},
            {name: 'styleName', label: '款名', width: 40},
            {name: 'colorName', label: '颜色', width: 40},
            {name: 'sizeName', label: '尺码', width: 40},
            {name: 'qty', label: '数量', width: 40},
            {name: 'actPrintQty', label: '已打印数量', width: 40},
            {
                name: 'printQty', label: '待打印数量', width: 40,
                editable: true,
                editoptions: {
                    dataInit: function (e) {
                        var maxValue = $(e).val();
                        $(e).spinner({
                            value: maxValue,
                            min: 0, //最小值
                            max: maxValue, //最大值
                            step: 1
                        });
                    }
                }
            },
            {name: 'inQty', label: '已入库数量', width: 40},
            {name: 'sku', label: 'SKU', width: 40},
            {name: 'price', label: '采购价格', width: 40,
                formatter: function (cellValue, options, rowObject) {
                    var price=parseFloat(cellValue).toFixed(2);
                    return price;
                }},
            {name: 'totPrice', label: '采购金额', width: 40,
                formatter: function (cellValue, options, rowObject) {
                    var totPrice=parseFloat(cellValue).toFixed(2);
                    return totPrice;
                }},
            {name: 'actPrice', label: '实际价格', width: 40,
                formatter: function (cellValue, options, rowObject) {
                    var actPrice=parseFloat(cellValue).toFixed(2);
                    return actPrice;
                }},
            {name: 'totActPrice', label: '实际金额', width: 40,
                formatter: function (cellValue, options, rowObject) {
                    var actPrice=parseFloat(cellValue).toFixed(2);
                    return actPrice;
                }},

            {name: 'actQty', hidden: true},
            {name: "billId", hidden: true},
            {name: "billNo", hidden: true},
            {name: "inStockType", hidden: true},
            {
                name: '', label: '唯一码明细', width: 40, align: "center",
                formatter: function (cellValue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=showCodesDetail('" + rowObject.sku + "')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
                }
            }
        ],
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: -1,
        pager: '#editDetailgrid-pager',
        multiselect: false,
        shrinkToFit: true,
        sortname: 'id',
        sortorder: "asc",
        footerrow: true,
        onSelectRow: function (rowid, status) {
            if (editDtailRowId != null) {
                $('#editDetailgrid').saveRow(editDtailRowId);
            }
            editDtailRowId = rowid;
            $('#editDetailgrid').editRow(rowid);
        },
        gridComplete: function () {
            setEditFooterData();
        }
    });

    if(curOwnerId !== "1"){
        $("#addDetailgrid").setGridParam().hideCol("price");
        $("#addDetailgrid").setGridParam().hideCol("totPrice");
        $("#addDetailgrid").setGridParam().hideCol("actPrice");
        $("#addDetailgrid").setGridParam().hideCol("totActPrice");
        $("#addDetailgrid").setGridParam().hideCol("discount");
        $("#editDetailgrid").setGridParam().hideCol("price");
        $("#editDetailgrid").setGridParam().hideCol("totPrice");
        $("#editDetailgrid").setGridParam().hideCol("actPrice");
        $("#editDetailgrid").setGridParam().hideCol("totActPrice");
        $("#editDetailgrid").setGridParam().hideCol("discount");
    }else {
        $("#addDetailgrid").setGridParam().showCol("price");
        $("#addDetailgrid").setGridParam().showCol("totPrice");
        $("#addDetailgrid").setGridParam().showCol("actPrice");
        $("#addDetailgrid").setGridParam().showCol("totActPrice");
        $("#addDetailgrid").setGridParam().showCol("discount");
        $("#editDetailgrid").setGridParam().showCol("price");
        $("#editDetailgrid").setGridParam().showCol("totPrice");
        $("#editDetailgrid").setGridParam().showCol("actPrice");
        $("#editDetailgrid").setGridParam().showCol("totActPrice");
        $("#editDetailgrid").setGridParam().showCol("discount");
    }
}
function setFooterData() {
    var sum_qty = $("#addDetailgrid").getCol('qty', false, 'sum');
    var sum_totPrice = $("#addDetailgrid").getCol('totPrice', false, 'sum');
    var sum_totActPrice = Math.round($("#addDetailgrid").getCol('totActPrice', false, 'sum'));
    $("#addDetailgrid").footerData('set', {
        styleId: "合计",
        qty: sum_qty,
        totPrice: sum_totPrice,
        totActPrice: sum_totActPrice
    });
}

function setEditFooterData() {
    var sum_qty = $("#editDetailgrid").getCol('qty', false, 'sum');
    var sum_totPrice = $("#editDetailgrid").getCol('totPrice', false, 'sum');
    var sum_totActPrice = Math.round($("#editDetailgrid").getCol('totActPrice', false, 'sum'));
    $("#editDetailgrid").footerData('set', {
        styleId: "合计",
        qty: sum_qty,
        totPrice: sum_totPrice,
        totActPrice: sum_totActPrice
    });
}

function addDetail() {
    $("#modal-addDetail-table").modal('show').on('hidden.bs.modal', function () {
        $("#StyleSearchForm").resetForm();
        $("#stylegrid").clearGridData();
        $("#color_size_grid").clearGridData();
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


function addProductInfo() {
    if (editDtailRowId != null) {
        $('#addDetailgrid').saveRow(editcolosizeRow);
    }
    var addProductInfo = [];
    if (editcolosizeRow != null) {
        $('#color_size_grid').saveRow(editcolosizeRow);
    }
    var styleRow = $("#stylegrid").getRowData($("#stylegrid").jqGrid("getGridParam", "selrow"));

    $.each($("#color_size_grid").getDataIDs(), function (index, value) {
        var productInfo = $("#color_size_grid").getRowData(value);
        if (productInfo.qty > 0) {
            productInfo.price = styleRow.preCast;
            productInfo.actPrice = productInfo.price;
            productInfo.status = 0;
            productInfo.printStatus = 0;
            productInfo.totPrice = productInfo.qty * productInfo.price;
            productInfo.totActPrice = productInfo.qty * productInfo.actPrice;
            productInfo.sku = productInfo.code;
            productInfo.printQty = 0;
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
    $("#modal-addDetail-table").modal('hide');
    setFooterData();

}

function openAddEpcDialog() {
    $("#modal-addEpc-table").modal('show').on('hidden.bs.modal', function () {
        $("#epcgrid").clearGridData();
    });
    $("#epcgrid").jqGrid('setGridParam', {
        page: 1,
        url: basePath + '/logistics/purchase/findNotInEpc.do',
        postData: {billNo: billNo}
    }).trigger("reloadGrid");
}

function convertToTagBirth() {
    if (editDtailRowId != null) {
        $("#editDetailgrid").saveRow(editDtailRowId);
        editDtailRowId = null;
    }
    var dtlArray = [];
    $.each($("#editDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#editDetailgrid").getRowData(value);
        dtlArray.push(rowData);
    });
    showWaitingPage();
    $.ajax({
        dataType: "json",
        url: basePath + "/logistics/purchase/covertToTagBirth.do",
        data: {strDtlList: JSON.stringify(dtlArray)},
        type: "POST",
        success: function (msg) {
            hideWaitingPage();
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#editDetailgrid").trigger("reloadGrid");
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}

function save() {
    debugger;
    $("#search_destId").removeAttr('disabled');

    $("#editForm").data('bootstrapValidator').destroy();
    $('#editForm').data('bootstrapValidator', null);
    initEditFormValid();
    $('#editForm').data('bootstrapValidator').validate();
    if (!$('#editForm').data('bootstrapValidator').isValid()) {
        return;
    }
    if (editDtailRowId != null) {
        $("#addDetailgrid").saveRow(editDtailRowId);
        editDtailRowId = null;
    }
    if ($("#addDetailgrid").getDataIDs().length == 0) {
        bootbox.alert("请添加采购商品");
        return;
    }
    /*if (editDtailRowIds != null && editDtailColumn != null) {
     $("#addDetailgrid").saveCell(editDtailRowIds, editDtailColumn);
     editDtailRowIds = null;
     editDtailColumn = null;
     }*/
    if (addDetailgridiRow != null && addDetailgridiCol != null) {
        $("#addDetailgrid").saveCell(addDetailgridiRow, addDetailgridiCol);
        addDetailgridiRow = null;
        addDetailgridiCol = null;
    }
    /*if(addDetailgridiRow.length!=0){
     for(var i=0;i<addDetailgridiRow.length;i++){
     $("#addDetailgrid").saveCell(addDetailgridiRow[i], addDetailgridiCol[i]);
     }
     }*/

    var dtlArray = [];
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        dtlArray.push(rowData);
    });
    showWaitingPage();
    $.ajax({
        dataType: "json",
        // async:false,
        url: basePath + "/logistics/purchase/save.do",
        data: {
            purchaseBillStr: JSON.stringify(array2obj($("#editForm").serializeArray())),
            strDtlList: JSON.stringify(dtlArray),
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
                quitback();

            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}
function quitback() {
    $.ajax({
        url: basePath +"/logistics/purchase/quit.do?billNo=" +billNo,
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {

            if(textStatus=="success"){
                $.gritter.add({
                    text: billNo+"可以编辑",
                    class_name: 'gritter-success  gritter-light'
                });
                window.location.href = basePath + "/logistics/purchase/index.do";
            }

        }
    });
}
function saveCovert() {
    var ids = $("#epcgrid").jqGrid("getGridParam", "selarrrow");
    if (ids.length == 0) {
        bootbox.alert("请选择要入库的唯一码信息");
    } else {
        var epcArray = [];
        $.each(ids, function (index, value) {
            var rowData = $("#epcgrid").getRowData(value);
            epcArray.push(rowData);
        });
        var dtlArray = [];
        $.each($("#editDetailgrid").getDataIDs(), function (index, value) {
            var rowData = $("#editDetailgrid").getRowData(value);
            dtlArray.push(rowData);
        });
        showWaitingPage();
        $.ajax({
            dataType: "json",
            url: basePath + "/logistics/purchase/convert.do",
            data: {strDtlList: JSON.stringify(dtlArray), recordList: JSON.stringify(epcArray)},
            type: "POST",
            success: function (msg) {
                hideWaitingPage();
                if (msg.success) {
                    $.gritter.add({
                        text: msg.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                    $("#modal-addEpc-table").modal('hide');
                    $("#editDetailgrid").trigger("reloadGrid");
                } else {
                    bootbox.alert(msg.msg);
                }
            }
        });
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
            billNo: {
                validators: {}
            },
            origUnitId: {
                validators: {
                    notEmpty: {
                        message: '请选择供应商'
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
            },
            payPrice: {
                validators: {
                    notEmpty: {
                        message: '实付金额不能为空'
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
function search_discount_onblur() {
    setDiscount();
}
function setDiscount() {
    var discount = $("#search_discount").val();
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        $('#addDetailgrid').setCell(index, "discount", discount);
        var var_actPrice = Math.round(discount * $('#addDetailgrid').getCell(index, "price") / 100);
        var var_totActPrice = var_actPrice * $('#addDetailgrid').getCell(index, "qty");
        $('#addDetailgrid').setCell(index, "actPrice", var_actPrice);
        $('#addDetailgrid').setCell(index, "totActPrice", var_totActPrice);
    });
    setFooterData();
}
function doPrint() {
    debugger;
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
function doPrint() {
    debugger;
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
function doPrint() {
    debugger;
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
function initSelectbuyahandIdForm() {
    var url;

    url = basePath + "/sys/user/list.do?filter_EQS_roleId=BUYER";



    $.ajax({
        url: url,
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_buyahandId").empty();
            $("#search_buyahandId").append("<option value='' >--请选择销售员--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_buyahandId").append("<option value='" + json[i].id + "'>" + json[i].name + "</option>");
                // $("#search_busnissId").trigger('chosen:updated');
            }

            $("#search_buyahandId").val(saleOrder_buyahandId);
        }
    });
}
