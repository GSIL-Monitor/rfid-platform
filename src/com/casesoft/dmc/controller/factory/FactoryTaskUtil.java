package com.casesoft.dmc.controller.factory;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.model.factory.*;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.service.factory.FactoryTaskService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FactoryTaskUtil {

	public static void covertToSaveTask(FactoryTask task, FactoryTaskService taskService) throws Exception {
		List<FactoryRecord> list = new ArrayList<FactoryRecord>();
		task.setTaskId(new GuidCreator().toString());
		for(InitEpc epc : task.getEpcList()){
			FactoryRecord r = new FactoryRecord();
			r.setId(new GuidCreator().toString());
			r.setTaskId(task.getTaskId());
			r.setToken(task.getToken());
			r.setType(task.getType());
			r.setCode(epc.getCode());
			r.setDeviceId(task.getDeviceId());
			r.setSku(epc.getSku());
			r.setStyleId(epc.getStyleId());
			r.setColorId(epc.getColorId());
			r.setSizeId(epc.getSizeId());
			r.setScanTime(task.getTaskTime());
			if((task.getType().equals(FactoryConstant.TaskType.Inbound)) || (task.getType().equals(FactoryConstant.TaskType.Back))){
				r.setTotalTime(0d);
				epc.setTotalTime(0d);
			}else if(task.getType().equals(FactoryConstant.TaskType.Restart)){
                FactoryRecord pasuseRecord = taskService.findUniqueRecord(r.getCode(),epc.getTaskId());
                FactoryBill bill = taskService.findUniqueFactoryBill(epc.getBillNo(), epc.getBillDate(), epc.getEndDate(), epc.getUploadNo());
                if(CommonUtil.isNotBlank(bill.getFactory())){
                   /* FactoryWorkTime workTime = CacheManager.getFactoryWorkTime(bill.getFactory(), task.getToken());
                    if(CommonUtil.isNotBlank(workTime)){
                        pasuseRecord.setTotalTime(FactoryUtil.getDutyDaysHoursByFactory(epc.getTaskTime(), task.getTaskTime(), workTime));
                    }else{
                        pasuseRecord.setTotalTime(FactoryUtil.getDutyDaysHours(epc.getTaskTime(), task.getTaskTime()));
                    }*/
                }else{
                    pasuseRecord.setTotalTime(FactoryUtil.getDutyDaysHours(epc.getTaskTime(), task.getTaskTime()));
                }
                list.add(pasuseRecord);
				r.setTotalTime(epc.getTotalTime());
			}else{

                FactoryBill bill = taskService.findUniqueFactoryBill(epc.getBillNo(), epc.getBillDate(), epc.getEndDate(), epc.getUploadNo());
                if(CommonUtil.isNotBlank(bill.getFactory())){
                    /*FactoryWorkTime workTime = CacheManager.getFactoryWorkTime(bill.getFactory(), task.getToken());
                    if(CommonUtil.isNotBlank(workTime)){
                        r.setTotalTime(epc.getTotalTime() + FactoryUtil.getDutyDaysHoursByFactory(epc.getTaskTime(), task.getTaskTime(), workTime));
                    }else{
                        r.setTotalTime(epc.getTotalTime()+ FactoryUtil.getDutyDaysHours(epc.getTaskTime(), task.getTaskTime()));
                    }*/
                }else{
                    r.setTotalTime(epc.getTotalTime()+ FactoryUtil.getDutyDaysHours(epc.getTaskTime(), task.getTaskTime()));
                }

			}
			r.setIsOutSource(CommonUtil.isBlank(task.getIsOutSource()) ? "N" : task.getIsOutSource());
			if(!task.getType().equals(FactoryConstant.TaskType.Inbound)){
				r.setStartTaskId(epc.getTaskId());
			}
			epc.setTaskId(task.getTaskId());
			epc.setTaskTime(task.getTaskTime());
			epc.setTotalTime(r.getTotalTime());
			list.add(r);
			
		}
		task.setRecordList(list);
	}

	public static void covertToOutSorceBill(List<FactoryOutSorceBill> billList,
			FactoryTaskService taskService) {
		for(FactoryOutSorceBill b:billList){
			
			User operator = CacheManager.getUserById(b.getOperator());
			/*b.setTokenName(CacheManager.getFactoryTokenByToken(b.getToken()).getName()+" 外包");*/
			b.setOperatorName(CommonUtil.isBlank(operator)?"":operator.getName());
			List<FactoryRecord> recordList = taskService.getOuSourceRecordAndBill(b.getTaskId());
            Map<String,String> billMap = new HashMap<String,String>();
            for(FactoryRecord r : recordList){
                if(billMap.containsKey(r.getBillNo())){
                    String codes = billMap.get(r.getBillNo()) + "," + r.getCode();
                    billMap.put(r.getBillNo(),codes);
                }else{
                    billMap.put(r.getBillNo(),r.getCode());
                }
            }
            String billNos = "";
            for(String billNo : billMap.keySet()){
                billNos += ","+billNo;
            }
            b.setBillNos(billNos.substring(1));
			b.setRecordList(recordList);
		}
		
	}


	

}
