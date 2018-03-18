$(function() {
	initGrid();
});

function initGrid() {
	$("#grid")
			.jqGrid(
					{
						height :  "auto",
						url : basePath + "/smart/collocat/page.do",
						datatype : "json",
						mtype:"POST",
						colModel : [
								{
									name : "",
									label : "操作",
									width : 50,
									editable : false,
									align : "center",
									formatter : function(cellvalue, options,
											rowObject) {
										var id = rowObject.id;
										return "<a href='"
												+ basePath
												+ "/smart/collocat/detail.do?id="
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
													+ basePath +"/mirror/collocat/" +pic[0] + " />";
											return html;
										}
									},
									width : 100,
									frozen : true
								},
								{
									name : "id",
									label : "套装编号",
									width : 200
								},
								{
									name : "styleIds",
									label : "关联款号",
									width : 200
								},
								{
									name : "seqNo",
									label : "序列号",
									width : 150
								},
								{
									name : "isShow",
									label : "是否展出",
									editable : false,
									align : "center",
									formatter : function(cellvalue, options,
											rowObject) {
										var isShow = cellvalue;
										if (isShow == "Y") {
											var html = '<input type="checkbox" data-role="flipswitch" checked disabled="disabled"/>';
											return html;
										} else {
											var html = '<input type="checkbox" data-role="flipswitch" disabled="disabled"/>';
											return html;
										}
									},
									width : 150
								}, {
									name : "price",
									label : "价格",
									width : 150
								}, {
									name : "remark",
									label : "备注",
									width : 400
								}, {
									name : "updater",
									label : "更新员",
									width : 150
								}, {
									name : "updateTime",
									label : "更新时间",
									width : 200
								}, ],
						viewrecords : true,
						autowidth : true,
						rownumbers : true,
						altRows : true,
						rowNum : 20,
						rowList : [ 20, 50, 100 ],
						pager : "#grid-pager",
						multiselect : false,
						shrinkToFit : false,
						sortname : 'id',
						sortorder : "desc",
						autoScroll : false
					});
	$("#grid").jqGrid("setFrozenColoumns");
}

function showcolSearchPanel() {
	$("#searchPanel").slideToggle("fast");
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

function add() {
	location.href = basePath + "/smart/collocat/add.do";
}

function edit() {
	var rowId = $("#grid").jqGrid("getGridParam", "selrow");
	if (rowId) {
		var row = $("#grid").jqGrid("getRowData", rowId);
		location.href = basePath + "/smart/collocat/edit.do?id=" + row.id;
	} else {
		bootbox.alert("请选择一项进行修改");
	}
}

function reload() {
	$("#grid").trigger("reloadGrid");
}