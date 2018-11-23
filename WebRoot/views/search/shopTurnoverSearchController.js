/**
 * Created by lly on 2018/9/29.
 */
var searchUrl = basePath + "/search/shopTurnover/getPriceCount.do";
var sum_totPrice = 0;//总金额
var sum_recivePrice = 0;//总收金额
var sum_savePrice = 0;//储值金额
var sum_returnPrice = 0;//付款金额
$(function () {
    initSelectShopForm();
    initGrid();
    $("input[name='GED_billDate']").val(getToDay("yyyy-MM-dd"));
    //$("#edit_payType").selectpicker('val',defaultPayType);
    $("#edit_shopId").selectpicker('val',ownerId);
    $(".selectpicker").selectpicker('refresh');
    _search();
});
function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        mtype: "POST",
        datatype: "local",
        colModel: [
            {name: 'shop', label: '店铺id', hidden: true, width: 150},
            {name: 'shopName', label: '店铺名', sortable: true, width: 150},
            {
                name: 'payType', label: '支付方式', sortable: true, width: 150,
                formatter: function (cellvalue, options, rowObject) {
                    switch(cellvalue){
                        case 'xianjinzhifu':
                            return '现金支付';
                        case 'wechatpay':
                            return '微信支付';
                        case 'zhifubaozhifu':
                            return '支付宝支付';
                        case 'cardpay':
                            return '刷卡支付';
                        case 'yuezhifu':
                            return '余额支付';
                    }
                }
            },

            {name: 'payDate', label: '支付时间', sortable: true, width: 150},
            {name: 'totPrice', label: '总金额', width: 150},
            {name: 'recivePrice', label: '收款金额', sortable: false, width: 150},
            {name: 'savePrice', label: '储值金额', sortable: false, width: 150},
            {name: 'returnPrice', label: '付款金额', width: 150},
            {
                name: "", label: "查看明细", width: 50, editable: false, align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    var shopId = rowObject.shop;
                    var payType = rowObject.payType;
                    var html;
                    html = "<a href='" + basePath + "/search/shopTurnover/priceDetail.do?shopId=" + shopId + "&payType=" + payType + "'><i class='ace-icon fa fa-list' title='查看明细'></i></a>";
                    return html;
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
        sortname: 'id',
        sortorder: 'desc',
        autoScroll: false,
        footerrow: true,
        gridComplete: function () {
            setFooterData();
        }
    });
}


function initSelectShopForm() {
    var url = basePath + "/sys/shop/search.do?filter_EQI_type=4&filter_EQS_code="+ownerId;
    if(userId == 'admin'){
        url = basePath + "/sys/shop/search.do?filter_EQI_type=4";
    }
    $.ajax({
        url: url,
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#edit_shopId").empty();
            $("#edit_shopId").append("<option value=''>--请选择--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#edit_shopId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
            }
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
        postData: params,
        datatype: 'json'
    });
    $("#grid").trigger("reloadGrid");
}
function setFooterData() {
    sum_totPrice = $("#grid").getCol('totPrice', false, 'sum');
    sum_recivePrice = $("#grid").getCol('recivePrice', false, 'sum');
    sum_savePrice = $("#grid").getCol('savePrice', false, 'sum');
    $("#grid").footerData('set', {
        shopName: "合计",
        totPrice: sum_totPrice,
        recivePrice: sum_recivePrice,
        savePrice: sum_savePrice,
        returnPrice: sum_returnPrice
    });
}
function doPrint() {

    $("#edit-dialog-print").modal('show');
    $("#edit-dialog-print").show();
    $.ajax({
        dataType: "json",
        url: basePath + "/sys/printset/findPrintSetListByOwnerId.do",
        type: "POST",
        data: {
            type:"ST"
        },
        success: function (msg) {

            if (msg.success) {
                var addcont = "";
                for (var i = 0; i < msg.result.length; i++) {
                    addcont += "<div class='form-group' onclick=set('" + msg.result[i].id + "') title='" + msg.result[i].name + "'>" +
                        "<button class='btn btn-info'>" +
                        "<i class='cae-icon fa fa-refresh'></i>" +
                        "<span class='bigger-10'>套打" + msg.result[i].name + "</span>" +
                        "</button>" +
                        "</div>"
                }
                $("#addbutton").html(addcont);

            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}

function set(id) {
    $("#id").val(id);
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $.ajax({
        dataType: "json",
        url: basePath + "/sys/printset/printCountMessage.do",
        data: params,
        type: "POST",
        success: function (msg) {
            if (msg.success) {
                var wechatpay = 0;//微信金额
                var alipay = 0;//支付宝金额
                var xianjinzhifu = 0;//现金金额
                var cardpay = 0;//刷卡金额
                var yuezhifu = 0;//余额支付金额
                var salecycle = "";//销售周期
                if($("input[name='LED_billDate']").val() != "" && $("input[name='LED_billDate']").val() != null){
                    salecycle = $("input[name='GED_billDate']").val()+"--"+$("input[name='LED_billDate']").val();
                }
                else {
                    salecycle = $("input[name='GED_billDate']").val()+"--"+getToDay("yyyy-MM-dd");
                }
                var print = msg.result.print;
                var cont = msg.result.cont;
                var contDel = msg.result.contDel;
                for (var a = 0; a < contDel.length; a++) {
                    var conts = contDel[a];
                    if(conts.payType == "wechatpay"){
                        wechatpay += conts.totPrice;
                    }
                    else if(conts.payType == "zhifubaozhifu"){
                        alipay += conts.totPrice;
                    }
                    else if(conts.payType == "xianjinzhifu"){
                        xianjinzhifu += conts.totPrice;
                    }
                    else if(conts.payType == "cardpay"){
                        cardpay += conts.totPrice;
                    }
                    else if(conts.payType == "yuezhifu") {
                        yuezhifu += conts.totPrice;
                    }
                }
                var LODOP = getLodop();
                eval(print.printCont);
                var printCode = print.printCode;
                var printCodes = printCode.split(",");
                for (var i = 0; i < printCodes.length; i++) {
                    var plp = printCodes[i];
                    var message = cont[plp];
                    if (message != "" && message != null && message != undefined) {
                        LODOP.SET_PRINT_STYLEA(printCodes[i], 'Content', message);
                    } else {
                        LODOP.SET_PRINT_STYLEA(printCodes[i], 'Content', "");
                    }
                    if(printCodes[i] == "totalPrice"){
                        LODOP.SET_PRINT_STYLEA(printCodes[i], 'Content', sum_totPrice);
                    }else if(printCodes[i] == "xianjinzhifu"){
                        LODOP.SET_PRINT_STYLEA(printCodes[i], 'Content', xianjinzhifu);
                    }else if(printCodes[i] == "wechat"){
                        LODOP.SET_PRINT_STYLEA(printCodes[i], 'Content', wechatpay);
                    }else if(printCodes[i] == "alipay"){
                        LODOP.SET_PRINT_STYLEA(printCodes[i], 'Content', alipay);
                    }else if(printCodes[i] == "cardPay"){
                        LODOP.SET_PRINT_STYLEA(printCodes[i], 'Content', cardpay);
                    }else if(printCodes[i] == "yuezhifu"){
                        LODOP.SET_PRINT_STYLEA(printCodes[i], 'Content', yuezhifu);
                    }else if(printCodes[i] == "countTime"){
                        LODOP.SET_PRINT_STYLEA(printCodes[i], 'Content', salecycle);
                    }

                }
                LODOP.PRINT();
                $("#edit-dialog-print").hide();
            } else {
                bootbox.alert(msg.msg);
            }
        }
    });
}