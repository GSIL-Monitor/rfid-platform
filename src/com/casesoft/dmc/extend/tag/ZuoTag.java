package com.casesoft.dmc.extend.tag;

import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.secret.EpcSecretUtil;

@Deprecated
public class ZuoTag extends AbstractBaseTag {
  private String styleId;
  private String colorId;
  private String sizeId;

  private String uniqueCode;
  private String epc;
  private String secretEpc;

  public ZuoTag() {
    super();
  }

  public ZuoTag(String styleId, String colorId, String sizeId) {
    super();
    this.styleId = styleId;
    this.colorId = colorId;
    this.sizeId = sizeId;
  }

  @Override
  public String getEpc() {
    // String indexStr = PropertyUtil.getValue("Sample_Code_Char_Index");
    // int[] indexChars = new int[]{0,1,2,7,8,9,10,11};
    StringBuffer epcStr = new StringBuffer("");

    char c = this.uniqueCode.charAt(0);
    epcStr.append(Integer.toHexString(c));
    c = this.uniqueCode.charAt(1);
    epcStr.append(Integer.toHexString(c));
    c = this.uniqueCode.charAt(2);
    epcStr.append(Integer.toHexString(c));

    epcStr.append(this.uniqueCode.substring(3, 7));
    c = this.uniqueCode.charAt(7);
    epcStr.append(Integer.toHexString(c));
    c = this.uniqueCode.charAt(8);
    epcStr.append(Integer.toHexString(c));
    c = this.uniqueCode.charAt(9);
    epcStr.append(Integer.toHexString(c));
    c = this.uniqueCode.charAt(10);
    epcStr.append(Integer.toHexString(c));
    c = this.uniqueCode.charAt(11);
    epcStr.append(Integer.toHexString(c));

    epcStr.append(this.uniqueCode.substring(12, 19));

    this.epc = epcStr.toString() + CommonUtil.produceIntToString(0, 32 - epcStr.length());
    return this.epc;
  }

  @Override
  public String getSecretEpc() {
    return EpcSecretUtil.encodeEpc(this.epc);
  }

  /**
   * HFY4101B088S3700005
   */
  @Override
  public String getUniqueCode(int startNo, int i) {
    if (this.colorId.length() == 2) {
      this.colorId = "0" + this.colorId;
    }
    if (this.sizeId.length() == 2) {
      this.sizeId = "Z" + this.sizeId;
    }
    String sku = this.styleId + this.colorId + this.sizeId;
    this.uniqueCode = sku + CommonUtil.produceIntToString(startNo + i - 1, this.getSerialLength());
    return uniqueCode;
  }

  @Override
  public String setUniqueCode(String uniqueCode) {
    return this.uniqueCode = uniqueCode;
  }

  @Override
  public String getSku() {
    if (this.colorId.length() == 2) {
      this.colorId = "0" + this.colorId;
    }
    if (this.sizeId.length() == 2) {
      this.sizeId = "Z" + this.sizeId;
    }
    String sku = this.styleId + this.colorId + this.sizeId;
    return sku;
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
  public int getStyleLength() {
    return 8;
  }

  @Override
  public int getColorLength() {
    return 3;
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
  public int getSerialLength() {
    return 5;
  }

  @Override
  public String getUniqueCode() {

    return this.uniqueCode;
  }

  @Override
  public int getEpcLength() {
    return 32;
  }

  @Override
  public String convertToStyle(int temp) throws Exception {
    throw new Exception("styleId can not be Integer!");
  }

  @Override
  public String convertToColor(int temp) throws Exception {
    String cId = "";
    if (temp < 10) {
      cId = CommonUtil.convertIntToString(temp, 2);
    } else {
      cId = "" + temp;
    }
    return cId;
  }

  @Override
  public String convertToSize(int temp) throws Exception {
    if (temp == 0)
      return CommonUtil.convertIntToString(temp, 3);
    return "" + temp;
  }

  @Override
  public String[] getSizeConfig() {
    return new String[] { "S65", "S70", "S75", "S80", "S85", "S90", "30", "31", "32", "33", "34",
        "35", "36", "000" };
  }

  @Override
  public String getTypeName() {
    return "Zuo-Old";
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
