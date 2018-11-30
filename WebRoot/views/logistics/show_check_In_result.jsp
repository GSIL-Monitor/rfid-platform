<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" %>
<div id="modal-batch-show-In-table" class="modal fade" role="dialog" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                入库批量扫码页面
            </div>
        </div>
        <div class="modal-content">
            <div class="widget-box transparent">
                <div class="widget-header ">
                    <div class="widget-toolbar no-border">
                        <ul class="nav nav-tabs" id="myTab">
                            <li class="active">
                                <a data-toggle="tab" href="#sInBillInformation">单据信息</a>
                            </li>
                            <li>
                                <a data-toggle="tab" href="#sInNotThisOne">非本单商品</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="widget-body">
                    <div class="widget-main padding-12 no-padding-left no-padding-right">
                        <div class="tab-content padding-4">
                            <div id="sInBillInformation" class="tab-pane in active">
                                <table id="billInformationIngrid"></table>
                                <div id="billInformationIngrid-pager"></div>
                            </div>
                            <div id="sInNotThisOne" class="tab-pane">
                                <table id="notThisOneIngrid"></table>
                                <div id="notThisOneIngrid-pager"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <div class="col-lg-3">
                        <span style="font-size:15px">已扫到数量：</span>
                        <span id="inCodeQty" style="font-size:15px; color:tomato">0</span>
                    </div>
                    <div id="dialog_buttonGroup">
                        <button id="scanningIn"  class='btn btn-primary' onclick="onScanningIn()">继续扫描</button>
                        <button id="stopIn"  class='btn btn-primary' onclick="stopIn()">停止</button>
                        <button id="saveIn"  class='btn btn-primary' onclick="saveIn()">保存</button>
                        <button id="clearIn"  class='btn btn-primary' onclick="deleteInCode()">清空</button>
                        <button id="close"  class='btn btn-primary' onclick="inClose()">关闭</button>
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
        loadnotThisOnegridInTable();
        //得到表格的宽度
    });
    function fullWebInSocket() {
        loadingwebsocket();
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
            "cmd":"10006"
        };
        sendMessgeInToServer(msg);
    }
    function sendMessgeInToServer(message) {
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
        $.gritter.add({
            text: "连接 Reader Server成功",
            class_name: 'gritter-success  gritter-light'
        });
        var msg={
            "cmd":"10002"
        };
        sendMessgeInToServer(msg);
    }
    function onCloseIn(evt) {
        if(evt.code == 1005){
            bootbox.alert("与服务器连接已断开");
         }else if(evt.code == 1006){
            bootbox.alert("连接服务器失败");
         }
    }
    function onMessageIn(evt) {
        var res = JSON.parse(evt.data);
        if (res.cmd === "10006") {
            $.each(res.data,function (index,value) {
                if (value!==null&&value.skuInfo!==null){
                    skuInfoIn.push(value.skuInfo);
                    $("#inCodeQty").text(skuInfoIn.length)
                }
            });
            return false;
        }
    }
    function onErrorIn(evt) {
        bootbox.alert("发生错误");
    }
    /*
     停止
     */
    function stopIn() {
        if (skuInfoIn !== null) {
            window.clearInterval(timeoutIn);
        }
        var msg={
            "cmd":"10003"
        };
        sendMessgeInToServer(msg);
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
                if(dtlRow.sku==rightEpc[i].sku){
                    if(dtlRow.uniqueCodes!=""&&dtlRow.uniqueCodes!=undefined){
                        dtlRow.uniqueCodes=dtlRow.uniqueCodes+","+rightEpc[i].code;
                        dtlRow.thisQty= parseInt(dtlRow.thisQty)+1;
                    }else{
                        dtlRow.uniqueCodes=rightEpc[i].code;
                        dtlRow.thisQty= parseInt(dtlRow.thisQty)+1;
                    }
                    dtlRow.floor=rightEpc[i].floor;
                    dtlRow.inStock=rightEpc[i].inStock;
                    dtlRow.warehouseId=rightEpc[i].warehouseId;
                    $("#billInformationIngrid").setRowData(dtlIndex, dtlRow);
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
                skuEpc.noOutPutCode=value.code;
                skuEpc.remark=value.remark;
                skuEpc.floor=value.floor;
                skuEpc.inStock=value.inStock;
                skuEpc.warehouseId=value.warehouseId;
                skuEpcList.set(value.sku,skuEpc);
            }else {
                var exist=skuEpcList.get(value.sku);
                exist.qty=parseInt(exist.qty)+1;
                exist.noOutPutCode=exist.noOutPutCode+","+value.code;
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
                skuEpc.noOutPutCode=value.code;
                skuEpc.remark=value.remark;
                skuEpc.floor=value.floor;
                skuEpc.inStock=value.inStock;
                skuEpc.warehouseId=value.warehouseId;
                skuEpcList.set(value.sku,skuEpc);
            }else {
                var exist=skuEpcList.get(value.sku);
                exist.qty=parseInt(exist.qty)+1;
                exist.noOutPutCode=exist.noOutPutCode+","+value.code;
                skuEpcList.set(value.sku,exist);
            }
        });
        //设值
        skuEpcList.forEach(function (value,key,map) {
            $("#notThisOneIngrid").addRowData($("#notThisOneIngrid").getDataIDs().length, value);
        });
        skuInfo=[];
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
                {name: 'colorId', label: '色码', width: 40},
                {name: 'colorName', label: '颜色',hidden:true},
                {name: 'sizeId', label: '尺码',hidden:true},
                {name: 'sizeName', label: '尺码', width: 40},
                {name: 'sku', label: 'SKU', width: 60},
                {name: 'qty', label: '单据数量', width: 65},
                {name: 'inQty', label: '入库数量', width: 65},
                {name: 'outQty', label: '出库数量', hidden: true},
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
                        return "<a href='javascript:void(0);' onclick=showCodesInDetail('" + options.rowId + "')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
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
        var parent_column = $("#billInformationIngrid").closest('.modal-dialog');
        console.log(parent_column.width());
        $("#billInformationIngrid").jqGrid('setGridWidth', parent_column.width() - 5);
    }

    function showCodesInDetail(rowId) {
        $("#show-allUniqueCode-list").modal('show').on('hidden.bs.modal',function () {
            $("#allUniqueCodeListGrid").jqGrid('clearGridData');//清空表格
        });
        batchCodeDetail(rowId,"billInformationIngrid");
    }

    function showCodesNoInDetail(rowId) {
        $("#show-allUniqueCode-list").modal('show').on('hidden.bs.modal',function () {
            $("#allUniqueCodeListGrid").jqGrid('clearGridData');//清空表格
        });
        batchNoCodeDetail(rowId,"notThisOneIngrid");
    }


    function loadnotThisOnegridInTable() {
        $("#notThisOneIngrid").jqGrid({
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
                {name: 'noOutPutCode', label: '唯一码',hidden: true},
                {name: 'remark', label:'异常类型', width: 100},
                {
                    name: '', label: '唯一码明细', width: 80, align: "center",
                    formatter: function (cellValue, options, rowObject) {
                        return "<a href='javascript:void(0);' onclick=showCodesNoInDetail('" + options.rowId + "')><i class='ace-icon ace-icon fa fa-list' title='显示唯一码明细'></i></a>";
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
        var parent_column = $("#notThisOneIngrid").closest('.modal-dialog');
        console.log(parent_column.width());
        $("#notThisOneIngrid").jqGrid('setGridWidth', parent_column.width() - 5);
    }

    function deleteInCode() {
        $("#notThisOneIngrid").clearGridData();
        $.each($("#billInformationIngrid").getDataIDs(), function (dtlIndex, dtlValue) {
            var dtlRow = $("#billInformationIngrid").getRowData(dtlValue);
            dtlRow.thisQty=0;
            dtlRow.uniqueCodes="";
            $("#billInformationIngrid").setRowData(dtlIndex, dtlRow);
        });
        $.gritter.add({
            text: "清除成功，请重新扫码",
            class_name: 'gritter-success  gritter-light'
        });
        var msg={
            "cmd":"10005"
        };
        sendMessgeInToServer(msg);
        $("#inCodeQty").text(0);
        skuInfo=[];
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
            var dtlRow = $("#addDetailgrid").getRowData(dtlValue);
            dtlRow.thisQty=0;
            dtlRow.uniqueCodes="";
            $("#billInformationIngrid").addRowData($("#billInformationIngrid").getDataIDs().length, dtlRow);
        });
    }
    function saveIn() {
        cs.showProgressBar();
        var billNo = $("#edit_billNo").val();
        var dtlArray = [];
        var epcArray = [];
        var allUniqueCodes="";
        $.each($("#billInformationIngrid").getDataIDs(), function (dtlIndex, dtlValue) {
            var dtlRow = $("#billInformationIngrid").getRowData(dtlValue);
            allUniqueCodes+=dtlRow.uniqueCodes;
        });
        if (allUniqueCodes===""){
            cs.closeProgressBar();
            bootbox.alert("未扫描到能入库的唯一码，请继续扫码");
            return;
        }
        $.each($("#billInformationIngrid").getDataIDs(), function (dtlIndex, dtlValue) {
            var dtlRow = $("#billInformationIngrid").getRowData(dtlValue);
            //判断入库库数量加本次数量是否大于单据数量
            if(parseInt(dtlRow.inQty)+parseInt(dtlRow.thisQty)>parseInt(dtlRow.qty)){
                cs.closeProgressBar();
                $.gritter.add({
                    text: dtlRow.sku+"要入库的数量超过本单数量"+(parseInt(dtlRow.inQty)+parseInt(dtlRow.thisQty)-parseInt(dtlRow.qty))+"件",
                    class_name: 'gritter-success  gritter-light'
                });

            }else {
                dtlArray.push(dtlRow);
                //填充epcArray的数组
                var Codes=dtlRow.uniqueCodes.split(",");
                if (dtlRow.uniqueCodes!==""){
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
            $("#saveIn").removeAttr("disabled");
            cs.closeProgressBar();
            return;
        }

        var prifex = billNo.substring(0,2);
        //获取单号前缀设置入库url
        var inurl;
        switch(prifex){
            case "PI":
                inurl = basePath + "/logistics/purchaseOrderBill/convert.do";
                break;
            case "SO":
                inurl = basePath + "/logistics/saleOrderBill/convertIn.do";
                break;
            case "CM":
                inurl = basePath + "/logistics/Consignment/convertIn.do";
                break;
            case "SR":
                inurl = basePath + "/logistics/saleOrderReturn/convertIn.do";
                break;
            case "TR":
                inurl = basePath + "/logistics/transferOrder/convertIn.do";
                break;

        }
        $.ajax({
            dataType: "json",
            async: true,
            url: inurl,
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
        //wareHouseIn();
    }
    //模态框关闭
    function inClose() {
        var msg={
            "cmd":"10004"
        };
        sendMessgeToServer(msg);
        $("#modal-batch-show-In-table").modal('hide');
    }
</script>