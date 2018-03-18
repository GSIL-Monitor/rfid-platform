package com.casesoft.dmc.controller.chart;

import com.casesoft.dmc.core.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by WinLi on 2017-05-17.
 */
@Controller
@RequestMapping("/chart/saleChart")
public class SaleChartController extends BaseController {
    @Override
    @RequestMapping(value = "/index")
    public String index(){
        return "/views/chart/saleChart";
    }
}
