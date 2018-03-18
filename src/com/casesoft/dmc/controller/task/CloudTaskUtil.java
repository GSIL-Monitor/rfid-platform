package com.casesoft.dmc.controller.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.task.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by WingLi on 2014/11/23.
 */
public class CloudTaskUtil {
    private Logger logger = LoggerFactory.getLogger(TaskUtil2.class);
    private List<CloudRecord> recordList = null;
    private Map<String, CloudBusinessDtl> dtlMap = null;
    private Map<String, CloudBox> boxMap = null;

    public CloudBusiness convertToDto(File taskZipFile) throws Exception {
        ZipFile zipFile = null;
        CloudBusiness bus = null;
        Map<String, StringBuffer> zipMap = new HashMap<String, StringBuffer>();
        try {
            // wing 2014-03-04 中文乱码bug解决
            InputStream ins = new FileInputStream(taskZipFile);
            ZipInputStream zipInput = new ZipInputStream(ins);
            ZipEntry zipEntry = null;

            BufferedReader in = null;
            while ((zipEntry = zipInput.getNextEntry()) != null) {

                in = new BufferedReader(
                        new InputStreamReader(zipInput, "UTF-8"));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = in.readLine()) != null) {
                    buffer.append(line);
                }

                zipMap.put(zipEntry.getName(), buffer);
                zipInput.closeEntry();

            }
            zipInput.close();
            ins.close();
            if (in != null) {
                in.close();
            }

            bus = convertToDtl(zipMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("解析ZIP压缩文件出错！！！");
        } finally {
            if (zipFile != null) {
                // zipFile.close();
            }
        }
        return bus;
    }

    public  CloudBusiness convertToDtl(Map<String, StringBuffer> zipMap)
            throws Exception {
        CloudBusiness bus = null;
        List<CloudBox> boxList = new ArrayList<CloudBox>();
        Map<String, CloudBusinessDtl> dtlMap = new HashMap<String, CloudBusinessDtl>();
        // 获取主任务信息
        for (String zipFileName : zipMap.keySet()) {
            if (!zipFileName.contains(".tsk")) {
                continue;
            }

            bus = jsonToBus(zipMap.get(zipFileName).toString());

            List<CloudRecord> recordList = new ArrayList<CloudRecord>();
            //bus.setRecordList(recordList);
            bus.setCloudRecordList(recordList);

            break;
        }
        // 获取箱信息
        for (String zipFileName : zipMap.keySet()) {
            if (!zipFileName.contains(".ctn")) {
                continue;
            }
            String boxId = zipFileName.substring(zipFileName.indexOf("CTN"),
                    zipFileName.indexOf(".ctn"));
            CloudBox box = jsonToCloudBox(zipMap.get(zipFileName).toString(), bus,
                    dtlMap, boxId);

            boxList.add(box);
        }

        // 获取单据信息
        for (String zipFileName : zipMap.keySet()) {
            if (!zipFileName.contains(".bill"))
                continue;
            Bill bill = jsonToBill(zipMap.get(zipFileName).toString());

            bill.setActSkuQty(bus.getTotSku());
            bill.setBoxQty(bus.getTotCarton());
            bill.setOwnerId(bus.getOwnerId());
            bill.setStatus(1);
            bill.setTaskId(bus.getId());

            bus.setBillId(bill.getId());
            bus.setBillNo(bill.getBillNo());
            bus.setBill(bill);
            // 2014-03-11 wing add
            bus.setStatus(1);
            break;
        }

        // 获取单据详细信息
        for (String zipFileName : zipMap.keySet()) {
            if (!zipFileName.contains(".dtl"))
                continue;
            String dtlJsonStr = zipMap.get(zipFileName).toString();
            List<BillDtl> dtlList = jsonToBillDtlList(dtlJsonStr, bus.getBill());// JSON.parseArray(dtlJsonStr,
            // BillDtl.class);
            bus.getBill().setDtlList(dtlList);
            break;
        }

        //bus.setBoxList(boxList);
        //bus.setDtlList(new ArrayList(dtlMap.values()));

        bus.setCloudBoxList(boxList);
        bus.setCloudBusinessDtlList(new ArrayList<CloudBusinessDtl>(dtlMap.values()));

        preInfo(bus);
        return bus;
    }

    private CloudBusiness jsonToBus(String json) throws Exception {
        CloudBusiness business = new CloudBusiness();
        JSONObject jsonObj = JSON.parseObject(json);

        business.setId(jsonObj.getString("taskId"));
        business.setStatus(("finish".equals(jsonObj.getString("flagTaskStatus"))) ? 0
                : -1);

        business.setDeviceId(jsonObj.getString("taskDeviceId"));
        business.setOwnerId(CacheManager
                .getDeviceByCode(business.getDeviceId()).getOwnerId());
        business.setStorageId(CacheManager.getDeviceByCode(
                business.getDeviceId()).getStorageId());

        business.setBeginTime(CommonUtil.converStrToDate(
                jsonObj.getString("taskBeginTime"), "yyyy/MM/dd hh:mm:ss"));
        business.setEndTime(CommonUtil.converStrToDate(
                jsonObj.getString("taskEndTime"), "yyyy/MM/dd hh:mm:ss"));
        business.setTotEpc(jsonObj.getLongValue("taskTotEpc"));
        business.setTotCarton(jsonObj.getLongValue("taskTotCarton"));
        business.setTotSku(jsonObj.getLongValue("taskTotSku"));
        business.setTotStyle(jsonObj.getLongValue("taskTotStyle"));
        business.setToken(jsonObj.getIntValue("taskRfidToken"));

        String storageId = jsonObj.getString("storageId");
        if(CommonUtil.isNotBlank(storageId)) {
            business.setStorageId(storageId);
        }
        Integer unit2Type = jsonObj.getInteger("unit2Type");
        if(CommonUtil.isNotBlank(unit2Type)) {
            business.setUnit2Type(unit2Type);
            business.setUnit2Id(jsonObj.getString("unit2Id"));
            business.setStorage2Id(jsonObj.getString("storage2Id"));
        }
        int type = tokenMapperType(business.getToken().intValue());//2014-12-18 wing add
        business.setType(type);
        return business;
    }
    private int tokenMapperType(int token) {
        int type = -1;//非出入库和盘点
        switch(token) {
            case Constant.Token.Label_Data_Receive:// = 4;//品牌商标签接收
            case Constant.Token.Label_Data_Feedback:// = 6;//供应商接收到标签
            case Constant.Token.Storage_Inbound:// = 8;//
            case Constant.Token.Storage_Refund_Inbound://= 23;//代理商或门店退给总部
            case Constant.Token.Storage_Transfer_Inbound:// = 25;
            case Constant.Token.Storage_Adjust_Inbound:// = 29;
            case Constant.Token.Shop_Adjust_Inbound:// = 31;
            case Constant.Token.Shop_Inbound:// = 14;//门店入库
            case Constant.Token.Shop_Transfer_Inbound:// = 19;
            case Constant.Token.Shop_Sales_refund:// = 17;
                type = Constant.TaskType.Inbound;//1;
                break;

            case Constant.Token.Label_Data_Send://  = 5;//品牌商将标签发放给供应商
            case Constant.Token.Factory_Box_Send://  = 7;//加工厂装箱发货
            case Constant.Token.Storage_Refund_Outbound://  = 26;//退货给供应商
            case Constant.Token.Storage_Transfer_Outbound://  = 24;
            case Constant.Token.Storage_Outbound://  = 10;
            case Constant.Token.Storage_Adjust_Outbound :// = 28;
            case Constant.Token.Shop_Sales://  = 15;
            case Constant.Token.Shop_Refund_Outbound://  = 27;
            case Constant.Token.Shop_Adjust_Outbound://  = 30;
            case Constant.Token.Shop_Transfer_Outbound://  = 18;
                type = Constant.TaskType.Outbound;//0;
                break;

            case Constant.Token.Storage_Inventory :// = 9;
            case Constant.Token.Shop_Inventory://  = 16;
                type = Constant.TaskType.Inventory;//3
                break;

            case Constant.Token.Label_Data_Birth:// = 1;
            case Constant.Token.Label_Data_Write:// = 2;//标签初始化
            case Constant.Token.Label_Data_Detect:// = 3;
                type = Constant.TaskType.Else;//-1
                break;
            default: type = Constant.TaskType.Else;//-1
        }
        return type;
    }
    private CloudBox jsonToCloudBox(String json, CloudBusiness bus,
                          Map<String, CloudBusinessDtl> dtlMap, String boxId) throws Exception {
        CloudBox box = new CloudBox();
        List<CloudBoxDtl> dtlList = new ArrayList<CloudBoxDtl>();

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
        String scanTimeStr = o.getString("cartonScanTime");
        box.setScanTime(CommonUtil.converStrToDate(scanTimeStr,
                "yyyy/MM/dd hh:mm:ss"));
        box.setSeqNo(o.getInteger("autoId"));

        JSONArray jsonArray = boxJsonObj.getJSONArray("cartonDtl");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            CloudBoxDtl dtl = new CloudBoxDtl();
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
            dtlList.add(dtl);

            // 增加到dtl
            if (dtlMap.containsKey(dtl.getSku())) {
                CloudBusinessDtl busDtl = dtlMap.get(dtl.getSku());
                busDtl.setQty(busDtl.getQty() + dtl.getQty());
            } else {
                CloudBusinessDtl busDtl = new CloudBusinessDtl();
                busDtl.setId(new GuidCreator().toString());
                busDtl.setOwnerId(bus.getOwnerId());
                busDtl.setDeviceId(bus.getDeviceId());
                busDtl.setTaskId(bus.getId());
                busDtl.setToken(bus.getToken());
                busDtl.setOrigId(bus.getStorageId());
                busDtl.setSku(jsonObj.getString("sku"));
                busDtl.setStyleId(jsonObj.getString("styleId"));
                busDtl.setColorId(jsonObj.getString("colorId"));
                busDtl.setSizeId(jsonObj.getString("sizeId"));
                busDtl.setQty(Long.parseLong(jsonObj.getString("skuCount")));

                dtlMap.put(busDtl.getSku(), busDtl);
            }

            // 增加CloudRecord
            JSONArray jsonArr = jsonObj.getJSONArray("epcList");
            for (Object epcStr : jsonArr) {
                CloudRecord r = new CloudRecord();
                r.setId(new GuidCreator().toString());
                r.setCartonId(boxId);
                r.setDeviceId(bus.getDeviceId());
                r.setTaskId(bus.getId());
                r.setOwnerId(bus.getOwnerId());
                r.setCode(epcStr.toString());
                r.setOrigId(bus.getStorageId());
                r.setToken(bus.getToken());
                r.setScanTime(box.getScanTime());// boxMap.get(boxId).getScanTime());
                r.setSku(dtl.getSku());

                r.setStyleId(dtl.getStyleId());
                r.setSizeId(dtl.getSizeId());
                r.setColorId(dtl.getColorId());

               // bus.getRecordList().add(r);
                bus.getCloudRecordList().add(r);
            }
        }
       // box.setBoxDtls(dtlList);
        box.setCloudBoxDtlList(dtlList);

        return box;
    }

    private Bill jsonToBill(String json) {
        return JSON.parseObject(json, Bill.class);
    }

    private  List<BillDtl> jsonToBillDtlList(String json, Bill bill) {
        // JSON.parseArray(dtlJsonStr, BillDtl.class);直接用fastjson时，在生产环境下存在解析bug
        List<BillDtl> dtlList = new ArrayList<BillDtl>();
        JSONArray jsonArray = JSON.parseArray(json);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            BillDtl dtl = new BillDtl();
            String dtlId = jsonObj.getString("id");
            dtl.setId(CommonUtil.isBlank(dtlId) ? new GuidCreator().toString()
                    : dtlId);
            dtl.setBillId(bill.getId());
            dtl.setBillNo(bill.getBillNo());
            dtl.setSku(jsonObj.getString("sku"));
            dtl.setStyleId(jsonObj.getString("styleId"));
            dtl.setColorId(jsonObj.getString("colorId"));
            dtl.setSizeId(jsonObj.getString("sizeId"));
            dtl.setQty(jsonObj.getLongValue("qty"));
            dtl.setActQty(jsonObj.getLongValue("actQty"));
            dtlList.add(dtl);
        }
        return dtlList;
    }

    // 更改数据类型
    private  void preInfo(CloudBusiness business) {
        Bill bill = business.getBill();

        if (bill == null) {
            return;
        }
        // john add
        business.setType(bill.getType());
        business.setStorage2Id(bill.getDestId());
        business.setUnit2Id(bill.getDestUnitId());
        //for (CloudBusinessDtl b : business.getDtlList()) {
        for (CloudBusinessDtl b : business.getCloudBusinessDtlList()) {
            b.setType(bill.getType());
            b.setDestId(bill.getDestId());
            b.setDestUnitId(bill.getDestUnitId());
        }
        for (CloudRecord r : business.getCloudRecordList()) {
            r.setType(bill.getType());
            r.setDestId(bill.getDestId());
            r.setDestUnitId(bill.getDestUnitId());
        }
        for (CloudBox b : business.getCloudBoxList()) {
            b.setType(bill.getType());
       
            //for (CloudBoxDtl bd : b.getCloudBoxDtls()) {
            for (CloudBoxDtl bd : b.getCloudBoxDtlList()) {
                bd.setType(bill.getType());
           
            }
        }
    }

}
