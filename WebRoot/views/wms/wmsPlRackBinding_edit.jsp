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
                    <li><a href="#" onclick="javascript: window.location.href=basePath+'/views/wms/wmsPlRackBindingRelation.jsp';">PL货架绑定</a></li>
                </ul>
                <a href="#" onclick="javascript: window.location.href=basePath+'/views/wms/wmsPlRackBindingRelation.jsp';" class="pull-right">返回</a>
            </div>
            <div id="page-content">
                <div class="col-lg-12">
                    <form class="form-horizontal" role="form" id="editForm" style="margin-top: 18px">
                        <input  class="form-control"  type="text" value="${rack.id}" readonly style="display: none;"/>
                        <div class="form-group">
                            <label class="col-xs-2 col-lg-offset-2 control-label text-right" for="form_floorAreaId">区</label>

                            <div class="col-xs-4 ">
                                <input id="form_floorAreaId" class="form-control" name="floorAreaId" type="text" value="${rack.floorAreaName}" readonly/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-2 col-lg-offset-2 control-label text-right" for="form_floorId">库位</label>

                            <div class="col-xs-4 ">
                                <input id="form_floorId" class="form-control" name="floorId" type="text" value="${rack.parentName}" readonly/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-2 col-lg-offset-2 control-label text-right" for="form_rackId">货架</label>

                            <div class="col-xs-4 ">
                                <input id="form_rackId" class="form-control" name="rackId" type="text" value="[${rack.id}]${rack.name}" readonly/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-2 col-lg-offset-2 control-label text-right" for="form_styleId">款式</label>

                            <div class="col-xs-4 ">
                                <div class="input-group">
                                <input class="form-control" id="form_styleId" type="text" name="styleId"  placeholder="款号" readonly/>
                                    <span class="input-group-btn" onclick="openstyleDialog('#form_styleId','#form_styleName',initSelect)">
                                        <button class="btn btn-sm btn-default" type="button"><i class="ace-icon fa fa-list"></i></button>
                                    </span>
                                <input class="form-control" id="form_styleName" type="text" placeholder="款名" readonly/>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-2 col-lg-offset-2 control-label text-right" for="form_colorId">颜色</label>

                            <div class="col-xs-4 ">
                                <select class="chosen-select  form-control" id="form_colorId" name="colorId" >
                                </select>
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
<jsp:include page="../base/style_dialog.jsp"></jsp:include>
</body>
<script>
    $(function () {
//        initSelect();
        initFormValid();
    })

    function  initSelect(){
        var obj=document.getElementById("form_colorId");
        obj.options.length=0;
        $.ajax({
            type: "POST",
            url: basePath + "/prod/product/list.do?filter_EQS_styleId="+$("#form_styleId").val(),
            dataType: "json",
            success: function (result) {
                var json = result;
                for (var i = 0; i < json.length; i++) {
                    var backColor = "#ffffff";
                    if (json[i].hex != undefined) {
                        backColor = json[i].hex;
                    }
                    $("#form_colorId").append("<option value='" + json[i].colorId + "' style='background-color: " + backColor + "'>" + json[i].colorName + "</option>");
                    $("#form_colorId").trigger('chosen:updated');
                }
            }
        });
    }

    function initFormValid() {
        $('#editForm').bootstrapValidator({
            message: '输入值无效',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                styleId: {
                    validators: {
                        notEmpty: {
                            message: '款式不能为空'
                        },
                        remote: {//ajax验证。server result:{"valid",true or false} 向服务发送当前input name值，获得一个json数据。例表示正确：{"valid",true}
                            url: basePath + "/wms/pl/binding/checkStyle.do?rackId="+$("#form_rackId").val(),//验证地址
                            message: '款式已在其他货架绑定',//提示消息
                            delay: 2000,//每输入一个字符，就发ajax请求，服务器压力还是太大，设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                            type: 'POST',//请求方式
                            data: function (validator) {
                                return {
                                };
                            }

                        }
                    }
                },
                colorId: {
                    validators: {
                        notEmpty: {
                            message: '颜色不能为空'
                        }
                    }
                }
            }
        });
    }

    function save(){
        $('#editForm').data('bootstrapValidator').validate();
        if(!$('#editForm').data('bootstrapValidator').isValid()){
            return ;
        }

        var formData= $("#editForm").serialize();
        $.ajax({
            type: "POST",
            data:formData,
            url: basePath + "/wms/pl/binding/bindRack.do",
            dataType: "json",
            success: function (result) {
                if(result.success == true || result.success == 'true') {
                    bootbox.alert("保存成功");
                }
            }
        })
    }
</script>
</html>
