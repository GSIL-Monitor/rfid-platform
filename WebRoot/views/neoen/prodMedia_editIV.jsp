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
    <jsp:include page="../baseView.jsp"></jsp:include>

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
                    <h5 class="widget-title">媒体查询</h5>
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

                                <label class="col-xs-1 control-label no-padding-right" for="form_isShow">是否展示</label>
                                <div class="col-xs-3">
                                    <select class="form-control" id="form_isShow" name="isShow" type="text">
                                        <option value="Y">是</option>
                                        <option value="N">否</option>
                                    </select>
                                </div>

                                <label class="col-xs-1 control-label no-padding-right" for="form_duration">播放时长</label>
                                <div class="col-xs-3">
                                    <input class="form-control" id="form_duration" name="duration" type="text" />
                                </div>

                                <label class="col-xs-1 control-label no-padding-right" for="form_seqNo">序号</label>
                                <div class="col-xs-3">
                                    <input class="form-control" id="form_seqNo" name="seqNo" type="text" readonly/>
                                </div>
                                <input class="form-control" id="form_Id" name="id" type="text" style="display: none"/>
                            </div>

                            <div class="form-group">

                                <label class="col-xs-1 control-label no-padding-right" for="form_url">URL</label>
                                <div class="col-xs-7">
                                    <input class="form-control" id="form_url" name="url" type="text" readonly/>
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
                    <h5 class="widget-title">媒体预览</h5>

                    <div class="widget-toolbar no-border">
                        <%--<button class="btn btn-xs btn-light bigger" id="btnshow" onclick="openUpload()">--%>
                        <%--<i class="ace-icon fa fa-plus"></i>--%>
                        <%--添加视频--%>
                        <%--</button>--%>
                        <button class="btn btn-xs btn-light bigger" id="btnhide" onclick="cancel()" style="display:none">
                            <i class="ace-icon fa fa-mail-reply"></i>
                            取消
                        </button>
                    </div>
                </div>
                <div class="widget-body" >
                    <div class="widget-main" id="showMediaArea" align="center" style="height: 500px">
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="../layout/footer.jsp"></jsp:include>

</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
</body>
<script>
    $(function () {
        $("#form_url").val("${styleMedia.url}");
        $("#form_seqNo").val("${styleMedia.seqNo}");
        $("#form_Id").val("${styleMedia.id}");
        $("#form_isShow").find("option[value='${styleMedia.isShow}']").attr("selected", true);
        $("#form_duration").val("${styleMedia.duration}");
        var type="${styleMedia.type}";
        if(type!=""){
            var url = basePath + "/neoen/" + "${styleMedia.url}";
            if(type=="I"){
               var html="<img height='450px' src='"+url+"'>";
                $("#showMediaArea").html(html);
            }else if(type=="V"){
               var html="<video width='600' height='450' id='video' controls>";
                html+="<source src='"+url+"'>";
                html+="</video>";
                $("#showMediaArea").html(html);
            }
        }
    });

       function save() {
           var formData = $("#editForm").serialize();
           $.ajax({
               url: basePath + "/neoen/prodMedia/saveMediaIV.do",
               type: 'post',
               data: formData,
               dataType: 'json',
               success: function (result) {
                   if (result.success == true || result.success == 'true') {
                       bootbox.alert("修改成功");

                   } else {
                       bootbox.alert("保存失败");
                   }
               }
           });
       }

</script>
</html>