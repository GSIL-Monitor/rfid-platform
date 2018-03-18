<%@ page language="java" pageEncoding="UTF-8" %>

<script>

    function initDialog() {
        initNotification();

        $("#searchMainDtl_dialog").kendoDialog({
            closable: false,
            height: 700,
            width: 950,
            content: "<div id='searchMainDtlGrid'></div>",
            title: "找货信息",
            actions: [/*{
                text: "确定",
                primary: true
            }, */{
                text: "关闭"
            }],
            initOpen: initOpen,
            close: function (e) {
                id=null;
                status=null;
                return true;
            }
        }).data("kendoDialog").close();
        //提交对话框
        $("#send_dialog").kendoDialog({
            closable: false,
            height: 350,
            width: 400,
            title: "确认",
            actions: [{
                text: "确定",
                action:sendAjax,
                primary: true
            }, {
                text: "关闭"
            }],
            close: function (e) {
                $('#send_remark').val("");
                return true;
            }
        }).data("kendoDialog").close();
        $("#destroy_dialog").kendoDialog({
            closable: false,
            height: 350,
            width: 400,
            title: "销毁",
            actions: [{
                text: "确定",
                action:sendDestroyAjax,
                primary: true
            }, {
                text: "关闭"
            }],
            close: function (e) {
                $('#destroy_remark').val("");
                return true;
            }
        }).data("kendoDialog").close();
        $("#searchMainDtlGrid").kendoGrid({
            filterable: false,
            sortable: {
                mode: "multiple",
                allowUnsort: true
            },
            pageable: false,
            columnMenu: true,
            toolbar: "<div class='btn-group'><p> <button class='btn btn-sm btn-success' type='button' id='btn_sendEndBill' onclick='openSendEndBillDialog()'>" +
            "<span class='glyphicon glyphicon-ok' ></span>执行</button>" +
            "<button class='btn btn-sm btn-danger' type='button' id='btn_sendDestroyBill' onclick='openDestroyBillDialog()'>" +
            "<span class='glyphicon glyphicon-ok' ></span>作废</button>" +
            "</p></div>",
            reorderable: true,
            resizable: false,
            scrollable: true,
            selectable: "multiple row",
            height: 550,
            dataBound: function (e) {
                e.sender.items().each(function () {
                    var dataItem = e.sender.dataItem(this);
                    kendo.bind(this, dataItem);
                    if (dataItem.searchSuccess) {
                        $(this).addClass("k-state-selected");
                    }
                });
            },
            columns: [
                {
                    field: "searchSuccess", width: "100px", locked: true, lockable: false, sortable: false,
                    template: "<input type='checkbox' data-bind='checked:searchSuccess' />",title: "是否找到"
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
                {field: "sku", title: "SKU", width: "180px", locked: true, lockable: false,},
                {field: "stockQty", title: "库存", width: "120px", locked: true, lockable: false, sortable: false},
                {field: "rackId", title: "货架编码", width: "120px", locked: true, lockable: false, sortable: false},
                {field: "floorId", title: "库位码", width: "120px", locked: true, lockable: false, sortable: false},
                {field: "styleId", title: "款号", width: "180px", sortable: false},
                {field: "styleName", title: "款名", width: "150px", sortable: false},
                {field: "colorId", title: "色号", width: "80px", sortable: false},
                {field: "colorName", title: "颜色", width: "80px", sortable: false},
                {field: "sizeId", title: "尺号", width: "80px", sortable: false},
                {field: "sizeName", title: "尺码", width: "140px", sortable: false},
                {field: "otherErpStocks", title: "其他店库存", width: "400px", sortable: false,
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
    }
    function checkAll(input) {
        var grid = $("#searchMainDtlGrid").data("kendoGrid");
        var items = grid.items();
        items.each(function () {
            var dataItem = grid.dataItem(this);
            if (dataItem.searchSuccess != input.checked) {
                dataItem.searchSuccess = input.checked;
                dataItem.dirty = true;
            }
        });
    }
    function initOpen(e) {
    }
    var id=null;
    var status=null;
    var toCode=null;
    function openSearchDialog(mainId,substatus,suCode) {
        var sta=parseInt(substatus);
        switch ( sta) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                $('#btn_sendEndBill').attr("disabled","disabled");
                $('#btn_sendDestroyBill').attr("disabled","disabled");
                break;
            case 4:
                $('#btn_sendDestroyBill').attr("disabled","disabled");
                break;
            case -1:
                $('#btn_sendEndBill').attr("disabled","disabled");
                $('#btn_sendDestroyBill').attr("disabled","disabled");
                break;
        }
       this.toCode=suCode;
        this.id=mainId;
        this.status=substatus;
        openProgress();
        if (substatus == 1) {
            $.ajax({
                cache: true,
                type: "POST",
                url: basePath + '/third/searcher/updateMainStatus.do',
                data: {id: mainId, status: 2},// 你的formid
                async: true,
                error: function (request) {
                    closeProgress();
                },
                success: function (data) {
                  /*  var dialog = $("#searchMainDtl_dialog").data("kendoDialog");
                    dialog.open();*/
                    search();
                    closeProgress();
                }
            });
        } else {
        }
        var dialog = $("#searchMainDtl_dialog").data("kendoDialog");
        dialog.open();
        var dataSource = new kendo.data.DataSource({
            transport: {
                total: "total",
                read: {
                    url: basePath + "/third/searcher/listDtl.do",
                    type: "POST",
                    dataType: "json",
                    contentType: 'application/json'
                },
                parameterMap: function (options) {
                    return JSON.stringify(options);
                }
            },
            schema: {
                total: "total",
                model: {
                    id: 'sku',
                    fields: {
                        searchSuccess: {type: "boolean"},
                        floorId:{type:"string"},
                        rackId:{type:"string"},
                        sku: {type: "string"},
                        styleId: {type: "string"},
                        colorId: {type: "string"},
                        sizeId: {type: "string"},
                        styleName: {type: "string"},
                        colorName: {type: "string"},
                        sizeName: {type: "string"},
                        stockQty: {type: "number"},
                        otherErpStocks:{type:"object"},
                        image:{type:"string"}
                    }
                },
                data: "data"
            },
            sort: [
                {field: "searchSuccess", dir: "asc"},
                {field: "sku", dir: "desc"},
            ],
            serverSorting: true,
            serverPaging: false,
            serverGrouping: false,
            serverFiltering: true,
            filter: {
                field: "mainId",
                operator: "eq",
                value: mainId + ""
            }

        });
        $("#searchMainDtlGrid").data("kendoGrid").setDataSource(dataSource);
        closeProgress();
    }
    function openSendEndBillDialog() {
        var grid = $("#searchMainDtlGrid").data("kendoGrid");
        var rows = grid.select();
        if(rows.length>0){
            $("#send_dialog").data("kendoDialog").open();
        }

    }
    function sendAjax() {
        var grid = $("#searchMainDtlGrid").data("kendoGrid");
        var rows = grid.select();
        var formData=new Object();
        var filters = [];
        if(rows.length>0){
            openProgress();
            for(var i=0;i<rows.length;i+=2){
                var data = grid.dataItem(rows[i]);
                filters.push({
                    sku: data.sku,
                    searchSuccess: data.searchSuccess
                });
            }
            $.ajax({
                cache: true,
                type: "POST",
                url: basePath + '/third/searcher/updateStatus.do',
                data: {id: id, status: 3,searchDtls:JSON.stringify(filters),remark:$('#send_remark').val()},// 你的formid
                async: true,
                error: function (request) {
                    closeProgress();
                },
                success: function (data) {
                    if (data.success) {
                        $("#send_dialog").data("kendoDialog").close();
                        $("#searchMainDtl_dialog").data("kendoDialog").close();
                        $("#notification").data('kendoNotification').showText('成功！', 'success');
                        sendMsg();
                        search();
                    }else{
                        $("#notification").data('kendoNotification').showText('失败！', 'error');
                    }
                    closeProgress();
                }
            });
        }
        return true;
    }

    function openDestroyBillDialog(mainId) {
        if(mainId){
            id=mainId;
        }
            $("#destroy_dialog").data("kendoDialog").open();
    }
    function sendDestroyAjax() {
        openProgress();
            $.ajax({
                cache: true,
                type: "POST",
                url: basePath + '/third/searcher/destroySearchMain.do',
                data: {id: id, updateRemark:$('#destroy_remark').val()},// 你的formid
                async: true,
                error: function (request) {
                    closeProgress();
                },
                success: function (data) {
                    if (data.success) {
                        $("#destroy_dialog").data("kendoDialog").close();
                        $("#searchMainDtl_dialog").data("kendoDialog").close();
                        $("#notification").data('kendoNotification').showText('成功！', 'success');
                        sendMsg();
                        closeProgress();
                    }else{
                        $("#destroy_dialog").data("kendoDialog").close();
                        $("#notification").data('kendoNotification').showText('失败！', 'success');
                    }
                    search();
                    closeProgress();
                }
            });
        return true;
    }

    function openProgress() {
        $("#progressDialog").data('kendoDialog').open();
    }
    function closeProgress() {
        $("#progressDialog").data('kendoDialog').close();
    }

    function initNotification() {
        $("#notification").kendoNotification({
            position: {
                top: 50
            },
            stacking: "left"
        }).data("kendoNotification").hide();
    }
</script>
<div id="searchMainDtl_dialog">
</div>
<div id="send_dialog">
    <form id="sendForm" role="form">
        <div class="form-group">
            <label for="send_remark">备注</label>
            <textarea class="form-control" rows="4" id="send_remark" name="remark"></textarea>
        </div>
    </form>
</div>
<div id="destroy_dialog">
    <form id="destroyForm" role="form">
        <div class="form-group">
            <label for="destroy_remark">备注</label>
            <textarea class="form-control" rows="4" id="destroy_remark" name="remark"></textarea>
        </div>
    </form>
</div>
<div id="progressDialog"></div>
<span id="notification"></span>