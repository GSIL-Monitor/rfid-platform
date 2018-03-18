/**
 * Created by yushen on 2017/7/10.
 */
var searchUrl = basePath + "/logistics/monthAccountStatement/page.do";
$(function () {
    initGrid();
});

function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: searchUrl,
        mtype: "POST",
        datatype: "json",
        colModel: [
            {name: 'billDate', label: '单据日期', sortable: true, width: 200},
            {name: 'month', label: '月份', sortable: false, width: 150},
            {name: 'billType', label: '收/付款', sortable: false, width: 150},
            {name: 'unitId', label: '对象', sortable: false, width: 150},
            {name: 'unitType', label: '客户类型', sortable: false, width: 150},
            {name: 'ownerId', label: '单据所属', sortable: false, width: 150},
            {name: 'totVal', label: '总收/欠款金额', sortable: false, width: 150,
                formatter: function (cellValue, options, rowObject) {
                    var totVal=rowObject.totVal.toFixed(2);
                    return totVal;
                }
            },
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
        sortname: 'billDate',
        sortorder: 'desc',
        autoScroll: false,

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


