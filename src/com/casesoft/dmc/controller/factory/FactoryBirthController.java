package com.casesoft.dmc.controller.factory;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.cfg.Device;
import com.casesoft.dmc.model.factory.*;
import com.casesoft.dmc.service.factory.FactoryInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by GuoJunwen on 2017-05-09.
 */

@Controller
@RequestMapping("/factory/birth")
public class FactoryBirthController extends BaseController implements IBaseInfoController<FactoryInit> {

    @Autowired
    private FactoryInitService factoryInitService;

    @RequestMapping("/index")
    @Override
    public String index() {
        return "views/factory/factoryBirth";
    }

    @RequestMapping("/page")
    @ResponseBody
    @Override
    public Page<FactoryInit> findPage(Page<FactoryInit> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        page.setPageProperty();
        page=this.factoryInitService.findPage(page,filters);

        return page;
    }

    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    @ResponseBody
    @Override
    public MessageBox importExcel(MultipartFile file) throws Exception {
        this.logAllRequestParams();
        InputStream in = file.getInputStream();
        String prefixTaskId = Constant.Bill.Tag_Init_Prefix
                + CommonUtil.getDateString(new Date(), "yyMMdd");
        String billNo = this.factoryInitService.findMaxNo(prefixTaskId);// 重新定义单号规则
        FactoryInit factoryInit=new FactoryInit();
        factoryInit.setBillDate(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        factoryInit.setBillNo(billNo);
        factoryInit.setFileName(file.getOriginalFilename());
        try {

            FactoryBillInfoList factoryBillInfoList = FactoryBillInitUtil.processNewExcel((FileInputStream)in, factoryInitService, getCurrentUser(),factoryInit);
            this.factoryInitService.save(factoryBillInfoList,factoryInit);
            if(CommonUtil.isNotBlank(factoryBillInfoList.getProductList())){
                CacheManager.refreshProductCache();
            }
            if(CommonUtil.isNotBlank(factoryBillInfoList.getStyleList())){
                CacheManager.refreshStyleCache();
            }
            if(CommonUtil.isNotBlank(factoryBillInfoList.getColorList())){
                CacheManager.refreshColorCache();
            }
            if(CommonUtil.isNotBlank(factoryBillInfoList.getSizeList())){
                CacheManager.refreshSizeCache();
            }
        } catch (Exception e) {
           return returnFailInfo("error",e.getMessage());
        }

        return returnSuccessInfo("ok");
    }

    @RequestMapping("/detail")
    @ResponseBody
    public ModelAndView detail(String billNo){
        ModelAndView model=new ModelAndView();
        model.setViewName("views/factory/factoryBirth_detail");
        FactoryInit init=this.factoryInitService.findInitByBillNo(billNo);
        model.addObject("init",init);
        return model;
    }

    @RequestMapping("/detailList")
    @ResponseBody
    public List<FactoryBillDtl> detailList(String billNo,String factoryBillNo){
        String ownerId=getCurrentUser().getOwnerId();
        List<FactoryBillDtl> dtlList ;
        if(CommonUtil.isBlank(factoryBillNo)){
            dtlList = this.factoryInitService.findDtls(billNo, ownerId);
        }else{
            dtlList = this.factoryInitService.findFactoryBillDtl(billNo, factoryBillNo, ownerId);
        }

        FactoryBillInitUtil.convertToVos(dtlList);

        return dtlList;
    }


    @Override
    public List<FactoryInit> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(FactoryInit entity) throws Exception {
        return null;
    }

    @Override
    public MessageBox edit(String taskId) throws Exception {
        return null;
    }


    @Override
    public MessageBox delete(String billNo) throws Exception {
        this.factoryInitService.delete(billNo);

        return returnSuccessInfo("ok");
    }



    @RequestMapping("/delete")
    @ResponseBody
    public MessageBox delete(String billNo,String factoryBillNo) throws Exception {
        FactoryBill factoryBill = this.factoryInitService.findUniqueFactoryBill(billNo,factoryBillNo);
        if (CommonUtil.isBlank(factoryBill)){
            return returnFailInfo("error","未找到标签");
        }
        if(CommonUtil.isNotBlank(factoryBill.getProgress())){
            return returnFailInfo("删除失败！,该办单已进行扫描无法删除",billNo);
        }else{
            this.factoryInitService.delete(billNo,factoryBillNo);
            return  returnSuccessInfo("删除成功");
        }
    }


    @Override
    public void exportExcel() throws Exception {

    }

    @RequestMapping("/confirm")
    @ResponseBody
    public MessageBox confirm(String billNo, String factoryBillNo,Integer status){
        this.logAllRequestParams();
        FactoryInit initBill;
        if(CommonUtil.isBlank(factoryBillNo)){
            initBill = this.factoryInitService.get("billNo", billNo);
        }else{
            initBill = this.factoryInitService.findFactoryInit(billNo, factoryBillNo);
        }
        if(initBill.getStatus() != 2){
            initBill.setStatus(status);
            this.factoryInitService.updateStatus(initBill);
        }
        return returnSuccessInfo("更新成功");
    }

    @RequestMapping("/downloadFile")
    @ResponseBody
    public void  downloadPrintFile(String billNo,String deviceId,String factoryBillNo) throws Exception {
        this.logAllRequestParams();
        Device device = CacheManager.getDeviceByCode(deviceId);

        String ownerId;
        if(CommonUtil.isBlank(device)){
            ownerId ="1";
        }else{
            ownerId = device.getOwnerId();
        }
        // 不能为空
        boolean isRfid = true;

        FactoryInit master;
        List<FactoryBillDtl> details  = new ArrayList<FactoryBillDtl>();
        if(CommonUtil.isBlank(factoryBillNo)){
            master = this.factoryInitService.get("billNo", billNo);
            details = this.factoryInitService.findDtls(billNo, ownerId);
        }else{
            master = this.factoryInitService.findFactoryInit(billNo, factoryBillNo);
            details = this.factoryInitService.findFactoryBillDtl(billNo, factoryBillNo, ownerId);
        }
         File inputPath = FactoryBillInitUtil.writeTextFile(details, null, isRfid);

        if (master.getStatus() == 1) {// 已确认状态
            this.factoryInitService.update(FactoryBillInitUtil.epcList, master);
        }

        String filename = inputPath.getName();
        String contentType = "application/zip;charset=utf-8";
        this.outFile(filename,inputPath,contentType);
    }


}
