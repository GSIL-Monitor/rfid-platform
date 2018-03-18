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
                    <div class="col-xs-12 col-sm-6">
                        <div class="widget-box widget-color-blue  light-border">

                            <div class="widget-header">

                                <h5 class="widget-title">访问日志查询</h5>
                                <div class="widget-toolbar padding-5 no-border">

                                    <a href="#" data-action="collapse">
                                        <i class="ace-icon fa fa-chevron-up"></i>
                                    </a>
                                </div>


                            </div>
                            <div class="widget-body">
                                <div class="widget-main">
                                    <form class="form-horizontal" role="form" id="server_searchForm">
                                        <div class="form-group">
                                            <label class="col-xs-1 control-label" for="search_method">URL</label>
                                            <div class="col-xs-2">
                                                <input class="form-control" id="search_method" name="filter_LIKES_method"
                                                       type="text"
                                                       placeholder="模糊查询"/>
                                            </div>

                                            <label class="col-xs-2 control-label" for="search_type">日志类型</label>
                                            <div class="col-xs-3">
                                                <select class="chosen-select form-control" id="search_type" name="filter_LIKES_type">
                                                    <option value="">--请选择--</option>
                                                    <option value="SYS">SYS</option>
                                                    <option value="SYN">SYN</option>
                                                    <option value="API">API</option>
                                                    <option value="LTM">LONG_TIME</option>
                                                </select>
                                            </div>

                                            <div class="col-xs-2">
                                                <button type="button" class="btn btn-sm btn-primary" onclick="_server_search()">
                                                    <i class="ace-icon fa fa-search"></i>
                                                    <span class="bigger-80">查询</span>
                                                </button>
                                              </div>

                                        </div>

                                    </form>

                                </div>

                                <table id="server_grid"></table>
                                <div id="server_grid-pager"></div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xs-12 col-sm-6">
                        <div class="widget-box widget-color-blue  light-border">

                            <div class="widget-header">

                                <h5 class="widget-title">更新日志查询</h5>
                                <div class="widget-toolbar padding-5 no-border">
                                    <a href="#" data-action="collapse" >
                                        <i class="ace-icon fa fa-chevron-up"></i>
                                    </a>
                                </div>


                            </div>
                            <div class="widget-body">
                                <div class="widget-main">
                                    <form class="form-horizontal" role="form" id="table_searchForm">
                                        <div class="form-group">
                                            <label class="col-xs-2 control-label" for="table_search_tableName">表名</label>

                                            <div class="col-xs-2">
                                                <input class="form-control" id="table_search_tableName" name="filter_LIKES_tableName"
                                                       type="text"
                                                       placeholder="模糊查询"/>
                                            </div>

                                            <div class="col-xs-2">
                                                <button type="button" class="btn btn-sm btn-primary" onclick="_table_search()">
                                                    <i class="ace-icon fa fa-search"></i>
                                                    <span class="bigger-80">查询</span>
                                                </button>
                                            </div>

                                        </div>

                                    </form>

                                </div>

                                <table id="table_grid"></table>
                                <div id="table_grid-pager"></div>
                            </div>
                        </div>
                    </div>

                </div>


                <!--/#page-content-->
            </div>
        </div>
    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>
    <!--/.fluid-container#main-container-->
</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>

<script type="text/javascript" src="<%=basePath%>/views/log/serverLogController.js"></script>
</body>
</html>