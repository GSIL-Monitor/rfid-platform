$(function () {
    initKendoUIGrid();
    $(".k-dropdown").css("width", "6em");
    $(".k-grid-toolbar").css("display", "none");//隐藏toolbar
    $(".k-datepicker input").prop("readonly", true);

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
});

function refresh() {
    resetData();
}
function resetData() {
    var gridData = $("#plEmail").data("kendoGrid");
    gridData.dataSource.filter({});
}
function exportExcel() {
    $(".k-grid-excel").click();
}
function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}
function search() {
    var gridData = $("#plEmail").data("kendoGrid");
    var filters = serializeToFilter($("#searchForm"));
    console.log(filters);
    gridData.dataSource.filter({
        logic: "and",
        filters: filters
    });

}

function add(){
 window.location.href=basePath+'/views/third/pl/plEmailTemplate_edit.jsp';
}

function edit(){
    var row = $("#plEmail").data("kendoGrid").select(); //获取选中行对象
    var data = $("#plEmail").data("kendoGrid").dataItem(row); //获取选中行元素数据
    if(data){
        window.location.href=basePath+"/third/playlounge/plEmailTemplate/showEdit.do?id="+data.id;
    }else{
        bootbox.alert("请选择一项进行修改！");
    }
}

function initKendoUIGrid() {

    $("#plEmail").kendoGrid({
        toolbar: ["excel"],
        excel: {
            allPages: true
        },
        dataSource: {
            schema: {
                total: "total",
                model: {
                    fields: {
                        toUser: {type: "string"},
                        warmLevel: {type: "string"},
                        sendCycle: {type: "string"}
                    }
                },
                data: "data",
                groups: "data"
            },

            transport: {
                read: {
                    url: basePath + "/third/playlounge/plEmailTemplate/list.do",
                    type: "POST",
                    dataType: "json",
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
            serverFiltering: true
        },
        pageable: {
            input: true,
            buttonCount: 5,
            pageSize: 100.0,
            pageSizes: [100, 500, 1000]
        },

        selectable: "multiple row",
        reorderable: true,
        resizable: false,
        scrollable: true,

        columns: [
            {field: "toUser", title: "收件人", width: "130px"},
            {field: "warmLevel", title: "等级", width: "130px",
                template: function (data) {
                    switch (data.warmLevel){
                        case "1":
                            return "I级";
                        break;
                        case "2":
                            return "II级";
                        break;
                        case "3":
                            return "III级";
                            break;
                        case "4":
                            return "IV级";
                            break;
                    }
                }
            },
            {field: "sendCycle", title: "周期", width: "130px",
                template: function (data) {
                    switch (data.sendCycle){
                        case "w":
                            return "每周";
                            break;
                        case "m":
                            return "每月";
                            break;
                    }
                }},
            {field: "remark", title: "备注", width: "180px"}
        ]
    });

}
