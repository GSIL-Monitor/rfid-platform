$(function(){
    inifroala();
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
function initUpload(){
    //调用bootstrap验证
    $('#editForm').data('bootstrapValidator').validate();
    if(!$('#editForm').data('bootstrapValidator').isValid()){
        return ;
    }

    $("#form_id").attr("readonly",true);
    $("#form_fileType").attr("disabled",true);

    var fileType=$("#form_fileType").val();

    if(fileType=="I"){
            initZyupload();

    }else{
          openVideoUpload();
    }
    $("#btnshow").hide();
    $("#btnhide").show();
}

//给下拉框绑定事件
$("#form_fileType").ready(function () {
    $("#form_fileType").bind("change",function(){
        if($(this).val()=="I"){
            $("#widget_title").html("图片");
        }
        else{
            $("#widget_title").html("视频");
        }
    });
});

function initImg(images){
    //逗号分割图片名称并加载
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
            img.setAttribute("src",basePath+"/mirror/home/"+n[i]);
            var del=document.createElement("div");
            li.appendChild(del);
            del.setAttribute("class","imgdel");
            del.setAttribute("onclick","delimg("+i+")");
        }
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
        thumbnail: 'small',
        preview_error : function(filename, error_code) {
        }
    }).on('change', function(){
    });
}


//动态显示video标签
function initMedia(){
    var url=$("#form_url").val();
    var html="";
    html+="<video width='600' height='450' id='video' controls>";
    html+="<source src="+basePath+"/mirror/home/"+url+" type='video/mp4'/>";
    html+="</video>";
    $("#uploadarea").append(html);
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
    if(!isEdit){
        save();
    }
    //var file = document.getElementById('fileUpload').files[0];
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
    xhr.open("POST", basePath + "/smart/home/saveVideo.do",true);
    xhr.send(fd);
}

//删除视频
function deleteVideo(url){
    $.ajax({
        url:basePath+"/smart/home/deleteVideo.do?url="+url,
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

function openVideoUpload(){
    $("#video").remove();//移除video
    //加载文件上传控件
    var html="";
    html+="<div class='col-xs-4 col-xs-offset-4' id='uploadArea' style='margin-top: 50px'>";
    html+='<input type="file" id="fileUpload" required="true" accept="video/mp4">';
    html+='<button type="button" class="btn btn-info btn-block" onclick="saveVideo();">';
    html+='上传';
    html+='</bntton>'
    html+='</div>';

    $("#uploadarea").append(html);
    initUploadFile();
    $("#btnshow").hide();
    $("#btnhide").show();
}

function cancel(){
    var fileType=$("#form_fileType").val();
    if(fileType=="I"){
        $("#uploadArea").remove();
        $("#zyupload").remove();
        clearImg();
        initImg($("#form_url").val());
        $("#gallery").show();
    }else if(fileType=="V"){
        $("#uploadArea").remove();
        $("#zyupload").remove();
        initMedia();
    }
    $("#btnshow").show();
    $("#btnhide").hide();

}

function delimg(i){
    var Id=$("#form_id").val();
    $.ajax({
        type:"POST",
        url:basePath+"/smart/home/deleteImg.do",
        data: {"Id":Id,"no":i},
        dataType:"json",
        success:function(result){
            console.log(result);
            clearImg();
            initImg(result.msg);
            $("#form_url").val(result.msg);
        }
    });
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
    $("#form_fileType").attr("disabled",false);
    var formData=  $("#editForm").serialize();
    $.ajax({
        url:basePath+"/smart/home/save.do",
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
    $("#form_fileType").attr("disabled",true);
}



function initZyupload(){
    $("#gallery").hide();
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
        url              :   $("#zyupload").url= basePath+"/smart/home/saveImages.do?id="+$("#form_id").val(),  // 上传文件的路径
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
            initImg(obj.msg)
            save();

        },
        onFailure: function(file, response){          // 文件上传失败的回调方法
            console.info("此文件上传失败：");
            console.info(file.name);
        },
        onComplete: function(response){           	  // 上传完成的回调方法

        }
    });
    $(".upload_preview").css("width","695px");
}

