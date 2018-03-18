$(function(){
    initGrid();
    var type_parent_column = $(".widget-header").width();
    $("#token_grid").jqGrid( 'setGridWidth', type_parent_column );
});
function initGrid(){
    $("#token_grid").jqGrid({
        height:  "auto",
        url: basePath + "/sys/sysSetting/page.do?filter_LIKES_name=TOKEN",
        datatype: "json",
        mtype:"POST",
        colModel: [

            {name: 'id', label: 'id',hidden:true, width: 40},
            {name: 'name', label: '名称', editable:true,width: 40},
            {name: 'value', label: '值', editable:true,width: 40},

        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#token_grid-pager",
        multiselect: false,
        shrinkToFit: true,
        sortname : 'id',
        sortorder : "asc"
    });


}

function refreshCache() {
    cs.showProgressBar("刷新缓存中...");
    $.post(basePath+"/sys/sysSetting/refreshCache.do",
        {},
        function(result) {
            if(result.success == true || result.success == 'true') {
                cs.closeProgressBar();
            }
        }, 'json');
}