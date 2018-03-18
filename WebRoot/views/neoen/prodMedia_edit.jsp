<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <style>
        .has-feedback .form-control {
            padding-right: 0px;
        }

    </style>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/Olive/plugin/zyupload/skins/zyupload-1.0.0.min.css"/>
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
        <div class="col-xs-12">
            <div class="widget-box widget-color-blue  light-border">

                <div class="widget-header">
                    <h5 class="widget-title">基本信息</h5>

                    <div class="widget-toolbar no-border">
                        <button class="btn btn-xs btn-light bigger" onclick="saveDesc()">
                            <i class="ace-icon fa fa-save"></i>
                            保存
                        </button>
                        <button class="btn btn-xs btn-light bigger" onclick="historyBack()">
                            <i class="ace-icon fa fa-arrow-left"></i>
                            返回
                        </button>
                    </div>
                </div>
                <div class="widget-body">
                    <div class="widget-main padding-12">
                        <form class="form-horizontal" role="form" id="editForm">
                            <div class="form-group">
                                <label class="col-xs-1 control-label no-padding-right" for="form_styleId">款号</label>

                                <div class="col-xs-2">
                                    <div class="input-group">
                                        <input class="form-control" id="form_styleId"
                                               type="text" name="styleId" readonly/>
                                                     <span class="input-group-btn">
                                                         <button class="btn btn-sm btn-default" type="button" id="btnStyle"
                                                                 onclick="openstyleDialog('#form_styleId','#form_styleName',findFab)">
                                                             <i class="ace-icon fa fa-list"></i>
                                                         </button>
								                      </span>
                                        <input class="form-control" id="form_styleName"
                                               type="text" name="styleName" readonly
                                               placeholder="款名"/>
                                    </div>
                                </div>

                                <label class="col-xs-1 control-label" for="form_fab">FAB</label>

                                <div class="col-xs-2">
                                    <input class="form-control" id="form_fab" name="fab"
                                           type="text" readonly/>
                                </div>
                                <%--<label class="col-xs-1 control-label" for="form_designer">设计师</label>--%>

                                <%--<div class="col-xs-2">--%>
                                    <%--<input class="form-control" id="form_designer" name="designer"--%>
                                           <%--type="text"/>--%>
                                <%--</div>--%>

                                <%--<label class="col-xs-1 control-label" for="form_remark">备注</label>--%>

                                <%--<div class="col-xs-2">--%>
                                    <%--<input class="form-control" id="form_remark" name="remark"--%>
                                           <%--type="text"/>--%>
                                <%--</div>--%>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xs-12">
            <div class="widget-box widget-color-blue  light-border">

                <div class="widget-header">
                    <h5 class="widget-title">图片/视频信息</h5>

                    <div class="widget-toolbar no-border">
                        <button class="btn btn-xs btn-light bigger" onclick="IVup()">
                            <i class="ace-icon fa fa-arrow-up"></i>
                            上移
                        </button>
                        <button class="btn btn-xs btn-light bigger" onclick="IVdown()">
                            <i class="ace-icon fa fa-arrow-down"></i>
                            下移
                        </button>
                        <button class="btn btn-xs btn-light bigger" onclick="showChangeDuration()">
                            <i class="ace-icon fa fa-save"></i>
                            修改时长
                        </button>
                        <button class="btn btn-xs btn-light bigger" onclick="showIVDialog()">
                            <i class="ace-icon fa fa-save"></i>
                            上传
                        </button>
                        <button class="btn btn-xs btn-light bigger" onclick="deleteIV()">
                            <i class="ace-icon fa fa-trash-o"></i>
                            删除
                        </button>
                    </div>
                </div>
                <div class="widget-body">
                    <div class="widget-main padding-12">
                            <table id="gridIV"></table>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xs-12">
            <div class="widget-box widget-color-blue  light-border">

                <div class="widget-header">
                    <h5 class="widget-title">音频信息</h5>

                    <div class="widget-toolbar no-border">
                        <button class="btn btn-xs btn-light bigger" onclick="Aup()">
                            <i class="ace-icon fa fa-arrow-up"></i>
                            上移
                        </button>
                        <button class="btn btn-xs btn-light bigger" onclick="Adown()">
                            <i class="ace-icon fa fa-arrow-down"></i>
                            下移
                        </button>
                        <button class="btn btn-xs btn-light bigger" onclick="showADialog()">
                            <i class="ace-icon fa fa-save"></i>
                            上传
                        </button>
                        <button class="btn btn-xs btn-light bigger" onclick="deleteA()">
                            <i class="ace-icon fa fa-trash-o"></i>
                            删除
                        </button>
                    </div>
                </div>
                <div class="widget-body">
                    <div class="widget-main padding-12">
                        <table id="gridA"></table>
                    </div>
                </div>
            </div>
        </div>

    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>

</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
</body>
<jsp:include page="../base/style_dialog.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/zyupload/zyUpload.js"></script>
<script type="text/javascript" src="<%=basePath%>/views/neoen/prodMedia_edit.js"></script>
<script>
    $(function () {
        if ("${styleDesc}" != "") {
            //编辑
            var styleId = "${styleDesc.styleId}";
            $("#form_styleId").val(styleId);
            $("#form_styleName").val("${styleDesc.styleName}");
            $("#btnStyle").removeAttr("onclick");
            <%--$("#form_designer").val("${styleDesc.designer}");--%>
            <%--$("#form_remark").val("${styleDesc.remark}");--%>
            $("#form_fab").val("${styleDesc.fab}");

            initGridIV(basePath + "/neoen/prodMedia/findMediaIV.do?styleId=" + styleId);
            initGridA(basePath + "/neoen/prodMedia/findMediaA.do?styleId=" + styleId);

            $("#form_AstyleId").val(styleId);
            $("#form_IVstyleId").val(styleId);

            var a_parent_column = $("#gridA").closest('.widget-body');
            var iv_parent_column = $("#gridIV").closest('.widget-body');
            $("#gridA").jqGrid( 'setGridWidth', a_parent_column.width()-20);
            $("#gridIV").jqGrid( 'setGridWidth', iv_parent_column.width()-20);
        } else {
            //新增
        }
    });
</script>
<jsp:include page="prodMedia_uploadA_dialog.jsp"></jsp:include>
<jsp:include page="prodMedia_uploadIV_dialog.jsp"></jsp:include>
<jsp:include page="prodMedia_duration_dialog.jsp"></jsp:include>
</html>