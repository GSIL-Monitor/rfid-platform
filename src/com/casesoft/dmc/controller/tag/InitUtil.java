package com.casesoft.dmc.controller.tag;

import com.alibaba.fastjson.JSON;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.FileUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.vo.ITag;
import com.casesoft.dmc.core.vo.TagFactory;

import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.product.Size;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.sys.User;
import com.casesoft.dmc.model.tag.*;
import com.casesoft.dmc.model.task.BusinessDtl;
import com.casesoft.dmc.service.tag.InitService;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;

public class InitUtil {

	public static List<Epc> epcList = null;

	/**
	 * 转换绑定关系
	 */
	public static void covertToBindInfo(List<EpcBindBarcode> rows) {
		if (CommonUtil.isNotBlank(rows)) {
			for (EpcBindBarcode pt : rows) {
				Product p = CacheManager.getProductByCode(pt.getCode());
				if (CommonUtil.isBlank(p)) {
					for (Map.Entry<String, Product> entry : CacheManager.getProductMap().entrySet()) {

						if (CommonUtil.isNotBlank(pt.getCode())) {
							if (CommonUtil.isNotBlank(entry.getValue().getBarcode()) && pt.getCode().equals(entry.getValue().getBarcode())) {
								p = entry.getValue();
								break;
							}
						}

					}
				}
				pt.setEpc(pt.getEpc().toUpperCase());
				if (CommonUtil.isBlank(p)) {
					continue;
				}
				pt.setStyleId(p.getStyleId());
				pt.setStyleName(p.getStyleName());
				pt.setColorId(p.getColorId());
				pt.setColorName(p.getColorName());
				pt.setSizeId(p.getSizeId());
				pt.setSizeName(p.getSizeName());
			}
		}
	}

	public static Init processExcel2(String billNo,
									 FileInputStream inputStream, InitService epcService, User user)
			throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);// 创建对Excel工作薄文件的引用
		HSSFSheet sheet = workbook.getSheetAt(0);// 创建对工作表的引用，也可用workbook.getSheet("sheetName");

		Init master = new Init();
		String taskId = billNo;
		master.setBillNo(taskId);

		int totQty = 0;

		int i = 1;// 从第二行开始读数据
		HSSFRow row = sheet.getRow(i);
		String cell0 = processData(row, 0), cell1 = processData(row, 1);// ,
		// cell2
		// =
		// processData(row,
		// 2);

		// 每行中的尺寸情况
		// String[] sizeInfo =
		// PropertyUtil.getValue("produce_epc_size_config").split(",");
		String[] sizeInfo = TagFactory
				.getTag(PropertyUtil.getValue("tag_name")).getSizeConfig();// wing
		// 2014-04-01
		int sizeCount = sizeInfo.length;
		Map<String, InitDtl> details = new HashMap<String, InitDtl>();
		while (!CommonUtil.isBlank(row)
				&& (CommonUtil.isLetterOrNum(cell0) && CommonUtil
				.isLetterOrNum(cell1))) {

			for (int size = 0; size < sizeCount; size++) {
				String cell2 = sizeInfo[size];
				String sku = cell0 + cell1 + cell2;
				if (details.containsKey(sku)) {
					throw new Exception("数据里存在重复的SKU");
				}
				int qty = (int) row.getCell(2 + size).getNumericCellValue();// 数量
				if (qty == 0)
					continue;// qty=0，sku不参与打印

				InitDtl detail = new InitDtl();
				// detail.setId(new GuidCreator().toString());
				detail.setId(taskId + "-" + sku);
				detail.setStyleId(cell0);
				detail.setColorId(cell1);
				detail.setSizeId(cell2);
				detail.setSku(cell0 + cell1 + cell2);
				detail.setStartNum(epcService.findMaxNoBySkuNo(detail.getSku()) + 1);
				detail.setEndNum(epcService.findMaxNoBySkuNo(detail.getSku())
						+ qty);
				detail.setQty(qty);

				detail.setOwnerId(user.getOwnerId());

				detail.setStatus(0);// 0:已导入
				totQty = totQty + qty;
				detail.setBillNo(taskId);
				details.put(sku, detail);
			}
			i++;
			row = sheet.getRow(i);
			if (row != null)
				if (row.getCell(0) != null
						&& !CommonUtil.isBlank(row.getCell(0)
						.getStringCellValue())) {
					cell0 = processData(row, 0);
					cell1 = processData(row, 1);
				} else {
					break;
				}
		}
		master.setTotEpc(totQty);
		master.setDtlList(new ArrayList<InitDtl>(details.values()));
		master.setOwnerId(user.getOwnerId());
		master.setHostId(user.getId());
		master.setTotSku(details.size());
		master.setBillDate(new Date());
		master.setStatus(0);// 已导入

		return master;
	}

	public static Init processExcel3(String billNo,
									 FileInputStream inputStream, InitService epcService, User user)
			throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);// 创建对Excel工作薄文件的引用
		HSSFSheet sheet = workbook.getSheetAt(0);// 创建对工作表的引用，也可用workbook.getSheet("sheetName");

		Init master = new Init();
		String taskId = billNo;
		master.setBillNo(taskId);

		int totQty = 0;

		int i = 1;// 从第二行开始读数据
		HSSFRow row = sheet.getRow(i);
		String cell0, cell1, cell2 = "";

		cell0 = row.getCell(0).getStringCellValue().trim();
		cell1 = row.getCell(1).getStringCellValue().trim();
		cell2 = row.getCell(2).getStringCellValue().trim();

		if (CommonUtil.isBlank(cell2)) {
			throw new Exception("第" + (i + 1) + "行尺寸分类编号不存在");
		} else {

			Map<String, InitDtl> details = new HashMap<String, InitDtl>();
			while (!CommonUtil.isBlank(row)
					&& (CommonUtil.isLetterOrNum(cell0) && CommonUtil
					.isLetterOrNum(cell1))
					&& CommonUtil.isNotBlank(cell2)) {
				List<Size> sizeList = CacheManager.getSizeSortById(cell2)
						.getSizeList();
				for (int j = 0; j < sizeList.size(); j++) {
					String cell3 = sizeList.get(j).getSizeId();
					String sku = cell0 + cell1 + cell3;
					if (CommonUtil.isBlank(row.getCell(3 + j))) {
						continue;
					}
					int qty = (int) row.getCell(3 + j).getNumericCellValue();// 数量
					if (qty == 0)
						continue;// qty=0，sku不参与打印
					if (CommonUtil.isBlank(CacheManager.getProductByCode(sku))) {
						throw new Exception("第" + (i + 1) + "行" + (j + 4)
								+ "列商品信息不存在");
					}
					if (details.containsKey(sku)) {
						throw new Exception("第" + (i + 1) + "行数据里存在重复的SKU");
					}

					InitDtl detail = new InitDtl();
					// detail.setId(new GuidCreator().toString());
					detail.setId(taskId + "-" + sku);
					detail.setStyleId(cell0);
					detail.setColorId(cell1);
					detail.setSizeId(cell3);
					detail.setSku(sku);
					detail.setStartNum(epcService.findMaxNoBySkuNo(detail
							.getSku()) + 1);
					detail.setEndNum(epcService.findMaxNoBySkuNo(detail
							.getSku()) + qty);
					detail.setQty(qty);

					detail.setOwnerId(user.getOwnerId());

					detail.setStatus(0);// 0:已导入
					totQty = totQty + qty;
					detail.setBillNo(taskId);
					details.put(sku, detail);
				}
				i++;
				row = sheet.getRow(i);
				if (row != null)
					if (CommonUtil.isNotBlank(row.getCell(0))
							&& CommonUtil.isNotBlank(row.getCell(0)
							.getStringCellValue())) {
						cell0 = row.getCell(0).getStringCellValue().trim();
						cell1 = row.getCell(1).getStringCellValue().trim();
						cell2 = row.getCell(2).getStringCellValue().trim();
					} else {
						break;
					}
			}
			master.setTotEpc(totQty);
			master.setDtlList(new ArrayList<InitDtl>(details.values()));
			master.setOwnerId(user.getOwnerId());
			master.setHostId(user.getId());
			master.setTotSku(details.size());
			master.setBillDate(new Date());
			master.setStatus(0);// 已导入

		}

		return master;
	}

	public static Init processExcel4(String billNo, InputStream inputStream,
									 InitService epcService, User user) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);// 创建对Excel工作薄文件的引用
		HSSFSheet sheet = workbook.getSheetAt(0);// 创建对工作表的引用，也可用workbook.getSheet("sheetName");

		Init master = new Init();
		String taskId = billNo;
		master.setBillNo(taskId);

		int totQty = 0;

		int i = 1;// 从第二行开始读数据
		HSSFRow row = sheet.getRow(i);
		if (CommonUtil.isBlank(row)) {
			throw new Exception("无有效数据");
		}
		String cell0 = row.getCell(0).getStringCellValue().trim();
		int qty = (int) row.getCell(1).getNumericCellValue();
		// String makeOrderNo = row.getCell(4).getStringCellValue();//制单号
		Map<String, InitDtl> details = new HashMap<String, InitDtl>();
		while (!CommonUtil.isBlank(row) && (CommonUtil.isNotBlank(cell0))) {
			if (details.containsKey(cell0)) {
				throw new Exception("第" + (i + 1) + "行数据里存在重复的条码");
			}

			if (qty != 0) {// qty=0，sku不参与打印

				InitDtl detail = new InitDtl();
				// detail.setId(new GuidCreator().toString());
				detail.setId(taskId + "-" + cell0);

				Product p = CacheManager.getProductByCode(cell0);
				// 增加判断款色码的规则
				if (CommonUtil.isBlank(p)) {
					throw new Exception("第" + (i + 1) + "商品信息不存在");
				}

				detail.setStyleId(p.getStyleId());
				detail.setColorId(p.getColorId());
				detail.setSizeId(p.getSizeId());
				detail.setSku(p.getCode());
				detail.setStartNum(epcService.findMaxNoBySkuNo(detail.getSku()) + 1);
				detail.setEndNum(epcService.findMaxNoBySkuNo(detail.getSku())
						+ qty);
				detail.setQty(qty);

				detail.setOwnerId(user.getOwnerId());

				detail.setStatus(0);// 0:已导入
				totQty = totQty + qty;
				detail.setBillNo(taskId);
				details.put(p.getCode(), detail);
			}
			i++;
			System.out.println("print:" + i);
			row = sheet.getRow(i);
			if (row != null) {
				cell0 = row.getCell(0).getStringCellValue().trim();
				qty = (int) row.getCell(1).getNumericCellValue();
			}

		}
		master.setTotEpc(totQty);
		master.setDtlList(new ArrayList<InitDtl>(details.values()));
		master.setOwnerId(user.getOwnerId());
		master.setHostId(user.getId());
		master.setTotSku(i - 1);
		master.setBillDate(new Date());
		master.setStatus(0);// 已导入

		return master;
	}

	public static Init processExcel2(FileInputStream inputStream,
									 InitService epcService, User user) throws Exception {
		String taskId = Constant.Bill.Tag_Init_Prefix
				+ CommonUtil.getDateString(new Date(), "yyMMdd")
				+ CommonUtil.randomNumeric(3);

		return processExcel2(taskId, inputStream, epcService, user);
	}

	public static Init processExcel(InputStream inputStream,
									InitService epcService, User user) throws Exception {
		String taskId = Constant.Bill.Tag_Init_Prefix
				+ CommonUtil.getDateString(new Date(), "yyMMdd")
				+ CommonUtil.randomNumeric(3);
		return processExcel(taskId, inputStream, epcService, user);
	}

	/**
	 * @param barcode
	 * @param qty
	 * @param deviceId
	 * @param epcService
	 * @param epcs       根据条码生成epc
	 * @throws Exception
	 */
	public static boolean excuteBarcodeEpc(String barcode, int qty,
										   String deviceId, InitService epcService, List<String> epcs) throws Exception {
		Product product = CacheManager.getProductByCode(barcode);
		if (CommonUtil.isBlank(product)) {
			List<Product> list = new ArrayList(CacheManager.getProductMap()
					.values());
			if (CommonUtil.isNotBlank(list)) {
				for (Product p : list) {
					if (CommonUtil.isNotBlank(p.getBarcode())
							&& barcode.equals(p.getBarcode())) {
						product = p;
						break;
					}
				}
				if (CommonUtil.isBlank(product)) {
					return false;
				}
			} else {
				return false;
			}

		}
		Init master = new Init();
		master.setFileName("条码生成");
		master.setRemark("条码生成");
		master.setBillNo(deviceId + new Date().getTime());
		InitDtl detail = new InitDtl();
		detail.setId(master.getBillNo() + "-" + barcode);
		detail.setStyleId(product.getStyleId());
		detail.setColorId(product.getColorId());
		detail.setSizeId(product.getSizeId());
		detail.setSku(product.getCode());
		detail.setStartNum(epcService.findMaxNoBySkuNo(detail.getSku()) + 1);
		detail.setEndNum(epcService.findMaxNoBySkuNo(detail.getSku()) + qty);
		detail.setQty(qty);
		detail.setOwnerId(CacheManager.getDeviceByCode(deviceId).getOwnerId());
		detail.setStatus(1);// 0:已导入
		detail.setBillNo(master.getBillNo());
		////////////////
		int startNo = (int) detail.getStartNum();
		List<Epc> listEpcs = new ArrayList<>();
		for (int i = 1; i <= detail.getQty(); i++) {

			String className = PropertyUtil.getValue("tag_name");
			ITag tag = TagFactory.getTag(className);
			tag.setStyleId(detail.getStyleId());
			tag.setColorId(detail.getColorId());
			tag.setSizeId(detail.getSizeId());
			tag.setSku(detail.getSku());
			String uniqueCode = tag.getUniqueCode(startNo, i);
			String epc = tag.getEpc();
			String secretEpc = tag.getSecretEpc();
			String code2 = PropertyUtil.getValue("webservice") + uniqueCode;
			Epc epcObj = initEpcObj(detail, uniqueCode, secretEpc, code2);
			epcs.add(secretEpc);
			listEpcs.add(epcObj);
		}
		//////////
		master.setTotEpc(qty);
		List<InitDtl> list = new ArrayList<InitDtl>();
		list.add(detail);
		master.setDtlList(list);
		master.setOwnerId(CacheManager.getDeviceByCode(deviceId).getOwnerId());
		master.setHostId("admin");
		master.setTotSku(1);
		master.setBillDate(new Date());
		master.setStatus(1);// 已导入
		epcService.save(listEpcs, master);
		return true;
	}

	public static List<Epc> excuteBarcodeEpcList(String barcode, int qty, String deviceId, InitService epcService, List<String> epcs) throws Exception {
		Product product = CacheManager.getProductByCode(barcode);
		if (CommonUtil.isBlank(product)) {
			List<Product> list = new ArrayList(CacheManager.getProductMap()
					.values());
			if (CommonUtil.isNotBlank(list)) {
				for (Product p : list) {
					if (CommonUtil.isNotBlank(p.getBarcode())
							&& barcode.equals(p.getBarcode())) {
						product = p;
						break;
					}
				}
				if (CommonUtil.isBlank(product)) {
					return null;
				}
			} else {
				return null;
			}

		}
		Init master = new Init();
		master.setFileName("条码生成");
		master.setRemark("条码生成");
		master.setBillNo(deviceId + new Date().getTime());
		InitDtl detail = new InitDtl();
		detail.setId(master.getBillNo() + "-" + barcode);
		detail.setStyleId(product.getStyleId());
		detail.setColorId(product.getColorId());
		detail.setSizeId(product.getSizeId());
		detail.setSku(product.getCode());
		detail.setStartNum(epcService.findMaxNoBySkuNo(detail.getSku()) + 1);
		detail.setEndNum(epcService.findMaxNoBySkuNo(detail.getSku()) + qty);
		detail.setQty(qty);
		detail.setOwnerId(CacheManager.getDeviceByCode(deviceId).getOwnerId());
		detail.setStatus(1);// 0:已导入
		detail.setBillNo(master.getBillNo());
		////////////////
		int startNo = (int) detail.getStartNum();
		List<Epc> listEpcs = new ArrayList<>();
		for (int i = 1; i <= detail.getQty(); i++) {

			String className = PropertyUtil.getValue("tag_name");
			ITag tag = TagFactory.getTag(className);
			tag.setStyleId(detail.getStyleId());
			tag.setColorId(detail.getColorId());
			tag.setSizeId(detail.getSizeId());
			tag.setSku(detail.getSku());
			String uniqueCode = tag.getUniqueCode(startNo, i);
			String epc = tag.getEpc();
			String secretEpc = tag.getSecretEpc();
			String code2 = PropertyUtil.getValue("webservice") + uniqueCode;
			Epc epcObj = initEpcObj(detail, uniqueCode, secretEpc, code2);
			epcObj.setStyleName(product.getStyleName());
			epcObj.setColorName(product.getColorName());
			epcObj.setSizeName(product.getSizeName());
			epcs.add(secretEpc);
			listEpcs.add(epcObj);
		}
		//////////
		master.setTotEpc(qty);
		List<InitDtl> list = new ArrayList<InitDtl>();
		list.add(detail);
		master.setDtlList(list);
		master.setOwnerId(CacheManager.getDeviceByCode(deviceId).getOwnerId());
		master.setHostId("admin");
		master.setTotSku(1);
		master.setBillDate(new Date());
		master.setStatus(1);// 已导入
		epcService.save(listEpcs, master);
		return listEpcs;

	}

	public static Init processExcel(String billNo, InputStream inputStream,
									InitService epcService, User user) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);// 创建对Excel工作薄文件的引用
		HSSFSheet sheet = workbook.getSheetAt(0);// 创建对工作表的引用，也可用workbook.getSheet("sheetName");

		Init master = new Init();
		// String taskId = Constant.Bill.Tag_Init_Prefix +
		// CommonUtil.getDateString(new Date(),
		// "yyMMdd")
		// + CommonUtil.randomNumeric(3);
		String taskId = billNo;
		master.setBillNo(taskId);

		int totQty = 0;

		int i = 1;// 从第二行开始读数据
		HSSFRow row = sheet.getRow(i);
		String cell0 = getStringFormCell(row.getCell(0)).replaceAll(" ","");
		String cell1 = getStringFormCell(row.getCell(1)).split("\\.")[0].replaceAll(" ","");
		String cell2 = getStringFormCell(row.getCell(2)).replaceAll(" ","");
		int qty = 0;
		qty = (int) row.getCell(3).getNumericCellValue();// 数量
		// String makeOrderNo = row.getCell(4).getStringCellValue();//制单号
		Map<String, InitDtl> details = new HashMap<String, InitDtl>();
		while (!CommonUtil.isBlank(row)
				&& (CommonUtil.isNotBlank(cell0)
				&& CommonUtil.isNotBlank(cell1)
				&& CommonUtil.isNotBlank(cell2) && CommonUtil
				.isNotBlank(qty))) {
			String sku = cell0 + cell1 + cell2;
			if (details.containsKey(sku)){
				throw new Exception("数据里存在重复的SKU");
			}


			if (qty != 0) {// qty=0，sku不参与打印

				InitDtl detail = new InitDtl();
				// detail.setId(new GuidCreator().toString());
				detail.setId(taskId + "-" + sku);

				// 增加判断款色码的规则
				/*
				 * ITag tag = TagFactory.getCurrentTag();
				 * if(!tag.matcherStyleId(cell0)) { throw new
				 * Exception("款号["+cell0+"]不符合规则！"); }
				 * if(!tag.matcherColorId(cell1)) { throw new
				 * Exception("色号["+cell1+"]不符合规则！"); }
				 * if(!tag.matcherSizeId(cell2)) { throw new
				 * Exception("尺码["+cell2+"]不符合规则！"); }
				 */
				if (CommonUtil.isBlank(CacheManager.getProductByCode(sku))) {
					throw new Exception("商品[" + sku + "]系统不能识别，请上传商品资料后，再重新操作");
				}
				if (CacheManager.getStyleById(cell0) == null) {
					throw new Exception("款号[" + cell0
							+ "]系统不能识别，请上传商品资料后，再重新操作");
				}
				if (CacheManager.getColorById((cell1)) == null) {
					throw new Exception("色号[" + cell1
							+ "]系统不能识别，请上传商品资料后，再重新操作");
				}

				if (CacheManager.getSizeByNo((cell2)) == null) {
					throw new Exception("尺码[" + cell2
							+ "]系统不能识别，请上传商品资料后，再重新操作");
				}

				detail.setStyleId(cell0);
				detail.setColorId(cell1);
				detail.setSizeId(cell2);
				detail.setSku(cell0 + cell1 + cell2);
				detail.setStartNum(epcService.findMaxNoBySkuNo(detail.getSku()) + 1);
				detail.setEndNum(epcService.findMaxNoBySkuNo(detail.getSku())
						+ qty);
				detail.setQty(qty);

				detail.setOwnerId(user.getOwnerId());

				detail.setStatus(0);// 0:已导入
				totQty = totQty + qty;
				detail.setBillNo(taskId);
				details.put(sku, detail);
			}

			i++;
			row = sheet.getRow(i);
			if (row != null)
				if (row.getCell(0) != null
						&& !CommonUtil.isBlank(row.getCell(0)
						.getStringCellValue())) {
					cell0 = getStringFormCell(row.getCell(0)).replaceAll(" ","");
					cell1 = getStringFormCell(row.getCell(1)).split("\\.")[0].replaceAll(" ","");
					cell2 = getStringFormCell(row.getCell(2)).replaceAll(" ","");
					qty = (int) row.getCell(3).getNumericCellValue();// 数量
				} else {
					break;
				}
		}
		master.setTotEpc(totQty);
		master.setDtlList(new ArrayList<InitDtl>(details.values()));
		master.setOwnerId(user.getOwnerId());
		master.setHostId(user.getId());
		master.setTotSku(i - 1);
		master.setBillDate(new Date());
		master.setStatus(0);// 已导入

		return master;
	}

	/**
	 * 集成ams系统时，将private改为public 2014-03-16 wing
	 *
	 * @param row
	 * @param colIndex
	 * @return
	 * @throws Exception
	 */
	public static String processData(HSSFRow row, int colIndex)
			throws Exception {
		String str = "";
		try {
			str = row.getCell(colIndex).getStringCellValue().trim();// 款
		} catch (Exception e) {
			int temp = (int) row.getCell(colIndex).getNumericCellValue();
			// str = ""+temp;//如果是颜色，尺码编码时，可能需要加0
			switch (colIndex) {
				case 0:
					// str = CommonUtil.convertToStyle(temp); //wing 2014-04-01
					str = TagFactory.getTag(PropertyUtil.getValue("tag_name"))
							.convertToStyle(temp);
					break;
				case 1:
					// str = CommonUtil.convertToColor(temp);
					str = TagFactory.getTag(PropertyUtil.getValue("tag_name"))
							.convertToColor(temp);
					break;
				case 2:
					// str = CommonUtil.convertToSize(temp);
					str = TagFactory.getTag(PropertyUtil.getValue("tag_name"))
							.convertToSize(temp);
					break;
			}
		}
		return str;
	}

	private static String getStringFormCell(HSSFCell cell) {
		try {
			return cell.getStringCellValue();
		} catch (java.lang.IllegalStateException ex) {
			return String.valueOf(cell.getNumericCellValue());
		}
	}

	public static void writeTextToFile(InitDtl detail, String fileFolder, List<Epc> epcs,
									   boolean isRfid) throws Exception {
		String str = writeTextToFile(detail, isRfid, epcs);
		String fileName = fileFolder + detail.getSku() + "_" + detail.getQty()
				+ "_" + CommonUtil.getDateString(new Date(), "yyyyMMdd")
				+ ".txt";
		FileUtil.writeStringToFile(str, fileName);
	}

	public static void writeTextFile(InitDtl detail, String fileFolder,
									 boolean isRfid) throws Exception {
		String str = writeTextFile(detail, isRfid);
		String fileName = fileFolder + detail.getSku() + "_" + detail.getQty()
				+ "_" + CommonUtil.getDateString(new Date(), "yyyyMMdd")
				+ ".txt";
		FileUtil.writeStringToFile(str, fileName);
	}

	public static void writeTextToFile(InitDtl detail, String fileFolder,
									   boolean isRfid, List<Epc> epcs) throws Exception {
		String str = writeTextToFile(detail, isRfid, epcs);
		String fileName = fileFolder + detail.getSku() + "_" + detail.getQty()
				+ "_" + CommonUtil.getDateString(new Date(), "yyyyMMdd")
				+ ".txt";
		FileUtil.writeStringToFile(str, fileName);
	}

	public static String formatStr(String str, int length) {

		if (str == null) {
			return null;
		}
		int strLen = str.length();
		if (strLen == length) {
			return str;
		} else if (strLen < length) {
			int temp = length - strLen;
			String tem = "";
			for (int i = 0; i < temp; i++) {
				tem = tem + " ";
			}
			return str + tem;
		} else {
			return str.substring(0, length);
		}

	}

	public static String writeZdhTextFile(InitDtl detail, boolean isRfid)
			throws Exception {
		String title = "";
//		if (isRfid)
//			title = "款式,款名,色码,颜色,尺寸,尺码,吊牌码,芯片码,二维码,价格,系列 ,条码 ,ENA,库位码\r\n";
//		else
//			title = "款式,款名,色码,颜色,尺寸,尺码,吊牌码,二维码,价格,系列,条码 ,ENA,库位码\r\n";

		StringBuffer sb = new StringBuffer(title);
		int startNo = (int) detail.getStartNum();
		for (int i = 1; i <= detail.getQty(); i++) {

			String className = PropertyUtil.getValue("tag_name");
			ITag tag = TagFactory.getTag(className);
			tag.setStyleId(detail.getStyleId());
			tag.setColorId(detail.getColorId());
			tag.setSizeId(detail.getSizeId());
			tag.setSku(detail.getSku());
			String uniqueCode = tag.getUniqueCode(startNo, i);
			String epc = tag.getEpc();
			String secretEpc = tag.getSecretEpc();
			String code2 = PropertyUtil.getValue("webservice") + uniqueCode;

			sb.append(detail.getStyleId());
			sb.append(" ");
			String styleName = CacheManager.getStyleNameById(detail.getStyleId());
			sb.append(formatStr(styleName, 70));
			sb.append(" ");
			sb.append(detail.getColorId());
			sb.append(" ");
			sb.append(formatStr(CacheManager.getColorNameById(detail.getColorId()), 30));
			sb.append(" ");
			sb.append(detail.getSizeId());
			sb.append(" ");
			sb.append(formatStr(CacheManager.getSizeNameById(detail.getSizeId()), 20));
			sb.append(" ");

			// sb.append(convertToASCII(epc,0));
			if (isRfid) {
//				sb.append(uniqueCode);
//				sb.append(",");
				sb.append(secretEpc);
			} else
				sb.append(uniqueCode);

			sb.append(" ");
//			sb.append(code2);

			sb.append(detail.getSku());
			sb.append(" ");
			Style style = CacheManager.getStyleById(detail.getStyleId());
			if (style == null) {
				sb.append("");
				sb.append(" ");
				sb.append("");
			} else {
				DecimalFormat df = null;
				if (CommonUtil.isNotBlank(tag.getTypeName())
						&& tag.getTypeName().equals("Playlounge")) {
					df = new DecimalFormat("#####0.00");// 设置价格显示两位小数位
				} else {
					df = new DecimalFormat("#####0");// 设置价格显示无小数位
				}
				sb.append(df.format(style.getPrice()));
				sb.append(" ");
			}
			Product p = CacheManager.getProductByCode(detail.getSku());
			if (CommonUtil.isNotBlank(p)) {
				String class7 = "";
				if (CommonUtil.isNotBlank(style) && CommonUtil.isNotBlank(style.getClass7())) {
					class7 = style.getClass7();
				}
				sb.append(" ").append(p.getBarcode()).append(" ").append(p.getEan())
						.append(" ").append(class7);
			} else {
				sb.append(",").append("").append(",").append("").append(",").append("");
			}
			sb.append("\r\n");
			Epc epcObj = initEpcObj(detail, uniqueCode, secretEpc, code2);
			epcList.add(epcObj);

		}
		return sb.toString();
	}

	public static void writeZdhTextFile(InitDtl detail, String fileFolder,
										boolean isRfid) throws Exception {
		String str = writeZdhTextFile(detail, isRfid);
		String fileName = fileFolder + detail.getSku() + "_" + detail.getQty()
				+ "_" + CommonUtil.getDateString(new Date(), "yyyyMMdd")
				+ ".txt";
		FileUtil.writeStringToFile(str, fileName);
	}

	public static File writeZdhTextFile(List<InitDtl> details, List<Epc> epcs,
										boolean isRfid) throws Exception {
		epcList = new ArrayList<Epc>();
		String taskId = details.get(0).getBillNo();

		String sTime = CommonUtil.getDateString(new Date(), "yyyyMMddHHmmss");
		String path = Constant.Folder.Epc_Init_File_Folder + sTime + File.separator;// 创建目录
		new File(path).mkdirs();
		for (InitDtl detail : details) {
			writeZdhTextFile(detail, path, isRfid);
		}
		// 压缩文件夹下所有文件
		String zipFileName = Constant.Folder.Epc_Init_Zip_File_Folder + taskId
				+ "_" + sTime + ".zip";
		FileUtil.zip(Constant.Folder.Epc_Init_File_Folder + sTime, zipFileName);
		return new File(zipFileName);
	}

	//first
	public static File writeTextFile(List<InitDtl> details, List<Epc> epcs,
									 boolean isRfid) throws Exception {
		epcList = new ArrayList<Epc>();
		String taskId = details.get(0).getBillNo();

		String sTime = CommonUtil.getDateString(new Date(), "yyyyMMddHHmmss");
		String path = Constant.Folder.Epc_Init_File_Folder + sTime + File.separator;// 创建目录
		new File(path).mkdirs();
		if (CommonUtil.isBlank(epcs)) {
			for (InitDtl detail : details) {
				writeTextFile(detail, path, isRfid);
			}
		} else {
			for (InitDtl detail : details) {
				writeTextToFile(detail, path, isRfid, epcs);
			}
		}
		// 压缩文件夹下所有文件
		String zipFileName = Constant.Folder.Epc_Init_Zip_File_Folder + taskId
				+ "_" + sTime + ".zip";
		FileUtil.zip(Constant.Folder.Epc_Init_File_Folder + sTime, zipFileName);
		return new File(zipFileName);
	}

	public static File writeTextFile(List<InitDtl> details, boolean isRfid)
			throws Exception {
		epcList = new ArrayList<Epc>();
		String taskId = details.get(0).getBillNo();

		String sTime = CommonUtil.getDateString(new Date(), "yyyyMMddHHmmss");
		String path = Constant.Folder.Epc_Init_File_Folder + sTime + "\\";// 创建目录
		new File(path).mkdirs();
		String title = "";
		if (isRfid)
			title = "款式,款名,色码,颜色,尺寸,尺码,吊牌码,芯片码,二维码,价格,系列,条码 ,ENA,执行标准,成分  \r\n";
		else
			title = "款式,款名,色码,颜色,尺寸,尺码,吊牌码,二维码,价格,系列 ,条码 ,ENA,执行标准,成分 \r\n";

		StringBuffer sb = new StringBuffer(title);

		for (InitDtl detail : details) {
			String str = writeTextFile3(detail, isRfid);
			sb.append(str);
		}
		String fileName = path + taskId + "_" + sTime + ".txt";
		FileUtil.writeStringToFile(sb.toString(), fileName);
		// 压缩文件夹下所有文件
		String zipFileName = Constant.Folder.Epc_Init_Zip_File_Folder + taskId
				+ "_" + sTime + ".zip";
		FileUtil.zip(Constant.Folder.Epc_Init_File_Folder + sTime, zipFileName);
		return new File(zipFileName);
	}

	public static String writeTextFile(InitDtl detail, boolean isRfid)
			throws Exception {
		String title = "";
//		String isCode = "";
//		if(CommonUtil.isNotBlank(detail.getBillNo()))
//			isCode="唯一码,";
		if (isRfid)
			title = "款式,款名,色码,颜色,尺寸,尺码,吊牌码,芯片码,二维码,价格,系列 ,条码 ,ENA,执行标准,成分\r\n";
		else
			title = "款式,款名,色码,颜色,尺寸,尺码,吊牌码,二维码,价格,系列,条码 ,ENA,执行标准,成分\r\n";

		StringBuffer sb = new StringBuffer(title);
		int startNo = (int) detail.getStartNum();
		for (int i = 1; i <= detail.getQty(); i++) {
			// int seriaLength = Constant.Length.Epc_Length -
			// detail.getSku().length();
			// String epc =
			// detail.getSku()+CommonUtil.produceIntToString(startNo+i-1,seriaLength);
			// String code2 = PropertyUtil.getValue("webservice")+epc;
			// String uniqueCode = EpcSecretUtil.getUniqueCode(detail.getSku(),
			// startNo, i);
			// String epc = EpcSecretUtil.convertUniqueCodeToEpc2(uniqueCode);//
			// 待加密的EPC为转码之后的唯一码+“补0”
			// String secretEpc = EpcSecretUtil.encodeEpc(epc);// encode(epc);
			// String code2 = PropertyUtil.getValue("webservice") + uniqueCode;

			String className = PropertyUtil.getValue("tag_name");
			ITag tag = TagFactory.getTag(className);
			tag.setStyleId(detail.getStyleId());
			tag.setColorId(detail.getColorId());
			tag.setSizeId(detail.getSizeId());
			tag.setSku(detail.getSku());
			String uniqueCode = tag.getUniqueCode(startNo, i);
			String epc = tag.getEpc();
			String secretEpc = tag.getSecretEpc();
			String code2 = PropertyUtil.getValue("webservice") + uniqueCode;

			sb.append(detail.getStyleId());
			sb.append(",");
			sb.append(CacheManager.getStyleNameById(detail.getStyleId()));
			sb.append(",");
			sb.append(detail.getColorId());
			sb.append(",");
			sb.append(CacheManager.getColorNameById(detail.getColorId()));
			sb.append(",");
			sb.append(detail.getSizeId());
			sb.append(",");
			sb.append(CacheManager.getSizeNameById(detail.getSizeId()));
			sb.append(",");

			// sb.append(convertToASCII(epc,0));
			if (isRfid) {
				sb.append(uniqueCode);
				sb.append(",");
				sb.append(secretEpc);
			} else
				sb.append(uniqueCode);

			sb.append(",");
			sb.append(code2);

			sb.append(",");
			Style style = CacheManager.getStyleById(detail.getStyleId());
			if (style == null) {
				sb.append("");
				sb.append(",");
				sb.append("");
			} else {
				DecimalFormat df = null;
				if (CommonUtil.isNotBlank(tag.getTypeName())
						&& tag.getTypeName().equals("Playlounge")) {
					df = new DecimalFormat("#####0.00");// 设置价格显示两位小数位
				} else {
					df = new DecimalFormat("#####0");// 设置价格显示无小数位
				}
				sb.append(df.format(style.getPrice()));
				sb.append(",");
				String class9 = "";
				if(CommonUtil.isNotBlank(style.getClass9())){
					class9 = CacheManager.getPropertyKey("C9-"+style.getClass9()).getName();
				}
				sb.append(class9);
			}
			Product p = CacheManager.getProductByCode(detail.getSku());
			if (CommonUtil.isNotBlank(p)) {
				String class7 = "";
				if (CommonUtil.isNotBlank(style) && CommonUtil.isNotBlank(style.getClass7())) {
					class7 = CacheManager.getPropertyKey("C7-"+style.getClass7()).getName();
				}

				sb.append(",").append(p.getBarcode()).append(",").append(p.getEan())
						.append(",").append(class7).append(",").append(p.getRemark());
			} else {
				sb.append(",").append("").append(",").append("").append(",").append("").append(",").append("");
			}
			sb.append("\r\n");
			Epc epcObj = initEpcObj(detail, uniqueCode, secretEpc, code2);
			epcList.add(epcObj);

		}
		return sb.toString();
	}

	public static String writeTextToFile(InitDtl detail, Boolean isRfid, List<Epc> epcs) throws Exception {
		String title = "";
		if (isRfid)
			title = "款式,款名,色码,颜色,尺寸,尺码,吊牌码,芯片码,二维码,价格,系列 ,条码 ,ENA,成分,执行标准\r\n";
		else
			title = "款式,款名,色码,颜色,尺寸,尺码,吊牌码,二维码,价格,系列,条码 ,ENA,成分,执行标准\r\n";

		StringBuffer sb = new StringBuffer(title);

			for (Epc epc : epcs) {
				if (epc.getSku().equals(detail.getSku())) {
						String className = PropertyUtil.getValue("tag_name");
						ITag tag = TagFactory.getTag(className);
						tag.setStyleId(detail.getStyleId());
						tag.setColorId(detail.getColorId());
						tag.setSizeId(detail.getSizeId());
						tag.setSku(detail.getSku());
						tag.setUniqueCode(epc.getCode());
						String uniqueCode = epc.getCode();
						String secretEpc=epc.getEpc();
						String code2 = epc.getDimension();
						sb.append(detail.getStyleId());
						sb.append(",");
						sb.append(CacheManager.getStyleNameById(detail.getStyleId()));
						sb.append(",");
						sb.append(detail.getColorId());
						sb.append(",");
						sb.append(CacheManager.getColorNameById(detail.getColorId()));
						sb.append(",");
						sb.append(detail.getSizeId());
						sb.append(",");
						sb.append(CacheManager.getSizeNameById(detail.getSizeId()));
						sb.append(",");

						if (isRfid) {
							sb.append(uniqueCode);
							sb.append(",");
							sb.append(secretEpc);
						} else
							sb.append(uniqueCode);

						sb.append(",");
						sb.append(code2);

						sb.append(",");
						Style style = CacheManager.getStyleById(detail.getStyleId());
						if (style == null) {
							sb.append("");
							sb.append(",");
							sb.append("");
						} else {
							DecimalFormat df = null;
							if (CommonUtil.isNotBlank(className)&&className.equals("Playlounge")) {
								df = new DecimalFormat("#####0.00");// 设置价格显示两位小数位
							} else {
								df = new DecimalFormat("#####0");// 设置价格显示无小数位
							}
							sb.append(df.format(style.getPrice()));
							sb.append(",");
							String class9 = "";
							if(CommonUtil.isNotBlank(style.getClass9())){
								class9 = CacheManager.getPropertyKey("C9-"+style.getClass9()).getName();
							}
							sb.append(class9);
						}
						Product p = CacheManager.getProductByCode(detail.getSku());
						if (CommonUtil.isNotBlank(p)) {
							String class7 = "";

							if (CommonUtil.isNotBlank(style) && CommonUtil.isNotBlank(style.getClass7())) {
								class7 = CacheManager.getPropertyKey("C7-"+style.getClass7()).getName();
							}


							sb.append(",").append(p.getBarcode()).append(",").append(p.getEan())
									.append(",").append(class7).append(",").append(p.getRemark());
						} else {
							sb.append(",").append("").append(",").append("").append(",").append("");
						}
						sb.append("\r\n");
						Epc epcObj = initEpcObj(detail, uniqueCode, secretEpc, code2);
						epcList.add(epcObj);
				}
			}
		return sb.toString();
	}


	public static String writeTextFile3(InitDtl detail, boolean isRfid)
			throws Exception {

		StringBuffer sb = new StringBuffer();
		int startNo = (int) detail.getStartNum();
		for (int i = 1; i <= detail.getQty(); i++) {
			// int seriaLength = Constant.Length.Epc_Length -
			// detail.getSku().length();
			// String epc =
			// detail.getSku()+CommonUtil.produceIntToString(startNo+i-1,seriaLength);
			// String code2 = PropertyUtil.getValue("webservice")+epc;
			String className = PropertyUtil.getValue("tag_name");
			ITag tag = TagFactory.getTag(className);
			tag.setStyleId(detail.getStyleId());
			tag.setColorId(detail.getColorId());
			tag.setSizeId(detail.getSizeId());
			tag.setSku(detail.getSku());
			// String uniqueCode = EpcSecretUtil.getUniqueCode(detail.getSku(),
			// startNo, i);
			// String epc = EpcSecretUtil.convertUniqueCodeToEpc2(uniqueCode);//
			// 待加密的EPC为转码之后的唯一码+“补0”
			// String secretEpc = EpcSecretUtil.encodeEpc(epc);// encode(epc);
			String uniqueCode = tag.getUniqueCode(startNo, i);
			String epc = tag.getEpc();
			String secretEpc = tag.getSecretEpc();
			String code2 = PropertyUtil.getValue("webservice") + uniqueCode;

			sb.append(detail.getStyleId());
			sb.append(",");
			sb.append(CacheManager.getStyleNameById(detail.getStyleId()));
			sb.append(",");
			sb.append(detail.getColorId());
			sb.append(",");
			sb.append(CacheManager.getColorNameById(detail.getColorId()));
			sb.append(",");
			sb.append(detail.getSizeId());
			sb.append(",");
			sb.append(CacheManager.getSizeNameById(detail.getSizeId()));
			sb.append(",");

			// sb.append(convertToASCII(epc,0));
			if (isRfid) {
				sb.append(uniqueCode);
				sb.append(",");
				sb.append(secretEpc);
			} else
				sb.append(uniqueCode);

			sb.append(",");
			sb.append(code2);

			sb.append(",");
			Style style = CacheManager.getStyleById(detail.getStyleId());
			Product p = CacheManager.getProductByCode(detail.getSku());
			if (style == null) {
				sb.append("");
				sb.append(",");
				sb.append("");
			} else {
				DecimalFormat df = null;
				if (CommonUtil.isNotBlank(tag.getTypeName())
						&& tag.getTypeName().equals("Playlounge")) {
					df = new DecimalFormat("#####0.00");// 设置价格显示两位小数位
				} else {
					df = new DecimalFormat("#####0");// 设置价格显示无小数位
				}
				sb.append(df.format(style.getPrice()));
				sb.append(",");
				sb.append(p.getRemark());
			}

			if (CommonUtil.isNotBlank(p)) {
				String class7 = "";
				if (CommonUtil.isNotBlank(style) && CommonUtil.isNotBlank(style.getClass7())) {
					class7 = style.getClass7();
				}
				sb.append(",").append(p.getBarcode()).append(",").append(p.getEan())
						.append(",").append(class7).append(",").append(p.getRemark());
			} else {
				sb.append(",").append("").append(",").append("").append(",").append("");
			}
			sb.append("\r\n");
			Epc epcObj = initEpcObj(detail, uniqueCode, secretEpc, code2);
			epcList.add(epcObj);

		}
		return sb.toString();
	}

	private static Epc initEpcObj(InitDtl dtl, String uniqueCode, String epc,
								  String code2) {
		Epc epcObj = new Epc();
		epcObj.setId(new GuidCreator().toString());

		epcObj.setCode(uniqueCode);// 唯一码

		epcObj.setEpc(epc);// 加密后的EPC
		epcObj.setDimension(code2);
		epcObj.setSku(dtl.getSku());
		epcObj.setOwnerId(dtl.getOwnerId());
		epcObj.setStyleId(dtl.getStyleId());
		epcObj.setColorId(dtl.getColorId());
		epcObj.setSizeId(dtl.getSizeId());
		epcObj.setBillNo(dtl.getBillNo());
		return epcObj;
	}

	public static List<InitDtl> convertToVos(List<InitDtl> dtlList) {
		for (InitDtl dtl : dtlList) {
			dtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
			dtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
			dtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));
		}
		return dtlList;
	}

	public static long diff(Init init, List<InitDtl> initDtls,
							List<BusinessDtl> busDtls, Integer token) {
		long totQty = 0;
		for (InitDtl iDtl : initDtls) {

			for (BusinessDtl bDtl : busDtls) {
				if (iDtl.getSku().trim().equals(bDtl.getSku().trim())) {
					totQty = totQty + setQtyValue(iDtl, bDtl, token);
					break;
				}
			}
		}
		if (token == 3) {
			init.setDetectTotQty(totQty);
			init.setStatus(3);
		} else {
			init.setReceiveTotQty(totQty);
			init.setStatus(4);
		}

		return totQty;
	}

	public static long setQtyValue(InitDtl iDtl, BusinessDtl bDtl, Integer token) {
		if (token == 3)// 检测数据
			iDtl.setDetectQty(bDtl.getQty());
		else
			iDtl.setReceiveQty(bDtl.getQty());
		return bDtl.getQty();
	}

	static Init convertToDto(Map<String, StringBuffer> zipMap) throws Exception {
		Init init = null;
		for (String zipFileName : zipMap.keySet()) {
			if (!zipFileName.contains(".initBill")) {
				continue;
			}
			init = jsonToBill(new String(zipMap.get(zipFileName).toString()
					.getBytes(), "utf-8"));
			break;
		}

		for (String zipFileName : zipMap.keySet()) {
			if (!zipFileName.contains(".initDtl")) {
				continue;
			}
			List<InitDtl> dtlList = jsonToBillDtl(new String(zipMap
					.get(zipFileName).toString().getBytes(), "utf-8"));
			init.setDtlList(dtlList);
			break;
		}

		return init;
	}

	private static Init jsonToBill(String json) {
		return JSON.parseObject(json, Init.class);
	}

	private static List<InitDtl> jsonToBillDtl(String json) {
		return JSON.parseArray(json, InitDtl.class);
	}

	public static File findZipFile(String billNo) {
		File[] files = FileUtil.filterFile(new File(
				Constant.Folder.Epc_Init_Zip_File_Folder), billNo);
		if (files.length > 0)
			return files[0];
		else
			return null;
	}

	@Deprecated
	static List<Bill> convertToBills(List<Init> initList) {
		List<Bill> bills = new ArrayList<Bill>();
		if (initList != null)
			for (Init i : initList) {
				Bill b = convertToBill(i);
				bills.add(b);
			}
		return bills;
	}

	public static List<Bill> convertToBills(List<Init> initList, int token) {
		List<Bill> bills = new ArrayList<Bill>();
		if (initList != null)
			for (Init i : initList) {
				Bill b = convertToBill(i, token);
				bills.add(b);
			}
		return bills;
	}

	static Bill convertToBill(Init init, int token) {
		Bill bill = new Bill();
		bill.setType(token);// type 与token值一样
		if (token < Constant.Token.Label_Data_Detect) {
			bill.setId(init.getBillNo() + "-" + token);
		} else {
			bill.setId(init.getBillNo());
		}
		bill.setBillNo(init.getBillNo());
		bill.setOwnerId(init.getOwnerId());
		bill.setBillDate(init.getBillDate());
		bill.setTotQty(init.getTotEpc());
		bill.setSkuQty(init.getTotSku());
		bill.setOrigUnitName(Constant.Sys.Company_Name);
		bill.setDestUnitId(init.getDestId());
		if (CommonUtil.isNotBlank(bill.getDestUnitId())) {
			bill.setDestUnitName(CacheManager.getUnitById(bill.getDestUnitId())
					.getName());
		}
		return bill;
	}

	@Deprecated
	static Bill convertToBill(Init init) {
		Bill bill = new Bill();
		bill.setType(5);// 1是代表入库单0为出库单；2标签检测单3.电商发货单 4 盘点盈亏单
		// 5标签打印单6:调拨入库单7调拨出库单
		bill.setId(init.getBillNo());
		bill.setBillNo(init.getBillNo());
		bill.setOwnerId(init.getOwnerId());
		bill.setBillDate(init.getBillDate());
		bill.setTotQty(init.getTotEpc());
		bill.setSkuQty(init.getTotSku());
		bill.setOrigUnitName(Constant.Sys.Company_Name);
		bill.setDestUnitName(CacheManager.getUnitById(init.getOwnerId()).getName());
		return bill;
	}

	public static List<BillDtl> convertToBillVos(List<InitDtl> dtlList, int token) {
		List<BillDtl> billDtls = new ArrayList<BillDtl>();
		for (InitDtl dtl : dtlList) {
			BillDtl billDtl = new BillDtl();
			billDtl.setSku(dtl.getSku());
			billDtl.setColorId(dtl.getColorId());
			billDtl.setStyleId(dtl.getStyleId());
			billDtl.setSizeId(dtl.getSizeId());
			billDtl.setQty(dtl.getQty());
			billDtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
			billDtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
			billDtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));

			if (token > Constant.Token.Label_Data_Detect) {
				billDtl.setBillId(token + "-" + billDtl.getBillId());
			}
			billDtl.setBillNo(billDtl.getBillNo());
			billDtls.add(billDtl);
		}
		return billDtls;
	}

	@Deprecated
	static List<BillDtl> convertToBillVos(List<InitDtl> dtlList) {
		List<BillDtl> billDtls = new ArrayList<BillDtl>();
		for (InitDtl dtl : dtlList) {
			BillDtl billDtl = new BillDtl();
			billDtl.setSku(dtl.getSku());
			billDtl.setColorId(dtl.getColorId());
			billDtl.setStyleId(dtl.getStyleId());
			billDtl.setSizeId(dtl.getSizeId());
			billDtl.setQty(dtl.getQty());
			billDtl.setStyleName(CacheManager.getStyleNameById(dtl.getStyleId()));
			billDtl.setColorName(CacheManager.getColorNameById(dtl.getColorId()));
			billDtl.setSizeName(CacheManager.getSizeNameById(dtl.getSizeId()));

			billDtls.add(billDtl);
		}
		return billDtls;
	}

	static void convertToPrintInfo(PropertyKey propertyKey,
								   PrintInfo printInfo, Init initBill, List<InitDtl> lstInitDtls) {
		printInfo.setId(new GuidCreator().toString());
		printInfo.setOwnerId(propertyKey.getId());
		printInfo.setPropertyKey(propertyKey.getOwnerId());
		printInfo.setBillNo(initBill.getBillNo());
		printInfo.setSku(lstInitDtls.get(0).getSku());
	}

	static void convertToPrintInfo(List<PropertyKey> pList,
								   List<PrintInfo> prList, Init initBill, List<InitDtl> lstInitDtls) {
		for (PropertyKey p : pList) {
			for (InitDtl dtl : lstInitDtls) {
				PrintInfo printInfo = new PrintInfo();
				printInfo.setId(new GuidCreator().toString());
				printInfo.setOwnerId(p.getOwnerId());
				printInfo.setPropertyKey(p.getOwnerId());
				printInfo.setBillNo(initBill.getBillNo());
				printInfo.setSku(dtl.getSku());
				prList.add(printInfo);
			}
		}

	}

	static List<PrintInfo> convertToPrintVos(List<PrintInfo> pList) {
		List<PrintInfo> printList = new ArrayList<PrintInfo>();
		for (PrintInfo p : pList) {
			PrintInfo printInfo = new PrintInfo();
			printInfo.setId(p.getId());
			printInfo.setBillNo(p.getBillNo());
			printInfo.setOwnerId(p.getOwnerId());
			printInfo.setPropertyKey(p.getPropertyKey());
			printInfo.setPropertyName(CacheManager.getPropertyKey(
					p.getPropertyKey()).getName());
			printInfo.setPropertyValue(p.getPropertyValue());
			printInfo.setSku(p.getSku());
			printList.add(printInfo);
		}
		return printList;
	}


	public static List<Init> convertToInitVos(List<Init> initList) {
		for (Init init : initList) {
			if (CommonUtil.isNotBlank(init.getDestId())) {
				init.setDestName(CacheManager.getUnitById(init.getDestId())
						.getName());
			}
		}
		return initList;
	}


}
