package com.casesoft.dmc.extend.playlounge.dao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.shop.FittingRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaoUtil {

	public static List<Bill> convertFittingToBill(List<FittingRecord> fittings) {
 		Map<String, Bill> map = new HashMap<>();
		if (CommonUtil.isNotBlank(fittings)) {
			for (FittingRecord fitting : fittings) {
				String date = CommonUtil.getDateString(fitting.getScanTime(),
						"yyyy-MM-ddHH:mm:ss")+"_"+fitting.getOwnerId();
				if (map.containsKey(date)) {
					Bill bill = map.get(date);
					bill.setActQty(bill.getActQty()+(long) fitting.getQty());
 					BillDtl dtl = new BillDtl();
					Style style = CacheManager.getStyleById(fitting
							.getStyleId());
					dtl.setBillNo(bill.getBillNo());
					dtl.setStyleId(fitting.getStyleId());
					dtl.setColorId(fitting.getColorId());
					dtl.setSizeId(fitting.getSizeId());
					dtl.setActQty((long)fitting.getQty());
					if (CommonUtil.isNotBlank(style)) {
						dtl.setPrice(style.getPrice());
						bill.setTotPrice(bill.getTotPrice() + style.getPrice()
								* dtl.getActQty());
					}
					bill.getDtlList().add(dtl);
				} else {
					Bill bill = new Bill();
					bill.setOrigId(fitting.getOwnerId());
					bill.setBillNo(date);
					bill.setActQty((long)fitting.getQty());
					bill.setDtlList(new ArrayList<BillDtl>());
					bill.setBillDate(fitting.getScanTime());
					BillDtl dtl = new BillDtl();
					bill.getDtlList().add(dtl);
					Style style = CacheManager.getStyleById(fitting
							.getStyleId());
					dtl.setBillNo(bill.getBillNo());
					dtl.setActQty((long)fitting.getQty());
					dtl.setStyleId(fitting.getStyleId());
					dtl.setColorId(fitting.getColorId());
					dtl.setSizeId(fitting.getSizeId());
					if (CommonUtil.isNotBlank(style)) {
						dtl.setPrice(style.getPrice());
						bill.setTotPrice(style.getPrice() * dtl.getActQty());
					} else {
						bill.setTotPrice(0d);
					}
					map.put(date, bill);
				}
			}
		}
		return new ArrayList<>(map.values());
	}
	
	public static List<BillDtl> getBillDtls(List<Bill> bills){
		List<BillDtl> billDtls=new ArrayList<>();
		if(CommonUtil.isNotBlank(bills)){
			for(Bill bill:bills){
				if(CommonUtil.isNotBlank(bill.getDtlList())){
					billDtls.addAll(bill.getDtlList());
				}
			}
		}
		return billDtls;
	}
	/**
	 * @param jsonArray
	 * @return
	 * 拼接属性sql
	 * */
	public static StringBuffer appendPropertis(JSONArray jsonArray){
		StringBuffer sql=new StringBuffer();
		if(CommonUtil.isNotBlank(jsonArray)&&jsonArray.size()>0){
			sql.append(" ('");
			for(int i=0;i<jsonArray.size();i++){
				JSONObject jsonObject=jsonArray.getJSONObject(i);
				sql.append(jsonObject.getString("code")).append("','");
			}
			sql.append("') ");
		}
		return sql;
	}
	/**
	 * @param jsonObject
	 * @return
	 * 获取查询属性
	 * */
	public static StringBuffer convertPropertis( JSONObject jsonObject){
		StringBuffer sqlCondition=new StringBuffer();
	/*	product.setClass1(rs.getString("BrandCode"));
		product.setClass2(rs.getString("Year"));
		product.setClass3(rs.getString("LargeCategory"));
		product.setClass4(rs.getString("SmallCategory"));
		product.setClass5(rs.getString("sex"));
		product.setClass6(rs.getString("Company"));
		product.setClass7(rs.getString("Location"));
		product.setClass8(rs.getString("Property3"));
		product.setClass9(rs.getString("Property5"));
		product.setClass10(rs.getString("Property6"));*/
		JSONArray class1=jsonObject.getJSONArray("C1");
		if(CommonUtil.isNotBlank(class1)&&class1.size()>0){
			sqlCondition.append(" and s.BrandCode in ").append(DaoUtil.appendPropertis(class1));
		}
		JSONArray class2=jsonObject.getJSONArray("C2");
		if(CommonUtil.isNotBlank(class2)&&class2.size()>0){
			sqlCondition.append(" and s.Year in ").append(DaoUtil.appendPropertis(class2));
		}
		JSONArray class3=jsonObject.getJSONArray("C3");
		if(CommonUtil.isNotBlank(class3)&&class3.size()>0){
			sqlCondition.append(" and s.LargeCategory in ").append(DaoUtil.appendPropertis(class3));
		}
		JSONArray class4=jsonObject.getJSONArray("C4");
		if(CommonUtil.isNotBlank(class4)&&class4.size()>0){
			sqlCondition.append(" and s.SmallCategory in ").append(DaoUtil.appendPropertis(class4));
		}
		JSONArray class5=jsonObject.getJSONArray("C5");
		if(CommonUtil.isNotBlank(class5)&&class5.size()>0){
			sqlCondition.append("and s.sex in ").append(DaoUtil.appendPropertis(class5));
		}
		JSONArray class6=jsonObject.getJSONArray("C6");
		if(CommonUtil.isNotBlank(class6)&&class6.size()>0){
			sqlCondition.append(" and s.Company in ").append(DaoUtil.appendPropertis(class6));
		}
		JSONArray class7=jsonObject.getJSONArray("C7");
		if(CommonUtil.isNotBlank(class7)&&class7.size()>0){
			sqlCondition.append(" and s.Location in ").append(DaoUtil.appendPropertis(class7));
		}
		JSONArray class8=jsonObject.getJSONArray("C8");
		if(CommonUtil.isNotBlank(class8)&&class8.size()>0){
			sqlCondition.append(" and s.Property3 in ").append(DaoUtil.appendPropertis(class8));
		}
		JSONArray class9=jsonObject.getJSONArray("C9");
		if(CommonUtil.isNotBlank(class9)&&class9.size()>0){
			sqlCondition.append(" and s.Property5 in ").append(DaoUtil.appendPropertis(class9));
		}
		JSONArray class10=jsonObject.getJSONArray("C10");
		if(CommonUtil.isNotBlank(class10)&&class10.size()>0){
			sqlCondition.append(" and s.Property6 in ").append(DaoUtil.appendPropertis(class10));
		}

		return sqlCondition;
	}
}
