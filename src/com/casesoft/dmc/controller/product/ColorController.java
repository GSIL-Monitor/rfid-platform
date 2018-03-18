package com.casesoft.dmc.controller.product;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.product.Color;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.product.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/prod/color")
public class ColorController extends BaseController implements IBaseInfoController<Color> {


    @Autowired
    private ColorService colorService;


    @Override
    @RequestMapping(value = "/index")
    public String index() {

        return "/views/prod/color";
    }


    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<Color> findPage(Page<Color> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.colorService.findPage(page, filters);
        return page;
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    @Override
    public List<Color> list() throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.
                getRequest());
        List<Color> colors = this.colorService.find(filters);
        return colors;
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    @Override
    public MessageBox save(Color color) throws Exception {
        this.logAllRequestParams();
        Color col = CacheManager.getColorById(color.getColorId());
        if (CommonUtil.isBlank(col)) {
            col = new Color(color.getColorName(),color.getColorName(),color.getColorName());
            col.setIsUse("Y");
        }
        User u = getCurrentUser();
        col.setOprId(u.getCode());
        col.setColorName(color.getColorName());
        //col.setColorId(color.getColorId());
        col.setHex(color.getHex());
        col.setUpdateTime(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        this.colorService.save(col);
        CacheManager.refreshColorCache();
        return returnSuccessInfo("ok");
    }

    @RequestMapping(value = "/changeColorStatus")
    @ResponseBody
    public MessageBox changeColorStatus(String colorId,String status){
        this.logAllRequestParams();
        try{
            Color col = CacheManager.getColorById(colorId);
            col.setIsUse(status);
            this.colorService.save(col);
            return returnSuccessInfo("更改成功");
        }catch(Exception e){
            e.printStackTrace();
            return returnFailInfo("更改失败");
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


}
