var searchUrl = basePath + "/search/findSkuInformation/page.do?";
$(function () {
    initGrid();
    initSelectbuyahandIdForm();
    /* initProgressDialog();
     initNotification();*/


});
function initSelectbuyahandIdForm() {
    var url;

    url = basePath + "/sys/user/list.do?filter_EQS_roleId=BUYER";



    $.ajax({
        url: url,
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_buyahandid").empty();
            $("#search_buyahandid").append("<option value='' >--请选择销售员--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_buyahandid").append("<option value='" + json[i].id + "'>" + json[i].name + "</option>");
                // $("#search_busnissId").trigger('chosen:updated');
            }


        }
    });
}
function openSearchVendorDialog() {
    dialogOpenPage = "purchaseOrder";
    $("#modal_vendor_search_table").modal('show').on('shown.bs.modal', function () {
        initVendorSelect_Grid();
    });
    $("#searchVendorDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='selected_VendorId_purchaseOrder()'>确认</button>"
    );
}
function selected_VendorId_purchaseOrder() {
    var rowId = $("#vendorSelect_Grid").jqGrid("getGridParam", "selrow");
    var rowData = $("#vendorSelect_Grid").jqGrid('getRowData', rowId);
    $("#search_origUnitId").val(rowData.id);
    $("#search_origUnitName").val(rowData.name);
    $("#modal_vendor_search_table").modal('hide');
}


function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url:searchUrl,
        datatype: "json",
        mtype: 'POST',
        colModel: [
            {name: 'id', label: '单据编号', sortable: true, width: 45},
            {name: 'sku', label: 'SKU', sortable: true, width: 35},
            {name: 'fristtime', label: '第一次到货时间', width: 35},
            {name: 'endtime', label: '最后到货时间', width: 30},
            {name: 'billdate', label: '下单时间', width: 30},
            {name: 'qty', label: '数量', width: 50},
            {name: 'inqty', label: '到货数量', width: 50},
            {name: 'destname', label: '分配仓库', width: 50},
            {name: 'origunitname', label: '供应商', width: 50},
            {name: 'class1name', label: '厂家名', width: 50},
            {name: 'buyahandname', label: '买手', width: 50},
            {name: "instocktype", hidden: true},
            {
                name: 'inStockTypeName', label: '入库类型', width: 40, editable: true,
                formatter: function (cellValue, options, rowObject) {
                    switch (rowObject.instocktype) {
                        case 'XK':
                            return "新款";
                        case 'BH':
                            return "补货";
                        case 'PH':
                            return "供应商配货";
                        case 'JS':
                            return "寄售";
                        default :
                            return "";
                    }
                }
            }

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
            //setFooterData();
        },
        onSelectRow: function (rowid, status) {
        }
    });
}
function showAdvSearchPanel() {

    $("#searchPanel").slideToggle("fast");
}

function refresh() {
    location.reload(true);
}


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

