package com.casesoft.dmc.extend.playlounge.dao;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;

import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.shop.SaleBill;
import com.casesoft.dmc.model.shop.SaleBillDtl;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.BusinessDtl;
import com.casesoft.dmc.model.task.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlayloungeUtil {
	/**
	 * @param bill
	 * @return 过滤已扫描的详单
	 * */
	public static List<BillDtl> filterNotScan(Bill bill) {
		List<BillDtl> billDtls = bill.getDtlList();
		List<BillDtl> newBillDtls = new ArrayList<>();
		double totPrice = 0;
		double totPuPrice = 0;
		double totPreCase = 0;
		if (CommonUtil.isNotBlank(billDtls)) {
			for (BillDtl dtl : billDtls) {
				if (CommonUtil.isNotBlank(dtl.getScanQty())&&dtl.getScanQty().longValue()!=0L) {
					Style style = CacheManager.getStyleById(dtl.getStyleId());
					if (CommonUtil.isNotBlank(style)) {
						if(CommonUtil.isNotBlank(style.getPrice())){
							totPrice += dtl.getScanQty().longValue() * style.getPrice();
						}
						if(CommonUtil.isNotBlank(style.getPreCast())){
							totPreCase += dtl.getScanQty().longValue() * style.getPreCast();							
						}
						if(CommonUtil.isNotBlank(style.getPuPrice())){
							totPuPrice += dtl.getScanQty().longValue() * style.getPuPrice();
						}
					}
					newBillDtls.add(dtl);
				}
			}
		}
		bill.setTotPrePrice(totPreCase);
		bill.setTotPuPrice(totPuPrice);
		bill.setTotPrice(totPrice);
		return newBillDtls;
	}
	public static List<BillDtl> filterNotScanByActQty(Bill bill) {
		List<BillDtl> billDtls = bill.getDtlList();
		List<BillDtl> newBillDtls = new ArrayList<>();
		double totPrice = 0;
		double totPuPrice = 0;
		double totPreCase = 0;
		if (CommonUtil.isNotBlank(billDtls)) {
			for (BillDtl dtl : billDtls) {
				if (CommonUtil.isNotBlank(dtl.getActQty())&&dtl.getActQty().longValue()!=0L) {
					Style style = CacheManager.getStyleById(dtl.getStyleId());
					if (CommonUtil.isNotBlank(style)) {
						if(CommonUtil.isNotBlank(style.getPrice())){
							totPrice += dtl.getActQty().longValue() * style.getPrice();
						}
						if(CommonUtil.isNotBlank(style.getPreCast())){
							totPreCase += dtl.getActQty().longValue() * style.getPreCast();							
						}
						if(CommonUtil.isNotBlank(style.getPuPrice())){
							totPuPrice += dtl.getActQty().longValue() * style.getPuPrice();
						}
					}
					newBillDtls.add(dtl);
				}
			}
		}
		bill.setTotPrePrice(totPreCase);
		bill.setTotPuPrice(totPuPrice);
		bill.setTotPrice(totPrice);
		return newBillDtls;
	}
	/**
	 * 自定义表id，多次提交
	 * */
	public static void formateBill(Business bus) {
		Bill bill = bus.getBill();
		if (CommonUtil.isNotBlank(bill)) {
			String billNoNew = String.valueOf(new Date().getTime()) + "_"
					+ bill.getBillNo();
			bill.setBillNo(billNoNew);
			for (BillDtl dtl : bill.getDtlList()) {
				dtl.setBillNo(billNoNew);
				dtl.setId(dtl.getId() + dtl.getBillId());
			}
			bus.setBillNo(billNoNew);
		}
	}

	public static void formatPrice(Business bus) {
		double totPrice = 0;
		double totPreCase = 0;
		double totPuPrice=0;
		for (BusinessDtl dtl : bus.getDtlList()) {
			Style style = CacheManager.getStyleById(dtl.getStyleId());
			if (CommonUtil.isNotBlank(style)) {
				if(CommonUtil.isNotBlank(style.getPrice())){
					totPrice += dtl.getQty() * style.getPrice();
				}
				if(CommonUtil.isNotBlank(style.getPreCast())){
					totPreCase += dtl.getQty() * style.getPreCast();
				}
				if(CommonUtil.isNotBlank(style.getPuPrice())){
					totPuPrice += dtl.getQty() * style.getPuPrice();							
				}
			}
		}
		bus.setTotPreCase(totPreCase);
		bus.setTotPrice(totPrice);
		bus.setTotPuPrice(totPuPrice);
	}
	/**
	 *   private String id;
  private String code;
  private String taskId;
  private String cartonId;
  private Integer token;
  private String deviceId;
  private String storageId;
  private String ownerId;

  private Date scanTime;

  private String sku;
  private String styleId;
  private String colorId;
  private String sizeId;
	 *销售更新详单
	 * */
	public static void addSaleDtl(List<SaleBill> bills, List<SaleBillDtl> dtls){
		if(CommonUtil.isNotBlank(bills)&&CommonUtil.isNotBlank(dtls)){
			for(SaleBill bill:bills){
				if(CommonUtil.isBlank(bill.getDtlList())){
					bill.setDtlList(new ArrayList<SaleBillDtl>());
				}
				if(CommonUtil.isBlank(bill.getRecordList())){
					bill.setRecordList(new ArrayList<Record>());
				}
				for(SaleBillDtl dtl:dtls){
					if(dtl.getBillNo().equals(bill.getBillNo())){
						bill.getDtlList().add(dtl);
						if(CommonUtil.isNotBlank(dtl.getUniqueCode())){
							Record r=new Record();
							r.setId(new GuidCreator().toString());
							r.setScanTime(dtl.getBillDate());
							r.setCode(dtl.getUniqueCode());
							r.setSku(dtl.getCode());
							r.setStyleId(dtl.getStyleId());
							r.setSizeId(dtl.getSizeId());
							r.setColorId(dtl.getColorId());
							r.setTaskId(dtl.getBillNo());
							r.setCartonId("1");
							r.setDeviceId("KE");
							if(bill.getType()== Constant.ScmConstant.SaleBillType.Outbound){
								r.setToken(Constant.Token.Shop_Sales);								
							}else{
								r.setToken(Constant.Token.Shop_Sales_refund);	
							}
							r.setOwnerId(bill.getOwnerId());
							r.setOrigId(bill.getOwnerId());
							bill.getRecordList().add(r);
						}
					}
					
				}
			}
		}
	}
}
