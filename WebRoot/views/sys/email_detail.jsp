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
        <div class="col-xs-12">
            <div class="widget-box widget-color-blue  light-border">

                <div class="widget-header">
                    <h5 class="widget-title">主题:${email.title}</h5>

                    <div class="widget-toolbar no-border">
                        <button class="btn btn-xs btn-light bigger" onclick="history.back(-1);">
                            <i class="ace-icon fa fa-arrow-left"></i>
                            返回
                        </button>
                    </div>
                </div>
                <div class="widget-body">
                    <div class="widget-main padding-12">
                        <form class="form-horizontal" role="form" id="editForm">
                            <div class="form-group">
                                <label class="col-xs-1 control-label" for="search_recipients">收件人</label>
                                <div class="col-xs-10">
                                    <input class="form-control" id="search_recipients" name="recipients"
                                           type="text" disabled value="${email.recipients}"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-1 control-label" for="search_sendTime">发送时间</label>
                                <div class="col-xs-10">
                                    <input class="form-control" id="search_sendTime" name="sendTime"
                                           type="text" disabled value="${email.sendTime}"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-xs-1 control-label" for="search_adjunctUrl">附件</label>
                                <div class="col-xs-10" id="search_adjunctUrl" name="adjunctUrl">

                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-xs-1 control-label" for="search_content">内容</label>
                                <div class="col-xs-10">
                                    <label class="col-sm-12 control-label" style="background:#eee;text-align: left;padding: 0 12px" id="search_content">${email.content}</label>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>


</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>

</body>

<script type="text/javascript">
    $(function () {
        var adjunct= "${email.adjunctUrl}";
        var adjuncts =adjunct.split(";");

        for(var i=0;i<adjuncts.length;i++) {
             var html="";
             html="<div class='col-xs-2'>";
            var fileName=""+adjuncts[i];
             html+="<a href='javascript:download("+'"'+fileName+'"'+")'>"+adjuncts[i]+"</a>";
             html+="</div>";
            $("#search_adjunctUrl").append(html);
        }
    });
    function download(fileName){
       window.location.href=basePath+"/email/download.do?fileName="+fileName;
    }
</script>
</html>