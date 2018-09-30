/**
 * Created by lly on 2018/9/29.
 */
var searchUrl = basePath + "/search/shopTurnover/getPriceCount.do";
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
    var sum_totPrice = $("#grid").getCol('totPrice', false, 'sum');
    var sum_recivePrice = $("#grid").getCol('recivePrice', false, 'sum');
    var sum_savePrice = $("#grid").getCol('savePrice', false, 'sum');
    var sum_returnPrice = $("#grid").getCol('returnPrice', false, 'sum');
    $("#grid").footerData('set', {
        shopName: "合计",
        totPrice: sum_totPrice,
        recivePrice: sum_recivePrice,
        savePrice: sum_savePrice,
        returnPrice: sum_returnPrice
    });
}