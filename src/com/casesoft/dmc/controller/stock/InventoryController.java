package com.casesoft.dmc.controller.stock;


import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.BusinessDtl;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.stock.InventoryService;
import com.casesoft.dmc.service.task.BusinessDtlService;
import com.casesoft.dmc.service.trace.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/stock/inventory")
public class InventoryController extends BaseController implements IBaseInfoController<Business> {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private RecordService recordService;
    @Autowired
    private BusinessDtlService businessDtlService;

    @RequestMapping(value = "/index")
    @Override
    public String index() {
        return "/views/stock/inventory";
    }

    @RequestMapping(value="/page")
    @ResponseBody
    @Override
    public Page<Business> findPage(Page<Business> page) throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page=this.inventoryService.findPage(page, filters);
        for (Business b:page.getRows()){
            if (b.getDestId()!=null){
                b.setDestName(CacheManager.getUnitById(b.getDestId()).getName());
            }
            if(b.getOrigId()!=null){
                b.setOrigName(CacheManager.getUnitById(b.getOrigId()).getName());
            }
        }
        return page;
    }

    @RequestMapping(value="/detail")
    @ResponseBody
    public ModelAndView showDetail(String id)throws Exception{
        this.logAllRequestParams();
        Business business=this.inventoryService.findById(id);
        if (business.getDestId()!=null){
            business.setDestName(CacheManager.getUnitById(business.getDestId()).getName());
            business.setOrigName(CacheManager.getUnitById(business.getOrigId()).getName());
        }
        ModelAndView model = new ModelAndView();
        model.addObject("business",business);
        model.setViewName("views/stock/inventory_detail");
        return model;
    }

   /* @RequestMapping(value = "/detailPage")
    @ResponseBody
    public List<BusinessDtl> detailPage(String id,String filter_LIKES_styleId,String filter_LIKES_sku)throws Exception{
        this.logAllRequestParams();
        if(CommonUtil.isBlank(filter_LIKES_styleId))
            filter_LIKES_styleId="";
        if(CommonUtil.isBlank(filter_LIKES_sku))
            filter_LIKES_sku="";
        List<BusinessDtl> businessDtls=this.inventoryService.findBusinessDtl(id,"","");
        for(BusinessDtl dtl : businessDtls) {
            dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
        }
        return businessDtls;
    }*/
    @RequestMapping(value = "/detailPage")
    @ResponseBody
    public Page<BusinessDtl> detailPage(Page<BusinessDtl> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.businessDtlService.findPage(page, filters);
        List<BusinessDtl> rows = page.getRows();
        ArrayList<BusinessDtl> list=new ArrayList<BusinessDtl>();
        for(BusinessDtl dtl : rows) {
            dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
            list.add(dtl);
        }
        page.setRows(list);
        return page;
    }
   /* @RequestMapping(value = "/codedetailPage")
    @ResponseBody
    public List<Record> codedetailPage(String id,String sku)throws Exception{
        this.logAllRequestParams();

        List<Record> record=this.inventoryService.findcodedetailPage(id,sku);
        for(Record dtl : record) {
            dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
        }
        return record;
    }*/
   @RequestMapping(value = "/codedetailPage")
   @ResponseBody
    public Page<Record> codedetailPage(Page<Record> page) throws Exception {
       List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.recordService.findPage(page, filters);
       List<Record> rows = page.getRows();
       ArrayList<Record> list=new ArrayList<Record>();
       for(Record dtl : rows) {
           dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
           dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
           dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
           list.add(dtl);
       }
       page.setRows(list);
       return page;
    }

    @RequestMapping(value = "/recordPage")
    @ResponseBody
    public List<Record> recordPages(String id)throws Exception{
        this.logAllRequestParams();
        List<Record> record=this.inventoryService.findRecord(id);
        for(Record dtl : record) {
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
    @RequestMapping(value = "/updateEpcStockInStock")
    @ResponseBody
    public MessageBox updateEpcStockInStock(String codes){
       try{
           this.inventoryService.updateEpcStockInStock(codes);
           return new MessageBox(true, "设置成功");
       }catch (Exception e) {
           e.printStackTrace();
           return new MessageBox(false, e.getMessage());
       }
    }
}
