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
                name: "", label: "查看明细", width: 70,editable:false,sortable:false,align:"center",frozen:true,
                formatter: function (cellvalue, options, rowObject) {
                    var id=rowObject.id;
                    return "<a href='"+basePath+"/shop/shopInventory/detail.do?id="+id+"'><i class='ace-icon fa fa-list'></i></a>";
                }
            },
            {name: 'status', label: '对接状态', editable:true,align:"center",width: 70,
            	formatter: function(value, options, rowObject){
	                html='';;;;;;;
	                if(value==1){
	                    html+='<i class="fa fa-check green"></i>';
	                }else{
	                	html+='<i class="fa fa-times red"></i>';
	                }
	                	return html;
                }
            },
            {name: 'deviceId', label: '设备号', editable:true,width: 200},
            {name: 'totEpc', label: '总数量',editable:true, width: 100},
            {name: 'totStyle', label: '款数',editable:true, width: 100},
            {name: 'totSku', label: 'SKU数', width: 100},
            {name: 'origId', label: '门店', width: 100,
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
            {name: 'beginTime', label: '开始时间', width: 250},
            {name: 'endTime', label: '结束时间',editable:true, width: 250},
            {name: 'billNo', label: 'ERP单号',editable:true, width: 300},
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
        autoScroll:false,
        sortname : 'beginTime',
        sortorder : "desc"

    });
    $("#grid").jqGrid("setFrozenColumns")
}

function _search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        page : 1,
        mtype: 'POST',
        postData : params,
        url: basePath + "/shop/shopInventory/page.do?filter_INI_token=16"
       
    });
   $("#grid").trigger("reloadGrid");
}


function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");

}

function reload(){
	location.reload(true);
}

function add(){
}