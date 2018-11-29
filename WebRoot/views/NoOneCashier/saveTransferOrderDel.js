$(function () {
    $("#footer").load("../layout/footer_js.jsp");
    loadingwebsocket();
    websocket.onopen = function(evt) { onOpen(evt) };
    websocket.onclose = function(evt) { onClose(evt) };
    websocket.onmessage = function(evt) { onMessage(evt) };
    websocket.onerror = function(evt) { onError(evt) };
    //得到表格的宽度
    var tableWith=$("#transferOrder").width();
    var oneWith=tableWith/12;
    $("#scanning").css("left","10");
    $("#scanning").css("width",oneWith);
    $("#stop").css("left",oneWith+oneWith+"");
    $("#stop").css("width",oneWith);
    $("#save").css("left",oneWith+oneWith+oneWith+oneWith+"");
    $("#save").css("width",oneWith);
    $("#clear").css("left",oneWith+oneWith+oneWith+oneWith+oneWith+oneWith+"");
    $("#clear").css("width",oneWith);
    $("#back").css("right","10px");
    $("#back").css("width",oneWith);
    loadTable();
    findOrigIdByUser();
    initSelectDestForm();

});
function loadTable() {
    $("#addSaleDetailgrid").jqGrid({
        height: "40%",
        datatype: "local",
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
            {name: 'uniqueCodes', label: '唯一码', hidden: true}
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
                    productInfo.qty = 1;
                    var ct = localStorage.getItem("unitType");
                    productInfo.sku=productInfo.code;
                    productInfo.puPrice=productInfo.price;
                    productInfo.wsPrice=productInfo.price;
                    productInfo.actPrice=productInfo.price;
                    productInfo.stockPrice = productInfo.price;
                   /* if(localStorage.getItem("discount")!=undefined&&localStorage.getItem("discount")!=""&&localStorage.getItem("discount")!=null){
                        productInfo.discount=localStorage.getItem("discount");
                    }else{
                        productInfo.discount=100;
                    }*/
                    productInfo.actPrice = Math.round(productInfo.price * productInfo.discount) / 100;
                    productInfo.abnormalStatus=0;
                    productInfo.outQty = 0;
                    productInfo.inQty = 0;
                    productInfo.status = 0;
                    productInfo.inStatus = 0;
                    productInfo.outStatus = 0;
                    productInfo.uniqueCodes = productInfo.unicode;
                    productInfo.totPrice = productInfo.price;
                    productInfo.totActPrice = productInfo.actPrice;
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
                $.each($("#addSaleDetailgrid").getDataIDs(), function (dtlIndex, dtlValue) {
                    var dtlRow = $("#addSaleDetailgrid").getRowData(dtlValue);
                    if (value.sku === dtlRow.sku) {
                        if (dtlRow.uniqueCodes.indexOf(value.unicode) !== -1) {
                            isAdd = false;
                        }else {
                            dtlRow.qty = parseInt(dtlRow.qty) + 1;
                            dtlRow.totPrice = dtlRow.qty * dtlRow.price;
                            dtlRow.totActPrice = dtlRow.qty * dtlRow.actPrice;
                            alltotActPrice += dtlRow.qty * dtlRow.actPrice;
                            dtlRow.uniqueCodes = dtlRow.uniqueCodes + "," + value.unicode;
                            if (dtlRow.id) {
                                $("#addSaleDetailgrid").setRowData(dtlRow.id, dtlRow);
                            } else {
                                $("#addSaleDetailgrid").setRowData(dtlIndex, dtlRow);
                            }
                            isAdd = false;
                        }
                    }
                });
                if (isAdd) {
                    $("#addSaleDetailgrid").addRowData($("#addSaleDetailgrid").getDataIDs().length, value);
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
function save() {
    var search_destId=$("#search_destId").val();
    if(search_destId==""&&search_destId==undefined){
        bootbox.alert("请选择调入仓库");
        return;
    }
    var billDate=$("#billDate").val();
    if(billDate==""&&billDate==undefined){
        bootbox.alert("请选择时间");
        return;
    }
    checkAndSave();

}
function checkAndSave() {
    var dtlArray = [];
    $.each($("#addSaleDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addSaleDetailgrid").getRowData(value);
        dtlArray.push(rowData);
    });
    console.log(dtlArray)
    $.ajax({
        dataType: "json",
        url: basePath + "/pad/scanning/checkEpcStockOnWebTransferWS.do",
        cache: false,
        async: false,
        data:{
            warehId:localStorage.getItem("defaultWarehId"),
            type:0,
            strDtlList:JSON.stringify(dtlArray)
        },
        type: "POST",
        success: function (msg) {
            console.log(msg)
           if(msg.success){
               saveTransfer(msg.result);
               //window.location.href=basePath+'/views/NoOneCashier/convertOutTransferOrderDel.html';
            }else{
               bootbox.alert(msg.msg);
           }
        }
    });
    //window.location.href=basePath+'/views/NoOneCashier/convertOutTransferOrderDel.html';
}
function saveTransfer(result) {
        cs.showProgressBar();
        var userId=localStorage.getItem("userId");
        $("#edit_origId").removeAttr('disabled');
        $("#edit_destId").removeAttr('disabled');

        if (localStorage.getItem("defaultWarehId") == localStorage.getItem("transferOrderDestUnitId")) {
            bootbox.alert("请选择不同的仓库进行调拨");
            cs.closeProgressBar();
            return;
        }

        /*if(userId !== 'admin'){
            if($("#defaultWarehId").val() !== curOwnerId && $("#transferOrderDestUnitId").val() !== curOwnerId){
                bootbox.alert("发货方或出货方必须有一个是本店");
                cs.closeProgressBar();
                return;
            }
        }*/



        console.log($("#edit_status").val());
       /* if (editDtailRowId !== null) {
            $("#addDetailgrid").saveRow(editDtailRowId);
            editDtailRowId = null;
        }*/
        if ($("#addSaleDetailgrid").getDataIDs().length === 0) {
            bootbox.alert("请添加入库商品");
            cs.closeProgressBar();
            return;
        }
           /* var dtlArray = [];
            $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
                var rowData = $("#addDetailgrid").getRowData(value);
                dtlArray.push(rowData);
            });*/
        $("#billDate").val(updateTime($("#billDate").val()));
        var bill={};
        bill.origUnitId=localStorage.getItem("unitId");
        bill.origUnitName=localStorage.getItem("unitName");
        bill.destUnitId=localStorage.getItem("transferOrderDestUnitId");
        bill.destUnitName=localStorage.getItem("transferOrderDestUnitName");
        bill.origId=localStorage.getItem("defaultWarehId");
        bill.destId=$('#search_destId').val();
         bill.billDate=$('#billDate').val();
        $.ajax({
            dataType: "json",
            // async:false,
            url: basePath + "/logistics/transferOrder/saveWS.do",
            data: {
                transferOrderBillStr: JSON.stringify(bill),
                strDtlList: JSON.stringify(result),
                userId: userId
            },
            type: "POST",
            success: function (msg) {
                cs.closeProgressBar();
                if (msg.success) {
                    $.gritter.add({
                        text: msg.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                    localStorage.setItem("transferOrderBillNo",msg.result.billNo);
                    localStorage.setItem("transferOrderBill",JSON.stringify(bill));
                    window.location.href=basePath+'/views/NoOneCashier/convertOutTransferOrderDel.html';
                } else {
                    bootbox.alert(msg.msg);
                }
            }
        });

}
function initSelectDestForm() {
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
           /* $(".selectpicker").selectpicker('refresh');*/
        }
    });
}
function fullMessage() {
    $("#origUnitName").val(localStorage.getItem("unitName"));
    $("#origId").val(localStorage.getItem("defaultWarehName"))
    $("#destUnitName").val(localStorage.getItem("transferOrderDestUnitName"))
}
function findOrigIdByUser() {
    $.ajax({
        dataType: "json",
        url: basePath + "/api/hub/base/getDefaultwarehouseWs.do",
        cache: false,
        async: false,
        data:{
            userId:localStorage.getItem("userId")
        },
        type: "POST",
        success: function (msg) {
            if(msg.success){
                localStorage.setItem("defaultSaleStaffId", msg.result.defaultSaleStaffId);
                localStorage.setItem("defaultSaleStaffName", msg.result.defaultSaleStaffName);
                localStorage.setItem("defaultWarehId",msg.result.defaultWarehId);
                localStorage.setItem("defaultWarehName",msg.result.defaultWarehouseName);
                localStorage.setItem("unitName",msg.result.name);
                localStorage.setItem("unitId",msg.result.id);
                fullMessage();
            }
        }
    });
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
function onBack() {
    window.location.href=basePath+'/views/NoOneCashier/selectTransferred.html';
}
function onClear() {
    $("#addSaleDetailgrid").clearGridData();
}