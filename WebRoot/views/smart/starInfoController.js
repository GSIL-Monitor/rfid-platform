$(function(){
	initGrid();
});

function initGrid(){
	$("#grid").jqGrid({
		url:basePath+"/smart/star/page.do",
		height: "auto",
		mtype:"POST",
		datatype:"json",
		colModel:[
					{
						name : "",
						label : "操作",
						width : 100,
						editable : false,
						align : "center",
						formatter : function(cellvalue, options,
								rowObject) {
							var id = rowObject.id;
							return "<a href='"
									+ basePath
									+ "/smart/star/detail.do?id="
									+ id
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
										+ basePath +"/mirror/starInfo/" +pic[0] + " />";
								return html;
							}
						},
						width : 100,
						frozen : true
					},
				  {name:'id',label:"编号",hidden:true,width:20,kay:true},
		          {name:"starName",label:"名称",width:350},
		          {name:"isShow",label:"是否展示",width:250,formatter : function(cellvalue, options,
							rowObject) {
						var isShow = cellvalue;
						if (isShow == "Y") {
							var html = '<input type="checkbox" data-role="flipswitch" checked disabled="disabled"/>';
							return html;
						} else {
							var html = '<input type="checkbox" data-role="flipswitch" disabled="disabled"/>';
							return html;
						}
						}
		          },
		          {name:"seqNo",label:"序号",width:350},
		          ],
		viewrecords:true,
		autowidth:true,
		rownumbers:true,
		altRows:true,
		rowNum:20,
		rowList:[20,50,100],
		pager:"#grid-pager",
		multiselect:false,
		shrinkToFit:false,
		sortName:"id",
		sortorder:"asc",
		autoScroll:false
		
	});
	
}

function showcolSearchPanel() {
	$("#searchPanel").slideToggle("fast");
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

function add()
{
	location.href=basePath+"/smart/star/add.do";
}

function edit(){
	var rowId=$("#grid").jqGrid("getGridParam","selrow")
	if(rowId)
		{
			var row =$("#grid").jqGrid("getRowData",rowId);
			
			location.href=basePath+"/smart/star/edit.do?id="+row.id;
		}
	else{
		bootbox.alert("请选择一项进行修改");
	}
	
}