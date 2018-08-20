package com.casesoft.dmc.controller.product;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;

import com.casesoft.dmc.cache.RedisUtils;
import com.casesoft.dmc.cache.SpringContextUtil;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.FileUtil;
import com.casesoft.dmc.core.util.file.ImgUtil;
import com.casesoft.dmc.core.vo.TagFactory;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.logistics.BillConstant;
import com.casesoft.dmc.model.logistics.ConsignmentBill;
import com.casesoft.dmc.model.logistics.LabelChangeBill;
import com.casesoft.dmc.model.logistics.LabelChangeBillDel;
import com.casesoft.dmc.model.product.Color;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.product.Size;
import com.casesoft.dmc.model.product.Style;

import com.casesoft.dmc.model.sys.PricingRules;
import com.casesoft.dmc.service.product.ProductService;
import com.casesoft.dmc.service.sys.impl.PricingRulesService;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static javax.swing.plaf.basic.BasicHTML.propertyKey;

public class StyleUtil {
    private static RedisUtils redisUtils = (RedisUtils) SpringContextUtil.getBean("redisUtils");

    static List<Style> processStyleFile(InputStream inputStream) throws Exception {
    List<Style> styleList = new ArrayList<Style>();
    HSSFWorkbook workbook = new HSSFWorkbook(inputStream);// 创建对Excel工作薄文件的引用
    HSSFSheet sheet = workbook.getSheetAt(0);// 创建对工作表的引用，也可用workbook.getSheet("sheetName");
    Iterator<Row> it = sheet.iterator();
    String value = "";
    Style style = null;
   // int styleLength = PropertyUtil.getIntValue("style_length");
    int styleLength = TagFactory.getCurrentTag().getStyleLength();
    int i = 0;
    try {
      while (it.hasNext()) {
        i++;
        Row row = it.next();
        if (i == 1)
          continue;

        style = new Style();
        value = getStrFormCell(row, styleLength);
        if (CommonUtil.isBlank(value))
          break;
        style.setId(value);
        style.setStyleId(value);
        value = row.getCell(1).getStringCellValue().trim();
        if (CommonUtil.isBlank(value))
          break;
        style.setStyleName(value);
        double price = getPrice(row);// row.getCell(2).getNumericCellValue();
        style.setPrice(price);

        // 因日播增加系列打印 winston
        Cell cell = row.getCell(3);
        if (!CommonUtil.isBlank(cell)) {
          value = cell.getStringCellValue().trim();
          style.setRemark(value);
        }

        styleList.add(style);
      }
    } catch (Exception e) {
      if (styleList.size() <= 0)
        throw new Exception("解析文件出错!");
    }
    return styleList;
  }

  private static double getPrice(Row row) {
    double price = 0.0;
    try {
      price = row.getCell(2).getNumericCellValue();
    } catch (Exception e) {
      String temp = row.getCell(2).getStringCellValue().trim();
      price = Double.parseDouble(temp);
    }
    return price;
  }

  static List<Color> processColorFile(InputStream inputStream) throws Exception {
    List<Color> colorList = new ArrayList<Color>();
    HSSFWorkbook workbook = new HSSFWorkbook(inputStream);// 创建对Excel工作薄文件的引用
    HSSFSheet sheet = workbook.getSheetAt(0);// 创建对工作表的引用，也可用workbook.getSheet("sheetName");
    Iterator<Row> it = sheet.iterator();
    String value = "";
   // int length = PropertyUtil.getIntValue("color_length");
    int length = TagFactory.getCurrentTag().getColorLength();
    Color color = null;
    int i = 0;
    while (it.hasNext()) {
      i++;
      Row row = it.next();
      if (i == 1)
        continue;
      color = new Color();
      value = getStrFormCell(row, length);
      if (CommonUtil.isBlank(value))
        break;
      color.setId(value);
      color.setColorId(value);
      value = row.getCell(1).getStringCellValue().trim();
      if (CommonUtil.isBlank(value))
        break;
      color.setColorName(value);

      colorList.add(color);
    }
    return colorList;
  }

  static List<Size> processSizeFile(InputStream inputStream) throws Exception {
    List<Size> sizeList = new ArrayList<Size>();
    HSSFWorkbook workbook = new HSSFWorkbook(inputStream);// 创建对Excel工作薄文件的引用
    HSSFSheet sheet = workbook.getSheetAt(0);// 创建对工作表的引用，也可用workbook.getSheet("sheetName");
    Iterator<Row> it = sheet.iterator();
    String value = "";
    //int length = PropertyUtil.getIntValue("size_length");
    int length = TagFactory.getCurrentTag().getSizeLength();
    Size s = null;
    int i = 0;
    while (it.hasNext()) {
      i++;
      Row row = it.next();
      if (i == 1)
        continue;
      s = new Size();
      value = getStrFormCell(row, length);
      if (CommonUtil.isBlank(value))
        break;
      s.setId(value);
      s.setSizeId(value);
      try {
        value = row.getCell(1).getStringCellValue().trim();
      } catch (Exception ex) {
        value =""+row.getCell(1).getNumericCellValue();
      }
      if (CommonUtil.isBlank(value))
        break;
      s.setSizeName(value);
      value = row.getCell(2).getStringCellValue().trim();
      if (CommonUtil.isBlank(value))
        break;
      s.setSortId(value);

      sizeList.add(s);
    }
    return sizeList;
  }

  private static String getStrFormCell(Row row, int length) {
    String str = "";
    try {
      str = row.getCell(0).getStringCellValue().trim();// 款
    } catch (Exception e) {
      int temp = (int) row.getCell(0).getNumericCellValue();
      str = CommonUtil.convertIntToString(temp, length);
    }
    return str;
  }

  static List<PropertyFilter> createrPropertyFilter(final HttpServletRequest request,
      final String requestParam, final String filterParam) {
    String requestValue = request.getParameter(requestParam);
    if (null == requestValue || "".equals(requestValue) || requestValue.length() < 3)
      return null;
    List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
    PropertyFilter filter = new PropertyFilter(filterParam, requestValue);
    filters.add(filter);
    return filters;
  }

    public static String createStyleInFilter(List<Style> styleList) {
        StringBuffer stringBuffer = new StringBuffer();
        for(Style style : styleList) {
            stringBuffer.append(",").append(style.getStyleId());
        }
        String value = stringBuffer.substring(1);
        return value;
    }

	public static List<String> setStyleImagePath(String styleId, String root) {
		List<String> list = new ArrayList<String>();
		if(CommonUtil.isNotBlank(styleId)){
			File file = new File(root+styleId);
			File[] imageFile = FileUtil.filterFile(file, "JPG");
            if(CommonUtil.isNotBlank(imageFile)) {
              for (File f : imageFile) {
                list.add("/images/style/" + styleId + "/" + f.getName());
              }
            }
			
		}else{
			File file = new File(root);
			File[] imageFile = FileUtil.filterFile(file, "");
          if(CommonUtil.isNotBlank(imageFile)) {
            for (File f : imageFile) {
              if (f.isDirectory()) {
                File[] files = FileUtil.filterFile(f, "JPG");
                for (File iamge : files) {
                  list.add("/images/style/" + f.getName() + "/" + iamge.getName());
                }
              }
            }
          }
		}
		return list;
	}

	public static List<Style> uploadNewExcel(InputStream in, String suffix) throws Exception {
		String date = CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss");
		Workbook workbook = null;  
		if(".xls".equals(suffix)){  
			workbook = new HSSFWorkbook(in);  
        }else if(".xlsx".equals(suffix)){  
        	workbook = new XSSFWorkbook(in);  
        }
		Map<String,Style> styleMap = new HashMap<String, Style>();
		Sheet sheet = workbook.getSheetAt(0);
		Style style = null;
		
		Pattern FilePattern = Pattern.compile("[\\\\/:*?\"<>|]");  
		Iterator<Row> iterator = sheet.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			i++;
			
			Row row = iterator.next();
			if(i == 1)
			   continue;
			String styleId = FilePattern.matcher(getStringFormCell(row.getCell(0))).replaceAll("-");
			if(CommonUtil.isBlank(styleId)){
				throw new Exception("第"+(i)+"行款号为空,请完善相关信息");
			}
			if(styleMap.containsKey(styleId)){
				throw new Exception("第"+(i)+"行信息重复,请删除该信息");
			}
			String styleName = getStringFormCell(row.getCell(1));
			if(CommonUtil.isBlank(styleName)){
				throw new Exception("第"+(i)+"行款名为空,请完善相关信息");
			}
			double price = getPrice(row);
			String remark =  getStringFormCell(row.getCell(3));
			style = new Style(styleId,styleId,styleName,price);
			style.setRemark(remark);
			style.setUpdateTime(date);
			styleMap.put(styleId, style);
		}
		return new ArrayList<Style>(styleMap.values());
	}

	private static String getStringFormCell(Cell cell) {
		try {
			return cell.getStringCellValue().toString().trim();
		} catch (java.lang.IllegalStateException ex) {
			return String.valueOf((int) cell.getNumericCellValue());
		}
	}

    public static List<Product> covertToProductInfo(Style sty, Style style, List<Product> productList) {
        sty.setStyleName(style.getStyleName());
        sty.setPrice(style.getPrice());
        sty.setRemark(style.getRemark());
        sty.setPreCast(style.getPreCast());
        sty.setPuPrice(style.getPuPrice());
        sty.setWsPrice(style.getWsPrice());
        sty.setBrandCode(style.getBrandCode());
        sty.setSizeSortId(style.getSizeSortId());
        sty.setUpdateTime(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        sty.setStyleEname(style.getStyleEname());
        sty.setBrandCode(style.getClass1());
        sty.setClass1(style.getClass1());
        sty.setClass2(style.getClass2());
        sty.setClass3(style.getClass3());
        sty.setClass4(style.getClass4());
        sty.setClass5(style.getClass5());
        sty.setClass6(style.getClass6());
        sty.setClass6(style.getClass6());
        sty.setClass7(style.getClass7());
        sty.setClass8(style.getClass8());
        sty.setClass9(style.getClass9());
        sty.setClass10(style.getClass10());
//        sty.setRules(style.getRules());
        sty.setIspush(style.getIspush());
        sty.setStyleCycle(style.getStyleCycle());
        sty.setBargainPrice(style.getBargainPrice());
        sty.setIsSeries(style.getIsSeries());
        List<Product> saveList = new ArrayList<>();
        int index = CacheManager.getMaxProductId();
        for(int i =0 ; i < productList.size(); i++){
            Product p = productList.get(i);
            Product product = CacheManager.getProductByCode(style.getStyleId()+p.getColorId()+p.getSizeId());//by sku
            if(CommonUtil.isBlank(product)){

                String id = ProductUtil.getNewProductId(index+i);
                p.setId(id);
                //查最新的版本号+1
                Long maxVersionId = CacheManager.getproductMaxVersionId();
                p.setVersion(maxVersionId+1);

            }else{
                p.setId(product.getId());
            }
            //查最新的版本号+1
            Long maxVersionId = CacheManager.getproductMaxVersionId();
            p.setVersion(maxVersionId+1);
            p.setStyleId(style.getStyleId());
            p.setStyleName(style.getStyleName());
            p.setColorName(CacheManager.getColorNameById(p.getColorId()));
            p.setSizeName(CacheManager.getSizeNameById(p.getSizeId()));
            p.setCode(p.getStyleId()+p.getColorId()+p.getSizeId());
            p.setBrandCode(style.getBrandCode());
            p.setStyleCycle(style.getStyleCycle());
            saveList.add(p);
        }
        return saveList;
    }

    public static void copyStyleInfo(Style sty, Style style) {
        sty.setStyleName(style.getStyleName());
        sty.setPrice(style.getPrice());
        sty.setRemark(style.getRemark());
        sty.setPreCast(style.getPreCast());
        sty.setPuPrice(style.getPuPrice());
        sty.setWsPrice(style.getWsPrice());
        sty.setBrandCode(style.getBrandCode());
        sty.setSizeSortId(style.getSizeSortId());
        sty.setUpdateTime(CommonUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        sty.setStyleEname(style.getStyleEname());
        style.setBrandCode(style.getClass1());
        sty.setClass1(style.getClass1());
        sty.setClass2(style.getClass2());
        sty.setClass3(style.getClass3());
        sty.setClass4(style.getClass4());
        sty.setClass5(style.getClass5());
        sty.setClass6(style.getClass6());
        sty.setClass6(style.getClass6());
        sty.setClass7(style.getClass7());
        sty.setClass8(style.getClass8());
        sty.setClass9(style.getClass9());
        sty.setClass10(style.getClass10());
    }
    /**
     * @param styleId 款号
     * @param rootPath 路径
     * @return 图片路径
    * */
    public static String findImgUrl(String styleId, String rootPath) {
        File file =  new File(rootPath + "/product/photo/" + styleId);
        if(file.exists()){
            File[] files = file.listFiles();
            if(files.length > 0){
                File[] photos = files[0].listFiles();
                if(photos.length > 0){
                    File photo = new File(photos[0].getPath());
                    if(photo.getName().lastIndexOf(".")==-1){
                        //将无后缀名文件转为图片
                        File newFile = new File(photo.getParentFile(),photo.getName()+".png");
                        boolean flag = photo.renameTo(newFile);
                        if(flag) {
                            return "/product/photo/" + styleId + "/" + files[0].getName() + "/" + newFile.getName();
                        }else{
                        }
                    }else{
                        return "/product/photo/" + styleId+"/"+files[0].getName()+"/"+photos[0].getName();
                    }
                }
            }
        }
        return "/product/photo/noImg.png";
    }

    /**
     * @param styleId 款号
     * @param rootPath 路径
     * @param imageExt 图片后缀
     * @return 压缩图片路径
     * */
    public static String exportImgUrl( String styleId, String rootPath,String imageExt) {
        File file =  new File(rootPath + "/product/photo/" + styleId);
        if(file.exists()){
            File[] files = file.listFiles();
            if(files.length > 0){
                File[] photos = files[0].listFiles();
                if(photos.length > 0){
                    File photo = new File(photos[0].getPath());
                    if(photo.getName().lastIndexOf(".")==-1){
                        //将无后缀名文件转为图片
                        File newFile = new File(photo.getParentFile(),photo.getName()+".png");
                        boolean flag = photo.renameTo(newFile);
                        if(flag) {
                            File exportFile =new File(rootPath+"/product/photo/" + styleId + "/" + files[0].getName() + "/" + photo.getName()+imageExt+".png");
                            if(!exportFile.exists()){
                                ImgUtil.img_change(rootPath+"/product/photo/" + styleId + "/" + files[0].getName() + "/" ,newFile.getName());
                            }
                            return "/product/photo/" + styleId + "/" + files[0].getName() + "/" + photo.getName()+imageExt+".png";

                        }
                    }else{
                        String photoname= photos[0].getName().substring(0,photos[0].getName().lastIndexOf("."));
                        String photoExt= photos[0].getName().substring(photos[0].getName().lastIndexOf("."));
                        File exportFile =new File(rootPath+"/product/photo/" + styleId + "/" + files[0].getName() + "/" +  photoname+imageExt+photoExt);
                        if(!exportFile.exists()){
                            ImgUtil.img_change(rootPath+"/product/photo/" + styleId + "/" + files[0].getName() + "/" ,photos[0].getName());
                        }
                        return "/product/photo/" + styleId + "/" + files[0].getName() + "/" +  photoname+imageExt+photoExt;
                    }
                }
            }
        }
        return "/product/photo/noImg.png";
    }
    /**
     * @param styleId 款号
     * @param rootPath 路径
     *  @return 图片路径
     * */
    public static String returnImageUrl(String styleId, String rootPath){
        File file =  new File(rootPath + "/product/photo/" + styleId);
        if(file.exists()){
            File[] files = file.listFiles();
            if(CommonUtil.isNotBlank(files)){
                if(files.length > 0){
                    File[] photos = files[0].listFiles();
                    if(CommonUtil.isNotBlank(photos)){
                        if(photos.length > 0){
                            //d.setUrl("/product/photo/" + d.getStyleId()+"/"+files[0].getName()+"/"+photos[0].getName());
                            String url = StyleUtil.exportImgUrl(styleId, rootPath, ImgUtil.ImgExt.small);
                            return url;
                        }else{
                            return "/product/photo/noImg.png";
                        }
                    }else{
                        return "/product/photo/noImg.png";
                    }

                }
            }else{
                return "/product/photo/noImg.png";
            }

        }
        return "/product/photo/noImg.png";
    }

    /**
     * 标签转换详情单中生成新的Style
     * @param labelChangeBill labelChangeBill实体
     * @param labelChangeBillDels labelChangeBillDel的list对象
     * @param pricingRulesService pricingRulesService的对象
     * @param productService productService的对象
     * @return
     */
    public static Map<String,Object> newstyleidonlabelChangeBillDel(LabelChangeBill labelChangeBill, List<LabelChangeBillDel> labelChangeBillDels,PricingRulesService pricingRulesService, ProductService productService){
        //系列的转换
        org.slf4j.Logger  logger = LoggerFactory.getLogger(LabelChangeBill.class);
        /* PricingRulesService pricingRulesService = ( PricingRulesService) SpringContextUtil.getBean(" pricingRulesService");
         ProductService productService = ( ProductService) SpringContextUtil.getBean(" productService");*/
        Map<String,Object> map=new HashMap<String,Object>();
        ArrayList<Style> list=new ArrayList<Style>();
        ArrayList<Product> listproduct=new ArrayList<Product>();
        Map<String,Style> mapStyle=new HashMap<String,Style>();
        boolean isUseOldStyle=false;
        int sum=1;//有保存几次product
        boolean ishavePricingRules=true;//是否有相应的对应定价规则
        String message="";
        if(labelChangeBill.getChangeType().equals(BillConstant.ChangeType.Series)){
            String seriesid=labelChangeBill.getNowclass9().split("-")[1];
            for(int i=0;i<labelChangeBillDels.size();i++){
                String styleId = labelChangeBillDels.get(i).getStyleId();
                //判断最后两位是有AA,AS,PD
                int styleIdLength = styleId.length();
                String styleTail=styleId.substring(styleIdLength-2,styleIdLength);
                if(styleTail.equals(BillConstant.styleNew.Alice)||styleTail.equals(BillConstant.styleNew.AncientStone)){
                    styleId=styleId.substring(0,styleIdLength-2);
                    Style style= CacheManager.getStyleById(styleId);
                    if(CommonUtil.isBlank(style)){
                        logger.error(labelChangeBill.getBillNo()+":StyleUtil没有"+styleId);
                        isUseOldStyle=false;
                    }else{
                        logger.error(labelChangeBill.getBillNo()+":StyleUtil有"+styleId);
                        isUseOldStyle=true;
                    }
                }else {
                    logger.error(labelChangeBill.getBillNo()+":StyleUtil"+styleId+"后缀没有AA和AS");
                    isUseOldStyle=false;
                }
                String stylePDTail=styleId.substring(styleIdLength-4,styleIdLength-2);
                if(stylePDTail.equals(BillConstant.styleNew.PriceDiscount)){
                    styleId=styleId.substring(0,styleIdLength-4);
                }
                Style style= CacheManager.getStyleById(styleId +seriesid);
                Style oldStyle = CacheManager.getStyleById(styleId);
                if(!isUseOldStyle){
                    if(CommonUtil.isBlank(style)&&CommonUtil.isBlank(mapStyle.get(styleId + seriesid))){

                        Style styleNew=new Style();
                        BeanUtils.copyProperties(oldStyle,styleNew);
                        styleNew.setId(styleId + seriesid);
                        styleNew.setStyleId(styleId + seriesid);
                        PricingRules series = pricingRulesService.findBySC(seriesid,styleNew.getClass3());
                        if(CommonUtil.isBlank(series)){
                            ishavePricingRules=false;
                            if(CommonUtil.isBlank(message)){
                                message+=seriesid+"-"+styleNew.getClass3();
                            }else{
                                message+=seriesid+"-"+styleNew.getClass3()+",";
                            }
                        }else {
                            styleNew.setPrice(CommonUtil.getInt(CommonUtil.doubleChange(oldStyle.getPreCast() * series.getRule1(), 1) / 10) * 10 + 9);
                            styleNew.setPuPrice(CommonUtil.doubleChange(styleNew.getPrice() * series.getRule3(), 1));
                            styleNew.setWsPrice(CommonUtil.doubleChange(styleNew.getPrice() * series.getRule2(), 1));
                            styleNew.setClass9(seriesid);
                            mapStyle.put((styleId + seriesid), styleNew);
                            list.add(styleNew);
                        }
                    }
                    String code=styleId +labelChangeBill.getNowclass9().split("-")[1]+labelChangeBillDels.get(i).getColorId()+labelChangeBillDels.get(i).getSizeId();
                    Product productByCode = CacheManager.getProductByCode(code);
                    if(CommonUtil.isBlank(productByCode)){
                        Product oldProduct= CacheManager.getProductByCode(labelChangeBillDels.get(i).getSku());
                        if(CommonUtil.isNotBlank(oldProduct)){
                            Product productNew=new Product();
                            BeanUtils.copyProperties(oldProduct,productNew);
                            String maxProductId = productService.getMaxProductId();
                            String id = ProductUtil.getNewProductId(Integer.parseInt(maxProductId)+sum);
                            productNew.setId(id);
                            productNew.setStyleId(styleId +seriesid);
                            productNew.setCode(styleId +labelChangeBill.getNowclass9().split("-")[1]+productNew.getColorId()+productNew.getSizeName());
                            listproduct.add(productNew);
                        }else{
                            Product productNew=new Product();
                            String maxProductId = productService.getMaxProductId();
                            String id = ProductUtil.getNewProductId(Integer.parseInt(maxProductId)+sum);
                            productNew.setId(id);
                            productNew.setStyleId(styleId +seriesid);
                            productNew.setCode(styleId +labelChangeBill.getNowclass9().split("-")[1]+labelChangeBillDels.get(i).getColorId()+labelChangeBillDels.get(i).getSizeId());
                            productNew.setStyleName(oldStyle.getStyleName());
                            productNew.setColorId(labelChangeBillDels.get(i).getColorId());
                            productNew.setColorName(labelChangeBillDels.get(i).getColorId());
                            productNew.setSizeId(labelChangeBillDels.get(i).getSizeId());
                            productNew.setSizeName(labelChangeBillDels.get(i).getSizeId());
                            productNew.setIsDeton(0);
                            listproduct.add(productNew);
                        }


                        sum++;
                    }
                }


            }
            map.put("newStylesuffix",seriesid);
        }
        //打折的转换
        if(labelChangeBill.getChangeType().equals(BillConstant.ChangeType.Price)){
            for(int i=0;i<labelChangeBillDels.size();i++){
                String styleId = labelChangeBillDels.get(i).getStyleId();

                Style style= CacheManager.getStyleById(styleId +BillConstant.styleNew.PriceDiscount+CommonUtil.getInt(labelChangeBillDels.get(i).getDiscount()));
                Style oldStyle = CacheManager.getStyleById(styleId);
                if(CommonUtil.isBlank(style)&&CommonUtil.isBlank(mapStyle.get(styleId + BillConstant.styleNew.PriceDiscount+CommonUtil.getInt(labelChangeBillDels.get(i).getDiscount())))){
                    Style styleNew=new Style();
                    BeanUtils.copyProperties(oldStyle,styleNew);
                    styleNew.setId(styleId + BillConstant.styleNew.PriceDiscount+CommonUtil.getInt(labelChangeBillDels.get(i).getDiscount()));
                    styleNew.setStyleId(styleId + BillConstant.styleNew.PriceDiscount+CommonUtil.getInt(labelChangeBillDels.get(i).getDiscount()));
                    styleNew.setPrice(CommonUtil.getInt(CommonUtil.doubleChange(oldStyle.getPrice()*labelChangeBillDels.get(i).getDiscount()/100,1)/10)*10+9);
                    styleNew.setPuPrice(CommonUtil.doubleChange(oldStyle.getPuPrice()*labelChangeBillDels.get(i).getDiscount()/100,1));
                    styleNew.setWsPrice(CommonUtil.doubleChange(oldStyle.getWsPrice()*labelChangeBillDels.get(i).getDiscount()/100,1));
                    list.add(styleNew);
                    mapStyle.put((styleId + BillConstant.styleNew.PriceDiscount+CommonUtil.getInt(labelChangeBillDels.get(i).getDiscount())),styleNew);
                }
                String code=styleId +BillConstant.styleNew.PriceDiscount+CommonUtil.getInt(labelChangeBillDels.get(i).getDiscount())+labelChangeBillDels.get(i).getColorId()+labelChangeBillDels.get(i).getSizeId();
                Product productByCode = CacheManager.getProductByCode(code);
                if(CommonUtil.isBlank(productByCode)){
                    Product oldProduct= CacheManager.getProductByCode(labelChangeBillDels.get(i).getSku());
                    if(CommonUtil.isNotBlank(oldProduct)){
                        Product productNew=new Product();
                        BeanUtils.copyProperties(oldProduct,productNew);
                        String maxProductId = productService.getMaxProductId();
                        String id = ProductUtil.getNewProductId(Integer.parseInt(maxProductId)+sum);
                        productNew.setId(id);
                        productNew.setStyleId(styleId +BillConstant.styleNew.PriceDiscount+CommonUtil.getInt(labelChangeBillDels.get(i).getDiscount()));
                        productNew.setCode(styleId +BillConstant.styleNew.PriceDiscount+CommonUtil.getInt(labelChangeBillDels.get(i).getDiscount())+productNew.getColorId()+productNew.getSizeName());
                        listproduct.add(productNew);
                    }else{
                        Product productNew=new Product();
                        String maxProductId = productService.getMaxProductId();
                        String id = ProductUtil.getNewProductId(Integer.parseInt(maxProductId)+sum);
                        productNew.setId(id);
                        productNew.setStyleId(styleId +BillConstant.styleNew.PriceDiscount+CommonUtil.getInt(labelChangeBillDels.get(i).getDiscount()));
                        productNew.setCode(styleId +BillConstant.styleNew.PriceDiscount+CommonUtil.getInt(labelChangeBillDels.get(i).getDiscount())+labelChangeBillDels.get(i).getColorId()+labelChangeBillDels.get(i).getSizeId());
                        productNew.setStyleName(oldStyle.getStyleName());
                        productNew.setColorId(labelChangeBillDels.get(i).getColorId());
                        productNew.setColorName(labelChangeBillDels.get(i).getColorId());
                        productNew.setSizeId(labelChangeBillDels.get(i).getSizeId());
                        productNew.setSizeName(labelChangeBillDels.get(i).getSizeId());
                        productNew.setIsDeton(0);
                        listproduct.add(productNew);
                    }
                    sum++;
                }
            }
            map.put("newStylesuffix",BillConstant.styleNew.PriceDiscount);
        }
        map.put("message",message);
        map.put("ishavePricingRules",ishavePricingRules);
        map.put("style",list);
        map.put("product",listproduct);
        return map;
    }

}
