var orig="";
var dest="";
var modelCode="";
var rowNo=0;
$(function () {
    initDataSourceSelect();
});
function initDataSourceSelect() {
    $.ajax({
        url: basePath + "/exchange/datasource/list.do",
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#form_origDs,#form_destDs").append("<option value='" + json[i].id + "'>" + json[i].name + "</option>");
                $("#form_origDs,#form_destDs").trigger('chosen:updated');
            }
        }
    });
}
function initTableSelect(dataSourceId, select) {
    var progressDialog = bootbox.dialog({
        message: '<p><i class="fa fa-spin fa-spinner"></i> 获取数据中...</p>'
    });
    $.ajax({
        url: basePath + "/exchange/interfaceModel/getTables.do?dataSourceId=" + dataSourceId,
        type: "POST",
        cache: false,
        async:false,
        success: function (data, textStatus) {
            progressDialog.modal('hide');
            var json = data.result;
            var html = "<option value=''>-请选择-</option>";
            if (json != null) {
                for (var i = 0; i < json.length; i++) {
                    html += "<option value='" + json[i] + "'>" + json[i] + "</option>";
                }
            }
            select.html(html);
            select.trigger('chosen:updated');
        }
    });
}

function saveInterfaceModel() {
    var origDsId = $("#form_origDs").val();
    var origTableName = $("#form_origTable").val();
    var destDsId = $("#form_destDs").val();
    var destTableName = $("#form_destTable").val();
    if (origDsId == destDsId) {
        bootbox.alert("数据库相同");
    }

    var progressDialog = bootbox.dialog({
        message: '<p><i class="fa fa-spin fa-spinner"></i> 保存中...</p>'
    });

    $.ajax({
        url: basePath + "/exchange/interfaceModel/save.do",
        dataType: 'json',
        type: "POST",
        data:{"origDsId":origDsId,"origTableName":origTableName,"destDsId":destDsId,"destTableName":destTableName},
        success: function (data) {
            if (data.success == true || data.success == 'true') {

                var origArray=data.result.orig;
                for (var i=0;i<origArray.length;i++){
                    if (i==origArray.length-1){
                        orig+=origArray[i]+":"+origArray[i];
                    }else{
                        orig+=origArray[i]+":"+origArray[i]+";";
                    }
                }
                console.log(orig);
                var destAray=data.result.dest;
                for (var i=0;i<destAray.length;i++){
                    if (i==destAray.length-1){
                        dest+=destAray[i]+":"+destAray[i];
                    }else{
                        dest+=destAray[i]+":"+destAray[i]+";";
                    }
                }
                console.log(dest);
                modelCode=data.result.modelCode;
                initGrid();
                progressDialog.modal('hide');

            }
        }
    });
}
function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url:basePath+"/exchange/interfaceModel/listDtl.do?modelCode="+modelCode,
        datatype: "json",
        colModel: [
            {name: 'id', label: 'id', hidden: true, width: 40},
            {name: 'modelCode', label: 'modelCode', hidden: true, width: 40},
            {name: 'origField', label: '源字段名', editable: true, width: 200,
               edittype:"select",editoptions:{ value: orig}
                },
            {name: 'origDescribe', label: '描述', editable: true, width: 200},
            {name: 'destField', label: '目标字段名', editable: true, width: 200,
                edittype:"select",editoptions:{ value: dest}
                },
            {name: 'destDescribe', label: '描述', editable: true, width: 200},
            {name: '', label: '操作', editable: false, width: 180,
                formatter: function (cellValue, options, rowObject) {

                    var html;
                    html = "<a style='margin-left: 20px' href='javascript:void(0);' onclick=deleteColumn('"+options.rowId+"')><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";
                    html += "<a style='margin-left: 20px' href='javascript:void(0);' onclick=editColumn('"+options.rowId+"')><i class='ace-icon fa fa-edit' title='编辑'></i></a>";
                    html += "<a style='margin-left: 20px' href='javascript:void(0);' onclick=cancleEdit('"+options.rowId+"')><i class='ace-icon fa fa-undo' title='取消'></i></a>";
                    html += "<a style='margin-left: 20px' href='javascript:void(0);' onclick=saveColumn('"+options.rowId+"')><i class='ace-icon fa fa-save' title='保存'></i></a>";
                    return html;

                }
            }
        ],
        viewrecords: true,
        rownumbers: true,
        altRows: true,
        // rowNum: 20,
        // rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        sortname: 'id',
        sortorder: "desc",
        shrinkToFit: false,
        autoScroll: false,
        autowidth: true
    });

    $("#grid").jqGrid('navGrid', "#grid-pager",
        {
            edit: false,
            add: true,
            addicon: "ace-icon fa fa-plus",
            addfunc: function () {
                addColumn();
            },
            del: false,
            search: false,
            refresh: false,
            view: false
        });
    rowNo=$("#grid").jqGrid("getRowData").length;

}
function addColumn() {
    rowNo+=1;
    $("#grid").jqGrid("addRowData",rowNo, {modelCode:modelCode}, rowNo);
}
function editColumn(rowId) {
    $("#grid").editRow("" + rowId);
}
function saveColumn(rowId) {
    $("#grid").saveRow("" + rowId);
    var row = $("#grid").jqGrid('getRowData',rowId+"");
    $.ajax({
        url:basePath+"/exchange/interfaceModel/saveDtl.do",
        dataType: 'json',
        type: "POST",
        data:{modelCode:row.modelCode,origField:row.origField,origDescribe:row.origDescribe,destField:row.destField,destDescribe:row.destDescribe},
        success: function (data) {
            if (data.success == true || data.success == 'true') {
                $("#grid").trigger("reloadGrid");
            }else{
                bootbox.alert("保存失败")
            }
        }
    })
}
function cancleEdit(rowId) {
    $("#grid").restoreRow("" + rowId);
}
function deleteColumn(rowId){
    $("#grid").saveRow("" + rowId);
    var row = $("#grid").jqGrid('getRowData',rowId+"");
    $.ajax({
        url:basePath+"/exchange/interfaceModel/deleteDtl.do?id="+row.id,
        dataType: 'json',
        type: "POST",
        success: function (data) {
            if (data.success == true || data.success == 'true') {
                $("#grid").trigger("reloadGrid");
            }else{
                bootbox.alert("删除失败")
            }
        }
    })
}