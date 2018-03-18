$(function () {
    initGrid();
});
function refresh() {
    location.reload(true);
}

function initGrid() {
    //设置左边表格数据
    $("#sortgrid").jqGrid({
        height: 500,
        url: basePath + "/prod/size/page.do",
        mtype:"POST",
        datatype: "json",
        colModel: [
            {name: 'id', label: 'id', hidden: true, width: 100},
            {name: 'isUse', label: 'isUse', hidden: true},
            //{name: 'seqNo', label: '序号',editable:true, width: '20%'},
            {name: 'sortNo', label: '尺寸组编码', editable: true, width: 20},
            {name: 'sortName', label: '尺寸组名', editable: true, width: 30},
            {
                name: '', label: '编辑', editable: true, width: 20, align: 'center',
                formatter: function (cellValue, option, rowObject) {
                    var id = rowObject.id;
                    var html = "<a  href='#' onclick=sizeSortEdit('" + id + "')><i class ='fa  fa-edit' aria-hidden='true' title='操作'></i></a>";

                    if(rowObject.isUse=="N"){
                        html += "<a style='margin-left: 20px' href='#' onclick=changeSortStatus('"+id+"','Y')><i class='ace-icon fa fa-check' title='启用'></i></a>";
                    }else{
                        html += "<a style='margin-left: 20px' href='#' onclick=changeSortStatus('"+id+"','N')><i class='ace-icon fa fa-lock' title='废除'></i></a>";
                    }
                    return html;
                }
            },
            {name: 'remark', label: '备注', editable: true, width: 40, sortable: false},
        ],

        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        // rowNum: -1,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: true,
        sortname: 'createTime',
        sortorder: "desc",
        //行点击事件

        onSelectRow: function (rowid) {
            var selectdata = $("#sortgrid").getRowData(rowid);
            //查询数据并设置到右边表格
            $("#setgrid").jqGrid('setGridParam', {
                url: basePath + "/prod/size/searchById.do",
                postData: {sortId: selectdata.sortNo}
            }).trigger("reloadGrid");

        }
    });
    $("#sortgrid").jqGrid('navGrid',"#grid-pager",
        {
            edit: false,
            add: true,
            addicon:"ace-icon fa fa-plus",
            addfunc:function(){
                sizeSortAdd();
            },
            del: false,
            search: false,
            refresh: false,
            view: false
        });

    //右边表格 
    $("#setgrid").jqGrid({
        height: 500,
        mtype:"POST",
        datatype: "json",
        colModel: [

            {name: 'id', label: 'id', hidden: true, width: 40},
            {name: 'isUse', label: 'isUse', hidden: true, width: 40},
            //{name: 'sortId', label: '序号',editable:true, width: 40},
            {name: 'sizeId', label: '尺寸编码', editable: true, width: 20},
            {name: 'sizeName', label: '尺寸', editable: true, width: 40},
            {name: 'oprId', label: '创建人', editable: true, width: 30},
            {name: 'sortId',label:'尺寸组',editable:true,hidden: true},
            {
                name: '', label: '编辑', editable: true, width: 30, align: 'center',
                formatter: function (cellValue, option, rowObject) {
                    var html = "<a  href='#' onclick=sizeEdit('" + rowObject.id + "')><i class ='fa  fa-edit' aria-hidden='true' title='操作'></i></a>";

                    if(rowObject.isUse=="Y"){
                        html += "<a style='margin-left: 20px' href='#' onclick=changeSizeStatus('"+rowObject.id+"','N')><i class='ace-icon fa fa-check' title='启用'></i></a>";
                    }else{
                        html += "<a style='margin-left: 20px' href='#' onclick=changeSizeStatus('"+rowObject.id+"','Y')><i class='ace-icon fa fa-lock' title='废除'></i></a>";
                    }
                    return html;
                }
            },
            {name: 'updateTime', label: '更新时间', editable: true, width: 40},
        ],

        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: -1,
        pager:"#grid-pager2",
        multiselect: false,
        shrinkToFit: true,
        sortname: 'createTime',
        sortorder: "desc"

    });
    $("#setgrid").jqGrid('navGrid',"#grid-pager2",
        {
            edit: false,
            add: true,
            addicon:"ace-icon fa fa-plus",
            addfunc:function(){
                sizeAdd();
            },
            del: false,
            search: false,
            refresh: false,
            view: false
        });
    var type_parent_column = $("#sortgrid").closest('.col-sm-6');
    var property_parent_column = $("#setgrid").closest('.col-sm-6');
    $("#sortgrid").jqGrid('setGridWidth', type_parent_column.width());
    $("#setgrid").jqGrid('setGridWidth', property_parent_column.width());
}

function fullScreen() {
    var jqGridWidth = $('#main-container').width() - 20;
    $("#setgrid").jqGrid('setGridWidth', jqGridWidth);
    $("#setgrid").trigger("reloadGrid");
}

function sizeSortAdd() {
    $("#editSizeSortForm").resetForm();
    $("#edit_size_sort_dialog").modal('show');
    $("#form_sortNo").removeAttr("disabled");
}

function sizeSortEdit(rowId) {
        var row = $("#sortgrid").jqGrid('getRowData', rowId);
        $("#form_sortNo").attr("disabled", true);
        $("#edit_size_sort_dialog").modal("show");
        $("#editSizeSortForm").loadData(row);
}

function sizeAdd() {
    var sortRowId = $("#sortgrid").jqGrid("getGridParam", "selrow");
        console.log(sortRowId);
        if(sortRowId){
            var row = $("#sortgrid").jqGrid('getRowData', sortRowId);
            $("#editSizeForm").resetForm();
            $("#edit_size_dialog").modal('show');
            $("#form_sortId").val(sortRowId);
            $("#form_sortId").attr("disabled",true);
        }else {
        bootbox.alert("请选择尺码组进行添加！");
    }
}
function sizeEdit(rowId) {
        var row = $("#setgrid").jqGrid('getRowData', rowId);
        $("#edit_size_dialog").modal("show");
        $("#editSizeForm").loadData(row);
        $("#form_sizeId").attr("disabled",true);
        $("#form_sortId").attr("disabled",true);

}
function changeSortStatus(rowId,status) {
    $.ajax({
        url: basePath + '/prod/size/changeSortStatus.do',
        dataType: 'json',
        data: {
            sortNo: rowId,
            status: status
        },
        success: function (result) {
            if (result.success) {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#sortgrid").trigger('reloadGrid');
            } else {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-fail  gritter-light'
                });
            }
        }
    });
}

function changeSizeStatus(rowId,status) {
    $.ajax({
        url: basePath + '/prod/size/changeSizeStatus.do',
        dataType: 'json',
        data: {
            sizeId: rowId,
            status: status
        },
        success: function (result) {
            if (result.success) {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#setgrid").trigger('reloadGrid');
            } else {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-fail  gritter-light'
                });
            }
        }
    });
}