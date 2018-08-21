package com.casesoft.dmc.controller.syn.tool;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.BusinessDtl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by john on 2017/1/9.
 */
public class SynTaskUtil {


    /**
     * 补充BusinessDtl基础信息
     * @param dtlList
     * @return
     * */
    public static List<BusinessDtl> convertToVo(List<BusinessDtl> dtlList) {
        for (BusinessDtl dtl : dtlList) {
            dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
        }
        return dtlList;
    }
    /**
     * 补充Business基础信息
     * @param rows
     * @return
     * */
    public static void convertToBusVos(List<Business> rows) {
        for (Business bus : rows) {
            convertToBusVo(bus);
        }
    }
    /**
     * 补充Business基础信息
     * @param business
     * @return
     * */
    public static void convertToBusVo(Business business) {
        business.setTotTime(business.getEndTime().getTime()
                - business.getBeginTime().getTime());
        business.setTaskType(CacheManager.getSetting(business.getToken().toString()).getValue());
        business.setDeviceName(CommonUtil.isBlank(CacheManager.getDeviceByCode(business.getDeviceId())) ? "" :
                CacheManager.getDeviceByCode(business.getDeviceId()).getName());
        // Storage s = CacheManager.getStorageById(bus.getOrigId());
        // //wing 2014-0607
        Unit s = CacheManager.getStorageById(business.getOrigId()); // wing
        // 2014-0607
        business.setOrigName(s == null ? "" : s.getName());
        business.setOrigUnitName(CacheManager.getUnitById(business.getOwnerId())
                .getName());

        if (CommonUtil.isNotBlank(business.getDestUnitId())) {
            Unit unit2 = CacheManager.getUnitById(business.getDestUnitId());
            business.setDestUnitName(unit2 == null ? business.getDestUnitId() : unit2
                    .getName());
        }
        if (CommonUtil.isNotBlank(business.getDestId())) {
            if(CommonUtil.isNotBlank(CacheManager.getUnitById(
                    business.getDestId()))){
                business.setDestName(CacheManager.getUnitById(
                        business.getDestId()).getName());
            }
        }
    }
    /**
     * 获取任务号
     * @param businesses
     * @return
     * */
    public static List<String> getTaskId(List<Business> businesses){
        List<String> taskIds=new LinkedList<>();
        if(CommonUtil.isNotBlank(businesses)){
            for(Business business:businesses){
                taskIds.add(business.getId());
            }
        }
        return taskIds;
    }
    /**
     * zip文件转换上传数据
     * @param ins
     * @return
     * */
    public static Business convertZipToDto(InputStream ins) throws Exception {
        ZipFile zipFile = null;
        Business bus = null;
        Map<String, StringBuffer> zipMap = new HashMap<String, StringBuffer>();
        try {
            // wing 2014-03-04 中文乱码bug解决
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
            bus =  convertToDtl(zipMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage()+"解析ZIP压缩文件出错！！！");
        } finally {
            if (zipFile != null) {
             }
        }
        return bus;
    }


    /**
     * 支持单任务，多箱，单个单据解析
     *
     * @exception Exception
     * @param zipMap
     * @return
     *
     * */
    private   static Business convertToDtl(Map<String, StringBuffer> zipMap)
            throws Exception {
        Business bus = ZipFileConvert.pickBusinessFile(zipMap);
        if(CommonUtil.isNotBlank(bus)){
           boolean pickSuccess= ZipFileConvert.pickBoxFile(zipMap,bus);
           if(pickSuccess){
               Bill bill= ZipFileConvert.pickBillFile(zipMap,bus);
               if(CommonUtil.isNotBlank(bill)){
                   boolean pickBillDtlSuccess= ZipFileConvert.pickBillDtlFile(zipMap,bill);
                   if(!pickBillDtlSuccess){
                       throw new Exception("解析单据明细失败！");
                   }
               }
           }else{
               throw new Exception("解析箱明细失败！");
           }
        }else {
            throw new Exception("任务文件解析失败！");
        }
        return bus;
    }
}
