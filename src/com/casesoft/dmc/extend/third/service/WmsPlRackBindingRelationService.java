package com.casesoft.dmc.extend.third.service;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.wms.*;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.model.WmsPlRackBindingRecord;
import com.casesoft.dmc.extend.third.model.WmsPlRackBindingRelation;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.model.wms.WmsFloor;
import com.casesoft.dmc.model.wms.WmsFloorArea;
import com.casesoft.dmc.model.wms.WmsRack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GuoJunwen on 2017/3/16 0016.
 */
@Service
@Transactional
public class WmsPlRackBindingRelationService extends BaseService<WmsPlRackBindingRelation,String> {
    @Autowired
    private WmsPlRackBindingRelationDao wmsPlRackBindingRelationDao;
    @Autowired
    private WmsFloorAreaDao wmsFloorAreaDao;
    @Autowired
    private WmsFloorDao wmsFloorDao;
    @Autowired
    private WmsRackDao wmsRackDao;
    @Autowired
    private WmsPlRackBindingRecordDao wmsPlRackBindingRecordDao;



    @Override
    public Page<WmsPlRackBindingRelation> findPage(Page<WmsPlRackBindingRelation> page, List<PropertyFilter> filters) {
        return null;
    }

    public void saveRackBindingRecord(WmsPlRackBindingRecord wmsPlRackBindingRecord){
        this.wmsPlRackBindingRecordDao.saveOrUpdate(wmsPlRackBindingRecord);
    }

    public DataResult find(RequestPageData<?> request) {
    return  this.wmsPlRackBindingRelationDao.find(request);
    }


    public List<String> findRackByStyleId(String styleId){
        String hql="select w.rackId from WmsPlRackBindingRelation w where w.styleId=?";
        return this.wmsPlRackBindingRelationDao.find(hql,new Object[]{styleId});
    }

    public List findByShopId(String shopId){
        String floorareaHql="from WmsFloorArea fa where fa.parentCode=?";
        String floorHql="from  WmsFloor fl where fl.parentId=?";
        String rackHql="from WmsRack ra where ra.parentId=?";
        String hql = " from WmsPlRackBindingRelation w where w.rackId=?";
        List<WmsFloorArea> wmsFloorAreaList=this.wmsFloorAreaDao.find(floorareaHql,new Object[]{shopId});
        List<WmsPlRackBindingRelation> wmsPlRackBindingRelationList=new ArrayList<>();
        for(WmsFloorArea wmsFloorArea:wmsFloorAreaList){
            List<WmsFloor> wmsFloorList=this.wmsFloorDao.find(floorHql,new Object[]{wmsFloorArea.getId()});
            for (WmsFloor wmsFloor:wmsFloorList){
                List<WmsRack> wmsRackList=this.wmsRackDao.find(rackHql,new Object[]{wmsFloor.getId()});
                for (WmsRack wmsRack:wmsRackList){
                    List<WmsPlRackBindingRelation> temp=this.wmsPlRackBindingRelationDao.find(hql,wmsRack.getId());
                    wmsPlRackBindingRelationList.addAll(temp);
                }
            }
        }


        return wmsPlRackBindingRelationList;
    }

    public List findByFloorAreaId(String floorareaId){
        String floorHql="from  WmsFloor fl where fl.parentId=?";
        String rackHql="from WmsRack ra where ra.parentId=?";
        String hql = " from WmsPlRackBindingRelation w where w.rackId=?";
        List<WmsFloor> wmsFloorList=this.wmsFloorDao.find(floorHql,new Object[]{floorareaId});
        List<WmsPlRackBindingRelation> wmsPlRackBindingRelationList=new ArrayList<>();
        for (WmsFloor wmsFloor:wmsFloorList){
            List<WmsRack> wmsRackList=this.wmsRackDao.find(rackHql,new Object[]{wmsFloor.getId()});
            for (WmsRack ra:wmsRackList){
                List<WmsPlRackBindingRelation> temp=this.wmsPlRackBindingRelationDao.find(hql,new Object[]{ra.getId()});
                wmsPlRackBindingRelationList.addAll(temp);
            }
        }

        return wmsPlRackBindingRelationList;
    }

    public List<WmsPlRackBindingRelation> findByFloorId(String floorId){
        String rackHql="from WmsRack ra where ra.parentId=?";
        List<WmsRack> wmsRackList=this.wmsRackDao.find(rackHql,new Object[]{floorId});
        List<WmsPlRackBindingRelation> wmsPlRackBindingRelationList=new ArrayList<>();
        String hql = " from WmsPlRackBindingRelation w where w.rackId=?";
        for (WmsRack ra:wmsRackList){
            List<WmsPlRackBindingRelation> temp=this.wmsPlRackBindingRelationDao.find(hql, new Object[]{ra.getId()});
            wmsPlRackBindingRelationList.addAll(temp);
        }
        return wmsPlRackBindingRelationList;
    }

    public List<WmsPlRackBindingRelation> findByRackId(String rackId){
        String hql = " from WmsPlRackBindingRelation w where w.rackId=?";
        return this.wmsPlRackBindingRelationDao.find(hql, new Object[]{rackId});
    }


    public WmsPlRackBindingRelation findRelationById(String id){
        String hql="from WmsPlRackBindingRelation w where w.id=?";
        return this.wmsPlRackBindingRelationDao.findUnique(hql, new Object[]{id});
    }

    public void unbindShop(String id){
        String shopHql="from WmsFloorArea f where f.parentId=?";
        List<WmsFloorArea> wmsFloorAreaList=this.wmsFloorAreaDao.find(shopHql,new Object[]{id});
        for (WmsFloorArea wmsFloorArea:wmsFloorAreaList){
            unbindFloorArea(wmsFloorArea.getId());
        }

    }

    public void unbindFloorArea(String id){
        String floorareaHql="update WmsFloorArea f set f.enabled =0 where f.id=?";
        this.wmsFloorAreaDao.batchExecute(floorareaHql,new Object[]{id});
       String findHql="from WmsFloor f where f.parentId=?";
       List<WmsFloor> wmsFloorList=this.wmsFloorDao.find(findHql,new Object[]{id});
        for (WmsFloor wmsFloor:wmsFloorList){
            unbindFloor(wmsFloor.getId());
        }

    }

    public void unbindFloor(String id){
        String floorHql="update WmsFloor f set f.enabled = 0 where f.id=?";
        this.wmsFloorDao.batchExecute(floorHql, new Object[]{id});
        String rackHql="update WmsRack r set r.enabled = 0 where r.parentId=?";
        this.wmsRackDao.batchExecute(rackHql,new Object[]{id});

    }

    public void unbindRack(String id){
       String rackHql="update WmsRack  set enabled = 0 where id=?";
       this.wmsRackDao.batchExecute(rackHql, new Object[]{id});
    }

        @Override
    public void save(WmsPlRackBindingRelation entity) {
           this.wmsPlRackBindingRelationDao.saveOrUpdate(entity);
    }

    @Override
    public WmsPlRackBindingRelation load(String id) {
        return null;
    }

    @Override
    public WmsPlRackBindingRelation get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<WmsPlRackBindingRelation> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<WmsPlRackBindingRelation> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(WmsPlRackBindingRelation entity) {

    }

    @Override
    public void delete(WmsPlRackBindingRelation entity) {

    }

    @Override
    public void delete(String id) {
        this.wmsPlRackBindingRelationDao.delete(id);
    }
}
