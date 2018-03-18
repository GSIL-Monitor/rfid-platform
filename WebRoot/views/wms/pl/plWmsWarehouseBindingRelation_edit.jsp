<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../../search/searchBaseView.jsp"></jsp:include>
    <jsp:include page="../../baseView.jsp"/>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>
    <style>
        #productShow {
            padding: 0
        }
    </style>
</head>
<body class="no-skin">
<div class="main-container" id="main-container" style="overflow-x:hidden;">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>
    <div class="main-content">
        <div class="main-content-inner">
            <div id="page-content">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="widget-body ">

                            <div class="widget-toolbox padding-8 clearfix">
                                <div class="btn-toolbar" role="toolbar">
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-info" onclick="javascript:window.location.reload();">
                                            <i class="ace-icon fa fa-refresh"></i>
                                            <span class="bigger-110">刷新</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button type="button" class="btn btn-primary" onclick="unbind()">
                                            <i class="ace-icon fa fa-unlock"></i>
                                            <span class="bigger-110">解绑</span>
                                        </button>
                                        <button type="button" class="btn btn-primary" onclick="bind()">
                                            <i class="ace-icon fa fa-lock"></i>
                                            <span class="bigger-110">绑定</span>
                                        </button>
                                    </div>

                                    <div class="btn-group btn-group-sm pull-right">
                                        <button class="btn btn-info" onclick="javascript: window.location.href=basePath+'/views/wms/pl/plWmsWarehouseBindingRelation.jsp'">
                                            <i class="ace-icon fa fa-reply"></i>
                                            <span class="bigger-110">返回</span>
                                        </button>

                                    </div>

                                </div>
                            </div>
                            <div class="widget-main" id="searchPanel">
                                <form class="form-horizontal" role="form" id="editForm">
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="form_shopId">仓库</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="chosen-select  form-control" id="form_shopId" name="shopId"  onchange="selectShopId()">
                                            </select>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="form_floorAreaId">区</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="chosen-select  form-control" id="form_floorAreaId" name="floorAreaId" onchange="selectFloorAreaId()">
                                            </select>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="form_floorId">库位</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="chosen-select  form-control" id="form_floorId" name="floorId" >
                                            </select>
                                        </div>

                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="form_styleId">款式</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <div class="input-group">
                                                <input class="form-control" id="form_styleId" type="text" name="styleId" placeholder="款号" readonly/>
                                                <span class="input-group-btn" onclick="openstyleDialog('#form_styleId','#form_styleName',initSelect)">
                                                    <button class="btn btn-sm btn-default" type="button"><i class="ace-icon fa fa-list"></i></button>
                                                </span>
                                                <input class="form-control" id="form_styleName" type="text" placeholder="款名" readonly/>
                                            </div>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="form_colorId">颜色</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="chosen-select  form-control" id="form_colorId" name="colorId" >
                                            </select>
                                        </div>
                                    </div>

                                </form>
                            </div>
                        </div>

                        <div class="col-lg-4 col-xs-4" style="padding-left:0">
                            <div id="treelist"></div>
                        </div>
                        <div class="col-lg-8 col-xs-8" style="padding-right:0">
                            <div id="grid"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="../../search/search_js.jsp"></jsp:include>
    <jsp:include page="../../layout/footer.jsp"></jsp:include>

</div>
<script type="text/x-kendo-template" id="template">
    <div class="tabstrip">
        <ul>
            <li class="k-state-active">
                商品信息
            </li>
        </ul>

        <div>
            <div class="#= styleId #">
                <ul>


                </ul>
            </div>
        </div>
    </div>
</script>

<div class="modal fade" id="imgModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">

        <img id="bigImg"/>

    </div>
</div>
<jsp:include page="../../layout/footer_js.jsp"></jsp:include>
<link href="<%=basePath%>/kendoUI/styles/kendo.common-material.min.css" rel="stylesheet">
<link href="<%=basePath%>/kendoUI/styles/kendo.rtl.min.css" rel="stylesheet">
<link href="<%=basePath%>/kendoUI/styles/kendo.material.min.css" rel="stylesheet">
<script src="<%=basePath%>/kendoUI/js/kendo.all.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/kendo.timezones.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/cultures/kendo.culture.zh-CN.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/messages/kendo.messages.zh-CN.min.js"></script>

<jsp:include page="../../base/style_dialog.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/wms/pl/plWmsWarehouseBindingRelation_edit.js"></script>
</body>
</html>