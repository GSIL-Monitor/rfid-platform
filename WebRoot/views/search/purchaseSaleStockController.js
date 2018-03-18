var searchUrl = basePath + "/search/purchaseSaleStock/page.do";
$(function () {
    initGrid();
    initForm();
    /* initProgressDialog();
     initNotification();*/


});

function initForm() {
    initMultiSelect();
}

function initMultiSelect(){
    /*$("#search_warehId").kendoMultiSelect({
        dataTextField: "name",
        dataValueField: "code",
        height: 400,
        suggest: true,
        dataSource: {
            transport: {
                read:  basePath + "/sys/warehouse/list.do?filter_INI_type=4,9"
            }
        }
    })*/
    $.ajax({
        url: basePath + "/sys/warehouse/list.do?filter_INI_type=9",
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_warehId").empty();
            $("#search_warehId").append("<option value='' >--请选择仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_warehId").append("<option value='" + json[i].id + "'>" + json[i].name + "</option>");
                // $("#search_busnissId").trigger('chosen:updated');
            }

        }
    });
}

function initGrid() {
    $("#grid").jqGrid({
        height: "auto",
        url:searchUrl,
        datatype: "json",
        mtype: 'POST',
        colModel: [
            {
                name: 'imgUrl', label: '图片', width: 50, sortable: false,
                formatter: function (cellValue, options, rowObject) {
                    if (rowObject.imgUrl ==  null) {
                        return "无图片";
                    } else {
                        return "<img width=80 height=100 src='" +basePath + rowObject.imgUrl + "' alt='" + rowObject.styleId + "'/>";
                    }
                }
            },
            {name: 'warehId', label: '仓库编号', sortable: true, width: 45},
            {name: 'warehName', label: '仓库名', sortable: true, width: 35},
            {name: 'sku', label: 'SKU',width: 30},
            {name: 'styleId', label: '款号', width: 30},
            {name: 'styleName', label: '款名', width: 30},
            {name: 'colorId', label: '颜色', width: 30},
            {name: 'sizeId', label: '尺码', width: 30},
            {name: 'price', label: '价格', width: 30,
                formatter: function (cellValue, options, rowObject) {
                    var price=rowObject.price.toFixed(2);
                    return price;
                }
            },
            {name: 'inQty', label: '入库数量', width: 30},
            {name: 'notInQty', label: '在途数量', width: 30},
            {name: 'stockQty', label: '库存数量', width: 30},
            {name: 'saleQty', label: '总销售量', width: 30},
            {name: 'saleLSQty', label: '零售数量', width: 30},
            {name: 'saleJMnotInQty', label: '销售加盟商(在途)', width: 30},
            {name: 'transferOutQty', label: '调拨出库数量', width: 30},
            {name: 'backQty', label: '返厂数量', width: 30},
            {name: 'backQty', label: '返厂数量', width: 30},
            {name: 'otherOutQty', label: '调整数量', width: 50}
        ],
        viewrecords: true,
        autowidth: true,
        rownumbers: true,
        altRows: true,
        rowNum: 20,
        rowList: [20, 50, 100],
        pager: "#grid-pager",
        multiselect: false,
        shrinkToFit: true,
        sortname: 'warehId',
        sortorder: 'desc',
        autoScroll: false,
        footerrow: true,
        gridComplete: function () {
            //setFooterData();
        },
        onSelectRow: function (rowid, status) {
        }
    });
}
function showAdvSearchPanel() {

    $("#searchPanel").slideToggle("fast");
}

function refresh() {
    location.reload(true);
}
function search () {

    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $("#grid").jqGrid('setGridParam', {
        page: 1,
        url: searchUrl,
        postData: params
    });
    $("#grid").trigger("reloadGrid");
}

function exportExcel() {

    var exportUrl=basePath + "/search/purchaseSaleStock/exportExcel.do";
    $("#form1").attr("action",exportUrl);
    $("#warehId").val($("#search_warehId").val());
    $("#sku").val($("#search_sku").val());
    $("#form1").submit();
}



