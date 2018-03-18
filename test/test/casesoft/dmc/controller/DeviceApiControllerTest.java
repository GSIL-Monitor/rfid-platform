package test.casesoft.dmc.controller;

import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.web.hub.DeviceApiController;
import com.casesoft.dmc.service.cfg.DeviceRunLogService;
import junit.framework.Assert;
import test.casesoft.dmc.BaseTestCase;
import test.casesoft.dmc.BeanContext;

/**
 * Created by WinLi on 2017-03-30.
 */
public class DeviceApiControllerTest extends BaseTestCase {
    DeviceApiController deviceApiController;
    DeviceRunLogService deviceRunLogService;

    public void setUp() throws Exception{
        super.setUp();
        deviceApiController = (DeviceApiController) BeanContext.getApplicationContext().getBean("deviceApiController");
        deviceRunLogService = (DeviceRunLogService) BeanContext.getApplicationContext().getBean("deviceRunLogService");
    }

    public void testUploadDeviceRunLog() throws Exception {
        String jsonString = "{deviceId:\"KA201601\"," +
                "logDate:\"2017-04-02\"," +
                "openPcTime:\"2017-03-30 12:23:11\"," +
                "deviceIp:\"192.168.1.9\"," +
                "smdjHighKm:16," +
                "smdjLowKm:6," +
                "sldjKm:6," +
                "cldjKm:2," +
                "qmOnCs:48," +
                "qmOffCs:34," +
                "hmOnCs:45," +
                "hmOffCs:56}";
        MessageBox msgBox = this.deviceApiController.uploadRunLog(jsonString);
        Assert.assertTrue(msgBox.getSuccess());
        this.deviceRunLogService.deleteRunLog("KA201601","2017-04-01");
    }
}
