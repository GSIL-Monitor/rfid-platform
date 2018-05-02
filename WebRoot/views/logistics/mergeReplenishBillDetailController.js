$(function () {
    initGrid();
    //initSelectBusinessIdForm();
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

        }
    });
}

function initButtonGroup() {
    $("#buttonGroup").html("" +
        "<button id='SODtl_changNum' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='onchangNum()'>" +
        "    <i class='ace-icon fa fa-save'></i>" +
        "    <span class='bigger-110'>转换数量</span>" +
        "</button>" +
        "<button id='SODtl_save_changNum' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='onchangPurchase()'>" +
        "    <i class='ace-icon fa fa-save'></i>" +
        "    <span class='bigger-110'>转换采购单</span>" +
        "</button>" +
        "<button id='SODtl_export_changNum' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='exportmessage()'>" +
        "    <i class='ace-icon fa fa-save'></i>" +
        "    <span class='bigger-110'>导出</span>" +
        "</button>"
    );

}
function initGrid() {
    $.ajax({
        url: basePath + "/logistics/mergeReplenishBillController/findMergeBillDetail.do",
        cache: false,
        data: {
            billNo: billNo

        },
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            debugger;
            var messagekey = data.result.key;
            var messageresult = data.result.result;

            var model = [];
            for (var i = 0; i < messagekey.length; i++) {
                if (messagekey[i].name == "url") {
                    model.push({
                        name: messagekey[i].name,
                        label: messagekey[i].label,
                        sortable: messagekey[i].sortable,
                        width: 100
                    });
                } else {
                    model.push({
                        name: messagekey[i].name,
                        label: messagekey[i].label,
                        width: 100
                    });
                }


            }
            debugger;
            $("#addDetailgrid").jqGrid({
                height: 'auto',
                datatype: "json",

                colModel: model,
                autowidth: true,
                rownumbers: true,
                altRows: true,
                rowNum: -1,
                pager: '#addDetailgrid-pager',
                multiselect: false,
                shrinkToFit: true,

                footerrow: true,
                cellEdit: true,
                cellsubmit: 'clientArray',
                gridComplete: function () {
                    setFooterData();
                },
                loadComplete: function () {

                }
            });
            $("#addDetailgrid").setColProp("url", {
                formatter: function imageFormatter(cellvalue, options, rowObject) {
                    if (rowObject.url == null) {
                        return "无图片";
                    } else {
                        return "<img width=80 height=100 src='" + basePath + rowObject.url + "' alt='" + rowObject.styleid + "'/>";
                    }

                }
            });
            for (var i = 0; i < messageresult.length; i++) {
                $("#addDetailgrid").jqGrid('addRowData', i, messageresult[i]);
            }

        }
    });
}

function setFooterData() {
    var sum_qty = $("#addDetailgrid").getCol('Allqty', false, 'sum');

    /*$("#search_actPrice").val(sum_totActPrice);*/
    $("#addDetailgrid").footerData('set', {
        styleId: "合计",
        Allqty: sum_qty,

    });
}

function addDetail() {
    $("#modal-addDetail-table").modal('show').on('hidden.bs.modal', function () {
        $("#StyleSearchForm").resetForm();
        /* $("#stylegrid").clearGridData();*/
        $("#color_size_grid").clearGridData();
    });
}

function addProductInfo(status) {
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
    if (status) {
        $("#modal-addDetail-table").modal('hide');
    }
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
    cs.showProgressBar();
    if ($("#addDetailgrid").getDataIDs().length == 0) {
        bootbox.alert("请添加补货商品！");
        cs.closeProgressBar();
        return;
    }
    var dtlArray = [];
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        dtlArray.push(rowData);
    });

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
            cs.closeProgressBar();

            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });

            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}

function onchangNum() {
    $("#modal-merege-table").modal('show').on('hidden.bs.modal', function () {
        $("#meregegrid").clearGridData();
    });
    $("#meregegrid").jqGrid('setGridParam', {
        page: 1,
        url: basePath + '/logistics/mergeReplenishBillController/findRecordsizeBybillNo.do',
        postData: {billNo: billNo}
    }).trigger("reloadGrid");
}

function onchangPurchase() {
    cs.showProgressBar();
    $("#SODtl_save_changNum").attr({"disabled": "disabled"});
    $.ajax({
        dataType: "json",
        async: false,
        url: basePath + "/logistics/mergeReplenishBillController/changePurchase.do",
        data: {
            billNo: billNo
        },
        type: "POST",
        success: function (msg) {
            cs.closeProgressBar();
            $("#SODtl_save_changNum").removeAttr("disabled");
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });

            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}

function exportmessage() {

    var exportUrl = basePath + "/logistics/mergeReplenishBillController/exportmessage.do";
    $("#form1").attr("action", exportUrl);
    $("#billNo").val(billNo);
    $("#form1").submit();
}
function savemerege() {

    $("#meregegrid").saveCell(editDtailiRow, editDtailiCol);
    editDtailiRow = null;
    editDtailiCol = null;
    var dtlArray = [];
    $.each($("#meregegrid").getDataIDs(), function (index, value) {
        var rowData = $("#meregegrid").getRowData(value);
        dtlArray.push(rowData);
    });
    cs.showProgressBar();
    $.ajax({
        dataType: "json",
        // async:false,
        url: basePath + "/logistics/mergeReplenishBillController/saveRecordsize.do",
        data: {
            'strDtlList': JSON.stringify(dtlArray),
        },
        type: "POST",
        success: function (msg) {
            cs.closeProgressBar();
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                debugger;
                /*$("#search_billNo").val(msg.result);*/
                /*  billNo = msg.result;*/
                $("#search_billNo").val(billNo);
                issaleretrun = true;
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
    $("#modal-merege-table").modal('hide');

}
