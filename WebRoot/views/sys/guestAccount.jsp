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
                                        <button class="btn btn-primary" onclick="showDetailPage()">
                                            <i class="ace-icon fa fa-list"></i>
                                            <span class="bigger-110">一览表</span>
                                        </button>
                                    </div>
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
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               for="search_nameOrTel">客户名称或电话</label>
                                        <div class="col-xs-8 col-sm-8 col-md-2 col-lg-2">
                                            <input class="form-control" id="search_nameOrTel" name="filter_LIKES_name_OR_tel"
                                                   type="text" placeholder="模糊查询"/>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                               for="search_unitType">客户类型</label>
                                        <div class="col-xs-8 col-sm-8 col-md-2 col-lg-2">
                                            <select class="form-control" name="filter_EQS_unitType" id="search_unitType">
                                                <option value="">--选择客户类型--</option>
                                                <option value="CT-AT">省代客户</option>
                                                <option value="CT-ST">门店客户</option>
                                                <option value="CT-LS">零售客户</option>
                                            </select>
                                        </div>

                                        <label class="col-xs-1 control-label" for="search_owingValue">欠款金额</label>
                                        <div class="col-xs-2">
                                            <div class="input-group">
                                                <input class="form-control" id="search_owingValue"
                                                       type="text" name="filter_GEN_owingValue"/>
                                                <span class="input-group-addon">
                                                    <i class="fa fa-exchange"></i>
                                                </span>
                                                <input  type="text" class="form-control" name="filter_LEN_owingValue"/>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <div class="col-sm-offset-5 col-sm-10">
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

                        <div id="isAdmin">
                            <div class="data-container col-lg-2">
                                <span class="title">客户数：</span>
                                <span class="title" id ="sumOfCustomer">查询中...</span>
                            </div>
                            <div class="data-container col-lg-2">
                                <span class="title">欠款客户数：</span>
                                <span class="title" id ="sumOfOwningCustomer">查询中...</span>
                            </div>
                            <div class="data-container col-lg-2">
                                <span class="title">欠款金额：</span>
                                <span class="title" id="sumOfOwningValue">查询中...</span>
                            </div>
                            <div class="data-container col-lg-2">
                                <span class="title">充值客户数：</span>
                                <span class="title" id="sumOfRechargeCustomer">查询中...</span>
                            </div>
                            <div class="data-container col-lg-2">
                                <span class="title">充值金额：</span>
                                <span class="title" id="sumOfRechargeValue">查询中...</span>
                            </div>
                        </div>

                        <table id="grid"></table>
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

    <jsp:include page="../layout/footer_js.jsp"></jsp:include>
    <!--/.fluid-container#main-container-->
</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/sys/guestAccountController.js"></script>
</body>
</html>