package test.casesoft.dmc.controller;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.shop.SaleBillController;
import com.casesoft.dmc.core.util.file.FileUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.dto.RespMessage;
import com.casesoft.dmc.extend.api.web.hub.SaleApiController;
import com.casesoft.dmc.model.shop.SaleBillDtl;

import junit.framework.Assert;
import test.casesoft.dmc.BaseTestCase;
import test.casesoft.dmc.BeanContext;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;



public class SaleApiControllerTest  extends AbstractTestCase{
	@Autowired
	SaleApiController saleApiController;
	@Autowired
	SaleBillController saleBillController;

//	public void setUp() throws Exception {
//		//super.setUp();
//		saleBillController = (SaleBillController) BeanContext
//				.getApplicationContext().getBean("saleBillController");
//		saleApiController = (SaleApiController) BeanContext
//				.getApplicationContext().getBean("saleApiController");
//	}
	@Test
	public void testlistCustomerWS() {
		RespMessage rmsg = this.saleApiController.listCustomerWS();
		Assert.assertTrue(rmsg.getSuccess());

	}
	@Test
	public void testlistCashierWS() {
		String shopId = "OLS001";
		RespMessage rmsg = this.saleApiController.findCashierWS("4", shopId);
		Assert.assertTrue(rmsg.getSuccess());
	}
	@Test
	public void testlistCashierWS_showId_nulls() {
		try {
			String shopId = "OLS001";
			RespMessage rmsg = this.saleApiController.findCashierWS("4",
					shopId);
			Assert.assertTrue(rmsg.getSuccess());
		} catch (Exception e) {
			Assert.assertTrue(e instanceof IllegalArgumentException
					&& e.getMessage().contains("filter_EQI_isAdmin不能为空"));
		}
	}

	//region 测试销售提交
	@Test
	public void testPosDataUploadWS_NULL_DeviceId() throws Exception {
		String order = "{\"Id\":\"EPB160715112560\",\"OwnerId\":\"1\",\"BillNo\":\"EPB160715112560\",\"BillDate\":\"2016-07-15 11:25:59\",\"Type\":0,\"ClientId\":\"saler001\",\"ClientName\":\"saler1??穃\",\"Client2Id\":null,\"Remark\":\"\",\"TotOrderQty\":1,\"TotOrderValue\":399.0,\"TotActValue\":399.0,\"TotOrderTax\":0.0,\"SrcBillId\":\"\",\"ShopId\":\"DGS_TSLHY\",\"GradeRate\":0,\"MileageCode\":null,\"IncreaseGrate\":0.0,\"ActCardValue\":0.0,\"ActCashValue\":399.0,\"ActGradeValue\":0.0,\"ActVoucherValue\":0.0,\"PayForCash\":400.0,\"BackForCash\":1.0,\"ToZero\":0.0,\"Balance\":0.0}";// SaleBill
																																																																																																																																																				// json
		String orderDtls = "[{\"Id\":\"EPB160715112560\",\"UniqueCode\":\"0000140000407\",\"Code\":\"HMO018WS70\",\"BillId\":\"EPB160715112560\",\"BillNo\":\"EPB160715112560\",\"StyleId\":\"HMO01\",\"ColorId\":\"8W\",\"SizeId\":\"S70\",\"Remark\":null,\"Price\":399.0,\"ActPrice\":399.0,\"Percent\":0.0,\"Client2Id\":\"\",\"Grade\":null,\"RefundPrice\":0.0,\"RefundBillId\":null,\"GradeRate\":0,\"IncreaseGrate\":0.0,\"StyleRemark\":null,\"BrandCode\":null,\"Season\":null,\"Sex\":null,\"BigCategory\":null,\"SmallCategory\":null,\"Material\":null,\"StyleEnglishName\":null}]";// List<SaleBillDtl>
																																																																																																																																																	// json
		String recordList = "[{\"DeviceId\":\"KC010008\",\"Code\":\"0000140000407\",\"Sku\":\"HMO018WS70\",\"StyleId\":\"HMO01\",\"ColorId\":\"8W\",\"SizeId\":\"S70\"}]";// List<Record>
																																											// json
		String deviceId = "";
		MessageBox rmsg = this.saleApiController.posDataUploadWS(order,
				orderDtls, recordList, deviceId);
		Assert.assertTrue(!rmsg.getSuccess()
				&& rmsg.getMsg().contains("设备号未注册"));
	}
	@Test
	public void testPosDataUploadWS_UNIQUE_CODE_NOT_SALES() throws Exception {
		String order = "{\"Id\":\"EPB160715112560\",\"OwnerId\":\"1\",\"BillNo\":\"EPB160715112560\",\"BillDate\":\"2016-07-15 11:25:59\",\"Type\":0,\"ClientId\":\"saler001\",\"ClientName\":\"saler1??穃\",\"Client2Id\":null,\"Remark\":\"\",\"TotOrderQty\":1,\"TotOrderValue\":399.0,\"TotActValue\":399.0,\"TotOrderTax\":0.0,\"SrcBillId\":\"\",\"ShopId\":\"DGS_TSLHY\",\"GradeRate\":0,\"MileageCode\":null,\"IncreaseGrate\":0.0,\"ActCardValue\":0.0,\"ActCashValue\":399.0,\"ActGradeValue\":0.0,\"ActVoucherValue\":0.0,\"PayForCash\":400.0,\"BackForCash\":1.0,\"ToZero\":0.0,\"Balance\":0.0}";// SaleBill
																																																																																																																																																				// json
		String orderDtls = "[{\"Id\":\"EPB160715112560\",\"UniqueCode\":\"0000140001407\",\"Code\":\"HMO018WS70\",\"BillId\":\"EPB160715112560\",\"BillNo\":\"EPB160715112560\",\"StyleId\":\"HMO01\",\"ColorId\":\"8W\",\"SizeId\":\"S70\",\"Remark\":null,\"Price\":399.0,\"ActPrice\":399.0,\"Percent\":0.0,\"Client2Id\":\"\",\"Grade\":null,\"RefundPrice\":0.0,\"RefundBillId\":null,\"GradeRate\":0,\"IncreaseGrate\":0.0,\"StyleRemark\":null,\"BrandCode\":null,\"Season\":null,\"Sex\":null,\"BigCategory\":null,\"SmallCategory\":null,\"Material\":null,\"StyleEnglishName\":null}]";// List<SaleBillDtl>
																																																																																																																																																	// json
		String recordList = "[{\"DeviceId\":\"KC010008\",\"Code\":\"0000140001407\",\"Sku\":\"HMO018WS70\",\"StyleId\":\"HMO01\",\"ColorId\":\"8W\",\"SizeId\":\"S70\"}]";// List<Record>
																																											// json
		String deviceId = "KC010008";

		MessageBox rmsg = this.saleApiController.posDataUploadWS(order,
				orderDtls, recordList, deviceId);
		Assert.assertTrue(!rmsg.getSuccess()
				&& rmsg.getMsg().contains("不能重复销售"));
	}
	@Test
	public void testPosDataUploadWS() throws Exception {
        String order = "{\"actCardValue\":0,\"actCashValue\":20,\"actVoucherValue\":0,\"alipay\":0,\"backF" +
				"orCash\":0,\"billDate\":\"2017-03-22 17:24:42\",\"billNo\":\"KE2016011490174682474\",\"can" +
				"UsePromotion\":false,\"client2Id\":\"\",\"client2Name\":\"\",\"clientId\":\"B001\",\"clientNam" +
				"e\":\"B001\",\"gradeRate\":0,\"gradeValue\":0,\"id\":\"KE2016011490174682474\",\"isHistory\":" +
				"true,\"isRfid\":0,\"isSelected\":false,\"isUpload\":false,\"ownerId\":\"HDWL\",\"payForCash" +
				"\":20,\"payWay\":1,\"promotionId\":\"\",\"promotionNo\":\"\",\"shopId\":\"HDWL\",\"toZero\":0,\"to" +
				"tActValue\":20,\"totOrderQty\":1,\"totOrderValue\":20,\"type\":0}";
        String deviceId = "KE201601";
        String orderDtls = "[{\"actPrice\":20,\"barcode\":\"B000001S10C0400016\",\"billId\":\"KE2016011490" +
				"174682474\",\"billNo\":\"KE2016011490174682474\",\"canUsePromotion\":true,\"code\":\"B0000" +
				"01S10C04\",\"colorId\":\"S10\",\"epc\":\"000048000161200000000000\",\"id\":\"KE2016011490174" +
				"682474000048000161200000000000\",\"isSelected\":false,\"percent\":100,\"price\":20,\"pro" +
				"duct\":{\"barcode\":\"B000001S10C0400016\",\"class1\":\"\",\"class10\":\"\",\"class2\":\"\",\"clas" +
				"s3\":\"\",\"class4\":\"\",\"class5\":\"\",\"class6\":\"\",\"class7\":\"\",\"class8\":\"\",\"class9\":\"\",\"" +
				"code\":\"B000001S10C04\",\"colorId\":\"S10\",\"colorName\":\"灰色\",\"id\":\"000048\",\"image\":\"" +
				"/images/sku/B000001S10C04.jpg\",\"price\":20,\"remark\":\"\",\"sizeId\":\"C04\",\"sizeName\":" +
				"\"XL\",\"stockQty\":0,\"styleId\":\"B000001\",\"styleName\":\"女式秋季针织外套\",\"styleSortN" +
				"ame\":\"\"},\"sizeId\":\"C04\",\"styleId\":\"B000001\",\"uniqueCode\":\"0000480001612\"}]";
		String 	recordList =	"[{\"code\":\"0000480001612\",\"colorId\":\"S10\",\"deviceId\":\"KE201601\",\"size" +
				"Id\":\"C04\",\"sku\":\"B000001S10C04\",\"styleId\":\"B000001\"}]";

		MessageBox rmsg = this.saleApiController.posDataUploadWS(order,
				orderDtls, recordList, deviceId);
        Assert.assertTrue(rmsg.getSuccess());
        this.saleBillController.delete("KE2016011490174682474");
	}
	//endregion

	// ----------------------testgetTrfundBillWS---------------------
	// 不能重复退货
	@Test
	public void testgetRefundBillWS_have() throws Exception {
		String uniqueCode = "F9F428FFFF6FDFFFFFFFFFFF";
		MessageBox msg;
		msg = this.saleApiController.getRefundBillWS(uniqueCode,"");
		Assert.assertTrue(msg.getSuccess());

	}

	// 空值
	@Test
	public void testgetRefundBillWS_nullpoint() {
		String uniqueCode = null;
		try {
			MessageBox msg = this.saleApiController.getRefundBillWS(uniqueCode,"");
			Assert.assertFalse(msg.getSuccess());
		} catch (Exception e) {
			Assert.assertTrue(e instanceof IllegalArgumentException
					&& e.getMessage().contains("uniqueCode 不能为空！"));
		}

	}

	// 错误值
	@Test
	public void testgetRefundBillWS_falsepoint() throws Exception {
		String uniqueCode = "123456789";
		MessageBox msg;

		msg = this.saleApiController.getRefundBillWS(uniqueCode,"");
		Assert.assertFalse(msg.getSuccess());

	}

	// return 6 未测
	@Test
	public void testgetSaleBillByNoWS() throws Exception {

		String billNo = "160903CCBJ0012-0081";
		String uniqueCode = "F9F428FFFF6FDFFFFFFFFFFF";
		MessageBox msg = this.saleApiController.getSaleBillByNoWS(billNo,
				uniqueCode);
		Assert.assertTrue(msg.getSuccess());

	}

}
