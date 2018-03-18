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
    <link href="<%=basePath%>/Olive/assets/css/fullcalendar.css" rel="stylesheet" />
    <link href="<%=basePath%>/Olive/assets/css/fullcalendar.print.css" rel="stylesheet" media="print" />
    <style>
        body {
            margin-top: 40px;
            text-align: center;
            font-size: 14px;
            font-family: "Lucida Grande",Helvetica,Arial,Verdana,sans-serif;
        }
        .calendarWrapper {
            width: 1190px;
            margin: 0 auto 15px;
        }
        #calendar {
            width: 885px;
            background: #fff;
            padding: 15px 10px;
        }
        .calendarWrapper .rightSidePanel {
            width: 240px;
            padding: 0px 15px;
        }
        .dib{display: inline-block;}
        .fr{float: right;}
        .fc-day-cnDate {
            margin-top: 12px;
        }
        .fc-day-cnTerm{
            margin-top: 12px;
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
        <div id="msgTopTipWrapper" style="display:none">
            <div id="msgTopTip">
                <span><i class="iconTip"></i>正在载入日历数据...</span>
            </div>
        </div>
        <div class="calendarWrapper">
            <div class="rightSidePanel mb50 fr">
                <div id="div_day_detail" class="h_calendar_alm">
                    <div class="alm_date"></div>
                    <div class="alm_content nofestival">
                        <div class="today_icon"></div>
                        <div class="today_date"></div>
                        <p id="alm_cnD"></p>
                        <p id="alm_cnY"></p>
                        <p id="alm_cnA"></p>
                        <div class="alm_lunar_date"></div>
                    </div>
                </div>
            </div>
            <div id="calendar" class="dib"></div>
        </div>
    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>

</div>

<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<script src="<%=basePath%>/Olive/assets/js/fullcalendar.js" ></script>

<script type="text/javascript" src="<%=basePath%>/views/factory/factoryWorkCalendarController.js"></script>

</body>
</html>