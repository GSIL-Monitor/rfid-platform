package com.casesoft.dmc.controller.hall;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.hall.Employee;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.hall.EmployeeService;
import com.casesoft.dmc.service.sys.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * Created by session on 2017/3/13 0013.
 */

@Controller
@RequestMapping(value="/hall/employee")
public class EmployeeController extends BaseController implements IBaseInfoController<Employee> {


	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private UserService userService;

	@RequestMapping(value="/page")
	@ResponseBody
	@Override
	public Page<Employee> findPage(Page<Employee> page) throws Exception {
		this.logAllRequestParams();
		List<PropertyFilter> filters =PropertyFilter.buildFromHttpRequest(this.getRequest());
		page.setPageProperty();

		page=this.employeeService.findPage(page,filters);

		for(Employee employee:page.getRows()){
			if(CommonUtil.isNotBlank(employee.getOwnerId()))
			employee.setUnitName(CacheManager.getDepartmentByCode(employee.getOwnerId()).getName());
		}
		return page;
	}

	@RequestMapping(value="/list")
	@ResponseBody
	@Override
	public List<Employee> list() throws Exception {
		this.logAllRequestParams();
		List<Employee> employeeList=this.employeeService.findAll();
		if (CommonUtil.isNotBlank(employeeList)){
			for(Employee employee:employeeList){
				if(CommonUtil.isNotBlank(employee.getOwnerId()))
					employee.setUnitName(CacheManager.getDepartmentByCode(employee.getOwnerId()).getName());
			}
		}
		return employeeList;
	}

	@RequestMapping(value = "/save")
	@ResponseBody
	@Override
	public MessageBox save(Employee employee) throws Exception {
		Employee employ =this.employeeService.findByCode(employee.getCode());
		if(CommonUtil.isBlank(employ)){
			String code=this.employeeService.findMaxCode();
			employ=new Employee();
			employ.setCode(code);
			employ.setId(code);
			employ.setCreateTime(new Date());
			employ.setCreator(this.getCurrentUser().getOwnerId());
			employ.setIsUser(0);
		}
		employ.setName(employee.getName());
		employ.setRemark(employee.getRemark());
		employ.setTel(employee.getTel());
		employ.setEmail(employee.getEmail());
		employ.setOwnerId(employee.getOwnerId());
		employ.setUpdater(this.getCurrentUser().getOwnerId());
		employ.setUpdateTime(new Date());

		try{
			this.employeeService.save(employ);
			CacheManager.refreshEmployee();
			return returnSuccessInfo("保存成功");
		}catch(Exception e){
			return returnFailInfo("保存失败");
		}
	}


	@RequestMapping(value = "/setAsUser")
	@ResponseBody
	public MessageBox setAsUser(Employee employee){

		User u =new User();
		if(CommonUtil.isNotBlank(employee)){
			u.setId(employee.getCode());//账号为员工编号、
			u.setCode(employee.getCode());
			u.setLocked(0);
			u.setCreatorId(this.getCurrentUser().getOwnerId());
			u.setCreateDate(new Date());
			u.setType(Constant.UserType.User);
			u.setName(employee.getName());
			u.setEmail(employee.getEmail());
			u.setPassword(employee.getPassword());
			u.setIsAdmin(0);
			u.setRoleId("H0010");
			u.setSrc("01");
			u.setOwnerId(employee.getOwnerId());
		}
		try{
			this.userService.save(u);
			this.employeeService.setAsUser(u.getCode());
			CacheManager.refreshUserCache();
			CacheManager.refreshEmployee();
			return returnSuccessInfo("设置成功");
		}catch(Exception e){
			return returnFailInfo("设置失败");
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

	@RequestMapping("/index")
	@Override
	public String index() {
		return "/views/hall/employeeInfo";
	}
}
