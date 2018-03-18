package com.casesoft.dmc.extend.playlounge.dao;

import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.extend.playlounge.dao.basic.IPlayloungeWarehousePostTaskDao;
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

public class PlayloungeWarehousePostTaskDao extends PlayloungeBasicDao
		implements IPlayloungeWarehousePostTaskDao {
	@Override
	public void batchWarehouseInTask(final Business bus) {	
			orderTable = "dj_zgsjhdhda";
			orderDtlTable = "dj_zgsjhdhdb";
			noticeTable = "dj_zgsjhtzda";
			noticeDtlTable = "dj_zgsjhtzdb";
			mainTable = "dj_zgsjhda";
			mainDtlTable = "dj_zgsjhdb";
		
			/**
			 * 3.添加入库单
			 * */
			final String billNo = erpBillIdFactory.productBillId(
					ErpBillIdFactory.Table.WAREH_IN, bus.getDestId());
			String mainSql = String
					.format("insert %s (item,BillDate,whichprice,"
							+ "Outcode,Incode,types,"
							+ "num,mny,cbmny1,"
							+ "flag,status,submit,execution,"
							+ "prints,printnum,mem,gzczy,gzrq,"
							+ "lrczy,lrrq,cbmny6)"
							+ " values( ?,?,1,"// 2
							+ "?,?,0,"
							+ "?,?,?,"// 5
							+ "?,?,?,?,"// 9
							+ "0,0,?,?,?,"// 12
							+ "?,?,?)", mainTable);
			int mi = this.playloungeJdbcTemplate.update(mainSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setString(1, billNo);
							ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setString(3, bus.getOrigUnitId());
							ps.setString(4, bus.getDestId());
							ps.setLong(5, bus.getTotEpc());
							ps.setDouble(6, bus.getTotPrice());
							ps.setDouble(7, bus.getTotPreCase());
							ps.setString(8, "1");// 设置审核flage
							ps.setString(9, "0");// 设置中止status
							ps.setString(10, "1");// 设置提交submit
							ps.setString(11, "1");// 设置执行execution
							ps.setString(12, (CommonUtil.isBlank(bus.getRemark())?"":bus.getRemark()));
							if (CommonUtil.isBlank(bus.getOperator())) {
								ps.setString(13, bus.getDeviceId());
								ps.setString(15, bus.getDeviceId());
							} else {
								ps.setString(13, bus.getOperator());
								ps.setString(15, bus.getOperator());

							}
							ps.setTimestamp(14,
									new Timestamp(new Date().getTime()));
							ps.setTimestamp(16,
									new Timestamp(new Date().getTime()));
							ps.setDouble(17, bus.getTotPuPrice());
						}

					});
			final List<BusinessDtl> dtls = bus.getDtlList();

			String mainDtl = String
					.format("insert %s (item,clthno,color,size,price,cb1,"
							+ "num,kcnum,cb6) "
							+ " select ?,clthno,color,size,Price1,Cost1,"
							+ " ?,num,Cost6 "
							+ " from (select s.Whcode, s.num,a.Price1,a.clthno,a.color,a.size,a.Cost1,a.Cost6"
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
							return dtls.size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BusinessDtl dtl =  dtls.get(i);
							ps.setString(1, billNo);
							ps.setDouble(2, dtl.getQty());
							ps.setString(3, bus.getDestId());
							ps.setString(4, dtl.getStyleId());
							ps.setString(5, dtl.getColorId());
							ps.setString(6, dtl.getSizeId());
						}
					});
			erpBillIdFactory.addErpBillId(billNo,
					ErpBillIdFactory.Table.WAREH_IN, bus.getDestId());
			bus.setSrcBillNo(billNo);
			/*
			 * 
			 * 更改库存 *
			 */
			String chrsql = "update store   set num=IsNull(num ,0) + ?, zjnum = IsNull(zjnum,0) +?"
					+ " WHERE Whcode= ? AND clthno = ? AND color =? AND size = ?";
			int[] update = this.playloungeJdbcTemplate.batchUpdate(chrsql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return dtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BusinessDtl dtl = dtls.get(i);
							ps.setDouble(1, dtl.getQty());
							ps.setDouble(2, dtl.getQty());
							ps.setString(3, bus.getDestId());// 收货方
							ps.setString(4, dtl.getStyleId());
							ps.setString(5, dtl.getColorId());
							ps.setString(6, dtl.getSizeId());
						}
					});
			final List<BusinessDtl> tempDtls = new ArrayList<>();
			for (int i = 0; i < update.length; i++) {
				if (update[i] < 1) {
					tempDtls.add(dtls.get(i));
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
							public void setValues(PreparedStatement ps, int i)
									throws SQLException {
								BusinessDtl dtl = tempDtls.get(i);
								ps.setDouble(1, dtl.getQty());
								ps.setDouble(2, dtl.getQty());
								ps.setString(3, bus.getDestId());// 收货方
								ps.setString(4, dtl.getStyleId());
								ps.setString(5, dtl.getColorId());
								ps.setString(6, dtl.getSizeId());
								ps.setTimestamp(7, Timestamp.valueOf(CommonUtil
										.getDateString(new Date(),
												"yyyy-MM-dd 00:00:00")));

							}
						});
			}
	}

	@Override
	public void batchWarehouseOutTask(final Business bus) {
		orderTable = "dj_zgdhda";
		orderDtlTable = "dj_zgdhdb";
		noticeTable = "dj_zgchtzda";
		noticeDtlTable = "dj_zgchtzdb";
		mainTable = "dj_zgchda";
		mainDtlTable = "dj_zgchdb";
		/**
		 * 3.添加出库单
		 * */
		final String billNo = erpBillIdFactory.productBillId(
				ErpBillIdFactory.Table.WAREH_OUT, bus.getOrigId());
		String mainSql = String.format("insert %s (item,BillDate,whichprice,"
				+ "Outcode,Incode,types," + "num,mny,cbmny1,"
				+ "flag,status,submit,execution," + "prints,printnum,mem,"
				+ "gzczy,gzrq,lrczy,lrrq)" + " values(?,?,1,"// 2
				+ "?,?,0,"// 4
				+ "?,?,?,"// 7
				+ "?,?,?,?,"// 9
				+ "0,0,?,"// 12
				+ "?,?,?,?)", mainTable);// 15
		int mi = this.playloungeJdbcTemplate.update(mainSql,
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps)
							throws SQLException {
						ps.setString(1, billNo);
						ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
								.getDateString(new Date(),
										"yyyy-MM-dd 00:00:00")));
						ps.setString(3, bus.getOrigId());
						ps.setString(4, bus.getDestId());
						ps.setLong(5, bus.getTotEpc());
						ps.setDouble(6, bus.getTotPrice());
						ps.setDouble(7, bus.getTotPreCase());
						ps.setString(8, "1");// 设置审核flage
						ps.setString(9, "1");// 设置中止status
						ps.setString(10, "1");// 设置提交submit
						ps.setString(11, "1");// 设置执行execution
						ps.setString(12, (CommonUtil.isBlank(bus.getRemark())?"":bus.getRemark()));
						if (CommonUtil.isBlank(bus.getOperator())) {
							ps.setString(13, bus.getDeviceId());
							ps.setString(15, bus.getDeviceId());

						} else {
							ps.setString(13, bus.getOperator());
							ps.setString(15, bus.getOperator());
						}
						ps.setTimestamp(14, new Timestamp(new Date().getTime()));
						ps.setTimestamp(16, new Timestamp(new Date().getTime()));
					}

				});
		final List<BusinessDtl> dtls = bus.getDtlList();
		String mainDtl = String
				.format("insert %s (item,clthno,color,size,price,cb1,"
						+ "num,kcnum) "
						+ " select ?,clthno,color,size,Price1,Cost1,"
						+ " ?,num "
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
						return dtls.size();
					}

					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						BusinessDtl dtl = (BusinessDtl) dtls.get(i);
						ps.setString(1, billNo);
						ps.setDouble(2, dtl.getQty());
						ps.setString(3, bus.getOrigId());
						ps.setString(4, dtl.getStyleId());
						ps.setString(5, dtl.getColorId());
						ps.setString(6, dtl.getSizeId());

					}
				});
		erpBillIdFactory.addErpBillId(billNo, ErpBillIdFactory.Table.WAREH_OUT,
				bus.getOrigId());
		bus.setSrcBillNo(billNo);
		/*
		 * 更改库存 *
		 */
		String chrsql = "update store   set num=IsNull(num ,0) - ?, chnum = IsNull(chnum,0) +?"
				+ " WHERE Whcode= ? AND clthno = ? AND color =? AND size = ?";
		int[] update = this.playloungeJdbcTemplate.batchUpdate(chrsql,
				new BatchPreparedStatementSetter() {
					@Override
					public int getBatchSize() {
						return dtls.size();
					}

					@Override
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						BusinessDtl dtl = (BusinessDtl) dtls.get(i);
						ps.setLong(1, dtl.getQty());
						ps.setLong(2, dtl.getQty());

						ps.setString(3, bus.getOrigId());// 发货
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
						return dtls.size();
					}

					@Override
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						BusinessDtl dtl = dtls.get(i);
						ps.setLong(1, dtl.getQty());
						ps.setString(2, bus.getDestId());// 收货方
						ps.setString(3, dtl.getStyleId());
						ps.setString(4, dtl.getColorId());
						ps.setString(5, dtl.getSizeId());
					}
				});

		final List<BusinessDtl> tempDtls = new ArrayList<>();
		for (int i = 0; i < updateIn.length; i++) {
			if (updateIn[i] < 1) {
				tempDtls.add(dtls.get(i));
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
							BusinessDtl dtl = tempDtls.get(i);
							ps.setLong(1, dtl.getQty());
							ps.setString(2, bus.getDestId());// 收货方
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
							ps.setTimestamp(6, null);

						}
					});
		}
		;

	}

	@Override
	public void batchWarehouseReturnInTask(final Business bus) {
		final Bill bill = bus.getBill();
		if (CommonUtil.isNotBlank(bill)) {
			status = bill.getStatus();
			noticeTable = "dj_zgthda";
			noticeDtlTable = "dj_zgthdb";
			mainTable = "dj_zgtrda";
			mainDtlTable = "dj_zgtrdb";
			status = bill.getStatus();

			final List<BillDtl> listBillDtls = PlayloungeUtil
					.filterNotScan(bill);
			final List<BusinessDtl> listBusDtls = bus.getDtlList();
			if (bill.getActQty().longValue() == bill.getTotQty().longValue()) {
				/***
				 * 2. 更改通知单
				 * */
				String noticeSql = String.format(
						"update %s set chnum=(case when chnum IS  NULL  then 0 else chnum end)+?"
								+ ",accept=?,acceptdate=? where item=? ",
						noticeTable);
				int ni = this.playloungeJdbcTemplate.update(noticeSql,
						new PreparedStatementSetter() {
							@Override
							public void setValues(PreparedStatement ps)
									throws SQLException {
								ps.setLong(1, bus.getTotEpc());
								if (status == 2
										|| (bill.getActQty().longValue() == bill
												.getTotQty().longValue())) {
									ps.setString(2, "1");
								} else {
									ps.setString(2, "0");
								}
								ps.setTimestamp(3, Timestamp.valueOf(CommonUtil
										.getDateString(new Date(),
												"yyyy-MM-dd 00:00:00")));
								ps.setString(4, bill.getBillNo());

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
			 * .Table.WAREH_OUT, bill.getUnitId());
			 */
			String mainSql = String
					.format("insert %s (accept,item,BillDate,whichprice,"
							+ "Outcode,Incode,types,dhItem,tzItem,"
							+ "num,chnum,mny,cbmny1,"
							+ "flag,status,submit,execution,"
							+ "prints,printnum,mem,gzczy,gzrq,"
							+ "lrczy,lrrq)"
							+ " select ?,?,?,whichprice,"// 2
							+ "Outcode,Incode,types,dhItem,item,"
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
									.getTotQty().longValue()) {
								ps.setString(1, "1");
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
								ps.setString(1, "0");
								ps.setString(5, "0");// 设置审核flage
								ps.setString(7, "0");// 设置提交submit
								ps.setString(10, null);

								ps.setTimestamp(11, null);
							}
							ps.setString(2, bill.getBillNo());
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
							ps.setString(9, (CommonUtil.isBlank(bus.getRemark())?"":bus.getRemark()));
							if (CommonUtil.isBlank(bus.getOperator())) {
								ps.setString(12, bus.getDeviceId());
							} else {
								ps.setString(12, bus.getOperator());
							}
							ps.setTimestamp(13,
									new Timestamp(new Date().getTime()));
							ps.setString(14, bill.getBillNo());
						}

					});
			String mainDtl = String
					.format("insert %s (item,clthno,color,size,price,cb1,"
							+ "num,kcnum,chnum) "
							+ " select ?,clthno,color,size,Price1,Cost1,"
							+ " ?,num,? "
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
							ps.setString(1, bill.getBillNo());
							ps.setDouble(2, dtl.getQty().longValue());
							ps.setDouble(3, dtl.getScanQty().longValue());
							ps.setString(4, bill.getDestId());
							ps.setString(5, dtl.getStyleId());
							ps.setString(6, dtl.getColorId());
							ps.setString(7, dtl.getSizeId());

						}
					});
			/*
			 * 
			 * 更改库存 *
			 */if (bill.getActQty().longValue() == bill.getTotQty().longValue()) {
				String chrsql = "update store   set num=IsNull(num ,0) + ?, thnum = IsNull(thnum,0) +?,"
						+ "waynum=IsNull(waynum ,0) - ? "
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
								ps.setString(4, bill.getDestId());// 收货方
								ps.setString(5, dtl.getStyleId());
								ps.setString(6, dtl.getColorId());
								ps.setString(7, dtl.getSizeId());
							}
						});
				final List<BillDtl> tempDtls = new ArrayList<>();
				for (int i = 0; i < update.length; i++) {
					if (update[i] < 1) {
						tempDtls.add(listBillDtls.get(i));
					}
				}
				if (tempDtls.size() > 0) {
					String chrtempsql = "insert store(num,thnum,Whcode,clthno,color,size,indate) values(?,?,?,?,?,?,?)";
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
	public void batchWarehouseReturnOutTask(final Business bus) {
	
			noticeTable = "dj_zgsthtzda";
			noticeDtlTable = "dj_zgsthtzdb";
			mainTable = "dj_zgsthda";
			mainDtlTable = "dj_zgsthdb";
			final List<BusinessDtl> dtls = bus.getDtlList();

			/**
			 * 3.添加入库单
			 * */
			final String billNo = erpBillIdFactory.productBillId(
					ErpBillIdFactory.Table.WAREH_RETURN_OUT, bus.getOrigId());

			String mainSql = String
					.format("insert %s (item,BillDate,whichprice,"
							+ "Outcode,Incode,types,"
							+ "num,mny,cbmny1,"
							+ "flag,status,submit,execution,"
							+ "prints,printnum,mem,gzczy,gzrq,"
							+ "lrczy,lrrq,cbmny6)"
							+ " values( ?,?,1,"// 2
							+ "?,?,0,"
							+ "?,?,?,"// 5
							+ "?,?,?,?,"// 9
							+ "0,0,?,?,?,"// 12
							+ "?,?,?) ", mainTable);
			int mi = this.playloungeJdbcTemplate.update(mainSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setString(1, billNo);
							ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setString(3, bus.getOrigId());
							ps.setString(4, bus.getDestUnitId());
							ps.setLong(5, bus.getTotEpc());
							ps.setDouble(6, bus.getTotPrice());
							ps.setDouble(7, bus.getTotPreCase());
							ps.setString(8, "1");// 设置审核flage
							ps.setString(9, "0");// 设置中止status
							ps.setString(10, "1");// 设置提交submit
							ps.setString(11, "1");// 设置执行execution
							if (CommonUtil.isBlank(bus.getOperator())) {
								ps.setString(13, bus.getDeviceId());
							} else {
								ps.setString(13, bus.getOperator());
							}
							ps.setTimestamp(14,
									new Timestamp(new Date().getTime()));
							ps.setString(12, (CommonUtil.isBlank(bus.getRemark())?"":bus.getRemark()));
							if (CommonUtil.isBlank(bus.getOperator())) {
								ps.setString(15, bus.getDeviceId());
							} else {
								ps.setString(15, bus.getDeviceId());
							}

							ps.setTimestamp(16,
									new Timestamp(new Date().getTime()));
							ps.setDouble(17, bus.getTotPuPrice());
 						}

					});
			String mainDtl = String
					.format("insert %s (item,clthno,color,size,price,cb1,"
							+ "num,kcnum,cb6) "
							+ " select ?,clthno,color,size,Price1,Cost1,"
							+ " ?,num,Cost6 "
							+ " from (select s.Whcode, s.num,a.Price1,a.clthno,a.color,a.size,a.Cost1,a.Cost6"
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
							return dtls.size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BusinessDtl dtl = dtls.get(i);
							ps.setString(1, billNo);
							ps.setDouble(2, dtl.getQty());
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
							ps.setString(6, bus.getOrigId());
						}
					});
			bus.setSrcBillNo(billNo);
			erpBillIdFactory.addErpBillId(billNo,
					ErpBillIdFactory.Table.WAREH_RETURN_OUT, bus.getOrigId());
			String chrsql = "update store   set num=IsNull(num ,0) - ?, ztnum = IsNull(ztnum,0) +?"
					+ " WHERE Whcode= ? AND clthno = ? AND color =? AND size = ?";
			int[] update = this.playloungeJdbcTemplate.batchUpdate(chrsql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return dtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BusinessDtl dtl = dtls.get(i);
							ps.setLong(1, dtl.getQty());
							ps.setLong(2, dtl.getQty());
							ps.setString(3, bus.getOrigId());// 收货方
							ps.setString(4, dtl.getStyleId());
							ps.setString(5, dtl.getColorId());
							ps.setString(6, dtl.getSizeId());
						}
					});
	}

	@Override
	public void batchWarehouseTransferInTask(final Business bus) {
		final Bill bill = bus.getBill();
		if (CommonUtil.isNotBlank(bill)) {
			status = bill.getStatus();
			noticeTable = "dj_ycda";
			noticeDtlTable = "dj_ycdb";
			mainTable = "dj_yrda";
			mainDtlTable = "dj_yrdb";
			status = bill.getStatus();

			final List<BillDtl> listBillDtls = PlayloungeUtil
					.filterNotScan(bill);
			final List<BusinessDtl> listBusDtls = bus.getDtlList();

			/***
			 * 2. 更改通知单
			 * */
			if (status == 2
					|| bill.getActQty().longValue() == bill.getTotQty()
							.longValue()) {

				String noticeSql = String.format(
						"update %s set chnum=(case when chnum IS  NULL  then 0 else chnum end)+?"
								+ ",accept=? where item=? ", noticeTable);
				int ni = this.playloungeJdbcTemplate.update(noticeSql,
						new PreparedStatementSetter() {
							@Override
							public void setValues(PreparedStatement ps)
									throws SQLException {
								ps.setLong(1, bus.getTotEpc());
								if (status == 2
										|| bill.getActQty().longValue() == bill
												.getTotQty().longValue()) {
									ps.setString(2, "1");
								} else {
									ps.setString(2, "0");
								}
								ps.setString(3, bill.getBillNo());
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
							if (status == 2
									|| (bill.getActQty().longValue() == bill
											.getTotQty().longValue())) {
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
							ps.setString(2, bill.getBillNo());
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
							ps.setString(9, (CommonUtil.isBlank(bus.getRemark())?"":bus.getRemark()));
							if (CommonUtil.isBlank(bus.getOperator())) {
								ps.setString(12, bus.getDeviceId());
							} else {
								ps.setString(12, bus.getOperator());
							}
							ps.setTimestamp(13,
									new Timestamp(new Date().getTime()));
							ps.setString(14, bill.getBillNo());
						}

					});
			String mainDtl = String
					.format("insert %s (item,clthno,color,size,price,cb1,"
							+ "num,kcnum,chnum) "
							+ " select ?,clthno,color,size,Price1,Cost1,"
							+ " ?,num,? "
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
							ps.setString(1, bill.getBillNo());
							ps.setDouble(2, dtl.getQty().longValue());
							ps.setDouble(3, dtl.getScanQty().longValue());
							ps.setString(4, bill.getDestId());
							ps.setString(5, dtl.getStyleId());
							ps.setString(6, dtl.getColorId());
							ps.setString(7, dtl.getSizeId());

						}
					});
			/*
			 * 
			 * 更改库存 *
			 */
			if (status == 2
					|| bill.getActQty().longValue() == bill.getTotQty()
							.longValue()) {
				String chrsql = "update store   set num=IsNull(num ,0) + ?,yrnum=IsNull(yrnum ,0) + ?,"
						+ "waynum=IsNull(waynum ,0) - ?,"
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
	public void batchWarehouseTransferOutTask(final Business bus) {
			orderTable = "dj_ycsqda";
			orderDtlTable = "dj_ycsqdb";
			noticeTable = "dj_yctzda";
			noticeDtlTable = "dj_yctzdb";
			mainTable = "dj_ycda";
			mainDtlTable = "dj_ycdb";
			/**
			 * 3.添加出库单
			 * */
			final String billNo = erpBillIdFactory
					.productBillId(ErpBillIdFactory.Table.WAREH_TRANSFER_OUT,
							bus.getOrigId());
			String mainSql = String
					.format("insert %s (accept,item,BillDate,whichprice,"
							+ "Outcode,Incode,types,"
							+ "num,mny,cbmny1,"
							+ "flag,status,submit,execution,"
							+ "prints,printnum,mem,"
							+ "gzczy,gzrq,lrczy,lrrq)"
							+ " values( 0,?,?,1,"// 2
							+ "?,?,0,"
							+ "?,?,?,"// 5
							+ "?,?,?,?,"// 9
							+ "0,0,?,"// 12
							+ "?,?,?,?)",
							mainTable);
			int mi = this.playloungeJdbcTemplate.update(mainSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setString(1, billNo);
							ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setString(3, bus.getOrigId());
							ps.setString(4, bus.getDestId());
							ps.setLong(5, bus.getTotEpc());
							ps.setDouble(6,bus.getTotPrice());
							ps.setDouble(7, bus.getTotPreCase());
							ps.setString(8, "1");// 设置审核flage
							ps.setString(9, "0");// 设置中止status
							ps.setString(10, "1");// 设置提交submit
							ps.setString(11, "0");// 设置执行execution
							ps.setString(12,  (CommonUtil.isBlank(bus.getRemark())?"":bus.getRemark()));
							if (CommonUtil.isBlank(bus.getOperator())) {
								ps.setString(13, bus.getDeviceId());
								ps.setString(15, bus.getDeviceId());
							} else {
								ps.setString(13, bus.getOperator());
								ps.setString(15, bus.getOperator());
							}
							ps.setTimestamp(14,
									new Timestamp(new Date().getTime()));
							ps.setTimestamp(16,
									new Timestamp(new Date().getTime()));
						}

					});
			final List<BusinessDtl> dtls = bus.getDtlList();

			String mainDtl = String
					.format("insert %s (item,clthno,color,size,price,cb1,"
							+ "num,kcnum) "
							+ " select ?,clthno,color,size,Price1,Cost1,"
							+ " ?,num "
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
							return dtls.size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BusinessDtl dtl =  dtls.get(i);
							ps.setString(1, billNo);
							ps.setDouble(2, dtl.getQty());
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
							ps.setString(6, bus.getOrigId());
						}
					});
			erpBillIdFactory
					.addErpBillId(billNo,
							ErpBillIdFactory.Table.WAREH_TRANSFER_OUT,
							bus.getOrigUnitId());
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
							return dtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BusinessDtl dtl = dtls.get(i);
							ps.setLong(1, dtl.getQty());
							ps.setLong(2, dtl.getQty());

							ps.setString(3, bus.getOrigId());// 收货方
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
							return dtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BusinessDtl dtl = dtls.get(i);
							ps.setLong(1, dtl.getQty());
							ps.setString(2, bus.getDestId());// 收货方
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
						}
					});

			final List<BusinessDtl> tempDtls = new ArrayList<>();
			for (int i = 0; i < updateIn.length; i++) {
				if (updateIn[i] < 1) {
					tempDtls.add(dtls.get(i));
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
								BusinessDtl dtl = tempDtls.get(i);
								ps.setLong(1, dtl.getQty());
								ps.setString(2, bus.getDestId());// 收货方
								ps.setString(3, dtl.getStyleId());
								ps.setString(4, dtl.getColorId());
								ps.setString(5, dtl.getSizeId());
								ps.setTimestamp(6, null);

							}
						});
			
		}
	}

	@Override
	public void batchWarehouseInventoryTask(final Business bus) {
		mainTable = "dj_pdda";
		mainDtlTable = "dj_pddb";
		final String billNo = erpBillIdFactory.productBillId(
				ErpBillIdFactory.Table.WAREH_INVENTORY, bus.getOrigId());
		String mainSql = String.format("insert %s (item,BillDate,whichprice,"
				+ "Outcode,types," + "num,mny,cbmny1,"
				+ "prints,printnum,mem,"
				+ "gzczy,gzrq,lrczy,lrrq,"
				+ "flag,status,submit,execution)" + " values(?,?,1,"// 2
				+ "?,0,"// 3
				+ "?,?,?,"// 6
				 + "0,0,?,"// 7
				+ "?,?,?,?,"
				+ "?,?,?,?)"// 9
		, mainTable);
		final List<BusinessDtl> dtls = bus.getDtlList();
		int mi = this.playloungeJdbcTemplate.update(mainSql,
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps)
							throws SQLException {
						ps.setString(1, billNo);
						ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
								.getDateString(bus.getEndTime(),
										"yyyy-MM-dd 00:00:00")));
						ps.setString(3, bus.getOrigId());
						ps.setLong(4, bus.getTotEpc());
						ps.setDouble(5, bus.getTotPrice());
						ps.setDouble(6, bus.getTotPreCase());
						ps.setString(7, (CommonUtil.isBlank(bus.getRemark())?"":bus.getRemark()));
						if (CommonUtil.isBlank(bus.getOperator())) {
							ps.setString(8, bus.getDeviceId());
							ps.setString(10, bus.getDeviceId());

						} else {
							ps.setString(8, bus.getOperator());
							ps.setString(10, bus.getOperator());

						}
						ps.setTimestamp(9, new Timestamp(new Date().getTime()));
						ps.setTimestamp(11, new Timestamp(new Date().getTime()));
						ps.setString(12, "1");// 设置审核flage
						ps.setString(13, "0");// 设置中止status
						ps.setString(14, "1");// 设置提交submit
						ps.setString(15, "0");// 设置执行execution
					}

				});
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
						return dtls.size();
					}

					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						BusinessDtl dtl = (BusinessDtl) dtls.get(i);
						ps.setString(1, billNo);
						ps.setDouble(2, dtl.getQty());
						ps.setString(3, bus.getOrigUnitId());
						ps.setString(4, dtl.getStyleId());
						ps.setString(5, dtl.getColorId());
						ps.setString(6, dtl.getSizeId());

					}
				});
		erpBillIdFactory.addErpBillId(billNo,
				ErpBillIdFactory.Table.WAREH_INVENTORY, bus.getOrigId());
	}

}
