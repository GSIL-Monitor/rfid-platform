var searchUrl = basePath + "/factory/birth/page.do";
$(function () {
    initGrid();
});

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
            {name: 'billNo', label: '批次号', editable: true, width: 200},
            {name: 'billDate', label: '导入时间', editable: true, width: 200},
            {name: 'factoryBillNo', label: '办单单号', editable: true, width: 200},
            {name: 'status', label: '状态', editable: true, width: 100,align:"center",
                formatter: function (cellValue, options, rowObject) {
                    var  html='';
                    if(cellValue==1){
                        html+='<i class="fa fa-check green" title="已确认"></i>';
                    }else if(cellValue==0){
                        html+='<i class="fa fa-times red" title="未确认"></i>';
                    }else if(cellValue==2){
                        html+='<i class="fa fa-print blue" title="已打印"></i>';
                    }
                    return html;
                }},
            {name: 'fileName', label: '导入文件名', editable: true, width: 350},
            {name: 'totSku', label: 'SKU数', editable: true, width: 150},
            {name: 'totQty', label: '总数量', editable: true, width: 180},
            {
                name: '', label: '操作', editable: true, width: 150,
                formatter: function (cellValue, options, rowObject) {
                    var billNoT = rowObject.billNo;
                    var factoryBillNoT = rowObject.factoryBillNo;
                    var statusT=rowObject.status;
                    var html;
                    html = "<a href='" + basePath + "/factory/birth/detail.do?billNo=" + billNoT + "'><i class='ace-icon fa fa-list' title='查询明细'></i></a>";
                    html += "<a style='margin-left: 20px' href='#' onclick=deleteBill('"+billNoT+"','"+factoryBillNoT+"')><i class='ace-icon fa fa-trash-o red' title='删除办单'></i></a>";
                    html += "<a style='margin-left: 20px' href='#' onclick=confirm('"+billNoT+"','"+factoryBillNoT+"','"+statusT+"')><i class='ace-icon fa fa-check-square-o' title='确认单据'></i></a>";
                    html += "<a style='margin-left: 20px' href='" + basePath + "/factory/birth/downloadFile.do?billNo=" + billNoT +"&deviceId=KF201301&factoryBillNo="+factoryBillNoT+ "' ><i class='ace-icon fa fa-file-excel-o' title='下载打印文件'></i></a>";
                    return html;
                }
            }
        ],
        viewrecords: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        sortname: 'billDate',
        sortorder: "desc",
        shrinkToFit: false,
        autoScroll: false,
        autowidth: true
    });
}

function uploadStyle() {

    $("#modal-tableUpload").on("hidden.bs.modal", function () {
        $("#uploadForm").resetForm();
    }).modal("show");

}

function initUploadSet() {
    $(".table-header").append("上传条码信息");
    var file = $('#upload_file_input');
    $('#upload_file_input').fileinput({
        language: 'zh',
        uploadUrl: basePath + "/factory/birth/importExcel.do",
        allowedPreviewTypes: ['image', 'html', 'text', 'video', 'audio', 'flash'],
        allowedFileExtensions: ['xls']

    }).on("fileuploaded", function (event, result) {
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

        $("#grid").trigger("reloadGrid");
    });
    $("#upload_img_info").attr("src", basePath + "/images/uploadSample/tag_sku.jpg");
}

function deleteBill(billNo,factoryBillNo) {
    $.ajax({
        dataType: "json",
        type: "POST",
        url:basePath+"/factory/birth/delete.do",
        data:{"billNo":billNo,"factoryBillNo":factoryBillNo},
        success:function (result) {
            if (result.success == true || result.success == 'true') {
                $("#grid").trigger("reloadGrid");
            }else{
                bootbox.alert(result.msg);
            }
        }

    })
}
function confirm(billNo,factoryBillNo,status) {
    if (status==0){
        $.ajax({
            dataType: "json",
            type: "POST",
            url:basePath+"/factory/birth/confirm.do",
            data:{"billNo":billNo,"factoryBillNo":factoryBillNo,status:1},
            success:function (result) {
                if (result.success == true || result.success == 'true') {
                    $("#grid").trigger("reloadGrid");
                }else{
                    bootbox.alert("确认失败"+result.result);
                }
            }

        })
    }else if(status==1){
        bootbox.alert("已确认");
    }else if(status==2){
        bootbox.alert("已打印");
    }

}
