package com.casesoft.dmc.controller.syn;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.syn.BasicConfiguration;
import com.casesoft.dmc.service.syn.BasicConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * Created by pc on 2016-12-18.
 */
@Controller
@RequestMapping("/syn/basicConfiguration")
public class BasicConfigurationController extends BaseController {

    @Autowired
    private BasicConfigurationService basicConfigurationService;
    public String save() throws Exception {
        return null;
    }

    @RequestMapping("/list")
    @ResponseBody
     public Page<BasicConfiguration> list(Page<BasicConfiguration> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
         page.setPageProperty();
         page = this.basicConfigurationService.findPage(page,filters);
         return page;
     }
    @RequestMapping("/update")
    @ResponseBody
    public MessageBox update(BasicConfiguration basicConfiguration) throws Exception {
         try {
            this.logAllRequestParams();
             basicConfiguration.setUpdateDate(new Date());
            this.basicConfigurationService.update(basicConfiguration);
             this.returnSuccess("更新成功！");
        }catch (Exception e){
            e.printStackTrace();
              this.returnFailur("更新失败！");
        }
        return null;
    }


    public String delete() throws Exception {
        return null;
    }
    @RequestMapping("/timerIndex/index")
    public String timerIndex(){
        return "/views/syn/timerManager";
    }
    @RequestMapping("/downBasicIndex/index")
    public String basicIndex(){
        return "/views/syn/downBasic";
    }
    @Override
    public String index() {
        return null;
    }
}
