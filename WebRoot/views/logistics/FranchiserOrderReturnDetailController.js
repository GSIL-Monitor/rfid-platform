var billNo;
$(function () {
    initGrid();
    initForm();
    if(billNo!=""){
        sessionStorage.setItem("billNosaleReturn",billNo);
    }
});

function initForm() {
    initCustomerTypeForm();
    initSelectDestForm();
    initSelectOrigForm();
    initSelectBusinessIdForm();
    $("#search_customerType").attr('disabled', true);
    $("#search_destId").attr('disabled', true);
    $("#search_origId").attr('disabled', true);
    $("#search_busnissId").attr('disabled', true);
    $("#search_customerType").val(saleOrderReturn_customerType);
    $("#search_origId").val(saleOrderReturn_origId);
    $("#search_destId").val(saleOrderReturn_destId);
    $("#search_busnissId").val(saleOrderReturn_busnissId);
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
        }
    });
}

function initSelectOrigForm() {
    if(userId=="admin"){
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
    }else{
        $.ajax({
            url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + $("#search_origUnitId").val(),
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
    if(userId=="admin"){
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
    }else{
        $.ajax({
            url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + curOwnerId,
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
    if(curOwnerId=="1"){
        url=basePath + "/sys/user/list.do?filter_EQI_type=4";
    }else{
        url=basePath + "/sys/user/list.do?filter_EQI_type=4&filter_EQS_ownerId=" + curOwnerId;
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
            if(defaultSaleStaffId!=""&&defaultSaleStaffId!=undefined){
                $("#search_busnissId").val(defaultSaleStaffId);
            }
        }
    });
}

function initGrid() {
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

            {name: 'styleId', label: '款号', width: 40},
            {name: 'styleName', label: '款式', width: 40},
            {name: 'colorId', label: '色号', width: 40},
            {name: 'colorName', label: '颜色', width: 30},
            {name: 'sizeId', label: '尺码', width: 30},
            {name: 'sizeName', label: '尺寸', width: 40},
            {
                name: 'qty', label: '数量', width: 40, editable: true,
                editrules: {
                    number: true,
                    minValue: 1
                }
            },
            {name: 'outQty', label: '已出库数量', width: 40},
            {name: 'inQty', label: '已入库数量', width: 40},
            {name: 'sku', label: 'sku', width: 50},
            {name: 'price', label: '销售价格', width: 40,
                formatter: function (cellValue, options, rowObject) {
                    var price=parseFloat(cellValue).toFixed(2);
                    return price;
                }
            },
            {name: 'uniqueCodes', label: '唯一码', hidden: true},
            {name:'',label:'唯一码明细',width:40, align:"center",
                formatter: function (cellValue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=showCodesDetail('" + rowObject.uniqueCodes + "')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
                }
            }
        ],
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: -1,
        multiselect: false,
        shrinkToFit: true,
        pager: '#addDetailgrid-pager',
        sortname: 'id',
        sortorder: "asc",
        footerrow: true,
        cellEdit: true,
        cellsubmit: 'clientArray'
    });
}

function showCodesDetail(uniqueCodes) {
    $("#show-uniqueCode-list").modal('show');
    initUniqueCodeList(uniqueCodes);
    codeListReload(uniqueCodes);
}