package com.casesoft.dmc.controller.sys;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.sys.PrintSet;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.sys.PrintSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by Administrator on 2018/5/31.
 */
@Controller
@RequestMapping(value = "/sys/printset")
public class PrintSetController extends BaseController implements IBaseInfoController<PrintSet> {
    @Autowired
    private PrintSetService  printSetService;
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
        return null;
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
            return new MessageBox(true, "保存成功");
        }catch (Exception e){
            e.printStackTrace();
            return new MessageBox(true, "保存失败");
        }


    }
}
