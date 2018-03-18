<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017-06-21
  Time: 下午 2:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.*" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    //协议+“：//”+服务器ip或域名+':'+端口+上下文
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script>
        var basePath = "<%=basePath%>";
        var pageType = "${pageType}";
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

                            <div class="widget-box widget-color-blue  light-border">
                                <div class="widget-header">
                                    <h5 class="widget-title">付款商对账信息</h5>
                                    <div class="widget-toolbar no-border">
                                        <a class="btn btn-xs bigger btn-yellow dropdown-toggle" href="<%=basePath%>${callback}"> <i class="ace-icon fa fa-arrow-left"></i>返回</a>
                                    </div>
                                </div>
                            </div>
                            <div class="hr hr4"></div>

                            <div class="widget-main" id="searchPanel">
                                <form class="form-horizontal" role="form" id="searchForm">

                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="search_id">供应商名称</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="search_id"  value="${unit.id}">
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="search_id">联系人</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="search_name"  value="${unit.name}">
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="search_id">欠款</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="search_owingValue"  value="${unit.owingValue}">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               for="search_createTime">查询期间 </label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <div class="input-group">
                                                <input class="form-control date-picker startDate"
                                                       id="search_createTime"
                                                       type="text" name="filter_GED_billDate"
                                                       data-date-format="yyyy-mm-dd"/>

                                                <span class="input-group-addon">
                                                    <i class="fa fa-exchange"></i>
                                                </span>

                                                <input class="form-control date-picker endDate" type="text"
                                                       class="input-sm form-control"
                                                       name="filter_LED_billDate"
                                                       data-date-format="yyyy-mm-dd"/>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <div class="col-sm-offset-5 col-sm-10">
                                            <button type="button" class="btn btn-sm btn-primary" onclick="_search()">
                                                <i class="ace-icon fa fa-search"></i>
                                                <span class="bigger-110">查询</span>
                                            </button>
                                            <%--<button type="reset" class="btn btn-sm btn-warning">
                                                <i class="ace-icon fa fa-undo"></i>
                                                <span class="bigger-110">清空</span></button>--%>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <div class="widget-body">
                            <div class="widget-main padding-12 no-padding-left no-padding-right">
                                <div class="tab-content padding-4">
                                    <div id="addDetail" class="tab-pane in active" style="height:80%;">
                                        <table id="minxigrid"></table>
                                        <div id="minxiPager"></div>
                                    </div>
                                </div>
                                <div id="buttonGroup" class="col-sm-offset-5 col-sm-10 ">
                                    <button type="button" class="btn btn-sm btn-primary" onclick="add()">
                                        <i class="ace-icon fa fa-jpy"></i>
                                        <span class="bigger-110">付款</span>
                                    </button>
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
</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="../base/unit_dialog.jsp"></jsp:include>
<jsp:include page="unit_edits.jsp"></jsp:include>
<script>

</script>
<script type="text/javascript" src="<%=basePath%>/views/factory/dateFormatUtil.js"></script>
<script src="<%=basePath%>/views/sys/unit_edit_controller.js"></script>

</body>
</html>
