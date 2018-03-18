package com.casesoft.dmc.extend.tag;

import com.casesoft.dmc.core.vo.ITag;

/**
 * Created by WinstonLi on 2014/6/26.
 */
public abstract class AbstractBaseTag implements ITag {

  protected String styleId;
  protected String colorId;
  protected String sizeId;
  protected String sku;
  protected String uniqueCode;
  protected String epc;

  @Override
  public String getStyleId() throws Exception {
    if(this.isNotBlank(this.styleId))
      return this.styleId;
    else if(this.isNotBlank(this.uniqueCode)){
      return this.uniqueCode.substring(0,this.getStyleLength());
    } else {
      throw new Exception("styleId can not be Null!");
    }
  }

  @Override
  public String getColorId() throws Exception {

    if(this.isNotBlank(this.colorId))
      return this.colorId;
    else if(this.isNotBlank(this.uniqueCode)){
      return this.uniqueCode.substring(this.getStyleLength(),this.getStyleLength()+this.getColorLength());
    } else {
      throw new Exception("colorId can not be Null!");
    }

  }

  @Override
  public String getSizeId() throws Exception {
    if(this.isNotBlank(this.sizeId))
      return this.sizeId;
    else if(this.isNotBlank(this.uniqueCode)){
      return this.uniqueCode.substring(this.getStyleLength()+this.getColorLength(),
          this.getStyleLength()+this.getColorLength()+this.getSizeLength());
    } else {
      throw new Exception("sizeId can not be Null!");
    }
  }

  @Override
  public String getSerialNo() throws Exception{
    if(this.isNotBlank(this.uniqueCode)) {
      int skuLength = this.getStyleLength()+this.getColorLength()+this.getSizeLength();
      return this.uniqueCode.substring(skuLength,skuLength+this.getSerialLength());
    } else {
      throw new Exception("uniqueCode can not be Null!");
    }
  }

  public boolean isNotBlank(String str) {
    return str != null && !str.equals("");
  }
  
  @Override
  public String getImagePath(String styleId, String colorId, String sizeId) {
	  return "";
  }
  @Override
  public boolean matcherStyleId(String styleId) {
    return true;
  }
  @Override
  public boolean matcherColorId(String colorId) {
    return true;
  }
  @Override
  public boolean matcherSizeId(String sizeId) {
    return true;
  }

  @Override
  public boolean isUniqueCodeStock() {
    return false;
  }
  @Override
  public boolean isImportUniqueCode() { return false; }
}
