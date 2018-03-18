package com.casesoft.dmc.service.location;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.location.RegionalDao;
import com.casesoft.dmc.model.location.AdministrativeRegion;
import com.casesoft.dmc.model.location.Regional;
import com.casesoft.dmc.model.sys.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * Created by Administrator on 2018/1/19.
 */
@Service
@Transactional
public class RegionalService implements IBaseService<Regional, String> {
    @Autowired
    private RegionalDao regionalDao;
    @Override
    public Page<Regional> findPage(Page<Regional> page, List<PropertyFilter> filters) {
        return this.regionalDao.findPage(page, filters);
    }

    @Override
    public void save(Regional entity) {
        this.regionalDao.save(entity);
    }

    @Override
    public Regional load(String id) {
        return this.regionalDao.load(id);
    }

    @Override
    public Regional get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<Regional> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<Regional> getAll() {
        return this.regionalDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(Regional entity) {

    }

    @Override
    public void delete(Regional entity) {

    }

    @Override
    public void delete(String id) {

    }

    public List<AdministrativeRegion> findprovince(){
        String hql="from AdministrativeRegion t where t.levelType=1 order by t.id asc";
        List<AdministrativeRegion> AdministrativeRegions = this.regionalDao.find(hql);
        return AdministrativeRegions;
    }

    public List<AdministrativeRegion> findcity(String provinceid){
        String hql="from AdministrativeRegion t where t.levelType=2 and t.parentId=? order by t.id asc";
        List<AdministrativeRegion> AdministrativeRegions = this.regionalDao.find(hql,new Object[]{provinceid});
        return AdministrativeRegions;
    }
    public List<AdministrativeRegion> findarea(String cityid){
        String hql="from AdministrativeRegion t where t.levelType=3 and t.parentId=? order by t.id asc";
        List<AdministrativeRegion> AdministrativeRegions = this.regionalDao.find(hql,new Object[]{cityid});
        return AdministrativeRegions;
    }
    public AdministrativeRegion findAdministrativeRegion(String id){
        String hql="from AdministrativeRegion t where t.id=? order by t.id asc";
        AdministrativeRegion AdministrativeRegions = this.regionalDao.findUnique(hql,new Object[]{id});
        return AdministrativeRegions;
    }

    public List<Unit> findallUnit(){
        String hql="from Unit t where t.status=1 and t.type in (2,8)";
        List<Unit> Units = this.regionalDao.find(hql);
        return Units;
    }

    public void pushMessage(String id,Unit unit){
        String hqlunit="update Unit t set t.areasId=?,t.ownerids=? where t.ownerId=?";
        this.regionalDao.batchExecute(hqlunit, unit.getAreasId(),unit.getOwnerids(),id);
        String hqlCustomer="update Customer t set t.areasId=?,t.ownerids=? where t.ownerId=?";
        this.regionalDao.batchExecute(hqlCustomer, unit.getAreasId(),unit.getOwnerids(),id);
    }

    
}
