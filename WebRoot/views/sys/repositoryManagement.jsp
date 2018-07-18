<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
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
                                        <button class="btn btn-info" onclick="refresh()">
                                            <i class="ace-icon fa fa-refresh"></i>
                                            <span class="bigger-110">刷新</span>
                                        </button>
                                    </div>

                                    <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                        <input class="form-control" id="search_organizationName" type="text"
                                               onkeyup="_search()"
                                               placeholder="模糊查询"/>
                                    </div>
                                </div>
                            </div>
                            <div class="hr hr-2 hr-dotted"></div>
                        </div>


                        <div class="row">
                            <%--左边展示树形组织架构--%>
                            <div class="col-md-5 col-lg-5">
                                <div class="widget-box widget-color-blue light-border">
                                    <div class="widget-header">
                                        <h5 class="widget-title">组织架构</h5>
                                        <div class="widget-toolbar no-border">
                                            <button class="btn btn-xs btn-success bigger" onclick="add();">
                                                <i class="ace-icon fa fa-plus"></i>
                                                新增
                                            </button>
                                        </div>
                                    </div>
                                    <div class="widget-body" style="height:600px; overflow-y:auto">
                                        <div class="widget-main no-padding">
                                            <div id="jstree"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <%--右边展示详细信息--%>
                            <div class="col-md-7 col-lg-7">
                                <div class="widget-box widget-color-blue light-border">
                                    <div class="widget-header">
                                        <h5 class="widget-title">组织信息</h5>
                                        <div class="widget-toolbar no-border">
                                            <button class="btn btn-xs btn-yellow bigger" onclick="saveEdit();">
                                                <i class="ace-icon fa fa-save"></i>
                                                保存
                                            </button>
                                        </div>
                                    </div>
                                    <div class="widget-body" style="height:600px">
                                        <div class="widget-main no-padding">
                                            <form class="form-horizontal" id="companyInfo" style="padding-top: 20px">
                                                <input type="hidden" name="id"/>

                                                <input type="hidden" id="info_creatorId" name='creatorId' hidden="true">
                                                <input type="hidden" id="info_createTime" name='createTime'
                                                       hidden="true">
                                                <div class="form-group" id="codeGroup">
                                                    <label class="col-sm-2 control-label no-padding-right"
                                                           for="info_code">编号</label>

                                                    <div class="col-xs-14 col-sm-7">
                                                        <input class="form-control" id="info_code" name="code"
                                                               type="text" readonly/>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-sm-2 control-label no-padding-right"
                                                           for="info_name">名称</label>
                                                    <div class="col-xs-14 col-sm-7">
                                                        <input class="form-control" id="info_name" name="name"
                                                               type="text" placeholder=""/>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-sm-2 control-label no-padding-right"
                                                           for="info_ownerId">所属方</label>
                                                    <div class="col-xs-14 col-sm-7">
                                                        <div class="input-group">
                                                            <input class="form-control" id="info_ownerId"
                                                                   type="text" name="ownerId" readonly/>
                                                            <span class="input-group-btn">
                                                                 <button class="btn btn-sm btn-default" disabled="disabled">
                                                                    <i class="ace-icon fa fa-list"></i>
                                                                </button>
                                                            </span>
                                                            <input class="form-control" id="info_unitName"
                                                                   type="text" name="unitName" readonly/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-sm-2 control-label no-padding-right"
                                                           for="from_tel">联系电话</label>

                                                    <div class="col-xs-14 col-sm-7">
                                                        <input class="form-control" id="from_tel" name="tel"
                                                               type="text"
                                                               placeholder=""/>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-sm-2 control-label no-padding-right"
                                                           for="info_linkman">联系人</label>

                                                    <div class="col-xs-14 col-sm-7">
                                                        <input class="form-control" id="info_linkman" name="linkman"
                                                               type="text" placeholder=""/>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-sm-2 control-label no-padding-right"
                                                           for="info_email">邮箱</label>

                                                    <div class="col-xs-14 col-sm-7">
                                                        <input class="form-control" id="info_email" name="email"
                                                               type="text" placeholder=""/>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-sm-2 control-label no-padding-right"
                                                           for="info_provinceId">所在省份</label>

                                                    <div class="col-xs-14 col-sm-7">
                                                        <input class="form-control" id="info_provinceId"
                                                               name="provinceId"
                                                               type="text" placeholder=""/>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-sm-2 control-label no-padding-right"
                                                           for="info_cityId">所在城市</label>

                                                    <div class="col-xs-14 col-sm-7">
                                                        <input class="form-control" id="info_cityId" name="cityId"
                                                               type="text" placeholder=""/>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-sm-2 control-label no-padding-right"
                                                           for="info_address">街道地址</label>

                                                    <div class="col-xs-14 col-sm-7">
                                                        <input class="form-control" id="info_address" name="address"
                                                               type="text" placeholder=""/>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-sm-2 control-label no-padding-right"
                                                           for="info_remark">备注</label>

                                                    <div class="col-xs-14 col-sm-7">
                                                        <input class="form-control" id="info_remark" name="remark"
                                                               type="text" placeholder=""/>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
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
    <jsp:include page="../layout/footer.jsp"></jsp:include>
    <!--/.fluid-container#main-container-->
</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="repositoryManagement_edit.jsp"></jsp:include>
<jsp:include page="../base/unit_dialog.jsp"></jsp:include>
<link rel="stylesheet" href="<%=basePath%>/font-awesome-4.7.0/css/font-awesome.min.css">
<script type="text/javascript" src="<%=basePath%>/views/sys/repositoryManagement.js"></script>
<script type="text/javascript">

</script>
</body>
</html>