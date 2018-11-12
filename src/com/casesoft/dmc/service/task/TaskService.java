package com.casesoft.dmc.service.task;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.cache.SpringContextUtil;
import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.task.TaskDao;
import com.casesoft.dmc.dao.trace.RecordDao;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.task.*;
import com.casesoft.dmc.service.product.SendInventoryService;
import com.casesoft.dmc.service.stock.EpcStockService;
import com.casesoft.dmc.service.syn.IBillWSService;
import com.casesoft.dmc.service.tag.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class TaskService extends AbstractBaseService<Business, String> {

    @Autowired
    private TaskDao taskDao;
    @Autowired
    private RecordDao recordDao;

    @Autowired
    private InitService initService;

    @Autowired
    private EpcStockService epcStockService;// 在出入库时，增加对库存操作

    @Autowired
    private IBillWSService billWSService;

    @Override
    @Transactional(readOnly = true)
    public Page<Business> findPage(Page<Business> page,
                                   List<PropertyFilter> filters) {
        return this.taskDao.findPage(page, filters);
    }

    @Override
    public void save(Business bus) {
        long time1 = System.currentTimeMillis();

        if (bus.getToken().intValue() == 3) {// 检测
            saveKFBusiness(bus);
            return;
        }
        // 持久化Bill数据
        try {
           // saveBill(bus);
            saveBus(bus);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        long time2 = System.currentTimeMillis();
        logger.error("存储数据消耗时间：" + (time2 - time1));

        // 增加对库存的处理 wing 2014-01-21，去掉 2015-01-28
        // if (CommonUtil.isOutStock(bus.getToken())) {
        // this.stockService.outStock(bus);
        // } else if (CommonUtil.isInStock(bus.getToken())) {
        // this.stockService.inStock(bus);
        // }
    }

   /**
    * @param bus
    * web 页面保存任务
    * */
    public void webSave(Business bus) {
        long time1 = System.currentTimeMillis();

        if (bus.getToken().intValue() == 3) {// 检测
            saveKFBusiness(bus);
            return;
        }
        // 持久化Bill数据
        try {
           // saveWebBill(bus);
            if(!CacheManager.getCheckWarehhouse()){
                updateTaskRecordInfo(bus);
            }
            saveBus(bus);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        long time2 = System.currentTimeMillis();
        logger.error("存储数据消耗时间：" + (time2 - time1));
    }
    public void updateTaskRecordInfo(Business bus){
        if (CommonUtil.isNotBlank(bus.getRecordList())) {
            if(bus.getType() == Constant.TaskType.Outbound) {
                List<String> codeList = TaskUtil.getRecordCodes(bus.getRecordList());
                String codes = TaskUtil.getSqlStrByList(codeList, EpcStock.class, "code");
                // 未入库的
                List<EpcStock> list = epcStockService.findEpcCodes(codes);
                Map<String, EpcStock> stockMap = new HashMap<>();
                for (EpcStock e : list) {
                    stockMap.put(e.getCode(), e);
                }
                for(Record r: bus.getRecordList()){
                    r.setOrigId(stockMap.get(r.getCode()).getWarehouseId());
                }
            }

        }
    }
    private void saveWebBill(Business bus) {
        Bill bill = bus.getBill();
        if (CommonUtil.isNotBlank(bill)) {
            if (bus.getToken().intValue() == 3) {
                initService.updateBill(bus, bill);
            } else {
                this.taskDao.saveOrUpdateX(bus.getBill());
                this.taskDao.doBatchInsert(bus.getBill().getDtlList());
            }

        }
    }
    /*
     * 未入库
     */
    public List<Record> getNotInRecord(String codes, String origId, int token) {
        String hql = "from Record e where e.code  in(" + codes
                + ") and e.origId=? and e.token=? )";
        return this.taskDao.find(hql, new Object[]{origId, token});
    }

    public List<BusinessDtl> findDtl(String taskId) {
        String hql = "from BusinessDtl dtl where dtl.taskId=?";
        return this.taskDao.find(hql, new Object[]{taskId});
    }

    @Transactional(readOnly = true)
    public List<Business> findTransferInBusiness(String origId) {
        String hql = "from Business b where b.status<? and b.destId=? and (b.token=? or b.token=?)";

        return this.taskDao.find(hql, new Object[]{
                Constant.TaskStatus.Confirmed, origId,
                Constant.Token.Storage_Transfer_Outbound,
                Constant.Token.Shop_Transfer_Outbound});
    }

    public void saveKFBusiness(Business bus) {
        Bill bill = bus.getBill();
        if (bill != null) {
            initService.updateBill(bus, bill);
        }

        List<BusinessDtl> dtlList = bus.getDtlList();
        List<Box> boxList = bus.getBoxList();
        if (CommonUtil.isNotBlank(boxList)) {
            for (Box box : boxList) {
                List<BoxDtl> boxDtls = box.getBoxDtls();
                this.taskDao.doBatchInsert(boxDtls);
            }
            this.taskDao.doBatchInsert(boxList);
        }
        List<Record> recordList = bus.getRecordList();

        this.taskDao.save(bus);

        this.taskDao.doBatchInsert(dtlList);

        if (CommonUtil.isNotBlank(recordList)) {
            if (recordList.get(0).getCode().contains("_")) {
                List<Epc> initEpcList = this.initService.findEpcList(bill
                        .getId());
                for (Record r : recordList) {
                    String[] code_tid = r.getCode().split("_");
                    r.setCode(code_tid[0]);

                    for (Epc epc : initEpcList) {
                        if (epc.getCode().trim().equals(code_tid[0])) {
                            epc.setTid(code_tid[1]);
                            break;
                        }
                    }
                }
                this.initService.updateEpcList(initEpcList);

            }
            this.taskDao.doBatchInsert(recordList);
        }
    }

    private void saveBill(Business bus) throws Exception {
        Bill bill = bus.getBill();
        if (bill != null) {
            if (bus.getToken().intValue() == 3) {
                initService.updateBill(bus, bill);
            } else {
                MessageBox msg = billWSService.uploadTaskToErp(bus);
                if (msg.getSuccess()) {
                    this.taskDao.saveOrUpdateX(bus.getBill());
                    this.taskDao.doBatchInsert(bus.getBill().getDtlList());
                }
            }

        }


    }

    private void saveBus(Business bus) throws Exception {
        List<BusinessDtl> dtlList = bus.getDtlList();
        List<Box> boxList = bus.getBoxList();
        if (CommonUtil.isNotBlank(boxList)) {
            for (Box box : boxList) {
                List<BoxDtl> boxDtls = box.getBoxDtls();
                this.taskDao.doBatchInsert(boxDtls);
            }
            this.taskDao.doBatchInsert(boxList);
        }
        List<Record> recordList = bus.getRecordList();

        this.taskDao.saveOrUpdate(bus);

        this.taskDao.doBatchInsert(dtlList);

        if (CommonUtil.isNotBlank(recordList)) {
            if (bus.getToken().intValue() != Constant.Token.Shop_Inventory
                    && bus.getToken().intValue() != Constant.Token.Storage_Inventory) {
                long version = CacheManager.getMaxEpcstockVersionId()+1;
                List<EpcStock> list = TaskUtil
                        .recordConvertEpsStock(recordList);
                List<String> codeList = new ArrayList<>();
                for(EpcStock e : list){
                    e.setVersion(version);
                    codeList.add(e.getCode());
                }
                CacheManager.setEpcstockVersionId(version);
                this.epcStockService.saveEpcStocks(list);
                if(bus.getToken().intValue() == Constant.Token.Storage_Inbound){
                    //采购入库更新tag_epc中唯一吗状态
                    this.initService.updateEpcStatus(CommonUtil.getSqlStrByList(codeList,Epc.class,"code"));
                }
            }
            this.taskDao.doBatchInsert(recordList);
        }
        //读取congif.properties文件
        boolean is_wxshop = Boolean.parseBoolean(PropertyUtil
                .getValue("is_wxshop"));
        if(CommonUtil.isNotBlank(is_wxshop)){
            if(is_wxshop){
                SendInventoryService sendInventoryService = (SendInventoryService) SpringContextUtil.getBean("sendInventoryService");
                logger.error("TaskService中SendInventoryService的对象"+sendInventoryService);
                sendInventoryService.WxChatStockUpdate(dtlList);
            }
        }


    }

    public void saveList(List<Business> busList) throws Exception {
        for (Business bus : busList) {
            save(bus);
        }
    }

    // john 添加
    @Transactional(readOnly = true)
    public List<Business> getQueryBussiness(String ids) {
        String hql = "from Business b where b.id in (" + ids + ")";
        return this.taskDao.find(hql, new Object[]{});
    }

    @Transactional(readOnly = true)
    public List<Business> ListChartBusiness(String ownerIds, Integer token) {
        String hql = "from Bussiness b where b.token =" + token
                + " and b.ownerId in (" + ownerIds + ")";
        return this.taskDao.find(hql, new Object[]{});
    }

    @Transactional(readOnly = true)
    public List<Business> ListChartBusiness(Integer token) {
        String hql = "from Business b where b.token=?";
        return this.taskDao.find(hql, new Object[]{token});
    }

    @Transactional(readOnly = true)
    public List<Business> findSale(String ownerId, Date startTime,
                                   Date endTime, int token) {
        String hql = "from Business b  where b.beginTime>? and b.beginTime<? and b.ownerId=? and b.token=?";
        return this.taskDao.find(hql, new Object[]{startTime, endTime,
                ownerId, token});
    }

    @Transactional(readOnly = true)
    public List<BusinessDtl> findSaleScore(String codes, int length, boolean asc) {
        String hql = "select new com.casesoft.dmc.model.task.BusinessDtl(s.code,s.styleNo,s.colorNo,"
                + "s.sizeNo,sum(s.count) as sum_count) from Score s where s.code in("
                + codes
                + ") group by s.code,s.styleNo,s.colorNo,s.sizeNo order by sum(s.count)";
        if (!asc) {
            hql = hql + " desc";
        }
        return this.taskDao.findInLength(hql, length, new Object[]{});
    }

    @Transactional(readOnly = true)
    public List<BusinessDtl> getBusinessDtls(String taskIds, int token) {
        String hql = "from BusinessDtl b where b.taskId in (" + taskIds
                + ") and token=?";
        return this.taskDao.find(hql, new Object[]{token});
    }

    // update
    public int updateBox(String oldTaskId, String taskId) {
        String hql = "update Box b set b.srcTaskId=b.taskId,b.taskId='"
                + taskId + "' where b.taskId  in(" + oldTaskId + ")";
        return this.taskDao.batchExecute(hql);
    }

    public int updateBoxDtl(String oldTaskId, String taskId) {
        String hql = "update BoxDtl b set b.srcTaskId=b.taskId, b.taskId='"
                + taskId + "' where b.taskId in (" + oldTaskId + ")";
        return this.taskDao.batchExecute(hql);
    }

    public int updateBusinessDtl(String oldTaskId, String taskId) {
        String hql = "update BusinessDtl  b set b.srcTaskId=b.taskId, b.taskId='"
                + taskId + "' where b.taskId in (" + oldTaskId + ")";
        return this.taskDao.batchExecute(hql);
    }

    public int updateRecord(String oldTaskId, String taskId) {
        String hql = "update Record r set r.srcTaskId=r.taskId, r.taskId='"
                + taskId + "' where r.taskId in (" + oldTaskId + ")";
        return this.taskDao.batchExecute(hql);
    }

    public boolean updateAll(List<Business> bList, Business bs) {
        boolean status = true;
        String taskId = bs.getId();
        String oldTaskId = "'";
        for (Business b : bList) {
            oldTaskId += b.getId() + "','";
        }
        oldTaskId = oldTaskId.substring(0, oldTaskId.length() - 2);
        if (0 == updateBusinessDtl(oldTaskId, taskId)
                || 0 == updateBox(oldTaskId, taskId)
                || 0 == updateBoxDtl(oldTaskId, taskId)
                || 0 == updateRecord(oldTaskId, taskId)) {
            status = false;
        }
        this.taskDao.save(bs);
        return status;
    }

    // Test专用
    public void restoreAll(String taskId) {
        String bhql = "delete Business b where b.id='" + taskId + "'";
        String blhql = "update BusinessDtl b set b.taskId=b.srcTaskId,b.srcTaskId='' where b.taskId='"
                + taskId + "'";
        String bxhql = "update Box b set b.taskId=b.srcTaskId,b.srcTaskId='' where b.taskId='"
                + taskId + "'";
        String bxlhql = "update BoxDtl b set b.taskId=b.srcTaskId,b.srcTaskId='' where b.taskId='"
                + taskId + "'";
        String rhql = "update Record b set b.taskId=b.srcTaskId,b.srcTaskId='' where b.taskId='"
                + taskId + "'";
        this.taskDao.batchExecute(bhql);
        this.taskDao.batchExecute(blhql);
        this.taskDao.batchExecute(bxhql);
        this.taskDao.batchExecute(bxlhql);
        this.taskDao.batchExecute(rhql);
    }

    // //////////

    /**
     * 获取某店试衣的top数量
     */
    public List<Object> countFittingQty(String shopId, int length, boolean asc) {
        String hql = "select dtl.ownerId,dtl.sku,dtl.styleId,dtl.colorId,dtl.sizeId,sum(dtl.qty) as sumQty"
                + " from BusinessDtl dtl where ownerId=? and token=20 "
                + "group by dtl.ownerId,dtl.sku,dtl.styleId,dtl.colorId,dtl.sizeId"
                + " order by sumQty";
        if (!asc)
            hql = hql + " desc";
        return this.taskDao.queryFitting(hql, length, shopId);

    }

    @Transactional(readOnly = true)
    public List<Record> findRecordList(PropertyFilter filter) {
        return this.taskDao.find(filter.getHql(), filter.getValues());
    }

    @Transactional(readOnly = true)
    public List<Record> findRecordList(List<PropertyFilter> filters) {
        return this.recordDao.find(filters);
    }

    @Transactional(readOnly = true)
    public List<Box> findBoxs(PropertyFilter filter) {
        return this.taskDao.find(filter.getHql(), filter.getValues());
    }

    @Transactional(readOnly = true)
    @Override
    public Business load(String id) {
        return get("id", id);
    }

    @Transactional(readOnly = true)
    @Override
    public Business get(String propertyName, Object value) {
        return this.taskDao.findUniqueBy(propertyName, value);
    }

    @Transactional(readOnly = true)
    public List<BusinessDtl> findBusinessDtl(String taskId) {
        String hql = "from BusinessDtl busdtl where busdtl.taskId=?";
        return this.taskDao.find(hql, new Object[]{taskId});
    }

    @Transactional(readOnly = true)
    public List<Record> findRecordByTask(String taskId) {
//        String hql = "from Record r where r.taskId=?";
//        return this.taskDao.find(hql, new Object[]{taskId});
        return this.billWSService.findRecordByTask(taskId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Business> find(List<PropertyFilter> filters) {
        return this.taskDao.find(filters);
    }

    @Transactional(readOnly = true)
    public List<BusinessDtl> findDtl(String taskId, Integer token) {
        String hql = "from BusinessDtl dtl where dtl.taskId=? and dtl.token=? order by sku";
        return this.taskDao.find(hql, new Object[]{taskId, token});
    }

    @Transactional(readOnly = true)
    public List<Box> findBoxs(Business bus) {
        String hql = "from  Box box where box.taskId=? and "
                + "box.ownerId=? and box.token=? order by box.seqNo";
        return this.taskDao.find(hql,
                new Object[]{bus.getId(), bus.getOwnerId(), bus.getToken()});
    }

    @Transactional(readOnly = true)
    public List<BoxDtl> findBoxDtls(Box box) {
        String hql = "from BoxDtl dtl where dtl.cartonId=? and dtl.taskId=? and"
                + " dtl.ownerId=?";
        return this.taskDao.find(
                hql,
                new Object[]{box.getCartonId(), box.getTaskId(),
                        box.getOwnerId()});
    }

    @Transactional(readOnly = true)
    public List<Business> findByDate(Date date1, Date date2) {
        String hql = "from Business bus where bus.beginTime between ? and ?";
        return this.taskDao.find(hql, new Object[]{date1, date2});
    }

    @Transactional(readOnly = true)
    public <X> List<X> findAll(PropertyFilter filter) {
        return this.taskDao.find(filter.getHql(), filter.getValues());
    }

    @Override
    public List<Business> getAll() {
        return null;
    }

    public TaskDao getTaskDao() {
        return taskDao;
    }

    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }


    @Override
    public void update(Business entity) {
        this.taskDao.update(entity);
    }

    public void update(Business entity, boolean autoUpload) {
        boolean success = true;
        if (autoUpload) {
            MessageBox msg = this.billWSService.updateUnitInfo(entity);
            success = msg.getSuccess();
        }
        if (success) {
            this.taskDao.update(entity);
            this.taskDao
                    .batchExecute(
                            "update BusinessDtl b set b.origId=?,b.destUnitId=?,b.destId=? "
                                    + " where b.taskId=?",
                            entity.getOrigId(),
                            entity.getDestUnitId(),
                            entity.getDestId(), entity.getId());
            this.taskDao.batchExecute(
                    "update Box b set b.origId=?,b.destUnitId=?,b.destId=? "
                            + "where b.taskId=?",
                    entity.getOrigId(),
                    entity.getDestUnitId(),
                    entity.getDestId(), entity.getId());
            this.taskDao.batchExecute(
                    "update BoxDtl b set b.origId=?,b.destUnitId=?,b.destId=? "
                            + " where b.taskId=?",
                    entity.getOrigId(),
                    entity.getDestUnitId(),
                    entity.getDestId(), entity.getId());
            this.taskDao.batchExecute(
                    "update Record b set b.origId=?,b.destUnitId=?,b.destId=? "
                            + " where b.taskId=?",
                    entity.getOrigId(),
                    entity.getDestUnitId(),
                    entity.getDestId(), entity.getId());
        }
    }

    @Override
    public void delete(Business entity) {
        List<String> ids = new ArrayList<>();
        ids.add(entity.getId());
        this.delete(ids);
    }

    /**
     * @param id
     * @param autoUpload
     */
    public void delete(String id, boolean autoUpload) {
        delete(id);
        if (autoUpload) {
            this.billWSService.delete(id);
        }
    }

    @Override
    public void delete(String id) {
        if (CommonUtil.isBlank(id)) {

            this.taskDao.batchExecute("delete Business b");
            this.taskDao.batchExecute("delete BusinessDtl b");
            this.taskDao.batchExecute("delete Box b");
            this.taskDao.batchExecute("delete BoxDtl b");
            this.taskDao.batchExecute("delete Record b");

            this.taskDao.batchExecute("delete InventoryDto b");


        } else {

            this.taskDao.batchExecute("delete Business b where b.id=?",
                    id);
            this.taskDao.batchExecute("delete BusinessDtl b where b.taskId=?",
                    id);
            this.taskDao.batchExecute("delete Box b where b.taskId=?",
                    id);
            this.taskDao.batchExecute("delete BoxDtl b where b.taskId=?",
                    id);
            this.taskDao.batchExecute("delete Record b where b.taskId=?",
                    id);
        }
    }

    public void delete(List<String> ids) {
        if (CommonUtil.isNotBlank(ids)) {
            String taskIdSql = CommonUtil.getSqlStrByList(ids, Business.class, "id");
            this.taskDao.batchExecute("delete Business business where " + taskIdSql);
            String dtlTaskIdSql = CommonUtil.getSqlStrByList(ids, BusinessDtl.class, "taskId");
            this.taskDao.batchExecute("delete BusinessDtl businessdtl where " + dtlTaskIdSql);
            String boxTaskIdSql = CommonUtil.getSqlStrByList(ids, Box.class, "taskId");
            this.taskDao.batchExecute("delete Box box where " + boxTaskIdSql);
            String boxDtlTaskIdSql = CommonUtil.getSqlStrByList(ids, BoxDtl.class, "taskId");
            this.taskDao.batchExecute("delete BoxDtl boxdtl where " + boxDtlTaskIdSql);
            String recordTaskIdSql = CommonUtil.getSqlStrByList(ids, Record.class, "taskId");
            this.taskDao.batchExecute("delete Record record where " + recordTaskIdSql);
            String billTaskIdSql = CommonUtil.getSqlStrByList(ids, Bill.class, "taskId");
            this.taskDao.batchExecute("delete BillDtl b where b.billNo in (select billNo from Bill bill where " + billTaskIdSql + ")");
            this.taskDao.batchExecute("delete Bill bill where " + billTaskIdSql);

        } else {
            this.taskDao.batchExecute("delete Business business");
            this.taskDao.batchExecute("delete BusinessDtl businessdtl");
            this.taskDao.batchExecute("delete Box box");

            this.taskDao.batchExecute("delete BoxDtl boxdtl");

            this.taskDao.batchExecute("delete Record record");

            this.taskDao.batchExecute("delete BillDtl b");
            this.taskDao.batchExecute("delete Bill bill");
        }

    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }


    public List<BusinessDtl> findDtlBySkus(String skus, int token) {
        String hql = "from BusinessDtl dtl where dtl.sku in (" + skus
                + ") and token=" + token;
        return this.taskDao.find(hql, new Object[]{});
    }

    public InitService getInitService() {
        return initService;
    }

    public void setInitService(InitService initService) {
        this.initService = initService;
    }

    /**
     * 2014-11-23 保存实时任务数据
     *
     * @param bus
     */
    public void saveCloudBusiness(CloudBusiness bus) {

        deleteCloudBusiness(bus.getId());

        List<CloudBusinessDtl> dtlList = bus.getCloudBusinessDtlList();
        List<CloudBox> boxList = bus.getCloudBoxList();
        if (CommonUtil.isNotBlank(boxList)) {
            for (CloudBox box : boxList) {
                List<CloudBoxDtl> boxDtls = box.getCloudBoxDtlList();
                this.taskDao.doBatchInsert(boxDtls);
            }
            this.taskDao.doBatchInsert(boxList);
        }
        List<CloudRecord> recordList = bus.getCloudRecordList();

        this.taskDao.saveOrUpdateX(bus);

        this.taskDao.doBatchInsert(dtlList);

        if (CommonUtil.isNotBlank(recordList)) {
            if (recordList.get(0).getCode().contains("_")) {
                for (Record r : recordList) {
                    r.setCode(r.getCode().split("_")[0]);
                }
            }
            this.taskDao.doBatchInsert(recordList);
        }

    }

    public void deleteCloudBusiness(String id) {
        this.taskDao.batchExecute("delete CloudBusiness b where b.id=?",
                id);
        this.taskDao.batchExecute("delete CloudBusinessDtl b where b.taskId=?",
                id);
        this.taskDao.batchExecute("delete CloudBox b where b.taskId=?",
                id);
        this.taskDao.batchExecute("delete CloudBoxDtl b where b.taskId=?",
                id);
        this.taskDao.batchExecute("delete CloudRecord b where b.taskId=?",
                id);
    }

    public List<CloudBusiness> findCloudBusinessList(PropertyFilter filter) {
        return this.taskDao.find(filter.getHql(), filter.getValues());
    }

    public List<CloudBox> findCloudBoxList(PropertyFilter filter) {
        return this.taskDao.find(filter.getHql(), filter.getValues());
    }

    public List<CloudBusinessDtl> findCloudBusinessDtlList(PropertyFilter filter) {
        return this.taskDao.find(filter.getHql(), filter.getValues());
    }

    public List<CloudBoxDtl> findCloudBoxDtlList(PropertyFilter filter) {
        return this.taskDao.find(filter.getHql(), filter.getValues());
    }

    public List<CloudRecord> findCloudRecordDtlList(PropertyFilter filter) {
        return this.taskDao.find(filter.getHql(), filter.getValues());
    }

    @Transactional
    public void save(Business bus, boolean autoUpload) {
        try {
            long time1 = System.currentTimeMillis();
            MessageBox msg = new MessageBox(true, "");
            if (autoUpload) {
                msg = this.billWSService.uploadTaskToErp(bus);

            }
            if (msg.getSuccess()) {
                if (bus.getToken().intValue() == 3) {// 检测
                    saveKFBusiness(bus);
                    return;
                }
                saveBus(bus);
                if(bus.getToken().intValue() == Constant.Token.Storage_Inventory){
                    saveBusBill(bus);
                }
                // saveBusBill(bus);

                // updateSrcTask(bus);
                long time2 = System.currentTimeMillis();
                logger.error("存储数据消耗时间：" + (time2 - time1));
            } else {
                //throw new Exception(msg.getMsg());
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        // 增加对库存的处理 wing 2014-01-21， 2015-01-28 不再统计库存
        // if (CommonUtil.isOutStock(bus.getToken())) {
        // this.stockService.outStock(bus);
        // } else if (CommonUtil.isInStock(bus.getToken())) {
        // this.stockService.inStock(bus);
        // }

    }

    private void saveBusBill(Business bus) {
        boolean saveLocalBillSuccess = false;
        Bill bill = bus.getBill();
        if (CommonUtil.isNotBlank(bill)) {

            System.out.println("保持Bill和BillDtl");
            bill.setStatus(1);
            bill.setSkuQty(new Long(bus.getBill().getDtlList().size()));
            List<BillDtl> billDtls = bill.getDtlList();
            if (CommonUtil.isNotBlank(bill.getRecords())) {
                this.taskDao.doBatchInsert(bill.getRecords());
                List<Record> records = this.recordDao.find(
                        "from Record r where r.taskId=?", new Object[]{bus
                                .getBill().getBillNo()});
                bill.setActQty((long) records.size());
                bill.setInitQty(bill.getActQty());
                bill.setScanQty(0l);
                Map<String, BillDtl> billMap = new HashMap<>();
                for (BillDtl dtl : billDtls) {
                    dtl.setScanQty(0l);
                    dtl.setInitQty(0l);
                    dtl.setActQty(0l);
                    billMap.put(dtl.getSku(), dtl);
                }
                for (Record r : records) {
                    BillDtl dtl = billMap.get(r.getSku());
                    if (CommonUtil.isNotBlank(dtl)) {
                        dtl.setActQty(dtl.getActQty() + 1);
                        dtl.setInitQty(dtl.getInitQty() + 1);
                    }
                }
            } else {
                for (BillDtl dtl : billDtls) {
                    dtl.setScanQty(0l);
                    dtl.setInitQty(dtl.getActQty());
                }
            }

            long totPreManualQty = 0l;
            if (CommonUtil.isNotBlank(billDtls)) {
                List<BillDtl> billDtlList = this.taskDao.find("from BillDtl d where d.billNo=?", new Object[]{bill.getBillNo()});
                this.taskDao.getSession().clear();
                if (CommonUtil.isNotBlank(billDtlList)) {
                    for (BillDtl dtl : billDtls) {
                        boolean isContained = false;
                        for (BillDtl orDtl : billDtlList) {
                            if (dtl.getSku().equals(orDtl.getSku())) {
                                if (CommonUtil.isBlank(orDtl.getPreManualQty())) {
                                    orDtl.setPreManualQty(0l);
                                }
                                if (CommonUtil.isBlank(dtl.getManualQty())) {
                                    dtl.setManualQty(0l);
                                }
                                dtl.setPreManualQty(orDtl.getPreManualQty().longValue() + dtl.getManualQty().longValue());
/*
                                totPreManualQty += dtl.getPreManualQty().longValue();
*/
                                dtl.setManualQty(0l);
                                isContained = true;
                            }
                        }
                        if (!isContained) {
                            dtl.setPreManualQty(dtl.getManualQty());
                        }
                    }
                } else {
                  /*  for (BillDtl dtl : billDtls) {
                        if (CommonUtil.isNotBlank(dtl.getManualQty())) {
*//*
                            totPreManualQty += dtl.getManualQty().longValue();
*//*
                            dtl.setManualQty(0l);
                        }
                    }*/
                }
                for (BillDtl orDtl : billDtls) {
                    if (CommonUtil.isBlank(orDtl.getPreManualQty())) {
                        orDtl.setPreManualQty(0l);
                    }
                    if (CommonUtil.isBlank(orDtl.getManualQty())) {
                        orDtl.setManualQty(0l);
                    }
                    totPreManualQty += orDtl.getPreManualQty().longValue();
                }
            }

            bill.setPreManualQty(totPreManualQty);
            bill.setManualQty(0l);
            Map<String, String> map = new HashMap<>();
            System.out.println(billDtls.size());
            if (bus.getToken().equals(16) || bus.getToken().equals(9)) {
                for (BillDtl dtl : billDtls) {
                    dtl.setId(bill.getBillNo() + "_" + dtl.getSku());
                    //System.out.println(dtl.getId());
                    if (map.containsKey(dtl.getId())) {

                    } else {
                        map.put(dtl.getId(), dtl.getId());

                    }
                }
            }

            this.taskDao.saveOrUpdateX(bill); // 客户要求不更新状态，该行是更新实际数量
            this.taskDao.doBatchInsert(billDtls);

            // saveBusinessStatus(bus.getBill());
        }

    }


    private void updateSrcTask(Business bus) {
        String srcTaskId = bus.getSrcTaskId();
        System.out.println("P:updateSrcTask:" + srcTaskId);
        if (CommonUtil.isNotBlank(srcTaskId)) {
            Business srcTask = this.taskDao.get(srcTaskId);
            if (CommonUtil.isNotBlank(srcTask)) {
                System.out.println("N:updateSrcTask:" + srcTaskId);
            }
            if (CommonUtil.isNotBlank(srcTask)) {
                srcTask.setStatus(Constant.TaskStatus.Confirmed);
                this.taskDao.update(srcTask);
            }
        }
    }

    public void updateTaskStorage(Business business) {
        this.taskDao.update(business);
        this.taskDao
                .batchExecute(
                        "update BusinessDtl dtl set dtl.origId=? where dtl.taskId=?",
                        business.getOrigId(),
                        business.getId());
        this.taskDao.batchExecute(
                "update Box box set box.origId=? where box.taskId=?",
                business.getOrigId(), business.getId());
        this.taskDao
                .batchExecute(
                        "update BoxDtl boxDtl set boxDtl.origId=? where boxDtl.taskId=?",
                        business.getOrigId(),
                        business.getId());
        this.taskDao.batchExecute(
                "update Record r set r.origId=? where r.taskId=?",
                business.getOrigId(), business.getId());
    }

    public void updateTaskStorage(Business business, boolean autoUpload) {
        boolean success = false;
        if (autoUpload) {
            MessageBox msg = this.billWSService.updateUnitInfo(business);
            success = msg.getSuccess();
        }
        if (success) {
            updateTaskStorage(business);
        }

    }

    /**
     * 统计某年每月的出入库量
     *
     * @param
     * @param tokens
     * @return
     */
    public List<Object[]> countMonthTaskQty(String ownerId, String tokens) {
        String hql = "select "
                + "sum(b.totStyle),sum(b.totSku),sum(b.totEpc),year(b.beginTime),"
                + "month(b.beginTime),b.token"
                + "  from Business b where b.ownerId=?  and b.token in ("
                + tokens
                + ") group by year(b.beginTime),month(b.beginTime),b.token "
                + " order by year(b.beginTime),month(b.beginTime),b.token";
        return this.taskDao.find(hql, new Object[]{ownerId});
    }

    public List<Object[]> countDayTaskQty(String ownerId, String tokens,
                                          Date startTime, Date endTime) {
        String hql = "select "
                + "sum(b.totStyle),sum(b.totSku),sum(b.totEpc),month(b.beginTime),	day(b.beginTime),b.token"
                + "  from Business b where b.ownerId=? and b.beginTime between ? and ? and b.token in ("
                + tokens
                + ") group by month(b.beginTime),day(b.beginTime),b.token"
                + " order by day(b.beginTime),b.token";
        return this.taskDao.find(hql, new Object[]{ownerId, startTime,
                endTime});
    }


    public List<EpcStock> getReturnInSaleStock(String codes, String origId) {
        String hql = "from EpcStock epcstock where epcstock.warehouseId=? and epcstock.token=? and epcstock.inStock=0 and ("
                + codes + ")";
        return this.taskDao.find(hql, new Object[]{origId,
                Constant.Token.Shop_Sales});
    }

    public List<EpcStock> getIn8And14Stock(String codes) {
        String hql = "from EpcStock epcstock where (" + codes + ")";
        return this.taskDao.find(hql, new Object[]{});
    }

    public List<EpcStock> getInStock(String codes, String origId) {

        String hql = "from EpcStock epcstock where  epcstock.inStock=1 and ("
                + codes + ")";
        if (CommonUtil.isNotBlank(origId)) {
            hql += " and epcstock.warehouseId='" + origId + "'";
        }
        return this.taskDao.find(hql, new Object[]{});
    }

    public List<EpcStock> getInStock(String codes, String warehouse1Id,
                                     String warehouse2Id) {
        String hql = "from EpcStock epcstock where epcstock.warehouse2Id=? and epcstock.inStock=0 and ("
                + codes + ")";
        return this.taskDao.find(hql, new Object[]{warehouse2Id});
    }

    public MessageBox checkEpcStock(Business bus) throws Exception {
        return this.billWSService.checkEpcStock(bus);
    }

    public MessageBox checkEpcStock(String uniqueCodeList, int token, String deviceId) {
        return this.billWSService.checkEpcStock(uniqueCodeList, token, deviceId);
    }

    public List<BusinessDtl> findBusinessDtlByTaskIds(String taskIdStr, String origSku) {
        String hql = "from BusinessDtl businessDtl where ("
                + taskIdStr + "and businessDtl.sku=" + origSku + ")";
        return this.taskDao.find(hql, new Object[]{});
    }

    public void deleteBSDtlByTaskIdAndSku(String taskId, String sku) {
        this.taskDao.batchExecute("delete from BusinessDtl where taskId=? and sku=?", taskId, sku);
    }

    public void saveBusDtlList(List<BusinessDtl> busDtlList) throws Exception {
        this.taskDao.doBatchInsert(busDtlList);
    }

    public String findBillNoByTaskId(String taskId) {
        return this.taskDao.findUnique("select billNo from Business where id=?", taskId);
    }

    public <T> List<T> findOrderDtlByBillNo(String billNo, Class<T> clazz) {
        return this.taskDao.find("from " + clazz.getName() + " where billNo=?", billNo);
    }

    public <T> void deleteBillDtlByBillNoAndSku(String billNo, String sku, Class<T> clazz) {
        this.taskDao.batchExecute("delete from " + clazz.getName() + " where billNo=? and sku=?", billNo, sku);
    }

    public <T> void saveBillDtlList(List<T> billDtlList) {
        this.taskDao.doBatchInsert(billDtlList);
    }

    public void updateInBillRecord(String newCode, String newSku, String origCode) {
        this.taskDao.batchExecute("update BillRecord set code=?, sku=? where code=?", newCode, newSku, origCode);
    }

    public void updateInTaskRecord(EpcStock newEpc, String origCode) {
        String newCode = newEpc.getCode();
        String newSku = newEpc.getSku();
        String newStyleId = newEpc.getStyleId();
        String newColorId = newEpc.getColorId();
        String newSizeId = newEpc.getSizeId();
        this.taskDao.batchExecute("update Record set code=?, sku=?, styleId=?, colorId=?, sizeId=? where code=?",
                newCode, newSku, newStyleId, newColorId, newSizeId, origCode);
    }

    public void updateInEpcStock(EpcStock newEpc, String origCode) {
        this.taskDao.batchExecute("update EpcStock set id=?, code=?, styleId=?, colorId=?, sizeId=?, sku=? where code=?",
                newEpc.getCode(), newEpc.getCode(), newEpc.getStyleId(), newEpc.getColorId(), newEpc.getSizeId(), newEpc.getSku(), origCode);
    }

    public List<Business> findBusinessByBillNo(String billNo) {
        return this.taskDao.findBy("billNo",billNo);
    }

    public List<Record> findRecordByTaskIdAndSku(String taskIdStr, String sku) {
        String hql = "from Record record where ("
                + taskIdStr + ") and sku like ?";
        return this.taskDao.find(hql, "%" + sku + "%");
    }

    public List<Record> findRecordByTaskIdAndType(String taskIdStr, Integer type) {
        String hql = "from Record record where ("
                + taskIdStr + ") and type=?";
        return this.taskDao.find(hql, type);
    }
}
