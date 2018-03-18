package com.casesoft.dmc.controller.shop;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.shop.SaleBill;
import com.casesoft.dmc.model.shop.SaleBillDtl;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.shop.SaleBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by WingLi on 2017-01-04.
 * 零售任务在保存时需要复制一份主单到Business表中和明细数据到BusinessDtl中，便于库存视图的查询
 */
@Controller
@RequestMapping("/shop/saleBill")
public class SaleBillController extends BaseController  {


    @Autowired
    private SaleBillService saleBillService;

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
        return "/views/shop/saleBill";
    }

    @RequestMapping("/page")
    @ResponseBody
    public Page<SaleBill> findPage(Page<SaleBill> page) throws Exception {
        this.logAllRequestParams();//日志
        List<PropertyFilter> filters=PropertyFilter.buildFromHttpRequest(this.getRequest());
        page.setPageProperty();
        page=this.saleBillService.findPage(page, filters);
        SaleBillUtil.convertToSaleBillVo(page.getRows());
        return page;
    }

    @RequestMapping("/listDtl")
    @ResponseBody
    public List<SaleBillDtl> listDtl(String billId) {
        this.logAllRequestParams();
        List<SaleBillDtl> saleBillDtlList = this.saleBillService.findDtlList(billId);
        SaleBillUtil.convertToVo(saleBillDtlList);
        return saleBillDtlList;
    }

    @RequestMapping("/listDtlPage")
    public ModelAndView listDtlPage(String billId) {
        this.logAllRequestParams();
        SaleBill saleBill = this.saleBillService.load(billId);
        SaleBillUtil.convertToSaleBillVo(saleBill);
        ModelAndView modelAndView = new ModelAndView("/views/shop/saleBill_detail");
        modelAndView.addObject("saleBill",saleBill);
        return modelAndView;
    }

    public MessageBox delete(String id){
            this.saleBillService.delete(id);
            return returnSuccessInfo("删除成功");
    }

}
