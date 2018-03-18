package com.casesoft.dmc.extend.playlounge.dao.basic;

import com.casesoft.dmc.extend.playlounge.dao.ErpBillIdFactory;
import com.casesoft.dmc.model.erp.Bill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class PlayloungeBasicDao {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected JdbcTemplate playloungeJdbcTemplate;
	protected ErpBillIdFactory erpBillIdFactory;
	/**
	 * 单据
	 * **/
	protected String orderTable;
	protected String orderDtlTable;
	protected String noticeTable;
	protected String noticeDtlTable;
	protected String mainTable;
	protected String mainDtlTable;
	protected int status;
	public void batchDiffBill(String table,String erpBillType,Bill bill){
		String mainSql = String
				.format("insert %s (item,BillDate,whichprice,"
						+ "Outcode,Incode,types,dhItem,tzItem,"
						+ "num,chnum,mny,cbmny1,"
						+ "flag,status,submit,execution,"
						+ "prints,printnum,mem,gzczy,gzrq,"
						+ "lrczy,lrrq)"
						+ " select ?,?,?,whichprice,"// 2
						+ "Outcode,Incode,0,dhItem,item,"
						+ "num,?,mny,cbmny1,"// 5
						+ "?,?,?,?,"// 9
						+ "0,0,?,?,?,"// 12
						+ "?,?"// 15
						+ " from %s where item=?", table);
		String dtlSql="";
	}
	public void batchAdjustBill(String erpBillType,Bill bill){
		
	}
	
	public JdbcTemplate getPlayloungeJdbcTemplate() {
		return playloungeJdbcTemplate; 
	}
	public void setPlayloungeJdbcTemplate(JdbcTemplate playloungeJdbcTemplate) {
		this.playloungeJdbcTemplate = playloungeJdbcTemplate;
	}
	public ErpBillIdFactory getErpBillIdFactory() {
		return erpBillIdFactory;
	}

	public void setErpBillIdFactory(ErpBillIdFactory erpBillIdFactory) {
		this.erpBillIdFactory = erpBillIdFactory;
	}
}
