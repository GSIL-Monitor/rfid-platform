$(function () {

    initForm();
   /* var myDate = new Date();
    var year = myDate.getFullYear();
    var month = myDate.getMonth() + 1;
    var day = myDate.getDate();
    if (month < 10) {
        if (day < 10) {
            $("#filter_gte_billDate").val(year + "-0" + month + "-0" + day);
            $("#filter_lte_billDate").val(year + "-0" + month + "-0" + day);
        } else {
            $("#filter_gte_billDate").val(year + "-0" + month + "-" + day);
            $("#filter_lte_billDate").val(year + "-0" + month + "-" + day);
        }
    } else {
        if(day < 10){
            $("#filter_gte_billDate").val(year + "-" + month + "-0" + day);
            $("#filter_lte_billDate").val(year + "-" + month + "-0" + day);
        }else{
            $("#filter_gte_billDate").val(year + "-" + month + "-" + day);
            $("#filter_lte_billDate").val(year + "-" + month + "-" + day);
        }

    }*/
    initKendoUIGrid();
    inttitledata();
    $(".k-dropdown").css("width", "6em");
    $(".k-grid-toolbar").css("display", "none");//隐藏toolbar

});
function inttitledata() {
    debugger;
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);

    $.ajax({
        url: basePath + "/search/transferorderCountViewSearch/findtitledate.do",
        cache: false,
        async: false,
        data: {"dates": JSON.stringify(params)},
        type: "POST",
        success: function (data, textStatus) {
            var result = data.result;
            console.log(result);
            $("#billNos").text(":" + result.billNos);
            $("#styleIds").text(":" + result.styleids);
            $("#sumnum").text(":" + result.sumnum);
            $("#origids").text(":" + result.origids);
            $("#destids").text(":" + result.destids);

        }
    });
}
function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}
function initForm() {
    initSelectOrigForm();
    initSelectDestForm();
}
function openSearchOrigDialog() {
    dialogOpenPage = "transferOrderOrig";
    $("#modal_guest_search_table").modal('show').on('shown.bs.modal', function () {
        initGuestSelect_Grid();
    });
    $("#searchGuestDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='confirm_selected_OrigUnit()'>确认</button>"
    );
}
function openSearchDestDialog() {
    dialogOpenPage = "transferOrderUnit";
    $("#modal_guest_search_table").modal('show').on('shown.bs.modal', function () {
        initGuestSelect_Grid();
    });
    $("#searchGuestDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='confirm_selected_DestUnit()'>确认</button>"
    );
}
function initSelectOrigForm() {
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + $("#search_origUnitId").val(),
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_origId").empty();
            $("#search_origId").append("<option value='' style='background-color: #eeeeee'>--请选择出库仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_origId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                $("#search_origId").trigger('chosen:updated');
            }
        }
    });
}
function initSelectDestForm() {
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9&filter_EQS_ownerId=" + $("#search_destUnitId").val(),
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#search_destId").empty();
            $("#search_destId").append("<option value='' style='background-color: #eeeeee'>--请选择入库仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#search_destId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                $("#search_destId").trigger('chosen:updated');
            }
        }
    });
}
function chooseExportFunction() {

    exportExcelKendo();
}
function exportExcelKendo() {
    $("#" + exportExcelid).children().find(".k-grid-excel").click();
}
var isoneinitKendoUIGrid = true;
function initKendoUIGrid() {
    exportExcelid = "searchGrid";
    if (isoneinitKendoUIGrid) {
        var filters = serializeToFilter($("#searchForm"));
        console.log(filters);
        $("#searchGrid").kendoGrid({
            toolbar: ["excel"],
            excel: {
                fileName: "调拨明细统计.xlsx",
                proxyURL: basePath + "/search/saleorderCountView/export.do",
                allPages:true,
                filterable: true
            },
            excelExport: function (e) {

            },
            dataSource: {
                schema: {
                    total: "total",
                    model: {

                        fields: {
                            billDate: {type: "date"},
                            billno: {type: "string"},
                            taskId: {type: "string"},
                            token: {type: "number"},
                            deviceId: {type: "string"},
                            origid: {type: "string"},
                            destid: {type: "string"},
                            destUnitId: {type: "string"},
                            busnissname: {type: "string"},
                            origName: {type: "string"},
                            destName: {type: "string"},
                            destUnitName: {type: "string"},
                            styleId: {type: "string"},
                            stylename: {type: "string"},
                            colorId: {type: "string"},
                            sizeId: {type: "string"},
                            qty: {type: "number"},
                            billQty: {type: "number"},
                            diffQty: {type: "number"},
                            price: {type: "number"},
                            saletype: {type: "string"}
                        }
                    },

                    data: "data",
                    groups: "data"
                },
                filter: {
                    logic: "and",
                    filters: filters
                },

                transport: {
                    read: {
                        url: basePath + "/search/transferorderCountViewSearch/list.do",
                        type: "POST",
                        dataType: "json",
                        async: false,
                        contentType: 'application/json'
                    },
                    parameterMap: function (options) {
                        return JSON.stringify(options);
                    }
                },
                sort: [{field: "billDate", dir: "desc"}],
                pageSize: 100.0,
                serverSorting: true,
                serverPaging: true,
                serverGrouping: false,
                serverFiltering: true,
                aggregate: [

                    {field: "qty", aggregate: "sum"},
                    {field: "styleId", aggregate: "count"},


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
                pageSize: 100.0,
                pageSizes: [100, 500, 1000, 2000, 5000]
            },

            groupable: true,
            columnMenu: true,
            filterable: {
                extra: false
            },
            //selectable: "multiple row",
            reorderable: true,
            resizable: true,
            scrollable: true,

            columns: [
                {
                    field: "", title: "图片", width: 100,
                    template: function (data) {

                        var url = data.url;
                        if (url == null) {
                            return "无图片";
                        } else {
                            return "<img width=80 height=100 src='" + data.url + "' alt='" + data.styleid + "'/>";
                        }
                    }

                },
                {
                    title: "日期",
                    field: "billDate",
                    width: "200px",
                    aggregates: ["count"],
                    filterable: {
                        extra: true,
                        ui: function (element) {
                            element.kendoDatePicker({
                                format: "yyyy-MM-dd",
                                culture: "zh-CN"
                            });
                        }

                    },

                    format: "{0:yyyy-MM-dd}",
                    groupHeaderTemplate: function (data) {

                        var totQty = data.aggregates.qty.sum;
                        var value = kendo.toString(data.value, 'yyyy-MM-dd HH:mm:ss');
                        var totactprice = data.aggregates.totactprice.sum;
                        return "日期:" + value + " 总数量:" + totQty + "; 总价 :" + kendo.toString(totactprice, '0.00');
                    }
                },
                {
                    title: "单号",
                    field: "billNo",
                    width: "250px",
                    aggregates: ["count"],
                    groupHeaderTemplate: function (data) {
                        debugger;
                        var totQty = data.aggregates.qty.sum;
                        var value = data.value;
                        var totactprice = data.aggregates.totactprice.sum;
                        return "单号:" + value + " 总数量:" + totQty + "; 总价 :" + kendo.toString(totactprice, '0.00');
                    }
                },
                {
                    field: "status",
                    title: "入库状态",
                    width: "150px",
                    template:function(data) {
                        var value = "";
                        switch (data.status) {

                            case 0 :
                                value = "订单状态";
                                break;
                            case 1:
                                value = "已入库";
                                break;
                            case 2 :
                                value = "入库中";
                                break;

                            default:
                                break;
                        }
                        return value;
                    }
                },
                {
                    field: "outStatus",
                    title: "出库状态",
                    width: "150px",
                    template:function(data) {
                        var value = "";
                        switch (data.status) {

                            case 0 :
                                value = "订单状态";
                                break;
                            case 1:
                                value = "已出库";
                                break;
                            case 2 :
                                value = "出库中";
                                break;

                            default:
                                break;
                        }
                        return value;
                    }
                },
                {
                    field: "sku",
                    title: "SKU",
                    width: "150px"
                },
                {
                    field: "destUnitName",
                    title: "收货方",
                    width: "180px",

                },
                {
                    field: "origUnitName",
                    title: "发货方",
                    width: "180px",

                },
                {
                    field: "destName",
                    title: "入库仓库",
                    width: "180px",

                },
                {
                    field: "origName",
                    title: "出库仓库",
                    width: "180px",

                },
                {
                    field: "precast",
                    title: "成本价",
                    width: "180px",

                },
                {
                    field: "styleId", title: "款号", width: "140px",
                    aggregates: ["count"],
                    groupHeaderTemplate: function (data) {

                        var totQty = data.aggregates.qty.sum;

                        return "款号:" +data.value + " 总数量:" + totQty;
                    }
                },
                {field: "styleName", title: "款名", width: "80px"},
                {field: "sizeId", title: "尺号", width: "80px"},
                {
                    field: "qty", title: "数量", width: "80px", groupable: false,
                    aggregates: ["sum"],
                    footerTemplate: "#=sum#"
                }




            ]

        });
        isoneinitKendoUIGrid = false;
    } else {
        search();
    }
    inttitledata();
}
var isoneTranKendoUIGrid = true;
function initTranKendoUIGrid() {
    exportExcelid = "searchTranGrid";
    if (isoneTranKendoUIGrid) {
        var filters = serializeToFilter($("#searchForm"));
        console.log(filters);
        $("#searchTranGrid").kendoGrid({
            toolbar: ["excel"],
            excel: {
                fileName: "调拨单统计.xlsx",
                proxyURL: basePath + "/search/saleorderCountView/export.do",
                allPages:true,
                filterable: true
            },
            excelExport: function (e) {

            },
            dataSource: {
                schema: {
                    total: "total",
                    model: {

                        fields: {
                            billDate: {type: "date"},
                            billno: {type: "string"},
                            taskId: {type: "string"},
                            token: {type: "number"},
                            deviceId: {type: "string"},
                            origid: {type: "string"},
                            destid: {type: "string"},
                            destUnitId: {type: "string"},
                            busnissname: {type: "string"},
                            origName: {type: "string"},
                            destName: {type: "string"},
                            destUnitName: {type: "string"},
                            styleId: {type: "string"},
                            stylename: {type: "string"},
                            colorId: {type: "string"},
                            sizeId: {type: "string"},
                            qty: {type: "number"},
                            billQty: {type: "number"},
                            diffQty: {type: "number"},
                            price: {type: "number"},
                            saletype: {type: "string"}
                        }
                    },

                    data: "data",
                    groups: "data"
                },
                filter: {
                    logic: "and",
                    filters: filters
                },

                transport: {
                    read: {
                        url: basePath + "/search/transferorderCountViewSearch/listreadTran.do",
                        type: "POST",
                        dataType: "json",
                        async: false,
                        contentType: 'application/json'
                    },
                    parameterMap: function (options) {
                        return JSON.stringify(options);
                    }
                },
                sort: [{field: "billDate", dir: "desc"}],
                pageSize: 100.0,
                serverSorting: true,
                serverPaging: true,
                serverGrouping: false,
                serverFiltering: true,
                aggregate: [




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
                pageSize: 100.0,
                pageSizes: [100, 500, 1000, 2000, 5000]
            },

            groupable: true,
            columnMenu: true,
            filterable: {
                extra: false
            },
            //selectable: "multiple row",
            reorderable: true,
            resizable: true,
            scrollable: true,

            columns: [
                {
                    title: "日期",
                    field: "billDate",
                    width: "200px",
                    aggregates: ["count"],
                    filterable: {
                        extra: true,
                        ui: function (element) {
                            element.kendoDatePicker({
                                format: "yyyy-MM-dd",
                                culture: "zh-CN"
                            });
                        }

                    },

                    format: "{0:yyyy-MM-dd}",
                    groupHeaderTemplate: function (data) {

                        var totQty = data.aggregates.qty.sum;
                        var value = kendo.toString(data.value, 'yyyy-MM-dd HH:mm:ss');
                        var totactprice = data.aggregates.totactprice.sum;
                        return "日期:" + value + " 总数量:" + totQty + "; 总价 :" + kendo.toString(totactprice, '0.00');
                    }
                },
                {
                    title: "单号",
                    field: "billNo",
                    width: "250px",
                    aggregates: ["count"],
                    groupHeaderTemplate: function (data) {
                        debugger;
                        var totQty = data.aggregates.qty.sum;
                        var value = data.value;
                        var totactprice = data.aggregates.totactprice.sum;
                        return "单号:" + value + " 总数量:" + totQty + "; 总价 :" + kendo.toString(totactprice, '0.00');
                    }
                },
                {
                    field: "status",
                    title: "入库状态",
                    width: "150px",
                    template:function(data) {
                        var value = "";
                        switch (data.status) {
                            case -1:
                                value = "撤销";
                            case 0 :
                                value = "录入";
                                break;
                            case 1:
                                value = "审核";
                                break;
                            case 2 :
                                value = "结束";
                                break;
                            case 3:
                                value = "操作中";
                                break;
                            case 4:
                                value = "申请撤销";
                                break;
                            default:
                                break;
                        }
                        return value;
                    }
                },
                {
                    field: "outStatus",
                    title: "出库状态",
                    width: "150px",
                    template:function(data) {
                        var value = "";
                        switch (data.status) {

                            case 0 :
                                value = "订单状态";
                                break;
                            case 1:
                                value = "已出库";
                                break;
                            case 2 :
                                value = "出库中";
                                break;

                            default:
                                break;
                        }
                        return value;
                    }
                },
                {
                    field: "inStatus",
                    title: "入库状态",
                    width: "150px",
                    template:function(data) {
                        var value = "";
                        switch (data.status) {

                            case 0 :
                                value = "订单状态";
                                break;
                            case 1:
                                value = "已入库";
                                break;
                            case 2 :
                                value = "入库中";
                                break;

                            default:
                                break;
                        }
                        return value;
                    }
                },
                {
                    field: "destUnitName",
                    title: "收货方",
                    width: "180px",

                },
                {
                    field: "origUnitName",
                    title: "发货方",
                    width: "180px",

                },
                {
                    field: "destName",
                    title: "入库仓库",
                    width: "180px",

                },
                {
                    field: "origName",
                    title: "出库仓库",
                    width: "180px",

                },
                {
                    field: "totQty",
                    title: "单据数量",
                    width: "180px",

                },
                {
                    field: "totOutQty",
                    title: "已出库数量",
                    width: "180px",

                },
                {
                    field: "totInQty",
                    title: "已入库数量",
                    width: "180px",

                },
                {
                    field: "remark",
                    title: "备注",
                    width: "180px",

                }






            ]

        });
        isoneTranKendoUIGrid = false;
        $(".k-dropdown").css("width", "6em");
        $(".k-grid-toolbar").css("display", "none");//隐藏toolbar
    } else {
        search();
    }
    inttitledata();
}
var isoneTranStyleKendoUIGrid = true;
function initTranStyleKendoUIGrid() {
    exportExcelid = "searchTranStyleGrid";
    if (isoneTranStyleKendoUIGrid) {
        var filters = serializeToFilter($("#searchForm"));
        console.log(filters);
        $("#searchTranStyleGrid").kendoGrid({
            toolbar: ["excel"],
            excel: {
                fileName: "按商品统计.xlsx",
                proxyURL: basePath + "/search/saleorderCountView/export.do",
                allPages:true,
                filterable: true
            },
            excelExport: function (e) {

            },
            dataSource: {
                schema: {
                    total: "total",
                    model: {

                        fields: {
                            billDate: {type: "date"},
                            billno: {type: "string"},
                            taskId: {type: "string"},
                            token: {type: "number"},
                            deviceId: {type: "string"},
                            origid: {type: "string"},
                            destid: {type: "string"},
                            destUnitId: {type: "string"},
                            busnissname: {type: "string"},
                            origName: {type: "string"},
                            destName: {type: "string"},
                            destUnitName: {type: "string"},
                            styleId: {type: "string"},
                            stylename: {type: "string"},
                            colorId: {type: "string"},
                            sizeId: {type: "string"},
                            qty: {type: "number"},
                            billQty: {type: "number"},
                            diffQty: {type: "number"},
                            price: {type: "number"},
                            saletype: {type: "string"}
                        }
                    },

                    data: "data",
                    groups: "data"
                },
                filter: {
                    logic: "and",
                    filters: filters
                },

                transport: {
                    read: {
                        url: basePath + "/search/transferorderCountViewSearch/readTransByStyleId.do",
                        type: "POST",
                        dataType: "json",
                        async: false,
                        contentType: 'application/json'
                    },
                    parameterMap: function (options) {
                        return JSON.stringify(options);
                    }
                },
                pageSize: 100.0,
                serverSorting: true,
                serverPaging: true,
                serverGrouping: false,
                serverFiltering: true,
                aggregate: [




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
                pageSize: 100.0,
                pageSizes: [100, 500, 1000, 2000, 5000]
            },

            groupable: true,
            columnMenu: true,
            filterable: {
                extra: false
            },
            //selectable: "multiple row",
            reorderable: true,
            resizable: true,
            scrollable: true,

            columns: [

                {
                    field: "styleid",
                    title: "款式",
                    width: "180px",

                },
                {
                    field: "stylename",
                    title: "款式名称",
                    width: "180px",

                },
                {
                    field: "totqty",
                    title: "调拨数量",
                    width: "180px",

                },
                {
                    field: "class3",
                    title: "大分类",
                    width: "180px",

                },
                {
                    field: "class4",
                    title: "小分类",
                    width: "180px",

                },
                {
                    field: "class8",
                    title: "材质",
                    width: "180px",

                },
                {
                    field: "class2",
                    title: "年份",
                    width: "180px",

                }








            ]

        });
        isoneTranStyleKendoUIGrid = false;
        $(".k-dropdown").css("width", "6em");
        $(".k-grid-toolbar").css("display", "none");//隐藏toolbar
    } else {
        search();
    }
    inttitledata();
}
var isoneTransByOrigKendoUIGrid = true;
function initTransByOrigKendoUIGrid() {
    exportExcelid = "searchTransByOrigGrid";
    if (isoneTransByOrigKendoUIGrid) {
        var filters = serializeToFilter($("#searchForm"));
        console.log(filters);
        $("#searchTransByOrigGrid").kendoGrid({
            toolbar: ["excel"],
            excel: {
                fileName: "按仓库统计.xlsx",
                proxyURL: basePath + "/search/saleorderCountView/export.do",
                allPages:true,
                filterable: true
            },
            excelExport: function (e) {

            },
            dataSource: {
                schema: {
                    total: "total",
                    model: {

                        fields: {
                            billDate: {type: "date"},
                            billno: {type: "string"},
                            taskId: {type: "string"},
                            token: {type: "number"},
                            deviceId: {type: "string"},
                            origid: {type: "string"},
                            destid: {type: "string"},
                            destUnitId: {type: "string"},
                            busnissname: {type: "string"},
                            origName: {type: "string"},
                            destName: {type: "string"},
                            destUnitName: {type: "string"},
                            styleId: {type: "string"},
                            stylename: {type: "string"},
                            colorId: {type: "string"},
                            sizeId: {type: "string"},
                            qty: {type: "number"},
                            billQty: {type: "number"},
                            diffQty: {type: "number"},
                            price: {type: "number"},
                            saletype: {type: "string"}
                        }
                    },

                    data: "data",
                    groups: "data"
                },
                filter: {
                    logic: "and",
                    filters: filters
                },

                transport: {
                    read: {
                        url: basePath + "/search/transferorderCountViewSearch/readTransByOrig.do",
                        type: "POST",
                        dataType: "json",
                        async: false,
                        contentType: 'application/json'
                    },
                    parameterMap: function (options) {
                        return JSON.stringify(options);
                    }
                },
                pageSize: 100.0,
                serverSorting: true,
                serverPaging: true,
                serverGrouping: false,
                serverFiltering: true,
                aggregate: [




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
                pageSize: 100.0,
                pageSizes: [100, 500, 1000, 2000, 5000]
            },

            groupable: true,
            columnMenu: true,
            filterable: {
                extra: false
            },
            //selectable: "multiple row",
            reorderable: true,
            resizable: true,
            scrollable: true,

            columns: [


                {
                    field: "origname",
                    title: "仓库",
                    width: "180px",

                },
                {
                    field: "totqty",
                    title: "调拨数量",
                    width: "180px",

                },
                {
                    field: "trantype",
                    title: "调出调入",
                    width: "180px",

                }








            ]

        });
        isoneTransByOrigKendoUIGrid = false;
        $(".k-dropdown").css("width", "6em");
        $(".k-grid-toolbar").css("display", "none");//隐藏toolbar
    } else {
        search();
    }
    inttitledata();
}
function search() {
    debugger;
    var gridData = $("#" + exportExcelid).data("kendoGrid");
    var filters = serializeToFilter($("#searchForm"));
    console.log(filters);
    gridData.dataSource.filter({
        logic: "and",
        filters: filters
    });
    inttitledata();
}
function resetData() {
    debugger;
    var gridData = $("#" + exportExcelid).data("kendoGrid");
    gridData.dataSource.filter({});
}
function exportExcelPOI() {
    var filters = serializeToFilter($("#searchForm"));
    var gridData = $("#" + exportExcelid).data("kendoGrid");
    var total = gridData.dataSource._total;
    var request = {};
    request.page = 1;
    request.pageSize = -1;
    request.take = total;
    request.skip = 0;
    request.filter = {
        logic: "and",
        filters : filters
    };

    var url=basePath+"/search/transferorderCountViewSearch/export.do";

    $("#form1").attr("action",url);
    $("#gridId").val(exportExcelid);
    $("#request").val(JSON.stringify(request));
    $("#form1").submit();

}
function newchooseExportFunction() {
    if(exportExcelid === "searchGrid"){
        exportExcelPOI();
    }else {
        exportExcelKendo();
    }
}