package com.casesoft.dmc.service.wms.pl;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.wms.pl.PlShopWmsFloorAreaDao;
import com.casesoft.dmc.dao.wms.pl.PlShopWmsViewDao;
import com.casesoft.dmc.dao.wms.pl.PlWmsRackDao;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.model.pl.PlWmsShopBindingRecord;
import com.casesoft.dmc.extend.third.model.pl.PlWmsShopBindingRelation;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.model.wms.pl.PlShopWmsFloorArea;
import com.casesoft.dmc.model.wms.pl.PlShopWmsView;
import com.casesoft.dmc.model.wms.pl.PlWmsRack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by GuoJunwen on 2017/4/5 0005.
 */
@Service
@Transactional
public class PlShopWmsViewService extends BaseService<PlShopWmsView, String> {

    @Autowired
    private PlShopWmsViewDao plShopWmsViewDao;
    @Autowired
    private PlShopWmsFloorAreaDao plShopWmsFloorAreaDao;
    @Autowired
    private PlWmsRackDao plWmsRackDao;

    private static String FlOORAREA = "FA";
    private static String RACK = "RA";

    public PlShopWmsView findViewByRaId(String id) {
        String hql = "from PlShopWmsView f where f.rackId=?";
        return this.plShopWmsViewDao.findUnique(hql, new Object[]{id});
    }

    public List<PlWmsRack> findRackFilter(List<PropertyFilter> filters){
        return this.plWmsRackDao.find(filters);
    }
    public List<PlShopWmsFloorArea> findShopFloorAreaFilter(List<PropertyFilter> filters){
        return this.plShopWmsFloorAreaDao.find(filters);
    }
    public List<PlShopWmsView> findViewByFaId(String id) {
        String hql = "from PlShopWmsView f where f.floorAreaId=?";
        return this.plShopWmsViewDao.find(hql, new Object[]{id});
    }

    public Boolean findRaBarcode(String barcode, String shopId) {
        String hql = "select fv.rackBarcode from PlShopWmsView fv where fv.floorAreaParentCode=?";
        List list = this.plShopWmsViewDao.find(hql, new Object[]{shopId});
        return list.contains(barcode);
    }


    public Boolean findFaBarcode(String barcode, String shopId) {
        String hql = "select fv.floorAreaBarcode from PlShopWmsView fv where fv.floorAreaParentCode=?";
        List list = this.plShopWmsViewDao.find(hql, new Object[]{shopId});
        return list.contains(barcode);
    }

    public String findShopIdByFaId(String faId) {
        String hql = "select fv.floorAreaParentCode from PlShopWmsView fv where fv.floorAreaId=?";
        return String.valueOf(this.plShopWmsViewDao.find(hql, new Object[]{faId}).get(0));
    }


    public String findShopIdByRaId(String raId) {
        String hql = "select fv.floorAreaParentCode from PlShopWmsView fv where fv.rackId=?";
        return String.valueOf(this.plShopWmsViewDao.find(hql, new Object[]{raId}).get(0));
    }

    public String findMaxId(String table) {
        String hql = "";
        if (table.equals("fa")) {
            hql = "select max(CAST(SUBSTRING(f.id," + (FlOORAREA.length() + 1) + "),integer)) from PlShopWmsFloorArea f";
            Integer id = this.plShopWmsFloorAreaDao.findUnique(hql);
            return id == null ? (FlOORAREA + "000001") : FlOORAREA + CommonUtil.convertIntToString(id + 1, 6);
        } else {
            hql = "select max(CAST(SUBSTRING(f.id," + (RACK.length() + 1) + "),integer)) from PlWmsRack f";
            Integer id = this.plWmsRackDao.findUnique(hql);
            return id == null ? (RACK + "000001") : RACK + CommonUtil.convertIntToString(id + 1, 6);
        }
    }

    public PlShopWmsFloorArea findFaById(String id){
        String hql="from PlShopWmsFloorArea w where w.id=?";
        return this.plShopWmsFloorAreaDao.findUnique(hql, new Object[]{id});
    }


    public PlWmsRack findRaById(String id) {
        String hql = "from PlWmsRack w where w.id=?";
        return this.plWmsRackDao.findUnique(hql, new Object[]{id});
    }

    public List<PlShopWmsFloorArea> findfa() {
        String hql = "from PlShopWmsFloorArea fa where fa.enabled=?";
        return this.plShopWmsFloorAreaDao.find(hql, new Object[]{true});
    }

    public List<PlWmsRack> findra() {
        String hql = "from PlWmsRack ra where ra.enabled=?";
        return this.plWmsRackDao.find(hql, new Object[]{true});
    }
    public void updatePlWmsRacks(List<PlWmsRack> plWmsRacks){
        if(CommonUtil.isNotBlank(plWmsRacks)){
            this.plWmsRackDao.doBatchInsert(plWmsRacks);
        }
    }
    public List<PlShopWmsFloorArea> findFaByShopId(String shopId) {
        String hql = "from PlShopWmsFloorArea fa where fa.enabled=? and fa.parentCode=?";
        return this.plShopWmsFloorAreaDao.find(hql, new Object[]{true, shopId});
    }

    public List<PlWmsRack> findRaByFaId(String floorAreaId) {
        String hql = "from PlWmsRack ra where ra.enabled=? and ra.parentId=?";
        return this.plWmsRackDao.find(hql, new Object[]{true, floorAreaId});
    }

    /**
     * @param barcode
     * @param shopCode
     * @return 根据货架编号获取货架信息
     */
    public PlWmsRack loadWmsRackByBarcode(String barcode, String shopCode) {
        StringBuffer hql = new StringBuffer("from PlWmsRack rack where rack.barcode=? and ");
        hql.append(" rack.parentId in ").append("(select area.id from PlShopWmsFloorArea area where area.parentCode=?)");
        PlWmsRack wmsRack = this.plWmsRackDao.findUnique(hql.toString(), new Object[]{barcode, shopCode});
        this.plWmsRackDao.getSession().clear();
        return wmsRack;
    }

    /**
     * @param warehouseCode
     * @param rackCode
     * @param skus          款+颜色，
     * @return 根据查询绑定信息
     * 不在设备所在仓库
     */
    public List<PlWmsShopBindingRelation> findWmsPlShopRelations(String warehouseCode, String rackCode, String skus) {
        StringBuffer hql = new StringBuffer("select new com.casesoft.dmc.extend.third.model.pl.PlWmsShopBindingRelation(")
                .append(" relation.styleId,relation.colorId,rack.barcode)")
                .append(" from PlWmsShopBindingRelation relation,PlWmsRack rack ")
                .append(" where rack.id=relation.rackId and rack.barcode !=?  and CONCAT(relation.styleId,relation.colorId) in(")
                .append(skus).append(") and  rack.id in  (select rack.id from Unit unit,PlShopWmsFloorArea area,PlWmsRack rack ")
                .append("where rack.parentId=area.id and  area.parentCode=unit.code and unit.code=?)");

        return this.plWmsRackDao.find(hql.toString(), new Object[]{rackCode, warehouseCode});
    }
    /**
     * @param warehouseCode
     * @param skus          款+颜色，
     * @return 根据查询绑定信息
     */
    public List<PlWmsShopBindingRelation> findWmsPlShopRelations(String warehouseCode, String skus) {
        StringBuffer hql = new StringBuffer("select new com.casesoft.dmc.extend.third.model.pl.PlWmsShopBindingRelation(").append(
                " relation.styleId,relation.colorId,")
                .append("rack.id,rack.barcode,rack.name,rack.image,rack.remark,")
                .append("area.id,area.barcode,area.name,area.image,area.remark)").append(
                        "from PlWmsShopBindingRelation relation,PlShopWmsFloorArea area,Unit unit,PlWmsRack rack ")
                .append(" where  CONCAT(relation.styleId,relation.colorId) in(")
                .append(skus).append(")")
                .append("and relation.rackId=rack.id and rack.parentId=area.id  and area.parentCode=unit.code and unit.code=?");
        return this.plWmsRackDao.find(hql.toString(), new Object[]{warehouseCode});
    }
    public List<PlWmsRack> loadPlWmsRackByBarcodes(String warehouseCode, String barcodes) {
        List<PlWmsRack> wmsRacks = this.plWmsRackDao.find("from PlWmsRack rack where rack.barcode in(" + barcodes + ") " +
                "and rack.id in(select orack.id from Unit unit,PlShopWmsFloorArea area ,PlWmsRack orack  " +
                "where orack.parentId=area.id and area.parentCode=unit.code and unit.code=?)", new Object[]{warehouseCode});
        this.plWmsRackDao.getSession().clear();
        return wmsRacks;
    }

    public void unbindingShopSku(String ids) {
        StringBuffer hql = new StringBuffer("delete  PlWmsShopBindingRelation relation ")
                .append(" where relation.id in (").append(ids).append(")");
        this.plWmsRackDao.batchExecute(hql.toString(), new Object[]{});
    }

    /**
     * @param wmsPlRackBindingRelations 添加绑定信息
     */
    public void doBatchWmsPlShopRackBindingRelation(List<PlWmsShopBindingRecord> records, List<PlWmsShopBindingRelation> wmsPlRackBindingRelations) {
        if (CommonUtil.isNotBlank(wmsPlRackBindingRelations)) {
            this.plWmsRackDao.doBatchInsert(wmsPlRackBindingRelations);
            this.plWmsRackDao.doBatchInsert(records);
            CacheManager.refreshWmsPlRackBindingRelationCache();
        }
    }
    /**
     * @param warehouseCode
     * @param rackCodes
     * @return 根据查询店铺货架绑定信息
     */
    public List<PlWmsShopBindingRelation> findPlWmsShopBindingRelations(String warehouseCode, String rackCodes) {
        String ins = "";
        if (CommonUtil.isNotBlank(rackCodes)) {
            ins = CommonUtil.addQuotes(rackCodes.split(","));
        }
        if (ins.equals("")) {
            ins = "''";
        }
        StringBuffer hql = new StringBuffer("select new com.casesoft.dmc.extend.third.model.pl.PlWmsShopBindingRelation( relation.styleId,relation.colorId,rack.barcode)")
                .append(" from PlWmsShopBindingRelation relation,PlWmsRack rack where relation.rackId in ")
                .append(" (select rack.id from Unit unit,PlShopWmsFloorArea area,PlWmsRack rack ")
                .append("where rack.parentId=area.id and area.parentCode=unit.code and unit.code=? ")
                .append(" and rack.barcode in( ").append(ins).append(")) and rack.id=relation.rackId ");
        return this.plWmsRackDao.find(hql.toString(), new Object[]{warehouseCode});
    }
    /**
     * @param rackCodes
     * @return 根据店铺货架绑定记录信息
     */
    public List<PlWmsShopBindingRecord> findPlWmsShopBindingRecord(String rackCodes) {
        String ins = "";
        if (CommonUtil.isNotBlank(rackCodes)) {
            ins = CommonUtil.addQuotes(rackCodes.split(","));
        }
        if (ins.equals("")) {
            ins = "''";
        }
        StringBuffer hql = new StringBuffer("from PlWmsShopBindingRecord record ")
                .append(" where  record.rackBarcode in( ").append(ins).append(")) order by record.updateDate desc");
        return this.plWmsRackDao.find(hql.toString(), new Object[]{});
    }
    /**
     * @param plWmsRack 保存或更新货架
     */
    public void saveOrUpdatePlWmsRack(PlWmsRack plWmsRack) {
        if (!plWmsRack.getEnabled()) {
            this.plWmsRackDao.batchExecute("delete PlWmsShopBindingRelation relation where relation.rackId=?", new Object[]{plWmsRack.getId()});
            CacheManager.refreshWmsPlRackBindingRelationCache();
        }
        this.plWmsRackDao.saveOrUpdateX(plWmsRack);
    }

    public void deleteShop(String id) {
        String shopHql = "from PlShopWmsFloorArea f where f.parentId=?";
        List<PlShopWmsFloorArea> plShopWmsFloorAreaList = this.plShopWmsFloorAreaDao.find(shopHql, new Object[]{id});
        for (PlShopWmsFloorArea plShopWmsFloorArea : plShopWmsFloorAreaList) {
            deleteFloorArea(plShopWmsFloorArea.getId());
        }

    }

    public void deleteFloorArea(String id) {
        String floorareaHql = "update PlShopWmsFloorArea f set f.enabled =0 where f.id=?";
        this.plShopWmsFloorAreaDao.batchExecute(floorareaHql, new Object[]{id});
        String findHql = "from PlWmsRack f where f.parentId=?";
        List<PlWmsRack> plWmsRackList = this.plWmsRackDao.find(findHql, new Object[]{id});
        for (PlWmsRack plWmsRack : plWmsRackList) {
            deleteRack(plWmsRack.getId());
        }

    }


    public void deleteRack(String id) {
        String rackHql = "update PlWmsRack  set enabled = 0 where id=?";
        this.plWmsRackDao.batchExecute(rackHql, new Object[]{id});
        CacheManager.refreshWmsPlRackBindingRelationCache();
    }
    @Override
    public DataResult find(RequestPageData<?> request) {
        return plShopWmsViewDao.find(request);
    }

    @Override
    public Page<PlShopWmsView> findPage(Page<PlShopWmsView> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(PlShopWmsView entity) {

    }

    public void saveFa(PlShopWmsFloorArea plShopWmsFloorArea) {
        this.plShopWmsFloorAreaDao.saveOrUpdate(plShopWmsFloorArea);
    }

    public void saveRa(PlWmsRack plWmsRack) {
        this.plWmsRackDao.saveOrUpdate(plWmsRack);
    }

    @Override
    public PlShopWmsView load(String id) {
        return null;
    }

    @Override
    public PlShopWmsView get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<PlShopWmsView> find(List<PropertyFilter> filters) {
        return this.plShopWmsViewDao.find(filters);
    }

    @Override
    public List<PlShopWmsView> getAll() {
        return this.plShopWmsViewDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(PlShopWmsView entity) {

    }

    @Override
    public void delete(PlShopWmsView entity) {

    }

    @Override
    public void delete(String id) {

    }
}
