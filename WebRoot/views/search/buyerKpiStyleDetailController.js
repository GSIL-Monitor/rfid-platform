$(function () {
    initSelectBuyerSelect();
    initKendoUIGrid();
    $(".k-dropdown").css("width", "6em");
    $(".k-grid-toolbar").css("display", "none");//隐藏toolbar
});

function today() {
    var today = formatDate(new Date(), "yyyy-MM-dd");
    $("#filter_gte_billDate").val(today);
    $("#filter_lte_billDate").val(today);
    search();
}

function thisMonth() {
    var today = formatDate(new Date(), 'yyyy-MM-dd');
    var firstDaty = formatDate(new Date((new Date()).setDate(1)), 'yyyy-MM-dd');
    $("#filter_gte_billDate").val(firstDaty);
    $("#filter_lte_billDate").val(today);
    search();
}

function lastMonth() {
    var theDay = new Date();
    theDay.setMonth(theDay.getMonth() - 1);
    var lastMonthFirstDay = formatDate(new Date(theDay.setDate(1)), 'yyyy-MM-dd');
    var lastMonthLastDay = formatDate(new Date((new Date()).setDate(0)), 'yyyy-MM-dd');
    $("#filter_gte_billDate").val(lastMonthFirstDay);
    $("#filter_lte_billDate").val(lastMonthLastDay);
    search();
}

function initSelectBuyerSelect() {
    var url;
    url = basePath + "/sys/user/list.do?filter_EQS_roleId=BUYER";
    $.ajax({
        url: url,
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#filter_contains_buyerId").empty();
            $("#filter_contains_buyerId").append("<option value='' >--请选择买手--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#filter_contains_buyerId").append("<option value='" + json[i].id + "'>" + json[i].name + "</option>");
            }
            $("#filter_contains_buyerId").val(buyerId);
            $(".selectpicker").selectpicker('refresh');
        }
    });
}

function refresh() {
    location.reload();
}

function resetData() {
    var today = formatDate(new Date(), "yyyy-MM-dd");
    $("#filter_gte_billDate").val(today);
    $("#filter_lte_billDate").val(today);
    $("#filter_contains_buyerName").val("");
    var gridData = $("#searchGrid").data("kendoGrid");
    var filters = serializeToFilter($("#searchForm"));
    gridData.dataSource.filter({
        logic: "and",
        filters: filters
    });
}

function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}

function search() {
    var gridData = $("#searchGrid").data("kendoGrid");
    var filters = serializeToFilter($("#searchForm"));
    console.log(filters);
    gridData.dataSource.filter({
        logic: "and",
        filters: filters
    });
}

function exportExcel() {
    $("#searchGrid").children().find(".k-grid-excel").click();
}

function initKendoUIGrid() {
    var filters = serializeToFilter($("#searchForm"));
    console.log(filters);
    $("#searchGrid").kendoGrid({
        toolbar: ["excel"],
        excel: {
            fileName: "买手KPI_款明细.xlsx",
            proxyURL: basePath + "/search/buyerKpiStyleDetail/export.do",
            allPages: true,
            filterable: true
        },
        excelExport: function (e) {
            var sheet = e.workbook.sheets[0];
            var rowIndex = 1;
            var groupNum = this.dataSource._group.length;
            for (var i = 1; i < sheet.rows.length; i++) {
                var row = sheet.rows[i];
                if (row.cells[3 + groupNum] && row.cells[5 + groupNum] && row.cells[6 + groupNum] && row.cells[7 + groupNum]) {
                    $("#searchGrid").data("kendoGrid").dataItem("tr:eq(" + rowIndex + ")");
                    rowIndex++;
                }
            }
        },
        dataSource: {
            schema: {
                total: "total",
                model: {
                    fields: {
                        styleId: {type: "string"},
                        purchaseQty: {type: "number"},
                        purchaseInQty: {type: "number"},
                        stockQty: {type: "number"},
                        saleQty: {type: "number"},
                        returnBackQty: {type: "number"}
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
                    url: basePath + "/search/buyerKpiStyleDetail/getStyleDetail.do",
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
            // serverSorting: true,
            serverPaging: true,
            serverGrouping: false,
            serverFiltering: true,
            aggregate: [
                {field: "styleId", aggregate: "count"},
                {field: "purchaseQty", aggregate: "sum"},
                {field: "purchaseInQty", aggregate: "sum"},
                {field: "stockQty", aggregate: "sum"},
                {field: "saleQty", aggregate: "sum"},
                {field: "returnBackQty", aggregate: "sum"}
            ]
        },
        // sortable: {
        //     mode: "multiple",
        //     allowUnsort: true
        // },
        rowNumber: true,
        pageable: {
            input: true,
            buttonCount: 5,
            pageSize: 20.0,
            pageSizes: [10, 20, 50, 100]
        },

        // groupable: true,
        /* group: onGrouping,*/
        // columnMenu: true,
        // filterable: {
        //     extra: false
        // },
        //selectable: "multiple row",
        // reorderable: true,
        resizable: true,
        scrollable: true,

        columns: [
            {field: "styleId", title: "款号", aggregates: ["count"], footerTemplate: "#=count#", width: "150px"},
            {field: "purchaseQty", title: "采购数", aggregates: ["sum"], footerTemplate: "#=sum#", width: "150px"},
            {field: "purchaseInQty", title: "采购入库数", aggregates: ["sum"], footerTemplate: "#=sum#", width: "150px"},
            {field: "stockQty", title: "库存数", aggregates: ["sum"], footerTemplate: "#=sum#", width: "150px"},
            {field: "saleQty", title: "销售数", aggregates: ["sum"], footerTemplate: "#=sum#", width: "150px"},
            {
                field: "returnBackQty",
                title: "返厂数",
                width: "150px",
                // groupable: false,
                aggregates: ["sum"],
                footerTemplate: "#=sum#"
            }
        ]
    });
}

function exportExcelImagePOI() {
    var filters = serializeToFilter($("#searchForm"));
    var gridData = $("#searchGrid").data("kendoGrid");
    console.log(gridData);
    var total = gridData.dataSource._total;
    var request = {};
    request.page = gridData.dataSource._page;
    request.pageSize = gridData.dataSource._pageSize;
    request.take = gridData.dataSource._pageSize;
    request.sort = gridData.dataSource._sort;
    request.skip = gridData.dataSource._skip;
    request.filter = {
        logic: "and",
        filters: filters
    };
    var url = basePath + "/search/PurchaseorCountviews/exportnew.do";
    $("#form1").attr("action", url);
    $("#request").val(JSON.stringify(request));
    $("#form1").submit();
}

function exportExcelProPOI() {
    var filters = serializeToFilter($("#searchForm"));
    var gridData = $("#searchGrid").data("kendoGrid");
    var total = gridData.dataSource._total;
    var request = {};
    request.page = 1;
    request.pageSize = total;
    request.take = total;
    request.skip = 0;
    request.filter = {
        logic: "and",
        filters: filters
    };
    var url = basePath + "/search/PurchaseorCountviews/exportnew.do";
    $("#form1").attr("action", url);
    $("#request").val(JSON.stringify(request));
    $("#form1").submit();
}

function exportExcelPOI() {
    var filters = serializeToFilter($("#searchForm"));
    var gridData = $("#searchGrid").data("kendoGrid");
    var total = gridData.dataSource._total;
    var request = {};
    request.page = 1;
    request.pageSize = -1;
    request.take = total;
    request.skip = 0;
    request.filter = {
        logic: "and",
        filters: filters
    };
    var url = basePath + "/search/PurchaseorCountviews/exportnew.do";
    $("#form1").attr("action", url);
    $("#request").val(JSON.stringify(request));
    $("#form1").submit();
}

