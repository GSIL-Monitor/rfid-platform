$(function(){
	initGrid();
});

function refresh(){
    location.reload(true);
}


function initGrid(){
    $("#grid").jqGrid({
		height:"auto",
		url:basePath + "/data/deviceRunLog/page.do",
		datatype: "json",
        colModel: [
            {name: 'deviceId', label: '设备编号', width: 150,frozen:true},
            {name: 'logDate', label: '日期',hidden:true, width: 40},
            {name: 'openPcTime', label: '开机时间', width: 100},
            {name: 'deviceIp', label: '设备IP', sortable:false,width: 150},
            {name: 'smdjHighKm', label: '扫描电机高速里程', width: 150},
            {name: 'smdjLowKm', label: '扫描电机低速里程', sortable:false,width:150},
            {name: 'sldjKm',label:'送料电机里程',sortable:false,width:150},
            {name: 'sldjKm',label:'出料电机里程',width:150},
            {name: 'qmOnCs',label:'前门开次数',sortable:false,width:150},
            {name: 'qmOffCs',label:'前门关次数',width:150},
            {name: 'hmOnCs', label:'后门开次数', width: 400},
            {name: 'hmOffCs', label:'后门关次数', width: 400},
            {name: 'qmAlarmOnCs',label:'前门开门报警次数',sortable:false,width:400},
            {name: 'qmAlarmOffCs',label:'前门关门报警次数',width:400},
            {name: 'hmAlarmOnCs', label:'后门开门报警次数', width: 400},
            {name: 'hmAlarmOffCs', label:'后门关门报警次数', width: 400},
            {name: 'uploadTime', label:'上传时间', width: 400}
        ],
        viewrecords: true,
        autowidth: false,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: false,
        sortname : 'logDate',
        sortorder : "asc",
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

