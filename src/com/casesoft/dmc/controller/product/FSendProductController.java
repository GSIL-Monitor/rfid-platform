package com.casesoft.dmc.controller.product;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.product.NoPushProduct;
import com.casesoft.dmc.model.product.NoPushStyle;
import com.casesoft.dmc.service.product.NoPushProductService;
import com.casesoft.dmc.service.product.NoPushStyleService;
import com.casesoft.dmc.service.wechat.SNSUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Administrator on 2018/1/4.
 */
@Controller
@RequestMapping("/sys/PushProduct")
public class FSendProductController extends BaseController implements IBaseInfoController<NoPushProduct> {
    @Autowired
    private NoPushProductService noPushProductService;
    @Autowired
    private NoPushStyleService  noPushStyleService;
    @Autowired
    private SNSUserInfoService sNSUserInfoService;



    @Override
    @RequestMapping("/page")
    @ResponseBody
    public Page<NoPushProduct> findPage(Page<NoPushProduct> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
        page.setPageProperty();
        page = this.noPushProductService.findPage(page,filters);
        return page;
    }

    @Override
    public List<NoPushProduct> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(NoPushProduct entity) throws Exception {
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
    @RequestMapping("/WxShopStyle")
    @ResponseBody
    public void WxShopStyle(){
        List<NoPushStyle> allStyle = this.noPushStyleService.findStyleAll();
        if(CommonUtil.isNotBlank(allStyle)){
            for(NoPushStyle noPushStyle:allStyle){
                List<NoPushProduct> noPushProductByStyleid = this.noPushProductService.findNoPushProductByStyleid(noPushStyle.getId());
                if(CommonUtil.isNotBlank(noPushProductByStyleid)){
                    this.noPushProductService.WxShopStyle(noPushStyle,noPushProductByStyleid);
                }

            }
        }


    }
    @RequestMapping("/sendURK")
    @ResponseBody
    public void sendURK(){
        try {
            HttpServletRequest request=this.getRequest();
            this.sNSUserInfoService.sendNoCustomertourk(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
