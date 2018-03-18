<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page import="com.casesoft.dmc.model.sys.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    String socketPath = "ws://" + request.getServerName() + ":"
            + request.getServerPort() + request.getContextPath();
    User user = (User)session.getAttribute("userSession");
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
        var socketPath="<%=socketPath%>/socketWS.do?acceptType=0&payerId=<%=user.getCode()%>";
    </script>
</head>
<body class="no-skin">
<div class="main-container" id="main-container" style="">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }

    </script>
    <div class="main-content">
        <div class="main-content-inner">

            <div id="page-content">

                <div class="row">
                    <div class="col-xs-12 col-sm-6">
                        <div class="widget-box widget-color-blue  light-border" id="parentWidth">
                            <div class="widget-header">
                                <h5 class="widget-title">在线设备</h5>
                                <div class="widget-toolbar padding-5 no-border">
                                    <a href="#" data-action="collapse" >
                                        <i class="ace-icon fa fa-chevron-up"></i>
                                    </a>
                                </div>
                            </div>
                            <div class="widget-body">
                                <table id="online_grid"></table>
                                <div id="online_grid-pager"></div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xs-12 col-sm-6">
                        <div class="widget-box widget-color-blue  light-border">

                            <div class="widget-header">

                                <h5 class="widget-title">消息控制台</h5>
                                <div class="widget-toolbar padding-5 no-border">
                                    <a href="#" data-action="collapse" >
                                        <i class="ace-icon fa fa-chevron-up"></i>
                                    </a>
                                </div>
                                <div class="widget-toolbar no-border">
                                    <button class="btn btn-xs btn-light bigger" onclick="refreshCache();">
                                        <i class="ace-icon fa fa-refresh"></i>
                                        返回
                                    </button>
                                </div>
                            </div>
                            <div class="widget-body">
                                <div class="widget-main no-padding">

                                            <style>
                                                textarea {
                                                    height: 300px;
                                                    width: 100%;
                                                    resize: none;
                                                    outline: none;
                                                }

                                                input[type=button] {
                                                    float: right;
                                                    margin: 5px;
                                                    width: 50px;
                                                    height: 35px;
                                                    border: none;
                                                    color: white;
                                                    font-weight: bold;
                                                    outline: none;
                                                }

                                                .clear {
                                                    background: red;
                                                }

                                                .send {
                                                    background: green;
                                                }

                                                .clear:active {
                                                    background: yellow;
                                                }

                                                .send:active {
                                                    background: yellow;
                                                }

                                                .msg {
                                                    width: 100%;
                                                    height: 25px;
                                                    outline: none;
                                                }

                                                #content {
                                                    border: 1px solid gray;
                                                    width: 100%;
                                                    height: 400px;
                                                    overflow-y: scroll;
                                                }

                                                .from {
                                                    background-color: green;
                                                    width: 80%;
                                                    border-radius: 10px;
                                                    height: 30px;
                                                    line-height: 30px;
                                                    margin: 5px;
                                                    float: left;
                                                    color: white;
                                                    padding: 5px;
                                                    font-size: 22px;
                                                }

                                                .to {
                                                    background-color: gray;
                                                    width: 80%;
                                                    border-radius: 10px;
                                                    height: 30px;
                                                    line-height: 30px;
                                                    margin: 5px;
                                                    float: right;
                                                    color: white;
                                                    padding: 5px;
                                                    font-size: 22px;
                                                }

                                                .name {
                                                    color: gray;
                                                    font-size: 12px;
                                                }

                                                .tmsg_text {
                                                    color: white;
                                                    background-color: rgb(47, 47, 47);
                                                    font-size: 18px;
                                                    border-radius: 5px;
                                                    padding: 2px;
                                                }

                                                .fmsg_text {
                                                    color: white;
                                                    background-color: rgb(66, 138, 140);
                                                    font-size: 18px;
                                                    border-radius: 5px;
                                                    padding: 2px;
                                                }

                                                .sfmsg_text {
                                                    color: white;
                                                    background-color: rgb(148, 16, 16);
                                                    font-size: 18px;
                                                    border-radius: 5px;
                                                    padding: 2px;
                                                }

                                                .tmsg {
                                                    clear: both;
                                                    float: right;
                                                    width: 80%;
                                                    text-align: right;
                                                }

                                                .fmsg {
                                                    clear: both;
                                                    float: left;
                                                    width: 80%;
                                                }
                                            </style>


                                            <div id="content"></div>
                                    <select class="chosen-select form-control" id="msg_type"
                                            name="msg_type" data-placeholder="">
                                        <option value="UD_PROD">更新商品</option>
                                        <option value="NT_MSG" selected>消息通知</option>
                                    </select>
                                            <input type="text" placeholder="请输入要发送的信息" id="msg" onkeydown="send(event)">
                                            <input type="button" value="发送" onclick="sendMsg()" >
                                            <input type="button" value="清空" onclick="clearAll()">

                                </div>
                            </div>
                        </div>
                    </div>

                </div>
                <!-- /.row -->
                <!--/#page-content-->
            </div>
        </div>
    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>
    <!--/.fluid-container#main-container-->
</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>

<script type="text/javascript">
    $(function () {

        websocketTestConsole();
    });

    function initGrid() {
        $("#online_grid").jqGrid({
            height: "300px",
            url: basePath + "/data/msgPush/listOnlineDevice.do",
            datatype: 'json',
            mtype: 'POST',
            colModel: [
                {name: 'code', label: '编号', editable: true,key:true},
                {name: 'ipAddr', label: 'IP地址', editable: true},
                {name: 'onlineTime', label: '上线时间', editable: true}
            ],
            viewrecords: true,
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum:-1,
            multiselect: false,
            shrinkToFit: true,
            sortname: 'onlineTime',
            sortorder: "asc"

        });
        $("#online_grid").setGridWidth($("#parentWidth").width());
    }


    //<editor-fold defaultstate="collapsed" desc="webSocket连接">

    var websocket;
    function websocketTestConsole() {

        var uid = -1;
        var from = uid;
        var fromName = 'TEST';
        var to = uid === 1 ? 2 : 1;


        if ('WebSocket' in window) {
            websocket = new WebSocket(socketPath);
        } else if ('MozWebSocket' in window) {
            //websocket = new MozWebSocket("ws://" + path + "/ws" + uid);
        } else {
            // websocket = new SockJS("http://" + path + "/ws/sockjs" + uid);
        }
        websocket.onopen = function (event) {
            console.log("WebSocket:已连接");
            console.log(event);
            initGrid();
        };

        websocket.onmessage = function (event) {
            var data = JSON.parse(event.data);
            console.log("WebSocket:收到一条消息", data);
            var textCss = data.fromCode === -1 ? "sfmsg_text" : "fmsg_text";
            $("#content").append("<div><label>" + data.fromCode  + "</label><div class='" + textCss + "'>" + data.content + "</div></div>");
            if(data.type==="NT_OLN") {
                /**
                 *         var ids = jQuery("#gridTable").jqGrid('getDataIDs');
                 var rowid = getMaxId(ids) + 1;
                 if (selectedId) {
            $("#gridTable").jqGrid("addRowData", rowid, dataRow, "after", selectedId);
        } else {
            $("#gridTable").jqGrid("addRowData", rowid, dataRow, "last");
        }
                 */
                var dataRow = {code:data.fromCode,ipAddr:data.ipAddr,onlineTime:data.onlineTime};
                $("#online_grid").jqGrid("addRowData", data.fromCode, dataRow, "last");
            }
            scrollToBottom();
        };
        websocket.onerror = function (event) {
            console.log("WebSocket:发生错误 ");
            $("#content").append("WebSocket:发生错误");
            console.log(event);
        };
        websocket.onclose = function (event) {
            console.log("WebSocket:已关闭");
            $("#content").append("WebSocket:已关闭");
            console.log(event);
        }
    }
    function sendMsg() {
        var v = $("#msg").val();
        if (v == "") {

        } else {
            var data = {};
            data["fromCode"] = "SYS";
            data["type"] = $("#msg_type").val();
            data["toCode"] = "";
            var rowId = $("#online_grid").jqGrid("getGridParam", "selrow");
            if(rowId) {
                var row = $("#online_grid").jqGrid('getRowData', rowId);
                data["toCode"] = row.code;
            }

            data["content"] = v;
            websocket.send(JSON.stringify(data));
            $("#content").append("<div><label>我&nbsp;" + new Date().Format("yyyy-MM-dd hh:mm:ss") + "</label><div>" + data.content + "</div></div>");
            scrollToBottom();
            $("#msg").val("");
        }
    }

    function scrollToBottom() {
        var div = document.getElementById('content');
        div.scrollTop = div.scrollHeight;
    }

    Date.prototype.Format = function (fmt) { //author: meizz
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    };

    function send(event) {
        var code;
        if (window.event) {
            code = window.event.keyCode; // IE
        } else {
            code = e.which; // Firefox
        }
        if (code == 13) {
            sendMsg();
        }
    }

    function clearAll() {
        $("#content").empty();
    }

    //</editor-fold>

</script>
</body>
</html>