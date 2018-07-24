<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017-06-20
  Time: 下午 3:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName()+":"+request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../baseView.jsp"></jsp:include>
    <script>
        var basePath = "<%=basePath%>";
        var groupid="${groupid}";
        var address="${address}";
    </script>
    <style>
        body {
            background-color: #ffffff;
            min-height: 100%;
            padding-bottom: 0;
            font-family: 'Open Sans';
            font-size: 13px;
            color: #393939;
            line-height: 1.5;
        }
        .stecs i {
            display: inline-block;
            width: 13px;
            height: 13px;
            border: 1px solid #19B394;
            margin-right: 5px;
            vertical-align: middle;
            position: relative;
            cursor: pointer;

        }
        .stecs.on i{
            height: 13px;
            background: #19B394;
        }
        .headTitle{
            width: 100%;
            position: relative;
            overflow: hidden;
            border: 1px solid #EAECED;
            padding: 18px;
        }
        .headTitle>h3{
            width: 100%;
            padding: 10px 0 20px 0;
            border-bottom: 1px solid #EAECED;
            overflow: hidden;
        }
        .headTitleDiv{
            width: 100%;
            position: relative;
            overflow: hidden;
            border: 1px solid #EAECED;
            padding: 18px;
        }
        .headTitleLi{
            width: 100%;
            position: relative;
            clear: both;
            margin-bottom: 18px;
            display: inline-block;
            margin-left: 8px;
        }
        .Print-Bg-Top {
            width: 100%;
            height: auto;
            overflow:hidden;
            background-image: url(<%=basePath%>/images/print/SmallTicketTop.png);
            background-position: center top;
            background-repeat: no-repeat;
            background-size:100%;
            padding: 100px 40px;
            padding-bottom: 0px;
        }
        .Print-Bg-Center {
            background-repeat: repeat-y;
            background-image: url(<%=basePath%>/images/print/SmallTicketCenter.png);
            background-position: center;
            background-size:100% 100%;
            min-height: 180px;
            display: inline-block;
            float: left;
        }
        .Print-Bg-Top-div{
            margin-left: 30px;
        }
        .Print-Bg{
            width:100%;
            height: 40px;
            background-image: url(<%=basePath%>/images/print/SmallTicketBottom.png);
            background-position: center bottom;
            background-size:100% 100%;
            background-repeat: no-repeat;
        }



    </style>
</head>
<body class="no-skin">
<div class="main-container" id="main-container">
    <div class="row">
        <div id="receiptPrint">
            <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12" style="margin-top: 20px;margin-left: 50px">
                <div class="col-xs-3 col-sm-3 col-md-3	col-lg-3">
                    <div class="Print-Bg-Center">
                        <div class="Print-Bg-Top">
                            <div id="printTop">
                                <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div">
                                    <div class="col-xs-8 col-sm-8 col-md-8	col-lg-8" id="storeName">
                                        <span class="col-xs-12 col-sm-12 col-md-12	col-lg-12" style="font-weight:bold;font-size:20px;display:table-cell;vertical-align:middle;text-align:center">店铺名称</span>
                                    </div>
                                    <%-- <div class="col-xs-8 col-sm-8 col-md-8	col-lg-8" id="billType">
                                         <span >销售单</span>
                                     </div>--%>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div">
                                    <div class="col-xs-8 col-sm-8 col-md-8	col-lg-8" id="billType">
                                        <span class="col-xs-12 col-sm-12 col-md-12	col-lg-12" style="font-weight:bold;font-size:20px;display:table-cell;vertical-align:middle;text-align:center">单据类型</span>
                                    </div>
                                    <%-- <div class="col-xs-8 col-sm-8 col-md-8	col-lg-8" id="billType">
                                         <span >销售单</span>
                                     </div>--%>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div" id="billNo" >
                                    <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                                        <span>单号:</span>
                                    </div>
                                    <div class="col-xs-8 col-sm-8 col-md-8	col-lg-8">
                                        <span>xxxxxx</span>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div" id="weChat" >
                                    <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                                        <span>微信:</span>
                                    </div>
                                    <div class="col-xs-8 col-sm-8 col-md-8	col-lg-8">
                                        <span>xxxxxx</span>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div" id="makeBill">
                                    <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                                        <span>制单人:</span>
                                    </div>
                                    <div class="col-xs-8 col-sm-8 col-md-8	col-lg-8">
                                        <span>xxxxxx</span>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div" id="billDate">
                                    <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                                        <span>日期:</span>
                                    </div>
                                    <div class="col-xs-8 col-sm-8 col-md-8	col-lg-8">
                                        <span>xxxxxx</span>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div" id="coustmer">
                                    <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                                        <span>客户:</span>
                                    </div>
                                    <div class="col-xs-8 col-sm-8 col-md-8	col-lg-8">
                                        <span>xxxxxx</span>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div" id="VIPNumber">
                                    <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                                        <span>会员卡号:</span>
                                    </div>
                                    <div class="col-xs-8 col-sm-8 col-md-8	col-lg-8">
                                        <span>xxxxxx</span>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div" id="remark">
                                    <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                                        <span>备注:</span>
                                    </div>
                                    <div class="col-xs-8 col-sm-8 col-md-8	col-lg-8">
                                        <span>xxxxxx</span>
                                    </div>
                                </div>
                            </div>

                            <div id="edit-dialog" style="text-align: center ;font-size:12px;" class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div">

                                <table style="text-align: center;font-size:12px;"class="col-xs-8 col-sm-8 col-md-8	col-lg-8">
                                    <thead style="text-align:center" border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
                                    <tr >
                                        <th align="left"  nowrap="nowrap" style="border:0px;height: 20px;width: 20%">商品</th>
                                        <th align="right" nowrap="nowrap" style="border:0px;height: 20px;width: 20%">数量</th>
                                        <th align="right" nowrap="nowrap"style="border:0px;height: 20px;width: 20%">原价</th>
                                        <th  align="right" nowrap="nowrap" style="border:0px;height: 20px;width: 20%">折后价</th>
                                        <th align="right" nowrap="nowrap" style="border:0px;height: 20px;width: 20%">金额</th>
                                    </tr>
                                    </thead>
                                    <tbody>
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
                            <div id="printFoot">
                                <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div" id="businessId">
                                    <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                                        <span>销售员:</span>
                                    </div>
                                    <div class="col-xs-8 col-sm-8 col-md-8	col-lg-8">
                                        <span>xxxxxx</span>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div" id="printTime">
                                    <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                                        <span>打印时间:</span>
                                    </div>
                                    <div class="col-xs-8 col-sm-8 col-md-8	col-lg-8">
                                        <span>xxxxxx</span>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div" id="shopBefore">
                                    <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                                        <span>售前余额:</span>
                                    </div>
                                    <div class="col-xs-8 col-sm-8 col-md-8	col-lg-8">
                                        <span>xxxxxx</span>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div" id="shopAfter">
                                    <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                                        <span>售后余额:</span>
                                    </div>
                                    <div class="col-xs-8 col-sm-8 col-md-8	col-lg-8">
                                        <span>xxxxxx</span>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div" id="integral">
                                    <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                                        <span>积分(本次/剩余):</span>
                                    </div>
                                    <div class="col-xs-8 col-sm-8 col-md-8	col-lg-8">
                                        <span>100/2000</span>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div" id="footExtend">
                                    <div class="col-xs-10 col-sm-10 col-md-10	col-lg-10">
                                        <span >欢迎来到Ancient Stone</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="Print-Bg"></div>
                    </div>

                </div>
                <div class="col-xs-8 col-sm-8 col-md-8	col-lg-8">
                    <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div" id="ruleReceipt">
                        <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2">
                            <span style="text-align:center;display: block">小票规格:</span>
                        </div>
                        <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2">
                            <ul class="stecs" data-name="58" onclick="selectRuleReceipt(58);">
                                <i></i>
                                <span>58mm</span>
                            </ul>
                        </div>
                        <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2">
                            <ul class="stecs" data-name="80" onclick="selectRuleReceipt(80);">
                                <i></i>
                                <span>80mm</span>
                            </ul>
                        </div>
                        <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2">
                            <ul class="stecs" data-name="110" onclick="selectRuleReceipt(110);">
                                <i></i>
                                <span>110mm</span>
                            </ul>
                        </div>
                        <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2" id="isshowA4">
                            <ul class="stecs" data-name="A4" onclick="selectRuleReceipt('A4');">
                                <i></i>
                                <span>A4</span>
                            </ul>
                        </div>
                        <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2" id="isshowA4N0Size">
                            <ul class="stecs" data-name="A4N0Size" onclick="selectRuleReceipt('A4N0Size');">
                                <i></i>
                                <span>A4(对货)</span>
                            </ul>
                        </div>
                        <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2" id="isshowSanLian">
                            <ul class="stecs" data-name="SanLian" onclick="selectRuleReceipt('SanLian');">
                                <i></i>
                                <span>三联</span>
                            </ul>
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div">
                        <div class="col-xs-10 col-sm-10 col-md-10	col-lg-10">
                            <div class="headTitle">
                                <input type="text" id="id" style="display: none"/>
                                <input type="text" id="ownerId" style="display: none"/>
                                <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                                    <span>小票名称:</span>
                                    <input type="text" id="receiptName"/>
                                </div>
                                <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                                    <span>小票类型:</span>
                                    <select id="receiptType" onchange="receiptTypeSelect()">
                                        <option value="SO">销售单据</option>
                                        <option value="PI">采购单据</option>
                                        <option value="PR">采购退货</option>
                                        <option value="SR">销售退货</option>
                                        <option value="TR">调拨单</option>
                                    </select>
                                </div>
                                <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                                    <span>公共类型:</span>
                                    <select id="commonType">
                                        <option value="1">否</option>
                                        <option value="0">是</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div">
                        <div class="col-xs-10 col-sm-10 col-md-10	col-lg-10">
                            <div class="headTitle">
                                <h3 onclick="selectheadPrint()">
                                    <span style="font-size: 14px;">头部打印</span>
                                </h3>
                                <div class="headTitleDiv" id="headPrint">
                                    <ul>
                                        <li class="headTitleLi">
                                            <div class="stecs on" data-name="storeName" onclick="selectThis(this,'storeName')">
                                                <i></i>
                                                <span>门店名称</span>
                                            </div>
                                        </li>
                                        <li class="headTitleLi">
                                            <div class="stecs on" data-name="billType" onclick="selectThis(this,'billType')">
                                                <i></i>
                                                <span>小票类型</span>
                                            </div>
                                        </li>
                                        <li class="headTitleLi">
                                            <div class="stecs on" data-name="billNo" onclick="selectThis(this,'billNo')">
                                                <i></i>
                                                <span>单号</span>
                                            </div>
                                        </li>
                                        <li class="headTitleLi">
                                            <div class="stecs on" data-name="weChat" onclick="selectThis(this,'weChat')">
                                                <i></i>
                                                <span>微信</span>
                                            </div>
                                        </li>
                                        <li class="headTitleLi">
                                            <div class="stecs on" data-name="makeBill" onclick="selectThis(this,'makeBill')">
                                                <i></i>
                                                <span>制单人</span>
                                            </div>
                                        </li>
                                        <li class="headTitleLi">
                                            <div class="stecs on" data-name="billDate" onclick="selectThis(this,'billDate')">
                                                <i></i>
                                                <span>日期</span>
                                            </div>
                                        </li>
                                        <li class="headTitleLi">
                                            <div class="stecs on" data-name="billDate" onclick="selectThis(this,'coustmer')">
                                                <i></i>
                                                <span>客户</span>
                                            </div>
                                        </li>
                                        <li class="headTitleLi">
                                            <div class="stecs on" data-name="VIPNumber" onclick="selectThis(this,'VIPNumber')">
                                                <i></i>
                                                <span>会员卡号</span>
                                            </div>
                                        </li>
                                        <li class="headTitleLi">
                                            <div class="stecs on" data-name="remark" onclick="selectThis(this,'remark')">
                                                <i></i>
                                                <span>备注</span>
                                            </div>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div">
                        <div class="col-xs-10 col-sm-10 col-md-10	col-lg-10">
                            <div class="headTitle">
                                <h3  onclick="selectfoorerPrint()">
                                    <span style="font-size: 14px;">页脚打印</span>
                                </h3>
                                <div class="headTitleDiv" id="footerPrint" style="display: none">
                                    <ul>
                                        <li class="headTitleLi">
                                            <div class="stecs on" data-name="businessId" onclick="selectThis(this,'businessId')">
                                                <i></i>
                                                <span>销售员</span>
                                            </div>
                                        </li>
                                        <li class="headTitleLi">
                                            <div class="stecs on" data-name="printTime" onclick="selectThis(this,'printTime')">
                                                <i></i>
                                                <span>打印时间</span>
                                            </div>
                                        </li>
                                        <li class="headTitleLi">
                                            <div class="stecs on" data-name="shopBefore" onclick="selectThis(this,'shopBefore')">
                                                <i></i>
                                                <span>售前余额</span>
                                            </div>
                                        </li>
                                        <li class="headTitleLi">
                                            <div class="stecs on" data-name="shopAfter" onclick="selectThis(this,'shopAfter')">
                                                <i></i>
                                                <span>售后余额</span>
                                            </div>
                                        </li>
                                        <li class="headTitleLi">
                                            <div class="stecs on" data-name="integral" onclick="selectThis(this,'integral')">
                                                <i></i>
                                                <span>积分(本次/剩余)</span>
                                            </div>
                                        </li>
                                        <li class="headTitleLi">
                                            <span>扩展打印(在页脚展示扩展信息,换行请输入&lt;br&gt;)：</span>
                                            <br>
                                            <textarea class="col-xs-8 col-sm-8 col-md-8	col-lg-8" id="footExtendWrite" onkeyup="writeFootExtend(this)">欢迎来到Ancient Stone</textarea>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div">
                        <div class="col-xs-10 col-sm-10 col-md-10	col-lg-10">
                            <div class="headTitle">
                                <button id='save_guest_button' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='savePrint()'>
                                    <i class='ace-icon fa fa-save'></i>
                                    <span class='bigger-110'>保存</span>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="A4Print" style="display: none">
            <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12" style="margin-top: 20px;margin-left: 50px">
                <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">

                    <div class="headTitle">
                        <div id="printTopA4">
                            <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12">
                                <span class="col-xs-12 col-sm-12 col-md-12	col-lg-12" data-name="storeName" id="storeNameA4" style="font-weight:bold;font-size:20px;display:table-cell;vertical-align:middle;text-align:center">店铺名称</span>

                            </div>
                            <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12">
                                <span class="col-xs-12 col-sm-12 col-md-12	col-lg-12" data-name="billType" id="billTypeA4" style="font-weight:bold;font-size:20px;display:table-cell;vertical-align:middle;text-align:center">单据类型</span>
                            </div>
                            <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12" >
                                <span class="col-xs-6 col-sm-6 col-md-6 col-lg-6" data-name="coustmer" id="coustmerA4" style="display:table-cell;vertical-align:middle;text-align:center">客户:xxxxxxxxxxxx</span>
                                <span class="col-xs-6 col-sm-6 col-md-6 col-lg-6" data-name="billNo" id="billNoA4" style="display:table-cell;vertical-align:middle;text-align:center">单号:xxxxxxxxxxxx</span>
                                <span class="col-xs-6 col-sm-6 col-md-6 col-lg-6" data-name="billDate" id="billDateA4" style="display:table-cell;vertical-align:middle;text-align:center">日期:xxxxxxxxxxxx</span>
                                <span class="col-xs-6 col-sm-6 col-md-6 col-lg-6" data-name="makeBill" id="makeBillA4" style="display:table-cell;vertical-align:middle;text-align:center">制单人:xxxxxxxxxxxx</span>
                                <span class="col-xs-6 col-sm-6 col-md-6 col-lg-6" data-name="remark" id="remarkA4" style="display:table-cell;vertical-align:middle;text-align:center">备注:xxxxxxxxxxxx</span>
                            </div>
                            <%-- <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12" data-name="coustmer" id="coustmerA4">
                               <span class="col-xs-12 col-sm-12 col-md-12	col-lg-12" style="display:table-cell;vertical-align:middle;text-align:center">客户:xxxxxxxxxxxx</span>
                             </div>
                             <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12" data-name="billNo" id="billNoA4">
                                 <span class="col-xs-12 col-sm-12 col-md-12	col-lg-12" style="display:table-cell;vertical-align:middle;text-align:center">单号:xxxxxxxxxxxx</span>
                             </div>
                             <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12" data-name="billDate" id="billDateA4">
                                 <span class="col-xs-12 col-sm-12 col-md-12	col-lg-12" style="display:table-cell;vertical-align:middle;text-align:center">日期:xxxxxxxxxxxx</span>
                             </div>
                             <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12" data-name="makeBill" id="makeBillA4">
                                 <span class="col-xs-12 col-sm-12 col-md-12	col-lg-12" style="display:table-cell;vertical-align:middle;text-align:center">制单人:xxxxxxxxxxxx</span>
                             </div>
                             <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12" data-name="remark" id="remarkA4">
                                 <span class="col-xs-12 col-sm-12 col-md-12	col-lg-12" style="display:table-cell;vertical-align:middle;text-align:center">备注:xxxxxxxxxxxx</span>
                             </div>--%>
                        </div>
                        <div id="edit-A4-dialog" style="text-align: center ;font-size:12px;" class="col-xs-12 col-sm-12 col-md-12	col-lg-12">

                            <table style="text-align: center;font-size:12px;border-collapse:collapse;border:1px solid #000;"class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                <thead >
                                <tr style="border:1px solid #000;">
                                    <th align="left" class="styleIdA4" nowrap="nowrap" style="border:0px;font-size:10px;border:1px solid #000;">款号</th>
                                    <th align="left" class="styleNameA4"nowrap="nowrap" style="border:0px;font-size:10px;border:1px solid #000;">款名</th>
                                    <th align="left" class="colorIdA4" nowrap="nowrap"style="border:0px;font-size:10px;border:1px solid #000;">颜色</th>
                                    <th align="left" class="sizeIdA4" nowrap="nowrap" style="border:0px;font-size:10px;border:1px solid #000;">尺码</th>
                                    <th align="left" class="qtyA4" nowrap="nowrap" style="border:0px;font-size:10px;border:1px solid #000;">数量</th>
                                    <th align="left" class="priceA4" nowrap="nowrap" style="border:0px;font-size:10px;border:1px solid #000;">吊牌价</th>
                                    <th align="left" class="totPriceA4" nowrap="nowrap" style="border:0px;font-size:10px;border:1px solid #000;">金额</th>
                                    <th align="left" class="discountA4" nowrap="nowrap" style="border:0px;font-size:10px;border:1px solid #000;">折扣</th>
                                    <th align="left" class="actPriceA4" nowrap="nowrap" style="border:0px;font-size:10px;border:1px solid #000;">折后价</th>
                                    <th align="left" class="totActPriceA4" nowrap="nowrap" style="border:0px;font-size:10px;border:1px solid #000;">折后金额</th>
                                </tr>
                                </thead>
                                <tbody id="loadtabA4">
                                <tr style="border-top:1px ;padding-top:5px;border:1px solid #000;">
                                    <td align="left" class="styleIdA4" style="border-top:1px ;padding-top:5px;border:1px solid #000;">&nbsp;</td>
                                    <td align="left" class="styleNameA4" style="border-top:1px ;padding-top:5px;border:1px solid #000;">&nbsp;</td>
                                    <td align="left" class="colorIdA4" style="border-top:1px ;padding-top:5px;border:1px solid #000;">&nbsp;</td>
                                    <td align="left" class="sizeIdA4" style="border-top:1px ;padding-top:5px;border:1px solid #000;">&nbsp;</td>
                                    <td align="left" class="qtyA4" style="border-top:1px ;padding-top:5px;border:1px solid #000;">0</td>
                                    <td align="left" class="priceA4" style="border-top:1px ;padding-top:5px;border:1px solid #000;">&nbsp;</td>
                                    <td align="left" class="totPriceA4" style="border-top:1px ;padding-top:5px;border:1px solid #000;">&nbsp;</td>
                                    <td align="left" class="discountA4" style="border-top:1px ;padding-top:5px;border:1px solid #000;">&nbsp;</td>
                                    <td align="left" class="actPriceA4" style="border-top:1px ;padding-top:5px;border:1px solid #000;">&nbsp;</td>
                                    <td align="left" class="totActPriceA4" style="border-top:1px ;padding-top:5px;border:1px solid #000;">0</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div id="printFootA4">
                            <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12" >
                                <span class="col-xs-6 col-sm-6 col-md-6 col-lg-6" data-name="shopBefore" id="shopBeforeA4" style="display:table-cell;vertical-align:middle;text-align:center">售前余额:xxxxxxxxxxxx</span>
                                <span class="col-xs-6 col-sm-6 col-md-6 col-lg-6" data-name="shopAfter" id="shopAfterA4" style="display:table-cell;vertical-align:middle;text-align:center">售后余额:xxxxxxxxxxxx</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-xs-8 col-sm-8 col-md-8	col-lg-8">
                    <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div" id="ruleReceiptA4">
                        <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2">
                            <span style="text-align:center;display: block">小票规格:</span>
                        </div>
                        <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2">
                            <ul class="stecs" data-name="58" onclick="selectRuleReceiptA4(58);">
                                <i></i>
                                <span>58mm</span>
                            </ul>
                        </div>
                        <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2">
                            <ul class="stecs" data-name="80" onclick="selectRuleReceiptA4(80);">
                                <i></i>
                                <span>80mm</span>
                            </ul>
                        </div>
                        <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2">
                            <ul class="stecs" data-name="110" onclick="selectRuleReceiptA4(110);">
                                <i></i>
                                <span>110mm</span>
                            </ul>
                        </div>
                        <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2">
                            <ul class="stecs" data-name="A4" onclick="selectRuleReceiptA4('A4');">
                                <i></i>
                                <span>A4</span>
                            </ul>
                        </div>
                        <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2">
                            <ul class="stecs" data-name="A4N0Size" onclick="selectRuleReceiptA4('A4N0Size');">
                                <i></i>
                                <span>A4(对货)</span>
                            </ul>
                        </div>
                        <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2" >
                            <ul class="stecs" data-name="SanLian" onclick="selectRuleReceiptA4('SanLian');">
                                <i></i>
                                <span>三联</span>
                            </ul>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div">
                            <div class="col-xs-10 col-sm-10 col-md-10	col-lg-10">
                                <div class="headTitle">
                                    <input type="text" id="idA4" style="display: none"/>
                                    <input type="text" id="ownerIdA4" style="display: none"/>
                                    <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                                        <span>小票名称:</span>
                                        <input type="text" id="receiptNameA4"/>
                                    </div>
                                    <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                                        <span>小票类型:</span>
                                        <select id="receiptTypeA4" onchange="receiptTypeSelectA4()">
                                            <option value="SO">销售单据</option>
                                            <option value="TR">调拨单</option>
                                        </select>
                                    </div>
                                    <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                                        <span>公共类型:</span>
                                        <select id="commonTypeA4">
                                            <option value="1">否</option>
                                            <option value="0">是</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div">
                            <div class="col-xs-10 col-sm-10 col-md-10	col-lg-10">
                                <div class="headTitle">
                                    <h3 onclick="selectheadPrintA4()">
                                        <span style="font-size: 14px;">头部打印</span>
                                    </h3>
                                    <div class="headTitleDiv" id="headPrintA4">
                                        <ul>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="storeName" onclick="selectThisA4(this,'storeNameA4')">
                                                    <i></i>
                                                    <span>门店名称</span>
                                                </div>
                                            </li>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="billType" onclick="selectThisA4(this,'billTypeA4')">
                                                    <i></i>
                                                    <span>单据类型</span>
                                                </div>
                                            </li>

                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="billNo" onclick="selectThisA4(this,'billNoA4')">
                                                    <i></i>
                                                    <span>单号</span>
                                                </div>
                                            </li>

                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="makeBill" onclick="selectThisA4(this,'makeBillA4')">
                                                    <i></i>
                                                    <span>制单人</span>
                                                </div>
                                            </li>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="billDate" onclick="selectThisA4(this,'billDateA4')">
                                                    <i></i>
                                                    <span>日期</span>
                                                </div>
                                            </li>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="coustmer" onclick="selectThisA4(this,'coustmerA4')">
                                                    <i></i>
                                                    <span>客户</span>
                                                </div>
                                            </li>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="remark" onclick="selectThisA4(this,'remarkA4')">
                                                    <i></i>
                                                    <span>备注</span>
                                                </div>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div">
                            <div class="col-xs-10 col-sm-10 col-md-10	col-lg-10">
                                <div class="headTitle">
                                    <h3 onclick="selectTablePrintA4()">
                                        <span style="font-size: 14px;">表头打印</span>
                                    </h3>
                                    <div class="headTitleDiv" id="tablePrintA4" style="display: none">
                                        <ul>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="styleId" onclick="selectThisA4class(this,'styleId')">
                                                    <i></i>
                                                    <span>款号</span>
                                                </div>
                                            </li>

                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="styleName" onclick="selectThisA4class(this,'styleName')">
                                                    <i></i>
                                                    <span>款名</span>
                                                </div>
                                            </li>

                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="colorId" onclick="selectThisA4class(this,'colorId')">
                                                    <i></i>
                                                    <span>颜色</span>
                                                </div>
                                            </li>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="sizeId" onclick="selectThisA4class(this,'sizeId')">
                                                    <i></i>
                                                    <span>尺码</span>
                                                </div>
                                            </li>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="qty" onclick="selectThisA4class(this,'qty')">
                                                    <i></i>
                                                    <span>数量</span>
                                                </div>
                                            </li>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="price" onclick="selectThisA4class(this,'price')">
                                                    <i></i>
                                                    <span>吊牌价</span>
                                                </div>
                                            </li>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="totPrice" onclick="selectThisA4class(this,'totPrice')">
                                                    <i></i>
                                                    <span>金额</span>
                                                </div>
                                            </li>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="discount" onclick="selectThisA4class(this,'discountA4')">
                                                    <i></i>
                                                    <span>折扣</span>
                                                </div>
                                            </li>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="actPrice" onclick="selectThisA4class(this,'actPrice')">
                                                    <i></i>
                                                    <span>折后价</span>
                                                </div>
                                            </li>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="totActPrice" onclick="selectThisA4class(this,'totActPrice')">
                                                    <i></i>
                                                    <span>折后金额</span>
                                                </div>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div">
                            <div class="col-xs-10 col-sm-10 col-md-10	col-lg-10">
                                <div class="headTitle">
                                    <h3 onclick="selectFootPrintA4()">
                                        <span style="font-size: 14px;">页脚打印</span>
                                    </h3>
                                    <div class="headTitleDiv" id="footPrintA4" style="display: none">
                                        <ul>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="storeName" onclick="selectThisA4(this,'shopBeforeA4')">
                                                    <i></i>
                                                    <span>售前余额</span>
                                                </div>
                                            </li>

                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="billNo" onclick="selectThisA4(this,'shopAfterA4')">
                                                    <i></i>
                                                    <span>售后余额</span>
                                                </div>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div">
                            <div class="col-xs-10 col-sm-10 col-md-10	col-lg-10">
                                <div class="headTitle">
                                    <button id='save_guest_buttonA4' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='saveA4()'>
                                        <i class='ace-icon fa fa-save'></i>
                                        <span class='bigger-110'>保存</span>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="SanLianPrint" style="display: none">
            <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12" style="margin-top: 20px;margin-left: 50px">
                <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                    <div class="headTitle">
                        <div >
                            <div id="printTopSanLian">
                                <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12">
                                    <span class="col-xs-12 col-sm-12 col-md-12	col-lg-12" data-name="storeName" id="storeNameSanLian" style="font-weight:bold;font-size:20px;display:table-cell;vertical-align:middle;text-align:center">店铺名称</span>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12" >
                                    <span class="col-xs-3 col-sm-3 col-md-3 col-lg-3" data-name="billNo" id="billNoSanLian" style="display:table-cell;vertical-align:middle;text-align:center">单号:xx</span>
                                    <span class="col-xs-3 col-sm-3 col-md-3 col-lg-3" data-name="coustmer" id="coustmerSanLian" style="display:table-cell;vertical-align:middle;text-align:center">客户:xx</span>
                                    <span class="col-xs-3 col-sm-3 col-md-3 col-lg-3" data-name="businessId" id="businessIdSanLian" style="display:table-cell;vertical-align:middle;text-align:center">销售员:xx</span>
                                    <span class="col-xs-3 col-sm-3 col-md-3 col-lg-3" data-name="billDate" id="billDateSanLian" style="display:table-cell;vertical-align:middle;text-align:center">日期:xx</span>
                                </div>
                            </div>
                            <div id="edit-SanLian-dialog" style="text-align: center ;font-size:12px;" class="col-xs-12 col-sm-12 col-md-12	col-lg-12">

                                <table style="text-align: center;font-size:12px;border-collapse:collapse;border:1px solid #000;"class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                    <thead >
                                    <tr style="border:1px solid #000;">
                                        <th align="left" class="styleIdA4" nowrap="nowrap" style="border:0px;font-size:10px;border:1px solid #000;">款号</th>
                                        <th align="left" class="styleNameA4"nowrap="nowrap" style="border:0px;font-size:10px;border:1px solid #000;">款名</th>
                                        <th align="left" class="colorIdA4" nowrap="nowrap"style="border:0px;font-size:10px;border:1px solid #000;">颜色</th>
                                        <th align="left" class="sizeIdA4" nowrap="nowrap" style="border:0px;font-size:10px;border:1px solid #000;">尺码</th>
                                        <th align="left" class="qtyA4" nowrap="nowrap" style="border:0px;font-size:10px;border:1px solid #000;">数量</th>
                                        <th align="left" class="priceA4" nowrap="nowrap" style="border:0px;font-size:10px;border:1px solid #000;">吊牌价</th>
                                        <th align="left" class="totPriceA4" nowrap="nowrap" style="border:0px;font-size:10px;border:1px solid #000;">金额</th>
                                        <th align="left" class="discountA4" nowrap="nowrap" style="border:0px;font-size:10px;border:1px solid #000;">折扣</th>
                                        <th align="left" class="actPriceA4" nowrap="nowrap" style="border:0px;font-size:10px;border:1px solid #000;">折后价</th>
                                        <th align="left" class="totActPriceA4" nowrap="nowrap" style="border:0px;font-size:10px;border:1px solid #000;">折后金额</th>
                                    </tr>
                                    </thead>
                                    <tbody id="loadtabSanLian">
                                    <tr style="border-top:1px ;padding-top:5px;border:1px solid #000;">
                                        <td align="left" class="styleIdA4" style="border-top:1px ;padding-top:5px;border:1px solid #000;">&nbsp;</td>
                                        <td align="left" class="styleNameA4" style="border-top:1px ;padding-top:5px;border:1px solid #000;">&nbsp;</td>
                                        <td align="left" class="colorIdA4" style="border-top:1px ;padding-top:5px;border:1px solid #000;">&nbsp;</td>
                                        <td align="left" class="sizeIdA4" style="border-top:1px ;padding-top:5px;border:1px solid #000;">&nbsp;</td>
                                        <td align="left" class="qtyA4" style="border-top:1px ;padding-top:5px;border:1px solid #000;">0</td>
                                        <td align="left" class="priceA4" style="border-top:1px ;padding-top:5px;border:1px solid #000;">&nbsp;</td>
                                        <td align="left" class="totPriceA4" style="border-top:1px ;padding-top:5px;border:1px solid #000;">&nbsp;</td>
                                        <td align="left" class="discountA4" style="border-top:1px ;padding-top:5px;border:1px solid #000;">&nbsp;</td>
                                        <td align="left" class="actPriceA4" style="border-top:1px ;padding-top:5px;border:1px solid #000;">&nbsp;</td>
                                        <td align="left" class="totActPriceA4" style="border-top:1px ;padding-top:5px;border:1px solid #000;">0</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div id="printFootSanLian">
                                <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12" >
                                    <span class="col-xs-4 col-sm-4 col-md-4 col-lg-4" data-name="thisMoney" id="thisMoneySanLian" style="display:table-cell;vertical-align:middle;text-align:center">本单额xx</span>
                                    <span class="col-xs-4 col-sm-4 col-md-4 col-lg-4" data-name="thisArrears" id="thisArrearsSanLian" style="display:table-cell;vertical-align:middle;text-align:center">本单欠xx</span>
                                    <span class="col-xs-4 col-sm-4 col-md-4 col-lg-4" data-name="totalArrears" id="totalArrearsSanLian" style="display:table-cell;vertical-align:middle;text-align:center">累计欠xx</span>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12" >
                                    <span class="col-xs-10 col-sm-10 col-md-10 col-lg-10" data-name="remark" id="remarkSanLian" style="display:table-cell;vertical-align:middle;text-align:center">备注:xx</span>
                                    <span class="col-xs-2 col-sm-2 col-md-2 col-lg-2" data-name="handler" id="handlerSanLian" style="display:table-cell;vertical-align:middle;text-align:center">经办人:xx</span>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12" >
                                    <span class="col-xs-6 col-sm-6 col-md-6 col-lg-6" data-name="Tel" id="TelSanLian" style="display:table-cell;vertical-align:middle;text-align:center">电话:xx</span>
                                    <span class="col-xs-6 col-sm-6 col-md-6 col-lg-6" data-name="phone" id="phoneSanLian" style="display:table-cell;vertical-align:middle;text-align:center">手机:xx</span>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12" >
                                    <span class="col-xs-12 col-sm-12 col-md-12 col-lg-12" data-name="address" id="addressSanLian" style="display:table-cell;vertical-align:middle;text-align:center">地址:xx</span>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div" id="footExtendSanLian">
                                <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12">
                                    <span >欢迎来到Ancient Stone</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-xs-8 col-sm-8 col-md-8	col-lg-8">
                    <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div">
                        <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2">
                            <span style="text-align:center;display: block">小票规格:</span>
                        </div>
                        <div id="ruleReceiptSanLian">
                            <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2">
                                <ul class="stecs" data-name="58" onclick="selectRuleReceiptSanLian(58);">
                                    <i></i>
                                    <span>58mm</span>
                                </ul>
                            </div>
                            <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2">
                                <ul class="stecs" data-name="80" onclick="selectRuleReceiptSanLian(80);">
                                    <i></i>
                                    <span>80mm</span>
                                </ul>
                            </div>
                            <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2">
                                <ul class="stecs" data-name="110" onclick="selectRuleReceiptSanLian(110);">
                                    <i></i>
                                    <span>110mm</span>
                                </ul>
                            </div>
                            <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2">
                                <ul class="stecs" data-name="A4" onclick="selectRuleReceiptSanLian('A4');">
                                    <i></i>
                                    <span>A4</span>
                                </ul>
                            </div>
                            <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2">
                                <ul class="stecs" data-name="A4N0Size" onclick="selectRuleReceiptSanLian('A4N0Size');">
                                    <i></i>
                                    <span>A4(对货)</span>
                                </ul>
                            </div>
                            <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2">
                                <ul class="stecs" data-name="SanLian" onclick="selectRuleReceiptSanLian('SanLian');">
                                    <i></i>
                                    <span>三联</span>
                                </ul>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div">
                            <div class="col-xs-10 col-sm-10 col-md-10	col-lg-10">
                                <div class="headTitle">
                                    <input type="text" id="idSanLian" style="display: none"/>
                                    <input type="text" id="ownerIdSanLian" style="display: none"/>
                                    <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                                        <span>小票名称:</span>
                                        <input type="text" id="receiptNameSanLian"/>
                                    </div>
                                    <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                                        <span>小票类型:</span>
                                        <select id="receiptTypeSanLian" onchange="receiptTypeSelect()">
                                            <option value="SO">销售单据</option>
                                        </select>
                                    </div>
                                    <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                                        <span>公共类型:</span>
                                        <select id="commonTypeSanLian">
                                            <option value="1">否</option>
                                            <option value="0">是</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div">
                            <div class="col-xs-10 col-sm-10 col-md-10	col-lg-10">
                                <div class="headTitle">
                                    <h3 onclick="selectheadPrintSanLian()">
                                        <span style="font-size: 14px;">头部打印</span>
                                    </h3>
                                    <div class="headTitleDiv" id="headPrintSanLian">
                                        <ul>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="storeName" onclick="selectThisSanLian(this,'storeNameSanLian')">
                                                    <i></i>
                                                    <span>店铺名称</span>
                                                </div>
                                            </li>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="billNo" onclick="selectThisSanLian(this,'billNoSanLian')">
                                                    <i></i>
                                                    <span>单号</span>
                                                </div>
                                            </li>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="coustmer" onclick="selectThisSanLian(this,'coustmerSanLian')">
                                                    <i></i>
                                                    <span>客户</span>
                                                </div>
                                            </li>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="businessId" onclick="selectThisSanLian(this,'businessIdSanLian')">
                                                    <i></i>
                                                    <span>销售员</span>
                                                </div>
                                            </li>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="billDate" onclick="selectThisSanLian(this,'billDateSanLian')">
                                                    <i></i>
                                                    <span>日期</span>
                                                </div>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div">
                            <div class="col-xs-10 col-sm-10 col-md-10	col-lg-10">
                                <div class="headTitle">
                                    <h3 onclick="selectTablePrintSanLian()">
                                        <span style="font-size: 14px;">表头打印</span>
                                    </h3>
                                    <div class="headTitleDiv" id="tablePrintSanLian" style="display: none">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div">
                            <div class="col-xs-10 col-sm-10 col-md-10	col-lg-10">
                                <div class="headTitle">
                                    <h3 onclick="selectFootPrintSanLian()">
                                        <span style="font-size: 14px;">页脚打印</span>
                                    </h3>
                                    <div class="headTitleDiv" id="footPrintSanLian" style="display: none">
                                        <ul>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="thisMoney" onclick="selectThisSanLian(this,'thisMoneySanLian')">
                                                    <i></i>
                                                    <span>本单额</span>
                                                </div>
                                            </li>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="thisArrears" onclick="selectThisSanLian(this,'thisArrearsSanLian')">
                                                    <i></i>
                                                    <span>本单额</span>
                                                </div>
                                            </li>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="totalArrears" onclick="selectThisSanLian(this,'totalArrearsSanLian')">
                                                    <i></i>
                                                    <span>累计欠</span>
                                                </div>
                                            </li>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="remark" onclick="selectThisSanLian(this,'remarkSanLian')">
                                                    <i></i>
                                                    <span>备注</span>
                                                </div>
                                            </li>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="handler" onclick="selectThisSanLian(this,'handlerSanLian')">
                                                    <i></i>
                                                    <span>经办人</span>
                                                </div>
                                            </li>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="Tel" onclick="selectThisSanLian(this,'TelSanLian')">
                                                    <i></i>
                                                    <span>电话</span>
                                                </div>
                                            </li>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="phone" onclick="selectThisSanLian(this,'phoneSanLian')">
                                                    <i></i>
                                                    <span>手机</span>
                                                </div>
                                            </li>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="address" onclick="selectThisSanLian(this,'addressSanLian')">
                                                    <i></i>
                                                    <span>地址</span>
                                                </div>
                                            </li>
                                            <li class="headTitleLi">
                                                <span>扩展打印(在页脚展示扩展信息,换行请输入&lt;br&gt;)：</span>
                                                <br>
                                                <textarea class="col-xs-8 col-sm-8 col-md-8	col-lg-8" id="footExtendWriteSanLian" onkeyup="writeFootExtendSanLian(this)">欢迎来到Ancient Stone</textarea>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div">
                            <div class="col-xs-10 col-sm-10 col-md-10	col-lg-10">
                                <div class="headTitle">
                                    <button id='save_guest_buttonSanLian' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='saveSanLian()'>
                                        <i class='ace-icon fa fa-save'></i>
                                        <span class='bigger-110'>保存</span>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="A4NoSizePrint" style="display: none">
            <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12" style="margin-top: 20px;margin-left: 50px;"  >
                <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                    <div class="headTitle">
                        <div id="edit-A4-dialog-NoSize" style="text-align: center ;font-size:12px;" class="col-xs-12 col-sm-12 col-md-12	col-lg-12">
                            <table style="text-align: center;font-size:12px;border-collapse:collapse;"class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                <thead >
                                <tr style="border:1px solid #000;" id="head-A4-dialog-NoSize">
                                    <th align="left" data-name="styleId" class="styleIdNoSizeA4" nowrap="nowrap" style="border:0px;font-size:10px;border:1px solid #000;">款号</th>
                                    <th align="left" data-name="styleName" class="styleNameNoSizeA4"nowrap="nowrap" style="border:0px;font-size:10px;border:1px solid #000;">款名</th>
                                    <th align="left" data-name="supplierName" class="supplierNameNoSizeA4" nowrap="nowrap"style="border:0px;font-size:10px;border:1px solid #000;">厂家/品牌</th>
                                    <th align="left" data-name="qty" class="qtyNoSizeA4" nowrap="nowrap"style="border:0px;font-size:10px;border:1px solid #000;">数量</th>
                                    <th align="left" data-name="price" class="priceNoSizeA4" nowrap="nowrap"style="border:0px;font-size:10px;border:1px solid #000;">吊牌价</th>
                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                                <tfoot id="foot-A4-dialog-NoSize">
                                <tr>
                                    <th align="left" class="styleIdNoSizeA4" nowrap="nowrap" style="border:0px;font-size:10px;border:1px solid #000;"></th>
                                    <th align="left" class="styleNameNoSizeA4"nowrap="nowrap" style="border:0px;font-size:10px;border:1px solid #000;"></th>
                                    <th align="left" class="supplierNameNoSizeA4" nowrap="nowrap"style="border:0px;font-size:10px;border:1px solid #000;"></th>
                                    <th align="left" class="qtyNoSizeA4" nowrap="nowrap"style="border:0px;font-size:10px;border:1px solid #000;">0</th>
                                    <th align="left" class="priceNoSizeA4" nowrap="nowrap"style="border:0px;font-size:10px;border:1px solid #000;"></th>
                                </tr>
                                <tr>
                                    <th align="left" data-name ='remark' colspan="4" class="remarkNoSizeA4" nowrap="nowrap"   style="border:0px;font-size:10px; text-align:center">备注</th>
                                </tr>
                                <tr>
                                    <th align="left" data-name ='destName' colspan="4" class="destNameNoSizeA4" nowrap="nowrap"  style="border:0px;font-size:10px; text-align:center">仓库</th>
                                </tr>
                                </tfoot>
                            </table>
                        </div>

                    </div>
                </div>
                <div class="col-xs-8 col-sm-8 col-md-8	col-lg-8">
                    <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div" id="ruleReceiptA4NoSize">
                        <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2">
                            <span style="text-align:center;display: block">小票规格:</span>
                        </div>
                        <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2">
                            <ul class="stecs" data-name="58" onclick="selectRuleReceiptA4NoSize(58);">
                                <i></i>
                                <span>58mm</span>
                            </ul>
                        </div>
                        <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2">
                            <ul class="stecs" data-name="80" onclick="selectRuleReceiptA4NoSize(80);">
                                <i></i>
                                <span>80mm</span>
                            </ul>
                        </div>
                        <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2">
                            <ul class="stecs" data-name="110" onclick="selectRuleReceiptA4NoSize(110);">
                                <i></i>
                                <span>110mm</span>
                            </ul>
                        </div>
                        <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2">
                            <ul class="stecs" data-name="A4" onclick="selectRuleReceiptA4NoSize('A4');">
                                <i></i>
                                <span>A4</span>
                            </ul>
                        </div>
                        <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2">
                            <ul class="stecs" data-name="A4N0Size" onclick="selectRuleReceiptA4NoSize('A4N0Size');">
                                <i></i>
                                <span>A4(对货)</span>
                            </ul>
                        </div>
                        <div class="col-xs-2 col-sm-2 col-md-2	col-lg-2" >
                            <ul class="stecs" data-name="SanLian" onclick="selectRuleReceiptA4NoSize('SanLian');">
                                <i></i>
                                <span>三联</span>
                            </ul>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div">
                            <div class="col-xs-10 col-sm-10 col-md-10	col-lg-10">
                                <div class="headTitle">
                                    <input type="text" id="idA4NoSize" style="display: none"/>
                                    <input type="text" id="ownerIdA4NoSize" style="display: none"/>
                                    <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                                        <span>小票名称:</span>
                                        <input type="text" id="receiptNameA4NoSize"/>
                                    </div>
                                    <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                                        <span>小票类型:</span>
                                        <select id="receiptTypeA4NoSize" onchange="receiptTypeSelectNoSizeA4(this)">
                                            <option value="TR">调拨单</option>
                                        </select>
                                    </div>
                                    <div class="col-xs-4 col-sm-4 col-md-4	col-lg-4">
                                        <span>公共类型:</span>
                                        <select id="commonTypeA4NoSize">
                                            <option value="1">否</option>
                                            <option value="0">是</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div">
                            <div class="col-xs-10 col-sm-10 col-md-10	col-lg-10">
                                <div class="headTitle">
                                    <h3 onclick="selectTablePrintA4NoSize()">
                                        <span style="font-size: 14px;">表头打印</span>
                                    </h3>
                                </div>
                                <div class="headTitleDiv" id="tablePrintA4NoSize" style="display: none">
                                    <ul>
                                        <li class="headTitleLi">
                                            <div class="stecs on" data-name="styleId" onclick="selectThisA4NoSizeclass(this,'styleIdNoSizeA4')">
                                                <i></i>
                                                <span>款号</span>
                                            </div>
                                        </li>
                                        <li class="headTitleLi">
                                            <div class="stecs on" data-name="styleName" onclick="selectThisA4NoSizeclass(this,'styleNameNoSizeA4')">
                                                <i></i>
                                                <span>款名</span>
                                            </div>
                                        </li>
                                        <li class="headTitleLi">
                                            <div class="stecs on" data-name="supplierName" onclick="selectThisA4NoSizeclass(this,'supplierNameNoSizeA4')">
                                                <i></i>
                                                <span>厂家/品牌</span>
                                            </div>
                                        </li>
                                        <li class="headTitleLi">
                                            <div class="stecs on" data-name="qty" onclick="selectThisA4NoSizeclass(this,'qtyNoSizeA4')">
                                                <i></i>
                                                <span>数量</span>
                                            </div>
                                        </li>
                                        <li class="headTitleLi">
                                            <div class="stecs on" data-name="price" onclick="selectThisA4NoSizeclass(this,'priceNoSizeA4')">
                                                <i></i>
                                                <span>吊牌价</span>
                                            </div>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div">
                            <div class="col-xs-10 col-sm-10 col-md-10	col-lg-10">
                                <div class="headTitle">
                                    <h3 onclick="selectFootPrintA4NoSize()">
                                        <span style="font-size: 14px;">页脚打印</span>
                                    </h3>
                                    <div class="headTitleDiv" id="footPrintA4NoSize">
                                        <ul>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="remark" onclick="selectThisA4NoSizeclass(this,'remarkNoSizeA4')">
                                                    <i></i>
                                                    <span>备注</span>
                                                </div>
                                            </li>
                                            <li class="headTitleLi">
                                                <div class="stecs on" data-name="destName" onclick="selectThisA4NoSizeclass(this,'destNameNoSizeA4')">
                                                    <i></i>
                                                    <span>仓库</span>
                                                </div>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-12 col-md-12	col-lg-12 Print-Bg-Top-div">
                            <div class="col-xs-10 col-sm-10 col-md-10	col-lg-10">
                                <div class="headTitle">
                                    <button id='save_guest_buttonA4NoSize' type='button' style='margin-left: 20px' class='btn btn-sm btn-primary' onclick='saveA4NoSize()'>
                                        <i class='ace-icon fa fa-save'></i>
                                        <span class='bigger-110'>保存</span>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
    <jsp:include page="../layout/footer.jsp"></jsp:include>
</div>
<jsp:include page="../layout/footer_js.jsp"></jsp:include>
<jsp:include page="../base/unit_dialog.jsp"></jsp:include>
<jsp:include page="print_one.jsp"></jsp:include>
<jsp:include page="print_two.jsp"></jsp:include>
<jsp:include page="print_threes.jsp"></jsp:include>
<script src="<%=basePath%>/views/sys/printSetController.js"></script>
<script src="<%=basePath%>/Olive/plugin/print/LodopFuncs.js"></script>

</body>
</html>
