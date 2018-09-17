package com.casesoft.dmc.controller.task;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.json.FastJSONUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.sys.ResourcePrivilege;
import com.casesoft.dmc.model.sys.Setting;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.service.logistics.*;
import com.casesoft.dmc.service.shop.CustomerService;
import com.casesoft.dmc.service.sys.ResourcePrivilegeService;
import com.casesoft.dmc.service.sys.SettingService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import com.casesoft.dmc.service.task.CargoTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yushen on 2017/9/19.
 */
@Controller
@RequestMapping("/task/cargoTracking")
public class CargoTrackingController extends BaseController implements IBaseInfoController<Business> {

    @Autowired
    CargoTrackingService cargoTrackingService;

    @RequestMapping(value = "/index")
    @Override
    public String index() {

        return "/views/task/cargoTracking";
    }

    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<Business> findPage(Page<Business> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        return this.cargoTrackingService.findPage(page, filters);
    }

    @Override
    public List<Business> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(Business entity) throws Exception {
        return null;
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

    @Autowired
    private OutboundTaskController outboundTaskController;

    @Autowired
    private InboundTaskController inboundTaskController;

    @RequestMapping(value = "/taskDetail")
    @ResponseBody
    public ModelAndView taskDetail(String id, String token) throws Exception {
        List<Integer> outTokens = Arrays.asList(10, 13, 24, 26, 28, 32);
        List<Integer> inTokens = Arrays.asList(8, 12, 23, 25, 29,11);
        if (CommonUtil.isNotBlank(token)) {
            int iToken = Integer.parseInt(token);
            if (outTokens.contains(iToken)) {
                return this.outboundTaskController.showDetail(id);
            } else if (inTokens.contains(iToken)) {
                return this.inboundTaskController.viewDtl(id);
            }
            return new ModelAndView();
        }
        return new ModelAndView();
    }

    @Autowired
    private PurchaseOrderBillService purchaseOrderBillService;

    @Autowired
    private SaleOrderBillService saleOrderBillService;

    @Autowired
    private SaleOrderReturnBillService saleOrderReturnBillService;

    @Autowired
    private TransferOrderBillService transferOrderBillService;

    @Autowired
    private ConsignmentBillService consignmentBillService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private ResourcePrivilegeService resourcePrivilegeService;

    @Autowired
    private UnitService unitService;

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/billDetail")
    @ResponseBody
    public ModelAndView billDetail(String billNo) throws Exception {
        String billType = billNo.substring(0, 2);
        switch (billType) {
            case "PI": {
                ModelAndView mv = new ModelAndView("/views/logistics/purchaseOrderBill");
                Setting setting = this.settingService.get("id", "repositoryManagement");
                mv.addObject("pageType", "add");
                List<ResourcePrivilege> resourcePrivilege = this.resourcePrivilegeService.findPrivilege("logistics/purchaseOrderBill", this.getCurrentUser().getRoleId());
                mv.addObject("resourcePrivilege", FastJSONUtil.getJSONString(resourcePrivilege));
                User user = this.getCurrentUser();
                mv.addObject("ownerId", user.getOwnerId());
                mv.addObject("userId", getCurrentUser().getId());
                Unit unit = CacheManager.getUnitByCode(getCurrentUser().getOwnerId());
                String defaultWarehId = unit.getDefaultWarehId();
                mv.addObject("defaultWarehId", defaultWarehId);
                this.getRequest().setAttribute("rm", setting);
                mv.addObject("cargoTracking", "cargoTracking");
                mv.addObject("cTbillNo", billNo);
                return mv;
            }
            case "TR": {
                ModelAndView mv = new ModelAndView("views/logistics/transferOrder");
                List<ResourcePrivilege> resourcePrivilege = this.resourcePrivilegeService.findPrivilege("logistics/transferOrder", this.getCurrentUser().getRoleId());
                mv.addObject("resourcePrivilege", FastJSONUtil.getJSONString(resourcePrivilege));
                mv.addObject("ownerId", getCurrentUser().getOwnerId());
                Unit unit = CacheManager.getUnitById(getCurrentUser().getOwnerId());
                mv.addObject("ownersId", unit.getOwnerids());
                mv.addObject("userId", getCurrentUser().getId());
                mv.addObject("pageType", "add");
                mv.addObject("ownersId", unit.getOwnerids());
                mv.addObject("cargoTracking", "cargoTracking");
                mv.addObject("cTbillNo", billNo);
                return mv;
            }
            case "SO": {
                ModelAndView mv = new ModelAndView("/views/logistics/saleOrderBill");
                Unit unit = this.unitService.getunitbyId(getCurrentUser().getOwnerId());
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
                List<ResourcePrivilege> resourcePrivilege = this.resourcePrivilegeService.findPrivilege("logistics/saleOrderBill", this.getCurrentUser().getRoleId());
                mv.addObject("resourcePrivilege", FastJSONUtil.getJSONString(resourcePrivilege));
                mv.addObject("userId", getCurrentUser().getId());
                mv.addObject("roleid", getCurrentUser().getRoleId());
                mv.addObject("defaultWarehId", defaultWarehId);
                mv.addObject("defaultSaleStaffId", defaultSaleStaffId);
                mv.addObject("ownersId", unit.getOwnerids());
                mv.addObject("pageType", "add");
                mv.addObject("ownersId", unit.getOwnerids());
                mv.addObject("userId", getCurrentUser().getId());
                Setting setting = this.settingService.get("id", "isUserAbnormal");
                mv.addObject("isUserAbnormal", setting.getValue());
                mv.addObject("cargoTracking", "cargoTracking");
                mv.addObject("cTbillNo", billNo);
                return mv;
            }
            case "SR": {
                ModelAndView mv = new ModelAndView("/views/logistics/saleOrderReturn");
                mv.addObject("ownerId", getCurrentUser().getOwnerId());
                Unit unit = this.unitService.getunitbyId(getCurrentUser().getOwnerId());
                List<ResourcePrivilege> resourcePrivilege = this.resourcePrivilegeService.findPrivilege("logistics/saleOrderReturn", this.getCurrentUser().getRoleId());
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
                mv.addObject("resourcePrivilege", FastJSONUtil.getJSONString(resourcePrivilege));

                mv.addObject("defaultSaleStaffId", defaultSaleStaffId);
                mv.addObject("ownersId", unit.getOwnerids());
                mv.addObject("pageType", "add");
                mv.addObject("ownersId", unit.getOwnerids());
                mv.addObject("userId", getCurrentUser().getId());
                mv.addObject("cargoTracking", "cargoTracking");
                mv.addObject("cTbillNo", billNo);
                return mv;
            }
            case "CM": {
                ModelAndView mv = new ModelAndView("/views/logistics/consignmentBill");
                List<ResourcePrivilege> resourcePrivilege = this.resourcePrivilegeService.findPrivilege("logistics/Consignment", this.getCurrentUser().getRoleId());
                mv.addObject("resourcePrivilege", FastJSONUtil.getJSONString(resourcePrivilege));
                mv.addObject("ownerId", getCurrentUser().getOwnerId());
                mv.addObject("userId", getCurrentUser().getId());
                Unit unit = CacheManager.getUnitById(getCurrentUser().getOwnerId());
                mv.addObject("ownersId", unit.getOwnerids());
                mv.addObject("pageType","add");
                String defaultWarehId = unit.getDefaultWarehId();
                String defaultSaleStaffId = unit.getDefaultSaleStaffId();
                String defalutCustomerId = unit.getDefalutCustomerId();
                if (CommonUtil.isNotBlank(defalutCustomerId)) {
                    Customer customer = CacheManager.getCustomerById(defalutCustomerId);
                    mv.addObject("defaultWarehId", defaultWarehId);
                    mv.addObject("defalutCustomerId", defalutCustomerId);
                    mv.addObject("defalutCustomerName", customer.getName());
                    mv.addObject("defalutCustomerdiscount", customer.getDiscount());
                    mv.addObject("defalutCustomercustomerType", unit.getType());
                    mv.addObject("defalutCustomerowingValue", customer.getOwingValue());
                }
                mv.addObject("defaultSaleStaffId", defaultSaleStaffId);
                mv.addObject("cargoTracking", "cargoTracking");
                mv.addObject("cTbillNo", billNo);
                return mv;
            }
        }
        return new ModelAndView();
    }
}
