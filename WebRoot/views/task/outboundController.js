$(function () {
    initGrid();
});
function refresh(){
    location.reload(true);
}
function initGrid() {
    $("#grid").jqGrid({
        height:  "auto",
        url: basePath + "/task/outbound/page.do?filter_INI_token=10,24,26,28,32",
        datatype: "json",
        colModel: [

            {name: 'id', label: '任务编号', editable:true,width: 200,frozen:true},
            {
                name: "", label: "查看明细", width: 70,editable:false,align:"center",frozen:true,sortable:false,
                formatter: function (cellvalue, options, rowObject) {
                    var id=rowObject.id;
                    return "<a href='"+basePath+"/task/outbound/detail.do?id="+id+"'><i class='ace-icon fa fa-list'></i></a>";
                }
            },
            {name: 'id', label: 'id',hidden:true, width: 40},
            {name: 'status', label: '对接状态',editable:true,align:"center",width: 70,
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
            {name: 'token', label: '出库方式', editable:true,width: 100,
                formatter:function(value,rowData,rowIndex){
                    switch(value) {
                        case 26: return "退货出库";
                        case 10: return "普通出库";
                        case 24: return "调拨出库";
                    }
                }},
            {name: 'origId', label: '出库仓库', editable:true,width: 200,
                	formatter:function(value,options,rowObject){
                        if(cs.isBlank(value)) {
                            return "";
                        }
                        return "["+value+"]"+rowObject.origName;
                    }
                },
                {name: 'destUnitId', label: '收货方', width: 200,
                	formatter:function(value,options,rowObject) {
                		if(value!=undefined&&rowObject.origName!=undefined)
                			{
                				return '['+value+']'+rowObject.destUnitName;
                			} else if(value!=undefined){
                				return value;
                			}else{
                				return '';
                			}
                	}
                },
                {name: 'destId',label:'收货仓库',width:200,sortable:false,
                	formatter:function(value,options,rowObject){
                        if(cs.isBlank(value)) {
                            return "";
                        }
                        return "["+value+"]"+rowObject.destName;
                    }
                },
            {name: 'totEpc', label: '总数量',editable:true, width: 100},
            {name: 'totCarton', label: '箱数', width: 100, sortable: true},
            {name: 'totStyle', label: '款数',editable:true, width: 100},
            {name: 'totSku', label: 'SKU数', width: 100},
            {name: 'beginTime', label: '开始时间', width: 150},
            {name: 'endTime', label: '结束时间',editable:true, width: 150},
            {name: 'billNo', label: 'ERP单号',editable:true, width: 150},
            
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


function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");

}

function add(){
}