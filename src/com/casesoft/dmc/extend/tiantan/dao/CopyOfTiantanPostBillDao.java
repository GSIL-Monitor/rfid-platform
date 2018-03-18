package com.casesoft.dmc.extend.tiantan.dao;

import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.extend.tiantan.dao.basic.ConstantType;
import com.casesoft.dmc.extend.tiantan.dao.basic.ITiantanPostBillDao;
import com.casesoft.dmc.extend.tiantan.dao.basic.TiantanBasicDao;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.task.Business;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Deprecated
public class CopyOfTiantanPostBillDao extends TiantanBasicDao implements ITiantanPostBillDao {
	private final String JS_CODE="";
	private final String JS_NAME="";
	private String mainNoticeTable;
	private String detailNoticeTable;
	private String mainTable;
    private String detailTable;
  

	@Override
	public void batchInBill(final Business bus) {
		final Bill bill=bus.getBill();
		if(CommonUtil.isNotBlank(bill)){
			final List<BillDtl> listBillDtls=TiantanUtil.filterNotScan(bill);
			/*
			 * 更新通知单
			 *js=1;已执行
			 * **/
			String updateBillSql = "update  set js=1,jsrq=?,jsr=? where DJBH=?";
			this.tianTanJdbcTemplate.update(updateBillSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setTimestamp(1, new Timestamp(new Date().getTime()));
							ps.setString(2,JS_NAME);
							ps.setString(3, bill.getBillNo());
						}

					});
			this.tianTanJdbcTemplate.batchUpdate("update JSENDMX set sl=sl_1,sl_2=sl_1 where DJBH=? "
					+ " and spdm=? and gg1dm=? and gg2dm=?", new BatchPreparedStatementSetter() {  
	            public int getBatchSize() {  
	                return listBillDtls.size();  
 	            }  
	            public void setValues(PreparedStatement ps, int i)throws SQLException {  
	            	BillDtl dtl = (BillDtl) listBillDtls.get(i);  
	             /*   ps.setString(1, String.valueOf(dtl.getQty()));
	                ps.setString(2, String.valueOf(dtl.getQty()));*/
	                ps.setString(1, bill.getBillNo());
	                ps.setString(2, dtl.getStyleId());
	                ps.setString(3, dtl.getColorId());
	                ps.setString(4, dtl.getSizeId());
	            }  
	        });  
			/*
			 * 添加入库单
			 * 
			* 选择"insert into SPJHD(DJBH,RQ,YDJH,DJXZ,FPLX,"
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
					+ "QDDM,QYDM,YGDM,SL*?,SL_1,SL_2,SL_3,JE,JE_1,JE_2,JE_3,"
					+ "BZJE,TJ,TJRQ,XC,XCRQ,"
					+ "YS,YSRQ,YSR,JZ,JZRQ,JZR,JS,JSRQ,JSR,SH,SHRQ,SHR,"
					+ "SP,SPRQ,SPR,LL,LLRQ,LLR,ZS,ZSRQ,ZSR,ZDR,YXRQ,RQ_1,"
					+ "RQ_2,RQ_3,RQ_4,BZ,BYZD1,BYZD2,BYZD3,BYZD4,BYZD5,BYZD6,"
					+ "BYZD7,BYZD8,BYZD9,BYZD10,BYZD11,BYZD12,BYZD13,BYZD14,BYZD15"
					+ " from JSEND where djbh=?";
			 * */
			String insertIn="insert into SPJHD(DJBH,RQ,YDJH,DJXZ,FPLX,"
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
					+ "?,?,?,0,?,?,1,?,?,SH,SHRQ,SHR,"
					+ "SP,SPRQ,SPR,LL,LLRQ,LLR,ZS,ZSRQ,ZSR,ZDR,YXRQ,RQ_1,"
					+ "RQ_2,RQ_3,RQ_4,BZ,BYZD1,BYZD2,LXDJ,BYZD4,BYZD5,BYZD6,"
					+ "BYZD7,JE,BYZD9,BYZD10,BYZD11,BYZD12,BYZD13,BYZD14,BYZD15"
					+ " from JSEND where djbh=?";
					
			this.tianTanJdbcTemplate.update(insertIn,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setString(1, bus.getDeviceId()+"_"+bill.getBillNo());//DJBH 单号
							ps.setTimestamp(2, new Timestamp(new Date().getTime()));//RQ 日期
							ps.setDouble(3, bill.getActQty());//SL 执行数量
							ps.setDouble(4, bill.getTotPrice());//JE 金额
							ps.setDouble(5, bill.getTotPrice());//BZJE 标准金额
							ps.setInt(6, 1);//YS 验收
							ps.setTimestamp(7, new Timestamp(new Date().getTime()));//YSRQ 验收日期
							ps.setString(8,JS_NAME);//YES 验收人
							ps.setTimestamp(9, null);//JZRQ 记账日期
							ps.setString(10, null);//JZR 人
							ps.setTimestamp(11, new Timestamp(new Date().getTime()));//JZRQ 执行日期
							ps.setString(12, JS_NAME);//JZRQ 执行人
							ps.setString(13, bill.getBillNo());

						}

					});

		/*	String sql = "insert into SPJHDMX(DJBH,MIBH,MXBH,SPDM,GG1DM,GG2DM,"
					+ "SL,SL_1,SL_2,SL_3,BZSL,CKJ,ZK,DJ,DJ_1,DJ_2,DJ_3,"
					+ "JE,JE_1,JE_2,JE_3,BZJE,BZS,HH,DJH,MIH,MXH,DJH_1,"
					+ "MIH_1,MXH_1,BZ,BYZD1,BYZD2,BYZD3,BYZD4,BYZD5,BYZD6,"
					+ "BYZD7,BYZD8,BYZD9,BYZD10,BYZD11,BYZD12,BYZD13,BYZD14,BYZD15) "
					+ "select DJBH,MIBH,MXBH,SPDM,GG1DM,GG2DM,"
					+ "SL,SL_1,SL_2,SL_3,BZSL,CKJ,ZK,DJ,DJ_1,DJ_2,DJ_3,"
					+ "JE,JE_1,JE_2,JE_3,BZJE,BZS,HH,DJH,MIH,MXH,DJH_1,"
					+ "MIH_1,MXH_1,BZ,BYZD1,BYZD2,BYZD3,BYZD4,BYZD5,BYZD6,"
					+ "BYZD7,BYZD8,BYZD9,BYZD10,BYZD11,BYZD12,BYZD13,BYZD14,BYZD15"
					+ " from JSENDMX where DJbh=? and spdm=? and gg1dm=? and gg2dm=?";*/
			String sql = "insert into SPJHDMX(DJBH,MIBH,MXBH,SPDM,GG1DM,GG2DM,"
					+ "SL,SL_1,SL_2,SL_3,BZSL,CKJ,ZK,DJ,DJ_1,DJ_2,DJ_3,"
					+ "JE,JE_1,JE_2,JE_3,BZJE,BZS,HH,DJH,MIH,MXH,DJH_1,"
					+ "MIH_1,MXH_1,BZ,BYZD1,BYZD2,BYZD3,BYZD4,BYZD5,BYZD6,"
					+ "BYZD7,BYZD8,BYZD9,BYZD10,BYZD11,BYZD12,BYZD13,BYZD14,BYZD15) "
					  + "select ?,MIBH,MXBH,SPDM,GG1DM,GG2DM,"
						+ "?,SL,SL_2,SL_3,BZSL,CKJ,ZK,DJ,DJ_1,DJ_2,DJ_3,"
						+ "DJ*?,JE_1,JE_2,JE_3,DJ*?,BZS,HH,DJH,MIH,MXH,DJH_1,"
						+ "MIH_1,MXH_1,BZ,BYZD1,BYZD2,BYZD3,BYZD4,BYZD5,BYZD6,"
						+ "BYZD7,BYZD8,BYZD9,BYZD10,BYZD11,BYZD12,BYZD13,BYZD14,BYZD15"
						+ " from JSENDMX where DJbh=? and spdm=? and gg1dm=? and gg2dm=?";
			this.tianTanJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public int getBatchSize() {
					return listBillDtls.size();
				}
				@Override
				public void setValues(PreparedStatement ps, int i)
						throws SQLException {
					 BillDtl dtl=listBillDtls.get(i);
					ps.setString(1, bus.getDeviceId()+"_"+bill.getBillNo());//DJBH 单号
					 ps.setDouble(2, dtl.getActQty());
					 ps.setDouble(3, dtl.getActQty());
					 ps.setDouble(4, dtl.getActQty());
				     ps.setString(5, bill.getBillNo());
					 ps.setString(6, dtl.getStyleId());
					 ps.setString(7, dtl.getColorId());
					 ps.setString(8, dtl.getSizeId());
				}
			});
			/*
			 * 
			 * 更改库存
			 * **/
		/*	String chsql = "update SPKCB set SL=sl-? where CKDM=? and SPDM=? and GG1DM=? and GG2DM=?";
			this.tianTanJdbcTemplate.batchUpdate(chsql, new BatchPreparedStatementSetter() {
				@Override
				public int getBatchSize() {
					return listBillDtls.size();
				}
				@Override
				public void setValues(PreparedStatement ps, int i)
						throws SQLException {
					 BillDtl dtl=listBillDtls.get(i);
					 ps.setDouble(1, dtl.getActQty());
 				     ps.setString(2, bill.getUnitId());
					 ps.setString(3, dtl.getStyleId());
					 ps.setString(4, dtl.getColorId());
					 ps.setString(5, dtl.getSizeId());
				}
			});*/
			String chrsql = "update SPKCB set SL=sl+? where CKDM=? and SPDM=? and GG1DM=? and GG2DM=?";
			this.tianTanJdbcTemplate.batchUpdate(chrsql, new BatchPreparedStatementSetter() {
				@Override
				public int getBatchSize() {
					return listBillDtls.size();
				}
				@Override
				public void setValues(PreparedStatement ps, int i)
						throws SQLException {
					 BillDtl dtl=listBillDtls.get(i);
					 ps.setDouble(1, dtl.getActQty());
 				     ps.setString(2, bill.getDestId());//收货方
					 ps.setString(3, dtl.getStyleId());
					 ps.setString(4, dtl.getColorId());
					 ps.setString(5, dtl.getSizeId());
				}
			});
			
		}
		
 	}

	@Override
	public void batchOutBill(final Business bus) {
		final Bill bill=bus.getBill();
		if(CommonUtil.isNotBlank(bill)){
			final List<BillDtl> listBillDtls=TiantanUtil.filterNotScan(bill);
			switch(Integer.parseInt(bill.getId().split("-")[0])){
			case ConstantType.WH_RENTURN_VENDER :
				mainTable="PFXHD";
				detailTable="PFXHDMX";
				mainNoticeTable="FSEND";
				detailNoticeTable="FSENDMX";
				break;
			case ConstantType.WH_RENTURN_SHOP:
				mainTable="SDPHD";
				detailTable="SDPHDMX";
				mainNoticeTable="PSEND";
				detailNoticeTable="PSENDMX";
				break;
			}
		
			
			/*
			 * 更新通知单
			 *js=1;已执行
			 * **/
			String updateBillSql =String.format( "update %s set js=1,jsrq=?,jsr=? where DJBH=?",mainNoticeTable);
			this.tianTanJdbcTemplate.update(updateBillSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setTimestamp(1, new Timestamp(new Date().getTime()));
							ps.setString(2,JS_NAME);
							ps.setString(3, bill.getBillNo());
						}

					});
			this.tianTanJdbcTemplate.batchUpdate(String.format("update %s set sl=sl_1,sl_2=sl_1 where DJBH=? ",detailNoticeTable)
					+ " and spdm=? and gg1dm=? and gg2dm=?", new BatchPreparedStatementSetter() {  
	            public int getBatchSize() {  
	                return listBillDtls.size();  
 	            }  
	            public void setValues(PreparedStatement ps, int i)throws SQLException {  
	            	BillDtl dtl = (BillDtl) listBillDtls.get(i);  
	             /*   ps.setString(1, String.valueOf(dtl.getQty()));
	                ps.setString(2, String.valueOf(dtl.getQty()));*/
	                ps.setString(1, bill.getBillNo());
	                ps.setString(2, dtl.getStyleId());
	                ps.setString(3, dtl.getColorId());
	                ps.setString(4, dtl.getSizeId());
	            }  
	        });  
			/*
			 * 添加入库单
			 * */
			String insertIn=String.format("insert into %s(DJBH,RQ,YDJH,DJXZ,FPLX,"
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
					+ "?,?,?,0,?,?,1,?,?,SH,SHRQ,SHR,"
					+ "SP,SPRQ,SPR,LL,LLRQ,LLR,ZS,ZSRQ,ZSR,ZDR,YXRQ,RQ_1,"
					+ "RQ_2,RQ_3,RQ_4,BZ,BYZD1,BYZD2,LXDJ,BYZD4,BYZD5,BYZD6,"
					+ "BYZD7,JE,BYZD9,BYZD10,BYZD11,BYZD12,BYZD13,BYZD14,BYZD15"
					+ " from %s where djbh=?",mainTable,mainNoticeTable);
					
			this.tianTanJdbcTemplate.update(insertIn,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setString(1, bus.getDeviceId()+"_"+bill.getBillNo());//DJBH 单号
							ps.setTimestamp(2, new Timestamp(new Date().getTime()));//RQ 日期
							ps.setDouble(3, bill.getActQty());//SL 执行数量
							ps.setDouble(4, bill.getTotPrice());//JE 金额
							ps.setDouble(5, bill.getTotPrice());//BZJE 标准金额
							ps.setInt(6, 1);//YS 验收
							ps.setTimestamp(7, new Timestamp(new Date().getTime()));//YSRQ 验收日期
							ps.setString(8,JS_NAME);//YES 验收人
							ps.setTimestamp(9, null);//JZRQ 记账日期
							ps.setString(10, null);//JZR 人
							ps.setTimestamp(11, new Timestamp(new Date().getTime()));//JZRQ 执行日期
							ps.setString(12, JS_NAME);//JZRQ 执行人
							ps.setString(13, bill.getBillNo());

						}

					});

			String sql = String.format("insert into %s(DJBH,MIBH,MXBH,SPDM,GG1DM,GG2DM,"
					+ "SL,SL_1,SL_2,SL_3,BZSL,CKJ,ZK,DJ,DJ_1,DJ_2,DJ_3,"
					+ "JE,JE_1,JE_2,JE_3,BZJE,BZS,HH,DJH,MIH,MXH,DJH_1,"
					+ "MIH_1,MXH_1,BZ,BYZD1,BYZD2,BYZD3,BYZD4,BYZD5,BYZD6,"
					+ "BYZD7,BYZD8,BYZD9,BYZD10,BYZD11,BYZD12,BYZD13,BYZD14,BYZD15) "
					  + "select ?,MIBH,MXBH,SPDM,GG1DM,GG2DM,"
						+ "?,SL,SL_2,SL_3,BZSL,CKJ,ZK,DJ,DJ_1,DJ_2,DJ_3,"
						+ "DJ*?,JE_1,JE_2,JE_3,DJ*?,BZS,HH,DJH,MIH,MXH,DJH_1,"
						+ "MIH_1,MXH_1,BZ,BYZD1,BYZD2,BYZD3,BYZD4,BYZD5,BYZD6,"
						+ "BYZD7,BYZD8,BYZD9,BYZD10,BYZD11,BYZD12,BYZD13,BYZD14,BYZD15"
						+ " from %s where DJbh=? and spdm=? and gg1dm=? and gg2dm=?",detailTable,detailNoticeTable);
			this.tianTanJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public int getBatchSize() {
					return listBillDtls.size();
				}
				@Override
				public void setValues(PreparedStatement ps, int i)
						throws SQLException {
					 BillDtl dtl=listBillDtls.get(i);
					ps.setString(1, bus.getDeviceId()+"_"+bill.getBillNo());//DJBH 单号
					 ps.setDouble(2, dtl.getActQty());
					 ps.setDouble(3, dtl.getActQty());
					 ps.setDouble(4, dtl.getActQty());
				     ps.setString(5, bill.getBillNo());
					 ps.setString(6, dtl.getStyleId());
					 ps.setString(7, dtl.getColorId());
					 ps.setString(8, dtl.getSizeId());
				}
			});
			/*
			 * 
			 * 更改库存
			 * **/
			String chsql = "update SPKCB set SL=sl-? where CKDM=? and SPDM=? and GG1DM=? and GG2DM=?";
			this.tianTanJdbcTemplate.batchUpdate(chsql, new BatchPreparedStatementSetter() {
				@Override
				public int getBatchSize() {
					return listBillDtls.size();
				}
				@Override
				public void setValues(PreparedStatement ps, int i)
						throws SQLException {
					 BillDtl dtl=listBillDtls.get(i);
					 ps.setDouble(1, dtl.getActQty());
 				     ps.setString(2, bill.getOrigId());
					 ps.setString(3, dtl.getStyleId());
					 ps.setString(4, dtl.getColorId());
					 ps.setString(5, dtl.getSizeId());
				}
			});
		/*	String chrsql = "update SPKCB set SL=sl-? where CKDM=? and SPDM=? and GG1DM=? and GG2DM=?";
			this.tianTanJdbcTemplate.batchUpdate(chrsql, new BatchPreparedStatementSetter() {
				@Override
				public int getBatchSize() {
					return listBillDtls.size();
				}
				@Override
				public void setValues(PreparedStatement ps, int i)
						throws SQLException {
					 BillDtl dtl=listBillDtls.get(i);
					 ps.setDouble(1, dtl.getActQty());
 				     ps.setString(2, bill.getUnit2Id());
					 ps.setString(3, dtl.getStyleId());
					 ps.setString(4, dtl.getColorId());
					 ps.setString(5, dtl.getSizeId());
				}
			});*/
			
		}
		
	}

	@Override
	public void batchReturnInBill(Business bus) {
		
	}

	@Override
	public void batchReturnOutBill(Business bus) {
	}

	@Override
	public void batchTransferInBill(Business bus) {
		
	}

	@Override
	public void batchTransferOutBill(Business bus) {
	}

	@Override
	public void batchInventory(Business business) {
	}

	@Override
	public void batchShopInBill(Business bus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void batchShopTransferInBill(Business bus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void batchShopReturnOutBill(Business bus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void batchInFromFactoryBill(Business bus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void batchInventoryBill(Business bus) {
		// TODO Auto-generated method stub
		
	}

}
