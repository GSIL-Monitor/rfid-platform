
$(function () {

    initSelect();
    initKendoUIGrid();
    $(".k-dropdown").css("width", "6em");
    /*   $(".k-grid-toolbar").css("display", "none");//隐藏toolbar*/
    $(".k-datepicker input").prop("readonly", true);
/*
    $("#form_sku").kendoMultiSelect({
        template: '<span class="order-id">#= code #</span> #= styleName #, #= colorName #,#=sizeName #',
        dataTextField: "code",
        dataValueField: "code",
        height: 400,
        virtual: true,
        suggest: true,
        dataSource: {
            transport: {
                read: basePath + "/prod/product/list.do",
            },
            schema: {
                model: {
                    fields: {
                        code: {type: "string"},
                        stockCode: {type: "string"},
                        styleId: {type: "string"},
                        colorId: {type: "string"},
                        sizeId: {type: "string"},
                        styleName: {type: "string"},
                        colorName: {type: "string"},
                        sizeName: {type: "string"}
                    }
                }
            }
        }
    });
*/
    $("#form_colorId").kendoMultiSelect({
        //  template: '<span class="order-id">#= code #</span> #= styleName #, #= colorName #,#=sizeName #',
        dataTextField: "colorName",
        dataValueField: "colorId",
        height: 400,
        suggest: true
    });
    initButton();
    initDialog();
    $("#timer").kendoNumericTextBox({
        max: 100,
        min: 10,
        step: 5,
        format: "# 秒",
        value:10,
        change:function (date) {
            window.clearInterval(timerId);
            timerId=window.setInterval(cylGet,this.value()*1000);

        }
    });
    timerId=window.setInterval(cylGet, $("#timer").data("kendoNumericTextBox").value()*1000);
    connect();
});
var timerId=undefined;
function cylGet() {
    search();
    console.log(formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+";"+timerId);
}
function initButton() {
    $("#button").kendoButton({
        click: function(e) {
            alert(e.event.target.tagName);
        }
    });
}
function initSelect() {
    //初始化颜色下拉框
    var obj = document.getElementById("form_colorId");
    obj.options.length = 0;
    var styleValue = $("#form_styleId").val()
    if (styleValue != "") {
        var dataSource = new kendo.data.DataSource({
            transport: {
                read: basePath + "/prod/product/list.do?filter_EQS_styleId=" + $("#form_styleId").val()

            },
            schema: {
                model: {
                    fields: {
                        code: {type: "string"},
                        stockCode: {type: "string"},
                        styleId: {type: "string"},
                        colorId: {type: "string"},
                        sizeId: {type: "string"},
                        styleName: {type: "string"},
                        colorName: {type: "string"},
                        sizeName: {type: "string"}
                    }
                }
            },
            serverPaging: false,
            serverFiltering: false
        });
        var multiselect = $("#form_colorId").data("kendoMultiSelect");
        multiselect.setDataSource(dataSource);
    }


}
function refresh() {
    resetData();
}
function resetData() {
/*
    $('#searchForm').clearForm();
*/

     var gridData = $("#fittingGrid").data("kendoGrid");
       gridData.dataSource.filter({}) ;
}
function exportExcel() {
    $(".k-grid-excel").click();
}
function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}

function addProduct(row) {
    var grid = $("#shopCartGrid").data("kendoGrid");
    var data = grid.dataSource.data();
    for (var i = 0; i < data.length; i++) {
        if (data[i].sku == row.sku) {
            return;
        }
    }
    var dataItem = grid.dataSource.insert(0,row);

    //grid.dataSource.add(row);
    /* $.ajax({
         cache:false,
         type: "POST",
         url: basePath + '/third/searcher/findErpStock.do',
         data: {sku:row.sku},// 你的formid
         async: true,
         error: function (request) {
             grid.dataSource.add(row);
             closeProgress();
         },
         success: function (data) {
             if(data.success){
                 row.stockQty=data.result.qty;
                 row.flooId=data.result.flooId;
                 row.otherErpStocks=data.result.otherErpStocks;
             }
             grid.dataSource.add(row);
             closeProgress();
         }
     });*/

}
function search() {
    var gridData = $("#fittingGrid").data("kendoGrid");
    var filters = serializeToFilter($("#searchForm"));
    var nowDate=new Date();
    nowDate.add("n", -2); //
    filters.push({
        field: "scanTime",
        operator: "gte",
        value: formatDate(nowDate,"yyyy-MM-dd HH:mm:00")
    });
    filters.push({
        field: "ownerId",
        operator: "eq",
        value:ownerId
    });
    console.log(filters);
    gridData.dataSource.filter({
        logic: "and",
        filters: filters
    });
}

function openShopCartShop(){
    $('#shopdialog').data('kendoDialog').open();
}
function serializeToFilter(from){
    var filters = [];
    var o = {};
    var fromDatafrom=from.serializeArray();
    $.each(fromDatafrom, function(index) {

        if (o[this['name']]) {
            o[this['name']] = o[this['name']] + "," + this['value'];
        } else {
            o[this['name']] = this['value'];
        }


    });
    for(var key in o){
        var obj = key.split("_");
        var value ;
        if(obj[2].indexOf("Date") != -1){

            if(o[key] != ""){
                value = new Date(o[key]);
                if(obj[1].indexOf("lte") != -1){
                    value = new Date(o[key]);
                    value = new Date(value.getTime()+24*60*60*1000-1);
                }
                filters.push({
                    field: obj[2],
                    operator: obj[1],
                    value: value
                });
            }
        }else{
            value = o[key];
            if(value !=""){
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
function deleteSku(uid) {
    var grid = $("#shopCartGrid").data("kendoGrid");
    var item = grid.dataSource.getByUid(uid);
    grid.dataSource.remove(item);
}
Date.prototype.add = function (part, value) {
    value *= 1;
    if (isNaN(value)) {
        value = 0;
    }
    switch (part) {
        case "y":
            this.setFullYear(this.getFullYear() + value);
            break;
        case "m":
            this.setMonth(this.getMonth() + value);
            break;
        case "d":
            this.setDate(this.getDate() + value);
            break;
        case "h":
            this.setHours(this.getHours() + value);
            break;
        case "n":
            this.setMinutes(this.getMinutes() + value);
            break;
        case "s":
            this.setSeconds(this.getSeconds() + value);
            break;
        default:

    }
}
function initKendoUIGrid() {
    var filters = serializeToFilter($("#searchForm"));
    var nowDate=new Date();
    nowDate.add("n", -2); //
    filters.push({
        field: "scanTime",
        operator: "gte",
        value: formatDate(nowDate,"yyyy-MM-dd HH:mm:00")
    });
    filters.push({
        field: "ownerId",
        operator: "eq",
        value:ownerId
    });
    console.log(filters);

    $("#fittingGrid").kendoGrid({
        dataSource: {
            transport: {
                total: "total",
                read: {
                    url: basePath + "/third/searcher/listFitting.do",
                    type: "POST",
                    dataType: "json",
                    contentType: 'application/json'
                },
                parameterMap: function (options) {
                    return JSON.stringify(options);
                }
            },
            change: function(e) {
                var data = this.data();
                console.log(data.length); // displays "77"
             },
            schema: {
                total: "total",
                model: {
                    id: 'sku',
                    fields: {
                        scanTime: {type: "date"},
                        sku: {type: "string"},
                        styleId: {type: "string"},
                        colorId: {type: "string"},
                        sizeId: {type: "string"},
                        styleName: {type: "string"},
                        colorName: {type: "string"},
                        sizeName: {type: "string"},
                        floorId: {type: "string"},
                        rackId: {type: "string"},
                        image: {type: "string"},
                        stockQty: {type: "number"},
                        otherErpStocks: {type: "object"}


                    }
                },
                data: "data"
            },
            sort: [
                {field: "scanTime", dir: "desc"},
                {field: "sku", dir: "desc"}
            ],
            pageSize: 100.0,
            serverSorting: true,
            serverPaging: true,
            serverGrouping: false,
            serverFiltering: true,
            filter:filters
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
        toolbar: "<p> 试衣信息</p>",
        columns: [
            {
                width: "150px",
                locked: true,
                lockable: false,
                template: function (data) {
                    return  "<div class='btn-group pull-left'><button class='btn btn-info'onclick='addProduct(" + JSON.stringify(data) + ")'> 要货</button>" +
                        "<button class='btn  btn-pink'onclick='openProductDialog(" + JSON.stringify(data) + ")'> 同款</button>" +
                        "</div>";
                }
            },
            {
                width: "120px",
                height:"90px",
                locked: true,
                lockable: false,
                sortable:false,
                template: function (data) {
                    return "<img src='"+data.image+"'  height='90px' width='100%' class='zoomTarget' onclick='openImageDialog("+JSON.stringify(data.image)+")'/>";
                }
            },
            {field: "scanTime", title: "试衣时间", width: "170px", format: "{0:yyyy-MM-dd HH:mm:ss}"},
            {field: "sku", title: "SKU", width: "150px"},
            {field: "styleId", title: "款号", width: "120px", sortable: false},
            {field: "styleName", title: "款名", width: "150px", sortable: false},
            {field: "colorId", title: "色号", width: "80px", sortable: false},
            {field: "colorName", title: "颜色", width: "80px", sortable: false},
            {field: "sizeId", title: "尺号", width: "80px", sortable: false},
            {field: "sizeName", title: "尺码", width: "140px", sortable: false},
            {field: "floorId", title: "库位号", width: "100px",  sortable: false},
            {field: "rackId", title: "货架码", width: "100px",  sortable: false},
            {field: "stockQty", title: "库存", width: "100px",  sortable: false},
            {field: "otherErpStocks", title: "其他店库存", width: "300px", sortable: false,
                template: function (data) {
                    var otherErpStocks=data.otherErpStocks;
                    if(otherErpStocks!=undefined){
                        var stocks="";
                        for(var i=0;i<otherErpStocks.length;i++){
                            stocks+="("+otherErpStocks[i].warehouseName+":"+otherErpStocks[i].qty+"),";
                        }
                        return stocks;
                    }else{
                        return "";
                    }

                }
            }
        ]
    });
}

function onGrouping(arg) {
}

var websocket;
function connect() {
    if (window.WebSocket) {
        websocket = new WebSocket(wsPath + '/requirePlWS.do');
    } else {
        console.log("浏览器不支持WebSocket!");
        return;
    }
    websocket.onopen = function (env) {
        if (websocket.readyState == WebSocket.OPEN) {
            console.log("连接已打开！");
        } else {
            console.log("连接失败！");

        }
    };
    websocket.onmessage = function (event) {
        if (event.data != null) {
            var contentMsg = eval('(' + event.data + ')');
            console.log(contentMsg.content);
            if(contentMsg.msgType==2){

                $('#orderQty').text(contentMsg.content);
            }
        }
    };
    websocket.onclose = function (event) {
        disconnect();
    };
    websocket.onerror = function (envent) {
        console.log("连接失败！");
    };
}

function disconnect() {
    if (websocket != null) {
        websocket.close();
        websocket = null;
    }
}
function sendMsg() {
    var msg = new Object();
    msg.toCode = $("#toCode").data("kendoComboBox").value();
    msg.fromCode = curUserCode;
    msg.msgType = 2;
    if(websocket==null||websocket==undefined){

    }else{
        websocket.send(JSON.stringify(msg));
    }
}
   