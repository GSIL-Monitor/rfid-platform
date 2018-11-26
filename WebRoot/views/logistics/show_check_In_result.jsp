<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="modal-batch-show-In-table" class="modal fade" role="dialog" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                入库批量扫码页面
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
                                <table id="billInformationIngrid"></table>
                                <div id="billInformationIngrid-pager"></div>
                            </div>
                            <div id="notThisOne" class="tab-pane">
                                <table id="notThisOneIngrid"></table>
                                <div id="notThisOneIngrid-pager"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <div id="dialog_buttonGroup">
                        <button id="linkIn"  class='btn btn-primary' onclick="fullWebInSocket()">连接</button>
                        <button id="scanningIn"  class='btn btn-primary' onclick="onScanningIn()">扫描</button>
                        <button id="stopIn"  class='btn btn-primary' onclick="stopIn()">停止</button>
                        <button id="saveIn"  class='btn btn-primary' onclick="saveIn()">保存</button>
                        <button id="clearIn"  class='btn btn-primary' onclick="onClearIn()">清空</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    var skuInfoIn = [];
    var timeoutIn;
    var websocket;
    $(function () {
        loadbillInformationInTable();
        loadnotThisOnegridInTable()
        //得到表格的宽度
    });
    function fullWebInSocket() {
        var wsUri ="ws://127.0.0.1:4649/csreader";
        websocket = new WebSocket(wsUri);
        websocket.onopen = function(evt) { onOpenIn(evt) };
        websocket.onclose = function(evt) { onCloseIn(evt) };
        websocket.onmessage = function(evt) { onMessageIn(evt) };
        websocket.onerror = function(evt) { onErrorIn(evt) };
    }
    /*
     扫描
     */
    function onScanningIn() {
        var msg={
            "cmd":"10002"
        };
        sendMessgeToServer(msg);
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
    function onOpenIn(evt) {
        /*showMessage("连接 Reader Server成功");*/
    }
    function onCloseIn(evt) {
        /*if(evt.code == 1005){
         /!*showMessage('与服务器连接已断开');*!/
         }else if(evt.code == 1006){
         /!*showMessage('连接服务器失败',true);*!/
         }*/
    }
    function onMessageIn(evt) {

        var res = JSON.parse(evt.data);

        if (res.cmd === "10006") {
            $.each(res.data,function (index,value) {
                if (value!==null&&value.skuInfo!==null){
                    skuInfoIn.push(value.skuInfo);
                }
            });

        } else {

        }
    }
    function onErrorIn(evt) {
        /*showMessage('发生错误' + evt.data,true);*/
    }
    /*
     停止
     */
    function stopIn() {
        if (skuInfoIn !== null) {
            window.clearInterval(timeoutOut);
        }
        var msg={
            "cmd":"10003"
        };
        sendMessgeOutToServer(msg);
        //检查出入库
        checkInCode();
    }
    function checkInCode() {
        //获取说有的code
        var progressDialog = bootbox.dialog({
            message: '<p><i class="fa fa-spin fa-spinner"></i> 正在...</p>'
        });
        var codeArray=[];
        $.each(skuInfoIn, function (index, value) {
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
                    fullInGridData(data.result);
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
    function fullInGridData(result) {
        //得到校验正确唯一码
        var rightEpc=result.rightEpc;
        //得到校验未通过唯一码
        var errorEpc=result.errorEpc;
        //得到校验非本单商品唯一码
        var noInBill=result.noInBill;
        //添加校验正确的唯一码到billInformationOutgrid中
        $.each($("#billInformationIngrid").getDataIDs(), function (dtlIndex, dtlValue) {
            var dtlRow = $("#billInformationIngrid").getRowData(dtlValue);
            for(var i=0;i<rightEpc.length;i++){
                if(dtlValue.sku==rightEpc[i].sku){
                    if(dtlRow.uniqueCodes!=""&&dtlRow.uniqueCodes!=undefined){
                        dtlRow.uniqueCodes=dtlRow.uniqueCodes+","+rightEpc[i].code;
                        dtlRow.thisQty= dtlRow.thisQty+1;
                    }else{
                        dtlRow.uniqueCodes=rightEpc[i].code;
                        dtlRow.thisQty= dtlRow.thisQty+1;
                    }
                    $("#billInformationIngrid").setRowData(dtlRow.id, dtlRow);
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
                skuEpc.sku=errorEpc[index].sku;
                skuEpc.styleId=errorEpc[index].styleId;
                skuEpc.styleName=errorEpc[index].styleName;
                skuEpc.colorId=errorEpc[index].colorId;
                skuEpc.colorName=errorEpc[index].colorName;
                skuEpc.sizeId=errorEpc[index].sizeId;
                skuEpc.sizeName=errorEpc[index].sizeName;
                skuEpc.qty=1;
                skuEpc.uniqueCodes=errorEpc[index].code;
                skuMap.sku=errorEpc[index].sku;
                skuMap.values=skuEpc;
                skuEpcList.push(skuMap);
            }else{
                //判断该sku是否在skuEpcList中
                var isHave=false;
                for(var i=0;i<skuEpcList.length;i++){
                    if(errorEpc[index].sku==skuEpcList[i].sku){
                        skuEpcList[i].values.qty=skuEpcList[i].values.qty+1;
                        skuEpcList[i].values.uniqueCodes=skuEpcList[i].values.uniqueCodes+","+errorEpc[index].code;
                        isHave=true;
                    }
                }
                if(isHave==false){
                    var skuMap={};
                    var skuEpc={};
                    skuEpc.sku=errorEpc[index].sku;
                    skuEpc.styleId=errorEpc[index].styleId;
                    skuEpc.styleName=errorEpc[index].styleName;
                    skuEpc.colorId=errorEpc[index].colorId;
                    skuEpc.colorName=errorEpc[index].colorName;
                    skuEpc.sizeId=errorEpc[index].sizeId;
                    skuEpc.sizeName=errorEpc[index].sizeName;
                    skuEpc.qty=1;
                    skuEpc.uniqueCodes=errorEpc[index].code;
                    skuMap.sku=errorEpc[index].sku;
                    skuMap.values=skuEpc;
                    skuEpcList.push(skuMap);
                }
            }
        });
        $.each(skuEpcList, function (index, value) {
            var values=skuEpcList[index].values;
            $("#notThisOneIngrid").addRowData($("#notThisOneIngrid").getDataIDs().length, values);
        });
    }
    function loadbillInformationInTable() {
        $("#billInformationIngrid").jqGrid({
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
                {name: 'InQty', label: '出库数量', width: 60},
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
                        return "<a href='javascript:void(0);' onclick=showCodesDetail('" + rowObject.uniqueCodes + "')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
                    }
                }
            ],
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum: -1,
            pager: '#billInformationIngrid-pager',
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
               // lodeBillInformationIngrid();
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
    function loadnotThisOnegridInTable() {
        $("#notThisOneIngrid").jqGrid({
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
                        return "<a href='javascript:void(0);' onclick=showCodesInDetail('" + rowObject.uniqueCodes + "')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
                    }
                }

            ],
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum: -1,
            pager: '#notThisOneIngrid-pager',
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
    function onClearIn() {
        $("#billInformationgrid").clearGridData();
        $("#notThisOnegrid").clearGridData();
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
    function lodeBillInformationIngrid() {
        $.each($("#addDetailgrid").getDataIDs(), function (dtlIndex, dtlValue) {
            dtlValue.thisQty=0;
            dtlValue.uniqueCodes="";
            $("#billInformationIngrid").addRowData($("#billInformationIngrid").getDataIDs().length, dtlValue);
        });
    }
    function saveIn() {
        cs.showProgressBar();
        $("#saveIn").attr({"disabled": "disabled"});
        var billNo = $("#edit_billNo").val();
        var dtlArray = [];
        var epcArray = [];
        $.each($("#billInformationIngrid").getDataIDs(), function (dtlIndex, dtlValue) {
            var dtlRow = $("#billInformationIngrid").getRowData(dtlValue);
            //判断入库库数量加本次数量是否大于单据数量
            if(parseInt(dtlRow.inQty)+parseInt(dtlRow.thisQty)>parseInt(dtlRow.qty)){
                $.gritter.add({
                    text: dtlRow.sku+"要入库的数量超过本单数量"+(parseInt(dtlRow.inQty)+parseInt(dtlRow.thisQty)-parseInt(dtlRow.qty))+"件",
                    class_name: 'gritter-success  gritter-light'
                });
            }else {
                dtlRow.inQty=parseInt(dtlRow.inQty)+parseInt(dtlRow.thisQty);
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
            $("#saveIn").removeAttr("disabled");
            cs.closeProgressBar();
            return;
        }
        $.ajax({
            dataType: "json",
            async: true,
            url: basePath + "/logistics/saleOrderBill/convertIn.do",
            data: {
                billNo: billNo,
                strEpcList: JSON.stringify(epcArray),
                userId: userId
            },
            type: "POST",
            success: function (msg) {
                cs.closeProgressBar();
                $("#SODtl_addUniqCode").removeAttr("disabled");
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
        $("#modal-batch-show-In-table").modal('hide');
    }
</script>