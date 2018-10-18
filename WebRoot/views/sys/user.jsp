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
        var pageType="pageType";
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
                                        <button type="button" class="btn btn-info" onclick="showAdvSearchPanel()">
                                            <i class="ace-icon fa fa-binoculars"></i>
                                            <span class="bigger-110">高级查询</span>
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="hr hr-2 hr-dotted"></div>
                            <div class="widget-main" id="searchPanel" style="display:none">
                                <form class="form-horizontal" role="form" id="searchForm">
                                    <!--Amber修改后-->
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label fontPosition text-right" for="search_name">姓名</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="search_name" name="filter_LIKES_name" type="text" placeholder="模糊查询"/>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label fontPosition text-right" for="search_code">登录名</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="search_code" name="filter_LIKES_code" type="text" placeholder="模糊查询"/>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label fontPosition text-right" for="search_createDate">创建日期 </label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <div class="input-group">
                                                <input class="form-control date-picker" id="search_createDate" type="text" name="filter_GTD_createDate" data-date-format="yyyy-mm-dd"/>
                                                <span class="input-group-addon"><i class="fa fa-exchange"></i></span>
                                                <input class="form-control date-picker" type="text" class="input-sm form-control" name="filter_LED_createDate" data-date-format="yyyy-mm-dd"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label fontPosition text-right" for="search_phone">电话号码</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="search_phone" name="filter_LIKES_phone" type="text" placeholder="模糊查询"/>
                                        </div>
                                    </div>
                                    <!--高级查询按钮-->
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


</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="user_edit.jsp"></jsp:include>
<jsp:include page="../base/unit_dialog.jsp"></jsp:include>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/sys/userController.js"></script>
<script type="text/javascript">

</script>
</body>
</html>