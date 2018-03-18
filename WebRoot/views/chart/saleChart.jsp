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
                    <div class="space-6"></div>
                    <div class="col-sm-6">
                        <div class="widget-box transparent">
                            <div class="widget-header widget-header-flat widget-header-small">
                                <h4 class="widget-title">
                                    商品零售量总统计
                                </h4>

                                <div class="widget-toolbar no-border">
                                    <div class="inline dropdown-hover">
                                        <button class="btn btn-minier btn-primary">
                                            当月
                                            <i class="ace-icon fa fa-angle-down icon-on-right bigger-110"></i>
                                        </button>

                                        <ul class="dropdown-menu dropdown-menu-right dropdown-125 dropdown-lighter dropdown-close dropdown-caret">
                                            <li class="active">
                                                <a href="#" class="blue">
                                                    <i class="ace-icon fa fa-caret-right bigger-110">&nbsp;</i>
                                                    上月
                                                </a>
                                            </li>

                                            <li>
                                                <a href="#">
                                                    <i class="ace-icon fa fa-caret-right bigger-110 invisible">
                                                        &nbsp;</i>
                                                    上周
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>

                            <div class="widget-body">
                                <div class="widget-main no-padding">
                                    <div id="prodQtyChart" style="height: 300px"></div>
                                </div><!-- /.widget-main -->
                            </div><!-- /.widget-body -->
                        </div><!-- /.widget-box -->
                    </div><!-- /.col -->
                    <div class="vspace-12-sm"></div>
                    <div class="col-sm-6">
                        <div class="widget-box transparent">
                            <div class="widget-header widget-header-flat  widget-header-small">
                                <h4 class="widget-title lighter">
                                    商品零售量周统计
                                </h4>

                                <div class="widget-toolbar">
                                    <a href="#" data-action="collapse">
                                        <i class="ace-icon fa fa-chevron-up"></i>
                                    </a>
                                </div>
                            </div>

                            <div class="widget-body">
                                <div class="widget-main no-padding">
                                    <div id="prodWeekChart" style="height: 300px"></div>
                                </div><!-- /.widget-main -->
                            </div><!-- /.widget-body -->
                        </div><!-- /.widget-box -->
                        <!-- PAGE CONTENT ENDS -->
                    </div>
                    <!-- /.col -->
                </div>
                <div class="hr hr32 hr-dotted"></div>

                <div class="row">

                    <div class="col-xs-6">
                        <div class="widget-box transparent">
                            <div class="widget-header widget-header-flat widget-header-small">
                                <h4 class="widget-title">
                                    商品零售量总统计
                                </h4>

                                <div class="widget-toolbar no-border">
                                    <div class="inline dropdown-hover">
                                        <button class="btn btn-minier btn-primary">
                                            当月
                                            <i class="ace-icon fa fa-angle-down icon-on-right bigger-110"></i>
                                        </button>

                                        <ul class="dropdown-menu dropdown-menu-right dropdown-125 dropdown-lighter dropdown-close dropdown-caret">
                                            <li class="active">
                                                <a href="#" class="blue">
                                                    <i class="ace-icon fa fa-caret-right bigger-110">&nbsp;</i>
                                                    上月
                                                </a>
                                            </li>

                                            <li>
                                                <a href="#">
                                                    <i class="ace-icon fa fa-caret-right bigger-110 invisible">
                                                        &nbsp;</i>
                                                    上周
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>

                            <div class="widget-body">
                                <div class="widget-main no-padding">
                        <div id="prodMonthChart" style="height: 600px"></div>
                                </div><!-- /.widget-main -->
                            </div><!-- /.widget-body -->
                        </div><!-- /.widget-box -->
                    </div>
                    <div class="col-xs-6">
                        <div class="widget-box transparent">
                            <div class="widget-header widget-header-flat widget-header-small">
                                <h4 class="widget-title">
                                    商品零售库存对比统计
                                </h4>

                                <div class="widget-toolbar no-border">
                                    <div class="inline dropdown-hover">
                                        <button class="btn btn-minier btn-primary">
                                            当月
                                            <i class="ace-icon fa fa-angle-down icon-on-right bigger-110"></i>
                                        </button>

                                        <ul class="dropdown-menu dropdown-menu-right dropdown-125 dropdown-lighter dropdown-close dropdown-caret">
                                            <li class="active">
                                                <a href="#" class="blue">
                                                    <i class="ace-icon fa fa-caret-right bigger-110">&nbsp;</i>
                                                    上月
                                                </a>
                                            </li>

                                            <li>
                                                <a href="#">
                                                    <i class="ace-icon fa fa-caret-right bigger-110 invisible">
                                                        &nbsp;</i>
                                                    上周
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>

                            <div class="widget-body">
                                <div class="widget-main no-padding">
                        <div id="shopMonthChart" style="height: 600px"></div>
                                </div><!-- /.widget-main -->
                            </div><!-- /.widget-body -->
                        </div><!-- /.widget-box -->
                    </div>
                </div>

            </div>
        </div>
    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>

    <!--/.fluid-container#main-container-->
</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/echarts/echarts.js"></script>
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/echarts/shine.js"></script>
<script type="text/javascript" src="<%=basePath%>/views/chart/saleChartController.js"></script>
</body>
</html>