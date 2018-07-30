package com.casesoft.dmc.service.stock;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.rem.vo.codeDao;
import com.casesoft.dmc.dao.rem.vo.skuDao;
import com.casesoft.dmc.dao.rem.vo.styledDao;
import com.casesoft.dmc.dao.stock.EpcStockDao;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.rem.VO.code;
import com.casesoft.dmc.model.rem.VO.sku;
import com.casesoft.dmc.model.rem.VO.styled;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.tag.Epc;
import org.apache.poi.ss.formula.functions.T;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WingLi on 2015-06-02.
 */
@Service
@Transactional
public class EpcStockService extends AbstractBaseService<EpcStock, String> {
    @Autowired
    private EpcStockDao epcStockDao;
    @Autowired
    private skuDao skuDao;
    @Autowired
    private codeDao codeDao;
    @Autowired
    private styledDao styledDao;

    @Override
    public Page<EpcStock> findPage(Page<EpcStock> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(EpcStock entity) {

    }

    public List<EpcStock> findEpcStock(String storageId) {
        String hql = "from EpcStock epcStock where epcStock.warehouseId=? and epcStock.inStock=1";
        return this.epcStockDao.find(hql, new Object[]{storageId});
    }

    public void saveEpcStocks(List<EpcStock> stocks) {
        Session session = this.epcStockDao.getSession();
        for (int i = 0; i < stocks.size(); i++) {
            EpcStock epcStock = (EpcStock) session.merge(stocks.get(i));
            session.saveOrUpdate(epcStock);
            if ((i + 1) % stocks.size() == 0) {
                session.flush();
                session.clear();
            }
        }
    }

    public List<BillDtl> findEpcStockSKUByWharehousId(String warehouseId, String sku,
                                                      String ownerId, String styleId) {

      /*  String hql="select new com.casesoft.dmc.model.erp.BillDtl(epcstock.warehouseId,epcstock.sku,"
                + "epcstock.styleId,epcstock.colorId ,epcstock.sizeId ,count(*) ) "
                + " from EpcStock epcstock where  epcstock.inStock=1  ";
        if(CommonUtil.isNotBlank(warehouseId)){
            hql+="and  epcstock.warehouseId='"+warehouseId+"' ";
        }
        if(CommonUtil.isNotBlank(sku)){
            hql+=" and  epcstock.sku like'"+sku+"%' ";
        }
        if(CommonUtil.isNotBlank(ownerId)){
            hql+=" and  epcstock.ownerId='"+ownerId+"' ";
        }
        if(CommonUtil.isNotBlank(styleId)){
            hql+=" and  replace(epcstock.styleId,' ','') like '"+styleId+"%' ";

        }
        hql+=" group by epcstock.warehouseId,epcstock.sku ,epcstock.styleId,epcstock.colorId,epcstock.sizeId";*/
        String hql = "select new com.casesoft.dmc.model.erp.BillDtl(epcstock.warehId,epcstock.sku,"
                + "epcstock.styleId,epcstock.colorId ,epcstock.sizeId ,epcstock.qty ) "
                + " from DetailStockView epcstock where  epcstock.warehId is not null  ";
        if (CommonUtil.isNotBlank(warehouseId)) {
            hql += "and  epcstock.warehId='" + warehouseId + "' ";
        }
        if (CommonUtil.isNotBlank(sku)) {
            hql += " and  epcstock.sku like'" + sku + "%' ";
        }
        if (CommonUtil.isNotBlank(ownerId)) {
            hql += " and  epcstock.ownerId='" + ownerId + "' ";
        }
        if (CommonUtil.isNotBlank(styleId)) {
            hql += " and  replace(epcstock.styleId,' ','') like '" + styleId + "%' ";

        }
        return this.epcStockDao.find(hql, new Object[]{});
    }

    public void saveAll(List<EpcStock> entitys) {
        this.epcStockDao.doBatchInsert(entitys);
    }

    public List<EpcStock> findStock(String warehouse,String rackId,String levelId,String allocationId) {
        if (CommonUtil.isNotBlank(warehouse)) {
            String  hql = "";
            hql+="from EpcStock e where e.warehouseId=? and e.inStock=1 ";
            if(CommonUtil.isNotBlank(rackId)){
                hql+="and e.floorRack ='"+rackId+"' ";
            }
            if(CommonUtil.isNotBlank(levelId)){
                hql+="and e.floorArea ='"+levelId+"' ";
            }
            if (CommonUtil.isNotBlank(allocationId)){
                hql+="and e.floorAllocation='"+allocationId+"' ";
            }
            return this.epcStockDao.find(hql, new Object[]{warehouse});
        } else {
            return this.epcStockDao.find("from EpcStock e where e.inStock=1", new Object[]{});
        }
    }

    @Override
    public EpcStock load(String id) {
        return null;
    }

    @Override
    public EpcStock get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<EpcStock> find(List<PropertyFilter> filters) {
        return this.epcStockDao.find(filters);
    }

    public List<EpcStock> findEpcInCodes(String codes) {
        String hql = "from EpcStock epcstock where  epcstock.inStock=1 and ("
                + codes + ")";
        return this.epcStockDao.find(hql, new Object[]{});
    }


    public List<EpcStock> findEpcByCodes(String codeStr) {
        String hql = "from EpcStock epcstock where ("
                + codeStr + ")";
        return this.epcStockDao.find(hql, new Object[]{});
    }

    public List<EpcStock> findSaleReturnEpcByCodes(String codeStr) {
        String hql = "from EpcStock epcstock where ("
                + codeStr + ")";
        return this.epcStockDao.find(hql, new Object[]{});
    }

    public List<EpcStock> findEpcSaleReturnByCodes(String codeStr, String warehId) {
        String hql = "SELECT new com.casesoft.dmc.model.stock.EpcStock" +
                "(r.code,e.sku, e.styleId, e.colorId, e.sizeId, b.billNo as originBillNo, b.beginTime as lastSaleTime,e.floor,e.warehouseId) " +
                "FROM Record r,Business b,EpcStock e " +
                "WHERE (" + codeStr + ") " +
                "AND r.taskId=b.id " +
                "AND r.origId='" + warehId + "' " +
                "AND r.code=e.code " +
                "AND r.token=10 " +
                "ORDER BY r.scanTime DESC";
        return this.epcStockDao.find(hql, new Object[]{});
    }

    @Override
    public List<EpcStock> getAll() {
        return this.epcStockDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(EpcStock entity) {
        this.epcStockDao.update(entity);
    }

    @Override
    public void delete(EpcStock entity) {

    }

    @Override
    public void delete(String id) {

    }

    public void deleteAll() {
        this.epcStockDao.batchExecute("delete EpcStock b");
    }

    /**
     * 检查在库唯一吗信息
     * warehId:仓库
     * code：唯一码
     * isCheckWareHouse：是否检验仓库
     * */
    public EpcStock findEpcInCode(String warehId, String code,boolean isCheckWareHouse) {
        if(CacheManager.getCheckWarehhouse()) {
            return this.epcStockDao.findUnique("from EpcStock epcstock where inStock=1 and code=? and warehouseId=?", new Object[]{code, warehId});
        }else{
            if(isCheckWareHouse){
                return this.epcStockDao.findUnique("from EpcStock epcstock where inStock=1 and code=? and warehouseId=?", new Object[]{code, warehId});
            }else{
                return this.epcStockDao.findUnique("from EpcStock epcstock where inStock=1 and code=?", new Object[]{code});
            }

        }
    }
    /**
     * 检查不在库唯一吗信息
     * warehId:仓库
     * code：唯一码
     * isCheckWareHouse：是否检验仓库
     * */
    public EpcStock findEpcNotInCode(String warehId, String code,boolean isCheckWareHouse) {
        if(CacheManager.getCheckWarehhouse()) {
            return this.epcStockDao.findUnique("from EpcStock epcstock where inStock=0 and code=? and warehouse2Id=?", new Object[]{code, warehId});
        }else{
            if(isCheckWareHouse){
                return this.epcStockDao.findUnique("from EpcStock epcstock where inStock=0 and code=? and warehouse2Id=?", new Object[]{code, warehId});
            }else{
                return this.epcStockDao.findUnique("from EpcStock epcstock where inStock=0 and code=?", new Object[]{code});
            }
        }
    }

    public EpcStock findProductByCode(String code) {
        return this.epcStockDao.findUnique("from EpcStock where code=?", code);
    }

    /**查找在库多个唯一码**/
    public List<EpcStock> findEpcInCodes(String warehId, String codes) {
        if(CacheManager.getCheckWarehhouse()) {
            return this.epcStockDao.find("from EpcStock epcstock where epcstock.inStock=1  and " + codes + " and epcstock.warehouseId=?", new Object[]{warehId});
        }else{
            return this.epcStockDao.find("from EpcStock epcstock where epcstock.inStock=1  and " + codes);
        }
    }

    public List<EpcStock> findEpcCodes(String codes){
        return this.epcStockDao.find("from EpcStock epcstock where" + codes);
    }

    public List<EpcStock> findEpcNotInCodes(String warehId, String codes) {
        return this.epcStockDao.find("from EpcStock epcstock where epcstock.inStock=0  and " + codes + " and (epcstock.warehouse2Id=? or epcstock.warehouseId=?)", new Object[]{warehId, warehId});
    }

    public EpcStock findEpcAllowInCode(String warehId, String code) {
        return this.epcStockDao.findUnique("from EpcStock epcstock where code=? and (warehouse2Id=? or warehouseId = ?)", new Object[]{code, warehId, warehId});
    }

    public EpcStock findEpcAllowInCode(String code) {
        return this.epcStockDao.findUnique("from EpcStock epcstock where code=?", new Object[]{code});
    }

    /* *
     * @param code  唯一吗
     * @param warehId 仓库
     * @param type 库存状态 0,null 0标识校验不在库,null标识校验信息
     * 查询销售退货单入库唯一吗关联销售单据
     * */
    public List<EpcStock> findSaleReturnFilterByOriginIdDtl(String code, String warehId, Integer type) {
        String hql = "SELECT new com.casesoft.dmc.model.stock.EpcStock" +
                "(r.code,e.sku, e.styleId, e.colorId, e.sizeId, b.billNo as originBillNo, b.beginTime as lastSaleTime,e.floor,e.warehouseId) " +
                "FROM Record r,Business b,EpcStock e " +
                "WHERE r.taskId=b.id " +
                "AND r.code=e.code " +
                "AND r.code=? " +
                "AND r.origId=? ";
        if (CommonUtil.isNotBlank(type)) {
            hql += "AND e.inStock=" + type
                +" AND (e.warehouseId = r.origId or e.warehouse2Id = r.origId) ";
        }
        hql += "AND r.token=10 " +
                "ORDER BY r.scanTime DESC";
        return this.epcStockDao.find(hql, new Object[]{code, warehId});

    }

    /* *
     * @param code  唯一吗
     * @param warehId 仓库
     * @param type 库存状态 1,null 1标识校验在库,null标识校验信息
     * 查询销售退货单出库唯一吗关联销售单据
     * */
    public List<EpcStock> findSaleReturnFilterByDestIdDtl(String code, String warehId, int type) {
        String hql = "SELECT new com.casesoft.dmc.model.stock.EpcStock" +
                "(r.code,e.sku, e.styleId, e.colorId, e.sizeId, b.billNo as originBillNo, b.beginTime as lastSaleTime,e.floor,e.warehouseId) " +
                "FROM Record r,Business b,EpcStock e " +
                "WHERE r.taskId=b.id " +
                "AND r.code=e.code " +
                "AND r.code=? " +
                "AND r.destId=? ";
        if (CommonUtil.isNotBlank(type)) {
            hql += "AND e.inStock=" + type
                + " AND e.warehouseId = r.destId " ;
        }
        hql += "AND r.token=10 " +
                "ORDER BY r.scanTime DESC";
        return this.epcStockDao.find(hql, new Object[]{code, warehId});

    }


    public EpcStock findStockEpcByCode(String code) {
        String hql = "from EpcStock where code=?";
        return this.epcStockDao.findUnique(hql, code);
    }

    public Epc findTagEpcByCode(String code) {
        String hql = "from Epc where code=?";
        return this.epcStockDao.findUnique(hql, code);
    }

    public List<Epc> findTagEpcByCodes(String codes) {
        return this.epcStockDao.find("from Epc epc where  " + codes);
    }

    public List<EpcStock> findCodes(String warehId, String codes) {
        return this.epcStockDao.find("from EpcStock epcstock where  " + codes + " and epcstock.warehouseId=?", new Object[]{warehId});
    }

    public List<EpcStock> findAlertEpcInCodes(String warehId, String codes) {
        return this.epcStockDao.find("from EpcStock epcstock where epcstock.inStock=1 and (epcstock.dressingStatus <> 1 or epcstock.dressingStatus is null ) and " + codes + " and epcstock.warehouseId=?", new Object[]{warehId});
    }

    public List<EpcStock> findInStockEpcBySku(String sku, String warehouseId) {
        return this.epcStockDao.find("from EpcStock where instock=1 and sku=? and warehouseId=?", sku, warehouseId);
    }

    /**
     * epcStock调整入库
     *
     * @param code 唯一码
     */
    public void updateEpcStockIn(String code) {
        this.epcStockDao.batchExecute("update EpcStock set inStock=1 where code=?", code);
    }

    /**
     * epcStock调整出库
     *
     * @param code 唯一码
     */
    public void updateEpcStockOut(String code){
        this.epcStockDao.batchExecute("update EpcStock set inStock=0 where code=?", code);
    }


    /**
     * 销售退货单和寄售单，后台校验重复入库。根据epcList返回在库的epcStock。
     *
     * @param epcList epcList
     * @return epcStockList
     */
    public List<EpcStock> findInStockByEpcList(List<Epc> epcList) {
        List<String> codeList = new ArrayList<>();
        for (Epc epc : epcList) {
            codeList.add(epc.getCode());
        }
        String codeListStr = TaskUtil.getSqlStrByList(codeList, EpcStock.class, "code");
        return this.findEpcInCodes(codeListStr);
    }
    /*
    * lly
    * 重复代码有时间做封装
    * */
    public Page<sku> findskuByRm(String wareId, String rackId, String levelId, String allocationId,Page<sku> page){
        SQLQuery query =null;
        int startIndex = 0;
        int endIndex = page.getPageSize()*page.getPageNo();
        StringBuilder sql =new StringBuilder();
        Session session = this.epcStockDao.getSession();
        //sql.append("select A.*,ROWNUM from(");
        sql.append("select ''as rackName,'' as areaName,'' as allocationName,sku,styleId,colorId,sizeId,warehouseId,floorRack,floorArea,floorAllocation,count(sku) as qty\n");
        sql.append("from STOCK_EPCSTOCK\r\n");
        sql.append("where inStock =1\r\n");
        if (CommonUtil.isNotBlank(wareId)){
            sql.append("and warehouseId='"+wareId+"'\r\n");
        }
        if(CommonUtil.isNotBlank(rackId)){
            sql.append("and floorRack='"+rackId+"'\r\n");
        }
        if(CommonUtil.isNotBlank(levelId)){
            sql.append("and floorArea='"+levelId+"'\r\n");
        }
        if (CommonUtil.isNotBlank(allocationId)){
            sql.append("and floorAllocation='"+allocationId+"'\r\n");
        }
        sql.append("group by sku,styleId,colorId,sizeId,warehouseId,floorRack,floorArea,floorAllocation\n");
        //sql.append(") A\r\n");
        //sql.append("where ROWNUM>=:startIndex and ROWNUM<=:endIndex\r\n");
        /*if(page.getPageNo() ==1){
            startIndex = 0;
        }
        else{
            startIndex = page.getPageSize() * (page.getPageNo()-1);
        }
        query = session.createSQLQuery(sql.toString());
        query.setParameter("wareId",wareId);
        *//*query.setParameter("startIndex",startIndex);
        query.setParameter("endIndex",endIndex);*//*
        if(CommonUtil.isNotBlank(rackId)) {
            query.setParameter("rackId",rackId);
        }
        if(CommonUtil.isNotBlank(levelId)) {
            query.setParameter("levelId",levelId);
        }
        if(CommonUtil.isNotBlank(allocationId)) {
            query.setParameter("allocationId",allocationId);
        }*/
        Page<sku> pageBySQl = this.skuDao.findPageBySQl(page, sku.class, sql.toString(), null);
      /*  //跟sku映射
        Field[] fields = sku.class.getDeclaredFields();
        for (Field field : fields) {
            query.addScalar(field.getName());
        }
        query.setResultTransformer(Transformers.aliasToBean(sku.class));
        List<sku> skus = query.list();
        return skus;*/
      return  pageBySQl;
    }
    /*
    * lly
    * 重复代码有时间做封装
    * */
    public Page<code> findcodeByRm(String wareId, String rackId, String levelId, String allocationId,Page<code> page){
        SQLQuery query =null;
        int startIndex = 0;
        //int endIndex = page.getPageSize()*page.getPageNo();
        StringBuilder sql =new StringBuilder();
        //Session session = this.epcStockDao.getSession();
        //sql.append("select A.*,ROWNUM from(");
        sql.append("select ''as rackName,'' as areaName,'' as allocationName,code,sku,styleId,colorId,sizeId,warehouseId,floorRack,floorArea,floorAllocation,count(sku) as qty\n");
        sql.append("from STOCK_EPCSTOCK\r\n");
        sql.append("where inStock =1\r\n");
        if (CommonUtil.isNotBlank(wareId)){
            sql.append("and warehouseId='"+wareId+"'\r\n");
        }
        if(CommonUtil.isNotBlank(rackId)){
            sql.append("and floorRack='"+rackId+"'\r\n");
        }
        if(CommonUtil.isNotBlank(levelId)){
            sql.append("and floorArea='"+levelId+"'\r\n");
        }
        if (CommonUtil.isNotBlank(allocationId)){
            sql.append("and floorAllocation='"+allocationId+"'\r\n");
        }
        sql.append("group by code,sku,styleId,colorId,sizeId,warehouseId,floorRack,floorArea,floorAllocation\n");
        //sql.append(") A\r\n");
        //sql.append("where ROWNUM>=:startIndex and ROWNUM<=:endIndex\r\n");
        /*if(page.getPageNo() ==1){
            startIndex = 0;
        }
        else{
            startIndex = page.getPageSize() * (page.getPageNo()-1);
        }
        query = session.createSQLQuery(sql.toString());
        query.setParameter("wareId",wareId);
        *//*query.setParameter("startIndex",startIndex);
        query.setParameter("endIndex",endIndex);*//*
        if(CommonUtil.isNotBlank(rackId)) {
            query.setParameter("rackId",rackId);
        }
        if(CommonUtil.isNotBlank(levelId)) {
            query.setParameter("levelId",levelId);
        }
        if(CommonUtil.isNotBlank(allocationId)) {
            query.setParameter("allocationId",allocationId);
        }*/
        Page<code> pageBySQl = this.codeDao.findPageBySQl(page, code.class, sql.toString(), null);
      /*  //跟sku映射
        Field[] fields = sku.class.getDeclaredFields();
        for (Field field : fields) {
            query.addScalar(field.getName());
        }
        query.setResultTransformer(Transformers.aliasToBean(sku.class));
        List<sku> skus = query.list();
        return skus;*/
        return  pageBySQl;
    }
    /*
    * lly
    * 重复代码有时间做封装
    * */
    public Page<styled> findstyledByRm(String wareId, String rackId, String levelId, String allocationId, Page<styled> page){
        SQLQuery query =null;
        int startIndex = 0;
        //int endIndex = page.getPageSize()*page.getPageNo();
        StringBuilder sql =new StringBuilder();
        //Session session = this.epcStockDao.getSession();
        //sql.append("select A.*,ROWNUM from(");
        sql.append("select ''as rackName,'' as areaName,'' as allocationName,styleId,warehouseId,floorRack,floorArea,floorAllocation,count(sku) as qty\n");
        sql.append("from STOCK_EPCSTOCK\r\n");
        sql.append("where inStock =1\r\n");
        if (CommonUtil.isNotBlank(wareId)){
            sql.append("and warehouseId='"+wareId+"'\r\n");
        }
        if(CommonUtil.isNotBlank(rackId)){
            sql.append("and floorRack='"+rackId+"'\r\n");
        }
        if(CommonUtil.isNotBlank(levelId)){
            sql.append("and floorArea='"+levelId+"'\r\n");
        }
        if (CommonUtil.isNotBlank(allocationId)){
            sql.append("and floorAllocation='"+allocationId+"'\r\n");
        }
        sql.append("group by styleId,warehouseId,floorRack,floorArea,floorAllocation\n");
        //sql.append(") A\r\n");
        //sql.append("where ROWNUM>=:startIndex and ROWNUM<=:endIndex\r\n");
        /*if(page.getPageNo() ==1){
            startIndex = 0;
        }
        else{
            startIndex = page.getPageSize() * (page.getPageNo()-1);
        }
        query = session.createSQLQuery(sql.toString());
        query.setParameter("wareId",wareId);
        *//*query.setParameter("startIndex",startIndex);
        query.setParameter("endIndex",endIndex);*//*
        if(CommonUtil.isNotBlank(rackId)) {
            query.setParameter("rackId",rackId);
        }
        if(CommonUtil.isNotBlank(levelId)) {
            query.setParameter("levelId",levelId);
        }
        if(CommonUtil.isNotBlank(allocationId)) {
            query.setParameter("allocationId",allocationId);
        }*/
        Page<styled> pageBySQl = this.styledDao.findPageBySQl(page, styled.class, sql.toString(), null);
      /*  //跟sku映射
        Field[] fields = sku.class.getDeclaredFields();
        for (Field field : fields) {
            query.addScalar(field.getName());
        }
        query.setResultTransformer(Transformers.aliasToBean(sku.class));
        List<sku> skus = query.list();
        return skus;*/
        return  pageBySQl;
    }
    //查询总数
    public int count(String wareId, String rackId, String levelId, String allocationId){
        SQLQuery query =null;
        int count = 0;
        StringBuilder sql =new StringBuilder();
        Session session = this.epcStockDao.getSession();
        sql.append("select count(*) from (");
        sql.append("select A.*,ROWNUM from(");
        sql.append("select ''as rackName,'' as areaName,'' as allocationName,sku,styleId,colorId,sizeId,warehouseId,floorRack,floorArea,floorAllocation,count(sku) as qty\n");
        sql.append("from STOCK_EPCSTOCK\r\n");
        sql.append("where inStock =1\r\n");
        sql.append("and warehouseId=:wareId\r\n");
        if(CommonUtil.isNotBlank(rackId)){
            sql.append("and floorRack=:rackId\r\n");
        }
        if(CommonUtil.isNotBlank(levelId)){
            sql.append("and floorArea=:levelId\r\n");
        }
        if (CommonUtil.isNotBlank(allocationId)){
            sql.append("and floorAllocation=:allocationId\r\n");
        }
        sql.append("group by sku,styleId,colorId,sizeId,warehouseId,floorRack,floorArea,floorAllocation\n");
        sql.append(") A\r\n");
        sql.append(")");
        query = session.createSQLQuery(sql.toString());
        query.setParameter("wareId",wareId);
        if(CommonUtil.isNotBlank(rackId)) {
            query.setParameter("rackId",rackId);
        }
        if(CommonUtil.isNotBlank(levelId)) {
            query.setParameter("levelId",levelId);
        }
        if(CommonUtil.isNotBlank(allocationId)) {
            query.setParameter("allocationId",allocationId);
        }
        List<BigDecimal> ct = query.list();
        count = ct.get(0).intValue();
        return count;
    }
}
