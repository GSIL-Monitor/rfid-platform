package com.casesoft.dmc.controller.hall;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.hall.Department;
import com.casesoft.dmc.service.hall.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/3/10 0010.
 */

@Controller
@RequestMapping(value="/hall/department")
public class DepartmentController extends BaseController implements IBaseInfoController<Department>{
	@Autowired
	private DepartmentService departmentService;

	@RequestMapping("/page")
	@ResponseBody
	@Override
	public Page<Department> findPage(Page<Department> page) throws Exception {
		this.logAllRequestParams();
		List<PropertyFilter> filters =PropertyFilter.buildFromHttpRequest(this.getRequest());
		page.setPageProperty();
		page=this.departmentService.findPage(page,filters);

		return page;
	}

	@RequestMapping(value="/list")
	@ResponseBody
	@Override
	public List<Department> list() throws Exception {
		this.logAllRequestParams();
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
				.getRequest());
		List<Department> list =this.departmentService.find(filters);
		return list;
	}

	@RequestMapping(value="save")
	@ResponseBody
	@Override
	public MessageBox save(Department department) throws Exception {
		Department depart =this.departmentService.findByCode(department.getCode());
		if(CommonUtil.isBlank(depart)){
			depart=new Department();
			String code=this.departmentService.findMaxCode();
			depart.setCode(code);
			depart.setId(code);
			depart.setOwnerId("1");
			depart.setCreateTime(new Date());
			depart.setCreator(this.getCurrentUser().getOwnerId());
		}
		depart.setName(department.getName());
		depart.setLinkTel(department.getLinkTel());
		depart.setLinkman(department.getLinkman());
		depart.setEmail(department.getEmail());
		depart.setRemark(department.getRemark());
		depart.setUpdater(this.getCurrentUser().getOwnerId());
		depart.setUpdateTime(new Date());
		try{
			this.departmentService.save(depart);
			/*CacheManager.refreshDepartment();*/
			return returnSuccessInfo("保存成功");
		}catch(Exception e){
			return returnFailInfo("保存失败");
		}

	}

	@Override
	public MessageBox edit(String Id) throws Exception {
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

	@RequestMapping("/index")
	@Override
	public String index() {
		return "/views/hall/departmentInfo";

	}
}
