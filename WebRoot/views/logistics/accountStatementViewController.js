/**
 * Created by yushen on 2017/7/10.
 */
var searchUrl = basePath + "/logistics/accountStatementView/page.do";
$(function () {
    initDate();
    initGrid();
});

function initDate(){
    var startDate = getMonthFirstDay("yyyy-MM-dd");
    var endDate = getToDay("yyyy-MM-dd");
    $('.startDate').datepicker('setDate', startDate);
    $('.endDate').datepicker('setDate', endDate);
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
            {name: 'unitId', label: '对象', sortable: false, hidden: true},
            {name: 'actPrice', label: '实际价格', sortable: false, width: 150},
            {name: 'payPrice', label: '支付价格', sortable: false, width: 150},
            {name: 'diffPrice', label: '本单差额', sortable: false, width: 150},
            {name: 'oprId', label: '操作人', sortable: false, width: 150},
            {name: 'remark', label: '备注', sortable: false, width: 150},
            {name: 'ownerId', label: '单据所属', sortable: false, width: 150},
            {name: 'groupId', label: '组号', hidden: true},
            {name: 'totalOwingVal',label:'累计欠款',sortable:false,width:150,
                formatter: function (cellValue, options, rowObject) {
                    var totalOwingVal=rowObject.totalOwingVal.toFixed(2);
                    return totalOwingVal;
                }
            }

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
        // sortname: 'unitId',
        // sortorder: 'asc',
        autoScroll: false,

        grouping: true,
        groupingView: {
            groupField: ['groupId'],
            groupColumnShow: [false],
            groupText: ['<b>{0}</b>'],
            plusicon: 'ace-icon fa fa-plus',
            minusicon: 'ace-icon fa fa-minus'
        },
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
