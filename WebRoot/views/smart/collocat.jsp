<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
 <jsp:include page="../baseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>
</head>
<body class="no-skin">
	<div class="main-container" id="main-container" style="">
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
                                        <button class="btn btn-info" onclick="reload()">
                                            <i class="ace-icon fa fa-refresh"></i>
                                            <span class="bigger-110">刷新</span>
                                        </button>
                                        
                                    </div>
                                    
                                    <div class="btn-group btn-group-sm pull-left">
                                    	<button type="button" class ="btn btn-primary" onclick="add()">
                                    		<i class ="ace-icen fa fa-plus"></i>
                                    		<span class="bigger-110">增加</span>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">
                                    	<button type="button" class ="btn btn-primary" onclick="edit()">
                                    		<i class ="ace-icen fa fa-edit"></i>
                                    		<span class="bigger-110">编辑</span>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-right">

                                    <button type="button" class="btn btn-info"
                                            onclick="showcolSearchPanel();">
                                        <i class="ace-icon fa fa-binoculars"></i> <span
                                            class="bigger-110">高级查询</span>
                                    </button>

                                </div>
                                </div>
                            </div>
                            <div class="hr hr-2 hr-dotted"></div>
							
                            <div class="widget-main" id="searchPanel" style="display:none">
                                <form class="form-horizontal" role="form" id="searchForm">
                                    <div class="form-group" align="center">

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-2 control-label text-right" for="search_styleIds">关联款号</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="search_styleIds" name="filter_LIKES_styleIds" type="text" placeholder="模糊查询"/>
                                        </div>
                                        
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-2 control-label text-right" for="search_id">套装编号</label>

                                         <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="search_id" name="filter_LIKES_id" type="text" placeholder="模糊查询"/>
                                        </div>
                                      
                                    </div>                 
                
                                    <div class="form-group">
                                        <div class="col-sm-offset-5 col-sm-10">
                                            <button type="button" class="btn btn-sm btn-primary" onclick="_search()">
                                                <i class="ace-icon fa fa-search"></i>
                                                <span class="bigger-110">查询</span>
                                            </button>
                                            <button type="reset" class="btn btn-sm btn-warning" onclick="_clearSearch()">
                                                <i class="ace-icon fa fa-undo"></i>
                                                <span class="bigger-110">清空</span></button>
                                        </div>
                                    </div>
                                </form>

                            </div>
                        </div>

                        <table id="grid"></table>

                        <div id="grid-pager"></div>

                        <!-- PAGE CONTENT ENDS -->
                    </div>
                    <!-- /.col -->
                </div>
                <!-- /.row -->
                <!--/#page-content-->
            </div>
        </div>
    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>
   </div>
   <jsp:include page="../layout/footer_js.jsp"></jsp:include>
   <script type="text/javascript" src="<%=basePath%>/views/smart/collocatController.js"></script>
</body>
</html>