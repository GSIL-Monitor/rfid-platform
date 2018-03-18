package com.casesoft.dmc.extend.tiantan.dao;

public class PostSQL {
	public static String getChReturnInOrderTableSQL(String mainTable, String orderTable){
		return String.format( "update %s set js=1,jsrq=?,jsr=?,sl_1=?,sl_2=? where DJBH in (select BYZD3 from %s where djbh=?) ",orderTable,mainTable);
	}
	public static String getChReturnInOrderDetailTableSQL(String orderDtlTable,String mainTable){
		return String.format("update %s set sl_1=?,sl_2=? where"
				+ "  DJBH in (select BYZD3 from %s where djbh=?) and spdm=? and gg1dm=? and gg2dm=?",orderDtlTable,mainTable);
	}
	public static String getChReturnInMainNoticeTableSQL(String mainTable){
		return String.format( "update %s set ys=1,ysrq=?,ysr=?,sl_2=? where DJBH"
				+ " =?",mainTable);
	}
	/**
	 * 添加终止
	 * */
	public static String getSpChReturnInMainNoticeTableSQL(String mainTable){
		return String.format( "update %s set sp=1,sprq=?,ys=1,ysrq=?,ysr=?,sl_2=? where DJBH"
				+ " =?",mainTable);
	}
	public static String getChReturnInDetialNoticeTableSQL(String noticeTable,String table){
		return String.format("update %s set  sl_2=? where DJBH in (select BYZD3 from %s where djbh=?)"
				+ " and spdm=? and gg1dm=? and gg2dm=?",noticeTable,table);
	}
	/**
	 * sl 订单数，sl_1 通知数，sl_2执行数
	 * */
	public static String getChOrderTableSQL(String noticTable, String orderTable){
		return String.format( "update %s set js=1,jsrq=?,jsr=?,sl_1=?,sl_2=? where DJBH in (select LXDJ from %s where djbh=?) ",orderTable,noticTable);
	}
	/**
	 * sl 订单数，sl_1 通知数，sl_2执行数
	 * sp,终止
	 * */
	public static String getSpChOrderTableSQL(String noticTable, String orderTable){
		return String.format( "update %s set sp=1,sprq=?, js=1,jsrq=?,jsr=?,sl_1=?,sl_2=? where DJBH in (select LXDJ from %s where djbh=?) ",orderTable,noticTable);
	}
	public static String getChOrderDetailTableSQL(String orderDtlTable,String noticeTable){
		return String.format("update %s set sl_1=?,sl_2=? where"
				+ "  DJBH in (select LXDJ from %s where djbh=?) and spdm=? and gg1dm=? and gg2dm=?",orderDtlTable,noticeTable);
	}
	/*
	* 退货出库入库
	* */
	public static String getAfterSpChOrderTableSQL(String noticTable, String orderTable){
		return String.format( "update %s set sp=1,sprq=?, js=1,jsrq=?,jsr=?,sl_2=? where DJBH in (select LXDJ from %s where djbh=?) ",orderTable,noticTable);
	}
	public static String getAfterChOrderDetailTableSQL(String orderDtlTable,String noticeTable){
		return String.format("update %s set sl_2=? where"
				+ "  DJBH in (select LXDJ from %s where djbh=?) and spdm=? and gg1dm=? and gg2dm=?",orderDtlTable,noticeTable);
	}


	/**
	 * sl 通知单数量，sl_1 订单数，sl_2执行数
	 * */
	public static String getChMainNoticeTableSQL(String table){
		return String.format( "update %s set js=1,jsrq=?,jsr=?,sl_2=? where DJBH=?",table);
	}
	/**
	 * sl 通知单数量，sl_1 订单数，sl_2执行数
	 * sp,终止
	 * */
	public static String getSpChMainNoticeTableSQL(String table){
		return String.format( "update %s set sp=1,sprq=?,js=1,jsrq=?,jsr=?,sl_2=? where DJBH=?",table);
	}
	public static String getChDetialNoticeTableSQL(String noticeTable){
		return String.format("update %s set  sl_2=? where DJBH=? and spdm=? and gg1dm=? and gg2dm=?",noticeTable);
	}
	/**
	 * sl 执行数，sl_1 通知数，
	 * */
	public static String getInsertMainTableSQL(String table,String noticeTable){
		return String.format("insert into %s(DJBH,RQ,YDJH,DJXZ,FPLX,"
				+ "LYLX,LXDJ,DAYS,DM1,DM1_1,DM2,DM2_1,DM3,DM3_1,"
				+ "DM4,DM4_1,QDDM,QYDM,YGDM,SL,SL_1,SL_2,SL_3,"
				+ "JE,JE_1,JE_2,JE_3,BZJE,TJ,TJRQ,XC,XCRQ,"
				+ "YS,YSRQ,YSR,JZ,JZRQ,JZR,JS,JSRQ,JSR,"
				+ "SH,SHRQ,SHR,SP,SPRQ,SPR,LL,LLRQ,LLR,ZS,"
				+ "ZSRQ,ZSR,ZDR,YXRQ,RQ_1,RQ_2,RQ_3,RQ_4,BZ,"
				+ "BYZD1,BYZD2,BYZD3,BYZD4,BYZD5,BYZD6,BYZD7,"
				+ "BYZD8,BYZD9,BYZD10,BYZD11,BYZD12,BYZD13,BYZD14,BYZD15"
				+ ") "
				+ "select ?,?,YDJH,DJXZ,FPLX,LYLX,LXDJ,DAYS,"
				+ "DM1,DM1_1,DM2,DM2_1,DM3,DM3_1,DM4,DM4_1,"
				+ "QDDM,QYDM,YGDM,?,SL,0,SL_3,?,JE_1,JE_2,JE_3,"
				+ "?,TJ,TJRQ,XC,XCRQ,"
				+ "?,?,?,0,?,?,0,?,?,SH,SHRQ,SHR,"
				+ "SP,SPRQ,SPR,LL,LLRQ,LLR,ZS,ZSRQ,ZSR,ZDR,YXRQ,RQ_1,"
				+ "RQ_2,RQ_3,RQ_4,BZ,BYZD1,BYZD2,djbh,BYZD4,BYZD5,BYZD6,"
				+ "BYZD7,JE,BYZD9,BYZD10,BYZD11,BYZD12,BYZD13,BYZD14,BYZD15"
				+ " from %s where djbh=?",table,noticeTable);
	}
	public static String getInsertShopMainTableSQL(String table,String noticeTable){
		return String.format("insert into %s(DJBH,RQ,YDJH,DJXZ,FPLX,"
				+ "LYLX,LXDJ,DAYS,DM1,DM1_1,DM2,DM2_1,DM3,DM3_1,"
				+ "DM4,DM4_1,QDDM,QYDM,YGDM,SL,SL_1,SL_2,SL_3,"
				+ "JE,JE_1,JE_2,JE_3,BZJE,TJ,TJRQ,XC,XCRQ,"
				+ "YS,YSRQ,YSR,JZ,JZRQ,JZR,JS,JSRQ,JSR,"
				+ "SH,SHRQ,SHR,SP,SPRQ,SPR,LL,LLRQ,LLR,ZS,"
				+ "ZSRQ,ZSR,ZDR,YXRQ,RQ_1,RQ_2,RQ_3,RQ_4,BZ,"
				+ "BYZD1,BYZD2,BYZD3,BYZD4,BYZD5,BYZD6,BYZD7,"
				+ "BYZD8,BYZD9,BYZD10,BYZD11,BYZD12,BYZD13,BYZD14,BYZD15"
				+ ") "
				+ "select ?,?,YDJH,DJXZ,FPLX,LYLX,LXDJ,DAYS,"
				+ "DM1,DM1_1,DM2,DM2_1,DM3,DM3_1,DM4,DM4_1,"
				+ "QDDM,QYDM,YGDM,?,SL,0,SL_3,?,JE_1,JE_2,JE_3,"
				+ "?,TJ,TJRQ,XC,XCRQ,"
				+ "?,?,?,0,?,?,0,?,?,1,?,?,"
				+ "SP,SPRQ,SPR,LL,LLRQ,LLR,ZS,ZSRQ,ZSR,ZDR,YXRQ,RQ_1,"
				+ "RQ_2,RQ_3,RQ_4,BZ,BYZD1,BYZD2,djbh,BYZD4,BYZD5,BYZD6,"
				+ "BYZD7,JE,BYZD9,BYZD10,BYZD11,BYZD12,BYZD13,BYZD14,BYZD15"
				+ " from %s where djbh=?",table,noticeTable);
	}
	public static String getInsertDetailTableSQL(String table,String noticeTable){
		return String.format("insert into %s(DJBH,MIBH,MXBH,SPDM,GG1DM,GG2DM,"
				+ "SL,SL_1,SL_2,SL_3,BZSL,CKJ,ZK,DJ,DJ_1,DJ_2,DJ_3,"
				+ "JE,JE_1,JE_2,JE_3,BZJE,BZS,HH,DJH,MIH,MXH,DJH_1,"
				+ "MIH_1,MXH_1,BZ,BYZD1,BYZD2,BYZD3,BYZD4,BYZD5,BYZD6,"
				+ "BYZD7,BYZD8,BYZD9,BYZD10,BYZD11,BYZD12,BYZD13,BYZD14,BYZD15) "
				+ "select ?,0,MXBH,SPDM,GG1DM,GG2DM,"
				+ "?,SL,SL_2,SL_3,BZSL,CKJ,ZK,DJ,DJ_1,DJ_2,DJ_3,"
				+ "DJ*?,JE_1,JE_2,JE_3,DJ*?,BZS,HH,DJH,MIH,MXH,DJH_1,"
				+ "MIH_1,MXH_1,BZ,BYZD1,BYZD2,DJBH,BYZD4,BYZD5,BYZD6,"
				+ "BYZD7,BYZD8,BYZD9,BYZD10,BYZD11,BYZD12,BYZD13,BYZD14,BYZD15"
				+ " from %s where DJbh=? and spdm=? and gg1dm=? and gg2dm=?",table,noticeTable);
	}
}
