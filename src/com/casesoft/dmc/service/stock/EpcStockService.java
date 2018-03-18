package com.casesoft.dmc.service.stock;

import java.util.ArrayList;
import java.util.List;

import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.tag.Epc;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.stock.EpcStockDao;
import com.casesoft.dmc.model.stock.EpcStock;

/**
 * Created by WingLi on 2015-06-02.
 */
@Service
@Transactional
public class EpcStockService extends AbstractBaseService<EpcStock,String>{
	@Autowired
	private  EpcStockDao  epcStockDao;
    @Override
    public Page<EpcStock> findPage(Page<EpcStock> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(EpcStock entity) {

    }
	public List<EpcStock> findEpcStock(String storageId){
		String hql="from EpcStock epcStock where epcStock.warehouseId=? and epcStock.inStock=1";
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
        String hql="select new com.casesoft.dmc.model.erp.BillDtl(epcstock.warehId,epcstock.sku,"
                + "epcstock.styleId,epcstock.colorId ,epcstock.sizeId ,epcstock.qty ) "
                + " from DetailStockView epcstock where  epcstock.warehId is not null  ";
        if(CommonUtil.isNotBlank(warehouseId)){
            hql+="and  epcstock.warehId='"+warehouseId+"' ";
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
        return this.epcStockDao.find(hql,new Object[]{});
    }
    public void saveAll(List<EpcStock> entitys) {
    	this.epcStockDao.doBatchInsert(entitys);
    }
    public List<EpcStock> findStock(String warehouse){
    	if(CommonUtil.isNotBlank(warehouse)){
    		return this.epcStockDao.find("from EpcStock e where e.warehouseId=? and e.inStock=1", new Object[]{warehouse});    		
    	}else{
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
        return null;
    }
    public List<EpcStock> findEpcInCodes(String codes){
    	String hql = "from EpcStock epcstock where  epcstock.inStock=1 and ("
				+ codes + ")";
		return this.epcStockDao.find(hql, new Object[] {});   
		}


    public List<EpcStock> findEpcByCodes(String codeStr) {
        String hql = "from EpcStock epcstock where ("
                + codeStr + ")";
        return this.epcStockDao.find(hql, new Object[] {});
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


    public EpcStock findEpcInCode(String warehId, String code) {
        return this.epcStockDao.findUnique("from EpcStock epcstock where inStock=1 and code=? and warehouseId=?",new Object[]{code,warehId});
    }

    public EpcStock findEpcNotInCode(String warehId, String code) {
        return this.epcStockDao.findUnique("from EpcStock epcstock where inStock=0 and code=? and warehouse2Id=?",new Object[]{code,warehId});
    }

    public EpcStock findProductByCode(String code){
        return this.epcStockDao.findUnique("from EpcStock where code=?", code);
    }

    public List<EpcStock> findEpcInCodes(String warehId, String codes) {
        return this.epcStockDao.find("from EpcStock epcstock where epcstock.inStock=1  and "+ codes + " and epcstock.warehouseId=?",new Object[]{warehId});
    }

    public List<EpcStock> findEpcNotInCodes(String warehId, String codes) {
        return this.epcStockDao.find("from EpcStock epcstock where epcstock.inStock=0  and "+ codes + " and (epcstock.warehouse2Id=? or epcstock.warehouseId=?)",new Object[]{warehId,warehId});
    }

    public EpcStock findEpcAllowInCode(String warehId, String code) {
        return this.epcStockDao.findUnique("from EpcStock epcstock where code=? and (warehouse2Id=? or warehouseId = ?)",new Object[]{code,warehId,warehId});
    }

    public EpcStock findEpcAllowInCode( String code) {
        return this.epcStockDao.findUnique("from EpcStock epcstock where code=?",new Object[]{code});
    }

    public EpcStock findStockEpcByCode(String code){
        String hql = "from EpcStock where code=?";
        return this.epcStockDao.findUnique(hql, code);
    }

    public Epc findTagEpcByCode(String code){
        String hql = "from Epc where code=?";
        return this.epcStockDao.findUnique(hql, code);
    }
    public List<Epc> findTagEpcByCodes(String codes) {
        return this.epcStockDao.find("from Epc epc where  "+ codes );
    }
    public List<EpcStock> findCodes(String warehId, String codes) {
        return this.epcStockDao.find("from EpcStock epcstock where  "+ codes + " and epcstock.warehouseId=?",new Object[]{warehId});
    }
    public List<EpcStock> findAlertEpcInCodes(String warehId, String codes) {
        return this.epcStockDao.find("from EpcStock epcstock where epcstock.inStock=1 and (epcstock.dressingStatus <> 1 or epcstock.dressingStatus is null ) and "+ codes + " and epcstock.warehouseId=?",new Object[]{warehId});
    }

    public List<EpcStock> findInStockEpcBySku(String sku, String warehouseId) {
        return this.epcStockDao.find("from EpcStock where instock=1 and sku=? and warehouseId=?", sku, warehouseId);
    }

    /**
     * epcStock调整入库
     * @param code 唯一码
     */
    public void updateEpcStockIn(String code){
        this.epcStockDao.batchExecute("update EpcStock set inStock=1 where code=?", code);
    }

    /**
     * epcStock调整出库
     * @param code 唯一码
     */
    public void updateEpcStockOut(String code){
        this.epcStockDao.batchExecute("update EpcStock set inStock=0 where code=?", code);
    }


    /**
     * 销售退货单和寄售单，后台校验重复入库。根据epcList返回在库的epcStock。
     *
     * @param epcList epcList
     * @return  epcStockList
     */
    public List<EpcStock> findInStockByEpcList(List<Epc> epcList){
        List<String> codeList = new ArrayList<>();
        for (Epc epc:epcList) {
            codeList.add(epc.getCode());
        }
        String codeListStr = TaskUtil.getSqlStrByList(codeList, EpcStock.class, "code");
        return this.findEpcInCodes(codeListStr);
    }
}
