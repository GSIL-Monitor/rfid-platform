package com.casesoft.dmc.controller.search;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.search.SaleWoWMoMView;
import com.casesoft.dmc.model.sys.Unit;

import java.text.NumberFormat;
import java.util.List;

public class SaleWoWMoMViewUtil {

	public static void formateSaleWoWMoMView(List<SaleWoWMoMView> lists){
		  NumberFormat numberFormat = NumberFormat.getInstance();  
		  
	        // 设置精确到小数点后2位  
	        numberFormat.setMaximumFractionDigits(2);  
		   if(lists!=null){
			   for(SaleWoWMoMView saleWoWMoMView:lists){
				   Unit unit= CacheManager.getUnitByCode(saleWoWMoMView.getWarehId());
				   if(!CommonUtil.isBlank(unit)){
					   saleWoWMoMView.setWarehName(unit.getName());
				   }
                   if(CommonUtil.isNotBlank(saleWoWMoMView.getLastWeekQty())){
                	   String result = numberFormat.format((float) (saleWoWMoMView.getWeekQty()-saleWoWMoMView.getLastWeekQty())
                			   / (float) saleWoWMoMView.getWeekQty() * 100);
                	   saleWoWMoMView.setWow(result+"%");
				   }
				   if(CommonUtil.isNotBlank(saleWoWMoMView.getLastMonthQty())){
					   String result = numberFormat.format((float) (saleWoWMoMView.getMonthQty()-saleWoWMoMView.getLastMonthQty())
                			   / (float) saleWoWMoMView.getMonthQty() * 100);
                	   saleWoWMoMView.setMom(result+"%");
				   }
				   if(CommonUtil.isNotBlank(saleWoWMoMView.getLastYearMonthQty())){
					   String result = numberFormat.format((float) (saleWoWMoMView.getMonthQty()-saleWoWMoMView.getLastYearMonthQty())
                			   / (float) saleWoWMoMView.getMonthQty() * 100);
                	   saleWoWMoMView.setMom(result+"%"); 
				   }
				   if(CommonUtil.isNotBlank(saleWoWMoMView.getLastYearWeekQty())){
					   String result = numberFormat.format((float) (saleWoWMoMView.getWeekQty()-saleWoWMoMView.getLastYearWeekQty())
                			   / (float) saleWoWMoMView.getWeekQty() * 100);
                	   saleWoWMoMView.setWowh(result+"%");
				   }
			   }
			   
		   }
		
	}
}
