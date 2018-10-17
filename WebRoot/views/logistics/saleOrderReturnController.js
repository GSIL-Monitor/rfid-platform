var searchUrl =  basePath + "/logistics/saleOrderReturn/page.do?filter_GTI_status=-1&userId=" + userId;
var autoSelect =false;//是否自动选中
var addDetailgridiRow;//存储iRow
var addDetailgridiCol;//存储iCol
var taskType; //用于判断出入库类型 1入库 0 出库
var wareHouse;
var isCheckWareHouse=false;//是否检测出库仓库
var slaeOrderReturn_customerType;
var allCodeStrInDtl;
var allCodes;
$(function (){
    load().then(function (data) {
        /*初始化左侧grig*/
        initSearchGrid();
    });
    $("#edit_payType").selectpicker('val',defaultPayType);
});

function load() {
    var promise = new Promise(function(resolve, reject){
        initSearchAndEditForm();
        /*初始化右侧grig*/
        initAddGrid();
        initButtonGroup(0);
        loadingButtonDivTable(0);
        initEditFormValid();
        setEditFormVal();
        /*回车监事件*/
        keydown();
        addProduct_keydown();
        input_keydown();
        //动态加载按钮
        $("#SRDtl_check").hide();
        resolve("success");
    });
    return promise;
}

/*
 *@param preId id前缀 search/edit 区分回调框id
 **/
function openSearchGuestDialog(preId) {
    dialogOpenPage = "saleOrderReturn";
    prefixId =preId;
    $("#modal_guest_search_table").modal('show').on('shown.bs.modal', function () {
        initGuestSelect_Grid();
    });
    console.log(prefixId);
    $("#searchGuestDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='confirm_selected_GuestId_saleReturn()'>确认</button>"
    );
}
function initSelectDestForm() {
    var url;
    if (userId == "admin") {
        url=basePath + "/unit/list.do?filter_EQI_type=9";
    }else{
        url=basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + curOwnerId
    }
    $.ajax({
        url: url,
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_destId").empty();
            $("#search_destId").append("<option value=''>--请选择--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_destId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");

            }
        }
    });


}
function initSelectOrigForm() {
    var searchOrigIdUrl="";
    if (userId == "admin") {
        searchOrigIdUrl=basePath + "/unit/list.do?filter_EQI_type=9";
    } else {
        searchOrigIdUrl=basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + curOwnerId;
    }
    $.ajax({
        url: searchOrigIdUrl,
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_origId").empty();
            $("#search_origId").append("<option value=''>--请选择--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_origId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");

            }
            console.log($("#search_origId").val())
        }
    });

}
function initSelectOrigEditForm() {
    if (userId == "admin") {
        $.ajax({
            url: basePath + "/unit/list.do?filter_EQI_type=9",
            cache: false,
            async: false,
            type: "POST",
            success: function (data, textStatus) {
                $("#edit_origId").empty();
                $("#edit_origId").append("<option value=''>--请选择出库仓库--</option>");
                var json = data;
                for (var i = 0; i < json.length; i++) {
                    $("#edit_origId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                    $("#edit_origId").selectpicker('refresh');
                }
            }
        });
    } else {
        $.ajax({
            url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + $("#edit_origUnitId").val(),
            cache: false,
            async: false,
            type: "POST",
            success: function (data, textStatus) {
                $("#edit_origId").empty();
                $("#edit_origId").append("<option value=''>--请选择出库仓库--</option>");
                var json = data;
                for (var i = 0; i < json.length; i++) {
                    $("#edit_origId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                    $("#edit_origId").selectpicker('refresh');
                }
            }
        });
    }

}

function initSelectDestEditForm() {
    if (userId == "admin") {
        $.ajax({
            url: basePath + "/unit/list.do?filter_EQI_type=9",
            cache: false,
            async: false,
            type: "POST",
            success: function (data, textStatus) {
                $("#edit_destId").empty();
                $("#edit_destId").append("<option value='' style='background-color: #eeeeee'>--请选择入库仓库--</option>");
                var json = data;
                for (var i = 0; i < json.length; i++) {
                    $("#edit_destId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                    $("#edit_destId").selectpicker('refresh');
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
                $("#edit_destId").empty();
                $("#edit_destId").append("<option value='' style='background-color: #eeeeee'>--请选择入库仓库--</option>");
                var json = data;
                for (var i = 0; i < json.length; i++) {
                    $("#edit_destId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                    $("#edit_destId").selectpicker('refresh');
                }
            }
        });
    }


}
function initSearchAndEditForm(){
    initSelectDestForm();
    initSelectOrigForm();
    initSelectDestEditForm();
    initSelectOrigEditForm();
    initSelectBusinessIdForm();
    initCustomerTypeForm();
    $(".selectpicker").selectpicker('refresh');
}
function initSearchGrid() {
    var url="";
    if (cargoTrack=="cargoTracking"){
        url= basePath + "/logistics/saleOrderReturn/findBill.do?billNo="+cTbillNo;
    }else {
        url = basePath +  "/logistics/saleOrderReturn/page.do?filter_GTI_status=-1&userId=" + userId;
    }
    $("#grid").jqGrid({
        height: 'auto',
        datatype: 'json',
        url:url,
        mtype: 'POST',
        colModel: [
            {name: 'billDate', label: '单据日期', width: 40},

            {name: 'billNo', label: "单号", width: 45, sortable: true,hidden:true},
            {name: "status", hidden: true},
            {name: "ownerId", hidden: true},
            {name: 'outStatus', label: '出库状态', hidden: true},
            {name: 'inStatus', label: '入库状态', hidden: true},
            {name: 'payType' , label: '支付方式', hidden : true},
            {
                name: '', label: '状态', width: 15, align: "center", sortable: false,
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
                        default:
                            break;
                    }
                    return html;
                }

            },
            {
                name: 'outStatusImg', label: '出库状态', width: 15, align: 'center', sortable: false, hidden: true,
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
            {
                name: 'inStatusImg', label: '入库状态', width: 15, align: 'center', sortable: false, hidden: true,
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

            {name: 'customerType', label: "客户类型", width: 30, hidden: true},
            {name: 'customerTypeName', label: "客户类型", width: 30, hidden: true},
            {
                label: "客户类型", width: 40, hidden: true,
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.customerType == "CT-AT")
                        return "省代客户";
                    else if (rowObject.customerType == "CT-ST")
                        return "门店客户";
                    else if (rowObject.customerType == "CT-LS")
                        return "零售客户"
                }
            },
            {name: 'origUnitId', label: '退货客户ID', hidden: true},
            {name: 'origUnitName', label: '退货客户', width: 30},
            {name: 'origId', label: '出库仓库ID', hidden: true},
            {name: 'origName', label: '出库仓库', width: 30, hidden: true},
            {name: 'destId', label: '收货仓库ID', hidden: true},
            {name: 'destName', label: '收货仓库', width: 30, hidden: true},
            {name: 'destUnitId', label: '收货方ID', hidden: true},
            {name: 'destUnitName', label: '收货方', width: 30, hidden: true},
            {name: 'totQty', label: '单据数量', width: 20,align: "center"},
            {name: 'totOutQty', label: '已出库数量', width: 30, hidden: true},
            // {name: 'totOutVal', label: '总出库金额', width: 30},
            {name: 'totInQty', label: '已入库数量', width: 20, align: "center"},
            // {name: 'totInVal', label: '总入库金额', width: 30},
            {name: 'actPrice', label: '应付付金额', width: 30,
                formatter: function (cellValue, options, rowObject) {
                    if(cellValue) {
                        var actPrice = cellValue.toFixed(2);
                        return actPrice;
                    }else{
                        return cellValue;
                    }
                }
            },
            {name: 'payPrice', label: '实付金额', width: 30,
                formatter: function (cellValue, options, rowObject) {
                    if(cellValue) {
                        var payPrice = cellValue.toFixed(2);
                        return payPrice;
                    }else{
                        return cellValue;
                    }
                }
            },
            {name: 'srcBillNo', label: '原始单号', hidden: true},
            {name: 'discount', label: '折扣', hidden: true},
            {name: 'busnissId', label: '销售员', hidden: true},

            {name: 'preBalance', label: '售前余额', hidden: true},
            {name: 'afterBalance', label: '售后余额', hidden: true},
            {name: 'busnissName', label: '销售员',width: 30},
            {name: 'remark', label: '备注', width:30}
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
            setFooterData();
            if(autoSelect){
                var rowIds = $("#grid").getDataIDs();
                $("#grid").setSelection(rowIds[0]);
                autoSelect =false;
            }
        },
        onSelectRow: function (rowid, status) {
            initDetailData(rowid)
        },
        loadComplete:function () {
            if (cargoTrack=="cargoTracking"){
                initDetailData(cTbillNo);
                $("#search_billId").val(cTbillNo);
            }
        }
    });
}
function initDetailData(rowid){
    $("#myTab li").eq(0).find("a").click();
    var rowData = $("#grid").getRowData(rowid);
    $("#editForm").setFromData(rowData);
    slaeOrderReturn_status = rowData.status;
    slaeOrderReturn_customerType=rowData.customerType;
    $("#edit_guest_button").removeAttr("disabled");
    $("#edit_origId").attr('disabled', true);
    $("#edit_destId").attr('disabled', true);
    $(".selectpicker").selectpicker('refresh');
    $('#addDetailgrid').jqGrid("clearGridData");
    $('#addDetailgrid').jqGrid('GridUnload');
    initeditGrid(rowData.billNo);
    $("#addDetailgrid").trigger("reloadGrid");
    $('#codegrid').jqGrid("clearGridData");
    $('#codegrid').jqGrid('GridUnload');
    initCodeGrid({billNo: rowData.billNo, warehId: rowData.origId});
    $("#codegrid").trigger("reloadGrid");
    initButtonGroup(slaeOrderReturn_status);
    if(userId == 'admin'){
        $("#SRDtl_save").attr('disabled', false);
    }
}

function initeditGrid(billId) {
    var billNo = billId;
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
                name: "", label: '操作', width: 30, align: 'center', sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    if(rowObject.status == '0'&&slaeOrderReturn_status == '2'){
                        return "<a href='javascript:void(0);'><i class='ace-icon ace-icon fa fa-save' title='保存'></i></a>"
                            + "<a href='javascript:void(0);' style='margin-left: 20px'><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";
                    }
                    else{
                        return "<a href='javascript:void(0);' onclick=saveItem('" + options.rowId + ")'><i class='ace-icon ace-icon fa fa-save' title='保存'></i></a>"
                            + "<a href='javascript:void(0);' style='margin-left: 20px'  onclick=deleteItem('" + options.rowId + "')><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";
                    }
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

            {name: 'styleId', label: '款号', width: 40,hidden:true,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'styleName', label: '款式', width: 40,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'colorId', label: '色号', width: 40,hidden:true,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'colorName', label: '颜色', width: 30,hidden:true,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'sizeId', label: '尺码', width: 30,hidden:true,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'sizeName', label: '尺寸', width: 40,hidden:true,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {
                name: 'qty', label: '数量', width: 40, editable: true,
                editrules: {
                    number: true,
                    minValue: 1
                },
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'outQty', label: '已出库数量', width: 40,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'inQty', label: '已入库数量', width: 40,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'sku', label: 'sku', width: 50,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {
                name: 'price', label: '销售价格', width: 40,
                formatter: function (cellValue, options, rowObject) {
                    var price = parseFloat(cellValue).toFixed(2);
                    return price;
                },
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {
                name: 'totPrice', label: '销售金额', width: 40,
                formatter: function (cellValue, options, rowObject) {
                    var totPrice = parseFloat(cellValue).toFixed(2);
                    return totPrice;
                },
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'discount', label: '折扣', width: 40, editable: true,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {
                name: 'actPrice', label: '实际价格', editable: true, width: 40,
                editrules: {
                    number: true
                },
                formatter: function (cellValue, options, rowObject) {
                    var actPrice = parseFloat(cellValue).toFixed(2);
                    return actPrice;
                },
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {
                name: 'totActPrice', label: '实际金额', width: 40,
                formatter: function (cellValue, options, rowObject) {
                    var totActPrice = parseFloat(cellValue).toFixed(2);
                    return totActPrice;
                },
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'uniqueCodes', label: '唯一码',hidden:true},
            {
                name: '', label: '唯一码明细', width: 40, align: "center",
                formatter: function (cellValue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=showCodesDetail('" + rowObject.uniqueCodes + "')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
                }
            },
            {
                name: '', label: '异常唯一码明细', width: 40, align: "center",
                formatter: function (cellValue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=showCodesDetail('" + rowObject.noOutPutCode + "')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
                }
            },
            {name:'stylePriceMap',label:'价格表',hidden:true},
            {name: 'noOutPutCode', label: '异常唯一码', hidden: true}
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
                //判断实际价格是不是小于门店批发价格
                var var_actPrice;
                var stylePriceMap=JSON.parse($('#addDetailgrid').getCell(rowid, "stylePriceMap"));
                if(userId = 'admin'){
                    var var_actPrice = Math.round(value * $('#addDetailgrid').getCell(rowid, "price")) / 100;
                    var var_totActPrice = -Math.abs(Math.round(var_actPrice * $('#addDetailgrid').getCell(rowid, "qty") * 100) / 100);
                    $('#addDetailgrid').setCell(rowid, "actPrice", var_actPrice);
                    $('#addDetailgrid').setCell(rowid, "totActPrice", var_totActPrice);
                }
                else {
                    if((value*$('#addDetailgrid').getCell(rowid, "price")/100)>stylePriceMap.wsPrice && isUserAbnormal){
                        $('#addDetailgrid').setCell(rowid, "discount", (stylePriceMap.wsPrice/$('#addDetailgrid').getCell(rowid, "price")).toFixed(2)*100);
                        var_actPrice =  stylePriceMap.wsPrice;
                        $('#addDetailgrid').setCell(rowid, "actPrice", stylePriceMap.wsPrice);
                        $('#addDetailgrid').setCell(rowid, "abnormalStatus",1);
                        changeWordscolor(rowid,"blue");
                    }else{
                        $('#addDetailgrid').setCell(rowid, "discount", value);
                        var_actPrice = Math.round(value * $('#addDetailgrid').getCell(rowid, "price")) / 100;
                        $('#addDetailgrid').setCell(rowid, "actPrice", var_actPrice);
                        $('#addDetailgrid').setCell(rowid, "abnormalStatus", 0);
                        changeWordscolor(rowid,"black");
                    }

                    var var_totActPrice = -Math.round(var_actPrice * $('#addDetailgrid').getCell(rowid, "qty") * 100) / 100;
                    $('#addDetailgrid').setCell(rowid, "totActPrice", var_totActPrice);
                }

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
            setAddFooterData();
        },
        gridComplete: function () {
            setAddFooterData();
        },
        loadComplete: function () {
            initAllCodesList();
        }
    });

    if (pageType === "edit" &&  $("#edit_status").val()!== "0" || $("#edit_BillNo").val() !== "") {
        $("#addDetailgrid").setGridParam().hideCol("operation");
    } else {
        $("#addDetailgrid").setGridParam().showCol("operation");
        if (userId!="admin"){
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
    }
    $("#addDetailgrid-pager_center").html("");

}
function changeWordscolor(value,color) {
    $("#addDetailgrid").setCell(value,"styleId",$('#addDetailgrid').getCell(value, "styleId"),{color:color});
    $("#addDetailgrid").setCell(value,"styleName",$('#addDetailgrid').getCell(value, "styleName"),{color:color});
    $("#addDetailgrid").setCell(value,"colorId",$('#addDetailgrid').getCell(value, "colorId"),{color:color});
    $("#addDetailgrid").setCell(value,"colorName",$('#addDetailgrid').getCell(value, "colorName"),{color:color});
    $("#addDetailgrid").setCell(value,"sizeId",$('#addDetailgrid').getCell(value, "sizeId"),{color:color});
    $("#addDetailgrid").setCell(value,"sizeName",$('#addDetailgrid').getCell(value, "sizeName"),{color:color});
    $("#addDetailgrid").setCell(value,"qty",$('#addDetailgrid').getCell(value, "qty"),{color:color});
    $("#addDetailgrid").setCell(value,"returnQty",$('#addDetailgrid').getCell(value, "returnQty"),{color:color});
    $("#addDetailgrid").setCell(value,"outQty",$('#addDetailgrid').getCell(value, "outQty"),{color:color});
    $("#addDetailgrid").setCell(value,"inQty",$('#addDetailgrid').getCell(value, "inQty"),{color:color});
    $("#addDetailgrid").setCell(value,"sku",$('#addDetailgrid').getCell(value, "sku"),{color:color});
    $("#addDetailgrid").setCell(value,"price",$('#addDetailgrid').getCell(value, "price"),{color:color});
    $("#addDetailgrid").setCell(value,"totPrice",$('#addDetailgrid').getCell(value, "totPrice"),{color:color});
    $("#addDetailgrid").setCell(value,"discount",$('#addDetailgrid').getCell(value, "discount"),{color:color});
    $("#addDetailgrid").setCell(value,"actPrice",$('#addDetailgrid').getCell(value, "actPrice"),{color:color});
    $("#addDetailgrid").setCell(value,"totActPrice",$('#addDetailgrid').getCell(value, "totActPrice"),{color:color});
}
function setFooterData() {

    var sum_totQty = $("#grid").getCol('totQty', false, 'sum');
    $("#grid").footerData('set', {
        billDate: "合计",
        totQty: sum_totQty
    });
    var sum_totInQty = $("#grid").getCol('totInQty', false, 'sum');
    $("#grid").footerData('set', {
        billDate: "合计",
        totInQty: sum_totInQty
    });
    var sun_actPrice = $("#grid").getCol('actPrice', false, 'sum');
    $("#grid").footerData('set', {
        billDate: "合计",
        actPrice: sun_actPrice
    });
    var sun_payPrice = $("#grid").getCol('payPrice', false, 'sum');
    $("#grid").footerData('set', {
        billDate: "合计",
        payPrice: sun_payPrice
    });
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

function _resetForm(){
    $("#searchForm").clearForm();
    $("#search_destId").val();
    $("#search_origId").val();
    $("#filter_INI_outStatus").val();
    $("#filter_INI_inStatus").val();
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
            $("#edit_busnissId").empty();
            $("#edit_busnissId").append("<option value='' >--请选择--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#edit_busnissId").append("<option value='" + json[i].id + "'>" + json[i].name + "</option>");
            }

            if (defaultSaleStaffId != "" && defaultSaleStaffId != undefined) {
                $("#edit_busnissId").val(defaultSaleStaffId);
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
            $("#edit_customerType").empty();
            $("#edit_customerType").append("<option value=''>--请选择--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#edit_customerType").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
            }
            $(".selectpicker").selectpicker('refresh');
            if (defalutCustomerId != "" && defalutCustomerId != undefined) {
                $("#edit_customerType").selectpicker('val',"CT-LS");
            }
        }
    });
}
/*根据权限初始化按钮*/
function initButtonGroup(type){
    if (type===0){
        $("#search_guest_button").removeAttr("disabled");
        $("#edit_billDate").val(getToDay("yyyy-MM-dd"));
    }
        $("#buttonGroup").html("" +
            "<button id='SRDtl_add' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='addNew()'>" +
            "    <i class='ace-icon fa fa-plus'></i>" +
            "    <span class='bigger-110'>新增</span>" +
            "</button>" +
            "<button id='SRDtl_cancel' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='cancel()'>" +
            "    <i class='ace-icon fa fa-undo'></i>" +
            "    <span class='bigger-110'>撤销</span>" +
            "</button>" +
            "<button id='SRDtl_save' type='button' style='margin: 8px'  class='btn btn-xs btn-primary' onclick='save()'>" +
            "    <i class='ace-icon fa fa-save'></i>" +
            "    <span class='bigger-110'>保存</span>" +
            "</button>" +
            "<button id='SRDtl_addUniqCode' type='button' style='margin: 8px'  class='btn btn-xs btn-primary' onclick='addUniqCode()'>" +
            "    <i class='ace-icon fa fa-barcode'></i>" +
            "    <span class='bigger-110'>扫码</span>" +
            "</button>" +
            "<button id='SRDtl_wareHouseOut' type='button' style='margin: 8px'  class='btn btn-xs btn-primary' onclick='wareHouseInOut(" + "\"out\"" + ")'>" +
            "    <i class='ace-icon fa fa-sign-out'></i>" +
            "    <span class='bigger-110'>出库</span>" +
            "</button>" +
            "<button id='SRDtl_wareHouseIn_noOutHouse' type='button' style='margin: 8px'  class='btn btn-xs btn-primary' onclick='wareHouseInOut(" + "\"in\"" + ")'>" +
            "    <i class='ace-icon fa fa-sign-in'></i>" +
            "    <span class='bigger-110'>入库</span>" +
            "</button>" +
            "<button id='SRDtl_wareHouseIn' type='button' style='margin: 8px'  class='btn btn-xs btn-primary' onclick='edit_wareHouseIn()'>" +
            "    <i class='ace-icon fa fa-sign-in'></i>" +
            "    <span class='bigger-110'>入库</span>" +
            "</button>" +
            "<button id='SRDtl_doPrint' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='doPrint()'>" +
            "    <i class='ace-icon fa fa-print'></i>" +
            "    <span class='bigger-110'>打印</span>" +
            "</button>"+
            "<button id='SRDtl_check' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='checkAjax()'>" +
            "    <i class='ace-icon fa fa-check-square-o'></i>" +
            "    <span class='bigger-110'>审核</span>" +
            "</button>"+
            "<button id='SRDtl_doPrintSanLian' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='doPrintSanLian()'>" +
            "    <i class='ace-icon fa fa-print'></i>" +
            "    <span class='bigger-110'>三联打印</span>" +
            "</button>"+
            "<button id='SRDtl_doPrintA4' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='doPrintA4()'>" +
            "    <i class='ace-icon fa fa-print'></i>" +
            "    <span class='bigger-110'>A4打印</span>" +
            "</button>"
        );
        loadingButtonDivTable(type);

}
//code明细

function initCodeGrid(parameters) {
    var billNo = parameters.billNo;
    var warehId = parameters.warehId;
    $("#codegrid").jqGrid({
        height: 'auto',
        url: basePath + "/logistics/saleOrderReturn/findCodeSaleReturnList.do?billNo=" + billNo+"&warehId="+warehId,
        datatype: "json",
        colModel: [
            {
                name: '', label: '操作', width: 40,
                formatter: function (cellValue, options, rowObject) {
                    return "<a style='margin-left: 10px' href='javascript:void(0);' onclick=deleteUniqueCode('" + options.rowId + "')><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";
                }
            },
            {name: 'code', label: '唯一码', width: 160},
            {name: 'updateTime', label: '修改时间', hidden: true},
            {name: 'styleId', label: '款号', hidden: true},
            {name: 'colorId', label: '色码', hidden: true},
            {name: 'sizeId', label: '尺码', hidden: true},
            {name: 'sku', label: 'SKU', width: 180},
            {name: 'styleName', label: '款式', hidden: true},
            {name: 'colorName', label: '颜色', hidden: true},
            {name: 'sizeName', label: '尺寸', hidden: true},
            {name: 'price', label: '销售价格', width: 160},                  //吊牌价格
            {name: 'preCast', label: '采购价', hidden: true},  //事前成本价(采购价)
            {name: 'wsPrice', label: '销售价格', hidden: true},  //门店批发价格
            {name: 'puPrice', label: '销售价格', hidden: true},  //代理商批发价格
            {name: 'stockPrice', label: '库存金额', hidden: true}, //库存金额
            /* add by Anna */
            {name: 'originBillNo', label: '原始单号', width: 220},
            {name: 'lastSaleTime', label: '最后销售时间', width: 220},
            {name: 'saleCycle', label: '销售周期', width: 130, cellattr: addCellAttr} //销售周期（开单当天时间－销售单时间）
        ],
        viewrecords: true,
        rownumbers: true,
        altRows: true,
        rowNum: -1,
        multiselect: false,
        shrinkToFit: true,
        sortname: 'id',
        sortorder: "desc"
    });
}

function addCellAttr(rowId, val, rawObject, cm, rdata) {
    if (rawObject.saleCycle >= 20) {
        return "style='color:red'";
    }
}

//删除不能退货的
function deleteUniqueCode(rowId){
    var row = $('#codegrid').getRowData(rowId);
    $("#codegrid").jqGrid("delRowData", rowId);
}

//sku明细
function initAddGrid() {
    $("#addDetailgrid").jqGrid({
        height: "auto",
        datatype: "local",
        colModel: [
            {name: 'id', label: 'id', hidden: true},
            {name: 'billId', label: 'billId', hidden: true},
            {name: 'billNo', label: 'billNo', hidden: true},
            {name: 'status', hidden: true},
            {name: 'inStatus', hidden: true},
            {name: 'outStatus', hidden: true},
            {
                name: "", label: '操作', width: 30, align: 'center', sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    if(rowObject.status != '0'&&slaeOrderReturn_status == '2'){
                        return "<a href='javascript:void(0);'><i class='ace-icon ace-icon fa fa-save' title='保存'></i></a>"
                            + "<a href='javascript:void(0);' style='margin-left: 20px'><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";
                    }
                    else{
                        return "<a href='javascript:void(0);' onclick=saveItem('" + options.rowId + ")'><i class='ace-icon ace-icon fa fa-save' title='保存'></i></a>"
                            + "<a href='javascript:void(0);' style='margin-left: 20px'  onclick=deleteItem('" + options.rowId + "')><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";
                    }
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

            {name: 'styleId', label: '款号', width: 40,hidden:true,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'styleName', label: '款式', width: 40,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'colorId', label: '色号', width: 40,hidden:true,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'colorName', label: '颜色', width: 30,hidden:true,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'sizeId', label: '尺码', width: 30,hidden:true,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'sizeName', label: '尺寸', width: 40,hidden:true,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {
                name: 'qty', label: '数量', width: 40, editable: true,
                editrules: {
                    number: true,
                    minValue: 1
                },
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'outQty', label: '已出库数量', width: 40,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'inQty', label: '已入库数量', width: 40,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'sku', label: 'sku', width: 50,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {
                name: 'price', label: '销售价格', width: 40,
                formatter: function (cellValue, options, rowObject) {
                    var price = parseFloat(cellValue).toFixed(2);
                    return price;
                },
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {
                name: 'totPrice', label: '销售金额', width: 40,
                formatter: function (cellValue, options, rowObject) {
                    var totPrice = parseFloat(cellValue).toFixed(2);
                    return totPrice;
                },
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
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
                },
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {
                name: 'totActPrice', label: '实际金额', width: 40,
                formatter: function (cellValue, options, rowObject) {
                    var totActPrice = parseFloat(cellValue).toFixed(2);
                    return totActPrice;
                },
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'uniqueCodes', label: '唯一码',hidden:true},
            {
                name: '', label: '唯一码明细', width: 40, align: "center",
                formatter: function (cellValue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=showCodesDetail('" + rowObject.uniqueCodes + "')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
                }
            },
            {name:'stylePriceMap',label:'价格表',hidden:true},
            {name: 'noOutPutCode', label: '异常唯一码'}
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
            setAddFooterData();
        },
        gridComplete: function () {
            setAddFooterData();
        },
        loadComplete: function () {
            initAllCodesList();
        }
    });

    $("#addDetailgrid").setGridParam().showCol("operation");
    if (userId!="admin"){
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
function edit_discount_onblur() {
    setDiscount();
}

//将整单折扣设置到明细中
function setDiscount() {

    if (addDetailgridiRow != null && addDetailgridiCol != null) {
        $("#addDetailgrid").saveCell(addDetailgridiRow, addDetailgridiCol);
        addDetailgridiRow = null;
        addDetailgridiCol = null;
    }
    var discount = $("#edit_discount").val();
    if (discount && discount != null && discount != "") {
        if(userId == 'admin') {
            $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
                $('#addDetailgrid').setCell(value, "discount", discount);
                var var_actPrice = Math.round(discount * $('#addDetailgrid').getCell(value, "price")) / 100;
                var var_totActPrice = -Math.abs(Math.round(var_actPrice * $('#addDetailgrid').getCell(value, "qty") * 100) / 100);
                $('#addDetailgrid').setCell(value, "actPrice", var_actPrice);
                $('#addDetailgrid').setCell(value, "totActPrice", var_totActPrice);
            });
        }
        else {
            $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
                //判断实际价格是不是小于门店批发价格
                var var_actPrice;
                var stylePriceMap=JSON.parse($('#addDetailgrid').getCell(value, "stylePriceMap"));
                if((discount*$('#addDetailgrid').getCell(value, "price")/100)<stylePriceMap.wsPrice&&isUserAbnormal){
                    $('#addDetailgrid').setCell(value, "discount", (stylePriceMap.wsPrice/$('#addDetailgrid').getCell(value, "price")).toFixed(2)*100);
                    var_actPrice = stylePriceMap.wsPrice;
                    $('#addDetailgrid').setCell(value, "actPrice", stylePriceMap.wsPrice);
                    $('#addDetailgrid').setCell(value, "abnormalStatus",1);
                    changeWordscolor(value,"blue");
                }else{
                    $('#addDetailgrid').setCell(value, "discount", discount);
                    var_actPrice = Math.round(discount * $('#addDetailgrid').getCell(value, "price")) / 100;
                    $('#addDetailgrid').setCell(value, "actPrice", var_actPrice);
                    $('#addDetailgrid').setCell(value, "abnormalStatus", 0);
                    changeWordscolor(value,"black");
                }
                var var_totActPrice = -Math.round(var_actPrice * parseInt($('#addDetailgrid').getCell(value, "qty")) * 100) / 100;
                $('#addDetailgrid').setCell(value, "totActPrice", var_totActPrice);
                $("#grid-table").setCell(value,"useable",0,{color:'red'});
            });
        }
    }
    setAddFooterData();
}
function save() {
    cs.showProgressBar();
    $("#edit_customerType").removeAttr('disabled');
    $("#edit_origId").removeAttr('disabled');
    $("#edit_destId").removeAttr('disabled');
    $("#edit_busnissId").removeAttr('disabled');
    //寄存单转的可以相同单位保存
    var ttt=$("#edit_srcBillNo").val();
    if ($("#edit_srcBillNo").val()===""){
        if ($("#edit_origId").val()==$("#edit_destId").val()) {
            bootbox.alert("不能在相同的单位之间做销售退货");
            cs.closeProgressBar();
            return;
        }
    }
    if(pageType=="edit"){
        if(slaeOrderReturn_customerType!=$("#edit_customerType").val()){
            bootbox.alert("客户类型不相同");
            cs.closeProgressBar();
            return;
        }
    }
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

    if (addDetailgridiRow != null && addDetailgridiCol != null) {
        $("#addDetailgrid").saveCell(addDetailgridiRow, addDetailgridiCol);
        addDetailgridiRow = null;
        addDetailgridiCol = null;
    }
    $("#edit_billDate").val(updateTime($("#edit_billDate").val()));
    var totActPrice = $("#edit_actPrice").val();
    var payPrice = $("#edit_payPrice").val();
    if (parseFloat(payPrice) < 0) {
        var summun = parseFloat(payPrice) - parseFloat(totActPrice);
        if (summun < 0) {
            $("#edit_payPrice").val(summun.toFixed(2));
        }
    }
    var actPrice = parseFloat($("#edit_actPrice").val());
    var payPrice = parseFloat($("#edit_payPrice").val());
    var preBalance = parseFloat($("#edit_pre_Balance").val());
    var afterBalance = parseFloat(preBalance + payPrice - actPrice).toFixed(2);
    $("#edit_after_Balance").val(afterBalance);
    var purchaseReturnBill = JSON.stringify(array2obj($("#editForm").serializeArray()));
    var dtlArray = [];
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        dtlArray.push(rowData);
    });

    //将客户传回去

    $.ajax({
        dataType: "json",
        async: true,
        url: basePath + "/logistics/saleOrderReturn/save.do",
        data: {
            'bill': purchaseReturnBill,
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
                $("#editForm").setFromData(msg.result);
                billNo = msg.result;
                $("#addDetailgrid").jqGrid('setGridParam', {
                    page: 1,
                    url: basePath + "/logistics/saleOrderReturn/returnDetails.do?billNo=" + msg.result
                });
                $("#addDetailgrid").trigger("reloadGrid");
                $("#grid").trigger("reloadGrid");
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
    $("#edit_customerType").attr({"disabled": "disabled"});
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
                        message: '折扣只能只能为0-200之间的数字'
                    },
                    callback: {
                        message: '折扣只能只能为0-200之间的数字',
                        callback: function (value, validator) {
                            if (parseInt(value) < 0 || parseInt(value) > 200) {
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
/*添加订货商品*/
function addDetail() {

    var ct = $("#edit_customerType").val();
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
/*选中订货商品添加*/
function addProductInfo(status) {

    if (addDetailgridiRow != null && addDetailgridiCol != null) {
        $("#addDetailgrid").saveCell(addDetailgridiRow, addDetailgridiCol);
        addDetailgridiRow = null;
        addDetailgridiCol = null;
    }

    var addProductInfo = [];
    if (editcolosizeRow != null) {

        $('#color_size_grid').saveRow(editcolosizeRow, false, 'clientArray');//仅保存数据到grid中，而不会发送ajax请求服务器
    }
    var ct = $("#edit_customerType").val();
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
            if ($("#edit_discount").val() && $("#edit_discount").val() !== null) {
                productInfo.discount = $("#edit_discount").val();
            } else {
                productInfo.discount = 100;
            }
            productInfo.actPrice = Math.round(productInfo.price * productInfo.discount) / 100;
            productInfo.totPrice = productInfo.qty * productInfo.price;
            productInfo.totActPrice = productInfo.qty * productInfo.actPrice;
            productInfo.sku = productInfo.code;
            productInfo.inStockType = styleRow.class6;
            addProductInfo.push(productInfo);
        }
    });

    jQuery("#color_size_grid").trigger("reloadGrid");  //清空数据重新加载
    
    var isAdd = true;
    $.each(addProductInfo, function (index, value) {
        isAdd = true;
        $.each($("#addDetailgrid").getDataIDs(), function (dtlndex, dtlValue) {
            var dtlRow = $("#addDetailgrid").getRowData(dtlValue);
            if (value.code === dtlRow.sku) {
                dtlRow.qty = parseInt(dtlRow.qty) + parseInt(value.qty);
                dtlRow.totPrice = dtlRow.qty * dtlRow.price;
                dtlRow.totActPrice = dtlRow.qty * dtlRow.actPrice;
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
    if(status) {
        $("#modal-addDetail-table").modal('hide');
    }
    setAddFooterData();

}
function setAddFooterData() {

    var sum_qty = $("#addDetailgrid").getCol('qty', false, 'sum');
    var sum_outQty = $("#addDetailgrid").getCol('outQty', false, 'sum');
    var sum_inQty = $("#addDetailgrid").getCol('inQty', false, 'sum');
    var sum_totPrice = $("#addDetailgrid").getCol('totPrice', false, 'sum');
    var sum_totActPrice = Math.round($("#addDetailgrid").getCol('totActPrice', false, 'sum'));
    $("#edit_actPrice").val(sum_totActPrice);
    $("#addDetailgrid").footerData('set', {
        styleId: "合计",
        qty: sum_qty,
        outQty: sum_outQty,
        inQty: sum_inQty,
        totPrice: -Math.abs(sum_totPrice),
        totActPrice: -Math.abs(sum_totActPrice)
    });
}
function setEditFormVal(){
    $("#edit_billDate").val(getToDay("yyyy-MM-dd"));
    $("#edit_origUnitId").val(defalutCustomerId);
    $("#edit_origUnitName").val(defalutCustomerName);
    $("#edit_discount").val(defalutCustomerdiscount);
    $("#edit_outStatus").val(0);
    $("#edit_inStatus").val(0);
    $("#edit_status").val(0);
    $("#edit_payPrice").val(0);
    $("#edit_actPrice").val(0);
    $("#edit_destId").selectpicker('val',defaultWarehId);
    $("#edit_pre_Balance").val((0 - defalutCustomerowingValue).toFixed(2));


}
//扫码
function addUniqCode() {
    var ct = $("#edit_customerType").val();

    if (ct && ct != null) {

        inOntWareHouseValid = 'addPage_scanUniqueCode';
        billNo = $("#edit_billNo").val();
        if ($("#edit_origId").val() && $("#edit_origId").val() !== null) {
            taskType = 0; //出库
            wareHouse = $("#edit_origId").val();
            isCheckWareHouse=true;
        } else if (($("#edit_destId").val() && $("#edit_destId").val() !== null)) {
            taskType = -1; //没有出库仓库时，直接入库。入库类型等于 -1 表明校验时不需要该参数
            wareHouse = $("#edit_destId").val();
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
    if (!$('#so_savecode_button').prop('disabled')) {
        $("#so_savecode_button").attr({"disabled": "disabled"});
        var productListInfo = [];
        var ct = $("#edit_customerType").val();
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
                if ($("#edit_discount").val() && $("#edit_discount").val() !== null) {
                    productInfo.discount = $("#edit_discount").val();
                } else {
                    productInfo.discount = 100;
                }
                productInfo.uniqueCodes = productInfo.code;
                productInfo.totPrice = -Math.abs(productInfo.price);
                productInfo.actPrice = Math.round(productInfo.price * productInfo.discount) / 100;
                productInfo.totActPrice = -Math.abs(productInfo.actPrice);
                productListInfo.push(productInfo);
            }
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
                    if(dtlRow.uniqueCodes == null || dtlRow.uniqueCodes ==undefined || dtlRow.uniqueCodes ==''){
                        dtlRow.uniqueCodes = value.code;
                    }
                    else {
                        dtlRow.uniqueCodes = dtlRow.uniqueCodes + "," + value.code;
                    }
                    if (dtlRow.id) {
                        $("#addDetailgrid").setRowData(dtlRow.id, dtlRow);//bug，应该写行号
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
        setAddFooterData();
        var check = true;
        saveother(0 - alltotActPrice,check);
    }
}
function addProductsNoOutPutCode(productInfo) {
    debugger;
    if (!$('#so_savecode_button').prop('disabled')) {
        $("#so_savecode_button").attr({"disabled": "disabled"});
        var productListInfo = [];
        var ct = $("#edit_customerType").val();
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
            if ($("#edit_discount").val() && $("#edit_discount").val() !== null) {
                productInfo.discount = $("#edit_discount").val();
            } else {
                productInfo.discount = 100;
            }
            productInfo.noOutPutCode = productInfo.code;
            productInfo.totPrice = -Math.abs(productInfo.price);
            productInfo.actPrice = Math.round(productInfo.price * productInfo.discount) / 100;
            productInfo.totActPrice = -Math.abs(productInfo.actPrice);
            productListInfo.push(productInfo);
        }
        if (productListInfo.length == 0) {
            bootbox.alert("请添加唯一码");
            return;
        }
        var isAdd = true;
        var alltotActPrice = 0;
        debugger;
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
                    if(dtlRow.noOutPutCode.indexOf(value.code) != -1){
                        isAdd = false;
                        return;
                    }else{
                        dtlRow.qty = parseInt(dtlRow.qty) + 1;
                        dtlRow.noOutPutCode = dtlRow.noOutPutCode + "," + value.code;
                    }
                    dtlRow.totPrice = -Math.abs(dtlRow.qty * dtlRow.price);
                    dtlRow.totActPrice = -Math.abs(dtlRow.qty * dtlRow.actPrice);
                    alltotActPrice += -Math.abs(dtlRow.qty * dtlRow.actPrice);
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
        //$("#add-uniqCode-dialog").modal('hide');
        setAddFooterData();
        var check = false;
        saveother(0 - alltotActPrice,check);
    }
}

function input_keydown() {
    $("#edit_discount").keydown(function (event) {
        if (event.keyCode == 13) {
            setDiscount();
        }
    })
}
function saveother(totActPrice,check) {

    cs.showProgressBar();
    $("#edit_customerType").removeAttr('disabled');
    $("#edit_origId").removeAttr('disabled');
    $("#edit_destId").removeAttr('disabled');
    $("#edit_busnissId").removeAttr('disabled');

    if ($("#edit_origId").val() == $("#edit_destId").val()) {
        bootbox.alert("不能在相同的单位之间做销售退货");
        cs.closeProgressBar();
        return;
    }

    $("#editForm").data('bootstrapValidator').destroy();
    $('#editForm').data('bootstrapValidator', null);
    initEditFormValid();
    $('#editForm').data('bootstrapValidator').validate();
    if (!$('#editForm').data('bootstrapValidator').isValid()) {
        cs.closeProgressBar();
        return;
    }
    if(pageType=="edit"){
        if(slaeOrderReturn_customerType!=$("#edit_customerType").val()){
            bootbox.alert("客户类型不相同");
            cs.closeProgressBar();
            return;
        }
    }
    if (addDetailgridiRow != null && addDetailgridiCol != null) {
        $("#addDetailgrid").saveCell(addDetailgridiRow, addDetailgridiCol);
        addDetailgridiRow = null;
        addDetailgridiCol = null;
    }
//实收金额的计算
    var payPrice = $("#edit_payPrice").val();
    if (parseFloat(payPrice) < 0) {
        var summun = parseFloat(payPrice) - parseFloat(totActPrice);
        if (summun < 0) {
            $("#edit_payPrice").val(summun.toFixed(2));
        }
    }
    var actPrice = parseFloat($("#edit_actPrice").val());
    var payPrice = parseFloat($("#edit_payPrice").val());
    var preBalance = parseFloat($("#edit_pre_Balance").val());
    var afterBalance = parseFloat(preBalance + payPrice - actPrice).toFixed(2);
    $("#edit_after_Balance").val(afterBalance);
    var bi = $("#edit_billNo").val();
    var purchaseReturnBill = JSON.stringify(array2obj($("#editForm").serializeArray()));
    var dtlArray = [];
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        dtlArray.push(rowData);
    });
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
            cs.closeProgressBar();
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#edit_billNo").val(msg.result);
                billNo = msg.result;
                initeditGrid(msg.result);
                if (check==true){
                    $('#codegrid').jqGrid("clearGridData");
                    $('#codegrid').jqGrid('GridUnload');
                    initCodeGrid({billNo: $("#edit_billNo").val(), warehId: $("#edit_origId").val()});
                    $("#codegrid").trigger("reloadGrid");
                }
                $("#grid").trigger("reloadGrid");
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
    $("#edit_customerType").attr({"disabled": "disabled"});
}
// @param: type     出入库类型，"in"入库；"out"出库
function wareHouseInOut(type) {
    cs.showProgressBar();
    var billNo = $("#edit_billNo").val();
    if (type === "in") {
        $("#SRDtl_wareHouseIn_noOutHouse").attr({"disabled": "disabled"});
    } else if (type === "out") {
        $("#SRDtl_wareHouseOut").attr({"disabled": "disabled"});
    }

    if (billNo && billNo != null) {
        if (inOutStockCheck(type)) {
            cs.closeProgressBar();
            return;
        }
        var url_ajax;
        var inOutString;
        if (type === "out") {
            url_ajax = basePath + "/logistics/saleOrderReturn/convertOut.do";
            inOutString = "出";
            taskType = 0;
            wareHouse = $("#edit_origId").val();
        } else {
            url_ajax = basePath + "/logistics/saleOrderReturn/convertIn.do";
            inOutString = "入";
            taskType = -1;
            wareHouse = $("#edit_destId").val();
        }

        var allUniqueCodes = "";
        $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
            var rowData = $("#addDetailgrid").getRowData(value);
            allUniqueCodes = allUniqueCodes + "," + rowData.uniqueCodes;
        });
        $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
            var rowData = $("#addDetailgrid").getRowData(value);
            allUniqueCodes = allUniqueCodes + "," + rowData.noOutPutCode;
        });
        if (allUniqueCodes.substr(0, 1) == ",") {
            allUniqueCodes = allUniqueCodes.substr(1);
        }
        var uniqueCodes_inHouse;

        $.ajax({
            async: true,
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

                var epcArray = [];
                $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
                    var rowData = $("#addDetailgrid").getRowData(value);
                    var codes = rowData.uniqueCodes.split(",");
                    var noOutcodes = rowData.noOutPutCode.split(",");
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
                    if (noOutcodes && noOutcodes != null && noOutcodes != "") {
                        $.each(noOutcodes, function (index, value) {
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
                    cs.closeProgressBar();
                    if (pageType === "edit") {
                        if (type === "out") {
                            edit_wareHouseOut();
                        } else {
                            edit_wareHouseIn_noOutHouse();
                        }
                    }
                    if (type === "in") {
                        $("#SRDtl_wareHouseIn_noOutHouse").removeAttr("disabled");
                    } else if (type === "out") {
                        $("#SRDtl_wareHouseOut").removeAttr("disabled");
                    }
                    return;
                }

                var dtlArray = [];
                $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
                    var rowData = $("#addDetailgrid").getRowData(value);
                    dtlArray.push(rowData);
                });

                $.ajax({
                    async: true,
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
                        cs.closeProgressBar();
                        if (type === "in") {
                            $("#SRDtl_wareHouseIn_noOutHouse").removeAttr("disabled");
                        } else if (type === "out") {
                            $("#SRDtl_wareHouseOut").removeAttr("disabled");
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
                                $("#SRDtl_save").attr({"disabled": "disabled"});
                                $("#SRDtl_addUniqCode").attr({"disabled": "disabled"});
                                $("#edit_origId").attr('disabled', true);
                                $("#edit_destId").attr('disabled', true);
                                $("#edit_billDate").attr('readOnly', true);
                                $("#edit_busnissId").attr('disabled', true);
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
                                _search();
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
                                        _search();
                                    }
                                });
                            }
                            initeditGrid($("#edit_billNo").val());
                        } else {
                            bootbox.alert(msg.msg);
                        }
                    }
                });
            }
        });


    } else {
        cs.closeProgressBar();
        if (type === "in") {
            $("#SODtl_wareHouseIn_noOutHouse").removeAttr("disabled");
        } else if (type === "out") {
            $("#SODtl_wareHouseOut").removeAttr("disabled");
        }
        bootbox.alert("请先保存当前单据");
    }
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
function showCodesDetail(uniqueCodes) {

    // $("#show-uniqueCode-list").modal('show');

    wareHouse = $("#search_destId").val();
    var billNo=$("#edit_billNo").val();
    $("#show-uniqueCode-saleReturn-list").modal('show');
    initUniqueCodeSaleReturnList(uniqueCodes,billNo);
    codeSaleReturnListReload(uniqueCodes,billNo);
}
function doPrint() {

    /*$("#editForm").resetForm();*/
    debugger;
    $("#edit-dialog-print").modal('show');
    $("#form_code").removeAttr("readOnly");
    var billNo = $("#edit_billNo").val();
    $("#billno").val(billNo);
    $("#edit-dialog-print").show();
    $.ajax({
        dataType: "json",
        url: basePath + "/sys/printset/findPrintSetListByOwnerId.do",
        type: "POST",
        data: {
            type:"SR"
        },
        success: function (msg) {

            if (msg.success) {
                var addcont = "";
                //var ishave = false;
                for (var i = 0; i < msg.result.length; i++) {
                    addcont += "<div class='form-group' onclick=set('" + msg.result[i].id + "') title='" + msg.result[i].name + "'>" +
                        "<button class='btn btn-info'>" +
                        "<i class='cae-icon fa fa-refresh'></i>" +
                        "<span class='bigger-10'>套打" + msg.result[i].name + "</span>" +
                        "</button>" +
                        "</div>"
                }
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
        url: basePath + "/sys/printset/printMessage.do",
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
/**
 * 新增单据调用
 * */
function addNew(){
    $('#addDetailgrid').jqGrid("clearGridData");
    $('#addDetailgrid').jqGrid('GridUnload');
    initAddGrid();
    $("#editForm").clearForm();
    setEditFormVal();
    $("#edit_origId").removeAttr("disabled");
    $("#edit_destId").removeAttr("disabled");
    $("#edit_busnissId").removeAttr("disabled");
    initCustomerTypeForm();
    $("#addDetailgrid").trigger("reloadGrid");
    initButtonGroup(0);
    if (defaultSaleStaffId != "" && defaultSaleStaffId != undefined) {
        $("#edit_busnissId").selectpicker('val',defaultSaleStaffId);
    }
    $("#edit_payType").selectpicker('val',defaultPayType);
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

            } else {
            }
        }
    });
}
function cancelAjax(billId) {
    $.ajax({
        dataType: "json",
        url: basePath + "/logistics/saleOrderReturn/cancel.do",
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
function edit_wareHouseIn() {
    skuQty = {};
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        skuQty[rowData.sku] = rowData.inQty;
    });

    inOntWareHouseValid = 'wareHouseIn_valid';
    taskType = 1;
    var destId = $("#edit_destId").val();
    wareHouse = destId;
    var ct = $("#edit_customerType").val();
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
    cs.showProgressBar();
    var billNo = $("#edit_billNo").val();
    $("#WareHouseIn_dialog_buttonGroup").attr({"disabled": "disabled"});
    var epcArray = [];
    $.each($("#uniqueCodeGrid").getDataIDs(), function (index, value) {
        var rowData = $("#uniqueCodeGrid").getRowData(value);
        epcArray.push(rowData);
    });
    if (epcArray.length === 0) {
        bootbox.alert("请添加唯一码!");
        cs.closeProgressBar();
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
            cs.closeProgressBar();
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
function updateBillDetailData(){
    var ct = $("#edit_customerType").val();
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
        dtlRow.totPrice =  -Math.abs(dtlRow.qty * dtlRow.price).toFixed(2);
        dtlRow.totActPrice = -Math.abs(dtlRow.qty * dtlRow.actPrice).toFixed(2);
        if(dtlRow.id){
            $("#addDetailgrid").setRowData(dtlRow.id, dtlRow);
        }else{
            $("#addDetailgrid").setRowData(value, dtlRow);
        }
    });

}
//加盟商退货专用审核通过后才能
function checkAjax() {
    var billNo = $("#edit_billNo").val();
    $.ajax({
        url: basePath + "/logistics/saleOrderReturn/check.do?billNo=" + billNo,
        type: "POST",
        success: function (result) {
            if (result.success) {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                _search();
                addNew();
            } else {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
            }
        }
    });
}
function doPrintSanLian() {
    $("#edit-dialog-print").modal('show');
    $("#form_code").removeAttr("readOnly");
    var billNo = $("#search_billNo").val();
    $("#billno").val(billNo);
    $("#edit-dialog-print").show();
    $.ajax({
        dataType: "json",
        url: basePath + "/sys/printset/findPrintSetListByOwnerId.do",
        type: "POST",
        data: {
            type:"SR",
            ruleReceipt:"SanLian"
        },
        success: function (msg) {
            if (msg.success) {
                var addcont = "";
                for (var i = 0; i < msg.result.length; i++) {
                    addcont += "<div class='form-group' onclick=setSanLian('" + msg.result[i].id + "') title='" + msg.result[i].name + "'>" +
                        "<button class='btn btn-info'>" +
                        "<i class='cae-icon fa fa-refresh'></i>" +
                        "<span class='bigger-10'>套打" + msg.result[i].name + "</span>" +
                        "</button>" +
                        "</div>"
                }
                $("#addbutton").html(addcont);

            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}
function setSanLian(id) {
    $.ajax({
        dataType: "json",
        url: basePath + "/sys/printset/printMessageSanLian.do",
        data: {"id": id, "billno": $("#edit_billNo").val()},
        type: "POST",
        success: function (msg) {
            if (msg.success) {

                var print = msg.result.print;
                var cont = msg.result.cont;
                var contDel = msg.result.contDel;
                console.log(print);
                console.log(cont);
                console.log(contDel);
                var LODOP = getLodop();
                //eval(print.printCont);
                $("#edit-dialogSanLian").html(print.printTableTh);
                var printCode=print.printCode;
                var printCodeArray=printCode.split(",");
                for(var i=0;i<printCodeArray.length;i++){
                    debugger;
                    var plp = printCodeArray[i];
                    var message = cont[plp];
                    $("#edit-dialogSanLian").find("#"+plp).text(message);
                }
                var tbodyCont="";
                for(var a=0;a<contDel.length;a++){
                    var del=contDel[a];
                    var printTableCode=print.printTableCode.split(",");
                    tbodyCont+=" <tr style='border-top:1px ;padding-top:5px;'>";
                    for(var b=0;b<printTableCode.length;b++){
                        if(printTableCode[b]=="styleId"||printTableCode[b]=="styleName"||printTableCode[b]=="colorId") {
                            tbodyCont += "<td align='middle' colspan='3' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        }else if(printParameter.sizeArrySanLian.indexOf(printTableCode[b])!=-1){
                            tbodyCont += "<td align='middle' colspan='1' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        }else{
                            tbodyCont += "<td align='middle' colspan='2' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        }

                    }
                    tbodyCont+="</tr>"
                }
                $("#loadtabSanLian").html(tbodyCont);
                console.log($("#edit-dialogSanLian").html());
                //LODOP.SET_PRINT_STYLEA("baseHtml", 'Content', $("#edit-dialogSanLian").html());
                LODOP.ADD_PRINT_TABLE(100,1,printParameter.receiptWidthSanLian,printParameter.receiptheightSanLian,$("#edit-dialogSanLian").html());
                //LODOP.PREVIEW();
                LODOP.PRINT();
                $("#edit-dialog-print").hide();



            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}
/**
 * A4打印
 * */
function doPrintA4() {

    $("#edit-dialog-print").modal('show');
    $("#form_code").removeAttr("readOnly");
    var billNo = $("#search_billNo").val();
    $("#billno").val(billNo);
    $("#edit-dialog-print").show();
    $.ajax({
        dataType: "json",
        url: basePath + "/sys/printset/findPrintSetListByOwnerId.do",
        type: "POST",
        data: {
            type:"SR",
            ruleReceipt:"A4"
        },
        success: function (msg) {
            if (msg.success) {
                var addcont = "";
                for (var i = 0; i < msg.result.length; i++) {
                    addcont += "<div class='form-group' onclick=setA4('" + msg.result[i].id + "') title='" + msg.result[i].name + "'>" +
                        "<button class='btn btn-info'>" +
                        "<i class='cae-icon fa fa-refresh'></i>" +
                        "<span class='bigger-10'>套打" + msg.result[i].name + "</span>" +
                        "</button>" +
                        "</div>"
                }
                $("#addbutton").html(addcont);

            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}
function setA4(id) {
    $.ajax({
        dataType: "json",
        url: basePath + "/sys/printset/printMessageA4.do",
        data: {"id": id, "billno": $("#edit_billNo").val()},
        type: "POST",
        success: function (msg) {
            if (msg.success) {

                var print = msg.result.print;
                var cont = msg.result.cont;
                var contDel = msg.result.contDel;
                console.log(print);
                console.log(cont);
                console.log(contDel);
                var LODOP = getLodop();
                //eval(print.printCont);
                $("#edit-dialogSanLian").html(print.printTableTh);
                var printCode=print.printCode;
                var printCodeArray=printCode.split(",");
                for(var i=0;i<printCodeArray.length;i++){
                    debugger;
                    var plp = printCodeArray[i];
                    var message = cont[plp];
                    $("#edit-dialogSanLian").find("#"+plp).text(message);
                }
                var tbodyCont="";
                for(var a=0;a<contDel.length;a++){
                    var del=contDel[a];
                    var printTableCode=print.printTableCode.split(",");
                    tbodyCont+=" <tr style='border-top:1px ;padding-top:5px;'>";
                    for(var b=0;b<printTableCode.length;b++){
                        if(printTableCode[b]=="styleId"||printTableCode[b]=="styleName"||printTableCode[b]=="colorId") {
                            tbodyCont += "<td align='middle' colspan='3' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        }else if(printParameter.sizeArry.indexOf(printTableCode[b])!=-1){
                            tbodyCont += "<td align='middle' colspan='1' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        } else if(printTableCode[b]=="other"){
                            tbodyCont += "<td align='middle' colspan='1' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        }else{
                            tbodyCont += "<td align='middle' colspan='2' style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>" + del[printTableCode[b]] + "</td>"
                        }

                    }
                    tbodyCont+="</tr>"
                }
                $("#loadtabA4").html(tbodyCont);
                console.log($("#edit-dialogSanLian").html());
                //LODOP.SET_PRINT_STYLEA("baseHtml", 'Content', $("#edit-dialogSanLian").html());
                LODOP.ADD_PRINT_TABLE(100,1,printParameter.receiptWidthA4,printParameter.receiptheightSanLian,$("#edit-dialogSanLian").html());
                //LODOP.PREVIEW();
                LODOP.PRINT();
                $("#edit-dialog-print").hide();



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
    var privilegeMap = ButtonAndDivPower(resourcePrivilege);
    $.each(privilegeMap['table'],function(index,value){
        if(value.isShow!=0) {
            $('#addDetailgrid').setGridParam().hideCol(value.privilegeId);
        }
    });
    var disableButtonIds = "";
    switch (billStatus){
        case "-1" :
            disableButtonIds = ["SRDtl_cancel","SRDtl_save","SRDtl_wareHouseOut","SRDtl_wareHouseIn_noOutHouse","SRDtl_wareHouseIn"];
            break;
        case "0" :
            disableButtonIds = ["SRDtl_check"];
            break;
        case "1":
            disableButtonIds = ["SRDtl_cancel","SRDtl_save"];
            break;
        case "2" :
            disableButtonIds = ["SRDtl_cancel","SRDtl_save","SRDtl_wareHouseOut","SRDtl_wareHouseIn_noOutHouse","SRDtl_wareHouseIn","SRDtl_doPrint","SRDtl_doPrintA4","SRDtl_check"];
            break;
        case "3":
            disableButtonIds = ["SRDtl_cancel","SRDtl_save"];
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
    if ($("#edit_origId").val() && $("#edit_origId").val() !== null && $("#edit_origId").val() !== "") {
        $("#SRDtl_wareHouseOut").removeAttr("disabled");
        $("#SRDtl_wareHouseIn").show();
        $("#SRDtl_wareHouseIn_noOutHouse").hide();
    } else {
        $("#SRDtl_wareHouseOut").attr({"disabled": "disabled"});
        $("#SRDtl_wareHouseIn").hide();
        $("#SRDtl_wareHouseIn_noOutHouse").show();
    }
    if ($("#edit_billNo").val()!= "" && $("#edit_status").val() == "2") {
        $("#SRDtl_addUniqCode").hide();
    }
    if ($("#edit_status").val()=="0"){
        $("#SRDtl_check").removeAttr("disabled");
    }else {
        $("#SRDtl_check").attr({"disabled": "disabled"});
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
    setAddFooterData();
}

function deleteItem(rowId) {
    var value = $('#addDetailgrid').getRowData(rowId);
    $("#addDetailgrid").jqGrid("delRowData", rowId);
    setAddFooterData();
    var totActPrice = value.totActPrice;
    //判断是否有异常的code
    if(value.noOutPutCode!=null&&value.noOutPutCode!=""&&value.noOutPutCode!=undefined){
        deletenoOutPutCode(value.noOutPutCode,totActPrice);
    }else{
        saveother(totActPrice);
    }
}
