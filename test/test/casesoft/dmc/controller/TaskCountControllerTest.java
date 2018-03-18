package test.casesoft.dmc.controller;

import com.casesoft.dmc.controller.chart.TaskCountController;
import test.casesoft.dmc.BaseTestCase;
import test.casesoft.dmc.BeanContext;

/**
 * Created by WinLi on 2017-03-21.
 */
public class TaskCountControllerTest extends BaseTestCase {

    TaskCountController taskCountController;

    public void setUp() throws Exception {
        super.setUp();
        taskCountController = (TaskCountController) BeanContext
                .getApplicationContext().getBean("taskCountController");
    }

    public void testCountVendorOutbound() {

    }
}
