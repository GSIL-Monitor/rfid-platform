package com.casesoft.dmc.extend.tiantan.dao;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.BusinessDtl;
import com.casesoft.dmc.model.task.Record;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TiantanUtil {
	/**
	 * @param bill
	 * @return 过滤已扫描的详单
	 * */
	public static List<BillDtl> filterNotScan(Bill bill) {
		List<BillDtl> billDtls = bill.getDtlList();
		List<BillDtl> newBillDtls = new ArrayList<>();
		double totPrice = 0;
		double preCast = 0;
		if (CommonUtil.isNotBlank(billDtls)) {
			for (BillDtl dtl : billDtls) {
				if (CommonUtil.isNotBlank(dtl.getActQty())
						&& dtl.getActQty() != 0l) {

					Style style = CacheManager.getStyleById(dtl.getStyleId());
					if (CommonUtil.isNotBlank(style)) {
						if (CommonUtil.isNotBlank(style.getPrice())) {
							totPrice += dtl.getActQty() * style.getPrice();
							dtl.setPrice(dtl.getActQty() * style.getPrice());
						}
						if (CommonUtil.isNotBlank(style.getPreCast())) {
							preCast += dtl.getActQty() * style.getPreCast();
							dtl.setPrePrice(dtl.getActQty() * style.getPreCast());;
						}
					}
				}else{
					dtl.setActQty(0l);
					dtl.setPrice(0d);
					dtl.setPrePrice(0d);
				}
				newBillDtls.add(dtl);
			}
		}
		bill.setTotPrePrice(preCast);
		bill.setTotPrice(totPrice);
		return newBillDtls;
	}

	public static void countTotPrice(Business bus) {
		List<BusinessDtl> dtls = bus.getDtlList();
		double totPrice = 0;
		if (CommonUtil.isNotBlank(dtls)) {
			for (BusinessDtl dtl : dtls) {
				Style style = CacheManager.getStyleById(dtl.getStyleId());
				if (CommonUtil.isNotBlank(style)&&CommonUtil.isNotBlank(style.getPrice())) {
					totPrice += style.getPrice()*dtl.getQty();
				}
			}
		}
		bus.setTotPrice(totPrice);
	}

	public static void buildBillRecord(Business bus, List<Record> records)
			throws Exception {
		Bill bill = bus.getBill();
		List<Record> addedRecords = new ArrayList<>();
		long actQty=0;
		long initQty=0;
		if (CommonUtil.isNotBlank(bill)) {
			List<Record> recordList = bus.getRecordList();
			Map<String, BillDtl> billMap = new HashMap<>();
			List<BillDtl> billDtls = bill.getDtlList();
			Map<String, String> recordMap = new HashMap<>();

			for (BillDtl dtl : billDtls) {
				dtl.setScanQty(0l);
				dtl.setInitQty(0l);
				dtl.setActQty(0l);
				billMap.put(dtl.getSku(), dtl);
			}
			for (Record r : records) {
				recordMap.put(r.getCode(), r.getCode());
				BillDtl dtl = billMap.get(r.getSku());
				if (CommonUtil.isNotBlank(dtl)) {
					dtl.setActQty(dtl.getActQty() + 1);
					dtl.setInitQty(dtl.getInitQty() + 1);
					actQty++;
					initQty++;
				}
			}
			if (CommonUtil.isNotBlank(recordList)) {
				for (Record r : recordList) {
					Record rs = copy(r);
					rs.setId(bill.getBillNo() + r.getCode());
					rs.setTaskId(bill.getBillNo());
					BillDtl dtl = billMap.get(r.getSku());
					if (!recordMap.containsKey(r.getCode())&&CommonUtil.isNotBlank(dtl)) {
						dtl.setActQty(dtl.getActQty() + 1);
						dtl.setInitQty(dtl.getInitQty() + 1);
						actQty++;
						initQty++;
					}
					addedRecords.add(rs);
				}
			}
			bill.setActQty(actQty);
			bill.setInitQty(initQty);
			bill.setScanQty(0l);
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
		// 是否实现了序列化接口，即使该类实现了，他拥有的对象未必也有...
		if (Serializable.class.isAssignableFrom(obj.getClass())) {
			// 如果子类没有继承该接口，这一步会报错
			try {
				return copyImplSerializable(obj);
			} catch (Exception e) {
				// 这里不处理，会运行到下面的尝试json
			}
		}

		return null;
	}

	/**
	 * 深层拷贝 - 需要类继承序列化接口
	 * 
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
		// 如果子类没有继承该接口，这一步会报错
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
		} finally {
			try {
				baos.close();
				oos.close();
				bais.close();
				ois.close();
			} catch (Exception e2) {
				// 这里报错不需要处理
			}
		}
	}
}
