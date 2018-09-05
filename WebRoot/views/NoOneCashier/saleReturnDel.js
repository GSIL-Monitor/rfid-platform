
$(function () {
    var wsUri ="ws://127.0.0.1:4649/csreader";
    websocket = new WebSocket(wsUri);
    websocket.onopen = function(evt) { onOpen(evt) };
    websocket.onclose = function(evt) { onClose(evt) };
    websocket.onmessage = function(evt) { onMessage(evt) };
    websocket.onerror = function(evt) { onError(evt) };
    loadTable();
    //得到表格的宽度
    var tableWith=$("#sale").width();
    var oneWith=tableWith/12;
    $("#scanning").css("left","10");
    $("#scanning").css("width",oneWith);
    $("#stop").css("left",oneWith+oneWith+"");
    $("#stop").css("width",oneWith);
    $("#save").css("left",oneWith+oneWith+oneWith+oneWith+"");
    $("#save").css("width",oneWith);
    $("#check").css("left",oneWith+oneWith+oneWith+oneWith+oneWith+oneWith+"");
    $("#check").css("width",oneWith);
    $("#pay").css("left",oneWith+oneWith+oneWith+oneWith+oneWith+oneWith+oneWith+oneWith+"");
    $("#pay").css("width",oneWith);
    $("#back").css("right","10px");
    $("#back").css("width",oneWith);
    initTable();
});
function initTable() {
    var retrunProductInfo = JSON.parse(localStorage.getItem("addProductInfo")); //转换为json对象
    console.log(retrunProductInfo);
    var isAdd = true;
    var alltotActPrice = 0;
    if(retrunProductInfo != null && retrunProductInfo!= "" && retrunProductInfo!=undefined){
        $.each(retrunProductInfo, function (index, value) {
            console.info(value);
            isAdd = true;
            $.each($("#addDetailgrid").getDataIDs(), function (dtlIndex, dtlValue) {
                var dtlRow = $("#addDetailgrid").getRowData(dtlValue);
                if (value.sku === dtlRow.sku) {
                    if (dtlRow.uniqueCodes.indexOf(value.code) != -1) {
                        isAdd = false;
                        $.gritter.add({
                            text: value.code + "不能重复添加",
                            class_name: 'gritter-success  gritter-light'
                        });
                        return true;
                    }
                    dtlRow.qty = parseInt(dtlRow.qty) + 1;
                    dtlRow.totPrice = dtlRow.qty * dtlRow.price;
                    dtlRow.totActPrice = dtlRow.qty * dtlRow.actPrice;
                    alltotActPrice += dtlRow.qty * dtlRow.actPrice;
                    dtlRow.uniqueCodes = dtlRow.uniqueCodes + "," + value.code;
                    console.info(dtlRow);
                    if (dtlRow.id) {
                        $("#addDetailgrid").setRowData(dtlRow.id, dtlRow);
                    } else {
                        $("#addDetailgrid").setRowData(dtlIndex, dtlRow);
                    }
                    isAdd = false;
                }
            });
            if (isAdd) {
                $("#addDetailgrid").addRowData($("#addDetailgrid").getDataIDs().length, value);
            }
        });
    }
    setFooterData();

}
function loadTable() {
    $("#addDetailgrid").jqGrid({
        height: "auto",
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
                    return parseFloat(cellValue).toFixed(2);
                }
            },
            {name: 'totPrice', label: '销售金额',width: 40,
                formatter: function (cellValue, options, rowObject) {
                    return parseFloat(cellValue).toFixed(2);
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
                    return parseFloat(cellValue).toFixed(2);
                }
            },
            {name: 'totActPrice', label: '实际金额', width:40,
                formatter: function (cellValue, options, rowObject) {
                    return parseFloat(cellValue).toFixed(2);
                }
            },
            {name: 'uniqueCodes', label: '唯一码', hidden: true}
        ],
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: -1,
        pager: '#grid-pager',
        multiselect: false,
        shrinkToFit: true,
        sortname: 'id',
        sortorder: "asc",
        footerrow: true,
        cellsubmit: 'clientArray',
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

}
function setFooterData() {
    var sum_qtymin = $("#addDetailgrid").getCol('qty', false, 'sum');
    var sum_totActPricemin = $("#addDetailgrid").getCol('totActPrice', false, 'sum');
    var sum_totPrice = $("#addDetailgrid").getCol('totPrice',false,'sum');
    sum_totActPrice = sum_totActPricemin.toFixed(0);
    $("#addDetailgrid").footerData('set', {
        styleId: "合计",
        qty: sum_qtymin,
        totPrice:-Math.abs(sum_totPrice),
        totActPrice: -Math.abs(sum_totActPricemin)
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
/*
扫描
 */
function onScanning() {
    var msg={
        "cmd":"10002"
    };
    sendMessgeToServer(msg);
}
function onOpen(evt) {
    /*showMessage("连接 Reader Server成功");*/
}
function onClose(evt) {
    /*if(evt.code == 1005){
        /!*showMessage('与服务器连接已断开');*!/
    }else if(evt.code == 1006){
        /!*showMessage('连接服务器失败',true);*!/
    }*/
}
function onMessage(evt) {
    var skuInfo = [];
    var res = JSON.parse(evt.data);
    var unicodes = [];
    var productListInfo = [];
    if (res.cmd === "10006") {
        $.each(res.data,function (index,value) {
            if (value!==null&&value.skuInfo!==null){
                skuInfo.push(value.skuInfo);
            }
        });
        if (skuInfo!==null) {
            console.log(skuInfo.unicode);
            unicodes.push(skuInfo.unicode);
            $.each(skuInfo, function (index, value) {
                var productInfo = value;
                if(productInfo.unicode!==""&&productInfo.unicode!==undefined){
                    productInfo.qty = -1;
                    var ct = localStorage.getItem("unitType");
                    productInfo.sku=productInfo.code;
                    productInfo.puPrice=productInfo.price;
                    productInfo.wsPrice=productInfo.price;
                    productInfo.actPrice=productInfo.price;
                    productInfo.stockPrice = productInfo.price;
                    if(localStorage.getItem("discount")!=undefined&&localStorage.getItem("discount")!=""&&localStorage.getItem("discount")!=null){
                        productInfo.discount=localStorage.getItem("discount");
                    }else{
                        productInfo.discount=100;
                    }
                    productInfo.actPrice = Math.round(productInfo.price * productInfo.discount) / 100;
                    productInfo.abnormalStatus=0;
                    productInfo.outQty = 0;
                    productInfo.inQty = 0;
                    productInfo.status = 0;
                    productInfo.inStatus = 0;
                    productInfo.outStatus = 0;
                    productInfo.uniqueCodes = productInfo.unicode;
                    productInfo.totPrice = -productInfo.price;
                    productInfo.totActPrice = -productInfo.actPrice;
                    var stylePriceMap={};
                    stylePriceMap['price']=productInfo.price;
                    stylePriceMap['wsPrice']=productInfo.wsPrice;
                    stylePriceMap['puPrice']=productInfo.puPrice;
                    productInfo.stylePriceMap=JSON.stringify(stylePriceMap);
                    delete productInfo.id;
                    productListInfo.push(productInfo);
                }
            });
            var alltotActPrice = 0;
            $.each(productListInfo, function (index, value) {
                isAdd = true;
                value.sku=value.code;
                $.each($("#addDetailgrid").getDataIDs(), function (dtlIndex, dtlValue) {
                    var dtlRow = $("#addDetailgrid").getRowData(dtlValue);
                    if (value.sku === dtlRow.sku) {
                        if (dtlRow.uniqueCodes.indexOf(value.unicode) !== -1) {
                            isAdd = false;
                        }else {
                            dtlRow.qty = parseInt(dtlRow.qty) -1;
                            dtlRow.totPrice = dtlRow.qty * dtlRow.price;
                            dtlRow.totActPrice = dtlRow.qty * dtlRow.actPrice;
                            alltotActPrice += dtlRow.qty * dtlRow.actPrice;
                            dtlRow.uniqueCodes = dtlRow.uniqueCodes + "," + value.unicode;
                            if (dtlRow.id) {
                                $("#addDetailgrid").setRowData(dtlRow.id, dtlRow);
                            } else {
                                $("#addDetailgrid").setRowData(dtlIndex, dtlRow);
                            }
                            isAdd = false;
                        }
                    }
                });
                if (isAdd) {
                    $("#addDetailgrid").addRowData($("#addDetailgrid").getDataIDs().length, value);
                }
            });
            setFooterData();
        }
        //所有唯一码unicodes
        console.log(skuInfo);
    } else {

    }
}
function onError(evt) {
    /*showMessage('发生错误' + evt.data,true);*/
}
/*
停止
 */
function stop() {
    if (timeout !== null) {
        window.clearInterval(timeout);
    }
    var msg={
        "cmd":"10003"
    };
    sendMessgeToServer(msg)
}

function pay() {
    window.location.href=basePath+'/views/NoOneCashier/payDetailWS.html';
}
function sendMessgeToServer(message) {

    if (typeof websocket==="undefined"){
       /* showMessage('websocket还没有连接，或者连接失败，请检测',true);*/
        return false;
    }
    if (websocket.readyState===3) {
        /*showMessage('websocket已经关闭，请重新连接',true);*/
        return false;
    }
    console.log(websocket);
    var data = websocket.send(JSON.stringify(message));
    console.log(data);
}
function save() {
    checkAndSave();

}
function checkAndSave() {
    var dtlArray = [];
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        dtlArray.push(rowData);
    });
    debugger
    $.ajax({
        dataType: "json",
        url: basePath + "/pad/scanning/checkEpcStockOnWebWS.do",
        cache: false,
        async: false,
        data:{
            warehId:localStorage.getItem("defaultWarehId"),
            type:1,
            strDtlList:JSON.stringify(dtlArray)
        },
        type: "POST",
        success: function (msg) {
            console.log(msg)
            debugger
            if(msg.success){
                localStorage.setItem("statusRetrun", true);
                localStorage.setItem("saleRetrunDel", JSON.stringify(dtlArray));
                console.log(JSON.parse(  localStorage.getItem("saleRetrunDel")));
                window.location.href='./selectSaleRetrunOrPay.html';
            }else{
                localStorage.setItem("statusRetrun", false);
                localStorage.setItem("saleRetrunDel", JSON.stringify(dtlArray));
            }
        },
        timeout:function(msg){
            localStorage.setItem("statusRetrun", false);
            localStorage.setItem("saleRetrunDel", JSON.stringify(dtlArray));
             window.location.href='./selectSaleRetrunOrPay.html';
            
        },
        error:function(msg){
            localStorage.setItem("statusRetrun", false);
            localStorage.setItem("saleRetrunDel", JSON.stringify(dtlArray));
            window.location.href=basePath+'/views/NoOneCashier/selectSaleRetrunOrPayWS.html';
        }
    });
}
function onBack(){
    window.location.href=basePath+'/views/NoOneCashier/selectSaleOrSaleRetrunWS.html';
}