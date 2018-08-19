package com.casesoft.dmc.controller.factory;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.factory.WorkCalendar;
import com.casesoft.dmc.service.factory.WorkCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * Created by GuoJunwen on 2017-05-11.
 */
@Controller
@RequestMapping("/factory/workCalendar")
public class WorkCalendarController extends BaseController implements IBaseInfoController<WorkCalendar> {

    @Autowired
    private WorkCalendarService workCalendarService;

    @RequestMapping("/index")
    @Override
    public String index() {
        return "views/factory/factoryWorkCalendar";
    }

    @RequestMapping("/page")
    @ResponseBody
    @Override
    public Page<WorkCalendar> findPage(Page<WorkCalendar> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        page.setPageProperty();
        page=this.workCalendarService.findPage(page,filters);
        return page;
    }


    @Override
    public List<WorkCalendar> list() throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        List<WorkCalendar> workCalendarList=this.workCalendarService.find(filters);
        return workCalendarList;
    }

    @RequestMapping("/list")
    @ResponseBody
    public MessageBox listWorkCalendar() throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        List<WorkCalendar> workCalendarList=this.workCalendarService.find(filters);
        return returnSuccessInfo("ok",workCalendarList);
    }


    @RequestMapping("/save")
    @ResponseBody
    @Override
    public MessageBox save(WorkCalendar workCalendar) throws Exception {

        workCalendar.setUpdateId(getCurrentUser().getCode());
        workCalendar.setUpdateTime(CommonUtil.getDateString(new Date(),"yyyy-MM-dd HH:mm:ss"));
        this.workCalendarService.save(workCalendar);
        /*CacheManager.refreshWorkCalendar();*/
        return returnSuccessInfo("ok");
    }

    @Override
    public MessageBox edit(String taskId) throws Exception {
        return null;
    }

    @RequestMapping("/delete")
    @ResponseBody
    @Override
    public MessageBox delete(String taskId) throws Exception {
        return null;
    }

    @Override
    public void exportExcel() throws Exception {

    }

    @Override
    public MessageBox importExcel(MultipartFile file) throws Exception {
        return null;
    }


}
