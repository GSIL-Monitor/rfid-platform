<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/8/15
  Time: 下午 1:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<html>
<!DOCTYPE html>
<head>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>
    <script src="<%=basePath%>/Olive/assets/js/jquery.js"></script>
    <script src="<%=basePath%>/Olive/assets/js/bootstrap.js"></script>
    <script src="<%=basePath%>/Olive/assets/js/bootbox.js"></script>
    <link rel="stylesheet" href="<%=basePath%>/Olive/assets/css/ace.css"/>
    <link rel="stylesheet" href="<%=basePath%>/Olive/assets/css/bootstrap-multiselect.css">
    <link rel="stylesheet" href="<%=basePath%>/Olive/assets/css/datepicker.css">
    <link rel="stylesheet" href="<%=basePath%>/Olive/assets/css/bootstrap.css">
    <link rel="stylesheet" href="<%=basePath%>/Olive/assets/css/jquery-ui.custom.css">
    <style>
        .btn1 {
            width:50%;
            height:90%;
            background:url('<%=basePath%>/images/pad/ordinary.png')  no-repeat left top;
            background-size:contain;
            border:none;
            color:#0a0a0a;
        }
        .btn2 {
            width:50%;
            height:90%;
            background:url('<%=basePath%>/images/pad/vip.png')  no-repeat left top;
            background-size:contain;
            border:none;
            color:#0a0a0a;
        }
        .input {
            width:50%;
            height:20%;
            background-size:contain;
            border:none;
            color:#0a0a0a;
        }
        .row3{
            height: 50%;
        }
        .row1{
            height: 30%;
        }
        .center-vertical {
            position:relative;
            top:50%;
            transform:translateY(-50%)
        }
        .center-vertical-bottom {
            position:relative;
            top:70%;
            transform:translateY(-15%)
        }
    </style>

</head>
<body style="height: 100% ; width: 100%">
  <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
     <div class="center">
         <div class="panel panel-default left-panel">
             <div class="panel-body">
                 <div class="widget-body">
                     <div class=" row3 col-lg-6 col-md-6 col-sm-6" style="text-align:center">
                         <button class="btn1  center-vertical" id="ordinary" type="button"></button>
                     </div>
                     <div class=" row3 col-lg-6 col-md-6 col-sm-6" style="text-align:center">
                         <button class="btn2  center-vertical" id="vip" type="button" ></button>
                     </div>
                     <div class="row1 col-lg-4 col-md-4 col-sm-4">
                         <label class="col-lg-3 col-md-3 col-sm-3 center-vertical-bottom text-info" for="search_origId">默认仓库:</label>
                         <div class="col-lg-9 col-md-9 col-sm-9 center-vertical-bottom">
                             <input class="form-control" id="search_origId" value="">
                             </input>
                         </div>
                     </div>
                     <div class="row1 col-lg-4 col-md-4 col-sm-4">
                         <label class="col-lg-3 col-md-3 col-sm-3 center-vertical-bottom text-info"  for="search_unitName">默认店铺:</label>
                         <div class="col-lg-9 col-md-9 col-sm-9 center-vertical-bottom">
                             <input class="form-control"   id="search_unitName">
                             </input>
                         </div>
                     </div>
                     <div class="row1 col-lg-4 col-md-4 col-sm-4">
                         <label class="col-lg-3 col-md-3 col-sm-3 center-vertical-bottom text-info" for="search_origId">销售员:</label>
                         <div class="col-lg-9 col-md-9 col-sm-9 center-vertical-bottom">
                             <select class="form-control selectpicker show-tick" id="search_busnissId"
                                     name="busnissId"
                                     style="width: 100%;" data-live-search="true">
                             </select>
                         </div>
                     </div>
                 </div>
             </div>
         </div>
     </div>
  </div>
<script>
    $(function () {
        //会员客户和普通客户的正方形
        var vipWidth=$("#vip").width();
        var vipHeight=$("#vip").height();
        if(vipWidth<=vipHeight) {
            $("#vip").css("height", vipWidth);
            $("#vip").css("width", vipWidth);
        }else{
            $("#vip").css("height", vipHeight);
            $("#vip").css("width", vipHeight);
        }
        var vipWidth=$("#ordinary").width();
        var vipHeight=$("#ordinary").height();
        if(vipWidth<=vipHeight) {
            $("#ordinary").css("height", vipWidth);
            $("#ordinary").css("width", vipWidth);
        }else{
            $("#ordinary").css("height", vipHeight);
            $("#ordinary").css("width", vipHeight);
        }
        findOrigIdByUser();
    });
  function findOrigIdByUser() {
      $.ajax({
          dataType: "json",
          url: basePath + "/api/hub/base/getDefaultwarehouseWs.do",
          cache: false,
          async: false,
          data:{
              userId:"admin"
          },
          type: "POST",
          success: function (msg) {
              if(msg.success){
                  localStorage.setItem("defaultSaleStaffId", msg.result.defaultSaleStaffId);
                  localStorage.setItem("defalutCustomerId", msg.result.defalutCustomerId);
                  localStorage.setItem("owingValue", msg.result.owingValue);
                  localStorage.setItem("defaultWarehId",msg.result.defaultWarehId);
                  localStorage.setItem("unitName",msg.result.name);
                  localStorage.setItem("ownerId",msg.result.ownerId);
                  $("#search_origId").val(msg.result.defaultWarehouseName);
                  $("#search_origId").attr("readOnly",true);
                  $("#search_unitName").val(msg.result.name);
                  $("#search_unitName").attr("readOnly",true);
                  findBusinessName(msg.result.ownerId)
              }
          }
      });
  }
  function findBusinessName(ownerId) {
      var url;
      if (ownerId == "1") {
          url = basePath + "/sys/user/listWS.do?filter_EQI_type=4";
      } else {
          url = basePath + "/sys/user/listWS.do?filter_EQI_type=4&filter_EQS_ownerId=" + ownerId;
      }
      $.ajax({
          url: url,
          cache: false,
          async: false,
          type: "POST",
          success: function (data, textStatus) {

              $("#search_busnissId").empty();
              $("#search_busnissId").append("<option value='' >--请选择销售员--</option>");
              var json = data;
              for (var i = 0; i < json.length; i++) {
                  $("#search_busnissId").append("<option value='" + json[i].id + "'>" + json[i].name + "</option>");
              }

              var defaultSaleStaffId=localStorage.getItem("defaultSaleStaffId");
              console.log(defaultSaleStaffId);
              if (defaultSaleStaffId != "" && defaultSaleStaffId != undefined&&defaultSaleStaffId != "undefined") {
                  $("#search_busnissId").val(defaultSaleStaffId);
              }

          }
      });

  }
</script>
</body>
</html>
