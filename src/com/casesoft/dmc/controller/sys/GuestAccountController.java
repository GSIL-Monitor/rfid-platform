package com.casesoft.dmc.controller.sys;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.PaymentGatheringBill;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.sys.GuestView;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.cfg.PropertyService;
import com.casesoft.dmc.service.logistics.PaymentGatheringBillService;
import com.casesoft.dmc.service.sys.GuestViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yushen on 2017/7/12.
 */
@Controller
@RequestMapping("/sys/guestAccount")
public class GuestAccountController extends BaseController implements IBaseInfoController<GuestView> {

    @Autowired
    private GuestViewService guestViewService;

    @Autowired
    private PaymentGatheringBillService paymentGatheringBillService;
    @Autowired
    private PropertyService propertyService;

    //    @RequestMapping("/index")
    @Override
    public String index() {
        return "/views/sys/guestAccount";
    }

    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {
        ModelAndView mv = new ModelAndView("/views/sys/guestAccount");
        mv.addObject("userId", getCurrentUser().getId());
        return mv;
    }


    @RequestMapping("/page")
    @ResponseBody
    public Page<GuestView> findPage(Page<GuestView> page, String userId) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        User currentUser = CacheManager.getUserById(userId);
        String ownerId = currentUser.getOwnerId();
        String roleId = currentUser.getRoleId();
        if (roleId.equals("JMSJS")) {
            PropertyFilter filter = new PropertyFilter("EQS_ownerId", ownerId);
            filters.add(filter);
        }
        page.setPageProperty();
        page = this.guestViewService.findPage(page, filters);

        for (GuestView guest : page.getRows()) {
            addUnitTypeName(guest);
        }
        return page;
    }

    @Override
    public Page<GuestView> findPage(Page<GuestView> page) throws Exception {
        return null;
    }

    @Override
    public List<GuestView> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(GuestView entity) throws Exception {
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

    @RequestMapping("/viewStatement")
    @ResponseBody
    public ModelAndView viewStatement(String guestId, String userId) {
        GuestView guest = this.guestViewService.get("id", guestId);

        String yearMonth = yearMonth();
        addUnitTypeName(guest);

        ModelAndView mv = new ModelAndView("/views/sys/guestAccountStatement");
        mv.addObject("mainUrl", "/sys/guestAccount/index.do");
        mv.addObject("guest", guest);
        mv.addObject("userId", userId);
        mv.addObject("masId", guest.getId() + "-" + yearMonth);
        PropertyKey propertyKey = propertyService.getDefaultPayType();
        mv.addObject("payType", propertyKey.getIconCode());
        return mv;
    }

    //通过UnitType设置unitTypeName
    private void addUnitTypeName(GuestView guest) {
        switch (guest.getUnitType()) {
            case BillConstant.customerType.Agent:
                guest.setUnitTypeName("省代客户");
                break;
            case BillConstant.customerType.Customer:
                guest.setUnitTypeName("零售客户");
                break;
            case BillConstant.customerType.Shop:
                guest.setUnitTypeName("门店客户");
                break;
        }
    }

    @RequestMapping("/showDetailPage")
    @ResponseBody
    public ModelAndView showDetail(String userId) {
        ModelAndView mv = new ModelAndView("/views/sys/guestAccountDetails");
        mv.addObject("mainUrl", "/sys/guestAccount/index.do");
        mv.addObject("userId", userId);
        return mv;
    }

    @RequestMapping("/findDetails")
    @ResponseBody
    public Page<PaymentGatheringBill> findDetails(Page<PaymentGatheringBill> page, String userId) {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());

        User currentUser = CacheManager.getUserById(userId);
        String ownerId = currentUser.getOwnerId();
        String roleId = currentUser.getRoleId();
        if (roleId.equals("JMSJS")) {
            PropertyFilter filter = new PropertyFilter("EQS_ownerId", ownerId);
            filters.add(filter);
        }

        page.setPageProperty();
        Page<PaymentGatheringBill> billPage = this.paymentGatheringBillService.findPage(page, filters);
        for (PaymentGatheringBill bill : billPage.getRows()) {
            if (CommonUtil.isNotBlank(bill.getCustomsId())) {
                Customer customer = CacheManager.getCustomerById(bill.getCustomsId());
                Unit unit = CacheManager.getUnitById(bill.getCustomsId());
                if (CommonUtil.isNotBlank(customer)) {
                    bill.setCustomsName(customer.getName());
                } else if (CommonUtil.isNotBlank(unit)) {
                    bill.setCustomsName(unit.getName());
                } else {
                    bill.setCustomsName("");
                }
            }
            if (CommonUtil.isNotBlank(bill.getVendorId())) {
                Unit unit = CacheManager.getUnitById(bill.getVendorId());
                if (CommonUtil.isNotBlank(unit)) {
                    bill.setVendorName(unit.getName());
                } else {
                    bill.setVendorName("");
                }
            }
        }
        return billPage;
    }

    @RequestMapping("/initSumInfo")
    @ResponseBody
    public MessageBox initSumInfo(String unitType, String nameOrTel, String userId) {

        User currentUser = CacheManager.getUserById(userId);
        String ownerId = currentUser.getOwnerId();
        String roleId = currentUser.getRoleId();

        Map<String, String> sumInfoMap;
        String guestNum;
        if (roleId.equals("JMSJS")) {
            sumInfoMap = this.guestViewService.countInfoByCondition(unitType, nameOrTel, ownerId);
            guestNum = this.guestViewService.getGuestNum(unitType, nameOrTel, ownerId);
        } else {
            sumInfoMap = this.guestViewService.countInfoByCondition(unitType, nameOrTel);
            guestNum = this.guestViewService.getGuestNum(unitType, nameOrTel);
        }
        sumInfoMap.put("totGuestNum", guestNum);
        return new MessageBox(true, "", sumInfoMap);
    }

    public String yearMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String yearMonth;
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        if (month == Calendar.JANUARY) {
            yearMonth = (year - 1) + "-" + "12";
        } else if (month < 10) {
            yearMonth = year + "-" + "0" + month;
        }else {
            yearMonth = year + "-" + month;
        }
        return yearMonth;
    }
}
