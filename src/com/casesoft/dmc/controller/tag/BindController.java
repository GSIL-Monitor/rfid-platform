package com.casesoft.dmc.controller.tag;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.model.search.SearchBoxDtlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.tag.EpcBindBarcode;
import com.casesoft.dmc.service.product.ProductService;
import com.casesoft.dmc.service.tag.BindService;

@Controller
@RequestMapping("/tag/bind")
public class BindController extends BaseController implements IBaseInfoController<EpcBindBarcode>{
	@Autowired
	private BindService bindService;	
	@Autowired
	private ProductService productService;
	
	@RequestMapping("/index")
	@Override
	public String index() {
		
		return "/views/tag/bind";
	}
	

	@Override
	public Page<EpcBindBarcode> findPage(@RequestBody Page<EpcBindBarcode> page)
			throws Exception {
		this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page=this.bindService.findPage(page, filters);

		return page;
	}
    
    @RequestMapping(value="/page")
    @ResponseBody
    public DataResult read(@RequestBody RequestPageData<EpcBindBarcode> request){

        DataResult dataResult = this.bindService.find(request);
        List<EpcBindBarcode> epcBindBarcodeList= (List<EpcBindBarcode>) dataResult.getData();
        InitUtil.covertToBindInfo(epcBindBarcodeList);
        return dataResult;
    }
	@RequestMapping(value="/findproduct")
	@ResponseBody
	public MessageBox findProduct(String code) throws Exception{
		Product product=this.productService.findProductByCode(code);
        if (CommonUtil.isBlank(product)){
            return returnFailInfo("条码错误");
        }else{
            return returnSuccessInfo("ok",product);
        }
	}
	
	@RequestMapping(value="/bindcode")
	@ResponseBody
	public MessageBox bind(String code,String epc)throws Exception{
		this.logAllRequestParams();
         if (CommonUtil.isBlank(code)){
             return returnFailInfo("条码为空");
         }
		 if(CommonUtil.isNotBlank(epc)){
             if ( epc.length()==12||epc.length()==24||epc.length()==32){
                 EpcBindBarcode epcbindbarcode=new EpcBindBarcode(epc,code);
                 Product p = this.bindService.findProductBybarcode(epcbindbarcode.getCode());
                 if(CommonUtil.isBlank(p)){
                     return this.returnFailInfo("条码错误");
                 }
                 epcbindbarcode.setUpdateTime(new Date());
                 epcbindbarcode.setUpdaterId(this.getCurrentUser().getId());
                 long version=this.bindService.findMaxProductTempVersion();
                 epcbindbarcode.setDisabled(false);
                 epcbindbarcode.setVersion(version);
                 this.bindService.save(epcbindbarcode);
                 return this.returnSuccessInfo("绑定成功",epcbindbarcode);
             }else {
                 return  returnFailInfo("epc长度为12、24、32");
             }


		 }else{
			 return this.returnFailInfo("epc为空");
		 }
	}

    @RequestMapping(value = "/unbind")
    @ResponseBody
    public MessageBox unbind(String epc)throws Exception{
        this.logAllRequestParams();

        if(CommonUtil.isNotBlank(epc)){
            EpcBindBarcode epcbindbarcode=this.bindService.get("epc",epc);
            if (epcbindbarcode==null){
                return this.returnFailInfo("epc错误");
            }else{
            	epcbindbarcode.setDisabled(true);
            	epcbindbarcode.setVersion(this.bindService.findMaxProductTempVersion()+1);
                this.bindService.delete(epcbindbarcode);
                return  this.returnSuccessInfo("解绑成功", epcbindbarcode);
            }
        }else{
            return this.returnFailInfo("epc为空");
        }
    }

    @RequestMapping(value = "/checkBarcode")
    @ResponseBody
    public Map<String, Boolean> checkBarcode(String form_code){
        form_code=form_code.toUpperCase();
        Map<String, Boolean> json = new HashMap<>();
        Product p = this.bindService.findProductBybarcode(form_code);
        if (CommonUtil.isBlank(p)){
            json.put("valid", false);
        }else{
            json.put("valid", true);
        }
        return json;
    }

    @RequestMapping(value = "/checkEpc")
    @ResponseBody
    public Map<String, Boolean> checkEpc(String unbind_epc)  {
        Map<String, Boolean> json = new HashMap<>();
        EpcBindBarcode epcbindbarcode=this.bindService.get("epc",unbind_epc);
        if (CommonUtil.isBlank(epcbindbarcode)){
            json.put("valid", false);
        }else {
            json.put("valid", true);
        }
        return json;
    }

	@Override
	public List<EpcBindBarcode> list() throws Exception {
		return null;
	}

	
	@Override
	public MessageBox save(EpcBindBarcode entity) throws Exception {
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
