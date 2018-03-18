$(function () {
    initGrid();
    setTimeout(function() {
        _search();
    },500);
});

function initGrid() {

    $("#grid").jqGrid({
        height: "auto",
        datatype: "json",
        colModel: [

            {name: 'id', label: '任务编号', editable:true,width: 200,frozen:true},
            {
                name: "", label: "查看明细", width: 70,sortable:false,editable:false,align:"center",
                formatter: function (cellvalue, options, rowObject) {
                    var id=rowObject.id;
                    return "<a href='"+basePath+"/shop/shopReceive/detail.do?id="+id+"'><i class='ace-icon fa fa-list'></i></a>";
                }
            },
            {name: 'status', label: '对接状态', editable:true,align:"center",width: 70,
            	formatter: function(value, options, rowObject){
	                html='';;;;;;;;;
	                if(value==1){
	                    html+='<i class="fa fa-check green"></i>';
	                }else{
	                	html+='<i class="fa fa-times red"></i>';
	                }
	                	return html;
                }
            },
            {name: 'deviceId', label: '设备号', editable:true,width: 200},
            {name: 'token', label: '入库方式', editable:true,width: 100,
            	formatter: function (cellvalue, options, rowObject) {
                    if(cellvalue==14){
                    	return "收货入库";
                    }else if(cellvalue==17){
                    	return "销售退货";
                    }else if(cellvalue==19){
                    	return "调拨入库";
                    }
                }
            },
            {name: 'totEpc', label: '总数量',editable:true, width: 100},
            {name: 'totStyle', label: '款数',editable:true, width: 100},
            {name: 'totSku', label: 'SKU数', width: 100},
            {name: 'destId', label: '门店', width:100,
            	formatter:function(value,options,rowObject) {
            		if(value!=undefined&&rowObject.destName!=undefined)
            			{
            				return '['+value+']'+rowObject.destName;
            			} else if(value!=undefined){
            				return value;
            			}else{
            				return '';
            			}
            	}
            },
            {name: 'origUnitId', label: '发货方', width: 100,
            	formatter:function(value,options,rowObject) {
            		if(value!=undefined&&rowObject.origUnitName!=undefined)
            			{
            				return '['+value+']'+rowObject.origUnitName;
            			} else if(value!=undefined){
            				return value;
            			}else{
            				return '';
            			}
            	}
            },
            {name: 'origId',label:'发货仓库',width:100,
            	formatter:function(value,options,rowObject) {
            		if(value!=undefined&&rowObject.origName!=undefined)
            			{
            				return '['+value+']'+rowObject.origName;
            			} else if(value!=undefined){
            				return value;
            			}else{
            				return '';
            			}
            	}
            },
            {name: 'beginTime', label: '开始时间', width: 200},
            {name: 'endTime', label: '结束时间',editable:true, width: 200},
            {name: 'billNo', label: 'ERP单号',editable:true, width: 200},
            {name: 'remark', label: '备注',editable:true, width: 200}

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
        postData : params,
        url: basePath + "/shop/shopReceive/page.do?filter_INI_token=17,19,14"
    });
    $("#grid").trigger("reloadGrid");
}


function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");

}

function add(){
}