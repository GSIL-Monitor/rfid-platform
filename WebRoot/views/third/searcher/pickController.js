$(function () {
    initSelect();
    initKendoUIGrid();
    $(".k-dropdown").css("width", "6em");
    /*   $(".k-grid-toolbar").css("display", "none");//隐藏toolbar*/
    $(".k-datepicker input").prop("readonly", true);
    initButton();
    initDialog();
/*
    connect();
*/
    search();
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

});
var timerId=null;
function initButton() {
    $("#button").kendoButton({
        click: function (e) {
            alert(e.event.target.tagName);
        }
    });
}
function cylGet() {
    search();
    console.log(formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
}
function initSelect() {
    $("#form_type").kendoMultiSelect({
        dataTextField: "name",
        dataValueField: "value",
        suggest: true,
        index:1,
        value: [1, 2],
        dataSource: {
            data: [
                {name: "普通", value: 1},
                {name: "紧急", value: 2}
            ],
            schema: {
                model: {
                    fields: {
                        name: {type: "string"},
                        value: {type: "number"}
                    }
                }
            }
        }
    });
    $("#form_status_main").kendoMultiSelect({
        dataTextField: "name",
        dataValueField: "value",
        suggest: true,
        value: [1, 2,4],
        dataSource: {
            data: [
                {name: "未读", value: 1},
                {name: "已读", value: 2},
                {name: "已执行", value: 3},
                {name: "部分执行", value: 4},
                {name: "已终止", value: -1}
            ],
            schema: {
                model: {
                    fields: {
                        name: {type: "string"},
                        value: {type: "number"}
                    }
                }
            }
        }
    });
    //初始化颜色下拉框
    $("#form_fromCode").kendoMultiSelect({

        template: '<span class="order-id">#= code #</span> #= name #',
        dataTextField: "name",
        dataValueField: "code",
        height: 400,
        virtual: true,

        suggest: true,
        index: -1,
        dataSource: {
            transport: {
                read: basePath + "/sys/user/list.do",
            }
        }
    });

}
function refresh() {
    resetData();
}
function resetData() {
    var gridData = $("#searchMainGrid").data("kendoGrid");
    var filters=new Array();
    filters.push({
        field: "origCode",
        operator: "eq",
        value: ownerId
    });
    filters.push({
        field: "sendDate",
        operator: "gte",
        value: formatDate(new Date(),"yyyy-MM-dd 00:00:00")
    });
    filters.push({
        field: "toCode",
        operator: "eq",
        value: curUserCode
    });
    gridData.dataSource.filter({
        logic: "and",
        filters: filters
    });
}
function exportExcel() {
    $(".k-grid-excel").click();
}
function showAdvSearchPanel() {
    $("#searchPanel").slideToggle("fast");
}
function search() {
    var gridData = $("#searchMainGrid").data("kendoGrid");
    var filters = serializeToFilter($("#searchForm"));
    filters.push({
        field: "origCode",
        operator: "eq",
        value: ownerId
    });
    filters.push({
        field: "sendDate",
        operator: "gte",
        value: formatDate(new Date(),"yyyy-MM-dd 00:00:00")
    });
    filters.push({
        field: "toCode",
        operator: "eq",
        value: curUserCode
    });
    console.log(filters);
    gridData.dataSource.filter({
        logic: "and",
        filters: filters
    });
    var dataSource = new kendo.data.DataSource({
            transport: {
                total: "total",
                read: {
                    url: basePath + "/third/searcher/list.do",
                    type: "POST",
                    dataType: "json",
                    contentType: 'application/json'
                },
                parameterMap: function (options) {
                    return JSON.stringify(options);
                }
            },
            /*   filter: { field: "sendDate", operator: "eq", value: "2017-04-24 00:00:00" },
             */
            schema: {
                total: "total",
                model: {
                    fields: {
                        status: {type: "string"},
                        id: {type: "string"},
                        type: {type: "number"},
                        toCode: {type: "string"},
                        fromName: {type: "string"},
                        sendDate: {type: "date"},
                        skuQty: {type: "number"},
                        searchQty: {type: "number"},
                        lostQty: {type: "number"},
                        remark: {type: "string"}
                    }
                },
                data: "data"
            },
            sort: [
                {field: "id", dir: "desc"},
                {field: "status", dir: "desc"},
                {field: "type", dir: "desc"}
            ],
        change: function(e) {
            var data = this.data();
            var un=0;
          for(var i=0;i<data.length;i++){
              if(data[i].status=="1"){
                  un++;
              }
          }
          if(parseInt( $('#orderQty').text())<=un){
              $('#orderQty').text(un);
          }
        },
            pageSize: 100.0,
            serverSorting: true,
            serverPaging: true,
            serverGrouping: false,
            serverFiltering: true,
            filter:filters
    });
    $("#searchMainGrid").data("kendoGrid").setDataSource(dataSource);

}

function initKendoUIGrid() {
    $("#searchMainGrid").kendoGrid({
        dataSource: {
            transport: {
                total: "total",
                read: {
                    url: basePath + "/third/searcher/list.do",
                    type: "POST",
                    dataType: "json",
                    contentType: 'application/json'
                },
                parameterMap: function (options) {
                    return JSON.stringify(options);
                }
            },
            /*   filter: { field: "sendDate", operator: "eq", value: "2017-04-24 00:00:00" },
             */
            schema: {
                total: "total",
                model: {
                    fields: {
                        status: {type: "number"},
                        id: {type: "string"},
                        type: {type: "number"},
                        toCode: {type: "string"},
                        fromName: {type: "string"},
                        sendDate: {type: "date"},
                        skuQty: {type: "number"},
                        searchQty: {type: "number"},
                        lostQty: {type: "number"},
                        remark: {type: "string"}
                    }
                },
                data: "data"
            },
            sort: [
                {field: "id", dir: "desc"},
                {field: "status", dir: "desc"},
                {field: "type", dir: "desc"}
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
            pageSize: 50.0,
            pageSizes: [50, 100, 150]
        },
        columnMenu: true,
        reorderable: true,
        resizable: false,
        scrollable: true,
        selectable: false,
        columns: [
            {
                width: "140px",
                locked: true,
                lockable: false,
                template: function (data) {
                    if(parseInt(data.status)<3&&parseInt(data.status)!=-1){
                        return "<div class='btn-group btn-group-sm pull-left'><button class='btn btn-danger'onclick=openDestroyBillDialog(" + data.id +")> 作废</button></div>"+
                            "<div class='btn-group btn-group-sm pull-left'><button class='btn btn-success'onclick=openSearchDialog(" + data.id + "," + data.status +",'"+(data.fromCode)+"')> 找货</button></div>";
                    }else{
                        return "<div class='btn-group btn-group-sm pull-left'><button class='btn btn-danger'disabled onclick=openDestroyBillDialog(" + data.id +")> 作废</button></div>"+
                            "<div class='btn-group btn-group-sm pull-left'><button class='btn btn-success'onclick=openSearchDialog(" + data.id + "," + data.status +",'"+(data.fromCode)+"')> 找货</button></div>";
                    }
                }
            },
            {
                field: "status", title: "状态", locked: true, lockable: false, width: "100px",
                template: function (data) {
                    switch ( parseInt(parseInt(data.status))) {
                        case 1:
                            return "<font color='#262626'>未处理</font>";
                            break;
                        case 2:
                            return "<font color='#4169E1'>已读</font>";
                            break;
                        case 3:
                            return "<font color='#00FA9A'>已执行</font>";
                            break;
                        case 4:
                            return "<font color='#00FF00'>部分执行</font>";
                            break;
                        case -1:
                            return "<font color='#CD0000'>已作废</font>";
                            break;
                    }
                }
            },
            {field: "id", title: "订单号", locked: true, lockable: false, width: "150px"},
            {
                field: "type", title: "等级", locked: true, lockable: false, width: "80px",
                template: function (data) {
                    if (data.type == 1) {
                        return "<img src='"+basePath+"/views/third/searcher/com.png' style='width: 30px;width: 30px'/>";

                        /*
                         return "普通";
                         */
                    } else {
                        return "<img src='"+basePath+"/views/third/searcher/cur.png' style='width: 30px;width: 30px'/>";

                        /*
                         return "紧急";
                         */
                    }
                }
            },
            {
                field: "sendDate",
                title: "订单日期",
                width: "180px",
                format: "{0:yyyy-MM-dd HH:mm:ss}"
            },
            {
                field: "diffDate",
                title: "已耗时",
                width: "120px"
            },
            {field: "fromCode", title: "申请人", width: "150px",
                template: function (data) {
                    return "("+data.fromCode+")"+data.fromName;
                }
            },
            {field: "skuQty", title: "数量", width: "90px", sortable: false,
                template: function (data) {
                    return data.searchQty+"/"+data.skuQty;
                }
            },
            {field: "remark", title: "备注", width: "300px", sortable: false},
            {
                field: "updateDate",
                title: "更新日期",
                width: "180px",
                format: "{0:yyyy-MM-dd HH:mm:ss}"
            },
            {field: "updateRemark", title: "更新备注", width: "300px", sortable: false}

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
            if(contentMsg.msgType==1){
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
    console.log("连接关闭！");

}
function sendMsg() {
    var msg = new Object();
    msg.toCode = toCode;
    msg.fromCode = curUserCode;
    msg.msgType = 1;
    if(websocket){
        websocket.send(JSON.stringify(msg));
    }
}
   