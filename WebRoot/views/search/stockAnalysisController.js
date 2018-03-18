/**
 * Created by yushen on 2017/11/17.
 */
$(function () {
    initWarehouseSelect();
    initStockAnalysisDate();
});

function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}

function exportExcel() {
    $("#stockAnalysisGrid").children().find(".k-grid-excel").click();
}

function refresh() {
    location.reload(true);
}

function resetData() {
    var gridData = $("#searchGrid").data("kendoGrid");
    gridData.dataSource.filter({});
}

function search() {
    var gridData = $("#stockAnalysisGrid").data("kendoGrid");
    var filters = serializeToFilter($("#searchForm"));
    console.log(filters);
    gridData.dataSource.filter({
        logic: "and",
        filters: filters
    });
}

function initWarehouseSelect() {
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9",
        cache: false,
        type: "POST",
        success: function (data) {
            $("#form_wareHouseId").empty();
            $("#form_wareHouseId").append("<option value='' style='background-color: #eeeeee'>--请选择出库仓库--</option>");
            $("#form_wareHouseId").append("<option value='All'>所有仓库</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#form_wareHouseId").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                $("#form_wareHouseId").trigger('chosen:updated');
            }
        }

    })
}

function initStockAnalysisDate() {
    var filters = serializeToFilter($("#searchForm"));
    console.log(filters);
    $("#stockAnalysisGrid").kendoGrid({
        toolbar: ["excel"],
        excel: {
            fileName: "库存状态分析.xlsx",
            proxyURL: basePath + "/search/stockAnalysis/export.do",
            allPages: true,
            filterable: true
        },
        // excelExport: function (e) {
        //     var sheet = e.workbook.sheets[0];
        //     var rowIndex = 1;
        //     var groupNum = this.dataSource._group.length;
        //     for (var i = 1; i < sheet.rows.length; i++) {
        //         var row = sheet.rows[i];
        //         if (row.cells[3 + groupNum] && row.cells[5 + groupNum] && row.cells[6 + groupNum] && row.cells[7 + groupNum]) {
        //             rowIndex++;
        //         }
        //     }
        // },
        dataSource: {
            schema: {
                total: "total",
                model: {
                    fields: {
                        wareHouseId: {type: "string"},
                        wareHouseName: {type: "string"},
                        sku: {type: "string"},
                        inStockQty: {type: "number"},
                        inStockTime: {type: "number"}
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
                    url: basePath + "/search/stockAnalysis/readStockAnalysis.do",
                    type: "POST",
                    dataType: "json",
                    async: false,
                    contentType: 'application/json'
                },
                parameterMap: function (options) {
                    return JSON.stringify(options);
                }
            },
            pageSize: 500.0,
            serverSorting: true,
            serverPaging: true,
            serverGrouping: false,
            serverFiltering: true,
            aggregate: [
                {field: "inStockQty", aggregate: "sum"}
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
            pageSize: 500.0,
            pageSizes: [100, 500, 1000, 2000, 5000]
        },
        groupable: true,
        group: onGrouping,
        // columnMenu: true,
        // filterable: {
        //     extra: false
        // },
        // reorderable: true,
        resizable: true,
        scrollable: true,
        columns: [
            {
                field: "wareHouseId", title: "仓库编号", width: "150px",
                attributes: {
                    style: "font-size: 12px"
                }
            },
            {
                field: "wareHouseName", title: "仓库名", width: "150px",
                attributes: {
                    style: "font-size: 12px"
                }
            },
            {
                field: "sku", title: "SKU(款色码)", width: "150px",
                attributes: {
                    style: "font-size: 12px"
                }
            },
            {
                field: "inStockQty", title: "在库数量", width: "150px",
                attributes: {
                    style: "font-size: 12px"
                },
                aggregates: ["sum"],
                footerTemplate: "#=sum#"
            },
            {
                field: "inStockTime", title: "平均在库时长(天)", width: "150px",
                attributes: {
                    style: "font-size: 12px"
                }
            }
        ]
    });
    $(".k-grid-toolbar").hide();
}
function onGrouping(arg) {
    /*
     kendoConsole.log("Group on " + kendo.stringify(arg.groups));
     */
}