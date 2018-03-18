package test.casesoft.dmc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.casesoft.dmc.core.util.json.FastJSONUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.vo.OldProduct;
import com.casesoft.dmc.extend.api.web.hub.MirrorApiController;
import com.casesoft.dmc.model.shop.FittingRecord;
import com.casesoft.dmc.model.shop.Score;
import com.casesoft.dmc.service.shop.FittingRecordService;
import com.casesoft.dmc.service.shop.ScoreService;

import junit.framework.Assert;
import test.casesoft.dmc.BaseTestCase;
import test.casesoft.dmc.BeanContext;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by WingLi on 2017-01-19.
 */

public class MirrorApiControllerTest extends AbstractTestCase {
	@Autowired
    MirrorApiController mirrorApiController;
	@Autowired
    FittingRecordService fittingRecordService;
	@Autowired
    ScoreService scoreService;

//    public void setUp() throws Exception{
//        //super.setUp();
//    	mirrorApiController = (MirrorApiController) BeanContext.getApplicationContext().getBean("mirrorApiController");
//        fittingRecordService = (FittingRecordService) BeanContext.getApplicationContext().getBean("fittingRecordService");
//        scoreService = (ScoreService) BeanContext.getApplicationContext().getBean("scoreService");
//    }
	@Test
    public void testUploadFittingData() {
        String jsonString = "[{\"Id\":\"EPB160715112560\",\"Code\":\"EPB160715112560\",\"DeviceId\":\"KL160827\",\"Sku\":\"EPB160715112560\",\"ScanTime\":\"2016-07-15 11:25:59\"},"
                +"{\"Id\":\"EPB160715112561\",\"Code\":\"EPB160715112561\",\"DeviceId\":\"KL160827\",\"Sku\":\"EPB160715112560\",\"ScanTime\":\"2016-07-15 11:25:59\"}]";

        MessageBox message = this.mirrorApiController.uploadFittingDataWS(jsonString);
        Assert.assertTrue(message.getSuccess());

        FittingRecord fittingRecord = this.fittingRecordService.get("id","EPB160715112560");
        this.fittingRecordService.delete(fittingRecord);
        fittingRecord = this.fittingRecordService.get("id","EPB160715112561");
        this.fittingRecordService.delete(fittingRecord);
    }
	@Test
    public void testUploadScoreData() {
        String jsonString = "[{\"styleNo\":\"707K23050\",\"code\":\"11111111\","
                + "\"count\":2,\"colorNo\":\"05\",\"sizeNo\":\"333\","
                + "\"scoreTime\":\"2017-03-26 11:30:02\","
                + "\"deviceId\":\"KL160825\"}]";
        MessageBox message = this.mirrorApiController.scoreToFittingDataWS(jsonString);
        Assert.assertTrue(message.getSuccess());
        List<Score> scoreList = (List<Score>) message.getResult();
        this.scoreService.delete(scoreList,true);
    }
	@Test
    public void testPageFittingRecord() {
        Page page = new Page<>();
        List rows = new ArrayList<>();
        rows.add(new Integer(50));

        page.setPage(1);
        page.setSort("scanTime");
        page.setOrder("desc");
        page.setRows(rows);

        MessageBox msgBox  = this.mirrorApiController.pageFittingRecord(page);
        page = (Page) msgBox.getResult();
        String json = FastJSONUtil.getJSONString(page);
        JSONObject jsonObject = JSON.parseObject(json);
        Assert.assertTrue(jsonObject.getIntValue("pageNo") == 1);
        Assert.assertTrue(jsonObject.getIntValue("pageSize") == 50);
        Assert.assertTrue(jsonObject.getIntValue("total") > 0);
    }
	@Test
    public void testListDetonProductWS() {
        List<OldProduct> oldProductList = this.mirrorApiController.listDetonProductWS();
        Assert.assertNotNull(oldProductList.size() > 0);
    }
	@Test
    public void testListDetonCollocationWS() throws Exception {
        String skuCode = "W2WW4060399A063";//W2WW4060399A063
        OldProduct product = this.mirrorApiController.listDetonCollocationWS(skuCode);
        Assert.assertTrue(product.getCollocation().size()>0);

    }

}
