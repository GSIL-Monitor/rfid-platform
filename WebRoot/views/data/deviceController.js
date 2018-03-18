$(function(){
	initGrid();
	iniowner();
});

function refresh(){
    location.reload(true);
}

function iniowner(){
	$.ajax({
		url:basePath+"/unit/list.do?filter_INI_type="+constant.unitType.Agent+","+constant.unitType.Headquarters,
		cache : false,
        async : false,
        type : "POST",
        success : function (data,textStatus){
            var json= data;
            for(var i=0;i<json.length;i++){
                $("#search_ownerId").append("<option value='"+json[i].code+"'>"+json[i].name+"</option>");
                //你在这里写初始化chosen 的那些代码
                $("#search_ownerId").trigger('chosen:updated');
            }
        }
	})
} 

function initGrid(){

	$("#grid").jqGrid({
		height:"auto",
		url:basePath + "/data/device/page.do",
		datatype: "json",
        colModel: [
            {name: 'code', label: '设备编号',editable:true, width: 150,frozen:true},
            {name: 'id', label: 'id',hidden:true, width: 40},
            {name: 'ownerId', label: '所属方编号', width: 100},
            {name: 'unitName', label: '所属方', sortable:false,editable:true,width: 150},
            {name: 'storageId', label: '所属仓店编号', editable:true,width: 150},
            {name: 'storageName', label: '所属仓店名称', sortable:false,editable:true,width:150},
            {name:'appCode',label:'用户ID',editable:true,width:150},
            {name:'mchId',label:'商户电子ID',editable:true,width:150},
            {name:'key',label:'电子签名',editable:true,width:150},
            {name:'callbackUrl',label:'回传地址',editable:true,width:200},
            {name: 'creator',label:'创建人',sortable:false,editable:true,width:150},
            {name: 'createTime',label:'创建时间',editable:true,width:170},
            {name: 'updater',label:'更新人',editable:true,sortable:false,width:150},
            {name: 'updateTime',label:'更新时间',editable:true,width:170},
            {name: 'remark', label:'备注', editable:true,width: 400},                   
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
        sortname : 'code',
        sortorder : "asc",
      	autoScroll:false
    });
	$("#grid").jqGrid("setFrozenColumns");
}

function _search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        page : 1,
        postData : params
    });
  $("#grid").trigger("reloadGrid");
}

function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");

}

function add(){
    $("#editForm").resetForm();
    $("#form_code").attr("readOnly",false);
    $("#edit-dialog").modal('show');
}
function edit(){
    $("#form_code").attr("readOnly",true);
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if(rowId) {
        var row = $("#grid").jqGrid('getRowData',rowId);
        $("#edit-dialog").modal("show");
        $("#form_code").val(row.code);
        $("#form_ownerId").val(row.ownerId);
        $("#form_ownerName").val(row.unitName);
        $("#form_appCode").val(row.appCode);
        $("#form_mchId").val(row.mchId);
        $("#form_key").val(row.key);
        $("#form_callbackUrl").val(row.callbackUrl);
/*        $("#form_storageId").empty();
        $("#form_storageId").append("<option value='"+row.storageId+"'>"+row.storageName+"</option>");*/
        $("#form_storageId").val(row.storageId);
        $("#form_storageName").val(row.storageName);
        $("#form_remark").val(row.remark);
    } else {
        bootbox.alert("请选择一项进行修改！");
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
    var formData=$("#editForm").serialize();
    $.ajax({
        type:"POST",
        url:basePath+"/data/device/save.do",
        data:formData,
        dataType:"json",
        success:function(result){
            if(result.success == true || result.success == 'true') {
                progressDialog.modal('hide');
                $("#edit-dialog").modal('hide');
                $("#grid").trigger("reloadGrid");
            }else{
                progressDialog.modal('hide');
                $("#edit-dialog").modal('hide');
                bootbox.alert(result.msg);
            }
        }
    })
}
function closeEditDialog(){
    $("#edit-dialog").modal('hide');
}

function editConfig() {
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if(rowId) {
        var row = $("#grid").jqGrid('getRowData',rowId);
        location.href = basePath+"/data/device/editConfigPage.do?deviceId="+row.code;

    } else {
        bootbox.alert("请选择一项进行修改！");
    }

}