package com.casesoft.dmc.controller.sys;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.sys.impl.UnitService;
import com.casesoft.dmc.service.sys.impl.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;



@Controller
@RequestMapping("/sys/warehouse")
public class WarehouseController extends BaseController implements IBaseInfoController<Unit>{

	 @Autowired
	 private WarehouseService warehouseService;
	@Autowired
	private UnitService unitService;
	
	 @Override
	    @RequestMapping(value = "/index")
	    public String index() {
	        return "/views/sys/warehouse";
	    }
	
	@RequestMapping(value="/page")
    @ResponseBody
    @Override
	public Page<Unit> findPage(Page<Unit> page) throws Exception {
		 this.logAllRequestParams();
	        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
	                .getRequest());
	        page.setPageProperty();
	        page = this.warehouseService.findPage(page,filters);
	        for(Unit u:page.getRows()){
				Unit unit = CacheManager.getUnitById(u.getOwnerId());
				if (CommonUtil.isNotBlank(unit)){
					u.setUnitName(CacheManager.getUnitById(u.getOwnerId()).getName());
				}
			}
	        return page;
	}

	@RequestMapping(value = "checkCode")
	@ResponseBody
	public Map<String,Boolean> checkCode(String code, String pageType){
		Unit warehouse =this.warehouseService.findUnitById(code);
		Map<String,Boolean> json =new HashMap<String,Boolean>();
		if("add".equals(pageType)&&CommonUtil.isNotBlank(warehouse)){
			json.put("valid",false);
		} else {
			json.put("valid",true);
		}
		return json;

	}

    @RequestMapping(value = "/list")
    @ResponseBody
	@Override
	public List<Unit> list() throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
		List<Unit> warehouse=new ArrayList<>();
		User currentUser = this.getCurrentUser();
		Unit unitById = CacheManager.getUnitById(currentUser.getOwnerId());
		if(CommonUtil.isBlank(unitById)){
			unitById=this.unitService.getunitbyId(currentUser.getOwnerId());
		}
		if(filters.size() >0){
			if(CommonUtil.isNotBlank(unitById.getGroupId())){
				if(unitById.getGroupId().equals("JMS")){
					PropertyFilter filter = new PropertyFilter("EQS_ownerId", unitById.getId());
					filters.add(filter);
					warehouse.addAll(this.warehouseService.find(filters));
				}else{
					warehouse.addAll(this.warehouseService.find(filters));
				}
			}else{
				if(this.warehouseService.find(filters) != null){
					warehouse.addAll(this.warehouseService.find(filters));
				}
			}
		}
		return warehouse;
	}



	@Override
	@RequestMapping(value="/save")
	@ResponseBody
	public MessageBox save(Unit entity) throws Exception {
	 	Unit unit =this.warehouseService.findUnitById(entity.getCode());
	 	if(CommonUtil.isBlank(unit)){
			unit =new Unit();
			if(CommonUtil.isBlank(entity.getCode())){
				String code=this.warehouseService.getMaxCode();
				unit.setCode(code);
			} else{
				unit.setCode(entity.getCode());
			}
			unit.setId(unit.getCode());
			unit.setCreatorId(this.getCurrentUser().getOwnerId());
			unit.setCreateTime(new Date());
			unit.setType(9);
			unit.setSrc("01");
			unit.setLocked(0);
		}
			unit.setName(entity.getName());
	 		unit.setGroupId(entity.getGroupId());
	 		unit.setOwnerId(entity.getOwnerId());
	 		unit.setTel(entity.getTel());
	 		unit.setLinkman(entity.getLinkman());
	 		unit.setEmail(entity.getEmail());
	 		unit.setProvinceId(entity.getProvinceId());
	 		unit.setCityId(entity.getCityId());
	 		unit.setAddress(entity.getAddress());
	 		unit.setRemark(entity.getRemark());
		try{
			this.warehouseService.save(unit);
			List<Unit> unitList = new ArrayList<>();
			unitList.add(unit);
			CacheManager.refreshUnitCache(unitList);
			return returnSuccessInfo("保存成功");
		}catch(Exception e){
			return returnFailInfo("保存失败");
		}
	}

	@RequestMapping(value="/setDefaultWareh")
	@ResponseBody
	public MessageBox setDefaultWareh(String warehId,String ownerId) throws Exception {
		Unit unit = CacheManager.getUnitByCode(ownerId);
		unit.setDefaultWarehId(warehId);
		this.warehouseService.save(unit);
		List<Unit> unitList = new ArrayList<>();
		unitList.add(unit);
		CacheManager.refreshUnitCache(unitList);
		return returnSuccessInfo("设置默认仓库成功");
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
