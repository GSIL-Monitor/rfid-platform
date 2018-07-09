$(function () {
    //获取当前转户
    initMultiSelect();
    var myDate = new Date();
    var year = myDate.getFullYear();
    var month = myDate.getMonth() + 1;
    var day = myDate.getDate();
    if (month < 10) {
        if (day < 10) {
            $("#filter_gte_intimeDate").val(year + "-0" + month + "-0" + day);
            $("#filter_lte_intimeDate").val(year + "-0" + month + "-0" + day);
        } else {
            $("#filter_gte_intimeDate").val(year + "-0" + month + "-" + day);
            $("#filter_lte_intimeDate").val(year + "-0" + month + "-" + day);
        }
    } else {
        if(day < 10){
            $("#filter_gte_intimeDate").val(year + "-" + month + "-0" + day);
            $("#filter_lte_intimeDate").val(year + "-" + month + "-0" + day);
        }else{
            $("#filter_gte_intimeDate").val(year + "-" + month + "-" + day);
            $("#filter_lte_intimeDate").val(year + "-" + month + "-" + day);
        }

    }
    onIntimeday();
    initKendoUIGrid();
    inttitledata();
    $(".k-dropdown").css("width", "6em");
    $(".k-grid-toolbar").css("display", "none");//隐藏toolbar

});
function inttitledata() {
    var serializeArray = $("#searchForm").serializeArray();
    var params = array2obj(serializeArray);
    $.ajax({
        url: basePath + "/search/PurchaseorCountviews/findtitledate.do",
        cache: false,
        async: false,
        data: {"dates": JSON.stringify(params)},
        type: "POST",
        success: function (data, textStatus) {
            var result = data.result;
            $(".purchasesum").text(":" + result.purchasesum);
            $(".purchasonesum").text(":" + result.purchasonesum);
            $(".purchasmony").text(": ￥" + result.purchasmony.toFixed(2));
        }
    });
}
function initMultiSelect() {
    $.ajax({
        url: basePath + "/unit/list.do?filter_EQI_type=9",
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            $("#filter_in_destid").empty();
            $("#filter_in_orderwarehouseid").empty();
            $("#filter_in_destid").append("<option value='' >--请选择入库仓库--</option>");
            $("#filter_in_orderwarehouseid").append("<option value=''>--请选择接收仓库--</option>");
            var json = data;
            for (var i = 0; i < json.length; i++) {
                $("#filter_in_destid").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                $("#filter_in_orderwarehouseid").append("<option value='" + json[i].id + "'>" + "[" + json[i].code + "]" + json[i].name + "</option>");
                /*$("#filter_in_destid").trigger('chosen:updated');
                $("#filter_in_orderwarehouseid").trigger('chosen:updated');*/
            }
            /*$("#filter_in_destid").selectpicker('refresh');
            $("#filter_in_orderwarehouseid").selectpicker('refresh');*/
            /*$('.selectpicker').selectpicker('refresh');*/
            $('.selectpicker').selectpicker('refresh');
        }
    });

}
function refresh() {
    resetData();
}
function resetData() {
    var gridData = $("#searchGrid").data("kendoGrid");
    gridData.dataSource.filter({});
}

function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}
var exportExcelid = "";
function search() {
    var gridData = $("#" + exportExcelid).data("kendoGrid");
    var filters = serializeToFilter($("#searchForm"));
    console.log(filters);
    gridData.dataSource.filter({
        logic: "and",
        filters: filters
    });
    inttitledata();
}
var exportExcelid = "";
function exportExcel() {
    $("#"+exportExcelid).children().find(".k-grid-excel").click();
}

function selectClass1() {
    var rowId = $("#class1Select_Grid").jqGrid("getGridParam", "selrow");
    var rowData = $("#class1Select_Grid").jqGrid('getRowData', rowId);
    $("#filter_eq_class1").val(rowData.code);
    $("#filter_eq_class1Name").val(rowData.name);
    $("#modal_class1_search_tables").modal('hide');
}
var dialogOpenPage;
function openSearchGuestDialog() {
    dialogOpenPage = "saleOrderReturn";
    $("#modal_guest_search_table").modal('show').on('shown.bs.modal', function () {
        initGuestSelect_Grid();
    });
    $("#searchGuestDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='confirm_selected_GuestId_saleReturn()'>确认</button>"
    );
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
                fileName: "采购明细统计.xlsx",
                proxyURL: basePath + "/search/PurchaseorCountviews/export.do",
                allPages:true,
                filterable: true
            },
            excelExport: function (e) {
                var sheet = e.workbook.sheets[0];
                /* var tokenTemplate = kendo.template(this.columns[4].template);
                 var destTemplate = kendo.template(this.columns[6].template);
                 var destUnitTemplate = kendo.template(this.columns[7].template);
                 var origTemplate = kendo.template(this.columns[8].template);
                 var diffTemplate = kendo.template(this.columns[15].template);*/
                var rowIndex = 1;
                var groupNum = this.dataSource._group.length;
                for (var i = 1; i < sheet.rows.length; i++) {
                    var row = sheet.rows[i];
                    if (row.cells[3 + groupNum] && row.cells[5 + groupNum] && row.cells[6 + groupNum] && row.cells[7 + groupNum]) {
                        var gridRow = $("#searchGrid").data("kendoGrid").dataItem("tr:eq(" + rowIndex + ")");
                        /* var dataItem = {
                         token: row.cells[4 + groupNum].value,
                         destid: row.cells[6 + groupNum].value,
                         destUnitId: row.cells[7 + groupNum].value,
                         origid: row.cells[8 + groupNum].value,
                         //destName: gridRow.destName,
                         //destUnitName: gridRow.destUnitName,
                         // origName: gridRow.origName,
                         billNo: gridRow.billNo,
                         qty: gridRow.qty,
                         billQty: gridRow.billQty
                         };*/
                        /* row.cells[4+groupNum].value = tokenTemplate(dataItem);
                         row.cells[6+groupNum].value = destTemplate(dataItem);
                         row.cells[7+groupNum].value = destUnitTemplate(dataItem);
                         row.cells[8+groupNum].value = origTemplate(dataItem);
                         row.cells[15+groupNum].value = diffTemplate(dataItem);*/
                        rowIndex++;
                    }

                }
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
                        url: basePath + "/search/PurchaseorCountviews/list.do",
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
                    {field: "price", aggregate: "average"},
                    {field: "totactprice", aggregate: "sum"},
                    {field: "inQty", aggregate: "sum"},
                    {field: "yetInitQty", aggregate: "sum"},
                    {field: "yetInitPrice", aggregate: "sum"}

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
                pageSizes: [100, 500, 1000, 2000]
            },

            groupable: true,
           /* group: onGrouping,*/
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
                            return "<img width=80 height=100  onclick=showImagesUrl('" +basePath + data.url + "') src='" +basePath + data.url + "' alt='" + data.styleid + "'/>";
                        }
                    }

                },
                {
                    field: "status",
                    title: "单据状态",
                    width: "150px",
                    template:function(data) {
                        var value = "";
                        switch (data.status) {
                            case -1 :
                                value = "撤销";
                                break;
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
                    field: "intimeDate",
                    title: "入库时间",
                    width: "150px"
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
                    field: "billid",
                    width: "250px",
                    aggregates: ["count"],
                    groupHeaderTemplate: function (data) {
                        var totQty = data.aggregates.qty.sum;
                        var value = data.value;
                        var totactprice = data.aggregates.totactprice.sum;
                        return "单号:" + value + " 总数量:" + totQty + "; 总价 :" + kendo.toString(totactprice, '0.00');
                    }
                },
                {
                    field: "taskid",
                    title: "任务号",
                    width: "150px"
                },
                {
                    field: "sku",
                    title: "SKU",
                    width: "150px"
                },
                {
                    field: "destname",
                    title: "出入仓库",
                    width: "180px",
                    /* template:function(data) {
                     if(data.origName){
                     return "["+data.origid+"]"+data.origName;
                     }else{
                     if(data.origid){
                     return "["+data.origid+"]";
                     }else{
                     return "";
                     }
                     }
                     }*/
                },
                {
                    field: "saletype",
                    title: "单据类型",
                    width: "180px",
                    groupHeaderTemplate: function (data) {
                        var totQty = data.aggregates.qty.sum;
                        var value = data.value;
                        var totactprice = data.aggregates.totactprice.sum;
                        if (value == undefined) {
                            value = "";
                        }
                        return "单据类型:" + value + " 总数量:" + totQty + "; 总价 :" + kendo.toString(totactprice, '0.00');
                    }

                },
               {
                    field: "destunitname",
                    title: "供应商",
                    width: "180px",

                },
                {
                    field: "class1Name",
                    title: "厂家",
                    width: "180px",

                },
                {
                    field: "actprice",
                    title: "实际价格",
                    width: "180px",
                },
              /*  {
                    field: "precast",
                    title: "成本价",
                    width: "180px",
                },*/
                /* {
                 field:"a",
                 title:"成本总价",
                 width:"180px",
                 },*/
               /* {
                    field: "gross",
                    title: "销售毛利",
                    width: "180px",
                },*/
               /* {
                    field: "grossprofits",
                    title: "销售毛利率(%)",
                    width: "180px",
                },*/
                {
                    field: "totactprice",
                    title: "实际金额",
                    width: "180px",
                    groupable: false,
                    aggregates: ["sum"],
                    footerTemplate: "#=kendo.toString(sum,'0.00')#"

                },

                {
                    field: "styleid", title: "款号", width: "140px",
                    aggregates: ["count"],
                    groupHeaderTemplate: function (data) {

                        var totQty = data.aggregates.qty.sum;
                        var value = data.value;
                        var avgPrice = data.aggregates.price.average;
                        return "款号:" + value + " 总数量:" + totQty + "; 平均价 :" + kendo.toString(avgPrice, '0.00');
                    }
                },
                {field: "stylename", title: "款名", width: "80px"},
                {field: "sizeid", title: "尺号", width: "80px"},
                {
                    field: "qty", title: "数量", width: "80px", groupable: false,
                    aggregates: ["sum"],
                    footerTemplate: "#=sum#"
                },
                {
                    field: "inQty",
                    title: "入库数量",
                    width: "150px",
                    groupable: false,
                    aggregates: ["sum"],
                    footerTemplate: "#=sum#"
                },
                {
                    field: "yetInitQty",
                    title: "已入库数量",
                    width: "150px",
                    groupable: false,
                    aggregates: ["sum"],
                    footerTemplate: "#=sum#"
                },
                {
                    field: "yetInitPrice",
                    title: "本次入库金额",
                    width: "150px",
                    groupable: false,
                    aggregates: ["sum"],
                    footerTemplate: "#=sum#"
                },
                {
                    field: "orderwarehousename",
                    title: "订货仓库",
                    width: "150px"
                },
                {field: "price", title: "吊牌价", width: "110px", groupable: false, aggregates: ["average"]},
                {
                    field: "outstatus",
                    title: "出库状态",
                    width: "150px",
                    template:function(data) {
                        if (data.outstatus == 0) {
                            return "订单状态";
                        } else if (data.outstatus == 2) {
                            return "已出库";
                        } else if (data.outstatus == 3) {
                            return "出库中";
                        } else {
                            return '';
                        }
                    }
                },
                {
                    field: "instatus",
                    title: "入库状态",
                    width: "150px",
                    template:function(data) {
                        if (data.instatus == 0) {
                            return "订单状态";
                        } else if (data.instatus == 1) {
                            return "已入库";
                        } else if (data.instatus == 4) {
                            return "入库中";
                        } else {
                            return '';
                        }
                    }
                }


            ]

        });
        $(".k-grid-toolbar").hide();
        isoneinitKendoUIGrid = false;
    } else {
        search();
    }


}
var isoneinitKendoUIPurchaseGrid = true;
function initKendoUIPurchaseGrid() {
    exportExcelid = "searchpuchaseGrid";
    if (isoneinitKendoUIPurchaseGrid) {
        var filters = serializeToFilter($("#searchForm"));
        console.log(filters);
        $("#searchpuchaseGrid").kendoGrid({
            toolbar: ["excel"],
            excel: {
                fileName: "采购明细统计.xlsx",
                proxyURL: basePath + "/search/PurchaseorCountviews/export.do",
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
                        url: basePath + "/search/PurchaseorCountviews/listpurchase.do",
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

                    {field: "totqty", aggregate: "sum"},
                    /*{field: "price", aggregate: "average"},*/
                    {field: "totinval", aggregate: "sum"},

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
                pageSizes: [100, 500, 1000, 2000]
            },

            groupable: true,
            /* group: onGrouping,*/
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

                        var totQty = data.aggregates.totqty.sum;
                        var value = kendo.toString(data.value, 'yyyy-MM-dd HH:mm:ss');
                        var totactprice = data.aggregates.totinval.sum;
                        return "日期:" + value + " 总数量:" + totQty + "; 总价 :" + kendo.toString(totactprice, '0.00');
                    }
                },
                {
                    field: "intimeDate",
                    title: "入库时间",
                    width: "180px",

                },
                {
                    title: "单号",
                    field: "billno",
                    width: "250px",
                    aggregates: ["count"],
                    groupHeaderTemplate: function (data) {
                        var totQty = data.aggregates.totqty.sum;
                        var value = data.value;
                        var totactprice = data.aggregates.totinval.sum;
                        return "单号:" + value + " 总数量:" + totQty + "; 总价 :" + kendo.toString(totactprice, '0.00');
                    }
                },
                {
                    field: "taskid",
                    title: "任务号",
                    width: "180px",

                },
                /* {
                 field: "sku",
                 title: "SKU",
                 width: "150px"
                 },*/
                {
                    field: "destname",
                    title: "出入仓库",
                    width: "180px",
                    /* template:function(data) {
                     if(data.origName){
                     return "["+data.origid+"]"+data.origName;
                     }else{
                     if(data.origid){
                     return "["+data.origid+"]";
                     }else{
                     return "";
                     }
                     }
                     }*/
                },
                {
                    field: "saletype",
                    title: "单据类型",
                    width: "180px",
                    groupHeaderTemplate: function (data) {
                        var totQty = data.aggregates.totqty.sum;
                        var value = data.value;
                        var totactprice = data.aggregates.totinval.sum;
                        if (value == undefined) {
                            value = "";
                        }
                        return "单据类型:" + value + " 总数量:" + totQty + "; 总价 :" + kendo.toString(totactprice, '0.00');
                    }

                },
                /*{
                    field: "destunitname",
                    title: "供应商",
                    width: "180px",

                },*/
                /* {
                 field: "actprice",
                 title: "实际价格",
                 width: "180px",
                 },*/
                /*  {
                 field: "precast",
                 title: "成本价",
                 width: "180px",
                 },*/
                /* {
                 field:"a",
                 title:"成本总价",
                 width:"180px",
                 },*/
                /* {
                 field: "gross",
                 title: "销售毛利",
                 width: "180px",
                 },*/
                /* {
                 field: "grossprofits",
                 title: "销售毛利率(%)",
                 width: "180px",
                 },*/
                {
                    field: "totinval",
                    title: "金额",
                    width: "180px",
                    groupable: false,
                    aggregates: ["sum"],
                    footerTemplate: "#=kendo.toString(sum,'0.00')#"

                },
                {field: "totinqty", title: "出入库数量", width: "80px"},
                {
                    field: "totqty", title: "数量", width: "80px", groupable: false,
                    aggregates: ["sum"],
                    footerTemplate: "#=sum#"
                },
                {
                    field: "inqty",
                    title: "本次入库数量",
                    width: "180px",

                },
                {
                    field: "orderwarehousename",
                    title: "订货仓库",
                    width: "180px",

                },
                /*{field: "totinval", title: "吊牌价", width: "110px", groupable: false, aggregates: ["average"]},*/
                {
                    field: "remark",
                    title: "备注",
                    width: "180px",
                },


            ]

        });
        $(".k-grid-toolbar").hide();
        isoneinitKendoUIPurchaseGrid = false;
    } else {
        search();
    }


}
var isoneinitKendoUIPurchasestyeidGrid = true;
function initKendoUIPurchasestyeidGrid() {
    exportExcelid = "searchpuchaseBystyeidGrid";
    if (isoneinitKendoUIPurchasestyeidGrid) {
        var filters = serializeToFilter($("#searchForm"));
        console.log(filters);
        $("#searchpuchaseBystyeidGrid").kendoGrid({
            toolbar: ["excel"],
            excel: {
                fileName: "采购明细统计.xlsx",
                proxyURL: basePath + "/search/PurchaseorCountviews/export.do",
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
                        url: basePath + "/search/PurchaseorCountviews/readpurchaseBybystyleid.do",
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

                    {field: "totqty", aggregate: "sum"},
                    /*{field: "price", aggregate: "average"},*/
                    {field: "totinval", aggregate: "sum"},

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
                pageSizes: [100, 500, 1000, 2000]
            },

            groupable: true,
            /* group: onGrouping,*/
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
                 field: "sku",
                 title: "SKU",
                 width: "150px"
                 },
                {
                    field: "stylename",
                    title: "商品名称",
                    width: "180px",

                },
                {
                    field: "qty",
                    title: "数量",
                    width: "180px",

                },
                {
                    field: "destunitname",
                    title: "厂家",
                    width: "180px",

                },
                {
                    field: "totactprice",
                    title: "金额",
                    width: "180px",
                },


            ]

        });
        $(".k-grid-toolbar").hide();
        isoneinitKendoUIPurchasestyeidGrid = false;
    } else {
        search();
    }


}
var isoneinitKendoUIPurchasedestunitidGrid = true;
function initKendoUIPurchasedestunitidGrid() {

    exportExcelid = "searchpuchaseBydestunitidGrid";
    if (isoneinitKendoUIPurchasedestunitidGrid) {
        var filters = serializeToFilter($("#searchForm"));
        console.log(filters);
        $("#searchpuchaseBydestunitidGrid").kendoGrid({
            toolbar: ["excel"],
            excel: {
                fileName: "采购明细统计.xlsx",
                proxyURL: basePath + "/search/PurchaseorCountviews/export.do",
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
                        url: basePath + "/search/PurchaseorCountviews/readpurchaseBydestunitid.do",
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

                    {field: "totqty", aggregate: "sum"},
                    /*{field: "price", aggregate: "average"},*/
                    {field: "totinval", aggregate: "sum"},

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
                pageSizes: [100, 500, 1000, 2000]
            },

            groupable: true,
            /* group: onGrouping,*/
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
                    field: "destunitname",
                    title: "供应商",
                    width: "180px",

                },
                {
                    field: "qty",
                    title: "数量",
                    width: "180px",

                },
                {
                    field: "totactprice",
                    title: "金额",
                    width: "180px",
                    template: function (data) {

                        var value = data.totactprice;
                        console.log("start"+value);
                        value=parseFloat(value).toFixed(2);
                        console.log("end"+value);

                        return value;
                    }
                },


            ]

        });
        $(".k-grid-toolbar").hide();
        isoneinitKendoUIPurchasedestunitidGrid = false;
    } else {
        search();
    }



}
function newchooseExportFunction() {
    if(exportExcelid === "searchpuchaseGrid"){
        exportExcelPOI();
    }else if(exportExcelid === "searchGrid"){
        exportExcelImagePOI();
    } else {
        exportExcelProPOI();
    }
}
function exportExcelImagePOI() {
    var filters = serializeToFilter($("#searchForm"));
    var gridData = $("#" + exportExcelid).data("kendoGrid");
    console.log(gridData);
    var total = gridData.dataSource._total;
    var request = {};
    request.page = gridData.dataSource._page;
    request.pageSize = gridData.dataSource._pageSize;
    request.take = gridData.dataSource._pageSize;
    request.sort =gridData.dataSource._sort;
    request.skip = gridData.dataSource._skip;
    request.filter = {
        logic: "and",
        filters : filters
    };
    var url=basePath+"/search/PurchaseorCountviews/exportnew.do";
    $("#form1").attr("action",url);
    $("#gridId").val(exportExcelid);
    $("#request").val(JSON.stringify(request));
    $("#form1").submit();
}
function exportExcelProPOI() {
    var filters = serializeToFilter($("#searchForm"));
    var gridData = $("#" + exportExcelid).data("kendoGrid");
    var total = gridData.dataSource._total;
    var request = {};
    request.page = 1;
    request.pageSize = total;
    request.take = total;
    request.skip = 0;
    request.filter = {
        logic: "and",
        filters : filters
    };
    var url=basePath+"/search/PurchaseorCountviews/exportnew.do";
    $("#form1").attr("action",url);
    $("#gridId").val(exportExcelid);
    $("#request").val(JSON.stringify(request));
    $("#form1").submit();
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
    var url=basePath+"/search/PurchaseorCountviews/exportnew.do";
    $("#form1").attr("action",url);
    $("#gridId").val(exportExcelid);
    $("#request").val(JSON.stringify(request));
    $("#form1").submit();
}

var dialogOpenPage;
function openSearchVendorDialog() {
    dialogOpenPage = "purchaseOrderCount";
    $("#modal_vendor_search_table").modal('show').on('shown.bs.modal', function () {
        initVendorSelect_Grid();
    });
    $("#searchVendorDialog_buttonGroup").html("" +
        "<button type='button'  class='btn btn-primary' onclick='selected_VendorId_purchaseOrder()'>确认</button>"
    );
}
function selected_VendorId_purchaseOrder() {
    var rowId = $("#vendorSelect_Grid").jqGrid("getGridParam", "selrow");
    var rowData = $("#vendorSelect_Grid").jqGrid('getRowData', rowId);
    $("#filter_eq_destunitid").val(rowData.id);
    $("#filter_eq_destunitname").val(rowData.name);
    $("#modal_vendor_search_table").modal('hide');
}
function hideImage() {
    $("#divshowImage").hide();

}
function showImagesUrl(url) {
    console.log(url);
    var Url="";
    var urlArray=url.split("_");
    var urlArrays=urlArray[1].split(".");
    Url=urlArray[0]+"."+urlArrays[1];
    $("#showImage").attr("src",Url);
    $("#divshowImage").show();

}