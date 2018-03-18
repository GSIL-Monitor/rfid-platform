
var url=basePath + "/product/SendStreamNOMessage/page.do";
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
            {name: 'billNo', label: '单号', editable:true,width: 100},
            {name: 'customName', label: '客户名字', editable:true,width: 100},
            {name: 'phone', label: '电话', editable:true,width: 100},
            {name: 'address', label: '地址', editable:true,width: 100},
            {name: 'streamNO', label: '快递单号', editable:true,width: 100}
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
        url: basePath + "/product/SendStreamNOMessage/WxShopStreamNO.do",
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $.gritter.add({
                text: data.result,
                class_name: 'gritter-success  gritter-light'
            });
        }
    });
}



