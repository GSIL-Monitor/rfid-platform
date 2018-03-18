package com.casesoft.dmc.controller.task;

import java.util.List;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.task.BusinessDtl;
import com.casesoft.dmc.model.task.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.service.task.InboundTaskService;

@Controller
@RequestMapping("/task/inbound")
public class InboundTaskController extends BaseController implements IBaseInfoController<Business> {

    @Autowired
    private InboundTaskService inboundTaskService;

    @RequestMapping(value = "/index")
    @Override
    public String index() {

        return "/views/task/inbound";
    }

    @Override
    @RequestMapping(value = "/page")
    @ResponseBody
    public Page<Business> findPage(Page<Business> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(getRequest());
        page.setPageProperty();
        page = inboundTaskService.findPage(page, filters);
        for (Business b : page.getRows()) {
            if (CommonUtil.isNotBlank(b.getDestUnitId())) {
                b.setDestUnitName(CacheManager.getUnitById(b.getDestUnitId()).getName());
            }
            if (CommonUtil.isNotBlank(b.getOrigUnitId())) {
                b.setOrigUnitName(CacheManager.getUnitById(b.getOrigUnitId()).getName());
            }
            if (b.getDestId() != null) {
                b.setDestName(CacheManager.getUnitById(b.getDestId()).getName());
            }
            if (b.getOrigId() != null) {
                b.setOrigName(CacheManager.getUnitById(b.getOrigId()).getName());
            }
        }
        return page;
    }

    @RequestMapping(value = "/detail")
    @ResponseBody
    public ModelAndView viewDtl(String id) {
        this.logAllRequestParams();
        Business business = this.inboundTaskService.findBySearchId(id);
        if (CommonUtil.isNotBlank(business.getDestId())) {
            business.setDestName(CacheManager.getUnitById(business.getDestId()).getName());
        }
        if (CommonUtil.isNotBlank(business.getOrigId())) {
            business.setOrigName(CacheManager.getUnitById(business.getOrigId()).getName());
        }
        if (CommonUtil.isNotBlank(business.getOrigUnitId())) {
            if (CommonUtil.isNotBlank(CacheManager.getUnitById(business.getOrigUnitId()))) {
                business.setOrigUnitName(CacheManager.getUnitById(business.getOrigUnitId()).getName());
            }else {
                business.setOrigUnitName(CacheManager.getCustomerById(business.getOrigUnitId()).getName());
            }
        }
        if (CommonUtil.isNotBlank(business.getDestUnitId())) {
            business.setDestUnitName(CacheManager.getUnitById(business.getDestUnitId()).getName());
        }
        ModelAndView model = new ModelAndView();
        model.addObject("business", business);
        model.setViewName("/views/task/inbound_detail");
        return model;

    }

    @RequestMapping(value = "/detailPage")
    @ResponseBody
    public List<BusinessDtl> detailPage(String id, String filter_LIKES_styleId, String filter_LIKES_sku) throws Exception {
        this.logAllRequestParams();
        if (CommonUtil.isBlank(filter_LIKES_styleId))
            filter_LIKES_styleId = "";
        if (CommonUtil.isBlank(filter_LIKES_sku))
            filter_LIKES_sku = "";
        List<BusinessDtl> businessDtls = this.inboundTaskService.findBusinessDtl(id, filter_LIKES_styleId, filter_LIKES_sku);
        for (BusinessDtl dtl : businessDtls) {
            dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
        }
        return businessDtls;
    }

    @RequestMapping(value = "/recordPage")
    @ResponseBody
    public List<Record> recordPages(String id) throws Exception {
        this.logAllRequestParams();
        List<Record> record = this.inboundTaskService.findRecord(id);
        for (Record dtl : record) {
            dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
        }
        return record;
    }

    @Override
    public List<Business> list() throws Exception {

        return null;
    }

    @Override
    public MessageBox save(Business entity) throws Exception {

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


}
