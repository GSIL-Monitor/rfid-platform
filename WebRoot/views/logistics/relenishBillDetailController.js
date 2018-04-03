var addDetailgridiRow;//存储iRow
var addDetailgridiCol;//存储iCol
$(function () {
    initGrid();
    initSelectBusinessIdForm();
    initSelectbuyahandIdForm();
    initButtonGroup();
    console.log(replenishType);
    if(replenishType!=""&&replenishType!=undefined){
        if(replenishType==="1"){
            $("#edit_replenishType input:radio[value='1']").attr("checked", "checked");
        }else{
            $("#edit_replenishType input:radio[value='0']").attr("checked", "checked");
        }
    }
    debugger;
    //加载审核图片
    if(slaeOrder_status==1){
        $("#approvedCheck").show();
        $("#checkImage").attr("src",basePath+"/images/check/check.png");

    }
    if(slaeOrder_status==8){
        $("#approvedCheck").show();
        $("#checkImage").attr("src",basePath+"/images/check/nocheck.png");
    }




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
                // $("#search_busnissId").trigger('chosen:updated');
            }

            $("#search_buyahandId").val(saleOrder_buyahandId);
        }
    });
}

function initButtonGroup() {
    $("#buttonGroup").html("" +
        "<button id='SODtl_save' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='save()'>" +
        "    <i class='ace-icon fa fa-save'></i>" +
        "    <span class='bigger-110'>保存</span>" +
        "</button>"+
        "<button id='SODtl_saveAndAdd' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='saveAndAdd()'>" +
        "    <i class='ace-icon fa fa-save'></i>" +
        "    <span class='bigger-110'>保存并新增</span>" +
        "</button>"+
        "<button id='SODtl_check' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='showcheck()'>" +
        "    <i class='ace-icon fa fa-save'></i>" +
        "    <span class='bigger-110'>审核</span>" +
        "</button>"+
        "<button id='SODtl_noCheck' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='showNocheck()'>" +
        "    <i class='ace-icon fa fa-save'></i>" +
        "    <span class='bigger-110'>反审核</span>" +
        "</button>"+
        "<button id='SODtl_changePurchase' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='changePurchase()'>" +
        "    <i class='ace-icon fa fa-save'></i>" +
        "    <span class='bigger-110'>生成采购单</span>" +
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
            {name: 'sizeId', label: '尺码', width: 40},
            {name: 'buyahandName', label: '买手', width: 30},
            {name: 'buyahandId', label: '买手', width: 30, hidden: true},
            {name: 'price', label: '采购价格', width: 30},
            {name: 'totPrice', label: '采购金额', width: 30},
            {name: 'actPrice', label: '实际价格', width: 30},
            {name: 'totActPrice', label: '实际价格', width: 30},
            {name: 'actConvertQty', label: '已转换数量', width: 30},
            {name: 'convertQty', label: '本次转换数量',editable: true,editrules: {
                number: true,
                minValue: 1
            },width: 30},
            {name: 'convertquitQty', label: '本次撤销数量',editable: true, editrules: {
                number: true,
                minValue: 1
            },width: 30},
            {name: 'actConvertquitQty', label: '已撤销的数量', width: 40},
            {name: 'remark', label: '备注',editable: true, width: 30},

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
            debugger
            var rowData = $('#addDetailgrid').getRowData(rowid);
            if (cellname === "convertQty") {
                if((parseInt(rowData.qty) - parseInt(rowData.actConvertQty)) >= parseInt(rowData.convertQty) || rowData.convertQty == ""){
                    $('#addDetailgrid').editCell(iRow, iCol, true);
                }else{
                    $('#addDetailgrid').setCell(rowid, cellname, 0);
                    $('#addDetailgrid').editCell(iRow, iCol, true);
                    $.gritter.add({
                        text: "本次转换数量过多！",
                        class_name: 'gritter-success  gritter-light'
                    });
                }

            }
            if(cellname === "convertquitQty"){
                if(parseInt(rowData.qty)>parseInt(rowData.actConvertQty)){
                    $('#addDetailgrid').editCell(iRow, iCol, true);
                    var qty=$('#addDetailgrid').getCell(rowid, "qty");
                    var sum=parseInt(qty)-parseInt(value);
                    if(rowData.actConvertquitQty!=""&&rowData.actConvertquitQty!=undefined){
                        var sumall=parseInt(value)+parseInt(rowData.actConvertquitQty);
                    }else{
                        var sumall=parseInt(value)+0;
                    }

                    $('#addDetailgrid').setCell(rowid, "qty", sum);
                    $('#addDetailgrid').setCell(rowid, "actConvertquitQty", sumall);
                }else{
                    $('#addDetailgrid').setCell(rowid, cellname, 0);
                    $('#addDetailgrid').editCell(iRow, iCol, true);
                    $.gritter.add({
                        text: "本次撤销数量过多！",
                        class_name: 'gritter-success  gritter-light'
                    });
                }


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
    if ($("#search_busnissId").val() == "") {
        bootbox.alert("请添销售员！");
        return;
    }
    if ($("#search_buyahandId").val() == "") {
        bootbox.alert("请添买手！");
        return;
    }
    $("#modal-addDetail-table").modal('show').on('hidden.bs.modal', function () {
        $("#StyleSearchForm").resetForm();
        /* $("#stylegrid").clearGridData();*/
        $("#color_size_grid").clearGridData();
    });
}

function addProductInfo() {

    var addProductInfo = [];
    $('#color_size_grid').saveRow(editcolosizeRow);
    var styleRow = $("#stylegrid").getRowData($("#stylegrid").jqGrid("getGridParam", "selrow"));
    $.each($("#color_size_grid").getDataIDs(), function (index, value) {

        var productInfo = $("#color_size_grid").getRowData(value);
        if (productInfo.qty > 0) {
            productInfo.sku = productInfo.code;
            productInfo.inStockType = styleRow.class6;
            productInfo.buyahandId=$("#search_buyahandId").val();
            productInfo.actConvertQty=0;
            productInfo.convertQty=0;
            productInfo.convertquitQty=0;
            productInfo.actConvertquitQty=0;
            productInfo.remark="";
            productInfo.price = styleRow.preCast;
            productInfo.actPrice = productInfo.price;
            productInfo.totPrice = productInfo.qty * productInfo.price;
            productInfo.totActPrice = productInfo.qty * productInfo.actPrice;
            productInfo.buyahandName=$("#search_buyahandId").next().find("button").attr("title");

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
    var issaletype=$("#edit_replenishType input:radio[value='1']").is(':checked');
    var isreturntype=$("#edit_replenishType input:radio[value='0']").is(':checked');
    if(!issaletype&&!isreturntype){
        bootbox.alert("请选择补货类型！");
        return;
    }
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
function changebuy() {

    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var buyahandId=$("#search_buyahandId").val();
        var buyahandName=$("#search_buyahandId").next().find("button").attr("title");
        $('#addDetailgrid').setCell(value, "buyahandId", buyahandId);
        $('#addDetailgrid').setCell(value, "buyahandName", buyahandName);

    });
}

function saveAndAdd() {
    save();
    //location.href = basePath + "/logistics/relenishBill/index.do";
    location.href =  basePath + "/views/logistics/relenishBillDetail.jsp";
}
function check() {
    showWaitingPage();
    $.ajax({
        dataType: "json",
        async: false,
        url: basePath + "/logistics/relenishBill/checkReplenishBill.do",
        data: {
            replenishBillNo: $("#search_billNo").val(),
            remark: $("#form_remarks").val()

        },
        type: "POST",
        success: function (msg) {
            hideWaitingPage();

            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                closeEditDialog();
                $("#form_remarks").val("");
                $("#approvedCheck").show();
                $("#checkImage").attr("src",basePath+"/images/check/check.png");
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });


}

function noCheck() {
    showWaitingPage();
    $.ajax({
        dataType: "json",
        async: false,
        url: basePath + "/logistics/relenishBill/noCheckReplenishBill.do",
        data: {
            replenishBillNo: $("#search_billNo").val(),
            remark: $("#form_remarks").val()

        },
        type: "POST",
        success: function (msg) {
            hideWaitingPage();

            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                closeEditDialog();
                $("#form_remarks").val("");
                $("#approvedCheck").show();
                $("#checkImage").attr("src",basePath+"/images/check/nocheck.png");
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}
function showcheck(){
    //$("#editForm").resetForm();
    $("#checkSave").show();
    $("#noCheckSave").hide();
    $("#edit-dialog").modal('show');
}
function showNocheck() {
    //$("#editForm").resetForm();
    $("#checkSave").hide();
    $("#noCheckSave").show();
    $("#edit-dialog").modal('show');
}
function closeEditDialog() {
    $("#edit-dialog").modal('hide');
}

function changePurchase() {
    showWaitingPage();
    $.ajax({
        dataType: "json",
        async: false,
        url: basePath + "/logistics/relenishBill/changePurchase.do",
        data: {
            replenishBillNO:$("#search_billNo").val(),
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

                if(pageType=="add"){
                    $.each($("#addDetailgrid").getDataIDs(), function (dtlndex, dtlValue) {

                        var dtlRow = $("#addDetailgrid").getRowData(dtlndex);
                        dtlRow.actConvertQty= parseInt(dtlRow.actConvertQty)+parseInt(dtlRow.convertQty);
                        dtlRow.convertQty=0;
                        $("#addDetailgrid").setRowData(dtlndex, dtlRow);


                    });
                }else{
                    $.each($("#addDetailgrid").getDataIDs(), function (dtlndex, dtlValue) {

                        var dtlRow = $("#addDetailgrid").getRowData(dtlValue);
                        dtlRow.actConvertQty= parseInt(dtlRow.actConvertQty)+parseInt(dtlRow.convertQty);
                        dtlRow.convertQty=0;
                        $("#addDetailgrid").setRowData(dtlRow.id, dtlRow);


                    });
                }



            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}
