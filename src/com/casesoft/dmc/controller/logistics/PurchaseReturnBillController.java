package com.casesoft.dmc.controller.logistics;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.json.FastJSONUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.BillRecord;
import com.casesoft.dmc.model.logistics.PurchaseReturnBill;
import com.casesoft.dmc.model.logistics.PurchaseReturnBillDtl;
import com.casesoft.dmc.model.sys.ResourcePrivilege;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.service.logistics.PurchaseReturnBillService;
import com.casesoft.dmc.service.sys.ResourcePrivilegeService;
import com.casesoft.dmc.service.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Session on 2017-06-29.
 * 采购退货
 */

@Controller
@RequestMapping(value = "/logistics/purchaseReturn")
public class PurchaseReturnBillController extends BaseController implements ILogisticsBillController<PurchaseReturnBill> {

    @Autowired
    private PurchaseReturnBillService purchaseReturnBillService;
    @Autowired
    private TaskService taskService;
    @Autowired
    ResourcePrivilegeService resourcePrivilegeService;

    //@RequestMapping(value = "/index")
    @Override
    public String index() {
        return "/views/logistics/purchaseReturnBill";
    }

    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {
        ModelAndView mv = new ModelAndView("/views/logistics/purchaseReturnBill");
        User user = this.getCurrentUser();
        mv.addObject("OwnerId", user.getOwnerId());
        mv.addObject("userId", getCurrentUser().getId());
        Unit unit = CacheManager.getUnitByCode(getCurrentUser().getOwnerId());
        List<ResourcePrivilege> resourcePrivilege = this.resourcePrivilegeService.findPrivilege("logistics/purchaseReturn", this.getCurrentUser().getRoleId());
        mv.addObject("resourcePrivilege", FastJSONUtil.getJSONString(resourcePrivilege));
        String defaultWarehId = unit.getDefaultWarehId();
        mv.addObject("defaultWarehId", defaultWarehId);
        mv.addObject("pageType", "add");
        return mv;
    }

    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<PurchaseReturnBill> findPage(Page<PurchaseReturnBill> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        page.setPageProperty();
        String ownerId = getCurrentUser().getOwnerId();
        String id = getCurrentUser().getId();
        if(!id.equals("admin")){
            PropertyFilter filter = new PropertyFilter("EQS_ownerId", ownerId);
            filters.add(filter);
        }
        page = this.purchaseReturnBillService.findPage(page, filters);

        for (PurchaseReturnBill p : page.getRows()) {
            if (CommonUtil.isNotBlank(p.getOrigId()))
                p.setOrigName(CacheManager.getUnitByCode(p.getOrigId()).getName());
            if (CommonUtil.isNotBlank(p.getDestUnitId()))
                p.setDestUnitName(CacheManager.getUnitByCode(p.getDestUnitId()).getName());

        }

        return page;
    }


    @RequestMapping(value = "/list")
    @ResponseBody
    @Override
    public List<PurchaseReturnBill> list() throws Exception {
        this.logAllRequestParams();

        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());

        List<PurchaseReturnBill> purchaseReturnBillList = this.purchaseReturnBillService.find(filters);

        return purchaseReturnBillList;
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    @Override
    public ModelAndView add() throws Exception {
        ModelAndView mv = new ModelAndView("/views/logistics/purchaseReturnBillDetail");
        mv.addObject("pageType", "add");
        mv.addObject("ownerId",getCurrentUser().getOwnerId());
        mv.addObject("userId",getCurrentUser().getId());
        Unit unit = CacheManager.getUnitByCode(getCurrentUser().getOwnerId());
        String defaultWarehId = unit.getDefaultWarehId();
        mv.addObject("defaultWarehId", defaultWarehId);
        mv.addObject("mainUrl", "/logistics/purchaseReturn/index.do");
        return mv;
    }

    @RequestMapping(value = "/edit")
    @Override
    public ModelAndView edit(String billNo) throws Exception {
        PurchaseReturnBill purchaseReturnBill = this.purchaseReturnBillService.findUniqueByBillNo(billNo);
        Boolean isAllowEdit = false;
        if(CommonUtil.isBlank(purchaseReturnBill.getBillType())){
            isAllowEdit = true;
            purchaseReturnBill.setBillType(Constant.ScmConstant.BillType.Edit);
            HttpServletRequest request = this.getRequest();
            HttpSession session = request.getSession();
            session.setAttribute("billNoPurchaseReturn",billNo);

        }else{
            if(purchaseReturnBill.getBillType().equals(Constant.ScmConstant.BillType.Save)){
                isAllowEdit = true;
                purchaseReturnBill.setBillType(Constant.ScmConstant.BillType.Edit);
                HttpServletRequest request = this.getRequest();
                HttpSession session = request.getSession();
                session.setAttribute("billNoPurchaseReturn",billNo);
            }else{
                isAllowEdit = false;
            }
        }
        if(isAllowEdit){
            ModelAndView mv = new ModelAndView("/views/logistics/purchaseReturnBillDetail");
            mv.addObject("pageType", "edit");
            mv.addObject("purchaseReturnBill", purchaseReturnBill);
            mv.addObject("ownerId", getCurrentUser().getOwnerId());
            mv.addObject("userId", getCurrentUser().getId());
            Unit unit = CacheManager.getUnitByCode(getCurrentUser().getOwnerId());
            String defaultWarehId = unit.getDefaultWarehId();
            mv.addObject("defaultWarehId", defaultWarehId);
            mv.addObject("mainUrl", "/logistics/purchaseReturn/back.do?billNo="+billNo);
            return mv;
        }else{
            ModelAndView mv = new ModelAndView("/views/logistics/purchaseReturnBill");
            mv.addObject("billNo",billNo);
            mv.addObject("ownerId", getCurrentUser().getOwnerId());
            mv.addObject("userId", getCurrentUser().getId());
            return  mv;
        }

    }

    @RequestMapping(value = "/back")
    @ResponseBody
    public ModelAndView back(String billNo){
        PurchaseReturnBill purchaseReturnBill = this.purchaseReturnBillService.findUniqueByBillNo(billNo);
        purchaseReturnBill.setBillType(Constant.ScmConstant.BillType.Save);
        HttpServletRequest request = this.getRequest();
        HttpSession session = request.getSession();
        session.removeAttribute("billNoPurchaseReturn");
        this.purchaseReturnBillService.save(purchaseReturnBill);
        ModelAndView mv = new ModelAndView("/views/logistics/purchaseReturnBill");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        mv.addObject("userId", getCurrentUser().getId());
        return mv;
    }
    @RequestMapping(value = "/quit")
    @ResponseBody
    public void quit(String billNo){
        PurchaseReturnBill purchaseReturnBill = this.purchaseReturnBillService.findUniqueByBillNo(billNo);
        purchaseReturnBill.setBillType(Constant.ScmConstant.BillType.Save);
        HttpServletRequest request = this.getRequest();
        HttpSession session = request.getSession();
        session.removeAttribute("billNoPurchaseReturn");
        this.purchaseReturnBillService.save(purchaseReturnBill);
    }

    @RequestMapping(value = "/findDtls")
    @ResponseBody
    public List<PurchaseReturnBillDtl> findDetails(String billNo) {
        this.logAllRequestParams();
        List<PurchaseReturnBillDtl> purchaseReturnBillDtlList = this.purchaseReturnBillService.findDetailsByBillNo(billNo);
        List<BillRecord> billRecordList = this.purchaseReturnBillService.getBillRecod(billNo);
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
        for (PurchaseReturnBillDtl dtl : purchaseReturnBillDtlList) {
            if (CommonUtil.isNotBlank(CacheManager.getColorNameById(dtl.getColorId()))) {
                dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            }
            if (CommonUtil.isNotBlank(CacheManager.getSizeNameById(dtl.getSizeId()))) {
                dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
            }
            if (CommonUtil.isNotBlank(CacheManager.getStyleNameById(dtl.getStyleId()))) {
                dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
            }
            if (codeMap.containsKey(dtl.getSku())) {
                dtl.setUniqueCodes(codeMap.get(dtl.getSku()));
            }
        }

        return purchaseReturnBillDtlList;
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    @Override
    public MessageBox save(String strBill, String strDtlList, String userId) throws Exception {
        try {
            System.out.println("bill={" + strBill + "}");
            System.out.println("dtlList={" + strDtlList + "}");
            PurchaseReturnBill purchaseReturnBill = JSON.parseObject(strBill, PurchaseReturnBill.class);
            List<PurchaseReturnBillDtl> purchaseReturnBillDtls = JSON.parseArray(strDtlList, PurchaseReturnBillDtl.class);
            if (CommonUtil.isBlank(purchaseReturnBill.getBillNo())) {
                String prefix = BillConstant.BillPrefix.purchaseReturn
                        + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
                String billNo = this.purchaseReturnBillService.findMaxBillNo(prefix);
                purchaseReturnBill.setBillNo(billNo);
                purchaseReturnBill.setId(billNo);
            }else{
                Integer status = this.purchaseReturnBillService.findBillStatus(purchaseReturnBill.getBillNo());
                if (status != Constant.ScmConstant.BillStatus.saved && !userId.equals("admin")) {
                    return new MessageBox(false, "单据不是录入状态无法保存,请返回");
                }
            }
            User currentUser = CacheManager.getUserById(userId);
            purchaseReturnBill.setId(purchaseReturnBill.getBillNo());
            BillConvertUtil.convertToPurchaseReturnBill(purchaseReturnBill, purchaseReturnBillDtls,currentUser);
            this.purchaseReturnBillService.saveBatch(purchaseReturnBill, purchaseReturnBillDtls);
            return returnSuccessInfo("保存成功", purchaseReturnBill.getBillNo());
        } catch (Exception e) {
            e.printStackTrace();
            return returnFailInfo("保存失败");
        }
    }

    @RequestMapping(value = "/check")
    @ResponseBody
    @Override
    public MessageBox check(String billNo) {
        PurchaseReturnBill purchaseReturnBill = this.purchaseReturnBillService.findUniqueByBillNo(billNo);
        if (purchaseReturnBill.getStatus() != 0)
            return returnFailInfo("非录入状态不可审核!");
        purchaseReturnBill.setStatus(BillConstant.BillStatus.Check);
        try {
            this.purchaseReturnBillService.save(purchaseReturnBill);
            return returnSuccessInfo("审核成功");
        } catch (Exception e) {
            return returnFailInfo("审核失败");
        }

    }

    @RequestMapping(value = "/cancel")
    @ResponseBody
    @Override
    public MessageBox cancel(String billNo) {
        PurchaseReturnBill purchaseReturnBill = this.purchaseReturnBillService.findUniqueByBillNo(billNo);
        if (purchaseReturnBill.getStatus() != 0)
            return returnFailInfo("非录入状态不可取消!");
        purchaseReturnBill.setStatus(BillConstant.BillStatus.Cancel);
        try {
            this.purchaseReturnBillService.cancel(purchaseReturnBill);
            return returnSuccessInfo("取消成功");
        } catch (Exception e) {
            return returnFailInfo("取消失败");
        }

    }

    @RequestMapping(value = "/apply")
    @ResponseBody
    public MessageBox apply(String billNo) throws Exception {
        PurchaseReturnBill purchaseReturnBill = this.purchaseReturnBillService.findUniqueByBillNo(billNo);
        purchaseReturnBill.setStatus(BillConstant.BillStatus.Apply);
        this.purchaseReturnBillService.update(purchaseReturnBill);
        return new MessageBox(true, "申请成功");
    }

    @Override
    public MessageBox end(String billNo) throws Exception {
        return null;
    }


    @Override
    public MessageBox convert(String strDtlList, String recordList) throws Exception {
        return null;
    }

    @RequestMapping(value = "/convertOut")
    @ResponseBody
    public MessageBox convertOut(String billNo, String strEpcList, String userId) throws Exception {
        User currentUser = CacheManager.getUserById(userId);
        PurchaseReturnBill purchaseReturnBill = this.purchaseReturnBillService.findUniqueByBillNo(billNo);
        List<PurchaseReturnBillDtl> purchaseReturnBillDtlList = this.purchaseReturnBillService.findDetailsByBillNo(billNo);
        List<Epc> epcList = JSON.parseArray(strEpcList, Epc.class);
        Business business = BillConvertUtil.covertToPurchaseBillBusiness(purchaseReturnBill, purchaseReturnBillDtlList, epcList, currentUser);
        MessageBox messageBox = this.purchaseReturnBillService.saveBusiness(purchaseReturnBill, purchaseReturnBillDtlList, business);
        if(messageBox.getSuccess()){
            return new MessageBox(true, "采购退货出库成功");
        }else{
            return messageBox;
        }


    }

    @RequestMapping(value = "/copyAdd")
    @ResponseBody
    public ModelAndView addCopy(String billNo) {
        ModelAndView mv = new ModelAndView("/views/logistics/purchaseReturnBillDetail");
        PurchaseReturnBill bill = this.purchaseReturnBillService.findUniqueByBillNo(billNo);
        User user = this.getCurrentUser();
        mv.addObject("OwnerId", user.getOwnerId());
        mv.addObject("userId",getCurrentUser().getId());
        mv.addObject("purchaseReturnBill", bill);
        mv.addObject("pageType", "copyAdd");
        mv.addObject("mainUrl", "/logistics/purchaseReturn/index.do");
        return mv;
    }
}
