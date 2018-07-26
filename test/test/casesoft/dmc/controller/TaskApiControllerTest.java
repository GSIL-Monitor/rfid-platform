package test.casesoft.dmc.controller;

import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.service.syn.BillService;

import test.casesoft.dmc.BaseTestCase;

import java.io.File;
import java.util.List;

import org.codehaus.groovy.tools.shell.util.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;

import com.casesoft.dmc.controller.syn.TaskController;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.dto.RespMessage;
import com.casesoft.dmc.extend.api.web.hub.TaskApiController;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.cfg.PropertyType;
import com.casesoft.dmc.model.tag.EpcBindBarcode;
import com.casesoft.dmc.model.task.BusinessDtl;
import com.casesoft.dmc.model.task.Record;

import junit.framework.Assert;
import test.casesoft.dmc.BaseTestCase;
import test.casesoft.dmc.BeanContext;

public class TaskApiControllerTest extends AbstractTestCase{
	@Autowired		
	TaskApiController taskApiController;
	@Autowired			
	TaskController taskController;
	@Autowired	
	BillService billService;
			
//	public void setUp()throws Exception
//	{
//		//super.setUp();
//		taskApiController =(TaskApiController)BeanContext.getApplicationContext().getBean("taskApiController");
//		taskController = (TaskController)BeanContext.getApplicationContext().getBean("taskController");
//		billService = (BillService) BeanContext.getApplicationContext().getBean("billService");
//	}
	
	
	//解析文件出错
//	public void testuploadTask()throws Exception{
//		
//		String path = TaskApiControllerTest.class.getResource("").getPath();
//        File file = new File(path + "\\TSK20160705KB0000010027.zip");
//        String fileFileName = file.getName();
//        MessageBox msg;
//
//	    	msg =this.taskApiController.uploadTask(file, fileFileName);
//	    
//	    	Assert.assertTrue(msg.getSuccess());
//	   
//	    	List<BusinessDtl> dtlList = this.taskController.findDetail("TSK20160705KB0000010027");
//	    	Assert.assertTrue(dtlList.size()>0);
//	    
//	    msg=this.taskController.deleteByTaskId("TSK20160705KB0000010027");
//	    Assert.assertTrue(msg.getSuccess());
//
//	}

	/**
	 * 采购入库测试
	 * @throws Exception
	 */
	@Test
	public void testuploadTask_8()throws Exception{
		String path = TaskApiControllerTest.class.getResource("").getPath();
		File file = new File(path + "\\KE2017011523251945177.zip");
		String fileFileName = file.getName();

		MessageBox msg =this.taskApiController.uploadTask(file, fileFileName);
		Assert.assertTrue(msg.getSuccess());
		/*Business bus = (Business) msg.getResult();
		//ZCBJ001 F001
		Assert.assertEquals(bus.getOrigUnitId(),"F001");
		Assert.assertEquals(bus.getDestId(),"ZCBJ001");
		Assert.assertNotNull(bus.getDestUnitId(),"1");
		String taskId = "TSK20170228KB2017010002";


		List<BusinessDtl> dtlList = taskController.findDetail(taskId);
		Assert.assertTrue(dtlList.size()>0);
		Assert.assertEquals(dtlList.get(0).getDestId(),"ZCBJ001");*/
		String taskId = "KE2017011523251945177";
		msg = this.taskController.deleteByTaskId(taskId);
		Assert.assertTrue(msg.getSuccess());

	}

	/**
	 * 仓库出库给门店测试
	 */
	@Test	
	public void testuploadTask_10(){
		String path = TaskApiControllerTest.class.getResource("/").getPath();
        File file = new File(path + "\\TSK20160714KB0000010004.zip");
        String fileFileName = file.getName();
        MessageBox msg;
        try{
	    msg =this.taskApiController.uploadTask(file, fileFileName);
	    Assert.assertTrue(msg.getSuccess());
	
	    String taskId = "TSK20160714KB0000010004";
	    List<BusinessDtl> dtlList = this.taskController.findDetail(taskId);
	    Assert.assertTrue(dtlList.size()>0);
	    Assert.assertEquals(dtlList.get(0).getDestId(),"DGS_TSLHY");
	    Assert.assertEquals(dtlList.get(0).getOrigId(),"IDFIX11");
	    
	    msg = this.taskController.deleteByTaskId(taskId);
	    Assert.assertTrue(msg.getSuccess());
	    }catch(Exception e){
	    	//logger.error(e.getMessage());
	    }
	}

	/**
	 * 仓库退货给供应商(采购退货)测试
	 * @throws Exception
	 */
	@Test
	public void testuploadTask_26()throws Exception{
		String path = TaskApiControllerTest.class.getResource("").getPath();
		File file = new File(path + "\\TSK20170308KB2017010005.zip");
		String fileFileName = file.getName();

		MessageBox msg =this.taskApiController.uploadTask(file, fileFileName);
		Assert.assertTrue(msg.getSuccess());
		Business bus = (Business) msg.getResult();
		Assert.assertEquals(bus.getToken().intValue(),26);
		//ZCBJ001 F001
		Assert.assertEquals(bus.getOrigId(),"IDFIX11");
		Assert.assertNotNull(bus.getDestUnitId(),"13HA09");
		String taskId = "TSK20170308KB2017010005";


		List<BusinessDtl> dtlList = taskController.findDetail(taskId);
		Assert.assertTrue(dtlList.size()>0);
		Assert.assertEquals(dtlList.get(0).getOrigId(),"IDFIX11");

		msg = this.taskController.deleteByTaskId(taskId);
		Assert.assertTrue(msg.getSuccess());

	}
	@Test
	public void testuploadTask0(){
		File file=null;
		String fileName=null;
		try{
		MessageBox msg=this.taskApiController.uploadTask(file, fileName);
		}
		catch(Exception e)
		{
			Assert.assertTrue(e instanceof IllegalArgumentException && e.getMessage().contains("file不可为空"));
		}
	}


	/**
	 * 测试仓库盘点
	 * @throws Exception
	 */
	@Test
	public void testuploadTask_9_bill()throws Exception{
		String path = TaskApiControllerTest.class.getResource("").getPath();
		File file = new File(path + "\\TSK20170228KB2017010004.zip");
		String fileFileName = file.getName();

		MessageBox msg =this.taskApiController.uploadTask(file, fileFileName);
		Assert.assertTrue(msg.getSuccess());
		Business bus = (Business) msg.getResult();
		Assert.assertEquals(bus.getToken().intValue(),9);
		Assert.assertEquals(bus.getOrigId(),"IDFIX11");//ZCBJ001
		Assert.assertEquals(bus.getDestId(),"IDFIX11");
		Assert.assertNotNull(bus.getDestUnitId());
		Assert.assertNotNull(bus.getBill());
		String taskId = "TSK20170228KB2017010004";
		List<BusinessDtl> dtlList = taskController.findDetail(taskId);
		Assert.assertTrue(dtlList.size()>0);
		Assert.assertEquals(dtlList.get(0).getDestId(),"IDFIX11");
		Assert.assertEquals(dtlList.get(0).getOrigId(),"IDFIX11");

		msg = this.taskController.deleteByTaskId(taskId);
		Assert.assertTrue(msg.getSuccess());
		this.billService.delete(bus.getBillId());

	}
	/**
	 * 仓库调拨出测试 W002  IDFIX11
	 * @throws Exception
	 */
	@Test
	public void testuploadTask_24()throws Exception{
		String path = TaskApiControllerTest.class.getResource("").getPath();
		File file = new File(path + "\\TSK20170308KB2017010004.zip");
		String fileFileName = file.getName();

		MessageBox msg =this.taskApiController.uploadTask(file, fileFileName);
		Assert.assertTrue(msg.getSuccess());
		Business bus = (Business) msg.getResult();
		Assert.assertEquals(bus.getToken().intValue(),24);
		//ZCBJ001 F001
		Assert.assertEquals(bus.getOrigId(),"IDFIX11");
		Assert.assertEquals(bus.getDestId(),"W002");
		String taskId = "TSK20170308KB2017010004";


		List<BusinessDtl> dtlList = taskController.findDetail(taskId);
		Assert.assertTrue(dtlList.size()>0);
		Assert.assertEquals(dtlList.get(0).getOrigId(),"IDFIX11");

		msg = this.taskController.deleteByTaskId(taskId);
		Assert.assertTrue(msg.getSuccess());

	}


	/**************************************************************************************/

	/**
	 * 测试门店入库（按单入库）  CCBJ001 IDFIX11
	 * @throws Exception
	 */
	@Test
	public void testuploadTask_14_bill()throws Exception{
		String path = TaskApiControllerTest.class.getResource("").getPath();
		File file = new File(path + "\\TSK20170308KC0100080001.zip");
		String fileFileName = file.getName();

		MessageBox msg =this.taskApiController.uploadTask(file, fileFileName);
		Assert.assertTrue(msg.getSuccess());
		Business bus = (Business) msg.getResult();
		Assert.assertEquals(bus.getToken().intValue(),14);
		Assert.assertEquals(bus.getOrigId(),"IDFIX11");//ZCBJ001
		Assert.assertEquals(bus.getDestId(),"CCBJ001");
		Assert.assertNotNull(bus.getDestUnitId());
		Assert.assertNotNull(bus.getBill());
		String taskId = "TSK20170308KC0100080001";
		List<BusinessDtl> dtlList = taskController.findDetail(taskId);
		Assert.assertTrue(dtlList.size()>0);
		Assert.assertEquals(dtlList.get(0).getDestId(),"CCBJ001");
		Assert.assertEquals(dtlList.get(0).getOrigId(),"IDFIX11");

		msg = this.taskController.deleteByTaskId(taskId);
		Assert.assertTrue(msg.getSuccess());
		this.billService.delete(bus.getBillId());

	}

	/**
	 * 测试门店退仓 CCBJ001 IDFIX11
	 * @throws Exception
	 */
	@Test
	public void testuploadTask_27()throws Exception{
		String path = TaskApiControllerTest.class.getResource("").getPath();
		File file = new File(path + "\\TSK20170308KC0100080005.zip");
		String fileFileName = file.getName();

		MessageBox msg =this.taskApiController.uploadTask(file, fileFileName);
		Assert.assertTrue(msg.getSuccess());
		Business bus = (Business) msg.getResult();
		Assert.assertEquals(bus.getToken().intValue(),27);
		//ZCBJ001 F001
		Assert.assertEquals(bus.getOrigId(),"CCBJ001");
		Assert.assertEquals(bus.getDestId(),"IDFIX11");
		String taskId = "TSK20170308KC0100080005";


		List<BusinessDtl> dtlList = taskController.findDetail(taskId);
		Assert.assertTrue(dtlList.size()>0);
		Assert.assertEquals(dtlList.get(0).getOrigId(),"CCBJ001");

		msg = this.taskController.deleteByTaskId(taskId);
		Assert.assertTrue(msg.getSuccess());

	}

	/**
	 * 测试门店盘点（按库存单盘点）
	 * @throws Exception
	 */
	@Test
	public void testuploadTask_16_bill()throws Exception{
		String path = TaskApiControllerTest.class.getResource("").getPath();
		File file = new File(path + "\\TSK20170308KC0100080002.zip");
		String fileFileName = file.getName();

		MessageBox msg =this.taskApiController.uploadTask(file, fileFileName);
		Assert.assertTrue(msg.getSuccess());
		Business bus = (Business) msg.getResult();
		Assert.assertEquals(bus.getToken().intValue(),16);
		Assert.assertEquals(bus.getOrigId(),"CCBJ001");//ZCBJ001
		Assert.assertEquals(bus.getDestId(),"CCBJ001");
		Assert.assertNotNull(bus.getDestUnitId());
		Assert.assertNotNull(bus.getBill());
		String taskId = "TSK20170308KC0100080002";
		List<BusinessDtl> dtlList = taskController.findDetail(taskId);
		Assert.assertTrue(dtlList.size()>0);
		Assert.assertEquals(dtlList.get(0).getDestId(),"CCBJ001");
		Assert.assertEquals(dtlList.get(0).getOrigId(),"CCBJ001");


		msg = this.taskController.deleteByTaskId(taskId);
		Assert.assertTrue(msg.getSuccess());
		this.billService.delete(bus.getBillId());

	}

	/**
	 * 测试门店调拨出 CCBJ001 CCBJ002
	 * @throws Exception
	 */
	@Test
	public void testuploadTask_18()throws Exception{
		String path = TaskApiControllerTest.class.getResource("").getPath();
		File file = new File(path + "\\TSK20170308KC0100080006.zip");
		String fileFileName = file.getName();

		MessageBox msg =this.taskApiController.uploadTask(file, fileFileName);
		Assert.assertTrue(msg.getSuccess());
		Business bus = (Business) msg.getResult();
		Assert.assertEquals(bus.getToken().intValue(),18);
		//ZCBJ001 F001
		Assert.assertEquals(bus.getOrigId(),"CCBJ001");
		Assert.assertEquals(bus.getDestId(),"CCBJ002");
		String taskId = "TSK20170308KC0100080006";


		List<BusinessDtl> dtlList = taskController.findDetail(taskId);
		Assert.assertTrue(dtlList.size()>0);
		Assert.assertEquals(dtlList.get(0).getOrigId(),"CCBJ001");
		Assert.assertEquals(dtlList.get(0).getType().intValue(),0);

		msg = this.taskController.deleteByTaskId(taskId);
		Assert.assertTrue(msg.getSuccess());

	}
	@Test
	public void testcheckEpcStockWS()throws Exception{
		MessageBox msg=this.taskApiController.checkEpcStockWS();
		Assert.assertTrue(msg.getSuccess());
		
	}
	@Test
	public void testlistRecordByTaskIdWS(){
		String filter_EQS_taskId="735571045651460";
		String billType="1";
		List<Record> recordList=this.taskApiController.testListRecordByTaskIdWS(filter_EQS_taskId, billType);
		
		Assert.assertTrue(recordList.isEmpty());
		
	}
	@Test
	public void testlistRecordByTaskIdWS_null(){
		String filter_EQS_taskId=null;
		String billType=null;
		try{
			List<Record> recordList=this.taskApiController.testListRecordByTaskIdWS(filter_EQS_taskId, billType);
			Assert.assertTrue(recordList==null);
		}catch(Exception e){
			Assert.assertTrue(e instanceof IllegalArgumentException && e.getMessage().contains("filter_EQS_taskId不能为空"));
		}
	}
}
