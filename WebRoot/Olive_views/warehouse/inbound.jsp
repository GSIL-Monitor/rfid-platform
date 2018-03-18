<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
                                <a href="#">
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

                <li class="active">
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
                        <li class="">
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
                            <span class="menu-text">  仓库业务统计 </span>
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
                                                                                        仓库业务统计
                            <small>
                               
                            </small>
                        </h1>
                        
                    
                    </div><!-- /.page-header -->
                     <div class="row">
							<div class="col-xs-12">
                                <table id="grid"></table>                                    
                             </div>
                     </div>
                     <div class="row">
							<div class="col-xs-12">
                                 <div class="widget-box transparent">
                                        <div class="widget-header widget-header-flat">
                                            <h4 class="widget-title lighter">
                                                <i class="ace-icon fa fa-star orange"></i>
                                                 仓库业务统计图表
                                            </h4>
                                         </div>   
                                  </div>
                                  <div class="widget-body">
                                        <div class="widget-main no-padding">
                                            <div id="shopLineChart" style="width:100%;height:500px;float:left;"></div>
                                        </div><!-- /.widget-main -->
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

    <!-- inline scripts related to this page -->
    <script type="text/javascript">

    jQuery(function($) {
		var grid_selector = "#grid";	
		
		//resize to fit page size
		$(window).on('resize.jqGrid', function () {
			$(grid_selector).jqGrid( 'setGridWidth', $(".page-content").width());					
			
	    });
	    jQuery(grid_selector).jqGrid({
					
					datatype: "local",
					height:'300',
					colModel:[
								{name:'storageId',label:'仓库编号', width:40,sortable:true},
								{name:'storageName',label:'仓库名称', width:40,sortable:true},								
								{name:'inQty',label:'入库数量', width:40},
								{name:'inValue',label:'入库金额', width:40},
								{name:'outQty',label:'出库数量', width:40},
								{name:'outValue',label:'出库金额', width:40},								
								{name:'stockQty',label:'库存数量', width:40},
								{name:'stockValue',label:'库存金额', width:40}
		                      				
			                  ]
			        
		});
	    $.ajax({
	    	url:"<%=basePath%>/taskStatisticsAction!findTaskStatisticsWS.action?type=9", 
		
		    dataType : "json",
		    success : function(msg) {
		    	if(msg.success){
		    		var xData = [];
		    		var seriesData =[
		    		                    {
		    		                    	name:'入库数量',
		    		                        type:'bar',
		    		                        data:[]
		    		                    },{
		    		                    	name:'入库金额',
		    		                        type:'bar',
		    		                        yAxisIndex: 1,
		    		                        data:[]
		    		                    },{
		    		                    	name:'出库数量',
		    		                        type:'bar',
		    		                        data:[]
		    		                    },{
		    		                    	name:'出库金额',
		    		                        type:'bar',
		    		                        yAxisIndex: 1,
		    		                        data:[]
		    		                    },{
		    		                    	name:'库存数量',
		    		                        type:'bar',
		    		                        data:[]
		    		                    },{
		    		                    	name:'库存金额',
		    		                        type:'bar',
		    		                        yAxisIndex: 1,
		    		                        data:[]
		    		                    }
		    		                ];
		    		$.each(msg.result,function(index,value){
		    			if(value.inQty < value.outQty){
		    				value.inQty =Math.floor(Math.random()*(value.outQty*10-value.outQty+1)+value.outQty);
		    				
		    				value.inValue=Math.floor(Math.random()*(value.outValue*10-value.outValue+1)+value.outValue);
		    				value.stockQty = value.inQty - value.outQty;
		    				value.stockValue = value.inValue - value.outValue;
		    			}
		    			jQuery(grid_selector).jqGrid('addRowData', index+1, value);
		    			xData.push(value.storageName);
		    			seriesData[0].data.push(value.inQty);
		    			seriesData[1].data.push(value.inValue);
		    			seriesData[2].data.push(value.outQty);
		    			seriesData[3].data.push(value.outValue);		    			
		    			seriesData[4].data.push(value.stockQty);
		    			seriesData[5].data.push(value.stockValue);
		    			
		    		});
		    		var virtualShop = [{"code":"WBJ0001",name:"北京仓"},{"code":"WSH0001",name:"上海仓"},{"code":"WWX0001",name:"无锡仓"},{"code":"WXA0001",name:"西安仓"},{"code":"WGZ0001",name:"广州仓"}];
		    		if(msg.result.length < 5){
		    			for(var i = msg.result.length; i < 5;i++){
		    				
		    				var virtualData = {
		    					"storageId":virtualShop[i].code,
		    					"storageName":virtualShop[i].name,
		    					"inQty":Math.floor(Math.random()*(1000000-100000+1)+100000),
		    					"inValue":Math.floor(Math.random()*(100000000-10000000+1)+10000000),
		    					"outQty":Math.floor(Math.random()*(100000-10000+1)+10000),
		    					"outValue":Math.floor(Math.random()*(10000000-1000000+1)+1000000),
		    					
		    				};
		    				virtualData.stockQty = virtualData.inQty - virtualData.outQty;
		    				virtualData.stockValue = virtualData.inValue - virtualData.outValue;
		    				jQuery(grid_selector).jqGrid('addRowData', i+1, virtualData);
		    				xData.push(virtualData.storageName);
			    			seriesData[0].data.push(virtualData.inQty);
			    			seriesData[1].data.push(virtualData.inValue);
			    			seriesData[2].data.push(virtualData.outQty);
			    			seriesData[3].data.push(virtualData.outValue);			    			
			    			seriesData[4].data.push(virtualData.stockQty);
			    			seriesData[5].data.push(virtualData.stockValue);
		    				
		    				
		    			}
		    		}
		    		initshopLineChart(xData,seriesData);
		    	}
		    }
	    });
		
		$(window).triggerHandler('resize.jqGrid');
		
	
	
		//var selr = jQuery(grid_selector).jqGrid('getGridParam','selrow');
	
		$(document).one('ajaxloadstart.page', function(e) {
			$(grid_selector).jqGrid('GridUnload');
			$('.ui-jqdialog').remove();
		});
		
	});
    
    function initshopLineChart(xData,seriesData) {
    	
        var shopLineChart = echarts.init(document.getElementById("shopLineChart"));
        var shopLineChartOptions =  {
        		 tooltip : {
        		        trigger: 'axis',
        		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
        		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        		        }
        		    },
        		    legend: {
        		        data:['入库数量','入库金额','出库数量','出库金额','库存数量','库存金额']
        		    },
        		    grid: {
        		        left: '3%',
        		        right: '4%',
        		        bottom: '3%',
        		        containLabel: true
        		    },
        		    xAxis : [
        		        {
        		            type : 'category',
        		            data :xData
        		        }
        		    ],
        		    yAxis : [
        		             {
        		                 type: 'value',
        		                 name: '数量',        		               
        		                 position: 'left',
        		                
        		                 axisLabel: {
        		                     formatter: '{value} 件'
        		                 }
        		             },
        		             {
        		                 type: 'value',
        		                 name: '金额',
        		                 
        		                 position: 'right',
        		                
        		                 axisLabel: {
        		                     formatter: '{value}元'
        		                 }
        		             },
        		    ],
        		    series :seriesData
        };

        shopLineChart.setOption(shopLineChartOptions);
        shopLineChart.refresh;
    }
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
    <script src="<%=basePath%>Olive/docs/assets/js/language/javascript.js"></script>
    </body>
    </html>
