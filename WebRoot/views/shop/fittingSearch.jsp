
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../search/searchBaseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>
<style>
    .sortFirst{
        color: #75710e;
        font-style: italic;
    }
    .sortSecond{
        color: #6e2f29;
        font-style: italic
    }
</style>

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
                                    <div class="btn-group btn-group-sm pull-right">
                                        <button class="btn btn-info" onclick="showEChartsPanel();">
                                            <i class="ace-icon fa fa-pie-chart"></i>
                                            <span class="bigger-110">图表</span>
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

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label" for="filter_contains_brand">品牌</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="filter_contains_brand" name="filter_contains_brand" type="text"
                                                   placeholder="模糊查询"/>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label" for="filter_in_class2">年份</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select id="filter_in_class2"name="filter_in_class2" multiple="multiple" data-placeholder="年份列表">
                                            </select>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label" for="filter_in_class3">大类</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select id="filter_in_class3"name="filter_in_class3" multiple="multiple" data-placeholder="大类列表">
                                            </select>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label" for="filter_in_class4">小类</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select id="filter_in_class4"name="filter_in_class4" multiple="multiple" data-placeholder="小类列表">
                                            </select>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label" for="filter_in_class10">季节</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select id="filter_in_class10"name="filter_in_class10" multiple="multiple" data-placeholder="季节列表">
                                            </select>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label" for="filter_contains_fitTime">日期</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <div class="input-group">
                                                <input class="form-control date-picker" id="filter_contains_fitTime" name="filter_contains_fitTime" type="text" data-date-format="yyyy-mm-dd" />
																	<span class="input-group-addon">
																		<i class="fa fa-calendar bigger-110"></i>
																	</span>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label" for="filter_contains_shop">店铺</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select id="filter_contains_shop"name="filter_contains_shop" multiple="multiple" data-placeholder="店铺列表">
                                            </select>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label" for="filter_contains_sku">SKU</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="filter_contains_sku" name="filter_contains_sku" type="text"
                                                   placeholder="模糊查询"/>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label" for="filter_contains_styleId">款式</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <div class="input-group">
                                                <input class="form-control" id="filter_contains_styleId"
                                                       type="text" name="filter_contains_styleId" readonly/>
                                                     <span class="input-group-btn" onclick="openstyleDialog('#filter_contains_styleId','#filter_contains_style',null)">
                                                         <button class="btn btn-sm btn-default" type="button" >
                                                             <i class="ace-icon fa fa-list"></i>
                                                         </button>
								                      </span>
                                                <input class="form-control" id="filter_contains_style"
                                                       type="text" name="filter_contains_style" readonly  placeholder="款名"/>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label" for="filter_contains_colorId">颜色</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="filter_contains_colorId" name="filter_contains_colorId" type="text"
                                                   placeholder="模糊查询"/>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label" for="filter_contains_sizeId">尺码</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="filter_contains_sizeId" name="filter_contains_sizeId" type="text"
                                                   placeholder="模糊查询"/>
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
                            <div class="row" id="EchartArea" style="overflow: scroll;padding-right: 20px">
                                <div id="fittime" class="col-lg-2 col-xs-2" style="height: 300px;text-align:center;" >
                                    <div class="infobox infobox-green infobox-small infobox-dark" style="margin-top: 40px">
                                        <div class="infobox-data" id="sumYear">

                                        </div>
                                    </div>
                                    <div class="infobox infobox-blue infobox-small infobox-dark" >
                                        <div class="infobox-data" id="sumMonth">

                                        </div>
                                    </div>
                                    <div class="infobox infobox-grey infobox-small infobox-dark">
                                        <div class="infobox-data" id="sumWeek">

                                        </div>
                                    </div>
                                    <div class="infobox infobox-red infobox-small infobox-dark">
                                        <div class="infobox-data" id="sumDay">

                                        </div>
                                    </div>
                                </div>

                                <div id="shop" class="col-lg-2 col-xs-2" style="line-height: 1" >
                                    <div class="widget-box widget-color-blue">
                                        <div class="widget-header">
                                            <h5 class="widget-title bigger lighter">款式试衣排行</h5>
                                        </div>
                                        <div class="widget-body">
                                            <div class="widget-main" id="sortStyle">

                                            </div>
                                        </div>
                                    </div>

                                </div>
                                <div id="style" class="col-lg-2 col-xs-2" style="line-height: 1" >
                                    <div class="widget-box widget-color-orange">
                                        <div class="widget-header">
                                            <h5 class="widget-title bigger lighter">门店试衣排行</h5>
                                        </div>
                                        <div class="widget-body">
                                            <div class="widget-main" id="sortShop">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div id="color" class="col-lg-2 col-xs-2" style="line-height: 1" >
                                    <div class="widget-box widget-color-orange">
                                        <div class="widget-header">
                                            <h5 class="widget-title bigger lighter">试衣颜色排行</h5>
                                        </div>
                                        <div class="widget-body">
                                            <div class="widget-main" id="sortColor">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div id="class3" class="col-lg-2 col-xs-2" style="height: 300px" ></div>
                                <div id="class10" class="col-lg-2 col-xs-2" style="height: 300px"></div>
                            </div>
                        <div id="fittingGrid" style="height:500px"></div>

                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>

</div>

<jsp:include page="../search/search_js.jsp"></jsp:include>
<jsp:include page="../base/style_dialog.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/echarts/echarts.js"></script>
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/echarts/shine.js"></script>
<script type="text/javascript" src="<%=basePath%>/views/shop/fittingSearchController.js"></script>


</body>
</html>