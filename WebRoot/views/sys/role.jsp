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
        var rowId = "rowId";
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
                        <div class="widget-body">
                            <div class="widget-toolbox padding-8 clearfix">
                                <div class="btn-toolbar" role="toolbar">
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-info" onclick="refresh()">
                                            <i class="ace-icon fa fa-refresh"></i>
                                            <span class="bigger-110">刷新</span>
                                        </button>
                                        <button type="button" class="btn btn-info" onclick="showAdvSearchPanel();">
                                            <i class="ace-icon fa fa-binoculars"></i>
                                            <span class="bigger-110">高级查询</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button type="button" class="btn btn-primary" onclick="add()">
                                            <i class="ace-icon fa fa-plus"></i>
                                            <span class="bigger-110">增加角色</span>
                                        </button>
                                        <button type="button" class="btn btn-primary" onclick="edit()">
                                            <i class="ace-icon fa fa-edit"></i>
                                            <span class="bigger-110">编辑角色</span>
                                        </button>
                                    </div>

                                    <div class="btn-group btn-group-sm pull-right">
                                        <button type="button" class="btn btn-primary" onclick="addPower()">
                                            <i class="ace-icon fa fa-plus"></i>
                                            <span class="bigger-110">增加权限</span>
                                        </button>
                                        <button type="button" class="btn btn-primary" onclick="editPower()">
                                            <i class="ace-icon fa fa-edit"></i>
                                            <span class="bigger-110">编辑权限</span>
                                        </button>
                                        <button type="button" class="btn btn-primary" onclick="addButton()">
                                            <i class="ace-icon fa fa-edit"></i>
                                            <span class="bigger-110">增添按钮</span>
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="hr hr-2 hr-dotted"></div>

                            <div class="widget-main" id="searchPanel" style="display:none">
                                    <form class="form-horizontal" role="form" id="searchForm" >
                                        <div class="form-group">
                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="search_code">角色编号</label>

                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                <input class="form-control" id="search_code" name="filter_LIKES_code"
                                                       type="text"
                                                       placeholder="模糊查询"/>
                                            </div>
                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="search_name">名称</label>

                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                <input class="form-control" id="search_name" name="filter_LIKES_name"
                                                       type="text" placeholder="模糊查询"/>
                                            </div>
                                        </div>
                                <div class="form-group">
                                    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-11 btnPosition">
                                        <button type="button" class="btn btn-sm btn-primary" onclick="_search()">
                                            <i class="ace-icon fa fa-search"></i>
                                            <span class="bigger-110">查询</span>
                                        </button>
                                        <button type="reset" class="btn btn-sm btn-warning">
                                            <i class="ace-icon fa fa-undo"></i>
                                            <span class="bigger-110">清空</span></button>
                                    </div>
                                </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12 col-sm-6">
                        <div class="widget-box widget-color-blue  light-border">
                            <div class="widget-header" id="roleHeader">
                                <h5 class="widget-title">角色列表</h5>

                                <div class="widget-toolbar">
                                    <a href="#" data-action="reload" onclick="refreshRole()">
                                        <i class="ace-icon fa fa-refresh"></i>
                                    </a>
                                    <%--<a href="#" data-action="fullscreen" class="orange2" onclick="fullRoleScreen()">--%>
                                        <%--<i class="ace-icon fa fa-expand"></i>--%>
                                    <%--</a>--%>
                                    <a href="#" data-action="collapse">
                                        <i class="ace-icon fa fa-chevron-up"></i>
                                    </a>
                                </div>
                            </div>
                            <div class="widget-body">
                                <div class="widget-main no-padding">
                                    <table id="roleGrid"></table>
                                    <div id="grid-pager"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-6">
                        <div class="widget-box widget-color-blue  light-border">
                            <div class="widget-header" id="resourceHeader">
                                <h5 class="widget-title">权限列表</h5>

                                <div class="widget-toolbar">
                                    <%--<a href="#" data-action="reload" >--%>
                                        <%--<i class="ace-icon fa fa-refresh"></i>--%>
                                    <%--</a>--%>
                                    <%--<a href="#" data-action="fullscreen" class="orange2" onclick="fullResourceScreen()">--%>
                                        <%--<i class="ace-icon fa fa-expand"></i>--%>
                                    <%--</a>--%>
                                    <a href="#" data-action="collapse">
                                        <i class="ace-icon fa fa-chevron-up"></i>
                                    </a>
                                </div>
                            </div>
                            <div class="widget-body">
                                <div class="widget-main no-padding">
                                    <table id="resourceGrid"></table>
                                    <div id="resourceGrid-pager"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>
    <jsp:include page="roleButton_add.jsp"></jsp:include>
    <jsp:include page="rolePower_edit.jsp"></jsp:include>

</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/sys/roleController.js"></script>
</body>
</html>