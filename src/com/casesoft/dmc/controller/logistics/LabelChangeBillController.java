package com.casesoft.dmc.controller.logistics;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.product.StyleUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.LabelChangeBill;
import com.casesoft.dmc.model.logistics.LabelChangeBillDel;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Init;
import com.casesoft.dmc.service.logistics.LabelChangeBillService;
import com.casesoft.dmc.service.product.ProductService;
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
    @Override
    public Page<LabelChangeBill> findPage(Page<LabelChangeBill> page) throws Exception {
        return null;
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
            String rootPath = this.getSession().getServletContext().getRealPath("/");
            String imageUrl = StyleUtil.returnImageUrl(labelChangeBillDels.get(0).getStyleId(), rootPath);
            Map<String, Object> map = StyleUtil.newstyleidonlabelChangeBillDel(labelChangeBill, labelChangeBillDels,pricingRulesService,productService);
            List<Style> listStyle=( List<Style>) map.get("style");
            List<Product> listproduct=( List<Product>)map.get("product");
            String newStylesuffix=(String)map.get("newStylesuffix");
            Init init=null;
            boolean issave=true;
            //2.保存数据，标签初始化
            if(CommonUtil.isBlank(labelChangeBill.getBillNo())){
                String prefix = BillConstant.BillPrefix.labelChangeBill
                        + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
                //String billNo = this.saleOrderBillService.findMaxBillNo(prefix);
                User currentUser = (User) this.getSession().getAttribute(
                        Constant.Session.User_Session);
                String prefixTaskId = Constant.Bill.Tag_Init_Prefix
                        + CommonUtil.getDateString(new Date(), "yyMMdd");
                String taskId = this.initService.findMaxNo(prefixTaskId);
                labelChangeBill.setId(prefix);
                labelChangeBill.setBillNo(prefix);
                labelChangeBill.setBillDate(new Date());
                init = BillConvertUtil.labelcovertToTagBirth(taskId, labelChangeBillDels, initService, currentUser,prefix,newStylesuffix);

            }else{
                issave=false;
            }
            User curUser = CacheManager.getUserById(userId);
            BillConvertUtil.covertToLabelChangeBill( labelChangeBill,labelChangeBillDels,curUser);
            this.labelChangeBillService.save(labelChangeBill,labelChangeBillDels,listStyle,listproduct,issave,init);
            return new MessageBox(true, "保存成功", labelChangeBill.getBillNo());
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
}
