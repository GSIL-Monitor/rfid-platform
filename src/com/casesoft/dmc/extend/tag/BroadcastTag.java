package com.casesoft.dmc.extend.tag;

import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.secret.EpcSecretUtil;

public class BroadcastTag extends AbstractBaseTag {

  public BroadcastTag() {
    super();
  }

  public BroadcastTag(String styleId, String colorId, String sizeId) {
    super();
    this.styleId = styleId;
    this.colorId = colorId;
    this.sizeId = sizeId;
  }

  /**
   * BDH2EY0588 W10 3 0001
   */
  @Override
  public String getEpc() {
    StringBuffer epcStr = new StringBuffer("");

    char c = this.uniqueCode.charAt(0);
    epcStr.append(Integer.toHexString(c));
    c = this.uniqueCode.charAt(1);
    epcStr.append(Integer.toHexString(c));
    c = this.uniqueCode.charAt(2);
    epcStr.append(Integer.toHexString(c));

    epcStr.append(this.uniqueCode.substring(3, 4));
    c = this.uniqueCode.charAt(4);
    epcStr.append(Integer.toHexString(c));
    c = this.uniqueCode.charAt(5);
    epcStr.append(Integer.toHexString(c));

    epcStr.append(this.uniqueCode.substring(6, 10));
    c = this.uniqueCode.charAt(10);
    epcStr.append(Integer.toHexString(c));

    epcStr.append(this.uniqueCode.substring(11, 18));

    this.epc = epcStr.toString();
    return this.epc;
  }

  @Override
  public int getEpcLength() {
    return 24;
  }

  @Override
  public String getSecretEpc() {
    return EpcSecretUtil.encodeEpc(this.epc);
  }

  @Override
  public String getUniqueCode(int startNo, int i) {
    String sku = this.styleId + this.colorId + this.sizeId;
    this.uniqueCode = sku + CommonUtil.produceIntToString(startNo + i - 1, this.getSerialLength());
    return uniqueCode;
  }

  @Override
  public String getUniqueCode() {
    return this.uniqueCode;
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
    return 4;
  }

  @Override
  public int getStyleLength() {
    return 10;
  }

  @Override
  public int getColorLength() {
    return 3;
  }

  @Override
  public int getSizeLength() {
    return 1;
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
    throw new Exception("colorId can not be Integer!");
  }

  @Override
  public String convertToSize(int temp) throws Exception {
    return CommonUtil.convertIntToString(temp, getSizeLength());
  }

  @Override
  public String[] getSizeConfig() {
    return new String[] { "1", "2", "3" };
  }

  @Override
  public String getTypeName() {
    return "Broadcast";
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
  public void setSecretEpc(String secretEpc) {
    // TODO Auto-generated method stub

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
