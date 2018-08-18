package com.casesoft.dmc.extend.api.web.hub;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.hall.HallConstant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.json.FastJSONUtil;
import com.casesoft.dmc.core.util.secret.EpcSecretUtil;
import com.casesoft.dmc.core.vo.ITag;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.core.vo.TagFactory;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.model.hall.*;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.hall.*;
import io.swagger.annotations.Api;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.*;

/**
 * Created by WinLi on 2017-03-24.
 */
@Controller
@RequestMapping(value = "/api/hub/hall", method = {RequestMethod.POST, RequestMethod.GET})
@Api(description = "样衣间子系统数据上传和获取接口")
public class HallApiController extends ApiBaseController {

	private static final long serialVersionUID = 1L;

	@Autowired
	private SampleService sampleService;

	@Autowired
	private HallTaskService hallTaskService;

	@Autowired
	private HallFloorService hallFloorService;

	@Autowired
	private HallRoomService hallRoomService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private HallInventoryService hallInventoryService;

	// 上传生产资料文件定义字段
//	private File fileInput;
//	private String fileInputFileName;
//	//下载文件时定义字段
//	private File inputPath;
//
//	private String contentType;
//	private String filename;
//	// jasperreports 相关参数
//	private Map<String, String> reportParameter;
//	private List<Sample> reportDetails;

	@Override
	public String index() {
		return null;
	}

	/**
	 * 提交借用样衣信息
	 * borrowWS
	 *
	 * @param mainInfo
	 * @param detailInfo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/borrowWS.do")
	@ResponseBody
	public synchronized MessageBox borrowWS(String mainInfo, String detailInfo) throws Exception {
		this.logAllRequestParams();
		try {
			HallTask task = JSON.parseObject(mainInfo, HallTask.class);
			List<HallTaskDetail> details = JSON.parseArray(detailInfo, HallTaskDetail.class);
			//先检查所有的样衣是否可借(非本人被预定的、未超期的不能借)
//			String ownerId = this.hallRoomService.findRoomByDeviceId(task.getDeviceId());
			String ownerId = "";
			if (CommonUtil.isNotBlank(CacheManager.getDeviceByCode(task.getDeviceId()))) {
				ownerId = CacheManager.getDeviceByCode(task.getDeviceId()).getStorageId();
			}
			task.setStatus(HallConstant.TaskType.Borrow);
			HallApiUtil.initBorrowTask(this.hallTaskService, task, ownerId, details);
			String codeList = HallApiUtil.getCodeList(details);
			List<Sample> sampleList = this.hallTaskService.findIsCanBorrow(codeList,
					task.getOwnerId(), task.getCustomerId());

			List<Sample> exceptionDetailList = HallApiUtil.findBorrowExceptionDetailList(task, details, sampleList);

			if (exceptionDetailList.size() > 0) {
				return returnFailInfo("存在不能借出的样衣", exceptionDetailList);
			}
			task.setDetailList(details);
			this.hallTaskService.save(task, sampleList);
			return returnSuccessInfo("借出成功", details);


		} catch (Exception e) {
			e.printStackTrace();
			return returnFailInfo("服务器处理错误");
		}

	}

	/**
	 * 获取当前customer 借用的，没有返还的样衣
	 * 实现跨库可还逻辑
	 * listWS
	 *
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/listBorrowedSampleWS.do")
	@ResponseBody
	public MessageBox listWS(String deviceId, String customerId) throws Exception {
		this.logAllRequestParams();
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		String ownerId="";
		if(CommonUtil.isNotBlank(deviceId)){
			ownerId=CacheManager.getDeviceByCode(deviceId).getStorageId();
			filters.add(new PropertyFilter("EQS_ownerId", ownerId));
		}
		filters.add(new PropertyFilter("EQS_customerId", customerId));

		List<HallTaskDetail> details = this.hallTaskService.findDetail(filters);
		HallApiUtil.convertToVos(details);
		return returnSuccessInfo(FastJSONUtil.retainField(details, "code", "styleId",
				"colorId", "sizeId", "styleName", "colorName", "sizeName",
				"epc", "borrowDate", "preBackDate"));
	}

	/**
	 * 提交返还样衣信息
	 * backWS3
	 *
	 * @param detailInfo
	 * @return
	 */
	@RequestMapping(value = "/backWS3.do")
	@ResponseBody
	public synchronized MessageBox backWS3(String detailInfo) {
		this.logAllRequestParams();
		List<Sample> samples = JSON.parseArray(detailInfo, Sample.class);
		List<Sample> sampleList = new ArrayList<>();
		List<HallTask> tasks = new ArrayList<>();
		String taskids = new String();
		String[] taskIds = new String[]{};
		if (!samples.isEmpty()) {
			taskids = SampleUtil.getbusinessTaskIdList(samples);
			taskIds = taskids.split(",");
		}
		List<HallTaskDetail> details = this.hallTaskService.findBorrowList(taskids);
		if (details.size() == 0) {
			return returnFailInfo("存在未借出的样衣");
		}
		List<HallTaskDetail> dtlList = new ArrayList<>();
		String taskIdPrefix = HallConstant.getTaskPrefix(HallConstant.TaskType.Back) + CommonUtil.getDateString(new Date(), "yyMMdd");

		String maxid = this.hallTaskService.findTaskMaxCode(taskIdPrefix);

		Date backDate = new Date();

		Integer size = Integer.valueOf(maxid.substring(taskIdPrefix.length(), maxid.length()));
		for (String taskid : taskIds) {
			HallTask task = new HallTask();
			task.setType(HallConstant.TaskType.Back);
			String id = taskIdPrefix + CommonUtil.convertIntToString(size, 3);
			task.setId(id);
			for (Sample s : samples) {
				if (taskid.indexOf(s.getBusinessTaskId()) != -1) {
					switch (s.getBackStatus()) {
						case HallConstant.BackStatus.BackOk:
							s.setStatus(HallConstant.SampleStatus.In_Stock);
							break;
						case HallConstant.BackStatus.BackLose:
							s.setStatus(HallConstant.SampleStatus.Lost);
							break;
						case HallConstant.BackStatus.BackBad:
							s.setStatus(HallConstant.SampleStatus.Badding);
							break;
						case HallConstant.BackStatus.BackTimeOut:
							s.setStatus(HallConstant.SampleStatus.In_Stock);
							break;
					}

					s.setBusinessDate(backDate);
					String ownerId = "";
					if (CommonUtil.isNotBlank(CacheManager.getDeviceByCode(s.getBusinessDeviceId()))) {
						ownerId = CacheManager.getDeviceByCode(s.getBusinessDeviceId()).getStorageId();
					}
//					String ownerId = this.hallRoomService.findRoomByDeviceId(s.getBusinessDeviceId());
					s.setOwnerId(ownerId);

					for (HallTaskDetail dtl : details) {
						if (dtl.getCode().equals(s.getCode())) {
							dtl.setBorrowTaskId(s.getBusinessTaskId());
							dtl.setCode(s.getCode());
							dtl.setColorId(s.getColorId());
							dtl.setSizeId(s.getSizeId());
							dtl.setStyleId(s.getStyleId());
							dtl.setTagPrice(CacheManager.getStyleById(dtl.getStyleId()).getPrice());
							dtl.setBusinessTaskId(id);
							dtl.setOwnerId(ownerId);
							dtl.setBackStatus(s.getBackStatus());
							dtl.setType(HallConstant.SampleStatus.In_Stock);
							dtlList.add(dtl);
						}
					}
					task.setSrcTaskId(s.getBusinessTaskId());
					task.setOwnerId(ownerId);
					task.setDeviceId(s.getBusinessDeviceId());
					task.setScanDate(backDate);
					task.setStatus(HallConstant.BackStatus.BackOk);
					task.setCustomerId(s.getBusinessOwnerId());
					task.setOprId(s.getBusinessOwnerId());
					s.setBusinessTaskId(id);
					sampleList.add(s);
				}
			}
			tasks.add(task);
			size += 1;
		}
		HallApiUtil.initBackTask(tasks, dtlList, backDate);
		this.hallTaskService.save(tasks);
		this.hallTaskService.saveSamples(sampleList);
		this.hallTaskService.saveDetails(dtlList);

		return returnSuccessInfo("返回成功", dtlList);
	}


	/**
	 * 提交装箱库位（盘点)信息
	 * adjustInventoryWS
	 *
	 * @param mainInfo
	 * @param detailInfo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/inventoryAdjustWS.do")
	@ResponseBody
	public synchronized MessageBox adjustInventoryWS(String mainInfo, String detailInfo) throws Exception {
		this.logAllRequestParams();
		HallInventory inv = JSON.parseObject(mainInfo, HallInventory.class);
		//其他实体中存储的deviceId在后台都做deviceCode处理，
//		String ownerId = this.hallRoomService.findRoomByDeviceId(inv.getDeviceId());
		String ownerId = "";
		if (CommonUtil.isNotBlank(CacheManager.getDeviceByCode(inv.getDeviceId()))) {
			ownerId = CacheManager.getDeviceByCode(inv.getDeviceId()).getStorageId();
		}
		if (CommonUtil.isBlank(ownerId)) {
			return returnFailInfo("该设备号未绑定样衣间");
		}

		List<HallInventoryDetail> details = JSON.parseArray(detailInfo, HallInventoryDetail.class);
		String codeList = "";
		String prefix = HallConstant.Code.Inventory_Prefix + CommonUtil.getDateString(new Date(), "yyMMdd");
		String taskId = this.hallTaskService.findBillMaxCode(prefix);

		inv.setTaskId(taskId);
		inv.setOwnerId(ownerId);
		for (HallInventoryDetail dtl : details) {
			dtl.setCode(dtl.getBarCode());
			if (codeList != "") {
				codeList += ",";
			}
			codeList += dtl.getBarCode();
		}
		String codes = CommonUtil.addQuotes(codeList.split(","));
		List<Sample> list = this.hallTaskService.findSampleByCodes(codes, ownerId);//根据code检索样衣信息
		//原实体为Storage
		HallFloor s = this.hallTaskService.findFloorByOnerwId(ownerId);
		HallFloor floor = new HallFloor();
		//原storage中有lock字段，parent字段，处理方式：注释
		HallFloor hallArea = this.hallFloorService.findDefaultArea(ownerId);//将所属方修改为默认分区
//		floor.setOwnerId();
		if (CommonUtil.isBlank(hallArea)) {
			return returnFailInfo("装箱失败，请联系后台工作人员添加默认装箱分区");
		}
		floor.setOwnerId(hallArea.getCode());
		floor.setCode(this.hallFloorService.findMaxCode("E", hallArea.getCode()));
		HallApiUtil.adjustFloor(floor, inv, list, details);
		try {
			this.hallTaskService.save(floor, list, inv, details);
		} catch (Exception e) {
			return returnFailInfo("该箱已装箱");
		}
		/*CacheManager.refreshHallFloor();*/
		return returnSuccessInfo(inv.getFloor() + "装箱成功");
	}

	/**
	 * 提交入库唯一码信息
	 * inboundWS
	 *
	 * @param deviceId
	 * @param detailInfo
	 * @param type
	 * @return
	 */

	@RequestMapping(value = "/inboundWS.do")
	@ResponseBody
	public synchronized MessageBox inboundWS(String deviceId, String detailInfo, String type) {
		this.logAllRequestParams();
		try {
			String mainInfo = this.getReqParam("mainInfo");
			HallTask task = JSON.parseObject(mainInfo, HallTask.class);
			if (CommonUtil.isBlank(task.getStatus()))
				task.setStatus(0);
			List<HallTaskDetail> details = JSON.parseArray(detailInfo, HallTaskDetail.class);

			Integer _type = new Integer(type);
			String codeList = SampleUtil.addTag(details);
			String ownerId = "";
			if (CommonUtil.isNotBlank(CacheManager.getDeviceByCode(task.getDeviceId()))) {
				ownerId = CacheManager.getDeviceByCode(task.getDeviceId()).getStorageId();
			}
			task.setOwnerId(ownerId);
			if("".equals(ownerId))
				return returnFailInfo("该设备未注册");
			HallFloor hallArea = this.hallFloorService.findDefaultArea(ownerId);
			if(CommonUtil.isBlank(hallArea)){
				return returnFailInfo("请先联系后台工作人员设置默认分区");
			}
			HallFloor hallFloor = this.hallFloorService.findDefaultFloor(hallArea.getCode());
			if (_type == HallConstant.TaskType.Init_Inbound) {// type=3 为初始化入库 4为调拨入库、、0:借出 1:返还
				// 3.初始化入库 4调拨入库5投产下单6调拨出库
				task = SampleUtil.initTask(task, this.hallTaskService, deviceId, _type, details);
				if(CommonUtil.isBlank(hallFloor)){
					return returnFailInfo("请先联系后台工作人员设置默认库位");
				}
				List<Sample> sampleList = SampleUtil.convertToInSample(details, hallFloor.getCode());
				this.sampleService.save(task, sampleList);


			} else if (_type == HallConstant.TaskType.Transfer_Inbound) {//调拨入库
				task = SampleUtil.initTask(task, this.hallTaskService, deviceId, _type, details);
				this.sampleService.toInbound(task, codeList);
			} else {
				String aownerId = "";
				if (CommonUtil.isNotBlank(CacheManager.getDeviceByCode(deviceId))) {
					aownerId = CacheManager.getDeviceByCode(deviceId).getStorageId();
				}
				task.setOwnerId(aownerId);
				task = SampleUtil.initTask(task, this.hallTaskService, deviceId, _type, details);
				this.sampleService.toForceInbound(task, codeList);
			}

			return returnSuccessInfo("入库成功！", details);
		} catch (Exception e) {
			e.printStackTrace();
			return returnFailInfo("服务器端处理错误");
		}
	}

	/**
	 * 提交出库（调拨、报损)唯一码信息
	 * outboundWS
	 *
	 * @param deviceId
	 * @param detailInfo
	 * @param unit2Id
	 * @param type
	 * @param mainInfo
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/outboundWS.do")
	@ResponseBody
	public synchronized MessageBox outboundWS(String deviceId, String detailInfo, String unit2Id, String type, String mainInfo) throws Exception {
		this.logAllRequestParams();
		try {
			HallTask task = JSON.parseObject(mainInfo, HallTask.class);
			List<HallTaskDetail> details = JSON.parseArray(detailInfo, HallTaskDetail.class);
			task.setStatus(HallConstant.TaskType.Transfer_Outbound);
			String ownerId = "";
			if (CommonUtil.isNotBlank(CacheManager.getDeviceByCode(deviceId))) {
				ownerId = CacheManager.getDeviceByCode(deviceId).getStorageId();
			}
			for (HallTaskDetail d : details) {
				String code = d.getCode().toUpperCase();
				d.setCode(code);
				d.setOwnerId(ownerId);

			}
			//检查样衣状态，查看是否可以出库
			Integer _type = Integer.parseInt(type);
			String taskIdPrefic = HallConstant.getTaskPrefix(_type) + CommonUtil.getDateString(new Date(), "yyMMdd");
			task.setOwnerId(ownerId);
			//初始化盲扫出库
			task = SampleUtil.initTask(task, this.hallTaskService, deviceId, _type, details);

			task.setCustomerId(unit2Id);

			String codeList = HallApiUtil.getCodeList(details);
//			String sampleRoomId = CacheManager.getDeviceByCode(deviceId).getStorageId();
			String sampleRoomId = ownerId;
			List<Sample> inRoomSampleList = this.hallTaskService.findIsInStock(codeList, sampleRoomId, false);
			List<Sample> exceptionDetailList = HallApiUtil.findOutboundExceptionDetailList(task, details, inRoomSampleList, _type);
			if (exceptionDetailList.size() > 0) {
				return returnFailInfo("存在不能出库的样衣", exceptionDetailList);
			} else {
				for (Sample sample : inRoomSampleList) {
					sample.setOwnerId(ownerId);
					sample.setInOwnerId(unit2Id);
				}
			}

			this.hallTaskService.save(task, inRoomSampleList);
			return returnSuccessInfo("出库完成", details);
		} catch (Exception e) {
			e.printStackTrace();
			return returnFailInfo("服务器端出现错误");
		}
	}
//单品追踪

	/**
	 * 单品追踪
	 * listTrackWS
	 *
	 * @return
	 */
	//接口数据正常，客户端解析报错
	@RequestMapping(value = "listTrackWS.do")
	@ResponseBody
	public MessageBox listTrackWS() {
		this.logAllRequestParams();
//		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
		List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
		String code=this.getReqParam("filter_LIKES_code");
		String floor=this.getReqParam("floor");
		filters.add(new PropertyFilter("LIKES_code",code));
		if(CommonUtil.isNotBlank(floor)&&!"".equals(floor)){
			filters.add(new PropertyFilter("EQS_floor",floor));
		}
		String deviceId = this.getReqParam("deviceId");
		if (!CommonUtil.isBlank(deviceId)) {
			String ownerId = CacheManager.getDeviceByCode(deviceId).getStorageId();//getOwnerId();
			filters.add(new PropertyFilter("EQS_ownerId", ownerId));
		}
		List<Sample> sampleList = this.hallTaskService.findSample(filters);
		if (CommonUtil.isNotBlank(sampleList)) {
			SampleUtil.convertToVos(sampleList);
		}
		return returnSuccessInfo("查询成功！", sampleList);
	}

	/**
	 * 获取部门信息
	 * listDepartmentWS
	 *
	 * @param deviceId
	 * @return
	 */

	@RequestMapping(value = "/listDepartmentWS.do")
	@ResponseBody
	public List<Unit> listDepartmentWS(String deviceId) throws Exception {
		this.logAllRequestParams();

		List<Department> departList = this.departmentService.getAll();
		List<Unit> unitList = new ArrayList<>();
		for (Department department : departList) {
			Unit unit = new Unit();
			BeanUtils.copyProperties(department, unit);
			unitList.add(unit);
		}
		return unitList;

	}


	/**
	 * 获取其他样衣间信息
	 * unitComboboxWS
	 *
	 * @param type
	 * @param deviceId
	 * @return
	 */

	@RequestMapping(value = "/listSampleRoomWS.do")
	@ResponseBody
	public List<Unit> unitComboboxWS(String type, String deviceId) {
		this.logAllRequestParams();


//		String SampleRoom = CacheManager.getDeviceByCode(deviceId).getStorageId();

//		List<Unit> unitList = this.hallRoomService.getAll();
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		PropertyFilter filter = new PropertyFilter("EQI_type", type);
		filters.add(filter);
		if (CommonUtil.isNotBlank(deviceId)) {
			String storageId = CacheManager.getDeviceByCode(deviceId).getStorageId();
			filter = new PropertyFilter("NINS_id", storageId);
			filters.add(filter);
		}

		//unitList = this.unitService.find("type", new Integer(type));
		List<Unit> unitList = this.hallRoomService.find(filters);

		return unitList;
	}

	/**
	 * 获取借用人信息列表
	 * listBorrowerWS
	 *
	 * @return
	 */
	@RequestMapping(value = "/listBorrowerWS.do")
	@ResponseBody
	public List<User> listBorrowerWS() {
		this.logAllRequestParams();
		List<Employee> employeeList = this.employeeService.getAll();
		List<User> userList = new ArrayList<>();
		for (Employee employee : employeeList) {
			User user = new User();
			user.setId(employee.getId());
			user.setCode(employee.getCode());
			user.setName(employee.getName());
			user.setOwnerId(employee.getOwnerId());
			userList.add(user);
		}
		return userList;
	}

	/**
	 * 通过唯一码查看借用信息
	 * findBorrowWS
	 *
	 * @param codeList
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findBorrowWS.do")
	@ResponseBody
	public MessageBox findBorrowWS(String codeList) throws Exception {
		this.logAllRequestParams();
		String[] codes = codeList.split(",");
		int size = codes.length;
		String codelst = CommonUtil.addQuotes(codes);
		List<Sample> sampleList = this.hallTaskService.findBorrowSample(codelst);
		if (sampleList.size() < size) {
			return returnFailInfo("存在未借出的样衣");
		}
		SampleUtil.setSampleOwnerNa(sampleList);
		return returnSuccessInfo("ok", sampleList);
	}

	/**
	 * 根据唯一码获取样衣信息
	 * getSampleInfoWS
	 *
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/getSampleInfoWS.do")
	@ResponseBody
	public Sample getSampleInfoWS(String code) {
		this.logAllRequestParams();
		Sample sample = this.sampleService.findByCode(code);
		if (CommonUtil.isNotBlank(sample)) {
			/*sample.setFloorName(CacheManager.getFloorByCode(sample.getFloor()).getName());*/
			String imagePath = TagFactory.getCurrentTag()
					.getImagePath(sample.getStyleId(), sample.getColorId(), sample.getSizeId());
			sample.setImage(imagePath);
		}
		return sample;
	}

	/**
	 * 装箱盘点获取样衣信息
	 * getSampleWS
	 *
	 * @param code
	 * @param deviceId
	 * @return
	 */
	@RequestMapping(value = "/getSampleWS.do")
	@ResponseBody
	public Sample getSampleWS(String code, String deviceId) {
		this.logAllRequestParams();
		String ownerId = "";
		if (CommonUtil.isNotBlank(CacheManager.getDeviceByCode(deviceId))) {
			ownerId = CacheManager.getDeviceByCode(deviceId).getStorageId();
		}

		Sample sample = this.hallTaskService.findSample(code, ownerId);
		if (CommonUtil.isNotBlank(sample)) {
			/*HallFloor floor = CacheManager.getFloorByCode(sample.getFloor());
			if (CommonUtil.isNotBlank(floor)) {
				sample.setFloorName(CacheManager.getFloorByCode(sample.getFloor()).getName());
			} else {
				sample.setFloorName("test01");
			}*/
			String imagePath = TagFactory.getCurrentTag().getImagePath(sample.getStyleId(), sample.getColorId(), sample.getSizeId());
			sample.setImage(imagePath);
		}
		return sample;
	}

	/**
	 *获取盘点信息
	 * @return
	 */
	@RequestMapping(value = "/listInvWS.do")
	@ResponseBody
	public MessageBox listInvWS() {
		this.logAllRequestParams();

//		List<PropertyFilter> filters =PropertyFilter.buildFromHttpRequest(this.getRequest());
		List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
		String floor=this.getReqParam("filter_EQS_floor");
		String deviceId =this.getReqParam("deviceId");
		if(CommonUtil.isNotBlank(floor)&&!"".equals(floor)){
			filters.add(new PropertyFilter("EQS_floor",floor));
		}
		filters.add(new PropertyFilter("EQI_status",String.valueOf(HallConstant.SampleStatus.In_Stock)));
		Boolean isNull=true;
		if (CommonUtil.isNotBlank(CacheManager.getDeviceByCode(deviceId))) {
			String ownerId = CacheManager.getDeviceByCode(deviceId).getStorageId();
			filters.add(new PropertyFilter("EQS_ownerId", ownerId));
			isNull=false;
		}
		if(isNull){
			return returnFailInfo("设备号不存在");
		}

		List<Sample> sampleList = this.sampleService.find(filters);
		if (CommonUtil.isNotBlank(sampleList)) {
			SampleUtil.convertToVos(sampleList);
		}
		return returnSuccessInfo("ok",sampleList);
	}



	/**
	 * 提交盘点数据
	 * @param mainInfo
	 * @param detailInfo
	 * @return
	 */
@RequestMapping(value = "/saveWS.do")
@ResponseBody
	public synchronized MessageBox saveInvWS(String mainInfo,String detailInfo){
		this.logAllRequestParams();

		HallInventory inv= JSON.parseObject(mainInfo,HallInventory.class);
		List<HallInventoryDetail> detailList = JSON.parseArray(detailInfo,HallInventoryDetail.class);
		for(HallInventoryDetail hid:detailList){
			if(hid.getStatus()==10){
				hid.setStatus(-1);
			}
		}
		String prefx =HallConstant.Code.Inventory_Prefix+CommonUtil.getDateString(new Date(),"yyMMdd");
		String taskId=this.hallInventoryService.findBillMaxCode(prefx);

		String ownerId="";
		if(CommonUtil.isNotBlank(CacheManager.getDeviceByCode(inv.getDeviceId()))) {
			ownerId = CacheManager.getDeviceByCode(inv.getDeviceId()).getStorageId();
		}
		HallFloor hallArea=this.hallFloorService.findDefaultArea(ownerId);
		if(CommonUtil.isBlank(hallArea)){
			return returnFailInfo("请先联系后台工作人员设置默认分区");
		}
		HallFloor hallFloor=this.hallFloorService.findDefaultFloor(hallArea.getCode());
		if(CommonUtil.isBlank(hallFloor)){
			return returnFailInfo("请先联系后台工作人员设置默认库位");
		}
		inv.setFloor(hallFloor.getCode());
		inv.setTaskId(taskId);
		inv.setOwnerId(ownerId);
		HallApiUtil.initInv(inv,detailList,ownerId,hallFloor);

		try{
			this.hallInventoryService.save(inv,detailList);
			return returnSuccessInfo("保存成功");

		}catch (Exception e){
			return returnFailInfo("保存失败");
		}
	}

	private void convertToVo(List<HallFloor> storageList) {
		Iterator var3 = storageList.iterator();

		while (var3.hasNext()) {
			HallFloor hallFloor = (HallFloor) var3.next();
			Unit unit = CacheManager.getUnitById(hallFloor.getOwnerId());
			if (!CommonUtil.isBlank(unit)) {
				hallFloor.setUnitName(unit.getName());
			}
		}

	}

	/**
	 * 提交唯一码与库位绑定信息
	 * adjustFloorWS
	 *
	 * @param code
	 * @param adjustFloorId
	 * @return
	 */
	@RequestMapping(value = "/adjustFloorWS.do")
	@ResponseBody
	public synchronized MessageBox adjustFloorWS(String code, String adjustFloorId) {
		this.logAllRequestParams();

		this.hallTaskService.updateSampleFloor(adjustFloorId, code);
		return returnSuccessInfo("调整成功");
	}


	/**
	 * 根据唯一码获取内部订单号！！！->唯一码已更改吗，需用款色码拼接字符串代替
	 * getBillInfoWS
	 *
	 * @param filter_EQS_code
	 * @return
	 */

	@RequestMapping(value = "/getBillInfoWS.do")
	@ResponseBody
	public MessageBox getBillInfoWS(String filter_EQS_code) {
		this.logAllRequestParams();
		String sku = filter_EQS_code.trim();
		Product product = CacheManager.getProductByCode(sku);
		Map<String, String> resultMap = new HashMap<String, String>();
		if (CommonUtil.isNotBlank(product)) {
			resultMap.put("styleName", CacheManager.getStyleNameById(product.getStyleName()));
			resultMap.put("colorName", CacheManager.getColorNameById(product.getColorName()));
			resultMap.put("sizeName", CacheManager.getSizeNameById(product.getSizeName()));
			resultMap.put("styleId", product.getStyleId());
			resultMap.put("colorId", product.getColorId());
			resultMap.put("sizeId", product.getSizeId());
		} else {
			return returnFailInfo("未获取到商品信息");
		}

		ITag tag = TagFactory.getCurrentTag();
		tag.setSku(sku);
		tag.setStyleId(product.getStyleId());
		tag.setColorId(product.getColorId());
		tag.setSizeId(product.getSizeId());
		String seqNo = CommonUtil.randomNumeric(5);
		String uniqueCode = tag.getUniqueCode(1,Integer.parseInt(seqNo));
		String epc = tag.getEpc();
		String secretEpc = EpcSecretUtil.encodeEpc(epc);


		//判断是否唯一
		boolean isUnique = (this.sampleService.get("code", uniqueCode) == null);
		if (!isUnique) {
			return returnFailInfo("唯一码已入库");
		}

		String billNo = CommonUtil.randomNumeric(5);
		if (CommonUtil.isBlank(billNo)) {
			return returnFailInfo("没有对应的内部订单号");
		}


		resultMap.put("billNo", billNo);
		resultMap.put("epc", secretEpc);
		resultMap.put("uniqueCode", uniqueCode);


		return returnSuccessInfo("获取成功!", resultMap);
	}

	/**
	 * 调拨出追回功能
	 * outboundBackWS
	 *
	 * @param filter_EQS_code
	 * @param filter_EQS_deviceId
	 * @param type
	 * @return
	 */

	@RequestMapping(value = "/outboundBackWS.do")
	@ResponseBody
	public MessageBox outboundBackWS(String filter_EQS_code, String filter_EQS_deviceId, String type) {
		this.logAllRequestParams();
		String code = filter_EQS_code;
		String deviceId = filter_EQS_deviceId;
		Integer _type = Integer.parseInt(type);
		String outboundsampleRoomId = "";
		if (CommonUtil.isNotBlank(CacheManager.getDeviceByCode(deviceId))) {
			outboundsampleRoomId = CacheManager.getDeviceByCode(deviceId).getStorageId();
		}
		List<Sample> sampleList = null;

		if (_type == HallConstant.TaskType.Transfer_Outbound) {
			_type = HallConstant.SampleStatus.Transfer_Outbound;
			sampleList = this.hallTaskService.findOutboundCodeListByCode(code, _type, outboundsampleRoomId);
		}
		return returnSuccessInfo("成功", sampleList);
	}


	/**
	 * 获取补标的唯一码信息
	 * listBadTagWS
	 *
	 * @param filter_EQS_status
	 * @param deviceId
	 * @return
	 */

	@RequestMapping(value = "/listBadTagWS.do")
	@ResponseBody
	public MessageBox listBadTagWS(String filter_EQS_status, String deviceId) {
		this.logAllRequestParams();
		if (CommonUtil.isNotBlank(deviceId)) {
			if (CommonUtil.isNotBlank(CacheManager.getDeviceByCode(deviceId))) {
				String ownerId = CacheManager.getDeviceByCode(deviceId).getStorageId();//getOwnerId();
				List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
				filters.add(new PropertyFilter("EQS_ownerId", ownerId));
				filters.add(new PropertyFilter("EQI_status", "7"));
				List<Sample> sampleList = this.sampleService.find(filters);
				if (CommonUtil.isNotBlank(sampleList)) {
					SampleUtil.convertToVos(sampleList);
					return returnSuccessInfo("ok", sampleList);
				}
			}
		}
		return returnFailInfo("无数据");
	}

	/**
	 * 提交补标的唯一码信息
	 * supplyTagInboundWS
	 *
	 * @param deviceId
	 * @param detailInfo
	 * @param type
	 * @param mainInfo
	 * @return
	 */
	@RequestMapping(value = "/supplyTagInboundWS.do")
	@ResponseBody
	public synchronized MessageBox supplyTagInboundWS(String deviceId, String detailInfo, String type, String mainInfo) {
		this.logAllRequestParams();
		try {
			HallTask task = JSON.parseObject(mainInfo, HallTask.class);

			List<HallTaskDetail> details = JSON.parseArray(detailInfo, HallTaskDetail.class);

			Integer _type = Integer.parseInt(type);
			String codeList = SampleUtil.addTag(details);

			task = SampleUtil.initTask(task, this.hallTaskService, deviceId, _type, details);
			String sampleRoomId = "";
			if (CommonUtil.isNotBlank(CacheManager.getDeviceByCode(deviceId))) {
				sampleRoomId = CacheManager.getDeviceByCode(deviceId).getStorageId();
			}
			task.setOwnerId(sampleRoomId);
			task.setStatus(HallConstant.TaskStatus.UnConfirm);
			List<Sample> sampleList = this.hallTaskService.findInRoom(codeList, sampleRoomId);

			for (HallTaskDetail detail : task.getDetailList()) {
				detail.setOwnerId(sampleRoomId);
			}
			for (Sample sample : sampleList) {
				sample.setStatus(HallConstant.SampleStatus.In_Stock);
				sample.setBusinessOwnerId(sampleRoomId);
				sample.setBusinessDeviceId(deviceId);
				sample.setBusinessTaskId(task.getId());
				sample.setBusinessDate(new Date());
			}
			this.hallTaskService.save(task, sampleList);

			return returnSuccessInfo("入库成功", details);
		} catch (Exception e) {
			e.printStackTrace();
			return returnFailInfo("服务器端处理错误");
		}
	}

	/**
	 * 根据唯一码获取单据(入库/调拨出/借出归还)明细
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findDtlListByCodeWS.do")
	@ResponseBody
	public MessageBox findDtlListByCodeWS() throws Exception {
		this.logAllRequestParams();
		String code = this.getReqParam("filter_EQS_code");
		int type = Integer.parseInt(this.getReqParam("type"));
		String deviceId = this.getReqParam("filter_EQS_deviceId");
		String sampleRoomId = CacheManager.getDeviceByCode(deviceId).getStorageId();
		List<Sample> sampleList = null;

		switch (type) {
			case HallConstant.TaskType.Transfer_Outbound:
				type = HallConstant.SampleStatus.In_Stock;
				sampleList = this.sampleService.findDtlListByCode(code, type, sampleRoomId);
				break;
			case HallConstant.TaskType.Transfer_Inbound:
				type = HallConstant.SampleStatus.Transfer_Outbound;
				sampleList = this.sampleService.findDtlListByCode(code, type, sampleRoomId);
				break;
			case HallConstant.TaskType.Back:
				type = HallConstant.SampleStatus.Borrowed;
				sampleList = this.sampleService.findDtlListByCode(code, type);
				SampleUtil.addEmployeeInfo(sampleList);
				break;
			default:
				type = -1;
				break;
		}
		return returnSuccessInfo("成功", sampleList);
	}

}
