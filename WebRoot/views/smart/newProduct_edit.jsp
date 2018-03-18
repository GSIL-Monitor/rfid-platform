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
                    <button class="btn btn-xs btn-light bigger" onclick="saveNewProduct()">
                        <i class="ace-icon fa fa-save"></i>
                        保存
                    </button>
                    <button class="btn btn-xs btn-light bigger" onclick="historyBack()">
                        <i class="ace-icon fa fa-arrow-left"></i>
                        返回
                    </button>
                </div>
            </div>
            <div class="widget-body">
                <div class="widget-main padding-12">
            <form class="form-horizontal" role="form" id="editForm">
            <div class="form-group">

                    <label class="col-xs-1 control-label no-padding-right" for="form_styleId">款号</label>

                    <div class="col-xs-2">
                        <div class="input-group">
                            <input class="form-control" id="form_styleId" type="text" name="styleId" readonly/>
                                <span class="input-group-btn">
                                    <button class="btn btn-sm btn-default" id="setstyle" type="button" onclick="openstyleDialog('#form_styleId','',initStyle)">
                                        <i class="ace-icon fa fa-list"></i>
                                    </button>
								</span>
                        </div>
                    </div>

                <label class="col-xs-1 control-label" for="form_name">款名</label>
                <div class="col-xs-2">
                    <input class="form-control" id="form_name" name="name"
                           type="text" readonly/>
                </div>
                <label class="col-xs-1 control-label" for="form_colorIds">颜色</label>
                <div class="col-xs-2">
                    <input class="form-control" id="form_colorIds" name="colorIds"
                           type="text"  readonly/>
                </div>

                <label class="col-xs-1 control-label" for="form_sizeIds">尺码</label>
                <div class="col-xs-2">
                    <input class="form-control" id="form_sizeIds" name="sizeIds"
                           type="text"  readonly/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-xs-1 control-label" for="form_price">价格</label>
                <div class="col-xs-2">
                    <input class="form-control" id="form_price" name="price"
                           type="text" readonly/>
                </div>

                <label class="col-xs-1 control-label" for="form_seqNo">序号</label>
                <div class="col-xs-2">
                    <input class="form-control" id="form_seqNo" name="seqNo"
                           type="text"  readonly/>
                </div>

                <label class="col-xs-1 control-label" for="form_brandCode">品牌</label>
                <div class="col-xs-2">
                <select class="form-control" id="form_brandCode" name="brandCode">
                    <option value="">--请选择--</option>
                </select>
                </div>

                <div class="col-xs-2" style="display: none">
                    <input class="form-control" id="form_url" name="url"
                           type="text"  />
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
                        <%--<div id="zyupload" class="zyupload" style="display: none">--%>
                        <%--</div>--%>

                        <ul class="ace-thumbnails clearfix" id="gallery">
                        </ul>
                    </div>
                    </div>
                </div>
            </div>
        </div>
        <%--<div class="col-sm-offset-4 col-sm-12" style="margin-top:16px;">--%>
            <%--<button class="btn btn-primary" type="button" id="save" onclick="saveNewProduct()">--%>
                <%--<i class="ace-icon fa fa-save"></i>--%>
                <%--<span class="bigger-110">保存</span>--%>
            <%--</button>--%>
        <%--</div>--%>
    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>

</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
</body>
<jsp:include page="../base/style_dialog.jsp"></jsp:include>

<script type="text/javascript">

    $(function(){
        //编辑时styleId不可修改
        if("${newProduct.styleId}"!=""){
            $("#setstyle").removeAttr("onclick");
        }
        initEdit();
        initBrandCode();
    });
    function initBrandCode(){
        $.ajax({
            url : basePath + "/smart/newProduct/findBrandCode.do",
            cache : false,
            async : false,
            type : "POST",
            success : function (data,textStatus){
                var json= data;
                for(var i=0;i<json.length;i++){
                    $("#form_brandCode").append("<option value='"+json[i].brand+"'>"+json[i].name+"</option>");
                    $("#form_brandCode").trigger('chosen:updated');
                    if("${newProduct.styleId}"!=""){
                        $("#form_brandCode").find("option[value='${newProduct.brandCode}']").attr("selected",true);
                    }
                }
            }
        })

    }
    function initEdit(){
        if("${newProduct.styleId}"!=""){
            $("#form_styleId").val("${newProduct.styleId}");
            $("#form_name").val("${newProduct.name}");
            $("#form_colorIds").val("${newProduct.colorIds}");
            $("#form_sizeIds").val("${newProduct.sizeIds}");
            $("#form_price").val("${newProduct.price}");
            $("#form_seqNo").val("${newProduct.seqNo}");
            var images="${newProduct.url}";
            $("#form_url").val(images);
           initImg(images);
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
        var styleId=$("#form_styleId").val();
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
                img.setAttribute("src","<%=basePath%>/mirror/newProduct/"+n[i]);
                var del=document.createElement("div");
                li.appendChild(del);
                del.setAttribute("class","imgdel");
                del.setAttribute("onclick","delimg("+i+")");
            }
        }

    }
	
    function delimg(i){
        var styleId=$("#form_styleId").val();
        $.ajax({
            type:"POST",
            url:basePath+"/smart/newProduct/deleteImg.do",
            data: {"styleId":styleId,"no":i},
            dataType:"json",
            success:function(result){
               console.log(result);
                clearImg();
                initImg(result.msg);
                $("#form_url").val(result.msg);
            }
        });
    }

    function initStyle(){
        var styleId=$("#form_styleId").val();
            $.ajax({
                    type:"POST",
                    url:basePath+"/smart/newProduct/show.do",
                    data: {"styleId":styleId},
                    dataType:"json",
                    success:function(result){
                            $("#form_name").val(result.name);
                            $("#form_colorIds").val(result.colorIds);
                            $("#form_sizeIds").val(result.sizeIds);
                            $("#form_price").val(result.price);
                            $("#form_seqNo").val(result.seqNo);
                            $("#form_price").val(result.price);
                            $("#form_url").val(result.images);

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
            url              :   $("#zyupload").url= basePath+"/smart/newProduct/saveImages.do?styleid="+$("#form_styleId").val(),  // 上传文件的路径
            fileType         :   ["jpg","jpeg","bmp","png"],// 上传文件的类型
            fileSize         :   51200000,                // 上传文件的大小
            multiple         :   true,                    // 是否可以多个文件上传
            dragDrop         :   true,                    // 是否可以拖动上传文件
            tailor           :   true,                    // 是否可以裁剪图片
            del              :   true,                    // 是否可以删除文件
            finishDel        :   false,  				  // 是否在上传文件完成后删除预览
            /* 外部获得的回调接口 */
            onSelect: function(selectFiles, allFiles){    // 选择文件的回调方法  selectFile:当前选中的文件  allFiles:还没上传的全部文件
                console.info("当前选择了以下文件：");
                console.info(selectFiles);

            },
            onDelete: function(file, files){              // 删除一个文件的回调方法 file:当前删除的文件  files:删除之后的文件
                console.info("当前删除了此文件：");
                console.info(file.name);
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
                    imgs+=","+obj.msg;
                }else{
                    imgs+=obj.msg;
                }
                $("#form_url").val(imgs);

                saveNewProduct();
            },
            onFailure: function(file, response){          // 文件上传失败的回调方法
                console.info("此文件上传失败：");
                console.info(file.name);
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
        var styleId=$("#form_styleId").val();
        if(styleId){
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
	
    function historyBack(){
    	location.href=basePath+"/smart/newProduct/index.do";
    }
    
    function saveNewProduct(){
        var progressDialog = bootbox.dialog({
            message: '<p><i class="fa fa-spin fa-spinner"></i> 数据上传中...</p>'
        });
        $.post(basePath+"/smart/newProduct/save.do",
                $("#editForm").serialize(),
                function(result) {
                    if(result.success == true || result.success == 'true') {
                        progressDialog.modal('hide');
                        $("#edit-dialog").modal('hide');
                         cancel();
                        var imgs= $("#form_url").val();
                        clearImg();
                        initImg(imgs);
                    }
                }, 'json');
    }

    function reloadImg(styleId){
        $.ajax({
            type:"POST",
            url:basePath+"/smart/newProduct/reloadImg.do",
            data: {"styleId":styleId},
            dataType:"json",
            success:function(response){
                if(response.success){
                    var respones=eval("("+response+")");
                  initImg(response.result);
                }
            }
        })

    }

</script>
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/zyupload/zyUpload.js"></script>

</html>