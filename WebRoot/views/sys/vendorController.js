var searchUrl = basePath + "/sys/vendor/page.do?filter_EQI_type=0";
$(function () {
    initGrid();

    initSearchType();

});
var vendorStatus;
function refresh(){
    location.reload(true);
}
function initSearchType() {
    $.ajax({
        url : basePath + "/sys/property/searchByType.do?type=VT",
        cache : false,
        async : false,
        mtype:"POST",
        type : "POST",
        success : function (data,textStatus){
            var json= data;
            for(var i=0;i<json.length;i++){
                $("#search_groupId,#form_groupId").append("<option value='"+json[i].code+"'>"+json[i].name+"</option>");

                //你在这里写初始化chosen 的那些代码
                $("#search_groupId,#form_groupId").trigger('chosen:updated');
            }
        }
    })
}

function initGrid() {
    var parent_column = $("#main-container");
//    $("#grid").jqGrid( 'setGridWidth', parent_column.width()-5);
    $("#grid").jqGrid({
        height: "auto",
        url: searchUrl,
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
            {name: 'id', label: 'id',hidden:true, width: 40,frozen:true},
            {name: 'code', label: '编号',editable:true, width: 100,frozen:true},
            {name: 'name', label: '名称', editable:true,width: 200},
            {name: 'groupId', label: '', editable:true,width: 100,hidden:true},
            {name: 'groupName',label:'分类',editable:true,width:100},
            {name: 'ownerId', label: '所属方',editable:true, width: 100},
            {name: 'unitName', label: '所属方名称',editable:true,sortable:false, width: 200},
            {name: 'tel', label: '联系电话',editable:true, width: 200},
            {name: 'creatorId', label: '创建人', width: 200},
            {name: 'createTime', label: '创建时间', width:200},
            {
                name: 'owingValue', label: '欠款金额', editable: true, width: 100,
                formatter: function (cellValue, options, rowObject) {
                    if (cellValue == undefined) {
                        return "";
                    }
                    return "¥" + cellValue;
                }
            },
            {name: 'remark', label: '备注', sortable:false,width: 400}
        ],
        autowidth: true,
        viewrecords: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        sortname : 'createTime',
        sortorder : "desc",
        shrinkToFit:false,
        autoScroll:false
    });
    $("#grid").jqGrid("setFrozenColumns");
}



function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");

}

function save() {
    $('#editForm').data('bootstrapValidator').validate();
    $('#editForm').data('bootstrapValidator').validate('ownerId');
    if(!$('#editForm').data('bootstrapValidator').isValid()){
        return ;
    }
    var progressDialog = bootbox.dialog({
        message: '<p><i class="fa fa-spin fa-spinner"></i> 数据上传中...</p>'
    });
    $.post(basePath+"/sys/vendor/save.do",
        $("#editForm").serialize(),
        function(result) {
            if(result.success == true || result.success == 'true') {
                progressDialog.modal('hide');
                $("#edit-dialog").modal('hide');
                $("#grid").trigger("reloadGrid");
            }
        }, 'json');

}

function unSelected() {

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

function _clearSearch() {
    $("#searchForm").resetForm();
}

function add() {
    vendorStatus ="add";
    $("#editForm").resetForm();
    $("#form_code").removeAttr("readOnly");
    $("#edit-dialog").modal('show');
    $("#codeGroup").hide();


}
function del() {

}
function locked() {


}
function edit() {
    vendorStatus="edit";
    var rowId = $("#grid").jqGrid("getGridParam", "selrow");
    if(rowId) {
        var row = $("#grid").jqGrid('getRowData',rowId);
        $("#edit-dialog").modal("show");
        $("#codeGroup").show();
        $("#form_code").attr("readOnly",true);
        $("#editForm").loadData(row);
    } else {
        bootbox.alert("请选择一项进行修改！");
    }
}

	