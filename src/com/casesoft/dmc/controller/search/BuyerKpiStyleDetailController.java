package com.casesoft.dmc.controller.search;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.dao.search.BuyerKpiStyleDetailDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

@Controller
@RequestMapping("/search/buyerKpiStyleDetail")
public class BuyerKpiStyleDetailController extends BaseController{

    @Autowired
    private BuyerKpiStyleDetailDao buyerKpiStyleDetailDao;

    @Override
    @RequestMapping("/index")
    public String index() {
        return "/views/search/buyerKpiStyleDetail";
    }


    @RequestMapping("/viewBuyerKpiStyleDetail")
    ModelAndView viewBuyerKpiStyleDetail(String buyerId, String startDate, String endDate){
        ModelAndView mv = new ModelAndView("views/search/buyerKpiStyleDetail");
        mv.addObject("buyerId", buyerId);
        mv.addObject("startDate", startDate);
        mv.addObject("endDate", endDate);
        return mv;
    }

    @RequestMapping("/getStyleDetail")
    @ResponseBody
    public DataSourceResult getStyleDetail(@RequestBody DataSourceRequest request) {
        DataSourceResult styleDetail= null;
        try {
            styleDetail = buyerKpiStyleDetailDao.getStyleDetail(request);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return styleDetail;
    }
}
