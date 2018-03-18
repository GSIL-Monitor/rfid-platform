package com.casesoft.dmc.extend.api.web.hub;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.hall.HallConstant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.vo.ITag;
import com.casesoft.dmc.core.vo.TagFactory;
import com.casesoft.dmc.model.hall.HallFloor;
import com.casesoft.dmc.model.hall.HallTask;
import com.casesoft.dmc.model.hall.HallTaskDetail;
import com.casesoft.dmc.model.hall.Sample;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.hall.HallFloorService;
import com.casesoft.dmc.service.hall.HallTaskService;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by session on 2017/3/28 0028.
 */
public class SampleUtil {

	private static HallFloorService hallFloorService;

	public static String getbusinessTaskIdList(List<Sample> sampleList) {
		StringBuffer codeBuffer = new StringBuffer("'");
		codeBuffer.append(sampleList.get(0).getBusinessTaskId());
		for (Sample detail : sampleList) {
			if(codeBuffer.indexOf(detail.getBusinessTaskId()) == -1){
				codeBuffer.append("','").append(detail.getBusinessTaskId());
			}
		}
		codeBuffer.append("'");
		return codeBuffer.toString();
	}

	public static HallTask initTask(HallTask task, HallTaskService service, String deviceId, Integer type, List<HallTaskDetail> details) {

		task.setType(type);// 0:借出 1:返还 3.初始化入库 4调拨入库5投产下单6调拨出库7、补标入库
		HallTaskDetail detail0 = details.get(0);
		if(CommonUtil.isBlank(detail0.getTaskId())) {
			String taskIdPrefix = HallConstant.getTaskPrefix(task.getType())
					+ CommonUtil.getDateString(new Date(), "yyMMdd");
			task.setId(service.findTaskMaxCode(taskIdPrefix));
		}
		if(!CommonUtil.isBlank(detail0.getBusinessTaskId())) {
			task.setSrcTaskId(detail0.getBusinessTaskId());
		}
		task.setDeviceId(deviceId);
		task.setScanDate(new Date());
		task.setQty(details.size());
//		task.setOwnerId(CacheManager.getDeviceByCode(deviceId).getStorageId());//所属样衣间
		for (HallTaskDetail detail : details) {
			if(CommonUtil.isBlank(detail.getId())){
				detail.setId(new GuidCreator().toString());
			}
			detail.setOwnerId(task.getOwnerId());
			detail.setType(task.getType());
			detail.setTaskId(task.getId());
			detail.setTagPrice(CacheManager.getStyleById(detail.getStyleId()).getPrice());
			switch (type) {
				case 0:// 0:借出
				case 5:// 5投产下单
				case HallConstant.TaskType.Transfer_Outbound:// 6调拨出库
					detail.setBorrowTaskId(task.getId());
					detail.setBorrowDeviceId(task.getDeviceId());
					detail.setBorrowDate(task.getScanDate());
					detail.setBackStatus(0);
					break;
				case HallConstant.TaskType.Back:
				case HallConstant.TaskType.Init_Inbound:
					detail.setBackDate(task.getScanDate());
					detail.setBackDeviceId(task.getDeviceId());
					detail.setBackTaskId(task.getId());

					break;
				case HallConstant.TaskType.Transfer_Inbound:
				case HallConstant.TaskType.Force_Inbound:
					detail.setBackDate(task.getScanDate());
					detail.setBackDeviceId(task.getDeviceId());
					detail.setBackTaskId(task.getId());
					detail.setBackStatus(HallConstant.TaskType.Back);
					task.setCustomerId(detail.getBusinessOwnerId());
					detail.setCustomerId(detail.getBusinessOwnerId());
					break;
			}

		}
		task.setDetailList(details);
		return task;
	}

	public static Sample convertToVo(Sample dtl) {
		Product p = CacheManager.getProductByCode(dtl.getStyleId()+dtl.getColorId()+dtl.getSizeId());
		if(CommonUtil.isNotBlank(p)) {
			dtl.setStyleName("" + p.getStyleName());
			dtl.setColorName("" + p.getColorName());
			dtl.setSizeName("" + p.getSizeName());
		}
		if(CommonUtil.isNotBlank(dtl.getOwnerId())) {
			if(CommonUtil.isNotBlank(CacheManager.getUnitById(dtl.getOwnerId()))) {
				dtl.setUnitName(CacheManager.getUnitById(dtl.getOwnerId()).getName());//样衣间
			}
		}
		if(CommonUtil.isBlank(CacheManager.getFloorByCode(dtl.getFloor()))){
			dtl.setFloorName(dtl.getFloor());
		}else{
			dtl.setFloorName(CacheManager.getFloorByCode(dtl.getFloor()).getName());
		}
//		String imagePath = TagFactory.getCurrentTag()
//				.getImagePath(dtl.getStyleId(), dtl.getColorId(), dtl.getSizeId());
//		dtl.setImage(imagePath);
		String code = dtl.getCode();
		String styleId = dtl.getStyleId();

		if(dtl.getStatus() == HallConstant.SampleStatus.Borrowed) {
			User employee = CacheManager.getUserById(dtl.getBusinessOwnerId());
			if(CommonUtil.isNotBlank(employee)) {
				dtl.setBusinessOwnerNa(employee.getName());
			}
		} else {
			dtl.setBusinessOwnerId("");
		}
		return dtl;
	}

	public static List<Sample> convertToInSample(List<HallTaskDetail> details,String hallFloor) {
		List<Sample> sampleList = new ArrayList<Sample>();
		for(HallTaskDetail detail: details) {
			Style p=CacheManager.getStyleById(detail.getStyleId());
			Sample sample = new Sample();
			sample.setCode(detail.getCode());
			sample.setStatus(HallConstant.SampleStatus.In_Stock);
			sample.setOwnerId(detail.getOwnerId());
			// sample.setBusinessDate(new Date());
			sample.setBusinessTaskId(detail.getTaskId());
			sample.setStyleId(detail.getStyleId());
			sample.setColorId(detail.getColorId());
			sample.setSizeId(detail.getSizeId());
			sample.setRemark(detail.getRemark());
			ITag tag = TagFactory.getCurrentTag();


			tag.setStyleId(detail.getStyleId());
			tag.setColorId(detail.getColorId());
			tag.setSizeId(detail.getSizeId());
			tag.setSku(detail.getStyleId()+detail.getColorId()+detail.getSizeId());
			if(detail.getTagPrice()!=0)
				sample.setTagPrice(detail.getTagPrice());
			else
				sample.setTagPrice(p.getPrice());
			String uniqueCode = tag.getUniqueCode(1,Integer.parseInt(RandomStringUtils.random(3,false,true)));
			String epc = tag.getEpc();
			String secretEpc = tag.getSecretEpc();
			sample.setEpc(secretEpc);




			sample.setBusinessDate(detail.getBackDate());
			sample.setBusinessDeviceId(detail.getBackDeviceId());
			sample.setBusinessOwnerId(detail.getOwnerId());

			sample.setInDate(new Date());
			sample.setInDeviceId(sample.getBusinessDeviceId());
			sample.setInOwnerId(sample.getOwnerId());
			sample.setInTaskId(sample.getBusinessTaskId());
			sample.setFloor(hallFloor);
			sampleList.add(sample);
		}
		return sampleList;
	}

	public static List<Sample> convertToVos(List<Sample> dtlList) {
		for (Sample dtl : dtlList) {
			convertToVo(dtl);
		}
		return dtlList;
	}

	public static void setSampleOwnerNa(List<Sample> sampleList) {
		for (Sample dtl : sampleList) {
			if(dtl.getStatus() == HallConstant.SampleStatus.Borrowed) {
				User employee = CacheManager.getUserById(dtl.getBusinessOwnerId());
				if(CommonUtil.isNotBlank(employee)) {
					dtl.setBusinessOwnerNa(employee.getName());
				}
			}
		}
	}

	public static String addTag(String codeList, String string) {
		String[] codes = codeList.split(",");
		StringBuffer strs = new StringBuffer("'");
		for (String code : codes) {
			if (!CommonUtil.isBlank(code))
				strs.append(code).append("','");
		}
		return strs.append("'").toString();
	}

	public static String addTag(List<HallTaskDetail> details) {
		StringBuffer strs = new StringBuffer("'");
		for (HallTaskDetail detail : details) {
			String code = detail.getCode();
			if (!CommonUtil.isBlank(code))
				strs.append(code).append("','");
		}
		return strs.append("'").toString();
	}

	public static void addEmployeeInfo(List<Sample> sampleList) {
		for(Sample sample : sampleList) {
			if(CommonUtil.isNotBlank(sample.getBusinessOwnerId())) {
				User u = CacheManager.getUserById(sample.getBusinessOwnerId());
				sample.setEmployeeName(u==null?sample.getBusinessOwnerId():u.getName());
				sample.setDepId(u.getOwnerId());
				sample.setBorrowDate(sample.getBusinessDate());
			}
		}
	}

	public static HallTask covertToBackTask(Sample sample, HallTask task, HallTaskService hallTaskService, User user, int status) {

		String taskIdPrefix = HallConstant.getTaskPrefix(HallConstant.TaskType.Back)
				+ CommonUtil.getDateString(new Date(), "yyMMdd");
		String maxid = hallTaskService.findTaskMaxCode(taskIdPrefix);
		task.setId(maxid);
		task.setOprId(user.getCode());
		task.setStatus(HallConstant.TaskType.Back);
		List<HallTaskDetail> details = hallTaskService.findDetailByCode(sample.getCode(),sample.getBusinessTaskId());

		for(HallTaskDetail d: details){
			d.setBackDeviceId(sample.getBusinessDeviceId());
			d.setBorrowTaskId(sample.getBusinessTaskId());
			d.setOwnerId(user.getOwnerId());
			d.setBackStatus(status);

		}
		sample.setBusinessTaskId(maxid);
		return initTask(task,hallTaskService,sample.getBusinessDeviceId(),HallConstant.TaskType.Back,details);
	}
}
