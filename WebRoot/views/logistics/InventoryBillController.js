$(function () {
    initSelectOrigForm();
    initSelectDestForm();
    initGrid();

});
function refresh(){
    location.reload(true);
}
//收货仓库，客户仓库
function initSelectDestForm() {
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9",
        cache: false,
        async: true,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_destId").empty();
            $("#search_destId").append("<option value='' >--请选择入库仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_destId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
            }
            $('.selectpicker').selectpicker('refresh');
        }
    });
}
//发货仓库
function initSelectOrigForm() {
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9",
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
            $('.selectpicker').selectpicker('refresh');
        }
    });
}
function initGrid() {
    $("#grid").jqGrid({
        height:  "auto",
        url: basePath + "/logistics/inventoryBillController/page.do?code="+$("#code").val(),
        datatype: "json",
        mtype:"POST",
        colModel: [
            {name: 'id', label: '单号', width: 40},
            {name: 'billDate', label: '时间', width: 40},
            {name: 'sku', label: 'SKU', editable: true, width: 40},
            {name: 'styleId', label: '款号', editable: true, width: 60},
            {name: 'colorId', label: '色码', editable: true, width: 40},
            {name: 'sizeId', label: '尺码', editable: true, width: 40},
            {name: 'styleName', label: '款名', editable: true, width: 40},
            {name: 'colorName', label: '颜色', editable: true, width: 40},
            {name: 'code', label: 'code', editable: true, width: 40},
            {
                label: '状态', width: 40, align: 'center',sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.state == "28") {
                        return '转出库';
                    } else if (rowObject.state == "29") {
                        return '转入库';
                    } else {
                        return '';
                    }
                }
            },
            {name:'reason',label: '原因', editable: true, width: 40}
            /*{name: 'sizeName', label: '尺码', editable: true, width: 40},*/
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
        autoScroll: false,
        sortname: 'id',
        sortorder: "desc"

    });
    $("#grid").jqGrid("setFrozenColumns");
}

function _search() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        page : 1,
        url: basePath + "/logistics/inventoryBillController/page.do?code="+$("#code").val(),
        postData : params
    });
    $("#grid").trigger("reloadGrid");
}


function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");

}
function addUniqCode() {
    allCodes="";
    $("#codeQtys").val(0);
    $("#dialog_buttonGroups").html("" +
                "<button type='button'  class='btn btn-primary' onclick='addProductsOnCode()'>保存</button>"
    );
    $("#add-inventory-dialog").modal('show').on('hidden.bs.modal', function () {
                $("#inventoryCodeGrid").clearGridData();
    });
}
function addProductsOnCode() {
    debugger;
    //var rowId = $("#inventoryCodeGrid").jqGrid("getGridParam", 'selrow');
    //if (rowId) {\
    /*var codes="(";*/
    var codes="";
    $.each($("#inventoryCodeGrid").getDataIDs(), function (index, value) {
        var productInfo = $("#inventoryCodeGrid").getRowData(value);
        if(index==0){
            codes+=productInfo.code;
        }else{
            codes+=","+productInfo.code;
        }
    });
    /*codes+=")";*/
        //var rowData = $("#inventoryCodeGrid").jqGrid("getRowData", rowId);
        $.ajax({
            url: basePath + "/logistics/inventoryBillController/saveInventory.do",
            data: {codes: codes},
            datatype: "json",
            type: "POST",
            success: function (data) {
                if (data.success) {
                    $.gritter.add({
                        text: data.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                    $("#grid").trigger("reloadGrid");

                } else {
                    $.gritter.add({
                        text: data.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                }
                $('#inventoryCode-editForm').clearForm();
                $("#add-inventory-dialog").modal('hide');
                //$("#inventoryCodeGrid").jqGrid("clearGridData");
                //progressDialog.modal('hide');
            }
        });
    /*} else {
        bootbox.alert("请选择一项进行设置!");
    }*/

}

function add(){
}