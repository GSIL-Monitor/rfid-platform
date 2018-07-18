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
        var pageType = "pageType";
    </script>
</head>
<body class="no-skin">
<div class="main-container" id="main-container" >
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
                                        <button class="btn btn-info" onclick="_search()">
                                            <i class="ace-icon fa fa-refresh"></i>
                                            <span class="bigger-110">刷新</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button type="button" class="btn btn-primary" onclick="add()">
                                            <i class="ace-icon fa fa-plus"></i>
                                            <span class="bigger-110">增加</span>
                                        </button>
                                        <button type="button" class="btn btn-primary" onclick="edit()">
                                            <i class="ace-icon fa fa-edit"></i>
                                            <span class="bigger-110">编辑</span>
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="hr hr-2 hr-dotted"></div>
                        </div>
                        <table id="grid" style="background:#ffffff"></table>
                        <div id="grid-pager"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="vipCard_edit.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/shop/vipCardController.js"></script>
<link rel="stylesheet" href="<%=basePath%>/Olive/assets/css/bootstrap-colorpicker.min.css" />
<script type="text/javascript" src="<%=basePath%>/Olive/assets/js/bootstrap-colorpicker.min.js"></script>
</body>
</html>