var searchUrl=basePath+"/smart/home/page.do";
$(function() {
    initGrid();
})
function initGrid(){
    $("#grid").jqGrid({
        height: "auto",
        url: searchUrl,
        mtype:"POST",
        datatype: "json",
        colModel: [
            {name: 'id', label: '编号',editable:true, width: 100},
            {name: 'name', label: '名称',editable:true, width: 200},
            {name: 'seqNo', label: '序号', editable:true,width: 200},
            {name:'createTime',label:'创建时间',editable:true, width: 200},
            {name:'updateTime',label:'修改时间',editable:true, width: 200},
            {name: 'remark', label: '备注',editable:true, width: 250}
        ],
        viewrecords: true,
        rownumbers: true,
        altRows: true,
        rowNum: 10,
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


function edit(){
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if(rowId) {
        var row = $("#grid").jqGrid('getRowData',rowId);
        window.location.href=basePath+"/smart/home/edit.do?id="+row.id;
    } else {
        bootbox.alert("请选择一项进行修改！");
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
function add(){
    window.location.href=basePath+'/views/smart/homeInfo_edit.jsp';
}