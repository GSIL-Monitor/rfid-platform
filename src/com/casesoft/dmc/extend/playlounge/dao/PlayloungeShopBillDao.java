package com.casesoft.dmc.extend.playlounge.dao;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.extend.playlounge.dao.basic.IPlayloungeShopBillDao;
import com.casesoft.dmc.extend.playlounge.dao.basic.PlayloungeBasicDao;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.sys.Unit;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlayloungeShopBillDao extends PlayloungeBasicDao implements
		IPlayloungeShopBillDao {

	@Override
	public List<Bill> findShopTransferInBills(String storageId,
			String beginDate, String endDate, String ownerId, int token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BillDtl> findShopTransferInBillDtls(String billId, int token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Bill> findShopTransferOutBills(String storageId,
			String beginDate, String endDate, final String ownerId,
			final int token) {
		List<Bill> listBill = this.playloungeJdbcTemplate
				.query("select item,billDate,Outcode,Incode,chnum,num,mem from dj_zhtzda where "
						+ "flag=1 and status=0 and outcode=? and  billDate between ? and ?",
						new Object[] { storageId, beginDate, endDate },
						new RowMapper<Bill>() {
							@Override
							public Bill mapRow(ResultSet rs, int index)
									throws SQLException {
								Bill bill = new Bill();
								bill.setType(token);
								bill.setId(String.valueOf(new Date().getTime())
										+ "_" + rs.getString("item"));
								bill.setBillNo(rs.getString("item"));// 用客户的Id作为本地的billNo
								bill.setBillDate(rs.getDate("billDate"));
								bill.setOwnerId(ownerId);

								bill.setBillType("门店调拨出库");
								bill.setOrigId(rs.getString("Outcode"));// //
																		// 发货方ID
								Unit unit1 = CacheManager.getUnitByCode(bill
										.getOrigId());
								if (CommonUtil.isNotBlank(unit1)) {
									bill.setOrigName(unit1.getName());
								}
								bill.setDestId(rs.getString("Incode"));// 收货方
								Unit unit2 = CacheManager.getUnitByCode(bill
										.getDestId());
								if (CommonUtil.isNotBlank(unit2)) {
									bill.setDestName(unit2.getName());
								}
								bill.setTotQty(rs.getLong("num"));
								bill.setInitQty(rs.getLong("chnum"));
								bill.setActQty(rs.getLong("chnum"));
								bill.setRemark(rs.getString("mem"));

								return bill;
							}
						});

		return listBill;
	}

	@Override
	public List<BillDtl> findShopTransferOutBillDtls(final String billId,
			final int token) {
		List<BillDtl> listBillDtls = null;
		String[] src = billId.split("_");
		listBillDtls = this.playloungeJdbcTemplate
				.query("select item,clthno,color,size,num,chnum from dj_zhtzdb where item=?",
						new Object[] { src[1] }, new RowMapper<BillDtl>() {
							@Override
							public BillDtl mapRow(ResultSet rs, int index)
									throws SQLException {
								BillDtl billDtl = new BillDtl();
								billDtl.setType(token);
								billDtl.setBillNo(rs.getString("item"));
								billDtl.setBillId(billId);
								billDtl.setStyleId(rs.getString("clthno"));
								billDtl.setColorId(rs.getString("color"));
								billDtl.setSizeId(rs.getString("size"));
								billDtl.setSku(billDtl.getStyleId()
										+ billDtl.getColorId()
										+ billDtl.getSizeId());
								billDtl.setId(billDtl.getSku());
								billDtl.setQty(rs.getLong("num"));
								billDtl.setInitQty(rs.getLong("chnum"));
								billDtl.setActQty(rs.getLong("chnum"));

								return billDtl;
							}
						});
		return listBillDtls;
	}

	@Override
	public List<Bill> findShopReturnOutBills(String storageId,
			String beginDate, String endDate, final String ownerId,
			final int token) {
		List<Bill> listBill = this.playloungeJdbcTemplate
				.query("select item,billDate,Outcode,Incode,num,chnum,mem from dj_zgthtzda where "
						+ "flag=1 and status=0 and outcode=? and  billDate between ? and ?",
						new Object[] { storageId, beginDate, endDate },
						new RowMapper<Bill>() {
							@Override
							public Bill mapRow(ResultSet rs, int index)
									throws SQLException {
								Bill bill = new Bill();
								bill.setType(token);
								bill.setId(String.valueOf(new Date().getTime())
										+ "_" + rs.getString("item"));
								bill.setBillNo(rs.getString("item"));// 用客户的Id作为本地的billNo
								bill.setBillDate(rs.getDate("billDate"));
								bill.setOwnerId(ownerId);

								bill.setBillType("门店退货出库");
								bill.setOrigId(rs.getString("Outcode"));// //
																		// 发货方ID
								Unit unit1 = CacheManager.getUnitByCode(bill
										.getOrigId());
								if (CommonUtil.isNotBlank(unit1)) {
									bill.setOrigName(unit1.getName());
								}
								bill.setDestId(rs.getString("Incode"));// 收货方
								Unit unit2 = CacheManager.getUnitByCode(bill
										.getDestId());
								if (CommonUtil.isNotBlank(unit2)) {
									bill.setDestName(unit2.getName());
								}
								bill.setTotQty(rs.getLong("num"));
								bill.setInitQty(rs.getLong("chnum"));
								bill.setActQty(rs.getLong("chnum"));
								bill.setRemark(rs.getString("mem"));
								return bill;
							}
						});

		return listBill;
	}

	@Override
	public List<BillDtl> findShopReturnOutBillDtls(final String billId,
			final int token) {
		List<BillDtl> listBillDtls = null;
		String[] src = billId.split("_");
		listBillDtls = this.playloungeJdbcTemplate
				.query("select item,clthno,color,size,num,chnum from dj_zgthtzdb where item=?",
						new Object[] { src[1]}, new RowMapper<BillDtl>() {
							@Override
							public BillDtl mapRow(ResultSet rs, int index)
									throws SQLException {
								BillDtl billDtl = new BillDtl();
								billDtl.setType(token);
								billDtl.setBillNo(rs.getString("item"));
								billDtl.setBillId(billId);
								billDtl.setStyleId(rs.getString("clthno"));
								billDtl.setColorId(rs.getString("color"));
								billDtl.setSizeId(rs.getString("size"));
								billDtl.setSku(billDtl.getStyleId()
										+ billDtl.getColorId()
										+ billDtl.getSizeId());
								billDtl.setId(billDtl.getSku());
								billDtl.setQty(rs.getLong("num"));
								billDtl.setInitQty(rs.getLong("chnum"));
								billDtl.setActQty(rs.getLong("chnum"));
								return billDtl;
							}
						});
		return listBillDtls;
	}

	@Override
	public List<Bill> findShopInBills(String storageId, String beginDate,
			String endDate, String ownerId, int token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BillDtl> findShopInBillDtls(String billId, int token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Bill> findShopInventoryBills(String storageId,
											 String beginDate, String endDate,
											 String billDate, String conditions,
											 String ownerId, int token) {
		Date date = new Date();
		String billId = String.valueOf(token) + "_" + ownerId + "_"
				+ CommonUtil.getDateString(date, "yyMMddHHmmss");
		List<BillDtl> dtls = findShopInventoryBillDtls(billId,billDate,conditions, token);
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
			if(CommonUtil.isBlank(billDate)){
				bill.setBillDate(date);
			}else{
				try {
					bill.setBillDate(CommonUtil.converStrToDate(billDate,"yyyy-MM-dd"));
				} catch (ParseException e) {
					e.printStackTrace();
					bill.setBillDate(date);
				}
			}
			bill.setDtlList(dtls);
			listBill.add(bill);
		}
		return listBill;
	}

	@Override
	public List<BillDtl> findShopInventoryBillDtls(final String billId, String billDate,
												   String conditions,final int token) {
		List<BillDtl> listBillDtls = null;
		String[] src = billId.split("_");
		StringBuffer sqlCondition=new StringBuffer();
		try{
			if(CommonUtil.isNotBlank(conditions)){
				sqlCondition=DaoUtil.convertPropertis(JSON.parseObject(conditions));
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		if(CommonUtil.isBlank(billDate)){
			billDate=CommonUtil.getDateString(new Date(),"yyyy-MM-dd");
		}
		StringBuffer sql=new StringBuffer("begin declare @guid varchar(40) ; select d.whcode,d.clthno,d.color,d.size,d.num ,b.barcode")
				.append(" from  fn_Get_AllStoreInfo(@guid,'ADM',?) d  ,jbClothBarcode b,jbCloth s ")
				.append(" where  d.whcode=? and s.clthno=d.clthno and d.clthno=b.clthno and d.size=b.size and d.color=b.color and  d.num>0  ")
				.append(sqlCondition)
				.append(" ;end");
/*
        select clthno,color,size,num from store where Whcode=? and num!=0
*/
		listBillDtls = this.playloungeJdbcTemplate
				.query(sql.toString(),
						new Object[]{billDate,src[1]}, new RowMapper<BillDtl>() {
							@Override
							public BillDtl mapRow(ResultSet rs, int index)
									throws SQLException {
								BillDtl billDtl = new BillDtl();
								billDtl.setType(token);
								billDtl.setBillNo(billId);
								billDtl.setBillId(billId);
								billDtl.setStyleId(rs.getString("clthno"));
								billDtl.setColorId(rs.getString("color"));
								billDtl.setSizeId(rs.getString("size"));
								billDtl.setSku(rs.getString("barcode"));
								billDtl.setId(billId + "_" + billDtl.getSku());
								billDtl.setQty(rs.getLong("num"));
								return billDtl;
							}
						});
		return listBillDtls;
	}

}
