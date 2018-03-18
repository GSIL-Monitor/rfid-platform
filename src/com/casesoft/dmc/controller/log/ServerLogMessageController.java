package com.casesoft.dmc.controller.log;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ISearchController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.model.log.ServerLogMessage;
import com.casesoft.dmc.service.log.ServerLogMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by WingLi on 2017/1/11.
 */
@Controller
@RequestMapping("/sys/sysLog")
public class ServerLogMessageController extends BaseController implements ISearchController<ServerLogMessage> {

    @Autowired
    private ServerLogMessageService serverLogMessageService;

    @RequestMapping(value="/index")
    @Override
    public String index() {
        return "/views/log/serverLog";
    }

    @RequestMapping(value="/page")
    @ResponseBody
    @Override
    public Page<ServerLogMessage> findPage(Page<ServerLogMessage> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page=this.serverLogMessageService.findPage(page, filters);
        return page;
    }

    @Override
    public List<ServerLogMessage> list() throws Exception {
        return null;
    }

    @Override
    public void exportExcel() throws Exception {

    }
}
