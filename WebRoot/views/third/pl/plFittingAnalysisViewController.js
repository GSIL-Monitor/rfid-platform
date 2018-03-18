$(function () {
    initMutiSelect();
    initKendoUIGrid();
    $(".k-dropdown").css("width", "6em");
    $(".k-grid-toolbar").css("display", "none");//隐藏toolbar
    $(".k-datepicker input").prop("readonly", true);

});
function imgShow(img){
    var bigImg=document.getElementById("bigImg");
    bigImg.src=img.src;
    bigImg.setAttribute("width",img.width*3);
    bigImg.setAttribute("height",img.height*3);
    $("#imgModel").modal("show");

}
function filterLazyDay(){
    var LazyDay=$("#lazyDay").children('option:selected').val();
    switch (LazyDay){
        case "3":
            $("#filter_GT_lazyDays").val(3);
            $("#filter_LTE_lazyDays").val(7);
            break;
        case "7":
            $("#filter_GT_lazyDays").val(7);
            $("#filter_LTE_lazyDays").val(14);
            break;
        case "14":
            $("#filter_GT_lazyDays").val(14);
            $("#filter_LTE_lazyDays").val(null);
            break;
        default :
            $("#filter_GT_lazyDays").val(null);
            $("#filter_LTE_lazyDays").val(null);
    }
}
function initMutiSelect() {
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
    $("#filter_CONTAINS_class1").kendoMultiSelect({
        dataTextField: "name",
        dataValueField: "code",
        height: 400,
        suggest: true,
        dataSource: {
            transport: {
                read: basePath + "/sys/property/searchByType.do?type=C1"
            }
        }
    })
    $("#filter_CONTAINS_class2").kendoMultiSelect({
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
    $("#filter_CONTAINS_class3").kendoMultiSelect({
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
    $("#filter_CONTAINS_class4").kendoMultiSelect({
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
    $("#filter_CONTAINS_class10").kendoMultiSelect({
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
}
function refresh() {
    resetData();
}
function resetData() {
    var gridData = $("#saleStock").data("kendoGrid");
    gridData.dataSource.filter({});
}
function exportExcel() {
    $(".k-grid-excel").click();
}
function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}
function search() {
    var gridData = $("#saleStock").data("kendoGrid");
    var filters = serializeToFilter($("#searchForm"));
    console.log(filters);
    gridData.dataSource.filter({
        logic: "and",
        filters: filters
    });

}

function initKendoUIGrid() {

    $("#saleStock").kendoGrid({
        toolbar: ["excel"],
        excel: {
            allPages: true
        },
        dataSource: {
            schema: {
                total: "total",
                model: {
                    fields: {
                        stockDay: {type: "string"},
                        stockCode: {type: "string"},
                        styleId: {type: "string"},
                        colorId: {type: "string"},
                        inDate: {type: "date"},
                        styleName: {type: "string"},
                        colorName:{type:"string"},
                        stockName: {type: "string"},
                        fittingQty: {type: "number"},
                        stockQty: {type: "number"},
                        saleQty: {type: "number"},
                        avgPercent:{type:"number"},
                        actPrice: {type: "number"},
                        backQty: {type: "number"},
                        backPrice:{type:"number"},
                        fittingMonthQty: {type: "number"},
                        saleMonthQty: {type: "number"},
                        backMonthQty: {type: "number"},
                        backMonthPrice: {type: "number"},

                        class1:{type: "string"},
                        class2:{type: "string"},
                        class3:{type: "string"},
                        class4:{type: "string"},
                        class10:{type: "string"},
                        sizeId: {type: "string"},
                        lastestSaleDate:{type: "string"},
                        lastestFittingDate:{type:"string"},
                        stockPrice:{type:"string"},
                        inDays:{type:"number"},
                        lazyDays:{type:"number"}
                    }
                },
                data: "data",
                groups: "data"
            },
            sort: [
                // sort by "category" in descending order and then by "name" in ascending order
                { field: "stockDay", dir: "desc" },
                { field: "stockCode", dir: "desc" },
                { field: "lazyDays", dir: "desc" }

            ],
            transport: {
                read: {
                    url: basePath + "/third/playlounge/analysis/list.do",
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
            serverFiltering: true,
            aggregate: [

                {field: "fittingQty", aggregate: "sum"},
                {field: "saleQty", aggregate: "sum"},
                {field: "stockQty", aggregate: "sum"},

            ]
        },
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

        groupable: true,
        group: onGrouping,
        columnMenu: true,
        filterable: {
            extra: false
        },
        selectable: "multiple row",
        reorderable: true,
        resizable: false,
        scrollable: true,
        detailTemplate: kendo.template($("#template").html()),
        detailInit: detailInit,

        columns: [
            {field: "stockDay", title: "日期", width: "130px", format: "{0:yyyy-MM-dd}"},
            {field: "stockCode", title: "库存编号", width: "160px",
                template: function (data) {
                    if (data.stockName) {
                        return "[" + data.stockCode + "]" + data.stockName;
                    } else {
                        return "[" + data.stockCode + "]";
                    }
                }
            },
            {field: "lazyDays", title: "未动天数", width: "130px",
                template: function (data) {
                    if (data.lazyDays>3&&data.lazyDays<=7) {
                        return "<span class='yellow'>"+data.lazyDays+"   <i class='ace-icon fa fa-warning'></i></span>";
                    } else if(data.lazyDays>7&&data.lazyDays<=14){
                        return "<span class='jacinth'>" + data.lazyDays + "   <i class='ace-icon fa fa-warning'></i></span>";
                    }else if(data.lazyDays>14){
                        return "<span class='red'>" + data.lazyDays + "  <i class='ace-icon fa fa-warning'></i></span>";
                    }else{
                        return data.lazyDays;
                    }
                }
            },
            {field: "inDate", title: "入库日期", width: "130px", format: "{0:yyyy-MM-dd}",
                groupHeaderTemplate: function (data) {
                    var sumfit=data.aggregates.fittingQty.sum;
                    var sumstock=data.aggregates.stockQty.sum;
                    var sumsale=data.aggregates.saleQty.sum;
                    if(sumfit==null){
                        sumfit=0;
                    }
                    if(sumstock==null){
                        sumstock=0;
                    }
                    if(sumsale==null){
                        sumsale=0;
                    }
                    var value = kendo.toString(data.value, 'yyyy-MM-dd')
                    return "日期:"+value+" 试衣总数:"+sumfit+" 库存总数:"+sumstock+" 销售总数:"+sumsale;
                }
            },
            {field: "styleId",title:"款号",width:"120px"},
            {field: "styleName", title: "款式", width: "130px"},
            {field: "colorId", title: "色码", width: "130px"},
            {field: "colorName", title: "颜色", width: "130px"},
            {field: "fittingQty", title: "试衣数量", width: "130px",groupable: false, aggregates: ["sum"]},
            {field: "stockQty", title: "库存数量", width: "130px",groupable: false, aggregates: ["sum"]},
            {field: "saleQty", title: "销售数量", width: "130px",groupable: false, aggregates: ["sum"]},
            {field: "avgPercent", title: "平均折扣", width: "130px",groupable: false,},
            {field: "actPrice", title: "价格", width: "130px",groupable: false,},
            {field: "backQty", title: "退货数量", width: "130px",groupable: false,},
            {field: "backPrice", title: "退货价格", width: "130px",groupable: false,},
            {field: "fittingMonthQty", title: "月试衣次数", width: "130px",groupable: false,},
            {field: "saleMonthQty", title: "月销售数量", width: "130px",groupable: false,},
            {field: "backMonthQty", title: "月退货数量", width: "130px",groupable: false,},
            {field: "backMonthPrice", title: "月退货价格", width: "130px",groupable: false,},
            {field: "class1", title: "品牌", width: "130px"},
            {field: "class2", title: "年份", width: "130px"},
            {field: "class3", title: "大类", width: "130px"},
            {field: "class4", title: "小类", width: "130px"},
            {field: "class10", title: "季节", width: "130px"},
            {field: "lastestSaleDate", title: "最新销售日期", width: "130px",groupable: false,},
            {field: "lastestFittingDate", title: "最新试衣日期", width: "130px",groupable: false,},
            {field: "stockPrice", title: "库存金额", width: "130px",groupable: false,},
            {field: "inDays", title: "进店天数", width: "130px",groupable: false,}

        ]
    });

}
function  detailInit(e){
    var detailRow = e.detailRow;
    detailRow.find(".tabstrip").kendoTabStrip({
        animation: {
            open: { effects: "fadeIn" }
        }
    });

    detailRow.find(".fitDetail").kendoGrid({
        dataSource: {
            schema: {
                total: "total",
                model: {
                    fields: {
                        stockDay: {type: "string"},
                        inDate: {type: "string"}
                    }
                },
                data: "data",
                groups: "data"
            },
            type: "odata",
            transport: {
                read: {
                    url: basePath + "/third/saleStockThirdStock/list.do",
                    type: "POST",
                    dataType: "json",
                    contentType: 'application/json'
                },
                parameterMap: function (options) {
                    return JSON.stringify(options);
                }
            },

            pageSize: 100.0,
            serverSorting: false,
            serverPaging: false,
            serverGrouping: false,
            serverFiltering: true,
            filter: [
                { field: "stockDay", operator: "EQ", value: e.data.stockDay },
                { field: "stockCode", operator: "EQ", value: e.data.stockCode },
                { field: "styleId", operator: "EQ", value: e.data.styleId },
                { field: "colorId", operator: "EQ", value: e.data.colorId}
            ]
        },
        scrollable: false,
        pageable: false,
        columns: [
            {field: "sku", title: "SKU", width: "120px"},
            {field: "inDate", title: "入库日期", width: "130px", format: "{0:yyyy-MM-dd}"},
            {field: "sizeId", title: "尺号", width: "80px"},
            {field: "sizeName", title: "尺码", width: "110px"},
            {field: "stockQty", title: "库存量", width: "100px"},
            {field: "fittingQty", title: "试衣量", width: "100px"},
            {field: "saleQty", title: "销售量", width: "100px"},
            {field: "stockPrice", title: "库存金额", width: "80px"},
            {field: "avgPercent", title: "平均折扣", width: "80px"},
            {field: "actPrice", title: "销售总额", width: "80px"}

        ]
    });
    $.ajax({
        url: basePath+"/third/playlounge/analysis/plFitting_detail.do?filter_EQS_styleId="+ e.data.styleId+"&filter_EQS_colorId="+ e.data.colorId,
        type: "post",
        success: function(data) {
            if(data.result.styleName!=null){
                var html="";
                html+='<div class="row">';
                html+='<div class="col-lg-12">';

                html+='<div class="col-lg-1 col-xs-1">';
                html+='<img src="#= image #"  onclick="imgShow(this)" width="100%"/>';
                html+='</div>';

                html+='<div class="col-lg-2 col-xs-2">';
                html+='<div class="profile-user-info profile-user-info-striped">';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 款号 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= styleId #</span>';
                html+='</div>';
                html+='</div>';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 款式 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= styleName #</span>';
                html+='</div>';
                html+='</div>';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 尺码 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= sizeName #</span>';
                html+='</div>';
                html+='</div>';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 颜色 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= colorName #</span>';
                html+='</div>';
                html+='</div>';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 品牌 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= class1 #</span>';
                html+='</div>';
                html+='</div>';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 年份 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= class2 #</span>';
                html+='</div>';
                html+='</div>';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 大类 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= class3 #</span>';
                html+='</div>';
                html+='</div>';
                html+='</div>';

                html+='</div>';
                html+='<div class="col-lg-2 col-xs-2">';
                html+='<div class="profile-user-info profile-user-info-striped">';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 小类 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= class4 #</span>';
                html+='</div>';
                html+='</div>';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 性别 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= class5 #</span>';
                html+='</div>';
                html+='</div>';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 厂商 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= class6 #</span>';
                html+='</div>';
                html+='</div>';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 库位码 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= class7 #</span>';
                html+='</div>';
                html+='</div>';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 材质 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= class8 #</span>';
                html+='</div>';
                html+='</div>';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 库类 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= class9 #</span>';
                html+='</div>';
                html+='</div>';
                html+='<div class="profile-info-row">';
                html+='<div class="profile-info-name"> 四季分类 </div>';
                html+='<div class="profile-info-value">';
                html+='<span class="editable" >#= class10 #</span>';
                html+='</div>';
                html+='</div>';
                html+='</div>';
                html+='</div>';
                html+='</div>';
                html+='</div>';
                var template = kendo.template(html);
                var result=template(data.result);
                $("."+data.result.styleId).html(result);
            }
        }
    })


}
function onGrouping(arg) {

}
