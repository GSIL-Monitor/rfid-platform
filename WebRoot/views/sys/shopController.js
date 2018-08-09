$(function () {
    initGrid();
    lodingareasId();
    lodingownerids();
    initSearchType();

});
function refresh() {
    location.reload(true);
}
function lodingareasId() {
    $.ajax({
        url : basePath + "/location/Regional/findallRegional.do",
        cache : false,
        async : false,
        type : "POST",
        success : function (data,textStatus){
            var json= data.result;
            for(var i=0;i<json.length;i++){
                $("#form_areasId").append("<option value='"+json[i].id+"'>"+json[i].name+"</option>");
                $("#form_areasId").trigger('chosen:updated');
            }
        }
    })

}
function lodingownerids() {
    $.ajax({
        url : basePath + "/location/Regional/findallUnit.do",
        cache : false,
        async : false,
        type : "POST",
        success : function (data,textStatus){
            var json= data.result;
            for(var i=0;i<json.length;i++){
                $("#form_ownerids").append("<option value='"+json[i].id+"'>"+json[i].name+"</option>");
                $("#form_ownerids").trigger('chosen:updated');
            }
        }
    })

}
var shopStatus;

function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: basePath + "/sys/shop/page.do?filter_EQI_type=4",
        datatype: "json",
        mtype:"POST",
        colModel: [
			{
			    name: "src", label: "来源", width: 80,editable:false,frozen:true,
			    formatter: function (cellValue, options, rowObject) {
			        var html = "";
			        switch(cellValue) {
			            case "01":
			                html = '<span class="label label-sm label-success">系统</span>';
			                break;
			            case "02":
			                html = '<span class="label label-sm label-inverse">同步</span>';
			                break;
			            case "03":
			                html = '<span class="label label-sm label-warning">导入</span>';
			                break;
			            default:
			                html = '<span class="label label-sm label-inverse">系统</span>';
			        }
			        return html;
			    }
			},
            /*{
                name: "", label: "操作", width: 55, editable: false, align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    var id = rowObject.id;
                    var html;

                    html = "<a style='margin-left: 20px' href='#' onclick=pushmessage('" + id + "')><i class='ace-icon fa fa-check-square-o' title='更新客户信息'></i></a>";
                    /!*html += "<a style='margin-left: 20px' href='#' onclick=doPrint('" + billNo + "')><i class='ace-icon fa fa-print' title='打印'></i></a>";*!/
                    return html;
                }
            },*/
            {name: 'code', label: '编号', editable: true, width: 100},
            {name: 'name', label: '名称', editable: true, width: 150},
            {name: 'groupId', hidden: true},
            {name: 'groupName', label: '分类', editable: true, width: 100},
            {name: 'ownerId',label:'所属方',editable:true,width:100},
            {name: 'unitName',label:'所属方名称',editable:true,sortable:false,width:100},
            {name: 'tel', label: '联系电话', editable: true, width: 200},
            {name: 'linkman', label: '联系人', editable:true,width:200},
            {name: 'email', label: '邮箱', editable: true, width: 200},
            {name: 'provinceId', label: '所在省份', editable: true, width: 100},
            {name: 'cityId', label: '所在城市', editable: true, width: 100},
            {name: 'address', label: '街道地址', editable: true, width: 200},
            {name: 'creatorId', label: '创建人', sortable:false,editable: true, width: 100},
            {name: 'createTime', label: '创建时间', width: 200},
            {name: 'remark', label: '备注', sortable:false, editable:true,width: 200}


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
        sortname: 'createTime',
        sortorder: "desc",
        autoScroll: false

    });
    $("#grid").jqGrid("setFrozenColumns");
}

function initSearchType() {
    $.ajax({
        url : basePath + "/sys/property/searchType.do?type=ST",
        cache : false,
        async : false,
        type : "POST",
        success : function (data,textStatus){
            var json= data;
            for(var i=0;i<json.length;i++){
                $("#search_groupId,#form_groupId").append("<option value='"+json[i].code+"'>"+"["+json[i].code+"]"+json[i].name+"</option>");
                //你在这里写初始化chosen 的那些代码
                $("#search_groupId,#form_groupId").trigger('chosen:updated');
            }
        }
    })
}
function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast", function () {
        var searchFormHeight = $(".widget-main").height();


    });

}
function save() {
    $("#editForm").data('bootstrapValidator').validate();
    if(!$("#editForm").data('bootstrapValidator').isValid()){
        return ;
    }
    if($("#form_ownerId").val()==""){
        bootbox.alert("所属方不能为空");
        return
    }
   var progressDialog = bootbox.dialog({
       message: '<p><i class="fa fa-spin fa-spinner"></i> 数据上传中...</p>'
   });
   $.post(basePath+"/sys/shop/save.do",
           $("#editForm").serialize(),
           function(result) {
               if(result.success == true || result.success == 'true') {
                   $.gritter.add({
                       text : result.msg,
                       class_name : 'gritter-success  gritter-light'
                   });
                   progressDialog.modal('hide');
                   $("#edit-dialog").modal('hide');
                   $('#grid').trigger("reloadGrid");
               }
           }, 'json');

}

function unSelected() {

}


function _clearSearch() {

}
function _search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        page: 1,
        postData: params
    });
    $("#grid").trigger("reloadGrid");
}

function cleanSearch() {

}

function add() {
    shopStatus="add";
    $("#editForm").resetForm();
	$("#edit-dialog").modal('show');
}

function del() {

}

function edit() {
    shopStatus="edit";
    var row=$("#grid").jqGrid("getGridParam",'selrow');
    $("#editForm").resetForm();
    if(row){
        var rowData=$("#grid").jqGrid("getRowData",row);
        $("#edit-dialog").modal('show');
        $("editForm").loadData(rowData);
    } else {
        bootbox.alert("请选择一项进行更改");
    }
}

function pushmessage(id) {
    $.ajax({
        dataType: "json",
        url: basePath + "/location/Regional/pushMessage.do",
        data: {id: id},
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

	
	