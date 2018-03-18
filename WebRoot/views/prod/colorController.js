var searchUrl = basePath + "/prod/color/page.do";
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
        datatype: "json",
        mtype: "POST",
        colModel: [

            {name: 'id', label: 'id', hidden: true, width: 20},
            {name: 'colorId', label: '颜色编码', editable: true, width: 40},
            {
                name: 'colorName', label: '颜色', editable: true, width: 80,
                cellattr: function (rowId, val, rawObject, cm, rdata) {
                    return "style='background-color:" + rawObject.hex + "'";
                }
            },
            {name: 'hex', label: '色码', editable: true, hidden: true, width: 80},
            {name: 'oprId', label: '创建人', editable: true, width: 80},
            {
                name: '', label: '操作', editable: true, width: 40, align: 'center',
                formatter: function (cellValue, option, rowObject) {
                    var html;
                    if (rowObject.isUse == "Y") {
                        html = "<a href='#' onclick=changeColorStatus('" + rowObject.id + "','N')><i class='ace-icon fa fa-check' title='启用'></i></a>";
                    } else {
                        html = "<a href='#' onclick=changeColorStatus('" + rowObject.id + "','Y')><i class='ace-icon fa fa-lock' title='废除'></i></a>";
                    }
                    return html;
                }
            },
            {name: 'isUse', label: '启用状态', hidden: true},
            {name: 'updateTime', label: '更新时间', editable: true, width: 200},
        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: true,
        sortname: 'colorId',
        sortorder: "desc"

    });

}

function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}


function _search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        page: 1,
        url: searchUrl,
        postData: params
    });
    $("#grid").trigger("reloadGrid");
}

function closeEditDialog() {
    $("#edit_color_dialog").modal('hide');
}

function add() {
    $("#editColorForm").resetForm();
    $("#edit_color_dialog").modal('show');
    // $("#form_colorId").attr("disabled", true);
}
function edit() {
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#grid").jqGrid('getRowData', rowId);
        $("#edit_color_dialog").modal("show");
        $("#editColorForm").loadData(row);
        // $("#form_colorId").attr("disabled", true);

    } else {
        bootbox.alert("请选择一项进行修改！");
    }
}
function changeColorStatus(rowId, status) {
    $.ajax({
        url: basePath + '/prod/color/changeColorStatus.do',
        datatype: 'json',
        data: {
            colorId: rowId,
            status: status
        },
        success: function (result) {
            if (result.success) {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#grid").trigger('reloadGrid')
            } else {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-fail gritter-light'
                });
            }
        }

    });
}
	
	