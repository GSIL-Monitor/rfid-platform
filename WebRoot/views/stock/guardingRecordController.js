var searchUrl = basePath + "/stock/guardingRecord/page.do?userId="+userId;
$(function () {
    initGrid();
});
function refresh(){
    location.reload(true);
}
function initGrid() {
    $("#grid").jqGrid({
        height:  "auto",
        url: searchUrl,
        datatype: "json",
        colModel: [
            {name: 'code', label: 'Code', editable:true,width: 200},
            {name: 'recordDate', label: '记录时间',editable:true, width: 150},
            {name: 'deviceId', label: '设备',editable:true, width: 150},
            {name:'shopName',label:'门店', width: 150},
            {name: 'isAlert', label: '是否警报', width: 150},
            {name: 'styleId', label: '款式', width: 200},
            {name: 'colorId', label: '颜色', width: 200},
            {name: 'sizeId', label: '尺寸', width: 200}

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
        sortname : 'recordDate',
        sortorder : "desc",
        autoScroll:false

    });
    $("#grid").jqGrid("setFrozenColumns");
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


function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");

}

function add(){
}