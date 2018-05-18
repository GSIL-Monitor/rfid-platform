var start, end;
$(function () {

    initKendoUIGrid();
    $(".k-dropdown").css("width", "6em");
    $(".k-grid-toolbar").css("display", "none");//隐藏toolbar


    start = $("#startDate").kendoDatePicker({
        format: "yyyy-MM-dd",
        culture: "zh-CN",
        change: "startChange"
    }).data("kendoDatePicker");

    end = $("#endDate").kendoDatePicker({
        format: "yyyy-MM-dd",
        culture: "zh-CN",
        change: "endChange"
    }).data("kendoDatePicker");
    initMultiSelect();
    $(".k-dropdown").css("width", "6em");
    $(".k-grid-toolbar").css("display", "none");
    $(".k-datepicker input").prop("readonly", true);
});

function refresh() {
    resetData();
}
function resetData() {
    var gridData = $("#stockGrid").data("kendoGrid");
    gridData.dataSource.filter({});
}
var exportExcelid = "";
function exportExcel() {
    //$(".k-grid-excel").click();
    $("#" + exportExcelid).children().find(".k-grid-excel").click();
}

function exportExcelPOI() {
    var filters = serializeToFilter($("#searchForm"));
    var gridData = $("#" + exportExcelid).data("kendoGrid");
    console.log(gridData);
    var request = {};
    request.page = gridData.dataSource._page;
    request.pageSize = gridData.dataSource._pageSize;
    request.take = gridData.dataSource._pageSize;
    request.sort =gridData.dataSource._sort;
    request.skip = gridData.dataSource._skip;
    request.filter = {
        logic: "and",
        filters: filters
    };

    var exportUrl;
    if (exportExcelid === "stockGrid") { //sku明细
        exportUrl = basePath + "/search/detailStockViewSearch/export.do"
    } else if (exportExcelid === "stockCodeeGrid") { //code明细
        exportUrl = basePath + "/stock/warehStock/export.do"
    }else if(exportExcelid === "stockstyleGrid"){
        $.each(filters,function (index,value) {
            if(value.field === "sku"){
                value.field = "styleId";
            }
        });
        exportUrl = basePath + "/stock/warehStock/exportStyle.do"
    }

    /*$.ajax({
        url: exportUrl,
        type: 'POST',
        data: {
            request: JSON.stringify(request)
        },
        success: function (data) {
            $.gritter.add({
                text: data.msg,
                class_name: 'gritter-success  gritter-light'
            });
        }

    })*/
    //window.location.href=exportUrl+"request="+JSON.stringify(request);



   /* document.write("<form action="+exportUrl+" method=post name=form1 style='display:none'>");
     document.write("<input type=hidden  name='request' value='"+JSON.stringify(request)+"'>");
     document.write("</form>");
     document.form1.submit();*/
    //window.location.href= basePath + "/stock/warehStock/index.do"
    $("#form1").attr("action",exportUrl);
    $("#request").val(JSON.stringify(request));
    $("#form1").submit();
}
function htmlspecialchars (string) {
    string = string.replace(/\"/g,"'");//这个就是双引号

    return string;
}

function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}
function search() {
    var gridData = $("#" + exportExcelid).data("kendoGrid");
    var filters = serializeToFilter($("#searchForm"));
    var isok=true;
    if(JMSCODE!=""&&JMSCODE!=undefined&&JMSNAME!=""&&JMSNAME!=undefined){
        isok=false;
        for(var i=0;i<filters.length;i++){
            if(filters[i].field=="warehId"){
                isok=true;
            }

        }

    }
    if(isok==false){
        $.gritter.add({
            text: "请选择仓库",
            class_name: 'gritter-success  gritter-light'
        });
        return false
    }

    filters.push({
        field: "warehType",
        operator: "eq",
        value: 9
    });
    if(exportExcelid === "stockstyleGrid"){
        //按款汇总将 SKU属性改为货号
        $.each(filters,function (index,value) {
            if(value.field === "sku"){
                value.field = "styleId";
            }
        })
    }
    console.log(filters);
    gridData.dataSource.filter({
        logic: "and",
        filters: filters
    });

}
function initMultiSelect() {
    $("#filter_in_origid").kendoMultiSelect({
        dataTextField: "name",
        dataValueField: "code",
        height: 400,
        suggest: true,
        dataSource: {
            transport: {
                read: basePath + "/sys/warehouse/list.do?filter_EQI_type=0"
            }
        }
    });
    console.log(JMSCODE);
    console.log(JMSNAME);
    if(JMSCODE!=""&&JMSCODE!=undefined&&JMSNAME!=""&&JMSNAME!=undefined){
        $("#filter_in_warehId").kendoMultiSelect({
            dataTextField: "name",
            dataValueField: "code",
            height: 400,
            suggest: true,
            dataSource: {
                transport: {
                    read: basePath + "/sys/warehouse/list.do?filter_INI_type=9"
                }
            },
            value:JMSCODE,
            text:JMSNAME
        });
    }else{
        $("#filter_in_warehId").kendoMultiSelect({
            dataTextField: "name",
            dataValueField: "code",
            height: 400,
            suggest: true,
            dataSource: {
                transport: {
                    read: basePath + "/sys/warehouse/list.do?filter_INI_type=9"
                }
            },
        });
    }



    $("#filter_in_destUnitId").kendoMultiSelect({
        dataTextField: "name",
        dataValueField: "code",
        height: 400,
        suggest: true,
        dataSource: {
            transport: {
                read: basePath + "/sys/warehouse/list.do?filter_INI_type=0,1,2"
            }
        }
    })
}
var isFristSkuGrid = true;
function initKendoUIGrid() {
    exportExcelid = "stockGrid";
    if (isFristSkuGrid) {
        $("#stockGrid").kendoGrid({
            toolbar: ["excel"],
            excel: {
                fileName: "仓库库存查询.xlsx",
                proxyURL: basePath + "/search/detailStockViewSearch/export.do",
                allPages: true,
                filterable: true
            },
            dataSource: {
                schema: {
                    total: "total",
                    model: {

                        fields: {

                            warehName: {type: "string"},
                            sku: {type: "string"},
                            styleId: {type: "string"},
                            styleName: {type: "string"},
                            colorId: {type: "string"},
                            sizeId: {type: "string"},
                            qty: {type: "number"},
                            price: {type: "number"}
                        }
                    },
                    data: "data",
                    groups: "data"
                },
                filter: {
                    logic: "and",
                    filters: [{
                        field: "warehType",
                        operator: "eq",
                        value: 9
                    }, {
                        field: "ownerId",
                        operator: "eq",
                        value: ownerId
                    }]
                },
                transport: {
                    read: {
                        url: basePath + "/stock/warehStock/page.do",
                        type: "POST",
                        dataType: "json",
                        contentType: 'application/json'
                    },
                    parameterMap: function (options) {
                        return JSON.stringify(options);
                    }
                },
                sort: [{field: "styleId", dir: "desc"}],
                pageSize: 500.0,
                serverSorting: true,
                serverPaging: true,
                serverGrouping: false,
                serverFiltering: true,
                aggregate: [

                    {field: "qty", aggregate: "sum"},
                    {field: "styleId", aggregate: "count"},
                    {field: "price", aggregate: "average"}

                ]


            },

            sortable: {
                mode: "multiple",
                allowUnsort: true
            },
            rowNumber: true,
            pageable: {
                input: true,
                buttonCount: 5,
                pageSize: 500.0,
                pageSizes: [100, 500, 1000, 2000]
            },
            height: 680,
            groupable: true,
            group: onGrouping,
            columnMenu: true,
            filterable: {
                extra: false
            },
            selectable: "multiple row",
            reorderable: true,
            resizable: true,
            scrollable: true,
            columns: [
                {
                    field: "", title: "图片", width: 100,
                    template: function (data) {
                        debugger;
                        var url = data.url;
                        if (url == null) {
                            return "无图片";
                        } else {
                            return "<img width=80 height=100  onclick=showImagesUrl('" +basePath + data.url + "') src='" +basePath + data.url + "' alt='" + data.styleid + "'/>";
                        }
                    }

                },

                {
                    field: "warehName", title: "仓库", width: 150,
                    template: function (data) {
                        if (data.warehName) {
                            return "[" + data.warehId + "]" + data.warehName;
                        } else {
                            return "[" + data.warehId + "]";
                        }
                    },
                    groupHeaderTemplate: function (data) {
                        var totQty = data.aggregates.qty.sum;
                        var value = data.value;
                        var avgPrice = data.aggregates.price.average;
                        return "仓库:" + value + " 总数量:" + totQty + "; 平均价 :" + kendo.toString(avgPrice, '0.00');
                    }
                },
                {field: "sku", title: "SKU", width: 150},
                {
                    field: "styleId", title: "款号", width: 80,
                    aggregates: ["count"],
                    groupHeaderTemplate: function (data) {

                        var totQty = data.aggregates.qty.sum;
                        var value = data.value;
                        var avgPrice = data.aggregates.price.average;
                        return "款号:" + value + " 总数量:" + totQty + "; 平均价 :" + kendo.toString(avgPrice, '0.00');
                    }
                },
                {field: "styleName", title: "款名", width: 100},
                {field: "colorId", title: "色号", width: 80},
                {field: "sizeId", title: "尺号", width: 80},
                {field: "class1Name", title: "厂家", width: 80},

                {
                    field: "qty", title: "库存数量", width: 80,
                    groupable: false,
                    aggregates: ["sum"],
                    footerTemplate: "#=sum#"
                },


                {field: "price", title: "吊牌价", width: 80, groupable: false, aggregates: ["average"],
                    template: function (data) {
                        var price=data.price.toFixed(2);
                        return price;
                    }
                },

                {field: "inStockPrice", title: "库存金额", width: 80,
                    template: function (data) {
                        var inStockPrice=data.inStockPrice.toFixed(2);
                        return inStockPrice;
                    }
                },
                {
                    command: [{name: "唯一码明细", click: showDetails}], title: " ", groupable: false, width: 80,
                    attributes: {
                        // "class": "table-cell",
                        style: "text-align: center; font-size: 8px"
                    }
                }
            ]
        });
        isFristSkuGrid = false;
        $(".k-grid-toolbar").css("display", "none");//隐藏toolbar
    } else {
        search();
    }


}
var isFristCodeGrid = true;
function initCodeKendoUIGrid() {
    exportExcelid = "stockCodeeGrid";
    if (isFristCodeGrid) {
        $("#stockCodeeGrid").kendoGrid({
            toolbar: ["excel"],
            excel: {
                fileName: "仓库库存Code查询.xlsx",
                proxyURL: basePath + "/search/detailStockViewSearch/export.do",
                allPages: true,
                filterable: true
            },
            dataSource: {
                schema: {
                    total: "total",
                    model: {

                        fields: {

                            warehName: {type: "string"},
                            sku: {type: "string"},
                            styleId: {type: "string"},
                            styleName: {type: "string"},
                            colorId: {type: "string"},
                            sizeId: {type: "string"},
                            qty: {type: "number"},
                            price: {type: "number"}
                        }
                    },
                    data: "data",
                    groups: "data"
                },
                filter: {
                    logic: "and",
                    filters: [{
                        field: "warehType",
                        operator: "eq",
                        value: 9
                    }, {
                        field: "ownerId",
                        operator: "eq",
                        value: ownerId
                    }]
                },
                transport: {
                    read: {
                        url: basePath + "/stock/warehStock/pageCode.do",
                        type: "POST",
                        dataType: "json",
                        contentType: 'application/json'
                    },
                    parameterMap: function (options) {
                        return JSON.stringify(options);
                    }
                },
                sort: [{field: "styleId", dir: "desc"}],
                pageSize: 500.0,
                serverSorting: true,
                serverPaging: true,
                serverGrouping: false,
                serverFiltering: true,
                aggregate: [

                    {field: "qty", aggregate: "sum"},
                    {field: "styleId", aggregate: "count"},
                    {field: "price", aggregate: "average"}

                ]


            },

            sortable: {
                mode: "multiple",
                allowUnsort: true
            },
            rowNumber: true,
            pageable: {
                input: true,
                buttonCount: 5,
                pageSize: 500.0,
                pageSizes: [100, 500, 1000, 2000]
            },
            height: 680,
            groupable: true,
            group: onGrouping,
            columnMenu: true,
            filterable: {
                extra: false
            },
            selectable: "multiple row",
            reorderable: true,
            resizable: true,
            scrollable: true,
            columns: [
                {
                    field: "", title: "图片", width: 100,
                    template: function (data) {
                        debugger;
                        var url = data.url;
                        if (url == null) {
                            return "无图片";
                        } else {
                            return "<img width=80 height=100  onclick=showImagesUrl('" +basePath + data.url + "') src='" +basePath + data.url + "' alt='" + data.styleid + "'/>";
                        }
                    }

                },

                {
                    field: "warehName", title: "仓库", width: 100,
                    template: function (data) {
                        if (data.warehName) {
                            return "[" + data.warehId + "]" + data.warehName;
                        } else {
                            return "[" + data.warehId + "]";
                        }
                    },
                    groupHeaderTemplate: function (data) {
                        var totQty = data.aggregates.qty.sum;
                        var value = data.value;
                        var avgPrice = data.aggregates.price.average;
                        return "仓库:" + value + " 总数量:" + totQty + "; 平均价 :" + kendo.toString(avgPrice, '0.00');
                    }
                },
                {field: "sku", title: "SKU", width: 100},
                {
                    field: "styleId", title: "款号", width: 80,
                    aggregates: ["count"],
                    groupHeaderTemplate: function (data) {

                        var totQty = data.aggregates.qty.sum;
                        var value = data.value;
                        var avgPrice = data.aggregates.price.average;
                        return "款号:" + value + " 总数量:" + totQty + "; 平均价 :" + kendo.toString(avgPrice, '0.00');
                    }
                },
                {field: "styleName", title: "款名", width: 80},
                {field: "colorId", title: "色号", width: 60},
                {field: "sizeId", title: "尺号", width: 60},
                {field: "class1Name", title: "厂家", width: 80},
                {field: "code", title: "Code", width: 100},

                {
                    field: "qty", title: "库存数量", width: 80,
                    groupable: false,
                    aggregates: ["sum"],
                    footerTemplate: "#=sum#"
                },


                {field: "price", title: "吊牌价", width: 80, groupable: false, aggregates: ["average"],
                    template: function (data) {
                        var price=data.price.toFixed(2);
                        return price;
                    }
                },
                {
                    field: "firstInStockTime", title: "初次入库时间", width: 100,
                    format: "{0: yyyy-MM-dd}",
                },
                {field: "inStockDays", title: "入库时长(天)", width: 100}

            ]
        });
        isFristCodeGrid = false;
        $(".k-grid-toolbar").css("display", "none");//隐藏toolbar
    } else {
        search();
    }


}
var isFriststyleGrid = true;
function initstyleKendoUIGrid() {
    exportExcelid = "stockstyleGrid";
    if (isFriststyleGrid) {
        $("#stockstyleGrid").kendoGrid({
            toolbar: ["excel"],
            excel: {
                fileName: "仓库库按款查询.xlsx",
                proxyURL: basePath + "/search/detailStockViewSearch/export.do",
                allPages: true,
                filterable: true
            },
            dataSource: {
                schema: {
                    total: "total",
                    model: {

                        fields: {

                            warehName: {type: "string"},
                            sku: {type: "string"},
                            styleId: {type: "string"},
                            styleName: {type: "string"},
                            colorId: {type: "string"},
                            sizeId: {type: "string"},
                            qty: {type: "number"},
                            price: {type: "number"}
                        }
                    },
                    data: "data",
                    groups: "data"
                },
                filter: {
                    logic: "and",
                    filters: [{
                        field: "warehType",
                        operator: "eq",
                        value: 9
                    }, {
                        field: "ownerId",
                        operator: "eq",
                        value: ownerId
                    }]
                },
                transport: {
                    read: {
                        url: basePath + "/stock/warehStock/pageStyle.do",
                        type: "POST",
                        dataType: "json",
                        contentType: 'application/json'
                    },
                    parameterMap: function (options) {
                        return JSON.stringify(options);
                    }
                },
                sort: [{field: "styleId", dir: "desc"}],
                pageSize: 500.0,
                serverSorting: true,
                serverPaging: true,
                serverGrouping: false,
                serverFiltering: true,
                aggregate: [

                    {field: "qty", aggregate: "sum"},
                    {field: "styleId", aggregate: "count"},
                    {field: "price", aggregate: "average"}

                ]


            },
            sortable: {
                mode: "multiple",
                allowUnsort: true
            },
            rowNumber: true,
            pageable: {
                input: true,
                buttonCount: 5,
                pageSize: 500.0,
                pageSizes: [100, 500, 1000, 2000]
            },
            height: 680,
            groupable: true,
            group: onGrouping,
            columnMenu: true,
            filterable: {
                extra: false
            },
            selectable: "multiple row",
            reorderable: true,
            resizable: true,
            scrollable: true,
            columns: [
                {
                    field: "", title: "图片", width: 100,
                    template: function (data) {
                        debugger;
                        var url = data.url;
                        if (url == null) {
                            return "无图片";
                        } else {
                            return "<img width=80 height=100  onclick=showImagesUrl('" +basePath + data.url + "') src='" +basePath + data.url + "' alt='" + data.styleid + "'/>";
                        }
                    }

                },
                {
                    field: "styleId", title: "款号", width: 80,
                    aggregates: ["count"],
                    groupHeaderTemplate: function (data) {

                        var totQty = data.aggregates.qty.sum;
                        var value = data.value;
                        var avgPrice = data.aggregates.price.average;
                        return "款号:" + value + " 总数量:" + totQty + "; 平均价 :" + kendo.toString(avgPrice, '0.00');
                    }
                },
                {
                    field: "warehName", title: "仓库", width: 100,
                    template: function (data) {
                        if (data.warehName) {
                            return "[" + data.warehId + "]" + data.warehName;
                        } else {
                            return "[" + data.warehId + "]";
                        }
                    },
                    groupHeaderTemplate: function (data) {
                        var totQty = data.aggregates.qty.sum;
                        var value = data.value;
                        var avgPrice = data.aggregates.price.average;
                        return "仓库:" + value + " 总数量:" + totQty + "; 平均价 :" + kendo.toString(avgPrice, '0.00');
                    }
                },

                {field: "styleName", title: "款名", width: 80},
                {field: "class1Name", title: "厂家", width: 80},
                {
                    field: "qty", title: "库存数量", width: 80,
                    groupable: false,
                    aggregates: ["sum"],
                    footerTemplate: "#=sum#"
                },


                {field: "price", title: "吊牌价", width: 80, groupable: false, aggregates: ["average"],
                    template: function (data) {
                        var price=data.price.toFixed(2);
                        return price;
                    }
                }


            ]
        });
        isFriststyleGrid = false;
        $(".k-grid-toolbar").css("display", "none");//隐藏toolbar
    } else {
        search();
    }


}

function hideImage() {
    $("#divshowImage").hide();

}
function showImagesUrl(url) {
    debugger;
    console.log(url);
    var Url="";
    var urlArray=url.split("_");
    var urlArrays=urlArray[1].split(".");
    Url=urlArray[0]+"."+urlArrays[1];
    $("#showImage").attr("src",Url);
    $("#divshowImage").show();

}

function onGrouping(arg) {
    /*
     kendoConsole.log("Group on " + kendo.stringify(arg.groups));
     */
}

function selectClass1() {
    var rowId = $("#class1Select_Grid").jqGrid("getGridParam", "selrow");
    var rowData = $("#class1Select_Grid").jqGrid('getRowData', rowId);
    $("#filter_eq_class1").val(rowData.code);
    $("#filter_eq_class1Name").val(rowData.name);
    $("#modal_class1_search_tables").modal('hide');
}

function showDetails(e) {

    var tr = $(e.target).closest("tr");
    var data = this.dataItem(tr);
    console.log(data.sku, data.warehId);

    $("#inStockCodes_window").modal('show');
    initInstockCodeList(data.sku, data.warehId);
    inStockCodeListReload(data.sku, data.warehId);
}





