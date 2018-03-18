$(function () {
    initGrid();   
    //connectWebSocketServer(socketPath);

});

function refresh() {
    location.reload(true);
}


function initGrid() {

    $("#grid").jqGrid({
        height: 500,
        url: basePath + "/data/msgPush/page.do",
        datatype: "json",
        colModel: [
            {name: 'id', label: 'id', hidden: true, width: 40},
            {name: 'sendDate', label: '发送时间', width: 200},
            {name: 'msgType', label: '消息类型', sortable: false, width: 200},
            {name: 'fromCode', label: '发送者', editable: true, width: 200},
            {name: 'content', label: '内容', sortable: false, width: 700},
            {name: 'acceptType', label: '接收类型', sortable: false, width: 200},
            {name: 'unitCode', label: '组织编码', sortable: false, width: 200}
        ],
        viewrecords: true,
        autowidth: false,
        rownumbers: true,  
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: false,
        sortname: 'sendDate',
        sortorder: "asc",
        autoScroll: false
    });
}

function _search() { 
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        page: 1,
        postData: params
    });
    $("#grid").trigger("reloadGrid");
}

function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");

}

function add() {
    $("#editForm").resetForm();
    $("#form_code").attr("readOnly", false);
    $("#edit-dialog").modal('show');
}
function edit() {
    $("#form_code").attr("readOnly", true);
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#grid").jqGrid('getRowData', rowId);
        $("#edit-dialog").modal("show");
        $("#form_code").val(row.code);
        $("#form_ownerId").val(row.ownerId);
        $("#form_ownerName").val(row.unitName);
        /*        $("#form_storageId").empty();
         $("#form_storageId").append("<option value='"+row.storageId+"'>"+row.storageName+"</option>");*/
        $("#form_storageId").val(row.storageId);
        $("#form_storageName").val(row.storageName);
        $("#form_remark").val(row.remark);
    } else {
        bootbox.alert("请选择一项进行修改！");
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
    var formData = $("#editForm").serialize();
    $.ajax({
        type: "POST",
        url: basePath + "/data/device/save.do",
        data: formData,
        dataType: "json",
        success: function (result) {
            if (result.success == true || result.success == 'true') {
                progressDialog.modal('hide');
                $("#edit-dialog").modal('hide');
                $("#grid").trigger("reloadGrid");
            } else {
                progressDialog.modal('hide');
                $("#edit-dialog").modal('hide');
                bootbox.alert(result.msg);
            }
        }
    })
}
function closeEditDialog() {
    $("#edit-dialog").modal('hide');
}

function editConfig() {
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#grid").jqGrid('getRowData', rowId);
        location.href = basePath + "/data/device/editConfigPage.do?deviceId=" + row.code;

    } else {
        bootbox.alert("请选择一项进行修改！");
    }

}

function toConsole() {
    location.href=basePath + "/data/msgPush/toConsole.do"
}