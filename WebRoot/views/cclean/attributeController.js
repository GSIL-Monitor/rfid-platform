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
    var combobox = $("#attr_type").data("kendoComboBox");
    combobox.readonly(false);
    $("#attribute_dialog").data('kendoDialog').open();
}
function openEditArribute(rowData) {
    $("#attribute_dialog").data('kendoDialog').open();
    $('#attributeForm').loadData(rowData);
    var combobox = $("#attr_type").data("kendoComboBox");
    combobox.value(rowData.type)
    combobox.readonly(true);
}
function initButton() {
    $("#button").kendoButton({
        click: function (e) {
            alert(e.event.target.tagName);
        }
    });
}
function initSelect() {
    $("#form_type").kendoMultiSelect({
        dataSource: [
            {name: "操作人", id: 1},
            {name: "产权所有者", id: 2}
        ],
        dataTextField: "name",
        dataValueField: "id",
        height: 400,
        suggest: true
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
                    url: basePath + "/cclean/listAddedAttribute.do",
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
                        id: {type: "string"},
                        code: {type: "string"},
                        type: {type: "number"},
                        remark: {type: "string"},
                        typeName: {type: "string"},
                        name: {type: "string"}

                    }
                },
                data: "data"
            },
            sort: [
                {field: "type", dir: "desc"},
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
        toolbar: "<p> 属性信息</p>",
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
                width: "200px",
                title: "ID",
                field: "id",
                sortable: false,
            },
            {
                field: "type", title: "类型", width: "120px", sortable: true,
                template: function (data) {
                    return data.typeName;
                }
            },
            {field: "code", title: "编号", width: "200px", sortable: true},
            {field: "name", title: "名称", width: "400px", sortable: true},
            {field: "updateCode", title: "修改人", width: "100px"},
            {field: "updateTime", title: "更新时间", width: "170px", format: "{0:yyyy-MM-dd HH:mm:ss}", sortable: true},
            {field: "remark", title: "备注", width: "200px", sortable: false}
        ]
    });
}

function onGrouping(arg) {
}

   