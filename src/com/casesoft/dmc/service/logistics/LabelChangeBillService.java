package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.logistics.LabelChangeBillDao;
import com.casesoft.dmc.model.logistics.LabelChangeBill;
import com.casesoft.dmc.model.logistics.LabelChangeBillDel;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.tag.Init;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by Administrator on 2018/5/9.
 */
@Service
@Transactional
public class LabelChangeBillService implements IBaseService<LabelChangeBill, String> {
    @Autowired
    private LabelChangeBillDao labelChangeBillDao;
    @Override
    public Page<LabelChangeBill> findPage(Page<LabelChangeBill> page, List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public void save(LabelChangeBill entity) {

    }

    @Override
    public LabelChangeBill load(String id) {
        return null;
    }

    @Override
    public LabelChangeBill get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<LabelChangeBill> find(List<PropertyFilter> filters) {
        return null;
    }

    @Override
    public List<LabelChangeBill> getAll() {
        return null;
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(LabelChangeBill entity) {

    }

    @Override
    public void delete(LabelChangeBill entity) {

    }

    @Override
    public void delete(String id) {

    }

    public void save(LabelChangeBill labelChangeBill, List<LabelChangeBillDel> labelChangeBillDels, List<Style> listStyle,List<Product> listproduct, boolean issave, Init init){
        this.labelChangeBillDao.batchExecute("delete from LabelChangeBillDel where billNo=?", labelChangeBill.getBillNo());
        this.labelChangeBillDao.batchExecute("delete from BillRecord where billNo=?", labelChangeBill.getBillNo());
        if(issave&& CommonUtil.isNotBlank(init)){
            this.labelChangeBillDao.dosaveBatchInsert(listStyle);
            this.labelChangeBillDao.dosaveBatchInsert(listproduct);
            this.labelChangeBillDao.saveOrUpdateX(init);
            this.labelChangeBillDao.doBatchInsert(init.getDtlList());

        }
        this.labelChangeBillDao.saveOrUpdate(labelChangeBill);
        this.labelChangeBillDao.doBatchInsert(labelChangeBillDels);
        if(CommonUtil.isNotBlank(labelChangeBill.getBillRecordList())){
            this.labelChangeBillDao.doBatchInsert(labelChangeBill.getBillRecordList());
        }
    }
}
