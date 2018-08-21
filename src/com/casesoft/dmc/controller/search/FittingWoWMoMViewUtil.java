package com.casesoft.dmc.controller.search;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.search.FittingWoWMoMView;
import com.casesoft.dmc.model.sys.Unit;

import java.text.NumberFormat;
import java.util.List;

public class FittingWoWMoMViewUtil {

	public static void formateFittingWoWMoMView(List<FittingWoWMoMView> lists){
			  NumberFormat numberFormat = NumberFormat.getInstance();  
			  
		        // 设置精确到小数点后2位  
		        numberFormat.setMaximumFractionDigits(2);  
			   if(lists!=null){
				   for(FittingWoWMoMView fittingWoWMoMView:lists){
					   Unit unit= CacheManager.getUnitByCode(fittingWoWMoMView.getWarehId());
					   if(!CommonUtil.isBlank(unit)){
						   fittingWoWMoMView.setWarehName(unit.getName());
					   }
	                   if(CommonUtil.isNotBlank(fittingWoWMoMView.getLastWeekQty())){
	                	   String result = numberFormat.format((float) (fittingWoWMoMView.getWeekQty()-fittingWoWMoMView.getLastWeekQty())
	                			   / (float) fittingWoWMoMView.getWeekQty() * 100);
	                	   fittingWoWMoMView.setWow(result+"%");
					   }
					   if(CommonUtil.isNotBlank(fittingWoWMoMView.getLastMonthQty())){
						   String result = numberFormat.format((float) (fittingWoWMoMView.getMonthQty()-fittingWoWMoMView.getLastMonthQty())
	                			   / (float) fittingWoWMoMView.getMonthQty() * 100);
						   fittingWoWMoMView.setMom(result+"%");
					   }
					   if(CommonUtil.isNotBlank(fittingWoWMoMView.getLastYearMonthQty())){
						   String result = numberFormat.format((float) (fittingWoWMoMView.getMonthQty()-fittingWoWMoMView.getLastYearMonthQty())
	                			   / (float) fittingWoWMoMView.getMonthQty() * 100);
						   fittingWoWMoMView.setMom(result+"%"); 
					   }
					   if(CommonUtil.isNotBlank(fittingWoWMoMView.getLastYearWeekQty())){
						   String result = numberFormat.format((float) (fittingWoWMoMView.getWeekQty()-fittingWoWMoMView.getLastYearWeekQty())
	                			   / (float) fittingWoWMoMView.getWeekQty() * 100);
						   fittingWoWMoMView.setWowh(result+"%");
					   }
				   }
				   
			   }
		
		
	}
}
