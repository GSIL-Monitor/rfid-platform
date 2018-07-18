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
        var roleid="${roleid}"
    </script>
    <style type="text/css">
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
                                   <button class="btn btn-info" onclick="purchaseDetail();">
                                       <i class="ace-icon"></i>
                                       <span class="bigger-110">采购明细</span>
                                   </button>
                               </div>
                               <div class="btn-group btn-group-sm pull-left">
                                   <button class="btn btn-info" onclick="purchaseBill();">
                                       <i class="ace-icon"></i>
                                       <span class="bigger-110">按单据汇总</span>
                                   </button>
                               </div>
                                <div class="btn-group btn-group-sm pull-left">
                                    <button class="btn btn-info" onclick="purchaseBystyeid();">
                                        <i class="ace-icon"></i>
                                        <span class="bigger-110">按商品汇总</span>
                                    </button>
                                </div>
                                <div class="btn-group btn-group-sm pull-left">
                                    <button class="btn btn-info" onclick="purchaseBydestunitid();">
                                        <i class="ace-icon"></i>
                                        <span class="bigger-110">按厂家汇总</span>
                                    </button>
                                </div>
                               <%--<div class="btn-group btn-group-sm pull-left">
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
                               </div>--%>
                            </div>

                            <div class="widget-toolbox padding-8 clearfix">
                                <div class="btn-toolbar" role="toolbar">
                                    <div class="btn-group btn-group-sm pull-left">
                                        <button class="btn btn-info" onclick="refresh();">
                                            <i class="ace-icon fa fa-refresh"></i>
                                            <span class="bigger-110">刷新</span>
                                        </button>
                                    </div>

                                 <%--   <div class="btn-group btn-group-sm pull-left">

                                        <button class="btn btn-info" onclick="exportExcel();">

                                            <i class="ace-icon fa fa-file-excel-o"></i>
                                            <span class="bigger-110">导出</span>

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
                                        <div id="intimeDateShow">
                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_gte_intimeDate">入库时间</label>

                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                <div class="input-group">
                                                    <input  id="filter_gte_intimeDate" class="form-control date-picker" name="filter_gte_intimeDate"
                                                            data-date-format="yyyy-mm-dd"/>
                                                    <span class="input-group-addon">
                                                                <i class="fa fa-exchange"></i>
                                                          </span>
                                                    <input  id="filter_lte_intimeDate" class="form-control date-picker"  name="filter_lte_intimeDate"
                                                            data-date-format="yyyy-mm-dd"/>
                                                </div>

                                             </div>
                                        </div>
                                        <%--  <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_contains_taskId">任务号</label>

                                          <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                              <input class="form-control" id="filter_contains_taskId" name="filter_contains_taskId" type="text" onkeyup="this.value=this.value.toUpperCase()"
                                                     placeholder="模糊查询"/>
                                          </div>--%>
                                        <div id="billno">
                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_contains_billid">单号</label>

                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                <input class="form-control" id="filter_contains_billid" name="filter_contains_billid" type="text" onkeyup="this.value=this.value.toUpperCase()"
                                                       placeholder="模糊查询"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                         <div id="class1">
                                           <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_eq_class1">厂家</label>

                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                <!-- <input class="form-control" id="filter_contains_styleName" name="filter_contains_styleName" type="text"
                                                       placeholder="模糊查询"/> -->
                                                <div class="input-group">
                                                    <input class="form-control" id="filter_eq_class1"
                                                           type="text" name="filter_eq_class1" readonly/>
                                                    <span class="input-group-btn">
                                                         <button class="btn btn-sm btn-default" type="button" onclick="initClass1Select_Grid('#filter_eq_class1','#filter_eq_class1Name')">
                                                             <i class="ace-icon fa fa-list"></i>
                                                         </button>
								                      </span>
                                                    <input class="form-control" id="filter_eq_class1Name"
                                                           type="text" name="filter_eq_class1Name" readonly  placeholder="厂家"/>
                                                </div>
                                            </div>
                                         </div>
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_in_destid">仓库</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <select class="form-control selectpicker show-tick" id="filter_in_destid" name="filter_in_destid" aria-expanded="true"
                                                    style="width: 100%;" data-live-search="true">
                                            </select>
                                        </div>
                                        <div id="orderwarehouseidShow">
                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_in_orderwarehouseid">订货仓库</label>
                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                <select class="form-control selectpicker show-tick" id="filter_in_orderwarehouseid" name="filter_in_orderwarehouseid"
                                                        style="width: 100%;" data-live-search="true">
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group" id="styleid">
                                        <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_eq_styleid">款号</label>
                                        <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                            <div class="input-group">
                                                <input class="form-control" id="filter_eq_styleid"
                                                       type="text" name="filter_eq_styleid" readonly/>
                                                <span class="input-group-btn">
                                                         <button class="btn btn-sm btn-default" type="button" onclick="openstyleDialog('#filter_eq_styleid','#filter_eq_stylename')">
                                                             <i class="ace-icon fa fa-list"></i>
                                                         </button>
								                      </span>
                                                <input class="form-control" id="filter_eq_stylename"
                                                       type="text"  readonly  placeholder="款名"/>
                                            </div>
                                        </div>
                                        <div id="destunitid">
                                            <label class="col-xs-4 col-sm-4 col-md-1 col-lg-1 control-label text-right" for="filter_eq_class1">供应商</label>

                                            <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                <!-- <input class="form-control" id="filter_contains_styleName" name="filter_contains_styleName" type="text"
                                                       placeholder="模糊查询"/> -->
                                                <div class="input-group">
                                                    <input class="form-control" id="search_destUnitId"
                                                           type="text" name="filter_eq_destunitid" readonly/>
                                                    <span class="input-group-btn">
                                                             <button class="btn btn-sm btn-default" type="button" onclick="openSearchVendorDialog()">
                                                                 <i class="ace-icon fa fa-list"></i>
                                                             </button>
                                                          </span>
                                                    <input class="form-control" id="search_destUnitName"
                                                           type="text" name="filter_eq_destunitname" readonly  placeholder="供应商"/>
                                                </div>
                                                <%--<select class="form-control" id="filter_eq_destunitid" name="filter_eq_destunitid"
                                                        style="width: 100%;" >
                                                </select>--%>
                                            </div>
                                        </div>



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
                                            <button id="clearMessage" style="display: none" type="reset" class="btn btn-sm btn-warning">
                                                <i class="ace-icon fa fa-undo"></i>
                                                <span class="bigger-110">清空</span></button>
                                        </div>
                                    </div>

                                </form>


                            </div>
                        </div>
                        <div style="font-size:0px;" ng-show="showCountData" id="noadmin">
                            <div class="data-container1">
                                <span class="title">采购单品数</span>
                                <span class="title purchasesum" id ="purchasesum"ng-show="sumDataLoading">&nbsp;&nbsp;查询中...</span>
                                <%--<span class="text" ng-bind = "sumQuantity" ng-show="!sumDataLoading"></span>--%>
                            </div>
                            <div class="data-container1">
                                <span class="title">采购数</span>
                                <span class="title purchasonesum" id ="purchasonesum"ng-show="sumDataLoading">&nbsp;&nbsp;查询中...</span>
                                <%--<span class="text" ng-bind = "sumQuantity" ng-show="!sumDataLoading"></span>--%>
                            </div>
                            <div class="data-container1">
                                <span class="title">采购金额</span>
                                <span class="title purchasmony" id="purchasmony"ng-show="sumDataLoading">&nbsp;&nbsp;查询中...</span>
                                <%--<span class="text" ng-bind = "sumTrueAmount|currency:'￥'" ng-show="!sumDataLoading"></span>--%>
                            </div>
                        </div>
                        <div id="searchGrid" style="height:800px"></div>
                       <%-- <div id="nosearchGrid" style="height:800px;display: none"></div>--%>
                        <div id="searchpuchaseGrid" style="height:800px;display: none"></div>
                        <div id="searchpuchaseBystyeidGrid" style="height:800px;display: none"></div>
                        <div id="searchpuchaseBydestunitidGrid" style="height:800px;display: none"></div>
                        <%-- <div id="searchsalebusinessnameGrid" style="height:800px;display: none"></div>
                        <div id="searchsaleorignameGrid" style="height:800px;display: none"></div>
--%>
                        <form  id="form1" action="" method=post name=form1 style='display:none'>
                            <input id="gridId" type=hidden  name='gridId' value=''>
                            <input id="request" type=hidden  name='request' value=''>
                        </form>
                        <div id ="divshowImage" class="divshowImage" style="display: none">
                            <img class="showImage" id="showImage" onclick="hideImage()">
                        </div>

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
<jsp:include page="../base/search_class1_dialog.jsp"></jsp:include>
<jsp:include page="../base/search_vendor_dialog.jsp"></jsp:include>
<%--<jsp:include page="../base/search_guest_dialog.jsp"></jsp:include>--%>
<script type="text/javascript" src="<%=basePath%>/views/search/PurchaseorCountviewsSearchController.js"></script>
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
    function onIntimeday() {
        var myDate=new Date();
        var year=myDate.getFullYear();
        var month=myDate.getMonth()+1;
        var day=myDate.getDate();
        if(month<10){
            if(day<10){
                $("#filter_gte_intimeDate").val(year+"-0"+month+"-0"+day);
                $("#filter_lte_intimeDate").val(year+"-0"+month+"-0"+day);
            }else{
                $("#filter_gte_intimeDate").val(year+"-0"+month+"-"+day);
                $("#filter_lte_intimeDate").val(year+"-0"+month+"-"+day);
            }
        }else{
            $("#filter_gte_intimeDate").val(year+"-"+month+"-"+day);
            $("#filter_lte_intimeDate").val(year+"-"+month+"-"+day);
        }

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
    function purchaseDetail() {
        $("#searchGrid").show();
        $("#searchpuchaseGrid").hide();
        $("#searchpuchaseBystyeidGrid").hide();
        $("#searchpuchaseBydestunitidGrid").hide();
        $("#styleid").show();
        $("#intimeDateShow").show();
        $("#taskDestidShow").show();
        $("#destunitid").show();
        $("#class1").show();
        $("#clearMessage").click();
        onIntimeday();
        exportExcelid="searchGrid";
        initKendoUIGrid();
    }
    function purchaseBill() {
        $("#searchGrid").hide();
        $("#searchpuchaseGrid").show();
        $("#searchpuchaseBystyeidGrid").hide();
        $("#searchpuchaseBydestunitidGrid").hide();
        $("#intimeDateShow").show();
        $("#orderwarehouseidShow").show();
        $("#styleid").hide();
        $("#destunitid").hide();
        $("#class1").hide();
        $("#clearMessage").click();
        onIntimeday();
        exportExcelid="searchpuchaseGrid";
        initKendoUIPurchaseGrid();
    }
    function purchaseBystyeid() {

        $("#searchGrid").hide();
        $("#searchpuchaseGrid").hide();
        $("#searchpuchaseBystyeidGrid").show();
        $("#searchpuchaseBydestunitidGrid").hide();
        $("#styleid").show();
        $("#intimeDateShow").hide();
        $("#orderwarehouseidShow").hide();
        $("#destunitid").hide();
        $("#class1").hide();
        $("#clearMessage").click();
        onday();
        exportExcelid="searchpuchaseBystyeidGrid";
        initKendoUIPurchasestyeidGrid();
    }
    function purchaseBydestunitid() {
        $("#searchGrid").hide();
        $("#searchpuchaseGrid").hide();
        $("#searchpuchaseBystyeidGrid").hide();
        $("#searchpuchaseBydestunitidGrid").show();
        $("#styleid").show();
        $("#intimeDateShow").hide();
        $("#orderwarehouseidShow").hide();
        $("#destunitid").hide();
        $("#class1").hide();
        $("#clearMessage").click();
        onday();
        exportExcelid="searchpuchaseBydestunitidGrid";
        initKendoUIPurchasedestunitidGrid();
    }
</script>
</body>
</html>