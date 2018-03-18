package com.casesoft.dmc.extend.tag;

import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.secret.EpcSecretUtil;

/**
 * Created by WingLi on 2015-05-12.
 */
public class OChirly2Tag extends AbstractBaseTag {
    private String secretEpc;

    public OChirly2Tag() {
        super();
    }


    public OChirly2Tag(String styleId, String colorId, String sizeId) {
        super();
        this.styleId = styleId;
        this.colorId = colorId;
        this.sizeId = sizeId;
    }


    // Y1110007011 0B5 9
    @Override
    public String getEpc() {
        StringBuffer epcStr = new StringBuffer("");

        char c = this.uniqueCode.charAt(0);
        epcStr.append(Integer.toHexString(c));

        epcStr.append(this.uniqueCode.substring(1,11));

        epcStr.append(Integer.toHexString(this.uniqueCode.charAt(11)));
        epcStr.append(Integer.toHexString(this.uniqueCode.charAt(12)));
        epcStr.append(Integer.toHexString(this.uniqueCode.charAt(13)));

        epcStr.append(this.uniqueCode.substring(14));

        this.epc = epcStr.toString() + CommonUtil.produceIntToString(0, 24 - epcStr.length());
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
        return this.uniqueCode;
    }

    @Override
    public String setUniqueCode(String uniqueCode) {
        this.styleId = uniqueCode.substring(0,11);
        this.colorId = uniqueCode.substring(11,14);
        this.sizeId = uniqueCode.substring(15,16);
        return this.uniqueCode;
    }

    @Override
    public String getSku() {
        return this.styleId+this.colorId+this.sizeId;
    }

    @Override
    public int getSerialLength() {
        return 4;
    }

    @Override
    public int getStyleLength() {
        return 11;
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
        return CommonUtil.produceIntToString(temp, 3);
    }

    @Override
    public String convertToSize(int temp) throws Exception {
        return ""+temp;
    }

    @Override
    public String[] getSizeConfig() {
        return new String[0];
    }

    @Override
    public String getTypeName() {
        return "OChirly-New";
    }

    @Override
    public String getServerUrl() throws Exception {
        return null;
    }

    @Override
    public String getClientUpdateFilePath() throws Exception {
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
