/**
 * Created by yushen on 2017/7/12.
 */
var searchUrl = basePath + "/sys/guestAccount/page.do?userId=" + userId;
$(function () {
    initGrid();
    initSumInfo();
});

function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        mtype: "POST",
        datatype: "json",
        url: searchUrl,
        colModel: [
            {name: 'id', label: '客户编号', sortable: true, width: 150},
            {name: 'name', label: '客户名称', sortable: true, width: 150},
            {name: 'unitTypeName', label: '客户类型', sortable: true, width: 150},
            {
                name: "", label: "查看明细", width: 50, editable: false, align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    var guestId = rowObject.id;
                    var html;
                    html = "<a href='" + basePath + "/sys/guestAccount/viewStatement.do?guestId=" + guestId + "&userId=" + userId + "'><i class='ace-icon fa fa-list' title='查看流水单'></i></a>";
                    return html;
                }
            },
            {name: 'tel', label: '电话', sortable: false, width: 150},
            {name: 'owingValue', label: '欠款', sortable: false, width: 150},
            {name: 'storedValue', label: '累计充值金额', width: 150},
            {name: 'ownerId', label: '所属方', width: 100}
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
        sortname: 'id',
        sortorder: 'desc',
        autoScroll: false,
        gridComplete: function () {
            initSumInfo();
        }
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


function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}

function showDetailPage() {
    location.href = basePath + "/sys/guestAccount/showDetailPage.do?userId=" + userId;
}

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
            $("#sumOfOwningValue").text("￥" + (Number(resultMap.totOwingValue)/10000).toFixed(4)+"万元");
            $("#sumOfRechargeCustomer").text(resultMap.totStoredQty);
            $("#sumOfRechargeValue").text("￥" + (Number(resultMap.totStoredValue)/10000).toFixed(4)+"万元");
        }
    });
}