package com.casesoft.dmc.controller.syn.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.task.*;

import java.util.*;

/**
 * Created by john on 2017/1/12.
 */
public class ZipFileConvert {
    /**
     * 转换成任务
     *
     * @param json
     * @exception Exception
     * @return
     */
    private static Business fileJsonConvertToBus(String json) throws Exception {
        Business business = new Business();
       
        JSONObject jsonObj = JSON.parseObject(json);
        business.setOperator(jsonObj.getString("operator"));
        business.setId(jsonObj.getString("taskId"));
        business.setRmId(jsonObj.getString("rmId"));
        business.setStatus(("finish".equals(jsonObj.getString("flagTaskStatus"))) ? 0
                : -1);
/*
        business.setRemark(jsonObj.getString("remark"));
*/
        business.setDeviceId(jsonObj.getString("taskDeviceId"));
        if (CommonUtil.isBlank(CacheManager.getDeviceByCode(business.getDeviceId()))) {
            throw new Exception("设备号未注册！");
        }
       if(jsonObj.getString("taskBeginTime").contains("-")) {
            business.setBeginTime(CommonUtil.converStrToDate(
                    jsonObj.getString("taskBeginTime"), "yyyy-MM-dd hh:mm:ss"));
            business.setEndTime(CommonUtil.converStrToDate(
                    jsonObj.getString("taskEndTime"), "yyyy-MM-dd hh:mm:ss"));
        } else {
            business.setBeginTime(CommonUtil.converStrToDate(
                    jsonObj.getString("taskBeginTime"), "yyyy/MM/dd hh:mm:ss"));
            business.setEndTime(CommonUtil.converStrToDate(
                    jsonObj.getString("taskEndTime"), "yyyy/MM/dd hh:mm:ss"));
        }
        business.setTotEpc(jsonObj.getLongValue("taskTotEpc"));
        business.setTotCarton(jsonObj.getLongValue("taskTotCarton"));
        business.setTotSku(jsonObj.getLongValue("taskTotSku"));
        business.setTotStyle(jsonObj.getLongValue("taskTotStyle"));
        business.setToken(jsonObj.getIntValue("taskRfidToken"));
        if(CommonUtil.isBlank(jsonObj.getString("remark"))){
            business.setRemark(jsonObj.getString("taskRemark"));
        }else{
            business.setRemark(jsonObj.getString("remark"));
        }


        business.setOwnerId(CacheManager
                .getDeviceByCode(business.getDeviceId()).getOwnerId());
        int type = tokenMapperTypeAndUnit(jsonObj,business);
        business.setType(type);


        business.setSrcTaskId(jsonObj.getString("srcTaskId"));
        List<Record> recordList = new ArrayList<Record>();
        business.setRecordList(recordList);
        return business;
    }
    /**
     * 转换成单据
     *
     * @param json
     * @exception Exception
     * @return
     */
    private static Bill fileJsonConvertToBill(String json){
        Bill bill=null;
        if(CommonUtil.isNotBlank(json)){
            JSONObject jsonObject=JSON.parseObject(json);
            bill=JSON.parseObject(json,Bill.class);
            bill.setDestId(jsonObject.getString("storage2Id"));
            bill.setDestUnitId(jsonObject.getString("unit2Id"));
            bill.setOrigId(jsonObject.getString("storageId"));
            bill.setOrigUnitId(jsonObject.getString("unitId"));
        }
        return bill;
    }
    /**
     *
     * 转换单据明细
     * @param json
     * @param bill
     * @return
     * */
    private static boolean fileJsonConvertToBillDtl(String json,Bill bill){
        List<BillDtl> dtlList = new ArrayList<BillDtl>();
        JSONArray jsonArray = JSON.parseArray(json);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            BillDtl dtl = new BillDtl();
            String dtlId = jsonObj.getString("id");
            dtl.setId(CommonUtil.isBlank(dtlId) ? new GuidCreator().toString()
                    : dtlId);
            dtl.setScanQty(jsonObj.getLongValue("scanQty"));
            dtl.setBillId(bill.getId());
            dtl.setBillNo(bill.getBillNo());
            dtl.setSku(jsonObj.getString("sku"));
            dtl.setStyleId(jsonObj.getString("styleId"));
            dtl.setColorId(jsonObj.getString("colorId"));
            dtl.setSizeId(jsonObj.getString("sizeId"));
            dtl.setQty(jsonObj.getLongValue("qty"));
            dtl.setActQty(jsonObj.getLongValue("actQty"));
            dtl.setManualQty(jsonObj.getLongValue("manualQty"));
            dtl.setPreManualQty(jsonObj.getLongValue("preManualQty"));
            dtlList.add(dtl);
        }
        bill.setDtlList(dtlList);
        return !CommonUtil.isBlank(dtlList);

    }

    private static int tokenMapperTypeAndUnit(JSONObject jsonObj, Business business) {
        int token = business.getToken().intValue();
        String storageId = jsonObj.getString("storageId");
        if(CommonUtil.isBlank(storageId)) {
            storageId = CacheManager.getDeviceByCode(
                    business.getDeviceId()).getStorageId();
        }
        Unit storage = CacheManager.getUnitById(storageId);
  //      business.setOrigId(storageId);

        Integer unit2Type = jsonObj.getInteger("unit2Type");


        int type = -1;//非出入库和盘点
        switch (token) {
            //入库
            case Constant.Token.Storage_Inbound:// = 8;//
            case Constant.Token.Storage_Inbound_customer:
            case Constant.Token.Storage_Consigment_Inbound:
                if (CommonUtil.isNotBlank(unit2Type)) {//发货方供应商
                    business.setOrigUnitId(jsonObj.getString("unit2Id"));
                    business.setOrigId(jsonObj.getString("storage2Id"));
                }
                business.setDestUnitId(business.getOwnerId());
                business.setDestId(storageId);
                type = Constant.TaskType.Inbound;//1;
                break;
            case Constant.Token.Storage_Refund_Inbound://= 23;//门店退给总部
                if (CommonUtil.isNotBlank(unit2Type)) {
                    business.setDestUnitId(business.getOwnerId());
                    business.setDestId(jsonObj.getString("unit2Id"));
                }
                business.setOrigUnitId(business.getOwnerId());
                business.setOrigId(storageId);
                type = Constant.TaskType.Inbound;//1;
                break;
            case Constant.Token.Storage_Transfer_Inbound:// = 25;
                if (CommonUtil.isNotBlank(unit2Type)) {
                    business.setOrigUnitId(business.getOwnerId());
                    business.setOrigId(storageId);
                }
                business.setDestUnitId(business.getOwnerId());
                business.setDestId(jsonObj.getString("unit2Id"));
                type = Constant.TaskType.Inbound;//1;
                break;
            case Constant.Token.Storage_Adjust_Inbound:// = 29;
                business.setDestUnitId(business.getOwnerId());
                business.setDestId(storageId);
                business.setOrigUnitId(business.getOwnerId());
                business.setOrigId(storageId);
                type = Constant.TaskType.Inbound;//1;
                break;
            case Constant.Token.Shop_Adjust_Inbound:// = 31;
                business.setDestUnitId(business.getOwnerId());
                business.setDestId(storageId);
                business.setOrigUnitId(business.getOwnerId());
                business.setOrigId(storageId);
                type = Constant.TaskType.Inbound;//1;
                break;
            case Constant.Token.Shop_Inbound:// = 14;//门店入库
                if (CommonUtil.isNotBlank(unit2Type)) {
                    business.setOrigUnitId(business.getOwnerId());
                    business.setOrigId(jsonObj.getString("unit2Id"));

                }
                business.setDestUnitId(business.getOwnerId());
                business.setDestId(storageId);
                type = Constant.TaskType.Inbound;//1;
                break;
            case Constant.Token.Shop_Transfer_Inbound:// = 19;
                if (CommonUtil.isNotBlank(unit2Type)) {
                    business.setOrigUnitId(business.getOwnerId());
                    business.setOrigId(jsonObj.getString("unit2Id"));

                }
                business.setDestUnitId(business.getOwnerId());
                business.setDestId(storageId);
                type = Constant.TaskType.Inbound;//1;
                break;
            case Constant.Token.Shop_Sales_refund:// = 17;
                if (CommonUtil.isNotBlank(unit2Type)) {
                    business.setOrigUnitId(jsonObj.getString("unit2Id"));
                    business.setOrigId(jsonObj.getString("storage2Id"));

                }
                business.setDestUnitId(business.getOwnerId());
                business.setDestId(storageId);
                type = Constant.TaskType.Inbound;//1;
                break;
            case Constant.Token.Storage_Inbound_agent_refund://代理商退给总部
                if (CommonUtil.isNotBlank(unit2Type)) {
                    business.setOrigUnitId(jsonObj.getString("unit2Id"));
                    business.setOrigId(jsonObj.getString("storage2Id"));
                }
                business.setDestUnitId(business.getOwnerId());
                business.setDestId(storageId);
                type = Constant.TaskType.Inbound;//1;
                break;
            //出库
            case Constant.Token.Label_Data_Send://  = 5;//品牌商将标签发放给供应商
            case Constant.Token.Factory_Box_Send://  = 7;//加工厂装箱发货
            case Constant.Token.Storage_Refund_Outbound://  = 26;//退货给供应商
            case Constant.Token.Storage_refoundOut_customer:
                if (CommonUtil.isNotBlank(unit2Type)) {
                    business.setDestUnitId(jsonObj.getString("unit2Id"));
                    business.setDestId(jsonObj.getString("storage2Id"));

                }
                business.setOrigUnitId(business.getOwnerId());
                business.setOrigId(storageId);
                type = Constant.TaskType.Outbound;//0;
                break;
            case Constant.Token.Storage_Transfer_Outbound://  = 24;
                if (CommonUtil.isNotBlank(unit2Type)) {
                    business.setDestUnitId(business.getOwnerId());
                    business.setDestId(jsonObj.getString("unit2Id"));

                }
                business.setOrigUnitId(business.getOwnerId());
                business.setOrigId(storageId);
                type = Constant.TaskType.Outbound;//0;
                break;
            case Constant.Token.Storage_Outbound://  = 10;
                if (CommonUtil.isNotBlank(unit2Type)) {
                    business.setDestUnitId(business.getOwnerId());
                    business.setDestId(jsonObj.getString("unit2Id"));

                }
                business.setOrigUnitId(business.getOwnerId());
                business.setOrigId(storageId);
                type = Constant.TaskType.Outbound;//0;
                break;
            case Constant.Token.Storage_Adjust_Outbound:// = 28;
                business.setDestUnitId(business.getOwnerId());
                business.setDestId(storageId);
                business.setOrigUnitId(business.getOwnerId());
                business.setOrigId(storageId);
                type = Constant.TaskType.Outbound;//0;
                break;
            case Constant.Token.Shop_Sales://  = 15;
                business.setDestUnitId(jsonObj.getString("unit2Id"));
                business.setDestId(jsonObj.getString("storage2Id"));
                business.setOrigUnitId(business.getOwnerId());
                business.setOrigId(storageId);
                type = Constant.TaskType.Outbound;//0;
                break;
            case Constant.Token.Shop_Refund_Outbound://  = 27;
                if (CommonUtil.isNotBlank(unit2Type)) {
                    business.setDestUnitId(business.getOwnerId());
                    business.setDestId(jsonObj.getString("storage2Id"));

                }
                business.setOrigUnitId(business.getOwnerId());
                business.setOrigId(storageId);
                type = Constant.TaskType.Outbound;//0;
                break;
            case Constant.Token.Shop_Adjust_Outbound://  = 30;
                business.setDestUnitId(business.getOwnerId());
                business.setDestId(storageId);
                business.setOrigUnitId(business.getOwnerId());
                business.setOrigId(storageId);
                type = Constant.TaskType.Outbound;//0;
                break;
            case Constant.Token.Shop_Transfer_Outbound://  = 18;
                if (CommonUtil.isNotBlank(unit2Type)) {
                    business.setDestUnitId(business.getOwnerId());
                    business.setDestId(jsonObj.getString("unit2Id"));

                }
                business.setOrigUnitId(business.getOwnerId());
                business.setOrigId(storageId);
                type = Constant.TaskType.Outbound;//0;
                break;
            case Constant.Token.Storage_Outbound_agent:
                if (CommonUtil.isNotBlank(unit2Type)) {
                    business.setDestUnitId(jsonObj.getString("unit2Id"));
                    business.setDestId(jsonObj.getString("storage2Id"));

                }
                business.setOrigUnitId(business.getOwnerId());
                business.setOrigId(storageId);
                type = Constant.TaskType.Outbound;//0;
                break;
            //盘点
            case Constant.Token.Storage_Inventory:// = 9;
                business.setDestUnitId(business.getOwnerId());
                business.setDestId(storageId);
                business.setOrigUnitId(business.getOwnerId());
                business.setOrigId(storageId);
                type = Constant.TaskType.Inventory;//3
                break;
            case Constant.Token.Shop_Inventory://  = 16;
                business.setDestUnitId(business.getOwnerId());
                business.setDestId(storageId);
                business.setOrigUnitId(business.getOwnerId());
                business.setOrigId(storageId);
                type = Constant.TaskType.Inventory;//3
                break;
            //其他
            case Constant.Token.Label_Data_Birth:// = 1;
            case Constant.Token.Label_Data_Write:// = 2;//标签初始化
            case Constant.Token.Label_Data_Detect:// = 3;
                type = Constant.TaskType.Else;//-1
                break;
            default:
                type = Constant.TaskType.Else;//-1
        }
        business.setType(type);
        return type;
    }

    /**
     * 上传文件转换成箱
     * @param json
     * @param businessDtlHashMap
     * @param boxId
     * @exception Exception
     * @return
     */
    private static Box fileJsonConvertToBox(String json, Business bus, Map<String, BusinessDtl> businessDtlHashMap,
                                            String boxId) throws Exception {
        try {
            Box box = new Box();
            List<BoxDtl> boxDtls = new ArrayList<>();
            JSONObject boxJsonObj = JSON.parseObject(json);// 主箱信息
            JSONObject o = boxJsonObj.getJSONObject("carton");
            box.setId(new GuidCreator().toString());
            box.setTaskId(bus.getId());
            box.setDeviceId(bus.getDeviceId());
            box.setOwnerId(bus.getOwnerId());
            box.setToken(bus.getToken());

            box.setCartonId(boxId);
            box.setTotEpc(o.getLongValue("cartonTotEpc"));
            box.setTotSku(o.getLongValue("cartonTotSku"));
            box.setTotStyle(o.getLongValue("cartonTotStyle"));
            if(o.getString("cartonScanTime").contains("-")) {
                box.setScanTime(CommonUtil.converStrToDate(
                        o.getString("cartonScanTime"), "yyyy-MM-dd hh:mm:ss"));

            } else {
                box.setScanTime(CommonUtil.converStrToDate(
                        o.getString("cartonScanTime"),"yyyy/MM/dd hh:mm:ss"));
            }
            box.setSeqNo(o.getInteger("autoId"));
            box.setType(bus.getType());

            /**
             * 箱明细
             * */
            JSONArray jsonArray = boxJsonObj.getJSONArray("cartonDtl");
            Iterator<Object> it = jsonArray.iterator();
            while (it.hasNext()) {
                JSONObject jsonObj = (JSONObject) it.next();
                BoxDtl dtl = new BoxDtl();
                dtl.setCartonId(boxId);
                dtl.setId(new GuidCreator().toString());
                dtl.setOwnerId(bus.getOwnerId());
                dtl.setDeviceId(bus.getDeviceId());
                dtl.setTaskId(bus.getId());
                dtl.setToken(bus.getToken());
            
                dtl.setSku(jsonObj.getString("sku"));
                dtl.setStyleId(jsonObj.getString("styleId"));
                dtl.setColorId(jsonObj.getString("colorId"));
                dtl.setSizeId(jsonObj.getString("sizeId"));
                dtl.setQty(Long.parseLong(jsonObj.getString("skuCount")));
                dtl.setType(bus.getType());

                boxDtls.add(dtl);
                // 增加到dtl
                if (businessDtlHashMap.containsKey(dtl.getSku())) {
                    BusinessDtl busDtl = businessDtlHashMap.get(dtl.getSku());
                    busDtl.setQty(busDtl.getQty() + dtl.getQty());
                } else {
                    BusinessDtl busDtl = new BusinessDtl();
                    busDtl.setId(new GuidCreator().toString());
                    busDtl.setOwnerId(bus.getOwnerId());
                    busDtl.setDeviceId(bus.getDeviceId());
                    busDtl.setTaskId(bus.getId());
                    busDtl.setToken(bus.getToken());
                    busDtl.setOrigId(bus.getOrigId());
                    busDtl.setSku(jsonObj.getString("sku"));
                    busDtl.setStyleId(jsonObj.getString("styleId"));
                    busDtl.setColorId(jsonObj.getString("colorId"));
                    busDtl.setSizeId(jsonObj.getString("sizeId"));
                    busDtl.setQty(Long.parseLong(jsonObj.getString("skuCount")));
                    busDtl.setType(bus.getType());

                    busDtl.setDestUnitId(bus.getDestUnitId());
                    busDtl.setDestId(bus.getDestId());

                    businessDtlHashMap.put(busDtl.getSku(), busDtl);
                }
                // 增加Record
                JSONArray jsonArr = jsonObj.getJSONArray("epcList");
                if(dtl.getQty() != jsonArr.size()){
                    BusinessDtl busDtl = businessDtlHashMap.get(dtl.getSku());
                    busDtl.setQty(jsonArr.size());
                    dtl.setQty(jsonArr.size());
                }
                for (Object epcStr : jsonArr) {
                    Record r = new Record();
                    r.setId(new GuidCreator().toString());
                    r.setCartonId(boxId);
                    r.setDeviceId(bus.getDeviceId());
                    r.setTaskId(bus.getId());
                    r.setOwnerId(bus.getOwnerId());
                    r.setCode(epcStr.toString());
                    r.setOrigId(bus.getOrigId());
                    r.setToken(bus.getToken());
                    r.setScanTime(box.getScanTime());
                    r.setSku(dtl.getSku());
                    r.setStyleId(dtl.getStyleId());
                    r.setSizeId(dtl.getSizeId());
                    r.setColorId(dtl.getColorId());
                    r.setType(bus.getType());

                    r.setDestUnitId(bus.getDestUnitId());
                    r.setDestId(bus.getDestId());
                    bus.getRecordList().add(r);
                }
            }
            box.setBoxDtls(boxDtls);
            return box;
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("箱明细解析失败");
        }
    }

    /**
     * 挑拣任务文件
     *
     * @param zipMap
     * @exception Exception
     * @return
     */
    public static Business pickBusinessFile(Map<String, StringBuffer> zipMap) throws Exception {
        Business bus = null;
        // 获取主任务信息
        for (Map.Entry<String, StringBuffer> zipContent : zipMap.entrySet()) {
            String fileName = zipContent.getKey();
            String[] subName = fileName.split("\\.");
            String fileContent = zipContent.getValue().toString();
            if (CommonUtil.isNotBlank(subName)) {
                String suffix = subName[subName.length - 1];
                if (suffix.equals("tsk")) {
                    return ZipFileConvert.fileJsonConvertToBus(fileContent);
                }
            }
        }
        return bus;
    }

    /**
     * 挑拣箱文件(添加任务明细，record记录)
     *
     * @param zipMap
     * @param business
     * @exception Exception
     * @return
     */
    public static boolean pickBoxFile(Map<String, StringBuffer> zipMap, Business business) throws Exception {
        List<Box> boxList = new ArrayList<>();
        Map<String, BusinessDtl> businessDtlHashMap = new HashMap<String, BusinessDtl>();
        // 获取主任务信息
        for (Map.Entry<String, StringBuffer> zipContent : zipMap.entrySet()) {
            String fileName = zipContent.getKey();
            String[] subName = fileName.split("\\.");
            String fileContent = zipContent.getValue().toString();
            if (CommonUtil.isNotBlank(subName)) {
                String suffix = subName[subName.length - 1];
                if (suffix.equals("ctn")) {
                    String boxId = fileName.substring(0, fileName.indexOf(".ctn"));
                    Box box = ZipFileConvert.fileJsonConvertToBox(fileContent, business, businessDtlHashMap, boxId);
                    boxList.add(box);
                }
            }
        }
        if(CommonUtil.isBlank(boxList)){
            return false;
        }else {
            business.setBoxList(boxList);
            business.setDtlList(new ArrayList<>(businessDtlHashMap.values()));
            return true;
        }
     }
    /**
     * 挑拣单据
     *
     * @param zipMap
     * @param business
     * @return
     */
    public static Bill pickBillFile(Map<String, StringBuffer> zipMap, Business business) throws Exception {
        Bill bill=null;
        try {
            // 获取主任务信息
            for (Map.Entry<String, StringBuffer> zipContent : zipMap.entrySet()) {
                String fileName = zipContent.getKey();
                String[] subName = fileName.split("\\.");
                String fileContent = zipContent.getValue().toString();
                if (CommonUtil.isNotBlank(subName)) {
                    String suffix = subName[subName.length - 1];
                    if (suffix.endsWith("bill")) {
                        bill= fileJsonConvertToBill(fileContent);
                        bill.setActSkuQty(business.getTotSku());
                        bill.setBoxQty(business.getTotCarton());
                        bill.setOwnerId(business.getOwnerId());
                        if(CommonUtil.isBlank(bill.getStatus())){
                            bill.setStatus(1);
                        }
                        bill.setTaskId(business.getId());
                        business.setBillId(bill.getId());
                        business.setBillNo(bill.getBillNo());
                        business.setBill(bill);
                        business.setStatus(0);
                        return bill;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("单据解析失败！");
        }

        return bill;
    }
    /**
     * 挑拣单据明细
     * @param zipMap
     * @param bill
     * @exception  Exception
     * @return
     */
    public static boolean pickBillDtlFile(Map<String, StringBuffer> zipMap, Bill bill) throws Exception {
        // 获取主任务信息
        for (Map.Entry<String, StringBuffer> zipContent : zipMap.entrySet()) {
            String fileName = zipContent.getKey();
            String[] subName = fileName.split("\\.");
            String fileContent = zipContent.getValue().toString();
            if (CommonUtil.isNotBlank(subName)) {
                String suffix = subName[subName.length - 1];
                if (suffix.equals("dtl")) {
                     return ZipFileConvert.fileJsonConvertToBillDtl(fileContent, bill);
                }
            }
        }
        return false;
    }
}
