$(function () {
    initGrid();

    initSearchType();
});
var pagetype;
function refresh() {
    location.reload(true);
}
function initSearchType() {
    $.ajax({
        url: basePath + "/sys/property/searchByType.do?type=WT",
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $('#search_groupId,#form_groupId').append("<option value='" + json[i].code + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                //你在这里写初始化chosen 的那些代码
                $("#search_groupId,#form_groupId").trigger('chosen:updated');
            }
        }
    })
}

function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        mtype: "POST",
        url: basePath + "/sys/warehouse/page.do?filter_EQI_type=9",
        datatype: "json",
        colModel: [
            {
                name: 'src',
                label: '来源',
                editable: true,
                width: 80,
                frozen: true,
                formatter: function (cellValue, options, rowObject) {
                    var html = "";
                    switch (cellValue) {
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
            {name: 'code', label: '编号', editable: true, width: 100},
            {name: 'id', label: 'id', hidden: true, width: 40},
            {name: 'name', label: '名称', editable: true, width: 100},
            {
                name: "", label: "设置", width: 200, editable: false, align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    var html;
                   /* href='" + basePath + "/sys/warehouse/setDefaultWareh.do?warehId=" + warehId + "&ownerId=" + ownerId + "'*/
                    html = "<a style='margin-left: 20px' onclick=onset('"+rowObject.id+"')><i class='ace-icon fa fa-cog' title='设置默认仓库'></i></a>";
                    return html;

                }
            },
            {name: 'groupId', label: '分类', editable: true, width: 100},
            {name: 'ownerId', label: '所属方', editable: true, width: 100},
            {name: 'unitName', label: '所属方名称', sortable: false, editable: true, wdith: 100},
            {name: 'tel', label: '联系电话', editable: true, width: 150},
            {name: 'linkman', label: '联系人', editable: true, width: 150},
            {name: 'email', label: '邮箱', editable: true, width: 150},
            {name: 'provinceId', label: '所在省份', editable: true, width: 100},
            {name: 'cityId', label: '所在城市', editable: true, width: 100},
            {name: 'address', label: '街道地址', editable: true, width: 200},
            {name:'creatorId',label:'创建人',editable:true,width:100},
            {name: 'createTime', label: '创建时间', width: 200},
            {name: 'remark', label: '备注', sortable: false, width: 200},

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
//        setFrozenColumns:0
    });
}

function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}
//john 添加
function save() {
    $("#editForm").data('bootstrapValidator').validate();
    if(!$("#editForm").data('bootstrapValidator').isValid()){
        return ;
    }
    if($("#form_ownerId").val()==""){
        bootbox.alert("所属方不能为空");
        return;
    }
    var progressDialog = bootbox.dialog({
        message: '<p><i class="fa fa-spin fa-spinner"></i>数据上传中...</p>'
    });

    $.post(basePath+'/sys/warehouse/save.do',
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
    pagetype="add";
    $("#editForm").resetForm();
    $("#edit-dialog").modal('show');
    $("#form_code").removeAttr("readOnly");
}
function del() {

}

function edit() {
    pagetype="edit";
    var rowId = $("#grid").jqGrid("getGridParam", 'selrow');
    $("#editForm").resetForm();
    if (rowId) {
        var rowData = $("#grid").jqGrid("getRowData", rowId);
        $("#edit-dialog").modal("show");
        $("#form_code").attr("readOnly",true);
        $("#editForm").loadData(rowData);
    } else {
        bootbox.alert("请选择一项进行修改!");
    }
}
function doSearch() {
    datagrid.datagrid('load', sy.serializeObject(searchForm));
}

function onset(rowId) {
    debugger;
    var row = $("#grid").jqGrid('getRowData', rowId);
    var warehId=row.id;
    var ownerId=row.ownerId;
    $.ajax({
        url : basePath + "/sys/warehouse/setDefaultWareh.do",
        cache : false,
        async : false,
        data :{"warehId":warehId,"ownerId":ownerId},
        type : "POST",
        success : function (data,textStatus){
            if(data.success==true||data.success=='true') {
                $.gritter.add({
                    text : "设置成功",
                    class_name : 'gritter-success  gritter-light'
                });
            }

        }
    })
}
	
	