package com.casesoft.dmc.controller.shop;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.shop.PointsChange;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by yushen on 2017/11/3.
 */
@Controller
@RequestMapping(value = "/shop/pointsChange")
public class PointsChangeController extends BaseController implements IBaseInfoController<PointsChange>{
    @Override
    public String index() {
        return null;
    }

    @Override
    public Page<PointsChange> findPage(Page<PointsChange> page) throws Exception {
        return null;
    }

    @Override
    public List<PointsChange> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(PointsChange entity) throws Exception {
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

}
