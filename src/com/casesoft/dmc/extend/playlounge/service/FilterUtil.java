package com.casesoft.dmc.extend.playlounge.service;

import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.Record;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FilterUtil {

	public static void doFilterNull(List<BillDtl> dtls){
	/*	if(CommonUtil.isNotBlank(dtls)){
			List<BillDtl> newDtls=new ArrayList<>();
			for(BillDtl dtl:dtls){
				if(dtl.getQty().longValue()-dtl.getInitQty()!=0l){
					newDtls.add(dtl);
				}
			}
			dtls.clear();
			dtls.addAll(newDtls);
		}*/
	}
	public static  void buildBillRecord(Business bus) throws Exception{
		Bill bill=bus.getBill();
		List<Record> addedRecords=new ArrayList<>();
		if(CommonUtil.isNotBlank(bill)){
			List<Record> recordList=bus.getRecordList();
			if(CommonUtil.isNotBlank(recordList)){
				for(Record r:recordList){
					Record rs=copy(r);
					rs.setId(bill.getBillNo()+r.getCode());
					rs.setTaskId(bill.getBillNo());
					addedRecords.add(rs);
				}
			}
			bill.setRecords(addedRecords);
 		}
   	}
	  /** 
     * 深层拷贝 
     *  
     * @param <T> 
     * @param obj 
     * @return 
     * @throws Exception 
     */  
    public static <T> T copy(T obj) throws Exception {  
        //是否实现了序列化接口，即使该类实现了，他拥有的对象未必也有...  
        if(Serializable.class.isAssignableFrom(obj.getClass())){  
            //如果子类没有继承该接口，这一步会报错  
            try {  
                return copyImplSerializable(obj);  
            } catch (Exception e) {  
                //这里不处理，会运行到下面的尝试json  
            }  
        }  
       
        return null;  
    }  
  
    /** 
     * 深层拷贝 - 需要类继承序列化接口 
     * @param <T> 
     * @param obj 
     * @return 
     * @throws Exception 
     */  
    @SuppressWarnings("unchecked")  
    public static <T> T copyImplSerializable(T obj) throws Exception {  
        ByteArrayOutputStream baos = null;  
        ObjectOutputStream oos = null;  
          
        ByteArrayInputStream bais = null;  
        ObjectInputStream ois = null;  
          
        Object o = null;  
        //如果子类没有继承该接口，这一步会报错  
        try {  
            baos = new ByteArrayOutputStream();  
            oos = new ObjectOutputStream(baos);  
            oos.writeObject(obj);  
            bais = new ByteArrayInputStream(baos.toByteArray());  
            ois = new ObjectInputStream(bais);  
  
            o = ois.readObject();  
            return (T) o;  
        } catch (Exception e) {  
            throw new Exception("对象中包含没有继承序列化的对象");  
        } finally{  
            try {  
                baos.close();  
                oos.close();  
                bais.close();  
                ois.close();  
            } catch (Exception e2) {  
                //这里报错不需要处理  
            }  
        }  
    }

    public static void pickManualBill(Business business){
        Bill bill=business.getBill();
        bill.setActQty(bill.getPreManualQty());
        List<BillDtl> dtls=new ArrayList<>();
        for(BillDtl dtl:bill.getDtlList()){
            if(CommonUtil.isNotBlank(dtl.getPreManualQty())&&dtl.getPreManualQty()!=0l){
                dtl.setActQty(dtl.getPreManualQty());
                dtls.add(dtl);
            }
        }
        bill.setDtlList(dtls);
    }
}
