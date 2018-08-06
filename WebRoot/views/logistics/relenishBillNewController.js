var addDetailgridiRow;//存储iRow
var addDetailgridiCol;//存储iCol
var relenish_status;
$(function () {
    /*初始化左侧grig*/
    initSearchGrid();
    initForm();
    /*初始化右侧grig*/
    initAddGrid();
    initButtonGroup();
    //动态初始化页面
    loadingButtonDivTable(0);
});
function initForm() {
    pageType="add";
    initSelectBusinessIdForm();
    initSelectbuyahandIdEditForm();
    initSelectBusinessIdEditForm();
    $(".selectpicker").selectpicker('refresh');
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
        }
    });
}
function initSelectBusinessIdEditForm() {
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
            $("#edit_busnissId").empty();
            $("#edit_busnissId").append("<option value='' >--请选择销售员--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#edit_busnissId").append("<option value='" + json[i].id + "'>" + json[i].name + "</option>");
                // $("#search_busnissId").trigger('chosen:updated');
            }
            //$("#edit_busnissId").val(saleOrder_busnissId);
        }
    });
}

function initSelectbuyahandIdEditForm() {
    var url;

    url = basePath + "/sys/user/list.do?filter_EQS_roleId=BUYER";



    $.ajax({
        url: url,
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#edit_buyahandId").empty();
            $("#edit_buyahandId").append("<option value='' >--请选择买手--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#edit_buyahandId").append("<option value='" + json[i].id + "'>" + json[i].name + "</option>");
                // $("#search_busnissId").trigger('chosen:updated');
            }

            //$("#edit_buyahandId").val(saleOrder_buyahandId);
        }
    });
}
function initSearchGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url:basePath + "/logistics/relenishBill/page.do?filter_GTI_status=-1&userId="+userId,
        datatype: "json",
        mtype: 'POST',
        colModel: [
            {name: 'billNo', label: '单据编号', sortable: true, width: 45},
            {
                name: 'statusImg', label: '状态', width: 15, align: 'center',sortable: false,
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
                            html = "<i class='fa fa-archive blue' title='结束'></i>";
                            break;
                        case 3:
                            html = "<i class='fa fa-wrench blue' title='操作中'></i>";
                            break;
                        case 4:
                            html = "<i class='fa fa-paper-plane blue' title='申请撤销'></i>";
                            break;
                        case 6:
                            html = "<i class='fa fa-bars blue' title='商城订单暂存'></i>";
                            break;
                        case 7:
                            html = "<i class='fa fa-caret-square-o-down blue' title='商城订单录入'></i>";
                            break;
                        case 8:
                            html = "<i class='fa fa-check-square-o blue' title='反审核'></i>";
                            break;
                        default:
                            break;
                    }
                    return html;
                }
            },
            {name: 'status', hidden: true},
            {name: 'billDate', label: '单据日期', sortable: true, width: 35},
            {name: 'busnissId', label: '业务员Id', hidden: true},
            {name: 'busnissName', label: '业务员', width: 30},
            {name: 'totQty', label: '单据数量', width: 30},

            {name: 'remark', label: '备注', width: 50,hidden: true}
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
        sortorder: 'desc',
        autoScroll: false,
        footerrow: true,
        gridComplete: function () {
            //setFooterData();
        },
        onSelectRow: function (rowid, status) {
            var rowData = $('#grid').getRowData(rowid);
            findDetailData(rowData.billNo);
        }
    });
}
function initAddGrid() {
    $("#addDetailgrid").jqGrid({
        height: 'auto',
        datatype: "local",
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
function findDetailData(billNo) {
    $.ajax({
        dataType: "json",
        async: false,
        url: basePath + "/logistics/relenishBill/findPurchases.do",
        data: {
            billNo: billNo
        },
        type: "POST",
        success: function (msg) {
            if (msg.success) {
                initDetailData(msg.result);
            }
        }
    });
}
function initDetailData(rowData){
    $("#editForm").setFromData(rowData);
    relenish_status=rowData.status;

    $(".selectpicker").selectpicker('refresh');
    $('#addDetailgrid').jqGrid("clearGridData");
    $('#addDetailgrid').jqGrid('GridUnload');
    initeditGrid(rowData.billNo);
    pageType="edit";
    initButtonGroup();
    $("#addDetailgrid").trigger("reloadGrid");
    //加载审核图片
    if(relenish_status==1){
        $("#approvedCheck").show();
        $("#checkImage").attr("src",basePath+"/images/check/check.png");
        $("#REDtl_noCheck").show();
        $("#REDtl_changePurchase").show();

    }
    if(relenish_status==8){
        $("#approvedCheck").show();
        $("#checkImage").attr("src",basePath+"/images/check/nocheck.png");
        $("#REDtl_check").show();
        $("#REDtl_changePurchase").show();
    }
  /*  slaeOrder_status = rowData.status;
    if (slaeOrder_status != "0" && userId != "admin") {
        $("#edit_origId").attr('disabled', true);
        $("#edit_destId").attr('disabled', true);
        $("#edit_billDate").attr('readOnly', true);
        $("#edit_busnissId").attr('disabled', true);
    }
    if (userId == "admin") {
        $("#edit_guest_button").removeAttr("disabled");
        $("#edit_origId").attr('disabled', true);
        $("#edit_destId").attr('disabled', true);
    }
    $(".selectpicker").selectpicker('refresh');
    $('#addDetailgrid').jqGrid("clearGridData");
    $('#addDetailgrid').jqGrid('GridUnload');
    initeditGrid(rowData.billNo);
    pageType="edit";
    initButtonGroup(pageType);
    $("#addDetailgrid").trigger("reloadGrid");*/
    loadingButtonDivTable(relenish_status);

}
function initeditGrid(billNo) {
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
function initButtonGroup() {
    $("#buttonGroup").html("" +
        "<button id='REDtl_add' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='addNew()'>" +
        "    <i class='ace-icon fa fa-plus'></i>" +
        "    <span class='bigger-110'>新增</span>" +
        "</button>" +
        "<button id='REDtl_cancel' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='cancel()'>" +
        "    <i class='ace-icon fa fa-undo'></i>" +
        "    <span class='bigger-110'>撤销</span>" +
        "</button>" +
        "<button id='REDtl_save' type='button'  style='margin: 8px' class='btn btn-xs btn-primary' onclick='save()'>" +
        "    <i class='ace-icon fa fa-save'></i>" +
        "    <span class='bigger-110'>保存</span>" +
        "</button>"+
        "<button id='REDtl_saveAndAdd' type='button'  style='margin: 8px' class='btn btn-xs btn-primary' onclick='saveAndAdd()'>" +
        "    <i class='ace-icon fa fa-save'></i>" +
        "    <span class='bigger-110'>保存并新增</span>" +
        "</button>"+
        "<button id='REDtl_check' type='button'  style='margin: 8px' class='btn btn-xs btn-primary' onclick='showcheck()'>" +
        "    <i class='ace-icon fa fa-save'></i>" +
        "    <span class='bigger-110'>审核</span>" +
        "</button>"+
        "<button id='REDtl_noCheck' type='button'  style='margin: 8px' class='btn btn-xs btn-primary' onclick='showNocheck()'>" +
        "    <i class='ace-icon fa fa-save'></i>" +
        "    <span class='bigger-110'>反审核</span>" +
        "</button>"+
        "<button id='REDtl_changePurchase' type='button'  style='margin: 8px' class='btn btn-xs btn-primary' onclick='changePurchase()'>" +
        "    <i class='ace-icon fa fa-save'></i>" +
        "    <span class='bigger-110'>生成采购单</span>" +
        "</button>"+
        "<button id='REDtl_findPurcahse' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='findPurcahse()'>" +
        "    <i class='ace-icon fa fa-search'></i>" +
        "    <span class='bigger-110'>查找采购单</span>" +
        "</button>"
    );
    $("#REDtl_check").hide();
    $("#REDtl_noCheck").hide();
    $("#REDtl_changePurchase").hide();


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
    var issaletype=$("#edit_replenishType input:radio[value='1']").is(':checked');
    var isreturntype=$("#edit_replenishType input:radio[value='0']").is(':checked');
    if(!issaletype&&!isreturntype){
        bootbox.alert("请选择补货类型！");
        cs.closeProgressBar();
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
    $("#edit_billDate").val(updateTime($("#edit_billDate").val()));
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
                $("#edit_billNo").val(msg.result);
                $("#REDtl_check").show();
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}
function changebuy() {

    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var buyahandId=$("#edit_buyahandId").val();
        var buyahandName=$("#edit_buyahandId").next().find("button").attr("title");
        $('#addDetailgrid').setCell(value, "buyahandId", buyahandId);
        $('#addDetailgrid').setCell(value, "buyahandName", buyahandName);

    });
}
function saveAndAdd() {
    save();
    initForm();
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
function changePurchase() {
    save();
    // 验证参数
    var isok=true;
    if(pageType=="add"){
        $.each($("#addDetailgrid").getDataIDs(), function (dtlndex, dtlValue) {

            var dtlRow = $("#addDetailgrid").getRowData(dtlndex);

            var convertQty=dtlRow.convertQty;
            if(parseInt(convertQty)<=0){
                isok=false;
            }
        });
    }else{
        $.each($("#addDetailgrid").getDataIDs(), function (dtlndex, dtlValue) {

            var dtlRow = $("#addDetailgrid").getRowData(dtlValue);

            var convertQty=dtlRow.convertQty;
            if(parseInt(convertQty)<=0){
                isok=false;
            }
        });
    }
    if(!isok){
        bootbox.alert("请添写采购单数量！");
        return;
    }
    cs.showProgressBar();
    $.ajax({
        dataType: "json",
        async: false,
        url: basePath + "/logistics/relenishBill/changePurchase.do",
        data: {
            replenishBillNO:$("#edit_billNo").val(),
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
function findPurcahse() {
    $("#show-findPurchase-list").modal('show');
    initUniquePurchaseList();
}
function addDetail() {
    if ($("#edit_busnissId").val() == "") {
        bootbox.alert("请添销售员！");
        return;
    }
    if ($("#edit_buyahandId").val() == "") {
        bootbox.alert("请添买手！");
        return;
    }
    $("#modal-addDetail-table").modal('show').on('hidden.bs.modal', function () {
        $("#StyleSearchForm").resetForm();
        /* $("#stylegrid").clearGridData();*/
        $("#color_size_grid").clearGridData();
    });
}
function addProductInfo(status) {

    var addProductInfo = [];
    $('#color_size_grid').saveRow(editcolosizeRow);
    var styleRow = $("#stylegrid").getRowData($("#stylegrid").jqGrid("getGridParam", "selrow"));
    $.each($("#color_size_grid").getDataIDs(), function (index, value) {

        var productInfo = $("#color_size_grid").getRowData(value);
        if (productInfo.qty > 0) {
            productInfo.sku = productInfo.code;
            productInfo.inStockType = styleRow.class6;
            productInfo.buyahandId=$("#edit_buyahandId").val();
            productInfo.actConvertQty=0;
            productInfo.convertQty=0;
            productInfo.convertquitQty=0;
            productInfo.actConvertquitQty=0;
            productInfo.remark="";
            productInfo.price = styleRow.preCast;
            productInfo.actPrice = productInfo.price;
            productInfo.totPrice = productInfo.qty * productInfo.price;
            productInfo.totActPrice = productInfo.qty * productInfo.actPrice;
            productInfo.buyahandName=$("#edit_buyahandId").next().find("button").attr("title");

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
    if(status){
        $("#modal-addDetail-table").modal('hide');
    }
    setFooterData();

}
function check() {
    cs.showProgressBar();
    $.ajax({
        dataType: "json",
        async: false,
        url: basePath + "/logistics/relenishBill/checkReplenishBill.do",
        data: {
            replenishBillNo: $("#edit_billNo").val(),
            remark: $("#form_remarks").val()

        },
        type: "POST",
        success: function (msg) {
            cs.closeProgressBar();

            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                closeEditDialog();
                $("#form_remarks").val("");
                $("#approvedCheck").show();
                $("#checkImage").attr("src",basePath+"/images/check/check.png");
                $("#REDtl_noCheck").show();
                $("#REDtl_changePurchase").show();
                $("#REDtl_check").hide();
                $("#edit-dialog").modal('hide');
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });


}
function noCheck() {
    cs.showProgressBar();
    $.ajax({
        dataType: "json",
        async: false,
        url: basePath + "/logistics/relenishBill/noCheckReplenishBill.do",
        data: {
            replenishBillNo: $("#edit_billNo").val(),
            remark: $("#form_remarks").val()

        },
        type: "POST",
        success: function (msg) {
            cs.closeProgressBar();

            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                closeEditDialog();
                $("#form_remarks").val("");
                $("#approvedCheck").show();
                $("#checkImage").attr("src",basePath+"/images/check/nocheck.png");
                $("#REDtl_noCheck").hide();
                $("#REDtl_changePurchase").hide();
                $("#REDtl_check").show();
                $("#edit-dialog").modal('hide');
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}
function _search() {

    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        page: 1,
        url: basePath + "/logistics/relenishBill/page.do?filter_GTI_status=-1&userId="+userId,
        postData: params
    });
    $("#grid").trigger("reloadGrid");
}
function _resetForm(){
    $("#searchForm").clearForm();
    $("#search_busnissId").val();
    $(".selectpicker").selectpicker('refresh');
}
/**
 * 新增单据调用
 * isScan 是否调用扫码框
 * */
function addNew(){
    $('#addDetailgrid').jqGrid("clearGridData");
    $('#addDetailgrid').jqGrid('GridUnload');
    $("#editForm").clearForm();
    initAddGrid();
    initSelectbuyahandIdEditForm();
    initSelectBusinessIdEditForm();
    $("#addDetailgrid").trigger("reloadGrid");
    $(".selectpicker").selectpicker('refresh');
    pageType="add";
    initButtonGroup(pageType);

}

function cancel() {

    var billId= $("#edit_billNo").val();
    var status = $("#edit_status").val();
    if (status != "0") {
        bootbox.alert("不是录入状态，无法撤销");
        return;
    }
    if(billId == "" || billId == undefined){
        bootbox.alert("不是录入状态，无法撤销");
        return;
    }
    bootbox.confirm({
        /*title: "余额确认",*/
        buttons: {confirm: {label: '确定'}, cancel: {label: '取消'}},
        message: "撤销确定",
        callback: function (result) {
            /* $("#SODtl_save").removeAttr("disabled");*/
            if (result) {
                cancelAjax(billId);
                addNew()
            } else {
            }
        }
    });
}
function cancelAjax(billId) {
    $.ajax({
        dataType: "json",
        url: basePath + "/logistics/relenishBill/cancel.do",
        data: {billNo: billId},
        type: "POST",
        success: function (msg) {
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#grid").trigger("reloadGrid");

            } else {
                bootbox.alert(msg.msg);
            }
        }
    });

}
/**
 * billStatus 单据状态新增为0
 * 动态配置按钮,div,表格列字段
 * */
function loadingButtonDivTable(billStatus) {
    console.info(resourcePrivilege);
    var privilegeMap = ButtonAndDivPower(resourcePrivilege);
    $.each(privilegeMap['table'],function(index,value){
        if(value.isShow!=0) {
            $('#addDetailgrid').setGridParam().hideCol(value.privilegeId);
        }
    });
    var privilegeMap = ButtonAndDivPower(resourcePrivilege);
    $.each(privilegeMap['div'],function(index,value){
        if(value.isShow!=0) {
            debugger
            $("#"+value.privilegeId).hide();
        }
    });
    $.each(privilegeMap['button'],function(index,value){
        if(value.isShow!=0) {
            $("#"+value.privilegeId).hide();
        }
    });
    var disableButtonIds = "";
    switch (billStatus){
        case "-1" :
            disableButtonIds = ["REDtl_cancel","REDtl_save","REDtl_saveAndAdd","REDtl_check","REDtl_noCheck","REDtl_changePurchase","REDtl_findPurcahse"];
            break;
        case "0" :
            disableButtonIds = [];
            break;
        case "1":
            disableButtonIds = ["REDtl_cancel","REDtl_save"];
            break;
        case "2" :
            disableButtonIds = ["REDtl_cancel","REDtl_save","REDtl_saveAndAdd","REDtl_check","REDtl_noCheck","REDtl_changePurchase","REDtl_findPurcahse"];
            break;
        case "3":
            disableButtonIds = ["REDtl_cancel","REDtl_save"];
            break;
        default:
            disableButtonIds = ["TRDtl_wareHouseIn"];
    }
    //根据单据状态disable按钮
    $.each(privilegeMap['button'],function(index,value){
        //找对应的按钮
        if($.inArray(value.privilegeId,disableButtonIds)!= -1){
            $("#"+value.privilegeId).attr({"disabled": "disabled"});
        }else{
            $("#"+value.privilegeId).removeAttr("disabled");
        }
    });
    $("#REDtl_check").hide();
    $("#REDtl_noCheck").hide();
    $("#REDtl_changePurchase").hide();
}
