package com.casesoft.dmc.extend.playlounge.dao;

public class PostSqlUtil {
	public static String getChOrderSql(String orderTable,String noticeTable){
		String sql="update %s set chnum=(case when chnum IS NULL   then 0  else chnum end) + ? "
				+ "where item in (select dhItem from %s where item=?)";
		return String.format(sql, orderTable,noticeTable);
	}
	public static String getChOrderDtlSql(String orderDtlTable,String noticeTable){
		String sql="update %s set chnum=(case when chnum IS NULL   then 0 else chnum end )+? where clthno=? and color=? and size=?"
				+ " and item in(select dhItem from %s where item=?)";
		return String.format(sql, orderDtlTable,noticeTable);
	}
	
	
	public static String getChNoticeSql(String noticeTable){
		String sql="update %s set chnum=(case when chnum IS NULL then 0 else chnum end)+? , status=?,execution=? where item=?";
		return String.format(sql,noticeTable);
	}
	
	public static String getChNoticeDtlSql(String noticeDtlTable){
		String sql="update %s set chnum=(case when chnum IS NULL   then 0 else chnum end)+? where clthno=? and color=? and size=?"
				+ "  and item=?";
		return String.format(sql,noticeDtlTable);
	}
	
	
	public static String getChMainSql(String mainTable,String noticeTable){
		String sql="insert %s (item,BillDate,whichprice,"
				+ "Outcode,Incode,types,dhItem,tzItem,"
				+ "num,mny,cbmny1,cbmny2,"
				+ "flag,status,submit,execution,"
				+ "prints,printnum,mem,gzczy,gzrq,"
				+ "lrczy,lrrq,jzbz,jzdate,cbmny6)"
				+ " select item,BillDate,whichprice,"
				+ " Outcode,Incode,types,dhItem,tzItem,"
				+ "num,mny,cbmny1,cbmny2,flag,status,"
				+ "submit,execution,prints,printnum,mem,gzczy,gzrq,"
				+ "lrczy,lrrq,jzbz,jzdate,cbmny6 from %s where tzItem=?";
		return String.format(sql,mainTable,noticeTable);
	}
	
	public static String getChMainDtlSql(String mainDtlTable){
		String sql="";
		return String.format(sql,mainDtlTable);
	}
}
