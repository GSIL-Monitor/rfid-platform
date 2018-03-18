var searchUrl = basePath + "/factory/token/page.do";

$(function () {
    initGrid();

});
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
            {name: 'token', label: 'token', hidden: true, width: 40},
            {name: 'sortIndex', label: '序号', hidden: true, width: 40},
            {
                name: 'locked', label: '状态', editable: true, width: 100,align:"center",
                formatter: function (cellValue, options, rowObject) {
                    var  html='';
                    if(cellValue==0){
                        html+='<i class="fa fa-check green" title="启用"></i>';
                    }else{
                        html+='<i class="fa fa-times red" title="废除"></i>';
                    }
                    return html;
                }
            },
            {name: 'name', label: '流程名', editable: true, width: 200},
            {
                name: 'types', label: '过程', editable: true, width: 200,
                formatter: function (cellValue, options, rowObject) {
                    var type = "";
                    var types = cellValue.split(",");
                    for (var i = 0; i < types.length; i++) {
                        switch (types[i]) {
                            case "I":
                                type += "开始,";
                                break;
                            case "O":
                                type += "结束,";
                                break;
                            case "P":
                                type += "暂停,";
                                break;
                            case "R":
                                type += "恢复,";
                                break;
                            default:
                                types+=",";
                                break;
                        }
                    }
                    return type.substr(0, type.length - 1);
                }
            },
            {
                name: 'necessary', label: '必须流程', editable: true, width: 200,
                formatter: function (cellValue, options, rowObject) {
                    if (cellValue == "Y") {
                        return "是";
                    } else {
                        return "否";
                    }
                }
            },
            {name: 'lastTokenName', label: '上一个必须流程', editable: true, width: 200},
            {
                name: 'multiple', label: '多次扫描', sortable: false, editable: true, width: 200,
                formatter: function (cellValue, options, rowObject) {
                    if (cellValue == "Y") {
                        return "允许";
                    } else {
                        return "不允许";
                    }
                }
            },
        ],
        viewrecords: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        sortname: 'sortIndex',
        sortorder: "asc",
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
    oldTokenName="";
    $("#edit-dialog").modal('show');


}

function edit() {
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#grid").jqGrid('getRowData', rowId);
        oldTokenName=row.name;
        $("#editForm").resetForm();
        $("#edit-dialog").modal('show');
        $.ajax({
            type: "POST",
            url: basePath + "/factory/token/findTokenById.do?token=" + row.token,
            dataType: "json",
            success: function (msg) {
                var token = msg.result;
                $.each(token.types.split(","),function (index,value) {
                    $("#form_types").multiselect('select', value);
                });
                $("#form_token").val(token.token);
                $("#form_name").val(token.name);
                $("#form_lastToken").val(token.lastToken);
                $("#form_necessary").val(token.necessary);
                $("#form_multiple").val(token.multiple);
                $("#form_isFirst").val(token.isFirst);
                $("#form_isLast").val(token.isLast);
            }
        });

    } else {
        bootbox.alert("请选择一项");
    }
}
function up() {
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#grid").jqGrid('getRowData', rowId);
        var token = row.token;
        var lastToken = $("#grid").jqGrid('getRowData', rowId-1).token;
        if (lastToken != null || lastToken !=undefined ) {
            $.ajax({
                type: "POST",
                url: basePath + "/factory/token/exchangeSortIndex.do?firstToken=" + token + "&secondToken=" + lastToken,
                dataType: "json",
                success: function (result) {

                    $("#grid").trigger("reloadGrid");//刷新grid
                }
            });
        }

    } else {
        bootbox.alert("请选择一项");
    }
}
function down() {
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#grid").jqGrid('getRowData', rowId);
        var token = row.token;
        rowId++;
        var nextToken = $("#grid").jqGrid('getRowData', rowId).token;
        if (nextToken != null || nextToken !=undefined ) {
            $.ajax({
                type: "POST",
                url: basePath + "/factory/token/exchangeSortIndex.do?firstToken=" + token + "&secondToken=" + nextToken,
                dataType: "json",
                success: function (result) {

                    $("#grid").trigger("reloadGrid");//刷新grid
                }
            });
        }

    } else {
        bootbox.alert("请选择一项");
    }
}
function startUsing() {
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#grid").jqGrid('getRowData', rowId);
        $.ajax({
            url: basePath + "/factory/token/startUsing.do?token=" + row.token,
            type: "POST",
            success: function (data, textStatus) {
                $("#grid").trigger("reloadGrid");
            }
        })
    } else {
        bootbox.alert("请选择一项");
    }

}
function stopUsing() {
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#grid").jqGrid('getRowData', rowId);
        $.ajax({
            url: basePath + "/factory/token/stopUsing.do?token=" + row.token,
            type: "POST",
            success: function (data, textStatus) {
                $("#grid").trigger("reloadGrid");
            }
        })
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
    $.post(basePath + "/factory/token/save.do",
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
function refresh() {
    location.reload(true);
}