package com.casesoft.dmc.extend.api.web.hub;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.task.TaskUtil;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.secret.EpcSecretUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.model.cfg.Device;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.model.tag.Epc;
import com.casesoft.dmc.service.stock.EpcStockService;
import com.casesoft.dmc.service.stock.GuardingRecordService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/9/8.
 */

@Controller
@RequestMapping(value = "/api/hub/stock", method = {RequestMethod.POST, RequestMethod.GET})
@Api(description = "库存防盗接口")
public class EpcStockApiControllor extends ApiBaseController {
    @Override
    public String index() {
        return null;
    }

    @Autowired
    private EpcStockService epcStockService;

    @Autowired
    private GuardingRecordService guardingRecordService;

    @RequestMapping(value = "/isNormalWS.do")
    @ResponseBody
    public MessageBox isNormalWS(String codeList, String deviceId) {
        this.logAllRequestParams();
        List<String> uniqueCodeList = new ArrayList<>();
        for (String code : codeList.split(",")) {
            if (code.length() != 13) {
                String epcCode = code.toUpperCase();
                code = EpcSecretUtil.decodeEpc(epcCode).substring(0, 13);
            }
            uniqueCodeList.add(code);
        }
        Device device = CacheManager.getDeviceByCode(deviceId);
        if (CommonUtil.isBlank(device)) {
            return new MessageBox(false, "设备号不存在");
        }
        List<EpcStock> epcStockList = this.epcStockService.findAlertEpcInCodes(device.getStorageId(), TaskUtil.getSqlStrByList(uniqueCodeList, EpcStock.class, "code"));
        for (EpcStock epcStock : epcStockList) {
            epcStock.setIsAlert("Y");
            epcStock.setUnicode(epcStock.getCode());
            Style s = CacheManager.getStyleById(epcStock.getStyleId());
            if(CommonUtil.isNotBlank(s)){
                epcStock.setPrice(s.getPrice());
            }else{
                epcStock.setPrice(0D);
            }
        }
        this.guardingRecordService.saveRecord(uniqueCodeList, device, epcStockList);

        if (CommonUtil.isNotBlank(epcStockList)) {
            return new MessageBox(true, "存在不能出库的唯一码", epcStockList);
        } else {
            List<EpcStock> rightStockList = this.epcStockService.findCodes(device.getStorageId(), TaskUtil.getSqlStrByList(uniqueCodeList, EpcStock.class, "code"));
            if(CommonUtil.isNotBlank(rightStockList)){
                for (EpcStock epcStock : rightStockList) {
                    epcStock.setIsAlert("N");
                    epcStock.setUnicode(epcStock.getCode());
                    Style s = CacheManager.getStyleById(epcStock.getStyleId());
                    if(CommonUtil.isNotBlank(s)){
                        epcStock.setPrice(s.getPrice());
                    }else{
                        epcStock.setPrice(0D);
                    }
                }
                return new MessageBox(true, "样衣状态正常",rightStockList);
            }else{
                List<Epc> epcList = this.epcStockService.findTagEpcByCodes(TaskUtil.getSqlStrByList(uniqueCodeList, Epc.class, "code"));
                List<EpcStock> epcStocks = new ArrayList<>();
                for (Epc epc : epcList) {
                    EpcStock epcStock = new EpcStock();
                    epcStock.setId(epc.getCode());
                    epcStock.setCode(epc.getCode());
                    epcStock.setUnicode(epc.getCode());
                    epcStock.setSku(epc.getSku());
                    epcStock.setStyleId(epc.getStyleId());
                    epcStock.setColorId(epc.getColorId());
                    epcStock.setSizeId(epc.getSizeId());
                    epcStock.setIsAlert("N");
                    Style s = CacheManager.getStyleById(epcStock.getStyleId());
                    if(CommonUtil.isNotBlank(s)){
                        epcStock.setPrice(s.getPrice());
                    }else{
                        epcStock.setPrice(0D);
                    }
                    epcStocks.add(epcStock);

                }
                if(CommonUtil.isNotBlank(epcList)){
                    return new MessageBox(true, "样衣状态正常",epcStocks);
                }else{
                    EpcStock epcStock = new EpcStock();
                    epcStock.setId(codeList);
                    epcStock.setCode(codeList);
                    epcStock.setUnicode(codeList);
                    epcStock.setIsAlert("N");
                    List<EpcStock> resultList = new ArrayList<>();
                    resultList.add(epcStock);
                    return new MessageBox(true, "样衣不存在",resultList);
                }

            }
        }
    }
}
