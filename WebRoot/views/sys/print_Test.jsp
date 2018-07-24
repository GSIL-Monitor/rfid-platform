<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%--<style type="text/css">
    body {text-align: center;font-size:12px;}
    .userCustomCss{
    }
    .userCustomCss td,th{
    }
    .orderTable {border:0px;}
    .orderTable  td {border:0px;height: 15px;}
    .orderTable  th {border:0px;height: 20px;}
    .orderTable .endTr td {border-top:1px dashed black;padding-top:5px;}
</style>--%>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<script type="text/javascript">
    var basePath = "<%=basePath%>";
</script>
<div id="edit-dialogSanLian" style="text-align: center;font-size:12px;display: none">

    <table style="text-align:center;font-size:10px;table-layout:fixed;" border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
        <thead >
        <tr>
          <th width="100%" colspan="25" style="font-size:25px;padding-top:5px">香港顺子批发时装批发销售单</th>
        </tr>
        <tr>
            <th width="100%" colspan="7" style="height:50px;font-size:15px;padding-top:5px">单号：SO180716091930331</th>
            <th width="100%" colspan="6" style="height:50px;font-size:15px;padding-top:5px">客户：温州晨晨</th>
            <th width="100%" colspan="6" style="height:50px;font-size:15px;padding-top:5px">营业员:小红</th>
            <th width="100%" colspan="6" style="height:50px;font-size:15px;padding-top:5px">日期:2018-07-16</th>
        </tr>
        <tr id="loadtabthA4" width="100%">
            <th align="middle" colspan="3" nowrap="nowrap" style="height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;">款号</th>
            <th align="middle" colspan="4"nowrap="nowrap" style="height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;">款名</th>
            <th align="middle" colspan="3" nowrap="nowrap"style="height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;">颜色</th>
            <th align="middle" colspan="1"nowrap="nowrap" style="height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;">S</th>
            <th align="middle" colspan="1" nowrap="nowrap" style="height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;">M</th>
            <th align="middle" colspan="1" nowrap="nowrap" style="height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;">L</th>
            <th align="middle" colspan="1" nowrap="nowrap" style="height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;">XL</th>
            <th align="middle" colspan="1" nowrap="nowrap" style="height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;">2XL</th>
            <th align="middle" colspan="1" nowrap="nowrap" style="height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;">3XL</th>
            <th align="middle" colspan="1" nowrap="nowrap" style="height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;">4XL</th>
            <th align="middle" colspan="1" nowrap="nowrap" style="height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;">5XL</th>
            <th align="middle" colspan="2" nowrap="nowrap" style="height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;">数量</th>
            <th align="middle" colspan="2" nowrap="nowrap" style="height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;">单价</th>
            <th align="middle" colspan="2" nowrap="nowrap" style="height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;">金额</th>
            <th align="middle" colspan="2" nowrap="nowrap" style="height:30px;border:0px;font-size:10px;border:1px solid #000;word-wrap:break-word;">备注</th>
        </tr>
        </thead>
        <tbody id="loadtabA4">
        <tr style="border-top:1px ;padding-top:5px;">
            <td align='middle' colspan="3" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="3" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="3"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
        </tr>
        <tr style="border-top:1px ;padding-top:5px;">
            <td align='middle' colspan="3" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="4" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="3"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
        </tr>
        <tr style="border-top:1px ;padding-top:5px;">
            <td align='middle' colspan="3" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="4" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="3"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
        </tr>
        <tr style="border-top:1px ;padding-top:5px;">
            <td align='middle' colspan="3" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="4" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="3"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
        </tr>
        <tr style="border-top:1px ;padding-top:5px;">
            <td align='middle' colspan="3" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="4" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="3"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
        </tr>
        <tr style="border-top:1px ;padding-top:5px;">
            <td align='middle' colspan="3" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="4" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="3"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
        </tr>
        <tr style="border-top:1px ;padding-top:5px;">
            <td align='middle' colspan="3" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="4" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="3"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
        </tr>
        <tr style="border-top:1px ;padding-top:5px;">
            <td align='middle' colspan="3" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="4" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="3"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
        </tr>
        <tr style="border-top:1px ;padding-top:5px;">
            <td align='middle' colspan="3" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="4" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="3"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
        </tr>
        <tr style="border-top:1px ;padding-top:5px;">
            <td align='middle' colspan="3" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="4" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="3"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
        </tr>
        <tr style="border-top:1px ;padding-top:5px;">
            <td align='middle' colspan="3" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="4" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="3"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
        </tr>
        <tr style="border-top:1px ;padding-top:5px;">
            <td align='middle' colspan="3" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="4" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="3"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
        </tr>
        <tr style="border-top:1px ;padding-top:5px;">
            <td align='middle' colspan="3" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="4" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="3"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
        </tr>
        <tr style="border-top:1px ;padding-top:5px;">
            <td align='middle' colspan="3" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="4" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="3"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
        </tr>
        <tr style="border-top:1px ;padding-top:5px;">
            <td align='middle' colspan="3" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="4" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="3"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
        </tr>
        <tr style="border-top:1px ;padding-top:5px;">
            <td align='middle' colspan="3" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="4" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="3"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
        </tr>
        <tr style="border-top:1px ;padding-top:5px;">
            <td align='middle' colspan="3" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="4" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="3"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>1</td>
            <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
        </tr>
        </tbody>
        <tfoot>
          <tr>
              <td align='middle' colspan="3" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>本页合计</td>
              <td align='middle' colspan="4" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'></td>
              <td align='middle' colspan="3"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'></td>
              <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'><font color="black" tdata="SubSum" format="#">##</font></td>
              <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'><font color="black" tdata="SubSum" format="#">##</font></td>
              <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'><font color="black" tdata="SubSum" format="#">##</font></td>
              <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'><font color="black" tdata="SubSum" format="#">##</font></td>
              <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'><font color="black" tdata="SubSum" format="#">##</font></td>
              <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'><font color="black" tdata="SubSum" format="#">##</font></td>
              <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'><font color="black" tdata="SubSum" format="#">##</font></td>
              <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'><font color="black" tdata="SubSum" format="#">##</font></td>
              <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'><font color="black" tdata="SubSum" format="#">##</font></td>
              <td align='middle' colspan="2"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'></td>
              <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'><font color="black" tdata="SubSum" format="#.00">##</font></td>
              <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
          </tr>
          <tr>
              <td align='middle' colspan="3" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>单据合计</td>
              <td align='middle' colspan="4" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'></td>
              <td align='middle' colspan="3"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'></td>
              <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'><font color="black" tdata="AllSum" format="#">##</font></td>
              <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'><font color="black" tdata="AllSum" format="#">##</font></td>
              <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'><font color="black" tdata="AllSum" format="#">##</font></td>
              <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'><font color="black" tdata="AllSum" format="#">##</font></td>
              <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'><font color="black" tdata="AllSum" format="#">##</font></td>
              <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'><font color="black" tdata="AllSum" format="#">##</font></td>
              <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'><font color="black" tdata="AllSum" format="#">##</font></td>
              <td align='middle' colspan="1" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'><font color="black" tdata="AllSum" format="#">##</font></td>
              <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'><font color="black" tdata="AllSum" format="#">##</font></td>
              <td align='middle' colspan="2"style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'></td>
              <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'><font color="black" tdata="AllSum" format="#.00">##</font></td>
              <td align='middle' colspan="2" style='word-wrap:break-word;border-top:1px ;padding-top:5px;border:1px solid #000;font-size:12px;'>asd</td>
          </tr>
          <tr>
              <th colspan="4" align='left' style="font-size:20px;padding-top:5px;word-wrap:break-word">本单额200</th>
              <th colspan="4" align='left' style="font-size:20px;padding-top:5px;word-wrap:break-word">本单欠200</th>
              <th colspan="4" align='left'style="font-size:20px;padding-top:5px;word-wrap:break-word">上次欠200</th>
              <th colspan="4" align='left' style="font-size:20px;padding-top:5px;word-wrap:break-word">累计欠200</th>
          </tr>
          <tr>
              <th colspan="20" align='left' style="font-size:20px;padding-top:5px;word-wrap:break-word">备注:余红</th>
              <th colspan="5"  align='left' style="font-size:20px;padding-top:5px;word-wrap:break-word">经办人:管理员</th>
          </tr>
          <tr>
              <th colspan="12" align='left' style="font-size:20px;padding-top:5px;word-wrap:break-word">电话:</th>
              <th colspan="13" align='left' style="font-size:20px;padding-top:5px;word-wrap:break-word">手机:15768734210</th>
          </tr>
          <tr>
              <th colspan="25" align='left' style="font-size:20px;padding-top:5px;word-wrap:break-word">地址:深圳市南山区南油第二工业区天安6座625</th>
          </tr>
          <tr>
              <th colspan="12" align='left' style="font-size:20px;padding-top:5px;word-wrap:break-word">张阿静 交行 6222 6213 1001 6553 303</th>
              <th colspan="13" align='left' style="font-size:20px;padding-top:5px;word-wrap:break-word">张阿静 农行 6228 4501 2801 9808 877</th>
          </tr>
          <tr>
              <th colspan="25" align='left' style="font-size:20px;padding-top:5px;word-wrap:break-word">提醒:欠款，件数清单面点清，离店不负责</th>
          </tr>
          <tr>
              <th colspan="18" align='middle' style="font-size:20px;padding-top:5px;word-wrap:break-word">2018-07-16</th>
              <th colspan="7" align='middle' style="font-size:20px;padding-top:5px;word-wrap:break-word">第<font tdata="PageNO" format="0" color="blue">##</font>页</span>/共<font tdata="PageCount" format="0" color="blue">##</font></span>页</th>
          </tr>
        </tfoot>
    </table>

<div>
    <button value="sdad" onclick="Test()"></button>
</div>

</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<script src="<%=basePath%>/Olive/plugin/print/LodopFuncs.js"></script>
<script>
       function Test() {
           var LODOP = getLodop();
           LODOP.ADD_PRINT_TABLE(100,1,780,250,document.getElementById("edit-dialogA4").innerHTML);
           LODOP.PREVIEW();
       }
</script>