<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/Olive/plugin/zyupload/skins/zyupload-1.0.0.min.css"/>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>
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
        <div class="main-content-inner">
            <div class="breadcrumbs" id="breadcrumbs">
                <ul class="breadcrumb">
                    <li><a href="#"
                           onclick="javascript: window.location.href=basePath+'/views/wms/wmsFloor.jsp';">PL区管理</a></li>
                    <li class="active">
                    </li>
                </ul>
                <a href="#" onclick="javascript: window.location.href=basePath+'/views/wms/wmsFloor.jsp';"
                   class="pull-right">返回</a>
            </div>
            <div id="page-content">
                <div class="col-lg-12">
                    <form class="form-horizontal" role="form" id="floorEditForm" style="margin-top: 18px">
                        <div class="form-group">
                            <label class="col-xs-2 col-lg-offset-2 control-label text-right" for="form_name">名称</label>

                            <div class="col-xs-4 ">
                                <input id="form_name" class="form-control" name="name" type="text"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-2 col-lg-offset-2 control-label text-right" for="form_deviceId">设备号</label>

                            <div class="col-xs-4 ">
                                <input id="form_deviceId" class="form-control" name="deviceId" type="text"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-2 col-lg-offset-2 control-label text-right" for="form_parentId">所属</label>

                            <div class="col-xs-4 ">
                                <input id="form_parentId" class="form-control" name="parentId" type="text"
                                       readonly/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-2 col-lg-offset-2 control-label text-right" for="form_barcode">条码</label>

                            <div class="col-xs-4 ">
                                <input id="form_barcode" class="form-control" name="barcode" type="text"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-2 col-lg-offset-2 control-label text-right" for="form_sales" >卖场</label>

                            <div class="col-xs-4 " id="sales">
                                <select id="form_sales" name="sales" class="chosen-select form-control">
                                    <option value="0">否</option>
                                    <option value="1">是</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-2 col-lg-offset-2 control-label text-right" for="form_remark">备注</label>

                            <div class="col-xs-4 ">
                                <input id="form_remark" class="form-control" name="remark" type="text"/>
                            </div>
                            <input id="form_id" class="form-control" name="id" type="text" style="display: none"/>
                            <input id="form_image" class="form-control" name="image" type="text" style="display: none"/>
                        </div>


                        <div class="form-group">
                            <label class="col-xs-2 col-lg-offset-2 control-label text-right">图片</label>

                            <div class="col-lg-4 col-xs-4">
                                <div class="widget-main padding-12" id="uploadarea">
                                    <input type="file" id="fileUpload" required="true">
                                </div>
                            </div>
                        </div>
                    </form>
                    <div class="col-xs-1" style="margin-left: 47%">
                        <a href="#" id="save" class="btn btn-primary" onclick= "save()">保存</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="../layout/footer.jsp"></jsp:include>
</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/zyupload/zyUpload.js"></script>

<script>
    $(function () {
        if ("${floor.id}" != "") {
            $("#form_name").val("${floor.name}");
            $("#form_deviceId").val("${floor.deviceId}");
            $("#form_parentId").val("${floor.parentId}");
            $("#form_barcode").val("${floor.barcode}");
            if("${floor.sales}"=="false"){
                //$("#form_sales").find("option[value='0']").attr("selected",true);
                var html =' <input id="form_sales" class="form-control" name="sales" type="text" value="${sales}" readonly/>';
                $("#sales").html(html);
            }else{
                $("#form_sales").find("option[value='1']").attr("selected",true);
            }
            <%--$("#form_sales").val("${floor.sales}");--%>
            $("#form_remark").val("${floor.remark}");
            $("#form_image").val("${floor.image}");
            $("#form_id").val("${floor.id}");
            initFloorareaFormValid(false,"${floor.barcode}","${shopId}");
        }else
        if ("${parentId}" != "") {
            $("#form_parentId").val("${parentId}");
            if("${sales}"=="false"){
              var html =' <input id="form_sales" class="form-control" name="sales" type="text" value="${sales}" readonly/>';
               // $("#form_sales").find("option[value='0']").attr("selected",true);
                $("#sales").html(html);

            }
            initFloorareaFormValid(true,"","${shopId}");
        }
        initUploadFile();
    })

    //初始化ace-admin文件上传控件
    function initUploadFile() {
        $('#fileUpload').ace_file_input({
            style: 'well',
            btn_choose: '点击选择图片或将图片拖到此处',
            btn_change: null,
            no_icon: 'ace-icon fa fa-cloud-upload',
            droppable: true,
            thumbnail: 'small'//large | fit
            ,
            preview_error: function (filename, error_code) {
            }
        }).on('change', function () {

        });
    }
    function initFloorareaFormValid(isAdd, oldBarcode) {
        $('#floorEditForm').bootstrapValidator({
            message: '输入值无效',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                barcode: {
                    validators: {
                        regexp: {
                            regexp: /^[A-Za-z0-9]+$/,
                            message: '只能是字母或数字'
                        },
                        stringCase: {
                            message: '字母必须大写',
                            'case': 'upper'
                        },
                        notEmpty: {//非空验证：提示消息
                            message: '条码不能为空'
                        },
                        stringLength: {
                            min: 0,
                            max: 32,
                            message: '长度不超过32'
                        },
                        threshold: 5, //有5字符以上才发送ajax请求，（input中输入一个字符，插件会向服务器发送一次，设置限制，6字符以上才开始）
                        remote: {//ajax验证。server result:{"valid",true or false} 向服务发送当前input name值，获得一个json数据。例表示正确：{"valid",true}
                            url: basePath + "/wms/pl/checkBarcode.do?flag=fl&isAdd=" + isAdd + "&oldBarcode=" + oldBarcode,//验证地址
                            message: '条码已存在',//提示消息
                            delay: 2000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                            type: 'POST',//请求方式
                            data: function (validator) {
                                return {
                                };
                            }

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
    function save(){
        $('#floorEditForm').data('bootstrapValidator').validate();
        if(!$('#floorEditForm').data('bootstrapValidator').isValid()){
            return ;
        }
        saveImg($("#form_barcode").val());
        var formData= $("#floorEditForm").serialize();

        $.ajax({
            type: "POST",
            url: basePath + "/wms/pl/saveFl.do",
            data: formData,
            dataType: "json",
            success: function (result) {
                if(result.success == true || result.success == 'true') {
                    bootbox.alert("保存成功")
                }
            }
        });
    }

    // 删除图片
    function deleteImg(image){
        $.ajax({
            url:basePath+"/wms/pl/deleteImg.do?image="+image,
            type:'post',
            async:false,
            dataType : 'json',
            success:function(result){
                if(result.success == true || result.success == 'true') {
                    console.log("删除图片:"+image);
                }else{
                    bootbox.alert("保存失败");
                }
            }
        });
    }
    function saveImg(barcode){
        try {
            var file = $("#fileUpload").data('ace_input_files')[0];
        }catch (e){
            return ;
        }
        if(file == null){
            bootbox.alert("请选择上传的图片");
            return;
        }
        var progressDialog = bootbox.dialog({
            message: '<p><i class="fa fa-spin fa-spinner"></i> 图片上传中...</p>'
        });
        var fd = new FormData();
        fd.append("file", file);
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange=function(){
            if(xhr.readyState==4){
                if(xhr.status==200){
                    if($("#form_image").val()!=""){
                        deleteImg($("#form_image").val());//删除原图片
                    }
                    var message=xhr.responseText;
                    var obj=eval("("+message+")");
                    $("#form_image").val(obj.msg);//设置url
                    progressDialog.modal('hide');
                }else{
                    progressDialog.modal('hide');
                    var fail="图片上传失败";
                    bootbox.alert(fail);

                }
            }
        }
        xhr.open("POST", basePath + "/wms/pl/saveImg.do?barcode="+barcode+"&flag=fl",false);
        xhr.send(fd);
    }
</script>

</body>
</html>