<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="modal_unit_wwner_table" class="modal fade" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header no-padding">
                <div class="table-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        <span class="white">&times;</span>
                    </button>
                    选择所属方
                </div>
            </div>

            <div class="modal-body no-padding">
                <div class="widget-body">

                    <div class="widget-main" id="search_unit_Panel">
                        <form class="form-horizontal" role="form" id="search_unit_Form">
                            <div class="form-group">
                                <label class="col-xs-2 control-label text-right" for="search_unit_code">编号</label>

                                <div class="col-xs-4">
                                    <input class="form-control" id="search_unit_code" name="filter_LIKES_code"
                                           onkeyup="this.value=this.value.toUpperCase()"
                                           type="text"
                                           placeholder="模糊查询"/>
                                </div>
                                <label class="col-xs-2 control-label text-right" for="search_unit_name">名称</label>

                                <div class="col-xs-4">
                                    <input class="form-control" id="search_unit_name" name="filter_LIKES_name"
                                           type="text" placeholder="模糊查询"/>
                                </div>


                            </div>
                            <div class="form-group" id="group_type">
                                <label class="col-xs-2 control-label text-right" for="search_unit_type">类型</label>

                                <div class="col-xs-4">
                                    <select class="chosen-select form-control" id="search_unit_type"
                                            name="filter_EQI_type" data-placeholder="">
                                        <option value="">-组织类型-</option>
                                        <option value="1">品牌商</option>
                                        <option value="2">代理商</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-xs-4 col-xs-offset-4">
                                    <button type="button" class="btn btn-sm btn-primary" onclick="search_Unit()">
                                        <i class="ace-icon fa fa-search"></i>
                                        <span class="bigger-110">查询</span>
                                    </button>
                                    <button type="reset" class="btn btn-sm btn-warning">
                                        <i class="ace-icon fa fa-undo"></i>
                                        <span class="bigger-110">清空</span></button>
                                </div>

                            </div>

                        </form>

                    </div>
                </div>
                <table id="unitSelect_Grid"></table>
                <div id="unitSelect_Page"></div>
            </div>

            <div class="modal-footer no-margin-top">
                <a href="#" class="btn" onclick="closeUnitSelectDialog()">关闭</a>
                <a href="#" class="btn btn-primary" onclick="selected_Owner_Unit();">确定</a>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
<script>
    function search_Unit() {
        var serializy = $("#search_unit_Form").serializeArray();
        var param = array2obj(serializy);
        $("#unitSelect_Grid").jqGrid('setGridParam', {
//            url:basePath+"/unit/page.do?filter_INI_type="+constant.unitType.Agent+","+constant.unitType.Headquarters,
            page: 1,
            postData: param,
        });
        $("#unitSelect_Grid").trigger("reloadGrid");
    }
    /**分别为第一赋值框Id(viewId)、第二赋值框Id(viewTextId)、回调方法(callbackfun)、所属方类型(owner)
     所属方类型仅有两种，
     第一种为"agent"-> 固定数据源为代理商，隐藏类型栏
     第二种为"head" -> 固定数据源为品牌商，隐藏类型栏
     第三种为“withSample” ->固定数据源为品牌商、代理商以及样衣间
     第四种为“withShop”  ->固定数据源为品牌商、代理商以及门店
     其余情况为以上一，二种数据源集合并且显示分类栏
     */
    function openUnitDialog(viewId, viewTextId, callbackFun, owner) {
        $("#modal_unit_wwner_table").data("viewId", viewId);
        $("#modal_unit_wwner_table").data("viewTextId", viewTextId);
        $("#modal_unit_wwner_table").data("callbackFun", callbackFun);

        var url = basePath + "/unit/page.do?filter_INI_type=";

        if (owner == "agent") {
            $("#group_type").hide();
            $("#search_unit_type option[value='4']").remove();
            $("#search_unit_type option[value='6']").remove();
            url += constant.unitType.Agent;
        } else if (owner == "head") {
            $("#group_type").hide();
            $("#search_unit_type option[value='4']").remove();
            $("#search_unit_type option[value='6']").remove();
            url += constant.unitType.Headquarters;
        } else if (owner == "withSample") {
            $("#search_unit_type option[value='4']").remove();
            url += constant.unitType.Headquarters + "," + constant.unitType.Agent + "," + constant.unitType.SampleRoom;
            var temp = $("#search_unit_type").find("option[value=6]").text();
            if ("" == temp) {
                $("#search_unit_type").append("<option value='6'>样衣间</option>")
            }
        } else if (owner == "withShop") {
            $("#search_unit_type option[value='6']").remove();
            url += constant.unitType.Shop + ',' + constant.unitType.Headquarters + ',' + constant.unitType.Agent;
            var temp = $("#search_unit_type").find("option[value=4]").text();
            if ("" == temp) {
                $("#search_unit_type").append("<option value='4'>门店</option>")
            }
        } else if (owner == "Organization") {
            $("#group_type").hide();
            url += constant.unitType.Organization + "," + constant.unitType.Headquarters;
        } else if (owner == "Shop") {
            $("#group_type").hide();
            url += constant.unitType.Organization + "," + constant.unitType.Headquarters;
        } else {
            $("#group_type").show();
            $("#search_unit_type option[value='6']").remove();
            url += constant.unitType.Agent + "," + constant.unitType.Headquarters;
        }

        $("#modal_unit_wwner_table").on('show.bs.modal', function () {
            $("#unitSelect_Grid").jqGrid({
                height: "auto",
                url: url,
                mtype: "POST",
                datatype: "json",
                colModel: [
                    {name: 'id', label: 'id', hidden: true, width: 40},
                    {name: 'code', label: '编号', editable: true, width: 20},
                    {name: 'name', label: '名称', editable: true, width: 50},
                    {
                        name: 'type', label: '组织类型', editable: true, width: 15,
                        formatter: function (cellvalue, options, rowObject) {
                            if (cellvalue == constant.unitType.Agent) {
                                return "代理商";
                            } else if (cellvalue == constant.unitType.SampleRoom) {
                                return "样衣间";
                            } else if (cellvalue == constant.unitType.Headquarters) {
                                return "总部";
                            } else if (cellvalue == constant.unitType.Shop) {
                                return "门店";
                            } else if (cellvalue == constant.unitType.Warehouse) {
                                return "仓库";
                            } else if (cellvalue == constant.unitType.Organization) {
                                return "公司";
                            } else if (cellvalue == constant.unitType.Vender) {
                                return "供应商";
                            } else if (cellvalue == constant.unitType.NetShop) {
                                return "网店";
                            } else if (cellvalue == constant.unitType.Department) {
                                return "部门";
                            } else if (cellvalue == constant.unitType.Franchisee) {
                                return "加盟商";
                            } else if (cellvalue == constant.unitType.Guest) {
                                return "客户";
                            } else {
                                return "";
                            }
                        }
                    },
                    {name: 'createTime', label: '日期', width: 17}
                ],
                viewrecords: true,
                autowidth: true,
                rownumbers: true,
                altRows: true,
                rowNum: 10,
                rowList: [10, 20, 50],
                pager: "#unitSelect_Page",
                multiselect: false,
                shrinkToFit: true,
                sortname: 'createTime',
                sortorder: "desc",
                ondblClickRow: function (rowid) {
                    var row = $("#unitSelect_Grid").jqGrid("getRowData", rowid);
                    $(viewId).val(row.code);
                    $(viewTextId).val(row.name);
                    closeUnitSelectDialog();
                }

            });

            var parent_column = $("#unitSelect_Grid").closest(
                '.modal-dialog');
            $("#unitSelect_Grid").jqGrid('setGridWidth',
                parent_column.width() - 2);

        }).modal("show");

    }
    function closeUnitSelectDialog() {
        var callbackFun = $("#modal_unit_wwner_table").data("callbackFun");
        if (callbackFun != null) {
            var fn = eval(callbackFun);
            fn.call(this);
        }
        $("#modal_unit_wwner_table").modal('hide');
    }
    function selected_Owner_Unit() {
        var rowId = $("#unitSelect_Grid").jqGrid("getGridParam", "selrow");
        var row = $("#unitSelect_Grid").jqGrid('getRowData', rowId);
        var viewId = $("#modal_unit_wwner_table").data("viewId");
        var viewTextId = $("#modal_unit_wwner_table").data("viewTextId");
        $(viewId).val(row.code);
        $(viewTextId).val(row.name);
        closeUnitSelectDialog();
    }
</script>
