package com.casesoft.dmc.service.wms;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.wms.WmsFloorAreaDao;
import com.casesoft.dmc.dao.wms.WmsFloorDao;
import com.casesoft.dmc.dao.wms.WmsFloorViewDao;
import com.casesoft.dmc.dao.wms.WmsRackDao;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.model.wms.WmsFloor;
import com.casesoft.dmc.model.wms.WmsFloorArea;
import com.casesoft.dmc.model.wms.WmsFloorView;
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
public class WmsFloorViewService extends BaseService<WmsFloorView,String> {
    @Autowired
    private WmsFloorViewDao wmsFloorViewDao;
    @Autowired
    private WmsFloorAreaDao wmsFloorAreaDao;
    @Autowired
    private WmsFloorDao wmsFloorDao;
    @Autowired
    private WmsRackDao wmsRackDao;

    private static String FlOOR="FL";
    private static String FlOORAREA="FA";
    private static String RACK="RA";

    @Override
    public DataResult find(RequestPageData<?> request) {

        return wmsFloorViewDao.find(request);
    }
    public WmsFloorArea findFaBarcode(String barcode){
        return this.wmsFloorAreaDao.findUniqueBy("barcode",barcode);
    }
    public WmsFloor findFlBarcode(String barcode){
        return this.wmsFloorDao.findUniqueBy("barcode",barcode);
    }
    public WmsRack findRaBarcode(String barcode){
        return this.wmsRackDao.findUniqueBy("barcode",barcode);
    }

    public WmsFloorView findViewByRaId(String id){
        String hql="from WmsFloorView f where f.raId=?";
        return  this.wmsFloorViewDao.findUnique(hql, new Object[]{id});
    }

    public List<WmsFloorView> findViewByFlId(String id){
        String hql="from WmsFloorView f where f.flId=?";
        return  this.wmsFloorViewDao.find(hql, new Object[]{id});
    }

    public List<WmsFloorView> findViewByFaId(String id){
        String hql="from WmsFloorView f where f.faId=?";
        return  this.wmsFloorViewDao.find(hql, new Object[]{id});
    }

    public Boolean findRaBarcode(String barcode,String shopId){
       String hql="select fv.raBarcode from WmsFloorView fv where fv.faParentCode=?";
        List list= this.wmsFloorViewDao.find(hql,new Object[]{shopId});
        return list.contains(barcode);
    }

    public Boolean findFlBarcode(String barcode,String shopId){
        String hql="select fv.faBarcode from WmsFloorView fv where fv.faParentCode=?";
        List list= this.wmsFloorViewDao.find(hql,new Object[]{shopId});
        return list.contains(barcode);
    }

    public Boolean findFaBarcode(String barcode,String shopId){
        String hql="select fv.faBarcode from WmsFloorView fv where fv.faParentCode=?";
        List list= this.wmsFloorViewDao.find(hql,new Object[]{shopId});
        return list.contains(barcode);
    }

    public String findShopIdByFaId(String faId){
        String hql="select fv.faParentCode from WmsFloorView fv where fv.faId=?";
        return String.valueOf(this.wmsFloorViewDao.find(hql, new Object[]{faId}).get(0));
    }

    public String findShopIdByFlId(String flId){
        String hql="select fv.faParentCode from WmsFloorView fv where fv.flId=?";
        return String.valueOf(this.wmsFloorViewDao.find(hql, new Object[]{flId}).get(0));
    }

    public String findShopIdByRaId(String raId){
        String hql="select fv.faParentCode from WmsFloorView fv where fv.raId=?";
        return String.valueOf(this.wmsFloorViewDao.find(hql, new Object[]{raId}).get(0));
    }

    public String findMaxId(String table){
        String hql="";
        if (table.equals("fa")){
            hql="select max(CAST(SUBSTRING(f.id,"+(FlOORAREA.length()+1)+"),integer)) from WmsFloorArea f";
            Integer id=this.wmsFloorAreaDao.findUnique(hql);
            return id==null?(FlOORAREA+"000001"):FlOORAREA+ CommonUtil.convertIntToString(id + 1, 6);
        }else if(table.equals("fl")){
            hql="select max(CAST(SUBSTRING(f.id,"+(FlOOR.length()+1)+"),integer)) from WmsFloor f";
            Integer id=this.wmsFloorDao.findUnique(hql);
            return id==null?(FlOOR+"000001"):FlOOR+ CommonUtil.convertIntToString(id + 1, 6);
        }else{
            hql="select max(CAST(SUBSTRING(f.id,"+(RACK.length()+1)+"),integer)) from WmsRack f";
            Integer id=this.wmsRackDao.findUnique(hql);
            return id==null?(RACK+"000001"):RACK+ CommonUtil.convertIntToString(id + 1, 6);
        }
    }

    public WmsFloorArea findFaById(String id){
        String hql="from WmsFloorArea w where w.id=?";
        return this.wmsFloorAreaDao.findUnique(hql, new Object[]{id});
    }

    public WmsFloor findFlById(String id){
        String hql="from WmsFloor w where w.id=?";
        return this.wmsFloorDao.findUnique(hql,new Object[]{id});
    }

    public WmsRack findRaById(String id){
        String hql="from WmsRack w where w.id=?";
        return this.wmsRackDao.findUnique(hql,new Object[]{id});
    }

    public List<WmsFloorArea> findfa() {
        String hql="from WmsFloorArea fa where fa.enabled=?";
        return this.wmsFloorAreaDao.find(hql,new Object[]{true});
    }
    public List<WmsFloor> findfl() {
        String hql="from WmsFloor fl where fl.enabled=?";
        return this.wmsFloorDao.find(hql,new Object[]{true});
    }
    public List<WmsRack> findra() {
        String hql="from WmsRack ra where ra.enabled=?";
        return this.wmsRackDao.find(hql,new Object[]{true});
    }

    public List<WmsFloorArea> findFaByShopId(String shopId) {
        String hql="from WmsFloorArea fa where fa.enabled=? and fa.parentCode=?";
        return this.wmsFloorAreaDao.find(hql,new Object[]{true,shopId});
    }
    public List<WmsFloor> findFlByFaId(String floorAreaId) {
        String hql="from WmsFloor fl where fl.enabled=? and fl.parentId=?";
        return this.wmsFloorDao.find(hql,new Object[]{true,floorAreaId});
    }
    public List<WmsRack> findRaByFlId(String floorId) {
        String hql="from WmsRack ra where ra.enabled=? and ra.parentId=?";
        return this.wmsRackDao.find(hql,new Object[]{true,floorId});
    }

    @Override
    public Page<WmsFloorView> findPage(Page<WmsFloorView> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(WmsFloorView entity) {
    }

    public void saveFa(WmsFloorArea wmsFloorArea){
        this.wmsFloorAreaDao.saveOrUpdate(wmsFloorArea);
    }
    public void saveFl(WmsFloor wmsFloor){
        this.wmsFloorDao.saveOrUpdate(wmsFloor);
    }
    public void saveRa(WmsRack wmsRack ){
        this.wmsRackDao.saveOrUpdate(wmsRack);
    }
    @Override
    public WmsFloorView load(String id) {
        return null;
    }

    @Override
    public WmsFloorView get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<WmsFloorView> find(List<PropertyFilter> filters) {
        return this.wmsFloorViewDao.find(filters);
    }

    @Override
    public List<WmsFloorView> getAll() {
        return this.wmsFloorViewDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(WmsFloorView entity) {

    }

    @Override
    public void delete(WmsFloorView entity) {

    }

    @Override
    public void delete(String id) {

    }
}
