package com.casesoft.dmc.controller.search;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.shop.ShopTurnOver;
import com.casesoft.dmc.model.shop.payDetail;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.cfg.PropertyService;
import com.casesoft.dmc.service.shop.payDetailService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by lly on 2018/9/29.
 */
@Controller
@RequestMapping("/search/shopTurnover")
public class ShopTurnoverController extends BaseController{
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private payDetailService payDetailService;
    @Autowired
    private UnitService unitService;

    @Override
    public String index() {
        return null;
    }
    @RequestMapping("/index")
    public ModelAndView indexMV(){
        ModelAndView mv = new ModelAndView("/views/search/shopTurnoverSearch");
        mv.addObject("userId", getCurrentUser().getId());
        PropertyKey propertyKey = propertyService.getDefaultPayType();
        mv.addObject("payType", propertyKey.getIconCode());
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        return mv;
    }
    @RequestMapping(value = {"/getDetail"})
    @ResponseBody
    public Page<payDetail> getDetail(Page<payDetail> page, String userId) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        //权限设置，增加过滤条件，只显示当前ownerId下的销售单信息
        User CurrentUser = CacheManager.getUserById(userId);
        String ownerId = CurrentUser.getOwnerId();
        String id = CurrentUser.getId();
        if (!id.equals("admin")) {
            PropertyFilter filter = new PropertyFilter("EQS_ownerId", ownerId);
            filters.add(filter);
        }
        page.setPageProperty();
        page = this.payDetailService.findPage(page, filters);
        return page;
    }
    @RequestMapping("/priceDetail")
    public ModelAndView detailMV(String shopId,String payType){
        ModelAndView mv = new ModelAndView("/views/search/shopTurnoverSearchDetail");
        mv.addObject("userId", getCurrentUser().getId());
        Unit unit = new Unit();
        if(!"1".equals(shopId)){
            unit = unitService.findUnitByCode(shopId,4);
        }
        else {
            unit = unitService.findUnitByCode(shopId,1);
        }
        mv.addObject("unit", unit);
        mv.addObject("shopId", shopId);
        mv.addObject("payType", payType);
        mv.addObject("mainUrl", "/search/shopTurnover/index.do");
        return mv;
    }
    @RequestMapping("getPriceCount")
    @ResponseBody
    public Page<ShopTurnOver> getPriceCount(Page<ShopTurnOver> page,String GED_billDate,String LED_billDate,String shopId,String payType){
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.payDetailService.getPriceCount(page,GED_billDate,LED_billDate,shopId,payType);
        return page;
    }
}
