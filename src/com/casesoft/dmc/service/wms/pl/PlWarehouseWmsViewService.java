package com.casesoft.dmc.service.wms.pl;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.wms.pl.PlWarehouseWmsFloorAreaDao;
import com.casesoft.dmc.dao.wms.pl.PlWarehouseWmsViewDao;
import com.casesoft.dmc.dao.wms.pl.PlWmsFloorDao;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.request.BaseService;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.model.wms.pl.PlWarehouseWmsFloorArea;
import com.casesoft.dmc.model.wms.pl.PlWarehouseWmsView;
import com.casesoft.dmc.model.wms.pl.PlWmsFloor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by GuoJunwen on 2017/4/5 0005.
 */
@Service
@Transactional
public class PlWarehouseWmsViewService extends BaseService<PlWarehouseWmsView,String> {
    @Autowired
    private PlWarehouseWmsViewDao plWarehouseWmsViewDao;
    @Autowired
    private PlWarehouseWmsFloorAreaDao plWarehouseWmsFloorAreaDao;
    @Autowired
    private PlWmsFloorDao plWmsFloorDao;

    private static String FlOOR="FL";
    private static String FlOORAREA="FA";

    @Override
    public DataResult find(RequestPageData<?> request) {

        return plWarehouseWmsViewDao.find(request);
    }
    public List<PlWarehouseWmsView> findViewByFaId(String id){
        String hql="from PlWarehouseWmsView f where f.floorAreaId=?";
        return  this.plWarehouseWmsViewDao.find(hql, new Object[]{id});
    }

    public List<PlWarehouseWmsView> findViewByFlId(String id){
        String hql="from PlWarehouseWmsView f where f.floorId=?";
        return  this.plWarehouseWmsViewDao.find(hql, new Object[]{id});
    }


    public Boolean findFlBarcode(String barcode,String shopId){
        String hql="select fv.floorBarcode from PlWarehouseWmsView fv where fv.floorAreaParentCode=?";
        List list= this.plWarehouseWmsViewDao.find(hql,new Object[]{shopId});
        return list.contains(barcode);
    }

    public Boolean findFaBarcode(String barcode,String shopId){
        String hql="select fv.floorAreaBarcode from PlWarehouseWmsView fv where fv.floorAreaParentCode=?";
        List list= this.plWarehouseWmsViewDao.find(hql,new Object[]{shopId});
        return list.contains(barcode);
    }

    public String findShopIdByFaId(String faId){
        String hql="select fv.floorAreaParentCode from PlWarehouseWmsView fv where fv.floorAreaId=?";
        return String.valueOf(this.plWarehouseWmsViewDao.find(hql, new Object[]{faId}).get(0));
    }

    public String findShopIdByFlId(String flId){
        String hql="select fv.floorAreaParentCode from PlWarehouseWmsView fv where fv.floorId=?";
        return String.valueOf(this.plWarehouseWmsViewDao.find(hql, new Object[]{flId}).get(0));
    }


    public String findMaxId(String table){
        String hql="";
        if (table.equals("fa")){
            hql="select max(CAST(SUBSTRING(f.id,"+(FlOORAREA.length()+1)+"),integer)) from PlWarehouseWmsFloorArea f";
            Integer id=this.plWarehouseWmsFloorAreaDao.findUnique(hql);
            return id==null?(FlOORAREA+"000001"):FlOORAREA+ CommonUtil.convertIntToString(id + 1, 6);
        }else {
            hql="select max(CAST(SUBSTRING(f.id,"+(FlOOR.length()+1)+"),integer)) from PlWmsFloor f";
            Integer id=this.plWmsFloorDao.findUnique(hql);
            return id==null?(FlOOR+"000001"):FlOOR+ CommonUtil.convertIntToString(id + 1, 6);
        }
    }

    public PlWarehouseWmsFloorArea findFaById(String id){
        String hql="from PlWarehouseWmsFloorArea w where w.id=?";
        return this.plWarehouseWmsFloorAreaDao.findUnique(hql, new Object[]{id});
    }

    public PlWmsFloor findFlById(String id){
        String hql="from PlWmsFloor w where w.id=?";
        return this.plWmsFloorDao.findUnique(hql,new Object[]{id});
    }


    public List<PlWarehouseWmsFloorArea> findfa() {
        String hql="from PlWarehouseWmsFloorArea fa where fa.enabled=?";
        return this.plWarehouseWmsFloorAreaDao.find(hql,new Object[]{true});
    }
    public List<PlWmsFloor> findfl() {
        String hql="from PlWmsFloor fl where fl.enabled=?";
        return this.plWmsFloorDao.find(hql,new Object[]{true});
    }

    public List<PlWarehouseWmsFloorArea> findFaByShopId(String shopId) {
        String hql="from PlWarehouseWmsFloorArea fa where fa.enabled=? and fa.parentCode=?";
        return this.plWarehouseWmsFloorAreaDao.find(hql,new Object[]{true,shopId});
    }
    public List<PlWmsFloor> findFlByFaId(String floorAreaId) {
        String hql="from PlWmsFloor fl where fl.enabled=? and fl.parentId=?";
        return this.plWmsFloorDao.find(hql,new Object[]{true,floorAreaId});
    }

    public void saveFa(PlWarehouseWmsFloorArea plWarehouseWmsFloorArea){
        this.plWarehouseWmsFloorAreaDao.saveOrUpdate(plWarehouseWmsFloorArea);
    }
    public void saveFl(PlWmsFloor plWmsFloor){
        this.plWmsFloorDao.saveOrUpdate(plWmsFloor);
    }


    public void deleteWarehouse(String id){
        String shopHql="from PlWarehouseWmsFloorArea f where f.parentId=?";
        List<PlWarehouseWmsFloorArea> plWarehouseWmsFloorAreaList=this.plWarehouseWmsFloorAreaDao.find(shopHql,new Object[]{id});
        for (PlWarehouseWmsFloorArea plWarehouseWmsFloorArea:plWarehouseWmsFloorAreaList){
            deleteFloorArea(plWarehouseWmsFloorArea.getId());
        }

    }

    public void deleteFloorArea(String id){
        String floorareaHql="update PlWarehouseWmsFloorArea f set f.enabled =0 where f.id=?";
        this.plWarehouseWmsFloorAreaDao.batchExecute(floorareaHql,new Object[]{id});
        String findHql="from PlWmsFloor f where f.parentId=?";
        List<PlWmsFloor> wmsFloorList=this.plWmsFloorDao.find(findHql,new Object[]{id});
        for (PlWmsFloor wmsFloor:wmsFloorList){
            deleteFloor(wmsFloor.getId());
        }

    }

    public void deleteFloor(String id){
        String floorHql="update PlWmsFloor f set f.enabled = 0 where f.id=?";
        this.plWmsFloorDao.batchExecute(floorHql, new Object[]{id});

    }

    @Override
    public Page<PlWarehouseWmsView> findPage(Page<PlWarehouseWmsView> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(PlWarehouseWmsView entity) {

    }

    @Override
    public PlWarehouseWmsView load(String id) {
        return null;
    }

    @Override
    public PlWarehouseWmsView get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<PlWarehouseWmsView> find(List<PropertyFilter> filters) {
        return this.plWarehouseWmsViewDao.find(filters);
    }

    @Override
    public List<PlWarehouseWmsView> getAll() {
        return this.plWarehouseWmsViewDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(PlWarehouseWmsView entity) {

    }

    @Override
    public void delete(PlWarehouseWmsView entity) {

    }

    @Override
    public void delete(String id) {

    }
}
