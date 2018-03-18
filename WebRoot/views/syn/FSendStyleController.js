
var url=basePath + "/sys/PushStyle/page.do";
$(function () {
    initGrid();
    /*if(roleId == '0'){
        $("#add_style_button").removeAttr("disabled");
    }else{
        $("#add_style_button").attr({"disabled": "disabled"});
    }*/

});
function refresh(){
    location.reload(true);
}

function initGrid() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid({
        height:  "auto",
        url: url,
        postData : params,
        datatype: "json",
        mtype:"POST",
        colModel: [
            {name: 'id', label:'编号',editable:true,width: 100},
            {name: 'styleId', label: '款号',editable:true, width: 100,sortable: true,frozen:true},
            {name: 'styleName', label: '款名', editable:true,width: 100},
            {name: 'price', label: '吊牌价', editable:true,width: 80},
            {name:'brandCode',label:'品牌',editable:true,width:80},
            {name:'sizeSortId',label:'尺寸组',eidtable:true,width:80},
            {name:'styleEname',label:'英文名',editable:true,width:80},
            {name:'oprId',label:'创建人',editable:true,width:80},
            {name: 'isUse', label: '启用状态', hidden: true},
            {name:'updateTime',label:'更新时间',editable:true,width:160},
            {name: 'remark', label: '款描述', width: 120,sortable:false}

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
        sortname : 'id',
        sortorder : "desc",
        autoScroll:false

    });


}


function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");

}

function save() {


}

function unSelected() {

}

function _search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);

    $("#grid").jqGrid("setGridParam",{
        url: url,
        page : 1,
        postData : params
    }).trigger('reloadGrid');
}

function cleanSearch() {

}

function del() {

}
function locked() {


}
function push() {
    $.ajax({
        url: basePath + "/sys/PushProduct/WxShopStyle.do",
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {

        }
    });
}



	