package com.casesoft.dmc.controller.stock;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.ILogisticsBillController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.cfg.Device;
import com.casesoft.dmc.model.logistics.SaleOrderBill;
import com.casesoft.dmc.model.stock.GuardingRecord;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.stock.GuardingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by Administrator on 2017/11/16.
 */
@Controller
@RequestMapping("/stock/guardingRecord")
public class GuardingRevordController extends BaseController implements ILogisticsBillController<GuardingRecord> {
    @Autowired
    private GuardingRecordService guardingRecordService;
    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {
        ModelAndView mv = new ModelAndView("/views/stock/guardingRecord");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        mv.addObject("userId", getCurrentUser().getId());
        return mv;
    }
    @RequestMapping(value = "/page")
    @ResponseBody
    public Page<GuardingRecord> findPage(Page<GuardingRecord> page, String userId) throws Exception {
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this
                .getRequest());
       //权限设置，增加过滤条件，只显示当前ownerId下的销售单信息
        User CurrentUser = CacheManager.getUserById(userId);
        String ownerId = CurrentUser.getOwnerId();
        String id = CurrentUser.getId();
        if (!id.equals("admin")) {
            PropertyFilter filter = new PropertyFilter("EQS_deviceOwnerId", ownerId);
            filters.add(filter);
        }
        page.setPageProperty();
        page = this.guardingRecordService.findPages(page, filters);
        for(GuardingRecord g : page.getRows()){
            Device device = CacheManager.getDeviceByCode(g.getDeviceId());
            if(CommonUtil.isNotBlank(device)){
                Unit unit = CacheManager.getUnitByCode(device.getOwnerId());
                if(CommonUtil.isNotBlank(unit)){
                    g.setShopId(unit.getId());
                    g.setShopName(unit.getName());
                }
            }
        }
        return page;
    }

    @Override
    public Page<GuardingRecord> findPage(Page<GuardingRecord> page) throws Exception {
        return null;
    }

    @Override
    public List<GuardingRecord> list() throws Exception {
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
        return "/views/stock/guardingRecord";
    }
}
