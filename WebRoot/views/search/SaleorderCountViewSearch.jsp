<%@ page language="java"
         import="java.util.*,com.casesoft.dmc.model.sys.User"
         pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    User user = (User) session.getAttribute("userSession");
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="searchBaseView.jsp"></jsp:include>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
        var curOwnerId = "${ownerId}";
        var deportId="${deportId}";
        var deportName="${deportName}";
        var Codes="<%=user.getCode()%>";
        var roleid="${roleid}";
        var groupid="${groupid}";
        var ownersId ="";
    </script>
    <style type="text/css">
        .data-container {
            display: inline-block;
            border: 1px solid #d7d7d7;
            border-right: 0px;
            width: 20%;
            margin-bottom: 10px;
            font-size: 12px;
            text-align: center;
            padding-top: 3px;
            padding-bottom: 3px;
        }
        .data-container1 {
            display: inline-block;
            border: 1px solid #d7d7d7;
            border-right: 0px;
            width: 33%;
            margin-bottom: 10px;
            font-size: 12px;
            text-align: center;
            padding-top: 3px;
            padding-bottom: 3px;
        }
        .title {
            color: #53606b;
            font-size: 14px;
            line-height: 31px;
        }
/*        .text {
            font-size: 24px;
            color: #fe3800;
            margin: 10px;
            line-height: 31px;
        }*/
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
            <!-- /.page-header -->

            <div id="page-content">

                <div class="row">
                    <div class="col-xs-12">
                        <div class="widget-body ">
                            <div class="widget-toolbox padding-8 clearfix">
                                <div class="btn-group btn-group-sm pull-left">
                                    <button class="btn btn-info" onclick="saleDetail();">
                                        <i class="ace-icon"></i>
                                        <span class="bigger-110">销售明细</span>
                                    </button>
                                </div>
                                <div class="btn-group btn-group-sm pull-left">
                                    <button class="btn btn-info" onclick="saleorderDetail();">
                                        <i class="ace-icon"></i>
                                        <span class="bigger-110">按单据汇总</span>
                                    </button>
                                </div>
                                <div class="btn-group btn-group-sm pull-left">
                                    <button class="btn btn-info" onclick="saleorderbusinessname();">
                                        <i class="ace-icon"></i>
                                        <span class="bigger-110">按销售员汇总</span>
                                    </button>
                                </div>
                                <div class="btn-group btn-group-sm pull-left">
                                    <button class="btn btn-info" onclick="saleorderorigname();">
                                        <i class="ace-icon"></i>
                                        <span class="bigger-110">按部门汇总</span>
                                    </button>
                                </div>
                            </div>

                            <div class="widget-toolbox padding-8 clearfix">
                                <div class="btn-toolbar" role="toolbar">
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-info" onclick="refresh();">
                                            <i class="ace-icon fa fa-refresh"></i>
                                            <span class="bigger-110">刷新</span>
                                        </button>
                                    </div>

                                  <%--  <div class="btn-group btn-group-sm pull-left">

                                        <button class="btn btn-info" onclick="chooseExportFunction();">

                                            <i class="ace-icon fa fa-file-excel-o"></i>
                                            <span class="bigger-110">导出旧</span>

                                        </button>
                                    </div>--%>
                                    <div class="btn-group btn-group-sm pull-left">

                                        <button class="btn btn-info" onclick="newchooseExportFunction();">

                                            <i class="ace-icon fa fa-file-excel-o"></i>
                                            <span class="bigger-110">导出新</span>

                                        </button>
                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-info" onclick="collapseGroup('#'+exportExcelid);">
                                            <i class="ace-icon fa fa-chevron-up"></i>
                                            <span class="bigger-110">折叠分组</span>
                                        </button>
                                        <button class="btn  btn-info" onclick="expandGroup('#'+exportExcelid);">
                                            <i class="ace-icon fa fa-chevron-down"></i>
                                            <span class="bigger-110">展开分组</span>
                                        </button>

                                    </div>
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-info" onclick="onday();">
                                            <i class="ace-icon fa fa-chevron-up"></i>
                                            <span class="bigger-110">当天</span>
                                        </button>
                                        <button class="btn  btn-info" onclick="halfmonth();">
                                            <i class="ace-icon fa fa-chevron-down"></i>
                                            <span class="bigger-110">半月</span>
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
                                    <%--<input  id="filter_contains_groupid" name="filter_contains_groupid" type="text" style="display: none"/>--%>
                                    <div class="form-group">

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_gte_billDate">日期</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <div class="input-group">
                                                <input  id="filter_gte_billDate" class="form-control date-picker" name="filter_gte_billDate"
                                                        data-date-format="yyyy-mm-dd"/>
                                                <span class="input-group-addon">
															<i class="fa fa-exchange"></i>
                                                      </span>
                                                <input  id="filter_lte_billDate" class="form-control date-picker"  name="filter_lte_billDate"
                                                        data-date-format="yyyy-mm-dd"/>
                                            </div>
                                        </div>

                                      <%--  <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_contains_taskId">任务号</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="filter_contains_taskId" name="filter_contains_taskId" type="text" onkeyup="this.value=this.value.toUpperCase()"
                                                   placeholder="模糊查询"/>
                                        </div>--%>

                                        <div id="billno">
                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_contains_billno">单号</label>

                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                <input class="form-control" id="filter_contains_billno" name="filter_contains_billno" type="text" onkeyup="this.value=this.value.toUpperCase()"
                                                       placeholder="模糊查询"/>
                                                <%--<input class="form-control" id="filter_contains_groupid" name="filter_contains_groupid" type="text"
                                                       style="display: none"/>--%>
                                            </div>
                                        </div>

                                    </div>
                                   <%-- <div class="form-group" style="display: none">

                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_gte_billDate">日期</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <div class="input-group">
                                                <input class="form-control" id="filter_contains_groupid" name="filter_contains_groupid" type="text"/>
                                            </div>
                                        </div>
                                    </div>--%>
                                    <div class="form-group">
                                        <div id="destUnitId">
                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_in_destunitid">发货方</label>
                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                <select  class="form-control selectpicker show-tick" id="filter_in_destUnitId" name="filter_in_ownerid" multiple="multiple"
                                                         style="width: 100%;" data-live-search="true">
                                                </select>
                                            </div>
                                        </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_in_deport">仓店</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                           <%--<select id="filter_in_deport"name="filter_in_deport" multiple="multiple" data-placeholder="编号列表">
                                            </select>--%>
                                            <select class="form-control selectpicker show-tick" id="filter_in_deport" name="filter_in_deport"
                                                       style="width: 100%;" onchange="changedeport()" data-live-search="true">
                                            </select>
                                        </div>
                                      <%-- <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_in_origid">收货仓店</label>--%>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3" style="display: none">
                                            <%--<select id="filter_in_origid"name="filter_in_destid" multiple="multiple" data-placeholder="编号列表">
                                            </select>--%>
                                                <input class="form-control" id="filter_contains_groupid" name="filter_contains_groupid" type="text" onkeyup="this.value=this.value.toUpperCase()"
                                                       />
                                        </div>
                                    </div>
                                    <div class="form-group" id="styleid">
                                        <label id="styleids" class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_eq_styleid">款号</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3" id="styleidname">
                                            <!-- <input class="form-control" id="filter_contains_styleName" name="filter_contains_styleName" type="text"
                                                   placeholder="模糊查询"/> -->
                                            <div class="input-group">
                                                <input class="form-control" id="filter_eq_styleid"
                                                       type="text" name="filter_eq_styleid" readonly/>
                                                <span class="input-group-btn">
                                                         <button class="btn btn-sm btn-default" type="button" onclick="openstyleDialog('#filter_eq_styleid','#filter_eq_stylename')">
                                                             <i class="ace-icon fa fa-list"></i>
                                                         </button>
								                      </span>
                                                <input class="form-control" id="filter_eq_styleName"
                                                       type="text" name="filter_eq_stylename" readonly  placeholder="款名"/>
                                            </div>
                                        </div>
                                       <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="search_origUnitId">客户</label>

                                       <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                           <!-- <input class="form-control" id="filter_contains_styleName" name="filter_contains_styleName" type="text"
                                                  placeholder="模糊查询"/> -->
                                           <div class="input-group">
                                               <input class="form-control" id="search_origUnitId"
                                                      type="text" name="filter_eq_origunitid" readonly/>
                                               <span class="input-group-btn">
                                                             <button class="btn btn-sm btn-default" type="button" onclick="openSearchGuestDialog()">
                                                                 <i class="ace-icon fa fa-list"></i>
                                                             </button>
                                                          </span>
                                               <input class="form-control" id="search_origUnitName"
                                                      type="text" name="filter_eq_origunitname" readonly  />
                                           </div>
                                       </div>
                                      <%--  &lt;%&ndash;<label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_contains_colorId">色号</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <input class="form-control" id="filter_contains_colorId" name="filter_contains_colorId" type="text"
                                                   placeholder="模糊查询"/>
                                        </div>&ndash;%&gt;
                                       &lt;%&ndash; <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="form_token">入库方式</label>

                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="chosen-select form-control" id="form_token" name="filter_eq_token">
                                                <option value="">-请选择-</option>
                                                <option value="8">仓库入库</option>
                                                <option value="23">退货入库</option>
                                                <option value="25">调拨入库</option>
                                                <option value="29">仓库调整入库</option>
                                                <option value="14">门店入库</option>
                                                <option value="17">门店销售退货入库</option>
                                                <option value="19">门店调拨入库</option>
                                                <option value="31">门店调整入库</option>
                                            </select>
                                        </div>&ndash;%&gt;--%>

                                    </div>
                                    <!-- #section:elements.form -->

                                    <div class="form-group">
                                        <div class="col-sm-offset-5 col-sm-10">
                                            <button type="button" class="btn btn-sm btn-primary" onclick="search();">
                                                <i class="ace-icon fa fa-search"></i>
                                                <span class="bigger-110">查询</span>
                                            </button>
                                            <button type="reset" class="btn btn-sm btn-warning" onclick="_reset();">
                                                <i class="ace-icon fa fa-undo"></i>
                                                <span class="bigger-110">清空</span></button>
                                        </div>
                                    </div>

                                </form>


                            </div>
                        </div>
                        <div style="font-size:0px;display: none" ng-show="showCountData" id="isadmin">
                            <div class="data-container">
                                <span class="title">销售单品数</span>
                                <span class="title salesum" id ="salesum"ng-show="sumDataLoading">&nbsp;&nbsp;查询中...</span>
                                <%--<span class="text" ng-bind = "sumQuantity" ng-show="!sumDataLoading"></span>--%>
                            </div>
                            <div class="data-container">
                                <span class="title">销售退货单品数</span>
                                <span class="title salereturnsum" id ="salereturnsum"ng-show="sumDataLoading">&nbsp;&nbsp;查询中...</span>
                                <%--<span class="text" ng-bind = "sumQuantity" ng-show="!sumDataLoading"></span>--%>
                            </div>
                            <div class="data-container">
                                <span class="title">销售额</span>
                                <span class="title salemony" id="salemony"ng-show="sumDataLoading">&nbsp;&nbsp;查询中...</span>
                                <%--<span class="text" ng-bind = "sumTrueAmount|currency:'￥'" ng-show="!sumDataLoading"></span>--%>
                            </div>
                            <div class="data-container">
                                <span class="title">销售毛利</span>
                                <span class="title pressAll"id="pressAll" ng-show="sumDataLoading">&nbsp;&nbsp;查询中...</span>
                                <%--<span class="text" ng-bind = "sumGrossProfit|currency:'￥'" ng-show="!sumDataLoading"></span>--%>
                            </div>
                            <div class="data-container last-container">
                                <span class="title">销售毛利率(%)</span>
                                <span class="title grossprofits"id="grossprofits" ng-show="sumDataLoading">&nbsp;&nbsp;查询中...</span>
                                <%--<span class="text" ng-bind = "avgGrossProfitRate" ng-show="!sumDataLoading"></span>--%>
                            </div>
                        </div>
                        <div style="font-size:0px; display: none" ng-show="showCountData" id="noadmin">
                            <div class="data-container1">
                                <span class="title">销售单品数</span>
                                <span class="title salesum" id ="salesum"ng-show="sumDataLoading">&nbsp;&nbsp;查询中...</span>
                                <%--<span class="text" ng-bind = "sumQuantity" ng-show="!sumDataLoading"></span>--%>
                            </div>
                            <div class="data-container1">
                                <span class="title">销售退货单品数</span>
                                <span class="title salereturnsum" id ="salereturnsum"ng-show="sumDataLoading">&nbsp;&nbsp;查询中...</span>
                                <%--<span class="text" ng-bind = "sumQuantity" ng-show="!sumDataLoading"></span>--%>
                            </div>
                            <div class="data-container1">
                                <span class="title">销售额</span>
                                <span class="title salemony" id="salemony"ng-show="sumDataLoading">&nbsp;&nbsp;查询中...</span>
                                <%--<span class="text" ng-bind = "sumTrueAmount|currency:'￥'" ng-show="!sumDataLoading"></span>--%>
                            </div>
                        </div>

                        <div id="searchGrid" style="height:800px"></div>
                       <%-- <div id="nosearchGrid" style="height:800px;display: none"></div>--%>
                        <div id="searchsaleGrid" style="height:800px;display: none"></div>
                        <div id="searchsalebusinessnameGrid" style="height:800px;display: none"></div>
                        <div id="searchsaleorignameGrid" style="height:800px;display: none"></div>



                        <!-- PAGE CONTENT ENDS -->
                    </div>
                    <form  id="form1" action="" method=post name=form1 style='display:none'>
                        <input id="gridId" type=hidden  name='gridId' value=''>
                        <input id="request" type=hidden  name='request' value=''>
                    </form>
                    <div id ="divshowImage" class="divshowImage" style="display: none">
                        <img class="showImage" id="showImage" onclick="hideImage()">
                    </div>
                    <!-- /.col -->
                </div>
                <!-- /.row -->
                <!--/#page-content-->
            </div>
        </div>
    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>

    <!--/.fluid-container#main-container-->
</div>

<jsp:include page="search_js.jsp"></jsp:include>
<jsp:include page="../base/style_dialog.jsp"></jsp:include>
<jsp:include page="../base/search_guest_dialog.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>/views/search/SaleorderCountViewSearchController.js"></script>
<script>
    function onday() {
        var myDate=new Date();
        var year=myDate.getFullYear();
        var month=myDate.getMonth()+1;
        var day=myDate.getDate();
        if(month<10){
            if(day<10){
                $("#filter_gte_billDate").val(year+"-0"+month+"-0"+day);
                $("#filter_lte_billDate").val(year+"-0"+month+"-0"+day);
            }else{
                $("#filter_gte_billDate").val(year+"-0"+month+"-"+day);
                $("#filter_lte_billDate").val(year+"-0"+month+"-"+day);
            }
        }else{
            $("#filter_gte_billDate").val(year+"-"+month+"-"+day);
            $("#filter_lte_billDate").val(year+"-"+month+"-"+day);
        }
        search();
    }
    function halfmonth() {
        var myDate=new Date();
        var year=myDate.getFullYear();
        var month=myDate.getMonth()+1;
        var day=myDate.getDate();
        if(month<10){
            if(day<10){
                $("#filter_lte_billDate").val(year+"-0"+month+"-0"+day);
            }else{
                $("#filter_lte_billDate").val(year+"-0"+month+"-"+day);
            }
        }else{
            $("#filter_lte_billDate").val(year+"-"+month+"-"+day);
        }
        myDate.setDate(myDate.getDate()-15);
        var yearb=myDate.getFullYear();
        var monthb=myDate.getMonth()+1;
        var dayb=myDate.getDate();
        if(monthb<10){
            if(dayb<10){
                $("#filter_gte_billDate").val(yearb+"-0"+monthb+"-0"+dayb);
            }else{
                $("#filter_gte_billDate").val(yearb+"-0"+monthb+"-"+dayb);
            }
        }else{
            $("#filter_gte_billDate").val(yearb+"-"+monthb+"-"+dayb);
        }
        search();
    }
    function saleDetail() {
        $("#searchGrid").show();
        $("#searchsaleGrid").hide();
        $("#searchsalebusinessnameGrid").hide();
        $("#searchsaleorignameGrid").hide();
        exportExcelid="searchGrid";
        $("#styleid").show();
        $("#styleids").show();
        $("#styleidname").show();
        $("#destUnitId").show();
        $("#billno").show();
        initKendoUIGrid();
    }
    function saleorderDetail(){
        $("#searchGrid").hide();
        $("#searchsaleGrid").show();
        $("#searchsalebusinessnameGrid").hide();
        $("#searchsaleorignameGrid").hide();
        exportExcelid="searchsaleGrid";
        $("#styleids").hide();
        $("#styleidname").hide();
        $("#destUnitId").show();
        $("#billno").show();
        initKendoUIGridSale();
    }
    function saleorderbusinessname(){
        $("#searchGrid").hide();
        $("#searchsaleGrid").hide();
        $("#searchsalebusinessnameGrid").show();
        $("#searchsaleorignameGrid").hide();
        exportExcelid="searchsalebusinessnameGrid";
        $("#styleid").hide();
        $("#destUnitId").hide();
        $("#billno").hide();
        if(Codes=="admin"){
            initKendoUIGridSalebusinessname();
        }else{
            initKendoUIGridSalebusinessnameno()
        }

    }
    function saleorderorigname(){
        $("#searchGrid").hide();
        $("#searchsaleGrid").hide();
        $("#searchsaleorignameGrid").show();
        $("#searchsalebusinessnameGrid").hide();
        exportExcelid="searchsaleorignameGrid";
        $("#styleid").hide();
        $("#destUnitId").hide();
        $("#billno").hide();
        if(Codes=="admin"){
            initKendoUIGridSaleorigname();
        }else{
            initKendoUIGridSaleorignameno()
        }

    }


</script>


</body>
</html>