<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/6/25
  Time: 上午 10:36
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
            <div class="row">
                <div class="center">
                    <div class="col-md-4 order-panel-left">
                        <div class="panel panel-default  left-panel">
                            <div class="panel-body">
                                <div class="widget-body">
                                    <form class="form-horizontal" role="form" id="searchForm">
                                        <div class="form-group">
                                            <label class="col-md-2 control-label" for="search_billId">单号</label>
                                            <div class="col-md-10">
                                                <input class="form-control" id="search_billId"
                                                       name="filter_LIKES_billNo"
                                                       type="text" onkeyup="this.value=this.value.toUpperCase()"
                                                       placeholder="模糊查询"/>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-2 control-label"
                                                   for="search_createTime">创建日期</label>
                                            <div class="col-md-10">
                                                <div class="input-group">
                                                    <input class="form-control date-picker"
                                                           id="search_createTime"
                                                           type="text" name="filter_GED_billDate"
                                                           data-date-format="yyyy-mm-dd"/>
                                                    <span class="input-group-addon">
                                                    <i class="fa fa-exchange"></i>
                                                </span>
                                                    <input class="form-control date-picker" type="text"
                                                           class="input-sm form-control"
                                                           name="filter_LED_billDate"
                                                           data-date-format="yyyy-mm-dd"/>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-md-2 control-label"
                                                   for="search_destUnitId">供应商</label>
                                            <div class="col-md-10">
                                                <div class="input-group">
                                                    <input class="form-control" id="search_destUnitId"
                                                           type="text"
                                                           name="filter_EQS_destUnitId" readonly/>
                                                    <span class="input-group-btn">
                                                            <button class="btn btn-sm btn-default"
                                                                    id="search_guest_button"
                                                                    type="button"
                                                                    onclick="openSearchVendorDialog('search')">
                                                                <i class="ace-icon fa fa-list"></i>
                                                            </button>
											            </span>
                                                    <input class="form-control" id="search_destUnitName"
                                                           type="text"
                                                           name="search_destUnitName" readonly/>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-md-2 control-label"
                                                   for="search_origId">出库仓库</label>
                                            <div class="col-md-4">
                                                <select class="form-control selectpicker show-tick"
                                                        id="search_origId"
                                                        name="filter_LIKES_origId"
                                                        style="width: 100%;" data-live-search="true">
                                                </select>
                                            </div>
                                            <label class="col-md-2 control-label"
                                                   for="select_outStatus">出库状态</label>
                                            <div class="col-md-4">
                                                <select class="form-control selectpicker show-tick"
                                                        id="select_outStatus"
                                                        name="filter_INI_outStatus" data-live-search="true">
                                                    <option value="">--请选择--</option>
                                                    <option value="0,3">订单状态</option>
                                                    <option value="2">已出库</option>
                                                    <option value="3">出库中</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-md-offset-3 col-md-6">
                                                <button type="button" class="btn btn-sm btn-primary"
                                                        onclick="_search()">
                                                    <i class="ace-icon fa fa-search"></i>
                                                    <span class="bigger-110">查询</span>
                                                </button>
                                                <button type="reset" class="btn btn-sm btn-warning" ,
                                                        onclick="_resetForm()">
                                                    <i class="ace-icon fa fa-undo"></i>
                                                    <span class="bigger-110">清空</span></button>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                                <div class="col-md-12">
                                    <table id="grid"></table>
                                    <div id="grid-pager"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
