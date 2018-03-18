package com.casesoft.dmc.service.wms;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.wms.WmsFloorAreaDao;
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

import java.util.List;

/**
 * Created by john on 2017-03-08.
 */
@Service
@Transactional
public class WmsFloorAreaService extends BaseService<WmsFloorArea, String> {
    @Autowired
    private WmsFloorAreaDao wmsFloorAreaDao;

    @Override
    public DataResult find(RequestPageData<?> request) {
        return wmsFloorAreaDao.find(request);
    }

    @Override
    public Page<WmsFloorArea> findPage(Page<WmsFloorArea> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(WmsFloorArea entity) {

    }

    @Override
    public WmsFloorArea load(String id) {
        return null;
    }

    @Override
    public WmsFloorArea get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<WmsFloorArea> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<WmsFloorArea> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(WmsFloorArea entity) {

    }

    @Override
    public void delete(WmsFloorArea entity) {

    }

    @Override
    public void delete(String id) {
    }

    /**
     * @param id
     * @return 根据id加载库位
     */
    public WmsFloor loadWmsFloor(String id) {
        return this.wmsFloorAreaDao.findUnique("from WmsFloor floor where floor.id=?", new Object[]{id});
    }

    /**
     * @param barcode
     * @return 根据货架编号获取货架信息
     */
    public WmsRack loadWmsRackByBarcode(String barcode) {
        WmsRack wmsRack = this.wmsFloorAreaDao.findUnique("from WmsRack rack where rack.barcode=?", new Object[]{barcode});
        this.wmsFloorAreaDao.getSession().clear();
        return wmsRack;
    }

    public List<WmsRack> loadWmsRackByBarcodes(String warehouseCode, String barcodes) {
        List<WmsRack> wmsRacks = this.wmsFloorAreaDao.find("from WmsRack rack where rack.barcode in(" + barcodes + ") " +
                "and rack.id in(select orack.id from Unit unit,WmsFloorArea area,WmsFloor floor,WmsRack orack  " +
                "where orack.parentId=floor.id and floor.parentId=area.id and area.parentCode=unit.code and unit.code=?)", new Object[]{warehouseCode});
        this.wmsFloorAreaDao.getSession().clear();
        return wmsRacks;
    }

    /**
     * @param wmsRack 保存或更新货架
     */
    public void saveOrUpdateWmsRack(WmsRack wmsRack) {
        if (!wmsRack.getEnabled()) {
            this.wmsFloorAreaDao.batchExecute("delete WmsPlRackBindingRelation relation where relation.rackId=?", new Object[]{wmsRack.getId()});
        }
        this.wmsFloorAreaDao.saveOrUpdateX(wmsRack);
    }

    /**
     * @param warehouseCode
     * @param rackCode
     * @param skus          款+颜色，
     * @return 根据查询绑定信息
     * 不在设备所在仓库
     */
    public List<WmsPlRackBindingRelation> findWmsPlRelations(String warehouseCode, String rackCode, String skus) {
        StringBuffer hql = new StringBuffer("select new com.casesoft.dmc.extend.third.model.WmsPlRackBindingRelation(")
                .append(" relation.styleId,relation.colorId,rack.barcode)")
                .append(" from WmsPlRackBindingRelation relation,WmsRack rack ")
                .append(" where rack.id=relation.rackId and rack.barcode !=?  and CONCAT(relation.styleId,relation.colorId) in(")
                .append(skus).append(") and  rack.id in  (select rack.id from Unit unit,WmsFloorArea area,WmsFloor floor,WmsRack rack ")
                .append("where rack.parentId=floor.id and floor.parentId=area.id and area.parentCode=unit.code and unit.code=?)");

        return this.wmsFloorAreaDao.find(hql.toString(), new Object[]{rackCode, warehouseCode});
    }

    public List<WmsPlRackBindingRelation> findWmsPlRelations() {
        StringBuffer hql = new StringBuffer("select new com.casesoft.dmc.extend.third.model.WmsPlRackBindingRelation(").append(
                " relation.styleId,relation.colorId,")
                .append("rack.id,rack.barcode,rack.name,rack.image,rack.remark,")
                .append("floor.id,floor.barcode,floor.name,floor.image,floor.remark,")
                .append("area.id,area.barcode,area.name,area.image,area.remark,unit.code)")
                .append("from WmsPlRackBindingRelation relation,WmsFloor floor,WmsFloorArea area,Unit unit,WmsRack rack ")
                .append(" where ")
                .append(" relation.rackId=rack.id and rack.parentId=floor.id and floor.parentId=area.id and area.parentCode=unit.code");
        return this.wmsFloorAreaDao.find(hql.toString(), new Object[]{});
    }

    /**
     * @param warehouseCode
     * @param skus          款+颜色，
     * @return 根据查询绑定信息
     */
    public List<WmsPlRackBindingRelation> findWmsPlRelations(String warehouseCode, String skus) {
        StringBuffer hql = new StringBuffer("select new com.casesoft.dmc.extend.third.model.WmsPlRackBindingRelation(").append(
                " relation.styleId,relation.colorId,")
                .append("rack.id,rack.barcode,rack.name,rack.image,rack.remark,")
                .append("floor.id,floor.barcode,floor.name,floor.image,floor.remark,")
                .append("area.id,area.barcode,area.name,area.image,area.remark)").append(
                        "from WmsPlRackBindingRelation relation,WmsFloor floor,WmsFloorArea area,Unit unit,WmsRack rack ")
                .append(" where  CONCAT(relation.styleId,relation.colorId) in(")
                .append(skus).append(")")
                .append("and relation.rackId=rack.id and rack.parentId=floor.id and floor.parentId=area.id and area.parentCode=unit.code and unit.code=?");

        return this.wmsFloorAreaDao.find(hql.toString(), new Object[]{warehouseCode});
    }

    /**
     * @param warehouseCode
     * @param rackCodes
     * @return 根据查询货架绑定信息
     */
    public List<WmsPlRackBindingRelation> findWmsRackPlRelations(String warehouseCode, String rackCodes) {
        String ins = "";
        if (CommonUtil.isNotBlank(rackCodes)) {
            ins = CommonUtil.addQuotes(rackCodes.split(","));
        }
        if (ins.equals("")) {
            ins = "''";
        }
        StringBuffer hql = new StringBuffer("select new com.casesoft.dmc.extend.third.model.WmsPlRackBindingRelation( relation.styleId,relation.colorId,rack.barcode)")
                .append(" from WmsPlRackBindingRelation relation,WmsRack rack where relation.rackId in ")
                .append(" (select rack.id from Unit unit,WmsFloorArea area,WmsFloor floor,WmsRack rack ")
                .append("where rack.parentId=floor.id and floor.parentId=area.id and area.parentCode=unit.code and unit.code=? ")
                .append(" and rack.barcode in( ").append(ins).append(")) and rack.id=relation.rackId ");
        return this.wmsFloorAreaDao.find(hql.toString(), new Object[]{warehouseCode});
    }

    /**
     * @param rackCodes
     * @return 根据查询货架绑定记录信息
     */
    public List<WmsPlRackBindingRecord> findWmsRackBindingRecord(String rackCodes) {
        String ins = "";
        if (CommonUtil.isNotBlank(rackCodes)) {
            ins = CommonUtil.addQuotes(rackCodes.split(","));
        }
        if (ins.equals("")) {
            ins = "''";
        }
        StringBuffer hql = new StringBuffer("from WmsPlRackBindingRecord record ")
                .append(" where  record.rackBarcode in( ").append(ins).append(")) order by record.updateDate desc");
        return this.wmsFloorAreaDao.find(hql.toString(), new Object[]{});
    }

    public void unbindingSku(String ids) {
        StringBuffer hql = new StringBuffer("delete  WmsPlRackBindingRelation relation ")
                .append(" where relation.id in (").append(ids).append(")");
        this.wmsFloorAreaDao.batchExecute(hql.toString(), new Object[]{});
    }

    /**
     * @param wmsPlRackBindingRelations 添加绑定信息
     */
    public void doBatchWmsPlRackBindingRelation(List<WmsPlRackBindingRecord> records, List<WmsPlRackBindingRelation> wmsPlRackBindingRelations) {
        if (CommonUtil.isNotBlank(wmsPlRackBindingRelations)) {
            this.wmsFloorAreaDao.doBatchInsert(wmsPlRackBindingRelations);
            this.wmsFloorAreaDao.doBatchInsert(records);
        }
    }
}
