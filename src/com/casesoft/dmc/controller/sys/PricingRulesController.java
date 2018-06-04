package com.casesoft.dmc.controller.sys;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;

import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.cfg.PropertyType;
import com.casesoft.dmc.model.sys.PricingRules;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.cfg.PropertyKeyService;
import com.casesoft.dmc.service.product.StyleService;
import com.casesoft.dmc.service.sys.impl.PricingRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;


/**
 * 定价规则
 *
 * @author liutianci 修改时间2018.3.28
 * 修改内容定价规则 验证add、edit方法的传送
 */

@Controller
@RequestMapping("/sys/pricingRules")
public class PricingRulesController extends BaseController implements IBaseInfoController<PricingRules> {

    @Autowired
    private PricingRulesService pricingRulesService;

    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<PricingRules> findPage(Page<PricingRules> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        page.setPageProperty();
        page = this.pricingRulesService.findPage(page, filters);
        for (PricingRules p : page.getRows()) {
            PropertyKey propertyKey = CacheManager.getPropertyKey("C9-" + p.getSeries());
            p.setSeriesName(propertyKey.getName());
        }
        return page;
    }


    /**
     * 小程序商品款式查listWS
     */
    @RequestMapping(value = {"/list", "/listWS"})
    @ResponseBody
    @Override
    public List<PricingRules> list() throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        List<PricingRules> pricingRules = this.pricingRulesService.find(filters);
        return pricingRules;
    }

    /*保存*/
    @RequestMapping(value = "/save")
    @ResponseBody
    @Override
    public MessageBox save(PricingRules pricingRules) throws Exception {
        this.logAllRequestParams();
        String pageType = this.getReqParam("pageType");
        PricingRules pr = this.pricingRulesService.get("series", pricingRules.getSeries());
        if ("add".equals(pageType)) {
            if (CommonUtil.isNotBlank(pr)) {
                return this.returnFailInfo("保存失败");
            } else {
                pr = new PricingRules();
                User u = getCurrentUser();
                pr.setUserId(u.getCode());
                pr.setName(pricingRules.getName());
                pr.setRule1(pricingRules.getRule1());
                pr.setRule2(pricingRules.getRule2());
                pr.setRule3(pricingRules.getRule3());
                pr.setSeries(pricingRules.getSeries());
                pr.setUpdateTime(new Date());
            }
        } else {
            pr.setName(pricingRules.getName());
            pr.setRule1(pricingRules.getRule1());
            pr.setRule2(pricingRules.getRule2());
            pr.setRule3(pricingRules.getRule3());
            pr.setUpdateTime(new Date());
            User u = getCurrentUser();
            pr.setUserId(u.getCode());
        }
        try {
            this.pricingRulesService.save(pr);
            return returnSuccessInfo("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return returnFailInfo("保存失败");
        }
    }

    @Override
    public MessageBox edit(String taskId) throws Exception {
        return null;
    }

    @Override
    public MessageBox delete(String taskId) throws Exception {
        return null;
    }

    @Override
    public void exportExcel() throws Exception {

    }

    @Override
    public MessageBox importExcel(MultipartFile file) throws Exception {
        return null;
    }

    @Override
    public String index() {
        return null;
    }

    @RequestMapping("/index")
    public ModelAndView indexMV() {
        ModelAndView mv = new ModelAndView("/views/sys/pricingRules");
        List<PropertyType> propertyTypeList = this.pricingRulesService.findPricingRulesPropertyType();
        mv.addObject("classTypes", propertyTypeList);
        return mv;
    }

    /*状态判断*/
    @RequestMapping(value = "/changePricingRulesStatus")
    @ResponseBody
    public MessageBox changePricingRulesStatus(String id, String state) {
        this.logAllRequestParams();
        try {
            PricingRules pr = this.pricingRulesService.load(id);
            pr.setState(state);
            this.pricingRulesService.save(pr);
            return returnSuccessInfo("更改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return returnFailInfo("更改失败");
        }
    }
}
