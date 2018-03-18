$(function () {
    initGrid();
});
function refresh(){
    location.reload(true);
}
function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: basePath + "/task/inbound/page.do?filter_INI_token=8,23,25,29",
        datatype: "json",
        colModel: [

            {name: 'id', label: '任务编号', editable:true,width:200,frozen:true},
            {
                name: "", label: "查看明细", width: 70,editable:false,align:"center",frozen:true,sortable:false,
                formatter: function (cellvalue, options, rowObject) {
                    var id=rowObject.id;
                    return "<a href='"+basePath+"/task/inbound/detail.do?id="+id+"'><i class='ace-icon fa fa-list'></i></a>";
                },frozen:true
            },
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
            {name: 'deviceId', label: '设备号', editable:true,width: 100},
            {name: 'token', label: '入库方式', editable:true,width: 100,
                formatter:function(value,options,rowObject){
                    switch(value) {
                        case 8: return "采购入库";
                        case 23: return "退货入库";
                        case 25: return "调拨入库";
                        case 29: return "调整入库";
                    }
                }},
            {name: 'destId', label: '入库仓库', editable:true,width: 200,
                formatter:function(value,options,rowObject){
                    if(cs.isBlank(value)) {
                        return "";
                    }
                    return "["+value+"]"+rowObject.destName;
                }},
            {name: 'origUnitId', label: '发货方', width: 200,
                formatter:function(value,options,rowObject){
                    if(cs.isBlank(value)) {
                        return "";
                    }
                    return "["+value+"]"+rowObject.origUnitName;
                }},
            {name: 'origId', label: '发货仓库', width: 200,
                	formatter:function(value,options,rowObject) {
                		if(value!=undefined&&rowObject.origName!=undefined){
                			return '['+value+']'+rowObject.origName;
                		} else if (value!=undefined){
                			return value;
                		} else {
                			return '';
                		}
                	}
                
            },
            {name: 'totEpc', label: '总数量',editable:true, width: 100},
            {name: 'totCarton', label: '箱数', width: 100},
            {name: 'totStyle', label: '款数',editable:true, width: 100},
            {name: 'totSku', label: 'SKU数', width: 100},

            {name: 'beginTime', label: '开始时间', width: 150},
            {name: 'endTime', label: '结束时间',editable:true, width: 150},
            {name: 'billNo', label: 'ERP单号',editable:true, width: 150},
            {name: 'remark', label: '备注',editable:true, width: 400}

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
        page : 1,
        postData : params
    });
    $("#grid").trigger("reloadGrid");
}
function _clearSearch(){
	$("#searchForm").resetForm();
}

function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");

}

function add(){
}