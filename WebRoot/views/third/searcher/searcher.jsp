<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page import="com.casesoft.dmc.model.sys.User" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    String path = request.getContextPath();
    String wsPath = "ws://" + request.getServerName() + ":" + request.getServerPort() + path ;
    User user = (User) session.getAttribute("userSession");
    String ownerId=user.getOwnerId();
    String curUserCode=user.getCode();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../../baseView.jsp"></jsp:include>
    <jsp:include page="../../search/searchBaseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
        var wsPath = "<%=wsPath%>";
        var ownerId="<%=ownerId%>";
        var curUserCode= "<%=curUserCode%>";
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
                                        <button class="btn btn-success" onclick="openSearchMainBillDialog();">
                                            <i class="ace-icon glyphicon glyphicon-list-alt"></i>
                                            <span class="bigger-110">订单</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">
                                        <label width="50px"
                                        >定时</label>
                                        <input id="timer"/>
                                    </div>
                                </div>
                            </div>
                            <div class="widget-main" id="searchPanel">
                                <form class="form-horizontal" role="form" id="searchForm">
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="form_sku">SKU</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="chosen-select  form-control" id="form_sku"
                                                    name="filter_IN_sku"/>

                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="form_styleId">款式</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <div class="input-group">
                                                <input class="form-control" id="form_styleId" type="text"
                                                       name="filter_IN_styleId"
                                                       placeholder="款号" readonly/>
                                                <span class="input-group-btn"
                                                      onclick="openstyleDialog('#form_styleId','#form_styleName',initSelect)">
                                        <button class="btn btn-sm btn-default" type="button"><i
                                                class="ace-icon fa fa-list"></i></button>
                                    </span>
                                                <input class="form-control" id="form_styleName" type="text"
                                                       placeholder="款名" readonly/>
                                            </div>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="form_colorId">颜色</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="chosen-select  form-control" id="form_colorId"
                                                    name="filter_IN_colorId">
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                      <%--  <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="filter_EQ_stockDay">日期</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <div class="input-group">
                                                <input class="form-control date-picker" id="filter_EQ_stockDay"
                                                       name="filter_GTE_scanTime" type="text"
                                                       data-date-format="yyyy-mm-dd"/>
                                                <span class="input-group-addon">
																		<i class="fa fa-calendar bigger-110"></i>
																	</span>
                                            </div>
                                        </div>--%>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="orderQty">今日已处理单据</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <div class="input-group">
                                                <span id="orderQty" class="label label-lg label-pink arrowed-right">0</span>
                                            </div>
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
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12">
                        <div id="fittingGrid" style="height:600px"></div>
                    </div>
                  <%--  <div class="col-xs-5">
                        <div id="searchGrid" style="height:600px"></div>
                    </div>--%>
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

<jsp:include page="../../base/style_dialog.jsp"></jsp:include>
<link href="<%=basePath%>/kendoUI/styles/kendo.common-material.min.css" rel="stylesheet">
<link href="<%=basePath%>/kendoUI/styles/kendo.rtl.min.css" rel="stylesheet">
<link href="<%=basePath%>/kendoUI/styles/kendo.material.min.css" rel="stylesheet">
<script src="<%=basePath%>/kendoUI/js/kendo.all.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/kendo.timezones.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/cultures/kendo.culture.zh-CN.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/messages/kendo.messages.zh-CN.min.js"></script>

<script type="text/javascript" src="<%=basePath%>/views/third/searcher/searcherController.js"></script>
<jsp:include page="../../search/search_js.jsp"></jsp:include>
<jsp:include page="dialog.jsp"></jsp:include>
 </body>
</html>