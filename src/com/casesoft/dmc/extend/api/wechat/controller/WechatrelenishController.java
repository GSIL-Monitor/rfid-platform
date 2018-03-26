package com.casesoft.dmc.extend.api.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.BillConvertUtil;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.search.DetailStockChatView;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.cfg.PropertyKeyService;
import com.casesoft.dmc.service.cfg.PropertyService;
import com.casesoft.dmc.service.logistics.PurchaseOrderBillService;
import com.casesoft.dmc.service.logistics.ReplenishBillDtlService;
import com.casesoft.dmc.service.logistics.ReplenishBillDtlViewsService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import com.casesoft.dmc.service.sys.impl.UserService;
import io.swagger.annotations.*;
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
    private UnitService uniService;
    @Autowired
    private PropertyKeyService propertyKeyService;


    @Override
    public String index() {
        return null;
    }

    @RequestMapping(value = "/findrelenishDelStock.do")
    @ApiOperation(value = "获取补货单")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageSize",value="分页大小(必填)",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="pageNo",value="当前页码(必填)",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="filter_LIKES_styleid",value="款号",dataType="String", paramType = "query")
    })
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
    @ApiOperation(value = "获取补货单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageSize",value="分页大小(必填)",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="pageNo",value="当前页码(必填)",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="filter_LIKES_billId",value="补货单号",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="filter_EQS_styleId",value="款号",dataType="String", paramType = "query")
    })
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
    @ApiOperation(value = "获取买手信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageSize",value="分页大小(必填)",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="pageNo",value="当前页码(必填)",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="filter_LIKES_name",value="买手名字",dataType="String", paramType = "query")

    })
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
    @ApiOperation(value = "保存采购单")
    @ApiImplicitParams({
            @ApiImplicitParam(name="purchaseBillStr",value="采购单",dataType="string", paramType = "query",example="{'origUnitId':'MIGAO','origUnitName':'米高','destId':'AUTO_WH001','billDate':'2018-03-20 00:00:00.0','payPrice':'100','payType':'','discount':'','remark':'','status':'0'}"),
            @ApiImplicitParam(name="strDtlList",value="采购单详情",dataType="String", paramType = "query",example="[{'id': '','sku': 'z6005一色S','status': '0','inStatus': '','printStatus': '0','inStockType': 'BH','inStockTypeName': '补货','styleId': 'z6005','colorId': '一色','sizeId': 'S','styleName': '上衣','colorName': '一色','sizeName': 'S','qty': '3','actPrintQty': '','inQty': '','price':'220','discount': '100','totPrice': '660','actPrice': '220','totActPrice': '660','expectTime': '2018-03-20 00:00:00.0'}]"),
            @ApiImplicitParam(name="userId",value="userId",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="replenishBillNo",value="补货单单号",dataType="String", paramType = "query")

    })
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
    @ApiOperation(value = "获取供应商信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageSize",value="分页大小(必填)",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="pageNo",value="当前页码(必填)",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="filter_EQI_type",value="类型(必须填0)",dataType="String", paramType = "query")

    })
    @ResponseBody
   public MessageBox findUnitStock(String pageSize,String pageNo){
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        //PropertyFilter filter = new PropertyFilter("filter_EQI_type", "0");
       // filters.add(filter);
        Page<Unit> page = new Page<Unit>(Integer.parseInt(pageSize));
        page.setPage(Integer.parseInt(pageSize));
        page.setPageNo(Integer.parseInt(pageNo));
        page = this.uniService.findPage(page,filters);
        return this.returnSuccessInfo("获取成功",page.getRows());
   }

    @RequestMapping(value = "/findClass1.do")
    @ApiOperation(value = "获取厂家信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageSize",value="分页大小(必填)",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="pageNo",value="当前页码(必填)",dataType="String", paramType = "query")


    })
    @ResponseBody
   public MessageBox findClass1(String pageSize,String pageNo){
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        PropertyFilter filter = new PropertyFilter("EQS_type", "C1");
        filters.add(filter);
        Page<PropertyKey> page = new Page<PropertyKey>(Integer.parseInt(pageSize));
        page.setPage(Integer.parseInt(pageSize));
        page.setPageNo(Integer.parseInt(pageNo));
        page = this.propertyKeyService.findPage(page,filters);
        return this.returnSuccessInfo("获取成功",page.getRows());


   }




}
