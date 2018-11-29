<<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" %>
<div id="modal-batch-show-table" class="modal fade" tabindex="-1">
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
                    <div class="col-lg-3">
                        <span style="font-size:15px">已扫到数量：</span>
                        <span id="outCodeQty" style="font-size:15px; color:tomato">0</span>
                    </div>
                    <div id="dialog_buttonGroup">
                        <button id="link"  class='btn btn-primary' onclick="fullOutWebSocket()">连接</button>
                        <button id="scanningOut"  class='btn btn-primary' onclick="onScanningOut()">扫描</button>
                        <button id="stopOut"  class='btn btn-primary' onclick="stopOut()">停止</button>
                        <button id="saveOut"  class='btn btn-primary' onclick="saveOut()">保存</button>
                        <button id="clearOut"  class='btn btn-primary' onclick="deleteCode()">清空</button>
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
    var dataResultCode=new Map();
    //点击唯一码明细时记录当前行的所有唯一码
    $(function () {
        loadbillInformationOutTable();
        loadnotThisOnegridOutTable();
    });
    function fullOutWebSocket() {
        var wsUri ="ws://192.168.0.109:4649/csreader";
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
        $.gritter.add({
            text: "连接 Reader Server成功",
            class_name: 'gritter-success  gritter-light'
        });
    }
    function onCloseOut(evt) {
        if(evt.code == 1005){
            bootbox.alert("与服务器连接已断开");
        }else if(evt.code == 1006){
            bootbox.alert("连接服务器失败");
        }
    }
    function onMessageOut(evt) {
        var res = JSON.parse(evt.data);
        if (res.cmd === "10006") {
            $.each(res.data, function (index, value) {
                if (value !== null && value.skuInfo !== null) {
                    skuInfo.push(value.skuInfo);
                    changeOutCodeQty();
                }
            });
            return false;
        }
    }

    function changeOutCodeQty() {
        var num=skuInfo.length;
        $("#outCodeQty").text(num);
    }

    function onErrorOut(evt) {
        bootbox.alert("发生错误");
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
        dataResultCode.clear();
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
        ajax_data = {uniqueCodes: JSON.stringify(codeArray), warehouseId: wareHouse, type: taskType, billNo: billNo, isAdd: false,rfidType:"code"};
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
                 if(dtlRow.sku==rightEpc[i].sku){
                     if(dtlRow.uniqueCodes!=""&&dtlRow.uniqueCodes!=undefined){
                         dtlRow.uniqueCodes=dtlRow.uniqueCodes+","+rightEpc[i].code;
                         dtlRow.thisQty=parseInt(dtlRow.thisQty)+1;
                     }else{
                         dtlRow.uniqueCodes=rightEpc[i].code;
                         dtlRow.thisQty=parseInt(dtlRow.thisQty)+1;
                     }
                     $("#billInformationOutgrid").setRowData(dtlIndex, dtlRow);
                 }

             }
        });
        //添加校验未通过唯一码和非本单商品唯一码到notThisOneOutgrid中
        //定义一个根据sku做的唯一码和非本单商品唯一码汇总的对象
        var skuEpcList=new Map();
        $.each(errorEpc, function (index, value) {
            if (!skuEpcList.has(value.sku)){
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
                skuEpc.exceptionType='异常唯一码';
                skuEpcList.set(value.sku,skuEpc);
            }else {
                var exist=skuEpcList.get(value.sku);
                exist.qty=parseInt(exist.qty)+1;
                exist.uniqueCodes=exist.uniqueCodes+","+value.code;
                skuEpcList.set(value.sku,exist);
            }
        });
        $.each(noInBill, function (index, value) {
            if (!skuEpcList.has(value.sku)){
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
                skuEpc.exceptionType='非本单唯一码';
                skuEpcList.set(value.sku,skuEpc);
            }else {
                var exist=skuEpcList.get(value.sku);
                exist.qty=parseInt(exist.qty)+1;
                exist.uniqueCodes=exist.uniqueCodes+","+value.code;
                skuEpcList.set(value.sku,exist);
            }
        });
        skuEpcList.forEach(function (value,key,map) {
            $("#notThisOneOutgrid").addRowData($("#notThisOneOutgrid").getDataIDs().length, value);
        });
        skuInfo=[];
    }
    function deleteCode() {
        $("#notThisOneOutgrid").clearGridData();
        $.each($("#billInformationOutgrid").getDataIDs(), function (dtlIndex, dtlValue) {
            var dtlRow = $("#billInformationOutgrid").getRowData(dtlValue);
            dtlRow.thisQty=0;
            dtlRow.uniqueCodes="";
            $("#billInformationOutgrid").setRowData(dtlIndex, dtlRow);
        });
        $.gritter.add({
            text: "清除成功，请重新扫码",
            class_name: 'gritter-success  gritter-light'
        });
        var msg={
            "cmd":"10005"
        };
        sendMessgeOutToServer(msg);
        $("#outCodeQty").text(0);
        skuInfo=[];
    }

    function sendMessgeOutToServer(message) {

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
                {name: 'colorId', label: '色码', width: 40},
                {name: 'colorName', label: '颜色',hidden:true},
                {name: 'sizeId', label: '尺码',hidden:true},
                {name: 'sizeName', label: '尺码', width: 40},
                {name: 'sku', label: 'SKU', width: 60},
                {name: 'qty', label: '单据数量', width: 65},
                {name: 'inQty', label: '入库数量', hidden: true},
                {name: 'outQty', label: '出库数量', width: 65},
                {name: 'thisQty', label: '本次数量', width: 65},
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
                    name: '', label: '唯一码明细', width: 65, align: "center",
                    formatter: function (cellValue, options, rowObject) {
                        return "<a href='javascript:void(0);' onclick=showCodesOutDetail('"+options.rowId+"')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
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
    function showCodesOutDetail(rowId) {
        $("#show-allUniqueCode-list").modal('show').on('hidden.bs.modal',function () {
            $("#allUniqueCodeListGrid").jqGrid('clearGridData');//清空表格
        });
        loadOutPutCodeDetail(rowId);
    }

    function showCodesNoOutDetail(rowId) {
        $("#show-allUniqueCode-list").modal('show').on('hidden.bs.modal',function () {
            $("#allUniqueCodeListGrid").jqGrid('clearGridData');//清空表格
        });
        loadPutCodeDetail(rowId);
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
                {name: 'styleId', label: '款号',width: 60},
                {name: 'styleName', label: '款名', hidden:true},
                {name: 'colorId', label: '色码', width: 45},
                {name: 'colorName', label: '颜色',hidden:true},
                {name: 'sizeId', label: '尺码',hidden:true},
                {name: 'sizeName', label: '尺码', width: 45},
                {name: 'sku', label: 'SKU', width: 80},
                {name: 'qty', label: '数量', width: 45},
                {name: 'uniqueCodes', label: '唯一码',hidden: true},
                {name: 'exceptionType', label:'异常类型', width: 60},
                {
                    name: '', label: '唯一码明细', width: 80, align: "center",
                    formatter: function (cellValue, options, rowObject) {
                        return "<a href='javascript:void(0);' onclick=showCodesNoOutDetail('" + options.rowId + ")><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
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
        var dtlArray = [];
        var epcArray = [];
        var billNo = $("#edit_billNo").val();
        var allUniqueCodes="";
        $.each($("#billInformationOutgrid").getDataIDs(), function (dtlIndex, dtlValue) {
            var dtlRow = $("#billInformationOutgrid").getRowData(dtlValue);
            allUniqueCodes+=dtlRow.uniqueCodes;
        });
        if (allUniqueCodes===""){
            cs.closeProgressBar();
            bootbox.alert("未扫描到能出库的唯一码，请继续扫码");
            return;
        }
        $.each($("#billInformationOutgrid").getDataIDs(), function (dtlIndex, dtlValue) {
            var dtlRow = $("#billInformationOutgrid").getRowData(dtlValue);
            //判断出库数量加本次数量是否大于单据数量
            if(parseInt(dtlRow.outQty)+parseInt(dtlRow.thisQty)>parseInt(dtlRow.qty)){
                cs.closeProgressBar();
                $.gritter.add({
                    text: dtlRow.sku+"要出库的数量超过本单数量"+(parseInt(dtlRow.outQty)+parseInt(dtlRow.thisQty)-parseInt(dtlRow.qty))+"件",
                    class_name: 'gritter-success  gritter-light'
                });

            }else {
                dtlArray.push(dtlRow);
                //填充epcArray的数组
                if (dtlRow.uniqueCodes!==""){
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
            }
        });
        if (epcArray.length == 0) {
            bootbox.alert("请添加唯一码!");
            $("#saveOut").removeAttr("disabled");
            cs.closeProgressBar();
            return;
        }
        var prifex = billNo.substring(0,2);
        //获取单号前缀设置入库url
        var outurl;
        switch(prifex){
            case "PR":
                outurl = "/logistics/purchaseReturn/convertOut.do";
                break;
            case "SO":
                outurl = basePath + "/logistics/saleOrderBill/convertOut.do";
                break;
            case "SR":
                outurl = basePath + "/logistics/saleOrderReturn/convertOut.do";
                break;
            case "TR":
                outurl = basePath + "/logistics/transferOrder/convertOut.do";
                break;

        }
        $.ajax({
            dataType: "json",
            url:  outurl,
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