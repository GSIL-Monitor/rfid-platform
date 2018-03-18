
var url=basePath + "/product/SendInventory/page.do";
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
            {name: 'styleName', label: '款名', editable:true,width: 100},
            {name: 'colorId', label: '颜色', editable:true,width: 100},
            {name: 'sizeId', label: '尺寸', editable:true,width: 100},
            {name: 'qty', label: '数量', editable:true,width: 100}
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
        url: basePath + "/product/SendInventory/WxShopStocke.do",
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {

        }
    });
}



