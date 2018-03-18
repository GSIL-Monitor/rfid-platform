var searchUrl = basePath + "/logistics/staticsInandOut/page.do";
$(function () {
    initDate();
    initSelectOrigForm();
    initGrid();


    //initSearchType();

});
var vendorStatus;
function refresh(){
    location.reload(true);
}
function initDate(){
    debugger;
    var startDate = getMonthFirstDay("yyyy-MM-dd");
    var endDate = getToDay("yyyy-MM-dd");
    $('.startDate').datepicker('setDate', startDate);
    $('.endDate').datepicker('setDate', endDate);
}
/*function initSearchType() {
    $.ajax({
        url : basePath + "/logistics/staticsInandOutmonth/searchByType.do",
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
}*/
function initSelectOrigForm() {
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + curOwnerId,
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_warehId").empty();
            $("#search_warehId").append("<option value='' style='background-color: #eeeeee'>--请选择出库仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_warehId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                $("#search_warehId").trigger('chosen:updated');
            }
        }
    });
}

function initGrid() {
    //var parent_column = $("#main-container");
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
//    $("#grid").jqGrid( 'setGridWidth', parent_column.width()-5);
    $("#grid").jqGrid({
        height: "auto",
        url: searchUrl,
        datatype: "json",
        postData: params,
        mtype:"POST",
        colModel: [
            {name: 'id', label: 'id',hidden:true, width: 40,frozen:true},
            {name: 'groupId', label: '编号',editable:true, width: 200,frozen:true},
            {name: 'billno', label: '单号', editable:true,width: 200},
            {name: 'time', label: '时间', editable:true,width: 200},
            {name: 'typeinout', label: '类型', editable:true,width: 200},
            {name: 'styleName', label: '风格', editable:true,width: 200,hidden:true},
            {name: 'colorName',label:'颜色',editable:true,width:200},
            {name: 'sizeName', label: '尺寸',editable:true, width: 200},
            {name: 'outQty', label: '出库量',editable:true,sortable:false, width: 200},
            {name: 'outpreval', label: '出库成本价', sortable:false,width: 200},
            {name: 'outprevalNum', label: '出库成本单价', sortable:false,width: 200},
            {name: 'inQty', label: '入库量',editable:true, width: 200},
            {name: 'inpreval', label: '入库成本价', sortable:false,width: 200},
            {name: 'inprevalNum', label: '入库成本单价', sortable:false,width: 200},
            {name: 'transferOutQty', label: '调拨出库量', width: 200},
            {name: 'transferOutpreval', label: '调拨出库成本价', sortable:false,width: 200},
            {name: 'transferOutprevalNum', label: '调拨出库成本单价', sortable:false,width: 200},
            {name: 'transferInQty', label: '调拨入库量', width:200},
            {name: 'transferInpreval', label: '调拨入库成本价', sortable:false,width: 200},
            {name: 'transferInprevalQtyNum', label: '调拨入库成本单价', sortable:false,width: 200},
            {name: 'totpreval', label: '累计库存价', sortable:false,width: 200}
           /* {name: 'price', label: '价格', sortable:false,width: 200}*/
        ],
        autowidth: true,
        viewrecords: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        sortname : 'id',
        sortorder : "desc",
        shrinkToFit:true,
        autoScroll:false,
        grouping:true,
        groupingView : {
            groupField : ['groupId'],
            groupColumnShow : [false],
            groupText : ['<b>{0}</b>'],
            plusicon: 'ace-icon fa fa-plus',
            minusicon: 'ace-icon fa fa-minus'
        },
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
    debugger;
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

