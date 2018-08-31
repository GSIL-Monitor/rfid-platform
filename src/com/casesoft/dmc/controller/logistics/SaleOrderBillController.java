
package com.casesoft.dmc.controller.logistics;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.pad.templatemsg.WechatTemplate;
import com.casesoft.dmc.controller.product.StyleUtil;
import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.json.FastJSONUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.pad.Template.TemplateMsg;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.sys.ResourcePrivilege;
import com.casesoft.dmc.model.sys.Setting;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.service.logistics.SaleOrderBillService;
import com.casesoft.dmc.service.pad.TemplateMsgService;
import com.casesoft.dmc.service.pad.WeiXinUserService;
import com.casesoft.dmc.service.shop.CustomerService;
import com.casesoft.dmc.service.stock.InventoryService;
import com.casesoft.dmc.service.sys.GuestViewService;
import com.casesoft.dmc.service.sys.ResourcePrivilegeService;
import com.casesoft.dmc.service.sys.SettingService;
import com.casesoft.dmc.service.sys.impl.ResourceService;
import com.casesoft.dmc.service.sys.impl.UnitService;
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
 * Created by koudepei on 2017/6/18.
 */
@Controller
@RequestMapping("/logistics/saleOrderBill")
public class SaleOrderBillController extends BaseController implements ILogisticsBillController<SaleOrderBill> {

    @Autowired
    private SaleOrderBillService saleOrderBillService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private  CustomerService customerService;
    @Autowired
    private WeiXinUserService weiXinUserService;
    @Autowired
    private TemplateMsgService templateMsgService;
    @Autowired
    private GuestViewService guestViewService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ResourcePrivilegeService resourcePrivilegeService;
    @Autowired
    private SettingService settingService;

    @Override
//    @RequestMapping(value = "/index")
    public String index() {
        return "/views/logistics/saleOrderBill";
    }

    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {
        ModelAndView mv = new ModelAndView("/views/logistics/saleOrderBill");
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
        List<ResourcePrivilege> resourcePrivilege = this.resourcePrivilegeService.findPrivilege("logistics/saleOrderBill", this.getCurrentUser().getRoleId());
        mv.addObject("resourcePrivilege", FastJSONUtil.getJSONString(resourcePrivilege));
        mv.addObject("userId", getCurrentUser().getId());
        mv.addObject("roleid", getCurrentUser().getRoleId());
        mv.addObject("defaultWarehId", defaultWarehId);
        mv.addObject("defaultSaleStaffId", defaultSaleStaffId);
        mv.addObject("ownersId", unit.getOwnerids());
        mv.addObject("pageType", "add");
        mv.addObject("ownersId", unit.getOwnerids());
        mv.addObject("userId", getCurrentUser().getId());
        Setting setting = this.settingService.get("id", "isUserAbnormal");
        mv.addObject("isUserAbnormal", setting.getValue());
        return mv;
    }

    @RequestMapping(value = {"/page","/pageWS"})
    @ResponseBody
    public Page<SaleOrderBill> findPage(Page<SaleOrderBill> page, String userId) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        //权限设置，增加过滤条件，只显示当前ownerId下的销售单信息
        User CurrentUser = CacheManager.getUserById(userId);
        String ownerId = CurrentUser.getOwnerId();
        String id = CurrentUser.getId();
        if (!id.equals("admin")) {
            PropertyFilter filter = new PropertyFilter("EQS_ownerId", ownerId);
            filters.add(filter);
        }
        page.setPageProperty();
        page = this.saleOrderBillService.findPage(page, filters);
        return page;
    }

    /**
     * add by Anna on 2018-08-28
     * 小程序调用，获取销售单，带图片的信息
     * @param pageSize
     * @param pageNo
     * @param userId
     * @param sortOrder
     * @return
     */
    @RequestMapping(value = "/findSaleOrderListWS")
    @ResponseBody
    public MessageBox findSaleOrderListWS(String pageSize, String pageNo, String userId, String sortOrder){
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        //权限设置，增加过滤条件，只显示当前ownerId下的销售单信息
        User CurrentUser = CacheManager.getUserById(userId);
        String ownerId = CurrentUser.getOwnerId();
        //只要是管理员权限的账号就可以看所有单据
//        String id = CurrentUser.getId();
        if (!ownerId.equals("1")) {
            PropertyFilter filter = new PropertyFilter("EQS_ownerId", ownerId);
            filters.add(filter);
        }
        Page<SaleOrderBill> page = new Page<SaleOrderBill>();
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
        page = this.saleOrderBillService.findPage(page, filters);
        String rootPath = this.getSession().getServletContext().getRealPath("/");
        if (CommonUtil.isNotBlank(page.getRows())) {
            for (SaleOrderBill saleOrderBill : page.getRows()) {
                List<SaleOrderBillDtl> dtlList = this.saleOrderBillService.findBillDtlByBillNo(saleOrderBill.getBillNo());
                for (SaleOrderBillDtl saleOrderBillDtl : dtlList) {
                    String imgUrl = StyleUtil.returnImageUrl(saleOrderBillDtl.getStyleId(), rootPath);
                    saleOrderBillDtl.setImgUrl(imgUrl);
                    Style style = CacheManager.getStyleById(saleOrderBillDtl.getStyleId());
                    if (CommonUtil.isNotBlank(style)) {
                        saleOrderBillDtl.setStyleName(style.getStyleName());
                    }
                }
                saleOrderBill.setDtlList(dtlList);
            }
        }
        return new MessageBox(true, "success", page);
    }

    @Override
    public Page<SaleOrderBill> findPage(Page<SaleOrderBill> page) throws Exception {
        return null;
    }

    @Override
    public List<SaleOrderBill> list() throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        List<SaleOrderBill> saleOrderBillList = this.saleOrderBillService.find(filters);
        return saleOrderBillList;
    }

    @RequestMapping(value = "/findBill")
    @ResponseBody
    public List<SaleOrderBill> findBill(String billNo){
        List<SaleOrderBill> saleOrderBills = this.saleOrderBillService.find(billNo);
        return saleOrderBills;
    }

    @RequestMapping(value = "/copyAdd")
    @ResponseBody
    public ModelAndView copyAdd(String billNo) throws Exception {
        SaleOrderBill saleOrderBill = this.saleOrderBillService.get("billNo", billNo);
        ModelAndView mv = new ModelAndView("/views/logistics/saleOrderBillDetail");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        mv.addObject("userId", getCurrentUser().getId());
        mv.addObject("pageType", "add");
        mv.addObject("saleOrderBill", saleOrderBill);
        mv.addObject("mainUrl", "/logistics/saleOrder/index.do");
        return mv;
    }

    @RequestMapping(value = "/check")
    @ResponseBody
    @Override
    public MessageBox check(String billNo) throws Exception {

        SaleOrderBill saleOrderBill = this.saleOrderBillService.get("billNo", billNo);
        saleOrderBill.setStatus(BillConstant.BillStatus.Check);
        this.saleOrderBillService.update(saleOrderBill);
        return new MessageBox(true, "审核成功");

    }

    @RequestMapping(value = "/end")
    @ResponseBody
    @Override
    public MessageBox end(String billNo) throws Exception {
        SaleOrderBill saleOrderBill = this.saleOrderBillService.get("billNo", billNo);
        saleOrderBill.setStatus(BillConstant.BillStatus.End);
        this.saleOrderBillService.update(saleOrderBill);
        return new MessageBox(true, "结束成功");
    }

    @RequestMapping(value = {"/cancel","/cancelWS"})
    @ResponseBody
    @Override
    public MessageBox cancel(String billNo) throws Exception {
        SaleOrderBill saleOrderBill = this.saleOrderBillService.get("billNo", billNo);
        try {
            if (saleOrderBill.getStatus() == BillConstant.BillStatus.Enter) {
                saleOrderBill.setStatus(BillConstant.BillStatus.Cancel);
                this.saleOrderBillService.cancelUpdate(saleOrderBill);
                return new MessageBox(true, "撤销成功");
            } else {
                return returnFailInfo("不是录入状态，无法撤销");
            }
        }catch (Exception e) {
            return returnFailInfo("撤销失败");
        }
    }

    @RequestMapping(value = "/apply")
    @ResponseBody
    public MessageBox apply(String billNo) throws Exception {
        SaleOrderBill saleOrderBill = this.saleOrderBillService.get("billNo", billNo);
        saleOrderBill.setStatus(BillConstant.BillStatus.Apply);
        this.saleOrderBillService.update(saleOrderBill);
        return new MessageBox(true, "申请成功");
    }

    @RequestMapping(value = {"/findBillDtl","/findBillDtlWS"})
    @ResponseBody
    public List<SaleOrderBillDtl> findBillDtl(String billNo) throws Exception {
        this.logAllRequestParams();
        List<SaleOrderBillDtl> saleOrderBillDtls = this.saleOrderBillService.findBillDtlByBillNo(billNo);
        List<BillRecord> billRecordList = this.saleOrderBillService.getBillRecod(billNo);
        List<AbnormalCodeMessage> abnormalCodeMessageByBillNo = this.saleOrderBillService.findAbnormalCodeMessageByBillNo(billNo);
        Map<String, String> codeMap = new HashMap<>();
        Map<String, String> abnormalCodeMap = new HashMap<>();
        for (BillRecord r : billRecordList) {
            if (codeMap.containsKey(r.getSku())) {
                String code = codeMap.get(r.getSku());
                code += "," + r.getCode();
                codeMap.put(r.getSku(), code);
            } else {
                codeMap.put(r.getSku(), r.getCode());
            }
        }
        for (AbnormalCodeMessage a : abnormalCodeMessageByBillNo) {
            if (abnormalCodeMap.containsKey(a.getSku())) {
                String code = abnormalCodeMap.get(a.getSku());
                code += "," + a.getCode();
                abnormalCodeMap.put(a.getSku(), code);
            } else {
                abnormalCodeMap.put(a.getSku(), a.getCode());
            }
        }
        for (int i = 0; i < saleOrderBillDtls.size(); i++) {
            SaleOrderBillDtl dtl = saleOrderBillDtls.get(i);
            Style style = CacheManager.getStyleById(dtl.getStyleId());
            dtl.setStyleName(style.getStyleName());
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
            dtl.setTagPrice(style.getPrice());
            Map<String,Double> stylePriceMap = new HashMap<>();
            stylePriceMap.put("price",style.getPrice());
            stylePriceMap.put("wsPrice",style.getWsPrice());
            stylePriceMap.put("puPrice",style.getPuPrice());
            dtl.setStylePriceMap(JSON.toJSONString(stylePriceMap));
            if (codeMap.containsKey(dtl.getSku())) {
                dtl.setUniqueCodes(codeMap.get(dtl.getSku()));
            }
            if (abnormalCodeMap.containsKey(dtl.getSku())) {
                dtl.setNoOutPutCode(abnormalCodeMap.get(dtl.getSku()));
            }
        }
        return saleOrderBillDtls;
    }

    @RequestMapping(value = {"/save","/saveWS"})
    @ResponseBody
    @Override
    public MessageBox save(String saleOrderBillStr, String strDtlList, String userId) throws Exception {
        this.logAllRequestParams();
        try {
             SaleOrderBill saleOrderBill = JSON.parseObject(saleOrderBillStr, SaleOrderBill.class);
            if(CommonUtil.isNotBlank(saleOrderBill.getBillNo())){
                Integer status = this.saleOrderBillService.findBillStatus(saleOrderBill.getBillNo());
                if(status != Constant.ScmConstant.BillStatus.saved && !userId.equals("admin")){
                    return new MessageBox(false, "单据不是录入状态无法保存,请返回");
                }
            }
            saleOrderBill.setBillType(Constant.ScmConstant.BillType.Save);
            List<SaleOrderBillDtl> saleOrderBillDtlList = JSON.parseArray(strDtlList, SaleOrderBillDtl.class);
            User curUser = CacheManager.getUserById(userId);
            if (CommonUtil.isBlank(saleOrderBill.getBillNo())) {
                String prefix = BillConstant.BillPrefix.saleOrder
                        + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
                //String billNo = this.saleOrderBillService.findMaxBillNo(prefix);
                saleOrderBill.setId(prefix);
                saleOrderBill.setBillNo(prefix);
            }
            saleOrderBill.setId(saleOrderBill.getBillNo());
            BillConvertUtil.covertToSaleOrderBill(saleOrderBill, saleOrderBillDtlList, curUser);
            //判断是否有异常唯一码
            //拼接code字符串
            String code="";
            for(SaleOrderBillDtl saleOrderBillDtl: saleOrderBillDtlList){
                if(CommonUtil.isBlank(code)){
                    code+=saleOrderBillDtl.getUniqueCodes();
                }else{
                    code+=","+saleOrderBillDtl.getUniqueCodes();
                }
            }
            List<AbnormalCodeMessage> list = BillConvertUtil.fullAbnormalCodeMessage(saleOrderBillDtlList,0,code);
            //判断是否开启了销售单自动转调拨单
            Setting setting = this.settingService.get("id", "isAutochangeTr");
            if(CommonUtil.isNotBlank(setting)&&CommonUtil.isNotBlank(setting.getValue())&&setting.getValue().equals("true")){
                this.saleOrderBillService.save(saleOrderBill, saleOrderBillDtlList,list);
                List<BillRecord> billRecordList = saleOrderBill.getBillRecordList();
                User user = this.getCurrentUser();
                Map<String, Object> map = this.saleOrderBillService.changeTr(saleOrderBill, saleOrderBillDtlList, billRecordList, list, user);
                return new MessageBox(true, "保存成功,"+(String) map.get("message"), saleOrderBill);
            }else{
                this.saleOrderBillService.save(saleOrderBill, saleOrderBillDtlList,list);
                return new MessageBox(true, "保存成功", saleOrderBill);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/saveRetrun")
    @ResponseBody
    public MessageBox saveRetrun(String billNo, String strDtlList, String userId) throws Exception {
        this.logAllRequestParams();
        try {
            SaleOrderBill saleOrderBill = this.saleOrderBillService.get("billNo", billNo);
            //saleOrderBill.setBillType(Constant.ScmConstant.BillType.Save);
            List<SaleOrderBillDtl> saleOrderBillDtlList = JSON.parseArray(strDtlList, SaleOrderBillDtl.class);
            List<String> skuStrList = new ArrayList<>();
            for (SaleOrderBillDtl dtl : saleOrderBillDtlList) {
                skuStrList.add(dtl.getSku());
            }
            User curUser = CacheManager.getUserById(userId);
            //this.saleOrderBillService.deleteBillDtlByBillNoAndSku(saleOrderBill.getBillNo(), TaskUtil.getSqlStrByList(skuStrList, SaleOrderBillDtl.class, "sku"));
           /* saleOrderBill.setId(saleOrderBill.getBillNo());
            saleOrderBill.setBillNo(saleOrderBill.getBillNo());
            BillConvertUtil.covertToSaleOrderBillOnRetrun(saleOrderBill, saleOrderBillDtlList, curUser);*/
            this.saleOrderBillService.saveRetrun(saleOrderBill, saleOrderBillDtlList,saleOrderBill.getBillNo(), TaskUtil.getSqlStrByList(skuStrList, SaleOrderBillDtl.class, "sku"),curUser);
            return new MessageBox(true, "保存成功", saleOrderBill.getBillNo());
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    @Override
    public ModelAndView add() throws Exception {
        ModelAndView mv = new ModelAndView("/views/logistics/saleOrderBillDetail");

        Unit unit = CacheManager.getUnitByCode(getCurrentUser().getOwnerId());
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

        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        mv.addObject("userId", getCurrentUser().getId());
        mv.addObject("roleid", getCurrentUser().getRoleId());
        mv.addObject("defaultWarehId", defaultWarehId);


        mv.addObject("defaultSaleStaffId", defaultSaleStaffId);
        mv.addObject("ownersId", unit.getOwnerids());
        mv.addObject("pageType", "add");
        mv.addObject("mainUrl", "/logistics/saleOrder/index.do");
        return mv;
    }

    @RequestMapping(value = "/edit")
    @ResponseBody
    @Override
    public ModelAndView edit(String billNo) throws Exception {
        SaleOrderBill saleOrderBill = this.saleOrderBillService.get("billNo", billNo);
        Boolean isAllowEdit = false;
        if(CommonUtil.isBlank(saleOrderBill.getBillType())){
            isAllowEdit = true;
            saleOrderBill.setBillType(Constant.ScmConstant.BillType.Edit);
            HttpServletRequest request = this.getRequest();
            HttpSession session = request.getSession();
            session.setAttribute("billNosale",billNo);
        }else{
            if(saleOrderBill.getBillType().equals(Constant.ScmConstant.BillType.Save)){
                isAllowEdit = true;
                saleOrderBill.setBillType(Constant.ScmConstant.BillType.Edit);
                HttpServletRequest request = this.getRequest();
                HttpSession session = request.getSession();
                session.setAttribute("billNosale",billNo);
            }else{
                isAllowEdit = false;
            }
        }
        if(isAllowEdit){
            ModelAndView mv = new ModelAndView("/views/logistics/saleOrderBillDetail");
            mv.addObject("ownerId", getCurrentUser().getOwnerId());
            mv.addObject("userId", getCurrentUser().getId());
            mv.addObject("pageType", "edit");
            mv.addObject("saleOrderBill", saleOrderBill);
            Unit unit = this.unitService.getunitbyId(getCurrentUser().getOwnerId());
            mv.addObject("ownersId", unit.getOwnerids());
            mv.addObject("roleid", getCurrentUser().getRoleId());
            mv.addObject("groupid", unit.getGroupId());
            mv.addObject("Codes", getCurrentUser().getCode());
            mv.addObject("mainUrl", "/logistics/saleOrder/back.do?billNo="+billNo);
            return mv;
        }else{
            Unit unit = this.unitService.getunitbyId(getCurrentUser().getOwnerId());
            ModelAndView mv = new ModelAndView("/views/logistics/saleOrderBill");
            mv.addObject("billNo",billNo);
            mv.addObject("ownerId", getCurrentUser().getOwnerId());
            mv.addObject("groupid", unit.getGroupId());
            mv.addObject("userId", getCurrentUser().getId());
            return  mv;
        }

    }
    @RequestMapping(value = "/back")
    @ResponseBody
    public ModelAndView back(String billNo){
        SaleOrderBill saleOrderBill = this.saleOrderBillService.get("billNo", billNo);
        saleOrderBill.setBillType(Constant.ScmConstant.BillType.Save);
        HttpServletRequest request = this.getRequest();
        HttpSession session = request.getSession();
        session.removeAttribute("billNosale");
        this.saleOrderBillService.save(saleOrderBill);
        ModelAndView mv = new ModelAndView("/views/logistics/saleOrderBill");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        mv.addObject("userId", getCurrentUser().getId());
        return mv;
    }
    @RequestMapping(value = "/quit")
    @ResponseBody
    public void quit(String billNo){
        SaleOrderBill saleOrderBill = this.saleOrderBillService.get("billNo", billNo);
        saleOrderBill.setBillType(Constant.ScmConstant.BillType.Save);
        HttpServletRequest request = this.getRequest();
        HttpSession session = request.getSession();
        session.removeAttribute("billNosale");
        this.saleOrderBillService.save(saleOrderBill);
    }

    @RequestMapping(value = "/covertToTagBirth")
    @ResponseBody
    public MessageBox covertToTagBirth(String strDtlList) {
        List<SaleOrderBillDtl> saleOrderBillDtlList = JSON.parseArray(strDtlList, SaleOrderBillDtl.class);
        return null;
    }

    @Override
    public MessageBox convert(String strDtlList, String recordList) throws Exception {
        return null;
    }

    /**
     * Wang Yushen
     * 将前端穿回的 billNo 和 EpcList 转换为出库信息，并存入数据库
     *
     * @param billNo
     * @param strEpcList 前端穿回的JSON字符串
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/convertOut","/convertOutWS"})
    @ResponseBody
    public MessageBox convertOut(String billNo, String strEpcList, String strDtlList, String userId) throws Exception {
//        List<SaleOrderBillDtl> saleOrderBillDtlList = this.saleOrderBillService.findBillDtlByBillNo(billNo);
        List<SaleOrderBillDtl> saleOrderBillDtlList = JSON.parseArray(strDtlList, SaleOrderBillDtl.class);
        List<Epc> epcList = JSON.parseArray(strEpcList, Epc.class);
        User currentUser = CacheManager.getUserById(userId);
        SaleOrderBill saleOrderBill = this.saleOrderBillService.get("billNo", billNo);
        //saleOrderBill.setBillType(Constant.ScmConstant.BillType.Save);
        Business business = BillConvertUtil.covertToSaleOrderBusinessOut(saleOrderBill, saleOrderBillDtlList, epcList, currentUser);
        //判断是否有异常唯一码
        //拼接code字符串
        String code="";
        for(SaleOrderBillDtl saleOrderBillDtl: saleOrderBillDtlList){
            if(CommonUtil.isBlank(code)){
                code+=saleOrderBillDtl.getUniqueCodes();
            }else{
                code+=","+saleOrderBillDtl.getUniqueCodes();
            }
        }
        List<AbnormalCodeMessage> list = BillConvertUtil.fullAbnormalCodeMessage(saleOrderBillDtlList,0,code);
        MessageBox messageBox = this.saleOrderBillService.saveBusinessout(saleOrderBill, saleOrderBillDtlList, business, epcList,list);
        if(messageBox.getSuccess()){
            String actPrice = saleOrderBill.getActPrice().toString();
            String totQty = saleOrderBill.getTotQty().toString();
            String sbillNo = saleOrderBill.getBillNo();
            String name = saleOrderBill.getOrigUnitName();
            try {
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
                String phone = this.guestViewService.get("id",saleOrderBill.getDestUnitId()).getTel();
                String openId = this.weiXinUserService.getByPhone(phone).getOpenId();
                String state = WechatTemplate.senMsg(openId,actPrice,totQty,sbillNo,name);
                if (state.equals("success")){
                    TemplateMsg templateMsg = new TemplateMsg();
                    templateMsg.setBillNo(billNo);
                    templateMsg.setOpenId(openId);
                    templateMsg.setActPrice(actPrice);
                    templateMsg.setTotQty(totQty);
                    templateMsg.setName(name);
                    templateMsg.setTime(simpleDateFormat.format(date));
                    templateMsg.setType(0);
                    templateMsgService.save(templateMsg);
                }
            }catch (Exception e){
                e.printStackTrace();
                this.logger.error(e.getMessage());
                return new MessageBox(true, "出库成功");
            }
            return new MessageBox(true, "出库成功");
        }else{
            return messageBox;
        }

    }

    /**
     * Wang Yushen
     * 将前端穿回的 billNo 和 EpcList 转换为入库信息，并存入数据库
     *
     * @param billNo
     * @param strEpcList 前端穿回的JSON字符串
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/convertIn")
    @ResponseBody
    public MessageBox convertIn(String billNo, String strEpcList, String userId) throws Exception {
        List<SaleOrderBillDtl> saleOrderBillDtlList = this.saleOrderBillService.findBillDtlByBillNo(billNo);
        List<Epc> epcList = JSON.parseArray(strEpcList, Epc.class);
        User currentUser = CacheManager.getUserById(userId);
        SaleOrderBill saleOrderBill = this.saleOrderBillService.get("billNo", billNo);
        //saleOrderBill.setBillType(Constant.ScmConstant.BillType.Save);
        Business business = BillConvertUtil.covertToSaleOrderBusinessIn(saleOrderBill, saleOrderBillDtlList, epcList, currentUser);
        MessageBox messageBox = this.saleOrderBillService.saveBusiness(saleOrderBill, saleOrderBillDtlList, business);
        if(messageBox.getSuccess()){
            return new MessageBox(true, "入库成功");
        }else{
            return messageBox;
        }

    }

    /**
     * Wang Yushen
     * 将加盟商入库前端穿回的 billNo 和 EpcList 转换为入库信息，并存入数据库
     *
     * @param billNo
     * @param strEpcList 前端穿回的JSON字符串
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/convertInfranchisee")
    @ResponseBody
    public MessageBox convertInfranchisee(String billNo, String strEpcList, String userId) throws Exception {
        List<SaleOrderBillDtl> saleOrderBillDtlList = this.saleOrderBillService.findBillDtlByBillNo(billNo);
        List<Epc> epcList = JSON.parseArray(strEpcList, Epc.class);
        String codes = "";
        for (int i = 0; i < epcList.size(); i++) {
            Epc epc = epcList.get(i);
            if (i == 0) {
                codes += epc.getCode();
            } else {
                codes += "," + epc.getCode();
            }
        }
        User currentUser = CacheManager.getUserById(userId);
        SaleOrderBill saleOrderBill = this.saleOrderBillService.get("billNo", billNo);
        saleOrderBill.setBillType(Constant.ScmConstant.BillType.Save);
        Business business = BillConvertUtil.covertToSaleOrderBusinessIn(saleOrderBill, saleOrderBillDtlList, epcList, currentUser);
        MessageBox messageBox = this.saleOrderBillService.saveBusiness(saleOrderBill, saleOrderBillDtlList, business);
        if(messageBox.getSuccess()){
            this.inventoryService.updateEpcStockInsStockss(codes);
            return new MessageBox(true, "入库成功");
        }else{

            return messageBox;
        }

    }

    /**
     * Wang Yushen
     * 用于销售单换货（已销售的）
     *
     * @param origCode 原唯一码
     * @param exchangeCode 换货唯一码
     * @param origSku 原SKU
     * @param exchangeSku 换货SKU
     * @param billNo 销售单号
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/confirmExchange")
    @ResponseBody
    public MessageBox confirmExchange(String origCode, String exchangeCode, String origSku, String exchangeSku, String billNo) throws Exception {
        try {
            this.saleOrderBillService.confirmExchange(origCode, exchangeCode, origSku, exchangeSku, billNo);
            return new MessageBox(true, "替换成功");
        }catch (Exception e){
            e.printStackTrace();
            return new MessageBox(false, "替换失败");
        }
    }
    @RequestMapping(value = "/findResourceButton")
    @ResponseBody
    public MessageBox findResourceButton(){
        try {
            List<ResourcePrivilege> resourcePrivilege = this.resourcePrivilegeService.findButtonByCodeAndRoleId("logistics/saleOrder", this.getCurrentUser().getRoleId(),"button");
            return new MessageBox(true, "查询成功", resourcePrivilege);
        }catch (Exception e){
            e.printStackTrace();
            return new MessageBox(true, "查询失败");
        }
    }
    @RequestMapping(value = "/findResourceTable")
    @ResponseBody
    public MessageBox findResourceTable(){
        try {
            List<ResourcePrivilege> resourcePrivilege = this.resourcePrivilegeService.findResourceButtonByCodeAndRoleId("logistics/saleOrder", this.getCurrentUser().getRoleId(),"table");
            return new MessageBox(true, "查询成功", resourcePrivilege);
        }catch (Exception e){
            e.printStackTrace();
            return new MessageBox(true, "查询失败");
        }
    }
    /**
     * czf
     * 用于销售单转调拨申请单
     ** @param billNo 销售单号
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/changeTr")
    @ResponseBody
    public MessageBox changeTr(String billNo){
        SaleOrderBill saleOrderBill = this.saleOrderBillService.get("id", billNo);
        List<SaleOrderBillDtl> billDtlByBillNo = this.saleOrderBillService.findBillDtlByBillNo(billNo);
        List<BillRecord> billRecordList = this.saleOrderBillService.getBillRecod(billNo);
        List<AbnormalCodeMessage> abnormalCodeMessageByBillNo = this.saleOrderBillService.findAbnormalCodeMessageByBillNo(billNo);
        User user = this.getCurrentUser();
        Map<String, Object> map = this.saleOrderBillService.changeTr(saleOrderBill, billDtlByBillNo, billRecordList, abnormalCodeMessageByBillNo, user);
        Boolean isok =(Boolean) map.get("isok");
        if(isok){
            return new MessageBox(true, (String) map.get("message"));
        }else{
            return new MessageBox(false, (String) map.get("message"));
        }

    }
    /**
     * czf
     * 用于销售单删除异常code
     ** @param billNo 销售单号
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/deletenoOutPutCode")
    @ResponseBody
    public MessageBox deletenoOutPutCode(String billNo,String noOutPutCode){
        try {
            this.saleOrderBillService.deletenoOutPutCode(billNo,noOutPutCode);
            return new MessageBox(true, "删除成功");
        }catch (Exception e){
            return new MessageBox(false, "删除失败");
        }

    }
}

