package com.casesoft.dmc.service.logistics;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.IBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.logistics.PurchaseBillOrderDao;
import com.casesoft.dmc.dao.logistics.PurchaseReturnBillDao;
import com.casesoft.dmc.dao.logistics.PurchaseReturnBillDtlDao;
import com.casesoft.dmc.dao.sys.UnitDao;

import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.service.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Session on 2017-06-29.
 */
@Service
@Transactional
public class PurchaseReturnBillService implements IBaseService<PurchaseReturnBill,String>{

	@Autowired
	private PurchaseReturnBillDao purchaseReturnBillDao;

	@Autowired
	private PurchaseReturnBillDtlDao purchaseReturnBillDtlDao;
	@Autowired
	private UnitDao unitDao;

	@Autowired
	private TaskService taskService;

	@Autowired
	private PurchaseBillOrderDao purchaseBillOrderDao;

	@Override
	public Page<PurchaseReturnBill> findPage(Page<PurchaseReturnBill> page, List<PropertyFilter> filters) {
		return this.purchaseReturnBillDao.findPage(page,filters);
	}

	@Override
	public void save(PurchaseReturnBill entity) {
		this.purchaseReturnBillDao.saveOrUpdate(entity);
	}


	public void cancel(PurchaseReturnBill entity) {
		String destId = entity.getDestUnitId();
		Unit unit = this.unitDao.get(destId);
		Double actPrice = entity.getActPrice();
		if(CommonUtil.isBlank(actPrice)){
			actPrice=0.0;
		}
		Double payPrice = entity.getPayPrice();
		if(CommonUtil.isBlank(payPrice)){
			payPrice=0.0;
		}
		Double owingValue = unit.getOwingValue()==null?0.0:unit.getOwingValue();
		unit.setOwingValue(owingValue-(actPrice-payPrice));
		this.unitDao.update(unit);
		this.purchaseReturnBillDao.saveOrUpdate(entity);
	}

	public void saveBatch(PurchaseReturnBill prb, List<PurchaseReturnBillDtl> prblst){
		this.purchaseReturnBillDao.batchExecute("delete from PurchaseReturnBillDtl pd where pd.billNo =?", prb.getBillNo());
		this.purchaseReturnBillDao.batchExecute("delete from BillRecord where billNo=?", prb.getBillNo());

		Double diffPrice = prb.getActPrice() - prb.getPayPrice();
		Double preDiffPrice = this.purchaseReturnBillDao.findUnique("select s.actPrice-s.payPrice from PurchaseReturnBill as s where s.billNo = ?", prb.getBillNo());
		String preDestUnitId = this.unitDao.findUnique("select destUnitId from PurchaseReturnBill as s where s.billNo = ?", prb.getBillNo());
		Unit unit = this.unitDao.findUnique("from Unit where id = ?",new Object[]{prb.getDestUnitId()});

		if (CommonUtil.isNotBlank(preDiffPrice) && CommonUtil.isNotBlank(preDestUnitId)){
			//this.purchaseReturnBillDao.batchExecute("update Unit set owingValue = owingValue - ? where id=?", preDiffPrice,preDestUnitId);
			unit.setOwingValue(unit.getOwingValue()-preDiffPrice);
		}
		Double owingValue = unit.getOwingValue()==null?0.0:unit.getOwingValue();
		unit.setOwingValue(owingValue+diffPrice);
		this.unitDao.update(unit);
		this.purchaseReturnBillDao.saveOrUpdate(prb);
		this.purchaseReturnBillDao.doBatchInsert(prblst);
		if(CommonUtil.isNotBlank(prb.getBillRecordList())){
			this.purchaseReturnBillDao.doBatchInsert(prb.getBillRecordList());
		}
	}

	public void saveBatch(PurchaseReturnBill prb, List<PurchaseReturnBillDtl> prblst,String PbillNo,String billNo){
		this.purchaseReturnBillDao.batchExecute("delete from PurchaseReturnBillDtl pd where pd.billNo =?", prb.getBillNo());
		this.purchaseReturnBillDao.batchExecute("delete from BillRecord where billNo=?", prb.getBillNo());

		Double diffPrice = prb.getActPrice() - prb.getPayPrice();
		Double preDiffPrice = this.purchaseReturnBillDao.findUnique("select s.actPrice-s.payPrice from PurchaseReturnBill as s where s.billNo = ?", prb.getBillNo());
		String preDestUnitId = this.unitDao.findUnique("select destUnitId from PurchaseReturnBill as s where s.billNo = ?", prb.getBillNo());
		Unit unit = this.unitDao.findUnique("from Unit where id = ?",new Object[]{prb.getDestUnitId()});

		if (CommonUtil.isNotBlank(preDiffPrice) && CommonUtil.isNotBlank(preDestUnitId)){
			//this.purchaseReturnBillDao.batchExecute("update Unit set owingValue = owingValue - ? where id=?", preDiffPrice,preDestUnitId);
			unit.setOwingValue(unit.getOwingValue()-preDiffPrice);
		}
		Double owingValue = unit.getOwingValue()==null?0.0:unit.getOwingValue();
		unit.setOwingValue(owingValue+diffPrice);
		this.unitDao.update(unit);
		prb.setStatus(2);
		prb.setReturnBillNo(PbillNo);
		this.purchaseReturnBillDao.saveOrUpdate(prb);
		this.purchaseReturnBillDao.doBatchInsert(prblst);
		if(CommonUtil.isNotBlank(prb.getBillRecordList())){
			this.purchaseReturnBillDao.doBatchInsert(prb.getBillRecordList());
		}
		this.purchaseBillOrderDao.batchExecute("update PurchaseOrderBill set returnBillNo = '"+billNo+"' where billNo = ?",PbillNo);
		this.purchaseBillOrderDao.batchExecute("update PurchaseOrderBill set status = 2 where billNo = ?",PbillNo);
	}

	@Override
	public PurchaseReturnBill load(String id) {
		return this.purchaseReturnBillDao.load(id);
	}

	@Override
	public PurchaseReturnBill get(String propertyName, Object value) {
		return null;
	}

	@Override
	public List<PurchaseReturnBill> find(List<PropertyFilter> filters) {
		return this.purchaseReturnBillDao.find(filters);
	}

	public PurchaseReturnBill findUniqueByBillNo(String billNo){
		return this.purchaseReturnBillDao.findUnique("from PurchaseReturnBill pcrb where  pcrb.billNo=?",new Object[]{billNo});
	}

	public String findMaxBillNo(String prefix){
		String hql="select max(CAST(SUBSTRING(PR.billNo,9),integer))from PurchaseReturnBill PR where PR.billNo like '"+prefix+"%'";
		Integer code =this.purchaseReturnBillDao.findUnique(hql,new Object[]{});
		return code==null?prefix+"001": prefix+CommonUtil.convertIntToString(code + 1, 3);
	}

	public List<PurchaseReturnBillDtl> findDetailsByBillNo(String billNo){
		String hql="from PurchaseReturnBillDtl pd where pd.billNo=?";
		return this.purchaseReturnBillDao.find(hql,new Object[]{billNo});
	}

	@Override
	public List<PurchaseReturnBill> getAll() {
		return this.purchaseReturnBillDao.getAll();
	}

	@Override
	public <X> List<X> findAll() {
		return null;
	}

	@Override
	public void update(PurchaseReturnBill entity) {
		this.purchaseReturnBillDao.update(entity);
	}

	@Override
	public void delete(PurchaseReturnBill entity) {

	}

	public void deleteBatchDtl(String billNo){
		String hql="delete from PurchaseReturnBillDtl pd where pd.billNo =?";
		this.purchaseReturnBillDao.batchExecute(hql, billNo);
	}
	@Override
	public void delete(String id) {

	}

	public Epc findCasnType(String code){
		Epc epcs=this.purchaseReturnBillDao.findUnique("from Epc pcrb where  pcrb.code=?",new Object[]{code});
		return epcs;
	}

	public MessageBox saveBusiness(PurchaseReturnBill purchaseOrderBill, List<PurchaseReturnBillDtl> purchaseOrderBillDtlList, Business business) throws Exception {
		List<Style> styleList = new ArrayList<>();
		for(PurchaseReturnBillDtl dtl : purchaseOrderBillDtlList){
			if(dtl.getStatus() == BillConstant.BillDtlStatus.OutStore ){
				Style s = CacheManager.getStyleById(dtl.getStyleId());
				s.setClass6(BillConstant.InStockType.BackOrder);
				styleList.add(s);
			}
		}
		MessageBox messageBox = this.taskService.checkEpcStock(business);
		if(messageBox.getSuccess()){
			this.purchaseReturnBillDao.saveOrUpdate(purchaseOrderBill);
			this.purchaseReturnBillDao.doBatchInsert(purchaseOrderBillDtlList);
			this.taskService.webSave(business);
			if(styleList.size() > 0){
				this.purchaseReturnBillDao.doBatchInsert(styleList);
			}
			return messageBox;
		}else{
			return messageBox;
		}

	}

	public List<PurchaseReturnBillDtl> findBillDtlByBillNo(String billNo) {
		return this.purchaseReturnBillDao.find("from PurchaseReturnBillDtl where billNo=?", new Object[]{billNo});
	}

	public List<BillRecord> getBillRecod(String billNo) {
		return  this.purchaseReturnBillDao.find("from BillRecord where billNo=?",new Object[]{billNo});
	}

	public Integer findBillStatus(String billNo) {
		return this.purchaseReturnBillDao.findUnique("select status from PurchaseReturnBill where id =?",billNo);
	}
}
