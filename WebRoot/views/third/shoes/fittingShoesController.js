$(function () {
    kendo.culture("zh-CN");
    initKendoUIGrid();
    initECharts();
    $(".k-dropdown").css("width", "6em");
    initDialog();
    initWidget();
});
function initWidget() {
    window.setInterval(search, 10*1000);
    /*$("#datepicker").kendoDatePicker({
        format: "yyyy-MM-dd"

    });
    $("#searchType").kendoComboBox({
        dataTextField: "text",
        dataValueField: "value",
        index:0,
        dataSource: [
            { text: "试鞋", value:1 },
            { text: "提鞋", value: 2}
        ],
        filter: "contains",
        suggest: true
     }).data("kendoComboBox").input[0].name = "";*/
}
//设置图表
function initECharts() {
    var chartc3 = echarts.init(document.getElementById('class3'), 'shine');
    //初始化
    var option = {
        title: {
            text: '款式统计',
            x: 'center'
        },
        tooltip: {
            trigger: 'item',
            formatter: "{c}({d}%)"
        },
        series: [
            {
                name: '试鞋次数',
                type: 'pie',
                radius: '80%',
                center: ['50%', '50%'],
                data: [],
                label: {
                    normal: {
                        position: 'inner'
                    }
                },
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
    chartc3.setOption(option);
    var chartc10 = echarts.init(document.getElementById('class10'), 'shine');
    //初始化
    var option = {
        title: {
            text: '颜色统计',
            x: 'center'
        },
        tooltip: {
            trigger: 'item',
            formatter: "{c}({d}%)"
        },
        series: [
            {
                name: '试鞋次数',
                type: 'pie',
                radius: '80%',
                center: ['50%', '50%'],
                label: {
                    normal: {
                        position: 'inner'
                    }
                },
                data: [],
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
    chartc10.setOption(option);
    //打开进度条
    var progressDialog = bootbox.dialog({
        message: '<p><i class="fa fa-spin fa-spinner"></i> 图表生成中...</p>'
        //设置图表数据
    });
    $.ajax({
        url: basePath + "/api/hub/yunbai/test/countFittingShoes.do",
        type: 'post',
        dataType: 'json',
        error:function(e){
            progressDialog.modal('hide');
        },
        success: function (result) {
            if (result.success == true || result.success == 'true') {
                //大类
                chartc3.setOption({
                    series: [{
                        name: '时间',
                        data: result.result.countStyles
                    }]
                })
                //季节
                chartc10.setOption({
                    series: [{
                        name: '时间',
                        data: result.result.countColors
                    }]
                })
                //一年试衣总数
                var sumYear = $("#sumYear");
                html = "";
                html += " <div class='infobox-content' >本年试鞋</div>"
                html += " <div class='infobox-content'>" + result.result.sumYear[0].value + " 次</div>";
                sumYear.html(html);
                //一个月试衣总数
                var sumMonth = $("#sumMonth");
                html = "";
                html += " <div class='infobox-content' >本月试鞋</div>"
                html += " <div class='infobox-content'>" + result.result.sumMonth[0].value + " 次</div>";
                sumMonth.html(html);
                //一周试衣总数
                var sumWeek = $("#sumWeek");
                html = "";
                html += " <div class='infobox-content' >本周试鞋</div>"
                html += " <div class='infobox-content'>" + result.result.sumWeek[0].value + " 次</div>";
                sumWeek.html(html);
                //一天试衣总数
                var sumDay = $("#sumDay");
                html = "";
                html += " <div class='infobox-content' >本日试鞋</div>"
                html += " <div class='infobox-content'>" + result.result.sumDay[0].value + " 次</div>";
                sumDay.html(html);
                //进度条关闭
                progressDialog.modal('hide');
            } else {
                progressDialog.modal('hide');
            }
        }
    });
    freshBar();
}

function freshBar() {
    /**初始化柱状图*/
    var chartStyle = echarts.init(document.getElementById('styleBar'), 'shine');

    var optionStyle = {
        title: {
            text: '款式分析图',
            x: 'left',
            //垂直安放位置，默认为全图顶端，可选为：'top' | 'bottom' | 'center' | {number}（y坐标，单位px）
            y: 'top'
        },
        tooltip: {},
        legend : {
            data:['提鞋','试鞋']
        },
        xAxis : [ {
            type : 'category',
            data : []
        } ],
        yAxis : [ {
            show: true,
            //坐标轴类型，纵轴默认为数值型'value'
            type: 'value',
            //分隔区域，默认不显示
            splitArea: {show: true},
            type : 'value'
        } ],
        series : []
    };
    chartStyle.setOption(optionStyle);
    var chartColor = echarts.init(document.getElementById('colorBar'), 'shine');

    var optionColor = {
        title: {
            text: '颜色分析图',
            x: 'left',
            //垂直安放位置，默认为全图顶端，可选为：'top' | 'bottom' | 'center' | {number}（y坐标，单位px）
            y: 'top'
        },
        tooltip: {},
        legend : {
            data:['提鞋','试鞋']
        },
        xAxis : [ {
            type : 'category',
            data : []
        } ],
        yAxis : [ {
            show: true,
            //坐标轴类型，纵轴默认为数值型'value'
            type: 'value',
            //分隔区域，默认不显示
            splitArea: {show: true}
        } ],
        series : []
    };
    chartColor.setOption(optionColor);

    $.ajax({
        url: basePath + "/api/hub/yunbai/test/countBar.do",
        type: 'post',
        dataType: 'json',
        error:function(e){

        },
        success: function (result) {
            if (result.success == true || result.success == 'true') {
                chartStyle.setOption({
                    xAxis : [ {
                        type : 'category',
                        data : result.result.styleAxisDate
                    } ],
                    series:result.result.styleSeries
                });
                chartColor.setOption({
                    xAxis : [ {
                        type : 'category',
                        data : result.result.colorAxisDate
                    } ],
                    series:result.result.colorSeries
                });
            }
        }
    });
}

function refresh() {
    resetData();
}
function resetData() {
    var gridData = $("#fittingGrid").data("kendoGrid");
    gridData.dataSource.filter({});
    initECharts();
}
function exportExcel() {
    $(".k-grid-excel").click();
}
function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}
function showEChartsPanel() {
    $("#EchartArea").slideToggle("fast");
}
function search() {
    var gridData = $("#fittingGrid").data("kendoGrid");
    var filters = serializeToFilter($("#searchForm"));
    console.log(filters);
    gridData.dataSource.filter({
        logic: "and",
        filters: filters
    });
}
function initKendoUIGrid() {

    $("#fittingGrid").kendoGrid({
        toolbar: ["excel"],
        excel: {
            allPages: true
        },
        dataSource: {
            schema: {
                total: "total",
                model: {
                    fields: {
                        styleId: {type: "string"},
                        styleName: {type: "string"},
                        colorId: {type: "string"},
                        colorName: {type: "string"},
                        type: {type: "number"},
                        fittingQty:{type:"number"},
                        pickingQty:{type:"number"}
                    }
                },
                data: "data"
            },
            sort: [{field: "styleId", dir: "desc"}, {field: "colorId", dir: "desc"}],
            transport: {
                read: {
                    url: basePath + "/api/hub/yunbai/test/listFittingPicking.do",
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
        sortable: {
            mode: "multiple",
            allowUnsort: true
        },
        pageable: {
            input: true,
            buttonCount: 5,
            pageSize: 100.0,
            pageSizes: [100, 150, 200]
        },

        groupable: false,
        columnMenu: true,
        filterable:false,
        reorderable: true,
        resizable: true,
        scrollable: true,
        toolbar: "<p><form id='searchAggForm' class='form-inline'>" +
        " <div class='form-group'>" +
        "<label for='form_styleIdd'>款号</label>" +
        "<input class='form-control'id='form_styleIdd'type='text'name='filter_CONTAINS_styleId'style='max-width: 150px'placeholder='款号'/>" +
        "</div>" +
        " <div class='form-group'>" +
        "<label for='form_sized'>色码</label>" +
        "<input class='form-control'id='form_sized'type='text' name='filter_CONTAINS_colorId' style='max-width: 150px' placeholder='色码'/>" +
        "</div>" +

        "<button  class='btn k-primary'id='btn_findProduct' type='button' style='min-width: 60px' onclick='searchSearcherBill()'>查询</button>" +

        "</form>" +
        "</p>",
        columns: [
            {field: "styleId", title: "款号", width: "190px",locked: true,lockable: false,},
            {field: "colorId", title: "色号", width: "190px",locked: true,lockable: false,},
            {field: "styleName", title: "款名", width: "250px"},
            {field: "colorName", title: "颜色", width: "190px"},
            {field: "fittingQty", title: "试鞋次数", width: "120px"},
            {field: "pickingQty", title: "提鞋次数", width: "120px"}
        ]
    });
 /*   $("#aggGrid").kendoGrid({
        toolbar: ["excel"],
        excel: {
            allPages: true
        },
        dataSource: {
            schema: {
                total: "total",
                model: {
                    fields: {
                        fittingDate: {type: "String"},
                        styleId: {type: "string"},
                        styleName: {type: "string"},
                        colorId: {type: "string"},
                        colorName: {type: "string"},
                        type: {type: "number"},
                        qty:{type:"number"}
                    }
                },
                data: "data",
                groups: "data"
            },
            sort: [{field: "fittingDate", dir: "desc"}, {field: "styleId", dir: "desc"}, {field: "colorId", dir: "desc"}],
            transport: {
                read: {
                    url: basePath + "/api/hub/yunbai/test/listAgg.do",
                    type: "POST",
                    dataType: "json",
                    contentType: 'application/json'
                },
                parameterMap: function (options) {
                    return JSON.stringify(options);
                }
            },
            group:[{
                field: "fittingDate",
                aggregates: [
                    { field: "qty", aggregate: "sum" },
                    { field: "styleId", aggregate: "count" },
                    { field: "colorId", aggregate: "count" }

                ]}

            ],
            pageSize: 100.0,
            serverSorting: true,
            serverPaging: true,
            serverGrouping: false,
            serverFiltering: true
        },
        sortable: {
            mode: "multiple",
            allowUnsort: true
        },
        pageable: {
            input: true,
            buttonCount: 5,
            pageSize: 100.0,
            pageSizes: [100, 150, 200]
        },

        groupable:true,
        columnMenu: true,
        filterable:false,
        reorderable: true,
        resizable: true,
        scrollable: true,
        toolbar: "<p><form id='searchAggForm' class='form-inline'>" +
        " <div class='form-group'>" +
        "<label for='searchType'>类型</label>" +
        "<input class='form-control'id='searchType'type='text'name='filter_eq_type' style='max-width: 120px'/>" +
        "</div>" +
            " <div class='form-group' >" +
            "<label for='datepicker'>日期</label>" +
            "<input class='form-control date-picker'  data-date-format='yyyy-mm-dd' style='max-width: 150px'id='datepicker' name='filter_CONTAINS_fittingDate'type='text'/>" +
            "</div>" +
            " <div class='form-group'>" +
            "<label for='form_styleIdd'>款号</label>" +
            "<input class='form-control'id='form_styleIdd'type='text'name='filter_CONTAINS_styleId'style='max-width: 150px'placeholder='款号'/>" +
            "</div>" +
            " <div class='form-group'>" +
            "<label for='form_sized'>色码</label>" +
            "<input class='form-control'id='form_sized'type='text' name='filter_CONTAINS_colorId' style='max-width: 150px' placeholder='色码'/>" +
            "</div>" +

            "<button  class='btn k-primary'id='btn_findProduct' type='button' style='min-width: 60px' onclick='searchSearcherBill()'>查询</button>" +

            "</form>" +
            "</p>",
        columns: [
            {field: "fittingDate", title: "日期", width: "120px",locked: true,lockable: false,
                groupHeaderTemplate: function (data) {
                    var sumstyleId=data.aggregates.styleId.count;
                    var sumcolorId=data.aggregates.colorId.count;
                    var sumqty=data.aggregates.qty.sum;

                    if(sumstyleId==null){
                        sumstyleId=0;
                    }
                    if(sumcolorId==null){
                        sumcolorId=0;
                    }
                    if(sumqty==null){
                        sumqty=0;
                    }
                    return "日期:"+data.value+" 总款数:"+sumstyleId+" 颜色总数:"+sumcolorId+"活跃次数："+sumqty;
                }},
            {field: "type", title: "类型", width: "80px",
                template: function (data) {
                    if (data.type == 2) {
                        return "提鞋";
                    } else {
                        return "试鞋";
                    }
                }
            },
            {field: "qty", title: "次数", width: "80px",groupable:false},
            {field: "styleId", title: "款号", width: "180px",locked: true,lockable: false},
            {field: "colorId", title: "色号", width: "100px",locked: true,lockable: false},
            {field: "styleName", title: "款名", width: "180px"},
            {field: "colorName", title: "颜色", width: "120px"}


        ]
    });*/
}

function clearData() {
    $("#alertDialog").data("kendoDialog").open();
}
function searchSearcherBill(){
    var gridData = $("#fittingGrid").data("kendoGrid");
    var filters = serializeToFilter($("#searchAggForm"));
    console.log(filters);
    gridData.dataSource.filter({
        logic: "and",
        filters: filters
    });

}
function initDialog() {
    $("#alertDialog").kendoDialog({
        width: "400px",
        height: "250px",
        title: "提示",
        closable: false,
        modal: true,
        buttonLayout: "normal",
        content:"<center><h3>删除后将无法恢复!</h3></center>",
        actions: [
            {
                text: '<span class="glyphicon glyphicon-ok">确定</span>',
                action:deleteAllFitting,
                primary: true
            },
            {text: '<span class="glyphicon glyphicon-remove">取消</span>'}
        ]
    }).data("kendoDialog").close();

    $("#progressDialog").kendoDialog({
        width: "400px",
        height: "250px",
        title: "提示",
        closable: false,
        animation: true,
        modal: true,
        content: '<center><h3>正在处理中...</h3></center>' +
        '<div class="progress"><div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="45" aria-valuemin="0" aria-valuemax="100" style="width: 45%">' +
        '<span class="sr-only">100%</span></div></div>',
        buttonLayout: "normal"
    }).data("kendoDialog").close();
    $("#notification").kendoNotification({
        position: {
            top: 50
        },
        stacking: "left"
    }).data("kendoNotification").hide();
}
function openProgress() {
    $("#progressDialog").data('kendoDialog').open();
}
function closeProgress() {
    $("#progressDialog").data('kendoDialog').close();
}
function deleteAllFitting(e) {
    openProgress();
    $.ajax({
        url: basePath + "/api/hub/yunbai/test/deleteAll.do",
        type: 'post',
        dataType: 'json',
        error:function (e) {
          closeProgress();
            $("#notification").data('kendoNotification').showText('删除失败！', 'error');
        },
        success: function (result) {
            if (result.success == true || result.success == 'true') {
                $("#notification").data('kendoNotification').showText('删除成功！', 'success');
            } else {
                $("#notification").data('kendoNotification').showText('删除失败！', 'error');
            }
            closeProgress();
        }
    });

}