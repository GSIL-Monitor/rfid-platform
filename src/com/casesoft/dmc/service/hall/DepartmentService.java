package com.casesoft.dmc.service.hall;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.hall.DepartmentDao;
import com.casesoft.dmc.model.hall.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/3/10 0010.
 */
@Service
@Transactional
public class DepartmentService extends AbstractBaseService<Department, String> {

	@Autowired
	private DepartmentDao departmentDao;

	@Override
	public Page<Department> findPage(Page<Department> page, List<PropertyFilter> filters) {
		return this.departmentDao.findPage(page, filters);
	}

	@Override
	public void save(Department entity) {
		this.departmentDao.saveOrUpdate(entity);
	}

	public String findMaxCode() {
		String codeFlag = "D";   //define department's codeFlag as "D"
		String hql = "select max(CAST(SUBSTRING(depart.code," + (codeFlag.length() + 1) + "),integer)) from Department as depart";
		Integer code = this.departmentDao.findUnique(hql, new Object[]{});

		return code == null ? (codeFlag + "001") : codeFlag + CommonUtil.convertIntToString(code+1, 3);


	}

	@Override
	public Department load(String id) {
		return this.departmentDao.load(id);
	}

	public Department findByCode(String code) {

		return (Department) this.departmentDao.findUnique("from Department d where d.code=?", new Object[]{code});
	}

	@Override
	public Department get(String propertyName, Object value) {
		return null;
	}

	@Override
	public List<Department> find(List<PropertyFilter> filters) {
		return this.departmentDao.find(filters);
	}

	public List<Department> find(String propertyName,Object value){
		return this.departmentDao.findBy(propertyName,value);
	}
	@Override
	public List<Department> getAll() {
		return this.departmentDao.getAll();
	}

	@Override
	public <X> List<X> findAll() {
		return null;
	}

	@Override
	public void update(Department entity) {

	}

	@Override
	public void delete(Department entity) {

	}

	@Override
	public void delete(String id) {

	}
}
