package com.casesoft.dmc.controller.shop;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.shop.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

/**
 * Created by WingLi on 2017-01-04.
 */
@Controller
@RequestMapping("/shop/customer")
public class CustomerController extends BaseController implements IBaseInfoController<Customer> {

    @Autowired
    private CustomerService customerService;

    @RequestMapping("/index")
    @Override
    public String index() {
        String ownerId = this.getCurrentUser().getOwnerId();
        Unit unit = CacheManager.getUnitById(ownerId);
        if (unit.getType() != Constant.UnitType.Shop) {
            // return "/views/error/noAuth";
            this.getRequest().setAttribute("ownerId", ownerId);
        } else {
            this.getRequest().setAttribute("ownerId", unit.getOwnerId());//门店所属组织
            this.getRequest().setAttribute("shopId", ownerId);
            this.getRequest().setAttribute("shopName", unit.getName());

        }
        return "/views/shop/customer";
    }

    @RequestMapping("/page")
    @ResponseBody
    @Override
    public Page<Customer> findPage(Page<Customer> page) throws Exception {
        this.logAllRequestParams();//日志
        String ownerId = getCurrentUser().getOwnerId();
        String roleId = getCurrentUser().getRoleId();
        return findPage(page, ownerId, roleId, "desc");
    }

    @RequestMapping("/pageWS")
    @ResponseBody
    public Page<Customer> findPage(Page<Customer> page, String ownerId, String roleId, String order) throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        if ("JMSJS".equals(roleId)) {
            PropertyFilter filter = new PropertyFilter("EQS_ownerId", ownerId);
            filters.add(filter);
        }
        page.setSort("updateTime");
        page.setOrder(order);
        page.setPageProperty();
        page = this.customerService.findPage(page, filters);
        return page;
    }

    @RequestMapping("/list")
    @ResponseBody
    @Override
    public List<Customer> list() throws Exception {
        this.logAllRequestParams();//日志
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        List<Customer> list = this.customerService.find(filters);
        return list;
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    public ModelAndView add() {
        ModelAndView mav = new ModelAndView("/views/shop/customer_edit");
        mav.addObject("mainUrl", "/shop/customer/index.do");
        mav.addObject("pageType", "add");

        return mav;
    }

    @RequestMapping(value = "/edit")
    @ResponseBody
    public ModelAndView editGuest(String id) {
        try {
            ModelAndView mav = new ModelAndView("/views/shop/customer_edit");
            //Unit gst = this.guestService.findById(id);
            Customer load = this.customerService.getCustomerById(id);
            mav.addObject("pageType", "edit");
            mav.addObject("mainUrl", "/shop/customer/index.do");
            mav.addObject("customer", load);
            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/getCustomerByIdWS")
    @ResponseBody
    public Customer getCustomerById(String id) {
        Customer customerById = this.customerService.getCustomerById(id);
        //   通过缓存id取联系人名字
        User findlinkman = CacheManager.getUserById(customerById.getLinkman());
        customerById.setLinkmanName(findlinkman.getName());
        return customerById;
    }

    @RequestMapping(value = "/saveWS")
    @ResponseBody
    public MessageBox saveWS(String customer, String currentUser) throws Exception {
        try {
            Customer entity = JSON.parseObject(customer, Customer.class);
            User user = JSON.parseObject(currentUser, User.class);
            String maxNo = this.customerService.getMaxNo(Constant.ScmConstant.CodePrefix.Customer);
            Customer customerById = this.customerService.getCustomerById(entity.getId());
            if (CommonUtil.isBlank(customerById)) {
                entity.setId(maxNo);
                entity.setCode(maxNo);
                entity.setCreateTime(new Date());
                entity.setCreatorId(user.getCode());
                entity.setOwnerId(user.getOwnerId());
                entity.setStoredValue(0.0);
                entity.setOwingValue(0.0);
                entity.setStatus(1);
                entity.setType(6);
                this.customerService.save(entity);
            }else{
                entity.setUpdaterId(user.getOwnerId());
            }
            return save(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return returnFailInfo("保存失败");
        }

    }


    @RequestMapping(value = "/save")
    @ResponseBody
    @Override
    public MessageBox save(Customer entity) throws Exception {
        String maxNo = this.customerService.getMaxNo(Constant.ScmConstant.CodePrefix.Customer);
        Customer customerById = this.customerService.getCustomerById(entity.getId());
        try {
            if (CommonUtil.isBlank(customerById)) {
                User user = this.getCurrentUser();
                entity.setId(maxNo);
                entity.setCode(maxNo);
                entity.setCreatorId(user.getCode());
                entity.setOwnerId(user.getOwnerId());
                this.customerService.save(entity);
                return returnSuccessInfo("保存成功");
            } else {
               /* entity.setCode(customerById.getCode());
                entity.setId(customerById.getId());*/
                customerById.setBirth(entity.getBirth());
                customerById.setName(entity.getName());
                customerById.setCompany(entity.getCompany());
                customerById.setEmail(entity.getEmail());
                customerById.setSex(entity.getSex());
                customerById.setRemark(entity.getRemark());
                customerById.setJob(entity.getJob());
                customerById.setSocialNo(entity.getSocialNo());
                customerById.setCreateTime(entity.getCreateTime());
                customerById.setStatus(entity.getStatus());
                customerById.setLinkman(entity.getLinkman());
                customerById.setDiscount(entity.getDiscount());
                customerById.setTel(entity.getTel());
                customerById.setIdCard(entity.getIdCard());
                customerById.setUpdateTime(new Date());

                this.customerService.save(customerById);
                return returnSuccessInfo("保存成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return returnFailInfo("保存失败");
        }

    }


    @RequestMapping("/changeStatus")
    @ResponseBody
    public MessageBox changeStatus(String id, Integer status) {
        this.logAllRequestParams();
        Customer customerById = this.customerService.getCustomerById(id);
        customerById.setStatus(status);
        try {
            this.customerService.save(customerById);
            return returnSuccessInfo("更改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return returnFailInfo("更改失败");
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
