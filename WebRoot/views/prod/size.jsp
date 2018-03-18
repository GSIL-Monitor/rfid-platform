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
                                            <i class="ace-icon fa fa-refresh"></i>
                                            <span class="bigger-110">刷新</span>
                                        </button>
                                    </div>

                                </div>
                            </div>


                        </div>
                        <div class="hr hr-2 hr-dotted"></div>

                        <div class="row">
                            <div class="col-xs-12 col-sm-6 widget-container-col">
                                <div class="widget-box widget-color-blue  light-border">
                                    <div class="widget-header">
                                        <h5 class="widget-title">尺寸组</h5>

                                        <div class="widget-toolbar">
                                            <a href="#" data-action="reload">
                                                <i class="ace-icon fa fa-refresh"></i>
                                            </a>
                                            <a href="#" data-action="collapse">
                                                <i class="1 ace-icon fa fa-chevron-up bigger-125"></i>
                                            </a>
                                        </div>

                                        <div class="widget-toolbar no-border">

                                        </div>
                                    </div>
                                    <div class="widget-body">
                                        <div class="widget-main  no-padding">

                                            <table id="sortgrid"></table>
                                            <div id="grid-pager"></div>


                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 widget-container-col">
                                <div class="widget-box widget-color-blue light-border">
                                    <div class="widget-header">
                                        <h5 class="widget-title">尺寸</h5>

                                        <div class="widget-toolbar">
                                            <a href="#" data-action="reload">
                                                <i class="ace-icon fa fa-refresh"></i>
                                            </a>
                                            <a href="#" data-action="collapse">
                                                <i class="1 ace-icon fa fa-chevron-up bigger-125"></i>
                                            </a>
                                            <a href="#" data-action="fullscreen" class="orange2" onclick="fullScreen()">
                                                <i class="ace-icon fa fa-expand"></i>
                                            </a>
                                        </div>

                                        <div class="widget-toolbar no-border">

                                        </div>
                                    </div>
                                    <div class="widget-body">
                                        <div class="widget-main no-padding">
                                            <table id="setgrid"></table>
                                            <div id="grid-pager2"></div>

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

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

<jsp:include page="size_sort_edit.jsp"></jsp:include>
<jsp:include page="size_edit.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/prod/sizeController.js"></script>
<script type="text/javascript"></script>
</body>
</html>