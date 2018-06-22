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
                        <form class="form-horizontal" role="form" id="search_guest_Form" onkeydown="if(event.keyCode==13)return false;">
                            <div class="form-group">
                                <label class="col-xs-2 control-label text-right" for="search_guest">客户</label>
                                <div class="col-xs-8">
                                    <input class="col-xs-4 form-control" id="search_guest"
                                           name="filter_LIKES_name_OR_tel_OR_vipId"
                                           type="text" placeholder="输入名称或电话或VIP"/>
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
                <div id="searchGuestDialog_buttonGroup"/>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
<script>
    var url;
    $(function () {
        customerkeydowns();

    });
    function customerkeydowns() {
        //监听回车键
        $("#search_guest").keydown(function (event) {

            if (event.keyCode == 13) {
                search_Guest();
            }
        })
    }

    function search_Guest() {
        var serializy = $("#search_guest_Form").serializeArray();
        var param = array2obj(serializy);
        $("#guestSelect_Grid").jqGrid('setGridParam', {
            url: url,
            page: 1,
            postData: param,
        });
        $("#guestSelect_Grid").trigger("reloadGrid");
    }

    function initGuestSelect_Grid() {

        if (dialogOpenPage === "transferOrderOrig" || dialogOpenPage === "transferOrderUnit") {

            url ="";
            if(curOwnerId=="1"){
                url= basePath + "/unit/page.do?filter_INI_type=1,2,4&filter_EQI_status=1";
            }else{
                url = basePath + "/unit/page.do?filter_INI_type=1,2,4&filter_EQI_status=1&filter_EQS_ownerids="+ownersId;
            }
            $("#guestSelect_Grid").jqGrid({
                height: 350,
                url: url,
                mtype: "POST",
                datatype: "json",
                colModel: [
                    {name: 'id', label: 'id', width: 40,hidden:true},
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
                    {name: 'tel', label: '电话', width: 50},
                    {
                        name :"idCard",label:"等级",hidden:true
                    }
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
                loadComplete: function(data) {
                    debugger;
                    var length=data.rows.length;
                    if(length==1){
                        $("#guestSelect_Grid").setSelection(data.rows[0].id, true);
                        if (dialogOpenPage === "saleOrder"){
                            confirm_selected_GuestId_sale();
                        }else if(dialogOpenPage ==="saleOrderReturn"){
                            confirm_selected_GuestId_saleReturn();
                        }else if(dialogOpenPage === "transferOrderOrig"){
                            confirm_selected_OrigUnit();
                        }else if(dialogOpenPage === "transferOrderUnit"){
                            confirm_selected_DestUnit();
                        }else if(dialogOpenPage==="transferOrderconsignmentBill"){
                            confirm_selected_GuestId_Consignment();
                        }
                        closeSearchGuestDialog();
                    }

                },
                ondblClickRow: function (rowid) {

                    if (dialogOpenPage === "saleOrder"){
                        confirm_selected_GuestId_sale();
                    }else if(dialogOpenPage ==="saleOrderReturn"){
                        confirm_selected_GuestId_saleReturn();
                    }else if(dialogOpenPage === "transferOrderOrig"){
                        confirm_selected_OrigUnit();
                    }else if(dialogOpenPage === "transferOrderUnit"){
                        confirm_selected_DestUnit();
                    }
                    closeSearchGuestDialog();
                }
            });
        }else if(dialogOpenPage==="transferOrderconsignmentBill"){
            url ="";
            if(curOwnerId=="1"){
                url= basePath + "/sys/guest/page.do?filter_EQI_status=1";
            }else{
                url = basePath + "/sys/guest/page.do?filter_EQI_status=1&filter_EQS_ownerids="+ownersId;
            }
            $("#guestSelect_Grid").jqGrid({
                height: 350,
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
                    {name: 'unitName', label: '所属门店', width: 30},
                    {name: 'defaultWarehId', hidden: true},
                    {name: 'tel', label: '电话', width: 50},
                    {
                        name :"idCard",label:"等级",hidden:true
                    }
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
                loadComplete: function(data) {
                      debugger;
                      var length=data.rows.length;
                      if(length==1){
                          $("#guestSelect_Grid").setSelection(data.rows[0].id, true);
                          if (dialogOpenPage === "saleOrder"){
                              confirm_selected_GuestId_sale();
                          }else if(dialogOpenPage ==="saleOrderReturn"){
                              confirm_selected_GuestId_saleReturn();
                          }else if(dialogOpenPage === "transferOrderOrig"){
                              confirm_selected_OrigUnit();
                          }else if(dialogOpenPage === "transferOrderUnit"){
                              confirm_selected_DestUnit();
                          }else if(dialogOpenPage==="transferOrderconsignmentBill"){
                              confirm_selected_GuestId_Consignment();
                          }
                          closeSearchGuestDialog();
                      }
                },
                ondblClickRow: function (rowid) {

                    if (dialogOpenPage === "saleOrder"){
                        confirm_selected_GuestId_sale();
                    }else if(dialogOpenPage ==="saleOrderReturn"){
                        confirm_selected_GuestId_saleReturn();
                    }else if(dialogOpenPage === "transferOrderOrig"){
                        confirm_selected_OrigUnit();
                    }else if(dialogOpenPage === "transferOrderUnit"){
                        confirm_selected_DestUnit();
                    }else if(dialogOpenPage==="transferOrderconsignmentBill"){
                        confirm_selected_GuestId_Consignment();
                    }
                    closeSearchGuestDialog();
                }
            });
        }
        else {
            url ="";
            if(curOwnerId=="1"){
                url= basePath + "/sys/guest/page.do?filter_EQI_status=1";
            }else{
                debugger;
                url = basePath + "/sys/guest/page.do?filter_EQI_status=1&filter_EQS_ownerids="+ownersId;
            }

            $("#guestSelect_Grid").jqGrid({
                height: 350,
                url: url,
                mtype: "POST",
                datatype: "json",
                colModel: [
                    {name: 'id', label: 'id', width: 40},
                    {name: 'name', label: '名称', editable: true, width: 30},
                    {name: 'unitType', hidden: true},
                    {name: 'discount', hidden: true},
                    {name: 'owingValue', hidden: true},
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
                    {name: 'unitName', label: '所属门店', width: 30},
                    {name: 'defaultWarehId', hidden: true},
                    {name: 'tel', label: '电话', width: 50},
                    {
                        name :"idCard",label:"等级",hidden:true
                    }
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
                loadComplete: function(data) {
                    debugger;
                    var length=data.rows.length;
                    if(length==1){
                        $("#guestSelect_Grid").setSelection(data.rows[0].id, true);
                        if (dialogOpenPage === "saleOrder"){
                            confirm_selected_GuestId_sale();
                        }else if(dialogOpenPage ==="saleOrderReturn"){
                            confirm_selected_GuestId_saleReturn();
                        }else if(dialogOpenPage === "transferOrderOrig"){
                            confirm_selected_OrigUnit();
                        }else if(dialogOpenPage === "transferOrderUnit"){
                            confirm_selected_DestUnit();
                        }else if(dialogOpenPage==="transferOrderconsignmentBill"){
                            confirm_selected_GuestId_Consignment();
                        }
                        closeSearchGuestDialog();
                    }
                },
                ondblClickRow: function (rowid) {

                    if (dialogOpenPage === "saleOrder"){
                        confirm_selected_GuestId_sale();
                    }else if(dialogOpenPage ==="saleOrderReturn"){
                        confirm_selected_GuestId_saleReturn();
                    }else if(dialogOpenPage === "transferOrderOrig"){
                        confirm_selected_OrigUnit();
                    }else if(dialogOpenPage === "transferOrderUnit"){
                        confirm_selected_DestUnit();
                    }
                    closeSearchGuestDialog();
                }
            });
        }


        var parent_column = $("#guestSelect_Grid").closest('.modal-dialog');
        $("#guestSelect_Grid").jqGrid('setGridWidth', parent_column.width() - 2);
    }

    function closeSearchGuestDialog() {
        $("#modal_guest_search_table").modal('hide');
    }

    function confirm_selected_GuestId_sale() {
        var rowId = $("#guestSelect_Grid").jqGrid("getGridParam", "selrow");
        var rowData = $("#guestSelect_Grid").jqGrid('getRowData', rowId);
        $("#search_destUnitId").val(rowData.id);
        $("#search_destUnitName").val(rowData.name);
        initSelectDestForm();
        $("#search_customerType").val(rowData.unitType);
        $("#search_destId").selectpicker('val', rowData.defaultWarehId);
        $("#search_destId").selectpicker('refresh');
        var idCard = rowData.idCard;
        $.ajax({
            dataType: "json",
            url: basePath+"/shop/vipCard/findCardDiscount.do?",
            data:{
                idCard:idCard
            },
            cache: false,
            async: false,
            type: "POST",
            success: function (msg) {
                if (msg.success) {
                    var discount = msg.result;
                    if (rowData.discount>discount){
                        $("#search_discount").val(discount);
                    }else {
                        $("#search_discount").val(rowData.discount);
                    }
                } else {
                    bootbox.alert(msg.msg);
                }
            }
        });
        $("#pre_Balance").val(0-rowData.owingValue);
        $("#modal_guest_search_table").modal('hide');

        if ($("#search_destId").val() && $("#search_destId").val() != null) {
            $("#SODtl_wareHouseIn").removeAttr("disabled");
        } else {
            $("#SODtl_wareHouseIn").attr({"disabled": "disabled"})
        }
        setDiscount();
    }

    function confirm_selected_GuestId_Consignment() {
        var rowId = $("#guestSelect_Grid").jqGrid("getGridParam", "selrow");
        var rowData = $("#guestSelect_Grid").jqGrid('getRowData', rowId);
        $("#search_origUnitId").val(rowData.id);
        $("#search_origUnitName").val(rowData.name);
        initSelectOrigForm();
        $("#search_customerType").val(rowData.unitType);
        $("#search_origId").selectpicker('val', rowData.defaultWarehId);
        $("#search_origId").selectpicker('refresh');
        $("#modal_guest_search_table").modal('hide');
    }
    function confirm_selected_GuestId_saleReturn() {
        var rowId = $("#guestSelect_Grid").jqGrid("getGridParam", "selrow");
        var rowData = $("#guestSelect_Grid").jqGrid('getRowData', rowId);
        $("#search_origUnitId").val(rowData.id);
        $("#search_origUnitName").val(rowData.name);
        initSelectOrigForm();
        $("#search_customerType").val(rowData.unitType);
        $("#search_origId").selectpicker('val', rowData.defaultWarehId);
        $("#search_origId").selectpicker('refresh');
        var idCard = rowData.idCard;
        $.ajax({
            dataType: "json",
            async: false,
            url: basePath+"/shop/vipCard/findCardDiscount.do?",
            data:{
                idCard:idCard
            },
            type: "POST",
            success: function (msg) {
                if (msg.success) {
                    var discount = msg.result;
                    if (rowData.discount>discount){
                        $("#search_discount").val(discount);
                    }else {
                        $("#search_discount").val(rowData.discount);
                    }
                } else {
                    bootbox.alert(msg.msg);
                }
            }
        });
        $("#modal_guest_search_table").modal('hide');

        if (pageType === "add"){
            if ($("#search_origId").val() && $("#search_origId").val() !== null && $("#search_origId").val() !== ""){
                $("#SODtl_wareHouseOut").removeAttr("disabled");
                $("#SODtl_wareHouseIn").attr({"disabled": "disabled"});
//                $("#addDetailgrid-pager_left").show();
//                $("#addDetailgrid").setColProp('qty',{editoptions:{value:"True"}});
            }else { //发货仓库为空，例如零售客户
                $("#SODtl_wareHouseOut").attr({"disabled": "disabled"});
                $("#SODtl_wareHouseIn").removeAttr("disabled");
//                $("#addDetailgrid-pager_left").hide();
//                $("#addDetailgrid").setColProp('qty',{editoptions:{value:"False"}});
            }
        }else if (pageType === "edit"){
            if ($("#search_origId").val() && $("#search_origId").val() !== null && $("#search_origId").val() !== ""){
                $("#SODtl_wareHouseOut").removeAttr("disabled");
                $("#SODtl_wareHouseIn").show();
                $("#SODtl_wareHouseIn_noOutHouse").hide();
//                $("#addDetailgrid-pager_left").show();
//                $("#addDetailgrid").setColProp('qty',{editoptions:{value:"True"}});
            }else {//发货仓库为空，例如零售客户
                $("#SODtl_wareHouseOut").attr({"disabled": "disabled"});
                $("#SODtl_wareHouseIn").hide();
                $("#SODtl_wareHouseIn_noOutHouse").show();
//                $("#addDetailgrid-pager_left").hide();
//                $("#addDetailgrid").setColProp('qty',{editoptions:{value:"False"}});
            }
        }
    }

    function confirm_selected_OrigUnit() {
        var rowId = $("#guestSelect_Grid").jqGrid("getGridParam", "selrow");
        var rowData = $("#guestSelect_Grid").jqGrid('getRowData', rowId);
        $("#search_origUnitId").val(rowData.id);
        $("#search_origUnitName").val(rowData.name);
        initSelectOrigForm();
        $("#search_origId").selectpicker('val', rowData.defaultWarehId);
        $("#search_origId").selectpicker('refresh');
        $("#modal_guest_search_table").modal('hide');
    }

    function confirm_selected_DestUnit() {
        var rowId = $("#guestSelect_Grid").jqGrid("getGridParam", "selrow");
        var rowData = $("#guestSelect_Grid").jqGrid('getRowData', rowId);
        $("#search_destUnitId").val(rowData.id);
        $("#search_destUnitName").val(rowData.name);
        initSelectDestForm();
        $("#search_destId").selectpicker('val', rowData.defaultWarehId);
        $("#search_destId").selectpicker('refresh');
        $("#modal_guest_search_table").modal('hide');
    }
</script>

