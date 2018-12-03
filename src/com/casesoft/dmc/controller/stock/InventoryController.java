package com.casesoft.dmc.controller.stock;


import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.SaleOrderBillDtl;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.BusinessDtl;
import com.casesoft.dmc.model.task.InventoryTransformation;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.stock.InventoryService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import com.casesoft.dmc.service.task.BusinessDtlService;
import com.casesoft.dmc.service.trace.RecordService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private UnitService unitService;

    @RequestMapping(value = "/index")
    public ModelAndView indexMV()throws Exception {
        ModelAndView modelAndView= new ModelAndView("/views/stock/inventory");
        Unit unit = this.unitService.getunitbyId(getCurrentUser().getOwnerId());
        String defaultWarehId = unit.getDefaultWarehId();
        modelAndView.addObject("defaultWarehId", defaultWarehId);
        modelAndView.addObject("userId", getCurrentUser().getId());
        modelAndView.addObject("ownerId", getCurrentUser().getOwnerId());
        return modelAndView;
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

    @Override
    public String index() {
        return null;
    }
    /*
     * @Author Liutianci
     * @Description //TODO 批量盘点
     * @Date 15:42 2018/12/1
     * @Param [origId, strEpcList] origId：店铺id strEpcList 商品信息sku汇总
     * @return com.casesoft.dmc.core.vo.MessageBox
     **/
    @RequestMapping(value = "/batchInventorySave")
    @ResponseBody
    public MessageBox batchInventorySave(String origId,String strEpcList,String rightEpcCode){
        logAllRequestParams();
        //Business Preservation method
        Long totEpc =0L;
        Long totSku =0L;
        List<String> styleIdList=new ArrayList<>();
        boolean isExist = false;
        List<InventoryTransformation> inventoryTransformationList =JSON.parseArray(strEpcList, InventoryTransformation.class);
        for (InventoryTransformation inventoryTransformation :inventoryTransformationList){
            totEpc+=inventoryTransformation.getQty();
            totSku+=1;
            if (styleIdList.size()==0){
                styleIdList.add(inventoryTransformation.getStyleId());
            }else {
                for (String styleId : styleIdList){
                    if (inventoryTransformation.getStyleId().equals(styleId)){
                        isExist=true;
                    }
                }
            }
            if (!isExist){
                styleIdList.add(inventoryTransformation.getStyleId());
            }
        }
        Integer styleIdNum=styleIdList.size();
        Business business = new Business();
        String newId= BillConstant.BillPrefix.batchInventoryBill+ CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
        business.setId(newId);
        business.setBeginTime(new Date());
        business.setDestId(origId);
        business.setDestUnitId(origId);
        business.setDeviceId("批量扫码");
        business.setEndTime(new Date());
        business.setOrigId(origId);
        business.setOrigUnitId(origId);
        business.setOwnerId(getCurrentUser().getId());
        business.setStatus(0);
        business.setToken(9);
        business.setTotCarton(1L);
        business.setTotEpc(totEpc);
        business.setTotSku(totSku);
        business.setTotStyle(styleIdNum.longValue());
        business.setType(1);
        //businessdtl Preservation method
        List<BusinessDtl> businessDtlList =JSON.parseArray(strEpcList, BusinessDtl.class);
        for (BusinessDtl businessDtl : businessDtlList){
            businessDtl.setId(new GuidCreator().toString());
            businessDtl.setOrigId(origId);
            businessDtl.setOrigUnitId(origId);
            businessDtl.setDeviceId("批量扫码");
            businessDtl.setDestId(origId);
            businessDtl.setDestUnitId(origId);
            businessDtl.setOwnerId(getCurrentUser().getId());
            businessDtl.setTaskId(business.getId());
            businessDtl.setToken(9);
            businessDtl.setType(3);
        }
        //record Preservation method
        List<Record> recordList = JSON.parseArray(rightEpcCode,Record.class);
        for (Record record :recordList){
            record.setId(new GuidCreator().toString());
            record.setCartonId(business.getId());
            record.setDestId(origId);
            record.setDestUnitId(origId);
            record.setDeviceId("批量扫码");
            record.setOrigId(origId);
            record.setOrigUnitId(origId);
            record.setOrigId(getCurrentUser().getId());
            record.setTaskId(business.getId());
            record.setToken(9);
            record.setType(1);
        }
        try {
            this.inventoryService.saveBatchDtl(business,businessDtlList,recordList);
            return new MessageBox(true,"保存成功!");
        }catch (Exception e){
            e.printStackTrace();
            return new MessageBox(false,"保存失败!");
        }

    }
}
