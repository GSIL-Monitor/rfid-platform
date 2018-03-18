<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>

<script type="text/javascript">
        var basePath = "<%=basePath%>";
</script>


<link rel="stylesheet"
	href="<%=basePath%>/Olive/plugin/img-show/css/base.css" />
<link rel="stylesheet"
	href="<%=basePath%>/Olive/plugin/img-show/css/styles.css" />
<link rel="stylesheet"
	href="<%=basePath%>/Olive/plugin/img-show/touchTouch/touchTouch.css" />

<jsp:include page="../baseView.jsp"></jsp:include>
<style>
#main-container{
	background-color: #ffffff;
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
							<button class="btn btn-xs btn-light bigger"
								onclick="javascript:history.back(-1);">
								<i class="ace-icon fa fa-arrow-left"></i> 返回
							</button>
						</div>
					</div>

					<div class="widget-main" id="searchPannel">
						<form class="form-horizontal" id="editform" role="form">
							<div class="form-group">
								<label
									class="col-xs-4 col-sm-1 col-xs-offset-3 control-label padding-right"
									for="form_id">编号</label>
								<div class="col-xs-8 col-sm-5">
									<input name="id" id="form_id" class="form-control" type="text"
										value="${starInfo.id}" readonly />
								</div>
							</div>
							<div class="form-group">
								<label
									class="col-xs-4 col-sm-1 col-xs-offset-3 control-label padding-right"
									for="form_starName">名称</label>

								<div class="col-xs-8 col-sm-5">
									<input class="form-control" id="form_starName" type="text"
										name="starName" value="${starInfo.starName}" readonly />
								</div>
							</div>
							<div class="form-group">
								<label
									class="com-xs-4 col-sm-1 col-xs-offset-3 control-label padding-right"
									for="form_isShow">是否展示</label>
								<div class="col-xs-8 col-sm-5">
									<select class="form-control" disabled id="form_isShow" name="isShow">
										<option value="Y">展示</option>
										<option value="N">不展示</option>
									</select>
								</div>
							</div>
							<div class="form-group">
								<div class="col-xs-5 col-sm-5 col-xs-offset-4 col-sm-offset-4"
									hidden>
									<input id="form_url" name="url" class="form-control"
										value="${starInfo.url}" />
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
			<div class="col-xs-12">
				<div class="widget-Box widget-color-blue light-border">
					<div class="widget-header">
						<h5 class="widget-title">图片</h5>
						<div class="widget-toolbar no-border">
						</div>
					</div>
					<div class="widget-body">
						<div class="widget-main">
							<div class="clearfix img-gather" id="thumbs"></div>
						</div>
					</div>
				</div>
		</div>
</div>
			<jsp:include page="../layout/footer.jsp"></jsp:include>
</div>
		<jsp:include page="../layout/footer_js.jsp"></jsp:include>
		<script src="<%=basePath%>/Olive/plugin/img-show/touchTouch/touchTouch.jquery.js"></script>
		<script>
		$(function(){
			initStarImg();
		});
		
		function initStarImg(){
	        var images=$("#form_url").val();
	        if(images!=""){
	            var n=images.split(',');
	            var html='';
	            for(var i=0;i< n.length;i++){
	                html +='<a href="<%=basePath%>/mirror/starInfo/'+n[i]+'" style="background-image:url(<%=basePath%>/mirror/starInfo/'+n[i]+')"></a>';
	            }
	            $("#thumbs").append(html);
	        }
	        $("#thumbs a").touchTouch();
		}
		</script>
</body>
</html>