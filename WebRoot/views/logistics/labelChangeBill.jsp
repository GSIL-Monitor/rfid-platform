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
                                        <button type="button" class="btn btn-primary" onclick="add('CS')">
                                            <i class="ace-icon fa fa-plus"></i>
                                            <span class="bigger-110">转换系列</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button type="button" class="btn btn-primary" onclick="add('PC')">
                                            <i class="ace-icon fa fa-plus"></i>
                                            <span class="bigger-110">转换标签价格</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button type="button" class="btn btn-primary" onclick="add('TG')">
                                            <i class="ace-icon fa fa-plus"></i>
                                            <span class="bigger-110">转换标签商场特供</span>
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
                            <div class="widget-main" id="searchPanel" style="display:none">
                                <form class="form-horizontal" role="form" id="searchForm">
                                    <div class="form-group">
                                        <label class="col-xs-1 control-label" for="search_billId">单号</label>
                                        <div class="col-xs-2">
                                            <input class="form-control" id="search_billId" name="filter_LIKES_t.billNo"
                                                   type="text" onkeyup="this.value=this.value.toUpperCase()"
                                                   placeholder="模糊查询"/>
                                        </div>
                                        <label class="col-xs-1 control-label" for="search_createTime">创建日期</label>
                                        <div class="col-xs-2">
                                            <div class="input-group">
                                                <input class="form-control date-picker" id="search_createTime"
                                                       type="text" name="filter_GED_t.billDate"
                                                       data-date-format="yyyy-mm-dd"/>
                                                <span class="input-group-addon">
                                                    <i class="fa fa-exchange"></i>
                                                </span>
                                                <input class="form-control date-picker" type="text"
                                                       class="input-sm form-control" name="filter_LED_t.billDate"
                                                       data-date-format="yyyy-mm-dd"/>
                                            </div>
                                        </div>
                                        <label class="col-xs-1 control-label" for="select_changeType">转变类型</label>
                                        <div class="col-xs-2">
                                            <select class="form-control" id="select_changeType"
                                                    name="filter_EQS_t.changeType">
                                                <option value="">--请选择--</option>
                                                <option value="CS">系列转变</option>
                                                <option value="PC">打折</option>
                                                <option value="TG">商场特供</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">

                                        <label class="col-xs-1 control-label" for="search_origId">仓库</label>
                                        <div class="col-xs-2">
                                            <select class="form-control selectpicker show-tick" id="search_origId"
                                                    name="filter_EQS_t.origId" style="width: 100%;" data-live-search="true">
                                            </select>
                                        </div>
                                        <%--<input id="search_unitId" type="text" value="${OwnerId}" name="filter_EQS_unitId" style="display: none">--%>
                                        <label class="col-xs-1 control-label" for="search_nowclass9">现系列</label>
                                        <div class="col-xs-2">
                                            <select class="form-control" id="search_nowclass9" name="filter_EQS_t.nowclass9"
                                                    style="width: 100%;">
                                            </select>
                                        </div>
                                        <label class="col-xs-1 control-label" for="search_beforeclass9">原系列</label>
                                        <div class="col-xs-2">
                                            <select class="form-control" id="search_beforeclass9" name="filter_EQS_t.beforeclass9"
                                                    style="width: 100%;">
                                            </select>
                                        </div>

                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-1 control-label" for="search_styleid">款号</label>
                                        <div class="col-xs-2">
                                            <div class="input-group">
                                                <input class="form-control" id="search_styleid"
                                                       type="text" name="filter_EQS_dtl.styleId" readonly/>
                                                <span class="input-group-btn">
                                                                 <button class="btn btn-sm btn-default" type="button" onclick="openstyleDialog('#search_styleid','#filter_eq_stylename')">
                                                                     <i class="ace-icon fa fa-list"></i>
                                                                 </button>
                                                              </span>
                                                <input class="form-control" id="filter_eq_stylename"
                                                       type="text" name="" readonly  placeholder="款名"/>
                                            </div>
                                        </div>

                                    </div>

                                    <div class="form-group">
                                        <div class="col-sm-offset-5 col-sm-10">
                                            <button type="button" class="btn btn-sm btn-primary" onclick="_search()">
                                                <i class="ace-icon fa fa-search"></i>
                                                <span class="bigger-110">查询</span>
                                            </button>
                                            <button type="reset" class="btn btn-sm btn-warning" onclick="_reset()">
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
                    <!-- /.col -->
                </div>
                <!-- /.row -->
                <!--/#page-content-->
            </div>
        </div>
    </div>



    <!--/.fluid-container#main-container-->
</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="purchaseOrderBillPrint.jsp"></jsp:include>
<jsp:include page="../sys/print_two.jsp"></jsp:include>
<jsp:include page="../base/style_dialog.jsp"></jsp:include>

<script type="text/javascript" src="<%=basePath%>/views/logistics/labelChangeBillController.js"></script>

<div id="dialog"></div>
<div id="progressDialog"></div>
<span id="notification"></span>
</body>
</html>