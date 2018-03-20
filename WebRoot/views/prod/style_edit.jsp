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
    </script>
    <style>
        a#focusColor:hover{
            background-color: #bbbbbb;
        }
        input[type=number] {
            -moz-appearance:textfield;
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
                                        <br />
                                        <form class="form-horizontal" role="form" id="editStyleForm" onkeydown="if(event.keyCode==13)return false;">


                                            <div class="form-group">
                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_styleId"><span class="text-danger">* </span>款号</label>

                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <input class="form-control" id="form_styleId" name="styleId"
                                                           type="text" placeholder="" value="${style.styleId}" onkeyup="this.value=this.value"/>
                                                </div>

                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_styleName"><span class="text-danger">* </span>款名</label>

                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <input class="form-control" id="form_styleName" name="styleName"
                                                           type="text" placeholder="" value="${style.styleName}"/>
                                                </div>

                                            </div>
                                            <div class="form-group" id="form-group-preCast" >
                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_preCast"><span class="text-danger">* </span>采购价</label>

                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <div class ="input-group">
                                                        <span class="input-group-addon"><i class="fa fa-jpy"></i></span>
                                                        <input class="form-control" id="form_preCast" name="preCast"
                                                                required="required" type="number" placeholder="" value="${style.preCast}" step="0.01"/>
                                                    </div>
                                                </div>
                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_puPrice"><span class="text-danger">* </span>代理商价</label>

                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <div class ="input-group">
                                                        <span class="input-group-addon"><i class="fa fa-jpy"></i></span>
                                                        <input class="form-control" id="form_puPrice" name="puPrice" readonly
                                                               required="required" type="number" placeholder="" value="${style.puPrice}" step="0.01"/>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_wsPrice"><span class="text-danger">* </span>门店价</label>

                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <div class ="input-group">
                                                        <span class="input-group-addon"><i class="fa fa-jpy"></i></span>
                                                        <input class="form-control" id="form_wsPrice" name="wsPrice" readonly
                                                               required="required" type="number" placeholder="" value="${style.wsPrice}" step="0.01"/>
                                                    </div>
                                                </div>
                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_price"><span class="text-danger">* </span>吊牌价</label>

                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <div class ="input-group">
                                                        <span class="input-group-addon"><i class="fa fa-jpy"></i></span>
                                                        <input class="form-control" id="form_price" name="price"
                                                               required="required" type="number" placeholder="" value="${style.price}" step="0.01"/>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="col-sm-2 control-label no-padding-right"
                                                       for="form_remark">成分</label>

                                                <div class="col-xs-9 col-sm-9">
                                            <textarea maxlength="400" class="form-control" id="form_remark"
                                                      name="remark">${style.remark}</textarea>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_brandCode">品牌</label>
                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <div class ="input-group col-xs-2">

                                                       <select class="chosen-select form-control" id="form_brandCode" name="class1">
                                                           <option value='' style='background-color: #eeeeee'>请选择品牌</option>")
                                                        </select>
                                                          <%--  <select id="form_brandCode" class="selectpicker" data-live-search="true">
                                                            </select>--%>
                                                            <span class="input-group-addon" title="添加${classTypes[0].value}">
                                                            <a  href='#'  class="white" onclick="addbrand('${classTypes[0].id}')">
                                                            <i class="fa fa-plus red"></i>
                                                            </a>
                                                        </span>
                                                    </div>
                                                </div>

                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_class2">${classTypes[1].value}</label>


                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <div class ="input-group col-xs-2">

                                                        <select class="chosen-select form-control" id="form_class2" name="class2">
                                                            <option value="" style="background-color: #eeeeee">请选择${classTypes[1].value}</option>
                                                        </select>
                                                        <span class="input-group-addon" title="添加${classTypes[1].value}">
                                                            <a  href='#'  class="white" onclick="addbrand('${classTypes[1].id}')">
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
                                                    <div class ="input-group ">
                                                        <%--<select class="chosen-select form-control" id="form_class3" name="class3">
                                                            <option value="" style="background-color: #eeeeee">--请选择${classTypes[2].value}--</option>
                                                        </select>--%>
                                                        <select class="chosen-select form-control" id="form_class3"  name="class3">
                                                            <option value="" style="background-color: #eeeeee">请选择${classTypes[2].value}</option>
                                                        </select>
                                                        <span class="input-group-addon" title="添加${classTypes[2].value}">
                                                            <a  href='#'  class="white" onclick="addbrand('${classTypes[2].id}')">
                                                            <i class="fa fa-plus red"></i>
                                                            </a>
                                                        </span>
                                                    </div>
                                                </div>
                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_class4">${classTypes[3].value}</label>
                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <div class ="input-group ">
                                                        <%--<select class="chosen-select form-control" id="form_class4" name="class4">
                                                            <option value="" style="background-color: #eeeeee">--请选择${classTypes[3].value}--</option>
                                                        </select>--%>
                                                        <select class="chosen-select form-control" id="form_class4"  name="class4">
                                                            <option value="" style="background-color: #eeeeee">请选择${classTypes[3].value}</option>
                                                        </select>
                                                        <span class="input-group-addon" title="添加${classTypes[3].value}">
                                                            <a  href='#'  class="white" onclick="addbrand('${classTypes[3].id}')">
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
                                                    <div class ="input-group ">
                                                        <%--<select class="chosen-select form-control" id="form_class5" name="class5">
                                                            <option value="" style="background-color: #eeeeee">--请选择无${classTypes[4].value}--</option>
                                                        </select>--%>
                                                        <select class="chosen-select form-control" id="form_class5"  name="class5">
                                                            <option value="" style="background-color: #eeeeee">请选择${classTypes[4].value}</option>
                                                        </select>
                                                        <span class="input-group-addon" title="添加${classTypes[4].value}">
                                                            <a  href='#'  class="white" onclick="addbrand('${classTypes[4].id}')">
                                                            <i class="fa fa-plus red"></i>
                                                            </a>
                                                        </span>
                                                    </div>
                                                </div>
                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_class6">${classTypes[5].value}</label>
                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <div class ="input-group ">
                                                       <%-- <select class="chosen-select form-control" id="form_class6" name="class6">
                                                        </select>--%>
                                                        <select class="chosen-select form-control" id="form_class6" name="class6">
                                                        </select>
                                                        <span class="input-group-addon" title="添加${classTypes[5].value}">
                                                            <a  href='#'  class="white" onclick="addbrand('${classTypes[5].id}')">
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
                                                    <div class ="input-group ">
                                                        <%--<select class="chosen-select form-control" id="form_class8" name="class8">
                                                            <option value="" style="background-color: #eeeeee">--请选择无${classTypes[7].value}--</option>
                                                        </select>--%>
                                                        <select class="chosen-select form-control" id="form_class8" name="class8">
                                                            <option value="" style="background-color: #eeeeee">请选择无${classTypes[7].value}</option>
                                                        </select>
                                                        <span class="input-group-addon" title="添加${classTypes[7].value}">
                                                            <a  href='#'  class="white" onclick="addbrand('${classTypes[7].id}')">
                                                            <i class="fa fa-plus red"></i>
                                                            </a>
                                                        </span>
                                                    </div>
                                                </div>
                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_class6">${classTypes[9].value}</label>
                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <div class ="input-group ">
                                                        <%--<select class="chosen-select form-control" id="form_class10" name="class10">
                                                            <option value="" style="background-color: #eeeeee">--请选择${classTypes[9].value}--</option>
                                                        </select>--%>
                                                        <select class="chosen-select form-control" id="form_class10" name="class10">
                                                            <option value="" style="background-color: #eeeeee">请选择${classTypes[9].value}</option>
                                                        </select>
                                                        <span class="input-group-addon" title="添加${classTypes[9].value}">
                                                            <a  href='#'  class="white" onclick="addbrand('${classTypes[9].id}')">
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
                                                    <div class ="input-group ">
                                                        <%--<select class="chosen-select form-control" id="form_class7" name="class7">
                                                            <option value="" style="background-color: #eeeeee">--请选择${classTypes[6].value}--</option>
                                                        </select>--%>
                                                        <select class="chosen-select form-control" id="form_class7">
                                                            <option value="" style="background-color: #eeeeee">请选择${classTypes[6].value}</option>
                                                        </select>
                                                        <span class="input-group-addon" title="添加${classTypes[6].value}">
                                                            <a  href='#'  class="white" onclick="addbrand('${classTypes[6].id}')">
                                                            <i class="fa fa-plus red"></i>
                                                            </a>
                                                        </span>
                                                    </div>
                                                </div>

                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_class6">${classTypes[8].value}</label>
                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <div class ="input-group ">
                                                        <%--<select class="chosen-select form-control" id="form_class10" name="class10">
                                                            <option value="" style="background-color: #eeeeee">--请选择${classTypes[9].value}--</option>
                                                        </select>--%>
                                                        <select class="chosen-select form-control" id="form_class9" name="class9">
                                                        </select>
                                                        <span class="input-group-addon" title="添加${classTypes[8].value}">
                                                            <a  href='#'  class="white" onclick="addbrand('${classTypes[8].id}')">
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
                                                        <select class="chosen-select form-control" id="form_sizeSortId" name="sizeSortId">
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

                                                <label class="col-xs-4 col-sm-4 col-md-2 col-lg-2 text-right control-label"
                                                       for="form_ispush">是否推送</label>
                                                <div class="col-xs-8 col-sm-8 col-md-3 col-lg-3">
                                                    <select class="form-control" id="form_ispush" name="ispush" value="${style.ispush}"/>
                                                        <option value="Y" style="background-color: #eeeeee">是</option>
                                                        <option value="N" style="background-color: #eeeeee">否</option>
                                                    </select>
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
                                                   <a  id="focusColor" href="javascript:void(0)" class="white" onclick="addColorAndSize()">
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
    <jsp:include page="../layout/footer.jsp"></jsp:include>
    <!--/.fluid-container#main-container-->
</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<link rel="stylesheet" href="<%=basePath%>/Olive/assets/css/bootstrap-colorpicker.min.css" />
<script type="text/javascript" src="<%=basePath%>/Olive/assets/js/bootstrap-colorpicker.min.js"></script>

<jsp:include page="style_colorAndSize_dialog.jsp"></jsp:include>
<jsp:include page="color_edit.jsp"></jsp:include>
<jsp:include page="size_edit.jsp"></jsp:include>
<jsp:include page="../sys/property_edit_ Detailed.jsp"></jsp:include>
<script src="<%=basePath%>/Olive/assets/js/bootstrap-multiselect.js"></script>
<script type="text/javascript">
    var checkNum = 4.5;
    $(function () {
//        initLoadStyle();
        iniGrid();
        inputPriceKeydown();

        if ('${pageType}' == 'edit') {
            $("#form_styleId").attr("readonly", true);
            if('${roleId}' == '0'){
                $("#form-group-preCast").show();
                $("#form_price").removeAttr("readonly");
            }else{
                $("#form-group-preCast").hide();
                $("#form_price").attr("readonly", true);
            }

        } else {
          /*  $("#focusColor").removeAttr("onclick");*/
            if('${roleId}' == '0'){
                $("#form-group-preCast").show();
            }else{
                $("#form-group-preCast").hide();
            }
            $("#edit_isNotDeton").click();
        }
        initeditStyleFormValid();

        initSelect();
        setTimeout(function(){
            setUrl();
        },500);

        $("#form_sizeId").multiselect({
            inheritClass: true,
            includeSelectAllOption: true,
            selectAllNumber: true,
            enableClickableOptGroups: true,
            enableCollapsibleOptGroups: true,
            enableFiltering: true,
            filterPlaceholder:"请输入尺寸",
            maxHeight: "400"
        });
        $("div.btn-group,div.btn-group>button,div.btn-group>ul").addClass("col-sm-12");
        $("div.btn-group").css("padding", "0");


    });
    //更改select背景色
    function changeBackColor() {
        var color = $("#form_colorId_select").find("option:selected").css("background-color");
        $("#form_colorId_select").css("background-color", color);
        console.log(color);
    }
    function initSelect(){
        $.ajax({
            url:basePath+"/sys/property/searchByType.do?type=C1",
            cache:false,
            async:false,
            type:"POST",
            success:function (data,textStatus) {
                var json=data;

                $("#form_brandCode").empty();
                $("#form_brandCode").multiselect({
                    inheritClass: true,
                    includeSelectAllOption: true,
                    selectAllNumber: true,
                    enableFiltering: true,
                    filterPlaceholder:"请选择品牌",
                    maxHeight: "400",
                    onChange: function (option, checked) {//change事件改变
                        //console.log(option.length + ' options ' + (checked ? 'selected' : 'deselected'));

                        var a=option[0].text;
                        if(a.length>5){
                            $("#form_class1").next().children("button").text(a.substr(0, 5)+"...");
                        }



                    }
                });
                $("#form_class1").append(" <option value='' style='background-color: #eeeeee'>请选择品牌</option>");
                for (var i = 0; i < json.length; i++) {
                    $("#form_class1").append("<option value='"+json[i].code+"' style='background-color: #eeeeee'>"+json[i].name+"</option>");
                }

                if("${pageType}"=="edit"){
                    $("#form_class1").find("option[value='${style.class1}']").attr("selected",true);
                }
                $('#form_class1').multiselect('rebuild');

            }



        });
        $.ajax({
            url:basePath+"/sys/property/searchByType.do?type=C2",
            cache:false,
            async:false,
            type:"POST",
            success:function (data,textStatus) {
                var json=data;

                $("#form_class2").empty();
                $("#form_class2").multiselect({
                    inheritClass: true,
                    includeSelectAllOption: true,
                    selectAllNumber: true,
                    enableFiltering: true,
                    filterPlaceholder:"请选择年份",
                    maxHeight: "400",
                    onChange: function (option, checked) {//change事件改变
                        //console.log(option.length + ' options ' + (checked ? 'selected' : 'deselected'));
                        
                        var a=option[0].text;
                        if(a.length>5){
                            $("#form_class2").next().children("button").text(a.substr(0, 5)+"...");
                        }



                    }
                });
                $("#form_class2").append(" <option value='' style='background-color: #eeeeee'>请选择年份</option>");
                for (var i = 0; i < json.length; i++) {
                    $("#form_class2").append("<option value='"+json[i].code+"' style='background-color: #eeeeee'>"+json[i].name+"</option>");
                }
                
                if("${pageType}"=="edit"){
                    $("#form_class2").find("option[value='${style.class2}']").attr("selected",true);
                }
                $('#form_class2').multiselect('rebuild');

            }



        });
        $.ajax({
            url:basePath+"/sys/property/searchByType.do?type=C3",
            cache:false,
            async:false,
            type:"POST",
            success:function (data,textStatus) {
                var json=data;

                $("#form_class3").empty();
                $("#form_class3").multiselect({
                    inheritClass: true,
                    includeSelectAllOption: true,
                    selectAllNumber: true,
                    enableFiltering: true,
                    filterPlaceholder:"请选择大类",
                    maxHeight: "400",
                    onChange: function (option, checked) {//change事件改变
                        //console.log(option.length + ' options ' + (checked ? 'selected' : 'deselected'));
                        
                        var a=option[0].text;
                        if(a.length>5){
                            $("#form_class3").next().children("button").text(a.substr(0, 5)+"...");
                        }



                    }
                });
                $("#form_class3").append(" <option value='' style='background-color: #eeeeee'>请选择大类</option>");
                for (var i = 0; i < json.length; i++) {
                    $("#form_class3").append("<option value='"+json[i].code+"' style='background-color: #eeeeee'>"+json[i].name+"</option>");
                }
                
                if("${pageType}"=="edit"){
                    $("#form_class3").find("option[value='${style.class3}']").attr("selected",true);
                }
                $('#form_class3').multiselect('rebuild');

            }
        });
        $.ajax({
            url:basePath+"/sys/property/searchByType.do?type=C4",
            cache:false,
            async:false,
            type:"POST",
            success:function (data,textStatus) {
                var json=data;

                $("#form_class4").empty();
                $("#form_class4").multiselect({
                    inheritClass: true,
                    includeSelectAllOption: true,
                    selectAllNumber: true,
                    enableFiltering: true,
                    filterPlaceholder:"请选择小类",
                    maxHeight: "400",
                    onChange: function (option, checked) {//change事件改变
                        //console.log(option.length + ' options ' + (checked ? 'selected' : 'deselected'));
                        
                        var a=option[0].text;
                        if(a.length>5){
                            $("#form_class4").next().children("button").text(a.substr(0, 5)+"...");
                        }



                    }
                });
                $("#form_class4").append(" <option value='' style='background-color: #eeeeee'>请选择小类</option>");
                for (var i = 0; i < json.length; i++) {
                    $("#form_class4").append("<option value='"+json[i].code+"' style='background-color: #eeeeee'>"+json[i].name+"</option>");
                }
                
                if("${pageType}"=="edit"){
                    $("#form_class4").find("option[value='${style.class4}']").attr("selected",true);
                }
                $('#form_class4').multiselect('rebuild');

            }
        });
        $.ajax({
            url:basePath+"/sys/property/searchByType.do?type=C5",
            cache:false,
            async:false,
            type:"POST",
            success:function (data,textStatus) {
                var json=data;

                $("#form_class5").empty();
                $("#form_class5").multiselect({
                    inheritClass: true,
                    includeSelectAllOption: true,
                    selectAllNumber: true,
                    enableFiltering: true,
                    filterPlaceholder:"请选择性别",
                    maxHeight: "400",
                    onChange: function (option, checked) {//change事件改变
                        //console.log(option.length + ' options ' + (checked ? 'selected' : 'deselected'));
                        
                        var a=option[0].text;
                        if(a.length>5){
                            $("#form_class5").next().children("button").text(a.substr(0, 5)+"...");
                        }



                    }
                });
              /*  $("#form_class5").append(" <option value='' style='background-color: #eeeeee'>请选择性别</option>");*/
                for (var i = 0; i < json.length; i++) {
                    $("#form_class5").append("<option value='"+json[i].code+"' style='background-color: #eeeeee'>"+json[i].name+"</option>");
                }
                
                if("${pageType}"=="edit"){
                    $("#form_class5").find("option[value='${style.class5}']").attr("selected",true);
                }
                $('#form_class5').multiselect('rebuild');

            }
        });
        $.ajax({
            url:basePath+"/sys/property/searchByType.do?type=C6",
            cache:false,
            async:false,
            type:"POST",
            success:function (data,textStatus) {
                var json=data;
              
                $("#form_class6").empty();
                $("#form_class6").multiselect({
                    inheritClass: true,
                    includeSelectAllOption: true,
                    selectAllNumber: true,
                    enableFiltering: true,
                    filterPlaceholder:"请选择入库类型",
                    maxHeight: "400",
                    onChange: function (option, checked) {//change事件改变
                        //console.log(option.length + ' options ' + (checked ? 'selected' : 'deselected'));
                        
                        var a=option[0].text;
                        if(a.length>5){
                            $("#form_class6").next().children("button").text(a.substr(0, 5)+"...");
                        }
                    }
                });
                for (var i = 0; i < json.length; i++) {
                    $("#form_class6").append("<option value='"+json[i].code+"' style='background-color: #eeeeee'>"+json[i].name+"</option>");
                }
                
                if("${pageType}"=="edit"){
                    $("#form_class6").find("option[value='${style.class6}']").attr("selected",true);
                }
                $('#form_class6').multiselect('rebuild');

            }
        });

        $.ajax({
            url:basePath+"/sys/property/searchByType.do?type=C7",
            cache:false,
            async:false,
            type:"POST",
            success:function (data,textStatus) {
                var json=data;

                $("#form_class7").empty();
                $("#form_class7").multiselect({
                    inheritClass: true,
                    includeSelectAllOption: true,
                    selectAllNumber: true,
                    enableFiltering: true,
                    filterPlaceholder:"请选择执行标准",
                    maxHeight: "400",
                    onChange: function (option, checked) {//change事件改变
                        //console.log(option.length + ' options ' + (checked ? 'selected' : 'deselected'));
                        
                        var a=option[0].text;
                        if(a.length>5){
                            $("#form_class7").next().children("button").text(a.substr(0, 5)+"...");
                        }
                    }
                });
                $("#form_class7").append(" <option value='' style='background-color: #eeeeee'>请选择执行标准 </option>");
                for (var i = 0; i < json.length; i++) {
                    $("#form_class7").append("<option value='"+json[i].code+"' style='background-color: #eeeeee'>"+json[i].name+"</option>");
                }
                
                if("${pageType}"=="edit"){
                    $("#form_class7").find("option[value='${style.class6}']").attr("selected",true);
                }
                $('#form_class7').multiselect('rebuild');

            }
        });

        $.ajax({
            url:basePath+"/sys/property/searchByType.do?type=C8",
            cache:false,
            async:false,
            type:"POST",
            success:function (data,textStatus) {
                var json=data;

                $("#form_class8").empty();
                $("#form_class8").multiselect({
                    inheritClass: true,
                    includeSelectAllOption: true,
                    selectAllNumber: true,
                    enableFiltering: true,
                    filterPlaceholder:"请选择材质",
                    maxHeight: "400",
                    onChange: function (option, checked) {//change事件改变
                        //console.log(option.length + ' options ' + (checked ? 'selected' : 'deselected'));
                        
                        var a=option[0].text;
                        if(a.length>5){
                            $("#form_class8").next().children("button").text(a.substr(0, 5)+"...");
                        }
                    }
                });
                $("#form_class8").append(" <option value='' style='background-color: #eeeeee'>请选择材质 </option>");
                for (var i = 0; i < json.length; i++) {
                    $("#form_class8").append("<option value='"+json[i].code+"' style='background-color: #eeeeee'>"+json[i].name+"</option>");
                }
                
                if("${pageType}"=="edit"){
                    $("#form_class8").find("option[value='${style.class8}']").attr("selected",true);
                }
                $('#form_class8').multiselect('rebuild');

            }
        });

        $.ajax({
            url:basePath+"/sys/property/searchByType.do?type=C9",
            cache:false,
            async:false,
            type:"POST",
            success:function (data,textStatus) {
                var json=data;

                $("#form_class9").empty();
                $("#form_class9").multiselect({
                    inheritClass: true,
                    includeSelectAllOption: true,
                    selectAllNumber: true,
                    enableFiltering: true,
                    filterPlaceholder:"请选择系列",
                    maxHeight: "400",
                    onChange: function (option, checked) {//change事件改变
                        changPrice(option[0].value);
                    }
                });
                for (var i = 0; i < json.length; i++) {
                    if(json[i].code === "AS"){
                        $("#form_class9").append("<option value='"+json[i].code+"' style='background-color: #eeeeee' selected>"+json[i].name+"</option>");
                        checkNum=4.5
                    }else{
                        $("#form_class9").append("<option value='"+json[i].code+"' style='background-color: #eeeeee'>"+json[i].name+"</option>");
                        checkNum=2.95
                    }
                }

                if("${pageType}"=="edit"){
                    $("#form_class9").find("option[value='${style.class9}']").attr("selected",true);
                }
                $('#form_class9').multiselect('rebuild');

            }
        });

        $.ajax({
            url:basePath+"/sys/property/searchByType.do?type=C10",
            cache:false,
            async:false,
            type:"POST",
            success:function (data,textStatus) {
                var json=data;

                $("#form_class10").empty();
                $("#form_class10").multiselect({
                    inheritClass: true,
                    includeSelectAllOption: true,
                    selectAllNumber: true,
                    enableFiltering: true,
                    filterPlaceholder:"请选择季节",
                    maxHeight: "400",
                    onChange: function (option, checked) {//change事件改变
                        //console.log(option.length + ' options ' + (checked ? 'selected' : 'deselected'));
                        
                        var a=option[0].text;
                        if(a.length>5){
                            $("#form_class10").next().children("button").text(a.substr(0, 5)+"...");
                        }
                    }
                });
                $("#form_class10").append(" <option value='' style='background-color: #eeeeee'>请选择季节 </option>");
                for (var i = 0; i < json.length; i++) {
                    $("#form_class10").append("<option value='"+json[i].code+"' style='background-color: #eeeeee'>"+json[i].name+"</option>");
                }
                
                if("${pageType}"=="edit"){
                    $("#form_class10").find("option[value='${style.class10}']").attr("selected",true);
                }
                $('#form_class10').multiselect('rebuild');

            }
        });
        initSizeSort();
        iniBrandCode();
        initColor();
        inimultiSize();
    }
    function inputPriceKeydown(){
        $("#form_price").keydown(function (event) {
            if (event.keyCode == 13) {
                changPrice($("#form_class9").val());
            }
        })
    }
    function changPrice(name){
        var price = $("#form_price").val();
        var purPrice;
        var wsPrice;
        switch (name){
            case "AS":
                checkNum = 4.5;
                purPrice = Math.round(price * 0.25*10)/10.0;
                wsPrice = Math.round(price * 0.3*10)/10.0;
                $("#form_puPrice").val(purPrice);
                $("#form_wsPrice").val(wsPrice);
                break;
            case "AA":
                checkNum=2.95;
                purPrice = Math.round(price * 0.3*10)/10.0;
                wsPrice = Math.round(price * 0.45*10)/10.0;
                $("#form_puPrice").val(purPrice);
                $("#form_wsPrice").val(wsPrice);
                break;
            default:
                break;
        }
    }
    function setUrl(){
        //更改Grid的url
        var url="";
        if("${pageType}"=="edit"){
            url=basePath + "/prod/product/page.do?filter_EQS_styleId=${styleId}";
        }
        $("#CSGrid").jqGrid("setGridParam",{
            url:url,
            page : 1,
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
        if(Math.round($("#form_preCast").val())*checkNum > Math.round($("#form_price").val())){
            $.gritter.add({
                text: "采购价和吊牌价不符合定价规则，请核对对应价格",
                class_name: 'gritter-success  gritter-light'
            });
        }else{
            $('#editStyleForm').data('bootstrapValidator').validate();
            if (!$('#editStyleForm').data('bootstrapValidator').isValid()) {
                return;
            }
            if (editDtailRowId != null) {
                saveItem(editDtailRowId)
            }
            $("#form_sizeSortId").removeAttr("disabled");
            cs.showProgressBar();
            var dtlArray = [];
            $.each($("#CSGrid").getDataIDs(), function (dtlndex, dtlValue) {
                var dtlRow = $("#CSGrid").getRowData(dtlValue);
                dtlArray.push(dtlRow);
            });
            $.ajax({
                dataType: "json",
                url: basePath + "/prod/style/saveStyleAndProduct.do",
                data:{
                    styleStr:JSON.stringify(array2obj($("#editStyleForm").serializeArray())),
                    productStr:JSON.stringify(dtlArray),
                    userId: userId,
                    pageType: str
                },
                type: "POST",
                success: function (msg) {
                    cs.closeProgressBar();
                    if(msg.success){
                        $.gritter.add({
                            text: msg.msg,
                            class_name: 'gritter-success  gritter-light'
                        });
                    }else{
                        bootbox.alert(msg.msg);
                    }
                }
            });
        }


    }

    function deleteColorSize(rowId){
        var row=$("#CSGrid").jqGrid("getRowData",rowId);
        cs.showProgressBar("删除中");
        $.post(basePath+"/prod/product/delete.do?code="+row.code,
            function (result) {
                cs.closeProgressBar();
                $.gritter.add({
                    text: result.msg,
                    class_name: 'gritter-success  gritter-light'
                });
                if(result.success){
                    $("#CSGrid").delRowData(rowId);
                }
            }
        );
    }
    var editDtailRowId;
    function iniGrid() {
        $("#CSGrid").jqGrid({
            height: "300px",
            datatype: 'json',
            mtype: 'POST',
            colModel: [
                {
                    name: "", label: "操作", width: 80, editable: false, align: "center",
                    formatter: function (cellvalue, options, rowObject) {

                        return "<a style='margin-left: 20px' a  href='javascript:void(0);' onclick=deleteColorSize('"+options.rowId+"')><i class='ace-icon fa fa-trash-o red' title='删除'></i></a>";

                    }
                },
                {name: 'colorId', label: '颜色', editable: false},
                {name: 'sizeId', label: '尺寸', editable: false},
                {name: 'barcode', label: '条码', editable: true,
                    editrules: {
                        number: true
                    }
                },
                {name: 'push', label: '推送', editable: false},
                {name: 'code', hidden:true}

            ],
            viewrecords: true,
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
            onSelectRow: function (rowid, status) {
                if (editDtailRowId != null) {
                    saveItem(editDtailRowId);
                }
                editDtailRowId = rowid;
                $('#CSGrid').editRow(rowid);
            }

        });
        $("#CSGrid").setGridWidth($("#parentWidth").width());
    }
    function saveItem(rowId) {
        editDtailRowId = null;
        $('#CSGrid').saveRow(rowId);

    }
    function inimultiSize() {
        $.ajax({
            url: basePath + "/prod/size/searchSizeMap.do?filter_EQS_sortId=${style.sizeSortId}",
            cache: false,
            async: false,
            type: 'POST',
            success: function (data, textStatus) {
              var index =1;
              for(var key in data){
                  $("#form_sizeId").append("<optgroup label='"+key+"' class= 'group"+index+"'>");
                    $.each(data[key],function(index,value){
                        $("#form_sizeId").append("<option value='" + value.sizeId + "'>" + value.sizeName + "</option>");
                    });
                    index++;
                  $("#form_sizeId").append("</optgroup>");
              }
                $('#form_sizeId').multiselect('rebuild');

        }
        });
    }

    function initSizeSort() {
        $.ajax({
            url: basePath + "/prod/size/listSizeSort.do?",
            cache: false,
            async: false,
            type: "POST",
            success: function (data, textStatus) {
                var json = data;
                $("#form_sizeSortId").empty();
                $("#form_sizeSortId").multiselect({
                    inheritClass: true,
                    includeSelectAllOption: true,
                    selectAllNumber: true,
                    enableFiltering: true,
                    filterPlaceholder:"请选择尺寸组",
                    maxHeight: "400",
                    onChange: function (option, checked) {//change事件改变
                        //console.log(option.length + ' options ' + (checked ? 'selected' : 'deselected'));
                        
                        var a=option[0].text;
                        if(a.length>5){
                            $("#form_sizeSortId").next().children("button").text(a.substr(0, 5)+"...");
                        }



                    }
                });
                $("#form_sizeSortId").append(" <option value='' style='background-color: #eeeeee'>请选择尺寸组</option>");
                for (var i = 0; i < json.length; i++) {
                    $("#form_sizeSortId").append("<option value='"+json[i].id+"' style='background-color: #eeeeee'>"+ "[" + json[i].id + "]" + json[i].sortName + "</option>");
                }
                
                if("${pageType}"=="edit"){
                    $("#form_sizeSortId").find("option[value='${style.sizeSortId}']").attr("selected",true);
                }
                $('#form_sizeSortId').multiselect('rebuild');
            }

        });
    }

    function iniBrandCode(){
        $.ajax({
            url:basePath+"/sys/property/searchByType.do?type=C1",
            cache:false,
            async:false,
            type:"POST",
            success:function (data,textStatus) {
                var json=data;

                $("#form_brandCode").empty();
                $("#form_brandCode").multiselect({
                    inheritClass: true,
                    includeSelectAllOption: true,
                    selectAllNumber: true,
                    enableFiltering: true,
                    filterPlaceholder:"请选择品牌",
                    maxHeight: "400",
                    onChange: function (option, checked) {//change事件改变
                        //console.log(option.length + ' options ' + (checked ? 'selected' : 'deselected'));
                        
                        var a=option[0].text;
                        if(a.length>5){
                            $("#form_brandCode").next().children("button").text(a.substr(0, 5)+"...");
                        }



                    }
                });
                $("#form_brandCode").append(" <option value='' style='background-color: #eeeeee'>选择品牌</option>");
                for (var i = 0; i < json.length; i++) {
                    $("#form_brandCode").append("<option value='"+json[i].code+"' style='background-color: #eeeeee'>"+json[i].name+"</option>");
                }
                
                if("${pageType}"=="edit"){
                    $("#form_brandCode").find("option[value='${style.brandCode}']").attr("selected",true);
                }
                $('#form_brandCode').multiselect('rebuild');
            }
        });
    }

    function initColor() {
        $.ajax({
            url: basePath + "/prod/color/list.do?",
            cache: false,
            async: false,
            type: "POST",
            success: function (data, textStatus) {
                var json = data;
                $("#form_colorId_select").empty();
                $("#form_colorId_select").multiselect({
                    inheritClass: true,
                    includeSelectAllOption: true,
                    selectAllNumber: true,
                    enableFiltering: true,
                    filterPlaceholder:"请输入颜色",
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
                        }
                    }
                },
                price: {
                    validators: {
                        notEmpty: {message: '价格不能为空'},
                        numeric: {message: '价格只能输入数字'}
                    }
                }
            }
        });
    }

    function addbrand(rowId) {
        pagetype="add";
        $("#editFormdetailed").resetForm();
        $("#edit-dialog-detailed").modal('show');
        $("#form_code").removeAttr("readOnly");
        $("#form_ids").val(rowId);
    }
    function saveproperty() {
        
        $("#editFormdetailed").data('bootstrapValidator').validate();
        if(!$("#editFormdetailed").data('bootstrapValidator').isValid()){
            return ;
        }
        /* if($("#form_ownerId").val()==""){
         bootbox.alert("所属方不能为空");
         return;
         }*/
        var progressDialog = bootbox.dialog({
            message: '<p><i class="fa fa-spin fa-spinner"></i>数据上传中...</p>'
        });
        $.post(basePath+'/sys/property/saveproperty.do',
            $("#editFormdetailed").serialize(),
            function(result){
                if(result.success==true||result.success=='true'){
                    $.gritter.add({
                        text : result.msg,
                        class_name : 'gritter-success  gritter-light'
                    });
                    progressDialog.modal('hide');
                    $("#edit-dialog-detailed").modal('hide');
                    $('#grid').trigger("reloadGrid");
                }
            },'json');
        window.location.reload();
    }

</script>
</body>
</html>