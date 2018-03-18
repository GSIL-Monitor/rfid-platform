var searchUrl=basePath+"/smart/activity/page.do";
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
            {name: 'url', label: '图片',editable:true, width: 120,
                formatter:function(cellValue, options, rowObject){
                    if(cellValue!=null){
                        var images=cellValue;
                        var n=images.split(',');
                        var  html="<img width=80px height=100px src='"+basePath+"/mirror/activity/"+n[0]+"'/>";
                        return html;
                    }else{
                        return "暂无图片";
                    }
                }},
            {name: 'id', label: '编号',editable:true, width: 100},
            {name: 'activityTime', label: '活动日期', editable:true,width: 150},
            {name: 'isShow', label: '是否展示',editable:true, width: 150},
            {name: 'seqNo', label: '序号',editable:true, width: 100},
            {name: 'remark', label: '备注',editable:true, width: 200},
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

function add(){
    window.location.href=basePath+'/views/smart/activityInfo_edit.jsp';
}

function edit(){
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if(rowId) {
        var row = $("#grid").jqGrid('getRowData',rowId);
        window.location.href=basePath+"/smart/activity/edit.do?id="+row.id;
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
function preview(){

}