package com.casesoft.dmc.service.factory;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.factory.FactoryInitDao;
import com.casesoft.dmc.model.factory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by GuoJunwen on 2017-05-09.
 */
@Service
@Transactional
public class FactoryInitService extends AbstractBaseService<FactoryInit,String> {

    @Autowired
    private FactoryInitDao factoryInitDao;

    public FactoryInit findInitByBillNo(String billNo){
        String hql="from FactoryInit f where f.billNo=?";
        return this.factoryInitDao.findUnique(hql,new Object[]{billNo});
    }

    public List<FactoryBill> findBillListByBillNos(String inBillNos) {
        return this.factoryInitDao.find("from FactoryBill factorybill where "+inBillNos);
    }

    public String findMaxNo(String prefix) {
        String hql = "select max(CAST(SUBSTRING(t.billNo,10),integer)) from FactoryInit t where t.billNo like ?";
        Integer code = this.factoryInitDao.findUnique(hql, prefix + '%');
        return code == null ? (prefix + "001") : prefix + CommonUtil.convertIntToString(code + 1, 3);
    }

    @Transactional(readOnly = true)
    public long findMaxNoBySkuNo(String sku) {
        String hql = "select max(detail.endNum) from FactoryBillDtl as detail where detail.sku=?";
        Number i = this.factoryInitDao.findUnique(hql, new Object[] { sku });
        return i == null ? 0 : i.longValue();
    }

    @Override
    public Page<FactoryInit> findPage(Page<FactoryInit> page, List<PropertyFilter> filters) {
        return this.factoryInitDao.findPage(page, filters);
    }

    @Override
    public void save(FactoryInit entity) {
        this.factoryInitDao.saveOrUpdate(entity);
    }
    public void save(FactoryBillInfoList factoryBillInfoList, FactoryInit master) {
        if(master.getTotQty() != 0L){
			/*this.initDao.save(master);*/
            this.factoryInitDao.doBatchInsert(factoryBillInfoList.getFactoryList());
            this.factoryInitDao.doBatchInsert(factoryBillInfoList.getInitList());
            this.factoryInitDao.doBatchInsert(factoryBillInfoList.getInitDtlList());
            if(CommonUtil.isNotBlank(factoryBillInfoList.getProductList())){
                this.factoryInitDao.doBatchInsert(factoryBillInfoList.getProductList());
            }
            if(CommonUtil.isNotBlank(factoryBillInfoList.getStyleList())){
                this.factoryInitDao.doBatchInsert(factoryBillInfoList.getStyleList());
            }
            if(CommonUtil.isNotBlank(factoryBillInfoList.getColorList())){
                this.factoryInitDao.doBatchInsert(factoryBillInfoList.getColorList());
            }
            if(CommonUtil.isNotBlank(factoryBillInfoList.getSizeList())){
                this.factoryInitDao.doBatchInsert(factoryBillInfoList.getSizeList());
            }
            if(CommonUtil.isNotBlank(factoryBillInfoList.getCategoryList())){
                this.factoryInitDao.doBatchInsert(factoryBillInfoList.getCategoryList());
            }
        }
    }

    public List<FactoryBillDtl> findDtls(String billNo, String ownerId) {
        String hql = "from FactoryBillDtl dtl where dtl.uploadNo=? and dtl.ownerId=?";
        return this.factoryInitDao.find(hql, new Object[] { billNo, ownerId });
    }

    public List<FactoryBillDtl> findFactoryBillDtl(String billNo, String factoryBillNo, String ownerId) {
        String hql = "from FactoryBillDtl dtl where dtl.uploadNo=? and billNo = ? and dtl.ownerId=?";
        return this.factoryInitDao.find(hql, new Object[] { billNo,factoryBillNo, ownerId });
    }

    @Override
    public FactoryInit load(String id) {
        return null;
    }

    @Override
    public FactoryInit get(String propertyName, Object value) {
        return this.factoryInitDao.findUniqueBy(propertyName,value);
    }

    public FactoryInit findFactoryInit(String billNo, String factoryBillNo) {
        return this.factoryInitDao.findUnique("from FactoryInit where billNo=? and factoryBillNo=?", new Object[]{billNo,factoryBillNo});
    }


    @Override
    public List<FactoryInit> find(List<PropertyFilter> filters) {
        return this.factoryInitDao.find(filters);
    }

    @Override
    public List<FactoryInit> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(FactoryInit entity) {

    }


    public void updateStatus(FactoryInit entity) {
        this.factoryInitDao.update(entity);
        this.factoryInitDao.batchExecute("update FactoryBill set status=? where billNo=?  and uploadNo=?",new Object[]{entity.getStatus().toString(),entity.getFactoryBillNo(),entity.getBillNo()});
        this.factoryInitDao.batchExecute("update FactoryBillDtl set status=? where billNo=?  and uploadNo=?",new Object[]{entity.getStatus().toString(),entity.getFactoryBillNo(),entity.getBillNo()});
        if(entity.getStatus() == 2){
            this.factoryInitDao.batchExecute("update FactoryBill set printDate=? where billNo=?  and uploadNo=?"
                    , CommonUtil.getDateString(new Date(), "yyyy-MM-dd"),entity.getFactoryBillNo(),entity.getBillNo());
        }

    }

    public void update(List<InitEpc> epcList, FactoryInit master) {
        this.update(master);
        List<InitEpc> tempList = this.factoryInitDao.find("from InitEpc e where e.uploadNo=? and e.billNo=?",
                new Object[] { master.getBillNo(),master.getFactoryBillNo() });
        if (!CommonUtil.isBlank(epcList) && CommonUtil.isBlank(tempList)) {
            this.factoryInitDao.doBatchInsert(epcList);
        }
    }
    @Override
    public void delete(FactoryInit entity) {

    }

    public void delete(String billNo, String factoryBillNo) {
        this.factoryInitDao.batchExecute("delete FactoryInit where billNo=? and factoryBillNo=?", new Object[] {billNo,factoryBillNo});
        this.factoryInitDao.batchExecute("delete FactoryBill where billNo=? and uploadNo=?", new Object[]{ factoryBillNo,billNo});
        this.factoryInitDao.batchExecute("delete FactoryBillDtl where billNo=? and uploadNo=?", new Object[]{factoryBillNo,billNo});
        this.factoryInitDao.batchExecute("delete InitEpc where billNo=? and uploadNo=?", new Object[]{factoryBillNo,billNo});
    }

    public FactoryBill findUniqueFactoryBill(String billNo, String factoryBillNo) {
        return this.factoryInitDao.findUnique("from FactoryBill where billNo=? and uploadNo=?", new Object[] {factoryBillNo,billNo});
    }

    @Override
    public void delete(String id) {
        this.factoryInitDao.delete(id);
    }

    public List<InitEpc> findInitEpcList() {
        return this.factoryInitDao.find("select new InitEpc(epc.code,epc.billNo,epc.billDate,epc.endDate,epc.uploadNo,epc.ownerId,"
                +"epc.epc,epc.sku,epc.styleId,epc.colorId,epc.sizeId,epc.type,bill.totQty"+
                ")from InitEpc epc ,FactoryBill bill where epc.billNo = bill.billNo and epc.uploadNo = bill.uploadNo");
    }
}
