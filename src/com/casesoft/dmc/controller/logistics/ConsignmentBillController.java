package com.casesoft.dmc.controller.logistics;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.service.logistics.ConsignmentBillService;
import com.casesoft.dmc.service.logistics.SaleOrderReturnBillService;
import com.casesoft.dmc.service.stock.EpcStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/14.
 */
@RequestMapping(value = "/logistics/Consignment")
@Controller
public class ConsignmentBillController extends BaseController implements ILogisticsBillController<ConsignmentBill> {
    @Autowired
    private EpcStockService epcStockService;
    @Autowired
    private ConsignmentBillService consignmentBillService;
    @Autowired
    private SaleOrderReturnBillService saleOrderReturnBillService;


    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {
        ModelAndView mv = new ModelAndView("/views/logistics/consignmentBillNew");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        mv.addObject("userId", getCurrentUser().getId());
        Unit unit = CacheManager.getUnitById(getCurrentUser().getOwnerId());
        mv.addObject("ownersId", unit.getOwnerids());
        mv.addObject("pageType","add");
        String defaultWarehId = unit.getDefaultWarehId();
        String defaultSaleStaffId = unit.getDefaultSaleStaffId();
        String defalutCustomerId = unit.getDefalutCustomerId();
        if (CommonUtil.isNotBlank(defalutCustomerId)) {
            Customer customer = CacheManager.getCustomerById(defalutCustomerId);
            mv.addObject("defaultWarehId", defaultWarehId);
            mv.addObject("defalutCustomerId", defalutCustomerId);
            mv.addObject("defalutCustomerName", customer.getName());
            mv.addObject("defalutCustomerdiscount", customer.getDiscount());
            mv.addObject("defalutCustomercustomerType", unit.getType());
            mv.addObject("defalutCustomerowingValue", customer.getOwingValue());
        }
        mv.addObject("defaultSaleStaffId", defaultSaleStaffId);
        return mv;
    }

    @RequestMapping(value = "/page")
    @ResponseBody
    public Page<ConsignmentBill> findPage(Page<ConsignmentBill> page, String userId) throws Exception {
        this.logAllRequestParams();

        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        User currentUser = CacheManager.getUserById(userId);
        String ownerId = currentUser.getOwnerId();
        String id = currentUser.getId();
        if (!id.equals("admin")) {
            PropertyFilter filter = new PropertyFilter("EQS_ownerId", ownerId);
            filters.add(filter);
        }
        page.setPageProperty();

        page = this.consignmentBillService.findPage(page, filters);
        for (ConsignmentBill sb : page.getRows()) {
            if (CommonUtil.isNotBlank(sb.getCustomer()))
                if (CommonUtil.isNotBlank(CacheManager.getUnitById(sb.getCustomer())))
                    sb.setCustomerName(CacheManager.getUnitById(sb.getCustomer()).getName());
            if (CommonUtil.isNotBlank(sb.getOrigId()))
                sb.setOrigName(CacheManager.getUnitByCode(sb.getOrigId()).getName());
        }
        return page;
    }

    @Override
    public Page<ConsignmentBill> findPage(Page<ConsignmentBill> page) throws Exception {
        return null;
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    @Override
    public List<ConsignmentBill> list() throws Exception {
        this.logAllRequestParams();

        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());

        List<ConsignmentBill> consignmentBills = this.consignmentBillService.find(filters);

        return consignmentBills;
    }

    @RequestMapping("/save")
    @ResponseBody
    @Override
    public MessageBox save(String bill, String strDtlList, String userId) throws Exception {
        System.out.println("bill=" + bill);
        System.out.println("strDtlList=" + strDtlList);
        try {
            ConsignmentBill consignmentBill = JSON.parseObject(bill, ConsignmentBill.class);
            if(CommonUtil.isNotBlank(consignmentBill.getBillNo())){
                Integer status = this.consignmentBillService.findBillStatus(consignmentBill.getBillNo());
                if(status != Constant.ScmConstant.BillStatus.saved && !userId.equals("admin")){
                    return new MessageBox(false, "单据不是录入状态无法保存,请返回");
                }
            }
            List<ConsignmentBillDtl> consignmentBillDtls = JSON.parseArray(strDtlList, ConsignmentBillDtl.class);
            User currentUser = CacheManager.getUserById(userId);

            if (CommonUtil.isBlank(consignmentBill.getBillNo())) {
                String prefix = BillConstant.BillPrefix.Consignment + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
                //String billNo = this.consignmentBillService.findMaxBillNo(prefix);
                consignmentBill.setBillNo(prefix);
                consignmentBill.setId(prefix);
            } else {
                consignmentBill.setId(consignmentBill.getBillNo());
                for (ConsignmentBillDtl sdtl : consignmentBillDtls) {
                    if (CommonUtil.isNotBlank(sdtl.getId())) {
                        sdtl.setBillNo(consignmentBill.getBillNo());
                        sdtl.setBillId(consignmentBill.getBillNo());
                    }
                }
            }

            BillConvertUtil.convertToConsignmentBillBill(consignmentBill, consignmentBillDtls, currentUser);

            this.consignmentBillService.saveReturnBatch(consignmentBill, consignmentBillDtls);
            return returnSuccessInfo("保存成功", consignmentBill.getBillNo());
        } catch (Exception e) {
            e.printStackTrace();
            return returnFailInfo("保存失败", e.getMessage());
        }
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    @Override
    public ModelAndView add() throws Exception {
        ModelAndView mav = new ModelAndView("/views/logistics/consignmentBillDetail");
        Unit unit = CacheManager.getUnitByCode(getCurrentUser().getOwnerId());
        String defaultWarehId = unit.getDefaultWarehId();
        String defaultSaleStaffId = unit.getDefaultSaleStaffId();
        String defalutCustomerId = unit.getDefalutCustomerId();

        if (CommonUtil.isNotBlank(defalutCustomerId)) {
            Customer customer = CacheManager.getCustomerById(defalutCustomerId);
            mav.addObject("defaultWarehId", defaultWarehId);
            mav.addObject("defalutCustomerId", defalutCustomerId);
            mav.addObject("defalutCustomerName", customer.getName());
            mav.addObject("defalutCustomerdiscount", customer.getDiscount());
            mav.addObject("defalutCustomercustomerType", unit.getType());
            mav.addObject("defalutCustomerowingValue", customer.getOwingValue());
        }
        mav.addObject("ownerId", getCurrentUser().getOwnerId());
        mav.addObject("userId", getCurrentUser().getId());

        mav.addObject("defaultSaleStaffId", defaultSaleStaffId);
        mav.addObject("pageType", "add");
        mav.addObject("ownersId", unit.getOwnerids());
        mav.addObject("mainUrl", "/logistics/Consignment/index.do");
        return mav;
    }

    @RequestMapping(value = "/edit")
    @ResponseBody
    @Override
    public ModelAndView edit(String billNo) throws Exception {
        ConsignmentBill consignmentBill = this.consignmentBillService.findBillByBillNo(billNo);
        Boolean isAllowEdit = false;
        if (CommonUtil.isBlank(consignmentBill.getBillType())) {
            isAllowEdit = true;
            consignmentBill.setBillType(Constant.ScmConstant.BillType.Edit);
            HttpServletRequest request = this.getRequest();
            HttpSession session = request.getSession();
            session.setAttribute("billNoConsignment", billNo);
        } else {
            if (consignmentBill.getBillType().equals(Constant.ScmConstant.BillType.Save)) {
                isAllowEdit = true;
                consignmentBill.setBillType(Constant.ScmConstant.BillType.Edit);
                HttpServletRequest request = this.getRequest();
                HttpSession session = request.getSession();
                session.setAttribute("billNoConsignment", billNo);
            } else {
                isAllowEdit = false;
            }
        }
        if (isAllowEdit) {
            ModelAndView mav = new ModelAndView("/views/logistics/consignmentBillDetail");
            mav.addObject("ownerId", getCurrentUser().getOwnerId());
            mav.addObject("userId", getCurrentUser().getId());
            mav.addObject("consignmentBill", consignmentBill);
            Unit unit = CacheManager.getUnitById(getCurrentUser().getOwnerId());
            mav.addObject("ownersId", unit.getOwnerids());
            mav.addObject("pageType", "edit");
            mav.addObject("mainUrl", "/logistics/Consignment/back.do?billNo=" + billNo);
            return mav;
        } else {
            ModelAndView mv = new ModelAndView("/views/logistics/consignmentBill");
            mv.addObject("billNo", billNo);
            mv.addObject("ownerId", getCurrentUser().getOwnerId());
            mv.addObject("userId", getCurrentUser().getId());
            return mv;
        }

    }

    @RequestMapping(value = "/back")
    @ResponseBody
    public ModelAndView back(String billNo) {
        ConsignmentBill consignmentBill = this.consignmentBillService.findBillByBillNo(billNo);
        consignmentBill.setBillType(Constant.ScmConstant.BillType.Save);
        HttpServletRequest request = this.getRequest();
        HttpSession session = request.getSession();
        session.removeAttribute("billNoConsignment");
        this.consignmentBillService.save(consignmentBill);
        ModelAndView mv = new ModelAndView("/views/logistics/consignmentBill");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        mv.addObject("userId", getCurrentUser().getId());
        return mv;
    }

    @RequestMapping(value = "/quit")
    @ResponseBody
    public void quit(String billNo) {
        ConsignmentBill consignmentBill = this.consignmentBillService.findBillByBillNo(billNo);
        consignmentBill.setBillType(Constant.ScmConstant.BillType.Save);
        HttpServletRequest request = this.getRequest();
        HttpSession session = request.getSession();
        session.removeAttribute("billNoConsignment");
        this.consignmentBillService.save(consignmentBill);
    }

    @RequestMapping(value = "/returnDetails")
    @ResponseBody
    public List<ConsignmentBillDtl> findReturnDtls(String billNo) {
        this.logAllRequestParams();
        List<ConsignmentBillDtl> consignmentBillDtls = this.consignmentBillService.findDtlByBillNo(billNo);
        for (ConsignmentBillDtl s : consignmentBillDtls) {
            if (CommonUtil.isNotBlank(CacheManager.getColorById(s.getColorId()))) {
                s.setColorName(CacheManager.getColorNameById(s.getColorId()));
            }
            if (CommonUtil.isNotBlank(CacheManager.getStyleById(s.getStyleId()))) {
                s.setStyleName(CacheManager.getStyleNameById(s.getStyleId()));
            }
            if (CommonUtil.isNotBlank(CacheManager.getSizeById(s.getSizeId()))) {
                s.setSizeName(CacheManager.getSizeNameById(s.getSizeId()));
            }
            if (CommonUtil.isBlank(s.getOutMonyQty())) {
                s.setOutMonyQty(0);
            }
            List<BillRecord> billRecordByBillNo = this.consignmentBillService.findBillRecordByBillNo(billNo, s.getSku());
            String uniqueCodes = "";
            for (int i = 0; i < billRecordByBillNo.size(); i++) {
                BillRecord billRecord = billRecordByBillNo.get(i);
                if (i == 0) {
                    uniqueCodes += billRecord.getCode();
                } else {
                    uniqueCodes += "," + billRecord.getCode();
                }
            }
            s.setUniqueCodes(uniqueCodes);
        }


        return consignmentBillDtls;
    }

    @RequestMapping(value = "/copyAdd")
    @ResponseBody
    public ModelAndView addCopy(String billNo) {
        ModelAndView mav = new ModelAndView("/views/logistics/consignmentBillDetail");
        ConsignmentBill consignmentBill = this.consignmentBillService.findBillByBillNo(billNo);
        mav.addObject("ownerId", getCurrentUser().getOwnerId());
        mav.addObject("userId", getCurrentUser().getId());
        mav.addObject("consignmentBill", consignmentBill);
        mav.addObject("pageType", "copyAdd");
        mav.addObject("mainUrl", "/logistics/Consignment/index.do");
        return mav;
    }

    @RequestMapping(value = "/check")
    @ResponseBody
    @Override
    public MessageBox check(String billNo) throws Exception {
        this.logAllRequestParams();

        ConsignmentBill consignmentBill = this.consignmentBillService.findBillByBillNo(billNo);
        if (consignmentBill.getStatus() == BillConstant.BillStatus.Enter)
            consignmentBill.setStatus(BillConstant.BillStatus.Check);
        else
            return returnFailInfo("不是录入状态，无法审核");
        try {
            this.consignmentBillService.save(consignmentBill);
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
        ConsignmentBill consignmentBill = this.consignmentBillService.findBillByBillNo(billNo);
        if (consignmentBill.getStatus() == BillConstant.BillStatus.Enter) {
            consignmentBill.setStatus(BillConstant.BillStatus.Cancel);
            //撤销对应的退货单
            List<SaleOrderReturnBill> saleOrderReturnBillNo = this.consignmentBillService.findSaleOrderReturnBillNo(billNo);
            for (SaleOrderReturnBill saleOrderReturnBill : saleOrderReturnBillNo) {
                saleOrderReturnBill.setStatus(BillConstant.BillStatus.Cancel);
                this.saleOrderReturnBillService.cancelUpdate(saleOrderReturnBill);
            }

        } else {
            return returnFailInfo("不是录入状态，无法取消");
        }
        try {
            this.consignmentBillService.cancelUpdate(consignmentBill);
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
     * 将前端传回的 billNo 和 EpcList 转换为入库信息，并存入数据库
     *
     * @param billNo
     * @param strEpcList 前端穿回的JSON字符串
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/convertIn")
    @ResponseBody
    public MessageBox convertIn(String billNo, String strEpcList, String userId) throws Exception {
        List<ConsignmentBillDtl> consignmentBillDtlList = this.consignmentBillService.findDtlByBillNo(billNo);
        List<Epc> epcList = JSON.parseArray(strEpcList, Epc.class);

        //入库校验
        List<EpcStock> EpcStockList = epcStockService.findInStockByEpcList(epcList);

        if (CommonUtil.isBlank(EpcStockList)) {
            User currentUser = CacheManager.getUserById(userId);
            ConsignmentBill consignmentBill = this.consignmentBillService.get("billNo", billNo);
            Business business = BillConvertUtil.covertToConsignmentBillBusinessIn(consignmentBill, consignmentBillDtlList, epcList, currentUser);
            MessageBox messageBox = this.consignmentBillService.saveBusiness(consignmentBill, consignmentBillDtlList, business);
            if (messageBox.getSuccess()) {
                return new MessageBox(true, "入库成功");
            } else {
                return messageBox;
            }

        } else {
            StringBuilder sb = new StringBuilder();
            for (EpcStock epcStock : EpcStockList) {
                String wareHouseName = CacheManager.getUnitById(epcStock.getWarehouseId()).getName();
                sb.append(epcStock.getSku()).append(" ").append(epcStock.getCode()).append(" 已在仓库：[").append(epcStock.getWarehouseId()).append("]").append(wareHouseName).append(" 中<br>");
            }
            sb.append("不能寄售入库！");
            return new MessageBox(false, sb.toString());
        }
    }

    @Override
    public String index() {
        return null;
    }

    @RequestMapping(value = "/saleRetrun")
    @ResponseBody
    public MessageBox saleRetrun(String billNo, String strEpcList, String userId) {
        List<Epc> epcList = JSON.parseArray(strEpcList, Epc.class);
        List<ConsignmentBillDtl> billDtlByBillNo = this.consignmentBillService.findBillDtlByBillNo(billNo);
        ConsignmentBill consignmentBill = this.consignmentBillService.findBillByBillNo(billNo);
        User currentUser = CacheManager.getUserById(userId);
        Double monny = 0D;
        Integer qty = 0;
        for (ConsignmentBillDtl dtl : billDtlByBillNo) {
            if (dtl.getSale() > 0) {
                monny += dtl.getSale() * dtl.getPrice();
            }
            if (dtl.getQty() - dtl.getSale() > 0) {
                qty += Integer.parseInt(dtl.getQty() - dtl.getSale() + "");
            }

        }
        boolean saleRetrun = this.consignmentBillService.saleRetrun(consignmentBill, billDtlByBillNo, currentUser, epcList);
        if (saleRetrun) {
            return new MessageBox(true, "退货" + qty + "件" + "和" + monny + "CNY");
        } else {
            return new MessageBox(false, "退货失败");
        }


    }

    @RequestMapping(value = "/saleRetrunNo")
    @ResponseBody
    public MessageBox saleRetrunNo( String strEpcList, String userId,String bill, String strDtlList) {
        List<Epc> epcList = JSON.parseArray(strEpcList, Epc.class);
        //List<ConsignmentBillDtl> billDtlByBillNo = this.consignmentBillService.findBillDtlByBillNo(billNo);
        //ConsignmentBill consignmentBill = this.consignmentBillService.findBillByBillNo(billNo);
        ConsignmentBill consignmentBill = JSON.parseObject(bill, ConsignmentBill.class);
        List<ConsignmentBillDtl> consignmentBillDtls = JSON.parseArray(strDtlList, ConsignmentBillDtl.class);
        consignmentBill.setId(consignmentBill.getBillNo());
        for (ConsignmentBillDtl sdtl : consignmentBillDtls) {
            if (CommonUtil.isNotBlank(sdtl.getId())) {
                sdtl.setBillNo(consignmentBill.getBillNo());
                sdtl.setBillId(consignmentBill.getBillNo());
            }
        }
        User currentUser = CacheManager.getUserById(userId);
        Double monny = 0D;
        Integer qty = 0;
        for (ConsignmentBillDtl dtl : consignmentBillDtls) {
            if (CommonUtil.isBlank(dtl.getReadysale())) {
                dtl.setReadysale(0);
            }
            if (CommonUtil.isBlank(dtl.getBeforeoutQty())) {
                dtl.setBeforeoutQty(0);
            }
            if (dtl.getReadysale() > 0) {
                monny += dtl.getReadysale() * dtl.getActPrice();
            }
            if ((dtl.getOutQty() - dtl.getBeforeoutQty()) > 0) {
                qty += Integer.parseInt(dtl.getOutQty() - dtl.getBeforeoutQty() + "");
            }

        }

        //List<ConsignmentBillDtl> consignmentBillDtls = JSON.parseArray(strDtlList, ConsignmentBillDtl.class);

        BillConvertUtil.convertToConsignmentBillBill(consignmentBill, consignmentBillDtls, currentUser);
        boolean saleRetrun = this.consignmentBillService.saleRetrunNo(consignmentBill, consignmentBillDtls, currentUser, epcList,consignmentBillDtls);
        if (saleRetrun) {

            return new MessageBox(true, "退货" + qty + "件" + "和" + monny + "CNY");
        } else {
            return new MessageBox(false, "退货失败");
        }


    }

    @RequestMapping(value = "/updateConsignmentNum")
    @ResponseBody
    public MessageBox updateConsignmentNum(String id, String outQtys) {
        try {
            this.consignmentBillService.updateDetailsByid(id, Integer.parseInt(outQtys));
            return new MessageBox(true, "成功");
        } catch (Exception e) {
            return new MessageBox(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/findSaleOrderReturnBillNo")
    @ResponseBody
    public List<SaleOrderReturnBill> findSaleOrderReturnBillNo(String billno) {
        try {
            List<SaleOrderReturnBill> saleOrderReturnBillNo = this.consignmentBillService.findSaleOrderReturnBillNo(billno);
            return saleOrderReturnBillNo;
        } catch (Exception e) {
            return null;
        }


    }

}
