package com.casesoft.dmc.controller.shop;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.BusinessDtl;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.stock.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/shop/shopInventory")
public class ShopInventoryController extends BaseController implements IBaseInfoController<Business> {

    @Autowired
    private InventoryService inventoryService;

    @RequestMapping(value = "/index")
    @Override
    public String index() {
        String ownerId = this.getCurrentUser().getOwnerId();
        Unit unit = CacheManager.getUnitById(ownerId);
        if(unit.getType() != Constant.UnitType.Shop) {
        	this.getRequest().setAttribute("ownerId",ownerId);
        } else {
        	this.getRequest().setAttribute("ownerId",unit.getOwnerId());//门店所属组织
            this.getRequest().setAttribute("shopId",ownerId);
            this.getRequest().setAttribute("shopName",unit.getName());

        }
        return "/views/shop/shopInventory";
    }

    @RequestMapping(value="/page")
    @ResponseBody
    @Override
    public Page<Business> findPage(Page<Business> page) throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page=this.inventoryService.findPage(page, filters);
        for (Business b : page.getRows()) {
            if(CommonUtil.isNotBlank(b.getDestId())) {
                b.setDestName(CacheManager.getUnitById(b.getDestId()).getName());
            }
            if (CommonUtil.isNotBlank(b.getOrigId())) {
                b.setOrigName(CacheManager.getUnitById(b.getOrigId()).getName());
            }

			if(CommonUtil.isNotBlank(b.getDestUnitId())) {
				b.setDestUnitName(CacheManager.getUnitById(b.getDestUnitId()).getName());
			}
			if(CommonUtil.isNotBlank(b.getOrigUnitId())) {
				b.setOrigUnitName(CacheManager.getUnitById(b.getOrigUnitId()).getName());
			}
        }
        return page;
    }

    @RequestMapping(value="/detail")
    @ResponseBody
    public ModelAndView showDetail(String id)throws Exception{
        this.logAllRequestParams();
        Business business=this.inventoryService.findById(id);
        ModelAndView model = new ModelAndView();
        if (CommonUtil.isNotBlank(business.getOrigId())) {
            business.setOrigName(CacheManager.getUnitById(business.getOrigId()).getName());
        }
        model.addObject("business",business);
        model.setViewName("views/shop/shopInventory_detail");
        return model;
    }

    @RequestMapping(value = "/detailPage")
    @ResponseBody
    public List<BusinessDtl> detailPage(String id)throws Exception{
        this.logAllRequestParams();
        List<BusinessDtl> businessDtls=this.inventoryService.findBusinessDtl(id,"","");
        for(BusinessDtl dtl : businessDtls) {
            dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
        }
        return businessDtls;
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
}
