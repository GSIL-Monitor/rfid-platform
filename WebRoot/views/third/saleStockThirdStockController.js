$(function () {
    initMutiSelect();
    initKendoUIGrid();
    $(".k-dropdown").css("width", "6em");
    $(".k-grid-toolbar").css("display", "none");//隐藏toolbar
    $(".k-datepicker input").prop("readonly", true);

});

function initMutiSelect() {
    $("#filter_IN_warehId").kendoMultiSelect({
        dataTextField: "name",
        dataValueField: "code",
        height: 400,
        suggest: true,
        dataSource: {
            transport: {
                read: basePath + "/sys/warehouse/list.do?filter_INI_type=4,9"
            }
        }
    })
    $("#filter_CONTAINS_class1").kendoMultiSelect({
        dataTextField: "name",
        dataValueField: "code",
        height: 400,
        suggest: true,
        dataSource: {
            transport: {
                read: basePath + "/sys/property/searchByType.do?type=C1"
            }
        }
    })
    $("#filter_CONTAINS_class2").kendoMultiSelect({
        dataTextField: "name",
        dataValueField: "code",
        height: 400,
        suggest: true,
        dataSource: {
            transport: {
                read: basePath + "/sys/property/searchByType.do?type=C2"
            }
        }
    })
    $("#filter_CONTAINS_class3").kendoMultiSelect({
        dataTextField: "name",
        dataValueField: "code",
        height: 400,
        suggest: true,
        dataSource: {
            transport: {
                read: basePath + "/sys/property/searchByType.do?type=C3"
            }
        }
    })
    $("#filter_CONTAINS_class4").kendoMultiSelect({
        dataTextField: "name",
        dataValueField: "code",
        height: 400,
        suggest: true,
        dataSource: {
            transport: {
                read: basePath + "/sys/property/searchByType.do?type=C4"
            }
        }
    })
    $("#filter_CONTAINS_class10").kendoMultiSelect({
        dataTextField: "name",
        dataValueField: "code",
        height: 400,
        suggest: true,
        dataSource: {
            transport: {
                read: basePath + "/sys/property/searchByType.do?type=C10"
            }
        }
    })

}
function refresh() {
    resetData();
}
function resetData() {
    var gridData = $("#saleStock").data("kendoGrid");
    gridData.dataSource.filter({});
}
function exportExcel() {
    $(".k-grid-excel").click();
}
function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}
function search() {
    var gridData = $("#saleStock").data("kendoGrid");
    var filters = serializeToFilter($("#searchForm"));
    console.log(filters);
    gridData.dataSource.filter({
        logic: "and",
        filters: filters
    });

}

function initKendoUIGrid() {

    $("#saleStock").kendoGrid({
        toolbar: ["excel"],
        excel: {
            allPages: true
        },
        dataSource: {
            schema: {
                total: "total",
                model: {
                    fields: {
                        stockDay: {type: "string"},
                        sku: {type: "string"},
                        stockCode: {type: "string"},
                        styleId: {type: "string"},
                        colorId: {type: "string"},
                        sizeId: {type: "string"},
                        inDate: {type: "date"},
                        styleName: {type: "string"},
                        colorName:{type: "string"},
                        sizeName:{type:"string"},
                        stockName: {type: "string"},
                        fittingQty: {type: "number"},
                        stockQty: {type: "number"},
                        saleQty: {type: "number"},
                        avgPercent: {type: "number"},
                        actPrice: {type: "number"},
                        class1: {type: "string"},
                        class2: {type: "string"},
                        class3: {type: "string"},
                        class4: {type: "string"},
                        class10: {type: "string"},
                        lastestSaleDate: {type: "string"},
                        lastestFittingDate: {type: "string"},
                        stockPrice: {type: "string"},
                        inDays: {type: "number"}

                    }
                },
                data: "data",
                groups: "data"
            },

            transport: {
                read: {
                    url: basePath + "/third/saleStockThirdStock/list.do",
                    type: "POST",
                    dataType: "json",
                    contentType: 'application/json'
                },
                parameterMap: function (options) {
                    return JSON.stringify(options);
                }
            },
            //   sort:[{field: "stockDay", dir: "desc"},{field: "stockCode", dir: "desc"},{field: "sku", dir: "desc"}],
            pageSize: 100.0,
            serverSorting: true,
            serverPaging: true,
            serverGrouping: false,
            serverFiltering: true,
            aggregate: [

                {field: "fittingQty", aggregate: "sum"},
                {field: "saleQty", aggregate: "sum"},
                {field: "actPrice", aggregate: "sum"},


            ]
        },
        sortable: {
            mode: "multiple",
            allowUnsort: true
        },
        pageable: {
            input: true,
            buttonCount: 5,
            pageSize: 100.0,
            pageSizes: [100, 500, 1000]
        },

        groupable: true,
        group: onGrouping,
        columnMenu: true,
        filterable: {
            extra: false
        },
        selectable: "multiple row",
        reorderable: true,
        resizable: false,
        scrollable: true,

        columns: [
            {
                field: "inDate", title: "入库日期",
                width: "130px",
                aggregates: ["count"],
                format: "{0:yyyy-MM-dd}"
            },
            {
                title: "日期",
                field: "stockDay",
                width: "130px",
                groupHeaderTemplate: function(data) {
                    var totStock=data.aggregates.stockQty.sum;
                    if(totStock==null){
                        totStock=0;
                    }
                    if(totFit==null){
                        totFit=0;
                    }
                    var totFit=data.aggregates.fittingQty.sum;
                    return "日期:"+data.value+" 库存总数:"+ totStock+" 试衣总数:"+totFit;
                }
            }, {
                field: "stockCode",
                title: "店仓",
                width: "150px",
                template: function (data) {
                    if (data.stockName) {
                        return "[" + data.stockCode + "]" + data.stockName;
                    } else {
                        return "[" + data.stockCode + "]";
                    }
                }
            },
            {field: "sku", title: "SKU", width: "180px"},
            {field: "styleId", title: "款号", width: "120px", aggregates: ["count"]},
            {field: "styleName", title: "款名", width: "120px"},
            {field: "colorId", title: "色号", width: "80px"},
            {field: "sizeId", title: "尺号", width: "80px"},
            {field: "stockQty", title: "库存量", width: "100px", groupable: false, aggregates: ["sum"]},
            {field: "fittingQty", title: "试衣量", width: "100px", groupable: false, aggregates: ["sum"]},
            {field: "saleQty", title: "销售量", width: "100px", groupable: false, aggregates: ["sum"]},
            {field: "avgPercent", title: "平均折扣", width: "80px", groupable: false},
            {field: "actPrice", title: "销售总额", width: "80px", groupable: false, aggregates: ["sum"]},
            {field: "class1", title: "品牌", width: "130px"},
            {field: "class2", title: "年份", width: "80px"},
            {field: "class3", title: "大类", width: "80px"},
            {field: "class4", title: "小类", width: "130px"},
            {field: "class10", title: "季节", width: "80px"},
            {field: "colorId", title: "色号", width: "80px"},
            {field: "lastestSaleDate", title: "最新销售日期", width: "130px",format: "{0:yyyy-MM-dd}"},
            {field: "lastestFittingDate", title: "最新试衣日期", width: "130px",format: "{0:yyyy-MM-dd}"},
            {field: "stockPrice", title: "库存金额", width: "80px"},
            {field: "inDays", title: "进店天数", width: "80px"},

        ]
    });

}

function onGrouping(arg) {

}
   