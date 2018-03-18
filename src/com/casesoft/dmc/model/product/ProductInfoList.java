package com.casesoft.dmc.model.product;

import java.util.List;

import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.cfg.PropertyType;
import com.casesoft.dmc.model.product.Color;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.product.Size;
import com.casesoft.dmc.model.product.Style;

/**
 * Created by WingLi on 2014/11/18.
 */
public class ProductInfoList {
    private List<Product> productList;
    private List<Style> styleList;
    private List<Color> colorList;
    private List<Size> sizeList;
    private List<SizeSort> sizeSort;
    private List<PropertyType> propertyTypeList;
    private List<PropertyKey> propertyKeyList;
    private Integer insertCount;
    private Integer updateCount;
    public ProductInfoList() {
    }
    
    
    public ProductInfoList(List<Product> productList, List<Style> styleList, List<Color> colorList, List<Size> sizeList
    		,List<SizeSort> sizeSort,List<PropertyType> propertyTypeList,List<PropertyKey> propertyKeyList
    		,Integer insertCount,Integer updateCount) {
        this.productList = productList;
        this.styleList = styleList;
        this.colorList = colorList;
        this.sizeList = sizeList;
        this.sizeSort = sizeSort;
        this.propertyTypeList = propertyTypeList;
        this.propertyKeyList = propertyKeyList;
        this.insertCount = insertCount;
        this.updateCount = updateCount;
        
    }
    public ProductInfoList(List<Product> productList, List<Style> styleList, List<Color> colorList, List<Size> sizeList) {
        this.productList = productList;
        this.styleList = styleList;
        this.colorList = colorList;
        this.sizeList = sizeList;
    }
    public ProductInfoList(List<Product> productList, List<Style> styleList, List<Color> colorList, List<Size> sizeList,List<PropertyKey> propertyKeyList) {
        this.productList = productList;
        this.styleList = styleList;
        this.colorList = colorList;
        this.sizeList = sizeList;
        this.propertyKeyList = propertyKeyList;
    }

    public List<PropertyKey> getPropertyKeyList() {
		return propertyKeyList;
	}

	public void setPropertyKeyList(List<PropertyKey> propertyKeyList) {
		this.propertyKeyList = propertyKeyList;
	}

	public List<Size> getSizeList() {
        return sizeList;
    }

    public void setSizeList(List<Size> sizeList) {
        this.sizeList = sizeList;
    }

    public List<Color> getColorList() {
        return colorList;
    }

    public void setColorList(List<Color> colorList) {
        this.colorList = colorList;
    }

    public List<Style> getStyleList() {
        return styleList;
    }

    public void setStyleList(List<Style> styleList) {
        this.styleList = styleList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

	public List<SizeSort> getSizeSort() {
		return sizeSort;
	}

	public void setSizeSort(List<SizeSort> sizeSort) {
		this.sizeSort = sizeSort;
	}

	public List<PropertyType> getPropertyTypeList() {
		return propertyTypeList;
	}

	public void setPropertyTypeList(List<PropertyType> propertyTypeList) {
		this.propertyTypeList = propertyTypeList;
	}


	public Integer getInsertCount() {
		return insertCount;
	}


	public void setInsertCount(Integer insertCount) {
		this.insertCount = insertCount;
	}


	public Integer getUpdateCount() {
		return updateCount;
	}


	public void setUpdateCount(Integer updateCount) {
		this.updateCount = updateCount;
	}
}
