package com.casesoft.dmc.extend.tag;

import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.secret.EpcSecretUtil;

/**
 * Created by WinstonLi on 2014/6/23.
 */
public class QiPaiTag extends AbstractBaseTag {

  private String secretEpc;

  public QiPaiTag() {
  }
  public QiPaiTag(String styleId, String colorId, String sizeId) {
    this.styleId = styleId;
    this.colorId = colorId;
    this.sizeId = sizeId;
  }

  /**
   * 706A35130 58 A27
   * 807K2D280
   * @return
   */
  @Override
  public String getEpc() {
    StringBuffer epcStr = new StringBuffer("");
    epcStr.append(this.uniqueCode.substring(0, 3));
    char c = this.uniqueCode.charAt(3);
    epcStr.append(Integer.toHexString(c));
   // epcStr.append(this.uniqueCode.substring(4, 11));
    epcStr.append(this.uniqueCode.substring(4, 5));
    c = this.uniqueCode.charAt(5);
    epcStr.append(Integer.toHexString(c));
    epcStr.append(this.uniqueCode.substring(6,11));
    c = this.uniqueCode.charAt(11);
    epcStr.append(Integer.toHexString(c));

    epcStr.append(this.uniqueCode.substring(12, 14));
    epcStr.append(this.uniqueCode.substring(14));
    this.epc = epcStr.toString()+ CommonUtil.produceIntToString(0, this.getEpcLength() - epcStr.length());
    return this.epc;
  }

  @Override
  public int getEpcLength() {
    return 24;
  }

  @Override
  public String getSecretEpc() {
    //System.out.println("唯一码："+this.uniqueCode+"; EPC："+this.epc);
		this.getEpc();
    return EpcSecretUtil.encodeEpc(this.epc);
  }

  @Override
  public void setSecretEpc(String secretEpc) {

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
    else
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
    return this.styleId+this.colorId+this.sizeId;
  }



  @Override
  public int getSerialLength() {
    return 5;
  }

  @Override
  public int getStyleLength() {
    return 9;
  }

  @Override
  public int getColorLength() {
    return 2;
  }

  @Override
  public int getSizeLength() {
    return 3;
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
    //if(temp>99 && temp <1000) return ""+temp;
    //throw new Exception("sizeId can not be Integer!");
  }

  @Override
  public String[] getSizeConfig() {
    return new String[0];
  }

  @Override
  public String getTypeName() {
    return "QiPai";
  }

  @Override
  public String getImagePath(String styleId, String colorId, String sizeId) {
	  return "/images/style_color/"+styleId+colorId+".jpg";
  }

  @Override
  public String getServerUrl() {
    return null;
  }

  @Override
  public String getClientUpdateFilePath() {
    return null;
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
