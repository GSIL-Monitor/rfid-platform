<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<html>
<!DOCTYPE html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
        var curOwnerId = "${ownerId}";
        var defaultWarehId="${defaultWarehId}";
        var userId = "${userId}";
    </script>
    <style type="text/css">
        .data-container {
            /*display: inline-block;*/
            border: 1px solid #d7d7d7;
            /*border-right: 0px;*/
            width: 20%;
            margin-bottom: 10px;
            font-size: 12px;
            text-align: center;
            padding-top: 3px;
            padding-bottom: 3px;
        }
        .title {
            color: #53606b;
            font-size: 14px;
            line-height: 31px;
        }
    </style>
</head>

<body class="no-skin">
<div class="main-container" id="main-container" style="">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
        var curOwnerId = "${ownerId}";
        var deportId="${deportId}";

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
                                        <button class="btn btn-info" onclick="checkout()">
                                            <i class="ace-icon fa fa-refresh"></i>
                                            <span class="bigger-110">导出</span>
                                        </button>
                                    </div>
                                    <%--<div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-primary" onclick="showDetailPage()">
                                            <i class="ace-icon fa fa-list"></i>
                                            <span class="bigger-110">一览表</span>
                                        </button>
                                    </div>--%>
                                    <div class="btn-group btn-group-sm pull-right">
                                        <button type="button" class="btn btn-info" onclick="showAdvSearchPanel();">
                                            <i class="ace-icon fa fa-binoculars"></i>
                                            <span class="bigger-110">高级查询</span>
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="hr hr4"></div>

                            <div class="widget-main" id="searchPanel" style="display: none">
                                <form class="form-horizontal" role="form" id="searchForm">
                                    <div class="form-group">


                                        <label class="col-xs-1 control-label" for="search_warehId">仓库</label>
                                        <div class="col-xs-2">
                                            <select class="form-control" id="search_warehId" name="filter_LIKES_warehId" style="width: 100%;">
                                            </select>
                                        </div>
                                        <label class="col-xs-1 control-label" for="search_sku">SKU</label>

                                        <div class="col-xs-2">
                                            <input class="form-control" id="search_sku" name="filter_LIKES_sku" type="text"
                                                   placeholder="模糊查询"/>
                                        </div>
                                        <label class="col-xs-1 control-label" for="search_styleId">款号</label>

                                        <div class="col-xs-2">
                                            <!-- <input class="form-control" id="filter_contains_styleName" name="filter_contains_styleName" type="text"
                                                   placeholder="模糊查询"/> -->
                                            <div class="input-group">
                                                <input class="form-control" id="search_styleId"
                                                       type="text" name="filter_EQS_styleId" readonly/>
                                                <span class="input-group-btn">
                                                         <button class="btn btn-sm btn-default" type="button" onclick="openstyleDialog('#filter_eq_styleId','#filter_eq_styleName')">
                                                             <i class="ace-icon fa fa-list"></i>
                                                         </button>
								                      </span>
                                                <input class="form-control" id="filter_EQS_styleName"
                                                       type="text" name="filter_EQS_styleName" readonly  placeholder="款名"/>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">

                                        <label class="col-xs-1 control-label" for="search_colorId">色号</label>

                                        <div class="col-xs-2">
                                            <input class="form-control" id="search_colorId" name="filter_LIKES_colorId" type="text"
                                                   placeholder="模糊查询"/>
                                        </div>
                                        <label class="col-xs-1 control-label" for="search_sizeId">尺号</label>

                                        <div class="col-xs-2">
                                            <input class="form-control" id="search_sizeId" name="filter_LIKES_sizeId" type="text"
                                                   placeholder="模糊查询"/>
                                        </div>
                                        <label class="col-xs-1 control-label" for="search_createTime">创建日期</label>
                                        <div class="col-xs-2">
                                            <div class="input-group">
                                                <input class="form-control date-picker" id="search_createTime"
                                                       type="text" name="filter_GED_billDate"
                                                       data-date-format="yyyy-mm-dd"/>
                                              <%--  <span class="input-group-addon">
                                                    <i class="fa fa-exchange"></i>
                                                </span>
                                                <input class="form-control date-picker" type="text"
                                                       class="input-sm form-control" name="filter_LED_billDate"
                                                       data-date-format="yyyy-mm-dd"/>--%>
                                            </div>
                                        </div>

                                    </div>
                                    <!-- #section:elements.form -->

                                    <div class="form-group">
                                        <div class="col-sm-offset-5 col-sm-10">
                                            <button type="button" class="btn btn-sm btn-primary" onclick="_search();">
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



                        <table id="grid"></table>
                        <div id="grid-pager"></div>
                        <!-- PAGE CONTENT ENDS -->
                    </div>
                    <form id="form1" action="" method=post name=form1 style='display:none'>
                        <%--<input id="request" type=hidden  name='request' value=''>--%>
                            <input id="filter_LIKES_warehId" type=hidden  name='filter_LIKES_warehId' value=''>
                            <input id="filter_LIKES_sku" type=hidden  name='filter_LIKES_sku' value=''>
                            <input id="filter_EQS_styleId" type=hidden  name='filter_EQS_styleId' value=''>
                            <input id="filter_LIKES_colorId" type=hidden  name='filter_LIKES_colorId' value=''>
                            <input id="filter_LIKES_sizeId" type=hidden  name='filter_LIKES_sizeId' value=''>
                            <input id="filter_GED_billDate" type=hidden  name='filter_GED_billDate' value=''>
                            <input id="filter_LED_billDate" type=hidden  name='filter_LED_billDate' value=''>
                    </form>
                    <!-- /.col -->
                </div>
                <!-- /.row -->
                <!--/#page-content-->
            </div>
        </div>
    </div>

    <jsp:include page="../layout/footer_js.jsp"></jsp:include>
    <!--/.fluid-container#main-container-->
</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="../base/style_dialog.jsp"></jsp:include>
<jsp:include page="../base/search_guest_dialog.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/factory/dateFormatUtil.js"></script>
<script type="text/javascript" src="<%=basePath%>/views/stock/dateStockDetailController.js"></script>
</body>
</html>