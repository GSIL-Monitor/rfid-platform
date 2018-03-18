package com.casesoft.dmc.controller.task;

import java.util.List;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.sys.Unit;
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
import com.casesoft.dmc.service.task.OutboundTaskService;

@Controller
@RequestMapping("/task/outbound")
public class OutboundTaskController extends BaseController implements IBaseInfoController<Business>{

	@Autowired
	private OutboundTaskService outboundtaskService;
	
	@RequestMapping(value = "/index")
	@Override
	public String index() {
		
		return "/views/task/outbound";
	}
	
	@RequestMapping(value="/page")
	@ResponseBody
	@Override
	public Page<Business> findPage(Page<Business> page) throws Exception {
		this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page=this.outboundtaskService.findPage(page, filters);
            for (Business b : page.getRows()) {
                if(b.getDestId()!=null) {
                    b.setDestName(CacheManager.getUnitById(b.getDestId()).getName());
                }
                if (b.getOrigId()!=null) {
                    b.setOrigName(CacheManager.getUnitById(b.getOrigId()).getName());
                }

				if(CommonUtil.isNotBlank(b.getDestUnitId())) {
                	Unit u = CacheManager.getUnitById(b.getDestUnitId());
                	if(CommonUtil.isNotBlank(u)){
						b.setDestUnitName(u.getName());
					}else{
						Customer customer = CacheManager.getCustomerById(b.getDestUnitId());
						if(CommonUtil.isNotBlank(customer)){
							b.setDestUnitName(customer.getName());
						}
					}

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
		Business business=this.outboundtaskService.findById(id);
		ModelAndView model = new ModelAndView();
        if (business.getOrigId()!=null) {
            business.setOrigName(CacheManager.getUnitById(business.getOrigId()).getName());
        }
        if(business.getDestId()!=null) {
            business.setDestName(CacheManager.getUnitById(business.getDestId()).getName());
        }
        if (business.getOrigId()!=null) {
            business.setOrigName(CacheManager.getUnitById(business.getOrigId()).getName());
        }

		if(CommonUtil.isNotBlank(business.getDestUnitId())) {
			Unit unit = CacheManager.getUnitById(business.getDestUnitId());
			Customer customer = CacheManager.getCustomerById(business.getDestUnitId());
			if(CommonUtil.isNotBlank(unit)){
				business.setDestUnitName(unit.getName());
			}else{
				if(CommonUtil.isNotBlank(customer)){
					business.setDestUnitName(customer.getName());
				}
			}
		}
		if(CommonUtil.isNotBlank(business.getOrigUnitId())) {
			business.setOrigUnitName(CacheManager.getUnitById(business.getOrigUnitId()).getName());
		}
        model.addObject("business",business);
		model.setViewName("views/task/outbound_detail");
		return model;
	}

    @RequestMapping(value = "/detailPage")
    @ResponseBody
    public List<BusinessDtl> detailPage(String id,String filter_LIKES_styleId,String filter_LIKES_sku)throws Exception{
        this.logAllRequestParams();
        if(CommonUtil.isBlank(filter_LIKES_styleId))
        	filter_LIKES_styleId="";
        if(CommonUtil.isBlank(filter_LIKES_sku))
        	filter_LIKES_sku="";
        List<BusinessDtl> businessDtls=this.outboundtaskService.findBusinessDtl(id,filter_LIKES_styleId,filter_LIKES_sku);
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
        List<Record> record=this.outboundtaskService.findRecord(id);
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
