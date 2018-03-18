package com.casesoft.dmc.controller.factory;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.DataSourceRequest;
import com.casesoft.dmc.core.controller.DataSourceResult;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.extend.third.descriptor.DataResult;
import com.casesoft.dmc.extend.third.request.RequestPageData;
import com.casesoft.dmc.model.factory.FactoryBillDtlView;
import com.casesoft.dmc.model.factory.FactoryRecord;
import com.casesoft.dmc.service.factory.FactoryBillDtlSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GuoJunwen on 2017-05-15.
 */
@Controller
@RequestMapping("/factory/billDtlSearch")
public class FactoryBillDtlSearchController extends BaseController {

    @Autowired
    private FactoryBillDtlSearchService factoryBillDtlSearchService;

    @RequestMapping("/index")
    @Override
    public String index() {
        return "views/factory/factoryBillDtlSearch";
    }

    @RequestMapping("/page")
    @ResponseBody
    public DataSourceResult read(@RequestBody DataSourceRequest request) {
        this.logAllRequestParams();
        DataSourceResult dataResult =this.factoryBillDtlSearchService.find(request);
        return dataResult;
    }

    @RequestMapping("/sign")
    @ResponseBody
    public MessageBox sign(String ids){
        ids=ids.substring(0,ids.length()-1);
        List<String> idList = new ArrayList<String>();
        for(String s:ids.split(",")){
            idList.add(s);
        }
        this.factoryBillDtlSearchService.signFactoryRecordByIds(CommonUtil.getSqlStrByList(idList, FactoryRecord.class, "id"));

        return returnSuccessInfo("ok");
    }

    @RequestMapping("/unSign")
    @ResponseBody
    public MessageBox unSign(String ids){
        ids=ids.substring(0,ids.length()-1);
        List<String> idList = new ArrayList<String>();
        for(String id:ids.split(",")){
            idList.add(id);
        }
        this.factoryBillDtlSearchService.unsignFactoryRecordByIds(CommonUtil.getSqlStrByList(idList, FactoryRecord.class, "id"));

        return returnSuccessInfo("ok");
    }

}
