package com.casesoft.dmc.extend.api.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.BillConvertUtil;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.search.DetailStockChatView;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.logistics.PurchaseOrderBillService;
import com.casesoft.dmc.service.logistics.ReplenishBillDtlService;
import com.casesoft.dmc.service.logistics.ReplenishBillDtlViewsService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import com.casesoft.dmc.service.sys.impl.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/3/19.
 */
@Controller
@RequestMapping(value = "/api/wx/Wechatrelenish")
@Api(description = "微信补货单接口")
public class WechatrelenishController extends ApiBaseController {
    @Autowired
    private ReplenishBillDtlViewsService replenishBillDtlViewsService;
    @Autowired
    private ReplenishBillDtlService replenishBillDtlService;
    @Autowired
    private UserService userService;
    @Autowired
    private PurchaseOrderBillService purchaseOrderBillService;
    @Autowired
    private UnitService unitService;


    @Override
    public String index() {
        return null;
    }

    @RequestMapping(value = "/findrelenishDelStock.do")
    @ResponseBody
    public MessageBox findrelenishDelStock(String pageSize,String pageNo){
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        Page<ReplenishBillDtlViews> page = new Page<ReplenishBillDtlViews>(Integer.parseInt(pageSize));
        page.setPage(Integer.parseInt(pageSize));
        page.setPageNo(Integer.parseInt(pageNo));
        page = this.replenishBillDtlViewsService.findPage(page,filters);
        String rootPath = this.getSession().getServletContext().getRealPath("/");
        for(ReplenishBillDtlViews d : page.getRows()){
            File file =  new File(rootPath + "/product/photo/" + d.getStyleid());
            if(file.exists()){
                File[] files = file.listFiles();
                if(files.length > 0){
                    File[] photos = files[0].listFiles();
                    if(photos.length > 0){
                        d.setUrl("/product/photo/" + d.getStyleid()+"/"+files[0].getName()+"/"+photos[0].getName());
                    }
                }
            }
        }
        return this.returnSuccessInfo("获取成功",page.getRows());
    }
    @RequestMapping(value = "/findrelenishStock.do")
    @ResponseBody
    public MessageBox findrelenishStock(String pageSize,String pageNo){
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        Page<ReplenishBillDtl> page = new Page<ReplenishBillDtl>(Integer.parseInt(pageSize));
        page.setPage(Integer.parseInt(pageSize));
        page.setPageNo(Integer.parseInt(pageNo));
        page = this.replenishBillDtlService.findPage(page,filters);
        String rootPath = this.getSession().getServletContext().getRealPath("/");
        for(ReplenishBillDtl d : page.getRows()){
            File file =  new File(rootPath + "/product/photo/" + d.getStyleId());
            if(file.exists()){
                File[] files = file.listFiles();
                if(files.length > 0){
                    File[] photos = files[0].listFiles();
                    if(photos.length > 0){
                        d.setUrl("/product/photo/" + d.getStyleId()+"/"+files[0].getName()+"/"+photos[0].getName());
                    }
                }
            }
        }
        return this.returnSuccessInfo("获取成功",page.getRows());

    }
    @RequestMapping(value = "/findrelenishuserStock.do")
    @ResponseBody
    public MessageBox findrelenishuserStock(String pageSize,String pageNo){List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        PropertyFilter filter = new PropertyFilter("EQS_roleId", "BUYER");
        filters.add(filter);
        Page<User> page = new Page<User>(Integer.parseInt(pageSize));
        page.setPage(Integer.parseInt(pageSize));
        page.setPageNo(Integer.parseInt(pageNo));
        page.setPage(Integer.parseInt(pageSize));
        page.setPageNo(Integer.parseInt(pageNo));
        page = this.userService.findPage(page,filters);
        return this.returnSuccessInfo("获取成功",page.getRows());
    }
    @RequestMapping(value = "/savepurchaseBill.do")
    @ResponseBody
   public MessageBox savepurchaseBill(String purchaseBillStr, String strDtlList,String userId,String replenishBillNo){
       this.logAllRequestParams();
       PurchaseOrderBill purchaseOrderBill = JSON.parseObject(purchaseBillStr,PurchaseOrderBill.class);
       List<PurchaseOrderBillDtl> purchaseOrderBillDtlList = JSON.parseArray(strDtlList,PurchaseOrderBillDtl.class);
       /* List<ReplenishBillDtl> replenishBillDtls = JSON.parseArray(ReplenishBillDtl, ReplenishBillDtl.class);*/
        try{
           String prefix = BillConstant.BillPrefix.purchase
                   + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
           //String billNo = this.purchaseOrderBillService.findMaxBillNo(prefix);
           purchaseOrderBill.setId(prefix);
           purchaseOrderBill.setBillNo(prefix);
           User curUser = CacheManager.getUserById(userId);
           BillConvertUtil.covertToPurchaseWeChatBill(purchaseOrderBill, purchaseOrderBillDtlList,curUser);
           this.purchaseOrderBillService.saveWechat(purchaseOrderBill, purchaseOrderBillDtlList,replenishBillNo);
           System.out.println(purchaseOrderBill.getBillNo());
           return new MessageBox(true,"保存成功", purchaseOrderBill.getBillNo());

       }catch (Exception e){
           e.printStackTrace();
           return new MessageBox(false,e.getMessage());
       }
   }
    @RequestMapping(value = "/findUnitStock.do")
    @ResponseBody
   public MessageBox findUnitStock(String pageSize,String pageNo){
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        //PropertyFilter filter = new PropertyFilter("filter_EQI_type", "0");
       // filters.add(filter);
        Page<Unit> page = new Page<Unit>(Integer.parseInt(pageSize));
        page.setPage(Integer.parseInt(pageSize));
        page.setPageNo(Integer.parseInt(pageNo));
        page = this.unitService.findPage(page,filters);
        return this.returnSuccessInfo("获取成功",page.getRows());
   }



}
