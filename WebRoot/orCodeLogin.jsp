<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

    String message = (String)request.getSession().getAttribute("message");
%>

<!DOCTYPE html>
<head>
    <!-- BEGIN META -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="CaseSoft RFID">
    <meta name="author" content="CaseSoft Software">
    <!-- END META -->

    <!-- BEGIN SHORTCUT ICON -->
    <link rel="shortcut icon" href="favicon.ico">
    <!-- END SHORTCUT ICON -->
    <title>CaseSoft RFID-SMART大数据平台</title>

    <meta name="description" content="User login page" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/bootstrap.css" />
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/font-awesome.css" />

    <!-- text fonts -->
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/ace-fonts.css" />

    <!-- ace styles -->
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/ace.css" />

    <!--[if lte IE 9]>
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/ace-part2.css" />
    <![endif]-->
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/ace-rtl.css" />

    <!--[if lte IE 9]>
    <link rel="stylesheet" href="<%=basePath%>Olive/assets/css/ace-ie.css" />
    <![endif]-->

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->

    <!--[if lt IE 9]>
    <script src="<%=basePath%>Olive/assets/js/html5shiv.js"></script>
    <script src="<%=basePath%>Olive/assets/js/respond.js"></script>
    <![endif]-->
    <style>
        body,button, input, select, textarea,h1 ,h2, h3, h4, h5, h6 { font-family: Microsoft YaHei,'宋体' , Tahoma, Helvetica, Arial, "\5b8b\4f53", sans-serif;}
        .center-vertical {
            position:relative;
            top:50%;
            transform:translateY(30%);
        }

        .login-bg
        {
            background-image:url(images/bg/login_bg.jpg);
            background-repeat:no-repeat;
        }

    </style>

    <script type="text/javascript">


        function check(){
            var frm = document.loginForm;
            if(frm.code.value==""){
                alert("用户名不能为空!");
                document.loginForm.code.focus();
                return false;
            }else if(frm.password.value==""){
                alert("登录密码不能为空!");
                document.loginForm.password.focus();
                return false;
            }else {
                if ($("#ck_remMe").is(':checked')) {
                    var str_username = $("#code").val();
                    var str_password = $("#password").val();
                    $.cookie("rmbUser", "true", { expires: 7 }); //存储一个带7天期限的cookie
                    $.cookie("code", str_username, { expires: 7 });
                    $.cookie("password", str_password, { expires: 7 });
                }
                else {
                    $.cookie("rmbUser", "false", { expire: -1 });
                    $.cookie("code", "", { expires: -1 });
                    $.cookie("password", "", { expires: -1 });
                }
                return true;
            }
        }

        function isExistUser() {
            var strcookie = document.cookie;
            var arrcookie = strcookie.split(";");
            for(var i=0;i<arrcookie.length;i++) {
                var arr = arrcookie[i].split("=");
                if("userId" === arr[0] && "exist" == arr[1]) {
                    return true;
                }
            }
            return false;
        }

        function checkParent() {

            var message = '<%=message%>';
            if(message != 'null')
                document.getElementById("messagePanel").style.display="";//messagePanel


            if (window.parent.length > 0) {
                window.parent.location.href = "<%=basePath%>oliveLogin.jsp";
            }


        }

    </script>
</head>

<body class="login-layout"  onload="checkParent();">
<div class="main-container " >
    <div class="main-content">
        <div class="row  center-vertical">
            <div class="col-sm-10 col-sm-offset-1">
                <div class="login-container">
                    <div class="center">
                        <h2>
                            <img src="images/icon/casesoft-white.png" style="width:25px;height:25px;margin-top:-7px;"/>
                            <span class="red">RFID-SMART</span>
                            <span class="white" id="id-text2">大数据平台</span>
                        </h2>
                        <h4 class="blue" id="id-company-text">HUB 4.0 &copy; 凯施智联</h4>
                    </div>

                    <div class="space-6"></div>
                    <div class="position-relative">
                        <div id="login-box" class="login-box visible widget-box no-border">
                            <div class="widget-body">
                                <div class="widget-main">
                                    <h4 class="header blue lighter bigger">
                                        <i class="ace-icon fa fa-coffee green"></i>
                                        用户登录
                                    </h4>

                                    <div class="space-6"></div>

                                    <form id="loginForm" name="loginForm" action="<%=basePath%>sys/user/loginOrCode.do?loginType=SU" method="post" onsubmit="return check();">
                                        <fieldset>
                                            <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" name="code" id="code" class="form-control" placeholder="编号" autofocus/>
															<i class="ace-icon fa fa-user"></i>
														</span>
                                            </label>

                                            <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="password" name="password"  id="password" class="form-control" placeholder="密码" />
															<i class="ace-icon fa fa-lock"></i>
														</span>
                                            </label>

                                            <div class="space"></div>

                                            <div class="clearfix">
                                                <label class="inline">
                                                    <input id="ck_remMe" type="checkbox" class="ace" />
                                                    <span class="lbl"> 记住我</span>
                                                </label>

                                                <button type="submit" class="width-35 pull-right btn btn-sm btn-primary">
                                                    <i class="ace-icon fa fa-key"></i>
                                                    <span class="bigger-110">确定</span>
                                                </button>
                                            </div>

                                            <div class="space-4"></div>
                                        </fieldset>
                                    </form>
                                    <div id="messagePanel" style="display:none">
                                        <span class="label label-important">错误</span>
                                        <label id="message"><%=message %></label>
                                    </div>

                                    <div class="social-or-login center">
                                        <span class="bigger-110">支持浏览器</span>
                                    </div>

                                    <div class="space-6"></div>


                                    <div class="social-login center">
                                        <a class="btn btn-primary" href="https://www.google.com/intl/zh-CN/chrome/browser/" target="_blank">
                                            <i class="ace-icon fa fa-chrome"></i>
                                        </a>

                                        <a class="btn btn-info" href="http://firefox.com.cn/download/" target="_blank">
                                            <i class="ace-icon fa fa-firefox"></i>
                                        </a>

                                        <a class="btn btn-danger" href="http://windows.microsoft.com/zh-CN/internet-explorer/products/ie/home" target="_blank">
                                            <i class="ace-icon fa fa-internet-explorer"></i>
                                        </a>
                                    </div>
                                </div><!-- /.widget-main -->


                            </div><!-- /.widget-body -->
                        </div><!-- /.login-box -->

                    </div><!-- /.position-relative -->


                </div>
            </div><!-- /.col -->
        </div><!-- /.row -->
    </div><!-- /.main-content -->
</div><!-- /.main-container -->

<!-- basic scripts -->

<!--[if !IE]> -->
<script type="text/javascript">
    window.jQuery || document.write("<script src='<%=basePath%>Olive/assets/js/jquery.js'>"+"<"+"/script>");
</script>

<!-- <![endif]-->

<!--[if IE]>
<script type="text/javascript">
    window.jQuery || document.write("<script src='<%=basePath%>Olive/assets/js/jquery1x.js'>"+"<"+"/script>");
</script>
<![endif]-->
<script type="text/javascript">
    if('ontouchstart' in document.documentElement) document.write("<script src='<%=basePath%>Olive/assets/js/jquery.mobile.custom.js'>"+"<"+"/script>");
</script>
<script src="<%=basePath%>Olive/plugin/jquery.cookie.js"></script>
<!-- inline scripts related to this page -->
<script type="text/javascript">
    jQuery(function($) {

        if ($.cookie("rmbUser") == "true") {
            $("#ck_remMe").attr("checked", true);
            $("#code").val($.cookie("code"));
            $("#password").val($.cookie("password"));
        }


        $(document).on('click', '.toolbar a[data-target]', function(e) {
            e.preventDefault();
            var target = $(this).data('target');
            $('.widget-box.visible').removeClass('visible');//hide others
            $(target).addClass('visible');//show target
        });
    });


</script>
</body>
</html>
