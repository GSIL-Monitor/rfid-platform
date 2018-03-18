var searchUrl = basePath + "/exchange/interfaceModel/page.do";
$(function () {
    initGrid();
});
var pageType;

function refresh(){
    location.reload(true);
}

function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: searchUrl,
        mtype:"POST",
        datatype: "json",
        colModel: [
            {name: 'code', label: '编号',editable:true, width: 100},
            {name: 'name', label: '名称',hidden:true, width: 250},
            {name: 'origDs', label: '源数据源',editable:true, width: 250},
            {name: 'origTable', label: '源数据库对应表', editable:true,width: 300},
            {name: 'destDs', label: '目标数据源', editable:true,width: 250},
            {name: 'destTable',label:'目标数据库对应表',editable:true,width:300},

        ],
        viewrecords: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        sortname : 'id',
        sortorder : "asc",
        shrinkToFit:false,
        autoScroll:false,
        autowidth: true
    });

}



function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");

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

function add() {
    window.location.href=basePath+"/views/exchange/interfaceModel_edit.jsp";
}


function reload(){
    location.reload(true);
}

function edit() {
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if(rowId) {
        var row = $("#grid").jqGrid('getRowData',rowId);
        window.location.href=basePath+"/exchange/interfaceModel/showEdit.do?code="+row.code;
    } else {
        bootbox.alert("请选择一项！");
    }
}