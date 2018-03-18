package com.casesoft.dmc.core.util.page;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * 单页显示数据的实体封装类
 * @author Administrator
 *
 * @param <T>
 */
public class SinglePage<T> {

	private int total;//总数
	private List<T> rows;//显示的数据列表
	private JSONArray footer = new JSONArray();//分页的下栏
	
	public SinglePage() {
		super();
	}

	public SinglePage(List<T> rows) {
		super();
		this.rows = rows;
		this.total = rows.size();
	}

	public SinglePage(int total, List<T> rows, JSONArray footer) {
		super();
		this.total = total;
		this.rows = rows;
		this.footer = footer;
	}

	public void addRow(T o) {
		if(rows == null)
			rows = new ArrayList<T>();
		rows.add(o);
	}
	
	public void addFooterRow(JSONObject row) {
		footer.add(row);
	}
	
	public void addFooterRow(String key,String value) {
		JSONObject jo = new JSONObject();
		jo.put(key, value);
		footer.add(jo);
	}
	
	public void addFooter(String key,String value) {
		addFooterColumn(key,value,0);
	}
	
	public void addFooterColumn(String key,String value,int rowNo) {
		int index = footer.size();
		if(index == 0) {
			addFooterRow(key,value);
		}
		if(rowNo > index) 
			rowNo = index - 1;
		footer.getJSONObject(rowNo).put(key,value);
	}
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
        this.total += rows.size();
	}

	public JSONArray getFooter() {
		return footer;
	}

	public void setFooter(JSONArray footer) {
		this.footer = footer;
	}	     	
}


