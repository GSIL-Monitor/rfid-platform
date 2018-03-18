var searchUrl=basePath + "/sys/sysLog/page.do";
$(function () {
    initGrid();

    initSearchType();
    var type_parent_column = $(".widget-header").width();
    var property_parent_column = $('.widget-header').width();
    $("#server_grid").jqGrid( 'setGridWidth', type_parent_column );
    $("#table_grid").jqGrid( 'setGridWidth', property_parent_column );
    $(window).on('resize.jqGrid', function () {
        var type_parent_column = $(".widget-header").width();
        var property_parent_column = $('.widget-header').width();
        $("#server_grid").jqGrid( 'setGridWidth', type_parent_column );
        $("#table_grid").jqGrid( 'setGridWidth', property_parent_column );
    });
});

function initSearchType() {
}

function initGrid() {
    $("#server_grid").jqGrid({
        height: "auto",
        url: basePath + "/sys/sysLog/page.do",
        datatype: "json",
        colModel: [
            {name: 'id', label: 'id',hidden:true, width: 40},
            {name: 'type', label: '类型', width: 40},
            {name: 'method', label: '方法',editable:true, width: 300},
            {name: 'consumeTime', label: '耗时(ms)', editable:true,width: 200},
            {name: 'creatorId', label: '访问人', editable:true,width: 200},
            {name: 'createTime', label: '访问时间',editable:true, width: 200},
            {name:'message',label:'消息',editable:true,width:200}
        ],
        viewrecords: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#server_grid-pager",
        multiselect: false,
        sortname : 'createTime',
        sortorder : "desc",
        shrinkToFit:false,
        autoScroll:false,
        autowidth: true
    });

    $("#table_grid").jqGrid({
        height: "auto",
        url: basePath + "/sys/sysTableLog/page.do",
        datatype: "json",
        colModel: [
            {name: 'id', label: 'id',hidden:true, width: 40},
            {name: 'tableName', label: '表名称',editable:true, width: 300},
            {name: 'version', label: '版本号', editable:true,width: 200},
            {name: 'consumeTime', label: '耗时(ms)', editable:true,width: 200},
            {name: 'creatorId', label: '访问人', editable:true,width: 200},
            {name: 'logDate', label: '访问时间',editable:true, width: 200}
        ],
        viewrecords: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#table_grid-pager",
        multiselect: false,
        sortname : 'logDate',
        sortorder : "desc",
        shrinkToFit:false,
        autoScroll:false,
        autowidth: true
    });

}



function _server_search() {
    var serializeArray = $("#server_searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#server_grid").jqGrid('setGridParam', {
        url:searchUrl,
        page : 1,
        postData : params
    });
  $("#server_grid").trigger("reloadGrid");
}

function _table_search() {
    var serializeArray = $("#table_searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#table_grid").jqGrid('setGridParam', {
        url:basePath + "/sys/sysTableLog/page.do",
        page : 1,
        postData : params
    });
    $("#table_grid").trigger("reloadGrid");
}



	