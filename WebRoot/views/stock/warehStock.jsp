<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <jsp:include page="../search/searchBaseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
        var ownerId = "${ownerId}";
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
                                <div class="btn-group btn-group-sm pull-left">
                                    <button class="btn btn-info" onclick="stockeSkuDetail();">
                                        <i class="ace-icon"></i>
                                        <span class="bigger-110">按sku汇总</span>
                                    </button>
                                </div>
                                <div class="btn-group btn-group-sm pull-left">
                                    <button class="btn btn-info" onclick="stockeCodeDetail();">
                                        <i class="ace-icon"></i>
                                        <span class="bigger-110">按code汇总</span>
                                    </button>
                                </div>
                                <div class="btn-group btn-group-sm pull-left">
                                    <button class="btn btn-info" onclick="stockestyleIdDetail();">
                                        <i class="ace-icon"></i>
                                        <span class="bigger-110">按款汇总</span>
                                    </button>
                                </div>

                            </div>

                            <div class="widget-toolbox padding-8 clearfix">
                                <div class="btn-toolbar" role="toolbar">
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-info" onclick="refresh();">
                                            <i class="ace-icon fa fa-refresh"></i>
                                            <span class="bigger-110">刷新</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-info" onclick="collapseGroup('#stockGrid');">
                                            <i class="ace-icon fa fa-chevron-up"></i>
                                            <span class="bigger-110">折叠分组</span>
                                        </button>
                                        <button class="btn  btn-info" onclick="expandGroup('#stockGrid');">
                                            <i class="ace-icon fa fa-chevron-down"></i>
                                            <span class="bigger-110">展开分组</span>
                                        </button>

                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">

                                        <button class="btn btn-info" onclick="exportExcel();">

                                            <i class="ace-icon fa fa-file-excel-o"></i>
                                            <span class="bigger-110">导出旧</span>

                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">

                                        <button class="btn btn-info" onclick="exportExcelPOI();">

                                            <i class="ace-icon fa fa-file-excel-o"></i>
                                            <span class="bigger-110">导出新</span>

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
                            <div class="hr hr-2 hr-dotted"></div>
                            <div class="widget-main" id="searchPanel" style="display:none;">
                                <form class="form-horizontal" role="form" id="searchForm">
                                    <div class="form-group">


                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_in_warehId">仓库</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <!-- <input class="form-control" id="filter_contains_warehId" name="filter_contains_warehId" type="text"
                                                   placeholder="模糊查询"/> -->
                                            <select id="filter_in_warehId" name="filter_in_warehId" multiple="multiple" data-placeholder="编号列表">
                                            </select>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_contains_sku">SKU</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="filter_contains_sku" name="filter_contains_sku" type="text" onkeyup="this.value=this.value.toUpperCase()"
                                                   placeholder="模糊查询"/>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_eq_styleId">款号</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <!-- <input class="form-control" id="filter_contains_styleName" name="filter_contains_styleName" type="text"
                                                   placeholder="模糊查询"/> -->
                                            <div class="input-group">
                                                <input class="form-control" id="filter_eq_styleId"
                                                       type="text" name="filter_eq_styleId" readonly/>
                                                     <span class="input-group-btn">
                                                         <button class="btn btn-sm btn-default" type="button" onclick="openstyleDialog('#filter_eq_styleId','#filter_eq_styleName')">
                                                             <i class="ace-icon fa fa-list"></i>
                                                         </button>
								                      </span>
                                                <input class="form-control" id="filter_eq_styleName"
                                                       type="text" name="filter_eq_styleName" readonly  placeholder="款名"/>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_contains_colorId">色号</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="filter_contains_colorId" name="filter_contains_colorId" type="text" 
                                                   placeholder="模糊查询"/>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_contains_sizeId">尺号</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3 ">
                                            <input class="form-control" id="filter_contains_sizeId" name="filter_contains_sizeId" type="text" onkeyup="this.value=this.value.toUpperCase()"
                                                   placeholder="模糊查询"/>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_eq_class1">厂家</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <!-- <input class="form-control" id="filter_contains_styleName" name="filter_contains_styleName" type="text"
                                                   placeholder="模糊查询"/> -->
                                            <div class="input-group">
                                                <input class="form-control" id="filter_eq_class1"
                                                       type="text" name="filter_eq_class1" readonly/>
                                                <span class="input-group-btn">
                                                         <button class="btn btn-sm btn-default" type="button" onclick="initClass1Select_Grid('#filter_eq_class1','#filter_eq_class1Name')">
                                                             <i class="ace-icon fa fa-list"></i>
                                                         </button>
								                      </span>
                                                <input class="form-control" id="filter_eq_class1Name"
                                                       type="text" name="filter_eq_class1Name" readonly  placeholder="厂家"/>
                                            </div>
                                            <%--<select class="form-control" id="filter_eq_destunitid" name="filter_eq_destunitid"
                                                    style="width: 100%;" >
                                            </select>--%>
                                        </div>


                                    </div>
                                    <!-- #section:elements.form -->

                                    <div class="form-group">
                                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-11 btnPosition">
                                            <button type="button" class="btn btn-sm btn-primary" onclick="search();">
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

                        <div id="stockGrid" style="height:580px"></div>
                        <div id="stockCodeeGrid" style="height:580px;display: none"></div>
                        <div id="stockstyleGrid" style="height:580px;display: none"></div>
                        <form id="form1" action="" method=post name=form1 style='display:none'>
                            <input id="request" type=hidden  name='request' value=''>
                        </form>

                        <!-- PAGE CONTENT ENDS -->
                    </div>
                    <div id ="divshowImage" class="divshowImage" style="display: none">
                        <img class="showImage" id="showImage" onclick="hideImage()">
                    </div>
                    <!-- /.col -->
                </div>
                <!-- /.row -->
                <!--/#page-content-->
            </div>
        </div>
    </div>
    <jsp:include page="../search/search_js.jsp"></jsp:include>
	<jsp:include page="../layout/footer.jsp"></jsp:include>

    <!--/.fluid-container#main-container-->
</div>
	<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<%--<jsp:include page="../search/search_js.jsp"></jsp:include>--%>
<jsp:include page="../base/style_dialog.jsp"></jsp:include>
<jsp:include page="inStockCode_list.jsp"></jsp:include>
<jsp:include page="../base/search_class1_dialog.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/stock/warehStockController.js"></script>
<script>
    function stockeSkuDetail() {
        $("#stockGrid").show();
        $("#stockCodeeGrid").hide();
        $("#stockstyleGrid").hide();
        exportExcelid="stockGrid";
        //$("#" + exportExcelid).children().find(".k-grid-excel").hide()
        initKendoUIGrid();
    }
    function stockeCodeDetail() {
        $("#stockCodeeGrid").show();
        $("#stockGrid").hide();
        $("#stockstyleGrid").hide();
        exportExcelid="stockCodeeGrid";
        //$("#" + exportExcelid).children().find(".k-grid-excel").hide()
        initCodeKendoUIGrid();
    }
    function stockestyleIdDetail() {
        $("#stockCodeeGrid").hide();
        $("#stockGrid").hide();
        $("#stockstyleGrid").show();
        exportExcelid="stockstyleGrid";
        initstyleKendoUIGrid();
    }


</script>
</body>
</html>