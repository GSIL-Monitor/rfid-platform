package com.casesoft.dmc.service.tag;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.ITag;
import com.casesoft.dmc.core.vo.TagFactory;
import com.casesoft.dmc.dao.tag.InitDao;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.tag.Init;
import com.casesoft.dmc.model.tag.InitDtl;
import com.casesoft.dmc.model.tag.PrintInfo;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class InitService extends AbstractBaseService<Init, String> {

    @Autowired
    private InitDao initDao;

    @Autowired
    private TaskService taskService;

    @Override
    @Transactional(readOnly = true)
    public Page<Init> findPage(Page<Init> page, List<PropertyFilter> filters) {

        return this.initDao.findPage(page, filters);
    }

    @Override
    public void save(Init entity) {
        this.initDao.save(entity);
        this.initDao.doBatchInsert(entity.getDtlList());
    }

    public void saveInportWithCode(Init init,List<Epc> epcs,List<InitDtl> initDtls){
        this.initDao.saveOrUpdateX(init);
        this.initDao.doBatchInsert(epcs);
        this.initDao.doBatchInsert(initDtls);
    }

    public void save(Init init, Business bus) throws Exception {
        this.update(init, init.getDtlList());
        this.taskService.save(bus);
    }

    @Transactional(readOnly = true)
    public long findMaxNoBySkuNo(String sku) {
        String hql = "select max(detail.endNum) from InitDtl as detail where detail.sku=?";
        Number i = this.initDao.findUnique(hql, new Object[]{sku});
        return i == null ? 0 : i.longValue();
    }

    /**
     * 查找与单据前缀匹配的最大单号
     *
     * @param prefix
     * @return
     */
    public String findMaxNo(String prefix) {
        String hql = "select max(CAST(SUBSTRING(t.billNo,10),integer))"
                + " from Init as t where t.billNo like ?";
        Integer code = this.initDao.findUnique(hql, prefix + '%');
        return code == null ? (prefix + "001") : prefix + CommonUtil.convertIntToString(code + 1, 3);
    }

    @Override
    @Transactional(readOnly = true)
    public Init load(String id) {
        return this.get("billNo", id);
    }

    @Transactional(readOnly = true)
    public Init loadWithDtl(String id) {
        Init init = this.load(id);
        List<InitDtl> dtlList = this.findDtls(init.getBillNo(), init.getOwnerId());
        init.setDtlList(dtlList);
        return init;
    }

    @Override
    @Transactional(readOnly = true)
    public Init get(String propertyName, Object value) {
        return this.initDao.findUniqueBy(propertyName, value);
    }

    @Deprecated
    public List<Epc> updateEpc(List<Epc> epcList, Init master) {
        if (master.getStatus() == 0) {// 未生成
            master.setStatus(1);// 设置已通知
            this.update(master);// 操作一次数据库
        }
        this.initDao.doBatchInsert(epcList);
        return epcList;
    }

    /**
     * 下载打印zip文件时，更新数据库状态
     *
     * @param epcList
     * @param master
     * @return
     */
    public List<Epc> update(List<Epc> epcList, Init master) {
        this.update(master);
        List<Epc> tempList = this.initDao.find("from Epc e where e.billNo=?",
                new Object[]{master.getBillNo()});
        if (!CommonUtil.isBlank(epcList) && CommonUtil.isBlank(tempList)) {
            this.initDao.doBatchInsert(epcList);
        }
        return epcList;
    }

    public void save(List<Epc> epcList, Init master) {
        this.save(master);
        this.initDao.doBatchInsert(epcList);
    }

    /**
     * 为更新epc的tid属性 2015-01-26
     *
     * @param epcList
     */
    public void updateEpcList(List<Epc> epcList) {
        this.initDao.doBatchInsert(epcList);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Init> find(List<PropertyFilter> filters) {
        return this.initDao.find(filters);
    }

    @Transactional(readOnly = true)
    public List<InitDtl> findDtls(String billNo, String ownerId) {
        String hql = "from InitDtl dtl where dtl.billNo=? and dtl.ownerId=?";
        return this.initDao.find(hql, new Object[]{billNo, ownerId});
    }

    @Transactional(readOnly = true)
    public List<InitDtl> findDtls(String billNo) {
        String hql = "from InitDtl dtl where dtl.billNo=?";
        return this.initDao.find(hql, new Object[]{billNo});
    }

    @Transactional(readOnly = true)
    public List<InitDtl> findInitDtl(String billNo) {
        String hql = "from InitDtl dtl where dtl.billNo=?";
        return this.initDao.find(hql, new Object[]{billNo});
    }

    public void saveListPrintInfo(List<PrintInfo> prList) {
        this.initDao.doBatchInsert(prList);
    }

    public void updateListPrintInfo(List<PrintInfo> prList) {
        for (PrintInfo p : prList) {
            savePrintInfo(p);
        }
    }

    public void savePrintInfo(PrintInfo printInfo) {
        this.initDao.saveOrUpdateX(printInfo);
    }

    public List<PrintInfo> findPrintInfo(String billNo, String sku) {
        String hql = "from PrintInfo p where p.billNo=? and p.sku=?";
        return this.initDao.find(hql, new Object[]{billNo, sku});
    }

    public int updateInitDtlEd(String billNo, String sku, Integer editStatus) {
        String hql = "update InitDtlDao d set d.editStatus=? where d.billNo=? and d.sku=?";
        return this.initDao.batchExecute(hql, editStatus, billNo, sku);
    }

    // /
    @Transactional(readOnly = true)
    public List<Epc> findEpcList(PropertyFilter filter) {
        return this.initDao.find(filter.getHql(), filter.getValues());
    }

    public List<Epc> findEpcList(String billNo) {
        return this.initDao.find("from Epc epc where epc.billNo=?", new Object[]{billNo});
    }

    public List<Epc> getAllEpc() {
        return this.initDao.find("from Epc epc", new Object[]{});
    }
    public List<Epc> getAllCurrentEpc()  {
        try {
            // String dateStr=CommonUtil .getDateString(new Date(),"yyyy-MM-dd 00:00:00");
            Date endDate = new Date();
            Calendar ca = Calendar.getInstance();
            String end = CommonUtil.getDateString(endDate, "yyyy-MM-dd HH:00:00");
            System.out.println("结束：" + end);
            ca.setTime(CommonUtil.converStrToDate(
                    CommonUtil.getDateString(endDate, "yyyy-MM-dd HH:00:00"),
                    "yyyy-MM-dd HH:mm:ss"));
            ca.add(Calendar.HOUR_OF_DAY, -1);
            String begin = CommonUtil.getDateString(ca.getTime(), "yyyy-MM-dd HH:00:00");
            System.out.println("开始：" + begin);
            try {
                 endDate = CommonUtil.converStrToDate(end,"yyyy-MM-dd HH:mm:ss");
                 Date beginDate=CommonUtil.converStrToDate(begin,"yyyy-MM-dd HH:mm:ss");
                return this.initDao.find("from Epc epc where epc.billNo in (select bill.billNo from Init" +
                        " bill where bill.billDate >? and bill.billDate<=?)", new Object[]{beginDate, endDate});
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public Epc findEpc(String code) {
        return this.initDao.findUnique("from Epc e where e.code=?", new Object[]{code});
    }

    public Epc findEpcByTid(String tid) {
        return this.initDao.findUnique("from Epc e where e.tid=?", new Object[]{tid});
    }

    public List<Epc> findEpcBySkuList(String sku){
        return this.initDao.find("from Epc e where e.sku=?",new Object[]{sku});
    }

    @Override
    public List<Init> getAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void update(Init entity) {
        this.initDao.update(entity);
    }

    @Override
    public void delete(Init entity) {
        this.initDao.delete(entity);
        this.initDao.createQuery("delete InitDtlDao d where d.billNo=?", new Object[]{entity.getBillNo()}).executeUpdate();
    }

    @Override
    public void delete(String id) {
        if (CommonUtil.isBlank(id)) {
            this.initDao.batchExecute("delete Init i");
            this.initDao.batchExecute("delete InitDtlDao i");
            this.initDao.batchExecute("delete Epc i");

            this.taskService.delete("");
        } else {
            this.initDao.delete(id);
        }
    }

    public void deleteNoStatus(String billNo) {
        this.initDao.batchExecute("delete Init i where i.billNo=?", new Object[]{billNo});
        this.initDao.batchExecute("delete InitDtl i where i.billNo=?", new Object[]{billNo});
    }

    public void deleteTemp(User user) {
        if (CommonUtil.isNotBlank(user)) {
            String storageId = user.getOwnerId();

            this.initDao.batchExecute("delete BusinessDtl dtl where dtl.taskId in " +
                    " (select bus.id from Business bus where bus.deviceId in" +
                    " (select d.code from Device d where d.storageId=?))", storageId);
            this.initDao.batchExecute("delete Business bus where bus.deviceId in" +
                    " (select d.code from Device d where d.storageId=?))", storageId);
            this.initDao.batchExecute("delete EpcStock e where e.warehouseId=?", storageId);
            this.initDao.batchExecute("delete Record r where r.deviceId in" +
                    " (select d.code from Device d where d.storageId=?)", storageId);
            this.initDao.batchExecute("delete SaleBillDtl dtl where dtl.billNo in " +
                    " (select bus.billNo from SaleBill bus where bus.ownerId=?)", storageId);

            this.initDao.batchExecute("delete SaleBill bus where bus.ownerId=?", storageId);
        }

    }

    public InitDao getInitDao() {
        return initDao;
    }

    public void setInitDao(InitDao initDao) {
        this.initDao = initDao;
    }

    @Override
    public <X> List<X> findAll() {
        // TODO Auto-generated method stub
        return null;
    }


    private Init convertToInit(Business bus, Bill bill) {
        Init init = this.loadWithDtl(bill.getId());
        init.setDetectTaskId(bus.getId());
        init.setDeliverNo(bill.getDeliverNo());
        init.setDetectTotQty(bus.getTotEpc());
        init.setStatus(new Integer(3));

        for (InitDtl dtl : init.getDtlList()) {
            for (BillDtl bDtl : bill.getDtlList()) {
                if (dtl.getSku().equals(bDtl.getSku())) {
                    dtl.setDetectQty(bDtl.getActQty());
                    break;
                }
            }
        }

        return init;
    }

    public int updateStatus(int status, String billNo) {
        String hql = "update Init i set i.status=? where i.billNo=?";
        return this.initDao.batchExecute(hql, status, billNo);
    }

    public void update(Init init, List<InitDtl> dtlList, Business bus) {
        if (CommonUtil.isBlank(bus.getRecordList())) {
            return;
        }
        Record r = bus.getRecordList().get(0);
        if (!r.getCode().contains("_")) {
            this.update(init, init.getDtlList());
            return;
        }
        try {
            List<Epc> epcList = new ArrayList<Epc>();
            for (Record record : bus.getRecordList()) {
                String[] codeStr = record.getCode().split("_");
                String uniqueCode = codeStr[0];//唯一码
                String tid = codeStr[1];
                ITag tag = TagFactory.getCurrentTag();
                tag.setUniqueCode(uniqueCode);
                tag.getEpc();
                String secretEpc = tag.getSecretEpc();
                String code2 = PropertyUtil.getValue("webservice") + uniqueCode;
                Epc epc = new Epc();
                epc.setCode(uniqueCode);
                epc.setEpc(secretEpc);
                epc.setDimension(code2);
                epc.setTid(tid);
                epc.setStyleId(tag.getStyleId());
                epc.setColorId(tag.getColorId());
                epc.setSizeId(tag.getSizeId());
                epc.setSku(tag.getSku());
                epc.setOwnerId(init.getOwnerId());
                epc.setBillNo(init.getBillNo());
                epcList.add(epc);
            }
            this.initDao.doBatchInsert(epcList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.update(init, dtlList);

    }

    public void update(Init init, List<InitDtl> initDtls) {
        this.initDao.update(init);
        this.initDao.doBatchInsert(initDtls);
    }

    public void updateEpcInfo(Epc epc) {
        this.initDao.saveOrUpdateX(epc);
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public void updateBill(Business bus, Bill bill) {

        Init init = convertToInit(bus, bill);
        // this.update(init, init.getDtlList());
        this.update(init, init.getDtlList(), bus);
    }

    /**
     * 将styleList保存
     *
     * @param styleList
     */
    public void saveList(List<Init> styleList) {
        this.initDao.doBatchInsert(styleList);
    }

}
