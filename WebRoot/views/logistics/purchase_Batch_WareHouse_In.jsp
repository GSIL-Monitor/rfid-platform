<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="modal_purchase_Batch_WareHouse_In" class="modal fade" role="dialog" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-header no-padding">
            <div class="table-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <span class="white">&times;</span>
                </button>
                添加唯一码信息入库批量扫码页面
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
                                <table id="batchEpcGrid"></table>
                            </div>
                            <div id="sInNotThisOne" class="tab-pane">
                                <table id="notThisOneIngrid"></table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <div class="col-lg-3">
                        <span style="font-size:15px">已扫到数量：</span>
                        <span id="inCodeQty" style="font-size:15px; color:tomato">0</span>
                        <span style="font-size:15px">异常数量：</span>
                        <span id="noCodeQty" style="font-size:15px; color:tomato">0</span>
                    </div>
                    <div id="dialog_buttonGroup">
                        <button id="linkIn"  class='btn btn-primary' onclick="fullWebInSocket()">连接</button>
                        <button id="scanningIn"  class='btn btn-primary' onclick="onScanningIn()">扫描</button>
                        <button id="stopIn"  class='btn btn-primary' onclick="stopIn()">停止</button>
                        <button id="saveIn"  class='btn btn-primary' onclick="saveEpc()">保存</button>
                        <button id="clearIn"  class='btn btn-primary' onclick="deleteInCode()">清空</button>
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
        initEpcGridOut();
        initNotThisOneIngrid();
    });
    //连接读写器
    function fullWebInSocket() {
        loadingwebsocket();
        websocket.onopen = function(evt) { onOpenIn(evt) };
        websocket.onclose = function(evt) { onCloseIn(evt) };
        websocket.onmessage = function(evt) { onMessageIn(evt) };
        websocket.onerror = function(evt) { onErrorIn(evt) };
    }

    function onOpenIn(evt) {
        $.gritter.add({
            text: "连接 Reader Server成功",
            class_name: 'gritter-success  gritter-light'
        });
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
     扫描
     */
    function onScanningIn() {
        var msg={
            "cmd":"10002"
        };
        sendMessgeInToServer(msg);
    }
    //发送指令
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
    //停止
    function stopIn() {
        if (skuInfoIn !== null) {
            window.clearInterval(timeoutIn);
        }
        var msg={
            "cmd":"10003"
        };
        sendMessgeInToServer(msg);
        //检查出入库
        fullInGridData();
    }

    //填充批量入库扫码页面数据
    function fullInGridData() {
        $.each($("#batchEpcGrid").getDataIDs(),function (index,value) {
            var rowData = $("#batchEpcGrid").getRowData(value);
            $.each(skuInfoIn,function (infoIndex,infoValue) {
                if (rowData.code==infoValue.unicode){
                    rowData.thisCode=infoValue.unicode;
                    $("#batchEpcGrid").setRowData(index, rowData);
                    infoValue.id=true;
                }
            });
        });
        var noCodeQty=0;
        $.each(skuInfoIn,function (index,value) {
            if (value.id!=true){
                $("#notThisOneIngrid").addRowData($("#notThisOneIngrid").getDataIDs().length, value);
                noCodeQty+=1;
            }
        });
        $("#noCodeQty").text(noCodeQty);
        skuInfoIn = [];
    }



    //清空
    function deleteInCode() {
        $.each($("#batchEpcGrid").getDataIDs(), function (dtlIndex, dtlValue) {
            var dtlRow = $("#batchEpcGrid").getRowData(dtlValue);
            dtlRow.uniqueCodes="";
            $("#batchEpcGrid").setRowData(dtlIndex, dtlRow);
        });
        $.gritter.add({
            text: "清除成功，请重新扫码",
            class_name: 'gritter-success  gritter-light'
        });
        var msg={
            "cmd":"10005"
        };
        skuInfoIn = [];
        sendMessgeInToServer(msg);
        $("#inCodeQty").text(0);
    }

    function initEpcGridOut() {
        $("#batchEpcGrid").jqGrid({
            height:  "350",
            datatype: "json",
            colModel: [
                {name: 'id', label: 'id', hidden: true, width: 240},
                {name: 'sku', label: 'sku', hidden: true, width: 240},
                {name:'code',label:'唯一码',eidtable:true,width:160},
                {name:'styleId', label: '款号', editable: true, width: 106},
                {name:'colorId',label:'色号',editable:true,width:147},
                {name:'sizeId',label:'尺寸',editable:true,width:147},
                {name:'thisCode',label:'本次扫描唯一码',width: 160}
            ],
            viewrecords: true,
            autowidth: false,
            rownumbers: true,
            altRows: true,
            rowNum:-1,
            multiselect: false,
            shrinkToFit: true,
            sortname: 'code',
            sortorder: "asc"
        });
        var parent_column = $("#batchEpcGrid").closest('.modal-dialog');
        console.log(parent_column.width());
        $("#batchEpcGrid").jqGrid('setGridWidth', parent_column.width() - 5);
    }

    function initNotThisOneIngrid() {
        $("#notThisOneIngrid").jqGrid({
            height:  "350",
            datatype: "json",
            colModel: [
                {name:'unicode',label:'异常唯一码',width: 160},
                {name: 'sku', label: 'sku', hidden: true, width: 240},
                {name:'styleId', label: '款号', editable: true, width: 160},
                {name:'colorId',label:'色号',editable:true,width:200},
                {name:'sizeId',label:'尺寸',editable:true,width:200}
            ],
            viewrecords: true,
            autowidth: false,
            rownumbers: true,
            altRows: true,
            rowNum:-1,
            multiselect: false,
            shrinkToFit: true,
            sortname: 'code',
            sortorder: "asc"
        });
        var parent_column = $("#notThisOneIngrid").closest('.modal-dialog');
        console.log(parent_column.width());
        $("#notThisOneIngrid").jqGrid('setGridWidth', parent_column.width() - 5);
    }

</script>
