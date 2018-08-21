package com.casesoft.dmc.controller.search;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.product.StyleUtil;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.search.PurchaseSaleStock;
import com.casesoft.dmc.model.sys.Unit;

import java.util.List;

/**
 * Created by Alvin on 2018/2/3.
 */
public class PurchaseSaleStockUtil {

    /**
     * @param purchaseSaleStockList
     * @param rootPath //服务器路径
     * @param imageExt //图片后缀 原图无后缀，压缩图有后缀用_连接
     * 完善PurchaseSaleStock中Transient字段信息
     * */
    public static void convertToPurchaseSaleSotck(List<PurchaseSaleStock> purchaseSaleStockList,String rootPath, String imageExt) {
        for(PurchaseSaleStock s:purchaseSaleStockList){
            Product p = CacheManager.getProductByCode(s.getSku());
            if(CommonUtil.isNotBlank(p)){
                s.setStyleId(p.getStyleId());
                s.setStyleName(p.getStyleName());
                s.setColorId(p.getColorId());
                s.setSizeId(p.getSizeId());
                Style style = CacheManager.getStyleById(s.getStyleId());
                s.setPrice(style.getPrice());
                //加载款图片信息
                String url="";
                if(CommonUtil.isBlank(imageExt)) {
                   url  = StyleUtil.findImgUrl(s.getStyleId(), rootPath);
                }else{
                    url = StyleUtil.exportImgUrl(s.getStyleId(), rootPath,imageExt);
                }
                s.setImgUrl(url);

            }
            Unit u = CacheManager.getUnitByCode(s.getWarehId());
            s.setWarehName(u.getName());

        }

    }
}
