<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<div id="modal_table_wareh" class="modal fade" tabindex="-1">
	<div class="modal-dialog" style="height: 550px">
		<div class="modal-content">
			<div class="modal-header no-padding">
				<div class="table-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">
						<span class="white">&times;</span>
					</button>
					选择仓店
				</div>
			</div>

			<div class="modal-body no-padding">
				<div class="widget-body">

					<div class="widget-main" id="search_unit_Panel">
						<form class="form-horizontal" role="form"
							id="search_wareh_shop_Form">
							<div class="form-group">
								<label class="col-xs-2 text-right control-label"
									for="search_wareh_shop_code">编号</label>

								<div class="col-xs-4">
									<input class="form-control" id="search_wareh_shop_code"
										name="filter_LIKES_code" onkeyup="this.value=this.value.toUpperCase()" type="text" placeholder="模糊查询" />
								</div>
								<label class="col-xs-2 text-right control-label"
									for="search_wareh_shop_name">名称</label>

								<div class="col-xs-4">
									<input class="form-control" id="search_wareh_shop_name"
										name="filter_LIKES_name" type="text" placeholder="模糊查询" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-xs-2 text-right control-label"
									for="search_wareh_shop_type">类型</label>

								<div class="col-xs-4">
									<select class="chosen-select form-control"
										id="search_wareh_shop_type" name="filter_EQI_type"
										data-placeholder="">
										<option value="">-请选择-</option>
										<option value="4">门店</option>
										<option value="9">仓库</option>
										<option value="6">样衣间</option>
									</select>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-offset-5 col-sm-10">
									<button type="button" class="btn btn-sm btn-primary"
										onclick="_searchUnit()">
										<i class="ace-icon fa fa-search"></i> <span class="bigger-110">查询</span>
									</button>
									<button type="reset" class="btn btn-sm btn-warning">
                                    	<i class="ace-icon fa fa-undo"></i>
                                    <span class="bigger-110">清空</span></button>
                                </div>
								</div>
							</div>


						</form>
					</div>
				</div>
				<table id="unit_Select_Grid"></table>
				<div id="unit_Select_Page"></div>
			</div>

			<div class="modal-footer no-margin-top">
				<a href="#" class="btn" onclick="_closeUnitSelectDialog()">关闭</a> <a
					href="#" class="btn btn-primary" onclick="selectedUnit()">确定</a>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<script>
		<%--function initSearchType() {--%>
		    <%--$.ajax({--%>
		        <%--url : basePath + "/sys/property/searchByType.do?type=ST",--%>
		        <%--cache : false,--%>
		        <%--async : false,--%>
		        <%--success : function (data,textStatus){--%>
		            <%--var json= data;--%>
		            <%--for(var i=0;i<json.length;i++){--%>
		                <%--$("search_wareh_shop_type").append("<option value='"+json[i].code+"'>"+json[i].name+"</option>");--%>
		                <%--$("#search_wareh_shop_type").trigger('chosen:updated');--%>
		            <%--}--%>
		        <%--}--%>
		    <%--})--%>
		<%--}--%>
	var ownerIdOfWS="";
	function _searchUnit() {
		var serializeArray = $("#search_wareh_shop_Form").serializeArray();
		var params = array2obj(serializeArray);
		params.filter_EQS_ownerId=ownerIdOfWS;
		$("#unit_Select_Grid").jqGrid('setGridParam', {
//            url : basePath + "/unit/page.do?filter_INI_type=4,9,6",
			page : 1,
			postData : params
		}).trigger("reloadGrid");
//		$("#unit_Select_Grid").trigger("reloadGrid");
	}

	function _openUnitDialog(viewId,viewTextId,ownerIdWS) {
		$("#modal_table_wareh").data("viewId", viewId);
		$("#modal_table_wareh").data("viewTextId", viewTextId);
        ownerIdOfWS=ownerIdWS;
		$("#modal_table_wareh").on(
				'show.bs.modal',
				function() {
					$("#unit_Select_Grid").jqGrid(
							{
								height : '250px',
                                url : basePath + "/unit/page.do?filter_INI_type=4,9,6",
								datatype : "json",
						        mtype : "POST",
								colModel : [ {
									name : 'id',
									label : 'id',
									hidden : true,
									width : 40
								}, {
									name : 'code',
									label : '编号',
									editable : true,
									width : 20
								}, {
									name : 'name',
									label : '名称',
									editable : true,
									width : 40
								}, {
									name : 'type',
									label : '组织类型',formatter:
										function(rowValue,option,rowObject){
											if(rowValue==4)
											    return "门店";
											else if(rowValue==9){
											    return "仓库";
											} else if(rowValue==6){
											    return"样衣间";
											}
										},
									editable : true,
									width : 20
								}],
								viewrecords: true,
					            autowidth: true,
					            rownumbers: true,
					            altRows: true,
								rowNum : 20,
								rowList : [ 20, 50 ],
								pager : "#unit_Select_Page",
								multiselect : false,
								shrinkToFit : true,
								sortname : 'createTime',
								sortorder : "desc",
								ondblClickRow : function(rowid) {
									var rowId = $("#unit_Select_Grid").jqGrid(
											"getGridParam", "selrow");
									var row = $("#unit_Select_Grid").jqGrid(
											"getRowData", rowid);
									$(viewId).val(row.code);
									$(viewTextId).val(row.name);
									$("#modal_table_wareh").modal('hide');
								}

							});
					var parent_column = $("#unit_Select_Grid").closest(
							'.modal-dialog');
					$("#unit_Select_Grid").jqGrid('setGridWidth',
							parent_column.width() - 2);

				}).modal("show");
		setTimeout( _searchUnit(),500);
        _searchUnit();
//		initSearchType();
	}
	function _closeUnitSelectDialog() {
		$("#modal_table_wareh").modal('hide');
	}
	function selectedUnit() {
		var rowId = $("#unit_Select_Grid").jqGrid("getGridParam", "selrow");
		var row = $("#unit_Select_Grid").jqGrid('getRowData', rowId);
		var viewId = $("#modal_table_wareh").data("viewId");
		var viewTextId = $("#modal_table_wareh").data("viewTextId");
		$(viewId).val(row.code);
		$(viewTextId).val(row.name);
		$("#modal_table_wareh").modal('hide');
	}
</script>
