var taskType; //用于判断出入库类型 1入库 0 出库
var wareHouse;
var allCodes; //用于拼接所有添加过的唯一码，防止重复添加
var inOntWareHouseValid; //用于判断在编辑BillDtl时出入库操作是否需要校验，使用哪种校验。
var allCodeStrInDtl = "";  //入库时，所有明细中的唯一码
$(function () {
    initForm();
    initGrid();
    keydown();

});
function  initForm() {
    //initSelectDestForm();
    initSelectOrigForm();
    initSelectclass9();
    initButtonGroup();

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
        url: basePath + "/logistics/purchase/findBillDtl.do?billNo=" + billNo,
        datatype: "json",
        sortorder: 'desc',
        colModel: [

            {name: 'styleId', label: '款号', sortable: true, width: 40},
            {
                name: "", label: "操作", width: 30, editable: false, sortable: false, align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=saveItem('" + options.rowId + "')><i class='ace-icon ace-icon fa fa-save' title='保存'></i></a>"
                        + "<a style='margin-left: 20px' href='javascript:void(0);' onclick=deleteItem('" + options.rowId + "')><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";
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
            {name: 'preCast', label: '采购价',hidden:true, width: 40},
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
            {name: 'uniqueCodes', label: '唯一码'}

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
            "</button>"

        );
    }
}
function addUniqCode() {
    inOntWareHouseValid = 'addPage_scanUniqueCode';
    var origId=$("#search_origId").val();
    //var destId=$("#search_destId").val();
    var beforeclass9=$("#search_beforeclass9").val();
    var nowclass9=$("#search_nowclass9").val();
    taskType = 0;
    wareHouse=origId;
    if (origId ==""|| origId == null) {
        bootbox.alert("仓库不能为空！")
        return
    }
    /*if (destId ==""|| destId == null) {
        bootbox.alert("入库仓库不能为空！")
        return
    }*/
    if (beforeclass9 ==""|| beforeclass9 == null) {
        bootbox.alert("原系列不能为空！")
        return
    }
    if (nowclass9 ==""|| nowclass9 == null) {
        bootbox.alert("现系列不能为空！")
        return
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
            productInfo.preCast=productInfo.preCast;
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
                    dtlRow.actPrice = dtlRow.price*dtlRow.discount;
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
    cs.showProgressBar();
    var dtlArray = [];
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        dtlArray.push(rowData);
    });
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

                /*$("#addDetailgrid").jqGrid('setGridParam', {
                    datatype: "json",
                    page: 1,
                    url: basePath + "/logistics/saleOrder/findBillDtl.do?billNo=" + msg.result,
                });
                $("#addDetailgrid").trigger("reloadGrid");
                $("#SODtl_adddoPrint1").show();*/
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}

