<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="searchBaseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>

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
            <!-- /.page-header -->

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
                                    <div class="btn-group btn-group-sm pull-left">
                                           <button class="btn btn-info" onclick="collapseGroup();">
                                               <i class="ace-icon fa fa-chevron-up"></i>
                                               <span class="bigger-110">折叠分组</span>
                                           </button>
                                           <button class="btn  btn-info" onclick="expandGroup();">
                                              <i class="ace-icon fa fa-chevron-down"></i>
                                              <span class="bigger-110">展开分组</span>
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

                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_gte_scanTime">日期</label>

                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                <div class="input-group">
                                                    <input  id="filter_gte_scanTime" class="form-control date-picker" name="filter_gte_scanTime"
                                                            data-date-format="yyyy-mm-dd"/>
												      <span class="input-group-addon">
															<i class="fa fa-exchange"></i>
                                                      </span>
                                                    <input  id="filter_lte_scanTime" class="form-control date-picker"  name="filter_lte_scanTime"
                                                            data-date-format="yyyy-mm-dd"/>
                                                </div>
                                            </div>
                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_contains_taskId">任务号</label>

                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                <input class="form-control" id="filter_contains_taskId" name="filter_contains_taskId" type="text" onkeyup="this.value=this.value.toUpperCase()"
                                                       placeholder="模糊查询"/>
                                            </div>
                                           
											<label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_contains_billNo">单号</label>

                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                <input class="form-control" id="filter_contains_billNo" name="filter_contains_billNo" type="text" onkeyup="this.value=this.value.toUpperCase()"
                                                       placeholder="模糊查询"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_in_origId">发货方</label>
                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                <select id="filter_in_origId" name="filter_in_origId" class="chosen-select form-control"  data-placeholder="编号列表">
                                                </select>
                                            </div>

                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="form_token">出入库方式</label>

                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                <select class="chosen-select form-control" id="form_token" name="filter_eq_token">
                                                    <option value="">-请选择-</option>
                                                    <option value="8">仓库入库</option>
                                                    <option value="23">退货入库</option>
                                                    <option value="25">调拨入库</option>

                                                    <option value="10">仓库出库</option>
                                                    <option value="24">仓库调拨出库</option>
                                                    <option value="26">仓库退货出库</option>
                                                </select>
                                            </div>
                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_in_destId">收货仓店</label>
                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                <select id="filter_in_destId" name="filter_in_destId" class="chosen-select form-control" data-placeholder="编号列表"></select>
                                             </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_eq_styleId">款号</label>

                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                <div class="input-group">
                                                    <input class="form-control" id="filter_eq_styleId"
                                                           type="text" name="filter_eq_styleId" readonly/>
                                                     <span class="input-group-btn">
                                                         <button class="btn btn-sm btn-default" type="button" onclick="openstyleDialog('#filter_eq_styleId','#filter_eq_styleName')">
                                                             <i class="ace-icon fa fa-list"></i>
                                                         </button>
								                      </span>
                                                    <input class="form-control" id="filter_eq_styleName"
                                                           type="text" name="filter_eq_styleName" readonly  placeholder="款名"/>
                                                </div>
                                            </div>
                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_contains_colorId">色号</label>

                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                <input class="form-control" id="filter_contains_colorId" name="filter_contains_colorId" type="text"
                                                       placeholder="模糊查询"/>
                                            </div>

                                            
                                        </div>
                                    <!-- #section:elements.form -->
                                    
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

                           <div id="searchGrid" style="height:800px"></div>
                             


                        <!-- PAGE CONTENT ENDS -->
                    </div>
                    <!-- /.col -->
                </div>
                <!-- /.row -->
                <!--/#page-content-->
            </div>
        </div>
    </div>

   
    <!--/.fluid-container#main-container-->
</div>

<jsp:include page="search_js.jsp"></jsp:include>
<jsp:include page="../base/style_dialog.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/search/boxDetailSearchController.js"></script>


</body>
</html>