package com.casesoft.dmc.controller.search;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.dao.search.BuyerKpiDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

@Controller
@RequestMapping("/search/buyerKpi")
public class BuyerKpiController extends BaseController {
    @Autowired
    private BuyerKpiDao buyerKpiDao;

    @Override
    @RequestMapping("/index")
    public String index() {
        return "/views/search/buyerKpi";
    }

    @RequestMapping("/getBuyerKpi")
    @ResponseBody
    public DataSourceResult getBuyerKpi(@RequestBody DataSourceRequest request) {
        DataSourceResult buyerKpi = null;
        try {
            buyerKpi = buyerKpiDao.getBuyerKpi(request);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buyerKpi;
    }
}
