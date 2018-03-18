package test.casesoft.dmc;



import junit.framework.Assert;
import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.file.FileUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;


public class BaseTestCase extends TestCase {

  public Logger logger = LoggerFactory.getLogger(this.getClass());


  protected void setUp() throws Exception {
    BeanContext.initApplicationContext();

    CacheManager.initCache();

    PropertyUtil.init();

    FileUtil.init();// 创建不存在的目录
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }

  protected void logger(String message) {
    System.out.println(message);
    logger.debug(message);
  }

  protected void returnFailur(String message) {
    System.out.println("测试失败：" + message);
    logger.debug("测试失败：" + message);
    Assert.assertTrue(false);

  }

  protected void returnSuccess(String message) {
    System.out.println("测试成功：" + message);
    logger.debug("测试成功：" + message);
    Assert.assertTrue(true);
  }
  
  public String getJSONFromFile(String fileName) throws Exception {
	  if(! fileName.toLowerCase().endsWith("json"))
		  throw new Exception("文件不是JSON格式");
	  String json = FileUtil.readFileToStr(
			  fileName).toString();
	  return json;
  }

}
