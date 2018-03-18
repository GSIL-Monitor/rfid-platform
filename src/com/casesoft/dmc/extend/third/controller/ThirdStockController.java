package com.casesoft.dmc.extend.third.controller;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.model.ThirdStock;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.extend.third.service.ThirdStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by john on 2017-02-22.
 */
@RequestMapping("/third/stock")
@Controller
public class ThirdStockController extends BaseController {
    @Autowired
    private ThirdStockService thirdStockService;
    @Override
    public String index() {
        return null;
    }
    @ResponseBody
    @RequestMapping("/listWS")
    public DataResult read(@RequestBody RequestPageData<ThirdStock> request) {
        return thirdStockService.find(request);
    }
}
