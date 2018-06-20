<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017-06-21
  Time: 下午 2:26
  To change this template use File | Settings | File Templates.
--%>
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
        var curOwnerId="${ownerId}";
        var userId="${userId}";
        var linkman="${guest.linkman}";
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
                            <button class="btn btn-xs bigger btn-yellow dropdown-toggle"
                                    onclick="history.back(-1);">
                                <i class="ace-icon fa fa-arrow-left"></i>
                                返回
                            </button>
                        </div>
                    </div>
                    <div class="widget-main pading-12">
                        <form class="form-horizontal" role="form" id="editForm" onkeydown="if(event.keyCode==13)return false;">
                            <div class="form-group">
                                <input name="id" value="${guest.id}" id="edit_id" hidden/>
                                <input name="preType" value="${guest.type}"hidden/>
                                <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                       for="edit_name">
                                    <span style="color: #ff0000;">*</span>
                                    姓名</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <input class="form-control" id="edit_name" name="name"
                                           value="${guest.name}"/>
                                </div>

                                <label class="col-xs-4 col-sm-4 col-md-3 col-lg-3 control-label text-right"
                                       for="edit_type">客户类型</label>
                                <div class="col-xs-8 col-sm-8 col-md-2 col-lg-2">
                                    <select class="chosen-select form-control" id="edit_type" name="type" onchange="typeChange()">
                                        <option value="6">零售客户</option>
                                        <option value="2">省代客户</option>
                                        <option value="4">门店客户</option>
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
                                       for="edit_birth">生日</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <input class="form-control date-picker" id="edit_birth" name="birth"
                                           value='<fmt:formatDate value="${guest.birth}" pattern="yyyy-MM-dd"/>'
                                           data-date-format="yyyy-mm-dd"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                       for="edit_status">启用</label>
                                <div class="col-xs-8 col-sm-8 col-md-1 col-lg-1">
                                    <select class="chosen-select form-control" id="edit_status" name="status">
                                        <option value="1">启用</option>
                                        <option value="0">停用</option>
                                    </select>
                                </div>

                                <label class="col-xs-4 col-sm-4 col-md-5 col-lg-5 control-label text-right"
                                       for="edit_ownerId"><span style="color: #ff0000;">*</span>所属方</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <div class="input-group">
                                        <input class="form-control" id="edit_ownerId"
                                               type="text" name="ownerId" readonly value="${guest.ownerId}"/>
                                        <span class="input-group-btn">
																			<button class="btn btn-sm btn-default"
                                                                                    id="setOwnerId"
                                                                                    type="button"
                                                                                    onclick="openUnitDialog('#edit_ownerId','#edit_unitName',null,'withShop')">
																				<i class="ace-icon fa fa-list"></i>
																			</button>

																		</span>
                                        <input class="form-control" id="edit_unitName"
                                               type="text" name="unitName" value="${guest.unitName}" readonly/>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">


                                <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                       for="edit_tel"><span style="color: #ff0000;">*</span>电话</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <input class="form-control" value="${guest.tel}" name="tel" id="edit_tel"/>
                                </div>

                                <label class="col-xs-4 col-sm-4 col-md-3 control-label text-right"
                                       for="edit_phone">手机</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <input class="form-control" id="edit_phone" name="phone"
                                           value="${guest.phone}"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 text-right control-label"
                                       for="edit_email">邮箱</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <input class="form-control" value="${guest.email}" name="email"
                                           id="edit_email"/>
                                </div>

                                <label class="col-xs-4 col-sm-4 col-md-3 col-lg-3 control-label text-right"
                                       for="edit_fax">传真</label>
                                <div class="col-xs-8 col-md-8 col-md-3 col-lg-3">
                                    <input class="form-control" name="fax" id="edit_fax" name="fax"
                                           value="${guest.fax}"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 text-right control-label"
                                       for="edit_bankCode">银行账户</label>
                                <div class="col-xs-8 col-md-8 col-md-3 col-lg-3">
                                    <input class="form-control" name="bankCode" id="edit_bankCode"
                                           value="${guest.bankCode}"/>
                                </div>

                                <label class="col-xs-4 col-sm-4 col-md-3 col-lg-3 text-right control-label"
                                       for="edit_bankAccount">银行账号</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <input class="form-control" id="edit_bankAccount" name="bankAccount"
                                           value="${guest.bankAccount}"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 text-right control-label"
                                       for="edit_depositBank">开户行</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <input class="form-control" name="depositBank" value="${guest.depositBank}"
                                           id="edit_depositBank"/>
                                </div>

                                <label class="col-xs-4 col-sm-4 col-md-3 col-lg-3 control-label text-right"
                                       for="edit_discount">
                                    <span style="color: #ff0000;">*</span>
                                    默认折扣(%)</label>
                                <div class="col-xs-8 col-sm-8 col-md-1 col-lg-1">
                                    <input class="form-control" id="edit_discount" name="discount"
                                           value="${guest.discount}"/>
                                </div>
                            </div>


                            <div class="form-group">
                                <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                       for="edit_linkman">
                                    <span style="color:#ff0000">*</span>
                                    联系人</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col0lg-3">
                                    <%--<input id="edit_linkman" class="form-control" name="linkman"
                                           value="${guest.linkman}"/>--%>
                                    <%--<select id="edit_linkman" class="form-control" name="linkman" value="${guest.linkman}"></select>--%>
                                        <select class="form-control selectpicker show-tick" id="edit_linkman" name="linkman" style="width: 100%;" data-live-search="true"></select>
                                </div>
                                <label class="col-xs-4 col-sm-4 col-md-3 col-lg-3 control-label text-right"
                                       for="edit_createTime">建立时间</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <input class="form-control date-picker" name="createTime"
                                           value='<fmt:formatDate value="${guest.createTime}" pattern="yyyy-MM-dd"/>'
                                           data-date-format="yyyy-mm-dd" id="edit_createTime"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                       for="edit_storedValue">储值金额</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <input class="form-control" id="edit_storedValue" name=""
                                           value="${guest.storedValue}" readonly/>
                                </div>
                                <label class="col-xs-4 col-sm-4 col-md-3 col-lg-3 control-label text-right"
                                       for="edit_owingValue">欠款金额</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <input class="form-control" id="edit_owingValue" name=""
                                           value="${guest.owingValue}" readonly/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                       for="edit_storeDate">储值日期</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <input class="form-control date-picker"
                                           value='<fmt:formatDate value="${guest.storeDate}" pattern="yyyy-MM-dd"/>'
                                           id="edit_storeDate" name="storeDate" data-date-format="yyyy-mm-dd"
                                           id="edit_storeDate"/>
                                </div>
                                <label class="col-xs-4 col-sm-4 col-md-3 col-lg-3 control-label text-right"
                                       for="edit_storeDate">VIP信息</label>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <input class="form-control" id="edit_vipId" name="vipId"
                                           value="${guest.vipId}"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                       for="edit_province">地址</label>
                                <div class="col-xs-8 col-sm-8 col-md-2 col-lg-2">
                                    <input class="form-control" value="${guest.province}" id="edit_province"
                                           name="province" placeholder="省"/>
                                </div>
                                <div class="col-xs-8 col-sm-8 col-md-2 col-lg-2">
                                    <input class="form-control" value="${guest.city}" name="city" id="edit_city"
                                           placeholder="市"/>
                                </div>
                                <div class="col-xs-8 col-sm-8 col-md-2 col-lg-2">
                                    <input class="form-control" value="${guest.areaId}" name="areaId"
                                           id="edit_areaId" placeholder="区/县"/>
                                </div>
                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                    <input class="form-control" value="${guest.address}" name="address"
                                           id="edit_adress" placeholder="详细地址"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                       for="edit_remark">备注</label>
                                <label class="col-xs-8 col-sm-8 col-md-9 col-lg-9">
                                            <textarea class="form-control limited" name="remark"
                                                      id="edit_remark">${guest.remark}</textarea>
                                </label>
                            </div>
                            <div class="form-group">
                                <div id="guest_edit_buttongroup" class="col-sm-offset-5 col-sm-12">
                                    <%--<button id="save_guest_button" class="btn btn-primary" type="button" onclick="saveGuest()">
                                        <i class="ace-icon fa fa-save"></i> <span class="bigger-110">保存</span>
                                    </button>
                                    <button id ="upadate_guest_button"class="btn btn-primary" type="button" onclick="updateGuest()">
                                        <i class="ace-icon fa fa-save"></i> <span class="bigger-110">保存</span>
                                    </button>--%>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>
</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="../base/unit_dialog.jsp"></jsp:include>
<script>
    $(function () {
        keydowns();
        if (userId=="admin"){
        }else {
            $("#edit_type").attr("disabled",true);
        }
    });
    function initData() {
        if(""=="${guest.sex}")
            $("#edit_sex input:radio[value='0']").attr("checked", "checked");
        else
            $("#edit_sex input:radio[value=${guest.sex}]").attr("checked", "checked");
        $("#edit_type").find("option[value=${guest.type}]").attr("selected", true);
        $("#edit_status").find("option[value=${guest.status}]").attr("selected", true);
        if (linkman!=""){
            $("#edit_linkman").find("option[value="+linkman+"]").attr("selected", true);
        }
        console.log("init over");
    }
    function keydowns() {
        //监听回车键
        $("#edit_vipId").keydown(function (event) {

            if (event.keyCode == 13) {
                var vipId=$("#edit_vipId").val();
                if(vipId!=""&&vipId!=undefined){
                    $("#edit_vipId").attr("readonly", "true");
                }

            }
        })
    }
</script>
<script src="<%=basePath%>/views/sys/guest_edit_controller.js"></script>

</body>
</html>
