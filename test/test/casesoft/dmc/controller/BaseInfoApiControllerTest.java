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

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.file.FileUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.dto.RespMessage;
import com.casesoft.dmc.extend.api.web.hub.BaseInfoApiController;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.cfg.PropertyType;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.tag.EpcBindBarcode;

import test.casesoft.dmc.BaseTestCase;
import junit.framework.Assert;
import test.casesoft.dmc.BeanContext;

/*
 * 
 */
  
public class BaseInfoApiControllerTest extends AbstractTestCase {
	@Autowired
	 BaseInfoApiController baseInfoApiController;

//	public void setUp() throws Exception {
//		//super.setUp();
//		
//	}


	// input_value current、null
	@Test
	public void testlistUnitWS() {

		String deviceId = "JH2FWEARE";
		List<Unit> lstnit = this.baseInfoApiController.listUnitWS(deviceId);
		Assert.assertTrue(lstnit.size() > 0);

	}
	@Test
	public void testlistUnitWS_null() {
		try {
			String deviceId = null;
			List<Unit> lstnit = this.baseInfoApiController.listUnitWS(deviceId);
			Assert.assertTrue(lstnit.size() > 0);
		} catch (Exception e) {
			Assert.assertTrue(e instanceof IllegalArgumentException
					&& e.getMessage().contains("deviceId不能为空！"));
		}
	}

	// Unit u =
	// CacheManager.getUnitById(CacheManager.getDeviceByCode(deviceId).getStorageId());无法获取到值
	@Test
	public void testfindUnitByDeviceWS() throws Exception {
		String deviceId = "KC082401";
		Unit unit = this.baseInfoApiController.findUnitByDeviceWS(deviceId);
		Assert.assertTrue(unit != null);

	}
	@Test
	public void testfindUnitByDeviceWS_isNull() {
		try {
			String deviceId = null;
			this.baseInfoApiController.findUnitByDeviceWS(deviceId);

		} catch (Exception e) {
			Assert.assertTrue(e instanceof IllegalArgumentException
					&& e.getMessage().contains("deviceId不能为空"));
		}
	}
	@Test
	public void testLoginWS() {
		String userCode = "B001";
		String password = "123456";

		try {
			Unit unit = this.baseInfoApiController.login(userCode,password,null);
			Assert.assertNotNull(unit);
		} catch (Exception e) {

			Assert.assertTrue(false);
			e.printStackTrace();
		}
	}

	
}
