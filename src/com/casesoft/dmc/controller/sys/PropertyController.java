package com.casesoft.dmc.controller.sys;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter; 
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.cfg.PropertyType;
import com.casesoft.dmc.model.product.Size;
import com.casesoft.dmc.service.cfg.PropertyKeyService;
import com.casesoft.dmc.service.cfg.PropertyService;


@Controller
@RequestMapping("/sys/property")
public class PropertyController extends BaseController implements IBaseInfoController<PropertyType>{

	@Autowired
	private PropertyKeyService propertyKeyService;
	@Autowired
	private PropertyService propertyService;
	
	
	@Override
	@RequestMapping(value = "/index")
	public String index() {
		return "/views/sys/property";
	}

	
	@Override
	@RequestMapping(value="/page")
    @ResponseBody
	public Page<PropertyType> findPage(Page<PropertyType> page)
			throws Exception {
		this.logAllRequestParams();
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.propertyService.findPage(page,filters);
        return page;
	}

	@RequestMapping(value="/searchByType")
	@ResponseBody
	public List<PropertyKey> searchByType(String type) throws Exception {
		this.logAllRequestParams();
		List<PropertyKey> pkList = this.propertyService.getPropertyKeyByType(type);
		return pkList;
	}

	
	@Override
	public List<PropertyType> list() throws Exception {

		return null;
	}
    @Override
	@RequestMapping(value="/save")
    @ResponseBody
	public MessageBox save(PropertyType entity) throws Exception {
		try{
			PropertyType propertyTypebyid = this.propertyService.findPropertyTypebyid(entity.getId());
			if(CommonUtil.isBlank(propertyTypebyid)){
				Integer num = this.propertyService.findtypeNum(entity.getType());
				entity.setSeqNo((num+1));
				entity.setBrand("*");
				entity.setIsUse("Y");
				entity.setKeyId(entity.getId());
				this.propertyService.save(entity);
			}else{
				propertyTypebyid.setType(entity.getType());
				propertyTypebyid.setValue(entity.getValue());
				this.propertyService.save(propertyTypebyid);
			}
			CacheManager.refreshPropertyTypeCache();
			return returnSuccessInfo("保存成功");
        }catch(Exception e){
			e.printStackTrace();
            return returnFailInfo("保存失败");
        }
	}
	/**
	 * 保存属性信息
	 * */
	@RequestMapping(value="/saveproperty")
	@ResponseBody
	public MessageBox saveproperty(PropertyKey entity) throws Exception {
		try{
			PropertyKey propertyKeybyid = CacheManager.getPropertyKey(entity.getId());
			if(CommonUtil.isBlank(propertyKeybyid)){
				PropertyKey propertyKey = this.propertyService.findPropertyKeyByNameAndType(entity.getName(),entity.getType());
				if(CommonUtil.isBlank(propertyKey)){
					Integer num = this.propertyService.findtkeyNum(entity.getType());
					entity.setSeqNo((num+1));
					User currentUser = getCurrentUser();
					entity.setCode(entity.getSeqNo()+"");
					entity.setType(entity.getId());
					entity.setId(entity.getId()+"-"+entity.getCode());
					entity.setOwnerId(currentUser.getCreatorId());
					entity.setLocked(0);
					entity.setRegisterDate(new Date());
					entity.setRegisterId(currentUser.getId());
					entity.setYnuse("Y");
					this.propertyService.saveKey(entity);
				}else{
					return returnFailInfo("保存失败,名称已存在不能重复添加");
				}

			}else{
				propertyKeybyid.setName(entity.getName());
				this.propertyService.saveKey(propertyKeybyid);
			}
			CacheManager.refreshPropertyCache();
			return returnSuccessInfo("保存成功",entity);
		}catch(Exception e){
			e.printStackTrace();
			return returnFailInfo("保存失败");
		}
	}
	@RequestMapping(value = "checkCode")
	@ResponseBody
	public Map<String,Boolean> checkCode(String id, String pageType){
		PropertyType propertyTypebyid =this.propertyService.findPropertyTypebyid(id);
		Map<String,Boolean> json =new HashMap<String,Boolean>();
		if("add".equals(pageType)&&CommonUtil.isNotBlank(propertyTypebyid)){
			Boolean valid = json.put("valid", false);
		} else {
			json.put("valid",true);
		}
		return json;

	}
	@RequestMapping(value = "checkCodetype")
	@ResponseBody
	public Map<String,Boolean> checkCodetype(String code,String type, String pageType){
		PropertyKey propertyKeybyid =this.propertyService.findPropertyKeybyid(type+"-"+code);
		Map<String,Boolean> json =new HashMap<String,Boolean>();
		if("add".equals(pageType)&&CommonUtil.isNotBlank(propertyKeybyid)){
			Boolean valid = json.put("valid", false);
		} else {
			json.put("valid",true);
		}
		return json;

	}

	@RequestMapping("/changetypeStatus")
	@ResponseBody
	public MessageBox changeStatus(String id, String status) {
		this.logAllRequestParams();
		PropertyType propertyTypebyid =this.propertyService.findPropertyTypebyid(id);
		propertyTypebyid.setIsUse(status);
		try {
			this.propertyService.save(propertyTypebyid);
			return returnSuccessInfo("更改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return returnFailInfo("更改失败");
		}
	}

	@RequestMapping("/changepropertyStatus")
	@ResponseBody
	public MessageBox changepropertyStatus(String id, String status) {
		this.logAllRequestParams();
		PropertyKey propertyKeybyid = this.propertyService.findPropertyKeybyid(id);
		propertyKeybyid.setYnuse(status);
		try {
			this.propertyService.saveKey(propertyKeybyid);
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
	@RequestMapping("/findclassname")
	@ResponseBody
	public Page<PropertyKey> findclassname(Page<PropertyKey> page) throws Exception {
		this.logAllRequestParams();
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
				.getRequest());
		page.setPageProperty();
		page = this.propertyKeyService.findPage(page,filters);
		return page;
	}


	
	
	
	
	
}
