$(function(){
    iniGrid();
    setTimeout(function(){
        search();
    },500);
});

function search(){
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        url:basePath+"/hall/department/page.do",
        page : 1,
        postData : params
    });
    $("#grid").trigger("reloadGrid");
}

function add(){
    $("#edit_dialog").modal("show");
    $("#codeGroup").hide();
    $("#createTimeGroup").hide();
}
function edit(){
    var rowId=$("#grid").jqGrid("getGridParam","selrow");
    if(rowId){
        var row=$("#grid").jqGrid("getRowData",rowId);
        $("#edit_dialog").modal("show");
        $("#editForm").loadData(row);
        $("#codeGroup").show();
        $("#createTimeGroup").show();

    }else{
        bootbox.alert("请选择一项进行编辑");
    }
}

function save(){
    $('#editForm').data('bootstrapValidator').validate();
    if(!$('#editForm').data('bootstrapValidator').isValid()){
        return ;
    }
    var progressDialog = bootbox.dialog({
        message: '<p><i class="fa fa-spin fa-spinner"></i> 数据上传中...</p>'
    });
    $.post(basePath+"/hall/department/save.do",
        $("#editForm").serialize(),
        function(result) {
            if(result.success == true || result.success == 'true') {
                progressDialog.modal('hide');
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#edit_dialog").modal('hide');
                $("#searchForm")
                search();
            }
        }, 'json');
}

function refresh(){
    location.reload();
}

function iniGrid(){
    $("#grid").jqGrid({
        height:"auto",
        mtype:"POST",
        datatype:"json",
        colModel:[
            {name:"code", label:"编号", width:180,editable:true,frozen:true,key:true},
            {name:"id",label:"id",width:10,editable:true,hidden:true},
            {name:"name",label:"部门名称",width:180,editable:true},
            {name:"linkTel",label:"联系电话",width:180,editable:true},
            {name:"linkman",label:"联系人",width:180,editable:true},
            {name:"email",label:"邮箱",width:300,editable:true,sortable:false},
            {name:"createTime",label:"创建时间",width:240,editable:true},
            {name:"remark",label:"备注",editable:true,width:480,sortable:false}
        ],
        viewrecords:true,
        autowidth:true,
        altRows:true,
        rownumbers:true,
        rowNum:20,
        rowList:[20,50,100],
        multiselect:false,
        shrinkToFit:false,
        pager:"#grid-pager",
        sortname:"createTime",
        sortorder:"desc",
        autoScroll:false
    });
    $("#grid").jqGrid("setFrozenColumns");
}


function showAdvSearchPanel(){
    $("#searchPanel").slideToggle("fast");
}