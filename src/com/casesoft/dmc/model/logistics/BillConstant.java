package com.casesoft.dmc.model.logistics;

/**
 * Created by CaseSoft-Software on 2017-06-13.
 */
public class BillConstant {
    public final static class BillStatus{
        public final static Integer hold = -2;// 暂存
        public final static Integer Cancel = -1;//取消
        public final static Integer Enter = 0;//已录入
        public final static Integer Check = 1;//已审核
        public final static Integer End= 2;//已完成
        public final static Integer Doing = 3;//操作中
        public final static Integer Apply = 4;//申请撤销
        public final static Integer Consign = 5;//寄售中
        public final static Integer shophold = 6;//商城订单暂存
        public final static Integer shopEnter = 7;//商城订单录入
        public final static Integer noCheck=8;//反审核
        public final static Integer EndWithCancel = 9;//部分完成部分撤销
        public final static Integer adjust =  10;//调整
        public final static Integer ThirdPartyCancel = -3; //第三方撤销，例如：补货单由销售员提出，又采购员全部撤销时的状态

    }
    public final static class BillInOutStatus{
        public final static Integer InStore = 1;//已入库
        public final static Integer OutStore = 2;//已出库
        public final static Integer Outing = 3; //出库中
        public final static Integer Ining = 4; //入库中
    }
    public final  static class BillPrefix{
        public final static String purchase ="PI";
        public final static String rmADjust ="RM";
        public final static String saleOrder ="SO";//
        public final static String purchaseReturn="PR";//采购退货
        public final static String SaleOrderReturn="SR";//退货
        public final static String Transfer="TR";//调拨
        public final static String Consignment="CM";//寄存
        public final static String Inventory="IT";//调整
        public final static String inventoryMergeBill="MG";//合并单
        public final static String ReplenishiBill="RE";//补货单
        public final static String ReplenishiBillMerge="ME";//补货合并单
        public final static String labelChangeBill="LC";//补货合并单
    }
    public final static  class PayBill{
        public final static  String PayForBill ="PB";//付款单
        public final static  String ReceiptBill= "RB";//收款单
    }
    public final static class BillDtlStatus{
        public final static Integer Order = 0;//订单
        public final static Integer InStore = 1;//已入库
        public final static Integer OutStore = 2;//已出库
        public final static Integer Outing = 3; //出库中
        public final static Integer Ining = 4; //入库中
    }

    public final static class PrintStatus{
        public final static Integer UnPrint = 0;//未打印
        public final static Integer Printting = 1;//部分打印
        public final static Integer Print = 2;//已打印
    }
    public final static class InStockType{
        public final static String NewStyle = "XK";//新款
        public final static String BackOrder = "BH";//补货
        public final static String Distribution ="PH";//供应商配货
        public final static String Consignment ="JS";//寄售

    }
    public final static class customerType{
        public final static String Agent = "CT-AT";     //省代 2
        public final static String Shop = "CT-ST";      // 门店 4
        public final static String Customer ="CT-LS";   //零售
    }

    public final static class styleNew{
        public final static String Alice = "AA";
        public final static String AncientStone = "AS";
        public final static String PriceDiscount = "PD";
        public final static String Shop = "TG";
        public final static String Style="ID";
    }

    public  final static class ChangeType{
        public final static String Series="CS";
        public final static String Price="PC";
        public final static String Shop="TG";
        public final static String Style="ID";
    }

    public final static class replenishOption{
        public final static String Convert = "CONVERT";
        public final static String Cancel = "CANCEL";
    }

    public final static class replenishBillDtlStatus{
        public final static Integer Order = 0;   //订单状态
        public final static Integer Doing = 1; //部分处理
        public final static Integer End = 2;  //全部完成
        public final static Integer EndWithCancel = 3; //部分完成部分撤销
        public final static Integer Cancel = 4;  //全部撤销
    }
}
