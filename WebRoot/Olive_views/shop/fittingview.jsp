<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

    <!DOCTYPE html>
    <html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
        <meta charset="utf-8" />
        <title>CaseSoft RFID大数据平台</title>

        <meta name="description" content="overview &amp; stats" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

        <!-- bootstrap & fontawesome -->
        <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/bootstrap.css" />
        <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/font-awesome.css" />

        <!-- page specific plugin styles -->

        <!-- text fonts -->
        <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/ace-fonts.css" />
        <link rel="stylesheet" href="<%=basePath%>Olive//assets/js/jqGrid/css/ui.jqgrid.css" />
        <!-- ace styles -->
        <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/ace.css" class="ace-main-stylesheet" id="main-ace-style" />

        <!--[if lte IE 9]>
        <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/ace-part2.css" class="ace-main-stylesheet" />
        <![endif]-->

        <!--[if lte IE 9]>
        <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/ace-ie.css" />
        <![endif]-->

        <!-- inline styles related to this page -->

        <!-- ace settings handler -->
        <script src="<%=basePath%>Olive/assets/js/ace-extra.js"></script>

        <!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

        <!--[if lte IE 8]>
        <script src="<%=basePath%>Olive/assets/js/html5shiv.js"></script>
        <script src="<%=basePath%>Olive/assets/js/respond.js"></script>
        <![endif]-->
    </head>

    <body class="no-skin">
    <!-- #section:basics/navbar.layout -->
    <div id="navbar" class="navbar navbar-default">
        <script type="text/javascript">
            try{ace.settings.check('navbar' , 'fixed')}catch(e){}
        </script>

        <div class="navbar-container" id="navbar-container">
            <!-- #section:basics/sidebar.mobile.toggle -->
            <button type="button" class="navbar-toggle menu-toggler pull-left" id="menu-toggler" data-target="#sidebar">
                <span class="sr-only">Toggle sidebar</span>

                <span class="icon-bar"></span>

                <span class="icon-bar"></span>

                <span class="icon-bar"></span>
            </button>

            <!-- /section:basics/sidebar.mobile.toggle -->
            <div class="navbar-header pull-left">
                <!-- #section:basics/navbar.layout.brand -->
                <a href="#" class="navbar-brand">
                    <small>
                        <i class="fa fa-cloud"></i>
                        RFID大数据平台
                    </small>
                </a>

                <!-- /section:basics/navbar.layout.brand -->

                <!-- #section:basics/navbar.toggle -->

                <!-- /section:basics/navbar.toggle -->
            </div>

            <!-- #section:basics/navbar.dropdown -->
            <div class="navbar-buttons navbar-header pull-right" role="navigation">
                <ul class="nav ace-nav">


                    <!-- #section:basics/navbar.user_menu -->
                    <li class="light-blue">
                        <a data-toggle="dropdown" href="#" class="dropdown-toggle">
                            <img class="nav-user-photo" src="<%=basePath%>Olive/assets/avatars/user.jpg" alt="Jason's Photo" />
								<span class="user-info">
									<small>Welcome,</small>
									B001
								</span>

                            <i class="ace-icon fa fa-caret-down"></i>
                        </a>

                        <ul class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
                            <li>
                                <a href="#">
                                    <i class="ace-icon fa fa-cog"></i>
                                    修改密码
                                </a>
                            </li>

                            <li>
                                <a href="profile.html">
                                    <i class="ace-icon fa fa-user"></i>
                                    用户信息
                                </a>
                            </li>

                            <li class="divider"></li>

                            <li>
                                <a href="#">
                                    <i class="ace-icon fa fa-power-off"></i>
                                    注销
                                </a>
                            </li>
                        </ul>
                    </li>

                    <!-- /section:basics/navbar.user_menu -->
                </ul>
            </div>

            <!-- /section:basics/navbar.dropdown -->
        </div><!-- /.navbar-container -->
    </div>

    <!-- /section:basics/navbar.layout -->
    <div class="main-container" id="main-container">
        <script type="text/javascript">
            try{ace.settings.check('main-container' , 'fixed')}catch(e){}
        </script>

        <!-- #section:basics/sidebar -->
        <div id="sidebar" class="sidebar                  responsive">
            <script type="text/javascript">
                try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
            </script>

            <div class="sidebar-shortcuts" id="sidebar-shortcuts">
                <div class="sidebar-shortcuts-large" id="sidebar-shortcuts-large">
                    <button class="btn btn-success">
                        <i class="ace-icon fa fa-signal"></i>
                    </button>

                    <button class="btn btn-info">
                        <i class="ace-icon fa fa-pencil"></i>
                    </button>

                    <!-- #section:basics/sidebar.layout.shortcuts -->
                    <button class="btn btn-warning">
                        <i class="ace-icon fa fa-users"></i>
                    </button>

                    <button class="btn btn-danger">
                        <i class="ace-icon fa fa-cogs"></i>
                    </button>

                    <!-- /section:basics/sidebar.layout.shortcuts -->
                </div>

                <div class="sidebar-shortcuts-mini" id="sidebar-shortcuts-mini">
                    <span class="btn btn-success"></span>

                    <span class="btn btn-info"></span>

                    <span class="btn btn-warning"></span>

                    <span class="btn btn-danger"></span>
                </div>
            </div><!-- /.sidebar-shortcuts -->

            <ul class="nav nav-list">
                <li class="">
                    <a href="<%=basePath%>/biIndex.jsp">
                        <i class="menu-icon fa fa-tachometer"></i>
                        <span class="menu-text"> 监控台 </span>
                    </a>

                    <b class="arrow"></b>
                </li>

                <li class="">
                    <a href="<%=basePath%>/Olive_views/warehouse/inbound.jsp">
                        <i class="menu-icon fa fa-desktop"></i>
							<span class="menu-text">
								仓库业务统计
							</span>

                    </a>

                </li>

                <b class="arrow"></b>


                <li class="">
                    <a href="#" class="dropdown-toggle">
                        <i class="menu-icon fa fa-list"></i>
                        <span class="menu-text">门店业务统计 </span>

                        <b class="arrow fa fa-angle-down"></b>
                    </a>

                    <b class="arrow"></b>

                    <ul class="submenu">
                        <li class="">
                            <a href="<%=basePath%>/Olive_views/shop/inbound.jsp">
                                <i class="menu-icon fa fa-caret-right"></i>
                                进销存统计
                            </a>

                            <b class="arrow"></b>
                        </li>

                        <li class="">
                            <a href="<%=basePath%>/saleAnalysisController/saleAnalysis.do">
                                <i class="menu-icon fa fa-caret-right"></i>
                                零售明细统计
                            </a>

                            <b class="arrow"></b>
                        </li>
                        <li class="active">
                            <a href="<%=basePath%>/fittingAnalysisController/fittingAnalysis.do">
                                <i class="menu-icon fa fa-caret-right"></i>
                                试衣明细统计
                            </a>

                            <b class="arrow"></b>
                        </li>
                    </ul>
                </li>


                <li class="">
                    <a href="<%=basePath%>/Olive_views/vendor/inbound.jsp">
                        <i class="menu-icon fa fa-list-alt"></i>
                        <span class="menu-text"> 供应商发货统计 </span>
                    </a>

                    <b class="arrow"></b>
                </li>

            </ul><!-- /.nav-list -->

            <!-- #section:basics/sidebar.layout.minimize -->
            <div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
                <i class="ace-icon fa fa-angle-double-left" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
            </div>

            <!-- /section:basics/sidebar.layout.minimize -->
            <script type="text/javascript">
                try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
            </script>
        </div>

        <!-- /section:basics/sidebar -->
        <div class="main-content">
            <div class="main-content-inner">
                <!-- #section:basics/content.breadcrumbs -->
                <div class="breadcrumbs" id="breadcrumbs">
                    <script type="text/javascript">
                        try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
                    </script>

                    <ul class="breadcrumb">
                        <li>
                            <i class="ace-icon fa fa-home home-icon"></i>
                            <a href="<%=basePath%>/biIndex.jsp">主页</a>
                        </li>
                      <%--  <li>
                            <i class="menu-icon fa fa-tachometer"></i>
                            <a href="<%=basePath%>/biIndex.jsp">监控台</a>
                       </li> --%>
                       <li>
                            <i class="menu-icon fa fa-list"></i>
                            <span class="menu-text">门店业务统计 </span>
                       </li>
                    </ul><!-- /.breadcrumb -->

                    <!-- #section:basics/content.searchbox -->
                    <div class="nav-search" id="nav-search">
                        <form class="form-search">
								<span class="input-icon">
									<input type="text" placeholder="Search ..." class="nav-search-input" id="nav-search-input" autocomplete="off" />
									<i class="ace-icon fa fa-search nav-search-icon"></i>
								</span>
                        </form>
                    </div><!-- /.nav-search -->

                    <!-- /section:basics/content.searchbox -->
                </div>

                <!-- /section:basics/content.breadcrumbs -->
                <div class="page-content">
                    <!-- #section:settings.box -->
                   <!-- /.ace-settings-container -->

                    <!-- /section:settings.box -->
                    <div class="page-header">
                        <h1>
                                                                                           试衣数据分析
                            <small>
                                <i class="ace-icon fa fa-angle-double-right"></i>
                                试衣数据
                            </small>
                        </h1>
                        
                    
                    </div><!-- /.page-header -->
                    <div class="row">

                        <div class="col-xs-12">
                            <div class="row">
                                <div class="col-xs-2">
                                    <div class="infobox infobox-blue2">
                                        <div class="infobox-icon">
                                            <i class="ace-icon fa fa-group"></i>
                                        </div>

                                        <div class="infobox-data">
                                            <span class="infobox-data-number">${curDaySale}次</span>
                                            <div class="infobox-content">本日试衣量</div>
                                        </div>
                                        <c:choose>
                                            <c:when test="${curDaySuccess.toString().equals('true')}">
                                            <div class="stat stat-success">${curDaySalePer}%</div>

                                            </c:when>
                                            <c:otherwise>
                                                <div class="stat stat-important">${curDaySalePer}%</div>
                                            </c:otherwise>
                                        </c:choose>


                                    </div>
                                </div>

                                <div class="col-xs-2">

                                    <div class="infobox infobox-blue">
                                        <div class="infobox-icon">
                                            <i class="ace-icon fa fa-group"></i>
                                        </div>

                                        <div class="infobox-data">
                                            <span class="infobox-data-number">${curWeekSale}次</span>
                                            <div class="infobox-content">本周试衣量</div>
                                        </div>

                                        <c:choose>
                                            <c:when test="${curWeekSuccess.toString().equals('true')}">
                                                <div class="stat stat-success">${curWeekSalePer}%</div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="stat stat-important">${curWeekSalePer}%</div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                <div class="col-xs-2">
                                    <div class="infobox infobox-red bg-info">
                                        <div class="infobox-icon">
                                            <i class="ace-icon fa fa-group"></i>
                                        </div>

                                        <div class="infobox-data">
                                            <span class="infobox-data-number">${curMonthSale}次</span>
                                            <div class="infobox-content">本月试衣量</div>
                                        </div>
                                        <c:choose>
                                            <c:when test="${curMonthSuccess.toString().equals('true')}">

                                                <div class="stat stat-success">${curMonthSalePer}%</div>
                                            </c:when>
                                            <c:otherwise>

                                                <div class="stat stat-important">${curMonthSalePer}%</div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>

                                <div class="col-xs-2">
                                    <div class="infobox infobox-blue2">
                                        <div class="infobox-icon">
                                            <i class="ace-icon fa fa-group"></i>
                                        </div>

                                        <div class="infobox-data">
                                            <span class="infobox-data-number">${preDaySale}次</span>
                                            <div class="infobox-content">昨日试衣量</div>
                                        </div>

                                        <c:choose>
                                            <c:when test="${preDaySaleSuccess.toString().equals('true')}">
                                                <div class="stat stat-success">${preDaySalePer}%</div>

                                            </c:when>
                                            <c:otherwise>
                                                <div class="stat stat-important">${preDaySalePer}%</div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                </div>
                                <div class="col-xs-2">
                                    <div class="infobox infobox-blue">
                                        <div class="infobox-icon">
                                            <i class="ace-icon fa fa-group"></i>
                                        </div>

                                        <div class="infobox-data">
                                            <span class="infobox-data-number">${preWeekSale}次</span>
                                            <div class="infobox-content">上周试衣量</div>
                                        </div>
                                        <c:choose>
                                            <c:when test="${preWeekSuccess.toString().equals('true')}">
                                                <div class="stat stat-success">${preWeekSalePer}%</div>

                                            </c:when>
                                            <c:otherwise>
                                                <div class="stat stat-important">${preWeekSalePer}%</div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                <div class="col-xs-2">

                                    <div class="infobox infobox-pink">
                                        <div class="infobox-icon">
                                            <i class="ace-icon fa fa-group"></i>
                                        </div>

                                        <div class="infobox-data">
                                            <span class="infobox-data-number">${preMonthSale}次</span>
                                            <div class="infobox-content">上月试衣量</div>
                                        </div>
                                        <c:choose>
                                            <c:when test="${preMonthSuccess.toString().equals('true')}">
                                                <div class="stat stat-success">${preMonthSalePer}%</div>

                                            </c:when>
                                            <c:otherwise>
                                                <div class="stat stat-important">${preMonthSalePer}%</div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-xs-4">
                                    <div id="mainBarSize" style="height:300px;border:1px"></div>
                                </div>
                                <div class="col-xs-4">
                                    <div id="mainBarColor" style="height:300px;border:1px"></div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-xs-12">
                                    <div id="mainBar" style="height:500px;border:1px"></div>
                                </div>
                            </div>
                        </div>
                    </div>

                </div><!-- /.page-content -->
            </div>
        </div><!-- /.main-content -->

        <div class="footer">
            <div class="footer-inner">
                <!-- #section:basics/footer -->
                <div class="footer-content">
						<span class="bigger-120">
							<span class="blue bolder">CaseSoft</span>
							RFID &copy; 2012-2017
						</span>

                    &nbsp; &nbsp;

                </div>

                <!-- /section:basics/footer -->
            </div>
        </div>

        <a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
            <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
        </a>
    </div><!-- /.main-container -->

    <!-- basic scripts -->

    <!--[if !IE]> -->
    <script type="text/javascript">
        window.jQuery || document.write("<script src='<%=basePath%>Olive/assets/js/jquery.js'>"+"<"+"/script>");
    </script>

    <!-- <![endif]-->

    <!--[if IE]>
    <script type="text/javascript">
        window.jQuery || document.write("<script src='<%=basePath%>Olive/assets/js/jquery1x.js'>"+"<"+"/script>");
    </script>
    <![endif]-->
    <script type="text/javascript">
        if('ontouchstart' in document.documentElement) document.write("<script src='<%=basePath%>Olive/assets/js/jquery.mobile.custom.js'>"+"<"+"/script>");
    </script>
    <script src="<%=basePath%>Olive/assets/js/bootstrap.js"></script>

    <!-- page specific plugin scripts -->

    <!--[if lte IE 8]>
    <script src="<%=basePath%>Olive/assets/js/excanvas.js"></script>
    <![endif]-->
    <script src="<%=basePath%>Olive/assets/js/jquery-ui.custom.js"></script>
    <script src="<%=basePath%>Olive/assets/js/jquery.ui.touch-punch.js"></script>
    <script src="<%=basePath%>Olive/assets/js/jquery.easypiechart.js"></script>
    <script src="<%=basePath%>Olive/assets/js/jquery.sparkline.js"></script>
    <script src="<%=basePath%>Olive/assets/js/flot/jquery.flot.js"></script>
    <script src="<%=basePath%>Olive/assets/js/flot/jquery.flot.pie.js"></script>
    <script src="<%=basePath%>Olive/assets/js/flot/jquery.flot.resize.js"></script>
    <script src="<%=basePath%>Olive//assets/js/jqGrid/src/jquery.jqGrid.js"></script>
	<script src="<%=basePath%>Olive//assets/js/jqGrid/js/i18n/grid.locale-cn.js"></script>

    <!-- ace scripts -->
    <script src="<%=basePath%>Olive/assets/js/ace/elements.scroller.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/elements.colorpicker.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/elements.fileinput.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/elements.typeahead.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/elements.wysiwyg.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/elements.spinner.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/elements.treeview.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/elements.wizard.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/elements.aside.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.ajax-content.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.touch-drag.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.sidebar.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.sidebar-scroll-1.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.submenu-hover.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.widget-box.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.settings.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.settings-rtl.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.settings-skin.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.widget-on-reload.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.searchbox-autocomplete.js"></script>

    <!-- 增加echart支持 -->
    <script src="<%=basePath%>jslib2/echarts/echarts.js"></script>
    <script src="<%=basePath%>jslib2/echarts/macarons.js"></script>

    <!-- inline scripts related to this page -->
    <script type="text/javascript">

    </script>

    <!-- the following scripts are used in demo only for onpage help and you don't need them -->
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/ace.onpage-help.css" />
    <link rel="stylesheet" href="<%=basePath%>Olive/docs/assets/js/themes/sunburst.css" />

    <script type="text/javascript"> ace.vars['base'] = '..'; </script>
    <script src="<%=basePath%>Olive/assets/js/ace/elements.onpage-help.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.onpage-help.js"></script>
    <script src="<%=basePath%>Olive/docs/assets/js/rainbow.js"></script>
    <script src="<%=basePath%>Olive/docs/assets/js/language/generic.js"></script>
    <script src="<%=basePath%>Olive/docs/assets/js/language/html.js"></script>
    <script src="<%=basePath%>Olive/docs/assets/js/language/css.js"></script>


    <script type="text/javascript">
    // Step:3 conifg ECharts's path, link to echarts.js from current page.
    // Step:3 为模块加载器配置echarts的路径，从当前页面链接到echarts.js，定义所需图表路径


    // Step:4 require echarts and use it in the callback.
    // Step:4 动态加载echarts然后在回调函数中开始使用，注意保持按需加载结构定义图表路径
    //创建ECharts图表方法

    //--- 折柱 ---
    //基于准备好的dom,初始化echart图表
    var myChart = echarts.init(document.getElementById('mainBar'),"macarons");
    //定义图表option
    var option = {
    //标题，每个图表最多仅有一个标题控件，每个标题控件可设主副标题
    title: {
    //主标题文本，'\n'指定换行
    text: '2016年度试衣数据统计表',
    //主标题文本超链接
     //副标题文本，'\n'指定换行
     //副标题文本超链接
     //水平安放位置，默认为左侧，可选为：'center' | 'left' | 'right' | {number}（x坐标，单位px）
    x: 'center',
    //垂直安放位置，默认为全图顶端，可选为：'top' | 'bottom' | 'center' | {number}（y坐标，单位px）
    y: 'top'
    },
    //提示框，鼠标悬浮交互时的信息提示
    tooltip: {
    //触发类型，默认（'item'）数据触发，可选为：'item' | 'axis'
    trigger: 'axis'
    },
    //图例，每个图表最多仅有一个图例
    legend: {
    //显示策略，可选为：true（显示） | false（隐藏），默认值为true
    show: true,
    //水平安放位置，默认为全图居中，可选为：'center' | 'left' | 'right' | {number}（x坐标，单位px）
    x: 'center',
    //垂直安放位置，默认为全图顶端，可选为：'top' | 'bottom' | 'center' | {number}（y坐标，单位px）
    y: 'top',
    //legend的data: 用于设置图例，data内的字符串数组需要与sereis数组内每一个series的name值对应
    data: ['试衣次数']
    },
    //工具箱，每个图表最多仅有一个工具箱
    toolbox: {
    //显示策略，可选为：true（显示） | false（隐藏），默认值为false
    show: true,
    //启用功能，目前支持feature，工具箱自定义功能回调处理
    feature: {
    //辅助线标志
    mark: {show: true},
    //dataZoom，框选区域缩放，自动与存在的dataZoom控件同步，分别是启用，缩放后退
    dataZoom: {
    show: true,
    title: {
    dataZoom: '区域缩放',
    dataZoomReset: '区域缩放后退'
    }
    },
    //数据视图，打开数据视图，可设置更多属性,readOnly 默认数据视图为只读(即值为true)，可指定readOnly为false打开编辑功能
    dataView: {show: true, readOnly: true},
    //magicType，动态类型切换，支持直角系下的折线图、柱状图、堆积、平铺转换
        magicType : {show: true, type: ['line', 'bar']},
    //restore，还原，复位原始图表
    restore: {show: true},
    //saveAsImage，保存图片（IE8-不支持）,图片类型默认为'png'
    saveAsImage: {show: true}
    }
    },
    //是否启用拖拽重计算特性，默认关闭(即值为false)
    calculable: true,
    //直角坐标系中横轴数组，数组中每一项代表一条横轴坐标轴，仅有一条时可省略数值
    //横轴通常为类目型，但条形图时则横轴为数值型，散点图时则横纵均为数值型
    xAxis: [
    {
    //显示策略，可选为：true（显示） | false（隐藏），默认值为true
    show: true,
    //坐标轴类型，横轴默认为类目型'category'
    type: 'category',
    //类目型坐标轴文本标签数组，指定label内容。 数组项通常为文本，'\n'指定换行
    data: ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
    }
    ],
    //直角坐标系中纵轴数组，数组中每一项代表一条纵轴坐标轴，仅有一条时可省略数值
    //纵轴通常为数值型，但条形图时则纵轴为类目型
    yAxis: [
    {
    //显示策略，可选为：true（显示） | false（隐藏），默认值为true
    show: true,
    //坐标轴类型，纵轴默认为数值型'value'
    type: 'value',
    //分隔区域，默认不显示
    splitArea: {show: true}
    }
    ],

    //sereis的数据: 用于设置图表数据之用。series是一个对象嵌套的结构；对象内包含对象
    series: [
    {
    //系列名称，如果启用legend，该值将被legend.data索引相关
    name: '试衣数据展示',
    //图表类型，必要参数！如为空或不支持类型，则该系列数据不被显示。
    type: 'line',
        itemStyle: {normal: {areaStyle: {type: 'default'}}},
        //系列中的数据内容数组，折线图以及柱状图时数组长度等于所使用类目轴文本标签数组axis.data的长度，并且他们间是一一对应的。数组项通常为数值
    data: [1200, 3200, 4300,4469, 4535, 5800, 6890, 78920,5360, 35200, 29300, 1825],
    //系列中的数据标注内容
    markPoint: {
    data: [
    {type: 'max', name: '最大值'},
    ]
    },
    //系列中的数据标线内容
    markLine: {
    data: [
    {type: 'average', name: '平均值'}
    ]
    }
    },
    ]
    };
    //为echarts对象加载数据
    myChart.setOption(option,"macarons");

    option2 = {
    title : {
    text: '2016年试衣尺码分析表',
    subtext: '无锡凯施',
    x:'center'
    },
        toolbox:{
        show:true,
                feature:{
            saveAsImage: {show: true}

        }
    },

    tooltip : {
    trigger: 'item',
    formatter: "{a} <br/>{b} : ({d}%)"
    },
    legend: {
    orient: 'vertical',
    left: 'left',
    data: ['M','L','XL','XLL','XLL']
    },
    series : [
    {
    name: 'M',
    type: 'pie',
    radius : '55%',
    center: ['50%', '60%'],
    data:[
    {value:332, name:'L'},
    {value:310, name:'XL'},
    {value:536, name:'XLL'},
    {value:135, name:'XLLL'},
    {value:123, name:'M'}
    ],
    itemStyle: {
    emphasis: {
    shadowBlur: 10,
    shadowOffsetX: 0,
    shadowColor: 'rgba(0, 0, 0, 0.5)'
    }
    }
    }
    ]
    };
    var myChart1 = echarts.init(document.getElementById('mainBarSize'),"macarons");
    myChart1.setOption(option2);


    var option3 ={
        title : {
            text: '2016年度试颜色分析表',
            subtext: '无锡凯施',
            x:'center'
        },
        toolbox:{
            show:true,
            feature:{
                saveAsImage: {show: true}

            }
        },

        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : ({d}%)"
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data: ['红色','绿色','灰色','白色','黑色','黄色','青色']
        },
        series : [
            {
                name: '红色',
                type: 'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {value:332, name:'灰色'},
                    {value:310, name:'绿色'},
                    {value:536, name:'白色'},
                    {value:135, name:'黑色'},
                    {value:123, name:'黄色'},
                    {value:136, name:'青色'}
                ],
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
    var myChart3 = echarts.init(document.getElementById('mainBarColor'),"macarons");
    myChart3.setOption(option3);


    </script>
    </body>
    </html>
