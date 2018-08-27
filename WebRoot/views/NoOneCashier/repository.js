/**
 * Created by lly on 2018/8/25.
 */
$(function () {
    var wsUri ="ws://127.0.0.1:4649/csreader";
    websocket = new WebSocket(wsUri);
    websocket.onopen = function(evt) { onOpen(evt) };
    websocket.onclose = function(evt) { onClose(evt) };
    websocket.onmessage = function(evt) { onMessage(evt) };
    websocket.onerror = function(evt) { onError(evt) };
    loadTable();
    initSelectOrigForm();
    //得到表格的宽度
    var tableWith=$("#sale").width();
    var oneWith=tableWith/12;
    $("#scanning").css("left","10");
    $("#scanning").css("width",oneWith);
    $("#stop").css("left",oneWith+oneWith+"");
    $("#stop").css("width",oneWith);
    $("#save").css("left",oneWith+oneWith+oneWith+oneWith+"");
    $("#save").css("width",oneWith);
    $("#addCommodity").css("left",oneWith+oneWith+oneWith+oneWith+oneWith+oneWith+"");
    $("#addCommodity").css("width",oneWith);
    $("#back").css("right","10px");
    $("#back").css("width",oneWith);
    initTable();
});
function initTable() {
    var addProductInfo = JSON.parse(localStorage.getItem("addProductInfo")); //转换为json对象
    console.log(addProductInfo);
    var isAdd = true;
    var alltotActPrice = 0;
    if(addProductInfo != null && addProductInfo!= "" && addProductInfo!=undefined){
        $.each(addProductInfo, function (index, value) {
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
        setFooterData();
    }


}
function loadTable() {
    $("#uniqueCodeListGrid").jqGrid({
        height: 400,
        datatype:"local",
        mtype: "POST",
        colModel: [
            {name: 'code', label: '唯一码', width: 150},
            {name: 'sku', label: 'SKU', width: 150},
            {name: 'warehouseId', label: '仓库', hidden:true},
            {name: 'floor',label:'仓库名',width:150},
            {name: 'inStock', label: '库存状态', hidden: true},
            {
                name: '', label: '库存状态', width: 110,
                formatter: function (cellValue, options, rowObject) {
                    switch (rowObject.inStock) {
                        case 1:
                            return "在库";
                        case 0:
                            return "不在库";
                        default:
                            return "";
                    }
                }
            }
        ],
        rownumbers: true,
        viewrecords: true,
        autowidth: true,
        altRows: true,
        multiselect: false,
        shrinkToFit: true,
        sortname: 'code',
        sortorder: "desc"
    });
    var parent_column = $("#uniqueCodeGrid").closest('.modal-dialog');
    $("#uniqueCodeGrid").jqGrid('setGridWidth', parent_column.width());
}
function initAllCodesList() {
    allCodeStrInDtl = "";
    $.each($("#addDetailgrid").getDataIDs(), function (index, value) {
        var rowData = $("#addDetailgrid").getRowData(value);
        allCodeStrInDtl = allCodeStrInDtl + "," + rowData.uniqueCodes;
    });
    if (allCodeStrInDtl.substr(0, 1) == ",") {
        allCodeStrInDtl = allCodeStrInDtl.substr(1);
    }
}
function back() {
    window.location.href=basePath+'/views/NoOneCashier/depositDel.html';
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
                    productInfo.discount=100;
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
                $.each($("#addDetailgrid").getDataIDs(), function (dtlIndex, dtlValue) {
                    var dtlRow = $("#addDetailgrid").getRowData(dtlValue);
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

function addCommodity() {

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
function initEditFormValid() {
    $('#editForm').bootstrapValidator({
        message: '输入值无效',
        excluded: [':disabled'],
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        submitHandler: function (validator, form, submitButton) {
            $.post(form.attr('action'), form.serialize(), function (result) {
                if (result.success == true || result.success == 'true') {
                } else {
                    // Enable the submit buttons
                    $('#editForm').bootstrapValidator('disableSubmitButtons', false);
                }
            }, 'json');
        },
        fields: {
            billNo: {
                validators: {}
            },
            billDate: {
                validators: {
                    notEmpty: {
                        message: '请选择单据日期'
                    }
                }
            },
            origId: {
                validators: {
                    notEmpty: {
                        message: '请选择入库仓库'
                    }
                }
            },
            newRmId: {
                validators: {
                    callback: {
                        message: '请选择入库库位',
                        callback: function (value, validator) {
                            if ($.trim(value) === $.trim("--请选择入库库位--")) {
                                return false;
                            }
                            return true;
                        }
                    }
                }
            }
        }
    });
}
function initSelectOrigForm() {
    var searchOrigIdUrl="";
    searchOrigIdUrl=basePath + "/unit/listWS.do?filter_EQI_type=9";
    $.ajax({
        url: searchOrigIdUrl,
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            var json = data;
            $("#search_origId").append("<option value=''>--请选择仓库--</option>");
            for (var i = 0; i < json.length; i++) {
                $("#search_origId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                $("#edit_origId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
            }
        }
    });

}