package com.casesoft.dmc.extend.playlounge.dao;

import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.extend.playlounge.dao.basic.PlayloungeBasicDao;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成erp单号
 * */
public class ErpBillIdFactory extends PlayloungeBasicDao{
	public static class Table{
		public static final String WAREH_IN="zgsjhd";
		public static final String WAREH_RETURN_OUT="zgsthd";
		public static final String WAREH_TRANSFER_OUT="ycd";
		public static final String WAREH_TRANSFER_IN="yrd";
		public static final String WAREH_OUT="zgchd";
		public static final String WAREH_RETURN_IN="zgtrd";
		public static final String WAREH_OUT_CUST="jxschd";
		public static final String WAREH_CUST_RETURN_IN="jxsthd";
		public static final String WAREH_INVENTORY="pdd";
		
		public static final String SHOP_IN="zgjhd";
		public static final String SHOP_TRANSFER_IN="zrd";
		public static final String SHOP_TRANSFER_OUT="zhd";
		public static final String SHOP_RETURN_OUT="zgthd";
		public static final String SHOP_INVENTORY="zgpdd";
		
		public static final String CUST_OUT="jxschd";
		public static final String CUST_RETURN="";//待定
		
		public static final String DIFF="ckxx_diff";
		public static final String ADJUST="ckalterd";
		
	}
	
	
	
	public String productBillId(String table,String storageId){
		String billNo = null;
		String itemSql="select top 1 Prefix from config_bill where item=?";
 		Map<String, Object> preItem=playloungeJdbcTemplate.queryForMap(itemSql, new Object[] {table});
 		final List<Map<String,Integer>> result=new ArrayList<>();
 		playloungeJdbcTemplate.query("select top 1 billId from config_bill_id where item=? and code=? ",
 				new Object[]{table,storageId}, new RowCallbackHandler() {  
 		      @Override  
 		      public void processRow(ResultSet rs) throws SQLException {  
 		          Map<String,Integer> row = new HashMap<>();  
 		          row.put("billId", rs.getInt("billId"));  
 		         result.add(row);  

 		  }});  
 		int index=0;
		if(CommonUtil.isNotBlank(result)){
			index=result.get(0).get("billId");
 		};
		 billNo= "R"+preItem.get("Prefix")+storageId+"-"+CommonUtil.convertIntToString(index+1, 7);	 
		// addErpBillId(billNo,table,storageId);
		return billNo;
	}
	public void addErpBillId(String billId,String table,String storageId){
		int id=Integer.parseInt(billId.split("-")[billId.split("-").length-1]);
 		final List<Map<String,Integer>> result=new ArrayList<>();
		playloungeJdbcTemplate.query("select top 1 billId from config_bill_id where item=? and code=? ",
 				new Object[]{table,storageId}, new RowCallbackHandler() {  
 		      @Override  
 		      public void processRow(ResultSet rs) throws SQLException {  
 		          Map<String,Integer> row = new HashMap<>();  
 		          row.put("billId", rs.getInt("billId"));  
 		         result.add(row);  

 		  }});
		if(CommonUtil.isBlank(result)){
		 int ind=	playloungeJdbcTemplate.update("insert config_bill_id(billId,item,code) values(?,?,?)", 
					new Object[]{id,table,storageId});
		 System.out.println(ind);
		}else{
			int ind=playloungeJdbcTemplate.update("update config_bill_id set billId=? where item=? and code=?", 
					new Object[]{id,table,storageId});
			 System.out.println(ind);
		}
	}
}
