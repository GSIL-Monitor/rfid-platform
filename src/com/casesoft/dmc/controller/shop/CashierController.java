package com.casesoft.dmc.controller.shop;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.sys.UserUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.sys.impl.UnitService;
import com.casesoft.dmc.service.sys.impl.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by WingLi on 2017-01-04.
 * user.type=4 是收银员
 */
@Controller
@RequestMapping("/shop/cashier")
public class CashierController extends BaseController implements IBaseInfoController<User> {

    @Autowired
    private UserService userService;

    @Autowired
    private UnitService unitService;

    private PropertyFilter typeFilter = new PropertyFilter("EQI_type", "" + Constant.UserType.Shop_Saler);

    @RequestMapping("/index")
    @Override
    public String index() {
        User curUser = this.getCurrentUser();
        String ownerId = curUser.getOwnerId();
        Unit unit = CacheManager.getUnitById(ownerId);
        this.getRequest().setAttribute("userId",curUser.getId());
        if (unit.getType() != Constant.UnitType.Shop) {
            // return "/views/error/noAuth";
            this.getRequest().setAttribute("ownerId", ownerId);
        } else {
            this.getRequest().setAttribute("ownerId", unit.getOwnerId());//门店所属组织
            this.getRequest().setAttribute("shopId", ownerId);
            this.getRequest().setAttribute("shopName", unit.getName());

        }
        return "/views/shop/cashier";
    }

    @RequestMapping("/page")
    @ResponseBody
    @Override
    public Page<User> findPage(Page<User> page) throws Exception {
        this.logAllRequestParams();//日志
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        filters.add(typeFilter);

        page.setPageProperty();
        page = this.userService.findPage(page, filters);
        UserUtil.convertToVo(page.getRows());
        return page;
    }

    @RequestMapping("/add")
    @ResponseBody
    public ModelAndView addCashier() {
        ModelAndView mav = new ModelAndView("/views/shop/cashier_edit");
        mav.addObject("pageType", "add");
        mav.addObject("id", "");
        return mav;
    }

    @RequestMapping("/edit")
    @ResponseBody
    public ModelAndView editCashier(String id) {
        ModelAndView mav = new ModelAndView("/views/shop/cashier_edit");
        User user = this.userService.getUser(id);
        Unit owner = CacheManager.getUnitByCode(user.getOwnerId());
        user.setUnitName(owner.getName());
        mav.addObject("user", user);
        mav.addObject("pageType", "edit");
        mav.addObject("id", id);
        return mav;
    }

    @RequestMapping("/save")
    @ResponseBody
    public MessageBox saveCashier(User user) {
        try {
            User us = this.userService.getUserByCode(user.getCode());
            if (CommonUtil.isBlank(us)) {

                Date createDate = new Date();
                us = new User();
                us.setId(user.getCode());
                us.setType(new Integer(4));
                us.setRoleId("SH0P01");
                us.setIsAdmin(0);
                us.setCreatorId(this.getCurrentUser().getOwnerId());
                us.setCreateDate(createDate);
                us.setLocked(0);
                us.setCode(user.getCode());
                us.setName(user.getName());
                us.setOwnerId(user.getOwnerId());
                us.setPassword(user.getPassword());
                us.setRemark(user.getRemark());
                us.setPhone(user.getPhone());
            } else {
                us.setName(user.getName());
                us.setOwnerId(user.getOwnerId());
                us.setPassword(user.getPassword());
                us.setRemark(user.getRemark());
                us.setPhone(user.getPhone());
                this.userService.save(us);
            }

            this.userService.save(us);
            CacheManager.refreshUserCache();
            return this.returnSuccessInfo("保存成功");
        } catch (Exception e) {
            return this.returnFailInfo("保存失败");
        }

    }

    /***
     * 设置门店对应默认销售员
     * @param id   销售员id
     * @param userId   当前登录用户
     * @author Alvin 2018-3-14
     * **/
    @RequestMapping("/setDefaultSaleStaff")
    @ResponseBody
    public MessageBox setDefaultSaleStaff(String id, String userId){
        this.logAllRequestParams();
        try {
             User salerStaff = CacheManager.getUserById(id);
             if(CommonUtil.isNotBlank(salerStaff)){
                 int updateRecord = this.unitService.setDefalutSaleStaff(id,salerStaff.getOwnerId(),userId);
                 if(updateRecord == 1){
                     return new MessageBox(true, "设置默认销售员成功");
                 }else{
                     return new MessageBox(false, "设置失败,更新门店信息失败");
                 }
             }else{
                 return new MessageBox(false,"销售员错误");
             }
        }catch (Exception e){
            return new MessageBox(false,"设置失败服务器异常"+e.getMessage());
        }
    }
    @Override
    public List<User> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(User entity) throws Exception {
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
}
