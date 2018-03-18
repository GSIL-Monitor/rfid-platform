
$(function(){
    initProdQtyCharts();
    initProdWeekChart();
    initProdMonthChart();
    initShopProdChart();
});

//region
function initProdQtyCharts() {
    var chartc3 = echarts.init(document.getElementById('prodQtyChart'), 'shine');
    //初始化
    var option = {
        // title : {
        //     text: '某站点用户访问来源',
        //     subtext: '纯属虚构',
        //     x:'center'
        // },
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data: ['商品1','商品2','商品3','商品4','其他']
        },
        series : [
            {
                name: '访问来源',
                type: 'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {value:335, name:'商品1'},
                    {value:310, name:'商品2'},
                    {value:234, name:'商品3'},
                    {value:135, name:'商品4'},
                    {value:1548, name:'其他'}
                ],
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

}
//endregion
//region
function initProdWeekChart() {
    var chartc3 = echarts.init(document.getElementById('prodWeekChart'), 'shine');
    var option = {
    //    backgroundColor: "#a73e5c",
    //    color: ['#ffd285', '#ff733f', '#ec4863'],

        // title: [{
        //     // text: '河南省主要城市空气质量指数',
        //     left: '1%',
        //     top: '6%',
        //     textStyle: {
        //         // color: '#ffd285'
        //     }
        // }, {
        //     text: '污染占比分析',
        //     left: '83%',
        //     top: '6%',
        //     textAlign: 'center',
        //     textStyle: {
        //         // color: '#ffd285'
        //     }
        // }],
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            x: 300,
            top: '7%',
            textStyle: {
                // color: '#ffd285',
            },
            data: ['商品1', '商品2', '商品3']
        },
        grid: {
            left: '1%',
            right: '35%',
            top: '16%',
            bottom: '6%',
            containLabel: true
        },
        toolbox: {
            "show": false,
            feature: {
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            "axisLine": {
                lineStyle: {
                    // color: '#c0576d'
                }
            },
            "axisTick": {
                "show": false
            },
            axisLabel: {
                textStyle: {
                    // color: '#ffd285'
                }
            },
            boundaryGap: false,
            data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
        },
        yAxis: {
            "axisLine": {
                lineStyle: {
                     color: '#c0576d'
                }
            },
            splitLine: {
                show: true,
                lineStyle: {
                     color: '#c0576d'
                }
            },
            "axisTick": {
                "show": false
            },
            axisLabel: {
                textStyle: {
                     color: '#0000ff'
                }
            },
            type: 'value'
        },
        series: [
            {
            name: '商品1',
            smooth: true,
            type: 'line',
            symbolSize: 8,
            symbol: 'circle',
            data: [90, 50, 39, 50, 120, 82, 80]
        }, {
            name: '商品2',
            smooth: true,
            type: 'line',
            symbolSize: 8,
            symbol: 'circle',
            data: [70, 50, 50, 87, 90, 80, 70]
        }, {
            name: '商品3',
            smooth: true,
            type: 'line',
            symbolSize: 8,
            symbol: 'circle',
            data: [290, 200,20, 132, 15, 200, 90]
        }]
    };
    chartc3.setOption(option);
}
//region
function initProdMonthChart() {
    var chartc3 = echarts.init(document.getElementById('prodMonthChart'), 'shine');
    var xData = function() {
        var data = [];
        for (var i = 1; i < 13; i++) {
            data.push(i + "月份");
        }
        return data;
    }();

    option = {
        // backgroundColor: "#344b58",
        // "title": {
        //     "text": "本年商场顾客男女人数统计",
        //     "subtext": "BY Wang Dingding",
        //     x: "4%",
        //
        //     textStyle: {
        //         color: '#fff',
        //         fontSize: '22'
        //     },
        //     subtextStyle: {
        //         color: '#90979c',
        //         fontSize: '16',
        //
        //     },
        // },
        "tooltip": {
            "trigger": "axis",
            "axisPointer": {
                "type": "shadow",
                textStyle: {
                   // color: "#fff"
                }

            },
        },
        "grid": {
            "borderWidth": 0,
            "top": 110,
            "bottom": 95,
            textStyle: {
               // color: "#fff"
            }
        },
        "legend": {
            x: '4%',
            top: '11%',
            textStyle: {
               // color: '#90979c',
            },
            "data": ['商品1', '商品2', '平均']
        },


        "calculable": true,
        "xAxis": [{
            "type": "category",
            "axisLine": {
                lineStyle: {
                    color: '#90979c'
                }
            },
            "splitLine": {
                "show": false
            },
            "axisTick": {
                "show": false
            },
            "splitArea": {
                "show": false
            },
            "axisLabel": {
                "interval": 0,

            },
            "data": xData,
        }],
        "yAxis": [{
            "type": "value",
            "splitLine": {
                "show": false
            },
            "axisLine": {
                lineStyle: {
                    color: '#90979c'
                }
            },
            "axisTick": {
                "show": false
            },
            "axisLabel": {
                "interval": 0,

            },
            "splitArea": {
                "show": false
            },

        }],
        "dataZoom": [{
            "show": true,
            "height": 30,
            "xAxisIndex": [
                0
            ],
            bottom: 30,
            "start": 10,
            "end": 80,
            handleIcon: 'path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z',
            handleSize: '110%',
            handleStyle:{
                color:"#d3dee5",

            },
            textStyle:{
               // color:"#fff"
            },
            borderColor:"#90979c"


        }, {
            "type": "inside",
            "show": true,
            "height": 15,
            "start": 1,
            "end": 35
        }],
        "series": [{
            "name": "商品1",
            "type": "bar",
            "stack": "总量",
            "barMaxWidth": 35,
            "barGap": "10%",
            "itemStyle": {
                "normal": {
                   // "color": "rgba(255,144,128,1)",
                    "label": {
                        "show": true,
                        "textStyle": {
                            "color": "#fff"
                        },
                        "position": "insideTop",
                        formatter: function(p) {
                            return p.value > 0 ? (p.value) : '';
                        }
                    }
                }
            },
            "data": [
                709,
                1917,
                2455,
                2610,
                1719,
                1433,
                1544,
                3285,
                5208,
                3372,
                2484,
                4078
            ],
        },

            {
                "name": "商品2",
                "type": "bar",
                "stack": "总量",
                "itemStyle": {
                    "normal": {
                        "color": "rgba(0,191,183,1)",
                        "barBorderRadius": 0,
                        "label": {
                            "show": true,
                            "position": "top",
                            formatter: function(p) {
                                return p.value > 0 ? (p.value) : '';
                            }
                        }
                    }
                },
                "data": [
                    327,
                    1776,
                    507,
                    1200,
                    800,
                    482,
                    204,
                    1390,
                    1001,
                    951,
                    381,
                    220
                ]
            }, {
                "name": "总数",
                "type": "line",
                "stack": "总量",
                symbolSize:10,
                symbol:'circle',
                "itemStyle": {
                    "normal": {
                        "color": "rgba(252,230,48,1)",
                        "barBorderRadius": 0,
                        "label": {
                            "show": true,
                            "position": "top",
                            formatter: function(p) {
                                return p.value > 0 ? (p.value) : '';
                            }
                        }
                    }
                },
                "data": [
                    1036,
                    3693,
                    2962,
                    3810,
                    2519,
                    1915,
                    1748,
                    4675,
                    6209,
                    4323,
                    2865,
                    4298
                ]
            },
        ]
    };
    chartc3.setOption(option);
}

function initShopProdChart() {
    var chartc3 = echarts.init(document.getElementById('shopMonthChart'), 'shine');

    option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        legend: {
            data: ['零售量', '库存量']
        },
        grid: {
            left: '5%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'value'

        },
        yAxis: {
            type: 'category',
            data: ['商品1','商品2','商品3','商品4','商品5','其他商品']
        },
        series: [
            {
                name: '零售量',
                type: 'bar',
                data: [720, 27,20 ,1152 , 700,248 ]
            },
            {
                name: '库存量',
                type: 'bar',
                data: [72,5, 67, 133, 500, 117]
            }
        ]
    };
    chartc3.setOption(option);
}
   