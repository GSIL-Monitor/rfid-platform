<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java"%>
<div id="modal-batch-table" class="modal fade" role="dialog" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                明细列表
            </div>
        </div>
        <div class="modal-content">
            <%--<div class="modal-body">


                <div class="row">
                    <form class="form-horizontal" role="search" id="uniqCode-editForm">
                        <label class="col-sm-2 control-label no-padding-right"
                               for="add_uniqueCode">唯一码</label>
                        <div class="col-xs-12 col-sm-9">
                            <input class="form-control" id="add_uniqueCode" name="add_uniqueCode" type="text"
                                   placeholder="按回车键结束" style="ime-mode:active"/>
                        </div>

                    </form>
                </div>
            </div>--%>
            <div class="hr hr4"></div>
            <table id="batchDetailgrid"></table>
            <div id="batch-grid-pager"></div>
        </div>
        <div class="modal-footer">
            <div id="dialog_buttonGroup">
                <button id="link"  class='btn btn-primary' onclick="fullWebSocket()">连接</button>
                <button id="scanning"  class='btn btn-primary' onclick="onScanning()">扫描</button>
                <button id="stop"  class='btn btn-primary' onclick="stop()">停止</button>
                <button id="clear"  class='btn btn-primary' onclick="onClear()">清空</button>
                <button id="saveEPC"  class='btn btn-primary' onclick="saveEPC()">保存</button>
            </div>
        </div>
    </div>
</div>
<script>
    var skuInfo = [];
    var timeout;
    var websocket;
    $(function () {
        loadTable();
        //得到表格的宽度
    });
    function fullWebSocket() {
        var wsUri ="ws://127.0.0.1:4649/csreader";
        websocket = new WebSocket(wsUri);
        websocket.onopen = function(evt) { onOpen(evt) };
        websocket.onclose = function(evt) { onClose(evt) };
        websocket.onmessage = function(evt) { onMessage(evt) };
        websocket.onerror = function(evt) { onError(evt) };
    }
    function loadTable() {
        $("#batchDetailgrid").jqGrid({
            height: 400,
            datatype: "local",
            mtype: 'POST',
            colModel: [
                {name: 'billId', label: 'billId', hidden: true},
                {name: 'billNo', label: 'billNo', hidden: true},
                {name: 'status', hidden: true},
                {name: 'inStatus', hidden: true},
                {name: 'outStatus', hidden: true},
                {name: "operation", label: "操作",hidden:true},
                {name: 'statusImg', label: '状态',hidden:true},
                {name: 'inStatusImg', label: '入库状态',hidden:true},
                {name: 'outStatusImg', label: '出库状态',hidden:true},
                {name: 'styleId', label: '款号',width: 50,
                    cellattr:function(rowId, val, rawObject, cm, rdata) {
                        if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                            return "style='color:red;'";
                        }
                    }
                },
                {name: 'styleName', label: '款名', hidden:true},
                {name: 'colorId', label: '色码', width: 50,
                    cellattr:function(rowId, val, rawObject, cm, rdata) {
                        if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                            return "style='color:red;'";
                        }
                    }
                },
                {name: 'colorName', label: '颜色',hidden:true},
                {name: 'sizeId', label: '尺码',hidden:true},
                {name: 'sizeName', label: '尺码', width: 50,
                    cellattr:function(rowId, val, rawObject, cm, rdata) {
                        if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                            return "style='color:red;'";
                        }
                    }
                },
                {name: 'qty', label: '数量', width: 50,
                    cellattr:function(rowId, val, rawObject, cm, rdata) {
                        if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                            return "style='color:red;'";
                        }
                    }
                },
                {name: 'outQty', label: '已出库数量',hidden:true},
                {name: 'inQty', label: '已入库数量',hidden:true},
                {name: 'sku', label: 'SKU', width: 50,
                    cellattr:function(rowId, val, rawObject, cm, rdata) {
                        if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                            return "style='color:red;'";
                        }
                    }
                },
                {
                    name: 'price', label: '销售价格', width: 75,
                    editrules: {
                        number: true
                    },
                    formatter: function (cellValue, options, rowObject) {
                        return parseFloat(cellValue).toFixed(2);
                    },
                    cellattr:function(rowId, val, rawObject, cm, rdata) {
                        if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                            return "style='color:red;'";
                        }
                    }
                },
                {name: 'totPrice', label: '销售金额',width: 75,
                    formatter: function (cellValue, options, rowObject) {
                        return parseFloat(cellValue).toFixed(2);
                    },
                    cellattr:function(rowId, val, rawObject, cm, rdata) {
                        if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                            return "style='color:red;'";
                        }
                    }
                },
                {
                    name: 'discount', label: "折扣", hidden:true, editable: true,
                    editrules: {
                        number: true,
                        minValue: 0,
                        maxValue: 100
                    },
                    cellattr:function(rowId, val, rawObject, cm, rdata) {
                        if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                            return "style='color:red;'";
                        }
                    }
                },
                {
                    name: 'actPrice', label: '实际价格', editable: true,width:75,
                    editrules: {
                        number: true,
                        minValue: 0
                    },
                    formatter: function (cellValue, options, rowObject) {
                        return parseFloat(cellValue).toFixed(2);
                    },
                    cellattr:function(rowId, val, rawObject, cm, rdata) {
                        if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                            return "style='color:red;'";
                        }
                    }
                },
                {name: 'totActPrice', label: '实际金额', width:75,
                    formatter: function (cellValue, options, rowObject) {
                        return parseFloat(cellValue).toFixed(2);
                    },
                    cellattr:function(rowId, val, rawObject, cm, rdata) {
                        if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                            return "style='color:red;'";
                        }
                    }
                },
                {name: 'puPrice', label: '门店批发价', hidden: true},
                {name: 'uniqueCodes', label: '唯一码', hidden: true},
                {name: 'noOutPutCode', label: '异常唯一码', hidden: true}
            ],
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum: -1,
            pager: '#batch-grid-pager',
            multiselect: false,
            shrinkToFit: true,
            sortname: 'id',
            sortorder: "asc",
            footerrow: true,
            cellsubmit: 'clientArray',
            afterEditCell: function (rowid, celname, value, iRow, iCol) {
               /* addDetailgridiRow = iRow;
                addDetailgridiCol = iCol;*/
            },
            gridComplete: function () {
                setFooterData();
            },
            loadComplete : function(){
                var table = this;
                setTimeout(function(){
                    //加载完成后，替换分页按钮图标
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0)
            }
        });

    }
    function setFooterData() {
        var sum_qtymin = $("#batchDetailgrid").getCol('qty', false, 'sum');
        var sum_totActPricemin = $("#batchDetailgrid").getCol('totActPrice', false, 'sum');
        var sum_totPrice = $("#batchDetailgrid").getCol('totPrice',false,'sum');
        sum_totActPrice = sum_totActPricemin.toFixed(0);
        $("#batchDetailgrid").footerData('set', {
            styleId: "合计",
            qty: sum_qtymin,
            totPrice:Math.abs(sum_totPrice),
            totActPrice: Math.abs(sum_totActPricemin)
        });
       /* $("#discount_Amount").val(sum_totActPrice);
        localStorage.setItem("search_actPrice",sum_totActPrice);
        $("#sum_totPrice").val(sum_totPrice);*/
    }
    function updatePagerIcons(table) {
        //ui-icon ui-icon-circlesmall-minus
        var replacement =
            {
                'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
                'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
                'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
                'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
            };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

            if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
        });
    }
    function enableTooltips(table) {
        $('.navtable .ui-pg-button').tooltip({
            container : 'body'
        });
        $(table).find('.ui-pg-div').tooltip({
            container : 'body'
        });
    }

    /*
     扫描
     */
    function onScanning() {
        var msg={
            "cmd":"10002"
        };
        sendMessgeToServer(msg);
    }
    function onOpen(evt) {
        $.gritter.add({
            text: "连接 Reader Server成功",
            class_name: 'gritter-success  gritter-light'
        });
    }
    function onClose(evt) {
        if(evt.code == 1005){
            bootbox.alert("与服务器连接已断开");
        }else if(evt.code == 1006){
            bootbox.alert("连接服务器失败");
        }
    }
    function onMessage(evt) {
        var res = JSON.parse(evt.data);
        /*var unicodes = [];
        var productListInfo = [];*/
        if (res.cmd === "10006") {
            $.each(res.data,function (index,value) {
                if (value!==null&&value.skuInfo!==null){
                    skuInfo.push(value.skuInfo);
                }
            });
        }
    }
    function onError(evt) {
        bootbox.alert("发生错误");
    }
    /*
     停止
     */
    function stop() {
        if (timeout !== null) {
            window.clearInterval(timeout);
        }
        var msg={
            "cmd":"10003"
        };
        sendMessgeToServer(msg);
        //检查出入库
        checkCode();
    }

    function sendMessgeToServer(message) {
        if (typeof websocket==="undefined"){
            bootbox.alert("websocket还没有连接，或者连接失败，请检测");
            return false;
        }
        if (websocket.readyState===3) {
            bootbox.alert("websocket已经关闭，请重新连接");
            return false;
        }
        websocket.send(JSON.stringify(message));
    }
    function checkCode() {
        var progressDialog = bootbox.dialog({
            message: '<p><i class="fa fa-spin fa-spinner"></i> 正在...</p>'
        });
        //获取说有的code
        var codeArray=[];
        $.each(skuInfo, function (index, value) {
            var productInfo = value;
            if(productInfo.unicode!==""&&productInfo.unicode!==undefined){
                codeArray.push(productInfo.unicode);

            }
        });
        var ajax_url;
        var ajax_data;
        ajax_url = basePath + "/stock/warehStock/checkUniqueCodes.do";
        ajax_data = {uniqueCodes: JSON.stringify(codeArray), warehouseId: wareHouse, type: taskType, billNo: billNo, isAdd: true,rfidType:"code"};
        $.ajax({
            async: false,
            url: ajax_url,
            data: ajax_data,
            datatype: "json",
            type: "POST",
            success: function (data) {
                if (data.success) {
                    //填充数据
                    fullGridData(data.result);
                    progressDialog.modal('hide');
                } else {
                    progressDialog.modal('hide');
                    $.gritter.add({
                        text: data.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                    delete data.result["id"];

                }
                progressDialog.modal('hide');
            }
        });
        progressDialog.modal('hide');

    }
    /**
     * 检测完出入库后填充表格数据
     */
    function fullGridData(result) {
        //得到校验正确唯一码
        var rightEpc=result.rightEpc;
        //得到校验未通过唯一码
        var errorEpc=result.errorEpc;
        //先做校验正确唯一码
        var skuMap = new Map();
        $.each(rightEpc,function (index,value) {
            if (!skuMap.has(value.sku)){
                var skuEpc={};
                skuEpc.sku=value.sku;
                skuEpc.styleId=value.styleId;
                skuEpc.colorId=value.colorId;
                skuEpc.sizeId=value.sizeId;
                skuEpc.styleName=value.styleName;
                skuEpc.colorName=value.colorName;
                skuEpc.sizeName=value.sizeName;
                skuEpc.preCast=value.preCast;
                skuEpc.price=value.price;
                skuEpc.puPrice=value.puPrice;
                skuEpc.wsPrice=value.wsPrice;
                skuEpc.bargainPrice=value.bargainPrice;
                skuEpc.uniqueCodes=value.code;
                skuEpc.discount=100;
                skuEpc.outQty = 0;
                skuEpc.inQty = 0;
                skuEpc.status = 0;
                skuEpc.inStatus = 0;
                skuEpc.outStatus = 0;
                skuEpc.qty = 1;
                skuEpc.actPrice = value.price*skuEpc.discount;
                skuEpc.totPrice = value.price*skuEpc.qty;
                skuEpc.totActPrice = value.price*skuEpc.qty*skuEpc.discount;
                skuMap.set(value.sku,skuEpc);
            }else {
                var exist=skuMap.get(value.sku);
                exist.qty+=1;
                exist.totPrice = exist.price*exist.qty;
                exist.totActPrice = exist.actPrice*exist.qty*exist.discount;
                if( exist.noOutPutCode!=""&&exist.noOutPutCode!=undefined){
                    exist.noOutPutCode =  exist.noOutPutCode+","+rightEpc[i].code ;
                }else{
                    exist.noOutPutCode =  rightEpc[i].code ;
                }
                skuMap.set(value.sku,exist);
            }
        });

        $.each(errorEpc,function (index,value) {
            if (!skuMap.has(value.sku)){
                var skuEpc={};
                skuEpc.sku=value.sku;
                skuEpc.styleId=value.styleId;
                skuEpc.colorId=value.colorId;
                skuEpc.sizeId=value.sizeId;
                skuEpc.styleName=value.styleName;
                skuEpc.colorName=value.colorName;
                skuEpc.sizeName=value.sizeName;
                skuEpc.preCast=value.preCast;
                skuEpc.price=value.price;
                skuEpc.puPrice=value.puPrice;
                skuEpc.wsPrice=value.wsPrice;
                skuEpc.bargainPrice=value.bargainPrice;
                skuEpc.noOutPutCode=value.code;
                skuEpc.discount=100;
                skuEpc.outQty = 0;
                skuEpc.inQty = 0;
                skuEpc.status = 0;
                skuEpc.inStatus = 0;
                skuEpc.outStatus = 0;
                skuEpc.qty = 1;
                skuEpc.actPrice = value.price*skuEpc.discount;
                skuEpc.totPrice = value.price*skuEpc.qty;
                skuEpc.totActPrice = value.price*skuEpc.qty*skuEpc.discount;
                skuMap.set(value.sku,skuEpc);
            }else {
                var exist=skuMap.get(value.sku);
                exist.qty+=1;
                exist.totPrice = exist.price*exist.qty;
                exist.totActPrice = exist.actPrice*exist.qty*exist.discount;
                if( exist.noOutPutCode!=""&&exist.noOutPutCode!=undefined){
                    exist.noOutPutCode =  exist.noOutPutCode+","+rightEpc[i].code ;
                }else{
                    exist.noOutPutCode =  rightEpc[i].code ;
                }
                skuMap.set(value.sku,exist);
            }
        });
        /*for(var i=0;i<rightEpc.length;i++){
            if(i===0){
                var skuMap={};
                var skuEpc={};
                skuEpc.sku=rightEpc[i].sku;
                skuEpc.styleId=rightEpc[i].styleId;
                skuEpc.colorId=rightEpc[i].colorId;
                skuEpc.sizeId=rightEpc[i].sizeId;
                skuEpc.styleName=rightEpc[i].styleName;
                skuEpc.colorName=rightEpc[i].colorName;
                skuEpc.sizeName=rightEpc[i].sizeName;
                skuEpc.preCast=rightEpc[i].preCast;
                skuEpc.price=rightEpc[i].price;
                skuEpc.puPrice=rightEpc[i].puPrice;
                skuEpc.wsPrice=rightEpc[i].wsPrice;
                skuEpc.bargainPrice=rightEpc[i].bargainPrice;
                skuEpc.uniqueCodes=rightEpc[i].code;
                skuEpc.discount=100;
                skuEpc.outQty = 0;
                skuEpc.inQty = 0;
                skuEpc.status = 0;
                skuEpc.inStatus = 0;
                skuEpc.outStatus = 0;
                skuEpc.qty = 1;
                skuEpc.totPrice = rightEpc[i].price*skuEpc.qty;
                skuEpc.totActPrice = rightEpc[i].price*skuEpc.qty*skuEpc.discount;
                skuMap.sku=rightEpc[i].sku;
                skuMap.values=skuEpc;
                skuEpcList.push(skuMap)
            }else{
                //判断该sku是否在skuEpcList中
                var isHave=false;
                for(var a=0;a<skuEpcList.length;a++){
                   if(rightEpc[i].sku==skuEpcList[a].sku){
                       skuEpcList[a].values.qty= skuEpcList[a].values.qty+1;
                       skuEpcList[a].values.totPrice = skuEpcList[a].values.price*skuEpcList[a].values.qty;
                       skuEpcList[a].values.totActPrice = skuEpcList[a].values.actPrice*skuEpcList[a].values.qty*skuEpcList[a].values.discount;
                       if( skuEpcList[a].values.noOutPutCode!=""&&skuEpcList[a].values.noOutPutCode!=undefined){
                           skuEpcList[a].values.noOutPutCode =  skuEpcList[a].values.noOutPutCode+","+rightEpc[i].code ;
                       }else{
                           skuEpcList[a].values.noOutPutCode =  rightEpc[i].code ;
                       }
                       isHave=true;
                   }
                }
                if(isHave==false){
                    var skuMap={};
                    var skuEpc={};
                    skuEpc.sku=rightEpc[i].sku;
                    skuEpc.styleId=rightEpc[i].styleId;
                    skuEpc.colorId=rightEpc[i].colorId;
                    skuEpc.sizeId=rightEpc[i].sizeId;
                    skuEpc.styleName=rightEpc[i].styleName;
                    skuEpc.colorName=rightEpc[i].colorName;
                    skuEpc.sizeName=rightEpc[i].sizeName;
                    skuEpc.preCast=rightEpc[i].preCast;
                    skuEpc.price=rightEpc[i].price;
                    skuEpc.puPrice=rightEpc[i].puPrice;
                    skuEpc.wsPrice=rightEpc[i].wsPrice;
                    skuEpc.bargainPrice=rightEpc[i].bargainPrice;
                    skuEpc.noOutPutCode=rightEpc[i].code;
                    skuEpc.discount=100;
                    skuEpc.outQty = 0;
                    skuEpc.inQty = 0;
                    skuEpc.status = 0;
                    skuEpc.inStatus = 0;
                    skuEpc.outStatus = 0;
                    skuEpc.qty = 1;
                    skuEpc.totPrice = rightEpc[i].price*skuEpc.qty;
                    skuEpc.totActPrice = rightEpc[i].price*skuEpc.qty*skuEpc.discount;
                    skuMap.sku=rightEpc[i].sku;
                    skuMap.values=skuEpc;
                    skuEpcList.push(skuMap)
                }
            }
        }
        //在做校验未通过唯一码
        for(var b=0;b<errorEpc.length;b++){
            //判断该sku是否在skuEpcList中
            var isHave=false;
            for(var c=0;c<skuEpcList.length;c++){
                if(rightEpc[b].sku==skuEpcList[c].sku){
                    skuEpcList[c].values.qty= skuEpcList[c].values.qty+1;
                    skuEpcList[c].values.totPrice = skuEpcList[c].values.price*skuEpcList[c].values.qty;
                    skuEpcList[c].values.totActPrice = skuEpcList[c].values.actPrice*skuEpcList[c].values.qty*skuEpcList[c].values.discount;
                    skuEpcList[c].values.uniqueCodes =  skuEpcList[c].values.uniqueCodes+","+rightEpc[b].code ;
                    isHave=true;
                }
            }
            if(isHave==false){
                var skuMap={};
                var skuEpc={};
                skuEpc.sku=errorEpc[b].sku;
                skuEpc.styleId=errorEpc[b].styleId;
                skuEpc.colorId=errorEpc[b].colorId;
                skuEpc.sizeId=errorEpc[b].sizeId;
                skuEpc.styleName=errorEpc[b].styleName;
                skuEpc.colorName=errorEpc[b].colorName;
                skuEpc.sizeName=errorEpc[b].sizeName;
                skuEpc.preCast=errorEpc[b].preCast;
                skuEpc.price=errorEpc[b].price;
                skuEpc.puPrice=errorEpc[b].puPrice;
                skuEpc.wsPrice=errorEpc[b].wsPrice;
                skuEpc.bargainPrice=errorEpc[b].bargainPrice;
                skuEpc.noOutPutCode=errorEpc[b].code;
                skuEpc.discount=100;
                skuEpc.outQty = 0;
                skuEpc.inQty = 0;
                skuEpc.status = 0;
                skuEpc.inStatus = 0;
                skuEpc.outStatus = 0;
                skuEpc.qty = 1;
                skuEpc.totPrice = errorEpc[b].price*skuEpc.qty;
                skuEpc.totActPrice = errorEpc[b].price*skuEpc.qty*skuEpc.discount;
                skuMap.sku=errorEpc[b].sku;
                skuMap.values=skuEpc;
                skuEpcList.push(skuMap)
            }
        }*/
        skuMap.forEach(function (value, key, map) {
            $("#batchDetailgrid").addRowData($("#batchDetailgrid").getDataIDs().length,value);
        });
        skuInfo=[];
    }


    function saveEPC() {
        var IDs=$("#batchDetailgrid").getDataIDs();
        $.each(IDs,function (index,value) {
            var rowData=$("#batchDetailgrid").jqGrid('getRowData',value);
            $("#addDetailgrid").addRowData($("#addDetailgrid").getDataIDs().length, rowData);
        });
        $("#modal-batch-table").modal('hide');
    }


    function onClear() {
        $("#addDetailgrid").clearGridData();
    }


</script>