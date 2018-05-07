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
        var curOwnerId = "${ownerId}";
        var ownersId="${ownersId}";
        var userId = "${userId}";
        var billNo = "${billNo}";
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
                                        <button class="btn btn-info" onclick="exportMessage()">
                                            <i class="ace-icon fa fa-refresh"></i>
                                            <span class="bigger-110">导出</span>
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
                                            <input class="form-control" id="search_billId" name="filter_LIKES_billNo"
                                                   type="text" onkeyup="this.value=this.value.toUpperCase()"
                                                   placeholder="模糊查询"/>
                                        </div>
                                        <label class="col-xs-1 control-label" for="search_createTime">创建日期</label>
                                        <div class="col-xs-2">
                                            <div class="input-group">
                                                <input class="form-control date-picker" id="search_createTime"
                                                       type="text" name="filter_GED_billDate"
                                                       data-date-format="yyyy-mm-dd"/>
                                                <span class="input-group-addon">
                                                    <i class="fa fa-exchange"></i>
                                                </span>
                                                <input class="form-control date-picker" type="text"
                                                       class="input-sm form-control" name="filter_LED_billDate"
                                                       data-date-format="yyyy-mm-dd"/>
                                            </div>
                                        </div>
                                        <label class="col-xs-1 control-label" for="search_styleid">款号</label>
                                        <div class="col-xs-2">
                                            <div class="input-group">
                                                <input class="form-control" id="search_styleid"
                                                       type="text" name="filter_EQS_styleid" readonly/>
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
                                        <label class="col-xs-1 control-label" for="search_origUnitId">供应商</label>
                                        <div class="col-xs-2">
                                            <div class="input-group">
                                                <input class="form-control" id="search_origUnitId" type="text"
                                                       name="filter_EQS_origUnitId" readonly/>
                                                <span class="input-group-btn">
                                                    <button class="btn btn-sm btn-default" id="search_vendor_button"
                                                            type="button" onclick="openSearchVendorDialog()">
                                                        <i class="ace-icon fa fa-list"></i>
                                                    </button>
											    </span>
                                                <input class="form-control" id="search_origUnitName" type="text"
                                                       name="" readonly/>
                                            </div>
                                        </div>
                                        <label class="col-xs-1 control-label" for="search_buyahandid">买手</label>
                                        <div class="col-xs-2">
                                            <select class="form-control selectpicker show-tick" id="search_buyahandid"
                                                    name="filter_EQS_buyahandid"
                                                    style="width: 100%;" data-live-search="true">
                                            </select>
                                        </div>
                                        <label class="col-xs-1 control-label" for="search_class1">厂家</label>
                                        <div class="col-xs-2">
                                            <div class="input-group">
                                                <input class="form-control" id="search_class1"
                                                       type="text" name="filter_EQS_styleid" readonly/>
                                                <span class="input-group-btn">
                                                                 <button class="btn btn-sm btn-default" type="button" onclick="initClass1Select_Grid('#search_class1','#filter_eq_class1name')">
                                                                     <i class="ace-icon fa fa-list"></i>
                                                                 </button>
                                                              </span>
                                                <input class="form-control" id="filter_eq_class1name"
                                                       type="text" name="" readonly  placeholder="厂家名"/>
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
                            <form  id="form1" action="" method=post name=form1 style='display:none'>
                                <input id="pages" type=hidden  name='pages' value=''>

                            </form>
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
    <jsp:include page="../layout/footer.jsp"></jsp:include>
    <!--/.fluid-container#main-container-->
</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="../sys/print_two.jsp"></jsp:include>
<jsp:include page="../base/style_dialog.jsp"></jsp:include>
<jsp:include page="../base/search_class1_dialog.jsp"></jsp:include>
<jsp:include page="../base/search_vendor_dialog.jsp"></jsp:include>
<jsp:include page="../base/search_guest_dialog.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/search/findSkuInformationSearchController.js"></script>



<%--<div id="progressDialog"></div>
<span id="notification"></span>--%>
</body>
</html>