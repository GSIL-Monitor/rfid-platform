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

<div id="edit-dialog2" style="text-align: center;font-size:12px; display: none">

    <table style="text-align:center;font-size:10px;" border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
        <thead >
        <tr >
            <th align="left"  nowrap="nowrap" style="border:0px;font-size:10px;">商品</th>
            <th align="right" nowrap="nowrap" style="border:0px;font-size:10px;">数量</th>
            <th align="right" nowrap="nowrap"style="border:0px;font-size:10px;">原价</th>
            <th  align="right" nowrap="nowrap" style="border:0px;font-size:10px;">折后价</th>
            <th align="right" nowrap="nowrap" style="border:0px;font-size:10px;">金额</th>
        </tr>
        </thead>
        <tbody id="loadtab">
        <tr style="border-top:1px dashed black;padding-top:5px;">
            <td align="left" style="border-top:1px dashed black;padding-top:5px;">合计:</td>
            <td align="right"style="border-top:1px dashed black;padding-top:5px;">0</td>
            <td style="border-top:1px dashed black;padding-top:5px;">&nbsp;</td>
            <td style="border-top:1px dashed black;padding-top:5px;">&nbsp;</td>
            <td align="right" style="border-top:1px dashed black;padding-top:5px;">0</td>
        </tr>
        </tbody>
    </table>



</div>
<div id="edit-dialog-TR" style="text-align: center;font-size:12px; display: none">

    <table style="text-align:center;font-size:10px;" border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
        <thead >
        <tr >
            <th align="left"  nowrap="nowrap" style="border:0px;font-size:10px;">商品</th>
            <th align="right" nowrap="nowrap" style="border:0px;font-size:10px;">数量</th>
            <th align="right" nowrap="nowrap"style="border:0px;font-size:10px;">原价</th>
        </tr>
        </thead>
        <tbody id="loadtabTR">
        <tr style="border-top:1px dashed black;padding-top:5px;">
            <td align="left" style="border-top:1px dashed black;padding-top:5px;">合计:</td>
            <td align="right"style="border-top:1px dashed black;padding-top:5px;">0</td>
            <td style="border-top:1px dashed black;padding-top:5px;">&nbsp;</td>
            <td style="border-top:1px dashed black;padding-top:5px;">&nbsp;</td>
            <td align="right" style="border-top:1px dashed black;padding-top:5px;">0</td>
        </tr>
        </tbody>
    </table>



</div>
<script>

</script>
