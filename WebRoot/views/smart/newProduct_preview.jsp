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




</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
</body>
<jsp:include page="../base/style_dialog.jsp"></jsp:include>
<script type="text/javascript">
    $(function () {
        init();
    });
    function init(){
        $.ajax({
            type:"POST",
            url:basePath+"/smart/newProduct/list.do",
            dataType:"json",
            success:function(result){
                for(var i=0;i<result.length;i++){
                    var n=result[i].images.split(',');
                    for(var j=0;j< n.length;j++){
                        var imgUrl=basePath+"/mirror/newProduct/"+result[i].styleId+"/"+n[j];


                    }
                }
            }
        });
    }

</script>
</html>