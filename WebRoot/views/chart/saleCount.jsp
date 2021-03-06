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

                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_GES_day">日期</label>

                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                <div class="input-group">
                                                    <input  id="filter_GES_day" class="form-control date-picker" name="filter_GES_day"
                                                            data-date-format="yyyy-mm-dd"/>
												      <span class="input-group-addon">
															<i class="fa fa-exchange"></i>
                                                      </span>
                                                    <input  id="filter_LES_day" class="form-control date-picker"  name="filter_LES_day"
                                                            data-date-format="yyyy-mm-dd"/>
                                                </div>
                                            </div>
                                             <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_INS_warehId">门店</label>

                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                <select id="filter_INS_warehId"name="filter_INS_warehId" multiple="multiple" data-placeholder="编号列表">
                                                </select>
                                            </div>

                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_EQS_styleId">款号</label>

                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">

                                                <div class="input-group">
                                                    <input class="form-control" id="filter_EQS_styleId"
                                                           type="text" name="filter_EQS_styleId" readonly/>
                                                     <span class="input-group-btn">
                                                         <button class="btn btn-sm btn-default" type="button" onclick="openstyleDialog('#filter_eq_styleId','#filter_eq_styleName')">
                                                             <i class="ace-icon fa fa-list"></i>
                                                         </button>
								                      </span>
                                                    <input class="form-control" id="filter_EQS_styleName"
                                                           type="text" name="EQS_styleName" readonly  placeholder="款名"/>
                                                </div>
                                            </div>
                                        </div>
                                       
                                        <div class="form-group">
                                            
                                             <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_contains_sku">SKU</label>

                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                <input class="form-control" id="filter_contains_sku" name="filter_contains_sku" type="text"
                                                       placeholder="模糊查询"/>
                                            </div>
                                            
                                            
                                        </div>

                                    <!-- #section:elements.form -->
                                    
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

                             


                        <!-- PAGE CONTENT ENDS -->
                    </div>
                    <!-- /.col -->
                </div>

                <div class="row">
                    <!--
                    <div class="col-xs-4">
                        <div id="configurator" class="hidden-on-narrow"></div>
                    </div>
                    -->
                    <div class="col-xs-12">
                    <div id="pivotgrid" class="hidden-on-narrow"  style="height:500px"></div>
                    </div>
                </div>

            </div>
        </div>
    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>
   
    <!--/.fluid-container#main-container-->
</div>

<jsp:include page="../search/search_js.jsp"></jsp:include>
<jsp:include page="../base/style_dialog.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/chart/saleCountController.js"></script>


</body>
</html>