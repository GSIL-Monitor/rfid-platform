package com.casesoft.dmc.controller.stock;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.mock.GuidCreator;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.core.util.page.SinglePage;
import com.casesoft.dmc.core.vo.ChartVo;
import com.casesoft.dmc.model.cfg.Device;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.product.Color;
import com.casesoft.dmc.model.product.Size;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.stock.*;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.task.BusinessDtl;
import com.casesoft.dmc.model.task.Record;

import java.text.ParseException;
import java.util.*;

public class StockUtil {

	// john : stock,stockdtl
	public static List<StockDtl> initStockAndDtls(List<List<StockCache>> list,
			List<Stock> stockList) throws ParseException {
		Map<String, Stock> owiMap = new HashMap<String, Stock>();
		String fkId = null;
		for (List<StockCache> dtl : list) {
			for (StockCache d : dtl) {
				StockDtl stkDtl = d.getStkDtl();
				String storageId = stkDtl.getStorageId();
				if (!owiMap.containsKey(storageId)) {
					Stock stk = new Stock();
					// 生成外键
					// fkId = CommonUtil.getDateString(stkDtl.getdateTime(),
					// "yyyyMMdd")
					// + CommonUtil.randomNumeric(3);
					fkId = CommonUtil.getDateString(stkDtl.getdateTime(),
							"yyyyMMdd") + storageId;
					// 为主表统计总数
					stk.setId(new GuidCreator().toString());
					stk.setFkId(fkId);
					stk.setDateTime(stkDtl.getdateTime());
					stk.setStorageId(storageId);
					stk.setOwnerId(stkDtl.getOwnerId());

                    //wing 2014-06-09 增加存储的仓库类型
                    Unit u = CacheManager.getStorageById(storageId);
                    if(u.getType()==Constant.UnitType.Shop) {
                        stk.setStoreType(Constant.StoreType.Shop);
                        stkDtl.setStoreType(Constant.StoreType.Shop);
                    } else {
                        stk.setStoreType(Constant.StoreType.Storage);
                        stk.setStoreType(Constant.StoreType.Storage);
                    }

					stkDtl.setFkId(fkId);
					stk.addDtl(stkDtl);
					owiMap.put(storageId, stk);

				} else {
					Stock stk = owiMap.get(storageId);

                    stk.setStoreType(stk.getStoreType());
					// 设置外键
					stkDtl.setFkId(stk.getFkId());
					stk.addDtl(stkDtl);
				}
			}
		}
		stockList.addAll(owiMap.values());
		List<StockDtl> sktDtlList = new ArrayList<StockDtl>();
		for (Stock s : stockList) {
			sktDtlList.addAll(s.getDtlList());
		}
		return sktDtlList;
	}

	public static void convertEpcStock(EpcStock epcStock){
		Style style = CacheManager.getStyleById(epcStock.getStyleId());
		Color color = CacheManager.getColorById(epcStock.getColorId());
		Size size = CacheManager.getSizeById(epcStock.getSizeId());
		if(CommonUtil.isNotBlank(style)){
			epcStock.setWsPrice(style.getWsPrice());
			epcStock.setPuPrice(style.getPuPrice());
			epcStock.setPreCast(style.getPreCast());
			epcStock.setBargainPrice(style.getBargainPrice());
		}
		if(CommonUtil.isNotBlank(color)) {
			epcStock.setColorName(color.getColorName());
		}
		if(CommonUtil.isNotBlank(size)) {
			epcStock.setSizeName(size.getSizeName());
		}
		epcStock.setBargainPrice(style.getBargainPrice());
		if (CommonUtil.isNotBlank(style.getBargainPrice())&&style.getBargainPrice()!=0){
			epcStock.setPrice(epcStock.getBargainPrice());
		}else {
			epcStock.setPrice(style.getPrice());
		}
	}

	public static List<EpcStock> convertEpcStockList(List<EpcStock> epcStockList) {
		for(EpcStock s : epcStockList){
			convertEpcStock(s);
		}
		return epcStockList;
	}


	// ///
	public static String convertToChartResult(List<Stock> l) {
		StringBuffer categories = new StringBuffer();
		StringBuffer data = new StringBuffer();
		StringBuffer data2 = new StringBuffer();
		StringBuffer data3 = new StringBuffer();
		String name = "出库数量";
		String name2 = "进库数量";
		String name3 = "库存数量";
		for (Stock o : l) {
			categories.append(",\"").append(o.getDateTime().getDate())
					.append("\"");
			data.append(",").append(o.getOqty());
			data2.append(",").append(o.getIqty());
			data3.append(",").append(o.getStockQty());

		}
		String result = "{\"categories\":[" + categories.substring(1) + "],"
				+ "\"series\":[{" + "\"name\":\"" + name + "\",\"data\":["
				+ data.substring(1) + "]},{" + "\"name\":\"" + name2
				+ "\",\"data\":[" + data2.substring(1) + "]" + "},"
				+ "{\"name\":\"" + name3 + "\",\"data\":[" + data3.substring(1)
				+ "]}" + "] }";

		return result;
	}

	public static String convertObjectToChartResult(List<Object[]> l) {
		StringBuffer categories = new StringBuffer();
		StringBuffer data = new StringBuffer();
		StringBuffer data2 = new StringBuffer();
		StringBuffer data3 = new StringBuffer();
		String name = "出库数量";
		String name2 = "进库数量";
		String name3 = "库存数量";
		for (Object[] o : l) {
			categories.append(",\"").append(o[3].toString() + "\"");
			data.append(",").append(o[0]);
			data2.append(",").append(o[1]);
			data3.append(",").append(o[2]);

		}
		String result = "{\"categories\":[" + categories.substring(1) + "],"
				+ "\"series\":[{" + "\"name\":\"" + name + "\",\"data\":["
				+ data.substring(1) + "]},{" + "\"name\":\"" + name2
				+ "\",\"data\":[" + data2.substring(1) + "]" + "},"
				+ "{\"name\":\"" + name3 + "\",\"data\":[" + data3.substring(1)
				+ "]}" + "] }";

		return result;
	}

	//
	public static List<InventoryDto> convertToVo(List<Object[]> objects) {
		List<InventoryDto> invList = new ArrayList<InventoryDto>();
		InventoryDto inv = null;
		for (Object[] os : objects) {
			inv = convertToVo(os);
			invList.add(inv);
		}
		return invList;
	}

	private static InventoryDto convertToVo(Object[] os) {

		InventoryDto inv = new InventoryDto();
		inv.setId(java.util.UUID.randomUUID().toString());

		inv.setOwnerId((String) os[0]);
		inv.setStorageId((String) os[1]);
		inv.setSku(((String) os[2]));
		inv.setStyleId(((String) os[3]));
		inv.setColorId(((String) os[4]));
		inv.setSizeId(((String) os[5]));
		inv.setIqty(Long.parseLong(CommonUtil.objToString(os[6])));

		// inv.setOSku((String)os[7]);
		// inv.setOOwnerId((String)os[8]);
		// inv.setOStorageId((String)os[9]);
		inv.setOqty(Long.parseLong(CommonUtil.objToString(os[10])));
		inv.setQty(Long.parseLong(CommonUtil.objToString(os[11])));

		// inv.setOStyleId((String)os[12]);
		// inv.setOColorId((String)os[13]);
		// inv.setOSizeId((String)os[14]);

		if (null == inv.getQty() || 0 == inv.getQty()) {
			if (CommonUtil.isBlank(inv.getSku())) {
				inv.setOwnerId((String) os[8]);
				inv.setStorageId((String) os[9]);
				inv.setSku((String) os[7]);
				inv.setStyleId((String) os[12]);
				inv.setColorId((String) os[13]);
				inv.setSizeId((String) os[14]);
				inv.setIqty(0L);
			}
			inv.setQty(inv.getIqty()
					- ((inv.getOqty() == null) ? 0 : inv.getOqty()));
		}

		inv.setStyleName(CacheManager.getStyleNameById(inv.getStyleId()));
		inv.setColorName(CacheManager.getColorNameById(inv.getColorId()));
		inv.setSizeName(CacheManager.getSizeNameById(inv.getSizeId()));
		inv.setStorageName(CacheManager.getStorageById(inv.getStorageId())
				.getName());

		return inv;
	}

	public static String convertToStr(List<InventoryDto> invDtos) {
		StringBuffer sb = new StringBuffer();
		for (InventoryDto dto : invDtos) {
			sb.append(dto.getSku()).append(",").append(dto.getQty())
					.append(";");
		}
		return sb.toString();
	}

	public static String getKcDeviceByStorageId(String storageId) {
		Collection<Device> devices = CacheManager.getDeviceMap().values();
		for (Device d : devices) {
			if (d.getCode().contains(Constant.Sys.Device_KC_Prefix)
					&& d.getStorageId().equals(storageId)) {
				return d.getCode();
			}
		}
		return null;
	}

	public static List<InventoryDto> convertToStockVo(List<Object> obs) {
		List<InventoryDto> dtos = new ArrayList<InventoryDto>();
		InventoryDto dto = null;
		// id,sku,styleId,colorId,sizeId,qty,ownerId,storageId,iQty,oQty
		for (Object o : obs) {
			Object[] os = (Object[]) o;
			dto = new InventoryDto();
			dto.setId((String) os[0]);
			dto.setSku((String) os[1]);
			dto.setStyleId((String) os[2]);
			dto.setColorId((String) os[3]);
			dto.setSizeId((String) os[4]);
			dto.setQty(Long.parseLong(os[5].toString()));
			dto.setOwnerId(((String) os[6]));
			dto.setStorageId((String) os[7]);
			dto.setIqty(Long.parseLong(os[8].toString()));
			dto.setOqty(Long.parseLong(os[9].toString()));

			dto.setUnitName(CacheManager.getUnitById(dto.getOwnerId())
					.getName());
			// dto.setOrigName(CacheManager.)

			dtos.add(dto);
		}
		return dtos;
	}

	public static List<InventoryDto> countToStockVo(List<Object> obs) {
		Map<String, InventoryDto> dtoMap = new HashMap<String, InventoryDto>();
		InventoryDto dto = null;
		for (Object o : obs) {
			Object[] os = (Object[]) o;
			String ownerId = (String) os[6];
			if (dtoMap.containsKey(ownerId)) {
				dto = dtoMap.get(ownerId);
				dto.setQty(dto.getQty() + Long.parseLong(os[5].toString()));
				dto.setIqty(dto.getIqty() + Long.parseLong(os[8].toString()));
				dto.setOqty(dto.getOqty() + Long.parseLong(os[9].toString()));
			} else {
				dto = new InventoryDto();
				dto.setId((String) os[0]);
				dto.setSku((String) os[1]);
				dto.setStyleId((String) os[2]);
				dto.setColorId((String) os[3]);
				dto.setSizeId((String) os[4]);
				dto.setQty(Long.parseLong(os[5].toString()));
				dto.setOwnerId(((String) os[6]));
				dto.setStorageId((String) os[7]);
				dto.setIqty(Long.parseLong(os[8].toString()));
				dto.setOqty(Long.parseLong(os[9].toString()));

				dto.setUnitName(CacheManager.getUnitById(dto.getOwnerId())
						.getName());

				dtoMap.put(dto.getOwnerId(), dto);
			}
		}

		return new ArrayList<InventoryDto>(dtoMap.values());
	}

	public static List<InventoryDto> convertToVos(List<InventoryDto> dtoList) {
		for (InventoryDto dto : dtoList) {
			convertWithDtl(dto);
		}
		return dtoList;
	}

	public static Page<InventoryDto> convertToVos(Page<InventoryDto> page,
			TotStockVo totQtyVo) {
		for (InventoryDto dto : page.getRows()) {
			convertWithDtl(dto);
		}
		if (CommonUtil.isBlank(totQtyVo))
			return page;

		long totQty = Long.parseLong(totQtyVo.getQty() == null ? "0" : totQtyVo
				.getQty().toString());
		long totIQty = Long.parseLong(totQtyVo.getIQty() == null ? "0"
				: totQtyVo.getIQty().toString());
		long totOQty = Long.parseLong(totQtyVo.getOQty() == null ? "0"
				: totQtyVo.getOQty().toString());
		page.addFooter("sizeId", "总库存:");
		page.addFooter("qty", "" + totQty);
		page.addFooter("storageId", "总入\\出库:");
		page.addFooter("iQty", "" + totIQty);
		page.addFooter("oQty", "" + totOQty);
		return page;

	}

	@Deprecated
	public static Page<InventoryDto> convertToVos(Page<InventoryDto> page,
			Object[] totQtys) {
		for (InventoryDto dto : page.getRows()) {
			convertWithDtl(dto);
		}
		if (CommonUtil.isBlank(totQtys))
			return page;

		long totQty = Long.parseLong(totQtys[0] == null ? "0" : totQtys[0]
				.toString());
		long totIQty = Long.parseLong(totQtys[1] == null ? "0" : totQtys[1]
				.toString());
		long totOQty = Long.parseLong(totQtys[2] == null ? "0" : totQtys[2]
				.toString());
		page.addFooter("sizeId", "总库存:");
		page.addFooter("qty", "" + totQty);
		page.addFooter("storageId", "总入\\出库:");
		page.addFooter("IQty", "" + totIQty);
		page.addFooter("OQty", "" + totOQty);
		return page;
	}

	// 错误的统计方式，只计算了单页的总量
	@Deprecated
	public static Page<InventoryDto> convertToVos(Page<InventoryDto> page) {
		long totQty = 0;
		long totIQty = 0;
		long totOQty = 0;
		for (InventoryDto dto : page.getRows()) {
			convertWithDtl(dto);
			totQty += dto.getQty();
			totIQty += dto.getIqty();
			totOQty += dto.getOqty();
		}
		page.addFooter("sizeId", "总库存:");
		page.addFooter("qty", "" + totQty);
		page.addFooter("storageId", "总入\\出库:");
		page.addFooter("IQty", "" + totIQty);
		page.addFooter("OQty", "" + totOQty);
		return page;
	}

	public static InventoryDto convertWithDtl(InventoryDto dto) {
		dto.setUnitName(CacheManager.getUnitById(dto.getOwnerId()).getName());
		dto.setStorageName(CacheManager.getStorageById(dto.getStorageId())
				.getName());
		dto.setStyleName(CacheManager.getStyleNameById(dto.getStyleId()));
		dto.setColorName(CacheManager.getColorNameById(dto.getColorId()));
		dto.setSizeName(CacheManager.getSizeNameById(dto.getSizeId()));
		return dto;
	}


	// //////////////移植Task模块Inventory函数////////////////////////
	static List<Inventory> taskConvertToVos(List<Object[]> countStock) {
		List<Inventory> invList = new ArrayList<Inventory>();
		Inventory inv = null;
		for (Object[] os : countStock) {
			inv = taskConvertToVo(os);
			invList.add(inv);
		}
		return invList;
	}

	// 统计库存，并返回单页格式
	static SinglePage<Inventory> taskConvertToPageVo(List<Object[]> countStock) {
		SinglePage<Inventory> sp = new SinglePage<Inventory>();
		List<Inventory> invList = new ArrayList<Inventory>();
		Inventory inv = null;
		long totStock = 0;
		long totInStock = 0;
		long totOutStock = 0;
		for (Object[] os : countStock) {
			inv = taskConvertToVo(os);
			invList.add(inv);
			totStock += inv.getStockQty();
			totInStock += inv.getIQty();
			totOutStock += inv.getOQty();
		}
		sp.setRows(invList);
		sp.addFooter("iSizeId", "总库存:");
		sp.addFooter("stockQty", "" + totStock);
		sp.addFooter("iStorageId", "总入\\出库:");
		sp.addFooter("iQty", "" + totInStock);
		sp.addFooter("oQty", "" + totOutStock);
		return sp;

	}
	// 统计库存，并返回单页格式
	public static SinglePage<Inventory> billConvertToPageVo(List<BillDtl> countStock) {
		SinglePage<Inventory> sp = new SinglePage<Inventory>();
		List<Inventory> invList = new ArrayList<Inventory>();
		Inventory inv = null;
		long totStock = 0;
		long totInStock = 0;
		long totOutStock = 0;
		for (BillDtl  os : countStock) {
			inv = new Inventory();
			inv.setIColorId(os.getColorId());
			inv.setISizeId(os.getSizeId());
			inv.setISku(os.getSku());
			inv.setIStyleId(os.getStyleId());
			inv.setIQty(os.getActQty());
			inv.setIStorageId(os.getUnitId());
			inv.setStyleName(CacheManager.getStyleNameById(inv.getIStyleId()));
			inv.setColorName(CacheManager.getColorNameById(inv.getIColorId()));
			inv.setSizeName(CacheManager.getSizeNameById(inv.getISizeId()));
			inv.setStorageName(CacheManager.getUnitByCode(inv.getIStorageId())
					.getName());
			inv.setStockQty(os.getActQty());
			invList.add(inv);
			totStock += inv.getStockQty();
		}
		sp.setRows(invList);
		sp.addFooter("iSizeId", "总库存:");
		sp.addFooter("stockQty", "" + totStock);
		sp.addFooter("iStorageId", "总入\\出库:");
		sp.addFooter("iQty", "" + totInStock);
		sp.addFooter("oQty", "" + totOutStock);
		return sp;
	}
	private static Inventory taskConvertToVo(Object[] os) {
		Inventory inv = new Inventory();
		inv.setIOwnerId((String) os[0]);
		inv.setIStorageId((String) os[1]);
		inv.setISku(((String) os[2]));
		inv.setIStyleId(((String) os[3]));
		inv.setIColorId(((String) os[4]));
		inv.setISizeId(((String) os[5]));
		inv.setIQty(Long.parseLong(CommonUtil.objToString(os[6])));

		inv.setOSku((String) os[7]);
		inv.setOOwnerId((String) os[8]);
		inv.setOStorageId((String) os[9]);
		inv.setOQty(Long.parseLong(CommonUtil.objToString(os[10])));
		inv.setStockQty(Long.parseLong(CommonUtil.objToString(os[11])));

		inv.setOStyleId((String) os[12]);
		inv.setOColorId((String) os[13]);
		inv.setOSizeId((String) os[14]);

		if (null == inv.getStockQty() || 0 == inv.getStockQty()) {
			if (CommonUtil.isBlank(inv.getISku())) {
				inv.setIOwnerId(inv.getOOwnerId());
				inv.setIStorageId(inv.getOStorageId());
				inv.setISku(inv.getOSku());
				inv.setIStyleId(inv.getOStyleId());
				inv.setIColorId(inv.getOColorId());
				inv.setISizeId(inv.getOSizeId());
				inv.setIQty(0L);
			}
			inv.setStockQty(inv.getIQty()
					- ((inv.getOQty() == null) ? 0 : inv.getOQty()));
		}

		inv.setStyleName(CacheManager.getStyleNameById(inv.getIStyleId()));
		inv.setColorName(CacheManager.getColorNameById(inv.getIColorId()));
		inv.setSizeName(CacheManager.getSizeNameById(inv.getISizeId()));
		inv.setStorageName(CacheManager.getStorageById(inv.getIStorageId())
				.getName());

		return inv;
	}

	static List<InventoryDtl> taskConvertToDtlVos(List<Object[]> epcObjects) {
		List<InventoryDtl> dtlList = new ArrayList<InventoryDtl>();
		InventoryDtl dtl = null;
		for (Object[] os : epcObjects) {
			dtl = taskConvertToDtlVo(os);
			dtlList.add(dtl);
		}
		return dtlList;
	}

	private static InventoryDtl taskConvertToDtlVo(Object[] os) {
		InventoryDtl dtl = new InventoryDtl();
		dtl.setStorageId((String) os[0]);
		dtl.setIEpc((String) os[1]);
		dtl.setIScanTime((Date) os[2]);

		dtl.setOEpc((String) os[3]);
		dtl.setOScanTime((Date) os[4]);

		if (CommonUtil.isBlank(dtl.getIEpc())) {
			dtl.setIEpc(dtl.getOEpc());
		}

		return dtl;
	}

	// //////////////////////////////////////

	static String filterRecordProperty(List<InventoryDtl> dtls) {
		com.alibaba.fastjson.serializer.PropertyFilter filter = new com.alibaba.fastjson.serializer.PropertyFilter() {

			@Override
			public boolean apply(Object source, String name, Object value) {
				return "iEpc".equals(name) || "iScanTime".equals(name)
						|| "oScanTime".equals(name);
			}
		};
		SerializeWriter sw = new SerializeWriter();
		JSONSerializer serializer = new JSONSerializer(sw);
		serializer.getPropertyFilters().add(filter);
		serializer.write(dtls);
		return sw.toString();
	}

	public static SinglePage<BillDtl> countToBillDtlList(
			List<BusinessDtl> busDtlList, List<InventoryDto> invDtoList) {

		List<BillDtl> invBillDtl = new ArrayList<BillDtl>();
		long totInvQty = 0;
		long totBusQty = 0;
		// 检测库存 有 的SKU
		for (InventoryDto invDto : invDtoList) {
			BillDtl billDtl = convertInvToBillDtl(invDto);
			if (null == invDto.getQty() || invDto.getQty().longValue() == 0)
				continue;
			totInvQty += billDtl.getQty();
			for (BusinessDtl busDtl : busDtlList) {
				if (billDtl.getSku().trim().equals(busDtl.getSku().trim())) {
					billDtl.setActQty(busDtl.getQty());
					totBusQty += busDtl.getQty();
					break;
				}
			}
			invBillDtl.add(billDtl);
		}
		// 检测库存 没有 的SKU
		for (BusinessDtl busDtl : busDtlList) {
			boolean isHave = false;
			for (InventoryDto invDto : invDtoList) {
				if (busDtl.getSku().trim().equals(invDto.getSku().trim())) {
					isHave = true;
					break;
				}
			}
			if (!isHave) {
				BillDtl billDtl = convertBusToBillDtl(busDtl);
				totBusQty += busDtl.getQty();
				invBillDtl.add(billDtl);
			}
		}
		SinglePage<BillDtl> page = new SinglePage<BillDtl>(invBillDtl);
		page.addFooter("styleId", "库存数:");
		page.addFooter("colorId", "" + totInvQty);
		page.addFooter("qty", "盘点数:");
		page.addFooter("actQty", "" + totBusQty);
		return page;
	}

	private static BillDtl convertBusToBillDtl(BusinessDtl busDtl) {
		BillDtl billDtl = new BillDtl();
		billDtl.setSku(busDtl.getSku());
		billDtl.setStyleId(busDtl.getStyleId());
		billDtl.setSizeId(busDtl.getSizeId());
		billDtl.setColorId(busDtl.getColorId());
		billDtl.setActQty(busDtl.getQty());

		billDtl.setStyleName(CacheManager.getStyleNameById(billDtl.getStyleId()));
		billDtl.setColorName(CacheManager.getColorNameById(billDtl.getColorId()));
		billDtl.setSizeName(CacheManager.getSizeNameById(billDtl.getSizeId()));

		return billDtl;
	}

	private static BillDtl convertInvToBillDtl(InventoryDto invDto) {
		BillDtl billDtl = new BillDtl();
		billDtl.setSku(invDto.getSku());
		billDtl.setStyleId(invDto.getStyleId());
		billDtl.setSizeId(invDto.getSizeId());
		billDtl.setColorId(invDto.getColorId());
		billDtl.setQty(invDto.getQty());

		billDtl.setStyleName(CacheManager.getStyleNameById(billDtl.getStyleId()));
		billDtl.setColorName(CacheManager.getColorNameById(billDtl.getColorId()));
		billDtl.setSizeName(CacheManager.getSizeNameById(billDtl.getSizeId()));

		return billDtl;
	}

	/**
	 * 统计库存和盘点的吊牌码差异
	 * 
	 * @param dtls
	 * @param records
	 * @return
	 */
	public static List<InventoryDtl> countToInvDtl(List<InventoryDtl> dtls,
			List<Record> records) {
		List<InventoryDtl> invCodes = new ArrayList<InventoryDtl>();
		// 除去已出库的code
		for (InventoryDtl dtl : dtls) {
			if (CommonUtil.isBlank(dtl.getOScanTime()))
				invCodes.add(dtl);
		}

		for (Record r : records) {
			boolean have = false;
			for (InventoryDtl dtl : invCodes) {
				if (r.getCode().equals(dtl.getIEpc())) {
					dtl.setOEpc(r.getCode());
					have = true;
					break;
				}
			}
			if (!have) {
				InventoryDtl invCode = new InventoryDtl();
				invCode.setOEpc(r.getCode());
				invCodes.add(invCode);
			}
		}
		return invCodes;
	}

	// ////////john
	public static ChartVo convertToChartVo(List<Stock> voList)
			throws Exception {
		ChartVo<Integer, String, long[]> chartVo = new ChartVo<Integer, String, long[]>();

		for (Stock vo : voList) {
			Date d =vo.getDateTime();
			long time = d.getTime();
			String name = CacheManager.getUnitById(vo.getOwnerId()).getName();
			boolean isHave = false;
			for (ChartVo.Serie s : chartVo.getSeries()) {
				if (s.getName().equals(name)) {
					long[] dataArr = new long[2];
					dataArr[0] = time;
					dataArr[1] = vo.getStockQty();
//					dataArr[2] = vo.getOqty();
//					dataArr[3] = vo.getIqty();
//					dataArr[4] = vo.getTotSku();
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
				dataArr[1] = vo.getStockQty();
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
	public static Page<InventoryDto> convertToStockVo(Page<InventoryDto> page) {
		long totQty = 0L;
		long totIQty = 0L;
		long totOQty = 0L;
		for (InventoryDto dto : page.getRows()) {
			totQty = totQty+Long.parseLong(dto.getQty() == null ? "0" : dto
					.getQty().toString());
			totIQty = totIQty + Long.parseLong(dto.getIqty() == null ? "0"
					: dto.getIqty().toString());
			totOQty = totOQty+Long.parseLong(dto.getOqty() == null ? "0"
					: dto.getOqty().toString());
			convertWithDtl(dto);
		}

		page.addFooter("sizeId", "总库存:");
		page.addFooter("qty", "" + totQty);
		page.addFooter("storageId", "总入\\出库:");
		page.addFooter("iQty", "" + totIQty);
		page.addFooter("oQty", "" + totOQty);
		return page;
		
	}

	public static List<Bill> conventEpcStocksToBill(int token,String storageId, List<EpcStock> list){
		List<Bill> listBill=new ArrayList<Bill>();
		if(CommonUtil.isBlank(list)){
			return null;
		}
		Bill bill=new Bill();
		bill.setTotQty(Long.parseLong(String.valueOf(list.size())));
		Date date=new Date();
		bill.setId(String.valueOf(token)+"-"+storageId+"-"+CommonUtil.getDateString(date, "yyMMddHHmmss"));
		bill.setBillNo(bill.getId());

		bill.setSkuQty(Long.parseLong(String.valueOf(list.size())));
		bill.setOrigUnitId(storageId);
		bill.setType(token);
		bill.setBillDate(date);
		bill.setBillType("STU");
		listBill.add(bill);
		return listBill;
	}


}
