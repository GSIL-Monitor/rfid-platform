package com.casesoft.dmc.controller.logistics;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.logistics.MonthAccountStatement;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Alvin on 2018/1/17.
 */
public class MonthAccountUtil {

    /**
     * @param billDate 单据日期
     * @param monthAccountMap  客户月结表信息
     * @param updateMonthAccountList 需要更新内容
     * @param unitId 客户id
     * @param diffPrice 单据差价
     * @param isAdd 加减月结表欠款金额操作 ture表示加欠款金额 false 表示减去欠款金额
     * */
    public static void updateMonthAcoutData(Date billDate, Map<String, MonthAccountStatement> monthAccountMap, List<MonthAccountStatement> updateMonthAccountList, String unitId, Double diffPrice,boolean isAdd) {
        String billDateStr = CommonUtil.getDateString(billDate,"yyyy-MM");
        String ownerId = "";
        if(CommonUtil.isNotBlank(CacheManager.getCustomerById(unitId))){
            ownerId = CacheManager.getCustomerById(unitId).getOwnerId();
        }else if(CommonUtil.isNotBlank(CacheManager.getUnitByCode(unitId))){
            ownerId = CacheManager.getUnitByCode(unitId).getOwnerId();
        }
        while(billDateStr.compareTo(CommonUtil.getDateString(new Date(),"yyyy-MM")) == -1){
            String preMonthAccountId = unitId+"-"+ billDateStr;
            String nextMonth = CommonUtil.getNextMonth(billDateStr.replaceAll("-",""),"yyyy-MM");
            if(CommonUtil.isNotBlank(monthAccountMap.get(preMonthAccountId))){
                MonthAccountStatement m = monthAccountMap.get(preMonthAccountId);
                if(isAdd){
                    m.setTotVal(Double.parseDouble(Math.round(m.getTotVal() + diffPrice)+""));
                }else {
                    m.setTotVal(Double.parseDouble(Math.round(m.getTotVal() - diffPrice)+""));
                }
                updateMonthAccountList.add(m);
            }else{
                MonthAccountStatement m = new MonthAccountStatement();
                m.setId(unitId+'-'+billDateStr);
                if(isAdd){
                    m.setTotVal(Double.parseDouble(Math.round(diffPrice)+""));
                }else{
                    m.setTotVal(Double.parseDouble(Math.round(0-diffPrice)+""));
                }
                m.setUnitId(unitId);
                m.setMonth(billDateStr);
                m.setUnitType("客户");
                m.setBillType("收款");
                m.setOwnerId(ownerId);
                try{
                    Date newDate = CommonUtil.converStrToDate(nextMonth+"-01","yyyy-MM-dd");
                    m.setBillDate(newDate);
                }catch (Exception e){
                    e.printStackTrace();
                }
                updateMonthAccountList.add(m);

            }
            billDateStr = nextMonth;
        }
    }
}
