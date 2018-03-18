<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/9/19
  Time: 10:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>

</head>
<body class="no-skin">
<div class="main-container" id="main-container">
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
                                            <i class="ace-icon fa fa-refresh"></i> <span
                                                class="bigger-110">刷新</span>
                                        </button>
                                    </div>
                                </div>
                            </div>

                            <div class="hr hr-2 hr-dotted"></div>

                            <div class="widget-main" id="searchPanel">
                                <form class="form-horizontal" role="form" id="searchForm">
                                    <div class="form-group">
                                        <label class="col-xs-1 control-label text-right" for="search_code">扫描唯一码</label>
                                        <div class="col-xs-2">
                                            <input class="form-control" id="search_code" name="filter_EQS_code"
                                                   type="text"/>
                                        </div>

                                        <label class="col-xs-1 control-label text-right" for="search_SKU">SKU</label>
                                        <div class="col-xs-2">
                                            <input class="form-control" id="search_SKU" name="filter_LIKES_sku"
                                                   type="text"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-1 control-label text-right" for="search_styleId">款号</label>
                                        <div class="col-xs-2">
                                            <input class="form-control" id="search_styleId" name="filter_EQS_styleId"
                                                   type="text"/>
                                        </div>

                                        <label class="col-xs-1 control-label text-right" for="search_colorId">色号</label>
                                        <div class="col-xs-2">
                                            <input class="form-control" id="search_colorId" name="filter_EQS_colorId"
                                                   type="text"/>
                                        </div>

                                        <label class="col-xs-1 control-label text-right" for="search_sizeId">码号</label>
                                        <div class="col-xs-2">
                                            <input class="form-control" id="search_sizeId" name="filter_EQS_sizeId"
                                                   type="text"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-11 btnPosition">
                                            <button type="button" class="btn btn-sm btn-primary" onclick="_search()">
                                                <i class="ace-icon fa fa-search"></i>
                                                <span class="bigger-110">查询</span>
                                            </button>
                                            <button type="reset" class="btn btn-sm btn-warning"
                                                    onclick="_clearSearch()">
                                                <i class="ace-icon fa fa-undo"></i>
                                                <span class="bigger-110">清空</span></button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>

                        <table id="grid" style="background:#ffffff"></table>

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
    <!--/.fluid-container#main-container-->
</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>

<script type="text/javascript" src="<%=basePath%>/views/task/cargoTrackingController.js"></script>
<script type="text/javascript">
    function callback() {
    }
</script>
</body>
</html>