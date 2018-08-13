package com.casesoft.dmc.controller.logistics;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.stock.StockUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.util.secret.EpcSecretUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.InventoryBill;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.service.logistics.InventoryBillService;
import com.casesoft.dmc.service.stock.EpcStockService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/9/8.
 */
@Controller
@RequestMapping("/logistics/inventoryBillController")
public class InventoryBillController extends BaseController implements ILogisticsBillController<InventoryBill> {

    @Autowired
    private InventoryBillService inventoryBillService;
    @Autowired
    private UnitService unitService;

    @Autowired
    private EpcStockService epcStockService;
    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<InventoryBill> findPage(Page<InventoryBill> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        String code=this.getReqParam("code");
        if(CommonUtil.isNotBlank(code)){
            if (code.length() != 13) {
                String epcCode = code.toUpperCase();
                code = EpcSecretUtil.decodeEpc(epcCode).substring(0, 13);
            }

                PropertyFilter filter = new PropertyFilter("EQS_code", code);
                filters.add(filter);

        }

        page.setPageProperty();
        page = this.inventoryBillService.findPage(page, filters);
        for (InventoryBill b:page.getRows()){
            if(CommonUtil.isNotBlank(b.getSizeId())) {
                b.setSizeName(CacheManager.getSizeNameById(b.getSizeId()));
            }
            if(CommonUtil.isNotBlank(b.getColorId())) {
                b.setColorName(CacheManager.getColorNameById(b.getColorId()));
            }
            if(CommonUtil.isNotBlank(b.getStyleId())) {
                b.setStyleName(CacheManager.getStyleNameById(b.getStyleId()));
            }


        }

        return page;

    }

    @Override
    public List<InventoryBill> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(String bill, String strDtlList, String userId) throws Exception {
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
    //@RequestMapping(value = "/index")
    @Override
    public String index() {

        return "/views/logistics/InventoryBill";
    }
    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {

        ModelAndView mv = new ModelAndView("/views/logistics/InventoryBill");
        User user = this.getCurrentUser();
        mv.addObject("ownerId", user.getOwnerId());
        return mv;
    }
    @RequestMapping(value = "/checkEpcStock")
    @ResponseBody
    public MessageBox checkEpcStock(String code){
        if (code.length() != 13) {
            String epcCode = code.toUpperCase();
            code = EpcSecretUtil.decodeEpc(epcCode).substring(0, 13);
        }
        EpcStock epcStock = this.inventoryBillService.checkEpcStock(code);
        if (CommonUtil.isBlank(epcStock)) {
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
                User currentUser = (User) this.getSession().getAttribute(Constant.Session.User_Session);
                epcStock.setWarehouseId(currentUser.getAddressId());
                StockUtil.convertEpcStock(epcStock);
                this.epcStockService.save(epcStock);
            } else {
                return new MessageBox(false, "唯一码:" + code + "不能入库");
            }
        }else{
            Unit unit = this.unitService.getunitbyId(epcStock.getWarehouseId());
            epcStock.setStorage(unit.getName());
        }
        return new MessageBox(true, "", epcStock);
    }
    @RequestMapping(value = "/checkconsignmentEpcStock")
    @ResponseBody
    public MessageBox checkconsignmentEpcStock(String code){
        if (code.length() != 13) {
            String epcCode = code.toUpperCase();
            code = EpcSecretUtil.decodeEpc(epcCode).substring(0, 13);
        }
        EpcStock epcStock = this.inventoryBillService.checkEpcStock(code);

        if (CommonUtil.isBlank(epcStock)) {
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
                User currentUser = (User) this.getSession().getAttribute(Constant.Session.User_Session);
                epcStock.setWarehouseId(currentUser.getAddressId());
                StockUtil.convertEpcStock(epcStock);
                this.epcStockService.save(epcStock);
            } else {
                return new MessageBox(false, "唯一码:" + code + "不能入库,或不在所属仓库");
            }
        }else{
            if(0==epcStock.getInStock()){
                return new MessageBox(false, "唯一码:" + code + "不在库");
            }else{
                Unit unit = this.unitService.getunitbyId(epcStock.getWarehouseId());
                epcStock.setStorage(unit.getName());
            }

        }
        return new MessageBox(true, "", epcStock);
    }
    @RequestMapping(value = "/checkinventoryEpcStock")
    @ResponseBody
    public MessageBox checkinventoryEpcStock(String code,String ownerId){
        if (code.length() != 13) {
            String epcCode = code.toUpperCase();
            code = EpcSecretUtil.decodeEpc(epcCode).substring(0, 13);
        }
        EpcStock epcStock = this.inventoryBillService.checkEpcStockNew(code,ownerId);
        if (CommonUtil.isBlank(epcStock)) {
            Epc tagEpc = this.epcStockService.findTagEpcByCodeNew(code,ownerId);
            if (CommonUtil.isNotBlank(tagEpc)) {
                epcStock = new EpcStock();
                epcStock.setId(code);
                epcStock.setCode(code);
                epcStock.setSku(tagEpc.getSku());
                epcStock.setStyleId(tagEpc.getStyleId());
                epcStock.setColorId(tagEpc.getColorId());
                epcStock.setSizeId(tagEpc.getSizeId());
                epcStock.setInStock(0);
                User currentUser = (User) this.getSession().getAttribute(Constant.Session.User_Session);
                epcStock.setWarehouseId(currentUser.getAddressId());
                StockUtil.convertEpcStock(epcStock);
                this.epcStockService.save(epcStock);
            } else {
                return new MessageBox(false, "唯一码:" + code + "不能入库");
            }
        }else{
           /* if(0==epcStock.getInStock()){
                return new MessageBox(false, "唯一码:" + code + "不在库");
            }else{*/
                Unit unit = this.unitService.getunitbyId(epcStock.getWarehouseId());
                epcStock.setStorage(unit.getName());
         /*   }*/

        }
        return new MessageBox(true, "", epcStock);
    }
    @RequestMapping(value = "/saveInventory")
    @ResponseBody
    public MessageBox saveInventory(String codes){
        try{
            User currentUser = (User) this.getSession().getAttribute(Constant.Session.User_Session);
            List<EpcStock> epcStockListBycodes = this.inventoryBillService.findEpcStockListBycodes(codes);
            this.inventoryBillService.saveInventory(epcStockListBycodes,currentUser);
            return new MessageBox(true, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, e.getMessage());
        }

    }
}
