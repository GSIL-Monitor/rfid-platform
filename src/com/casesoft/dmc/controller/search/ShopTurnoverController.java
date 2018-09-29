package com.casesoft.dmc.controller.search;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.shop.ShopTurnOver;
import com.casesoft.dmc.service.cfg.PropertyService;
import com.casesoft.dmc.service.shop.payDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
        return mv;
    }
    @RequestMapping("getPriceCount")
    public Page<ShopTurnOver> getPriceCount(Page<ShopTurnOver> page){
        return this.payDetailService.getPriceCount(page);
    }
}
