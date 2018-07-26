<%@ page import="java.util.*" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    //协议+“：//”+服务器ip或域名+':'+端口+上下文
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script>
        var basePath = "<%=basePath%>";
        var pageType = "${pageType}";
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
        <div class="row">
            <div class="col-xs-12">
                <div class="widget-box widget-color-blue  light-border">
                    <div class="widget-header">
                        <h5 class="widget-title">客户信息</h5>
                        <div class="widget-toolbar no-border">
                            <a   class="btn btn-xs bigger btn-yellow dropdown-toggle"  href="<%=basePath%>/${mainUrl}">
                                <i class="ace-icon fa fa-arrow-left"></i> 返回
                            </a>
                        </div>
                    </div>
                    <div class="widget-main pading-12">
                        <form class="form-horizontal" role="form" id="editForm">
                            <div class="form-group">
                                <input name="id" value="${customer.id}" id="edit_id" hidden/>
                                <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                       for="edit_name">
                                    <span style="color: #ff0000;">*</span>
                                    姓名</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <input class="form-control" id="edit_name" name="name"
                                           value="${customer.name}"/>
                                </div>
                                <label class="col-xs-4 col-sm-4 col-md-3 col-lg-3 control-label text-right"
                                       for="edit_status">启用</label>
                                <div class="col-xs-8 col-sm-8 col-md-2 col-lg-2">
                                    <select class="chosen-select form-control" id="edit_status" name="status">
                                        <option value="1">启用</option>
                                        <option value="0">停用</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                       for="edit_sex">性别</label>
                                <div class="col-xs-8 col-sm-8 col-md-2 col-lg-2 control-group">
                                    <div class="radio" id="edit_sex">
                                        <label><input name="sex" type="radio" value="1"/>男</label>
                                        <label><input name="sex" type="radio" value="0"/>女</label>
                                    </div>
                                </div>

                                <label class="col-xs-4 col-sm-4 col-md-4 col-lg-4 control-label text-right"
                                       for="edit_birthDay">生日</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <input class="form-control date-picker" id="edit_birthDay" name="birthDay"
                                           value='<fmt:formatDate value="${customer.birthDay}" pattern="yyyy-MM-dd"/>'
                                           data-date-format="yyyy-mm-dd"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-4 col-sm-4 col-md-1 control-label text-right"
                                       for="edit_phone"><span style="color: #ff0000;">*</span>手机</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <input class="form-control" id="edit_phone" name="phone"
                                           value="${customer.phone}"/>
                                </div>
                                <label class="col-xs-4 col-sm-4 col-md-3 col-lg-3 control-label text-right"
                                       for="edit_socialNo">身份证</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <input class="form-control" value="${customer.socialNo}" name="socialNo"
                                           id="edit_socialNo"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 text-right control-label"
                                       for="edit_email">邮箱</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <input class="form-control" value="${customer.email}" name="email"
                                           id="edit_email"/>
                                </div>
                                <label class="col-xs-4 col-sm-4 col-md-3 col-lg-3 control-label text-right"
                                       for="edit_createTime">建立时间</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <input class="form-control date-picker" name="createTime"
                                           value='<fmt:formatDate value="${customer.createTime}" pattern="yyyy-MM-dd"/>'
                                           data-date-format="yyyy-mm-dd" id="edit_createTime"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 text-right control-label"
                                       for="edit_saveMon">储值金额</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <input class="form-control" value="${customer.saveMon}" name="saveMon"
                                           id="edit_saveMon"/>
                                </div>
                                <label class="col-xs-4 col-sm-4 col-md-3 col-lg-3 control-label text-right"
                                       for="edit_zk"><span style="color: #ff0000;">*</span> 默认折扣(%)</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <input class="form-control" value="${customer.zk}" name="zk"
                                           id="edit_zk"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 text-right control-label"
                                       for="edit_ownerId"><span style="color: #ff0000;">*</span>所属方</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <div class="input-group">
                                        <input class="form-control" id="edit_ownerId"
                                               type="text" name="ownerId" readonly value="${customer.ownerId}"/>
                                        <span class="input-group-btn">
																			<button class="btn btn-sm btn-default"
                                                                                    id="setOwnerId"
                                                                                    type="button"
                                                                                    onclick="openUnitDialog('#edit_ownerId','#edit_unitName',null,'withShop')">
																				<i class="ace-icon fa fa-list"></i>
																			</button>

																		</span>
                                        <input class="form-control" id="edit_unitName"
                                               type="text" name="unitName" value="${customer.unitName}" readonly/>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 text-right control-label"
                                       for="edit_job">工作</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <input class="form-control" value="${customer.job}" name="job"
                                           id="edit_job"/>
                                </div>
                                <label class="col-xs-4 col-sm-4 col-md-3 col-lg-3 control-label text-right"
                                       for="edit_company">公司</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <input class="form-control" value="${customer.company}" name="company"
                                           id="edit_company"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                       for="edit_remark">备注</label>
                                <label class="col-xs-8 col-sm-8 col-md-9 col-lg-9">
                                            <textarea class="form-control limited" name="remark"
                                                      id="edit_remark">${customer.remark}</textarea>
                                </label>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-5 col-sm-12">
                                    <button class="btn btn-primary" type="button" onclick="saveGuest()">
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
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="../base/unit_dialog_hy.jsp"></jsp:include>
<script>
    function initData() {
        debugger;
        if(""=="${customer.sex}") {
            $("#edit_sex input:radio[value='0']").attr("checked", "checked");
        }
        else{
            $("#edit_sex input:radio[value=${customer.sex}]").attr("checked", "checked");
        }
        $("#edit_status").find("option[value=${customer.status}]").attr("selected", true);
        console.log("init over");
    }
</script>
<script src="<%=basePath%>/views/shop/customer_edit_Controller.js"></script>

</body>
</html>
