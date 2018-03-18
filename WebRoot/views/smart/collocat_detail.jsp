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
<style>

#lbl_remark{
	background-color:#f4f4f4;
	text-align:left
}
#main-container{
	background-color:#ffffff
}
</style>


<link
	href='<%=basePath%>/Olive/plugin/froala_editor/css/froala_editor.min.css'
	rel='stylesheet' type='text/css' />
<link
	href='<%=basePath%>/Olive/plugin/froala_editor/css/froala_style.min.css'
	rel='stylesheet' type='text/css' />
<link
	href='<%=basePath%>/Olive/plugin/froala_editor/css/plugins/char_counter.css'
	rel="stylesheet" />
<link
	href='<%=basePath%>/Olive/plugin/froala_editor/css/plugins/colors.css'
	rel='stylesheet' />
<link
	href="<%=basePath%>/Olive/plugin/froala_editor/css/themes/gray.min.css"
	rel="stylesheet" type="text/css" />
	
	<link rel="stylesheet" href="<%=basePath%>/Olive/plugin/img-show/css/base.css" />
	<link rel="stylesheet" href="<%=basePath%>/Olive/plugin/img-show/css/styles.css" />
	<link rel="stylesheet" href="<%=basePath%>/Olive/plugin/img-show/touchTouch/touchTouch.css"/>
	
<script src="<%=basePath%>/Olive/plugin/froala_editor/js/jquery.min.js"></script>
<script
	src="<%=basePath%>/Olive/plugin/froala_editor/js/plugins/char_counter.min.js"></script>
<script
	src="<%=basePath%>/Olive/plugin/froala_editor/js/plugins/colors.min.js"></script>
<script
	src="<%=basePath%>/Olive/plugin/froala_editor/js/froala_editor.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/Olive/plugin/froala_editor/js/languages/zh_cn.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/Olive/plugin/froala_editor/js/languages/ja.js"></script>
	

<!--[if lt IE 9]>
    <script src="../js/froala_editor_ie8.min.js"></script>
<![endif]-->



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
				<div id="page-content">
					<div class="row">
						<div class="col-xs-12">
							<!-- PAGE CONTENT BEGINS -->
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

											<div class="widget-main" id="searchPanel">
												<form class="form-horizontal" role="form" id="editForm">
													<!--                                     <input type="hidden" name="id" id="form_id"/>
 -->
													<div class="form-group">


														<label class="col-xs-4 col-sm-4 col-md-1 control-label no-padding-right"
															for="form_id">套装编号</label>

														<div class="col-xs-8 col-sm-8 col-md-5">
															<input class="form-control" id="form_id" name="id"
																readonly type="text" value="${collocat.id}" />
														</div>
														<label class="col-xs-4 col-sm-1 control-label no-padding-right"
															for="form_styleIds">关联款号</label>

														<div class="col-xs-8 col-sm-5">
															<input class="form-control" id="form_styleIds" readonly
																name="styleIds" type="text" value="${collocat.styleIds}"
																oninput="changeids()" />
														</div>
													</div>
													<div class="form-group">
														<label class="col-xs-4 col-sm-1 control-label no-padding-right"
															for="form_isShow" readonly>是否展出</label>

														<div class="col-xs-8 col-sm-5">
															<select class="form-control" id="form_isShow"
																name="isShow">
																<option value="Y">展出</option>
																<option value="N">不展出</option>
															</select>
														</div>
														<label class="col-xs-4 col-sm-1 control-label no-padding-right"
															for="form_seqNo">序号</label>
														<div class="col-xs-8 col-sm-5">
															<input class="form-control" id="form_seqNo" name="seqNo"
																type="text" value="${collocat.seqNo}" readonly />
														</div>
													</div>
													<div class="form-group">
														<label class="col-xs-4 col-sm-1 control-label no-padding-right"
															for="form_price">价格</label>
														<div class="col-xs-8 col-sm-5">
															<input class="form-control" id="form_price" name="price"
																type="text" value="${collocat.price}" readonly />
														</div>
													</div>
													<input type="text" id="from_reamrk" name="remark"
														style="display: none" /> <input type="text"
														class="form-control" id="form_url" name="url"
														value="${collocat.url}" style="display: none" /> <br />
													<div class="form-group" id="colRemark">
														<label class="col-sm-1 control-label no-padding-right"
															for="lbl_remark">备注</label>
														<div class="col-xs-11 col-sm-11">
															<label class="col-sm-12 control-label" style="text-align:left" id="lbl_remark">${collocat.remark}</label>
														</div>
													</div>
												</form>

											</div>

										</div>

										<!-- PAGE CONTENT ENDS -->
									</div>
									<div class="col-xs-12">
										<div class="widget-box widget-color-blue  light-border">
											<div class="widget-header">
												<h5 class="widget-title">图片</h5>
											</div>
											<div class="widget-body">
												<div class="widget-main padding-12">
														<div class="clearfix img-gather" id="thumbs">
														</div>
												</div>
											</div>
										</div>
									</div>
									<!-- /.col -->
								</div>
								<!-- /.row -->
								<!--/#page-content-->
							</div>
						</div>
					</div>
					<jsp:include page="../layout/footer.jsp"></jsp:include>

				</div>
				<jsp:include page="../layout/footer_js.jsp"></jsp:include>
				<jsp:include page="../base/style_dialog.jsp"></jsp:include>
					<script src="<%=basePath%>/Olive/plugin/img-show/touchTouch/touchTouch.jquery.js"></script>
				
<script type="text/javascript">
	
		$(function() {
			initShow();
			initColImg();
			$("#form_isShow").attr("disabled","disabled");
			if("${collocat.remark}"=="")
			{
				$("#colRemark").hide();
			}
		});
		function initShow() {
			if ("${collocat.isShow}" != "")
				$("#form_isShow").find("option[value='${collocat.isShow}']")
						.attr("selected", true);
		}
		
		function initColImg(){
	        var images=$("#form_url").val();
	        if(images!=""){
	            var n=images.split(',');
	            var html='';
	            for(var i=0;i< n.length;i++){
	                html +='<a href="<%=basePath%>/mirror/collocat/'+n[i]+'" style="background-image:url(<%=basePath%>/mirror/collocat/'+n[i]+')"></a>';
	            }
	            $("#thumbs").append(html);
	        }
	        $("#thumbs a").touchTouch();
		}
		
	</script>
</body>

</html>