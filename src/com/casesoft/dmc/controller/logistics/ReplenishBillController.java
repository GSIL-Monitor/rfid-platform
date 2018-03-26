package com.casesoft.dmc.controller.logistics;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.logistics.ReplenishBillService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by Alvin on 2018/1/27.
 */

@Controller
@RequestMapping("/logistics/relenishBill")
public class ReplenishBillController extends BaseController implements ILogisticsBillController<ReplenishBill>{

    @Autowired
    private ReplenishBillService replenishBillService;
    @Autowired
    private UnitService unitService;

    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {
     /*   ModelAndView mv = new ModelAndView("/views/logistics/relenishBill");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        Unit unit = this.unitService.getunitbyId(getCurrentUser().getOwnerId());
        mv.addObject("ownersId", unit.getOwnerids());
        mv.addObject("userId", getCurrentUser().getId());*/
        ModelAndView mv = new ModelAndView("/views/logistics/relenishBillDetail");

        Unit unit = CacheManager.getUnitByCode(getCurrentUser().getOwnerId());
        String defaultWarehId = unit.getDefaultWarehId();
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        mv.addObject("userId", getCurrentUser().getId());
        mv.addObject("roleid", getCurrentUser().getRoleId());
        mv.addObject("defaultWarehId", defaultWarehId);
        mv.addObject("ownersId", unit.getOwnerids());
        mv.addObject("pageType", "add");
        mv.addObject("mainUrl", "/logistics/relenishBill/history.do");
        return mv;
    }

    @RequestMapping(value = "/page")
    @ResponseBody
    public Page<ReplenishBill> findPage(Page<ReplenishBill> page, String userId) throws Exception {
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
        page = this.replenishBillService.findPage(page, filters);
        return page;
    }
    @Override
    public Page<ReplenishBill> findPage(Page<ReplenishBill> page) throws Exception {
        return null;
    }

    @Override
    public List<ReplenishBill> list() throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        List<ReplenishBill> list = this.replenishBillService.find(filters);
        return list;
    }
    @RequestMapping(value = "/save")
    @ResponseBody
    @Override
    public MessageBox save(String replenishBillStr, String strDtlList, String userId) throws Exception {
        this.logAllRequestParams();
        try {
            ReplenishBill replenishBill = JSON.parseObject(replenishBillStr, ReplenishBill.class);
            replenishBill.setBillType(Constant.ScmConstant.BillType.Save);
            List<ReplenishBillDtl> replenishBillDtlList = JSON.parseArray(strDtlList, ReplenishBillDtl.class);
            User curUser = CacheManager.getUserById(userId);
            if (CommonUtil.isBlank(replenishBill.getBillNo())) {
                String prefix = BillConstant.BillPrefix.ReplenishiBill
                        + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
                //String billNo = this.saleOrderBillService.findMaxBillNo(prefix);
                replenishBill.setId(prefix);
                replenishBill.setBillNo(prefix);
            }
            replenishBill.setId(replenishBill.getBillNo());
            BillConvertUtil.covertToReplenishBill(replenishBill, replenishBillDtlList, curUser);
            this.replenishBillService.saveMessage(replenishBill,replenishBillDtlList);
            return new MessageBox(true, "保存成功", replenishBill.getBillNo());
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, e.getMessage());
        }
    }
    @RequestMapping(value = "/add")
    @ResponseBody
    @Override
    public ModelAndView add() throws Exception {
       ModelAndView mv = new ModelAndView("/views/logistics/relenishBillDetail");

        Unit unit = CacheManager.getUnitByCode(getCurrentUser().getOwnerId());
        String defaultWarehId = unit.getDefaultWarehId();
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        mv.addObject("userId", getCurrentUser().getId());
        mv.addObject("roleid", getCurrentUser().getRoleId());
        mv.addObject("defaultWarehId", defaultWarehId);
        mv.addObject("ownersId", unit.getOwnerids());
        mv.addObject("pageType", "add");
        mv.addObject("mainUrl", "/logistics/relenishBill/index.do");
      /*  ModelAndView mv = new ModelAndView("/views/logistics/relenishBill");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        Unit unit = this.unitService.getunitbyId(getCurrentUser().getOwnerId());
        mv.addObject("ownersId", unit.getOwnerids());
        mv.addObject("userId", getCurrentUser().getId());*/
        return mv;
    }
    @RequestMapping(value = "/history")
    @ResponseBody
    public ModelAndView history()throws Exception {
        ModelAndView mv = new ModelAndView("/views/logistics/relenishBill");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        Unit unit = this.unitService.getunitbyId(getCurrentUser().getOwnerId());
        mv.addObject("ownersId", unit.getOwnerids());
        mv.addObject("userId", getCurrentUser().getId());
        return mv;
    }
    @RequestMapping(value = "/edit")
    @ResponseBody
    @Override
    public ModelAndView edit(String billNo) throws Exception {
        ReplenishBill replenishBill = this.replenishBillService.load(billNo);
        ModelAndView mv = new ModelAndView("/views/logistics/relenishBillDetail");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        mv.addObject("userId", getCurrentUser().getId());
        mv.addObject("pageType", "edit");
        mv.addObject("replenishBill", replenishBill);
        Unit unit = this.unitService.getunitbyId(getCurrentUser().getOwnerId());
        mv.addObject("ownersId", unit.getOwnerids());
        mv.addObject("roleid", getCurrentUser().getRoleId());
        mv.addObject("Codes", getCurrentUser().getCode());
        mv.addObject("mainUrl", "/logistics/relenishBill/index.do");
        return mv;

    }

    @Override
    public MessageBox check(String billNo) throws Exception {
        return null;
    }

    @Override
    public MessageBox end(String billNo) throws Exception {
        return null;
    }
    @RequestMapping(value = "/cancel")
    @ResponseBody
    @Override
    public MessageBox cancel(String billNo) throws Exception {
        ReplenishBill replenishBill = this.replenishBillService.get("billNo", billNo);
        replenishBill.setStatus(BillConstant.BillStatus.Cancel);
        this.replenishBillService.cancelUpdate(replenishBill);
        return new MessageBox(true, "撤销成功");
    }

    @Override
    public MessageBox convert(String strDtlList, String recordList) throws Exception {
        return null;
    }
    /**
     * @param  strDtlList 转换采购单明细
     * @param  userId 操作Id
     * 实现补货单转化采购订单 采购单中SrcBillNo 为补货单单号
     * */
    public MessageBox convertToPurchaseOrder(String strDtlList,String userId)throws Exception{
        return null;
    }

    @Override
    public String index() {
        return null;
    }
    @RequestMapping(value = "/findBillDtl")
    @ResponseBody
    public List<ReplenishBillDtl> findBillDtl(String billNo){
        List<ReplenishBillDtl> list=this.replenishBillService.findBillDtl(billNo);
        return list;
    }
    @RequestMapping(value = "/mergeReplenishBill")
    @ResponseBody
    public MessageBox mergeReplenishBill(){
        //1.查询合并单据的数据
        //查询补货单
        List<ReplenishBill> replenishBills = this.replenishBillService.findmergeReplenishBill();
        //查询补货但详情
        String billNos="";
        for(int i=0;i<replenishBills.size();i++){
            if(i==0){
                billNos+="'"+replenishBills.get(i).getId()+"'";
            }else{
                billNos+=",'"+replenishBills.get(i).getId()+"'";
            }
        }
        List<ReplenishBillDtl> replenishBillDtls = this.replenishBillService.findmereReplenishBillDtl(billNos);
        boolean mergeBill = this.replenishBillService.mergeBill(replenishBills, replenishBillDtls, billNos);
        if(mergeBill){
            return new MessageBox(true, "合并成功");
        }else{
            return new MessageBox(false, "合并失败");
        }

    }
}
