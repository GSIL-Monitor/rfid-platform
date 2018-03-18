<%@ page pageEncoding="UTF-8" language="java" import="java.util.*" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script>
        var basePath = "<%=basePath%>";
    </script>
</head>
<body class="no-skin">
<div class="main-container" id="main-container">
    <script>
        try {
            ace.settings.check("main-container", fixed)
        } catch (e) {
        }
    </script>
    <div class="main-content">
        <div class="main-content-inner">
            <div class="widget-body">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="widget-body">
                            <div class="widget-toolbox padding-8 clearfix">
                                <div class="btn-toolbar" role="toolbar">
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-info " onclick="refresh()">
                                            <i class="ace-icon fa fa-refresh"></i>
                                            <span class="bigger-110">刷新</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button type="button" class="btn btn-primary" onclick="addArea()">
                                            <i class="ace-icon fa fa-plus"></i>
                                            <span class="bigger-110">增加分区</span>
                                        </button>
                                        <button type="button" class="btn btn-primary" onclick="editArea()">
                                            <i class="ace-icon fa fa-edit"></i>
                                            <span class="bigger-110">编辑分区</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left col-md-offset-5">
                                        <button type="button" class="btn btn-primary" onclick="addStorage()">
                                            <i class="ace-icon fa fa-plus"></i>
                                            <span class="bigger-110">增加库位</span>
                                        </button>
                                        <button type="button" class="btn btn-primary" onclick="editStorage()">
                                            <i class="ace-icon fa fa-edit"></i>
                                            <span class="bigger-110">编辑库位</span>
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
                            <div class="hr hr-2 hr-dotted"></div>
                            <div class="widget-main" id="searchPanel" style="display:none">
                                <form class="form-horizontal" id="searchForm">
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               for="filter_EQS_code">分区编号</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" name="filter_LIKES_code" id="filter_EQS_code" onkeyup="this.value=this.value.toUpperCase()"
                                                   placeholder="模糊查询">
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               for="filter_EQS_name">分区名称</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" name="filter_LIKES_name" id="filter_EQS_name"
                                                   placeholder="模糊查询">
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               for="filter_EQI_status">分区状态</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="form-control chosen-select" name="filter_EQI_status" id="filter_EQI_status">
                                                <option value="">-请选择-</option>
                                                <option value="0">停用</option>
                                                <option value="1">启用</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right">所属展厅</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="form-control chosen-select" name="filter_EQS_ownerId" id="filter_EQS_ownerId">
                                                <option value="">-请选择-</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-11 btnPosition">
                                            <button type="button" class="btn btn-sm btn-primary" onclick="search()">
                                                <i class="ace-icon fa fa-search"></i>
                                                <span class="bigger-110">查询</span>
                                            </button>
                                            <button type="reset" class="btn btn-sm btn-warning">
                                                <i class="ace-icon fa fa-undo"></i>
                                                <span class="bigger-110">清空</span></button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
                            <table id="Agrid"></table>
                            <table id="Agrid-pager"></table>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
                            <table id="Egrid"></table>
                            <table id="Egrid-pager"></table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>

</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="hallFloor_edit_dialog.jsp"></jsp:include>
<jsp:include page="hallStorage_edit_dialog.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/hall/hallFloorController.js"></script>
</body>
</html>
