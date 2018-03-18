$(function () {

    initMutiSelect();
    initKendoUIGrid();
    initECharts();
    $(".k-dropdown").css("width", "6em");
    $(".k-grid-toolbar").css("display", "none");//隐藏toolbar

});
//设置图表
function initECharts() {
    var chartc3 = echarts.init(document.getElementById('class3'), 'shine');
    //初始化
    var option = {
        title: {
            text: '试衣大类统计',
            x: 'center'
        },
        tooltip: {
            trigger: 'item',
            formatter: "{c}({d}%)"
        },
        series: [
            {
                name: '试衣次数',
                type: 'pie',
                radius: '55%',
                center: ['50%', '50%'],
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
    chartc3.setOption(option);
    var chartc10 = echarts.init(document.getElementById('class10'), 'shine');
    //初始化
    var option = {
        title: {
            text: '试衣季节统计',
            x: 'center'
        },
        tooltip: {
            trigger: 'item',
            formatter: "{c}({d}%)"
        },
        series: [
            {
                name: '试衣次数',
                type: 'pie',
                radius: '55%',
                center: ['50%', '50%'],
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
        url: basePath + "/shop/fittingSearch/getEChartData.do",
        type: 'post',
        dataType: 'json',
        success: function (result) {
            if (result.success == true || result.success == 'true') {
                //大类
                chartc3.setOption({
                    series: [{
                        name: '时间',
                        data: result.result.countc3
                    }]
                })
                //季节
                chartc10.setOption({
                    series: [{
                        name: '时间',
                        data: result.result.countc10
                    }]
                })
                //门店排行
                var sortShop = $("#sortShop");
                var html="";
                html="<ul class='list-unstyled spaced2'>"
                for(var i=0;i<result.result.sortShop.length;i++){
                    if(i==0){
                        html+="<li class='sortFirst'><b>NO."+(i+1)+" </b>"+result.result.sortShop[i][0]+": <b>"+result.result.sortShop[i][1]+"</b></li>";
                    }else if(i==1){
                        html+="<li class='sortSecond'><b>NO."+(i+1)+" </b>"+result.result.sortShop[i][0]+": <b>"+result.result.sortShop[i][1]+"</b></li>";
                    }else{
                        html+="<li><b>NO."+(i+1)+" </b>"+result.result.sortShop[i][0]+": <b>"+result.result.sortShop[i][1]+"</b></li>";
                    }
                }
                html+="</ul>";
                sortShop.html(html);
                //款式排行
                var sortStyle = $("#sortStyle");
                html="<ul class='list-unstyled spaced2'>"
                for(var i=0;i<result.result.sortStyle.length;i++){
                    if(i==0){
                        html+="<li class='sortFirst'><b>NO."+(i+1)+" </b>"+result.result.sortStyle[i].name+": <b>"+result.result.sortStyle[i].value+"</b></li>";
                    }else if(i==1){
                        html+="<li class='sortSecond'><b>NO."+(i+1)+" </b>"+result.result.sortStyle[i].name+": <b>"+result.result.sortStyle[i].value+"</b></li>";
                    }else{
                        html+="<li><b>NO."+(i+1)+" </b>"+result.result.sortStyle[i].name+": <b>"+result.result.sortStyle[i].value+"</b></li>";
                    }

                }
                html+="</ul>";
                sortStyle.html(html);
                //颜色排行
                var sortColor = $("#sortColor");
                html="<ul class='list-unstyled spaced2'>"
                for(var i=0;i<result.result.sortColor.length;i++){
                    if(i==0){
                        html+="<li class='sortFirst'><b>NO."+(i+1)+" </b>"+result.result.sortColor[i].name+": <b>"+result.result.sortColor[i].value+"</b></li>";
                    }else if(i==1){
                        html+="<li class='sortSecond'><b>NO."+(i+1)+" </b>"+result.result.sortColor[i].name+": <b>"+result.result.sortColor[i].value+"</b></li>";
                    }else{
                        html+="<li><b>NO."+(i+1)+" </b>"+result.result.sortColor[i].name+": <b>"+result.result.sortColor[i].value+"</b></li>";
                    }

                }
                html+="</ul>";
                sortColor.html(html);
                //一年试衣总数
                var sumYear=$("#sumYear");
                html="";
                html+=" <div class='infobox-content' >本年试衣</div>"
                html+=" <div class='infobox-content'>"+result.result.sumYear[0].value+" 次</div>";
                sumYear.html(html);
                //一个月试衣总数
                var sumMonth=$("#sumMonth");
                html="";
                html+=" <div class='infobox-content' >本月试衣</div>"
                html+=" <div class='infobox-content'>"+result.result.sumMonth[0].value+" 次</div>";
                sumMonth.html(html);
                //一周试衣总数
                var sumWeek=$("#sumWeek");
                html="";
                html+=" <div class='infobox-content' >本周试衣</div>"
                html+=" <div class='infobox-content'>"+result.result.sumWeek[0].value+" 次</div>";
                sumWeek.html(html);
                //一天试衣总数
                var sumDay=$("#sumDay");
                html="";
                html+=" <div class='infobox-content' >本日试衣</div>"
                html+=" <div class='infobox-content'>"+result.result.sumDay[0].value+" 次</div>";
                sumDay.html(html);
                //进度条关闭
                progressDialog.modal('hide');
            }else{
                progressDialog.modal('hide');
            }
        }
    })

}
function refresh() {
    resetData();
}
function resetData() {
    var gridData = $("#fittingGrid").data("kendoGrid");
    gridData.dataSource.filter({});
}
function exportExcel() {
    $(".k-grid-excel").click();
}
function initMutiSelect() {
    $("#filter_in_class2").kendoMultiSelect({
        dataTextField: "name",
        dataValueField: "code",
        height: 400,
        suggest: true,
        dataSource: {
            transport: {
                read: basePath + "/sys/property/searchByType.do?type=C2"
            }
        }
    })
    $("#filter_in_class3").kendoMultiSelect({
        dataTextField: "name",
        dataValueField: "code",
        height: 400,
        suggest: true,
        dataSource: {
            transport: {
                read: basePath + "/sys/property/searchByType.do?type=C3"
            }
        }
    })
    $("#filter_in_class4").kendoMultiSelect({
        dataTextField: "name",
        dataValueField: "code",
        height: 400,
        suggest: true,
        dataSource: {
            transport: {
                read: basePath + "/sys/property/searchByType.do?type=C4"
            }
        }
    })
    $("#filter_in_class10").kendoMultiSelect({
        dataTextField: "name",
        dataValueField: "code",
        height: 400,
        suggest: true,
        dataSource: {
            transport: {
                read: basePath + "/sys/property/searchByType.do?type=C10"
            }
        }
    })
    $("#filter_contains_shop").kendoMultiSelect({
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
                        fitTime: {type: "string"},
                        shop: {type: "string"},
                        sku: {type: "string"},
                        style: {type: "string"},
                        color: {type: "string"},
                        sizeName: {type: "string"},
                        brand: {type: "string"},
                        class3: {type: "string"},
                        class4: {type: "string"},
                        class10: {type: "string"},
                        price: {type: "float"},
                        fT: {type: "number"}
                    }
                },
                data: "data",
                group: [
                    {field: "fitTime"},
                    {field: "shop"},
                    {field: "style"},
                    {field: "color"},
                    {field: "brand"},
                    {field: "class3"},
                    {field: "class4"},
                    {field: "class10"},
                ]
            },
            sort: [{field: "shop", dir: "desc"}],
            transport: {
                read: {
                    url: basePath + "/shop/fittingSearch/list.do",
                    type: "POST",
                    dataType: "json",
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
                {field: "fT", aggregate: "sum"},
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
        columnMenu: true,
        filterable: {
            extra: false
        },
        selectable: "multiple row",
        reorderable: true,
        resizable: true,
        scrollable: true,
        columns: [
            {
                field: "fitTime", title: "时间", width: "130px", aggregates: ["count"],
                groupHeaderTemplate: function (data) {
                    var totft = data.aggregates.fT.sum;
                    return "日期:" + data.value + ";试衣次数:" + totft;
                }
            },
            {field: "shop", title: "店铺", width: "180px"},
            {field: "sku", title: "SKU", width: "150px"},
            {field: "style", title: "款式", width: "180px"},
            {field: "color", title: "颜色", width: "100px"},
            {field: "sizeName", title: "尺码", width: "140px"},
            {field: "brand", title: "品牌", width: "120px"},
            {field: "class3", title: "大类", width: "120px"},
            {field: "class4", title: "小类", width: "120px"},
            {field: "class10", title: "季节", width: "80px"},
            {field: "price", title: "价格", width: "100px", groupable: false},
            {field: "fT", title: "试衣次数", width: "100px", groupable: false, aggregates: ["sum"]}
        ]
    });

}