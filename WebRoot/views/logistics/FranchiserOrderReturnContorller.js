var searchUrl = basePath + "/logistics/FranchiserOrderReturn/page.do?filter_GTI_status=-1&userId=" + userId;
$(function () {
    initGrid();
    initForm();
});

function initForm() {
    initSelectOrigForm();
    initSelectDestForm();
}

function initSelectOrigForm() {
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

function initSelectDestForm() {
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

function initGrid() {
    $("#grid").jqGrid({
        height: 'auto',
        datatype: 'json',
        url: basePath + "/logistics/FranchiserOrderReturn/page.do?filter_GTI_status=-1&userId=" + userId,
        mtype: 'POST',
        colModel: [
            {name: 'billNo', label: "单号", width: 40, sortable: true},
            {
                name: '', label: '操作', width: 50, align: "center", sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    var html = "<a style='text-decoration: none;margin-left: 20px' href='" + basePath + "/logistics/FranchiserOrderReturn/edit.do?billNo=" + rowObject.billNo + "'><i class='ace-icon fa fa-edit'></i></a>";
                    return html;
                }
            },
            {name: "status", hidden: true},
            {name: 'outStatus', label: '出库状态', hidden: true},
            {name: 'inStatus', label: '入库状态', hidden: true},
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
                name: 'outStatusImg', label: '出库状态', width: 25, align: 'center', sortable: false,
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
                name: 'inStatusImg', label: '入库状态', width: 25, align: 'center', sortable: false,
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
            {name: 'customerType', label: "客户类型", width: 30, hidden: true},
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
            {name: 'origUnitId', label: '退货客户ID', hidden: true},
            {name: 'origUnitName', label: '退货客户', width: 30},
            {name: 'origId', label: '出库仓库ID', hidden: true},
            {name: 'origName', label: '出库仓库', width: 30},
            {name: 'destId', label: '收货仓库ID', hidden: true},
            {name: 'destName', label: '收货仓库', width: 30},
            {name: 'destUnitId', label: '收货方ID', hidden: true},
            {name: 'destUnitName', label: '收货方', width: 30},
            {name: 'totQty', label: '单据数量', width: 30},
            {name: 'totOutQty', label: '已出库数量', width: 30},
            {name: 'totInQty', label: '已入库数量', width: 30},
            {name: 'remark', label: '备注', width: 40}
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
        }
    });
}

function setFooterData() {

    var sum_totQty = $("#grid").getCol('totQty', false, 'sum');
    var sum_totOutQty = $("#grid").getCol('totOutQty', false, 'sum');
    var sum_totOutVal = $("#grid").getCol('totOutVal', false, 'sum');
    var sum_totInQty = $("#grid").getCol('totInQty', false, 'sum');
    var sum_totInVal = $("#grid").getCol('totInVal', false, 'sum');
    var sum_totActPrice = $("#grid").getCol('actPrice', false, 'sum');
    var sum_payPrice = $("#grid").getCol('payPrice', false, 'sum');
    /*$("#search_actPrice").val(sum_totActPrice);*/
    $("#grid").footerData('set', {
        billNo: "合计",
        totQty: sum_totQty,
        totOutQty: sum_totOutQty,
        totOutVal: sum_totOutVal,
        totInQty: sum_totInQty,
        totInVal: sum_totInVal,
        actPrice: sum_totActPrice,
        payPrice: sum_payPrice
    });
}

function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}

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

function refresh() {
    location.reload(true);
}

var dialogOpenPage;
function openSearchGuestDialog() {
    dialogOpenPage = "saleOrderReturn";
    $("#modal_guest_search_table").modal('show').on('shown.bs.modal', function () {
        initGuestSelect_Grid();
    });
    $("#searchGuestDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='confirm_selected_GuestId_saleReturn()'>确认</button>"
    );
}
