package com.casesoft.dmc.controller.log;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ISearchController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.model.log.SysLog;
import com.casesoft.dmc.service.log.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by WingLi on 2017-01-12.
 */
@Controller
@RequestMapping("/sys/sysTableLog")
public class SysTableLogController extends BaseController implements ISearchController<SysLog> {
    @Autowired
    private SysLogService sysLogService;
    @Override
    public String index() {
        return null;
    }
    @RequestMapping(value="/page")
    @ResponseBody
    @Override
    public Page<SysLog> findPage(Page<SysLog> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page=this.sysLogService.findPage(page, filters);
        return page;
    }

    @Override
    public List<SysLog> list() throws Exception {
        return null;
    }

    @Override
    public void exportExcel() throws Exception {

    }
}
