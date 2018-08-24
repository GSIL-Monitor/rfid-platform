package com.casesoft.dmc.controller.sys;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.sys.FactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by GuoJunwen on 2017-05-08.
 */
@Controller
@RequestMapping("/sys/factory")
public class FactoryController extends BaseController implements IBaseInfoController<Unit> {

    @Autowired
    private FactoryService factoryService;

    @RequestMapping("/index")
    @Override
    public String index() {
        return "views/sys/factory";
    }


    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<Unit> findPage(Page<Unit> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page=this.factoryService.findPage(page,filters);
        for (Unit u:page.getRows()){
            u.setUnitName(CacheManager.getUnitById(u.getOwnerId()).getName());
        }
        return page;
    }

    @Override
    public List<Unit> list() throws Exception {
        return null;
    }


    @RequestMapping(value = "/save")
    @ResponseBody
    @Override
    public MessageBox save(Unit unit) throws Exception {
        this.logAllRequestParams();
        if (CommonUtil.isBlank(unit.getId())){
            if(CommonUtil.isBlank(unit.getCode())){
               String code=this.factoryService.findMaxCode();
               unit.setCode(code);
            }
            unit.setId(unit.getCode());
            unit.setCreateTime(new Date());
            unit.setCreatorId(getCurrentUser().getCode());
        }else{

        }
        if (CommonUtil.isBlank(unit.getOwnerId())){
            unit.setOwnerId("1");
        }
        unit.setType(Constant.UnitType.Factory);
        this.factoryService.save(unit);
        List<Unit> unitList = new ArrayList<>();
        unitList.add(unit);
        CacheManager.refreshUnitCache(unitList);
        return returnSuccessInfo("ok");
    }

    @RequestMapping(value = "/lock")
    @ResponseBody
    public MessageBox lock(String id){
        Unit unit=this.factoryService.findUnitById(id);
        unit.setLocked(1);
        this.factoryService.save(unit);
        return returnSuccessInfo("ok");
    }

    @RequestMapping(value = "/unlock")
    @ResponseBody
    public MessageBox unlock(String id){
        Unit unit=this.factoryService.findUnitById(id);
        unit.setLocked(0);
        this.factoryService.save(unit);
        return returnSuccessInfo("ok");
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

    @Override
    public MessageBox importExcel(MultipartFile file) throws Exception {
        return null;
    }
}
