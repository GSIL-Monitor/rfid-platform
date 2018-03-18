$(function(){
    //初始化
    initGrid();
    setTimeout(function() {
        _search();
    },500);
});

function initGrid(){
    $("#grid").jqGrid({
        height:"auto",
        mtype:"POST",
//        url:basePath + "/shop/cashier/page.do",
        datatype: "json",
        colModel: [
            {name: 'name', label: '姓名', editable:true,width: 150},
            {name: 'id', label: 'id',hidden:true, width: 40,key:true},
            {name: 'type', label: 'type',hidden:true, width: 40},
            {
                name: '', label: '操作', editable: true, width: 150,align:"center",
                formatter: function (cellValue, option, rowObject) {
                    var html="";
                    html +="<a style='margin-left: 20px' href='#' onclick=setDefaultSaleStaff('"+rowObject.id+"')><i class='ace-icon fa fa-cog' title='设置默认销售员'></i></a>";


                    return html;
                }
            },
            {name: 'code', label: '编号', editable:true,width: 200},
            {name: 'phone', label: '手机号', editable:true,width: 200},
            {name: 'ownerId', label: '所属门店', sortable:false,editable:true,width: 150},
            {name: 'password',label:'密码',editable:false,width:100,hidden:true},
            {name:'unitName',label:'门店名称',sortable:false,editable:true,width:200},
            {name: 'isAdmin', label: '管理员',sortable:false,editable:true, width: 150,
            	formatter:function(value,rows,rowObject){
            		if(value=="1"){
            			return "是";
            		} else {
            			return "否";
            		}
            	}
            },
            {name: 'createDate', label: '创建时间',editable:true,width: 200},
            {name:'creatorId',label:'创建人',sortable:false,editable:true,width:200},
            {name:'remark',label:'备注',sortable:false,editable:true,width:400}
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
        sortname : 'createDate',
        sortorder : "asc",
        autoScroll:false

    });
    $("#grid").jqGrid("setFrozenColumns");
}
function add(){
	location.href=basePath+"/shop/cashier/add.do";
}
function editCashier(){
	var rowId = $("#grid").jqGrid("getGridParam", "selrow");
	$("#form_code").attr("readonly",true);
    if(rowId) {
        var row = $("#grid").jqGrid('getRowData',rowId);
        location.href=basePath+"/shop/cashier/edit.do?id="+row.id;

    } else {
        bootbox.alert("请选择一项进行修改！");
    }
}

function setDefaultSaleStaff(rowId){
    var row = $("#grid").jqGrid('getRowData', rowId);
    cs.showProgressBar("设置中");
    $.ajax({
        url: basePath + '/shop/cashier/setDefaultSaleStaff.do',
        dataType: 'json',
        data: {
            id: row.id,
            userId: userId
        },
        success: function (result) {
            cs.closeProgressBar();
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
function refresh(){
	location.reload(true);
}

function _search() {

    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);

    $("#grid").jqGrid('setGridParam', {
        url:basePath + "/shop/cashier/page.do",
        page : 1,
        postData : params
    });
    $("#grid").trigger("reloadGrid");
}
function showAdvSearchPanel(){
	 $("#searchPanel").slideToggle("fast");
}
function _clearSearch(){

}
