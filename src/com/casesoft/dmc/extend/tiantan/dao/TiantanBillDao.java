package com.casesoft.dmc.extend.tiantan.dao;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.extend.tiantan.dao.basic.ConstantType;
import com.casesoft.dmc.extend.tiantan.dao.basic.ITiantanBillDao;
import com.casesoft.dmc.extend.tiantan.dao.basic.TiantanBasicDao;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.sys.Unit;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Repository
public class TiantanBillDao extends TiantanBasicDao implements ITiantanBillDao {

	@Override
	public List<Bill> findInBills(String storageId, String beginDate,
			String endDate, final String ownerId, final int token) {
		List<Bill> bills=new ArrayList<>();
		/**
		 * ys=1:已审核 jz=1:已记账 js=1:已执行 sp=1:已中止 商品进货入库通知单
		 * */
		List<Bill> listBill = this.tianTanJdbcTemplate
				.query("select DJBH,rq,dm1,dm2,sl,ygdm from JSEND where jz=1 and js=0 and sp=0 and  rq between ? and ?",
						new Object[] { beginDate, endDate },
						new RowMapper<Bill>() {
							@Override
							public Bill mapRow(ResultSet rs, int index)
									throws SQLException {
								Bill bill = new Bill();
								bill.setType(token);
								bill.setId(ConstantType.WH_IN_VENDOR + "-"
										+ Constant.Token.Storage_Inbound + "-"
										+ rs.getString("DJBH"));
								bill.setBillNo(rs.getString("DJBH"));// 用客户的Id作为本地的billNo
								bill.setBillDate(rs.getDate("rq"));
								bill.setOwnerId(ownerId);

								bill.setBillType("仓库入库");
								bill.setOrigId(Constant.UnitCodePrefix.Vender+rs.getString("dm1"));// // 发货方ID
								Unit unit1 = CacheManager
										.getUnitByCode( bill.getOrigId());
								if (CommonUtil.isNotBlank(unit1)) {
									bill.setOrigName(unit1.getName());
								}
								bill.setDestId(Constant.UnitCodePrefix.Warehouse+rs.getString("dm2"));// 收货方
								Unit unit2 = CacheManager
										.getUnitByCode(
												bill.getDestId());
								if (CommonUtil.isNotBlank(unit2)) {
									bill.setDestName(unit2.getName());
								}
								bill.setTotQty((long) rs.getDouble("sl"));
								bill.setOprId(rs.getString("ygdm"));
								return bill;
							}
						});
		
		List<Bill> listFactoryBills = this.tianTanJdbcTemplate
				.query("select DJBH,RQ,GCDM,SL,ygdm from SCZZD where rq between ? and ?",
						new Object[] { beginDate, endDate },
						new RowMapper<Bill>() {
							@Override
							public Bill mapRow(ResultSet rs, int index)
									throws SQLException {
								Bill bill = new Bill();
								bill.setType(token);
								bill.setId(ConstantType.WH_IN_FACTORY + "-"
										+ Constant.Token.Storage_Inbound + "-"
										+ rs.getString("DJBH"));
								bill.setBillNo(rs.getString("DJBH"));// 用客户的Id作为本地的billNo
								bill.setBillDate(rs.getDate("rq"));
								bill.setOwnerId(ownerId);

								bill.setBillType("仓库入库(工厂)");
								bill.setOrigId(Constant.UnitCodePrefix.Factory+rs.getString("GCDM"));// // 发货方ID
								Unit unit1 = CacheManager
										.getUnitByCode(bill.getOrigId());
								if (CommonUtil.isNotBlank(unit1)) {
									bill.setOrigName(unit1.getName());
								}
								/*bill.setDestId(rs.getString("dm2"));// 收货方
								Unit unit2 = CacheManager
										.getUnitByCode(Constant.UnitCodePrefix.Warehouse
												+ bill.getDestId());
								if (CommonUtil.isNotBlank(unit2)) {
									bill.setDestName(unit2.getName());
								}*/
								bill.setTotQty((long) rs.getDouble("sl"));
								bill.setOprId(rs.getString("ygdm"));
								return bill;
							}
						});
		if(CommonUtil.isNotBlank(listBill)){
			bills.addAll(listBill);
		}
		if(CommonUtil.isNotBlank(listFactoryBills)){
			bills.addAll(listFactoryBills);
		}
		return bills;
	}

	@Override
	public List<BillDtl> findInBillDtls(final String billId, final int token) {
		List<BillDtl> listBillDtls = null;
		String[] src = billId.split("-");
		/*
		 * 商品进货入库通知单
		 */
		switch(Integer.parseInt(src[0])){
		case ConstantType.WH_IN_FACTORY:
			listBillDtls = this.tianTanJdbcTemplate.query(
					"select DJBH,spdm,GG1DM,GG2DM,sl from SCZZDMX where DJBH=?",
					new Object[] {src[2] }, new RowMapper<BillDtl>() {
						@Override
						public BillDtl mapRow(ResultSet rs, int index)
								throws SQLException {
							BillDtl billDtl = new BillDtl();
							billDtl.setType(token);
							billDtl.setBillNo(rs.getString("DJBH"));
							billDtl.setBillId(billId);
							billDtl.setStyleId(rs.getString("spdm"));
							billDtl.setColorId(rs.getString("GG1DM"));
							billDtl.setSizeId(rs.getString("GG2DM"));
							billDtl.setSku(billDtl.getStyleId()
									+ billDtl.getColorId() + billDtl.getSizeId());
							billDtl.setId(ConstantType.WH_IN_VENDOR + "-" + rs.getString("DJBH")
									+ "-" + billDtl.getSku());
							billDtl.setQty((long) rs.getDouble("sl"));
							return billDtl;
						}
					});
			break;
		case ConstantType.WH_IN_VENDOR:
 			 
			listBillDtls = this.tianTanJdbcTemplate.query(
					"select DJBH,spdm,GG1DM,GG2DM,sl,dj from JSENDMX where DJBH=?",
					new Object[] {src[2] }, new RowMapper<BillDtl>() {
						@Override
						public BillDtl mapRow(ResultSet rs, int index)
								throws SQLException {
							BillDtl billDtl = new BillDtl();
							billDtl.setType(token);
							billDtl.setBillNo(rs.getString("DJBH"));
							billDtl.setBillId(billId);
							billDtl.setStyleId(rs.getString("spdm"));
							billDtl.setColorId(rs.getString("GG1DM"));
							billDtl.setSizeId(rs.getString("GG2DM"));
							billDtl.setPrice(rs.getDouble("dj"));
							billDtl.setSku(billDtl.getStyleId()
									+ billDtl.getColorId() + billDtl.getSizeId());
							billDtl.setId(ConstantType.WH_IN_VENDOR + "-" + rs.getString("DJBH")
									+ "-" + billDtl.getSku());
							billDtl.setQty((long) rs.getDouble("sl"));
							return billDtl;
						}
					});
			break;
		}
	
		
		return listBillDtls;
	}

	@Override
	public List<Bill> findOutBills(String storageId, String beginDate,
			String endDate, final String ownerId, final int token) {
		List<Bill> listBills = new ArrayList<Bill>();
		/*
		 * 批发出库通知单
		 */
		List<Bill> listVenderBill = this.tianTanJdbcTemplate
				.query("select f.DJBH,f.rq,f.dm1,f.dm2,f.sl,f.ygdm, k.KHMC from FSEND f,kehu k "
						+ "where jz=1 and js=0 and sp=0 and k.khdm=f.dm1 and  rq between ? and ?",
						new Object[] { beginDate, endDate },
						new RowMapper<Bill>() {
							@Override
							public Bill mapRow(ResultSet rs, int index)
									throws SQLException {
								Bill bill = new Bill();
								bill.setType(token);
								bill.setId(ConstantType.WH_RENTURN_VENDER + "-"
										+ Constant.Token.Storage_Outbound + "-"
										+ rs.getString("DJBH"));
								bill.setBillNo(rs.getString("DJBH"));// 用客户的Id作为本地的billNo
								bill.setBillDate(rs.getDate("rq"));
								bill.setOwnerId(ownerId);

								bill.setBillType("批发发货出库");
								bill.setOrigId(Constant.UnitCodePrefix.Warehouse+rs.getString("dm2"));// 发货方ID
								Unit unit = CacheManager
										.getUnitByCode( bill.getOrigId());
								if (CommonUtil.isNotBlank(unit)) {
									bill.setOrigName(unit.getName());
								}
								bill.setDestId(Constant.UnitCodePrefix.Agent+rs.getString("dm1"));// 收货方ID
								bill.setDestName(rs.getString("KHMC"));
								bill.setTotQty((long) rs.getDouble("sl"));
								bill.setOprId(rs.getString("ygdm"));
								return bill;
							}
						});

		/*
		 * 商店配货出库通知单
		 */
		List<Bill> listShopBill = this.tianTanJdbcTemplate
				.query("select f.DJBH,f.rq,f.dm1,f.dm2,f.sl,f.ygdm, k.ckmc from PSEND f,CangKu k "
						+ "where jz=1 and js=0 and sp=0 and k.ckdm=f.dm1 and  rq between ? and ?",
						new Object[] { beginDate, endDate },
						new RowMapper<Bill>() {
							@Override
							public Bill mapRow(ResultSet rs, int index)
									throws SQLException {
								Bill bill = new Bill();
								bill.setType(token);
								bill.setId(ConstantType.WH_RENTURN_SHOP + "-"
										+ Constant.Token.Storage_Outbound + "-"
										+ rs.getString("DJBH"));
								bill.setBillNo(rs.getString("DJBH"));// 用客户的Id作为本地的billNo
								bill.setBillDate(rs.getDate("rq"));
								bill.setOwnerId(ownerId);
								bill.setBillType("商店配货发出库");
								bill.setOrigId(Constant.UnitCodePrefix.Warehouse
										+rs.getString("dm2"));// 发货方ID
								Unit unit = CacheManager
										.getUnitByCode( bill.getOrigId());
								if (CommonUtil.isNotBlank(unit)) {
									bill.setOrigName(unit.getName());
								}
								bill.setDestId(Constant.UnitCodePrefix.Shop
										+rs.getString("dm1"));// 收货方ID
								bill.setDestName(rs.getString("ckmc"));
								bill.setTotQty((long) rs.getDouble("sl"));
								bill.setOprId(rs.getString("ygdm"));
								return bill;
							}
						});
		if (CommonUtil.isNotBlank(listShopBill)) {
			listBills.addAll(listShopBill);
		}
		if (CommonUtil.isNotBlank(listVenderBill)) {
			listBills.addAll(listVenderBill);
		}
		return listBills;
	}

	@Override
	public List<BillDtl> findOutBillDtls(final String billId, final int token) {
		List<BillDtl> listBillDtls = null;
		String[] src = billId.split("-");

		switch (Integer.parseInt(src[0])) {
		case ConstantType.WH_RENTURN_VENDER:
			/*
			 * 批发出库通知单详单
			 */
			listBillDtls = this.tianTanJdbcTemplate
					.query("select DJBH,spdm,GG1DM,GG2DM,sl,dj from FSENDMX where DJBH=?",
							new Object[] { src[2] }, new RowMapper<BillDtl>() {
								@Override
								public BillDtl mapRow(ResultSet rs, int index)
										throws SQLException {
									BillDtl billDtl = new BillDtl();
									billDtl.setType(token);
									billDtl.setBillNo(rs.getString("DJBH"));
									billDtl.setBillId(billId);
									billDtl.setPrice(rs.getDouble("dj"));

									billDtl.setStyleId(rs.getString("spdm"));
									billDtl.setColorId(rs.getString("GG1DM"));
									billDtl.setSizeId(rs.getString("GG2DM"));
									billDtl.setSku(billDtl.getStyleId()
											+ billDtl.getColorId()
											+ billDtl.getSizeId());
									billDtl.setId(ConstantType.WH_RENTURN_VENDER + "-"
											+ rs.getString("DJBH") + "-"
											+ billDtl.getSku());
									billDtl.setQty((long) rs.getDouble("sl"));
									return billDtl;
								}
							});
			break;
		case ConstantType.WH_RENTURN_SHOP:
			/*
			 * 配货发货出库通知单详单
			 */
			listBillDtls = this.tianTanJdbcTemplate
					.query("select DJBH,spdm,GG1DM,GG2DM,sl,dj from PSENDMX where DJBH=?",
							new Object[] { src[2] }, new RowMapper<BillDtl>() {
								@Override
								public BillDtl mapRow(ResultSet rs, int index)
										throws SQLException {
									BillDtl billDtl = new BillDtl();
									billDtl.setType(token);
									billDtl.setBillNo(rs.getString("DJBH"));
									billDtl.setBillId(billId);
									billDtl.setStyleId(rs.getString("spdm"));
									billDtl.setColorId(rs.getString("GG1DM"));
									billDtl.setSizeId(rs.getString("GG2DM"));
									billDtl.setPrice(rs.getDouble("dj"));
									billDtl.setSku(billDtl.getStyleId()
											+ billDtl.getColorId()
											+ billDtl.getSizeId());
									billDtl.setId(ConstantType.WH_RENTURN_SHOP + "-"
											+ rs.getString("DJBH") + "-"
											+ billDtl.getSku());
									billDtl.setQty((long) rs.getDouble("sl"));
									return billDtl;
								}
							});
			break;
		}

		return listBillDtls;
	}

	@Override
	public List<Bill> findReturnOutBills(String storageId, String beginDate,
			String endDate, final String ownerId, final int token) {
		List<Bill> listBill = this.tianTanJdbcTemplate
				.query("select DJBH,rq,dm1,dm2,sl,ygdm from JTSND where jz=1 and js=0 and sp=0 and  rq between ? and ?",
						new Object[] { beginDate, endDate },
						new RowMapper<Bill>() {
							@Override
							public Bill mapRow(ResultSet rs, int index)
									throws SQLException {
								Bill bill = new Bill();
								bill.setType(token);
								bill.setId(ConstantType.WH_IN_VENDOR
										+ "-"
										+ Constant.Token.Storage_Refund_Outbound
										+ "-" + rs.getString("DJBH"));
								bill.setBillNo(rs.getString("DJBH"));// 用客户的Id作为本地的billNo
								bill.setBillDate(rs.getDate("rq"));
								bill.setOwnerId(ownerId);

								bill.setBillType("仓库退货出库");
								bill.setDestId(Constant.UnitCodePrefix.Vender
										+rs.getString("dm1"));// // 收货方ID
								Unit unit2 = CacheManager
										.getUnitByCode( bill.getDestId());
								if (CommonUtil.isNotBlank(unit2)) {
									bill.setDestName(unit2.getName());
								}
								bill.setOrigId(Constant.UnitCodePrefix.Warehouse
										+ rs.getString("dm2"));// 发货方
								Unit unit = CacheManager
										.getUnitByCode(bill.getOrigId());
								if (CommonUtil.isNotBlank(unit)) {
									bill.setOrigName(unit.getName());
								}
								bill.setTotQty((long) rs.getDouble("sl"));
								bill.setOprId(rs.getString("ygdm"));
								return bill;
							}
						});
		return listBill;
	}

	@Override
	public List<BillDtl> findReturnOutBillDtls(final String billId,
			final int token) {
		List<BillDtl> listBillDtls = null;
		String[] src = billId.split("-");
		/*
		 * 商品退货出库通知单详单
		 */
		listBillDtls = this.tianTanJdbcTemplate.query(
				"select DJBH,spdm,GG1DM,GG2DM,sl,dj from JTSNDMX where DJBH=?",
				new Object[] { src[2] }, new RowMapper<BillDtl>() {
					@Override
					public BillDtl mapRow(ResultSet rs, int index)
							throws SQLException {
						BillDtl billDtl = new BillDtl();
						billDtl.setType(token);
						billDtl.setBillNo(rs.getString("DJBH"));
						billDtl.setBillId(billId);
						billDtl.setStyleId(rs.getString("spdm"));
						billDtl.setColorId(rs.getString("GG1DM"));
						billDtl.setSizeId(rs.getString("GG2DM"));
						billDtl.setPrice(rs.getDouble("dj"));

						billDtl.setSku(billDtl.getStyleId()
								+ billDtl.getColorId() + billDtl.getSizeId());
						billDtl.setId(ConstantType.WH_IN_VENDOR + "-" + rs.getString("DJBH")
								+ "-" + billDtl.getSku());
						billDtl.setQty((long) rs.getDouble("sl"));
						return billDtl;
					}
				});
		return listBillDtls;
	}

	@Override
	public List<Bill> findReturnInBills(String storageId, String beginDate,
			String endDate, final String ownerId, final int token) {
		List<Bill> listBills = new ArrayList<Bill>();
		/*
		 * 批发退货入库通知单
		 */
		List<Bill> listVenderBill = this.tianTanJdbcTemplate
				.query("select f.DJBH,f.rq,f.dm1,f.dm2,f.sl,f.ygdm, k.KHMC from FTSND f,kehu k "
						+ "where jz=1 and js=0 and sp=0 and k.khdm=f.dm1 and  rq between ? and ?",
						new Object[] { beginDate, endDate },
						new RowMapper<Bill>() {
							@Override
							public Bill mapRow(ResultSet rs, int index)
									throws SQLException {
								Bill bill = new Bill();
								bill.setType(token);
								bill.setId(ConstantType.WH_RENTURN_VENDER + "-"
										+ token
										+ "-" + rs.getString("DJBH"));
								bill.setBillNo(rs.getString("DJBH"));// 用客户的Id作为本地的billNo
								bill.setBillDate(rs.getDate("rq"));
								bill.setOwnerId(ownerId);

								bill.setBillType("批发退货入库");
								bill.setOrigId(Constant.UnitCodePrefix.Agent
										+rs.getString("dm1"));// 发货方ID
								bill.setOrigName(rs.getString("KHMC"));
								bill.setDestId(Constant.UnitCodePrefix.Warehouse
										+rs.getString("dm2"));// 收货方ID
								Unit unit2 = CacheManager
										.getUnitByCode( bill.getDestId());
								if (CommonUtil.isNotBlank(unit2)) {
									bill.setDestName(unit2.getName());
								}
								bill.setTotQty((long) rs.getDouble("sl"));
								bill.setOprId(rs.getString("ygdm"));
								return bill;
							}
						});

		/*
		 * 商店退货入库通知单
		 */
		
		List<Bill> listShopBill = this.tianTanJdbcTemplate
				.query("select y.DJBH,y.rq,y.dm1,y.dm2,sl,y.ygdm,y.BYZD3,c.CKMC CKMC1,d.CKMC CKMC2 from SDTHD y, CangKu c,CangKu d  where "
						+ "c.CKDM=y.DM1 and d.CKDM=y.DM2  and "
						+ " y.ys=0 and y.sh=1 and "
						+ " y.rq between ? and ?", new Object[] {
						beginDate, endDate }, new RowMapper<Bill>() {
					@Override
					public Bill mapRow(ResultSet rs, int index)
							throws SQLException {
						Bill bill = new Bill();
						bill.setType(token);
						bill.setId(ConstantType.WH_RENTURN_SHOP + "-" + token + "-"
								+ rs.getString("DJBH"));
						bill.setSrcBillNo(rs.getString("BYZD3"));
						bill.setBillNo(rs.getString("DJBH"));// 用客户的Id作为本地的billNo
						bill.setBillDate(rs.getDate("rq"));
						bill.setOwnerId(ownerId);
						bill.setBillType("退货入库！");
						bill.setOrigId(Constant.UnitCodePrefix.Shop+rs.getString("dm1"));// 发货货方
						bill.setDestId(Constant.UnitCodePrefix.Warehouse+rs.getString("dm2"));// // 收货方ID	
						bill.setOrigName(rs.getString("CKMC1"));
						bill.setDestName(rs.getString("CKMC2"));
						bill.setTotQty((long) rs.getDouble("sl"));
						bill.setOprId(rs.getString("ygdm"));
						return bill;
					}
				});
		
		//////////
		/*List<Bill> listShopBill = this.tianTanJdbcTemplate
				.query("select f.DJBH,f.rq,f.dm1,f.dm2,f.sl,f.ygdm, k.ckmc from PTSND f,CangKu k "
						+ "where jz=1 and js=0 and sp=0 and k.ckdm=f.dm1 and  rq between ? and ?",
						new Object[] { beginDate, endDate },
						new RowMapper<Bill>() {
							@Override
							public Bill mapRow(ResultSet rs, int index)
									throws SQLException {
								Bill bill = new Bill();
								bill.setType(token);
								bill.setId(ConstantType.WH_RENTURN_SHOP + "-"
										+ Constant.Token.Storage_Refund_Inbound
										+ "-" + rs.getString("DJBH"));
								bill.setBillNo(rs.getString("DJBH"));// 用客户的Id作为本地的billNo
								bill.setBillDate(rs.getDate("rq"));
								bill.setOwnerId(ownerId);

								bill.setBillType("商店退货入库");
								bill.setOrigId(Constant.UnitCodePrefix.Shop
										+rs.getString("dm1"));// 发货方ID
								bill.setOrigName(rs.getString("ckmc"));
								bill.setDestId(Constant.UnitCodePrefix.Warehouse
										+rs.getString("dm2"));// 收货方ID
								Unit unit2 = CacheManager
										.getUnitByCode( bill.getDestId());
								if (CommonUtil.isNotBlank(unit2)) {
									bill.setDestName(unit2.getName());
								}
								bill.setTotQty((long) rs.getDouble("sl"));
								bill.setPayerId(rs.getString("ygdm"));
								return bill;
							}
						});*/
		if (CommonUtil.isNotBlank(listShopBill)) {
			listBills.addAll(listShopBill);
		}
		if (CommonUtil.isNotBlank(listVenderBill)) {
			listBills.addAll(listVenderBill);
		}
		return listBills;
	}

	@Override
	public List<BillDtl> findReturnInBillDtls(final String billId,
			final int token) {
		List<BillDtl> listBillDtls = null;
		String[] src = billId.split("-");

		switch (Integer.parseInt(src[0])) {
		case ConstantType.WH_RENTURN_VENDER:
			/*
			 * 批发退货入库通知单
			 */
			listBillDtls = this.tianTanJdbcTemplate
					.query("select DJBH,spdm,GG1DM,GG2DM,sl,dj from FTSNDMX where DJBH=?",
							new Object[] { src[2] }, new RowMapper<BillDtl>() {
								@Override
								public BillDtl mapRow(ResultSet rs, int index)
										throws SQLException {
									BillDtl billDtl = new BillDtl();
									billDtl.setType(token);
									billDtl.setBillNo(rs.getString("DJBH"));
									billDtl.setBillId(billId);
									billDtl.setStyleId(rs.getString("spdm"));
									billDtl.setColorId(rs.getString("GG1DM"));
									billDtl.setSizeId(rs.getString("GG2DM"));
									billDtl.setPrice(rs.getDouble("dj"));

									billDtl.setSku(billDtl.getStyleId()
											+ billDtl.getColorId()
											+ billDtl.getSizeId());
									billDtl.setId(ConstantType.WH_RENTURN_VENDER + "-"
											+ rs.getString("DJBH") + "-"
											+ billDtl.getSku());
									billDtl.setQty((long) rs.getDouble("sl"));
									return billDtl;
								}
							});
			break;
		case ConstantType.WH_RENTURN_SHOP:
			/*
			 * 配货退货入库通知单
			 */
			listBillDtls = this.tianTanJdbcTemplate
					.query("select DJBH,spdm,GG1DM,GG2DM,sl,dj from SDTHDMX where DJBH=?",
							new Object[] { src[2] }, new RowMapper<BillDtl>() {
								@Override
								public BillDtl mapRow(ResultSet rs, int index)
										throws SQLException {
									BillDtl billDtl = new BillDtl();
									billDtl.setType(token);
									billDtl.setBillNo(rs.getString("DJBH"));
									billDtl.setBillId(billId);
									billDtl.setStyleId(rs.getString("spdm"));
									billDtl.setColorId(rs.getString("GG1DM"));
									billDtl.setPrice(rs.getDouble("dj"));

									billDtl.setSizeId(rs.getString("GG2DM"));
									billDtl.setSku(billDtl.getStyleId()
											+ billDtl.getColorId()
											+ billDtl.getSizeId());
									billDtl.setId(ConstantType.WH_RENTURN_SHOP + "-"
											+ rs.getString("DJBH") + "-"
											+ billDtl.getSku());
									billDtl.setQty((long) rs.getDouble("sl"));
									return billDtl;
								}
							});
			break;
		}

		return listBillDtls;
	}

	@Override
	public List<Bill> findTransferInBills(String storageId, String beginDate,
			String endDate, final String ownerId, final int token) {
		/*
		 * ys:0未出库，1已出库 sh:0未入库，1已入库 仓库，门店调拨入库通知单
		 */
		int FPLX=0;
		if(token==Constant.Token.Shop_Transfer_Inbound){
			FPLX=1;
		}
		List<Bill> listBill = this.tianTanJdbcTemplate
				.query("select y.DJBH,y.rq,y.dm1,y.dm2,sl,y.ygdm,c.CKMC CKMC1,d.CKMC CKMC2 from SPYCD y, CangKu c,CangKu d  where "
						+ "c.CKDM=y.DM1 and d.CKDM=y.DM2  and "
						+ " y.ys=1 and y.sh=0  and FPLX=? and "
						+ " y.rq between ? and ?", new Object[] {FPLX,
						beginDate, endDate }, new RowMapper<Bill>() {
					@Override
					public Bill mapRow(ResultSet rs, int index)
							throws SQLException {
						Bill bill = new Bill();
						bill.setType(token);
						bill.setId(ConstantType.WH_IN_VENDOR + "-" + token + "-"
								+ rs.getString("DJBH"));
						bill.setBillNo(rs.getString("DJBH"));// 用客户的Id作为本地的billNo
						bill.setBillDate(rs.getDate("rq"));
						bill.setOwnerId(ownerId);
						bill.setBillType("调拨入库！");
						if(token==Constant.Token.Storage_Transfer_Inbound){
							bill.setOrigId(Constant.UnitCodePrefix.Warehouse+rs.getString("dm1"));// 发货货方
							bill.setDestId(Constant.UnitCodePrefix.Warehouse+rs.getString("dm2"));// // 收货方ID
						}else{
							bill.setOrigId(Constant.UnitCodePrefix.Shop+rs.getString("dm1"));// 发货货方
							bill.setDestId(Constant.UnitCodePrefix.Shop+rs.getString("dm2"));// // 收货方ID	
						}
						bill.setOrigName(rs.getString("CKMC1"));
						bill.setDestName(rs.getString("CKMC2"));
						bill.setTotQty((long) rs.getDouble("sl"));
						bill.setOprId(rs.getString("ygdm"));
						return bill;
					}
				});
		return listBill;
	}

	@Override
	public List<BillDtl> findTransferInBillDtls(final String billId,
			final int token) {
		List<BillDtl> listBillDtls = null;
		String[] src = billId.split("-");
		listBillDtls = this.tianTanJdbcTemplate.query(
				"select DJBH,spdm,GG1DM,GG2DM,sl,dj from SPYCDMX where DJBH=?",
				new Object[] { src[2] }, new RowMapper<BillDtl>() {
					@Override
					public BillDtl mapRow(ResultSet rs, int index)
							throws SQLException {
						BillDtl billDtl = new BillDtl();
						billDtl.setType(token);
						billDtl.setBillNo(rs.getString("DJBH"));
						billDtl.setBillId(billId);
						billDtl.setStyleId(rs.getString("spdm"));
						billDtl.setColorId(rs.getString("GG1DM"));
						billDtl.setSizeId(rs.getString("GG2DM"));
						billDtl.setPrice(rs.getDouble("dj"));

						billDtl.setSku(billDtl.getStyleId()
								+ billDtl.getColorId() + billDtl.getSizeId());
						billDtl.setId(ConstantType.WH_IN_VENDOR + "-"
								+ rs.getString("DJBH") + "-" + billDtl.getSku());
						billDtl.setQty((long) rs.getDouble("sl"));
						return billDtl;
					}
				});
		return listBillDtls;
	}

	@Override
	public List<Bill> findTransferOutBills(String storageId, String beginDate,
			String endDate, final String ownerId, final int token) {
		/**
		 * ys=1:已审核 jz=1:已记账 js=1:已执行 sp=1:已中止 商品调拨出库通知单 fplx:1店，0仓
		 * */
		String transql="select y.DJBH,y.rq,y.dm1,y.dm2,sl,y.ygdm,c.CKMC CKMC1,d.CKMC CKMC2 from YSEND y, CangKu c,CangKu d  where "
				+ "c.CKDM=y.DM1 and d.CKDM=y.DM2  and "
				+ " y.jz=1 and y.js=0 and y.sp=0 and %s"
				+ " y.FPLX=? and  y.rq between ? and ?";
		int fplx = 0;
		switch (token) {
		case Constant.Token.Storage_Transfer_Outbound:
			transql=String.format(transql, "");
			fplx = 0;
			break;
		case Constant.Token.Shop_Transfer_Outbound:
			transql=String.format(transql, " y.dm1='"+storageId+"' and ");
			fplx = 1;
			break;
		}
		/*
		 * 仓库，门店调拨出库通知单
		 */
		List<Bill> listBill = this.tianTanJdbcTemplate
				.query(transql,new Object[] {
						fplx, beginDate, endDate }, new RowMapper<Bill>() {
					@Override
					public Bill mapRow(ResultSet rs, int index)
							throws SQLException {
						Bill bill = new Bill();
						bill.setType(token);
						bill.setId(ConstantType.WH_IN_VENDOR + "-" + token + "-"
								+ rs.getString("DJBH"));
						bill.setBillNo(rs.getString("DJBH"));// 用客户的Id作为本地的billNo
						bill.setBillDate(rs.getDate("rq"));
						bill.setOwnerId(ownerId);
						bill.setBillType("调拨出库！");
						if(token==Constant.Token.Storage_Transfer_Outbound){
							bill.setOrigId(Constant.UnitCodePrefix.Warehouse+rs.getString("dm1"));// // 发货方ID
							bill.setDestId(Constant.UnitCodePrefix.Warehouse+rs.getString("dm2"));// 收货方
							
						}else{
							bill.setOrigId(Constant.UnitCodePrefix.Shop+rs.getString("dm1"));// // 发货方ID
							bill.setDestId(Constant.UnitCodePrefix.Shop+rs.getString("dm2"));// 收货方
						}
						bill.setOrigName(rs.getString("CKMC1"));
						bill.setDestName(rs.getString("CKMC2"));
						bill.setTotQty((long) rs.getDouble("sl"));
						bill.setOprId(rs.getString("ygdm"));
						return bill;
					}
				});
		return listBill;
	}

	@Override
	public List<BillDtl> findTransferOutBillDtls(final String billId,
			final int token) {
		List<BillDtl> listBillDtls = null;
		String[] src = billId.split("-");
		listBillDtls = this.tianTanJdbcTemplate.query(
				"select DJBH,spdm,GG1DM,GG2DM,sl,dj from YSENDMX where DJBH=?",
				new Object[] { src[2] }, new RowMapper<BillDtl>() {
					@Override
					public BillDtl mapRow(ResultSet rs, int index)
							throws SQLException {
						BillDtl billDtl = new BillDtl();
						billDtl.setType(token);
						billDtl.setBillNo(rs.getString("DJBH"));
						billDtl.setBillId(billId);
						billDtl.setPrice(rs.getDouble("dj"));

						billDtl.setStyleId(rs.getString("spdm"));
						billDtl.setColorId(rs.getString("GG1DM"));
						billDtl.setSizeId(rs.getString("GG2DM"));
						billDtl.setSku(billDtl.getStyleId()
								+ billDtl.getColorId() + billDtl.getSizeId());
						billDtl.setId(ConstantType.WH_RENTURN_SHOP + "-"
								+ rs.getString("DJBH") + "-" + billDtl.getSku());
						billDtl.setQty((long) rs.getDouble("sl"));
						return billDtl;
					}
				});
		return listBillDtls;
	}

	@Override
	public List<Bill> findShopReturnOutBills(String storageId,
			String beginDate, String endDate, final String ownerId, final int token) {
		/*
		 * 商店退货入库通知单
		 */
		List<Bill> listShopBill = this.tianTanJdbcTemplate
				.query("select f.DJBH,f.rq,f.dm1,f.dm2,f.sl,f.ygdm, k.ckmc from PTSND f,CangKu k "
						+ "where jz=1 and js=0 and sp=0 and k.ckdm=f.dm1 and f.dm1=? and  rq between ? and ?",
						new Object[] { storageId,beginDate, endDate },
						new RowMapper<Bill>() {
							@Override
							public Bill mapRow(ResultSet rs, int index)
									throws SQLException {
								Bill bill = new Bill();
								bill.setType(token);
								bill.setId(ConstantType.WH_RENTURN_SHOP + "-"
										+ Constant.Token.Storage_Refund_Inbound
										+ "-" + rs.getString("DJBH"));
								bill.setBillNo(rs.getString("DJBH"));// 用客户的Id作为本地的billNo
								bill.setBillDate(rs.getDate("rq"));
								bill.setOwnerId(ownerId);

								bill.setBillType("商店退货出库");
								bill.setOrigId(Constant.UnitCodePrefix.Shop
										+ rs.getString("dm1"));// 发货方ID
								bill.setOrigName(rs.getString("ckmc"));
								bill.setDestId(Constant.UnitCodePrefix.Warehouse
										+ rs.getString("dm2"));// 收货方ID
								Unit unit2 = CacheManager
										.getUnitByCode(bill.getDestId());
								if (CommonUtil.isNotBlank(unit2)) {
									bill.setDestName(unit2.getName());
								}
								bill.setTotQty((long) rs.getDouble("sl"));
								bill.setOprId(rs.getString("ygdm"));
								return bill;
							}
						});
		return listShopBill;
	}

	@Override
	public List<BillDtl> findShopReturnOutBillDtls(final String billId, final int token) {
		 
		String[] src = billId.split("-");

		List<BillDtl >listBillDtls = this.tianTanJdbcTemplate
				.query("select DJBH,spdm,GG1DM,GG2DM,sl,dj from PTSNDMX where DJBH=?",
						new Object[] { src[2] }, new RowMapper<BillDtl>() {
							@Override
							public BillDtl mapRow(ResultSet rs, int index)
									throws SQLException {
								BillDtl billDtl = new BillDtl();
								billDtl.setType(token);
								billDtl.setBillNo(rs.getString("DJBH"));
								billDtl.setBillId(billId);
								billDtl.setStyleId(rs.getString("spdm"));
								billDtl.setColorId(rs.getString("GG1DM"));
								billDtl.setPrice(rs.getDouble("dj"));

								billDtl.setSizeId(rs.getString("GG2DM"));
								billDtl.setSku(billDtl.getStyleId()
										+ billDtl.getColorId()
										+ billDtl.getSizeId());
								billDtl.setId(ConstantType.WH_RENTURN_SHOP + "-"
										+ rs.getString("DJBH") + "-"
										+ billDtl.getSku());
								billDtl.setQty((long) rs.getDouble("sl"));
								return billDtl;
							}
						});
		return listBillDtls;
	}
	@Override
	public List<Bill> findInventoryBills(String beginDate, String endDate,
			String ownerId, int token) {
		Date date = new Date();
		String billId=String.valueOf(token) + "_" + ownerId + "_"
				+ CommonUtil.getDateString(date, "yyMMddHHmmss");
		List<BillDtl> dtls = this.findInventoryBillDtls(billId, token);
		List<Bill> listBill = new ArrayList<Bill>();
		if (CommonUtil.isBlank(dtls)) {
		}else{
			Bill bill = new Bill();
			long totQty=0l;
			for(BillDtl dtl:dtls){
				totQty+=dtl.getQty();
			}
			bill.setTotQty(totQty);
 			bill.setId(billId);
			bill.setBillNo(bill.getId());
			bill.setSkuQty(Long.parseLong(String.valueOf(dtls.size())));
			bill.setOrigId(ownerId);
			bill.setType(token);
			bill.setBillDate(date);
			bill.setDtlList(dtls);
			listBill.add(bill);
		}
		return listBill;
	}

	@Override
	public List<BillDtl> findInventoryBillDtls(final String billId,final int token) {
		List<BillDtl> listBillDtls = null;
		String[] src = billId.split("_");
		listBillDtls = this.tianTanJdbcTemplate
				.query("select   SL ,SPDM, GG1DM,GG2DM  from SPKCB where ckdM=?",
						new Object[] { src[1].substring(1, src[1].length()) }, new RowMapper<BillDtl>() {
							@Override
							public BillDtl mapRow(ResultSet rs, int index)
									throws SQLException {
								BillDtl billDtl = new BillDtl();
								billDtl.setType(token);
								billDtl.setBillNo(billId);
								billDtl.setBillId(billId);
								billDtl.setStyleId(rs.getString("SPDM"));
								billDtl.setColorId(rs.getString("GG1DM"));
								billDtl.setSizeId(rs.getString("GG2DM"));
								billDtl.setSku(billDtl.getStyleId()
										+ billDtl.getColorId()
										+ billDtl.getSizeId());
								billDtl.setId(billDtl.getBillId()+"_"+billDtl.getSku());
								billDtl.setQty(rs.getLong("SL"));
								return billDtl;
							}
						});
		return listBillDtls;
	}
}
