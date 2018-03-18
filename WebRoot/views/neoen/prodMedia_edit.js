/**
 * I 图片 V视频 A音频
 */
var searchUrl = basePath + "/neoen/prodMedia/page.do";
var styleId;
$(function () {

});


function initGridIV(url) {
    $("#gridIV").jqGrid({
        height: "auto",
        url: url,
        mtype: 'POST',
        datatype: "json",
        colModel: [
            {name: 'id', label: 'id', hidden: true, width: 100},
            {
                name: '', label: '操作', editable: true, width: 60,
                formatter: function (cellValue, options, rowObject) {
                    var id=rowObject.id;
                    var html="<a href='"+basePath+"/neoen/prodMedia/toMediaEdit.do?id="+id+"'><i class='ace-icon fa fa-list'></i></a>";
                    return html;
                }
            },
            {
                name: 'type', label: '类型', editable: true, width: 100,
                formatter: function (cellValue, options, rowObject) {
                    switch (cellValue) {
                        case "I":
                            return "图片";
                            break;
                        case "V":
                            return "视频";
                            break;
                        case "A":
                            return "声音";
                            break;
                    }
                }
            },
            {name: 'name', label: '媒体描述', hidden: true, width: 200},
            {
                name: 'isShow', label: '是否展示', editable: true, width: 100,
                formatter: function (cellValue, options, rowObject) {
                    if (cellValue == "Y") {
                        return "是";
                    } else {
                        return "否";
                    }
                }
            },
            {name: 'seqNo', label: '序号', editable: true, width: 100},
            {name: 'updateTime', label: '更新时间(s)', hidden: true, width: 150},
            {name: 'duration', label: '播放时长(s)', editable: true, width: 150},
            {name: 'remark', label: '备注', hidden: true, width: 200},
            {name: 'url', label: 'URL', editable: true, width: 500},
        ],

        viewrecords: true,
        autowidth: false,
        rownumbers: true,
        altRows: true,
        multiselect: false,
        shrinkToFit: false

    });
}

function initGridA(url) {
    $("#gridA").jqGrid({
        height: "auto",
        url: url,
        datatype: "json",
        colModel: [
            {name: 'id', label: 'id', hidden: true, width: 100},
            {
                name: '', label: '操作', editable: true, width: 60,
                formatter: function (cellValue, options, rowObject) {
                    var id=rowObject.id;
                    var html="<a href='"+basePath+"/neoen/prodMedia/toMediaEdit.do?id="+id+"'><i class='ace-icon fa fa-list'></i></a>";
                    return html;
                }
            },
            {
                name: 'type', label: '类型', editable: true, width: 100,
                formatter: function (cellValue, options, rowObject) {
                    switch (cellValue) {
                        case "I":
                            return "图片";
                            break;
                        case "V":
                            return "视频";
                            break;
                        case "A":
                            return "声音";
                            break;
                    }
                }
            },
            {name: 'name', label: '媒体描述', editable: true, width: 200},
            {
                name: 'isShow', label: '是否展示', editable: true, width: 100,
                formatter: function (cellValue, options, rowObject) {
                    if (cellValue == "Y") {
                        return "是";
                    } else {
                        return "否";
                    }
                }
            },
            {name: 'seqNo', label: '序号', editable: true, width: 100},
            {name: 'updateTime', label: '更新时间(s)', hidden: true, width: 150},
            {name: 'startTime', label: '开始时间(s)', editable: true, width: 150},
            {name: 'remark', label: '备注', editable: true, width: 200},
            {name: 'url', label: 'URL', editable: true, width: 500},
        ],

        viewrecords: true,
        autowidth: false,
        rownumbers: true,
        altRows: true,
        rowNum: -1,
        multiselect: false,
        shrinkToFit: false

    });
}
function historyBack() {
    location.href = basePath + "/neoen/prodMedia/index.do";
}
function saveDesc() {
    if($("#form_styleId").val()==""||$("#form_styleId").val()==null){
        bootbox.alert("请选择款式");
        return;
    }

    $.post(basePath + "/neoen/prodMedia/saveDesc.do",
        $("#editForm").serialize(),
        function (result) {
            if (result.success == true || result.success == 'true') {
                var styleId = $("#form_styleId").val();
                initGridIV(basePath + "/neoen/prodMedia/findMediaIV.do?styleId=" + styleId);
                initGridA(basePath + "/neoen/prodMedia/findMediaA.do?styleId=" + styleId);

                $("#form_AstyleId").val(styleId);
                $("#form_IVstyleId").val(styleId);

                var a_parent_column = $("#gridA").closest('.widget-body');
                var iv_parent_column = $("#gridIV").closest('.widget-body');
                $("#gridA").jqGrid('setGridWidth', a_parent_column.width() - 20);
                $("#gridIV").jqGrid('setGridWidth', iv_parent_column.width() - 20);

                bootbox.alert("保存成功");
            } else {
                bootbox.alert("保存失败");
            }
        }, 'json');

}

function IVup() {
    var rowId = $("#gridIV").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#gridIV").jqGrid('getRowData', rowId);
        var id=row.id;
        //var nextId = $("#gridIV #" + id).next()[0].id;//获取选择数据下一行的ID
        var last=$("#gridIV #"+id).prev();
        if(last!=null){
            var lastId = last[0].id;
           if(lastId!=""||lastId!=null){
               $.ajax({
                   type: "POST",
                   url: basePath + "/neoen/prodMedia/exchangeSeqNo.do?firstId="+id+"&secondId="+lastId,
                   dataType: "json",
                   success: function (result) {

                       $("#gridIV").trigger("reloadGrid");//刷新grid
                   }
               });
           }

        }

    } else {
        bootbox.alert("请选择一项");
    }
}
function IVdown() {
    var rowId = $("#gridIV").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#gridIV").jqGrid('getRowData', rowId);
        var id=row.id;
        var next = $("#gridIV #" + id).next();//获取选择数据下一行的ID
        //var last=$("#gridIV #"+id).prev();
        if(next!=null){
            var nextId = next[0].id;
            if(nextId!=""||nextId!=null) {
                $.ajax({
                    type: "POST",
                    url: basePath + "/neoen/prodMedia/exchangeSeqNo.do?firstId=" + id + "&secondId=" + nextId,
                    dataType: "json",
                    success: function (result) {

                        $("#gridIV").trigger("reloadGrid");//刷新grid
                    }
                });
            }
        }

    } else {
        bootbox.alert("请选择一项");
    }
}
function showADialog() {
    if($("#form_styleId").val()==""||$("#form_styleId").val()==null){
        bootbox.alert("请选择款式并保存");
        return;
    }
    $(".remove").click();
    $("#modal_prodMediaA").modal("show");
    $("#form_Aname").val("");
    $("#form_AstartTime").val("");
    $("#form_Aremark").val("");
    $("#form_AId").val("");
}
function showIVDialog() {
    if($("#form_styleId").val()==""||$("#form_styleId").val()==null){
        bootbox.alert("请选择款式并保存");
        return;
    }
    $(".remove").click();
    console.log($("#form_styleId").val());

    $('#upload_file_input').on('filepreajax', function(event, previewId, index) {
        styleId = {"styleId": $("#form_styleId").val()};
    });
    $("#modal-tableUpload").on("hidden.bs.modal", function() {
        $("#uploadForm").resetForm();
        $("#gridIV").trigger("reloadGrid");//刷新grid

    }).modal("show");
}

function initUploadSet(){
    $(".table-header").append("图片/视频上传");
    var file = $('#upload_file_input');
    $('#upload_file_input').fileinput({
        language: 'zh',
        uploadUrl: basePath+"/neoen/prodMedia/uploadMedia.do",
        uploadExtraData: function(previewId, index) {
            return styleId;
        },
        allowedPreviewTypes : ['image', 'video'],
        allowedFileExtensions:["jpg","jpeg","png","mp4"]

    }).on("fileuploaded", function(event, result) {
        if(result.response.success){
            $.gritter.add({
                text: result.response.msg,
                class_name: 'gritter-success  gritter-light'
            });

        }else{
            $.gritter.add({
                text: result.response.msg+"!"+result.response.result,
                class_name: 'gritter-false  gritter-light'
            });
        }

    });

    $('#upload_file_input').on('fileuploaderror', function(event, data, previewId, index) {
        var form = data.form, files = data.files, extra = data.extra,
            response = data.response, reader = data.reader;
        console.log(data);
        console.log('File upload error');
    });

    $('#upload_file_input').on('fileerror', function(event, data) {
        console.log(data.id);
        console.log(data.index);
        console.log(data.file);
        console.log(data.reader);
        console.log(data.files);
    });

    $('#upload_file_input').on('fileuploaded', function(event, data, previewId, index) {
        var form = data.form, files = data.files, extra = data.extra,
            response = data.response, reader = data.reader;
        console.log('File uploaded triggered');
    });
}

function showChangeDuration(){
    $("#modal_duration").modal("show");
}
function Aup() {
    var rowId = $("#gridA").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#gridA").jqGrid('getRowData', rowId);
        var id=row.id;
        //var nextId = $("#gridIV #" + id).next()[0].id;//获取选择数据下一行的ID
        var last=$("#gridA #"+id).prev();
        if(last!=null){
            var lastId = last[0].id;
            if(lastId!=""||lastId!=null) {
                $.ajax({
                    type: "POST",
                    url: basePath + "/neoen/prodMedia/exchangeSeqNo.do?firstId=" + id + "&secondId=" + lastId,
                    dataType: "json",
                    success: function (result) {

                        $("#gridA").trigger("reloadGrid");//刷新grid
                    }
                });
            }
        }

    } else {
        bootbox.alert("请选择一项");
    }
}
function Adown() {
    var rowId = $("#gridA").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#gridA").jqGrid('getRowData', rowId);
        var id=row.id;
        var next = $("#gridA #" + id).next();//获取选择数据下一行的ID
        //var last=$("#gridIV #"+id).prev();
        if(next!=null){
            var nextId = next[0].id;
            if(nextId!=""||nextId!=null) {
                $.ajax({
                    type: "POST",
                    url: basePath + "/neoen/prodMedia/exchangeSeqNo.do?firstId=" + id + "&secondId=" + nextId,
                    dataType: "json",
                    success: function (result) {

                        $("#gridA").trigger("reloadGrid");//刷新grid
                    }
                });
            }
        }

    } else {
        bootbox.alert("请选择一项");
    }
}
function deleteIV() {
    var rowId = $("#gridIV").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#gridIV").jqGrid('getRowData', rowId);
        var progressDialog = bootbox.dialog({
            message: '<p><i class="fa fa-spin fa-spinner"></i> 文件删除中...</p>'
        });
        $.ajax({
            type: "POST",
            url: basePath + "/neoen/prodMedia/delete.do",
            data: {"id": row.id},
            dataType: "json",
            success: function (result) {
                $("#gridIV").trigger("reloadGrid");//刷新grid
                progressDialog.modal('hide');
            }
        });
    } else {
        bootbox.alert("请选择一项");
    }
}
function deleteA() {
    var rowId = $("#gridA").jqGrid("getGridParam", "selrow");
    if (rowId) {
        var row = $("#gridA").jqGrid('getRowData', rowId);
        var progressDialog = bootbox.dialog({
            message: '<p><i class="fa fa-spin fa-spinner"></i> 文件删除中...</p>'
        });
        $.ajax({
            type: "POST",
            url: basePath + "/neoen/prodMedia/delete.do",
            data: {"id": row.id},
            dataType: "json",
            success: function (result) {
                $("#gridA").trigger("reloadGrid");//刷新grid
                progressDialog.modal('hide');
            }
        });
    } else {
        bootbox.alert("请选择一项");
    }
}

function uploadMediaA() {
    var file = $("#AfileUpload").data('ace_input_files')[0];
    if (file == null) {
        bootbox.alert("请选择上传的文件");
        return;
    }
    var progressDialog = bootbox.dialog({
        message: '<p><i class="fa fa-spin fa-spinner"></i> 文件上传中...</p>'
    });
    var fd = new FormData();
    fd.append("file", file);
    fd.append("id", $("#form_AId").val());
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            if (xhr.status == 200) {
                progressDialog.modal('hide');
                $("#gridA").trigger("reloadGrid");//刷新grid
                $("#modal_prodMediaA").modal("hide");
                bootbox.alert("文件上传完成");
            } else {
                progressDialog.modal('hide');
                bootbox.alert("文件上传失败");
            }
        }
    };
    xhr.open("POST", basePath + "/neoen/prodMedia/uploadMediaA.do", true);
    xhr.send(fd);
}

function saveMediaA() {
    $('#AForm').data('bootstrapValidator').validate();
    if (!$('#AForm').data('bootstrapValidator').isValid()) {
        return;
    }
    var formData = $("#AForm").serialize();
    $.ajax({
        url: basePath + "/neoen/prodMedia/saveMediaA.do",
        type: 'post',
        data: formData,
        dataType: 'json',
        success: function (result) {
            if (result.success == true || result.success == 'true') {
                $("#form_AId").val(result.result);
                uploadMediaA();

            } else {
                bootbox.alert("保存失败");
            }
        }
    })
}
function uploadMediaIV() {
    var file = $("#IVfileUpload").data('ace_input_files')[0];
    if (file == null) {
        bootbox.alert("请选择上传的文件");
        return;
    }
    var progressDialog = bootbox.dialog({
        message: '<p><i class="fa fa-spin fa-spinner"></i> 文件上传中...</p>'
    });
    var fd = new FormData();
    fd.append("file", file);
    fd.append("id", $("#form_IVId").val());
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            if (xhr.status == 200) {
                progressDialog.modal('hide');
                bootbox.alert("文件上传完成");
                $("#modal_prodMediaIV").modal("hide");
                $("#gridIV").trigger("reloadGrid");//刷新grid
            } else {
                progressDialog.modal('hide');
                bootbox.alert("文件上传失败");
            }
        }
    };
    xhr.open("POST", basePath + "/neoen/prodMedia/uploadMedia.do", true);
    xhr.send(fd);
}
function saveMediaIV() {
    $('#IVForm').data('bootstrapValidator').validate();
    if (!$('#IVForm').data('bootstrapValidator').isValid()) {
        return;
    }
    var formData = $("#IVForm").serialize();
    $.ajax({
        url: basePath + "/neoen/prodMedia/saveMediaIV.do",
        type: 'post',
        data: formData,
        dataType: 'json',
        success: function (result) {
            if (result.success == true || result.success == 'true') {
                $("#form_IVId").val(result.result);
                uploadMediaIV();
            } else {
                bootbox.alert("保存失败");
            }
        }
    });
}

function findFab() {
    var progressDialog = bootbox.dialog({
        message: '<p><i class="fa fa-spin fa-spinner"></i> 文件上传中...</p>'
    });
    $.ajax({
        url: basePath + "/neoen/prodMedia/findFab.do?styleId="+$("#form_styleId").val(),
        type: 'post',
        dataType: 'json',
        success: function (result) {
            $("#form_fab").val(result.result);
            saveDesc();
            progressDialog.modal('hide');
        }
    });
}
