var searchUrl = basePath + "/stock/inventory/page.do?filter_INI_token=9";
    $(function () {
    initGrid();
});
function refresh(){
    location.reload(true);
}
function initGrid() {
    $("#grid").jqGrid({
        height:  "auto",
        url: searchUrl,
        datatype: "json",
        colModel: [
            {
                name: "", label: "查看明细", width: 70,editable:false,frozen:true,align:"center",
                formatter: function (cellvalue, options, rowObject) {
                    var id=rowObject.id;
                    return "<a href='"+basePath+"/stock/inventory/detail.do?id="+id+"'><i class='ace-icon fa fa-list'></i></a>";
                }
            },
            {name: 'id', label: '任务编号', editable:true,width: 200,frozen:true},
            {name: 'status', label: '对接状态', editable:true,width: 70,align:"center",
            	formatter: function(value, options, rowObject){
	                html=''
	                if(value==1){
	                    html+='<i class="fa fa-check green"></i>';
	                }else{
	                	html+='<i class="fa fa-times red"></i>';
	                }
	                	return html;
                }
            },
           
            {name: 'deviceId', label: '设备号', editable:true,width: 200},
            {name: 'origId', label: '仓库', editable:true,width: 200,formatter:function(cellValue, options, rowObject) {
                if(cellValue){
                	return "["+cellValue+"]"+rowObject.origName;
                }else{
                	return "";
                }
            }},
            {name: 'totEpc', label: '总数量',editable:true, width: 150},
            {name: 'totStyle', label: '总款数',editable:true, width: 150},
            {name: 'totSku', label: 'SKU数', width: 150},
            {name: 'beginTime', label: '开始时间', width: 200},
            {name: 'endTime', label: '结束时间',editable:true, width: 200},

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
        sortname : 'beginTime',
        sortorder : "desc",
        autoScroll:false

    });
    $("#grid").jqGrid("setFrozenColumns");
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


function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");

}

function add(){
}
//批量盘点
function batchInventory() {
    $("#modal_batch_dialog_Inventory").modal('show').on('hidden.bs.modal', function () {
        $("#batchDetailgrid").clearGridData();
        skuInfo=[];
        oldSkuInfo = [];
    });
    initWebSocket();
    $("#scanCodeQty").text(0);
}
//批量保存方法
function saveEPC() {
    cs.showProgressBar();
    var epcArray=[];
    $.each($("#batchDetailgrid").getDataIDs(),function (index,value) {
        var rowData=$("#batchDetailgrid").getRowData(value);
        epcArray.push(rowData);
    });
    var origId=$("#search_origId").val();
    $.ajax({
        dataType: "json",
        url: basePath+"/stock/inventory/batchInventorySave.do",
        data: {
            origId: origId,
            strEpcList: JSON.stringify(epcArray),
            rightEpcCode:rightEpcCode
        },
        type: "POST",
        success: function (msg) {
            cs.closeProgressBar();
            $("#modal_batch_dialog_Inventory").modal('hide');
            if (msg.success) {
                $.gritter.add({
                    text: msg.msg,
                    class_name: 'gritter-success  gritter-light'
                });
            } else {
                cs.closeProgressBar();
                bootbox.alert(msg.msg);
            }
        }
    });
}