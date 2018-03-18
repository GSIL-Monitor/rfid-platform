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
                <label class="col-xs-1 control-label" for="form_showArea">显示区域</label>
                <div class="col-xs-2">
                    <select class="form-control" id="form_showArea" name="showArea">
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                    </select>
                </div>

                <label class="col-xs-1 control-label" for="form_isShow">是否展示</label>
                <div class="col-xs-2">
                    <select class="chosen-select form-control" id="form_isShow" name="isShow">
                        <option value="Y">Y</option>
                        <option value="N">N</option>
                    </select>

                </div>

            </div>
                <div class="form-group">


                 <%--<label class="col-xs-1 control-label" for="form_url">URL</label>--%>
                 <div class="col-xs-2">
                     <input class="form-control" id="form_url" name="url" style="display: none"
                            type="text"  />
                 </div>


                    <div class="col-xs-2" style="display: none">
                        <input class="form-control" id="form_remark" name="remark"
                               type="text"  />
                    </div>
                    <!--HTML5文件上传-->
                    <%--<div class="col-xs-5">--%>
                            <%--<input type="file" id="fileUpload" required="true" accept="video/mp4">--%>
                    <%--</div>--%>
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
                    <h5 class="widget-title">视频预览</h5>

                <div class="widget-toolbar no-border">
                    <button class="btn btn-xs btn-light bigger" id="btnshow" onclick="openUpload()">
                        <i class="ace-icon fa fa-plus"></i>
                        添加视频
                    </button>
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
<jsp:include page="../base/style_dialog.jsp"></jsp:include>
<script type="text/javascript">

    $(function(){
        initEdit();
        inifroala();
        initMedia();
        initEditFormValid();

    });

    //初始化验证
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
                name: {
                    validators: {
                        notEmpty: {
                            message: '名称不能为空'
                        }
                    }
                }

            }
        });
    }
     //编辑时初始化数据
    function initEdit(){
        if("${mediaInfo}"!=""){
            $("#form_id").val("${mediaInfo.id}");
            $("#form_id").attr("readonly",true);
            $("#form_name").val("${mediaInfo.name}");
            $("#form_showArea").find("option[value='${mediaInfo.showArea}']").attr("selected",true);
            $("#form_seqNo").val("${mediaInfo.seqNo}");
            $("#form_isShow").find("option[value='${mediaInfo.isShow}']").attr("selected",true);
            $("#form_url").val("${mediaInfo.url }");
            $("#editor").html("${mediaInfo.remark}");
        }
    }

    //初始化ace-admin文件上传控件
    function initUploadFile(){
        $('#fileUpload').ace_file_input({
            style: 'well',
            btn_choose: '点击选择视频或将视频拖到此处',
            btn_change: null,
            no_icon: 'ace-icon fa fa-cloud-upload',
            droppable: true,
            thumbnail: 'small'//large | fit
            //,icon_remove:null//set null, to hide remove/reset button
            /**,before_change:function(files, dropped) {
						//Check an example below
						//or examples/file-upload.html
						return true;
					}*/
            /**,before_remove : function() {
						return true;
					}*/
            ,
            preview_error : function(filename, error_code) {
                //name of the file that failed
                //error_code values
                //1 = 'FILE_LOAD_FAILED',
                //2 = 'IMAGE_LOAD_FAILED',
                //3 = 'THUMBNAIL_FAILED'
                //alert(error_code);
            }

        }).on('change', function(){
            //console.log($(this).data('ace_input_files'));
            //console.log($(this).data('ace_input_method'));
        });
    }


    //动态显示video标签
    function initMedia(){
        var url=$("#form_url").val();
        var html="";
        html+="<video width='600' height='450' id='video' controls>";
        html+="<source src='<%=basePath%>/mirror/media/"+url+"'"+"type='video/mp4'/>";
        html+="</video>";
        $("#showMediaArea").append(html);
    }

    //初始化文本编辑控件
    function inifroala() {
        $('#editor').froalaEditor({
            height : "100px",
            theme : "gray",
            language : "zh_cn"
        });
    }

    //上传视频文件
    function saveVideo(){
       if("${mediaInfo}"==""){
           save();
       }
        $('#editForm').data('bootstrapValidator').validate();
        if(!$('#editForm').data('bootstrapValidator').isValid()){
            return ;
        }
        var file = $("#fileUpload").data('ace_input_files')[0];
        if(file == null){
            bootbox.alert("请选择上传的视频");
            return;
        }
        var progressDialog = bootbox.dialog({
            message: '<p><i class="fa fa-spin fa-spinner"></i> 视频上传中...</p>'
        });
        var fd = new FormData();
        fd.append("file", file);
        fd.append("id", $("#form_id").val());
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange=function(){
            if(xhr.readyState==4){
                if(xhr.status==200){
                    $("#form_id").attr("readonly",true);//禁止修改ID
                    deleteVideo($("#form_url").val());//删除原视频
                    var message=xhr.responseText;
                    var obj=eval("("+message+")");
                    $("#form_url").val(obj.msg);//设置url
                    progressDialog.modal('hide');
                    bootbox.alert("视频上传完成");
                }else{
                    progressDialog.modal('hide');
                    var fail="视频上传失败";
                    bootbox.alert(fail);

                }
            }
        }
        xhr.open("POST", basePath + "/smart/media/saveVideo.do",true);
        xhr.send(fd);
    }

    //删除视频
    function deleteVideo(url){
        $.ajax({
            url:basePath+"/smart/media/deleteVideo.do?url="+url,
            type:'post',
            dataType : 'json',
            success:function(result){
                if(result.success == true || result.success == 'true') {
                      console.log("删除视频:"+url);
                }else{
                    bootbox.alert("保存失败");
                }
            }
        });
    }

    function openUpload(){
        $("#video").remove();//移除video
        //加载文件上传控件
        var html="";
        html+="<div class='col-xs-4 col-xs-offset-4' id='uploadArea' style='margin-top: 50px'>";
        html+='<input type="file" id="fileUpload" required="true" accept="video/mp4">';
        html+='<button type="button" class="btn btn-info btn-block" onclick="saveVideo();">';
        html+='上传';
        html+='</bntton>'
        html+='</div>';

        $("#showMediaArea").append(html);
        initUploadFile();
        $("#btnshow").hide();
        $("#btnhide").show();
    }

    function cancel(){
        $("#uploadArea").remove();//移除fileupload
        initMedia();
        $("#btnshow").show();
        $("#btnhide").hide();

    }

    //保存信息
    function save(){
        $('#editForm').data('bootstrapValidator').validate();
        if(!$('#editForm').data('bootstrapValidator').isValid()){
            return ;
        }

        var progressDialog = bootbox.dialog({
            message: '<p><i class="fa fa-spin fa-spinner"></i> 数据上传中...</p>'
        });
        $("#form_remark").val($('#editor').froalaEditor('html.get'));
        var formData=  $("#editForm").serialize();
        $.ajax({
            url:basePath+"/smart/media/save.do",
            type:'post',
            data : formData,
            dataType : 'json',
            success:function(result){
                if(result.success == true || result.success == 'true') {
                    $("#form_id").attr("readonly",true);//禁止修改ID

                    progressDialog.modal('hide');
                }else{
                    bootbox.alert("保存失败");
                }
            }
        });

    }

</script>
</html>