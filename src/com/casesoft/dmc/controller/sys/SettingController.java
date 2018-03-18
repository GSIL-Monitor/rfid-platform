package com.casesoft.dmc.controller.sys;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.sys.Setting;
import com.casesoft.dmc.service.sys.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Iterator;
import java.util.List;

/**
 * Created by WingLi on 2017-01-12.
 */
@Controller
@RequestMapping("/sys/sysSetting")
public class SettingController extends BaseController {

    @Autowired
    private SettingService settingService;

    @Override
    @RequestMapping(value = "/index")
    public String index() {
        List<Setting> settingList = this.settingService.getAll();
        Iterator sListIterator = settingList.iterator();
        int i=1;
        while(sListIterator.hasNext()){
            sListIterator.next();
            if(i>6){
                sListIterator.remove();
            }
            i++;
        }
        this.getRequest().setAttribute("settingList", settingList);
        return "/views/sys/setting";
    }

    @RequestMapping(value = "/page")
    @ResponseBody
    public Page<Setting> findPage(Page<Setting> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.settingService.findPage(page,filters);
        return page;
    }
    @RequestMapping(value = "/find")
    @ResponseBody
    public List<Setting> find() throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        return this.settingService.find(filters);
    }
    @RequestMapping(value = "/list")
    @ResponseBody
    public ModelAndView list() {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        List<Setting> settingList = this.settingService.find(filters);
        ModelAndView mv = new ModelAndView();
        for (int i=6;i<settingList.size();i++){
            settingList.remove(i);
        }
        mv.addObject("settingList",settingList);
        return mv;
    }
    @RequestMapping(value = "/refreshCache")
    @ResponseBody
    public MessageBox refreshCache() throws Exception {
        CacheManager.refresh();
        return this.returnSuccessInfo("刷新成功");
    }
}
