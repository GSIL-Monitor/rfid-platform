package com.casesoft.dmc.controller.sys;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.model.cfg.PropertyKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.sys.impl.VendorService;

@Controller
@RequestMapping("/sys/vendor")
public class VendorController extends BaseController implements IBaseInfoController<Unit>{
	
	@Autowired
	private VendorService vendorService;
	@Override
	@RequestMapping(value = "/index")
	public String index() {		
		 return "/views/sys/vendor";
	}
	
    @RequestMapping(value="/page")
	@ResponseBody
	@Override
	public Page<Unit> findPage(Page<Unit> page) throws Exception {
		this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.vendorService.findPage(page,filters);
        Map<String,String> propertyKeyMap=new HashMap<String,String>();
		for(PropertyKey propertyKey:CacheManager.getPropertyKeyMap().values()){
			if("VT".equals(propertyKey.getType())){
				propertyKeyMap.put(propertyKey.getCode(),propertyKey.getName());
			}
		}
        //加入所属方的Name
        for(Unit u: page.getRows()){
        		String ownerName=CacheManager.getUnitById(u.getOwnerId()).getName();
                u.setUnitName(ownerName);
                if(CommonUtil.isNotBlank(propertyKeyMap.get(u.getGroupId()))){
                	u.setGroupName(propertyKeyMap.get(u.getGroupId()));
				}
        }
        return page;
	}

	@RequestMapping(value = "/checkCode")
	@ResponseBody
	public Map<String,Boolean> checkCode(String code,String pageType){
		Unit vendor =this.vendorService.findUniqueByCode(code);
		Map<String,Boolean> json = new HashMap<String,Boolean>();
		if("add".equals(pageType)&&CommonUtil.isNotBlank(vendor)){
			json.put("valid",false);
		} else {
			json.put("valid",true);
		}
		return json;
	}

	@RequestMapping(value="/search")
	@ResponseBody   
	@Override
	public List<Unit> list() throws Exception {
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
				.getRequest());
		List<Unit> vendors = this.vendorService.find(filters);
		return vendors;
	}
	
	@RequestMapping(value="/save")
	@ResponseBody
	@Override
	public MessageBox save(Unit unit) throws Exception {
		this.logAllRequestParams();
       Unit ut=this.vendorService.findById(unit.getId());
        if(CommonUtil.isBlank(ut)) {
        	ut=new Unit();
        	if(CommonUtil.isBlank(unit.getCode())){
				String code = this.vendorService.findMaxCode(Constant.UnitType.Vender);
				ut.setCode(code);
			} else {
        		ut.setCode(unit.getCode());
			}
            ut.setId(ut.getCode());
            ut.setCreatorId(this.getCurrentUser().getCode());
            ut.setCreateTime(new Date());
        }
        ut.setName(unit.getName());
        ut.setType(Constant.UnitType.Vender);
        ut.setUpdateTime(new Date());
        ut.setTel(unit.getTel());
        ut.setGroupId(unit.getGroupId());
        ut.setRemark(unit.getRemark());
        ut.setUpdaterId(this.getCurrentUser().getCode());
        ut.setOwnerId(unit.getOwnerId());
        ut.setSrc(Constant.DataSrc.SYS);

        this.vendorService.save(ut);
		CacheManager.refreshUnitCache();
        return this.returnSuccessInfo("保存成功");
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
