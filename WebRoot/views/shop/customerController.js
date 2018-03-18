$(function(){
	//初始化
	initGrid();
/*    setTimeout(function() {
        _search();
    },500);*/
});

function initGrid(){
	$("#grid").jqGrid({
		height:"auto",
		url:basePath + "/shop/customer/page.do",
		mtype:"POST",
		datatype: "json",
        colModel: [
            {name: 'name', label: '姓名', editable:true,width: 40,frozen:true},
            {name: 'id', label: 'id',hidden:true, width: 40},
            {name: 'status', label: 'status',hidden:true, width: 40},
            {name: 'code', label: '编号', editable:true,width: 40},
            {
                name: '', label: '操作', editable: true, width: 150,align:"center",
                formatter: function (cellValue, option, rowObject) {
                    var html="<a style='margin-left: 20px;' href='" + basePath + "/shop/customer/edit.do?id=" + rowObject.id + "'>"
                        +    "<i class ='fa  fa-edit' aria-hidden='true' title='编辑'></i>"
                        +" </a>";
                   if(rowObject.status == 1){
                        html += "<a style='margin-left: 20px' href='#' onclick=changeStatus('"+rowObject.id+"',0)><i class='ace-icon fa fa-lock' title='废除'></i></a>";
                    } else{
                        html += "<a style='margin-left: 20px' href='#' onclick=changeStatus('"+rowObject.id+"',1)><i class='ace-icon fa fa-check' title='启用'></i></a>";
                    }
                    return html;
                }
            },
            {name: 'socialNo', label: '身份证',editable:true, width: 40},
            {name: 'job', label: '工作',editable:true, width: 40},
            {name: 'company', label: '公司',editable:true, width: 40},
            {name: 'phone', label: '电话',editable:true, width: 40},
            {name: 'createTime', label: '创建时间', editable:true,width: 40},
            {name: 'remark', label: '备注', editable:true,width: 40}
        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: true,
        sortname : 'createDate',
        sortorder : "desc",
        autoScroll:false

    });
$("#grid").jqGrid("setFrozenColumns");
}

function _search() {
	
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    
    $("#grid").jqGrid('setGridParam', {
    	url:basePath + "/shop/customer/page.do",
        page : 1,
        postData : params     
    });  
  $("#grid").trigger("reloadGrid");
}
 function refresh(){
	 location.reload(true);
 }
function _clearSearch(){
	
	}

function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");

}

function addcustomer() {
    location.href = basePath + "/shop/customer/add.do";
}

function changeStatus(rowId,status) {
    $.ajax({
        url: basePath + '/shop/customer/changeStatus.do',
        dataType: 'json',
        data: {
            id: rowId,
            status: status
        },
        success: function (result) {
            debugger;
            if (result.success) {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#grid").trigger('reloadGrid');
            } else {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-fail  gritter-light'
                });
            }
        }
    });
}
