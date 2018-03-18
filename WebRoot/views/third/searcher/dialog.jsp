<%@ page language="java" pageEncoding="UTF-8" %>

<script>

    function initDialog() {
        $("#product_dialog").kendoDialog({
            closable: true,
            height: 700,
            width: 950,
            content: "<div id='productGrid'></div>",
            title: "选则商品信息",
            actions: [{
                text: "关闭"
            }],
            initOpen: initOpen,
            close: function (e) {
                $(':input','#searchProductForm').val("");
                return true;
            }
        });
        $("#imageDialog").blur(function () {
            $("#imageDialog").data('kendoDialog').close();
        });
        $("#imageDialog").kendoDialog({
            closable: true,
            height: 300,
            width: 400,
            title: false
        }).data("kendoDialog").close();
        $("#product_dialog").data("kendoDialog").close();
        initNotification();
        /**
         * 订单发送dialog
         * */
        $("#searcherMain_dialog").kendoDialog({
            closable: true,
            height: 400,
            width: 450,
            title: "填写发送信息",
            actions: [{
                text: "发送",
                action: sendSearchMain,
                primary: true
            }, {
                text: "关闭"
            }],
            initOpen: initOpen,
            close: function (e) {
                /*   $('#form_skuf').val("");
                 $('#searchProductForm').val("");
                 $('#form_sized').val("");
                 $("#productGrid").data("kendoGrid").clearSelection();*/
            }
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
        $("#sendForm").kendoValidator({
            messages: {
                required: "必须填写"
            }
        });

        $("#toCode").kendoComboBox({
            template: '<span class="order-id">#= code #</span> #= name #',
            dataTextField: "name",
            dataValueField: "code",
            /*
             virtual: true,
             */
            autoWidth: true,
            suggest: true,
            index: 0,
            dataSource: {
                transport: {
                    read: basePath + "/sys/user/list.do?filter_EQI_type=4&filter_EQS_ownerId=" + ownerId+"&filter_NINS_code="+curUserCode,
                }
            },
            change: function (e) {
                $('#toName').val(this.text());
            }
        }).data("kendoComboBox").input[0].name = "";
        $("#searcherMain_dialog").data("kendoDialog").close();

        /**
         * 订单查询
         * */
        $("#searcherBill_dialog").kendoDialog({
            closable: true,
            height: 700,
            width: 950,
            content: "<div id='searcherBillGrid'></div>",
            title: "订单查询",
            actions: [{
                text: "关闭"
            }],
             open:function (e) {
                 var t = setTimeout("freshSearcherBillGrid()", 500)
            }
        });
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
        $("#searcherBill_dialog").data("kendoDialog").close();
        /**
         * 购物车
         * */
        $("#shopdialog").kendoDialog({
            modal: true,
            closable: true,
            height: 700,
            width: 950,
            content: "<div id='shopCartGrid'></div>",
            title: "要货详细",
            actions: [{
                text: "关闭"
            }],
            open: function (e) {
                var t = setTimeout("freshShop()", 500)
            }
        }).data("kendoDialog").close();
        $("#searcherMain_dialog").data("kendoDialog").close();
        $("#cartDialog").kendoWindow({
            closeable: false,
            modal: false,
            height: 110,
            width: 180,
            title: false,
            iframe: false,
            position: {
                top: '80%',
                left: '80%'
            }
        });
        $("#searchMainDtl_dialog").kendoDialog({
            closable: false,
            height: 700,
            width: 950,
            content: "<div id='searchMainDtlGrid'></div>",
            title: "订单明细",
            actions: [{
                text: "关闭"
            }],
            open:function(e){
                var t = setTimeout("freshSearchDtlGrid()", 500)
            }
        }).data("kendoDialog").close();

        initInGrid();
    }
    function initInGrid() {

        $("#productGrid").kendoGrid({
            dataSource: {
                transport: {
                    total: "total",
                    read: {
                        url: basePath + "/third/searcher/listProduct.do",
                        type: "POST",
                        dataType: "json",
                        contentType: 'application/json'
                    },
                    parameterMap: function (options) {
                        return JSON.stringify(options);
                    }
                },
                group: [{
                    field: "styleId", aggregates: [
                        {field: "stockQty", aggregate: "sum"}]
                },
                    {field: "colorName", aggregates: [{field: "stockQty", aggregate: "sum"}]}],
                schema: {
                    total: "total",
                    model: {
                        id: 'sku',
                        fields: {
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
                    {field: "code", dir: "desc"}
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
                pageSize: 100.0,
                pageSizes: [100, 150, 200]
            },
            columnMenu: true,
            reorderable: true,
            resizable: false,
            toolbar: kendo.template("<p><div class='row'><form id='searchProductForm' class='form-inline'>" +
                " <div class='form-group' >" +
                "<label for='form_skuf'>款号</label>" +
                "<input class='form-control' id='product_styleId' name='filter_CONTAINS_styleId'placeholder='sku'/>" +
                "</div>" +
                " <div class='form-group'>" +
                "<label for='form_styleIdd'>颜色</label>" +
                "<input class='form-control'id='product_colorId'type='text'name='filter_CONTAINS_colorId'placeholder='颜色'/>" +
                "</div>" +
                " <div class='form-group'>" +
                "<label for='form_sized'>尺码</label>" +
                "<input class='form-control'id='product_sized'type='text'name='filter_CONTAINS_sizeId'placeholder='尺码'/>" +
                "</div>" +
                "<button  class='btn k-primary'id='btn_findProduct' type='button' style='min-width: 60px' onclick='searchProduct()'>查询</button>" +

                "</form>" +
                "</div></p>"),
            scrollable: true,
            height: 550,
            selectable: false,
            columns: [
                {
                    title: "操作",
                    width: "90px",
                    locked: true,
                    sortable: false,
                    template: function (data) {
                        return "<button class='btn btn-info'onclick='addProduct(" + JSON.stringify(data) + ")'> 要货</button>";
                    }
                },
                {
                    locked: true,
                    title: "图片",
                    width: "120px",
                    height: "90px",
                    sortable: false,
                    template: function (data) {
                        return "<img src='" + data.image + "'  height='90px' width='100%' class='zoomTarget' onclick='openImageDialog(" + JSON.stringify(data.image) + ")'/>";
                    }
                },
                {field: "sku", title: "SKU", width: "150px", locked: true},
                {
                    field: "styleId", title: "款", width: "250px", sortable: false,
                    groupHeaderTemplate: function (data) {
                        var sumstyleId = data.aggregates.stockQty.sum;
                        if (sumstyleId == null) {
                            sumstyleId = 0;
                        }

                        return "款号:" + data.value + " 本店库存:" + sumstyleId;
                    },
                    template: function (data) {
                        return "(" + data.styleId + ")" + data.styleName;
                    }
                },
                {field: "colorName", title: "颜色", width: "120px", sortable: false,
                    groupHeaderTemplate: function (data) {
                        var sumstyleId = data.aggregates.stockQty.sum;
                        if (sumstyleId == null) {
                            sumstyleId = 0;
                        }
                        return "颜色:" + data.value + " 本店库存:" + sumstyleId;
                    },
                    template: function (data) {
                        return "(" + data.colorId + ")" + data.colorName;
                    }
                },
                {field: "sizeId", title: "尺码", width: "130px", sortable: false,
                    template: function (data) {
                        return "(" + data.sizeId + ")" + data.sizeName;
                    }
                },
                {field: "floorId", title: "库位号", width: "120px", sortable: false},
                {field: "rackId", title: "货架码", width: "120px", sortable: false},
                {field: "stockQty", title: "库存", width: "80px", sortable: false},
                {field: "otherErpStocks", title: "其他店库存", width: "300px", sortable: false,
                    template: function (data) {
                        var otherErpStocks = data.otherErpStocks;
                        if (otherErpStocks != undefined) {
                            var stocks = "";
                            for (var i = 0; i < otherErpStocks.length; i++) {
                                stocks += "(" + otherErpStocks[i].warehouseName + ":" + otherErpStocks[i].qty + "),";
                            }
                            return stocks;
                        } else {
                            return "";
                        }

                    }
                }
            ]

        });
        $("#searcherBillGrid").kendoGrid({
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
                filter: [
                    { field: "sendDate", operator: "GTE", value: formatDate(new Date(),"yyyy-MM-dd 00:00:00")},
                    { field: "origCode", operator: "eq", value: ownerId },
                    { field: "fromCode", operator: "eq", value: curUserCode }],

                schema: {
                    total: "total",
                    model: {
                        fields: {
                            status: {type: "string"},
                            id: {type: "string"},
                            type: {type: "number"},
                            toCode: {type: "string"},
                            toName: {type: "string"},
                            sendDate: {type: "date"},
                            updateDate: {type: "date"},
                            skuQty: {type: "number"},
                            searchQty: {type: "number"},
                            lostQty: {type: "number"},
                            remark: {type: "string"},
                            updateRemark: {type: "string"},
                            diffDate:{type:"string"}
                        }
                    },
                    data: "data"
                },
                sort: [
                    {field: "sendDate", dir: "desc"},
                    {field: "status", dir: "desc"},
                    {field: "type", dir: "desc"}
                ],
                pageSize: 50.0,
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
            height: "100%",
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
                                "<div class='btn-group btn-group-sm pull-left'><button class='btn btn-success'onclick=openSearchDtlDialog(" + data.id +")> 明细</button></div>";
                        }else{
                            return "<div class='btn-group btn-group-sm pull-left'><button class='btn btn-danger'disabled onclick=openDestroyBillDialog(" + data.id +")> 作废</button></div>"+
                                "<div class='btn-group btn-group-sm pull-left'><button class='btn btn-success'onclick=openSearchDtlDialog(" + data.id +")> 明细</button></div>";                        }
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
                {field: "toCode", title: "库管", width: "150px",
                    template: function (data) {
                        return "("+data.toCode+")"+data.toName;
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
        $("#searchMainDtlGrid").kendoGrid({
            filterable: false,
            sortable: {
                mode: "multiple",
                allowUnsort: true
            },
            pageable: false,
            columnMenu: false,
            reorderable: true,
            resizable: false,
            scrollable: true,
            selectable: false,
            height: 550,
            columns: [
                {
                    field: "searchSuccess", width: "100px", title: "是否找到",locked: true, lockable: false, sortable: true,
                    template: function (data) {
                        if(data.searchSuccess){
                            return "已找到";
                        }else{
                            return "未找到";
                        }
                    }
                },
                {field: "sku", title: "SKU", width: "180px", locked: true, lockable: false},
                {
                    locked: true,
                    title: "图片",
                    width: "120px",
                    height: "90px",
                    sortable: false,
                    template: function (data) {
                        return "<img src='" + data.image + "'  height='90px' width='100%' class='zoomTarget' onclick='openImageDialog(" + JSON.stringify(data.image) + ")'/>";
                    }
                },
                {field: "stockQty", title: "库存", width: "80px", locked: true, lockable: false, sortable: false},
                {field: "rackId", title: "货架编码", width: "120px", sortable: false},
                {field: "floorId", title: "库位码", width: "120px",  sortable: false},
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

        $("#shopCartGrid").kendoGrid({
            dataSource: {
                schema: {
                    model: {
                        id: 'sku',
                        fields: {
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
                    sort: [
                        {field: "sku", dir: "desc"}
                    ]
                },
                change: function (e) {
                    var data = this.data();
                    $('#shopCount').text("(" + data.length + ")")
                }
            },
            filterable: false,
            sortable: {field: "sku", dir: "desc"},
            pageable: false,
            columnMenu: false,
            selectable: false,
            reorderable: true,
            resizable: true,
            scrollable: true,
            height: "100%",
            toolbar: "<div class='btn-group'> <button class='btn btn-sm btn-danger' type='button' id='btn_refresh' onclick='clearShop()'><span class='glyphicon glyphicon-remove' ></span>清空</button>" +
            "<button class='btn btn-sm btn-info' type='button' id='btn_refresh' onclick='openSearchMainDialog()'><span class='glyphicon glyphicon-send' ></span>发送</button>" +
            "</div>",
            columns: [
                {
                    title: "操作",
                    width: "90px",
                    locked: true,
                    sortable: false,
                    template: function (data) {
                        return "<button class='btn btn-warning'onclick='deleteSku(" + JSON.stringify(data.uid) + ")'> 删除</button>";
                    }
                },{
                    locked: true,
                    title: "图片",
                    width: "120px",
                    height: "90px",
                    sortable: false,
                    template: function (data) {
                        return "<img src='" + data.image + "'  height='90px' width='100%' class='zoomTarget' onclick='openImageDialog(" + JSON.stringify(data.image) + ")'/>";
                    }
                },
                {field: "sku", title: "SKU", width: "150px", locked: true},
                {field: "styleId", title: "款号", width: "120px", sortable: false},
                {field: "styleName", title: "款名", width: "150px", sortable: false},
                {field: "colorId", title: "色号", width: "80px", sortable: false},
                {field: "colorName", title: "颜色", width: "80px", sortable: false},
                {field: "sizeId", title: "尺号", width: "80px", sortable: false},
                {field: "sizeName", title: "尺码", width: "140px", sortable: false},
                {field: "floorId", title: "库位号", width: "100px", sortable: false},
                {field: "rackId", title: "货架码", width: "100px", sortable: false},
                {field: "stockQty", title: "库存", width: "100px", sortable: false},
                {field: "otherErpStocks", title: "其他店库存", width: "300px", sortable: false,
                    template: function (data) {
                        var otherErpStocks = data.otherErpStocks;
                        if (otherErpStocks != undefined) {
                            var stocks = "";
                            for (var i = 0; i < otherErpStocks.length; i++) {
                                stocks += "(" + otherErpStocks[i].warehouseName + ":" + otherErpStocks[i].qty + "),";
                            }
                            return stocks;
                        } else {
                            return "";
                        }

                    }
                }
            ]
        });

    }
    function openProductDialog(row) {
        $('#product_styleId').val(row.styleId);
        var dialog = $("#product_dialog").data("kendoDialog");

        dialog.open();
        searchProduct();
        $('#product_colorId').val(row.colorId)
    }
    function  openSearchDtlDialog(mainId){
        id=mainId;
        $("#searchMainDtl_dialog").data("kendoDialog").open();
    }
    function freshSearchDtlGrid(){

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
                        otherErpStocks:{type:"object"}
                    }
                },
                data: "data"
            },
            sort: [
                {field: "searchSuccess", dir: "desc"},
                {field: "sku", dir: "desc"}
            ],
            serverSorting: true,
            serverPaging: false,
            serverGrouping: false,
            serverFiltering: true,
            filter: {
                field: "mainId",
                operator: "eq",
                value: id + ""
            }

        });
        $("#searchMainDtlGrid").data("kendoGrid").setDataSource(dataSource);
    }
    var id;
    function openDestroyBillDialog(mainId) {
        id=mainId;
        $("#destroy_dialog").data("kendoDialog").open();
    }
    function freshShop() {
        var grid = $("#shopCartGrid").data("kendoGrid");
        if (grid != undefined) {
            grid.refresh();
        }
    }
    function freshSearcherBillGrid() {
        var grid = $("#searcherBillGrid").data("kendoGrid");
        if (grid != undefined) {
            grid.dataSource.filter(   [
                { field: "sendDate", operator: "GTE", value: formatDate(new Date(),"yyyy-MM-dd 00:00:00")},
                { field: "origCode", operator: "eq", value: ownerId },
                { field: "fromCode", operator: "eq", value: curUserCode }]) ;
        }
    }
    function openImageDialog(imag) {
        $('#imageDialog').data('kendoDialog').content("<img src='" + imag + "' height='100%' width='100%'/>");
        $('#imageDialog').data('kendoDialog').open();
    }
    function clearShop(){

        var data = $('#shopCartGrid').data('kendoGrid').dataSource.data();
        var length = data.length;
        for (var i = 0; i < length; i++) {
            var dataItem = data.at(0);
            data.remove(dataItem);
        }
    }
    function sendSearchMain() {
        var validator = $("#sendForm").data("kendoValidator");
        if (validator.validate()) {
            openProgress();
            var grid = $("#shopCartGrid").data("kendoGrid");
            var data = grid.dataSource.data();
            $('#searchDtls').val(JSON.stringify(data));
            $.ajax({
                cache: true,
                type: "POST",
                url: basePath + '/third/searcher/save.do',
                data: $('#sendForm').serialize(),// 你的formid
                async: true,
                error: function (request) {
                    closeProgress();
                },
                success: function (data) {
                    if (data.success) {
                        $("#searcherMain_dialog").data("kendoDialog").close();
                        $("#remark").val("");
                        /* $("input[name='type'][value='1']").attr("checked",true);*/
                        $("#notification").data('kendoNotification').showText('发送成功！', 'success');
                        var data = $('#shopCartGrid').data('kendoGrid').dataSource.data();
                        var length = data.length;
                        for (var i = 0; i < length; i++) {
                            var dataItem = data.at(0);
                            data.remove(dataItem);
                        }
                        $('#shopdialog').data('kendoDialog').close();
                        sendMsg();
                    } else {
                        $("#notification").data('kendoNotification').showText('发送失败！', 'error');
                    }
                    closeProgress();
                }
            });
            console.log("发送");
        }
        return false;
    }
    function initOpen(e) {

    }
    function initSearchMainOpen(e) {
        setTimeout(function () {
            $("#searcherBillGrid").data("kendoGrid").refresh();
        });
    }
    function searchSearcherBill() {
        var gridData = $("#searcherBillGrid").data("kendoGrid");
        var filters = serializeToFilter($("#searchMainBillForm"));

        console.log(filters);
        gridData.dataSource.filter({
            logic: "and",
            filters: filters
        });
    }
    function searchProduct() {
        var gridData = $("#productGrid").data("kendoGrid");
        var filters = serializeToFilter($("#searchProductForm"));
        console.log(filters);
        gridData.dataSource.filter({
            logic: "and",
            filters: filters
        });
    }
    function openSearchMainBillDialog() {
        var dialog = $("#searcherBill_dialog").data("kendoDialog");
        dialog.open();
    }
    function openSearchMainDialog() {
        var dataSource = $("#shopCartGrid").data("kendoGrid").dataSource;
        if (dataSource.data().length > 0) {
            var dialog = $("#searcherMain_dialog").data("kendoDialog");
            dialog.open();
        }
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
    function sendDestroyAjax() {
        openProgress();
        $.ajax({
            cache: true,
            type: "POST",
            url: basePath + '/third/searcher/destroySearchMain.do',
            data: {id: id, status: -1,updateRemark:$('#destroy_remark').val()},// 你的formid
            async: true,
            error: function (request) {
                closeProgress();
            },
            success: function (data) {
                if (data.success) {
                    $("#destroy_dialog").data("kendoDialog").close();
                    $("#notification").data('kendoNotification').showText('成功！', 'success');
                }else{
                    $("#destroy_dialog").data("kendoDialog").close();
                    $("#notification").data('kendoNotification').showText('失败！', 'error');
                }
                freshSearcherBillGrid();
                closeProgress();
            }
        });
        return true;
    }

</script>
<div id="product_dialog">

</div>
<div id="searcherMain_dialog">
    <form id="sendForm" role="form">
        <input name="searchDtls" id="searchDtls" hidden>
        <input id="toName" name="toName" hidden>

        <div class="form-group">
            <label for="toCode">收件人</label>
            <input type="text" class="form-control" id="toCode" name="toCode" width="250px" required>
        </div>
        <div class="form-group">
            <label for="remark">等级</label>
            <label class="checkbox-inline">
                <input type="radio" name="type" id="type_con" value="1" checked> 普通
            </label>
            <label class="checkbox-inline">
                <input type="radio" name="type" id="type_cur" value="2"> 紧急
            </label>
        </div>
        <div class="form-group">
            <label for="remark">备注</label>
            <textarea class="form-control" rows="3" id="remark" name="remark"></textarea>
        </div>
    </form>
</div>
<div id="progressDialog"></div>
<span id="notification"></span>
<div id="imageDialog"></div>
<div id="searcherBill_dialog"></div>
<div id="shopdialog"></div>
<div id="destroy_dialog">
    <form id="destroyForm" role="form">
        <div class="form-group">
            <label for="destroy_remark">备注</label>
            <textarea class="form-control" rows="4" id="destroy_remark" name="remark"></textarea>
        </div>
    </form>
</div>
<div id="cartDialog">
    <div align="center" class="btn btn-app btn-success" id="ace-settings-btn" onclick="openShopCartShop()"><i
            class="ace-icon fa fa-shopping-cart  bigger-330">
        <span id="shopCount" style="font-size: 16px;color:#FF3030 ">(0)</span></i></div>
</div>
<div id="searchMainDtl_dialog"></div>
<script type="text/x-kendo-template" id="template">
    <div class="tabstrip">
        <ul>
            <li class="k-state-active">
                订单信息
            </li>
        </ul>
        <div>
            <div class="#= mainId #">
                <ul>

                </ul>
            </div>
        </div>
    </div>
</script>
