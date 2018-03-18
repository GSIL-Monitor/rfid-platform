package test.casesoft.dmc.controller;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.casesoft.dmc.BaseTestCase;
import test.casesoft.dmc.BeanContext;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.file.FileUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.extend.api.web.hub.BaseInfoApiController;

@RunWith(Suite.class)
@SuiteClasses({ BaseInfoApiControllerTest.class, BillApiControllerTest.class,
		MirrorApiControllerTest.class, ProductApiControllerTest.class,
		SaleApiControllerTest.class, TaskApiControllerTest.class, HallApiControllerTest.class})
public class AllTests {
	
	public  void setUpBeforeClass() throws Exception {
		BeanContext.initApplicationContext();
	    CacheManager.initCache();
	    PropertyUtil.init();
	    FileUtil.init();// 创建不存在的目录
	    
	}
	

}
