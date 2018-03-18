package com.casesoft.dmc.controller.sys;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.sys.GuestView;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.shop.CustomerService;
import com.casesoft.dmc.service.sys.GuestService;
import com.casesoft.dmc.service.sys.GuestViewService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import java.util.*;

/**
 * Created by Session on 2017-06-20.
 */

@Controller
@RequestMapping(value = "/sys/guest")
public class GuestController extends BaseController implements IBaseInfoController<Unit> {

    @Autowired
    private GuestService guestService;

    @Autowired
    private GuestViewService guestViewService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UnitService unitService;

    //@RequestMapping(value = "/index")
    @Override
    public String index() {
        return "views/sys/guest";
    }
    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {
        ModelAndView mv = new ModelAndView("views/sys/guest");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        Unit unit = CacheManager.getUnitByCode(getCurrentUser().getOwnerId());
        mv.addObject("unitOwnerId",unit.getOwnerId());
        mv.addObject("userId", getCurrentUser().getId());
        return mv;
    }

    @RequestMapping("/page")
    @ResponseBody
    public Page<GuestView> findPageView(Page<GuestView> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        page.setPageProperty();
        String ownerId = getCurrentUser().getOwnerId();
        String roleId = getCurrentUser().getRoleId();
        if (roleId.equals("JMSJS")) {
            PropertyFilter filter = new PropertyFilter("EQS_ownerId", ownerId);
            filters.add(filter);
        }
        page = this.guestViewService.findPage(page, filters);
        if (page.getRows().size() > 0) {
            for (GuestView g : page.getRows()) {
                if (CommonUtil.isNotBlank(CacheManager.getUserById(g.getUpdaterId()))) {
                    g.setUpdaterName(CacheManager.getUserById(g.getUpdaterId()).getName());
                }
                if (CommonUtil.isNotBlank(CacheManager.getUnitById(g.getOwnerId()))) {
                    g.setUnitName(CacheManager.getUnitById(g.getOwnerId()).getName());
                }
            }
        }
        return page;
    }

    @Override
    public Page<Unit> findPage(Page<Unit> page) throws Exception {
        return null;
    }

    @Override
    public List<Unit> list() throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        List<Unit> guestList = this.guestService.find(filters);
        return guestList;
    }

    @RequestMapping("/listGuest")
    @ResponseBody
    public List<GuestView> listGuest() throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        List<GuestView> guestList = this.guestViewService.find(filters);
        return guestList;
    }

    @RequestMapping("/changeStatus")
    @ResponseBody
    public MessageBox changeStatus(String id, Integer status, String type) {
        this.logAllRequestParams();
        if (!"CT-LS".equals(type)) {
            Unit gst = this.guestService.findById(id);
            gst.setStatus(status == 1 ? 0 : 1);
            try {
                this.guestService.save(gst);
                return returnSuccessInfo("更改成功");
            } catch (Exception e) {
                e.printStackTrace();
                return returnFailInfo("更改失败");
            }
        } else {
            Customer gst = this.customerService.getCustomerById(id);
            gst.setStatus(status == 1 ? 0 : 1);
            try {
                this.customerService.save(gst);
                return returnSuccessInfo("更改成功");
            } catch (Exception e) {
                e.printStackTrace();
                return returnFailInfo("更改失败");
            }
        }
    }
    /**
     * 设置店铺默认客户
     * @param id 客户id
     * @param userId 登陆I账号用户Id
     * @author Alvin 2018-3-13
     */
    @RequestMapping("/setDefaultGuest")
    @ResponseBody
    public MessageBox setDefaultGuest(String id,String userId){
        this.logAllRequestParams();
        try {
            Customer customer = CacheManager.getCustomerById(id);
            if (CommonUtil.isNotBlank(customer)) {
                Unit shop = this.unitService.load(customer.getOwnerId());
                if (CommonUtil.isNotBlank(shop)) {
                    int updateRecord = this.unitService.setDefaultCustomer(shop.getId(), id, userId);
                    if (updateRecord == 1) {
                        return new MessageBox(true, "设置默认客户成功");
                    } else {
                        return new MessageBox(false, "设置失败,更新门店信息失败");
                    }
                } else {
                    return new MessageBox(false, "设置失败,更新门店信息失败");
                }
            }else{
                return new MessageBox(false, "设置失败,客户信息错误");
            }
        }catch (Exception e){
            return new MessageBox(false,"设置失败服务器异常"+ e.getMessage());
        }
    }


    @RequestMapping(value = "/add")
    @ResponseBody
    public ModelAndView add() {
        ModelAndView mav = new ModelAndView("/views/sys/guest_edit");
        mav.addObject("pageType", "add");
        mav.addObject("ownerId", getCurrentUser().getOwnerId());
        return mav;
    }

    @RequestMapping(value = "/edit")
    @ResponseBody
    public ModelAndView editGuest(String id, String unitType) {
        ModelAndView mav = new ModelAndView("/views/sys/guest_edit");
        mav.addObject("pageType", "edit");
        mav.addObject("ownerId", getCurrentUser().getOwnerId());
        if (!"CT-LS".equals(unitType)) {
            Unit gst = CacheManager.getUnitByCode(id);
            if (CommonUtil.isNotBlank(CacheManager.getUnitById(gst.getOwnerId()))) {
                gst.setUnitName(CacheManager.getUnitById(gst.getOwnerId()).getName());
            }
            mav.addObject("guest", gst);
        } else {
            Customer gst = CacheManager.getCustomerById(id);
            if (CommonUtil.isNotBlank(CacheManager.getUnitById(gst.getOwnerId()))) {
                gst.setUnitName(CacheManager.getUnitById(gst.getOwnerId()).getName());
            }
            mav.addObject("guest", gst);
        }
        return mav;
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    @Override
    public MessageBox save(Unit entity) throws Exception {
        if (entity.getType() != 6) {
            Unit guest = this.guestService.getGuestById(entity.getId());
            if (CommonUtil.isBlank(guest)) {
                guest = new Unit();
                String newId = this.guestService.findMaxNo(Constant.GuestPrefix.DefaultGuest);
                guest.setId(newId);
                guest.setCode(newId);
                guest.setCreateTime(new Date());
                guest.setCreatorId(this.getCurrentUser().getId());
                guest.setStoredValue(0.0);
                guest.setOwingValue(0.0);
            }
            guest.setName(entity.getName());
            guest.setStatus(entity.getStatus());
            guest.setSex(entity.getSex());
            guest.setType(entity.getType());
            guest.setLinkman(entity.getLinkman());
            guest.setTel(entity.getTel());
            guest.setEmail(entity.getEmail());
            guest.setBankCode(entity.getBankCode());//变更为bankCode
            guest.setBankAccount(entity.getBankAccount());//变更为BankAccount
            guest.setDepositBank(entity.getDepositBank());
            guest.setBirth(entity.getBirth());
            guest.setPhone(entity.getPhone());
            guest.setOwnerId(entity.getOwnerId());
            Unit unit = CacheManager.getUnitById(entity.getOwnerId());
            if (CommonUtil.isNotBlank(unit)) {
                guest.setAreasId(unit.getAreasId());
                guest.setOwnerids(unit.getOwnerids());
            }

            guest.setFax(entity.getFax());
            guest.setDiscount(entity.getDiscount());
            guest.setAddress(entity.getAddress());
            guest.setVipMessage(entity.getVipMessage());
            guest.setProvince(entity.getProvince());
            guest.setCity(entity.getCity());
            guest.setAreaId(entity.getAreaId());//原为County
            guest.setRemark(entity.getRemark());
            guest.setUpdaterId(this.getCurrentUser().getId());
            guest.setUpdateTime(new Date());
        /*	guest.setStoredValue(entity.getStoredValue());
            guest.setStoreDate(entity.getStoreDate());*/
            try {
                this.guestService.save(guest);
                CacheManager.refreshUnitCache();
                return returnSuccessInfo("保存成功");
            } catch (Exception e) {
                e.printStackTrace();
                return returnFailInfo("保存失败");
            }
        } else {
            Customer guest = this.customerService.getById(entity.getId());
            if (CommonUtil.isBlank(guest)) {
                guest = new Customer();
                String maxNo = this.customerService.getMaxNo(Constant.ScmConstant.CodePrefix.Customer);
                guest.setId(maxNo);
                guest.setCode(maxNo);
                guest.setCreateTime(new Date());
                guest.setCreatorId(getCurrentUser().getId());
                guest.setStoredValue(0.0);
                guest.setOwingValue(0.0);
            }
            guest.setName(entity.getName());
            guest.setVipMessage(entity.getVipMessage());
            guest.setStatus(entity.getStatus());
            guest.setSex(entity.getSex());
            guest.setType(entity.getType());
            guest.setLinkman(entity.getLinkman());
            guest.setTel(entity.getTel());
            guest.setEmail(entity.getEmail());
            guest.setBankCode(entity.getBankCode());
            guest.setBankAccount(entity.getBankAccount());
            guest.setDepositBank(entity.getDepositBank());
            guest.setBirth(entity.getBirth());
            guest.setPhone(entity.getPhone());
            guest.setOwnerId(entity.getOwnerId());
            Unit unit = CacheManager.getUnitById(entity.getOwnerId());
            if (CommonUtil.isNotBlank(unit)) {
                guest.setAreasId(unit.getAreasId());
                guest.setOwnerids(unit.getOwnerids());
            }
            guest.setFax(entity.getFax());
            guest.setDiscount(entity.getDiscount());
            guest.setAddress(entity.getAddress());
            guest.setProvince(entity.getProvince());
            guest.setCity(entity.getCity());
            guest.setAreaId(entity.getAreaId());//原为County
            guest.setRemark(entity.getRemark());
            guest.setUpdaterId(this.getCurrentUser().getId());
            guest.setUpdateTime(new Date());
		/*	guest.setStoredValue(entity.getStoredValue());
			guest.setStoreDate(entity.getStoreDate());*/

            try {
                this.customerService.save(guest);
                CacheManager.refreshUnitCache();
                CacheManager.refreshCustomer();
                return returnSuccessInfo("保存成功");
            } catch (Exception e) {
                e.printStackTrace();
                return returnFailInfo("保存失败");
            }
        }
    }

    @RequestMapping(value = "/update")
    @ResponseBody
    public MessageBox update(Unit entity) throws Exception {
        String preType = this.getReqParam("preType");
        Boolean deletePre = false;
        if (!preType.equals(entity.getType().toString())) {
           deletePre = true;
        }
        if (entity.getType().equals(Constant.UnitType.Shop) || entity.getType().equals(Constant.UnitType.Agent)) {
            try {
                Unit guest = CacheManager.getUnitById(entity.getId());
                if(CommonUtil.isBlank(guest)){
                    Customer preCustomer = CacheManager.getCustomerById(entity.getId());
                    guest = new Unit();
                    guest.setId(entity.getId());
                    guest.setCode(entity.getId());
                    guest.setOwnerId(preCustomer.getOwnerId());
                    guest.setOwingValue(preCustomer.getOwingValue());
                    guest.setStoredValue(preCustomer.getStoredValue());
                    guest.setStoreDate(preCustomer.getStoreDate());
                    guest.setCreateTime(preCustomer.getCreateTime());
                    guest.setCreatorId(preCustomer.getCreatorId());
                    guest.setUpdateTime(new Date());
                    guest.setUpdaterId(getCurrentUser().getId());

                }
                guest.setName(entity.getName());
                guest.setStatus(entity.getStatus());
                guest.setSex(entity.getSex());
                guest.setType(entity.getType());
                guest.setLinkman(entity.getLinkman());
                guest.setTel(entity.getTel());
                guest.setEmail(entity.getEmail());
                guest.setBankCode(entity.getBankCode());//变更为bankCode
                guest.setBankAccount(entity.getBankAccount());//变更为BankAccount
                guest.setDepositBank(entity.getDepositBank());
                guest.setBirth(entity.getBirth());
                guest.setPhone(entity.getPhone());
                guest.setOwnerId(entity.getOwnerId());
                Unit unit = CacheManager.getUnitById(entity.getOwnerId());
                if (CommonUtil.isNotBlank(unit)) {
                    guest.setAreasId(unit.getAreasId());
                    guest.setOwnerids(unit.getOwnerids());
                }
                guest.setFax(entity.getFax());
                guest.setDiscount(entity.getDiscount());
                guest.setAddress(entity.getAddress());
                guest.setVipMessage(entity.getVipMessage());
                guest.setProvince(entity.getProvince());
                guest.setCity(entity.getCity());
                guest.setAreaId(entity.getAreaId());//原为County
                guest.setRemark(entity.getRemark());
                guest.setUpdaterId(this.getCurrentUser().getId());
                guest.setUpdateTime(new Date());
                if(deletePre){
                    this.guestService.updateUnit(guest);
                    CacheManager.refreshCustomer();
                }else{
                    this.guestService.save(guest);

                }
                CacheManager.refreshUnitCache();
                return returnSuccessInfo("保存成功");
            } catch (Exception e) {
                e.printStackTrace();
                return returnFailInfo("保存失败");
            }
        } else {
            try {
                Customer guest = CacheManager.getCustomerById(entity.getId());
                if(CommonUtil.isBlank(guest)){
                    Unit preUnit = CacheManager.getUnitById(entity.getId());
                    guest = new Customer();
                    guest.setId(preUnit.getId());
                    guest.setCode(preUnit.getId());
                    guest.setOwnerId(preUnit.getOwnerId());
                    guest.setOwingValue(preUnit.getOwingValue());
                    guest.setStoredValue(preUnit.getStoredValue());
                    guest.setStoreDate(preUnit.getStoreDate());
                    guest.setCreateTime(preUnit.getCreateTime());
                    guest.setCreatorId(preUnit.getCreatorId());
                    guest.setUpdateTime(new Date());
                    guest.setUpdaterId(getCurrentUser().getId());

                }
                guest.setName(entity.getName());
                guest.setVipMessage(entity.getVipMessage());
                guest.setStatus(entity.getStatus());
                guest.setSex(entity.getSex());
                guest.setType(entity.getType());
                guest.setLinkman(entity.getLinkman());
                guest.setTel(entity.getTel());
                guest.setEmail(entity.getEmail());
                guest.setBankCode(entity.getBankCode());
                guest.setBankAccount(entity.getBankAccount());
                guest.setDepositBank(entity.getDepositBank());
                guest.setBirth(entity.getBirth());
                guest.setPhone(entity.getPhone());
                guest.setOwnerId(entity.getOwnerId());
                Unit unit = CacheManager.getUnitById(entity.getOwnerId());
                if (CommonUtil.isNotBlank(unit)) {
                    guest.setAreasId(unit.getAreasId());
                    guest.setOwnerids(unit.getOwnerids());
                }
                guest.setFax(entity.getFax());
                guest.setDiscount(entity.getDiscount());
                guest.setAddress(entity.getAddress());
                guest.setProvince(entity.getProvince());
                guest.setCity(entity.getCity());
                guest.setAreaId(entity.getAreaId());//原为County
                guest.setRemark(entity.getRemark());
                guest.setUpdaterId(this.getCurrentUser().getId());
                guest.setUpdateTime(new Date());
                if(deletePre){
                    this.guestService.updateCustomer(guest);
                }else {
                    this.customerService.save(guest);
                }
                CacheManager.refreshUnitCache();
                CacheManager.refreshCustomer();
                return returnSuccessInfo("保存成功");
            } catch (Exception e) {
                e.printStackTrace();
                return returnFailInfo("保存失败");
            }

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
}
