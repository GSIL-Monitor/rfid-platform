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
        #thumbs {
            float: none;
            display: block;
            margin-left: auto;
            margin-right: auto;
        }
        .no-skin{
            background: #fff;
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
	    <link rel="stylesheet" href="<%=basePath%>/Olive/plugin/img-show/css/base.css"/>
    <link rel="stylesheet" href="<%=basePath%>/Olive/plugin/img-show/css/styles.css"/>
    <link rel="stylesheet" href="<%=basePath%>/Olive/plugin/img-show/touchTouch/touchTouch.css"/>

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
				<div id="page-content">
						<div class="widget-body">
							<div class="main-content">
								<div class="col-xs-12">
									<div class="widget-box widget-color-blue  light-border">
										<div class="widget-header">
											<h5 class="widget-title">基本信息</h5>
											<div class="widget-toolbar no-border">
												<button class="btn btn-xs btn-light bigger"
													id="saveStarInfo" onclick="saveStar()">
													<i class="ace-icon fa fa-save"></i> 保存
												</button>
												<button class="btn btn-xs btn-light bigger" id="historyback"
													onclick="historyBack()">
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
														<input name="id" id="form_id" class="form-control"
															type="text" onkeyup="this.value=this.value.toUpperCase()" value="${starInfo.id}" />
													</div>
												</div>
												<div class="form-group">
													<label
														class="col-xs-4 col-sm-1 col-xs-offset-3 control-label padding-right"
														for="form_starName">名称</label>

													<div class="col-xs-8 col-sm-5">
														<input class="form-control" id="form_starName"
															type="text" name="starName" value="${starInfo.starName}" />
															<input id="form_seqNo" name="seqNo" class="form-control" value="${starInfo.seqNo}" style="display:none"/>
													</div>
												</div>
												<div class="form-group">
													<label
														class="com-xs-4 col-sm-1 col-xs-offset-3 control-label padding-right"
														for="form_isShow">是否展示</label>
													<div class="col-xs-8 col-sm-5">
														<select class="form-control" id="form_isShow"
															name="isShow">
															<option value="Y">展示</option>
															<option value="N">不展示</option>
														</select>
													</div>
												</div>
												<div class="form-group">
													<div
														class="col-xs-5 col-sm-5 col-xs-offset-4 col-sm-offset-4" hidden>
														<input id="form_url" name="url" class="form-control" value="${starInfo.url}"/>
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
									<button class="btn btn-xs btn-light bigger" id="btnCancel"
										onclick="hideUpload()" hidden>
										<i class="ace-icon fa fa-minus"></i>取消
									</button>
									<button class="btn btn-xs btn-light bigger" id="btnAdd"
										onclick="showUpload()">
										<i class="ace-icon fa fa-plus"></i> 添加图片
									</button>
								</div>
							</div>
							<div class="widget-body">
								<div class="widget-main">
									<div class="clearfix img-gather" id="thumbs"></div>
									<div id="zyUpload" class="zyupload" style="display: none"></div>
								</div>
							</div>
						</div>
					</div>
		</div>
		<jsp:include page="../layout/footer.jsp"></jsp:include>
	</div>
	<jsp:include page="../layout/footer_js.jsp"></jsp:include>
	<script src="<%=basePath%>/Olive/plugin/zyupload/zyUpload.js"></script>
	<script src="<%=basePath%>/Olive/plugin/photo-gallery.js"></script>
	<script src="<%=basePath%>/Olive/plugin/img-show/touchTouch/touchTouch.jquery.js"></script>
	<script>
$(function(){
	initShow();
//	initStarImg();
	initstarInfoPic();
//	iniupload();
	hideUpload();
	inivaladitor();
	if("${pageType}"=="add"){
		$("#form_id").attr("readonly",false);
		$("#form_starName").attr("readonly",false);
		$("#form_seqNo").val("${seqNo}");
	}
	if("${pageType}"=="edit")
	{
 		$("#form_id").attr("readonly",true);
		$("#form_starName").attr("readonly",true);
		html = '<i class="ace-icon fa fa-edit"></i>修改图片';
		$("#btnAdd").html(html);
	}

});
function historyBack(){
	location.href=basePath+"/smart/star/index.do";
}
function initShow() {
	if ("${starInfo.isShow}" != "")
		$("#form_isShow").find("option[value='${starInfo.isShow}']")
				.attr("selected", true);
}
<%--多图片初始化--%>
function initStarImg(){
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
            img.setAttribute("src","<%=basePath%>/mirror/star/"+n[i]);
            var del=document.createElement("div");
            li.appendChild(del);
            del.setAttribute("class","imgdel");
            del.setAttribute("onclick","delimg("+i+")");
        }
    }
}
<%--单图片初始化--%>
function initstarInfoPic() {
    var images = $("#form_url").val();
    if (images != "") {
        var n = images.split(',');
        var html = '';
        for (var i = 0; i < n.length; i++) {
            html += '<a href="<%=basePath%>/mirror/starInfo/' + n[i] + '" style="background-image:url(<%=basePath%>/mirror/starInfo/' + n[i] + ')"></a>';
        }
        $("#thumbs").append(html);
    }
    $("#thumbs a").touchTouch();
}
function hideUpload(){
	$("#btnCancel").hide();
	$("#btnAdd").show();
	$("#zyUpload").hide();
//	$("#gallery").show();
	$("#thumbs").show();
}

function showUpload(){
	$('#editform').data('bootstrapValidator').validate();
	if(!$("#editform").data("bootstrapValidator").isValid())
		{
			bootbox.alert("请先确保信息填写完整准确");
			return
		}
	 var content = $("#zyUpload").html();
     if (content == null || content.length == 0) {
    	 iniupload();
     }
	$("#btnCancel").show();
	$("#btnAdd").hide();
//	$("#gallery").hide();
	$("#zyUpload").show();
	$("#thumbs").hide();
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
		url : basePath + "/smart/star/delimg.do",
		data : {
			"id" : id,
			"no" : i
		},
		dataType : "json",
		success : function(result) {
			console.log(result);
			var msg=result.msg;
			$("#form_url").val(result.msg);
			initstarInfoPic();
		}
	});
}


var saveflag=false;
function saveStar(){
	$('#editform').data('bootstrapValidator').validate();
	if(!$("#editform").data("bootstrapValidator").isValid())
		{	
			return
		}
	if($("#form_url").val()==null||$("#form_url").val()=="") {
		bootbox.alert("请至少上传一张图片");
		return
	}
	cs.showProgressBar();
	$.post(basePath + "/smart/star/save.do", $("#editform")
			.serialize(), function(result) {
		cs.closeProgressBar();
		if (result.success == true || result.success == 'true') {
			$.gritter.add({
				text : result.msg,
				class_name : 'gritter-success  gritter-light'
			});
			if("${starInfo.id}"!=""){
				window.location.reload();
			}
			else{
				if(saveflag)
					{
						saveflag=false;
						var starInfo_id = $("#form_id").val();
						location.href=basePath +"/smart/star/edit.do?id="+starInfo_id;
					}
				else{
					location.href=basePath+"/smart/star/index.do";
				}
			}
		} else {
			cs.showAlertMsgBox(result.msg);
			history.back(-1);
		}
	}, 'json');
}

function inivaladitor() {
	$("#editform")
			.bootstrapValidator(
					{
						message : '输入值无效',
						feedbackIcons : {
							valid : 'glyphicon glyphicon-ok',
							invalid : 'glyphicon glyphicon-remove',
							validating : 'glyphicon glyphicon-refresh'
						},
						submitHandler : function(validator, form,
								submitButton) {
							$.post(form.attr('action'), form
									.serialize(), function(result) {
								if (result.success == true
										|| result.success == 'true') {

								} else {

									// Enable the submit buttons	
									$('#editform').bootstrapValidator(
											'disableSubmitButtons',
											false);
								}
							}, 'json');
						},
						fields : {
							 id : {
								validators : {
									notEmpty : {//非空验证：提示消息
										message : '编号不能为空'
									}
								}
							},
							starName:{
								validators : {
									notEmpty : {//非空验证：提示消息
										message : '名称不能为空'
									},
							    }
							},
							url:{
								validators:{
									notEmpty:{
										message:'请至少上传一张图片'
									}
								}
							}
						}
					});
}

function iniupload() {
	$("#zyUpload").zyUpload(
			{
				width : "650px", // 宽度
				height : "400px", // 高度
				itemWidth : "140px", // 文件项的宽度
				itemHeight : "115px", // 文件项的高度
				url : $("#zyUpload").url = basePath
						+ "/smart/star/savestarImages.do?id="
						+ $("#form_id").val(), // 上传文件的路径
				fileType : [ "jpg", "png", "jpeg" ],// 上传文件的类型
				fileSize : 51200000, //上传文件的大小
				multiple : false, // 是否可以多个文件上传
				dragDrop : false, //是否可以拖动文件上传
				tailor : true, // 是否可以裁剪图片
				del : true, // 是否可以删除图片
				finishDel : false, // 是否在文件上传完成后删除预览

				/* 外部获得的回调接口 */
				onSelect : function(selectFiles, allFiles) { // 选择文件的回调方法，select File:当前选中的文件，All File：还没上传的全部文件
					$("#rapidAddImg").hide();
                    $(".webuploader_pick").hide();
				},
				onDelete : function(file, files) { // 删除一个文件的回调方法 file：当前删除的文件 files：删除之后的文件
					$("#rapidAddImg").show();
                    $(".webuploader_pick").show();
				},
				onSuccess : function(file, response) { // 文件上传成功的回调方法
					console.info("此文件上传成功");
					console.info(file.name);
					console.info("此文件上传到服务器地址：");
					console.info(response);
					var obj = eval('(' + response + ')');
					$("#uploadInf").append(
							"<p>上传成功，文件地址是：" + obj.result + "</p>");
					saveflag =true;
					var sun = obj.result;
					var cut=sun.substr(sun
                            .lastIndexOf("/mirror/starInfo/")+17);
					$("#form_url").val(cut);
					saveStar();
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
</script>
</body>
</html>