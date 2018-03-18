package com.casesoft.dmc.model.tag;


import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * Created by session on 2017-05-24.
 */
public class ExcelReader {
	@Excel(name="唯一码",needMerge = true)
	private String code;
	@Excel(name="条码",needMerge = true)
	private String barcode;


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
}
