package com.casesoft.dmc.extend.tiantan.service;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.syn.tool.BillUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.syn.BillDao;
import com.casesoft.dmc.dao.task.TaskDao;
import com.casesoft.dmc.extend.tiantan.dao.TiantanBillDao;
import com.casesoft.dmc.extend.tiantan.dao.TiantanPostBillDao;
import com.casesoft.dmc.extend.tiantan.dao.TiantanUtil;
import com.casesoft.dmc.extend.tiantan.dao.basic.ConstantType;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.erp.ErpStock;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.shop.SaleBill;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.BusinessDtl;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.syn.IBillWSService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

//@Service
@Transactional
public class TiantanBillService implements IBillWSService {
	// @Autowired
	private TiantanBillDao tiantanBillDao;
	// @Autowired
	private TiantanPostBillDao tiantanPostBillDao;
 	private BillDao billDao;

 	private TaskDao taskDao;
	public BillDao getBillDao() {
		return billDao;
	}

	public void setBillDao(BillDao billDao) {
		this.billDao = billDao;
	}

	@Override
	public List<Bill> findBills(String[] properties, String[] values)
			throws Exception {
		System.out.println(System.currentTimeMillis());
		int type = Integer.parseInt(values[1]);// token 值
		String deviceId = values[5];
		String ownerId = values[0];

		String beginDate = values[2].replaceAll("/", "-");
		String endDate = values[3].replaceAll("/", "-");

		List<Bill> billList = null;
		String storageId = values[7];
		if (CommonUtil.isBlank(storageId)) {
 			if (CommonUtil.isNotBlank(CacheManager.getDeviceByCode(deviceId))) {
				storageId = CacheManager.getDeviceByCode(deviceId)
						.getStorageId();
			} else {
				return new ArrayList<>();
			}
		}else{
			if(CommonUtil.isBlank(CacheManager.getUnitByCode(storageId))){
				return new ArrayList<>();
			}
		}
		switch (type) {
		case Constant.Token.Storage_Inbound:// 成品入库
			billList = this.tiantanBillDao.findInBills(storageId, beginDate,
					endDate, ownerId, type);
			break;
		case Constant.Token.Storage_Refund_Inbound:
			billList = this.tiantanBillDao.findReturnInBills(storageId,
					beginDate, endDate, ownerId, type);
			break;
		case Constant.Token.Storage_Outbound:
			billList = this.tiantanBillDao.findOutBills(storageId, beginDate,
					endDate, ownerId, type);
			break;
		case Constant.Token.Storage_Refund_Outbound:// 仓库发货给门店，代理商
			billList = this.tiantanBillDao.findReturnOutBills(storageId,
					beginDate, endDate, ownerId, type);
			break;
		case Constant.Token.Shop_Transfer_Outbound:
			storageId = storageId.substring(7, storageId.length());
		case Constant.Token.Storage_Transfer_Outbound:
			billList = this.tiantanBillDao.findTransferOutBills(storageId,
					beginDate, endDate, ownerId, type);
			break;
		case Constant.Token.Shop_Refund_Outbound:
			storageId = storageId.substring(7, storageId.length());
			billList = this.tiantanBillDao.findShopReturnOutBills(storageId,
					beginDate, endDate, ownerId, type);
			break;
		case Constant.Token.Storage_Transfer_Inbound:
			billList = this.tiantanBillDao.findTransferInBills(storageId,
					beginDate, endDate, ownerId, type);
			break;
		case Constant.Token.Shop_Transfer_Inbound:
			List<Business> busListin = findTransferInBusiness(storageId);
			billList = BillUtil.convertBusToBill(busListin,
					Constant.Token.Shop_Transfer_Inbound);
			break;
		case Constant.Token.Shop_Inbound:
			List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
			PropertyFilter filter = new PropertyFilter("EQI_token", ""
					+ Constant.Token.Storage_Outbound);
			filters.add(filter);
			filter = new PropertyFilter("LTI_status", ""
					+ Constant.TaskStatus.Confirmed);
			filters.add(filter);
			filter = new PropertyFilter("EQS_destId", storageId);
			filters.add(filter);
			List<Business> busList = this.taskDao.find(filters);
			billList = BillUtil.convertBusToBill(busList,
					Constant.Token.Shop_Inbound);
			break;
		case Constant.Token.Shop_Inventory:
			List<PropertyFilter> hfilters = new ArrayList<PropertyFilter>();
			PropertyFilter hfilter = new PropertyFilter("EQI_type", ""
					+ Constant.Token.Shop_Inventory);
			hfilters.add(hfilter);
			hfilter = new PropertyFilter("LTI_status", "" + 2);
			hfilters.add(hfilter);
			hfilter = new PropertyFilter("EQS_origId", storageId);
			hfilters.add(hfilter);
			hfilter = new PropertyFilter("GED_billDate", beginDate);
			hfilters.add(hfilter);
			hfilter = new PropertyFilter("LED_billDate", endDate);
			hfilters.add(hfilter);
			billList = this.billDao.find(hfilters);
			BillUtil.convertToVoList(billList);
			break;
		case Constant.Token.Storage_Inventory:
			List<PropertyFilter> sfilters = new ArrayList<PropertyFilter>();
			PropertyFilter sfilter = new PropertyFilter("EQI_type", ""
					+ Constant.Token.Storage_Inventory);
			sfilters.add(sfilter);
			sfilter = new PropertyFilter("LTI_status", "" + 2);
			sfilters.add(sfilter);
			sfilter = new PropertyFilter("EQS_origId", storageId);
			sfilters.add(sfilter);
			sfilter = new PropertyFilter("GED_billDate", beginDate);
			sfilters.add(sfilter);
			sfilter = new PropertyFilter("LED_billDate", endDate);
			sfilters.add(sfilter);
			billList = this.billDao.find(sfilters);
			BillUtil.convertToVoList(billList);
			break;
		}
		System.out.println(System.currentTimeMillis());
		return billList;
	}

	@Override
	public List<BillDtl> findBillDtls(String[] properties, String[] values)
			throws Exception {
		return findBillDtlList(values[0],Integer.parseInt(values[1]));
	}

	@Override
	public List<BillDtl> findBillDtls(String id) {
		return null;
	}

	@Override
	public String findBillsJSON(String[] properties, String[] values)
			throws Exception {
		return null;
	}

	@Override
	public String findBillDtlsJSON(String[] properties, String[] values)
			throws Exception {
		String billId = values[0];
		System.out.print("billId:" + billId);
		int type = Integer.parseInt(values[1]);
		System.out.print("type:" + type);
		return this.findBillDtlsJSON(billId, type);
	}

	@Override
	public String findBillDtlsJSON(String id) throws Exception {
		return null;
	}

	@Transactional(readOnly = true)
	public String findBillDtlsJSON(String bId, int type) throws Exception {
		return JSON.toJSONString(this.findBillDtlList(bId, type));
	}

	private List<BillDtl> findBillDtlList(String bId, int type)
			throws Exception {

		List<BillDtl> dtlList = null;
		switch (type) {
		case Constant.Token.Storage_Inbound:
			dtlList = this.tiantanBillDao.findInBillDtls(bId, type);
			break;
		case Constant.Token.Storage_Refund_Inbound:
			dtlList = this.tiantanBillDao.findReturnInBillDtls(bId, type);
			break;
		case Constant.Token.Storage_Outbound:
			dtlList = this.tiantanBillDao.findOutBillDtls(bId, type);
			break;
		case Constant.Token.Storage_Refund_Outbound:
			dtlList = this.tiantanBillDao.findReturnOutBillDtls(bId, type);
			break;
		case Constant.Token.Storage_Transfer_Outbound:
		case Constant.Token.Shop_Transfer_Outbound:
			dtlList = this.tiantanBillDao.findTransferOutBillDtls(bId, type);
			break;
		case Constant.Token.Storage_Transfer_Inbound:
			dtlList = this.tiantanBillDao.findTransferInBillDtls(bId, type);
			break;
		case Constant.Token.Shop_Inbound:
		case Constant.Token.Shop_Transfer_Inbound:
			String taskId = bId;
			int token = (type == Constant.Token.Shop_Inbound ? Constant.Token.Storage_Outbound
					: Constant.Token.Shop_Transfer_Outbound);
			List<BusinessDtl> busDtlList =findBusinessDtl(taskId,token);
			dtlList = BillUtil.convertBusDtlToBillDtl(busDtlList, bId);
			break;
		case Constant.Token.Shop_Refund_Outbound:
			dtlList = this.tiantanBillDao.findShopReturnOutBillDtls(bId, type);
			break;
		case Constant.Token.Shop_Inventory:
		case Constant.Token.Storage_Inventory:
			dtlList = this.loadDtls(bId);
			BillUtil.convertToVo(dtlList);
			break;
		}
		return dtlList;
	}

	@Override
	public MessageBox uploadToERP(Bill bill) {
		return null;
	}

	@Override
	public MessageBox uploadTaskToErp(Business bus) {
		String unitId;// 发货方
		String unit2Id;// 收货方ID
		Bill bill = bus.getBill();
		switch (bus.getToken()) {
		case Constant.Token.Storage_Inbound:
			if (CommonUtil.isNotBlank(bill)) {
				String billId = bill.getId();
				switch (Integer.parseInt(billId.split("-")[0])) {
				case ConstantType.WH_IN_FACTORY:
					this.tiantanPostBillDao.batchInFromFactoryBill(bus);
					/*
					 * bill.setUnitId(Constant.UnitCodePrefix.Factory +
					 * bill.getUnitId()); bus.setStorageId(bill.getUnitId());
					 */
					break;
				case ConstantType.WH_IN_VENDOR:
					this.tiantanPostBillDao.batchInBill(bus);
					/*
					 * bill.setUnitId(Constant.UnitCodePrefix.Vender +
					 * bill.getUnitId());
					 * bill.setUnit2Id(Constant.UnitCodePrefix.Warehouse +
					 * bill.getUnit2Id()); bus.setStorageId(bill.getUnitId());
					 * bus.setUnit2Id(bill.getUnit2Id());
					 */
					break;
				}
			}
			break;
		case Constant.Token.Storage_Refund_Inbound:
			if (CommonUtil.isNotBlank(bill)) {
				String billId = bill.getId();

				switch (Integer.parseInt(billId.split("-")[0])) {
				case ConstantType.WH_RENTURN_VENDER:
					this.tiantanPostBillDao.batchReturnInBill(bus);
					break;
				case ConstantType.WH_RENTURN_SHOP:
					this.tiantanPostBillDao.batchReturnInFromShopBill(bus);
					break;
				}
			}
			/*
			 * if(CommonUtil.isNotBlank(bill)){
			 * bill.setUnitId(Constant.UnitCodePrefix.Shop + bill.getUnitId());
			 * bill.setUnit2Id(Constant.UnitCodePrefix.Warehouse +
			 * bill.getUnit2Id()); bus.setStorageId(bill.getUnitId());
			 * bus.setUnit2Id(bill.getUnit2Id());
			 * 
			 * } }
			 */
			break;
		case Constant.Token.Storage_Outbound:
			this.tiantanPostBillDao.batchOutBill(bus);
			/*
			 * if(CommonUtil.isNotBlank(bill)){ String billId=bill.getId();
			 * 
			 * bill.setUnitId(Constant.UnitCodePrefix.Warehouse +
			 * bill.getUnitId());
			 * if(ConstantType.WH_RENTURN_SHOP==Integer.parseInt
			 * (billId.split("-")[0])){
			 * bill.setUnit2Id(Constant.UnitCodePrefix.Shop +
			 * bill.getUnit2Id()); }else{
			 * bill.setUnit2Id(Constant.UnitCodePrefix.Agent +
			 * bill.getUnit2Id()); } bus.setStorageId(bill.getUnitId());
			 * bus.setUnit2Id(bill.getUnit2Id()); }
			 */
			break;
		case Constant.Token.Storage_Refund_Outbound:
			this.tiantanPostBillDao.batchReturnOutBill(bus);
			/*
			 * if(CommonUtil.isNotBlank(bill)){
			 * bill.setUnitId(Constant.UnitCodePrefix.Warehouse +
			 * bill.getUnitId()); bill.setUnit2Id(Constant.UnitCodePrefix.Vender
			 * + bill.getUnit2Id()); bus.setStorageId(bill.getUnitId());
			 * bus.setUnit2Id(bill.getUnit2Id()); }
			 */
			break;
		case Constant.Token.Storage_Transfer_Outbound:
			this.tiantanPostBillDao.batchTransferOutBill(bus);
			/*
			 * if(CommonUtil.isNotBlank(bill)){
			 * bill.setUnitId(Constant.UnitCodePrefix.Warehouse +
			 * bill.getUnitId());
			 * bill.setUnit2Id(Constant.UnitCodePrefix.Warehouse +
			 * bill.getUnit2Id()); bus.setStorageId(bill.getUnitId());
			 * bus.setUnit2Id(bill.getUnit2Id()); }
			 */
			break;
		case Constant.Token.Shop_Transfer_Outbound:
			this.tiantanPostBillDao.batchTransferOutBill(bus);
			/*
			 * if(CommonUtil.isNotBlank(bill)){
			 * bill.setUnitId(Constant.UnitCodePrefix.Shop + bill.getUnitId());
			 * bill.setUnit2Id(Constant.UnitCodePrefix.Shop +
			 * bill.getUnit2Id()); bus.setStorageId(bill.getUnitId());
			 * bus.setUnit2Id(bill.getUnit2Id()); }
			 */
			break;
		case Constant.Token.Storage_Transfer_Inbound:
			bus.setSrcBill(this.getBillByBillId(bus.getBillId()));
			this.tiantanPostBillDao.batchTransferInBill(bus);

			/*
			 * if(CommonUtil.isNotBlank(bill)){
			 * bill.setUnitId(Constant.UnitCodePrefix.Warehouse +
			 * bill.getUnitId());
			 * bill.setUnit2Id(Constant.UnitCodePrefix.Warehouse +
			 * bill.getUnit2Id()); bus.setStorageId(bill.getUnitId());
			 * bus.setUnit2Id(bill.getUnit2Id()); }
			 */
			break;
		case Constant.Token.Shop_Transfer_Inbound:
			if (CommonUtil.isNotBlank(bill)) {
				Business bus1 =getBusiness("id",bus.getBill().getBillNo());
				bus.setSrcBill(this.getBillByBillId(bus1
						.getBillId()));
				bus.setSrcTaskId(bus1.getId());
				bus.getBill().setSrcBillNo(bus1.getBillNo());
				this.tiantanPostBillDao.batchShopTransferInBill(bus);
			}
			break;
		case Constant.Token.Shop_Inbound:
			if (CommonUtil.isNotBlank(bill)) {
				Business bus2 = getBusiness("id",bus.getBill().getBillNo());
				bus.setSrcBill(this.getBillByBillId(bus2
						.getBillId()));
				bus.setSrcTaskId(bus2.getId());
				bus.getBill().setSrcBillNo("R3_" + bus2.getBillNo());
				System.out.println(bus2.getBillNo());
				this.tiantanPostBillDao.batchShopInBill(bus);
			}
			break;
		case Constant.Token.Shop_Refund_Outbound:
			this.tiantanPostBillDao.batchShopReturnOutBill(bus);
			break;
		case Constant.Token.Shop_Inventory:
		case Constant.Token.Storage_Inventory:
			if (CommonUtil.isBlank(bus.getBill())) {
				this.tiantanPostBillDao.batchInventory(bus);
			} else {
				List<Record> records = taskDao.find(
						"from Record r where r.taskId=?",
						new Object[] { bus.getBill().getBillNo() });
				try {
					TiantanUtil.buildBillRecord(bus, records);
					this.tiantanPostBillDao.batchInventoryBill(bus);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return new MessageBox(false,e.getMessage());
				}
			}
			break;
		}
		return new MessageBox(true,"ok");
	}

	@Override
	public MessageBox delete(String id) {
		return null;
	}

	@Override
	public MessageBox update(Business bus) {
		return null;
	}

	@Override
	public MessageBox uploadPosToERP(SaleBill bill) {
		return null;
	}

	public TiantanBillDao getTiantanBillDao() {
		return tiantanBillDao;
	}

	public void setTiantanBillDao(TiantanBillDao tiantanBillDao) {
		this.tiantanBillDao = tiantanBillDao;
	}

	public TiantanPostBillDao getTiantanPostBillDao() {
		return tiantanPostBillDao;
	}

	public void setTiantanPostBillDao(TiantanPostBillDao tiantanPostBillDao) {
		this.tiantanPostBillDao = tiantanPostBillDao;
	}

	public TaskDao getTaskDao() {
		return taskDao;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	@Override
	public List<ErpStock> findErpStock(String[] properties, String[] values)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> findErpBasic(String styleId, String colorId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bill productBill(String[] properties, String[] values) {
		Bill bill = null;
		String deviceId = values[0];
		String unitId = values[1];
		String token = values[2];
		int type = Integer.parseInt(token);
		switch (type) {
		case Constant.Token.Storage_Inbound:

			break;
		case Constant.Token.Storage_Refund_Inbound:

			break;
		case Constant.Token.Storage_Outbound:

			break;
		case Constant.Token.Storage_Refund_Outbound:

			break;
		case Constant.Token.Storage_Transfer_Outbound:

			break;
		case Constant.Token.Storage_Transfer_Inbound:

			break;
		/**
		 * 门店
		 * */
		case Constant.Token.Shop_Transfer_Outbound:

			break;
		case Constant.Token.Shop_Refund_Outbound:

			break;
		case Constant.Token.Shop_Inbound:
			break;
		case Constant.Token.Shop_Transfer_Inbound:

			break;
		case Constant.Token.Shop_Inventory:
			List<Bill> bills = this.tiantanBillDao.findInventoryBills(null,
					null, unitId, type);

			if (CommonUtil.isNotBlank(bills)) {
				bill = bills.get(0);
				bill.setOwnerId("1");
			}
			break;
		case Constant.Token.Storage_Inventory:
			List<Bill> sbills = this.tiantanBillDao.findInventoryBills(null,
					null, unitId, type);
			if (CommonUtil.isNotBlank(sbills)) {
				bill = sbills.get(0);
				bill.setOwnerId("1");

			}
			break;
		}
		return bill;
	}

	@Override
	public String destroyBill(String[] properties, String[] values) {
		String token = values[0];
		String billNo = values[1];
		String deviceId = values[2];
		Bill bill = this.getBillByBillId(billNo);
		if (CommonUtil.isBlank(bill) || bill.getActQty() == null
				|| bill.getActQty() == 0l) {
			return null;
		} else {
			bill.setDeliverNo(deviceId);
			Business bus = new Business();
			bus.setBill(bill);
			int type = Integer.parseInt(token);
			switch (type) {
			case Constant.Token.Storage_Inbound:

				break;
			case Constant.Token.Storage_Refund_Inbound:

				break;
			case Constant.Token.Storage_Outbound:

				break;
			case Constant.Token.Storage_Refund_Outbound:

				break;
			case Constant.Token.Storage_Transfer_Outbound:

				break;
			case Constant.Token.Storage_Transfer_Inbound:

				break;
			/**
			 * 门店
			 * */
			case Constant.Token.Shop_Transfer_Outbound:

				break;
			case Constant.Token.Shop_Refund_Outbound:

				break;
			case Constant.Token.Shop_Inbound:
				break;
			case Constant.Token.Shop_Transfer_Inbound:

				break;
			case Constant.Token.Shop_Inventory:
				// this.tiantanPostBillDao.batchInventory(bus);
				break;
			case Constant.Token.Storage_Inventory:
				// this.tiantanPostBillDao.batchInventory(bus);
				break;
			}
			bill.setStatus(2);
			this.billDao.update(bill);
			return "success";
		}
	}

	@Override
	public MessageBox updateUnitInfo(Business bus) {
		return null;
	}

	@Override
	public List<Record> findRecordByTask(String taskId) {
		return null;
	}

	public Bill getBillByBillId(String id){
		Bill bill=this.billDao.findUnique("from Bill b where b.id=?", new Object[]{id});
		if(CommonUtil.isNotBlank(bill)){
			List<BillDtl> listDtls=this.billDao.find("from BillDtl d where d.billId=?",new Object[]{id});
			bill.setDtlList(listDtls);
        }
		return bill;
	}
	public List<BillDtl> loadDtls(String id) {
		return this.billDao.find("from BillDtl dtl where dtl.billId=?", id);
	}
	public List<Business> findTransferInBusiness(String origId) {
		String hql = "from Business b where b.status<? and b.destId=? and (b.token=? or b.token=?)";

		return this.billDao.find(hql, new Object[] {
				Constant.TaskStatus.Confirmed, origId,
				Constant.Token.Storage_Transfer_Outbound,
				Constant.Token.Shop_Transfer_Outbound });
	}
	public Business getBusiness(String propertyName, Object value) {
		return this.taskDao.findUniqueBy(propertyName, value);
	}
	public List<BusinessDtl> findBusinessDtl(String taskId, Integer token) {
		String hql = "from BusinessDtl dtl where dtl.taskId=? and dtl.token=? order by sku";
		return this.taskDao.find(hql, new Object[] { taskId, token });
	}

}
