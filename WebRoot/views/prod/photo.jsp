<%@ page pageEncoding="UTF-8" import="java.util.*" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>

    <script>
        var basePath = "<%=basePath%>";
    </script>
    <style>
        #showAlbum {
            height: auto;
        }
        .file-drop-zone{
            height:auto;
        }
        .content {
            width: 100%;
            height: 100%;
            margin: 10px auto;
        }

        @media screen and (min-width: 980px) /* Desktop */ {
            .content {
                width: 70%;
            }
        }

        .mygallery {
            margin: 25px 0;
        }
    </style>
    <link rel="stylesheet" href="<%=basePath%>/Olive/plugin/waterfall/css/chromagallery.min.css">
    <script src="<%=basePath%>/Olive/plugin/waterfall/js/modernizr-chrg.min.js"></script>
    <script src="<%=basePath%>/Olive/plugin/waterfall/js/imagesloaded.min.js"></script>
    <script src="<%=basePath%>/Olive/plugin/waterfall/js/masonry.min.js"></script>

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
            <!-- /.page-header -->

            <div id="page-content">

                <div class="row">
                    <div class="col-xs-12">
                        <!-- PAGE CONTENT BEGINS -->
                        <div class="widget-body">

                            <div class="widget-toolbox padding-8 clearfix">
                                <div class="btn-toolbar" role="toolbar">
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-info" onclick="refresh()">
                                            <i class="ace-icon fa fa-refresh"></i>
                                            <span class="bigger-110">刷新</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-primary" onclick="addphoto()">
                                            <i class="ace-icon fa fa-plus"></i>
                                            <span class="bigger-110">添加</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-primary" onclick="deletephoto()">
                                            <i class="ace-icon fa fa-close"></i>
                                            <span class="bigger-110">删除</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">

                                        <button type="button" class="btn btn-primary" onclick="uploadStyles()">

                                            <i class="ace-icon fa fa-file-excel-o"></i>
                                            <span class="bigger-110">批量导入</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">

                                        <button type="button" class="btn btn-primary" onclick="uploadWithStyles()">

                                            <i class="ace-icon fa fa-file-excel-o"></i>
                                            <span class="bigger-110">款式导入</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-primary" onclick="showAlbum()">
                                            <i class="ace-icon fa fa-area-chart"></i>
                                            <span class="bigger-110">查看相册</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-primary" onclick="showDetails()">
                                            <i class="ace-icon fa fa-align-justify"></i>
                                            <span class="bigger-110">相册明细</span>
                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-right">

                                        <button type="button" class="btn btn-info"
                                                onclick="showAdvSearchPanel()">
                                            <i class="ace-icon fa fa-binoculars"></i><span
                                                class="bigger-110">高级查询</span>
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="widget-main" id="searchPannel" hidden>
                                <form class="form-horizontal" id="searchForm" role="form">

                                    <div class="form-group" align="center">
                                        <div class="form-group">
                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                                   for="filter_LIKES_styleId">款号</label>
                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                <input class="form-control" id="filter_LIKES_styleId"
                                                       name="filter_LIKES_styleId" type="text" placeholder="模糊查询"/>

                                            </div>
                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                                   for="filter_LIKES_colorId">颜色编号</label>
                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                <input class="form-control" id="filter_LIKES_colorId"
                                                       name="filter_LIKES_colorId" type="text" placeholder="模糊查询"/>

                                            </div>
                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right"
                                                   for="filter_LED_createTime">添加时间</label>
                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">

                                                <div class="input-group">
                                                    <input class="form-control date-picker"
                                                           id="filter_LED_createTime" type="text"
                                                           name="filter_LED_createTime" data-date-format="yyyy-mm-dd"/>

                                                    <span class="input-group-addon"> <i
                                                            class="fa fa-exchange"></i></span>
                                                    <input class="form-control date-picker" type="text"
                                                           class="input-sm form-control"
                                                           name="filter_GED_createTime"
                                                           data-date-format="yyyy-mm-dd"/>
                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                    <div class="form-group">
                                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-11 btnPosition">
                                            <button type="button" class="btn btn-sm btn-primary" onclick="_search()">
                                                <i class="ace-icon fa fa-search"></i>
                                                <span class="bigger-110">查询</span>
                                            </button>
                                            <button type="reset" class="btn btn-sm btn-warning">
                                                <i class="ace-icon fa fa-undo"></i>
                                                <span class="bigger-110">清空</span></button>
                                        </div>

                                    </div>
                                </form>
                            </div>
                        </div>
                        <div id="showDetail">
                            <table id="grid"></table>
                            <table id="gridPager"></table>
                        </div>
                        <div class="widget-main" style="overflow: scroll">
                            <div id="showAlbum">
                                <div class="chroma-gallery mygallery" id="albumn"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>


</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<script src="<%=basePath%>/Olive/plugin/waterfall/js/chromagallery.min.js"></script>
<script src="<%=basePath%>/Olive/plugin/waterfall/js/chromagallery.pkgd.min.js"></script>
<script src="<%=basePath%>/views/prod/photoController.js"></script>
<jsp:include page="upload_batch_photos.jsp"></jsp:include>
<jsp:include page="upload_singleFolder_photos.jsp"></jsp:include>
</body>
</html>
