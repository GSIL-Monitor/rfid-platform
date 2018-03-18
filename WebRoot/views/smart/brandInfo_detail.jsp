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
<jsp:include page="../baseView.jsp"></jsp:include>
<script type="text/javascript">
        var basePath = "<%=basePath%>";
</script>


	<link rel="stylesheet" href="<%=basePath%>/Olive/plugin/img-show/css/base.css" />
	<link rel="stylesheet" href="<%=basePath%>/Olive/plugin/img-show/css/styles.css" />
	<link rel="stylesheet" href="<%=basePath%>/Olive/plugin/img-show/touchTouch/touchTouch.css"/>


<style>
#lbl_remark{
	background-color:#f6f6f6;
	text-align:left
}
#main-container{
	background-color:#ffffff;
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
				<div class="widget-body">
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
											<label class="col-xs-4 col-sm-4 col-md-1 control-label padding-right"
												for="form_brand">编号</label>
											<div class="col-xs-8 col-sm-8 col-md-5">
												<input name="brand" id="form_brand" class="form-control"
													type="text" value="${brandInfo.brand}" />
											</div>

											<label class="col-xs-4 col-sm-4 col-md-1 control-label padding-right"
												for="form_name">名称</label>

											<div class="col-xs-8 col-sm-8 col-md-5">
												<input name="name" id="form_name" class="form-control"
													type="text" value="${brandInfo.name}" />
											</div>
										</div>
										<div class="form-group">
											<label class="col-xs-4 col-sm-4 col-md-1 control-label padding-right"
												for="form_address">归属地</label>
											<div class="col-xs-8 col-sm-8 col-md-5">
												<input name="address" id="form_address" class="form-control"
													type="text" value="${brandInfo.address}" />
											</div>

											<label class="col-xs-4 col-sm-1 control-label padding-right"
												for="form_designer">设计师</label>
											<div class="col-xs-8 col-sm-5">
												<input name="designer" id="form_designer"
													class="form-control" type="text"
													value="${brandInfo.designer}" />
											</div>
										</div>

										<div class="form-group">
											<label class="col-xs-1 col-sm-1 control-label padding-right"
												for="form_remark">备注</label>
											<div class="col-xs-11 col-sm-11">
												<label class="col-sm-12 control-label" style="text-align:left" id="lbl_remark">${brandInfo.remark}</label>
											</div>
										</div>
										<div class="form-group" hidden>
											<div
												class="col-xs-5 col-sm-5 col-xs-offset-1 col-sm-offset-1">
												<input id="form_remark" class="form-control" name="remark"
													value="${brandInfo.remark}" />
											</div>
											<div
												class="col-xs-5 col-sm-5 col-xs-offset-1 col-sm-offset-1">
												<input id="form_url" name="url" class="form-control"
													value="${brandInfo.url}" />
												<input id="form_seqNo" name="seqNo" class="form-control" value="${brandInfo.url}" />
											</div>
										</div>
									</form>
								</div>
							</div>
						</div>
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
						<div class="widget-main padding-12">
								<div class="clearfix img-gather" id="thumbs">
						</div>
					</div>
				</div>
			</div>
		</div>
		<jsp:include page="../layout/footer_js.jsp"></jsp:include>
	</div>
	</div>
	<jsp:include page="../layout/footer.jsp"></jsp:include>
	<script src="<%=basePath%>/Olive/plugin/img-show/touchTouch/touchTouch.jquery.js"></script>

	<script>
			$(function(){
				initBrandImg();
				$("#form_brand").attr("readonly",true);
				$("#form_name").attr("readonly",true);
				$("#form_address").attr("readonly",true);
				$("#form_designer").attr("readonly",true);
			});	
			
			function initBrandImg(){
			        var images=$("#form_url").val();
			        if(images!=""){
			            var n=images.split(',');
			            var html='';
			            for(var i=0;i< n.length;i++){
			                html +='<a href="<%=basePath%>/mirror/brand/'+n[i]+'" style="background-image:url(<%=basePath%>/mirror/brand/'+n[i]+')"></a>';
			            }
			            $("#thumbs").append(html);
			        }
			        $("#thumbs a").touchTouch();
				}
		
</script>
</body>
</html>