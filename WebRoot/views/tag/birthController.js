$(function () {
    //初始化
    initGrid();

});
function refresh() {
    location.reload(true);
}
function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: basePath + "/tag/birth/page.do",
        datatype: "json",
        colModel: [
            {name: 'billNo', label: '批次号', editable: true, width: 200, frozen: true},
            {
                name: "", label: "查看明细", width: 80, editable: false, align: "center",
                formatter: function (cellValue, options, rowObject) {
                    var billNo = rowObject.billNo;
                    return "<a href='" + basePath + "/tag/birth/detail.do?billNo=" + billNo + "'><i class='ace-icon fa fa-list'></i></a>";
                }
            },
            {name: 'id', label: 'id', hidden: true, width: 40},
            {
                name: 'status', label: '状态', editable: true, width: 80, align: "center",
                formatter: function (cellValue, option, rowObject) {
                    var html = '';
                    if (cellValue == 1) {
                        html += '<i class="fa fa-check green" title="已确认"></i>';
                    } else if (cellValue == 0) {
                        html += '<i class="fa fa-times red" title="未确认"></i>';
                    } else if (cellValue == 2) {
                        html += '<i class="fa fa-print blue" title="已打印"></i>';
                    }else if (cellValue == -1){
                        html += '<i class="fa fa-print red" title="打印中"></i>';
                    }
                    return html;
                }
            },

            {name: 'billDate', label: '导入时间', editable: true, width: 200},
            {name: 'fileName', label: '导入文件名', editable: true, width: 150},
            {name: 'importType', label: '导入类型', editable: true, width: 100},
            {name: 'totSku', label: 'SKU数', editable: true, width: 50},
            {name: 'totEpc', label: '总数量', editable: true, width: 50},
            {name: 'totPrintQty', label: '已打印数量', editable: true, width: 50},
            {name: '', label: '打印Rfid标签', editable: true, width: 50,
                formatter: function (cellValue, option, rowObject) {
                  var html ="<a href='javascript:void(0);' onclick=printRfidTag('" + rowObject.billNo +"')> <i class='fa fa-print blue' title='打印RFID标签'></i></a>";
                  return html;
                }
            },
            {name: '', label: '打印洗水唛', editable: true, width: 50,
                formatter: function (cellValue, option, rowObject) {
                    var html ="<a href='javascript:void(0);' onclick=printLabelTag('" + rowObject.billNo +"')><i class='fa fa-print blue'title='打印洗水唛标签'></i></a>";
                    return html;
                }
            },
        ],
        viewrecords: true,
        autowidth: false,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: true,
        sortname: 'billDate',
        sortorder: "desc",
        autoScroll: false
    });
    $("#grid").jqGrid("setFrozenColumns");
    var a_parent_column = $("#grid").closest('.col-xs-12');
    $("#grid").jqGrid('setGridWidth', a_parent_column.width() - 20);


}
function printRfidTag(billNo){
    console.log(billNo+"rfid");
    downloadPrintInfo(billNo,"rfid");
}
function printLabelTag(billNo){
    console.log(billNo+"label");
    downloadPrintInfo(billNo,"label");
}
//打印标签
function downloadPrintInfo(billNo,outPutFile){
    window.location.href = basePath + "/tag/birth/printByBillNo.do?billNo=" + billNo+"&dtlListStr=&epcListStr="
                                    + "&outFileName="+outPutFile+"&isAll=true";
}
function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}

function _search() {
    var searchUrl = basePath + "/tag/birth/page.do";
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        url: searchUrl,
        page: 1,
        postData: params
    });
    $("#grid").trigger("reloadGrid");
}

function initUploadSet() {
    $(".table-header").append("上传条码信息");
    var file = $('#upload_file_input');
    $('#upload_file_input').fileinput({
        language: 'zh',
        uploadUrl: basePath + "/tag/birth/importExcel.do",
        allowedPreviewTypes: ['image', 'html', 'text', 'video', 'audio', 'flash'],
        allowedFileExtensions: ['xls']

    }).on("fileuploaded", function (event, result) {
        cs.closeProgressBar();
        if (result.response.success) {
            $.gritter.add({
                text: result.response.msg,
                class_name: 'gritter-success  gritter-light'
            });

        } else {
            $.gritter.add({
                text: result.response.msg + "!" + result.response.result,
                class_name: 'gritter-false  gritter-light'
            });
        }

    });

    $('#upload_file_input').on('fileuploaderror', function (event, data, previewId, index) {
        var form = data.form, files = data.files, extra = data.extra,
            response = data.response, reader = data.reader;
        console.log(data);
        console.log('File upload error');
    });

    $('#upload_file_input').on('fileerror', function (event, data) {
        console.log(data.id);
        console.log(data.index);
        console.log(data.file);
        console.log(data.reader);
        console.log(data.files);
    });

    $('#upload_file_input').on('fileuploaded', function (event, data, previewId, index) {
        var form = data.form, files = data.files, extra = data.extra,
            response = data.response, reader = data.reader;
        console.log('File uploaded triggered');
    });
    $("#upload_img_info").attr("src", basePath + "/images/uploadSample/tag_sku.jpg");
    $("div#searchUploadPanel a.fileinput-upload-button").bind("click", function () {
        console.log("上传前触发事件");
        $("#modal-tableUpload").modal('hide');
        cs.showProgressBar("文件解析上传中...");
    });
}

function initUploadExcelSet() {
    $("#modal-excelUpload .table-header").append("上传唯一码信息");
    var file = $('#upload_file_input');
    $('#upload_excel_input').fileinput({
        language: 'zh',
        uploadUrl: basePath + "/tag/birth/importExcelCode.do",
        allowedPreviewTypes: ['image', 'html', 'text', 'video', 'audio', 'flash'],
        allowedFileExtensions: ['xls', 'xlsx']

    }).on("fileuploaded", function (event, result) {
        cs.closeProgressBar();
        if (result.response.success) {
            $.gritter.add({
                text: result.response.msg,
                class_name: 'gritter-success  gritter-light'
            });
            $("#modal-excelUpload").modal('hide');
        } else {
            $.gritter.add({
                text: result.response.msg + "!" + result.response.result,
                class_name: 'gritter-false  gritter-light'
            });
        }

    });

    $('#upload_excel_input').on('fileuploaderror', function (event, data, previewId, index) {
        var form = data.form, files = data.files, extra = data.extra,
            response = data.response, reader = data.reader;
        console.log(data);
        console.log('File upload error');
    });

    $('#upload_excel_input').on('fileerror', function (event, data) {
        console.log(data.id);
        console.log(data.index);
        console.log(data.file);
        console.log(data.reader);
        console.log(data.files);
    });

    $('#upload_excel_input').on('fileuploaded', function (event, data, previewId, index) {
        var form = data.form, files = data.files, extra = data.extra,
            response = data.response, reader = data.reader;
        console.log('File uploaded triggered');
    });
    $("#upload_img_tip").attr("src", basePath + "/images/uploadSample/tag_code.jpg");
    $("div#searchExcelPanel a.fileinput-upload-button").bind("click", function () {
        console.log("上传前触发事件");
        $("#modal-excelUpload").modal('hide');
        cs.showProgressBar("文件解析上传中...");
    });
}

function uploadStyle() {

    $("#modal-tableUpload").on("hidden.bs.modal", function () {
        $("#uploadForm").resetForm();
    }).modal("show");

}

function uploadCode() {
    $("#modal-excelUpload").modal("show");
}

function exportFile() {
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#grid").jqGrid('getRowData', rowId);
        window.location.href = basePath + "/tag/birth/exportFile.do?billNo=" + row.billNo;
    } else {
        bootbox.alert("请选择一项进行确认！");
    }
}

function confirm() {
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#grid").jqGrid('getRowData', rowId);
        if (row.status == "该项已确认") {
            bootbox.alert("已确认！");
        } else {
            var url = basePath + "/tag/birth/updateStatus.do";
            $.post(url, "billNo=" + row.billNo, function (result) {
                bootbox.alert(result.msg);
                _search();
            });

        }

    } else {
        bootbox.alert("请选择一项进行确认！");
    }
}
function deleteFirm() {
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#grid").jqGrid('getRowData', rowId);
        if (row.status == "已确认") {
            bootbox.alert("已确认 的单据不能删除！");

        } else {
            var url = basePath + "/tag/birth/delete.do";
            $.post(url, "billNo=" + row.billNo, function (result) {
                bootbox.alert(result.msg);
                _search();
            });

        }

    } else {
        bootbox.alert("请选择一项进行删除！");
    }
}

function scanCode() {
    if ($("#search_scanCode") === null || $("#search_scanCode") === "" || $("#search_scanCode") === undefined) {
        return;
    }
    var uniqueCode = $("#search_scanCode").val();
    var billNo;
    $.ajax({
        dataType: "json",
        async: false,
        url: basePath + "/tag/birth/scanCode.do",
        data: {uniqueCode: uniqueCode},
        type: "POST",
        success: function (result) {
            if (result.success) {
                billNo = result.result;
            } else {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#search_scanCode").val("");
            }
        }
    });
    if (billNo && billNo !== null) {
        location.href = basePath + "/tag/birth/detail.do?billNo=" + billNo;
    }
}