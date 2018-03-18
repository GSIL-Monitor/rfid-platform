<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../../baseView.jsp"></jsp:include>
    <jsp:include page="../../search/searchBaseView.jsp"></jsp:include>
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
                                        <button class="btn btn-primary" onclick="add()">
                                            <i class="ace-icon fa fa-plus"></i>
                                            <span class="bigger-110">增加</span>
                                        </button>
                                        <button class="btn btn-primary" onclick="edit()">
                                            <i class="ace-icon fa fa-edit"></i>
                                            <span class="bigger-110">编辑</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-right">

                                        <button class="btn btn-info" onclick="showAdvSearchPanel()">
                                            <i class="ace-icon fa fa-binoculars"></i>
                                            <span class="bigger-110">高级查询</span>
                                        </button>

                                    </div>
                                </div>
                            </div>
                            <div class="widget-main" id="searchPanel" style="display:none;">
                                <form class="form-horizontal" role="form" id="searchForm">
                                    <div class="form-group">

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="filter_CONTAINS_toUser">收件人</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="filter_CONTAINS_toUser" name="filter_CONTAINS_toUser"
                                                   type="text"  />
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="filter_CONTAINS_warmLevel">等级</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="form-control" id="filter_CONTAINS_warmLevel" name="filter_CONTAINS_warmLevel">
                                                <option value="">-请选择-</option>
                                                <option value="1">I级</option>
                                                <option value="2">II级</option>
                                                <option value="3">III级</option>
                                                <option value="4">IV级</option>
                                            </select>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="filter_CONTAINS_sendCycle">周期</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="form-control" id="filter_CONTAINS_sendCycle" name="filter_CONTAINS_sendCycle" >
                                                <option value="">-请选择-</option>
                                                <option value="w">每周</option>
                                                <option value="m">每月</option>
                                            </select>
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

                        <div id="plEmail" style="height:500px"></div>

                    </div>
                </div>
                <!-- /.row -->
                <!--/#page-content-->
            </div>
        </div>
    </div>
    <jsp:include page="../../layout/footer.jsp"></jsp:include>

    <!--/.fluid-container#main-container-->
</div>
<jsp:include page="../../layout/footer_js.jsp"></jsp:include>

<link href="<%=basePath%>/kendoUI/styles/kendo.common-material.min.css" rel="stylesheet">
<link href="<%=basePath%>/kendoUI/styles/kendo.rtl.min.css" rel="stylesheet">
<link href="<%=basePath%>/kendoUI/styles/kendo.material.min.css" rel="stylesheet">
<script src="<%=basePath%>/kendoUI/js/kendo.all.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/kendo.timezones.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/cultures/kendo.culture.zh-CN.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/messages/kendo.messages.zh-CN.min.js"></script>
<jsp:include page="../../search/search_js.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/third/pl/plEmailTemplateController.js"></script>


</body>
</html>