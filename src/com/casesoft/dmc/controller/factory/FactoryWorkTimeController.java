package com.casesoft.dmc.controller.factory;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.factory.FactoryWorkTime;
import com.casesoft.dmc.model.factory.Token;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.factory.FactoryTokenService;
import com.casesoft.dmc.service.factory.FactoryWorkTimeService;
import com.casesoft.dmc.service.sys.FactoryService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by GuoJunwen on 2017-05-10.
 */
@Controller
@RequestMapping("/factory/workTime")
public class FactoryWorkTimeController extends BaseController implements IBaseInfoController<FactoryWorkTime> {

    @Autowired
    private FactoryWorkTimeService factoryWorkTimeService;
    @Autowired
    private FactoryTokenService factoryTokenService;
    @Autowired
    private FactoryService factoryService;

    @RequestMapping("/index")
    @Override
    public String index() {
        return "views/factory/factoryWorkTime";
    }

    @RequestMapping("/page")
    @ResponseBody
    @Override
    public Page<FactoryWorkTime> findPage(Page<FactoryWorkTime> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        page.setPageProperty();
        page=this.factoryWorkTimeService.findPage(page,filters);
        for (FactoryWorkTime f:page.getRows()){
            f.setTokenName(CacheManager.getFactoryTokenByToken(f.getToken()).getName());
            f.setCodeName(CacheManager.getUnitByCode(f.getCode()).getName());
        }
        return page;
    }

    @Override
    public List<FactoryWorkTime> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(FactoryWorkTime entity) throws Exception {
        return null;
    }

    @RequestMapping("/findWorkTime")
    @ResponseBody
    public MessageBox findWorkTime(String code,Integer token){
        FactoryWorkTime factoryWorkTime=this.factoryWorkTimeService.findUniqueFactoryWorkTime(code,token);
        return returnSuccessInfo("ok",factoryWorkTime);
    }

    @RequestMapping("/save")
    @ResponseBody
    public MessageBox save(FactoryWorkTime factoryWorkTime,String token) throws Exception {

        String code[]=factoryWorkTime.getCode().split(",");
        String tokens[]=token.split(",");
        List<FactoryWorkTime> factoryWorkTimeList=new ArrayList<>();
        String morningStartTime=factoryWorkTime.getMorningStartTime();
        String morningEndTime=factoryWorkTime.getMorningEndTime();
        String afternoonStartTime=factoryWorkTime.getAfternoonStartTime();
        String afternoonEndTime=factoryWorkTime.getAfternoonEndTime();
        Double morningTotalTime=factoryWorkTime.getMorningTotalTime();
        Double dayTotalTime=factoryWorkTime.getDayTotalTime();
        for (int i=0;i<code.length;i++){
            for (int j=0;j<tokens.length;j++){
                FactoryWorkTime f=new FactoryWorkTime();
                f.setMorningStartTime(morningStartTime);
                f.setMorningEndTime(morningEndTime);
                f.setAfternoonStartTime(afternoonStartTime);
                f.setAfternoonEndTime(afternoonEndTime);
                f.setMorningTotalTime(morningTotalTime);
                f.setDayTotalTime(dayTotalTime);
                f.setLocked(0);
                f.setCode(code[i]);
                f.setToken(new Integer(tokens[j]));
                f.setUpdateId(getCurrentUser().getCode());
                f.setUpdateTime(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"));
                factoryWorkTimeList.add(f);
            }
        }

        this.factoryWorkTimeService.saveList(factoryWorkTimeList);
        CacheManager.refreshFactoryWorkTime();
        return returnSuccessInfo("ok");
    }

    @RequestMapping("/edit")
    @ResponseBody
    @Override
    public MessageBox edit(String taskId) throws Exception {
        return null;
    }

    @Override
    public MessageBox delete(String taskId) throws Exception {
        return null;
    }


    @RequestMapping("/delete")
    @ResponseBody
    public MessageBox delete(String code,Integer token) throws Exception {
        this.factoryWorkTimeService.deleteWorkTime(code,token);
        CacheManager.refreshFactoryWorkTime();
        return returnSuccessInfo("ok");
    }

    @Override
    public void exportExcel() throws Exception {

    }

    @Override
    public MessageBox importExcel(MultipartFile file) throws Exception {
        return null;
    }

    @RequestMapping("/initSelect")
    @ResponseBody
    public MessageBox initSelect(){
        List<Token> tokenList = this.factoryTokenService.findAllToken();
        List factoryList=this.factoryService.findAllFactory();
        JSONObject obj=new JSONObject();
        obj.put("tokenList",tokenList);
        obj.put("factoryList",factoryList);
        return returnSuccessInfo("ok",obj);
    }

}
