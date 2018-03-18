package com.casesoft.dmc.controller.logistics;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.BillRecord;
import com.casesoft.dmc.model.logistics.SaleOrderBill;
import com.casesoft.dmc.model.logistics.SaleOrderBillDtl;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.service.logistics.SaleOrderBillService;
import com.casesoft.dmc.service.stock.InventoryService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by koudepei on 2017/6/18.
 */
@Controller
@RequestMapping("/logistics/saleOrder")
public class SaleOrderBillController extends BaseController implements ILogisticsBillController<SaleOrderBill> {

    @Autowired
    private SaleOrderBillService saleOrderBillService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private UnitService unitService;

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
        mv.addObject("ownersId", unit.getOwnerids());
        mv.addObject("userId", getCurrentUser().getId());
        return mv;
    }

    @RequestMapping(value = "/page")
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

    @RequestMapping(value = "/cancel")
    @ResponseBody
    @Override
    public MessageBox cancel(String billNo) throws Exception {
        SaleOrderBill saleOrderBill = this.saleOrderBillService.get("billNo", billNo);
        saleOrderBill.setStatus(BillConstant.BillStatus.Cancel);
        this.saleOrderBillService.cancelUpdate(saleOrderBill);
        return new MessageBox(true, "撤销成功");
    }

    @RequestMapping(value = "/apply")
    @ResponseBody
    public MessageBox apply(String billNo) throws Exception {
        SaleOrderBill saleOrderBill = this.saleOrderBillService.get("billNo", billNo);
        saleOrderBill.setStatus(BillConstant.BillStatus.Apply);
        this.saleOrderBillService.update(saleOrderBill);
        return new MessageBox(true, "申请成功");
    }

    @RequestMapping(value = "/findBillDtl")
    @ResponseBody
    public List<SaleOrderBillDtl> findBillDtl(String billNo) throws Exception {
        this.logAllRequestParams();
        List<SaleOrderBillDtl> saleOrderBillDtls = this.saleOrderBillService.findBillDtlByBillNo(billNo);
        List<BillRecord> billRecordList = this.saleOrderBillService.getBillRecod(billNo);
        Map<String, String> codeMap = new HashMap<>();
        for (BillRecord r : billRecordList) {
            if (codeMap.containsKey(r.getSku())) {
                String code = codeMap.get(r.getSku());
                code += "," + r.getCode();
                codeMap.put(r.getSku(), code);
            } else {
                codeMap.put(r.getSku(), r.getCode());
            }
        }
        for (int i = 0; i < saleOrderBillDtls.size(); i++) {
            SaleOrderBillDtl dtl = saleOrderBillDtls.get(i);
            dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
            dtl.setTagPrice(CacheManager.getStyleById(dtl.getStyleId()).getPrice());
            if (codeMap.containsKey(dtl.getSku())) {
                dtl.setUniqueCodes(codeMap.get(dtl.getSku()));
            }
        }
        return saleOrderBillDtls;
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    @Override
    public MessageBox save(String saleOrderBillStr, String strDtlList, String userId) throws Exception {
        this.logAllRequestParams();
        try {
            SaleOrderBill saleOrderBill = JSON.parseObject(saleOrderBillStr, SaleOrderBill.class);
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
            this.saleOrderBillService.save(saleOrderBill, saleOrderBillDtlList);
            return new MessageBox(true, "保存成功", saleOrderBill.getBillNo());
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
        Customer customer = CacheManager.getCustomerById(defalutCustomerId);
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        mv.addObject("userId", getCurrentUser().getId());
        mv.addObject("roleid", getCurrentUser().getRoleId());
        mv.addObject("defaultWarehId", defaultWarehId);
        mv.addObject("defalutCustomerId", defalutCustomerId);
        mv.addObject("defalutCustomerName", customer.getName());
        mv.addObject("defalutCustomerdiscount", customer.getDiscount());
        mv.addObject("defalutCustomercustomerType", unit.getType());
        mv.addObject("defalutCustomerowingValue", customer.getOwingValue());
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
            mv.addObject("Codes", getCurrentUser().getCode());
            mv.addObject("mainUrl", "/logistics/saleOrder/back.do?billNo="+billNo);
            return mv;
        }else{
            ModelAndView mv = new ModelAndView("/views/logistics/saleOrderBill");
            mv.addObject("billNo",billNo);
            mv.addObject("ownerId", getCurrentUser().getOwnerId());
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
    @RequestMapping(value = "/convertOut")
    @ResponseBody
    public MessageBox convertOut(String billNo, String strEpcList, String strDtlList, String userId) throws Exception {
//        List<SaleOrderBillDtl> saleOrderBillDtlList = this.saleOrderBillService.findBillDtlByBillNo(billNo);
        List<SaleOrderBillDtl> saleOrderBillDtlList = JSON.parseArray(strDtlList, SaleOrderBillDtl.class);
        List<Epc> epcList = JSON.parseArray(strEpcList, Epc.class);
        User currentUser = CacheManager.getUserById(userId);
        SaleOrderBill saleOrderBill = this.saleOrderBillService.get("billNo", billNo);
        //saleOrderBill.setBillType(Constant.ScmConstant.BillType.Save);
        Business business = BillConvertUtil.covertToSaleOrderBusinessOut(saleOrderBill, saleOrderBillDtlList, epcList, currentUser);

        this.saleOrderBillService.saveBusinessout(saleOrderBill, saleOrderBillDtlList, business, epcList);
        return new MessageBox(true, "出库成功");
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
        this.saleOrderBillService.saveBusiness(saleOrderBill, saleOrderBillDtlList, business);
        return new MessageBox(true, "入库成功");
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
        this.saleOrderBillService.saveBusiness(saleOrderBill, saleOrderBillDtlList, business);
        this.inventoryService.updateEpcStockInsStockss(codes);
        return new MessageBox(true, "入库成功");
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
}
