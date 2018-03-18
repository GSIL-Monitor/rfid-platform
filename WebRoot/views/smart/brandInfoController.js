var url=basePath + "/smart/brand/page.do"
$(function(){
	initJqGrid();
});
function initJqGrid(){
	$("#grid").jqGrid({
		url:url,
		height: "auto",
		mtype:"POST",
		datatype:"json",
		colModel:[
					{
						name : "",
						label : "操作",
						width : 50,
						editable : false,
						align : "center",
						formatter : function(cellvalue, options,
								rowObject) {
							var brand = rowObject.brand;
							return "<a href='"
									+ basePath
									+ "/smart/brand/detail.do?brand="
									+ brand
									+ "'><i class='ace-icon fa fa-list'></i></a>";
						}
					},
					{
						name : "url",
						label : "图片",
						editable : true,
						formatter : function(cellvalue, options,
								rowObject) {
							var url = rowObject.url;
							if (url == null)
								return "无图片";
							else {
								var pic = url.split(",");
								var html = "<img width=80 height=100 src="
										+ basePath +"/mirror/brand/" +pic[0] + " />";
								return html;
							}
						},
						width : 100,
						frozen : true
					},
		          {name:"brand",label:"编号",editable:true,width:200,key:true,frozen:true},
		          {name:"name",label:"名称",editable:true,width:200},
		          {name:"address",label:"品牌归属地",width:300},
		          {name:"seqNo",label:"序号",width:200},
		          {name:"designer",label:"设计师",width:300},
		          {name:"remark",label:"备注",width:600}
		          ],
		viewrecords: true,
		autowidth:true,
		rownumbers: true,
		altRows: true,
		rowNum: 20,
		rowList: [20, 50, 100],
		pager: "#grid-pager",
		multiselect: false,
		shrinkToFit: false,
		sortname : 'brand',
		sortorder : "desc",
		autoScroll:false
	});
	$("#grid").jqGrid("setFrozenColoumns");
}

function reloadBrand(){
	window.location.reload();
}

function add(){
	window.location.href=basePath+ "/smart/brand/add.do";
}

function edit(){
	var rowBrand = $("#grid").jqGrid("getGridParam","selrow");
	
	if(rowBrand)
	{
		var row =$("#grid").jqGrid("getRowData",rowBrand);
		location.href=basePath+"/smart/brand/edit.do?brand="+row.brand;
	}else
	{
		bootbox.alert("请选择一项进行修改");
		
	}
}

function showcolSearchPanel(){
	$("#searchPanel").slideToggle("fast")
}

function reload(){
	$("#grid").trigger("reloadGrid");
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

function _clearSearch() {
    $("#searchForm").resetForm();
}