var searchUrl = basePath + "/logistics/saleOrderBill/page.do?filter_GTI_status=-1&userId=" + userId;
var addDetailgridiRow;//存储iRow
var addDetailgridiCol;//存储iCol
var allCodes; //用于拼接所有添加过的唯一码，防止重复添加
var taskType; //用于判断出入库类型 1入库 0 出库
var wareHouse;
var inOntWareHouseValid; //用于判断在编辑BillDtl时出入库操作是否需要校验，使用哪种校验。
var skuQty = {};//保存每个SKU对应的出入库数量。
var allCodeStrInDtl = "";  //入库时，所有明细中的唯一码
var billNo;
var sizeArry="S,XS,M,L,XL,XXL,XXXL,F,other";
var autoSelect =false;//是否自动选中
var showScanDialog = false;
var isCheckWareHouse=false;//是否检测出库仓库
var slaeOrder_status = "0";
var outStatus = null;//出库状态
$(function () {
    load().then(function (data) {
        /*初始化左侧grig*/
        initSearchGrid();
    });


});

function load() {
    var promise = new Promise(function(resolve, reject){
        /*初始化右侧grig*/
        initAddGrid();
        /*初始化from表单*/
        initSearchAndEditForm();
        if (billNo) {
            bootbox.alert("单据" + billNo + "正在编辑中");
        } else {
            sessionStorage.removeItem("billNosale");
        }
        /*回车监事件*/
        keydown();
        addProduct_keydown();
        input_keydown();
        /*初始化按钮*/
        initButtonGroup(0);
        /*初始化右侧表单验证*/
        initEditFormValid();
        loadingButtonDivTable();
        resolve("success");
    });
    return promise;
}


function initSearchGrid() {
    var url="";
    if (cargoTrack=="cargoTracking"){
        url= basePath + "/logistics/saleOrderBill/findBill.do?billNo="+cTbillNo;
    }else {
        url = basePath + "/logistics/saleOrderBill/page.do?filter_GTI_status=-1&userId=" + userId;
    }
    $("#grid").jqGrid({
        height: "auto",
        url:url,
        datatype: "json",
        mtype: 'POST',
        colModel: [
            {name: 'billDate', label: '单据日期', sortable: true, width: 30},
            {name: 'billNo', label: '单据编号', sortable: true, width: 45,hidden:true},

            {name: 'status', hidden: true},
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
                        default:
                            break;
                    }
                    return html;
                }
            },
            {name: 'outStatus', hidden: true},

            {name: 'inStatus', hidden: true},

            {name: 'destUnitId', label: '客户ID', hidden: true},
            {name: 'destUnitName', label: '客户', width: 20},


            {name: 'customerTypeId', label: '客户类型', hidden: true},

            {name: 'origUnitId', label: '发货方ID', hidden: true},
            {name: 'origUnitName', label: '发货方', hidden: true},
            {name: 'origId', label: '发货仓库ID', hidden: true},
            {name: 'origName', label: '发货仓库', hidden: true},
            {name: 'ownerId',  hidden: true},
            {name: 'destId', label: '收货仓库ID', hidden: true},
            {name: 'destName', label: '收货仓库', hidden: true},
            {name: 'busnissId', hidden: true},

            {name: 'totQty', label: '单据数量', width: 20,align: "center"},
            {name: 'totOutQty', label: '出库数量', width: 20,align: "center"},
            {name: 'busnissName', label: '销售员', width: 20},
            {name: 'preBalance', label: '售前余额', width: 20, hidden: true},
            {name: 'afterBalance', label: '售后余额', width: 20, hidden: true},
            {name: 'actPrice',label: '应付金额',width: 20},
            {name: 'payPrice',label: '实付金额', width: 20},
            {name: 'remark', label: '备注',  width: 20}
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
            if($("#edit_billNo").val()!=null&&$("#edit_billNo").val()!=""&&$("#edit_billNo").val()!=undefined){
                initDetailData($("#edit_billNo").val());
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
    var rowData = $("#grid").getRowData(rowid);
    if(rowData.ownerId!=""&&rowData.ownerId!=undefined){
        var url = basePath + "/sys/user/list.do?filter_EQI_type=4&filter_EQS_ownerId=" + rowData.ownerId;
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
                fullDetailData(rowData)
            }
        });
    }else{
        fullDetailData(rowData);
    }

}
function fullDetailData(rowData) {
    $("#editForm").setFromData(rowData);
    slaeOrder_status = rowData.status;
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
        $("#SODtl_save").removeAttr("disabled");
    }
    $(".selectpicker").selectpicker('refresh');
    $('#addDetailgrid').jqGrid("clearGridData");
    $('#addDetailgrid').jqGrid('GridUnload');
    initeditGrid(rowData.billNo);
    initButtonGroup(slaeOrder_status);
    $("#addDetailgrid").trigger("reloadGrid");
}
/**
 * 新增单据调用
 * isScan 是否调用扫码框
 * */
function addNew(isScan){
    showScanDialog = isScan;
    $('#addDetailgrid').jqGrid("clearGridData");
    $('#addDetailgrid').jqGrid('GridUnload');
    initAddGrid();
    $("#editForm").clearForm();
    setEditFormVal();
    initCustomerTypeForm();
    $("#addDetailgrid").trigger("reloadGrid");
    $(".selectpicker").selectpicker('refresh');
    slaeOrder_status ="0";
    initButtonGroup(0);
    initSelectBusinessIdFormOnAddNew();
}
function setFooterData() {

    var sum_totQty = $("#grid").getCol('totQty', false, 'sum');
    var sum_totOutQty = $("#grid").getCol('totOutQty', false, 'sum');
    $("#grid").footerData('set', {
        billDate: "合计",
        totQty: sum_totQty,
        totOutQty: sum_totOutQty
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

function initAddGrid() {


    $("#addDetailgrid").jqGrid({
        height: 'auto',
        datatype: "local",
        //url: basePath + "/logistics/saleOrderBill/findBillDtl.do?billNo=" + billNo,
        mtype: 'POST',
        colModel: [
            {name: 'id', label: 'id', hidden: true},
            {name: 'billId', label: 'billId', hidden: true},
            {name: 'billNo', label: 'billNo', hidden: true},
            {name: 'status', hidden: true},
            {name: "ownerId", hidden: true},
            {name: 'inStatus', hidden: true},
            {name: 'outStatus', hidden: true},
            {
                name: "operation", label: "操作", width: 30, align: "center", sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    return  "<a href='javascript:void(0);' onclick=saveItem('" + options.rowId + "')>"
                        +     "<i class= 'ace-icon ace-icon fa fa-save' title='保存'></i>"
                        + "</a>"
                        + "<a style='margin: 8px' href='javascript:void(0);' onclick=deleteItem('" + options.rowId + "')>"
                        +     "<i class='ace-icon fa fa-trash-o red' title='删除'></i>"
                        + "</a>";
                }
            },
            {
                name: 'statusImg', label: '状态', width: 20, hidden: true, sortable: false,
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
            {
                name: 'outStatusImg', label: '出库状态', width: 30, align: 'center', sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    outStatus = rowObject.outStatus;
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
            },
            {name: 'styleId', label: '款号', width: 40,hidden:true,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'styleName', label: '款名', width: 40,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'colorId', label: '色码', width: 40,hidden:true,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'colorName', label: '颜色', width: 30,hidden:true,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'sizeId', label: '尺码', width: 30,hidden:true,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'sizeName', label: '尺码', width: 40,hidden:true,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
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
                },
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'outQty', label: '已出库数量', width: 40,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'inQty', label: '已入库数量', width: 40,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'sku', label: 'SKU', width: 60,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {
                name: 'price', label: '销售价格', width: 40,
                editrules: {
                    number: true
                },
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'totPrice', label: '销售金额', width: 40,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {
                name: 'discount', label: "折扣", width: 40, editable: true,
                editrules: {
                    number: true,
                    minValue: 0,
                    maxValue: 200
                },
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {
                name: 'actPrice', label: '实际价格', editable: true, width: 40,
                editrules: {
                    number: true,
                    minValue: 0
                },
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'totActPrice', label: '实际金额', width: 40,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'delRemark', label: '详情备注', editable: true,width: 100,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'uniqueCodes', label: '唯一码', hidden: true},
            {name: 'puPrice', label: '代理商批发价格', hidden: true},
            {name: 'wsPrice', label: '门店批发价格', hidden: true},
            {name: 'changeTRqty', label: '转调拨单数量', hidden: true},
            {name: 'noOutPutCode', label: '异常唯一码', hidden: true},
            {name:'stylePriceMap',label:'价格表',hidden:true},
            {name:'abnormalStatus',label:'异常单状态',hidden:true},
            {
                name: '', label: '唯一码明细', width: 40, align: "center",
                formatter: function (cellValue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=showCodesDetail('" + rowObject.uniqueCodes + "')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
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
             /*   var var_actPrice = Math.round(value * $('#addDetailgrid').getCell(rowid, "price")) / 100;
                var var_totActPrice = Math.round(var_actPrice * $('#addDetailgrid').getCell(rowid, "qty") * 100) / 100;
                $('#addDetailgrid').setCell(rowid, "actPrice", var_actPrice);
                $('#addDetailgrid').setCell(rowid, "totActPrice", var_totActPrice);*/
                //判断实际价格是不是小于门店批发价格
                var var_actPrice;

                if((value*$('#addDetailgrid').getCell(rowid, "price")/100)<$('#addDetailgrid').getCell(rowid, "wsPrice")&&isUserAbnormal){
                    $('#addDetailgrid').setCell(rowid, "discount", parseFloat($('#addDetailgrid').getCell(rowid, "wsPrice")/$('#addDetailgrid').getCell(rowid, "price")).toFixed(2)*100);
                    var_actPrice =  $('#addDetailgrid').getCell(rowid, "wsPrice");
                    $('#addDetailgrid').setCell(rowid, "actPrice", $('#addDetailgrid').getCell(rowid, "puPrice"));
                    $('#addDetailgrid').setCell(rowid, "abnormalStatus",1);
                    changeWordscolor(rowid,"blue");
                }else{
                    $('#addDetailgrid').setCell(rowid, "discount", value);
                    var_actPrice = Math.round(value * $('#addDetailgrid').getCell(rowid, "price")) / 100;
                    $('#addDetailgrid').setCell(rowid, "actPrice", var_actPrice);
                    $('#addDetailgrid').setCell(rowid, "abnormalStatus", 0);
                    changeWordscolor(rowid,"black");
                }

                var var_totActPrice = Math.round(var_actPrice * $('#addDetailgrid').getCell(rowid, "qty") * 100) / 100;
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
    //$("#addDetailgrid").setGridParam().hideCol("styleName");
    loadingTableName();
}

function setEditFormVal(){
    $("#edit_billDate").val(getToDay("yyyy-MM-dd"));
    $("#edit_payPrice").val(0);
    $("#edit_origId").val(defaultWarehId);
    $("#edit_destUnitId").val(defalutCustomerId);
    $("#edit_destUnitName").val(defalutCustomerName);
    $("#edit_discount").val(defalutCustomerdiscount);
    $("#edit_outStatus").val(0);
    $("#edit_inStatus").val(0);
    $("#edit_status").val(0);
    $("#edit_pre_Balance").val((0 - defalutCustomerowingValue).toFixed(2));
    $("#edit_busnissId").val(defaultSaleStaffId);
    $("#edit_busnissId").removeAttr("disabled");
    $("#edit_origId").removeAttr("disabled");
    $("#edit_destId").removeAttr("disabled");

}
function initeditGrid(billId) {
    billNo = billId;
    $("#addDetailgrid").jqGrid({
        height: 'auto',
        datatype: "json",
        url: basePath + "/logistics/saleOrderBill/findBillDtl.do?billNo=" + billNo,
        mtype: 'POST',
        colModel: [
            {name: 'id', label: 'id', hidden: true},
            {name: 'billId', label: 'billId', hidden: true},
            {name: 'billNo', label: 'billNo', hidden: true},
            {name: 'status', hidden: true},
            {name: 'inStatus', hidden: true},
            {name: 'outStatus', hidden: true},
            {
                name: "operation", label: "操作", width: 30, editable: false, sortable: false, align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=saveItem('" + options.rowId + "')><i class='ace-icon ace-icon fa fa-save' title='保存'></i></a>"
                        + "<a style='margin: 8px' href='javascript:void(0);' onclick=deleteItem('" + options.rowId + "')><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";
                }
            },
            {
                name: 'statusImg', label: '状态', width: 20, hidden: true, sortable: false,
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
                name: 'inStatusImg', label: '入库状态', width: 50, align: 'center', sortable: false,
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
                name: 'outStatusImg', label: '出库状态', width: 50, align: 'center', sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    outStatus = rowObject.outStatus;
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
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'styleName', label: '款名', width: 60,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'colorId', label: '色码', width: 40,hidden:true,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'colorName', label: '颜色', width: 30,hidden:true,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'sizeId', label: '尺码', width: 30,hidden:true,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'sizeName', label: '尺码', width: 40,hidden:true,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
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
                },
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'returnQty', label: '退货数量', editable: true, width: 40,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'outQty', label: '已出库数量', width: 40,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'inQty', label: '已入库数量', width: 40,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'sku', label: 'SKU', width: 100,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {
                name: 'price', label: '销售价格', width: 60,
                editrules: {
                    number: true
                },
                formatter: function (cellValue, options, rowObject) {
                    var price = parseFloat(cellValue).toFixed(2);
                    return price;
                },
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },

            {
                name: 'totPrice', label: '销售金额', width: 60,
                formatter: function (cellValue, options, rowObject) {
                    var totPrice = parseFloat(cellValue).toFixed(2);
                    return totPrice;
                },
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {
                name: 'discount', label: "折扣", width: 50, editable: true,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {
                name: 'actPrice', label: '实际价格', editable: true, width: 60,
                editrules: {
                    number: true
                },
                formatter: function (cellValue, options, rowObject) {
                    var actPrice = parseFloat(cellValue).toFixed(2);
                    return actPrice;
                },
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {
                name: 'totActPrice', label: '实际金额', width: 60,
                formatter: function (cellValue, options, rowObject) {
                    var totActPrice = parseFloat(cellValue).toFixed(2);
                    return totActPrice;
                },
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'delRemark', label: '详情备注', editable: true,width: 110,
                cellattr:function(rowId, val, rawObject, cm, rdata) {
                    if(rawObject.abnormalStatus==1){
                        return "style='color:blue;'";
                    }
                    if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                        return "style='color:red;'";
                    }
                }
            },
            {name: 'uniqueCodes', label: '唯一码', hidden: true},
            {
                name: '', label: '唯一码明细', width: 65, align: "center",
                formatter: function (cellValue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=showCodesDetail('" + rowObject.uniqueCodes + "')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
                }
            },
            {
                name: '', label: '异常唯一码明细', width: 75, align: "center",
                formatter: function (cellValue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=showCodesDetail('" + rowObject.noOutPutCode + "')><i class='ace-icon ace-icon fa fa-list' title='显示异常唯一码明细'></i></a>";
                }
            },
            {name: 'noOutPutCode', label: '异常唯一码', hidden: true},
            {name: 'changeTRqty', label: '转调拨单数量', hidden: true},
            {name: 'puPrice', label: '代理商批发价格', hidden: true},
            {name: 'wsPrice', label: '门店批发价格', hidden: true},
            {name: 'returnbillNo', label: '退货单号', hidden: true},
            {name:'stylePriceMap',label:'价格表',hidden:true},
            {name:'abnormalStatus',label:'异常单状态',hidden:true}

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
        afterSaveCell: function (rowid, cellname, value, iRow, iCol) {
            var rowData = $('#addDetailgrid').getRowData(rowid);
            if ((parseInt(rowData.qty) - parseInt(rowData.outQty)) >= parseInt(rowData.returnQty) || rowData.returnQty == "") {
                $('#addDetailgrid').editCell(iRow, iCol, true);
            } else {
                $('#addDetailgrid').setCell(rowid, cellname, 0);
                $('#addDetailgrid').editCell(iRow, iCol, true);
                $.gritter.add({
                    text: "退货数量过多！",
                    class_name: 'gritter-success  gritter-light'
                });
            }
            if (cellname === "discount") {
                //判断实际价格是不是小于门店批发价格
                var var_actPrice;
                var stylePriceMap=JSON.parse($('#addDetailgrid').getCell(rowid, "stylePriceMap"));
                if((value*$('#addDetailgrid').getCell(rowid, "price")/100)<stylePriceMap.wsPrice && isUserAbnormal){
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

                var var_totActPrice = Math.round(var_actPrice * $('#addDetailgrid').getCell(rowid, "qty") * 100) / 100;
                $('#addDetailgrid').setCell(rowid, "totActPrice", var_totActPrice);

            } else if (cellname === "actPrice") {
                var var_discount = Math.round(value / $('#addDetailgrid').getCell(rowid, "price") * 100);
                var var_totActPrice = Math.round(value * $('#addDetailgrid').getCell(rowid, "qty") * 100) / 100;
                $('#addDetailgrid').setCell(rowid, "discount", var_discount);
                $('#addDetailgrid').setCell(rowid, "totActPrice", var_totActPrice);
            } else if (cellname === "qty") {
                $('#addDetailgrid').setCell(rowid, "totPrice", Math.round($('#addDetailgrid').getCell(rowid, "price") * value) * 100) / 100;
                $('#addDetailgrid').setCell(rowid, "totActPrice", Math.round($('#addDetailgrid').getCell(rowid, "actPrice") * value) * 100) / 100;
            }
            setAddFooterData();
        },
        afterEditCell: function (rowid, celname, value, iRow, iCol) {
            var value = $('#addDetailgrid').getRowData(rowid);
            if ((value.outStatus != 2 && celname == "returnQty" && value.returnbillNo == "") ||
                (value.outStatus != 2 && celname == "discount" && value.returnbillNo == "") ||
                (value.outStatus != 2 && celname == "actPrice" && value.returnbillNo == "")||
                (value.outStatus != 2 && celname == "delRemark" && value.returnbillNo == "")) {
                addDetailgridiRow = iRow;
                addDetailgridiCol = iCol;
            } else {
                $('#addDetailgrid').restoreCell(iRow, iCol);
            }
        },

        gridComplete: function () {
            setAddFooterData();
        },
        loadComplete: function () {
            initAllCodesList();

        }
    });
    if (slaeOrder_status != "0"||userId=="admin") {
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

    //$("#addDetailgrid").setGridParam().hideCol("styleName");
    loadingTableName();
}
function setAddFooterData() {
    var sum_qty = $("#addDetailgrid").getCol('qty', false, 'sum');
    var sum_totOutQty = $("#addDetailgrid").getCol('totOutQty', false, 'sum');
    var sum_inQty = $("#addDetailgrid").getCol('inQty', false, 'sum');
    var sum_returnQty = $("#addDetailgrid").getCol('returnQty', false, 'sum');
    var sum_totPrice = $("#addDetailgrid").getCol('totPrice', false, 'sum');
    var sum_totActPrice = Math.round($("#addDetailgrid").getCol('totActPrice', false, 'sum'));
    $("#edit_actPrice").val(sum_totActPrice);
    $("#addDetailgrid").footerData('set', {
        styleId: "合计",
        qty: sum_qty,
        totOutQty: sum_totOutQty,
        inQty: sum_inQty,
        returnQty: sum_returnQty,
        totPrice: sum_totPrice,
        totActPrice: sum_totActPrice
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
    console.log(allCodeStrInDtl);
}


function initSearchAndEditForm(){
    initSelectDestForm();
    initSelectOrigForm();
    initCustomerTypeForm();
    initSelectBusinessIdForm();
    setEditFormVal();
    $(".selectpicker").selectpicker('refresh');
}

function initSelectDestForm() {

    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + $("#edit_destUnitId").val(),
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_destId").empty();
            $("#search_destId").append("<option value=''>--请选择--</option>");
            $("#edit_destId").empty();
            $("#edit_destId").append("<option value=''>--请选择--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {

                $("#search_destId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                $("#edit_destId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
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
            $("#edit_origId").empty();
            $("#edit_origId").append("<option value=''>--请选择--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_origId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                $("#edit_origId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
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
            if (defalutCustomerId != "" && defalutCustomerId != undefined) {
                $("#edit_customerType").selectpicker('val',"CT-LS");
            }
        }
    });
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
            $("#search_busnissId").append("<option value='' >--请选择--</option>");
            $("#edit_busnissId").empty();
            $("#edit_busnissId").append("<option value='' >--请选择--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#edit_busnissId").append("<option value='" + json[i].id + "'>" + json[i].name + "</option>");
                $("#search_busnissId").append("<option value='" + json[i].id + "'>" + json[i].name + "</option>");
            }
            if (defaultSaleStaffId != "" && defaultSaleStaffId != undefined) {
                $("#edit_busnissId").val(defaultSaleStaffId);
            }
        }
    });
}
function initSelectBusinessIdFormOnAddNew() {
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
            debugger
            $("#edit_busnissId").empty();
            $("#edit_busnissId").append("<option value='' >--请选择--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#edit_busnissId").append("<option value='" + json[i].id + "'>" + json[i].name + "</option>");

            }
            if (defaultSaleStaffId != "" && defaultSaleStaffId != undefined) {
                $("#edit_busnissId").val(defaultSaleStaffId);
            }
            $(".selectpicker").selectpicker('refresh');
        }
    });
}
function input_keydown() {
    $("#edit_discount").keydown(function (event) {
        if (event.keyCode == 13) {
            setDiscount();
        }
    })
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
            customerTypeId: {
                validators: {
                    notEmpty: {
                        message: '请选择客户类型'
                    }
                }
            },
            destUnitId: {
                validators: {
                    notEmpty: {
                        message: '请选择客户'
                    }
                }
            },
            origId: {
                validators: {
                    notEmpty: {
                        message: '请选择出库仓库'
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
//将整单折扣设置到明细中
function setDiscount() {
    debugger
    if (addDetailgridiRow != null && addDetailgridiCol != null) {
        $("#addDetailgrid").saveCell(addDetailgridiRow, addDetailgridiCol);
        addDetailgridiRow = null;
        addDetailgridiCol = null;
    }
    var discount = $("#edit_discount").val();
    if (discount && discount != null && discount != "") {
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
            var var_totActPrice = Math.round(var_actPrice * parseInt($('#addDetailgrid').getCell(value, "qty")) * 100) / 100;
            $('#addDetailgrid').setCell(value, "totActPrice", var_totActPrice);
            $("#grid-table").setCell(value,"useable",0,{color:'red'});
        });
    }
    setAddFooterData();
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
/*根据权限初始化按钮*/
function initButtonGroup(billStatus){
        $("#buttonGroup").html("" +
            "<button id='SODtl_add' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='addNew(true)'>" +
            "    <i class='ace-icon fa fa-plus'></i>" +
            "    <span class='bigger-110'>新增</span>" +
            "</button>" +
            "<button id='SODtl_search' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='opnenSearchOrderDialog()'>" +
            "    <i class='ace-icon fa fa-barcode'></i>" +
            "    <span class='bigger-110'>查询订单</span>" +
            "</button>" +
            "<button id='SODtl_save' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='save()'>" +
            "    <i class='ace-icon fa fa-save'></i>" +
            "    <span class='bigger-110'>保存</span>" +
            "</button>" +
            "<button id='SODtl_cancel' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='cancel()'>" +
            "    <i class='ace-icon fa fa-undo'></i>" +
            "    <span class='bigger-110'>撤销</span>" +
            "</button>" +
            "<button id='SODtl_addUniqCode' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='addUniqCode()'>" +
            "    <i class='ace-icon fa fa-barcode'></i>" +
            "    <span class='bigger-110'>扫码</span>" +
            "</button>" +
            "<button id='SODtl_wareHouseOut' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='wareHouseOut()'>" +
            "    <i class='ace-icon fa fa-sign-out'></i>" +
            "    <span class='bigger-110'>出库</span>" +
            "</button>" +
            "<button id='SODtl_wareHouseIn' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='wareHouseIn()'>" +
            "    <i class='ace-icon fa fa-sign-in'></i>" +
            "    <span class='bigger-110'>入库</span>" +
            "</button>" +
            "<button id='SODtl_wareHouseRe' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='Returngoods()'>" +
            "    <i class='ace-icon fa fa-reply'></i>" +
            "    <span class='bigger-110'>退货</span>" +
            "</button>" +
            "<button id='SODtl_wareHouseTr' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='changeTr()'>" +
            "    <i class='ace-icon fa fa-share'></i>" +
            "    <span class='bigger-110'>调拨</span>" +
            "</button>" +
            "<button id='SODtl_wareHouseCh' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='exchangeGoods()'>" +
            "    <i class='ace-icon fa fa-exchange'></i>" +
            "    <span class='bigger-110'>换货</span>" +
            "</button>" +
            "<button id='SODtl_doPrint' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='doPrint()'>" +
            "    <i class='ace-icon fa fa-print'></i>" +
            "    <span class='bigger-110'>打印</span>" +
            "</button>" +
            "<button id='SODtl_doPrintA4' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='doPrintA4()'>" +
            "    <i class='ace-icon fa fa-print'></i>" +
            "    <span class='bigger-110'>A4打印</span>" +
            "</button>" +
            "<button id='SODtl_doPrintSanLian' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='doPrintSanLian()'>" +
            "    <i class='ace-icon fa fa-print'></i>" +
            "    <span class='bigger-110'>三联打印</span>" +
            "</button>" +
            "<button id='SODtl_findRetrunno' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='findRetrunno()'>" +
            "    <i class='ace-icon fa fa-search'></i>" +
            "    <span class='bigger-110'>查找退单</span>" +
            "</button>" +
            "<button id='SODtl_findTransferOrder' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='findTransferOrder()'>" +
            "    <i class='ace-icon fa fa-search'></i>" +
            "    <span class='bigger-110'>查找调拨单</span>" +
            "</button>" +
            "<button id='SODtl_findshopMessage' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='findshopMessage()'>" +
            "    <i class='ace-icon fa fa-search'></i>" +
            "    <span class='bigger-110'>查找商城信息</span>" +
            "</button>" +
            "<button id='SODtl_StreamNO' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='sendStreamNO()'>" +
            "    <i class='ace-icon fa fa-search'></i>" +
            "    <span class='bigger-110'>推送物流号</span>" +
            "</button>"

        );
        //订单状态：0，表示录入态
        if (slaeOrder_status != "0" && userId != "admin") {
            $("#edit_guest_button").attr({"disabled": "disabled"});
            $("#SODtl_addUniqCode").attr({"disabled": "disabled"});
        }else {
            $("#edit_guest_button").removeAttr("disabled");
        }
        //如果入库仓库为空，禁止入库按钮
        if ($("#edit_destId").val() && $("#edit_destId").val() != null) {
            $("#SODtl_wareHouseIn").removeAttr("disabled");
        } else {
            $("#SODtl_wareHouseIn").attr({"disabled": "disabled"})
        }
        //判断是否是admin
        /* if (roleid != "0" && roleid != "SHOPUSER") {
         $("#SODtl_doPrintA4").hide();
         }*/
        if(groupid=="JMS"){
            $("#SODtl_doPrintA4").hide();
        }
        $("#addDetail").show();
    loadingButtonDivTable(billStatus);
}
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
            disableButtonIds = ["SODtl_search","SODtl_save","SODtl_cancel","SODtl_addUniqCode","SODtl_wareHouseOut","SODtl_wareHouseRe","SODtl_wareHouseCh","SODtl_findRetrunno"];
            break;
        case "0" :
            disableButtonIds = ["SODtl_wareHouseIn","SODtl_wareHouseRe","SODtl_wareHouseCh"];
            break;
        case "1":
            disableButtonIds = ["SODtl_save","SODtl_cancel","SODtl_addUniqCode"];
            break;
        case "2" :
            disableButtonIds = ["SODtl_save","SODtl_cancel","SODtl_addUniqCode","SODtl_wareHouseOut","SODtl_wareHouseIn","SODtl_wareHouseRe","SODtl_wareHouseCh"];
            break;
        case "3":
            disableButtonIds = ["SODtl_save","SODtl_cancel","SODtl_addUniqCode"];
            break;
        case "6":
            disableButtonIds = ["SODtl_save","SODtl_cancel","SODtl_wareHouseIn","SODtl_wareHouseRe"];
            break;
        case "7":
            disableButtonIds = ["SODtl_save","SODtl_cancel","SODtl_wareHouseIn","SODtl_wareHouseRe"];
            break;
        default:
            disableButtonIds = [];
    }
    //根据单据状态disable按钮
   $.each(privilegeMap['button'],function(index,value){
        if($.inArray(value.privilegeId,disableButtonIds)!= -1){
            $("#"+value.privilegeId).attr({"disabled": "disabled"});
        }else{
            $("#"+value.privilegeId).removeAttr("disabled");
        }
    });
    if (userId == "admin") {
        $("#SODtl_save").removeAttr("disabled");
    }
}
var dialogOpenPage;
var prefixId;
/*
 *@param preId id前缀 search/edit 区分回调框id
 **/
function openSearchGuestDialog(preId) {
    dialogOpenPage = "saleOrder";
    prefixId =preId;
    $("#modal_guest_search_table").modal('show').on('shown.bs.modal', function () {
        initGuestSelect_Grid();
    });
    console.log(prefixId);
    $("#searchGuestDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='confirm_selected_GuestId_sale()'>确认</button>"
    );
}

/**
 * 查找订单
 * */
function opnenSearchOrderDialog(){


    $("#searchbill_dialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='comfirmSeerchBillNo()'>确定</button>"
    );
    $("#saerch-saleOrder-dialog").modal('show').on('hidden.bs.modal', function () {
        $("#saerchbillGrid").clearGridData();
    });

}

function comfirmSeerchBillNo(){
    var rowId = $("#saerchbillGrid").jqGrid("getGridParam", "selrow");
    var rowData = $("#saerchbillGrid").jqGrid('getRowData', rowId);
    searchOrderBillInfo(rowData.billId);
}

/**
 * 查找订单信息
 * */

function searchOrderBillInfo(billNo){
    _resetForm();
    $("#search_billId").val(billNo);
    _search();
    $("#saerch-saleOrder-dialog").modal('hide');
    autoSelect =true;
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
        dtlRow.totPrice = dtlRow.qty * dtlRow.price;
        dtlRow.totActPrice = (dtlRow.qty * dtlRow.actPrice).toFixed(2);
        if(dtlRow.id){
            $("#addDetailgrid").setRowData(dtlRow.id, dtlRow);
        }else{
            $("#addDetailgrid").setRowData(value, dtlRow);
        }
    });

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
            if (parseInt(styleRow.bargainPrice)!=0&&styleRow.bargainPrice!=""){
                productInfo.price = styleRow.bargainPrice;
            }else {
                if (ct == "CT-AT") {//省代价格
                    productInfo.price = styleRow.puPrice;
                } else if (ct == "CT-ST") {//门店价格
                    productInfo.price = styleRow.wsPrice;
                } else if (ct == "CT-LS") {//吊牌价格
                    productInfo.price = styleRow.price;
                }
            }
            productInfo.outQty = 0;
            productInfo.inQty = 0;
            productInfo.status = 0;
            productInfo.inStatus = 0;
            productInfo.outStatus = 0;
            if (parseInt(styleRow.bargainPrice)!=0&&styleRow.bargainPrice!=""){
                productInfo.discount = 100;
            }else {
                if ($("#edit_discount").val() && $("#edit_discount").val() !== null) {
                    productInfo.discount = $("#edit_discount").val();
                    console.log(productInfo.discount);
                } else {
                    productInfo.discount = 100;
                }
            }
            productInfo.puPrice=styleRow.puPrice;
            //判断实际价格是不是小于门店批发价格
            if (parseInt(styleRow.bargainPrice)!=0&&styleRow.bargainPrice!="") {
                productInfo.actPrice = Math.round(productInfo.price * productInfo.discount) / 100;
                productInfo.abnormalStatus=0;
            }else {
                if(Math.round(productInfo.price * productInfo.discount) / 100<styleRow.wsPrice&&isUserAbnormal){
                    productInfo.actPrice = styleRow.wsPrice;
                    productInfo.discount = parseFloat(styleRow.wsPrice/productInfo.price).toFixed(2)*100;
                    productInfo.abnormalStatus=1;
                }else{
                    productInfo.actPrice = Math.round(productInfo.price * productInfo.discount) / 100;
                    productInfo.abnormalStatus=0;
                }
            }
            productInfo.totPrice = productInfo.qty * productInfo.price;
            productInfo.totActPrice = productInfo.qty * productInfo.actPrice;
            productInfo.sku = productInfo.code;
            productInfo.inStockType = styleRow.class6;
            var stylePriceMap={};
            stylePriceMap['price']=styleRow.price;
            stylePriceMap['wsPrice']=styleRow.wsPrice;
            stylePriceMap['puPrice']=styleRow.puPrice;
            productInfo.stylePriceMap=JSON.stringify(stylePriceMap);
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
function deletenoOutPutCode(noOutPutCode,totActPrice) {
    cs.showProgressBar();
    $.ajax({
        dataType: "json",
        url: basePath + "/logistics/saleOrderBill/deletenoOutPutCode.do",
        data: { billNo:$("#edit_billNo").val(),noOutPutCode:noOutPutCode},
        type: "POST",
        success: function (msg) {
            cs.closeProgressBar();
            if (msg.success) {
                saveother(totActPrice);

            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}

function saveother(totActPrice) {
    cs.showProgressBar();
    $("#edit_customerType").removeAttr('disabled');
    $("#edit_origId").removeAttr('disabled');
    $("#edit_destId").removeAttr('disabled');
    $("#edit_busnissId").removeAttr('disabled');
    if ($("#edit_origId").val() == $("#edit_destId").val()) {
        bootbox.alert("不能在相同的单位之间销售");
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
    if (addDetailgridiRow != null && addDetailgridiCol != null) {
        $("#addDetailgrid").saveCell(addDetailgridiRow, addDetailgridiCol);
        addDetailgridiRow = null;
        addDetailgridiCol = null;
    }
    //实收金额的计算
    var payPrice = $("#edit_payPrice").val();
    if (parseFloat(payPrice) > 0) {
        var summun = parseFloat(payPrice) - parseFloat(totActPrice);
        if (summun > 0) {
            $("#edit_payPrice").val(summun.toFixed(2));
        }
    }
    saveAjax();
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

/*查看唯一吗明细*/
function showCodesDetail(uniqueCodes) {
    $("#show-uniqueCode-list").modal('show');
    codeListReload(uniqueCodes,billNo);
}


function addUniqCode() {
    inOntWareHouseValid = 'addPage_scanUniqueCode';
    var origId = $("#edit_origId").val();
    taskType = 0;
    wareHouse = origId;
    billNo = $("#edit_billNo").val();
    var ct;
    if($('#sale_discount_div').is(':hidden')){
        ct = "CT-LS";
    }else {
        ct = $("#edit_customerType").val();
    }
    if (ct && ct != null) {
        if (origId && origId != null) {
            $("#dialog_buttonGroup").html("" +
                "<button  type='button' id = 'so_savecode_button'  class='btn btn-primary' onclick='addProductsOnCode()'>保存</button>"
            );
            $("#add-uniqCode-dialog").modal('show').on('hidden.bs.modal', function () {
                $("#uniqueCodeGrid").clearGridData();
            });
            initUniqeCodeGridColumn(ct);
            $("#codeQty").text(0);
        } else {
            bootbox.alert("出库仓库不能为空！")
        }
    } else {
        bootbox.alert("请选择客户！");
    }
    allCodes = "";
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
                addNew(false)
                _search();
            } else {
            }
        }
    });
}
function cancelAjax(billId) {
    $.ajax({
        dataType: "json",
        url: basePath + "/logistics/saleOrderBill/cancel.do",
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


function addProductsOnCode() {
    var productListInfo = [];
    if (!$('#so_savecode_button').prop('disabled')) {
        $("#so_savecode_button").attr({"disabled": "disabled"});
        var ct = $("#edit_customerType").val();
        $.each($("#uniqueCodeGrid").getDataIDs(), function (index, value) {
            var productInfo = $("#uniqueCodeGrid").getRowData(value);
            if(productInfo.code!=""&&productInfo.code!=undefined){
                productInfo.qty = 1;
                if($('#sale_discount_div').is(':hidden')){
                    productInfo.price = productInfo.price;
                }else {
                    if (ct == "CT-AT") {//省代价格
                        productInfo.price = productInfo.puPrice;
                    } else if (ct == "CT-ST") {//门店价格
                        productInfo.price = productInfo.wsPrice;
                    } else if (ct == "CT-LS") {//吊牌价格
                        productInfo.price = productInfo.price;
                    }
                }
                if ($("#edit_discount").val() && $("#edit_discount").val() !== null) {
                    productInfo.discount = $("#edit_discount").val();
                } else {
                    productInfo.discount = 100;
                }
                productInfo.puPrice=productInfo.puPrice;
                //判断实际价格是不是小于门店批发价格
                if($('#sale_discount_div').is(':hidden')) {
                    productInfo.actPrice = Math.round(productInfo.price * productInfo.discount) / 100;
                    productInfo.abnormalStatus=0;
                }else {
                    if(Math.round(productInfo.price * productInfo.discount) / 100<productInfo.wsPrice&&isUserAbnormal){
                        productInfo.actPrice = productInfo.wsPrice;
                        productInfo.discount = parseFloat(productInfo.wsPrice/productInfo.price).toFixed(2)*100;
                        productInfo.abnormalStatus=1;
                    }else{
                        productInfo.actPrice = Math.round(productInfo.price * productInfo.discount) / 100;
                        productInfo.abnormalStatus=0;
                    }
                }
                productInfo.outQty = 0;
                productInfo.inQty = 0;
                productInfo.status = 0;
                productInfo.inStatus = 0;
                productInfo.outStatus = 0;
                productInfo.uniqueCodes = productInfo.code;
                productInfo.totPrice = productInfo.price;
                productInfo.totActPrice = productInfo.actPrice;
                var stylePriceMap={};
                stylePriceMap['price']=productInfo.price;
                stylePriceMap['wsPrice']=productInfo.wsPrice;
                stylePriceMap['puPrice']=productInfo.puPrice;
                productInfo.stylePriceMap=JSON.stringify(stylePriceMap);
                productListInfo.push(productInfo);
            }
        });
        if (productListInfo.length == 0) {
            bootbox.alert("请添加唯一码");
            $("#so_savecode_button").removeAttr("disabled");
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
                        $("#so_savecode_button").removeAttr("disabled");
                        return true;
                    }
                    dtlRow.qty = parseInt(dtlRow.qty) + 1;
                    dtlRow.totPrice = dtlRow.qty * dtlRow.price;
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
        setFooterData();
        saveother(0 - alltotActPrice);
    }
}
function addProductsNoOutPutCode(productInfo) {
    console.log(productInfo);
    var ct = $("#edit_customerType").val();
    var productListInfo = [];
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
        productInfo.noOutPutCode = productInfo.code;
        if ($("#edit_discount").val() && $("#edit_discount").val() !== null) {
            productInfo.discount = $("#edit_discount").val();
        } else {
            productInfo.discount = 100;
        }
        productInfo.puPrice=productInfo.puPrice;
        //判断实际价格是不是小于门店批发价格
        if(Math.round(productInfo.price * productInfo.discount) / 100<productInfo.wsPrice&&isUserAbnormal){
            productInfo.actPrice = productInfo.wsPrice;
            productInfo.discount = parseFloat(productInfo.wsPrice/productInfo.price).toFixed(2)*100;
            productInfo.abnormalStatus=1;
        }else{
            productInfo.actPrice = Math.round(productInfo.price * productInfo.discount) / 100;
            productInfo.abnormalStatus=0;
        }
        //productInfo.actPrice = Math.round(productInfo.price * productInfo.discount) / 100;
        productInfo.totPrice = productInfo.price;
        productInfo.totActPrice = productInfo.actPrice;
        var stylePriceMap={};
        stylePriceMap['price']=productInfo.price;
        stylePriceMap['wsPrice']=productInfo.wsPrice;
        stylePriceMap['puPrice']=productInfo.puPrice;
        productInfo.stylePriceMap=JSON.stringify(stylePriceMap);
        productListInfo.push(productInfo);
    }
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
                if(dtlRow.noOutPutCode.indexOf(value.code) != -1){
                    isAdd = false;
                    return;
                }else{
                    dtlRow.qty = parseInt(dtlRow.qty) + 1;
                    dtlRow.noOutPutCode = dtlRow.noOutPutCode + "," + value.code;
                }

                dtlRow.totPrice = dtlRow.qty * dtlRow.price;
                dtlRow.totActPrice = dtlRow.qty * dtlRow.actPrice;
                alltotActPrice += dtlRow.qty * dtlRow.actPrice;

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
    setFooterData();
    saveother(0 - alltotActPrice);
}


function saveAjax() {
    $("#edit_billDate").val(updateTime($("#edit_billDate").val()));
    var dtlArray = [];
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        dtlArray.push(rowData);
    });
    console.log(array2obj($("#editForm").serializeArray()));
    $("#SODtl_wareHouseIn").attr({"disabled": "disabled"});
    $.ajax({
        dataType: "json",
        async: true,
        url: basePath + "/logistics/saleOrderBill/save.do",
        data: {
            saleOrderBillStr: JSON.stringify(array2obj($("#editForm").serializeArray())),
            strDtlList: JSON.stringify(dtlArray),
            userId: userId
        },
        type: "POST",
        success: function (msg) {
            cs.closeProgressBar();
            $("#SODtl_wareHouseIn").removeAttr("disabled");
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#editForm").setFromData(msg.result);
                $("#addDetailgrid").jqGrid('setGridParam', {
                    datatype: "json",
                    page: 1,
                    url: basePath + "/logistics/saleOrderBill/findBillDtl.do?billNo=" + msg.result.billNo,
                });
                $("#addDetailgrid").trigger("reloadGrid");
                $("#grid").trigger("reloadGrid");
                $("#SODtl_adddoPrint1").show();
                _search();
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
    $("#edit_customerType").attr({"disabled": "disabled"});
    window.isTrue = true;
}

/**
 * 保存按钮方法
 * */
function save() {
    var p = new Promise(function (resolve,reject) {
        cs.showProgressBar();
        $("#edit_customerType").removeAttr('disabled');
        $("#edit_origId").removeAttr('disabled');
        $("#edit_destId").removeAttr('disabled');
        $("#edit_busnissId").removeAttr('disabled');

        if ($("#edit_origId").val() == $("#edit_destId").val()) {
            bootbox.alert("不能在相同的单位之间销售");
            cs.closeProgressBar();
            return false;
        }

        $("#editForm").data('bootstrapValidator').destroy();
        $('#editForm').data('bootstrapValidator', null);
        initEditFormValid();
        $('#editForm').data('bootstrapValidator').validate();
        if (!$('#editForm').data('bootstrapValidator').isValid()) {
            cs.closeProgressBar();
            $("#edit_customerType").attr('disabled',true);
            $("#edit_origId").attr('disabled',true);
            $("#edit_destId").attr('disabled',true);
            $("#edit_busnissId").attr('disabled',true);
            reject("失败");
            return false;
        }
        if ($("#addDetailgrid").getDataIDs().length == 0) {
            bootbox.alert("请添加销售商品！");
            reject("请添加销售商品！");
            $("#edit_customerType").attr('disabled',true);
            $("#edit_origId").attr('disabled',true);
            $("#edit_destId").attr('disabled',true);
            $("#edit_busnissId").attr('disabled',true);
            cs.closeProgressBar();
            return false;
        }
        if (addDetailgridiRow != null && addDetailgridiCol != null) {
            $("#addDetailgrid").saveCell(addDetailgridiRow, addDetailgridiCol);
            addDetailgridiRow = null;
            addDetailgridiCol = null;
        }
        //客户余额变动
        guestBalanceChange();
        resolve("成功");
    });
    return p;
}

function edit_discount_onblur() {
    setDiscount();
}

/*
 *客户余额变动
 * */
function guestBalanceChange() {
    var actPrice = parseFloat($("#edit_actPrice").val());
    var payPrice = parseFloat($("#edit_payPrice").val());
    var preBalance = parseFloat($("#edit_pre_Balance").val());
    var afterBalance = parseFloat(preBalance + payPrice - actPrice).toFixed(2);

    $("#edit_after_Balance").val(afterBalance);
    if (afterBalance < 0) {
        $("#SODtl_save").attr({"disabled": "disabled"});

        if (confirm("客户余额不足，是否继续开单")) {
            saveAjax();
            flag = true;
        }else{
            cs.closeProgressBar();
        }

        $("#SODtl_save").removeAttr("disabled");
    } else {
        saveAjax();
        flag = true;
    }
}
/*
 * 出库调用方法
 * */
function wareHouseOut() {
    cs.showProgressBar();
    $("#SODtl_wareHouseOut").attr({"disabled": "disabled"});
    var billNo = $("#edit_billNo").val();
    if (billNo && billNo != null) {
        if (outStockCheck()) {
            cs.closeProgressBar();
            $("#SODtl_wareHouseOut").removeAttr("disabled");
            return;
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
        taskType = 0;
        wareHouse = $("#edit_origId").val();
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
                    var curOutQty;
                    rowData.outQty == "" ? curOutQty = 0 : curOutQty = rowData.outQty;
                    var codes = rowData.uniqueCodes.split(",");
                    if (codes && codes != null && codes != "") {
                        $.each((codes), function (index, value) {
                            if (rowData.qty > curOutQty) {
                                if (uniqueCodes_inHouse.indexOf(value) != -1) {
                                    var epc = {};
                                    epc.code = value;
                                    epc.styleId = rowData.styleId;
                                    epc.sizeId = rowData.sizeId;
                                    epc.colorId = rowData.colorId;
                                    epc.qty = 1;
                                    epc.sku = rowData.sku;
                                    epcArray.push(epc);
                                    curOutQty++;
                                }
                            }
                        })
                    }
                });
                if (epcArray.length == 0) {
                    cs.closeProgressBar();
                    $.gritter.add({
                        text: "请扫码出库",
                        class_name: 'gritter-success  gritter-light'
                    });
                    cs.closeProgressBar();
                    $("#SODtl_wareHouseOut").removeAttr("disabled");
                    edit_wareHouseOut();
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
                    url: basePath + "/logistics/saleOrderBill/convertOut.do",
                    data: {
                        billNo: billNo,
                        strEpcList: JSON.stringify(epcArray),
                        strDtlList: JSON.stringify(dtlArray),
                        userId: userId
                    },
                    type: "POST",
                    success: function (msg) {
                        cs.closeProgressBar();
                        $("#SODtl_wareHouseOut").removeAttr("disabled");
                        if (msg.success) {
                            $.gritter.add({
                                text: msg.msg,
                                class_name: 'gritter-success  gritter-light'
                            });
                            $("#modal-addEpc-table").modal('hide');

                            var sum_qty = parseInt($("#addDetailgrid").footerData('get').qty);        //reload前总数量
                            var sum_outQty = parseInt($("#addDetailgrid").footerData('get').outQty);  //reload前总出库数量
                            $("#addDetailgrid").jqGrid('setGridParam', {
                                page: 1,
                                url: basePath + "/logistics/saleOrderBill/findBillDtl.do?billNo=" + billNo
                            });
                            $("#addDetailgrid").trigger("reloadGrid");
                            _search();
                            var all_outQty = sum_outQty + epcArray.length;
                            var diff_qty = sum_qty - all_outQty;
                            if (pageType === "edit") {

                                //出库成功后，禁止保存扫码和表单上的控件
                                $("#search_busnissId").attr('disabled', true);
                                $("#search_origId").attr('disabled', true);
                                $("#search_destId").attr('disabled', true);
                                $("#search_billDate").attr('readOnly', true);
                                $("#search_guest_button").attr({"disabled": "disabled"});
                                $("#SODtl_addUniqCode").attr({"disabled": "disabled"});
                                $("#SODtl_save").attr({"disabled": "disabled"});
                                if (sum_qty > all_outQty) {
                                    $.gritter.add({
                                        text: "已出库数量为：" + all_outQty + "；剩余数量为：" + diff_qty + "，其余商品请扫码出库",
                                        class_name: 'gritter-success  gritter-light'
                                    });
                                    edit_wareHouseOut();
                                } else if (sum_qty === all_outQty) {
                                    $.gritter.add({
                                        text: "共" + all_outQty + "件商品，已全部出库",
                                        class_name: 'gritter-success  gritter-light'
                                    });
                                }
                            } else if (pageType === "add") {
                                var alertMessage;
                                if (sum_qty > all_outQty) {
                                    alertMessage = "已出库数量为：" + all_outQty + "；剩余数量为：" + diff_qty + "，其余商品请扫码出库"
                                } else if (sum_qty == all_outQty) {
                                    alertMessage = "共" + all_outQty + "件商品，已全部出库";
                                }
                                bootbox.alert({
                                    buttons: {ok: {label: '确定'}},
                                    message: alertMessage,
                                    callback: function () {

                                    }
                                });
                            }

                        } else {
                            bootbox.alert(msg.msg);
                        }
                    }
                });
            }
        });


    } else {
        cs.closeProgressBar();
        bootbox.alert("请先保存当前单据");
    }
}

function outStockCheck() {
    var sum_qty = parseInt($("#addDetailgrid").footerData('get').qty);
    var sum_outQty = parseInt($("#addDetailgrid").footerData('get').outQty);
    var sum_returnQty = parseInt($("#addDetailgrid").footerData('get').returnQty);
    if (isNaN(sum_returnQty)) {
        sum_returnQty = 0;
    }
    if (sum_qty <= sum_outQty + sum_returnQty) {
        $.gritter.add({
            text: '已全部出库',
            class_name: 'gritter-success  gritter-light'
        });
        return true;
    }
}

/**
 * 扫码出库
 * */
function edit_wareHouseOut() {
    skuQty = {};
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        skuQty[rowData.sku] = rowData.outQty;
    });
    inOntWareHouseValid = 'wareHouseOut_valid';
    billNo = $("#edit_billNo").val();
    var ct = $("#eidt_customerType").val();

    $("#dialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='confirmWareHouseOut()'>确认出库</button>"
    );
    $("#add-uniqCode-dialog").modal('show').on('hidden.bs.modal', function () {
        $("#uniqueCodeGrid").clearGridData();
    });
    initUniqeCodeGridColumn(ct);
    $("#codeQty").text(0);
    allCodes = "";
}

/*
 * 扫码出库确认
 * */
function confirmWareHouseOut() {
    cs.showProgressBar();
    $("#so_comfirmout_button").attr({"disabled": "disabled"});
    var billNo = $("#edit_billNo").val();
    var epcArray = [];
    $.each($("#uniqueCodeGrid").getDataIDs(), function (index, value) {
        var rowData = $("#uniqueCodeGrid").getRowData(value);
        epcArray.push(rowData);
    });
    if (epcArray.length == 0) {
        bootbox.alert("请添加唯一码!");
        $("#so_comfirmout_button").removeAttr("disabled");
        cs.closeProgressBar();
        return;
    }
    $.each(epcArray, function (index, value) {
        $.each($("#addDetailgrid").getDataIDs(), function (dtlIndex, dtlValue) {
            var dtlRow = $("#addDetailgrid").getRowData(dtlValue);
            if (value.sku === dtlRow.sku) {
                if (dtlRow.uniqueCodes.indexOf(value.code) != -1) {
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
        url: basePath + "/logistics/saleOrderBill/convertOut.do",
        data: {
            billNo: billNo,
            strEpcList: JSON.stringify(epcArray),
            strDtlList: JSON.stringify(dtlArray),
            userId: userId
        },
        type: "POST",
        success: function (msg) {
            $("#so_comfirmout_button").removeAttr("disabled");
            cs.closeProgressBar();
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#modal-addEpc-table").modal('hide');
                $("#addDetailgrid").trigger("reloadGrid");
                _search();
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
    $("#add-uniqCode-dialog").modal('hide');
}
/**
 * 入库方法
 * */
function wareHouseIn() {
    skuQty = {};
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        skuQty[rowData.sku] = rowData.inQty;
    });
    inOntWareHouseValid = 'wareHouseIn_valid';
    taskType = 1;
    var destId = $("#edit_destId").val();
    wareHouse = destId;
    billNo = $("#edit_billNo").val();
    var ct = $("#edit_customerType").val();
    if (destId && destId != null) {
        $("#dialog_buttonGroup").html("" +
            "<button type='button' id='In_dialog_buttonGroup'  class='btn btn-primary' onclick='confirmWareHouseIn()'>确认入库</button>"
        );
        $("#add-uniqCode-dialog").modal('show').on('hidden.bs.modal', function () {
            $("#uniqueCodeGrid").clearGridData();
        });
        initUniqeCodeGridColumn(ct);
        $("#codeQty").text(0);
    } else {
        bootbox.alert("入库仓库不能为空！");
    }
    allCodes = "";
}

/**
 * 扫码确认入库
 * */
function confirmWareHouseIn() {
    cs.showProgressBar();
    $("#In_dialog_buttonGroup").attr({"disabled": "disabled"});
    var billNo = $("#edit_billNo").val();
    var epcArray = [];
    $.each($("#uniqueCodeGrid").getDataIDs(), function (index, value) {
        var rowData = $("#uniqueCodeGrid").getRowData(value);
        epcArray.push(rowData);
    });
    if (epcArray.length == 0) {
        bootbox.alert("请添加唯一码!");
        $("#SODtl_addUniqCode").removeAttr("disabled");
        cs.closeProgressBar();
        return;
    }

    $.ajax({
        dataType: "json",
        async: true,
        url: basePath + "/logistics/saleOrderBill/convertIn.do",
        data: {
            billNo: billNo,
            strEpcList: JSON.stringify(epcArray),
            userId: userId
        },
        type: "POST",
        success: function (msg) {
            cs.closeProgressBar();
            $("#SODtl_addUniqCode").removeAttr("disabled");
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#modal-addEpc-table").modal('hide');
                $("#addDetailgrid").trigger("reloadGrid");
                _search();
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
    $("#add-uniqCode-dialog").modal('hide');

}
/**
 * 销售转退货
 * */
function Returngoods() {
    $("#edit_customerType").removeAttr('disabled');
    $("#edit_origId").removeAttr('disabled');
    $("#edit_destId").removeAttr('disabled');

    $("#editForm").data('bootstrapValidator').destroy();
    $('#editForm').data('bootstrapValidator', null);
    initEditFormValid();
    $('#editForm').data('bootstrapValidator').validate();
    if (!$('#editForm').data('bootstrapValidator').isValid()) {
        return;
    }
    if ($("#addDetailgrid").getDataIDs().length == 0) {
        bootbox.alert("请添加销售商品！");
        return;
    }
    if (addDetailgridiRow != null && addDetailgridiCol != null) {
        $("#addDetailgrid").saveCell(addDetailgridiRow, addDetailgridiCol);
        addDetailgridiRow = null;
        addDetailgridiCol = null;
    }
    /*if(isretrun==true){
     bootbox.alert("一行中的退货数量过大！");
     return;
     }*/
    var dtlArray = [];
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        if (rowData.returnbillNo == "" && rowData.returnQty > 0) {
            dtlArray.push(rowData);
        }
    });
    if (dtlArray.length > 0) {
        cs.showProgressBar();
        $.ajax({
            dataType: "json",
            async: true,
            url: basePath + "/logistics/saleOrderBill/saveRetrun.do",
            data: {
                billNo: $("#edit_billNo").val(),
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
                    $("#addDetailgrid").trigger("reloadGrid");
                    setFooterData();
                } else {
                    bootbox.alert(msg.msg);
                }
            }
        });
    } else {
        $.gritter.add({
            text: "无可退商品！",
            class_name: 'gritter-success  gritter-light'
        });
    }

}


/**
 * 换货
 * * */
function exchangeGoods() {
    wareHouse = $("#edit_origId").val();
    billNo = $("#edit_billNo").val();
    $("#exchangeGoods-dialog").modal('show');
    $("#addDetailgrid").trigger("reloadGrid");
}

/**
 * 查找退单
 * */
function findRetrunno() {
    $("#show-findRetrunNo-list").modal('show');
    $('#findRetrunNoListGrid').jqGrid("clearGridData");
    $('#findRetrunNoListGrid').jqGrid('GridUnload');
    initUniqueretrunList();
    retrunListReload();
    $("#findRetrunNoListGrid").trigger("reloadGrid");
}
/**
 * 查找调拨单
 * */
function findTransferOrder() {
    $("#show-findTransferOrder-list").modal('show');
    $('#findTransferOrderGrid').jqGrid("clearGridData");
    $('#findTransferOrderGrid').jqGrid('GridUnload');
    initUniqueTransferOrderList();
    retrunTransferOrderListReload();
    $("#findTransferOrderGrid").trigger("reloadGrid");
}

/**
 * 查找商城信息单号
 * */
function findshopMessage() {
    $("#show-findWxShop-list").modal('show');
    initfindWxShopList();

}
/**
 * 推送物流号
 * */
function sendStreamNO() {
    $("#show-sendStreamNO-list").modal('show');

}

/**
 * 热敏打印
 * */
function doPrint() {
    /*$("#editForm").resetForm();*/
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
            type:"SO",
            ruleReceipt:"58mm"
        },
        success: function (msg) {
            if (msg.success) {
                var addcont = "";
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
            type:"SO",
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
            type:"SO",
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


function _resetForm(){
    $("#searchForm").clearForm();
    $("#search_destId").val();
    $("#search_origId").val();
    $("#filter_INI_outStatus").val();
    $("#filter_INI_inStatus").val();
    $(".selectpicker").selectpicker('refresh');
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
            type:"SO"
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
function loadingTableName() {
    $.ajax({
        dataType: "json",
        async: false,
        url: basePath + "/logistics/saleOrderBill/findResourceTable.do",
        type: "POST",
        success: function (msg) {
            if (msg.success) {
                var result=msg.result;
                for(var i=0;i<result.length;i++){
                    if(result[i].ishow===1){
                        $("#addDetailgrid").setGridParam().hideCol(result[i].buttonId);
                    }
                }
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
                    var plp = printCodeArray[i];
                    var message = cont[plp];
                    $("#edit-dialogSanLian").find("#"+plp).text(message);
                }
                var tbodyCont="";
                for(var a=0;a<contDel.length;a++){
                    var del=contDel[a];
                    var printTableCode=print.printTableCode.split(",");
                    tbodyCont+=" <tr style='border-top:1px ;padding-top:5px;'>"
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
                /* var a=$("#edit-dialogSanLian").html();
                 LODOP.ADD_PRINT_TABLE(100,1,800,300,a);
                 LODOP.PREVIEW();
                 $("#edit-dialog-print").hide();*/
                /*var LODOP = getLodop();
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
                 LODOP.PREVIEW();
                 //LODOP.PRINT();
                 $("#edit-dialog-print").hide();*/


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
                    debugger
                    var plp = printCodeArray[i];
                    var message = cont[plp];
                    $("#edit-dialogSanLian").find("#"+plp).text(message);
                }
                var tbodyCont=""
                for(var a=0;a<contDel.length;a++){
                    var del=contDel[a];
                    var printTableCode=print.printTableCode.split(",");
                    tbodyCont+=" <tr style='border-top:1px ;padding-top:5px;'>"
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
                /* var a=$("#edit-dialogSanLian").html();
                 LODOP.ADD_PRINT_TABLE(100,1,800,300,a);
                 LODOP.PREVIEW();
                 $("#edit-dialog-print").hide();*/
                /*var LODOP = getLodop();
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
                 LODOP.PREVIEW();
                 //LODOP.PRINT();
                 $("#edit-dialog-print").hide();*/


            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}
/**
 * 转调拨申请单
 */
function changeTr() {
    cs.closeProgressBar();
    var billNo=$("#edit_billNo").val();
    if(billNo!=""&&billNo!=undefined){
        $.ajax({
            dataType: "json",
            url: basePath + "/logistics/saleOrderBill/changeTr.do",
            type: "POST",
            data: {
                billNo:billNo
            },
            success: function (msg) {
                cs.closeProgressBar();
                if (msg.success) {
                    $.gritter.add({
                        text: msg.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                    $("#addDetailgrid").trigger("reloadGrid");
                } else {
                    bootbox.alert(msg.msg);
                }
            }
        });
    }else{
        bootbox.alert("单号不能为空");
        cs.closeProgressBar();
    }

}