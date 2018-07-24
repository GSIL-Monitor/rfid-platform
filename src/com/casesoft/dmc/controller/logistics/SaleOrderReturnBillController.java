package com.casesoft.dmc.controller.logistics;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.pad.templatemsg.WechatTemplate;
import com.casesoft.dmc.controller.stock.StockUtil;
import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.util.secret.EpcSecretUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.BillRecord;
import com.casesoft.dmc.model.logistics.SaleOrderReturnBill;
import com.casesoft.dmc.model.logistics.SaleOrderReturnBillDtl;
import com.casesoft.dmc.model.pad.Template.TemplateMsg;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.sys.Resource;
import com.casesoft.dmc.model.sys.ResourceButton;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.logistics.SaleOrderReturnBillService;
import com.casesoft.dmc.service.shop.CustomerService;
import com.casesoft.dmc.service.pad.TemplateMsgService;
import com.casesoft.dmc.service.pad.WeiXinUserService;
import com.casesoft.dmc.service.shop.CustomerService;
import com.casesoft.dmc.service.stock.EpcStockService;
import com.casesoft.dmc.service.sys.ResourceButtonService;
import com.casesoft.dmc.service.sys.impl.ResourceService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import com.casesoft.dmc.service.sys.GuestViewService;
import com.casesoft.dmc.service.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017-06-29.
 */
@RequestMapping(value = "/logistics/saleOrderReturn")
@Controller
public class SaleOrderReturnBillController extends BaseController implements ILogisticsBillController<SaleOrderReturnBill> {

    @Autowired
    private SaleOrderReturnBillService saleOrderReturnBillService;
    @Autowired
    private EpcStockService epcStockService;
    @Autowired
    private WeiXinUserService weiXinUserService;
    @Autowired
    private TemplateMsgService templateMsgService;
    @Autowired
    private GuestViewService guestViewService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ResourceButtonService resourceButtonService;
    @Autowired
    private TaskService taskService;
    private String billNo;
    private String warehId;


    /**
     * add by Anna 2018-04-20
     * 销售退货流程新增查看 原始单号＋最近销售日期＋销售周期
     *
     * @param warehId 仓库id 出库为发货仓，入库为收货仓
     * @return Messbox true ,允许操作，false允许出，入库提示msg信息
     */
    @RequestMapping(value = "/findCodeSaleReturnList")
    @ResponseBody
    public List<EpcStock> findCodeSaleReturnList(String billNo,String warehId) {
        try {
            List<String> codeList =this.saleOrderReturnBillService.codeList(billNo);
            List<EpcStock> stockList = new ArrayList<>();
            for (String code :codeList){
                EpcStock epcStock;
                List<EpcStock> epcStockList = new ArrayList<>();
                epcStockList = this.epcStockService.findSaleReturnFilterByDestIdDtl(code, warehId,1);
                if (epcStockList.size() == 0 || epcStockList.isEmpty()) {
                    epcStock = this.epcStockService.findEpcAllowInCode(code);
                } else {
                    epcStock = epcStockList.get(0);
                    Long cycle = Long.parseLong(""+CommonUtil.daysBetween(epcStock.getLastSaleTime(),new Date()));
                    epcStock.setSaleCycle(cycle);
                }
                if (CommonUtil.isNotBlank(epcStock)) {
                    StockUtil.convertEpcStock(epcStock);
                        stockList.add(epcStock);
                }else {
                    Epc tagEpc = this.epcStockService.findTagEpcByCode(code);
                    if (CommonUtil.isNotBlank(tagEpc)) {
                        epcStock = new EpcStock();
                        epcStock.setId(code);
                        epcStock.setCode(code);
                        epcStock.setSku(tagEpc.getSku());
                        epcStock.setStyleId(tagEpc.getStyleId());
                        epcStock.setColorId(tagEpc.getColorId());
                        epcStock.setSizeId(tagEpc.getSizeId());
                        epcStock.setInStock(0);
                        epcStock.setWarehouseId(warehId);
                        StockUtil.convertEpcStock(epcStock);
                        stockList.add(epcStock);
                    }
                }
            }
            return stockList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
//    @RequestMapping(value = "/index")
    public String index() {
        return "/views/logistics/saleOrderReturn";
    }

    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {
        ModelAndView mv = new ModelAndView("/views/logistics/saleOrderReturnNew");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        Unit unit = this.unitService.getunitbyId(getCurrentUser().getOwnerId());
        String defaultWarehId = unit.getDefaultWarehId();
        String defaultSaleStaffId = unit.getDefaultSaleStaffId();
        String defalutCustomerId = unit.getDefalutCustomerId();
        if(CommonUtil.isNotBlank(defalutCustomerId)&&defalutCustomerId!=null){
            Customer customer = this.customerService.load(defalutCustomerId);
            mv.addObject("defalutCustomerId", defalutCustomerId);
            mv.addObject("defalutCustomerName", customer.getName());
            mv.addObject("defalutCustomerdiscount", customer.getDiscount());
            mv.addObject("defalutCustomercustomerType", unit.getType());
            mv.addObject("defalutCustomerowingValue", customer.getOwingValue());
        }
        mv.addObject("userId", getCurrentUser().getId());
        mv.addObject("roleid", getCurrentUser().getRoleId());
        mv.addObject("defaultWarehId", defaultWarehId);


        mv.addObject("defaultSaleStaffId", defaultSaleStaffId);
        mv.addObject("ownersId", unit.getOwnerids());
        mv.addObject("pageType", "add");
        mv.addObject("ownersId", unit.getOwnerids());
        mv.addObject("userId", getCurrentUser().getId());
        return mv;
    }


    @RequestMapping(value = "/page")
    @ResponseBody
    public Page<SaleOrderReturnBill> findPage(Page<SaleOrderReturnBill> page, String userId) throws Exception {
        this.logAllRequestParams();

        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        User currentUser = CacheManager.getUserById(userId);
        String ownerId = currentUser.getOwnerId();
        String id = currentUser.getId();
        if(!id.equals("admin")){
            PropertyFilter filter = new PropertyFilter("EQS_ownerId", ownerId);
            filters.add(filter);
        }
        page.setPageProperty();
        page = this.saleOrderReturnBillService.findPage(page, filters);
        for (SaleOrderReturnBill sb : page.getRows()) {
            if (CommonUtil.isNotBlank(sb.getCustomer()))
                if (CommonUtil.isNotBlank(CacheManager.getUnitById(sb.getCustomer())))
                    sb.setCustomerName(CacheManager.getUnitById(sb.getCustomer()).getName());
            if (CommonUtil.isNotBlank(sb.getOrigId()))
                sb.setOrigName(CacheManager.getUnitByCode(sb.getOrigId()).getName());
        }
        return page;
    }


    @Override
    public Page<SaleOrderReturnBill> findPage(Page<SaleOrderReturnBill> page) throws Exception {
        return null;
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    @Override
    public List<SaleOrderReturnBill> list() throws Exception {
        this.logAllRequestParams();

        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());

        List<SaleOrderReturnBill> saleOrderReturnBillList = this.saleOrderReturnBillService.find(filters);

        return saleOrderReturnBillList;
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    @Override
    public ModelAndView add() throws Exception {
        ModelAndView mav = new ModelAndView("/views/logistics/saleOrderReturnDetail");
        Unit unit = CacheManager.getUnitByCode(getCurrentUser().getOwnerId());
        String defaultWarehId = unit.getDefaultWarehId();

        String defaultSaleStaffId = unit.getDefaultSaleStaffId();
        String defalutCustomerId = unit.getDefalutCustomerId();
        if(CommonUtil.isNotBlank(defalutCustomerId)){
            Customer customer = CacheManager.getCustomerById(defalutCustomerId);
            mav.addObject("defalutCustomerId", defalutCustomerId);
            mav.addObject("defalutCustomerName", customer.getName());
            mav.addObject("defalutCustomerdiscount", customer.getDiscount());
            mav.addObject("defalutCustomercustomerType", unit.getType());
            mav.addObject("defalutCustomerowingValue", customer.getOwingValue());
        }

        mav.addObject("ownerId", getCurrentUser().getOwnerId());
        mav.addObject("userId",getCurrentUser().getId());
        mav.addObject("defaultWarehId", defaultWarehId);

        mav.addObject("defaultSaleStaffId", defaultSaleStaffId);
        mav.addObject("roleid", getCurrentUser().getRoleId());
        mav.addObject("pageType", "add");
        mav.addObject("ownersId", unit.getOwnerids());
        mav.addObject("mainUrl", "/logistics/saleOrderReturn/index.do");
        return mav;
    }

    @RequestMapping(value = "/edit")
    @ResponseBody
    @Override
    public ModelAndView edit(String billNo) throws Exception {
        SaleOrderReturnBill saleOrderReturnBill = this.saleOrderReturnBillService.findBillByBillNo(billNo);
        Boolean isAllowEdit = false;
        if(CommonUtil.isBlank(saleOrderReturnBill.getBillType())){
            isAllowEdit=true;
            saleOrderReturnBill.setBillType(Constant.ScmConstant.BillType.Edit);
            HttpServletRequest request = this.getRequest();
            HttpSession session = request.getSession();
            session.setAttribute("billNosaleReturn",billNo);
        }else{
            if(saleOrderReturnBill.getBillType().equals(Constant.ScmConstant.BillType.Save)){
                isAllowEdit = true;
                saleOrderReturnBill.setBillType(Constant.ScmConstant.BillType.Edit);
                HttpServletRequest request = this.getRequest();
                HttpSession session = request.getSession();
                session.setAttribute("billNosaleReturn",billNo);
            }else{
                isAllowEdit = false;
            }
        }
        if(isAllowEdit){
            ModelAndView mav = new ModelAndView("/views/logistics/saleOrderReturnDetail");
            mav.addObject("ownerId", getCurrentUser().getOwnerId());
            mav.addObject("userId",getCurrentUser().getId());
            mav.addObject("saleOrderReturn", saleOrderReturnBill);
            mav.addObject("roleid", getCurrentUser().getRoleId());
            mav.addObject("pageType", "edit");
            Unit unit = CacheManager.getUnitById(getCurrentUser().getOwnerId());
            mav.addObject("ownersId", unit.getOwnerids());
            mav.addObject("mainUrl", "/logistics/saleOrderReturn/back.do?billNo="+billNo);
            return mav;
        }else{
            ModelAndView mv = new ModelAndView("/views/logistics/saleOrderReturn");
            mv.addObject("billNo",billNo);
            mv.addObject("ownerId", getCurrentUser().getOwnerId());
            mv.addObject("userId", getCurrentUser().getId());
            return  mv;

        }

    }
    @RequestMapping(value = "/back")
    @ResponseBody
    public ModelAndView back(String billNo){
        SaleOrderReturnBill saleOrderReturnBill = this.saleOrderReturnBillService.findBillByBillNo(billNo);
        saleOrderReturnBill.setBillType(Constant.ScmConstant.BillType.Save);
        HttpServletRequest request = this.getRequest();
        HttpSession session = request.getSession();
        session.removeAttribute("billNosaleReturn");
        this.saleOrderReturnBillService.save(saleOrderReturnBill);
        ModelAndView mv = new ModelAndView("/views/logistics/saleOrderReturn");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        mv.addObject("userId", getCurrentUser().getId());
        return mv;
    }
    @RequestMapping(value = "/quit")
    @ResponseBody
    public void quit(String billNo){
        SaleOrderReturnBill saleOrderReturnBill = this.saleOrderReturnBillService.findBillByBillNo(billNo);
        saleOrderReturnBill.setBillType(Constant.ScmConstant.BillType.Save);
        HttpServletRequest request = this.getRequest();
        HttpSession session = request.getSession();
        session.removeAttribute("billNosaleReturn");
        this.saleOrderReturnBillService.save(saleOrderReturnBill);
    }
    @RequestMapping(value = "/edittwo")
    @ResponseBody
    public ModelAndView edittwo(String billNo,String url) throws Exception {
        SaleOrderReturnBill saleOrderReturnBill = this.saleOrderReturnBillService.findBillByBillNo(billNo);
        ModelAndView mav = new ModelAndView("/views/logistics/saleOrderReturnDetail");
        mav.addObject("ownerId", getCurrentUser().getOwnerId());
        mav.addObject("userId",getCurrentUser().getId());
        mav.addObject("saleOrderReturn", saleOrderReturnBill);
        mav.addObject("pageType", "edit");
        mav.addObject("mainUrl", url);
        return mav;
    }

    @RequestMapping(value = {"/returnDetails","/returnDetailsWS"})
    @ResponseBody
    public List<SaleOrderReturnBillDtl> findReturnDtls(String billNo) {
        this.logAllRequestParams();
        List<SaleOrderReturnBillDtl> saleOrderReturnBillDtls = this.saleOrderReturnBillService.findDtlByBillNo(billNo);
        List<BillRecord> billRecordList = this.saleOrderReturnBillService.getBillRecod(billNo);
        Map<String,String> codeMap = new HashMap<>();
        for(BillRecord r : billRecordList){
            if(codeMap.containsKey(r.getSku())){
                String code = codeMap.get(r.getSku());
                code += ","+r.getCode();
                codeMap.put(r.getSku(),code);
            }else{
                codeMap.put(r.getSku(),r.getCode());
            }
        }
        for (SaleOrderReturnBillDtl s : saleOrderReturnBillDtls) {
            Style style = CacheManager.getStyleById(s.getStyleId());
            if(codeMap.containsKey(s.getSku())){
                s.setUniqueCodes(codeMap.get(s.getSku()));
            }
            if (CommonUtil.isNotBlank(CacheManager.getColorById(s.getColorId()))) {
                s.setColorName(CacheManager.getColorNameById(s.getColorId()));
            }
            if (CommonUtil.isNotBlank(CacheManager.getStyleById(s.getStyleId()))) {
                s.setStyleName(CacheManager.getStyleNameById(s.getStyleId()));
            }
            if (CommonUtil.isNotBlank(CacheManager.getSizeById(s.getSizeId()))) {
                s.setSizeName(CacheManager.getSizeNameById(s.getSizeId()));
            }
            Map<String,Double> stylePriceMap = new HashMap<>();
            stylePriceMap.put("price",style.getPrice());
            stylePriceMap.put("wsPrice",style.getWsPrice());
            stylePriceMap.put("puPrice",style.getPuPrice());
            s.setStylePriceMap(JSON.toJSONString(stylePriceMap));
            s.setTagPrice(CacheManager.getStyleById(s.getStyleId()).getPrice());
        }
        return saleOrderReturnBillDtls;
    }

    @RequestMapping(value = {"/save","/saveReturnWS"})
    @ResponseBody
    @Override
    public MessageBox save(String bill, String strDtlList, String userId) throws Exception {
        System.out.println("bill=" + bill);
        System.out.println("strDtlList=" + strDtlList);
        try {
            SaleOrderReturnBill saleOrderReturnBill = JSON.parseObject(bill, SaleOrderReturnBill.class);
            if(CommonUtil.isNotBlank(saleOrderReturnBill.getBillNo())){
                Integer status = this.saleOrderReturnBillService.findBillStatus(saleOrderReturnBill.getBillNo());
                if(status != Constant.ScmConstant.BillStatus.saved && !userId.equals("admin")){
                    return new MessageBox(false, "单据不是录入状态无法保存,请返回");
                }
            }
            List<SaleOrderReturnBillDtl> saleOrderReturnBillDtls = JSON.parseArray(strDtlList, SaleOrderReturnBillDtl.class);

            if (CommonUtil.isBlank(saleOrderReturnBill.getBillNo())) {
                String prefix = BillConstant.BillPrefix.SaleOrderReturn + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
                //String billNo = this.saleOrderReturnBillService.findMaxBillNO(prefix);
                saleOrderReturnBill.setBillNo(prefix);
                saleOrderReturnBill.setId(prefix);
            }
            User currentUser = CacheManager.getUserById(userId);
            saleOrderReturnBill.setId(saleOrderReturnBill.getBillNo());
            // set&get BillRecordList
            BillConvertUtil.convertToSaleOrderReturnBill(saleOrderReturnBill, saleOrderReturnBillDtls, currentUser);
            this.saleOrderReturnBillService.saveReturnBatch(saleOrderReturnBill, saleOrderReturnBillDtls);
            return returnSuccessInfo("保存成功", saleOrderReturnBill.getBillNo());
        } catch (Exception e) {
            e.printStackTrace();
            return returnFailInfo("保存失败", e.getMessage());
        }
    }

    @RequestMapping(value = "/copyAdd")
    @ResponseBody
    public ModelAndView addCopy(String billNo) {
        ModelAndView mav = new ModelAndView("/views/logistics/saleOrderReturnDetail");
        SaleOrderReturnBill bill = this.saleOrderReturnBillService.findBillByBillNo(billNo);
        mav.addObject("ownerId", getCurrentUser().getOwnerId());
        mav.addObject("userId",getCurrentUser().getId());
        mav.addObject("saleOrderReturn", bill);
        mav.addObject("pageType", "copyAdd");
        mav.addObject("mainUrl", "/logistics/saleOrderReturn/index.do");
        return mav;
    }


    @RequestMapping(value = "/check")
    @ResponseBody
    @Override
    public MessageBox check(String billNo) throws Exception {
        this.logAllRequestParams();

        SaleOrderReturnBill saleOrderReturnBill = this.saleOrderReturnBillService.findBillByBillNo(billNo);
        if (saleOrderReturnBill.getStatus() == BillConstant.BillStatus.Enter)
            saleOrderReturnBill.setStatus(BillConstant.BillStatus.Check);
        else {
            return returnFailInfo("不是录入状态，无法审核");
        }
        try {
            this.saleOrderReturnBillService.save(saleOrderReturnBill);
            return returnSuccessInfo("审核成功");
        } catch (Exception e) {
            return returnFailInfo("审核失败");
        }
    }


    @Override
    public MessageBox end(String billNo) throws Exception {
        return null;
    }

    @RequestMapping(value = "/cancel")
    @ResponseBody
    @Override
    public MessageBox cancel(String billNo) throws Exception {
        this.logAllRequestParams();
        SaleOrderReturnBill saleOrderReturnBill = this.saleOrderReturnBillService.findBillByBillNo(billNo);
        if (saleOrderReturnBill.getStatus() == BillConstant.BillStatus.Enter){
            if(CommonUtil.isNotBlank(saleOrderReturnBill.getSrcBillNo())){
                //销售退货撤销关联的对应寄存单和销售单
                if(saleOrderReturnBill.getSrcBillNo().indexOf("CM")!=-1){
                    if(saleOrderReturnBill.getActPrice().equals(0D)){
                        User currentUser = this.getCurrentUser();
                        String handleQtycancel = this.saleOrderReturnBillService.handleQtycancel(billNo, saleOrderReturnBill.getSrcBillNo(),currentUser);
                        if(CommonUtil.isNotBlank(handleQtycancel)){
                            return returnFailInfo(handleQtycancel);
                        }else{
                            saleOrderReturnBill.setStatus(BillConstant.BillStatus.Cancel);
                        }
                    }else{
                        String handleMoneycancel = this.saleOrderReturnBillService.handleMoneycancel(billNo, saleOrderReturnBill.getSrcBillNo());
                        if(CommonUtil.isNotBlank(handleMoneycancel)){
                            return returnFailInfo(handleMoneycancel);
                        }else{
                            saleOrderReturnBill.setStatus(BillConstant.BillStatus.Cancel);
                        }
                    }

                }
                if(saleOrderReturnBill.getSrcBillNo().indexOf("SO")!=-1){
                    String handleMoneycancel = this.saleOrderReturnBillService.handleMoneycancelSO(billNo, saleOrderReturnBill.getSrcBillNo());
                    if(CommonUtil.isNotBlank(handleMoneycancel)){
                        return returnFailInfo(handleMoneycancel);
                    }else{
                        saleOrderReturnBill.setStatus(BillConstant.BillStatus.Cancel);
                    }
                }
            }else{
                saleOrderReturnBill.setStatus(BillConstant.BillStatus.Cancel);
            }

        }else{
            return returnFailInfo("不是录入状态，无法取消");
        }
        try {
            this.saleOrderReturnBillService.cancelUpdate(saleOrderReturnBill);
            return returnSuccessInfo("取消成功");
        } catch (Exception e) {
            return returnFailInfo("取消失败");
        }
    }

    @Override
    public MessageBox convert(String strDtlList, String recordList) throws Exception {
        return null;
    }

    /**
     * 将前端传回的 billNo 和 EpcList 转换为出库信息，并存入数据库
     *
     * @param billNo
     * @param strEpcList 前端穿回的JSON字符串
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/convertOut")
    @ResponseBody
    public MessageBox convertOut(String billNo, String strEpcList, String strDtlList, String userId) throws Exception {
//        List<SaleOrderReturnBillDtl> saleOrderReturnBillDtlList = this.saleOrderReturnBillService.findDtlByBillNo(billNo);
        List<SaleOrderReturnBillDtl> saleOrderReturnBillDtlList = JSON.parseArray(strDtlList, SaleOrderReturnBillDtl.class);
        List<Epc> epcList = JSON.parseArray(strEpcList, Epc.class);
        User currentUser = CacheManager.getUserById(userId);
        SaleOrderReturnBill saleOrderReturnBill = this.saleOrderReturnBillService.get("billNo", billNo);
        Business business = BillConvertUtil.covertToSaleReturnOrderBusinessOut(saleOrderReturnBill, saleOrderReturnBillDtlList, epcList, currentUser);
        MessageBox messageBox = this.saleOrderReturnBillService.saveBusiness(saleOrderReturnBill, saleOrderReturnBillDtlList, business);
        if(messageBox.getSuccess()){
            return new MessageBox(true, "出库成功");
        }else{
            return messageBox;
        }

    }

    /**
     * 将前端传回的 billNo 和 EpcList 转换为入库信息，并存入数据库
     *
     * @param billNo
     * @param strEpcList 前端穿回的JSON字符串
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/convertIn","/convertInWS"})
    @ResponseBody
    public MessageBox convertIn(String billNo, String strEpcList, String strDtlList, String userId) throws Exception {
//        List<SaleOrderReturnBillDtl> saleOrderReturnBillDtlList = this.saleOrderReturnBillService.findDtlByBillNo(billNo);
        List<SaleOrderReturnBillDtl> saleOrderReturnBillDtlList = JSON.parseArray(strDtlList, SaleOrderReturnBillDtl.class);
        List<Epc> epcList = JSON.parseArray(strEpcList, Epc.class);
        //入库校验
        List<EpcStock> EpcStockList = epcStockService.findInStockByEpcList(epcList);

        if(CommonUtil.isBlank(EpcStockList)){
            User currentUser = CacheManager.getUserById(userId);
            SaleOrderReturnBill saleOrderReturnBill = this.saleOrderReturnBillService.get("billNo", billNo);
            Business business = BillConvertUtil.covertToSaleReturnOrderBusinessIn(saleOrderReturnBill, saleOrderReturnBillDtlList, epcList, currentUser);
            MessageBox messageBox = this.saleOrderReturnBillService.saveBusiness(saleOrderReturnBill, saleOrderReturnBillDtlList, business);
            if(messageBox.getSuccess()){
                String rBillNo = saleOrderReturnBill.getBillNo();
                String totQty = saleOrderReturnBill.getTotQty().toString();
                String actPrice = saleOrderReturnBill.getActPrice().toString();
                String name = currentUser.getName();
                try {
                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
                    String phone = this.guestViewService.get("id",saleOrderReturnBill.getOrigUnitId()).getTel();
                    String openId = this.weiXinUserService.getByPhone(phone).getOpenId();
                    String state = WechatTemplate.returnMsg(openId,rBillNo,totQty,actPrice);
                    if (state.equals("success")){
                        TemplateMsg templateMsg = new TemplateMsg();
                        templateMsg.setBillNo(rBillNo);
                        templateMsg.setOpenId(openId);
                        templateMsg.setActPrice(actPrice);
                        templateMsg.setTotQty(totQty);
                        templateMsg.setTime(simpleDateFormat.format(date));
                        templateMsg.setType(1);
                        templateMsg.setName(name);
                        templateMsgService.save(templateMsg);
                    }
                }catch (Exception e){
                    this.logger.error(e.getMessage());
                    return new MessageBox(true, "入库成功");
                }
                return new MessageBox(true, "入库成功");
            }else{
                return messageBox ;
            }

        }else {
            StringBuilder sb = new StringBuilder();
            for (EpcStock epcStock: EpcStockList) {
                String wareHouseName = CacheManager.getUnitById(epcStock.getWarehouseId()).getName();
                sb.append(epcStock.getSku()).append(" ").append(epcStock.getCode()).append(" 已在仓库 [").append(epcStock.getWarehouseId()).append("]").append(wareHouseName).append(" 中<br>");
            }
            sb.append("不能退货入库！");
            return new MessageBox(false, sb.toString());
        }
    }
    @RequestMapping(value = "/findsaleReturn")
    @ResponseBody
    public ModelAndView findsaleReturn(String billNo,String url){
        SaleOrderReturnBill saleOrderReturnBill = this.saleOrderReturnBillService.findBillByBillNo(billNo);
        ModelAndView mav = new ModelAndView("/views/logistics/saleOrderReturnDetail");
        mav.addObject("ownerId", getCurrentUser().getOwnerId());
        mav.addObject("userId",getCurrentUser().getId());
        mav.addObject("saleOrderReturn", saleOrderReturnBill);
        mav.addObject("roleid", getCurrentUser().getRoleId());
        mav.addObject("pageType", "edit");
        Unit unit = CacheManager.getUnitById(getCurrentUser().getOwnerId());
        mav.addObject("ownersId", unit.getOwnerids());
        mav.addObject("mainUrl", url);
        return mav;
    }
    @RequestMapping(value = "/findResourceButton")
    @ResponseBody
    public MessageBox findResourceButton(){
        try {
            Resource resource = this.resourceService.get("url", "logistics/saleOrderReturn");
            List<ResourceButton> resourceButton = this.resourceButtonService.findResourceButtonByCodeAndRoleId(resource.getCode(), this.getCurrentUser().getRoleId(),"button");
            return new MessageBox(true, "查询成功",resourceButton);
        }catch (Exception e){
            e.printStackTrace();
            return new MessageBox(true, "查询失败");
        }
    }

}
