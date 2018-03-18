package com.casesoft.dmc.controller.product;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.product.NoPushStyle;
import com.casesoft.dmc.model.product.PaymentMessage;
import com.casesoft.dmc.model.product.SendInventory;
import com.casesoft.dmc.service.product.SendInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by Administrator on 2018/1/13.
 */
@Controller
@RequestMapping("/product/SendInventory")
public class SendInventoryController extends BaseController implements IBaseInfoController<SendInventory> {
    @Autowired
    private SendInventoryService sendInventoryService;
    @Override
    @RequestMapping("/page")
    @ResponseBody
    public Page<SendInventory> findPage(Page<SendInventory> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.sendInventoryService.findPage(page,filters);

        return page;
    }

    @Override
    public List<SendInventory> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(SendInventory entity) throws Exception {
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

    @Override
    @RequestMapping(value = "/index")
    public String index() {
        return "views/syn/SendInventory";
    }
    @RequestMapping("/WxShopStocke")
    @ResponseBody
    public void WxShopStocke(){
        try {
            this.sendInventoryService.sendNoInventory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("/findPaymentMessage")
    @ResponseBody
    public List<PaymentMessage> findPaymentMessage(String billno){
        List<PaymentMessage> list= this.sendInventoryService.findPaymentMessage(billno);
        return list;
    }
    @RequestMapping("/sendStreamNO")
    @ResponseBody
    public MessageBox sendStreamNO(String StreamNO,String billNo){
        String message = this.sendInventoryService.sendStreamNO(StreamNO, billNo);
        return this.returnSuccessInfo("保存成功", message);
    }
}
