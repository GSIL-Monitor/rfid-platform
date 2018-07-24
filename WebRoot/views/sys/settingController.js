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
function switchOn(id){
    var rowData=$("#grid").jqGrid('getRowData',id);
    var jobUrl=basePath;
    if(rowData.configState==0){
        jobUrl+="/timer/runSingleJob.do";
    }else{
        jobUrl+="/timer/shutdownSingleJob.do";
    }
    openProgress();
    $.ajax({
        type: "POST",
        url: jobUrl,
        data: {job:rowData.id},
        dataType: "json",
        success: function(data) {
            closeProgress();
            if(data.success) {
                $("#notification").data('kendoNotification').showText('成功！', 'success');
            }else{
                openWarmDialog('操作失败！'+data.msg);
                $("#notification").data('kendoNotification').showText('操作失败！'+data.msg, 'error');
            }
            $("#grid").jqGrid().trigger('reloadGrid');
        },
        error:function (e) {
            closeProgress();
            openWarmDialog("操作失败");
            $("#notification").data('kendoNotification').showText('操作失败！', 'error');
        }
    });
}
function setOperate(id,value) {
    console.info(id);
    console.info(value);
    const jobUrl = basePath+'/sys/sysSetting/setOperate.do';
    if($(".ace").hasClass("checked")){
        $(".ace").removeAttr("checked");
        cs.showProgressBar();
    }
    else {
        $(".ace").attr("checked");
        cs.showProgressBar();
    }
    $.ajax({
        type: "POST",
        url: jobUrl,
        data: {id:id,value:value},
        dataType: "json",
        success: function(data) {
            cs.closeProgressBar();
            location.href=basePath+"/sys/sysSetting/index.do"
            $.gritter.add({
                text: data.msg,
                class_name: 'gritter-success  gritter-light'
            });
        },
        error:function (e) {
            cs.closeProgressBar();
            location.href=basePath+"/sys/sysSetting/index.do"
            $.gritter.add({
                text: data.msg,
                class_name: 'gritter-fail gritter-light'
            });
        }
    });
}
