package com.casesoft.dmc.controller.sys;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.sys.impl.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WingLi on 2016-12-20.
 */
@Controller
@RequestMapping("/sys/shop")
public class ShopController extends BaseController implements IBaseInfoController<Unit>{

    @Autowired
    private ShopService shopService;

    @Override
    @RequestMapping(value = "/index")
    public String index() {
        return "/views/sys/shop";
    }

    @RequestMapping(value="/page")
    @ResponseBody
    @Override
    public Page<Unit> findPage(Page<Unit> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.shopService.findPage(page,filters);
        for(Unit u: page.getRows()){
            if(CommonUtil.isNotBlank(u.getGroupId())){
                if(CommonUtil.isNotBlank(CacheManager.getPropertyKey("ST-B-"+u.getGroupId())))
                    u.setGroupName(CacheManager.getPropertyKey("ST-B-"+u.getGroupId()).getName());
            }
            u.setUnitName(CacheManager.getUnitById(u.getOwnerId()).getName());
        }
        return page;
    }

    @RequestMapping(value="/search")
    @ResponseBody
    @Override
    public List<Unit> list() throws Exception {
    	 List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                 .getRequest());
    	 List<Unit> shop = this.shopService.find(filters);
        return shop;
    }

    @RequestMapping(value = "/checkCode")
    @ResponseBody
    public Map<String,Boolean> checkCode(String code,String pageType){
        code=code.toUpperCase();
        Unit shop =this.shopService.findUnitByCode(code);
        Map<String,Boolean> json =new HashMap<String,Boolean>();
        if("add".equals(pageType)&&CommonUtil.isNotBlank(shop)){
            json.put("valid",false);
        } else {
            json.put("valid",true);
        }
        return json;
    }

    @RequestMapping(value="/save")
    @ResponseBody
    @Override
    public MessageBox save(Unit unit) throws Exception {
        this.logAllRequestParams();
        User sessionUser = this.getCurrentUser();
        Unit u=this.shopService.findUnitByCode(unit.getCode());
        if(CommonUtil.isBlank(u)) {
            u=new Unit();
            String code = this.shopService.findMaxCode(Constant.UnitType.Shop);
            u.setCode(code);
            u.setId(code);//
            u.setCreatorId(sessionUser.getCode());
            u.setCreateTime(new Date());
            u.setType(Constant.UnitType.Shop);
            u.setSrc(Constant.DataSrc.SYS);
            u.setLocked(0);
        }
        u.setName(unit.getName());
        u.setGroupId(unit.getGroupId());
        u.setOwnerId(unit.getOwnerId());
        u.setTel(unit.getTel());
        u.setLinkman(unit.getLinkman());
        u.setEmail(unit.getEmail());
        u.setProvinceId(unit.getProvinceId());
        u.setCityId(unit.getCityId());
        u.setAddress(unit.getAddress());
        u.setRemark(unit.getRemark());
        u.setAreasId(unit.getAreasId());
        u.setOwnerids(unit.getOwnerids());

        try{
            this.shopService.save(u);
            CacheManager.refreshUnitCache();
            return this.returnSuccessInfo("保存成功");
        } catch(Exception e){
            return this.returnFailInfo("保存失败");
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
