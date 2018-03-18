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
    <link href="<%=basePath%>/Olive/plugin/zyupload/skins/zyupload-1.0.0.min.css" rel="stylesheet"/>
    <link href='<%=basePath%>/Olive/plugin/froala_editor/css/froala_editor.min.css' rel='stylesheet' type='text/css'/>
    <link href='<%=basePath%>/Olive/plugin/froala_editor/css/froala_style.min.css' rel='stylesheet' type='text/css'/>
    <link href="<%=basePath%>/Olive/plugin/froala_editor/css/themes/gray.min.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath%>/Olive/plugin/img-show/css/base.css"/>
    <link rel="stylesheet" href="<%=basePath%>/Olive/plugin/img-show/css/styles.css"/>
    <link rel="stylesheet" href="<%=basePath%>/Olive/plugin/img-show/touchTouch/touchTouch.css"/>
    <script src="<%=basePath%>/Olive/plugin/froala_editor/js/jquery.min.js"></script>
    <script src="<%=basePath%>/Olive/plugin/froala_editor/js/froala_editor.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/Olive/plugin/froala_editor/js/languages/zh_cn.js"></script>
    <script type="text/javascript" src="<%=basePath%>/Olive/plugin/froala_editor/js/languages/ja.js"></script>
    <style>
        #thumbs {
            float: none;
            display: block;
            margin-left: auto;
            margin-right: auto;
        }
        .no-skin{
            background: #fff;
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
                                            id="saveCollocatInfo" onclick="saveBrand()">
                                        <i class="ace-icon fa fa-save"></i> 保存
                                    </button>
                                    <button class="btn btn-xs btn-light bigger"
                                            onclick="historyBack()">
                                        <i class="ace-icon fa fa-arrow-left"></i> 返回
                                    </button>
                                </div>
                            </div>
                            <div class="widget-body">
                            <div class="widget-main" id="searchPannel">
                                <form class="form-horizontal" id="editForm" role="form">
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-1 control-label padding-right"
                                               for="form_brand">编号</label>

                                        <div class="col-xs-8 col-sm-5">
                                            <input name="brand" id="form_brand" class="form-control" onkeyup="this.value=this.value.toUpperCase()"
                                                   type="text" value="${brandInfo.brand}"/>
                                        </div>

                                        <label class="col-xs-4 col-sm-1 control-label padding-right"
                                               for="form_name">名称</label>

                                        <div class="col-xs-8 col-sm-5">
                                            <input name="name" id="form_name" class="form-control"
                                                   type="text" value="${brandInfo.name}"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-1 control-label padding-right"
                                               for="form_address">归属地</label>

                                        <div class="col-xs-8 col-sm-5">
                                            <input name="address" id="form_address" class="form-control"
                                                   type="text" value="${brandInfo.address}"/>
                                        </div>

                                        <label class="col-xs-4 col-sm-1 control-label padding-right"
                                               for="form_designer">设计师</label>

                                        <div class="col-xs-8 col-sm-5">
                                            <input name="designer" id="form_designer"
                                                   class="form-control" type="text"
                                                   value="${brandInfo.designer}"/>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-xs-1 col-sm-1 control-label padding-right"
                                               for="form_remark">备注</label>

                                        <div class="col-xs-11 col-sm-11">
                                            <div id="editor">${brandInfo.remark}</div>
                                        </div>
                                    </div>
                                    <div class="form-group" hidden>
                                        <div
                                                class="col-xs-5 col-sm-5 col-xs-offset-1 col-sm-offset-1">
                                            <input id="form_remark" class="form-control" name="remark"
                                                   value="${brandInfo.remark}"/>
                                        </div>
                                        <div
                                                class="col-xs-5 col-sm-5 col-xs-offset-1 col-sm-offset-1">
                                            <input id="form_url" name="url" class="form-control"
                                                   value="${brandInfo.url}"/>
                                            <input id="form_seqNo" name="seqNo" class="form-control"
                                                   value="${brandInfo.seqNo}"/>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            </div>
                        </div>
        </div>
        <div class="col-xs-12">
            <div class="widget-Box widget-color-blue light-border">
                <div class="widget-header">
                    <h5 class="widget-title">图片</h5>

                    <div class="widget-toolbar no-border">
                        <button class="btn btn-xs btn-light bigger" id="btnCancel" onclick="hideUploadImg()" hidden>
                            <i class="ace-icon fa fa-minus"></i>取消
                        </button>
                        <button class="btn btn-xs btn-light bigger" id="btnAdd" onclick="showUploadImg()">
                            <i class="ace-icon fa fa-plus"></i>添加图片
                        </button>
                    </div>
                </div>
                <div class="widget-body">
                    <div class="widget-main padding-12">
                        <div class="clearfix img-gather" id="thumbs"></div>
                        <div id="zyUpload" class="zyupload"></div>
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
    $(function () {
        initEditor();
//				initBrandImg();
//				initrBandPic();
        hideUploadImg();
        inivaladitor();
        if ("${brandInfo.seqNo}" == "") {
            $("#form_seqNo").val("${seqNo}");
        }
        if ("${pageType}" == "edit") {
            $("#form_brand").attr("radonly", true);

            html = '<i class="ace-icon fa fa-edit"></i>修改图片'
            $("#btnAdd").html(html);
        }
    });

    function hideUploadImg() {
//				initBrandImg();
        reloadImg();
        $("#btnAdd").show();
        $("#btnCancel").hide();
        $("#zyUpload").hide();
        $("#thumbs").show();
        if ("${pageType}" == "edit") {
            $("#form_brand").attr("readonly", "readonly");
        }
    }

    function reloadImg() {
        var contents = $("#thumbs").html();
        if (contents = null || contents.length == 0) {
            initrBandPic();
        }
    }
    ;
    function showUploadImg() {
        $('#editForm').data('bootstrapValidator').validate();
        if (!$("#editForm").data("bootstrapValidator").isValid()) {
            bootbox.alert("请先确保信息填写完整准确");
            return
        }
        var content = $("#zyUpload").html();
        if (content == null || content.length == 0) {
            iniupload();
        }
        $("#btnCancel").show();
        $("#btnAdd").hide();
        $("#zyUpload").show();
        $("#thumbs").hide();

    }
function historyBack(){
	location.href = basePath + "/smart/brand/index.do";
}

    var saveflag = false;
    function saveBrand() {
        $('#editForm').data('bootstrapValidator').validate();
        if (!$("#editForm").data("bootstrapValidator").isValid()) {
            return
        }
        if ($("#form_url").val() == "" || $("#form_url").val() == null) {
            bootbox.alert("请至少添加一张图片");
            return

        }
        cs.showProgressBar();
        var content = $("#editor").froalaEditor('html.get');
        $("#form_remark").val(content);
        $.post(basePath + "/smart/brand/save.do", $("#editForm")
                .serialize(), function (result) {
            cs.closeProgressBar();
            if (result.success == true || result.success == 'true') {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                if ("${brandInfo.brand}" != "") {
                    window.location.reload();
                }
                else {
                    if (saveflag) {
                        saveflag = false;
                        var brand = $("#form_brand").val();
                        location.href = basePath + "/smart/brand/edit.do?brand=" + brand;
                    }
                    else {
                        location.href = basePath + "/smart/brand/index.do";
                    }
                }
            } else {
                cs.showAlertMsgBox(result.msg);
                history.back(-1);
            }
        }, 'json');
    }

    function delimg(i) {

        if ($("ul > li").length <= 1) {
            bootbox.alert("请至少保留一张图片");
            return
        }
        var brand = $("#form_brand").val();
        $.ajax({
            type: "POST",
            async: false,
            url: basePath + "/smart/brand/delimg.do",
            data: {
                "brand": brand,
                "no": i
            },
            dataType: "json",
            success: function (result) {
                console.log(result);
                var msg = result.msg;
                $("#form_url").val(result.msg);
//						initBrandImg();
            }
        });
    }
    function clearImg() {
        var ul = document.getElementById("thumbs");
        for (var i = ul.childNodes.length - 1; i > 0; i--) {
            if (i == ul.childNodes.length - 1) {
                ul.removeChild(ul.childNodes[i]);
            }
        }
    }

<%--初始化图片--%>
    function initBrandImg() {
        if ($("ul").has("li").length > 0)
            clearImg();
        var images = $("#form_url").val();
        if (images != "") {
            var n = images.split(',');
            var ul = document.getElementById("thumbs");
            for (var i = 0; i < n.length; i++) {
                var li = document.createElement("li");
                var img = document.createElement("img");
                ul.appendChild(li);
                li.appendChild(img);
                img.setAttribute("width", "200px");
                img.setAttribute("height", "250px");
                img.setAttribute("src", "<%=basePath%>/mirror/brand/" + n[i]);
                var del = document.createElement("div");
                li.appendChild(del);
                del.setAttribute("class", "imgdel");
                del.setAttribute("onclick", "delimg(" + i + ")");
            }
        }
    }

    function initrBandPic() {
        var images = $("#form_url").val();
        if (images != "") {
            var n = images.split(',');
            var html = '';
            for (var i = 0; i < n.length; i++) {
                html += '<a href="<%=basePath%>/mirror/brand/' + n[i] + '" style="background-image:url(<%=basePath%>/mirror/brand/' + n[i] + ')"></a>';
            }
            $("#thumbs").append(html);
        }
        $("#thumbs a").touchTouch();
    }

    function inivaladitor() {
        $("#editForm")
                .bootstrapValidator(
                {
                    message: '输入值无效',
                    feedbackIcons: {
                        valid: 'glyphicon glyphicon-ok',
                        invalid: 'glyphicon glyphicon-remove',
                        validating: 'glyphicon glyphicon-refresh'
                    },
                    submitHandler: function (validator, form,
                                             submitButton) {
                        $.post(form.attr('action'), form
                                .serialize(), function (result) {
                            if (result.success == true
                                    || result.success == 'true') {

                            } else {

                                // Enable the submit buttons
                                $('#editForm').bootstrapValidator(
                                        'disableSubmitButtons',
                                        false);
                            }
                        }, 'json');
                    },
                    fields: {
                        brand: {
                            validators: {
                                notEmpty: {//非空验证：提示消息
                                    message: '编号不能为空'
                                }
                            }
                        },
                        name: {
                            validators: {
                                notEmpty: {//非空验证：提示消息
                                    message: '名称不能为空'
                                }
                            }
                        },
                        address: {
                            validators: {
                                notEmpty: {
                                    message: '归属地不能为空'
                                }
                            }
                        },
                        designer: {
                            validators: {
                                notEmpty: {
                                    message: '设计师不能为空'
                                }
                            }
                        }

                    }
                });
    }

    //initFunction
    function initEditor() {
        $("#editor").froalaEditor({
            height: "300px",
            language: "zh_cn",
            theme: "gray"
        });
    }

    function iniupload() {
        $("#zyUpload").zyUpload(
                {
                    width: "650px", // 宽度
                    height: "400px", // 高度
                    itemWidth: "140px", // 文件项的宽度
                    itemHeight: "115px", // 文件项的高度
                    url: $("#zyUpload").url = basePath
                    + "/smart/brand/saveBrandImages.do?brand="
                    + $("#form_brand").val(), // 上传文件的路径
                    fileType: ["jpg", "png", "jpeg"],// 上传文件的类型
                    fileSize: 51200000, //上传文件的大小
                    multiple: false, // 是否可以多个文件上传
                    dragDrop: false, //是否可以拖动文件上传
                    tailor: true, // 是否可以裁剪图片
                    del: true, // 是否可以删除图片
                    finishDel: false, // 是否在文件上传完成后删除预览

                    /* 外部获得的回调接口 */
                    onSelect: function (selectFiles, allFiles) { // 选择文件的回调方法，select File:当前选中的文件，All File：还没上传的全部文件
                        $("#rapidAddImg").hide();
                        $(".webuploader_pick").hide();
                    },
                    onDelete: function (file, files) { // 删除一个文件的回调方法 file：当前删除的文件 files：删除之后的文件
                        $("#rapidAddImg").show();
                        $(".webuploader_pick").show();
                    },
                    onSuccess: function (file, response) { // 文件上传成功的回调方法
                        console.info("此文件上传成功");
                        console.info(file.name);
                        console.info("此文件上传到服务器地址：");
                        console.info(response);
                        var obj = eval('(' + response + ')');
                        $("#uploadInf").append(
                                "<p>上传成功，文件地址是：" + obj.result + "</p>");
                        //	var imgs = $("#form_url").val();
                        var sun = obj.result;
                        var cut = sun.substr(sun
                                .lastIndexOf("/mirror/brand/")+14);
                        /*								 if (imgs != "" && imgs != null) {
                         imgs += "," + cut;
                         } else {
                         imgs += cut;
                         }
                         imgs+=cut; */
                        $("#form_url").val(cut);
                        saveflag = true;
                        saveBrand();
                    },
                    onFailure: function (file, response) { // 文件上传失败的回调方法
                        console.info("此文件上传失败：");
                        console.info(file.name);
                    },
                    onComplete: function (response) { // 上传完成的回调方法
                        console.info("文件上传完成");
                        console.info(response);
                    }
                });
        $(".upload_preview").css("float", "left");
        $(".upload_preview").css("width", "648px");
    }
</script>
</body>
<script src="<%=basePath%>/Olive/plugin/zyupload/zyUpload.js"></script>
</html>