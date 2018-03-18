var searchUrl = basePath + "/factory/pauseReason/page.do";
$(function () {
    initGrid();
    initSearchSelect();
});
function initSearchSelect() {
    $("#search_token").empty();
    $.ajax({
        dataType: "json",
        url : basePath + "/factory/pauseReason/findToken.do",
        cache : false,
        async : false,
        type : "POST",
        success : function (data){
            var json= data.result;
            for(var i=0;i<json.length;i++){
                $("#search_token").append("<option value='"+json[i].token+"'>"+json[i].name+"</option>");

                $("#search_token").trigger('chosen:updated');
            }
        }
    });
}
function refresh() {
    location.reload(true);
}
function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}
function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: searchUrl,
        mtype: "POST",
        datatype: "json",
        colModel: [
            {name: 'id', label: 'id', hidden: true, width: 40},
            {name: 'token', label: '流程', hidden: true, width: 200},
            {name: 'tokenName', label: '流程', editable: true, width: 200},
            {name: 'reason', label: '暂停原因', editable: true, width: 400},
            {name: 'upDateTime', label: '更新时间', editable: true, width: 200}
        ],
        viewrecords: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        sortname: 'upDateTime',
        sortorder: "desc",
        shrinkToFit: false,
        autoScroll: false,
        autowidth: true
    });
}
function _search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        url: searchUrl,
        page: 1,
        postData: params
    });
    $("#grid").trigger("reloadGrid");
}

function _clearSearch() {
    $("#searchForm").resetForm();
}
function add() {
    $("#editForm").resetForm();
    $("#edit-dialog").modal('show');


}

function edit() {
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#grid").jqGrid('getRowData', rowId);
        $("#editForm").resetForm();
        $("#edit-dialog").modal('show');
        $("#form_id").val(row.id);
        $("#form_reason").val(row.reason);
        $("#form_token").val(row.token);

    } else {
        bootbox.alert("请选择一项");
    }
}


function save() {
    $('#editForm').data('bootstrapValidator').validate();
    if (!$('#editForm').data('bootstrapValidator').isValid()) {
        return;
    }
    var progressDialog = bootbox.dialog({
        message: '<p><i class="fa fa-spin fa-spinner"></i> 数据上传中...</p>'
    });
    $.post(basePath + "/factory/pauseReason/save.do",
        $("#editForm").serialize(),
        function (result) {
            if (result.success == true || result.success == 'true') {
                progressDialog.modal('hide');
                $("#edit-dialog").modal('hide');
                $("#grid").trigger("reloadGrid");
            } else {
                bootbox.alert("保存失败");
            }
        }, 'json');
}

function remove(){
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#grid").jqGrid('getRowData', rowId);
        $.ajax({
            type: "POST",
            url:basePath + "/factory/pauseReason/delete.do?id=" + row.id,
            success:function (result) {
                if (result.success == true || result.success == 'true') {
                    $("#grid").trigger("reloadGrid");
                }else{
                    bootbox.alert("删除失败");
                }
            }
        })
    } else {
        bootbox.alert("请选择一项");
    }
}