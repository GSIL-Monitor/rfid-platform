$(function () {
    initGrid();
});
function showAdvSearchPanel() {

    $("#searchPanel").slideToggle("fast");

}
function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: basePath + "/logistics/labelChangeBill/page.do",
        datatype: "json",
        mtype: 'POST',
        sortorder: 'desc',
        colModel: [

            {name: 'billNo', label: '单据编号', sortable: true, width: 40},
            {
                name: "", label: "操作", width: 60, editable: false, align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    var billNo = rowObject.billNo;
                    var html;
                    html = "<a style='margin-left: 20px' href='" + basePath + "/logistics/purchase/edit.do?billNo=" + billNo + "'><i class='ace-icon fa fa-edit' title='编辑'></i></a>";

                    html += "<a style='margin-left: 20px' href='#' onclick=cancel('" + billNo + "')><i class='ace-icon fa fa-undo' title='撤销'></i></a>";


                    html += "<a style='margin-left: 20px' href='#' onclick=quit('" + rowObject.billNo + "')><i class='ace-icon fa fa-check-circle-o' title='修改'></i></a>";

                    return html;

                }
            },
            {name: 'status', hidden: true},
            {name: 'billDate', label: '单据日期', sortable: true, width: 40},
            {name: 'destName', label: '入库仓库', hidden: true},
            {name: 'origName', label: '出库仓库', width: 40},
            {name: 'beforeclass9', label: '原系列', width: 40},
            {name: 'nowclass9', label: '现系列', width: 40},
            {name: 'changeType', label: '类型', width: 40},
            {name: 'remark', label: '备注', sortable: false, width: 40},
            {name: 'id', hidden: true}
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
        autoScroll: false,
        footerrow: true,
        gridComplete: function () {
            //setFooterData();
        },
        onSelectRow: function (rowid, status) {
        }
    });
}

function add(type) {
    location.href = basePath + "/logistics/labelChangeBill/add.do?type="+type;
}