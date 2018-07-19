<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017-06-20
  Time: 下午 3:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName()+":"+request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script>
        var basePath = "<%=basePath%>";
        var ownerId = "${ownerId}";
        var unitOwnerId ="${unitOwnerId}";
        var userId="${userId}";
    </script>
</head>
<body class="no-skin">
<div class="main-container" id="main-container">
    <div class="row">
        <div class="col-xs-12">
            <div class ="widget-body">
                <div class="widget-toolbox padding-8 clearfix">
                    <div class="btn-toolbar" role="toolbar">
                        <div class="btn-group btn-group-sm pull-left" onclick="refresh()">
                            <button class="btn btn-info">
                                <i class="cae-icon fa fa-refresh"></i>
                                <span class="bigger-10">刷新</span>
                            </button>
                        </div>
                        <div class="btn-group btn-group-sm pull-left" onclick="addGuest();">
                            <button class="btn btn-primary">
                                <i class="ace-icon fa fa-plus"></i>
                                <span class="bigger-10">新增客户</span>
                            </button>
                        </div>
                        <%--<div class="btn-group btn-group-sm pull-left" onclick="editGuest()">--%>
                            <%--<button class="btn btn-primary">--%>
                                <%--<i class="ace-icon fa fa-edit"></i>--%>
                                <%--<span class="bigger-10">编辑客户</span>--%>
                            <%--</button>--%>
                        <%--</div>--%>
                        <div class="btn-group btn-group-sm pull-right" onclick="showSearchPannel()">
                            <button class="btn btn-info">
                                <i class="ace-icon fa fa-binoculars"></i>
                                <span class="bigger-10">高级查询</span>
                            </button>
                        </div>
                    </div>
                </div>
                <div class="hr hr-2 hr-dotted"></div>
                <div class="widget-main" id="search-pannel" hidden>
                    <form role="form" class="form-horizontal" id="searchForm">
                        <div class="form-group">
                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="search_id">客户编号</label>
                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                <input class="form-control" id="search_id" name="filter_LIKES_id" onkeyup="this.value=this.value.toUpperCase()" placeholder="模糊查询">
                            </div>

                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="search_name">客户名称</label>
                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                <input class="form-control" id="search_name" name="filter_LIKES_name" placeholder="模糊查询">
                            </div>

                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="search_unitType">客户类型</label>
                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                <select id="search_unitType" class="chosen-select form-control" name="filter_EQS_unitType">
                                    <option value="CT-AT">省代客户</option>
                                    <option value="CT-ST">门店客户</option>
                                    <option value="CT-LS">零售客户</option>

                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="search_linkman">联系人</label>
                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                <input class="form-control" id="search_linkman" name="filter_LIKES_linkman" placeholder="模糊查询">
                            </div>

                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="search_ownerId">所属方</label>
                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <div class="input-group">
                                        <input class="form-control" id="search_ownerId"
                                               type="text" name="filter_EQS_ownerId" readonly/>
                                        <span class="input-group-btn">
																			<button class="btn btn-sm btn-default" id="setOwnerId"
                                                                                    type="button"
                                                                                    onclick="openUnitDialog('#search_ownerId','#form_unitName',null,'withShop')">
																				<i class="ace-icon fa fa-list"></i>
																			</button>

																		</span>
                                        <input class="form-control" id="form_unitName"
                                               type="text" name="unitName" readonly/>
                                    </div>
                            </div>

                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="search_status">客户状态</label>
                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                <select id="search_status" class="chosen-select form-control" name="filter_EQI_status">
                                    <option value="">--请选择--</option>
                                    <option value="1">已启用</option>
                                    <option value="0">已废除</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="search_rank">客户等级</label>
                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                <select id="search_rank" class="chosen-select form-control selectpicker show-tick" data-live-search="true" name="filter_EQS_idCard">
                                    <option value="">--请选择--</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-11 btnPosition">
                                <button type="button" class="btn btn-sm btn-primary" onclick="_search()">
                                    <i class="ace-icon fa fa-search"></i>
                                    <span class="bigger-110">查询</span>
                                </button>
                                <button type="reset" class="btn btn-sm btn-warning" onclick="empty()">
                                    <i class="ace-icon fa fa-undo"></i>
                                    <span class="bigger-110">清空</span></button>
                            </div>
                        </div>
                    </form>
                </div>

            </div>
            <table id="grid"></table>
            <table id="gridPager"></table>
        </div>
    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>
</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="../base/unit_dialog.jsp"></jsp:include>
<jsp:include page="../sys/setRank_edit.jsp"></jsp:include>
<script src="<%=basePath%>/views/sys/guestController.js"></script>
</body>
</html>
