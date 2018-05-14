package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.BillConvertUtil;
import com.casesoft.dmc.controller.product.StyleUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.logistics.LabelChangeBillDao;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.LabelChangeBill;
import com.casesoft.dmc.model.logistics.LabelChangeBillDel;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Init;
import com.casesoft.dmc.service.product.ProductService;
import com.casesoft.dmc.service.sys.impl.PricingRulesService;
import com.casesoft.dmc.service.tag.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


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
        return this.labelChangeBillDao.findPage(page,filters);
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

    public MessageBox saveLabelChangeBill(User currentUser,LabelChangeBill labelChangeBill, List<LabelChangeBillDel> labelChangeBillDels,String userId,InitService initService,PricingRulesService pricingRulesService,ProductService productService){

            Map<String, Object> map = StyleUtil.newstyleidonlabelChangeBillDel(labelChangeBill, labelChangeBillDels,pricingRulesService,productService);
            List<Style> listStyle=( List<Style>) map.get("style");
            List<Product> listproduct=( List<Product>)map.get("product");
            String newStylesuffix=(String)map.get("newStylesuffix");
            Init init=null;
            boolean issave=true;
            //2.保存数据，标签初始化
            if(CommonUtil.isBlank(labelChangeBill.getBillNo())){
                String prefix = BillConstant.BillPrefix.labelChangeBill
                        + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
                //String billNo = this.saleOrderBillService.findMaxBillNo(prefix);
              /*  User currentUser = (User) this.getSession().getAttribute(
                        Constant.Session.User_Session);*/
                String prefixTaskId = Constant.Bill.Tag_Init_Prefix
                        + CommonUtil.getDateString(new Date(), "yyMMdd");
                String taskId = initService.findMaxNo(prefixTaskId);
                labelChangeBill.setId(prefix);
                labelChangeBill.setBillNo(prefix);
                labelChangeBill.setBillDate(new Date());
                init = BillConvertUtil.labelcovertToTagBirth(taskId, labelChangeBillDels, initService, currentUser,prefix,newStylesuffix);

            }else{
                issave=false;
            }
            User curUser = CacheManager.getUserById(userId);
            BillConvertUtil.covertToLabelChangeBill( labelChangeBill,labelChangeBillDels,curUser);
            save(labelChangeBill,labelChangeBillDels,listStyle,listproduct,issave,init);
            return new MessageBox(true, "保存成功", labelChangeBill.getBillNo());

    }

}
