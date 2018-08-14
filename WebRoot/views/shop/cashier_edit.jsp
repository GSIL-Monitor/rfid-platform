<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + request.getContextPath();
%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
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
                <script type="text/javascript">
                    try {
                        ace.settings.check('breadcrumbs', 'fixed')
                    } catch (e) {
                    }
                </script>

                <ul class="breadcrumb">
                    <li><a href="#" onclick="history.back(-1);">收银员信息</a></li>
                    <li class="active">

                    </li>
                </ul>
                <!-- /.breadcrumb -->
                <a href="#" onclick="history.back(-1);" class="pull-right">返回</a>
            </div>
            <div id="page-content">

                <div class="row">
                    <div class="col-xs-12">
                        <!-- PAGE CONTENT BEGINS -->
                        <div class="widget-body"></div>
                        <div class="widget-main">
                            <form class="form-horizontal" role="form" id="editForm">
                                <div class="form-group" id="idGroup">
                                    <label class="col-sm-4 control-label no-padding-right"
                                           for="form_id"> 编&nbsp;&nbsp;号:</label>
                                    <div class="col-xs-5 col-sm-5">
                                        <input class="form-control" id="form_id" name="id"
                                               type="text" value="${user.id}"/>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-4 control-label no-padding-right"
                                           for="form_name"> 姓&nbsp;&nbsp;名 :</label>
                                    <div class="col-xs-5 col-sm-5">
                                        <input class="form-control" id="form_name" name="name"
                                               type="text" value="${user.name}"/>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-4 control-label no-padding-right"
                                           for="form_code"> 登录名:</label>
                                    <div class="col-xs-5 col-sm-5">
                                        <input class="form-control" id="form_code" name="code"
                                               type="text" value="${user.code}"
                                               onkeyup="this.value=this.value.toUpperCase()"/>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-4 control-label no-padding-right"
                                           for="form_phone"> 手机号:</label>
                                    <div class="col-xs-5 col-sm-5">
                                        <input class="form-control" id="form_phone" name="phone"
                                               type="text" value="${user.phone}"/>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-4 control-label no-padding-right"
                                           for="form_ownerId">所属门店:</label>
                                    <div class="col-xs-5 col-sm-5">
                                        <div class="input-group">
                                            <input class="form-control" id="form_ownerId"
                                                   type="text" name="ownerId" readonly value="${user.ownerId}"/>
                                            <span class="input-group-btn">
																			<button class="btn btn-sm btn-default"
                                                                                    id="setOwnerId"
                                                                                    type="button"
                                                                                    onclick="openShopSelectDialog('#form_ownerId','#form_unitName','')">
																				<i class="ace-icon fa fa-list"></i>
																			</button>
													
																		</span>
                                            <input class="form-control" id="form_unitName"
                                                   type="text" name="unitName" value="${user.unitName}" readonly/>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label no-padding-right">密&nbsp;&nbsp;码:</label>
                                    <div class="col-xs-5 col-sm-5">
                                        <input class="form-control" id="form_password" name="password"
                                               type="password" value="${user.password}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label no-padding-right">备&nbsp;&nbsp;注</label>
                                    <div class="col-xs-5 col-sm-5">
                                        <input class="form-control" id="form_remark" name="remark"
                                               type="text" value="${user.remark}"/>
                                    </div>
                                </div>
                                <div class="form-group" id="isShowCreteDate">
                                    <label class="col-sm-4 control-label no-padding-right"
                                           for="form_ownerId">创建时间:</label>
                                    <div class="col-xs-5 col-sm-5">
                                        <input class="form-control" id="form_createDate" name="createDate"
                                               type="text"
                                               value='<fmt:formatDate value="${user.createDate}" pattern="yyyy-MM-dd HH:mm:ss" />'
                                               readonly/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-offset-5 col-sm-12"
                                         style="margin-top: 16px">
                                        <button class="btn btn-primary" type="button" onclick="saveCashier()">
                                            <i class="ace-icon fa fa-save"></i> <span class="bigger-110">保存</span>
                                        </button>
                                    </div>
                                </div>

                            </form>
                        </div>
                        <!-- PAGE CONTENT ENDS -->
                    </div>
                    <!-- /.col -->
                </div>
                <!-- /.row -->
                <!--/#page-content-->
            </div>
        </div>
    </div>


</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="../base/shop_dialog.jsp"></jsp:include>
<script type="text/javascript"
        src="<%=basePath%>/views/shop/cashierController.js"></script>
<script type="text/javascript">
    $(function () {
        showcreateDate();
        init();
    });
    function init() {
        initEditFormValid();
        flag = "${pageType}";
        var active = $(".active");
        var html = "";
        if (flag == "add") {
            html = "增加";
            $("#idGroup").hide();
        } else {
            $("#idGroup").hide();
            $("#form_code").attr("readonly", true);
            html = "编辑";

        }
        active.append(html);
    }
    function showcreateDate() {
        if ($("#form_createDate").val() == "" || $("#form_createDate").val() == null)
            $("#isShowCreteDate").hide();
    }
    function saveCashier() {
        $('#editForm').data('bootstrapValidator').validate();
        if (!$("#editForm").data("bootstrapValidator").isValid()) {
            return
        }
        if ($("#form_ownerId").val() == "") {
            bootbox.alert("请选择所属门店");
            return
        }
        cs.showProgressBar();
        debugger;
        $.post(basePath + "/shop/cashier/save.do?flag="+flag, $("#editForm")
            .serialize(), function (result) {
            debugger;
            cs.closeProgressBar();
            if (result.success == true || result.success == 'true') {
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                $("#form_id").val(result.id);
                history.back(-1);
            } else {
                cs.showAlertMsgBox(result.msg);

            }
        }, 'json');

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
                name: {
                    validators: {
                        notEmpty: {//非空验证：提示消息
                            message: '姓名为空'
                        }
                    }
                },
                code: {
                    validators: {
                        notEmpty: {
                            message: "登陆名为空"
                        }
                    }
                },
                phone: {
                    validators: {
                        notEmpty: {
                            message: "手机号为空"
                        },
                        stringLength: {
                            min: 11,
                            max: 11,
                            message: '请输入11位手机号码'
                        },
                        regexp: {
                            regexp: /^1[2|3|4|5|6|7|8]{1}[0-9]{9}$/,
                            message: '请输入正确的手机号码'
                        }
                    }
                },
                password: {
                    validators: {
                        notEmpty: {
                            message: "密码为空"
                        },
                        stringLength: {
                            min: 6,
                            max: 16,
                            message: '密码长度必须在6到16之间'
                        },
                        regexp: {
                            regexp: /^[a-zA-Z0-9_]+$/,
                            message: "密码只能是数字和字母下划线"
                        }
                    }
                }
            }

        });
    }
</script>
</body>
</html>