package com.casesoft.dmc.controller.sys;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.sys.impl.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 2017/1/5.
 */
@Controller
@RequestMapping("/unit")
public class UnitController extends BaseController implements IBaseInfoController<Unit> {
    @Autowired
    private UnitService unitService;

    @RequestMapping(value = "/index")
    @Override
    public String index() {
        return "views/sys/unit";
    }

    @RequestMapping(value={"/page","/pageWS"})
    @ResponseBody
    @Override
    public Page<Unit> findPage(Page<Unit> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.unitService.findPage(page,filters);
        return page;
    }

    @RequestMapping(value={"/list","/listWS"})
    @ResponseBody
    @Override
    public List<Unit> list() throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        List<Unit> vendors = this.unitService.find(filters);
        return vendors;
    }


    @RequestMapping(value="/save")
    @ResponseBody
    @Override
    public MessageBox save(Unit unit) throws Exception {
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
    @Override
    public MessageBox importExcel(MultipartFile file) throws Exception {

        return null;
    }


    @RequestMapping(value = "/viewStatement")
    @ResponseBody
    public ModelAndView findUnit(String id) {
        ModelAndView mav = new ModelAndView("/views/sys/unit_edit");
        mav.addObject("id", id);
        mav.addObject("callback","/unit/index.do");
        Unit unit = this.unitService.load(id);
        mav.addObject("unit",unit);
        return mav;
    }

}
