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
                                        <button class="btn btn-info" onclick="showAdvSearchPanel();">
                                            <i class="ace-icon fa fa-binoculars"></i>
                                            <span class="bigger-110">高级查询</span>
                                        </button>

                                    </div>

                                </div>
                            </div>
                            <div class="widget-main" id="searchPanel" style="display:none;">
                                <form class="form-horizontal" role="form" id="searchForm">
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="filter_IN_floorAreaParentCode">店仓</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select id="filter_IN_floorAreaParentCode" name="filter_IN_floorAreaParentCode"
                                                    multiple="multiple" data-placeholder="仓库列表">
                                            </select>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="filter_CONTAINS_floorAreaBarcode">区</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="filter_CONTAINS_floorAreaBarcode"
                                                   name="filter_CONTAINS_floorAreaBarcode" type="text"
                                                   placeholder="模糊查询"/>
                                        </div>


                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="filter_CONTAINS_floorBarcode">库位</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="filter_CONTAINS_floorBarcode"
                                                   name="filter_CONTAINS_floorBarcode" type="text"
                                                   placeholder="模糊查询"/>
                                        </div>
                                    </div>


                                    <div class="form-group">
                                        <div class="col-sm-offset-5 col-sm-10">
                                            <button type="button" class="btn btn-sm btn-primary" onclick="search();">
                                                <i class="ace-icon fa fa-search"></i>
                                                <span class="bigger-110">查询</span>
                                            </button>
                                            <button type="reset" class="btn btn-sm btn-warning" onclick="resetData();">
                                                <i class="ace-icon fa fa-undo"></i>
                                                <span class="bigger-110">清空</span></button>
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
<script type="text/javascript" src="<%=basePath%>/views/wms/pl/plWmsWarehouseBindingRelationController.js"></script>
</body>
</html>