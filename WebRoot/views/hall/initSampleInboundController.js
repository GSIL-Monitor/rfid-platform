$(function () {
    inisearchHallRoom();
    iniInboundGrid();
    $(".k-dropdown").css("width", "6em");
    $(".k-grid-toolbar").css("display","none");//隐藏toolbar
    $(".k-datepicker input").prop("readonly", true);
});

function refresh() {
    resetData();
}

function exportExcel() {
    $(".k-grid-excel").click();
}

function resetData() {
    var gridData = $("#initGrid").data("kendoGrid");
    gridData.dataSource.filter({
        field:"type",
        operator:"eq",
        value:3

    });
}

function search() {
    var gridData = $("#initGrid").data("kendoGrid");
    var filters = serializeToFilter($("#searchForm"));
    filters.push({
        field:"type",
        operator:"eq",
        value:3
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

function iniInboundGrid() {
    $("#initGrid").kendoGrid({
        toolbar: ["excel"],
        excel: {
            allPage:true,
            fileName: "初始入库统计.xlsx",
            // proxyURL: basePath + "/hall/initInbound/export.do",
            // filterable: true
        },
        // excelExport: function(e) {
        //     var sheet = e.workbook.sheets[0];
        //     var statusTemplate = kendo.template(this.columns[2].template);
        //
        //     var rowIndex =1;
        //     var groupNum = this.dataSource._group.length;
        //     for (var i = 1; i < sheet.rows.length; i++) {
        //         var row = sheet.rows[i];
        //         if(row.cells[1+groupNum]){
        //             var gridRow = $("#initGrid").data("kendoGrid").dataItem("tr:eq("+rowIndex+")");
        //             var dataItem = {
        //                 billDate:row.cells[1+groupNum].value,
        //                 status: row.cells[2+groupNum].value,
        //             };
        //             row.cells[2+groupNum].value = statusTemplate(dataItem);
        //             rowIndex++;
        //         }
        //     }
        // },
        dataSource: {
            schema : {
                total : "total",
                model : {
                    fields: {
                        taskId: {type: "string"},     //任务号
                        scanDate: {type: "date"},         // 任务时间
                        deviceId: {type: "string"},   // 扫描设备
                        ownerId: {type: "string"},    // 入库编号
                        unitName: {type: "string"},     // 入库方名称
                        code:{type:"string"},            // 吊牌码
                        styleId:{type:"string"},        // 款号
                        styleName:{type:"string"},      // 款名
                                                        // 批次
                        tagPrice:{type:"number"},
                        colorId:{type:"string"},        // 颜色
                        sizeId:{type:"string"},         // 尺码
                        remark:{type:"string"},         // 备注
                        floor:{type:"string"},          // 当前库位
                        floorName:{type:"string"},       // 库位名称
                        type:{type:"number"}
                    }
                },
                "data" : "data",
                "groups" : "data"
            },
            transport: {
                read: {
                    url: basePath + "/hall/initInbound/list.do",
                    type:"POST",
                    dataType: "json",
                    contentType:'application/json'
                },
                parameterMap : function(options) {
                    return JSON.stringify(options);
                }
            },
            filter: {
                logic: "and",
                filters: [{
                    field: "type",
                    operator: "eq",
                    value: 3
                }]
            },
            sort:[{field: "scanDate", dir: "desc"}],
            pageSize: 500.0,
            serverSorting : true,
            serverPaging : true,
            serverGrouping : true ,
            serverFiltering : true,
            aggregate: [
                { field: "taskId", aggregate: "count" },
            ]
        },


        sortable: {
            mode: "multiple",
            allowUnsort: true
        },
        pageable: {
            input : true,
            buttonCount: 5,
            pageSize: 500.0,
            pageSizes : [100, 500, 1000, 2000, 5000]
        },

        groupable: true,
        group: onGrouping,
        columnMenu: true,
        filterable: {
            extra:false
        },
        //   selectable: "multiple row",
        reorderable: true,
        resizable: false,
        scrollable: true,
        columns: [
            {field: "taskId", title: "任务号", width: 150,aggregates:["count"]},
            {field: "scanDate", title: "任务日期", width: 200
                ,aggregates:["count"],
                filterable:{
                    extra:true,
                    ui:function (element) {
                        element.kendoDatePicker({
                            format:"yyyy-MM-dd",
                            culture:"zh-CN"
                        });
                    }
                },
                format:"{0:yyyy-MM-dd HH:mm:ss}",
                groupHeaderTemplate:function(data){
                    var totitem=data.aggregates.taskId.count;
                    var val=kendo.toString(data.value,"yyyy-MM-dd HH:mm:ss");
                    return "任务日期:" + val + ", 总数量:" + totitem;
                }
            },
            {field:"deviceId",title:"扫描设备",width:150},
            {field:"ownerId",title:"入库方编号",width:150},
            {field:"unitName",title:"入库方名称",width:150,groupable:false,sortable:false,filterable:false},
            {field:"code",title:"吊牌码",width:230},
            {field:"tagPrice",title:"吊牌价",width:100,template:function (data) {
                    var txt="";
                    if(data.tagPrice==0)
                        txt+="¥";
                    txt+=data.tagPrice;
                return txt;
            }},
            {field:"styleId",title:"款号",width:150},
            {field:"styleName",title:"款名",width:150,groupable:false,sortable:false,filterable:false},
            {field:"",title:"批次",width:150,groupatble:false,sortable:false,
                template:function (data) {
                    var code =data.code;
                    var styleId=data.styleId;
                    var group=code.substring(styleId.length,code.length);
                    return group;
                }
            },
            {field:"colorId",title:"颜色",width:100},
            {field:"sizeId",title:"尺码",width:100},
            {field:"floor", title:"当前库位",width:150},
            {field:"floorName",title:"库位名称",width:150,groupable:false,sortable:false,filterable:false},
            {field:"remark",title:"备注",width:100,filterable:false,sortable:false,groupable:false}
        ]
    });

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


function onGrouping(arg) {
    /*
     kendoConsole.log("Group on " + kendo.stringify(arg.groups));
     */
}