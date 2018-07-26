<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/11/15
  Time: 16:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%

    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="searchBaseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>
</head>
<body>

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
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-info" onclick="refresh();">
                                            <i class="ace-icon fa fa-refresh"></i>
                                            <span class="bigger-110">刷新</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-info" onclick="exportExcel();">
                                            <i class="ace-icon fa fa-file-excel-o"></i>
                                            <span class="bigger-110">导出</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-info" onclick="collapseGroup('#stockAnalysisGrid');">
                                            <i class="ace-icon fa fa-chevron-up"></i>
                                            <span class="bigger-110">折叠分组</span>
                                        </button>
                                        <button class="btn  btn-info" onclick="expandGroup('#stockAnalysisGrid');">
                                            <i class="ace-icon fa fa-chevron-down"></i>
                                            <span class="bigger-110">展开分组</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-right">
                                        <button class="btn btn-info" onclick="showAdvSearchPanel();">
                                            <i class="ace-icon fa fa-binoculars"></i>
                                            <span class="bigger-110">高级查询</span>
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="widget-main" id="searchPanel" style="display:none;">
                                <form class="form-horizontal" role="form" id="searchForm">
                                    <div class="form-group">
                                        <div id="wareHouseId">
                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="form_wareHouseId">仓库</label>
                                            <div class="col-xs-8 col-sm-8 col-md-2 col-lg-2">
                                                <select class="form-control" id="form_wareHouseId" name="filter_eq_wareHouseId" type="text">
                                                </select>
                                            </div>
                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="form_inStockQty">在库数量大于</label>
                                            <div class="col-xs-8 col-sm-8 col-md-2 col-lg-2">
                                                <input class="form-control" id="form_inStockQty" name="filter_gte_inStockQty" type="number"/>
                                            </div>
                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="form_inStockTime">在库时长大于</label>
                                            <div class="col-xs-8 col-sm-8 col-md-2 col-lg-2">
                                                <input class="form-control" id="form_inStockTime" name="filter_gte_inStockTime" type="number"/>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <div class="col-sm-offset-5 col-sm-10">
                                            <button type="button" class="btn btn-sm btn-primary" onclick="search();">
                                                <i class="ace-icon fa fa-search"></i>
                                                <span class="bigger-110">查询</span>
                                            </button>
                                            <button type="reset" class="btn btn-sm btn-warning" onclick="resetData();">
                                                <i class="ace-icon fa fa-undo"></i>
                                                <span class="bigger-110">清空</span></button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>

                        <div id="stockAnalysisGrid" style="height:800px"></div>
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

<jsp:include page="search_js.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/search/stockAnalysisController.js"></script>

</body>
</html>
