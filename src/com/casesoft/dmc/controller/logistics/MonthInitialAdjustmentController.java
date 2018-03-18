package com.casesoft.dmc.controller.logistics;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.logistics.MonthAccountStatement;
import com.casesoft.dmc.model.logistics.MonthInitialAdjustment;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.logistics.MonthAccountStatementService;
import com.casesoft.dmc.service.logistics.MonthInitialAdjustmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

/**
 * Created by yushen on 2017/10/20.
 */
@Controller
@RequestMapping("/logistics/initialAdjustment")
public class MonthInitialAdjustmentController  extends BaseController implements ILogisticsBillController<MonthInitialAdjustment> {

    @Autowired
    private MonthInitialAdjustmentService monthInitialAdjustmentService;
    @Autowired
    private MonthAccountStatementService monthAccountStatementService;

    @Override
    public String index() {
        return null;
    }

    @Override
    public Page<MonthInitialAdjustment> findPage(Page<MonthInitialAdjustment> page) throws Exception {
        return null;
    }

    @Override
    public List<MonthInitialAdjustment> list() throws Exception {
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

    @RequestMapping("/saveInitialAdjustment")
    @ResponseBody
    public MessageBox saveInitialAdjustment(String initialAdjustmentStr, String userId, String masId){
        try{
            MonthInitialAdjustment monthInitialAdjustment = JSON.parseObject(initialAdjustmentStr, MonthInitialAdjustment.class);
            monthInitialAdjustment.setId(new GuidCreator().toString());
            monthInitialAdjustment.setOprId(userId);
            monthInitialAdjustment.setBillDate(new Date());

            MonthAccountStatement mas = new MonthAccountStatement();
            mas.setId(masId);
            mas.setBillDate(new Date());
            mas.setBillType("客户期初调整");
            mas.setMonth(masId.substring(7));
            mas.setTotVal(monthInitialAdjustment.getAfterVal());
            mas.setUnitId(masId.substring(0,6));
            String ownerId;
            Customer guest = CacheManager.getCustomerById(mas.getUnitId());
            ownerId = guest.getOwnerId();
            if(CommonUtil.isBlank(guest)){
                Unit unit = CacheManager.getUnitById(mas.getUnitId());
                ownerId = unit.getOwnerId();
            }
            mas.setOwnerId(ownerId);
            mas.setUnitType("客户");

            this.monthInitialAdjustmentService.saveAdjustment(monthInitialAdjustment,mas);
            return new MessageBox(true,"调整成功");
        }catch (Exception e){
            e.printStackTrace();
            return new MessageBox(false,"调整失败");
        }
    }
}
