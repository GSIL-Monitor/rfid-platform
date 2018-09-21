$(function () {
    var today = formatDate(new Date(), "yyyy-MM-dd");
    $("#filter_gte_billDate").val(today);
    $("#filter_lte_billDate").val(today);
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

function aMonth() {
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

function refresh() {
    resetData();
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
            fileName: "买手.xlsx",
            proxyURL: basePath + "/search/buyerKpi/export.do",
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
                        buyerId: {type: "string"},
                        buyerName: {type: "string"},
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
                    url: basePath + "/search/buyerKpi/getBuyerKpi.do",
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
                {field: "buyerId", aggregate: "count"},
                {field: "buyerName", aggregate: "count"},
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
            {field: "buyerId", title: "买手号", width: "150px"},
            {field: "buyerName", title: "买手名", aggregates: ["count"], footerTemplate: "#=count#", width: "150px"},
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
            },
            {
                command: [{name: "款明细", click: showDetails}],
                title: "操作",
                // groupable: false,
                width: "150px",
                attributes: {
                    style: "text-align: center; font-size: 8px"
                }
            }
        ]
    });
}

function excelExportPOI() {
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

    $.ajax({
        url:  basePath + "/search/buyerKpi/excelExport.do",
        cache: false,
        async: false,
        type: "POST",
        data: {request: JSON.stringify(request)},
        success: function (data, textStatus) {
        }
    });

    $("#exportForm").attr("action", basePath + "/search/buyerKpi/excelExport.do");
    $("#request").val(JSON.stringify(request));
    $("#exportForm").submit();
}

function showDetails(e) {
    var tr = $(e.target).closest("tr");
    var data = this.dataItem(tr);
    console.log(data.buyerId);

    location.href=basePath + "/search/buyerKpiStyleDetail/viewBuyerKpiStyleDetail.do?buyerId=" + data.buyerId + "&startDate=" + $("#filter_gte_billDate").val() + "&endDate=" + $("#filter_lte_billDate").val();
}
