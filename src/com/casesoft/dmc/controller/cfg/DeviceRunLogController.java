package com.casesoft.dmc.controller.cfg;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.model.cfg.DeviceRunLog;
import com.casesoft.dmc.service.cfg.DeviceRunLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by WinLi on 2017-03-30.
 */
@Controller
@RequestMapping("/data/deviceRunLog")
public class DeviceRunLogController extends BaseController {
    @Autowired
    private DeviceRunLogService deviceRunLogService;
    @RequestMapping("/index")
    @Override
    public String index() {
        return "views/data/deviceRunLog";
    }

    @RequestMapping("/page")
    @ResponseBody
    public Page<DeviceRunLog> findPage(Page<DeviceRunLog> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = deviceRunLogService.findPage(page, filters);
        return page;
    }
}
