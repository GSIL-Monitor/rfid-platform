package com.casesoft.dmc.extend.playlounge.dao;

import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.extend.playlounge.dao.basic.IPlayloungeShopPostBillDao;
import com.casesoft.dmc.extend.playlounge.dao.basic.PlayloungeBasicDao;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.BusinessDtl;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlayloungeShopPostBillDao extends PlayloungeBasicDao implements
		IPlayloungeShopPostBillDao {
	@Override
	public void batchShopInBill(final Business bus) {
		final Bill bill = bus.getBill();
		if (CommonUtil.isNotBlank(bill)) {
			status = bill.getStatus();
			noticeTable = "dj_zgchda";
			noticeDtlTable = "dj_zgchdb";
			mainTable = "dj_zgjhda";
			mainDtlTable = "dj_zgjhdb";
			status = bill.getStatus();

			final List<BillDtl> listBillDtls = PlayloungeUtil
					.filterNotScan(bill);
			final List<BusinessDtl> listBusDtls = bus.getDtlList();

			/***
			 * 2. 更改通知单
			 * */
			if (bill.getActQty().longValue() == bill.getTotQty().longValue()||bill.getStatus()==2) {

				String noticeSql = String.format(
						"update %s set chnum=(case when chnum IS  NULL  then 0 else chnum end)+?"
								+ ",accept=?,acceptDate=? where item=? ",
						noticeTable);
				int ni = this.playloungeJdbcTemplate.update(noticeSql,
						new PreparedStatementSetter() {
							@Override
							public void setValues(PreparedStatement ps)
									throws SQLException {
								ps.setLong(1, bus.getTotEpc());
								if (bill.getActQty().longValue() == bill
										.getTotQty().longValue()||bill.getStatus()==2) {
									ps.setString(2, "1");
								} else {
									ps.setString(2, "0");
								}
								ps.setTimestamp(3, Timestamp.valueOf(CommonUtil
										.getDateString(new Date(),
												"yyyy-MM-dd 00:00:00")));
								ps.setString(4, bill.getSrcBillNo());
							}
						});
				String noticeDtlSql = String
						.format("update %s set chnum=(case when chnum IS NULL   then 0 else chnum end)+?"
								+ "  where clthno=? and color=? and size=? and item=?",
								noticeDtlTable);
				int[] ndi = this.playloungeJdbcTemplate.batchUpdate(
						noticeDtlSql, new BatchPreparedStatementSetter() {
							public int getBatchSize() {
								return listBusDtls.size();
							}

							public void setValues(PreparedStatement ps, int i)
									throws SQLException {
								BusinessDtl dtl = (BusinessDtl) listBusDtls
										.get(i);
								ps.setLong(1, dtl.getQty());
								ps.setString(2, dtl.getStyleId());
								ps.setString(3, dtl.getColorId());
								ps.setString(4, dtl.getSizeId());
								ps.setString(5, bill.getBillNo());
							}
						});
			}
			/**
			 * 3.添加出库单
			 * */
			/*
			 * final String
			 * billNo=erpBillIdFactory.productBillId(ErpBillIdFactory
			 * .Table.WAREH_OUT, bill.getOrigId());
			 */
			String mainSql = String
					.format("insert %s (accept,item,BillDate,whichprice,"
							+ "Outcode,Incode,types,dhItem,tzItem,"
							+ "num,chnum,mny,cbmny1,"
							+ "flag,status,submit,execution,"
							+ "prints,printnum,mem,gzczy,gzrq,"
							+ "lrczy,lrrq)"
							+ " select ?,?,?,whichprice,"// 2
							+ "Outcode,Incode,3,dhItem,item,"
							+ "num,?,mny,cbmny1,"// 5
							+ "?,?,?,?,"// 9
							+ "0,0,(case when mem IS NULL   then '' else mem end)+?,?,?,"// 12
							+ "?,?"// 15
							+ " from %s where item=?", mainTable, noticeTable);
			int mi = this.playloungeJdbcTemplate.update(mainSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							if (bill.getActQty().longValue() == bill
									.getTotQty().longValue()||bill.getStatus()==2) {
								ps.setString(1, "1");// accept
							} else {
								ps.setString(1, "0");// accept

							}
							ps.setString(2, bill.getSrcBillNo());
							ps.setTimestamp(3, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setLong(4, bill.getScanQty().longValue());
							/*
							 * ps.setDouble(4, bill.getTotPrice());
							 * ps.setDouble(5, bill.getTotPrePrice());
							 */
							if (bill.getActQty().longValue() == bill
									.getTotQty().longValue()||bill.getStatus()==2) {
								ps.setString(5, "1");// 设置审核flage
								ps.setString(7, "1");// 设置审核flage
								if (CommonUtil.isBlank(bus.getOperator())) {
									ps.setString(10, bus.getDeviceId());
								} else {
									ps.setString(10, bus.getOperator());
								}
								ps.setTimestamp(11,
										new Timestamp(new Date().getTime()));

							} else {
								ps.setString(5, "0");// 设置审核flage
								ps.setString(7, "0");// 设置提交submit
								ps.setString(10, null);
								ps.setTimestamp(11, null);
							}
							ps.setString(6, "0");// 设置中止status
							ps.setString(8, "0");// 设置执行execution
							ps.setString(9, (CommonUtil.isBlank(bill.getRemark())?"":bill.getRemark()));
							if (CommonUtil.isBlank(bus.getOperator())) {
								ps.setString(12, bus.getDeviceId());
							} else {
								ps.setString(12, bus.getOperator());
							}
							ps.setTimestamp(13,
									new Timestamp(new Date().getTime()));
							ps.setString(14, bill.getSrcBillNo());
						}

					});
			String mainDtl = String
					.format("insert %s (item,clthno,color,size,price,cb1,"
							+ "num,kcnum,dhnum,"
							+ "dhwcnum,kpnum,chnum) "
							+ " select ?,clthno,color,size,Price1,Cost1,"
							+ " ?,num,?,?,?,? "
							+ " from (select s.Whcode, s.num,a.Price1,a.clthno,a.color,a.size,a.Cost1"
							+ "  from (select s.Price1,s.Cost1,s.Cost6,"
							+ " s.clthno,i.size,c.color "
							+ " from jbCloth s,jbClothSize i,jbClothColor c "
							+ " where s.clthno=i.clthno and c.clthno=s.clthno and i.clthno=c.clthno) a left join store s "
							+ " on s.color=a.color and s.clthno=a.clthno and a.size=s.size "
							+ "  and Whcode=?) a where clthno=? and  color=? and  size=?",
							mainDtlTable);
			int[] mdi = this.playloungeJdbcTemplate.batchUpdate(mainDtl,
					new BatchPreparedStatementSetter() {
						public int getBatchSize() {
							return listBillDtls.size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = (BillDtl) listBillDtls.get(i);
							ps.setString(1, bill.getSrcBillNo());
							ps.setDouble(2, dtl.getQty().longValue());
							ps.setDouble(3, dtl.getQty().longValue());
							ps.setDouble(4, dtl.getQty().longValue());
							ps.setDouble(5, dtl.getScanQty().longValue());
							ps.setDouble(6, dtl.getScanQty().longValue());
							ps.setString(7, bill.getDestId());
							ps.setString(8, dtl.getStyleId());
							ps.setString(9, dtl.getColorId());
							ps.setString(10, dtl.getSizeId());

						}
					});
			/*
			 * erpBillIdFactory.addErpBillId(billNo,
			 * ErpBillIdFactory.Table.WAREH_OUT, bill.getOrigId());
			 */
			/*
			 * 
			 * 更改库存 *
			 */
			if (bill.getActQty().longValue() == bill.getTotQty().longValue()||bill.getStatus()==2) {

				String chrsql = "update store   set num=IsNull(num ,0) + ?, zjnum = IsNull(zjnum,0) +?,"
						+ "waynum=IsNull(waynum ,0) - ?,"
						+ "indate=(case when indate IS NULL   then ? else indate end)"
						+ " WHERE Whcode= ? AND clthno = ? AND color =? AND size = ?";
				int[] update = this.playloungeJdbcTemplate.batchUpdate(chrsql,
						new BatchPreparedStatementSetter() {
							@Override
							public int getBatchSize() {
								return listBillDtls.size();
							}

							@Override
							public void setValues(PreparedStatement ps, int i)
									throws SQLException {
								BillDtl dtl = listBillDtls.get(i);
								ps.setDouble(1, dtl.getScanQty().longValue());
								ps.setDouble(2, dtl.getScanQty().longValue());
								ps.setDouble(3, dtl.getScanQty().longValue());

								ps.setTimestamp(4, Timestamp.valueOf(CommonUtil
										.getDateString(new Date(),
												"yyyy-MM-dd 00:00:00")));
								ps.setString(5, bill.getDestId());// 收货方
								ps.setString(6, dtl.getStyleId());
								ps.setString(7, dtl.getColorId());
								ps.setString(8, dtl.getSizeId());
							}
						});
				final List<BillDtl> tempDtls = new ArrayList<>();
				for (int i = 0; i < update.length; i++) {
					if (update[i] < 1) {
						tempDtls.add(listBillDtls.get(i));
					}
				}
				if (tempDtls.size() > 0) {
					String chrtempsql = "insert store(num,zjnum,Whcode,clthno,color,size,indate) values(?,?,?,?,?,?,?)";
					this.playloungeJdbcTemplate.batchUpdate(chrtempsql,
							new BatchPreparedStatementSetter() {
								@Override
								public int getBatchSize() {
									return tempDtls.size();
								}

								@Override
								public void setValues(PreparedStatement ps,
										int i) throws SQLException {
									BillDtl dtl = tempDtls.get(i);
									ps.setDouble(1, dtl.getScanQty()
											.longValue());
									ps.setDouble(2, dtl.getScanQty()
											.longValue());
									ps.setString(3, bill.getDestId());// 收货方
									ps.setString(4, dtl.getStyleId());
									ps.setString(5, dtl.getColorId());
									ps.setString(6, dtl.getSizeId());
									ps.setTimestamp(7, Timestamp
											.valueOf(CommonUtil.getDateString(
													new Date(),
													"yyyy-MM-dd 00:00:00")));
								}
							});
				}
				;
			}
		}
	}

	@Override
	public void batchShopTransferInBill(final Business bus) {
		final Bill bill = bus.getBill();
		if (CommonUtil.isNotBlank(bill)) {
			status = bill.getStatus();
			noticeTable = "dj_zhda";
			noticeDtlTable = "dj_zhdb";
			mainTable = "dj_zrda";
			mainDtlTable = "dj_zrdb";
			status = bill.getStatus();

			final List<BillDtl> listBillDtls = PlayloungeUtil
					.filterNotScan(bill);
			final List<BusinessDtl> listBusDtls = bus.getDtlList();

			/***
			 * 2. 更改通知单
			 * */
			if (bill.getActQty().longValue() == bill.getTotQty().longValue()||bill.getStatus()==2) {

				String noticeSql = String.format(
						"update %s set chnum=(case when chnum IS  NULL  then 0 else chnum end)+?"
								+ ",accept=?,acceptDate=? where item=? ",
						noticeTable);
				int ni = this.playloungeJdbcTemplate.update(noticeSql,
						new PreparedStatementSetter() {
							@Override
							public void setValues(PreparedStatement ps)
									throws SQLException {
								ps.setLong(1, bus.getTotEpc());
								if (bill.getActQty().longValue() == bill
										.getTotQty().longValue()||bill.getStatus()==2) {
									ps.setString(2, "1");
								} else {
									ps.setString(2, "0");
								}
								ps.setTimestamp(3, Timestamp.valueOf(CommonUtil
										.getDateString(new Date(),
												"yyyy-MM-dd 00:00:00")));
								ps.setString(4, bill.getSrcBillNo());
							}
						});
				String noticeDtlSql = String
						.format("update %s set chnum=(case when chnum IS NULL   then 0 else chnum end)+?"
								+ "  where clthno=? and color=? and size=? and item=?",
								noticeDtlTable);
				int[] ndi = this.playloungeJdbcTemplate.batchUpdate(
						noticeDtlSql, new BatchPreparedStatementSetter() {
							public int getBatchSize() {
								return listBusDtls.size();
							}

							public void setValues(PreparedStatement ps, int i)
									throws SQLException {
								BusinessDtl dtl = (BusinessDtl) listBusDtls
										.get(i);
								ps.setLong(1, dtl.getQty());
								ps.setString(2, dtl.getStyleId());
								ps.setString(3, dtl.getColorId());
								ps.setString(4, dtl.getSizeId());
								ps.setString(5, bill.getSrcBillNo());
							}
						});
			}
			/**
			 * 3.添加出库单
			 * */
			/*
			 * final String
			 * billNo=erpBillIdFactory.productBillId(ErpBillIdFactory
			 * .Table.WAREH_OUT, bill.getOrigId());
			 */
			String mainSql = String
					.format("insert %s (accept,item,BillDate,whichprice,"
							+ "Outcode,Incode,types,dhItem,tzItem,"
							+ "num,chnum,mny,cbmny1,"
							+ "flag,status,submit,execution,"
							+ "prints,printnum,mem,gzczy,gzrq,"
							+ "lrczy,lrrq)"
							+ " select ?,?,?,whichprice,"// 2
							+ "Outcode,Incode,3,dhItem,item,"
							+ "num,?,mny,cbmny1,"// 5
							+ "?,?,?,?,"
							+ "0,0,(case when mem IS NULL then '' else mem end)+?,?,?,"// 9
							+ "?,?"// 15
							+ " from %s where item=?", mainTable, noticeTable);
			int mi = this.playloungeJdbcTemplate.update(mainSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							if (bill.getActQty().longValue() == bill
									.getTotQty().longValue()||bill.getStatus()==2) {
								ps.setString(1, "1");// accept
								ps.setString(5, "1");// 设置审核flage
								ps.setString(7, "1");// 设置提交submit
								if (CommonUtil.isBlank(bus.getOperator())) {
									ps.setString(10, bus.getDeviceId());
								} else {
									ps.setString(10, bus.getOperator());
								}
								ps.setTimestamp(11,
										new Timestamp(new Date().getTime()));
							} else {
								ps.setString(1, "0");// accept
								ps.setString(5, "0");// 设置审核flage
								ps.setString(7, "0");// 设置提交submit
								ps.setString(10, null);
								ps.setTimestamp(11, null);
							}
							ps.setString(2, bill.getSrcBillNo());
							ps.setTimestamp(3, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setLong(4, bill.getScanQty().longValue());
							/*
							 * ps.setDouble(4, bill.getTotPrice());
							 * ps.setDouble(5, bill.getTotPrePrice());
							 */
							ps.setString(6, "0");// 设置中止status
							ps.setString(8, "0");// 设置执行execution
							ps.setString(9,  (CommonUtil.isBlank(bill.getRemark())?"":bill.getRemark()));

							if (CommonUtil.isBlank(bus.getOperator())) {
								ps.setString(12, bus.getDeviceId());
							} else {
								ps.setString(12, bus.getOperator());
							}
							ps.setTimestamp(13,
									new Timestamp(new Date().getTime()));
							ps.setString(14, bill.getSrcBillNo());
						}

					});
			String mainDtl = String
					.format("insert %s (item,clthno,color,size,price,cb1,"
							+ "num,kcnum,dhnum,"
							+ "dhwcnum,kpnum,chnum) "
							+ " select ?,clthno,color,size,Price1,Cost1,"
							+ " ?,num,?,?,?,? "
							+ " from (select s.Whcode, s.num,a.Price1,a.clthno,a.color,a.size,a.Cost1"
							+ "  from (select s.Price1,s.Cost1,s.Cost6,"
							+ " s.clthno,i.size,c.color "
							+ " from jbCloth s,jbClothSize i,jbClothColor c "
							+ " where s.clthno=i.clthno and c.clthno=s.clthno and i.clthno=c.clthno) a left join store s "
							+ " on s.color=a.color and s.clthno=a.clthno and a.size=s.size "
							+ " and Whcode=?) a where clthno=? and  color=? and  size=? ",
							mainDtlTable);
			int[] mdi = this.playloungeJdbcTemplate.batchUpdate(mainDtl,
					new BatchPreparedStatementSetter() {
						public int getBatchSize() {
							return listBillDtls.size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = (BillDtl) listBillDtls.get(i);
							ps.setString(1, bill.getSrcBillNo());
							ps.setDouble(2, dtl.getQty().longValue());
							ps.setDouble(3, dtl.getQty().longValue());
							ps.setDouble(4, dtl.getQty().longValue());
							ps.setDouble(5, dtl.getScanQty().longValue());
							ps.setDouble(6, dtl.getScanQty().longValue());
							ps.setString(7, bill.getDestId());
							ps.setString(8, dtl.getStyleId());
							ps.setString(9, dtl.getColorId());
							ps.setString(10, dtl.getSizeId());

						}
					});
			/*
			 * erpBillIdFactory.addErpBillId(billNo,
			 * ErpBillIdFactory.Table.WAREH_OUT, bill.getOrigId());
			 */
			/*
			 * 
			 * 更改库存 *
			 */
			if (bill.getActQty().longValue() == bill.getTotQty().longValue()||bill.getStatus()==2) {
				String chrsql = "update store   set num=IsNull(num ,0) + ?,yrnum=IsNull(yrnum ,0) + ?,waynum=IsNull(waynum ,0) - ?,"
						+ "indate=(case when indate IS NULL  "
						+ " then indate else ? end )"
						+ " WHERE Whcode= ? AND clthno = ? AND color =? AND size = ?";
				int[] update = this.playloungeJdbcTemplate.batchUpdate(chrsql,
						new BatchPreparedStatementSetter() {
							@Override
							public int getBatchSize() {
								return listBillDtls.size();
							}

							@Override
							public void setValues(PreparedStatement ps, int i)
									throws SQLException {
								BillDtl dtl = listBillDtls.get(i);
								ps.setDouble(1, dtl.getScanQty().longValue());
								ps.setDouble(2, dtl.getScanQty().longValue());
								ps.setDouble(3, dtl.getScanQty().longValue());

								ps.setTimestamp(4, Timestamp.valueOf(CommonUtil
										.getDateString(new Date(),
												"yyyy-MM-dd 00:00:00")));
								ps.setString(5, bill.getDestId());// 收货方
								ps.setString(6, dtl.getStyleId());
								ps.setString(7, dtl.getColorId());
								ps.setString(8, dtl.getSizeId());
							}
						});
				final List<BillDtl> tempDtls = new ArrayList<>();
				for (int i = 0; i < update.length; i++) {
					if (update[i] < 1) {
						tempDtls.add(listBillDtls.get(i));
					}
				}
				if (tempDtls.size() > 0) {
					String chrtempsql = "insert store(num,yrnum,Whcode,clthno,color,size,indate) values(?,?,?,?,?,?,?)";
					this.playloungeJdbcTemplate.batchUpdate(chrtempsql,
							new BatchPreparedStatementSetter() {
								@Override
								public int getBatchSize() {
									return tempDtls.size();
								}

								@Override
								public void setValues(PreparedStatement ps,
										int i) throws SQLException {
									BillDtl dtl = tempDtls.get(i);
									ps.setDouble(1, dtl.getScanQty()
											.longValue());
									ps.setDouble(2, dtl.getScanQty()
											.longValue());
									ps.setString(3, bill.getDestId());// 收货方
									ps.setString(4, dtl.getStyleId());
									ps.setString(5, dtl.getColorId());
									ps.setString(6, dtl.getSizeId());
									ps.setTimestamp(7, Timestamp
											.valueOf(CommonUtil.getDateString(
													new Date(),
													"yyyy-MM-dd 00:00:00")));
								}
							});
				}
				;
			}

		}
	}

	@Override
	public void batchShopTransferOutBill(final Business bus) {
		final Bill bill = bus.getBill();
		if (CommonUtil.isNotBlank(bill)) {
			status = bill.getStatus();
			noticeTable = "dj_zhtzda";
			noticeDtlTable = "dj_zhtzdb";
			mainTable = "dj_zhda";
			mainDtlTable = "dj_zhdb";
			status = bill.getStatus();

			final List<BillDtl> listBillDtls = PlayloungeUtil
					.filterNotScan(bill);

			/***
			 * 2. 更改通知单
			 * */
			String noticeSql = PostSqlUtil.getChNoticeSql(noticeTable);
			int ni = this.playloungeJdbcTemplate.update(noticeSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setLong(1, bill.getScanQty().longValue());
							if (status == 2
									|| (bill.getActQty().longValue() == bill
											.getTotQty().longValue())) {
								ps.setString(2, "1");
							} else {
								ps.setString(2, "0");
							}
							ps.setString(3, "1");
							ps.setString(4, bill.getBillNo());
						}
					});
			String noticeDtlSql = PostSqlUtil.getChNoticeDtlSql(noticeDtlTable);
			int[] ndi = this.playloungeJdbcTemplate.batchUpdate(noticeDtlSql,
					new BatchPreparedStatementSetter() {
						public int getBatchSize() {
							return listBillDtls.size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = (BillDtl) listBillDtls.get(i);
							ps.setLong(1, dtl.getScanQty().longValue());
							ps.setString(2, dtl.getStyleId());
							ps.setString(3, dtl.getColorId());
							ps.setString(4, dtl.getSizeId());
							ps.setString(5, bill.getBillNo());
						}
					});
			/**
			 * 3.添加出库单
			 * */
			final String billNo = erpBillIdFactory.productBillId(
					ErpBillIdFactory.Table.SHOP_TRANSFER_OUT, bill.getOrigId());
			String mainSql = String
					.format("insert %s (accept,item,BillDate,whichprice,"
							+ "Outcode,Incode,types,dhItem,tzItem,"
							+ "num,mny,cbmny1,"
							+ "flag,status,submit,execution,"
							+ "prints,printnum,mem,"
							+ "gzczy,gzrq,lrczy,lrrq)"
							+ " select ?,?,?,whichprice,"// 2
							+ "Outcode,Incode,3,dhItem,item,"
							+ "?,?,?,"// 5
							+ "?,?,?,?,"// 9
							+ "0,0,(case when mem IS NULL   then '' else mem end)+?,"// 12
							+ "?,?,?,?"// 15
							+ " from %s where item=?", mainTable, noticeTable);
			int mi = this.playloungeJdbcTemplate.update(mainSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setString(1, "0");
							ps.setString(2, billNo);
							ps.setTimestamp(3, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setLong(4, bill.getScanQty().longValue());
							ps.setDouble(5, bill.getTotPrice());
							ps.setDouble(6, bill.getTotPrePrice());
							ps.setString(7, "1");// 设置审核flage
							ps.setString(8, "0");// 设置中止status
							ps.setString(9, "1");// 设置提交submit
							ps.setString(10, "0");// 设置执行execution
							ps.setString(11, (CommonUtil.isBlank(bill.getRemark())?"":bill.getRemark()));
							if (CommonUtil.isBlank(bus.getOperator())) {
								ps.setString(12, bus.getDeviceId());
								ps.setString(14, bus.getDeviceId());
							} else {
								ps.setString(12, bus.getOperator());
								ps.setString(14, bus.getDeviceId());
							}
							ps.setTimestamp(13,
									new Timestamp(new Date().getTime()));
							ps.setTimestamp(15,
									new Timestamp(new Date().getTime()));
							ps.setString(16, bill.getBillNo());
						}

					});
			String mainDtl = String
					.format("insert %s (item,clthno,color,size,price,cb1,"
							+ "num,kcnum,dhnum,"
							+ "dhwcnum,kpnum) "
							+ " select ?,clthno,color,size,Price1,Cost1,"
							+ " ?,num,?,?,? "
							+ " from (select s.Whcode, s.num,a.Price1,a.clthno,a.color,a.size,a.Cost1"
							+ "  from (select s.Price1,s.Cost1,s.Cost6,"
							+ " s.clthno,i.size,c.color "
							+ " from jbCloth s,jbClothSize i,jbClothColor c "
							+ " where s.clthno=i.clthno and c.clthno=s.clthno and i.clthno=c.clthno) a left join store s "
							+ " on s.color=a.color and s.clthno=a.clthno and a.size=s.size "
							+ " ) a where clthno=? and  color=? and  size=? and Whcode=?",
							mainDtlTable);
			int[] mdi = this.playloungeJdbcTemplate.batchUpdate(mainDtl,
					new BatchPreparedStatementSetter() {
						public int getBatchSize() {
							return listBillDtls.size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = (BillDtl) listBillDtls.get(i);
							ps.setString(1, billNo);
							ps.setDouble(2, dtl.getScanQty().longValue());
							ps.setDouble(3, dtl.getQty().longValue());
							ps.setDouble(4, dtl.getQty().longValue());
							ps.setDouble(5, dtl.getScanQty().longValue());
							ps.setString(6, dtl.getStyleId());
							ps.setString(7, dtl.getColorId());
							ps.setString(8, dtl.getSizeId());
							ps.setString(9, bill.getOrigId());
						}
					});
			erpBillIdFactory.addErpBillId(billNo,
					ErpBillIdFactory.Table.SHOP_TRANSFER_OUT, bill.getOrigId());
			bill.setSrcBillNo(billNo);
			bus.setSrcBillNo(billNo);

			/*
			 * 添加库存 *
			 */
			String chrsql = "update store   set num=IsNull(num ,0) - ?, ycnum = IsNull(ycnum,0) +?"
					+ " WHERE Whcode= ? AND clthno = ? AND color =? AND size = ?";
			int[] update = this.playloungeJdbcTemplate.batchUpdate(chrsql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBillDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = listBillDtls.get(i);
							ps.setLong(1, dtl.getScanQty().longValue());
							ps.setLong(2, dtl.getScanQty().longValue());

							ps.setString(3, bill.getOrigId());// 收货方
							ps.setString(4, dtl.getStyleId());
							ps.setString(5, dtl.getColorId());
							ps.setString(6, dtl.getSizeId());
						}
					});
			String chrInsql = "update store   set waynum=IsNull(waynum ,0) + ? "
					+ " WHERE Whcode= ? AND clthno = ? AND color =? AND size = ?";
			int[] updateIn = this.playloungeJdbcTemplate.batchUpdate(chrInsql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBillDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = listBillDtls.get(i);
							ps.setLong(1, dtl.getScanQty().longValue());
							ps.setString(2, bill.getDestId());// 收货方
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
						}
					});

			final List<BillDtl> tempDtls = new ArrayList<>();
			for (int i = 0; i < updateIn.length; i++) {
				if (updateIn[i] < 1) {
					tempDtls.add(listBillDtls.get(i));
				}
			}
			if (tempDtls.size() > 0) {
				String chrtempsql = "insert store(waynum,Whcode,clthno,color,size,indate) values(?,?,?,?,?,?)";
				this.playloungeJdbcTemplate.batchUpdate(chrtempsql,
						new BatchPreparedStatementSetter() {
							@Override
							public int getBatchSize() {
								return tempDtls.size();
							}

							@Override
							public void setValues(PreparedStatement ps, int i)
									throws SQLException {
								BillDtl dtl = tempDtls.get(i);
								ps.setLong(1, dtl.getScanQty().longValue());
								ps.setString(2, bill.getDestId());// 收货方
								ps.setString(3, dtl.getStyleId());
								ps.setString(4, dtl.getColorId());
								ps.setString(5, dtl.getSizeId());
								ps.setTimestamp(6, null);

							}
						});
			}
			;
		}
	}

	@Override
	public void batchShopInventory(final Business bus) {
		mainTable = "dj_zgpdda";
		mainDtlTable = "dj_zgpddb";
		final Bill bill = bus.getBill();
		if (CommonUtil.isNotBlank(bill)) {
			final List<BillDtl> listBillDtls = PlayloungeUtil
					.filterNotScanByActQty(bill);

			final String billNo = erpBillIdFactory.productBillId(
					ErpBillIdFactory.Table.SHOP_INVENTORY, bill.getOrigId());
			String mainSql = String.format(
					"insert %s (item,BillDate,whichprice," + "Outcode,types,"
							+ "num,mny,cbmny1,"

							+ "prints,printnum,mem," + "gzczy,gzrq,lrczy,lrrq,"
							+ "flag,status,submit,execution)"
							+ " values(?,?,1,"// 2
							+ "?,0,"// 3
							+ "?,?,?,"// 6
							+ "0,0,?,"// 7
							+ "?,?,?,?," + "?,?,?,?)"// 9
					, mainTable);

			String mainDtl = String
					.format("insert %s (item,clthno,color,size,price,cb1,"
							+ "num) "
							+ " select ?,clthno,color,size,Price1,Cost1,"
							+ " ? "
							+ " from (select s.Whcode, s.num,a.Price1,a.clthno,a.color,a.size,a.Cost1"
							+ "  from (select s.Price1,s.Cost1,s.Cost6,"
							+ " s.clthno,i.size,c.color "
							+ " from jbCloth s,jbClothSize i,jbClothColor c "
							+ " where s.clthno=i.clthno and c.clthno=s.clthno and i.clthno=c.clthno) a left join store s "
							+ " on s.color=a.color and s.clthno=a.clthno and a.size=s.size and s.Whcode=?"
							+ " ) a where clthno=? and  color=? and  size=? ",
							mainDtlTable);
 			int[] mdi = this.playloungeJdbcTemplate.batchUpdate(mainDtl,
					new BatchPreparedStatementSetter() {
						public int getBatchSize() {
							return listBillDtls.size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = (BillDtl) listBillDtls.get(i);
							ps.setString(1, billNo);
							ps.setDouble(2, dtl.getActQty());
							ps.setString(3, bill.getOrigId());
							ps.setString(4, dtl.getStyleId());
							ps.setString(5, dtl.getColorId());
							ps.setString(6, dtl.getSizeId());

						}
					});
 			for(int i=0;i<mdi.length;i++){
 				if(mdi[i]==0){
 					bill.setActQty(bill.getActQty()-listBillDtls.get(i).getActQty());
				}
			}
			int mi = this.playloungeJdbcTemplate.update(mainSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setString(1, billNo);
							ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
									.getDateString(bill.getBillDate(),
											"yyyy-MM-dd 00:00:00")));
							ps.setString(3, bill.getOrigId());
							ps.setLong(4, bill.getActQty().longValue());
							ps.setDouble(5, bill.getTotPrice());
							ps.setDouble(6, bill.getTotPrePrice());
							System.out.println("备注："+bill.getRemark());
							ps.setString(7,(CommonUtil.isBlank(bill.getRemark())?"":bill.getRemark()));
/*							if (CommonUtil.isBlank(bus.getOperator())) {
*/								ps.setString(8, bill.getDeliverNo());
							ps.setString(10, bill.getDeliverNo());

						/*	} else {
								ps.setString(8, bus.getOperator());
								ps.setString(10, bus.getOperator());

							}*/
							ps.setTimestamp(9,
									new Timestamp(new Date().getTime()));
							ps.setTimestamp(11,
									new Timestamp(new Date().getTime()));
							ps.setString(12, "1");// 设置审核flage
							ps.setString(13, "0");// 设置中止status
							ps.setString(14, "1");// 设置提交submit
							ps.setString(15, "0");// 设置执行execution
						}

					});
			erpBillIdFactory.addErpBillId(billNo,
					ErpBillIdFactory.Table.SHOP_INVENTORY, bill.getOrigId());
			bill.setSrcBillNo(billNo);
		}
	}

	@Override
	public void batchShopReturnOutBill(final Business bus) {
		final Bill bill = bus.getBill();
		if (CommonUtil.isNotBlank(bill)) {
			status = bill.getStatus();
			noticeTable = "dj_zgthtzda";
			noticeDtlTable = "dj_zgthtzdb";
			mainTable = "dj_zgthda";
			mainDtlTable = "dj_zgthdb";
			status = bill.getStatus();

			final List<BillDtl> listBillDtls = PlayloungeUtil
					.filterNotScan(bill);

			/***
			 * 2. 更改通知单
			 * */
			String noticeSql = PostSqlUtil.getChNoticeSql(noticeTable);
			int ni = this.playloungeJdbcTemplate.update(noticeSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setLong(1, bill.getScanQty().longValue());
							if (status == 2
									|| (bill.getActQty().longValue() == bill
											.getTotQty().longValue())) {
								ps.setString(2, "1");
							} else {
								ps.setString(2, "0");
							}
							ps.setString(3, "1");
							ps.setString(4, bill.getBillNo());
						}
					});
			String noticeDtlSql = PostSqlUtil.getChNoticeDtlSql(noticeDtlTable);
			int[] ndi = this.playloungeJdbcTemplate.batchUpdate(noticeDtlSql,
					new BatchPreparedStatementSetter() {
						public int getBatchSize() {
							return listBillDtls.size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = (BillDtl) listBillDtls.get(i);
							ps.setLong(1, dtl.getScanQty().longValue());
							ps.setString(2, dtl.getStyleId());
							ps.setString(3, dtl.getColorId());
							ps.setString(4, dtl.getSizeId());
							ps.setString(5, bill.getBillNo());
						}
					});
			/**
			 * 3.添加出库单
			 * */
			final String billNo = erpBillIdFactory.productBillId(
					ErpBillIdFactory.Table.SHOP_RETURN_OUT, bill.getOrigId());
			String mainSql = String
					.format("insert %s (accept,item,BillDate,whichprice,"
							+ "Outcode,Incode,types,dhItem,tzItem,"
							+ "num,mny,cbmny1,"
							+ "flag,status,submit,execution,"
							+ "prints,printnum,mem,"
							+ "gzczy,gzrq,lrczy,lrrq)"
							+ " select ?,?,?,whichprice,"// 2
							+ "Outcode,Incode,3,dhItem,item,"
							+ "?,?,?,"// 5
							+ "?,?,?,?,"// 9
							+ "0,0,(case when mem IS NULL   then '' else mem end)+?,?,?,"// 12
							+ "?,?"// 15
							+ " from %s where item=?", mainTable, noticeTable);
			int mi = this.playloungeJdbcTemplate.update(mainSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setString(1, "0");
							ps.setString(2, billNo);
							ps.setTimestamp(3, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setLong(4, bill.getScanQty().longValue());
							ps.setDouble(5, bill.getTotPrice());
							ps.setDouble(6, bill.getTotPrePrice());
							ps.setString(7, "1");// 设置审核flage
							ps.setString(8, "0");// 设置中止status
							ps.setString(9, "1");// 设置提交submit
							ps.setString(10, "0");// 设置执行execution
							ps.setString(11,  (CommonUtil.isBlank(bill.getRemark())?"":bill.getRemark()));
							if (CommonUtil.isBlank(bus.getOperator())) {
								ps.setString(12, bus.getDeviceId());
								ps.setString(14, bus.getDeviceId());
							} else {
								ps.setString(12, bus.getOperator());
								ps.setString(14, bus.getDeviceId());
							}
							ps.setTimestamp(13,
									new Timestamp(new Date().getTime()));
							ps.setTimestamp(15,
									new Timestamp(new Date().getTime()));
							ps.setString(16, bill.getBillNo());
						}

					});
			String mainDtl = String
					.format("insert %s (item,clthno,color,size,price,cb1,"
							+ "num,kcnum,dhnum,"
							+ "dhwcnum,kpnum) "
							+ " select ?,clthno,color,size,Price1,Cost1,"
							+ " ?,num,?,?,? "
							+ " from (select s.Whcode, s.num,a.Price1,a.clthno,a.color,a.size,a.Cost1"
							+ "  from (select s.Price1,s.Cost1,s.Cost6,"
							+ " s.clthno,i.size,c.color "
							+ " from jbCloth s,jbClothSize i,jbClothColor c "
							+ " where s.clthno=i.clthno and c.clthno=s.clthno and i.clthno=c.clthno) a left join store s "
							+ " on s.color=a.color and s.clthno=a.clthno and a.size=s.size "
							+ " ) a where clthno=? and  color=? and  size=? and Whcode=?",
							mainDtlTable);
			int[] mdi = this.playloungeJdbcTemplate.batchUpdate(mainDtl,
					new BatchPreparedStatementSetter() {
						public int getBatchSize() {
							return listBillDtls.size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = (BillDtl) listBillDtls.get(i);
							ps.setString(1, billNo);
							ps.setDouble(2, dtl.getScanQty().longValue());
							ps.setDouble(3, dtl.getQty().longValue());
							ps.setDouble(4, dtl.getQty().longValue());
							ps.setDouble(5, dtl.getScanQty().longValue());
							ps.setString(6, dtl.getStyleId());
							ps.setString(7, dtl.getColorId());
							ps.setString(8, dtl.getSizeId());
							ps.setString(9, bill.getOrigId());
						}
					});
			erpBillIdFactory.addErpBillId(billNo,
					ErpBillIdFactory.Table.SHOP_RETURN_OUT, bill.getOrigId());
			bill.setSrcBillNo(billNo);

			/*
			 * 添加库存 *
			 */
			String chrsql = "update store   set num=IsNull(num ,0) - ?, tcnum = IsNull(tcnum,0) +?"
					+ " WHERE Whcode= ? AND clthno = ? AND color =? AND size = ?";
			int[] update = this.playloungeJdbcTemplate.batchUpdate(chrsql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBillDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = listBillDtls.get(i);
							ps.setLong(1, dtl.getScanQty().longValue());
							ps.setLong(2, dtl.getScanQty().longValue());

							ps.setString(3, bill.getOrigId());// 收货方
							ps.setString(4, dtl.getStyleId());
							ps.setString(5, dtl.getColorId());
							ps.setString(6, dtl.getSizeId());
						}
					});
			String chrInsql = "update store   set waynum=IsNull(waynum ,0) + ? "
					+ " WHERE Whcode= ? AND clthno = ? AND color =? AND size = ?";
			int[] updateIn = this.playloungeJdbcTemplate.batchUpdate(chrInsql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBillDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = listBillDtls.get(i);
							ps.setLong(1, dtl.getScanQty().longValue());
							ps.setString(2, bill.getDestId());// 收货方
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
						}
					});

			final List<BillDtl> tempDtls = new ArrayList<>();
			for (int i = 0; i < updateIn.length; i++) {
				if (updateIn[i] < 1) {
					tempDtls.add(listBillDtls.get(i));
				}
			}
			if (tempDtls.size() > 0) {
				String chrtempsql = "insert store(waynum,Whcode,clthno,color,size,indate) values(?,?,?,?,?,?)";
				this.playloungeJdbcTemplate.batchUpdate(chrtempsql,
						new BatchPreparedStatementSetter() {
							@Override
							public int getBatchSize() {
								return tempDtls.size();
							}

							@Override
							public void setValues(PreparedStatement ps, int i)
									throws SQLException {
								BillDtl dtl = tempDtls.get(i);
								ps.setLong(1, dtl.getScanQty().longValue());
								ps.setString(2, bill.getDestId());// 收货方
								ps.setString(3, dtl.getStyleId());
								ps.setString(4, dtl.getColorId());
								ps.setString(5, dtl.getSizeId());
								ps.setTimestamp(6, null);

							}
						});
			}
			;
		}
	}

}
