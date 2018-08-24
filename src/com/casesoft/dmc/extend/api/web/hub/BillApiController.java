package com.casesoft.dmc.extend.api.web.hub;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.controller.syn.tool.BillUtil;
import com.casesoft.dmc.controller.tag.InitUtil;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.core.vo.TagFactory;
import com.casesoft.dmc.extend.api.vo.BillVO;
import com.casesoft.dmc.extend.api.web.ApiBaseController;
import com.casesoft.dmc.extend.tag.PlayloungeTag;
import com.casesoft.dmc.model.cfg.Device;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.erp.ErpStock;
import com.casesoft.dmc.model.log.ServerLogMessage;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.tag.Init;
import com.casesoft.dmc.model.tag.InitDtl;
import com.casesoft.dmc.service.log.ServerLogMessageService;
import com.casesoft.dmc.service.syn.BillService;
import com.casesoft.dmc.service.syn.IBillWSService;
import com.casesoft.dmc.service.tag.InitService;
import io.swagger.annotations.Api;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by WingLi on 2017-01-04.
 */

@Controller
@RequestMapping(value = "/api/hub/bill", method = {RequestMethod.POST, RequestMethod.GET})
@Api
public class BillApiController extends ApiBaseController {

    @Autowired
    private IBillWSService billWSService;
    @Autowired
    private InitService initService;

    @Autowired
    private ServerLogMessageService serverLogMessageService;

    @Autowired
    private BillService billService;

    @Autowired
    private ProductApiController productApiController;

    @Override
    public String index() {
        return null;
    }

    @RequestMapping(value = "/findERPBasicImgWS.do")
    @ResponseBody
    public MessageBox findERPBasicImgWS(String styleId, String colorId) throws Exception {
        this.logAllRequestParams();
        if (!(TagFactory.getCurrentTag() instanceof PlayloungeTag)) {
            return productApiController.productImage(styleId, colorId);
        }
        if (CommonUtil.isBlank(styleId)) {
            return this.returnFailInfo("参数缺省！", new ArrayList<>());
        } else {
            List<Product> products = this.billWSService.findErpBasic(styleId,
                    colorId);
            return this.returnSuccessInfo("下载成功！", products);
        }
    }

    /**
     * 查询erp图片接口
     */
    @RequestMapping(value = "/findErpStockWS.do")
    @ResponseBody
    public MessageBox findErpStockWS() throws Exception {
        this.logAllRequestParams();
        String deviceId = this.getReqParam("deviceId");
        String filter_LIKES_styleId = this.getReqParam("filter_LIKES_styleId");
        String filter_LIKES_colorId = this.getReqParam("filter_LIKES_colorId");
        String filter_LIKES_sizeId = this.getReqParam("filter_LIKES_sizeId");
        String filter_LIKES_sku = this.getReqParam("filter_LIKES_sku");
        String filter_LIKES_warehouseId = this
                .getReqParam("filter_LIKES_warehouseId");
        if (CommonUtil.isNotBlank(deviceId)
                && CommonUtil
                .isNotBlank(CacheManager.getDeviceByCode(deviceId))) {
            filter_LIKES_warehouseId = CacheManager.getDeviceByCode(deviceId)
                    .getStorageId();
        }

        List<ErpStock> erpStocks = this.billWSService.findErpStock(
                new String[]{"filter_LIKES_styleId",
                        "filter_LIKES_colorId", "filter_LIKES_sizeId",
                        "filter_LIKES_warehouseId,filter_LIKES_sku"},
                new String[]{filter_LIKES_styleId, filter_LIKES_colorId,
                        filter_LIKES_sizeId, filter_LIKES_warehouseId,
                        filter_LIKES_sku});
        return this.returnSuccessInfo("下载成功", erpStocks);
    }

    /**
     * 设备获取ERP单据入口
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/listBillsByDeviceWS.do")
    @ResponseBody
    public List<BillVO> listBillsByDevice(String deviceId, String type, String beginDate, String endDate, String billId, String unitId, String status,String rmId) throws Exception {
        this.logAllRequestParams();
        String styleId = this.getReqParam("styleId");
        if(CommonUtil.isBlank(styleId)){
            styleId = "";
        }
        List<Bill> billList = null;
        if (Integer.parseInt(type) <= Constant.Token.Label_Data_Feedback) {// 当type=2，说明为标签初始化单据,5为标签打印单
            billList = this.listPrintBillWS(deviceId, type ,beginDate, endDate,unitId, status,styleId);
        } else {
            billList = this.findERPBillWS(deviceId, type, beginDate, endDate, billId, unitId, status,rmId);
        }
        List<BillVO> voList = new ArrayList<BillVO>();
        if (CommonUtil.isNotBlank(billList)) {
            for (Bill bill : billList) {
                BillVO vo = new BillVO();
                BeanUtils.copyProperties(vo, bill);
                vo.setUnitId(bill.getOrigUnitId());
                vo.setStorageId(bill.getOrigId());
                vo.setUnit2Id(bill.getDestUnitId());
                vo.setStorage2Id(bill.getDestId());
                vo.setUnitName(bill.getOrigName());
                vo.setUnit2Name(bill.getDestName());
                voList.add(vo);
            }
        }
        return voList;
    }

    /**
     * 获取ERP单据
     *
     * @param deviceId
     * @param type
     * @return
     * @throws Exception
     */
    private List<Bill> findERPBillWS(String deviceId, String type, String beginDate, String endDate, String billId, String unitId, String status,String rmId) throws Exception {
        if (CommonUtil.isBlank(CacheManager.getDeviceByCode(deviceId))) {
            return new ArrayList<>();

        }
        String ownerId = CacheManager.getDeviceByCode(deviceId).getOwnerId();
        if (CommonUtil.isBlank(status)) {
            status = "0,1";
        }
        List<Bill> billList = this.billWSService.findBills(new String[]{
                "filter_EQS_ownerId", "filter_EQI_type", "filter_GED_billDate",
                "filter_LED_billDate", "filter_INI_status", "deviceId",
                "styleId", "fitler_EQS_origUnitId","fitler_EQS_rmId"}, new String[]{ownerId, type, beginDate, endDate,
                String.valueOf(status), deviceId, billId, unitId,rmId});

        return billList;

    }

    /**
     * 提供给打印机或检测仪已确认，未打印的单据
     *
     * @return
     */
    private List<Bill> listPrintBillWS(String deviceId, String type, String beginDate, String endDate,String unitId, String status,String styleId) {
        List<PropertyFilter> filters = null;
        String ownerId = "1";

        switch (Integer.parseInt(type)) {
            case Constant.Token.Label_Data_Write:
            case Constant.Token.Label_Data_Detect:

                if (!CommonUtil.isBlank(deviceId)
                        && !CommonUtil.isBlank(CacheManager
                        .getDeviceByCode(deviceId)))
                    ownerId = CacheManager.getDeviceByCode(deviceId).getOwnerId();
                filters = PropertyFilter.buildFromParams(new String[]{
                        "EQS_ownerId", "GEI_status", "LEI_status","GED_billDate","LED_billDate","LIKES_billNo"}, new String[]{
                        ownerId, "1", "2",beginDate,endDate,styleId});
                break;

            case Constant.Token.Label_Data_Receive:
            case Constant.Token.Label_Data_Send:
            case Constant.Token.Label_Data_Feedback:
                ownerId = CacheManager.getDeviceByCode(deviceId).getOwnerId();
                filters = PropertyFilter.buildFromParams(new String[]{
                        "EQS_ownerId", "GEI_status", "LEI_status","GED_billDate","LED_billDate"}, new String[]{
                        ownerId, "1", "2",beginDate,endDate});
                break;
        }

        List<Init> initList = this.initService.find(filters);
        Collections.sort(initList, new Comparator<Init>() {

            @Override
            public int compare(Init i1, Init i2) {
                return i2.getBillNo().compareTo(i1.getBillNo());
            }

        });
        List<Bill> bills = InitUtil.convertToBills(initList,
                Integer.parseInt(type));
        return bills;
    }

    @RequestMapping(value = "/findERPBillDtlWS.do")
    @ResponseBody
    public List<BillDtl> findERPBillDtlWS(String id, String type) {
        this.logAllRequestParams();
        Assert.notNull(id, "单据id不能为空");
        Assert.notNull(type, "单据类型type不能为空");

        if (id.startsWith(Constant.Bill.Tag_Init_Prefix)) {// 2014-06-27
            return this.findInitBillDtlWS(id, type);
        }

        List<BillDtl> billDtlList = null;
        try {
            billDtlList = this.billWSService.findBillDtls(new String[]{"billId",
                    "type"}, new String[]{id, type});

        } catch (Exception e) {
            e.printStackTrace();
            ServerLogMessage logMessage = new ServerLogMessage();
            logMessage.setMethod("findERPBillDtlWS.do");
            logMessage.setMessage(e.getLocalizedMessage());
            logMessage.setCreateTime(new Date());
            this.serverLogMessageService.save(logMessage);
        }
        BillUtil.convertToVo(billDtlList);
        return billDtlList;
    }

    private List<BillDtl> findInitBillDtlWS(String billNo, String type) {
        int intType = Integer.parseInt(type);
        List<InitDtl> dtlList = null;
        if (intType > Constant.Token.Label_Data_Detect) {
            dtlList = this.initService.findDtls(billNo.split("-")[0]);
        } else {
            dtlList = this.initService.findDtls(billNo);
        }
        List<BillDtl> billDtls = InitUtil.convertToBillVos(dtlList, intType);
        return billDtls;
    }


    /**
     * 生成协同盘点单
     *
     * @param deviceId
     * @param unitId
     * @param token
     * @param billDate   //单据日期
     * @param conditions 明细条件
     * @return
     */
    @RequestMapping(value = "/createCooperateBill.do")
    @ResponseBody
    public MessageBox createCooperateBill(String deviceId, String unitId, String token, String billDate, String conditions) {
        Assert.notNull(deviceId, "设备号不能为空");
        if (CommonUtil.isBlank(unitId)) {
            Device device = CacheManager.getDeviceByCode(deviceId);
            if (CommonUtil.isNotBlank(device)) {
                unitId = device.getStorageId();
            }
        }
        Unit unit = CacheManager.getUnitById(unitId);
        if (CommonUtil.isBlank(unit)) {
            return this.returnFailInfo("设备号或店仓编号错误！", new Bill());
        }
        try {
            Bill bill = this.billWSService.productBill(new String[]{
                    "deviceId", "unitId", "token", "billDate", "conditions"}, new String[]{deviceId,
                    unitId, token, billDate, conditions});
            if (CommonUtil.isBlank(bill)) {
                return this.returnFailInfo("生成失败！", new Bill());
            } else {
                this.billService.save(bill);
                bill.setDtlList(null);
                return this.returnSuccessInfo("生成成功！", bill);

            }
        } catch (Exception e) {
            e.printStackTrace();
            return this.returnFailInfo("生成失败！", new Bill());
        }
    }

    /**
     * 终止协同盘点单
     *
     * @param deviceId
     * @param billNo
     * @param token
     * @return
     */
    @RequestMapping(value = "/endCooperateBill.do")
    @ResponseBody
    public MessageBox endCooperateBill(String deviceId, String billNo, String token) {
        Assert.notNull(deviceId, "设备号不能为空");
        Device device = CacheManager.getDeviceByCode(deviceId);
        if (CommonUtil.isBlank(device)) {
            return this.returnFailInfo("终止失败！设备号未注册！");
        }
        String result = this.billWSService.destroyBill(new String[]{"token",
                        "billNo", "deviceId"},
                new String[]{token, billNo, deviceId});
        if (CommonUtil.isBlank(result)) {
            return this.returnFailInfo("终止失败！");
        } else {
            return this.returnSuccessInfo("成功！");
        }
    }

    /**
     * 更新单据状态
     *
     * @param deviceId
     * @param billNo
     * @param token
     * @return
     */
    @RequestMapping(value = "/updateBillStatusWS.do")
    @ResponseBody
    public MessageBox updateBillStatusWS(String deviceId, String billNo, String token,Integer status) {
        Assert.notNull(deviceId, "设备号不能为空");
        Device device = CacheManager.getDeviceByCode(deviceId);
        if (CommonUtil.isBlank(device)) {
            return this.returnFailInfo("终止失败！设备号未注册！");
        }
        if (CommonUtil.isBlank(status)) {
            return this.returnFailInfo("单据状态未填写！");
        }
        Bill bill = this.billService.get("billNo", billNo);
        if (CommonUtil.isBlank(bill)) {
            bill = this.billService.get("id", billNo);
            if (CommonUtil.isBlank(bill)) {
                return this.returnFailInfo("更新失败！单据不存在！");
            } else {
                bill.setStatus(status);
                this.billService.update(bill);
            }
        } else {
            bill.setStatus(status);
            this.billService.update(bill);
        }

        return this.returnSuccessInfo("更新成功！");
    }


}
