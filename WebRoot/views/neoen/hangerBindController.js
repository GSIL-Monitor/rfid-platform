var searchUrl=basePath + "/neoen/hangerBind/page.do";
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
            {name: "code", label: "EPC", width: 300,editable:false},
            {name: 'styleId', label: '款号',editable:false, width: 200},
            {name: 'styleName', label: '款名',editable:false, width: 200},
            {name: 'updateTime', label: '更新时间',editable:true, width: 250},
            {name: 'sku', label: 'SKU', editable:true,width: 200},
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
