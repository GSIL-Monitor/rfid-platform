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
                                        <button class="btn btn-success" onclick="freshJobs();">
                                            <i class="glyphicon glyphicon-refresh"></i>
                                            <span class="bigger-110">刷新</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-info" onclick="runJobs();">
                                            <i class="glyphicon glyphicon-play-circle" ></i>
                                            <span class="bigger-110">全部启动</span>
                                        </button>
                                        <button class="btn btn-warning" onclick="shutdownJobs()">
                                            <i class="glyphicon glyphicon-stop"></i>
                                            <span class="bigger-110">全部暂停</span>
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="hr hr4"></div>
                            <div class="widget-main" id="searchPanel" style="display:none">
                            </div>

                        </div>

                        <table id="grid" style="background:#ffffff"></table>

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
    <div class="modal fade" id="configModel" tabindex="-1" role="dialog"
         aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"
                            aria-hidden="true">×
                    </button>
                    <h4 class="modal-title" id="myModalLabel">
                        编辑
                    </h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" role="form" id="detailForm">
                        <input type="text" id="type" name="type" hidden="true" aria-hidden="true">
                        <fieldset disabled>
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="id">任务编号</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" id="id" name="id">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="name" class="col-sm-2 control-label">名称</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" id="name" name="name">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="checkbox">
                                    <label   class="col-sm-2 control-label">是否有效</label>
                                    <div class="col-sm-10">
                                        <label><input type="radio" name="enable" value="true">是</label>
                                        <label><input type="radio"  name="enable" value="false"> 否</label>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="checkbox">
                                    <label   class="col-sm-2  control-label">状态</label>
                                    <div class="col-sm-10">
                                        <label><input type="radio" name="configState" value="1">是</label>
                                        <label><input type="radio" name="configState" value="0"> 否</label>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="api" class="col-sm-2 control-label">API</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" id="api" name="api">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="updateDate" class="col-sm-2 control-label">更新日期</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" id="updateDate" name="updateDate">
                                </div>
                            </div>

                        </fieldset>
                        <div class="form-group">
                            <label for="description" class="col-sm-2 control-label">cron表达式</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="description" name="description">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="remark" class="col-sm-2 control-label">备注</label>
                            <div class="col-sm-10">
                                <textarea class="form-control" id="remark" rows="3" name="remark"></textarea>
                            </div>
                        </div>
                    </form>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger"
                            data-dismiss="modal">
                        <span class='glyphicon  glyphicon-remove' aria-hidden='true'></span>
                        关闭
                    </button>
                    <button type="button" class="btn btn-warning" onclick="submitForm()">
                        <span class='glyphicon  glyphicon-ok' aria-hidden='true'></span>
                        提交
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>

    <!--/.fluid-container#main-container-->
</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/syn/cronGen.js"></script>
<link href="<%=basePath%>/kendoUI/styles/kendo.common-material.min.css" rel="stylesheet">
<link href="<%=basePath%>/kendoUI/styles/kendo.rtl.min.css" rel="stylesheet">
<link href="<%=basePath%>/kendoUI/styles/kendo.material.min.css" rel="stylesheet">
<%--
<link href="<%=basePath%>/kendoUI/styles/kendo.dataviz.bootstrap.min.css" rel="stylesheet">
--%>
<script src="<%=basePath%>/kendoUI/js/kendo.all.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/kendo.timezones.min.js"></script>

<script src="<%=basePath%>/kendoUI/js/cultures/kendo.culture.zh-CN.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/messages/kendo.messages.zh-CN.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/views/syn/timerManagerController.js"></script>
<div id="dialog"></div>
<div id="progressDialog"></div>
<span id="notification"></span>



</body>
</html>