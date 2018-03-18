package com.casesoft.dmc.extend.tag;

import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.secret.EpcSecretUtil;

public class Zuo2Tag extends AbstractBaseTag  {
  private String secretEpc;

  private static final char separator = ';';

  public Zuo2Tag() {
    super();
  }

  public Zuo2Tag(String styleId, String colorId, String sizeId) {
    super();
    this.styleId = styleId;
    this.colorId = colorId;
    this.sizeId = sizeId;
  }

  /**
   * HFY4101B 088 S37 00005
   */
  @Override
  public String getEpc() {
    // String indexStr = PropertyUtil.getValue("Sample_Code_Char_Index");
    // int[] indexChars = new int[]{0,1,2,7,8,9,10,11};
    StringBuffer epcStr = new StringBuffer("");
    char c = 'a';
    c = this.styleId.charAt(0);
    epcStr.append(Integer.toHexString(c));
    c = this.styleId.charAt(1);
    epcStr.append(Integer.toHexString(c));
    c = this.styleId.charAt(2);
    epcStr.append(Integer.toHexString(c));
    epcStr.append(this.styleId.substring(3, 7));
    c = this.styleId.charAt(7);
    epcStr.append(Integer.toHexString(c));
    if (this.styleId.length() == 9) {
      c = this.styleId.charAt(8);
      epcStr.append(Integer.toHexString(c));
    }
    c = separator;
    epcStr.append(Integer.toHexString(c));

    c = this.colorId.charAt(0);
    epcStr.append(Integer.toHexString(c));
    c = this.colorId.charAt(1);
    epcStr.append(Integer.toHexString(c));
    c = this.colorId.charAt(2);
    epcStr.append(Integer.toHexString(c));

    // c = separator;
    // epcStr.append(Integer.toHexString(c));

    c = this.sizeId.charAt(0);
    epcStr.append(Integer.toHexString(c));
    epcStr.append(this.sizeId.substring(1));

    // c = separator;
    // epcStr.append(Integer.toHexString(c));

    // epcStr.append(this.uniqueCode.split("" + separator)[3]);
    String sku = this.styleId + separator + this.colorId + this.sizeId;
    epcStr.append(this.uniqueCode.substring(sku.length()));

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
    // String sku = this.styleId + separator + this.colorId + separator + this.sizeId + separator;
    String sku = this.styleId + separator + this.colorId + this.sizeId;
    this.uniqueCode = sku + CommonUtil.produceIntToString(startNo + i - 1, this.getSerialLength());
    return uniqueCode;
  }

  @Override
  public String setUniqueCode(String uniqueCode) {
     this.uniqueCode = uniqueCode;
    int cIndex = this.uniqueCode.indexOf(separator);
    this.styleId = this.uniqueCode.substring(0,cIndex);
    this.colorId = this.uniqueCode.substring(cIndex+1,cIndex+3);
    if(this.colorId.startsWith("0"))
      this.colorId = this.colorId.substring(1);
    this.sizeId = this.uniqueCode.substring(cIndex+4,cIndex+7);
    if(this.sizeId.startsWith("Z"))
      this.sizeId = this.sizeId.substring(1);
    return this.uniqueCode;
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
  public String getSerialNo() {
    String sku = this.getSku();
    return this.uniqueCode.substring(sku.length()+1);
  }

  @Override
  public String getStyleId() throws Exception{
    if(this.isNotBlank(this.styleId))
      return this.styleId;
    else if(this.isNotBlank(this.uniqueCode)) {
      this.setUniqueCode(this.uniqueCode);
      return this.styleId;
    } else {
      throw new Exception("styleId can not be Null!");
    }
  }

  @Override
  public String getColorId() throws Exception{
    //return this.colorId;
    if(this.isNotBlank(this.colorId)) {
      if (this.colorId.startsWith("0"))
        this.colorId = this.colorId.substring(1);
      return this.colorId;
    }
    else if(this.isNotBlank(this.uniqueCode)) {
      this.setUniqueCode(this.uniqueCode);
      return this.colorId;
    } else {
      throw new Exception("colorId can not be Null!");
    }
  }

  @Override
  public String getSizeId() throws Exception{
    //return this.sizeId;
    if(this.isNotBlank(this.sizeId)) {
      if(this.sizeId.startsWith("Z"))
        this.sizeId = this.sizeId.substring(1);
      return this.sizeId;
    }
    else if(this.isNotBlank(this.uniqueCode)) {
      this.setUniqueCode(this.uniqueCode);
      return this.sizeId;
    } else {
      throw new Exception("sizeId can not be Null!");
    }
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
        "35", "36", "37","38","000" };
  }

  @Override
  public String getTypeName() {
    return "Zuo-New";
  }

  @Override
  public String getServerUrl() throws Exception {
    return PropertyUtil.getValue("zuo2_dataServer_url");
  }


  @Override
  public String getClientUpdateFilePath() throws Exception {
    return PropertyUtil.getValue("zuo2_updateFile_Path");
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
