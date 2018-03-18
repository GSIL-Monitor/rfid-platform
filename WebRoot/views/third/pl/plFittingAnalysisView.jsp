<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../../baseView.jsp"></jsp:include>
    <jsp:include page="../../search/searchBaseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>
   <style>
    .proDetails .col-lg-12, #detail .col-xs-12,#detail .col-md-6{
        padding:0;
    }
       .yellow{
           color: #DAA520;
       }
       .jacinth{
           color:orangered;
       }
       .red{
           color: #fa0002;
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
                                        <button class="btn btn-info" onclick="refresh();">
                                            <i class="ace-icon fa fa-refresh"></i>
                                            <span class="bigger-110">刷新</span>
                                        </button>
                                    </div>

                                    <div class="btn-group btn-group-sm pull-left">

                                        <button class="btn btn-info" onclick="exportExcel();">

                                            <i class="ace-icon fa fa-file-excel-o"></i>
                                            <span class="bigger-110">导出</span>

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
                                               for="filter_EQ_stockDay">日期</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <div class="input-group">
                                                <input class="form-control date-picker" id="filter_EQ_stockDay"
                                                       name="filter_EQ_stockDay" type="text"
                                                       data-date-format="yyyy-mm-dd"/>
																	<span class="input-group-addon">
																		<i class="fa fa-calendar bigger-110"></i>
																	</span>
                                            </div>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="filter_IN_warehId">店仓编号</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select id="filter_IN_warehId" name="filter_IN_stockCode"
                                                    multiple="multiple" data-placeholder="编号列表">
                                            </select>
                                        </div>

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="filter_eq_styleId">款号</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <div class="input-group">
                                                <input class="form-control" id="filter_EQ_styleId"
                                                       type="text" name="filter_EQ_styleId" readonly/>
                                                     <span class="input-group-btn"
                                                           onclick="openstyleDialog('#filter_EQ_styleId','#filter_EQ_styleName',null)">
                                                         <button class="btn btn-sm btn-default" type="button">
                                                             <i class="ace-icon fa fa-list"></i>
                                                         </button>
								                      </span>
                                                <input class="form-control" id="filter_EQ_styleName"
                                                       type="text" name="filter_EQ_styleName" readonly
                                                       placeholder="款名"/>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="filter_CONTAINS_colorId">色号</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="filter_CONTAINS_colorId"
                                                   name="filter_CONTAINS_colorId" type="text"
                                                   placeholder="模糊查询"/>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="filter_CONTAINS_sizeId">尺号</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="filter_CONTAINS_sizeId"
                                                   name="filter_CONTAINS_sizeId" type="text"
                                                   placeholder="模糊查询"/>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="filter_CONTAINS_class1">品牌</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select id="filter_CONTAINS_class1" name="filter_CONTAINS_class1"
                                                    multiple="multiple" data-placeholder="品牌列表">
                                            </select>
                                        </div>


                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="filter_CONTAINS_class2">年份</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select id="filter_CONTAINS_class2" name="filter_CONTAINS_class2"
                                                    multiple="multiple" data-placeholder="年份列表">
                                            </select>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="filter_CONTAINS_class3">大类</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select id="filter_CONTAINS_class3" name="filter_CONTAINS_class3"
                                                    multiple="multiple" data-placeholder="大类列表">
                                            </select>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="filter_CONTAINS_class4">小类</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select id="filter_CONTAINS_class4" name="filter_CONTAINS_class4"
                                                    multiple="multiple" data-placeholder="小类列表">
                                            </select>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="filter_CONTAINS_class10">季节</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select id="filter_CONTAINS_class10" name="filter_CONTAINS_class10"
                                                    multiple="multiple" data-placeholder="季节列表">
                                            </select>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label"
                                               for="lazyDay">试衣预警</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="chosen-select form-control" id="lazyDay" onchange="filterLazyDay()">
                                                <option>--请选择--</option>
                                                <option value="3">I级</option>
                                                <option value="7">II级</option>
                                                <option value="14">III级</option>
                                            </select>
                                        </div>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3" style="display:none;">
                                            <input class="form-control" name="filter_GT_lazyDays" id="filter_GT_lazyDays" type="text"/>
                                            <input class="form-control" name="filter_LTE_lazyDays" id="filter_LTE_lazyDays" type="text"/>
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
                        <div id="saleStock" style="height:500px"></div>

                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="../../layout/footer.jsp"></jsp:include>

</div>
<script type="text/x-kendo-template" id="template">
    <div class="tabstrip">
        <ul>
            <li class="k-state-active">
                试衣明细
            </li>
            <li>
                商品信息
            </li>
        </ul>
        <div>
            <div class="row" id="detail">
                <div class="col-xs-12">
                    <div class="col-md-6">
                    <div class="fitDetail">
                    </div>
                    </div>
                </div>
            </div>
        </div>
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
</body>
</html>

<jsp:include page="../../layout/footer_js.jsp"></jsp:include>
<jsp:include page="../../base/style_dialog.jsp"></jsp:include>
<link href="<%=basePath%>/kendoUI/styles/kendo.common-material.min.css" rel="stylesheet">
<link href="<%=basePath%>/kendoUI/styles/kendo.rtl.min.css" rel="stylesheet">
<link href="<%=basePath%>/kendoUI/styles/kendo.material.min.css" rel="stylesheet">
<script src="<%=basePath%>/kendoUI/js/kendo.all.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/kendo.timezones.min.js"></script>

<script src="<%=basePath%>/kendoUI/js/cultures/kendo.culture.zh-CN.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/messages/kendo.messages.zh-CN.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/views/third/pl/plFittingAnalysisViewController.js"></script>
<jsp:include page="../../search/search_js.jsp"></jsp:include>

</body>
</html>
