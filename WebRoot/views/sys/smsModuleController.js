var searchUrl = basePath + "/sys/SMSModule/page.do";
$(function(){
    //初始化
    initGrid();
    //initadd();
});
function refresh(){
    location.reload(true);
}
/*function initadd(){
    $.ajax({
        url : basePath + "/sys/role/list.do",
        cache : false,
        async : false,
        type : "POST",
        success : function (data,textStatus){
            var json= data;
            for(var i=0;i<json.length;i++){
                $("#form_roleId").append("<option value='"+json[i].code+"'>"+json[i].name+"</option>");
                $("#form_roleId").trigger('chosen:updated');
            }
        }
    })

}*/
function initGrid(){
    $("#grid").jqGrid({
        height: "auto",
        url:basePath + "/sys/SMSModule/page.do",
        datatype: "json",
        mtype:"POST",
        colModel: [
            {name: 'templateid', label: '模板ID', width: 200},
            {
                name: "", label: "操作", width: 55, editable: false, align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    var templateid = rowObject.templateid;
                    var html;
                    /*html = "<a href='" + basePath + "/sys/SMSModule/check.do?templateid=" + templateid + "'><i class='ace-icon fa fa-files-o' title='审核'></i></a>";*/
                    html = "<a style='margin-left: 20px' href='#' onclick=check('" + templateid + "')><i class='ace-icon fa fa-undo' title='审核'></i></a>";
                    return html;
                }
            },
            {name: 'title', label: '模板标题',width: 200},
            {name: 'content', label: '模板内容',width: 200},
            { label: '审核',sortable: false,width: 200,
                formatter: function (cellValue, options, rowObjec) {
                    if (rowObjec.approval == "isshow") {
                        return "审核通过";
                    } else if (rowObjec.approval == "noshow") {
                        return "审核没通过";
                    }
                }
            },
            {name:'saveTime',label:"时间",width:200}

        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: false,
        sortname : 'saveTime',
        sortorder : "desc",
        autoScroll:false

    });
    $("#grid").jqGrid("setFrozenColumns");
    function isAdmin(cellvalues,option,rowObject){
        if(cellvalues==1){
            return "是";
        }else{
            return "否";
        }
    }

}

function _search() {

    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);

    $("#grid").jqGrid('setGridParam', {
        url:searchUrl,
        page : 1,
        postData : params
    });
    $("#grid").trigger("reloadGrid");
}

function _clearSearch(){
    $("#searchForm").resetForm();
}

function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");

}
function add(){
    $("#editForm").resetForm();
    $("#form_id").val("");
    $("#form_templateid").val("");
    /*$("#form_code").attr("readOnly",false);
    $("#form_isAdmin").find("option[value='1']").removeAttr("selected");
    $("#form_isAdmin").find("option[value='0']").attr("selected",true);*/
    $("#edit-dialog").modal('show');
}
function edit(){
    $("#editForm").resetForm();
    $("#form_code").attr("readOnly",true);
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if(rowId) {
     /*   var row = $("#grid").jqGrid('getRowData',rowId);
        if(row.isAdmin=="是"){
            row.isAdmin="1";
        } else {
            row.isAdmin="0";
        }
        $("#edit-dialog").modal("show");
        $("#editForm").loadData(row);*/

    } else {
        bootbox.alert("请选择一项进行修改！");
    }
}

function closeEditDialog() {
    $("#edit-dialog").modal('hide');
}

function save(){
    /*$('#editForm').data('bootstrapValidator').validate();
    if(!$('#editForm').data('bootstrapValidator').isValid()){
        return ;
    }*/
    debugger;
    var progressDialog = bootbox.dialog({
        message: '<p><i class="fa fa-spin fa-spinner"></i> 数据上传中...</p>'
    });
    if($("#form_id").val()==""){
         $.post(basePath+"/sys/SMSModule/save.do",
            $("#editForm").serialize(),
            function(result) {
                debugger;
                if(result.success == true || result.success == 'true') {
                    progressDialog.modal('hide');
                    $("#edit-dialog").modal('hide');
                    $("#grid").trigger("reloadGrid");
                    $.gritter.add({
                        text: result.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                }else{
                    bootbox.alert(result.msg);
                }
            }, 'json');
    }else{
        $.post(basePath+"/sys/SMSModule/update.do",
            $("#editForm").serialize(),
            function(result) {
                debugger;
                if(result.success == true || result.success == 'true') {
                    progressDialog.modal('hide');
                    $("#edit-dialog").modal('hide');
                    $("#grid").trigger("reloadGrid");
                    $.gritter.add({
                        text: result.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                }else{
                    bootbox.alert(result.msg);
                }
            }, 'json');
    }

}

function check(templateid) {
    $.ajax({
        dataType: "json",
        async: false,
        url: basePath + "/sys/SMSModule/check.do",
        data: {templateid: templateid},
        type: "POST",
        success: function (msg) {
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#grid").trigger("reloadGrid");
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}

function edit() {
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if(rowId) {
        var row = $("#grid").jqGrid('getRowData',rowId);
       /* location.href = basePath+"/sys/SMSModule/editPage.do?templateid="+row.templateid;*/
        $.ajax({
            dataType: "json",
            async: false,
            url: basePath + "/sys/SMSModule/editPage.do",
            data: {templateid: row.templateid},
            type: "POST",
            success: function (msg) {
                if (msg.success) {
                   debugger;
                   $("#form_id").val(msg.result.id);
                   $("#form_templateid").val(msg.result.templateid);
                    $("#form_title").val(msg.result.title);
                    $("#form_content").val(msg.result.content);
                } else {
                    bootbox.alert(msg.msg);
                }
            }
        });
        $("#edit-dialog").modal('show');
    } else {
        bootbox.alert("请选择一项进行修改！");
    }

}