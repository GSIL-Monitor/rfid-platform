<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017-06-20
  Time: 下午 3:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName()+":"+request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script>
        var basePath = "<%=basePath%>";
    </script>
</head>
<body class="no-skin">
<div class="main-container" id="main-container">
    <div class="row">
        <div class="col-xs-12">
            <div class ="widget-body">
                <div class="widget-toolbox padding-8 clearfix">
                    <div class="btn-toolbar" role="toolbar" id="addbutton">
                        <div class="btn-group btn-group-sm pull-left" onclick="set()">
                            <button class="btn btn-info">
                                <i class="cae-icon fa fa-refresh"></i>
                                <span class="bigger-10">套打一</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xs-12">
            <div class ="widget-body">
                <div class="widget-toolbox padding-8 clearfix">
                    <div class="btn-toolbar" role="toolbar" id="printCode">
                       <%-- <div class="btn-group btn-group-sm pull-left" onclick="refresh()">
                            <button class="btn btn-info">
                                <i class="cae-icon fa fa-refresh"></i>
                                <span class="bigger-10">套打一</span>
                            </button>
                        </div>--%>
                            <div class="btn-group btn-group-sm pull-left">
                                <input id="ordersSn" type="checkbox" name="vehicle" value="ordersSn" onclick="ordersSn()"/>
                                <span class="bigger-10">单号</span>
                            </div>
                           <%--<div class="btn-group btn-group-sm pull-left">
                                <input id="colour" type="checkbox" name="vehicle" value="colour" onclick="colour()"/>
                                <span class="bigger-10">颜色</span>
                            </div>
                            <div class="btn-group btn-group-sm pull-left">
                                <input id="size" type="checkbox" name="vehicle" value="size" onclick="size()"/>
                                <span class="bigger-10">尺码</span>
                            </div>
                            <div class="btn-group btn-group-sm pull-left">
                                <input id="number" type="checkbox" name="vehicle" value="number" onclick="number()"/>
                                <span class="bigger-10">数量</span>
                            </div>--%>
                            <div class="btn-group btn-group-sm pull-left">
                                <input id="companyName" type="checkbox" name="vehicle" value="companyName" onclick="companyName()"/>
                                <span class="bigger-10">公司名称</span>
                            </div>
                            <div class="btn-group btn-group-sm pull-left">
                                <input id="price" type="checkbox" name="vehicle" value="price" onclick="price()"/>
                                <span class="bigger-10">原价</span>
                            </div>
                            <div class="btn-group btn-group-sm pull-left">
                                <input id="remark" type="checkbox" name="vehicle" value="remark" onclick="remark()"/>
                                <span class="bigger-10">备注</span>
                            </div>
                            <div class="btn-group btn-group-sm pull-left">
                                <input id="custerm" type="checkbox" name="vehicle" value="custerm" onclick="custerm()"/>
                                <span class="bigger-10">客户</span>
                            </div>
                           <div class="btn-group btn-group-sm pull-left">
                               <input id="operatorName" type="checkbox" name="vehicle" value="operatorName" onclick="operatorName()"/>
                               <span class="bigger-10">制单人</span>
                           </div>
                           <div class="btn-group btn-group-sm pull-left">
                               <input id="storehouseName" type="checkbox" name="vehicle" value="storehouseName" onclick="storehouseName()"/>
                               <span class="bigger-10">仓库</span>
                           </div>
                           <div class="btn-group btn-group-sm pull-left">
                               <input id="orderDate" type="checkbox" name="vehicle" value="orderDate" onclick="orderDate()"/>
                               <span class="bigger-10">日期</span>
                           </div>
                           <div class="btn-group btn-group-sm pull-left">
                               <input id="printTime" type="checkbox" name="vehicle" value="printTime" onclick="printTime()"/>
                               <span class="bigger-10">打印时间</span>
                           </div>
                           <div class="btn-group btn-group-sm pull-left">
                               <input id="billtype" type="checkbox" name="vehicle" value="billtype" onclick="billtype()"/>
                               <span class="bigger-10">单据类型</span>
                           </div>

                    </div>
                </div>
            </div>
        </div>
        <div class="col-xs-12">
            <div class ="widget-body">
                <div class="widget-toolbox padding-8 clearfix">
                    <div class="btn-toolbar" role="toolbar" id="printtabCode">
                        <%-- <div class="btn-group btn-group-sm pull-left" onclick="refresh()">
                             <button class="btn btn-info">
                                 <i class="cae-icon fa fa-refresh"></i>
                                 <span class="bigger-10">套打一</span>
                             </button>
                         </div>--%>

                        <div class="btn-group btn-group-sm pull-left">
                            <input id="colour" type="checkbox" name="vehicle" value="colour" onclick="colour()"/>
                            <span class="bigger-10">颜色</span>
                        </div>
                        <div class="btn-group btn-group-sm pull-left">
                            <input id="size" type="checkbox" name="vehicle" value="size" onclick="size()"/>
                            <span class="bigger-10">尺码</span>
                        </div>
                        <div class="btn-group btn-group-sm pull-left">
                            <input id="number" type="checkbox" name="vehicle" value="number" onclick="number()"/>
                            <span class="bigger-10">数量</span>
                        </div>


                    </div>
                </div>
            </div>
        </div>
        <div class="col-xs-12">
            <div class ="widget-body">
                <div class="widget-toolbox padding-8 clearfix">
                    <div class="btn-toolbar" role="toolbar">
                        <div class="btn-group btn-group-sm pull-left">
                            <input id="name" type="text" name="vehicle"/>
                            <span class="bigger-10">模块名称</span>
                        </div>
                        <div class="btn-group btn-group-sm pull-left">
                           <%-- <input id="" type="text" name="vehicle"/>--%>
                            <select id="type">
                                <option value="PI">采购单据</option>
                                <option value="SO">销售单据</option>
                                <option value="PR">采购退货</option>
                                <option value="SR">销售退货</option>
                                <option value="TR">调拨单</option>
                            </select>
                            <span class="bigger-10">单据类型</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xs-12">
            <object id="LODOP2" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=810 height=407>
                <param name="Caption" value="内嵌显示区域">
                <param name="Border" value="1">
                <param name="Color" value="#C0C0C0">
                <embed id="LODOP_EM2" TYPE="application/x-print-lodop" width=100% height=750 PLUGINSPAGE="install_lodop.exe">
            </object>
        </div>
        <input id="saveID"type="text" style="display: none">
        <div class="modal-footer">

           <%-- <a href="#" class="btn" onclick="closeEditDialog()">关闭</a>--%>

               <button type="button" href="#" class="btn btn-primary" onclick="savetype()">保存</button>
               <button type="button" href="#" class="btn btn-primary" onclick="test()">修改</button>

        </div>

    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>
</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="../base/unit_dialog.jsp"></jsp:include>
<jsp:include page="print_one.jsp"></jsp:include>
<jsp:include page="print_two.jsp"></jsp:include>
<jsp:include page="print_threes.jsp"></jsp:include>
<script src="<%=basePath%>/views/sys/printController.js"></script>
<script src="<%=basePath%>/Olive/plugin/print/LodopFuncs.js"></script>

</body>
</html>
