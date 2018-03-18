<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>
    <link href='<%=basePath%>/Olive/plugin/froala_editor/css/froala_editor.min.css' rel='stylesheet' type='text/css' />
    <link href="<%=basePath%>/Olive/plugin/froala_editor/css/themes/gray.min.css" rel="stylesheet" type="text/css" />
    <script src="<%=basePath%>/Olive/plugin/froala_editor/js/jquery.min.js"></script>
    <script src="<%=basePath%>/Olive/plugin/froala_editor/js/plugins/char_counter.min.js"></script>
    <script src="<%=basePath%>/Olive/plugin/froala_editor/js/froala_editor.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/Olive/plugin/froala_editor/js/languages/zh_cn.js"></script>
    <script type="text/javascript" src="<%=basePath%>/Olive/plugin/froala_editor/js/languages/ja.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/Olive/plugin/zyupload/skins/zyupload-1.0.0.min.css"/>
    <script type="text/javascript" src="<%=basePath%>/Olive/plugin/zyupload/zyUpload.js"></script>
    <jsp:include page="../baseView.jsp"></jsp:include>
<style>
    .imgdel{
        width: 25px;
        height: 25px;
        background-image: url("<%=basePath%>/Olive/plugin/zyupload/skins/images/delete_blue.png");
        background-repeat: no-repeat;
        background-size: 100%;
        position: absolute;
        margin-top: -244px;
        margin-left: 170px;
    }
    .imgdel:hover{
        cursor: pointer;
    }
</style>
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
                    <button class="btn btn-xs btn-light bigger" onclick="save();">
                        <i class="ace-icon fa fa-save"></i>
                       保存
                    </button>
                    <button class="btn btn-xs btn-light bigger" onclick="javascript:history.back(-1);">
                        <i class="ace-icon fa fa-arrow-left"></i>
                        返回
                    </button>
                </div>
            </div>
            <div class="widget-body">
                <div class="widget-main padding-12">
            <form class="form-horizontal" role="form" id="editForm" method="post">
            <div class="form-group">

                    <label class="col-xs-1 control-label no-padding-right" for="form_id">编号</label>
                <div class="col-xs-2">
                    <input class="form-control" id="form_id" name="id" onkeyup="this.value=this.value.toUpperCase()"
                           type="text" />
                </div>

                <label class="col-xs-1 control-label" for="form_name">名称</label>
                <div class="col-xs-2">
                    <input class="form-control" id="form_name" name="name"
                           type="text" />
                </div>
                <label class="col-xs-1 control-label" for="form_fileType">文件类型</label>
                <div class="col-xs-2">
                    <select class="form-control" id="form_fileType" name="fileType">
                        <option value="I">图片</option>
                        <option value="V">视频</option>
                    </select>
                </div>


            </div>
                <div class="form-group">

                 <div class="col-xs-2">
                     <input class="form-control" id="form_url" name="url" style="display: none"
                            type="text"  />
                 </div>


                    <div class="col-xs-2" >
                        <input class="form-control" id="form_remark" name="remark" style="display: none"
                               type="text"  />
                    </div>
            </div>
                <div class="form-group">
                    <label class="col-sm-1 control-label no-padding-right"
                           >备注</label>
                    <div class="col-xs-11 col-sm-11">
                        <div id="editor"></div>
                    </div>
                </div>
            </form>
                </div>
            </div>
            </div>
        </div>


        <div class="col-xs-12">
            <div class="widget-box widget-color-blue  light-border">
                <div class="widget-header">
                    <h5 class="widget-title" id="widget_title">图片</h5>

                <div class="widget-toolbar no-border">
                    <button class="btn btn-xs btn-light bigger" id="btnshow" onclick="initUpload()">
                        <i class="ace-icon fa fa-plus"></i>
                        添加
                    </button>
                    <button class="btn btn-xs btn-light bigger" id="btnhide" onclick="cancel()" style="display:none">
                        <i class="ace-icon fa fa-mail-reply"></i>
                        取消
                    </button>
                </div>
                </div>
                <div class="widget-body" >
                    <div class="widget-main" id="uploadarea" align="center" style="height: 500px">

                        <ul class="ace-thumbnails clearfix" id="gallery">
                        </ul>
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
<script type="text/javascript">
    var isEdit=false;
    $(function(){

        if("${homeInfo}"!=""){
            isEdit=true;
        }
        //编辑时初始化数据
            if(isEdit){
                $("#form_id").val("${homeInfo.id}");
                $("#form_id").attr("readonly",true);
                $("#form_name").val("${homeInfo.name}");
                $("#form_fileType").find("option[value='${homeInfo.fileType}']").attr("selected",true);
                $("#form_fileType").attr("disabled",true);
                $("#form_url").val("${homeInfo.url}");
                $("#editor").html("${homeInfo.remark}");
                if("${homeInfo.fileType}"=="I"){
                    $("#widget_title").html("图片");
                    initImg("${homeInfo.url}");
                }else if("${homeInfo.fileType}"=="V"){
                    $("#widget_title").html("视频");
                    initMedia();
                }

            }
    });

</script>
<script type="text/javascript" src="<%=basePath%>/views/smart/homeInfo_edit_Controller.js"></script>
</html>