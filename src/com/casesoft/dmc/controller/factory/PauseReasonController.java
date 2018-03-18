package com.casesoft.dmc.controller.factory;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.factory.PauseReason;
import com.casesoft.dmc.model.factory.Token;
import com.casesoft.dmc.service.factory.PauseReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * Created by GuoJunwen on 2017-05-08.
 */
@Controller
@RequestMapping("/factory/pauseReason")
public class PauseReasonController extends BaseController implements IBaseInfoController<PauseReason> {

    @Autowired
    private PauseReasonService pauseReasonService;

    @RequestMapping("/index")
    @Override
    public String index() {
        return "/views/factory/factoryPauseReason";
    }


    @RequestMapping("/page")
    @ResponseBody
    @Override
    public Page<PauseReason> findPage(Page<PauseReason> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        page.setPageProperty();
        page=this.pauseReasonService.findPage(page,filters);
        for (PauseReason p:page.getRows()){
            Token t= CacheManager.getFactoryTokenByToken(p.getToken());
            if (CommonUtil.isNotBlank(t)){
                p.setTokenName(t.getName());
            }
        }
        return page;
    }

    @RequestMapping("/list")
    @ResponseBody
    @Override
    public List<PauseReason> list() throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        List<PauseReason> pauseReasonList=this.pauseReasonService.find(filters);
        return pauseReasonList;
    }

    @RequestMapping("/save")
    @ResponseBody
    @Override
    public MessageBox save(PauseReason pauseReason) throws Exception {
        if (CommonUtil.isBlank(pauseReason.getId())){
             pauseReason.setId(new GuidCreator().toString());
        }else{

        }
        pauseReason.setUpDateTime(CommonUtil.getDateString(new Date(),"yyyy-MM-dd HH:mm:ss"));
        try {
            this.pauseReasonService.save(pauseReason);
        }catch (Exception e){
            return returnFailInfo("error",e.getMessage());
        }
        return returnSuccessInfo("ok");
    }

    @Override
    public MessageBox edit(String id) throws Exception {
        return null;
    }
    @RequestMapping("/delete")
    @ResponseBody
    @Override
    public MessageBox delete(String id) throws Exception {
        try {
            this.pauseReasonService.delete(id);
        }catch (Exception e){
            returnFailInfo("error");
        }
        return returnSuccessInfo("ok");
    }


    @RequestMapping("/findToken")
    @ResponseBody
    public void findToken() throws Exception{
        this.logAllRequestParams();
        List<Token> list = this.pauseReasonService.findToken();
        this.returnSuccess("ok",list);
    }

    @Override
    public void exportExcel() throws Exception {

    }

    @Override
    public MessageBox importExcel(MultipartFile file) throws Exception {
        return null;
    }
}
