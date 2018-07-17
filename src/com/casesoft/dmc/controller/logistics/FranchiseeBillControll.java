package com.casesoft.dmc.controller.logistics;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.SaleOrderBill;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.Record;
import com.casesoft.dmc.service.logistics.FranchiseeBillService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by Administrator on 2017/10/18.
 */
@Controller
@RequestMapping("/logistics/franchisee")
public class FranchiseeBillControll extends BaseController implements ILogisticsBillController<SaleOrderBill> {
    @Autowired
    private FranchiseeBillService franchiseeBillService;
    @Autowired
    private UnitService unitService;
    @Override
    public Page<SaleOrderBill> findPage(Page<SaleOrderBill> page) throws Exception {
        return null;
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
        if(!id.equals("admin")){
            PropertyFilter filter = new PropertyFilter("EQS_destUnitId", ownerId);
            filters.add(filter);
        }
        page.setPageProperty();
        page = this.franchiseeBillService.findPage(page, filters);
        return page;
    }

    @Override
    public List<SaleOrderBill> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(String bill, String strDtlList, String userId) throws Exception {
        return null;
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    @Override
    public ModelAndView add() throws Exception {
        ModelAndView mv = new ModelAndView("/views/logistics/franchiseeBillDetail");

        Unit unit = CacheManager.getUnitByCode(getCurrentUser().getOwnerId());
        String defaultWarehId = unit.getDefaultWarehId();
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        mv.addObject("userId",getCurrentUser().getId());
        mv.addObject("defaultWarehId", defaultWarehId);
        mv.addObject("pageType", "add");
        mv.addObject("mainUrl", "/logistics/franchisee/index.do");
        return mv;
    }

    @RequestMapping(value = "/edit")
    @ResponseBody
    @Override
    public ModelAndView edit(String billNo) throws Exception {
        SaleOrderBill saleOrderBill = this.franchiseeBillService.get("billNo", billNo);
        ModelAndView mv = new ModelAndView("/views/logistics/franchiseeBillDetail");
        mv.addObject("ownerId",getCurrentUser().getOwnerId());
        mv.addObject("userId",getCurrentUser().getId());
        mv.addObject("pageType", "edit");
        mv.addObject("saleOrderBill", saleOrderBill);
        mv.addObject("roleid", getCurrentUser().getRoleId());
        mv.addObject("Codes", getCurrentUser().getCode());
        mv.addObject("mainUrl", "/logistics/franchisee/index.do");
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

    @Override
    public MessageBox cancel(String billNo) throws Exception {
        return null;
    }

    @Override
    public MessageBox convert(String strDtlList, String recordList) throws Exception {
        return null;
    }

    @Override
    public String index() {
        return null;
    }
    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {
        ModelAndView mv = new ModelAndView("/views/logistics/franchiseeBillNew");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        mv.addObject("userId", getCurrentUser().getId());
        Unit unit = this.unitService.getunitbyId(getCurrentUser().getOwnerId());
        mv.addObject("deportId", unit.getDefaultWarehId());
        mv.addObject("roleid", getCurrentUser().getRoleId());
        mv.addObject("deportName", unit.getName());
        return mv;
    }
    @RequestMapping(value = "/codeDetail")
    @ResponseBody
    public List<Record> recordPages(String billNo)throws Exception{
        this.logAllRequestParams();
        List<Business> businessList=this.franchiseeBillService.findBusiness(billNo);
        //拼接taskid
        String taskid="";
        for(int i=0;i<businessList.size();i++){
            Business business = businessList.get(i);
            if(i==0){
                taskid+=business.getId();
            }else{
                taskid+=","+business.getId();
            }
        }
        List<Record> recordList=this.franchiseeBillService.findRecord(taskid);
       for(Record dtl : recordList) {
           List<EpcStock> epcStock = this.franchiseeBillService.findEpcStock(dtl.getCode());
           if(epcStock.get(0).getInStock()==1){
               dtl.setOnlibrary("在库");
           }else{
               dtl.setOnlibrary("不在库");
           }
           dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
        }
        return recordList;
    }
}
