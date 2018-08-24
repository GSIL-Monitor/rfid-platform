package com.casesoft.dmc.service.product;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.product.NoPushStyleDao;
import com.casesoft.dmc.model.product.NoPushStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/1/4.
 */
@Service
@Transactional
public class NoPushStyleService extends AbstractBaseService<NoPushStyle, String> {
    @Autowired
    private NoPushStyleDao noPushStyleDao;
    @Override
    public Page<NoPushStyle> findPage(Page<NoPushStyle> page, List<PropertyFilter> filters) {
        return this.noPushStyleDao.findPage(page,filters);
    }

    @Override
    public void save(NoPushStyle entity) {

    }

    @Override
    public NoPushStyle load(String id) {
        return null;
    }

    @Override
    public NoPushStyle get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<NoPushStyle> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<NoPushStyle> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(NoPushStyle entity) {

    }

    @Override
    public void delete(NoPushStyle entity) {

    }

    @Override
    public void delete(String id) {

    }

    public List<NoPushStyle> findStyleAll(){
        String hql="from NoPushStyle t where t.pushsuccess='N'";
        List<NoPushStyle> NoPushStyleList = this.noPushStyleDao.find(hql);
        return NoPushStyleList;
    }
}
