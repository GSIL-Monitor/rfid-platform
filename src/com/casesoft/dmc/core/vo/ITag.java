package com.casesoft.dmc.core.vo;

public interface ITag {

	String getEpc();

	String getEpc(String uniqueCode);

	int getEpcLength();

	String getSecretEpc(String epc);

	String getSecretEpc();

	void setSecretEpc(String secretEpc);

	String getUniqueCode(int startNo, int i);

	String getUniqueCodeBySku(int startNo, int i, String sku);

	String getUniqueCode();

	String setUniqueCode(String uniqueCode);

	String getSku();

	String getStyleId() throws Exception;

	String getColorId() throws Exception;

	String getSizeId() throws Exception;

	String getSerialNo() throws Exception;

	int getSerialLength();

	int getStyleLength();

	int getColorLength();

	int getSizeLength();

	void setStyleId(String styleId);

	void setColorId(String colorId);

	void setSizeId(String sizeId);

	String convertToStyle(int temp) throws Exception;

	String convertToColor(int temp) throws Exception;

	String convertToSize(int temp) throws Exception;

	String[] getSizeConfig();

	String getTypeName();
  
  void setSku(String sku);

	String getImagePath(String styleId, String colorId, String sizeId);

	boolean matcherStyleId(String styleId);

	boolean matcherColorId(String colorId);

	boolean matcherSizeId(String sizeId);

	String getServerUrl() throws Exception;

	String getClientUpdateFilePath() throws Exception;

	String getSkuByUniqueCode(String uniqueCode);

	String getStyleIdByUniqueCode(String uniqueCode);

	String getColorIdByUniqueCode(String uniqueCode);

	String getSizeIdByUniqueCode(String uniqueCode);

  boolean isUniqueCodeStock();
  boolean isImportUniqueCode();
}
