package com.casesoft.dmc.controller.sys;

import java.util.Collections;
import java.util.List;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.sys.Resource;
import com.casesoft.dmc.model.sys.RoleRes;
import com.casesoft.dmc.service.sys.impl.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.sys.impl.CompanyService;

@Controller
@RequestMapping("/sys/company")
public class CompanyController extends BaseController implements IBaseInfoController<Unit>{

	@Autowired
	private CompanyService companyService;
	@Autowired
	private ResourceService resourceService;
	 
	@Override
    @RequestMapping(value = "/index")
    public String index() {
        return "/views/sys/company";
    }
	@RequestMapping(value="/page")
    @ResponseBody
	@Override
	public Page<Unit> findPage(Page<Unit> page) throws Exception {
		    this.logAllRequestParams();
	        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
	                .getRequest());
	        page.setPageProperty();
	        page = this.companyService.findPage(page,filters);
	        
	        return page;
	}
	@RequestMapping(value="/findAllModule")
	@ResponseBody
	public List<Resource> findAllModule() {
		List<Resource> resourceList  = this.resourceService.getResourceList();
		RoleUtil.convertToTreeGrid(resourceList);
		Collections.sort(resourceList, new MenuIdComparator());//对子菜单进行排序
		return resourceList;
	}

	@RequestMapping(value="/selectModule")
	@ResponseBody
    public MessageBox selectModule(String resId) {
		Assert.notNull(resId,"模块编号不能为空");
		Resource resource = this.resourceService.load(resId);
		resource.setStatus(1);
		this.resourceService.save(resource);
		return this.returnSuccessInfo("模块"+resId+"已选择");
	}
	@RequestMapping(value="/cancelModule")
	@ResponseBody
	public MessageBox cancelModul(String resId) {
		Assert.notNull(resId,"模块编号不能为空");
		Resource resource = this.resourceService.load(resId);
		resource.setStatus(0);
		this.resourceService.save(resource);
		return this.returnSuccessInfo("模块"+resId+"已取消");
	}

	@Override
	public List<Unit> list() throws Exception {
		return null;
	}

	@Override
	public MessageBox save(Unit entity) throws Exception {
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
