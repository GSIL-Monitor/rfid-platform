package com.casesoft.dmc.extend.syndemo;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.stock.StockUtil;
import com.casesoft.dmc.controller.syn.tool.BillUtil;
import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.syn.BillDao;
import com.casesoft.dmc.dao.task.TaskDao;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.erp.ErpStock;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.shop.SaleBill;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.BusinessDtl;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.stock.EpcStockService;
import com.casesoft.dmc.service.syn.IBillWSService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by WingLi on 2016-01-19.
 */
@Transactional
public class BillWSDemoService implements IBillWSService {
	private TaskDao taskDao;
	private BillDao billDao;// 本地单据
	private EpcStockService epcStockService;

	@Transactional(readOnly = true)
	@Override
	public List<Bill> findBills(String[] properties, String[] values)
			throws Exception {
		System.out.println(System.currentTimeMillis());
		int type = Integer.parseInt(values[1]);// token 值
		int obType = 0;
		String deviceId = values[5];
		String ownerId = values[0];
		String beginDate = values[2].replaceAll("-", "").replaceAll("/", "");
		String endDate = values[3].replaceAll("-", "").replaceAll("/", "");
		String storageId = "";
		List<Bill> billList = new ArrayList<Bill>();
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		if (CommonUtil.isBlank(CacheManager.getDeviceByCode(deviceId))) {
			return billList;
		}
		storageId = CacheManager.getDeviceByCode(deviceId).getStorageId();

		PropertyFilter filter = new PropertyFilter("LTI_status", ""
				+ Constant.TaskStatus.Confirmed);
		filters.add(filter);

		PropertyFilter filterToken = null;
		List<Business> busList = null;
		switch (type) {
		case Constant.Token.Shop_Inbound:
			obType = Constant.Token.Storage_Outbound;
			filter = new PropertyFilter("EQS_destId", storageId);
			filters.add(filter);
			filterToken = new PropertyFilter("EQI_token", "" + obType);
			filters.add(filterToken);
			busList = this.taskDao.find(filters);
			billList = BillUtil.convertBusToBill(busList, type);
			break;
		case Constant.Token.Shop_Transfer_Inbound:
			obType = Constant.Token.Shop_Transfer_Outbound;
			filter = new PropertyFilter("EQS_destId", storageId);
			filters.add(filter);
			filterToken = new PropertyFilter("EQI_token", "" + obType);
			filters.add(filterToken);
			busList = this.taskDao.find(filters);
			billList = BillUtil.convertBusToBill(busList, type);
			break;
		case Constant.Token.Storage_Transfer_Inbound:
			obType = Constant.Token.Storage_Transfer_Outbound;
			filter = new PropertyFilter("EQS_destId", storageId);
			filters.add(filter);
			filterToken = new PropertyFilter("EQI_token", "" + obType);
			filters.add(filterToken);
			busList = this.taskDao.find(filters);
			billList = BillUtil.convertBusToBill(busList, type);
			break;
		case Constant.Token.Storage_Inbound:
			List<PropertyFilter> filtersWhInbounds = new ArrayList<PropertyFilter>();
			obType = Constant.Token.Factory_Box_Send;
			filterToken = new PropertyFilter("EQI_token", "" + obType);
			filtersWhInbounds.add(filterToken);
			busList = this.taskDao.find(filtersWhInbounds);
			if (CommonUtil.isNotBlank(busList)) {
				for (Business bus : busList) {
					if (CommonUtil.isBlank(bus.getDestUnitId())) {
						bus.setDestUnitId("ZCBJ001");
					}
				}
			}
			billList = BillUtil.convertBusToBill(busList, type);
			break;
		case Constant.Token.Storage_Refund_Inbound:
			obType = Constant.Token.Shop_Refund_Outbound;
			filter = new PropertyFilter("EQS_destId", storageId);
			filters.add(filter);
			filterToken = new PropertyFilter("EQI_token", "" + obType);
			filters.add(filterToken);
			busList = this.taskDao.find(filters);
			billList = BillUtil.convertBusToBill(busList, type);
			break;
		case Constant.Token.Shop_Inventory:
		case Constant.Token.Storage_Inventory:
			storageId = CacheManager.getDeviceByCode(deviceId).getStorageId();
			List<EpcStock> epcStockList = this.epcStockService
					.findEpcStock(storageId);
			billList = StockUtil.conventEpcStocksToBill(type, storageId,
					epcStockList);
			break;
		case Constant.Token.Storage_Outbound:
		case Constant.Token.Storage_Refund_Outbound:
		case Constant.Token.Storage_Transfer_Outbound:

		case Constant.Token.Shop_Refund_Outbound:
		case Constant.Token.Shop_Transfer_Outbound:
			obType = Constant.Token.Storage_Outbound;
			filter = new PropertyFilter("EQS_origId", storageId);
			filters.add(filter);
			filterToken = new PropertyFilter("EQI_type", "" + type);
			filters.add(filterToken);
			billList = this.billDao.find(filters);
			break;
		default:
		}
		BillUtil.convertToVoList(billList);
		System.out.println(System.currentTimeMillis());
		return billList;
	}

	@Transactional(readOnly = true)
	@Override
	public List<BillDtl> findBillDtls(String[] properties, String[] values)
			throws Exception {
		String billId = values[0];
		int type = Integer.parseInt(values[1]);

		List<BillDtl> dtlList = null;
		int obType = 0;
		List<BusinessDtl> busDtlList = null;
		switch (type) {
		case Constant.Token.Shop_Inbound:
			obType = Constant.Token.Storage_Outbound;
			String hql = "from BusinessDtl dtl where dtl.taskId=? and dtl.token=? order by sku";
			busDtlList = this.taskDao
					.find(hql, new Object[] { billId, obType });
			dtlList = BillUtil.convertBusDtlToBillDtl(busDtlList, billId);
			break;
		case Constant.Token.Shop_Transfer_Inbound:
			obType = Constant.Token.Shop_Transfer_Outbound;
			hql = "from BusinessDtl dtl where dtl.taskId=? and dtl.token=? order by sku";
			busDtlList = this.taskDao
					.find(hql, new Object[] { billId, obType });
			dtlList = BillUtil.convertBusDtlToBillDtl(busDtlList, billId);
			break;
		case Constant.Token.Storage_Transfer_Inbound:
			obType = Constant.Token.Storage_Transfer_Outbound;
			hql = "from BusinessDtl dtl where dtl.taskId=? and dtl.token=? order by sku";
			busDtlList = this.taskDao
					.find(hql, new Object[] { billId, obType });
			dtlList = BillUtil.convertBusDtlToBillDtl(busDtlList, billId);
			break;
		case Constant.Token.Storage_Inbound:
			obType = Constant.Token.Factory_Box_Send;
			hql = "from BusinessDtl dtl where dtl.taskId=? and dtl.token=? order by sku";
			busDtlList = this.taskDao
					.find(hql, new Object[] { billId, obType });
			dtlList = BillUtil.convertBusDtlToBillDtl(busDtlList, billId);
			break;
		case Constant.Token.Storage_Refund_Inbound:
			obType = Constant.Token.Shop_Refund_Outbound;
			hql = "from BusinessDtl dtl where dtl.taskId=? and dtl.token=? order by sku";
			busDtlList = this.taskDao
					.find(hql, new Object[] { billId, obType });
			dtlList = BillUtil.convertBusDtlToBillDtl(busDtlList, billId);
			break;
		case Constant.Token.Shop_Inventory:
		case Constant.Token.Storage_Inventory:
			String storageId = billId.split("-")[1];
			List<EpcStock> listEpcStock = this.epcStockService
					.findEpcStock(storageId);
			dtlList = BillUtil.conventSpcStocksToBillDtls(type, listEpcStock);
			break;
		case Constant.Token.Storage_Outbound:
		case Constant.Token.Storage_Refund_Outbound:
		case Constant.Token.Storage_Transfer_Outbound:
		case Constant.Token.Shop_Refund_Outbound:
		case Constant.Token.Shop_Transfer_Outbound:
			dtlList = this.billDao.find("from BillDtl dtl where dtl.billId=?",
					billId);
			break;

		default:
		}
		return dtlList;
	}

	@Override
	public List<BillDtl> findBillDtls(String id) {
		return null;
	}

	@Transactional(readOnly = true)
	@Override
	public String findBillsJSON(String[] properties, String[] values)
			throws Exception {
		List<BillDtl> billDtlList = this.findBillDtls(properties, values);
		return JSON.toJSONString(billDtlList);
	}

	@Override
	public String findBillDtlsJSON(String[] properties, String[] values)
			throws Exception {
		List<BillDtl> billDtlList = this.findBillDtls(properties, values);
		return JSON.toJSONString(billDtlList);
	}

	@Override
	public String findBillDtlsJSON(String id) throws Exception {
		return null;
	}

	@Override
	public MessageBox uploadToERP(Bill bill) {
		bill.setStatus(Constant.TaskStatus.Jointed);
		// this.billService.update(bill, bill.getDtlList());
		return new MessageBox(true,"");
	}

	@Override
	public MessageBox uploadTaskToErp(Business bus) {
		Bill bill = bus.getBill();
		if (CommonUtil.isNotBlank(bill)) {
			bus.setSrcTaskId(bill.getId());
			this.uploadToERP(bill);
		}
        return new MessageBox(true,"");
	}

	@Override
	public MessageBox delete(String id) {
        return new MessageBox(false,"");
	}

	@Override
	public MessageBox update(Business bus) {
        return new MessageBox(false,"");
	}

	@Override
	public MessageBox uploadPosToERP(SaleBill bill) {
        return new MessageBox(true,"");
	}

	public EpcStockService getEpcStockService() {
		return epcStockService;
	}

	public void setEpcStockService(EpcStockService epcStockService) {
		this.epcStockService = epcStockService;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String destroyBill(String[] properties, String[] values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageBox checkEpcStock(Business bus) {
		MessageBox msgBox = new MessageBox(true,"不检查EPC库存");
		switch (bus.getToken().intValue()) {
			case Constant.Token.Storage_Adjust_Inbound:
			case Constant.Token.Storage_Refund_Inbound:
			case Constant.Token.Storage_Inbound:
			case Constant.Token.Shop_Adjust_Inbound:
			case Constant.Token.Shop_Transfer_Inbound:
			case Constant.Token.Shop_Inbound:
			case Constant.Token.Storage_Inbound_agent_refund:
				List<String> codeList = TaskUtil.getRecordCodes(bus.getRecordList());
				String codes = TaskUtil.getSqlStrByList(codeList, EpcStock.class, "code");
				// 未入库的
				List<EpcStock> list = this.getInStock(codes, null);
				List<String> copyList = TaskUtil.getDifferEpcStockCodes(list, codeList);
				if (CommonUtil.isNotBlank(list)) {
					msgBox =  new MessageBox(false,"存在不能入库的唯一码!", JSON.toJSON(copyList));
				}
				break;
			case Constant.Token.Storage_Adjust_Outbound:
			case Constant.Token.Storage_Outbound:
			case Constant.Token.Storage_Transfer_Outbound:
			case Constant.Token.Shop_Adjust_Outbound:
			case Constant.Token.Shop_Refund_Outbound:
			case Constant.Token.Shop_Transfer_Outbound:
			case Constant.Token.Storage_Outbound_agent:
				List<String> ocodeList = TaskUtil.getRecordCodes(bus.getRecordList());
				String ocodes = TaskUtil.getSqlStrByList(ocodeList, EpcStock.class, "code");
				String ostorageId = bus.getOrigId();
				// 未入库的
				List<EpcStock> olist = this.getInStock(ocodes, ostorageId);
				List<String> ocopyList = TaskUtil.getDifferEpcStockCodes(olist, ocodeList);
				if (ocodeList.size() != 0) {
					msgBox =  new MessageBox(false,"存在不能出库的唯一码!",
							JSON.toJSON(ocodeList));
				}
				break;
		}
		return msgBox;
	}

	private List<EpcStock> getInStock(String codes, String warehouse1Id,
									 String warehouse2Id) {
		String hql = "from EpcStock epcstock where epcstock.warehouse2Id=? and epcstock.inStock=0 and ("
				+ codes + ")";
		return this.taskDao.find(hql, new Object[]{warehouse2Id});
	}
	private List<EpcStock> getInStock(String codes, String origId) {

		String hql = "from EpcStock epcstock where  epcstock.inStock=1 and ("
				+ codes + ")";
		if (CommonUtil.isNotBlank(origId)) {
			hql += " and epcstock.warehouseId='" + origId + "'";
		}
		return this.taskDao.find(hql, new Object[]{});
	}

    @Override
	public MessageBox checkEpcStock(String uniqueCodeList, int token, String deviceId) {
		String storageId = "";

		switch (token) {
			case Constant.Token.Storage_Inbound:// = 8;
			case Constant.Token.Storage_Transfer_Inbound:// = 25;

			case Constant.Token.Storage_Refund_Inbound:// = 23;//代理商或门店退给总部

				storageId = CacheManager.getDeviceByCode(deviceId)
						.getStorageId();
				List<String> incodeList_8 = new ArrayList<String>(Arrays
						.asList(uniqueCodeList.split(",")));
				String codes_8 = TaskUtil.getSqlStrByList(incodeList_8,
						EpcStock.class, "code");
				List<EpcStock> list_8 = null;
				list_8 = this.checkInStock(codes_8);// 在库（有记录）
				List<String> ocopyList_8 = TaskUtil.getDifferEpcStockCodes(list_8,
						incodeList_8);
				if (CommonUtil.isNotBlank(ocopyList_8)) {
					return new MessageBox(false,"存在不能入库的唯一码", JSON.toJSON(ocopyList_8));
				}
				break;
			//	case Constant.Token.Storage_Transfer_Inbound:// = 25;
			case Constant.Token.Storage_Adjust_Inbound:// = 29;
				storageId = CacheManager.getDeviceByCode(deviceId)
						.getOwnerId();
			case Constant.Token.Shop_Adjust_Inbound:// = 31;
			case Constant.Token.Shop_Transfer_Inbound:// = 19;
			case Constant.Token.Shop_Sales_refund:// = 17;
				List<String> incodeList = new ArrayList<String>(Arrays.asList(uniqueCodeList.split(",")));
				String codes = TaskUtil.getSqlStrByList(incodeList, EpcStock.class,
						"code");
				if (CommonUtil.isBlank(storageId)) {
					storageId = CacheManager.getDeviceByCode(deviceId)
							.getStorageId();
				}
				// 未入库的
				List<EpcStock> list = this.getInStock(codes, "",
						storageId);
				TaskUtil.getDifferEpcStockCodes(list, incodeList);
				if (CommonUtil.isNotBlank(incodeList)) {
					return new MessageBox(false,"存在不能装箱的唯一码!", JSON.toJSON(incodeList));
				}

				break;
			case Constant.Token.Storage_Refund_Outbound:// = 26;//退货给供应商
			case Constant.Token.Storage_Transfer_Outbound:// = 24;
			case Constant.Token.Storage_Other_Outbound:
			case Constant.Token.Storage_Outbound:// = 10;
			case Constant.Token.Storage_Adjust_Outbound:// = 28;
				storageId = CacheManager.getDeviceByCode(deviceId).getOwnerId();
			case Constant.Token.Shop_TransferSale_Outbound:
            /*storageId=bus.getDestUnitId();*/
			case Constant.Token.Shop_Sales:// = 15;
			case Constant.Token.Shop_Other_Outbound:
			case Constant.Token.Shop_Refund_Outbound:// = 27;
			case Constant.Token.Shop_Adjust_Outbound:// = 30;
			case Constant.Token.Shop_Transfer_Outbound:// = 18;
				if (CommonUtil.isBlank(storageId)) {
					storageId = CacheManager.getDeviceByCode(deviceId)
							.getStorageId();
				}
				// 在库的
				List<String> outcodeList = new ArrayList<String>(Arrays.asList(uniqueCodeList.split(",")));
				String outcodes = TaskUtil.getSqlStrByList(outcodeList,
						EpcStock.class, "code");
				List<EpcStock> outList = this.getInStock(outcodes,
						storageId);// 在库（有记录）
				TaskUtil.getDifferEpcStockCodes(outList, outcodeList);// 在库的唯一码
				if (outcodeList.size() != 0) {
					return new MessageBox(false,"存在不能装箱的唯一码!", JSON.toJSON(outcodeList));
				}
				break;
		}
		return new MessageBox(true,"检测成功！");
	}



	private List<EpcStock> checkInStock(String codes) {
		String hql = "from EpcStock epcstock where  epcstock.inStock=1 and ("
				+ codes + ")";
		return this.taskDao.find(hql, new Object[]{});
	}

	private List<EpcStock> getInStockWS(String codes) {
		String hql = "from EpcStock epcstock where epcstock.inStock=1 and ("
				+ codes + ")";
		return this.taskDao.find(hql, new Object[]{});
	}

	@Override
    public List<Record> findRecordByTask(String taskId){
		String hql = "from Record r where r.taskId=?";
		return this.taskDao.find(hql, new Object[]{taskId});
	}

	@Override
	public MessageBox updateUnitInfo(Business bus) {
		return new MessageBox(false,"");
	}

	public BillDao getBillDao() {
		return billDao;
	}

	public void setBillDao(BillDao billDao) {
		this.billDao = billDao;
	}

	public TaskDao getTaskDao() {
		return taskDao;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}
}
