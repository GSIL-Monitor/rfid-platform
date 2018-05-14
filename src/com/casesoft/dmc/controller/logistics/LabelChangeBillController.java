package com.casesoft.dmc.controller.logistics;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.product.StyleUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.LabelChangeBill;
import com.casesoft.dmc.model.logistics.LabelChangeBillDel;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Init;
import com.casesoft.dmc.service.logistics.LabelChangeBillService;
import com.casesoft.dmc.service.product.ProductService;
import com.casesoft.dmc.service.stock.EpcStockService;
import com.casesoft.dmc.service.sys.impl.PricingRulesService;
import com.casesoft.dmc.service.tag.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/9.
 */
@Controller
@RequestMapping("/logistics/labelChangeBill")
public class LabelChangeBillController extends BaseController implements ILogisticsBillController<LabelChangeBill> {
    @Autowired
    private LabelChangeBillService labelChangeBillService;
    @Autowired
    private InitService initService;
    @Autowired
    private PricingRulesService pricingRulesService;
    @Autowired
    private ProductService productService;
    @Autowired
    private EpcStockService epcStockService;
    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<LabelChangeBill> findPage(Page<LabelChangeBill> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        //权限设置，增加过滤条件，只显示当前ownerId下的销售单信息

        page.setPageProperty();
        page = this.labelChangeBillService.findPage(page, filters);
        return page;
    }

    @Override
    public List<LabelChangeBill> list() throws Exception {
        return null;
    }
    @RequestMapping(value = "/save")
    @ResponseBody
    @Override
    public MessageBox save(String bill, String strDtlList, String userId) throws Exception {
        //1.筛选需要保存的款式（有的新款式就不保存（爱丽丝的加AA），（as的加as））
        try {
            LabelChangeBill labelChangeBill = JSON.parseObject(bill, LabelChangeBill.class);
            List<LabelChangeBillDel> labelChangeBillDels = JSON.parseArray(strDtlList, LabelChangeBillDel.class);
            User currentUser = (User) this.getSession().getAttribute(
                    Constant.Session.User_Session);
            MessageBox messageBox = this.labelChangeBillService.saveLabelChangeBill(currentUser, labelChangeBill, labelChangeBillDels, userId, initService, pricingRulesService, productService);
            return messageBox;
        }catch (Exception e){
            e.printStackTrace();
            return new MessageBox(false, e.getMessage());
        }



    }

    @Override
    public ModelAndView add() throws Exception {
        return null;
    }

    @Override
    public ModelAndView edit(String billNo) throws Exception {
        return null;
    }

    @Override
    public MessageBox check(String billNo) throws Exception {
        return null;
    }

    @Override
    public MessageBox end(String billNo) throws Exception {
        return null;
    }

    @Override
    public MessageBox cancel(String billNo) throws Exception {
        return null;
    }

    @Override
    public MessageBox convert(String strDtlList, String recordList) throws Exception {
        return null;
    }
    @RequestMapping(value = "/index")
    @Override
    public String index() {
        return "/views/logistics/labelChangeBill";
    }
    @RequestMapping(value = "/add")
    @ResponseBody
    public ModelAndView add(String type) throws Exception {
        ModelAndView mv = new ModelAndView("views/logistics/labelChangeBillDel");
        mv.addObject("pageType", "add");
        mv.addObject("mainUrl", "/logistics/labelChangeBill/index.do");
        mv.addObject("userId", getCurrentUser().getId());
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        mv.addObject("type", type);
        return mv;
    }
    @RequestMapping(value = "/inventortyChangeLaber")
    @ResponseBody
    public MessageBox inventortyChangeLaber(String bill, String strDtlList,String userId){
        try {
            List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                    .getRequest());
            LabelChangeBill labelChangeBill = JSON.parseObject(bill, LabelChangeBill.class);
            List<LabelChangeBillDel> labelChangeBillDels = JSON.parseArray(strDtlList, LabelChangeBillDel.class);
            //查询所有sku和对应仓库在库的唯一码
            String skus="";
            for(int i=0;i<labelChangeBillDels.size();i++){
                if(i==0){
                    skus=labelChangeBillDels.get(i).getSku();
                }else{
                    skus+=","+labelChangeBillDels.get(i).getSku();
                }

            }
            PropertyFilter filterwarehouseId = new PropertyFilter("EQS_warehouseId", labelChangeBill.getOrigId());
            filters.add(filterwarehouseId);
            PropertyFilter filtersku = new PropertyFilter("INS_sku", skus);
            filters.add(filtersku);
            PropertyFilter filterin = new PropertyFilter("EQI_inStock", "1");
            filters.add(filterin);
            List<EpcStock> epcStocks = epcStockService.find(filters);
            //得到所有的在库的唯一
            String codes="";
            for(int b=0;b<epcStocks.size();b++){
                if(b==0){
                    codes=epcStocks.get(b).getCode();
                }else{
                    codes+=","+epcStocks.get(b).getCode();
                }
            }
            System.out.print(codes.split(",").length);
            //去掉code不在库存唯一码
            for(int a=0;a<labelChangeBillDels.size();) {
                String uniqueCodes = labelChangeBillDels.get(a).getUniqueCodes();
                //包含,
                if (CommonUtil.isNotBlank(uniqueCodes)) {
                    if (uniqueCodes.indexOf(",") != -1) {
                        String uniqueCodesew = "";
                        String[] split = uniqueCodes.split(",");
                        for (int c = 0; c < split.length; c++) {
                            if (codes.indexOf(split[c]) != -1) {
                                if (uniqueCodesew.equals("")) {
                                    uniqueCodesew = split[c];
                                } else {
                                    uniqueCodesew += "," + split[c];
                                }
                            }
                        }
                        if (uniqueCodesew.equals("")) {
                            labelChangeBillDels.remove(a);
                        } else {
                            labelChangeBillDels.get(a).setUniqueCodes(uniqueCodesew);
                            a++;
                        }
                    } else {
                        if (codes.indexOf(uniqueCodes) != -1) {
                            a++;
                        } else {
                            labelChangeBillDels.remove(a);
                        }
                    }

                }else {
                    labelChangeBillDels.remove(a);
                }
            }
            //判断是否还需要赛选系列
            if(labelChangeBill.getChangeType().equals(BillConstant.ChangeType.Series)){
                for(int d=0;d<labelChangeBillDels.size();){
                    String styleId = labelChangeBillDels.get(d).getStyleId();
                    Style style = CacheManager.getStyleById(styleId);
                    if(style.getClass9().equals(labelChangeBill.getNowclass9().split("-")[1])){
                        d++;
                    }else {
                        labelChangeBillDels.remove(d);
                    }
                }
            }
            User currentUser = (User) this.getSession().getAttribute(
                    Constant.Session.User_Session);
            MessageBox messageBox = this.labelChangeBillService.saveLabelChangeBill(currentUser, labelChangeBill, labelChangeBillDels, userId, initService, pricingRulesService, productService);
            return messageBox;
        }catch (Exception e){
            e.printStackTrace();
            return new MessageBox(false, e.getMessage());
        }


    }
}
