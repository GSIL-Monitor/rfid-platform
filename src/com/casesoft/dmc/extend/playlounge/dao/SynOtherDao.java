package com.casesoft.dmc.extend.playlounge.dao;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.extend.playlounge.dao.basic.ISynOtherDuty;
import com.casesoft.dmc.extend.playlounge.dao.basic.PlayloungeBasicDao;

import com.casesoft.dmc.extend.third.model.WmsPlRackBindingRelation;
import com.casesoft.dmc.extend.third.model.pl.PlWmsShopBindingRelation;
import com.casesoft.dmc.model.shop.SaleBill;
import com.casesoft.dmc.model.shop.SaleBillDtl;
import com.casesoft.dmc.model.tag.Epc;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SynOtherDao extends PlayloungeBasicDao implements ISynOtherDuty {

	@Override
	public void batchEpc(final List<Epc> epcs) {
		/*String sql = "if not "
				+ " exists( select RFID_Code from codeSync where RFID_Code=?)"
				+ " insert into codeSync(RFID_Code,barcode,lastModify) "
				+ " values(?,?,?)";*/
		String sql =" insert into codeSync(RFID_Code,barcode,lastModify) "
				+ " values(?,?,?)";
		if (CommonUtil.isNotBlank(epcs)) {
			this.playloungeJdbcTemplate.batchUpdate(sql,
					new BatchPreparedStatementSetter() {
						public int getBatchSize() {
							return epcs.size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							Epc epc = (Epc) epcs.get(i);
							ps.setString(1, epc.getEpc());
							ps.setString(2, epc.getSku());
							ps.setTimestamp(3, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd HH:mm:ss")));
						}
					});
			
		}
	}

	@Override
	public void batchFittingRecord() {

	}

	@Override
	public List<SaleBill> downloadSaleInfo() {
		List<SaleBill> saleBills = null;
		try {
			Date endDate = new Date();
			Calendar ca = Calendar.getInstance();
			String end=CommonUtil.getDateString(endDate, "yyyy-MM-dd HH:mm:ss");
			System.out.println("结束："+end);
			ca.setTime(CommonUtil.converStrToDate(
					CommonUtil.getDateString(endDate, "yyyy-MM-dd HH:mm:00"),
					"yyyy-MM-dd HH:mm:ss"));
			ca.add(Calendar.MINUTE, -2);
			String begin=CommonUtil.getDateString(ca.getTime(),"yyyy-MM-dd HH:mm:ss");
			System.out.println("开始："+begin);
			saleBills = this.playloungeJdbcTemplate
					.query("select item,billDate,Outcode,discount,mark,num,cjmny,ysmny,"
							+ "mny,types,mem,gzczy,gzrq,vip,lrrq,lrczy,vipcardno from dj_pos_salesa where flag=1"
							+ " and gzrq between ? and ? ",
							new Object[] { begin, end },
							new RowMapper<SaleBill>() {
								@Override
								public SaleBill mapRow(ResultSet rs, int arg1)
										throws SQLException {
									SaleBill saleBill = new SaleBill();
									saleBill.setId(rs.getString("item"));
									saleBill.setBillNo(rs.getString("item"));
									saleBill.setBillDate(rs.getDate("gzrq"));
									saleBill.setScanTime(CommonUtil
											.getDateString(rs.getDate("lrrq"),
													"yyyy-MM-dd HH:mm:ss"));
									saleBill.setShopId(rs.getString("outcode"));
									saleBill.setOwnerId(rs.getString("outcode"));
									saleBill.setClientId(rs.getString("lrczy"));
									saleBill.setIsRfid(Constant.ScmConstant.IsRfid.IsRfid);
									if (rs.getString("types").equals("0")) {
										saleBill.setType(Constant.ScmConstant.SaleBillType.Outbound);
									} else {
										saleBill.setType(Constant.ScmConstant.SaleBillType.Inbound);
									}
									saleBill.setRemark(rs.getString("mem"));
									saleBill.setClient2Id(rs.getString("vipcardno"));
									saleBill.setTotOrderQty(rs.getLong("num"));
									saleBill.setTotOrderValue(rs
											.getDouble("ysmny"));
									saleBill.setTotActValue(rs
											.getDouble("cjmny"));
									saleBill.setActCashValue(rs
											.getDouble("cjmny"));
									saleBill.setGradeRate(rs
											.getDouble("mark"));
									return saleBill;
								}
							});
			List<SaleBillDtl> lists = this.playloungeJdbcTemplate
					.query("select a.outcode,a.item,a.vip,a.vipcardno,a.gzrq,"
							+ "b.num,b.discount,b.ysmny,"
							+ "b.cjprice,b.memo,"
							+ "b.id,b.clthno,b.color,b.size,b.price"
							+ " from dj_pos_salesa a,dj_pos_salesb b "
							+ " where a.item=b.item and a.flag=1"
							+ " and a.gzrq between ? and ? ",
							new Object[] { begin, end },
							new RowMapper<SaleBillDtl>() {
								@Override
								public SaleBillDtl mapRow(ResultSet rs, int arg1)
										throws SQLException {
									SaleBillDtl saleBillDtl = new SaleBillDtl();
									saleBillDtl.setId(String.valueOf(rs.getInt("id")));
									saleBillDtl.setStyleId(rs.getString("clthno"));
									saleBillDtl.setColorId(rs.getString("color"));
									saleBillDtl.setSizeId(rs.getString("size"));
									saleBillDtl.setClient2Id(rs.getString("vipcardno"));
									saleBillDtl.setBarcode(saleBillDtl.getStyleId()
											+ saleBillDtl.getColorId()
											+ saleBillDtl.getSizeId());
									saleBillDtl.setCode(saleBillDtl.getBarcode());
									saleBillDtl.setPrice(rs.getDouble("price"));
									saleBillDtl.setActPrice(rs.getDouble("ysmny"));
									saleBillDtl.setActValue(rs.getInt("num"));
									saleBillDtl.setQty(rs.getLong("num"));
									saleBillDtl.setBillId(rs.getString("item"));
									saleBillDtl.setBillNo(rs.getString("item"));
									saleBillDtl.setBillDate(rs.getDate("gzrq"));
									saleBillDtl.setPercent(rs.getDouble("discount"));
									saleBillDtl.setUniqueCode(rs.getString("memo"));
									StringBuffer unique=new StringBuffer(rs.getString("outcode"));
									unique.append(rs.getString("clthno")).append(rs.getString("color"));
									PlWmsShopBindingRelation wmsPlRackBindingRelation= CacheManager.getWmsPlRackBindingRelationByUnitCodeSku(unique.toString());
									if(CommonUtil.isNotBlank(wmsPlRackBindingRelation)){
										saleBillDtl.setRackId(wmsPlRackBindingRelation.getRackId());
									}
									return saleBillDtl;
								}
							});
			PlayloungeUtil.addSaleDtl(saleBills, lists);
			System.out.println("结束：");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return saleBills;
		}

	}

}
