package com.casesoft.dmc.core.listener;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.syn.QuartzJobController;
import com.casesoft.dmc.core.util.file.FileUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  QuartzJobController quartzJobController;
  @Override
  public void onApplicationEvent(ContextRefreshedEvent arg0) {
    logger.debug("Spring容器加载完毕...");
    // String propertyFile = this.getServletContext()
    // .getRealPath("/WEB-INF/classes/config.properties");
    // String propertyFile = getClass().getClassLoader().getResource("").getFile() + File.separator
    // + "config.properties";
    // PropertyUtil.init(propertyFile);// 初始化国际化配置文件
    PropertyUtil.init();
    try {
		CacheManager.initCache();
		this.quartzJobController.runJobs();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}// 初始化缓存

    FileUtil.init();// 创建不存在的目录

  }

}
