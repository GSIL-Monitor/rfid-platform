/**
 * Created by lly on 2018/9/29.
 */
var searchUrl = basePath + "/search/shopTurnover/getPriceCount.do";
$(function () {
    initSelectShopForm();
    initGrid();
    $("#edit_payType").selectpicker('val',defaultPayType);
    $(".selectpicker").selectpicker('refresh');
});
function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        mtype: "POST",
        datatype: "json",
        url: searchUrl,
        colModel: [
            {name: 'shop', label: '店铺', sortable: true, width: 150},
            {name: 'payType', label: '支付方式', sortable: true, width: 150},
            {name: 'payDate', label: '支付事件', sortable: true, width: 150},
            {name: 'recivePrice', label: '收款金额', sortable: false, width: 150},
            {name: 'savePrice', label: '储值金额', sortable: false, width: 150},
            {name: 'returnPrice', label: '付款金额', width: 150},
            {name: 'totPrice', label: '总金额', width: 150},
            {
                name: "", label: "查看明细", width: 50, editable: false, align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    var guestId = rowObject.id;
                    var html;
                    html = "<a href='" + basePath + "/sys/guestAccount/viewStatement.do?guestId=" + guestId + "&userId=" + userId + "'><i class='ace-icon fa fa-list' title='查看流水单'></i></a>";
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
        gridComplete: function () {

        }
    });
}


function initSelectShopForm() {
    $.ajax({
        url: basePath + "/sys/shop/search.do?filter_EQI_type=4",
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