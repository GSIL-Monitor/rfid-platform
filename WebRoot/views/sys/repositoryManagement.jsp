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
        var ownerId = ${ownerId};
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
                    <div class="col-xs-12">
                        <!-- PAGE CONTENT BEGINS -->
                        <div class="widget-body col-md-5">

                            <div class="widget-toolbox padding-8 clearfix">
                                <div class="btn-toolbar" role="toolbar">
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-info" onclick="refresh()">
                                            <i class="ace-icon fa fa-refresh"></i>
                                            <span class="bigger-110">刷新</span>
                                        </button>
                                    </div>

                                    <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                        <input class="form-control" id="search_organizationName" type="text"
                                               placeholder="模糊查询,回车结束"/>
                                    </div>
                                </div>
                            </div>
                            <div class="hr hr-2 hr-dotted"></div>
                            <%--左边展示树形组织架构--%>
                            <div class="col-md-12 col-lg-12">
                                <div class="widget-box widget-color-blue light-border">
                                    <div class="widget-header">
                                        <h5 class="widget-title">仓库信息</h5>
                                        <div class="widget-toolbar no-border">
                                            <button class="btn btn-xs btn-success bigger" onclick="add()">
                                                <i class="ace-icon fa fa-plus"></i>
                                                新增
                                            </button>
                                        </div>
                                    </div>
                                    <div class="widget-body" style="height:600px; overflow-y:auto">
                                        <div class="widget-main no-padding">
                                            <div id="jstree"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>


                        <div class="widget-body">
                                <%--右边展示详细信息--%>
                                <div class="col-xs-7">
                                    <div class="widget-box transparent">
                                        <div class="widget-header ">
                                            <div class="widget-toolbar no-border">
                                                <ul class="nav nav-tabs" id="myTab">
                                                    <li class="active">
                                                        <a data-toggle="tab" href="#skuDetail">SKU明细</a>
                                                    </li>
                                                    <li>
                                                        <a data-toggle="tab" href="#codeDetail">唯一码明细</a>
                                                    </li>
                                                    <li>
                                                        <a data-toggle="tab" href="#styleDetail">款明细</a>
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>
                                        <div class="widget-body" id="wid">
                                            <div class="widget-main padding-12 no-padding-left no-padding-right">
                                                <div class="tab-content padding-4">
                                                    <div id="skuDetail" class="tab-pane  active" style="height:80%;">
                                                        <div class="col-sm-12">
                                                            <div class="col-xs-12 col-sm-12">
                                                                <div class="widget-box widget-color-blue  light-border" id="sku">
                                                                    <div class="widget-header">
                                                                        <h5 class="widget-title">按sku汇总</h5>
                                                                    </div>
                                                                    <table id="gridsku"></table>
                                                                    <div id="grid-pagersku"></div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div id="codeDetail" class="tab-pane" style="height:80%;">
                                                        <div class="col-sm-12">
                                                            <div class="col-xs-12 col-sm-12">
                                                                <div class="widget-box widget-color-blue  light-border" id="code">
                                                                    <div class="widget-header">
                                                                        <h5 class="widget-title">按code汇总</h5>
                                                                    </div>
                                                                    <table id="gridcode"></table>
                                                                    <div id="grid-pagercode"></div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div id="styleDetail" class="tab-pane" style="height:80%;">
                                                        <div class="col-sm-12">
                                                            <div class="col-xs-12 col-sm-12">
                                                                <div class="widget-box widget-color-blue  light-border" id="style">
                                                                    <div class="widget-header">
                                                                        <h5 class="widget-title">按款汇总</h5>
                                                                    </div>
                                                                    <table id="gridstyle"></table>
                                                                    <div id="grid-pagerstyle"></div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
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

    <!--/.fluid-container#main-container-->
</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="repositoryManagement_edit.jsp"></jsp:include>
<jsp:include page="../base/unit_dialog.jsp"></jsp:include>
<link rel="stylesheet" href="<%=basePath%>/font-awesome-4.7.0/css/font-awesome.min.css">
<script type="text/javascript" src="<%=basePath%>/views/sys/repositoryManagement.js"></script>
<script type="text/javascript">

</script>
</body>
</html>