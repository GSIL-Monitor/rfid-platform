/**
 * Created by yushen on 2017/7/10.
 */
var searchUrl = basePath + "/prod/color/page.do";
$(function () {
    initGrid();
});

function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: searchUrl,
        datatype: "json",
        mtype: "POST",
        colModel: [

            {name: 'id', label: 'id', hidden: true, width: 20},
            {name: 'billNo', label: '单据编号', editable: true, width: 40},
            {
                name: '', label: '操作', editable: true, width: 40, align: 'center',
                formatter: function (cellValue, option, rowObject) {
                    var html;
                    html = "<a href='" + basePath + "/logistics/transferOrder/edit.do?billNo=" + billNo + "'><i class='ace-icon fa fa-edit' title='编辑'></i></a>";
                    return html;
                }
            },
            {name: 'billType', label: '收/付款', editable: true, width: 80},
            {name: 'billDate', label: '支付日期', editable: true, width: 80},
            {name: 'customsId', label: '客户',editable: true, width: 80},
            {name: 'vendorId', label: '供应商', editable: true, width: 200},
            {name: 'payPrice', label: '支付金额', editable: true, width: 200},
            {name: 'oprId', label: '操作人', editable: true, width: 200},
            {name: 'ownerId', label: '当前用户', editable: true, width: 200,hidden:true},
            {name: 'remark', label: '更新时间', editable: true, width: 200}
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
        sortname: 'colorId',
        sortorder: "desc"

    });

}

function refresh() {
    location.reload(true);
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

function closeEditDialog() {
    $("#edit_color_dialog").modal('hide');
}

function add() {
    $("#editColorForm").resetForm();
    $("#edit_color_dialog").modal('show');
    // $("#form_colorId").attr("disabled", true);
}
function edit() {
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#grid").jqGrid('getRowData', rowId);
        $("#edit_color_dialog").modal("show");
        $("#editColorForm").loadData(row);
        // $("#form_colorId").attr("disabled", true);

    } else {
        bootbox.alert("请选择一项进行修改！");
    }
}


