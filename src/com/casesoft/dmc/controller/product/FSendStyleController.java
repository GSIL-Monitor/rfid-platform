package com.casesoft.dmc.controller.product;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.product.NoPushStyle;
import com.casesoft.dmc.service.product.NoPushStyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */
@Controller
@RequestMapping("/sys/PushStyle")
public class FSendStyleController  extends BaseController implements IBaseInfoController<NoPushStyle> {
    @Autowired
    private NoPushStyleService noPushStyleService;
    @Override
    @RequestMapping("/page")
    @ResponseBody
    public Page<NoPushStyle> findPage(Page<NoPushStyle> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.noPushStyleService.findPage(page,filters);
        return page;
    }

    @Override
    public List<NoPushStyle> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(NoPushStyle entity) throws Exception {
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

    @Override
    @RequestMapping(value = "/index")
    public String index() {
        return "views/syn/FSendStyle";
    }
}
