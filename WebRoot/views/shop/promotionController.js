$(function(){
    //初始化
    initGrid();
    setTimeout(function() {
        _search();
    },500);
});

function initGrid(){
    $("#grid").jqGrid({
        height:"auto",
        mtype:"POST",
        datatype: "json",
        colModel: [
            {name: 'status', label: '状态',width: 60},
            {name: 'id', label: '活动编号', editable:true,width: 150,key:true},
            {name: 'name', label: '活动名称',width: 150},
            {name: 'billDate', label: '单据日期',width: 150},
            {name: 'priceType', label: '促销类型',width: 150,
                formatter:function(value,rows,rowObject){
                    switch(value) {
                        case "DP":
                            return "单品促销";
                        case "TJ":
                            return "套件促销";
                        case "ZP":
                            return "赠品促销";
                        case "MZ":
                            return "满增促销";
                        case "MJ":
                            return "满减促销";
                        case "MN":
                            return "满M件减N件促销";
                    }
                }},
            {name: 'redSDate', label: '活动开始日期', width: 200},
            {name: 'redEDate', label: '活动结束日期', width: 150},
            {name: 'registeTime', label: '创建时间',width: 200},
            {name:'registerId',label:'创建人',sortable:false,width:200},
            {name:'remark',label:'备注',sortable:false,width:400}
        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: false,
        sortname : 'createDate',
        sortorder : "asc",
        autoScroll:false

    });
    $("#grid").jqGrid("setFrozenColumns");
}
function add(){
	location.href=basePath+"/shop/promotion/add.do";
}
function editCashier(){
	var rowId = $("#grid").jqGrid("getGridParam", "selrow");
	$("#form_code").attr("readonly",true);
    if(rowId) {
        var row = $("#grid").jqGrid('getRowData',rowId)
        location.href=basePath+"/shop/promotion/edit.do?id="+row.id;

    } else {
        bootbox.alert("请选择一项进行修改！");
    }
}
function refresh(){
	location.reload(true);
}

function _search() {

    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);

    $("#grid").jqGrid('setGridParam', {
        url:basePath + "/shop/promotion/page.do",
        page : 1,
        postData : params
    });
    $("#grid").trigger("reloadGrid");
}
function showAdvSearchPanel(){
	 $("#searchPanel").slideToggle("fast");
}
function _clearSearch(){

}
function _addMzcx() {
    location.href=basePath+"/shop/promotion/addMzcx.do";
}
function _addMjcx() {
    location.href=basePath+"/shop/promotion/addMjcx.do";
}