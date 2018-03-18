package com.casesoft.dmc.controller.chart;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.chart.StockStatistics;
import com.casesoft.dmc.service.chart.TaskCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by WinLi on 2017-03-21.
 */
@Controller
@RequestMapping("/chart/task")
public class TaskCountController extends BaseController {
    @Autowired
    private TaskCountService taskCountService;

    @Override
    public String index() {
        return null;
    }

    @RequestMapping(value = "/vendorOutbound/index")
    public String indexVendorOutbound() {
        return "/views/chart/vendorOutbound";
    }

    public MessageBox countTask(String typeStr) {
//        this.logAllRequestParams();
//        Integer type = Integer.parseInt(typeStr);
//        List<StockStatistics> list = this.taskCountService.findTaskStatistics(type);
//        for(StockStatistics s : list){
//            s.setStorageName(CacheManager.getUnitByCode(s.getStorageId()).getName());
//        }
//        return this.returnSuccessInfo("ok", list);
        return null;
    }
}
