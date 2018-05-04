package com.casesoft.dmc.controller.search;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.search.FindSkuInformation;
import com.casesoft.dmc.model.search.PurchaseSaleStock;
import com.casesoft.dmc.service.search.FindSkuInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by Administrator on 2018/5/3.
 */
@RequestMapping(value = "/search/findSkuInformation")
@Controller
public class FindSkuInformationController extends BaseController implements IBaseInfoController<FindSkuInformation> {
    @Autowired
    private FindSkuInformationService  findSkuInformationService;
    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<FindSkuInformation> findPage(Page<FindSkuInformation> page) throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page= this.findSkuInformationService.findPagePro(page, filters);

        return page;
    }

    @Override
    public List<FindSkuInformation> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(FindSkuInformation entity) throws Exception {
        return null;
    }

    @Override
    public MessageBox edit(String taskId) throws Exception {
        return null;
    }

    @Override
    public MessageBox delete(String taskId) throws Exception {
        return null;
    }

    @Override
    public void exportExcel() throws Exception {

    }

    @Override
    public MessageBox importExcel(MultipartFile file) throws Exception {
        return null;
    }
    @RequestMapping(value = "/index")
    @Override
    public String index() {
        return "/views/search/findSkuInformationSearch";
    }


}
