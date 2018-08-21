package com.casesoft.dmc.controller.product;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.cfg.Device;
import com.casesoft.dmc.model.product.StyleScore;

import java.util.List;

public class StyleSocreUtil {

	public static void setShopId(List<StyleScore> list) throws Exception {		
		for(StyleScore s: list){
			String deviceId = s.getDeviceId();
			Device d = CacheManager.getDeviceByCode(deviceId);
			if(CommonUtil.isBlank(d)){
				throw new Exception("该设备没有所属店铺,请设置");
			}
			s.setShopId(d.getStorageId());
			
		}
	}

}
