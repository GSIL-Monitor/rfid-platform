$(function(){
    iniGrid();
    setTimeout(function () {
        search();
    },500);
});

function search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        url:basePath+"/hall/room/page.do",
        page : 1,
        postData : params
    });
    $("#grid").trigger("reloadGrid");
}

function iniGrid() {
    $("#grid").jqGrid({
        height:"auto",
        mtype:"POST",
        datatype:"json",
        colModel:[
            // {name:"",label:"状态",width:70,editable:true,align:"center",
            //     formatter:function(cellValue,option,rowObject){
            //         var html="";
            //         if(rowObject.industry=="1"){
            //             html+="<i class='ace-icon fa fa-check green'></i>";
            //         }else{
            //             html+="<i class='ace-icon fa fa-close red'></i>";
            //         }
            //         return html;
            //     },frozen:true
            // },//重用废弃字段Industry字段保存状态信息
            // {name:"industry",label:"status",width:20,editable:true,hidden:true},
            // {name:"address",label:"address",width:20,editable:true,hidden:true},
            {name:"code",label:'编号',width:200,editable:true,frozen:true},
            {name:"name",label:'名称',width:200,editable:true},
            {name:"ownerId",label:'所属方',width:200,editable:true,hidden:true},
            {name:"unitName",label:"所属方名称",width:200,editable:true,hidden:true},
            // {name:"deviceId",label:"绑定设备",width:200,editable:true},
            {name:"tel",label:"联系电话",width:150,editable:true},
            {name:"linkman",label:"联系人",width:150,editable:true},
            {name:"email",label:"邮箱",width:200,editable:true},
            {name:"createTime",label:"创建时间",width:200,editable:true},
            {name:"remark",label:"备注",editable:true,width:400}
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

function add(){
    $("#edit_dialog").modal("show");
    $("#codeGroup").hide();
    $("#createTimeGroup").hide();
}

function edit() {
    var rowId=$("#grid").jqGrid("getGridParam","selrow");
    if(rowId){
        var row =$("#grid").jqGrid(("getRowData"),rowId);
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
        message: '<p><i class="fa fa-spin fa-spinner"></i> 数据上传中...</p>'});
    $.post(basePath+"/hall/room/save.do",$("#editForm").serialize(),
        function (result) {
            progressDialog.modal('hide');
            if(result.success == true || result.success == 'true'){
                $("#searchForm").resetForm();
                search();
            }
            $.gritter.add({
                text: result.msg,
                class_name: 'gritter-success  gritter-light'
            });
            $("#edit_dialog").modal('hide');
        },"json"
    );
}

function refresh(){
    location.reload();
}

function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}