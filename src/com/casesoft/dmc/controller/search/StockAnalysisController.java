package com.casesoft.dmc.controller.search;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.dao.search.StockAnalysisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by yushen on 2017/11/15.
 */

@Controller
@RequestMapping("/search/stockAnalysis")
public class StockAnalysisController extends BaseController {
    @Autowired
    private StockAnalysisDao stockAnalysisDao;

    @RequestMapping(value = "/index")
    @Override
    public String index() {
        return "/views/search/stockAnalysis";
    }

    @RequestMapping(value = "/readAllStockAnalysis", method = RequestMethod.POST)
    public @ResponseBody
    DataSourceResult readAllStockAnalysis(@RequestBody DataSourceRequest request) {
        return null;
    }

    @RequestMapping(value = "/readStockAnalysis", method = RequestMethod.POST)
    public @ResponseBody DataSourceResult readStockAnalysis(@RequestBody DataSourceRequest request) {
        DataSourceResult result = null;
        try {
            result = stockAnalysisDao.getStockAnalysis(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
