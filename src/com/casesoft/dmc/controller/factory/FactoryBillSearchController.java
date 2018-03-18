package com.casesoft.dmc.controller.factory;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.factory.BillSchedule;
import com.casesoft.dmc.model.factory.FactoryBillDtlView;
import com.casesoft.dmc.model.factory.FactoryBillView;
import com.casesoft.dmc.service.factory.FactoryBillSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * Created by GuoJunwen on 2017-05-15.
 */
@Controller
@RequestMapping("/factory/billSearch")
public class FactoryBillSearchController extends BaseController implements IBaseInfoController<FactoryBillView> {

    @Autowired
    private FactoryBillSearchService factoryBillSearchService;

    @RequestMapping("/index")
    @Override
    public String index() {
        return "views/factory/factoryBillSearch";
    }

    @RequestMapping("/page")
    @ResponseBody
    @Override
    public Page<FactoryBillView> findPage(Page<FactoryBillView> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        page.setPageProperty();
        page=this.factoryBillSearchService.findPage(page,filters);
        FactoryBillUtil.coverToBill(page.getRows());
        return page;
    }


    @Override
    public List<FactoryBillView> list() throws Exception {
        return null;
    }



    @Override
    public MessageBox save(FactoryBillView entity) throws Exception {
        return null;
    }

    @Override
    public MessageBox edit(String taskId) throws Exception {
        return null;
    }

    @Override
    public MessageBox delete(String taskId) throws Exception {
        return null;
    }

    @Override
    public void exportExcel() throws Exception {

    }


    @RequestMapping("/exportExcel")
    @ResponseBody
    public MessageBox exportExcelFile() throws Exception{
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        String sort[]={"billDate"};
        List<FactoryBillView> list= this.factoryBillSearchService.find(filters,sort,"asc");
        File inputPath = FactoryBillUtil.writeBillFile(list);
        String filename =inputPath.getName();
        String contentType = "application/vnd.ms-excel;charset=utf-8";
        this.outFile(filename,inputPath,contentType);

        return returnSuccessInfo("ok");
    }


    @RequestMapping("/exportScheduleExcel")
    @ResponseBody
    public MessageBox exportScheduleExcel() throws Exception{
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        String sort[]={"billDate"};
        List<FactoryBillView> list= this.factoryBillSearchService.find(filters,sort,"asc");
        List<BillSchedule> scheduleList = this.factoryBillSearchService.findAllSchedule();
        File inputPath = FactoryBillUtil.writeBillScheduleFile(list,scheduleList);
        String filename =inputPath.getName();
        String contentType = "application/vnd.ms-excel;charset=utf-8";
        this.outFile(filename,inputPath,contentType);

        return returnSuccessInfo("ok");
    }

    @Override
    public MessageBox importExcel(MultipartFile file) throws Exception {

        return returnSuccessInfo("ok");
    }


}
