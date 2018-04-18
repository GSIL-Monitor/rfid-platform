<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/7/26
  Time: 14:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="modal_class1_search_table" class="modal fade" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header no-padding">
                <div class="table-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        <span class="white">&times;</span>
                    </button>
                   厂家查询
                </div>
            </div>

            <div class="modal-body no-padding">
                <div class="widget-body">

                    <div class="widget-main" id="search_unit_Panel">
                        <form class="form-horizontal" role="form" id="search_vendor_Form">
                            <div class="form-group">
                                <label class="col-xs-2 control-label text-right" for="search_vendor">厂家</label>
                                <div class="col-xs-8">
                                    <input class="col-xs-4 form-control" id="search_vendor"
                                           name="filter_LIKES_name_OR_code"
                                           type="text" placeholder="输入名称"/>
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
                <table id="class1Select_Grid"></table>
                <div id="class1Select_Page"></div>
            </div>
            <div class="modal-footer no-margin-top">
                <a href="#" class="btn" onclick="closeClass1Dialog()">关闭</a>
                <a href="#" class="btn btn-primary" onclick="selectClass1();">确定</a>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
<script>
    function search_Vendor() {
        var serializy = $("#search_vendor_Form").serializeArray();
        var param = array2obj(serializy);
        $("#class1Select_Grid").jqGrid('setGridParam', {
            url: basePath + "/sys/property/findclassname.do?filter_EQS_type=C1",
            page: 1,
            postData: param
        });
        $("#class1Select_Grid").trigger("reloadGrid");
    }

    function openSelectVendorDialog(vendorId, vendorName, dialogOpenPage) {
        $("#modal_class1_search_table").data("vendorId", vendorId);
        $("#modal_class1_search_table").data("vendorName", vendorName);
        $("#modal_class1_search_table").on('show.bs.modal', function () {
            initVendorSelect_Grid();
        }).modal("show");
        class1GridReload();
    }

    function initClass1Select_Grid() {
        $("#class1Select_Grid").jqGrid({
            height: "auto",
            url: basePath + "/sys/property/findclassname.do?filter_EQS_type=C1",
            mtype: "POST",
            datatype: "json",
            colModel: [
                {name: 'id', label: 'id', width: 40},
                {name: 'name', label: '厂家', editable: true, width: 30},
                {name: 'code', label: 'CODE', width: 50}
            ],
            viewrecords: true,
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum: 10,
            rowList: [10, 20, 50],
            pager: "#class1Select_Page",
            multiselect: false,
            shrinkToFit: true,
            sortname: 'id',
            sortorder: "desc",
            ondblClickRow: function (rowId) {
                assignClaas1Value(rowId);
                closeClass1Dialog();
            }
        });

        var parent_column = $("#class1Select_Grid").closest('.modal-dialog');
        $("#class1Select_Grid").jqGrid('setGridWidth', parent_column.width() - 2);
    }

  /*  function selectClass1() {
        debugger
        var rowId = $("#class1Select_Grid").jqGrid("getGridParam", "selrow");
        assignClaas1Value(rowId);
        closeClass1Dialog();
    }*/

    function assignClaas1Value(rowId) {
        debugger;
        var row = $("#class1Select_Grid").jqGrid("getRowData", rowId);
      /*  var vendorId = $("#modal_class1_search_table").data("vendorId");
        var vendorName = $("#modal_class1_search_table").data("vendorName");*/
       /* $(vendorId).val(row.id);
        $(vendorName).val(row.name);*/
       $("#filter_eq_destunitid").val(row.code);
        $("#filter_eq_destunitname").val(row.name)
    }

    function class1GridReload() {
        $("#vendorSelect_Grid").clearGridData();
        $("#vendorSelect_Grid").jqGrid('setGridParam', {
            url: basePath + "/sys/property/findclassname.do?filter_EQS_type=C1"
        }).trigger("reloadGrid");
    }

    function closeClass1Dialog() {
        $("#modal_class1_search_table").modal('hide');
    }

</script>
