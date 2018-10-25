<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
        var pageType = "${pageType}";
        var userId = "${userId}";
        var billNo = "${labelChangeBill.billNo}";
        var ownersId = "${ownersId}";
        var beforeclass9 = "${labelChangeBill.beforeclass9}";
        var origId = "${labelChangeBill.origId}";
        var nowclass9 = "${labelChangeBill.nowclass9}";
        var changeType = "${labelChangeBill.changeType}";
        var type = "${type}";
        var defaultWarehId = "${defaultWarehId}";
    </script>
</head>
<body class="no-skin">

<div class="modal fade" id="loadingModal">
    <div style="width: 200px;height:20px; z-index: 20000; position: absolute; text-align: center; left: 50%; top: 50%;margin-left:-100px;margin-top:-10px">
        <div class="progress progress-striped active" style="margin-bottom: 0;">
            <div class="progress-bar" style="width: 100%;"></div>
        </div>
        <h5>正在加载...</h5>
    </div>

</div>

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
                        <h5 class="widget-title">基本信息</h5>
                        <div class="widget-toolbar no-border">

                            <a class="btn btn-xs bigger btn-yellow dropdown-toggle" href="<%=basePath%>/${mainUrl}">
                                <i class="ace-icon fa fa-arrow-left"></i> 返回
                            </a>
                        </div>
                    </div>
                    <div class="widget-body">
                        <div class="widget-main padding-12">
                            <form id="editForm" class="form-horizontal" role="form"
                                  onkeydown="if(event.keyCode==13)return false;">
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="search_billNo">单据编号</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_billNo" name="billNo"
                                               type="text" readOnly value="${labelChangeBill.billNo}"/>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_billDate">单据日期</label>
                                    <div class="col-xs-2">
                                        <input class="form-control date-picker" id="search_billDate" name="billDate"
                                               type="text" value="${labelChangeBill.billDate}"/>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_beforeclass9">原系列</label>
                                    <div class="col-xs-2">
                                        <select class="form-control" id="search_beforeclass9" name="beforeclass9"
                                                style="width: 100%;" value="${labelChangeBill.beforeclass9}">
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group">


                                    <%-- <label class="col-xs-1 control-label" for="search_destId">入库仓库</label>
                                     <div class="col-xs-2">
                                         <select class="form-control" id="search_destId" name="destId"
                                                 style="width: 100%;" value="${LabelChangeBill.destId}">
                                         </select>
                                     </div>--%>
                                    <label class="col-xs-1 control-label" for="search_origId">仓库</label>
                                    <div class="col-xs-2">
                                        <select class="form-control" id="search_origId" name="origId"
                                                style="width: 100%;" value="${labelChangeBill.origId}">
                                        </select>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_nowclass9">现系列</label>
                                    <div class="col-xs-2">
                                        <select class="form-control" id="search_nowclass9" name="nowclass9"
                                                style="width: 100%;" value="${labelChangeBill.beforeclass9}">
                                        </select>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_prefix">货号前缀</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_prefix" name="prefix"
                                               value="${labelChangeBill.prefix}">
                                        </input>
                                    </div>
                                </div>
                                <div class="form-group">

                                    <label class="col-xs-1 control-label" for="search_discount">整单折扣</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_discount" name="discount"
                                               value="${labelChangeBill.discount}" onblur="search_discount_onblur()"
                                               onkeyup="value=value.replace(/[^\d]/g,'')">
                                        </input>
                                    </div>
                                    <label class="col-xs-1 control-label" for="select_changeType">转变类型</label>
                                    <div class="col-xs-2">
                                        <select class="form-control" id="select_changeType"
                                                name="changeType" value="${labelChangeBill.changeType}">
                                            <option value="">--请选择--</option>
                                            <option value="CS">系列转变</option>
                                            <option value="PC">打折</option>
                                            <option value="TG">商场特供</option>
                                            <option value="ID">货号转换</option>
                                        </select>
                                    </div>
                                    <label class="col-xs-1 control-label" for="search_suffix">货号后缀</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="search_suffix" name="suffix"
                                               value="${labelChangeBill.suffix}">
                                        </input>
                                    </div>

                                </div>
                                <div class="form-group">
                                    <label class="col-xs-1 control-label"
                                           for="form_remark">备注</label>

                                    <div class="col-xs-9 col-sm-9">
                                            <textarea maxlength="400" class="form-control" id="form_remark"
                                                      name="remark">${labelChangeBill.remark}</textarea>
                                    </div>
                                </div>
                                <div>
                                    <input id="search_status" name="status" value="${labelChangeBill.status}"
                                           type="hidden">
                                    </input>
                                    <input id="search_id" name="id" value="${labelChangeBill.id}"
                                           type="hidden">
                                    </input>

                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="hr hr4"></div>


    <div class="widget-box transparent">
        <div class="widget-header ">
            <h4 class="widget-title lighter">单据明细</h4>
            <div class="widget-toolbar no-border">
                <ul class="nav nav-tabs" id="myTab">
                    <li class="active">
                        <a data-toggle="tab" href="#detail">SKU明细</a>
                    </li>
                </ul>
            </div>
        </div>
        <div class="widget-body">
            <div class="widget-main padding-12 no-padding-left no-padding-right">
                <div class="tab-content padding-4">
                    <div id="addDetail" class="tab-pane in active" style="height:80%;">
                        <table id="addDetailgrid"></table>
                        <div id="addDetailgrid-pager"></div>
                    </div>
                </div>
                <div id="buttonGroup" style="margin-left: 10%" class="col-sm-offset-4 col-sm-10 ">
                </div>
                <%-- <div id="buttonGroupfindWxshop" style="margin-top: 20px" class="col-sm-offset-4 col-sm-10 ">
                 </div>--%>

            </div>
        </div>
    </div>

</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="add_detail_dialog.jsp"></jsp:include>
<jsp:include page="add_uniqCode_dialog.jsp"></jsp:include>
<jsp:include page="uniqueCode_detail_list.jsp"></jsp:include>
<jsp:include page="exchageGood_dialog.jsp"></jsp:include>
<jsp:include page="findRetrunNo.jsp"></jsp:include>
<jsp:include page="findWxShop.jsp"></jsp:include>
<jsp:include page="sendStreamNO.jsp"></jsp:include>
<jsp:include page="findBirthNo.jsp"></jsp:include>
<jsp:include page="../base/waitingPage.jsp"></jsp:include>
<jsp:include page="../base/search_guest_dialog.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/logistics/labelChangeBillDelController.js"></script>
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/dateFormatUtil.js"></script>

<script type="text/javascript">

</script>
</body>
</html>