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
                                        <button type="button" class="btn btn-primary" onclick="uploadStyle()">
                                            <i class="ace-icon fa fa-file-excel-o"></i>
                                            <span class="bigger-110">导入文件（条码）</span>
                                        </button>
                                        <button type="button" class="btn btn-primary" onclick="uploadCode()">
                                            <i class="ace-icon fa fa-file-excel-o"></i>
                                            <span class="bigger-110">导入文件（唯一码）</span>
                                        </button>
                                        <button type="button" class="btn btn-primary" onclick="exportFile()">
                                            <i class="ace-icon fa fa-file-excel-o"></i>
                                            <span class="bigger-110">导出</span>
                                        </button>
                                        <button type="button" class="btn btn-primary" onclick="confirm()">
                                            <i class="ace-icon fa fa-check-square-o"></i>
                                            <span class="bigger-110">确认单据</span>
                                        </button>
                                        <button type="button" class="btn btn-primary" onclick="deleteFirm()">
                                            <i class="ace-icon fa fa-trash-o"></i>
                                            <span class="bigger-110">删除单据</span>
                                        </button>
                                        <button type="button" class="btn btn-primary" onclick="scanCode()">
                                            <i class="ace-icon fa fa-barcode"></i>
                                            <span class="bigger-110">扫码查单</span>
                                        </button>
                                    </div>

                                    <div class="pull-left row">
                                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                            <input class="form-control" id="search_scanCode"
                                                   type="text" placeholder="请扫描唯一码"/>
                                        </div>
                                    </div>

                                    <div class="btn-group btn-group-sm pull-right">
                                        <button type="button" class="btn btn-info" onclick="showAdvSearchPanel();">
                                            <i class="ace-icon fa fa-binoculars"></i>
                                            <span class="bigger-110">高级查询</span>
                                        </button>

                                    </div>
                                </div>
                            </div>
                            <div class="hr hr-2 hr-dotted"></div>

                            <div class="widget-main" id="searchPanel" style="display:none">
                                <form class="form-horizontal" role="form" id="searchForm">
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 text-right control-label" for="search_code">批次号</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="search_code" name="filter_LIKES_billNo"
                                                   type="text" onkeyup="this.value=this.value.toUpperCase()"
                                                   placeholder="模糊查询"/>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="search_createTime">导入时间 </label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
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

                                    </div>
                                    <!-- #section:elements.form -->
                                    <div class="form-group">

                                    </div>
                                    <div class="form-group">
                                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-11 btnPosition">
                                            <button type="button" class="btn btn-sm btn-primary" onclick="_search()">
                                                <i class="ace-icon fa fa-search"></i>
                                                <span class="bigger-110">查询</span>
                                            </button>
                                            <button type="reset" class="btn btn-sm btn-warning"
                                                    onclick="_clearSearch()">
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
<script type="text/javascript" src="<%=basePath%>/views/tag/birthController.js"></script>
<jsp:include page="../base/upload_dialog.jsp"></jsp:include>
<jsp:include page="uploadExcel_Code.jsp"></jsp:include>
</body>
</html>