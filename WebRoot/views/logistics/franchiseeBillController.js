var searchUrl = basePath + "/logistics/franchisee/page.do?filter_INI_status=2,3&userId="+userId;
var addDetailgridiRow;//存储iRow
var addDetailgridiCol;//存储iCol
var allCodes; //用于拼接所有添加过的唯一码，防止重复添加
var taskType; //用于判断出入库类型 1入库 0 出库
var wareHouse;
var inOntWareHouseValid; //用于判断在编辑BillDtl时出入库操作是否需要校验，使用哪种校验。
var skuQty = {};//保存每个SKU对应的出入库数量。
var allCodeStrInDtl = "";  //入库时，所有明细中的唯一码
$(function () {
    //初始化左侧grid
    initGrid();
    //初始化右侧grad
    initeditGrid(null);
    //初始化from表单
    initForm();
    //初始化按钮
    initButtonGroup(0);
    //initProgressDialog();
    //initNotification();
});
function initForm() {
    initCustomerTypeForm();
    initSelectBusinessIdForm();
    initSelectOrigForm();
    initSelectDestForm();
    $(".selectpicker").selectpicker('refresh');
}
//客户类型
function initCustomerTypeForm() {
    $.ajax({
        url: basePath + "/sys/property/searchByType.do?type=CT",
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#edit_customerType").empty();
            $("#edit_customerType").append("<option value='' style='background-color: #eeeeee'>--请选择客户类型--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#edit_customerType").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                $("#edit_customerType").trigger('chosen:updated');
            }
        }
    });
}
//销售员
function initSelectBusinessIdForm() {
    var url;
    if(curOwnerId=="1"){
        url=basePath + "/sys/user/list.do?filter_EQI_type=4";
    }else{
        url=basePath + "/sys/user/list.do?filter_EQI_type=4";
    }
    $.ajax({
        url: url,
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#edit_busnissId").empty();
            $("#edit_busnissId").append("<option value='' >--请选择销售员--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#edit_busnissId").append("<option value='" + json[i].id + "'>" + json[i].name + "</option>");
                // $("#search_busnissId").trigger('chosen:updated');
            }
        }
    });
}
//发货仓库
function initSelectOrigForm() {
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + curOwnerId,
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#form_origId").empty();
            $("#form_origId").append("<option value=''>--请选择出库仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#form_origId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
            }
        }
    });
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9",
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_origId").empty();
            $("#search_origId").append("<option value=''>--请选择出库仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_origId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
            }
        }
    });
}
//客户选择
var dialogOpenPage;
function openSearchGuestDialog(preId) {
    dialogOpenPage = "saleOrder";
    prefixId =preId;
    $("#modal_guest_search_table").modal('show').on('shown.bs.modal', function () {
        initGuestSelect_Grid();
    });
    $("#searchGuestDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='confirm_selected_GuestId_sale()'>确认</button>"
    );
}

//收货仓库，客户仓库
function initSelectDestForm() {
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + $("#search_destUnitId").val(),
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_destId").empty();
            $("#search_destId").append("<option value=''>--请选择入库仓库--</option>");
            $("#form_destId").empty();
            $("#form_destId").append("<option value=''>--请选择入库仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_destId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                $("#form_destId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
            }
        }
    });
}
function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url: basePath + "/logistics/franchisee/page.do?filter_GTI_status=2,3&userId=" + userId,
        datatype: "json",
        mtype: 'POST',
        colModel: [
            {name: 'billNo', label: '单据编号', sortable: true, width: 45,hidden:true},
            {name: 'status', hidden: true},
            {name: 'destUnitId', label: '客户ID', hidden: true},
            {name: 'destUnitName', label: '客户', width: 40,hidden:true},
            {name: 'outStatus', label: '出库状态', hidden: true,sortable: false},
            {name: 'inStatus', label: '入库状态', hidden: true,sortable: false},
            {name: 'billDate', label: '单据日期', sortable: true, width: 30},
            {
                name: 'statusImg', label: '状态', width: 15, align: 'center',sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    var html = "";
                    switch (rowObject.status) {
                        case -1 :
                            html = "<i class='fa fa-undo blue' title='撤销'></i>";
                            break;
                        case 0 :
                            html = "<i class='fa fa-caret-square-o-down blue' title='录入'></i>";
                            break;
                        case 1:
                            html = "<i class='fa fa-check-square-o blue' title='审核'></i>";
                            break;
                        case 2 :
                            html = "<i class='fa fa-archive blue' title='结束'></i>";
                            break;
                        case 3:
                            html = "<i class='fa fa-wrench blue' title='操作中'></i>";
                            break;
                        case 4:
                            html = "<i class='fa fa-paper-plane blue' title='申请撤销'></i>";
                            break;
                        default:
                            break;
                    }
                    return html;
                }
            },
            {
                name: 'outStatusImg', label: '出库状态', width: 25, align: 'center',sortable: false,hidden:true,
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.outStatus == 0) {
                        return '<i class="fa fa-tasks blue" title="订单状态"></i>';
                    } else if (rowObject.outStatus == 2) {
                        return '<i class="fa fa-sign-out blue" title="已出库"></i>';
                    } else if (rowObject.outStatus == 3) {
                        return '<i class="fa fa-truck blue" title="出库中"></i>';
                    } else {
                        return '';
                    }
                }
            },
            {
                name: 'inStatusImg', label: '入库状态', width: 25, align: 'center',sortable: false,hidden:true,
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.inStatus == 0) {
                        return '<i class="fa fa-tasks blue" title="订单状态"></i>';
                    } else if (rowObject.inStatus == 1) {
                        return '<i class="fa fa-sign-in blue" title="已入库"></i>';
                    } else if (rowObject.inStatus == 4) {
                        return '<i class="fa fa-truck blue" title="入库中"></i>';
                    } else {
                        return '';
                    }
                }
            },

            {name: 'customerTypeId', label: '客户类型', hidden: true},
            {
                label: '客户类型', sortable: false, width: 30,hidden:true,
                formatter: function (cellValue, options, rowObjec) {
                    if (rowObjec.customerTypeId == "CT-AT") {
                        return "省代客户";
                    } else if (rowObjec.customerTypeId == "CT-ST") {
                        return "门店客户";
                    } else if (rowObjec.customerTypeId == "CT-LS") {
                        return "零售客户";
                    }
                }
            },
            {name: 'origUnitId', label: '发货方ID', hidden: true},
            {name: 'origUnitName', label: '发货方', width: 30,hidden:true},
            {name: 'origId', label: '发货仓库ID', hidden: true},
            {name: 'origName', label: '发货仓库', width: 30,hidden:true},

            {name: 'destId', label: '收货仓库ID', hidden: true},
            {name: 'destName', label: '收货仓库', width: 30,hidden:true},
            {name: 'busnissId', label: '业务员Id', hidden: true},
            {name: 'busnissName', label: '业务员', width: 20},
            {name: 'totInQty', label: '收货数量', width: 20,align:"center"},
            {name: 'totQty', label: '单据数量', width: 20,align:"center"},
            {name:'customerTypeId',hidden:true},
            {name:'payPrice',hidden:true},
            {name:'totPrice',label:'金额',width:20},
            {name:'discount',hidden:true},
            {name: 'remark', label: '备注', width: 20}
        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: true,
        sortname: 'billNo',
        sortorder: 'desc',
        autoScroll: false,
        footerrow: true,
        gridComplete: function () {
            setFooterData();
        },
        onSelectRow: function (rowid, status) {
            initDetailData(rowid)
        }
    });
    if (roleid=="JMSJS"){
        $('#grid').setGridParam().hideCol("totPrice");
    }
}

function initDetailData(rowid) {
    $("#myTab li").eq(0).find("a").click();
    $("#SODtl_wareHouseIn").removeAttr("disabled");
    var rowData = $("#grid").getRowData(rowid);
    $("#editForm").setFromData(rowData);
    $(".selectpicker").selectpicker('refresh');
    $('#addDetailgrid').jqGrid("clearGridData");
    $('#addDetailgrid').jqGrid('GridUnload');
    $('#codeDetailgrid').jqGrid("clearGridData");
    $('#codeDetailgrid').jqGrid('GridUnload');
    initeditGrid(rowData.billNo);
    initcodeDetail(rowData.billNo);
    var franchiseeStatus = $("#edit_status").val();
    initButtonGroup(franchiseeStatus);
    $("#codeDetailgrid").trigger("reloadGrid");
    $("#addDetailgrid").trigger("reloadGrid");
    var slaeOrder_status = rowData.status;
    //如果入库仓库为空，禁止入库按钮
    if (!$("#search_destId").val() || $("#search_destId").val() == null) {
            $("#SODtl_wareHouseIn").attr("disabled","disabled");
    }
    $("#edit_customerType").attr('disabled', true);
    $("#edit_busnissId").attr('disabled', true);
    $("#addDetail").show();
}
/*根据权限初始化按钮*/
function initButtonGroup(billStatus) {
    $("#buttonGroup").html("" +
        "<button id='SODtl_wareHouseIn' type='button' style='margin: 8px' class='btn btn-xs btn-primary' onclick='wareHouseInfranchisee()'>" +
        "    <i class='ace-icon fa fa-plus'></i>" +
        "    <span class='bigger-110'>入库</span>" +
        "</button>");
    loadingButtonDivTable(billStatus);
}

/**
 * 动态配置按钮,div,表格列字段
 * */
function loadingButtonDivTable(billStatus) {
    var privilegeMap = ButtonAndDivPower(resourcePrivilege);
    $.each(privilegeMap['table'],function(index,value){
        if(value.isShow!=0) {
            $('#addDetailgrid').setGridParam().hideCol(value.privilegeId);
        }
    });
    var disableButtonIds = "";
    switch (billStatus){
        case "-1" :
            disableButtonIds = ["SODtl_wareHouseIn"];
            break;
        case "0" :
            disableButtonIds = ["SODtl_wareHouseIn"];
            break;
        case "1":
            disableButtonIds = ["SODtl_wareHouseIn"];
            break;
        case "2" :
            disableButtonIds = ["SODtl_wareHouseIn"];
            break;
        case "3":
            disableButtonIds = [];
            break;
        default:
            disableButtonIds = ["SODtl_wareHouseIn"];
    }
    //根据单据状态disable按钮
    $.each(privilegeMap['button'],function(index,value){
        if($.inArray(value.privilegeId,disableButtonIds)!= -1){
            $("#"+value.privilegeId).attr({"disabled": "disabled"});
        }else{
            $("#"+value.privilegeId).removeAttr("disabled");
        }
    });
}

//左侧表格汇总
function setFooterData() {
    var sum_totQty = $("#grid").getCol('totQty', false, 'sum');
    var sum_totInQty = $("#grid").getCol('totInQty',false,'sum');
    var sum_totActPrice = $("#grid").getCol('totPrice', false, 'sum');
    $("#grid").footerData('set', {
        billDate: "合计",
        totQty: sum_totQty,
        totInQty: sum_totInQty,
        totPrice: sum_totActPrice
    });
}
//左侧表格查询
function _search() {

    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        page: 1,
        url: searchUrl,
        postData: params
    });
    $("#grid").trigger("reloadGrid");
}
function initeditGrid(billNo) {
    $("#addDetailgrid").jqGrid({
        height: 'auto',
        datatype: "json",
        url: basePath + "/logistics/saleOrderBill/findBillDtl.do?billNo=" + billNo,
        mtype: 'POST',
        colModel: [
            {name: 'id', label: 'id', hidden: true},
            {name: 'billId', label: 'billId', hidden: true},
            {name: 'billNo', label: 'billNo', hidden: true},
            {name: 'status', hidden: true},
            {name: 'inStatus', hidden: true},
            {name: 'outStatus', hidden: true},
            {
                name: 'statusImg', label: '状态', width: 20, hidden: true, sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.status == 0) {
                        return '<i class="fa fa-tasks blue" title="订单状态"></i>';
                    } else if (rowObject.status == 1) {
                        return '<i class="fa fa-sign-in blue" title="入库状态"></i>';
                    } else if (rowObject.status == 2) {
                        return '<i class="fa fa-sign-out blue" title="出库状态"></i>';
                    } else {
                        return '';
                    }
                }
            },
            {
                name: 'inStatusImg', label: '入库状态', width: 30, align: 'center', sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.inStatus == 0) {
                        return '<i class="fa fa-tasks blue" title="订单状态"></i>';
                    } else if (rowObject.inStatus == 1) {
                        return '<i class="fa fa-sign-in blue" title="已入库"></i>';
                    } else if (rowObject.inStatus == 4) {
                        return '<i class="fa fa-truck blue" title="入库中"></i>';
                    } else {
                        return '';
                    }
                }
            },
            {
                name: 'outStatusImg', label: '出库状态', width: 30, align: 'center', sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.outStatus == 0) {
                        return '<i class="fa fa-tasks blue" title="订单状态"></i>';
                    } else if (rowObject.outStatus == 2) {
                        return '<i class="fa fa-sign-out blue" title="已出库"></i>';
                    } else if (rowObject.outStatus == 3) {
                        return '<i class="fa fa-truck blue" title="出库中"></i>';
                    } else {
                        return '';
                    }
                }
            },
            {name: 'styleId', label: '款号', width: 20,hidden: true},
            {name: 'colorId', label: '色码', width: 20,hidden: true},
            {name: 'sizeId', label: '尺码', width: 20,hidden: true},
            {name: 'styleName', label: '款名', width: 20},
            {name: 'colorName', label: '颜色', width: 20,hidden: true},
            {name: 'sizeName', label: '尺码', width: 20,hidden: true},
            {name: 'qty', label: '数量', editable: false, width: 40},
            {name: 'returnQty', label: '退货数量', editable: true, width: 40},
            {name: 'outQty', label: '已出库数量', width: 40},
            {name: 'inQty', label: '已入库数量', width: 40},
            {name: 'sku', label: 'SKU', width: 50},
            {name: 'tagPrice', label: '吊牌价', width: 40},
            {name: 'totPrice', label: '金额', width: 40,
                formatter: function (cellValue, options, rowObject) {
                    cellValue = rowObject.qty*rowObject.tagPrice;
                    return cellValue;
                }
            },
            {name: 'uniqueCodes', label: '唯一码', hidden: true},
            {name:'',label:'唯一码明细',width:40, align:"center",
                formatter: function (cellValue, options, rowObject) {
                    return "<a href='javascript:void(0);' onclick=showCodesDetail('" + rowObject.uniqueCodes + "')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
                }
            },
            {name: 'returnbillNo', label: '退货单号', hidden: true}
        ],
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: -1,
        pager: '#addDetailgrid-pager',
        multiselect: false,
        shrinkToFit: true,
        sortname: 'id',
        sortorder: "asc",
        footerrow: true,
        cellEdit: true,
        cellsubmit: 'clientArray',
        afterSaveCell: function (rowid, cellname, value, iRow, iCol) {
            var rowData = $('#addDetailgrid').getRowData(rowid);

            if ((parseInt(rowData.qty) - parseInt(rowData.outQty)) >= parseInt(rowData.returnQty) || rowData.returnQty == "") {
                $('#addDetailgrid').editCell(iRow, iCol, true);
            } else {
                //isretrun=true;
                //$('#addDetailgrid').restoreCell(iRow,iCol);
                $('#addDetailgrid').setCell(rowid, cellname, 0);
                $('#addDetailgrid').editCell(iRow, iCol, true);
                /* bootbox.alert("退货数量过多！");*/
                $.gritter.add({
                    text: "退货数量过多！",
                    class_name: 'gritter-success  gritter-light'
                });
                //$(".review-"+rowid).removeClass('not-editable-cell');

            }
            if (cellname === "discount") {
                var var_actPrice = Math.round(value * $('#addDetailgrid').getCell(rowid, "price")) / 100;
                var var_totActPrice = Math.round(var_actPrice * $('#addDetailgrid').getCell(rowid, "qty")*100)/100;
                $('#addDetailgrid').setCell(rowid, "actPrice", var_actPrice);
                $('#addDetailgrid').setCell(rowid, "totActPrice", var_totActPrice);
            } else if (cellname === "actPrice") {
                var var_discount = Math.round(value / $('#addDetailgrid').getCell(rowid, "price") * 100);
                var var_totActPrice = Math.round(value * $('#addDetailgrid').getCell(rowid, "qty")*100)/100;
                $('#addDetailgrid').setCell(rowid, "discount", var_discount);
                $('#addDetailgrid').setCell(rowid, "totActPrice", var_totActPrice);
            } else if (cellname === "qty") {
                $('#addDetailgrid').setCell(rowid, "totPrice", Math.round($('#addDetailgrid').getCell(rowid, "price") * value)*100)/100;
                $('#addDetailgrid').setCell(rowid, "totActPrice", Math.round($('#addDetailgrid').getCell(rowid, "actPrice") * value)*100)/100;
            }
            setEditFooterData();
        },
        afterEditCell: function (rowid, celname, value, iRow, iCol) {
            var value = $('#addDetailgrid').getRowData(rowid);
            if ((value.outStatus != 2 && celname == "returnQty" && value.returnbillNo == "") ||
                (value.outStatus != 2 && celname == "discount" && value.returnbillNo == "") ||
                (value.outStatus != 2 && celname == "actPrice" && value.returnbillNo == "")) {
                /* if (editDtailRowId != null) {
                 saveItem(editDtailRowId);
                 }
                 editDtailRowId = rowid;*/
                addDetailgridiRow = iRow;
                addDetailgridiCol = iCol;

            } else {
                $('#addDetailgrid').restoreCell(iRow, iCol);
            }
        },
        gridComplete: function () {
            setEditFooterData();
        },
        loadComplete: function () {
            initAllCodesList();
        }
    });
    $("#addDetailgrid-pager_center").html("");
}
//唯一码明细
function initcodeDetail(billNo) {
    $("#codeDetailgrid").jqGrid({
        height: 'auto',
        url: basePath + "/logistics/franchisee/codeDetail.do?billNo=" + billNo,
        datatype: "json",
        mtype:"POST",
        colModel: [
            {name: 'id', label: 'id', hidden: true, width: 40},
            {name: 'sku', label: 'SKU', editable: true, width: 40},
            {name: 'code', label: '吊牌码', editable: true, width: 40},
            {name: 'cartonId', label: '箱码', editable: true, width: 40},
            {name: 'styleId', label: '款号', editable: true, width: 40},
            {name: 'colorId', label: '色码', editable: true, width: 40},
            {name: 'sizeId', label: '尺码', editable: true, width: 40},
            {name: 'styleName', label: '款名', editable: true, width: 40},
            {name: 'colorName', label: '颜色', editable: true, width: 40},
            {name: 'sizeName', label: '尺码', editable: true, width: 40},
            {name: 'onlibrary', label: '是否在库', editable: true, width: 40},
        ],

        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: -1,
        multiselect: false,
        shrinkToFit: true,
        sortname: 'id',
        sortorder: "asc"
    });
    var parent_column = $("#rightdiv");
    $("#codeDetailgrid").jqGrid('setGridWidth', parent_column.width());
}

function initAllCodesList() {
    allCodeStrInDtl = "";
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        allCodeStrInDtl = allCodeStrInDtl + "," + rowData.uniqueCodes;
    });
    if (allCodeStrInDtl !== "") {
        if (allCodeStrInDtl.substr(0, 1) === ",") {
            allCodeStrInDtl = allCodeStrInDtl.substr(1);
        }
    }
}

function setEditFooterData() {
    var sum_qty = $("#addDetailgrid").getCol('qty', false, 'sum');
    var sum_outQty = $("#addDetailgrid").getCol('outQty', false, 'sum');
    var sum_inQty = $("#addDetailgrid").getCol('inQty', false, 'sum');
    var sum_returnQty = $("#addDetailgrid").getCol('returnQty', false, 'sum');
    var sum_totPrice = $("#addDetailgrid").getCol('totPrice', false, 'sum');
    var sum_totActPrice = Math.round($("#addDetailgrid").getCol('totActPrice', false, 'sum'));
    $("#edit_actPrice").val(sum_totActPrice);
    $("#addDetailgrid").footerData('set', {
        styleId: "合计",
        qty: sum_qty,
        outQty: sum_outQty,
        inQty: sum_inQty,
        totPrice: sum_totPrice
    },false);
}

function wareHouseInfranchisee() {
    var uniqueCodes="";
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        if(index==0){
            uniqueCodes+=rowData.uniqueCodes;
        }else{
            uniqueCodes+=","+rowData.uniqueCodes;
        }
    });
    $("#show-in-uniqueCode-list").modal('show');
    initUniqueCodeListin(uniqueCodes);
    codeListReloadin(uniqueCodes);
}

function showCodesDetail(uniqueCodes) {
    $("#show-uniqueCode-list").modal('show');
    initUniqueCodeList(uniqueCodes);
    codeListReload(uniqueCodes);
}

function _resetForm() {
    $("#searchForm").clearForm();
    $("#form_origId").val();
    $("#select_outStatus").val();
    $("#form_destId").val();
    $("#select_inStatus").val();
    $(".selectpicker").selectpicker('refresh');
}
//入库
function confirm_warehousing(epcinArray) {
    cs.showProgressBar();
    var billNo = $("#edit_billNo").val();
    $("#fb_comfirm_in").attr({"disabled": "disabled"});
    if(epcinArray.length == 0){
        bootbox.alert("请添加唯一码!");
        cs.closeProgressBar();
        $("#fb_comfirm_in").removeAttr("disabled");
        return;

    }
    $.ajax({
        dataType: "json",
        // async: false,
        url: basePath + "/logistics/saleOrderBill/convertInfranchisee.do",
        data: {
            billNo: billNo,
            strEpcList: JSON.stringify(epcinArray),
            userId: userId
        },
        type: "POST",
        success: function (msg) {
            cs.closeProgressBar();
            $("#fb_comfirm_in").removeAttr("disabled");
            if (msg.success) {
                bootbox.alert({
                    buttons: {ok: {label: '确定'}},
                    message: msg.msg,
                    callback: function () {
                        window.location.href = basePath + '/logistics/franchisee/index.do';
                    }
                });
                $("#show-in-uniqueCode-list").modal('hide');
                $("#inuniqueCodeListGrid").trigger("reloadGrid");
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
    $("#add-uniqCode-dialog").modal('hide');
}
