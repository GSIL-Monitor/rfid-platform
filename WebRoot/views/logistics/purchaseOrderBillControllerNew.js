var searchUrl = basePath + "/logistics/purchase/page.do";
var autoSelect =false;//是否自动选中
var showScanDialog = false;
var editDtailRowId = null;  //当前编辑行号
var editDtailColumn = null; //当前编辑列名
var addDetailgridiRow;//存储iRow
var addDetailgridiCol;//存储iCol
$(function () {
    /*初始化左侧grig*/
    initSearchGrid();
    /*初始化右侧grig*/
    initAddGrid();
    /*初始化from表单*/
    initForm();
    if(billNo){
        bootbox.alert("单据"+billNo+"正在编辑中");
    }else{
        sessionStorage.removeItem("billNopurchase");
    }
    pageType="add";
    initButtonGroup(pageType);
    initEditFormValid();
    initSelectbuyahandIdForm();
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9",
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
            $(".selectpicker").selectpicker('refresh');
        }
    });
});
function initForm() {
    initSelectDestForm();

}
function initSearchGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: basePath + "/logistics/purchase/page.do?filter_GTI_status=-1",
        datatype: "json",
        sortorder: 'desc',
        colModel: [
            {name: 'billNo', label: '单据编号', sortable: true, width: 45},
            {
                name: "", label: "操作", width: 60, editable: false, align: "center",hidden:true,
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
                name: 'statusImg', label: '状态', width: 15, align: 'center',
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
                name: 'inStatusImg', label: '入库状态', width: 20, align: 'center',hidden:true,
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
            {name: 'billDate', label: '单据日期', sortable: true, width: 30},
            {name: 'origUnitId', label: '供应商ID', hidden: true},
            {name: 'origUnitName', label: '供应商', width: 40,hidden:true},
            {name: 'origId', label: '出库仓库ID', hidden: true},
            {name: 'origName', label: '出库仓库', hidden: true, width: 40},
            {name: 'destUnitId', label: '收货方ID', hidden: true},
            {name: 'destUnitName', label: '收货方', hidden: true, width: 40},
            {name: 'destId', label: '收货仓库ID', hidden: true},
            {name: 'destName', label: '收货仓库', width: 35},
            {name: 'totQty', label: '单据数量', sortable: false, width: 20},
            {name: 'totInQty', label: '已入库数量', width: 30,hidden:true},
            {name: 'totInVal', label: '总入库金额', width: 30,hidden:true,
                formatter: function (cellValue, options, rowObject) {
                    var totInVal=parseFloat(cellValue).toFixed(2);
                    return totInVal;
                }},
            {name: 'remark', label: '备注', sortable: false, width: 40,hidden:true},
            {name: 'id', hidden: true},
            {name:'buyahandId',hidden:true},
            {name:'payPrice',hidden:true},
            {name:'payType',hidden:true},
            {name:'orderWarehouseId',hidden:true},
            {name:'discount',hidden:true},
            {name:'srcBillNo',hidden:true},
            {name:'remark',hidden:true}
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
            if(autoSelect){
                var rowIds = $("#grid").getDataIDs();
                $("#grid").setSelection(rowIds[0]);
                autoSelect =false;
            }
        },
        onSelectRow: function (rowid, status) {
            initDetailData(rowid)
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
        totInVal: sum_totInVal
    });
}
function initDetailData(rowid) {
    var rowData = $("#grid").getRowData(rowid);
    $("#editForm").setFromData(rowData);
    slaeOrder_status = rowData.status;
    if (slaeOrder_status != "0" && userId != "admin") {
        $("#edit_origId").attr('disabled', true);
    }
    if (userId == "admin") {
        $("#edit_guest_button").removeAttr("disabled");
        $("#edit_origId").attr('disabled', true);
    }
    $(".selectpicker").selectpicker('refresh');
    $('#addDetailgrid').jqGrid("clearGridData");
    $('#addDetailgrid').jqGrid('GridUnload');
    initeditGrid(rowData.billNo);
    pageType="edit";
    initButtonGroup(pageType);
    $("#addDetailgrid").trigger("reloadGrid");
}
function initAddGrid() {
    $("#addDetailgrid").jqGrid({
        height: 'auto',
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
            {name: 'inStatus', label: '入态', hidden: true},
            {
                name: 'printStatus', hidden: true, align: 'center',
                formatter: function (cellValue, options, rowObject) {
                    return 0;
                }
            },
            {
                name: "operation", label: "操作", width: 40, editable: false, align: "center",hidden:true,
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
                name: 'inStatusImg', label: '入态', width: 20, align: 'center',
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
                name: 'printStatusImg', label: '打印状态', width: 20,
                formatter: function (cellValue, options, rowObject) {
                    return '<i class="fa fa-times blue" title="未打印"></i>';
                }
            },
            {name: "inStockType", hidden: true},
            {
                name: 'inStockTypeName', label: '入库类型', width: 20, editable: true,
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
            {name: 'styleId', label: '款号', width: 30},
            {name: 'colorId', label: '色码', width: 30},
            {name: 'sizeId', label: '尺码', width: 30},
            {name: 'styleName', label: '款名', width: 30},
            {name: 'colorName', label: '颜色', width: 30},
            {name: 'sizeName', label: '尺码', width: 30},
            {
                name: 'qty', label: '数量', editable: true, width: 30,
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
            {name: 'actPrintQty', label: '已打印数量', width: 30},
            {name: 'inQty', label: '已入库数量', width: 30},
            {name: 'sku', label: 'SKU', width: 30},

            {
                name: 'price', label: '采购价格', editable: true, width: 30,
                editrules: {
                    number: true
                }
            },
            {
                name: 'discount', label: "折扣", width: 30, editable: true,
                editrules: {
                    number: true,
                    minValue: 0,
                    maxValue: 100
                }
            },
            {name: 'totPrice', label: '采购金额', width: 30},
            {name: 'actPrice', label: '实际价格', editable: true, width: 30},
            {name: 'totActPrice', label: '实际金额', width: 30},
            {
                name: '', label: '唯一码明细', width: 30, align: "center",
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
        afterEditCell: function (rowid, celname, value, iRow, iCol) {
            editDtailRowIds = rowid;
            editDtailColumn = celname;
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
            setEditFooterData();
            for (var i = 0; i < addDetailgridiRow.length; i++) {
                if (addDetailgridiRow[i] == iRow && addDetailgridiCol[i] == iCol) {
                    addDetailgridiRow.splice(i, 1);
                    addDetailgridiCol.splice(i, 1);
                }
            }
        },
        gridComplete: function () {
            setEditFooterData();
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
}
function initeditGrid(billNo) {
    $("#addDetailgrid").jqGrid({
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
            },
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
        onSelectRow: function (rowid, status) {
            if (editDtailRowId != null) {
                $('#addDetailgrid').saveRow(editDtailRowId);
            }
            editDtailRowId = rowid;
            $('#addDetailgrid').editRow(rowid);
        },
        gridComplete: function () {
            setAddFooterData();
        }
    });

    if(curOwnerId !== "1"){
        $("#addDetailgrid").setGridParam().hideCol("price");
        $("#addDetailgrid").setGridParam().hideCol("totPrice");
        $("#addDetailgrid").setGridParam().hideCol("actPrice");
        $("#addDetailgrid").setGridParam().hideCol("totActPrice");
        $("#addDetailgrid").setGridParam().hideCol("discount");
    }else {
        $("#addDetailgrid").setGridParam().showCol("price");
        $("#addDetailgrid").setGridParam().showCol("totPrice");
        $("#addDetailgrid").setGridParam().showCol("actPrice");
        $("#addDetailgrid").setGridParam().showCol("totActPrice");
        $("#addDetailgrid").setGridParam().showCol("discount");
    }
}
function setAddFooterData() {
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
var prefixId;
function openSearchVendorDialog(preId) {
    dialogOpenPage = "purchaseOrder";
    prefixId =preId;
    $("#modal_vendor_search_table").modal('show').on('shown.bs.modal', function () {
        initVendorSelect_Grid();
    });
    console.log(prefixId);
    $("#searchVendorDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='confirm_selected_VendorId_purchaseOrder()'>确认</button>"
    );
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
            orderWarehouseId: {
                validators: {
                    notEmpty: {
                        message: '请选择订货仓库'
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
function initButtonGroup(type){
    if (type === "add") {
        $("#buttonGroup").html("" +
            "<button id='SODtl_add' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='addNew(true)'>" +
            "    <i class='ace-icon fa fa-plus'></i>" +
            "    <span class='bigger-110'>新增</span>" +
            "</button>" +
            "<button id='SODtl_save' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='save()'>" +
            "    <i class='ace-icon fa fa-save'></i>" +
            "    <span class='bigger-110'>保存</span>" +
            "</button>"
        );
        $("#edit_vendor_button").removeAttr("disabled");
        if (defalutCustomerId != "" && defalutCustomerId != undefined && showScanDialog) {
            addUniqCode();
        }
    }
    if (type === "edit") {
        $("#buttonGroup").html("" +
            "<button id='SODtl_add' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='add(true)'>" +
            "    <i class='ace-icon fa fa-plus'></i>" +
            "    <span class='bigger-110'>新增</span>" +
            "</button>" +
            "<button id='SODtl_save' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='save()'>" +
            "    <i class='ace-icon fa fa-save'></i>" +
            "    <span class='bigger-110'>保存</span>" +
            "</button>" +
            "<button id='SODtl_cancel' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='cancel()'>" +
            "    <i class='ace-icon fa fa-undo'></i>" +
            "    <span class='bigger-110'>撤销</span>" +
            "</button>" +
            "<button id='SODtl_doPrint' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='doPrint()'>" +
            "    <i class='ace-icon fa fa-print'></i>" +
            "    <span class='bigger-110'>打印</span>" +
            "</button>" +
            "<button id='SODtl_findRetrunno' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='convertToTagBirth()'>" +
            "    <i class='ace-icon fa fa-search'></i>" +
            "    <span class='bigger-110'>标签初始化</span>" +
            "</button>" +
            "<button id='SODtl_findshopMessage' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='openAddEpcDialog()'>" +
            "    <i class='ace-icon fa fa-search'></i>" +
            "    <span class='bigger-110'>采购入库单</span>" +
            "</button>"
        );
    }
    $("#addDetail").show();
    loadingButton();
}

function loadingButton() {
    $.ajax({
        dataType: "json",
        async: false,
        url: basePath + "/logistics/saleOrder/findResourceButton.do",
        type: "POST",
        success: function (msg) {
            if (msg.success) {
                var result=msg.result;
                for(var i=0;i<result.length;i++){
                    if(result[i].ishow===0){
                        if( $("#"+result[i].buttonId).length>0){
                            $("#"+result[i].buttonId).show();
                        }
                    }else {
                        if( $("#"+result[i].buttonId).length>0){
                            $("#"+result[i].buttonId).hide();
                        }
                    }
                }
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
function showCodesDetail(sku) {

    var billNo = $("#edit_billNo").val();
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
            $("#search_buyahandId").append("<option value='' >--请选择买手--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_buyahandId").append("<option value='" + json[i].id + "'>" + json[i].name + "</option>");
            }
            $(".selectpicker").selectpicker('refresh');
            $("#search_buyahandId").val(saleOrder_buyahandId);
        }
    });
}
function initSelectDestForm() {
    if(userId==="admin"){
        $.ajax({
            url: basePath + "/unit/list.do?filter_EQI_type=9",
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
    }else{
        $.ajax({
            url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + $("#findOwnerId").val(),
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
                $(".selectpicker").selectpicker('refresh');
            }
        });
    }
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9",
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_orderWarehouseId").empty();
            $("#search_orderWarehouseId").append("<option value=''>--请选择订货仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_orderWarehouseId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
            }
            $(".selectpicker").selectpicker('refresh');
        }
    });
    $("#edit_destId").val($("#defaultWarehId").val());
}

/**
 * 查询左侧表格内容
 * */
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

function _resetForm() {
    $("#searchForm").clearForm();
    $("#search_destId").val();
    $("#select_inStatus").val();
    $(".selectpicker").selectpicker('refresh');
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
    setEditFooterData();
}

function addProductInfo(status) {
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
    if(status){
        $("#modal-addDetail-table").modal('hide');
    }
    setEditFooterData();
}
function setEditFooterData() {
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

function saveItem(rowId) {
    $('#addDetailgrid').saveRow(rowId);
    var value = $('#addDetailgrid').getRowData(rowId);
    value.totPrice = value.qty * value.price;
    value.totActPrice = value.qty * value.actPrice;
    $("#addDetailgrid").setRowData(rowId, value);
    setEditFooterData();
}
function deleteItem(rowId) {
    $("#addDetailgrid").jqGrid("delRowData", rowId);
    setEditFooterData();
}