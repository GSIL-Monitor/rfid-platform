var addDetailgridiRow;//存储iRow
var addDetailgridiCol;//存储iCol
var allCodes; //用于拼接所有添加过的唯一码，防止重复添加
var taskType; //用于判断出入库类型 1入库 0 出库
var wareHouse;
var inOntWareHouseValid; //用于判断在编辑BillDtl时出入库操作是否需要校验，使用哪种校验。
var skuQty = {};//保存每个SKU对应的出入库数量。
var allCodeStrInDtl = "";  //入库时，所有明细中的唯一码
var billNo;
$(function () {


    if (pageType === "edit") {
        initeditGrid();
    } else {
        initGrid();
        //initSelectDestForm();
        $("#search_destUnitId").val(defalutCustomerId);
        $("#search_destUnitName").val(defalutCustomerName);


        $("#search_discount").val(defalutCustomerdiscount);
        $("#pre_Balance").val(0 - defalutCustomerowingValue);


    }
    initForm();
    keydown();
    addProduct_keydown();
    input_keydown();
    initButtonGroup();
    initEditFormValid();
    $(".selectpicker").selectpicker({'noneResultsText': ''});
    /*$(".glyphicon glyphicon-remove").on("click", function (e) {
        var saleBillNo =  $("#search_billNo").val();
        if(saleBillNo != ""){
            window.location.href= basePath + "/logistics/saleOrder/quit.do?billNo=" + billNo;
        }
    });*/
    if (billNo != "") {
        sessionStorage.setItem("billNosale", billNo);
    }
    if (pageType === "edit") {
    } else {
        if (defalutCustomerId != "" && defalutCustomerId != undefined) {
            addUniqCode();
        }

    }

});


function initForm() {
    initCustomerTypeForm();
    initSelectDestForm();
    initSelectOrigForm();
    initSelectBusinessIdForm();
    $("#search_customerType").attr('disabled', true);

    if (pageType === "add") {
        $("#search_billDate").val(getToDay("yyyy-MM-dd"));
        $("#search_payPrice").val(0);
        $("#search_origId").val(defaultWarehId);
    } else if (pageType === "edit") {
        $("#search_customerType").val(saleOrder_customerTypeId);
        $("#search_origId").val(saleOrder_origId);
        $("#search_destId").val(saleOrder_destId);
        $("#search_busnissId").val(saleOrder_busnissId);
        if (slaeOrder_status != "0" && userId != "admin") {
            $("#search_origId").attr('disabled', true);
            $("#search_destId").attr('disabled', true);
            $("#search_billDate").attr('readOnly', true);
            $("#search_busnissId").attr('disabled', true);
        }
        if (userId == "admin") {
            $("#search_guest_button").removeAttr("disabled");
            $("#search_origId").attr('disabled', true);
            $("#search_destId").attr('disabled', true);
        }
    } else if (pageType === "copyAdd") {
        $("#search_billNo").val("");
        $("#search_origId").val(saleOrder_origId);
        $("#search_destId").val(saleOrder_destId);
        $("#search_busnissId").val(saleOrder_busnissId);
        $("#search_billDate").val(getToDay("yyyy-MM-dd"));
    }

}

function closeTabs() {
    var saleBillNo = $("#search_billNo").val();
    if (saleBillNo != "" || saleBillNo != undefined) {
        quitback();
        window.location.href = basePath + "/logistics/saleOrder/quit.do?billNo=" + billNo;
    }
//关闭TAB
    $("#tab_" + id).remove();
    $("#" + id).remove();
}

function initCustomerTypeForm() {
    $.ajax({
        url: basePath + "/sys/property/searchByType.do?type=CT",
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {

            $("#search_customerType").empty();
            $("#search_customerType").append("<option value='' style='background-color: #eeeeee'>--请选择客户类型--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_customerType").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                $("#search_customerType").trigger('chosen:updated');
            }
            if (pageType === "edit") {

            } else {
                if (defalutCustomerId != "" && defalutCustomerId != undefined) {
                    $("#search_customerType").val("CT-LS");
                }

            }

        }
    });
}

function initSelectOrigForm() {
    if (userId == "admin") {
        $.ajax({
            url: basePath + "/unit/list.do?filter_EQI_type=9",
            cache: false,
            async: false,
            type: "POST",
            success: function (data, textStatus) {
                $("#search_origId").empty();
                $("#search_origId").append("<option value='' style='background-color: #eeeeee'>--请选择出库仓库--</option>");
                var json = data;
                for (var i = 0; i < json.length; i++) {
                    $("#search_origId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                    $("#search_origId").trigger('chosen:updated');
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
                $("#search_origId").empty();
                $("#search_origId").append("<option value='' style='background-color: #eeeeee'>--请选择出库仓库--</option>");
                var json = data;
                for (var i = 0; i < json.length; i++) {
                    $("#search_origId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                    $("#search_origId").trigger('chosen:updated');
                }
            }
        });
    }

}

function initSelectDestForm() {

    if (userId == "admin") {
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
    } else {
        $.ajax({
            url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + $("#search_destUnitId").val(),
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

            if (defaultSaleStaffId != "" && defaultSaleStaffId != undefined) {
                $("#search_busnissId").val(defaultSaleStaffId);
            }


        }
    });

}


function initButtonGroup() {
    $("#addDetailgrid").clearGridData();

    if (pageType === "add") {
        $("#buttonGroup").html("" +
            "<button id='SODtl_save' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='save()'>" +
            "    <i class='ace-icon fa fa-save'></i>" +
            "    <span class='bigger-110'>保存</span>" +
            "</button>" +
         /*   "<button id='SODtl_saveAndAdd' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='saveAndAdd()'>" +
            "    <i class='ace-icon fa fa-save'></i>" +
            "    <span class='bigger-110'>保存并新增</span>" +
            "</button>" +*/
            "<button id='SODtl_addUniqCode' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='addUniqCode()'>" +
            "    <i class='ace-icon fa fa-barcode'></i>" +
            "    <span class='bigger-110'>扫码</span>" +
            "</button>" +
            "<button id='SODtl_wareHouseOut' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='wareHouseOut()'>" +
            "    <i class='ace-icon fa fa-sign-out'></i>" +
            "    <span class='bigger-110'>出售</span>" +
            "</button>" +
            "<button id='SODtl_adddoPrint1' type='button' style='margin-left: 20px;display: none;' class='btn btn-sm btn-primary' onclick='doPrint()'>" +
            "    <i class='ace-icon fa fa-print'></i>" +
            "    <span class='bigger-110'>打印</span>" +
            "</button>"
        );

        $("#search_guest_button").removeAttr("disabled");
    }
    if (pageType === "edit") {
        $("#buttonGroup").html("" +
            "<button id='SODtl_save' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='save()'>" +
            "    <i class='ace-icon fa fa-save'></i>" +
            "    <span class='bigger-110'>保存</span>" +
            "</button>" +
            "<button id='SODtl_addUniqCode' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='addUniqCode()'>" +
            "    <i class='ace-icon fa fa-barcode'></i>" +
            "    <span class='bigger-110'>扫码</span>" +
            "</button>" +
            "<button id='SODtl_wareHouseOut' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='wareHouseOut()'>" +
            "    <i class='ace-icon fa fa-sign-out'></i>" +
            "    <span class='bigger-110'>出库</span>" +
            "</button>" +
            "<button id='SODtl_wareHouseIn' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='wareHouseIn()'>" +
            "    <i class='ace-icon fa fa-sign-in'></i>" +
            "    <span class='bigger-110'>入库</span>" +
            "</button>" +
            "<button id='SODtl_wareHouseIn' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='Returngoods()'>" +
            "    <i class='ace-icon fa fa-reply'></i>" +
            "    <span class='bigger-110'>退货</span>" +
            "</button>" +
            "<button id='SODtl_wareHouseIn' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='exchangeGoods()'>" +
            "    <i class='ace-icon fa fa-exchange'></i>" +
            "    <span class='bigger-110'>换货</span>" +
            "</button>" +
            "<button id='SODtl_doPrint' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='doPrint()'>" +
            "    <i class='ace-icon fa fa-print'></i>" +
            "    <span class='bigger-110'>打印</span>" +
            "</button>" +
            "<button id='SODtl_doPrintA4' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='doPrintA4()'>" +
            "    <i class='ace-icon fa fa-print'></i>" +
            "    <span class='bigger-110'>A4打印Boss</span>" +
            "</button>" +
            "<button id='SODtl_doPrintA41' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='doPrintA41()'>" +
            "    <i class='ace-icon fa fa-print'></i>" +
            "    <span class='bigger-110'>A4打印</span>" +
            "</button>" +
            "<button id='CMDtl_findRetrunno' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='findRetrunno()'>" +
            "    <i class='ace-icon fa fa-search'></i>" +
            "    <span class='bigger-110'>查找退单</span>" +
            "</button>" +
            "<button id='CMDtl_findshopMessage' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='findshopMessage()'>" +
            "    <i class='ace-icon fa fa-search'></i>" +
            "    <span class='bigger-110'>查找商城信息</span>" +
            "</button>" +
            "<button id='CMDtl_StreamNO' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='sendStreamNO()'>" +
            "    <i class='ace-icon fa fa-search'></i>" +
            "    <span class='bigger-110'>推送物流号</span>" +
            "</button>"
        );
        /* $("#buttonGroupfindWxshop").html("" +
             "<button id='CMDtl_findshopMessage' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='findshopMessage()'>" +
             "    <i class='ace-icon fa fa-search'></i>" +
             "    <span class='bigger-110'>查找商城信息</span>" +
             "</button>"
         );*/
        //订单状态：0，表示录入态
        if (slaeOrder_status != "0" && userId != "admin") {
            $("#search_guest_button").attr({"disabled": "disabled"});
            $("#SODtl_addUniqCode").attr({"disabled": "disabled"});
            $("#SODtl_save").attr({"disabled": "disabled"});
        }
        //如果入库仓库为空，禁止入库按钮
        if ($("#search_destId").val() && $("#search_destId").val() != null) {
            $("#SODtl_wareHouseIn").removeAttr("disabled");
        } else {
            $("#SODtl_wareHouseIn").attr({"disabled": "disabled"})
        }
        //判断是否是admin
        if (roleid != "0" && roleid != "SHOPUSER") {
            $("#SODtl_doPrintA4").hide();
            $("#SODtl_doPrintA41").hide();
        }
    }
    $("#addDetail").show();
}

function initGrid() {
    

    $("#addDetailgrid").jqGrid({
        height: 'auto',
        datatype: "local",
        //url: basePath + "/logistics/saleOrder/findBillDtl.do?billNo=" + billNo,
        mtype: 'POST',
        colModel: [
            {name: 'id', label: 'id', hidden: true},
            {name: 'billId', label: 'billId', hidden: true},
            {name: 'billNo', label: 'billNo', hidden: true},
            {name: 'status', hidden: true},
            {name: 'inStatus', hidden: true},
            {name: 'outStatus', hidden: true},
            {
                name: "operation", label: "操作", width: 30, align: "center", sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=saveItem('" + options.rowId + "')><i class='ace-icon ace-icon fa fa-save' title='保存'></i></a>"
                        + "<a style='margin-left: 20px' href='javascript:void(0);' onclick=deleteItem('" + options.rowId + "')><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";
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
            {name: 'styleId', label: '款号', width: 40},
            {name: 'styleName', label: '款名', width: 40},
            {name: 'colorId', label: '色码', width: 40},
            {name: 'colorName', label: '颜色', width: 30},
            {name: 'sizeId', label: '尺码', width: 30},
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
            {name: 'outQty', label: '已出库数量', width: 40},
            {name: 'inQty', label: '已入库数量', width: 40},
            {name: 'sku', label: 'SKU', width: 50},
            {
                name: 'price', label: '销售价格', width: 40,
                editrules: {
                    number: true
                }
            },
            {name: 'totPrice', label: '销售金额', width: 40},
            {
                name: 'discount', label: "折扣", width: 40, editable: true,
                editrules: {
                    number: true,
                    minValue: 0,
                    maxValue: 100
                }
            },
            {
                name: 'actPrice', label: '实际价格', editable: true, width: 40,
                editrules: {
                    number: true,
                    minValue: 0
                }
            },
            {name: 'totActPrice', label: '实际金额', width: 40},
            {name: 'uniqueCodes', label: '唯一码', hidden: true}
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
            initAllCodesList();
        }
    });
    console.log("1234");
    $("#addDetailgrid").setGridParam().showCol("operation");
    console.log("12345");
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
    console.log("123456");
    $("#addDetailgrid-pager_center").html("");
    console.log("1234567");
}

//var isretrun=false;
function initeditGrid() {
    $("#addDetailgrid").jqGrid({
        height: 'auto',
        datatype: "json",
        url: basePath + "/logistics/saleOrder/findBillDtl.do?billNo=" + billNo,
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
                        + "<a style='margin-left: 20px' href='javascript:void(0);' onclick=deleteItem('" + options.rowId + "')><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";
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
            {name: 'styleId', label: '款号', width: 40},
            {name: 'styleName', label: '款名', width: 40},
            {name: 'colorId', label: '色码', width: 40},
            {name: 'colorName', label: '颜色', width: 30},
            {name: 'sizeId', label: '尺码', width: 30},
            {name: 'sizeName', label: '尺码', width: 40},
            {
                name: 'qty', label: '数量', editable: false, width: 40,
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
            {name: 'returnQty', label: '退货数量', editable: true, width: 40},
            {name: 'outQty', label: '已出库数量', width: 40},
            {name: 'inQty', label: '已入库数量', width: 40},
            {name: 'sku', label: 'SKU', width: 50},
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

            {
                name: 'totPrice', label: '销售金额', width: 40,
                formatter: function (cellValue, options, rowObject) {
                    var totPrice = parseFloat(cellValue).toFixed(2);
                    return totPrice;
                }
            },
            {
                name: 'discount', label: "折扣", width: 40, editable: true
            },
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
            {name: 'returnbillNo', label: '退货单号', hidden: true}

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
                $('#addDetailgrid').setCell(rowid, "totPrice", Math.round($('#addDetailgrid').getCell(rowid, "price") * value) * 100) / 100;
                $('#addDetailgrid').setCell(rowid, "totActPrice", Math.round($('#addDetailgrid').getCell(rowid, "actPrice") * value) * 100) / 100;
            }
            setFooterData();
        },
        afterEditCell: function (rowid, celname, value, iRow, iCol) {
            var value = $('#addDetailgrid').getRowData(rowid);
            if ((value.outStatus != 2 && celname == "returnQty" && value.returnbillNo == "") ||
                (value.outStatus != 2 && celname == "discount" && value.returnbillNo == "") ||
                (value.outStatus != 2 && celname == "actPrice" && value.returnbillNo == "")) {
                addDetailgridiRow = iRow;
                addDetailgridiCol = iCol;
            } else {
                $('#addDetailgrid').restoreCell(iRow, iCol);
            }
        },

        gridComplete: function () {
            setFooterData();
        },
        loadComplete: function () {
            initAllCodesList();

        }
    });
    if (slaeOrder_status != "0") {
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
    $("#addDetailgrid-pager_center").html("");
}

function setFooterData() {
    var sum_qty = $("#addDetailgrid").getCol('qty', false, 'sum');
    var sum_outQty = $("#addDetailgrid").getCol('outQty', false, 'sum');
    var sum_inQty = $("#addDetailgrid").getCol('inQty', false, 'sum');
    var sum_returnQty = $("#addDetailgrid").getCol('returnQty', false, 'sum');
    var sum_totPrice = $("#addDetailgrid").getCol('totPrice', false, 'sum');
    var sum_totActPrice = Math.round($("#addDetailgrid").getCol('totActPrice', false, 'sum'));
    $("#search_actPrice").val(sum_totActPrice);
    $("#addDetailgrid").footerData('set', {
        styleId: "合计",
        qty: sum_qty,
        outQty: sum_outQty,
        inQty: sum_inQty,
        returnQty: sum_returnQty,
        totPrice: sum_totPrice,
        totActPrice: sum_totActPrice
    });
}

function addDetail() {

    var ct = $("#search_customerType").val();
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

function deleteItem(rowId) {
    var value = $('#addDetailgrid').getRowData(rowId);
    $("#addDetailgrid").jqGrid("delRowData", rowId);
    setFooterData();

    var totActPrice = value.totActPrice;

    saveother(totActPrice);
}

function saveother(totActPrice) {
     cs.showProgressBar();
    $("#search_customerType").removeAttr('disabled');
    $("#search_origId").removeAttr('disabled');
    $("#search_destId").removeAttr('disabled');
    $("#search_busnissId").removeAttr('disabled');

    if ($("#search_origId").val() == $("#search_destId").val()) {
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
    /* if ($("#addDetailgrid").getDataIDs().length == 0) {
         bootbox.alert("请添加销售商品！");
         return;
     }*/
    if (addDetailgridiRow != null && addDetailgridiCol != null) {
        $("#addDetailgrid").saveCell(addDetailgridiRow, addDetailgridiCol);
        addDetailgridiRow = null;
        addDetailgridiCol = null;
    }
    //实收金额的计算
    var payPrice = $("#search_payPrice").val();
    if (parseFloat(payPrice) > 0) {
        var summun = parseFloat(payPrice) - parseFloat(totActPrice);
        if (summun > 0) {
            $("#search_payPrice").val(summun.toFixed(2));
        }
    }
    saveAjax();


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
    var ct = $("#search_customerType").val();
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
            if ($("#search_discount").val() && $("#search_discount").val() !== null) {
                productInfo.discount = $("#search_discount").val();
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
    setFooterData();

}


function save() {
    cs.showProgressBar();
    $("#search_customerType").removeAttr('disabled');
    $("#search_origId").removeAttr('disabled');
    $("#search_destId").removeAttr('disabled');
    $("#search_busnissId").removeAttr('disabled');

    if ($("#search_origId").val() == $("#search_destId").val()) {
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
        return false;
    }
    if ($("#addDetailgrid").getDataIDs().length == 0) {
        bootbox.alert("请添加销售商品！");
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
}

function add() {
    location.href = basePath + "/logistics/saleOrder/add.do";
}

window.flag = false;

function saveAndAdd() {
    save();
    if (flag) {
        alert("开单成功")
        add();
        // gritter("开单成功");
    }
}


function saveAjax() {
    var dtlArray = [];
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        dtlArray.push(rowData);
    });
    $("#SODtl_wareHouseIn").attr({"disabled": "disabled"});
    $.ajax({
        dataType: "json",
        async: false,
        url: basePath + "/logistics/saleOrder/save.do",
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
                $("#search_billNo").val(msg.result);

                $("#addDetailgrid").jqGrid('setGridParam', {
                    datatype: "json",
                    page: 1,
                    url: basePath + "/logistics/saleOrder/findBillDtl.do?billNo=" + msg.result,
                });
                $("#addDetailgrid").trigger("reloadGrid");
                $("#SODtl_adddoPrint1").show();
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
    window.isTrue = true;
}

function addUniqCode() {
    inOntWareHouseValid = 'addPage_scanUniqueCode';
    var origId = $("#search_origId").val();
    taskType = 0;
    wareHouse = origId;
    billNo = $("#search_billNo").val();
    var ct = $("#search_customerType").val();
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

function addProductsOnCode() {
    var productListInfo = [];
    if (!$('#so_savecode_button').prop('disabled')) {
        $("#so_savecode_button").attr({"disabled": "disabled"});
        var ct = $("#search_customerType").val();
        $.each($("#uniqueCodeGrid").getDataIDs(), function (index, value) {
            var productInfo = $("#uniqueCodeGrid").getRowData(value);
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

function wareHouseOut() {
     cs.showProgressBar();
    $("#SODtl_wareHouseOut").attr({"disabled": "disabled"});
    var billNo = $("#search_billNo").val();
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
        wareHouse = $("#search_origId").val();
        $.ajax({
            async: false,
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
            }
        });

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
            if (pageType === "edit") {
                edit_wareHouseOut();
            }
            return;
        }
        var dtlArray = [];
        $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
            var rowData = $("#addDetailgrid").getRowData(value);
            dtlArray.push(rowData);
        });
        $.ajax({
            async: false,
            dataType: "json",
            url: basePath + "/logistics/saleOrder/convertOut.do",
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
                        url: basePath + "/logistics/saleOrder/findBillDtl.do?billNo=" + billNo
                    });
                    $("#addDetailgrid").trigger("reloadGrid");

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
                                quitback();

                            }
                        });
                    }
                } else {
                    bootbox.alert(msg.msg);
                }
            }
        });
    } else {
        cs.closeProgressBar()
        bootbox.alert("请先保存当前单据");
    }
}

function quitback() {
    $.ajax({
        url: basePath + "/logistics/saleOrder/quit.do?billNo=" + billNo,
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {

            if (textStatus == "success") {
                $.gritter.add({
                    text: billNo + "可以编辑",
                    class_name: 'gritter-success  gritter-light'
                });
                window.location.href = basePath + "/logistics/saleOrder/index.do";
            }

        }
    });
}

function edit_wareHouseOut() {
    skuQty = {};
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        skuQty[rowData.sku] = rowData.outQty;
    });
    inOntWareHouseValid = 'wareHouseOut_valid';
    billNo = $("#search_billNo").val();
    var ct = $("#search_customerType").val();

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


function confirmWareHouseOut() {
     cs.showProgressBar();
    $("#so_comfirmout_button").attr({"disabled": "disabled"});
    var billNo = $("#search_billNo").val();
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
        url: basePath + "/logistics/saleOrder/convertOut.do",
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
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
    $("#add-uniqCode-dialog").modal('hide');
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


function wareHouseIn() {
    skuQty = {};
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        skuQty[rowData.sku] = rowData.inQty;
    });
    inOntWareHouseValid = 'wareHouseIn_valid';
    taskType = 1;
    var destId = $("#search_destId").val();
    wareHouse = destId;
    billNo = $("#search_billNo").val();
    var ct = $("#search_customerType").val();
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

function confirmWareHouseIn() {
     cs.showProgressBar();
    $("#In_dialog_buttonGroup").attr({"disabled": "disabled"});
    var billNo = $("#search_billNo").val();
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
        async: false,
        url: basePath + "/logistics/saleOrder/convertIn.do",
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
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
    $("#add-uniqCode-dialog").modal('hide');

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

var dialogOpenPage;

function openSearchGuestDialog() {
    dialogOpenPage = "saleOrder";
    $("#modal_guest_search_table").modal('show').on('shown.bs.modal', function () {
        initGuestSelect_Grid();
    });
    $("#searchGuestDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='confirm_selected_GuestId_sale()'>确认</button>"
    );
}

function input_keydown() {
    $("#search_discount").keydown(function (event) {
        if (event.keyCode == 13) {
            setDiscount();
        }
    })
}

function search_discount_onblur() {
    setDiscount();
}

//将整单折扣设置到明细中
function setDiscount() {
    if (addDetailgridiRow != null && addDetailgridiCol != null) {
        $("#addDetailgrid").saveCell(addDetailgridiRow, addDetailgridiCol);
        addDetailgridiRow = null;
        addDetailgridiCol = null;
    }
    var discount = $("#search_discount").val();
    if (discount && discount != null && discount != "") {
        $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
            $('#addDetailgrid').setCell(value, "discount", discount);
            var var_actPrice = Math.round(discount * $('#addDetailgrid').getCell(value, "price")) / 100;
            var var_totActPrice = Math.round(var_actPrice * parseInt($('#addDetailgrid').getCell(value, "qty")) * 100) / 100;
            $('#addDetailgrid').setCell(value, "actPrice", var_actPrice);
            $('#addDetailgrid').setCell(value, "totActPrice", var_totActPrice);
        });
    }
    setFooterData();
}

function Returngoods() {
    $("#search_customerType").removeAttr('disabled');
    $("#search_origId").removeAttr('disabled');
    $("#search_destId").removeAttr('disabled');

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
            async: false,
            url: basePath + "/logistics/saleOrder/saveRetrun.do",
            data: {
                billNo: $("#search_billNo").val(),
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

function doPrint() {
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
                var addcont = "";
                //var ishave = false;
                for (var i = 0; i < msg.result.length; i++) {

                    if (billNo.indexOf(msg.result[i].type) >= 0) {
                        if (roleid == "JMSJS" && msg.result[i].isFranchisee == "is") {
                            addcont += "<div class='form-group' onclick=set('" + msg.result[i].id + "') title='" + msg.result[i].name + "'>" +
                                "<button class='btn btn-info'>" +
                                "<i class='cae-icon fa fa-refresh'></i>" +
                                "<span class='bigger-10'>套打" + msg.result[i].name + "</span>" +
                                "</button>" +
                                "</div>"
                        }
                        if (roleid != "JMSJS" && msg.result[i].isFranchisee != "is") {
                            addcont += "<div class='form-group' onclick=set('" + msg.result[i].id + "') title='" + msg.result[i].name + "'>" +
                                "<button class='btn btn-info'>" +
                                "<i class='cae-icon fa fa-refresh'></i>" +
                                "<span class='bigger-10'>套打" + msg.result[i].name + "</span>" +
                                "</button>" +
                                "</div>"
                        }

                    }
                }

                /* for (var i = 0; i < msg.result.length; i++) {
                 if (msg.result[i].saveownerid == curOwnerId) {
                 /!* addcont += "<div class='form-group' onclick=set('" + msg.result[i].id + "') title='" + msg.result[i].name + "'>" +
                 "<button class='btn btn-info'>" +
                 "<i class='cae-icon fa fa-refresh'></i>" +
                 "<span class='bigger-10'>套打" + msg.result[i].name + "</span>" +
                 "</button>" +
                 "</div>"*!/
                 ishave = true;
                 break;
                 }
                 }
                 if (ishave == true) {
                 for (var i = 0; i < msg.result.length; i++) {
                 if (msg.result[i].saveownerid == curOwnerId && billNo.indexOf(msg.result[i].type) >= 0) {
                 addcont += "<div class='form-group' onclick=set('" + msg.result[i].id + "') title='" + msg.result[i].name + "'>" +
                 "<button class='btn btn-info'>" +
                 "<i class='cae-icon fa fa-refresh'></i>" +
                 "<span class='bigger-10'>套打" + msg.result[i].name + "</span>" +
                 "</button>" +
                 "</div>"

                 }
                 }
                 }
                 if (ishave == false) {
                 for (var i = 0; i < msg.result.length; i++) {
                 if (msg.result[i].saveownerid == undefined && billNo.indexOf(msg.result[i].type) >= 0) {
                 addcont += "<div class='form-group' onclick=set('" + msg.result[i].id + "') title='" + msg.result[i].name + "'>" +
                 "<button class='btn btn-info'>" +
                 "<i class='cae-icon fa fa-refresh'></i>" +
                 "<span class='bigger-10'>套打" + msg.result[i].name + "</span>" +
                 "</button>" +
                 "</div>"

                 }
                 }
                 }*/

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
        url: basePath + "/sys/print/printMessage.do",
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

function doPrintA4() {

    var billno = $("#search_billNo").val();
    var destUnitId = $("#search_destUnitId").val();
    $.ajax({
        dataType: "json",
        url: basePath + "/sys/print/printA4Message.do",
        data: {"id": 21, "billno": billno, "destUnitId": destUnitId},
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
                /* var alldiscount=0;*/
                for (var a = 0; a < contDel.length; a++) {
                    var conts = contDel[a];
                    /*recordmessage += "<tr style='border-top:1px dashed black;padding-top:5px;'>" +
                     "<td align='left' style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + conts.sku + "</td>" +
                     "<td align='left'style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + conts.qty + "</td>" +
                     "<td style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + conts.price.toFixed(1) + "</td>" +
                     "<td style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + conts.actPrice.toFixed(1) + "</td>" +
                     "<td align='right' style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + (conts.actPrice*conts.qty).toFixed(2) + "</td>" +
                     "</tr>";*/

                    sum = sum + parseInt(conts.qty);
                    allprice = allprice + parseFloat(conts.totActPrice.toFixed(2));
                    //allprice = allprice + parseFloat(conts.actPrice*conts.qty.toFixed(2));
                    //alldiscount = alldiscount+parseFloat((conts.actPrice*conts.qty).toFixed(2));*/
                    recordmessage += "<tr style='border-top:1px ;padding-top:5px;'>" +
                        "<td align='left' style='border-top:1px ;padding-top:5px;width: 16%;font-size:17px;'>" + conts.styleId + "</td>" +
                        "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>" + conts.styleName + "</td>" +
                        "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>" + conts.colorId + "</td>" +
                        "<td align='left' style='border-top:1px ;padding-top:5px;width: 7%;font-size:17px;'>" + conts.sizeId + "</td>" +
                        "<td align='left' style='border-top:1px ;padding-top:5px;width: 7%;font-size:17px;'>" + conts.qty + "</td>" +
                        "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>" + conts.price.toFixed(2) + "</td>" +
                        "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>" + conts.totPrice.toFixed(2) + "</td>" +
                        "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>" + conts.discount + "</td>" +
                        "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>" + conts.actPrice.toFixed(2) + "</td>" +
                        "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>" + conts.totActPrice.toFixed(2) + "</td>" +
                        "</tr>";

                }
                /*alldiscount=alldiscount.toFixed(0);*/
                recordmessage += "<tr style='border-top:1px ;padding-top:5px;'>" +
                    "<td align='left' style='border-top:1px ;padding-top:5px;width: 16%;font-size:17px;'>&nbsp;</td>" +
                    "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>&nbsp;</td>" +
                    "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>&nbsp;</td>" +
                    "<td align='left' style='border-top:1px ;padding-top:5px;width: 7%;font-size:17px;'>&nbsp;</td>" +
                    "<td align='left' style='border-top:1px ;padding-top:5px;width: 7%;font-size:17px;'>" + sum + "</td>" +
                    "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>&nbsp;</td>" +
                    "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>&nbsp;</td>" +
                    "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>&nbsp;</td>" +
                    "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>&nbsp;</td>" +
                    "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>" + allprice.toFixed(2) + "</td>" +
                    "</tr>";

                $("#loadtabA4").html(recordmessage);
                //alert($("#edit-dialogA4").html());
                LODOP.SET_PRINT_STYLEA("baseHtml", 'Content', $("#edit-dialogA4").html());
                //LODOP.PREVIEW();
                LODOP.PRINT();
                $("#edit-dialog-print").hide();


            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}

function doPrintA41() {

    var billno = $("#search_billNo").val();
    var destUnitId = $("#search_destUnitId").val();
    $.ajax({
        dataType: "json",
        url: basePath + "/sys/print/printA4Message.do",
        data: {"id": 41, "billno": billno, "destUnitId": destUnitId},
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
                /* var printCodes = printCode.split(",");
                 for (var i = 0; i < printCodes.length; i++) {
                 var plp = printCodes[i];
                 var message = cont[plp];
                 if (message != "" && message != null && message != undefined) {
                 LODOP.SET_PRINT_STYLEA(printCodes[i], 'Content', message);
                 } else {
                 LODOP.SET_PRINT_STYLEA(printCodes[i], 'Content', "");
                 }

                 }*/

                var recordmessage = "";
                var sum = 0;
                var allprice = 0;
                /* var alldiscount=0;*/
                for (var a = 0; a < contDel.length; a++) {
                    var conts = contDel[a];
                    /*recordmessage += "<tr style='border-top:1px dashed black;padding-top:5px;'>" +
                     "<td align='left' style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + conts.sku + "</td>" +
                     "<td align='left'style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + conts.qty + "</td>" +
                     "<td style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + conts.price.toFixed(1) + "</td>" +
                     "<td style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + conts.actPrice.toFixed(1) + "</td>" +
                     "<td align='right' style='border-top:1px dashed black;padding-top:5px;font-size:12px;'>" + (conts.actPrice*conts.qty).toFixed(2) + "</td>" +
                     "</tr>";*/

                    sum = sum + parseInt(conts.qty);
                    allprice = allprice + parseFloat(conts.totActPrice.toFixed(2));
                    //allprice = allprice + parseFloat(conts.actPrice*conts.qty.toFixed(2));
                    //alldiscount = alldiscount+parseFloat((conts.actPrice*conts.qty).toFixed(2));*/
                    recordmessage += "<tr style='border-top:1px ;padding-top:5px;'>" +
                        "<td align='left' style='border-top:1px ;padding-top:5px;width: 16%;font-size:17px;'>" + conts.styleId + "</td>" +
                        "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>" + conts.styleName + "</td>" +
                        "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>" + conts.colorId + "</td>" +
                        "<td align='left' style='border-top:1px ;padding-top:5px;width: 7%;font-size:17px;'>" + conts.sizeId + "</td>" +
                        "<td align='left' style='border-top:1px ;padding-top:5px;width: 7%;font-size:17px;'>" + conts.qty + "</td>" +
                        "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>" + conts.price.toFixed(2) + "</td>" +
                        "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>" + conts.totPrice.toFixed(2) + "</td>" +
                        "</tr>";

                }
                /*alldiscount=alldiscount.toFixed(0);*/
                recordmessage += "<tr style='border-top:1px ;padding-top:5px;'>" +
                    "<td align='left' style='border-top:1px ;padding-top:5px;width: 16%;font-size:17px;'>&nbsp;</td>" +
                    "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>&nbsp;</td>" +
                    "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>&nbsp;</td>" +
                    "<td align='left' style='border-top:1px ;padding-top:5px;width: 7%;font-size:17px;'>&nbsp;</td>" +
                    "<td align='left' style='border-top:1px ;padding-top:5px;width: 7%;font-size:17px;'>" + sum + "</td>" +
                    "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>&nbsp;</td>" +
                    "<td align='left' style='border-top:1px ;padding-top:5px;width: 10%;font-size:17px;'>&nbsp;</td>" +
                    "</tr>";

                $("#loadtabA41").html(recordmessage);
                //alert($("#edit-dialogA4").html());
                LODOP.SET_PRINT_STYLEA("baseHtml", 'Content', $("#edit-dialogA41").html());
                //LODOP.PREVIEW();
                LODOP.PRINT();
                $("#edit-dialog-print").hide();


            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}

function showCodesDetail(uniqueCodes) {

    $("#show-uniqueCode-list").modal('show');
    initUniqueCodeList(uniqueCodes);
    codeListReload(uniqueCodes);
}

function guestBalanceChange() {
    var actPrice = parseFloat($("#search_actPrice").val());
    var payPrice = parseFloat($("#search_payPrice").val());
    var preBalance = parseFloat($("#pre_Balance").val());
    var afterBalance = parseFloat(preBalance + payPrice - actPrice).toFixed(2);

    $("#after_Balance").val(afterBalance);
    if (afterBalance < 0) {
        $("#SODtl_save").attr({"disabled": "disabled"});
        /*bootbox.confirm({
            title: "余额确认",
            buttons: {confirm: {label: '确定'}, cancel: {label: '取消'}},
            message: "客户余额不足，是否继续开单？",
            callback: function (result) {
                $("#SODtl_save").removeAttr("disabled");
                if (result) {
                    saveAjax();
                    $.gritter.add({
                        text: "开单成功",
                        class_name: 'gritter-success  gritter-light'
                    });
                } else {
                }
            }
        });*/
        if (confirm("客户余额不足，是否继续开单")) {
            saveAjax();
            flag = true;
        }

        $("#SODtl_save").removeAttr("disabled");
    } else {
        saveAjax();
        flag = true;
    }
}

function exchangeGoods() {
    wareHouse = $("#search_origId").val();
    billNo = $("#search_billNo").val();
    $("#exchangeGoods-dialog").modal('show');
    $("#addDetailgrid").trigger("reloadGrid");
}

function findRetrunno() {
    $("#show-findRetrunNo-list").modal('show');
    initUniqueretrunList();
    retrunListReload();

}

function findshopMessage() {
    $("#show-findWxShop-list").modal('show');
    initfindWxShopList();

}

function sendStreamNO() {
    $("#show-sendStreamNO-list").modal('show');

}