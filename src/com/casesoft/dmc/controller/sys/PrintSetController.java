package com.casesoft.dmc.controller.sys;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.sys.GuestView;
import com.casesoft.dmc.model.sys.PrintSet;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.sys.GuestViewService;
import com.casesoft.dmc.service.sys.PrintSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/31.
 */
@Controller
@RequestMapping(value = "/sys/printset")
public class PrintSetController extends BaseController implements IBaseInfoController<PrintSet> {
    @Autowired
    private PrintSetService  printSetService;
    @Autowired
    private GuestViewService guestViewService;

    @Override
    public Page<PrintSet> findPage(Page<PrintSet> page) throws Exception {
        return null;
    }

    @Override
    public List<PrintSet> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(PrintSet entity) throws Exception {
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
    public String index() {
        return "views/sys/printSet";
    }
    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {
        ModelAndView mv = new ModelAndView("/views/sys/printSet");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        User currentUser = getCurrentUser();
        Unit unit = CacheManager.getUnitById(getCurrentUser().getOwnerId());
        mv.addObject("deportId", unit.getDefaultWarehId());
        mv.addObject("groupid", unit.getGroupId());
        if(CommonUtil.isNotBlank(unit.getGroupId())&&unit.getGroupId().equals("JMS")){
            GuestView guestView = this.guestViewService.findByid(currentUser.getOwnerId());
            mv.addObject("address", guestView.getAddress());
        }
        return mv;
    }
    @RequestMapping(value="/savePrintSetMessage")
    @ResponseBody
    public MessageBox savePrintSetMessage(String printSet) throws Exception {
        try {
            PrintSet entity = JSON.parseObject(printSet, PrintSet.class);
            User currentUser = this.getCurrentUser();
            if(CommonUtil.isBlank(entity.getId())&&entity.getCommonType()!=0){
                entity.setOwnerId(currentUser.getOwnerId());
            }
            this.printSetService.savePrintSetMessage(entity);
            return new MessageBox(true, "保存成功",entity);
        }catch (Exception e){
            e.printStackTrace();
            return new MessageBox(false, "保存失败");
        }
    }
    @RequestMapping(value="/findPrintSet")
    @ResponseBody
    public MessageBox findPrintSet(String ruleReceipt,String type){
        try {
            User currentUser = this.getCurrentUser();
            PrintSet printSet = this.printSetService.findPrintSet(ruleReceipt, type, currentUser.getOwnerId());
            return new MessageBox(true, "查询成功",printSet);
        }catch (Exception e){
            return new MessageBox(false, "查询失败");
        }

    }
    @RequestMapping(value="/findPrintSetListByOwnerId")
    @ResponseBody
    public MessageBox findPrintSetListByOwnerId(String type, String ruleReceipt){
        try {
            User currentUser = this.getCurrentUser();
            List<PrintSet> printSets;
            if(ruleReceipt.equals("A4")&&currentUser.getOwnerId().equals("1")){
                printSets = this.printSetService.findPrintSetListByOwnerIdA4(type,ruleReceipt);
            }else{
                printSets = this.printSetService.findPrintSetListByOwnerId(currentUser.getOwnerId(),type,ruleReceipt);
            }
            return new MessageBox(true, "查询成功",printSets);
        }catch (Exception e){
            return new MessageBox(false, "查询失败");
        }
    }
    @RequestMapping(value="/printMessage")
    @ResponseBody
    public MessageBox printMessage(String id,String billno){
        try {
            Map<String, Object> map = this.printSetService.printMessage(id, billno);
            return new MessageBox(true, "查询成功",map);
        }catch (Exception e){
            return new MessageBox(false, "查询失败");
        }

    }
    @RequestMapping(value="/printMessageA4")
    @ResponseBody
    public MessageBox printMessageA4(String id,String billno){
        try {
            Map<String, Object> map = this.printSetService.printMessageA4(id, billno);
            return new MessageBox(true, "查询成功",map);
        }catch (Exception e){
            return new MessageBox(false, "查询失败");
        }

    }


}
