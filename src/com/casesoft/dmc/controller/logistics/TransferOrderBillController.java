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
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.sys.*;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.service.logistics.TransferOrderBillService;
import com.casesoft.dmc.service.sys.PrintService;
import com.casesoft.dmc.service.sys.PrintSetService;
import com.casesoft.dmc.service.sys.ResourcePrivilegeService;
import com.casesoft.dmc.service.sys.SettingService;
import com.casesoft.dmc.service.sys.impl.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by yushen on 2017/7/4.
 */

@Controller
@RequestMapping("/logistics/transferOrder")
public class TransferOrderBillController extends BaseController implements ILogisticsBillController<TransferOrderBill> {

    @Autowired
    private TransferOrderBillService transferOrderBillService;

    @Autowired
    private PrintService printService;
    @Autowired
    private PrintSetService printSetService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ResourcePrivilegeService resourcePrivilegeService;
    @Autowired
    private SettingService settingService;


    @Override
    public String index() {
        return "";
    }

    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {
        ModelAndView mv = new ModelAndView("views/logistics/transferOrder");
        List<ResourcePrivilege> resourcePrivilege = this.resourcePrivilegeService.findPrivilege("logistics/transferOrder", this.getCurrentUser().getRoleId());
        mv.addObject("resourcePrivilege", FastJSONUtil.getJSONString(resourcePrivilege));
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        Unit unit = CacheManager.getUnitById(getCurrentUser().getOwnerId());
        mv.addObject("ownersId", unit.getOwnerids());
        mv.addObject("userId", getCurrentUser().getId());
        mv.addObject("pageType", "add");
        mv.addObject("ownersId", unit.getOwnerids());
        return mv;
    }

    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<TransferOrderBill> findPage(Page<TransferOrderBill> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        page.setPageProperty();
        User currentUser = getCurrentUser();
        if(!currentUser.getOwnerId().equals("1")){
            PropertyFilter filter = new PropertyFilter("EQS_destUnitId_OR_origUnitId", currentUser.getOwnerId());
            filters.add(filter);
        }
        page = this.transferOrderBillService.findPage(page, filters);
        return page;
    }

    @Override
    public List<TransferOrderBill> list() throws Exception {
        return null;
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public List<TransferOrderBill> list(String billNo) throws Exception {
        return this.transferOrderBillService.find(billNo);
    }

    @RequestMapping(value = "/findBillDtl")
    @ResponseBody
    public List<TransferOrderBillDtl> findBillDtl(String billNo) throws Exception {
        this.logAllRequestParams();
        List<TransferOrderBillDtl> transferOrderBillDtls = this.transferOrderBillService.findBillDtlByBillNo(billNo);
        Map<String,String> codeMap = new HashMap<>();
        List<BillRecord> billRecordList = this.transferOrderBillService.getBillRecod(billNo);
        for(BillRecord r : billRecordList){
            if(codeMap.containsKey(r.getSku())){
                String code = codeMap.get(r.getSku());
                code += ","+r.getCode();
                codeMap.put(r.getSku(),code);
            }else{
                codeMap.put(r.getSku(),r.getCode());
            }
        }
        for (TransferOrderBillDtl dtl : transferOrderBillDtls) {
            Style s = CacheManager.getStyleById(dtl.getStyleId());
            if(CommonUtil.isNotBlank(s)){
                dtl.setStyleName(s.getStyleName());
                if(CommonUtil.isNotBlank(s.getClass1())){
                    PropertyKey key = CacheManager.getPropertyKey("C1"+"-"+s.getClass1());
                    if(CommonUtil.isNotBlank(key)){
                        dtl.setSupplierName(key.getName());
                    }
                }
            }
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
            if(codeMap.containsKey(dtl.getSku())){
                dtl.setUniqueCodes(codeMap.get(dtl.getSku()));
            }
        }
        return transferOrderBillDtls;
    }


    @RequestMapping(value = "/save")
    @ResponseBody
    @Override
    public MessageBox save(String transferOrderBillStr, String strDtlList, String userId) throws Exception {
        this.logAllRequestParams();
        try {
            TransferOrderBill transferOrderBill = JSON.parseObject(transferOrderBillStr, TransferOrderBill.class);
            System.out.print(transferOrderBill);
            if (CommonUtil.isBlank(transferOrderBill.getBillNo())) {
                String prefix = BillConstant.BillPrefix.Transfer
                        + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
                //String billNo = this.transferOrderBillService.findMaxBillNo(prefix);
                transferOrderBill.setId(prefix);
                transferOrderBill.setBillNo(prefix);
            }else{

                Integer status = this.transferOrderBillService.findBillStatus(transferOrderBill.getBillNo());
                if(status != Constant.ScmConstant.BillStatus.saved && !userId.equals("admin")){
                    return new MessageBox(false, "单据不是录入状态无法保存,请返回");
                }

            }
            List<TransferOrderBillDtl> transferOrderBillDtlList = JSON.parseArray(strDtlList, TransferOrderBillDtl.class);
            transferOrderBill.setId(transferOrderBill.getBillNo());
            User curUser = CacheManager.getUserById(userId);
            BillConvertUtil.covertToTransferOrderBill(transferOrderBill, transferOrderBillDtlList, curUser);
            this.transferOrderBillService.save(transferOrderBill, transferOrderBillDtlList);
            return new MessageBox(true, "保存成功", transferOrderBill);
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    @Override
    public ModelAndView add() throws Exception {
        ModelAndView mv = new ModelAndView("/views/logistics/transferOrderBillDetail");
        mv.addObject("pageType", "add");
        mv.addObject("mainUrl", "/logistics/transferOrder/index.do");
        mv.addObject("ownerId",getCurrentUser().getOwnerId());
        Unit unit = CacheManager.getUnitById(getCurrentUser().getOwnerId());
        mv.addObject("ownersId", unit.getOwnerids());
        mv.addObject("userId",getCurrentUser().getId());
        return mv;
    }

    @RequestMapping(value = "/copyAdd")
    @ResponseBody
    public ModelAndView copyAdd(String billNo) throws Exception {
        TransferOrderBill transferOrderBill = this.transferOrderBillService.get("billNo", billNo);
        ModelAndView mv = new ModelAndView("/views/logistics/transferOrderBillDetail");
        mv.addObject("pageType", "add");
        mv.addObject("transferOrderBill", transferOrderBill);
        mv.addObject("mainUrl", "/logistics/transferOrder/index.do");
        mv.addObject("copy", "copyadd");
        mv.addObject("ownerId",getCurrentUser().getOwnerId());
        mv.addObject("userId",getCurrentUser().getId());
        return mv;
    }

    @RequestMapping(value = "/edit")
    @ResponseBody
    @Override
    public ModelAndView edit(String billNo) throws Exception {
        TransferOrderBill transferOrderBill = this.transferOrderBillService.get("billNo", billNo);
        Boolean isAllowEdit = false;
        if(CommonUtil.isBlank(transferOrderBill.getBillType())){
            isAllowEdit = true;
            transferOrderBill.setBillType(Constant.ScmConstant.BillType.Edit);
            HttpServletRequest request = this.getRequest();
            HttpSession session = request.getSession();
            session.setAttribute("billNotransfer",billNo);
        }else{
            if(transferOrderBill.getBillType().equals(Constant.ScmConstant.BillType.Save)){
                isAllowEdit = true;
                transferOrderBill.setBillType(Constant.ScmConstant.BillType.Edit);
                HttpServletRequest request = this.getRequest();
                HttpSession session = request.getSession();
                session.setAttribute("billNotransfer",billNo);
            }else{
                isAllowEdit = false;
            }
        }
        if(isAllowEdit){
            ModelAndView mv = new ModelAndView("/views/logistics/transferOrderBillDetail");
            mv.addObject("pageType", "edit");
            mv.addObject("transferOrderBill", transferOrderBill);
            mv.addObject("mainUrl", "/logistics/transferOrder/back.do?billNo="+billNo);
            mv.addObject("ownerId",getCurrentUser().getOwnerId());
            Unit unit = CacheManager.getUnitById(getCurrentUser().getOwnerId());
            mv.addObject("ownersId", unit.getOwnerids());
            mv.addObject("roleId", getCurrentUser().getRoleId());
            mv.addObject("userId",getCurrentUser().getId());
            return mv;
        }else{
            ModelAndView mv = new ModelAndView("views/logistics/transferOrder");
            mv.addObject("billNo",billNo);
            mv.addObject("ownerId", getCurrentUser().getOwnerId());
            mv.addObject("userId", getCurrentUser().getId());
            return  mv;
        }

    }
    @RequestMapping(value = "/back")
    @ResponseBody
    public ModelAndView back(String billNo){
        TransferOrderBill transferOrderBill = this.transferOrderBillService.get("billNo", billNo);
        transferOrderBill.setBillType(Constant.ScmConstant.BillType.Save);
        HttpServletRequest request = this.getRequest();
        HttpSession session = request.getSession();
        session.removeAttribute("billNotransfer");
        this.transferOrderBillService.update(transferOrderBill);
        ModelAndView mv = new ModelAndView("views/logistics/transferOrder");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        mv.addObject("userId", getCurrentUser().getId());
        return mv;
    }
    @RequestMapping(value = "/quit")
    @ResponseBody
    public void quit(String billNo){
        TransferOrderBill transferOrderBill = this.transferOrderBillService.get("billNo", billNo);
        transferOrderBill.setBillType(Constant.ScmConstant.BillType.Save);
        HttpServletRequest request = this.getRequest();
        HttpSession session = request.getSession();
        session.removeAttribute("billNotransfer");
        this.transferOrderBillService.update(transferOrderBill);
    }
    @RequestMapping(value = "/check")
    @ResponseBody
    @Override
    public MessageBox check(String billNo) throws Exception {
        TransferOrderBill transferOrderBill = this.transferOrderBillService.get("billNo", billNo);
        transferOrderBill.setStatus(BillConstant.BillStatus.Check);
        this.transferOrderBillService.update(transferOrderBill);
        return new MessageBox(true, "审核成功");
    }

    @Override
    public MessageBox end(String billNo) throws Exception {
        return null;
    }

    @RequestMapping(value = "/cancel")
    @ResponseBody
    @Override
    public MessageBox cancel(String billNo) throws Exception {
        TransferOrderBill transferOrderBill = this.transferOrderBillService.get("billNo", billNo);
        if(transferOrderBill.getStatus().equals(BillConstant.BillStatus.Enter)){
            transferOrderBill.setStatus(BillConstant.BillStatus.Cancel);
            this.transferOrderBillService.update(transferOrderBill);
            return new MessageBox(true, "撤销成功");
        }else{
            return returnFailInfo("不是录入状态，无法取消");
        }

    }

    @Override
    public MessageBox convert(String strDtlList, String recordList) throws Exception {
        return null;
    }

    /**
     * Wang Yushen
     * 将前端穿回的 billNo 和 EpcList 转换为出库信息，并存入数据库
     * @param billNo
     * @param strEpcList 前端穿回的JSON字符串
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/convertOut")
    @ResponseBody
    public MessageBox convertOut(String billNo, String strEpcList, String strDtlList, String userId) throws Exception {
//        List<TransferOrderBillDtl> transferOrderBillDtlList = this.transferOrderBillService.findBillDtlByBillNo(billNo);
        List<TransferOrderBillDtl> transferOrderBillDtlList = JSON.parseArray(strDtlList, TransferOrderBillDtl.class);
        List<Epc> epcList = JSON.parseArray(strEpcList, Epc.class);
        User currentUser = CacheManager.getUserById(userId);
        TransferOrderBill transferOrderBill = this.transferOrderBillService.get("billNo", billNo);
        List<BillRecord> billRecodlist = this.transferOrderBillService.getBillRecod(billNo);
        //得到所有的code
        String codes="";
        if(billRecodlist.size()!=0){
            for(BillRecord billRecord:billRecodlist){
                if(CommonUtil.isBlank(codes)){
                    codes+=billRecord.getCode();
                }else{
                    codes+=","+billRecord.getCode();
                }
            }
        }
        List<BillRecord> billRecordList = new ArrayList<>();
        for(Epc epc:epcList){
            if(!(codes.indexOf(epc.getCode())!=-1)){
                BillRecord billRecord = new BillRecord(billNo + "-" + epc.getCode(), epc.getCode(), billNo, epc.getSku());
                billRecordList.add(billRecord);
            }
        }
        Business business = BillConvertUtil.covertToTransferOrderBusinessOut(transferOrderBill, transferOrderBillDtlList, epcList, currentUser);
        MessageBox messageBox = this.transferOrderBillService.saveBusiness(transferOrderBill, transferOrderBillDtlList, business,billRecordList);
        if(messageBox.getSuccess()){
            return new MessageBox(true,"出库成功");
        }else{
            return messageBox;
        }

    }

    /**
     * Wang Yushen
     * 将前端穿回的 billNo 和 EpcList 转换为入库信息，并存入数据库
     * @param billNo
     * @param strEpcList 前端穿回的JSON字符串
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/convertIn")
    @ResponseBody
    public MessageBox convertIn(String billNo, String strEpcList, String userId) throws Exception {
        List<TransferOrderBillDtl> transferOrderBillDtlList = this.transferOrderBillService.findBillDtlByBillNo(billNo);
        List<Epc> epcList = JSON.parseArray(strEpcList, Epc.class);
        User currentUser = CacheManager.getUserById(userId);
        TransferOrderBill transferOrderBill = this.transferOrderBillService.get("billNo", billNo);
        Business business = BillConvertUtil.covertToTransferOrderBusinessIn(transferOrderBill, transferOrderBillDtlList, epcList, currentUser);
        //MessageBox messageBox = this.transferOrderBillService.saveBusiness(transferOrderBill, transferOrderBillDtlList, business);
        MessageBox messageBox =null;
        //判断单据是否有销售单和判断
        Setting setting = this.settingService.get("id", "isAutoSaleOut");
        if(CommonUtil.isNotBlank(transferOrderBill.getSrcBillNo())&&transferOrderBill.getSrcBillNo().indexOf("SO")!=-1){
            User user = this.getCurrentUser();
            messageBox = this.transferOrderBillService.saveBusinessOnHaveSaleNo(transferOrderBill, transferOrderBillDtlList, business,epcList,user,setting);
        }else{
            messageBox = this.transferOrderBillService.saveBusiness(transferOrderBill, transferOrderBillDtlList, business,null);
        }
        if(messageBox.getSuccess()){
            return new MessageBox(true,"入库成功");
        }else{
            return messageBox;
        }

    }
    /**
     * 调拨单A4打印模块
     * @param billNo 单据编号
     * @Author Alvin 2018-3-27
     * */
    @RequestMapping(value = "/printA4Info")
    @ResponseBody
    public MessageBox printA4Info(String billNo,String ruleReceipt,String type){
        try {
            //Print print = this.printService.findPrint(Long.parseLong("42"));//打印Id需要优化不能些定值
            User currentUser = this.getCurrentUser();
            PrintSet printSet = this.printSetService.findPrintSet(ruleReceipt, type, currentUser.getOwnerId());
            TransferOrderBill bill = this.transferOrderBillService.load(billNo);
            List<TransferOrderBillDtl> dtlList = this.transferOrderBillService.findBillDtlByBillNo(billNo);
            Map<String, TransferOrderBillDtl> map = new HashMap<>();
            Map<String, Object> resultMap = new HashMap<>();
            for (TransferOrderBillDtl dtl : dtlList) {
                //按款汇总调拨单明细
                if (map.containsKey(dtl.getStyleId())) {
                    TransferOrderBillDtl billDtl = map.get(dtl.getStyleId());
                    billDtl.setQty(billDtl.getQty() + dtl.getQty());
                    billDtl.setInQty(billDtl.getInQty() + dtl.getInQty());
                    billDtl.setOutQty(billDtl.getOutQty() + dtl.getOutQty());
                    map.put(billDtl.getStyleId(), billDtl);
                } else {
                    Style sty = CacheManager.getStyleById(dtl.getStyleId());
                    TransferOrderBillDtl billDtl = new TransferOrderBillDtl();
                    billDtl.setStyleId(dtl.getStyleId());
                    billDtl.setColorId(dtl.getColorId());
                    billDtl.setSizeId(dtl.getSizeId());
                    billDtl.setQty(dtl.getQty());
                    billDtl.setInQty(dtl.getInQty());
                    billDtl.setOutQty(dtl.getOutQty());
                    billDtl.setStyleName(sty.getStyleName());
                    billDtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
                    billDtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
                    billDtl.setPrice(dtl.getPrice());
                    PropertyKey key = CacheManager.getPropertyKey("C1" + "-" + sty.getClass1());
                    if (CommonUtil.isNotBlank(key)) {
                        billDtl.setSupplierName(key.getName());
                    }
                    map.put(dtl.getStyleId(), billDtl);
                }
            }
            resultMap.put("print", printSet);
            resultMap.put("bill", bill);
            resultMap.put("dtl", new ArrayList(map.values()));
            return new MessageBox(true, "ok", resultMap);
        }catch (Exception e){
            return new MessageBox(false,"获取数据失败"+e.getMessage());
        }
    }
    /**
     * 调拨单A4(有尺寸)打印模块
     * @param billNo 单据编号
     * @Author czf 2018-7-24
     * */
    @RequestMapping(value = "/printA4SizeInfo")
    @ResponseBody
    public MessageBox printA4SizeInfo(String billNo,String ruleReceipt,String type){
        try {
            //Print print = this.printService.findPrint(Long.parseLong("42"));//打印Id需要优化不能些定值
            User currentUser = this.getCurrentUser();
            PrintSet printSet = this.printSetService.findPrintSet(ruleReceipt, type, currentUser.getOwnerId());
            Map<String, Object> map = this.printSetService.printMessageA4(printSet.getId()+"", billNo);
            return new MessageBox(true, "查询成功",map);
        }catch (Exception e){
            return new MessageBox(false,"获取数据失败"+e.getMessage());
        }
    }
    @RequestMapping(value = "/findFloorallocationAndSku")
    @ResponseBody
    public List<FloorallocationAndSku> findFloorallocationAndSku(String billNo){
        List<TransferOrderBillDtl> transferOrderBillDtls = this.transferOrderBillService.findBillDtlByBillNo(billNo);
        TransferOrderBill transferOrderBill = this.transferOrderBillService.get("id", billNo);
        List<FloorallocationAndSku> floorallocationAndSkus = this.transferOrderBillService.findFloorallocationAndSku(transferOrderBillDtls, transferOrderBill);
        for(FloorallocationAndSku floorallocationAndSku:floorallocationAndSkus){
            String floorallocation = floorallocationAndSku.getFloorallocation();
            if(CommonUtil.isNotBlank(floorallocation)){
                String[] floorallocationArray = floorallocation.split("-");
                String unitname=CacheManager.getUnitById(floorallocationArray[0]).getName();
                floorallocationAndSku.setFloorallocation(unitname+"仓库-"+floorallocationArray[1]+"货架-"+floorallocationArray[2]+"货层-"+floorallocationArray[2]+"货位");
            }
        }
        return floorallocationAndSkus;
    }
    @RequestMapping(value = "/findResourceButton")
    @ResponseBody
    public MessageBox findResourceButton(){
        try {
            Resource resource = this.resourceService.get("url", "logistics/transferOrder");
            List<ResourcePrivilege> resourcePrivilege = this.resourcePrivilegeService.findResourceButtonByCodeAndRoleId(resource.getCode(), this.getCurrentUser().getRoleId(),"button");
            return new MessageBox(true, "查询成功", resourcePrivilege);
        }catch (Exception e){
            e.printStackTrace();
            return new MessageBox(true, "查询失败");
        }
    }
    @RequestMapping(value = "/findTransferBillNo")
    @ResponseBody
    public List<TransferOrderBill> findTransferBillNo(String billno){
        try{
            List<TransferOrderBill> transferBillNo = this.transferOrderBillService.findTransferBillNo(billno);
            return transferBillNo;
        }catch (Exception e){
            return null;
        }
    }
}