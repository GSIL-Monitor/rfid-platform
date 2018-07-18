<%@ page pageEncoding="UTF-8" import="java.util.*" language="java" %>
<%
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + request.getContextPath();
%>
<html>
<head>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>

    <jsp:include page="../baseView.jsp"></jsp:include>

    <style>
        .has-feedback .form-control {
            padding-right: 0px;
        }
        .file-drop-zone{
             height:auto;
         }
        .imgdel {
            width: 25px;
            height: 25px;
            background-image:url("<%=basePath%>/Olive/plugin/zyupload/skins/images/delete_blue.png");
            background-repeat: no-repeat;
            background-size: 100%;
            position: absolute;
            margin-top: -245px;
            margin-left: 170px;
        }

        .imgdel:hover {
            cursor: pointer;
        }
    </style>
</head>
<body class="no-skin">
<div class="main-container" id="main-container">
    <script>
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>

    <div class="main-content">
        <div class="col-xs-12">
            <div class="widget-box widget-color-blue light-border">
                <div class="widget-header">
                    <h5 class="widget-title">基本信息</h5>
                    <div class="btn-group btn-group-sm pull-right">
                        <button class="btn btn-primary" type="button" onclick="history.back(-1)">
                            <i class="ace-icon fa fa-arrow-left"></i> <span
                                class="bigger-110">返回</span>
                        </button>
                    </div>
                </div>
                <div class="widget-body">
                    <div class="widget-main padding-12">
                        <form class="form-horizontal" role="form" id="editForm">
                            <div class="form-group">
                                <label class="col-xs-4 col-sm-4,col-md-1 col-lg-1 control-label">款号</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <%--<input type="text" name="styleId" class="form-control" id="form_styleId"/>--%>
                                    <div class="input-group">
                                        <input class="form-control" id="form_styleId" type="text" name="styleId"
                                               readonly="">
                                        <span class="input-group-btn" onclick="openstyleDialog('#form_styleId','#form_styleName',iniColor)">
                                                         <button class="btn btn-sm btn-default" type="button"
                                                                 id="btnStyle">
                                                             <i class="ace-icon fa fa-list"></i>
                                                         </button>
								                      </span>
                                        <input class="form-control" id="form_styleName" type="text" name="styleName"
                                               readonly="" placeholder="款名">
                                    </div>
                                </div>
                                <label class="col-xs-4 col-sm-4,col-md-1 col-lg-1 control-label">颜色编码</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <%--<input type="text" name="colorId" class="form-control" id="form_colorId"/>--%>
                                    <select class="chosen-select  form-control" id="form_colorId" >
                                    </select>
                                </div>
                            </div>
                            <%--<div class="form-group" style="display: none">--%>
                                <%--<label class="col-xs-4 col-sm-4,col-md-1 col-lg-1 control-label">图片路径</label>--%>
                                <%--<div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">--%>
                                    <%--<input type="text" name="urls" class="form-control" id="form_urls"/>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        </form>
                    </div>
                </div>
                <div class="widget-header">
                    <h5 class="widget-title">上传图片</h5>
                    <div class="btn-group btn-group-sm pull-right" hidden>
                        <button class="btn btn-primary" type="button" onclick="showUpload()">
                            <i class="ace-icon fa fa-add"></i> <span
                                class="bigger-110">上传</span>
                        </button>
                    </div>
                </div>
                <div class="widget-body">
                    <div class="widget-main padding-12">
                        <div>
                            <ul class="ace-thumbnails clearfix" id="gallery">
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
    <jsp:include page="../layout/footer_js.jsp"></jsp:include>
    <jsp:include page="../base/style_dialog.jsp"></jsp:include>
    <script src="<%=basePath%>/Olive/plugin/photo-gallery.js"></script>
    <script src="photo_add_controller.js"></script>
</div>
<jsp:include page="photo_upload_dialog.jsp"></jsp:include>
</body>
</html>
