$(function () {
    iniGrid();
    iniDepartment();
    iniOwner();
    setTimeout(function () {
        search();
    },500);
});

function iniDepartment(){
    $.ajax({
        url:basePath+"/hall/department/list.do",
        cache: false,
        async: false,
        type: "POST",
        success:function (data,textStatus) {
                    var json =data;
                    if(json){
                        for(var i=0;i<json.length;i++)
                        $("#filter_EQS_ownerId,#form_ownerId").append("<option value='"+json[i].code+"'>"+json[i].name+"</option>");
                        $("#filter_EQS_ownerId,#form_ownerId").trigger("chosen:updated");
                    }
        }
        
    });
}

function iniOwner(){
    $.ajax({
        url:basePath+"/hall/room/list.do",
        cache:false,
        async:false,
        type:"POST",
        success:function (data, textStatus) {
            var json=data;
            if(json){
                for(var i=0;i<json.length;i++){
                    $("#form_uOwnerName").append("<option value='"+ json[i].code+"'>"+json[i].name+"</option>");
                    $("#form_uOwnerName").trigger("chosen:updated");
                }
            }
        }
    });
}

function search(){
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        url:basePath+"/hall/employee/page.do",
        page : 1,
        postData : params
    });
    $("#grid").trigger("reloadGrid");
}

function iniGrid(){
    $("#grid").jqGrid({
        height:"auto",
        datatype:"json",
        mtype:"POST",
        colModel:[
            {name:"isUser",label:"是否用户",width:70,editable:false,frozen:true,
                formatter:function (cellVaue,option,rowObject) {
                    if(cellVaue==1){
                        return "是";
                    } else {
                        return "否";
                    }
                }
            },
            {name:"code",label:"工号",width:180,editable:true,frozen:true},
            {name:"id",label:"id",width:10,editable:true,hidden:true},
            {name:"name",label:"姓名",width:180,editable:true,},
            {name:"",label:"所在部门",width:220,editable:true,
                formatter:function(cellValue,option,rowObject){
                        if(rowObject.ownerId!=undefined){
                            return rowObject.unitName;
                        }
            }},
            {name:"ownerId",label:"部门编号",width:20,editable:true,hidden:true},
            {name:"tel",label:"联系电话",width:220,editable:true},
            {name:"email",label:"邮箱",width:220,editable:true,sortable:false},
            {name:"createTime",label:"创建时间",width:250,editable:true},
            {name:"remark",label:"备注",width:400,editable:true}
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

function edit(){
    var rowId=$("#grid").jqGrid("getGridParam",'selrow');
    if(rowId){
        var row=$("#grid").jqGrid("getRowData",rowId);
        $("#form_code").show();
        $("#createTimeGroup").show();
        $("#edit_dialog").modal("show");
        $("#editForm").loadData(row);
    }else{
        bootbox.alert("请选择一项进行编辑");
    }

}

function refresh(){
    location.reload();
}

function setUser(){
    var uRowId=$("#grid").jqGrid("getGridParam",'selrow');
    if(uRowId){
        var uRow=$("#grid").jqGrid("getRowData",uRowId);
        if(uRow.isUser=="是"){
            bootbox.alert("该员工已设置账号,修改用户信息请联系管理员");
        } else {
            $("#edit_User_dialog").modal("show");
            var name=uRow.name;
            $("#setUserForm #form_asuName").val(name);
            $("#form_uCode").val(uRow.code);
        }

    } else {
        bootbox.alert("请选择员工进行设置");
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
   $.post(basePath+"/hall/employee/save.do",
       $("#editForm").serialize(),
        function (result) {
            if(result.success == true || result.success == 'true') {
                progressDialog.modal('hide');
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#edit_dialog").modal('hide');
                $("#grid").trigger("reloadGrid");
            }
        },"json"
   );
}

function showAdvSearchPanel(){
    $("#searchPanel").slideToggle("fast");
}