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
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>
    <div class="main-content">
        <div class="main-content-inner">
            <div id="page-content">
                <div class="col-xs-12">

                    <div class="widget-box widget-color-blue  light-border">
                    <div class="widget-header">
                        <h5 class="widget-title">数据源连接设置</h5>
                        <div class="widget-toolbar no-border">
                            <button class="btn btn-xs btn-light bigger" onclick="javascript:history.back(-1);">
                                <i class="ace-icon fa fa-arrow-left"></i>
                                返回
                            </button>
                        </div>
                    </div>
                    <div class="widget-body">
                        <div class="widget-main padding-12">
                    <form class="form-horizontal" role="form" id="editForm">
                        <input id="form_id" name="id" type="hidden" />
                        <input id="form_status" name="status" type="hidden" />
                        <div class="form-group">
                            <label class="< col-xs-offset-1 col-xs-2 control-label text-right" for="form_name">数据库描述</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="form_name" name="name" type="text" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-offset-1 col-xs-2 control-label text-right" for="form_type">数据库类型</label>
                            <div class="col-xs-5">
                                <select class="chosen-select form-control" id="form_type" name="type" >
                                    <option value="">--请选择--</option>
                                    <option value="S">SQL Server</option>
                                    <option value="M">mysql</option>
                                    <option value="O">Oracle</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-offset-1 col-xs-2 control-label text-right" for="form_dsIp">服务器IP</label>
                            <div class="col-xs-2">
                                <input class="form-control" id="form_dsIp" name="dsIp" type="text" />
                            </div>

                            <label class="col-xs-1  control-label text-right" for="form_dsPort">端口</label>
                            <div class="col-xs-2 ">
                                <input class="form-control" id="form_dsPort" name="dsPort" type="text" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-offset-1 col-xs-2 control-label text-right" for="form_dbName">数据库名称</label>
                            <div class="col-xs-5 ">
                                <input class="form-control" id="form_dbName" name="dbName" type="text" />
                            </div>

                        </div>
                        <div class="form-group">
                            <label class="col-xs-offset-1 col-xs-2 control-label text-right" for="form_dbUser">数据库用户</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="form_dbUser" name="dbUser" type="text" />
                            </div>

                        </div>
                        <div class="form-group">
                            <label class="col-xs-offset-1 col-xs-2 control-label text-right" for="form_dbPass">数据库密码</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="form_dbPass" name="dbPass" type="text" />
                            </div>

                        </div>
                        <div class="form-group">
                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-11 btnPosition">
                                <button type="button" class="btn btn-sm btn-primary" onclick="connectionTest()">
                                    <i class="ace-icon fa fa-exchange"></i>
                                    <span class="bigger-110">测试连接</span>
                                </button>
                                <button type="button" class="btn btn-sm btn-warning" onclick="save()">
                                    <i class="ace-icon fa fa-save"></i>
                                    <span class="bigger-110">保存</span>
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
    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>
</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>


<script>

    $(function() {
          if ("${DataSrcInfo}"!=""){
              $("#form_id").val("${DataSrcInfo.id}");
              $("#form_status").val("${DataSrcInfo.status}");
              $("#form_name").val("${DataSrcInfo.name}");
              $("#form_type").val("${DataSrcInfo.type}");
              $("#form_dsIp").val("${DataSrcInfo.dsIp}");
              $("#form_dsPort").val("${DataSrcInfo.dsPort}");
              $("#form_dbName").val("${DataSrcInfo.dbName}");
              $("#form_dbUser").val("${DataSrcInfo.dbUser}");
              $("#form_dbPass").val("${DataSrcInfo.dbPass}");
          }
    });


    function save() {
        var progressDialog = bootbox.dialog({
            message: '<p><i class="fa fa-spin fa-spinner"></i> 正在连接...</p>'
        });

        $.ajax({
            url: basePath + "/exchange/datasource/save.do",
            data: $("#editForm").serialize(),
            type: 'post',
            dataType: 'json',
            success: function (result) {
                progressDialog.modal('hide');
                if (result.success == true || result.success == 'true') {
                    bootbox.alert("保存成功");
                } else {
                    bootbox.alert(result.result);
                }
            }
        })
    }
    function connectionTest(){
        var progressDialog = bootbox.dialog({
            message: '<p><i class="fa fa-spin fa-spinner"></i> 正在连接...</p>'
        });

        $.ajax({
            url: basePath + "/exchange/datasource/connectionTest.do",
            data: $("#editForm").serialize(),
            type: 'post',
            dataType: 'json',
            success: function (result) {
                progressDialog.modal('hide');
                if (result.success == true || result.success == 'true') {

                    bootbox.alert("连接成功")
                } else {
                    bootbox.alert(result.result);
                }
            }
        })
    }
</script>
</body>
</html>