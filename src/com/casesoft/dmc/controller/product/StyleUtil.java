package com.casesoft.dmc.controller.product;

import com.casesoft.dmc.cache.CacheManager;

import com.casesoft.dmc.cache.SpringContextUtil;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.FileUtil;
import com.casesoft.dmc.core.util.file.ImgUtil;
import com.casesoft.dmc.core.vo.TagFactory;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.logistics.BillConstant;
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
import org.springframework.beans.BeanUtils;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static javax.swing.plaf.basic.BasicHTML.propertyKey;

public class StyleUtil {

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
        sty.setIspush(style.getIspush());
        List<Product> saveList = new ArrayList<>();
        int index = CacheManager.getMaxProductId();
        for(int i =0 ; i < productList.size(); i++){
            Product p = productList.get(i);
            Product product = CacheManager.getProductByCode(style.getStyleId()+p.getColorId()+p.getSizeId());
            if(CommonUtil.isBlank(product)){
                String id = ProductUtil.getNewProductId(index+i);
                p.setId(id);
            }else{
                p.setId(product.getId());
            }
            p.setStyleId(style.getStyleId());
            p.setStyleName(style.getStyleName());
            p.setColorName(CacheManager.getColorNameById(p.getColorId()));
            p.setSizeName(CacheManager.getSizeNameById(p.getSizeId()));
            p.setCode(p.getStyleId()+p.getColorId()+p.getSizeId());
            p.setBrandCode(style.getBrandCode());
            if(CommonUtil.isNotBlank(sty.getRemark())){
                p.setRemark(sty.getRemark());
            }
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
            if(files.length > 0){
                File[] photos = files[0].listFiles();
                if(photos.length > 0){
                    //d.setUrl("/product/photo/" + d.getStyleId()+"/"+files[0].getName()+"/"+photos[0].getName());
                    String url = StyleUtil.exportImgUrl(styleId, rootPath, ImgUtil.ImgExt.small);
                    return url;
                }
            }
        }
        return "/product/photo/noImg.png";
    }
    /**
     * 标签转换详情单中生成新的Style
     * @return
     */
    public static Map<String,Object> newstyleidonlabelChangeBillDel(LabelChangeBill labelChangeBill, List<LabelChangeBillDel> labelChangeBillDels,PricingRulesService pricingRulesService, ProductService productService){
        //系列的转换
        /* PricingRulesService pricingRulesService = ( PricingRulesService) SpringContextUtil.getBean(" pricingRulesService");
         ProductService productService = ( ProductService) SpringContextUtil.getBean(" productService");*/
        Map<String,Object> map=new HashMap<String,Object>();
        ArrayList<Style> list=new ArrayList<Style>();
        ArrayList<Product> listproduct=new ArrayList<Product>();
        int sum=1;//有保存几次product
        if(labelChangeBill.getChangeType().equals(BillConstant.ChangeType.Series)){
            String seriesid=labelChangeBill.getNowclass9().split("-")[1];
            for(int i=0;i<labelChangeBillDels.size();i++){
                String styleId = labelChangeBillDels.get(i).getStyleId();
                Style style= CacheManager.getStyleById(styleId +seriesid);
                if(CommonUtil.isBlank(style)){
                    Style formerstyle = CacheManager.getStyleById(styleId);
                    Style stylenew=new Style();
                    BeanUtils.copyProperties(formerstyle,stylenew);
                    stylenew.setId(styleId + seriesid);
                    stylenew.setStyleId(styleId + seriesid);

                    PricingRules series = pricingRulesService.get("series", seriesid);
                    stylenew.setPrice(CommonUtil.doubleChange(formerstyle.getPreCast()*series.getRule1(),1));
                    stylenew.setPuPrice(CommonUtil.doubleChange(stylenew.getPrice()*series.getRule3(),1));
                    stylenew.setWsPrice(CommonUtil.doubleChange(stylenew.getPrice()*series.getRule2(),1));
                    stylenew.setClass9(seriesid);
                    list.add(stylenew);

                    /*List<Product> productList = productService.findByStyleId(styleId);
                    for(int a=0;a<productList.size();a++){
                        Product product = productList.get(a);
                        Product productnew=new Product();
                        BeanUtils.copyProperties(product,productnew);
                        String maxProductId = productService.getMaxProductId();
                        String id = ProductUtil.getNewProductId(Integer.parseInt(maxProductId)+sum);
                        productnew.setId(id);

                        productnew.setStyleId(styleId +seriesid);
                        productnew.setCode(styleId +labelChangeBill.getNowclass9().split("-")[1]+productnew.getColorId()+productnew.getSizeName());
                        sum++;
                        listproduct.add(productnew);
                    }*/
                }
                String code=styleId +labelChangeBill.getNowclass9().split("-")[1]+labelChangeBillDels.get(i).getColorId()+labelChangeBillDels.get(i).getSizeId();
                Product productByCode = CacheManager.getProductByCode(code);
                if(CommonUtil.isBlank(productByCode)){
                    Product oldProperty= CacheManager.getProductByCode(labelChangeBillDels.get(i).getSku());
                    if(CommonUtil.isNotBlank(oldProperty)){
                        Product productnew=new Product();
                        BeanUtils.copyProperties(oldProperty,productnew);
                        String maxProductId = productService.getMaxProductId();
                        String id = ProductUtil.getNewProductId(Integer.parseInt(maxProductId)+sum);
                        productnew.setId(id);
                        productnew.setStyleId(styleId +seriesid);
                        productnew.setCode(styleId +labelChangeBill.getNowclass9().split("-")[1]+productnew.getColorId()+productnew.getSizeName());
                        listproduct.add(productnew);
                    }else{
                        Product productnew=new Product();
                        String maxProductId = productService.getMaxProductId();
                        String id = ProductUtil.getNewProductId(Integer.parseInt(maxProductId)+sum);
                        productnew.setId(id);
                        productnew.setStyleId(styleId +seriesid);
                        productnew.setCode(styleId +labelChangeBill.getNowclass9().split("-")[1]+labelChangeBillDels.get(i).getColorId()+labelChangeBillDels.get(i).getSizeId());
                        productnew.setStyleName(style.getStyleName());
                        productnew.setColorId(labelChangeBillDels.get(i).getColorId());
                        productnew.setColorName(labelChangeBillDels.get(i).getColorId());
                        productnew.setSizeId(labelChangeBillDels.get(i).getSizeId());
                        productnew.setSizeName(labelChangeBillDels.get(i).getSizeId());
                        productnew.setIsDeton(0);
                        listproduct.add(productnew);
                    }


                    sum++;
                }

            }
            map.put("newStylesuffix",seriesid);
        }
        //打折的转换
        if(labelChangeBill.getChangeType().equals(BillConstant.ChangeType.Price)){
            for(int i=0;i<labelChangeBillDels.size();i++){
                String styleId = labelChangeBillDels.get(i).getStyleId();
                Style style= CacheManager.getStyleById(styleId +BillConstant.styleNew.PriceDiscount);
                if(CommonUtil.isBlank(style)){
                    Style formerstyle = CacheManager.getStyleById(styleId);
                    Style stylenew=new Style();
                    BeanUtils.copyProperties(formerstyle,stylenew);
                    stylenew.setId(styleId + BillConstant.styleNew.PriceDiscount);
                    stylenew.setStyleId(styleId + BillConstant.styleNew.PriceDiscount);
                    stylenew.setPrice(CommonUtil.doubleChange(formerstyle.getPrice()*labelChangeBillDels.get(i).getDiscount(),1));
                    stylenew.setPuPrice(CommonUtil.doubleChange(formerstyle.getPuPrice()*labelChangeBillDels.get(i).getDiscount(),1));
                    stylenew.setWsPrice(CommonUtil.doubleChange(formerstyle.getWsPrice()*labelChangeBillDels.get(i).getDiscount(),1));
                    list.add(stylenew);
                    /*List<Product> productList = productService.findByStyleId(styleId);
                    for(int a=0;a<productList.size();a++){
                        Product product = productList.get(a);
                        Product productnew=new Product();
                        BeanUtils.copyProperties(product,productnew);
                        String maxProductId = productService.getMaxProductId();
                        productnew.setId((Integer.parseInt(maxProductId)+sum)+"");
                        productnew.setStyleId(styleId +BillConstant.styleNew.PriceDiscount);
                        productnew.setCode(styleId +BillConstant.styleNew.PriceDiscount+productnew.getColorId()+productnew.getSizeName());
                        sum++;
                        listproduct.add(productnew);
                    }*/
                }
                String code=styleId +BillConstant.styleNew.PriceDiscount+labelChangeBillDels.get(i).getColorId()+labelChangeBillDels.get(i).getSizeId();
                Product productByCode = CacheManager.getProductByCode(code);
                if(CommonUtil.isBlank(productByCode)){
                    Product oldProperty= CacheManager.getProductByCode(labelChangeBillDels.get(i).getSku());
                    if(CommonUtil.isNotBlank(oldProperty)){
                        Product productnew=new Product();
                        BeanUtils.copyProperties(oldProperty,productnew);
                        String maxProductId = productService.getMaxProductId();
                        String id = ProductUtil.getNewProductId(Integer.parseInt(maxProductId)+sum);
                        productnew.setId(id);
                        productnew.setStyleId(styleId +BillConstant.styleNew.PriceDiscount);
                        productnew.setCode(styleId +BillConstant.styleNew.PriceDiscount+productnew.getColorId()+productnew.getSizeName());
                        listproduct.add(productnew);
                    }else{
                        Product productnew=new Product();
                        String maxProductId = productService.getMaxProductId();
                        String id = ProductUtil.getNewProductId(Integer.parseInt(maxProductId)+sum);
                        productnew.setId(id);
                        productnew.setStyleId(styleId +BillConstant.styleNew.PriceDiscount);
                        productnew.setCode(styleId +BillConstant.styleNew.PriceDiscount+labelChangeBillDels.get(i).getColorId()+labelChangeBillDels.get(i).getSizeId());
                        productnew.setStyleName(style.getStyleName());
                        productnew.setColorId(labelChangeBillDels.get(i).getColorId());
                        productnew.setColorName(labelChangeBillDels.get(i).getColorId());
                        productnew.setSizeId(labelChangeBillDels.get(i).getSizeId());
                        productnew.setSizeName(labelChangeBillDels.get(i).getSizeId());
                        productnew.setIsDeton(0);
                        listproduct.add(productnew);
                    }
                    sum++;
                }
            }
            map.put("newStylesuffix",BillConstant.styleNew.PriceDiscount);
        }
        map.put("style",list);
        map.put("product",listproduct);
        return map;
    }

}
