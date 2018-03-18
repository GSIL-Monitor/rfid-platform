var searchUrl = basePath + "/sys/factory/page.do?filter_EQI_type=3";
var pageType;
$(function () {
    initGrid();

});

function refresh() {
    location.reload(true);
}


function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: searchUrl,
        mtype: "POST",
        datatype: "json",
        colModel: [

            {
                name: "src", label: "来源", width: 80, editable: false,
                formatter: function (cellValue, options, rowObject) {
                    var html = "";
                    switch (cellValue) {
                        case "01":
                            html = '<span class="label label-sm label-success">系统</span>';
                            break;
                        case "02":
                            html = '<span class="label label-sm label-inverse">同步</span>';
                            break;
                        case "03":
                            html = '<span class="label label-sm label-warning">导入</span>';
                            break;
                        default:
                            html = '<span class="label label-sm label-inverse">系统</span>';
                    }
                    return html;
                }
            },
            {name: 'id', label: 'id', hidden: true, width: 40},
            {
                name: 'locked', label: '状态', editable: true, width: 100,align:"center",
                formatter: function (cellValue, options, rowObject) {
                    var html = '';
                    if (cellValue == 1) {
                        html += '<i class="fa fa-times red" title="锁定"></i>';
                    } else {
                        html += '<i class="fa fa-check green" title="未锁定"></i>';
                    }
                    return html;
                }
            },
            {name: 'code', label: '编号', editable: true, width: 100},
            {name: 'name', label: '名称', editable: true, width: 200},
            {name: 'ownerId', label: '所属方', editable: true, width: 100},
            {name: 'unitName', label: '所属方名称', editable: true, width: 200},
            {name: 'creatorId', label: '创建人', editable: true,width: 200},
            {name: 'tel', label: '联系电话', editable: true, width: 200},
            {name: 'linkman', label: '联系人', editable: true, width: 100},
            {name: 'email', label: '邮箱', editable: true, width: 200},
            {name: 'createTime', label: '创建时间', editable: true, width: 200},
            {name: 'remark', label: '备注', editable: true, width: 400}
        ],
        viewrecords: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        sortname: 'createTime',
        sortorder: "desc",
        shrinkToFit: false,
        autoScroll: false,
        autowidth: true
    });
}


function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");

}

function save() {
    $('#editForm').data('bootstrapValidator').validate();
    if (!$('#editForm').data('bootstrapValidator').isValid()) {
        return;
    }

    var progressDialog = bootbox.dialog({
        message: '<p><i class="fa fa-spin fa-spinner"></i> 数据上传中...</p>'
    });
    $.post(basePath + "/sys/factory/save.do",
        $("#editForm").serialize(),
        function (result) {
            if (result.success == true || result.success == 'true') {
                progressDialog.modal('hide');
                $("#edit-dialog").modal('hide');
                $("#grid").trigger("reloadGrid");
            }
        }, 'json');

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

function lock() {
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#grid").jqGrid('getRowData', rowId);
        $.ajax({
            type: "POST",
            url: basePath + "/sys/factory/lock.do?id=" + row.id ,
            dataType: "json",
            success: function (result) {

                $("#grid").trigger("reloadGrid");//刷新grid
            }
        });


    } else {
        bootbox.alert("请选择一项");
    }

}
function unlock() {
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#grid").jqGrid('getRowData', rowId);
        $.ajax({
            type: "POST",
            url: basePath + "/sys/factory/unlock.do?id=" + row.id ,
            dataType: "json",
            success: function (result) {

                $("#grid").trigger("reloadGrid");//刷新grid
            }
        });
    } else {
        bootbox.alert("请选择一项");
    }
}

function add() {
    $("#editForm").resetForm();
    $("#edit-dialog").modal('show');
    $("#form_code").removeAttr("readOnly");
    pageType="add";
}
function edit() {
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#grid").jqGrid('getRowData', rowId);
        $("#editForm").resetForm();
        $("#edit-dialog").modal('show');
        $("#form_code").attr("readOnly",true);
        pageType="edit";
        $("editForm").loadData(row);

    } else {
        bootbox.alert("请选择一项");
    }
}