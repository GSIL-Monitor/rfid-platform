/**
 * Created by lly on 2018/9/30.
 */
var searchUrl = basePath + "/search/shopTurnover/getDetail.do?filter_EQS_status=1&userId=" + userId+"&filter_EQS_shop="+shopId+"&filter_EQS_payType"+payType;
$(function () {
    initGrid();
});
function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        mtype: "POST",
        url: basePath + "/search/shopTurnover/getDetail.do?filter_EQS_status=1&userId=" + userId+"&filter_EQS_shop="+shopId+"&filter_EQS_payType"+payType,
        datatype: "json",
        colModel: [
            {name: 'id', label: '销售单号+支付方式', hidden: true, width: 150},
            {name: 'shop', label: '店铺id', hidden: true, width: 150},
            {name: 'shopName', label: '店铺名', sortable: true, width: 150},
            {name: 'customerId', label: '客户', hidden: true, width: 150},
            {name: 'customerName', label: '客户名', sortable: true, width: 150},
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
            {name: 'payPrice', label: '应付金额', sortable: false, width: 150},
            {name: 'actPayPrice', label: '实付金额', sortable: false, width: 150},
            {
                name: 'returnPrice', label: '找零', width: 150,
                formatter: function (cellvalue, options, rowObject) {
                    if(cellvalue == null || cellvalue == "" ){
                        return 0;
                    }
                    else {
                        return cellvalue;
                    }
                }
            },
            {
                name: 'donationPrice', label: '赠送金额', width: 150,
                formatter: function (cellvalue, options, rowObject) {
                    if(cellvalue == null || cellvalue == ""){
                        return 0;
                    }
                    else {
                        return cellvalue;
                    }
                }
            },
            {
                name: 'billType', label: '账单类型', width: 150,
                formatter: function (cellvalue, options, rowObject) {
                    switch(cellvalue){
                        case '0':
                            return '收款';
                        case '1':
                            return '储值';
                        case '2':
                            return '付款';
                    }
                }
            },
            {name: 'billNo', label: '销售单号', hidden: true, width: 150},
            {name: 'returnBillNo', label: '退货单号', hidden: true, width: 150}
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

        }
    });
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
