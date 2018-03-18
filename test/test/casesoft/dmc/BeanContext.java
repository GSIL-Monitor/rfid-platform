package test.casesoft.dmc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanContext {

  private final static Log log = LogFactory.getLog(BeanContext.class);

  private static ApplicationContext applicationContext;

  public static void initApplicationContext() {
    if (null == applicationContext) {
      applicationContext = new ClassPathXmlApplicationContext("test-applicationContext.xml");
      log.info("Loading test-applicationContext-*.xml...");
      // initParam();
    } else
      ((ClassPathXmlApplicationContext) applicationContext).refresh();
  }

  public static void close() {
    if (null != applicationContext) {
      ((ClassPathXmlApplicationContext) applicationContext).close();
    }
    log.info("Close Spring container...");
  }

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

}
