package test.casesoft.dmc.controller;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;

import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.dto.RespMessage;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.cfg.PropertyType;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.tag.EpcBindBarcode;
import com.casesoft.dmc.extend.api.vo.BillVO;
import com.casesoft.dmc.extend.api.web.hub.BillApiController;

import junit.framework.Assert;
import test.casesoft.dmc.BaseTestCase;
import test.casesoft.dmc.BeanContext;


/*2017/1/12
 * 已测
 * 未过方法：listBillsByDevice(),testfindERPBillDtlWS()
 */
  
public class BillApiControllerTest extends AbstractTestCase{
	@Autowired
	BillApiController billApiController;
	
//	public void setUp() throws Exception{
//		//super.setUp();
//		
//			
//	}
	
	@Test
	public void testlistBillsByDevice_lessthan5() throws Exception{
			String deviceId="KC201601";
			String type="5";
			String beginDate="03-11月-16";
			String endDate="05-11月-16";
			String styleId="P2GG604003";
			String unitId="ZOL001";

			List<BillVO> lstbill = this.billApiController.listBillsByDevice(deviceId,type,beginDate,endDate,styleId,unitId,"0");
			Assert.assertTrue(lstbill.isEmpty());
				
		
	}
	@Test
		public void testlistBillsByDevice_largerthan5() throws Exception{
			String deviceId="KC201601";
			String type="9";
			String beginDate="03-11月-16";
			String endDate="05-11月-16";
			String styleId="P2GG604003";
			String unitId="ZOL001";
			List<BillVO> lstbll;			
			lstbll = this.billApiController.listBillsByDevice(deviceId,type,beginDate,endDate,styleId,unitId,"0");
			Assert.assertTrue(lstbll.size()>0);
		}
		
		 //返回值为null 方法中的billDtlList元素未获取到值 ERROR!!!
	@Test
		public void testfindERPBillDtlWS(){
			
			
			String billId="00000000330";
			String type="27";
			List<BillDtl> lstbill =this.billApiController.findERPBillDtlWS(billId, type);
			Assert.assertTrue(lstbill.size()>0);
		}
	@Test
		public void testfindERPBillDtlWS_billId_null(){
			try{
				String billId=null;
				String type="27";
				List<BillDtl> lstbill =this.billApiController.findERPBillDtlWS(billId, type);
				Assert.assertTrue(lstbill.size()>0);
			}catch(Exception e){
				Assert.assertTrue(e instanceof IllegalArgumentException && e.getMessage().contains("id不能为空"));
			}
			
		}
	@Test
		public void testfindERPBillDtlWS_type_null(){
			try{
				String billId="00000000330";
				String type=null;
				List<BillDtl> lstbill =this.billApiController.findERPBillDtlWS(billId, type);
				Assert.assertTrue(lstbill.size()>0);
			}catch (Exception e){
				Assert.assertTrue(e instanceof IllegalArgumentException && e.getMessage().contains("type不能为空"));
			}
			
			
		}

}
