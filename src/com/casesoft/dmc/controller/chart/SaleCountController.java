package com.casesoft.dmc.controller.chart;


import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.dao.search.DetailSaleViewDao;
import com.casesoft.dmc.model.search.DetailSaleView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by WinLi on 2017-03-14.
 */
@Controller
@RequestMapping("/chart/saleCount")
public class SaleCountController extends BaseController {
    @Autowired
    DetailSaleViewDao detailSaleViewDao;
    @Override
    @RequestMapping(value = "/index")
    public String index(){
        return "/views/chart/saleCount";
    }

    @RequestMapping(value = "/list", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public List<DetailSaleView> read() {

        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        List<DetailSaleView> detailSaleViewList = this.detailSaleViewDao.find(filters);
        for(DetailSaleView detailSaleView : detailSaleViewList) {
            detailSaleView.setWarehName(detailSaleView.getWarehId());
        }
        return detailSaleViewList;
    }

}
