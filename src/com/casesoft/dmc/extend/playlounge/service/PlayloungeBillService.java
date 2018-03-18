package com.casesoft.dmc.extend.playlounge.service;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.syn.tool.BillUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.syn.BillDao;
import com.casesoft.dmc.extend.playlounge.dao.*;
import com.casesoft.dmc.extend.playlounge.dao.basic.PlayloungeConstants;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.erp.ErpStock;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.shop.SaleBill;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.BusinessDtl;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.syn.IBillWSService;
import com.casesoft.dmc.service.task.TaskService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
public class PlayloungeBillService implements IBillWSService {

	private BillDao billDao;
	private TaskService taskService;
	private PlayloungeShopBillDao playloungeShopBillDao;
	private PlayloungeShopPostBillDao playloungeShopPostBillDao;
	private PlayloungeShopPostTaskDao playloungeShopPostTaskDao;

	private PlayloungeWarehouseBillDao playloungeWarehouseBillDao;
	private PlayloungeWarehousePostBillDao playloungeWarehousePostBillDao;
	private PlayloungeWarehousePostTaskDao playloungeWarehousePostTaskDao;

	@Override
	public List<Bill> findBills(String[] properties, String[] values)
			throws Exception {
		System.out.println(System.currentTimeMillis());
		int type = Integer.parseInt(values[1]);// token 值
		String deviceId = values[5];
		String ownerId = values[0];

		String beginDate = values[2].replaceAll("/", "-");
		String endDate = values[3].replaceAll("/", "-");
		String status= values[4];

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
			billList = this.playloungeWarehouseBillDao.findWarehouseInBills(
					storageId, beginDate, endDate, ownerId, type);
			break;
		case Constant.Token.Storage_Refund_Inbound:
			billList = this.playloungeWarehouseBillDao
					.findWarehouseReturnInBills(storageId, beginDate, endDate,
							ownerId, type);
			break;
		case Constant.Token.Storage_Outbound:
			billList = this.playloungeWarehouseBillDao.findWarehouseOutBills(
					storageId, beginDate, endDate, ownerId, type);
			break;
		case Constant.Token.Storage_Refund_Outbound:// 仓库发货给门店，代理商
			billList = this.playloungeWarehouseBillDao
					.findWarehouseReturnOutBills(storageId, beginDate, endDate,
							ownerId, type);
			break;
		case Constant.Token.Storage_Transfer_Outbound:
			billList = this.playloungeWarehouseBillDao
					.findWarehouseTransferOutBills(storageId, beginDate,
							endDate, ownerId, type);
			break;
		case Constant.Token.Storage_Transfer_Inbound:
			billList = this.playloungeWarehouseBillDao
					.findWarehouseTransferInBills(storageId, beginDate,
							endDate, ownerId, type);
			break;
		/**
		 * 门店
		 * */
		case Constant.Token.Shop_Transfer_Outbound:
			billList = this.playloungeShopBillDao.findShopTransferOutBills(
					storageId, beginDate, endDate, ownerId, type);
			break;
		case Constant.Token.Shop_Refund_Outbound:
			billList = this.playloungeShopBillDao.findShopReturnOutBills(
					storageId, beginDate, endDate, ownerId, type);
			break;
		case Constant.Token.Shop_Transfer_Inbound:
			List<Business> busListin = this.taskService
					.findTransferInBusiness(storageId);
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
			filter = new PropertyFilter("GED_beginTime", beginDate);
			filters.add(filter);
			filter = new PropertyFilter("LED_beginTime", endDate);
			filters.add(filter);
			List<Business> busList = this.taskService.find(filters);
			billList = BillUtil.convertBusToBill(busList,
					Constant.Token.Shop_Inbound);
			break;
		case Constant.Token.Storage_Inventory:
			List<PropertyFilter> sfilters = new ArrayList<PropertyFilter>();
			PropertyFilter sfilter = new PropertyFilter("EQI_type", ""
					+ Constant.Token.Storage_Inventory);
			sfilters.add(sfilter);
			sfilter = new PropertyFilter("INI_status",status);
			sfilters.add(sfilter);
			sfilter = new PropertyFilter("EQS_origId", storageId);
			sfilters.add(sfilter);
			sfilter = new PropertyFilter("GED_billDate", beginDate);
			sfilters.add(sfilter);
			sfilter = new PropertyFilter("LED_billDate", endDate);
			sfilters.add(sfilter);
			billList = this.billDao.find(sfilters);
			BillUtil.convertToVoList(billList);
			/*
			 * billList=this.playloungeWarehouseBillDao.findWarehouseInventoryBills
			 * (beginDate, endDate, storageId, storageId, type);
			 */break;
		case Constant.Token.Shop_Inventory:
			List<PropertyFilter> hfilters = new ArrayList<PropertyFilter>();
			PropertyFilter hfilter = new PropertyFilter("EQI_type", ""
					+ Constant.Token.Shop_Inventory);
			hfilters.add(hfilter);
			hfilter = new PropertyFilter("INI_status",status);
			hfilters.add(hfilter);
			hfilter = new PropertyFilter("EQS_origId", storageId);
			hfilters.add(hfilter);
			hfilter = new PropertyFilter("GED_billDate", beginDate);
			hfilters.add(hfilter);
			hfilter = new PropertyFilter("LED_billDate", endDate);
			hfilters.add(hfilter);
			billList = this.billDao.find(hfilters);
			BillUtil.convertToVoList(billList);

			/*
			 * billList=this.playloungeShopBillDao.findShopInventoryBills(beginDate
			 * , endDate, storageId, type);
			 */break;
		}
		System.out.println(System.currentTimeMillis());
		return billList;
	}

	@Override
	public String findBillDtlsJSON(String[] properties, String[] values)
			throws Exception {
		String billId = values[0];
		System.out.println("billId:" + billId);
		int type = Integer.parseInt(values[1]);
		System.out.println("type:" + type);
		return this.findBillDtlsJSON(billId, type);
	}

	@Override
	public List<BillDtl> findBillDtls(String[] properties, String[] values)
			throws Exception {
		// TODO Auto-generated method stub
		return findBillDtlList(values[0],Integer.parseInt(values[1]));
	}

	private List<BillDtl> findBillDtlList(String bId, int type)
			throws Exception {

		List<BillDtl> dtlList = null;
		switch (type) {
		case Constant.Token.Storage_Inbound:
			dtlList = this.playloungeWarehouseBillDao.findWarehouseInBillDtls(
					bId, type);
			BillUtil.convertToVo(dtlList);

			/* FilterUtil.doFilterNull(dtlList); */
			break;
		case Constant.Token.Storage_Refund_Inbound:
			dtlList = this.playloungeWarehouseBillDao
					.findWarehouseReturnInBillDtls(bId, type);
			FilterUtil.doFilterNull(dtlList);
			BillUtil.convertToVo(dtlList);

			break;
		case Constant.Token.Storage_Outbound:
			dtlList = this.playloungeWarehouseBillDao.findWarehouseOutBillDtls(
					bId, type);
			FilterUtil.doFilterNull(dtlList);
			BillUtil.convertToVo(dtlList);

			break;
		case Constant.Token.Storage_Refund_Outbound:
			dtlList = this.playloungeWarehouseBillDao
					.findWarehouseReturnOutBillDtls(bId, type);
			FilterUtil.doFilterNull(dtlList);
			BillUtil.convertToVo(dtlList);

			break;
		case Constant.Token.Storage_Transfer_Outbound:
			dtlList = this.playloungeWarehouseBillDao
					.findWarehouseTransferOutBillDtls(bId, type);
			FilterUtil.doFilterNull(dtlList);
			BillUtil.convertToVo(dtlList);

			break;
		case Constant.Token.Storage_Transfer_Inbound:
			dtlList = this.playloungeWarehouseBillDao
					.findWarehouseTransferInBillDtls(bId, type);
			FilterUtil.doFilterNull(dtlList);
			BillUtil.convertToVo(dtlList);

			break;
		/**
		 * 门店
		 * */
		case Constant.Token.Shop_Transfer_Outbound:
			dtlList = this.playloungeShopBillDao.findShopTransferOutBillDtls(
					bId, type);
			FilterUtil.doFilterNull(dtlList);
			BillUtil.convertToVo(dtlList);

			break;
		case Constant.Token.Shop_Refund_Outbound:
			dtlList = this.playloungeShopBillDao.findShopReturnOutBillDtls(bId,
					type);
			FilterUtil.doFilterNull(dtlList);
			BillUtil.convertToVo(dtlList);

			break;
		case Constant.Token.Shop_Inbound:
		case Constant.Token.Shop_Transfer_Inbound:
			String taskId = bId;
			int token = (type == Constant.Token.Shop_Inbound ? Constant.Token.Storage_Outbound
					: Constant.Token.Shop_Transfer_Outbound);
			List<BusinessDtl> busDtlList = this.taskService.findDtl(taskId,
					token);
			dtlList = BillUtil.convertBusDtlToBillDtl(busDtlList, bId);
			FilterUtil.doFilterNull(dtlList);
			BillUtil.convertToVo(dtlList);

			break;
		case Constant.Token.Shop_Inventory:
			dtlList = this.loadDtls(bId);
			BillUtil.convertToVo(dtlList);
			/*
			 * dtlList=this.playloungeShopBillDao.findShopInventoryBillDtls(bId,
			 * type);
			 */break;
		case Constant.Token.Storage_Inventory:
			dtlList = this.loadDtls(bId);
			BillUtil.convertToVo(dtlList);
			/*
			 * dtlList=this.playloungeWarehouseBillDao.
			 * findWarehouseInventoryBillDtls(bId, type);
			 */break;
		}
		return dtlList;
	}

	@Transactional(readOnly = true)
	public String findBillDtlsJSON(String bId, int type) throws Exception {
		return JSON.toJSONString(this.findBillDtlList(bId, type));
	}

	@Override
	public List<BillDtl> findBillDtls(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String findBillsJSON(String[] properties, String[] values)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageBox uploadToERP(Bill bill) {
		// TODO Auto-generated method stub
		return new MessageBox(false,"");
	}

	@Transactional(readOnly = true)
	@Override
	public MessageBox uploadTaskToErp(Business bus) {
		String unitId;// 发货方
		String unit2Id;// 收货方ID
		Bill bill = bus.getBill();
		switch (bus.getToken()) {
		case Constant.Token.Storage_Inbound:
			if (CommonUtil.isNotBlank(bill)) {
				this.playloungeWarehousePostBillDao.batchWarehouseInBill(bus);
			} else {
				PlayloungeUtil.formatPrice(bus);
				this.playloungeWarehousePostTaskDao.batchWarehouseInTask(bus);
			}
			PlayloungeUtil.formateBill(bus);

			break;
		case Constant.Token.Storage_Refund_Inbound:
			if (CommonUtil.isNotBlank(bill)) {
				String billId = bill.getId();
				if (billId.split("_")[billId.split("_").length - 1]
						.equals(String.valueOf(PlayloungeConstants.BillTo.CUST))) {
				} else {
					this.playloungeWarehousePostBillDao
							.batchWarehouseReturnInBill(bus);
					PlayloungeUtil.formateBill(bus);
				}
			}

			break;
		case Constant.Token.Storage_Outbound:
			if (CommonUtil.isNotBlank(bill)) {
				String billId = bill.getId();
				if (billId.split("_")[billId.split("_").length - 1]
						.equals(String.valueOf(PlayloungeConstants.BillTo.CUST))) {
					if (bill.getBillType().equals("手动出库单")) {

					} else if (bill.getBillType().equals("订单")) {

					} else {

					}
				} else {
					if (bill.getBillType().equals("手动出库单")) {
						this.playloungeWarehousePostBillDao
								.batchWarehouseOutBill(bus);
					} else if (bill.getBillType().equals("订单")) {
						this.playloungeWarehousePostBillDao
								.batchWarehouseOutBill(bus);
					} else {
						this.playloungeWarehousePostBillDao
								.batchWarehouseOutAddedBill(bus);
					}
				}
			} else {
				PlayloungeUtil.formatPrice(bus);
				this.playloungeWarehousePostTaskDao.batchWarehouseOutTask(bus);
			}
			PlayloungeUtil.formateBill(bus);

			break;
		case Constant.Token.Storage_Refund_Outbound:
			if (CommonUtil.isNotBlank(bill)) {
				this.playloungeWarehousePostBillDao
						.batchWarehouseReturnOutBill(bus);
			} else {
				PlayloungeUtil.formatPrice(bus);
				this.playloungeWarehousePostTaskDao
						.batchWarehouseReturnOutTask(bus);
			}

			PlayloungeUtil.formateBill(bus);

			break;
		case Constant.Token.Storage_Transfer_Outbound:
			if (CommonUtil.isNotBlank(bill)) {
				this.playloungeWarehousePostBillDao
						.batchWarehouseTransferOutBill(bus);
			} else {
				PlayloungeUtil.formatPrice(bus);
				this.playloungeWarehousePostTaskDao
						.batchWarehouseTransferOutTask(bus);
			}
			PlayloungeUtil.formateBill(bus);

			break;
		case Constant.Token.Storage_Transfer_Inbound:
			this.playloungeWarehousePostBillDao
					.batchWarehouseTransferInBill(bus);
			PlayloungeUtil.formateBill(bus);

			break;
		case Constant.Token.Storage_Inventory:
			if (CommonUtil.isNotBlank(bill)) {
				try {
					FilterUtil.buildBillRecord(bus);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				PlayloungeUtil.formatPrice(bus);
				this.playloungeWarehousePostTaskDao
						.batchWarehouseInventoryTask(bus);
			}

			break;
		case Constant.Token.Shop_Transfer_Outbound:
			if (CommonUtil.isNotBlank(bill)) {
				this.playloungeShopPostBillDao.batchShopTransferOutBill(bus);
			} else {
				PlayloungeUtil.formatPrice(bus);
				this.playloungeShopPostTaskDao.batchShopTransferOutTask(bus);
			}
			PlayloungeUtil.formateBill(bus);

			break;
		case Constant.Token.Shop_Transfer_Inbound:
			if (CommonUtil.isNotBlank(bill)) {
				Business bus1 = this.taskService
						.load(bus.getBill().getBillNo());
				if (CommonUtil.isNotBlank(bus1.getBillId())) {
					bus.setSrcBill(this.getBillByBillId(bus1
							.getBillId()));
					bus.setSrcTaskId(bus1.getId());
					bus.getBill().setSrcBillNo(bus.getSrcBill().getSrcBillNo());
					this.playloungeShopPostBillDao.batchShopTransferInBill(bus);
				} else {
					bus.setSrcTaskId(bus1.getId());
					bus.getBill().setSrcBillNo(bus1.getSrcBillNo());
					this.playloungeShopPostTaskDao.batchShopTransferInTask(bus);
				}
			}
			break;
		case Constant.Token.Shop_Inbound:
			if (CommonUtil.isNotBlank(bill)) {
				Business bus1 = this.taskService
						.load(bus.getBill().getBillNo());
				if (CommonUtil.isNotBlank(bus1.getBillId())) {
					bus.setSrcBill(this.getBillByBillId(bus1
							.getBillId()));
					bus.setSrcTaskId(bus1.getId());
					bus.getBill().setSrcBillNo(bus.getSrcBill().getSrcBillNo());
					this.playloungeShopPostBillDao.batchShopInBill(bus);
				} else {
					bus.setSrcTaskId(bus1.getId());
					bus.getBill().setSrcBillNo(bus1.getSrcBillNo());
					this.playloungeShopPostTaskDao.batchShopInTask(bus);
				}
			}
			break;
		case Constant.Token.Shop_Refund_Outbound:
			if (CommonUtil.isNotBlank(bill)) {
				this.playloungeShopPostBillDao.batchShopReturnOutBill(bus);
			} else {
				PlayloungeUtil.formatPrice(bus);
				this.playloungeShopPostTaskDao.batchShopReturnOutTask(bus);
			}
			PlayloungeUtil.formateBill(bus);

			break;
		case Constant.Token.Shop_Inventory:
			if (CommonUtil.isNotBlank(bill)) {
				try {
					FilterUtil.buildBillRecord(bus);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				PlayloungeUtil.formatPrice(bus);
				this.playloungeShopPostTaskDao.batchShopInventoryTask(bus);
			}
			break;
		}
		return new MessageBox(true,"");
	}

	@Override
	public MessageBox delete(String id) {
		// TODO Auto-generated method stub
        return new MessageBox(false,"");
	}

	@Override
	public MessageBox update(Business bus) {
		// TODO Auto-generated method stub
        return new MessageBox(false,"");
	}

	@Override
	public MessageBox uploadPosToERP(SaleBill bill) {
		// TODO Auto-generated method stub
        return new MessageBox(true,"");
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

	public PlayloungeShopBillDao getPlayloungeShopBillDao() {
		return playloungeShopBillDao;
	}

	public void setPlayloungeShopBillDao(
			PlayloungeShopBillDao playloungeShopBillDao) {
		this.playloungeShopBillDao = playloungeShopBillDao;
	}

	public PlayloungeShopPostBillDao getPlayloungeShopPostBillDao() {
		return playloungeShopPostBillDao;
	}

	public void setPlayloungeShopPostBillDao(
			PlayloungeShopPostBillDao playloungeShopPostBillDao) {
		this.playloungeShopPostBillDao = playloungeShopPostBillDao;
	}

	public PlayloungeWarehouseBillDao getPlayloungeWarehouseBillDao() {
		return playloungeWarehouseBillDao;
	}

	public void setPlayloungeWarehouseBillDao(
			PlayloungeWarehouseBillDao playloungeWarehouseBillDao) {
		this.playloungeWarehouseBillDao = playloungeWarehouseBillDao;
	}

	public PlayloungeWarehousePostBillDao getPlayloungeWarehousePostBillDao() {
		return playloungeWarehousePostBillDao;
	}

	public void setPlayloungeWarehousePostBillDao(
			PlayloungeWarehousePostBillDao playloungeWarehousePostBillDao) {
		this.playloungeWarehousePostBillDao = playloungeWarehousePostBillDao;
	}

	@Override
	public String findBillDtlsJSON(String id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public PlayloungeWarehousePostTaskDao getPlayloungeWarehousePostTaskDao() {
		return playloungeWarehousePostTaskDao;
	}

	public void setPlayloungeWarehousePostTaskDao(
			PlayloungeWarehousePostTaskDao playloungeWarehousePostTaskDao) {
		this.playloungeWarehousePostTaskDao = playloungeWarehousePostTaskDao;
	}

	public PlayloungeShopPostTaskDao getPlayloungeShopPostTaskDao() {
		return playloungeShopPostTaskDao;
	}

	public void setPlayloungeShopPostTaskDao(
			PlayloungeShopPostTaskDao playloungeShopPostTaskDao) {
		this.playloungeShopPostTaskDao = playloungeShopPostTaskDao;
	}

	@Override
	public List<ErpStock> findErpStock(String[] properties, String[] values)
			throws Exception {
		String filter_LIKES_styleId = values[0];
		String filter_LIKES_colorId = values[1];
		String filter_LIKES_sizeId = values[2];
		String filter_LIKES_warehouseId = values[3];
		String filter_LIKES_sku = values[4];
		if(values.length<=5){
			return this.playloungeWarehouseBillDao.findErpStocks(filter_LIKES_sku,
					filter_LIKES_styleId, filter_LIKES_colorId,
					filter_LIKES_sizeId, filter_LIKES_warehouseId);
		}else{
			return this.playloungeWarehouseBillDao.findErpStocks(filter_LIKES_sku);
		}
	}

	@Override
	public List<Product> findErpBasic(String styleId, String colorId) {
		if(colorId==null||colorId.equals("")){
			return this.billDao.find("from Product p where p.styleId=? ",new Object[]{styleId});
		}else{
			return this.billDao.find("from Product p where p.styleId=? and p.colorId=?",new Object[]{styleId,colorId});
		}
/*
		return this.playloungeWarehouseBillDao.findErpImg(styleId, colorId);
*/
	}

	@Override
	public Bill productBill(String[] properties, String[] values) {
		Bill bill = null;
		String deviceId = values[0];
		String unitId = values[1];
		String token = values[2];
		String billDate=values[3];
		String conditions=values[4];
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
			List<Bill> bills = this.playloungeShopBillDao
					.findShopInventoryBills(unitId, null, null,billDate,conditions, unitId,
							type);
			if (CommonUtil.isNotBlank(bills)) {
				bill = bills.get(0);
				bill.setOwnerId("1");
			}
			break;
		case Constant.Token.Storage_Inventory:
			List<Bill> sbills = this.playloungeWarehouseBillDao
					.findWarehouseInventoryBills(unitId, null, null,billDate,conditions, unitId,
							type);
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
		if (CommonUtil.isBlank(bill) ||(( bill.getActQty() == null
				|| bill.getActQty() == 0l)&&( bill.getPreManualQty() == null
				|| bill.getPreManualQty() == 0l))) {
			return null;
		}else {
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
				this.playloungeShopPostBillDao.batchShopInventory(bus);
				try {
					Business otherBus=FilterUtil.copy(bus);
					Bill otherBill=otherBus.getBill();
					if(CommonUtil.isNotBlank(otherBill)){
						if(CommonUtil.isNotBlank(otherBill.getPreManualQty())
								&&otherBill.getPreManualQty()!=0l){
							otherBus.setRemark(otherBill.getSrcBillNo()+";手动输入单");
							otherBill.setRemark(otherBill.getSrcBillNo()+";手动输入单");

							FilterUtil.pickManualBill(otherBus);
							this.playloungeShopPostBillDao.batchShopInventory(otherBus);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case Constant.Token.Storage_Inventory:
				this.playloungeWarehousePostBillDao
						.batchWarehouseInventory(bus);
				try {
					Business otherBus=FilterUtil.copy(bus);
					Bill otherBill=otherBus.getBill();
					if(CommonUtil.isNotBlank(otherBill)){
						if(CommonUtil.isNotBlank(otherBill.getPreManualQty())&&otherBill.getPreManualQty()!=0l){
							otherBus.setRemark(otherBill.getSrcBillNo()+";手动输入单");
							otherBill.setRemark(otherBill.getSrcBillNo()+";手动输入单");
							FilterUtil.pickManualBill(otherBus);
							this.playloungeWarehousePostBillDao.batchWarehouseInventory(otherBus);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			bill.setStatus(Constant.ScmConstant.BillStatus.Finished);
			this.billDao.update(bill);
			return "success";
		}
	}

	@Override
	public MessageBox updateUnitInfo(Business bus) {
        return new MessageBox(false,"");
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
}
