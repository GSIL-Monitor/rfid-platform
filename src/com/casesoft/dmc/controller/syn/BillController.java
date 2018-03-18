package com.casesoft.dmc.controller.syn;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.syn.tool.BillUtil;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.syn.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 2017/1/10.
 */
@Controller
@RequestMapping("/syn/bill")
public class BillController extends BaseController {

    @Autowired
    private BillService billService;

    @RequestMapping("/index")
    @Override
    public String index() {
        return "/views/syn/billManager";
    }

    @RequestMapping("/page")
    @ResponseBody
    public Page<Bill> list(Page<Bill> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.billService.findPage(page, filters);
        BillUtil.covertToVos(page.getRows());
        return page;
    }

    @RequestMapping(value = "/detail/index")
    @ResponseBody
    public ModelAndView viewDtlIndex(String billNo) {
        this.logAllRequestParams();
        Bill bill = this.billService.get("billNo", billNo);
        ModelAndView model = new ModelAndView();
        if (!CommonUtil.isBlank(bill.getOrigId())
                && CommonUtil.isNotBlank(CacheManager.getUnitById(bill.getOrigId()))) {
            Unit u = CacheManager.getUnitById(bill.getOrigId());
            bill.setOrigName(u.getName());
        }
        if (!CommonUtil.isBlank(bill.getDestId())
                && CommonUtil.isNotBlank(CacheManager.getUnitById(bill.getDestId()))) {
            bill.setDestName(CacheManager.getUnitById(bill.getDestId())
                    .getName());
        }
        if (!CommonUtil.isBlank(bill.getDestUnitId())
                && CommonUtil.isNotBlank(CacheManager.getUnitById(bill.getDestUnitId()))) {
            Unit u = CacheManager.getUnitById(bill.getDestUnitId());
            bill.setDestUnitName(u.getName());
        }
        if (!CommonUtil.isBlank(bill.getOrigUnitId())
                && CommonUtil.isNotBlank(CacheManager.getUnitById(bill.getOrigUnitId()))) {
            bill.setOrigUnitName(CacheManager.getUnitById(bill.getOrigUnitId())
                    .getName());
        }
        bill.setBillType(CacheManager.getSetting(bill.getType().toString()).getValue());
        model.addObject("mainUrl", "/syn/bill/index.do");
        model.addObject("bill", bill);
        model.setViewName("/views/syn/billDetail");
        return model;
    }

    @RequestMapping(value = "/findDetail")
    @ResponseBody
    public List<BillDtl> findDetail(String billId) throws Exception {
        this.logAllRequestParams();
        List<BillDtl> billDtls = this.billService.loadDtls(billId);
        billDtls = BillUtil.convertToVo(billDtls);
        return billDtls;
    }

}
