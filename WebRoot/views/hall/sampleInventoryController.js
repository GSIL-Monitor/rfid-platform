$(function () {
    inisearchHallRoom();
    inisearchHallFloor();
    iniInventoryGrid();
    $(".k-dropdown").css("width", "6em");
    $(".k-grid-toolbar").css("display", "none");//隐藏toolbar
    $(".k-datepicker input").prop("readonly", true);
});

function inisearchHallRoom() {
    $.ajax({
        url: basePath + "/hall/room/list.do",
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            var json = data;
            if (json) {
                for (var i = 0; i < json.length; i++) {
                    $("#filter_eq_ownerId").append("<option value='" + json[i].code + "'>" + json[i].name + "</option>");
                    $("#filter_eq_ownerId").trigger("chosen:updated");
                }
            }
        }
    });
}

function iniInventoryGrid() {
    $("#sampleInventoryGrid").kendoGrid({
        toolbar: ["excel"],
        excel: {
            allPages:true,
            fileName: "样衣盘点查询.xlsx",
            // proxyURL: basePath + "/hall/sampleInventory/export.do",
            // filterable: true
        },
        // excelExport: function (e) {
        //     var sheet = e.workbook.sheets[0];
        //     var statusTemplate = kendo.template(this.columns[2].template);
        //
        //     var rowIndex = 1;
        //     var groupNum = this.dataSource._group.length;
        //     for (var i = 1; i < sheet.rows.length; i++) {
        //         var row = sheet.rows[i];
        //         if (row.cells[1 + groupNum]) {
        //             var gridRow = $("#sampleInventoryGrid").data("kendoGrid").dataItem("tr:eq(" + rowIndex + ")");
        //             var dataItem = {
        //                 scanDate: row.cells[1 + groupNum].value,
        //                 status: row.cells[2 + groupNum].value,
        //             };
        //             row.cells[2 + groupNum].value = statusTemplate(dataItem);
        //             rowIndex++;
        //         }
        //     }
        // },
        dataSource: {
            schema: {
                total: "total",
                model: {
                    fields: {
                        id: {type: "string"},
                        taskId: {type: "string"},
                        billDate: {type: "date"},
                        // status: {type: "string"},
                        isCheck: {type: "string"},
                        ownerId: {type: "string"},
                        ownerName: {type: "string"},
                        deviceId: {type: "string"},
                        floor: {type: "string"},
                        // floorName: {type: "string"},
                        actQty: {type: "number"},
                        qty: {type: "number"}
                    }
                },
                "data": "data",
                "groups": "data"
            },

            transport: {
                read: {
                    url: basePath + "/hall/sampleInventory/list.do",
                    type: "POST",
                    dataType: "json",
                    contentType: 'application/json'
                },
                parameterMap: function (options) {
                    return JSON.stringify(options);
                }
            },
            sort: [{field: "scanDate", dir: "desc"}],
            pageSize: 500.0,
            serverSorting: true,
            serverPaging: true,
            serverGrouping: true,
            serverFiltering: true,
            aggregate: [

                {field: "qty", aggregate: "sum"},
                {field: "actQty", aggregate: "sum"},
                {field: "taskId", aggregate: "count"},

            ]
        },
        sortable: {
            mode: "multiple",
            allowUnsort: true
        },
        pageable: {
            input: true,
            buttonCount: 5,
            pageSize: 500.0,
            pageSizes: [100, 500, 1000, 2000, 5000]
        },

        groupable: true,
        group: onGrouping,
        columnMenu: true,
        filterable: {
            extra: false
        },
        selectable: "row",
        reorderable: true,
        resizable: false,
        scrollable: true,
        columns: [
            {
                field: "isCheck",
                title: "审核状态",
                width: 100,
                sortable: false,
                groupable: false,
                template: function (data) {
                    var check = data.isCheck;
                    if (check == "CK") {
                        return "已审核";
                    } else {
                        return "未审核";
                    }
                }
            },
            {field: "taskId", title: "任务号", width: 100, aggregates: ["count"]},
            {
                field: "billDate", title: "盘点日期", width: 150
                , aggregates: ["count"],
                filterable: {
                    extra: true,
                    ui: function (element) {
                        element.kendoDatePicker({
                            format: "yyyy-MM-dd",
                            culture: "zh-CN"
                        });
                    }
                },
                format: "{0:yyyy-MM-dd HH:mm:ss}",
                groupHeaderTemplate: function (data) {
                    var totitem = data.aggregates.taskId.count;
                    var val = kendo.toString(data.value, "yyyy-MM-dd HH:mm:ss");
                    return "盘点日期:" + val + ", 总数量:" + totitem;
                }
            },
            {
                field: "status", title: "状态", width: 70,
                template: function (data) {
                    var status = data.status;
                    var text = "";
                    if (status == 0) {
                        text += "正常";
                    } else if (status == 1) {
                        text += "盘盈";
                    } else if (status == 2) {
                        text += "盘亏";
                    } else if (status == 3) {
                        text += "有盈有亏";
                    } else if (status == 4) {
                        text += "装箱盘点";
                    }
                    return text;
                },
                groupHeaderTemplate: function (data) {
                    var status = data.value;
                    var text = "状态：";
                    if (status == 0) {
                        text += "正常";
                    } else if (status == 1) {
                        text += "盘盈";
                    } else if (status == 2) {
                        text += "盘亏";
                    } else if (status == 3) {
                        text += "有盈有亏";
                    } else if (status == 4) {
                        text += "装箱盘点";
                    }
                    return text;
                }
            },
            {field: "ownerId", title: "样衣间编号", width: 100},
            {field: "ownerName", title: "样衣间名称", width: 150, sortable: false, groupable: false, filterable: false},
            {field: "deviceId", title: "扫描设备", width: 150},
            {field: "floor", title: "库位", width: 100, sortable: false, groupable: false, filterable: false},
            {field: "floorName", title: "库位名称", width: 150, sortable: false, groupable: false, filterable: false},
            {field: "qty", title: "应盘数量", width: 100},
            {field: "actQty", title: "实盘数量", width: 100},
        ]
    });
}

function exportExcel() {
    $(".k-grid-excel").click();
}

function inisearchHallFloor() {
    $.ajax({
        url: basePath + "/hall/floor/list.do",
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            var json = data;
            if (json) {
                for (var i = 0; i < json.length; i++) {
                    if (json[i].code.length == 8) {
                        $("#filter_eq_floor").append("<option value='" + json[i].code + "'>" + json[i].name + "</option>");
                        $("#filter_eq_floor").trigger("chosen:updated");
                    }
                }
            }
        }
    });
}
function checkSample() {
    var row = $("#sampleInventoryGrid").data("kendoGrid").select();
    var data = $("#sampleInventoryGrid").data("kendoGrid").dataItem(row);
    if (data) {
        if (data.isCheck == "CK") {
            bootbox.alert("已审核过的信息不必重复审核");
        } else {
            bootbox.confirm({
                title: "确认审核？",
                message: "审核前请确保信息的完整",
                buttons: {
                    confirm: {
                        label: "确认",
                        className: "btn-primary"
                    },
                    cancel: {
                        label: "取消",
                        // className: "btn-inverse"
                    },
                },
                callback: function (result) {
                    console.log("the confirm result is:" + result);
                    console.log("select row data is:" + data.taskId);
                    if (result) {
                        $.post(
                            basePath + "/hall/sampleInventory/check.do?taskId=" + data.taskId, function (result) {
                                if (result.success == true) {
                                    resetData();
                                } else {
                                    bootbox.alert("审核失败!");
                                }
                            }
                        );
                    }
                }
            });
        }
    } else {
        bootbox.alert("请先选中一行以审核");
    }
}

function refresh() {
    resetData();
}
function resetData() {
    var gridData = $("#sampleInventoryGrid").data("kendoGrid");
    gridData.dataSource.filter({});
}

function showDetails() {
    var row = $("#sampleInventoryGrid").data("kendoGrid").select();
    var data = $("#sampleInventoryGrid").data("kendoGrid").dataItem(row);
    if (data) {
       location.href=basePath+"/hall/sampleInventory/detail.do?taskId="+data.taskId;
    } else {
        bootbox.alert("请选择一项查看明细");
    }
}


function search() {
    var gridData = $("#sampleInventoryGrid").data("kendoGrid");
    var filters = serializeToFilter($("#searchForm"));
    console.log(filters);
    gridData.dataSource.filter({
        logic: "and",
        filters: filters
    });

}


function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}

function onGrouping(arg) {
    /*
     kendoConsole.log("Group on " + kendo.stringify(arg.groups));
     */
}