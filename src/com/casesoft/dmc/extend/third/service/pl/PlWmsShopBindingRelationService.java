package com.casesoft.dmc.extend.third.service.pl;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.wms.pl.PlShopBindingRecordDao;
import com.casesoft.dmc.dao.wms.pl.PlShopBindingRelationDao;
import com.casesoft.dmc.dao.wms.pl.PlShopWmsFloorAreaDao;
import com.casesoft.dmc.dao.wms.pl.PlWmsRackDao;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.model.pl.PlWmsShopBindingRecord;
import com.casesoft.dmc.extend.third.model.pl.PlWmsShopBindingRelation;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.model.wms.pl.PlShopWmsFloorArea;
import com.casesoft.dmc.model.wms.pl.PlWmsRack;
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
public class PlWmsShopBindingRelationService extends BaseService<PlWmsShopBindingRelation, String> {

    @Autowired
    private PlShopBindingRecordDao plShopBindingRecordDao;
    @Autowired
    private PlShopBindingRelationDao plShopBindingRelationDao;
    @Autowired
    private PlWmsRackDao plWmsRackDao;
    @Autowired
    private PlShopWmsFloorAreaDao plShopWmsFloorAreaDao;


    public DataResult find(RequestPageData<?> request) {
        return this.plShopBindingRelationDao.find(request);
    }

    public void saveShopBindingRecord(PlWmsShopBindingRecord plWmsShopBindingRecord) {
        this.plShopBindingRecordDao.saveOrUpdate(plWmsShopBindingRecord);
    }

    public List<String> findRackByStyleId(String styleId) {
        String hql = "select w.rackId from PlWmsShopBindingRelation w where w.styleId=?";
        return this.plShopBindingRelationDao.find(hql, new Object[]{styleId});
    }


    public List<PlWmsShopBindingRelation> findByShopId(String shopId) {
        String floorareaHql = "from PlShopWmsFloorArea fa where fa.parentCode=?";
        List<PlShopWmsFloorArea> plShopWmsFloorAreaList = this.plShopWmsFloorAreaDao.find(floorareaHql, new Object[]{shopId});
        List<PlWmsShopBindingRelation> plWmsShopBindingRelationList = new ArrayList<>();
        for (PlShopWmsFloorArea plShopWmsFloorArea : plShopWmsFloorAreaList) {
            List<PlWmsShopBindingRelation> temp=findByFloorAreaId(plShopWmsFloorArea.getId());
            plWmsShopBindingRelationList.addAll(temp);
        }
        return plWmsShopBindingRelationList;
    }

    public List<PlWmsShopBindingRelation> findByFloorAreaId(String floorareaId) {
        String floorHql = "from  PlWmsRack ra where ra.parentId=?";
        List<PlWmsRack> plWmsRackList = this.plWmsRackDao.find(floorHql, new Object[]{floorareaId});
        List<PlWmsShopBindingRelation> plWmsShopBindingRelationList = new ArrayList<>();
        for (PlWmsRack plWmsRack : plWmsRackList) {
            List<PlWmsShopBindingRelation> temp = findByRackId(plWmsRack.getId());
            plWmsShopBindingRelationList.addAll(temp);
        }

        return plWmsShopBindingRelationList;
    }

    public List<PlWmsShopBindingRelation> findWmsPlShopRelations() {
        StringBuffer hql = new StringBuffer("select new com.casesoft.dmc.extend.third.model.pl.PlWmsShopBindingRelation(").append(
                " relation.styleId,relation.colorId,")
                .append("rack.id,rack.barcode,rack.name,rack.image,rack.remark,")
                .append("area.id,area.barcode,area.name,area.image,area.remark,unit.code)").append(
                        "from PlWmsShopBindingRelation relation,PlShopWmsFloorArea area,Unit unit,PlWmsRack rack ")
                .append(" where ")
                .append(" relation.rackId=rack.id and rack.parentId=area.id  and area.parentCode=unit.code ");
        return this.plWmsRackDao.find(hql.toString(), new Object[]{});
    }
    public List<PlWmsShopBindingRelation> findByRackId(String rackId) {
        String hql = " from PlWmsShopBindingRelation w where w.rackId=?";
        return this.plShopBindingRelationDao.find(hql, new Object[]{rackId});
    }


    public PlWmsShopBindingRelation findRelationById(String id) {
        String hql = "from PlWmsShopBindingRelation w where w.id=?";
        return this.plShopBindingRelationDao.findUnique(hql, new Object[]{id});
    }




    @Override
    public Page<PlWmsShopBindingRelation> findPage(Page<PlWmsShopBindingRelation> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(PlWmsShopBindingRelation entity) {
         this.plShopBindingRelationDao.saveOrUpdate(entity);
    }

    @Override
    public PlWmsShopBindingRelation load(String id) {
        return null;
    }

    @Override
    public PlWmsShopBindingRelation get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<PlWmsShopBindingRelation> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<PlWmsShopBindingRelation> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(PlWmsShopBindingRelation entity) {

    }

    @Override
    public void delete(PlWmsShopBindingRelation entity) {

    }

    @Override
    public void delete(String id) {
        this.plShopBindingRelationDao.delete(id);
        /*CacheManager.refreshWmsPlRackBindingRelationCache();*/
    }
}
