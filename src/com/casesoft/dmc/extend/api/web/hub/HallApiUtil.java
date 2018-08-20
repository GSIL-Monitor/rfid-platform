package com.casesoft.dmc.extend.api.web.hub;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.hall.HallConstant;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.vo.ITag;
import com.casesoft.dmc.core.vo.TagFactory;
import com.casesoft.dmc.model.hall.*;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.hall.HallTaskService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by session on 2017/3/27 0027.
 */
public class HallApiUtil {


	public static String getCodeList(List<HallTaskDetail> details) {
		StringBuffer codeBuffer = new StringBuffer("'");
		for (HallTaskDetail detail : details) {
			codeBuffer.append(detail.getCode()).append("'").append(",'");
		}
		codeBuffer.append("'");
		return codeBuffer.toString();
	}

	//初始化入库未获取库位
	public static HallTask initInboundTask(HallTaskService service,
										   HallTask task, List<HallTaskDetail> details, List<Sample> sampleList) {

		// String taskId = new GuidCreator().toString();
		Date initDate = new Date();
		task.setType(HallConstant.TaskType.Init_Inbound);// // 0:借出 1:返还 3.初始化入库
		// 4调拨入库5投产下单6调拨出库
		task.setQty(details.size());
		task.setScanDate(initDate);
		// task.setTaskId(taskId);
		String taskIdPrefix = HallConstant.getTaskPrefix(task.getType())
				+ CommonUtil.getDateString(new Date(), "yyMMdd");
		task.setId(service.findTaskMaxCode(taskIdPrefix));

		// sampleList = new ArrayList<Sample>();
		for (HallTaskDetail detail : details) {
			detail.setType(task.getType());
			detail.setId(new GuidCreator().toString());
			detail.setBackTaskId(task.getId());
			detail.setBackDeviceId(task.getDeviceId());
			detail.setBackDate(initDate);
			detail.setOwnerId(task.getOwnerId());
			detail.setTaskId(task.getId());

			Sample s = new Sample();
			s.setCode(detail.getCode());
			s.setStyleId(detail.getStyleId());
			s.setColorId(detail.getColorId());
			s.setSizeId(detail.getSizeId());
			s.setOwnerId(detail.getOwnerId());
			s.setFloor(task.getFloorId());
			s.setStatus(HallConstant.SampleStatus.In_Stock);
			ITag tag = TagFactory.getCurrentTag();
			tag.setUniqueCode(s.getCode());
			s.setEpc(tag.getSecretEpc());
			s.setImage(tag.getImagePath(s.getStyleId(), s.getColorId(),
					s.getSizeId()));
			s.setInTaskId(task.getId());
			s.setInOwnerId(task.getOwnerId());
			s.setInDate(initDate);
			s.setInDeviceId(task.getDeviceId());
			sampleList.add(s);
		}
		task.setDetailList(details);

		return task;
	}

	public static void convertToVosGroup(List<HallTaskDetailView> list, String group) throws Exception {
		for (HallTaskDetailView dtl : list) {
			convertToVo2(dtl);
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String preName = null;
			String backStatusName = "";
			switch (group) {
				case "backStatus":
					switch (dtl.getType()) {
						case HallConstant.TaskType.Borrow:
							if (CommonUtil.isNotBlank(dtl.getBackTaskId())) {
								dtl.setBackStatus(1);
							}
							backStatusName = "状态:" + ((dtl.getBackStatus() == HallConstant.Status.Back) ? "已归还" : "未归还");
							break;
						case HallConstant.TaskType.Transfer_Outbound:
							backStatusName = "入库状态:" + ((dtl.getBackStatus() == HallConstant.Status.Back) ? "已入库" : "在途");
							break;
						case HallConstant.TaskType.Back:
							String statusName = "";
							switch (dtl.getBackStatus()) {
								case HallConstant.BackStatus.BackOk:
									statusName = "正常";
									break;
								case HallConstant.BackStatus.BackLose:
									statusName = "丢失";
									break;
								case HallConstant.BackStatus.BackBad:
									statusName = "损坏";
									break;
								case HallConstant.BackStatus.BackTimeOut:
									statusName = "超期";
									break;

							}
							backStatusName = "归还状态:" + statusName;
							break;
						default:
							break;
					}
					dtl.setGroup(backStatusName);
					break;
				case "taskId":
					dtl.setGroup("任务号:" + dtl.getTaskId());
					break;
				case "scanDate":
					dtl.setGroup("任务时间:" + sdFormat.format(dtl.getScanDate()));
					break;
				case "deviceId":
					dtl.setGroup("扫描设备:" + dtl.getDeviceId());
					break;
				case "ownerId":

					switch (dtl.getType()) {
						case HallConstant.TaskType.Back:
						case HallConstant.TaskType.Borrow:
							preName = "样衣间:";
							break;
						case HallConstant.TaskType.Init_Inbound:
							preName = "入库方:";
							break;
						case HallConstant.TaskType.Transfer_Inbound:
						case HallConstant.TaskType.Force_Inbound:
							preName = "调入方:";
							break;
						case HallConstant.TaskType.Produce_Outbound:
						case HallConstant.TaskType.Transfer_Outbound:
							preName = "发货方:";
							break;

						default:
							break;
					}

					dtl.setGroup(preName + CacheManager.getUnitById(dtl.getOwnerId()).getName());


					break;
				case "remark2":
					preName = "部门:";
					dtl.setGroup(preName + dtl.getCenterName());
					break;
				case "customerId":

					if (dtl.getType() == HallConstant.TaskType.Back || dtl.getType() == HallConstant.TaskType.Borrow) {
						/*if (!CommonUtil.isBlank(CacheManager.getUserById(dtl.getCustomerId()))) {
		    				User user = CacheManager.getUserById(dtl.getCustomerId());
		    				dtl.setCustomerName(user.getName());
		    			}*/
						dtl.setGroup("借用人:" + dtl.getCustomerName());
					} else if (dtl.getType() == HallConstant.TaskType.Transfer_Inbound) {
						dtl.setGroup("调出方:" + CacheManager.getUnitById(dtl.getCustomerId()).getName());
					} else {
						dtl.setGroup("收货方:" + CacheManager.getUnitById(dtl.getCustomerId()).getName());
					}
					break;
				case "businessTaskId":
					dtl.setGroup("调出单号:" + dtl.getBusinessTaskId());
					break;
				case "styleId":
					dtl.setGroup("款号:" + dtl.getStyleId());
					break;
				default:
					break;


			}

		}
	}

	private static HallTaskDetailView convertToVo2(HallTaskDetailView dtl) throws Exception {
		// TODO Auto-generated method stub
		Product p = CacheManager.getProductByCode(dtl.getStyleId() + dtl.getColorId() + dtl.getSizeId());
		dtl.setStyleName(p.getStyleName());
		dtl.setColorName(p.getColorName());
		dtl.setSizeName(p.getSizeName());
		dtl.setUnitName(CacheManager.getUnitById(dtl.getOwnerId()).getName());
		if (CommonUtil.isNotBlank(dtl.getFloor())) {
			/*dtl.setFloor(CacheManager.getFloorByCode(dtl.getFloor())
					.getName());*/
		}
		if (CommonUtil.isNotBlank(dtl.getBackOwnerId())) {
			dtl.setBackUnitName(CacheManager.getUnitById(dtl.getBackOwnerId()).getName());
		}

		ITag tag = TagFactory.getTag(PropertyUtil.getValue("tag_name"));
		tag.setUniqueCode(dtl.getCode());
		tag.getEpc();
		dtl.setEpc(tag.getSecretEpc());

		if (dtl.getType() == HallConstant.TaskType.Borrow || dtl.getType() == HallConstant.TaskType.Back) {
			User u = CacheManager.getUserById(dtl.getCustomerId());
			Unit unit = CacheManager.getUnitById(u.getOwnerId());
			if (CommonUtil.isNotBlank(dtl.getBackTaskId()) && dtl.getType() == HallConstant.TaskType.Borrow) {
				dtl.setBackStatus(HallConstant.Status.Back);
			}
			if (CommonUtil.isNotBlank(u)) {
				dtl.setCustomer(u);
				dtl.setCustomerName(u.getName());
				dtl.setRemark2(unit.getCode());//临时作为部门编号
				dtl.setCenterName(unit.getName());
			}
		} else if (dtl.getType() == HallConstant.TaskType.Transfer_Outbound || dtl.getType() == HallConstant.TaskType.Transfer_Inbound || dtl.getType() == HallConstant.TaskType.Force_Inbound) {
			if (CommonUtil.isNotBlank(dtl.getBackTaskId()) && dtl.getType() == HallConstant.TaskType.Transfer_Outbound) {
				dtl.setBackStatus(HallConstant.Status.Back);
			}
			Unit u = CacheManager.getUnitById(dtl.getCustomerId());
			if (u != null)
				dtl.setCustomerName(u.getName());
		}
		return dtl;
	}

	public static void convertToVos(List<HallTaskDetail> rows, List<HallTask> tasks, String group) throws Exception {
		for (HallTaskDetail dtl : rows) {
			convertToVo(dtl);
		}
	}

	public static void convertToVos(List<HallTaskDetail> rows, List<HallTask> tasks) throws Exception {
		for (HallTaskDetail dtl : rows) {
			for (HallTask task : tasks) {
				if (dtl.getTaskId().equals(task.getId())) {
					dtl.setCustomerId(task.getCustomerId());
				}
			}
			convertToVo(dtl);
		}
	}

	public static void convertToVos2(List<HallTaskDetailView> rows) throws Exception {
		// TODO Auto-generated method stub
		for (HallTaskDetailView dtl : rows) {
			convertToVo2(dtl);
		}
	}

	public static void convertToVos(List<HallTaskDetail> rows) throws Exception {
		for (HallTaskDetail dtl : rows) {
			convertToVo(dtl);
		}

	}

	public static HallTaskDetail convertToVo(HallTaskDetail dtl) throws Exception {
//		dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
//		dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
//		dtl.setSizeName(CacheManage.getSizeNameById(dtl.getSizeId()));
		Product p = CacheManager.getProductByCode(dtl.getStyleId() + dtl.getColorId() + dtl.getSizeId());
		dtl.setStyleName(p.getStyleName());
		dtl.setColorName(p.getColorName());
		dtl.setSizeName(p.getSizeName());
		dtl.setUnitName(CacheManager.getUnitById(dtl.getOwnerId()).getName());
		if (CommonUtil.isNotBlank(dtl.getFloorId())) {
			/*dtl.setFloorName(CacheManager.getFloorByCode(dtl.getFloorId())
					.getName());*/
		}

//		ITag tag = TagFactory.getTag(PropertyUtil.getValue("tag_name"));
//		tag.setUniqueCode(dtl.getCode());
//		tag.getEpc();
//		dtl.setEpc(tag.getSecretEpc());

		if (dtl.getType() == HallConstant.TaskType.Borrow) {
			User u = CacheManager.getUserById(dtl.getCustomerId());
			if (u != null) {
				dtl.setCustomer(u);
				dtl.setCustomerName(u.getName());
			}
		} else if (dtl.getType() == HallConstant.TaskType.Transfer_Outbound
				|| dtl.getType() == HallConstant.TaskType.Transfer_Inbound) {
			Unit u = CacheManager.getUnitById(dtl.getCustomerId());
			if (u != null)
				dtl.setCustomerName(u.getName());
		}
		return dtl;
	}


	public static void initBackTask(HallTaskService service, HallTask task,
									List<HallTaskDetail> details, Employee employee) {

		// String taskId = new GuidCreator().toString();
		Date backDate = new Date();
		task.setType(HallConstant.TaskType.Back);// 0:借出 1:返还 3.初始化入库
		// 4调拨入库5投产下单6调拨出库
		// task.setOwnerId(ownerId);// sample room Id
		task.setQty(details.size());
		task.setScanDate(backDate);
		String taskIdPrefix = HallConstant.getTaskPrefix(task.getType())
				+ CommonUtil.getDateString(new Date(), "yyMMdd");
		task.setId(service.findTaskMaxCode(taskIdPrefix));

		HallTaskDetail detail0 = details.get(0);
		if (!CommonUtil.isBlank(detail0.getBusinessTaskId())) {
			task.setSrcTaskId(detail0.getBusinessTaskId());
		}

		for (HallTaskDetail detail : details) {
			detail.setType(1);
			detail.setId(new GuidCreator().toString());
			detail.setBackTaskId(task.getId());
			detail.setBackDeviceId(task.getDeviceId());
			detail.setBackDate(backDate);
			detail.setBackScore(task.getBackScore());
			detail.setOprId(task.getOprId());
			detail.setCustomerId(task.getCustomerId());
			detail.setOwnerId(task.getOwnerId());
			detail.setTaskId(task.getId());

			if (detail.getBackStatus() > 0) {
				//employee.setIsDebt(1);// 欠款
			}
		}


//		employee.setScore((employee.getScore()==null?0:employee.getScore())
//				+(task.getBackScore()==null?0:task.getBackScore()));
//		employee.setScoreCount((employee.getScoreCount()==null?0:employee.getScoreCount())+1);


	}

	public static void initBackTask(HallTaskService service, HallTask task, String ownerId,
									List<HallTaskDetail> details) {
//		String ownerId = CacheManager.getDeviceByCode(task.getDeviceId())
//				.getStorageId();
		// String taskId = new GuidCreator().toString();
		Date backDate = new Date();
		task.setType(HallConstant.TaskType.Back);// 0:借出 1:返还 3.初始化入库
		// 4调拨入库5投产下单6调拨出库
		task.setOwnerId(ownerId);// sample room Id
		task.setQty(details.size());
		task.setScanDate(backDate);
		String taskIdPrefix = HallConstant.getTaskPrefix(task.getType())
				+ CommonUtil.getDateString(new Date(), "yyMMdd");
		task.setId(service.findTaskMaxCode(taskIdPrefix));

		for (HallTaskDetail detail : details) {
			detail.setType(1);
			detail.setId(new GuidCreator().toString());
			detail.setBackTaskId(task.getId());
			detail.setBackDeviceId(task.getDeviceId());
			detail.setBackDate(backDate);
			detail.setCustomerId(task.getCustomerId());
			detail.setOwnerId(ownerId);
			detail.setTaskId(task.getId());
		}

	}

	public static void initBackTask(List<HallTask> tasks,
									List<HallTaskDetail> details, Date backDate) {

		for (HallTask task : tasks) {
			Integer borrowQty = 0;
			Integer lostQty = 0;
			Integer badQty = 0;
			Integer overDueQty = 0;
			for (HallTaskDetail detail : details) {

				if (task.getId().equals(detail.getBusinessTaskId())) {

					switch (detail.getBackStatus()) {
						case HallConstant.BackStatus.BackOk:
							borrowQty++;
							break;
						case HallConstant.BackStatus.BackLose:
							lostQty++;
							break;
						case HallConstant.BackStatus.BackBad:
							badQty++;
							break;
						case HallConstant.BackStatus.BackTimeOut:
							overDueQty++;
							break;
						default:
							break;
					}
					detail.setType(1);
					detail.setBackTaskId(task.getId());
					detail.setBackDate(backDate);
					detail.setBackDeviceId(task.getDeviceId());
					detail.setCustomerId(task.getCustomerId());
					detail.setTaskId(task.getId());
				}

			}
			task.setBorrowQty(borrowQty);
			task.setLostQty(lostQty);
			task.setBadQty(badQty);
			task.setOverdueQty(overDueQty);
			task.setQty(borrowQty + lostQty + badQty + overDueQty);


		}

	}

	public static void initBorrowTask(HallTaskService service, HallTask task, String ownerId,
									  List<HallTaskDetail> details) {
//		String ownerId = CacheManager.getDeviceByCode(task.getDeviceId())
//				.getStorageId();// .getOwnerId();
		// String taskId = new GuidCreator().toString();
		Date borrowDate = new Date();
		task.setType(HallConstant.TaskType.Borrow);// // 0:借出 1:返还 3.初始化入库
		// 4调拨入库5投产下单6调拨出库
		task.setOwnerId(ownerId);// sample room Id
		task.setQty(details.size());
		task.setScanDate(borrowDate);
		// task.setTaskId(taskId);
		String taskIdPrefix = HallConstant.getTaskPrefix(task.getType())
				+ CommonUtil.getDateString(new Date(), "yyMMdd");
		task.setId(service.findTaskMaxCode(taskIdPrefix));

		for (HallTaskDetail detail : details) {
			detail.setTagPrice(CacheManager.getStyleById(detail.getStyleId()).getPrice());
			detail.setType(task.getType());
			detail.setId(new GuidCreator().toString());
			detail.setBorrowTaskId(task.getId());
			detail.setBorrowDeviceId(task.getDeviceId());
			detail.setBorrowDate(borrowDate);
			detail.setCustomerId(task.getCustomerId());
			detail.setOwnerId(ownerId);
			detail.setBackStatus(null);
			detail.setPreBackDate(task.getPreBackDate());
			detail.setTaskId(task.getId());
		}
	}

	public static List<Sample> findBorrowExceptionDetailList(HallTask task, List<HallTaskDetail> details, List<Sample> sampleList) {
		List<Sample> exceptionDetailList = new ArrayList<Sample>();
		if (CommonUtil.isNotBlank(sampleList)) {

			for (HallTaskDetail detail : details) {
				boolean isHave = false;
				for (Sample sample : sampleList) {
					if (detail.getCode().equals(sample.getCode()))//如果该code在库
					{
						//未被预借或者预借了，预借人和借用人相同-----暂时去掉预借限制
//	              if((sample.getIsPreBorrow() == HallConstant.SampleStatus.PreBorrowStatus.unPreBorrow) ||
//	              (sample.getIsPreBorrow() == HallConstant.SampleStatus.PreBorrowStatus.preBorrowed
//	              && sample.getPreBorrowUser().equals(task.getCustomerId()) ))
						isHave = true;
						if (sample.getStatus() == HallConstant.SampleStatus.In_Stock)//在厅
						{


							sample.setStatus(HallConstant.SampleStatus.Borrowed);
							sample.setBusinessDeviceId(task.getDeviceId());
							sample.setBusinessOwnerId(task.getCustomerId());
							sample.setBusinessDate(new Date());
							sample.setBusinessTaskId(task.getId());

							sample.setPreBackDate(CommonUtil.getDateString(detail.getPreBackDate(), "yyyy-MM-dd"));//20140801 add wing

							sample.setIsPreBorrow(0);
							sample.setPreBorrowBillId("");
							sample.setPreBorrowDate("");
							sample.setPreBorrowUser("");
							break;
						} else {
							if (sample.getStatus() == HallConstant.SampleStatus.Borrowed) {
								User employee = CacheManager.getUserById(sample.getBusinessOwnerId());
								if (CommonUtil.isNotBlank(employee)) {
									sample.setBusinessOwnerId(employee.getName());
								}
							} else
								sample.setBusinessOwnerId("");
							exceptionDetailList.add(sample);
						}
					}
				}
				if (!isHave) {
					Sample s = new Sample();
					s.setCode(detail.getCode());
					s.setStatus(0);
					exceptionDetailList.add(s);
				}
			}
		} else {
			for (HallTaskDetail detail : details) {
				Sample s = new Sample();
				s.setCode(detail.getCode());
				s.setStatus(0);
				exceptionDetailList.add(s);
			}
		}
		return exceptionDetailList;
	}

	public static List<Sample> findOutboundExceptionDetailList(HallTask task, List<HallTaskDetail> details, List<Sample> sampleList, int type) {
		List<Sample> exceptionDetailList = new ArrayList<Sample>();
		if (CommonUtil.isNotBlank(sampleList)) {
			for (HallTaskDetail detail : details) {
				boolean isHave = false;
				for (Sample sample : sampleList) {
					if (detail.getCode().equals(sample.getCode())) {//如果该code在库

						isHave = true;
						if (sample.getStatus() == HallConstant.SampleStatus.In_Stock) {
							sample.setBusinessOwnerId(sample.getOwnerId());//出库时设置 出方为businessOwnerId
							if (type == HallConstant.TaskType.Transfer_Outbound) {
								sample.setStatus(HallConstant.SampleStatus.Transfer_Outbound);
								sample.setOwnerId(task.getCustomerId());//出库时设置
							} else {
								sample.setStatus(HallConstant.SampleStatus.To_Bill);
							}
							sample.setBusinessDeviceId(task.getDeviceId());
							sample.setBusinessDate(new Date());
							sample.setBusinessTaskId(task.getId());

							sample.setPreBackDate("");

							sample.setIsPreBorrow(0);
							sample.setPreBorrowBillId("");
							sample.setPreBorrowDate("");
							sample.setPreBorrowUser("");
						} else {
							if (sample.getStatus() == HallConstant.SampleStatus.Borrowed) {
								User employee = CacheManager.getUserById(sample.getBusinessOwnerId());
								if (CommonUtil.isNotBlank(employee)) {
									sample.setBusinessOwnerId(employee.getName());
								}
							} else
								sample.setBusinessOwnerId("");
							exceptionDetailList.add(sample);
						}
						break;
					}
				}
				if (!isHave) {
					Sample s = new Sample();
					s.setCode(detail.getCode());
					s.setStatus(0);
					exceptionDetailList.add(s);
				}
			}
		} else {
			for (HallTaskDetail detail : details) {
				Sample s = new Sample();
				s.setCode(detail.getCode());
				s.setStatus(0);
				exceptionDetailList.add(s);
			}
		}
		return exceptionDetailList;
	}

	public static void adjustFloor(HallFloor floor, HallInventory inv,
								   List<Sample> list, List<HallInventoryDetail> details) {
		int qty = list.size();
		int actQty =details.size();
		inv.setActQty(qty);
		inv.setStatus(4);
		inv.setQty(actQty);

		inv.setBillDate(new Date());

		floor.setAreaId(Constant.UnitCodePrefix.SampleRoom);
		floor.setName(inv.getFloor());
		inv.setFloor(floor.getCode());
		floor.setStatus(1);
		for (Sample sample : list) {
			for (HallInventoryDetail dtl : details) {
				dtl.setScanDate(inv.getBillDate());
				String dtlId = new GuidCreator().toString();
				dtl.setId(dtlId);
				dtl.setTaskId(inv.getTaskId());
				dtl.setOwnerId(inv.getOwnerId());
				dtl.setScanStatus(1);
				dtl.setStatus(HallConstant.SampleStatus.In_Stock);
				if (sample.getCode().equals(dtl.getCode())) {
					sample.setFloor(inv.getFloor());
				}
			}
		}

	}

	//-----盘点工具方法
	public static void initInv(HallInventory inv,List<HallInventoryDetail> detailList,String ownerId,HallFloor hallFloor){//获取数据 所属方

		int preQty = 0;
		int actQty = 0;
		inv.setOwnerId(ownerId);
		for(HallInventoryDetail dtl:detailList){
			dtl.setScanDate(new Date());
			String dtlId= new GuidCreator().toString();
			dtl.setId(dtlId);
			dtl.setTaskId(inv.getTaskId());
			dtl.setFloor(hallFloor.getCode());
			dtl.setOwnerId(ownerId);
			if(dtl.getScanStatus()==1){
				actQty++;
			}
			if(dtl.getStatus()==HallConstant.SampleStatus.In_Stock){
				preQty++;
			}
		}
		if(actQty==preQty)
			inv.setStatus(0);
		else if(actQty<preQty)
			inv.setStatus(2);
		else if(actQty>preQty)
			inv.setStatus(1);
		inv.setQty(preQty);
		inv.setActQty(actQty);
		inv.setOwnerId(ownerId);
		inv.setBillDate(new Date());
	}
}
