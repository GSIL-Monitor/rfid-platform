package com.casesoft.dmc.controller.shop;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.SinglePage;
import com.casesoft.dmc.core.vo.ChartVo;
import com.casesoft.dmc.core.vo.MapChartVo;
import com.casesoft.dmc.core.vo.MapData;
import com.casesoft.dmc.core.vo.MessageBox;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.shop.FittingRecord;
import com.casesoft.dmc.model.shop.Score;
import com.casesoft.dmc.model.shop.ShowRecord;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.task.BusinessDtl;
import org.apache.poi.hssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.*;

public class ShopUtil {

  public static List<FittingRecord> convertToVo(List<FittingRecord> products) {
    for (FittingRecord p : products) {
      Style s = CacheManager.getStyleById(p.getStyleId());
      if (!CommonUtil.isBlank(s)) {
        p.setStyleName(CacheManager.getStyleNameById(s.getStyleId()));
        p.setPrice(s.getPrice());
      }
      p.setColorName(CacheManager.getColorNameById(p.getColorId()));
      p.setSizeName(CacheManager.getSizeNameById(p.getSizeId()));

      Unit u = CacheManager.getUnitById(p.getOwnerId());
      if (!CommonUtil.isBlank(u))
        p.setUnitName(u.getName());
    }
    return products;
  }

  public static List<Score> convertToPageVo(List<Score> rows) {
    for (Score p : rows) {
      convertToVo(p);
    }
    return rows;

  }

  static Score convertToVo(Score p) {
    Style s = CacheManager.getStyleById(p.getStyleNo());
    if (!CommonUtil.isBlank(s)) {
      p.setStyleName(s.getStyleId());
      p.setPrice(s.getPrice());
    }
    p.setColorName(CacheManager.getColorNameById(p.getColorNo()));
    p.setSizeName(CacheManager.getSizeNameById(p.getSizeNo()));

    Unit u = CacheManager.getUnitById(p.getOwnerId());
    if (!CommonUtil.isBlank(u))
      p.setUnitName(u.getName());
    return p;
  }

  public static String convertToChartResult(List<Score> l, List<BusinessDtl> saleObjs) {
    StringBuffer categories = new StringBuffer();
    StringBuffer data = new StringBuffer();
    StringBuffer data2 = new StringBuffer();
    String name = "试衣量";
    String name2 = "销售量";
    for (Score o : l) {
      categories.append(",\"").append(o.getCode()).append("\"");
      data.append("," + o.getCount());
      boolean have = false;
      for (BusinessDtl dtl : saleObjs) {
        if (dtl.getSku().equals(o.getCode())) {
          have = true;
          data2.append(",").append(dtl.getQty());
          break;
        }
      }
      if (!have) {
        data2.append(",").append(0);
      }
    }
    String result = "{\"categories\":[" + categories.substring(1) + "]," + "\"series\":[{"
        + "\"name\":\"" + name + "\",\"data\":[" + data.substring(1) + "]},{" + "\"name\":\""
        + name2 + "\",\"data\":[" + data2.substring(1) + "]" + "}] }";
    // String result = "{\"categories\":[" + categories.substring(1) + "]," + "\"series\":[{"
    // + "\"name\":\"" + name + "\",\"data\":[" + data.substring(1) + "]} ] }";
    return result;
  }

  public static String convertToChartResult2(List<Score> l) {
    StringBuffer categories = new StringBuffer();
    StringBuffer data = new StringBuffer();
    String name = "试衣量";
    for (Score o : l) {
      categories.append(",\"").append(o.getCode()).append("\"");
      data.append("," + o.getCount());
    
    }
    String result = "{\"categories\":[" + categories.substring(1) + "]," + "\"series\":[{"
     + "\"name\":\"" + name + "\",\"data\":[" + data.substring(1) + "]} ] }";
    return result;
  }

  public static String convertToStyleChartResult(List<Object> l) {
    StringBuffer categories = new StringBuffer();
    StringBuffer data = new StringBuffer();
    String name = "试衣量";
    for (Object o : l) {
    	Object []os=(Object[])o;
      categories.append(",\"").append(os[0]).append("\"");
      data.append("," + os[2]);
    
    }
    String result = "{\"categories\":[" + categories.substring(1) + "]," + "\"series\":[{"
     + "\"name\":\"" + name + "\",\"data\":[" + data.substring(1) + "]} ] }";
    return result;
  }
  public static String convertToChartResult(List<Score> l) {
    StringBuffer categories = new StringBuffer();
    StringBuffer data = new StringBuffer();
    StringBuffer data2 = new StringBuffer();
    StringBuffer data3 = new StringBuffer();
    String name = "款式评分";
    String name2 = "质量评分";
    String name3 = "价格评分";
    for (Score o : l) {
      categories.append(",\"").append(o.getCode()).append("\"");
      data.append(",").append(o.getStyleScore());
      data2.append(",").append(o.getQualityScore());
      data3.append(",").append(o.getPriceScore());

    }
    String result = "{\"categories\":[" + categories.substring(1) + "]," + "\"series\":[{"
        + "\"name\":\"" + name + "\",\"data\":[" + data.substring(1) + "]},{" + "\"name\":\""
        + name2 + "\",\"data\":[" + data2.substring(1) + "]" + "}," + "{\"name\":\"" + name3
        + "\",\"data\":[" + data3.substring(1) + "]}" + "] }";

    return result;
  }

  public static List<Score> convertToScores(List<Object> objs) {
    List<Score> scoreList = new ArrayList<Score>();
    for (Object o : objs) {
      Object[] os = (Object[]) o;
      Score c = new Score();
      c.setCode(os[0].toString());
      c.setStyleNo(os[1].toString());
      c.setColorNo(os[2].toString());
      c.setSizeNo(os[3].toString());

      c.setCount(Integer.valueOf(os[4].toString()));

      c.setStyleScore(Integer.valueOf(os[5].toString()));
      c.setQualityScore(Integer.valueOf(os[6].toString()));
      c.setPriceScore(Integer.valueOf(os[7].toString()));

      convertToVo(c);

      scoreList.add(c);
    }
    return scoreList;
  }

  public static Score convertToScoreVo(Score s, List<FittingRecord> records) throws Exception {
    if (CommonUtil.isBlank(s.getUniqueCodes())){
      FittingRecord r = new FittingRecord();
      r.setId(new GuidCreator().toString());
      r.setScanTime(s.getScoreTime());
      r.setColorId(s.getColorNo());
      r.setStyleId(s.getStyleNo());
      r.setSizeId(s.getSizeNo());
      r.setSku(r.getStyleId() + r.getColorId() + r.getSizeId());
      r.setCode(r.getSku());
      r.setOwnerId(s.getOwnerId());
      r.setDeviceId(s.getDeviceId());
      r.setTaskId(s.getId());
      r.setParentId(s.getParentId());//20140607
      records.add(r);
      return s;
    }
    s.setUniqueCodes(s.getUniqueCodes().replace("\\/", "-").replace('/', '-'));
    List<FittingRecord> frList = JSON.parseArray(s.getUniqueCodes(), FittingRecord.class);

    if (s.getCount() != frList.size())
      throw new Exception("SKU 为 " + s.getCode() + " 的count与唯一码数量不一致");
    for (FittingRecord r : frList) {
      r.setId(new GuidCreator().toString());

      r.setColorId(s.getColorNo());
      r.setStyleId(s.getStyleNo());
      r.setSizeId(s.getSizeNo());
      r.setSku(r.getStyleId() + r.getColorId() + r.getSizeId());
      r.setOwnerId(s.getOwnerId());
      r.setDeviceId(s.getDeviceId());
      r.setTaskId(s.getId());
        r.setParentId(s.getParentId());//20140607

        records.add(r);
    }

    return s;
  }
  public static Score convertToScoreVo2(Score s, List<ShowRecord> records) throws Exception {
	    if (CommonUtil.isBlank(s.getUniqueCodes()))
	      return s;
	    List<ShowRecord> frList = JSON.parseArray(s.getUniqueCodes(), ShowRecord.class);	   
	    if (s.getCount() == frList.size()) {
	    	
	   
	        for (ShowRecord r : frList) {
	          r.setId(new GuidCreator().toString());
	          r.setTaskId(s.getId());

	          r.setColorId(s.getColorNo());
	          r.setStyleId(s.getStyleNo());
	          r.setSizeId(s.getSizeNo());
	          r.setSku(r.getStyleId() + r.getColorId() + r.getSizeId());
	          r.setOwnerId(s.getOwnerId());
	          r.setDeviceId(s.getDeviceId());

	          r.setParentId(s.getParentId());//20140607

	          records.add(r);
	       }
	    }
	    return s;
	  }

  public static void convertToRecordPageVo(List<FittingRecord> rows) {
    for (FittingRecord r : rows) {
      convertToRecordPageVo(r);
    }

  }

  public static void convertToRecordPageVo(FittingRecord p) {
    Style s = CacheManager.getStyleById(p.getStyleId());
    if (!CommonUtil.isBlank(s)) {
      p.setStyleName(s.getStyleName());
      p.setPrice(s.getPrice());
    }
    p.setColorName(CacheManager.getColorNameById(p.getColorId()));
    p.setSizeName(CacheManager.getSizeNameById(p.getSizeId()));
    Unit unit = CacheManager.getUnitById(p.getOwnerId());
    if(CommonUtil.isNotBlank(unit)) {
      p.setUnitName(unit.getName());
    }
  }

  public static MessageBox filterReqParam(String shopCode, String startDate, String endDate,
      String sku, List<PropertyFilter> filters) throws Exception {
    String shopId = "";
    if (CommonUtil.isBlank(shopCode)) {
      return new MessageBox(false, "门店编码不能为空");
    } else {
      Unit u = CacheManager.getUnitByCode(shopCode);
      if (u == null)
        return new MessageBox(false, "门店编码为非法编号");
      else
        shopId = u.getId();
    }
    if (CommonUtil.isBlank(startDate)) {
      return new MessageBox(false, "起始日期不能为空");
    }
    if (!CommonUtil.isBlank(endDate) && CommonUtil.dateInterval(startDate, endDate) > 30) {
      return new MessageBox(false, "查询天数间隔不能超过30天");
    }
    if (!CommonUtil.isBlank(sku))
      filters.add(new PropertyFilter("LIKES_code", sku));
    filters.add(new PropertyFilter("GED_scanTime", startDate));
    filters.add(new PropertyFilter("EQS_ownerId", shopId));
    String ed = endDate;
    if (CommonUtil.isBlank(ed)) {
      ed = CommonUtil.addDay(startDate, 1);
    }
    filters.add(new PropertyFilter("LED_scanTime", ed));
    return new MessageBox(true, "", filters);
  }

  public static MessageBox filterSkuReqParam(String shopCode, String startDate, String endDate,
      String sku, List<PropertyFilter> filters) throws Exception {
    String shopId = "";
    if (CommonUtil.isBlank(shopCode)) {
      return new MessageBox(false, "门店编码不能为空");
    } else {
      Unit u = CacheManager.getUnitByCode(shopCode);
      if (u == null)
        return new MessageBox(false, "门店编码为非法编号");
      else
        shopId = u.getId();
    }
    if (CommonUtil.isBlank(startDate)) {
      return new MessageBox(false, "起始日期不能为空");
    }
    if (!CommonUtil.isBlank(endDate) && CommonUtil.dateInterval(startDate, endDate) > 30) {
      return new MessageBox(false, "查询天数间隔不能超过30天");
    }
    if (!CommonUtil.isBlank(sku))
      filters.add(new PropertyFilter("LIKES_code", sku));
    filters.add(new PropertyFilter("GED_scoreTime", startDate));
    filters.add(new PropertyFilter("EQS_ownerId", shopId));
    String ed = endDate;
    if (CommonUtil.isBlank(ed)) {
      ed = CommonUtil.addDay(startDate, 1);
    }
    filters.add(new PropertyFilter("LED_scoreTime", ed));
    return new MessageBox(true, "", filters);
  }

  public static File writeFile(List<FittingRecord> recordList) throws Exception {
    String sTime = CommonUtil.getDateString(new Date(), "yyyyMMddHHmmss");
    String path = Constant.Folder.Epc_Init_File_Folder;// 创建目录
    String fileName = path + sTime + ".xls";

    HSSFWorkbook excelBook = resultSetToExcel(recordList);
    File file = new File(fileName);
    if (!file.exists())
      file.createNewFile();
    FileOutputStream fos = new FileOutputStream(file);
    excelBook.write(fos);
    fos.flush();
    fos.close();
    return file;
  }

  public static HSSFWorkbook resultSetToExcel(List<FittingRecord> recordList) throws Exception {
    HSSFWorkbook epcBook = new HSSFWorkbook();
    HSSFSheet sheet = epcBook.createSheet("试衣记录");
    initSheet(sheet);// 初始化sheet，设置列数和每列宽度

    HSSFCellStyle centerStyle = createCellStyle(epcBook);

    HSSFRow row = null;
    // HSSFCell cell = null;

    initHeader(sheet, centerStyle);

    FittingRecord r = null;
    for (int i = 1; i <= recordList.size(); i++) {
      r = recordList.get(i - 1);
      row = sheet.createRow(i);
      row.setHeight((short) (33 * 15));

      createCell(row, 0, i, centerStyle);
      createCell(row, 1, CacheManager.getUnitById(r.getOwnerId()).getName(), centerStyle);
      Style style = CacheManager.getStyleById(r.getStyleId());
      createCell(row, 2, style.getRemark(), centerStyle);
      createCell(row, 3, style.getStyleId(), centerStyle);
      createCell(row, 4, style.getStyleName(), centerStyle);
      createCell(row, 5, r.getColorId(), centerStyle);
      createCell(row, 6, CacheManager.getSizeNameById(r.getSizeId()), centerStyle);
      DecimalFormat df = new DecimalFormat("#####0");// 设置价格显示无小数位
      createCell(row, 7, df.format(style.getPrice()), centerStyle);
      createCell(row,8,r.getCode(),centerStyle);//createCell(row, 8, 1, centerStyle);
      createCell(row, 9, CommonUtil.getDateString(r.getScanTime(), "yyyy-MM-dd HH:mm:ss"),
          centerStyle);
    }

    return epcBook;
  }

  // 初始化sheet，设置列数和每列宽度
  private static void initSheet(HSSFSheet sheet) {
    sheet.setColumnWidth(0, (short) (35.7 * 70));
    sheet.setColumnWidth(1, (short) (35.7 * 150));
    sheet.setColumnWidth(2, (short) (35.7 * 120));
    sheet.setColumnWidth(3, (short) (35.7 * 120));
    sheet.setColumnWidth(4, (short) (35.7 * 100));
    sheet.setColumnWidth(5, (short) (35.7 * 80));
    sheet.setColumnWidth(6, (short) (35.7 * 80));
    sheet.setColumnWidth(7, (short) (35.7 * 100));
    sheet.setColumnWidth(8, (short) (35.7 * 75));
    sheet.setColumnWidth(9, (short) (35.7 * 160));
  }

  /**
   * 初始化sheet样式
   * 
   * @param sheet
   * @param style
   */
  private static void initHeader(HSSFSheet sheet, HSSFCellStyle style) {
    HSSFRow row1 = sheet.createRow((short) 0);
    row1.setHeight((short) (33 * 15));
    createCell(row1, 0, "序号", style);
    createCell(row1, 1, "店铺名称", style);
    createCell(row1, 2, "款式订货季度", style);
    createCell(row1, 3, "款式编号", style);
    createCell(row1, 4, "款式名称", style);
    createCell(row1, 5, "款式颜色", style);
    createCell(row1, 6, "款式尺码", style);
    createCell(row1, 7, "原价金额", style);
    createCell(row1, 8, "唯一码", style);
    createCell(row1, 9, "试穿时间", style);
  }

  /**
   * 创建单元格
   * 
   * @param row
   *          行
   * @param column
   *          列位置
   * @param value
   *          值
   * @param style
   *          样式
   */
  private static void createCell(HSSFRow row, int column, Object value, HSSFCellStyle style) {
    HSSFCell cell = row.createCell(column);
    cell.setCellValue(String.valueOf(value));
    cell.setCellStyle(style);
  }

  private static HSSFCellStyle createCellStyle(HSSFWorkbook epcBook) {
    HSSFFont font = epcBook.createFont();
    font.setFontHeightInPoints((short) 11);
    font.setFontName("宋体");
    HSSFCellStyle centerStyle = epcBook.createCellStyle();
    centerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    centerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    centerStyle.setFont(font);
    // 虚线HSSFCellStyle.BORDER_DOTTED
    // 实线HSSFCellStyle.BORDER_THIN
    centerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 下边框
    centerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
    centerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
    centerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
    return centerStyle;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
public static ChartVo convertToChartVo(List<TrendVo> voList) throws Exception {
    ChartVo<Integer, String, long[]> chartVo = new ChartVo<Integer, String, long[]>();

    for (TrendVo vo : voList) {
      Date d = CommonUtil.converStrToDate(
          "" + vo.getYear() + "-" + vo.getMonth() + "-" + vo.getDay(), "yyyy-mm-dd");
      long time = d.getTime();
      String name = CacheManager.getUnitById(vo.getOwnerId()).getName();
      boolean isHave = false;
      for ( ChartVo.Serie s : chartVo.getSeries()) {
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
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static ChartVo convertShowToChartVo(List<ShowSkuVo> voList) throws Exception {
      ChartVo<String, String, String[]> chartVo = new ChartVo<String,String, String[]>();
      ChartVo<String, String, String[]>.Categories categories = new ChartVo<String, String, String[]>().new Categories();
      for (ShowSkuVo vo : voList) {
        Date d = CommonUtil.converStrToDate(
            "" + vo.getYear() + "-" + vo.getMonth() + "-" + vo.getDay(), "yyyy-mm-dd");
        String month =  (vo.getMonth() < 10) ?("0"+vo.getMonth()):(""+vo.getMonth());
        String date = vo.getYear() + "-" + month + "-" + vo.getDay();
        long time = d.getTime();
        String name = vo.getStyleId();
        boolean isHave = false;
        
        if(!(categories.getData().contains(date))){
        	categories.addData(date);
        }
        for ( ChartVo.Serie s : chartVo.getSeries()) {
          if (s.getName().equals(name)) {
            String[] dataArr = new String[2];
            dataArr[0] = date;
            dataArr[1] = ""+vo.getQty();
            s.addData(dataArr);
            isHave = true;
            break;
          }
        }
        if (!isHave) {
          ChartVo<String, String, String[]>.Serie serie = new ChartVo<String, String, String[]>().new Serie();
          serie.setName(name);
          serie .setType("line");
          String[] dataArr = new String[2];
          dataArr[0] = date;
          dataArr[1] = ""+vo.getQty();
          serie.addData(dataArr);          
          chartVo.addSerie(serie);
        }        
        
        
      }
      Collections.sort(categories.getData());
      chartVo.setCategories(categories.getData());      

      for (ChartVo.Serie s : chartVo.getSeries()) {
    	for(String category : categories.getData()){
    		Object [] dates = new Object[s.getData().size()];
    		String[] dateArry = new String[s.getData().size()];
    		for(int i = 0; i < s.getData().size(); i++){
    			dates[i] =  s.getData().get(i); 
    			dateArry[i] = ((String [])dates[i])[0];
    			
    		}     		
    		boolean isContain = false;
    		for(String date : dateArry){
    			if(date.equals(category)){
    				isContain = true;
    				break;
    			}
    		}
    		if(!isContain){
    			String[] dataArr = new String[2];
    	        dataArr[0] = category;
    	        dataArr[1] = "0";
    	        s.addData(dataArr);    
    		}
    	}
        ChartVo<String, String, String[]>.DataStringComparator comparator = new ChartVo<String, String, String[]>().new DataStringComparator();
        Collections.sort(s.getData(),comparator);
      }  
      ChartVo<String, String, String> result = new ChartVo<String,String, String>();
      
      result.setCategories(chartVo.getCategories());
      for (ChartVo.Serie s : chartVo.getSeries()) {
    	  List<String> date =  new ArrayList<String>();
    	  for(int i = 0; i < s.getData().size(); i++){
    		  date.add(date.size(), (((String[])s.getData().get(i))[1]));
    	  }
    	  ChartVo<String, String, String>.Serie serie = new ChartVo<String, String, String>().new Serie();
    	  serie.setName((String) s.getName());
    	  serie.setType(s.getType());
    	  serie.setData(date);  
    	  result.addSerie(serie);    	  
      }   
      
      return result;
    }
    public static MapChartVo<String, String> convertShowToMapChartVo(
			List<ShowSkuVo> shopList, Map<String, Double[]> geoCoord) throws Exception{
    	MapChartVo<String, String> mapChart = new MapChartVo<String, String>();    	
    	MapChartVo<String, String>.Serie serie = new MapChartVo<String, String>().new Serie();
    	serie.setName("搬动次数");
    	serie.setType("map");
    	serie.setMapType("china");
    	serie.setGeoCoord(geoCoord);
    	List<MapData> list = new ArrayList<MapData>();
    	Map<String,MapData> proviceMap = new HashMap<String, MapData>();
    	 
    	for(ShowSkuVo s: shopList){    			
    		MapData data = new MapData(s.getName(),s.getQty());
    		String str = s.getProvince();
    		MapData provice = new MapData();
    		if(!(str.equals("北京")|| str.equals("天津"))){
    			str = str.substring(0,str.length()-1);
    		}
    		provice.setName(str);
    		provice.setValue(s.getQty());
    		if(CommonUtil.isBlank(proviceMap.get(str))){
    			proviceMap.put(str,provice);
    		}
    		else{
    			Long qty = proviceMap.get(str).getValue()+s.getQty();
    			proviceMap.get(str).setValue(qty);
    		}    		   		
            list.add(data);           
            
        		
        }    	
    	
    	serie.setData(proviceMap.values());
    	serie.getMarkPoint().setData(list);    	
    	mapChart.addSerie(serie);    	
		return mapChart;
	}

    public static SinglePage<FittingSkuVo> initFittingSkuVo(List<FittingSkuVo> dtlList) {
        SinglePage<FittingSkuVo> singlePage = new SinglePage<FittingSkuVo>();
        long totQty = 0;
        double totPrice = 0.0;
        for(FittingSkuVo dtl : dtlList) {
            Style style = CacheManager.getStyleById(dtl.getStyleId());
            if(CommonUtil.isNotBlank(style)) {
                dtl.setStyleName(style.getStyleName());
                dtl.setPrice(style.getPrice());
            }
            //dtl.set
            dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
            dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));


            if(CommonUtil.isBlank(dtl.getOwnerId())) {
                dtl.setUnitName(dtl.getOwnerId());
            } else {
                Unit u = CacheManager.getUnitById(dtl.getOwnerId());
                if(CommonUtil.isNotBlank(u))
                    dtl.setUnitName(u.getName());
            }

            totQty += dtl.getQty();
            totPrice += (dtl.getPrice()==null?0:dtl.getPrice());
        }
        singlePage.setRows(dtlList);
        singlePage.addFooter("price",""+totPrice);
        singlePage.addFooter("qty",""+totQty);
        return singlePage;
    }

    /**
     * 为hongdou添加任务明细文件下载功能
     *
     * @param
     * @param detailList
     * @return
     * @throws Exception
     */
    public static File writeTaskExcelFile(List<FittingSkuVo> detailList) throws Exception {
        HSSFWorkbook taskExcelBook = new HSSFWorkbook();
        HSSFSheet sheet = taskExcelBook.createSheet("RFID清点明细");
        HSSFRow row = null;
        HSSFCell cell = null;

        int size = 0;
        row = sheet.createRow(0);
        FittingSkuVo voOne = detailList.get(0);
        if(CommonUtil.isNotBlank(voOne.getOwnerId())) {

            cell = row.createCell(0);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue("店铺代码");

            cell = row.createCell(1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue("店铺名称");

            size = 2;
        }

        cell = row.createCell(size+0);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("产品代码");

        cell = row.createCell(size+1);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("产品名称");

        cell = row.createCell(size+2);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cell.setCellValue("价格");

        cell = row.createCell(size+3);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("颜色代码");

        cell = row.createCell(size+4);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("颜色名称");

        cell = row.createCell(size+5);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("尺寸代码");

        cell = row.createCell(size+6);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("尺寸名称");

        cell = row.createCell(size+7);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cell.setCellValue("试衣次数");
        int totQty = 0;
        for (int i = 1; i <= detailList.size(); i++) {
            row = sheet.createRow(i);
            FittingSkuVo dtl = detailList.get(i - 1);

            if (CommonUtil.isNotBlank(voOne.getOwnerId())) {
                cell = row.createCell(0);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(dtl.getOwnerId());

                cell = row.createCell(1);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(CacheManager.getUnitById(dtl.getOwnerId()).getName());
            }

            cell = row.createCell(size+0);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dtl.getStyleId());

            Style style = CacheManager.getStyleById(dtl.getStyleId());
            cell = row.createCell(size+1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(style == null ? dtl.getStyleId() : style.getStyleName());

            cell = row.createCell(size+2);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(style==null?0:style.getPrice());

            cell = row.createCell(size+3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dtl.getColorId());
            cell = row.createCell(size+4);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(CacheManager.getColorNameById(dtl.getColorId()));

            cell = row.createCell(size+5);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dtl.getSizeId());
            cell = row.createCell(size+6);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(CacheManager.getSizeNameById(dtl.getSizeId()));

            cell = row.createCell(size+7);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(dtl.getQty());

            totQty += dtl.getQty();
        }
        File file = new File(PropertyUtil.getValue("MilanUploadHistory")
                + File.separatorChar + CommonUtil.getDateString(new Date(),"yyyyMMddHHmmss") + "_" + totQty + ".xls");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        taskExcelBook.write(fos);
        fos.flush();
        fos.close();
        return file;
    }

	public static List<ShowSkuVo> converShowSkuVo(List<ShowSkuVo> subList) {
		for(ShowSkuVo s: subList){
			Style style = CacheManager.getStyleById(s.getName());
			s.setStyleId(s.getName());
			if(CommonUtil.isNotBlank(style)){
				s.setStyleName(style.getStyleName());
			}
		}
		return subList;
	}
    

	
}
