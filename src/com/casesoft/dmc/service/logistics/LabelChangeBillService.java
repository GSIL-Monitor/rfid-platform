package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.BillConvertUtil;
import com.casesoft.dmc.controller.product.StyleUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.logistics.LabelChangeBillDao;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.BillRecord;
import com.casesoft.dmc.model.logistics.LabelChangeBill;
import com.casesoft.dmc.model.logistics.LabelChangeBillDel;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Init;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.service.product.ProductService;
import com.casesoft.dmc.service.sys.impl.PricingRulesService;
import com.casesoft.dmc.service.tag.InitService;
import com.casesoft.dmc.service.task.TaskService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2018/5/9.
 */
@Service
@Transactional
public class LabelChangeBillService  extends AbstractBaseService<LabelChangeBill, String> {
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
        return this.labelChangeBillDao.findUniqueBy(propertyName,value);
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
    /*
     *保存标签改和标签初始化
     * by：czf
     */
    public MessageBox saveLabelChangeBill(User currentUser,LabelChangeBill labelChangeBill, List<LabelChangeBillDel> labelChangeBillDels,String userId,InitService initService,PricingRulesService pricingRulesService,ProductService productService){


            List<Style> listStyle=null;
            List<Product> listproduct=null;
            String newStylesuffix=null;
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
                labelChangeBill.setStatus(0);
                labelChangeBill.setBillDate(new Date());
                Map<String, Object> map = StyleUtil.newstyleidonlabelChangeBillDel(labelChangeBill, labelChangeBillDels,pricingRulesService,productService);
                 listStyle=( List<Style>) map.get("style");
                listproduct=( List<Product>)map.get("product");
                 newStylesuffix=(String)map.get("newStylesuffix");
                init = BillConvertUtil.labelcovertToTagBirth(taskId, labelChangeBillDels, initService, currentUser,prefix,newStylesuffix,labelChangeBill.getChangeType());

            }else{
                issave=false;
            }
            User curUser = CacheManager.getUserById(userId);
            BillConvertUtil.covertToLabelChangeBill( labelChangeBill,labelChangeBillDels,curUser);
            save(labelChangeBill,labelChangeBillDels,listStyle,listproduct,issave,init);
            return new MessageBox(true, "保存成功", labelChangeBill.getBillNo());

    }

    public List<LabelChangeBillDel> findBillDtl(String billNo){
        return this.labelChangeBillDao.find("from LabelChangeBillDel where billNo=?", new Object[]{billNo});
    }

    public List<BillRecord> getBillRecod(String billNo) {
        return this.labelChangeBillDao.find("from BillRecord where billNo=?", new Object[]{billNo});
    }

    public void cancel( LabelChangeBill labelChangeBill,Init init){
        labelChangeBill.setStatus(BillConstant.BillStatus.Cancel);
        init.setStatus(BillConstant.BillStatus.Cancel);
        this.labelChangeBillDao.saveOrUpdateX(labelChangeBill);
        this.labelChangeBillDao.saveOrUpdateX(init);
    }
    @Autowired
    private TaskService taskService;
    public  MessageBox saveBusinessout(LabelChangeBill labelChangeBill, List<LabelChangeBillDel> labelChangeBillDelList, Business business,Business businessIn)throws Exception{
        MessageBox messageBox = this.taskService.checkEpcStock(business);
        MessageBox messageBoxIn = this.taskService.checkEpcStock(businessIn);
        if(messageBox.getSuccess()&&messageBoxIn.getSuccess()){
            labelChangeBill.setStatus(BillConstant.BillStatus.End);
            this.labelChangeBillDao.saveOrUpdateX(labelChangeBill);
            this.labelChangeBillDao.doBatchInsert(labelChangeBillDelList);
            if (CommonUtil.isNotBlank(labelChangeBill.getBillRecordList())) {
                this.labelChangeBillDao.doBatchInsert(labelChangeBill.getBillRecordList());
            }
            this.taskService.save(business);
            this.taskService.save(businessIn);
        }
        return messageBox;
    }

    /**
     *
     * @param page
     * @param filters
     * @param billClass//单据的class
     * @param billDtlClass//单据详情的class
     * @param constructorParameter//构造函数的参数
     * @return
     */

    public  Page<LabelChangeBill> findNewPage(Page<LabelChangeBill> page, List<PropertyFilter> filters, Class<?> billClass, Class<?> billDtlClass,String constructorParameter){
        String hql= CommonUtil.hqlbyBillandBillDel(billClass, billDtlClass, filters, constructorParameter);
         page = this.labelChangeBillDao.findSqlPage(page, hql);

        return page;
    }


}
