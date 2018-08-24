package com.casesoft.dmc.core.util.page;

import com.alibaba.fastjson.annotation.JSONField;
import com.casesoft.dmc.core.util.CommonUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {

	//-- 公共变量 --//
	public static final String ASC = "asc";
	public static final String DESC = "desc";

	//-- 分页参数 --//
	private int page;//rows=50   当前页码 兼容jqgrid新增参数，小程序设置分页时，也要设置此参数。
	protected int pageNo = 1;  //当前页码
	protected int pageSize = -1;
	protected String orderBy = null;
	protected String order = null;	//升序降序  desc asc
	protected boolean autoCount = true;

	//-- 返回结果 --//
	protected List<T> rows = new ArrayList<T>();//Lists.newArrayList();
	protected long total = -1;
	
	private JSONArray footer = null;

	private String sumFields = null;//qty,value ,隔开
	private String sumValues = null;

    private String sort;	//排序字段

	private String sord;//asc
	private String sidx;//code

    private long totPage;
	
	
	//private 
	
	public Page() {
		
	}
    @JSONField(name="total")
    public long getTotPage() {
        return totPage;
    }

    public void setTotPage(long totPage) {
        this.totPage = totPage;
    }

    public Page(int pageSize) {
		this.pageSize = pageSize;
	}

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public String getSord() {
        return sord;
    }

    public void setSord(String sord) {
        this.sord = sord;
    }

    //-- 分页参数访问函数 --//
	/**
	 * 获得当前页的页号,序号从1开始,默认为1.
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * 设置当前页的页号,序号从1开始,低于1时自动调整为1.
	 */
	public void setPageNo(final int pageNo) {
		this.pageNo = pageNo;

		if (pageNo < 1) {
			this.pageNo = 1;
		}
	}

	/**
	 * 返回Page对象自身的setPageNo函数,可用于连续设置。
	 */
	public Page<T> pageNo(final int thePageNo) {
		setPageNo(thePageNo);
		return this;
	}
	
	/**
	 * 获得每页的记录数量, 默认为-1.
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页的记录数量.
	 */
	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 返回Page对象自身的setPageSize函数,可用于连续设置。
	 */
	public Page<T> pageSize(final int thePageSize) {
		setPageSize(thePageSize);
		return this;
	}
	
	/**
	 * 获得总记录数, 默认值为-1.
	 */
    @JSONField(name="records")
	public long getTotal() {
		return total;
	}

	/**
	 * 设置总记录数.
	 */
	public void setTotal(final long total) {
		this.total = total;
	}
	
	/**
	 * 获得页内的记录列表.
	 * 如果以JSON数据输出时，设置name为rows
	 */
	public List<T> getRows() {
		return rows;
	}

	/**
	 * 设置页内的记录列表.
	 */
	public void setRows(final List<T> rows) {
		this.rows = rows;
	}
	
	public void addResult(T t) {
		this.rows.add(t);
	}

	/**
	 * 获得查询对象时是否先自动执行count查询获取总记录数, 默认为true.
	 */
	@JSONField(serialize=false)
	public boolean isAutoCount() {
		return autoCount;
	}
	
	/**
	 * 返回Page对象自身的setAutoCount函数,可用于连续设置。
	 */
	public Page<T> autoCount(final boolean theAutoCount) {
		setAutoCount(theAutoCount);
		return this;
	}

	/**
	 * 设置查询对象时是否自动先执行count查询获取总记录数.
	 */
	public void setAutoCount(final boolean autoCount) {
		this.autoCount = autoCount;
	}

	/**
	 * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从1开始.
	 */
	@JSONField(serialize=false)
	public int getFirst() {
		return ((pageNo - 1) * pageSize) + 1;
	}
	
	/**
	 * 设置排序方式向.
	 * 
	 * @param order 可选值为desc或asc,多个排序字段时用','分隔.
	 */
	public void setOrder(final String order) {
		String lowcaseOrder = StringUtils.lowerCase(order);

		//检查order字符串的合法值
		String[] orders = StringUtils.split(lowcaseOrder, ',');
		for (String orderStr : orders) {
			if (!StringUtils.equals(DESC, orderStr) && !StringUtils.equals(ASC, orderStr)) {
				throw new IllegalArgumentException("排序方向" + orderStr + "不是合法值");
			}
		}

		this.order = lowcaseOrder;
	}
	
	/**
	 * 获得排序方向, 无默认值.
	 */
	@JSONField(serialize=false)
	public String getOrder() {
		return order;
	}
	
	/**
	 * 获得排序字段,无默认值. 多个排序字段时用','分隔.
	 */
	@JSONField(serialize=false)
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * 设置排序字段,多个排序字段时用','分隔.
	 */
	public void setOrderBy(final String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * 是否已设置排序字段,无默认值.
	 */
	@JSONField(serialize=false)
	public boolean isOrderBySetted() {
		return (StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(order));
	}
	
	public JSONArray getFooter() {
		return footer;
	}

	public void setFooter(JSONArray footer) {
		this.footer = footer;
	}
	
	
	public void addFooter(String key,String value) {
		if(null == footer || footer.size() <= 0) {
		    footer = new JSONArray();
                    JSONObject jo = new JSONObject();
		    jo.put(key, value);
		    footer.add(jo);
		} else {
			((JSONObject)footer.get(0)).put(key, value);
		}
		
		
	}
	public void addFooter(int index, String[] keys,Object[] values){
		if(CommonUtil.isBlank(footer)){
			footer = new JSONArray();
		}		
		JSONObject jo = new JSONObject();
		for(int i = 1; i < values.length; i++){			
			jo.put(keys[i-1], values[i]);			
		}
		footer.add(index,jo);
	}

	public void addFooter(String[] keys,Object[] values){
		if(CommonUtil.isBlank(footer)){
			footer = new JSONArray();
			addFooter(0, keys, values);
		} else {
			addFooter(footer.size() - 1, keys, values);
		}
	}

    /*
 * 设置页面属性
 */
    public void setPageProperty() {
        if (null != sort && order != null) {
            this.setOrderBy(sort);
            this.setOrder(order);
        }
        if(null != sidx && sord !=null){
        	for(String sortId :sidx.split(",")){
        		if(sortId.contains("asc") || sortId.contains("desc")){
					sidx =sidx.replaceAll(sortId+",","");
				}
			}
			sidx = sidx.trim();
            this.setOrderBy(sidx);
            this.setOrder(sord);
        }
        if(CommonUtil.isNotBlank(sumFields)) {
            this.setSumFields(sumFields);
        }
        this.setPageNo(page);
        if(CommonUtil.isNotBlank(rows)) {
            int rowCount = Integer.parseInt(""+rows.get(0));
            this.setPageSize(rowCount);
        }
        this.setFooter(null);
    }

	public String getSumFields() {
		return sumFields;
	}

	public void setSumFields(String sumFields) {
		this.sumFields = sumFields;
	}

	public String getSumValues() {
		return sumValues;
	}

	public void setSumValues(String sumValues) {
		this.sumValues = sumValues;
	}
}
