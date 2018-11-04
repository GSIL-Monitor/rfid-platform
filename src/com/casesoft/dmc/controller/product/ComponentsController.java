package com.casesoft.dmc.controller.product;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.PinyinTool;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.product.ComponentsProduct;
import com.casesoft.dmc.model.shop.Components;
import com.casesoft.dmc.service.product.ComponentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by lly on 2018/11/3.
 */
@Controller
@RequestMapping("/prod/components")
public class ComponentsController extends BaseController implements IBaseInfoController<Components>{
    @Autowired
    private ComponentsService componentsService;

    @RequestMapping("/index")
    @Override
    public String index() {
        return "/views/prod/pageComponents";
    }



    @Override
    @RequestMapping(value = "/page")
    @ResponseBody
    public Page<Components> findPage(Page<Components> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.componentsService.findPage(page, filters);
        return page;
    }

    @Override
    public List<Components> list() throws Exception {
        return null;
    }

    @Override
    @RequestMapping(value = "/save")
    @ResponseBody
    public MessageBox save(Components entity) throws Exception {
        try {
            PinyinTool tool = new PinyinTool();//汉字转换拼音工具
            entity.setId(UUID.randomUUID().toString());
            entity.setCode(tool.toPinYin(entity.getName(),"", PinyinTool.Type.LOWERCASE));
            if(CommonUtil.isBlank(entity.getParentId())){
                entity.setDeep(0);
            }
            else {
                entity.setDeep(1);
            }
            entity.setCreateDate(new Date());
            entity.setCreaterId(this.getCurrentUser().getId());
            componentsService.save(entity);
            return this.returnSuccessInfo("保存成功", entity);
        }catch (Exception e){
            e.printStackTrace();
            return this.returnFailInfo("保存失败");
        }
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
    @RequestMapping(value = {"/searchByDeep","/searchByDeepWS"})
    @ResponseBody
    public List<Components> searchByLevel(Integer deep) throws Exception {
        this.logAllRequestParams();
        List<Components> ComponentList = this.componentsService.getComponents(deep);
        return ComponentList;
    }
    @RequestMapping(value = {"/findByRemark","/findByRemarkWS"})
    @ResponseBody
    public List<Components> findByRemark(String term,String parentCode) throws Exception {
        this.logAllRequestParams();
        List<Components> components = this.componentsService.findByRemark(term,parentCode);
        return components;
    }
    @RequestMapping(value = {"/findByStyleId","/findByStyleIdWS"})
    @ResponseBody
    public List<ComponentsProduct> findByStyleId() throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        List<ComponentsProduct> components = this.componentsService.findByStyleId(filters);
        return components;
    }
}
