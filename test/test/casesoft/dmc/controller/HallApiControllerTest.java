package test.casesoft.dmc.controller;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.WebMockUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.web.hub.HallApiController;
import com.casesoft.dmc.model.cfg.Device;
import com.casesoft.dmc.model.hall.HallFloor;
import com.casesoft.dmc.model.hall.HallTaskDetail;
import com.casesoft.dmc.model.hall.Sample;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.cfg.DeviceService;
import com.casesoft.dmc.service.hall.HallFloorService;
import com.casesoft.dmc.service.hall.HallRoomService;
import com.casesoft.dmc.service.hall.HallTaskService;
import com.casesoft.dmc.service.hall.SampleService;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Date;
import java.util.List;

/**
 * Created by session on 2017-04-28.
 */


public class HallApiControllerTest extends AbstractTestCase {
	@Autowired
	HallApiController hallApiController;
	@Autowired
	HallTaskService hallTaskService;
	@Autowired
	SampleService sampleService;

	@Autowired
	HallRoomService hallRoomService;

	@Autowired
	DeviceService deviceService;

	@Autowired
	HallFloorService hallFloorService;
//	public void setUp() throws Exception {
//		BeanContext.initApplicationContext();
//		CacheManager.initCache();
//		PropertyUtil.init();
//		FileUtil.init();// 创建不存在的目录
//	}

	public void iniData() throws Exception {
		Unit sampleRoom=new Unit();//初始化样衣间
		Device device =new Device();//初始化设备
		HallFloor hallArea=new HallFloor();//初始化分区
		HallFloor hallFloor =new HallFloor();//初始化库位

		sampleRoom.setId("HR000");
		sampleRoom.setCreateTime(new Date());
		sampleRoom.setCreatorId("E000");
		sampleRoom.setType(6);
		sampleRoom.setOwnerId("1");
		sampleRoom.setCode(sampleRoom.getId());
		sampleRoom.setName("测试样衣间");
		sampleRoom.setLocked(0);

		device.setId("KZ000000");
		device.setCode(device.getId());
		device.setName(device.getId());
		device.setOwnerId("1");
		device.setStorageId(sampleRoom.getId());
		device.setLocked(0);


		hallArea.setCode("SE000");
		hallArea.setAreaId("A");
		hallArea.setOwnerId(sampleRoom.getId());
		hallArea.setAsDefault(1);

		hallFloor.setCode("SE000000");
		hallFloor.setAreaId("E");
		hallFloor.setOwnerId(hallArea.getCode());
		hallFloor.setAsDefault(1);

		this.hallRoomService.save(sampleRoom);
		this.deviceService.save(device);
		this.hallFloorService.save(hallArea);
		this.hallFloorService.save(hallFloor);
		CacheManager.refreshUnitCache();
		CacheManager.refreshDeviceCache();
//		CacheManager.refreshHallFloor();

	}

	@Test
	public void testInboundWS() throws Exception {
		iniData();
		String mainInfo="{\"DeviceId\":\"KZ000000\",\"Remark\":\"\"}";
		String deviceId="KZ000000";
		String detailInfo="[{\"Epc\":\"FFFFF8FFF0C9EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"Sku\":\"B1BB20401591FA\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"91白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":0,\"BackStatus\":0,\"TagPrice\":0.0,\"Batches\":\"91FA\",\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":0,\"FloorName\":\"\",\"BusinessOwnerId\":\"\",\"StatusInfo\":\"\"}]";
		String type="3";
		MockHttpServletRequest request=(MockHttpServletRequest) WebMockUtil.getHttpServletRequest();
		request.addParameter("mainInfo",mainInfo);
		MessageBox msgBox=this.hallApiController.inboundWS(deviceId,detailInfo,type);
		Sample sample=this.sampleService.getById("0000040003512");
		if(msgBox.getSuccess()){
			hallTaskService.deleteWithInbound("0000040003512","KZ000000");
		}
		request.close();
		Assert.assertTrue(msgBox.getSuccess()&&sample.getStatus()==1);
	}

	/**
	 * 提交借用人信息
	 *
	 * @throws Exception
	 */
	@Test
	public void testborrowWS() throws Exception {
		iniData();
		String mainInfoin="{\"DeviceId\":\"KZ000000\",\"Remark\":\"\"}";
		String deviceIdin="KZ000000";
		String detailInfoin="[{\"Epc\":\"FFFFF8FFF0C9EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"Sku\":\"B1BB20401591FA\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"91白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":0,\"BackStatus\":0,\"TagPrice\":0.0,\"Batches\":\"91FA\",\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":0,\"FloorName\":\"\",\"BusinessOwnerId\":\"\",\"StatusInfo\":\"\"}]";
		String typein="3";
		MockHttpServletRequest request=(MockHttpServletRequest) WebMockUtil.getHttpServletRequest();
		request.addParameter("mainInfo",mainInfoin);
		MessageBox msgBoxIn=this.hallApiController.inboundWS(deviceIdin,detailInfoin,typein);
		request.close();

		String mainInfo = "{\"DeviceId\":\"KZ000000\",\"CustomerId\":\"E005\",\"PreBackDate\":\"2017-05-19\",\"OprId\":\"E005\",\"Remark\":\"\"}";
		String detailInfo = "[{\"Epc\":\"FFFFF8FFF0C9EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"Barcode\":\"B1BB20401591FA00035\",\"Sku\":\"B1BB20401591FA\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"91白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":0,\"BackStatus\":0,\"TagPrice\":0.0,\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":1,\"Remark\":\"\",\"FloorName\":\"一号库位\",\"BusinessOwnerId\":\"\",\"ownerId\":\"HR003\",\"StatusInfo\":\"在库\"}]";
		MessageBox msgBox = this.hallApiController.borrowWS(mainInfo, detailInfo);
		Sample sample=this.sampleService.getById("0000040003512");
		if(msgBoxIn.getSuccess()&&msgBox.getSuccess())
			hallTaskService.deleteWithInbound("0000040003512","KZ000000");
		Assert.assertTrue(msgBox.getSuccess()&&sample.getStatus()==2);
	}

	/**
	 * 获取当前customer 借用的，没有返还的样衣
	 *
	 * @throws Exception
	 */


	@Test
	public void testlistWS() throws Exception {
		iniData();
		//样衣入库
		String mainInfoin="{\"DeviceId\":\"KZ000000\",\"Remark\":\"\"}";
		String deviceIdin="KZ000000";
		String detailInfoin="[{\"Epc\":\"FFFFF8FFF0C9EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"Sku\":\"B1BB20401591FA\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"91白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":0,\"BackStatus\":0,\"TagPrice\":0.0,\"Batches\":\"91FA\",\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":0,\"FloorName\":\"\",\"BusinessOwnerId\":\"\",\"StatusInfo\":\"\"}]";
		String typein="3";
		MockHttpServletRequest request=(MockHttpServletRequest) WebMockUtil.getHttpServletRequest();
		request.addParameter("mainInfo",mainInfoin);
		MessageBox msgBoxIn=this.hallApiController.inboundWS(deviceIdin,detailInfoin,typein);
		request.close();

		//借出样衣
		String mainInfo = "{\"DeviceId\":\"KZ000000\",\"CustomerId\":\"E005\",\"PreBackDate\":\"2017-05-19\",\"OprId\":\"E005\",\"Remark\":\"\"}";
		String detailInfo = "[{\"Epc\":\"FFFFF8FFF0C9EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"Barcode\":\"B1BB20401591FA00035\",\"Sku\":\"B1BB20401591FA\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"91白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":0,\"BackStatus\":0,\"TagPrice\":0.0,\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":1,\"Remark\":\"\",\"FloorName\":\"一号库位\",\"BusinessOwnerId\":\"\",\"ownerId\":\"HR003\",\"StatusInfo\":\"在库\"}]";
		MessageBox msgBox = this.hallApiController.borrowWS(mainInfo, detailInfo);
		//查询借出信息
		String deviceId = "KZ000000";
		String customerId = "E005";
		MessageBox msgBox2 = this.hallApiController.listWS(deviceId, customerId);
		if(msgBoxIn.getSuccess()&&msgBox.getSuccess())
			hallTaskService.deleteWithInbound("0000040003512","KZ000000");
		Assert.assertTrue(msgBox2.getSuccess());
	}

	/**
	 * 提交返还样衣信息
	 */
	@Test
	public void testbackWS3() throws Exception{
		iniData();
		//样衣入库
		String mainInfoin="{\"DeviceId\":\"KZ000000\",\"Remark\":\"\"}";
		String deviceIdin="KZ000000";
		String detailInfoin="[{\"Epc\":\"FFFFF8FFF0C9EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"Sku\":\"B1BB20401591FA\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"91白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":0,\"BackStatus\":0,\"TagPrice\":0.0,\"Batches\":\"91FA\",\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":0,\"FloorName\":\"\",\"BusinessOwnerId\":\"\",\"StatusInfo\":\"\"}]";
		String typein="3";
		MockHttpServletRequest request=(MockHttpServletRequest) WebMockUtil.getHttpServletRequest();
		request.addParameter("mainInfo",mainInfoin);
		MessageBox msgBoxIn=this.hallApiController.inboundWS(deviceIdin,detailInfoin,typein);
		request.close();
		//借出样衣
		String mainInfo = "{\"DeviceId\":\"KZ000000\",\"CustomerId\":\"E005\",\"PreBackDate\":\"2017-05-19\",\"OprId\":\"E005\",\"Remark\":\"\"}";
		String detailInfo1 = "[{\"Epc\":\"FFFFF8FFF0C9EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"Barcode\":\"B1BB20401591FA00035\",\"Sku\":\"B1BB20401591FA\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"91白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":0,\"BackStatus\":0,\"TagPrice\":0.0,\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":1,\"Remark\":\"\",\"FloorName\":\"一号库位\",\"BusinessOwnerId\":\"\",\"ownerId\":\"HR003\",\"StatusInfo\":\"在库\"}]";
		MessageBox msgBox1 = this.hallApiController.borrowWS(mainInfo, detailInfo1);
		List<HallTaskDetail> hallTaskDetailList=(List<HallTaskDetail>)msgBox1.getResult();
		//返还样衣
		String detailInfo = "[{\"InOwnerId\":\"HR003\",\"InDeviceId\":\"KZ000000\",\"BusinessDeviceId\":\"KZ000000\",\"BusinessOwnerNa\":\"张三\",\"Epc\":\"FFFFF8FF4609EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"SkuCount\":0,\"PreBackDate\":\"2017-05-19\",\"BackStatus\":0,\"BusinessTaskId\":\""+hallTaskDetailList.get(0).getTaskId()+"\",\"TaskId\":\""+hallTaskDetailList.get(0).getTaskId()+"\",\"TagPrice\":0.0,\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":2,\"InDate\":\"2017-05-17 11:49:58\",\"Floor\":\"SE006005\",\"FloorName\":\"\",\"BusinessOwnerId\":\"E005\",\"ownerId\":\"HR003\",\"StatusInfo\":\"借出\",\"BusinessDate\":\"2017-05-17 11:50:25\"}]";

		MessageBox msgBox = this.hallApiController.backWS3(detailInfo);
		Sample sample=this.sampleService.getById("0000040003512");
		if(msgBox.getSuccess()){
			this.hallTaskService.deleteWithInbound("0000040003512","KZ000000");
		}
		Assert.assertTrue(msgBox.getSuccess()&&sample.getStatus()==1);
	}

//提交盘点信息
	@Test
	public void testadjustInventoryWS() throws Exception {
		iniData();
		String mainInfoin="{\"DeviceId\":\"KZ000000\",\"Remark\":\"\"}";
		String deviceIdin="KZ000000";
		String detailInfoin="[{\"Epc\":\"FFFFF8FFF0C9EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"Sku\":\"B1BB20401591FA\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"91白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":0,\"BackStatus\":0,\"TagPrice\":0.0,\"Batches\":\"91FA\",\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":0,\"FloorName\":\"\",\"BusinessOwnerId\":\"\",\"StatusInfo\":\"\"}]";
		String typein="3";
		MockHttpServletRequest request=(MockHttpServletRequest) WebMockUtil.getHttpServletRequest();
		request.addParameter("mainInfo",mainInfoin);
		MessageBox msgBoxIn=this.hallApiController.inboundWS(deviceIdin,detailInfoin,typein);
		request.close();

		String mainInfo = "{\"DeviceId\":\"KZ000000\",\"Floor\":\"17051770306047\",\"StatusInfo\":\"\",\"Status\":0}";
		String detailInfo = "[{\"Epc\":\"FFFFF8FFF0C9EFFFFFFFFFFF\",\"Barcode\":\"0000040003512\",\"Sku\":\"B1BB20401591FA\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"91白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":1,\"BackStatus\":0,\"TagPrice\":0.0,\"Batches\":\"91FA\",\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":1,\"Floor\":\"17051770306047\",\"FloorName\":\"一号库位\",\"BusinessOwnerId\":\"\",\"ownerId\":\"HR003\",\"StatusInfo\":\"在库\"}]";
		MessageBox msgBox = this.hallApiController.adjustInventoryWS(mainInfo, detailInfo);
		if(msgBox.getSuccess()){
			//this.hallTaskService.deleteWithInv("0000040003512","KZ000000");
		}
		Assert.assertTrue(msgBox.getSuccess());
	}

//获取其他样衣间信息
	@Test
	public void testUnitComboboxWS() throws Exception{
		iniData();
		String type = "6";
		String deviceId = "KZ000000";
		List<Unit> unitList = this.hallApiController.unitComboboxWS(type, deviceId);
		Assert.assertTrue(unitList.size() > 0);
	}


	/**
	 * 提交出库的唯一码信息
	 *
	 * @throws Exception
	 */
	@Test
	public void testoutboundWS() throws Exception {
		iniData();
		String mainInfoin="{\"DeviceId\":\"KZ000000\",\"Remark\":\"\"}";
		String deviceIdin="KZ000000";
		String detailInfoin="[{\"Epc\":\"FFFFF8FFF0C9EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"Sku\":\"B1BB20401591FA\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"91白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":0,\"BackStatus\":0,\"TagPrice\":0.0,\"Batches\":\"91FA\",\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":0,\"FloorName\":\"\",\"BusinessOwnerId\":\"\",\"StatusInfo\":\"\"}]";
		String typein="3";
		MockHttpServletRequest request=(MockHttpServletRequest) WebMockUtil.getHttpServletRequest();
		request.addParameter("mainInfo",mainInfoin);
		MessageBox msgBoxIn=this.hallApiController.inboundWS(deviceIdin,detailInfoin,typein);
		request.close();

		String deviceId = "KZ000000";
		String detailInfo = "[{\"Epc\":\"FFFFF8FF8E49EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"Sku\":\"B1BB20401591FA\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"91白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":0,\"BackStatus\":0,\"BusinessTaskId\":\"AII170517001\",\"TagPrice\":0.0,\"Batches\":\"91FA\",\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":1,\"InDate\":\"2017-05-17 15:31:38\",\"Floor\":\"SE006005\",\"FloorName\":\"\",\"BusinessOwnerId\":\"HR003\",\"ownerId\":\"HR003\",\"StatusInfo\":\"在库\",\"BusinessDate\":\"2017-05-17 15:31:38\"}]";
		String unit2Id = "HR001";
		String type = "6";
		String mainInfo = "{\"DeviceId\":\"KZ000000\",\"Remark\":\"\"}";
		MessageBox msgBox = this.hallApiController.outboundWS(deviceId, detailInfo, unit2Id, type, mainInfo);
		Sample sample=this.sampleService.getById("0000040003512");
		if(msgBox.getSuccess()){
			this.hallTaskService.deleteWithInbound("0000040003512","KZ000000");
		}

		Assert.assertTrue(msgBox.getSuccess());

	}
	@Test
	public void testlistTrackWS() throws Exception {
		iniData();
		String mainInfoin="{\"DeviceId\":\"KZ000000\",\"Remark\":\"\"}";
		String deviceIdin="KZ000000";
		String detailInfoin="[{\"Epc\":\"FFFFF8FFF0C9EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"Sku\":\"B1BB20401591FA\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"91白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":0,\"BackStatus\":0,\"TagPrice\":0.0,\"Batches\":\"91FA\",\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":0,\"FloorName\":\"\",\"BusinessOwnerId\":\"\",\"StatusInfo\":\"\"}]";
		String typein="3";
		MockHttpServletRequest request1=(MockHttpServletRequest) WebMockUtil.getHttpServletRequest();
		request1.addParameter("mainInfo",mainInfoin);
		MessageBox msgBoxIn=this.hallApiController.inboundWS(deviceIdin,detailInfoin,typein);
		request1.close();

//		String deviceId = "";
//		String floor = "";
		String filter_LIKES_code = "0000040003512";
		MockHttpServletRequest request = (MockHttpServletRequest) WebMockUtil.getHttpServletRequest();
//		request.addParameter("deviceId", deviceId);
//		request.addParameter("floor", floor);
		request.addParameter("filter_LIKES_code", filter_LIKES_code);
		MessageBox msgBox = this.hallApiController.listTrackWS();
		List<Sample> sampleList = (List<Sample>) msgBox.getResult();
		this.hallTaskService.deleteWithInbound("0000040003512","KZ000000");
		request.close();
		Assert.assertTrue(sampleList.size() > 0);
	}


	@Test
	public void testlistBorrowerWS() throws Exception {
		iniData();
		String filter_EQI_isAdmin = "3";
		String filter_LIKES_code = "";
		MockHttpServletRequest request = (MockHttpServletRequest) WebMockUtil.getHttpServletRequest();
		request.addParameter("filter_EQI_isAdmin", filter_EQI_isAdmin);
		request.addParameter("filter_LIKES_code", "");
		List<User> users = this.hallApiController.listBorrowerWS();
		request.close();
		Assert.assertTrue(users.size() > 0);
	}

	/**
	 * 通过唯一码查看借用信息
	 *
	 * @throws Exception
	 */
	@Test
	public void testFindBorrowWS() throws Exception {
		iniData();
//		入库样衣
		String mainInfoin="{\"DeviceId\":\"KZ000000\",\"Remark\":\"\"}";
		String deviceIdin="KZ000000";
		String detailInfoin="[{\"Epc\":\"FFFFF8FFF0C9EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"Sku\":\"B1BB20401591FA\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"91白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":0,\"BackStatus\":0,\"TagPrice\":0.0,\"Batches\":\"91FA\",\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":0,\"FloorName\":\"\",\"BusinessOwnerId\":\"\",\"StatusInfo\":\"\"}]";
		String typein="3";
		MockHttpServletRequest request=(MockHttpServletRequest) WebMockUtil.getHttpServletRequest();
		request.addParameter("mainInfo",mainInfoin);
		MessageBox msgBoxIn=this.hallApiController.inboundWS(deviceIdin,detailInfoin,typein);
		request.close();

		//借出样衣
		String mainInfo = "{\"DeviceId\":\"KZ000000\",\"CustomerId\":\"E005\",\"PreBackDate\":\"2017-05-19\",\"OprId\":\"E005\",\"Remark\":\"\"}";
		String detailInfo1 = "[{\"Epc\":\"FFFFF8FFF0C9EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"Barcode\":\"B1BB20401591FA00035\",\"Sku\":\"B1BB20401591FA\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"91白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":0,\"BackStatus\":0,\"TagPrice\":0.0,\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":1,\"Remark\":\"\",\"FloorName\":\"一号库位\",\"BusinessOwnerId\":\"\",\"ownerId\":\"HR003\",\"StatusInfo\":\"在库\"}]";
		MessageBox msgBox1 = this.hallApiController.borrowWS(mainInfo, detailInfo1);
		List<HallTaskDetail> hallTaskDetailList=(List<HallTaskDetail>)msgBox1.getResult();

		String codeList = "0000040003512";
		MessageBox msgBox = this.hallApiController.findBorrowWS(codeList);
		this.hallTaskService.deleteWithInbound("0000040003512","KZ000000");
		Assert.assertTrue(msgBox.getSuccess());
	}
	@Test
	public void testgetSampleInfoWS() throws Exception {
		iniData();
		//		入库样衣
		String mainInfoin="{\"DeviceId\":\"KZ000000\",\"Remark\":\"\"}";
		String deviceIdin="KZ000000";
		String detailInfoin="[{\"Epc\":\"FFFFF8FFF0C9EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"Sku\":\"B1BB20401591FA\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"91白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":0,\"BackStatus\":0,\"TagPrice\":0.0,\"Batches\":\"91FA\",\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":0,\"FloorName\":\"\",\"BusinessOwnerId\":\"\",\"StatusInfo\":\"\"}]";
		String typein="3";
		MockHttpServletRequest request=(MockHttpServletRequest) WebMockUtil.getHttpServletRequest();
		request.addParameter("mainInfo",mainInfoin);
		MessageBox msgBoxIn=this.hallApiController.inboundWS(deviceIdin,detailInfoin,typein);
		request.close();

		String code = "0000040003512";
		Sample sample = this.hallApiController.getSampleInfoWS(code);
		this.hallTaskService.deleteWithInbound("0000040003512","KZ000000");

		Assert.assertTrue(CommonUtil.isNotBlank(sample));
	}
	@Test
	public void testgetSampleWS() throws Exception {
		iniData();
		String mainInfoin="{\"DeviceId\":\"KZ000000\",\"Remark\":\"\"}";
		String deviceIdin="KZ000000";
		String detailInfoin="[{\"Epc\":\"FFFFF8FFF0C9EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"Sku\":\"B1BB20401591FA\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"91白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":0,\"BackStatus\":0,\"TagPrice\":0.0,\"Batches\":\"91FA\",\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":0,\"FloorName\":\"\",\"BusinessOwnerId\":\"\",\"StatusInfo\":\"\"}]";
		String typein="3";
		MockHttpServletRequest request=(MockHttpServletRequest) WebMockUtil.getHttpServletRequest();
		request.addParameter("mainInfo",mainInfoin);
		MessageBox msgBoxIn=this.hallApiController.inboundWS(deviceIdin,detailInfoin,typein);
		request.close();

		String code = "0000040003512";
		String deviceId = "KZ000000";
		Sample sample = this.hallApiController.getSampleWS(code, deviceId);
		this.hallTaskService.deleteWithInbound("0000040003512","KZ000000");
		Assert.assertTrue(CommonUtil.isNotBlank(sample));
	}
	@Test
	public void testlistInvWS() throws Exception {
		iniData();
		String mainInfoin="{\"DeviceId\":\"KZ000000\",\"Remark\":\"\"}";
		String deviceIdin="KZ000000";
		String detailInfoin="[{\"Epc\":\"FFFFF8FFF0C9EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"Sku\":\"B1BB20401591FA\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"91白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":0,\"BackStatus\":0,\"TagPrice\":0.0,\"Batches\":\"91FA\",\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":0,\"FloorName\":\"\",\"BusinessOwnerId\":\"\",\"StatusInfo\":\"\"}]";
		String typein="3";
		MockHttpServletRequest request1=(MockHttpServletRequest) WebMockUtil.getHttpServletRequest();
		request1.addParameter("mainInfo",mainInfoin);
		MessageBox msgBoxIn=this.hallApiController.inboundWS(deviceIdin,detailInfoin,typein);
		request1.close();

		String floor = "SE008003";
		MockHttpServletRequest request = (MockHttpServletRequest) WebMockUtil.getHttpServletRequest();
		request.addParameter("filter_EQS_floor", floor);
		request.addParameter("deviceId", deviceIdin);
		MessageBox msgBox = this.hallApiController.listInvWS();
		this.hallTaskService.deleteWithInbound("0000040003512","KZ000000");
		request.close();
		Assert.assertTrue(msgBox.getSuccess());
	}

	//保存
	@Test
	public void testSaveInvWS() throws Exception {
		iniData();
		String mainInfoin="{\"DeviceId\":\"KZ000000\",\"Remark\":\"\"}";
		String deviceIdin="KZ000000";
		String detailInfoin="[{\"Epc\":\"FFFFF8FFF0C9EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"Sku\":\"B1BB20401591FA\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"91白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":0,\"BackStatus\":0,\"TagPrice\":0.0,\"Batches\":\"91FA\",\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":0,\"FloorName\":\"\",\"BusinessOwnerId\":\"\",\"StatusInfo\":\"\"}]";
		String typein="3";
		MockHttpServletRequest request1=(MockHttpServletRequest) WebMockUtil.getHttpServletRequest();
		request1.addParameter("mainInfo",mainInfoin);
		MessageBox msgBoxIn=this.hallApiController.inboundWS(deviceIdin,detailInfoin,typein);
		request1.close();

		String mainInfo = "{deviceId:'KZ000000', floor:'', status:2}";
		String detailInfo = "[{'code':'0000040003512','colorId':'91','scaStatus':'1','sizeId':'FA','status':'1','styleId':'B1BB204015'}]";

		MessageBox msgBox = this.hallApiController.saveInvWS(mainInfo, detailInfo);
		this.hallTaskService.deleteWithInv("0000040003512","KZ000000");
		Assert.assertTrue(msgBox.getSuccess());
	}

	@Test
	public void testGetBillInfoWS() throws Exception {
		iniData();
		String mainInfoin="{\"DeviceId\":\"KZ000000\",\"Remark\":\"\"}";
		String deviceIdin="KZ000000";
		String detailInfoin="[{\"Epc\":\"FFFFF8FFF0C9EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"Sku\":\"B1BB20401591FA\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"91白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":0,\"BackStatus\":0,\"TagPrice\":0.0,\"Batches\":\"91FA\",\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":0,\"FloorName\":\"\",\"BusinessOwnerId\":\"\",\"StatusInfo\":\"\"}]";
		String typein="3";
		MockHttpServletRequest request1=(MockHttpServletRequest) WebMockUtil.getHttpServletRequest();
		request1.addParameter("mainInfo",mainInfoin);
		MessageBox msgBoxIn=this.hallApiController.inboundWS(deviceIdin,detailInfoin,typein);
		request1.close();

		String filter_EQS_code = "B1BB20401591FA";
		MessageBox msgBox = this.hallApiController.getBillInfoWS(filter_EQS_code);
		Assert.assertTrue(msgBox.getSuccess());
	}
	@Test
	public void testOutboundBackWS() throws Exception{
		iniData();
		String mainInfoin="{\"DeviceId\":\"KZ000000\",\"Remark\":\"\"}";
		String deviceIdin="KZ000000";
		String detailInfoin="[{\"Epc\":\"FFFFF8FFF0C9EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"Sku\":\"B1BB20401591FA\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"91白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":0,\"BackStatus\":0,\"TagPrice\":0.0,\"Batches\":\"91FA\",\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":0,\"FloorName\":\"\",\"BusinessOwnerId\":\"\",\"StatusInfo\":\"\"}]";
		String typein="3";
		MockHttpServletRequest request=(MockHttpServletRequest) WebMockUtil.getHttpServletRequest();
		request.addParameter("mainInfo",mainInfoin);
		MessageBox msgBoxIn=this.hallApiController.inboundWS(deviceIdin,detailInfoin,typein);
		request.close();

		String deviceId = "KZ000000";
		String detailInfo = "[{\"Epc\":\"FFFFF8FF8E49EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"Sku\":\"B1BB20401591FA\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"91白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":0,\"BackStatus\":0,\"BusinessTaskId\":\"AII170517001\",\"TagPrice\":0.0,\"Batches\":\"91FA\",\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":1,\"InDate\":\"2017-05-17 15:31:38\",\"Floor\":\"SE006005\",\"FloorName\":\"\",\"BusinessOwnerId\":\"HR003\",\"ownerId\":\"HR003\",\"StatusInfo\":\"在库\",\"BusinessDate\":\"2017-05-17 15:31:38\"}]";
		String unit2Id = "HR001";
		String typea = "6";
		String mainInfo = "{\"DeviceId\":\"KZ000000\",\"Remark\":\"\"}";
		MessageBox msgBox12 = this.hallApiController.outboundWS(deviceId, detailInfo, unit2Id, typea, mainInfo);

		String filter_EQS_code = "0000040003512";
		String filter_EQS_deviceId = "KZ000000";
		String type = "6";
		MessageBox msgBox = this.hallApiController.outboundBackWS(filter_EQS_code, filter_EQS_deviceId, type);
		this.hallTaskService.deleteWithInbound("0000040003512","KZ000000");
		Assert.assertTrue(msgBox.getSuccess());
	}
	@Test
	public void testListBadTagWS() throws Exception{
		iniData();
		String mainInfoin="{\"DeviceId\":\"KZ000000\",\"Remark\":\"\"}";
		String deviceIdin="KZ000000";
		String detailInfoin="[{\"Epc\":\"FFFFF8FFF0C9EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"Sku\":\"B1BB20401591FA\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"91白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":0,\"BackStatus\":0,\"TagPrice\":0.0,\"Batches\":\"91FA\",\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":0,\"FloorName\":\"\",\"BusinessOwnerId\":\"\",\"StatusInfo\":\"\"}]";
		String typein="3";
		MockHttpServletRequest request=(MockHttpServletRequest) WebMockUtil.getHttpServletRequest();
		request.addParameter("mainInfo",mainInfoin);
		MessageBox msgBoxIn=this.hallApiController.inboundWS(deviceIdin,detailInfoin,typein);
		request.close();

		String deviceId = "KZ000000";
		String detailInfo = "[{\"Epc\":\"FFFFF8FFF0C9EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"SkuCount\":0,\"BackStatus\":0,\"TagPrice\":0.0,\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":0,\"FloorName\":\"\",\"BusinessOwnerId\":\"\",\"StatusInfo\":\"\"}]";
		String unit2Id = "";
		String type = "5";
		String mainInfo = "{\"DeviceId\":\"KZ000000\",\"Remark\":\"\"}";
		MessageBox msgBox1 = this.hallApiController.outboundWS(deviceId, detailInfo, unit2Id, type, mainInfo);
		Sample sample =this.sampleService.getById("0000040003512");
		sample.setStatus(7);
		this.sampleService.save(sample);

		String filter_EQS_status = "";//为空即可
		MessageBox msgBox = this.hallApiController.listBadTagWS(filter_EQS_status, deviceId);
		this.hallTaskService.deleteWithInbound("0000040003512","KZ000000");
		Assert.assertTrue(msgBox.getSuccess());
	}
	@Test
	public void testSupplyTagInboundWS() throws Exception{
		iniData();
		String mainInfoin="{\"DeviceId\":\"KZ000000\",\"Remark\":\"\"}";
		String deviceIdin="KZ000000";
		String detailInfoin="[{\"Epc\":\"FFFFF8FFF0C9EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"Sku\":\"B1BB20401591FA\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"91白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":0,\"BackStatus\":0,\"TagPrice\":0.0,\"Batches\":\"91FA\",\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":0,\"FloorName\":\"\",\"BusinessOwnerId\":\"\",\"StatusInfo\":\"\"}]";
		String typein="3";
		MockHttpServletRequest request=(MockHttpServletRequest) WebMockUtil.getHttpServletRequest();
		request.addParameter("mainInfo",mainInfoin);
		MessageBox msgBoxIn=this.hallApiController.inboundWS(deviceIdin,detailInfoin,typein);
		request.close();

		String deviceId = "KZ000000";
		String detailInfo1 = "[{\"Epc\":\"FFFFF8FFF0C9EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"SkuCount\":0,\"BackStatus\":0,\"TagPrice\":0.0,\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":0,\"FloorName\":\"\",\"BusinessOwnerId\":\"\",\"StatusInfo\":\"\"}]";
		String unit2Id = "";
		String type1 = "5";
		String mainInfo1 = "{\"DeviceId\":\"KZ000000\",\"Remark\":\"\"}";
		MessageBox msgBox1 = this.hallApiController.outboundWS(deviceId, detailInfo1, unit2Id, type1, mainInfo1);
		Sample sample =this.sampleService.getById("0000040003512");
		sample.setStatus(7);
		this.sampleService.save(sample);

		String filter_EQS_status = "";//为空即可
		MessageBox msgBox2 = this.hallApiController.listBadTagWS(filter_EQS_status, deviceId);
		this.hallTaskService.deleteWithInbound("0000040003512","KZ000000");

		String detailInfo = "[{\"Epc\":\"FFFFF8FF9D69EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":0,\"BackStatus\":0,\"BusinessTaskId\":\"APO170517001\",\"TagPrice\":0.0,\"IsWrite\":true,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":7,\"InDate\":\"2017-05-17 17:49:50\",\"Floor\":\"SE006005\",\"FloorName\":\"一号库位\",\"BusinessOwnerId\":\"\",\"ownerId\":\"HR003\",\"StatusInfo\":\"未知\",\"UnitName\":\"深圳_弄影一仓\",\"BusinessDate\":\"2017-05-17 17:50:19\"}]";
		String type = "7";
		String mainInfo = "{\"DeviceId\":\"KZ000000\",\"Remark\":\"\"}";
		MessageBox msgBox = this.hallApiController.supplyTagInboundWS(deviceId, detailInfo, type, mainInfo);
		Assert.assertTrue(msgBox.getSuccess());
	}
	@Test
	public void testFindDtlListByCodeWS() throws Exception {
		iniData();
		String mainInfoin="{\"DeviceId\":\"KZ000000\",\"Remark\":\"\"}";
		String deviceIdin="KZ000000";
		String detailInfoin="[{\"Epc\":\"FFFFF8FFF0C9EFFFFFFFFFFF\",\"Code\":\"0000040003512\",\"Sku\":\"B1BB20401591FA\",\"StyleId\":\"B1BB204015\",\"ColorId\":\"91\",\"SizeId\":\"FA\",\"StyleName\":\"宽袖T恤\",\"ColorName\":\"91白色\",\"SizeName\":\"M 160/80A 均码\",\"SkuCount\":0,\"BackStatus\":0,\"TagPrice\":0.0,\"Batches\":\"91FA\",\"IsWrite\":false,\"IsScan\":false,\"Comment\":\"正常\",\"Status\":0,\"FloorName\":\"\",\"BusinessOwnerId\":\"\",\"StatusInfo\":\"\"}]";
		String typein="3";
		MockHttpServletRequest request1=(MockHttpServletRequest) WebMockUtil.getHttpServletRequest();
		request1.addParameter("mainInfo",mainInfoin);
		MessageBox msgBoxIn=this.hallApiController.inboundWS(deviceIdin,detailInfoin,typein);
		request1.close();

		String filter_EQS_code = "0000040003512";
		String type = "3";
		String filter_EQS_deviceId = "KZ000000";
		MockHttpServletRequest request = (MockHttpServletRequest) WebMockUtil.getHttpServletRequest();
		request.addParameter("filter_EQS_code", filter_EQS_code);
		request.addParameter("type", type);
		request.addParameter("filter_EQS_deviceId", filter_EQS_deviceId);
		MessageBox msgBox = this.hallApiController.findDtlListByCodeWS();
		if(msgBox.getSuccess())
			this.hallTaskService.deleteWithInbound("0000040003512","KZ000000");
		request.close();
		Assert.assertTrue(msgBox.getSuccess());
	}

}
