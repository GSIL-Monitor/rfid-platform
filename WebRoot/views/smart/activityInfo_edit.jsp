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
    <link href='<%=basePath%>/Olive/plugin/froala_editor/css/froala_editor.min.css' rel='stylesheet' type='text/css' />
    <link href="<%=basePath%>/Olive/plugin/froala_editor/css/themes/gray.min.css" rel="stylesheet" type="text/css" />
    <script src="<%=basePath%>/Olive/plugin/froala_editor/js/jquery.min.js"></script>
    <script src="<%=basePath%>/Olive/plugin/froala_editor/js/plugins/char_counter.min.js"></script>
    <script src="<%=basePath%>/Olive/plugin/froala_editor/js/froala_editor.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/Olive/plugin/froala_editor/js/languages/zh_cn.js"></script>
    <script type="text/javascript" src="<%=basePath%>/Olive/plugin/froala_editor/js/languages/ja.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/Olive/plugin/zyupload/skins/zyupload-1.0.0.min.css"/>
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
                    <button class="btn btn-xs btn-light bigger" onclick="saveActivityInfo()">
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
            <form class="form-horizontal" role="form" id="editForm">
            <div class="form-group">

                <label class="col-xs-1 control-label no-padding-right" for="form_Id">编号</label>
                <div class="col-xs-2">
                    <input class="form-control" id="form_Id" name="Id" onkeyup="this.value=this.value.toUpperCase()"
                           type="text" />
                </div>

                <label class="col-xs-1 control-label" for="form_activityTime">活动时间</label>
                <div class="col-xs-2">
                        <input class="form-control date-picker" id="form_activityTime"
                               type="text" name="activityTime"
                               data-date-format="yyyy-mm-dd"/>
                </div>
                <label class="col-xs-1 control-label" for="form_isShow">是否展示</label>
                <div class="col-xs-2">
                    <select class="form-control" id="form_isShow" name="isShow">
                        <option value="Y">Y</option>
                        <option value="N">N</option>
                    </select>
                </div>

                <div class="col-xs-2" style="display: none">
                    <input class="form-control" id="form_remark" name="remark" type="text"/>
                </div>

                <div class="col-xs-2"  style="display: none">
                    <input class="form-control" id="form_url" name="url" type="text"  />
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
                    <h5 class="widget-title">图片</h5>

                <div class="widget-toolbar no-border">
                <button class="btn btn-xs btn-light bigger" id="btnshow" onclick="showUploadImg()">
                    <i class="ace-icon fa fa-plus"></i>
                    添加图片
                </button>
                    <button class="btn btn-xs btn-light bigger" id="btnhide" onclick="cancel()" style="display:none">
                        <i class="ace-icon fa fa-mail-reply"></i>
                        取消
                    </button>
                </div>
                </div>
                <div class="widget-body">
                    <div class="widget-main padding-12" id="uploadarea">
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
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/zyupload/zyUpload.js"></script>
<script type="text/javascript">

    $(function(){
        //编辑时styleId不可修改
        if("${activity}"!=""){

        }
        initEdit();
        inifroala();
        initEditFormValid();
    });
    //初始化文本编辑控件
    function inifroala() {
        $('#editor').froalaEditor({
            height : "100px",
            theme : "gray",
            language : "zh_cn"
        });
    }
    function initEdit(){
        if("${activityInfo}"!=""){
            $("#form_Id").val("${activityInfo.id}");
            $("#form_activityTime").val("${activityInfo.activityTime}");
            $("#editor").html("${activityInfo.remark}");
            var url="${activityInfo.url}";
            $("#form_url").val(url);
            initImg(url);
        }
    }

    function clearImg(){
        var ul=document.getElementById("gallery");
        for (var i=ul.childNodes.length-1;i>0;i--)
        {
            if (i==ul.childNodes.length-1)
            {
                ul.removeChild(ul.childNodes[i]);
            }
        }
    }

    function initImg(images){
        //逗号分割图片名称并加载
        if(images!=""&&images!=null){
            var n=images.split(',');
            var ul=document.getElementById("gallery");
            for(var i=0;i< n.length;i++){
                var li=document.createElement("li");
                var img=document.createElement("img");
                ul.appendChild(li);
                li.appendChild(img);
                img.setAttribute("width","200px");
                img.setAttribute("height","250px");
                img.setAttribute("src","<%=basePath%>/mirror/activity/"+n[i]);
                var del=document.createElement("div");
                li.appendChild(del);
                del.setAttribute("class","imgdel");
                del.setAttribute("onclick","delimg("+i+")");
            }
        }

    }

    function delimg(i){
        var Id=$("#form_Id").val();
        $.ajax({
            type:"POST",
            async:false,
            url:basePath+"/smart/activity/deleteImg.do",
            data: {"Id":Id,"no":i},
            dataType:"json",
            success:function(result){
                clearImg();
                initImg(result.msg);
                $("#form_url").val(result.msg);
            }
        });
    }


    function intiImagesArea(){
        $(".upload_preview").css("width","695px");
    }

    function initZyupload(){
        var uploadarea=document.getElementById("uploadarea");
        var zyupload=document.createElement("div");
        zyupload.setAttribute("id","zyupload");
        zyupload.setAttribute("class","zyupload");
        uploadarea.appendChild(zyupload);
        $("#zyupload").zyUpload({
            width            :   "700px",                 // 宽度
            height           :   "400px",                 // 高度
            itemWidth        :   "140px",                 // 文件项的宽度
            itemHeight       :   "140px",                 // 文件项的高度
            url              :   $("#zyupload").url= basePath+"/smart/activity/saveImages.do?id="+$("#form_Id").val(),  // 上传文件的路径
            fileType         :   ["jpg","jpeg","bmp","png"],// 上传文件的类型
            fileSize         :   51200000,                // 上传文件的大小
            multiple         :   false,                    // 是否可以多个文件上传
            dragDrop         :   false,                    // 是否可以拖动上传文件
            tailor           :   true,                    // 是否可以裁剪图片
            del              :   true,                    // 是否可以删除文件
            finishDel        :   false,  				  // 是否在上传文件完成后删除预览
            /* 外部获得的回调接口 */
            onSelect: function(selectFiles, allFiles){    // 选择文件的回调方法  selectFile:当前选中的文件  allFiles:还没上传的全部文件
                $("#rapidAddImg").hide();
                $(".webuploader_pick").hide();

            },
            onDelete: function(file, files){              // 删除一个文件的回调方法 file:当前删除的文件  files:删除之后的文件
                $("#rapidAddImg").show();
                $(".webuploader_pick").show();
            },
            onSuccess: function(file, response){          // 文件上传成功的回调方法
                console.info("此文件上传成功：");
                console.info(file.name);
                console.info("此文件上传到服务器地址：");
                console.info(response);
                var obj=eval("("+response+")");
                $("#uploadInf").append("<p>上传成功，文件地址是：" + obj.result+ "</p>");
                var imgs=$("#form_url").val();
                if(imgs){
                    delimg(0);
                }
                $("#form_url").val(obj.msg);
                initImg($("#form_url").val());
                saveActivityInfo();

            },
            onFailure: function(file, response){          // 文件上传失败的回调方法
                console.info("此文件上传失败：");
                console.info(file.name);
                bootbox.alert("上传失败");
            },
            onComplete: function(response){           	  // 上传完成的回调方法
                console.info("文件上传完成");
                console.info(response);

            }
        });
    }

    function deleteZyupload(){
        $("#zyupload").remove();
    }


    function showUploadImg(){
        var Id=$("#form_Id").val();
        if(Id){
        }else{
            bootbox.alert("请选择款号");
            return;
        }
        $("#zyupload").show();
        $("#gallery").hide();
        // 初始化插件
        initZyupload();
        intiImagesArea();
        $("#btnshow").hide();
        $("#btnhide").show();
    }
    function hideUploadImg(){
        $("#zyupload").remove();
        $("#gallery").show();
    }

    function cancel(){
        hideUploadImg();
        $("#btnshow").show();
        $("#btnhide").hide();
    }

    function initEditFormValid() {
        $('#editForm').bootstrapValidator({
            message: '输入值无效',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                id: {
                    validators: {
                        notEmpty: {
                            message: '编号不能为空'
                        }
                    }
                },
                activityTime: {
                    validators: {
                        notEmpty: {
                            message: '时间不能为空'
                        }
                    }
                }
            }
        });
    }

    //保存到数据库
    function saveActivityInfo(){
        //调用bootstrap验证
        $('#editForm').data('bootstrapValidator').validate();
        if(!$('#editForm').data('bootstrapValidator').isValid()){
            return ;
        }
        //进度条
        var progressDialog = bootbox.dialog({
            message: '<p><i class="fa fa-spin fa-spinner"></i> 数据上传中...</p>'
        });
        //获取备注编辑器文本
        var remark=$('#editor').froalaEditor('html.get');
        $("#form_remark").val(remark);

        var formData=  $("#editForm").serialize();
        $.ajax({
            url:basePath+"/smart/activity/save.do",
            type:'post',
            data : formData,
            dataType : 'json',
            success:function(result){
                if(result.success == true || result.success == 'true') {
                    progressDialog.modal('hide');

                }else{
                    bootbox.alert("保存失败");
                }
            }
        })

    }


</script>
</html>