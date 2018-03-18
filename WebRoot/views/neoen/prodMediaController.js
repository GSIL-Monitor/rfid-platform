var searchUrl=basePath + "/neoen/prodMedia/page.do";
$(function () {
    initGrid();
});
function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");

}
function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: searchUrl,
        mtype:"POST",
        datatype: "json",
        colModel: [
            {name: "styleId", label: "款号", width: 150,editable:false},
            {name: 'styleName', label: '款名',editable:false, width: 150},
            {name: 'fab', label: 'FAB',editable:true, width: 150},
            {name: 'stockQty', label: '库存', editable:true,width: 200},
            {name: 'designer', label: '设计师', editable:true,width: 150},
            {name: 'remark', label: '备注', editable:true,width: 300},
        ],
        viewrecords: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        sortname : 'styleId',
        sortorder : "desc",
        shrinkToFit:false,
        autowidth: true
    });
}

function add() {
    window.location.href=basePath+"/views/neoen/prodMedia_edit.jsp";
}

function edit() {
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#grid").jqGrid('getRowData',rowId);
        window.location.href=basePath+"/neoen/prodMedia/toEdit.do?styleId="+row.styleId;
    } else {
        bootbox.alert("请选择一项");
    }
}
function _search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        url:searchUrl,
        page : 1,
        postData : params
    });
    $("#grid").trigger("reloadGrid");
}

function _clearSearch() {
    $("#searchForm").resetForm();
}
function refresh(){
    location.reload(true);
}

