package com.casesoft.dmc.controller.trace;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.resource.ResourceUtil;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.task.Box;
import com.casesoft.dmc.model.task.BoxDtl;
import com.casesoft.dmc.model.task.Record;

class TraceUtil {

  static void convertToBoxVo(List<Box> boxs) throws Exception {
    for (Box box : boxs) {
      box.setUnitName(CacheManager.getUnitById(box.getOwnerId()).getName());
      box.setTokenName(PropertyUtil.getValue(Integer.toString(box.getToken())));
    }

  }

  static void convertToRecordVo(List<Record> records) throws Exception {
	 
	  for (Record r : records) {
      r.setStyleName(CacheManager.getStyleNameById(r.getStyleId()));
      r.setColorName(CacheManager.getColorNameById(r.getColorId()));
      r.setSizeName(CacheManager.getSizeNameById(r.getSizeId()));

      // r.setTokenName(PropertyUtil.getValue(Integer.toString(r.getToken())));
      r.setTokenName(ResourceUtil.getValue("" + r.getToken()));
      if (!CommonUtil.isBlank(r.getOrigId()))
        r.setOrigName(CacheManager.getStorageById(r.getOrigId()).getName());
      r.setOrigUnitName(CacheManager.getUnitById(r.getOwnerId()).getName());
      if(!CommonUtil.isBlank(r.getDestUnitId())){
    	  Unit unit=CacheManager.getUnitById(r.getDestUnitId());
    	  if(unit==null){
    		  unit=CacheManager.getUnitByCode(r.getDestUnitId());
    	  }
    	  if(unit!=null){
    		  r.setDestUnitName(CacheManager.getUnitById(r.getDestUnitId()).getName());
    	  }
      }
    }

  }
  static String convertToRecordGIS(List<Record> records) throws Exception {
	  String series="[";
	  Integer i=0;
	  for (Record r : records) {
	      r.setStyleName(CacheManager.getStyleNameById(r.getStyleId()));
	      r.setColorName(CacheManager.getColorNameById(r.getColorId()));
	      r.setSizeName(CacheManager.getSizeNameById(r.getSizeId()));
	      i++;
	      r.setTokenName(ResourceUtil.getValue("" + r.getToken()));
	      if (!CommonUtil.isBlank(r.getOrigId())){
	    	  Unit u=CacheManager.getStorageById(r.getOrigId());
	    	  r.setOrigName(u.getName());
	    		  series+="{name: '"+i.toString()+"',type: 'map',mapType: 'china',hoverable: false,roam:true," +
	    				  "itemStyle:{normal:{label:{show:true}},},data : []," +
	    				  "markPoint : {symbol:'image://'+basePath+'/img/GIS/"+r.getToken()+".png',symbolSize:14," +
	    				  "itemStyle: {normal: {borderColor: '#87cefa',borderWidth: 1,label: {show: false}}," +
	    				  "emphasis: {borderColor: '#1e90ff',borderWidth: 5,label: {show: false}}}," +
		    			  "data :[{name:'"+r.getOrigName()+"', value: 9}]}," +
	    				  "geoCoord: {'"+r.getOrigName()+"':["+u.getLongitude()+","+u.getLatitude()+"]}},";
	      }
	      r.setOrigUnitName(CacheManager.getUnitById(r.getOwnerId()).getName());
	    }
	  if(series.equals("[")){
		  series+="]";
	  }else{
		  series=series.substring(0, series.length()-1)+"]";
	  }
	  return series;
  }
  static List<Unit> convertToUnits(List<Object> obs) {
    List<Unit> unitList = new ArrayList<Unit>();
    Unit u = null;
    for (Object o : obs) {
      String unitId = (String) o;
      u = CacheManager.getUnitById(unitId);
      unitList.add(u);
    }
    return unitList;
  }

  static List<BoxDtl> convertRecordToBoxDtl(List<Record> recordList, boolean withEpc) {
    Map<String, BoxDtl> dtlMap = new HashMap<String, BoxDtl>();
    for (Record r : recordList) {
      String sku = r.getSku();
      if (dtlMap.containsKey(sku)) {
        BoxDtl dtl = dtlMap.get(sku);
        if (withEpc)
          dtl.getCodeList().add(r.getCode());
        dtl.setQty(dtl.getQty() + 1);
        dtl.setSkuCount(dtl.getQty());
      } else {
        BoxDtl dtl = new BoxDtl();
        dtl.setSku(sku);
        dtl.setColorId(r.getColorId());
        dtl.setSizeId(r.getSizeId());
        dtl.setStyleId(r.getStyleId());
        dtl.setCode(dtl.getSku());
        dtl.setQty(1);
        dtl.setSkuCount(dtl.getQty());
        setSkuNames(dtl);
        if (withEpc) {
          List<String> codeList = new ArrayList<String>();
          codeList.add(r.getCode());
          dtl.setCodeList(codeList);
          dtl.setEpcList(codeList);
        }
        dtlMap.put(sku, dtl);
      }
    }

    return new ArrayList<BoxDtl>(dtlMap.values());
  }

  static List<BoxDtl> convertRecordToBoxDtl(List<Record> recordList) {
    return convertRecordToBoxDtl(recordList, true);
  }

  private static void setSkuNames(BoxDtl dtl) {
    dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
    dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
    dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
  }

public static void initRecordList(List<Record> recordList) {
	for(Record r : recordList) {
		String deviceId = r.getDeviceId();
		r.setOrigId(CacheManager.getDeviceByCode(deviceId).getStorageId());//仓库或门店
		r.setOwnerId(CacheManager.getDeviceByCode(deviceId).getOwnerId());
		String uuid = new GuidCreator().toString();
		r.setCartonId(uuid);
		r.setId(uuid);
		r.setTaskId(uuid);
	}
	
}
public static String addTag(String codeList) {
    String[] codes = codeList.split(",");
    StringBuffer strs = new StringBuffer("'");
    for (String code : codes) {
      if (!CommonUtil.isBlank(code))
        strs.append(code).append("','");
    }
    return strs.append("'").toString();
  }

public static File writeTaskExcelFile(List<Record> records)throws Exception{
    HSSFWorkbook traceExcelBook = new HSSFWorkbook();
    HSSFSheet sheet = traceExcelBook.createSheet("出库异常清单");
    HSSFRow row = null;
    HSSFCell cell = null;
    
    row = sheet.createRow(0);


    cell = row.createCell(0);
    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
    cell.setCellValue("任务日期");

    cell = row.createCell(1);
    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
    cell.setCellValue("任务编号");
    cell = row.createCell(2);
    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
    cell.setCellValue("唯一吗");
    cell = row.createCell(3);
    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
    cell.setCellValue("组织名称1");

    cell = row.createCell(4);
    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
    cell.setCellValue("组织名称2");

    cell = row.createCell(5);
    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
    cell.setCellValue("产品代码");

    cell = row.createCell(6);
    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
    cell.setCellValue("产品名称");

    cell = row.createCell(7);
    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
    cell.setCellValue("颜色代码");

    cell = row.createCell(8);
    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
    cell.setCellValue("颜色名称");

    cell = row.createCell(9);
    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
    cell.setCellValue("尺寸代码");

    cell = row.createCell(10);
    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
    cell.setCellValue("尺寸名称");

  
    
    for (int i = 1; i <= records.size(); i++) {
        row = sheet.createRow(i);
        Record dtl = records.get(i - 1);

        cell = row.createCell(0);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(dtl.getTaskId().substring(3,11));

        cell = row.createCell(1);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(dtl.getTaskId());
        
        cell = row.createCell(2);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(dtl.getCode());
        
        cell = row.createCell(3);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);         
      if(CommonUtil.isBlank(dtl.getOrigId())) {
            cell.setCellValue("");
        } else {  
        	Unit storage = CacheManager.getStorageById(dtl.getOrigId());
        	cell.setCellValue(storage==null?dtl.getOrigId():storage.getName());
        } 
        
         
        cell = row.createCell(4);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        if(CommonUtil.isBlank(dtl.getDestUnitId())) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(CacheManager.getUnitById(dtl.getDestUnitId()).getName());
        }

        cell = row.createCell(5);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(dtl.getStyleId());

        Style style = CacheManager.getStyleById(dtl.getStyleId());
        cell = row.createCell(6);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(style == null ? dtl.getStyleId() : style.getStyleName());


        cell = row.createCell(7);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(dtl.getColorId());
        cell = row.createCell(8);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(CacheManager.getColorNameById(dtl.getColorId()));

        cell = row.createCell(9);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(dtl.getSizeId());
        cell = row.createCell(10);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(CacheManager.getSizeNameById(dtl.getSizeId()));

        
       
    }
    File file = new File(PropertyUtil.getValue("MilanUploadHistory")
            + File.separatorChar + CommonUtil.getDateString(new Date(),"yyyyMMddHHmmss") +".xls");
    if (!file.exists()) {
        file.createNewFile();
    }
    FileOutputStream fos = new FileOutputStream(file);
    traceExcelBook.write(fos);
    fos.flush();
    fos.close();
    return file;
	
}

}
