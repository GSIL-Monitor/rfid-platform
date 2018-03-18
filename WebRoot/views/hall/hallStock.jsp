<%@ page language="java" import="java.util.*,com.casesoft.dmc.model.sys.User" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    User user = (User) session.getAttribute("userSession");
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../search/searchBaseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
        var userCode ="<%=user.getCode()%>";
        var userManage="<%=user.getOwnerId()%>"
    </script>
    <link rel="stylesheet" href="<%=basePath%>/Olive/plugin/img-show/css/base.css" />
    <link rel="stylesheet" href="<%=basePath%>/Olive/plugin/img-show/css/styles.css" />
    <link rel="stylesheet" href="<%=basePath%>/Olive/plugin/img-show/touchTouch/touchTouch.css"/>
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

                                    <div class="btn-group btn-group-sm pull-left">

                                        <button class="btn btn-info" onclick="badDebt()">

                                            <i class="ace-icon fa fa-warning"></i>
                                            <span class="bigger-110">报损补标</span>

                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">

                                        <button class="btn btn-info" onclick="confirm()">

                                            <i class="ace-icon fa fa-check"></i>
                                            <span class="bigger-110">确认报损</span>

                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">

                                        <button class="btn btn-info" onclick="showphotoes()">

                                            <i class="ace-icon fa fa-area-chart"></i>
                                            <span class="bigger-110">样衣图片</span>

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
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right">入库时间</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <div class="input-group">
                                                <input class="form-control date-picker"
                                                       id="search_inDate" type="text"
                                                       name="filter_gte_inDate" data-date-format="yyyy-mm-dd"/>

                                                <span class="input-group-addon"> <i
                                                        class="fa fa-exchange"></i>
													</span> <input class="form-control date-picker" type="text"
                                                                   class="input-sm form-control"
                                                                   name="filter_lte_inDate"
                                                                   data-date-format="yyyy-mm-dd"/>
                                            </div>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right">吊牌码</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="filter_contains_code" name="filter_contains_code"
                                                   type="text"
                                                   placeholder="模糊查询"/>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right">样衣间</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="form-control chosen-select" name="filter_eq_ownerId"
                                                    id="filter_eq_ownerId">
                                                <option value="">-请选择-</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right">状态</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="form-control chosen-select" id="filter_eq_status" name="filter_eq_status">
                                                <option value="">-请选择-</option>
                                                <option value="0">未入库</option>
                                                <option value="1">在库</option>
                                                <option value="2">外借</option>
                                                <option value="3">在途</option>
                                                <option value="4">报损出库</option>
                                                <option value="5">丢失</option>
                                                <option value="6">已报损</option>
                                                <option value="7">报损补标</option>
                                            </select>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right">款号</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="filter_contains_styleId" name="filter_contains_styleId"
                                                   type="text"
                                                   placeholder="模糊查询"/>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right">库位</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="form-control chosen-select" name="filter_eq_floor"
                                                    id="filter_eq_floor">
                                                <option value="">-请选择-</option>
                                            </select>
                                        </div>
                                    </div>

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

                        <div id="stockGrid" style="height:700px"></div>


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

<jsp:include page="../search/search_js.jsp"></jsp:include>
<script src="<%=basePath%>/Olive/plugin/img-show/touchTouch/touchTouch.jquery.js"></script>
<jsp:include page="show_photoes_in_hallStock.jsp"></jsp:include>

<!--/.fluid-container#main-container-->
</div>
<script type="text/javascript" src="<%=basePath%>/views/hall/hallStockController.js"></script>
<span id="notification"></span>
</body>
</html>