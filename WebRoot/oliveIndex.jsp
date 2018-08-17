<%@ page language="java"
         import="java.util.*,com.casesoft.dmc.model.sys.User"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    User user = (User) session.getAttribute("userSession");
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <link rel="shortcut icon" href="<%=basePath%>favicon.ico">
    <title>CaseSoft SMARTRFID智慧平台</title>

    <meta name="description" content="overview &amp; stats"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>

    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/bootstrap.css"/>
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/font-awesome.css"/>

    <!-- page specific plugin styles -->
    <link rel="stylesheet" href="<%=basePath%>Olive/plugin/bootstrap-validator/css/bootstrapValidator.min.css"/>
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/jquery-ui.css"/>

    <!-- text fonts -->
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/ace-fonts.css"/>

    <!-- ace styles -->
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/ace.css" class="ace-main-stylesheet"
          id=""/>
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/ace-skins.css" class="ace-main-stylesheet"
          id="main-ace-style"/>

    <!--[if lte IE 9]>
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/ace-part2.css" class="ace-main-stylesheet"/>
    <![endif]-->

    <!--[if lte IE 9]>
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/ace-ie.css"/>
    <![endif]-->

    <!-- inline styles related to this page -->

    <!-- ace settings handler -->
    <script src="<%=basePath%>Olive/assets/js/ace-extra.js"></script>

    <!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

    <!--[if lte IE 8]>
    <script src="<%=basePath%>Olive/assets/js/html5shiv.js"></script>
    <script src="<%=basePath%>Olive/assets/js/respond.js"></script>
    <![endif]-->

    <link rel="stylesheet" href="<%=basePath%>css/main.css"/>
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/jquery-ui.custom.css"/>
</head>

<body class="skin-1">
<!-- #section:basics/navbar.layout -->
<div id="navbar" class="navbar navbar-default">
    <script type="text/javascript">
        try {
            ace.settings.check('navbar', 'fixed')
        } catch (e) {
        }

    </script>

    <div class="navbar-container" id="navbar-container">
        <!-- #section:basics/sidebar.mobile.toggle -->
        <button type="button" class="navbar-toggle menu-toggler pull-left" id="menu-toggler" data-target="#sidebar">
            <span class="sr-only">Toggle sidebar</span>

            <span class="icon-bar"></span>

            <span class="icon-bar"></span>

            <span class="icon-bar"></span>
        </button>

        <!-- /section:basics/sidebar.mobile.toggle -->
        <div class="navbar-header pull-left">
            <!-- #section:basics/navbar.layout.brand -->
            <a href="#" class="navbar-brand">
                <small>
                    <img src="<%=basePath%>/images/icon/casesoft-white.png"
                         style="width:25px;height:25px;margin-top:-3px;"/>
                    RFID-SMART 智慧平台
                </small>
            </a>

            <!-- /section:basics/navbar.layout.brand -->

            <!-- #section:basics/navbar.toggle -->

            <!-- /section:basics/navbar.toggle -->
        </div>

        <!-- #section:basics/navbar.dropdown -->
        <div class="navbar-buttons navbar-header pull-right" role="navigation">
            <ul class="nav ace-nav">


                <!-- #section:basics/navbar.user_menu -->
                <li class="light-blue">
                    <a data-toggle="dropdown" href="#" class="dropdown-toggle">
                        <img class="nav-user-photo" src="<%=basePath%>images/icon/user.png"/>
								<span class="user-info">
									<small>Welcome,</small>
									<%=user.getCode()%>
								</span>

                        <i class="ace-icon fa fa-caret-down"></i>
                    </a>

                    <ul class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
                        <li>
                            <a href="#" onclick="openEditPasswordDialog()">
                                <i class="ace-icon fa fa-lock"></i>
                                修改密码
                            </a>
                        </li>

                        <li>
                            <a href="#" onclick="openUserInfoDialog()">
                                <i class="ace-icon fa fa-user"></i>
                                用户信息
                            </a>
                        </li>

                        <li class="divider"></li>

                        <li>
                            <a href="#" onclick="logoutSys()">
                                <i class="ace-icon fa fa-power-off"></i>
                                注销
                            </a>
                        </li>
                    </ul>
                </li>

                <!-- /section:basics/navbar.user_menu -->
            </ul>
        </div>

        <!-- /section:basics/navbar.dropdown -->
    </div>

    <!-- /.navbar-container -->
</div>

<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>

    <div id="sidebar" class="sidebar h-sidebar navbar-collapse collapse">

        <script type="text/javascript">
            try {
                ace.settings.check('sidebar', 'fixed')
            } catch (e) {
            }
        </script>

        <div class="sidebar-shortcuts" id="sidebar-shortcuts">
            <!--左侧上方四个图标按钮-->
            <div class="sidebar-shortcuts-large" id="sidebar-shortcuts-large">
                <button class="btn btn-success" onclick="showOrHiddenSettingDialog()">
                    <i class="ace-icon fa fa-wrench bigger-110"></i>
                </button>

                <button class="btn btn-warning" onclick="openEditPasswordDialog()">
                    <i class="ace-icon fa fa-lock bigger-110"></i>
                </button>

                <!-- #section:basics/sidebar.layout.shortcuts -->
                <button class="btn btn-info" onclick="openUserInfoDialog()">
                    <i class="ace-icon fa fa-user bigger-110"></i>
                </button>

                <button class="btn btn-danger" onclick="logoutSys()">
                    <i class="ace-icon fa fa-power-off bigger-110"></i>
                </button>

                <!-- /section:basics/sidebar.layout.shortcuts -->
            </div>

            <div class="sidebar-shortcuts-mini" id="sidebar-shortcuts-mini">
                <span class="btn btn-success"></span>

                <span class="btn btn-info"></span>

                <span class="btn btn-warning"></span>

                <span class="btn btn-danger"></span>
            </div>
        </div>
        <!-- /.sidebar-shortcuts -->

        <ul class="nav nav-list" id="menu"></ul>
        <!-- /.nav-list -->

        <!-- #section:basics/sidebar.layout.minimize -->

        <!-- /section:basics/sidebar.layout.minimize -->
        <script type="text/javascript">
            try {
                ace.settings.check('sidebar', 'collapsed')
            } catch (e) {
            }
        </script>

    </div>

    <div class="main-content">
        <div class="main-content-inner">
            <div class="page-content">

                <div class="ace-settings-container" id="ace-settings-container">
                    <div class="btn btn-app btn-xs btn-warning ace-settings-btn" id="ace-settings-btn">
                        <i class="ace-icon fa fa-cog bigger-130"></i>
                    </div>

                    <div class="ace-settings-box clearfix" id="ace-settings-box">
                        <div class="pull-left width-50">
                            <!-- #section:settings.skins -->
                            <div class="ace-settings-item">
                                <div class="pull-left">
                                    <select id="skin-colorpicker" class="hide">
                                        <!--<option data-skin="no-skin" value="#438EB9">#438EB9</option>
                                        <option data-skin="skin-1" value="#222A2D">#222A2D</option>-->
                                        <option data-skin="skin-1" value="#222A2D">#222A2D</option>
                                        <option data-skin="no-skin" value="#438EB9">#438EB9</option>
                                        <option data-skin="skin-2" value="#C6487E">#C6487E</option>
                                        <option data-skin="skin-3" value="#D0D0D0">#D0D0D0</option>
                                    </select>
                                </div>
                                <span>&nbsp; 选择皮肤</span>
                            </div>

                            <!-- /section:settings.skins -->


                            <!-- #section:settings.container -->
                            <div class="ace-settings-item">
                                <input type="checkbox" class="ace ace-checkbox-2" id="ace-settings-add-container"/>
                                <label class="lbl" for="ace-settings-add-container">
                                    内容居中
                                </label>
                            </div>

                            <!-- /section:settings.container -->
                        </div>
                        <!-- /.pull-left -->

                        <div class="pull-left width-50">
                            <!-- #section:basics/sidebar.options -->
                            <div class="ace-settings-item">
                                <input type="checkbox" class="ace ace-checkbox-2" id="ace-settings-hover"/>
                                <label class="lbl" for="ace-settings-hover">子菜单悬浮</label>
                            </div>

                            <div class="ace-settings-item">
                                <input type="checkbox" class="ace ace-checkbox-2" id="ace-settings-compact"/>
                                <label class="lbl" for="ace-settings-compact">组合菜单</label>
                            </div>
                            <!-- /section:basics/sidebar.options -->
                        </div>
                        <!-- /.pull-left -->
                    </div>
                    <!-- /.ace-settings-box -->
                </div>
                <!-- /.ace-settings-container -->

                <div class="row">
                    <div class="col-xs-12  no-padding">
                        <div class="widget-box transparent">
                            <div class="widget-header">
                                <h5 class="widget-title bigger lighter">
                                    <i class="ace-icon fa fa-laptop"></i>
                                    <span id="tabTitle">主页</span>
                                </h5>
                                <div class="widget-toolbar no-border">
                                    <ul class="nav nav-tabs" id="nav_tabs" role="tablist">

                                    </ul>
                                </div>
                            </div>
                            <div class="widget-body">
                                <div class="widget-main no-padding">
                                    <div class="tab-content">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- /.page-content -->

            </div>
        </div>
        <!-- #main-content -->


    </div>
    </div>
    <!-- /.main-container -->


   <jsp:include page="/views/layout/footer.jsp"></jsp:include>

    <!-- 修改密码 -->
    <div id="editPassword-dialog" class="modal fade" tabindex="-1" role="dialog">>
        <div class="modal-dialog">
            <div class="modal-header no-padding">
                <div class="table-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        <span class="white">&times;</span>
                    </button>
                    修改密码
                </div>
            </div>
            <div class="modal-content">
                <div class="modal-body">
                    <form class="form-horizontal" role="form" id="editPasswordForm">
                        <input name="code" type="hidden" value="<%=user.getCode()%>"/>

                        <div class="form-group">
                            <label class="col-sm-2 control-label no-padding-right" for="form_password">新密码</label>

                            <div class="col-xs-14 col-sm-7">
                                <input class="form-control" id="form_password" name="password"
                                       type="password"
                                       placeholder="新密码"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-padding-right" for="form_rePassword">重复密码</label>

                            <div class="col-xs-14 col-sm-7">
                                <input class="form-control" id="form_rePassword" name="rePassword"
                                       type="password"
                                       placeholder="重复新密码"/>
                            </div>
                        </div>
                    </form>

                </div>
            </div>
            <div class="modal-footer">
                <button type="button" href="#" class="btn btn-primary" onclick="savePassword()">保存</button>
            </div>
        </div>
        <!-- /.modal -->
    </div>


    <!-- 用户信息 -->
    <div id="userInfo-dialog" class="modal fade" tabindex="-1" role="dialog">>
        <div class="modal-dialog">
            <div class="modal-header no-padding">
                <div class="table-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        <span class="white">&times;</span>
                    </button>
                    用户信息
                </div>
            </div>
            <div class="modal-content">
                <div class="modal-body">
                    <form class="form-horizontal" role="form" id="userInfoForm">
                        <input name="id" type="hidden" value="<%=user.getId()%>"/>

                        <div class="form-group">
                            <label class="col-sm-2 control-label no-padding-right" for="userInfoForm_code">编号</label>

                            <div class="col-xs-14 col-sm-7">
                                <input class="form-control" id="userInfoForm_code" name="code"
                                       value="<%=user.getCode()%>"
                                       type="text" readonly/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-padding-right" for="userInfoForm_name">名称</label>

                            <div class="col-xs-14 col-sm-7">
                                <input class="form-control" id="userInfoForm_name" name="name"
                                       value="<%=user.getName()%>"
                                       type="text" readonly/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-padding-right" for="userInfoForm_email">邮箱</label>

                            <div class="col-xs-14 col-sm-7">
                                <input class="form-control" id="userInfoForm_email" name="email"
                                       value="<%=user.getEmail()%>"
                                       type="text" readonly/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label no-padding-right" for="userInfoForm_phone"
                                   value="<%=user.getPhone()%>">手机</label>

                            <div class="col-xs-14 col-sm-7">
                                <input class="form-control" id="userInfoForm_phone" name="phone"
                                       type="text" readonly/>
                            </div>
                        </div>

                    </form>

                </div>
            </div>
            <div class="modal-footer">
                <button type="button" href="#" class="btn btn-primary" onclick="closeUserInfoDialog()">关闭</button>
            </div>
        </div>
        <!-- /.modal -->
    </div>
    <!-- basic scripts -->

    <!--[if !IE]> -->
    <script type="text/javascript">
        window.jQuery || document.write("<script src='<%=basePath%>Olive/assets/js/jquery.js'>" + "<" + "/script>");
    </script>

    <!-- <![endif]-->

    <!--[if IE]>
    <script type="text/javascript">
        window.jQuery || document.write("<script src='<%=basePath%>Olive/assets/js/jquery1x.js'>" + "<" + "/script>");
    </script>
    <![endif]-->
    <script type="text/javascript">
        if ('ontouchstart' in document.documentElement) document.write("<script src='<%=basePath%>Olive/assets/js/jquery.mobile.custom.js'>" + "<" + "/script>");
    </script>
    <script src="<%=basePath%>Olive/assets/js/bootstrap.js"></script>

    <!-- page specific plugin scripts -->

    <!--[if lte IE 8]>
    <script src="<%=basePath%>Olive/assets/js/excanvas.js"></script>
    <![endif]-->
    <script src="<%=basePath%>Olive/assets/js/jquery-ui.js"></script>
    <script src="<%=basePath%>Olive/assets/js/jquery.ui.touch-punch.js"></script>
    <script src="<%=basePath%>Olive/assets/js/bootbox.js"></script>

    <script src="<%=basePath%>Olive/plugin/bootstrap-validator/js/bootstrapValidator.js"></script>
    <script src="<%=basePath%>Olive/plugin/bootstrap-validator/js/language/zh_CN.js"></script>
    <script src="<%=basePath%>Olive/plugin/jquery.form.js"></script>

    <!-- ace scripts -->
    <script src="<%=basePath%>Olive/assets/js/ace/elements.scroller.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/elements.colorpicker.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/elements.fileinput.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/elements.typeahead.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/elements.wysiwyg.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/elements.spinner.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/elements.treeview.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/elements.wizard.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/elements.aside.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.ajax-content.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.touch-drag.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.sidebar.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.sidebar-scroll-1.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.submenu-hover.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.widget-box.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.settings.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.settings-rtl.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.settings-skin.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.widget-on-reload.js"></script>
    <script src="<%=basePath%>Olive/assets/js/ace/ace.searchbox-autocomplete.js"></script>
    <script src="<%=basePath%>Olive/assets/js/sidebar-menu.js"></script>
    <script type="text/javascript" src="<%=basePath%>/jslib2/constant.js"></script>
    <!--左侧手风琴-->
    <script type="text/javascript" src="<%=basePath%>/Olive/assets/js/jquery.slimscroll.min.js"></script>

    <!-- inline scripts related to this page -->
    <script type="text/javascript">
        window.onbeforeunload = function () {

            var billNosale = sessionStorage.getItem("billNosale");
            if (billNosale != "" && billNosale != null) {
                $.ajax({
                    url: "<%=basePath%>logistics/saleOrder/quit.do?billNo=" + billNosale,
                    cache: false,
                    async: false,
                    type: "POST",
                    success: function (data, textStatus) {

                        if (textStatus == "success") {

                            sessionStorage.removeItem("billNosale");
                        }

                    }
                });
            }
            var billNosaleReturn = sessionStorage.getItem("billNosaleReturn");
            if (billNosaleReturn != "" && billNosaleReturn != null) {
                $.ajax({
                    url: "<%=basePath%>logistics/saleOrderReturn/quit.do?billNo=" + billNosaleReturn,
                    cache: false,
                    async: false,
                    type: "POST",
                    success: function (data, textStatus) {

                        if (textStatus == "success") {

                            sessionStorage.removeItem("billNosaleReturn");
                        }

                    }
                });
            }
            var billNoConsignment = sessionStorage.getItem("billNoConsignment");
            if (billNoConsignment != "" && billNoConsignment != null) {
                $.ajax({
                    url: "<%=basePath%>logistics/Consignment/quit.do?billNo=" + billNoConsignment,
                    cache: false,
                    async: false,
                    type: "POST",
                    success: function (data, textStatus) {

                        if (textStatus == "success") {

                            sessionStorage.removeItem("billNoConsignment");
                        }

                    }
                });
            }
            var billNotransfer = sessionStorage.getItem("billNotransfer");
            if (billNotransfer != "" && billNotransfer != null) {
                $.ajax({
                    url: "<%=basePath%>logistics/transferOrder/quit.do?billNo=" + billNotransfer,
                    cache: false,
                    async: false,
                    type: "POST",
                    success: function (data, textStatus) {

                        if (textStatus == "success") {

                            sessionStorage.removeItem("billNotransfer");
                        }

                    }
                });
            }
            var billNoPurchaseReturn = sessionStorage.getItem("billNoPurchaseReturn");
            if (billNopurchase != "" && billNopurchase != null) {
                $.ajax({
                    url: "<%=basePath%>logistics/purchaseReturn/quit.do?billNo=" + billNoPurchaseReturn,
                    cache: false,
                    async: false,
                    type: "POST",
                    success: function (data, textStatus) {

                        if (textStatus == "success") {

                            sessionStorage.removeItem("billNoPurchaseReturn");
                        }

                    }
                });
            }
            var billNopurchase = sessionStorage.getItem("billNopurchase");
            if (billNopurchase != "" && billNopurchase != null) {
                $.ajax({
                    url: "<%=basePath%>logistics/purchase/quit.do?billNo=" + billNopurchase,
                    cache: false,
                    async: false,
                    type: "POST",
                    success: function (data, textStatus) {

                        if (textStatus == "success") {

                            sessionStorage.removeItem("billNopurchase");
                        }

                    }
                });
            }

        };

        function initMenu() {
            $('#menu').sidebarMenu({
                url: "<%=basePath%>/sys/role/showSidebarMenu.do",
                param: {roleId: "<%=user.getRoleId()%>"}
            });
        }

        var addTabs = function (options) {
            var url = "<%=basePath%>";
            options.url = url + options.url;
            var id = "tab_" + options.id;
            $(".active").removeClass("active");
            //如果TAB不存在，创建一个新的TAB
            if (!$("#" + id)[0]) {

                var mainHeight = $(window).height();
                var title = '<li class="hover" role="presentation" id="tab_' + id + '"><a href="#' + id + '" aria-controls="' + id + '" role="tab" data-toggle="tab">' + options.title;
                if (options.close) {
                    title += ' <i class="glyphicon glyphicon-remove" tabclose="' + id + '"></i>';
                }
                title += '</a></li>';
                var content;
                var innerHeight = mainHeight - 110;
                if (options.content) {
                    content = '<div role="tabpanel" class="tab-pane" id="' + id + '">' + options.content + '</div>';
                } else { //没有内容，使用IFRAME打开链接
                    content = '<div role="tabpanel" class="tab-pane" id="' + id + '"><iframe id="iframe_' + options.id + '" name="iframe_' + options.id + '"  src="' + options.url + '/index.do" width="100%" height="' + innerHeight +
                            '" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="auto" allowtransparency="yes"></iframe></div>';
                }
                //加入TABS
                $(".nav-tabs").append(title);
                $(".tab-content").append(content);
            }
            //激活TAB
            $("#tab_" + id).addClass('active');
            $("#tabTitle").html(options.title);
            $("#" + id).addClass("active");
            $("#menu_" + options.id).attr("class", "active");
            $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
                var activeTab = $(e.target).text();
                $("#tabTitle").html(activeTab);
            });
        };

        var closeTab = function (id) {

            //如果关闭的是当前激活的TAB，激活他的前一个TAB
            if ($("li.active").attr('id') == "tab_" + id) {
                $("#tab_" + id).prev().addClass('active');
                $("#" + id).prev().addClass('active');
            }
            //关闭TAB
            $("#tab_" + id).remove();
            $("#" + id).remove();
            var billNosale = sessionStorage.getItem("billNosale");
            if (billNosale != "" && billNosale != null) {
                $.ajax({
                    url: "<%=basePath%>logistics/saleOrder/quit.do?billNo=" + billNosale,
                    cache: false,
                    async: false,
                    type: "POST",
                    success: function (data, textStatus) {

                        if (textStatus == "success") {

                            sessionStorage.removeItem("billNosale");
                        }

                    }
                });
            }
            var billNosaleReturn = sessionStorage.getItem("billNosaleReturn");
            if (billNosaleReturn != "" && billNosaleReturn != null) {
                $.ajax({
                    url: "<%=basePath%>logistics/saleOrderReturn/quit.do?billNo=" + billNosaleReturn,
                    cache: false,
                    async: false,
                    type: "POST",
                    success: function (data, textStatus) {

                        if (textStatus == "success") {

                            sessionStorage.removeItem("billNosale");
                        }

                    }
                });
            }
            var billNoConsignment = sessionStorage.getItem("billNoConsignment");
            if (billNoConsignment != "" && billNoConsignment != null) {
                $.ajax({
                    url: "<%=basePath%>logistics/Consignment/quit.do?billNo=" + billNoConsignment,
                    cache: false,
                    async: false,
                    type: "POST",
                    success: function (data, textStatus) {

                        if (textStatus == "success") {

                            sessionStorage.removeItem("billNosale");
                        }

                    }
                });
            }
            var billNotransfer = sessionStorage.getItem("billNotransfer");
            if (billNotransfer != "" && billNotransfer != null) {
                $.ajax({
                    url: "<%=basePath%>logistics/transferOrder/quit.do?billNo=" + billNotransfer,
                    cache: false,
                    async: false,
                    type: "POST",
                    success: function (data, textStatus) {

                        if (textStatus == "success") {

                            sessionStorage.removeItem("billNosale");
                        }

                    }
                });
            }
            var billNopurchase = sessionStorage.getItem("billNopurchase");
            if (billNopurchase != "" && billNopurchase != null) {
                $.ajax({
                    url: "<%=basePath%>logistics/purchase/quit.do?billNo=" + billNopurchase,
                    cache: false,
                    async: false,
                    type: "POST",
                    success: function (data, textStatus) {

                        if (textStatus == "success") {

                            sessionStorage.removeItem("billNosale");
                        }

                    }
                });
            }
            var billNoPurchaseReturn = sessionStorage.getItem("billNoPurchaseReturn");
            if (billNopurchase != "" && billNopurchase != null) {
                $.ajax({
                    url: "<%=basePath%>logistics/purchaseReturn/quit.do?billNo=" + billNoPurchaseReturn,
                    cache: false,
                    async: false,
                    type: "POST",
                    success: function (data, textStatus) {

                        if (textStatus == "success") {

                            sessionStorage.removeItem("billNoPurchaseReturn");
                        }

                    }
                });
            }
        };
        $(function () {

            $("[addtabs]").click(function () {
                addTabs({
                    id: $(this).attr("id"),
                    title: $(this).attr('title'),
                    close: true
                });
            });

            $(".nav-tabs").on("click", "[tabclose]", function (e) {
                var id = $(this).attr("tabclose");
                closeTab(id);
            });
            initMenu();
            $("#ace-settings-container").hide();

        });


        /**
         * 修改密码
         */
        function openEditPasswordDialog() {
            $("#editPassword-dialog").modal('show');
        }
        $(function () {
            $("#editPassword-dialog").on('show.bs.modal', function () {

                initEditPassFormValid();
            });
        });

        function initEditPassFormValid() {
            $('#editPasswordForm').bootstrapValidator({
                message: '输入值无效',
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                fields: {
                    password: {
                        message: '密码无效',
                        validators: {
                            notEmpty: {
                                message: '密码不能为空'
                            },
                            stringLength: {
                                min: 6,
                                max: 10,
                                message: '用户名长度必须在6到10之间'
                            },
                            identical: {//相同
                                field: 'rePassword', //需要进行比较的input name值
                                message: '两次密码不一致'
                            },
//                        different: {//不能和用户名相同
//                            field: 'username',//需要进行比较的input name值
//                            message: '不能和用户名相同'
//                        },
                            regexp: {
                                regexp: /^[a-zA-Z0-9_\.]+$/,
                                message: '密码只能是数字或字母'
                            }
                        }
                    },
                    rePassword: {
                        message: '密码无效',
                        validators: {
                            notEmpty: {
                                message: '重复不能为空'
                            },
                            stringLength: {
                                min: 5,
                                max: 10,
                                message: '用户名长度必须在5到10之间'
                            },
                            identical: {//相同
                                field: 'password',
                                message: '两次密码不一致'
                            },
                            regexp: {//匹配规则
                                regexp: /^[a-zA-Z0-9_\.]+$/,
                                message: '密码只能是数字或字母'
                            }
                        }
                    }
                }
            });
        }
        function savePassword() {
            $('#editPasswordForm').data('bootstrapValidator').validate();
            if (!$('#editPasswordForm').data('bootstrapValidator').isValid()) {
                return;
            }
            cs.showProgressBar();
            $.post("<%=basePath%>/sys/user/resetPwd.do",
                    $("#editPasswordForm").serialize(),
                    function (result) {
                        if (result.success == true || result.success == 'true') {
                            cs.closeProgressBar();
                            $("#editPassword-dialog").modal('hide');
                        } else {
                            cs.showAlertMsgBox(result.msg);
                        }
                    }, 'json');
        }

        /**
         *用户信息
         */
        function openUserInfoDialog() {
            $("#userInfo-dialog").modal('show');
        }

        function closeUserInfoDialog() {
            $("#userInfo-dialog").modal('hide');
        }

        /**
         *退出系统
         */
        function logoutSys() {
            cs.showConfirmMsgBox('是否退出系统登录！', function (result) {
                if (result) {
                    $.post("<%=basePath%>sys/user/logout.do",
                            function (result) {
                                if (result.success == true) {
                                    location.href = "<%=basePath%>oliveLogin.jsp";
                                } else {
                                    cs.showAlertMsgBox(result.msg);
                                }


                            }, 'json');

                }
            });

        }

        /**
         *显示或隐藏 设置Dialog
         */
        var showSetting = false;
        function showOrHiddenSettingDialog() {
            if (showSetting) {
                $("#ace-settings-container").hide();
                showSetting = false;
            } else {
                $("#ace-settings-container").show();
                showSetting = true;
            }
        }

    </script>
    <script type="text/javascript">

        function setScroll() {
            var winHeight = window.innerHeight - 45;
            $("#scroll").slimScroll({
                height: winHeight,
                alwaysVisible: true,
            });
        }
        setScroll();
        $(window).on("resize", setScroll);
    </script>
    <script type="text/javascript"> ace.vars['base'] = '..'; </script>
</body>
</html>
