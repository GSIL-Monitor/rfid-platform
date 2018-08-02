package com.casesoft.dmc.controller.logistics;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.SaleOrderReturnBill;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.sys.Resource;
import com.casesoft.dmc.model.sys.ResourcePrivilege;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.logistics.SaleOrderReturnBillService;
import com.casesoft.dmc.service.shop.CustomerService;
import com.casesoft.dmc.service.stock.EpcStockService;
import com.casesoft.dmc.service.sys.ResourcePrivilegeService;
import com.casesoft.dmc.service.sys.impl.ResourceService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 *
 * Created by admin on 2018/4/11.
 */
@Controller
@RequestMapping(value = "/logistics")
public class FranchiserOrderReturnController extends BaseController implements ILogisticsBillController<SaleOrderReturnBill> {
    @Autowired
    private SaleOrderReturnBillService saleOrderReturnBillService;
    @Autowired
    private EpcStockService epcStockService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ResourcePrivilegeService resourcePrivilegeService;

    @RequestMapping(value = "/franchiserOrderReturnNew/index")
    public ModelAndView indexMV() throws Exception {
        ModelAndView mv = new ModelAndView("/views/logistics/franchiserOrderReturnNew");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        Unit unit = this.unitService.getunitbyId(getCurrentUser().getOwnerId());
        mv.addObject("name", unit.getName());
        String defaultWarehId = unit.getDefaultWarehId();
        String defaultSaleStaffId = unit.getDefaultSaleStaffId();
        String defalutCustomerId = unit.getDefalutCustomerId();
        if(CommonUtil.isNotBlank(defalutCustomerId)&&defalutCustomerId!=null){
            Customer customer = this.customerService.load(defalutCustomerId);
            mv.addObject("defalutCustomerId", defalutCustomerId);
            mv.addObject("defalutCustomerName", customer.getName());
            mv.addObject("defalutCustomerdiscount", customer.getDiscount());
            mv.addObject("defalutCustomercustomerType", unit.getType());
            mv.addObject("defalutCustomerowingValue", customer.getOwingValue());
        }
        mv.addObject("userId", getCurrentUser().getId());
        mv.addObject("roleid", getCurrentUser().getRoleId());
        mv.addObject("defaultWarehId", defaultWarehId);


        mv.addObject("defaultSaleStaffId", defaultSaleStaffId);
        mv.addObject("ownersId", unit.getOwnerids());
        mv.addObject("pageType", "add");
        mv.addObject("ownersId", unit.getOwnerids());
        mv.addObject("userId", getCurrentUser().getId());
        return mv;
    }


    @RequestMapping(value = "/FranchiserOrderReturn/page")
    @ResponseBody
    public Page<SaleOrderReturnBill> findPage(Page<SaleOrderReturnBill> page, String userId) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        User currentUser = CacheManager.getUserById(userId);
        String ownerId = currentUser.getOwnerId();
        String id = currentUser.getId();
        if(!id.equals("admin")){
            PropertyFilter filter = new PropertyFilter("EQS_origUnitId", ownerId);
            filters.add(filter);
        }
        page.setPageProperty();
        page = this.saleOrderReturnBillService.findPage(page, filters);
        return page;
    }

    @Override
    public Page<SaleOrderReturnBill> findPage(Page<SaleOrderReturnBill> page) throws Exception {
        return null;
    }

    @RequestMapping(value = "/FranchiserOrderReturn/list")
    @ResponseBody
    @Override
    public List<SaleOrderReturnBill> list() throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());

        List<SaleOrderReturnBill> saleOrderReturnBillList = this.saleOrderReturnBillService.find(filters);

        return saleOrderReturnBillList;
}

    @Override
    public MessageBox save(String bill, String strDtlList, String userId) throws Exception {
        return null;
    }

    @Override
    public ModelAndView add() throws Exception {
        return null;
    }

    @RequestMapping(value = "/FranchiserOrderReturn/edit")
    @ResponseBody
    @Override
    public ModelAndView edit(String billNo) throws Exception {
        SaleOrderReturnBill saleOrderReturnBill = this.saleOrderReturnBillService.findBillByBillNo(billNo);
        ModelAndView mav = new ModelAndView("/views/logistics/FranchiserOrderReturnDetail");
        mav.addObject("ownerId", getCurrentUser().getOwnerId());
        mav.addObject("userId",getCurrentUser().getId());
        mav.addObject("saleOrderReturn", saleOrderReturnBill);
        mav.addObject("roleid", getCurrentUser().getRoleId());
        Unit unit = CacheManager.getUnitById(getCurrentUser().getOwnerId());
        mav.addObject("ownersId", unit.getOwnerids());
        mav.addObject("mainUrl", "/logistics/FranchiserOrderReturn/index.do");
        return mav;
    }

    @RequestMapping(value = "/FranchiserOrderReturn/back")
    @ResponseBody
    public ModelAndView back(String billNo){
        SaleOrderReturnBill saleOrderReturnBill = this.saleOrderReturnBillService.findBillByBillNo(billNo);
        saleOrderReturnBill.setBillType(Constant.ScmConstant.BillType.Save);
        HttpServletRequest request = this.getRequest();
        HttpSession session = request.getSession();
        session.removeAttribute("billNosaleReturn");
        this.saleOrderReturnBillService.save(saleOrderReturnBill);
        ModelAndView mv = new ModelAndView("/views/logistics/FranchiserOrderRetur");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        mv.addObject("userId", getCurrentUser().getId());
        return mv;
    }

    @Override
    public MessageBox check(String billNo) throws Exception {
        return null;
    }

    @Override
    public MessageBox end(String billNo) throws Exception {
        return null;
    }

    @Override
    public MessageBox cancel(String billNo) throws Exception {
        return null;
    }

    @Override
    public MessageBox convert(String strDtlList, String recordList) throws Exception {
        return null;
    }

    @Override
    public String index() {
        return null;
    }

    @RequestMapping(value = "/FranchiserOrderReturn/findResourceButton")
    @ResponseBody
    public MessageBox findResourceButton(){
        try {
            Resource resource = this.resourceService.get("url", "logistics/franchiserOrderReturnNew");
            List<ResourcePrivilege> resourcePrivilege = this.resourcePrivilegeService.findResourceButtonByCodeAndRoleId(resource.getCode(), this.getCurrentUser().getRoleId(),"button");
            return new MessageBox(true, "查询成功", resourcePrivilege);
        }catch (Exception e){
            e.printStackTrace();
            return new MessageBox(true, "查询失败");
        }
    }
}
