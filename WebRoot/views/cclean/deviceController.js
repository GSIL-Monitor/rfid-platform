$(function () {

    initSelect();
    initKendoUIGrid();
    $(".k-dropdown").css("width", "6em");
    /*   $(".k-grid-toolbar").css("display", "none");//隐藏toolbar*/
    $(".k-datepicker input").prop("readonly", true);
    initButton();
    initDialog();
});

function openNewAttribute() {
    var combobox = $("#deviceId").data("kendoComboBox");
    combobox.readonly(false);
    $("#device_dialog").data('kendoDialog').open();
}
function openEditArribute(rowData) {
    $("#device_dialog").data('kendoDialog').open();
    $('#deviceForm').loadData(rowData);
    var combobox = $("#deviceId").data("kendoComboBox");
    combobox.value(rowData.deviceId);
    combobox.readonly(true);
    var location = $("#locationCode").data("kendoComboBox");
    location.value(rowData.locationCode);
    var warehouse = $("#warehouseCode").data("kendoComboBox");
    warehouse.value(rowData.warehouseCode);
    var factory = $("#factoryCode").data("kendoComboBox");
    factory.value(rowData.factoryCode);
}
function initButton() {
    $("#button").kendoButton({
        click: function (e) {
            alert(e.event.target.tagName);
        }
    });
}
function initSelect() {
    $("#form_factoryCode").kendoMultiSelect({
        dataTextField: "name",
        dataValueField: "code",
        height: 400,
        suggest: true,
        dataSource: {
            transport: {
                read: basePath + "/api/cclean/downloadFactoryWS.do?deviceId=KE201601"
            },
            schema: {
                data: "result"
            }
        }
    });
    $("#form_warehouseCode").kendoMultiSelect({
        dataTextField: "name",
        dataValueField: "code",
        height: 400,
        suggest: true,
        dataSource: {
            transport: {
                read: basePath + "/api/cclean//downloadWarehouseWS.do?deviceId=KE201601"
            },
            schema: {
                data: "result"
            }
        }
    });
}
function refresh() {
    resetData();
}
function resetData() {
    var gridData = $("#grid").data("kendoGrid");
    gridData.dataSource.filter({});
}
function exportExcel() {
    $(".k-grid-excel").click();
}
function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}

function search() {
    var gridData = $("#grid").data("kendoGrid");
    var filters = serializeToFilter($("#searchForm"));
    gridData.dataSource.filter({
        logic: "and",
        filters: filters
    });
}

function serializeToFilter(from) {
    var filters = [];
    var o = {};
    var fromDatafrom = from.serializeArray();
    $.each(fromDatafrom, function (index) {

        if (o[this['name']]) {
            o[this['name']] = o[this['name']] + "," + this['value'];
        } else {
            o[this['name']] = this['value'];
        }


    });
    for (var key in o) {
        var obj = key.split("_");
        var value;
        if (obj[2].indexOf("Date") != -1) {

            if (o[key] != "") {
                value = new Date(o[key]);
                if (obj[1].indexOf("lte") != -1) {
                    value = new Date(o[key]);
                    value = new Date(value.getTime() + 24 * 60 * 60 * 1000 - 1);
                }
                filters.push({
                    field: obj[2],
                    operator: obj[1],
                    value: value
                });
            }
        } else {
            value = o[key];
            if (value != "") {
                filters.push({
                    field: obj[2],
                    operator: obj[1],
                    value: value
                });
            }

        }
    }
    return filters;
}
function initKendoUIGrid() {
    $("#grid").kendoGrid({
        dataSource: {
            transport: {
                total: "total",
                read: {
                    url: basePath + "/cclean/listLinenDevice.do",
                    type: "POST",
                    dataType: "json",
                    contentType: 'application/json'
                },
                parameterMap: function (options) {
                    return JSON.stringify(options);
                }
            },
            change: function (e) {
                var data = this.data();
                console.log(data.length); // displays "77"
            },
            schema: {
                total: "total",
                model: {
                    id: 'id',
                    fields: {
                        updateTime: {type: "date"},
                        updateCode: {type: "string"},
                        deviceId: {type: "string"},
                        storageName: {type: "string"},
                        factoryCode: {type: "string"},
                        factory: {type: "string"},
                        warehouseCode: {type: "string"},
                        locationCode: {type: "string"},
                        location: {type: "string"},
                        remark: {type: "string"}

                    }
                },
                data: "data"
            },
            sort: [
                {field: "deviceId", dir: "desc"},
                {field: "updateTime", dir: "desc"}
            ],
            pageSize: 100.0,
            serverSorting: true,
            serverPaging: true,
            serverGrouping: false,
            serverFiltering: true
        },
        filterable: false,
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
        columnMenu: true,
        selectable: false,
        reorderable: true,
        resizable: false,
        scrollable: true,
        toolbar: "<p> 设备信息</p>",
        columns: [
            {
                width: "100px",
                locked: true,
                lockable: false,
                template: function (data) {
                    return "<div class='btn-group pull-left'><button class='btn btn-info'onclick='openEditArribute(" + JSON.stringify(data) + ")'> 修改</button>"
                    "</div>";
                }
            },
            {
                width: "100px",
                title: "设备号",
                field: "deviceId",
                sortable: true,
            },
            {
                field: "storageName", title: "归属地", width: "120px", sortable: true
            },
            {field: "warehouseCode", title: "仓库编号", width: "200px", sortable: true},
            {field: "warehouse", title: "仓库名称", width: "250px", sortable: true},
            {field: "factoryCode", title: "工厂编号", width: "200px", sortable: true},
            {field: "factory", title: "工厂", width: "250px", sortable: true},
            {field: "locationCode", title: "所在地编号", width: "200px", sortable: true},
            {field: "location", title: "所在地", width: "250px", sortable: true},
            {field: "updateCode", title: "修改人", width: "100px"},
            {field: "updateTime", title: "更新时间", width: "170px", format: "{0:yyyy-MM-dd HH:mm:ss}", sortable: true},
            {field: "remark", title: "备注", width: "200px", sortable: false}
        ]
    });
}

function onGrouping(arg) {
}

   