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
var comsigment_status;
var comsigment_customerType;
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
        /*回车监事件*/
        keydown();
        initForm();
        /*初始化右侧grig*/
        initAddGrid();
        /*初始化右侧表单验证*/
        initEditFormValid();
        //动态初始化页面
        loadingButtonDivTable(0);
        resolve("success");
    });
    return promise;
}

function initForm() {
    initSelectOrigForm();
    initSelectDestForm();
    initSelectBusinessIdForm();
    initSelectDestEditForm();
    initCustomerTypeForm();
    initButtonGroup(pageType);
    /* if(billNo){
     bootbox.alert("单据"+billNo+"正在编辑中");
     }else{
     sessionStorage.removeItem("billNoConsignment");
     }*/
    if (pageType === "add") {
        $("#edit_origUnitId").val(defalutCustomerId);
        $("#edit_origUnitName").val(defalutCustomerName);
        $("#edit_discount").val(defalutCustomerdiscount);
        $("#edit_destId").val(defaultWarehId);
        if (defaultSaleStaffId != "" && defaultSaleStaffId != undefined) {
            //addUniqCode();
        }

    }
    $("#edit_billDate").val(getToDay("yyyy-MM-dd"));
    $(".selectpicker").selectpicker('refresh');
}
function initSelectOrigForm() {
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId="+$("#search_origUnitId").val(),
        cache: false,
        async: true,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_origId").empty();
            $("#search_origId").append("<option value='' >--请选择出库仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_origId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
            }
        }
    });
}

function initSelectDestForm() {
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId="+curOwnerId,
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
            if(defaultWarehId!=undefined&&defaultWarehId!=""){
                $("#search_destId").val(defaultWarehId);
            }
        }
    });
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
                    $("#edit_destId").trigger('chosen:updated');
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
                    $("#edit_destId").trigger('chosen:updated');
                }
            }
        });
    }

}
function initSearchGrid() {
    var url="";
    if (cargoTrack=="cargoTracking"){
        url= basePath + "/logistics/Consignment/findBill.do?billNo="+cTbillNo;
    }else {
        url = basePath + "/logistics/Consignment/page.do?filter_GTI_status=-1&userId="+userId;
    }
    $("#grid").jqGrid({
        height: 'auto',
        datatype: 'json',
        url: url,
        mtype: 'POST',
        colModel: [
            {name: 'billNo', label: "单号", width: 50,hidden: true},
            {name: "status", hidden: true},
            {name: 'outStatus', label: '出库状态', hidden: true},
            {name: 'inStatus', label: '入库状态', hidden: true},
            {name: 'billDate', label: '单据日期', width: 30},
            {
                name: '', label: '状态', width: 15, align: "center",
                formatter: function (cellValue, options, rowObject) {
                    var html = "";
                    switch (rowObject.status) {
                        case -2:
                            html = "<i class='fa fa-file-text purple' title='草稿'></i>";
                            break;
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
                name: 'outStatusImg', label: '出库状态', width: 30, align: 'center', hidden: true,
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
            },
            {
                name: 'inStatusImg', label: '入库状态', width: 30, align: 'center', hidden: true,
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

            {name: 'customerType', label: "客户类型", hidden: true},
            {name: 'customerTypeName', label: "客户类型", hidden: true},
            {
                label: "客户类型", width: 25,
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.customerType == "CT-AT")
                        return "省代客户";
                    else if (rowObject.customerType == "CT-ST")
                        return "门店客户";
                    else if (rowObject.customerType == "CT-LS")
                        return "零售客户"
                }
            },
            {name: 'origUnitId', label: '寄售客户ID', hidden: true},
            {name: 'origUnitName', label: '寄售客户', width: 30},
            {name: 'origId', label: '出库仓库ID', hidden: true},
            {name: 'ownerId',  hidden: true},
            {name: 'destId', label: '收货仓库ID', hidden: true},
            {name: 'destName', label: '收货仓库', width: 30,hidden: true},
            {name: 'discount', label: '整单折扣', hidden: true},
            {name: 'busnissId', label: '销售员', hidden: true},
            {name: 'destUnitId', label: '收货方ID', hidden: true},
            {name: 'destUnitName', label: '收货方', width: 30, hidden: true},
            {name: 'totQty', label: '单据数量', width: 15,align:"center"},
            {name: 'totOutQty', label: '已出库数量', width: 30, hidden: true},
            {name: 'totOutVal', label: '总出库金额', width: 30, hidden: true},
            {name: 'totInQty', label: '已入库数量', width: 15,align:"center"},
            {name: 'totInVal', label: '总入库金额', width: 20,
                formatter: function (cellValue, options, rowObject) {
                    if(cellValue) {
                        var totInVal = cellValue.toFixed(2);
                        return totInVal;
                    }else{
                        return cellValue;
                    }
                }
            },
            {name: 'payPrice', label: '实付金额', width: 30, hidden: true,
                formatter: function (cellValue, options, rowObject) {
                    if(cellValue) {
                        var payPrice = cellValue.toFixed(2);
                        return payPrice;
                    }else{
                        return cellValue;
                    }
                }
            },
            {name: 'remark', label: '备注', width: 20}

        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 50,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: true,
        sortname: 'billNo',
        sortorder: "desc",
        autoScroll: false,
        footerrow: true,
        gridComplete: function () {
            setFooterData();
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
function initDetailData(rowid) {
    var rowData = $("#grid").getRowData(rowid);
    $("#editForm").setFromData(rowData);
    comsigment_status=rowData.status;
    comsigment_customerType=rowData.customerType;
    $('#addDetailgrid').jqGrid("clearGridData");
    $('#addDetailgrid').jqGrid('GridUnload');
    initeditGrid(rowData.billNo);
    pageType="edit";
    initButtonGroup(pageType);
    $("#addDetailgrid").trigger("reloadGrid");
    loadingButtonDivTable(comsigment_status);
}
var beforsale = 0;
var readysale = 0;
var isfrist = true;
function initeditGrid(billId) {
    billNo = billId;
    $("#addDetailgrid").jqGrid({
        height: "auto",
        datatype: "json",
        url: basePath + "/logistics/Consignment/returnDetails.do?billNo=" + billNo,
        colModel: [
            {name: 'id', label: 'id', hidden: true},
            {name: 'billId', label: 'billId', hidden: true},
            {name: 'billNo', label: 'billNo', hidden: true},
            {name: 'status', hidden: true},
            {name: "ownerId", hidden: true},
            {name: 'inStatus', hidden: true},
            {name: 'outStatus', hidden: true},
            {
                name: "", label: '操作', width: 30, align: 'center', sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.inStatus != 4 || rowObject.inStatus != 1) {
                        return "<a href='javascript:void(0);' onclick=saveItem('" + options.rowId + ")'><i class='ace-icon ace-icon fa fa-save' title='保存'></i></a>"
                            + "<a href='javascript:void(0);' style='margin-left: 20px' onclick=deleteItem('" + options.rowId + "')><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";
                    } else {
                        return "";
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
            {name: 'styleId', label: '款号', width: 20,hidden: true},
            {name: 'colorId', label: '色码', width: 20,hidden: true},
            {name: 'sizeId', label: '尺码', width: 20,hidden: true},
            {name: 'styleName', label: '款名', width: 20},
            {name: 'colorName', label: '颜色', width: 20,hidden: true},
            {name: 'sizeName', label: '尺码', width: 20,hidden: true},
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
            } else if (cellname === "qty") {
                $('#addDetailgrid').setCell(rowid, "totPrice", -Math.abs(Math.round($('#addDetailgrid').getCell(rowid, "price") * value * 100) / 100));
                $('#addDetailgrid').setCell(rowid, "totActPrice", -Math.abs(Math.round($('#addDetailgrid').getCell(rowid, "actPrice") * value * 100) / 100));
            } else if (cellname === "outMonyQty") {

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
            setAddFooterData();
        },

        gridComplete: function () {
            setAddFooterData();
        },
        loadComplete: function () {
            initAllCodesList();

        }
    });

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
function setAddFooterData() {
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
    $("#edit_actPrice").val(-Math.abs(sum_totActPrice));

}
function setFooterData() {
    var sum_totQty = $("#grid").getCol('totQty', false, 'sum');
    var sum_totOutQty = $("#grid").getCol('totOutQty', false, 'sum');
    var sum_totOutVal = $("#grid").getCol('totOutVal', false, 'sum');
    var sum_totInQty = $("#grid").getCol('totInQty',false,'sum');
    var sum_totInVal = $("#grid").getCol('totInVal', false, 'sum');
    var sum_payPrice = $("#grid").getCol('payPrice', false, 'sum');
    $("#grid").footerData('set', {
        billDate: "合计",
        totQty: sum_totQty,
        totOutQty: sum_totOutQty,
        totOutVal: sum_totOutVal,
        totInQty: sum_totInQty,
        totInVal: sum_totInVal,
        payPrice: sum_payPrice
    });
}
function _search() {

    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        page: 1,
        // url:searchUrl,
        postData: params
    });
    $("#grid").trigger("reloadGrid");
}
function _resetForm(){
    $("#searchForm").clearForm();
    $("#search_destId").val();
    $("#select_inStatus").val();
    $(".selectpicker").selectpicker('refresh');
}
/**
 *
 */
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
            $("#edit_busnissId").append("<option value='' >--请选择销售员--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#edit_busnissId").append("<option value='" + json[i].id + "'>" + json[i].name + "</option>");
                // $("#search_busnissId").trigger('chosen:updated');
            }
            if (defaultSaleStaffId != "" && defaultSaleStaffId != undefined) {
                $("#edit_busnissId").val(defaultSaleStaffId);
            }
        }
    });
}
/**
 * 加载客户类型
 */
function initCustomerTypeForm() {
    $.ajax({
        url: basePath + "/sys/property/searchByType.do?type=CT",
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#edit_customerType").empty();
            $("#edit_customerType").append("<option value='' style='background-color: #eeeeee'>--请选择客户类型--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#edit_customerType").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                $("#edit_customerType").trigger('chosen:updated');
            }
            if (pageType === "add") {
                if (defaultSaleStaffId != "" && defaultSaleStaffId != undefined) {
                    $("#edit_customerType").val("CT-LS");
                }

            }
        }
    });
}
function initButtonGroup(pageType) {
    var html = "";
    $("#edit_guest_button").removeAttr("disabled");
    html +=
        "<button id='CMDtl_add' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='addNew()'>" +
        "    <i class='ace-icon fa fa-plus'></i>" +
        "    <span class='bigger-110'>新增</span>" +
        "</button>" +
        "<button id='CMDtl_addUniqCode' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='addUniqCode()'>" +
        "    <i class='ace-icon fa fa-undo'></i>" +
        "    <span class='bigger-110'>扫码</span>" +
        "</button>" +
        "<button id='cm_scan_codes' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='addAllUniqCode()'>" +
        "    <i class='ace-icon fa fa-undo'></i>" +
        "    <span class='bigger-110'>批量扫码</span>" +
        "</button>" +
        "<button id='CMDtl_save' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='save()'>" +
        "    <i class='ace-icon fa fa-search'></i>" +
        "    <span class='bigger-110'>保存</span>" +
        "</button>"+
        "<button id='CMDtl_wareHouseIn' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='confirmWareHouseIn()'>" +
        "    <i class='ace-icon fa fa-search'></i>" +
        "    <span class='bigger-110'>入库</span>" +
        "</button>" +
        "<button id='cm_scancodes_in' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='batchWareHouseIn()'>" +
        "    <i class='ace-icon fa fa-undo'></i>" +
        "    <span class='bigger-110'>批量入库</span>" +
        "</button>" +

        "<button id='CMDtl_cancel' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='cancel()'>" +
        "    <i class='ace-icon fa fa-search'></i>" +
        "    <span class='bigger-110'>撤销</span>" +
        "</button>"+
        "<button id='CMDtl_wareHouseSale' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='saleRetrunNo()'>" +
        "    <i class='ace-icon fa fa-search'></i>" +
        "    <span class='bigger-110'>扫描退货</span>" +
        "</button>" +
        "<button id='CMDtl_wareHouseokSale' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='saleRetrunNook()'>" +
        "    <i class='ace-icon fa fa-search'></i>" +
        "    <span class='bigger-110'>退货</span>" +
        "</button>" +
        "<button id='CMDtl_findRetrunno' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='findRetrunno()'>" +
        "    <i class='ace-icon fa fa-search'></i>" +
        "    <span class='bigger-110'>查找退单</span>" +
        "</button>";

    $("#buttonGroup").html("" + html);
}
/**
 * 新增单据调用
 *
 * */
function addNew(){
    $('#addDetailgrid').jqGrid("clearGridData");
    $('#addDetailgrid').jqGrid('GridUnload');
    pageType="add";
    initAddGrid();
    $("#editForm").clearForm();
    initForm();
    initCustomerTypeForm();
    $("#addDetailgrid").trigger("reloadGrid");
    $(".selectpicker").selectpicker('refresh');

    initButtonGroup(pageType);
}
var dialogOpenPage;
var prefixId;
/*
 *@param preId id前缀 search/edit 区分回调框id
 **/
function openSearchGuestDialog(preId) {
    dialogOpenPage = "transferOrderconsignmentBill";
    prefixId =preId;
    $("#modal_guest_search_table").modal('show').on('shown.bs.modal', function () {
        initGuestSelect_Grid();
    });
    console.log(prefixId);
    $("#searchGuestDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='confirm_selected_GuestId_Consignment()'>确认</button>"
    );
}

function updateBillDetailData(){
    var ct = $("#edit_customerType").val();
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var dtlRow = $("#addDetailgrid").getRowData(value);
        var map = dtlRow.stylePriceMap;
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
                name: "operation", label: '操作', width: 30, align: 'center', sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.inStatus != 4 || rowObject.inStatus != 1) {
                        return "<a href='javascript:void(0);' onclick=saveItem('" + options.rowId + ")'><i class='ace-icon ace-icon fa fa-save' title='保存'></i></a>"
                            + "<a href='javascript:void(0);' style='margin-left: 20px' onclick=deleteItem('" + options.rowId + "')><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";
                    } else {
                        return "";
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
            {name: 'styleId', label: '款号', width: 20,hidden: true},
            {name: 'colorId', label: '色码', width: 20,hidden: true},
            {name: 'sizeId', label: '尺码', width: 20,hidden: true},
            {name: 'styleName', label: '款名', width: 20},
            {name: 'colorName', label: '颜色', width: 20,hidden: true},
            {name: 'sizeName', label: '尺码', width: 20,hidden: true},
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
            {name: 'uniqueCodes', label: '唯一码',hidden:true},
            {
                name: '', label: '唯一码明细', width: 40, align: "center",
                formatter: function (cellValue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=showCodesDetail('" + rowObject.uniqueCodes + "')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
                }
            },
            {name: 'beforeoutQty', hidden: true},
            {name: 'outMonyQty', hidden: true},
            {name: 'savehaveuniqueCodes', label: '唯一码', hidden: true},
            {
                name: '', label: '异常唯一码明细', width: 75, align: "center",
                formatter: function (cellValue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=showCodesDetail('" + rowObject.noOutPutCode + "')><i class='ace-icon ace-icon fa fa-list' title='显示异常唯一码明细'></i></a>";
                }
            },
            {name: 'noOutPutCode', label: '异常唯一码',hidden: true},
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
            setAddFooterData();
        },

        gridComplete: function () {
            setAddFooterData();
        },
        loadComplete: function () {
            initAllCodesList();

        }
    });
    $("#addDetailgrid-pager_center").html("");
}
//扫码
function addUniqCode() {
    inOntWareHouseValid = 'addPage_scanUniqueCode';
    taskType = -1; //出库
    var destId = $("#edit_destId").val();
    wareHouse = destId;
    billNo = $("#edit_billNo").val();
    var ct = $("#edit_customerType").val();
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
    if (!$('#so_savecode_button').prop('disabled')) {
        $("#so_savecode_button").attr({"disabled": "disabled"});
        var productListInfo = [];
        var alltotActPrice = 0;
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
                /*productInfo.actPrice = Math.round(productInfo.price * productInfo.discount) / 100;*/
                if(Math.round(productInfo.price * productInfo.discount) / 100<productInfo.wsPrice&&isUserAbnormal){
                    productInfo.actPrice = productInfo.wsPrice;
                    productInfo.discount = parseFloat(productInfo.wsPrice/productInfo.price).toFixed(2)*100;
                    productInfo.abnormalStatus=1;
                }else{
                    productInfo.actPrice = Math.round(productInfo.price * productInfo.discount) / 100;
                    productInfo.abnormalStatus=0;
                }
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
        setAddFooterData();
        saveother(0 - alltotActPrice);
    }
}
function saveother(totActPrice) {
    cs.showProgressBar();
    $("#addDetailgrid").saveCell(editDtailiRow, editDtailiCol);
    $("#edit_customerType").removeAttr('disabled');
    $("#edit_origId").removeAttr('disabled');
    $("#edit_destId").removeAttr('disabled');
    $("#edit_busnissId").removeAttr('disabled');

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
    if ($("#edit_origId").val() === $("#edit_destId").val()) {
        bootbox.alert("相同店不能寄售");
        cs.closeProgressBar();
        return;
    }
    if(pageType=="edit"){
        if(comsigment_customerType!=$("#edit_customerType").val()){
            bootbox.alert("客户类型不相同");
            cs.closeProgressBar();
            return;
        }
    }
    //实收金额的计算
    var payPrice = $("#edit_payPrice").val();
    if (parseFloat(payPrice) < 0) {
        var summun = parseFloat(payPrice) - parseFloat(totActPrice);
        if (summun < 0) {
            $("#edit_payPrice").val(summun.toFixed(2));
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
                $("#edit_billNo").val(msg.result);
                billNo = msg.result;
                issaleretrun = true;
                _search();
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
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
function showCodesDetail(uniqueCodes) {

    $("#show-uniqueCode-list").modal('show');
    initUniqueCodeList(uniqueCodes);
    codeListReload(uniqueCodes);
}
function save() {
    cs.showProgressBar();
    $("#addDetailgrid").saveCell(editDtailiRow, editDtailiCol);
    $("#edit_customerType").removeAttr('disabled');
    $("#edit_origId").removeAttr('disabled');
    $("#edit_destId").removeAttr('disabled');
    $("#edit_busnissId").removeAttr('disabled');

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
    if(pageType=="edit"){
        if(comsigment_customerType!=$("#edit_customerType").val()){
            bootbox.alert("客户类型不相同");
            cs.closeProgressBar();
            return;
        }
    }
    $("#edit_billDate").val(updateTime($("#edit_billDate").val()));
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
                $("#edit_billNo").val(msg.result);
                billNo = msg.result;
                issaleretrun = true;
                _search();
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });

}
function confirmWareHouseIn() {
    cs.showProgressBar();
    $("#CMDtl_wareHouseIn").attr({"disabled": "disabled"});

    var billNo = $("#edit_billNo").val();

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
                bootbox.alert( "已入库" + inqty + "件");
                _search();
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });

    $("#add-uniqCode-dialog").modal('hide');
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

        var isok = 0;
        var inventory = $("#inventoryCodeGrid").getRowData(value);
        $.each($("#addDetailgrid").getDataIDs(), function (index, value) {


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
function saleRetrunNook() {
    cs.showProgressBar();
    isfrist=true;
    var isok = true;
    var billNo = $("#edit_billNo").val();
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
        $("#edit_customerType").removeAttr('disabled');
        $("#edit_origId").removeAttr('disabled');
        $("#edit_destId").removeAttr('disabled');
        $("#edit_busnissId").removeAttr('disabled');
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
                        initeditGrid($("#edit_billNo").val());

                        bootbox.alert(msg.msg);

                    } else {
                        bootbox.alert(msg.msg);
                    }
                }
            });
        }
    }

}
function findRetrunno() {
    $("#show-findRetrunNo-list").modal('show');
    $("#findRetrunNoListGrid").jqGrid("clearGridData");
    $('#findRetrunNoListGrid').jqGrid('GridUnload');
    initUniqueretrunList();
    $("#findRetrunNoListGrid").trigger("reloadGrid");
    retrunListReload();

}
function cancel() {
    var billNo=$("#edit_billNo").val();
    var row = $("#grid").jqGrid("getRowData", billNo);
    if (row.status != "0") {
        bootbox.alert("不是录入状态，不可取消!");
        return
    }
    bootbox.confirm({
        /*title: "余额确认",*/
        buttons: {confirm: {label: '确定'}, cancel: {label: '取消'}},
        message: "撤销确定",
        callback: function (result) {
            /* $("#SODtl_save").removeAttr("disabled");*/
            if (result) {
                cancelAjax(billNo);
            } else {
            }
        }
    });
}
function cancelAjax(billNo) {
    $.ajax({
        url: basePath + "/logistics/Consignment/cancel.do?billNo=" + billNo,
        type: "POST",
        success: function (result) {
            if (result.success) {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#grid").trigger("reloadGrid");
            } else {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
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
    var privilegeMap = ButtonAndDivPower(resourcePrivilege);
    $.each(privilegeMap['div'],function(index,value){
        if(value.isShow!=0) {
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
            disableButtonIds = ["CMDtl_save","CMDtl_cancel","CMDtl_wareHouseSale","CMDtl_wareHouseokSale"];
            break;
        case "0" :
            disableButtonIds = [];
            break;
        case "1":
            disableButtonIds = ["CMDtl_cancel","CMDtl_save,TRDtl_addUniqCode"];
            break;
        case "2" :
            disableButtonIds = ["CMDtl_save","CMDtl_cancel"];
            break;
        case "3":
            disableButtonIds = ["CMDtl_save","CMDtl_cancel"];
            break;
        default:
            disableButtonIds = ["CMDtl_wareHouseIn"];
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
    if (userId != "admin") {
        $("#edit_guest_button").attr({"disabled": "disabled"});
    }
}
function search_discount_onblur() {
    setDiscount();
}
//将整单折扣设置到明细中
function setDiscount() {

    if (editDtailiRow != null && editDtailiCol != null) {
        $("#addDetailgrid").saveCell(editDtailiRow, editDtailiCol);
        editDtailiRow = null;
        editDtailiCol = null;
    }
    var discount = $("#edit_discount").val();
    if (discount && discount != null && discount != "") {
        /*if(userId == 'admin') {
            $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
                $('#addDetailgrid').setCell(value, "discount", discount);
                var var_actPrice = Math.round(discount * $('#addDetailgrid').getCell(value, "price")) / 100;
                var var_totActPrice = -Math.abs(Math.round(var_actPrice * $('#addDetailgrid').getCell(value, "qty") * 100) / 100);
                $('#addDetailgrid').setCell(value, "actPrice", var_actPrice);
                $('#addDetailgrid').setCell(value, "totActPrice", var_totActPrice);
            });
        }
        else {*/
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
        /*}*/
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

function addAllUniqCode() {
    var destId = $("#edit_destId").val();
    taskType = 1;
    wareHouse = destId;
    if(wareHouse && wareHouse !== null) {

    }else {
        bootbox.alert("请选择入库仓库");
        return;
    }
    billNo = $("#edit_billNo").val();
    var ct = $("#edit_customerType").val();
    if (ct && ct != null) {
        $("#modal-batch-table").modal('show').on('hidden.bs.modal', function () {
            $("#batchDetailgrid").clearGridData();
            skuInfo=[];
            oldSkuInfo = [];
        });
        initWebSocket();
    } else {
        bootbox.alert("客户类型不能为空！");
    }
}


/**
 * 批量扫码入库
 */
function batchWareHouseIn(){
    taskType = 1;
    var destId = $("#edit_destId").val();
    wareHouse = destId;
    billNo = $("#edit_billNo").val();
    var ct = $("#edit_customerType").val();
    if (destId && destId != null) {
        $("#modal-batch-show-In-table").modal('show').on('hidden.bs.modal', function () {
            $("#billInformationIngrid").clearGridData();
            $("#notThisOneIngrid").clearGridData();
            skuInfo=[];
            oldSkuInfo=[];
        });
        lodeBillInformationIngrid();
        fullWebInSocket();
        initUniqeCodeGridColumn(ct);
        $("#inCodeQty").text(0);
    } else {
        bootbox.alert("入库仓库不能为空！");
    }
}

//添加批量扫码页面到单据明细中
function saveEPC(){
    var uniqueCodeList=[];//商品唯一码list(含商品信息)
    var rightCodesArry=[];//正确唯一码（只有唯一码)
    var errorCodesArry=[];//异常唯一码(只有唯一码信息)
    //遍历批量扫到的唯一码将sku明细转换为唯一码明细
    $.each($("#batchDetailgrid").getDataIDs(),function (index,value) {
        var rowData=$("#batchDetailgrid").jqGrid('getRowData',value);
        var rightCodes;//正常唯一码
        var errorCodes;//异常唯一码
        if(rowData.uniqueCodes!=""&&rowData.uniqueCodes!=undefined){
            rightCodes=rowData.uniqueCodes.split(",");
            rightCodesArry = rightCodesArry.concat(rightCodes);
        }
        if(rowData.noOutPutCode!=""&&rowData.noOutPutCode!=undefined){
            errorCodes=rowData.noOutPutCode.split(",");
            errorCodesArry = errorCodesArry.concat(errorCodes);
        }
        if (rightCodes!=undefined){
            delete rowData.noOutPutCode;
            $.each(rightCodes,function (cIndex,cValue) {
                var newRowData=JSON.parse(JSON.stringify(rowData));
                newRowData.uniqueCodes=cValue;
                newRowData.qty = 1;
                //判断实际价格是不是小于门店批发价格
                if($('#discount_div').is(':hidden')) {
                    newRowData.actPrice = Math.round(newRowData.price * newRowData.discount) / 100;
                    newRowData.abnormalStatus=0;
                }else {
                    if(Math.round(newRowData.price * newRowData.discount) / 100<newRowData.wsPrice&&isUserAbnormal){
                        newRowData.actPrice = newRowData.wsPrice;
                        newRowData.discount = parseFloat(newRowData.wsPrice/newRowData.price).toFixed(2)*100;
                        newRowData.abnormalStatus=1;
                    }else{
                        newRowData.actPrice = Math.round(newRowData.price * newRowData.discount) / 100;

                        newRowData.abnormalStatus=0;
                    }
                }
                newRowData.outQty = 0;
                newRowData.inQty = 0;
                newRowData.status = 0;
                newRowData.inStatus = 0;
                newRowData.outStatus = 0;
                newRowData.totPrice = newRowData.price;
                newRowData.totActPrice = newRowData.actPrice;
                var stylePriceMap={};
                stylePriceMap['price']=newRowData.price;
                stylePriceMap['wsPrice']=newRowData.wsPrice;
                stylePriceMap['puPrice']=newRowData.puPrice;
                newRowData.stylePriceMap=JSON.stringify(stylePriceMap);
                uniqueCodeList.push(newRowData);
            });
        }
    });
    //适用于寄存调拨只能添加正常唯一码情况
    if(rightCodesArry.length === 0){
        bootbox.alert("没有正常的唯一码,请重新扫码");
    }else{
        var isAdd = true;
        var alltotActPrice = 0;
        //将唯一码明细添加入明细表格
        $.each(uniqueCodeList,function (index,value) {
            isAdd=true;
            $.each($("#addDetailgrid").getDataIDs(),function (dtlIndex,dtlValue) {
                var dtlRow = $("#addDetailgrid").getRowData(dtlValue);
                if (value.sku === dtlRow.sku) {
                    if (value.uniqueCodes!=""&&value.uniqueCodes!=undefined&&dtlRow.uniqueCodes.indexOf(value.uniqueCodes)=== -1) {
                        dtlRow.qty = parseInt(dtlRow.qty) + 1;
                        dtlRow.totPrice = dtlRow.qty * dtlRow.price;
                        dtlRow.totActPrice = dtlRow.qty * dtlRow.actPrice;
                        alltotActPrice += dtlRow.qty * dtlRow.actPrice;
                        dtlRow.uniqueCodes = dtlRow.uniqueCodes + "," + value.uniqueCodes;
                    }
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
        setFooterData();
        bootbox.alert("添加正常数据"+rightCodesArry.length+"条,异常数据"+errorCodesArry.length+"条无法添加");
    }
    $("#modal-batch-table").modal('hide');
    setAddFooterData();
    saveother(0 - alltotActPrice);
}