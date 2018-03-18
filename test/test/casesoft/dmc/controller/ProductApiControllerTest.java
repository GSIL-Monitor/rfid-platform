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
import com.casesoft.dmc.extend.api.web.hub.ProductApiController;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.cfg.PropertyType;
import com.casesoft.dmc.model.tag.EpcBindBarcode;

import junit.framework.Assert;
import test.casesoft.dmc.BaseTestCase;
import test.casesoft.dmc.BeanContext;

/**
 * Created by WingLi on 2017-01-04.
 * 
 */
/*failuremethods:
 * 	findEpcListnull() version为空
 * 	testboundEpcWS()  code字段无数据，barcode字段有数据
 */

public class ProductApiControllerTest extends AbstractTestCase {
    @Autowired
	ProductApiController productApiController;

//    public void setUp() throws Exception {
//        //super.setUp();
//        productApiController = (ProductApiController) BeanContext.getApplicationContext().getBean("productApiController");
//        
//      
//    }
    @Test
    public void testFindProductListByVersionWS_null_version() {
        //测试版本号为空
        RespMessage respMessage = this.productApiController.findProductListByVersionWS(null);
        Assert.assertTrue(respMessage.getSuccess()==false);
    }
    @Test
    public void testFindProductListByVersionWS_1_version() {

        //测试版本号为1
        long version = 1;
        RespMessage respMessage = this.productApiController.findProductListByVersionWS(version);
        Assert.assertTrue(respMessage.getSuccess() && respMessage.getResult()!=null);

    }
    @Test
    public void testFindProductListByVersionWS_max_version() {
        //测试版本号最大时
        long version = 100000;
        RespMessage respMessage = this.productApiController.findProductListByVersionWS(version);
        Assert.assertTrue(respMessage.getSuccess());
    }
    
   
    
    // TAG_EPCBARCODE表中有BARCODE数据无CODE数据
    @Test
    public void testboundEpcWS_null_barcode(){
    	try{
    	String boundInfo = "[{'epc':'','code':''}]";
    	MessageBox msg=this.productApiController.boundEpcWS(boundInfo);
    	Assert.assertFalse(msg.getSuccess());
    	}
    	catch(Exception e){
    		//logger.error(e.getMessage());
    	}
    }

//    public void testboundEpcWS_have_barcode() throws Exception{
//
//    	String boundInfo = "[{'epc':'FE280DFFFF9FDFFFFFFFFFFF','code':'P2TT30601035F0'}]";
//    	MessageBox msg=this.productApiController.boundEpcWS(boundInfo);
//    	Assert.assertTrue(msg.getSuccess());
//    	String unboundEpcJson = "['FE280DFFFF9FDFFFFFFFFFFF']";
//    	msg=this.productApiController.unboundEpcWS(unboundEpcJson);
//    	Assert.assertTrue(msg.getSuccess());
//
//
//    }
    
    

    /*
     * maxversion=0
     */
    @Test
    public void testfindEpcList()
    {
    	
    	// version 为空  结果->语句未执行：List<EpcBindBarcode> epcBindBarcodeList = this.bindService.getAll();
    	List<EpcBindBarcode> epcBindBarcodeList= this.productApiController.findEpcList(null);
    	Assert.assertTrue(epcBindBarcodeList.size()>0);
    }

    @Test
    public void testgetProductVersionWS(){
    	RespMessage rmsg = this.productApiController.getProductVersionWS();
    	Assert.assertTrue(rmsg.getSuccess());
    }
    @Test
    public void testgetBindVersionWS(){
    	
    	RespMessage rmsg=this.productApiController.getBindVersionWS();
    	Assert.assertTrue(rmsg.getSuccess());
    }
    @Test
    public void testupdatePrintStatus(){
    	String billNo ="TIB160819005";
    	String status="1";
    	RespMessage rmsg =this.productApiController.updatePrintStatus(billNo, status);
    	Assert.assertTrue(rmsg.getSuccess());
    }
    @Test
    public void testupdatePrintStatus_billNBo_null()
    {
    	try{
    	String billNo =null;
    	String status ="1";
    	RespMessage rmsg=this.productApiController.updatePrintStatus(billNo, status);
    	Assert.assertTrue(rmsg.getSuccess());
    	}
    	catch(Exception e){
    		Assert.assertTrue(e instanceof IllegalArgumentException && e.getMessage().contains("billNo不能为空"));
    		
    	}
    	
    }
    @Test
    public void testupdatePrintStatus_status_null(){
    	try{
    		String billNo ="TIB160819005";
        	String status=null;
        	RespMessage rmsg =this.productApiController.updatePrintStatus(billNo, status);
        	Assert.assertTrue(rmsg.getSuccess());
    	}catch(Exception e){
    		Assert.assertTrue(e instanceof IllegalArgumentException && e.getMessage().contains("status不能为空"));
    	}
    }
    @Test
    public void testlistPropertyKeyWS(){
    	
    	List<PropertyKey> lstPK =this.productApiController.listPropertyKeyWS();
    	Assert.assertTrue(lstPK.size()>0);
    }
    @Test
    public void testlistPropertyNameWS(){
    	List<PropertyType> lstp =this.productApiController.listPropertyNameWS();
    	Assert.assertTrue(lstp.size()>0);
    }
    
}
