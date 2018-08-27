package com.casesoft.dmc.extend.api.wechatShop;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.json.JSONUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.extend.api.wechatShop.model.Wxshoppramer;
import com.casesoft.dmc.model.product.PaymentMessage;
import com.casesoft.dmc.service.search.DetailStockskuViewService;
import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/8.
 */
@Controller
@RequestMapping(value = "/api/wechatShop/warehStock", method = {RequestMethod.POST, RequestMethod.GET})
@Api(description="微信商城库存")
public class WxChatShopStockController extends ApiBaseController {
    @Autowired
    private DetailStockskuViewService detailStockskuViewService;
    @Override
    public String index() {
        return null;
    }
    @RequestMapping(value = "/findSkuStock")
    @ResponseBody
    public MessageBox findSkuStock(String datas,String paymentDates){
        //String a="[{'sku':'KE12026卡其S','weraId':'B4253','qty':'1' ,'Price':'100','totPrice':'100'},{'sku':'s22179白色L','weraId':'AUTO_WH003','qty':'1','Price':'100','totPrice':'100'}]";
        PaymentMessage paymentMessage= JSON.parseObject(paymentDates,PaymentMessage.class);
       List <Wxshoppramer>list = JSONUtil.getJsonStr2List(datas, Wxshoppramer.class);
        List<Map<String, Object>> skuStock = this.detailStockskuViewService.findSkuStock(paymentMessage,list, "", "", "100");
        Map<String, Object> stringObjectMap = skuStock.get(0);
        Object mes = stringObjectMap.get("mes");
        if(CommonUtil.isNotBlank(mes)){
            return returnFailInfo("库存不足", skuStock);
        }else{
            return returnSuccessInfo("保存成功", skuStock);
        }
    }
    @RequestMapping(value = "/updateSalestate")
    @ResponseBody
    public MessageBox updateSalestate(String dates){
        List <PaymentMessage>list = JSONUtil.getJsonStr2List(dates, PaymentMessage.class);

       this.detailStockskuViewService.updateSalestate(list);
        return returnSuccessInfo("确认收款");
    }
}
