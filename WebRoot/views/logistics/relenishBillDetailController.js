$(function () {
    initGrid();
    initSelectBusinessIdForm();
    initButtonGroup();
});
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
            $("#search_busnissId").val(saleOrder_busnissId);
        }
    });
}

function initButtonGroup() {
    $("#buttonGroup").html("" +
        "<button id='SODtl_save' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='save()'>" +
        "    <i class='ace-icon fa fa-save'></i>" +
        "    <span class='bigger-110'>保存</span>" +
        "</button>"
    );

}
function initGrid() {
    $("#addDetailgrid").jqGrid({
        height: 'auto',
        datatype: "json",
        url: basePath + "/logistics/relenishBill/findBillDtl.do?billNo=" + billNo,
        mtype: 'POST',
        colModel: [
            {name: 'id', label: 'id', hidden: true},
            {name: 'billId', label: 'billId', hidden: true},
            {name: 'billNo', label: 'billNo', hidden: true},
            {name: 'status', hidden: true},

            {
                name: "operation", label: "操作", width: 30, align: "center", sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=saveItem('" + options.rowId + "')><i class='ace-icon ace-icon fa fa-save' title='保存'></i></a>"
                        + "<a style='margin-left: 20px' href='javascript:void(0);' onclick=deleteItem('" + options.rowId + "')><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";
                }
            },

            {name: 'styleId', label: '款号', width: 40  },

            {name: 'colorId', label: '色码', width: 40},

            {name: 'sizeId', label: '尺码', width: 30},

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
            {name: 'sku', label: 'SKU', width: 50}
            /*{
                name: 'actPrice', label: '实际价格', editable: true, width: 40,
                editrules: {
                    number: true,
                    minValue: 0
                }
            }*/

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
            addDetailgridiRow = iRow;
            addDetailgridiCol = iCol;
        },
        afterSaveCell: function (rowid, cellname, value, iRow, iCol) {
            if (cellname === "discount") {
                var var_actPrice = Math.round(value * $('#addDetailgrid').getCell(rowid, "price")) / 100;
                var var_totActPrice = Math.round(var_actPrice * $('#addDetailgrid').getCell(rowid, "qty") * 100) / 100;
                $('#addDetailgrid').setCell(rowid, "actPrice", var_actPrice);
                $('#addDetailgrid').setCell(rowid, "totActPrice", var_totActPrice);
            } else if (cellname === "actPrice") {
                var var_discount = Math.round(value / $('#addDetailgrid').getCell(rowid, "price") * 100);
                var var_totActPrice = Math.round(value * $('#addDetailgrid').getCell(rowid, "qty") * 100) / 100;
                $('#addDetailgrid').setCell(rowid, "discount", var_discount);
                $('#addDetailgrid').setCell(rowid, "totActPrice", var_totActPrice);
            } else if (cellname === "qty") {
                $('#addDetailgrid').setCell(rowid, "totPrice", Math.round($('#addDetailgrid').getCell(rowid, "price") * value * 100) / 100);
                $('#addDetailgrid').setCell(rowid, "totActPrice", Math.round($('#addDetailgrid').getCell(rowid, "actPrice") * value * 100) / 100);
            }
            setFooterData();
        },
        gridComplete: function () {
            setFooterData();
        },
        loadComplete: function () {

        }
    });
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
    $("#addDetailgrid-pager_center").html("");
}
function setFooterData() {
    var sum_qty = $("#addDetailgrid").getCol('qty', false, 'sum');

    /*$("#search_actPrice").val(sum_totActPrice);*/
    $("#addDetailgrid").footerData('set', {
        styleId: "合计",
        qty: sum_qty,

    });
}

function addDetail() {
    $("#modal-addDetail-table").modal('show').on('hidden.bs.modal', function () {
        $("#StyleSearchForm").resetForm();
       /* $("#stylegrid").clearGridData();*/
        $("#color_size_grid").clearGridData();
    });
}

function addProductInfo() {
    debugger;
    var addProductInfo = [];
    $('#color_size_grid').saveRow(editcolosizeRow);
    var styleRow = $("#stylegrid").getRowData($("#stylegrid").jqGrid("getGridParam", "selrow"));
    $.each($("#color_size_grid").getDataIDs(), function (index, value) {
        debugger;
        var productInfo = $("#color_size_grid").getRowData(value);
        if (productInfo.qty > 0) {
            productInfo.sku = productInfo.code;
            productInfo.inStockType = styleRow.class6;
            addProductInfo.push(productInfo);
        }
    });
    console.log(addProductInfo);
    var isAdd = true;
    debugger;
    $.each(addProductInfo, function (index, value) {
        isAdd = true;
        debugger;
        $.each($("#addDetailgrid").getDataIDs(), function (dtlndex, dtlValue) {
            debugger;
            var dtlRow = $("#addDetailgrid").getRowData(dtlValue);
            if (value.code === dtlRow.sku) {
                dtlRow.qty = parseInt(dtlRow.qty) + parseInt(value.qty);
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
function saveItem(rowId) {

    var value = $('#addDetailgrid').getRowData(rowId);

    $("#addDetailgrid").setRowData(rowId, value);
    setFooterData();
}
function deleteItem(rowId) {
    var value = $('#addDetailgrid').getRowData(rowId);
    $("#addDetailgrid").jqGrid("delRowData", rowId);
    setFooterData();


}

function save() {
    if ($("#addDetailgrid").getDataIDs().length == 0) {
        bootbox.alert("请添加补货商品！");
        return;
    }
    var dtlArray = [];
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        dtlArray.push(rowData);
    });
    showWaitingPage();
    $.ajax({
        dataType: "json",
        async: false,
        url: basePath + "/logistics/relenishBill/save.do",
        data: {
            replenishBillStr: JSON.stringify(array2obj($("#editForm").serializeArray())),
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
                $("#search_billNo").val(msg.result);
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}
