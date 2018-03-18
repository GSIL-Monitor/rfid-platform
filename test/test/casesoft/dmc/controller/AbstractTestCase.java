package test.casesoft.dmc.controller;
import junit.framework.Assert;

import org.junit.runner.RunWith;  
  
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;  
import org.springframework.transaction.annotation.Transactional;  

import com.casesoft.dmc.core.util.file.FileUtil;
@ContextConfiguration(locations="classpath:test-applicationContext.xml")  
@RunWith(SpringJUnit4ClassRunner.class)  
@Transactional  
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)   
public abstract class AbstractTestCase {    
       
	

	 
}  
