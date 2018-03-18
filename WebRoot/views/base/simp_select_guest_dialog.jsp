<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/7/24
  Time: 11:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="modal_guest_search_table" class="modal fade" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header no-padding">
                <div class="table-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        <span class="white">&times;</span>
                    </button>
                    客户查询
                </div>
            </div>

            <div class="modal-body no-padding">
                <div class="widget-body">

                    <div class="widget-main" id="search_unit_Panel">
                        <form class="form-horizontal" role="form" id="search_guest_Form"
                              onkeydown="if(event.keyCode==13)return false;">
                            <div class="form-group">
                                <label class="col-xs-2 control-label text-right" for="search_guest">客户</label>
                                <div class="col-xs-8">
                                    <input class="col-xs-4 form-control" id="search_guest"
                                           name="filter_LIKES_name_OR_tel"
                                           type="text" placeholder="输入名称或电话"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-xs-4 col-xs-offset-4">
                                    <button type="button" class="btn btn-sm btn-primary" onclick="search_Guest()">
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
                <table id="guestSelect_Grid"></table>
                <div id="guestSelect_Page"></div>
            </div>

            <div class="modal-footer no-margin-top">
                <a href="#" class="btn" onclick="closeGuestDialog()">关闭</a>
                <a href="#" class="btn btn-primary" onclick="selectGuest();">确定</a>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
<script>
    var url;

    function search_Guest() {
        var serializy = $("#search_guest_Form").serializeArray();
        var param = array2obj(serializy);
        $("#guestSelect_Grid").jqGrid('setGridParam', {
            url: url,
            page: 1,
            postData: param
        });
        $("#guestSelect_Grid").trigger("reloadGrid");
    }

    function openSelectGuestDialog(customsId, customsName, dialogOpenPage) {
        $("#modal_guest_search_table").data("customsId", customsId);
        $("#modal_guest_search_table").data("customsName", customsName);
        $("#modal_guest_search_table").on('show.bs.modal', function () {
            initGuestSelect_Grid(dialogOpenPage);
        }).modal("show");
        guestGridReload();
    }


    function initGuestSelect_Grid(dialogOpenPage) {
        if (dialogOpenPage === "transferOrderOrig" || dialogOpenPage === "transferOrderUnit") {
            url = basePath + "/unit/page.do?filter_INI_type=1,2,4&filter_EQI_status=1";
            $("#guestSelect_Grid").jqGrid({
                height: "auto",
                url: url,
                mtype: "POST",
                datatype: "json",
                colModel: [
                    {name: 'id', label: 'id', width: 40, hidden: true},
                    {name: 'name', label: '名称', editable: true, width: 30},
                    {name: 'type', hidden: true},
                    {
                        label: '类型', editable: true, width: 30,
                        formatter: function (cellvalue, options, rowObject) {
                            if (rowObject.type == "1") {
                                return "总部";
                            } else if (rowObject.type == "2") {
                                return "代理商";
                            } else {
                                return "门店";
                            }
                        }
                    },
                    {name: 'defaultWarehId', label: '默认仓库', width: 30},
                    {name: 'tel', label: '电话', width: 50}
                ],
                viewrecords: true,
                autowidth: true,
                rownumbers: true,
                altRows: true,
                rowNum: 50,
                rowList: [10, 20, 50],
                pager: "#guestSelect_Page",
                multiselect: false,
                shrinkToFit: true,
                sortname: 'id',
                sortorder: "desc",
                ondblClickRow: function (rowId) {
                    assignGuestValue(rowId);
                    closeGuestDialog();
                }
            });
        } else if (dialogOpenPage === "transferOrderconsignmentBill") {
            url = basePath + "/sys/guest/page.do?filter_EQI_status=1";
            $("#guestSelect_Grid").jqGrid({
                height: "auto",
                url: url,
                mtype: "POST",
                datatype: "json",
                colModel: [
                    {name: 'id', label: 'id', width: 40},
                    {name: 'name', label: '名称', editable: true, width: 30},
                    {name: 'unitType', hidden: true},
                    {name: 'discount', hidden: true},
                    {
                        label: '客户类型', editable: true, width: 30,
                        formatter: function (cellvalue, options, rowObject) {
                            if (rowObject.unitType == "CT-AT") {
                                return "省代客户";
                            } else if (rowObject.unitType == "CT-ST") {
                                return "门店客户";
                            } else {
                                return "零售客户";
                            }
                        }
                    },
                    {name: 'defaultWarehId', label: '默认仓库', width: 30},
                    {name: 'tel', label: '电话', width: 50}
                ],
                viewrecords: true,
                autowidth: true,
                rownumbers: true,
                altRows: true,
                rowNum: 20,
                rowList: [10, 20, 50],
                pager: "#guestSelect_Page",
                multiselect: false,
                shrinkToFit: true,
                sortname: 'id',
                sortorder: "desc",
                ondblClickRow: function (rowId) {
                    assignGuestValue(rowId);
                    closeGuestDialog();
                }
            });
        }
        else {
            url = basePath + "/sys/guest/page.do?filter_EQI_status=1";
            $("#guestSelect_Grid").jqGrid({
                height: "auto",
                url: url,
                mtype: "POST",
                datatype: "json",
                colModel: [
                    {name: 'id', label: 'id', width: 40},
                    {name: 'name', label: '名称', editable: true, width: 30},
                    {name: 'unitType', hidden: true},
                    {name: 'discount', hidden: true},
                    {
                        label: '客户类型', editable: true, width: 30,
                        formatter: function (cellvalue, options, rowObject) {
                            if (rowObject.unitType == "CT-AT") {
                                return "省代客户";
                            } else if (rowObject.unitType == "CT-ST") {
                                return "门店客户";
                            } else {
                                return "零售客户";
                            }
                        }
                    },
                    {name: 'defaultWarehId', label: '默认仓库', width: 30},
                    {name: 'tel', label: '电话', width: 50}
                ],
                viewrecords: true,
                autowidth: true,
                rownumbers: true,
                altRows: true,
                rowNum: 50,
                rowList: [10, 20, 50],
                pager: "#guestSelect_Page",
                multiselect: false,
                shrinkToFit: true,
                sortname: 'id',
                sortorder: "desc",
                ondblClickRow: function (rowId) {
                    assignGuestValue(rowId);
                    closeGuestDialog();
                }
            });
        }

        var parent_column = $("#guestSelect_Grid").closest('.modal-dialog');
        $("#guestSelect_Grid").jqGrid('setGridWidth', parent_column.width() - 2);
    }

    function guestGridReload() {
        $("#guestSelect_Grid").clearGridData();
        $("#guestSelect_Grid").jqGrid('setGridParam', {
            url: basePath + "/sys/guest/page.do"
        }).trigger("reloadGrid");
    }

    function selectGuest() {
        var rowId = $("#guestSelect_Grid").jqGrid("getGridParam", "selrow");
        assignGuestValue(rowId);
        closeGuestDialog();
    }

    function assignGuestValue(rowId) {
        var row = $("#guestSelect_Grid").jqGrid("getRowData", rowId);
        var customsId = $("#modal_guest_search_table").data("customsId");
        var customsName = $("#modal_guest_search_table").data("customsName");
        $(customsId).val(row.id);
        $(customsName).val(row.name);
    }

    function closeGuestDialog() {
        $("#modal_guest_search_table").modal('hide');
    }

</script>

