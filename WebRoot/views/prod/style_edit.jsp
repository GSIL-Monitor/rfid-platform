<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <link rel="stylesheet" href="<%=basePath%>/Olive/assets/css/bootstrap-multiselect.css"/>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
        var userId = "${userId}";
        var styleId = "${styleId}";
        var pageType = "${pageType}";
        var remark  = "${style.remark}";
        var fieldList = ${fieldList};
    </script>
    <style>
        a#focusColor:hover {
            background-color: #bbbbbb;
        }

        input[type=number] {
            -moz-appearance: textfield;
        }

        input[type=number]::-webkit-inner-spin-button,
        input[type=number]::-webkit-outer-spin-button {
            -webkit-appearance: none;
            margin: 0;
        }
    </style>
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
            <div class="breadcrumbs" id="breadcrumbs">
                <script type="text/javascript">
                    try {
                        ace.settings.check('breadcrumbs', 'fixed')
                    } catch (e) {
                    }
                </script>

                <ul class="breadcrumb">
                    <li>
                        <a href="#" onclick="toIndex()">商品款式</a>
                    </li>
                    <li class="active">
                        <c:if test="${pageType == 'add'}">
                            增加
                        </c:if>
                        <c:if test="${pageType == 'edit'}">
                            编辑
                        </c:if>
                    </li>
                </ul><!-- /.breadcrumb -->


            </div>

            <div id="page-content">

                <div class="row">
                    <div class="col-xs-12">
                        <!-- PAGE CONTENT BEGINS -->
                        <div class="widget-body">


                            <div class="widget-main col-sm-6">
                                <div class="col-xs-12 col-sm-12">
                                    <div class="widget-box widget-color-blue  light-border">
                                        <div class="widget-header">
                                            <h5 class="widget-title">款式基本信息</h5>
                                            <div class="widget-toolbar">

                                            </div>
                                        </div>
                                        <br/>
                                        <form class="form-horizontal" role="form" id="editStyleForm"
                                              onkeydown="if(event.keyCode==13)return false;">
                                            <div class="form-group">
                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_styleId"><span class="text-danger">* </span>款号</label>

                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <input class="form-control" id="form_styleId" name="styleId"
                                                           type="text" placeholder="" value="${style.styleId}"
                                                           onkeyup="this.value=this.value"/>
                                                </div>

                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_styleName"><span
                                                        class="text-danger">* </span>款名</label>

                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <input class="form-control" id="form_styleName" name="styleName"
                                                           type="text" placeholder="" value="${style.styleName}"/>
                                                </div>

                                            </div>
                                            <div class="form-group" id="form-group-preCast">
                                                <div id="style_preCase_div">
                                                    <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                           for="form_preCast"><span class="text-danger">* </span>采购价</label>

                                                    <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                        <div class="input-group">
                                                            <span class="input-group-addon"><i class="fa fa-jpy"></i></span>
                                                            <input class="form-control" id="form_preCast" name="preCast"
                                                                   required="required" type="number" placeholder=""
                                                                   value="${style.preCast}" step="0.01"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div id="style_puPrice_div">
                                                    <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                           for="form_puPrice"><span
                                                            class="text-danger">* </span>代理商价</label>

                                                    <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                        <div class="input-group">
                                                            <span class="input-group-addon"><i class="fa fa-jpy"></i></span>
                                                            <input class="form-control" id="form_puPrice" name="puPrice"
                                                                   readonly
                                                                   required="required" type="number" placeholder=""
                                                                   value="${style.puPrice}" step="0.01"/>
                                                        </div>
                                                    </div>
                                                </div>

                                            </div>

                                            <div class="form-group">
                                                <div id="style_wsPrice_div">
                                                    <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                           for="form_wsPrice"><span class="text-danger">* </span>门店价</label>

                                                    <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                        <div class="input-group">
                                                            <span class="input-group-addon"><i class="fa fa-jpy"></i></span>
                                                            <input class="form-control" id="form_wsPrice" name="wsPrice"
                                                                   readonly
                                                                   required="required" type="number" placeholder=""
                                                                   value="${style.wsPrice}" step="0.01"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div id="style_price_div">
                                                    <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                           for="form_price"><span class="text-danger">* </span>吊牌价</label>

                                                    <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                        <div class="input-group">
                                                            <span class="input-group-addon"><i class="fa fa-jpy"></i></span>
                                                            <input class="form-control" id="form_price" name="price"
                                                                   readonly
                                                                   required="required" type="number" placeholder=""
                                                                   value="${style.price}" step="0.01"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div id="style_bargainPrice_div">
                                                    <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                           for="form_bargainPrice"><span class="text-danger">* </span>特价</label>
                                                    <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                        <div class="input-group">
                                                            <span class="input-group-addon"><i class="fa fa-jpy"></i></span>
                                                            <input class="form-control" id="form_bargainPrice" name="bargainPrice"
                                                                   required="required" type="number" placeholder=""
                                                                   value="${style.bargainPrice}" step="0.01"/>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">

                                            </div>
                                            <input id="form_remark" name ="remark" type="hidden"/>
                                            <div class="form-group">
                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_class1">品牌</label>
                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <div class="input-group ">
                                                        <select class="chosen-select form-control" id="form_class1"
                                                                name="class1">
                                                            <option value='' style='background-color: #eeeeee'>请选择品牌
                                                            </option>
                                                            ")
                                                        </select>
                                                        <%--  <select id="form_brandCode" class="selectpicker" data-live-search="true">
                                                          </select>--%>
                                                        <span class="input-group-addon"
                                                              title="添加${classTypes[0].value}">
                                                            <a href='#' class="white"
                                                               onclick="addStyleProperty('${classTypes[0].id}')">
                                                            <i class="fa fa-plus red"></i>
                                                            </a>
                                                        </span>
                                                    </div>
                                                </div>

                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_class2">${classTypes[1].value}</label>
                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <div class="input-group ">
                                                        <select class="chosen-select form-control" id="form_class2"
                                                                name="class2">
                                                            <option value="" style="background-color: #eeeeee">
                                                                请选择${classTypes[1].value}</option>
                                                        </select>
                                                        <span class="input-group-addon"
                                                              title="添加${classTypes[1].value}">
                                                            <a href='#' class="white"
                                                               onclick="addStyleProperty('${classTypes[1].id}')">
                                                            <i class="fa fa-plus red"></i>
                                                            </a>
                                                        </span>
                                                    </div>
                                                </div>


                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_class3">${classTypes[2].value}</label>
                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <div class="input-group ">
                                                        <%--<select class="chosen-select form-control" id="form_class3" name="class3">
                                                            <option value="" style="background-color: #eeeeee">--请选择${classTypes[2].value}--</option>
                                                        </select>--%>
                                                        <select class="chosen-select form-control" id="form_class3"
                                                                name="class3">
                                                            <option value="" style="background-color: #eeeeee">
                                                                请选择${classTypes[2].value}</option>
                                                        </select>
                                                        <span class="input-group-addon"
                                                              title="添加${classTypes[2].value}">
                                                            <a href='#' class="white"
                                                               onclick="addStyleProperty('${classTypes[2].id}')">
                                                            <i class="fa fa-plus red"></i>
                                                            </a>
                                                        </span>
                                                    </div>
                                                </div>
                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_class4">${classTypes[3].value}</label>
                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <div class="input-group ">
                                                        <%--<select class="chosen-select form-control" id="form_class4" name="class4">
                                                            <option value="" style="background-color: #eeeeee">--请选择${classTypes[3].value}--</option>
                                                        </select>--%>
                                                        <select class="chosen-select form-control" id="form_class4"
                                                                name="class4">
                                                            <option value="" style="background-color: #eeeeee">
                                                                请选择${classTypes[3].value}</option>
                                                        </select>
                                                        <span class="input-group-addon"
                                                              title="添加${classTypes[3].value}">
                                                            <a href='#' class="white"
                                                               onclick="addStyleProperty('${classTypes[3].id}')">
                                                            <i class="fa fa-plus red"></i>
                                                            </a>
                                                        </span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_class5">${classTypes[4].value}</label>
                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <div class="input-group ">
                                                        <%--<select class="chosen-select form-control" id="form_class5" name="class5">
                                                            <option value="" style="background-color: #eeeeee">--请选择无${classTypes[4].value}--</option>
                                                        </select>--%>
                                                        <select class="chosen-select form-control" id="form_class5"
                                                                name="class5">
                                                            <option value="" style="background-color: #eeeeee">
                                                                请选择${classTypes[4].value}</option>
                                                        </select>
                                                        <span class="input-group-addon"
                                                              title="添加${classTypes[4].value}">
                                                            <a href='#' class="white"
                                                               onclick="addStyleProperty('${classTypes[4].id}')">
                                                            <i class="fa fa-plus red"></i>
                                                            </a>
                                                        </span>
                                                    </div>
                                                </div>
                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_class6">${classTypes[5].value}</label>
                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <div class="input-group ">
                                                        <%-- <select class="chosen-select form-control" id="form_class6" name="class6">
                                                         </select>--%>
                                                        <select class="chosen-select form-control" id="form_class6"
                                                                name="class6">
                                                        </select>
                                                        <span class="input-group-addon"
                                                              title="添加${classTypes[5].value}">
                                                            <a href='#' class="white"
                                                               onclick="addStyleProperty('${classTypes[5].id}')">
                                                            <i class="fa fa-plus red"></i>
                                                            </a>
                                                        </span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_class5">${classTypes[7].value}</label>
                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <div class="input-group ">
                                                        <%--<select class="chosen-select form-control" id="form_class8" name="class8">
                                                            <option value="" style="background-color: #eeeeee">--请选择无${classTypes[7].value}--</option>
                                                        </select>--%>
                                                        <select class="chosen-select form-control" id="form_class8"
                                                                name="class8">
                                                            <option value="" style="background-color: #eeeeee">
                                                                请选择无${classTypes[7].value}</option>
                                                        </select>
                                                        <span class="input-group-addon"
                                                              title="添加${classTypes[7].value}">
                                                            <a href='#' class="white"
                                                               onclick="addStyleProperty('${classTypes[7].id}')">
                                                            <i class="fa fa-plus red"></i>
                                                            </a>
                                                        </span>
                                                    </div>
                                                </div>
                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_class6">${classTypes[9].value}</label>
                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <div class="input-group ">
                                                        <%--<select class="chosen-select form-control" id="form_class10" name="class10">
                                                            <option value="" style="background-color: #eeeeee">--请选择${classTypes[9].value}--</option>
                                                        </select>--%>
                                                        <select class="chosen-select form-control" id="form_class10"
                                                                name="class10">
                                                            <option value="" style="background-color: #eeeeee">
                                                                请选择${classTypes[9].value}</option>
                                                        </select>
                                                        <span class="input-group-addon"
                                                              title="添加${classTypes[9].value}">
                                                            <a href='#' class="white"
                                                               onclick="addStyleProperty('${classTypes[9].id}')">
                                                            <i class="fa fa-plus red"></i>
                                                            </a>
                                                        </span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_class7">${classTypes[6].value}</label>
                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <div class="input-group ">
                                                        <%--<select class="chosen-select form-control" id="form_class7" name="class7">
                                                            <option value="" style="background-color: #eeeeee">--请选择${classTypes[6].value}--</option>
                                                        </select>--%>
                                                        <select class="chosen-select form-control" id="form_class7">
                                                            <option value="" style="background-color: #eeeeee">
                                                                请选择${classTypes[6].value}</option>
                                                        </select>
                                                        <span class="input-group-addon"
                                                              title="添加${classTypes[6].value}">
                                                            <a href='#' class="white"
                                                               onclick="addStyleProperty('${classTypes[6].id}')">
                                                            <i class="fa fa-plus red"></i>
                                                            </a>
                                                        </span>
                                                    </div>
                                                </div>

                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_class6">${classTypes[8].value}</label>
                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <div class="input-group ">
                                                        <select class="chosen-select form-control" id="form_class9"
                                                                name="class9">
                                                        </select>
                                                        <span class="input-group-addon"
                                                              title="添加${classTypes[8].value}">
                                                            <a href='#' class="white"
                                                               onclick="addStyleProperty('${classTypes[8].id}')">
                                                            <i class="fa fa-plus red"></i>
                                                            </a>
                                                        </span>
                                                    </div>
                                                </div>

                                            </div>

                                            <div class="form-group">
                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_sizeSortId">尺寸组</label>
                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">

                                                    <%--<select class="form-control" id="form_sizeSortId" name="sizeSortId"
                                                            placeholder=""></select>--%>
                                                    <select class="chosen-select form-control" id="form_sizeSortId"
                                                            name="sizeSortId">
                                                    </select>
                                                </div>
                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_styleEname">英文名</label>
                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <input class="form-control" id="form_styleEname" name="styleEname"
                                                           type="text" placeholder="" value="${style.styleEname}"/>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <div id="style_ispush_div">
                                                    <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                           for="form_ispush">是否推送</label>
                                                    <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                        <select class="form-control" id="form_ispush" name="ispush"
                                                                value="${style.ispush}"/>
                                                        <option value="N" style="background-color: #eeeeee">否</option>
                                                        <option value="Y" style="background-color: #eeeeee">是</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div id="style_isSeries_div">
                                                    <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                           for="form_isSeries">是否用定价规则</label>
                                                    <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                        <select class="form-control" id="form_isSeries"
                                                                onchange="priceIsUse();" name="isSeries"/>
                                                        <option value="Y" style="background-color: #eeeeee">是</option>
                                                        <option value="N" style="background-color: #eeeeee">否</option>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <div id="style_styleCycle_div">
                                                    <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                           for="form_styleCycle">退换周期(天)</label>
                                                    <%--<div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">--%>
                                                    <%--<select class="chosen-select form-control" id="form_styleCycle"--%>
                                                    <%--name="styleCycle">--%>
                                                    <%--</select>--%>
                                                    <%--</div>--%>
                                                    <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                        <div class="input-group">
                                                            <c:if test="${pageType == 'edit'}">
                                                                <input class="form-control"
                                                                       id="form_styleCycle"
                                                                       name="styleCycle"
                                                                       required="required"
                                                                       type="number"
                                                                       placeholder=""
                                                                       value="${style.styleCycle}"
                                                                       step="1"/>
                                                            </c:if>
                                                            <c:if test="${pageType == 'add'}">
                                                                <input class="form-control"
                                                                       id="form_styleCycle"
                                                                       name="styleCycle"
                                                                       required="required"
                                                                       type="number"
                                                                       placeholder=""
                                                                       value="20" step="1"/>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                        <form class="form-horizontal">
                                            <div class="form-group">
                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="tags_3">成分</label>
                                                <div class="col-xs-7 col-sm-7">
                                                    <div class="input-group ">
                                                    <textarea maxlength="400" class="form-control" id="tags_3"
                                                              name="remark">
                                                    </textarea>
                                                    <span class="input-group-addon"
                                                          title="添加${classTypes[10].value}">
                                                            <a href='#' class="white"
                                                               onclick="addStyleProperty('${classTypes[10].id}')">
                                                            <i class="fa fa-plus red"></i>
                                                            </a>
                                                        </span>
                                                    </div>
                                                </div>

                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                            <div class="widget-main col-sm-6">
                                <div class="col-xs-12 col-sm-12">
                                    <div class="widget-box widget-color-blue  light-border" id="parentWidth">
                                        <div class="widget-header">
                                            <h5 class="widget-title">色码列表</h5>
                                            <div class="widget-toolbar">
                                                <%--<Button onclick="addColorAndSize()" class="btn btn-info btn-sm">--%>
                                                <a id="focusColor" href="javascript:void(0)" class="white"
                                                   onclick="addColorAndSize()">
                                                    <i class="ace-icon fa fa-plus"></i>
                                                    <span class="bigger-110 ">增加</span>
                                                </a>
                                                <%-- <a  id="deleteColorSize" href="javascript:void(0)" class="white" onclick="deleteColorSize()">
                                                     <i class="ace-icon fa fa-close"></i>
                                                     <span class="bigger-110 ">删除</span>
                                                 </a>--%>
                                                <%--</Button>--%>
                                            </div>
                                        </div>

                                        <table id="CSGrid"></table>
                                        <table id="CSPage"></table>

                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-offset-5 col-sm-10">
                                <button class="btn btn-xs btn-success" type="button"
                                        id="saveStyleBtn"
                                        onclick="saveStyleAndProduct('${pageType}')">
                                    <i class="ace-icon fa fa-save"></i>
                                    <span class="bigger-110">保存</span>
                                </button>
                            </div>
                        </div>
                    </div>
                    <!-- /.col -->
                </div>
                <!-- /.row -->
                <!--/#page-content-->
            </div>
        </div>
    </div>

</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>

<link rel="stylesheet" href="<%=basePath%>/Olive/assets/css/bootstrap-colorpicker.min.css"/>
<script type="text/javascript" src="<%=basePath%>/Olive/assets/js/bootstrap-colorpicker.min.js"></script>
<link rel="stylesheet" href="<%=basePath%>/Olive/assets/JSON/jquery.tagsinput.css"/>
<script src="<%=basePath%>/Olive/assets/JSON/jquery.tagsinput.js"></script>
<jsp:include page="style_colorAndSize_dialog.jsp"></jsp:include>
<jsp:include page="style_color_edit.jsp"></jsp:include>
<jsp:include page="style_size_edit.jsp"></jsp:include>
<jsp:include page="../sys/property_edit_ Detailed.jsp"></jsp:include>
<jsp:include page="changeRemark_dialog.jsp"></jsp:include>
<script src="<%=basePath%>/Olive/assets/js/bootstrap-multiselect.js"></script>


<script type="text/javascript">
    var checkNum;
    $(function () {
        loadingButton();
        $("#tags_3").val(remark);
        $('#tags_3').tagsInput({
            width: '540',
            height:'70',
            autocomplete_url:'remark.do'
        });
        iniGrid();
        inputPriceKeydown();
        inputPriceKeydowno();


        initeditStyleFormValid();

        initSelect();
        setTimeout(function () {
            setUrl();
        }, 500);
        $("#form_sizeId").multiselect({
            inheritClass: true,
            includeSelectAllOption: true,
            selectAllNumber: true,
            enableClickableOptGroups: true,
            enableCollapsibleOptGroups: true,
            enableFiltering: true,
            filterPlaceholder: "请输入尺寸",
            maxHeight: "400"
        });
        $("div.btn-group,div.btn-group>button,div.btn-group>ul").addClass("col-sm-12");
        $("div.btn-group").css("padding", "0");

    });

    $("#form_bargainPrice").blur(function(){
        var bargainPrice = $("#form_bargainPrice").val();
        var price = $("#form_price").val();
        if (parseInt(bargainPrice)>parseInt(price)){
            $.gritter.add({
                text: "特价价格不能高于吊牌价",
                class_name: 'gritter-success  gritter-light'
            });
            $("#form_bargainPrice").val("");
        }
    });

    //更改select背景色
    function changeBackColor() {
        var color = $("#form_colorId_select").find("option:selected").css("background-color");
        $("#form_colorId_select").css("background-color", color);
        console.log(color);
    }

    function initSelect() {
        $.ajax({
            url: basePath + "/sys/property/searchByType.do?type=C1",
            cache: false,
            async: true,
            type: "POST",
            success: function (data, textStatus) {
                var json = data;
                $("#form_class1").empty();
                $("#form_class1").multiselect({
                    inheritClass: true,
                    includeSelectAllOption: true,
                    selectAllNumber: true,
                    enableFiltering: true,
                    filterPlaceholder: "请选择品牌",
                    maxHeight: "400",
                    onChange: function (option, checked) {//change事件改变
                        //console.log(option.length + ' options ' + (checked ? 'selected' : 'deselected'));
                        var a = option[0].text;
                        if (a.length > 5) {
                            $("#form_class1").next().children("button").text(a.substr(0, 5) + "...");
                        }
                    }
                });
                $("#form_class1").append(" <option value='' style='background-color: #eeeeee'>请选择品牌</option>");
                var value = "value";
                var text = "text";
                for (var i = 0; i < json.length; i++) {
                    value = json[i].code;
                    text =
                        $("#form_class1").append("<option value='" + json[i].code + "' style='background-color: #eeeeee'>" + json[i].name + "</option>");
                }
                if ("${pageType}" == "edit") {
                    $("#form_class1").find("option[value='${style.class1}']").attr("selected", true);
                }
                $('#form_class1').multiselect('rebuild');
                $("div.btn-group,div.btn-group>button,div.btn-group>ul").addClass("col-sm-12");
                $("div.btn-group").css("padding", "0");
            }
        });


        $.ajax({
            url: basePath + "/sys/property/searchByType.do?type=C2",
            cache: false,
            async: true,
            type: "POST",
            success: function (data, textStatus) {
                var json = data;

                $("#form_class2").empty();
                $("#form_class2").multiselect({
                    inheritClass: true,
                    includeSelectAllOption: true,
                    selectAllNumber: true,
                    enableFiltering: true,
                    filterPlaceholder: "请选择年份",
                    maxHeight: "400",
                    onChange: function (option, checked) {//change事件改变
                        //console.log(option.length + ' options ' + (checked ? 'selected' : 'deselected'));
                        var a = option[0].text;
                        if (a.length > 5) {
                            $("#form_class2").next().children("button").text(a.substr(0, 5) + "...");
                        }
                    }
                });
                $("#form_class2").append(" <option value='' style='background-color: #eeeeee'>请选择年份</option>");
                for (var i = 0; i < json.length; i++) {
                    $("#form_class2").append("<option value='" + json[i].code + "' style='background-color: #eeeeee'>" + json[i].name + "</option>");
                }
                if ("${pageType}" == "edit") {
                    $("#form_class2").find("option[value='${style.class2}']").attr("selected", true);
                }
                $('#form_class2').multiselect('rebuild');
                $("div.btn-group,div.btn-group>button,div.btn-group>ul").addClass("col-sm-12");
                $("div.btn-group").css("padding", "0");
            }
        });
        $.ajax({
            url: basePath + "/sys/property/listMultiLevel.do?type=C3",
            cache: false,
            async: true,
            type: "POST",
            success: function (data, textStatus) {
                var json = data;
                $("#form_class3").empty();
                $("#form_class3").multiselect({
                    inheritClass: true,
                    // includeSelectAllOption: true,
                    // selectAllNumber: true,
                    enableFiltering: true,
                    // filterPlaceholder: "请选择大类",
                    maxHeight: "400",
                    onChange: function (option, checked) {//change事件改变
                        //console.log(option.length + ' options ' + (checked ? 'selected' : 'deselected'));
                        var a = option[0].text;
                        a = $.trim(a);
                        if (a.length > 5) {
                            $("#form_class3").next().children("button").text(a.substr(0, 5) + "...");
                        }
                        changPrice($("#form_class9").val(),option[0].value);

                    }
                });
                $("#form_class3").append(" <option value='' style='background-color: #eeeeee'>请选择大类</option>");
                for (var i = 0; i < json.length; i++) {
                    var subLevelHeader = "";
                    var depth = json[i].depth;
                    if(depth > 1){
                        while (depth > 1){
                            subLevelHeader += "&nbsp&nbsp&nbsp&nbsp";
                            depth = depth - 1;
                        }
                    }
                    $("#form_class3").append("<option value='" + json[i].code + "' style='background-color: #eeeeee'>" + subLevelHeader +json[i].name + "</option>");
                }
                if ("${pageType}" == "edit") {
                    $("#form_class3").find("option[value='${style.class3}']").attr("selected", true);
                }
                $('#form_class3').multiselect('rebuild');
                $("div.btn-group,div.btn-group>button,div.btn-group>ul").addClass("col-sm-12");
                $("div.btn-group").css("padding", "0");
            }
        });
        $.ajax({
            url: basePath + "/sys/property/searchByType.do?type=C4",
            cache: false,
            async: true,
            type: "POST",
            success: function (data, textStatus) {
                var json = data;

                $("#form_class4").empty();
                $("#form_class4").multiselect({
                    inheritClass: true,
                    includeSelectAllOption: true,
                    selectAllNumber: true,
                    enableFiltering: true,
                    filterPlaceholder: "请选择小类",
                    maxHeight: "400",
                    onChange: function (option, checked) {//change事件改变
                        //console.log(option.length + ' options ' + (checked ? 'selected' : 'deselected'));
                        var a = option[0].text;
                        if (a.length > 5) {
                            $("#form_class4").next().children("button").text(a.substr(0, 5) + "...");
                        }
                    }
                });
                $("#form_class4").append(" <option value='' style='background-color: #eeeeee'>请选择小类</option>");
                for (var i = 0; i < json.length; i++) {
                    $("#form_class4").append("<option value='" + json[i].code + "' style='background-color: #eeeeee'>" + json[i].name + "</option>");
                }
                if ("${pageType}" == "edit") {
                    $("#form_class4").find("option[value='${style.class4}']").attr("selected", true);
                }
                $('#form_class4').multiselect('rebuild');
                $("div.btn-group,div.btn-group>button,div.btn-group>ul").addClass("col-sm-12");
                $("div.btn-group").css("padding", "0");
            }
        });
        $.ajax({
            url: basePath + "/sys/property/searchByType.do?type=C5",
            cache: false,
            async: true,
            type: "POST",
            success: function (data, textStatus) {
                var json = data;
                $("#form_class5").empty();
                $("#form_class5").multiselect({
                    inheritClass: true,
                    includeSelectAllOption: true,
                    selectAllNumber: true,
                    enableFiltering: true,
                    filterPlaceholder: "请选择性别",
                    maxHeight: "400",
                    onChange: function (option, checked) {//change事件改变
                        //console.log(option.length + ' options ' + (checked ? 'selected' : 'deselected'));

                        var a = option[0].text;
                        if (a.length > 5) {
                            $("#form_class5").next().children("button").text(a.substr(0, 5) + "...");
                        }
                    }
                });
                /*  $("#form_class5").append(" <option value='' style='background-color: #eeeeee'>请选择性别</option>");*/
                for (var i = 0; i < json.length; i++) {
                    $("#form_class5").append("<option value='" + json[i].code + "' style='background-color: #eeeeee'>" + json[i].name + "</option>");
                }
                $("#form_class5").find("option[value='1']").attr("selected", true);
                if ("${pageType}" == "edit") {
                    $("#form_class5").find("option[value='${style.class5}']").attr("selected", true);
                }
                $('#form_class5').multiselect('rebuild');

                $("div.btn-group,div.btn-group>button,div.btn-group>ul").addClass("col-sm-12");
                $("div.btn-group").css("padding", "0");
            }
        });
        $.ajax({
            url: basePath + "/sys/property/searchByType.do?type=C6",
            cache: false,
            async: true,
            type: "POST",
            success: function (data, textStatus) {
                var json = data;
                $("#form_class6").empty();
                $("#form_class6").multiselect({
                    inheritClass: true,
                    includeSelectAllOption: true,
                    selectAllNumber: true,
                    enableFiltering: true,
                    filterPlaceholder: "请选择入库类型",
                    maxHeight: "400",
                    onChange: function (option, checked) {//change事件改变
                        //console.log(option.length + ' options ' + (checked ? 'selected' : 'deselected'));
                        var a = option[0].text;
                        if (a.length > 5) {
                            $("#form_class6").next().children("button").text(a.substr(0, 5) + "...");
                        }
                    }
                });
                for (var i = 0; i < json.length; i++) {
                    $("#form_class6").append("<option value='" + json[i].code + "' style='background-color: #eeeeee'>" + json[i].name + "</option>");
                }
                if ("${pageType}" == "edit") {
                    $("#form_class6").find("option[value='${style.class6}']").attr("selected", true);
                }
                $('#form_class6').multiselect('rebuild');
                $("div.btn-group,div.btn-group>button,div.btn-group>ul").addClass("col-sm-12");
                $("div.btn-group").css("padding", "0");
            }
        });

        $.ajax({
            url: basePath + "/sys/property/searchByType.do?type=C7",
            cache: false,
            async: true,
            type: "POST",
            success: function (data, textStatus) {
                var json = data;
                $("#form_class7").empty();
                $("#form_class7").multiselect({
                    inheritClass: true,
                    includeSelectAllOption: true,
                    selectAllNumber: true,
                    enableFiltering: true,
                    filterPlaceholder: "请选择执行标准",
                    maxHeight: "400",
                    onChange: function (option, checked) {//change事件改变
                        //console.log(option.length + ' options ' + (checked ? 'selected' : 'deselected'));
                        var a = option[0].text;
                        if (a.length > 5) {
                            $("#form_class7").next().children("button").text(a.substr(0, 5) + "...");
                        }
                    }
                });
                $("#form_class7").append(" <option value='' style='background-color: #eeeeee'>请选择执行标准 </option>");
                for (var i = 0; i < json.length; i++) {
                    $("#form_class7").append("<option value='" + json[i].code + "' style='background-color: #eeeeee'>" + json[i].name + "</option>");
                }
                if ("${pageType}" == "edit") {
                    $("#form_class7").find("option[value='${style.class6}']").attr("selected", true);
                }
                $('#form_class7').multiselect('rebuild');
                $("div.btn-group,div.btn-group>button,div.btn-group>ul").addClass("col-sm-12");
                $("div.btn-group").css("padding", "0");
            }
        });

        $.ajax({
            url: basePath + "/sys/property/searchByType.do?type=C8",
            cache: false,
            async: true,
            type: "POST",
            success: function (data, textStatus) {
                var json = data;
                $("#form_class8").empty();
                $("#form_class8").multiselect({
                    inheritClass: true,
                    includeSelectAllOption: true,
                    selectAllNumber: true,
                    enableFiltering: true,
                    filterPlaceholder: "请选择材质",
                    maxHeight: "400",
                    onChange: function (option, checked) {//change事件改变
                        //console.log(option.length + ' options ' + (checked ? 'selected' : 'deselected'));

                        var a = option[0].text;
                        if (a.length > 5) {
                            $("#form_class8").next().children("button").text(a.substr(0, 5) + "...");
                        }
                    }
                });
                $("#form_class8").append(" <option value='' style='background-color: #eeeeee'>请选择材质 </option>");
                for (var i = 0; i < json.length; i++) {
                    $("#form_class8").append("<option value='" + json[i].code + "' style='background-color: #eeeeee'>" + json[i].name + "</option>");
                }
                if ("${pageType}" == "edit") {
                    $("#form_class8").find("option[value='${style.class8}']").attr("selected", true);
                }
                $('#form_class8').multiselect('rebuild');
                $("div.btn-group,div.btn-group>button,div.btn-group>ul").addClass("col-sm-12");
                $("div.btn-group").css("padding", "0");
            }
        });

        $.ajax({
            url: basePath + "/sys/property/searchByType.do?type=C9",
            cache: false,
            async: true,
            type: "POST",
            success: function (data, textStatus) {
                var json = data;
                $("#form_class9").empty();
                $("#form_class9").multiselect({
                    inheritClass: true,
                    includeSelectAllOption: true,
                    selectAllNumber: true,
                    enableFiltering: true,
                    filterPlaceholder: "请选择系列",
                    maxHeight: "400",
                    onChange: function (option, checked) {//change事件改变
                        changPrice(option[0].value,$("#form_class3").val());
                    }
                });
                for (var i = 0; i < json.length; i++) {
                    if (json[i].code === "AS") {
                        $("#form_class9").append("<option value='" + json[i].code + "' style='background-color: #eeeeee' selected>" + json[i].name + "</option>");
                    } else {
                        $("#form_class9").append("<option value='" + json[i].code + "' style='background-color: #eeeeee'>" + json[i].name + "</option>");
                    }
                }
                if ("${pageType}" == "edit") {
                    $("#form_class9").find("option[value='${style.class9}']").attr("selected", true);
                }
                $('#form_class9').multiselect('rebuild');
                $("div.btn-group,div.btn-group>button,div.btn-group>ul").addClass("col-sm-12");
                $("div.btn-group").css("padding", "0");
            }
        });

        $.ajax({
            url: basePath + "/sys/property/searchByType.do?type=C10",
            cache: false,
            async: true,
            type: "POST",
            success: function (data, textStatus) {
                var json = data;
                $("#form_class10").empty();
                $("#form_class10").multiselect({
                    inheritClass: true,
                    includeSelectAllOption: true,
                    selectAllNumber: true,
                    enableFiltering: true,
                    filterPlaceholder: "请选择季节",
                    maxHeight: "400",
                    onChange: function (option, checked) {
                        var a = option[0].text;
                        if (a.length > 5) {
                            $("#form_class10").next().children("button").text(a.substr(0, 5) + "...");
                        }
                    }
                });
                $("#form_class10").append(" <option value='' style='background-color: #eeeeee'>请选择季节 </option>");
                for (var i = 0; i < json.length; i++) {
                    $("#form_class10").append("<option value='" + json[i].code + "' style='background-color: #eeeeee'>" + json[i].name + "</option>");
                }

                if ("${pageType}" == "edit") {
                    $("#form_class10").find("option[value='${style.class10}']").attr("selected", true);
                }
                $('#form_class10').multiselect('rebuild');
                $("div.btn-group,div.btn-group>button,div.btn-group>ul").addClass("col-sm-12");
                $("div.btn-group").css("padding", "0");
            }
        });
        $.ajax({
            url: basePath + "/prod/size/listSort.do?",
            cache: false,
            async: false,
            type: "POST",
            success: function (data, textStatus) {
                var json = data;
                for (var i = 0; i < json.length; i++) {
                    $("#form_sizeSortId").append("<option value='" + json[i].id + "'>" + "[" + json[i].id + "]" + json[i].sortName + "</option>");
                    $("#form_sizeSortId").trigger('chosen:updated');
                }
                if ("${pageType}" == "edit") {
                    $("#form_sizeSortId").find("option[value='${style.sizeSortId}']").attr("selected", true);
                }else{
                  $("#form_sizeSortId").find("option[value='01']").attr("selected", true);
                }
            }
        });

    }

    function inputPriceKeydown() {
        var isSeries = $("#form_isSeries").val();
        $("#form_preCast").keydown(function (event) {
            if (event.keyCode == 13) {
                if (isSeries == "Y") {
                    changPrice($("#form_class9").val(),$("#form_class3").val());
                }

            }
        })
    }
    function inputPriceKeydowno() {
        $("#form_price").keydown(function (event) {
            var price = $("#form_price").val();
            if (event.keyCode == 13) {
                $("#form_puPrice").val(price);
                $("#form_wsPrice").val(price);
            }
        })
    }

    /*判断是否使用定价规则*/
    function priceIsUse() {
        var price = $("#form_price").val();
        $("#form_puPrice").val(price);
        $("#form_wsPrice").val(price);
        var isSeries = $("#form_isSeries").val();
        if (isSeries == "Y") {
            $("#form_price").attr("readonly",true);
            changPrice($("#form_class9").val(),$("#form_class3").val());
        } else {
            $("#form_price").attr("readonly",false);
            $("#form_puPrice").val(price);
            $("#form_wsPrice").val(price);
        }
    }

    /*name=系列的code*/
    function changPrice(name,class3) {
        var isSeries = $("#form_isSeries").val();
        if (isSeries == "Y") {
            var price;
            var purPrice;
            var wsPrice;
            var preCast = $("#form_preCast").val();
            $.ajax({
                url: basePath + "/sys/pricingRules/findPricingRules.do",
                cache: false,
                async: true,
                inheritClass: true,
                type: "POST",
                data: {
                    series: name,
                    class3:class3
                },
                success: function (msg) {
                    if (msg.success) {
                        var json = msg.result;
                        checkNum = msg.rule1;
                        price = Math.floor(preCast * (json.rule1) /10) * 10 +9;
                        /*规则1 表示吊牌价与采购价之间关系*/
                        purPrice = Math.round(price * (json.rule3) * 10) / 10.0;
                        /*规则3 代理商价与吊牌价之间关系*/
                        wsPrice = Math.round(price * (json.rule2) * 10) / 10.0;
                        /*规则2 门店价与吊牌价直接关系*/
                        $("#form_price").val(price);
                        $("#form_puPrice").val(purPrice);
                        $("#form_wsPrice").val(wsPrice);
                    } else {
                        bootbox.alert(msg.msg);
                    }
                }
            });
        }
    }

    function setUrl() {
        //更改Grid的url
        var url = "";
        if ("${pageType}" == "edit") {
            url = basePath + "/prod/product/page.do?filter_EQS_styleId=${styleId}";
        }
        $("#CSGrid").jqGrid("setGridParam", {
            datatype: 'json',
            url: url,
            page: 1
        });
        $("#CSGrid").trigger("reloadGrid");
    }

    function toIndex() {
        location.href = basePath + "/prod/style/index.do"
    }

    function addColorAndSize() {
        $("#modal-colorAndSize").modal("show");
    }


    function saveStyleAndProduct(str) {
        if($('#form_isSeries').is(':hidden')){
            $('#form_isSeries').val("N");
            var price = $("#form_price").val();
            $("#form_puPrice").val(price);
            $("#form_wsPrice").val(price);
            $("#form_preCast").val(price);
        }else {
            $("#form_bargainPrice").val(0);
        }
        var bargainPrice = $("#form_bargainPrice").val();
        if (bargainPrice==""){
            return false;
        }
        var isSeries = $("#form_isSeries").val();
        var re = $("#tags_3").val();
        $("#form_remark").val(re);
        if ((re.indexOf(",") >= 0)||(re.indexOf("，") >= 0)){
            bootbox.alert("成分中不允许含有回车及逗号字符");
        }else {
            bootbox.setDefaults("locale","zh_CN");
            if (isSeries == "N") {
                $('#editStyleForm').data('bootstrapValidator').validate();
                if (!$('#editStyleForm').data('bootstrapValidator').isValid()) {
                    return;
                }
                if (editDtailRowId != null) {
                    saveItem(editDtailRowId)
                }
                $("#form_sizeSortId").removeAttr("disabled");
                var dtlArray = [];
                var isRemark=true;
                $.each($("#CSGrid").getDataIDs(), function (dtlndex, dtlValue) {
                    var dtlRow = $("#CSGrid").getRowData(dtlValue);
                    if (re!=dtlRow.remark){
                        isRemark=false;
                        return false;
                    }
                });
                if (!isRemark){
                    bootbox.confirm("是否需要覆盖色码列表成分", function(result) {
                        if (result){
                            $.each($("#CSGrid").getDataIDs(), function (index, value) {
                                $('#CSGrid').setCell(value, "remark", re);
                            });
                            cs.showProgressBar();
                            $.each($("#CSGrid").getDataIDs(), function (dtlndex, dtlValue) {
                                var dtlRow = $("#CSGrid").getRowData(dtlValue);
                                dtlArray.push(dtlRow);
                            });
                            $.ajax({
                                dataType: "json",
                                url: basePath + "/prod/style/saveStyleAndProduct.do",
                                data: {
                                    styleStr: JSON.stringify(array2obj($("#editStyleForm").serializeArray())),
                                    productStr: JSON.stringify(dtlArray),
                                    userId: userId,
                                    pageType: str
                                },
                                type: "POST",
                                success: function (msg) {
                                    cs.closeProgressBar();
                                    if (msg.success) {
                                        $.gritter.add({
                                            text: msg.msg,
                                            class_name: 'gritter-success  gritter-light'
                                        });
                                        toIndex();
                                    } else {
                                        bootbox.alert(msg.msg);
                                    }
                                }
                            });
                        }else {
                            cs.showProgressBar();
                            $.each($("#CSGrid").getDataIDs(), function (dtlndex, dtlValue) {
                                var dtlRow = $("#CSGrid").getRowData(dtlValue);
                                dtlArray.push(dtlRow);
                            });
                            $.ajax({
                                dataType: "json",
                                url: basePath + "/prod/style/saveStyleAndProduct.do",
                                data: {
                                    styleStr: JSON.stringify(array2obj($("#editStyleForm").serializeArray())),
                                    productStr: JSON.stringify(dtlArray),
                                    userId: userId,
                                    pageType: str
                                },
                                type: "POST",
                                success: function (msg) {
                                    cs.closeProgressBar();
                                    if (msg.success) {
                                        $.gritter.add({
                                            text: msg.msg,
                                            class_name: 'gritter-success  gritter-light'
                                        });
                                        toIndex();
                                    } else {
                                        bootbox.alert(msg.msg);
                                    }
                                }
                            });
                        }
                    });
                }else {
                    cs.showProgressBar();
                    $.each($("#CSGrid").getDataIDs(), function (dtlndex, dtlValue) {
                        var dtlRow = $("#CSGrid").getRowData(dtlValue);
                        dtlArray.push(dtlRow);
                    });
                    $.ajax({
                        dataType: "json",
                        url: basePath + "/prod/style/saveStyleAndProduct.do",
                        data: {
                            styleStr: JSON.stringify(array2obj($("#editStyleForm").serializeArray())),
                            productStr: JSON.stringify(dtlArray),
                            userId: userId,
                            pageType: str
                        },
                        type: "POST",
                        success: function (msg) {
                            cs.closeProgressBar();
                            if (msg.success) {
                                $.gritter.add({
                                    text: msg.msg,
                                    class_name: 'gritter-success  gritter-light'
                                });
                                toIndex();
                            } else {
                                bootbox.alert(msg.msg);
                            }
                        }
                    });
                }
            } else {
                if (Math.round($("#form_preCast").val()) * checkNum > Math.round($("#form_price").val())) {
                    $.gritter.add({
                        text: "采购价和吊牌价不符合定价规则，请核对对应价格",
                        class_name: 'gritter-success  gritter-light'
                    });
                } else {
                    $('#editStyleForm').data('bootstrapValidator').validate();
                    if (!$('#editStyleForm').data('bootstrapValidator').isValid()) {
                        return;
                    }
                    if (editDtailRowId != null) {
                        saveItem(editDtailRowId)
                    }
                    $("#form_sizeSortId").removeAttr("disabled");
                    var isRemark=true;
                    $.each($("#CSGrid").getDataIDs(), function (dtlndex, dtlValue) {
                        var dtlRow = $("#CSGrid").getRowData(dtlValue);
                        if (re!=dtlRow.remark){
                            isRemark=false;
                            return false;
                        }
                    });
                    if (!isRemark){
                        bootbox.confirm("是否需要覆盖色码列表成分", function(result) {
                            if (result){
                                $.each($("#CSGrid").getDataIDs(), function (index, value) {
                                    $('#CSGrid').setCell(value, "remark", re);
                                });
                                cs.showProgressBar();
                                var dtlArray = [];
                                $.each($("#CSGrid").getDataIDs(), function (dtlndex, dtlValue) {
                                    var dtlRow = $("#CSGrid").getRowData(dtlValue);
                                    dtlArray.push(dtlRow);
                                });
                                $.ajax({
                                    dataType: "json",
                                    url: basePath + "/prod/style/saveStyleAndProduct.do",
                                    data: {
                                        styleStr: JSON.stringify(array2obj($("#editStyleForm").serializeArray())),
                                        productStr: JSON.stringify(dtlArray),
                                        userId: userId,
                                        pageType: str
                                    },
                                    type: "POST",
                                    success: function (msg) {
                                        cs.closeProgressBar();
                                        if (msg.success) {
                                            $.gritter.add({
                                                text: msg.msg,
                                                class_name: 'gritter-success  gritter-light'
                                            });
                                            toIndex();
                                        } else {
                                            bootbox.alert(msg.msg);
                                        }
                                    }
                                });
                            }else {
                                cs.showProgressBar();
                                var dtlArray = [];
                                $.each($("#CSGrid").getDataIDs(), function (dtlndex, dtlValue) {
                                    var dtlRow = $("#CSGrid").getRowData(dtlValue);
                                    dtlArray.push(dtlRow);
                                });
                                $.ajax({
                                    dataType: "json",
                                    url: basePath + "/prod/style/saveStyleAndProduct.do",
                                    data: {
                                        styleStr: JSON.stringify(array2obj($("#editStyleForm").serializeArray())),
                                        productStr: JSON.stringify(dtlArray),
                                        userId: userId,
                                        pageType: str
                                    },
                                    type: "POST",
                                    success: function (msg) {
                                        cs.closeProgressBar();
                                        if (msg.success) {
                                            $.gritter.add({
                                                text: msg.msg,
                                                class_name: 'gritter-success  gritter-light'
                                            });
                                            toIndex();
                                        } else {
                                            bootbox.alert(msg.msg);
                                        }
                                    }
                                });
                            }
                        });
                    }else {
                        cs.showProgressBar();
                        var dtlArray = [];
                        $.each($("#CSGrid").getDataIDs(), function (dtlndex, dtlValue) {
                            var dtlRow = $("#CSGrid").getRowData(dtlValue);
                            dtlArray.push(dtlRow);
                        });
                        $.ajax({
                            dataType: "json",
                            url: basePath + "/prod/style/saveStyleAndProduct.do",
                            data: {
                                styleStr: JSON.stringify(array2obj($("#editStyleForm").serializeArray())),
                                productStr: JSON.stringify(dtlArray),
                                userId: userId,
                                pageType: str
                            },
                            type: "POST",
                            success: function (msg) {
                                cs.closeProgressBar();
                                if (msg.success) {
                                    $.gritter.add({
                                        text: msg.msg,
                                        class_name: 'gritter-success  gritter-light'
                                    });
                                    toIndex();
                                } else {
                                    bootbox.alert(msg.msg);
                                }
                            }
                        });
                    }
                }
            }
        }
    }


    var editDtailRowId;

    function iniGrid() {
        $("#CSGrid").jqGrid({
            height: "300px",
            datatype: 'local',
            mtype: 'POST',
            colModel: [
                {
                    name: "", label: "操作", width: 80, align: "center",
                    formatter: function (cellValue, options, rowObject) {
                        var html;
                        if (pageType == "add") {
                            return "<a style='margin-left: 20px' a  href='javascript:void(0);' onclick=deleteColorSize('" + options.rowId + "')><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";
                        } else {
                            if (rowObject.isUse == "Y") {
                                html = "<a style='margin-left: 0px' href='#' onclick=changePS('" + rowObject.code + "','N')><i class='ace-icon fa fa-check' title='启用'></i></a>";
                            } else {
                                html = "<a style='margin-left: 0px' href='#' onclick=changePS('" + rowObject.code + "','Y')><i class='ace-icon fa fa-lock' title='废除'></i></a>";
                            }
                            return html;
                        }
                    }
                },
                {name: 'colorId', label: '颜色'},
                {name: 'sizeId', label: '尺寸'},
                {
                    name: 'barcode', label: '条码',
                    editrules: {
                        number: true
                    }
                },
                {name:'remark',label:'成分'},
                {name: 'push', label: '推送'},
                {name: 'code', hidden: true}

            ],
            viewrecords: true,
            cellEdit:true,
            autoWidth: true,
            rownumbers: true,
            altRows: true,
            rowNum: 20,
            rowList: [20, 50, 100],
            pager: "#CSPage",
            multiselect: false,
            shrinkToFit: true,
            autoScroll: false,
            sortname: 'colorId',
            sortorder: "asc",
            /*onSelectRow: function (rowid, status) {
                if (editDtailRowId != null) {
                    saveItem(editDtailRowId);
                }
                editDtailRowId = rowid;
                var rowData = $("#CSGrid").jqGrid('getRowData',rowid);


            }*/
            onCellSelect : function(rowid, index, contents, event){
                if (index==5){
                    changeRemark(rowid,contents);
                }
            }
        });
        $("#CSGrid").setGridWidth($("#parentWidth").width());
    }

    function changeRemark(rowid,contents) {
        $("#changeRemark_form").html("");
        $("#changeRemark_dialog").modal('show');
        var remarkList = contents.split(" ");
        var remark = [];
        var number = [];
        $.each(remarkList, function (dtlndex, dtlValue) {
            remark.push(GetChinese(dtlValue));
            number.push(GetNumber(dtlValue));
        });
        $("#changeRemark_buttonGroup").html("" +
            "<button  type='button' id = 'changeRemark_save'  class='btn btn-primary' onclick=RemarkSave('" + remark + "','" + rowid + "')>确认</button>"
        );
        $.each(remark,function (Rindex,Rvalue) {
            $("#changeRemark_form").append("<div class='form-group'>" +
                "<label class='col-sm-2 control-label no-padding-right'>"+Rvalue+"</label> " +
                "<div class='col-xs-9 col-sm-5'> " +
                "<input class='form-control' id='remark"+Rindex+"' type='number' value='"+number[Rindex]+"'/> </div>" +
                "<label class='col-sm-1 control-label no-padding-right'>%</label>" +
                " </div>");
        });
    }
    function RemarkSave(remark,rowid) {
        var newRemark = remark.split(",");
        var strRemark ="";
        $.each(newRemark,function (Rindex,Rvalue) {
            if((Rindex+1)==newRemark.length){
                strRemark +=Rvalue + $("#remark"+Rindex).val()+"%";
            }else {
                strRemark +=Rvalue + $("#remark"+Rindex).val()+"% ";
            }
        });
        $.ajax({
            url: basePath + "/prod/product/remarkSave.do?",
            data:{
                id:rowid,
                remark:strRemark
            },
            cache: false,
            async: false,
            type: 'POST',
            success: function (data) {
                if (data.success) {
                    $("#changeRemark_dialog").modal('hide');
                    $("#CSGrid").setCell(rowid, 'remark', strRemark);
                    $.gritter.add({
                        text: data.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                }else {
                    $.gritter.add({
                        text: data.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                }
            }
        });
    }


    function saveItem(rowId) {
        editDtailRowId = null;
        $('#CSGrid').saveRow(rowId);
    }

    function inimultiSize() {
        var sizeSortIdValue = $("#form_sizeSortId").val();
        $.ajax({
            //url: basePath + "/prod/size/searchSizeMap.do?filter_EQS_sortId=${style.sizeSortId}",
            url: basePath + "/prod/size/searchSizeMap.do?filter_EQS_sortId="+sizeSortIdValue,
            cache: false,
            async: true,
            type: 'POST',
            success: function (data, textStatus) {
                $("#form_sizeId").empty();
                var index = 1;
                for (var key in data) {
                    if (sizeSortIdValue == (key.slice(1, 3))) {
                        $("#form_sizeId").append("<optgroup label='" + key + "' class= 'group" + index + "'>");
                        $.each(data[key], function (index, value) {
                            $("#form_sizeId").append("<option value='" + value.sizeId + "'>" + value.sizeName + "</option>");
                        });
                        index++;
                        $("#form_sizeId").append("</optgroup>");
                    }
                }
                $('#form_sizeId').multiselect({
                    maxHeight: "400"
                });
                $('#form_sizeId').multiselect('rebuild');
                $("div.btn-group,div.btn-group>button,div.btn-group>ul").addClass("col-sm-12");
                $("div.btn-group").css("padding", "0");
            }
        });
    }

    function initColor() {

        $.ajax({
            url: basePath + "/prod/color/list.do?",
            cache: false,
            async: true,
            type: "POST",
            success: function (data, textStatus) {
                var json = data;
                $("#form_colorId_select").empty();
                $("#form_colorId_select").multiselect({
                    inheritClass: true,
                    includeSelectAllOption: true,
                    selectAllNumber: true,
                    enableFiltering: true,
                    filterPlaceholder: "请输入颜色",
                    maxHeight: "400"
                });
                for (var i = 0; i < json.length; i++) {
                    var backColor = "#ffffff";
                    if (json[i].hex != undefined) {
                        backColor = json[i].hex;
                    }
                    $("#form_colorId_select").append("<option value='" + json[i].colorId + "' style='background-color: " + backColor + "'>" + json[i].colorName + "</option>");
                }
                $('#form_colorId_select').multiselect('rebuild');
                $("div.btn-group,div.btn-group>button,div.btn-group>ul").addClass("col-sm-12");
                $("div.btn-group").css("padding", "0");
            }
        });
    }

    function initeditStyleFormValid() {
        $('#editStyleForm').bootstrapValidator({
            message: '输入值无效',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            submitHandler: function (validator, form, submitButton) {
                $.post(form.attr('action'), form.serialize(), function (result) {
                    if (result.success == true || result.success == 'true') {

                    } else {


                        // Enable the submit buttons
                        $('#editStyleForm').bootstrapValidator('disableSubmitButtons', false);
                    }
                }, 'json');
            },
            fields: {
                styleId: {
                    validators: {
                        notEmpty: {//非空验证：提示消息
                            message: '款号不能为空'
                        },

                        regexp: {
                            regexp: /^[a-zA-Z0-9_-]+$/,
                            message: '款号由数字字母下划线组成'
                        }
                    }
                },
                styleName: {
                    validators: {
                        notEmpty: {
                            message: '款名不能为空'
                        },
                        regexp: {
                            regexp: /^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$/,
                            message: '款名由数字字母下划线汉字组成'
                        }
                    }
                },
                price: {
                    validators: {
                        notEmpty: {message: '价格不能为空'},
                        numeric: {message: '价格只能输入数字'}
                    }
                },
                styleCycle: {
                    validators: {
                        regexp: {
                            regexp: /^[0-9]\d*$/,
                            message: '退货周期应为正整数'
                        }
                    }
                },
                bargainPrice:{
                    validators: {
                        notEmpty: {message: '特价不能为空'},
                        numeric: {message: '特价只能输入数字'}
                    }
                }
            }
        });
    }

    function addStyleProperty(rowId) {
        pagetype = "add";
        $("#editFormdetailed").resetForm();
        $("#edit-dialog-detailed").modal('show');
        $("#form_code").removeAttr("readOnly");
        $("#form_ids").val(rowId);
        $("#form_types").val(rowId);
    }

    function saveproperty() {

        $("#editFormdetailed").data('bootstrapValidator').validate();
        if (!$("#editFormdetailed").data('bootstrapValidator').isValid()) {
            return;
        }
        if( $("#form_ids").val()=="C11"){
            var name =GetNumber($("#form_name").val()) ;
            if (name!=""){
                bootbox.alert("成分名称中不能有数字");
                return;
            }
        }
        var progressDialog = bootbox.dialog({
            message: '<p><i class="fa fa-spin fa-spinner"></i>数据上传中...</p>'
        });
        $.post(basePath + '/sys/property/saveproperty.do',
            $("#editFormdetailed").serialize(),
            function (result) {
                progressDialog.modal('hide');
                if (result.success == true || result.success == 'true') {
                    $.gritter.add({
                        text: result.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                    $("#edit-dialog-detailed").modal('hide');
                    //window.location.reload();
                    var num = result.result.type;
                    var formClassX = "form_class" + "" + num.substr(1, 2);
                    $("#" + formClassX).append("<option value='" + result.result.code + "' selected style='background-color: #eeeeee'>" + result.result.name + "</option>");
                    $("#" + formClassX).multiselect('rebuild');
                } else {
                    $.gritter.add({
                        text: result.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                }
            }, 'json');

    }

    function changePS(code, status) {
        $.ajax({
            url: basePath + '/prod/style/changePS.do',
            datatype: 'json',
            data: {
                code: code,
                status: status
            },
            success: function (result) {
                if (result.success) {
                    $.gritter.add({
                        text: result.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                    $("#CSGrid").trigger('reloadGrid')
                } else {
                    $.gritter.add({
                        text: result.msg,
                        class_name: 'gritter-fail gritter-light'
                    });
                }
            }

        });
    }

    function deleteColorSize(rowId) {
        var row = $("#CSGrid").jqGrid("getRowData", rowId);
        cs.showProgressBar("删除中");
        $.post(basePath + "/prod/product/delete.do?code=" + row.code,
            function (result) {
                cs.closeProgressBar();
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                if (result.success) {
                    $("#CSGrid").delRowData(rowId);
                }
            }
        );
    }
    function loadingButton() {
        $.each(fieldList,function (index,value) {
            if(fieldList[index].isShow===0){
                if( $("#"+fieldList[index].privilegeId).length>0){
                    $("#"+fieldList[index].privilegeId).show();
                }
            }else {
                if( $("#"+fieldList[index].privilegeId).length>0){
                    $("#"+fieldList[index].privilegeId).hide();
                }
            }
        });
        if($('#form_isSeries').is(':hidden')){
            $('#form_isSeries').val("N");
            $("#form_price").removeAttr("readonly");
            var price = $("form_price").val();
            $("#form_puPrice").val(price);
            $("#form_wsPrice").val(price);
            if ('${pageType}' == 'edit') {
                $("#form_styleId").attr("readonly", true);
                if ('${roleId}' == '0') {
                    $("#form-group-preCast").show();
                    $("#form_price").removeAttr("readonly");
                } else {
                    $("#form-group-preCast").hide();
                    $("#form_price").attr("readonly", true);
                }
                $("#form_isSeries").val("${style.isSeries}");
            } else {
                /*  $("#focusColor").removeAttr("onclick");*/
                if ('${roleId}' == '0') {
                    $("#form-group-preCast").show();
                } else {
                    $("#form-group-preCast").hide();
                }
                //$("#form_price").removeAttr("readonly");
                $("#edit_isNotDeton").click();
                //$("#form_bargainPrice").val(0.0);
            }
        }else {
            if ('${pageType}' == 'edit') {
                $("#form_styleId").attr("readonly", true);
                if ('${roleId}' == '0') {
                    $("#form-group-preCast").show();
                } else {
                    $("#form-group-preCast").hide();
                }
                $("#form_isSeries").val("${style.isSeries}");
            } else {
                if ('${roleId}' == '0') {
                    $("#form-group-preCast").show();
                } else {
                    $("#form-group-preCast").hide();
                }
                $("#edit_isNotDeton").click();
                $("#form_bargainPrice").val(0.0);
            }
        }

    }
</script>
</body>
</html>