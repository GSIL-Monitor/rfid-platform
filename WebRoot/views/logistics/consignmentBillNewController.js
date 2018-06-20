var comsigment_status;
$(function () {
    /*初始化左侧grig*/
    initSearchGrid();
    initForm();
});
function initForm() {
    initSelectOrigForm();
    initSelectDestForm();
    initSelectBusinessIdForm();
    initSelectDestEditForm();
    initCustomerTypeForm();
    if(billNo){
        bootbox.alert("单据"+billNo+"正在编辑中");
    }else{
        sessionStorage.removeItem("billNoConsignment");
    }
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
    $("#grid").jqGrid({
        height: 'auto',
        datatype: 'json',
        url: basePath + "/logistics/Consignment/page.do?filter_GTI_status=-1&userId="+userId,
        mtype: 'POST',
        colModel: [
            {name: 'billNo', label: "单号", width: 50},
            {name: "status", hidden: true},
            {name: 'outStatus', label: '出库状态', hidden: true},
            {name: 'inStatus', label: '入库状态', hidden: true},
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
                            html = "<i class='fa fa-tasks blue' title='结束'></i>";
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
            {name: 'billDate', label: '单据日期', width: 35},
            {name: 'customerType', label: "客户类型", hidden: true},
            {
                label: "客户类型", width: 40,
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
            /*{name: 'origName', label: '出库仓库', width: 30},*/
            {name: 'destId', label: '收货仓库ID', hidden: true},
            {name: 'destName', label: '收货仓库', width: 30,hidden: true},
            {name: 'discount', label: '整单折扣', hidden: true},
            {name: 'busnissId', label: '销售员', hidden: true},
            {name: 'destUnitId', label: '收货方ID', hidden: true},
            {name: 'destUnitName', label: '收货方', width: 30, hidden: true},
            {name: 'totQty', label: '单据数量', width: 15},
            {name: 'totOutQty', label: '已出库数量', width: 30, hidden: true},
            {name: 'totOutVal', label: '总出库金额', width: 30, hidden: true},
            {name: 'totInQty', label: '已入库数量', width: 30, hidden: true},
            {name: 'totInVal', label: '总入库金额', width: 30, hidden: true,
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
            {name: 'remark', label: '备注', width: 40, hidden: true}

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
        }
    });
}
function initDetailData(rowid) {
    var rowData = $("#grid").getRowData(rowid);
    $("#editForm").setFromData(rowData);
    comsigment_status=rowData.status;

}
function setFooterData() {
    debugger;
    var sum_totQty = $("#grid").getCol('totQty', false, 'sum');
    var sum_totOutQty = $("#grid").getCol('totOutQty', false, 'sum');
    var sum_totOutVal = $("#grid").getCol('totOutVal', false, 'sum');
    var sum_totInQty = $("#grid").getCol('totInQty',false,'sum');
    var sum_totInVal = $("#grid").getCol('totInVal', false, 'sum');
    var sum_payPrice = $("#grid").getCol('payPrice', false, 'sum');
    $("#grid").footerData('set', {
        billNo: "合计",
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
            /*if (defaultSaleStaffId != "" && defaultSaleStaffId != undefined) {
                $("#search_busnissId").val(defaultSaleStaffId);
            }*/
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
           /* if (pageType === "add") {
                if (defaultSaleStaffId != "" && defaultSaleStaffId != undefined) {
                    $("#search_customerType").val("CT-LS");
                }

            }*/
        }
    });
}