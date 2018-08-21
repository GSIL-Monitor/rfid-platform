package com.casesoft.dmc.extend.third.service.pl;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.wms.pl.PlWarehouseBindingRecordDao;
import com.casesoft.dmc.dao.wms.pl.PlWarehouseBindingRelationDao;
import com.casesoft.dmc.dao.wms.pl.PlWarehouseWmsFloorAreaDao;
import com.casesoft.dmc.dao.wms.pl.PlWmsFloorDao;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.model.pl.PlWmsWarehouseBindingRecord;
import com.casesoft.dmc.extend.third.model.pl.PlWmsWarehouseBindingRelation;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.model.wms.pl.PlWarehouseWmsFloorArea;
import com.casesoft.dmc.model.wms.pl.PlWmsFloor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GuoJunwen on 2017/4/6 0006.
 */
@Service
@Transactional
public class PlWmsWarehouseBindingRelationService extends BaseService<PlWmsWarehouseBindingRelation,String> {

    @Autowired
    private PlWarehouseBindingRecordDao plWarehouseBindingRecordDao;
    @Autowired
    private PlWarehouseBindingRelationDao plWarehouseBindingRelationDao;
    @Autowired
    private PlWarehouseWmsFloorAreaDao plWarehouseWmsFloorAreaDao;
    @Autowired
    private PlWmsFloorDao plWmsFloorDao;

    public DataResult find(RequestPageData<?> request) {
        return  this.plWarehouseBindingRecordDao.find(request);
    }

    public void saveWarehouseBindingRecord(PlWmsWarehouseBindingRecord plWmsShopBindingRecord) {
        this.plWarehouseBindingRecordDao.saveOrUpdate(plWmsShopBindingRecord);
    }

    public List<String> findFloorByStyleId(String styleId){
        String hql="select w.floorId from PlWmsWarehouseBindingRecord w where w.styleId=?";
        return this.plWarehouseBindingRecordDao.find(hql,new Object[]{styleId});
    }

    public List findByShopId(String shopId){
        String floorareaHql="from PlWarehouseWmsFloorArea fa where fa.parentCode=?";
        List<PlWarehouseWmsFloorArea> plWarehouseWmsFloorAreaList
                =this.plWarehouseWmsFloorAreaDao.find(floorareaHql,new Object[]{shopId});
        List<PlWmsWarehouseBindingRelation> plWmsWarehouseBindingRelationList=new ArrayList<>();
        for(PlWarehouseWmsFloorArea plWarehouseWmsFloorArea:plWarehouseWmsFloorAreaList){
            List<PlWmsWarehouseBindingRelation> temp=findByFloorAreaId(plWarehouseWmsFloorArea.getId());
            plWmsWarehouseBindingRelationList.addAll(temp);
        }


        return plWmsWarehouseBindingRelationList;
    }

    public List<PlWmsWarehouseBindingRelation> findByFloorAreaId(String floorareaId){
        String floorHql="from  PlWmsFloor fl where fl.parentId=?";
        List<PlWmsFloor> plWmsFloorList=this.plWmsFloorDao.find(floorHql,new Object[]{floorareaId});
        List<PlWmsWarehouseBindingRelation> plWmsWarehouseBindingRelationList=new ArrayList<>();
        for (PlWmsFloor plWmsFloor:plWmsFloorList){
            List<PlWmsWarehouseBindingRelation> temp=findByFloorId(plWmsFloor.getId());
            plWmsWarehouseBindingRelationList.addAll(temp);
        }

        return plWmsWarehouseBindingRelationList;
    }

    public List<PlWmsWarehouseBindingRelation> findByFloorId(String floorId){
        String hql = " from PlWmsWarehouseBindingRelation w where w.floorId=?";
        return this.plWarehouseBindingRelationDao.find(hql, new Object[]{floorId});
    }


    public PlWmsWarehouseBindingRelation findRelationById(String id){
        String hql="from WmsPlRackBindingRelation w where w.id=?";
        return this.plWarehouseBindingRelationDao.findUnique(hql, new Object[]{id});
    }




    @Override
    public Page<PlWmsWarehouseBindingRelation> findPage(Page<PlWmsWarehouseBindingRelation> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(PlWmsWarehouseBindingRelation entity) {
          this.plWarehouseBindingRelationDao.saveOrUpdate(entity);
    }

    @Override
    public PlWmsWarehouseBindingRelation load(String id) {
        return null;
    }

    @Override
    public PlWmsWarehouseBindingRelation get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<PlWmsWarehouseBindingRelation> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<PlWmsWarehouseBindingRelation> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(PlWmsWarehouseBindingRelation entity) {

    }

    @Override
    public void delete(PlWmsWarehouseBindingRelation entity) {

    }

    @Override
    public void delete(String id) {
       this.plWarehouseBindingRelationDao.delete(id);
    }
}
