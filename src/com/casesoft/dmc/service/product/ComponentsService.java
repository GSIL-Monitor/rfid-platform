package com.casesoft.dmc.service.product;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.product.ComponentsDao;
import com.casesoft.dmc.dao.product.ComponentsProductDao;
import com.casesoft.dmc.model.product.ComponentsProduct;
import com.casesoft.dmc.model.shop.Components;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lly on 2018/11/2.
 */
@Service
@Transactional
public class ComponentsService extends AbstractBaseService<Components,String>{
    @Autowired
    private ComponentsDao componentsDao;
    @Autowired
    private ComponentsProductDao componentsProductDao;

    @Override
    public Page<Components> findPage(Page<Components> page, List<PropertyFilter> filters) {
        return this.componentsDao.findPage(page, filters);
    }

    @Override
    public void save(Components entity) {
        this.componentsDao.saveOrUpdate(entity);
    }
    public void saveComponentsProduct(ComponentsProduct entity) {
        this.componentsProductDao.saveOrUpdate(entity);
    }
    public void saveBatchComponentsProduct(List<ComponentsProduct> entity) {
        this.componentsProductDao.batchExecute("delete from ComponentsProduct where styleId=?", entity.get(0).getStyleId());
        this.componentsProductDao.doBatchInsert(entity);
    }

    @Override
    public Components load(String id) {
        return null;
    }

    @Override
    public Components get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<Components> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<Components> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(Components entity) {

    }

    @Override
    public void delete(Components entity) {

    }

    @Override
    public void delete(String id) {

    }
    public List<Components> getComponents(Integer deep){
        List<PropertyFilter> filters = new ArrayList<>();
        PropertyFilter filter = new PropertyFilter("EQI_deep",deep.toString());
        filters.add(filter);
        return this.componentsDao.find(filters);
    }
    //模糊查詢成分
    public List<Components> findByRemark(String term,String parentCode){
        String hql = "from Components where parentId = '"+parentCode+"' and name like '%"+term+"%'";
        return this.componentsDao.find(hql);
    }
    public List<ComponentsProduct> findByStyleId(List<PropertyFilter> filters){
        return this.componentsProductDao.find(filters);
    }
}
