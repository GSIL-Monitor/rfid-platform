$(function(){

    iniOwner();
    setTimeout(function () {
        iniAgrid();
        iniEgrid();
    },500);
});

var storageName="库位";
function search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#Agrid").jqGrid('setGridParam', {
        page : 1,
        postData : params
    }).trigger("reloadGrid");
}

function refresh(){
    location.reload();
}

//编辑分区信息
function addArea(){
    $("#area_edit_dialog").modal("show");
    $("#AcodeGroup").hide();
    $("#form_Astatus").show();
}

function editArea(){
    var rowId=$("#Agrid").jqGrid("getGridParam","selrow");
    if(rowId){
        var row =$("#Agrid").jqGrid("getRowData",rowId);
        $("#area_edit_dialog").modal("show");
        if(row.status==0) {
            $("#asDefaultAreaGroup").hide();
        }
        $("#AcodeGroup").show();
        $("#area_editForm").loadData(row);
    }else{
        bootbox.alert("请先选择一项进行编辑");
    }
}

function saveArea() {
    $("#area_editForm").data("bootstrapValidator").validate();
    if(!$("#area_editForm").data("bootstrapValidator").isValid()){
        return
    }
    var progress =bootbox.dialog({
        message: '<p><i class="fa fa-spin fa-spinner"></i> 数据上传中...</p>'
    });
    $.post(basePath+"/hall/floor/save.do",$("#area_editForm").serialize(),function (result) {
        progress.modal("hide");
        if(result.success == true || result.success == 'true'){
            $("#Agrid").trigger("reloadGrid");
            $("#Egrid").trigger("reloadGrid");
        }
        $.gritter.add({
            text:result.msg,
            class_name: 'gritter-success  gritter-light'
        });
        $("#area_edit_dialog").modal("hide");
    },"json");

}

//编辑库位信息
function addStorage() {
    var rowId=$("#Agrid").jqGrid("getGridParam",'selrow');
    if(rowId){
        var row =$("#Agrid").jqGrid("getRowData",rowId);

        $("#ScodeGroup").hide();
        $("#storage_edit_dialog").modal("show");
        $("#form_SownerId").val(row.code);
    } else {
        bootbox.alert("请先选择分区");
    }
}

function editStorage(){
    var rowId=$("#Egrid").jqGrid("getGridParam",'selrow');
    if(rowId){
        var row=$("#Egrid").jqGrid("getRowData",rowId);
        $("#ScodeGroup").show();
        $("#storage_edit_dialog").modal('show');
        $("#storage_editForm").loadData(row);
        if(row.status==0){
            $("#asDefaultGroup").hide();
        } else {
            $("#asDefaultGroup").show();
        }
    } else {
        bootbox.alert("请选择库位");
    }
}


function saveStorage(){


    $("#storage_editForm").data("bootstrapValidator").validate();
    if(!$("#storage_editForm").data("bootstrapValidator").isValid()){
        return
    }
    var progressDialog =bootbox.dialog({
        message: '<p><i class="fa fa-spin fa-spinner"></i> 数据上传中...</p>'
    });
    $.post(basePath+"/hall/floor/save.do",$("#storage_editForm").serialize(),
        function (result) {
            progressDialog.modal('hide');
            $.gritter.add({
                text:result.msg,
                class_name: 'gritter-success  gritter-light'
            });
            if(result.success=="true"||result.success==true){
                $("#Egrid").trigger("reloadGrid");
            }
            $("#storage_edit_dialog").modal('hide');
        },"json"
    );
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
                    $("#filter_EQS_ownerId,#form_AownerId").append("<option value='"+ json[i].code+"'>"+json[i].name+"</option>");
                    $("#filter_EQS_ownerId,#form_AownerId").trigger("chosen:updated");
                }
            }
        }
    });
}

function iniAgrid(){
    $("#Agrid").jqGrid({
        height:"auto",
        datatype:"json",
        url:basePath+"/hall/floor/page.do?filter_EQS_areaId=A",//分区
        mtype:"POST",
        colModel:[
            {name:"",label:"分区状态",editable:true,width:70,sortable:false,align:"center",
                formatter:function (rowValue, option, rowObject) {
                    var html="";
                    if(rowObject.status==1){
                        html+="<i class='ace-icon fa fa-check green'></i>";
                    } else {
                        html+="<i class='ace-icon fa fa-close red'></i>";
                    }
                    return html;
                }},
            {name:"status",label:status,editable:true,width:20,hidden:true},
            {name:"",label:"分区默认",width:70,editable:true,sortable:false,align:"center",
                formatter:function (rowValue, option, rowObject) {
                    var html="";
                    if(rowObject.asDefault==1){
                        html+="<i class='ace-icon fa fa-flag red'></i>";
                    } else {
                        html +="<i class='ace-icon fa fa-circle'></i>"
                    }
                    return html;
                }
            },
            {name:"asDefault",label:"asDefault",editable:true,width:70,hidden:true},
            {name:"code",label:"分区编码",editable:true,width:150},
            {name:"name",label:"分区名称",editable:true,width:150},
            {name:"ownerId",label:"ownerId",editable:true,width:150,hidden:true},
            {name:"unitName",label:"所属展厅",editable:true,width:150,sortable:false},
            {name:"remark",label:"备注",editable:true,width:300}
        ],
        viewrecords:true,
        autowidth:true,
        altRows:true,
        rownumbers:true,
        rowNum:20,
        caption:"分区信息",
        rowList:[20,50,100],
        multiselect:false,
        shrinkToFit:false,
        pager:"#Agrid-pager",
        sortname:"code",
        sortorder:"desc",
        autoScroll:false,
        onSelectRow:function(rowId){
        var selRow = $("#Agrid").jqGrid("getRowData",rowId);
        $("#Egrid").jqGrid("setGridParam",{
            url:basePath+"/hall/floor/page.do?filter_EQS_areaId=E&filter_EQS_ownerId="+selRow.code,//库位
        }).trigger("reloadGrid");
    }

});
}

function iniEgrid() {
    $("#Egrid").jqGrid({
        height:"auto",
        datatype:"json",
        mtype:"POST",
        colModel:[
            {name:"",label:storageName+"状态",editable:true,width:70,align:"center",
                formatter:function (rowValue, option, rowObject) {
                    var html="";
                    if(rowObject.status==1){
                        html+="<i class='ace-icon fa fa-check green'></i>";
                    } else {
                        html+="<i class='ace-icon fa fa-close red'></i>";
                    }
                    return html;
                }},
            {name:"status",label:"status",editable:true,width:20,hidden:true},
            {name:"",label:storageName+"默认",width:70,editable:true,sortable:false,align:"center",
                formatter:function (rowValue, option, rowObject) {
                    var html="";
                    if(rowObject.asDefault==1){
                        html+="<i class='ace-icon fa fa-flag red'></i>";
                    } else {
                        html +="<i class='ace-icon fa fa-circle'></i>"
                    }
                    return html;
                }
            },
            {name:"asDefault",label:"asDefault",editable:true,width:70,hidden:true},
            {name:"code",label:storageName+"编码",editable:true,width:150},
            {name:"name",label:storageName+"名称",editable:true,width:150},
            {name:"ownerId",label:"所属分区",editable:true,width:150,hidden:true},
            {name:"remark",label:"备注",editable:true,width:300},
        ],
        viewrecords:true,
        autowidth:true,
        altRows:true,
        rownumbers:true,
        rowNum:20,
        rowList:[20,50,100],
        multiselect:false,
        caption:storageName+"信息",
        shrinkToFit:false,
        pager:"#Egrid-pager",
        sortname:"code",
        sortorder:"desc",
        autoScroll:false
    });
}

function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}