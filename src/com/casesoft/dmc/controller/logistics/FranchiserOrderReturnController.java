package com.casesoft.dmc.controller.logistics;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.json.FastJSONUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.SaleOrderReturnBill;
import com.casesoft.dmc.model.shop.Customer;
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

import java.util.List;

/**
 * @Author Liutianci
 * @Description //TODO 
 * @Date 14:38 2018/9/20
 * @Param
 * @return 
 **/
@Controller
@RequestMapping(value = "/logistics/franchiserOrderReturn")
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

    /*
     * @Author Liutianci
     * @Description //TODO
     * @Date 14:38 2018/9/20
     * @Param []
     * @return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {
        ModelAndView mv = new ModelAndView("/views/logistics/franchiserOrderReturn");
        List<ResourcePrivilege> resourcePrivilege = this.resourcePrivilegeService.findPrivilege("logistics/franchiserOrderReturn", this.getCurrentUser().getRoleId());
        mv.addObject("resourcePrivilege", FastJSONUtil.getJSONString(resourcePrivilege));
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        Unit unit = this.unitService.getunitbyId(getCurrentUser().getOwnerId());
        mv.addObject("name", unit.getName());
        String defaultWarehId = unit.getDefaultWarehId();
        String defaultSaleStaffId = unit.getDefaultSaleStaffId();
        String defalutCustomerId = unit.getDefalutCustomerId();
        if (CommonUtil.isNotBlank(defalutCustomerId) && defalutCustomerId != null) {
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

    /*
     * @Author Liutianci
     * @Description //TODO
     * @Date 14:38 2018/9/20
     * @Param [page, userId]
     * @return com.casesoft.dmc.core.util.page.Page<com.casesoft.dmc.model.logistics.SaleOrderReturnBill>
     **/
    @RequestMapping(value = "/page")
    @ResponseBody
    public Page<SaleOrderReturnBill> findPage(Page<SaleOrderReturnBill> page, String userId) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        User currentUser = CacheManager.getUserById(userId);
        String ownerId = currentUser.getOwnerId();
        String id = currentUser.getId();
        if (!id.equals("admin")) {
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

    @Override
    public List<SaleOrderReturnBill> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(String bill, String strDtlList, String userId) throws Exception {
        return null;
    }

    @Override
    public ModelAndView add() throws Exception {
        return null;
    }


    @Override
    public ModelAndView edit(String billNo) throws Exception {
        return null;
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
}
