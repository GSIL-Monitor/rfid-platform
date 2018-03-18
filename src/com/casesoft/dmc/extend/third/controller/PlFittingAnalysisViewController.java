package com.casesoft.dmc.extend.third.controller;

import com.alibaba.fastjson.JSONObject;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.model.PlFittingAnalysisView;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.extend.third.service.PlFittingAnalysisViewService;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by john on 2017-03-03.
 */
@Controller
@RequestMapping("/third/playlounge/analysis")
public class PlFittingAnalysisViewController extends BaseController {

    @Autowired
    private ProductService productService;
    @Autowired
    private PlFittingAnalysisViewService plFittingAnalysisViewService;

    @RequestMapping(value = "/index")
    @Override
    public String index() {
        return "views/third/pl/plFittingAnalysisView";
    }


    @RequestMapping(value = "/plFitting_detail")
    @ResponseBody
    public MessageBox plFitting_detail() throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        List<Product> products = this.productService.find(filters);
        if (CommonUtil.isNotBlank(products)) {
            String sizeName = "";
            for (Product p : products) {
                sizeName += p.getSizeName();
            }
            JSONObject jsonObject=new JSONObject();
            String styleId=products.get(0).getStyleId();
            if (CommonUtil.isNotBlank(products.get(0).getImage())){
                jsonObject.put("image",products.get(0).getImage());
            }else{
                jsonObject.put("image","");
            }
            jsonObject.put("styleId",styleId);
            jsonObject.put("styleName",products.get(0).getStyleName());
            jsonObject.put("sizeName",sizeName);
            jsonObject.put("colorName",products.get(0).getColorName());

            PropertyKey pk1= CacheManager.getPropertyKey("C1-A-" + CacheManager.getStyleById(styleId).getClass1());
            if (CacheManager.getStyleById(styleId).getClass1()==null){
                jsonObject.put("class1", "");
            }else if (pk1==null){
                jsonObject.put("class1", CacheManager.getStyleById(styleId).getClass1());
            }else{
                jsonObject.put("class1",CacheManager.getPropertyKey("C1-A-"+CacheManager.getStyleById(styleId).getClass1()).getName());
            }

            PropertyKey pk2= CacheManager.getPropertyKey("C2-B-" + CacheManager.getStyleById(styleId).getClass2());
            if (CacheManager.getStyleById(styleId).getClass2()==null){
                jsonObject.put("class2", "");
            }else if (pk2==null){
                jsonObject.put("class2", CacheManager.getStyleById(styleId).getClass2());
            }else{
                jsonObject.put("class2",CacheManager.getPropertyKey("C2-B-" + CacheManager.getStyleById(styleId).getClass2()).getName());
            }

            PropertyKey pk3= CacheManager.getPropertyKey("C3-D-" + CacheManager.getStyleById(styleId).getClass3());
            if (CacheManager.getStyleById(styleId).getClass3()==null){
                jsonObject.put("class3", "");
            }else if (pk3==null){
                jsonObject.put("class3", CacheManager.getStyleById(styleId).getClass3());
            }else{
                jsonObject.put("class3",CacheManager.getPropertyKey("C3-D-" + CacheManager.getStyleById(styleId).getClass3()).getName());
            }

            PropertyKey pk4= CacheManager.getPropertyKey("C4-E-" + CacheManager.getStyleById(styleId).getClass4());
            if (CacheManager.getStyleById(styleId).getClass4()==null){
                jsonObject.put("class4", "");
            }else if (pk4==null){
                jsonObject.put("class4", CacheManager.getStyleById(styleId).getClass4());
            }else{
                jsonObject.put("class4",CacheManager.getPropertyKey("C4-E-" + CacheManager.getStyleById(styleId).getClass4()).getName());
            }

            PropertyKey pk5= CacheManager.getPropertyKey("C5-J-" + CacheManager.getStyleById(styleId).getClass5());
            if (CacheManager.getStyleById(styleId).getClass5()==null){
                jsonObject.put("class5", "");
            }else if (pk5==null){
                jsonObject.put("class5", CacheManager.getStyleById(styleId).getClass5());
            }else{
                jsonObject.put("class5",CacheManager.getPropertyKey("C5-J-" + CacheManager.getStyleById(styleId).getClass5()).getName());
            }


            PropertyKey pk6= CacheManager.getPropertyKey("C6-K-" + CacheManager.getStyleById(styleId).getClass6());
            if (CacheManager.getStyleById(styleId).getClass6()==null){
                jsonObject.put("class6", "");
            }else if (pk6==null){
                jsonObject.put("class6", CacheManager.getStyleById(styleId).getClass6());
            }else{
                jsonObject.put("class6",CacheManager.getPropertyKey("C6-K-" + CacheManager.getStyleById(styleId).getClass6()).getName());
            }


            if (CacheManager.getStyleById(styleId).getClass7()==null){
                jsonObject.put("class7","");
            }else{
                jsonObject.put("class7",CacheManager.getStyleById(styleId).getClass7());
            }

            PropertyKey pk8= CacheManager.getPropertyKey("C8-O-" + CacheManager.getStyleById(styleId).getClass8());
            if (CacheManager.getStyleById(styleId).getClass8()==null){
                jsonObject.put("class8", "");
            }else if (pk8==null){
                jsonObject.put("class8", CacheManager.getStyleById(styleId).getClass8());
            }else{
                jsonObject.put("class8",CacheManager.getPropertyKey("C8-O-" + CacheManager.getStyleById(styleId).getClass8()).getName());
            }

            PropertyKey pk9= CacheManager.getPropertyKey("C9-Q-" + CacheManager.getStyleById(styleId).getClass9());
            if (CacheManager.getStyleById(styleId).getClass9()==null){
                jsonObject.put("class9", "");
            }else if (pk9==null){
                jsonObject.put("class9", CacheManager.getStyleById(styleId).getClass9());
            }else{
                jsonObject.put("class9",CacheManager.getPropertyKey("C9-Q-" + CacheManager.getStyleById(styleId).getClass9()).getName());
            }

            PropertyKey pk10= CacheManager.getPropertyKey("C10-R-" + CacheManager.getStyleById(styleId).getClass10());
            if (CacheManager.getStyleById(styleId).getClass10()==null){
                jsonObject.put("class10", "");
            }else if (pk10==null){
                jsonObject.put("class10", CacheManager.getStyleById(styleId).getClass10());
            }else{
                jsonObject.put("class10",CacheManager.getPropertyKey("C10-R-" + CacheManager.getStyleById(styleId).getClass10()).getName());
            }
            return returnSuccessInfo("ok", jsonObject);
        }
        return returnSuccessInfo("null");
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public DataResult read(@RequestBody RequestPageData<PlFittingAnalysisView> request) {
        this.logAllRequestParams();
        DataResult plFittingAnalysisView= plFittingAnalysisViewService.find(request);
        if (CommonUtil.isNotBlank(plFittingAnalysisView)){
            for(PlFittingAnalysisView fv:(List<PlFittingAnalysisView>)plFittingAnalysisView.getData()){
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
        return plFittingAnalysisView;
    }


}
