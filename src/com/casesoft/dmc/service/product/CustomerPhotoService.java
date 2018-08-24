package com.casesoft.dmc.service.product;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.product.CustomerPhotoDao;
import com.casesoft.dmc.model.product.CustomerPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/3/13.
 */
@Service
@Transactional
public class CustomerPhotoService implements IBaseService<CustomerPhoto,String> {
    @Autowired
    private CustomerPhotoDao customerPhotoDao;

    @Override
    public Page<CustomerPhoto> findPage(Page<CustomerPhoto> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(CustomerPhoto entity) {
        this.customerPhotoDao.saveOrUpdate(entity);
    }

    @Override
    public CustomerPhoto load(String id) {
        return null;
    }

    @Override
    public CustomerPhoto get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<CustomerPhoto> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<CustomerPhoto> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(CustomerPhoto entity) {

    }

    @Override
    public void delete(CustomerPhoto entity) {

    }

    @Override
    public void delete(String id) {

    }
    public Integer getMaxSeq(){
        String hql="select Max(seqNo) from CustomerPhoto";
        Integer seqNo= this.customerPhotoDao.findUnique(hql);
        return seqNo==null?1:seqNo+1;
    }
}
