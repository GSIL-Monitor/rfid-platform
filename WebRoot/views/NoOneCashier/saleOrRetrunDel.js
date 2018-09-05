var addDetailgridiRow;//存储iRow
var addDetailgridiCol;//存储iCol
$(function () {
    intSaleDel();
    intSaleRetrunDel();
    //加载头信息
    inttitleMessage();
    input_keydown();
});
function inttitleMessage(){
    $("#customer").val(localStorage.getItem("custmerName"));
    var myTime= formatDateTime(new Date())
    $("#billDate").val(myTime);
    $("#discount").val(localStorage.getItem("discount"));
    $("#retrunDiscount").val(localStorage.getItem("discount"));
    $("#unitName").val(localStorage.getItem("unitName"));
    $("#defaultWarehId").val(localStorage.getItem("defaultWarehName"));
    $("#busnissId").val(localStorage.getItem("busnissName"));
}


 function formatDateTime(date) {
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    var h = date.getHours();
    var minute = date.getMinutes();
    minute = minute < 10 ? ('0' + minute) : minute;
    return y + '-' + m + '-' + d+' '+h+':'+minute;
}



function intSaleDel(){
    $("#addSaleDetailgrid").jqGrid({
        height: "40%",
        datatype: "local",
        mtype: 'POST',
        colModel: [
            {name: 'id', label: 'id', hidden: true},
            {name: 'billId', label: 'billId', hidden: true},
            {name: 'billNo', label: 'billNo', hidden: true},
            {name: 'status', hidden: true},
            {name: 'inStatus', hidden: true},
            {name: 'outStatus', hidden: true},
            {name: "operation", label: "操作",hidden:true},
            {name: 'statusImg', label: '状态',hidden:true},
            {name: 'inStatusImg', label: '入库状态',hidden:true},
            {name: 'outStatusImg', label: '出库状态',hidden:true},
            {name: 'styleId', label: '款号',width: 40},
            {name: 'styleName', label: '款名', hidden:true},
            {name: 'colorId', label: '色码', width: 40},
            {name: 'colorName', label: '颜色',hidden:true},
            {name: 'sizeId', label: '尺码',hidden:true},
            {name: 'sizeName', label: '尺码', width: 40},
            {name: 'qty', label: '数量', width: 40},
            {name: 'outQty', label: '已出库数量',hidden:true},
            {name: 'inQty', label: '已入库数量',hidden:true},
            {name: 'sku', label: 'SKU', width: 50},
            {
                name: 'price', label: '销售价格', width: 40,
                editrules: {
                    number: true
                },
                formatter: function (cellValue, options, rowObject) {
                    var price = parseFloat(cellValue).toFixed(2);
                    return price;
                }
            },
            {name: 'totPrice', label: '销售金额',width: 40,
                formatter: function (cellValue, options, rowObject) {
                    var totPrice = parseFloat(cellValue).toFixed(2);
                    return totPrice;
                }
            },
            {
                name: 'discount', label: "折扣", editable: true,width:40,
                editrules: {
                    number: true,
                    minValue: 0,
                    maxValue: 100
                }
            },
            {
                name: 'actPrice', label: '实际价格', editable: true,width:40,
                editrules: {
                    number: true,
                    minValue: 0
                },
                formatter: function (cellValue, options, rowObject) {
                    var actPrice = parseFloat(cellValue).toFixed(2);
                    return actPrice;
                }
            },
            {name: 'totActPrice', label: '实际金额', width:40,
                formatter: function (cellValue, options, rowObject) {
                    var totActPrice = parseFloat(cellValue).toFixed(2);
                    return totActPrice;
                }
            },
            {name: 'puPrice', label: '门店批发价', hidden: true},
            {name: 'uniqueCodes', label: '唯一码', hidden: true},
            {name: 'noOutPutCode', label: '异常唯一码', hidden: true}
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
        cellEdit: true,
        cellsubmit: 'clientArray',
        afterSaveCell: function (rowid, cellname, value, iRow, iCol) {
            var rowData = $('#addSaleDetailgrid').getRowData(rowid);
            if (cellname === "discount") {
                //判断实际价格是不是小于门店批发价格
                var var_actPrice;
                if((value*$('#addSaleDetailgrid').getCell(rowid, "price")/100)<$('#addSaleDetailgrid').getCell(rowid, "puPrice")){
                    $('#addSaleDetailgrid').setCell(rowid, "discount", parseFloat($('#addSaleDetailgrid').getCell(rowid, "puPrice")/$('#addSaleDetailgrid').getCell(rowid, "price")).toFixed(2)*100);
                    var_actPrice =  $('#addSaleDetailgrid').getCell(rowid, "puPrice");
                    $('#addSaleDetailgrid').setCell(rowid, "actPrice", $('#addSaleDetailgrid').getCell(rowid, "puPrice"));
                    $('#addSaleDetailgrid').setCell(rowid, "abnormalStatus",1);
                    
                }else{
                    $('#addSaleDetailgrid').setCell(rowid, "discount", value);
                    var_actPrice = Math.round(value * $('#addSaleDetailgrid').getCell(rowid, "price")) / 100;
                    $('#addSaleDetailgrid').setCell(rowid, "actPrice", var_actPrice);
                    $('#addSaleDetailgrid').setCell(rowid, "abnormalStatus", 0);
                   
                }

                var var_totActPrice = Math.round(var_actPrice * $('#addSaleDetailgrid').getCell(rowid, "qty") * 100) / 100;
                $('#addSaleDetailgrid').setCell(rowid, "totActPrice", var_totActPrice);

            } else if (cellname === "actPrice") {
                var var_discount = Math.round(value / $('#addSaleDetailgrid').getCell(rowid, "price") * 100);
                var var_totActPrice = Math.round(value * $('#addSaleDetailgrid').getCell(rowid, "qty") * 100) / 100;
                $('#addSaleDetailgrid').setCell(rowid, "discount", var_discount);
                $('#addSaleDetailgrid').setCell(rowid, "totActPrice", var_totActPrice);
            } else if (cellname === "qty") {
                $('#addSaleDetailgrid').setCell(rowid, "totPrice", Math.round($('#addSaleDetailgrid').getCell(rowid, "price") * value) * 100) / 100;
                $('#addSaleDetailgrid').setCell(rowid, "totActPrice", Math.round($('#addSaleDetailgrid').getCell(rowid, "actPrice") * value) * 100) / 100;
            }
            setFooterData();
        },
        afterEditCell: function (rowid, celname, value, iRow, iCol) {
            addDetailgridiRow = iRow;
            addDetailgridiCol = iCol;
        },
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
    intSaleDelMessage();
}
function intSaleDelMessage(){
    var saleDel= JSON.parse(localStorage.getItem("saleDel"));
    if(saleDel!=null){
        $.each(saleDel,function (index,value) {
            $("#addSaleDetailgrid").addRowData($("#addSaleDetailgrid").getDataIDs().length, value);
        });
    }
   

}
function intSaleRetrunDelMessage(){
    
    var saleRetrunDel= JSON.parse(localStorage.getItem("saleRetrunDel"));
    if(saleRetrunDel!=null){
        $.each(saleRetrunDel,function (index,value) {
            $("#addSaleRetrunDetailgrid").addRowData($("#addSaleRetrunDetailgrid").getDataIDs().length, value);
        });
    }

}
function setFooterData() {
    var sum_qtymin = $("#addSaleDetailgrid").getCol('qty', false, 'sum');
    var sum_totActPricemin = $("#addSaleDetailgrid").getCol('totActPrice', false, 'sum');
    var sum_totPrice = $("#addSaleDetailgrid").getCol('totPrice',false,'sum');
    sum_totActPrice = sum_totActPricemin.toFixed(0);
    $("#addSaleDetailgrid").footerData('set', {
        styleId: "合计",
        qty: sum_qtymin,
        totPrice:sum_totPrice,
        totActPrice:sum_totActPrice
    });
    localStorage.setItem("search_actPrice",sum_totActPrice);
    $("#saleAmount").val(sum_totActPrice);
    $("#saleActAmount").val(sum_totPrice);
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

function intSaleRetrunDel(){
    $("#addSaleRetrunDetailgrid").jqGrid({
        height: "40%",
        datatype: "local",
        mtype: 'POST',
        colModel: [
            {name: 'id', label: 'id', hidden: true},
            {name: 'billId', label: 'billId', hidden: true},
            {name: 'billNo', label: 'billNo', hidden: true},
            {name: 'status', hidden: true},
            {name: 'inStatus', hidden: true},
            {name: 'outStatus', hidden: true},
            {name: "operation", label: "操作",hidden:true},
            {name: 'statusImg', label: '状态',hidden:true},
            {name: 'inStatusImg', label: '入库状态',hidden:true},
            {name: 'outStatusImg', label: '出库状态',hidden:true},
            {name: 'styleId', label: '款号',width: 40},
            {name: 'styleName', label: '款名', hidden:true},
            {name: 'colorId', label: '色码', width: 40},
            {name: 'colorName', label: '颜色',hidden:true},
            {name: 'sizeId', label: '尺码',hidden:true},
            {name: 'sizeName', label: '尺码', width: 40},
            {name: 'qty', label: '数量', width: 40},
            {name: 'outQty', label: '已出库数量',hidden:true},
            {name: 'inQty', label: '已入库数量',hidden:true},
            {name: 'sku', label: 'SKU', width: 50},
            {
                name: 'price', label: '销售价格', width: 40,
                editrules: {
                    number: true
                },
                formatter: function (cellValue, options, rowObject) {
                    var price = parseFloat(cellValue).toFixed(2);
                    return price;
                }
            },
            {name: 'totPrice', label: '销售金额',width: 40,
                formatter: function (cellValue, options, rowObject) {
                    var totPrice = parseFloat(cellValue).toFixed(2);
                    return totPrice;
                }
            },
            {
                name: 'discount', label: "折扣", hidden:true, editable: true,
                editrules: {
                    number: true,
                    minValue: 0,
                    maxValue: 100
                }
            },
            {
                name: 'actPrice', label: '实际价格', editable: true,width:40,
                editrules: {
                    number: true,
                    minValue: 0
                },
                formatter: function (cellValue, options, rowObject) {
                    var actPrice = parseFloat(cellValue).toFixed(2);
                    return actPrice;
                }
            },
            {name: 'totActPrice', label: '实际金额', width:40,
                formatter: function (cellValue, options, rowObject) {
                    var totActPrice = parseFloat(cellValue).toFixed(2);
                    return totActPrice;
                }
            },
            {name: 'uniqueCodes', label: '唯一码', hidden: true}
        ],
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: -1,
        pager: '#grid-SaleRetrun-pager',
        multiselect: false,
        shrinkToFit: true,
        sortname: 'id',
        sortorder: "asc",
        footerrow: true,
        cellEdit: true,
        cellsubmit: 'clientArray',
        afterSaveCell: function (rowid, cellname, value, iRow, iCol) {
            if (cellname === "discount") {
                var var_actPrice = Math.round(value * $('#addSaleRetrunDetailgrid').getCell(rowid, "price")) / 100;
                var var_totActPrice = -Math.abs(Math.round(var_actPrice * $('#addSaleRetrunDetailgrid').getCell(rowid, "qty") * 100) / 100);
                $('#addSaleRetrunDetailgrid').setCell(rowid, "actPrice", var_actPrice);
                $('#addSaleRetrunDetailgrid').setCell(rowid, "totActPrice", var_totActPrice);
            } else if (cellname === "actPrice") {
                var var_discount = Math.round(value / $('#addSaleRetrunDetailgrid').getCell(rowid, "price") * 100);
                var var_totActPrice = -Math.abs(Math.round(value * $('#addSaleRetrunDetailgrid').getCell(rowid, "qty") * 100) / 100);
                $('#addSaleRetrunDetailgrid').setCell(rowid, "discount", var_discount);
                $('#addSaleRetrunDetailgrid').setCell(rowid, "totActPrice", var_totActPrice);
            }
            else if (cellname === "qty") {
                $('#addSaleRetrunDetailgrid').setCell(rowid, "totPrice", -Math.abs(Math.round($('#addSaleRetrunDetailgrid').getCell(rowid, "price") * value * 100) / 100));
                $('#addSaleRetrunDetailgrid').setCell(rowid, "totActPrice", -Math.abs(Math.round($('#addSaleRetrunDetailgrid').getCell(rowid, "actPrice") * value * 100) / 100));
            }
            returnSetFooterData();
        },
        afterEditCell: function (rowid, celname, value, iRow, iCol) {
            addDetailgridiRow = iRow;
            addDetailgridiCol = iCol;
        },
        gridComplete: function () {
            returnSetFooterData();
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
    intSaleRetrunDelMessage();
}
function returnSetFooterData() {
   
    var sum_qtymin = $("#addSaleRetrunDetailgrid").getCol('qty', false, 'sum');
    var sum_totActPricemin = $("#addSaleRetrunDetailgrid").getCol('totActPrice', false, 'sum');
    var sum_totPrice = $("#addSaleRetrunDetailgrid").getCol('totPrice',false,'sum');
    sum_totActPrice = sum_totActPricemin.toFixed(0);
    $("#addSaleRetrunDetailgrid").footerData('set', {
        styleId: "合计",
        qty: sum_qtymin,
        totPrice:-Math.abs(sum_totPrice),
        totActPrice: -Math.abs(sum_totActPricemin)
    });
    localStorage.setItem("return_search_actPrice",sum_totActPrice);
    $("#saleRetrunAmount").val( -Math.abs(sum_totActPrice));
    $("#saleRetrunActAmount").val(-Math.abs(sum_totPrice));
}
function onPay(){
    localStorage.setItem("saleAmount", $("#saleAmount").val());
    localStorage.setItem("saleActAmount", $("#saleActAmount").val());
    localStorage.setItem("saleRetrunAmount", $("#saleRetrunAmount").val());
    localStorage.setItem("saleRetrunActAmount", $("#saleRetrunActAmount").val());
    localStorage.setItem("discount", $("#discount").val());
    localStorage.setItem("retrunDiscount", $("#retrunDiscount").val());
    var dtlArray = [];
    $.each($("#addSaleDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addSaleDetailgrid").getRowData(value);
        dtlArray.push(rowData);
    });
    if(dtlArray.length>0){
        localStorage.setItem("saleDel", JSON.stringify(dtlArray));
    }
   
    var dtlRetrunArray = [];
    $.each($("#addSaleRetrunDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addSaleRetrunDetailgrid").getRowData(value);
        dtlRetrunArray.push(rowData);
    });
    if(dtlRetrunArray.length>0){
        localStorage.setItem("saleRetrunDel", JSON.stringify(dtlRetrunArray));
    }
    window.location.href=basePath+'/views/NoOneCashier/payDetailWS.html';
}
function edit_discount_onblur(){
    setDiscount();
}
function setDiscount(){
    if (addDetailgridiRow != null && addDetailgridiCol != null) {
        $("#addDetailgrid").saveCell(addDetailgridiRow, addDetailgridiCol);
        addDetailgridiRow = null;
        addDetailgridiCol = null;
    }
    var discount = $("#discount").val();
    if (discount && discount != null && discount != "") {
        $.each($("#addSaleDetailgrid").getDataIDs(), function (index, value) {
            //判断实际价格是不是小于门店批发价格
            var var_actPrice;
            if((discount*$('#addSaleDetailgrid').getCell(value, "price")/100)<$('#addSaleDetailgrid').getCell(value, "puPrice")){
                $('#addSaleDetailgrid').setCell(value, "discount", parseFloat($('#addSaleDetailgrid').getCell(value, "puPrice")/$('#addSaleDetailgrid').getCell(value, "price")).toFixed(2)*100);
                var_actPrice =  $('#addSaleDetailgrid').getCell(value, "puPrice");
                $('#addSaleDetailgrid').setCell(value, "actPrice", $('#addSaleDetailgrid').getCell(value, "puPrice"));
                $('#addSaleDetailgrid').setCell(value, "abnormalStatus",1);
                
            }else{
                $('#addSaleDetailgrid').setCell(value, "discount", discount);
                var_actPrice = Math.round(discount * $('#addSaleDetailgrid').getCell(value, "price")) / 100;
                $('#addSaleDetailgrid').setCell(value, "actPrice", var_actPrice);
                $('#addSaleDetailgrid').setCell(value, "abnormalStatus", 0);
                
            }
            var var_totActPrice = Math.round(var_actPrice * parseInt($('#addSaleDetailgrid').getCell(value, "qty")) * 100) / 100;
            $('#addSaleDetailgrid').setCell(value, "totActPrice", var_totActPrice);
            //$("#grid-table").setCell(value,"useable",0,{color:'red'});
        });
        setFooterData();
        $("#retrunDiscount").val(discount);

    }
} 
function  setRetrunDiscount(){
    if (addDetailgridiRow != null && addDetailgridiCol != null) {
        $("#addDetailgrid").saveCell(addDetailgridiRow, addDetailgridiCol);
        addDetailgridiRow = null;
        addDetailgridiCol = null;
    }
    var retrunDiscount = $("#retrunDiscount").val();
    if (retrunDiscount && retrunDiscount != null && retrunDiscount != "") {
        $.each($("#addSaleRetrunDetailgrid").getDataIDs(), function (index, value) {
            var var_actPrice;
            $('#addSaleRetrunDetailgrid').setCell(value, "discount", retrunDiscount);
            var_actPrice = Math.round(retrunDiscount * $('#addSaleRetrunDetailgrid').getCell(value, "price")) / 100;
            $('#addSaleRetrunDetailgrid').setCell(value, "actPrice", var_actPrice);
            $('#addSaleRetrunDetailgrid').setCell(value, "abnormalStatus", 0);
            var var_totActPrice = Math.round(var_actPrice * parseInt($('#addSaleRetrunDetailgrid').getCell(value, "qty")) * 100) / 100;
            $('#addSaleRetrunDetailgrid').setCell(value, "totActPrice", var_totActPrice);
            //$("#grid-table").setCell(value,"useable",0,{color:'red'});
        });
    }
    returnSetFooterData();
}
function edit_retrundiscount_onblur(){
    setRetrunDiscount();
}
function input_keydown() {
    $("#discount").keydown(function (event) {
        if (event.keyCode == 13) {
            setDiscount();
        }
    })
    $("#retrunDiscount").keydown(function (event) {
        if (event.keyCode == 13) {
            setRetrunDiscount();
        }
    })
}
function onBack(){
    window.location.href=basePath+'/views/NoOneCashier/selectSaleRetrunOrPayWS.html';
}