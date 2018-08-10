package com.casesoft.dmc.service.sys.impl;

import java.util.ArrayList;
import java.util.List;

import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.model.cfg.VO.State;
import com.casesoft.dmc.model.rem.VO.TreeChildVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.sys.WarehouseDao;
import com.casesoft.dmc.model.sys.Unit;


@Service
@Transactional
public class WarehouseService implements IBaseService<Unit, String> {

	@Autowired
	private WarehouseDao warehouseDao;


	@Override
	@Transactional(readOnly = true)
	public Page<Unit> findPage(Page<Unit> page, List<PropertyFilter> filters) {
		if (CommonUtil.isBlank(filters)) {
			return this.warehouseDao.findPage(page, "from Unit u");
		}
		return this.warehouseDao.findPage(page, filters);


	}

	public Unit findUnitById(String code) {
		return (Unit) this.warehouseDao.findUnique("from Unit u where u.code='" + code + "'");
	}

	@Override
	public void save(Unit entity) {
		this.warehouseDao.saveOrUpdate(entity);

	}

	public String getMaxCode(){
		String wareFlag= Constant.UnitCodePrefix.Warehouse;
		Integer wareType=Constant.UnitType.Warehouse;
		String hql="select max(CAST(SUBSTRING(unit.code,"+(wareFlag.length()+1)+"),integer))" +
				" from Unit as unit where unit.type=? and unit.src='01' and unit.code like '"+wareFlag+"%'";
		Integer code=this.warehouseDao.findUnique(hql,new Object[]{wareType});

		return code==null?(wareFlag+"001"):wareFlag+CommonUtil.convertIntToString(code+1,3);

	}

	@Override
	public Unit load(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Unit get(String propertyName, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Unit> find(List<PropertyFilter> filters) {

		return this.warehouseDao.find(filters);
	}

	/**
	 * 把查询的全部仓库信息转成jstree格式，使用懒加载
	 * @param filters
	 * @return
	 */
	public List<TreeChildVO> unitTreeList(List<PropertyFilter> filters){
		List<TreeChildVO> result =new ArrayList<>();
		List<Unit> unitList = this.warehouseDao.find(filters);
		for(Unit unit:unitList){
			TreeChildVO treeChildVO =new TreeChildVO();
			treeChildVO.setId(unit.getId());
			treeChildVO.setChildren(true);
			treeChildVO.setState(new State(false));
			treeChildVO.setText(unit.getName());
			treeChildVO.setDeep(Constant.RepositoryType.root);
			result.add(treeChildVO);
		}
		return result;
	}

	@Override
	public List<Unit> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <X> List<X> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Unit entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Unit entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}


}
