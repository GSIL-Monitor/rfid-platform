package com.casesoft.dmc.controller.product;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.FileUtil;
import com.casesoft.dmc.model.product.Color;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.product.StyleCombine;
import com.casesoft.dmc.model.product.StyleDescription;
import com.casesoft.dmc.model.product.Suit;
import com.casesoft.dmc.model.product.SuitDtl;

public class SuitUtil {

	public static List<String> setSuitImagePath(String code, String root) {
		List<String> list = new ArrayList<String>();
		if (CommonUtil.isNotBlank(code)) {
			File file = new File(root + code);
			File[] imageFile = FileUtil.filterFile(file, "JPG");
			if(CommonUtil.isNotBlank(imageFile)){
				for (File f : imageFile) {
					list.add("/images/suit/" + code + "/" + f.getName());
				}				
			}

		} else {
			File file = new File(root);
			File[] imageFile = FileUtil.filterFile(file, "");

			for (File f : imageFile) {
				if (f.isDirectory()) {
					File[] files = FileUtil.filterFile(f, "JPG");
					for (File iamge : files) {
						list.add("/images/suit/" + f.getName() + "/"
								+ iamge.getName());
					}
				}
			}
		}
		return list;
	}
	public static void initDtl(Suit s, List<SuitDtl> dtlList) {
		s.setImageUrl("/images/suit/" + s.getCode());
		String remark = s.getRemark();
		remark.replace('\t', ' ');
		remark.replace('\\', ' ');

		s.setRemark(remark);
		for (SuitDtl dtl : dtlList) {
			dtl.setSuitCode(s.getCode());
		}
		s.setList(dtlList);
	}

	public static List<SuitDtl> covertToDtlVo(List<SuitDtl> list) {
		for (SuitDtl dtl : list) {
			Style s = CacheManager.getStyleById(dtl.getStyleId());
			Color color=CacheManager.getColorById(dtl.getColorId());
			if(CommonUtil.isNotBlank(color)){
				dtl.setColorName(color.getColorName());
			}
			dtl.setStyleName(s.getStyleName());
			dtl.setPrice(s.getPrice());
		}
		return list;
	}
	public static List<SuitDtl> covertToDtlVo(String root,List<SuitDtl> list) {
		for (SuitDtl dtl : list) {
			Style s = CacheManager.getStyleById(dtl.getStyleId());
			File file = new File(root + dtl.getStyleId()+"/"+dtl.getColorId());
			File[] imageFile = FileUtil.filterFile(file, "JPG");
			List<String> imgs = new ArrayList<String>();
			if(CommonUtil.isNotBlank(imageFile)){
				for (File f : imageFile) {
					imgs.add("/images/style/" + dtl.getStyleId()+"/"+dtl.getColorId() +"/"+ f.getName() );
				}				
			}
			Color color=CacheManager.getColorById(dtl.getColorId());
			if(CommonUtil.isNotBlank(color)){
				dtl.setColorName(color.getColorName());
			}
			dtl.setStyleName(s.getStyleName());
			dtl.setPrice(s.getPrice());
			dtl.setImages(imgs);
		}
		return list;
	}
	public static List<StyleCombine> covertToCombineVo(boolean isWS,String root,List<StyleCombine> list) {
		for (StyleCombine dtl : list) {
			Style s2 = CacheManager.getStyleById(dtl.getCombineStyleId());
			Style s = CacheManager.getStyleById(dtl.getStyleId());
			if(CommonUtil.isNotBlank(root)){
				File file = new File(root +"/"+dtl.getCombineStyleId()+"/"+dtl.getCombineColorId());
				File[] imageFile = FileUtil.filterFile(file, "JPG");
				List<String> imgs = new ArrayList<String>();
				if(CommonUtil.isNotBlank(imageFile)){
					for (File f : imageFile) {
						imgs.add("/images/style/" + dtl.getCombineStyleId()+"/"+dtl.getCombineColorId() +"/"+ f.getName() );
					}				
				}
	 			dtl.setImages(imgs);
			}
			Color color=CacheManager.getColorById(dtl.getColorId());
			Color color2=CacheManager.getColorById(dtl.getColorId());
			if(isWS){
				dtl.setColorId(dtl.getCombineColorId());
				dtl.setCombineColorId(null);
				System.out.println("jia:");
				if(CommonUtil.isNotBlank(color2)){
					dtl.setColorName(color2.getColorName());
				}
				 dtl.setStyleId(dtl.getCombineStyleId());
				 dtl.setCombineStyleId(null);
				if(CommonUtil.isNotBlank(s2)){
					dtl.setStyleName(s2.getStyleName());
					dtl.setPrice(s2.getPrice());
					System.out.println("P2:"+s2.getPrice());
				}
				 
			}else{
				System.out.println("jia2:");

				if(CommonUtil.isNotBlank(color)){
					dtl.setColorName(color.getColorName());
				}
				if(CommonUtil.isNotBlank(color2)){
					dtl.setCombineColorName(color2.getColorName());
				}
				if(CommonUtil.isNotBlank(s)){
					dtl.setStyleName(s.getStyleName());
				}
				if(CommonUtil.isNotBlank(s2)){
					dtl.setCombineStyleName(s2.getStyleName());
					dtl.setPrice(s2.getPrice());
					System.out.println("P2:"+s2.getPrice());

				}
			}

		}
		return list;
	}
	public static void removePic(String imageid, String rootPath) {
		// TODO Auto-generated method stub
		File path = new File(rootPath + imageid);

		if (path.exists()) {
			path.delete();
		}
	}

	/**
	 * 读取搭配信息
	 * */
	public static List<Suit> readSuitFile(FileInputStream inputStream)
			throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);// 创建对Excel工作薄文件的引用
		HSSFSheet sheet = workbook.getSheetAt(0);// 创建对工作表的引用，也可用workbook.getSheet("sheetName");

		int i = 1;// 从第二行开始读数据
		HSSFRow row = sheet.getRow(i);
		String id = null;
		String styleId = null;
		String colorId = null;
		List<Suit> suits = new ArrayList<>();
		Map<String,String> map=new HashMap<>();
		Date dt=new Date();
		try {
			id = row.getCell(0).getStringCellValue();
			styleId = (row.getCell(1).getStringCellValue()).replace(" ", "");
			colorId = (row.getCell(2).getStringCellValue()).replace(" ", "");
		} catch (java.lang.IllegalStateException e) {
			throw new Exception("第" + (i + 1) + "行，非文本类型");
		}

		while (!CommonUtil.isBlank(row)) {
			if (CommonUtil.isBlank(id) || CommonUtil.isBlank(colorId)
					|| CommonUtil.isBlank(styleId)) {
				throw new Exception("第" + (i + 1) + "行，信息不完整！");
			}
			Style style = CacheManager.getStyleById(styleId);
			Color color = CacheManager.getColorById(colorId);
			
			if (CommonUtil.isBlank(id) || CommonUtil.isBlank(color)
					|| CommonUtil.isBlank(style)) {
				throw new Exception("第" + (i + 1) + "行，信息错误！");
			}
			Suit suit = new Suit();
			SuitDtl dtl = new SuitDtl();
			dtl.setId(id+styleId+colorId);
			if(map.containsKey(dtl.getId())){
				System.out.println(dtl.getId()+":"+"第" + (i + 1) + "行重复！");
			}
			map.put(dtl.getId(), dtl.getId());
			List<SuitDtl> suitDtls = new ArrayList<>();
			suits.add(suit);
			dtl.setPrice(style.getPrice());
			dtl.setStyleId(styleId);
			dtl.setSuitCode(id);
			dtl.setColorId(colorId);
			suit.setSex(style.getClass6());
			suit.setUpdateDate(dt);
			suit.setCode(id);
			suit.setName(id);
			suit.setRemark("导入生成！");
			double totPrice = style.getPrice();
			suit.setPrice(totPrice);
			suit.setImageUrl("/images/suit/" + suit.getCode());

			suitDtls.add(dtl);
			suit.setList(suitDtls);

			boolean isEnd = false;
			int j = 3;
			while (!isEnd) {
				try {
					HSSFCell cell=row.getCell(j);
					++j;
					HSSFCell cellC=row.getCell(j);
					if(CommonUtil.isBlank(cellC)||CommonUtil.isBlank(cell)){
						isEnd=true;
						styleId = null;
						colorId = null;
						break;
					}
					styleId = cell.getStringCellValue();
					colorId = cellC.getStringCellValue();
				} catch (java.lang.IllegalStateException e) {
					styleId = null;
					colorId = null;
				} finally {
					if (CommonUtil.isBlank(colorId)
							|| CommonUtil.isBlank(styleId)) {
						isEnd = true;
					} else {
						Style styleNext = CacheManager.getStyleById(styleId);
						Color colorNext = CacheManager.getColorById(colorId);
						++j;
						if (CommonUtil.isBlank(styleNext)
								|| CommonUtil.isBlank(colorNext)) {
							throw new Exception("第" + (i + 1) + "行，信息错误！");
						} else {
							dtl = new SuitDtl();
							dtl.setId(id+styleId+colorId);
							if(map.containsKey(dtl.getId())){
								System.out.println(dtl.getId()+":"+"第" + (i + 1) + "行重复！");
							}
							map.put(dtl.getId(), dtl.getId());
							if(CommonUtil.isBlank(suit.getSex())){
								suit.setSex(styleNext.getClass6());
							}
							dtl.setPrice(style.getPrice());
							dtl.setStyleId(styleId);
							dtl.setSuitCode(id);
							dtl.setColorId(colorId);
							suitDtls.add(dtl);
						}
					}
				}
			}
	 
			i++;
			System.out.println("当前第" + i + "行");
			row = sheet.getRow(i);
			if (row != null) {
				if (!CommonUtil.isBlank(row)) {
					boolean is = false;
					try {
							id = row.getCell(0).getStringCellValue();
							styleId = row.getCell(1).getStringCellValue();
							colorId = row.getCell(2).getStringCellValue();
							is=true;
					} catch (java.lang.IllegalStateException e) {
						e.printStackTrace();
					} finally {
						if (!is) {
							throw new Exception("第" + (i + 1) + "行，非文本类型");
						}
					}

				} else {
					break;
				}
			}
		}
		return suits;
	}
	/**
	 * 读取搭配信息
	 * */
	public static List<StyleDescription> readStyleDescriptionFile(FileInputStream inputStream)
			throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);// 创建对Excel工作薄文件的引用
		HSSFSheet sheet = workbook.getSheetAt(0);// 创建对工作表的引用，也可用workbook.getSheet("sheetName");

		int i = 1;// 从第二行开始读数据
		HSSFRow row = sheet.getRow(i);
		String styleId = null;
		String remark=null;
		List<StyleDescription> styleDescriptions = new ArrayList<>();
		Map<String,String> map=new HashMap<>();
		Date dt=new Date();
		try {
			styleId = row.getCell(0).getStringCellValue();
			remark = row.getCell(1).getStringCellValue();
		} catch (java.lang.IllegalStateException e) {
			throw new Exception("第" + (i + 1) + "行，非文本类型");
		}

		while (!CommonUtil.isBlank(row)) {
			if (CommonUtil.isBlank(styleId)) {
				throw new Exception("第" + (i + 1) + "行，信息不完整！");
			}
			Style style = CacheManager.getStyleById(styleId);
			
			if ( CommonUtil.isBlank(style)) {
				throw new Exception("第" + (i + 1) + "行，信息错误！");
			}
			StyleDescription des=new StyleDescription();
			des.setDescription(remark);
			des.setStyleId(styleId);
			des.setUpdateDate(dt);
			if(map.containsKey(styleId)){
				
			}else{
				styleDescriptions.add(des);				
			}
			map.put(styleId, styleId);
			i++;
			System.out.println("当前第" + i + "行");
			row = sheet.getRow(i);
			if (row != null) {
				if (!CommonUtil.isBlank(row)) {
					boolean is = false;
					try {
						styleId = row.getCell(0).getStringCellValue();
						remark = row.getCell(1).getStringCellValue();
							is=true;
					} catch (java.lang.IllegalStateException e) {
						e.printStackTrace();
					} finally {
						if (!is) {
							throw new Exception("第" + (i + 1) + "行，非文本类型");
						}
					}

				} else {
					break;
				}
			}
		}
		return styleDescriptions;
	}
	// john
		public static List<StyleCombine> readStyleCollocation(
				FileInputStream inputStream) throws Exception {
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			HSSFSheet sheet = workbook.getSheetAt(0);
			List<StyleCombine> listStyleCombine = new ArrayList<StyleCombine>();
			Iterator<Row> iterator = sheet.iterator();
			Map<String,String> infoMap=new HashMap<>();
			int i = 0;
 			while (iterator.hasNext()) {
				Row row = iterator.next();
				if (i == 0) {
					i++;
					continue;
				}
				String styleId = row.getCell(0).getStringCellValue().trim();
				String colorId = row.getCell(1).getStringCellValue().trim();
				String styleId2 = row.getCell(2).getStringCellValue().trim();
				String colorId2 = row.getCell(3).getStringCellValue().trim();
				if (CommonUtil.isBlank(CacheManager.getStyleById(styleId))) {
					throw new Exception("第" + (i + 1) + "行" + "第1列款号不存在,请上传对应信息");
				}
				if (CommonUtil.isBlank(CacheManager.getStyleById(styleId2))) {
					throw new Exception("第" + (i + 1) + "行" + "第2列款号不存在,请上传对应信息");
				}
				StringBuilder id=new StringBuilder();
				id.append(styleId).append(colorId).append(styleId2).append(colorId2);
				if(!infoMap.containsKey(id.toString())){
					StyleCombine styleCombine = new StyleCombine();
					styleCombine.setId(id.toString());
					styleCombine.setStyleId(styleId);
					styleCombine.setColorId(colorId);
					styleCombine.setCombineStyleId(styleId2);
					styleCombine.setCombineColorId(colorId2);
					listStyleCombine.add(styleCombine);
					infoMap.put(styleCombine.getId(), styleCombine.getId());
				}
				StringBuilder id2=new StringBuilder();
				id2.append(styleId2).append(colorId2).append(styleId).append(colorId);
				if(!infoMap.containsKey(id2.toString())){
					StyleCombine styleCombine2 = new StyleCombine();
					styleCombine2.setId(id2.toString());
					styleCombine2.setStyleId(styleId2);
					styleCombine2.setColorId(colorId2);
					styleCombine2.setCombineStyleId(styleId);
					styleCombine2.setCombineColorId(colorId);
					listStyleCombine.add(styleCombine2);
					infoMap.put(styleCombine2.getId(), styleCombine2.getId());
				}
				i++;
			}
			return listStyleCombine;
		}

}
