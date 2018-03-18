<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../../baseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>
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
            <!-- /.page-header -->
            <div class="breadcrumbs" id="breadcrumbs">
                <ul class="breadcrumb">
                    <li><a href="#" onclick="javascript:history.back(-1);">邮件库存预警</a></li>
                    <li class="active">

                    </li>
                </ul>
                <a href="#" onclick="javascript:history.back(-1);" class="pull-right">返回</a>
            </div>
            <div id="page-content">

                <div class="row">
                    <div class="col-xs-12">
                        <div class="widget-body"></div>
                        <div class="widget-main">
                            <form class="form-horizontal" role="form" id="editForm">
                                <div class="form-group">
                                    <label class="col-sm-1 control-label no-padding-right"
                                           for="form_title"> 主题:</label>
                                    <div class="col-xs-4 col-sm-4">
                                        <input class="form-control" id="form_title" name="title"
                                               type="text" value="" />
                                    </div>
                                    <label class="col-sm-1 control-label no-padding-right"
                                           for="form_shopCode"> 店铺:</label>
                                    <div class="col-xs-4 col-sm-4">
                                        <select id="form_shopCode" name="shopCode"
                                                multiple="multiple" data-placeholder="店铺列表">
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-1 control-label no-padding-right"
                                           for="form_class1"> 品牌:</label>
                                    <div class="col-xs-4 col-sm-4">
                                        <select id="form_class1" name="class1"
                                                multiple="multiple" data-placeholder="品牌列表">
                                        </select>
                                    </div>
                                    <label class="col-sm-1 control-label no-padding-right"
                                           for="form_class2"> 年份:</label>
                                    <div class="col-xs-4 col-sm-4">
                                        <select id="form_class2" name="class2"
                                                multiple="multiple" data-placeholder="年份列表">
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-1 control-label no-padding-right"
                                           for="form_class3"> 大类:</label>
                                    <div class="col-xs-4 col-sm-4">
                                        <select id="form_class3" name="class3"
                                                multiple="multiple" data-placeholder="大类列表">
                                        </select>
                                    </div>
                                    <label class="col-sm-1 control-label no-padding-right"
                                           for="form_class4"> 小类:</label>
                                    <div class="col-xs-4 col-sm-4">
                                        <select id="form_class4" name="class4"
                                                multiple="multiple" data-placeholder="小类列表">
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-1 control-label no-padding-right"
                                           for="form_class10"> 季节:</label>
                                    <div class="col-xs-4 col-sm-4">
                                        <select id="form_class10" name="class10"
                                                multiple="multiple" data-placeholder="季节列表">
                                        </select>
                                    </div>
                                    <label class="col-sm-1 control-label no-padding-right"
                                           for="form_sendCycle"> 发送周期:</label>
                                    <div class="col-xs-4 col-sm-4">
                                        <select class="chosen-select form-control" id="form_sendCycle" name="sendCycle">
                                            <option value="w">每周</option>
                                            <option value="m">每月</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-1 control-label no-padding-right"
                                           for="form_toUser"> 收件人:</label>
                                    <div class="col-xs-4 col-sm-4">
                                        <input class="form-control" id="form_toUser" name="toUser"
                                               type="text" value="" />
                                    </div>
                                    <label class="col-sm-1 control-label no-padding-right"
                                           for="form_fromUser"> 发件人:</label>
                                    <div class="col-xs-4 col-sm-4">
                                        <input class="form-control" id="form_fromUser" name="fromUser"
                                               type="text" value="" readonly />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-1 control-label no-padding-right"
                                           for="form_warmLevel"> 等级:</label>
                                    <div class="col-xs-4 col-sm-4">
                                        <select class="chosen-select form-control" id="form_warmLevel" name="warmLevel">
                                            <option value="1">I级</option>
                                            <option value="2">II级</option>
                                            <option value="3">III级</option>
                                            <option value="4">IV级</option>
                                        </select>
                                    </div>
                                    <label class="col-sm-1 control-label no-padding-right"
                                           for="form_status"> 状态:</label>
                                    <div class="col-xs-4 col-sm-4">
                                        <select class="chosen-select form-control" id="form_status" name="status">
                                            <option value="1">开启</option>
                                            <option value="0">未开启</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-1 control-label no-padding-right"
                                           for="form_remark"> 备注:</label>
                                    <div class="col-xs-4 col-sm-4">
                                        <input class="form-control" id="form_remark" name="remark"
                                               type="text" value="" />
                                    </div>
                                    <div class="col-xs-4 col-sm-4" style="display: none;">
                                        <input class="form-control" id="form_id" name="id"
                                               type="text"  />
                                    </div>
                                </div>

                                <div class="form-group">
                                    <div class="col-sm-offset-5 col-sm-12"
                                         style="margin-top: 16px">
                                        <button class="btn btn-primary" type="button"  onclick="save()">
                                            <i class="ace-icon fa fa-save"></i> <span class="bigger-110">保存</span>
                                        </button>
                                    </div>
                                </div>

                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <link href="<%=basePath%>/kendoUI/styles/kendo.common-material.min.css" rel="stylesheet">
    <link href="<%=basePath%>/kendoUI/styles/kendo.rtl.min.css" rel="stylesheet">
    <link href="<%=basePath%>/kendoUI/styles/kendo.material.min.css" rel="stylesheet">
    <script src="<%=basePath%>/kendoUI/js/kendo.all.min.js"></script>
    <script src="<%=basePath%>/kendoUI/js/kendo.timezones.min.js"></script>
    <script src="<%=basePath%>/kendoUI/js/cultures/kendo.culture.zh-CN.min.js"></script>
    <script src="<%=basePath%>/kendoUI/js/messages/kendo.messages.zh-CN.min.js"></script>
    <jsp:include page="../../search/search_js.jsp"></jsp:include>
    <jsp:include page="../../layout/footer.jsp"></jsp:include>
</div>
<jsp:include page="../../layout/footer_js.jsp"></jsp:include>
<script>
    $(function () {

        initMultiSelect();
        $("#form_fromUser").val("support@casesoft.com.cn");
        if("${plEmailTemplate.id}"){
           $("#form_title").val("${plEmailTemplate.title}");
            $("#form_toUser").val("${plEmailTemplate.toUser}");
            $("#form_remark").val("${plEmailTemplate.remark}");
            $("#form_id").val("${plEmailTemplate.id}");
            $("#form_sendCycle").find("option[value='${plEmailTemplate.sendCycle}']").attr("selected",true);
            $("#form_status").find("option[value='${plEmailTemplate.status}']").attr("selected",true);
            $("#form_warmLevel").find("option[value='${plEmailTemplate.warmLevel}']").attr("selected",true);
        }
        initEditFormValid();
    })
    function initEditFormValid() {
        $("#editForm").bootstrapValidator({
            message: '输入值无效',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                title: {
                    validators: {
                        notEmpty: {
                            message: '主题不能为空'
                        }
                    }

                },
                toUser: {
                    validators: {
                        notEmpty: {
                            message: '收件人不能为空,多个收件人请用逗号隔开'
                        },
                        regexp: {
                            regexp: /^((([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6}\,))*(([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})))$/,
                            message: '邮件格式有误,多个收件人请用逗号隔开'
                        }
                    }

                }

            }
        });
    }
    function initMultiSelect() {
        $.ajax({
            url: basePath + "/third/playlounge/plEmailTemplate/initSelect.do?types=C1,C2,C3,C4,C10",
            type: "post",
            success: function(data) {
                $("#form_class1").kendoMultiSelect({
                    dataTextField: "name",
                    dataValueField: "code",
                    height: 400,
                    suggest: true,
                    dataSource: {
                        data:data.result.C1
                    }
                })
                $("#form_class2").kendoMultiSelect({
                    dataTextField: "name",
                    dataValueField: "code",
                    height: 400,
                    suggest: true,
                    dataSource: {
                        data:data.result.C2
                    }
                })
                $("#form_class3").kendoMultiSelect({
                    dataTextField: "name",
                    dataValueField: "code",
                    height: 400,
                    suggest: true,
                    dataSource: {
                        data:data.result.C3
                    }
                })
                $("#form_class4").kendoMultiSelect({
                    dataTextField: "name",
                    dataValueField: "code",
                    height: 400,
                    suggest: true,
                    dataSource: {
                        data:data.result.C4
                    }
                })
                $("#form_class10").kendoMultiSelect({
                    dataTextField: "name",
                    dataValueField: "code",
                    height: 400,
                    suggest: true,
                    dataSource: {
                        data:data.result.C10
                    }
                })

                var form_class1 = $("#form_class1").data("kendoMultiSelect");
                var class1="${plEmailTemplate.class1}";
                form_class1.value(class1.split(","));
                form_class1.trigger("change");
                var form_class2 = $("#form_class2").data("kendoMultiSelect");
                var class2="${plEmailTemplate.class2}";
                form_class2.value(class2.split(","));
                form_class2.trigger("change");
                var form_class3 = $("#form_class3").data("kendoMultiSelect");
                var class3="${plEmailTemplate.class3}";
                form_class3.value(class3.split(","));
                form_class3.trigger("change");
                var form_class4 = $("#form_class4").data("kendoMultiSelect");
                var class4="${plEmailTemplate.class4}";
                form_class4.value(class4.split(","));
                form_class4.trigger("change");
                var form_class10 = $("#form_class10").data("kendoMultiSelect");
                var class10="${plEmailTemplate.class10}";
                form_class10.value(class10.split(","));
                form_class10.trigger("change");


            }});
        $.ajax({
            url: basePath + "/sys/warehouse/list.do?filter_EQI_type=4",
            type: "post",
            success: function(data) {
                $("#form_shopCode").kendoMultiSelect({
                    dataTextField: "name",
                    dataValueField: "code",
                    height: 400,
                    suggest: true,
                    dataSource: {
                        data:data
                        }
                })
                var form_shopCode = $("#form_shopCode").data("kendoMultiSelect");
                var shopCode="${plEmailTemplate.shopCode}";
                form_shopCode.value(shopCode.split(","));
                form_shopCode.trigger("change");


            }
        });


    }

    function save(){
        $("#editForm").data('bootstrapValidator').validate();
        if(!$("#editForm").data('bootstrapValidator').isValid()){
            return ;
        }
        var progressDialog = bootbox.dialog({
            message: '<p><i class="fa fa-spin fa-spinner"></i> 数据上传中...</p>'
        });
        var formData=$("#editForm").serialize();
        $.ajax({
            url: basePath+"/third/playlounge/plEmailTemplate/save.do",
            type: "post",
            data:formData,
            success: function(result) {
                if(result.success == true || result.success == 'true') {
                    bootbox.alert("保存成功");
                }
                progressDialog.modal('hide');
            }});
    }
</script>
</body>
</html>