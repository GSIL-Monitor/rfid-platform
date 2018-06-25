<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/7/26
  Time: 14:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="modal_vendor_search_table" class="modal fade" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header no-padding">
                <div class="table-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        <span class="white">&times;</span>
                    </button>
                    供应商查询
                </div>
            </div>

            <div class="modal-body no-padding">
                <div class="widget-body">

                    <div class="widget-main" id="search_unit_Panel">
                        <form class="form-horizontal" role="form" id="search_vendor_Form">
                            <div class="form-group">
                                <label class="col-xs-2 control-label text-right" for="search_vendor">供应商</label>
                                <div class="col-xs-8">
                                    <input class="col-xs-4 form-control" id="search_vendor"
                                           name="filter_LIKES_name_OR_tel"
                                           type="text" placeholder="输入名称或电话"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-xs-4 col-xs-offset-4">
                                    <button type="button" class="btn btn-sm btn-primary" onclick="search_Vendor()">
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
                <table id="vendorSelect_Grid"></table>
                <div id="vendorSelect_Page"></div>
            </div>
            <div class="modal-footer no-margin-top">
                <div id="searchVendorDialog_buttonGroup"/>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
<script>
    function search_Vendor() {
        debugger;
        var serializy = $("#search_vendor_Form").serializeArray();
        var param = array2obj(serializy);
        $("#vendorSelect_Grid").jqGrid('setGridParam', {
            url: basePath + "/unit/page.do?filter_EQI_type=0",
            page: 1,
            postData: param,
        });
        $("#vendorSelect_Grid").trigger("reloadGrid");
    }

    function initVendorSelect_Grid() {
        $("#vendorSelect_Grid").jqGrid({
            height: "auto",
            url: basePath + "/unit/page.do?filter_EQI_type=0",
            mtype: "POST",
            datatype: "json",
            colModel: [
                {name: 'id', label: 'id', width: 40},
                {name: 'name', label: '供应商', editable: true, width: 30},
                {name: 'tel', label: '电话', width: 50}
            ],
            viewrecords: true,
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum: 10,
            rowList: [10, 20, 50],
            pager: "#vendorSelect_Page",
            multiselect: false,
            shrinkToFit: true,
            sortname: 'id',
            sortorder: "desc",
            ondblClickRow: function (rowid) {
                debugger;
                if (dialogOpenPage === "purchaseOrder") {
                    confirm_selected_VendorId_purchaseOrder(prefixId);
                } else if (dialogOpenPage === "purchaseReturn") {
                    confirm_selected_VendorId_purchaseReturn();
                }else if(dialogOpenPage==="purchaseOrderSearch"){
                    confirm_selected_VendorId_purchaseOrder_search();
                }else if(dialogOpenPage==="meregegridDialog"){
                    confirm_selected_VendorId_purchaseOrders(saveid);
                }

                closeSearchGuestDialog();
            }
        });

        var parent_column = $("#vendorSelect_Grid").closest('.modal-dialog');
        $("#vendorSelect_Page_left").css("width","2px");
        $("#vendorSelect_Grid").jqGrid('setGridWidth', parent_column.width() - 2);
    }
    function confirm_selected_VendorId_purchaseOrder(prefixId) {
        var rowId = $("#vendorSelect_Grid").jqGrid("getGridParam", "selrow");
        var rowData = $("#vendorSelect_Grid").jqGrid('getRowData', rowId);
        if (prefixId=="edit"){
            $("#edit_origUnitId").val(rowData.id);
            $("#edit_origUnitName").val(rowData.name);
        }else {
            $("#search_origUnitId").val(rowData.id);
            $("#search_origUnitName").val(rowData.name);
        }
        $("#modal_vendor_search_table").modal('hide');
    }
    function confirm_selected_VendorId_purchaseReturn() {
        var rowId = $("#vendorSelect_Grid").jqGrid("getGridParam", "selrow");
        var rowData = $("#vendorSelect_Grid").jqGrid('getRowData', rowId);
        $("#search_destUnitId").val(rowData.id);
        $("#search_destUnitName").val(rowData.name);
        $("#modal_vendor_search_table").modal('hide');
    }
    function closeSearchGuestDialog() {
        $("#modal_vendor_search_table").modal('hide');
    }

</script>
