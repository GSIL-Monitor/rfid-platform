package com.casesoft.dmc.controller.hall;

/**
 * Created by WinLi on 2017-03-09.
 */
public class HallConstant {
    public final static class TaskType {
        // 0:借出 1:返还 3：初始化入库 4：调拨入库 5:投产下单 6:调拨出库 7:补标入库 9:强制入库
        public final static int Borrow = 0;
        public final static int Back = 1;
        public final static int Init_Inbound = 3;
        public final static int Transfer_Inbound = 4;
        public final static int Produce_Outbound = 5;
        public final static int Transfer_Outbound = 6;

        public final static int Supply_Tag_Inbound = 7;

        public final static int Force_Inbound = 9;
    }
    public final static class CombineType{
        //1表示合并单号
        public final static int Combine = 1;
    }

    public static String getTaskPrefix(int type) {
        String unitFlag = "";
        switch (type) {
            // 0:借出 1:返还 3.初始化入库 4调拨入库 5投产下单6调拨出库
            case HallConstant.TaskType.Borrow:
                unitFlag = "ABO";
                break;
            case HallConstant.TaskType.Back:
                unitFlag = "ABA";
                break;
            case HallConstant.TaskType.Init_Inbound:
                unitFlag = "AII";
                break;
            case HallConstant.TaskType.Transfer_Inbound:
                unitFlag = "ATI";
                break;
            case HallConstant.TaskType.Force_Inbound:
                unitFlag = "AFI";
                break;
            case HallConstant.TaskType.Produce_Outbound:
                unitFlag = "APO";
                break;
            case HallConstant.TaskType.Transfer_Outbound:
                unitFlag = "ATO";
                break;
            case TaskType.Supply_Tag_Inbound:
                unitFlag = "ASI";
                break;
        }
        return unitFlag;
    }

    public final static class TaskStatus {
        public final static int UnConfirm = 0;
        public final static int Confirmed = 1;
    }

    public final static class TagStatus {
        public final static int Birth = 0;
        public final static int Printed = 1;
        public final static int Detected = 2;

        public final static int Inbounded =3;
    }

    public final static class InboundType {
        public final static int Init = 3;
        public final static int Transfer = 4;
    }

    public final static class SampleStatus {
        // 0:未入库1在厅2外借3调拨出库4.投产下单5丢失6申请报损 7报损
        public final static int Un_Inbound = 0;
        public final static int In_Stock = 1;
        public final static int Borrowed = 2;
        public final static int Transfer_Outbound = 3;
        public final static int To_Bill = 4;
        public final static int Lost = 5;


        public final static int Badding = 6;//报损中
        public final static int Bad = 7;//已报损


        public final static class PreBorrowStatus {
            public final static int unPreBorrow = 0;
            public final static int preBorrowed = 1;
        }
    }

    public final static class BackStatus {
        public final static int BackOk = 0;
        public final static int BackLose = 1;
        public final static int BackBad = 2;
        public final static int BackTimeOut = 3;
        public final static int BadTag = 4;
    }
    public final static class Status{//借出调出是否归还
        public final static int notBack = 0;//未归还or 未调入
        public final static int Back = 1;// 已归还 or 调入
    }
    public final static class Code {
        public final static String Inbound_Prefix = "SI";
        public final static String Outbound_Prefix = "SO";

        public final static String Inventory_Prefix = "IVB";

        public final static String Pre_Borrow_Bill_Prefix = "PBB";
        public final static String Search_Bill_Prefix = "SHB";
    }

    public final static class SearchBillType {
        public final static int Search = 0;
        public final static int Pre_Borrow = 1;
    }
    public final static class SearchBillSrcWay {
        public final static int File = 0;
        public final static int Pre_Borrow_Bill = 1;
    }

    public final static class BillStatus {
        public final static int UnConfirm = 0;
        public final static int Confirmed = 1;
        public final static int Checked = 2;
    }
}
