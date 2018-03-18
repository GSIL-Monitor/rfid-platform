package com.casesoft.dmc.extend.third.controller;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.model.SaleStockThirdStockView;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.extend.third.service.SaleStockThirdStockViewService;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.sys.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by john on 2017-02-22.
 */
@RequestMapping("/third/saleStockThirdStock")
@Controller
public class SaleStockThirdStockViewController extends BaseController {
    @Autowired
    private SaleStockThirdStockViewService saleStockThirdStockViewService;
    @RequestMapping("/index")
    @Override
    public String index() {
        return "/views/third/saleStockThirdStock";
    }
    @ResponseBody
    @RequestMapping("/list")
    public DataResult read(@RequestBody RequestPageData<SaleStockThirdStockView> request) {
        this.logAllRequestParams();
        DataResult saleStockThirdStockView= saleStockThirdStockViewService.find(request);
        if (CommonUtil.isNotBlank(saleStockThirdStockView)){
            for(SaleStockThirdStockView fv:(List<SaleStockThirdStockView>)saleStockThirdStockView.getData()){
                Unit stock=CacheManager.getUnitById(fv.getStockCode());
                if(stock!=null){
                    fv.setStockName(stock.getName());
                }
                PropertyKey pkc10= CacheManager.getPropertyKey("C10-R-" + fv.getClass10());
                if (pkc10!=null){
                    fv.setClass10(pkc10.getName());
                }
                PropertyKey pkc4=CacheManager.getPropertyKey("C4-E-"+fv.getClass4());
                if (pkc4!=null){
                    fv.setClass4(pkc4.getName());
                }
                PropertyKey pkc3=CacheManager.getPropertyKey("C3-D-"+fv.getClass3());
                if (pkc3!=null){
                    fv.setClass3(pkc3.getName());
                }
                PropertyKey pkc2=CacheManager.getPropertyKey("C2-B-"+fv.getClass2());
                if (pkc2!=null){
                    fv.setClass2(pkc2.getName());
                }
                PropertyKey pkc1=CacheManager.getPropertyKey("C1-A-"+fv.getClass1());
                if (pkc1!=null){
                    fv.setClass1(pkc1.getName());
                }
            }
        }

        return saleStockThirdStockView;
    }

}
