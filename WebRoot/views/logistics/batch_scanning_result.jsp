<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java"%>
<div id="modal-batch-table" class="modal fade" role="dialog" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                明细列表
            </div>
        </div>
        <div class="modal-content">
            <div class="hr hr4"></div>
            <table id="batchDetailgrid"></table>
            <div id="batch-grid-pager"></div>
        </div>
        <div class="modal-footer">
            <div class="col-lg-3">
                <span style="font-size:15px">已扫到数量：</span>
                <span id="scanCodeQty" style="font-size:15px; color:tomato">0</span>
            </div>
            <div id="dialog_buttonGroup">
                <button id="scanning"  class='btn btn-primary' onclick="onScanning()">继续扫描</button>
                <button id="stop"  class='btn btn-primary' onclick="stop()">暂停</button>
                <button id="clear"  class='btn btn-primary' onclick="onClear()">清空</button>
                <button id="saveEPC"  class='btn btn-primary' onclick="saveEPC()">保存</button>
                <button id="close"  class='btn btn-primary' onclick="batchClose()">关闭</button>
            </div>
        </div>
    </div>
</div>
<script>
    var skuInfo = [];
    var oldSkuInfo = [];
    var timeout;
    var websocket;
    $(function () {
        loadTable();
        //得到表格的宽度
    });
    function initWebSocket() {
        loadingwebsocket();
        websocket.onopen = function(evt) { onOpen(evt) };
        websocket.onclose = function(evt) { onClose(evt) };
        websocket.onmessage = function(evt) { onMessage(evt) };
        websocket.onerror = function(evt) { onError(evt) };
    }
    //关闭读写器
    function batchClose() {
        var msg={
            "cmd":"10004"
        };
        sendMessgeToServer(msg);
        $("#modal-batch-table").modal('hide');
    }

    /*
     继续扫描
     */
    function onScanning() {
        var msg={
            "cmd":"10002"
        };
        sendMessgeToServer(msg);
    }

    function onOpen(evt) {
        var startMsg={
            "cmd":"10001"
        };
        if ( sendMessgeInToServer(startMsg)!=false){
            var msg={
                "cmd":"10002"
            };
            if (sendMessgeInToServer(msg)!=false){
                $.gritter.add({
                    text: "连接 Reader Server成功",
                    class_name: 'gritter-success  gritter-light'
                });
            }
        }
    }
    function onClose(evt) {
        if(evt.code === 1005){
            $.gritter.add({
                text: "与服务器连接已断开",
                class_name: 'gritter-success  gritter-light'
            });
        }else if(evt.code === 1006){
            $.gritter.add({
                text: "连接服务器失败",
                class_name: 'gritter-success  gritter-light'
            });
        }
    }
    function onMessage(evt) {
        var res = JSON.parse(evt.data);
        if (res.cmd === "10006") {
            $.each(res.data,function (index,value) {
                if (value!==null&&value.skuInfo!==null){
                    skuInfo.push(value.skuInfo);
                    changeScanCodeQty();
                }
            });
        }
        if (res.cmd==="10002"&&res.success===true){
            $.gritter.add({
                text: res.msg,
                class_name: 'gritter-success  gritter-light'
            });
        }
    }

    function changeScanCodeQty() {
        //剔除SkuInfo中相同唯一码的商品
        var num=skuInfo.length;
        $("#scanCodeQty").text(num);
    }

    function onError(evt) {
        //bootbox.alert("发生错误");
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
                {name: 'styleId', label: '款号',width: 60,
                    cellattr:function(rowId, val, rawObject, cm, rdata) {
                        if(rawObject.noOutPutCode!=""&&rawObject.noOutPutCode!=undefined){
                            return "style='color:red;'";
                        }
                    }
                },
                {name: 'styleName', label: '款名', hidden:true},
                {name: 'colorId', label: '色码', width: 30,
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
                {name: 'qty', label: '数量', width: 30,
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
                    name: 'price', label: '销售价格', width: 70,
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
                {name: 'totPrice', label: '销售金额',width: 70,
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
                    name: 'actPrice', label: '实际价格', editable: true,width:70,
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
                {name: 'totActPrice', label: '实际金额', width:70,
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
                {name: 'noOutPutCode', label: '异常唯一码', hidden: true},
                {
                    name: '', label: '码明细', width: 30, align: "center",
                    formatter: function (cellValue, options, rowObject) {
                        return "<a href='javascript:void(0);' onclick=showCodesBatchDetail('" + options.rowId + "')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
                    }
                },
                {
                    name: '', label: '异常码明细', width: 30, align: "center",
                    formatter: function (cellValue, options, rowObject) {
                        return "<a href='javascript:void(0);' onclick=showCodesNoBatchDetail('" + options.rowId + "')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
                    }
                },
                {name: 'warehouseId', hidden: true},
                {name: 'floor', hidden: true},
                {name: 'inStock', hidden: true}
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
                setBatchFooterData();
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
        var parent_column = $("#batchDetailgrid").closest('.modal-dialog');
        console.log(parent_column.width());
        $("#batchDetailgrid").jqGrid('setGridWidth', parent_column.width() - 5);

    }
    function setBatchFooterData() {
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



    //是否能出入库
    function checkCode() {
        var progressDialog = bootbox.dialog({
            message: '<p><i class="fa fa-spin fa-spinner"></i> 正在...</p>'
        });
        //校验新扫到的唯一码
        if (oldSkuInfo.length!=0){
            $.each(skuInfo,function (index,value) {
                $.each(oldSkuInfo,function (oldIndex,oldValue) {
                    if (value.unicode==oldValue.unicode){
                        value.isExist=true;
                    }
                })
            });
        }
        oldSkuInfo=JSON.parse(JSON.stringify(skuInfo));
        //获取说有的code
        var codeArray=[];
        $.each(skuInfo, function (index, value) {
            if (value.isExist==undefined||value.isExist!=true){
                var productInfo = value;
                if(productInfo.unicode!==""&&productInfo.unicode!==undefined){
                    codeArray.push(productInfo.unicode);
                }
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
                    skuInfo=[];
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
        var ct = $("#edit_customerType").val();
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
                if (ct == "CT-AT") {//省代价格
                    value.price = value.puPrice;
                } else if (ct == "CT-ST") {//门店价格
                    value.price = value.wsPrice;
                }
                skuEpc.price=value.price;
                skuEpc.puPrice=value.puPrice;
                skuEpc.wsPrice=value.wsPrice;
                skuEpc.bargainPrice=value.bargainPrice;
                skuEpc.uniqueCodes=value.code;
                var discount;
                if ($("#edit_discount").val() && $("#edit_discount").val() !== null) {
                    discount = $("#edit_discount").val();
                } else {
                    discount = 100;
                }
                skuEpc.discount=discount;
                skuEpc.outQty = 0;
                skuEpc.inQty = 0;
                skuEpc.status = 0;
                skuEpc.inStatus = 0;
                skuEpc.outStatus = 0;
                skuEpc.qty = 1;
                skuEpc.actPrice =Math.round(value.price*skuEpc.discount)/100;
                skuEpc.totPrice = skuEpc.price*skuEpc.qty;
                skuEpc.totActPrice = Math.round(skuEpc.price*skuEpc.qty*skuEpc.discount)/100;
                //为唯一码展示
                skuEpc.floor=value.floor;
                skuEpc.inStock=value.inStock;
                skuEpc.warehouseId=value.warehouseId;
                skuMap.set(value.sku,skuEpc);
            }else {
                var exist=skuMap.get(value.sku);
                exist.qty+=1;
                exist.totPrice = exist.price*exist.qty;
                exist.totActPrice = Math.round(exist.actPrice*exist.qty*exist.discount)/100;
                if( exist.uniqueCodes!=""&&exist.uniqueCodes!=undefined){
                    exist.uniqueCodes =  exist.uniqueCodes+","+value.code ;
                }else{
                    exist.uniqueCodes = value.code ;
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
                if (ct == "CT-AT") {//省代价格
                    value.price = value.puPrice;
                } else if (ct == "CT-ST") {//门店价格
                    value.price = value.wsPrice;
                }
                skuEpc.price=value.price;
                skuEpc.puPrice=value.puPrice;
                skuEpc.wsPrice=value.wsPrice;
                skuEpc.bargainPrice=value.bargainPrice;
                skuEpc.noOutPutCode=value.code;
                var discount;
                if ($("#edit_discount").val() && $("#edit_discount").val() !== null) {
                    discount = $("#edit_discount").val();
                } else {
                    discount = 100;
                }
                skuEpc.discount=discount;
                skuEpc.outQty = 0;
                skuEpc.inQty = 0;
                skuEpc.status = 0;
                skuEpc.inStatus = 0;
                skuEpc.outStatus = 0;
                skuEpc.qty = 1;
                skuEpc.actPrice = Math.round(value.price*skuEpc.discount)/100;
                skuEpc.totPrice = value.price*skuEpc.qty;
                skuEpc.totActPrice = Math.round(value.price*skuEpc.qty*skuEpc.discount)/100;
                //为唯一码展示
                skuEpc.floor=value.floor;
                skuEpc.inStock=value.inStock;
                skuEpc.warehouseId=value.warehouseId;
                skuMap.set(value.sku,skuEpc);
            }else {
                var exist=skuMap.get(value.sku);
                exist.qty+=1;
                exist.totPrice = exist.price*exist.qty;
                exist.totActPrice = Math.round(exist.actPrice*exist.qty*exist.discount)/100;
                if( exist.noOutPutCode!=""&&exist.noOutPutCode!=undefined){
                    exist.noOutPutCode =  exist.noOutPutCode+","+value.code ;
                }else{
                    exist.noOutPutCode =  value.code ;
                }
                skuMap.set(value.sku,exist);
            }
        });
        skuMap.forEach(function (value, key, map) {
            $("#batchDetailgrid").addRowData($("#batchDetailgrid").getDataIDs().length,value);
        });
        skuInfo=[];
        $("#scanCodeQty").text(0);
    }

    function onClear() {
        skuInfo=[];
        $("#batchDetailgrid").clearGridData();
        var msg={
            "cmd":"10005"
        };
        sendMessgeToServer(msg);
        $("#scanCodeQty").text(0);
    }

    function showCodesBatchDetail(rowId) {
        $("#show-allUniqueCode-list").modal('show').on('hidden.bs.modal',function () {
            $("#allUniqueCodeListGrid").jqGrid('clearGridData');//清空表格
        });
        batchCodeDetail(rowId,"batchDetailgrid");
    }

    function showCodesNoBatchDetail(rowId) {
        $("#show-allUniqueCode-list").modal('show').on('hidden.bs.modal',function () {
            $("#allUniqueCodeListGrid").jqGrid('clearGridData');//清空表格
        });
        batchNoCodeDetail(rowId,"batchDetailgrid");
    }


</script>