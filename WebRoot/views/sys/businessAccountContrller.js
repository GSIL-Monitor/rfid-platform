$(function () {
    initGrid();
    initForm();
});

function initForm() {
    initSelectOrigForm();
    initSelectDestForm();
    if(billNo){
        bootbox.alert("单据"+billNo+"正在编辑中");
    }else{
        sessionStorage.removeItem("billNoConsignment");
    }
    $(".selectpicker").selectpicker('refresh');
}

function initSelectOrigForm() {
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId="+$("#search_origUnitId").val(),
        cache: false,
        async: true,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_origId").empty();
            $("#search_origId").append("<option value='' >--请选择出库仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_origId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
            }
        }
    });
}

function initSelectDestForm() {
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId="+curOwnerId,
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_destId").empty();
            $("#search_destId").append("<option value=''>--请选择入库仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_destId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
            }
        }
    });
}


function initGrid() {
    $("#grid").jqGrid({
        height: 'auto',
        datatype: 'json',
        url: basePath + "/sys/businessAccount/page.do",
        mtype: 'POST',
        colModel: [
            {name: 'billDate', label: '日期', width: 30},
            {name: 'destunitname', label: '店铺名称', width: 30},
            {name: 'destunitid', label: '店铺名称id', hidden: true},
            {name: 'saleqty', label: '发货数量', width: 30},
            {name: 'saleprice', label: '发货金额', width: 30,
                formatter: function (cellValue, options, rowObject) {
                    if(cellValue) {
                        var saleprice = cellValue.toFixed(2);
                        return saleprice;
                    }else{
                        return cellValue;
                    }
                }
            },
            {name: 'salereturnqty', label: '退货数量', width: 30},
            {name: 'salereturnprice', label: '退货金额', width: 30,
                formatter: function (cellValue, options, rowObject) {
                    if(cellValue) {
                        var salereturnprice = cellValue.toFixed(2);
                        return salereturnprice;
                    }else{
                        return cellValue;
                    }
                }
            },
            {name: 'payprice', label: '充值', width: 30,
                formatter: function (cellValue, options, rowObject) {
                    if(cellValue) {
                        var payprice = cellValue.toFixed(2);
                        return payprice;
                    }else{
                        return 0;
                    }
                }
            },
            {name: 'owingValue', label: '欠款金额', width: 30,
                formatter: function (cellValue, options, rowObject) {
                    if(cellValue) {
                        var owingValue = cellValue.toFixed(2);
                        return owingValue;
                    }else{
                        return 0;
                    }
                }
            }

        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 50,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: true,
        sortname: 'billNo',
        sortorder: "desc",
        autoScroll: false,
        footerrow: true,
        gridComplete: function () {
            setFooterData();
        },
    });
}

function setFooterData() {
    debugger;
    var sum_totQty = $("#grid").getCol('totQty', false, 'sum');
    var sum_totOutQty = $("#grid").getCol('totOutQty', false, 'sum');
    var sum_totOutVal = $("#grid").getCol('totOutVal', false, 'sum');
    var sum_totInQty = $("#grid").getCol('totInQty',false,'sum');
    var sum_totInVal = $("#grid").getCol('totInVal', false, 'sum');
    var sum_payPrice = $("#grid").getCol('payPrice', false, 'sum');
    $("#grid").footerData('set', {
        billNo: "合计",
        totQty: sum_totQty,
        totOutQty: sum_totOutQty,
        totOutVal: sum_totOutVal,
        totInQty: sum_totInQty,
        totInVal: sum_totInVal,
        payPrice: sum_payPrice
    });
}

function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}



function _search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        page: 1,
        // url:searchUrl,
        postData: params
    });
    $("#grid").trigger("reloadGrid");
}

function refresh() {
    location.reload(true);
}



var dialogOpenPage;
var prefixId;
function openSearchGuestDialog() {
    prefixId="search";
    dialogOpenPage = "businessAccountDialog";
    $("#modal_guest_search_table").modal('show').on('shown.bs.modal', function () {
        initGuestSelect_Grid();
    });
    $("#searchGuestDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='confirm_selected_GuestId_saleReturn()'>确认</button>"
    );
}
function Export() {
    window.location.href=basePath+"/sys/businessAccount/export.do?filter_GED_billDate="+$("#search_billDate").val() + "&filter_LED_billDate="+$("#search_LEDbillDate").val()+ "&filter_EQS_destunitid="+$("#search_destunitid").val();
}


