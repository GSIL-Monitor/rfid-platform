<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.casesoft.dmc.model.sys.User" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
        var curOwnerId = "${ownerId}";
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
                                            <i class="ace-icon fa fa-refresh"></i> <span
                                                class="bigger-110">刷新</span>
                                        </button>
                                    </div>
                                 <%--   <div class="btn-group btn-group-sm pull-left">
                                        <button type="button" class="btn btn-primary" onclick="add()">
                                            <i class="ace-icon fa fa-plus"></i>
                                            <span class="bigger-110">增加</span>
                                        </button>
                                        <button type="button" class="btn btn-primary" onclick="edit()">
                                            <i class="ace-icon fa fa-edit"></i>
                                            <span class="bigger-110">编辑</span>
                                        </button>
                                    </div>--%>
                                    <div class="btn-group btn-group-sm pull-right">

                                        <button type="button" class="btn btn-info"
                                                onclick="showAdvSearchPanel();">
                                            <i class="ace-icon fa fa-binoculars"></i> <span
                                                class="bigger-110">高级查询</span>
                                        </button>

                                    </div>
                                </div>
                            </div>

                            <div class="hr hr-2 hr-dotted"></div>

                            <div class="widget-main" id="searchPanel" style="display:none">
                                <form class="form-horizontal" role="form" id="searchForm">
                                    <div class="form-group">
                            <%--            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="search_code">编号</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="search_code" name="filter_LIKES_code" type="text" onkeyup="this.value=this.value.toUpperCase()"
                                                   placeholder="模糊查询"/>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="search_name">名称</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="search_name" name="filter_LIKES_name" type="text" placeholder="模糊查询"/>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="search_groupId">分类</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="chosen-select form-control" id="search_groupId" name="filter_EQS_groupId">
                                                <option value="">--请选择--</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="search_src">来源</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="chosen-select form-control" id="search_src" name="filter_EQS_src">
                                                <option value="">--请选择--</option>
                                                <option value="01">系统</option>
                                                <option value="02">同步</option>
                                                <option value="03">导入</option>
                                            </select>
                                        </div>--%>


                                        <!-- #section:elements.form -->

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="search_timedate">创建日期 </label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <div class="input-group">
                                                <input class="form-control date-picker startDate" id="search_timedate"
                                                       type="text" name="filter_GED_timedate"
                                                       data-date-format="yyyy-mm-dd"/>

                                                <span class="input-group-addon">
																		<i class="fa fa-exchange"></i>
																	</span>

                                                <input class="form-control date-picker endDate" type="text"
                                                       class="input-sm form-control" name="filter_LED_timedate"
                                                       data-date-format="yyyy-mm-dd"/>
                                            </div>
                                        </div>
                                        <label class="col-xs-1 control-label" for="search_warehId">仓库</label>
                                        <div class="col-xs-2">
                                            <select class="form-control" id="search_warehId" name="filter_LIKES_warehId" style="width: 100%;">
                                            </select>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="search_sku">sku</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="search_sku" name="filter_LIKES_sku" type="text"
                                                   placeholder="模糊查询" onkeyup="this.value=this.value.toUpperCase()"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-11 btnPosition">
                                            <button type="button" class="btn btn-sm btn-primary" onclick="_search()">
                                                <i class="ace-icon fa fa-search"></i>
                                                <span class="bigger-110">查询</span>
                                            </button>
                                            <button type="reset" class="btn btn-sm btn-warning" onclick="_clearSearch()">
                                                <i class="ace-icon fa fa-undo"></i>
                                                <span class="bigger-110">清空</span></button>
                                        </div>
                                    </div>

                                </form>

                            </div>
                        </div>

                        <table id="grid" style="background:#ffffff"></table>

                        <div id="grid-pager"></div>


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
<jsp:include page="../base/unit_dialog.jsp"></jsp:include>
<script type="text/javascript"
        src="<%=basePath%>/views/logistics/monthStatisticsController.js"></script>
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/dateFormatUtil.js"></script>
<script type="text/javascript">


</script>
</body>
</html>