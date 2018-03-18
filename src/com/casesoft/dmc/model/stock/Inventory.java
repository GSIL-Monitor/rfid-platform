package com.casesoft.dmc.model.stock;

import javax.persistence.Transient;

/**
 * i.ownerId as iOwnerId,i.storageId as iStorageId,i.sku as iSku,
 * i.styleId as iStyleId," +
"  i.colorId as iColorId,i.sizeId as iSizeId,i.iQty as iQty," +
				"o.sku as oSku,o.ownerId as oOwnerId,o.storageId
				 as oStorageId,o.oQty,i.iQty-o.oQty as stockQty
 * @author Administrator
 *
 */
public class Inventory {
    private String iOwnerId;
    private String iStorageId;
    private String iSku;
    private String iStyleId;
    private String iColorId;
    private String iSizeId;
    private Long iQty;
    
    private String oSku;
    private String oOwnerId;
    private String oStorageId;
    private String oStyleId;
    private String oColorId;
    private String oSizeId;
    
    private Long oQty;
    
    private Long stockQty;
    
    private String styleName;
    private String colorName;
    private String sizeName;
    
    private String storageName;


	public String getIOwnerId() {
		return iOwnerId;
	}

	public void setIOwnerId(String iOwnerId) {
		this.iOwnerId = iOwnerId;
	}

	public String getIStorageId() {
		return iStorageId;
	}

	public void setIStorageId(String iStorageId) {
		this.iStorageId = iStorageId;
	}

	public String getISku() {
		return iSku;
	}

	public void setISku(String iSku) {
		this.iSku = iSku;
	}

	public String getIStyleId() {
		return iStyleId;
	}

	public void setIStyleId(String iStyleId) {
		this.iStyleId = iStyleId;
	}

	public String getIColorId() {
		return iColorId;
	}

	public void setIColorId(String iColorId) {
		this.iColorId = iColorId;
	}

	public String getISizeId() {
		return iSizeId;
	}

	public void setISizeId(String iSizeId) {
		this.iSizeId = iSizeId;
	}

	public Long getIQty() {
		return iQty;
	}

	public void setIQty(Long iQty) {
		this.iQty = iQty;
	}

	public String getOSku() {
		return oSku;
	}

	public void setOSku(String oSku) {
		this.oSku = oSku;
	}

	public String getOOwnerId() {
		return oOwnerId;
	}

	public void setOOwnerId(String oOwnerId) {
		this.oOwnerId = oOwnerId;
	}

	public String getOStorageId() {
		return oStorageId;
	}

	public void setOStorageId(String oStorageId) {
		this.oStorageId = oStorageId;
	}
	
	

	public String getOStyleId() {
		return oStyleId;
	}

	public void setOStyleId(String oStyleId) {
		this.oStyleId = oStyleId;
	}

	public String getOColorId() {
		return oColorId;
	}

	public void setOColorId(String oColorId) {
		this.oColorId = oColorId;
	}

	public String getOSizeId() {
		return oSizeId;
	}

	public void setOSizeId(String oSizeId) {
		this.oSizeId = oSizeId;
	}

	public Long getOQty() {
		return oQty;
	}

	public void setOQty(Long oQty) {
		this.oQty = oQty;
	}

	public Long getStockQty() {
		return stockQty;
	}

	public void setStockQty(Long stockQty) {
		this.stockQty = stockQty;
	}
    
    ////===================>
    
    @Transient
	public String getStyleName() {
		return styleName;
	}


	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	@Transient
	public String getColorName() {
		return colorName;
	}


	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	@Transient
	public String getSizeName() {
		return sizeName;
	}


	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}
	@Transient
	public String getStorageName() {
		return storageName;
	}

	
	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}
	
	
    
}
