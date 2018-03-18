package com.casesoft.dmc.controller.search;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.model.search.SearchBoxDtlView;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.search.BoxDetailSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by WinLi on 2017-03-30.
 */
@Controller
@RequestMapping("/search/boxDtlSearch")
public class BoxDetailSearchController extends BaseController{

    @Autowired
    private BoxDetailSearchService boxDetailSearchService;

    @RequestMapping(value = "/index")
    public String index(){
        return "/views/search/boxDetailSearch";
    }

    @RequestMapping("list")
    @ResponseBody
    public DataResult read(@RequestBody RequestPageData<SearchBoxDtlView> request){
        DataResult dataResult = this.boxDetailSearchService.find(request);
        List<SearchBoxDtlView> searchBoxDtlViewList= (List<SearchBoxDtlView>) dataResult.getData();
        for (SearchBoxDtlView searchBoxDtlView:searchBoxDtlViewList){
            searchBoxDtlView.setStyleName(CacheManager.getStyleNameById(searchBoxDtlView.getStyleId()));
            Unit dest = CacheManager.getUnitById(searchBoxDtlView.getDestId());
            if(null != dest) {
                searchBoxDtlView.setDestName(dest.getName());
            }
            Unit orig = CacheManager.getUnitById(searchBoxDtlView.getOrigId());
            if(null != orig) {
                searchBoxDtlView.setOrigName(orig.getName());
            }
        }
        dataResult.setData(searchBoxDtlViewList);
        return dataResult;
    }

}
