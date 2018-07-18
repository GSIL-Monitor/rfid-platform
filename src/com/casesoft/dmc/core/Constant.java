package com.casesoft.dmc.core;

import com.casesoft.dmc.core.util.file.PropertyUtil;
import io.swagger.models.auth.In;

public class Constant {
    public static Boolean isWindows;
    static {
        try {
            isWindows = Boolean.parseBoolean(PropertyUtil.getValue("is_windows"));
        } catch (Exception e) {
            isWindows = false;
        }
    }
    public static String rootPath;
    static {
        if(isWindows){
            rootPath ="C:";
        }else{
            rootPath="\\hone";
        }
    }
    public final static class LogType {
        public final static String LONG_TIME = "LTM";
        public final static String SYSTEM = "SYS";
        public final static String API = "API";
        public final static String SYN = "SYN";
    }

    public final static class DataSrc {
        public final static String SYS = "01";
        public final static String SYN = "02";
        public final static String EXCEL = "03";
    }

  public final static class Session {
    public final static String User_Session = "userSession";
  }

  public final static class Code {
    public final static String Property_Key_Code_prefix = "PK";
  }


  public final static class Password {
    public final static String IV = "84532679";
    public final static String User_Init = "123456";
  }

  public final static class Bill {
    public final static String Inbound_Prefix = "WIB";
    public final static String Outbound_Prefix = "WOB";

    public final static String Refund_Inbound_Prefix = "RIB";
    public final static String Refund_Outbound_Prefix = "ROB";

    public final static String Transfer_Inbound_Prefix = "TIB";
    public final static String Transfer_Outbound_Prefix = "TOB";

    public final static String Tag_Init_Prefix = "TIB";// 标签初始化任务号前缀

    public final static String EPayBill_Prefix = "EPB";// 电商销售单(消费者O2O电商支付单)前缀
  }

    public final static class DataSource {
        public final static String SQL_Server = "S";
        public final static String ORACLE = "O";
        public final static String MYSQL = "M";
    }

    public final static class GuestPrefix{
        public final static String DefaultGuest="AGST";
    }

    public static final class GuestType{
        public static final int ProvinceProxy=0;//省代客户
        public static final int ShopProxy=1;    //门店客户
        public static final int RetailProcy=2;  //零售客户
    }
  public final static class Sys {

    public final static String Company_Name = "凯施智联软件科技";

    public final static int Menu_Code_Min_Length = 2;

    public final static String User_Menu_Session = "menuSession";

    public static final String Role_Id_String = "roleId";

    public static final String All_Authorize = "所有权限";

    public static final int Parent_Menu_Length = 6;

    public static final String Menu_Component_Separator = "_";

    public static final String Menu_Id_String = "menuId";

    public static final String Storage_Code_Prefix = "S";

    public static final String Device_KC_Prefix = "KC";
  }

  public  static final class Folder {
    public static final  String Epc_Init_File_Folder=rootPath+"\\casesoft_temp\\initFile\\";
    public final static String Epc_Init_Zip_File_Folder=rootPath+"\\casesoft_temp\\initFileZip\\";
    public final static String Report_File_Folder=rootPath+"\\casesoft_temp\\report\\";
    public final static String Product_File_Folder=rootPath+"\\casesoft_temp\\productZip\\";
  }
  public final static class TagSerial {
      public final static int EPC_Prefix_Length = 6;

	  public final static String Xinshuang = "00";
	  public final static String Burton = "01";
	  public final static String Aoyang = "02";
	  public final static String Jinjiang = "03";
	  public final static String Auchan = "04";
	  public final static String HodoMan = "05";
	  public final static String SpecialCloud="06";
	  public final static String Hanuman="07";
	  public final static String Tiantan="08";
	  public final static String Playlounge="09";
      public final static String MetersBonWe="10";
      public final static String NewBroadcast="11";
      public final static String Exhibition="12";
      public final static String Leverstyle="13";
      public final static String Kingthy="14";
      public final static String ELand="15";
      public final static String Ellassay = "17";
      public final static String neoen = "18";
      public final static String Ivykki = "19";
      public final static String Glm = "20";
      public final static String Heyi = "21";
      public final static String AncientStone = "24";
  }


    public static final class StoreType {
        public static final int Storage = 0;
        public static final int Shop = 1;
    }
    
    public static final class UserType {
    	public static final int User = 0;
    	public static final int Admin = 1;
    	public static final int Customer =2;
    	public static final int Employeer = 3;
        public static final int Shop_Saler = 4;
    }

    public static final class RoleType {
        public static final String Admin = "0";
        public static final String Shop = "4";
        public static final String Header = "1";
        public static final String Storage = "W-1";
    }

    public static final class UnitType {
        public static final int Vender = 0;//供应商
        public static final int Headquarters = 1;//总部
        public static final int Agent = 2;//代理商，经销商，分销商  --客户省代类型
        public static final int Factory = 3;//加工厂
        public static final int Shop = 4;//门店                   --客户门店零售

        public static final int NetShop = 5;//网店

        public static final int SampleRoom = 6;//样衣间
        public static final int Department = 7;//部门
        public static final int Franchisee = 8;//加盟商
        public static final int Warehouse = 9;//

        public static final int Guest =10;//客户,
        public static final int Organization = 11; //组织or公司。用于做分级管理，门店的所属方
    }

    public static final class UnitCodePrefix {
        public static final String Vender = "AUTO_VD";//供应商
        public static final String Headquarters = "B";//总部
        public static final String Agent = "AUTO_AG";//代理商，经销商，分销商
        public static final String Factory = "AUTO_FC";//加工厂
        public static final String Shop = "AUTO_SH";//门店

        public static final String NetShop = "N";//门店


        public static final String SampleRoom = "E";//样衣间
        public static final String Department = "D";//部门

        public static final String Warehouse = "AUTO_WH";//仓库
        public static final String Franchisee = "AUTO_FR";//加盟

        public static final String Organization = "AUTO_OR"; //组织or公司
    }

    public static final class Token {
        public final static int Label_Data_Birth = 1;
        public final static int Label_Data_Write = 2;//标签初始化
        public final static int Label_Data_Detect = 3;
        public final static int Label_Data_Receive = 4;//品牌商标签接收
        public final static int Label_Data_Send = 5;//品牌商将标签发放给供应商
        public final static int Label_Data_Feedback = 6;//供应商接收到标签

        public final static int Factory_Box_Send = 7;//加工厂装箱发货
        public final static int Factory_Paper = 51;//加工厂纸样
        public final static int Factory_Cut = 52;//加工厂裁剪
        public final static int Factory_SubConstant = 53;//加工厂分包
        public final static int Factory_Stitch = 54;//加工厂缝纫
        public final static int Factory_OutSource = 55;//加工厂外包出
        public final static int Factory_InBound = 56;//加工厂外包入
        public final static int Factory_StitchChecked = 57;//加工厂缝纫后质检   
        public final static int Factory_Wash = 58;//加工厂水洗
        public final static int Factory_WashChecked =59;//加工厂水洗后质检
        public final static int Factory_CheckBack =60;//加工厂质检返工
        public final static int Factory_Pack = 61;//加工厂包装
        public final static int Factory_Out = 62;//加工厂发货
        public final static int Factory_dye = 63;//加工厂染色
        
        public final static int Storage_Inbound = 8;//采购入库
        public final static int Storage_Refund_Outbound = 26;//退货给供应商
        public final static int Storage_Inventory = 9;//盘点
        public final static int Storage_Outbound = 10;//仓库出库
        public final static int Storage_Inbound_customer = 11;//客户仓库收货（代理商或门店仓库收货token值 销售订单入库)
        public final static int Storage_refoundOut_customer  = 13;//客户仓库退货出库（代理商或门店仓库退货出库token值 销售退货出库)
        public final static int Storage_Refund_Inbound = 23;//门店退给总部
        public final static int Storage_Transfer_Outbound = 24;//调拨出库
        public final static int Storage_Transfer_Inbound = 25;//调拨入库
        public final static int Storage_Adjust_Outbound = 28;//调整出库
        public final static int Storage_Adjust_Inbound = 29;//调整入库
        public final static int Storage_Consigment_Inbound = 12;//寄售入库

        public final static int Storage_Outbound_agent = 37;//仓库给代理商发货
        public final static int Storage_Inbound_agent_refund = 38;//代理商退仓

        public final static int Storage_Other_Outbound=32;

        public final static int Shop_Inbound = 14;//门店入库
        public final static int Shop_Sales = 15;
        public final static int Shop_Inventory = 16;
        public final static int Shop_Transfer_Outbound = 18;
        public final static int Shop_Transfer_Inbound = 19;
        public final static int Shop_Sales_refund = 17;
        public final static int Shop_Refund_Outbound = 27;
        public final static int Shop_Adjust_Outbound = 30;
        public final static int Shop_Adjust_Inbound = 31;

        public final static int Shop_Other_Outbound=33;
        public final static int Shop_TransferSale_Outbound=50;


    }

    public class TaskType {
        public final static int Inbound = 1;
        public final static int Outbound = 0;
        public final static int Inventory = 3;

        public final static int Else = -1;
    }    
    
    public class TaskStatus {
        public final static int Scanning = -1;//扫描中
        public final static int Submitted = 0;//已提交，任务数据已保存
        public final static int Jointed = 1;//已对接ERP
        public final static int Confirmed = 2;// 出库后入库方已确认
    }

    public static final class DressingStatus {
        public static final int Dressing = 1; //穿着中
        public static final int Returned = 0; //在库
    }

    public static class ScmConstant {

        public final static class SaleBillStatus{
            public final static int UnPay = 0;
            public final static int Pay =1;
            public final static int RefundMoney=2;
        }
        public final static class Code {
            public final static String Id_Separate_Char = "-";
            public final static String Size_Qty_Field_Prefix = "sizeQty";
            public final static String BillNo_Start_No = "0001";
            public final static int BillNo_Seq_No_Num = 4;
        }

        /**
         * 保存后的单据运行删除、修改，确认后的单据不允许再做删除、修改
         */
        public final static class BillStatus {
            public final static int invalid = -1;//已作废
            public final static int saved = 0;//已保存状态
            public final static int confirmed = 1;//已确认
            public final static int Finished = 2;//已完成
        }

        public final static class SaleBillType {
            public final static int Outbound = 0;//零售出库
            public final static int Inbound = 1;//零售退货
        }

        public final static class BillType{
            public  final static String Edit = "E";
            public  final static String Save = "S";
        }
        public final static class TaskType {
            public final static int Outbound = 0;//出库
            public final static int Inbound = 1;//退货
            public final static class InbTaskStatus {
                public final static int unReceive = 0;//未收货
                public final static int received = 1;//已收货
            }
            public final static class OubTaskStatus {
                public final static int unSend = 0;//未发货
                public final static int sent = 1;//已发货
            }
        }

        public final static class PayWay {
            public final static int Money = 0;
            public final static int Card = 1;//银联卡
            public final static int Grand = 2;//积分支付
            public final static int Alipay = 3; // 支付宝
            public final static int Wxpay =4;//微信
            public final static int Voucher =5;//购物券
        }
        public final static class PromotionType{
            public final static String CommonPromotion = "CP";//单品促销
            public final static String BillPromotion = "BP";//整单促销
            public final static String Redemption ="RN";//换购促销

        }
        public final static class PromotionRulerType{
            public final static String Discount = "DT";//打折
            public final static String ReductPrice ="RP";//减免金额
            public final static String Gift ="GT";//赠品
            public final static String Voucher ="VR";//优惠券、购物券
            public final static String Bargain ="BN";//特价
            public final static String Grade ="GE";//积分换购

        }
        public final static class IsRfid{
            public final static int IsRfid = 0;
            public final static int IsBarcode = 1;
        }
        public final static class BillPrefix {
            public final static String SaleBill_Inbound = "SBI";
            public final static String SaleBill_Outbound = "SBO";
        }

        public final static class CodePrefix {
            public final static String Customer = "SC";
        }
    }

    public final static class MultiLevelType{
        public final static String Company = "COMPANY";
        public final static String Style = "STYLE";
        public final static String Color = "COLOR";
        public final static String Size = "SIZE";
        public final static String Product = "PRODUCT";
    }
    public final static class RepositoryType{
        public final static String root = "0";
        public final static String rack = "1";
        public final static String level = "2";
        public final static String allocation = "3";
        public final static String Company = "REPOSITORY";
    }
    //客户欠款变动，积分变动记录的状态设置
    public final static class ChangeRecordStatus{
        public final static Integer SaleOrder = 1; //销售单
        public final static Integer SaleOrderChange = -1; //销售单据发生改变
        public final static Integer SaleOrderCancel = -2; //销售单据撤销
        public final static Integer SaleReturnOrder = 2; //销售退货单
        public final static Integer SaleReturnOrderChange = -3; //销售退货单发生改变
        public final static Integer SaleReturnOrderCancel = -4; //销售退货单撤销
        public final static Integer SaleOrder2ReturnOrder = 3; //销售单关联生成退货单
        public final static Integer CMOrder2ReturnOrder = 4; //寄存单关联销售退货单
    }
}
