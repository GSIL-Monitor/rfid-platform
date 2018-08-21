package com.casesoft.dmc.extend.tag;


/**
 * Created by pc on 2015/3/10.
 */
public class MarcopoloTag extends AbstractBaseTag {
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
    public String getSecretEpc(String uniqueCode) {
        return uniqueCode;
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
        return 0;
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
        return null;
    }


	@Override
	public String getEpc(String uniqueCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUniqueCodeBySku(int startNo, int i, String sku) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSku(String sku) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getServerUrl() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClientUpdateFilePath() throws Exception {
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
