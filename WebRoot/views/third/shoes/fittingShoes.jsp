
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
        .sortFirst{
            color: #75710e;
            font-style: italic;
        }
        .sortSecond{
            color: #6e2f29;
            font-style: italic
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
                                        <button class="btn btn-danger" onclick="clearData();">
                                            <i class="glyphicon glyphicon-trash"></i>
                                            <span class="bigger-110">清空</span>
                                        </button>
                                    </div>
                                </div>
                            </div>
                                 <div id="fittime" class="col-lg-1 col-xs-1" style="height: 300px;text-align:center;" >
                                    <div class="infobox infobox-green infobox-small infobox-dark" style="margin-top: 40px">
                                        <div class="infobox-data" id="sumYear">

                                        </div>
                                    </div>
                                    <div class="infobox infobox-blue infobox-small infobox-dark" >
                                        <div class="infobox-data" id="sumMonth">

                                        </div>
                                    </div>
                                    <div class="infobox infobox-grey infobox-small infobox-dark">
                                        <div class="infobox-data" id="sumWeek">

                                        </div>
                                    </div>
                                    <div class="infobox infobox-red infobox-small infobox-dark">
                                        <div class="infobox-data" id="sumDay">

                                        </div>
                                    </div>
                                </div>
                                <div id="class3" class="col-lg-2 col-xs-2" style="height: 300px" ></div>
                                <div id="class10" class="col-lg-2 col-xs-2" style="height: 300px"></div>
                            <div id="styleBar" class="col-lg-4 col-xs-4" style="height: 300px"></div>
                            <div id="colorBar" class="col-lg-3 col-xs-3" style="height: 300px"></div>

                        </div>
                    </div>
                </div>
                 <div class="row">
                    <div class="col-xs-12">
                        <div id="fittingGrid" style="height:600px"></div>
                    </div>
                   <%-- <div class="col-xs-6">
                        <div id="aggGrid" style="height:600px"></div>
                    </div>--%>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="../../layout/footer_js.jsp"></jsp:include>
    <div id="progressDialog"></div>
    <span id="notification"></span>
    <div id="alertDialog"></div>
</div>
<jsp:include page="../../layout/footer_js.jsp"></jsp:include>

 <link href="<%=basePath%>/kendoUI/styles/kendo.common-material.min.css" rel="stylesheet">
<link href="<%=basePath%>/kendoUI/styles/kendo.rtl.min.css" rel="stylesheet">
<link href="<%=basePath%>/kendoUI/styles/kendo.material.min.css" rel="stylesheet">
<script src="<%=basePath%>/kendoUI/js/kendo.all.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/kendo.timezones.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/cultures/kendo.culture.zh-CN.min.js"></script>
<script src="<%=basePath%>/kendoUI/js/messages/kendo.messages.zh-CN.min.js"></script>

<script type="text/javascript" src="<%=basePath%>/Olive/plugin/echarts/echarts.js"></script>
<script type="text/javascript" src="<%=basePath%>/Olive/plugin/echarts/shine.js"></script>
<script type="text/javascript" src="<%=basePath%>/views/third/shoes/fittingShoesController.js"></script>
<jsp:include page="../../search/search_js.jsp"></jsp:include>


</body>
</html>