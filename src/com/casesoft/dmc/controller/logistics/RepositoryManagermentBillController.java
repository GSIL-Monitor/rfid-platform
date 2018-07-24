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
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.rem.RepositoryManagementBill;
import com.casesoft.dmc.model.rem.RepositoryManagementBillDtl;
import com.casesoft.dmc.model.rem.UniqueCodeBill;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.sys.*;
import com.casesoft.dmc.service.rem.RepositoryManagementBillDtlService;
import com.casesoft.dmc.service.rem.RepositoryManagementBillService;
import com.casesoft.dmc.service.rem.UniqueCodeBillService;
import com.casesoft.dmc.service.stock.EpcStockService;
import com.casesoft.dmc.service.sys.ResourceButtonService;
import com.casesoft.dmc.service.sys.impl.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lly on 2018/7/20.
 */
@Controller
@RequestMapping(value = "logistics/repositoryAdjust")
public class RepositoryManagermentBillController extends BaseController implements ILogisticsBillController<RepositoryManagementBill> {
    @Autowired
    private UniqueCodeBillService uniqueCodeBillService;
    @Autowired
    private RepositoryManagementBillService repositoryManagementBillService;
    @Autowired
    private RepositoryManagementBillDtlService repositoryManagementBilllDtlService;
    @Autowired
    private EpcStockService epcStockService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ResourceButtonService resourceButtonService;

    @Override
    public String index() {
        return "/views/logistics/repositoryAdjust";
    }

    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {

        ModelAndView mv = new ModelAndView("/views/logistics/repositoryAdjust");
        mv.addObject("pageType", "add");
        User user = this.getCurrentUser();
        mv.addObject("ownerId", user.getOwnerId());
        mv.addObject("user", getCurrentUser());
        Unit unit = CacheManager.getUnitByCode(getCurrentUser().getOwnerId());
        String defaultWarehId = unit.getDefaultWarehId();
        mv.addObject("roleid", getCurrentUser().getRoleId());
        mv.addObject("defaultWarehId", defaultWarehId);
        return mv;
    }

    @Override
    public Page<RepositoryManagementBill> findPage(Page<RepositoryManagementBill> page) throws Exception {
        return null;
    }

    @Override
    public List<RepositoryManagementBill> list() throws Exception {
        return null;
    }


    @Override
    public ModelAndView add() throws Exception {
        return null;
    }

    @Override
    public ModelAndView edit(String billNo) throws Exception {
        return null;
    }

    @Override
    public MessageBox check(String billNo) throws Exception {
        return null;
    }



    /**
     * @param strDtlList
     * @param recordList
     * @Param String strDtlList 单据明细 jsonArryList字符串
     * @Param String recordList 唯一码明细 jsonArryList字符串
     */
    @Override
    public MessageBox convert(String strDtlList, String recordList) throws Exception {
        return null;
    }
    //记录商品原库位
    private MessageBox saveOldRm(String billNo,String sku,String code,String rmId,String warehouseId,String userId){
        UniqueCodeBill uniqueCodeBill = new UniqueCodeBill();
        uniqueCodeBill.setBillNo(billNo);
        uniqueCodeBill.setSku(sku);
        uniqueCodeBill.setId(new GuidCreator().toString());
        uniqueCodeBill.setUniqueCode(code);
        uniqueCodeBill.setOldRm(rmId);
        uniqueCodeBill.setWarehouseId(warehouseId);
        uniqueCodeBill.setUserId(userId);

        try {
            uniqueCodeBillService.save(uniqueCodeBill);
            return new MessageBox(true,"记录成功!");
        }catch (Exception e){
            e.printStackTrace();
            return new MessageBox(false,"记录失败!");
        }
    }
    @RequestMapping(value = {"/findBillDtl","/findBillDtlWS"})
    @ResponseBody
    public List<RepositoryManagementBillDtl> findBillDtl(String billNo) throws Exception {
        this.logAllRequestParams();
        List<RepositoryManagementBillDtl> repositoryManagementBillDtls = this.repositoryManagementBilllDtlService.findBillDtlByBillNo(billNo);
        //根据单据号获得唯一码
        List<PropertyFilter> filters = new ArrayList<>();
        PropertyFilter filter = new PropertyFilter("EQS_billNo", billNo);
        filters.add(filter);
        List<UniqueCodeBill> uniqueCodeBills = uniqueCodeBillService.find(filters);
        Map<String, String> codeMap = new HashMap<>();
        for (UniqueCodeBill r : uniqueCodeBills) {
            if (codeMap.containsKey(r.getSku())) {
                //不判断会根据记录数拼同样的code
                if(!r.getUniqueCode().equals(codeMap.get(r.getSku()))){
                    String code = codeMap.get(r.getSku());
                    code += "," + r.getUniqueCode();
                    codeMap.put(r.getSku(), code);
                }
            } else {
                codeMap.put(r.getSku(), r.getUniqueCode());
            }
        }
        for (int i = 0; i < repositoryManagementBillDtls.size(); i++) {
            RepositoryManagementBillDtl dtl = repositoryManagementBillDtls.get(i);
            Style style = CacheManager.getStyleById(dtl.getStyleId());
            dtl.setStyleName(style.getStyleName());
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
            Map<String,Double> stylePriceMap = new HashMap<>();
            stylePriceMap.put("price",style.getPrice());
            stylePriceMap.put("wsPrice",style.getWsPrice());
            stylePriceMap.put("puPrice",style.getPuPrice());
            dtl.setStylePriceMap(JSON.toJSONString(stylePriceMap));
            if (codeMap.containsKey(dtl.getSku())) {
                dtl.setUniqueCodes(codeMap.get(dtl.getSku()));
            }
        }
        return repositoryManagementBillDtls;
    }
    @RequestMapping(value = {"/save","/saveWS"})
    @ResponseBody
    @Override
    public MessageBox save(String rmAdjustBillStr, String strDtlList, String userId) throws Exception {
        this.logAllRequestParams();
        try {
            Long totQty = 0L;
            RepositoryManagementBill repositoryManagementBill = JSON.parseObject(rmAdjustBillStr, RepositoryManagementBill.class);
            repositoryManagementBill.setOrigName(request.getParameter("origName"));
            if (CommonUtil.isBlank(repositoryManagementBill.getBillNo())) {
                String prefix = BillConstant.BillPrefix.rmADjust
                        + CommonUtil.getDateString(new Date(), "yyMMddHHmmssSSS");
                repositoryManagementBill.setId(prefix);
                repositoryManagementBill.setBillNo(prefix);
            } else {
                //查询单据状态
                Integer status = this.repositoryManagementBillService.findBillStatus(repositoryManagementBill.getBillNo());
                if (status != Constant.ScmConstant.BillStatus.saved && !userId.equals("admin")) {
                    return new MessageBox(false, "单据不是录入状态无法保存,请返回");
                }

            }
            List<RepositoryManagementBillDtl> repositoryManagementBillDtls = JSON.parseArray(strDtlList, RepositoryManagementBillDtl.class);
            User curUser = CacheManager.getUserById(userId);
            if (repositoryManagementBillDtls.size() > 0)
            repositoryManagementBill.setAdjustCount(String.valueOf(repositoryManagementBillDtls.size()));
            repositoryManagementBill.setOprId(curUser.getId());
            for (RepositoryManagementBillDtl repositoryManagementBillDtl :repositoryManagementBillDtls){
                if(!CommonUtil.isNotBlank(repositoryManagementBillDtl.getId())){
                    repositoryManagementBillDtl.setId(new GuidCreator().toString());
                }
                repositoryManagementBillDtl.setBillId(repositoryManagementBill.getId());
                repositoryManagementBillDtl.setBillNo(repositoryManagementBill.getBillNo());
                repositoryManagementBillDtl.setStatus(repositoryManagementBill.getStatus());
                totQty += repositoryManagementBillDtl.getQty();


                //保存变动记录表
                this.saveOldRm(repositoryManagementBill.getBillNo(),repositoryManagementBillDtl.getSku(),repositoryManagementBillDtl.getUniqueCodes(),repositoryManagementBillDtl.getOldRmId(),repositoryManagementBill.getOrigName(),curUser.getId());
                //保存单据明细表
                repositoryManagementBilllDtlService.save(repositoryManagementBillDtl);
            }
            repositoryManagementBill.setTotQty(totQty);
            this.repositoryManagementBillService.save(repositoryManagementBill);
            return new MessageBox(true, "保存成功", repositoryManagementBill);

        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, e.getMessage());
        }
    }
    @RequestMapping(value = "/cancel")
    @ResponseBody
    @Override
    public MessageBox cancel(String billNo) throws Exception {
        RepositoryManagementBill repositoryManagementBill = this.repositoryManagementBillService.get("billNo", billNo);
        repositoryManagementBill.setStatus(BillConstant.BillStatus.Cancel);
        this.repositoryManagementBillService.save(repositoryManagementBill);
        return new MessageBox(true, "撤销成功");
    }
    @RequestMapping(value = "/rmIdChange")
    @ResponseBody
    public MessageBox rmIdChange(String billNo) throws Exception {
        RepositoryManagementBill repositoryManagementBill = this.repositoryManagementBillService.get("billNo", billNo);
        repositoryManagementBill.setStatus(BillConstant.BillStatus.adjust);//调整
        //根据单据号获得唯一码
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        PropertyFilter filter = new PropertyFilter("EQS_billNo", billNo);
        filters.add(filter);
        List<UniqueCodeBill> uniqueCodeBills = uniqueCodeBillService.find(filters);
        //修改对应商品库位
        String []rmId = null;
        if (CommonUtil.isNotBlank(repositoryManagementBill.getNewRmId())){
            rmId = repositoryManagementBill.getNewRmId().split("-");
        }
        String rackId = null;
        String levelId = null;
        String allocationId = null;
        if(CommonUtil.isNotBlank(rmId) && rmId.length == 3){
            rackId = rmId[0];
            levelId = rmId[1];
            allocationId = rmId[2];
        }
        try{
            for (UniqueCodeBill uniqueCodeBill : uniqueCodeBills){
                EpcStock epcStock = epcStockService.findStockEpcByCode(uniqueCodeBill.getUniqueCode());
                uniqueCodeBill.setNewRm(repositoryManagementBill.getNewRmId());
                //获取当前时间
                SimpleDateFormat myFmt1=new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss");
                Date now=new Date();
                String date=myFmt1.format(now);
                uniqueCodeBill.setUpdateTime(date);
                epcStock.setFloorRack(rackId);
                epcStock.setFloorArea(levelId);
                epcStock.setFloorAllocation(allocationId);
                epcStockService.save(epcStock);
                uniqueCodeBillService.save(uniqueCodeBill);
            }
            return new MessageBox(true, "调整成功");
        }catch (Exception e){
            e.printStackTrace();
            return new MessageBox(false, "调整失败");
        }
    }
    @RequestMapping(value = "/page")
    @ResponseBody
    public Page<RepositoryManagementBill> findPage(Page<RepositoryManagementBill> page, String userId) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        //权限设置，增加过滤条件，只显示当前ownerId下的库位单信息
        User CurrentUser = CacheManager.getUserById(userId);
        String ownerId = CurrentUser.getOwnerId();
        String id = CurrentUser.getId();
        if (!id.equals("admin")) {
            PropertyFilter filter = new PropertyFilter("EQS_ownerId", ownerId);
            filters.add(filter);
        }
        page.setPageProperty();
        page = this.repositoryManagementBillService.findPage(page, filters);
        return page;
    }
    @RequestMapping(value = "/end")
    @ResponseBody
    @Override
    public MessageBox end(String billNo) throws Exception {
        RepositoryManagementBill repositoryManagementBill = this.repositoryManagementBillService.get("billNo", billNo);
        repositoryManagementBill.setStatus(BillConstant.BillStatus.End);
        this.repositoryManagementBillService.update(repositoryManagementBill);
        return new MessageBox(true, "结束成功");
    }
    @RequestMapping(value = "/findResourceButton")
    @ResponseBody
    public MessageBox findResourceButton(){
        try {
            Resource resource = this.resourceService.get("url", "logistics/repositoryAdjust");
            List<ResourceButton> resourceButton = this.resourceButtonService.findResourceButtonByCodeAndRoleId(resource.getCode(), this.getCurrentUser().getRoleId(),"button");
            return new MessageBox(true, "查询成功",resourceButton);
        }catch (Exception e){
            e.printStackTrace();
            return new MessageBox(true, "查询失败");
        }
    }
    @RequestMapping(value = "/codeDetail")
    @ResponseBody
    public List<UniqueCodeBill> codeDetail(String billNo) throws Exception {
        //根据单据号获得唯一码明细
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        PropertyFilter filter = new PropertyFilter("EQS_billNo", billNo);
        filters.add(filter);
        List<UniqueCodeBill> uniqueCodeBills = uniqueCodeBillService.find(filters);
        return uniqueCodeBills;
    }
}

