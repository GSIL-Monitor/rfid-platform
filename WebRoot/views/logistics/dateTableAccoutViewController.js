/**
 * Created by yushen on 2017/7/12.
 */
var searchUrl = basePath + "/logistics/dateTableAccoutView/page.do";
$(function () {
    initDate();
    inittitle();
    initGrid();

});
function initDate() {
    var startDate = getToDay("yyyy-MM-dd");
    var endDate = getToDay("yyyy-MM-dd");
    $('.startDate').datepicker('setDate', startDate);
    $('.endDate').datepicker('setDate', endDate);
}
function inittitle() {
    $.ajax({
        dataType: "json",
        url: basePath + "/logistics/dateTableAccoutView/findtiltle.do",
        data: {
            startDate: $('#search_createTime').val(),
            endDate:$('#endDate').val()
        },
        type: "POST",
        success: function (msg) {
            debugger;
           if (msg.success) {
                $("#saleNum").text(msg.result.saleNum.toFixed(2));
               $("#saleRetrunNum").text(msg.result.saleRetrunNum.toFixed(2));
               $("#storedvalueNum").text(msg.result.storedvalueNum.toFixed(2));
               $("#memberNum").text(msg.result.memberNum.toFixed(2));
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });

}

function initGrid() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid({
        height: "auto",
        mtype: "POST",
        datatype: "json",
        url: searchUrl,
        postData: params,
        colModel: [
            {name: 'billNo', label: '编号', sortable: false, width: 200},
            {name: 'billDate', label: '单据日期', sortable: false, width: 200},
            {name: 'billType', label: '单据类型', sortable: false, width: 150},
            {name: 'unitType', label: '对象类型', sortable: false, width: 150, hidden: true},
            {
                name: '', label: '对象类型', sortable: false, width: 150, hidden: true,
                formatter: function (cellValue, options, rowObject) {
                    if(rowObject.unitType === "1"){
                        return "客户";
                    }else if(rowObject.unitType === "0"){
                        return "供应商";
                    }
                }
            },
            {name: 'unitId', label: '对象', sortable: false, hidden: true},
            {name: 'actPrice', label: '实际价格', sortable: false, width: 150,
                formatter: function (cellValue, options, rowObject) {
                    var actPrice=rowObject.actPrice.toFixed(2);
                    return actPrice;
                }
            },
            {name: 'payPrice', label: '支付价格', sortable: false, width: 150,
                formatter: function (cellValue, options, rowObject) {
                    var payPrice=rowObject.payPrice.toFixed(2);
                    return payPrice;
                }
            },
            {name: 'diffPrice', label: '本单差额', sortable: false, width: 150,
                formatter: function (cellValue, options, rowObject) {
                    var diffPrice=rowObject.diffPrice.toFixed(2);
                    return diffPrice;
                }
            },
            {name: 'oprId', label: '操作人', sortable: false, width: 150},
            {name: 'remark', label: '备注', sortable: false, width: 150},
            {name: 'ownerIdname', label: '单据所属', sortable: false, width: 150}
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
        // sortname: 'billDate',
        // sortorder: 'asc',
        autoScroll: false


    });
}

function refresh() {
    location.reload(true);
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
function openSearchGuestDialog() {
    dialogOpenPage = "saleOrder";
    $("#modal_guest_search_table").modal('show').on('shown.bs.modal', function () {
        initGuestSelect_Grid();
    });
    $("#searchGuestDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='confirm_selected_GuestId_sale()'>确认</button>"
    );
}

function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}

function showDetailPage() {
    location.href = basePath + "/sys/guestAccount/showDetailPage.do?userId=" + userId;
}

/*
function initSumInfo() {
    $.ajax({
        url: basePath + "/sys/guestAccount/initSumInfo.do",
        cache: false,
        async: false,
        data: {
            unitType: $("#search_unitType").val(),
            nameOrTel: $("#search_nameOrTel").val(),
            userId: userId
        },
        type: "POST",
        success: function (data, textStatus) {
            var resultMap = data.result;
            $("#sumOfCustomer").text(resultMap.totGuestNum);
            $("#sumOfOwningCustomer").text(resultMap.totOwingQty);
            $("#sumOfOwningValue").text("￥" + resultMap.totOwingValue);
            $("#sumOfRechargeCustomer").text(resultMap.totStoredQty);
            $("#sumOfRechargeValue").text("￥" + resultMap.totStoredValue);
        }
    });
}*/
function checkout() {
    $.ajax({
        dataType: "json",
        url: basePath + "/logistics/dateTableAccoutView/dateStockDetail.do",
        data: {},
        type: "POST",
        success: function (msg) {
            debugger;
            if (msg.success) {
                bootbox.alert(msg.msg);
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}
