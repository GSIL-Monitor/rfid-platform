package com.casesoft.dmc.controller.search;

import java.text.DecimalFormat;
import java.util.List;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.search.FittingConvertSaleView;
import com.casesoft.dmc.model.sys.Unit;

public class FittingConvertSaleViewUtil {

	public static void countScale(List<FittingConvertSaleView> lists){
		   DecimalFormat   df=new   
				  DecimalFormat("#.##"); 
		   
		   if(lists!=null){
			   for(FittingConvertSaleView fittingConvertSaleView:lists){
				   
				   if(fittingConvertSaleView.getFittingQty()!=0
						   &&fittingConvertSaleView.getSaleQty()!=0){
					   fittingConvertSaleView.setScale(df.format(fittingConvertSaleView.getFittingQty()/fittingConvertSaleView.getSaleQty()));				
				   }
				   Unit unit= CacheManager.getUnitByCode(fittingConvertSaleView.getWarehId());
				   if(!CommonUtil.isBlank(unit)){
					   fittingConvertSaleView.setWarehName(unit.getName());
				   }
			   }
			   
		   }
		
	}
}
