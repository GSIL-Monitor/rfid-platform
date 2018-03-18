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
                    <div class="col-sm-9">
                         <div id="chart" style="width:100%;height:900px"></div>
                    </div><!-- /.col -->
                    <div class="col-sm-3">
                        <div class="widget-body">
                            <div class="widget-toolbox padding-8 clearfix">
                                <div class="btn-toolbar" role="toolbar">
                                    <div class="btn-group btn-group-sm pull-left" onclick="refresh()">
                                        <button class="btn btn-info">
                                            <i class="ace-icon fa fa-refresh"></i> <span
                                                class="bigger-110">刷新</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-right">

                                        <button type="button" class="btn btn-info" onclick="showAdvSearchPanel()">
                                            <i class="ace-icon fa fa-binoculars"></i>
                                            <span class="bigger-110">高级查询</span>


                                        </button>

                                    </div>
                                </div>
                            </div>

                            <div class="widget-main no-padding">

                            </div><!-- /.widget-main -->
                        </div><!-- /.widget-body -->
                    </div><!-- /.col -->
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
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/echarts/china.js"></script>
<script type="text/javascript" src="<%=basePath%>/views/chart/areaSaleController.js"></script>
</body>
</html>