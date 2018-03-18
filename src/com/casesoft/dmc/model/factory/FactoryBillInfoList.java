package com.casesoft.dmc.model.factory;

import com.casesoft.dmc.model.product.Color;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.product.Size;
import com.casesoft.dmc.model.product.Style;

import java.io.Serializable;
import java.util.List;

public class FactoryBillInfoList implements Serializable
{
	List<FactoryInit> factoryList;
	List<FactoryBill> initList;
	List<FactoryBillDtl> initDtlList;
	List<Product> productList;
	List<Style> styleList;
	List<Color> colorList;
	List<Size> sizeList;
	List<FactoryCategory> categoryList;



    List<BillSchedule> billScheduleList;

	public FactoryBillInfoList(){}

    public FactoryBillInfoList(List<FactoryBill> initList, List<BillSchedule> billScheduleList) {
        this.initList = initList;
        this.billScheduleList = billScheduleList;
    }

    public FactoryBillInfoList(List<FactoryBill> initList, List<Product> productList, List<Style> styleList, List<Color> colorList, List<Size> sizeList)
	{
		this.initList = initList;
		this.productList = productList;
		this.styleList = styleList;
		this.colorList = colorList;
		this.sizeList = sizeList;
	}

	public FactoryBillInfoList(List<FactoryBill> initList, List<FactoryBillDtl> initDtlList, List<Product> productList, List<Style> styleList, List<Color> colorList, List<Size> sizeList)
	{
		this.initList = initList;
		this.initDtlList = initDtlList;
		this.productList = productList;
		this.styleList = styleList;
		this.colorList = colorList;
		this.sizeList = sizeList;
	}
	
	public FactoryBillInfoList(List<FactoryInit> factoryList, List<FactoryBill> initList, List<FactoryBillDtl> initDtlList, List<Product> productList,
                               List<Style> styleList, List<Color> colorList, List<Size> sizeList, List<FactoryCategory> categoryList)
	{
		this.factoryList = factoryList;
		this.initList = initList;
		this.initDtlList = initDtlList;
		this.productList = productList;
		this.styleList = styleList;
		this.colorList = colorList;
		this.sizeList = sizeList;
		this.categoryList = categoryList;
	}
	public List<FactoryInit> getFactoryList(){
		return this.factoryList;
	}
	public void setFactoryList(List<FactoryInit> factoryList){
		this.factoryList=factoryList;
	}	
	public List<FactoryBill> getInitList()
	{
		return this.initList;
	}
	public void setInitList(List<FactoryBill> initList) {
		this.initList = initList;
	}
	public List<Product> getProductList() {
		return this.productList;
	}
	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}
	public List<Style> getStyleList() {
		return this.styleList;
	}
	public void setStyleList(List<Style> styleList) {
		this.styleList = styleList;
	}
	public List<Color> getColorList() {
		return this.colorList;
	}
	public void setColorList(List<Color> colorList) {
		this.colorList = colorList;
	}
	public List<Size> getSizeList() {
    return this.sizeList;
	}
	public List<FactoryBillDtl> getInitDtlList() {
		return this.initDtlList;
	}

	public void setInitDtlList(List<FactoryBillDtl> initDtlList)
	{
		this.initDtlList = initDtlList;
	}

	public List<FactoryCategory> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<FactoryCategory> categoryList) {
		this.categoryList = categoryList;
	}

	public void setSizeList(List<Size> sizeList)
	{
		this.sizeList = sizeList;
	}

    public List<BillSchedule> getBillScheduleList() {
        return billScheduleList;
    }

    public void setBillScheduleList(List<BillSchedule> billScheduleList) {
        this.billScheduleList = billScheduleList;
    }
}