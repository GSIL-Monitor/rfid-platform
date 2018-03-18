<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <jsp:include page="../search/searchBaseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>

</head>
<body class="no-skin">
<div class="main-container" id="main-container" style="overflow-x:hidden;">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>
    <div class="main-content">
        <div class="main-content-inner">
            <!-- /.page-header -->

            <div id="page-content">

                <div class="row">
                    <div class="col-xs-12">
                        <div class="widget-body ">

                            <div class="widget-toolbox padding-8 clearfix">
                                <div class="btn-toolbar" role="toolbar">
                                    <div class="btn-group btn-group-sm pull-right">

                                        <button class="btn btn-info" onclick="javascript:history.back(-1)">
                                            <i class="ace-icon fa fa-back"></i>
                                            <span class="bigger-110">返回</span>
                                        </button>

                                    </div>
                                </div>
                            </div>
                            <div class="hr hr-2 hr-dotted"></div>
                            <div class="widget-main">
                                <form class="form-horizontal" role="form">
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right">任务号</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="taskId" value="${HallInventory.taskId}"
                                                   onfocus="blur()"/>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right">盘点日期</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="scanDate"
                                                   value='<fmt:formatDate value="${HallInventory.billDate}" pattern="yyyy-MM-dd HH:mm:ss"/>'
                                                   onfocus="blur()"/>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right">状态</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="status" onfocus="blur()">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right">审核状态</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="isCheck" onfocus="blur()"/>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right">样衣间</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="ownerId"
                                                   value="[${HallInventory.ownerId}]${HallInventory.ownerName}"
                                                   onfocus="blur()"/>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right">扫描设备</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="deviceId" value="${HallInventory.deviceId}"
                                                   onfocus="blur()"/>
                                        </div>

                                    </div>

                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right">库位</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="floor"
                                                   value="[${HallInventory.floor}]${HallInventory.floorName}"
                                                   onfocus="blur()">
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right">应盘数量</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="qty" value="${HallInventory.qty}"
                                                   onfocus="blur()"/>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right">实盘数量</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="actQty" value="${HallInventory.actQty}"
                                                   onfocus="blur()">
                                        </div>
                                    </div>
                                </form>
                            </div>

                            <div class="hr hr-2 hr-dotted"></div>
                            <div>
                                <table id="sampleInventoryGrid"></table>
                            </div>

                            <!-- PAGE CONTENT ENDS -->
                        </div>
                        <!-- /.col -->
                    </div>
                    <!-- /.row -->
                    <!--/#page-content-->
                </div>
            </div>
        </div>

        <!--/.fluid-container#main-container-->
    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>
    <jsp:include page="../search/search_js.jsp"></jsp:include>
</div>
</body>
<script>
    $(function () {
//        iniKendo();
        iniDetails();
        inistatus();
        $(".k-dropdown").css("width", "6em");
        $(".k-grid-toolbar").css("display", "none");//隐藏toolbar
        $(".k-datepicker input").prop("readonly", true);
    });

    function exportExcel() {
        $(".k-grid-excel").click();
    }


    function inistatus() {
        var status = "${HallInventory.status}";
        var isCheck = "${HallInventory.isCheck}";
        var statusText = "";
        var scanStatusText = "";

        switch (status) {
            case "0":
                statusText += "正常";
                break;
            case "1":
                statusText += "盘盈";
                break;
            case "2":
                statusText += "盘亏";
                break;
            case "3":
                statusText += "有盈有亏";
                break;
            case "4":
                statusText += "装箱盘点";
                break;
        }
        $("#status").val(statusText);
        if (isCheck == "CK") {
            $("#isCheck").val("已审核");
        } else {
            $("#isCheck").val("未审核");
        }
    }
    function iniDetails() {

        $("#sampleInventoryGrid").jqGrid({
            url: basePath + "/hall/hallInventoryDetail/page.do?filter_EQS_taskId=${HallInventory.taskId}&filter_EQI_status=1",
            height: "auto",
            datatype: "json",
            mtype: "POST",
            colModel: [
                {
                    name: "status", label: "盘点前状态", width: 70, frozen: true,
                    formatter: function (cellValue, option, rowObject) {
                        var status = cellValue;
                        var statusText = "";
                        switch (status) {
                            case 0:
                                statusText += "未入库";
                                break;
                            case 1:
                                statusText += "在库";
                                break;
                            case 2:
                                statusText += "外借";
                                break;
                            case 3:
                                statusText += "在途";
                                break;
                            case 4:
                                statusText += "报损出库";
                                break;
                            case 5:
                                statusText += "丢失";
                                break;
                            case 6:
                                statusText += "报损中";
                                break;
                            case 7:
                                statusText += "已报损";
                                break;
                            default:
                                statusText += "丢失";
                                break;
                        }
                        return statusText;
                    }
                },
                {
                    name: "scanStatus", label: "是否被盘", width: 70, align: "center", frozen: true,
                    formatter: function (cellValue, options, rowObject) {
                        var scanStatus = cellValue;
                        if (scanStatus == 1) {
                            return "<i class='ace-icon fa fa-check green'></i>";
                        } else {
                            return "<i class='ace-cisn fa fa-close red'></i>";
                        }
                    }
                },
                {name: "code", label: "吊牌码", width: 150},
                {name: "styleId", label: "款式", width: 150},
                {name: "colorId", label: "颜色", width: 150},
                {name: "sizeId", label: "尺码", width: 150}
            ],
            viewrecords: true,
            autowidth: true,
            altRows: true,
            rownumbers: true,
//            rowNum:20,
//            rowList:[20,50,100],
            multiselect: false,
            shrinkToFit: true,
            sortname: "styleId",
            sortorder: "desc",
            autoScroll: false
        });
        $("#sampleInventoryGrid").jqGrid("setFrozenColumns");
    }
</script>
</html>