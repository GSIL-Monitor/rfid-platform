package com.casesoft.dmc.controller.syn.third;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.syn.third.basic.IBillController;
import com.casesoft.dmc.controller.syn.third.basic.ThirdBillRequestEntity;
import com.casesoft.dmc.controller.syn.tool.BillUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.controller.BaseController;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.syn.BillService;
import com.casesoft.dmc.service.syn.IBillWSService;
import com.casesoft.dmc.service.sys.impl.UnitService;
import com.casesoft.dmc.service.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 2017/1/15.
 * 第三方接口
 */
@RequestMapping("/syn/third")
@Controller
public class ThirdBillController extends BaseController implements IBillController {
    @Autowired
    private BillService billService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IBillWSService billWSService;
    @Autowired
    private UnitService unitService;

    @RequestMapping("/bill/findBill")
    @ResponseBody
    @Override
    public MessageBox findBill(@Valid ThirdBillRequestEntity thirdBillRequestEntity, BindingResult result) {
        if (result.hasErrors()) {
            List<FieldError> list = result.getFieldErrors();
            StringBuffer subError = new StringBuffer();
            for (int i = 0; i < list.size(); i++) {
                subError.append(i + 1).append(":").append(list.get(i).getDefaultMessage()).append(";");
            }
            return this.returnFailInfo(subError.toString());
        } else {
            if (thirdBillRequestEntity.getType() <= Constant.Token.Label_Data_Feedback) {// 当type=2，说明为标签初始化单据,5为标签打印单
                return this.returnFailInfo("此类型单据无法查询！");
            } else {
                if (CommonUtil.isBlank(CacheManager.getDeviceByCode(thirdBillRequestEntity.getDeviceId()))) {
                    return this.returnFailInfo("设备号无效！");
                }
                String ownerId = CacheManager.getDeviceByCode(thirdBillRequestEntity.getDeviceId()).getOwnerId();
                List<Bill> billList = null;
                try {
                    billList = this.billWSService.findBills(new String[]{
                                    "filter_EQS_ownerId", "filter_EQI_type"
                                    , "filter_GED_billDate", "filter_LED_billDate"
                                    , "deviceId", "deviceId", "filter_LIKES_billId","filter_EQS_unitId"},
                            new String[]{ownerId, thirdBillRequestEntity.getType().toString(),
                                    thirdBillRequestEntity.getBeginDate(), thirdBillRequestEntity.getEndDate(),
                                    thirdBillRequestEntity.getDeviceId(), thirdBillRequestEntity.getDeviceId(),thirdBillRequestEntity.getBillId(),thirdBillRequestEntity.getUnitId()});
                    BillUtil.covertToVos(billList);
                    return returnSuccessInfo("查询成功", billList);
                } catch (Exception e) {
                    e.printStackTrace();
                    return returnFailInfo("查询失败");
                }
            }
        }
    }
    @RequestMapping("/bill/synElanInBills")
    @ResponseBody
    public MessageBox synElanInBills(){

            String date = CommonUtil.getDateString(new Date(),"yyyyMMdd");
         /*   String date = "20170418";*/
            List<Bill> billList = new ArrayList<Bill>();
            List<Unit> shopList = this.unitService.findShop();

            for(Unit u : shopList){
                List<Bill> resultList = new ArrayList<>();
                try {
                    resultList = this.billWSService.findBills(new String[]{
                            "storeCode", "token", "beginDate", "endDate", "shopCode", "", "", "unitCode"
                    }, new String[]{
                            u.getCode(), "" + Constant.Token.Shop_Inbound, date, date, u.getRemark().split("-")[0], "", "", u.getCode()
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
                billList.addAll(resultList);

            }
            if(billList.size() > 0){
                this.billService.save(billList);

            }
            return returnSuccessInfo("同步成功"+billList.size()+"单入库数据");

    }



    @RequestMapping("/bill/synElanInventoryBill")
    @ResponseBody
    public MessageBox synElanInventoryBill(){

            List<Bill> billList = new ArrayList<Bill>();
            List<Unit> shopList = this.unitService.findShop();
            for(Unit u : shopList){
                List<Bill> resultList = new ArrayList<>();
                try{
                    resultList = this.billWSService.findBills(new String[]{
                        "storeCode","token","beginDate","endDate","shopCode","","","unitCode"
                     },new String[]{
                         u.getCode(),""+Constant.Token.Shop_Inventory,"","",u.getRemark().split("-")[0],"","",u.getCode()
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
                billList.addAll(resultList);

            }
            if(billList.size() > 0){
                this.billService.save(billList);
            }
            return returnSuccessInfo("同步成功"+billList.size()+"盘点数据");


    }

    @RequestMapping("/bill/findBillDtls")
    @ResponseBody
    @Override
    public MessageBox findBillDtls(String billId, Integer type) {
        if (CommonUtil.isBlank(billId)) {
            return this.returnFailInfo("无效单据号！");
        } else if (CommonUtil.isBlank(type)) {
            return this.returnFailInfo("无效单据类型");
        } else {
            List<BillDtl> billDtls = null;
            try {
                billDtls = this.billWSService.findBillDtls(new String[]{"billId",
                        "type"}, new String[]{billId, type.toString()});
                return this.returnSuccessInfo("下载成功", billDtls);
            } catch (Exception e) {
                e.printStackTrace();
                return this.returnFailInfo("下载失败");
            }
        }
    }

    @RequestMapping("/bill/findViewBillDtls")
    @ResponseBody
    public List<BillDtl> findViewBillDtls(String billId, Integer type) {
        this.logAllRequestParams();
        List<BillDtl> billDtls = null;
        try {
            if (CommonUtil.isBlank(billId) || CommonUtil.isBlank(type)) {
                return new ArrayList<>();
            }
            billDtls = this.billWSService.findBillDtls(new String[]{"billId",
                    "type"}, new String[]{billId, type.toString()});
            if (CommonUtil.isBlank(billDtls)) {
                return new ArrayList<>();
            } else {
                return billDtls;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @RequestMapping(value = "/bill/detail/index")
    @ResponseBody
    @Override
    public ModelAndView viewDtlIndex(String billId, Integer type) {
        String billNo = this.getReqParam("billNo");
        String billType =  CacheManager.getSetting(type.toString()).getValue();
        this.logAllRequestParams();
        ModelAndView model = new ModelAndView();
        model.addObject("billId", billId);
        model.addObject("billNo",billNo);
        model.addObject("billType",billType);
        model.addObject("mainUrl","/syn/third/bill/index.do");
        model.addObject("type",type);
        model.setViewName("/views/syn/thirdBillDetail");
        return model;
    }
    @RequestMapping(value = "/bill/index")
    @Override
    public String index() {
        return "/views/syn/thirdBillManager";
    }

}
