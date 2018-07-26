package com.casesoft.dmc.service.product;

import java.util.List;

import com.casesoft.dmc.model.cfg.PropertyType;
import com.casesoft.dmc.model.product.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.product.StyleCollocationDao;
import com.casesoft.dmc.dao.product.StyleDao;

@Service
@Transactional
public class StyleService extends AbstractBaseService<Style, String> {

	@Autowired
	private StyleDao styleDao;
	
    @Autowired
    private StyleCollocationDao styleCollocationDao;
	@Override
	@Transactional(readOnly = true)
	public Page<Style> findPage(Page<Style> page, List<PropertyFilter> filters) {
		return this.styleDao.findPage(page, filters);
	}

	@Override
	public void save(Style entity) {

		this.styleDao.saveOrUpdate(entity);
	}

	public void saveSize(Size size) {
		this.styleDao.saveOrUpdateX(size);
	}
	public void saveColor(Color color){
		this.styleDao.saveOrUpdateX(color);
	}
	public void saveSizeSort(SizeSort sizeSort) {
		this.styleDao.saveOrUpdateX(sizeSort);
	}

	public Style fundByStyleId(String styleId){
		return (Style)this.styleDao.findUnique("from Style s where s.styleId=?", styleId);
	}
//判断是否存在
	public boolean existSize(String id) {
		String hql = "from Size s where s.id=?";
		return this.styleDao.find(hql, new Object[]{id}).size() > 0;
	}
	public boolean existColor(String id) {
		String hql = "from Color c where c.id=?";
		return this.styleDao.find(hql, new Object[]{id}).size() > 0;
	}
	public boolean existSizeSort(String id) {
		String hql = "from SizeSort s where s.id=?";
		return this.styleDao.find(hql, new Object[]{id}).size() > 0;
	}
	public List<SizeSort> getAllSizeSort() {
		return this.styleDao.find("from SizeSort s", new Object[] {});
	}

	public List<Size> getSizeBySortId(String sortId,String sizeId){
		String hql="from  Size s where s.sortId=?";
		if(sizeId!=null&&!"".equals(sizeId)){
			hql+=" and s.sizeId='"+sizeId+"'";
		}
		return this.styleDao.find(hql, new Object[]{sortId});
	}
	/**
	 * 将styleList保存
	 * 
	 * @param styleList
	 */
	public void saveList(List<Style> styleList) {
		// this.styleDao.batchExecute("delete from Style", new Object[]{});
		this.styleDao.doBatchInsert(styleList);
	}

	public void saveStyleSortList(List<StyleSort> ssList) {
		this.styleDao.doBatchInsert(ssList);
	}

	@Transactional(readOnly = true)
	public List<Style> find(int type) {
		// 0,未搭配1,已搭配
		String sql = "";
		if (type == 0) {
			// sql =
			// "select * from tbl_cfg_sku p where p.code not in (select c.code from tbl_shop_collocation c)";
			sql = "select * from tcsty p where p.sn not in (select c.s1 from tssc c)";
		} else {
			sql = "select * from tcsty p where p.sn in (select c.s1 from tssc c)";
		}

		return this.styleDao.findObjectBySQL(sql);
	}

	public void saveList2(List<Color> colorList) {
		// this.styleDao.batchExecute("delete from Color", new Object[]{});
		this.styleDao.doBatchInsert(colorList);
	}

	public void saveList3(List<Size> sizeList) {
		/*
		 * Map<String,SizeSort> sortMap = new HashMap<String,SizeSort>();
		 * for(Size s : sizeList) { String sortId = s.getSortId();
		 * if(!sortMap.containsKey(sortId)) { SizeSort sort = new SizeSort();
		 * sort.setId(sortId); sort.setSortNo(sortId); sort.setSortName(sortId);
		 * sort.setSizeNames(s.getSizeName()); sortMap.put(sortId, sort); } else
		 * { SizeSort sort = sortMap.get(sortId); String sortNames =
		 * sort.getSizeNames(); sortNames += ("/"+s.getSizeName());
		 * sort.setSizeNames(sortNames); sortMap.put(s.getSortId(), sort); } }
		 */

		// this.styleDao.batchExecute("delete from Size", new Object[]{});
		this.styleDao.doBatchInsert(sizeList);
		// this.styleDao.doBatchInsert(sortMap.values());//测试时不执行
	}

	public void saveList3(List<Size> sizeList, List<SizeSort> ssList) {

		this.styleDao.doBatchInsert(sizeList);
		if (null != ssList)
			this.saveSizeSortList(ssList);
	}

	public void saveSizeSortList(List<SizeSort> ssList) {
		this.styleDao.doBatchInsert(ssList);
	}

	@Override
	public Style load(String id) {
		// TODO Auto-generated method stub
		return this.styleDao.load(id);
	}

	@Override
	public Style get(String propertyName, Object value) {
		// TODO Auto-generated method stub
		return this.styleDao.findUniqueBy(propertyName,value);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Style> find(List<PropertyFilter> filters) {
		return this.styleDao.find(filters);
	}
	@Transactional(readOnly = true)	
	public List<StyleCollocation> findStyleCollocation(List<PropertyFilter> filters) {
		return this.styleCollocationDao.find(filters);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Style> getAll() {
		return this.styleDao.getAll();
	}

	@Override
	public void update(Style entity) {
		this.styleDao.saveOrUpdate(entity);

	}

	@Override
	public void delete(Style entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}

	/**
	 * 获取尺寸组信息
	 * 
	 * @return
	 */
	public <SizeSort> List<SizeSort> findSizeSort() {
		String hql = "from SizeSort ss";
		return this.styleDao.find(hql, new Object[] {});
	}

	@SuppressWarnings("hiding")
	@Override
	@Transactional(readOnly = true)
	public <Color> List<Color> findAll() {
		String hql = "from Color c";
		return this.styleDao.find(hql, new Object[] {});
	}

	// john修改
	@Transactional(readOnly = true)
	public List<Color> findAll(String colorId) {
		String hql = "from Color c";
		hql += " where c.colorId='" + colorId + "'";
		return this.styleDao.find(hql, new Object[] {});
	}

	// ////
	@Transactional(readOnly = true)
	public List<Size> findAll2() {
		String hql = "from Size s";
		return this.styleDao.find(hql, new Object[] {});
	}

	// john
	@Transactional(readOnly = true)
	public List<Size> findAll2(String sizeId) {
		String hql = "from Size s";
		hql += " where s.sizeId='" + sizeId + "'";
		return this.styleDao.find(hql, new Object[] {});
	}

	public StyleDao getStyleDao() {
		return styleDao;
	}

	public void setStyleDao(StyleDao styleDao) {
		this.styleDao = styleDao;
	}

    public List<PropertyType> findStylePropertyType() {
		return this.styleDao.find("from PropertyType where type=? order by seqNo asc",new Object[]{"商品代码分类"} );
    }

	public void saveStyleAndProducts(Style sty, List<Product> saveList) {
		this.styleDao.saveOrUpdate(sty);
		if(saveList.size() > 0){
			this.styleDao.doBatchInsert(saveList);
		}
	}
}
