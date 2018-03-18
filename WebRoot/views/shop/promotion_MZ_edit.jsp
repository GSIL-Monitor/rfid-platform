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
    </script>
    <style>
        a#focusColor:hover{
            background-color: #bbbbbb;
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

                        <a href="#" onclick="toIndex()">促销活动</a>
                    </li>
                    <li class="active">
                        <c:if test="${pageType == 'add'}">
                            增加
                        </c:if>
                        <c:if test="${pageType == 'edit'}">
                            编辑
                        </c:if>
                        ${pageTitle}
                    </li>
                </ul><!-- /.breadcrumb -->


            </div>

            <div id="page-content">

                <div class="row">
                    <div class="col-xs-5">
                        <!-- PAGE CONTENT BEGINS -->
                        <div class="widget-body">

                            <div class="widget-main">
                       <div class="widget-box widget-color-blue  light-border">
                                        <div class="widget-header">
                                            <h5 class="widget-title">促销规则</h5>
                                            <div class="widget-toolbar">

                                            </div>
                                        </div>
                                        <br />
                                        <form class="form-horizontal" role="form" id="editForm">


                                            <div class="form-group">
                                                <label class="col-sm-2 control-label no-padding-right"
                                                       for="form_styleId">活动名称</label>

                                                <div class="col-xs-9 col-sm-9">
                                                    <input class="form-control" id="form_styleId" name="styleId"
                                                           type="text" placeholder="" value="" onkeyup="this.value=this.value.toUpperCase()"/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label no-padding-right"
                                                       for="search_createDate">活动日期</label>

                                                <div class="col-xs-9 col-sm-9">
                                                    <div class="input-group">
                                                        <input class="form-control date-picker"
                                                               id="search_createDate" type="text"
                                                               name="filter_GED_createDate" data-date-format="yyyy-mm-dd" />

                                                        <span class="input-group-addon"> <i
                                                                class="fa fa-exchange"></i>
													</span> <input class="form-control date-picker" type="text"
                                                                   class="input-sm form-control" name="filter_LED_createDate"
                                                                   data-date-format="yyyy-mm-dd" />
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label no-padding-right"
                                                       for="form_price">活动规则</label>

                                                <div class="col-xs-9 col-sm-9">
                                                    <span class="help-inline ">
												<span class="middle">满</span>
											</span>

                                                    <input class="input-mini" id="form_price" name="price"
                                                           type="text" placeholder="" value=""/><span class="help-inline">
												<span class="middle"> 元
                                                    加</span>
											</span>
                                                    <input class="input-mini" id="form_price_2" name="price"
                                                           type="text" placeholder="" value=""/> 元
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="col-sm-2 control-label no-padding-right"
                                                       for="form_vip">最低会员限制</label>

                                                <div class="col-xs-9 col-sm-9">
                                                    <select class="chosen-select form-control" id="form_vip" name="brandCode">
                                                        <option value="" style="background-color: #eeeeee" selected>无限制</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label no-padding-right"
                                                       for="form_remark">活动备注</label>

                                                <div class="col-xs-9 col-sm-9">
                                            <textarea maxlength="400" class="form-control" id="form_remark"
                                                      name="remark"></textarea>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label no-padding-right"
                                                       for="form_styleEname">广告语</label>
                                                <div class="col-xs-9 col-sm-9">
                                                    <input class="form-control" id="form_styleEname" name="styleEname"
                                                           type="text" placeholder="" value=""/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <div class="col-sm-offset-5 col-sm-10">
                                                    <button class="btn btn-xs btn-success" type="button"
                                                            id="saveStyleBtn"
                                                            onclick="saveStyle()">
                                                        <i class="ace-icon fa fa-hand-right"></i>
                                                        <span class="bigger-110">下一步</span>
                                                    </button>
                                                </div>
                                            </div>
                                        </form>
                       </div>
                            </div>
                        </div>


                    </div>
                    <!-- /.col -->
                    <div class="col-xs-7">
                        <!-- PAGE CONTENT BEGINS -->
                        <div class="widget-body">

                            <div class="widget-main">
                                    <div class="widget-box widget-color-blue  light-border"  id="zhuPingParent">
                                        <div class="widget-header">
                                            <h5 class="widget-title">设置主商品</h5>
                                            <div class="widget-toolbar">
                                                <a href="javascript:void(0)" class="white" onclick="addColorAndSize()">
                                                    <i class="ace-icon fa fa-plus"></i>
                                                    <span class="bigger-110 ">增加</span>
                                                </a>
                                                <a href="javascript:void(0)" class="white" onclick="deleteProd()">
                                                    <i class="ace-icon fa fa-close"></i>
                                                    <span class="bigger-110 ">删除</span>
                                                </a>

                                            </div>
                                        </div>

                                        <table id="zhuPingGrid"></table>
                                        <table id="zhuPingPage"></table>

                                    </div>
                            </div>
                        </div>
                        <div class="widget-body">

                            <div class="widget-main">
                                <div class="widget-box widget-color-blue  light-border" id="zengPingParent">
                                    <div class="widget-header">
                                        <h5 class="widget-title">设置赠品</h5>
                                        <div class="widget-toolbar">
                                            <a   href="javascript:void(0)" class="white" onclick="addColorAndSize()">
                                                <i class="ace-icon fa fa-plus"></i>
                                                <span class="bigger-110 ">增加</span>
                                            </a>
                                            <a   href="javascript:void(0)" class="white" onclick="deleteProd()">
                                                <i class="ace-icon fa fa-close"></i>
                                                <span class="bigger-110 ">删除</span>
                                            </a>

                                        </div>
                                    </div>

                                    <table id="zengPingGrid"></table>
                                    <table id="zengPingPage"></table>

                                </div>
                            </div>
                        </div>
                    </div>
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

<script src="<%=basePath%>/Olive/assets/js/bootstrap-multiselect.js"></script>
<script type="text/javascript">
    $(function () {
//        initLoadStyle();
        iniGrid();
        iniBrandCode();
        initColor();

        initEditFormValid();

        $("#form_sizeId").multiselect({
            inheritClass: true,
            includeSelectAllOption: true,
            selectAllNumber: true,
            maxHeight: "400"
        });

    });


    function toIndex() {
        location.href = basePath + "/shop/promotion/index.do"
    }



    function saveStyle() {
        $('#editForm').data('bootstrapValidator').validate();
        if (!$('#editForm').data('bootstrapValidator').isValid()) {
            return;
        }
        $("#form_sizeSortId").removeAttr("disabled");
        cs.showProgressBar();
        $.post(basePath + "/prod/style/save.do",
            $("#editForm").serialize(),
            function (result) {
                cs.closeProgressBar();
                if (result.success == true || result.success == 'true') {

                    $("#form_id").val(result.result.id);
                    $("#form_styleId").attr("readonly", true);
                    $.gritter.add({
                        text: result.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                    location.href=basePath+"/prod/style/edit.do?styleId="+$("#form_styleId").val();
                } else {
                    cs.showAlertMsgBox(result.msg);
                }
            }, 'json');
    }

    function deleteProd(){
        var rowId = $("#CSGrid").jqGrid("getGridParam","selrow");
        if(rowId){
                var row=$("#CSGrid").jqGrid("getRowData",rowId);
                cs.showProgressBar("删除中");
                $.post(basePath+"/prod/product/delete.do?code="+row.code,
                function (result) {
                    cs.closeProgressBar();
                    $.gritter.add({
                        text: result.msg,
                        class_name: 'gritter-success  gritter-light'
                    });
                    setUrl();
                }
            );
        } else{
            bootbox.alert("请选择一项进行删除");
        }

    }
    function iniGrid() {
        $("#zhuPingGrid").jqGrid({
            height: "180px",
            datatype: 'json',
            mtype: 'POST',
            colModel: [
                {
                    name: "", label: "操作", width: 70,editable:false,align:"center",frozen:true,sortable:false,
                    formatter: function (cellvalue, options, rowObject) {

                        return "<a><i class='ace-icon fa fa-list'></i>删除</a>";
                    }
                },
                {name: 'code', label: 'SKU', editable: true},
                {name: 'styleId', label: '款号', editable: true},
                {name: 'colorId', label: '颜色', editable: true},
                {name: 'sizeId', label: '尺寸', editable: true},
                {name: 'price', label: '吊牌价', editable: true}

            ],
            viewrecords: true,
            autoWidth: true,
            rownumbers: true,
            altRows: true,
            rowNum: 20,
            rowList: [20],
            pager: "#zhuPingPage",
            multiselect: false,
            shrinkToFit: true,
            autoScroll: false,
            sortname: 'colorId',
            sortorder: "asc"

        });
        $("#zhuPingGrid").setGridWidth($("#zhuPingParent").width());
        $("#zengPingGrid").jqGrid({
            height: "180px",
            datatype: 'json',
            mtype: 'POST',
            colModel: [
                {
                    name: "", label: "操作", width: 70,editable:false,align:"center",frozen:true,sortable:false,
                    formatter: function (cellvalue, options, rowObject) {

                        return "<a><i class='ace-icon fa fa-list'></i>删除</a>";
                    }
                },
                {name: 'code', label: 'SKU', editable: true},
                {name: 'styleId', label: '款号', editable: true},
                {name: 'colorId', label: '颜色', editable: true},
                {name: 'sizeId', label: '尺寸', editable: true},
                {name: 'price', label: '吊牌价', editable: true}

            ],
            viewrecords: true,
            autoWidth: true,
            rownumbers: true,
            altRows: true,
            rowNum: 20,
            rowList: [20],
            pager: "#zengPingPage",
            multiselect: false,
            shrinkToFit: true,
            autoScroll: false,
            sortname: 'colorId',
            sortorder: "asc"

        });
        $("#zengPingGrid").setGridWidth($("#zengPingParent").width());

    }



    function initEditFormValid() {
        $('#editForm').bootstrapValidator({
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
                        $('#editForm').bootstrapValidator('disableSubmitButtons', false);
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
                            regexp: /^[a-zA-Z0-9_]+$/,
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
</script>
</body>
</html>