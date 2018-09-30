<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/7/12
  Time: 13:25
  To change this template use File | Settings | File Templates.
--%>
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
        var userId = "${userId}";
        var unit = "${unit}";
        var shopId = "${shopId}";
        var payType = "${payType}";
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
        <div class="row">
            <div class="col-xs-12">
                <div class="widget-box widget-color-blue  light-border">
                    <div class="widget-header">
                        <h5 class="widget-title">基本信息</h5>
                        <div class="widget-toolbar no-border">

                            <a class="btn btn-xs bigger btn-yellow dropdown-toggle" href="<%=basePath%>/${mainUrl}">
                                <i class="ace-icon fa fa-arrow-left"></i> 返回
                            </a>
                        </div>
                    </div>
                    <div class="widget-body">
                        <div class="widget-main padding-12">
                            <form id="searchForm" class="form-horizontal" role="form">
                                <div class="form-group">
                                    <div class="col-xs-2" hidden>
                                        <input class="form-control" id="search_shopId" name="shopId"
                                               type="text" readonly value="${unit.id}"/>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_shopName">店铺名称</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_shopName" name="shopName"
                                               style="width: 100%;" readonly value="${unit.name}"/>
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_billType">账单类型</label>
                                    <div class="col-xs-2">
                                        <select class="form-control" id="search_billType" name="filter_EQS_billType"
                                                style="width: 100%;">
                                            <option value="">--请选择--</option>
                                            <option value="0">收款</option>
                                            <option value="1">储值</option>
                                            <option value="2">付款</option>
                                        </select>
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_linkman">联系人</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_linkman" name="linkman"
                                               type="text" readonly value="${unit.linkman}"/>
                                    </div>

                                    <label class="col-xs-1 control-label" for="search_tel">电话</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_tel" name="tel"
                                               type="text" readonly value="${unit.tel}"/>
                                    </div>

                                </div>

                                <div class="form-group">
                                    <div class="col-sm-offset-5 col-sm-10">
                                        <button type="button" class="btn btn-sm btn-primary" onclick="_search()">
                                            <i class="ace-icon fa fa-search"></i>
                                            <span class="bigger-110">查询</span>
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="hr hr4"></div>


    <div class="widget-box transparent">
        <div class="widget-header ">
            <h4 class="widget-title lighter">明细</h4>
            <div class="widget-toolbar no-border">
                <ul class="nav nav-tabs" id="myTab">
                    <li class="active">
                        <a data-toggle="tab" href="#detail">支付明细</a>
                    </li>
                </ul>
            </div>
        </div>
        <div class="widget-body">
            <div class="widget-main padding-12 no-padding-left no-padding-right">
                <div class="tab-content padding-4">
                    <div id="addDetail" class="tab-pane in active" style="height:80%;">
                        <table id="grid"></table>
                        <div id="grid-pager"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>

<script type="text/javascript" src="<%=basePath%>/views/factory/dateFormatUtil.js"></script>
<script type="text/javascript" src="<%=basePath%>/views/search/shopTurnoverSearchDetailController.js"></script>

<script type="text/javascript">

</script>
</body>
</html>
