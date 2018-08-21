package com.casesoft.dmc.controller.shop;

import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.controller.IBaseInfoController;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.util.secret.EpcSecretUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.shop.DressRecord;
import com.casesoft.dmc.model.stock.EpcStock;
import com.casesoft.dmc.service.shop.DressRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by yushen on 2017/9/8.
 */
@Controller
@RequestMapping("/shop/dressRecord")
public class DressRecordController extends BaseController implements IBaseInfoController<DressRecord> {

    @Autowired
    private DressRecordService dressRecordService;

    @Override
//    @RequestMapping(value = "/index")
    public String index() {
        return "/views/shop/dressRecord";
    }

    @RequestMapping(value = "/index")
    public ModelAndView indexMV() throws Exception {
        ModelAndView mv = new ModelAndView("/views/shop/dressRecord");
        mv.addObject("ownerId", getCurrentUser().getOwnerId());
        return mv;
    }


    @RequestMapping(value = "/page")
    @ResponseBody
    @Override
    public Page<DressRecord> findPage(Page<DressRecord> page) throws Exception {
        this.logAllRequestParams();
        List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(this.getRequest());
        String ownerId = getCurrentUser().getOwnerId();
        PropertyFilter filter = new PropertyFilter("EQS_ownerId", ownerId);
        filters.add(filter);
        page.setPageProperty();
        page = this.dressRecordService.findPage(page, filters);
        return page;
    }

    @RequestMapping("/findInfo")
    @ResponseBody
    public MessageBox findInfo(String code) throws Exception {
        if (code.length() != 13) {
            String epcCode = code.toUpperCase();
            code = EpcSecretUtil.decodeEpc(epcCode).substring(0, 13);
        }
        String defaultWarehId = this.dressRecordService.findWarehouseByCode(getCurrentUser().getOwnerId());
        EpcStock epcStock = this.dressRecordService.findStockEpcByCode(code, defaultWarehId);
        if (CommonUtil.isNotBlank(epcStock)) {
            return new MessageBox(true, "", epcStock.getCode());
        } else {
            return new MessageBox(false, "未查询到此码" + code);
        }
    }

    @Override
    public List<DressRecord> list() throws Exception {
        return null;
    }

    @Override
    public MessageBox save(DressRecord entity) throws Exception {
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

    @RequestMapping("/dressing")
    @ResponseBody
    public MessageBox dressing(String code, String businessId) {
        try {
            String defaultWareHId = this.dressRecordService.findWarehouseByCode(getCurrentUser().getOwnerId());
            EpcStock epcStock = this.dressRecordService.findStockEpcInHouseByCode(code, defaultWareHId);

            if (CommonUtil.isNotBlank(epcStock)) {
                DressRecord dressRecord = new DressRecord();
                dressRecord.setDressCode(code);
                dressRecord.setBusinessId(businessId);
                dressRecord.setOwnerId(getCurrentUser().getOwnerId());
                this.dressRecordService.dressing(dressRecord, epcStock);
                return new MessageBox(true, "借出成功");
            } else {
                return new MessageBox(false, "此件商品不在本库");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, e.getMessage());
        }
    }

    @RequestMapping("/returnBack")
    @ResponseBody
    public MessageBox returnBack(String code, String businessId) {
        try {
            DressRecord dressRecord = this.dressRecordService.findDressRecord(code, businessId);
            if (CommonUtil.isNotBlank(dressRecord)) {
                String defaultWareHId = this.dressRecordService.findWarehouseByCode(getCurrentUser().getOwnerId());
                EpcStock epcStock = this.dressRecordService.findStockEpcNotInHouseByCode(code, defaultWareHId);
                if(CommonUtil.isNotBlank(epcStock)){
                    this.dressRecordService.returnBack(dressRecord, epcStock);
                    return new MessageBox(true, "归还成功");
                }else {
                    return new MessageBox(false, "样衣状态异常");
                }
            } else {
                return new MessageBox(false, "请归还借出的衣服");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageBox(false, e.getMessage());
        }
    }
}
