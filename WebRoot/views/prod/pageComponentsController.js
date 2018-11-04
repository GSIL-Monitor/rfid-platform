var selectType =null;
$(function () {
    initComponentsGrid();
    initComponentsDetailGrid();
});
function initComponentsGrid() {
    $("#componentsGrid").jqGrid({
        height: "auto",
        url: basePath + "/prod/components/page.do?filter_EQI_deep=0",
        datatype: "json",
        mtype: 'POST',
        colModel: [
            {name: 'id', label: '主键',hidden: true},
            {name: 'code', label: '成分code',hidden: true},
            {name: 'name', label: '成分名',width: 130},
            {name: 'rule', label: '成分校验规则',width: 100},
            {name: 'parentId', label: '父类成分code',width: 100},
            {name: 'deep', label: '层级',width: 40},
            {name: 'createDate', label: '创建时间',width: 100},
            {name: 'updateDate', label: '更新时间',width: 100},
            {name: 'createrId', label: '创建人',width: 100},
            {name: 'updaterId', label: '更新人',width: 85},
            {name: 'iconCode', label: '图标code',width: 85}
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
        sortname : 'createDate',
        sortorder : "desc",
        grouping:true,
        onSelectRow: function(rowid){
            var selectdata=$("#componentsGrid").getRowData(rowid);   //获取选中一行的数据
            selectType = selectdata.code;
            //查询数据并设置到右边表格
            console.info(selectdata);
            //右边加载表明细
            $("#componentsDetailGrid").jqGrid('setGridParam',{
                url:basePath + "/prod/components/page.do?filter_EQS_parentId="+selectType
            }).trigger("reloadGrid");   //刷新

        }
    });
    $("#componentsGrid").jqGrid('navGrid',"#grid-pager", {
            edit: false,
            add: true,
            addicon:"ace-icon fa fa-plus",
            addfunc:function(){
                addComponents();
            },
            del: false,
            search: false,
            refresh: false,
            view: false
    });
}
function initComponentsDetailGrid() {
    $("#componentsDetailGrid").jqGrid({
        height: "auto",
        url: basePath + "/prod/components/page.do?filter_EQS_parentId="+selectType,
        datatype: "json",
        mtype: 'POST',
        colModel: [
            {name: 'id', label: '主键',hidden: true},
            {name: 'code', label: '成分code',hidden: true},
            {name: 'name', label: '成分名',width: 130},
            {name: 'rule', label: '成分校验规则',width: 100},
            {name: 'parentId', label: '父类成分code',width: 100},
            {name: 'deep', label: '层级',width: 40},
            {name: 'createDate', label: '创建时间',width: 100},
            {name: 'updateDate', label: '更新时间',width: 100},
            {name: 'createrId', label: '创建人',width: 100},
            {name: 'updaterId', label: '更新人',width: 85},
            {name: 'iconCode', label: '图标code',width: 85}
        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 50,
        rowList: [20, 50, 100],
        pager: "#grid-pager2",
        multiselect: false,
        shrinkToFit: true,
        sortname : 'createDate',
        sortorder : "desc",
        grouping:true
    });
    $("#componentsDetailGrid").jqGrid('navGrid',"#grid-pager2", {
        edit: false,
        add: true,
        addicon:"ace-icon fa fa-plus",
        addfunc:function(){
            addComponentsDetail(selectType);
        },
        del: false,
        search: false,
        refresh: false,
        view: false
    });
}
function addComponentsDetail(selectType) {
    pagetype="add";
    $("#editForm").resetForm();
    $("#form_parentId").val(selectType);
    $("#edit-dialog").modal('show');
}
function addComponents() {
    pagetype="add";
    $("#editForm").resetForm();
    $("#edit-dialog").modal('show');
}
function save() {
    $("#editForm").data('bootstrapValidator').validate();
    if(!$("#editForm").data('bootstrapValidator').isValid()){
        return ;
    }
    var progressDialog = bootbox.dialog({
        message: '<p><i class="fa fa-spin fa-spinner"></i>数据上传中...</p>'
    });
    $.post(basePath+'/prod/components/save.do',
        $("#editForm").serialize(),
        function(result){
            progressDialog.modal('hide');
            if(result.success==true||result.success=='true'){
                $.gritter.add({
                    text : result.msg,
                    class_name : 'gritter-success  gritter-light'
                });
                $("#edit-dialog").modal('hide');
                $('#componentsGrid').trigger("reloadGrid");
                $('#componentsDetailGrid').trigger("reloadGrid");
            }else{
                $.gritter.add({
                    text : result.msg,
                    class_name : 'gritter-success  gritter-light'
                });
            }
        },'json');

}
