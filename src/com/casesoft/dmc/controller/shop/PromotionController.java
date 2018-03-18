package com.casesoft.dmc.controller.shop;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.shop.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by WinLi on 2017-05-17.
 */
@Controller
@RequestMapping("/shop/promotion")
public class PromotionController extends BaseController {

    @Autowired
    private PromotionService promotionService;
    @RequestMapping("/index")
    @Override
    public String index() {
        String ownerId = this.getCurrentUser().getOwnerId();
        Unit unit = CacheManager.getUnitById(ownerId);
        if(unit.getType() != Constant.UnitType.Shop) {
            this.getRequest().setAttribute("ownerId",ownerId);
        } else {
            this.getRequest().setAttribute("ownerId",unit.getOwnerId());//门店所属组织
            this.getRequest().setAttribute("shopId",ownerId);
            this.getRequest().setAttribute("shopName",unit.getName());
        }
        return "/views/shop/promotion";
    }
    @RequestMapping("/addMzcx")
    public ModelAndView addMzcx() {
        ModelAndView modelAndView = new ModelAndView("/views/shop/promotion_MZ_edit");
        modelAndView.addObject("pageTitle","满赠促销");
        return modelAndView;
    }

    @RequestMapping("/addMjcx")
    public ModelAndView addMjcx() {
        ModelAndView modelAndView = new ModelAndView("/views/shop/promotion_MJ_edit");
        modelAndView.addObject("pageTitle","满减促销");
        return modelAndView;
    }
}
