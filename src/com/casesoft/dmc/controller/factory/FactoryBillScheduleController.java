package com.casesoft.dmc.controller.factory;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.factory.BillSchedule;
import com.casesoft.dmc.model.factory.FactoryBill;
import com.casesoft.dmc.service.factory.FactoryBillScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * Created by GuoJunwen on 2017-05-17.
 */
@Controller
@RequestMapping("/factory/billSchedule")
public class FactoryBillScheduleController extends BaseController {

    @Autowired
    private FactoryBillScheduleService factoryBillScheduleService;

    @RequestMapping("/index")
    @Override
    public String index() {
        return "views/factory/factoryBillSchedule";
    }

    @RequestMapping("/page")
    @ResponseBody
    public Page<BillSchedule> findPage(Page<BillSchedule> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        page.setPageProperty();
        page=this.factoryBillScheduleService.findPage(page,filters);
        return page;
    }

    @RequestMapping("/list")
    @ResponseBody
    public List<BillSchedule> list() throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        List<BillSchedule> billScheduleList=this.factoryBillScheduleService.find(filters);
        FactoryBillUtil.coverToScehdule(billScheduleList);
        return billScheduleList;
    }

    @RequestMapping("/saveMutiSchedule")
    @ResponseBody
    public MessageBox saveMutiSchedule(String billData, String schedule){
        List<FactoryBill> billList = JSON.parseArray(billData, FactoryBill.class);
        List<BillSchedule> scheduleList = JSON.parseArray(schedule, BillSchedule.class);

       try{
           List<BillSchedule> billScheduleList =FactoryBillUtil.covertToBillSchedule(billList, scheduleList,getCurrentUser());
           this.factoryBillScheduleService.saveList(billList,billScheduleList);

           return returnSuccessInfo("ok");
       }catch (Exception e){
           return returnFailInfo("error",e.getMessage());
       }
    }

    @RequestMapping("/saveSchedule")
    @ResponseBody
    public MessageBox saveSchedule(String  factoryBillStr, String billScheduleStr){
        try{
            FactoryBill factoryBill = JSON.parseObject(factoryBillStr,FactoryBill.class);
            BillSchedule billSchedule = JSON.parseObject(billScheduleStr,BillSchedule.class);
            billSchedule.setBillNo(factoryBill.getBillNo());
            billSchedule.setBillDate(factoryBill.getBillDate());
            billSchedule.setEndDate(factoryBill.getEndDate());
            billSchedule.setUploadNo(factoryBill.getUploadNo());
            billSchedule.setUpdateId(getCurrentUser().getCode());
            billSchedule.setUpdateTime(CommonUtil.getDateString(new Date(),"yyyy-MM-dd HH:mm:ss"));
            /*if( CacheManager.getFactoryTokenByToken(billSchedule.getToken()).getIsLast()=="Y"){
                billSchedule.setType("I");
            }else{
                billSchedule.setType("O");
            }*/
            this.factoryBillScheduleService.save(billSchedule);

            return returnSuccessInfo("ok");
        }catch (Exception e){
            return returnFailInfo("error",e.getMessage());
        }
    }

    @RequestMapping("/deleteSchedule")
    @ResponseBody
    public MessageBox saveSchedule(String  type, Integer token, String billNo, String schedule) {
     this.factoryBillScheduleService.deleteSchedule(  type,   token, billNo, schedule);
     return returnSuccessInfo("ok");
    }

}
