<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../search/searchBaseView.jsp"></jsp:include>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>

</head>
<body class="no-skin">
<div class="main-container" id="main-container" >
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
                                        <button type="button" class="btn btn-primary" onclick="bind()">
                                            <i class="ace-icon fa fa-plus"></i>
                                            <span class="bigger-110">绑定</span>
                                        </button>
                                        <button type="button" class="btn btn-primary" onclick="del()">
                                            <i class="ace-icon fa fa-edit"></i>
                                            <span class="bigger-110">解绑</span>
                                        </button>
                                       
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">

                                        <button class="btn btn-info" onclick="exportExcel();">

                                            <i class="ace-icon fa fa-file-excel-o"></i>
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
                            <div class="hr hr-2 hr-dotted"></div>

                            <div class="widget-main" id="searchPanel" style="display:none">
                                <form class="form-horizontal" role="form" id="searchForm">
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 text-right control-label" for="search_code">条码</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="search_code" name="filter_CONTAINS_code"
                                                   type="text" onkeyup="this.value=this.value.toUpperCase()"
                                                   placeholder="模糊查询"/>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 text-right control-label" for="search_name">EPC</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="search_name" name="filter_CONTAINS_epc"
                                                   type="text" placeholder="模糊查询"/>
                                        </div>
                      
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="search_updateTime">修改时间 </label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <div class="input-group">
                                                <input class="form-control date-picker" id="search_updateTime"
                                                       type="text" name="filter_GTE_updateTime"
                                                       data-date-format="yyyy-mm-dd"/>

                                            <span class="input-group-addon">
																		<i class="fa fa-exchange"></i>
																	</span>

                                                <input class="form-control date-picker" type="text"
                                                       class="input-sm form-control" name="filter_LTE_updateTime"
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

                        <div id="grid" style="height: 800px"></div>


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
<jsp:include page="../search/search_js.jsp"></jsp:include>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<link href="<%=basePath%>/kendoUI/styles/kendo.common-material.min.css" rel="stylesheet">
<link href="<%=basePath%>/kendoUI/styles/kendo.rtl.min.css" rel="stylesheet">
<link href="<%=basePath%>/kendoUI/styles/kendo.material.min.css" rel="stylesheet">
<script src="<%=basePath%>/kendoUI/js/kendo.all.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/kendo.timezones.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/cultures/kendo.culture.zh-CN.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/messages/kendo.messages.zh-CN.min.js"></script>

<script type="text/javascript" src="<%=basePath%>/Olive/plugin/zyupload/zyUpload.js"></script>

<jsp:include page="bind_edit.jsp"></jsp:include>
<jsp:include page="bind_unbind.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/tag/bindController.js"></script>
<script type="text/javascript">

</script>
</body>
</html>