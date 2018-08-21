package com.casesoft.dmc.controller.shop;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.vo.ChartVo;
import com.casesoft.dmc.model.cfg.Device;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.shop.SaleBill;
import com.casesoft.dmc.model.shop.SaleBillDtl;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.BusinessDtl;
import com.casesoft.dmc.model.task.Record;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Wing Li on 2014/6/21.
 */
public class SaleBillUtil {
    public static List<SaleBillDtl> convertToVo(List<SaleBillDtl> dtls) {
        if (CommonUtil.isNotBlank(dtls)) {
            for (SaleBillDtl dtl : dtls) {
                if(CommonUtil.isNotBlank(CacheManager.getStyleById(dtl.getStyleId()))) {
                    dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
                    dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
                    dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
                    dtl.setPrice(CacheManager.getStyleById(dtl.getStyleId())
                            .getPrice());
                }
            }
        }
        return dtls;
    }
    public static String convertToChartResult2(List<Object> l) {
        StringBuffer categories = new StringBuffer();
        StringBuffer data = new StringBuffer();
        String name = "销售量";
        for (Object o : l) {
            Object[] os = (Object[]) o;
            categories.append(",\"").append(os[0]).append("\"");
            data.append("," + os[2]);

        }
        String result = "{\"categories\":[" + categories.substring(1) + "]," + "\"series\":[{"
                + "\"name\":\"" + name + "\",\"data\":[" + data.substring(1) + "]} ] }";
        return result;
    }
    static void initOrderDtlNo(String billNo1, List<SaleBillDtl> listOrderDtl,
                               SaleBill o) {
        // billNo++;
        o.setId(billNo1);
        o.setBillNo(billNo1);
        o.setBillDate(new Date());
        String shopId = o.getShopId();
        o.setOwnerId(CacheManager.getUnitById(shopId).getOwnerId());
        int i = 0;
        for (SaleBillDtl dtl : listOrderDtl) {
            dtl.setBillNo(billNo1);
            dtl.setBillId(billNo1);
            dtl.setCode(dtl.getStyleId() + dtl.getColorId() + dtl.getSizeId());
			/*
			 * 客户端计算
			 */
			/*
			 * dtl.setPercent(dtl.getActPrice()/dtl.getPrice());
			 * dtl.setActValue(dtl.getActPrice()*dtl.getQty());
			 */
            dtl.setId(dtl.getBillNo() + "_" + String.valueOf(++i));
        }
    }

    public static void convertSaleBill(Customer customer, SaleBill saleBill) {
        if (saleBill.getType() != Constant.ScmConstant.PayWay.Grand) {// 非积分情况下，加积分
            if (saleBill.getType() == Constant.ScmConstant.SaleBillType.Inbound) {
                customer.setGrade(customer.getGrade()
                        + Math.round(saleBill.getTotActValue()));
                // customer.set
            } else {
                customer.setGrade(customer.getGrade()
                        - Math.round(saleBill.getTotActValue()));

            }
        } else {
            customer.setGrade(customer.getGrade()
                    - Math.round(saleBill.getTotActValue()));// 扣积分
        }

        saleBill.setCustomer(customer);
    }

    static public void initRecordList(SaleBill o, List<Record> listRecord) {
        Integer type = o.getType();
        // String billType=o.getBillType();
        Integer token;
        if (type == 1) {
            token = 14;
        } else {
            token = 17;
        }
        for (Record r : listRecord) {
            r.setScanTime(o.getBillDate());
            r.setSrcTaskId(new GuidCreator().toString());
            r.setId(new GuidCreator().toString());
            r.setTaskId(o.getBillNo());
            // r.setOwnerId(o.getOrigId());
            r.setCartonId(new GuidCreator().toString());
            // r.setOrigId(o.getOrigId());
            r.setToken(token);
            r.setSku(r.getStyleId() + r.getColorId() + r.getSizeId());
        }
    }

    @Deprecated
    public static void convertSaleBillRecord(SaleBill saleBill,
                                             List<Record> recordList) {
        Integer type = saleBill.getType();
        int token;
        if (type == Constant.ScmConstant.SaleBillType.Inbound) {// 零售退货入库
            token = 17;
        } else {
            token = 15;
        }
        if (CommonUtil.isNotBlank(recordList)) {
            for (Record r : recordList) {
                r.setId(new GuidCreator().toString());
                r.setTaskId(saleBill.getBillNo());
                r.setOwnerId(CacheManager.getDeviceByCode(r.getDeviceId())
                        .getOwnerId());
                r.setOrigId(CacheManager.getDeviceByCode(r.getDeviceId())
                        .getStorageId());// 门店ID
                r.setCartonId(saleBill.getBillNo());
                r.setToken(token);
                r.setSku(r.getStyleId() + r.getColorId() + r.getSizeId());
                r.setScanTime(new Date());
            }
        }
    }

    // double相减
    public static double sub(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.subtract(bd2).doubleValue();
    }

    public static String convertSaleBillRecord(SaleBill saleBill,
                                               List<SaleBillDtl> saleBillDtlList, List<Record> recordList) {
        // 计算销售额
        saleBill.setBalance(sub(saleBill.getTotOrderValue(),
                saleBill.getTotActValue()));
        Integer type = saleBill.getType();
        int token;
        int taskType = Constant.TaskType.Inbound;//入库
        if (type == Constant.ScmConstant.SaleBillType.Inbound) {// 零售退货入库
            token = 17;
        } else {
            token = 15;
            taskType = Constant.TaskType.Outbound;//出库
        }

        StringBuffer codes = new StringBuffer("'");
        String deviceId = "";
        if (CommonUtil.isNotBlank(recordList)) {
            deviceId = recordList.get(0).getDeviceId();
            String ownerId = CacheManager.getDeviceByCode(deviceId)
                    .getOwnerId();
            String storageId = CacheManager.getDeviceByCode(deviceId)
                    .getStorageId();

            Business business = new Business();
            business.setId(saleBill.getId());
            business.setBeginTime(new Date());
            business.setEndTime(new Date());
            business.setOwnerId(ownerId);
            business.setDeviceId(deviceId);
            business.setOrigId(storageId);
            business.setStatus(0);
            business.setToken(token);
            business.setType(taskType);
            business.setTotEpc(saleBill.getTotOrderQty());
            business.setTotCarton(1L);
            business.setTotSku(1L);
            business.setTotStyle(1L);
     
            List<BusinessDtl> dtlList = new ArrayList<BusinessDtl>();
            for (SaleBillDtl dtl : saleBillDtlList) {

                if (CommonUtil.isNotBlank(saleBill.getClient2Id())) {
                    dtl.setClient2Id(saleBill.getClient2Id());
                }

                if (CommonUtil.isNotBlank(saleBill.getGradeRate())) {
                    dtl.setGradeRate(saleBill.getGradeRate());
                    dtl.setIncreaseGrate(Math.round(dtl.getGradeRate() / 100
                            * dtl.getActPrice()));
                    saleBill.setIncreaseGrate(saleBill.getIncreaseGrate()
                            + dtl.getIncreaseGrate());
                }
                dtl.setActValue(dtl.getActPrice());
                codes.append(dtl.getUniqueCode()).append("','");
                dtl.setId(new GuidCreator().toString());
                dtl.setBillDate(saleBill.getBillDate());
                dtl.setQty(1);
                dtl.setCode(dtl.getStyleId() + dtl.getColorId()
                        + dtl.getSizeId());
                // dtl.setId(new GuidCreator().toString());
                BusinessDtl bdtl = new BusinessDtl();
                bdtl.setId(new GuidCreator().toString());
                bdtl.setDeviceId(deviceId);
                bdtl.setOwnerId(ownerId);
                bdtl.setOrigId(storageId);
                bdtl.setStyleId(dtl.getStyleId());
                bdtl.setColorId(dtl.getColorId());
                bdtl.setSizeId(dtl.getSizeId());
                bdtl.setQty(dtl.getQty());
                bdtl.setSku(bdtl.getStyleId() + bdtl.getColorId()
                        + bdtl.getSizeId());
                bdtl.setToken(token);
                bdtl.setType(type);
                bdtl.setTaskId(business.getId());
           
                dtlList.add(bdtl);
            }

            for (Record r : recordList) {
                r.setId(new GuidCreator().toString());
                r.setTaskId(saleBill.getBillNo());
                r.setOwnerId(ownerId);
                r.setOrigId(storageId);// 门店ID
                r.setCartonId(saleBill.getBillNo());
                r.setToken(token);
                r.setType(type);
        
                r.setSku(r.getStyleId() + r.getColorId() + r.getSizeId());
                r.setScanTime(new Date());
            }

            business.setDtlList(dtlList);
			/* saleBill.setDtlList(saleBillDtlList); */
            saleBill.setBusiness(business);
        }
        saleBill.setDtlList(saleBillDtlList);
        return codes.toString().substring(0, codes.length() - 2);
    }

    public static String convertSaleBillRecord(SaleBill saleBill,
                                               List<SaleBillDtl> saleBillDtlList, List<Record> recordList,
                                               String deviceId) {
        // 计算销售额
        saleBill.setBalance(sub(saleBill.getTotOrderValue(),
                saleBill.getTotActValue()));
        Integer type = saleBill.getType();
        int token;
        int taskType = Constant.TaskType.Inbound;//入库
        if (type == Constant.ScmConstant.SaleBillType.Inbound) {// 零售退货入库
            token = 17;
        } else {
            token = 15;
            taskType = Constant.TaskType.Outbound;//出库
        }

        StringBuffer codes = new StringBuffer("'");

        if (CommonUtil.isNotBlank(recordList)) {

            String ownerId = CacheManager.getDeviceByCode(deviceId)
                    .getOwnerId();
            String storageId = CacheManager.getDeviceByCode(deviceId)
                    .getStorageId();

            Business business = new Business();
            business.setId(saleBill.getId());
            business.setBeginTime(new Date());
            business.setEndTime(new Date());
            business.setOwnerId(ownerId);
            business.setDeviceId(deviceId);
            business.setOrigId(storageId);
            business.setStatus(0);
            business.setToken(token);
            business.setType(taskType);
            business.setTotEpc(saleBill.getTotOrderQty());
            business.setTotCarton(1L);
            business.setTotSku(1L);
            business.setTotStyle(1L);
   
            List<BusinessDtl> dtlList = new ArrayList<BusinessDtl>();
            for (SaleBillDtl dtl : saleBillDtlList) {

                if (CommonUtil.isNotBlank(saleBill.getClient2Id())) {
                    dtl.setClient2Id(saleBill.getClient2Id());
                }

                if (CommonUtil.isNotBlank(saleBill.getGradeRate())) {
                    dtl.setGradeRate(saleBill.getGradeRate());
                    dtl.setIncreaseGrate(Math.round(dtl.getGradeRate() / 100
                            * dtl.getActPrice()));
                    saleBill.setIncreaseGrate((saleBill.getIncreaseGrate()==null?0:saleBill.getIncreaseGrate())
                            + (dtl.getIncreaseGrate()));
                }
                dtl.setActValue(dtl.getActPrice());
                codes.append(dtl.getUniqueCode()).append("','");
                dtl.setId(new GuidCreator().toString());
                dtl.setBillDate(saleBill.getBillDate());
                dtl.setQty(1);
                dtl.setCode(dtl.getStyleId() + dtl.getColorId()
                        + dtl.getSizeId());
                // dtl.setId(new GuidCreator().toString());
                BusinessDtl bdtl = new BusinessDtl();
                bdtl.setId(new GuidCreator().toString());
                bdtl.setDeviceId(deviceId);
                bdtl.setOwnerId(ownerId);
                bdtl.setOrigId(storageId);
                bdtl.setStyleId(dtl.getStyleId());
                bdtl.setColorId(dtl.getColorId());
                bdtl.setSizeId(dtl.getSizeId());
                bdtl.setQty(dtl.getQty());
                bdtl.setSku(bdtl.getStyleId() + bdtl.getColorId()
                        + bdtl.getSizeId());
                bdtl.setToken(token);
                bdtl.setType(type);
                bdtl.setTaskId(business.getId());
          
                dtlList.add(bdtl);
            }

            for (Record r : recordList) {
                r.setId(new GuidCreator().toString());
                r.setTaskId(saleBill.getBillNo());
                r.setOwnerId(ownerId);
                r.setOrigId(storageId);// 门店ID
                r.setCartonId(saleBill.getBillNo());
                r.setToken(token);
                r.setType(type);
          
                r.setSku(r.getStyleId() + r.getColorId() + r.getSizeId());
                r.setScanTime(new Date());
            }

            business.setDtlList(dtlList);
					/* saleBill.setDtlList(saleBillDtlList); */
            saleBill.setBusiness(business);
        }
        saleBill.setDtlList(saleBillDtlList);
        return codes.toString().substring(0, codes.length() - 2);
    }

    public static void convertSaleBill(SaleBill saleBill,
                                       List<SaleBillDtl> saleBillDtlList, String deviceId) {
        saleBill.setBalance(sub(saleBill.getTotOrderValue(),
                saleBill.getTotActValue()));
        Integer type = saleBill.getType();
        int token;
        int taskType = Constant.TaskType.Inbound;//入库
        if (type == Constant.ScmConstant.SaleBillType.Inbound) {// 零售退货入库
            token = 17;
        } else {
            token = 15;
            taskType = Constant.TaskType.Outbound;//出库
        }

        String ownerId = CacheManager.getDeviceByCode(deviceId)
                .getOwnerId();
        String storageId = CacheManager.getDeviceByCode(deviceId)
                .getStorageId();

        Business business = new Business();
        business.setId(saleBill.getId());
        business.setBeginTime(new Date());
        business.setEndTime(new Date());
        business.setOwnerId(ownerId);
        business.setDeviceId(deviceId);
        business.setOrigId(storageId);
        business.setStatus(0);
        business.setToken(token);
        business.setType(taskType);
        business.setTotEpc(saleBill.getTotOrderQty());
        business.setTotCarton(1L);
        business.setTotSku(1L);
        business.setTotStyle(1L);
       
        List<BusinessDtl> dtlList = new ArrayList<BusinessDtl>();
        for (SaleBillDtl dtl : saleBillDtlList) {

            if (CommonUtil.isNotBlank(saleBill.getClient2Id())) {
                dtl.setClient2Id(saleBill.getClient2Id());
            }

            if (CommonUtil.isNotBlank(saleBill.getGradeRate())) {
                dtl.setGradeRate(saleBill.getGradeRate());
                dtl.setIncreaseGrate(Math.round(dtl.getGradeRate() / 100
                        * dtl.getActPrice()));
                saleBill.setIncreaseGrate(saleBill.getIncreaseGrate()
                        + dtl.getIncreaseGrate());
            }
            dtl.setActValue(dtl.getActPrice());
            dtl.setId(new GuidCreator().toString());
            dtl.setBillDate(saleBill.getBillDate());
            dtl.setQty(1);
            dtl.setCode(dtl.getStyleId() + dtl.getColorId()
                    + dtl.getSizeId());
            // dtl.setId(new GuidCreator().toString());
            BusinessDtl bdtl = new BusinessDtl();
            bdtl.setId(new GuidCreator().toString());
            bdtl.setDeviceId(deviceId);
            bdtl.setOwnerId(ownerId);
            bdtl.setOrigId(storageId);
            bdtl.setStyleId(dtl.getStyleId());
            bdtl.setColorId(dtl.getColorId());
            bdtl.setSizeId(dtl.getSizeId());
            bdtl.setQty(dtl.getQty());
            bdtl.setSku(bdtl.getStyleId() + bdtl.getColorId()
                    + bdtl.getSizeId());
            bdtl.setToken(token);
            bdtl.setType(type);
            bdtl.setTaskId(business.getId());
       
            dtlList.add(bdtl);
        }

        business.setDtlList(dtlList);
			/* saleBill.setDtlList(saleBillDtlList); */
        saleBill.setBusiness(business);

        saleBill.setDtlList(saleBillDtlList);

    }

    public static void convertToSaleBillVo(List<SaleBill> rows) {
        for (SaleBill sb : rows) {
            convertToSaleBillVo(sb);
        }

    }
    public static void convertToSaleBillVo(SaleBill sb) {
        if (CommonUtil.isNotBlank(sb.getClientId())) { //收银员
            User user = CacheManager.getUserById(sb.getClientId());
            sb.setClientName(user == null? "" :user.getName());
        }
        if(CommonUtil.isNotBlank(sb.getShopId())) {
            Unit unit = CacheManager.getUnitByCode(sb.getShopId());
            sb.setShopName(unit == null?"":unit.getName());
        }
        if(CommonUtil.isNotBlank(sb.getClient2Id())) {//会员
            Customer customer = CacheManager.getCustomerById(sb.getClient2Id());
            sb.setClient2Name(customer == null?"":customer.getName());
        }
        if(null == sb.getPayWay())
            return;
        switch(sb.getPayWay()) {
            case 0:
                sb.setPayWayName("混合");
                break;
            case 1:
                sb.setPayWayName("现金");
                break;
            case 2:
                sb.setPayWayName("积分");
                break;
            case 3:
                sb.setPayWayName("银行卡");
                break;
            case 4:
                sb.setPayWayName("代金券");
                break;
            case 5:
                sb.setPayWayName("支付宝");
                break;
            case 6:
                sb.setPayWayName("微信");
                break;
            case 7:
                sb.setPayWayName("盒子支付(支付宝扫描)");
                break;
            case 8:
                sb.setPayWayName("盒子支付(支付宝被扫)");
                break;
            case 9:
                sb.setPayWayName("盒子支付(微信扫描)");
                break;
            case 10:
                sb.setPayWayName("盒子支付(微信被扫)");
                break;
            case 11:
                sb.setPayWayName("盒子支付(刷卡)");
                break;
            case 12:
                sb.setPayWayName("盒子支付(现金)");
                break;
        }

    }

    public static SaleBill initZuoSaleData(SaleOrderVo billVo,
                                           List<SaleOrderDtlVo> dtlVoList) {
        List<Record> recordList = new ArrayList<Record>();

        SaleBill saleBill = convertToSaleBill(billVo);

        List<SaleBillDtl> dtlList = new ArrayList<SaleBillDtl>();
        for (SaleOrderDtlVo vo : dtlVoList) {
            if (CommonUtil.isNotBlank(vo.getUniqueCode())) {
                continue;
            }
            SaleBillDtl dtl = convertToSaleBillDtl(saleBill, vo);
            dtlList.add(dtl);
            Record r = new Record();
            r.setCode(dtl.getUniqueCode());
            List<Device> deviceList = CacheManager.getDevices(
                    saleBill.getShopId(), "KE");
            r.setDeviceId(deviceList.get(0).getCode());
            r.setStyleId(dtl.getStyleId());
            r.setColorId(dtl.getColorId());
            r.setSizeId(dtl.getSizeId());
            recordList.add(r);
        }
        convertSaleBillRecord(saleBill, dtlList, recordList);
        saleBill.setDtlList(dtlList);
        saleBill.setRecordList(recordList);
        return saleBill;
    }

    private static SaleBillDtl convertToSaleBillDtl(SaleBill saleBill,
                                                    SaleOrderDtlVo voDtl) {
        SaleBillDtl dtl = new SaleBillDtl();
        dtl.setBillId(saleBill.getId());
        dtl.setBillNo(saleBill.getBillNo());
        dtl.setId(saleBill.getId() + "-" + voDtl.getUniqueCode());
        String code = voDtl.getUniqueCode();
        String[] codeList = code.split(";");
        String styleId = codeList[0];
        String colorId = codeList[1].length() == 2 ? ("0" + codeList[1])
                : codeList[1];
        String sizeId = codeList[3].length() == 2 ? ("Z" + codeList[2])
                : codeList[2];
        dtl.setUniqueCode(styleId + colorId + sizeId + codeList[3]);
        dtl.setCode(styleId + codeList[1] + codeList[2]);
        dtl.setQty(voDtl.getSaleQty());
        dtl.setPrice(voDtl.getTagPrice());
        dtl.setActPrice(voDtl.getActSaleAmount());
        dtl.setPercent(dtl.getActPrice() / dtl.getPrice());
        dtl.setActValue(voDtl.getActSaleAmount());
        dtl.setPriceType(voDtl.getPriceType());
        return dtl;
    }

    private static SaleBill convertToSaleBill(SaleOrderVo billVo) {
        SaleBill saleBill = new SaleBill();
        saleBill.setShopId(billVo.getStoreCode().trim());
        saleBill.setId(billVo.getSaleNoCode() + "-" + saleBill.getShopId());
        saleBill.setBillNo(billVo.getSaleNoCode());
        saleBill.setOwnerId(CacheManager.getUnitById(saleBill.getShopId())
                .getOwnerId());
        saleBill.setBillDate(new Date());
        saleBill.setClientId(billVo.getSaleUserCode());
        saleBill.setClient2Id(billVo.getCustomerCode());

        if (billVo.getReSaleQty() != 0) {// 顾客退货
            saleBill.setType(Constant.ScmConstant.SaleBillType.Inbound);
            saleBill.setTotOrderQty((long) billVo.getReSaleQty());
            saleBill.setTotOrderValue((long) billVo.getActSaleAmount()
                    + billVo.getDiscountAmout());
            saleBill.setTotActValue(billVo.getRealSaleAmount());
        } else {// 销售
            saleBill.setType(Constant.ScmConstant.SaleBillType.Outbound);
            saleBill.setTotOrderQty((long) billVo.getActSaleQty());
            saleBill.setTotOrderValue(billVo.getActSaleAmount()
                    + billVo.getDiscountAmout());
            saleBill.setTotActValue(billVo.getRealSaleAmount());
        }

        saleBill.setActCardValue(billVo.getCrCardAmount());
        saleBill.setActCashValue(billVo.getCashAmount());
        saleBill.setActGradeValue(billVo.getUsedIntegral());
        saleBill.setActVoucherValue(billVo.getGiftAmount());

        saleBill.setIncreaseGrate(billVo.getIncreaseIntegral());
        saleBill.setMileageCode(billVo.getMileageCode());
        saleBill.setGradeRate(billVo.getMileageRate());

        saleBill.setBalance(billVo.getDiscountAmout());// 差额
        saleBill.setToZero(billVo.getLooseAmount());// 抹零

        return saleBill;
    }
    public static ChartVo convertToChartVo(List<TrendVo> voList) throws Exception {
        ChartVo<Integer, String, long[]> chartVo = new ChartVo<Integer, String, long[]>();

        for (TrendVo vo : voList) {
            Date d = CommonUtil.converStrToDate(
                    "" + vo.getYear() + "-" + vo.getMonth() + "-" + vo.getDay(), "yyyy-mm-dd");
            long time = d.getTime();
            String name =vo.getOwnerId();// CacheManager.getUnitById(vo.getOwnerId()).getName();
            boolean isHave = false;
            for (ChartVo.Serie s : chartVo.getSeries()) {
                if (s.getName().equals(name)) {
                    long[] dataArr = new long[2];
                    dataArr[0] = time;
                    dataArr[1] = vo.getTotQty();
                    s.addData(dataArr);
                    isHave = true;
                    break;
                }
            }
            if (!isHave) {
                ChartVo<Integer, String, long[]>.Serie serie = new ChartVo<Integer, String, long[]>().new Serie();
                serie.setName(name);
                long[] dataArr = new long[2];
                dataArr[0] = time;
                dataArr[1] = vo.getTotQty();
                serie.addData(dataArr);
                chartVo.addSerie(serie);
            }
        }

        for (ChartVo.Serie s : chartVo.getSeries()) {
            ChartVo<Integer, String, long[]>.DataComparator comparator = new ChartVo<Integer, String, long[]>().new DataComparator();
            Collections.sort(s.getData(), comparator);
        }
        return chartVo;
    }


}
