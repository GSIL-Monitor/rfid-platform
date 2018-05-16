package com.casesoft.dmc.extend.api.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.BillConvertUtil;
import com.casesoft.dmc.controller.product.StyleUtil;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.dao.logistics.ChangeReplenishBillDtlDao;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.cfg.PropertyKeyService;
import com.casesoft.dmc.service.logistics.PurchaseOrderBillService;
import com.casesoft.dmc.service.logistics.ReplenishBillDtlService;
import com.casesoft.dmc.service.logistics.ReplenishBillDtlViewsService;
import com.casesoft.dmc.service.logistics.ReplenishBillService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import com.casesoft.dmc.service.sys.impl.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.*;

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
    @Autowired
    private ReplenishBillService replenishBillService;
    @Autowired
    private ChangeReplenishBillDtlDao changeReplenishBillDtlDao;

    @Override
    public String index() {
        return null;
    }

    @RequestMapping(value = "/findrelenishDelStock.do")
    @ApiOperation(value = "获取补货单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "分页大小(必填)", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pageNo", value = "当前页码(必填)", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "filter_LIKES_styleid", value = "款号", dataType = "String", paramType = "query")
    })
    @ResponseBody
    public MessageBox findrelenishDelStock(String pageSize, String pageNo) {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        Page<ReplenishBillDtlViews> page = new Page<ReplenishBillDtlViews>(Integer.parseInt(pageSize));
        page.setPage(Integer.parseInt(pageSize));
        page.setPageNo(Integer.parseInt(pageNo));
        page.setSort("billdate");
        page.setOrder("desc");
        page = this.replenishBillDtlViewsService.findPage(page, filters);
        String rootPath = this.getSession().getServletContext().getRealPath("/");
        for (ReplenishBillDtlViews d : page.getRows()) {
            File file = new File(rootPath + "/product/photo/" + d.getStyleid());
            if (file.exists()) {
                File[] files = file.listFiles();
                if (files.length > 0) {
                    File[] photos = files[0].listFiles();
                    if (photos.length > 0) {
                        d.setUrl("/product/photo/" + d.getStyleid() + "/" + files[0].getName() + "/" + photos[0].getName());
                    }
                }
            }
        }
        return this.returnSuccessInfo("获取成功", page.getRows());
    }

    @RequestMapping(value = "/findrelenishStock.do")
    @ApiOperation(value = "获取补货单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "分页大小(必填)", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pageNo", value = "当前页码(必填)", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "filter_LIKES_billId", value = "补货单号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "filter_EQS_styleId", value = "款号", dataType = "String", paramType = "query")
    })
    @ResponseBody
    public MessageBox findrelenishStock(String pageSize, String pageNo) {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        Page<ReplenishBillDtl> page = new Page<ReplenishBillDtl>(Integer.parseInt(pageSize));
        page.setPage(Integer.parseInt(pageSize));
        page.setPageNo(Integer.parseInt(pageNo));
        page = this.replenishBillDtlService.findPage(page, filters);
        String rootPath = this.getSession().getServletContext().getRealPath("/");
        for (ReplenishBillDtl d : page.getRows()) {
            File file = new File(rootPath + "/product/photo/" + d.getStyleId());
            if (file.exists()) {
                File[] files = file.listFiles();
                if (files.length > 0) {
                    File[] photos = files[0].listFiles();
                    if (photos.length > 0) {
                        d.setUrl("/product/photo/" + d.getStyleId() + "/" + files[0].getName() + "/" + photos[0].getName());
                    }
                }
            }
        }
        return this.returnSuccessInfo("获取成功", page.getRows());

    }

    @RequestMapping(value = "/findrelenishuserStock.do")
    @ApiOperation(value = "获取买手信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "分页大小(必填)", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pageNo", value = "当前页码(必填)", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "filter_LIKES_name", value = "买手名字", dataType = "String", paramType = "query")

    })
    @ResponseBody
    public MessageBox findrelenishuserStock(String pageSize, String pageNo) {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        PropertyFilter filter = new PropertyFilter("EQS_roleId", "BUYER");
        filters.add(filter);
        Page<User> page = new Page<User>(Integer.parseInt(pageSize));
        page.setPage(Integer.parseInt(pageSize));
        page.setPageNo(Integer.parseInt(pageNo));
        page.setPage(Integer.parseInt(pageSize));
        page.setPageNo(Integer.parseInt(pageNo));
        page = this.userService.findPage(page, filters);
        return this.returnSuccessInfo("获取成功", page.getRows());
    }

//    @RequestMapping(value = "/savepurchaseBill.do")
//    @ApiOperation(value = "保存采购单")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "purchaseBillStr", value = "采购单", dataType = "string", paramType = "query", example = "{'origUnitId':'MIGAO','origUnitName':'米高','destId':'AUTO_WH001','billDate':'2018-03-20 00:00:00.0','payPrice':'100','payType':'','discount':'','remark':'','status':'0'}"),
//            @ApiImplicitParam(name = "strDtlList", value = "采购单详情", dataType = "String", paramType = "query", example = "[{'id': '','sku': 'z6005一色S','status': '0','inStatus': '','printStatus': '0','inStockType': 'BH','inStockTypeName': '补货','styleId': 'z6005','colorId': '一色','sizeId': 'S','styleName': '上衣','colorName': '一色','sizeName': 'S','qty': '3','actPrintQty': '','inQty': '','price':'220','discount': '100','totPrice': '660','actPrice': '220','totActPrice': '660','expectTime': '2018-03-20 00:00:00.0'}]"),
//            @ApiImplicitParam(name = "userId", value = "userId", dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "replenishBillNo", value = "补货单单号", dataType = "String", paramType = "query")
//
//    })
//    @ResponseBody
//    public MessageBox savepurchaseBill(String purchaseBillStr, String strDtlList, String userId, String replenishBillNo) {
//        this.logAllRequestParams();
//        PurchaseOrderBill purchaseOrderBill = JSON.parseObject(purchaseBillStr, PurchaseOrderBill.class);
//        List<PurchaseOrderBillDtl> purchaseOrderBillDtlList = JSON.parseArray(strDtlList, PurchaseOrderBillDtl.class);
//        /* List<ReplenishBillDtl> replenishBillDtls = JSON.parseArray(ReplenishBillDtl, ReplenishBillDtl.class);*/
//        try {
//            //筛选明细中的数据（排除明细中已经完成的）
//            purchaseOrderBillDtlList=this.purchaseOrderBillService.filtrateMessage(purchaseOrderBillDtlList,replenishBillNo);
//            if(purchaseOrderBillDtlList.size()>0){
//                String prefix = BillConstant.BillPrefix.purchase
//                        + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
//                //String billNo = this.purchaseOrderBillService.findMaxBillNo(prefix);
//                purchaseOrderBill.setId(prefix);
//                purchaseOrderBill.setBillNo(prefix);
//                User curUser = CacheManager.getUserById(userId);
//                BillConvertUtil.covertToPurchaseWeChatBill(purchaseOrderBill, purchaseOrderBillDtlList, curUser);
//                this.purchaseOrderBillService.saveWechat(purchaseOrderBill, purchaseOrderBillDtlList, replenishBillNo, curUser);
//                System.out.println(purchaseOrderBill.getBillNo());
//                return new MessageBox(true, "保存成功", purchaseOrderBill.getBillNo());
//            }else{
//                return new MessageBox(true, "保存成功");
//            }
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new MessageBox(false, e.getMessage());
//        }
//    }

    /**
     * add by yushen 处理补货单，生成采购单，更新补货单状态
     */
    @RequestMapping(value = "/processReplenishBill.do")
    @ApiOperation(value = "处理补货单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "purchaseBillStr", value = "采购单", dataType = "string", paramType = "query", example = "{'origUnitId':'MIGAO','origUnitName':'米高','destId':'AUTO_WH001','billDate':'2018-03-20 00:00:00.0','payPrice':'100','payType':'','discount':'','remark':'','status':'0'}"),
            @ApiImplicitParam(name = "strDtlList", value = "采购单详情", dataType = "String", paramType = "query", example = "[{'id': '','sku': 'z6005一色S','status': '0','inStatus': '','printStatus': '0','inStockType': 'BH','inStockTypeName': '补货','styleId': 'z6005','colorId': '一色','sizeId': 'S','styleName': '上衣','colorName': '一色','sizeName': 'S','qty': '3','actPrintQty': '','inQty': '','price':'220','discount': '100','totPrice': '660','actPrice': '220','totActPrice': '660','expectTime': '2018-03-20 00:00:00.0'}]"),
            @ApiImplicitParam(name = "userId", value = "userId", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "replenishBillNo", value = "补货单单号", dataType = "String", paramType = "query")

    })
    @ResponseBody
    public MessageBox processReplenishBill(String purchaseBillStr, String strDtlList, String userId, String replenishBillNo) throws Exception{
        this.logAllRequestParams();
        PurchaseOrderBill purchaseOrderBill = JSON.parseObject(purchaseBillStr, PurchaseOrderBill.class);
        List<PurchaseOrderBillDtl> purchaseOrderBillDtlList = JSON.parseArray(strDtlList, PurchaseOrderBillDtl.class);
        List<PurchaseOrderBillDtl> filteredDtlList = new ArrayList<>();  //存放转换数量大于零的明细
        ReplenishBill replenishBill = replenishBillService.get("id", replenishBillNo);
        List<ReplenishBillDtl> replenishBillDtlList = this.replenishBillService.findBillDtl(replenishBillNo);

        Map<String, ReplenishBillDtl> sku2billDtlMap = new HashMap<>();
        for (ReplenishBillDtl rDtl : replenishBillDtlList) {
            sku2billDtlMap.put(rDtl.getSku(), rDtl);
        }

        //将转换数量放入补货单明细，并过滤出转换数量大于零的采购明细
        Long totConvertQty = 0L;
        for (PurchaseOrderBillDtl pDtl : purchaseOrderBillDtlList) {
            Long convertQty = pDtl.getQty();
            if(CommonUtil.isBlank(convertQty)){
                convertQty = 0L;
            }
            totConvertQty += convertQty;
            String thisSku = pDtl.getSku();
            ReplenishBillDtl replenishBillDtl = sku2billDtlMap.get(thisSku);
            if (convertQty > 0) {
                replenishBillDtl.setConvertQty(convertQty.intValue());
                replenishBillDtl.setLastTime(pDtl.getExpectTime());
                //add by yushen 多次备注拼接，存入补货申请单
                String remark = "处理日期：" + CommonUtil.getDateString(new Date(), "yyyy-MM-dd")
                        + "，预计到货日期：" + CommonUtil.getDateString(pDtl.getExpectTime(),"yyyy-MM-dd")
                        + "，处理数量：" + pDtl.getQty()
                        + "，备注说明：" + pDtl.getRemark() + "；<br>";
                String oldRemark = replenishBillDtl.getRemark();
                if(CommonUtil.isBlank(oldRemark)){
                    oldRemark="";
                }
                replenishBillDtl.setRemark(oldRemark + remark);
                filteredDtlList.add(pDtl);
            }
        }
        //总转换数量小于1，返回错误：没有待处理的商品
        if (totConvertQty < 1) {
            throw new Exception("没有待处理的商品");
//            return new MessageBox(false, "没有待处理的商品");
        }

        if (filteredDtlList.size() > 0) {
            String prefix = BillConstant.BillPrefix.purchase
                    + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
            purchaseOrderBill.setId(prefix);
            purchaseOrderBill.setBillNo(prefix);
            //补货单的买手和仓库信息传递给采购单
            User buyer = CacheManager.getUserById(replenishBill.getBuyahandId());
            if(CommonUtil.isNotBlank(buyer)){
                purchaseOrderBill.setBuyahandId(buyer.getId());
                purchaseOrderBill.setBuyahandName(buyer.getName());
            }
            Unit warehouse = CacheManager.getUnitByCode(replenishBill.getOwnerId());
            if(CommonUtil.isNotBlank(warehouse)){
                purchaseOrderBill.setOrderWarehouseId(warehouse.getId());
                purchaseOrderBill.setOrderWarehouseName(warehouse.getName());
            }
            User curUser = CacheManager.getUserById(userId);
            BillConvertUtil.covertToPurchaseWeChatBill(purchaseOrderBill, filteredDtlList, curUser);
            BillConvertUtil.convertReplenishInProcessing(replenishBill, replenishBillDtlList);
            this.purchaseOrderBillService.processReplenishBill(purchaseOrderBill, filteredDtlList, replenishBill, replenishBillDtlList, curUser);
            return new MessageBox(true, "保存成功", purchaseOrderBill.getBillNo());
        }else{
            return new MessageBox(true, "保存成功");
        }
    }




    @RequestMapping(value = "/findUnitStock.do")
    @ApiOperation(value = "获取供应商信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "分页大小(必填)", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pageNo", value = "当前页码(必填)", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "filter_EQI_type", value = "类型(必须填0)", dataType = "String", paramType = "query")

    })
    @ResponseBody
    public MessageBox findUnitStock(String pageSize, String pageNo) {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        //PropertyFilter filter = new PropertyFilter("filter_EQI_type", "0");
        // filters.add(filter);
        Page<Unit> page = new Page<Unit>(Integer.parseInt(pageSize));
        page.setPage(Integer.parseInt(pageSize));
        page.setPageNo(Integer.parseInt(pageNo));
        page = this.uniService.findPage(page, filters);
        return this.returnSuccessInfo("获取成功", page.getRows());
    }

    @RequestMapping(value = "/findClass1.do")
    @ApiOperation(value = "获取厂家信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "分页大小(必填)", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pageNo", value = "当前页码(必填)", dataType = "String", paramType = "query")


    })
    @ResponseBody
    public MessageBox findClass1(String pageSize, String pageNo) {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        PropertyFilter filter = new PropertyFilter("EQS_type", "C1");
        filters.add(filter);
        Page<PropertyKey> page = new Page<PropertyKey>(Integer.parseInt(pageSize));
        page.setPage(Integer.parseInt(pageSize));
        page.setPageNo(Integer.parseInt(pageNo));
        page = this.propertyKeyService.findPage(page, filters);
        return this.returnSuccessInfo("获取成功", page.getRows());


    }

    /**
     * add by Yushen
     * 销售员下补货单后，查看补货单列表的接口。
     *
     * @param sortOrder 排序字段 按日期降序 billDate_desc; 按日期升序 billDate_asc;
     */
    @RequestMapping(value = "/findReplenishOrderList.do")
    @ResponseBody
    public MessageBox findReplenishOrderList(String pageSize, String pageNo, String userId, String sortOrder) {

        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        //权限设置，增加过滤条件，只显示当前ownerId下的销售单信息
        User CurrentUser = CacheManager.getUserById(userId);
        String ownerId = CurrentUser.getOwnerId();
        //只要是管理员权限的账号就可以看所有单据
//        String id = CurrentUser.getId();
        if (!ownerId.equals("1")) {
            PropertyFilter filter = new PropertyFilter("EQS_ownerId", ownerId);
            filters.add(filter);
        }
        Page<ReplenishBill> page = new Page<ReplenishBill>();
        page.setPageSize(Integer.parseInt(pageSize));
        page.setPageNo(Integer.parseInt(pageNo));
        page.setPage(Integer.parseInt(pageNo));
        String sort = "billDate";
        String order = "desc";
        if (CommonUtil.isNotBlank(sortOrder)) {
            String[] split = sortOrder.split("_");
            sort = split[0];
            order = split[1];
        }
        page.setSort(sort);
        page.setOrder(order);
        page.setPageProperty();
        page = this.replenishBillService.findPage(page, filters);
        if (CommonUtil.isNotBlank(page.getRows())) {
            for (ReplenishBill replenishBill : page.getRows()) {
                User user = CacheManager.getUserById(replenishBill.getBuyahandId());
                if (CommonUtil.isNotBlank(user)) {
                    replenishBill.setBuyahandName(user.getName());
                }
            }
        }
        return new MessageBox(true, "success", page);
    }

    /**
     * 撤销未处理的补货单的接口。
     */
    @RequestMapping(value = "/cancelReplenishOrderList.do")
    @ResponseBody
    public MessageBox cancelReplenishOrderList(String billNo) throws Exception {
        ReplenishBill replenishBill = this.replenishBillService.get("billNo", billNo);
        replenishBill.setStatus(BillConstant.BillStatus.Cancel);
        this.replenishBillService.cancelUpdate(replenishBill);
        return new MessageBox(true, "撤销成功");

    }

    /**
     * add by yushen
     * 销售员下补货单后，查看补货单明细的接口。
     */
    @RequestMapping(value = "/findReplenishOrderDtl.do")
    @ResponseBody
    public MessageBox findReplenishOrderDtl(String billNo) {

        this.logAllRequestParams();
        ReplenishBill replenishBill = this.replenishBillService.load(billNo);
        User orderUser = CacheManager.getUserById(replenishBill.getBuyahandId());
        if (CommonUtil.isNotBlank(orderUser)) {
            replenishBill.setBuyahandName(orderUser.getName());
        }
        List<ReplenishBillDtl> replenishBillDtlList = this.replenishBillService.findBillDtl(billNo);
        for (ReplenishBillDtl replenishBillDtl : replenishBillDtlList) {
            User orderDtlUser = CacheManager.getUserById(replenishBillDtl.getBuyahandId());
            if (CommonUtil.isNotBlank(orderDtlUser)) {
                replenishBillDtl.setBuyahandName(orderDtlUser.getName());
            }
            String rootPath = this.getSession().getServletContext().getRealPath("/");
            String imgUrl = StyleUtil.returnImageUrl(replenishBillDtl.getStyleId(), rootPath);
            replenishBillDtl.setUrl(imgUrl);
        }
        replenishBill.setDtlList(replenishBillDtlList);
        return new MessageBox(true, "success", replenishBill);
    }

    /**
     * add by yushen
     * 买手处理完补货后，查看补单处理记录。
     */
    @RequestMapping(value = "/findReplenishHandleRecord.do")
    @ResponseBody
    public MessageBox findReplenishHandleRecord(String pageSize, String pageNo, String userId) {
        this.logAllRequestParams();

        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        //权限设置，增加过滤条件，只显示自己转的采购单
        if (CommonUtil.isNotBlank(userId)) {
            PropertyFilter filter = new PropertyFilter("EQS_userId", userId);
            filters.add(filter);
        }

        Page<ChangeReplenishBillDtl> page = new Page<ChangeReplenishBillDtl>();
        page.setPageSize(Integer.parseInt(pageSize));
        page.setPageNo(Integer.parseInt(pageNo));
        page.setPage(Integer.parseInt(pageNo));
        page.setPageProperty();
        Page<ChangeReplenishBillDtl> recordPage = this.changeReplenishBillDtlDao.findPage(page, filters);
        if (CommonUtil.isNotBlank(recordPage.getRows())) {
            Map<String, ChangeReplenishBillDtl> billNo2DtlMap = new HashMap<>();
            for (ChangeReplenishBillDtl dtl : recordPage.getRows()) {
                Product product = CacheManager.getProductByCode(dtl.getSku());
                dtl.setStyleId(product.getStyleId());
                dtl.setStyleName(product.getStyleName());
                billNo2DtlMap.put(dtl.getPurchaseNo(), dtl);
            }
            List<ChangeReplenishBillDtl> uniqueBillNoList = new ArrayList<>(billNo2DtlMap.values());
            // FIXME: 2018/4/2 采购模块优化之后，改为销售单List
            Collections.sort(uniqueBillNoList, new Comparator<ChangeReplenishBillDtl>() {
                @Override
                public int compare(ChangeReplenishBillDtl o1, ChangeReplenishBillDtl o2) {
                    return (int) (o2.getBillDate().getTime() - o1.getBillDate().getTime());
                }
            });
            recordPage.setRows(uniqueBillNoList);
        }
        return new MessageBox(true, "success", recordPage);
    }

    /**
     * add by yushen
     * 查看所有采购单据
     */
    @RequestMapping(value = "/findPurchaseOrderList.do")
    @ResponseBody
    public MessageBox findPurchaseOrderList(String pageSize, String pageNo, String userId) {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        if (CommonUtil.isNotBlank(userId)) {
            PropertyFilter filter = new PropertyFilter("EQS_userId", userId);
            filters.add(filter);
        }
        Page<PurchaseOrderBill> page = new Page<PurchaseOrderBill>();
        page.setPageSize(Integer.parseInt(pageSize));
        page.setPageNo(Integer.parseInt(pageNo));
        page.setPage(Integer.parseInt(pageNo));
        page.setSort("billDate");
        page.setOrder("desc");
        page.setPageProperty();
        Page<PurchaseOrderBill> purchaseOrderBillPage = this.purchaseOrderBillService.findPage(page, filters);
        for (PurchaseOrderBill bill : purchaseOrderBillPage.getRows()) {
            List<PurchaseOrderBillDtl> billDtlList = this.purchaseOrderBillService.findBillDtlByBillNo(bill.getBillNo());
            bill.setDtlList(billDtlList);
        }

        return new MessageBox(true, "success", purchaseOrderBillPage);
    }

    /**
     * add by yushen
     * 买手处理完补货后，查看补单转换的采购单
     */
    @RequestMapping(value = "/getPurchaseBillDtl.do")
    @ResponseBody
    public MessageBox getPurchaseBillDtl(String purchaseNo) {
        this.logAllRequestParams();
        PurchaseOrderBill purchaseOrderBill = this.purchaseOrderBillService.load(purchaseNo);
        User buyer = CacheManager.getUserById(purchaseOrderBill.getBuyahandId());
        if(CommonUtil.isNotBlank(buyer)){
            purchaseOrderBill.setBuyahandName(buyer.getName());
        }
        List<PurchaseOrderBillDtl> billDtls = this.purchaseOrderBillService.findBillDtlByBillNo(purchaseNo);
        String rootPath = this.getSession().getServletContext().getRealPath("/");
        for (PurchaseOrderBillDtl dtl : billDtls) {
            String imgUrl = StyleUtil.returnImageUrl(dtl.getStyleId(), rootPath);
            dtl.setImgUrl(imgUrl);
            Style style = CacheManager.getStyleById(dtl.getStyleId());
            if(CommonUtil.isNotBlank(style)){
                dtl.setStyleName(style.getStyleName());
            }
        }
        purchaseOrderBill.setDtlList(billDtls);
        return new MessageBox(true, "success", purchaseOrderBill);
    }

    /**
     * add by Anna
     * @param sbillDate 开始时间
     * @param ebillDate 结束时间
     * 进销存信息内查看采购量-详细
     */
    @RequestMapping(value = "/findPurchaseByStyleId.do")
    @ResponseBody
    public Map<String, Object> findPurchaseByStyleId(String styleId,String sbillDate,String ebillDate) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        List<PurchaseBystyleid> purchaseBystyleids = this.purchaseOrderBillService.findBillDtlByStyleId(styleId,sbillDate,ebillDate);
        map.put("purchaseBystyleids", purchaseBystyleids);
        return map;
    }

    /**
     * add by Anna
     * @param sbillDate 开始时间
     * @param ebillDate 结束时间
     * 进销存信息内查看采购量－总采购量
     */
    @RequestMapping(value = "/findPurchaseTotal.do")
    @ResponseBody
    public Map<String, Object> findPurchaseTotal(String styleId,String sbillDate,String ebillDate) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        List<PurchaseBystyleid> purchaseBystyleids = this.purchaseOrderBillService.findPurchaseTotByStyleId(styleId,sbillDate,ebillDate);
        map.put("purchaseBystyleids", purchaseBystyleids);
        return map;
    }
}
