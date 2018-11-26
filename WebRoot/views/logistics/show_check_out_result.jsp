<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="modal-batch-show-table" class="modal fade" role="dialog" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                出库批量扫码页面
            </div>
        </div>
        <div class="modal-content">
            <div class="widget-box transparent">
                <div class="widget-header ">
                    <div class="widget-toolbar no-border">
                        <ul class="nav nav-tabs" id="myTab">
                            <li class="active">
                                <a data-toggle="tab" href="#billInformation">单据信息</a>
                            </li>
                            <li>
                                <a data-toggle="tab" href="#notThisOne">非本单商品</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="widget-body">
                    <div class="widget-main padding-12 no-padding-left no-padding-right">
                        <div class="tab-content padding-4">
                            <div id="billInformation" class="tab-pane in active">
                                <table id="billInformationOutgrid"></table>
                                <div id="billInformationOutgrid-pager"></div>
                            </div>
                            <div id="notThisOne" class="tab-pane">
                                <table id="notThisOneOutgrid"></table>
                                <div id="notThisOneOutgrid-pager"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <div id="dialog_buttonGroup">
                        <button id="link"  class='btn btn-primary' onclick="fullOutWebSocket()">连接</button>
                        <button id="scanningOut"  class='btn btn-primary' onclick="onScanningOut()">扫描</button>
                        <button id="stopOut"  class='btn btn-primary' onclick="stopOut()">停止</button>
                        <button id="saveOut"  class='btn btn-primary' onclick="saveOut()">保存</button>
                        <button id="clearOut"  class='btn btn-primary' onclick="onClearOut()">清空</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    var skuInfo = [];
    var timeoutOut;
    var websocket;
    $(function () {

        loadbillInformationOutTable();
        loadnotThisOnegridOutTable()

    });
    function fullOutWebSocket() {
        var wsUri ="ws://127.0.0.1:4649/csreader";
        websocket = new WebSocket(wsUri);
        websocket.onopen = function(evt) { onOpenOut(evt) };
        websocket.onclose = function(evt) { onCloseOut(evt) };
        websocket.onmessage = function(evt) { onMessageOut(evt) };
        websocket.onerror = function(evt) { onErrorOut(evt) };
    }
    /*
     扫描
     */
    function onScanningOut() {
        var msg={
            "cmd":"10002"
        };
        sendMessgeOutToServer(msg);
    }
    function onOpenOut(evt) {
        /*showMessage("连接 Reader Server成功");*/
    }
    function onCloseOut(evt) {
        /*if(evt.code == 1005){
         /!*showMessage('与服务器连接已断开');*!/
         }else if(evt.code == 1006){
         /!*showMessage('连接服务器失败',true);*!/
         }*/
    }
    function onMessageOut(evt) {

        var res = JSON.parse(evt.data);

        if (res.cmd === "10006") {
            $.each(res.data,function (index,value) {
                if (value!==null&&value.skuInfo!==null){
                    skuInfo.push(value.skuInfo);
                }
            });
        return false;
        } else {

        }
    }
    function onErrorOut(evt) {
        /*showMessage('发生错误' + evt.data,true);*/
    }
    /*
     停止
     */
    function stopOut() {
        if (timeoutOut !== null) {
            window.clearInterval(timeoutOut);
        }
        var msg={
            "cmd":"10003"
        };
        sendMessgeOutToServer(msg);
        //检查出入库
        checkOutCode();
    }
    function checkOutCode() {
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
                    fullOutGridData(data.result);
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
    function fullOutGridData(result) {
        //得到校验正确唯一码
        var rightEpc=result.rightEpc;
        //得到校验未通过唯一码
        var errorEpc=result.errorEpc;
        //得到校验非本单商品唯一码
        var noInBill=result.noInBill;
        //添加校验正确的唯一码到billInformationOutgrid中
        $.each($("#billInformationOutgrid").getDataIDs(), function (dtlIndex, dtlValue) {
            var dtlRow = $("#billInformationOutgrid").getRowData(dtlValue);
             for(var i=0;i<rightEpc.length;i++){
                 if(dtlValue.sku==rightEpc[i].sku){
                     if(dtlRow.uniqueCodes!=""&&dtlRow.uniqueCodes!=undefined){
                         dtlRow.uniqueCodes=dtlRow.uniqueCodes+","+rightEpc[i].code;
                         dtlRow.thisQty= dtlRow.thisQty+1;
                     }else{
                         dtlRow.uniqueCodes=rightEpc[i].code;
                         dtlRow.thisQty= dtlRow.thisQty+1;
                     }
                     $("#billInformationOutgrid").setRowData(dtlRow.id, dtlRow);
                 }

             }
        });
        //添加校验未通过唯一码和非本单商品唯一码到notThisOneOutgrid中
        //定义一个根据sku做的唯一码和非本单商品唯一码汇总的对象
        var skuEpcList=[];
        $.each(errorEpc, function (index, value) {
            if(index===0){
                var skuMap={};
                var skuEpc={};
                skuEpc.sku=value.sku;
                skuEpc.styleId=value.styleId;
                skuEpc.styleName=value.styleName;
                skuEpc.colorId=value.colorId;
                skuEpc.colorName=value.colorName;
                skuEpc.sizeId=value.sizeId;
                skuEpc.sizeName=value.sizeName;
                skuEpc.qty=1;
                skuEpc.uniqueCodes=value.code;
                skuMap.sku=value.sku;
                skuMap.values=skuEpc;
                skuEpcList.push(skuMap);
            }else{
                //判断该sku是否在skuEpcList中
                var isHave=false;
                for(var i=0;i<skuEpcList.length;i++){
                    if(value.sku==skuEpcList[i].sku){
                        skuEpcList[i].values.qty=skuEpcList[i].values.qty+1;
                        skuEpcList[i].values.uniqueCodes=skuEpcList[i].values.uniqueCodes+","+value.code;
                        isHave=true;
                    }
                }
                if(isHave==false){
                    var skuMap={};
                    var skuEpc={};
                    skuEpc.sku=value.sku;
                    skuEpc.styleId=value.styleId;
                    skuEpc.styleName=value.styleName;
                    skuEpc.colorId=value.colorId;
                    skuEpc.colorName=value.colorName;
                    skuEpc.sizeId=value.sizeId;
                    skuEpc.sizeName=value.sizeName;
                    skuEpc.qty=1;
                    skuEpc.uniqueCodes=value.code;
                    skuMap.sku=value.sku;
                    skuMap.values=skuEpc;
                    skuEpcList.push(skuMap);
                }
            }
        });
        $.each(skuEpcList, function (index, value) {
            var values=value.values;
            $("#notThisOneOutgrid").addRowData($("#notThisOneOutgrid").getDataIDs().length, values);
        });
    }
    function onClearOut() {
        $("#billInformationgrid").clearGridData();
        $("#notThisOnegrid").clearGridData();
    }

    function sendMessgeOutToServer(message) {

        if (typeof websocket==="undefined"){
            /* showMessage('websocket还没有连接，或者连接失败，请检测',true);*/
            return false;
        }
        if (websocket.readyState===3) {
            /*showMessage('websocket已经关闭，请重新连接',true);*/
            return false;
        }
        console.log(websocket);
        var data = websocket.send(JSON.stringify(message));
        console.log(data);
       
    }
    function loadbillInformationOutTable() {
        $("#billInformationOutgrid").jqGrid({
            height: 400,
            datatype: "local",
            mtype: 'POST',
            colModel: [
                {name: 'id', label: 'id', hidden: true},
                {name: 'billId', label: 'billId', hidden: true},
                {name: 'billNo', label: 'billNo', hidden: true},
                {name: 'status', hidden: true},
                {name: 'inStatus', hidden: true},
                {name: 'outStatus', hidden: true},
                {name: 'styleId', label: '款号',width: 60},
                {name: 'styleName', label: '款名', hidden:true},
                {name: 'colorId', label: '色码', width: 60},
                {name: 'colorName', label: '颜色',hidden:true},
                {name: 'sizeId', label: '尺码',hidden:true},
                {name: 'sizeName', label: '尺码', width: 60},
                {name: 'sku', label: 'SKU', width: 60},
                {name: 'qty', label: '单据数量', width: 60},
                {name: 'outQty', label: '出库数量', width: 60},
                {name: 'thisQty', label: '本次数量', width: 60},
                {name: 'returnQty', label: '退货数量',hidden: true},
                {name: 'price', label: '销售价格',hidden: true},
                {name: 'totPrice', label: '销售金额',hidden: true},
                {name: 'discount', label: '折扣',hidden: true},
                {name: 'actPrice', label: '实际价格',hidden: true},
                {name: 'totActPrice', label: '实际金额',hidden: true},
                {name: 'delRemark', label: '详情备注',hidden: true},
                {name: 'noOutPutCode', label: '异常唯一码', hidden: true},
                {name: 'changeTRqty', label: '转调拨单数量', hidden: true},
                {name: 'puPrice', label: '代理商批发价格', hidden: true},
                {name: 'wsPrice', label: '门店批发价格', hidden: true},
                {name: 'returnbillNo', label: '退货单号', hidden: true},
                {name:'stylePriceMap',label:'价格表',hidden:true},
                {name:'abnormalStatus',label:'异常单状态',hidden:true},
                {name: 'uniqueCodes', label: '唯一码',hidden: true},
                {
                    name: '', label: '唯一码明细', width: 100, align: "center",
                    formatter: function (cellValue, options, rowObject) {
                        return "<a href='javascript:void(0);' onclick=showCodesOutDetail('" + rowObject.uniqueCodes + "')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
                    }
                }
            ],
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum: -1,
            pager: '#billInformationOutgrid-pager',
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
                //加载表的数据
                //lodeBillInformationOutgrid();
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
    function lodeBillInformationOutgrid() {
        $.each($("#addDetailgrid").getDataIDs(), function (dtlIndex, dtlValue) {
            var dtlRow = $("#addDetailgrid").getRowData(dtlValue);
            dtlRow.thisQty=0;
            dtlRow.uniqueCodes="";
            $("#billInformationOutgrid").addRowData($("#billInformationOutgrid").getDataIDs().length, dtlRow);
        });
    }
    function loadnotThisOnegridOutTable() {
        $("#notThisOneOutgrid").jqGrid({
            height: 400,
            datatype: "local",
            mtype: 'POST',
            colModel: [
                {name: 'styleId', label: '款号',width: 80},
                {name: 'styleName', label: '款名', hidden:true},
                {name: 'colorId', label: '色码', width: 80},
                {name: 'colorName', label: '颜色',hidden:true},
                {name: 'sizeId', label: '尺码',hidden:true},
                {name: 'sizeName', label: '尺码', width: 80},
                {name: 'sku', label: 'SKU', width: 80},
                {name: 'qty', label: '数量', width: 80},
                {name: 'uniqueCodes', label: '唯一码',hidden: true},
                {
                    name: '', label: '唯一码明细', width: 120, align: "center",
                    formatter: function (cellValue, options, rowObject) {
                        return "<a href='javascript:void(0);' onclick=showCodesDetail('" + rowObject.uniqueCodes + "')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
                    }
                }

            ],
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum: -1,
            pager: '#notThisOneOutgrid-pager',
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
    function saveOut() {
        cs.showProgressBar();
        $("#saveOut").attr({"disabled": "disabled"});
        var dtlArray = [];
        var epcArray = [];
        $.each($("#billInformationOutgrid").getDataIDs(), function (dtlIndex, dtlValue) {
            var dtlRow = $("#billInformationOutgrid").getRowData(dtlValue);
            //判断出库数量加本次数量是否大于单据数量
            if(parseInt(dtlRow.outQty)+parseInt(dtlRow.thisQty)>parseInt(dtlRow.qty)){
                $.gritter.add({
                    text: dtlRow.sku+"要出库的数量超过本单数量"+(parseInt(dtlRow.outQty)+parseInt(dtlRow.thisQty)-parseInt(dtlRow.qty))+"件",
                    class_name: 'gritter-success  gritter-light'
                });
            }else {
                dtlRow.outQty=parseInt(dtlRow.outQty)+parseInt(dtlRow.thisQty);
                dtlArray.push(dtlRow);
                //填充epcArray的数组
                var Codes=dtlRow.uniqueCodes.split(",");
                for(var m=0;m<Codes.length;m++){
                    var rowData={};
                    rowData.code=Codes[m];
                    rowData.styleId=dtlRow.styleId;
                    rowData.colorId=dtlRow.colorId;
                    rowData.sizeId=dtlRow.sizeId;
                    rowData.sku=dtlRow.sku;
                    rowData.styleName=dtlRow.styleName;
                    rowData.colorName=dtlRow.sizeName;
                    rowData.price=dtlRow.price;
                    rowData.preCast=dtlRow.preCast;
                    rowData.wsPrice=dtlRow.wsPrice;
                    epcArray.push(rowData);
                }
            }
        });
        if (epcArray.length == 0) {
            bootbox.alert("请添加唯一码!");
            $("#saveOut").removeAttr("disabled");
            cs.closeProgressBar();
            return;
        }
        $.ajax({
            dataType: "json",
            url: basePath + "/logistics/saleOrderBill/convertOut.do",
            data: {
                billNo: billNo,
                strEpcList: JSON.stringify(epcArray),
                strDtlList: JSON.stringify(dtlArray),
                userId: userId
            },
            type: "POST",
            success: function (msg) {
                $("#saveOut").removeAttr("disabled");
                cs.closeProgressBar();
                if (msg.success) {
                    $.gritter.add({
                        text: msg.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                    $("#addDetailgrid").trigger("reloadGrid");
                    _search();
                } else {
                    bootbox.alert(msg.msg);
                }
            }
        });
        $("#modal-batch-show-table").modal('hide');
        //wareHouseOut();
    }

</script>