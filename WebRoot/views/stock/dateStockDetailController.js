/**
 * Created by yushen on 2017/7/12.
 */
var searchUrl = basePath + "/search/DateStockDetailController/page.do";
$(function () {
    initSelectOrigForm();
    //加载当前时间
    nowDay();
    initGrid();

});
function nowDay() {
    var myDate = new Date();
    var year = myDate.getFullYear();
    var month = myDate.getMonth() + 1;
    var day = myDate.getDate();
    if (month < 10) {
        if (day < 10) {
            $("#search_createTime").val(year + "-0" + month + "-0" + day);

        } else {
            $("#search_createTime").val(year + "-0" + month + "-" + day);

        }
    } else {
        if(day < 10){
            $("#search_createTime").val(year + "-" + month + "-0" + day);

        }else{
            $("#search_createTime").val(year + "-" + month + "-" + day);

        }

    }
}


//发货仓库
function initSelectOrigForm() {
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + curOwnerId,
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            debugger;
            $("#search_warehId").empty();
            $("#search_warehId").append("<option value='' style='background-color: #eeeeee'>--请选择仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_warehId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                $("#search_warehId").trigger('chosen:updated');
            }
            $("#search_warehId").val(defaultWarehId);
        }
    });
}
function initGrid() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    var  time=params.filter_GED_billDate;
    if(time!=""){

        params.filter_GED_billDate=time+" 00:00:00";
        params.filter_LED_billDate=time+" 23:59:59";
    }


    $("#grid").jqGrid({
        height: "auto",
        mtype: "POST",
        datatype: "json",
        url: searchUrl,
        postData: params,
        colModel: [
            {name: 'billDate', label: '时间', sortable: false, width: 200},
            {name: 'warehName', label: '仓库', sortable: false, width: 200},
            {name: 'sku', label: 'SKU', sortable: false, width: 200},
            {name: 'styleId', label: '款号', sortable: false, width: 150},
            {name: 'styleName', label: '款名', sortable: false, width: 150},
            {name: 'colorId', label: '色号', sortable: false, hidden: true},
            {name: 'sizeId', label: '尺号', sortable: false, width: 150},
            {name: 'qty', label: '库存数量', sortable: false, width: 150},
            {name: 'price', label: '吊牌价', sortable: false, width: 150,
                formatter: function (cellValue, options, rowObject) {
                    var price=rowObject.price.toFixed(2);
                    return price;
                }
            },
            {name: 'inStockPrice', label: '库存金额', sortable: false, width: 150,
                formatter: function (cellValue, options, rowObject) {
                    var inStockPrice=rowObject.inStockPrice.toFixed(2);
                    return inStockPrice;
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
        // sortname: 'billDate',
        // sortorder: 'asc',
        autoScroll: false


    });
}

function refresh() {
    location.reload(true);
}


function _search() {

    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    var  time=params.filter_GED_billDate;
    if(time!=""){

        params.filter_GED_billDate=time+" 00:00:00";
        params.filter_LED_billDate=time+" 23:59:59";
    }
    $("#grid").jqGrid('setGridParam', {
        page: 1,
        url: searchUrl,
        postData: params
    });
    $("#grid").trigger("reloadGrid");
}


function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}

function showDetailPage() {
    location.href = basePath + "/sys/guestAccount/showDetailPage.do?userId=" + userId;
}


function checkout() {
    debugger;
    exportUrl = basePath + "/search/DateStockDetailController/export.do";
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#form1").attr("action",exportUrl);
       $("#filter_LIKES_warehId").val($("#search_warehId").val());
       $("#filter_LIKES_sku").val($("#search_sku").val());
       $("#filter_EQS_styleId").val($("#search_styleId").val());
       $("#filter_LIKES_colorId").val($("#search_colorId").val());
       $("#filter_LIKES_sizeId").val($("#search_sizeId").val());
       if($("#search_createTime").val()!=""){
           $("#filter_GED_billDate").val($("#search_createTime").val()+" 00:00:00");
           $("#filter_LED_billDate").val($("#search_createTime").val()+" 23:59:59");
       }



    $("#form1").submit();
}
