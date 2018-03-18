$(function () {
    initGrid();

    var type_parent_column = $("#typeGrid").closest('.widget-main');
    var property_parent_column = $("#propertyGrid").closest('.widget-main');
    //resize to fit page size
    $(window).on('resize.jqGrid', function () {
        $("#typeGrid").jqGrid( 'setGridWidth', type_parent_column.width() );
        $("#propertyGrid").jqGrid( 'setGridWidth', property_parent_column.width() );
    });
});
function refresh(){
    location.reload(true);
}
var pagetype;
//左边表格数据
function initGrid() {

    $("#typeGrid").jqGrid({
    	treeGrid: true,
        height: 500,
        mtype:"POST",
        url: basePath + "/sys/property/page.do",
        datatype: "json",
        colModel: [
           
            {name: 'id', label: 'id',hidden:true, width: 40},
            {name: 'isUse', label: 'id',hidden:true, width: 40},
            {name: 'type', label: '类型', editable:true,width: 40},
            {name: 'keyId', label: '分类编号',editable:true, width: 40},
            {name: 'value', label: '名称',editable:true, width: 40},
            {
                name: "", label: "操作", width: 40, editable: false, align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    var id = rowObject.id;
                    var html =  "<a style='margin-left: 20px' href='#' onclick=edittype('"+id+"')><i class='ace-icon fa fa-check-square-o' title='编辑'></i></a>";
                    if(rowObject.isUse=="N"){
                        html += "<a style='margin-left: 20px' href='#' onclick=changetypeStatus('"+id+"','Y')><i class='ace-icon fa fa-check' title='启用'></i></a>";
                    }else{
                        html += "<a style='margin-left: 20px' href='#' onclick=changetypeStatus('"+id+"','N')><i class='ace-icon fa fa-lock' title='废除'></i></a>";
                    }
                    return html;
                }
            }
        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 50,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: true,
        sortname : 'type',
        sortorder : "desc",
        
        viewrecords: true,      
        grouping:true,
        groupingView : {
            groupField : ['type'],
            groupColumnShow : [false],
            groupText : ['<b>{0} - {1} 项</b>'],
            plusicon: 'ace-icon fa fa-plus',
            minusicon: 'ace-icon fa fa-minus'
        },
        onSelectRow: function(rowid){
        	 var selectdata=$("#typeGrid").getRowData(rowid);   //获取选中一行的数据
   	
        	//查询数据并设置到右边表格
        	 $("#propertyGrid").jqGrid('setGridParam',{
        		 url:basePath + "/sys/property/searchByType.do?type="+selectdata.id
        		 //postData:{id:selectdata.id}
        	 }).trigger("reloadGrid");   //刷新
        	
        	
        }
        
        

    });
    $("#typeGrid").jqGrid('navGrid',"#grid-pager",
        {
            edit: false,
            add: true,
            addicon:"ace-icon fa fa-plus",
            addfunc:function(){
                addtype();
            },
            del: false,
            search: false,
            refresh: false,
            view: false
        });
    $("#propertyGrid").jqGrid({
        height: 500,     
        datatype: "json",
        colModel: [
            {name: 'id', label: 'id',hidden:true, width: 40},
    		{name: 'code', label: '编号',hidden:true, width: 40},
            {name: 'ynuse', label: 'id',hidden:true, width: 40},
    		//{name: 'seqNo', label: '序号',editable:true, width: 40},
    		{name: 'locked', label: '锁定', editable:true,width: 40,align:"center",
                formatter:function(cellValue, options, rowObject){
                    if(cellValue==0){
                        return "<i class='ace-icon fa fa-unlock'></i>";
                    }else{
                        return "<i class='ace-icon fa fa-lock'></i>";
                    }
                }},
    		{name: 'name', label: '代码名称', editable:true,width: 40},
            {
                name: "", label: "操作", width: 40, editable: false, align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    var id = rowObject.id;
                    var html =  "<a style='margin-left: 20px' href='#' onclick=editproperty('"+id+"')><i class='ace-icon fa fa-check-square-o' title='编辑'></i></a>";
                    if(rowObject.ynuse=="N"){
                        html += "<a style='margin-left: 20px' href='#' onclick=changepropertyStatus('"+id+"','Y')><i class='ace-icon fa fa-check' title='启用'></i></a>";
                    }else{
                        html += "<a style='margin-left: 20px' href='#' onclick=changepropertyStatus('"+id+"','N')><i class='ace-icon fa fa-lock' title='废除'></i></a>";
                    }
                    return html;
                }
            },
    		{name: 'ownerId', label: '品牌商',editable:true, width: 40},
    		{name: 'registerId', label: '创建者',editable:true, width: 40},
    		{name: 'registerDate', label: '创建时间',editable:true, width: 60},
    		{name: 'ynuse', label: '是否使用',editable:true, width: 40},           
    		
    		
    		
        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 100,
        rowList: [20, 50, 100],
        pager: "#grid-pager2",
        multiselect: false,
        shrinkToFit: true,
        sortname : 'registerDate',
        sortorder : "desc"


    });
    $("#propertyGrid").jqGrid('navGrid',"#grid-pager2",
        {
            edit: false,
            add: true,
            addicon:"ace-icon fa fa-plus",
            addfunc:function(){
                addproperty();
            },
            del: false,
            search: false,
            refresh: false,
            view: false
        });

    var type_parent_column = $("#typeGrid").closest('.col-sm-6');
    var property_parent_column = $("#propertyGrid").closest('.col-sm-6');
    $("#typeGrid").jqGrid( 'setGridWidth', type_parent_column.width());
    $("#propertyGrid").jqGrid( 'setGridWidth', property_parent_column.width() );
}

function fullScreen(){
    var jqGridWidth = $('#main-container').width()-20;
    $("#propertyGrid").jqGrid( 'setGridWidth', jqGridWidth);
    $("#propertyGrid").trigger("reloadGrid");
}

function edittype (rowId) {
    debugger;
   $("#form_id").attr("disabled","disabled");
    pagetype="edit";
    $("#editForm").resetForm();
    if (rowId) {
        var rowData = $("#typeGrid").jqGrid("getRowData", rowId);
        $("#edit-dialog").modal("show");
        $("#form_code").attr("readOnly",true);
        $("#editForm").loadData(rowData);
    } else {
        bootbox.alert("请选择一项进行修改!");
    }
}

function editproperty(rowId) {
    debugger;
    pagetype="edit";
    $("#editFormdetailed").resetForm();
    if (rowId) {
        var rowData = $("#propertyGrid").jqGrid("getRowData", rowId);
        $("#edit-dialog-detailed").modal("show");
        $("#form_code").attr("readOnly",true);
        $("#editFormdetailed").loadData(rowData);
    } else {
        bootbox.alert("请选择一项进行修改!");
    }
}


function addtype() {
    $("#form_id").removeAttr("disabled");
    pagetype="add";
    $("#editForm").resetForm();
    $("#edit-dialog").modal('show');
    $("#form_code").removeAttr("readOnly");
}

function addproperty() {
    debugger;
    pagetype="add";
    var rowId = $("#typeGrid").jqGrid("getGridParam", 'selrow');
    if (rowId) {
        $("#editFormdetailed").resetForm();
        $("#edit-dialog-detailed").modal('show');
        $("#form_code").removeAttr("readOnly");
        $("#form_ids").val(rowId);
        $("#form_type").val(rowId);
    }else {
        bootbox.alert("请选择一项进行修改!");
    }
}


function savetype() {
    $("#editForm").data('bootstrapValidator').validate();
    if(!$("#editForm").data('bootstrapValidator').isValid()){
        return ;
    }
   /* if($("#form_ownerId").val()==""){
        bootbox.alert("所属方不能为空");
        return;
    }*/
    $("#form_id").removeAttr("disabled");
    var progressDialog = bootbox.dialog({
        message: '<p><i class="fa fa-spin fa-spinner"></i>数据上传中...</p>'
    });

    $.post(basePath+'/sys/property/save.do',
        $("#editForm").serialize(),
        function(result){
            if(result.success==true||result.success=='true'){
                $.gritter.add({
                    text : result.msg,
                    class_name : 'gritter-success  gritter-light'
                });
                progressDialog.modal('hide');
                $("#edit-dialog").modal('hide');
                $('#grid').trigger("reloadGrid");
            }
        },'json');
    window.location.reload();

}

function saveproperty() {
    $("#editFormdetailed").data('bootstrapValidator').validate();
    if(!$("#editFormdetailed").data('bootstrapValidator').isValid()){
     return ;
     }
    /* if($("#form_ownerId").val()==""){
     bootbox.alert("所属方不能为空");
     return;
     }*/
    var progressDialog = bootbox.dialog({
        message: '<p><i class="fa fa-spin fa-spinner"></i>数据上传中...</p>'
    });
    $.post(basePath+'/sys/property/saveproperty.do',
        $("#editFormdetailed").serialize(),
        function(result){
            if(result.success==true||result.success=='true'){
                $.gritter.add({
                    text : result.msg,
                    class_name : 'gritter-success  gritter-light'
                });
                progressDialog.modal('hide');
                $("#edit-dialog-detailed").modal('hide');
                $('#grid').trigger("reloadGrid");
            }
        },'json');
    window.location.reload();
}

function changetypeStatus(rowId,status) {
    $.ajax({
        url: basePath + '/sys/property/changetypeStatus.do',
        dataType: 'json',
        data: {
            id: rowId,
            status: status
        },
        success: function (result) {
            debugger;
            if (result.success) {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#typeGrid").trigger('reloadGrid');
            } else {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-fail  gritter-light'
                });
            }
        }
    });
}

function changepropertyStatus(rowId,status) {
    $.ajax({
        url: basePath + '/sys/property/changepropertyStatus.do',
        dataType: 'json',
        data: {
            id: rowId,
            status: status
        },
        success: function (result) {
            debugger;
            if (result.success) {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#propertyGrid").trigger('reloadGrid');
            } else {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-fail  gritter-light'
                });
            }
        }
    });
}

	