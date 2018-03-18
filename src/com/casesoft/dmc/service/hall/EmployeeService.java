package com.casesoft.dmc.service.hall;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.hall.EmployeeDao;
import com.casesoft.dmc.model.hall.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/3/10 0010.
 */

@Service
@Transactional
public class EmployeeService extends AbstractBaseService<Employee,String>{

	@Autowired
	private EmployeeDao employeeDao;

	@Override
	public Page<Employee> findPage(Page<Employee> page, List<PropertyFilter> filters) {
		return this.employeeDao.findPage(page,filters);
	}

	@Override
	public void save(Employee entity) {
		this.employeeDao.saveOrUpdate(entity);
	}

	@Override
	public Employee load(String id) {
		return this.employeeDao.load(id);
	}

	public Employee findByCode(String code){
		return this.employeeDao.findUnique("from Employee e where e.code =?",new Object[]{code});
	}

	public String findMaxCode(){
		String codeFlag="E";
		String hql="select max(CAST(SUBSTRING(e.code,"+(codeFlag.length()+1)+"),integer))from Employee as e";
		Integer code=this.employeeDao.findUnique(hql,new Object[]{});

		return code==null?codeFlag+"001":codeFlag+ CommonUtil.convertIntToString(code + 1,3);
	}

	public void setAsUser(String code){
		Employee e=findByCode(code);
		e.setIsUser(1);
		this.employeeDao.saveOrUpdate(e);
	}

	@Override
	public Employee get(String propertyName, Object value) {
		return null;
	}

	@Override
	public List<Employee> find(List<PropertyFilter> filters) {
		return this.employeeDao.find(filters);
	}

	@Override
	public List<Employee> getAll() {
		return this.employeeDao.getAll();
	}

	@Override
	public  List<Employee> findAll() {
		return this.employeeDao.getAll();
	}

	@Override
	public void update(Employee entity) {

	}

	@Override
	public void delete(Employee entity) {

	}

	@Override
	public void delete(String id) {

	}
}
