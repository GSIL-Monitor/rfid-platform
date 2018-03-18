package com.casesoft.dmc.extend.tag;

import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.secret.EpcSecretUtil;

public class TestTag extends AbstractBaseTag {

  private String secretEpc;

  public TestTag() {
    super();
  }

  public TestTag(String styleId, String colorId, String sizeId) {
    super();
    this.styleId = styleId;
    this.colorId = colorId;
    this.sizeId = sizeId;
  }

  @Override
  public String getEpc() {
    StringBuffer epcStr = new StringBuffer("");

    char c = this.uniqueCode.charAt(0);
    epcStr.append(Integer.toHexString(c));

    epcStr.append(this.uniqueCode.substring(1));

    this.epc = epcStr.toString() + CommonUtil.produceIntToString(0, 24 - epcStr.length());
    return this.epc;
  }

  @Override
  public int getEpcLength() {
    return 24;
  }

  @Override
  public String getSecretEpc() {
//    if (this.secretEpc != null && !this.secretEpc.equals(""))
//      return this.secretEpc;
//    if (this.epc != null && !this.epc.equals(""))
//      return EpcSecretUtil.encodeEpc(this.epc);
//    else {
//    	this.getEpc();
//    	return EpcSecretUtil.encodeEpc(this.epc);
//    }
	  this.getEpc();
	    return EpcSecretUtil.encodeEpc(this.epc);
  }

  @Override
  public void setSecretEpc(String secretEpc) {
    this.secretEpc = secretEpc;
    this.epc = EpcSecretUtil.decodeEpc(secretEpc);
  }

  @Override
  public String getUniqueCode(int startNo, int i) {
    String sku = this.styleId + this.colorId + this.sizeId;
    this.uniqueCode = sku + CommonUtil.produceIntToString(startNo + i - 1, this.getSerialLength());
    return uniqueCode;
  }

  @Override
  public String getUniqueCode() {
    if (this.isNotBlank(this.uniqueCode)) {
      return this.uniqueCode;
    }

    if (this.isNotBlank(this.epc)) {
      // int charIndex={0}
      StringBuffer uniqueCodeStr = new StringBuffer("");
      String asciiStr = this.epc.substring(0, 2);
      uniqueCodeStr.append((char) Integer.parseInt(asciiStr, 16));
      uniqueCodeStr.append(this.epc.substring(2));
      this.uniqueCode = uniqueCodeStr.substring(0, this.getStyleLength() + this.getColorLength()
          + this.getSizeLength() + this.getSerialLength());
      this.styleId = this.uniqueCode.substring(0, this.getStyleLength());
      this.colorId = this.uniqueCode.substring(this.getStyleLength(),
          this.getStyleLength() + this.getColorLength());
      this.sizeId = this.uniqueCode.substring(this.getStyleLength() + this.getColorLength(),
          this.getStyleLength() + this.getColorLength() + this.getSizeLength());
      return this.uniqueCode;
    } else
      return "";
  }

  @Override
  public String setUniqueCode(String uniqueCode) {
      if(CommonUtil.isBlank(this.uniqueCode)) {
          this.uniqueCode = uniqueCode;
          if(CommonUtil.isBlank(this.styleId)) {
              this.styleId = this.uniqueCode.substring(0, this.getStyleLength());
              this.colorId = this.uniqueCode.substring(this.getStyleLength(),
                      this.getStyleLength() + this.getColorLength());
              this.sizeId = this.uniqueCode.substring(this.getStyleLength() + this.getColorLength(),
                      this.getStyleLength() + this.getColorLength() + this.getSizeLength());
          }
      }

    return this.uniqueCode;
  }

  @Override
  public String getSku() {
    return this.styleId + this.colorId + this.sizeId;
  }

  @Override
  public String getStyleId() {
    return this.styleId;
  }

  @Override
  public String getColorId() {
    return this.colorId;
  }

  @Override
  public String getSizeId() {
    return this.sizeId;
  }

  @Override
  public int getSerialLength() {
    return 6;
  }

  @Override
  public int getStyleLength() {
    return 6;
  }

  @Override
  public int getColorLength() {
    return 2;
  }

  @Override
  public int getSizeLength() {
    return 2;
  }

  @Override
  public void setStyleId(String styleId) {
    this.styleId = styleId;

  }

  @Override
  public void setColorId(String colorId) {
    this.colorId = colorId;

  }

  @Override
  public void setSizeId(String sizeId) {
    this.sizeId = sizeId;

  }

  @Override
  public String convertToStyle(int temp) throws Exception {
    throw new Exception("styleId can not be Integer!");
  }

  @Override
  public String convertToColor(int temp) throws Exception {
    return CommonUtil.convertIntToString(temp, getColorLength());
  }

  @Override
  public String convertToSize(int temp) throws Exception {
    return CommonUtil.convertIntToString(temp, getSizeLength());
  }

  @Override
  public String[] getSizeConfig() {
    return new String[] { "01", "02", "03" };
  }

  @Override
  public String getTypeName() {
    return "Test";
  }

@Override
public String getImagePath(String styleId, String colorId, String sizeId) {
	return "/sku/"+styleId+colorId+sizeId+".jpg";
}

  @Override
  public String getServerUrl() throws Exception {
    return PropertyUtil.getValue("test_dataServer_url");
  }


  @Override
  public String getClientUpdateFilePath() throws Exception {
    return PropertyUtil.getValue("test_updateFile_Path");
  }
  @Override
  public void setSku(String sku) {
		this.sku = sku;
  }
@Override
public String getEpc(String uniqueCode) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSecretEpc(String epc) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getUniqueCodeBySku(int startNo, int i, String sku) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSkuByUniqueCode(String uniqueCode) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getStyleIdByUniqueCode(String uniqueCode) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getColorIdByUniqueCode(String uniqueCode) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSizeIdByUniqueCode(String uniqueCode) {
	// TODO Auto-generated method stub
	return null;
}



}