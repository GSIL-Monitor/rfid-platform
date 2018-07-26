<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/11/6
  Time: 14:56
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
        var userId = "${userId}";
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
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-info" onclick="refresh()">
                                            <i class="ace-icon fa fa-refresh"></i>
                                            <span class="bigger-110">刷新</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-right">
                                        <button type="button" class="btn btn-info btn-yellow"  onclick="history.back(-1)">
                                            <i class="ace-icon fa fa-arrow-left"></i>
                                            <span class="bigger-110">返回</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-right">
                                        <button type="button" class="btn btn-info" onclick="showAdvSearchPanel();">
                                            <i class="ace-icon fa fa-binoculars"></i>
                                            <span class="bigger-110">高级查询</span>
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="hr hr4"></div>

                            <div class="widget-main" id="searchPanel" style="display:none">
                                <form class="form-horizontal" role="form" id="searchForm">
                                    <div class="form-group">
                                        <label class="col-xs-1 control-label" for="search_status">替换结果</label>
                                        <div class="col-xs-2">
                                            <select class="form-control" id="search_status" name="filter_EQI_status">
                                                <option value="">--选择替换结果--</option>
                                                <option value="1">替换成功</option>
                                                <option value="0">替换失败</option>
                                            </select>
                                        </div>

                                        <label class="col-xs-1 control-label" for="search_origCode">原唯一码</label>
                                        <div class="col-xs-2">
                                            <input class="form-control" id="search_origCode" name="filter_EQS_origCode"/>
                                        </div>

                                        <label class="col-xs-1 control-label" for="search_origSku">原SKU</label>
                                        <div class="col-xs-2">
                                            <input class="form-control" id="search_origSku" name="filter_LIKES_origSku" type="text"/>
                                        </div>

                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-1 control-label" for="search_recordDate">替换时间</label>
                                        <div class="col-xs-2">
                                            <div class="input-group">
                                                <input class="form-control date-picker" id="search_recordDate"
                                                       type="text" name="filter_GED_recordDate"
                                                       data-date-format="yyyy-mm-dd"/>
                                                <span class="input-group-addon">
                                                    <i class="fa fa-exchange"></i>
                                                </span>
                                                <input class="form-control date-picker"
                                                       type="text" name="filter_LED_recordDate"
                                                       data-date-format="yyyy-mm-dd"/>
                                            </div>
                                        </div>

                                        <label class="col-xs-1 control-label" for="search_newCode">新唯一码</label>
                                        <div class="col-xs-2">
                                            <input class="form-control" id="search_newCode" name="filter_EQS_newCode" type="text"/>
                                        </div>

                                        <label class="col-xs-1 control-label" for="search_newSku">新SKU</label>
                                        <div class="col-xs-2">
                                            <input class="form-control" id="search_newSku" name="filter_LIKES_newSku" type="text"/>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <div class="col-sm-offset-5 col-sm-10">
                                            <button type="button" class="btn btn-sm btn-primary" onclick="_search()">
                                                <i class="ace-icon fa fa-search"></i>
                                                <span class="bigger-110">查询</span>
                                            </button>
                                            <button type="reset" class="btn btn-sm btn-warning">
                                                <i class="ace-icon fa fa-undo"></i>
                                                <span class="bigger-110">清空</span>
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <table id="grid"></table>
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

</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<script>
    $(function () {
        initGrid();
    });

    function initGrid() {
        $("#grid").jqGrid({
            height: "auto",
            mtype: "POST",
            url: basePath + "/tag/tagReplace/findRecordPage.do",
            datatype: "json",
            colModel: [
                {name: 'id', label: 'id', hidden: true, key: true},
                {name: 'status', label: '', hidden: true},
                {
                    name: '', label: '替换结果', width: 200,
                    formatter: function (cellValue, option, object) {
                        switch (object.status){
                            case 1:
                                return "替换成功";
                            case 2:
                                return "替换失败";
                        }
                    }
                },
                {name: 'recordDate', label: '替换时间', width: 250},
                {name: 'origCode', label: '原唯一码',  width: 200},
                {name: 'origSku', label: '原SKU',  width: 200},
                {name: 'newCode', label: '新唯一码', sortable: false, width: 200},
                {name: 'newSku', label: '新SKU', width: 200},
                {name: 'oprId', label: '操作人', width: 200}
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
    }

    function refresh() {
        location.reload(true);
    }

    function showAdvSearchPanel() {
        $("#searchPanel").slideToggle("fast");
    }

    function _search() {

        var serializeArray = $("#searchForm").serializeArray();
        var params = array2obj(serializeArray);
        $("#grid").jqGrid('setGridParam', {
            page: 1,
//            url: basePath + "/tag/tagReplace/findRecordPage.do",
            postData: params
        });
        $("#grid").trigger("reloadGrid");
    }
</script>
</body>
</html>
