package com.casesoft.dmc.controller.logistics;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.MergeReplenishBillDtl;
import com.casesoft.dmc.service.logistics.MergeReplenishBillDtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by Administrator on 2018/3/19.
 */
@Controller
@RequestMapping("/logistics/mergeReplenishBillDtl")
public class MergeReplenishBillDtlController extends BaseController implements ILogisticsBillController<MergeReplenishBillDtl> {
    @Autowired
    private MergeReplenishBillDtlService  mergeReplenishBillDtlService;
    @Override
    public Page<MergeReplenishBillDtl> findPage(Page<MergeReplenishBillDtl> page) throws Exception {
        return null;
    }

    @Override
    public List<MergeReplenishBillDtl> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(String bill, String strDtlList, String userId) throws Exception {
        return null;
    }

    @Override
    public ModelAndView add() throws Exception {
        return null;
    }

    @Override
    public ModelAndView edit(String billNo) throws Exception {
        return null;
    }

    @Override
    public MessageBox check(String billNo) throws Exception {
        return null;
    }

    @Override
    public MessageBox end(String billNo) throws Exception {
        return null;
    }

    @Override
    public MessageBox cancel(String billNo) throws Exception {
        return null;
    }

    @Override
    public MessageBox convert(String strDtlList, String recordList) throws Exception {
        return null;
    }

    @Override
    public String index() {
        return null;
    }
}
