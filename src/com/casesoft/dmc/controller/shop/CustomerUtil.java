package com.casesoft.dmc.controller.shop;


import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.product.ProductUtil;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.shop.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CustomerUtil {

	public static void covertToCustmomer(Customer customer,
			Long buyBillQty, List<Object[]> saleAndRefundSum, SaleBill lastBill, List<SaleBillColorAnalysis> colorAnalysis,
			List<SaleBillStyleAnalysis> styleAnalysis,
			List<SaleBillSizeAnalysis> sizeAnalysis) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		customer.setLastBill(CommonUtil.isBlank(lastBill)?"":sdf.format(lastBill.getBillDate()));	
		if(saleAndRefundSum.size() == 0){
			customer.setBuyQty(0L);
			customer.setRefundQty(0L);
		}else{
			customer.setBuyQty(Long.parseLong(saleAndRefundSum.get(0)[1].toString()));
			customer.setRefundQty(Long.parseLong("0"));
			if(saleAndRefundSum.size() == 2){
				customer.setRefundQty(~Long.parseLong(saleAndRefundSum.get(1)[1].toString())+1);
			}
		}
		if(sizeAnalysis.size() > 4){
			customer.setSizeLike(sizeAnalysis.subList(0, 5));
		}else{
			customer.setSizeLike(sizeAnalysis);
		}
		if(colorAnalysis.size() > 4){
			customer.setColorLike(colorAnalysis.subList(0, 5));
		}else{
			customer.setColorLike(colorAnalysis);
		}
		List<StyleSubClassAnalysis> styleSubList = new ArrayList<StyleSubClassAnalysis>();
		List<StyleMaterialAnalysis> styleMateriallist = new ArrayList<StyleMaterialAnalysis>();
		int i = 1;
		long totSpringSummerBuyQty =0;
		long totFallWinterBuyQty = 0;
		double totSpringSummerPrice =0;
		double totFallWinterPrice =0;
		for(SaleBillStyleAnalysis s : styleAnalysis){
			if(CommonUtil.isNotBlank(s.getClass10())){
			    if(s.getClass10().equals("1")
			      ||s.getClass10().equals("2")){
				    totSpringSummerBuyQty += s.getClas10BuyCount();
				    totSpringSummerPrice += s.getTotalPrice();
			    }else if(s.getClass10().equals("3")
				  ||s.getClass10().equals("4")){
				   totFallWinterBuyQty += s.getClas10BuyCount();
				   totFallWinterPrice += s.getTotalPrice();
			    }
			}
			if(i > 5){
				
			}else{
				if(CommonUtil.isNotBlank(s.getClass4())){
					PropertyKey subClass = CacheManager.getPlayloungePropertyKey(s.getClass4()+"-C4");
					StyleSubClassAnalysis styleSubClass = new StyleSubClassAnalysis(s.getStyleId(), s.getStyleName(),subClass.getName(), s.getClass4BuyCount());
					styleSubList.add(styleSubClass);
				}				
				if(CommonUtil.isNotBlank(s.getClass8())){
					PropertyKey material = CacheManager.getPlayloungePropertyKey(s.getClass8()+"-C8");
					StyleMaterialAnalysis styleMater = new StyleMaterialAnalysis(s.getStyleId(), s.getStyleName(), material.getName(), s.getClass8BuyCount());
				    styleMateriallist.add(styleMater);
				}
			}
			
			i++;
		}
		if(customer.getRefundQty() ==0){
			customer.setRefundRate(0.0);
		}else{
			customer.setRefundRate((double)(customer.getRefundQty()/customer.getBuyQty()));
		}
		customer.setStyleMateriaLike(styleMateriallist);
		customer.setStyleSubClassLike(styleSubList);
		DecimalFormat df  = new DecimalFormat("######0.00");
		if(totSpringSummerBuyQty == 0){
			customer.setSpringSummerAvgPrice(0.0);
		}else{
			customer.setSpringSummerAvgPrice(Double.parseDouble(df.format(totSpringSummerPrice/totSpringSummerBuyQty)));
		}
		if(totFallWinterBuyQty == 0){
			customer.setFallWIntegererAvgPrice(0.0);
		}else{
			customer.setFallWIntegererAvgPrice(Double.parseDouble(df.format(totFallWinterPrice/totFallWinterBuyQty)));
		}		
		customer.setAssociatedRate(buyBillQty == 0L ? 0.0:(Double.parseDouble(df.format((double) (customer.getBuyQty()/buyBillQty)))));
		
        		
	}

	public static void covertToBriefSaleBillDtl(List<SaleBillDtl> dtlList, List<SaleBillDtlBreifInfo> dtlBriefList, String root) {
		for(SaleBillDtl s : dtlList){
			SaleBillDtlBreifInfo briefInfo = new SaleBillDtlBreifInfo(s.getBillNo(), s.getCode(), s.getPercent(), s.getActPrice(), s.getQty(), ProductUtil.setProducImagePath(s.getStyleId(), s.getColorId(), root));
			dtlBriefList.add(briefInfo);
		}
	}

	public static void covertProperty(Customer customer,List<Object[]> reasons) {
		List<StyleSubClassAnalysis> styleSubList = customer.getStyleSubClassLike();
		List<StyleMaterialAnalysis> styleMateriallist = customer.getStyleMateriaLike();
		if (CommonUtil.isNotBlank(styleSubList)) {
			for (StyleSubClassAnalysis s : styleSubList) {
				if (CommonUtil.isNotBlank(s.getSubName())) {
					PropertyKey subClass = CacheManager.getPlayloungePropertyKey(s.getSubName() + "-C4");
					if (CommonUtil.isNotBlank(subClass)) {
						s.setSubName(subClass.getName());
					}
				}
			}
		}
	}

}
