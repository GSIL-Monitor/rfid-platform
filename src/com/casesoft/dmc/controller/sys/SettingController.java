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
        //查询系统操作
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        PropertyFilter filter = new PropertyFilter("EQS_valueType", "sys");
        filters.add(filter);
        List<Setting> settingList = this.settingService.getAll();
        List<Setting> operateList = this.settingService.find(filters);
        Iterator sListIterator = settingList.iterator();

        while(sListIterator.hasNext()){
            Setting setting = (Setting) sListIterator.next();
            if("sys".equals(setting.getValueType())){
                sListIterator.remove();
            }
        }
        Iterator sListIterator1 = settingList.iterator();
        int i= 1;
        while (sListIterator1.hasNext()){
            sListIterator1.next();
            if(i>6){
                sListIterator1.remove();
            }
            i++;
        }
        this.getRequest().setAttribute("settingList", settingList);
        this.getRequest().setAttribute("operateList", operateList);
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
    @RequestMapping(value = "/setOperate")
    @ResponseBody
    public MessageBox setOperate() throws Exception {
        try{
            String opId = request.getParameter("id");
            String opValue = request.getParameter("value");
            Setting setting = settingService.get("id",opId);
            if("false".equals(opValue)){
                setting.setValue("true");
            }
            else{
                setting.setValue("false");
            }
            this.settingService.save(setting);
        }catch (Exception e){
            e.printStackTrace();
            return this.returnSuccessInfo("设置失败");
        }
        return this.returnSuccessInfo("设置成功");
    }
}
