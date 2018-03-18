<%--
  Created by IntelliJ IDEA.
  User: yushen
  Date: 2017/8/23
  Time: 11:30
  To change this template use File | Settings | File Templates.
--%>
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
        <div class="row">
            <div class="col-xs-12">
                <div class="widget-box widget-color-blue  light-border">
                    <div class="widget-header">
                        <h5 class="widget-title">原始吊牌信息</h5>
                    </div>
                    <div class="widget-body">
                        <div class="widget-main padding-12">
                            <form class="form-horizontal" id="tagOrigForm" onkeydown="if(event.keyCode==13)return false;">
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="orig_code">扫描唯一码</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="orig_code" name="code"
                                               type="text"/>
                                    </div>
                                    <label class="col-xs-1 control-label" for="orig_styleId">款号</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="orig_styleId" name="styleId"
                                               type="text" readonly/>
                                    </div>
                                    <label class="col-xs-1 control-label" for="orig_styleName">款名</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="orig_styleName" name="styleName"
                                               type="text" readonly/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-1 control-label" for="orig_colorId">颜色</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="orig_colorId" name="colorId"
                                               type="text" readonly/>
                                    </div>

                                    <label class="col-xs-1 control-label" for="orig_sizeId">尺寸</label>
                                    <div class="col-xs-2">
                                        <input class="form-control" id="orig_sizeId" name="sizeId"
                                               type="text" readonly/>
                                    </div>

                                    <label class="col-xs-1 control-label" for="orig_price">吊牌价</label>
                                    <div class="col-xs-2">
                                        <div class="input-group">
                                            <span class="input-group-addon"><i class="fa fa-jpy"></i></span>
                                            <input class="form-control" id="orig_price" name="price"
                                                   type="number" readonly/>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <div class="widget-box widget-color-blue light-border">
                    <div class="row">
                        <div class="col-xs-12 col-sm-6 widget-container-col">
                            <div class="widget-box widget-color-blue  light-border">
                                <div class="widget-header">
                                    <h5 class="widget-title">替换吊牌信息</h5>
                                </div>
                                <div class="widget-body">
                                    <div class="widget-main padding-12">
                                        <form class="form-horizontal" id="tagReplaceForm">
                                            <div class="form-group">
                                                <label class="col-xs-1 control-label" for="replace_styleId">款号</label>
                                                <div class="col-xs-4">
                                                    <input class="form-control" id="replace_styleId" name="styleId"
                                                           type="text" readonly/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-1 control-label" for="replace_styleName">款名</label>
                                                <div class="col-xs-4">
                                                    <input class="form-control" id="replace_styleName" name="styleName"
                                                           type="text" readonly/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-1 control-label" for="replace_price">吊牌价</label>
                                                <div class="col-xs-4">
                                                    <div class="input-group">
                                                        <span class="input-group-addon"><i class="fa fa-jpy"></i></span>
                                                        <input class="form-control" id="replace_price" name="price"
                                                               type="number" readonly/>
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-6 widget-container-col">
                            <div class="widget-box widget-color-blue  light-border">
                                <div class="widget-header">
                                    <h5 class="widget-title lighter">色码列表</h5>
                                </div>
                                <div class="widget-body">
                                    <div class="widget-main padding-12 no-padding-left no-padding-right">
                                        <div class="tab-content padding-4">
                                            <table id="codeReplace_CSGrid"></table>
                                            <div id="codeReplace_CSPager"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>


                    <div class="widget-box widget-color-blue light-border">
                        <div class="widget-header">
                            <h5 class="widget-title">新吊牌信息核对</h5>
                        </div>
                        <div class="widget-body">
                            <div class="widget-main padding-12">
                                <form class="form-horizontal" id="newTagForm" onkeydown="if(event.keyCode==13)return false;">
                                    <div class="form-group">
                                        <label class="col-xs-1 control-label" for="newTag_code">扫描唯一码</label>
                                        <div class="col-xs-2">
                                            <input class="form-control" id="newTag_code" name="code"
                                                   type="text"/>
                                        </div>
                                        <label class="col-xs-1 control-label" for="newTag_styleId">款号</label>
                                        <div class="col-xs-2">
                                            <input class="form-control" id="newTag_styleId" name="styleId"
                                                   type="text" readonly/>
                                        </div>
                                        <label class="col-xs-1 control-label" for="newTag_styleName">款名</label>
                                        <div class="col-xs-2">
                                            <input class="form-control" id="newTag_styleName" name="styleName"
                                                   type="text" readonly/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-1 control-label" for="newTag_colorId">颜色</label>
                                        <div class="col-xs-2">
                                            <input class="form-control" id="newTag_colorId" name="colorId"
                                                   type="text" readonly/>
                                        </div>

                                        <label class="col-xs-1 control-label" for="newTag_sizeId">尺寸</label>
                                        <div class="col-xs-2">
                                            <input class="form-control" id="newTag_sizeId" name="sizeId"
                                                   type="text" readonly/>
                                        </div>
                                        <label class="col-xs-1 control-label" for="newTag_price">吊牌价</label>
                                        <div class="col-xs-2">
                                            <div class="input-group">
                                                <span class="input-group-addon"><i class="fa fa-jpy"></i></span>
                                                <input class="form-control" id="newTag_price" name="price"
                                                       type="number" readonly/>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-sm-offset-5 col-sm-10">
                    <button class="btn btn-sm btn-primary" type="button" id="produceNewCode"
                            onclick="produceNewTag()">
                        <i class="ace-icon fa fa-save"></i>
                        <span class="bigger-110">导入新标签</span>
                    </button>
                    <button class="btn btn-sm btn-primary" type="button" id="confirmReplaceTag"
                            style="margin-left: 20px" onclick="confirmReplaceTag()">
                        <i class="ace-icon fa fa-save"></i>
                        <span class="bigger-110">更新标签信息</span>
                    </button>
                    <button class="btn btn-sm btn-primary" type="button" id="viewRecord"
                            style="margin-left: 20px" onclick="viewReplaceRecord()">
                        <i class="ace-icon fa fa-list"></i>
                        <span class="bigger-110">查看替换记录</span>
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="../layout/footer.jsp"></jsp:include>
<!--/.fluid-container#main-container-->
</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<script src="<%=basePath%>/views/tag/tagReplaceController.js"></script>
</body>
</html>