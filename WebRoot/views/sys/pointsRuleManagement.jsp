<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/11/3
  Time: 15:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
        var ownerId = "${ownerId}";
        var userId = "${userId}"
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
            <!-- /.page-header -->

            <div id="page-content">

                <div class="row">
                    <div class="col-xs-12">
                        <!-- PAGE CONTENT BEGINS -->
                        <div class="widget-body">
                            <div class="widget-toolbox padding-8 clearfix">
                                <div class="btn-toolbar" role="toolbar">
                                    <div class="btn-group btn-group-sm pull-left" onclick="refresh()">
                                        <button class="btn btn-info">
                                            <i class="ace-icon fa fa-refresh"></i> <span
                                                class="bigger-110">刷新</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-primary" onclick="add()">
                                            <i class="ace-icon fa fa-plus"></i> <span class="bigger-110">新增规则</span>
                                        </button>
                                        <%--<button class="btn btn-primary" onclick="">--%>
                                        <%--<i class="ace-icon fa fa-remove"></i> <span class="bigger-110">废除所有规则</span>--%>
                                        <%--</button>--%>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-right">
                                        <button type="button" class="btn btn-info" onclick="showAdvSearchPanel()">
                                            <i class="ace-icon fa fa-binoculars"></i>
                                            <span class="bigger-110">高级查询</span>
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="hr hr-2 hr-dotted"></div>
                            <div class="widget-main" id="searchPanel" hidden>
                                <form class="form-horizontal" role="form" id="searchForm">
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               for="search_unitId">所属门店</label>
                                        <div class="col-xs-8 col-sm-8 col-md-2 col-lg-2">
                                            <div class="input-group">
                                                <input class="form-control" id="search_unitId" type="text"
                                                       name="filter_EQS_unitId" readonly/>
                                                <span class="input-group-btn">
                                                    <button class="btn btn-sm btn-default" id="setOwnerId" type="button"
                                                            onclick="openShopSelectDialog('#search_unitId','#search_unitName','')">
                                                        <i class="ace-icon fa fa-list"></i>
                                                    </button>
                                                </span>
                                                <input class="form-control" id="search_unitName" type="text"
                                                       name="unitName" readonly/>
                                            </div>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               for="search_name">启用状态</label>
                                        <div class="col-xs-8 col-sm-8 col-md-2 col-lg-2">
                                            <select class="form-control" id="search_name" name="filter_EQI_status">
                                                <option value="">--请选择状态--</option>
                                                <option value="1">启用</option>
                                                <option value="0">废除</option>
                                            </select>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label no-padding-right text-right"
                                               for="search_startDate">有效日期</label>
                                        <div class="col-xs-8 col-sm-8 col-md-2 col-lg-2">
                                            <div class="input-group">
                                                <input class="form-control date-picker" id="search_startDate"
                                                       type="text"
                                                       name="filter_GED_startDate" data-date-format="yyyy-mm-dd"/>
                                                <span class="input-group-addon">
                                                    <i class="fa fa-exchange"></i>
                                                </span>
                                                <input class="form-control date-picker" id="search_endDate" type="text"
                                                       name="filter_LED_endDate" data-date-format="yyyy-mm-dd"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-11 btnPosition">
                                            <button type="button" class="btn btn-sm btn-primary"
                                                    onclick="_search()">
                                                <i class="ace-icon fa fa-search"></i> <span
                                                    class="bigger-110">查询</span>
                                            </button>
                                            <button type="reset" class="btn btn-sm btn-warning"
                                                    onclick="_clearSearch()">
                                                <i class="ace-icon fa fa-undo"></i>
                                                <span class="bigger-110">清空</span>
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <table id="grid" style="background: #ffffff"></table>
                        <div id="grid-pager"></div>
                        <!-- PAGE CONTENT ENDS -->
                    </div>
                    <!-- /.col -->
                </div>
                <!-- /.row -->
                <!--/#page-content-->
            </div>
        </div>
    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>
</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="pointsRuleManagement_dialog.jsp"></jsp:include>
<jsp:include page="../base/shop_dialog.jsp"></jsp:include>

<script type="text/javascript">
    $(function () {
        initGrid();
    });

    var url;

    function initGrid() {
        if (ownerId === 1 || ownerId === "1") {
            url = basePath + "/sys/pointsRule/page.do";
        }else {
            url = basePath + "/sys/pointsRule/page.do?filter_EQS_unitId=" + ownerId;
        }
        $("#grid").jqGrid({
            height: "auto",
            mtype: "POST",
            url: url,
            datatype: "json",
            colModel: [
                {name: 'id', label: 'id', hidden: true, key: true},
                {name: 'unitId', label: 'unitId', hidden: true, width: 150},
                {name: 'unitName', label: '门店', width: 150},
                {
                    name: '', label: '编辑', width: 100, align: 'center',
                    formatter: function (cellValue, option, rowObject) {
                        return "<a href='#' onclick=loadDetail('" + rowObject.id + "')><i class='ace-icon fa fa-edit' title='编辑'></i></a>";
                    }
                },
                {name: 'status', label: '', hidden: true},
                {
                    name: '', label: '状态', width: 150,
                    formatter: function (cellValue, option, rowObject) {
                        switch (rowObject.status) {
                            case 1:
                                return "启用";
                            case 0:
                                return "废除";
                        }
                    }
                },
                {name: 'defaultRule', label: '', hidden: true},
                {
                    name: 'ruleDefault', label: '默认规则', width: 150,
                    formatter: function (cellValue, option, rowObject) {
                        switch (rowObject.defaultRule) {
                            case true:
                                return "是";
                            case false:
                                return "否";
                        }
                    }
                },
                {name: 'startDate', label: '开始时间', width: 200},
                {name: 'endDate', label: '结束时间', width: 200},
                {name: 'unitPoints', label: '每100元对应积分', width: 150},
                {name: 'ownerId', label: '所属方', hidden: true, width: 150},
                {name: 'remark', label: '备注', sortable: false, width: 400}
            ],
            viewrecords: true,
            autowidth: true,
            rownumbers: true,
            altRows: true,
            rowNum: 20,
            rowList: [20, 50, 100],
            pager: "#grid-pager",
            multiselect: false,
            shrinkToFit: false,
            sortname: 'id',
            sortorder: "asc",
            autoScroll: false
        });
        if (ownerId === 1 || ownerId === "1") {
            $("#grid").showCol('ruleDefault');
        } else {
            $("#grid").hideCol('ruleDefault');
        }

    }
    function add() {

        $('#ruleManagement_dialog').modal('show');
//        $("#ruleManagement_dialog").on('shown.bs.modal', function () {
        $("#ruleManagementForm").resetForm();
        if (ownerId === 1 || ownerId === "1") {
            $("#div_defaultRule").show();
        } else {
            $("#form_unitId").val(ownerId);
        }
//        }).modal('show');
    }

    function loadDetail(id) {
        var rowData = $("#grid").jqGrid('getRowData', id);
        $('#ruleManagement_dialog').modal('show');
        $('#form_unitId').attr('disabled',true);
        $('#form_startDate').attr('disabled', true);
        $('#form_endDate').attr('disabled', true);
        $('#ruleManagementForm').loadData(rowData);
        if (ownerId === 1 || ownerId === "1") {
            $("#div_defaultRule").show();
        }
    }

    function refresh() {
        location.reload(true);
    }

    function _search() {
        var serializeArray = $("#searchForm").serializeArray();
        var params = array2obj(serializeArray);
        $("#grid").jqGrid('setGridParam', {
            url: basePath + "/sys/pointsRule/page.do",
            page: 1,
            postData: params
        });
        $("#grid").trigger("reloadGrid");
    }
    function showAdvSearchPanel() {
        $("#searchPanel").slideToggle("fast");
    }
    function _clearSearch() {

    }
</script>
</body>
</html>