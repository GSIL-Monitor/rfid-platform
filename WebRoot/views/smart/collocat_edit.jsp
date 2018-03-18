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
.imgdel {
	width: 25px;
	height: 25px;
	background-image:
		url("<%=basePath%>/Olive/plugin/zyupload/skins/images/delete_blue.png");
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
<link href="<%=basePath%>/Olive/plugin/zyupload/skins/zyUpload.css"
	rel="stylesheet" />
<link
	href="<%=basePath%>/Olive/plugin/zyupload/skins/zyupload-1.0.0.min.css"
	rel="stylesheet" />

<link
	href='<%=basePath%>/Olive/plugin/froala_editor/css/froala_editor.min.css'
	rel='stylesheet' type='text/css' />
<link
	href='<%=basePath%>/Olive/plugin/froala_editor/css/froala_style.min.css'
	rel='stylesheet' type='text/css' />
<link
	href="<%=basePath%>/Olive/plugin/froala_editor/css/themes/gray.min.css"
	rel="stylesheet" type="text/css" />
<script src="<%=basePath%>/Olive/plugin/froala_editor/js/jquery.min.js"></script>

<script
	src="<%=basePath%>/Olive/plugin/froala_editor/js/froala_editor.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/Olive/plugin/froala_editor/js/languages/zh_cn.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/Olive/plugin/froala_editor/js/languages/ja.js"></script>



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
				<%-- 	<div class="breadcrumbs" id="breadcrumbs">
					<script type="text/javascript">
						try {
							ace.settings.check('breadcrumbs', 'fixed')
						} catch (e) {
						}
					</script>

					<ul class="breadcrumb">
						<li><a href="#" onclick="javascript:history.back(-1);">套装搭配</a></li>
						<li class="active"><c:if test="${pageType == 'add'}">
                            增加
                        </c:if> <c:if test="${pageType == 'edit'}">
                            编辑
                        </c:if></li>
					</ul>
					<!-- /.breadcrumb -->


				</div> --%>

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
														id="saveCollocatInfo" onclick="saveAllCollocat()">
														<i class="ace-icon fa fa-save"></i> 保存
													</button>
													<button class="btn btn-xs btn-light bigger"
														onclick="historyBack()">
														<i class="ace-icon fa fa-arrow-left"></i> 返回
													</button>
												</div>
											</div>

											<div class="widget-main" id="searchPanel">
												<form class="form-horizontal" role="form" id="editForm">
													<div class="form-group">


														<label
															class="col-xs-4 col-sm-4 col-md-1 control-label no-padding-right"
															for="form_id">套装编号</label>

														<div class="col-xs-8 col-sm-8 col-md-5">
															<input class="form-control" id="form_id" name="id"
																readonly type="text" value="${collocat.id}" />
														</div>
														<label
															class="col-xs-4 col-sm-4 col-md-1 control-label no-padding-right"
															for="form_styleIds">关联款号</label>

														<div class="col-xs-8 col-sm-8 col-md-5">
															<div class="input-group">
																<input class="form-control" id="form_styleIds"
																	type="text" name="styleIds" readonly
																	value="${collocat.styleIds}" /> <span
																	class="input-group-btn">
																	<button class="btn btn-sm btn-default" id="setstyle"
																		type="button"
																		onclick="open_multi_newProductDialog('#form_styleIds',changeids,initStyles)">
																		<i class="ace-icon fa fa-list"></i>
																	</button>
																</span>
															</div>
														</div>
													</div>
													<div class="form-group">
														<label
															class="col-xs-4 col-sm-4 col-md-1 control-label no-padding-right"
															for="form_isShow">是否展出</label>

														<div class="col-xs-8 col-sm-8 col-md-5">
															<select class="form-control" id="form_isShow"
																name="isShow">
																<option value="Y">展出</option>
																<option value="N">不展出</option>
															</select>
														</div>
														<label
															class="col-xs-4 col-sm-4 col-md-1 control-label no-padding-right"
															for="form_seqNo">序号</label>
														<div class="col-xs-8 col-sm-8 col-md-5">
															<input class="form-control" id="form_seqNo" name="seqNo"
																readonly type="text" value="${collocat.seqNo}" />
														</div>
													</div>
													<div class="form-group">
														<label
															class="col-xs-4 col-sm-4 col-md-1 control-label no-padding-right"
															for="form_price">价格</label>
														<div class="col-xs-8 col-sm-8 col-md-5">
															<input class="form-control" id="form_price" name="price"
																type="text" value="${collocat.price}" />
														</div>
													</div>

													<input type="text" id="form_remark" name="remark"
														style="display: none" value="${collocat.remark}" /> <input
														type="text" class="form-control" id="form_url" name="url"
														style="display: none" value="${collocat.url}" /> <br />

													<div class="form-group">
														<label class="col-sm-1 control-label no-padding-right"
															for="form_remark">备注</label>
														<div class="col-xs-11 col-sm-11">
															<div id="editor">${collocat.remark}</div>
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

												<div class="widget-toolbar no-border">
													<button class="btn btn-xs btn-light bigger" id="addPic"
														onclick="showuploadImg()">
														<i class="ace-icon fa fa-plus"></i> 添加图片
													</button>
													<button class="btn btn-xs btn-light bigger" id="backPic"
														onclick="hideuploadImg()">
														<i class="ace-icon fa fa-plus"></i> 取消
													</button>
												</div>
											</div>
											<div class="widget-body">
												<div class="widget-main padding-12">
													<div>
														<ul class="ace-thumbnails clearfix" id="gallery">
														</ul>
													</div>
													<div id="zyUpload" class="zyupload" style="display: none"></div>
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
				<script src="<%=basePath%>/Olive/plugin/zyupload/zyUpload.js"></script>
				<script src="<%=basePath%>/Olive/plugin/photo-gallery.js"></script>


				<jsp:include page="../base/newProduct_multi_dialog.jsp"></jsp:include>
</body>
<script>
	
		$(function() {
			initShow();
			inivaladitor();
			inifroala();
			initColImg();
			$("#backPic").hide();
			if("${collocat.styleIds}"!=""){
				$("#setstyle").removeAttr("onclick");
			}
			if("${pageType}"=="add"){
				$("#form_seqNo").val("${seqNo}");
			}
				
		});
		
		var saveflag=false;
		function initShow() {
			if ("${collocat.isShow}" != "")
				$("#form_isShow").find("option[value='${collocat.isShow}']")
						.attr("selected", true);
		}
		
		function initStyles(){}
		
		function reloadData(){
			if("${collocat.id}"!=null)
				{
					$("#form_id").val("${collocat.id}");
					$("#form_styleIds").val("${collocat.styleIds}");
					initShow();
					$("#form_seqNo").val("${collocat.seqNo}");
					$("#form_price").val("${collocat.price}")
					$("#editor").html("${collocat.remark}");
					initColImg();
				}
			
		}
		
	
		function initColImg(){
			if($("ul").has("li").length>0)
				clearImg();
	        var images=$("#form_url").val();
	        if(images!=""){
	            var n=images.split(',');
	            var ul=document.getElementById("gallery");
	            for(var i=0;i< n.length;i++){
	                var li=document.createElement("li");
	                var img=document.createElement("img");
	                ul.appendChild(li);
	                li.appendChild(img);
	                img.setAttribute("width","200px");
	                img.setAttribute("height","250px");
	                img.setAttribute("src","<%=basePath%>/mirror/collocat/"+n[i]);
	                var del=document.createElement("div");
	                li.appendChild(del);
	                del.setAttribute("class","imgdel");
	                del.setAttribute("onclick","delimg("+i+")");
	            }
	        }
		}

		function clearImg() {
			var ul = document.getElementById("gallery");
			for (var i = ul.childNodes.length - 1; i > 0; i--) {
				if (i == ul.childNodes.length - 1) {
					ul.removeChild(ul.childNodes[i]);
				}
			}
		}

		function delimg(i) {
			
			if($("ul > li").length<=1)
				{
					bootbox.alert("请至少保留一张图片");
					return
				}
			var id = $("#form_id").val();
			$.ajax({
				type : "POST",
				async:false,
				url : basePath + "/smart/collocat/delimg.do",
				data : {
					"id" : id,
					"no" : i
				},
				dataType : "json",
				success : function(result) {
					console.log(result);
					var msg=result.msg;
					$("#form_url").val(result.msg);
					initColImg();
				}
			});
		}
		
		function showuploadImg() {
			var id=$("#form_id").val();
			var price=$("#form_price").val();
			
			if(id&&price){}else{
				 	bootbox.alert("请先选择款号并填写价格！");
					return
			}
			if($("#zyUpload").html()==null||$("#zyUpload").html()==""){
				iniupload();
			}
			<%--初始化后禁用该控件 --%>
			$("#addPic").hide();
			$("#backPic").show();
			$("#zyUpload").show();
			$("#gallery").hide();

		}

		function hideuploadImg() {
			initColImg();
			$("#addPic").show();
			$("#backPic").hide();
			$("#zyUpload").hide();
			$("#gallery").show();
		}

		function inifroala() {
			$('#editor').froalaEditor({
				height : "100px",
				theme : "gray",
				language : "zh_cn"
			});
		}
		
		function iniupload() {
					$("#zyUpload").zyUpload(
							{
								width : "650px", // 宽度
								height : "400px", // 高度
								itemWidth : "140px", // 文件项的宽度
								itemHeight : "115px", // 文件项的高度
								url : $("#zyUpload").url = basePath+ "/smart/collocat/saveColImages.do?id="
										+ $("#form_id").val(), // 上传文件的路径
								fileType : [ "jpg", "png", "jpeg" ],// 上传文件的类型
								fileSize : 51200000, //上传文件的大小
								multiple : true, // 是否可以多个文件上传
								dragDrop : true, //是否可以拖动文件上传
								tailor : true, // 是否可以裁剪图片
								del : true, // 是否可以删除图片
								finishDel : false, // 是否在文件上传完成后删除预览
		
								/* 外部获得的回调接口 */
								onSelect : function(selectFiles, allFiles) { // 选择文件的回调方法，select File:当前选中的文件，All File：还没上传的全部文件
									console.info("当前选择了以下文件");
									console.info(selectFiles);
								},
								onDelete : function(file, files) { // 删除一个文件的回调方法 file：当前删除的文件 files：删除之后的文件
									console.info("当前删除了此文件");
									console.info(file.name);
								},
								onSuccess : function(file, response) { // 文件上传成功的回调方法
									console.info("此文件上传成功");
									console.info(file.name);
									console.info("此文件上传到服务器地址：");
									console.info(response);
									var obj = eval('(' + response + ')');
									$("#uploadInf").append(
											"<p>上传成功，文件地址是：" + obj.result + "</p>");
									var imgs = $("#form_url").val();
									var sun = obj.msg;
									/* var cut = sun.substr(sun
											.lastIndexOf("/collocat/")+10); */
									if (imgs != "" && imgs != null) {
										imgs += "," + sun;
									} else {
										imgs += sun;
									}
									$("#form_url").val(imgs);
									saveflag=true;
								//	reloadData();
									saveAllCollocat();
									
								//	reloadData();
								/*	hideuploadImg(); */
								},
								onFailure : function(file, response) { // 文件上传失败的回调方法
									console.info("此文件上传失败：");
									console.info(file.name);
								},
								onComplete : function(response) { // 上传完成的回调方法
									console.info("文件上传完成");
									console.info(response);
								}
							});
					$(".upload_preview").css("float", "left");
					$(".upload_preview").css("width", "648px");
				
		}

		function changeids() {
				var reg = /\,/g;
				var translate = $("#form_styleIds").val().replace(reg, '-');
				$("#form_id").val(translate);
		}

	function historyBack(){
		location.href=basePath+"/smart/collocat/index.do";
	}
	
		function saveAllCollocat() {
			$("#form_remark").val($('#editor').froalaEditor('html.get'));
			$('#editForm').data('bootstrapValidator').validate();
		        if(!$('#editForm').data('bootstrapValidator').isValid()){
		            return ;
		        }
		    if($("#form_url").val()==null||$("#form_url").val()=="")
		    	{
		    		bootbox.alert("请至少添加一张图片");
		    		return
		    	}
			cs.showProgressBar();
			$.post(basePath + "/smart/collocat/save.do", $("#editForm")
					.serialize(), function(result) {
				cs.closeProgressBar();
				if (result.success == true || result.success == 'true') {
					$.gritter.add({
						text : result.msg,
						class_name : 'gritter-success  gritter-light'
					});
					if("${collocat.id}"!=""){
						window.location.reload();
					}
					else{
						if(saveflag)
							{
								saveflag=false;
								var id=$("#form_id").val();
								location.href =basePath+"/smart/collocat/edit.do?id="+$("#form_id").val();
							}
						else{
							location.href=basePath+"/smart/collocat/index.do";
						}
					}
				} else {
					cs.showAlertMsgBox(result.msg);
					history.back(-1);
				}
			}, 'json');
		}
		function inivaladitor() {
			$("#editForm")
					.bootstrapValidator(
							{
								message : '输入值无效',
								feedbackIcons : {
									valid : 'glyphicon glyphicon-ok',
									invalid : 'glyphicon glyphicon-remove',
									validating : 'glyphicon glyphicon-refresh'
								},
								fields : {
									 styleIds : {
										validators : {
											notEmpty : {//非空验证：提示消息
												message : '关联款号不能为空'
											}
										}
									},
									price:{
										validators:{
											notEmpty : {//非空验证：提示消息
												message : '价格不能为空'
											},
											regexp:{
												regexp:/^[1-9](\d){0,9}(.)?(\d){0,3}$/,
												message:'请规范填写价格'		
											    }
									    }
									},
									
								}
							});
		}
	</script>

</html>