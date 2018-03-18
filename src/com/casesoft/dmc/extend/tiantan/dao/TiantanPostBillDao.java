package com.casesoft.dmc.extend.tiantan.dao;

import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.extend.tiantan.dao.basic.ConstantType;
import com.casesoft.dmc.extend.tiantan.dao.basic.ITiantanPostBillDao;
import com.casesoft.dmc.extend.tiantan.dao.basic.TiantanBasicDao;
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

//@Repository
public class TiantanPostBillDao extends TiantanBasicDao implements
		ITiantanPostBillDao {
	private final String JS_CODE = "";
	private final String JS_NAME = "CaseSoft";
	private String mainNoticeTable;
	private String detailNoticeTable;
	private String mainTable;
	private String detailTable;
	private String orderTable;
	private String orderDtlTable;

	@Override
	public void batchInBill(final Business bus) {
		final Bill bill = bus.getBill();
		if (CommonUtil.isNotBlank(bill)) {
			orderTable = "JORDER";
			orderDtlTable = "JORDERMX";
			mainNoticeTable = "JSEND";
			detailNoticeTable = "JSENDMX";
			mainTable = "SPJHD";
			detailTable = "SPJHDMX";
			final List<BillDtl> listBillDtls = TiantanUtil.filterNotScan(bill);
			/*
			 * 更改订单
			 */
			/*
			修改入库订单
			String orderSql = PostSQL.getChOrderTableSQL(mainNoticeTable,
					orderTable);*/
			String orderSql = PostSQL.getSpChOrderTableSQL(mainNoticeTable,
					orderTable);
			this.tianTanJdbcTemplate.update(orderSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setTimestamp(1, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setString(3, JS_NAME);
							ps.setDouble(4, bill.getTotQty().longValue());
							ps.setDouble(5, bill.getActQty().longValue());
							ps.setString(6, bill.getBillNo());
						}
					});
			String orderDtlSql = PostSQL.getChOrderDetailTableSQL(
					orderDtlTable, mainNoticeTable);
			this.tianTanJdbcTemplate.batchUpdate(orderDtlSql,
					new BatchPreparedStatementSetter() {
						public int getBatchSize() {
							return listBillDtls.size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = (BillDtl) listBillDtls.get(i);
							ps.setDouble(1, dtl.getQty().longValue());
							ps.setDouble(2, dtl.getActQty().longValue());
							ps.setString(3, bill.getBillNo());
							ps.setString(4, dtl.getStyleId());
							ps.setString(5, dtl.getColorId());
							ps.setString(6, dtl.getSizeId());
						}
					});

			/*
			 * 更新通知单js=1;已执行 *
			 */
			/*String updateBillSql = PostSQL
					.getChMainNoticeTableSQL(mainNoticeTable);

					修改终止*/
			String updateBillSql = PostSQL
					.getSpChMainNoticeTableSQL(mainNoticeTable);
			this.tianTanJdbcTemplate.update(updateBillSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setTimestamp(1, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setString(3, JS_NAME);
							ps.setDouble(4, bill.getActQty().longValue());
							ps.setString(5, bill.getBillNo());
						}

					});
			String updateBillDtlSql = PostSQL
					.getChDetialNoticeTableSQL(detailNoticeTable);
			this.tianTanJdbcTemplate.batchUpdate(updateBillDtlSql,
					new BatchPreparedStatementSetter() {
						public int getBatchSize() {
							return listBillDtls.size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = (BillDtl) listBillDtls.get(i);
							/*
							 * ps.setString(1, String.valueOf(dtl.getQty()));
							 * ps.setString(2, String.valueOf(dtl.getQty()));
							 */
							ps.setDouble(1, dtl.getActQty().longValue());

							ps.setString(2, bill.getBillNo());
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
						}
					});
			/*
			 * 添加入库单
			 */
			String insertIn = PostSQL.getInsertMainTableSQL(mainTable,
					mainNoticeTable);

			this.tianTanJdbcTemplate.update(insertIn,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setString(1, "R3_" + bill.getBillNo());// DJBH 单号
							ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));// RQ 日期
							ps.setDouble(3, bill.getActQty().longValue());// SL 执行数量
							ps.setDouble(4, bill.getTotPrice().doubleValue());// JE 金额
							ps.setDouble(5, bill.getTotPrice().doubleValue());// BZJE 标准金额
							ps.setInt(6, 1);// YS 验收
							ps.setTimestamp(7, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));// YSRQ
							// 验收日期
							ps.setString(8, JS_NAME);// YES 验收人
							ps.setTimestamp(9, null);// JZRQ 记账日期
							ps.setString(10, null);// JZR 人
							ps.setTimestamp(11, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));// JZRQ
							// 执行日期
							ps.setString(12, JS_NAME);// JZRQ 执行人
							ps.setString(13, bill.getBillNo());

						}

					});
			String sql = PostSQL.getInsertDetailTableSQL(detailTable,
					detailNoticeTable);
			this.tianTanJdbcTemplate.batchUpdate(sql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBillDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = listBillDtls.get(i);
							ps.setString(1, "R3_" + bill.getBillNo());// DJBH 单号
							ps.setDouble(2, dtl.getActQty().longValue());
							ps.setDouble(3, dtl.getActQty().longValue());
							ps.setDouble(4, dtl.getActQty().longValue());
							ps.setString(5, bill.getBillNo());
							ps.setString(6, dtl.getStyleId());
							ps.setString(7, dtl.getColorId());
							ps.setString(8, dtl.getSizeId());
						}
					});
			/*
			 *
			 * 更改库存 *
			 */
			String chrsql = "update SPKCB set SL=sl+? where CKDM=? and SPDM=? and GG1DM=? and GG2DM=?";
			int[] update = this.tianTanJdbcTemplate.batchUpdate(chrsql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBillDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = listBillDtls.get(i);
							ps.setDouble(1, dtl.getActQty().longValue());
							ps.setString(
									2,
									bill.getDestId().substring(7,
											bill.getDestId().length()));// 收货方
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
						}
					});
			final List<BillDtl> tempDtls = new ArrayList<>();
			for (int i = 0; i < update.length; i++) {
				if (update[i] < 1) {
					tempDtls.add(listBillDtls.get(i));
				}
			}
			if (tempDtls.size() > 0) {
				String chrtempsql = "insert SPKCB(sl,CKDM,SPDM,GG1DM,GG2DM) values(?,?,?,?,?)";
				this.tianTanJdbcTemplate.batchUpdate(chrtempsql,
						new BatchPreparedStatementSetter() {
							@Override
							public int getBatchSize() {
								return tempDtls.size();
							}

							@Override
							public void setValues(PreparedStatement ps, int i)
									throws SQLException {
								BillDtl dtl = tempDtls.get(i);
								ps.setDouble(1, dtl.getActQty().longValue());
								ps.setString(
										2,
										bill.getDestId().substring(7,
												bill.getDestId().length()));// 收货方
								ps.setString(3, dtl.getStyleId());
								ps.setString(4, dtl.getColorId());
								ps.setString(5, dtl.getSizeId());
							}
						});
			}
		}

	}

	@Override
	public void batchOutBill(final Business bus) {
		final Bill bill = bus.getBill();
		if (CommonUtil.isNotBlank(bill)) {
			final List<BillDtl> listBillDtls = TiantanUtil.filterNotScan(bill);
			switch (Integer.parseInt(bill.getId().split("-")[0])) {
				case ConstantType.WH_RENTURN_VENDER:

					orderTable="PFJRD";
					orderDtlTable="PFJRDMX";
					mainTable = "PFXHD";
					detailTable = "PFXHDMX";
					mainNoticeTable = "FSEND";
					detailNoticeTable = "FSENDMX";
				/*
				 * 更改订单
				 */
				/*	String orderSql = PostSQL.getChOrderTableSQL(mainNoticeTable,
							orderTable);
							更改订单状态
							*/
					String orderSql = PostSQL.getSpChOrderTableSQL(mainNoticeTable,
							orderTable);
					this.tianTanJdbcTemplate.update(orderSql,
							new PreparedStatementSetter() {
								@Override
								public void setValues(PreparedStatement ps)
										throws SQLException {
									ps.setTimestamp(1, Timestamp.valueOf(CommonUtil
											.getDateString(new Date(),
													"yyyy-MM-dd 00:00:00")));
									ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
											.getDateString(new Date(),
													"yyyy-MM-dd 00:00:00")));
									ps.setString(3, JS_NAME);
									ps.setDouble(4, bill.getTotQty().longValue());
									ps.setDouble(5, bill.getActQty().longValue());
									ps.setString(6, bill.getBillNo());
								}
							});
					String orderDtlSql = PostSQL.getChOrderDetailTableSQL(
							orderDtlTable, mainNoticeTable);
					this.tianTanJdbcTemplate.batchUpdate(orderDtlSql,
							new BatchPreparedStatementSetter() {
								public int getBatchSize() {
									return listBillDtls.size();
								}

								public void setValues(PreparedStatement ps, int i)
										throws SQLException {
									BillDtl dtl = (BillDtl) listBillDtls.get(i);
									ps.setDouble(1, dtl.getQty().longValue());
									ps.setDouble(2, dtl.getActQty().longValue());
									ps.setString(3, bill.getBillNo());
									ps.setString(4, dtl.getStyleId());
									ps.setString(5, dtl.getColorId());
									ps.setString(6, dtl.getSizeId());
								}
							});
					break;
				case ConstantType.WH_RENTURN_SHOP:
					orderTable="PHJRD";
					orderDtlTable="PHJRDMX";

					mainTable = "SDPHD";
					detailTable = "SDPHDMX";
					mainNoticeTable = "PSEND";
					detailNoticeTable = "PSENDMX";
/*
				 * 更改订单
				 * 更改终止状态
				 */
					String orderSqlc = PostSQL.getSpChOrderTableSQL(mainNoticeTable,
							orderTable);
					this.tianTanJdbcTemplate.update(orderSqlc,
							new PreparedStatementSetter() {
								@Override
								public void setValues(PreparedStatement ps)
										throws SQLException {
									ps.setTimestamp(1, Timestamp.valueOf(CommonUtil
											.getDateString(new Date(),
													"yyyy-MM-dd 00:00:00")));
									ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
											.getDateString(new Date(),
													"yyyy-MM-dd 00:00:00")));
									ps.setString(3, JS_NAME);
									ps.setDouble(4, bill.getTotQty().longValue());
									ps.setDouble(5, bill.getActQty().longValue());
									ps.setString(6, bill.getBillNo());
								}
							});
					String orderDtlSqlc = PostSQL.getChOrderDetailTableSQL(
							orderDtlTable, mainNoticeTable);
					this.tianTanJdbcTemplate.batchUpdate(orderDtlSqlc,
							new BatchPreparedStatementSetter() {
								public int getBatchSize() {
									return listBillDtls.size();
								}

								public void setValues(PreparedStatement ps, int i)
										throws SQLException {
									BillDtl dtl = (BillDtl) listBillDtls.get(i);
									ps.setDouble(1, dtl.getQty().longValue());
									ps.setDouble(2, dtl.getActQty().longValue());
									ps.setString(3, bill.getBillNo());
									ps.setString(4, dtl.getStyleId());
									ps.setString(5, dtl.getColorId());
									ps.setString(6, dtl.getSizeId());
								}
							});

					break;
			}

			/*
			 * 更新通知单js=1;已执行 *
			 *
			 * 更改通知单状态终止
			 */
			/*String updateBillSql = PostSQL
					.getChMainNoticeTableSQL(mainNoticeTable);*/
			String updateBillSql = PostSQL
					.getSpChMainNoticeTableSQL(mainNoticeTable);
			this.tianTanJdbcTemplate.update(updateBillSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setTimestamp(1, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setString(3, JS_NAME);
							ps.setDouble(4, bill.getActQty());
							ps.setString(5, bill.getBillNo());
						}

					});
			String updateBillDtlSql = PostSQL
					.getChDetialNoticeTableSQL(detailNoticeTable);
			this.tianTanJdbcTemplate.batchUpdate(updateBillDtlSql,
					new BatchPreparedStatementSetter() {
						public int getBatchSize() {
							return listBillDtls.size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = (BillDtl) listBillDtls.get(i);
							/*
							 * ps.setString(1, String.valueOf(dtl.getQty()));
							 * ps.setString(2, String.valueOf(dtl.getQty()));
							 */
							ps.setDouble(1, dtl.getActQty().longValue());

							ps.setString(2, bill.getBillNo());
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
						}
					});
			/*
			 * 添加出库单
			 */
			String insertIn = PostSQL.getInsertMainTableSQL(mainTable,
					mainNoticeTable);

			this.tianTanJdbcTemplate.update(insertIn,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setString(1, "R3_" + bill.getBillNo());// DJBH 单号
							ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));// RQ 日期
							ps.setDouble(3, bill.getActQty().longValue());// SL 执行数量
							ps.setDouble(4, bill.getTotPrice().doubleValue());// JE 金额
							ps.setDouble(5, bill.getTotPrice().doubleValue());// BZJE 标准金额
							ps.setInt(6, 1);// YS 验收
							ps.setTimestamp(7, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));// YSRQ
							// 验收日期
							ps.setString(8, JS_NAME);// YES 验收人
							ps.setTimestamp(9, null);// JZRQ 记账日期
							ps.setString(10, null);// JZR 人
							ps.setTimestamp(11, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));// JZRQ
							// 执行日期
							ps.setString(12, JS_NAME);// JZRQ 执行人
							ps.setString(13, bill.getBillNo());

						}

					});

			String sql = PostSQL.getInsertDetailTableSQL(detailTable,
					detailNoticeTable);
			this.tianTanJdbcTemplate.batchUpdate(sql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBillDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = listBillDtls.get(i);
							ps.setString(1, "R3_" + bill.getBillNo());// DJBH 单号
							ps.setDouble(2, dtl.getActQty().longValue());
							ps.setDouble(3, dtl.getActQty().longValue());
							ps.setDouble(4, dtl.getActQty().longValue());
							ps.setString(5, bill.getBillNo());
							ps.setString(6, dtl.getStyleId());
							ps.setString(7, dtl.getColorId());
							ps.setString(8, dtl.getSizeId());
						}
					});
			/*
			 *
			 * 更改库存 *
			 */
			String chsql = "update SPKCB set SL=sl-? where CKDM=? and SPDM=? and GG1DM=? and GG2DM=?";
			this.tianTanJdbcTemplate.batchUpdate(chsql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBillDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = listBillDtls.get(i);
							ps.setDouble(1, dtl.getActQty().longValue());
							ps.setString(
									2,
									bill.getOrigId().substring(7,
											bill.getOrigId().length()));
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
						}
					});
		}

	}

	@Override
	public void batchReturnInBill(final Business bus) {
		final Bill bill = bus.getBill();

		if (CommonUtil.isNotBlank(bill)) {
			String orderSql = "";
			String orderDtlSql = "";
			final List<BillDtl> listBillDtls = TiantanUtil.filterNotScan(bill);
			orderTable = "FTSQD";
			orderDtlTable = "FTSQDMX";
			mainNoticeTable = "FTSND";
			detailNoticeTable = "FTSNDMX";
			mainTable = "PFTHD";
			detailTable = "PFTHDMX";
			orderSql = PostSQL.getChOrderTableSQL(mainNoticeTable, orderTable);
			orderDtlSql = PostSQL.getChOrderDetailTableSQL(orderDtlTable,
					mainNoticeTable);
			/*
			 * 添加出库单
			 */
			String insertIn = PostSQL.getInsertMainTableSQL(mainTable,
					mainNoticeTable);
			this.tianTanJdbcTemplate.update(insertIn,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setString(1, "R3_" + bill.getBillNo());// DJBH 单号
							ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));// RQ 日期
							ps.setDouble(3, bill.getActQty().longValue());// SL 执行数量
							ps.setDouble(4, bill.getTotPrice().doubleValue());// JE 金额
							ps.setDouble(5, bill.getTotPrice().doubleValue());// BZJE 标准金额
							ps.setInt(6, 1);// YS 验收
							ps.setTimestamp(7, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));// YSRQ
							// 验收日期
							ps.setString(8, JS_NAME);// YES 验收人
							ps.setTimestamp(9, null);// JZRQ 记账日期
							ps.setString(10, null);// JZR 人
							ps.setTimestamp(11, null);// JZRQ
							// 执行日期
							ps.setString(12, JS_NAME);// JZRQ 执行人
							ps.setString(13, bill.getBillNo());

						}

					});
			String sql = PostSQL.getInsertDetailTableSQL(detailTable,
					detailNoticeTable);
			this.tianTanJdbcTemplate.batchUpdate(sql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBillDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = listBillDtls.get(i);
							ps.setString(1, "R3_" + bill.getBillNo());// DJBH 单号
							ps.setDouble(2, dtl.getActQty().longValue());
							ps.setDouble(3, dtl.getActQty().longValue());
							ps.setDouble(4, dtl.getActQty().longValue());
							ps.setString(5, bill.getBillNo());
							ps.setString(6, dtl.getStyleId());
							ps.setString(7, dtl.getColorId());
							ps.setString(8, dtl.getSizeId());
						}
					});
			/*
			 * 更改订单
			 */
			int jj = this.tianTanJdbcTemplate.update(orderSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setTimestamp(1, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setString(2, JS_NAME);
							ps.setDouble(3, bill.getTotQty().longValue());
							ps.setDouble(4, bill.getActQty().longValue());
							ps.setString(5, bill.getBillNo());

						}
					});
			int[] ij = this.tianTanJdbcTemplate.batchUpdate(orderDtlSql,
					new BatchPreparedStatementSetter() {
						public int getBatchSize() {
							return listBillDtls.size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = (BillDtl) listBillDtls.get(i);
							ps.setDouble(1, dtl.getQty().longValue());
							ps.setDouble(2, dtl.getActQty().longValue());
							ps.setString(3, bill.getBillNo());
							ps.setString(4, dtl.getStyleId());
							ps.setString(5, dtl.getColorId());
							ps.setString(6, dtl.getSizeId());
						}
					});

			/*
			 * 更新通知单js=1;已执行 *
			 */
			String updateBillSql = PostSQL
					.getChMainNoticeTableSQL(mainNoticeTable);
			this.tianTanJdbcTemplate.update(updateBillSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setTimestamp(1, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setString(2, JS_NAME);
							ps.setDouble(3, bill.getActQty().longValue());
							ps.setString(4, bill.getBillNo());
						}

					});
			String updateBillDtlSql = PostSQL
					.getChDetialNoticeTableSQL(detailNoticeTable);
			this.tianTanJdbcTemplate.batchUpdate(updateBillDtlSql,
					new BatchPreparedStatementSetter() {
						public int getBatchSize() {
							return listBillDtls.size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = (BillDtl) listBillDtls.get(i);
							/*
							 * ps.setString(1, String.valueOf(dtl.getQty()));
							 * ps.setString(2, String.valueOf(dtl.getQty()));
							 */
							ps.setDouble(1, dtl.getActQty().longValue());
							ps.setString(2, bill.getBillNo());
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
						}
					});

			/*
			 *
			 * 更改库存 *
			 */
			String chsql = "update SPKCB set SL=sl+? where CKDM=? and SPDM=? and GG1DM=? and GG2DM=?";
			int[] update = this.tianTanJdbcTemplate.batchUpdate(chsql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBillDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = listBillDtls.get(i);
							ps.setDouble(1, dtl.getActQty().longValue());
							ps.setString(
									2,
									bill.getDestId().substring(7,
											bill.getDestId().length()));
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
						}
					});
			final List<BillDtl> tempDtls = new ArrayList<>();
			for (int i = 0; i < update.length; i++) {
				if (update[i] < 1) {
					tempDtls.add(listBillDtls.get(i));
				}
			}
			if (tempDtls.size() > 0) {
				String chrtempsql = "insert SPKCB(sl,CKDM,SPDM,GG1DM,GG2DM) values(?,?,?,?,?)";
				this.tianTanJdbcTemplate.batchUpdate(chrtempsql,
						new BatchPreparedStatementSetter() {
							@Override
							public int getBatchSize() {
								return tempDtls.size();
							}

							@Override
							public void setValues(PreparedStatement ps, int i)
									throws SQLException {
								BillDtl dtl = tempDtls.get(i);
								ps.setDouble(1, dtl.getActQty().longValue());
								ps.setString(
										2,
										bill.getDestId().substring(7,
												bill.getDestId().length()));
								ps.setString(3, dtl.getStyleId());
								ps.setString(4, dtl.getColorId());
								ps.setString(5, dtl.getSizeId());
							}
						});
			}
		}
	}

	public void batchReturnInFromShopBill(final Business bus) {
		final Bill bill = bus.getBill();

		if (CommonUtil.isNotBlank(bill)) {
			orderTable = "PTSQD";
			orderDtlTable = "PTSQDMX";
			mainNoticeTable = "PTSND";
			detailNoticeTable = "PTSNDMX";
			mainTable = "SDTHD";
			detailTable = "SDTHDMX";
			final List<BillDtl> listBillDtls = TiantanUtil.filterNotScan(bill);

			/**
			 * 添加差异
			 * */
			String insertDetail = "insert into SDTHDRKMX(DJBH,MXBH,SPDM,GG1DM,GG2DM,sl) values(?,?,?,?,?,?)";
			int gg[]=	this.tianTanJdbcTemplate.batchUpdate(insertDetail,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBillDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = listBillDtls.get(i);
							ps.setString(1, bill.getBillNo());

							ps.setInt(2, i);
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
							ps.setDouble(6, dtl.getActQty());
						}
					});
			/*
			 * 更改订单
			 */
/*
			String orderSql = PostSQL.getChOrderTableSQL(mainTable, orderTable);
			终止sp
*/
			String orderSql = PostSQL.getAfterSpChOrderTableSQL(mainTable, orderTable);

			String orderDtlSql = PostSQL.getAfterChOrderDetailTableSQL(	orderDtlTable, mainTable);
			int jj = this.tianTanJdbcTemplate.update(orderSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setTimestamp(1, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setString(3, JS_NAME);
							ps.setDouble(4, bill.getTotQty().longValue());
							ps.setString(5, bill.getBillNo());
						}
					});
			int[] ij = this.tianTanJdbcTemplate.batchUpdate(orderDtlSql,
					new BatchPreparedStatementSetter() {
						public int getBatchSize() {
							return listBillDtls.size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = (BillDtl) listBillDtls.get(i);
							ps.setDouble(1, dtl.getQty().longValue());
							ps.setString(2, bill.getBillNo());
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
						}
					});

			/*
			 * 更新通知单js=1;已执行 *
			 */
			/*String updateBillSql = PostSQL
					.getChReturnInMainNoticeTableSQL(mainTable);*/
			String updateBillSql = PostSQL
					.getSpChReturnInMainNoticeTableSQL(mainTable);
			int dd=this.tianTanJdbcTemplate.update(updateBillSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setTimestamp(1, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setString(3, JS_NAME);
							ps.setDouble(4, bill.getActQty().longValue());
							ps.setString(5, bill.getBillNo());

						}

					});
			String updateBillDtlSql = PostSQL
					.getChReturnInDetialNoticeTableSQL(detailNoticeTable,mainTable);
			int []dd2=this.tianTanJdbcTemplate.batchUpdate(updateBillDtlSql,
					new BatchPreparedStatementSetter() {
						public int getBatchSize() {
							return listBillDtls.size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = (BillDtl) listBillDtls.get(i);
							ps.setDouble(1, dtl.getQty().longValue());

/*
							ps.setDouble(1, dtl.getActQty().longValue());
*/
							ps.setString(2, bill.getBillNo());
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
						}
					});

			/**
			 * 更改库存
			 * */
			String chsql = "update SPKCB set SL=sl+? where CKDM=? and SPDM=? and GG1DM=? and GG2DM=?";
			int[] update = this.tianTanJdbcTemplate.batchUpdate(chsql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBillDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = listBillDtls.get(i);
							ps.setDouble(1, dtl.getActQty().longValue());
							ps.setString(
									2,
									bill.getDestId().substring(7,
											bill.getDestId().length()));
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
						}
					});
			final List<BillDtl> tempDtls = new ArrayList<>();
			for (int i = 0; i < update.length; i++) {
				if (update[i] < 1) {
					tempDtls.add(listBillDtls.get(i));
				}
			}
			if (tempDtls.size() > 0) {
				String chrtempsql = "insert SPKCB(sl,CKDM,SPDM,GG1DM,GG2DM) values(?,?,?,?,?)";
				this.tianTanJdbcTemplate.batchUpdate(chrtempsql,
						new BatchPreparedStatementSetter() {
							@Override
							public int getBatchSize() {
								return tempDtls.size();
							}

							@Override
							public void setValues(PreparedStatement ps, int i)
									throws SQLException {
								BillDtl dtl = tempDtls.get(i);
								ps.setDouble(1, dtl.getActQty().longValue());
								ps.setString(
										2,
										bill.getDestId().substring(7,
												bill.getDestId().length()));
								ps.setString(3, dtl.getStyleId());
								ps.setString(4, dtl.getColorId());
								ps.setString(5, dtl.getSizeId());
							}
						});
			}
		}

	}

	@Override
	public void batchReturnOutBill(final Business bus) {
		final Bill bill = bus.getBill();
		if (CommonUtil.isNotBlank(bill)) {
			orderTable = "JTSQD";
			orderDtlTable = "JTSQDMX";
			mainNoticeTable = "JTSND";
			detailNoticeTable = "JTSNDMX";
			mainTable = "SPTHD";
			detailTable = "SPTHDMX";
			final List<BillDtl> listBillDtls = TiantanUtil.filterNotScan(bill);
			/*
			 * 更改订单
			 */
			/*String orderSql = PostSQL.getChOrderTableSQL(mainNoticeTable,
					orderTable);
					更改终止状态
					*/
			String orderSql = PostSQL.getSpChOrderTableSQL(mainNoticeTable,
					orderTable);
			this.tianTanJdbcTemplate.update(orderSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setTimestamp(1, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setString(3, JS_NAME);
							ps.setDouble(4, bill.getTotQty().longValue());
							ps.setDouble(5, bill.getActQty());
							ps.setString(6, bill.getBillNo());
						}
					});
			String orderDtlSql = PostSQL.getChOrderDetailTableSQL(
					orderDtlTable, mainNoticeTable);
			this.tianTanJdbcTemplate.batchUpdate(orderDtlSql,
					new BatchPreparedStatementSetter() {
						public int getBatchSize() {
							return listBillDtls.size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = (BillDtl) listBillDtls.get(i);
							ps.setDouble(1, dtl.getQty().longValue());
							ps.setDouble(2, dtl.getActQty().longValue());
							ps.setString(3, bill.getBillNo());
							ps.setString(4, dtl.getStyleId());
							ps.setString(5, dtl.getColorId());
							ps.setString(6, dtl.getSizeId());
						}
					});

			/*
			 * 更新通知单js=1;已执行 *
			 */
		/*	String updateBillSql = PostSQL
					.getSpChMainNoticeTableSQL(mainNoticeTable);
					添加终止状态
					*/
			String updateBillSql = PostSQL
					.getSpChMainNoticeTableSQL(mainNoticeTable);
			this.tianTanJdbcTemplate.update(updateBillSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setTimestamp(1, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setString(3, JS_NAME);
							ps.setDouble(4, bill.getActQty().longValue());
							ps.setString(5, bill.getBillNo());
						}

					});
			String updateBillDtlSql = PostSQL
					.getChDetialNoticeTableSQL(detailNoticeTable);
			this.tianTanJdbcTemplate.batchUpdate(updateBillDtlSql,
					new BatchPreparedStatementSetter() {
						public int getBatchSize() {
							return listBillDtls.size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = (BillDtl) listBillDtls.get(i);
							/*
							 * ps.setString(1, String.valueOf(dtl.getQty()));
							 * ps.setString(2, String.valueOf(dtl.getQty()));
							 */
							ps.setDouble(1, dtl.getActQty().longValue());

							ps.setString(2, bill.getBillNo());
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
						}
					});
			/*
			 * 添加出库单
			 */
			String insertIn = PostSQL.getInsertMainTableSQL(mainTable,
					mainNoticeTable);

			this.tianTanJdbcTemplate.update(insertIn,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setString(1, "R3_" + bill.getBillNo());// DJBH 单号
							ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));// RQ 日期
							ps.setDouble(3, bill.getActQty().longValue());// SL 执行数量
							ps.setDouble(4, bill.getTotPrice().doubleValue());// JE 金额
							ps.setDouble(5, bill.getTotPrice().doubleValue());// BZJE 标准金额
							ps.setInt(6, 1);// YS 验收
							ps.setTimestamp(7, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));// YSRQ
							// 验收日期
							ps.setString(8, JS_NAME);// YES 验收人
							ps.setTimestamp(9, null);// JZRQ 记账日期
							ps.setString(10, null);// JZR 人
							ps.setTimestamp(11, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));// JZRQ
							// 执行日期
							ps.setString(12, JS_NAME);// JZRQ 执行人
							ps.setString(13, bill.getBillNo());

						}

					});
			String sql = PostSQL.getInsertDetailTableSQL(detailTable,
					detailNoticeTable);
			this.tianTanJdbcTemplate.batchUpdate(sql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBillDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = listBillDtls.get(i);
							ps.setString(1, "R3_" + bill.getBillNo());// DJBH 单号
							ps.setDouble(2, dtl.getActQty().longValue());
							ps.setDouble(3, dtl.getActQty().longValue());
							ps.setDouble(4, dtl.getActQty().longValue());
							ps.setString(5, bill.getBillNo());
							ps.setString(6, dtl.getStyleId());
							ps.setString(7, dtl.getColorId());
							ps.setString(8, dtl.getSizeId());
						}
					});
			/*
			 *
			 * 更改库存 *
			 */
			String chrsql = "update SPKCB set SL=sl-? where CKDM=? and SPDM=? and GG1DM=? and GG2DM=?";
			this.tianTanJdbcTemplate.batchUpdate(chrsql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBillDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = listBillDtls.get(i);
							ps.setDouble(1, dtl.getActQty().longValue());
							ps.setString(
									2,
									bill.getOrigId().substring(7,
											bill.getOrigId().length()));
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
						}
					});

		}
	}

	@Override
	public void batchTransferInBill(final Business bus) {
		final Bill bill = bus.getBill();
		if (CommonUtil.isNotBlank(bill)) {
			orderTable = "YCJRD";
			orderDtlTable = "YCJRDMX";
			mainNoticeTable = "YSEND";
			detailNoticeTable = "YSENDMX";
			mainTable = "SPYCD";
			detailTable = "SPYCDMX";
			final List<BillDtl> listBillDtls = TiantanUtil.filterNotScan(bill);

			/*
			 * 更改订单
			 */
		/*	String orderSql = PostSQL.getChOrderTableSQL(mainNoticeTable,
					orderTable);
			int ij = this.tianTanJdbcTemplate.update(orderSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setTimestamp(1, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setString(2, JS_NAME);
							ps.setDouble(3, bus.getBill().getTotQty().longValue());
							ps.setDouble(4, bus.getBill().getActQty().longValue());
							if (bus.getBill().getBillNo().contains("R3_")) {
								ps.setString(
										5,
										bus.getBill()
												.getBillNo()
												.substring(
														3,
														bus.getBill()
																.getBillNo()
																.length()));
							} else {
								ps.setString(5, bus.getBill().getBillNo());
							}
						}
					});
			String orderDtlSql = PostSQL.getChOrderDetailTableSQL(
					orderDtlTable, mainNoticeTable);
			int[] ji = this.tianTanJdbcTemplate.batchUpdate(orderDtlSql,
					new BatchPreparedStatementSetter() {
						public int getBatchSize() {
							return bus.getBill().getDtlList().size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = (BillDtl) bus.getBill().getDtlList()
									.get(i);
							ps.setDouble(1, dtl.getQty().longValue());
							ps.setDouble(2, dtl.getActQty().longValue());
							ps.setString(
									3,
									bus.getBill()
											.getBillNo()
											.substring(
													3,
													bus.getBill().getBillNo()
															.length()));
							ps.setString(4, dtl.getStyleId());
							ps.setString(5, dtl.getColorId());
							ps.setString(6, dtl.getSizeId());
						}
					});
*/
			/*
			 * 添加出库单
			 */
			String insertIn = String.format(
					"update %s set SH=1,SHRQ=?,SHR=? where DJBH=?", mainTable);

			this.tianTanJdbcTemplate.update(insertIn,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setTimestamp(1, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));// SHRQ 日期
							ps.setString(2, JS_NAME);// JZRQ 执行人
							ps.setString(3, bill.getBillNo());
						}
					});
			String insertDetail = "insert into SPYCDRKMX(DJBH,MXBH,SPDM,GG1DM,GG2DM,sl) values(?,?,?,?,?,?)";
			this.tianTanJdbcTemplate.batchUpdate(insertDetail,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBillDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = listBillDtls.get(i);
							ps.setString(1, bus.getBill().getBillNo());
							ps.setInt(2, i);
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
							ps.setDouble(6, dtl.getActQty());
						}
					});
			/*
			 *
			 * 更改库存 *
			 */
			String chrsql = "update SPKCB set SL=sl+? where CKDM=? and SPDM=? and GG1DM=? and GG2DM=?";
			int[] update = this.tianTanJdbcTemplate.batchUpdate(chrsql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBillDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = listBillDtls.get(i);
							ps.setDouble(1, dtl.getActQty().longValue());
							ps.setString(
									2,
									bill.getDestId().substring(7,
											bill.getDestId().length()));
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
						}
					});
			final List<BillDtl> tempDtls = new ArrayList<>();
			for (int i = 0; i < update.length; i++) {
				if (update[i] < 1) {
					tempDtls.add(listBillDtls.get(i));
				}
			}
			if (tempDtls.size() > 0) {
				String chrtempsql = "insert SPKCB(sl,CKDM,SPDM,GG1DM,GG2DM) values(?,?,?,?,?)";
				this.tianTanJdbcTemplate.batchUpdate(chrtempsql,
						new BatchPreparedStatementSetter() {
							@Override
							public int getBatchSize() {
								return tempDtls.size();
							}

							@Override
							public void setValues(PreparedStatement ps, int i)
									throws SQLException {
								BillDtl dtl = tempDtls.get(i);
								ps.setDouble(1, dtl.getActQty().longValue());
								ps.setString(
										2,
										bill.getDestId().substring(7,
												bill.getDestId().length()));
								ps.setString(3, dtl.getStyleId());
								ps.setString(4, dtl.getColorId());
								ps.setString(5, dtl.getSizeId());
							}
						});
			}

		}
	}

	@Override
	public void batchTransferOutBill(final Business bus) {
		final Bill bill = bus.getBill();
		if (CommonUtil.isNotBlank(bill)) {
			orderTable = "YCJRD";
			orderDtlTable = "YCJRDMX";
			mainNoticeTable = "YSEND";
			detailNoticeTable = "YSENDMX";
			mainTable = "SPYCD";
			detailTable = "SPYCDMX";
			final List<BillDtl> listBillDtls = TiantanUtil.filterNotScan(bill);
			/*终止订单*/
			String orderSql = PostSQL.getSpChOrderTableSQL(mainNoticeTable,
					orderTable);
			int ij = this.tianTanJdbcTemplate.update(orderSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setTimestamp(1, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setString(3, JS_NAME);
							ps.setDouble(4, bus.getBill().getTotQty().longValue());
							ps.setDouble(5, bus.getBill().getActQty().longValue());
							ps.setString(6, bus.getBill().getBillNo());
						}
					});
			String orderDtlSql = PostSQL.getChOrderDetailTableSQL(
					orderDtlTable, mainNoticeTable);
			int[] ji = this.tianTanJdbcTemplate.batchUpdate(orderDtlSql,
					new BatchPreparedStatementSetter() {
						public int getBatchSize() {
							return bus.getBill().getDtlList().size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = (BillDtl) bus.getBill().getDtlList()
									.get(i);
							ps.setDouble(1, dtl.getQty().longValue());
							ps.setDouble(2, dtl.getActQty().longValue());
							ps.setString(3,bus.getBill().getBillNo());
							ps.setString(4, dtl.getStyleId());
							ps.setString(5, dtl.getColorId());
							ps.setString(6, dtl.getSizeId());
						}
					});

			/*
			 * 更新通知单js=1;已执行 *
			 */
			/*String updateBillSql = PostSQL
					.getChMainNoticeTableSQL(mainNoticeTable);
					终止sp
					*/
			String updateBillSql = PostSQL
					.getSpChMainNoticeTableSQL(mainNoticeTable);
			this.tianTanJdbcTemplate.update(updateBillSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setTimestamp(1, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setString(3, JS_NAME);
							ps.setDouble(4, bill.getActQty().longValue());

							ps.setString(5, bill.getBillNo());
						}

					});
			String updateBillDtlSql = PostSQL
					.getChDetialNoticeTableSQL(detailNoticeTable);
			this.tianTanJdbcTemplate.batchUpdate(updateBillDtlSql,
					new BatchPreparedStatementSetter() {
						public int getBatchSize() {
							return listBillDtls.size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = (BillDtl) listBillDtls.get(i);
							/*
							 * ps.setString(1, String.valueOf(dtl.getQty()));
							 * ps.setString(2, String.valueOf(dtl.getQty()));
							 */
							ps.setDouble(1, dtl.getActQty().longValue());
							ps.setString(2, bill.getBillNo());
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
						}
					});
			/*
			 * 添加出库单
			 */
			String insertIn = PostSQL.getInsertMainTableSQL(mainTable,
					mainNoticeTable);

			this.tianTanJdbcTemplate.update(insertIn,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setString(1, "R3_" + bill.getBillNo());// DJBH 单号
							ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));// RQ 日期
							ps.setDouble(3, bill.getActQty().longValue());// SL 执行数量
							ps.setDouble(4, bill.getTotPrice().doubleValue());// JE 金额
							ps.setDouble(5, bill.getTotPrice().doubleValue());// BZJE 标准金额
							ps.setInt(6, 1);// YS 验收
							ps.setTimestamp(7, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));// YSRQ
							// 验收日期
							ps.setString(8, JS_NAME);// YES 验收人
							ps.setTimestamp(9, null);// JZRQ 记账日期
							ps.setString(10, null);// JZR 人
							ps.setTimestamp(11, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));// JZRQ
							// 执行日期
							ps.setString(12, JS_NAME);// JZRQ 执行人
							ps.setString(13, bill.getBillNo());

						}

					});
			String sql = PostSQL.getInsertDetailTableSQL(detailTable,
					detailNoticeTable);
			this.tianTanJdbcTemplate.batchUpdate(sql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBillDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = listBillDtls.get(i);
							ps.setString(1, "R3_" + bill.getBillNo());// DJBH 单号
							ps.setDouble(2, dtl.getActQty().longValue());
							ps.setDouble(3, dtl.getActQty().longValue());
							ps.setDouble(4, dtl.getActQty().longValue());
							ps.setString(5, bill.getBillNo());
							ps.setString(6, dtl.getStyleId());
							ps.setString(7, dtl.getColorId());
							ps.setString(8, dtl.getSizeId());
						}
					});
			/*
			 *
			 * 更改库存 *
			 */
			String chrsql = "update SPKCB set SL=sl-? where CKDM=? and SPDM=? and GG1DM=? and GG2DM=?";
			this.tianTanJdbcTemplate.batchUpdate(chrsql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBillDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = listBillDtls.get(i);
							ps.setDouble(1, dtl.getActQty().longValue());
							ps.setString(
									2,
									bill.getOrigId().substring(7,
											bill.getOrigId().length()));
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
						}
					});

		}
	}

	@Override
	public void batchInventory(final Business business) {
		final List<BusinessDtl> dtls = business.getDtlList();
		String mainSql = "insert into CKPDD(DJBH,RQ,YDJH,DM1,DM2,DM4,QDDM,QYDM,YGDM,"
				+ "SL,JE,BZJE,LL,BYZD12,RFID_LAST_UPDATE_DATETIME,BYZD1,ZDR,RQ_4) "
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,1,?,?,3,?,?)";

		TiantanUtil.countTotPrice(business);
		int i = this.tianTanJdbcTemplate.update(mainSql,
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps)
							throws SQLException {
						business.setBillNo("R3"
								+ String.valueOf(new Date().getTime()));
						ps.setString(1, business.getBillNo());

						ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
								.getDateString(new Date(),
										"yyyy-MM-dd 00:00:00")));
						ps.setString(3, business.getBillNo());
						ps.setString(4, "000");
						ps.setString(
								5,
								business.getOrigId().substring(7,
										business.getOrigId().length()));
						ps.setString(6, "000");
						ps.setString(7, "000");
						ps.setString(8, "000");
						ps.setString(9, "000");
						ps.setDouble(10, business.getTotEpc());
						ps.setDouble(11, business.getTotPrice());
						ps.setDouble(12, business.getTotPrice());
						ps.setDouble(13, 1);
						ps.setTimestamp(14, Timestamp.valueOf(CommonUtil
								.getDateString(new Date(),
										"yyyy-MM-dd 00:00:00")));
						ps.setString(15, business.getDeviceId());
						ps.setTimestamp(16, Timestamp.valueOf(CommonUtil
								.getDateString(new Date(),
										"yyyy-MM-dd 00:00:00")));

					}
				});
		String detailSql = "insert into CKPDDMX(DJBH,MXBH,SPDM,GG1DM,GG2DM,SL,"
				+ "CKJ,ZK,DJ,JE,BZJE,HH,BYZD12) "
				+ "select ?,?,?,?,?,?,s.bzsj,1,s.bzsj,s.bzsj*?,s.bzsj*?,1,1 "
				+ " from shangpin s where s.spdm=? ";
		int[] dtlc = this.tianTanJdbcTemplate.batchUpdate(detailSql,
				new BatchPreparedStatementSetter() {
					@Override
					public int getBatchSize() {
						return dtls.size();
					}

					@Override
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						BusinessDtl dtl = dtls.get(i);
						ps.setString(1, business.getBillNo());// DJBH 单号
						ps.setDouble(2, i);
						ps.setString(3, dtl.getStyleId());
						ps.setString(4, dtl.getColorId());
						ps.setString(5, dtl.getSizeId());
						ps.setDouble(6, dtl.getQty());
						ps.setDouble(7, dtl.getQty());
						ps.setDouble(8, dtl.getQty());
						ps.setString(9, dtl.getStyleId());
					}
				});
		System.out.println("");
	}

	@Override
	public void batchShopInBill(final Business bus) {
		final Bill bill = bus.getBill();
		if (CommonUtil.isNotBlank(bill)) {
			orderTable = "PHJRD";
			orderDtlTable = "PHJRDMX";

			mainTable = "SDPHD";
			detailTable = "SDPHDMX";
			mainNoticeTable = "PSEND";
			detailNoticeTable = "PSENDMX";
			final List<BusinessDtl> listBusDtls = bus.getDtlList();
			final List<BillDtl> listBillDtls = bus.getSrcBill().getDtlList();

			/*
			 * 更改订单
			 * 取消订单
			 */
	/*		String orderSql = PostSQL.getChOrderTableSQL(mainNoticeTable,
					orderTable);
			this.tianTanJdbcTemplate.update(orderSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setTimestamp(1, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setString(2, JS_NAME);
							ps.setDouble(3, bus.getSrcBill().getTotQty());
							ps.setDouble(4, bus.getSrcBill().getActQty());
							ps.setString(5, bus.getSrcBill().getBillNo());
						}
					});
			String orderDtlSql = PostSQL.getChOrderDetailTableSQL(
					orderDtlTable, mainNoticeTable);
			this.tianTanJdbcTemplate.batchUpdate(orderDtlSql,
					new BatchPreparedStatementSetter() {
						public int getBatchSize() {
							return listBusDtls.size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = (BillDtl) listBillDtls.get(i);
							ps.setDouble(1, dtl.getQty());
							ps.setDouble(2, dtl.getActQty());
							ps.setString(3, bus.getSrcBill().getBillNo());
							ps.setString(4, dtl.getStyleId());
							ps.setString(5, dtl.getColorId());
							ps.setString(6, dtl.getSizeId());
						}
					});*/

			/*
			 * 添加出库单
			 */
			String insertIn = String.format(
					"update %s set SH=1,SHRQ=?,SHR=? where DJBH=?", mainTable);

			this.tianTanJdbcTemplate.update(insertIn,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setTimestamp(1, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));// SHRQ 日期
							ps.setString(2, JS_NAME);// JZRQ 执行人
							ps.setString(3, "R3_"
									+ bus.getSrcBill().getBillNo());
						}
					});
			String insertDetail = "insert into SDPHDRKMX(DJBH,MXBH,SPDM,GG1DM,GG2DM,sl,BYZD6)"
					+ " select ?,?,?,?,?,?,SPMC from shangpin s where s.spdm=? ";
			int up[]=this.tianTanJdbcTemplate.batchUpdate(insertDetail,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBusDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BusinessDtl dtl = listBusDtls.get(i);
							ps.setString(1, bill.getSrcBillNo());
							ps.setInt(2, i);
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
							ps.setDouble(6, dtl.getQty());
							ps.setString(7, dtl.getStyleId());

						}
					});
			/*
			 *
			 * 更改库存 *
			 */
			String chrsql = "update SPKCB set SL=sl+? where CKDM=? and SPDM=? and GG1DM=? and GG2DM=?";
			int[] update = this.tianTanJdbcTemplate.batchUpdate(chrsql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBusDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BusinessDtl dtl = listBusDtls.get(i);
							ps.setDouble(1, dtl.getQty());
							ps.setString(
									2,
									bill.getDestId().substring(7,
											bill.getDestId().length()));
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
						}
					});
			final List<BusinessDtl> tempDtls = new ArrayList<>();
			for (int i = 0; i < update.length; i++) {
				if (update[i] < 1) {
					tempDtls.add(listBusDtls.get(i));
				}
			}
			if (tempDtls.size() > 0) {
				String chrtempsql = "insert SPKCB(sl,CKDM,SPDM,GG1DM,GG2DM) values(?,?,?,?,?)";
				this.tianTanJdbcTemplate.batchUpdate(chrtempsql,
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
								ps.setString(
										2,
										bill.getDestId().substring(7,
												bill.getDestId().length()));
								ps.setString(3, dtl.getStyleId());
								ps.setString(4, dtl.getColorId());
								ps.setString(5, dtl.getSizeId());
							}
						});
			}
		}
	}

	@Override
	public void batchShopTransferInBill(final Business bus) {
		final Bill bill = bus.getBill();
		if (CommonUtil.isNotBlank(bill)) {
			orderTable = "YCJRD";
			orderDtlTable = "YCJRDMX";
			mainNoticeTable = "YSEND";
			detailNoticeTable = "YSENDMX";
			mainTable = "SPYCD";
			detailTable = "SPYCDMX";
			final List<BusinessDtl> listBusDtls = bus.getDtlList();
			final List<BillDtl> listBillDtls = bus.getSrcBill().getDtlList();

			/*
			 * 更改订单
			 */
			String orderSql = PostSQL.getChOrderTableSQL(mainNoticeTable,
					orderTable);
			this.tianTanJdbcTemplate.update(orderSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setTimestamp(1, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setString(2, JS_NAME);
							ps.setDouble(3, bus.getSrcBill().getTotQty());
							ps.setDouble(4, bus.getSrcBill().getActQty());
							ps.setString(5, bus.getSrcBill().getBillNo());
						}
					});
			String orderDtlSql = PostSQL.getChOrderDetailTableSQL(
					orderDtlTable, mainNoticeTable);
			this.tianTanJdbcTemplate.batchUpdate(orderDtlSql,
					new BatchPreparedStatementSetter() {
						public int getBatchSize() {
							return listBusDtls.size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = (BillDtl) listBillDtls.get(i);
							ps.setDouble(1, dtl.getQty());
							ps.setDouble(2, dtl.getActQty());
							ps.setString(3, bus.getSrcBill().getBillNo());
							ps.setString(4, dtl.getStyleId());
							ps.setString(5, dtl.getColorId());
							ps.setString(6, dtl.getSizeId());
						}
					});

			/*
			 * 添加出库单
			 */
			String insertIn = String.format(
					"update %s set SH=1,SHRQ=?,SHR=? where DJBH=?", mainTable);

			int ide = this.tianTanJdbcTemplate.update(insertIn,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setTimestamp(1, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));// SHRQ 日期
							ps.setString(2, JS_NAME);// JZRQ 执行人
							ps.setString(3, "R3_"
									+ bus.getSrcBill().getBillNo());
						}
					});
			String insertDetail = "insert into SPYCDRKMX(DJBH,MXBH,SPDM,GG1DM,GG2DM,sl) values(?,?,?,?,?,?)";
			int[] jj = this.tianTanJdbcTemplate.batchUpdate(insertDetail,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBillDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BusinessDtl dtl = listBusDtls.get(i);
							ps.setString(1, bus.getSrcBill().getBillNo());
							ps.setInt(2, i);
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
							ps.setDouble(6, dtl.getQty());
						}
					});
			/*
			 *
			 * 更改库存 *
			 */
			String chrsql = "update SPKCB set SL=sl+? where CKDM=? and SPDM=? and GG1DM=? and GG2DM=?";
			int[] update = this.tianTanJdbcTemplate.batchUpdate(chrsql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBusDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BusinessDtl dtl = listBusDtls.get(i);
							ps.setDouble(1, dtl.getQty());
							ps.setString(
									2,
									bill.getDestId().substring(7,
											bill.getDestId().length()));
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
						}
					});
			final List<BusinessDtl> tempDtls = new ArrayList<>();
			for (int i = 0; i < update.length; i++) {
				if (update[i] < 1) {
					tempDtls.add(listBusDtls.get(i));
				}
			}
			if (tempDtls.size() > 0) {
				String chrtempsql = "insert SPKCB(sl,CKDM,SPDM,GG1DM,GG2DM) values(?,?,?,?,?)";
				this.tianTanJdbcTemplate.batchUpdate(chrtempsql,
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
								ps.setString(
										2,
										bill.getDestId().substring(7,
												bill.getDestId().length()));
								ps.setString(3, dtl.getStyleId());
								ps.setString(4, dtl.getColorId());
								ps.setString(5, dtl.getSizeId());
							}
						});
			}
		}
	}

	@Override
	public void batchShopReturnOutBill(Business bus) {

		final Bill bill = bus.getBill();
		if (CommonUtil.isNotBlank(bill)) {
			mainNoticeTable = "PTSND";
			detailNoticeTable = "PTSNDMX";
			mainTable = "SDTHD";
			detailTable = "SDTHDMX";
			final List<BillDtl> listBillDtls = TiantanUtil.filterNotScan(bill);

			/*
			 * 更新通知单js=1;已执行 *
			 */
			String updateBillSql = PostSQL
					.getChMainNoticeTableSQL(mainNoticeTable);
			int dd=this.tianTanJdbcTemplate.update(updateBillSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setTimestamp(1, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setString(2, JS_NAME);
							ps.setDouble(3, bill.getActQty());

							ps.setString(4, bill.getBillNo());
						}

					});
			String updateBillDtlSql = PostSQL
					.getChDetialNoticeTableSQL(detailNoticeTable);
			int []dd2=this.tianTanJdbcTemplate.batchUpdate(updateBillDtlSql,
					new BatchPreparedStatementSetter() {
						public int getBatchSize() {
							return listBillDtls.size();
						}

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = (BillDtl) listBillDtls.get(i);
							/*
							 * ps.setString(1, String.valueOf(dtl.getQty()));
							 * ps.setString(2, String.valueOf(dtl.getQty()));
							 */
							ps.setDouble(1, dtl.getActQty());
							ps.setString(2, bill.getBillNo());
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
						}
					});
			/*
			 * 添加出库单
			 */
			String insertIn = PostSQL.getInsertShopMainTableSQL(mainTable,
					mainNoticeTable);

			this.tianTanJdbcTemplate.update(insertIn,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setString(1, "R3_" + bill.getBillNo());// DJBH 单号
							ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));// RQ 日期
							ps.setDouble(3, bill.getActQty());// SL 执行数量
							ps.setDouble(4, bill.getTotPrice());// JE 金额
							ps.setDouble(5, bill.getTotPrice());// BZJE 标准金额
							ps.setInt(6, 0);// YS 验收
							ps.setTimestamp(7, null);// YSRQ
							// 验收日期
							ps.setString(8, null);// YES 验收人
							ps.setTimestamp(9, null);// JZRQ 记账日期
							ps.setString(10, null);// JZR 人
							ps.setTimestamp(11, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));// JZRQ
							// 执行日期
							ps.setString(12, JS_NAME);// JZRQ 执行人
							ps.setTimestamp(13, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));// JZRQ
							// 执行日期
							ps.setString(14, JS_NAME);// JZRQ 执行人
							ps.setString(15, bill.getBillNo());
						}

					});
			String sql = PostSQL.getInsertDetailTableSQL(detailTable,
					detailNoticeTable);
			this.tianTanJdbcTemplate.batchUpdate(sql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBillDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = listBillDtls.get(i);
							ps.setString(1, "R3_" + bill.getBillNo());// DJBH 单号
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
			 * 更改库存 *
			 */
			String chrsql = "update SPKCB set SL=sl-? where CKDM=? and SPDM=? and GG1DM=? and GG2DM=?";
			this.tianTanJdbcTemplate.batchUpdate(chrsql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBillDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = listBillDtls.get(i);
							ps.setDouble(1, dtl.getActQty());
							ps.setString(
									2,
									bill.getOrigId().substring(7,
											bill.getOrigId().length()));
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
						}
					});

		}
	}

	@Override
	public void batchInFromFactoryBill(final Business bus) {
		final Bill bill = bus.getBill();
		if (CommonUtil.isNotBlank(bill)) {
			mainNoticeTable = "SCZZD";
			detailNoticeTable = "SCZZDMX";
			mainTable = "CPRKD";
			detailTable = "CPRKDMX";
			final List<BillDtl> listBillDtls = TiantanUtil.filterNotScan(bill);
			final String billNo="R3_" + String.valueOf(new Date().getTime());
			/*
			 * 添加入库单
			 */
			String insertIn = "insert into CPRKD(DJBH,RQ,YDJH,LXDJ,Dm1,DM2,QDDM,sl,ys,ysrq,ysr,bz,BYZD3,JE,bzje,LL)"
					+ " select ?,?,n.YDJH,n.DJBH,n.GCDM,?,'000',?,?,?,?,n.bz,n.LXDJ,?,?,? from SCZZD n where n.djbh=?";

			this.tianTanJdbcTemplate.update(insertIn,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setString(1, billNo);// DJBH 单号
							ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));// RQ 日期
							ps.setString(
									3,
									bill.getDestId().substring(7,
											bill.getDestId().length()));// DJBH
							// 单号
							ps.setDouble(4, bill.getActQty());
							ps.setInt(5, 1);
							ps.setTimestamp(6, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));// YSRQ
							// 验收日期
							ps.setString(7, JS_NAME);// YES 验收人
							ps.setDouble(8, bill.getTotPrice());
							ps.setDouble(9, bill.getTotPrice());
							ps.setInt(10, 1);
							ps.setString(11, bill.getBillNo());

						}

					});

			String sql = "insert into CPRKDMX(DJBH,MXBH,SPDM,GG1DM,GG2DM,SL,dj,JE,bzje) "
					+ " select ?,?,SPDM,GG1DM,GG2DM,?,?,?,? from SCZZDMX "
					+ " where DJbh=? and spdm=? and gg1dm=? and gg2dm=?";
			this.tianTanJdbcTemplate.batchUpdate(sql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBillDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = listBillDtls.get(i);
							ps.setString(1, billNo);// DJBH 单号
							ps.setDouble(2, i);
							ps.setDouble(3, dtl.getActQty());
							ps.setDouble(4, dtl.getPrice());

							ps.setDouble(5, dtl.getPrice() * dtl.getActQty());
							ps.setDouble(6, dtl.getPrice() * dtl.getActQty());

							ps.setString(7, bill.getBillNo());
							ps.setString(8, dtl.getStyleId());
							ps.setString(9, dtl.getColorId());
							ps.setString(10, dtl.getSizeId());
						}
					});
			/*
			 *
			 * 更改库存 *
			 */
			String chrsql = "update SPKCB set SL=sl+? where CKDM=? and SPDM=? and GG1DM=? and GG2DM=?";
			int[] update = this.tianTanJdbcTemplate.batchUpdate(chrsql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return listBillDtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = listBillDtls.get(i);
							ps.setDouble(1, dtl.getActQty());
							ps.setString(
									2,
									bill.getDestId().substring(7,
											bill.getDestId().length()));// 收货方
							ps.setString(3, dtl.getStyleId());
							ps.setString(4, dtl.getColorId());
							ps.setString(5, dtl.getSizeId());
						}
					});
			final List<BillDtl> tempDtls = new ArrayList<>();
			for (int i = 0; i < update.length; i++) {
				if (update[i] < 1) {
					tempDtls.add(listBillDtls.get(i));
				}
			}
			if (tempDtls.size() > 0) {
				String chrtempsql = "insert SPKCB(sl,CKDM,SPDM,GG1DM,GG2DM) values(?,?,?,?,?)";
				this.tianTanJdbcTemplate.batchUpdate(chrtempsql,
						new BatchPreparedStatementSetter() {
							@Override
							public int getBatchSize() {
								return tempDtls.size();
							}

							@Override
							public void setValues(PreparedStatement ps, int i)
									throws SQLException {
								BillDtl dtl = tempDtls.get(i);
								ps.setDouble(1, dtl.getActQty());
								ps.setString(
										2,
										bill.getDestId().substring(7,
												bill.getDestId().length()));
								ps.setString(3, dtl.getStyleId());
								ps.setString(4, dtl.getColorId());
								ps.setString(5, dtl.getSizeId());
							}
						});
			}
		}
	}

	@Override
	public void batchInventoryBill(final Business business) {
		final Bill bill = business.getBill();
		if (CommonUtil.isNotBlank(bill)) {
/*			final List<BillDtl> dtls = bill.getDtlList();
*/			bill.setSrcBillNo(bill.getBillNo().split("_")[2]);
			String mainSql = "if exists"
					+ " (select DJBH from CKPDD where DJBH=?) "
					+ " begin "
					+ "update CKPDD set "
					+ "SL=?,JE=?,BZJE=?,RFID_LAST_UPDATE_DATETIME=? where DJBH=? "
					+ " end "
					+ "else"
					+ " begin "
					+ "insert into CKPDD(DJBH,RQ,YDJH,DM1,DM2,DM4,QDDM,QYDM,YGDM,"
					+ "SL,JE,BZJE,LL,BYZD12,RFID_LAST_UPDATE_DATETIME,BYZD1,ZDR,RQ_4) "
					+ " values(?,?,?,?,?,?,?,?,?,?,?,?,1,?,?,3,?,?)"
					+ " end";
			final List<BillDtl> dtls=TiantanUtil.filterNotScan(bill);
			int i = this.tianTanJdbcTemplate.update(mainSql,
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps)
								throws SQLException {

							ps.setString(1,"R3"+bill.getSrcBillNo());
							ps.setDouble(2, bill.getActQty().longValue());
							ps.setDouble(3, bill.getTotPrice());
							ps.setDouble(4, bill.getTotPrePrice());
							ps.setTimestamp(5, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setString(6,"R3"+bill.getSrcBillNo());

							ps.setString(7,"R3"+bill.getSrcBillNo());

							ps.setTimestamp(8, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setString(9, "R3"+bill.getSrcBillNo());
							ps.setString(10, "000");
							ps.setString(
									11,
									bill.getOrigId().substring(7,
											bill.getOrigId().length()));
							ps.setString(12, "000");
							ps.setString(13, "000");
							ps.setString(14, "000");
							ps.setString(15, "000");
							ps.setDouble(16, bill.getActQty().longValue());
							ps.setDouble(17, bill.getTotPrice());
							ps.setDouble(18, bill.getTotPrePrice());
							ps.setDouble(19, 1);
							ps.setTimestamp(20, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));
							ps.setString(21, business.getDeviceId());
							ps.setTimestamp(22, Timestamp.valueOf(CommonUtil
									.getDateString(new Date(),
											"yyyy-MM-dd 00:00:00")));

						}
					});
			String detailSql = "if exists(select DJBH from CKPDDMX where DJBH=? and SPDM=? and GG1DM=? and GG2DM=?)"
					+ " begin"
					+ " update CKPDDMX set SL=?,JE=?,BZJE=? where DJBH=? and SPDM=? and GG1DM=? and GG2DM=? "
					+ " end"
					+ " else"
					+ " begin"
					+ " insert into CKPDDMX(DJBH,MXBH,SPDM,GG1DM,GG2DM,SL,"//6
					+ "CKJ,ZK,DJ,JE,BZJE,HH,BYZD12) "
					+ " select ?,?,?,?,?,?"
					+ ",s.bzsj,1,s.bzsj,s.bzsj*?,s.bzsj*?,1,1 "
					+ " from shangpin s where s.spdm=? "
					+ " end ";
			int[] dtlc = this.tianTanJdbcTemplate.batchUpdate(detailSql,
					new BatchPreparedStatementSetter() {
						@Override
						public int getBatchSize() {
							return dtls.size();
						}

						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							BillDtl dtl = dtls.get(i);
							ps.setString(1, "R3"+bill.getSrcBillNo());// DJBH 单号
							ps.setString(2, dtl.getStyleId());
							ps.setString(3, dtl.getColorId());
							ps.setString(4, dtl.getSizeId());
							ps.setDouble(5, dtl.getActQty().longValue());
							ps.setDouble(6, dtl.getPrice());
							ps.setDouble(7, dtl.getPrePrice());
							ps.setString(8, "R3"+bill.getSrcBillNo());// DJBH 单号
							ps.setString(9, dtl.getStyleId());
							ps.setString(10, dtl.getColorId());
							ps.setString(11, dtl.getSizeId());
							ps.setString(12, "R3"+bill.getSrcBillNo());// DJBH 单号
							ps.setDouble(13, i);
							ps.setString(14, dtl.getStyleId());
							ps.setString(15, dtl.getColorId());
							ps.setString(16, dtl.getSizeId());
							ps.setDouble(17, dtl.getActQty().longValue());
							ps.setDouble(18, dtl.getActQty().longValue());
							ps.setDouble(19, dtl.getActQty().longValue());
							ps.setString(20, dtl.getStyleId());
						}
					});
			System.out.println("");
		}

	}

}
