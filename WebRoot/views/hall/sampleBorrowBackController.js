$(function () {
    inisearchHallRoom();
    iniBorrowBackGrid();
    $(".k-dropdown").css("width", "6em");
    $(".k-grid-toolbar").css("display","none");//隐藏toolbar
    $(".k-datepicker input").prop("readonly", true);
});

function refresh() {
    resetData();
}

function exportExcel(){
    $(".k-grid-excel").click();
}

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

function resetData() {
    var gridData = $("#borrowBackGrid").data("kendoGrid");
    gridData.dataSource.filter({
        field:"type",
        operator:"eq",
        value:0
    });
}

function search() {
    var gridData = $("#borrowBackGrid").data("kendoGrid");
    var filters = serializeToFilter($("#searchForm"));
    filters.push({
        field:"type",
        operator:"eq",
        value:0
    });
    console.log(filters);
    gridData.dataSource.filter({
        logic: "and",
        filters: filters
    });

}


function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}

function iniBorrowBackGrid() {
        $("#borrowBackGrid").kendoGrid({
            toolbar: ["excel"],
            excel: {
                fileName: "样衣借还查询.xlsx",
                proxyURL: basePath + "/hall/borrowBack/export.do",
                filterable: true
            },
            excelExport: function(e) {
                var sheet = e.workbook.sheets[0];
                var backStatus = kendo.template(this.columns[7].template);

                var rowIndex =1;
                var groupNum = this.dataSource._group.length;
                for (var i = 1; i < sheet.rows.length; i++) {
                    var row = sheet.rows[i];
                    if(row.cells[1+groupNum]){
                        var gridRow = $("#borrowBackGrid").data("kendoGrid").dataItem("tr:eq("+rowIndex+")");
                        var dataItem = {
                            taskId:row.cells[1+groupNum].value,
                            backStatus: row.cells[7+groupNum].value,
                        };
                        row.cells[7+groupNum].value = backStatus(dataItem);
                        rowIndex++;
                    }
                }
            },
            dataSource: {
                schema: {
                    total: "total",
                    model: {
                        fields: {
                            taskId: {type: "string"}, // 任务号
                            scanDate: {type: "date"},       // 任务时间
                            deviceId: {type: "string"},     // 扫描设备
                            ownerId:{type:"string"},        // 样衣间编号
                            unitName:{type:"string"},       // 样衣间名称
                            customerId:{type:"string"},     // 借用人编号
                            customerName:{type:"string"},   // 借用人
                                                            // 部门编号
                                                            // 部门名称
                            backStatus:{type:"number"},     // 归还状态
                            backTaskId:{type:"string"},     // 归还任务号
                            preBackDate:{type:"date"},         // 预计归还日期
                                                            // 实际归还日期
                                                            // 归还样衣间编号
                            code:{type:"string"},
                            tagPrice:{type:"string"},       //吊牌价
                            styleId:{type:"string"},
                            styleName:{type:"string"},
                            colorId:{type:"string"},
                            sizeId:{type:"string"},
                            type: {type: "number"},
                            floor:{type:"string"},
                            floorName:{type:"string"}
                        }
                    },
                    "data": "data",
                    "groups": "data"
                },

                transport: {
                    read: {
                        url: basePath + "/hall/borrowBack/list.do",
                        type: "POST",
                        dataType: "json",
                        contentType: 'application/json'
                    },
                    parameterMap: function (options) {
                        return JSON.stringify(options);
                    }
                },
                filter: {
                    logic: "and",
                    filters: [{
                        field: "type",
                        operator: "eq",
                        value: 0
                    }]
                },
                sort: [{field: "scanDate", dir: "desc"}],
                pageSize: 500.0,
                serverSorting: true,
                serverPaging: true,
                serverGrouping: true,
                serverFiltering: true,
                aggregate: [
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
            //   selectable: "multiple row",
            reorderable: true,
            resizable: false,
            scrollable: true,
            columns: [
                {field: "taskId", title: "任务号", width: 150,aggregates:["count"]},
                {field:"styleId",title:"款号",width:150},
                {field:"styleName",title:"款名",width:100,sortable:false,groupable:false},
                {field:"sizeId",title:"尺码",width:100},
                {field:"floor",title:"当前库位",width:150},
                {field:"floorName",title:"库位名称",width:150,sortable:false,groupable:false},
                {field:"tagPrice",title:"吊牌价",width:100,template:function (data) {
                    var txt="";
                    if(data.tagPrice!=0)
                        txt+="¥";
                    txt+=data.tagPrice;
                    return txt;
                }},
                {
                    field: "scanDate", title: "任务时间", width: 200,sortable:true
                    ,aggregates: ["count"],
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
                        var val = kendo.toString(data.value,"yyyy-HH-dd HH:mm:ss");
                        return "任务时间:" + val + ", 总数量:" + totitem;
                    }
                },
                {field: "deviceId", title: "扫描设备", width: 150},
                {field: "ownerId", title: "样衣间编号", width: 150},
                {field: "unitName", title: "样衣间名称", width: 150,sortable:false,groupable:false,filterable:false},
                {field: "customerId", title: "借用人编号", width: 150},
                {field: "customerName", title: "借用人", width: 150,hidden:true},
                {field: "backStatus", title: "归还状态", width: 100,sortable:false,groupable:false,
                    template:function (data) {
                        var status=data.backStatus;
                        var txt="";
                        switch(status){
                            case 0:txt+="正常";break;
                            case 1:txt+="丢失";break;
                            case 2:txt+="损坏";break;
                            case 3:txt+="补标";break
                        }
                        return txt;
                    },
                    groupHeaderTemplate:function (data) {
                    var status=data.backStatus;
                    var txt="";
                    switch(status){
                        case 0:txt+="正常";break;
                        case 1:txt+="丢失";break;
                        case 2:txt+="损坏";break;
                        case 3:txt+="补标";break
                    }
                    return txt;
                }
                },
                {field: "backTaskId", title: "归还任务号", width: 150},
                {field: "preBackDate", title: "预计归还日期", width: 200,
                    aggretates:["count"],
                    filterable:{
                        extra:true,
                        ui:function(element){
                            element.kendoDatePicker({
                               format:"yyyy-MM-dd",
                                cultrae:"zh_CN"
                            });
                        }
                    },
                    format:"{0:yyyy-MM-dd HH:mm:ss}",
                    groupHeaderTemplate:function (data) {
                        var date=data.value;
                        return "预计归还日期:"+kendo.toString(date,"yyyy-MM-dd HH:mm:ss");
                    }
                },
                {field: "backDate", title: "实际归还日期", width: 200,
                    aggretates:["count"],
                    filterable:{
                        extra:true,
                        ui:function(element){
                            element.kendoDatePicker({
                                format:"yyyy-MM-dd",
                                cultrae:"zh_CN"
                            });
                        }
                    },
                    format:"{0:yyyy-MM-dd HH:mm:ss}",
                    groupHeaderTemplate:function (data) {
                        var date=data.value;
                        return "实际归还日期:"+kendo.toString(date,"yyyy-MM-dd HH:mm:ss");
                    }
                },
                {field:"backOwnerId",title:"归还样衣间编号",width:170},
                {field:"backUnitName",title:"归还样衣间名称",width:170,sortable:false,groupable:false},
                {field:"code",title:"吊牌码",width:200},
                {field:"",title:"批次",width:100,template:function (data) {
                    var styleId=data.styleId;
                    var code=data.code;
                    var group=code.substring(styleId.length,code.length);
                    return group;
                }},
                {field:"colorId",title:"颜色",width:100},
            ]
        });
}
function onGrouping(arg) {
    /*
     kendoConsole.log("Group on " + kendo.stringify(arg.groups));
     */
}