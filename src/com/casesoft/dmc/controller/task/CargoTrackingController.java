package com.casesoft.dmc.controller.task;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.logistics.ConsignmentBillController;
import com.casesoft.dmc.controller.logistics.SaleOrderReturnBillController;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.*;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.service.logistics.*;
import com.casesoft.dmc.service.task.CargoTrackingService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yushen on 2017/9/19.
 */
@Controller
@RequestMapping("/task/cargoTracking")
public class CargoTrackingController extends BaseController implements IBaseInfoController<Business> {

    @Autowired
    CargoTrackingService cargoTrackingService;

    @RequestMapping(value = "/index")
    @Override
    public String index() {

        return "/views/task/cargoTracking";
    }

    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<Business> findPage(Page<Business> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        return this.cargoTrackingService.findPage(page, filters);
    }

    @Override
    public List<Business> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(Business entity) throws Exception {
        return null;
    }

    @Override
    public MessageBox edit(String taskId) throws Exception {
        return null;
    }

    @Override
    public MessageBox delete(String taskId) throws Exception {
        return null;
    }

    @Override
    public void exportExcel() throws Exception {

    }

    @Override
    public MessageBox importExcel(MultipartFile file) throws Exception {
        return null;
    }

    @Autowired
    private OutboundTaskController outboundTaskController;

    @Autowired
    private InboundTaskController inboundTaskController;

    @RequestMapping(value = "/taskDetail")
    @ResponseBody
    public ModelAndView taskDetail(String id, String token) throws Exception {
        List<Integer> outTokens = Arrays.asList(10, 13, 24, 26, 28, 32);
        List<Integer> inTokens = Arrays.asList(8, 12, 23, 25, 29,11);
        if (CommonUtil.isNotBlank(token)) {
            int iToken = Integer.parseInt(token);
            if (outTokens.contains(iToken)) {
                return this.outboundTaskController.showDetail(id);
            } else if (inTokens.contains(iToken)) {
                return this.inboundTaskController.viewDtl(id);
            }
            return new ModelAndView();
        }
        return new ModelAndView();
    }

    @Autowired
    private PurchaseOrderBillService purchaseOrderBillService;

    @Autowired
    private SaleOrderBillService saleOrderBillService;

    @Autowired
    private SaleOrderReturnBillService saleOrderReturnBillService;

    @Autowired
    private TransferOrderBillService transferOrderBillService;

    @Autowired
    private ConsignmentBillService consignmentBillService;

    @RequestMapping(value = "/billDetail")
    @ResponseBody
    public ModelAndView billDetail(String billNo) throws Exception {

        String billType = billNo.substring(0, 2);

        if(billType.equals("PI")){
            PurchaseOrderBill purchaseOrderBill = this.purchaseOrderBillService.get("billNo",billNo);
            ModelAndView mv = new ModelAndView("views/logistics/purchaseOrderBillDetail");
            mv.addObject("pageType","view");
            mv.addObject("purchaseOrderBill", purchaseOrderBill);
            mv.addObject("mainUrl","/task/cargoTracking/index.do");
            mv.addObject("ownerId",getCurrentUser().getOwnerId());
            Unit unit = CacheManager.getUnitByCode(getCurrentUser().getOwnerId());
            String defaultWarehId = unit.getDefaultWarehId();
            mv.addObject("defaultWarehId",defaultWarehId);
            return mv;
        } else if (billType.equals("TR")) {
            TransferOrderBill transferOrderBill = this.transferOrderBillService.get("billNo", billNo);
            ModelAndView mv = new ModelAndView("/views/logistics/transferOrderBillDetail");
            mv.addObject("pageType", "view");
            mv.addObject("transferOrderBill", transferOrderBill);
            mv.addObject("mainUrl", "/task/cargoTracking/index.do");
            mv.addObject("ownerId", getCurrentUser().getOwnerId());
            return mv;
        } else if (billType.equals("SO")) {
            SaleOrderBill saleOrderBill = this.saleOrderBillService.get("billNo", billNo);
            ModelAndView mv = new ModelAndView("/views/logistics/saleOrderBillDetail");
            mv.addObject("ownerId", getCurrentUser().getOwnerId());
            mv.addObject("pageType", "view");
            mv.addObject("saleOrderBill", saleOrderBill);
            mv.addObject("mainUrl", "/task/cargoTracking/index.do");
            return mv;
        } else if (billType.equals("SR")) {
            SaleOrderReturnBill saleOrderReturnBill = this.saleOrderReturnBillService.findBillByBillNo(billNo);
            ModelAndView mv = new ModelAndView("/views/logistics/saleOrderReturnDetail");
            mv.addObject("ownerId", getCurrentUser().getOwnerId());
            mv.addObject("saleOrderReturn", saleOrderReturnBill);
            mv.addObject("pageType", "view");
            mv.addObject("mainUrl", "/task/cargoTracking/index.do");
            return mv;
        } else if (billType.equals("CM")) {
            ConsignmentBill consignmentBill = this.consignmentBillService.findBillByBillNo(billNo);
            ModelAndView mv = new ModelAndView("/views/logistics/consignmentBillDetail");
            mv.addObject("ownerId", getCurrentUser().getOwnerId());
            mv.addObject("consignmentBill", consignmentBill);
            mv.addObject("pageType", "edit");
            mv.addObject("mainUrl", "/task/cargoTracking/index.do");
            return mv;
        }

        return new ModelAndView();
    }
}
