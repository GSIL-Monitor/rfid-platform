package com.casesoft.dmc.service.syn;

import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.syn.BillDao;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.tag.Init;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.service.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class BillService extends AbstractBaseService<Bill, String> {

	@Autowired
	private BillDao billDao;

	@Autowired
	private TaskService taskService;

	@Transactional(readOnly = true)
	@Override
	public Page<Bill> findPage(Page<Bill> page, List<PropertyFilter> filters) {
		return this.billDao.findPage(page, filters);
	}
	public Bill getBillByBillId(String id){
		Bill bill=this.billDao.findUnique("from Bill b where b.id=?", new Object[]{id});
		if(CommonUtil.isNotBlank(bill)){
			List<BillDtl> listDtls=this.billDao.find("from BillDtl d where d.billId=?",new Object[]{id});
			bill.setDtlList(listDtls);
        }
		return bill;
	}
	@Override
	public <X> List<X> findAll(PropertyFilter filter) {
		// TODO Auto-generated method stub
		String hql = filter.getHql();
		return this.billDao.find(hql, filter.getValues());
	}

	@Override
	public void save(Bill entity) {
		this.billDao.save(entity);
		if (CommonUtil.isNotBlank(entity.getDtlList())) {
			this.billDao.doBatchInsert(entity.getDtlList());
		}
	}

	public void save(List<Bill> entityList) {
		for (Bill bill : entityList) {
			if (CommonUtil.isNotBlank(bill.getDtlList())) {
				this.billDao.saveOrUpdate(bill);
				this.billDao.doBatchInsert(bill.getDtlList());
			}
		}
	}
	/**
	 * 金昔借口添加
	 * */
	public void save(List<Bill> entityList, List<Product> products, List<Init> inits,List<Epc> epcs) {
		for (Bill bill : entityList) {
			if (CommonUtil.isNotBlank(bill.getDtlList())) {
				this.billDao.save(bill);
				this.billDao.doBatchInsert(bill.getDtlList());
			}
		}
		for (Init bill : inits) {
			if (CommonUtil.isNotBlank(bill.getDtlList())) {
				this.billDao.saveOrUpdateX(bill);
				this.billDao.doBatchInsert(bill.getDtlList());
			}
		}
		this.billDao.doBatchInsert(epcs);
		this.billDao.doBatchInsert(products);
	}
	public List<Bill> findNoScanBill(String ownerId,String storageId,Date beginDate,Date endDate){
		String hql="from Bill b where  b.taskId is  null and b.unitId=? and (b.billDate between ? and ?)";
		return this.billDao.find(hql, new Object[]{storageId,beginDate,endDate});
	}
	/*
	 * 手动更新单据
	 */
	// /john 修改保存
	public void save(Bill bill, List<BillDtl> dtls) {

		if ("0".equals(bill.getBillType())) {
			bill.setSkuQty(new Long(dtls.size()));
			this.billDao.saveOrUpdate(bill);
			this.billDao.batchExecute("delete BillDtl dtl"
					+ " where dtl.billId=? and dtl.billNo=?", bill.getId(), bill.getBillNo());
		} else {
			this.save(bill);
		}
		this.billDao.doBatchInsert(dtls);
	}

	@Transactional(readOnly = true)
	@Override
	public Bill load(String id) {
		return this.billDao.get(id.trim());
	}

	public List<BillDtl> loadDtls(String id) {
		return this.billDao.find("from BillDtl dtl where dtl.billId=?", id);
	}

	@Override
	public Bill get(String propertyName, Object value) {
		return this.billDao.findUniqueBy(propertyName, value);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Bill> find(List<PropertyFilter> filters) {
		return this.billDao.find(filters);
	}

	@Override
	public List<Bill> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <X> List<X> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Bill entity) {
		this.billDao.update(entity);

	}

	/**
	 * 更新单据实际数量
	 * 
	 * @param bill
	 * @param dtls
	 */
	public void update(Bill bill, List<BillDtl> dtls) {
		bill.setSkuQty(new Long(dtls.size()));
		this.billDao.update(bill);
		this.billDao.doBatchInsert(dtls);
	}




	public void update(Business bus, Bill bill, List<BillDtl> dtls) {
		bill.setSkuQty(new Long(dtls.size()));
		this.taskService.update(bus);
		this.update(bill, dtls);
	}

	@Override
	public void delete(Bill entity) {

	}

    @Override
    public void delete(String id) {
        this.billDao.delete(id);
		this.billDao.batchExecute("delete BillDtl dtl where dtl.billId=?",
				id);
    }


    /**
	 * 根据ID删除某行详单记录
	 * 
	 * @param id
	 */
	public int deleteDtl(String id) {
		return this.billDao.batchExecute("delete BillDtl dtl where dtl.id=?",
				id);
	}

	public BillDao getBillDao() {
		return billDao;
	}

	public void setBillDao(BillDao billDao) {
		this.billDao = billDao;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}


	/**
	 * 查找与单据前缀匹配的最大单号
	 *
	 * @param prefix
	 * @return
	 */
	/*public String findMaxNo(String prefix) {
		String hql = "select max(CAST(SUBSTRING(t.billNo,10),integer))"
				+ " from Bill as t where t.billNo like ?";
		Integer code = this.billDao.findUnique(hql,prefix + '%');
		return code == null ? (prefix + "0001") : prefix
				+ CommonUtil.convertIntToString(code + 1, 4);
	}*/
    /*sqlService SUBSTRING(str,pos,length) 三个参数*/
    public String findMaxNo(String prefix) {
        String hql = "select max(CAST(SUBSTRING(t.billNo,?,4),integer))"
                + " from Bill as t where t.billNo like ?";
        Integer code = this.billDao.findUnique(hql,new Object[]{prefix.length()+1,prefix + '%'});
        return code == null ? (prefix + "0001") : prefix
                + CommonUtil.convertIntToString(code + 1, 4);
    }


    public MessageBox findInBillList(String storeCode, Date beginDate, Date endDate, Integer token, String shopCode) {
        List<Bill> billList = this.billDao.find("from Bill where destUnitId =? and type=? and status = 0 and billDate between ? and ?",new Object[]{shopCode,token,beginDate,endDate});
        return new MessageBox(true,"ok",billList);
    }
}
