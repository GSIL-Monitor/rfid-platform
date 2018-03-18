package com.casesoft.dmc.extend.tag;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.casesoft.dmc.core.util.secret.EpcSecretUtil;

/**
 * Created by WingLi on 2015-03-10.
 */
public class BurtonTag extends AbstractBaseTag{
    @Override
    public String getEpc() {
        return null;
    }

    @Override
    public int getEpcLength() {
        return 0;
    }

    @Override
    public String getSecretEpc() {
        return null;
    }

    @Override
    public void setSecretEpc(String secretEpc) {

    }

    @Override
    public String getUniqueCode(int startNo, int i) {
        return null;
    }

    @Override
    public String getUniqueCode() {
        return null;
    }

    @Override
    public String setUniqueCode(String uniqueCode) {
        return null;
    }

    @Override
    public String getSku() {
        return null;
    }

    @Override
    public int getSerialLength() {
        return 5;
    }

    @Override
    public int getStyleLength() {
        return 0;
    }

    @Override
    public int getColorLength() {
        return 0;
    }

    @Override
    public int getSizeLength() {
        return 0;
    }

    @Override
    public void setStyleId(String styleId) {

    }

    @Override
    public void setColorId(String colorId) {

    }

    @Override
    public void setSizeId(String sizeId) {

    }

    @Override
    public String convertToStyle(int temp) throws Exception {
        return null;
    }

    @Override
    public String convertToColor(int temp) throws Exception {
        return null;
    }

    @Override
    public String convertToSize(int temp) throws Exception {
        return null;
    }

    @Override
    public String[] getSizeConfig() {
        return new String[0];
    }

    @Override
    public String getTypeName() {
        return "Burton";
    }

    @Override
    public String getServerUrl() throws Exception {
        return null;
    }

    @Override
    public String getClientUpdateFilePath() throws Exception {
        return PropertyUtil.getValue("marcopolo_updateFile_Path");
    }

	@Override
	public String getUniqueCodeBySku(int startNo, int i, String sku) {
        String epcId = CacheManager.getProductByCode(sku).getId();
		return epcId+CommonUtil.produceIntToString(startNo + i - 1, this.getSerialLength())+"01";
	}

	@Override
	public String getEpc(String uniqueCode) {
		int length=24-uniqueCode.length();
		for(int i=0;i<length;i++){
			uniqueCode+="0";	
		}
		return uniqueCode;
	}

	@Override
	public String getSecretEpc(String epc) {
		// TODO Auto-generated method stub
		return EpcSecretUtil.encodeEpc(epc);
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

	@Override
	public void setSku(String sku) {
		// TODO Auto-generated method stub
		
	}
}
