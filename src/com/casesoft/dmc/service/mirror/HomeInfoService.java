package com.casesoft.dmc.service.mirror;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.mirror.HomeDao;
import com.casesoft.dmc.model.mirror.HomeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class HomeInfoService extends AbstractBaseService<HomeInfo,String> {

    @Autowired
    private HomeDao homeDao;

    @Override
    public Page<HomeInfo> findPage(Page<HomeInfo> page, List<PropertyFilter> filters) {
        return this.homeDao.findPage(page, filters);
    }

    public HomeInfo findById(String id){
        String hql="from HomeInfo h where h.id=?";
        return this.homeDao.findUnique(hql,new Object[]{id});
    }

    public int findMaxSeqNo(){
        String hql="select max(seqNo) from HomeInfo";
        Object obj=this.homeDao.findUnique(hql);
        if (obj==null){
            return 0;
        }else {
            return Integer.parseInt(obj.toString());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public void save(HomeInfo homeInfo) {
       this.homeDao.saveOrUpdate(homeInfo);
    }

    @Override
    public HomeInfo load(String id) {
        return null;
    }

    @Override
    public HomeInfo get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<HomeInfo> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<HomeInfo> getAll() {
        return this.homeDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(HomeInfo entity) {

    }

    @Override
    public void delete(HomeInfo entity) {

    }

    @Override
    public void delete(String id) {

    }
}
