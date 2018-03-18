package com.casesoft.dmc.controller.search;

import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import com.casesoft.dmc.model.search.SaleBillDtlView;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.cache.SpringContextUtil;
import com.casesoft.dmc.controller.search.temp.SaleAndFittingStatistics;
import com.casesoft.dmc.controller.search.temp.SaleRanking;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.cfg.PropertyType;
import com.casesoft.dmc.model.product.Color;
import com.casesoft.dmc.model.product.Size;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.search.DetailFittingView;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.service.cfg.PropertyKeyService;




public class DetailViewUtil {

	private static PropertyKeyService propertyKeyService = (PropertyKeyService) SpringContextUtil
	        .getBean("propertyKeyService");
	
	private static int size = 0;
	private static String[] fields;//定义下载字段	
	private static String[] methods;

	/**
	 * 明细查询导出Excel方法
	 * @param dtlList 查询结果
	 * @param sheetName sheet名称
	 * @return File
	 * */
	@SuppressWarnings("hiding")
	public static <T> File writeDetailViewFile(List<T> dtlList,String sheetName) throws Exception {
		List<PropertyType> list = propertyKeyService.findPrpertyByType();
		size = list.size();			
		HSSFWorkbook excelBook = new HSSFWorkbook();
		HSSFSheet sheet = null;
		sheet = excelBook.createSheet(sheetName);			
		HSSFCellStyle style = excelBook.createCellStyle(); 
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中  
		initFieldsbySheetName(sheetName);
		initExcelWidth(sheet);
		HSSFRow row = null;
		row = sheet.createRow(0);
		
		initExcelCell(style,row,list);	
		int i = 0;
		int count = 1;
		for(T dtl: dtlList){
			 if(i > 9999){  
				 sheet = excelBook.createSheet(sheetName+count);				 
				 initExcelWidth(sheet);
				 row = sheet.createRow(0);					
				 initExcelCell(style,row,list);	
				 count++;
				 i = 0;
			 }
			 row = sheet.createRow(i+1);
		     initExcelData(style,row,dtl);
			 i++;
		}	
		File file = new File(CommonUtil.getDateString(new Date(), "yyyyMMddhhmmss")+".xls");
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		excelBook.write(fos);
		fos.flush();
		fos.close();
		return file;
		
	}
	
	private static void initFieldsbySheetName(String sheetName) {
		switch(sheetName){
		    case "入库明细":
		    case "出库明细":
		    case "盘点明细":
		    	fields = new String[] {"日期","仓库(店铺)编号","商品编号","数量",sheetName+"类型","是否为单据","单据数量","单据实际数量"};
		    	methods = new String[] {"getBillDate","getWarehId","getSku","getQty","getToken","getIsBill","getBillQty","getActQty"};
		        break;
		    case "试衣明细":
		    case "销售明细":
		    	fields = new String[] {"日期","仓库(店铺)编号","商品编号"};
		    	methods = new String[] {"getBillDate","getWarehId","getSku"};
		    	break;
		    
		    	
		}
	}

	/**
	 * 初始化Excel数据		 
	 * @param dtl 数据
	 * @author Alvin
	 * */	
	@SuppressWarnings({ "hiding", "rawtypes", "unchecked" })
	private static <T> void initExcelData(HSSFCellStyle style,HSSFRow row,
			T dtl) throws Exception {

	    HSSFCell cell = null;
	   
	    
		Class cls = dtl.getClass();			
		for(int i = 0,length = methods.length; i < length; i++){
			cell = row.createCell(i);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			if(methods[i].contains("BillDate")){
				cell.setCellValue(CommonUtil.getDateString((Date)cls.getMethod(methods[i]).invoke(dtl),
                        "yyyy-MM-dd HH:mm:ss"));
				cell.setCellStyle(style);
			}else if(methods[i].contains("Token")){
				String value = findTokenValue((int)cls.getMethod(methods[i]).invoke(dtl));
				cell.setCellValue(value);
				cell.setCellStyle(style);
			}else{	
				
				Object value = cls.getMethod(methods[i]).invoke(dtl);
				cell.setCellValue(CommonUtil.isBlank(value)?"":""+value);
				cell.setCellStyle(style);  
			}
			 
		}	
		
		initProductViewData(methods.length-1,style,row,dtl);
		
		   
	}
   
	private static String findTokenValue(int token) {
		String value = "";
		switch(token){
		    case Constant.Token.Storage_Inbound :
		    	value = "供应商采购入库";
		        break;		    
		    case Constant.Token.Storage_Inventory:
		    	value = "供应商仓库盘点";
		    	break;		   
		    case Constant.Token.Storage_Refund_Inbound:
		    	value = "供应商退货入库";
		    	break;
		    case Constant.Token.Storage_Outbound:
		    	value = "供应商普通出库";
		    	break;
		    case Constant.Token.Storage_Transfer_Outbound:
		    	value = "供应商调拨出库";
		    	break;
		    case Constant.Token.Storage_Transfer_Inbound:
		    	value = "供应商调拨入库";
		    	break;
		    case Constant.Token.Storage_Refund_Outbound:
		    	value = "供应商退货出库";
		    	break;
		    case Constant.Token.Storage_Adjust_Outbound:
		    	value = "供应商调整出库";
		    	break;
		    case Constant.Token.Storage_Adjust_Inbound:
		    	value = "供应商调整入库";
		    	break;
		    case Constant.Token.Storage_Other_Outbound:
		    	value = "供应商其他出库";
		    	break;
		    case Constant.Token.Shop_Inbound:
		    	value = "门店入库";
		    	break;
		    case Constant.Token.Shop_Sales:
		    	value = "门店销售";
		    	break;
		    case Constant.Token.Shop_Inventory:
		    	value = "门店盘点";
		    	break;
		    case Constant.Token.Shop_Transfer_Outbound:
		    	value = "门店调拨出库";
		    	break;
		    case Constant.Token.Shop_Transfer_Inbound:
		    	value = "门店调拨入库";
		    	break;
		    case Constant.Token.Shop_Sales_refund:
		    	value = "门店销售退货";
		    	break;
		    case Constant.Token.Shop_Refund_Outbound:
		    	value = "门店退货出库";
		    	break;
		    case Constant.Token.Shop_Adjust_Outbound:
		    	value = "门店调整出库";
		    	break;
		    case Constant.Token.Shop_Adjust_Inbound:
		    	value = "门店调整入库";
		    	break;
		    case Constant.Token.Shop_Other_Outbound:
		    	value = "门店其他出库";
		    	break;
		    case Constant.Token.Shop_TransferSale_Outbound:
		    	value = "门店调拨销售";
		    	break;
		}
		return value;
	}

	/**
	 * 初始化表格商品属性数据
	 * @param index 起始位置
	 * @param style 单元格格式
	 * @param row 单元格行
	 * @param dtl 数据
	 * @author Alvin
	 * */
	@SuppressWarnings({ "hiding", "unchecked", "rawtypes" })
	private static <T> void initProductViewData(int index,HSSFCellStyle style
			                                   ,HSSFRow row,T dtl) throws Exception{
		
		Class cls = dtl.getClass();
		HSSFCell cell = null;
		cell = row.createCell(index+1);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(""+cls.getMethod("getStyleId").invoke(dtl));
		cell.setCellStyle(style);    
		
		cell = row.createCell(index+2);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(""+cls.getMethod("getStyleName").invoke(dtl));
		cell.setCellStyle(style);    
		
		cell = row.createCell(index+3);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(""+cls.getMethod("getColorId").invoke(dtl));
		cell.setCellStyle(style);    
		
		cell = row.createCell(index+4);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(""+cls.getMethod("getSizeId").invoke(dtl));
		cell.setCellStyle(style);    
		
		cell = row.createCell(index+5);
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue((Double)cls.getMethod("getPrice").invoke(dtl));
		cell.setCellStyle(style);    
		
		   
		
		cell = row.createCell(index+6);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(""+cls.getMethod("getBrandCode").invoke(dtl));
		cell.setCellStyle(style);    
		
		for(int i = 0; i < size;i++){
			cell = row.createCell(index+7+i);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			String value = (String) cls.getMethod("getClass"+(i+1)).invoke(dtl);
			cell.setCellValue(CommonUtil.isBlank(value)?"":value);
			cell.setCellStyle(style);    
		}
		
		cell = row.createCell(index+7+size);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		String stlyle_E_Name = (String) cls.getMethod("getStyleEName").invoke(dtl);
		cell.setCellValue(CommonUtil.isBlank(stlyle_E_Name)?"":stlyle_E_Name);
		cell.setCellStyle(style);    
		
		cell = row.createCell(index+8+size);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		String productRemark = (String) cls.getMethod("getProdRemark").invoke(dtl);
		cell.setCellValue(CommonUtil.isBlank(productRemark)?"":productRemark);
		cell.setCellStyle(style);
	}
    
	
	/**
	 * 初始化Excel表头		 
	 * @author Alvin
	 * */	
	private static void initExcelCell(HSSFCellStyle style,HSSFRow row,List<PropertyType>list) {
		
	    HSSFCell cell = null;	   
	    
	    for(int i = 0,length = fields.length; i < length; i++){
	    	cell = row.createCell(i);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(fields[i]);
			cell.setCellStyle(style); 
	    }
		initProductViewCell(fields.length-1,style,row,list);		
		
	}
	
	/**
	 * 初始化商品属性表头
	 * @param indexCell 起始位置
	 * @param style 单元格格式
	 * @param row 单元格行
	 * @author Alvin
	 * */
	private static void initProductViewCell(int indexCell,HSSFCellStyle style,HSSFRow row,List<PropertyType> list){
		
		HSSFCell cell = null;
		
		cell = row.createCell(indexCell+1);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("款号");
		cell.setCellStyle(style); 
		
		cell = row.createCell(indexCell+2);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("款名");
		cell.setCellStyle(style); 
		
		cell = row.createCell(indexCell+3);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("色号");
		cell.setCellStyle(style); 
		
		cell = row.createCell(indexCell+4);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("尺码");
		cell.setCellStyle(style); 
		
		cell = row.createCell(indexCell+5);
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue("吊牌价");
		cell.setCellStyle(style); 
		
		
		
		cell = row.createCell(indexCell+6);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("品牌");
		cell.setCellStyle(style); 
		
		for(int i = 0; i < size;i++){
			cell = row.createCell(indexCell+7+i);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(""+list.get(i).getValue());
			cell.setCellStyle(style); 
		}
		cell = row.createCell(indexCell+7+size);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("款名(英文)");
		cell.setCellStyle(style); 
		
		cell = row.createCell(indexCell+8+size);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("商品备注");
		cell.setCellStyle(style);
	}
	private static void initExcelWidth(HSSFSheet sheet) {
		
		int fieldsLength = 16 + fields.length;
		for(int i = 0 ; i < fieldsLength; i++){
			sheet.setColumnWidth(i, 20 * 256);
		}
		sheet.setColumnWidth(fieldsLength, 40 * 256);//商品属性英文名
		sheet.setColumnWidth(fieldsLength+1, 100 * 256);//商品备注
		
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<DetailFittingView> covertToDtlFitting(List<DetailFittingView> list) throws Exception{
		for(DetailFittingView d :list){
			
			Style s = CacheManager.getStyleById(d.getStyleId());
			if(CommonUtil.isNotBlank(s)){
				d.setStyleName(s.getStyleName());
				d.setPrice(s.getPrice());
				String brandCode = s.getBrandCode();
				if(CommonUtil.isNotBlank(brandCode)){
					d.setBrandCode(brandCode);
				}
				Class styleCls = s.getClass();
				Class dtlFittingCls = d.getClass();
				for(int i = 0; i < 10; i++){
					String value = (String) styleCls.getMethod("getClass"+(i+1)).invoke(s);					
				    dtlFittingCls.getMethod("setClass"+(i+1),String.class)
				                 .invoke(d, CommonUtil.isBlank(value)?"":value);
				}
				
			}
		}
		return list;
	}
	
	public static void initRankingBasicInfo(List<SaleRanking> saleRankings){
		
		if(CommonUtil.isNotBlank(saleRankings)){
			 NumberFormat numberFormat = NumberFormat.getInstance();  
			  
		        // 设置精确到小数点后2位  
		        numberFormat.setMaximumFractionDigits(2);  
			for(SaleRanking s:saleRankings){
				Unit unit=CacheManager.getUnitByCode(s.getWarehId());
				if(CommonUtil.isNotBlank(unit)){
					s.setWarehName(unit.getName());
				}
				Style style=CacheManager.getStyleById(s.getStyleId());
			   if(CommonUtil.isNotBlank(style)){
					s.setStyleName(style.getStyleName());
				}
			  if(s.getTotAllPrice()!=0d){
				  s.setProportion(numberFormat.format(s.getTotPrice()/s.getTotAllPrice()*100)+"%");				  
			  }
			}
		}
	}
 public static void initStatisticsBasicInfo(List<SaleAndFittingStatistics> saleRankings){
		
		if(CommonUtil.isNotBlank(saleRankings)){
			 
			for(SaleAndFittingStatistics s:saleRankings){
				Unit unit=CacheManager.getUnitByCode(s.getWarehId());
				if(CommonUtil.isNotBlank(unit)){
					s.setWarehName(unit.getName());
				}
				Style style=CacheManager.getStyleById(s.getStyleId());
			   if(CommonUtil.isNotBlank(style)){
					s.setStyleName(style.getStyleName());
				}
			    Size size=CacheManager.getSizeById(s.getSizeId());
			    Color color=CacheManager.getColorById(s.getColorId());
			    if(size!=null){
			    	s.setSizeName(size.getSizeName());
			    }
			    if(color!=null){
			    	s.setColorName(color.getColorName());
			    }
			}
		}
	} 
	private static void initSaleBillDtlExcelWidth(HSSFSheet sheet) {
		
		for(int i = 0; i < 18; i++){
			sheet.setColumnWidth(i, 25 * 256);
		}
	}

	public static File writeSaleBillDtlView(List<SaleBillDtlView> dtlList,
			String sheetName) throws Exception {
		HSSFWorkbook excelBook = new HSSFWorkbook();
		HSSFSheet sheet = null;
		sheet = excelBook.createSheet(sheetName);			
		HSSFCellStyle style = excelBook.createCellStyle(); 
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中 	
		initSaleBillDtlExcelWidth(sheet);
		HSSFRow row = null;
		row = sheet.createRow(0);
		
		initSaleBillDtlExcelCell(style,row,sheetName);	
		int i = 0;
		int count = 1;
		for(SaleBillDtlView dtl: dtlList){
			 if(i > 9999){  
				 sheet = excelBook.createSheet(sheetName+count);				 
				 initSaleBillDtlExcelWidth(sheet);
				 row = sheet.createRow(0);					
				 initSaleBillDtlExcelCell(style,row,sheetName);	
				 count++;
				 i = 0;
			 }
			 row = sheet.createRow(i+1);
		     initSaleBillDtlExcelData(style,row,dtl,sheetName);
			 i++;
		}	
		File file = new File(CommonUtil.getDateString(new Date(), "yyyyMMddhhmmss")+".xls");
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		excelBook.write(fos);
		fos.flush();
		fos.close();
		return file;
	}

	private static void initSaleBillDtlExcelCell(HSSFCellStyle style,
			HSSFRow row,String sheetName) {
		HSSFCell cell = null;	
		cell = row.createCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("日期");
		cell.setCellStyle(style); 
		
		cell = row.createCell(1);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("编号");
		cell.setCellStyle(style); 
		
	
		cell = row.createCell(2);
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue("数量");
		cell.setCellStyle(style); 
		
		initProductViewCell(2,style,row);
		
		cell = row.createCell(15);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("实价");
		cell.setCellStyle(style);
		
		cell = row.createCell(16);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("折率");
		cell.setCellStyle(style); 
		
		if(sheetName.equals("退货明细")){
			cell = row.createCell(17);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue("退款金额");
			cell.setCellStyle(style); 
		}
	}


	
	private static void initSaleBillDtlExcelData(HSSFCellStyle style,
			HSSFRow row, SaleBillDtlView dtl ,String sheetName) throws Exception {
		HSSFCell cell = null;	
		cell = row.createCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	//	cell.setCellValue(CommonUtil.getDateString(dtl.getDate(), "yyyy-MM-dd HH:mm:ss"));
		cell.setCellStyle(style); 
			
		cell = row.createCell(1);
	//	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		//cell.setCellValue(dtl.getWarehId());
		cell.setCellStyle(style); 		
	
		cell = row.createCell(2);
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(dtl.getQty());
		cell.setCellStyle(style); 
		
		initProductViewData(2,style,row,dtl);
		
		cell = row.createCell(15);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(""+dtl.getActPrice());
		cell.setCellStyle(style); 
		
		cell = row.createCell(16);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(""+dtl.getPercent());
		cell.setCellStyle(style); 
		
		if(sheetName.equals("退货明细")){
			cell = row.createCell(17);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			//cell.setCellValue(dtl.getRefPrice());
			cell.setCellStyle(style); 
		}
		
	}

	/**
	 * 初始化商品属性表头
	 * @param indexCell 起始位置
	 * @param style 单元格格式
	 * @param row 单元格行
	 * @author Alvin
	 * */
	private static void initProductViewCell(int indexCell,HSSFCellStyle style,HSSFRow row){
		
		HSSFCell cell = null;
		
		cell = row.createCell(indexCell+1);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("品牌");
		cell.setCellStyle(style); 
		
		cell = row.createCell(indexCell+2);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("款色码");
		cell.setCellStyle(style); 
		
		cell = row.createCell(indexCell+3);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("款名");
		cell.setCellStyle(style); 
		
		cell = row.createCell(indexCell+4);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("中文名");
		cell.setCellStyle(style); 
		
		cell = row.createCell(indexCell+5);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("英文名");
		cell.setCellStyle(style); 
		
		
		cell = row.createCell(indexCell+6);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("季节");
		cell.setCellStyle(style); 
		
		cell = row.createCell(indexCell+7);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("性别");
		cell.setCellStyle(style); 
		
		cell = row.createCell(indexCell+8);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("PH1");
		cell.setCellStyle(style); 
		
		cell = row.createCell(indexCell+9);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("PH2");
		cell.setCellStyle(style); 
		
		cell = row.createCell(indexCell+10);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("PH3");
		cell.setCellStyle(style); 
		
		cell = row.createCell(indexCell+11);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("EAN");
		cell.setCellStyle(style); 
		
		cell = row.createCell(indexCell+12);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("零售价");
		cell.setCellStyle(style); 
		
	}


}
