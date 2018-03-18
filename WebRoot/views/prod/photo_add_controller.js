
var photo,photourls="",SCN={};
function iniColor(){

    $.ajax({
        url: basePath + "/prod/color/list.do?",
        cache: false,
        async: false,
        type: "POST",
        success: function (data, textStatus) {
            var json = data;
            $("#form_colorId").empty();
            $("#form_colorId").multiselect({
                inheritClass: true,
                includeSelectAllOption: true,
                selectAllNumber: true,
                enableFiltering: true,
                filterPlaceholder:"请输入颜色",
                maxHeight: "400"
            });
            for (var i = 0; i < json.length; i++) {
                var backColor = "#ffffff";
                if (json[i].hex != undefined) {
                    backColor = json[i].hex;
                }
                $("#form_colorId").append("<option value='" + json[i].colorId + "' style='background-color: " + backColor + "'>" + json[i].colorName + "</option>");


            }
            $('#form_colorId').multiselect('rebuild');
        }
    });
}
function iniPic(url,SCN){

    var ul = document.getElementById("gallery");
    if(ul.childElementCount>0){
        for (var i = ul.childNodes.length - 1; i > 0; i--) {
            if (i == ul.childNodes.length - 1) {
                ul.removeChild(ul.childNodes[i]);
            }
        }
    }
    if(url==""){

    }else{
        var n=url.split(',');
        var ul=document.getElementById("gallery");
        for(var i=0;i< n.length;i++){
            var styleId=SCN[n[i]].substr(0,SCN[n[i]].indexOf("&"));
            var colorId=SCN[n[i]].substr(SCN[n[i]].indexOf("&")+1);
            var li=document.createElement("li");
            li.style.border="solid #ffffff";
            var img=document.createElement("img");
            ul.appendChild(li);
            li.appendChild(img);
            img.setAttribute("width","200px");
            img.setAttribute("height","250px");
            img.setAttribute("src",basePath+"/product/photo"+n[i]);
            img.setAttribute("alt",styleId+"&#13;"+colorId);
            var del=document.createElement("div");
            li.appendChild(del);
            del.setAttribute("class","imgdel");
            del.setAttribute("onclick","delimg("+i+")");
            var info=document.createElement("p");
            info.innerHTML="<span>款号:"+styleId+"&nbsp;&nbsp;&nbsp;&nbsp;颜色:"+colorId+"</span>";
            li.appendChild(info);
        }
    }
}
function delimg(i){
    var src=$("li:eq("+i+") img").attr("src");
    var url=src.substring(src.indexOf('product/photo')+13);
    $.ajax({
        url:basePath+"/prod/photo/delPhoto.do",
        async:false,
        type:"POST",
        data:{
            src:url
        },
        dataType:"json",
        success:function(result){
            var urlArray=photourls.split(",");
            urlArray.splice(jQuery.inArray(url,urlArray),1);
            var example="";
            if(urlArray.length>0){
                for(var iu=0;iu<urlArray.length;iu++){
                    if(urlArray[i]==""){
                        continue;
                    }
                    if(example==""){
                        example+=urlArray[iu];
                    } else {
                        example+=","+urlArray[iu];
                    }
                }
            }
            photourls=example;
            iniPic(photourls,SCN);
            $.gritter.add({
                text: result.msg,
                class_name: 'gritter-success  gritter-light'
            });
        }
    });
}

$("#form_colorId").bind("change",function () {
    var color = $("#form_colorId").find("option:selected").css("background-color");
    $("#form_colorId").css("background-color", color);
    console.log(color);
});
function initUploadSet() {
    $(".table-header").append("图片上传");
    $('#upload_file_inputs').fileinput({
        language: 'zh',
        uploadUrl: basePath+"/prod/photo/uploadPhoto.do",
        uploadExtraData: function (previewId, index) {
            return photo;
        },
        allowedPreviewTypes: ['image'],
        allowedFileExtensions: ["jpg", "jpeg","png"]

    }).on("fileuploaded", function (event, result) {
        if (result.response.success) {
            $.gritter.add({
                text: result.response.msg,
                class_name: 'gritter-success  gritter-light'
            });
            $("#modal-Upload").modal("hide");
            var res=result.response.result;

            var src=res.src;
            SCN[src]=res.styleId+"&"+res.colorId;
            console.log(SCN);
            iniPic(photourls==""?photourls+=res.src:photourls+=","+res.src,SCN);
        } else {
            $.gritter.add({
                text: result.response.msg + "!" + result.response.result,
                class_name: 'gritter-false  gritter-light'
            });
        }
    });

    $('#upload_file_inputs').on('fileuploaderror', function (event, data, previewId, index) {
        var form = data.form, files = data.files, extra = data.extra,
            response = data.response, reader = data.reader;
        console.log(data);
        console.log('File upload error');
    });

    $('#upload_file_inputs').on('fileerror', function (event, data) {
        console.log(data.id);
        console.log(data.index);
        console.log(data.file);
        console.log(data.reader);
        console.log(data.files);
    });

    $('#upload_file_inputs').on('fileuploaded', function (event, data, previewId, index) {
        var form = data.form, files = data.files, extra = data.extra,
            response = data.response, reader = data.reader;

        console.log('File uploaded triggered');
    });
}



function showUpload() {
    if (document.getElementById("form_styleId").value == "" || $("#form_colorId").val() == null) {
        bootbox.alert("请先选择款式与颜色");
        return;
    }
    photo = {"styleId": $("#form_styleId").val(),"colorId":$("#form_colorId").val()};
    $("#modal-Upload").modal("show");
}