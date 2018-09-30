$(function () {
    $("#footer").load("../layout/footer_js.jsp");
    //得到表格的宽度
    var tableWith=$("#transferOrder").width();
    var oneWith=tableWith/12;
    $("#convertOut").css("left","10");
    $("#convertOut").css("width",oneWith);
    $("#back").css("right","10px");
    $("#back").css("width",oneWith);
    loadTable();
    fullFromMessage();
    initSelectDestForm();
});
function loadTable() {
    $("#addSaleDetailgrid").jqGrid({
        height: "40%",
        url: basePath + "/logistics/transferOrder/findBillDtlWS.do?billNo=" + localStorage.getItem("transferOrderBillNo"),
        datatype: "json",
        mtype: 'POST',
        colModel: [
            {name: 'id', label: 'id', hidden: true, width: 40},
            {name: 'billId', label: 'billId', hidden: true},
            {name: 'billNo', label: 'billNo', hidden: true},
            {name: 'status', hidden: true},
            {name: 'outStatus', hidden: true},
            {name: 'inStatus', hidden: true},
            {name: 'styleId', label: '款号', width: 40},
            {name: 'styleName', label: '款名', width: 40},
            {name: 'colorId', label: '色码', width: 40, hidden: true},
            {name: 'colorName', label: '颜色', width: 30},
            {name: 'sizeId', label: '尺码', width: 30, hidden: true},
            {name: 'sizeName', label: '尺码', width: 40},
            {
                name: 'qty', label: '数量', editable: true, width: 40,
                editrules: {
                    number: true,
                    minValue: 1
                },
                editoptions: {
                    dataInit: function (e) {
                        $(e).spinner();
                    }
                }
            },
            {name: 'outQty', label: '已出库数量', width: 40},
            {name: 'inQty', label: '已入库数量', width: 40},
            {name: 'sku', label: 'SKU', width: 40},
            {name: 'price', label: '调拨成本', width: 40, hidden: true},
            {name: 'totPrice', label: '总成本', width: 40, hidden: true},
            {name: 'uniqueCodes', label: '唯一码', hidden: true},
            {name: 'uniqueCodes', label: '异常的唯一码', hidden: true}
            /*{
             name: '', label: '唯一码明细', width: 40, align: "center",
             formatter: function (cellValue, options, rowObject) {
             return "<a href='javascript:void(0);' onclick=showCodesDetail('" + rowObject.uniqueCodes + "')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
             }
             }*/
        ],
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: -1,
        pager: '#grid-Sale-pager',
        multiselect: false,
        shrinkToFit: true,
        sortname: 'id',
        sortorder: "asc",
        footerrow: true,
        gridComplete: function () {
            setFooterData();
        },
        loadComplete : function(){
            var table = this;
            setTimeout(function(){

                //加载完成后，替换分页按钮图标
                updatePagerIcons(table);
                enableTooltips(table);
            }, 0)
        }
    });

}
function setFooterData() {
    var sum_qty = $("#addSaleDetailgrid").getCol('qty', false, 'sum');
    var sum_outQty = $("#addSaleDetailgrid").getCol('outQty', false, 'sum');
    var sum_inQty = $("#addSaleDetailgrid").getCol('inQty', false, 'sum');
    var sum_totPrice = $("#addSaleDetailgrid").getCol('totPrice', false, 'sum');
    $("#addSaleDetailgrid").footerData('set', {
        styleId: "合计",
        qty: sum_qty,
        outQty: sum_outQty,
        inQty: sum_inQty,
        totPrice: sum_totPrice,
    });
}
function updatePagerIcons(table) {
    //ui-icon ui-icon-circlesmall-minus
    var replacement =
        {
            'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
            'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
            'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
            'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
        };
    $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
        var icon = $(this);
        var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

        if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
    });
}
function enableTooltips(table) {
    $('.navtable .ui-pg-button').tooltip({
        container : 'body'
    });
    $(table).find('.ui-pg-div').tooltip({
        container : 'body'
    });
}
/**
 * 出库
 */
function wareHouseOut() {

    var sum_qty = parseInt($("#addDetailgrid").footerData('get').qty);
    var sum_outQty = parseInt($("#addDetailgrid").footerData('get').outQty);

    if (sum_qty === sum_outQty) {
        $.gritter.add({
            text: '已全部出库',
            class_name: 'gritter-success  gritter-light'
        });
        $("#addSaleDetailgrid").trigger("reloadGrid");

        return;
    }


    cs.showProgressBar();
    var billNo = $("#billNo").val();
    if (billNo && billNo !== null) {

        var epcArray = [];
        $.each($("#addSaleDetailgrid").getDataIDs(), function (index, value) {
            var rowData = $("#addSaleDetailgrid").getRowData(value);
            if (rowData.uniqueCodes && rowData.uniqueCodes !== null && rowData.uniqueCodes !== "") {
                var codes = rowData.uniqueCodes.split(",");
                $.each((codes), function (index, value) {
                    // if (uniqueCodes_inHouse.indexOf(value) !== -1) {
                    var epc = {};
                    epc.code = value;
                    epc.styleId = rowData.styleId;
                    epc.sizeId = rowData.sizeId;
                    epc.colorId = rowData.colorId;
                    epc.qty = 1;
                    epc.sku = rowData.sku;
                    epcArray.push(epc);
                    // }
                })
            }
        });
        if (epcArray.length === 0) {
            /*$.gritter.add({
                text: "请扫码添加出库商品",
                class_name: 'gritter-success  gritter-light'
            });*/
            bootbox.confirm("没有出库的商品", function(result) {
                if (result){
                    localStorage.clear();
                    window.location.href=basePath+'/views/NoOneCashier/selectSaleOrTransferOrder.html';

                }
            });
            cs.closeProgressBar();
            $("#TRDtl_wareHouseOut").removeAttr("disabled");
            //edit_wareHouseOut();
            return;
        }

        var dtlArray = [];
        $.each($("#addSaleDetailgrid").getDataIDs(), function (index, value) {
            var rowData = $("#addSaleDetailgrid").getRowData(value);
            dtlArray.push(rowData);
        });

        $.ajax({
            dataType: "json",
            // async:false,
            url: basePath + "/logistics/transferOrder/convertOutWS.do",
            data: {
                billNo: billNo,
                strEpcList: JSON.stringify(epcArray),
                strDtlList: JSON.stringify(dtlArray),
                userId: localStorage.getItem("userId")
            },
            type: "POST",
            success: function (msg) {
                cs.closeProgressBar();
                $("#TRDtl_wareHouseOut").removeAttr("disabled");
                if (msg.success) {
                  /*  $.gritter.add({
                        text: msg.msg,
                        class_name: 'gritter-success  gritter-light'
                    });*/
                    bootbox.confirm("已出库" + epcArray.length + "件商品", function(result) {
                        if (result){
                            localStorage.clear();
                            window.location.href=basePath+'/views/NoOneCashier/selectSaleOrTransferOrder.html';
                        }
                    });

                } else {
                    bootbox.alert(msg.msg);
                }
            }
        });
    } else {
        cs.closeProgressBar();
        bootbox.alert("请先保存当前单据");
    }

}
//加载表单数据
function fullFromMessage() {
    var bill=JSON.parse(localStorage.getItem("transferOrderBill"));
    $("#billDate").val(bill.billDate);
    $("#origUnitName").val(bill.origUnitName);
    $("#origId").val(bill.origId);
    $("#destUnitName").val(bill.destUnitName);
    $("#billNo").val(localStorage.getItem("transferOrderBillNo"))
}
function initSelectDestForm() {
    var bill=JSON.parse(localStorage.getItem("transferOrderBill"));
    $.ajax({
        url: basePath + "/unit/listWS.do?filter_EQI_type=9&filter_EQS_ownerId=" + localStorage.getItem("transferOrderDestUnitId"),
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
            $("#search_destId").val(bill.destId);
            /* $(".selectpicker").selectpicker('refresh');*/
        }
    });
}

function onBack() {
    window.location.href=basePath+'/views/NoOneCashier/selectSaleOrTransferOrder.html';
}

